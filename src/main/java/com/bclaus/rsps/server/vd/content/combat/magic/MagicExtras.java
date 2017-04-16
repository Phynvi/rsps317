package com.bclaus.rsps.server.vd.content.combat.magic;

import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.util.Misc;

public class MagicExtras {

	public static void multiSpellEffectNPC(Player c, int npcId, int damage) {
		switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 12891:
		case 12881:
			if (NPCHandler.NPCS.get(npcId).freezeTimer < -4) {
				NPCHandler.NPCS.get(npcId).freezeTimer = c.getCombat().getFreezeTime();
			}
			break;
		}
	}

	public static boolean checkMultiBarrageReqsNPC(int i) {
		if (NPCHandler.NPCS.get(i) == null) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean checkMultiBarrageReqs(Player c, int i) {
		Player target = World.PLAYERS.get(i);
		if (World.PLAYERS.get(i) == null) {
			return false;
		}
		if (i == c.getIndex())
			return false;
		if (!World.PLAYERS.get(i).inWild()) {
			return false;
		}
		if (!c.getAccount().getType().attackableTypes().contains(target.getAccount().getType())) {
			return false;
		}
		if (Constants.COMBAT_LEVEL_DIFFERENCE) {
			int combatDif1 = c.getCombat().getCombatDifference(c.combatLevel, World.PLAYERS.get(i).combatLevel);
			if (combatDif1 > c.wildLevel || combatDif1 > World.PLAYERS.get(i).wildLevel) {
				c.sendMessage("Your combat level difference is too great to attack that player here.");
				return false;
			}
		}
		if (Constants.SINGLE_AND_MULTI_ZONES) {
			if (!World.PLAYERS.get(i).inMulti()) { // single combat zones
				if (World.PLAYERS.get(i).underAttackBy != c.getIndex() && World.PLAYERS.get(i).underAttackBy != 0) {
					return false;
				}
				if (World.PLAYERS.get(i).getIndex() != c.underAttackBy && c.underAttackBy != 0) {
					c.sendMessage("You are already in combat.");
					return false;
				}
			}
		}
		return true;
	}

	public static void appendMultiBarrageNPC(Player c, int npcId, boolean splashed) {
		if (NPCHandler.NPCS.get(npcId) != null) {
			NPC n = NPCHandler.NPCS.get(npcId);
			if (n.isDead)
				return;
			if (checkMultiBarrageReqsNPC(npcId)) {
				c.barrageCount++;
				c.multiAttacking = true;
				NPCHandler.NPCS.get(npcId).underAttackBy = c.getIndex();
				NPCHandler.NPCS.get(npcId).underAttack = true;
				if (Misc.random(c.getCombat().mageAtk()) > Misc.random(c.getCombat().mageDef()) && !c.magicFailed) {
					if (c.getCombat().getEndGfxHeight() == 100) { // end GFX
						n.gfx100(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					} else {
						n.gfx0(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					}
					int damage = Misc.random(c.getCombat().magicMaxHit());
					if (NPCHandler.NPCS.get(npcId).HP - damage < 0) {
						damage = NPCHandler.NPCS.get(npcId).HP;
					}
					NPCHandler.NPCS.get(npcId).damage(new Hit(damage));
					c.totalPlayerDamageDealt += damage;
					c.getCombat().multiSpellEffectNPC(npcId, damage);
					c.totalDamageDealt += damage;
					c.getCombat().multiSpellEffectNPC(npcId, damage);
				} else {
					n.gfx100(85);
				}
			}
		}
	}

	public static void multiSpellEffect(Player c, int playerId, int damage) {
		switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 13011:
		case 13023:
			if (System.currentTimeMillis() - World.PLAYERS.get(playerId).reduceStat > 35000) {
				World.PLAYERS.get(playerId).reduceStat = System.currentTimeMillis();
				World.PLAYERS.get(playerId).playerLevel[0] -= ((Player.getLevelForXP(World.PLAYERS.get(playerId).playerXP[0]) * 10) / 100);
			}
			break;
		case 12919: // blood spells
		case 12929:
			/*int heal = damage / 4;
			if (c.playerLevel[3] + heal >= c.getPA().getLevelForXP(c.playerXP[3])) {
				c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
			} else {
				c.playerLevel[3] += heal;
			}
			c.getPA().refreshSkill(3);*/
			break;
		case 12891:
		case 12881:
			if (World.PLAYERS.get(playerId).freezeTimer < -4) {
				World.PLAYERS.get(playerId).freezeTimer = c.getCombat().getFreezeTime();
				World.PLAYERS.get(playerId).stopMovement();
			}
			break;
		}
	}

	public static void appendMultiBarrage(Player c, int playerId, boolean splashed) {
		if (World.PLAYERS.get(playerId) != null) {
			Player c2 = World.PLAYERS.get(playerId);
			if (c2.isDead)
				return;
			if (c.getCombat().checkMultiBarrageReqs(playerId)) {
				c.barrageCount++;
				if (Misc.random(c.getCombat().mageAtk()) > Misc.random(c2.getCombat().mageDef()) && !c.magicFailed) {
					if (c.getCombat().getEndGfxHeight() == 100) { // end GFX
						c2.gfx100(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					} else {
						c2.gfx0(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					}
					int damage = Misc.random(c.getCombat().magicMaxHit());
					if (c2.prayerActive[12]) {
						damage *= (int) (.60);
					}
					if (c2.playerLevel[3] - damage < 0) {
						damage = c2.playerLevel[3];
					}
					c.getPA().addSkillXP((Player.MAGIC_SPELLS[c.oldSpellId][7] + damage * Constants.MAGIC_EXP_RATE), 6);
					c.getPA().addSkillXP((Player.MAGIC_SPELLS[c.oldSpellId][7] + damage * Constants.MAGIC_EXP_RATE / 3), 3);
					World.PLAYERS.get(playerId).damage(new Hit(damage));
					World.PLAYERS.get(playerId).addDamageReceived(c.playerName, damage);
					c.getPA().refreshSkill(3);
					c.totalPlayerDamageDealt += damage;
					c.getCombat().multiSpellEffect(playerId, damage);
				} else {
					c2.gfx100(85);
				}
			}
		}
	}
}