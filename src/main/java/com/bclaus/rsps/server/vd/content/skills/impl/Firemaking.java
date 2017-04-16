package com.bclaus.rsps.server.vd.content.skills.impl;

import com.bclaus.rsps.server.Server;
import org.omicron.jagex.runescape.CollisionMap;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.task.ScheduledTask;

public class Firemaking extends SkillHandler {

	private static int[][] data = { { 1511, 1, 40, 2732 }, // LOG
			{ 7406, 1, 315, 11406 }, // BLUE LOG
			{ 7405, 1, 315, 11405 }, // GREEN LOG
			{ 7404, 1, 315, 11404 }, // RED LOG
			{ 10328, 1, 315, 20000 }, // WHITE LOG
			{ 10329, 1, 315, 20001 }, // PURPLE LOG
			{ 2862, 1, 40, 2732 }, // ACHEY
			{ 1521, 15, 60, 2732 }, // OAK
			{ 1519, 30, 105, 2732 }, // WILLOW
			{ 6333, 35, 105, 2732 }, // TEAK
			{ 1517, 45, 135, 2732 }, // MAPLE
			{ 10810, 45, 135, 2732 }, // ARTIC PINE
			{ 6332, 50, 158, 2732 }, // MAHOGANY
			{ 1515, 60, 203, 2732 }, // YEW
			{ 1513, 75, 304, 2732 }, // MAGIC
	};

	public static boolean playerLogs(int i, int l) {
		boolean flag = false;
		for (int[] aData : data) {
			if ((i == aData[0] && requiredItem(l)) || (requiredItem(i) && l == aData[0])) {
				flag = true;
			}
		}
		return flag;
	}

	private static int getAnimation(int item, int item1) {
		int[][] dataobtained = { { 841, 6714 }, { 843, 6715 }, { 849, 6716 }, { 853, 6717 }, { 857, 6718 }, { 861, 6719 }, };
		for (int[] aDataobtained : dataobtained) {
			if (item == aDataobtained[0] || item1 == aDataobtained[0]) {
				return aDataobtained[1];
			}
		}
		return 733;
	}

	private static boolean requiredItem(int i) {
		int[] dataobtained = { 841, 843, 849, 853, 857, 861, 590 };
		for (int aDataobtained : dataobtained) {
			if (i == aDataobtained) {
				return true;
			}
		}
		return false;
	}

	public static void grabData(final Player c, final int useWith, final int withUse) {
		if (c.inBank()) {
			c.sendMessage("You can't light a fire in the bank!");
			return;
		}
		if (c.absX == 2855 && c.absY == 3441) {
			return;
		}
		if (c.inSkillArea()) {
			c.sendMessage("You can't light a fire near these ores!");
			return;
		}

		if (c.altars()) {
			c.sendMessage("You can't light a fire near these altars!");
			return;
		}

		final int[] coords = new int[3];
		coords[0] = c.getAbsX();
		coords[1] = c.getAbsY();
		coords[2] = c.getHeight();
		for (int[] aData : data) {
			if (Server.objectHandler.fireExists(aData[3], c.absX, c.absY, c.height) != null) {
				c.sendMessage("You can't light a fire on a fire!");
				return;
			}
			if ((requiredItem(useWith) && withUse == aData[0] || useWith == aData[0] && requiredItem(withUse))) {
				if (c.playerLevel[11] < aData[1]) {
					c.sendMessage("You don't have the correct Firemaking level to light this log!");
					c.sendMessage("You need the Firemaking level of at least " + aData[1] + ".");
					return;
				}
				if (System.currentTimeMillis() - c.lastFire > 1200) {

					if (c.playerIsFiremaking) {
						return;
					}

					final int[] time = new int[3];
					final int log = aData[0];
					final int fire = aData[3];
					if (System.currentTimeMillis() - c.lastFire > 3000) {
						c.startAnimation(getAnimation(useWith, withUse));
						time[0] = 4;
						time[1] = 3;
					} else {
						time[0] = 1;
						time[1] = 2;
					}

					c.playerIsFiremaking = true;
					c.playerIsWoodcutting = false;
					Server.itemHandler.createGroundItem(c, log, coords[0], coords[1], coords[2], 1);

					Server.getTaskScheduler().schedule(new ScheduledTask(time[0]) {
						@Override
						public void execute() {
							Server.objectHandler.createAnObject(c, fire, coords[0], coords[1]);
							Server.itemHandler.removeGroundItem(c, log, coords[0], coords[1], c.getHeight(), false);
							c.playerIsFiremaking = false;
							stop();
						}
					}.attach(c));

					if (!CollisionMap.isEastBlocked(c.heightLevel, c.absX - 1, c.absY)) {
						c.getPA().walkTo(-1, 0);
					} else if (!CollisionMap.isWestBlocked(c.heightLevel, c.absX + 1, c.absY)) {
						c.getPA().walkTo(1, 0);
					}

					c.sendMessage("You light the logs.");
					if (c.timesLit < 10) {
						c.timesLit++;
					} else {
						c.sendMessage("You successfully light 10 fire in a row and gain 1 FM Point.");
						c.fireMakingPoints++;
						c.timesLit = 0;
					}
					Server.getTaskScheduler().schedule(new ScheduledTask(time[1]) {
						@Override
						public void execute() {
							c.startAnimation(65535);
							stop();
						}
					}.attach(c));

					Server.getTaskScheduler().schedule(new ScheduledTask(100) {
						@Override
						public void execute() {
							Server.objectHandler.createAnObject(c, -1, coords[0], coords[1]);
							stop();
						}

						@Override
						public void onStop() {
							if (c.getOutStream() != null && c != null && !c.disconnected) {
								Server.itemHandler.createGroundItem(c, 592, coords[0], coords[1], coords[2], 1);
							}
						}
					}.attach(c));
					c.getPA().addSkillXP(aData[2] * FIREMAKING_XP, 11);
					c.turnPlayerTo(c.getAbsX() + 1, c.getAbsY());
					c.getItems().deleteItem(aData[0], c.getItems().getItemSlot(aData[0]), 1);
					c.lastFire = System.currentTimeMillis();
				}
			}
		}
	}
}