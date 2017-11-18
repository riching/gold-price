package me.riching.goldprice.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import me.riching.goldprice.model.GoldPrice;
import me.riching.goldprice.model.GoldPriceStat;
import me.riching.goldprice.service.GoldPriceCrawlerService;

@Controller
@RequestMapping
public class GoldPriceShowController {

	@Autowired
	private GoldPriceCrawlerService crawlerService;

	@RequestMapping()
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}

	@RequestMapping("price")
	public ModelAndView price(@RequestParam(value = "page", required = false, defaultValue = "price_line") String page) {
		ModelAndView mav = new ModelAndView(page);
		return mav;
	}

	@RequestMapping("sync")
	public String sync() {
		crawlerService.crawlGoldPriceSchedule();
		return "redirect:/";
	}

	@RequestMapping("pricedata")
	@ResponseBody
	public String getPrice(@RequestParam("start") String startDate, @RequestParam("end") String endDate)
			throws Exception {
		List<GoldPrice> prices = this.crawlerService.getPriceByDate(startDate, endDate);
		List<String> labels = new ArrayList<>();
		List<Double> values = new ArrayList<>();
		for (GoldPrice price : prices) {
			labels.add(DateFormatUtils.format(price.getCreateTime(), "dd-HH:mm"));
			values.add(price.getPrice());
		}
		Map<String, Object> result = new HashMap<>();
		result.put("labels", labels);
		result.put("values", values);
		return new ObjectMapper().writeValueAsString(result);
	}

	@RequestMapping("pricestat")
	@ResponseBody
	public String getPriceStat(@RequestParam("start") String startDate, @RequestParam("end") String endDate,
			@RequestParam("startHour") int startHour, @RequestParam("endHour") int endHour) throws Exception {
		List<GoldPriceStat> stats = this.crawlerService.getPriceStat(startDate, endDate, startHour, endHour);
		List<String> labels = new ArrayList<>();
		List<Double> maxPrices = new ArrayList<>();
		List<Double> minPrices = new ArrayList<>();
		List<Double> avgPrices = new ArrayList<>();
		for (GoldPriceStat stat : stats) {
			labels.add(stat.getDate());
			maxPrices.add(stat.getMaxPrice());
			minPrices.add(stat.getMinPrice());
			avgPrices.add(new BigDecimal(stat.getAvgPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		Map<String, Object> result = new HashMap<>();
		result.put("labels", labels);
		result.put("maxPrices", maxPrices);
		result.put("minPrices", minPrices);
		result.put("avgPrices", avgPrices);
		return new ObjectMapper().writeValueAsString(result);
	}

}
