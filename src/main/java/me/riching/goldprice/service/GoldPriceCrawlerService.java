package me.riching.goldprice.service;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;

import me.riching.goldprice.cache.GoldPriceSnapShotCache;
import me.riching.goldprice.cache.RemindTimeCache;
import me.riching.goldprice.dao.GoldPriceDao;
import me.riching.goldprice.dao.WarningConditionDao;
import me.riching.goldprice.model.GoldPrice;
import me.riching.goldprice.model.GoldPriceSnapShot;
import me.riching.goldprice.model.GoldPriceStat;
import me.riching.goldprice.model.WarningCondition;

@Service
public class GoldPriceCrawlerService {

	private static Logger logger = LoggerFactory.getLogger(GoldPriceCrawlerService.class);

	private static String url = "https://www.avicks.com/";

	private static final int timeoutMillis = 25000;

	private static final AtomicBoolean IS_WARNING = new AtomicBoolean(false);

	private GoldPriceSnapShotCache cache = new GoldPriceSnapShotCache();

	private RemindTimeCache remindTimeCache = RemindTimeCache.getInstance();

	@Autowired
	private GoldPriceDao goldPriceDao;
	@Autowired
	private WarningConditionDao warningConditionDao;
	@Autowired
	private MailService mailService;

	@Scheduled(cron = "*/30 * * * * ?")
	public void crawlGoldPriceSchedule() {
		Calendar calendar = Calendar.getInstance();
		// 每天六点-七点休市
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour == 6)
			return;
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		// 周六四点至周一七点休市
		if (week == Calendar.SUNDAY || (week == Calendar.SATURDAY && hour >= 4)
				|| (week == Calendar.MONDAY && hour < 7))
			return;
		try {
			Document doc = Jsoup.parse(new URL(url), timeoutMillis);
			String priceStr = doc.select("div.ks-in-tgprice > dl > dt > b").text();
			if (StringUtils.isBlank(priceStr))
				return;
			double priceNum = NumberUtils.parseNumber(priceStr, Double.class);
			if (priceNum <= 0)
				return;
			saveAndAnalysisPrice(priceNum);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public List<GoldPrice> getPriceByDate(String startDate, String endDate) {
		return this.goldPriceDao.getByDate(startDate, endDate);
	}

	public List<GoldPriceStat> getPriceStat(String startDate, String endDate, int startHour, int endHour) {
		return this.goldPriceDao.getPriceStat(startDate, endDate, startHour, endHour);
	}

	public void saveAndAnalysisPrice(double priceNum) {
		GoldPrice price = new GoldPrice();
		price.setPrice(priceNum);
		int result = this.goldPriceDao.insert(price);
		logger.info("insert gold price to db result " + result);
		cache.addSnapShot(new GoldPriceSnapShot(priceNum));
		// 判断是否进入警戒区
		WarningCondition warning = this.isWarning(priceNum);
		if (null == warning) {
			IS_WARNING.set(false);
			return;
		}
		IS_WARNING.set(true);
		// 判断是否需要发送提醒
		String remindMsg = this.generateRemindMessage();
		if (StringUtils.isNoneBlank(remindMsg)) {
			boolean inc = remindMsg.contains("增") || remindMsg.contains("上");
			// 如果金价是上升趋势，并且出现新高，则触发卖出提醒
			if (inc && warning.getOperator() == 1 && this.remindTimeCache.getSoldTimes() > 0) {
				remindTimeCache.decSold();
				this.mailService.sendHtmlMail("金价卖出提醒", this.toRemindMessage(priceNum, warning, remindMsg));
			}
			if (!inc && warning.getOperator() == 2 && this.remindTimeCache.getBuyTimes() > 0) {
				remindTimeCache.decBuy();
				this.mailService.sendHtmlMail("金价买入提醒", this.toRemindMessage(priceNum, warning, remindMsg));
			}
		}
	}

	/**
	 * 判断当前价格是否进入警戒区
	 * 
	 * @return
	 */
	private WarningCondition isWarning(double currentPrice) {
		List<WarningCondition> conditions = this.warningConditionDao.get();
		if (CollectionUtils.isEmpty(conditions))
			return null;
		for (WarningCondition condition : conditions) {
			double beforePrice = this.goldPriceDao.getByWarningCondition(condition);
			if (condition.isMatch(beforePrice, currentPrice))
				return condition;
		}
		return null;
	}

	/**
	 * 判断是否发送提醒
	 * 
	 * @return
	 */
	private String generateRemindMessage() {
		StringBuilder sb = new StringBuilder();
		String msg = this.cache.isIncreasing();
		if (StringUtils.isNotEmpty(msg))
			sb.append(msg).append('\t');
		msg = this.cache.isDecline();
		if (StringUtils.isNotEmpty(msg))
			sb.append(msg).append('\t');
		msg = this.cache.isCumulativeChange();
		if (StringUtils.isNotEmpty(msg))
			sb.append(msg).append('\t');
		msg = this.cache.isBigStep();
		if (StringUtils.isNotEmpty(msg))
			sb.append(msg).append('\t');
		msg = this.cache.isContinuityChangeTwice();
		if (StringUtils.isNotEmpty(msg))
			sb.append(msg).append('\t');
		msg = this.isBiggerThanBefore(10, 0.3d);
		if (StringUtils.isNotEmpty(msg))
			sb.append(msg).append('\t');
		return sb.toString();
	}

	private String isBiggerThanBefore(int timesAgo, double step) {
		List<GoldPrice> result = this.goldPriceDao.getByLimit(timesAgo, 1);
		if (CollectionUtils.isEmpty(result))
			return null;
		double differ = this.cache.getLast().getCurrentGoldPrice() - result.get(0).getPrice();
		if (differ > step)
			return "金价相比5分钟前，已上升超过" + step;
		if (differ < -step)
			return "金价相比5分钟前，已下降超过" + step;
		return null;
	}

	public String toRemindMessage(double currentPrice, WarningCondition warningCondition, String remindMessage) {
		StringBuilder sb = new StringBuilder();
		sb.append("<bold>当前金价：</bold>").append(currentPrice).append("<br/>");
		sb.append("<bold>当前时间：</bold>").append(DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss"))
				.append("<br/>");
		sb.append("<bold>进入警戒区的原因：</bold>").append(warningCondition.toMessage()).append("<br/>");
		sb.append("<bold>触发提醒的原因：</bold>").append(remindMessage).append("<br/>");
		return sb.toString();
	}

	public static void main(String[] args) {
		// GoldPriceCrawlerService crawler = new GoldPriceCrawlerService();
		// crawler.crawlGoldPriceSchedule();
		// System.out.println(logger.getName());
		// logger.info("test");
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
	}
}
