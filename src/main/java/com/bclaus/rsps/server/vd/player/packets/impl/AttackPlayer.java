package com.bclaus.rsps.server.vd.player.packets.impl;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

/**
 * Attack Player
 **/
public class AttackPlayer implements PacketType {

	public static final int ATTACK_PLAYER = 73, MAGE_PLAYER = 249;

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.playerIndex = 0;
		c.npcIndex = 0;
		c.walkingToObject = false;
		if (c.playerIsNPC || c.teleporting) {
			return;
		}
		switch (packetType) {
		/**
		 * Attack player
		 **/
		case ATTACK_PLAYER:
			// int index = c.getInStream().readSignedWordBigEndian()
			c.playerIndex = c.getInStream().readSignedWordBigEndian();
			if (World.PLAYERS.get(c.playerIndex) == null) {
				break;
			}
			if (c.getIndex() < 0 || c.isDead) {
				return;
			}
			if (c.autocastId > 0)
				c.autocasting = true;
			if (!c.autocasting && c.spellId > 0) {
				c.spellId = 0;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.getCombat().resetPlayerAttack();
				c.getBankPin().open(2);
				return;
			}
			if (c.getQuarantine().isQuarantined()) {
				c.sendMessage("You are quarantined, you cannot do this.");
				return;
			}
			c.mageFollow = false;
			c.spellId = 0;
			c.usingMagic = false;
			boolean usingBow = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			boolean usingCross = c.playerEquipment[Player.playerWeapon] == 9185 || c.playerEquipment[Player.playerWeapon] == 12349;
			for (int bowId : Player.BOWS) {
				if (c.playerEquipment[Player.playerWeapon] == bowId) {
					usingBow = true;
					for (int arrowId : Player.ARROWS) {
						if (c.playerEquipment[Player.playerArrows] == arrowId) {
							usingArrows = true;
						}
					}
				}
			}
			for (int otherRangeId : Player.OTHER_RANGE_WEAPONS) {
				if (c.playerEquipment[Player.playerWeapon] == otherRangeId) {
					usingOtherRangeWeapons = true;
				}
			}
			for (int otherToxicBlowpipeId : c.TOXIC_BLOWPIPE) {
				if (c.playerEquipment[c.playerWeapon] == otherToxicBlowpipeId) {
					c.usingToxicBlowpipe = true;
				}
			}
			if (c.duelStatus == 5) {
				if (c.duelCount > 0) {
					c.sendMessage("The duel hasn't started yet!");
					c.getCombat().resetPlayerAttack();
					c.stopMovement();
					c.playerIndex = 0;
					return;
				}
				if (c.duelRule[9]) {
					boolean canUseWeapon = false;
					for (int funWeapon : Constants.FUN_WEAPONS) {
						if (c.playerEquipment[Player.playerWeapon] == funWeapon) {
							canUseWeapon = true;
						}
					}
					if (!canUseWeapon) {
						c.sendMessage("You can only use fun weapons in this duel!");
						return;
					}
				}

				if (c.duelRule[2] && (usingBow || usingOtherRangeWeapons)) {
					c.sendMessage("Range has been disabled in this duel!");
					return;
				}
				if (c.duelRule[3] && (!usingBow && !usingOtherRangeWeapons)) {
					c.sendMessage("Melee has been disabled in this duel!");
					return;
				}
			}

			if ((usingBow || c.autocasting) && Player.goodDistance(c.getX(), c.getY(), World.PLAYERS.get(c.playerIndex).getX(), World.PLAYERS.get(c.playerIndex).getY(), 7)) {
				c.usingBow = true;
				c.stopMovement();
			}

			if (usingOtherRangeWeapons && Player.goodDistance(c.getX(), c.getY(), World.PLAYERS.get(c.playerIndex).getX(), World.PLAYERS.get(c.playerIndex).getY(), 3)) {
				c.usingRangeWeapon = true;
				c.stopMovement();
			}
			if (!usingBow)
				c.usingBow = false;
			if (!usingOtherRangeWeapons)
				c.usingRangeWeapon = false;

			if (!usingCross && !usingArrows && usingBow && c.playerEquipment[Player.playerWeapon] < 4212 && c.playerEquipment[Player.playerWeapon] > 4223) {
				c.sendMessage("You have run out of arrows!");
				return;
			}
			if (c.getCombat().correctBowAndArrows() < c.playerEquipment[Player.playerArrows] && usingBow && !c.getCombat().usingCrystalBow() && c.playerEquipment[Player.playerWeapon] != 9185 && c.playerEquipment[Player.playerWeapon] != 12349) {
				c.sendMessage("You can't use " + c.getItems().getItemName(c.playerEquipment[Player.playerArrows]).toLowerCase() + "s with a " + c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase() + ".");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return;
			}
			if ((c.playerEquipment[Player.playerWeapon] == 9185 || c.playerEquipment[Player.playerWeapon] == 12349) && !c.getCombat().properBolts()) {
				c.sendMessage("You must use bolts with a crossbow.");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (c.getCombat().checkReqs()) {
				c.followId = c.playerIndex;
				if (!c.usingMagic && !usingBow && !usingOtherRangeWeapons) {
					c.followDistance = 1;
					c.getPA().followPlayer();
				}
				if (c.attackTimer <= 0) {
				}
			}
			break;

		/**
		 * Attack player with magic
		 **/
		case MAGE_PLAYER:
			if (!c.mageAllowed) {
				c.mageAllowed = true;
				break;
			}
			if (c.getQuarantine().isQuarantined()) {
				c.sendMessage("You are quarantined making you unable to do this action.");
				return;
			}
			c.playerIndex = c.getInStream().readSignedWordA();
			c.castingSpellId = c.getInStream().readSignedWordBigEndian();
			c.usingMagic = false;
			if (World.PLAYERS.get(c.playerIndex) == null) {
				c.playerIndex = 0;
				break;
			}

			if (c.isDead) {
				break;
			}

			for (int i = 0; i < Player.MAGIC_SPELLS.length; i++) {
				if (c.castingSpellId == Player.MAGIC_SPELLS[i][0]) {
					c.spellId = i;
					c.usingMagic = true;
					break;
				}
			}
			if (c.autocasting) {
				c.autocasting = false;
			}

			for (int r = 0; r < c.REDUCE_SPELLS.length; r++) { // reducing
				if (World.PLAYERS.get(c.playerIndex).REDUCE_SPELLS[r] == Player.MAGIC_SPELLS[c.spellId][0]) {
					if ((System.currentTimeMillis() - World.PLAYERS.get(c.playerIndex).reduceSpellDelay[r]) < World.PLAYERS.get(c.playerIndex).REDUCE_SPELL_TIME[r]) {
						c.sendMessage("That player is currently immune to this spell.");
						c.usingMagic = false;
						c.stopMovement();
						c.getCombat().resetPlayerAttack();
					}
					break;
				}
			}

			if (System.currentTimeMillis() - World.PLAYERS.get(c.playerIndex).teleBlockDelay < World.PLAYERS.get(c.playerIndex).teleBlockLength && Player.MAGIC_SPELLS[c.spellId][0] == 12445) {
				c.sendMessage("That player is already affected by this spell.");
				c.usingMagic = false;
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
			}
			if (c.duelStatus == 5) {
				if (c.duelCount > 0) {
					c.sendMessage("The duel hasn't started yet!");
					c.getCombat().resetPlayerAttack();
					c.stopMovement();
					c.playerIndex = 0;
					return;
				}
				if (c.duelRule[9]) {
					boolean canUseWeapon = false;
					for (int funWeapon : Constants.FUN_WEAPONS) {
						if (c.playerEquipment[Player.playerWeapon] == funWeapon) {
							canUseWeapon = true;
						}
					}
					if (!canUseWeapon) {
						c.sendMessage("You can only use fun weapons in this duel!");
						return;
					}
				}
			}
			if (c.usingMagic) {
				if (Player.goodDistance(c.getX(), c.getY(), World.PLAYERS.get(c.playerIndex).getX(), World.PLAYERS.get(c.playerIndex).getY(), 7)) {
					c.stopMovement();
				}
				if (c.getCombat().checkReqs()) {
					// c.playerFollowIndex = c.playerIndex;
					c.mageFollow = true;
				}
			}
		}

	}
}
