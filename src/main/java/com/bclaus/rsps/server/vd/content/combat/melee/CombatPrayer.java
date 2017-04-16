package com.bclaus.rsps.server.vd.content.combat.melee;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.Constants;

/**
 * Handles prayers drain and switching
 * 
 * @author 2012
 * @author Organic
 */

public class CombatPrayer {

	public static final int THICK_SKIN = 0, BURST_OF_STRENGTH = 1, CLARITY_OF_THOUGHT = 2, SHARP_EYE = 3, MYSTIC_WILL = 4, ROCK_SKIN = 5, SUPERHUMAN_STRENGTH = 6, IMPROVED_REFLEXES = 7, RAPID_RESTORE = 8, RAPID_HEAL = 9, PROTECT_ITEM = 10, HAWK_EYE = 11, MYSTIC_LORE = 12, STEEL_SKIN = 13, ULTIMATE_STRENGTH = 14, INCREDIBLE_REFLEXES = 15, PROTECT_FROM_MAGIC = 16, PROTECT_FROM_RANGED = 17, PROTECT_FROM_MELEE = 18, EAGLE_EYE = 19, MYSTIC_MIGHT = 20, RETRIBUTION = 21, REDEMPTION = 22, SMITE = 23, CHIVALRY = 24, PIETY = 25;

	public static double[] prayerData = { 1, // Thick Skin.
			1, // Burst of Strength.
			1, // Clarity of Thought.
			2, // Sharp Eye.
			2, // Mystic Will.
			2, // Rock Skin.
			2, // SuperHuman Strength.
			2, // Improved Reflexes.
			2, // Rapid restore.
			2, // Rapid Heal.
			2, // Protect Items.
			3, // Hawk eye.
			3, // Mystic Lore.
			4, // Steel Skin.
			4, // Ultimate Strength.
			4, // Incredible Reflexes.
			4, // Protect from Magic.
			4, // Protect from Missiles.
			4, // Protect from Melee.
			4, // Eagle Eye.
			4, // Mystic Might.
			1, // Retribution.
			2, // Exorium.
			6, // Smite.
			8, // Chivalry.
			8, // Piety.
	};

	public static void handlePrayerDrain(Player c) {
		if (c.playerLevel[3] <= 0 || c.isDead)
			return;
		c.usingPrayer = false;
		double toRemove = 0.0;
		for (int j = 0; j < prayerData.length; j++) {
			if (c.prayerActive[j]) {
				toRemove += prayerData[j] / 20;
				c.usingPrayer = true;
			}
		}
		if (toRemove > 0) {
			toRemove /= (1 + (0.035 * c.playerBonus[11]));
		}
		c.prayerPoint -= toRemove;
		if (c.prayerPoint <= 0) {
			c.prayerPoint = 1.0 + c.prayerPoint;
			reducePrayerLevel(c);
		}
	}

	public static void reducePrayerLevel(Player c) {
		if (c.playerLevel[5] - 1 > 0) {
			c.playerLevel[5] -= 1;
		} else {
			c.sendMessage("You have run out of prayer points!");
			c.playerLevel[5] = 0;
			resetPrayers(c);
			c.prayerId = -1;
		}
		c.getPA().refreshSkill(5);
	}

	public static void resetPrayers(Player c) {
		for (int i = 0; i < c.prayerActive.length; i++) {
			c.prayerActive[i] = false;
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
		}
		c.headIcon = -1;
		c.getPA().requestUpdates();
	}

	public static void activatePrayer(Player c, int i) {
		if (c.duelRule[7]) {
			for (int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows
				c.prayerActive[p] = false;
				c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);
			}
			c.sendMessage("Prayer has been disabled in this duel!");
			return;
		}
		if (i == 24 && c.playerLevel[1] < 65) {
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
			c.sendMessage("You may not use this prayer yet.");
			return;
		}
		if (i == 25 && c.playerLevel[1] < 70) {
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
			c.sendMessage("You may not use this prayer yet.");
			return;
		}
		int[] defPray = { 0, 5, 13, 24, 25 };
		int[] strPray = { 1, 6, 14, 24, 25 };
		int[] atkPray = { 2, 7, 15, 24, 25 };
		int[] rangePray = { 3, 11, 19 };
		int[] magePray = { 4, 12, 20 };

		if (c.playerLevel[5] > 0 || !Constants.PRAYER_POINTS_REQUIRED) {
			if (c.getPA().getLevelForXP(c.playerXP[5]) >= c.PRAYER_LEVEL_REQUIRED[i] || !Constants.PRAYER_LEVEL_REQUIRED) {
				boolean headIcon = false;
				switch (i) {
				case 0:
				case 5:
				case 13:
					break;

				case 1:
				case 6:
				case 14:
					break;

				case 2:
				case 7:
				case 15:
					break;

				case 3:// range prays
				case 11:
				case 19:
					break;
					
				case 4:
				case 12:
				case 20:
					break;
					
				case 10:
					c.lastProtItem = System.currentTimeMillis();
					break;

				case 16:
				case 17:
				case 18:
					if (System.currentTimeMillis() - c.stopPrayerDelay < 5000) {
						c.sendMessage("You have been injured and can't use this prayer!");
						c.getPA().sendFrame36(c.PRAYER_GLOW[16], 0);
						c.getPA().sendFrame36(c.PRAYER_GLOW[17], 0);
						c.getPA().sendFrame36(c.PRAYER_GLOW[18], 0);
						return;
					}
					if (i == 16)
						c.protMageDelay = System.currentTimeMillis();
					else if (i == 17)
						c.protRangeDelay = System.currentTimeMillis();
					else if (i == 18)
						c.protMeleeDelay = System.currentTimeMillis();
				case 21:
				case 22:
				case 23:
					headIcon = true;
					break;
					
				case 24:
				case 25:
					break;
				}

				if (!headIcon) {
					if (c.prayerActive[i] == false) {
						c.prayerActive[i] = true;
						c.getPA().sendFrame36(c.PRAYER_GLOW[i], 1);
						
						for (int j : getTurnOff(i)) {
							c.prayerActive[j] = false;
							c.getPA().sendFrame36(c.PRAYER_GLOW[j], 0);
						}
					} else {
						c.prayerActive[i] = false;
						c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
					}
				} else {
					if (c.prayerActive[i] == false) {
						c.prayerActive[i] = true;
						c.getPA().sendFrame36(c.PRAYER_GLOW[i], 1);
						c.headIcon = c.PRAYER_HEAD_ICONS[i];
						
						for (int j : getTurnOff(i)) {
							c.prayerActive[j] = false;
							c.getPA().sendFrame36(c.PRAYER_GLOW[j], 0);
						}
						
						c.getPA().requestUpdates();
					} else {
						c.prayerActive[i] = false;
						c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
						c.headIcon = -1;
						c.getPA().requestUpdates();
					}
				}
			} else {
				c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
				c.getPA().sendFrame126("You need a @blu@Prayer level of " + c.PRAYER_LEVEL_REQUIRED[i] + " to use " + c.PRAYER_NAME[i] + ".", 357);
				c.getPA().sendFrame126("Click here to continue", 358);
				c.getPA().sendFrame164(356);
			}
		} else {
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
			c.sendMessage("You have run out of prayer points!");
		}
	}

	public static int[] getTurnOff(int id) {
		System.out.println("Turn offs for " + id);
		int[] turnOff = new int[0];
		switch (id) {
		case THICK_SKIN:
			turnOff = new int[] { ROCK_SKIN, STEEL_SKIN, CHIVALRY, PIETY };
			break;
		case ROCK_SKIN:
			turnOff = new int[] { THICK_SKIN, STEEL_SKIN, CHIVALRY, PIETY };
			break;
		case STEEL_SKIN:
			turnOff = new int[] { THICK_SKIN, ROCK_SKIN, CHIVALRY, PIETY };
			break;
		case CLARITY_OF_THOUGHT:
			turnOff = new int[] { IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY };
			break;
		case IMPROVED_REFLEXES:
			turnOff = new int[] { CLARITY_OF_THOUGHT, INCREDIBLE_REFLEXES, CHIVALRY, PIETY };
			break;
		case INCREDIBLE_REFLEXES:
			turnOff = new int[] { IMPROVED_REFLEXES, CLARITY_OF_THOUGHT, CHIVALRY, PIETY };
			break;
		case BURST_OF_STRENGTH:
			turnOff = new int[] { SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, SHARP_EYE, MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY };
			break;
		case SUPERHUMAN_STRENGTH:
			turnOff = new int[] { BURST_OF_STRENGTH, ULTIMATE_STRENGTH, SHARP_EYE, MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY };
			break;
		case ULTIMATE_STRENGTH:
			turnOff = new int[] { SUPERHUMAN_STRENGTH, BURST_OF_STRENGTH, SHARP_EYE, MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY };
			break;
		case SHARP_EYE:
			turnOff = new int[] { EAGLE_EYE, MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, PIETY, MYSTIC_MIGHT, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, THICK_SKIN, ROCK_SKIN, STEEL_SKIN };
			break;
		case HAWK_EYE:
			turnOff = new int[] { SHARP_EYE, MYSTIC_WILL, EAGLE_EYE, MYSTIC_LORE, PIETY, MYSTIC_MIGHT, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, THICK_SKIN, ROCK_SKIN, STEEL_SKIN };
			break;
		case EAGLE_EYE:
			turnOff = new int[] { SHARP_EYE, MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, PIETY, MYSTIC_MIGHT, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, THICK_SKIN, ROCK_SKIN, STEEL_SKIN };
			break;
		case MYSTIC_WILL:
			turnOff = new int[] { SHARP_EYE, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY };
			break;
		case MYSTIC_LORE:
			turnOff = new int[] { MYSTIC_WILL, HAWK_EYE, SHARP_EYE, EAGLE_EYE, MYSTIC_MIGHT, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY };
			break;
		case MYSTIC_MIGHT:
			turnOff = new int[] { MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, SHARP_EYE, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, PIETY };
			break;
		case PROTECT_FROM_MAGIC:
			turnOff = new int[] { REDEMPTION, SMITE, RETRIBUTION, PROTECT_FROM_RANGED, PROTECT_FROM_MELEE };
			break;
		case PROTECT_FROM_RANGED:
			turnOff = new int[] { REDEMPTION, SMITE, RETRIBUTION, PROTECT_FROM_MAGIC, PROTECT_FROM_MELEE };
			break;
		case PROTECT_FROM_MELEE:
			turnOff = new int[] { REDEMPTION, SMITE, RETRIBUTION, PROTECT_FROM_RANGED, PROTECT_FROM_MAGIC };
			break;
		case RETRIBUTION:
			turnOff = new int[] { REDEMPTION, SMITE, PROTECT_FROM_MELEE, PROTECT_FROM_RANGED, PROTECT_FROM_MAGIC };
			break;
		case REDEMPTION:
			turnOff = new int[] { RETRIBUTION, SMITE, PROTECT_FROM_MELEE, PROTECT_FROM_RANGED, PROTECT_FROM_MAGIC };
			break;
		case SMITE:
			turnOff = new int[] { REDEMPTION, RETRIBUTION, PROTECT_FROM_MELEE, PROTECT_FROM_RANGED, PROTECT_FROM_MAGIC };
			break;
		case CHIVALRY:
			turnOff = new int[] { SHARP_EYE, MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, PIETY, THICK_SKIN, ROCK_SKIN, STEEL_SKIN };
			break;
		case PIETY:
			turnOff = new int[] { SHARP_EYE, MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT, BURST_OF_STRENGTH, SUPERHUMAN_STRENGTH, ULTIMATE_STRENGTH, CLARITY_OF_THOUGHT, IMPROVED_REFLEXES, INCREDIBLE_REFLEXES, CHIVALRY, THICK_SKIN, ROCK_SKIN, STEEL_SKIN };
			break;
		}
		return turnOff;
	}
}