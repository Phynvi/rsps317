package com.bclaus.rsps.server.vd.content.market;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.items.ItemList;


public class MarketSearchItem {
	
	private String name;
	private ItemList item;
	private boolean searchingByName;
	private Pattern pattern = Pattern.compile("[//d]");
	private Matcher matcher = null;
	
	public MarketSearchItem(String name) {
		this.name = name;
	}
	
	public boolean exists() {
		if(name.length() == 0)
			return false;
		try {
			for(int i = 0; i < Server.itemHandler.ItemList.length; i++) {
				if(Server.itemHandler.ItemList[i] == null)
					continue;
				if(Server.itemHandler.ItemList[i].itemId == Integer.parseInt(name)) {
					setSearchingByName(false);
					this.item = Server.itemHandler.ItemList[i];
					return true;
				}
			}
			return false;
		} catch (NumberFormatException nfe) {
			for(int i = 0; i < Server.itemHandler.ItemList.length; i++) {
				if(Server.itemHandler.ItemList[i] == null)
					continue;
				if(Server.itemHandler.ItemList[i].itemName.equalsIgnoreCase(name)) {
					this.item = Server.itemHandler.ItemList[i];
					setSearchingByName(true);
					return true;
				}
			}
		}
		return false;
	}
	
	public void checkIfNumber() {
		setSearchingByName(false);
		for(char ch : name.toCharArray()) {
			matcher = pattern.matcher(Character.toString(ch));
			if(!matcher.matches()) {
				setSearchingByName(true);
				return;
			}
		}
	}

	public ItemList getItem() {
		return item;
	}

	public void setItem(ItemList item) {
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSearchingByName() {
		return searchingByName;
	}

	public void setSearchingByName(boolean searchByName) {
		this.searchingByName = searchByName;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Matcher getMatcher() {
		return matcher;
	}

	public void setMatcher(Matcher matcher) {
		this.matcher = matcher;
	}

}