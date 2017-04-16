package com.bclaus.rsps.server.vd.content.skills.impl;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.player.Player;

public class SkillHandler {

	public static final int AGILITY_XP = Constants.AGILITY_EXPERIENCE;
	public static final int PRAYER_XP = Constants.PRAYER_EXPERIENCE;
	public static final int MINING_XP = Constants.MINING_EXPERIENCE;
	public static final int COOKING_XP = Constants.COOKING_EXPERIENCE;
	public static final int RUNECRAFTING_XP = Constants.RUNECRAFTING_EXPERIENCE;
	public static final int WOODCUTTING_XP = Constants.WOODCUTTING_EXPERIENCE;
	public static final int THIEVING_XP = Constants.THIEVING_EXPERIENCE;
	public static final int FLETCHING_XP = Constants.FLETCHING_EXPERIENCE;
	public static final int FIREMAKING_XP = Constants.FIREMAKING_EXPERIENCE;

	public static boolean noInventorySpace(Player c, String skill) {
		if (c.getItems().freeSlots() == 0) {
			c.sendMessage("You don't have enough inventory space.");
			return false;
		}
		return true;
	}

	public static boolean hasRequiredLevel(final Player c, int id, int lvlReq, String skill, String event) {
		if (c.playerLevel[id] < lvlReq) {
			c.sendMessage("You at least need a " + skill + " level of " + lvlReq + " to " + event + ".");
			return false;
		}
		return true;
	}

	public static void deleteTime(Player c) {
		c.doAmount--;
	}
}