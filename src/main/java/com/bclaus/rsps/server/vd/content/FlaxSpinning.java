package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.Constants;

public class FlaxSpinning {

	private static final int FLAX[][] = { { 1779, 1777, 1777, 40, 42 }, { 1737, 1760, 1760, 40, 120 } };
	private static final int EMOTE = 896;
	private static long spinDelay;

	public static void itemOnObject(Player c, int itemId) {
		for (int j = 0; j < FLAX.length; j++) {
			if (FLAX[j][0] == itemId)
				handleStringing(c, FLAX[j][0], j);
		}
	}

	public static void handleStringing(final Player c, final int id, final int slot) {
		if (!c.getItems().playerHasItem(id, 1)) {
			c.sendMessage("You don't have any flax to string.");
			return;
		}
		c.flaxCycles = c.getItems().getItemAmount(1779);
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {

			@Override
			public void execute() {
				if (!c.getItems().playerHasItem(1779)) {
					c.sendMessage("You have finished spinning all of the flax in your inventory.");
					c.startAnimation(65535);
					this.stop();
					return;
				}
				if (System.currentTimeMillis() - spinDelay > 1700) {
					if (c.flaxCycles-- > 0) {
						c.startAnimation(EMOTE);
						c.getItems().deleteItem(id, c.getItems().getItemSlot(id), 1);
						c.getItems().addItem(FLAX[slot][1], 1);
						c.getPA().addSkillXP(FLAX[slot][4] * Constants.CRAFTING_EXPERIENCE, Player.playerCrafting);
						spinDelay = System.currentTimeMillis();
					} else {
						this.stop();
					}
				}
			}
		}.attach(c));
	}

}
