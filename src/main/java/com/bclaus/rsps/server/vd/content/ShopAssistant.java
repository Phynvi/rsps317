package com.bclaus.rsps.server.vd.content;

import java.util.ArrayList;
import java.util.List;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.player.Player;

public class ShopAssistant {

	private List<ShopItem> shopStock = new ArrayList<ShopItem>();

	private final Player c;

	public ShopAssistant(Player client) {
		this.c = client;
	}

	public class ShopItem {
		private int id, currentAmount;

		private ShopItem(int itemId, int amount) {
			id = itemId;
			currentAmount = amount;
		}

	}

	public int tryToBuy(int itemId, int amount) {
		if (shopHasItem(itemId)) {
			for (int i = 0; i < shopStock.size();) {
				if (shopStock.get(i).id == itemId) {
					if (shopStock.get(i).currentAmount > amount) {
						int temp = shopStock.get(i).currentAmount;
						shopStock.remove(i);
						return temp;
					} else {
						shopStock.set(i, new ShopItem(itemId, shopStock.get(i).currentAmount - amount));
						return amount;
					}
				} else {
					return 0;
				}
			}
		}
		return 0;
	}

	/**
	 * Shops
	 */

	public void openShop(int ShopID) {
		c.getItems().resetItems(3823);
		resetShop(ShopID);
		c.isShopping = true;
		c.myShopId = ShopID;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendFrame126(ShopHandler.ShopName[ShopID], 3901);
	}

	public void updatePlayerShop() {
		for (int i = 1; i < World.PLAYERS.capacity(); i++) {
			if (World.PLAYERS.get(i) != null) {
				if (World.PLAYERS.get(i).isShopping && World.PLAYERS.get(i).myShopId == c.myShopId && i != c.getIndex()) {
					World.PLAYERS.get(i).updateShop = true;
				}
			}
		}
	}

	public void resetShop(int ShopID) {

		int TotalItems = 0;
		for (int i = 0; i < ShopHandler.MaxShopItems; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0) {
				TotalItems++;
			}
		}
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(3900);
		c.getOutStream().writeWord(TotalItems);
		int TotalCount = 0;
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0 || i <= ShopHandler.ShopItemsStandard[ShopID]) {
				if (ShopHandler.ShopItemsN[ShopID][i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(ShopHandler.ShopItemsN[ShopID][i]);
				} else {
					c.getOutStream().writeByte(ShopHandler.ShopItemsN[ShopID][i]);
				}
				if (ShopHandler.ShopItems[ShopID][i] > Constants.ITEM_LIMIT || ShopHandler.ShopItems[ShopID][i] < 0) {
					ShopHandler.ShopItems[ShopID][i] = Constants.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(ShopHandler.ShopItems[ShopID][i]);
				TotalCount++;
			}
			if (TotalCount > TotalItems) {
				break;
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public double getItemShopValue(int ItemID, int Type) {
		double ShopValue = 1;
		double TotPrice;
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					ShopValue = Server.itemHandler.ItemList[i].ShopValue;
				}
			}
		}

		TotPrice = ShopValue;

		if (ShopHandler.ShopBModifier[c.myShopId] == 1) {
			TotPrice *= 1;
			TotPrice *= 1;
			if (Type == 1) {
				TotPrice *= 1;
			}
		} else if (Type == 1) {
			TotPrice *= 1;
		}
		return TotPrice;
	}

	public static int getItemShopValue(int itemId) {
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == itemId) {
					return (int) Server.itemHandler.ItemList[i].ShopValue;
				}
			}
		}
		return 0;
	}

	/**
	 * buy item from shop (Shop Price)
	 */

	public void buyFromShopPrice(int removeId) {
		int ShopValue = (int) Math.floor(getItemShopValue(removeId, 0));
		ShopValue *= 1.10;
		String ShopAdd = "";
		/*
		 * if (!c.getAccount().getType().shopAccessible(c.myShopId)) {
		 * c.sendMessage(
		 * "You are not permitted to use this shop because of a restriction on your account."
		 * ); return; }
		 */
		if (c.myShopId == 30) {
			if (!c.getAchievements().hasBoughtItem(removeId)) {
				c.sendMessage(c.getItems().getItemName(removeId) + " " + " currently costs " + c.getAchievements().getCost(removeId) + "achievement points. You only need to buy this once.");
				return;
			}
		}
		if (c.myShopId == 69) {
			if (!c.getPet().isPetOwner(removeId)) {
				c.sendMessage("You are unable to buy this because you have not obtained it from a boss.");
				return;
			}
		}
		if (c.myShopId >= 17 && c.myShopId != 50 && c.myShopId != 31 && c.myShopId != 32 && c.myShopId != 30 && c.myShopId != 34 && c.myShopId != 40 && c.myShopId != 35 && c.myShopId != 65 && c.myShopId != 67 && c.myShopId != 69 && c.myShopId < 80 && c.myShopId != 20) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " points.");
			return;
		}
		if (c.myShopId == 26) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " Donator points");
			return;
		}
		if (c.myShopId == 66) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": Currently costs " + getTokkulValue(removeId) + " Donator points");
			return;
		}
		if (c.myShopId == 67 || c.myShopId == 22 || c.myShopId == 23 || c.myShopId == 24) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": Currently Costs " + getPKValue(removeId) + " PK Points");
			return;
		}
		if (c.myShopId == 40) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": Currently Costs " + getvoteValue(removeId) + " Vote Points");
			return;
		}
		if (c.myShopId == 31) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getTokkulValue(removeId) + " Tokkul.");
			return;
		}

		if (c.myShopId == 32) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getTokkulValue(removeId) + " Warrior Guild Tokens.");
			return;
		}

		if (c.myShopId == 35) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getTokkulValue(removeId) + " Castle Wars Tickets");
			return;
		}

		if (c.myShopId == 65) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getTokkulValue(removeId) + " Archery Tickets");
			return;
		}

		if (c.myShopId == 19) {
			c.sendMessage("This item current costs " + c.getItems().getUntradePrice(removeId) + " coins.");
			return;
		}
		if (ShopValue >= 1000 && ShopValue < 1000000) {
			ShopAdd = " (" + (ShopValue / 1000) + "K)";
		} else if (ShopValue >= 1000000) {
			ShopAdd = " (" + (ShopValue / 1000000) + " million)";
		}
		c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + ShopValue + " coins" + ShopAdd);
	}

	public int getTokkulValue(int id) {
		switch (id) {
		case 6571:
			return 210000;
		case 6528:
			return 6000;
		case 6524:
			return 6000;
		case 6568:
			return 6000;
		case 6525:
			return 3000;
		case 6526:
			return 3000;
		case 6522:
			return 15;
		case 3488:
		case 3486:
		case 3481:
		case 3483:
		case 3485:
			return 8200;
		case 9005:
		case 9006:
			return 2000;
		case 15021:
			return 16000;
		}
		return 500;
	}

	/* Pk Shop Prices */
	// Beautifully Organized by Zek Kappa
	public static int getvoteValue(int id) {
		switch (id) {
		case 1037:
		case 9005:
		case 761:
		case 7927:
		case 1052:
		case 12360:
			return 10;
		case 5607:
			return 5;
		case 7673:
			return 2;
		
		}
		return 3;
	}
	public static int getPKValue(int id) {
		switch (id) {
		case 12471: // Amulet of Fury (or)
			return 700;
		case 12470: // Rangers_Tunic
			return 500;
		case 12469: // TokHaar-Kal_cape
			return 1000;
		case 15597: // Barrows_gloves
			return 500;
		case 12419: // Light_Infinity_hat
		case 12420: // Light_Infinity_top
		case 12421: // Light_Infinity_bottoms
		case 12457: // Dark_Infinity_hat
		case 12458: // Dark_Infinity_top
		case 12459: // Dark_Infinity_bottoms
			return 1500;
		case 14507: // Occult_necklace
			return 700;
		case 7509: // Dwarven_rock_cake
			return 20;
		case 15343: // Lime_whip
			return 3500;
		case 3204: // Dragon_halberd
			return 350;
		case 10551: // Fighters_Torso
			return 80;
		case 10548: // Fighters Hat
			return 70;
		case 15021: // Staff_of_light
			return 1500;
		case 4151:// Abbysal_Whip
			return 1000;
		case 10887: // Barrelchest_anchor
			return 1000;
		case 6735: // Warrior_ring
			return 60;
		case 6731: // Seer's_ring
			return 60;
		case 6733: // Archer's_ring
			return 60;
		case 6737: // Berserker_ring
			return 120;
		case 11732: // Dragon Boots
			return 100;
		case 8848: // Mithril_defender
			return 20;
		case 8849: // Adamant_defender
			return 10;
		case 8850: // Rune_defender
			return 50;
		case 15496: // Dragon_defender
			return 80;
		case 11724: // Bandos_Chest_Plate
			return 2500;
		case 11726: // Bandos_Tassets
			return 2500;
		case 11728: // Bandos_boots
			return 2500;
		case 11718:// Armadyl_helmet
			return 2500;
		case 11720: // Armadyl_chestplate
			return 2500;
		case 11722: // Armadyl_plateskirt
			return 2500;
		case 11694: // Armadyl_godsword
			return 3000;
		case 11696: // Bandos_godsword
			return 2500;
		case 11698: // Saradomin_godsword
			return 2700;
		case 11700: // Zamorak_godsword
			return 2800;
		case 11730: // Saradomin_sword
			return 1200;
		case 15001: // Spirit_shield
			return 1000;
		case 15004: // Blessed_spirit_shield
			return 1200;
		case 15003: // Spectral_spirit_shield
			return 5000;
		case 14999: // Arcane_spirit_shield
			return 5000;
		case 15002: // Elysian_spirit_shield
			return 5200;
		case 11235: // Dark_bow
			return 800;
		case 12349: // Armadyl_c'bow
			return 3000;
		case 15495: // Robin_Hood_Hat
			return 600;
		case 2577: // Ranger_boots
			return 1500;
		case 14614: // Toxic_Blowpipe
			return 3000;
		case 12345: // Trident_of_the_seas_staff
			return 1200;
		case 12460: // Staff_of_the_Dead
			return 1350;
		case 6914: // Master_wand
			return 7000;
		case 6889: // Mage's_book
			return 700;
		case 7458: // Mith_gloves
			return 45;
		case 7459: // Addy_gloves
			return 50;
		case 7462: // Barrows_gloves
			return 75;
		case 15000: // Divine
			return 5000;
		case 12904: // Barrows_gloves
			return 2500;
		}
		return 3;
	}


	/**
	 * Sell item to shop (Shop Price)
	 */
	public void sellToShopPrice(int removeId) {
		for (int i : Constants.ITEM_SELLABLE) {
			if (i == removeId) {
				c.sendMessage("You can't sell " + c.getItems().getItemName(removeId).toLowerCase() + ".");
				return;
			}
		}
		if (!Player.tradeEnabled) {
			c.sendMessage("Server restrictions are now set! You cannot sell any items to the general store");
			return;
		}
		boolean IsIn = false;
		if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
			for (int j = 0; j <= ShopHandler.ShopItemsStandard[c.myShopId]; j++) {
				if (removeId == (ShopHandler.ShopItems[c.myShopId][j] - 1)) {
					IsIn = true;
					break;
				}
			}
		} else {
			IsIn = true;
		}
		if (!IsIn) {
			c.sendMessage("You can't sell " + c.getItems().getItemName(removeId).toLowerCase() + " to this store.");
		} else {
			int ShopValue = (int) Math.floor(getItemShopValue(removeId, 1));
			String ShopAdd = "";
			if (ShopValue >= 1000 && ShopValue < 1000000) {
				ShopAdd = " (" + (ShopValue / 1000) + "K)";
			} else if (ShopValue >= 1000000) {
				ShopAdd = " (" + (ShopValue / 1000000) + " million)";
			}
			c.sendMessage(c.getItems().getItemName(removeId) + ": shop will buy for " + ShopValue + " coins" + ShopAdd);
		}
	}

	public boolean shopSellsItem(int itemID) {
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
				return true;
			}
		}
		return false;
	}

	public boolean sellItem(int itemID, int fromSlot, int amount) {
		if (c.inTrade || c.isBanking || c.duelStatus != 0) {
			c.sendMessage("You cant sell items to the shop while your in trade!");
			return false;
		}
		if (c.getShops().getItemShopValue(itemID, 1) > 5000000  && c.myShopId != 50) {
			c.sendMessage("You cannot sell any item over 5M to the general store.");
			return false;
		}
		if (!c.getAccount().getType().shopAccessible(c.myShopId) ) {
			c.sendMessage("You are not permitted to use this shop because of a restriction on your account.");
			return false;
		}
		if (!Player.tradeEnabled) {
			c.sendMessage("Server-restrictions are now set! you cannot sell your items here");
			return false;
		}
		if (c.myShopId == 14) {
			return false;
		}
		for (int i : Constants.ITEM_SELLABLE) {
			if (i == itemID) {
				c.sendMessage("You can't sell " + c.getItems().getItemName(itemID).toLowerCase() + ".");
				return false;
			}
		}
		if (amount > 0 && itemID == (c.playerItems[fromSlot] - 1)) {
			if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
				boolean IsIn = false;
				for (int i = 0; i <= ShopHandler.ShopItemsStandard[c.myShopId]; i++) {
					if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
						IsIn = true;
						break;
					}
				}
				if (!IsIn) {
					c.sendMessage("You can't sell " + c.getItems().getItemName(itemID).toLowerCase() + " to this store.");
					return false;
				}
			}

			if (amount > c.playerItemsN[fromSlot] && (Item.itemIsNote[(c.playerItems[fromSlot] - 1)] || Item.itemStackable[(c.playerItems[fromSlot] - 1)])) {
				amount = c.playerItemsN[fromSlot];
			} else if (amount > c.getItems().getItemAmount(itemID) && !Item.itemIsNote[(c.playerItems[fromSlot] - 1)] && !Item.itemStackable[(c.playerItems[fromSlot] - 1)]) {
				amount = c.getItems().getItemAmount(itemID);
			}
			int TotPrice2;
			for (int i = amount; i > 0; i--) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 1));
				if (ShopHandler.itemsAvailable(c.myShopId) >= ShopHandler.MaxInShopItems) {
					c.sendMessage("You cannot sell this right now, this shop cannot hold anymore than 40 items.");
					return false;
				}

				if (c.getItems().freeSlots() > 0 || c.getItems().playerHasItem(995)) {
					if (!Item.itemIsNote[itemID]) {
						c.getItems().deleteItem(itemID, c.getItems().getItemSlot(itemID), 1);
					} else {
						c.getItems().deleteItem(itemID, fromSlot, 1);
					}
					c.getItems().addItem(995, TotPrice2);
					if (c.myShopId == 20) {

					} else {
						addShopItem(itemID, 1);
					}
				} else {
					c.sendMessage("You don't have enough space in your inventory.");
					break;
				}
				
				}
			{
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return true;
	}

	public boolean addShopItem(int itemID, int amount) {
		boolean Added = false;
		if (amount <= 0) {
			return false;
		}
		if (Item.itemIsNote[itemID]) {
			itemID = c.getItems().getUnnotedItem(itemID);
		}
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if ((ShopHandler.ShopItems[c.myShopId][i] - 1) == itemID) {
				ShopHandler.ShopItemsN[c.myShopId][i] += amount;
				Added = true;
			}
		}
		if (!Added) {
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[c.myShopId][i] == 0) {
					ShopHandler.ShopItems[c.myShopId][i] = (itemID + 1);
					ShopHandler.ShopItemsN[c.myShopId][i] = amount;
					ShopHandler.ShopItemsDelay[c.myShopId][i] = 0;
					break;
				}
			}
		}
		return true;
	}

	public boolean shopHasItem(int itemId) {
		for (ShopItem aShopStock : shopStock) {
			if (aShopStock.id == itemId) {
				return true;
			}
		}
		return false;
	}

	public boolean buyItem(int itemID, int fromSlot, int amount) {
		if (c.myShopId != 66 || c.myShopId != 26 || c.myShopId != 27) {
			if (!c.getAccount().getType().shopAccessible(c.myShopId) ) {
				c.sendMessage("You are not permitted to use this shop because of a restriction on your account.");
				return false;
			}
		}
		if (c.myShopId == 14) {
			skillBuy(itemID);
			return false;
		} else if (c.myShopId == 15) {
			return false;
		}
		if (ShopHandler.ShopItems[c.myShopId][fromSlot] - 1 != itemID || ShopHandler.ShopItems[c.myShopId][fromSlot] < 0) {
			return false;
		}
		if (!shopSellsItem(itemID)) {
			return false;
		}
		if (amount <= 0) {
			return false;
		}
		if (amount > 0) {
			if (amount > ShopHandler.ShopItemsN[c.myShopId][fromSlot]) {
				amount = ShopHandler.ShopItemsN[c.myShopId][fromSlot];
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2;
			// int Overstock;
			int Slot = 0;
			int Slot1;// Tokkul
			if (c.myShopId == 30 && !c.getAchievements().hasBoughtItem(itemID)) {
				handleOtherShop(itemID);
				return false;
			}
			if (c.myShopId >= 18 && c.myShopId < 80 && c.myShopId != 30 && c.myShopId != 69 && c.myShopId != 20) {
				handleOtherShop(itemID);
				return false;
			}
			if (c.myShopId == 69 && !c.getPet().isPetOwner(itemID)) {
				c.sendMessage("You cannot buy this item because you have not obtained it legitimately.");
				return false;
			}
			for (int i = amount; i > 0; i--) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0));
				Slot = c.getItems().getItemSlot(995);
				Slot1 = c.getItems().getItemSlot(6529);
				if (Slot == -1 && c.myShopId != 29 && c.myShopId != 31 && c.myShopId != 47) {
					c.sendMessage("You don't have enough coins.");
					break;
				}
				if (Slot1 == -1 && c.myShopId == 31) {
					c.sendMessage("You don't have enough tokkul.");
					break;
				}
				if (TotPrice2 <= 1) {
					TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0));
					TotPrice2 *= 1.66;
				}
				if (c.myShopId != 29 && c.myShopId != 31 || c.myShopId != 47 || c.myShopId != 21) {
					if (c.playerItemsN[Slot] >= TotPrice2) {
						if (Item.itemStackable[itemID] && c.getItems().playerHasItem(995, TotPrice2*amount)) {
								if (Server.shopHandler.ShopItemsN[c.myShopId][fromSlot] < amount) {
									amount = Server.shopHandler.ShopItemsN[c.myShopId][fromSlot];
								}
								c.getItems().deleteItem(995, c.getItems().getItemSlot(995), TotPrice2*amount);
								c.getItems().addItem(itemID, amount);
								Server.shopHandler.ShopItemsN[c.myShopId][fromSlot] -= amount;
								Server.shopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								c.getItems().resetItems(3823);
								resetShop(c.myShopId);
								updatePlayerShop();
								return false;
							}
							if (Item.itemStackable[itemID] && !c.getItems().playerHasItem(995, TotPrice2*amount)) {
								int itemAmount = c.playerItemsN[c.getItems().getItemSlot(995)]/TotPrice2;
								c.getItems().deleteItem(995, c.getItems().getItemSlot(995), TotPrice2*itemAmount);
								c.getItems().addItem(itemID, itemAmount);
								Server.shopHandler.ShopItemsN[c.myShopId][fromSlot] -= itemAmount;
								Server.shopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								c.getItems().resetItems(3823);
								resetShop(c.myShopId);
								updatePlayerShop();
								return false;
							}
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(995, c.getItems().getItemSlot(995), TotPrice2);
							tryToBuy(itemID, 1);
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							/*
							 * if ((fromSlot + 1) >
							 * ShopHandler.ShopItemsStandard[c.myShopId]) {
							 * ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							 * }
							 */
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough coins.");
						break;
					}
				}
				if (c.myShopId == 31) {
					if (c.playerItemsN[Slot1] >= TotPrice2) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(6529, c.getItems().getItemSlot(6529), TotPrice2);
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
								ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough tokkul.");
						break;
					}
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return false;
	}

	public void handleOtherShop(int itemID) {

		if (c.myShopId == 30 && !c.getAchievements().hasBoughtItem(itemID)) {
			if (c.getItems().freeSlots() <= 0) {
				c.sendMessage("You need atleast one free slot to buy from here.");
				return;
			}
			int cost = c.getAchievements().getCost(itemID);
			if (c.getAchievements().getPoints() >= cost) {
				c.getAchievements().setPoints(c.getAchievements().getPoints() - cost);
				c.getItems().addItem(itemID, 1);
				if (itemID == 7582) {
					c.getPet().setOwnedPet(itemID, true);
				}
				c.getAchievements().setBoughtItem(itemID);
				c.getItems().resetItems(3823);
			} else {
				c.sendMessage("You need atleast " + cost + " achievement points to buy this item.");
			}
			return;
		}
		if (c.myShopId == 56) {
			if (c.assaultPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.assaultPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You dont have enough Assault Points to buy this item!");
			}
		} else if (c.myShopId == 26) {
			if (c.getDonationPoints() >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.setDonationPoints(c.getDonationPoints() - getSpecialItemValue(itemID));
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough donation points to buy this item.");
			}
		} else if (c.myShopId == 66) {
			if (c.getDonationPoints() >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.setDonationPoints(c.getDonationPoints() - getSpecialItemValue(itemID));
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough donation points to buy this item.");
			}
		} else if (c.myShopId == 61) {
			if (c.slaypoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.slaypoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You dont have enough Slayer Points to buy this item!");
			}

		} else if (c.myShopId == 21 || c.myShopId == 28) {
			if (c.magePoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.magePoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough points to buy this item.");
			}
		} else if (c.myShopId == 18) {
			if (c.pcPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.pcPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough points to buy this item.");
			}
		} else if (c.myShopId == 40) {
			if (c.votePoints >= getvoteValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.votePoints -= getvoteValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough points to buy this item.");
			}
		} else if (c.myShopId == 67 || c.myShopId == 22 || c.myShopId == 23 || c.myShopId == 24) {
			if (c.pkPoints >= getPKValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.pkPoints -= getPKValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough PKP to buy this item.");
			}
		} else if (c.myShopId == 31) {
			if (c.getItems().getItemAmount(6529) >= getTokkulValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getItems().deleteItem(6529, c.getItems().getItemSlot(6529), getTokkulValue(itemID));
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough Tokkuls.");
			}
		} else if (c.myShopId == 32) {
			if (c.getItems().getItemAmount(8851) >= getTokkulValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getItems().deleteItem(8851, c.getItems().getItemSlot(8851), getTokkulValue(itemID));
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("Guild Tokens.");
			}
		} else if (c.myShopId == 35) {
			if (c.getItems().getItemAmount(4067) >= getTokkulValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getItems().deleteItem(4067, c.getItems().getItemSlot(4067), getTokkulValue(itemID));
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You don't have enough Castle Wars Tickets.");
			}
		} else if (c.myShopId == 65) {
			if (c.getItems().getItemAmount(1464) >= getTokkulValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getItems().deleteItem(1464, c.getItems().getItemSlot(1464), getTokkulValue(itemID));
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You don't have enough Archery Tickets.");
			}
		} else if (c.myShopId == 33) {
			if (c.skillPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.skillPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough skilling points to buy this item.");
			}
		} else if (c.myShopId == 37) {
			if (c.fireMakingPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.fireMakingPoints -= getSpecialItemValue(itemID);
					if (itemID >= 7329 && itemID <= 7331 || itemID == 10326 || itemID == 10327) {
						c.getItems().addItem(itemID, 15);
					} else {
						c.getItems().addItem(itemID, 1);
					}
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough FireMaking Points to buy this item.");
			}
		} else if (c.myShopId == 38) {
			if (c.wcPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.wcPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You don't have the required WC Points to purchase this.");
			}
		} else if (c.myShopId == 29) {
			if (c.agilityPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.agilityPoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You dont have enough Agility Points to buy this item.");
			}
		} else if (c.myShopId == 36) {
			if (c.thievePoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.thievePoints -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("Your thieving awareness isn't heightened yet. Try thieving some more.");
			}
		}
	}

	public void openSkillCape() {
		int capes = get99Count();
		if (capes > 1) {
			capes = 1;
		} else {
			capes = 0;
		}
		c.myShopId = 14;
		setupSkillCapes(get99Count());
	}

	public int[] skillCapes = { 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 1293, 9948 };

	public int get99Count() {
		int count = 0;
		for (int j = 0; j < c.playerLevel.length; j++) {
			if (Player.getLevelForXP(c.playerXP[j]) >= 99) {
				count++;
			}
		}
		return count;
	}

	public void setupSkillCapes(int capes2) {
		c.getItems().resetItems(3823);
		c.isShopping = true;
		c.myShopId = 14;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendFrame126("Skillcape Shop", 3901);

		int TotalItems;
		TotalItems = capes2;
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(3900);
		c.getOutStream().writeWord(TotalItems);
		for (int i = 0; i <= 22; i++) {
			if (Player.getLevelForXP(c.playerXP[i]) < 99) {
				continue;
			}
			c.getOutStream().writeByte(1);
			c.getOutStream().writeWordBigEndianA(skillCapes[i] + 2);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public void skillBuy(int item) {
		int nn = get99Count();
		if (nn > 1) {
			nn = 1;
		} else {
			nn = 0;
		}
		for (int j = 0; j < skillCapes.length; j++) {
			if (skillCapes[j] == item || skillCapes[j] + 1 == item) {
				if (c.getItems().freeSlots() > 1) {
					if (c.getItems().playerHasItem(995, 99000)) {
						if (Player.getLevelForXP(c.playerXP[j]) >= 99) {
							c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 99000);
							c.getItems().addItem(skillCapes[j] + nn, 1);
							c.getItems().addItem(skillCapes[j] + 2, 1);
						} else {
							c.sendMessage("You must have 99 in the skill of the cape you're trying to buy.");
						}
					} else {
						c.sendMessage("You need 99k to buy this item.");
					}
				} else {
					c.sendMessage("You must have at least 1 inventory spaces to buy this item.");
				}
			}
		}
		c.getItems().resetItems(3823);
	}

	public int getSpecialItemValue(int id) {
		switch (id) {
		/*
		 * Donator Credit store
		 */
		// if (c.myShopId == 10) {
		// }
		case 11600:
			return 100;
		case 6199:
			return 30;
		case 12464:
		case 12465:
		case 12466:
		case 12467:
		case 12468:
			return 350;
		case 4067:
			return 1;
		case 14614:
			return 375;
		case 15584:		
			return 100;
		case 1038:
		case 1040:
		case 1042:
		case 1044:
		case 1046:
		case 1048:
			return 750;
		case 15598:
			return 400;
		case 15499:
			return 50;
		case 15498:
			return 50;
		case 15497:
			return 50;
		case 15494:
			return 200;
		case 6829:
			return 150;
		case 13220:
			return 60;
		case 15998:
		case 15997:
			return 500;
		case 12398:
		case 12399:
		case 12400:
		case 12401:
			return 200;
		case 6831:
			return 450;
		case 6830:
			return 250;
		case 10330:
		case 10332:
		case 10334:
		case 10336:
		case 10338:
		case 10340:
		case 10342:
		case 10344:
		case 10346:
		case 10348:
		case 10350:
		case 10352:
		case 6465:
			return 2000;
		case 10939:
		case 10940:
		case 10941:
		case 10933:
			return 15;
		case 6739:
			return 30;
			/** Lighters **/
		case 7329:
		case 7330:
		case 7331:
		case 10326:
		case 10327:
			return 1;
			/** Honour Shop **/
		case 11377:
			return 4;
		case 8676:
		case 8740:
		case 10394:
			return 12;
		case 9629:
		case 2402:
			return 2;
		case 9731:
			return 1;
			/** Thieving **/
		case 1506:
			return 10;
		case 4502:
			return 40;
		case 2631:
			return 16;
		case 9634:
		case 9636:
		case 9638:
			return 20;
		case 7918:
			return 250;
		case 7917:
			return 4;
			/** End **/
		case 606:
			return 1;
		case 1555:
		case 1556:
		case 1557:
		case 1558:
		case 1559:
		case 1560:
			return 10;
		case 6555:
			return 20;
		case 7583:
			return 25;
		case 7582:
			return 50;
		case 6583:
			return 200;
		case 1052:
			return 80;
		case 775:
		case 1833:
		case 1835:
		case 1837:
			return 5;
		case 10547:
		case 10548:
		case 10549:
		case 10550:
			return 45;
		case 10553:
			return 25;
		case 10555:
			return 75;
		case 10552:
			return 30;
		case 6137:
		case 6139:
		case 6141:
		case 6147:
		case 6153:
			return 45;
		case 9084:
			return 100;
		case 9096:
		case 9097:
		case 9098:
		case 9099:
			return 150;
		case 9100:
		case 9101:
		case 9102:
		case 9104:
			return 75;
		case 3385:
		case 3387:
		case 3389:
		case 3391:
		case 3393:
			return 25;
		case 7400:
		case 7399:
		case 7398:
			return 35;
		case 2890:
			return 125;
		case 10400:
		case 10402:
			return 40;
		case 10404:
		case 10406:
		case 10408:
		case 10410:
		case 10412:
		case 10414:
		case 10416:
		case 10418:
		case 10420:
		case 10424:
		case 10426:
		case 10428:
		case 10430:
		case 10432:
		case 10434:
		case 10436:
		case 10438:
			return 40;
		case 6859:
			return 50;
		case 7053:
			return 50;
		case 1419:
			return 50;
		case 2460:
			return 10;
		case 2462:
			return 10;
		case 2464:
			return 10;
		case 2466:
			return 10;
		case 2468:
			return 10;
		case 2470:
			return 10;
		case 2472:
			return 10;
		case 2474:
			return 10;
		case 9044:
			return 50;
		case 12710:
			return 400;
		case 4166:
			return 8;
		case 12708:
			return 400;
		case 12712:
			return 400;
		case 4170:
			return 12;
		case 4156:
			return 17;
		case 4551:
			return 12;
		case 10952:
			return 1;
		case 6708:
			return 6;
		case 4158:
			return 21;
		case 8901:
			return 250;
		case 8839:
			return 125;
		case 2528:
			return 1;
		case 10422:
			return 40;
		case 8841:
			return 40;
		case 8840:
			return 125;
		case 8842:
			return 50;
		case 11663:
			return 75;
		case 11664:
			return 75;
		case 11665:
			return 75;
		case 6570:
			return 3;
		case 11283:
			return 5;
		case 10499:
			return 30;
		case 11724:
			return 100;
		case 11726:
			return 100;
		case 7462:
			return 40;
		case 15022:
			return 65;
		case 10551:
			return 75;
		case 15496:
			return 100;
		case 3840:
			return 100;
		case 3842:
			return 100;
		case 3844:
			return 100;
		case 8850:
			return 20;
		case 11732:
			return 3;
		case 6585:
		case 4202:
		case 12469:
		case 12346:
			return 150;
		case 6737:
			return 20;
		case 11128:
			return 50;
		case 4151:
			return 50;
		case 4447:
			return 10;
		case 1580:
			return 3;
		case 4153:
			return 2;
		case 11777:
			return 160;
		case 1053:
		case 1055:
		case 1057:
			return 20;
		case 11718:
		case 11720:
		case 11722:
			return 80;
		case 8013:
			return 1;
		case 11698:
		case 11696:
		case 11700:
			return 20;
		case 11730:
			return 60;
		case 2595:
		case 2591:
		case 2593:
		case 2597:
			return 15;
		case 2581:
		case 2577:
			return 300;
		case 15999:
			return 110;
		case 15813:
		case 14507:
			return 150;
		case 11694:
		case 15001:
			return 25;
		case 12356:
		case 12357:
		case 12358:
		case 12359:
			return 200;
		case 14999:
		case 15002:
		case 12349:
			return 375;
		case 15003:
			return 400;
		case 15052:
			return 180;
		case 15021:
			return 50;
		case 15343:
			return 200;
		case 15495:
			return 50;
		case 15004:
		case 12460:
			return 150;
		case 15000:
			return 200;
		case 13738:
		case 13744:
		case 13740:
		case 13742:
		case 12362:
		case 12363:
		case 12361:
			return 50;
		case 1037:
			return 150;
		case 4566:
			return 20;
		case 9920:
			return 690;
		case 10507:
			return 20;
		case 10679:
		case 10294:
			return 25;
		case 2583:
		case 2585:
		case 2587:
		case 2589:
			return 15;
			// graceful
		case 15373:
			return 40;
		case 15374:
			return 60;
		case 15375:
			return 150;
		case 15376:
			return 100;
		case 15377:
			return 80;
		case 15378:
			return 40;
			//graceful end
		case 2669:
		case 2671:
		case 2673:
		case 2675:
		case 2653:
		case 2655:
		case 2657:
		case 2659:
		case 2661:
		case 2663:
		case 2665:
		case 2667:
			return 25;
		case 6912:
			return 30;
		case 6914:
			return 50;
		case 6916:
			return 60;
		case 6918:
			return 50;
		case 6920:
			return 50;
		case 6922:
			return 45;
		case 6924:
			return 65;
		case 6889:
		case 14502:
		case 14503:
			return 70;
		case 7321:
			return 75;
		case 7323:
			return 100;
		case 13858:
			return 100;
		case 13861:
			return 105;

		case 13896:
			return 155;
		case 13884:
			return 135;
		case 13890:
			return 140;
		case 13876:
			return 110;
		case 13870:
			return 100;
		case 13873:
			return 105;
		case 3486:
			return 35;
		case 3481:
			return 35;
		case 3483:
			return 35;
		case 3485:
			return 35;
		case 3488:
			return 35;
		case 12000:
			return 60;
		case 13263:
			return 300;
		case 15372:
			return 1000;
		}
		return 0;
	}

}
