package com.bclaus.rsps.server.vd.content.shops;
/*package server.legacy.content.shops;

import server.legacy.World;
import server.legacy.content.shops.Shop.Stock;
import server.legacy.items.Item;
import server.legacy.items.ItemAssistant;
import server.legacy.player.Player;
import server.legacy.player.PlayerAssistant;
import server.legacy.player.account_type.Account;
import Misc;

*//**
 * ShopScript shop system.
 * 
 * @author Rob/Bubletan
 *//*
public class ShopExecutor {

	public static void open(Player c, int shop) {
		Shop s = Shops.get(shop);
		update(c, s);
		for (int i = 0; i < s.stocks.length; i++) {
			PlayerAssistant.sendInterfaceText(c, s.stocks[i].price + "," + s.stocks[i].currency, 28000 + i);
		}
		c.shopping = true;
		c.shopIndex = shop;
	}

	public static void close(Player c) {
		c.shopping = false;
		c.shopIndex = -1;
	}

	protected static void update(Player c, Shop shop) {
		ItemAssistant.resetItems(c, 3823);
		int length = shop.stocks.length;
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeShort(3900);
		c.getOutStream().writeShort(length);
		for (int i = 0; i < length; i++) {
			if (shop.stocks[i].amount < 255) {
				c.getOutStream().writeByte(shop.stocks[i].amount);
			} else {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(shop.stocks[i].amount);
			}
			c.getOutStream().writeLEShortA(shop.stocks[i].id + 1);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// c.getPA().sendFrame248(3824, 3822);
		PlayerAssistant.sendInventoryOverlay(c, 3824, 3822);
		PlayerAssistant.sendInterfaceText(c, shop.name, 3901);
	}

	public static void buyValue(Player c, int shop, int stock) {
		Stock s = Shops.get(shop).stocks[stock];
		c.getItems();
		c.sendMessage(ItemAssistant.getItemName(s.id) + " currency costs " + Misc.format(s.price) + " " + s.getCurrencyName(s.price) + ".");
	}

	public static void buy(Player c, int shop, int stock, int amount) {
		if(c.getAccount().getType().equals(Account.IRON_MAN_TYPE)) {
			c.sendMessage("Iron Man accounts can not buy from shops.");
			c.getPA().closeAllWindows();
			return;
		}
		Stock s = Shops.get(shop).stocks[stock];
		if (s.amount < amount) {
			amount = s.amount;
		}
		if (s.price > 0) {
			if (s.pointCurrency()) {
				int points = (int) s.getCurrencyPoints(c);
				if (points < s.price) {
					c.sendMessage("You don't have enough " + s.getCurrencyName(0) + ".");
					return;
				}
				amount = Math.min(points / s.price, amount);
				amount = checkBuyLimits(c, s.id, amount);
				if (amount <= 0) {
					return;
				}
				s.removeCurrencyPoints(c, amount * s.price);
			} else {
				int item = s.getCurrencyItem();
				if (!c.getItems().playerHasItem(item, s.price)) {
					c.sendMessage("You don't have enough " + s.getCurrencyName(0) + ".");
					return;
				}
				amount = Math.min(c.playerItemsN[c.getItems().getItemSlot(item)] / s.price, amount);
				amount = checkBuyLimits(c, s.id, amount);
				if (amount <= 0) {
					return;
				}
				c.getItems().deleteItem(item, amount * s.price);
			}
		} else {
			amount = checkBuyLimits(c, s.id, amount);
			if (amount <= 0) {
				return;
			}
		}
		s.amount -= amount;
		c.getItems().addItem(s.id, amount);
		for (Player p : World.PLAYERS) {
			if (p != null && p.shopIndex == shop) {
				update((Player) p, Shops.get(shop));
			}
		}
	}

	private static int checkBuyLimits(Player c, int id, int amount) {
		if (Item.itemStackable[id] && c.getItems().playerHasItem(id)) {
			int current = c.playerItemsN[c.getItems().getItemSlot(id)];
			if (amount + current > Integer.MAX_VALUE) {
				amount = Integer.MAX_VALUE - current;
				c.sendMessage("You don't have enough space in your inventory.");
			}
		} else if (!Item.itemStackable[id]) {
			int free = c.getItems().freeSlots();
			if (amount > free) {
				amount = free;
				c.sendMessage("You don't have enough space in your inventory.");
			}
		}
		return amount;
	}

	@SuppressWarnings("unused")
	public static void sellValue(Player c, int shop, int slot) {
		int item = c.playerItems[slot] - 1;
		Stock s = Shops.get(shop).getSellStock(item);
		int price = (int) (s.price * 0.66f);
		
		 * for (int i : Config.ITEM_SELLABLE) {
		 * c.sendMessage("You can't sell "+c
		 * .getItems().getItemName(item).toLowerCase()+"."); return; }
		 
		if (shop >= 1) {
			c.sendMessage("You can't sell Items to shops");
			return;
		}
		if (s == null) {
			c.sendMessage("You can't sell " + c.getItems().getItemName(item).toLowerCase() + " to this store.");
			return;
		}
		c.sendMessage(c.getItems().getItemName(item) + " store will buy for " + Misc.format(price) + " " + s.getCurrencyName(price) + ".");
	}

	public static void sell(Player c, int shop, int slot, int amount) {
		int item = c.playerItems[slot] - 1;
		Stock s = Shops.get(shop).getSellStock(item);
		if (shop >= 1) {
			c.sendMessage("You can't sell items to shops.");
			return;
		}
		if (s == null) {
			c.sendMessage("You can't sell " + c.getItems().getItemName(item).toLowerCase() + " to this store.");
			return;
		}
		int current = 0;
		for (int i = 0; i < 28; i++) {
			if (c.playerItems[i] - 1 == item) {
				current += c.playerItemsN[i];
			}
		}
		if (amount > current) {
			amount = current;
		}
		if (s.price > 0) {
			int price = (int) (s.price * 0.66f);
			if (c.getItems().playerHasItem(s.getCurrencyItem(), 1)) {
				amount = Math.min((Integer.MAX_VALUE - c.playerItemsN[c.getItems().getItemSlot(s.getCurrencyItem())]) / price, amount);
			} else if (c.getItems().freeSlots() > 0) {
				amount = Math.min(Integer.MAX_VALUE / price, amount);
			}
			if (amount > Integer.MAX_VALUE - s.amount) {
				amount = Integer.MAX_VALUE - s.amount;
			}
			c.getItems().addItem(s.getCurrencyItem(), price * amount);
		}
		if (amount <= 0) {
			return;
		}
		s.amount += amount;
		if (amount == 1 || Item.itemStackable[item]) {
			c.getItems().deleteItem(item, slot, amount);
		} else {
			for (int i = 0; i < 28; i++) {
				if (c.playerItems[i] - 1 == item) {
					c.getItems().deleteItem(item, i, 1);
					if ((--amount) == 0) {
						break;
					}
				}
			}
		}
		for (Player p : World.PLAYERS) {
			if (p != null && p.shopIndex == shop) {
				update((Player) p, Shops.get(shop));
			}
		}
	}
}
*/