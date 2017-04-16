package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Clicking stuff (interfaces)
 **/
public class ClickingStuff implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		if (c.teleporting) {
			return;
		}
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (c.inTrade) {
			if (!c.acceptedTrade) {
				Player o = World.PLAYERS.get(c.tradeWith);
				o.tradeAccepted = false;
				c.tradeAccepted = false;
				o.tradeStatus = 0;
				c.tradeStatus = 0;
				c.tradeConfirmed = false;
				c.tradeConfirmed2 = false;
				c.sendMessage("Trade has been declined.");
				o.sendMessage("Other player has declined the trade.");
				c.getTradeAndDuel().declineTrade();
			}
		}
		Player o = World.PLAYERS.get(c.duelingWith);
		if (o != null) {
			if (c.duelStatus >= 1 && c.duelStatus <= 4) {
				c.getTradeAndDuel().declineDuel();
				o.getTradeAndDuel().declineDuel();
			}
		}
	}

}
