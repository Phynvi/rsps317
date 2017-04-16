package com.bclaus.rsps.server.vd.player.packets.impl;

import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

public class FollowPlayer implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int followPlayer = c.getInStream().readUnsignedWordBigEndian();
		if (World.PLAYERS.get(followPlayer) == null) {
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
		c.walkingToObject = false;
		c.playerIndex = 0;
		c.npcIndex = 0;
		c.mageFollow = false;
		c.usingBow = false;
		c.usingRangeWeapon = false;
		c.followDistance = 1;
		c.followId = followPlayer;
	}
}