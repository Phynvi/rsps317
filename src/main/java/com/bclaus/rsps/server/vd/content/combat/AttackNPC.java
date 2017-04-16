package com.bclaus.rsps.server.vd.content.combat;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.content.combat.range.RangeData;
import com.bclaus.rsps.server.vd.content.minigames.PestControl;
import com.bclaus.rsps.server.vd.content.skills.impl.slayer.Slayer;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.vd.mobile.PoisonCombatTask;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.npc.boss.Bosses;
import com.bclaus.rsps.server.vd.player.Boundary;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.util.Misc;

public class AttackNPC {
	public static void addNPCHit(final int i, final Player c, Item item) {
		if (c.projectileStage == 0 && !c.usingMagic && !c.castingMagic) {
			if (c.doubleHit) {
				c.getCombat().applyNpcMeleeDamage(i, 1, Misc.random(c.getCombat().calculateMeleeMaxHit()));
				c.getCombat().applyNpcMeleeDamage(i, 2, Misc.random(c.getCombat().calculateMeleeMaxHit()));
			} else {
				c.getCombat().applyNpcMeleeDamage(i, 1, Misc.random(c.getCombat().calculateMeleeMaxHit()));
			}
			PoisonCombatTask.getPoisonType(item).ifPresent(NPCHandler.NPCS.get(i)::poison);
		}
		
		NPC npc = NPCHandler.NPCS.get(i);
		c.getPA().sendFrame126(NPCHandler.getNpcListName(npc.npcType).replaceAll("_", " ")+
				"-"+npc.MaxHP+"-"+npc.HP, 35000);
		
	}

	public static void applyNpcMeleeDamage(Player c, int i, int damageMask, int damage) {
		if (PestControl.isInGame(c)) {
			c.pcDamage += damage;
		}
		c.previousDamage = damage;
		boolean fullVeracsEffect = c.getCombat().fullVeracs() && Misc.random(3) == 1;
		if (NPCHandler.NPCS.get(i).HP - damage < 0) {
			damage = NPCHandler.NPCS.get(i).HP;
		}
		if (!fullVeracsEffect) {
			if (Misc.random(NPCHandler.NPCS.get(i).defence) > 10 + Misc.random(c.getCombat().calculateMeleeAttack())) {
				damage = 0;
			}
		}
		if (c.vengOn && damage > 0) {
			c.getCombat().appendVengeanceNPC(i, damage);
		}
		boolean guthansEffect = false;
		if (c.getCombat().fullGuthans()) {
			if (Misc.random(3) == 1) {
				guthansEffect = true;
			}
		}
		if (damageMask == 1) {
			damage = c.delayedDamage;
			c.delayedDamage = 0;
		} else {
			damage = c.delayedDamage2;
			c.delayedDamage2 = 0;
		}
		if (c.playerEquipment[Player.playerHat] == 8901) {
			if (c.slayerTask > 0 && Slayer.isSlayerNpc(NPCHandler.NPCS.get(i).npcType))
				damage *= 1.00;
		}
		if (c.playerEquipment[Player.playerHat] == 13263) {
			if (c.slayerTask > 0 && Slayer.isSlayerNpc(NPCHandler.NPCS.get(i).npcType))
				damage *= 1.15;
		}
		if (NPCHandler.NPCS.get(i).npcType == 2883 || NPCHandler.NPCS.get(i).npcType == 2882) {
			damage = 0;
		}
		if (NPCHandler.NPCS.get(i).HP - damage < 0) {
			damage = NPCHandler.NPCS.get(i).HP;
		}
		if (c.fightMode == 3) {
			c.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE), 2);
			c.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE / 3), 3);
			c.getPA().refreshSkill(2);
			c.getPA().refreshSkill(3);
		} else {
			c.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE), c.fightMode);
			c.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE / 3), 3);
			c.getPA().refreshSkill(c.fightMode);
			c.getPA().refreshSkill(3);
		}
		if (c.ssSpec && damageMask == 2) {
			damage = 5 + Misc.random(11);
			c.ssSpec = false;
		}
		if (damage > 0 && guthansEffect) {
			c.playerLevel[3] += damage;
			if (c.playerLevel[3] > Player.getLevelForXP(c.playerXP[3]))
				c.playerLevel[3] = Player.getLevelForXP(c.playerXP[3]);
			c.getPA().refreshSkill(3);
			NPCHandler.NPCS.get(i).gfx0(398);
		}

		NPCHandler.NPCS.get(i).underAttack = true;
		c.killingNpcIndex = c.npcIndex;
		c.lastNpcAttacked = i;
		switch (c.specEffect) {
		case 4:
			if (damage > 0) {
				if (c.playerLevel[3] + damage > Player.getLevelForXP(c.playerXP[3]))
					if (c.playerLevel[3] > Player.getLevelForXP(c.playerXP[3]))
						;
					else
						c.playerLevel[3] = Player.getLevelForXP(c.playerXP[3]);
				else
					c.playerLevel[3] += damage;
				c.getPA().refreshSkill(3);
			}
			c.specEffect = 0;
			break;
		}
		switch (damageMask) {
		case 1:
			NPCHandler.NPCS.get(i).damage(new Hit(damage));
			c.totalDamageDealt += damage;
			break;

		case 2:
			NPCHandler.NPCS.get(i).damage(new Hit(damage));
			c.totalDamageDealt += damage;
			break;
		}
	}

	public static void delayedHit(final Player c, final int i, Item item) {
		if (NPCHandler.NPCS.get(i) != null) {
			if (NPCHandler.NPCS.get(i).isDead) {
				c.npcIndex = 0;
				return;
			}
			NPCHandler.NPCS.get(i).facePlayer(c.getIndex());
			NPCHandler.NPCS.get(i).walkingHome = false;
			if (NPCHandler.NPCS.get(i).underAttackBy > 0) {
				NPCHandler.NPCS.get(i).killerId = c.getIndex();
			} else if (NPCHandler.NPCS.get(i).underAttackBy < 0) {
				NPCHandler.NPCS.get(i).killerId = c.getIndex();
			}
			c.lastNpcAttacked = i;
			c.getCombat().addNPCHit(i, c, item);
			if (!c.castingMagic && c.projectileStage > 0) { // range hit damag
				int damage = Misc.random(c.getCombat().rangeMaxHit());
				int damage2 = -1;
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1)
					damage2 = Misc.random(c.getCombat().rangeMaxHit());
				if (Misc.random(NPCHandler.NPCS.get(i).defence) > Misc.random(10 + c.getCombat().calculateRangeAttack()) && !c.ignoreDefence) {
					damage = 0;
				} else if (NPCHandler.NPCS.get(i).npcType == 2881 || NPCHandler.NPCS.get(i).npcType == 2883 && !c.ignoreDefence) {
					damage = 0;
				}
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1) {
					if (Misc.random(NPCHandler.NPCS.get(i).defence) > Misc.random(10 + c.getCombat().calculateRangeAttack()))
						damage2 = 0;
				}
				if (c.dbowSpec) {
					NPCHandler.NPCS.get(i).gfx100(c.lastArrowUsed == 11212 ? 1100 : 1103);
					if (damage < 8)
						damage = 8;
					if (damage2 < 8)
						damage2 = 8;
					c.dbowSpec = false;
				}

				if (c.playerEquipment[3] == 12349) {
					damage *= 1.40;
				}
				if (c.playerEquipment[3] == 4212) {
					damage *= 2.30;
				}
				if (c.playerEquipment[3] == 14614) {
					damage *= 1.60;
				}
				if (c.playerEquipment[3] == 9185 || c.playerEquipment[3] == 12349) {
					NPC n = NPCHandler.NPCS.get(i);
					if (n == null) {
						return;
					}
					if (Misc.random(8) == 1) {
						if (damage > 0) {
							c.boltDamage = damage;
							c.getCombat().crossbowSpecial(c, i);
							damage *= c.crossbowDamage;
						}
					}
				}
				if (c.slayerTask > 0 && Slayer.isSlayerNpc(NPCHandler.NPCS.get(i).npcType)) {
					damage *= 1.15;
				}

				if (NPCHandler.NPCS.get(i).HP - damage < 0) {
					damage = NPCHandler.NPCS.get(i).HP;
				}
				if (damage2 > 0) {
					if (damage == NPCHandler.NPCS.get(i).HP && NPCHandler.NPCS.get(i).HP - damage2 > 0) {
						damage2 = 0;
					}
				}
				if (c.fightMode == 3) {
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 4);
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 1);
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 3);
					c.getPA().refreshSkill(1);
					c.getPA().refreshSkill(3);
					c.getPA().refreshSkill(4);
				} else {
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE), 4);
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 3);
					c.getPA().refreshSkill(3);
					c.getPA().refreshSkill(4);
				}
				boolean dropArrows = true;
				for (int noArrowId : Player.NO_ARROW_DROP) {
					if (c.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					c.getItems().dropArrowNpc();
					if (c.playerEquipment[3] == 11235) {
						c.getItems().dropArrowNpc();
					}
				}
				if (NPCHandler.NPCS.get(i).attackTimer < 5)
					NPCHandler.startAnimation(c.getCombat().npcDefenceAnim(i), i);
				c.rangeEndGFX = RangeData.getRangeEndGFX(c);
				if ((c.playerEquipment[3] == 10034 || c.playerEquipment[3] == 10033)) {
					for (int j = 0; j < NPCHandler.NPCS.capacity(); j++) {
						if (NPCHandler.NPCS.get(j) != null && NPCHandler.NPCS.get(j).MaxHP > 0) {
							int nX = NPCHandler.NPCS.get(j).getX();
							int nY = NPCHandler.NPCS.get(j).getY();
							int pX = NPCHandler.NPCS.get(i).getX();
							int pY = NPCHandler.NPCS.get(i).getY();
							if ((nX - pX == -1 || nX - pX == 0 || nX - pX == 1) && (nY - pY == -1 || nY - pY == 0 || nY - pY == 1)) {
								if (NPCHandler.NPCS.get(i).inMulti()) {
									Player p = World.PLAYERS.get(c.getIndex());
									c.getCombat().appendMutliChinchompa(j);
									Server.npcHandler.attackPlayer(p, j);
								}
							}
						}
					}
				}
				if (!c.multiAttacking) {
					NPCHandler.NPCS.get(i).underAttack = true;
					NPCHandler.NPCS.get(i).damage(new Hit(damage));

					if (damage2 > -1) {
						NPCHandler.NPCS.get(i).damage(new Hit(damage2));
						c.totalDamageDealt += damage2;
					}
				}
				c.ignoreDefence = false;
				c.multiAttacking = false;
				if (c.rangeEndGFX > 0) {
					if (c.rangeEndGFXHeight) {
						NPCHandler.NPCS.get(i).gfx100(c.rangeEndGFX);
					} else {
						NPCHandler.NPCS.get(i).gfx0(c.rangeEndGFX);
					}
				}
				if (c.killingNpcIndex != c.oldNpcIndex) {
					c.totalDamageDealt = 0;
				}
				c.killingNpcIndex = c.oldNpcIndex;
				c.totalDamageDealt += damage;
				NPCHandler.NPCS.get(i).hitUpdateRequired = true;
				if (damage2 > -1)
					NPCHandler.NPCS.get(i).hitUpdateRequired2 = true;
				NPCHandler.NPCS.get(i).updateRequired = true;

			} else if (c.projectileStage > 0) { // magic hit damage
				if (NPCHandler.NPCS.get(i).HP <= 0) {
					return;
				}
				if (c.spellSwap) {
					c.spellSwap = false;
					c.setSidebarInterface(6, 16640);
					c.playerMagicBook = 2;
					c.gfx0(-1);
				}
				int damage = 0;
				c.usingMagic = true;
				damage = Misc.random(c.getCombat().magicMaxHit());
				if (c.getCombat().godSpells()) {
					if (System.currentTimeMillis() - c.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
						damage += Misc.random(10);
					}
				}
				boolean magicFailed = false;
				if (c.playerEquipment[Player.playerWeapon] == 12346) {
					Misc.random(NPCHandler.NPCS.get(i).defence);
				} else if (Misc.random(NPCHandler.NPCS.get(i).defence) > 10 + Misc.random(c.getCombat().mageAtk())) {
					damage = 0;
					magicFailed = true;
				} else if (NPCHandler.NPCS.get(i).npcType == 2881 || NPCHandler.NPCS.get(i).npcType == 2882) {
					damage = 0;
					magicFailed = true;
				}
				for (int j = 0; j < NPCHandler.NPCS.capacity(); j++) {
					if (NPCHandler.NPCS.get(j) != null && NPCHandler.NPCS.get(j).MaxHP > 0) {
						int nX = NPCHandler.NPCS.get(j).getX();
						int nY = NPCHandler.NPCS.get(j).getY();
						int pX = NPCHandler.NPCS.get(i).getX();
						int pY = NPCHandler.NPCS.get(i).getY();
						if ((nX - pX == -1 || nX - pX == 0 || nX - pX == 1) && (nY - pY == -1 || nY - pY == 0 || nY - pY == 1)) {
							if (c.getCombat().multis() && NPCHandler.NPCS.get(i).inMulti()) {
								Player p = World.PLAYERS.get(c.getIndex());
								c.getCombat().appendMultiBarrageNPC(j, c.magicFailed);
								Server.npcHandler.attackPlayer(p, j);
							}
						}
					}
				}
				if (NPCHandler.NPCS.get(i).HP - damage < 0) {
					damage = NPCHandler.NPCS.get(i).HP;
				}
				if (c.magicDef) {
					c.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE / 2), 1);
					c.getPA().refreshSkill(1);
				}
				c.getPA().addSkillXP((Player.MAGIC_SPELLS[c.oldSpellId][7] + damage * Constants.MAGIC_EXP_RATE), 6);
				c.getPA().addSkillXP((Player.MAGIC_SPELLS[c.oldSpellId][7] + damage * Constants.MAGIC_EXP_RATE / 3), 3);
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);
				if (c.getCombat().getEndGfxHeight() == 100 && !magicFailed) { // end
																				// GFX
					NPCHandler.NPCS.get(i).gfx100(Player.MAGIC_SPELLS[c.oldSpellId][5]);
					if (NPCHandler.NPCS.get(i).attackTimer < 5)
						NPCHandler.startAnimation(c.getCombat().npcDefenceAnim(i), i);
				} else if (!magicFailed) {
					NPCHandler.NPCS.get(i).gfx0(Player.MAGIC_SPELLS[c.oldSpellId][5]);
				}

				if (magicFailed) {
					if (NPCHandler.NPCS.get(i).attackTimer < 5) {
						NPCHandler.startAnimation(c.getCombat().npcDefenceAnim(i), i);
					}
					NPCHandler.NPCS.get(i).gfx100(85);
				}
				if (!magicFailed) {
					int freezeDelay = c.getCombat().getFreezeTime();// freeze
					if (freezeDelay > 0 && NPCHandler.NPCS.get(i).freezeTimer == 0) {
						NPCHandler.NPCS.get(i).freezeTimer = freezeDelay;
					}
					switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int heal = Misc.random(damage / 2);
						if (c.playerLevel[3] + heal >= c.getPA().getLevelForXP(c.playerXP[3])) {
							c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
						} else {
							c.playerLevel[3] += heal;
						}
						c.getPA().refreshSkill(3);
						break;
					}

				}
				NPCHandler.NPCS.get(i).underAttack = true;
				if (c.getCombat().magicMaxHit() != 0) {
					if (!c.multiAttacking) {
						NPCHandler.NPCS.get(i).damage(new Hit(damage));
						c.totalDamageDealt += damage;
					}
				}
				c.multiAttacking = false;
				c.killingNpcIndex = c.oldNpcIndex;
				NPCHandler.NPCS.get(i).updateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
				c.oldSpellId = 0;
			}
		}
	//	Degrade.degrade(c, 1);
		if (c.bowSpecShot <= 0) {
			c.oldNpcIndex = 0;
			c.projectileStage = 0;
			c.doubleHit = false;
			c.lastWeaponUsed = 0;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot >= 2) {
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot == 1) {
			// c.hitDelay = 10;
			c.bowSpecShot = 0;
		}
	}

	public static void attackNpc(Player c, int i) {
		if (NPCHandler.NPCS.get(i) != null) {
			if (NPCHandler.NPCS.get(i).isDead || NPCHandler.NPCS.get(i).MaxHP <= 0) {
				c.usingMagic = false;
				c.faceUpdate(0);
				c.npcIndex = 0;
				return;
			}
			
			NPC npc = NPCHandler.NPCS.get(i);
			c.getPA().sendFrame126(NPCHandler.getNpcListName(npc.npcType).replaceAll("_", " ")+
					"-"+npc.MaxHP+"-"+npc.HP, 35000);
			if (c.getBankPin().requiresUnlock()) {
				c.sendMessage("You cannot attack this NPC while you haven't entered your PIN.");
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (NPCHandler.KQnpc(i) && !c.getCombat().fullVeracs()) {
				c.getCombat().resetPlayerAttack();
				c.sendMessage("Your attacks have no effect on the Queen.");
				return;
			}
			if (c.isDead) {
				c.npcIndex = 0;
				return;
			}
			if (NPCHandler.NPCS.get(i).underAttackBy > 0 && NPCHandler.NPCS.get(i).underAttackBy != c.getIndex() && !NPCHandler.NPCS.get(i).inMulti()) {
				c.npcIndex = 0;
				c.sendMessage("This monster is already in combat.");
				return;
			}
			if ((c.underAttackBy > 0 || c.underAttackBy2 > 0) && c.underAttackBy2 != i && !c.inMulti()) {
				c.getCombat().resetPlayerAttack();
				c.sendMessage("I am already under attack.");
				return;
			}
			if (Boundary.isInBounds(NPCHandler.NPCS.get(i), Boundary.KRACKEN)) {
				if (NPCHandler.NPCS.get(i).npcType == 3847 && NPCHandler.isAlive(NPCHandler.getNpc(3943), Boundary.KRACKEN)) {
					c.sendMessage("You must kill the minions before you can attack the kracken.");
					c.getCombat().resetPlayerAttack();
					return;
				}
			}

			if (!c.getCombat().goodSlayer(i)) {
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (NPCHandler.NPCS.get(i).spawnedBy != c.getIndex() && NPCHandler.NPCS.get(i).spawnedBy > 0) {
				c.getCombat().resetPlayerAttack();
				c.sendMessage("This monster was not spawned for you.");
				return;
			}
			if (Boundary.isInBounds(NPCHandler.NPCS.get(i), Boundary.KRACKEN)) {
				if (NPCHandler.NPCS.get(i).npcType == 3847 && NPCHandler.isAlive(NPCHandler.getNpc(3943), Boundary.KRACKEN)) {
					c.sendMessage("You must kill the minions before you can attack the kracken.");
					c.getCombat().resetPlayerAttack();
					return;
				}
			}
			if (c.playerEquipment[Player.playerWeapon] == 1409) {
				c.spellId = 53;
				c.castingMagic = true;
			}
			if (c.playerEquipment[Player.playerWeapon] == 12346) {
				c.spellId = 52;
				c.castingMagic = true;
			}
			if (c.getX() == NPCHandler.NPCS.get(i).getX() && c.getY() == NPCHandler.NPCS.get(i).getY()) {
				c.getPA().walkTo(0, 1);
			}
			c.followId2 = i;
			c.followId = 0;
			if (c.attackTimer <= 0) {
				c.usingBow = false;
				c.usingArrows = false;
				c.usingToxicBlowpipe = false;
				c.usingOtherRangeWeapons = false;
				c.usingCross = c.playerEquipment[Player.playerWeapon] == 9185 || c.playerEquipment[Player.playerWeapon] == 12349;
				c.bonusAttack = 0;
				c.rangeItemUsed = 0;
				c.projectileStage = 0;
				if (c.autocasting) {
					c.spellId = c.autocastId;
					c.usingMagic = true;
				}
				if (c.spellId > 0 || c.playerEquipment[Player.playerWeapon] == 12346 || c.playerEquipment[Player.playerWeapon] == 1409) {
					c.usingMagic = true;
				}
				c.attackTimer = c.getCombat().getAttackDelay(c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
				c.specAccuracy = 1.0;
				c.specDamage = 1.0;
				c.delayedDamage = c.delayedDamage2 = 0;
				if (!c.usingMagic) {
					for (int bowId : Player.BOWS) {
						if (c.playerEquipment[Player.playerWeapon] == bowId) {
							c.usingBow = true;
							for (int arrowId : Player.ARROWS) {
								if (c.playerEquipment[Player.playerArrows] == arrowId) {
									c.usingArrows = true;
								}
							}
						}
					}

					for (int otherRangeId : Player.OTHER_RANGE_WEAPONS) {
						if (c.playerEquipment[Player.playerWeapon] == otherRangeId) {
							c.usingOtherRangeWeapons = true;
						}
					}
					for (int otherToxicBlowpipeId : c.TOXIC_BLOWPIPE) {
						if (c.playerEquipment[c.playerWeapon] == otherToxicBlowpipeId) {
							c.usingToxicBlowpipe = true;
							int removeDart = 1;
							if (removeDart == 1) {
								c.dartsLoaded -= 1;
								c.runedartsLoaded -= 1;
							}
							
						}
					}
				}
				if ((!Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(i).getX(), NPCHandler.NPCS.get(i).getY(), 2) && (c.getCombat().usingHally() && !c.usingOtherRangeWeapons && !c.usingBow && !c.usingMagic)) || (!Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(i).getX(), NPCHandler.NPCS.get(i).getY(), 4) && (c.usingOtherRangeWeapons && !c.usingBow && !c.usingMagic)) || (!Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(i).getX(), NPCHandler.NPCS.get(i).getY(), 1) && (!c.usingOtherRangeWeapons && !c.getCombat().usingHally() && !c.usingBow && !c.usingMagic)) || ((!Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(i).getX(), NPCHandler.NPCS.get(i).getY(), 8) && (c.usingBow || c.usingMagic)))) {
					c.attackTimer = 2;
					return;
				}

				if (!c.usingCross && !c.usingArrows && c.usingBow && (c.playerEquipment[Player.playerWeapon] < 4212 || c.playerEquipment[Player.playerWeapon] > 4223 && !c.usingMagic && c.playerEquipment[Player.playerWeapon] != 14614)) {
					c.sendMessage("You have run out of arrows!");
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}
				if (c.dartsLoaded <= 0 && c.playerEquipment[Player.playerWeapon] == 14614) {
						c.sendMessage("Your Toxic blowpipe has no darts left!");
						c.stopMovement();
						c.npcIndex = 0;
						c.dartsLoaded = 0;
					return;
				}
		/*		if(c.playerEquipment[Player.playerWeapon] == 14614 && !c.getCombat().properDarts()) { //: XXX
					c.sendMessage("You must use darts with the toxic blowpipe.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}*/
				boolean isBoss = Bosses.isBoss(NPCHandler.NPCS.get(i).npcType);
				if (isBoss && !Player.goodDistance(c.getAbsX(), c.getAbsY(), NPCHandler.NPCS.get(i).getAbsX(), NPCHandler.NPCS.get(i).getAbsY(), 5)) {
					c.sendMessage("You're standing too far away to attack this NPC.");
					c.getCombat().resetPlayerAttack(); // XXX
					c.npcIndex = 0;
					return;
				}
				if (c.getCombat().correctBowAndArrows() < c.playerEquipment[Player.playerArrows] && Constants.CORRECT_ARROWS && c.usingBow && !c.getCombat().usingCrystalBow() && c.playerEquipment[Player.playerWeapon] != 9185 && c.playerEquipment[Player.playerWeapon] != 12349) {
					c.sendMessage("You can't use " + c.getItems().getItemName(c.playerEquipment[Player.playerArrows]).toLowerCase() + "s with a " + c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase() + ".");
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}
				if (c.playerEquipment[Player.playerWeapon] == 12349 && c.playerEquipment[Player.playerWeapon] == 9185 && !c.getCombat().properBolts()) {
					c.sendMessage("You must use bolts with a crossbow.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}
				
				if (c.playerEquipment[Player.playerWeapon] == 4734 && !c.getCombat().properBoltRacks()) {
					c.sendMessage("You must use bolt racks with this bow.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.usingBow || c.castingMagic || c.usingOtherRangeWeapons || (c.getCombat().usingHally() && Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(i).getX(), NPCHandler.NPCS.get(i).getY(), 2))) {
					c.stopMovement();
				}
				if (!c.getCombat().checkMagicReqs(c.spellId)) {
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}
				if (NPCHandler.isArmadylNpc(i) && !c.usingCross && !c.usingBow && !c.usingMagic && !c.getCombat().usingCrystalBow() && !c.usingOtherRangeWeapons) {
					c.sendMessage("You can only use range against this.");
					c.getCombat().resetPlayerAttack();
					return;
				}

				c.faceUpdate(i);
				NPCHandler.NPCS.get(i).underAttackBy = c.getIndex();
				NPCHandler.NPCS.get(i).lastDamageTaken = System.currentTimeMillis();
				c.specAccuracy = 1.0;
				c.specDamage = 1.0;
				c.delayedDamage = c.delayedDamage2 = 0;
				if (c.usingSpecial && !c.usingMagic) {
					if (c.getCombat().checkSpecAmount(c.playerEquipment[Player.playerWeapon])) {
						c.lastWeaponUsed = c.playerEquipment[Player.playerWeapon];
						c.lastArrowUsed = c.playerEquipment[Player.playerArrows];
						c.getCombat().activateSpecial(c.playerEquipment[Player.playerWeapon], i);
						return;
					} else {
						c.sendMessage("You don't have the required special energy to use this attack.");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						c.npcIndex = 0;
						return;
					}
				}
				c.specMaxHitIncrease = 0;
				if (c.playerLevel[3] > 0 && !c.isDead && NPCHandler.NPCS.get(i).MaxHP > 0) {
					if (!c.usingBow && !c.usingMagic && !c.usingOtherRangeWeapons && !c.usingCross && !c.usingArrows) {
						c.startAnimation(c.getCombat().getWepAnim(c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase()));
						if (NPCHandler.NPCS.get(i).attackTimer < 5) {
							NPCHandler.startAnimation(c.getCombat().npcDefenceAnim(i), i);
						}
					} else {
						if (!c.usingBow && !c.usingOtherRangeWeapons) {
							c.startAnimation(Player.MAGIC_SPELLS[c.spellId][2]);
						} else {
							c.startAnimation(c.getCombat().getWepAnim(c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase()));
						}
					}
				}
				c.lastWeaponUsed = c.playerEquipment[Player.playerWeapon];
				c.lastArrowUsed = c.playerEquipment[Player.playerArrows];

				if (!c.usingBow && !c.usingMagic && !c.usingOtherRangeWeapons) { // melee
					c.followId2 = NPCHandler.NPCS.get(i).getIndex();
					c.getPA().followNpc();
					c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
					c.delayedDamage = Misc.random(c.getCombat().calculateMeleeMaxHit());
					c.projectileStage = 0;
					c.oldNpcIndex = i;
				}
				if (c.usingBow && !c.usingOtherRangeWeapons && !c.usingMagic || c.usingCross) { // range
					if (c.usingCross)
						c.usingBow = true;
					if (c.fightMode == 2)
						c.attackTimer--;
					c.followId2 = NPCHandler.NPCS.get(i).getIndex();
					c.getPA().followNpc();
					c.lastArrowUsed = c.playerEquipment[Player.playerArrows];
					c.lastWeaponUsed = c.playerEquipment[Player.playerWeapon];
					c.gfx100(c.getCombat().getRangeStartGFX());
					c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if (c.playerEquipment[Player.playerWeapon] >= 4212 && c.playerEquipment[Player.playerWeapon] <= 4223) {
						c.rangeItemUsed = c.playerEquipment[Player.playerWeapon];
						c.crystalBowArrowCount++;
						c.lastArrowUsed = 0;
					} else {
						c.rangeItemUsed = c.playerEquipment[Player.playerArrows];
						c.getItems().deleteArrow();
					}
					if (NPCHandler.NPCS.get(i).HP > 0) {
						c.gfx100(c.getCombat().getRangeStartGFX());
						c.getCombat().fireProjectileNpc();
					}
				}

				if (c.usingOtherRangeWeapons && !c.usingMagic && !c.usingBow) {
					c.followId2 = NPCHandler.NPCS.get(i).getIndex();
					c.getPA().followNpc();
					c.rangeItemUsed = c.playerEquipment[Player.playerWeapon];
					c.getItems().deleteEquipment(); // here
					c.gfx100(c.getCombat().getRangeStartGFX());
					c.lastArrowUsed = 0;
					c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if (c.fightMode == 2)
						c.attackTimer--;
					c.getCombat().fireProjectileNpc();
				}
				if (c.usingMagic) { // magic hit delay
					int pX = c.getX();
					int pY = c.getY();
					int nX = NPCHandler.NPCS.get(i).getX();
					int nY = NPCHandler.NPCS.get(i).getY();
					int offX = (pY - nY) * -1;
					int offY = (pX - nX) * -1;
					c.castingMagic = true;
					c.projectileStage = 2;
					c.stopMovement();
					if (Player.MAGIC_SPELLS[c.spellId][3] > 0) {
						if (c.getCombat().getStartGfxHeight() == 100) {
							c.gfx100(Player.MAGIC_SPELLS[c.spellId][3]);
						} else {
							c.gfx0(Player.MAGIC_SPELLS[c.spellId][3]);
						}
					}
					if (Player.MAGIC_SPELLS[c.spellId][4] > 0) {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, Player.MAGIC_SPELLS[c.spellId][4], c.getCombat().getStartHeight(), c.getCombat().getEndHeight(), i + 1, 50);
					}
					c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
					c.oldNpcIndex = i;
					c.oldSpellId = c.spellId;
					if (c.playerEquipment[Player.playerWeapon] == 12346) {
						return;
					}
					c.spellId = 0;
					if (!c.autocasting)
						c.npcIndex = 0;
				}
				if (c.playerEquipment[3] == 15052) {
					if (c.tentacleHits != 0)
						c.tentacleHits--;
					else if (c.tentacleHits == 0) {
						c.playerEquipment[3] = -1;
						c.playerEquipmentN[3] = 0;
						c.sendMessage("Your abyssal tentacle has degraded");
						if (!c.getItems().addItem(15372, 1)) {
							c.getItems().addItemToBank(15372, 1);
							c.sendMessage("You had no room so the tentacle was placed in your bank.");
						}
						c.tentacleHits = 200000000;
					}	
				}
				if (c.usingBow && Constants.CRYSTAL_BOW_DEGRADES) { // crystal
					if (c.playerEquipment[Player.playerWeapon] == 4212) { // new
						c.getItems().wearItem(4214, 1, 3);
					}
					if (c.crystalBowArrowCount >= 250) {
						switch (c.playerEquipment[Player.playerWeapon]) {

						case 4223: // 1/10 bow
							c.getItems().wearItem(4207, 1, 3);
							c.sendMessage("Your crystal bow has fully degraded.");
							c.crystalBowArrowCount = 0;
							break;

						default:
							c.getItems().wearItem(++c.playerEquipment[Player.playerWeapon], 1, 3);
							c.sendMessage("Your crystal bow degrades.");
							c.crystalBowArrowCount = 0;
							break;
						}
					}
				}
				c.getPA().sendFrame126(NPCHandler.getNpcListName(npc.npcType).replaceAll("_", " ")+
						"-"+npc.MaxHP+"-"+npc.HP, 35000);
			}
		}
	}
}