package com.bclaus.rsps.server.vd.player.packets.impl;

import com.bclaus.rsps.server.vd.content.cluescroll.ClueDifficulty;
import com.bclaus.rsps.server.vd.npc.pets.Pet;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.items.ItemAssistant;
import com.bclaus.rsps.server.vd.player.PlayerSave;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Drop Item
 */
public class DropItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId = c.getInStream().readUnsignedWordA();
		c.getInStream().readUnsignedByte();
		c.getInStream().readUnsignedByte();
		int slot = c.getInStream().readUnsignedWordA();
		if (c.isDead || c.playerLevel[3] <= 0 || c.teleporting) {
			return;
		}
		if (ClueDifficulty.isClue(itemId)) {
			c.sendMessage("You cannot drop clue scrolls!");
			return;
		}
		if (itemId == 2714) {
			c.sendMessage("You cannot drop clue reward caskets!");
			return;
		}
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		if(c.doingBone) {
			return;
		}
		if (c.underAttackBy != 0 && (ItemAssistant.getItemDef(itemId).get().ShopValue * .75) > 1000) {
			c.sendMessage("You can't drop items worth over 1,000 gold in combat.");
			return;
		}
		if (!Player.tradeEnabled) {
			c.sendMessage("Server-restrictions are currently set! you cannot drop your items!");
			return;
		}
		/*if (c.totalPlaytime() < 1500) {
			c.sendMessage("Your Player-protection data stops you from dropping items.");
			return;
		}*/
		if (c.inTrade || c.inDuelScreen || !c.getItems().playerHasItem(itemId, 1, slot)) {
			return;
		}
		if (c.totalPlaytime() < 36000 && itemId == 995) {
			c.sendMessage("You can no longer drop coins if you haven't played long enough.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		if (c.arenas()) {
			c.sendMessage("Wouldn't it be too risky to drop your items here? You might lose them!");
			return;
		}
		if (c.getAchievements().isAchievementItem(itemId) && !c.getPet().isPetOwner(itemId)) {
			c.sendMessage("You cannot drop an achievement reward item!");
			return;
			}
		if (c.getItems().playerHasItem(6) || c.getItems().playerHasItem(8) || c.getItems().playerHasItem(10) || c.getItems().playerHasItem(12)) {
			c.sendMessage("You can no longer drop your dwarf-multi cannon");
			return;
		}

		boolean droppable = true;
		if (c.playerItemsN[slot] != 0 && itemId != -1 && c.playerItems[slot] == itemId + 1) {
			if (droppable) {
				if (c.underAttackBy > 0) {
					
					if (ItemAssistant.getItemDef(itemId).get().ShopValue > 1000) {
						c.sendMessage("You can't drop items worth over 1,000 gold in combat.");
						return;
					}
				}
				if(itemId == 962) {
					if (!c.inWild()) {
						WearItem.usingRing(c);
						c.sendMessage("As you touch the cracker you suddenly turn into Santa!!");
						c.playerNPCID = 1552;
						c.forcedChat("Ho, Ho, Ho! Merry Christmas!");
						c.gfx0(606);
						return;
					} else {
						c.sendMessage("You can't do that here.");
					}
				}

				boolean dropOnGround = true;
				boolean deleteFromInventory = true;
				Pet petItem = Pet.get(itemId);
				if (petItem != null) {
					if (!c.getPet().spawn(itemId))
						deleteFromInventory = false;
					dropOnGround = false;
				}
				if(itemId == 10729) {
					return;
				}

				if (dropOnGround) {
					//Server.itemHandler.createGroundItem(c, itemId, c.getX(), c.getY(), c.getHeight(), c.playerItemsN[slot]);
				}
				if (deleteFromInventory) {
					c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
				}
				c.getPA().resetVariables();
				c.getCombat().resetPlayerAttack();
				PlayerSave.saveGame(c);
			} else {
				c.sendMessage("This items cannot be dropped.");
			}
		}

	}
}
