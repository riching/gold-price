package me.riching.goldprice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import me.riching.goldprice.dao.WarningConditionDao;
import me.riching.goldprice.model.WarningCondition;

@Controller
@RequestMapping("warning")
public class WarningController {

	private static final Logger logger = LoggerFactory.getLogger(WarningController.class);
	@Autowired
	private WarningConditionDao warningConditionDao;

	@RequestMapping("list")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("warning");
		List<WarningCondition> conditions = this.warningConditionDao.get();
		mav.addObject("list", conditions);
		return mav;
	}

	@RequestMapping("add")
	public String add(WarningCondition condition) {
		this.warningConditionDao.insert(condition);
		return "redirect:/warning/list";
	}

	@RequestMapping("del")
	public String del(@RequestParam("ids") String[] ids) {
		if (ArrayUtils.isNotEmpty(ids)) {
			List<Integer> intIds = new ArrayList<>();
			for (String id : ids) {
				intIds.add(NumberUtils.toInt(id));
			}
			int result = this.warningConditionDao.batchDel(intIds);
			logger.info("del warning record {} result {}", Arrays.toString(ids), result);
		}
		return "redirect:/warning/list";
	}
}
