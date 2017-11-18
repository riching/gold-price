package me.riching.goldprice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import me.riching.goldprice.model.WarningCondition;

@Service
public class WarningService {

	Logger logger = LoggerFactory.getLogger(WarningService.class);

	public void SendWarning(WarningCondition con, double currentGoldPrice) {
		logger.info("send warning con {} current price {}", con, currentGoldPrice);
	}
}
