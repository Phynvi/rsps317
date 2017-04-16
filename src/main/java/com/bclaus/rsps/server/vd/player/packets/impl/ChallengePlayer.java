package com.bclaus.rsps.server.vd.player.packets.impl;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Challenge Player
 **/
public class ChallengePlayer implements PacketType {

	@Override
	public final void processPacket(Player c, int packetType, int packetSize) {
		switch (packetType) {
		case 128:
			int answerPlayer = c.getInStream().readUnsignedWord();
			if (World.PLAYERS.get(answerPlayer) == null || c.arenas() || c.duelStatus == 5 || c.teleporting) {
				return;
			}
			if (c.getQuarantine().isQuarantined()) {
				c.sendMessage("You are quarantined making you unable to do this action.");
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.getBankPin().open(2);
				return;
			}
			c.getTradeAndDuel().requestDuel(answerPlayer);
			break;
		}
	}
}
