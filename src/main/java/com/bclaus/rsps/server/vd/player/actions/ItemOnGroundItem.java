package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

public class ItemOnGroundItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.getInStream().readSignedWord();
		int itemUsed = c.getInStream().readSignedWordA();
		int groundItem = c.getInStream().readUnsignedWord();
		int gItemY = c.getInStream().readSignedWordA();
		int itemUsedSlot = c.getInStream().readSignedWordBigEndianA();
		int gItemX = c.getInStream().readUnsignedWord();
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (!c.getItems().playerHasItem(itemUsed, 1, itemUsedSlot) || !Server.itemHandler.itemExists(groundItem, gItemX, gItemY)) {
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.isBanking = false;
			c.getBankPin().open(2);
			return;
		}
		switch (itemUsed) {

		default:
			if (c.playerRights == 5)
				Misc.println("ItemUsed " + itemUsed + " on Ground Item " + groundItem);
			break;
		}
	}

}
