package me.riching.goldprice.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import me.riching.goldprice.model.GoldPrice;
import me.riching.goldprice.model.GoldPriceStat;

public class GoldPriceDao extends NamedParameterJdbcDaoSupport {

	private static final BeanPropertyRowMapper<GoldPrice> rowMapper = new BeanPropertyRowMapper<GoldPrice>(
			GoldPrice.class);

	private static final BeanPropertyRowMapper<GoldPriceStat> statRowMapper = new BeanPropertyRowMapper<GoldPriceStat>(
			GoldPriceStat.class);

	private static final String SQL_INSESRT = "insert into price(price) values(?)";

	public int insert(GoldPrice price) {
		return this.getJdbcTemplate().update(SQL_INSESRT, price.getPrice());
	}

	private static final String SQL_GET = "select id, price, create_time from price order by id desc limit ?";

	public List<GoldPrice> get(int limit) {
		return this.getJdbcTemplate().query(SQL_GET, rowMapper, limit);
	}

	private static final String SQL_QUERY_BY_DATE = "select id, price, create_time from price where create_time>=? and create_time<=?";

	public List<GoldPrice> getByDate(String start, String end) {
		return this.getJdbcTemplate().query(SQL_QUERY_BY_DATE, rowMapper, start, end);
	}

	private static final String SQL_QUERY_STAT = "select max(price) as maxPrice, min(price) as minPrice, avg(price) as avgPrice, date(create_time) as date "
			+ "from price where create_time>=? and create_time<=? and hour(create_time) between ? and ? group by 4 order by 4";

	private static final String SQL_QUERY_STAT_2 = "select max(price) as maxPrice, min(price) as minPrice, avg(price) as avgPrice, date(create_time) as date "
			+ "from price where create_time>=? and create_time<=? and (hour(create_time) >= ? or hour(create_time) <= ?) group by 4 order by 4";

	public List<GoldPriceStat> getPriceStat(String startDate, String endDate, int startHour, int endHour) {
		return this.getJdbcTemplate().query(startHour > endHour ? SQL_QUERY_STAT_2 : SQL_QUERY_STAT, statRowMapper,
				startDate, endDate, startHour, endHour);
	}
}
