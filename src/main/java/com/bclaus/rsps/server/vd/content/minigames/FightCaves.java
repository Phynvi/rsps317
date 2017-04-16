package com.bclaus.rsps.server.vd.content.minigames;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.npc.NPCHandler;

/**
 * FightCaves.java
 * 
 * @author Tim
 * 
 */

public class FightCaves {
	public static final int TZ_KIH = 2627, TZ_KEK_SPAWN = 2738, TZ_KEK = 2630, TOK_XIL = 2631, YT_MEJKOT = 2741, KET_ZEK = 2743, TZTOK_JAD = 2745;

	/**
	 * Holds the data for the 63 waves fight cave.
	 */
	private static final int[][] WAVES = { { TZTOK_JAD } };
	private final static int[][] COORDINATES = { { 2403, 5094 }, { 2390, 5096 }, { 2392, 5077 }, { 2408, 5080 }, { 2413, 5108 }, { 2381, 5106 }, { 2379, 5072 }, { 2420, 5082 } };

	/**
	 * Handles spawning the next fightcave wave.
	 * 
	 * @param player
	 *            The player.
	 */
	public static void spawnNextWave(Player player) {
		if (player != null) {
			if (player.waveId >= WAVES.length) {
				player.waveId = 0;
				return;
			}
			if (player.waveId < 0) {
				player.waveId = 0;
				return;
			}
			int npcAmount = WAVES[player.waveId].length;
			if (player.waveId < 62 && player.waveId > -1) {
				for (int j = 0; j < npcAmount; j++) {
					int npc = WAVES[player.waveId][j];
					int X = COORDINATES[j][0];
					int Y = COORDINATES[j][1];
					int H = player.heightLevel;
					int hp = getHp(npc);
					int max = getMax(npc);
					int atk = getAtk(npc);
					int def = getDef(npc);
					NPCHandler.spawnNpc(player, npc, X, Y, H, 0, hp, max, atk, def, true, false);
				}
			}
		}

	}

	public static int getHp(int npc) {
		switch (npc) {
		case TZ_KIH:
		case TZ_KEK_SPAWN:
			return 10;
		case TZ_KEK:
			return 20;
		case TOK_XIL:
			return 40;
		case YT_MEJKOT:
			return 80;
		case KET_ZEK:
			return 150;
		case TZTOK_JAD:
			return 250;
		}
		return 100;
	}

	public static int getMax(int npc) {
		switch (npc) {
		case TZTOK_JAD:
			return 97;
		}
		return 5;
	}

	public static int getAtk(int npc) {
		switch (npc) {
		case TZTOK_JAD:
			return 650;
		}
		return 100;
	}

	public static int getDef(int npc) {
		switch (npc) {
		case TZTOK_JAD:
			return 500;
		}
		return 100;
	}

}