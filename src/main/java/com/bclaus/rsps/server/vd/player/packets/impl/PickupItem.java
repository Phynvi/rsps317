package com.bclaus.rsps.server.vd.player.packets.impl;

import java.util.Arrays;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueDifficulty;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Pickup Item
 */
public class PickupItem implements PacketType {

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		int y = c.getInStream().readSignedWordBigEndian();
		int id = c.getInStream().readUnsignedWord();
		int x = c.getInStream().readSignedWordBigEndian();
		if (!Server.itemHandler.itemExists(id, x, y, c.getHeight())) {
			return;
		}
		if (Arrays.stream(ClueDifficulty.getClueIds()).anyMatch(i -> i == id)) {
			if (c.getItems().playerOwnsItem(id)) {
				c.sendMessage("You cannot have multiple clue scrolls!");
				return;
			}
		}
		/*if (c.totalPlaytime() < 600) {
			c.sendMessage("You have a pickup item delay of 5 minutes, it must deminish first.");
			return;
		}*/
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		c.pItemY = y;
		c.pItemId = id;
		c.pItemX = x;
		if (Math.abs(c.getX() - c.pItemX) > 25 || Math.abs(c.getY() - c.pItemY) > 25 || c.playerIsFiremaking) {
			c.resetWalkingQueue();
			return;
		}
		c.getCombat().resetPlayerAttack();
		c.walkingToItem = true;
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {

			@Override
			public void execute() {
				if (c.disconnected) {
					stop();
					return;
				}
				if (!c.walkingToItem)
					stop();
				if (c.getX() == c.pItemX && c.getY() == c.pItemY) {
					Server.itemHandler.removeGroundItem(c, c.pItemId, c.pItemX, c.pItemY, c.getHeight(), true);
					this.stop();
					return;//sec
				}
				if (Math.abs(c.getX() - c.pItemX) < 2 || Math.abs(c.getY() - c.pItemY) < 2) {
					Server.itemHandler.removeGroundItem(c, c.pItemId, c.pItemX, c.pItemY, c.getHeight(), true);
					this.stop();
					return;

				}
				this.stop();
			}

			@Override
			public void onStop() {
				c.walkingToItem = false;
			}
		}.attach(c));
	}
}
