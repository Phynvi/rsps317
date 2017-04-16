package com.bclaus.rsps.server.vd.content;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.content.achievements.AchievementType;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.content.achievements.Achievements;

public class PVPAssistant {

	static DecimalFormat format = new DecimalFormat("##.##");

	public static final int MINIMUM_ARTIFACT_ID = 14892;
	public static final int MAXIMUM_ARTIFACT_ID = 14876;
	public static final int FOOD = 0;
	public static final int POTIONS_JEWELLERY = 1;
	public static final int LOW_LEVEL = 2;
	public static final int MED_LEVEL = 3;
	public static final int HIGH_LEVEL = 4;
	public static final int CORRUPT = 5;
	public static final int PVP = 6;
	public static final int BRAWLING_GLOVES = 7;
	public static final int ARTIFACTS = 8;
	public static final int RARE = 9;

	public static final int ARTIFACT_POINTS[][] = { { 14892, 25 }, { 14891, 50 }, { 14890, 75 }, { 14889, 100 }, { 14888, 125 }, { 14887, 150 }, { 14886, 175 }, { 14885, 200 }, { 14884, 225 }, { 14883, 250 }, { 14882, 275 }, { 14881, 300 }, { 14880, 325 }, { 14879, 350 }, { 14878, 375 }, { 14877, 400 }, { 14876, 425 },

	};

	public static final int ITEM_DROP_DATA[][] = { { 361, 329, 379, 373, 7946, 385, 391, 397 }, // FOOD
			{ 157, 121, 3026, 113, 139, 163, 3042, 169, 2550, 6685, 2552, 2570, 1478, 1704, 1725, 1727, 1729, 1731, 11126, 3853 }, // POTIONS
			{ 6106, 6108, 6110, 3105, 11133, 1331, 1317, 5574, 5575, 5576, 1147, 2489, 2495, 2501, 861, 859, 9185, 542, 544 }, // LOW-LEVEL
			{ 4131, 1201, 2491, 2497, 2503, 1333, 1319, 1303, 1289, 1079, 1127, 1113, 1163, 6522, 6523, 6524, 6525, 6526 }, // MED-LEVEL
			{ 10828, 1079, 1127, 1163, 1319, 1333, 5698, 6528, 1215, 1201, 4587, 1305, 1434, 4153, 4675, 3122, 6809, 11128, 1187, 2746, 2748 }, // HIGH-LEVEL
			{}, { 14892, 14891, 14890, 14889, 14888, 14887, 14886, 14885, 14884, 14883, 14882, 14881, 14880, 14879, 14878, 14877, 14876 }, // ARTIFACTS
			{ 11283, 6585, 11732, 4151, 15126, 15021 }, };

	public static void dropItem(Player player, int x, int y) {
		if (player.hasDisabledEpDrops) {
			player.sendMessage("You do not receive any EP drops because you currently have them disabled!");
			return;
		}
		if (player.getWildernessPotential() < 10.0) {
			player.sendMessage("You did not receive a pvp drop because your potential was not above 10%.");
			return;
		}
		int potential = ((int) player.getWildernessPotential() / 2) + Misc.random((int) player.getWildernessPotential() / 2);
		player.setWildernessPotential(player.getWildernessPotential() - potential);
		int amountOfFood = 2 + Misc.random(2);
		ArrayList<Integer> items = new ArrayList<Integer>();
		if (potential > 0)
			for (int i = 0; i < amountOfFood; i++)
				items.add(ITEM_DROP_DATA[FOOD][Misc.random(ITEM_DROP_DATA[FOOD].length - 1)]);
		if (potential >= 0)
			items.add(ITEM_DROP_DATA[POTIONS_JEWELLERY][Misc.random(ITEM_DROP_DATA[POTIONS_JEWELLERY].length - 1)]);
		if (potential > 5 && potential < 25) {
			items.add(ITEM_DROP_DATA[LOW_LEVEL][Misc.random(ITEM_DROP_DATA[LOW_LEVEL].length - 1)]);
			items.add(ITEM_DROP_DATA[MED_LEVEL][Misc.random(ITEM_DROP_DATA[MED_LEVEL].length - 1)]);
		}
		if (potential >= 30 && potential < 40) {
			items.add(ITEM_DROP_DATA[MED_LEVEL][Misc.random(ITEM_DROP_DATA[MED_LEVEL].length - 1)]);
			items.add(ITEM_DROP_DATA[HIGH_LEVEL][Misc.random(ITEM_DROP_DATA[HIGH_LEVEL].length - 1)]);
		}
		if (potential >= 60) {
			items.add(ITEM_DROP_DATA[HIGH_LEVEL][Misc.random(ITEM_DROP_DATA[HIGH_LEVEL].length - 1)]);
			items.add(ITEM_DROP_DATA[HIGH_LEVEL][Misc.random(ITEM_DROP_DATA[HIGH_LEVEL].length - 1)]);
		}
		if (potential >= 10)
			if (Misc.random(10) == 0)
				items.add(ITEM_DROP_DATA[CORRUPT][Misc.random(ITEM_DROP_DATA[CORRUPT].length - 1)]);
		if (potential >= 12)
			if (Misc.random(15) == 0)
				items.add(ITEM_DROP_DATA[CORRUPT][Misc.random(ITEM_DROP_DATA[CORRUPT].length - 1)]);
		if (potential >= 45) {
			if (Misc.random(10) == 0)
				items.add(ITEM_DROP_DATA[PVP][Misc.random(ITEM_DROP_DATA[PVP].length - 1)]);
			else if (Misc.random(20) == 0)
				items.add(ITEM_DROP_DATA[RARE][Misc.random(ITEM_DROP_DATA[RARE].length - 1)]);
		}
		items.add(potential < 30 ? ITEM_DROP_DATA[ARTIFACTS][Misc.random(8)] : potential >= 10 && potential < 65 ? ITEM_DROP_DATA[ARTIFACTS][4 + Misc.random(12)] : ITEM_DROP_DATA[ARTIFACTS][8 + Misc.random(8)]);
		for (int i = 0; i < items.size(); i++)
			Server.itemHandler.createGroundItem(player, items.get(i).intValue(), x, y, player.heightLevel, 1);
		player.updateWalkEntities();
		Achievements.increase(player, AchievementType.PVP, 1);
	}

	public static String representLevelDifference(Player player) {
		int minimum = player.combatLevel - 12 < 3 ? 3 : player.combatLevel - 12;
		int maximum = player.combatLevel + 12 > 126 ? 126 : player.combatLevel + 12;
		return minimum + " - " + maximum;
	}

	public static boolean canAttack(Player player, Player otherPlayer) {
		int minimum = player.combatLevel - 12 < 3 ? 3 : player.combatLevel - 12;
		int maximum = player.combatLevel + 12 > 126 ? 126 : player.combatLevel + 12;
		return minimum >= otherPlayer.combatLevel && maximum <= otherPlayer.combatLevel;
	}

	public static double getRatio(int kills, int deaths) {
		double ratio = kills / Math.max(1D, deaths);
		return ratio;
	}

	public static double getRatio(Player player) {
		return PVPAssistant.getRatio(player.playerKillCount, player.playerDeathCount);
	}

	public static String displayRatio(Player player) {
		return format.format(getRatio(player));
	}

	public static void updateStatistics(Player player) {
		player.getPA().sendFrame126("Kills: " + player.originalKillCount, 29002);
		player.getPA().sendFrame126("Deaths: " + player.originalDeathCount, 29003);
		player.getPA().sendFrame126("KDR: " + displayRatio(player), 29004);
		player.getPA().sendFrame126("My Killstreak: " + player.killStreak, 29014);
		player.getPA().sendFrame126("Max Killstreak: " + player.highestKillStreak, 29015);
	}

	public static void process(Player player) {
		if (!player.inWild()) {
			player.potentialStage = 0;
			return;
		}
		if (player.inWild()) {
				if (System.currentTimeMillis() - player.lastEearningPotential > 75000) {
					if (player.potentialStage == 2)
						addPotential(player, 1D);
					player.lastEearningPotential = System.currentTimeMillis();
					player.potentialStage = 2;
				}
		} else {
			if (player.potentialStage > 0)
				player.potentialStage = 0;
		}

	}

	public static void increasePotential(Player player, int damage) {
		if (player.getWildernessPotential() >= 100D)
			return;
		if (damage <= 0)
			return;
		if (!player.inWild())
			return;
		player.setWildernessPotential(player.getWildernessPotential() + (0.004D * damage));
	}

	public static void addPotential(Player player, double value) {
		player.sendMessage("" + player.getWildernessPotential());
		if (player.getWildernessPotential() >= 100D)
			return;
		player.sendMessage("" + player.getWildernessPotential());
		if (player.combatLevel < 50) {
			player.sendMessage("You must be over 50 Combat to gain wilderness potential.");
			return;
		}
		player.setWildernessPotential(player.getWildernessPotential() + value);
		if (player.getWildernessPotential() > 100D)
			player.setWildernessPotential(100D);

	}

	public static void tradeSingleArtefact(Player player, int itemId) {
		boolean found = false;
		for (int i = 0; i < ARTIFACT_POINTS.length; i++) {
			if (ARTIFACT_POINTS[i][0] == itemId) {
				player.getItems().deleteItem2(itemId, 1);
				player.pkPoints += ARTIFACT_POINTS[i][1];
				player.getDH().sendStatement("You sell a @blu@" + player.getItems().getItemName(ARTIFACT_POINTS[i][0]) + "@bla@ for @blu@" + (ARTIFACT_POINTS[i][1]) + " @bla@pk points to Peksa.", "Please come back to trade more artefacts.");
				player.nextChat = -1;
				found = true;
				break;
			}
		}
		if (!found)
			player.sendMessage("You cannot trade this item to Mandrith, it must be an artefact.");
	}

	public static void tradeArtifacts(Player c) {
		int cash = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] > 0) {
				for (int j = 0; j < ARTIFACT_POINTS.length; j++) {
					if ((c.playerItems[i] - 1) == ARTIFACT_POINTS[j][0]) {
						cash += ARTIFACT_POINTS[j][1];
						c.playerItems[i] = 0;
						c.playerItemsN[i] = 0;
						continue;
					}
				}
			}
		}
		c.getItems().updateInventory();
		if (cash == 0) {
			c.getDH().sendStatement("It seems you don't have any artifacts for trade.", "Please come back when you do.");
			c.nextChat = -1;
		} else {
			c.pkPoints += cash;
			c.sendMessage("You receive " + cash + " pk points for the artefacts.");
			c.getPA().removeAllWindows();
		}
	}

}
