package me.riching.goldprice.dpac;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;

@Service
public class DpacCrawler {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(DpacCrawler.class);

	private static final String URL_DPAC = "http://www.dpac.gov.cn/cpqxcj/VehicleComplaintPublicity.aspx";

	private static final String RESULT_PATH = "/tmp/tousu.html";

	private static final Random random = new Random();

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Scheduled(cron = "1 1 0 * * ?")
	public void crawDpac() {
		List<Element> list = new ArrayList<>();
		Map<String, String> data = new HashMap<>();
		data.put("__EVENTTARGET", "pagelist");
		data.put("__EVENTARGUMENT", "1");
		for (int i = 2; i < 3; i++) {
			try {
				Connection conn = Jsoup.connect(URL_DPAC);
				conn.data(data);
				Document doc = conn.post();
				String __VIEWSTATE = doc.getElementById("__VIEWSTATE").val();
				String __VIEWSTATEGENERATOR = doc.getElementById("__VIEWSTATEGENERATOR").val();
				String __EVENTVALIDATION = doc.getElementById("__EVENTVALIDATION").val();

				Element table = doc.getElementById("tdRepeater");
				Elements lines = table.child(0).children();
				int index = 0;
				for (Element line : lines) {
					index++;
					if (index <3) continue;
					if (justFord() && StringUtils.indexOf(line.child(2).text(), "福特") < 0 && StringUtils.indexOf(line.child(2).text(), "长安") < 0)
						continue;
					Element nameElement = line.child(0);
					nameElement.text(nameElement.child(0).val());
					if (StringUtils.indexOf(line.child(2).text(), "福特") > 0)
						list.add(0, line);
					else
						list.add(line);
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

				data.put("__EVENTARGUMENT", i + "");
				data.put("__VIEWSTATE", __VIEWSTATE);
				data.put("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR);
				data.put("__EVENTVALIDATION", __EVENTVALIDATION);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		try {
			Template template = this.freeMarkerConfigurer.createConfiguration().getTemplate("dacp.ftl");
			FileWriter fw = new FileWriter(new File(RESULT_PATH));
			Map<String, Object> map = new HashMap<>();
			map.put("date", DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd"));
			map.put("lines", list);
			template.process(map, fw);
			fw.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public boolean justFord() {
		return random.nextInt(100) < 50;
	}

	public static void main(String[] args) throws Exception {
		// Map<String, String> data = new HashMap<>();
		// data.put("__EVENTTARGET", "pagelist");
		// for (int i = 2; i < 5; i++) {
		// Connection conn = Jsoup.connect(URL_DPAC);
		// conn.data(data);
		// Document doc = conn.post();
		// String __VIEWSTATE = doc.getElementById("__VIEWSTATE").val();
		// String __VIEWSTATEGENERATOR =
		// doc.getElementById("__VIEWSTATEGENERATOR").val();
		// String __EVENTVALIDATION = doc.getElementById("__EVENTVALIDATION").val();
		//
		// Element table = doc.getElementById("tdRepeater");
		// System.out.println("==== " + i);
		// System.out.println(table.child(0).child(1));
		// try {
		// Thread.sleep(2000);
		// } catch (Exception e) {
		// logger.error(e.getMessage(), e);
		// }
		//
		// data.put("__EVENTARGUMENT", i + "");
		// data.put("__VIEWSTATE", __VIEWSTATE);
		// data.put("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR);
		// data.put("__EVENTVALIDATION", __EVENTVALIDATION);
		// }
		DpacCrawler dc = new DpacCrawler();
		dc.crawDpac();
	}
}
