package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.items.bank.BankItem;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

public class BankAllButOne implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int interfaceId = player.getInStream().readSignedWordBigEndianA();
		int itemId = player.getInStream().readSignedWordBigEndianA();
		int itemSlot = player.getInStream().readSignedWordBigEndian();
		if (player.getQuarantine().isQuarantined()) {
			player.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		switch (interfaceId) {
		case 5382:
			int amount = player.getBank().getCurrentBankTab().getItemAmount(new BankItem(itemId + 1));
			if (amount < 1)
				return;
			if (amount == 1) {
				player.sendMessage("Your bank only contains one of this item.");
				return;
			}
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().removeItem(itemId, amount - 1);
				return;
			}
			if ((player.getBank().getCurrentBankTab().getItemAmount(new BankItem(itemId + 1)) - 1) > 1)
				player.getItems().removeFromBank(itemId, amount - 1, true);
			break;
		}
	}

}
