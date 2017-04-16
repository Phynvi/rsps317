package com.bclaus.rsps.server.vd.content.combat;

import com.bclaus.rsps.server.vd.player.Player;

public class CombatStyle {

	public static final int ACCURATE = 0, RAPID = 1, AGGRESSIVE = 2, LONGRANGE = 2, BLOCK = 2, DEFENSIVE = 1, CONTROLLED = 3;

	public static void switchCombatType(Player c, int buttonId) {
		switch (buttonId) {
		case 22228: // Punch (unarmed)
			c.fightMode = ACCURATE;
			if (c.autocasting) {
				c.getPA().resetAutoCast();
			}
			break;

		case 22230: // Kick (unarmed)
			c.fightMode = AGGRESSIVE;
			if (c.autocasting) {
				c.getPA().resetAutoCast();
			}
			break;

		case 22229: // Block (unarmed)
			c.fightMode = BLOCK;
			if (c.autocasting) {
				c.getPA().resetAutoCast();
			}
			break;

		case 9125: // Accurate
		case 6221: // range accurate
		case 48010: // flick (whip)
		case 21200: // spike (pickaxe)
		case 1080: // bash (staff)
		case 6168: // chop (axe)
		case 6236: // accurate (long bow)
		case 17102: // accurate (darts)
		case 8234: // stab (dagger)
		case 14128: // pound (Mace)
		case 18077: // Lunge (spear)
		case 18103: // Chop
		case 30088: // Chop (claws)
		case 3014: // Reap (Pickaxe)
		case 1177: // Pound (hammer)
		case 23249: // Bash (battlestaff)
		case 33020: // Jav
			c.fightMode = ACCURATE;
			if (c.autocasting) {
				c.getPA().resetAutoCast();
			}
			break;

		case 9126: // Defensive
		case 48008: // deflect (whip)
		case 21201: // block (pickaxe)
		case 1078: // focus - block (staff)
		case 6169: // block (axe)
		case 33019: // fend (hally)
		case 18078: // block (spear)
		case 8235: // block (dagger)
		case 14219: // block (mace)
		case 18104: // block
		case 30089: // block (claws)
		case 3015: // block
		case 1175: // block (warhammer/hammer)
		case 23247: // block (battlestaff)
		case 33018: // fend (halberd)
			c.fightMode = DEFENSIVE;
			if (c.autocasting) {
				c.getPA().resetAutoCast();
			}
			break;

		case 9127: // Controlled
		case 48009: // lash (whip)
			// case 33018: //jab (hally)
		case 6234: // longrange (long bow)
		case 6219: // longrange
					// case 18077: //lunge (spear)
					// case 18080: //swipe (spear)
					// case 18079: //pound (spear)
		case 17100: // longrange (darts)
		case 6170: // Smash (axe)
		case 14220: // Spike (mace)
		case 18080: // Swipe (spear)
		case 18079: // Pound (spear)
			c.fightMode = CONTROLLED;
			if (c.autocasting) {
				c.getPA().resetAutoCast();
			}
			break;

		case 9128: // Aggressive
		case 6220: // Rapid
		case 21203: // Impale (pickaxe)
		case 21202: // Smash (pickaxe)
		case 1079: // Pound (staff)
		case 6171: // Hack (axe)
					// case 33020: // Swipe
		case 6235: // Rapid
		case 17101: // Rapid
		case 8237: // Lunge
		case 8236: // Slash
		case 14221: // Pummel (mace)
		case 18106: // Slash
		case 18105: // Smash
		case 30091: // Slash (claws)
		case 30090: // Slash (claws)
		case 3017: // Chop (pickaxe)
		case 3016: // Jab (pickaxe)
		case 1176: // Pummel (hammer)
		case 23248: // Pound (battlestaff)
			// case 33019: //Swipe (halberd)
			c.fightMode = AGGRESSIVE;
			if (c.autocasting) {
				c.getPA().resetAutoCast();
			}
			break;
		}
	}
}