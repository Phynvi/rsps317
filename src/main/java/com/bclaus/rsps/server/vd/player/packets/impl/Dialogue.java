package com.bclaus.rsps.server.vd.player.packets.impl;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Dialogue
 **/
public class Dialogue implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		/*if (c.dialogue().isActive()) {
			if (c.dialogue().next()) {
				return;
			}*/
		//}

		if (c.nextChat > 0) {
			if (c.getQuarantine().isQuarantined()) {
				c.sendMessage("You are quarantined making you unable to do this action.");
				return;
			}
			c.getDH().sendDialogues(c.nextChat, c.talkingNpc);
		} else {
			c.getDH().sendDialogues(0, -1);
		}

	}

}
