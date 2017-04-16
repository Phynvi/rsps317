/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bclaus.rsps.server.vd.content.combat.melee;

import com.bclaus.rsps.server.vd.player.Player;

/**
 * 
 * @author Tim/Someone
 */
public class MeleeMaxHit {

	public static int calculateMeleeMaxHit(Player client) {
		int maxHit = 0;
		double prayerMultiplier = 1;
		double otherBonusMultiplier = 1; // TODO: void melee = 1.2, slayer helm
		int strengthLevel = client.playerLevel[2];
		int combatStyleBonus = 0;
		if (client.prayerActive[1]) {
			prayerMultiplier = 1.05;
		} else if (client.prayerActive[6]) {
			prayerMultiplier = 1.1;
		} else if (client.prayerActive[14]) {
			prayerMultiplier = 1.15;
		} else if (client.prayerActive[24]) {
			prayerMultiplier = 1.18;
		} else if (client.prayerActive[25]) {
			prayerMultiplier = 1.23;
		}
		combatStyleBonus = 2;
		int effectiveStrengthDamage = (int) ((strengthLevel * prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
		double baseDamage = 1.3 + (effectiveStrengthDamage / 10) + (client.playerBonus[10] / 80) + ((effectiveStrengthDamage * client.playerBonus[10]) / 640);

		maxHit = (int) (baseDamage * client.specDamage);
		if (obsidianEffect(client)) {
			maxHit += 6;
		}
		if (fullVoidMelee(client) || fullVoidMage(client) || fullVoidRange(client)) {
			maxHit += 8;
		}
		if (client.getCombat().fullDharok()) {
			int hpLost = Player.getLevelForXP(client.playerXP[Player.playerHitpoints]) - client.playerLevel[Player.playerHitpoints];
			maxHit += hpLost * 0.35;
		}
		return maxHit;
	}

	public static int calculateMeleeAttack(Player c) {
		int attackLevel = c.playerLevel[0];
		if (c.prayerActive[2]) {
			attackLevel = (int) (attackLevel + Player.getLevelForXP(c.playerXP[Player.playerAttack]) * 0.05D);
		} else if (c.prayerActive[7]) {
			attackLevel = (int) (attackLevel + Player.getLevelForXP(c.playerXP[Player.playerAttack]) * 0.1D);
		} else if (c.prayerActive[15]) {
			attackLevel = (int) (attackLevel + Player.getLevelForXP(c.playerXP[Player.playerAttack]) * 0.15D);
		} else if (c.prayerActive[24]) {
			attackLevel = (int) (attackLevel + Player.getLevelForXP(c.playerXP[Player.playerAttack]) * 0.15D);
		} else if (c.prayerActive[25]) {
			attackLevel = (int) (attackLevel + Player.getLevelForXP(c.playerXP[Player.playerAttack]) * 0.2D);
		}
		attackLevel = (int) (attackLevel * c.specAccuracy);
		int i = c.playerBonus[bestMeleeAtk(c)];
		i += c.bonusAttack;
		if (c.playerEquipment[Player.playerAmulet] == 11128 && c.playerEquipment[Player.playerWeapon] == 6528) {
			i = (int) (i * 1.3D);
		}
		int toReturn = (int) (attackLevel + attackLevel * 0.15D + i + i * 0.5D);
		if (c.getCombat().fullDharok()) {
			toReturn += 250;
		}
		return toReturn;
	}

	public static int bestMeleeAtk(Player c) {
		return c.playerBonus[0] > c.playerBonus[1] && c.playerBonus[0] > c.playerBonus[2] ? 0 : (c.playerBonus[1] > c.playerBonus[0] && c.playerBonus[1] > c.playerBonus[2] ? 1 : (c.playerBonus[2] > c.playerBonus[1] && c.playerBonus[2] > c.playerBonus[0] ? 2 : 0));
	}

	public static int calculateMeleeDefence(Player c) {
		int defenceLevel = c.playerLevel[1];
		int i = c.playerBonus[bestMeleeDef(c)];
		if (c.prayerActive[0]) {
			defenceLevel = (int) (defenceLevel + Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.05D);
		} else if (c.prayerActive[5]) {
			defenceLevel = (int) (defenceLevel + Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.1D);
		} else if (c.prayerActive[13]) {
			defenceLevel = (int) (defenceLevel + Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.15D);
		} else if (c.prayerActive[24]) {
			defenceLevel = (int) (defenceLevel + Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.2D);
		} else if (c.prayerActive[25]) {
			defenceLevel = (int) (defenceLevel + Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.25D);
		}
		double mod = 1.0D;
		return (int) ((defenceLevel + defenceLevel * 0.05D + i + i * 0.05D) * mod);
	}

	public static int bestMeleeDef(Player c) {
		return c.playerBonus[5] > c.playerBonus[6] && c.playerBonus[5] > c.playerBonus[7] ? 5 : (c.playerBonus[6] > c.playerBonus[5] && c.playerBonus[6] > c.playerBonus[7] ? 6 : (c.playerBonus[7] > c.playerBonus[5] && c.playerBonus[7] > c.playerBonus[6] ? 7 : 5));
	}

	public static boolean fullDharok(Player mob) {
		return mob.playerEquipment != null && mob.playerEquipment[Player.playerHat] == 4716 && mob.playerEquipment[Player.playerChest] == 4720 && mob.playerEquipment[Player.playerLegs] == 4722 && mob.playerEquipment[Player.playerWeapon] == 4718;
	}

	public static boolean obsidianEffect(Player mob) {
		return mob.playerEquipment != null && mob.playerEquipment[Player.playerWeapon] == 6528 && mob.playerEquipment[Player.playerAmulet] == 11128;

	}

	public static boolean fullVoidMelee(Player mob) {
		return mob.playerEquipment != null && mob.playerEquipment[Player.playerHat] == 11665 && mob.playerEquipment[Player.playerChest] == 8839 && mob.playerEquipment[Player.playerLegs] == 8840 && mob.playerEquipment[Player.playerHands] == 8842;
	}

	public static boolean fullVoidMage(Player mob) {
		return mob.playerEquipment != null && mob.playerEquipment[Player.playerHat] == 11663 && mob.playerEquipment[Player.playerChest] == 8839 && mob.playerEquipment[Player.playerLegs] == 8840 && mob.playerEquipment[Player.playerHands] == 8842;
	}

	public static boolean fullVoidRange(Player mob) {
		return mob.playerEquipment != null && mob.playerEquipment[Player.playerHat] == 11664 && mob.playerEquipment[Player.playerChest] == 8839 && mob.playerEquipment[Player.playerLegs] == 8840 && mob.playerEquipment[Player.playerHands] == 8842;
	}

	public static boolean fullVeracs(Player mob) {
		return mob.playerEquipment != null && mob.playerEquipment[Player.playerHat] == 4753 && mob.playerEquipment[Player.playerWeapon] == 4755 && mob.playerEquipment[Player.playerChest] == 4757 && mob.playerEquipment[Player.playerLegs] == 4759;

	}

	public static boolean fullGuthans(Player mob) {
		return mob.playerEquipment[Player.playerHat] == 4724 && mob.playerEquipment[Player.playerChest] == 4728 && mob.playerEquipment[Player.playerLegs] == 4730 && mob.playerEquipment[Player.playerWeapon] == 4726;
	}

}