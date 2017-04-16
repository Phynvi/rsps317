package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.content.ResourceBag;
import com.bclaus.rsps.server.vd.items.GameItem;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.items.bank.BankItem;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Bank All Items
 */
public class BankAll implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int removeSlot = c.getInStream().readUnsignedWordA();
		int interfaceId = c.getInStream().readUnsignedWord();
		int removeId = c.getInStream().readUnsignedWordA();
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (interfaceId >= 44003 && interfaceId <= 44030) {
			ResourceBag.remove(c, removeId, 100);
			return;
		}	
		switch (interfaceId) {
		case 44054:
			if(c.getItems().getItemAmount(removeId) > 100) {
				ResourceBag.add(c, removeId, 100);
				return;
			}
			ResourceBag.add(c, removeId, c.getItems().getItemAmount(removeId));
			break;
		case 3900:
			c.getShops().buyItem(removeId, removeSlot, 10);
			break;
		case 3823:
			if (c.inTrade) {
				return;
			}
			c.getShops().sellItem(removeId, removeSlot, 10);
			break;

		case 5064:
			if (c.inTrade) {
				return;
			}
			if (c.isBanking) {
				c.getItems().addToBank(removeId, c.getItems().getItemAmount(removeId), true);
			}
			break;

		case 5382:
			if (!c.isBanking) {
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().removeItem(removeId, c.getBank().getCurrentBankTab().getItemAmount(new BankItem(removeId + 1)));
				return;
			}
			c.getItems().removeFromBank(removeId, c.getBank().getCurrentBankTab().getItemAmount(new BankItem(removeId + 1)), true);
			break;

		case 3322:
			if (c.duelStatus <= 0) {
				if (Item.itemStackable[removeId]) {
					c.getTradeAndDuel().tradeItem(removeId, removeSlot, c.playerItemsN[removeSlot]);
				} else {
					c.getTradeAndDuel().tradeItem(removeId, removeSlot, 28);
				}
			} else {
				if (Item.itemStackable[removeId] || Item.itemIsNote[removeId]) {
					c.getTradeAndDuel().stakeItem(removeId, removeSlot, c.playerItemsN[removeSlot]);
				} else {
					c.getTradeAndDuel().stakeItem(removeId, removeSlot, 28);
				}
			}
			break;

		case 3415:
			if (c.duelStatus <= 0) {
				if (Item.itemStackable[removeId]) {
					for (GameItem item : c.getTradeAndDuel().offeredItems) {
						if (item.id == removeId) {
							c.getTradeAndDuel().fromTrade(removeId, c.getTradeAndDuel().offeredItems.get(removeSlot).amount);
						}
					}
				} else {
					for (GameItem item : c.getTradeAndDuel().offeredItems) {
						if (item.id == removeId) {
							c.getTradeAndDuel().fromTrade(removeId, 28);
						}
					}
				}
			}
			break;

		case 6669:
			if (Item.itemStackable[removeId] || Item.itemIsNote[removeId]) {
				for (GameItem item : c.getTradeAndDuel().stakedItems) {
					if (item.id == removeId) {
						c.getTradeAndDuel().fromDuel(removeId, removeSlot, c.getTradeAndDuel().stakedItems.get(removeSlot).amount);
					}
				}

			} else {
				c.getTradeAndDuel().fromDuel(removeId, removeSlot, 28);
			}
			break;

		}
	}

}
