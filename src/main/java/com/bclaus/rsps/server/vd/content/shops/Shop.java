package com.bclaus.rsps.server.vd.content.shops;
/*package server.legacy.content.shops;

import server.legacy.player.Player;

*//**
 * ShopScript shop system.
 * 
 * @author Rob/Bubletan
 *//*
public class Shop {

	public final Stock[] stocks;
	public final String name;

	protected Shop(String name, String data) {
		this.name = name;
		if (data != null) {
			String[] components = data.replaceAll("<", "").split(">");
			int length = Math.min(components.length, Shops.MAX_STOCKS);
			stocks = new Stock[length];
			for (int i = 0; i < length; i++) {
				stocks[i] = new Stock(components[i]);
			}
		} else {
			stocks = new Stock[0];
		}
	}

	protected boolean restore() {
		boolean changed = false;
		for (Stock stock : stocks) {
			if (stock.amount < stock.defaultamount) {
				stock.amount++;
				changed = true;
			} else if (stock.amount > stock.defaultamount) {
				stock.amount--;
				changed = true;
			}
		}
		return changed;
	}

	protected Stock getSellStock(int id) {
		for (Stock s : stocks) {
			if (s.id == id && !s.pointCurrency()) {
				return s;
			}
		}
		return null;
	}

	protected static class Stock {

		protected static final byte COINS = 0;
		protected static final byte TOKKULS = 1;
		protected static final byte DONATOR_POINTS = 2;
		protected static final byte VOTE_POINTS = 3;
		protected static final byte PK_POINTS = 4;
		protected static final byte SLAYER_POINTS = 5;
		protected static final byte RANGE_STORE = 6;
		protected static final byte MAGIC_STORE = 7;
		protected static final byte MELEE_STORE = 8;
		protected static final byte ACTIVITY_POINTS = 9;
		protected static final byte THEIVING_POINTS = 10;
		protected short id = 0;
		protected int amount = 1;
		protected int defaultamount = amount;
		protected int price = 0;
		protected byte currency = COINS;

		private Stock(String data) {
			String[] components = data.replaceAll(" ", "").toLowerCase().split(",");
			for (String s : components) {
				if (s.startsWith("id:")) {
					id = Short.parseShort(s.substring(3));
				} else if (s.startsWith("amount:")) {
					amount = Integer.parseInt(s.substring(7));
					defaultamount = amount;
				} else if (s.startsWith("price:")) {
					price = Integer.parseInt(s.substring(6).replace("m", "000000").replace("k", "000"));
				} else if (s.startsWith("currency:")) {
					s = s.substring(9);
					if (s.equals("tokkuls")) {
						currency = TOKKULS;
					} else if (s.equals("donator")) {
						currency = DONATOR_POINTS;
					} else if (s.equals("vote")) {
						currency = VOTE_POINTS;
					} else if (s.equals("pk")) {
						currency = PK_POINTS;
					} else if (s.equals("slayer")) {
						currency = SLAYER_POINTS;
					} else if (s.equals("range")) {
						currency = RANGE_STORE;
					} else if (s.equals("theive")) {
						currency = THEIVING_POINTS;
					} else if (s.equals("magic")) {
						currency = MAGIC_STORE;
					} else if (s.equals("melee")) {
						currency = MELEE_STORE;
					} else if (s.equals("activity")) {
						currency = ACTIVITY_POINTS;
					}
				}
			}
		}

		protected boolean pointCurrency() {
			return  currency == DONATOR_POINTS || currency == MELEE_STORE || currency == ACTIVITY_POINTS || currency == VOTE_POINTS || currency == PK_POINTS || currency == SLAYER_POINTS;
		}

		protected String getCurrencyName(int amount) {
			String name = "null";
			if (currency == COINS) {
				name = "coin";
			} else if (currency == TOKKULS) {
				name = "tokkul";
			} else if (currency == DONATOR_POINTS) {
				name = "donator point";
			} else if (currency == VOTE_POINTS) {
				name = "vote point";
			} else if (currency == PK_POINTS) {
				name = "pk point";
			} else if (currency == SLAYER_POINTS) {
				name = "slayer point";
			} else if (currency == RANGE_STORE) {
				name = "pk point";
			} else if (currency == THEIVING_POINTS) {
				name = "Theiving Points";
			} else if (currency == MAGIC_STORE) {
				name = "pk point";
			} else if (currency == MELEE_STORE) {
				name = "kill";
			} else if (currency == ACTIVITY_POINTS) {
				name = "activity point";
			}
			return name + (amount != 1 ? "s" : "");
		}

		protected int getCurrencyItem() {
			if (currency == COINS) {
				return 995;
			} else if (currency == TOKKULS) {
				return 6529;
				
				 * } else if (currency == MELEE_STORE) { return 995; } else if
				 * (currency == RANGE_STORE) { return 995; } else if(currency ==
				 * MAGIC_STORE) { return 995;
				 
			} else {
				return 0;
			}
		}

		protected int getCurrencyPoints(Player c) {
			if (currency == DONATOR_POINTS) {
				return c.getDonationPoints();
			} else if (currency == VOTE_POINTS) {
				return c.votePoints;
			} else if (currency == PK_POINTS) {
				return c.pkPoints;
			} else if (currency == SLAYER_POINTS) {
				return c.slaypoints;
			} else if (currency == MELEE_STORE) {
				return c.KC;
			} else if (currency == RANGE_STORE) {
				return c.pkPoints;
			} else if (currency == THEIVING_POINTS) {
				return c.thievePoints;
			} else if (currency == MAGIC_STORE) {
				return c.pkPoints;
			} else {
				return 0;
			}
		}

		protected void removeCurrencyPoints(Player c, int amount) {
			if (currency == DONATOR_POINTS) {
				c.donationPoints -= amount;
			} else if (currency == VOTE_POINTS) {
				c.votePoints -= amount;
			} else if (currency == THEIVING_POINTS) {
				c.thievePoints -= amount;
			} else if (currency == PK_POINTS) {
				c.pkPoints -= amount;
			} else if (currency == SLAYER_POINTS) {
				c.slaypoints -= amount;
			} else if (currency == MELEE_STORE) {
				c.KC -= amount;
			} else if (currency == RANGE_STORE) {
				c.pkPoints -= amount;
			} else if (currency == MAGIC_STORE) {
				c.pkPoints -= amount;
			}
		}
	}
}*/