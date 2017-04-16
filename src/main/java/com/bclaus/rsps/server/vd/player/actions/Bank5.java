package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.content.ResourceBag;
import com.bclaus.rsps.server.vd.content.skills.impl.Smithing;
import com.bclaus.rsps.server.vd.content.skills.impl.crafting.JewelleryMaking;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Bank 5 Items
 */
public class Bank5 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readSignedWordBigEndianA();
		int removeId = c.getInStream().readSignedWordBigEndianA();
		int removeSlot = c.getInStream().readSignedWordBigEndian();
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (interfaceId >= 44003 && interfaceId <= 44030) {
			ResourceBag.remove(c, removeId, 5);
			return;
		}
		switch (interfaceId) {
		case 44054:
			ResourceBag.add(c, removeId, 5);
			break;
		case 4233:
			JewelleryMaking.jewelryMaking(c, "RING", removeId, 5);
			break;
		case 4239:
			JewelleryMaking.jewelryMaking(c, "NECKLACE", removeId, 5);
			break;
		case 4245:
			JewelleryMaking.jewelryMaking(c, "AMULET", removeId, 5);
			break;
		case 1688:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
			}
			c.getPA().useOperate(removeId);
			break;
		case 3900:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
			}
			c.getShops().buyItem(removeId, removeSlot, 1);
			break;

		case 3823:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
			}
			c.getShops().sellItem(removeId, removeSlot, 1);
			break;

		case 5064:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
				c.sendMessage("You can't store items while trading!");
				return;
			}
			if (c.isBanking) {
				c.getItems().addToBank(removeId, 5, true);
			}
			break;
		case 5382:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().removeItem(removeId, 5);
				return;
			}
			c.getItems().removeFromBank(removeId, 5, true);
			break;

		case 3322:
			if (c.duelStatus <= 0) {
				c.getTradeAndDuel().tradeItem(removeId, removeSlot, 5);
			} else {
				c.getTradeAndDuel().stakeItem(removeId, removeSlot, 5);
			}
			break;

		case 3415:
			if (c.duelStatus <= 0) {
				c.getTradeAndDuel().fromTrade(removeId, 5);
			}
			break;

		case 6669:
			c.getTradeAndDuel().fromDuel(removeId, removeSlot, 5);
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
