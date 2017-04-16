package com.bclaus.rsps.server.vd.content.market;

public class MarketEntry {
	
	private String playerName, playerInternetProtocol, itemName, expiryDate;
	private int itemAmount, itemPrice, uptime, itemId;
	private boolean buying;
	private int searchIndex;
	
	public MarketEntry() {
		
	}
	
	public MarketEntry(String playerName, String itemName, int itemAmount, int itemPrice, String expiryDate, int uptime, boolean buying) {
		this.playerName = playerName;
		this.itemName = itemName;
		this.itemAmount = itemAmount;
		this.itemPrice = itemPrice;
		this.expiryDate = expiryDate;
		this.uptime = uptime;
		this.buying = buying;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getExpiryDate() {
		return this.expiryDate;
	}
	
	public void setDate(String date) {
		this.expiryDate = date;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public String getPlayerInternetProtocol() {
		return playerInternetProtocol;
	}
	
	public void setPlayerInternetProtocol(String playerInternetProtocol) {
		this.playerInternetProtocol = playerInternetProtocol;
	}

	public int getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}

	public int getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(int itemPrice) {
		this.itemPrice = itemPrice;
	}

	public int getUptime() {
		return uptime;
	}

	public void setUptime(int uptime) {
		this.uptime = uptime;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public boolean isBuying() {
		return buying;
	}

	public void setBuying(boolean buying) {
		this.buying = buying;
	}

	public int getSearchIndex() {
		return searchIndex;
	}

	public void setSearchIndex(int searchIndex) {
		this.searchIndex = searchIndex;
	}

}