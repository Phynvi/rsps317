package com.bclaus.rsps.server.vd.player.packets.impl;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.util.Misc;

/**
 * Chat
 **/
public class Chat implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.setChatTextEffects(c.getInStream().readUnsignedByteS());
		c.setChatTextColor(c.getInStream().readUnsignedByteS());
		c.setChatTextSize((byte) (c.packetSize - 2));
		c.inStream.readBytes_reverseA(c.getChatText(), c.getChatTextSize(), 0);
		String term = Misc.textUnpack(c.getChatText(), c.packetSize - 2)
				.toLowerCase();
		if (c.isMuted) {
			if (System.currentTimeMillis() - 2000 < c.lastAction) {
				return;
			}
			c.lastAction = System.currentTimeMillis();
			c.sendMessage("Sorry, your account is still muted, please appeal on our forums.");
			// c.sendMessage(""+ c.getMuteReport().getExpireMessage());
			return;
		}
		c.setChatTextUpdateRequired(true);

	}
}
