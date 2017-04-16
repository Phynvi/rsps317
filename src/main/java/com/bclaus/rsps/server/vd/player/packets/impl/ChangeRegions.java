package com.bclaus.rsps.server.vd.player.packets.impl;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.vd.world.ItemHandler;
import com.bclaus.rsps.server.vd.world.ObjectManager;

/**
 * Change Regions
 */
public class ChangeRegions implements PacketType {
	@Override
	public final void processPacket(Player c, int packetType, int packetSize) {
		ObjectManager.loadObjects(c);
		ItemHandler.reloadItems(c);
		if (c.skullTimer > 0) {
			c.isSkulled = true;
			c.headIconPk = 0;
			c.getPA().requestUpdates();
		}
		c.aggressionTolerance.reset();
	}
}
