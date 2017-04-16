package com.bclaus.rsps.server.vd.items;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.content.ResourceBag;
import com.bclaus.rsps.server.vd.items.bank.BankTab;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.account_type.Account;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.items.bank.BankItem;

public class ItemAssistant {

	private Player c;

	public ItemAssistant(Player client) {
		this.c = client;
	}

	public void updateInventory() {
		c.getItems().resetItems(3214);
	}

	public boolean wearingItems() {
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > 0 && c.playerEquipmentN[i] > 0) {
				return true;
			}
		}
		return false;
	}

	public int getRiskCarried() {
		int toReturn = 0;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			toReturn += Item.getItemShopValue(c, c.playerEquipment[i]) * c.playerEquipmentN[i];
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.getItems().contains(995))
				continue;
			toReturn += Item.getItemShopValue(c, c.playerItems[i]) * c.playerItemsN[i];
		}
		if (toReturn > Integer.MAX_VALUE)
			toReturn = Integer.MAX_VALUE;
		return toReturn;
	}

	// probably like that beceause its coins? lets see.
	/**
	 * Adds an item to the bank without checking if the player has it in there
	 * inventory
	 * 
	 * @param itemId
	 *            the id of the item were banking
	 * @param amount
	 *            amount of items to bank
	 */
	public void addItemToBank(int itemId, int amount) {
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemId + 1, amount);
		Iterator<BankTab> iterator = Arrays.asList(c.getBank().getBankTab()).iterator();
		while (iterator.hasNext()) {
			BankTab t = iterator.next();
			if (t != null && t.size() > 0) {
				Iterator<BankItem> iterator2 = t.getItems().iterator();
				while (iterator2.hasNext()) {
					BankItem i = iterator2.next();
					if (i.getId() == item.getId() && !isNotable(itemId)) {
						if (t.getTabId() != tab.getTabId()) {
							tab = t;
							c.getBank().setCurrentBankTab(tab);
							resetBank();
						}
					} else {
						if (isNotable(itemId) && i.getId() == item.getId() - 1) {
							item = new BankItem(itemId, amount);
							if (t.getTabId() != tab.getTabId()) {
								tab = t;
								c.getBank().setCurrentBankTab(tab);
								resetBank();
							}
						}
					}
				}
			}
		}
		if (isNotable(itemId))
			item = new BankItem(itemId, amount);
		if (tab.freeSlots() == 0) {
			c.sendMessage("The item has been dropped on the floor.");
			Server.itemHandler.createGroundItem(c, itemId, c.absX, c.absY, c.heightLevel, amount);
			return;
		}
		long totalAmount = ((long) tab.getItemAmount(item) + (long) item.getAmount());
		if (totalAmount >= Integer.MAX_VALUE) {
			c.sendMessage("The item has been dropped on the floor.");
			Server.itemHandler.createGroundItem(c, itemId, c.absX, c.absY, c.heightLevel, amount);
			return;
		}
		tab.add(item);
		resetTempItems();
		if (c.isBanking)
			resetBank();
		c.sendMessage(getItemName(itemId) + " x" + item.getAmount() + " has been added to your bank.");
	}

	public void addItemToBank(String playerName, int itemId, int amount) {
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemId + 1, amount);
		Iterator<BankTab> iterator = Arrays.asList(c.getBank().getBankTab()).iterator();
		while (iterator.hasNext()) {
			BankTab t = iterator.next();
			if (t != null && t.size() > 0) {
				Iterator<BankItem> iterator2 = t.getItems().iterator();
				while (iterator2.hasNext()) {
					BankItem i = iterator2.next();
					if (i.getId() == item.getId() && !isNotable(itemId)) {
						if (t.getTabId() != tab.getTabId()) {
							tab = t;
							c.getBank().setCurrentBankTab(tab);
							resetBank();
						}
					} else {
						if (isNotable(itemId) && i.getId() == item.getId() - 1) {
							item = new BankItem(itemId, amount);
							if (t.getTabId() != tab.getTabId()) {
								tab = t;
								c.getBank().setCurrentBankTab(tab);
								resetBank();
							}
						}
					}
				}
			}
		}
		if (isNotable(itemId))
			item = new BankItem(itemId, amount);
		if (tab.freeSlots() == 0) {
			c.sendMessage("The item has been dropped on the floor.");
			Server.itemHandler.createGroundItem(c, itemId, c.absX, c.absY, c.heightLevel, amount);
			return;
		}
		long totalAmount = ((long) tab.getItemAmount(item) + (long) item.getAmount());
		if (totalAmount >= Integer.MAX_VALUE) {
			c.sendMessage("The item has been dropped on the floor.");
			Server.itemHandler.createGroundItem(c, itemId, c.absX, c.absY, c.heightLevel, amount);
			return;
		}
		tab.add(item);
		resetTempItems();
		if (c.isBanking)
			resetBank();
		c.sendMessage(getItemName(itemId) + " x" + item.getAmount() + " has been added to your bank.");
	}

	public boolean contains(int... items) {
		for (int it : items) {
			if (!playerHasItem(it, 1)) {
				return false;
			}
		}
		return true;
	}

	public int getWornItemAmount(int itemID) {
		for (int i = 0; i < 12; i++) {
			if (c.playerEquipment[i] == itemID) {
				return c.playerEquipmentN[i];
			}
		}
		return 0;
	}

	public int getWornItemSlot(int itemId) {
		for (int i = 0; i < c.playerEquipment.length; i++)
			if (c.playerEquipment[i] == itemId)
				return i;
		return -1;
	}

	public boolean isWearingItem(int itemID) {
		for (int i = 0; i < 12; i++) {
			if (c.playerEquipment[i] == itemID) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Items
	 */
	public int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 }, { 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 }, { 4720, 4896 }, { 4718, 4890 }, { 4722, 4902 }, { 4732, 4932 }, { 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 }, { 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 }, { 4745, 4956 }, { 4747, 4962 }, { 4749, 4968 }, { 4751, 4974 }, { 4753, 4980 }, { 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	public void resetItems(int WriteFrame) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(WriteFrame);
			c.getOutStream().writeWord(c.playerItems.length);
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.playerItemsN[i]);
				}
				c.getOutStream().writeWordBigEndianA(c.playerItems[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void writeBonus() {
		int offset = 0;
		String send;
		for (int i = 0; i < c.playerBonus.length; i++) {
			if (c.playerBonus[i] >= 0) {
				send = BONUS_NAMES[i] + ": +" + c.playerBonus[i];
			} else {
				send = BONUS_NAMES[i] + ": -" + java.lang.Math.abs(c.playerBonus[i]);
			}

			if (i == 10) {
				offset = 1;
			}
			c.getPA().sendFrame126(send, (1675 + i + offset));
		}

	}

	public boolean isNotable(int itemId) {
		for (ItemList list : Server.itemHandler.ItemList)
			if (list != null)
				if (list.itemId == itemId)
					if (list.itemDescription.startsWith("Swap this note at any bank"))
						return true;
		return false;
	}

	public void sendItemsKept() {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6963);
			c.getOutStream().writeWord(c.itemKeptId.length);
			for (int i = 0; i < c.itemKeptId.length; i++) {
				if (c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(1);
				} else {
					c.getOutStream().writeByte(1);
				}
				if (c.itemKeptId[i] > 0) {
					c.getOutStream().writeWordBigEndianA(c.itemKeptId[i] + 1);
				} else {
					c.getOutStream().writeWordBigEndianA(0);
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	/**
	 * Item kept on death
	 * 
	 * @param keepItem
	 *            Keep item id
	 * @param deleteItem
	 *            Delete item id
	 */

	public void keepItem(int keepItem, boolean deleteItem) {
		int value = 0;
		int item = 0;
		int slotId = 0;
		boolean itemInInventory = false;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] - 1 > 0) {
				Optional<ItemList> def = ItemAssistant.getItemDef(c.playerItems[i] - 1);
				if (!def.isPresent())
					continue;
				int inventoryItemValue = (int) def.get().ShopValue;
				if (inventoryItemValue > value && (!c.invSlot[i])) {
					value = inventoryItemValue;
					item = c.playerItems[i] - 1;
					slotId = i;
					itemInInventory = true;
				}
			}
		}
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			if (c.playerEquipment[i1] > 0) {
				int equipmentItemValue = (int) ItemAssistant.getItemDef(c.playerEquipment[i1]).get().ShopValue;
				if (equipmentItemValue > value && (!c.equipSlot[i1])) {
					value = equipmentItemValue;
					item = c.playerEquipment[i1];
					slotId = i1;
					itemInInventory = false;
				}
			}
		}
		if (itemInInventory) {
			c.invSlot[slotId] = true;
			if (deleteItem) {
				deleteItem(c.playerItems[slotId] - 1, getItemSlot(c.playerItems[slotId] - 1), 1);
			}
		} else {
			c.equipSlot[slotId] = true;
			if (deleteItem) {
				deleteEquipment(item, slotId);
			}
		}
		c.itemKeptId[keepItem] = item;
	}

	public void replaceItem(Player c, int i, int l) {
		for (int k = 0; k < c.playerItems.length; k++) {
			if (playerHasItem(i, 1)) {
				deleteItem(i, getItemSlot(i), 1);
				addItem(l, 1);
			}
		}
	}

	/**
	 * Reset items kept on death
	 */

	public void resetKeepItems() {
		for (int i = 0; i < c.itemKeptId.length; i++) {
			c.itemKeptId[i] = -1;
		}
		for (int i1 = 0; i1 < c.invSlot.length; i1++) {
			c.invSlot[i1] = false;
		}
		for (int i2 = 0; i2 < c.equipSlot.length; i2++) {
			c.equipSlot[i2] = false;
		}
	}

	/**
	 * delete all items
	 */

	public void deleteAllItems() {
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			deleteEquipment(c.playerEquipment[i1], i1);
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			deleteItem(c.playerItems[i] - 1, getItemSlot(c.playerItems[i] - 1), c.playerItemsN[i]);
		}
	}

	/**
	 * Drop all items for your killer
	 */

	public void dropAllItems() {
		// one thing I can think to do but probably wont fix it
		// verify that you aren't the killer, so the drop will always be yours
		// if you are the killer
		// Well i can test it and see if i have any occurences after this
		Player killer = ((c.killerId != -1 && c.killerId != c.getIndex()) ? World.PLAYERS.get(c.killerId) : null);
		if (Objects.nonNull(killer)) {
			if (killer.getAccount() != null && c.getAccount() != null && !killer.getAccount().getType().attackableTypes().contains(c.getAccount().getType().alias())) {
				killer.sendMessage("You do not receive drops from this player.");
				return;
			}
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			if (killer != null) {
				if (c.getItems().playerHasItem(10525)) {
					ResourceBag.deleteItem(c, c.playerItems[i], c.playerItemsN[i]);
				}
				if (tradeable(c.playerItems[i] - 1)) {
					Server.itemHandler.createGroundItem(killer, c.playerItems[i] - 1, c.getX(), c.getY(), c.getHeight(), c.playerItemsN[i]);
				} else {
					if (specialCase(c.playerItems[i] - 1))
						Server.itemHandler.createGroundItem(killer, 995, c.getX(), c.getY(), c.getHeight(), getUntradePrice(c.playerItems[i] - 1));
					Server.itemHandler.createGroundItem(c, c.playerItems[i] - 1, c.getX(), c.getY(), c.getHeight(), c.playerItemsN[i]);
				}
			} else {
				Server.itemHandler.createGroundItem(c, c.playerItems[i] - 1, c.getX(), c.getY(), c.getHeight(), c.playerItemsN[i]);
			}
		}
		for (int e = 0; e < c.playerEquipment.length; e++) {
			if (killer != null) {
				if (tradeable(c.playerEquipment[e])) {
					Server.itemHandler.createGroundItem(killer, c.playerEquipment[e], c.getX(), c.getY(), c.getHeight(), c.playerEquipmentN[e]);
				} else {
					if (specialCase(c.playerEquipment[e]))
						Server.itemHandler.createGroundItem(killer, 995, c.getX(), c.getY(), c.getHeight(), getUntradePrice(c.playerEquipment[e]));
					Server.itemHandler.createGroundItem(c, c.playerEquipment[e], c.getX(), c.getY(), c.getHeight(), c.playerEquipmentN[e]);
				}
			} else {
				Server.itemHandler.createGroundItem(c, c.playerEquipment[e], c.getX(), c.getY(), c.getHeight(), c.playerEquipmentN[e]);
			}
		}
		Server.itemHandler.createGroundItem(killer != null ? killer : c, 526, c.getX(), c.getY(), c.getHeight(), 1);
	}

	public int getUntradePrice(int item) {
		switch (item) {
		case 2518:
		case 2524:
		case 2526:
			return 100000;
		case 2520:
		case 2522:
			return 150000;
		}
		return 0;
	}

	public boolean specialCase(int itemId) {
		switch (itemId) {
		case 2518:
		case 2520:
		case 2522:
		case 2524:
		case 2526:
			return true;
		}
		return false;
	}

	public boolean tradeable(int itemId) {
		for (int j = 0; j < Constants.ITEM_TRADEABLE.length; j++) {
			if (itemId == Constants.ITEM_TRADEABLE[j])
				return false;
		}
		return true;
	}

	public boolean replaceItem(int itemHave, int replaceWith) {
		if (!playerHasItem(itemHave)) {
			return false;
		}
		deleteItem(itemHave, 1);
		addItem(replaceWith, 1);
		return true;
	}

	public boolean addItem(int item, int amount, int slot) {
		if (amount < 1) {
			amount = 1;
		}
		if (item <= 0) {
			return false;
		}
		if (c.playerItems[slot] > 0 || c.playerItemsN[slot] > 0) {
			return addItem(item, amount);
		}
		c.playerItems[slot] = (item + 1);
		if (c.playerItemsN[slot] + amount < Constants.MAXITEM_AMOUNT && c.playerItemsN[slot] + amount > -1) {
			c.playerItemsN[slot] += amount;
		} else {
			c.playerItemsN[slot] = Constants.MAXITEM_AMOUNT;
		}
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(3214);
			c.getOutStream().writeByte(slot);
			c.getOutStream().writeWord(c.playerItems[slot]);
			if (c.playerItemsN[slot] > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(c.playerItemsN[slot]);
			} else {
				c.getOutStream().writeByte(c.playerItemsN[slot]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
		return false;
	}

	/**
	 * Add Item
	 * 
	 * @param item
	 *            item id
	 * @param amount
	 *            the amount
	 * @return return
	 */
	public boolean addItem(int item, int amount) {
		if (amount < 1)
			amount = 1;
		if (item <= 0)
			return false;
		if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && Item.itemStackable[item])
				|| ((freeSlots() > 0) && !Item.itemStackable[item])) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] == item + 1 && Item.itemStackable[item]
						&& c.playerItems[i] > 0) {
					c.playerItems[i] = (item + 1);
					if (c.playerItemsN[i] + amount < Constants.MAXITEM_AMOUNT
							&& c.playerItemsN[i] + amount > -1)
						c.playerItemsN[i] += amount;
					else
						c.playerItemsN[i] = Constants.MAXITEM_AMOUNT;
					if (c.getOutStream() != null && c != null) {
						c.getOutStream().createFrameVarSizeWord(34);
						c.getOutStream().writeWord(3214);
						c.getOutStream().writeByte(i);
						c.getOutStream().writeWord(c.playerItems[i]);
						if (c.playerItemsN[i] > 254) {
							c.getOutStream().writeByte(255);
							c.getOutStream().writeDWord(c.playerItemsN[i]);
						} else
							c.getOutStream().writeByte(c.playerItemsN[i]);
						c.getOutStream().endFrameVarSizeWord();
						c.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] <= 0) {
					c.playerItems[i] = item + 1;
					if (amount < Constants.MAXITEM_AMOUNT && amount > -1) {
						c.playerItemsN[i] = 1;
						if (amount > 1) {
							c.getItems().addItem(item, amount - 1);
							return true;
						}
					} else
						c.playerItemsN[i] = Constants.MAXITEM_AMOUNT;
					resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			resetItems(3214);
			c.sendMessage("Not enough space in your inventory.");
			return false;
		}
	}



	public void addItem(Item item) {
		addItem(item.getId(), item.getCount());
	}

	public void addItemToBank(Item item) {
		addItemToBank(item.getId(), item.getCount());
	}

	public String itemType(int item) {

		if (Item.playerCape(item)) {
			return "cape";
		}
		if (Item.playerBoots(item)) {
			return "boots";
		}
		if (Item.playerGloves(item)) {
			return "gloves";
		}
		if (Item.playerShield(item)) {
			return "shield";
		}
		if (Item.playerAmulet(item)) {
			return "amulet";
		}
		if (Item.playerArrows(item)) {
			return "arrows";
		}
		if (Item.playerRings(item)) {
			return "ring";
		}
		if (Item.playerHats(item)) {
			return "hat";
		}
		if (Item.playerLegs(item)) {
			return "legs";
		}
		if (Item.playerBody(item)) {
			return "body";
		}
		return "weapon";
	}

	/**
	 * Bonuses
	 */

	public final String[] BONUS_NAMES = { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range", "Strength", "Prayer" };

	public void resetBonus() {
		for (int i = 0; i < c.playerBonus.length; i++) {
			c.playerBonus[i] = 0;
		}
	}

	public void getBonus() {
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > -1) {
				for (int j = 0; j < Constants.ITEM_LIMIT; j++) {
					if (Server.itemHandler.ItemList[j] != null) {
						if (Server.itemHandler.ItemList[j].itemId == c.playerEquipment[i]) {
							for (int k = 0; k < c.playerBonus.length; k++) {
								c.playerBonus[k] += Server.itemHandler.ItemList[j].Bonuses[k];
							}
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Wear Item
	 * 
	 * @param Weapon
	 *            the Weapon.
	 * @param WeaponName
	 *            the Weapon's name.
	 */

	public void sendWeapon(int Weapon, String WeaponName) {
		String WeaponName2 = WeaponName.replaceAll("Bronze", "");
		WeaponName2 = WeaponName2.replaceAll("Iron", "");
		WeaponName2 = WeaponName2.replaceAll("Steel", "");
		WeaponName2 = WeaponName2.replaceAll("Black", "");
		WeaponName2 = WeaponName2.replaceAll("Mithril", "");
		WeaponName2 = WeaponName2.replaceAll("Adamant", "");
		WeaponName2 = WeaponName2.replaceAll("Rune", "");
		WeaponName2 = WeaponName2.replaceAll("Granite", "");
		WeaponName2 = WeaponName2.replaceAll("Dragon", "");
		WeaponName2 = WeaponName2.replaceAll("Drag", "");
		WeaponName2 = WeaponName2.replaceAll("Crystal", "");
		WeaponName2 = WeaponName2.trim();
		if (WeaponName.equals("Unarmed")) {
			c.setSidebarInterface(0, 5855); // punch, kick, block
			c.getPA().sendFrame126(WeaponName, 5857);
		} else if (WeaponName.endsWith("whip")) {
			c.setSidebarInterface(0, 12290); // flick, lash, deflect
			c.getPA().sendFrame246(12291, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 12293);
		} else if (WeaponName.endsWith("bow") || WeaponName.endsWith("Blowpipe") || WeaponName.endsWith("10") || WeaponName.endsWith("full") || WeaponName.startsWith("" + "seercull") || WeaponName.endsWith("salamander")) {
			c.setSidebarInterface(0, 1764); // accurate, rapid, longrange
			c.getPA().sendFrame246(1765, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 1767);
		} else if (WeaponName.startsWith("Staff") || WeaponName.endsWith("staff") || WeaponName.endsWith("wand")) {
			c.setSidebarInterface(0, 328); // spike, impale, smash, block
			c.getPA().sendFrame246(329, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 331);
		} else if (WeaponName2.endsWith("chinchompa") || WeaponName2.startsWith("dart") || WeaponName2.startsWith("knife") || WeaponName2.startsWith("javelin") || WeaponName.equalsIgnoreCase("toktz-xil-ul")) {
			c.setSidebarInterface(0, 4446); // accurate, rapid, longrange
			c.getPA().sendFrame246(4447, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 4449);
		} else if (WeaponName2.startsWith("dagger") || WeaponName2.contains("Abyssaldagger") || WeaponName2.contains("sword") || WeaponName2.contains("dark") || c.playerEquipment[Player.playerWeapon] == 6746 || c.playerEquipment[Player.playerWeapon] == 7158) {
			c.setSidebarInterface(0, 2276); // stab, lunge, slash, block
			c.getPA().sendFrame246(2277, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 2279);
		} else if (WeaponName2.endsWith("godsword")) {
			c.setSidebarInterface(0, 2423); // chop, slash, lunge, block
			c.getPA().sendFrame246(2424, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 2426);
		} else if (WeaponName2.startsWith("pickaxe")) {
			c.setSidebarInterface(0, 5570); // spike, impale, smash, block
			c.getPA().sendFrame246(5571, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 5573);
		} else if (WeaponName2.startsWith("axe") || WeaponName2.startsWith("battleaxe")) {
			c.setSidebarInterface(0, 1698); // chop, hack, smash, block
			c.getPA().sendFrame246(1699, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 1701);
		} else if (WeaponName2.startsWith("halberd") || WeaponName2.contains("Scythe")) {
			c.setSidebarInterface(0, 8460); // jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 8463);
		} else if (WeaponName2.contains("claw")) {
			c.setSidebarInterface(0, 7762); // claws
			c.getPA().sendFrame246(7763, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 7765);
		} else if (WeaponName2.startsWith("spear")) {
			c.setSidebarInterface(0, 4679); // lunge, swipe, pound, block
			c.getPA().sendFrame246(4680, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 4682);
		} else if (WeaponName2.contains("mace") || WeaponName2.endsWith("anchor")) {
			c.setSidebarInterface(0, 3796);
			c.getPA().sendFrame126(WeaponName, 3799);
		} else if (WeaponName2.contains("warhammer") || WeaponName2.toLowerCase().contains("tzhaar") || WeaponName2.contains("flail") || WeaponName2.contains("maul")) {
			c.setSidebarInterface(0, 425); // war hamer equip.
			c.getPA().sendFrame246(426, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 428);
		} else {
			c.setSidebarInterface(0, 2423); // chop, slash, lunge, block
			c.getPA().sendFrame246(2424, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 2426);
		}
	}

	/**
	 * Weapon Requirements
	 * 
	 * @param itemName
	 *            Item's name.
	 * @param itemId
	 *            Item's id.
	 */

	public void getRequirements(String itemName, int itemId) {
		c.attackLevelReq = c.defenceLevelReq = c.strengthLevelReq = c.rangeLevelReq = c.prayerLevelReq = c.magicLevelReq = c.undergroundReq = c.horrorReq = c.cookReq = c.hunterLevelReq = 0;
		if (itemName.contains("mystic") || itemName.contains("nchanted")) {
			if (itemName.contains("staff")) {
				c.magicLevelReq = 20;
				c.attackLevelReq = 40;
			} else {
				c.magicLevelReq = 20;
				c.defenceLevelReq = 20;
			}
		}
		if (itemName.contains("rock-shell") || itemName.contains("skeletal") || itemName.contains("spined")) {
			c.defenceLevelReq = 40;
			return;
		}
		if (itemName.contains("adam plate") || itemName.contains("adam full")) {
			c.defenceLevelReq = 30;
		}

		if (itemName.contains("kyatt")) {
			c.hunterLevelReq = 52;
		}
		if (itemName.contains("infinity")) {
			c.magicLevelReq = 50;
			c.defenceLevelReq = 25;
		}
		if (itemName.contains("splitbark")) {
			c.magicLevelReq = 40;
			c.defenceLevelReq = 40;
		}
		if (itemName.contains("vesta's longsword") || itemName.contains("vesta's spear") || itemName.contains("statius's warhammer")) {
			c.attackLevelReq = 78;
		}
		if (itemName.contains("toxic blowpipe")) {
			c.rangeLevelReq = 76;
			return;

		}
		if (itemName.contains("rune c'bow")) {
			c.rangeLevelReq = 61;
			return;
		}
		if (itemId >= 2653 && itemId <= 2675) {
			c.defenceLevelReq = 40;
			return;
		}
		if ((itemName.contains("guthix") || itemName.contains("saradomin") || itemName.contains("zamorak")) && !itemName.contains("sword")) {
			if (itemName.contains("saradomin sword")) {
				c.attackLevelReq = 70;
				return;
			} else if (itemName.contains("godsword")) {
				c.attackLevelReq = 75;
				return;
			} else if (itemName.contains("d'hide") || itemName.contains("coif") || itemName.contains("bracers") || itemName.contains("dragonhide")) {
				c.defenceLevelReq = 40;
				c.rangeLevelReq = 70;
				return;
			} else if (itemName.contains("chaps")) {
				c.rangeLevelReq = 70;
				return;
			}
			return;
		}
		if (itemName.contains("black d'hide")) {
			if (itemName.contains("body")) {
				c.defenceLevelReq = 40;
			}
			c.rangeLevelReq = 70;
		}
		if (itemName.contains("dragonfire")) {
			c.defenceLevelReq = 75;
		}
		if (itemName.contains("granite")) {
			c.defenceLevelReq = 50;
		}
		if (itemName.contains("tzhaar")) {
			c.strengthLevelReq = 60;
		}
		if (itemName.contains("red d'hide")) {
			c.rangeLevelReq = 60;
		}
		if (itemName.contains("blue d'hide")) {
			c.rangeLevelReq = 50;
		}
		if (itemName.contains("green d'hide")) {
			c.rangeLevelReq = 40;
		}
		if (itemName.contains("initiate")) {
			c.defenceLevelReq = 20;
			c.prayerLevelReq = 10;
		}
		if (itemName.contains("bronze")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("iron")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("steel")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 5;
			}
			return;
		}
		if (itemName.contains("black")) {
			if (!itemName.contains("d'hide") && !itemName.contains("knife") && !itemName.contains("cavalier") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("ele'") && !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 10;
			}
			return;
		}
		if (itemName.contains("mithril")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 20;
			}
			return;
		}
		if (itemName.contains("adamant")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 30;
			}
			return;
		}
		if (itemName.contains("rune") || itemName.contains("gilded") || itemName.contains("decorative")) {
			if (!itemName.contains("hood") && !itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe") && !itemName.contains("'bow")) {
				c.attackLevelReq = c.defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("dragon")) {
			if (!itemName.contains("nti-") && !itemName.contains("fire")) {
				c.attackLevelReq = c.defenceLevelReq = 60;
				return;
			}
		}
		if (itemName.contains("crystal")) {
			if (itemName.contains("shield")) {
				c.defenceLevelReq = 70;
			} else {
				c.rangeLevelReq = 70;
			}
			return;
		}
		if (itemName.contains("ahrim")) {
			if (itemName.contains("staff")) {
				c.magicLevelReq = 70;
				c.attackLevelReq = 70;
			} else {
				c.magicLevelReq = 70;
				c.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("karil")) {
			if (itemName.contains("crossbow")) {
				c.rangeLevelReq = 70;
			} else {
				c.rangeLevelReq = 70;
				c.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("godsword")) {
			c.attackLevelReq = 75;
		}
		if (itemName.contains("3rd age") && !itemName.contains("amulet")) {
			c.defenceLevelReq = 60;
		}
		if (itemName.contains("verac") || itemName.contains("guthan") || itemName.contains("dharok") || itemName.contains("torag")) {

			if (itemName.contains("hammers")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("axe")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("warspear")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("flail")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else {
				c.defenceLevelReq = 70;
			}
		}
		if (itemName.endsWith("spirit shield")) {
			if (itemName.startsWith("elysian")) {
				c.defenceLevelReq = 75;
				c.prayerLevelReq = 70;
			} else if (itemName.startsWith("divine")) {
				c.defenceLevelReq = 75;
				c.prayerLevelReq = 70;
			} else if (itemName.startsWith("spectral")) {
				c.defenceLevelReq = 75;
				c.prayerLevelReq = 70;
				c.magicLevelReq = 65;
			} else if (itemName.startsWith("arcane")) {
				c.defenceLevelReq = 75;
				c.prayerLevelReq = 70;
				c.magicLevelReq = 65;
			} else if (itemName.startsWith("blessed")) {
				c.defenceLevelReq = 70;
				c.prayerLevelReq = 60;
			} else {
				c.defenceLevelReq = 40;
				c.prayerLevelReq = 55;
			}
			return;
		}
		if (itemName.contains("deg")) {
			c.defenceLevelReq = 20;
			return;
		}

		if (itemName.contains("zuriel")) {
			c.defenceLevelReq = 78;
			c.magicLevelReq = 78;
			return;
		}
		if (itemId >= 7370 && itemId <= 7385) {
			c.rangeLevelReq = 50;
			if (itemName.contains("body")) {
				c.defenceLevelReq = 40;
			}
			return;
		}
		switch (itemId) {
		case 12346:
			c.magicLevelReq = 80;
			break;
		case 1409:
			c.magicLevelReq = 50;
			c.attackLevelReq = 50;
			c.undergroundReq = 5;
			return;
		case 10033:
			c.rangeLevelReq = 45;
			return;
		case 10034:
			c.rangeLevelReq = 55;
			return;
		case 15021:
		case 12460:
			c.attackLevelReq = 75;
			c.magicLevelReq = 75;
			break;
		case 12349:
			c.rangeLevelReq = 70;
			break;

		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			c.attackLevelReq = 42;
			c.rangeLevelReq = 42;
			c.strengthLevelReq = 42;
			c.magicLevelReq = 42;
			c.defenceLevelReq = 42;
			return;
		case 10887:
			c.strengthLevelReq = 60;
			break;
		case 10551:
		case 2503:
		case 2501:
		case 2499:
		case 1135:
		case 13263:
		case 10547:
		case 10548:
		case 10549:
		case 10550:
		case 10552:
		case 10553:
		case 10555:
			c.defenceLevelReq = 40;
			return;
		case 3753:
			c.defenceLevelReq = 45;
			break;
		case 11235:
		case 6522:
			c.rangeLevelReq = 60;
			break;
		case 6524:
			c.defenceLevelReq = 60;
			break;
		case 11284:
		case 11283:
			c.defenceLevelReq = 75;
			return;
		case 6889:
		case 6914:
			c.magicLevelReq = 60;
			break;
		case 861:
		case 859:
			c.rangeLevelReq = 50;
			break;
		case 10828:
			c.defenceLevelReq = 55;
			break;
		case 11724:
		case 11726:
		case 11728:
			c.defenceLevelReq = 65;
			break;
		case 3751:
			c.defenceLevelReq = 45;
			break;
		case 3749:
		case 3755:
			c.defenceLevelReq = 40;
			break;

		case 7462:
		case 7461:
			c.defenceLevelReq = 40;
			break;
		case 8846:
			c.defenceLevelReq = 5;
			break;
		case 8847:
			c.defenceLevelReq = 10;
			break;
		case 8848:
			c.defenceLevelReq = 20;
			break;
		case 8849:
			c.defenceLevelReq = 30;
			break;
		case 15496:

			c.defenceLevelReq = 60;

			break;
		case 8850:
			c.defenceLevelReq = 40;
			break;

		case 7460:
			c.defenceLevelReq = 20;
			break;

		case 5698:
		case 14611:
		case 13576:
			c.attackLevelReq = 60;
			break;
			
		case 11730:
		case 13047:
		case 13049:
			c.attackLevelReq = 70;
			break;

		case 837:
			c.rangeLevelReq = 61;
			break;
		case 15343:
		case 15052:
		case 15053:
		case 4151: // if you don't want to use names
		case 13758:
		case 13764:
		case 13740:
		case 13741:
			c.attackLevelReq = 70;
			break;

		case 15001: // if you don't want to use names
			c.magicLevelReq = 75;
			return;

		case 15000: // if you don't want to use names
			c.magicLevelReq = 75;
			break;
		case 11718:
		case 11720:
		case 11722:
			c.defenceLevelReq = 70;
			break;

		case 6724: // seercull
			c.rangeLevelReq = 60; // idk if that is correct
			return;
		case 4675:
		case 14604:
			c.attackLevelReq = 50;
		case 4153:
			c.attackLevelReq = 50;
			c.strengthLevelReq = 50;
			break;
		case 13045:
			c.attackLevelReq = 70;
			c.strengthLevelReq = 70;
			break;
		}
	}

	/**
	 * two handed weapon check
	 * 
	 * @param itemName
	 *            item's name
	 * @param itemId
	 *            the item's id
	 * @return return;
	 */

	public boolean is2handed(String itemName, int itemId) {
		if (itemName.contains("ahrim") || itemName.contains("karil") || itemName.contains("verac") || itemName.contains("guthan") || itemName.contains("dharok") || itemName.contains("torag")) {
			return true;
		}
		if (itemName.contains("longbow") || itemName.contains("shortbow") || itemName.contains("ark bow") || itemName.contains("training bow")) {
			return true;
		}
		if (itemName.contains("crystal")) {
			return true;
		}
		if (itemName.contains("godsword") || itemName.contains("claws") || itemName.contains("saradomin sword") || itemName.contains("2h") || itemName.contains("spear") || itemName.contains("Abyssal Blundgean")) {
			return true;
		}
		switch (itemId) {
		case 14614:
		case 10887:
		case 6724: // seercull
		case 11730:
		case 6526:
		case 4153:
		case 3204:
		case 14484:
		case 6528:
		case 11777:
		case 10146:
		case 10147:
		case 10148:
			return true;
		}
		return false;
	}

	/**
	 * Weapons special bar, adds the spec bars to weapons that require them and
	 * removes the spec bars from weapons which don't require them
	 * 
	 * @param weapon
	 *            Weapon's id
	 */

	public void addSpecialBar(int weapon) {
		switch (weapon) {

		case 4151: // whip
		case 13758:
		case 15343:
		case 13764:
		case 13740:
		case 13741:
			c.getPA().sendFrame171(0, 12323);
			specialAmount(weapon, c.specAmount, 12335);
			break;

		case 859: // magic bows
		case 861:
		case 14614:
		case 11235:
		case 12349:
			c.getPA().sendFrame171(0, 7549);
			specialAmount(weapon, c.specAmount, 7561);
			break;
		case 14610:
		case 13047:
		case 13049:
		case 4587: // dscimmy
			c.getPA().sendFrame171(0, 7599);
			specialAmount(weapon, c.specAmount, 7611);
			break;

		case 3204:
		case 15045:              // d hally
			c.getPA().sendFrame171(0, 8493);
			specialAmount(weapon, c.specAmount, 8505);
			break;

		case 1377: // d battleaxe
			c.getPA().sendFrame171(0, 7499);
			specialAmount(weapon, c.specAmount, 7511);
			break;

		case 4153:
		case 13576:// gmaul
			c.getPA().sendFrame171(0, 7474);
			specialAmount(weapon, c.specAmount, 7486);
			break;

		case 1249: // dspear
			c.getPA().sendFrame171(0, 7674);
			specialAmount(weapon, c.specAmount, 7686);
			break;

		case 1215:// dragon dagger
		case 6746:
		case 1231:
		case 5680:
		case 7158:
		case 5698:
		case 14611:
		case 1305: // dragon long
		case 11694:
		case 11698:
		case 11700:
		case 11730:
		case 11696:
			c.getPA().sendFrame171(0, 7574);
			specialAmount(weapon, c.specAmount, 7586);
			break;

		case 1434: // dragon mace
		case 10887:
			c.getPA().sendFrame171(0, 7624);
			specialAmount(weapon, c.specAmount, 7636);
			break;

		default:
			c.getPA().sendFrame171(1, 7624); // mace interface
			c.getPA().sendFrame171(1, 7474); // hammer, gmaul
			c.getPA().sendFrame171(1, 7499); // axe
			c.getPA().sendFrame171(1, 7549); // bow interface
			c.getPA().sendFrame171(1, 7574); // sword interface
			c.getPA().sendFrame171(1, 7599); // scimmy sword interface, for most
												// swords
			c.getPA().sendFrame171(1, 8493);
			c.getPA().sendFrame171(1, 12323); // whip interface
			break;
		}
	}

	/**
	 * Specials bar filling amount
	 * 
	 * @param weapon
	 *            Weapon's id
	 * @param specAmount
	 *            The spec's amount
	 * @param barId
	 *            The Bar's id
	 */

	public void specialAmount(int weapon, double specAmount, int barId) {
		c.specBarId = barId;
		c.getPA().sendFrame70(specAmount >= 10 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 9 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 8 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 7 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 6 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 5 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 4 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 3 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 2 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 1 ? 500 : 0, 0, (--barId));
		updateSpecialBar();
		sendWeapon(weapon, getItemName(weapon));
	}

	/**
	 * Special attack text and what to highlight or blackout
	 */

	public void updateSpecialBar() {
		if (c.usingSpecial) {
			c.getPA().sendFrame126("" + (c.specAmount >= 2 ? "@yel@S P" : "@bla@S P") + "" + (c.specAmount >= 3 ? "@yel@ E" : "@bla@ E") + "" + (c.specAmount >= 4 ? "@yel@ C I" : "@bla@ C I") + "" + (c.specAmount >= 5 ? "@yel@ A L" : "@bla@ A L") + "" + (c.specAmount >= 6 ? "@yel@  A" : "@bla@  A") + "" + (c.specAmount >= 7 ? "@yel@ T T" : "@bla@ T T") + "" + (c.specAmount >= 8 ? "@yel@ A" : "@bla@ A") + "" + (c.specAmount >= 9 ? "@yel@ C" : "@bla@ C") + "" + (c.specAmount >= 10 ? "@yel@ K" : "@bla@ K"), c.specBarId);
		} else {
			c.getPA().sendFrame126("@bla@S P E C I A L  A T T A C K", c.specBarId);
		}
	}

	/**
	 * Skill Capes
	 * 
	 * @param capeId
	 *            Skill Cape item id.
	 * @return return id.
	 */
	private int skillCapes(int capeId) {
		switch (capeId) {
		case 9747:
		case 9748:
		case 9749:
		case 11600:
			return 0;
		case 9753:
		case 9754:
		case 9755:
			return 1;
		case 9750:
		case 9751:
		case 9752:
			return 2;
		case 9768:
		case 9769:
		case 9770:
			return 3;
		case 9756:
		case 9757:
		case 9758:
			return 4;
		case 9759:
		case 9760:
		case 9761:
			return 5;
		case 9762:
		case 9763:
		case 9764:
			return 6;
		}
		return -1;
	}

	/**
	 * Wear Item
	 * 
	 * @param wearID
	 *            the item id
	 * @param slot
	 *            the slot
	 * @return return;
	 */

	public boolean wearItem(int wearID, int slot) {
		if (wearID == 3281 && !Constants.legendPlayer(c)) {
			c.sendMessage("This a Legendaey weapon only!");
			return false;
		}
		if (wearID == 3281 && c.getAccount().getType().equals(Account.IRON_MAN_TYPE)) {
			c.sendMessage("This cannot be equipted by an Iron man.");
			return false;
		}
		if (wearID == 12345) {
			c.sendMessage("Your Trident of the Seas is empty, speak to Otto in varrock to charge it.");
			return false;
		}
		/*if (wearID == 3844 && c.horrorQP < 2) {
			c.sendMessage("You need to complete the Horror from the Deep Quest to wear this.");
			return false;
		}
		if (wearID == 3842 && c.horrorQP < 2) {
			c.sendMessage("You need to complete the Horror from the Deep Quest to wear this.");
			return false;
		}
		if (wearID == 3840 && c.horrorQP < 2) {
			c.sendMessage("You need to complete the Horror from the Deep Quest to wear this.");
			return false;
		}
		if (wearID == 10662 && c.horrorQP < 3 && c.undergroundQP < 5 && c.cookQP < 2) {
			c.sendMessage("You need to complete all quests to wear this cape.");
			return false;
		}*/
		/*if (wearID == 9814 && c.horrorQP < 3 && c.undergroundQP < 5 && c.cookQP < 2) {
			c.sendMessage("You need to complete all quests to wear this hood.");
			return false;
		}*/
		// if (wearID == 6465 && !Constants.premiumRights(c)) {
		// c.sendMessage("This is a Premium only item");
		// return false;
		// }
		if (wearID == 9813 && !c.canWearCape) {
			c.sendMessage("You need to have completed all of the Achievements to wear this cape");
			return false;
		}
		/*
		 * if (wearID == 15812 && !c.isMaxed() ) {
		 * c.sendMessage("You must be a maxed to wear the completionist cape.");
		 * c
		 * .sendMessage("All achievements must be completed to wear this cape");
		 * return false; } if (wearID == 15378 && !c.isMaxed() ) {
		 * c.sendMessage(
		 * "You must be a legend & maxed to wear the completionist cape.");
		 * c.sendMessage
		 * ("All achievements must be completed to wear this cape"); return
		 * false; } if (wearID == 15377 && !c.isMaxed() ) { c.sendMessage(
		 * "You must be a legend & maxed to wear the completionist cape.");
		 * c.sendMessage
		 * ("All achievements must be completed to wear this cape"); return
		 * false; } if (wearID == 15376 && !c.isMaxed() ) { c.sendMessage(
		 * "You must be a legend & maxed to wear the completionist cape.");
		 * c.sendMessage
		 * ("All achievements must be completed to wear this cape"); return
		 * false; } if (wearID == 15375 && !c.isMaxed() ) { c.sendMessage(
		 * "You must be a legend & maxed to wear the completionist cape.");
		 * c.sendMessage
		 * ("All achievements must be completed to wear this cape"); return
		 * false; } if (wearID == 15374 && !c.isMaxed() ) { c.sendMessage(
		 * "You must be a legend & maxed to wear the completionist cape.");
		 * c.sendMessage
		 * ("All achievements must be completed to wear this cape"); return
		 * false; } if (wearID == 15373 && !c.isMaxed() ) { c.sendMessage(
		 * "You must be a legend & maxed to wear the completionist cape.");
		 * c.sendMessage
		 * ("All achievements must be completed to wear this cape"); return
		 * false; }
		 */
		int targetSlot;
		boolean canWearItem = true;
		if (itemType(wearID).equalsIgnoreCase("cape")) {
			targetSlot = 1;
		} else if (itemType(wearID).equalsIgnoreCase("hat")) {
			targetSlot = 0;
		} else if (itemType(wearID).equalsIgnoreCase("amulet")) {
			targetSlot = 2;
		} else if (itemType(wearID).equalsIgnoreCase("arrows")) {
			targetSlot = 13;
		} else if (itemType(wearID).equalsIgnoreCase("body")) {
			targetSlot = 4;
		} else if (itemType(wearID).equalsIgnoreCase("shield")) {
			targetSlot = 5;
		} else if (itemType(wearID).equalsIgnoreCase("legs")) {
			targetSlot = 7;
		} else if (itemType(wearID).equalsIgnoreCase("gloves")) {
			targetSlot = 9;
		} else if (itemType(wearID).equalsIgnoreCase("boots")) {
			targetSlot = 10;
		} else if (itemType(wearID).equalsIgnoreCase("ring")) {
			targetSlot = 12;
		} else if (itemType(wearID).equalsIgnoreCase("hood")) {
			targetSlot = 0;
		} else {
			targetSlot = 3;
		}
		switch (wearID) {
		case 1035:
			targetSlot = 4;// Slot Desired (In Equipment Tab)
			break;
		case 13281:
			targetSlot = 0;
		case 13280:
			targetSlot = 1;
		case 10838:
			targetSlot = 7;
			break;
		case 6523:
		case 6525:
		case 6522:
		case 13741:
			targetSlot = 3;
		case 15220:
			targetSlot = 12;
			break;
		case 11600:
			targetSlot = 1;
			break;
		}
		if (targetSlot == 3 && c.usingBow) {
			c.usingBow = false;
		}
		if (c.getAchievements().isAchievementItem(wearID)) {
			if (!c.getAchievements().hasBoughtItem(wearID)) {
				c.sendMessage("You must buy this from the achievement store with points before wearing.");
				return false;
			}
		}
		/** Skill Capes **/

		if (skillCapes(wearID) != -1 && Player.getLevelForXP((c.playerXP[skillCapes(wearID)])) < 99) {
			c.sendMessage("You don't meet the skill requirement to wear that item.");
			return false;
		}
		if (c.duelRule[11] && targetSlot == 0) {
			c.sendMessage("Wearing hats has been disabled in this duel!");
			return false;
		}
		if (c.duelRule[12] && targetSlot == 1) {
			c.sendMessage("Wearing capes has been disabled in this duel!");
			return false;
		}
		if (c.duelRule[13] && targetSlot == 2) {
			c.sendMessage("Wearing amulets has been disabled in this duel!");
			return false;
		}
		if (c.duelRule[14] && targetSlot == 3) {
			c.sendMessage("Wielding weapons has been disabled in this duel!");
			return false;
		}
		if (c.duelRule[15] && targetSlot == 4) {
			c.sendMessage("Wearing bodies has been disabled in this duel!");
			return false;
		}
		if ((c.duelRule[16] && targetSlot == 5) || (c.duelRule[16] && is2handed(getItemName(wearID).toLowerCase(), wearID))) {
			c.sendMessage("Wearing shield has been disabled in this duel!");
			return false;
		}
		if (c.duelRule[17] && targetSlot == 7) {
			c.sendMessage("Wearing legs has been disabled in this duel!");
			return false;
		}
		if (c.duelRule[18] && targetSlot == 9) {
			c.sendMessage("Wearing gloves has been disabled in this duel!");
			return false;
		}
		if (c.duelRule[19] && targetSlot == 10) {
			c.sendMessage("Wearing boots has been disabled in this duel!");
			return false;
		}
		if (c.duelRule[20] && targetSlot == 12) {
			c.sendMessage("Wearing rings has been disabled in this duel!");
			return false;
		}
		if (c.duelRule[21] && targetSlot == 13) {
			c.sendMessage("Wearing arrows has been disabled in this duel!");
			return false;
		}
		if (!c.getItems().playerHasItem(wearID) || c.playerItems[slot] - 1 != wearID || c.playerItemsN[slot] <= 0) {
			return false;
		}
		if (c.playerItems[slot] == (wearID + 1) && wearID == wearID) {
			getRequirements(getItemName(wearID).toLowerCase(), wearID);
			targetSlot = Item.targetSlots[wearID];
			if (targetSlot == 3) {
				if (c.usingSpecial && c.specEffect > 0) {
					c.specialDelay = 0;
					c.specEffect = 0;
				}
				c.usingSpecial = false;
				addSpecialBar(wearID);
			}
			if (itemType(wearID).equalsIgnoreCase("cape")) {
				targetSlot = 1;
			} else if (itemType(wearID).equalsIgnoreCase("hat")) {
				targetSlot = 0;
			} else if (itemType(wearID).equalsIgnoreCase("amulet")) {
				targetSlot = 2;
			} else if (itemType(wearID).equalsIgnoreCase("arrows")) {
				targetSlot = 13;
			} else if (itemType(wearID).equalsIgnoreCase("body")) {
				targetSlot = 4;
			} else if (itemType(wearID).equalsIgnoreCase("shield")) {
				targetSlot = 5;
			} else if (itemType(wearID).equalsIgnoreCase("legs")) {
				targetSlot = 7;
			} else if (itemType(wearID).equalsIgnoreCase("gloves")) {
				targetSlot = 9;
			} else if (itemType(wearID).equalsIgnoreCase("boots")) {
				targetSlot = 10;
			} else if (itemType(wearID).equalsIgnoreCase("ring")) {
				targetSlot = 12;
			} else {
				targetSlot = 3;
			}
			switch (wearID) {
			case 12708:
				targetSlot = 10;
				break;
			case 12710:
				targetSlot = 10;
				break;
			case 12712:
				targetSlot = 10;
				break;
			case 1035:
				targetSlot = 4;// Slot Desired (In Equipment Tab)
				break;
			case 10838:
				targetSlot = 7;
				break;
			case 6523:
			case 6525:
			case 6522:
				targetSlot = 3;
				break;
			case 13281:
				targetSlot = 0;
			case 13280:
				targetSlot = 1;
			}
			if (targetSlot == 3 && c.usingBow) {
				c.usingBow = false;
			}
		}
		if (Constants.itemRequirements) {
			if (targetSlot == 10 || targetSlot == 7 || targetSlot == 5 || targetSlot == 4 || targetSlot == 0 || targetSlot == 9) {
				if (c.defenceLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[1]) < c.defenceLevelReq) {
						c.sendMessage("You need a defence level of " + c.defenceLevelReq + " to wear this item.");
						canWearItem = false;
					}
				}

				if (c.rangeLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[4]) < c.rangeLevelReq) {
						c.sendMessage("You need a range level of " + c.rangeLevelReq + " to wear this item.");
						canWearItem = false;
					}
				}

				if (c.magicLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[6]) < c.magicLevelReq) {
						c.sendMessage("You need a magic level of " + c.magicLevelReq + " to wear this item.");
						canWearItem = false;
					}
				}
				if (c.prayerLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[5]) < c.prayerLevelReq) {
						c.sendMessage("You need a prayer level of " + c.prayerLevelReq + " to wear this item.");
						canWearItem = false;
					}
				}
				if (c.strengthLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[2]) < c.strengthLevelReq) {
						c.sendMessage("You need a strength level of " + c.strengthLevelReq + " to wear this item.");
						canWearItem = false;
					}
				}

				if (c.hunterLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[22]) < c.hunterLevelReq) {
						c.sendMessage("You need a Hunter level of " + c.hunterLevelReq + " to wear this item.");
						canWearItem = false;
					}
				}
			}
			if (targetSlot == 3) {
				if (c.attackLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[0]) < c.attackLevelReq) {
						c.sendMessage("You need an attack level of " + c.attackLevelReq + " to wield this weapon.");
						canWearItem = false;
					}
				}
				if (c.rangeLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[4]) < c.rangeLevelReq) {
						c.sendMessage("You need a range level of " + c.rangeLevelReq + " to wield this weapon.");
						canWearItem = false;
					}
				}
				if (c.strengthLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[2]) < c.strengthLevelReq) {
						c.sendMessage("You need a strength level of " + c.strengthLevelReq + " to wield this weapon.");
						canWearItem = false;
					}
				}
				if (c.magicLevelReq > 0) {
					if (c.getPA().getLevelForXP(c.playerXP[6]) < c.magicLevelReq) {
						c.sendMessage("You need a magic level of " + c.magicLevelReq + " to wield this weapon.");
						canWearItem = false;
					}
				}
				if (c.undergroundReq > 0) {
					if (c.undergroundQP < c.undergroundReq) {
						c.sendMessage("You need to complete the Underground Pass quest to wear this.");
						canWearItem = false;
					}
				}
				if (c.horrorReq > 0) {
					if (c.horrorQP < c.horrorReq) {
						c.sendMessage("You need to complete the Horror from the Deep quest to wear this.");
						canWearItem = false;
					}
				}
				if (c.cookReq > 0) {
					if (c.cookQP < c.cookReq) {
						c.sendMessage("You need to complete the Cook's Assistant Quest to wear this.");
						canWearItem = false;
					}
				}
			}
			if (!canWearItem) {
				return false;
			}
			if (!c.getItems().playerHasItem(wearID) || c.playerItems[slot] - 1 != wearID || c.playerItemsN[slot] <= 0) {
				return false;
			}
			int wearAmount = c.playerItemsN[slot];
			if (wearAmount < 1) {
				return false;
			}

			if (targetSlot == Player.playerWeapon) {
				c.autocasting = false;
				c.autocastId = 0;
				c.getPA().sendFrame36(108, 0);
			}

			if (slot >= 0 && wearID >= 0) {
				int toEquip = c.playerItems[slot];
				int toEquipN = c.playerItemsN[slot];
				int toRemove = c.playerEquipment[targetSlot];
				int toRemoveN = c.playerEquipmentN[targetSlot];
				/**
				 * Castle wars
				 **/

				if (toEquip == toRemove + 1 && Item.itemStackable[toRemove]) {
					deleteItem(toRemove, getItemSlot(toRemove), toEquipN);
					c.playerEquipmentN[targetSlot] += toEquipN;
				} else if (targetSlot != 5 && targetSlot != 3) {
					int remove_slot = getItemSlot(toRemove);
					if (toRemove != -1 && remove_slot != -1 && Item.itemStackable[toRemove]) {
						c.playerItems[slot] = 0;
						c.playerItemsN[slot] = 0;
						c.playerItemsN[remove_slot] += toRemoveN;
					} else {
						c.playerItems[slot] = toRemove + 1;
						c.playerItemsN[slot] = toRemoveN;
					}
					c.playerEquipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 5) {
					boolean wearing2h = is2handed(getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase(), c.playerEquipment[Player.playerWeapon]);
					if (wearing2h) {
						toRemove = c.playerEquipment[Player.playerWeapon];
						toRemoveN = c.playerEquipmentN[Player.playerWeapon];
						c.playerEquipment[Player.playerWeapon] = -1;
						c.playerEquipmentN[Player.playerWeapon] = 0;
						updateSlot(Player.playerWeapon);
					}
					c.playerItems[slot] = toRemove + 1;
					c.playerItemsN[slot] = toRemoveN;
					c.playerEquipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 3) {
					boolean is2h = is2handed(getItemName(wearID).toLowerCase(), wearID);
					boolean wearingShield = c.playerEquipment[Player.playerShield] > 0;
					boolean wearingWeapon = c.playerEquipment[Player.playerWeapon] > 0;
					if (is2h) {
						if (wearingShield && wearingWeapon) {
							if (freeSlots() > 0) {
								c.playerItems[slot] = toRemove + 1;
								c.playerItemsN[slot] = toRemoveN;
								c.playerEquipment[targetSlot] = toEquip - 1;
								c.playerEquipmentN[targetSlot] = toEquipN;
								removeItem(Player.playerShield);
							} else {
								c.sendMessage("You do not have enough inventory space to do this.");
								return false;
							}
						} else if (wearingShield && !wearingWeapon) {
							c.playerItems[slot] = c.playerEquipment[Player.playerShield] + 1;
							c.playerItemsN[slot] = c.playerEquipmentN[Player.playerShield];
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
							c.playerEquipment[Player.playerShield] = -1;
							c.playerEquipmentN[Player.playerShield] = 0;
							updateSlot(Player.playerShield);
						} else {
							int remove_slot = getItemSlot(toRemove);
							if (toRemove != -1 && remove_slot != -1 && Item.itemStackable[toRemove]) {
								c.playerItems[slot] = 0;
								c.playerItemsN[slot] = 0;
								c.playerItemsN[remove_slot] += toRemoveN;
							} else {
								c.playerItems[slot] = toRemove + 1;
								c.playerItemsN[slot] = toRemoveN;
							}
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
						}
					} else {
						int remove_slot = getItemSlot(toRemove);
						if (toRemove != -1 && remove_slot != -1 && Item.itemStackable[toRemove]) {
							c.playerItems[slot] = 0;
							c.playerItemsN[slot] = 0;
							c.playerItemsN[remove_slot] += toRemoveN;
						} else {
							c.playerItems[slot] = toRemove + 1;
							c.playerItemsN[slot] = toRemoveN;
						}
						c.playerEquipment[targetSlot] = toEquip - 1;
						c.playerEquipmentN[targetSlot] = toEquipN;
					}
				}
				resetItems(3214);
			}
			if (targetSlot == 3) {
				c.usingSpecial = false;
				addSpecialBar(wearID);
			}
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(targetSlot);
				c.getOutStream().writeWord(wearID + 1);

				if (c.playerEquipmentN[targetSlot] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.playerEquipmentN[targetSlot]);
				} else {
					c.getOutStream().writeByte(c.playerEquipmentN[targetSlot]);
				}

				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
			sendWeapon(c.playerEquipment[Player.playerWeapon], getItemName(c.playerEquipment[Player.playerWeapon]));
			resetBonus();
			getBonus();
			c.getPA().resetAutoCast();
			c.autocasting = false;
			writeBonus();
			c.getItems();
			c.getCombat().getPlayerAnimIndex(ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.isFullHelm = Item.isFullHelm(c.playerEquipment[Player.playerHat]);
			c.isFullMask = Item.isFullMask(c.playerEquipment[Player.playerHat]);
			c.isFullBody = Item.isFullBody(c.playerEquipment[Player.playerChest]);
			c.getPA().requestUpdates();
			return true;
		} else {
			return false;
		}
	}

	public void wearItem(int wearID, int wearAmount, int targetSlot) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(targetSlot);
			c.getOutStream().writeWord(wearID + 1);

			if (wearAmount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(wearAmount);
			} else {
				c.getOutStream().writeByte(wearAmount);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipment[targetSlot] = wearID;
			c.playerEquipmentN[targetSlot] = wearAmount;
			c.getItems();
			c.getItems().sendWeapon(c.playerEquipment[Player.playerWeapon], ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]));
			c.getItems().resetBonus();
			c.getItems().getBonus();
			c.getPA().resetAutoCast();
			c.getItems().writeBonus();
			c.getItems();
			c.getCombat().getPlayerAnimIndex(ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}

	public void updateSlot(int slot) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(slot);
			c.getOutStream().writeWord(c.playerEquipment[slot] + 1);
			if (c.playerEquipmentN[slot] > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(c.playerEquipmentN[slot]);
			} else {
				c.getOutStream().writeByte(c.playerEquipmentN[slot]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}

	}

	public boolean playerHasEquipped(int slot, int itemID) {
		return c.playerEquipment[slot] == itemID;
	}

	public boolean playerHasEquipped(int itemID) {
		itemID++;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] == itemID) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove Item
	 * 
	 * @param item
	 *            item's id
	 * @param slot
	 *            the slot
	 */

	public void removeItem2(int item, int slot) {
		if (c.getOutStream() != null && c != null) {
			if (c.playerEquipment[slot] > -1) {

				if (addItem(c.playerEquipment[slot], c.playerEquipmentN[slot])) {
					c.playerEquipment[slot] = -1;
					c.playerEquipmentN[slot] = 0;
					sendWeapon(c.playerEquipment[Player.playerWeapon], getItemName(c.playerEquipment[Player.playerWeapon]));
					resetBonus();
					getBonus();
					writeBonus();
					c.autocasting = false;
					c.getItems();
					c.getCombat().getPlayerAnimIndex(ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
					c.getOutStream().createFrame(34);
					c.getOutStream().writeWord(6);
					c.getOutStream().writeWord(1688);
					c.getOutStream().writeByte(slot);
					c.getOutStream().writeWord(0);
					c.getOutStream().writeByte(0);
					c.flushOutStream();
					// c.getPA().resetAutoCast();
					c.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
				}
			}
		}
	}

	public void removeItem(int slot) {
		if (c.getOutStream() != null && c != null) {
			if (c.playerEquipment[slot] > -1) {

				if (addItem(c.playerEquipment[slot], c.playerEquipmentN[slot])) {
					c.playerEquipment[slot] = -1;
					c.playerEquipmentN[slot] = 0;
					sendWeapon(c.playerEquipment[Player.playerWeapon], getItemName(c.playerEquipment[Player.playerWeapon]));
					resetBonus();
					getBonus();
					c.autocasting = false;
					writeBonus();
					c.getItems();
					c.getCombat().getPlayerAnimIndex(ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
					c.getOutStream().createFrame(34);
					c.getOutStream().writeWord(6);
					c.getOutStream().writeWord(1688);
					c.getOutStream().writeByte(slot);
					c.getOutStream().writeWord(0);
					// c.getPA().resetAutoCast();
					c.getOutStream().writeByte(0);
					c.flushOutStream();
					c.isFullHelm = Item.isFullHelm(c.playerEquipment[Player.playerHat]);
					c.isFullMask = Item.isFullMask(c.playerEquipment[Player.playerHat]);
					c.isFullBody = Item.isFullBody(c.playerEquipment[Player.playerChest]);
					c.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
				}
			}
		}
		// }
	}

	public void resetBank() {
		int tabId = c.getBank().getCurrentBankTab().getTabId();
		for (int i = 0; i < c.getBank().getBankTab().length; i++) {
			if (i == 0)
				continue;
			if (i != c.getBank().getBankTab().length - 1 && c.getBank().getBankTab()[i].size() == 0 && c.getBank().getBankTab()[i + 1].size() > 0) {
				for (BankItem item : c.getBank().getBankTab()[i + 1].getItems()) {
					c.getBank().getBankTab()[i].add(item);
				}
				c.getBank().getBankTab()[i + 1].getItems().clear();
			}
		}
		c.getPA().sendFrame36(600, 0);
		c.getPA().sendFrame34a(58040, -1, 0, 0);
		int newSlot = -1;
		for (int i = 0; i < c.getBank().getBankTab().length; i++) {
			BankTab tab = c.getBank().getBankTab()[i];
			if (i == tabId) {
				c.getPA().sendFrame36(600 + i, 1);
			} else {
				c.getPA().sendFrame36(600 + i, 0);
			}
			if (tab.getTabId() != 0 && tab.size() > 0 && tab.getItem(0) != null) {
				c.getPA().sendFrame171(0, 58050 + i);
				c.getPA().sendFrame34a(58040 + i, c.getBank().getBankTab()[i].getItem(0).getId() - 1, 0, c.getBank().getBankTab()[i].getItem(0).getAmount());
			} else if (i != 0) {
				if (newSlot == -1) {
					newSlot = i;
					c.getPA().sendFrame34a(58040 + i, -1, 0, 0);
					c.getPA().sendFrame171(0, 58050 + i);
					continue;
				}
				c.getPA().sendFrame34a(58040 + i, -1, 0, 0);
				c.getPA().sendFrame171(1, 58050 + i);
			}
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(5382); // bank
		c.getOutStream().writeWord(Constants.BANK_SIZE);
		BankTab tab = c.getBank().getCurrentBankTab();
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			if (i > tab.size() - 1) {
				c.getOutStream().writeByte(0);
				c.getOutStream().writeWordBigEndianA(0);
				continue;
			} else {
				BankItem item = tab.getItem(i);
				if (item == null)
					item = new BankItem(-1, 0);
				if (item.getAmount() > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(item.getAmount());
				} else {
					c.getOutStream().writeByte(item.getAmount());
				}
				if (item.getAmount() < 1)
					item.setAmount(0);
				if (item.getId() > Constants.ITEM_LIMIT || item.getId() < 0)
					item.setId(-1);
				c.getOutStream().writeWordBigEndianA(item.getId());
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		c.getPA().sendFrame126("" + c.getBank().getCurrentBankTab().size(), 58061);
		c.getPA().sendFrame126(Integer.toString(tabId), 5292);
	}

	public void resetTempItems() {
		int itemCount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] > -1) {
				itemCount = i;
			}
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(5064);
		c.getOutStream().writeWord(itemCount + 1);
		for (int i = 0; i < itemCount + 1; i++) {
			if (c.playerItemsN[i] > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
			} else {
				c.getOutStream().writeByte(c.playerItemsN[i]);
			}
			if (c.playerItems[i] > Constants.ITEM_LIMIT || c.playerItems[i] < 0) {
				c.playerItems[i] = Constants.ITEM_LIMIT;
			}
			c.getOutStream().writeWordBigEndianA(c.playerItems[i]);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public boolean addToBank(int itemID, int amount, boolean updateView) {
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return false;
		}
		if (c.playerStun) {
			return false;
		}
		if (!c.isBanking)
			return false;
		if (!c.getItems().playerHasItem(itemID))
			return false;
		if (c.getBank().getBankSearch().isSearching()) {
			c.getBank().getBankSearch().reset();
			return false;
		}
		if (c.getBankPin().requiresUnlock()) {
			resetBank();
			if (c.openInterface != 59500) {
				c.getBankPin().open(2);
			}
			return false;
		}
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemID + 1, amount);
		for (BankTab t : c.getBank().getBankTab()) {
			if (t == null || t.size() == 0)
				continue;
			for (BankItem i : t.getItems()) {
				if (i.getId() == item.getId() && !isNotable(itemID)) {
					if (t.getTabId() != tab.getTabId()) {
						tab = t;
						break;
					}
				} else {
					if (isNotable(itemID) && i.getId() == item.getId() - 1) {
						item = new BankItem(itemID, amount);
						if (t.getTabId() != tab.getTabId()) {
							tab = t;
							break;
						}
					}
				}
			}
		}
		if (isNotable(itemID)) {
			item = new BankItem(itemID, amount);
		}
		if (item.getAmount() > getItemAmount(itemID))
			item.setAmount(getItemAmount(itemID));
		if (tab.getItemAmount(item) == Integer.MAX_VALUE) {
			c.sendMessage("Your bank is already holding the maximum amount of " + getItemName(itemID).toLowerCase() + " possible.");
			return false;
		}
		if (tab.freeSlots() == 0 && !tab.contains(item)) {
			c.sendMessage("Your current bank tab is full.");
			return false;
		}
		long totalAmount = ((long) tab.getItemAmount(item) + (long) item.getAmount());
		if (totalAmount >= Integer.MAX_VALUE) {
			int difference = Integer.MAX_VALUE - tab.getItemAmount(item);
			item.setAmount(difference);
			deleteItem2(itemID, difference);
		} else {
			deleteItem2(itemID, item.getAmount());
		}
		tab.add(item);
		if (updateView) {
			resetTempItems();
			resetBank();
		}
		return true;
	}

	public boolean bankContains(int itemId) {

		for (BankTab tab : c.getBank().getBankTab())
			if (tab.contains(new BankItem(itemId + 1)))
				return true;
		return false;
	}

	public void removeFromBank(int itemId, int itemAmount, boolean updateView) {
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemId + 1, itemAmount);
		boolean noted = false;
		if (!c.isBanking)
			return;
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return;
		}
		if (itemAmount <= 0)
			return;
		if (c.getBankPin().requiresUnlock()) {
			resetBank();
			if (c.openInterface != 59500) {
				c.getBankPin().open(2);
			}
			return;
		}
		if (System.currentTimeMillis() - c.lastBankDeposit < 3000)
			return;
		if (c.inTrade || c.duelStatus != 0) {
			c.getPA().closeAllWindows();
			return;
		}
		if (!tab.contains(item))
			return;
		if (c.takeAsNote) {
			if (getItemName(itemId).trim().equalsIgnoreCase(getItemName(itemId + 1).trim()) && isNotable(itemId + 1)) {
				noted = true;
			} else
				c.sendMessage("This item cannot be taken out as noted.");
		}
		if (freeSlots() == 0 && !playerHasItem(itemId)) {
			c.sendMessage("There is not enough space in your inventory.");
			return;
		}
		if (getItemAmount(itemId) == Integer.MAX_VALUE) {
			c.sendMessage("Your inventory is already holding the maximum amount of " + getItemName(itemId).toLowerCase() + " possible.");
			return;
		}
		if (isStackable(item.getId() - 1) || noted) {
			long totalAmount = (long) getItemAmount(itemId) + (long) itemAmount;
			if (totalAmount > Integer.MAX_VALUE)
				item.setAmount(tab.getItemAmount(item) - getItemAmount(itemId));
		}
		if (tab.getItemAmount(item) < itemAmount) {
			item.setAmount(tab.getItemAmount(item));
		}
		if (!isStackable(item.getId() - 1) && !noted) {
			if (freeSlots() < item.getAmount())
				item.setAmount(freeSlots());
		}
		if (item.getAmount() < 0)
			item.setAmount(0);
		if (!noted)
			addItem(item.getId() - 1, item.getAmount());
		else
			addItem(item.getId(), item.getAmount());
		tab.remove(item);
		if (tab.size() == 0) {
			c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
		}
		if (updateView) {
			resetBank();
		}
		c.getItems().resetItems(5064);
	}

	public boolean addEquipmentToBank(int itemID, int slot, int amount, boolean updateView) {
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return false;
		}
		if (!c.isBanking)
			return false;
		if (c.playerEquipment[slot] != itemID || c.playerEquipmentN[slot] <= 0)
			return false;
		BankTab tab = c.getBank().getCurrentBankTab();
		BankItem item = new BankItem(itemID + 1, amount);
		Iterator<BankTab> iterator = Arrays.asList(c.getBank().getBankTab()).iterator();
		while (iterator.hasNext()) {
			BankTab t = iterator.next();
			if (t != null && t.size() > 0) {
				Iterator<BankItem> iterator2 = t.getItems().iterator();
				while (iterator2.hasNext()) {
					BankItem i = iterator2.next();
					if (i.getId() == item.getId() && !isNotable(itemID)) {
						if (t.getTabId() != tab.getTabId()) {
							tab = t;
							break;
						}
					} else {
						if (isNotable(itemID) && i.getId() == item.getId() - 1) {
							item = new BankItem(itemID, amount);
							if (t.getTabId() != tab.getTabId()) {
								tab = t;
								break;
							}
						}
					}
				}
			}
		}
		if (isNotable(itemID))
			item = new BankItem(itemID, amount);
		if (item.getAmount() > c.playerEquipmentN[slot])
			item.setAmount(c.playerEquipmentN[slot]);
		if (tab.getItemAmount(item) == Integer.MAX_VALUE) {
			c.sendMessage("Your bank is already holding the maximum amount of " + getItemName(itemID).toLowerCase() + " possible.");
			return false;
		}
		if (tab.freeSlots() == 0 && !tab.contains(item)) {
			c.sendMessage("Your current bank tab is full.");
			return false;
		}
		long totalAmount = ((long) tab.getItemAmount(item) + (long) item.getAmount());
		if (totalAmount >= Integer.MAX_VALUE) {
			c.sendMessage("Your bank is already holding the maximum amount of this item.");
			return false;
		} else
			c.playerEquipmentN[slot] -= item.getAmount();
		if (c.playerEquipmentN[slot] <= 0) {
			c.playerEquipmentN[slot] = -1;
			c.playerEquipment[slot] = -1;
		}
		tab.add(item);
		if (updateView) {
			resetTempItems();
			resetBank();
			updateSlot(slot);
		}
		return true;
	}

	public boolean isStackable(int itemID) {
		return Item.itemStackable[itemID];
	}

	/**
	 * Update Equip tab
	 * 
	 * @param wearID
	 *            the wear id
	 * @param amount
	 *            the amount
	 * @param targetSlot
	 *            the target slot
	 */

	public void setEquipment(int wearID, int amount, int targetSlot) {
		c.getOutStream().createFrameVarSizeWord(34);
		c.getOutStream().writeWord(1688);
		c.getOutStream().writeByte(targetSlot);
		c.getOutStream().writeWord(wearID + 1);
		if (amount > 254) {
			c.getOutStream().writeByte(255);
			c.getOutStream().writeDWord(amount);
		} else {
			c.getOutStream().writeByte(amount);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		c.playerEquipment[targetSlot] = wearID;
		c.playerEquipmentN[targetSlot] = amount;
		c.isFullHelm = Item.isFullHelm(c.playerEquipment[Player.playerHat]);
		c.isFullMask = Item.isFullMask(c.playerEquipment[Player.playerHat]);
		c.isFullBody = Item.isFullBody(c.playerEquipment[Player.playerChest]);
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	/**
	 * Move Items
	 */

	public void swapBankItem(int from, int to) {
		BankItem item = c.getBank().getCurrentBankTab().getItem(from);
		c.getBank().getCurrentBankTab().setItem(from, c.getBank().getCurrentBankTab().getItem(to));
		c.getBank().getCurrentBankTab().setItem(to, item);
	}

	public void moveItems(int from, int to, int moveWindow, boolean insertMode) {
		if (moveWindow == 3214) {
			int tempI;
			int tempN;
			tempI = c.playerItems[from];
			tempN = c.playerItemsN[from];
			c.playerItems[from] = c.playerItems[to];
			c.playerItemsN[from] = c.playerItemsN[to];
			c.playerItems[to] = tempI;
			c.playerItemsN[to] = tempN;
		}
		if (moveWindow == 5382) {
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				resetBank();
				return;
			}
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				resetBank();
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			if (to > 999) {
				int tabId = to - 1000;
				if (tabId < 0)
					tabId = 0;
				if (tabId == c.getBank().getCurrentBankTab().getTabId()) {
					c.sendMessage("You cannot add an item from it's tab to the same tab.");
					resetBank();
					return;
				}
				if (from >= c.getBank().getCurrentBankTab().size()) {
					resetBank();
					return;
				}
				BankItem item = c.getBank().getCurrentBankTab().getItem(from);
				if (item == null) {
					resetBank();
					return;
				}
				if (c.getBank().getBankTab()[tabId].size() >= Constants.BANK_SIZE) {
					c.sendMessage("You cannot move anymore items to that tab.");
					resetBank();
					return;
				}
				c.getBank().getCurrentBankTab().remove(item);
				c.getBank().getBankTab()[tabId].add(item);
			} else {
				if (from > c.getBank().getCurrentBankTab().size() - 1 || to > c.getBank().getCurrentBankTab().size() - 1) {
					resetBank();
					return;
				}
				if (!insertMode) {
					BankItem item = c.getBank().getCurrentBankTab().getItem(from);
					c.getBank().getCurrentBankTab().setItem(from, c.getBank().getCurrentBankTab().getItem(to));
					c.getBank().getCurrentBankTab().setItem(to, item);
				} else {
					int tempFrom = from;
					for (int tempTo = to; tempFrom != tempTo;)
						if (tempFrom > tempTo) {
							swapBankItem(tempFrom, tempFrom - 1);
							tempFrom--;
						} else if (tempFrom < tempTo) {
							swapBankItem(tempFrom, tempFrom + 1);
							tempFrom++;
						}
				}
			}
		}
		if (moveWindow == 5382) {
			resetBank();
		}
		if (moveWindow == 5064) {
			int tempI;
			int tempN;
			tempI = c.playerItems[from];
			tempN = c.playerItemsN[from];

			c.playerItems[from] = c.playerItems[to];
			c.playerItemsN[from] = c.playerItemsN[to];
			c.playerItems[to] = tempI;
			c.playerItemsN[to] = tempN;
			resetItems(3214);
		}
		resetTempItems();
		if (moveWindow == 3214) {
			resetItems(3214);
		}

	}

	/**
	 * delete Item
	 */

	public void deleteEquipment(int i, int j) {
		if (World.PLAYERS.get(c.getIndex()) == null) {
			return;
		}
		if (i < 0) {
			return;
		}

		c.playerEquipment[j] = -1;
		c.playerEquipmentN[j] = c.playerEquipmentN[j] - 1;
		c.getOutStream().createFrame(34);
		c.getOutStream().writeWord(6);
		c.getOutStream().writeWord(1688);
		c.getOutStream().writeByte(j);
		c.getOutStream().writeWord(0);
		c.getOutStream().writeByte(0);
		getBonus();
		if (j == Player.playerWeapon) {
			sendWeapon(-1, "Unarmed");
		}
		resetBonus();
		getBonus();
		writeBonus();
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}
	/*
	 * public void deleteItem(int id, int amount) {
		if (id <= 0)
			return;
		
		
		for (int j = 0; j < c.playerItems.length; j++) {
			if (amount <= 0)
				break;
			if (c.playerItems[j] == id + 1) {
				c.playerItems[j] = 0;
				c.playerItemsN[j] = 0;
				amount--;
			}
		}
		resetItems(3214);
	}
*/
	public void deleteItem(final int id, int amount) {
		if (id <= 0) {
			return;
		}
		for (int j = 0; j < c.playerItems.length; j++) {
			if (amount <= 0) {
				break;
			}
			if (c.playerItems[j] == id + 1) {
				if (c.playerItemsN[j] > amount) {
					c.playerItemsN[j] -= amount;
					if (c.playerItemsN[j] < 0) {
						c.playerItemsN[j] = 0;
						c.playerItems[j] = 0;
					}
					amount = 0;
				} else {
					c.playerItems[j] = 0;
					c.playerItemsN[j] = 0;
					amount--;
				}
			}
		}
		resetItems(3214);
	}

	public void deleteItem(int id) {
		deleteItem(id, 1);
	}

	public void deleteItem(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (c.playerItems[slot] == (id + 1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItemsN[slot] = 0;
				c.playerItems[slot] = 0;
			}
			resetItems(3214);
		}
	}

	public void deleteItem2(int id, int amount) {
		int am = amount;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (am == 0) {
				break;
			}
			if (c.playerItems[i] == (id + 1)) {
				if (c.playerItemsN[i] > amount) {
					c.playerItemsN[i] -= amount;
					break;
				} else {
					c.playerItems[i] = 0;
					c.playerItemsN[i] = 0;
					am--;
				}
			}
		}
		resetItems(3214);
	}

	/**
	 * Delete Arrows
	 */
	public void deleteArrow() {
		if (c.playerEquipment[Player.playerCape] == 10499 && Misc.random(5) != 1)
			return;
		if (c.playerEquipmentN[Player.playerArrows] == 1) {
			c.getItems().deleteEquipment(c.playerEquipment[Player.playerArrows], Player.playerArrows);
		}

		if (c.playerEquipmentN[Player.playerArrows] != 0) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(Player.playerArrows);
			c.getOutStream().writeWord(c.playerEquipment[Player.playerArrows] + 1);
			if (c.playerEquipmentN[Player.playerArrows] - 1 > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(c.playerEquipmentN[Player.playerArrows] - 1);
			} else {
				c.getOutStream().writeByte(c.playerEquipmentN[Player.playerArrows] - 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipmentN[Player.playerArrows] -= 1;
		}
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void deleteEquipment() {
		if (c.usingOtherRangeWeapons && c.playerEquipment[Player.playerCape] == 10499 && Misc.random(5) != 1) {
			return;
		}
		if (c.playerEquipmentN[Player.playerWeapon] == 1) {
			c.getItems().deleteEquipment(c.playerEquipment[Player.playerWeapon], Player.playerWeapon);
		}
		if (c.playerEquipmentN[Player.playerWeapon] != 0) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(Player.playerWeapon);
			c.getOutStream().writeWord(c.playerEquipment[Player.playerWeapon] + 1);
			if (c.playerEquipmentN[Player.playerWeapon] - 1 > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(c.playerEquipmentN[Player.playerWeapon] - 1);
			} else {
				c.getOutStream().writeByte(c.playerEquipmentN[Player.playerWeapon] - 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipmentN[Player.playerWeapon] -= 1;
		}
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}


	/**
	 * Dropping Arrows
	 */

	public void dropArrowNpc() {
		if (c.playerEquipment[Player.playerCape] == 10499)
			return;
		int enemyX = 0;
		int enemyY = 0;
		int enemyHeight = 0;
		if (c != null && c.outStream != null) {
			if (NPCHandler.NPCS.get(c.oldNpcIndex) != null) {
				enemyX = NPCHandler.NPCS.get(c.oldNpcIndex).getX();
				enemyY = NPCHandler.NPCS.get(c.oldNpcIndex).getY();
				enemyHeight = NPCHandler.NPCS.get(c.oldNpcIndex).heightLevel;
			}
		}
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, enemyHeight, 1);
			} else if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX, enemyY, enemyHeight, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, enemyHeight, amount + 1);
			}
		}
	}

	public void dropArrow() {
		int enemyX = 0, enemyY = 0, enemyHeight = 0;
		if (c.playerEquipment[Player.playerCape] == 10499)
			return;
		if (c.oldNpcIndex > 0) {
			enemyX = NPCHandler.NPCS.get(c.oldNpcIndex).getX();
			enemyY = NPCHandler.NPCS.get(c.oldNpcIndex).getY();
			enemyHeight = NPCHandler.NPCS.get(c.oldNpcIndex).heightLevel;
		} else if (c.oldPlayerIndex > 0) {
			enemyX = World.PLAYERS.get(c.oldPlayerIndex).getX();
			enemyY = World.PLAYERS.get(c.oldPlayerIndex).getHeight();
		}
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, enemyHeight, 1);
			} else if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX, enemyY, enemyHeight, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, enemyHeight, amount + 1);
			}
		}
	}

	public void dropArrowPlayer() {
		if (c.playerEquipment[Player.playerWeapon] == 10033 || c.playerEquipment[Player.playerWeapon] == 10034) {
			return;
		}
		int enemyX = World.PLAYERS.get(c.oldPlayerIndex).getX();
		int enemyY = World.PLAYERS.get(c.oldPlayerIndex).getY();
		int enemyHeight = World.PLAYERS.get(c.oldPlayerIndex).getHeight();
		if (c.playerEquipment[Player.playerCape] == 10499)
			return;
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, enemyHeight, 1);
			} else if (Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(c.playerName, c.rangeItemUsed, enemyX, enemyY);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX, enemyY, enemyHeight, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, enemyHeight, amount + 1);
			}
		}
	}

	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public boolean spaceFor(Item item) {
		boolean stackable = Item.itemStackable[item.getId()];
		if (stackable) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] == item.getId()) {
					int totalCount = item.getCount() + c.playerItemsN[i];
					if (totalCount >= Integer.MAX_VALUE || totalCount < 1) {
						return false;
					}
					return true;
				}
			}
			int slot = freeSlot();
			return slot != -1;
		}

		int slots = freeSlots();
		return slots >= item.getCount();
	}

	public int freeBankSlots() {
		int freeS = 0;
		for (int i = 0; i < c.bankItems.length; i++) {
			if (c.bankItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public int freeSlot() {
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] <= 0) {
				return i;
			}
		}
		return -1;
	}

	public static String getItemName(int ItemID) {
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					return Server.itemHandler.ItemList[i].itemName;
				}
			}
		}
		return "Unarmed";
	}

	public static Optional<ItemList> getItemDef(int ItemID) {
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					return Optional.of(Server.itemHandler.ItemList[i]);
				}
			}
		}
		return Optional.empty();
	}

	public int getItemId(String itemName) {
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemName.equalsIgnoreCase(itemName)) {
					return Server.itemHandler.ItemList[i].itemId;
				}
			}
		}
		return -1;
	}

	public int getItemSlot(int ItemID) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == ItemID) {
				return i;
			}
		}
		return -1;
	}

	public int getItemCount(int itemID) {
		int count = 0;
		for (int j = 0; j < c.playerItems.length; j++) {
			if (c.playerItems[j] == itemID + 1) {
				count += c.playerItemsN[j];
			}
		}
		return count;
	}

	public int getItemAmount(int ItemID) {
		int itemCount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == ItemID) {
				itemCount += c.playerItemsN[i];
			}
		}
		return itemCount;
	}

	public boolean playerHasItem(int itemID, int amt, int slot) {
		itemID++;
		int found = 0;
		if (c.playerItems[slot] == (itemID)) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] == itemID) {
					if (c.playerItemsN[i] >= amt) {
						return true;
					} else {
						found++;
					}
				}
			}
			return found >= amt;
		}
		return false;
	}

	public boolean playerHasItem(int itemID) {
		itemID++;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID)
				return true;
		}
		return false;
	}

	public boolean playerHasItem(int itemID, int amt) {
		itemID++;
		int found = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID) {
				if (c.playerItemsN[i] >= amt) {
					return true;
				} else {
					found++;
				}
			}
		}
		return found >= amt;
	}

	public int getUnnotedItem(int ItemID) {
		int NewID = ItemID - 1;
		String NotedName = "";
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					NotedName = Server.itemHandler.ItemList[i].itemName;
				}
			}
		}
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemName == null ? NotedName == null : Server.itemHandler.ItemList[i].itemName.equals(NotedName)) {
					if (!Server.itemHandler.ItemList[i].itemDescription.startsWith("Swap this note at any bank for a")) {
						NewID = Server.itemHandler.ItemList[i].itemId;
						break;
					}
				}
			}
		}
		return NewID;
	}

	/**
	 * Dropping items
	 **/
	public void createGroundItem(int itemID, int itemX, int itemY, int height, int itemAmount) {
		if (c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
			c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
			c.getOutStream().createFrame(44);
			c.getOutStream().writeWordBigEndianA(itemID);
			c.getOutStream().writeWord(itemAmount);
			c.getOutStream().writeByte(0);
			c.flushOutStream();
		}
	}

	/**
	 * Pickup Item
	 */

	public void removeGroundItem(int itemID, int itemX, int itemY, int height) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
			c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
			c.getOutStream().createFrame(156);
			c.getOutStream().writeByteS(0);
			c.getOutStream().writeWord(itemID);
			c.flushOutStream();
		}
	}

	/**
	 * Checks if a player owns a cape.
	 * 
	 * @return
	 */
	public void makeGodsword(int i) {
		if (playerHasItem(11690) && playerHasItem(i)) {
			deleteItem(11690, 1);
			deleteItem(i, 1);
			addItem(i - 8, 1);
			c.sendMessage("You combine the hilt and the blade to make a godsword.");
		}
	}

	/**
	 * Sends an item to the bank in any tab possible.
	 * 
	 * @param itemId
	 *            the item id
	 * @param amount
	 *            the item amount
	 */
	public void sendItemToAnyTab(int itemId, int amount) {
		BankItem item = new BankItem(itemId, amount);
		for (BankTab tab : c.getBank().getBankTab()) {
			if (tab.freeSlots() > 0 || tab.contains(item)) {
				c.getBank().setCurrentBankTab(tab);
				addItemToBank(itemId, amount);
				return;
			}
		}
		addItemToBank(itemId, amount);
	}

	public boolean playerOwnsItem(int id) {
		return playerHasItem(id) || bankContains(id);
	}

	public boolean playerOwnsAnyItems(int... ids) {
		return Arrays.stream(ids).anyMatch(this::playerOwnsItem);
	}

	public boolean isHilt(int i) {
		return i >= 11702 && i <= 11708 && i % 2 == 0;
	}

	public int search(int... ids) {
		for (int id : ids)
			if (playerHasItem(id))
				return id;
		return -1;
	}

	public void sendItemsOnInterface(int widget, Item[] container) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(widget);
			c.getOutStream().writeWord(container.length);
			for (Item item : container) {
				if (item.getCount() > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(item.getCount());
				} else {
					c.getOutStream().writeByte(item.getCount());
				}
				c.getOutStream().writeWordBigEndianA(item.getId() + 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void bankItems(int itemID, int invSlot, int i) {
		// TODO Auto-generated method stub
		
	}

}