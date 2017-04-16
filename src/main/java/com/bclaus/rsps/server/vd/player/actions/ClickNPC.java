package com.bclaus.rsps.server.vd.player.actions;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.npc.pets.Pet;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.content.skills.impl.hunter.HunterHandler;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.task.ScheduledTask;

/**
 * Click NPC
 */
public class ClickNPC implements PacketType {
	public static final int ATTACK_NPC = 72, MAGE_NPC = 131, FIRST_CLICK = 155, SECOND_CLICK = 17, THIRD_CLICK = 21;

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		c.npcIndex = 0;
		c.npcClickIndex = 0;
		c.playerIndex = 0;
		c.clickNpcType = 0;
		c.walkingToObject = false;
		c.getPA().resetFollow();
		if (c.playerIsNPC && c.playerNPCID != 1530 || c.teleporting) {
			return;
		}

		switch (packetType) {
		/**
		 * Attack npc melee or range
		 **/
		case ATTACK_NPC:
			c.npcIndex = c.getInStream().readUnsignedWordA();
			if (NPCHandler.NPCS.get(c.npcIndex) == null) {
				break;
			}
			if (NPCHandler.NPCS.get(c.npcIndex).MaxHP == 0) {
				c.npcIndex = 0;
				break;
			}
			if (!c.mageAllowed) {
				c.mageAllowed = true;
				c.sendMessage("I can't reach that.");
				break;
			}
			if (c.autocastId > 0) {
				c.autocasting = true;
			}
			if (!c.autocasting && c.spellId > 0) {
				c.spellId = 0;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.npcIndex = 0;
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			c.faceUpdate(c.npcIndex);
			c.usingMagic = false;
			boolean usingBow = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			boolean usingCross = c.playerEquipment[Player.playerWeapon] == 9185 || c.playerEquipment[Player.playerWeapon] == 12349;
			if (c.playerEquipment[Player.playerWeapon] >= 4214 && c.playerEquipment[Player.playerWeapon] <= 4223) {
				usingBow = true;
			}
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
			if ((usingBow || c.autocasting) && Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(c.npcIndex).getX(), NPCHandler.NPCS.get(c.npcIndex).getY(), 7)) {
				c.stopMovement();
			}
			if (usingOtherRangeWeapons && Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(c.npcIndex).getX(), NPCHandler.NPCS.get(c.npcIndex).getY(), 4)) {
				c.stopMovement();
			}
			if (!usingCross && !usingArrows && usingBow && c.playerEquipment[Player.playerWeapon] < 4212 && c.playerEquipment[Player.playerWeapon] > 4223 && !usingCross) {
				c.sendMessage("You have run out of arrows!");
				break;
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

			if (c.playerFollowIndex > 0) {
				c.getPA().resetFollow();
			}
			if (c.attackTimer <= 0) {
				c.getCombat().attackNpc(c.npcIndex);
				c.attackTimer++;
			}
			break;

		/**
		 * Attack npc with magic
		 **/
		case MAGE_NPC:
			if (!c.mageAllowed) {
				c.mageAllowed = true;
				c.sendMessage("I can't reach that.");
				break;
			}
			c.npcIndex = c.getInStream().readSignedWordBigEndianA();
			int castingSpellId = c.getInStream().readSignedWordA();
			c.usingMagic = false;

			if (NPCHandler.NPCS.get(c.npcIndex) == null) {
				return;
			}
			if (NPCHandler.NPCS.get(c.npcIndex).MaxHP == 0 || NPCHandler.NPCS.get(c.npcIndex).npcType == 944) {
				c.sendMessage("You can't attack this npc.");
				break;
			}
			for (int i = 0; i < Player.MAGIC_SPELLS.length; i++) {
				if (castingSpellId == Player.MAGIC_SPELLS[i][0]) {
					c.spellId = i;
					c.usingMagic = true;
					break;
				}
			}
			if (c.getBankPin().requiresUnlock()) {
				c.npcIndex = 0;
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			if (castingSpellId == 1171) { // crumble undead
				for (int npc : Constants.UNDEAD_NPCS) {
					if (NPCHandler.NPCS.get(c.npcIndex).npcType != npc) {
						c.sendMessage("You can only attack undead monsters with this spell.");
						c.usingMagic = false;
						c.stopMovement();
						break;
					}
				}
			}
			if (c.autocasting) {
				c.autocasting = false;
			}
			if (c.usingMagic) {
				if (Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(c.npcIndex).getX(), NPCHandler.NPCS.get(c.npcIndex).getY(), 6)) {
					c.stopMovement();
				}
				if (c.attackTimer <= 0) {
					c.getCombat().attackNpc(c.npcIndex);
					c.attackTimer++;
				}
			}
			break;

		case FIRST_CLICK:
			c.npcClickIndex = c.inStream.readSignedWordBigEndian();
			if (NPCHandler.NPCS.get(c.npcClickIndex) == null) {
				return;
			}
			if (HunterHandler.tryCatch(c, c.npcClickIndex)) {
				return;
			}
			c.npcType = NPCHandler.NPCS.get(c.npcClickIndex).npcType;
			if (NPCHandler.NPCS.get(c.npcClickIndex) == null) {
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.npcIndex = 0;
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			if (Pet.isPet(c.npcType)) {
				if (NPCHandler.NPCS.get(c.npcClickIndex).spawnedBy != c.getIndex()) {
					c.sendMessage("You cannot pickup this pet, you do not own it.");
					return;
				}
				if (c.getPet().getNpc() != null && NPCHandler.NPCS.get(c.npcClickIndex) == c.getPet().getNpc()) {
					if (c.getPet().getPet() != null) {
						c.getPet().dispose();
						return;
					}
				}
			}
			if (Player.goodDistance(NPCHandler.NPCS.get(c.npcClickIndex).getX(), NPCHandler.NPCS.get(c.npcClickIndex).getY(), c.getX(), c.getY(), 1)) {
				c.turnPlayerTo(NPCHandler.NPCS.get(c.npcClickIndex).getX(), NPCHandler.NPCS.get(c.npcClickIndex).getY());
				NPCHandler.NPCS.get(c.npcClickIndex).faceLocation(c.getX(), c.getY());

				c.getActions().firstClickNpc(c.npcType);
			} else {
				c.clickNpcType = 1;
				Server.getTaskScheduler().schedule(new ScheduledTask(1) {
					@Override
					public void execute() {
						if (c.disconnected) {
							stop();
							return;
						}
						if ((c.clickNpcType == 1) && NPCHandler.NPCS.get(c.npcClickIndex) != null) {
							if (Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(c.npcClickIndex).getX(), NPCHandler.NPCS.get(c.npcClickIndex).getY(), 1)) {
								c.turnPlayerTo(NPCHandler.NPCS.get(c.npcClickIndex).getX(), NPCHandler.NPCS.get(c.npcClickIndex).getY());
								NPCHandler.NPCS.get(c.npcClickIndex).faceLocation(c.getX(), c.getY());
								c.getActions().firstClickNpc(c.npcType);
								stop();
							}
						}
						if (c.clickNpcType == 0 || c.clickNpcType > 1)
							stop();
					}

					@Override
					public void onStop() {
						c.clickNpcType = 0;
					}
				});
			}
			break;

		case SECOND_CLICK:
			c.npcClickIndex = c.inStream.readUnsignedWordBigEndianA();
			c.npcType = NPCHandler.NPCS.get(c.npcClickIndex).npcType;
			if (c.getBankPin().requiresUnlock()) {
				c.npcIndex = 0;
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			if (Player.goodDistance(NPCHandler.NPCS.get(c.npcClickIndex).getX(), NPCHandler.NPCS.get(c.npcClickIndex).getY(), c.getX(), c.getY(), 1)) {
				c.turnPlayerTo(NPCHandler.NPCS.get(c.npcClickIndex).getX(), NPCHandler.NPCS.get(c.npcClickIndex).getY());
				NPCHandler.NPCS.get(c.npcClickIndex).faceLocation(c.getX(), c.getY());
				c.getActions().secondClickNpc(c.npcType);
			} else {
				c.clickNpcType = 2;
				Server.getTaskScheduler().schedule(new ScheduledTask(1) {

					@Override
					public void execute() {
						if ((c.clickNpcType == 2) && NPCHandler.NPCS.get(c.npcClickIndex) != null) {
							if (Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(c.npcClickIndex).getX(), NPCHandler.NPCS.get(c.npcClickIndex).getY(), 1)) {
								c.turnPlayerTo(NPCHandler.NPCS.get(c.npcClickIndex).getX(), NPCHandler.NPCS.get(c.npcClickIndex).getY());
								NPCHandler.NPCS.get(c.npcClickIndex).faceLocation(c.getX(), c.getY());
								c.getActions().secondClickNpc(c.npcType);
								stop();
							}
						}
						if (c.clickNpcType < 2 || c.clickNpcType > 2)
							stop();
					}

					@Override
					public void onStop() {
						c.clickNpcType = 0;

					}
				});
			}
			break;

		case THIRD_CLICK:
			c.npcClickIndex = c.inStream.readSignedWord();
			c.npcType = NPCHandler.NPCS.get(c.npcClickIndex).npcType;
			if (c.getBankPin().requiresUnlock()) {
				c.npcIndex = 0;
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			if (Player.goodDistance(NPCHandler.NPCS.get(c.npcClickIndex).getX(), NPCHandler.NPCS.get(c.npcClickIndex).getY(), c.getX(), c.getY(), 1)) {
				c.turnPlayerTo(NPCHandler.NPCS.get(c.npcClickIndex).getX(), NPCHandler.NPCS.get(c.npcClickIndex).getY());
				NPCHandler.NPCS.get(c.npcClickIndex).faceLocation(c.getX(), c.getY());
				c.getActions().thirdClickNpc(c.npcType);
			} else {
				c.clickNpcType = 3;
				Server.getTaskScheduler().schedule(new ScheduledTask(1) {
					@Override
					public void execute() {
						if ((c.clickNpcType == 3) && NPCHandler.NPCS.get(c.npcClickIndex) != null) {
							if (Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(c.npcClickIndex).getX(), NPCHandler.NPCS.get(c.npcClickIndex).getY(), 1)) {
								c.turnPlayerTo(NPCHandler.NPCS.get(c.npcClickIndex).getX(), NPCHandler.NPCS.get(c.npcClickIndex).getY());
								NPCHandler.NPCS.get(c.npcClickIndex).faceLocation(c.getX(), c.getY());
								c.getActions().thirdClickNpc(c.npcType);
								stop();
							}
						}
						if (c.clickNpcType < 3)
							stop();
					}

					@Override
					public void onStop() {
						c.clickNpcType = 0;

					}
				});
			}
			break;
		}

	}
}
