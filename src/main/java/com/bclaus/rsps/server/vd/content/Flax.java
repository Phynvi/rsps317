package com.bclaus.rsps.server.vd.content;

import java.util.ArrayList;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.ObjectManager;
import com.bclaus.rsps.server.util.Misc;

public class Flax {

	public static ArrayList<int[]> flaxRemoved = new ArrayList<int[]>();

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
			if (Misc.random(3) == 1) {
				flaxRemoved.add(new int[] { x, y });
				ObjectManager.removeObject(x, y);
			}
		} else {
			c.sendMessage("Not enough space in your inventory.");
			return;
		}

	}
}
