package me.riching.goldprice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.riching.goldprice.dpac.DpacCrawler;

@Controller
@RequestMapping("test")
public class TestController {

	@Autowired
	private DpacCrawler crawler;

	@ResponseBody
	@RequestMapping("dpac")
	public String dpac() {
		this.crawler.crawDpac();
		return "succ";
	}
}
