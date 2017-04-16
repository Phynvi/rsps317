package com.bclaus.rsps.server.vd.player.packets.impl;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.content.ResourceBag;
import com.bclaus.rsps.server.vd.content.skills.impl.Smithing;
import com.bclaus.rsps.server.vd.content.skills.impl.crafting.JewelleryMaking;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Remove Item
 */
public class RemoveItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readUnsignedWordA();
		int removeSlot = c.getInStream().readUnsignedWordA();
		int removeId = c.getInStream().readUnsignedWordA();
		/*
		 * if (c.playerItems[removeSlot] - 1 != removeId ||
		 * c.playerItemsN[removeSlot] <= 0) { return; }
		 */
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		if (interfaceId >= 44003 && interfaceId <= 44030) {
			ResourceBag.remove(c, removeId, 1);
			return;
		}
		switch (interfaceId) {

		case 44054:
			ResourceBag.add(c, removeId, 1);
			break;
		case 4233:
			JewelleryMaking.jewelryMaking(c, "RING", removeId, 1);
			break;
		case 4239:
			JewelleryMaking.jewelryMaking(c, "NECKLACE", removeId, 1);
			break;
		case 4245:
			JewelleryMaking.jewelryMaking(c, "AMULET", removeId, 1);
			break;
		case 1688:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
				return;
			}
			c.getItems().removeItem(removeSlot);
			break;

		case 2274:
			if (c.inTrade) {
				return;
			}
			break;

		case 5064:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
				return;
			}
			if (c.isBanking) {
				c.getItems().addToBank(removeId, 1, true);
			}
			break;
		case 5382:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().removeItem(removeId, 1);
				return;
			}
			c.getItems().removeFromBank(removeId, 1, true);
			break;

		case 3900:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
				return;
			}
			c.getShops().buyFromShopPrice(removeId);
			break;

		case 3823:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
				return;
			}
			c.getShops().sellToShopPrice(removeId);
			break;

		case 3322:
			if (!c.canOffer) {
				return;
			}
			if (c.duelStatus <= 0) {
				c.getTradeAndDuel().tradeItem(removeId, removeSlot, 1);
			} else {
				c.getTradeAndDuel().stakeItem(removeId, removeSlot, 1);
			}
			break;

		case 3415:
			if (!c.canOffer) {
				return;
			}
			if (c.duelStatus <= 0) {
				c.getTradeAndDuel().fromTrade(removeId, 1);
			}
			break;

		case 6669:
			c.getTradeAndDuel().fromDuel(removeId, removeSlot, 1);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			Smithing.readInput(c, c.playerLevel[Player.playerSmithing], Integer.toString(removeId), 1);

			break;
		}
	}

}
