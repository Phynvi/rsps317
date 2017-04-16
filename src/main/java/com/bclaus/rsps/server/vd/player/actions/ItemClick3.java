package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.content.HandleEmpty;
import com.bclaus.rsps.server.vd.content.consumables.Potions;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.util.Misc;

/**
 * Item Click 3 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick3 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId11 = c.getInStream().readSignedWordBigEndianA();
		int itemId1 = c.getInStream().readSignedWordA();
		int itemId = c.getInStream().readSignedWordA();
		if (!c.getItems().playerHasItem(itemId))
			return;
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.isBanking = false;
			c.getBankPin().open(2);
			return;
		}
		if (Potions.potionNames(c, itemId)) {
			if (c.getItems().playerHasItem(itemId))
				HandleEmpty.handleEmptyItem(c, itemId, 229);
		}
		if (HandleEmpty.canEmpty(itemId)) {
			HandleEmpty.handleEmptyItem(c, itemId, HandleEmpty.filledToEmpty(itemId));
			return;
		}
		switch (itemId) {
		case 4079:
			c.startAnimation(1460);
			break;
		case 13220:
			c.getItems().deleteItem(13220, 1);
			c.getItems().addItemToBank(536, 250);
			break;
		case 14614:
			if (c.dartsLoaded > 0) {
			c.sendMessage("You unload <col=ff0000>" + c.dartsLoaded + "</col> darts from your blowpipe.");
			c.getItems().addItem(811, c.dartsLoaded);
			c.dartsLoaded = 0;
			} else if (c.dartsLoaded == 0) {
			c.sendMessage("You have no darts left to unload!");
			c.dartsLoaded = 0;
			}
			/*if (c.runedartsLoaded > 0) {
			c.sendMessage("You unload <col=ff0000>" + c.runedartsLoaded + "</col> darts from your blowpipe.");
			c.getItems().addItem(811, c.runedartsLoaded);
			c.runedartsLoaded = 0;
			} else if (c.runedartsLoaded == 0) {		
			c.sendMessage("You have no darts left to unload!");
			c.runedartsLoaded = 0;
			}*/
			break;
		case 5733:
			c.getDH().sendDialogues(1656, 0);
			break;
		default:
			if (c.playerRights == 5)
				Misc.println(c.playerName + " - Item3rdOption: " + itemId + " : " + itemId11 + " : " + itemId1);
			break;

		}

	}

}
