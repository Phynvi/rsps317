package com.bclaus.rsps.server.vd.content;

import java.util.Map.Entry;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.npc.NPCDeathTracker.NPCName;

public class KillcountList {

	public static void openInterface(Player player) {
		int index = 41001;

		player.getPA().showInterface(40500);

		for (int i = index; i < index + 100; i++)
			player.getPA().sendFrame126("", +i);

		player.getPA().sendFrame126("Boss Killcount List - Total Kills: " + getTotalKills(player), 40505);

		for (Entry<NPCName, Integer> entry : player.getBossDeathTracker().getTracker().entrySet()) {
			if (entry == null)
				continue;
			player.getPA().sendFrame126(entry.getKey().format() + " - Kills: " + entry.getValue(), index++);
		}

	}

	public static int getTotalKills(Player player) {
		int total = 0;
		for (Entry<NPCName, Integer> entry : player.getBossDeathTracker().getTracker().entrySet()) {
			if (entry == null)
				continue;
			total += entry.getValue();
		}
		return total;
	}
}
