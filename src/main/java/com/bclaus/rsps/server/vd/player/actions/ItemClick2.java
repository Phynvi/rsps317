package com.bclaus.rsps.server.vd.player.actions;


import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.content.skills.impl.slayer.SocialSlayerData;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Item Click 2 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick2 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();
		if (!c.getItems().playerHasItem(itemId))
			return;
	//	Degrade.checkCharges(c, itemId);
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.isBanking = false;
			c.getBankPin().open(2);
			return;
		}
		switch (itemId) {
		case 4155:
		case 13281:
			SocialSlayerData.obtainSlayerInformation(c);
			break;
		case 6830:
			c.getDH().sendDialogues(1505, 932);
			break;
		case 6831:
			c.getDH().sendDialogues(1507, 932);
			break;
		case 6828:
			c.getDH().sendDialogues(1509, 932);
			break;
		case 6833:
			c.getItems().deleteItem(6833, 1);
			c.isExtremeDonator = true;
			c.isDonator = true;
			c.donatorRights = 4;
			c.playerRights = 10;
			c.sendMessage("You claim your donator status.");
			break;
		case 6722:
			c.forcedChat("Alas!");
			c.startAnimation(2840);
			break;
		case 4079:
			c.startAnimation(1459);
			break;
		case 11700:
		case 11694:
		case 11696:
		case 11698:
			if (c.getItems().freeSlots() > 1) {
				if (c.getItems().playerHasItem(itemId, 1)) {
					c.getItems().deleteItem(itemId, 1);
					c.getItems().addItem(11690, 1);
					c.getItems().addItem(itemId + 8, 1);
					c.sendMessage("You dismantle the godsword blade from the hilt.");
				} else {
					c.sendMessage("You don't have anything to dismantle!");
				}
			} else {
				c.sendMessage("You don't have enough space to do this!");
			}
			break;
		case 6865:
			c.startAnimation(3005);
			c.gfx0(513);
			break;
		case 6867:
			c.startAnimation(3005);
			c.gfx0(509);
			break;
		case 6866:
			c.startAnimation(3005);
			c.gfx0(517);
			break;
		case 5733:
			c.getDH().sendDialogues(1656, 0);
			break;
		case 14614:
			if (c.dartsLoaded > 0) {
            c.sendMessage("You currently have <col=ff0000>" + c.dartsLoaded + "</col> darts in your blowpipe.");
			}
			if (c.dartsLoaded == 0) {
	            c.sendMessage("You currently have <col=ff0000>" + c.dartsLoaded + "</col> darts in your blowpipe.");
			}
			/*if (c.runedartsLoaded > 0) {
			c.sendMessage("You currently have <col=ff0000>" + c.runedartsLoaded + "</col> rune darts in your blowpipe.");	
			}
			if (c.runedartsLoaded == 0 && c.dartsLoaded == 0) {
			c.sendMessage("You have no darts in your blowpipe!");	
			}*/
            break;
		case 12345:
			c.sendMessage("The Trident of the Seas is empty and needs to be recharged.");
			break;
		case 12346:
			c.sendMessage("The Trident of the Seas is full and ready to use.");
			break;
		/*
		 * default: if (c.playerRights == 3) Misc.println(c.playerName+
		 * " - Item3rdOption: "+itemId); break;
		 */

		}

	}

}
