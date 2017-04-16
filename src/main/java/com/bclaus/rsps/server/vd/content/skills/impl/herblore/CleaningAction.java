/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bclaus.rsps.server.vd.content.skills.impl.herblore;

import com.bclaus.rsps.server.vd.player.Player;

/**
 * 
 * @author Tim/Someone
 */

public class CleaningAction {

	/**
	 * Gives data for cleaning
	 */
	public static final int[][] data = { { 199, 249, 1, 3 }, // Guam leaf
			{ 201, 251, 5, 4 }, // Marrentil Indentify
			{ 203, 253, 11, 5 }, // Tarromin Indentify
			{ 205, 255, 20, 6 }, // Harralander Indentify
			{ 207, 257, 25, 8 }, // Ranarr weed Indentify
			{ 3049, 2998, 30, 8 }, // Toadflax Indentify
			{ 209, 259, 40, 9 }, // Irit leaf Indentify
			{ 211, 261, 48, 10 }, // Avantoe Indentify
			{ 213, 263, 54, 11 }, // Kwuarm Indentify
			{ 3051, 3000, 59, 12 }, // Snapdragon Indentify
			{ 215, 265, 65, 13 }, // Cadantine Indentify
			{ 2485, 2481, 67, 13 }, // Lantadyme Indentify
			{ 217, 267, 70, 14 }, // Dwarf weed Indentify
			{ 219, 269, 75, 15 }, { 11900, 11901, 85, 16 } // Grimy creature
	}; // Torstol Indentify

	/**
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isHerb(int item) {
		boolean isHerb = false;
		for (int[] element : data) {
			if (item == element[0]) {
				isHerb = true;
			}
		}
		return isHerb;
	}

	/**
	 * 
	 * @param c
	 * @param itemId
	 *            Checks if the player has requirements
	 */
	public static void handleCleaning(Player c, int itemId) {
		for (int[] element : data) {
			if (itemId == element[0]) {
				if (c.playerLevel[Player.playerHerblore] < element[2]) {
					c.sendMessage("You need a herblore level of " + element[2] + " to identifiy this herb.");
					return;
				}
				if (c.getItems().playerHasItem(element[0])) {
					c.getPA().addSkillXP(element[3] * 10, Player.playerHerblore);
					c.getItems().deleteItem(element[0], 1);
					c.getItems().addItem(element[1], 1);
					c.sendMessage("You clean and identify the herb as an " + c.getItems().getItemName(element[1]) + ".");
				}
				break;
			}
		}
	}
}
