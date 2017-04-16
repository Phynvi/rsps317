package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Bank X Items
 **/
public class InputDialogueIntegerPacketHandler implements PacketType {
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int amount = c.getInStream().readDWord();
		if (amount <= 0) {
			amount = 0;
		}
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		
		if (c.dialogue().isActive()) {
			if (c.dialogue().input(amount)) {
				return;
			}
		}
		switch (c.xInterfaceId) {

		case 5064:
			c.getItems().addToBank(c.playerItems[c.xRemoveSlot] - 1, amount, true);
			break;
		case 5382:
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().removeItem(c.getBank().getCurrentBankTab().getItem(c.xRemoveSlot).getId() - 1, amount);
				return;
			}
			c.getItems().removeFromBank(c.getBank().getCurrentBankTab().getItem(c.xRemoveSlot).getId() - 1, amount, true);
			break;
		case 3322:
			if (c.duelStatus <= 0) {
				c.getTradeAndDuel().tradeItem(c.xRemoveId, c.xRemoveSlot, amount > c.getItems().getItemAmount(c.xRemoveId) ? c.getItems().getItemAmount(c.xRemoveId) : amount);
			} else {
				c.getTradeAndDuel().stakeItem(c.xRemoveId, c.xRemoveSlot, amount > c.getItems().getItemAmount(c.xRemoveId) ? c.getItems().getItemAmount(c.xRemoveId) : amount);
			}
			break;

		case 3415:
			if (c.duelStatus <= 0) {
				c.getTradeAndDuel().fromTrade(c.xRemoveId, c.xRemoveSlot, amount > c.getItems().getItemAmount(c.xRemoveId) ? c.getItems().getItemAmount(c.xRemoveId) : amount);
			}
			break;

		case 6669:
			c.getTradeAndDuel().fromDuel(c.xRemoveId, c.xRemoveSlot, amount > c.getItems().getItemAmount(c.xRemoveId) ? c.getItems().getItemAmount(c.xRemoveId) : amount);
			break;
		}
	}
}