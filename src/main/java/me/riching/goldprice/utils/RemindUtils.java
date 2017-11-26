package me.riching.goldprice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemindUtils {

	private static final Logger logger = LoggerFactory.getLogger(RemindUtils.class);

	public static void sendRemind(String msg) {
		logger.info("send remind \n" + msg);
	}
}
