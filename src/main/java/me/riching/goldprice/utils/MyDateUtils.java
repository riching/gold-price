package me.riching.goldprice.utils;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class MyDateUtils {

	public static final String DATE_PATTERN_FULL = "yyyy-MM-dd HH:mm:ss";

	public static Pair<String, String> getWeekRange() {
		Calendar calendar = Calendar.getInstance();
		String end = DateFormatUtils.format(calendar, DATE_PATTERN_FULL);
		calendar.add(Calendar.DATE, -7);
		String start = DateFormatUtils.format(calendar, DATE_PATTERN_FULL);
		return new ImmutablePair<String, String>(start, end);
	}
}
