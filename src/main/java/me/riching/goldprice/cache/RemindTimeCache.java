package me.riching.goldprice.cache;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class RemindTimeCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RemindTimeCache() {
	}

	public static RemindTimeCache getInstance() {
		return cache;
	}

	private static RemindTimeCache cache = new RemindTimeCache();

	private AtomicInteger buyTimes = new AtomicInteger(5);
	private AtomicInteger soldTimes = new AtomicInteger(5);

	public void set(int byTimes, int soldTimes) {
		this.buyTimes.set(byTimes);
		this.soldTimes.set(soldTimes);
	}

	public synchronized void decBuy() {
		if (this.buyTimes.get() > 0) {
			this.buyTimes.decrementAndGet();
			this.soldTimes.incrementAndGet();
		}
	}

	public synchronized void decSold() {
		if (this.soldTimes.get() > 0) {
			this.soldTimes.decrementAndGet();
			this.buyTimes.incrementAndGet();
		}
	}

	public int getBuyTimes() {
		return this.buyTimes.get();
	}

	public int getSoldTimes() {
		return this.soldTimes.get();
	}

}
