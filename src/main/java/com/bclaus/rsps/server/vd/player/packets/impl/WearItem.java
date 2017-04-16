package com.bclaus.rsps.server.vd.player.packets.impl;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Wear Item
 **/
public class WearItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.wearId = c.getInStream().readUnsignedWord();
		c.wearSlot = c.getInStream().readUnsignedWordA();
		c.interfaceId = c.getInStream().readUnsignedWordA();
		if (c.isDead || c.playerLevel[3] <= 0 || c.teleporting) {
			return;
		}
		if (!c.getItems().playerHasItem(c.wearId, 1, c.wearSlot)) {
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.isBanking = false;
			c.getBankPin().open(2);
			return;
		}
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		switch (c.wearId) {
		case 7927:
			if (!c.inWild()) {
				usingRing(c);
				c.sendMessage("As you put on the ring you turn into an egg!");
				c.playerNPCID = 3689;
			} else {
				c.sendMessage("You can't do that here.");
			}
			break;
		case 6583:
			if (!c.inWild()) {
				usingRing(c);
				c.sendMessage("As you put on the ring you turn into a rock!");
				c.playerNPCID = 2626;
			} else {
				c.sendMessage("You can't do that here.");
			}
			break;
		case 11014:
			if (!c.inWild()) {
				usingRing(c);
				c.sendMessage("As you put on the ring you turn into a pet-rabbit!");
				c.playerNPCID = 4845;
				c.playerStandIndex = 4706;
				c.playerWalkIndex = 6088;
				c.playerRunIndex = 6088;
			} else {
				c.sendMessage("You can't do that here.");
			}
			break;
		case 10729:
			if (!c.inWild()) {
				if(System.currentTimeMillis() - 24000 < c.lastAction) {
					c.sendMessage("You cannot perform this action again, please wait a few seconds.");
					return;
				}
				usingRing(c);
				c.playerNPCID = 5829;
				c.playerStandIndex = 6089;
				c.playerWalkIndex = 6088;
				c.playerRunIndex = 6088;
				c.getPet().setOwnedPet(10729, true);
				c.sendMessage("You can now drop your easter-ring to spawn a rabbit follower!");
				c.lastAction = System.currentTimeMillis();
			} else {
				c.sendMessage("You can't do that here.");
			}
			break;
		case 10732:
			if (!c.inWild()) {
				if(System.currentTimeMillis() - 24000 < c.lastAction) {
					c.sendMessage("You cannot perform this action again, please wait a few seconds.");
					return;
				}
				usingRing(c);
				c.sendMessage("As you attempt to wield the rubber chicken you feel weird..");
				c.playerNPCID = 3375;
				c.playerStandIndex = 2298;
				c.playerWalkIndex = 2297;
				c.playerRunIndex = 2297;
				c.lastAction = System.currentTimeMillis();
				c.forcedChat("Bwaaaaak bwuk bwuk!");
			} else {
				c.sendMessage("You can't do that here.");
			}
			break;
		}

		if ((c.playerIndex > 0 || c.npcIndex > 0) && c.wearId != 4153)
			c.getCombat().resetPlayerAttack();
		if (c.wearId >= 5509 && c.wearId <= 5515) {
			int pouch = -1;
			int a = c.wearId;
			if (a == 5509)
				pouch = 0;
			if (a == 5510)
				pouch = 1;
			if (a == 5512)
				pouch = 2;
			if (a == 5514)
				pouch = 3;
			c.getPA().emptyPouch(pouch);
			return;
		}
		if (c.inTrade) {
			c.getPA().closeAllWindows();
			return;
		}
		c.getItems().wearItem(c.wearId, c.wearSlot);
	}

	public static void usingRing(Player c) {
		c.resetWalkingQueue();
		for (int i = 0; i < 14; i++) {
			c.setSidebarInterface(i, 6014);
			c.isMorphed = true;
			c.playerIsNPC = true;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			c.getPA().resetFollow();
		}
	}

}
