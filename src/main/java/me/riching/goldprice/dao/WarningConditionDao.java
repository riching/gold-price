package me.riching.goldprice.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import me.riching.goldprice.model.WarningCondition;

public class WarningConditionDao extends NamedParameterJdbcDaoSupport {

	private static final BeanPropertyRowMapper<WarningCondition> MAPPER = new BeanPropertyRowMapper<>(
			WarningCondition.class);
	private static final String INSERT = "insert into warning_condition(start_hour, end_hour, value_type, operator, differ) values(?, ?, ?, ?, ?)";

	public int insert(WarningCondition condition) {
		return this.getJdbcTemplate().update(INSERT, condition.getStartHour(), condition.getEndHour(),
				condition.getValueType(), condition.getOperator(), condition.getDiffer());
	}

	private static final String GET = "select id, start_hour, end_hour, value_type, operator, differ, status, update_time from warning_condition where status=0";

	public List<WarningCondition> get() {
		return this.getJdbcTemplate().query(GET, MAPPER);
	}

	public static final String UP_STATUS = "update warning_condition set status=1 where id in (:ids)";

	public int batchDel(List<Integer> ids) {
		return this.getNamedParameterJdbcTemplate().update(UP_STATUS, new MapSqlParameterSource("ids", ids));
	}
}
