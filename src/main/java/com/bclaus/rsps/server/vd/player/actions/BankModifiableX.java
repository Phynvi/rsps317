package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

public class BankModifiableX implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int slot = player.getInStream().readUnsignedWordA();
		int component = player.getInStream().readUnsignedWord();
		int item = player.getInStream().readUnsignedWordA();
		int amount = player.getInStream().readDWord();
		if (amount <= 0)
			return;
		switch (component) {
		case 5382:
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().removeItem(item, amount);
				return;
			}
			player.getItems().removeFromBank(item, amount, true);
			break;
		}
	}

}
