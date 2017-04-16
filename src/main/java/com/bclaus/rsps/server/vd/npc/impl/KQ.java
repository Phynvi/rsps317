/**
 * 
 */
package com.bclaus.rsps.server.vd.npc.impl;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.task.ScheduledTask;

/**
 * @author Tim http://rune-server.org/members/Someone
 * 
 */
public class KQ {

	static int[] boss1Coords = { 3475, 9498, 0, 3475, 9498, 0, 3475, 9498, 0 };

	public static void spawnSecondForm(final int i) {
		Server.getTaskScheduler().schedule(new ScheduledTask(20) {
			@Override
			public void execute() {
				for (int i = 0; i < NPCHandler.NPCS.capacity(); i++) {
					if (NPCHandler.NPCS.get(i) == null)
						continue;
					if (NPCHandler.NPCS.get(i).npcType == 1160) {
						NPCHandler.NPCS.remove(NPCHandler.NPCS.get(i));
					}
				}
				Server.npcHandler.spawnNpc2(1160, boss1Coords[0], boss1Coords[1], 0, 0, 140, 45, 400, 300);
				this.stop();
			}

		});
	}

	public static void killedBarrow(int i) {
		Player c = World.PLAYERS.get(NPCHandler.NPCS.get(i).killedBy);
		if (c != null) {
			for (int o = 0; o < c.barrowsNpcs.length; o++) {
				if (NPCHandler.NPCS.get(i).npcType == c.barrowsNpcs[o][0]) {
					c.barrowsNpcs[o][1] = 2; // 2 for dead
					c.barrowsKillCount++;
				}
			}
		}
	}

	private static int[][] barrowCrypt = { { 4921, 0 }, { 2035, 0 } };

	public static void killedCrypt(int i) {
		Player c = World.PLAYERS.get(NPCHandler.NPCS.get(i).killedBy);
		if (c != null) {
			for (int o = 0; o < barrowCrypt.length; o++) {
				if (NPCHandler.NPCS.get(i).npcType == barrowCrypt[o][0]) {
					c.barrowsKillCount++;
					c.getPA().sendFrame126("" + c.barrowsKillCount, 16137);
				}
			}
		}
	}

	public static void appendAssaultCount(int i) {
		Player c = World.PLAYERS.get(NPCHandler.NPCS.get(i).killedBy);
		if (c != null) {
			int[] assaultMonsters = { 253, 258 };
			for (int j : assaultMonsters) {
				if (NPCHandler.NPCS.get(i).npcType == 258) {
					c.assaultPoints += 2;
				}
				if (NPCHandler.NPCS.get(i).npcType == j) {
					c.assaultPoints++;
					c.sendMessage("Assault Points: " + c.assaultPoints);
				}
			}
		}
	}

	public static void appendMageCount(int i) {
		Player c = World.PLAYERS.get(NPCHandler.NPCS.get(i).killedBy);
		if (c != null) {
			int[] assaultMonsters = { 912, 913, 914 };
			for (int j : assaultMonsters) {
				if (NPCHandler.NPCS.get(i).npcType == j) {
					c.magePoints += 3;
					c.sendMessage("Mage Points : " + c.magePoints);
				}
			}
		}
	}

	public static void appendKillCount(int i) {
		Player c = World.PLAYERS.get(NPCHandler.NPCS.get(i).killedBy);
		if (c != null) {
			for (int j = 6267; j < 6274; j++) {
				if (NPCHandler.NPCS.get(i).npcType == j) {
					if (c.killCount < 10) {
						c.killCount++;
						c.sendMessage("Killcount: " + c.killCount);
					} else {
						c.sendMessage("You already have 10 kill count");
					}
					break;
				}
			}
		}
	}


}
