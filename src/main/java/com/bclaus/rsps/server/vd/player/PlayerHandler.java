package com.bclaus.rsps.server.vd.player;

import com.bclaus.rsps.server.vd.World;

public class PlayerHandler {

	public static final Player[] players = null;
	public static final Player getPlayerCount = null;
	public static Object PLAYERS;

	public static int getPlayerID(String otherPName) {
		// TODO Auto-generated method stub
		return 0;
	}
	public static Player getPlayer(String name) {
		for (int i = 0; i < World.PLAYERS.capacity(); i++) {
			if (World.PLAYERS.get(i) != null
					&& World.PLAYERS.get(i).playerName.equalsIgnoreCase(name)) {
				return World.PLAYERS.get(i);
			}
		}

		return null;
	}
	





}
