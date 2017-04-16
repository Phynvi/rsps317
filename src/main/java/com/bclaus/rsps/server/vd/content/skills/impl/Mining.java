/**
 * 
 */
package com.bclaus.rsps.server.vd.content.skills.impl;

/**
 * @author Tim http://rune-server.org/members/Someone
 *
 */

import java.util.ArrayList;
import java.util.Iterator;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.Player;

public class Mining extends SkillHandler {

	public static void attemptData(final Player c, final int object, final int obX, final int obY) {
		c.turnPlayerTo(c.objectX, c.objectY);
		if (!noInventorySpace(c, "mining")) {
			resetMining(c);
			return;
		}
		if (!hasRequiredLevel(c, 14, getLevelReq(c, object), "mining", "mine here")) {
			return;
		}
		if (!hasPickaxe(c)) {
			c.sendMessage("You need a pickaxe to mine this rock.");
			return;
		}
		if (Mining.isMineralDepleted(451, obX, obY)) {
			c.sendMessage("This mineral is depleted, wait until it refills.");
			return;
		}
		c.sendMessage("You swing your pickaxe at the rock.");
		if (c.playerSkilling[14]) {
			return;
		}
		c.playerSkilling[14] = true;
		c.stopPlayerSkill = true;
		c.startAnimation(getAnimation(c));
		for (int i = 0; i < data.length; i++) {
			if (object == data[i][0]) {
				c.playerSkillProp[14][0] = data[i][1];
				c.playerSkillProp[14][1] = data[i][3];
				c.startAnimation(getAnimation(c));
				Server.getTaskScheduler().schedule(new ScheduledTask(getTimer(c, object)) {
					@Override
					public void execute() {
						if (Mining.isMineralDepleted(451, obX, obY)) {
							c.sendMessage("This mineral is depleted, wait until it refills.");
							this.stop();
							return;
						}
						if (c.playerSkillProp[14][0] > 0) {
							c.getItems().addItem(c.playerSkillProp[14][0], 1);
							c.chance = Misc.random(5);
							if (c.playerEquipment[Player.playerWeapon] == 15813 && c.chance == 3 && c.getItems().freeSlots() > 2) {
								c.getItems().addItem(c.playerSkillProp[14][0], 1);
								c.sendMessage("You feel your pickaxe hit slightly harder..");
							}
							c.sendMessage("You manage to mine some " + c.getItems().getItemName(c.playerSkillProp[14][0]).toLowerCase() + ".");
							c.startAnimation(getAnimation(c));
						}
						if (c.playerSkillProp[14][1] > 0) {
							c.getPA().addSkillXP(c.playerSkillProp[14][1] * MINING_XP, Player.playerMining);
							if (object == 2491) {
							} else {
								Server.objectHandler.createAnObject(c, 451, obX, obY);
								minerals.add(new DepletedMineral(451, obX, obY));
							}
						}
						if (!hasPickaxe(c)) {
							c.sendMessage("You need a pickaxe to mine this rock.");
							resetMining(c);
							this.stop();
						}
						if (!c.stopPlayerSkill) {
							resetMining(c);
							this.stop();
						}
						if (!noInventorySpace(c, "mining")) {
							resetMining(c);
							this.stop();
						}
						if (object == 2491) {

						} else {
							resetMining(c);
							this.stop();
						}
					}
				}.attach(c));
				Server.getTaskScheduler().schedule(new ScheduledTask(getTimer(c, object) + getRespawnTime(c, object)) {
					@Override
					public void execute() {
						Server.objectHandler.createAnObject(c, object, obX, obY);
						Iterator<DepletedMineral> i = minerals.iterator();
						while (i.hasNext()) {
							DepletedMineral mineral = i.next();
							if (mineral.x == obX && mineral.y == obY) {
								i.remove();
							}
						}
						this.stop();
					}
				});
				Server.getTaskScheduler().schedule(new ScheduledTask(15) {
					@Override
					public void execute() {
						if (c.playerSkilling[14]) {
							c.startAnimation(getAnimation(c));
						}
						if (!c.stopPlayerSkill || !c.playerSkilling[14]) {
							resetMining(c);
							stop();
						}
					}
				}.attach(c));
			}
		}
	}

	private static int getTimer(Player c, int i) {
		return (getMineTime(c, i) + getTime(c) + playerMiningLevel(c));
	}

	private static int getMineTime(Player c, int object) {
		for (int i = 0; i < data.length; i++) {
			if (object == data[i][0]) {
				return data[i][4];
			}
		}
		return -1;
	}

	private static int playerMiningLevel(Player c) {
		return (10 - (int) Math.floor(c.playerLevel[14] / 10));
	}

	private static int getTime(Player c) {
		for (int i = 0; i < pickaxe.length; i++) {
			if (c.getItems().playerHasItem(pickaxe[i][0]) || c.playerEquipment[3] == pickaxe[i][0]) {
				if (c.playerLevel[Player.playerMining] >= pickaxe[i][1]) {
					return pickaxe[i][2];
				}
			}
		}
		return 10;
	}

	public static void resetMining(Player c) {
		c.playerSkilling[14] = false;
		c.stopPlayerSkill = false;
		for (int i = 0; i < 2; i++) {
			c.playerSkillProp[14][i] = -1;
		}
		c.startAnimation(65535);
	}

	public static boolean miningRocks(Player c, int object) {
		for (int i = 0; i < data.length; i++) {
			if (object == data[i][0]) {
				return true;
			}
		}
		return false;
	}

	private static int getRespawnTime(Player c, int object) {
		for (int i = 0; i < data.length; i++) {
			if (object == data[i][0]) {
				return data[i][5];
			}
		}
		return -1;
	}

	private static int getLevelReq(Player c, int object) {
		for (int i = 0; i < data.length; i++) {
			if (object == data[i][0]) {
				return data[i][2];
			}
		}
		return -1;
	}

	private static boolean hasPickaxe(Player c) {
		for (int i = 0; i < animation.length; i++) {
			if (c.getItems().playerHasItem(animation[i][0]) || c.playerEquipment[3] == animation[i][0]) {
				return true;
			}
		}
		return false;
	}

	private static int getAnimation(Player c) {
		for (int i = 0; i < animation.length; i++) {
			if (c.getItems().playerHasItem(animation[i][0]) || c.playerEquipment[3] == animation[i][0]) {
				return animation[i][1];
			}
		}
		return -1;
	}

	private static int[][] animation = { { 1275, 624 }, { 1271, 628 }, { 1273, 629 }, { 1269, 627 }, { 1267, 626 }, { 1265, 625 }, { 15813, 624 }, {15577, 624}, };
	private static int[][] pickaxe = { { 1275, 41, 0 }, // RUNE
			{ 15813, 61, 0 },
			{15577, 61, 0},
			{ 1271, 31, 1 }, // ADDY
			{ 1273, 21, 2 }, // MITH
			{ 1269, 6, 3 }, // STEEL
			{ 1267, 1, 3 }, // IRON
			{ 1265, 1, 4 }, // BRONZE
	};
	private static int[][] data = { 
			{ 2091, 436, 1, 36, 1, 5 }, // COPPER
			{ 2090, 436, 1, 36, 1, 5 }, // COPPER
			{ 2094, 438, 1, 36, 1, 5 }, // TIN
			{ 2095, 438, 1, 36, 1, 5 }, // TIN
			{ 2093, 440, 15, 70, 2, 5 }, // IRON
			{ 2092, 440, 15, 70, 2, 5 }, // IRON
			{ 2097, 453, 30, 100, 3, 8 }, // COAL
			{ 2096, 453, 30, 100, 3, 8 }, // COAL
			{ 2098, 444, 40, 135, 3, 10 }, // GOLD
			{ 2099, 444, 40, 135, 3, 10 }, // GOLD
			{ 2103, 447, 55, 160, 5, 20 }, // MITH
			{ 2102, 447, 55, 160, 5, 20 }, // MITH
			{ 2104, 449, 70, 190, 7, 50 }, // ADDY
			{ 2105, 449, 70, 190, 7, 50 }, // ADDY
			{ 2100, 442, 20, 160, 5, 5 }, // SILVER
			{ 2101, 442, 20, 160, 5, 5 }, // SILVER
			{ 2491, 1436, 20, 400, 5, 5 }, // Essence
			{ 2106, 451, 85, 450, 40, 50 },// RUNE
			{ 2107, 451, 85, 450, 40, 50 },// RUNE
			{ 14859, 451, 85, 450, 40, 50 },// RUNE
			{ 14860, 451, 85, 450, 40, 50 },// RUNE
	};

	public static void prospectRock(final Player c, final String itemName) {
		c.sendMessage("You examine the rock for ores...");
		Server.getTaskScheduler().schedule(new ScheduledTask(4) {
			@Override
			public void execute() {
				c.sendMessage("This rock contains " + itemName.toLowerCase() + ".");
				stop();
			}
		}.attach(c));
	}

	public static void prospectNothing(final Player c) {
		c.sendMessage("You examine the rock for ores...");
		Server.getTaskScheduler().schedule(new ScheduledTask(2) {
			@Override
			public void execute() {
				c.sendMessage("There is no ore left in this rock.");
				stop();
			}
		}.attach(c));
	}

	public static boolean isMineralDepleted(int id, int x, int y) {
		for (DepletedMineral mineral : minerals)
			if (mineral != null && mineral.id == id && mineral.x == x && mineral.y == y)
				return true;
		return false;
	}

	private static ArrayList<DepletedMineral> minerals = new ArrayList<DepletedMineral>();

	public static class DepletedMineral {

		int id, x, y;

		public DepletedMineral(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}
}
