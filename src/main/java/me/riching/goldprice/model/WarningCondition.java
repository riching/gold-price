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

}
