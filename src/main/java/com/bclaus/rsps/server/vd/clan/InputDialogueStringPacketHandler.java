package com.bclaus.rsps.server.vd.clan;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

public class InputDialogueStringPacketHandler implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		long value = c.getInStream().readQWord();

		if (value < 0) {
			// prevent invalid packets
			value = 0;
		}

		String stringValue = Misc.longToPlayerName2(value);
		
		if (c.dialogue().input(stringValue)) {
			return;
		}

		String owner = stringValue.replaceAll("_", " ");
		if (owner != null && owner.length() > 0) {
			if (c.clan == null) {
				Clan clan = Server.clanManager.getClan(owner);
				if (clan != null) {
					clan.addMember(c);
				} else if (owner.equalsIgnoreCase(c.playerName)) {
					Server.clanManager.create(c);
				} else {
					c.sendMessage(Misc.formatPlayerName(owner) + " has not created a clan yet.");
				}
				// c.getSkill().refresh(21);
				// c.getSkill().refresh(22);
				// c.getSkill().refresh(23);
			}
		}
	}
}