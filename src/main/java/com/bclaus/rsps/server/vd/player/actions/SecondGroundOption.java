package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

public class SecondGroundOption implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.getInStream().readSignedWordBigEndian();
		c.getInStream().readUnsignedWord();
		c.getInStream().readSignedWordBigEndian();
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.isBanking = false;
			c.getBankPin().open(2);
			return;
		}
	}
}
