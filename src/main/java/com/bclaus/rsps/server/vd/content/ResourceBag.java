package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.items.ItemAssistant;

public class ResourceBag {

	public static void removetoBank(Player player, int itemId, int amount) {
		if (player.getPA().viewingOtherBank) {
			player.getPA().resetOtherBank();
			return;
		}
		if (player.isBanking)
			return;
		if (player.inTrade || player.inDuelArena()) {
			player.getPA().closeAllWindows();
			return;
		}
		if (!contains(player, itemId))
			return;
		if (player.getItems().freeSlots() == 0 && !player.getItems().playerHasItem(itemId)) {
			player.sendMessage("There is not enough space in your inventory.");
			return;
		}
		if (player.getItems().getItemAmount(itemId) == Integer.MAX_VALUE) {
			player.getItems();
			player.sendMessage("Your inventory is already holding the maximum amount of " + ItemAssistant.getItemName(itemId).toLowerCase() + " possible.");
			return;
		}
		if (player.getItems().isStackable(itemId)) {
			long totalAmount = (long) player.getItems().getItemAmount(itemId) + (long) amount;
			if (totalAmount > Integer.MAX_VALUE)
				amount = (amount - player.getItems().getItemAmount(itemId));
		}
		if (amount > getItemAmount(player, itemId)) {
			amount = getItemAmount(player, itemId);
		}
		if (!player.getItems().isStackable(itemId)) {
			if (player.getItems().freeSlots() < amount)
				amount = player.getItems().freeSlots();
		}
		if (amount < 0) {
			player.sendMessage("There is not enough space in your inventory.");
			return;
		}
		deleteItem(player, itemId, amount);
		player.getItems().addItemToBank(itemId, amount);
		refreshInterface(player);
	}

	public static void remove(Player player, int itemId, int amount) {

		if (player.getPA().viewingOtherBank) {
			player.getPA().resetOtherBank();
			return;
		}
		if (player.isBanking)
			return;
		if (System.currentTimeMillis() - player.lastBankDeposit < 3000)
			return;
		if (player.inTrade || player.inDuelArena()) {
			player.getPA().closeAllWindows();
			return;
		}
		if (!contains(player, itemId))
			return;
		if (player.getItems().freeSlots() == 0 && !player.getItems().playerHasItem(itemId)) {
			player.sendMessage("There is not enough space in your inventory.");
			return;
		}
		if (player.getItems().getItemAmount(itemId) == Integer.MAX_VALUE) {
			player.getItems();
			player.sendMessage("Your inventory is already holding the maximum amount of " + ItemAssistant.getItemName(itemId).toLowerCase() + " possible.");
			return;
		}
		if (player.getItems().isStackable(itemId)) {
			long totalAmount = (long) player.getItems().getItemAmount(itemId) + (long) amount;
			if (totalAmount > Integer.MAX_VALUE)
				amount = (amount - player.getItems().getItemAmount(itemId));
		}
		if (amount > getItemAmount(player, itemId)) {
			amount = getItemAmount(player, itemId);
		}
		if (!player.getItems().isStackable(itemId)) {
			if (player.getItems().freeSlots() < amount)
				amount = player.getItems().freeSlots();
		}
		if (amount < 0) {
			player.sendMessage("There is not enough space in your inventory.");
			return;
		}
		if (player.getItems().isStackable(itemId)) {
			amount = player.getItems().getItemAmount(itemId);
		}
		player.getItems().addItem(itemId, amount);
		deleteItem(player, itemId, amount);
		refreshInterface(player);
	}

	public static void add(Player player, int itemId, int amount) {
		if (player.getPA().viewingOtherBank) {
			player.getPA().resetOtherBank();
			return;
		}
		if (player.getItems().isStackable(itemId) && getItemAmount(player, itemId) >= 100) {
			player.sendMessage("You cannot place any more of this type of item in here.");
			return;
		}
		if (player.isBanking)
			return;
		if (player.inFightCaves()) {
			player.sendMessage("You cannot add items to the pouch from within the fight caves minigame.");
			return;
		}
		if (!player.getItems().playerHasItem(itemId))
			return;
	//if (!isAllowed(itemId)) {
			//player.sendMessage("You cannot add this item to the resource pouch.");
			//return;
		//}
		if (!player.getItems().isStackable(itemId) && freeSlots(player) < amount)
			amount = freeSlots(player);
		if (amount > player.getItems().getItemAmount(itemId))
			amount = player.getItems().getItemAmount(itemId);
		if (getItemAmount(player, itemId) == Integer.MAX_VALUE) {
			player.getItems();
			player.sendMessage("Your pouch is already holding the maximum amount of " + ItemAssistant.getItemName(itemId).toLowerCase() + " possible.");
			return;
		}
		if (freeSlots(player) <= 0) {
			if (!player.getItems().isStackable(itemId) || player.getItems().isStackable(itemId) && !contains(player, itemId)) {
				player.sendMessage("Your pouch is full, you need to remove items from it.");
				return;
			}
		}
		long totalAmount = ((long) getItemAmount(player, itemId) + (long) amount);
		if (totalAmount >= Integer.MAX_VALUE) {
			int difference = Integer.MAX_VALUE - getItemAmount(player, itemId);
			amount = difference;
			player.getItems().deleteItem2(itemId, difference);
		} else {
			player.getItems().deleteItem2(itemId, amount);
		}
		while (amount > 0) {
			addItem(player, itemId, 1);
			amount--;
		}
		if (!player.usedItemOnBag)
			refreshInterface(player);
		player.usedItemOnBag = false;
	}

	public static void refreshInterface(Player player) {
		for (int i = 0; i < player.resourceItemId.length; i++)
			if (player.resourceItemId[i] <= 0 && player.resourceItemAmount[i] <= 0)
				player.getPA().sendFrame34a(44003 + i, -1, 0, 0);
			else
				player.getPA().sendFrame34a(44003 + i, player.resourceItemId[i], 0, player.resourceItemAmount[i]);
		player.getItems().resetItems(44054);
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(248);
			player.getOutStream().writeWordA(44000);
			player.getOutStream().writeWord(44053);
			player.flushOutStream();
		}
	}

	public static int freeSlots(Player player) {
		int toReturn = 0;
		for (int i = 0; i < player.resourceItemId.length; i++)
			if (player.resourceItemId[i] <= 0 && player.resourceItemAmount[i] <= 0)
				toReturn++;
		return toReturn;
	}

	public static boolean contains(Player player, int itemId, int itemAmount) {
		for (int i = 0; i < player.resourceItemId.length; i++)
			if (player.resourceItemId[i] == itemId && player.resourceItemAmount[i] == itemAmount)
				return true;
		return false;
	}

	public static boolean contains(Player player, int itemId) {
		for (int i = 0; i < player.resourceItemId.length; i++)
			if (player.resourceItemId[i] == itemId)
				return true;
		return false;
	}

	public static int getItemAmount(Player player, int itemId) {
		int amount = 0;
		for (int i = 0; i < player.resourceItemId.length; i++)
			if (player.resourceItemId[i] == itemId)
				amount += player.resourceItemAmount[i];
		return amount;
	}

	public static void deleteItem(Player player, int itemId, int amount) {
		if (player.getItems().isStackable(itemId)) {
			for (int i = 0; i < player.resourceItemId.length; i++) {
				if (player.resourceItemId[i] == itemId) {
					player.resourceItemAmount[i] -= amount;
					if (player.resourceItemAmount[i] <= 0) {
						player.resourceItemAmount[i] = 0;
						player.resourceItemId[i] = -1;
					}
					break;
				}
			}
		} else {
			while (amount > 0) {
				for (int i = 0; i < player.resourceItemId.length; i++) {
					if (player.resourceItemId[i] == itemId) {
						player.resourceItemAmount[i] -= amount;
						if (player.resourceItemAmount[i] <= 0) {
							player.resourceItemAmount[i] = 0;
							player.resourceItemId[i] = -1;
						}
						amount--;
						break;
					}
				}
			}
		}
	}

	public static void addItem(Player player, int itemId, int amount) {
		boolean found = false;
		int openSlot = -1;
		for (int i = 0; i < player.resourceItemId.length; i++) {
			if (player.resourceItemId[i] <= 0 && openSlot == -1)
				openSlot = i;
			if (player.resourceItemId[i] == itemId && player.getItems().isStackable(itemId)) {
				player.resourceItemAmount[i] += amount;
				if (player.resourceItemAmount[i] <= 0) {
					player.resourceItemAmount[i] = 0;
					player.resourceItemId[i] = -1;
					found = true;
				}
				break;
			}
		}
		if (!found && openSlot > -1) {
			player.resourceItemId[openSlot] = itemId;
			player.resourceItemAmount[openSlot] = amount;
		}
	}

	//public static final int[] ALLOWED = { 1436, 7936, };

	/*public static boolean isAllowed(int item) {
		if (item > 552 && item < 568 || item == 9075)
			return true;
		if (item > 1600 && item < 1634)
			return true;
		String name = ItemAssistant.getItemName(item).toLowerCase();
		if (name.contains("essence"))
			return true;
		if (name.contains("clean") || name.contains("grimy"))
			return true;
		if (name.contains("logs"))
			return true;
		if (name.contains("seed"))
			return true;
		if (name.contains("bone"))
			return true;
		if (name.contains("bar")) {
			if (name.contains("bronze") || name.contains("iron") || name.contains("steel") || name.contains("silver") || name.contains("gold") || name.contains("mithril") || name.contains("adamant") || name.contains("rune"))
				return true;
		}
		if (name.endsWith("gem"))
			if (name.equalsIgnoreCase("dragonhide"))
				return true;
		if (name.equalsIgnoreCase("coal"))
			return true;
		if (name.contains("ore")) {
			if (name.contains("iron") || name.contains("tin") || name.contains("copper") || name.contains("mithril") || name.contains("adamantite") || name.contains("runite") || name.contains("silver"))
				return true;
		}
		if (name.contains("shrimp") || name.contains("anchovies") || name.contains("trout") || name.contains("bass") || name.contains("lobster") || name.contains("swordfish") || name.contains("shark") || name.contains("rocktail") || name.contains("manta") || name.contains("monkfish") || name.contains("sardine") || name.contains("herring") || name.contains("salmon") || name.contains("tuna"))
			return true;
		if (name.contains("pouch"))
			if (name.contains("herb") || name.contains("food") || name.contains("ore"))
				return true;
		return false;
	}*/

}
