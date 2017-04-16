package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.content.ResourceBag;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Bank X Items
 **/
public class BankX1 implements PacketType {

	public static final int PART1 = 135;
	public static final int PART2 = 208;
	public int XremoveSlot, XinterfaceID, XremoveID, Xamount;

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		
		if (packetType == 135) {
			c.xRemoveSlot = c.getInStream().readSignedWordBigEndian();
			c.xInterfaceId = c.getInStream().readUnsignedWordA();
			c.xRemoveId = c.getInStream().readSignedWordBigEndian();
		}
		if(c.xInterfaceId >= 44003 && c.xInterfaceId <= 44030) {
			ResourceBag.removetoBank(c, c.xRemoveId, 100);
			return;
		}
		if (c.xInterfaceId == 3900) {
			c.getShops().buyItem(c.xRemoveId, c.xRemoveSlot, 1000);// buy 1000
			c.xRemoveSlot = 0;
			c.xInterfaceId = 0;
			c.xRemoveId = 0;
			return;
		}
		if (c.isUsingBag) {
		    c.getPA().storeBagItems(c.tempIdHolder, Xamount);
		}
		if (c.xInterfaceId == 3823) {
			c.getShops().sellItem(c.xRemoveId, c.xRemoveSlot, 1000);// buy 1000
			c.xRemoveSlot = 0;
			c.xInterfaceId = 0;
			c.xRemoveId = 0;
			return;
		}
		if (packetType == PART1) {
			c.getOutStream().createFrame(27);
		}

	}
}
