package me.riching.goldprice.service;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;

import me.riching.goldprice.dao.GoldPriceDao;
import me.riching.goldprice.dao.WarningConditionDao;
import me.riching.goldprice.model.GoldPrice;
import me.riching.goldprice.model.GoldPriceSnapShot;
import me.riching.goldprice.model.GoldPriceSnapShotCache;
import me.riching.goldprice.model.GoldPriceStat;
import me.riching.goldprice.model.WarningCondition;
import me.riching.goldprice.utils.RemindUtils;

@Service
public class GoldPriceCrawlerService {

	private static Logger logger = LoggerFactory.getLogger(GoldPriceCrawlerService.class);

	private static String url = "https://www.avicks.com/";

	private static final int timeoutMillis = 25000;

	private static final AtomicBoolean IS_WARNING = new AtomicBoolean(false);

	private GoldPriceSnapShotCache cache = new GoldPriceSnapShotCache();

	@Autowired
	private GoldPriceDao goldPriceDao;
	@Autowired
	private WarningConditionDao warningConditionDao;

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
			logger.info("crawl gold price " + priceNum);
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
		this.goldPriceDao.insert(price);
		cache.addSnapShot(new GoldPriceSnapShot(priceNum));
		// 判断是否进入警戒区
		WarningCondition warning = this.isWarning(priceNum);
		if (null == warning) {
			IS_WARNING.set(false);
			return;
		}
		IS_WARNING.set(true);
		// 判断是否需要发送提醒
		boolean isSendRemind = this.sendRemind();
		if (isSendRemind)
			RemindUtils.sendRemind("hello world");
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
	private boolean sendRemind() {
		return this.cache.isIncreasing() || this.cache.isDecline() || this.cache.isCumulativeChange()
				|| this.cache.isBigStep() || this.cache.isContinuityChangeTwice() || this.isBiggerThanBefore(10, 0.3d);
	}

	private boolean isBiggerThanBefore(int timesAgo, double step) {
		List<GoldPrice> result = this.goldPriceDao.getByLimit(timesAgo, 1);
		if (CollectionUtils.isEmpty(result))
			return false;
		return Math.abs(result.get(0).getPrice() - this.cache.getLast().getCurrentGoldPrice()) > step;
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
