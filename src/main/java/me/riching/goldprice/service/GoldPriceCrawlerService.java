package me.riching.goldprice.service;

import java.net.URL;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import me.riching.goldprice.dao.GoldPriceDao;
import me.riching.goldprice.model.GoldPrice;
import me.riching.goldprice.model.GoldPriceStat;

@Service
public class GoldPriceCrawlerService {

	private static Logger logger = LoggerFactory.getLogger(GoldPriceCrawlerService.class);

	private static String url = "https://www.avicks.com/";

	private static final int timeoutMillis = 20000;

	@Autowired
	private GoldPriceDao goldPriceDao;

	@Scheduled(cron = "*/30 * * * * ?")
	public void crawlGoldPriceSchedule() {
		Calendar calendar = Calendar.getInstance();
		// 每天六点-七点休市
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour == 6)
			return;
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		// 周六四点至周一七点休市
		if (week == Calendar.SUNDAY || (week == Calendar.SATURDAY && hour > 4) || (week == Calendar.MONDAY && hour < 7))
			return;
		try {
			Document doc = Jsoup.parse(new URL(url), timeoutMillis);
			String priceStr = doc.select("div.ks-in-tgprice > dl > dt > b").text();
			if (StringUtils.isBlank(priceStr))
				return;
			double priceNum = NumberUtils.parseNumber(priceStr, Double.class);
			if (priceNum <= 0)
				return;
			GoldPrice price = new GoldPrice();
			price.setPrice(priceNum);
			this.goldPriceDao.insert(price);
			//如果价格高于或者低于过去24小时内的平均价格0.5以上，进行提醒
			//TODO
			//如果价格高于或者低于过去6小时内的平均价格0.5以上，进行提醒
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

	public static void main(String[] args) {
		// GoldPriceCrawlerService crawler = new GoldPriceCrawlerService();
		// crawler.crawlGoldPriceSchedule();
		// System.out.println(logger.getName());
		// logger.info("test");
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
	}
}
