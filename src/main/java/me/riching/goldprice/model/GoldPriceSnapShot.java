package me.riching.goldprice.model;

import java.io.Serializable;
import java.util.Date;

public class GoldPriceSnapShot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double currentGoldPrice;

	public GoldPriceSnapShot(double currentGoldPrice, Date timestamp) {
		this.currentGoldPrice = currentGoldPrice;
		this.timestamp = timestamp;
	}

	private Date timestamp;

	public GoldPriceSnapShot(double currentGoldPrice) {
		this.currentGoldPrice = currentGoldPrice;
		this.timestamp = new Date();
	}

	public GoldPriceSnapShot() {
	}

	public double getCurrentGoldPrice() {
		return currentGoldPrice;
	}

	public void setCurrentGoldPrice(double currentGoldPrice) {
		this.currentGoldPrice = currentGoldPrice;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
