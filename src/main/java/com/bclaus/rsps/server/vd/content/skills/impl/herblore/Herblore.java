/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bclaus.rsps.server.vd.content.skills.impl.herblore;

import com.bclaus.rsps.server.vd.content.achievements.AchievementType;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.content.achievements.Achievements;

/**
 * 
 * @author Tim/Someone
 */
public class Herblore {

	private static int[][] information = { { 199, 249, 1, 3 }, { 201, 251, 5, 4 }, { 203, 253, 11, 5 }, { 205, 255, 20, 6 }, { 207, 257, 25, 8 }, { 3049, 2998, 30, 8 }, { 209, 259, 40, 9 }, { 211, 261, 48, 10 }, { 213, 263, 54, 11 }, { 3051, 3000, 59, 12 }, { 215, 265, 65, 13 }, { 2485, 2481, 67, 13 }, { 217, 267, 70, 14 }, { 219, 269, 75, 15 }, { 11900, 11901, 90, 20 } };

	private static int[][] information2 = { { 249, 121, 2428, 1, 25 }, { 251, 175, 2446, 5, 38 }, { 253, 115, 113, 12, 50 }, { 255, 3010, 3008, 22, 63 },// <-Energy
			{ 257, 139, 2434, 38, 88 }, { 261, 2448, 2448, 40, 100 }, { 259, 145, 2436, 45, 100 }, { 263, 157, 2440, 55, 125 }, { 3000, 3026, 3024, 63, 143 }, { 265, 163, 2442, 66, 155 }, { 2481, 2454, 2452, 69, 158 }, { 267, 169, 2444, 72, 163 }, { 269, 189, 2450, 78, 175 }, { 2998, 6687, 6685, 81, 180 }, { 11901, 151, 2438, 90, 200 } };

	public static void handleHerbClick(Player player, int herbId) {
		for (int j = 0; j < information.length; j++) {
			if (information[j][0] == herbId)
				idHerb(player, j);
		}
	}

	public static void handlePotMaking(Player player, int item1, int item2) {
		if (item1 == 227 && isIdedHerb(item2))
			makePot(player, item2);
		else if (item2 == 227 && isIdedHerb(item1))
			makePot(player, item1);
	}

	public static boolean isUnidHerb(int clickId) {
		for (int[] anInformation : information)
			if (anInformation[0] == clickId)
				return true;
		return false;
	}

	public static boolean isIdedHerb(int item) {
		for (int[] anInformation2 : information2)
			if (anInformation2[0] == item)
				return true;
		return false;
	}

	private static void idHerb(Player player, int slot) {
		if (player.getItems().playerHasItem(information[slot][0])) {
			if (player.playerLevel[Player.playerHerblore] >= information[slot][2]) {
				player.getItems().deleteItem(information[slot][0], player.getItems().getItemSlot(information[slot][0]), 1);
				player.getItems().addItem(information[slot][1], 1);
				if (player.diffLevel != 100)
					player.getPA().addSkillXP(information[slot][3] * (Constants.HERBLORE_EXPERIENCE * player.diffLevel), Player.playerHerblore);
				else
					player.getPA().addSkillXP(information[slot][3], Player.playerHerblore);
				player.sendMessage("You identify the herb as a " + player.getItems().getItemName(information[slot][1]) + ".");
			} else {
				player.sendMessage("You need a herblore level of " + information[slot][2] + " to identify this herb.");
			}
		}
	}

	private static void makePot(Player player, int herbId) {
		if (player.getItems().playerHasItem(227) && player.getItems().playerHasItem(herbId)) {
			int slot = getSlot(herbId);
			/*if (player.getItems().playerHasItem(11901)) {
				player.sendMessage("You use the Combat herb on the vial of water to make a Super combat potion.");
				return;
			}*/
			if (player.playerLevel[Player.playerHerblore] >= information2[slot][3]) {
				player.startAnimation(363);
				player.getItems().deleteItem(herbId, player.getItems().getItemSlot(herbId), 1);
				player.getItems().deleteItem(227, player.getItems().getItemSlot(227), 1);
				player.getItems().addItem(information2[slot][2], 1);
				player.sendMessage("You make a " + player.getItems().getItemName(information2[slot][2]) + ".");
				player.sendMessage("" + herbId);
				if (herbId == 269)
					Achievements.increase(player, AchievementType.ZAMORAK_BREWER, 1);
				if (player.diffLevel != 100) {
					player.getPA().addSkillXP(information2[slot][4] * (Constants.HERBLORE_EXPERIENCE * player.diffLevel), Player.playerHerblore);
				} else {
					player.getPA().addSkillXP(information2[slot][4], Player.playerHerblore);
				}
			} else {
				player.sendMessage("You need a herblore level of " + information2[slot][3] + " to make this pot.");
			}
		}
	}

	private static int getSlot(int herb) {
		for (int j = 0; j < information2.length; j++)
			if (information2[j][0] == herb)
				return j;
		return -1;
	}

}
