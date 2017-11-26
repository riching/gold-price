package me.riching.goldprice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoldPriceSnapShotCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int MAX_SIZE = 5;

	private List<GoldPriceSnapShot> list = new ArrayList<GoldPriceSnapShot>(MAX_SIZE);

	/**
	 * 价格如果一直递增，则一直添加，出现反转则清空
	 */
	private List<Double> incRec = new ArrayList<>();

	/**
	 * 价格如果一直递减，则一直添加，出现反转则清空
	 */
	private List<Double> decRec = new ArrayList<>();

	public void addSnapShot(GoldPriceSnapShot snapShot) {
		if (list.size() == MAX_SIZE)
			list.remove(0);
		list.add(snapShot);

		int incSize = incRec.size();
		double price = snapShot.getCurrentGoldPrice();
		if (incSize > 0 && incRec.get(incSize - 1) > price)
			incRec.clear();
		incRec.add(price);

		int decSize = decRec.size();
		if (decSize > 0 && decRec.get(decSize - 1) < price)
			decRec.clear();
		decRec.add(price);

	}

	/**
	 * 判断是否递增
	 * 
	 * @return
	 */
	public boolean isIncreasing() {
		if (list.size() < MAX_SIZE)
			return false;
		for (int i = 0; i < MAX_SIZE - 1; i++) {
			if (list.get(i + 1).getCurrentGoldPrice() <= list.get(i).getCurrentGoldPrice())
				return false;
		}
		return true;
	}

	/**
	 * 判断是否递减
	 * 
	 * @return
	 */
	public boolean isDecline() {
		if (list.size() < MAX_SIZE)
			return false;
		for (int i = 0; i < MAX_SIZE - 1; i++) {
			if (list.get(i + 1).getCurrentGoldPrice() >= list.get(i).getCurrentGoldPrice())
				return false;
		}
		return true;
	}

	/**
	 * 判断累计变化幅度是否大于0.3
	 * 
	 * @return
	 */
	public boolean isCumulativeChange() {
		int incSize = incRec.size();
		if (incSize > 2)
			return incRec.get(incSize - 1) - incRec.get(0) > 0.3;
		int decSize = decRec.size();
		if (decSize > 2)
			return decRec.get(0) - decRec.get(decSize - 1) > 0.3;
		return false;
	}

	/**
	 * 是否两次连续增长或者下降，幅度大于0.1
	 * 
	 * @return
	 */
	public boolean isContinuityChangeTwice() {
		boolean result = false;
		int size = list.size();
		if (size < 3)
			return result;
		// 是否连续两次增长超过0.1
		for (int i = size; i > size - 2; i--) {
			if ((list.get(i - 1).getCurrentGoldPrice() - list.get(i - 2).getCurrentGoldPrice()) <= 0.1) {
				result = false;
				break;
			}
		}
		if (result)
			return result;
		// 是否连续两次下降超过0.1
		for (int i = size; i > size - 2; i--) {
			if ((list.get(i - 2).getCurrentGoldPrice() - list.get(i - 1).getCurrentGoldPrice()) <= 0.1) {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * 是否单次增长超过0.2
	 * 
	 * @return
	 */
	public boolean isBigStep() {
		if (list.size() < 2)
			return false;
		int size = list.size();
		return Math.abs(list.get(size - 1).getCurrentGoldPrice() - list.get(size - 2).getCurrentGoldPrice()) > 0.2;
	}

	/**
	 * 获取最近一次缓存的价格
	 * 
	 * @return
	 */
	public GoldPriceSnapShot getLast() {
		if (this.list.size() == 0)
			return null;
		return this.list.get(this.list.size() - 1);
	}

}
