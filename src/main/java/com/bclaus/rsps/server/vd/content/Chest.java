/**
 * 
 */
package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author 
 *
 */
public class Chest {

	private static final int[] CHEST_REWARDS = { 451, 1079, 1093, 536, 11900, 371, 2572, 451, 1149, 1079, 2503, 2497, 1753, 1615, 1645, 1683 };
	private static final int[] RARE_REWARDS = { 2680, 11732, 10354, 10364, 10366, 11732, 4151, 10714, 15220, 15221, 15223, 15224, 6571 };
	public static final int[] KEY_HALVES = { 985, 987 };
	public static final int KEY = 989;
	private static final int DRAGONSTONE = 1631;
	private static final int OPEN_ANIMATION = 881;

	public static void makeKey(Player c) {
		if (c.getItems().playerHasItem(toothHalf(), 1) && c.getItems().playerHasItem(loopHalf(), 1)) {
			c.getItems().deleteItem(toothHalf(), 1);
			c.getItems().deleteItem(loopHalf(), 1);
			c.getItems().addItem(KEY, 1);
		}
	}

	public static boolean canOpen(Player c) {
		if (c.getItems().playerHasItem(KEY)) {
			return true;
		} else {
			c.sendMessage("The chest is locked.");
			return false;
		}
	}

	public static void searchChest(final Player c, final int id, final int x, final int y) {
		if (canOpen(c)) {
			c.sendMessage("You unlock the chest with your key.");
			c.getItems().deleteItem(KEY, 1);
			c.startAnimation(OPEN_ANIMATION);
			c.getPA().checkObjectSpawn(id + 1, x, y, 2, 10);
			Server.getTaskScheduler().schedule(new ScheduledTask(3) {
				@Override
				public void execute() {
					c.getItems().addItem(DRAGONSTONE, 1);
					c.getItems().addItem(995, Misc.random(160000));
					c.getItems().addItem(CHEST_REWARDS[Misc.random(getLength() - 1)], 1);
					c.sendMessage("You find some treasure in the chest.");
					c.getPA().checkObjectSpawn(id, x, y, 2, 10);
					if (Misc.random(15) == 0) {
						c.getItems().addItem(RARE_REWARDS[Misc.random(1)], 1);
					}
					super.stop();
				}
			}.attach(c));
		}
	}

	public static int getLength() {
		return CHEST_REWARDS.length;
	}

	public static int toothHalf() {
		return KEY_HALVES[0];
	}

	public static int loopHalf() {
		return KEY_HALVES[1];
	}
}
