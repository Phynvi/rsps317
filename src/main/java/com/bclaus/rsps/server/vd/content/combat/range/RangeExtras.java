package com.bclaus.rsps.server.vd.content.combat.range;

import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.util.Misc;

public class RangeExtras {

	public static void appendMutliChinchompa(Player c, int npcId) {
		if (NPCHandler.NPCS.get(npcId) != null) {
			NPC n = NPCHandler.NPCS.get(npcId);
			if (n.isDead) {
				return;
			}
			c.multiAttacking = true;
			int damage = Misc.random(c.getCombat().rangeMaxHit());
			if (NPCHandler.NPCS.get(npcId).HP - damage < 0) {
				damage = NPCHandler.NPCS.get(npcId).HP;
			}
			NPCHandler.NPCS.get(npcId).underAttackBy = c.getIndex();
			NPCHandler.NPCS.get(npcId).underAttack = true;
			NPCHandler.NPCS.get(npcId).damage(new Hit(damage));
		}
	}

	private static void createCombatGFX(Player c, int i, int gfx, boolean height100) {
		Player p = World.PLAYERS.get(i);
		NPC n = NPCHandler.NPCS.get(i);
		if (c.playerIndex > 0) {
			if (height100) {
				p.gfx100(gfx);
			} else {
				p.gfx0(gfx);
			}
		} else if (c.npcIndex > 0) {
			if (height100) {
				n.gfx100(gfx);
			} else {
				n.gfx0(gfx);
			}
		}
	}

	public static void crossbowSpecial(Player c, int i) {
		if (i > World.PLAYERS.capacity() || i > NPCHandler.NPCS.capacity()) {
			c.sendMessage("" + NPCHandler.NPCS.capacity());
			return;
		}
		Player p = World.PLAYERS.get(i);
		NPC n = NPCHandler.NPCS.get(i);
		c.crossbowDamage = 1.0;
		switch (c.lastArrowUsed) {
		case 9236: // Lucky Lightning
			createCombatGFX(c, i, 749, false);
			c.crossbowDamage = 1.25;
			break;
		case 9237: // Earth's Fury
			createCombatGFX(c, i, 755, false);
			break;
		case 9238: // Sea Curse
			createCombatGFX(c, i, 750, false);
			c.crossbowDamage = 1.10;
			break;
		case 9239: // Down to Earth
			createCombatGFX(c, i, 757, false);
			if (c.playerIndex > 0) {
				p.playerLevel[6] -= 2;
				p.getPA().refreshSkill(6);
				p.sendMessage("Your magic has been lowered!");
			}
			break;
		case 9240: // Clear Mind
			createCombatGFX(c, i, 751, false);
			if (c.playerIndex > 0) {
				p.playerLevel[5] -= 2;
				p.getPA().refreshSkill(5);
				p.sendMessage("Your prayer has been lowered!");
				p.playerLevel[5] += 2;
				if (p.playerLevel[5] >= c.getPA().getLevelForXP(c.playerXP[5])) {
					p.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				}
				p.getPA().refreshSkill(5);
			}
			break;
		case 9241: // Magical Posion
			createCombatGFX(c, i, 752, false);
			break;
		case 9242: // Blood Forfiet
			createCombatGFX(c, i, 754, false);

			if (c.playerLevel[3] - c.playerLevel[3] / 20 < 1) {
				break;
			}
			c.damage(new Hit((c.playerLevel[3] / 20)));
			if (c.npcIndex > 0) {
				n.damage(new Hit((n.HP / 10)));
			} else if (c.playerIndex > 0) {
				p.damage(new Hit((c.playerLevel[3] / 10)));
			}
			break;
		case 9243: // Armour Piercing
			createCombatGFX(c, i, 758, true);
			c.crossbowDamage = 1.15;
			c.ignoreDefence = true;
			break;
		case 9244: // Dragon's Breath
			createCombatGFX(c, i, 756, false);
			if (c.playerEquipment[Player.playerShield] != 1540 || c.playerEquipment[Player.playerShield] != 11283 || c.playerEquipment[Player.playerShield] != 11284) {
				c.crossbowDamage = 1.45;
			}
			break;
		case 9245: // Life Leech
			createCombatGFX(c, i, 753, false);
			int damage = Misc.random(c.getCombat().rangeMaxHit());
			int greatDamage = damage *= 1.25;
			int hpHeal = (int) (greatDamage * 0.25);
			c.playerLevel[3] += hpHeal;
			if (c.playerLevel[3] >= 112) {
				c.playerLevel[3] = 112;
			}
			c.getPA().refreshSkill(3);
			break;
			/**
			 * if (player.prayerPoint > 0) {
					if (player.playerLevel[3] > 4) {
						double damageRecieved = damage * 0.85;
						int prayerLost = (int) (damage * 0.3);
						if (player.prayerPoint >= prayerLost) {
							damage = (int) damageRecieved;
							player.prayerPoint -= prayerLost;
							player.playerLevel[5] -= prayerLost;
							player.getPA().refreshSkill(5);
							if (player.prayerPoint < 0)
								player.prayerPoint = 0;
							c.gfx0(247);
			 */
		}
	}
}