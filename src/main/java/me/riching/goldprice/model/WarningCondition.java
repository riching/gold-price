package me.riching.goldprice.model;

import java.io.Serializable;
import java.util.Date;

public class WarningCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private int startHour;
	private int endHour;
	/**
	 * 1:最大值 2：最小值 3：均值
	 */
	private int valueType;
	/**
	 * 1:大于 2：小于 3：等于
	 */
	private int operator;
	/**
	 * 差值大小
	 */
	private double diff;
	/**
	 * 0:有效 1：无效
	 */
	private int status;

	private Date updateTime;

	public WarningCondition() {
	}

	public WarningCondition(int startHour, int endHour) {
		this.startHour = startHour;
		this.endHour = endHour;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getValueType() {
		return valueType;
	}

	public void setValueType(int valueType) {
		this.valueType = valueType;
	}

	public int getOperator() {
		return operator;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}

	public double getDiff() {
		return diff;
	}

	public void setDiff(double diff) {
		this.diff = diff;
	}

	public boolean isMatch(double beforePrice, double currentPrice) {
		switch (operator) {
		case 1:
			return currentPrice - beforePrice - diff > 0;
		case 2:
			return currentPrice - beforePrice + diff < 0;
		case 3:
			return currentPrice == (beforePrice + diff);
		}
		return false;
	}

}
