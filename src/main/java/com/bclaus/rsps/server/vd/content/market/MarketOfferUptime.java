package com.bclaus.rsps.server.vd.content.market;

public enum MarketOfferUptime {
	ONE_DAY(1, 0),
	THREE_DAYS(3, 150000),
	FIVE_DAYS(5, 300000);
	
	private int lifetime, cost;
	MarketOfferUptime(int lifetime, int cost) {
		this.lifetime = lifetime;
		this.cost = cost;
	}
	
	public int getLifetime() {
		return this.lifetime;
	}
	
	public int getCost() {
		return this.cost;
	}
	

}