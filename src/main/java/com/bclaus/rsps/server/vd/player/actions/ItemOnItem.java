package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.items.UseItem;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

//import server.model.mobile.players.skills.farming.Farming2;

public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int usedWithSlot = c.getInStream().readUnsignedWord();
		int itemUsedSlot = c.getInStream().readUnsignedWordA();
		int useWith = c.playerItems[usedWithSlot] - 1;
		int itemUsed = c.playerItems[itemUsedSlot] - 1;
		if (!c.getItems().playerHasItem(useWith, 1, usedWithSlot) || !c.getItems().playerHasItem(itemUsed, 1, itemUsedSlot) || c.teleporting) {
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.isBanking = false;
			c.getBankPin().open(2);
			return;
		}
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		UseItem.ItemonItem(c, itemUsed, useWith);
	}

}
