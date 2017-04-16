package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.content.ResourceBag;
import com.bclaus.rsps.server.vd.content.skills.impl.Smithing;
import com.bclaus.rsps.server.vd.content.skills.impl.crafting.JewelleryMaking;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Bank 10 Items
 */
public class Bank10 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readUnsignedWordBigEndian();
		int removeId = c.getInStream().readUnsignedWordA();
		int removeSlot = c.getInStream().readUnsignedWordA();
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (interfaceId >= 44003 && interfaceId <= 44030) {
			ResourceBag.remove(c, removeId, 10);
			return;
		}
		switch (interfaceId) {
		case 44054:
			ResourceBag.add(c, removeId, 10);
			break;
		case 4233:
			JewelleryMaking.jewelryMaking(c, "RING", removeId, 10);
			break;
		case 4239:
			JewelleryMaking.jewelryMaking(c, "NECKLACE", removeId, 10);
			break;
		case 4245:
			JewelleryMaking.jewelryMaking(c, "AMULET", removeId, 10);
			break;
		case 3900:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
			}
			c.getShops().buyItem(removeId, removeSlot, 5);
			break;
		case 3823:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
			}
			c.getShops().sellItem(removeId, removeSlot, 5);
			break;

		case 5064:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
				c.sendMessage("You can't store items while trading!");
				return;
			}
			if (c.isBanking) {

				c.getItems().addToBank(removeId, 10, true);
			}
			break;

		case 5382:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().removeItem(removeId, 10);
				return;
			}
			c.getItems().removeFromBank(removeId, 10, true);
			break;

		case 7423:
			if (c.storing) {
				return;
			}
			c.getItems().addToBank(removeId, 10, true);
			c.getItems().resetItems(7423);
			break;

		case 3322:
			if (c.duelStatus <= 0) {
				c.getTradeAndDuel().tradeItem(removeId, removeSlot, 10);
			} else {
				c.getTradeAndDuel().stakeItem(removeId, removeSlot, 10);
			}
			break;

		case 3415:
			if (c.duelStatus <= 0) {
				c.getTradeAndDuel().fromTrade(removeId, 10);
			}
			break;

		case 6669:
			c.getTradeAndDuel().fromDuel(removeId, removeSlot, 10);
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			Smithing.readInput(c, c.playerLevel[Player.playerSmithing], Integer.toString(removeId), 5);
			break;
		}
	}

}
