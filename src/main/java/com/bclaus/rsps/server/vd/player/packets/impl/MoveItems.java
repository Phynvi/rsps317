package com.bclaus.rsps.server.vd.player.packets.impl;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

public class MoveItems implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {

		int interfaceId = c.getInStream().readUnsignedWordBigEndianA();
		boolean insertMode = c.getInStream().readSignedByteC() == 1;
		int from = c.getInStream().readUnsignedWordBigEndianA();
		int to = c.getInStream().readUnsignedWordBigEndian();
		if (c.inTrade || c.tradeStatus == 1 || c.duelStatus == 1 || c.teleporting) {
			c.getTradeAndDuel().declineTrade();
			return;
		}
		c.getItems().moveItems(from, to, interfaceId, insertMode);

	}
}