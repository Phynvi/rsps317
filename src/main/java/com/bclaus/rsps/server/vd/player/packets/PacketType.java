package com.bclaus.rsps.server.vd.player.packets;

import com.bclaus.rsps.server.vd.player.Player;

public interface PacketType {
	public void processPacket(Player c, int packetType, int packetSize);
}
