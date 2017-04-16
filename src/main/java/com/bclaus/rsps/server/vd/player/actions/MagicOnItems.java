/**
 * 
 */
package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Magic on items
 * 
 */
public class MagicOnItems implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int slot = c.getInStream().readSignedWord();
		int itemId = c.getInStream().readSignedWordA();
		int junk = c.getInStream().readSignedWord();
		int spellId = c.getInStream().readSignedWordA();
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.isBanking = false;
			c.getBankPin().open(2);
			return;
		}
		c.usingMagic = true;
		c.getPA().magicOnItems(slot, itemId, spellId);
		c.usingMagic = false;

	}
}
