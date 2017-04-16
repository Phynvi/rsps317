package com.bclaus.rsps.server.vd.content;

import java.util.Map.Entry;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.npc.NPCSlayerDeathTracker.NPCNAMES;

public class SlayerKillCountList {

	public static void openInterface(Player player) {
		int index = 41001;
		player.getPA().showInterface(40500);
		for (int i = index; i < index + 100; i++)
			player.getPA().sendFrame126("", +i);

		player.getPA().sendFrame126("Slayer Killcount List - Total Kills: " + getTotalKills(player), 40505);

		for (Entry<NPCNAMES, Integer> entry : player.getSlayerDeathTracker().getTracker().entrySet()) {
			if (entry == null)
				continue;
			player.getPA().sendFrame126(entry.getKey().format() + " - Kills: " + entry.getValue(), index++);
		}

	}

	public static int getTotalKills(Player player) {
		int total = 0;
		for (Entry<NPCNAMES, Integer> entry : player.getSlayerDeathTracker().getTracker().entrySet()) {
			if (entry == null)
				continue;
			total += entry.getValue();
		}
		return total;
	}
}
