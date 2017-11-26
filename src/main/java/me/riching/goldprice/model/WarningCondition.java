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
	private double differ;
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

	public double getDiffer() {
		return differ;
	}

	public void setDiffer(double differ) {
		this.differ = differ;
	}

	public boolean isMatch(double beforePrice, double currentPrice) {
		switch (operator) {
		case 1:
			return currentPrice - beforePrice - differ > 0;
		case 2:
			return currentPrice - beforePrice + differ < 0;
		case 3:
			return currentPrice == (beforePrice + differ);
		}
		return false;
	}

	/**
	 * 将警戒条件转化成可读的文本
	 * 
	 * @return
	 */
	public String toMessage() {
		StringBuilder sb = new StringBuilder("当前金价");
		if (this.operator == 1)
			sb.append("大于");
		else
			sb.append("小于");
		sb.append("过去");
		sb.append(startHour).append("-").append(endHour).append("小时的");
		switch (valueType) {
		case 1:
			sb.append("最大值");
			break;
		case 2:
			sb.append("最小值");
			break;
		case 3:
			sb.append("均值");
			break;
		}
		sb.append("价格").append(differ);
		return sb.toString();
	}

}
