package com.bclaus.rsps.server.vd.content.market;
/*package server.game.content.market;

import server.game.player.Player;
import server.game.player.PlayerHandler;
import Misc;

public class MarketMessage {
	
	private String owner;
	private String item;
	private int amount;
	private int price;
	private boolean buying;
	
	public MarketMessage(boolean buying, String owner, String item, int amount, int price) {
		this.owner = owner;
		this.item = item;
		this.amount = amount;
		this.price = price;
		this.buying = buying;
	}
	
	public void send() {
		for(Player player : PlayerHandler.PLAYERS) {
			if(player == null)
				continue;
			//if(player.getGameOptions().isToggled(Toggles.OPTION_1.ordinal()))
				player.sendMessage(":market:<col=FF0000><b>[Game Market] "+Misc.capitalize(this.owner)+" is "+(this.buying ? "buying" : "selling")+" "+Misc.insertCommas(Integer.toString(amount))+"x "+item+" for "+Misc.insertCommas(Integer.toString(price))+" gp each.</b></col>");
		}
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isBuying() {
		return buying;
	}

	public void setBuying(boolean buying) {
		this.buying = buying;
	}

}*/