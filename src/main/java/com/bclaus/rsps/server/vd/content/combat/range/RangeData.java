package com.bclaus.rsps.server.vd.content.combat.range;

import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;

public class RangeData {

	public static void fireProjectileNpc(Player c) {
		if (c.oldNpcIndex > 0) {
			if (NPCHandler.NPCS.get(c.oldNpcIndex) != null) {
				c.projectileStage = 2;
				int pX = c.getX();
				int pY = c.getY();
				int nX = NPCHandler.NPCS.get(c.oldNpcIndex).getX();
				int nY = NPCHandler.NPCS.get(c.oldNpcIndex).getY();
				int offX = (pY - nY) * -1;
				int offY = (pX - nX) * -1;
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, c.getCombat().getProjectileSpeed(), c.getCombat().getRangeProjectileGFX(), 43, 31, c.oldNpcIndex + 1, c.getCombat().getProjectileShowDelay());
				if (c.getCombat().usingDbow())
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, c.getCombat().getProjectileSpeed(), c.getCombat().getRangeProjectileGFX(), 60, 31, c.oldNpcIndex + 1, c.getCombat().getStartDelay(), 35);
			}
		}
	}

	public static void fireProjectilePlayer(Player c) {
		if (c.oldPlayerIndex > 0) {
			if (World.PLAYERS.get(c.oldPlayerIndex) != null) {
				c.projectileStage = 2;
				int pX = c.getX();
				int pY = c.getY();
				int oX = World.PLAYERS.get(c.oldPlayerIndex).getX();
				int oY = World.PLAYERS.get(c.oldPlayerIndex).getY();
				int offX = (pY - oY) * -1;
				int offY = (pX - oX) * -1;
				if (!c.msbSpec)
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, c.getCombat().getProjectileSpeed(), c.getCombat().getRangeProjectileGFX(), 43, 31, -c.oldPlayerIndex - 1, c.getCombat().getStartDelay());
				else if (c.msbSpec) {
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, c.getCombat().getProjectileSpeed(), c.getCombat().getRangeProjectileGFX(), 43, 31, -c.oldPlayerIndex - 1, c.getCombat().getStartDelay(), 10);
					c.msbSpec = false;
				}
				if (c.getCombat().usingDbow())
					c.getPA().createProjectile3(pY, pX, offY, offX, c.getCombat().getRangeProjectileGFX(), 53, 31, 100, -c.oldPlayerIndex - 1);

			}
		}
	}

	public static int getRangeStr(int i) {
		int str = 0;
		if(i == 4214 || i == 4215 || i == 4216 || i == 4217 || i == 4218 || i == 4219 || i == 4220 || i == 4221 || i == 4222 || i == 4223){
			return 85;
		}
		int[][] data = { { 877, 10 }, { 9140, 46 }, { 9145, 36 }, { 9141, 64 }, { 9142, 82 }, { 9143, 100 }, { 9144, 115 }, { 9236, 14 }, { 9237, 30 }, { 9238, 48 }, { 9239, 66 }, { 9240, 83 }, { 9241, 85 }, { 9242, 103 }, { 9243, 105 }, { 9244, 117 }, { 9245, 120 }, { 882, 2 }, { 884, 4 }, { 886, 8 }, { 888, 12 }, { 890, 16 }, { 892, 22 }, {78, 49}, { 4740, 55 }, { 11212, 60 }, { 806, 1 }, { 807, 3 }, { 808, 4 }, { 809, 7 }, { 810, 10 }, { 811, 14 }, { 11230, 20 }, { 864, 3 }, { 863, 4 }, { 865, 7 }, { 866, 19 }, { 867, 12 }, { 868, 14 }, { 14614, 26} , { 825, 6 }, { 826, 10 }, { 827, 12 }, { 828, 18 }, { 829, 28 }, { 830, 42 }, { 800, 5 }, { 801, 7 }, { 802, 11 }, { 803, 16 }, { 804, 23 }, { 805, 36 }, { 9976, 0 }, { 9977, 15 }, { 6522, 49 }, { 10034, 15 }, { 9341, 90 }, };
		for (int l = 0; l < data.length; l++) {
			if (i == data[l][0]) {
				str = data[l][1];
			}
		}
		return str;
	}
	
	public static int getCrystalStrength(int i) {
		if(i == 4212 || i == 4214 || i == 4215 || i == 4216 || i == 4217 || i == 4218 || i == 4219 || i == 4220 || i == 4221 || i == 4222 || i == 4223){
			return 85;
		}
		return 0;
	}
	public static boolean hasCrystalBow(Player player) {
		switch(player.playerEquipment[Player.playerWeapon]) {
		case 4212:
		case 4215:
		case 4216:
		case 4217:
		case 4218:
		case 4219:
		case 4220:
		case 4221:
		case 4222:
		case 4223:
			return true;
		}
		return false;
	}

	public static int getRangeStartGFX(Player c) {
		int str = -1;
		int[][] data = {
				// KNIFES
				{ 863, 220 }, { 864, 219 }, { 865, 221 }, { 866, 223 }, { 867, 224 }, { 868, 225 }, { 869, 222 },

				// DARTS
				{ 806, 1234 }, { 807, 1235 }, { 808, 1236 }, { 809, 1238 }, { 810, 1239 }, { 811, 1240 }, { 11230, 1242 },

				// JAVELIN
				{ 825, 206 }, { 826, 207 }, { 827, 208 }, { 828, 209 }, { 829, 210 }, { 830, 211 },

				// AXES
				{ 800, 42 }, { 801, 43 }, { 802, 44 }, { 803, 45 }, { 804, 46 }, { 805, 48 },

				// ARROWS
				{ 882, 19 }, { 884, 18 }, { 886, 20 }, { 888, 21 }, { 890, 22 }, { 892, 24 }, {78, 25},

				// CRYSTAL_BOW
				{ 4212, 250 }, { 4214, 250 }, { 4215, 250 }, { 4216, 250 }, { 4217, 250 }, { 4218, 250 }, { 4219, 250 }, { 4220, 250 }, { 4221, 250 }, { 4222, 250 }, { 4223, 250 }, };
		for (int l = 0; l < data.length; l++) {
			if (c.rangeItemUsed == data[l][0]) {
				str = data[l][1];
			}
		}
		if (c.playerEquipment[3] == 11235) {
			int[][] moreD = { { 882, 1104 }, { 884, 1105 }, { 886, 1106 }, { 888, 1107 }, { 890, 1108 }, { 892, 1109 }, { 11212, 1111 }, {78, 1114}, };
			for (int l = 0; l < moreD.length; l++) {
				if (c.playerEquipment[Player.playerArrows] == moreD[l][0]) {
					str = moreD[l][1];
				}
			}
		}
		
		return str;
	}

	public static int getRangeProjectileGFX(Player c) {
		if (c.dbowSpec) {
			return c.playerEquipment[Player.playerArrows] == 11212 ? 1099 : 1101;
		}
		if (c.bowSpecShot > 0) {
			switch (c.rangeItemUsed) {
			default:
				return 249;
			}
		}
		boolean castingMagic = (c.usingMagic || c.mageFollow || c.autocasting || c.spellId > 0);
		if (castingMagic) {
			return -1;
		}
		if (c.playerEquipment[Player.playerWeapon] == 9185 || c.playerEquipment[Player.playerWeapon] == 12349)
			return 27;
		int str = -1;
		int[][] data = {
				// KNIFES
				{ 863, 213 }, { 864, 212 }, { 865, 214 }, { 866, 216 }, { 867, 217 }, { 868, 218 }, { 869, 215 },

				// DARTS
				{ 806, 226 }, { 807, 227 }, { 808, 228 }, { 809, 229 }, { 810, 230 }, { 811, 231 },

				// JAVELINS
				{ 825, 200 }, { 826, 201 }, { 827, 202 }, { 828, 203 }, { 829, 204 }, { 830, 205 },

				// AXES
				{ 800, 36 }, { 801, 35 }, { 802, 37 }, { 803, 38 }, { 804, 39 }, { 805, 40 },

				// ARROWS
				{ 882, 10 }, { 884, 9 }, { 886, 11 }, { 888, 12 }, { 890, 13 }, { 892, 15 }, {78, 16}, { 11212, 1120 },
				// CHINCHOMPA
				{ 10033, 908 }, { 10034, 909 },

				// OTHERS
				{ 6522, 442 }, {14614, 226}, { 4740, 27 }, { 4212, 249 }, { 4214, 249 }, { 4215, 249 }, { 4216, 249 }, { 4217, 249 }, { 4218, 249 }, { 4219, 249 }, { 4220, 249 }, { 4221, 249 }, { 4222, 249 }, { 4223, 249 }, };
		for (int l = 0; l < data.length; l++) {
			if (c.rangeItemUsed == data[l][0]) {
				str = data[l][1];
			}
		}
		return str;
	}

	public static int getRangeEndGFX(Player c) {
		int str = -1;
		int gfx = 0;
		int[][] data = { { 10033, 157, 100 }, { 10034, 157, 100 }, };
		for (int l = 0; l < data.length; l++) {
			if (c.playerEquipment[Player.playerWeapon] == data[l][0]) {
				str = data[l][1];
				gfx = data[l][2];
			}
		}
		if (gfx == 100) {
			c.rangeEndGFXHeight = true;
		}
		return str;
	}

	public static int correctBowAndArrows(Player c) {
		if (c.getCombat().properBolts())
			return -1;
		switch (c.playerEquipment[Player.playerWeapon]) {

		case 839:
		case 841:
			return 882;

		case 843:
		case 845:
			return 884;

		case 847:
		case 849:
			return 886;

		case 851:
		case 853:
			return 888;

		case 855:
		case 857:
			return 890;
		case 6724:
			return 78;
		case 859:
		case 861:
			return 892;

		case 4734:
		case 4935:
		case 4936:
		case 4937:
			return 4740;

		case 11235:
			return 11212;
		}
		return -1;
	}

	public static int getProjectileSpeed(Player c) {
		if (c.dbowSpec)
			return 100;
		switch (c.playerEquipment[3]) {
		case 10033:
		case 10034:
			return 60;
		}
		return 70;
	}

	public static int getProjectileShowDelay(Player c) {
		int[] data = { 806, 806, 808, 809, 810, 811, 10033, 10034, 11230, };
		int str = 53;
		for (int i = 0; i < data.length; i++) {
			if (c.playerEquipment[Player.playerWeapon] == data[i]) {
				str = 32;
			}
		}
		return str;
	}
}