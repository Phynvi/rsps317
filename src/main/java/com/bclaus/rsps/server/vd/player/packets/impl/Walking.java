package com.bclaus.rsps.server.vd.player.packets.impl;

import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.vd.world.ItemHandler;

/**
 * Walking packet
 **/
public class Walking implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		if (packetType == 248 || packetType == 164) {
			c.faceUpdate(0);
			c.npcIndex = 0;
			c.clickObjectType = 0;
			c.walkingToObject = false;
			c.clickNpcType = 0;
			c.playerIndex = 0;
			if (c.followId > 0 || c.followId2 > 0) {
				c.getPA().resetFollow();
			}
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
		c.getPA().sendFrame126("", 35000);
		
		if (c.duelStatus == 6) {
			// dont allow the player to walk until we've claimed our items
			return;
		}
		
		c.walkingToItem = false;
		c.mageFollow = false;
		c.clickNpcType = 0;
		c.clickObjectType = 0;

		if (c.inItemOnDeath) {
			c.inItemOnDeath = false;
		}
		if (c.inTrade) {
			return;
		}
		if (!c.modeTut) {
			return;
		}
		if (c.doingAgility) {
			return;
		}
		if (c.playerStun) {
			return;
		}
		if (!c.canWalk) {
			return;
		}
		if (c.followId > 0 || c.followId2 > 0) {
			c.getPA().resetFollow();
		}
		if (c.isBanking) {
			c.isBanking = false;
		}
		
		c.getPA().removeAllWindows();
		if (c.duelRule[1] && c.duelStatus == 5) {
			if (World.PLAYERS.get(c.duelingWith) != null) {
				if (!Player.goodDistance(c.getX(), c.getY(), World.PLAYERS.get(c.duelingWith).getX(), World.PLAYERS.get(c.duelingWith).getY(), 1) || c.attackTimer == 0) {
					c.sendMessage("Walking has been disabled in this duel!");
				}
			}
			c.playerIndex = 0;
			return;
		}
		if (c.teleTimer > 0 || c.teleporting) {
			return;
		}

		if (c.freezeTimer > 0) {
			if (World.PLAYERS.get(c.playerIndex) != null) {
				if (Player.goodDistance(c.getX(), c.getY(), World.PLAYERS.get(c.playerIndex).getX(), World.PLAYERS.get(c.playerIndex).getY(), 1) && packetType != 98) {
					c.playerIndex = 0;
				}
			}
			return;
		}
		if (c.inDuelScreen && c.duelStatus != 5) {
			Player o = World.PLAYERS.get(c.duelingWith);
			c.getTradeAndDuel().declineDuel();
			o.getTradeAndDuel().declineDuel();
		}
		if (c.isDead) {
			return;
		}
		
		if (packetType == 248) {
			packetSize -= 14;
		}
		if (c.stopPlayerSkill) {
			c.stopPlayerSkill = false;
		}
		/*
		 * actual walking
		 */
		c.newWalkCmdSteps = packetSize - 5;
		c.newWalkCmdSteps /= 2;
		c.resetWalkingQueue();

		if (++c.newWalkCmdSteps > c.walkingQueueSize) {
			c.resetWalkingQueue();
			c.newWalkCmdSteps = 0;
			return;
		}
		if (c.getLastRegionHeight() != c.getHeight()) {
			c.setLastRegionHeight(c.getHeight());
			ItemHandler.reloadItems(c);
		}
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;

		int firstStepX = c.getInStream().readSignedWordBigEndianA();
		firstStepX -= c.mapRegionX * 8;
		for (int i = 1; i < c.newWalkCmdSteps; i++) {
			c.getNewWalkCmdX()[i] = c.getInStream().readSignedByte();
			c.getNewWalkCmdY()[i] = c.getInStream().readSignedByte();
		}

		int firstStepY = c.getInStream().readSignedWordBigEndian() - c.getMapRegionY() * 8;

		c.setNewWalkCmdIsRunning(c.getInStream().readSignedByteC() == 1);
		for (int i1 = 0; i1 < c.newWalkCmdSteps; i1++) {
			c.finalDestX = c.getNewWalkCmdX()[i1] += firstStepX;
			c.finalDestY = c.getNewWalkCmdY()[i1] += firstStepY;
		}
	}

}