package me.riching.goldprice.model;

import java.io.Serializable;

public class GoldPriceStat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String date;
	private double maxPrice;
	private double mminPrice;
	private double avgPrice;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public double getMminPrice() {
		return mminPrice;
	}

	public void setMminPrice(double mminPrice) {
		this.mminPrice = mminPrice;
	}

	public double getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}

}
