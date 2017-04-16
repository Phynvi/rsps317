/**
 * 
 */
package com.bclaus.rsps.server.vd.content.skills.impl;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

/**
 * @author Tim http://rune-server.org/members/Someone
 *
 */
public class MysteryBox {

	/**
	 * Index 0 = Item Id Index 1 = Amount Index 2 = Rarity
	 */
	public static final int MAXIMUM_RARITY = 100;
	public static final int MINIMUM_RARITY = 1;
	public static final int MAXIMUM_ITEMS = 40;
	public static final int CYCLE_TIME = 4;
	public static final int EVENT_ID = 245193413;
	public static final int[][] ITEM_DATA = {

	{ 995, 5000, 1 }, { 995, 15000, 1 }, { 995, 25000, 1 }, { 8839, 1, 1 }, { 8840, 1, 1 }, { 8841, 1, 1 }, { 11665, 1, 1 }, { 11664, 1, 1 }, { 11663, 1, 1 }, { 8842, 1, 1 }, { 995, 50000, 1 }, { 995, 75000, 1 }, { 995, 100000, 1 }, { 3025, 5, 1 }, { 2435, 3, 1 }, { 2441, 2, 1 }, { 392, 10, 1 }, { 392, 50, 1 }, { 392, 100, 1 }, { 392, 50, 1 }, { 392, 5, 1 }, { 378, 10, 1 }, { 537, 2, 1 }, { 995, 5000, 1 }, { 995, 10000, 1 }, { 868, 10, 1 }, { 868, 50, 1 }, { 868, 100, 1 }, { 1127, 1, 1 }, { 1128, 1, 1 }, { 1129, 1, 1 }, { 1079, 1, 1 }, { 1080, 1, 1 }, { 1163, 1, 1},  { 15496, 1, 1}

	};

	public static void addItem(Player c) {
		int rarity = getRarityFormula();
		int[][] items = new int[MAXIMUM_ITEMS][2];
		int itemsIndex = 0;
		for (int i = 0; i < ITEM_DATA.length; i++) {
			if (ITEM_DATA[i][2] <= rarity) {
				items[itemsIndex][0] = ITEM_DATA[i][0];
				items[itemsIndex][1] = ITEM_DATA[i][1];
				itemsIndex++;
			}
		}
		int index = Misc.random(itemsIndex - 1);
		int itemId = items[index][0];
		int amount = items[index][1];
		c.getItems().deleteItem2(2717, 1);
		execute(c, itemId, amount);
	}

	public static void execute(final Player player, final int itemId, final int amount) {
		Server.getTaskScheduler().schedule(new ScheduledTask(3) {
			@Override
			public void execute() {
				if (player.getItems().freeSlots() != 0) {
					player.getItems().addItem(itemId, amount);
				} else {
					Server.itemHandler.createGroundItem(player, itemId, player.absX, player.absY, player.heightLevel, amount);
				}
				player.sendMessage("You have received " + player.getItems().getItemName(itemId).replaceAll("_", " ") + "x" + amount + " from the casket.");
				stop();
			}

		}.attach(player));
	}

	public static int getRarityFormula() {
		int rarity = MINIMUM_RARITY + Misc.random(MysteryBox.MAXIMUM_RARITY - 1);
		for (int i = 0; i < 3; i++) {
			if (rarity >= MAXIMUM_RARITY - 1) {
				rarity = MINIMUM_RARITY + Misc.random(MysteryBox.MAXIMUM_RARITY - 1);
			}
		}
		return rarity;
	}

}
