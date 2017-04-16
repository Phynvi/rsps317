package com.bclaus.rsps.server.vd.content.combat.range;

import com.bclaus.rsps.server.vd.player.Player;

public class RangeMaxHit extends RangeData {

	public static int calculateRangeDefence(Player c) {
		int defenceLevel = c.playerLevel[1];
		if (c.prayerActive[0]) {
			defenceLevel += Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.05;
		} else if (c.prayerActive[5]) {
			defenceLevel += Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.1;
		} else if (c.prayerActive[13]) {
			defenceLevel += Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.15;
		} else if (c.prayerActive[24]) {
			defenceLevel += Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.2;
		} else if (c.prayerActive[25]) {
			defenceLevel += Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.25;
		}
		return defenceLevel + c.playerBonus[9] + (c.playerBonus[9] / 2);
	}

	public static int calculateRangeAttack(Player c) {
		int attackLevel = c.playerLevel[4];
		attackLevel *= c.specAccuracy;
		if (c.prayerActive[3])
			attackLevel *= 1.05;
		else if (c.prayerActive[11])
			attackLevel *= 1.10;
		else if (c.prayerActive[19])
			attackLevel *= 1.15;

		return (int) (attackLevel + (c.playerBonus[4] * 1.95));
	}

	public static int maxHit(Player c) {
		int rangeLevel = c.playerLevel[4];
		double modifier = 1.0;
		double wtf = c.specDamage;
		int itemUsed = c.usingBow ? c.lastArrowUsed : c.lastWeaponUsed;
		if (c.prayerActive[3])
			modifier += 0.05;
		else if (c.prayerActive[11])
			modifier += 0.10;
		else if (c.prayerActive[19])
			modifier += 0.15;
		double c1 = modifier * rangeLevel;
		int rangeStr = getRangeStr(itemUsed);
		if(hasCrystalBow(c)) {
			rangeStr = getRangeStr(c.playerEquipment[Player.playerWeapon]);
		}
		double max = (c1 + 12) * (rangeStr + 74) / 640;
		if (wtf != 1)
			max *= wtf;
		if (max < 1)
			max = 1;
		return (int) max;
	}

}
