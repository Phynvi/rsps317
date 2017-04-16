package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author Genesis
 */
public class HandleEmpty {

	public static boolean canEmpty(int id) {
		return filledToEmpty(id) != -1;
	}

	public static int filledToEmpty(int id) {
		switch (id) {
		case 1937: // Jugs
		case 1989:
		case 1991:
		case 1993:
		case 3729:
			return 1935;
		case 227: // Vial of Water
			return 229;
		case 1927: // Buckets
		case 1929:
			return 1925;
		}
		return -1;
	}

	public static void handleEmptyItem(Player c, int itemId, int giveItem) {
		final String name = c.getItems().getItemName(itemId);
		c.sendMessage("You empty your " + name + ".");
		c.getItems().deleteItem(itemId, 1);
		c.getItems().addItem(giveItem, 1);
	}

}
