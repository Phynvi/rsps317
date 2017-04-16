package com.bclaus.rsps.server.vd.content.combat.melee;

import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.util.Misc;

public class MeleeExtras {

	public static void applySmite(Player c, int index, int damage) {
		if (!c.prayerActive[23])
			return;
		if (damage <= 0)
			return;
		if (World.PLAYERS.get(index) != null) {
			Player c2 = World.PLAYERS.get(index);
			c2.playerLevel[5] -= damage / 4;
			if (c2.playerLevel[5] <= 0) {
				c2.playerLevel[5] = 0;
				c2.getCombat().resetPrayers();
			}
			c2.getPA().refreshSkill(5);
		}
	}

	public static void handleDfs(Player c) {
		if (System.currentTimeMillis() - c.dfsDelay > 30000) {
			if (c.playerIndex > 0 && World.PLAYERS.get(c.playerIndex) != null) {
				if (c.duelStatus == 5 && World.PLAYERS.get(c.playerIndex).duelStatus == 5) {
					if (World.PLAYERS.get(c.playerIndex).duelingWith == c.getIndex()) {
					} else {
						c.sendMessage("This isn't your opponent!");
						return;
					}
				}
				int damage = Misc.random(15) + 5;
				c.startAnimation(6695);
				c.gfx0(1164);
				World.PLAYERS.get(c.playerIndex).damage(new Hit(damage));
				c.dfsDelay = System.currentTimeMillis();
			} else {
				c.sendMessage("I should be in combat before using this.");
			}
		} else {
			c.sendMessage("My shield hasn't finished recharging yet.");
		}
	}

	public static void handleDfsNPC(Player c) {
		if (System.currentTimeMillis() - c.dfsDelay > 30000) {
			if (c.npcIndex > 0 && NPCHandler.NPCS.get(c.npcIndex) != null) {
				int damage = Misc.random(15) + 5;
				c.startAnimation(6695);
				c.gfx0(1164);
				NPCHandler.NPCS.get(c.npcIndex).damage(new Hit(damage));
				c.dfsDelay = System.currentTimeMillis();
			} else {
				c.sendMessage("I should be in combat before using this.");
			}
		} else {
			c.sendMessage("My shield hasn't finished recharging yet.");
		}
	}

	public static void appendVengeanceNPC(Player c, int otherPlayer, int damage) {
		if (damage <= 0)
			return;
		if (c.npcIndex > 0 && NPCHandler.NPCS.get(c.npcIndex) != null) {
			c.forcedText = "Taste vengeance!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
			c.vengOn = false;
			if ((NPCHandler.NPCS.get(c.npcIndex).HP - damage) > 0) {
				damage = (int) (damage * 0.75);
				if (damage > NPCHandler.NPCS.get(c.npcIndex).HP) {
					damage = NPCHandler.NPCS.get(c.npcIndex).HP;
				}
				NPCHandler.NPCS.get(c.npcIndex).damage(new Hit(damage));
			}
		}
		c.updateRequired = true;
	}

	public static void appendVengeance(Player c, int otherPlayer, int damage) {
		if (damage <= 0)
			return;
		Player o = World.PLAYERS.get(otherPlayer);
		o.forcedText = "Taste vengeance!";
		o.forcedChatUpdateRequired = true;
		o.updateRequired = true;
		o.vengOn = false;
		if ((o.playerLevel[3] - damage) > 0) {
			damage = (int) (damage * 0.75);
			if (damage > c.playerLevel[3]) {
				damage = c.playerLevel[3];
			}
			c.damage(new Hit(damage));
		}
		c.updateRequired = true;
	}

	public static void applyRecoilNPC(Player c, int damage, int i) {
		if (damage > 0 && c.playerEquipment[Player.playerRing] == 2550) {
			int recDamage = damage / 10 + 1;
			NPCHandler.NPCS.get(c.npcIndex).damage(new Hit(recDamage));
			removeRecoil(c);
			c.recoilHits += damage;
		}
	}

	public static void applyRecoil(Player c, int damage, int i) {
		if (damage > 0 && World.PLAYERS.get(i).playerEquipment[Player.playerRing] == 2550) {
			int recDamage = damage / 10 + 1;
			c.damage(new Hit(recDamage));
			c.updateRequired = true;
			removeRecoil(c);
			c.recoilHits += damage;
		}
	}

	public static void applySpiritEffects(Player c, int damage, int i) {
		if (damage > 0) {
			if (c.playerEquipment[Player.playerShield] == 15000) {
				Player player = World.PLAYERS.get(i);
				if (player.prayerPoint > 0) {
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
						}
					}
				}
			}
		}
	}

	public static void removeRecoil(Player c) {
		Player o = World.PLAYERS.get(c.playerIndex);
		if (o != null) {
			if (o.recoilHits >= 400) {
				o.getItems().wearItem(-1, 0, 12);
				o.sendMessage("Your ring of recoil shaters!");
				o.recoilHits = 0;
			} else
				o.recoilHits++;
		}
	}

	public static void graniteMaulSpecial(Player c) {
		if (c.playerIndex > 0) {
			Player o = World.PLAYERS.get(c.playerIndex);
			if (Player.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), c.getCombat().getRequiredDistance())) {
				if (c.getCombat().checkReqs()) {
					if (c.getCombat().checkSpecAmount(4153)) {
						boolean hit = Misc.random(c.getCombat().calculateMeleeAttack()) > Misc.random(o.getCombat().calculateMeleeDefence());
						int damage = 0;
						if (hit)
							damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
						if (o.prayerActive[18] && System.currentTimeMillis() - o.protMeleeDelay > 1500)
							damage *= .6;
						if (o.playerLevel[3] - damage <= 0) {
							damage = o.playerLevel[3];
						}
						if (o.playerLevel[3] > 0) {
							o.damage(new Hit(damage));
							c.startAnimation(1667);
							o.gfx100(337);
						}
					}
				}
			}
		} else if (c.npcIndex > 0) {
			int x = NPCHandler.NPCS.get(c.npcIndex).absX;
			int y = NPCHandler.NPCS.get(c.npcIndex).absY;
			if (Player.goodDistance(c.getX(), c.getY(), x, y, 2)) {
				if (c.getCombat().checkReqs()) {
					if (c.getCombat().checkSpecAmount(4153)) {
						int damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
						if (NPCHandler.NPCS.get(c.npcIndex).HP - damage < 0) {
							damage = NPCHandler.NPCS.get(c.npcIndex).HP;
						}
						if (NPCHandler.NPCS.get(c.npcIndex).HP > 0) {
							NPCHandler.NPCS.get(c.npcIndex).damage(new Hit(damage));
							c.startAnimation(1667);
							c.gfx100(337);
						}
					}
				}
			}
		}
	}
}