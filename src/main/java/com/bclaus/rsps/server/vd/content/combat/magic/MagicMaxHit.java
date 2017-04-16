package com.bclaus.rsps.server.vd.content.combat.magic;

import com.bclaus.rsps.server.vd.player.Player;

public class MagicMaxHit {

	public static int mageAttack(Player c) {
		int attackLevel = c.playerLevel[6];

		if (c.prayerActive[4]) {
			attackLevel = (int) (attackLevel * 1.05D);
		} else if (c.prayerActive[12]) {
			attackLevel = (int) (attackLevel * 1.1D);
		} else if (c.prayerActive[20]) {
			attackLevel = (int) (attackLevel * 1.15D);
		}

		return (int) (attackLevel + c.playerBonus[3] * 2.25D);
	}

	public static int mageDef(Player c) {
		int defenceLevel = c.playerLevel[1] / 2 + c.playerLevel[6] / 2;
		if (c.prayerActive[4]) {
			defenceLevel = (int) (defenceLevel + Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.05D);
		} else if (c.prayerActive[12]) {
			defenceLevel = (int) (defenceLevel + Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.1D);
		} else if (c.prayerActive[20]) {
			defenceLevel = (int) (defenceLevel + Player.getLevelForXP(c.playerXP[Player.playerDefence]) * 0.15D);
		}
		return defenceLevel + c.playerBonus[8] + c.playerBonus[8] / 4;
	}

	public static int magiMaxHit(Player c) {
		double damage = Player.MAGIC_SPELLS[c.oldSpellId][6];
		double damageMultiplier = 1;
		if (c.playerLevel[Player.playerMagic] > 99) {
			damageMultiplier += .2;
		}


		switch (c.playerEquipment[Player.playerWeapon]) {
		case 4675:
		case 6914:
			damageMultiplier += .05;
			break;
		case 12346:
			damageMultiplier += .9;
			break;
		}
		
		
		
		damage *= damageMultiplier;
		
		//equations for higher end weapons
		if (c.playerEquipment[Player.playerWeapon] == 12345 || c.playerEquipment[Player.playerWeapon] == 12346) {
			damage = 20;
			int difference = c.playerLevel[6] - 75;
			if (difference > 0)
				damage += (difference / 3);
		}
		return (int) damage;
	}
}
