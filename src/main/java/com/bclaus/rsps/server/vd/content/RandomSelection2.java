package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

/**
 * 
 * @Author Tim http://rune-server.org/members/Someone
 * 
 */
public class RandomSelection2 {

	/**
	 * Index 0 = Item Id Index 1 = Amount Index 2 = Rarity
	 */
	public static final int MAXIMUM_RARITY = 100;
	public static final int MINIMUM_RARITY = 1;
	public static final int MAXIMUM_ITEMS = 77;
	public static final int CYCLE_TIME = 4;
	public static final int EVENT_ID = 245193413;
	public static final int[][] ITEM_DATA = { { 995, 250000, 1 }, { 995, 400000, 1 }, { 995, 500000, 1 }, { 995, 750000, 5 }, { 74, 1, 1 }, { 75, 1, 1 }, { 4740, 250, 25 }, { 4740, 125, 15 }, { 4740, 50, 10 }, { 4740, 75, 12 }, { 1514, 25, 15 }, { 1514, 50, 20 }, { 1514, 75, 22 }, { 1514, 100, 24 }, { 1713, 15, 25 }, { 2439, 5, 35 }, { 2579, 1, 50 }, { 3122, 1, 1 }, { 537, 25, 30 }, { 4585, 1, 65 }, { 537, 50, 50 }, { 6215, 1, 15 }, { 6249, 1, 15 }, { 6322, 1, 1 }, { 6324, 1, 1 }, { 6326, 1, 1 }, { 6328, 1, 1 }, { 6330, 1, 1 }, { 6339, 1, 1 }, { 6341, 1, 30 }, { 6345, 1, 25 }, { 6349, 1, 15 }, { 6351, 1, 30 }, { 6353, 1, 25 }, { 6355, 1, 15 }, { 6357, 1, 15 }, { 6361, 1, 25 }, { 6363, 1, 25 }, { 6365, 1, 25 }, { 6371, 1, 25 }, { 6373, 1, 25 }, { 6375, 1, 10 }, { 6377, 1, 10 }, { 6402, 1, 10 }, { 6404, 1, 10 }, { 6547, 1, 10 }, { 6548, 1, 10 }, { 6912, 1, 35 }, { 7332, 1, 50 }, { 7335, 1, 75 }, { 7337, 1, 80 }, { 7340, 1, 65 }, { 7342, 1, 50 }, { 7348, 1, 90 }, { 7362, 1, 35 }, { 7364, 1, 35 }, { 7370, 1, 45 }, { 7372, 1, 25 }, { 7374, 1, 30 }, { 7376, 1, 35 }, { 7378, 1, 35 }, { 7378, 1, 20 }, { 7380, 1, 20 }, { 7382, 1, 40 }, { 7384, 1, 35 }, { 1949, 1, 50 }, { 1757, 1, 50 }, { 1005, 1, 50 }, { 8839, 1, 50 }, { 8840, 1, 50 }, { 8841, 1, 50 }, { 8842, 1, 50 }, { 11663, 1, 75 }, { 11664, 1, 75 }, { 11665, 1, 75 }, { 4151, 1, 65 }, { 4088, 1, 60 },

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
		c.getItems().deleteItem2(6199, 1);
		execute(c, itemId, amount);
	}

	public static void execute(final Player player, final int itemId, final int amount) {
		Server.getTaskScheduler().schedule(new ScheduledTask(3) {
			@Override
			public void execute() {
				if (player.disconnected) {
					this.stop();
					return;
				}
				if (player.getItems().freeSlots() != 0) {
					player.getItems().addItemToBank(itemId, amount);
				} else {
					Server.itemHandler.createGroundItem(player, itemId, player.absX, player.absY, player.heightLevel, amount);
				}
				player.sendMessage("You have received " + player.getItems().getItemName(itemId).replaceAll("_", " ") + "x" + amount + " from the casket.");
				super.stop();
			}

		}.attach(player));
	}

	public static int getRarityFormula() {
		int rarity = MINIMUM_RARITY + Misc.random(RandomSelection2.MAXIMUM_RARITY - 1);
		for (int i = 0; i < 3; i++) {
			if (rarity >= MAXIMUM_RARITY - 1) {
				rarity = MINIMUM_RARITY + Misc.random(RandomSelection2.MAXIMUM_RARITY - 1);
			}
		}
		return rarity;
	}

}
