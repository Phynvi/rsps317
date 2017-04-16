package com.bclaus.rsps.server.vd.content.market;

import com.bclaus.rsps.server.vd.player.Player;


public class Market {
	
	private Player player;
	private MarketEntry marketEntry = new MarketEntry();
	private MarketOfferUptime marketOfferUptime;
	private MarketSearchItem marketSearchItem;
	private long lastMarketResults;
	private MarketEntry[] searchResults;
	
	public Market(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public MarketEntry getMarketEntry() {
		return this.marketEntry;
	}

	public MarketOfferUptime getMarketOfferUptime() {
		return marketOfferUptime;
	}

	public void setMarketOfferUptime(MarketOfferUptime marketOfferUptime) {
		this.marketOfferUptime = marketOfferUptime;
	}

	public MarketSearchItem getMarketSearchItem() {
		return marketSearchItem;
	}

	public void setMarketSearchItem(MarketSearchItem marketSearchItem) {
		this.marketSearchItem = marketSearchItem;
	}

	public long getLastMarketResults() {
		return lastMarketResults;
	}

	public void setLastMarketResults(long lastMarketResults) {
		this.lastMarketResults = lastMarketResults;
	}

	public MarketEntry[] getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(MarketEntry[] searchResults) {
		this.searchResults = searchResults;
	}
}
