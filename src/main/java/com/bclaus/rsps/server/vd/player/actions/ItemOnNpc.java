package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.items.UseItem;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

public class ItemOnNpc implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();
		int i = c.getInStream().readSignedWordA();
		int slot = c.getInStream().readSignedWordBigEndian();
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.isBanking = false;
			c.getBankPin().open(2);
			return;
		}
		NPC npc = NPCHandler.NPCS.get(i);
		if (npc == null) {
			return;
		}
		if (!c.getItems().playerHasItem(itemId, 1, slot)) {
			return;
		}
		int npcId = npc.npcType;
		c.walkingToObject = false;

		UseItem.ItemonNpc(c, itemId, npcId, slot);
		switch (itemId) {

		}
	}
}
