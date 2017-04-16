package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.vd.player.Player;

public class Construction {

	public static void pickFlax(final Player c, final int x, final int y) {
		if (System.currentTimeMillis() - c.flaxTimer < 1000) {
			return;
		}
		if (c.getItems().freeSlots() != 0) {
			c.getItems().addItem(1779, 1);
			c.startAnimation(827);
			c.turnPlayerTo(x, y);
			c.sendMessage("You pick some flax.");
			c.flaxTimer = System.currentTimeMillis();
		} else {
			c.sendMessage("Not enough space in your inventory.");
			return;
		}

	}
}
