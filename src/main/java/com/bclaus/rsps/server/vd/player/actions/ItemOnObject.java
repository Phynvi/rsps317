package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.content.skills.impl.hunter.HunterHandler;
import com.bclaus.rsps.server.vd.items.UseItem;
import com.bclaus.rsps.server.vd.objects.Objects;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.vd.world.ObjectHandler;

public class ItemOnObject implements PacketType {

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		c.getInStream().readUnsignedWord();
		c.objectId = c.getInStream().readSignedWordBigEndian();
		c.objectY = c.getInStream().readSignedWordBigEndianA();
		c.getInStream().readUnsignedWord();
		c.objectX = c.getInStream().readSignedWordBigEndianA();
		c.objectDistance = 3;
		c.objectDistance = c.objectDistance < 1 ? 1 : c.objectDistance;
		c.itemUsedOn = c.getInStream().readUnsignedWord();
		c.turnPlayerTo(c.objectX, c.objectY);
		c.cookingCoords[0] = c.objectX;
		c.cookingCoords[1] = c.objectY;
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.isBanking = false;
			c.getBankPin().open(2);
			return;
		}
		if (!c.getItems().playerHasItem(c.itemUsedOn, 1)) {
			return;
		}
		for (Objects o : ObjectHandler.globalObjects) {
			if (o.objectX == c.objectY && o.objectY == c.objectX) {
				if (o != null) {
					HunterHandler.bait(c, c.itemUsedOn, o);
				}
			}
		}
		if (c.destinationReached()) {
			UseItem.ItemonObject(c, c.objectId, c.objectX, c.objectY, c.itemUsedOn);
		} else {
			c.clickObjectType = 4;
		}
	}

}
