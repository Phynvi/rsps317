package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.content.skills.impl.slayer.SocialSlayerData;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.util.Misc;

/**
 * @author JaydenD12/Jaydennn
 */

public class ItemOnPlayer implements PacketType {
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int playerId = c.inStream.readUnsignedWord();
		int itemId = c.playerItems[c.inStream.readSignedWordBigEndian()] - 1;
		c.walkingToObject = false;
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
		case 13281:
		case 4155:
			slayerInviteItemOnPlayer(c, itemId, playerId);
			break;
		case 962:
			Player o = World.PLAYERS.get(playerId);
			c.gfx0(176);
			c.startAnimation(451);
			c.sendMessage("You pull the Christmas Cracker...");
			o.sendMessage("You pull the Christmas Cracker...");
			c.getItems().deleteItem(962, 1);
			if (Misc.random(3) == 1) {
				o.forcedText = "Yay I got the Cracker!";
				o.forcedChatUpdateRequired = true;
				o.getItems().addItem((1038 + Misc.random(5) * 2), 1);
			} else {
				c.forcedText = "Yay I got the Cracker!";
				c.forcedChatUpdateRequired = true;
				c.getItems().addItem((1038 + Misc.random(5) * 2), 1);
			}
			c.turnPlayerTo(o.getAbsX(), o.getAbsY());
			break;
		default:
			c.sendMessage("Nothing interesting happens.");
			break;
		}
	}

	private void slayerInviteItemOnPlayer(Player c, int itemId, int playerId) {
		try {
			Player client = World.PLAYERS.get(playerId);
			if (client != null && c != null) {
				if (!SocialSlayerData.isClientAvailable(c, client))
					return;
				SocialSlayerData.sendInviteInterface(c, client);
			}
		} catch (Exception e) {
			System.out.println("Unable to use item on player caused by Coop slayer method.");
			e.printStackTrace();
		}
	}

}