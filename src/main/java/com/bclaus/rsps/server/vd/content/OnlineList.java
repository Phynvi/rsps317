package com.bclaus.rsps.server.vd.content;

import java.util.ArrayList;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.util.Misc;

public class OnlineList {

	public static void openInterface(Player player) {
		if (player != null) {
			player.sendMessage("Searching for all online players...");
			ArrayList<String> players = new ArrayList<String>();
			for (Player p : World.PLAYERS) {
				if (p != null) {
					players.add("<col=ff0033>" + Constants.rank(p, p.playerRights) + " " + Misc.ucFirst(p.playerName) + " " + (p.getDisplayName() != null ? "(" + p.getDisplayName() + ")" : ""));
				}
			}
			int index = 0;
			for (String s : players) {
				player.getPA().sendFrame126(s, 41001 + index);
				index++;
			}
			for (int i = index; i < 100; i++)
				player.getPA().sendFrame126("", 41001 + i);
			player.getPA().sendFrame126("Online List - " + PlayerUpdating.getPlayerCount() + " Player(s) Online", 40505);
			player.getPA().sendFrame126("", 40506);
			player.getPA().sendFrame126("", 40507);
			player.getPA().sendFrame126("", 40508);
			player.getPA().showInterface(40500);
			player.sendMessage("There are currently " + PlayerUpdating.getPlayerCount() + " players online.");
		}
	}

}
