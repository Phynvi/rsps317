package com.bclaus.rsps.server.vd.content.combat.magic;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.World;

public class MagicRequirements extends MagicData {

	public static boolean hasRunes(Player c, int[] runes, int[] amount) {
		for (int i = 0; i < runes.length; i++) {
			if (c.getItems().playerHasItem(runes[i], amount[i])) {
				return true;
			}
		}
		c.sendMessage("You don't have enough required runes to cast this spell!");
		return false;
	}

	public static void deleteRunes(Player c, int[] runes, int[] amount) {
		if (c.playerRights == 0) {
			for (int i = 0; i < runes.length; i++) {
				c.getItems().deleteItem(runes[i], c.getItems().getItemSlot(runes[i]), amount[i]);
			}
		}
	}

	public static boolean hasRequiredLevel(Player c, int i) {
		return c.playerLevel[6] >= i;
	}

	public static boolean wearingStaff(Player c, int runeId) {
		int wep = c.playerEquipment[Player.playerWeapon];
		switch (runeId) {
		// 6562
		case 554:
			if (wep == 1387 || wep == 3053)
				return true;
			break;
		case 555:
			if (wep == 1383 || wep == 6562 || wep == 1398)
				return true;
			break;
		case 556:
			if (wep == 1381)
				return true;
			break;
		case 557:
			if (wep == 1385 || wep == 6562 || wep == 3053 || wep == 6562)
				return true;
			break;
		}
		return false;
	}

	public static boolean checkMagicReqs(Player c, int spell) {
		if (c.playerEquipment[Player.playerWeapon] == 12346) {
			if (spell != 52) {
				c.sendMessage("You can only use the Water Wave attack with this weapon.");
				return false;
			}
		}
		if (c.usingMagic && Constants.RUNES_REQUIRED) { 
			
			if ((!c.getItems().playerHasItem(Player.MAGIC_SPELLS[spell][8], Player.MAGIC_SPELLS[spell][9]) && !wearingStaff(c, Player.MAGIC_SPELLS[spell][8])) || (!c.getItems().playerHasItem(Player.MAGIC_SPELLS[spell][10], Player.MAGIC_SPELLS[spell][11]) && !wearingStaff(c, Player.MAGIC_SPELLS[spell][10])) || (!c.getItems().playerHasItem(Player.MAGIC_SPELLS[spell][12], Player.MAGIC_SPELLS[spell][13]) && !wearingStaff(c, Player.MAGIC_SPELLS[spell][12])) || (!c.getItems().playerHasItem(Player.MAGIC_SPELLS[spell][14], Player.MAGIC_SPELLS[spell][15]) && !wearingStaff(c, Player.MAGIC_SPELLS[spell][14]))) {
				c.sendMessage("You don't have the required runes to cast this spell.");
				return false;
			}
		}
		if (c.usingMagic && c.playerIndex > 0) {
			if (World.PLAYERS.get(c.playerIndex) != null) {
				for (int r = 0; r < c.REDUCE_SPELLS.length; r++) {
					if (World.PLAYERS.get(c.playerIndex).REDUCE_SPELLS[r] == Player.MAGIC_SPELLS[spell][0]) {
						c.reduceSpellId = r;
						if ((System.currentTimeMillis() - World.PLAYERS.get(c.playerIndex).reduceSpellDelay[c.reduceSpellId]) > World.PLAYERS.get(c.playerIndex).REDUCE_SPELL_TIME[c.reduceSpellId]) {
							World.PLAYERS.get(c.playerIndex).canUseReducingSpell[c.reduceSpellId] = true;
						} else {
							World.PLAYERS.get(c.playerIndex).canUseReducingSpell[c.reduceSpellId] = false;
						}
						break;
					}
				}
				if (!World.PLAYERS.get(c.playerIndex).canUseReducingSpell[c.reduceSpellId]) {
					c.sendMessage("That player is currently immune to this spell.");
					c.usingMagic = false;
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return false;
				}
			}
		}

		if (c.duelStatus == 5 && World.PLAYERS.get(c.playerIndex).duelStatus == 5) {
			if (World.PLAYERS.get(c.playerIndex).duelingWith == c.getIndex()) {
				return true;
			} else {
				c.sendMessage("This isn't your opponent!");
				return false;
			}
		}
		int staffRequired = getStaffNeeded(c);
		if (c.usingMagic && staffRequired > 0 && Constants.RUNES_REQUIRED) { // staff
			if (c.playerEquipment[Player.playerWeapon] != staffRequired) {
				c.sendMessage("You need a " + c.getItems().getItemName(staffRequired).toLowerCase() + " to cast this spell.");
				return false;
			}
		}
		if (c.usingMagic && Constants.MAGIC_LEVEL_REQUIRED) { // check magic
																// level
			if (c.playerLevel[6] < Player.MAGIC_SPELLS[spell][1]) {
				c.sendMessage("You need to have a magic level of " + Player.MAGIC_SPELLS[spell][1] + " to cast this spell.");
				return false;
			}
		}
		if (c.usingMagic && Constants.RUNES_REQUIRED) {
			if (Player.MAGIC_SPELLS[spell][8] > 0) { // deleting runes
				if (!wearingStaff(c, Player.MAGIC_SPELLS[spell][8]))
					c.getItems().deleteItem(Player.MAGIC_SPELLS[spell][8], c.getItems().getItemSlot(Player.MAGIC_SPELLS[spell][8]), Player.MAGIC_SPELLS[spell][9]);
			}
			if (Player.MAGIC_SPELLS[spell][10] > 0) {
				if (!wearingStaff(c, Player.MAGIC_SPELLS[spell][10]))
					c.getItems().deleteItem(Player.MAGIC_SPELLS[spell][10], c.getItems().getItemSlot(Player.MAGIC_SPELLS[spell][10]), Player.MAGIC_SPELLS[spell][11]);
			}
			if (Player.MAGIC_SPELLS[spell][12] > 0) {
				if (!wearingStaff(c, Player.MAGIC_SPELLS[spell][12]))
					c.getItems().deleteItem(Player.MAGIC_SPELLS[spell][12], c.getItems().getItemSlot(Player.MAGIC_SPELLS[spell][12]), Player.MAGIC_SPELLS[spell][13]);
			}
			if (Player.MAGIC_SPELLS[spell][14] > 0) {
				if (!wearingStaff(c, Player.MAGIC_SPELLS[spell][14]))
					c.getItems().deleteItem(Player.MAGIC_SPELLS[spell][14], c.getItems().getItemSlot(Player.MAGIC_SPELLS[spell][14]), Player.MAGIC_SPELLS[spell][15]);
			}
		}
		return true;
	}

	public static final int FIRE = 554;
	public static final int WATER = 555;
	public static final int AIR = 556;
	public static final int EARTH = 557;
	public static final int MIND = 558;
	public static final int BODY = 559;
	public static final int DEATH = 560;
	public static final int NATURE = 561;
	public static final int CHAOS = 562;
	public static final int LAW = 563;
	public static final int COSMIC = 564;
	public static final int BLOOD = 565;
	public static final int SOUL = 566;
	public static final int ASTRAL = 9075;
}