package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.player.Player;

/*
 * @author Tim
 */
public class Spinning {
	private static final int FLAX[][] = { { 1737, 1759, 1759, 20, 60 } };
	private static final int EMOTE = 896;

	public static void itemOnObject(Player c, int itemId) {
		for (int j = 0; j < FLAX.length; j++) {
			if (FLAX[j][0] == itemId)
				handleStringing(c, FLAX[j][0], j);
		}
	}

	public static void handleStringing(final Player c, final int id, final int slot) {
		if (c.playerLevel[12] < 20) {
			c.sendMessage("You need a Crafting level of " + FLAX[slot][3] + " to Craft this.");
			return;
		}
		if (!c.getItems().playerHasItem(id, 1)) {
			c.sendMessage("You don't have any wool to string.");
			return;
		}
		c.flaxCycles = c.getItems().getItemAmount(1737);
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void execute() {
				if (!c.getItems().playerHasItem(1737)) {
					c.sendMessage("You have finished spinning all of the wool in your inventory.");
					c.startAnimation(65535);
					this.stop();
					return;
				}
				if (System.currentTimeMillis() - c.spinDelay > 1700) {
					if (c.flaxCycles-- > 0) {
						c.startAnimation(EMOTE);
						c.getItems().deleteItem(id, c.getItems().getItemSlot(id), 1);
						c.getItems().addItem(FLAX[slot][1], 1);
						c.getPA().addSkillXP(FLAX[slot][4] * 2 * Constants.CRAFTING_EXPERIENCE, Player.playerCrafting);
						c.spinDelay = System.currentTimeMillis();
					} else {
						this.stop();
					}
				}
			}
		}.attach(c));
	}

}
