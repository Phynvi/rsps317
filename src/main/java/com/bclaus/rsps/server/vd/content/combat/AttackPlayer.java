package com.bclaus.rsps.server.vd.content.combat;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.content.PVPAssistant;
import com.bclaus.rsps.server.vd.content.combat.range.RangeData;
import com.bclaus.rsps.server.vd.content.consumables.Potions;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.vd.mobile.PoisonCombatTask;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;

public class AttackPlayer {

	public static void applyPlayerHit(final Player c, final int i, Item item) {
		if (c.projectileStage == 0 && !c.usingMagic && !c.castingMagic) {
			if (c.doubleHit) {
				c.getCombat().applyPlayerMeleeDamage(i, 1, Misc.random(c.getCombat().calculateMeleeMaxHit()));
				c.getCombat().applyPlayerMeleeDamage(i, 2, Misc.random(c.getCombat().calculateMeleeMaxHit()));
			} else {
				c.getCombat().applyPlayerMeleeDamage(i, 1, Misc.random(c.getCombat().calculateMeleeMaxHit()));
			}
			if (c.playerEquipment[Player.playerHat] != 14613)
				PoisonCombatTask.getPoisonType(item).ifPresent(World.PLAYERS.get(i)::poison);
		}
	}

	public static void applyPlayerMeleeDamage(Player c, int i, int damageMask, int damage) {
		c.previousDamage = damage;
		Player o = World.PLAYERS.get(i);
		if (o == null) {
			return;
		}
		boolean veracsEffect = false;
		boolean guthansEffect = false;
		if (c.getCombat().fullVeracs()) {
			if (Misc.random(3) == 1) {
				veracsEffect = true;
			}
		}
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
		if (Misc.random(o.getCombat().calculateMeleeDefence()) > Misc.random(c.getCombat().calculateMeleeAttack()) && !veracsEffect) {
			damage = 0;
			c.bonusAttack = 0;
		} else if (c.playerEquipment[Player.playerWeapon] == 5698 || (c.playerEquipment[Player.playerWeapon] == 16022 && o.poisonDamage <= 0 && Misc.random(3) == 1)) {
			c.bonusAttack += damage / 3;
		} else {
			c.bonusAttack += damage / 3;
		}
		if (o.prayerActive[18] && System.currentTimeMillis() - o.protMeleeDelay > 1500 && !veracsEffect) { // if
			damage = damage * 60 / 100;
		}
		if (damage > 0 && guthansEffect) {
			c.playerLevel[3] += damage;
			if (c.playerLevel[3] > Player.getLevelForXP(c.playerXP[3]))
				c.playerLevel[3] = Player.getLevelForXP(c.playerXP[3]);
			c.getPA().refreshSkill(3);
			o.gfx0(398);
		}
		if (c.ssSpec && damageMask == 2) {
			damage = 5 + Misc.random(11);
			c.ssSpec = false;
		}
		if (World.PLAYERS.get(i).playerLevel[3] - damage < 0) {
			damage = World.PLAYERS.get(i).playerLevel[3];
		}
		if (o.vengOn && damage > 0)
			c.getCombat().appendVengeance(i, damage);
		if (damage > 0) {
			c.getCombat().applyRecoil(damage, i);
			// c.getCombat().applySpiritShield(damage, i);
			PVPAssistant.increasePotential(c, damage);
		}
		switch (c.specEffect) {
		case 1: // dragon scimmy special
			if (damage > 0) {
				if (o.prayerActive[16] || o.prayerActive[17] || o.prayerActive[18]) {
					o.headIcon = -1;
					o.getPA().sendFrame36(c.PRAYER_GLOW[16], 0);
					o.getPA().sendFrame36(c.PRAYER_GLOW[17], 0);
					o.getPA().sendFrame36(c.PRAYER_GLOW[18], 0);
				}
				o.sendMessage("You have been injured!");
				o.stopPrayerDelay = System.currentTimeMillis();
				o.prayerActive[16] = false;
				o.prayerActive[17] = false;
				o.prayerActive[18] = false;
				o.getPA().requestUpdates();
			}
			break;
		case 2:
			if (damage > 0) {
				if (o.freezeTimer <= 0)
					o.freezeTimer = 30;
				o.gfx0(369);
				o.sendMessage("You have been frozen.");
				o.frozenBy = c.playerId;
				o.stopMovement();
				c.sendMessage("You freeze your enemy.");
			}
			break;
		case 3:
			if (damage > 0) {
				o.playerLevel[1] -= damage;
				o.sendMessage("You feel weak.");
				if (o.playerLevel[1] < 1)
					o.playerLevel[1] = 1;
				c.getPA().refreshSkill(1);
			}
			break;
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
			break;
		}
		c.specEffect = 0;

		if (c.fightMode == 3) { // check
			c.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE / 3), 2);
			c.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE / 3), 3);
			c.getPA().refreshSkill(2);
			c.getPA().refreshSkill(3);
		} else {
			c.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE), c.fightMode);
			c.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE / 3), 3);
			c.getPA().refreshSkill(c.fightMode);
			c.getPA().refreshSkill(3);
		}
		World.PLAYERS.get(i).logoutDelay = System.currentTimeMillis();
		World.PLAYERS.get(i).underAttackBy = c.getIndex();
		World.PLAYERS.get(i).killerId = c.getIndex();
		World.PLAYERS.get(i).singleCombatDelay = System.currentTimeMillis();
		if (c.killedBy != World.PLAYERS.get(i).getIndex())
			c.totalPlayerDamageDealt = 0;
		c.killedBy = World.PLAYERS.get(i).getIndex();
		c.getCombat().applySmite(i, damage);
		switch (damageMask) {
		case 1:
			World.PLAYERS.get(i).damage(new Hit(damage));
			c.totalDamageDealt += damage;
			World.PLAYERS.get(i).addDamageReceived(c.playerName, damage);
			break;

		case 2:
			World.PLAYERS.get(i).damage(new Hit(damage));
			c.totalDamageDealt += damage;
			World.PLAYERS.get(i).addDamageReceived(c.playerName, damage);
			break;
		}
	}

	public static void playerDelayedHit(final Player c, final int i, Item item) {
		if (World.PLAYERS.get(i) != null) {
			if (World.PLAYERS.get(i).isDead || c.isDead || World.PLAYERS.get(i).playerLevel[3] <= 0 || c.playerLevel[3] <= 0) {
				c.playerIndex = 0;
				return;
			}
			if (World.PLAYERS.get(i).isDead) {
				c.faceUpdate(0);
				c.playerIndex = 0;
				return;
			}
			Player o = World.PLAYERS.get(i);
			o.getPA().removeAllWindows();

			if (o.playerIndex <= 0 && o.npcIndex <= 0 || o.playerIndex == c.getIndex()) {
				if (o.autoRetaliate) {
					o.playerIndex = c.getIndex();
				}
			}
			if (o.attackTimer <= 3 || o.attackTimer == 0 && o.playerIndex == 0 && !c.castingMagic) { // block
																										// animation
				o.startAnimation(o.getCombat().getBlockEmote());
			}
			if (o.inTrade) {
				o.getTradeAndDuel().declineTrade();
			}
			if (c.projectileStage == 0 && !c.usingMagic) { // melee hit damage
				c.getCombat().applyPlayerHit(c, i, item);
			}
			if (!c.castingMagic && c.projectileStage > 0) { // range hit damage
				int damage = Misc.random(c.getCombat().rangeMaxHit());
				int damage2 = -1;
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1) {
					damage2 = Misc.random(c.getCombat().rangeMaxHit());
				}
				c.rangeEndGFX = RangeData.getRangeEndGFX(c);

				if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc.random(10 + c.getCombat().calculateRangeAttack()) && !c.ignoreDefence) {
					damage = 0;
				}
				if (c.playerEquipment[3] == 12349) {
					damage *= 1.30;
				}
				if (c.playerEquipment[3] == 14614) {
					damage *= 1.45;
				}
				if (c.playerEquipment[3] == 9185 || c.playerEquipment[3] == 12349) {
					Player p = World.PLAYERS.get(i);
					if (p == null) {
						return;
					}
					if (Misc.random(10) == 1) {
						if (damage > 0) {
							c.boltDamage = damage;
							c.getCombat().crossbowSpecial(c, i);
							damage *= c.crossbowDamage;
						}
					}
				}
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1) {
					if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc.random(10 + c.getCombat().calculateRangeAttack()))
						damage2 = 0;
				}

				if (c.dbowSpec) {
					o.gfx100(c.lastArrowUsed == 11212 ? 1100 : 1103);
					if (damage < 8)
						damage = 8;
					if (damage2 < 8)
						damage2 = 8;
					c.dbowSpec = false;
				}

				if (o.prayerActive[17] && System.currentTimeMillis() - o.protRangeDelay > 1500) { // if
																									// prayer
																									// active
																									// reduce
																									// damage
																									// by
																									// half
					damage = damage * 60 / 100;
					if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1)
						damage2 = damage2 * 60 / 100;
				}
				if (World.PLAYERS.get(i).playerLevel[3] - damage < 0) {
					damage = World.PLAYERS.get(i).playerLevel[3];
				}
				if (World.PLAYERS.get(i).playerLevel[3] - damage - damage2 < 0) {
					damage2 = World.PLAYERS.get(i).playerLevel[3] - damage;
				}
				if (damage < 0)
					damage = 0;
				if (damage2 < 0 && damage2 != -1)
					damage2 = 0;
				if (o.vengOn) {
					c.getCombat().appendVengeance(i, damage);
					c.getCombat().appendVengeance(i, damage2);
				}
				if (damage > 0)
					c.getCombat().applyRecoil(damage, i);
				if (damage2 > 0)
					c.getCombat().applyRecoil(damage2, i);
				PVPAssistant.increasePotential(c, damage);
				if (damage2 > 0)
					PVPAssistant.increasePotential(c, damage2);
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
				if (c.playerEquipment[Player.playerShield] == 15000 && Misc.random(5) == 0) {
					if (c.playerLevel[5] > 0) {
						if (c.playerLevel[3] > 4) {
							double damageRecieved = damage * 0.85;
							int prayerLost = (int) (damage * 0.3);
							if (c.playerLevel[5] >= prayerLost) {
								damage = (int) damageRecieved;
								c.playerLevel[5] -= prayerLost;
								if (c.playerLevel[5] < 0)
									c.playerLevel[5] = 0;
								c.playerLevel[5] -= prayerLost;
								c.getPA().refreshSkill(5);
							}
						}
					}
				}
				if (c.playerEquipment[Player.playerShield] == 15002 && Misc.random(6) == 2) {
					double damageRecieved = damage * 0.91;
					damage = (int) damageRecieved;
				}
				if (dropArrows) {
					c.getItems().dropArrowPlayer();
				}
				if (c.rangeEndGFX > 0 && !c.getCombat().usingBolts(c.lastArrowUsed)) {
					if (c.rangeEndGFXHeight) {
						o.gfx100(c.rangeEndGFX);
					} else {
						o.gfx0(c.rangeEndGFX);
					}
				}
				World.PLAYERS.get(i).underAttackBy = c.getIndex();
				World.PLAYERS.get(i).logoutDelay = System.currentTimeMillis();
				World.PLAYERS.get(i).singleCombatDelay = System.currentTimeMillis();
				World.PLAYERS.get(i).killerId = c.getIndex();
				World.PLAYERS.get(i).addDamageReceived(c.playerName, damage);
				c.killedBy = World.PLAYERS.get(i).getIndex();
				World.PLAYERS.get(i).damage(new Hit(damage));
				c.ignoreDefence = false;
				if (damage2 != -1) {
					World.PLAYERS.get(i).damage(new Hit(damage2));
					World.PLAYERS.get(i).addDamageReceived(c.playerName, damage2);
				}
				o.getPA().refreshSkill(3);
				World.PLAYERS.get(i).updateRequired = true;
				c.getCombat().applySmite(i, damage);
				if (damage2 != -1)
					c.getCombat().applySmite(i, damage2);

			} else if (c.projectileStage > 0) { // magic hit damageno0b
				int damage = 0;
				if (c.spellSwap) {
					c.spellSwap = false;
					c.setSidebarInterface(6, 16640);
					c.playerMagicBook = 2;
					c.gfx0(-1);
				}

				damage = Misc.random(c.getCombat().magicMaxHit());

				if (c.getCombat().godSpells()) {
					if (System.currentTimeMillis() - c.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
						damage += 10;
					}
				}
				if (c.magicFailed)
					damage = 0;

				if (o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500) { // if
																									// prayer
																									// active
																									// reduce
																									// damage
																									// by
																									// half
					damage = damage * 60 / 100;
				}
				if (World.PLAYERS.get(i).playerLevel[3] - damage < 0) {
					damage = World.PLAYERS.get(i).playerLevel[3];
				}
				if (o.vengOn)
					c.getCombat().appendVengeance(i, damage);
				if (damage > 0)
					c.getCombat().applyRecoil(damage, i);
				if (c.magicDef) {
					c.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE / 3), 1);
					c.getPA().refreshSkill(1);
				}
				c.getPA().addSkillXP((Player.MAGIC_SPELLS[c.oldSpellId][7] + damage * Constants.MAGIC_EXP_RATE), 6);
				c.getPA().addSkillXP((Player.MAGIC_SPELLS[c.oldSpellId][7] + damage * Constants.MAGIC_EXP_RATE / 3), 3);
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);

				if (c.getCombat().getEndGfxHeight() == 100 && !c.magicFailed) { // end
																				// GFX
					World.PLAYERS.get(i).gfx100(Player.MAGIC_SPELLS[c.oldSpellId][5]);
				} else if (!c.magicFailed) {
					World.PLAYERS.get(i).gfx0(Player.MAGIC_SPELLS[c.oldSpellId][5]);
				} else if (c.magicFailed) {
					World.PLAYERS.get(i).gfx100(85);
				}

				if (!c.magicFailed) {
					if (System.currentTimeMillis() - World.PLAYERS.get(i).reduceStat > 35000) {
						World.PLAYERS.get(i).reduceStat = System.currentTimeMillis();
						switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
						case 12987:
						case 13011:
						case 12999:
						case 13023:
							World.PLAYERS.get(i).playerLevel[0] -= ((c.getPA().getLevelForXP(World.PLAYERS.get(i).playerXP[0]) * 10) / 100);
							break;
						}
					}

					switch (Player.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 12445: // teleblock
						if (System.currentTimeMillis() - o.teleBlockDelay > o.teleBlockLength) {
							o.teleBlockDelay = System.currentTimeMillis();
							o.sendMessage("You have been teleblocked.");
							o.putInCombat(1);
							if (o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500)
								o.teleBlockLength = 150000;
							else
								o.teleBlockLength = 300000;
						}
						break;

					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int heal = damage / 4;
						if (c.playerLevel[3] + heal > c.getPA().getLevelForXP(c.playerXP[3])) {
							c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
						} else {
							c.playerLevel[3] += heal;
						}
						c.getPA().refreshSkill(3);
						break;

					case 1153:
						World.PLAYERS.get(i).playerLevel[0] -= ((c.getPA().getLevelForXP(World.PLAYERS.get(i).playerXP[0]) * 5) / 100);
						o.sendMessage("Your attack level has been reduced!");
						World.PLAYERS.get(i).reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;

					case 1157:
						World.PLAYERS.get(i).playerLevel[2] -= ((c.getPA().getLevelForXP(World.PLAYERS.get(i).playerXP[2]) * 5) / 100);
						o.sendMessage("Your strength level has been reduced!");
						World.PLAYERS.get(i).reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;

					case 1161:
						World.PLAYERS.get(i).playerLevel[1] -= ((c.getPA().getLevelForXP(World.PLAYERS.get(i).playerXP[1]) * 5) / 100);
						o.sendMessage("Your defence level has been reduced!");
						World.PLAYERS.get(i).reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;

					case 1542:
						World.PLAYERS.get(i).playerLevel[1] -= ((c.getPA().getLevelForXP(World.PLAYERS.get(i).playerXP[1]) * 10) / 100);
						o.sendMessage("Your defence level has been reduced!");
						World.PLAYERS.get(i).reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;

					case 1543:
						World.PLAYERS.get(i).playerLevel[2] -= ((c.getPA().getLevelForXP(World.PLAYERS.get(i).playerXP[2]) * 10) / 100);
						o.sendMessage("Your strength level has been reduced!");
						World.PLAYERS.get(i).reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;

					case 1562:
						World.PLAYERS.get(i).playerLevel[0] -= ((c.getPA().getLevelForXP(World.PLAYERS.get(i).playerXP[0]) * 10) / 100);
						o.sendMessage("Your attack level has been reduced!");
						World.PLAYERS.get(i).reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;
					}
				}
				PVPAssistant.increasePotential(c, damage);
				World.PLAYERS.get(i).logoutDelay = System.currentTimeMillis();
				World.PLAYERS.get(i).underAttackBy = c.getIndex();
				World.PLAYERS.get(i).killerId = c.getIndex();
				World.PLAYERS.get(i).singleCombatDelay = System.currentTimeMillis();
				if (c.getCombat().magicMaxHit() != 0 && !c.magicFailed) {
					World.PLAYERS.get(i).damage(new Hit(damage));
					World.PLAYERS.get(i).addDamageReceived(c.playerName, damage);
					c.totalPlayerDamageDealt += damage;
				}
				c.getCombat().applySmite(i, damage);
				c.killedBy = World.PLAYERS.get(i).getIndex();
				c.getPA().refreshSkill(3);
				World.PLAYERS.get(i).updateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
				if (o.inMulti() && c.getCombat().multis()) {
					c.barrageCount = 0;
					for (int j = 0; j < World.PLAYERS.capacity(); j++) {
						if (World.PLAYERS.get(j) != null) {
							if (j == o.getIndex())
								continue;
							if (c.barrageCount >= 9)
								break;
							if (Player.goodDistance(o.getX(), o.getY(), World.PLAYERS.get(j).getX(), World.PLAYERS.get(j).getY(), 1))
								c.getCombat().appendMultiBarrage(j, c.magicFailed);
						}
					}
				}
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);
				c.oldSpellId = 0;
			}
		}
		c.getPA().requestUpdates();
		//Degrade.degrade(c, 3);
		if (c.bowSpecShot <= 0) {
			c.oldPlayerIndex = 0;
			c.projectileStage = 0;
			c.lastWeaponUsed = 0;
			c.doubleHit = false;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot != 0) {
			c.bowSpecShot = 0;
		}
	}

	private static void isAttackable(Player player) {
		if (player.inTask)
			return;
		Server.getTaskScheduler().schedule(new ScheduledTask(60) {
			@Override
			public void execute() {
				this.stop();

			}

			public void onStop() {
				player.sendMessage("You're now attackable..");
				player.attackable = true;
				player.inTask = false;
			}
		}.attach(player));
	}

	@SuppressWarnings("static-access")
	public static void attackPlayer(Player c, int i) {
		if (World.PLAYERS.get(i) != null) {
			if (World.PLAYERS.get(i).isDead) {
				c.getCombat().resetPlayerAttack();
				return;
			}
			Player target = World.PLAYERS.get(i);
			c.getPA().sendFrame126(target.playerName+
					"-"+c.getPA().getLevelForXP(target.playerXP[Player.playerHitpoints])+"-"+target.playerLevel[Player.playerHitpoints], 35000);
			if (!c.inWild() && !c.inArena() && !c.inFightCaves())
				return;
			if (c.isDead || World.PLAYERS.get(i).isDead) {
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (!c.getCombat().checkReqs()) {
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				c.getBankPin().open(2);
				c.getCombat().resetPlayerAttack();
				return;
			}

			if (World.PLAYERS.get(i).getBankPin().requiresUnlock()) {
				if (World.PLAYERS.get(i).attackable) {

				} else {
					isAttackable(World.PLAYERS.get(i));
					World.PLAYERS.get(i).inTask = true;
					c.sendMessage("The other player needs to unlock the account before they can be in combat.");
					c.sendMessage("The account will become attackable after 1 minute");
					c.getCombat().resetPlayerAttack();
					return;
				}
			}
			/*if (c.totalPlaytime() < 6200) {
				c.sendMessage("You are still learning the ropes, please wait at least an hour before pking");
				c.getCombat().resetPlayerAttack();
				return;
			}*/
			Player o = World.PLAYERS.get(i);
			/*if (PKHandler.isSameConnection(c, o) && c.playerRights != 5) {
				c.sendMessage("You cannot attack players on the same connection as you.");
				c.getCombat().resetPlayerAttack();
				return;
			}*/
			if (!Player.tradeEnabled) {
				c.sendMessage("The server is restricting all PKING");
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (c.playerEquipment[Player.playerWeapon] == 3281) {
				c.sendMessage("You cannot use the Legend's whip in the wilderness");
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (!c.getAccount().getType().attackableTypes().contains(o.getAccount().getType().alias())) {
				c.sendMessage("You cannot attack this player. Your account type does not permit you");
				c.sendMessage("to attack " + o.getAccount().getType().alias() + " accounts.");
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (!o.getAccount().getType().attackableTypes().contains(c.getAccount().getType().alias())) {
				c.sendMessage("You cannot attack this player. Their account type does not permit you");
				c.sendMessage("to attack " + o.getAccount().getType().alias() + " accounts.");
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (World.PLAYERS.get(i).isDead) {
				World.PLAYERS.get(i).playerIndex = 0;
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (World.PLAYERS.get(i).heightLevel != c.heightLevel) {
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (c.getCreaturePotionTimer() > 0) {
				Potions.resetCreatureCombat(c);
				c.getCombat().resetPlayerAttack();
				return;
			}
			boolean sameSpot = c.absX == World.PLAYERS.get(i).getX() && c.absY == World.PLAYERS.get(i).getY();
			if (!Player.goodDistance(World.PLAYERS.get(i).getX(), World.PLAYERS.get(i).getY(), c.getX(), c.getY(), 25) && !sameSpot) {
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (c.duelingWith != 0) {
				if (c.duelingWith != i) {
					c.sendMessage("You can only attack your opponent.");
					c.getCombat().resetPlayerAttack();
					return;
				}
			}
			c.followId = i;
			c.followId2 = 0;
			if (c.attackTimer <= 0) {
				c.usingBow = false;
				c.specEffect = 0;
				c.usingRangeWeapon = false;
				c.rangeItemUsed = 0;
				c.usingBow = false;
				c.usingArrows = false;
				c.usingToxicBlowpipe = false;
				c.usingOtherRangeWeapons = false;
				c.usingCross = c.playerEquipment[Player.playerWeapon] == 9185 || c.playerEquipment[Player.playerWeapon] == 12349;
				c.projectileStage = 0;
				if (c.absX == World.PLAYERS.get(i).absX && c.absY == World.PLAYERS.get(i).absY) {
					if (c.freezeTimer > 0) {
						c.getCombat().resetPlayerAttack();
						return;
					}
					c.followId = i;
					c.attackTimer = 0;
					return;
				}
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
				}
				if (c.usingToxicBlowpipe) {
					if (c.dartsLoaded >= 1) {
					int removeDart = 1;
					if (removeDart == 1) {
						c.dartsLoaded -= 1;
					}
					//c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
					//c.getItems().deleteEquipment();
					c.startAnimation(5061);
					c.usingRangeWeapon = true;
					c.getPA().followPlayer();
					c.gfx100(c.getCombat().getRangeStartGFX());
					if (c.fightMode == 2)
						c.attackTimer--;
					c.hitDelay = c.getCombat().getHitDelay(
							i,
							c.getItems()
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.projectileStage = 1;
					c.oldPlayerIndex = i;
					c.getCombat().fireProjectilePlayer();
					} else if (c.dartsLoaded >= 1) {
						c.dartsLoaded -= 1;
						//c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
					//c.getItems().deleteEquipment();
					c.startAnimation(5061);
					c.usingRangeWeapon = true;
					c.getPA().followPlayer();
					c.gfx100(c.getCombat().getRangeStartGFX());
					if (c.fightMode == 2)
						c.attackTimer--;
					c.hitDelay = c.getCombat().getHitDelay(i,
					c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldPlayerIndex = i;
					c.getCombat().fireProjectilePlayer();
					} else
						c.sendMessage("You must load your blowpipe with darts in order to attack another Player.");
						return;
				}
				if (c.autocasting) {
					c.spellId = c.autocastId;
					c.usingMagic = true;
				}
				if (c.spellId > 0) {
					c.usingMagic = true;
				}
				c.attackTimer = c.getCombat().getAttackDelay(c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());

				if (c.duelRule[9]) {
					boolean canUseWeapon = false;
					for (int funWeapon : Constants.FUN_WEAPONS) {
						if (c.playerEquipment[Player.playerWeapon] == funWeapon) {
							canUseWeapon = true;
						}
					}
					if (!canUseWeapon) {
						c.sendMessage("You can only use DDS & Whip in this duel.");
						c.getCombat().resetPlayerAttack();
						return;
					}
				}
				if (c.duelRule[2] && (c.usingBow || c.usingOtherRangeWeapons)) {
					c.sendMessage("Range has been disabled in this duel!");
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.duelRule[3] && (!c.usingBow && !c.usingOtherRangeWeapons && !c.usingMagic)) {
					c.sendMessage("Melee has been disabled in this duel!");
					c.getCombat().resetPlayerAttack();
					return;
				}

				if (c.duelRule[4] && c.usingMagic) {
					c.sendMessage("Magic has been disabled in this duel!");
					c.getCombat().resetPlayerAttack();
					return;
				}

				if ((!Player.goodDistance(c.getX(), c.getY(), World.PLAYERS.get(i).getX(), World.PLAYERS.get(i).getY(), 4) && (c.usingOtherRangeWeapons && !c.usingBow && !c.usingMagic)) || (!Player.goodDistance(c.getX(), c.getY(), World.PLAYERS.get(i).getX(), World.PLAYERS.get(i).getY(), 2) && (!c.usingOtherRangeWeapons && c.getCombat().usingHally() && !c.usingBow && !c.usingMagic)) || (!Player.goodDistance(c.getX(), c.getY(), World.PLAYERS.get(i).getX(), World.PLAYERS.get(i).getY(), c.getCombat().getRequiredDistance()) && (!c.usingOtherRangeWeapons && !c.getCombat().usingHally() && !c.usingBow && !c.usingMagic)) || (!Player.goodDistance(c.getX(), c.getY(), World.PLAYERS.get(i).getX(), World.PLAYERS.get(i).getY(), 10) && (c.usingBow || c.usingMagic))) {
					c.attackTimer = 1;
					if (!c.usingBow && !c.usingMagic && !c.usingOtherRangeWeapons && c.freezeTimer > 0)
						c.getCombat().resetPlayerAttack();
					return;
				}

				if (!c.usingCross && !c.usingArrows && c.usingBow && (c.playerEquipment[Player.playerWeapon] < 4212 || c.playerEquipment[Player.playerWeapon] > 4223) && !c.usingMagic && c.playerEquipment[Player.playerWeapon] != 14614) {
					c.sendMessage("You have run out of arrows!");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.getCombat().correctBowAndArrows() < c.playerEquipment[Player.playerArrows] && Constants.CORRECT_ARROWS && c.usingBow && !c.getCombat().usingCrystalBow() && c.playerEquipment[Player.playerWeapon] != 9185 && c.playerEquipment[Player.playerWeapon] != 12349 && !c.usingMagic) {
					c.sendMessage("You can't use " + c.getItems().getItemName(c.playerEquipment[Player.playerArrows]).toLowerCase() + "s with a " + c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase() + ".");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}

				if (c.playerEquipment[Player.playerWeapon] == 9185 && c.playerEquipment[Player.playerWeapon] == 12349 && !c.getCombat().properBolts() && !c.usingMagic) {
					c.sendMessage("You must use bolts with a crossbow.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.usingBow || c.usingMagic || c.usingOtherRangeWeapons || c.getCombat().usingHally()) {
					c.stopMovement();
				}
				if (!c.getCombat().checkMagicReqs(c.spellId)) {
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c != null && c.attackedPlayers != null && World.PLAYERS.get(c.playerIndex) != null && World.PLAYERS.get(c.playerIndex).attackedPlayers != null) {
					if (!c.attackedPlayers.contains(c.playerIndex) && !World.PLAYERS.get(c.playerIndex).attackedPlayers.contains(c.getIndex())) {
						c.attackedPlayers.add(c.playerIndex);
						c.isSkulled = true;
						c.skullTimer = Constants.SKULL_TIMER;
						c.headIconPk = 0;
						c.getPA().closeAllWindows();
					}
				}
				c.faceUpdate(i + 32768);
				c.specAccuracy = 1.0;
				c.specDamage = 1.0;
				c.delayedDamage = c.delayedDamage2 = 0;
				if (c.usingSpecial && !c.usingMagic) {
					if (c.duelRule[10] && c.duelStatus == 5) {
						c.sendMessage("Special attacks have been disabled during this duel!");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						c.getCombat().resetPlayerAttack();
						return;
					}
					if (c.getCombat().checkSpecAmount(c.playerEquipment[Player.playerWeapon])) {
						c.lastArrowUsed = c.playerEquipment[Player.playerArrows];
						c.getCombat().activateSpecial(c.playerEquipment[Player.playerWeapon], i);
						c.followId = c.playerIndex;
						return;
					} else {
						c.sendMessage("You don't have the required special energy to use this attack.");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						c.playerIndex = 0;
						return;
					}
				}
				if (c.playerLevel[3] > 0 && !c.isDead && World.PLAYERS.get(i).playerLevel[3] > 0) {
					if (!c.usingMagic) {
						c.startAnimation(c.getCombat().getWepAnim(c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase()));
						c.mageFollow = false;
					} else {
						c.startAnimation(Player.MAGIC_SPELLS[c.spellId][2]);
						c.mageFollow = true;
						c.followId = c.playerIndex;
					}
				}
				World.PLAYERS.get(i).underAttackBy = c.getIndex();
				World.PLAYERS.get(i).logoutDelay = System.currentTimeMillis();
				World.PLAYERS.get(i).singleCombatDelay = System.currentTimeMillis();
				World.PLAYERS.get(i).killerId = c.getIndex();
				World.PLAYERS.get(i).updateLastCombatAction();
				c.updateLastCombatAction();
				c.lastArrowUsed = 0;
				c.rangeItemUsed = 0;
				if (!c.usingBow && !c.usingMagic && !c.usingOtherRangeWeapons) {
					c.followId = c.playerIndex;
					c.getPA().followPlayer();
					c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
					c.delayedDamage = Misc.random(c.getCombat().calculateMeleeMaxHit());
					c.projectileStage = 0;
					c.oldPlayerIndex = i;
				}
				if (c.usingBow && !c.usingOtherRangeWeapons && !c.usingMagic || c.usingCross) { // range
					if (c.playerEquipment[Player.playerWeapon] >= 4212 && c.playerEquipment[Player.playerWeapon] <= 4223) {
						c.rangeItemUsed = c.playerEquipment[Player.playerWeapon];
						c.crystalBowArrowCount++;
					} else {
						c.rangeItemUsed = c.playerEquipment[Player.playerArrows];
						c.getItems().deleteArrow();
					}
					if (c.fightMode == 2)
						c.attackTimer--;
					if (c.usingCross)
						c.usingBow = true;
					c.usingBow = true;
					c.followId = World.PLAYERS.get(c.playerIndex).getIndex();
					c.getPA().followPlayer();
					c.lastWeaponUsed = c.playerEquipment[Player.playerWeapon];
					c.lastArrowUsed = c.playerEquipment[Player.playerArrows];
					c.gfx100(c.getCombat().getRangeStartGFX());
					c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldPlayerIndex = i;
					c.getCombat().fireProjectilePlayer();
				}

				if (c.usingOtherRangeWeapons) { // knives, darts, etc hit delay
					c.rangeItemUsed = c.playerEquipment[Player.playerWeapon];
					c.getItems().deleteEquipment();
					c.usingRangeWeapon = true;
					c.followId = World.PLAYERS.get(c.playerIndex).getIndex();
					c.getPA().followPlayer();
					c.gfx100(c.getCombat().getRangeStartGFX());
					if (c.fightMode == 2)
						c.attackTimer--;
					c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldPlayerIndex = i;
					c.getCombat().fireProjectilePlayer();
				}

				if (c.usingMagic) { // magic hit delay
					int pX = c.getX();
					int pY = c.getY();
					int nX = World.PLAYERS.get(i).getX();
					int nY = World.PLAYERS.get(i).getY();
					int offX = (pY - nY) * -1;
					int offY = (pX - nX) * -1;
					c.castingMagic = true;
					c.projectileStage = 2;
					if (Player.MAGIC_SPELLS[c.spellId][3] > 0) {
						if (c.getCombat().getStartGfxHeight() == 100) {
							c.gfx100(Player.MAGIC_SPELLS[c.spellId][3]);
						} else {
							c.gfx0(Player.MAGIC_SPELLS[c.spellId][3]);
						}
					}
					if (Player.MAGIC_SPELLS[c.spellId][4] > 0) {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, Player.MAGIC_SPELLS[c.spellId][4], c.getCombat().getStartHeight(), c.getCombat().getEndHeight(), -i - 1, c.getCombat().getStartDelay());
					}
					if (c.autocastId > 0) {
						c.followId = c.playerIndex;
						c.followDistance = 5;
					}
					c.hitDelay = c.getCombat().getHitDelay(i, c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
					c.oldPlayerIndex = i;
					c.oldSpellId = c.spellId;
					c.spellId = 0;

					if (Player.MAGIC_SPELLS[c.oldSpellId][0] == 12891 && o.isMoving) {
						// c.sendMessage("Barrage projectile..");
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 85, 368, 25, 25, -i - 1, c.getCombat().getStartDelay());
					}
					if (Misc.random(o.getCombat().mageDef()) > Misc.random(c.getCombat().mageAtk())) {
						c.magicFailed = true;
					} else {
						c.magicFailed = false;
					}
					int freezeDelay = c.getCombat().getFreezeTime();// freeze
																	// time
					if (freezeDelay > 0 && World.PLAYERS.get(i).freezeTimer <= -3 && !c.magicFailed) {
						World.PLAYERS.get(i).freezeTimer = freezeDelay;
						o.resetWalkingQueue();
						o.sendMessage("You have been frozen.");
						o.frozenBy = c.getIndex();
					}
					if (!c.autocasting && c.spellId <= 0)
						c.playerIndex = 0;
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
						c.tentacleHits = 5000;
					}	
				}
				if (c.usingBow && Constants.CRYSTAL_BOW_DEGRADES) { // crystal
					if (c.playerEquipment[Player.playerWeapon] == 4212) { // new
						c.getItems().wearItem(4214, 1, 3);
					}

					if (c.crystalBowArrowCount >= 250) {
						switch (c.playerEquipment[Player.playerWeapon]) {

						case 4223: // 1/10 bow
							c.getItems().wearItem(-1, 1, 3);
							c.sendMessage("Your crystal bow has fully degraded.");
							if (!c.getItems().addItem(4207, 1)) {
								Server.itemHandler.createGroundItem(c, 4207, c.getX(), c.getY(), 1, c.getIndex());
							}
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
			}
			c.getPA().sendFrame126(target.playerName+
					"-"+c.getPA().getLevelForXP(target.playerXP[Player.playerHitpoints])+"-"+target.playerLevel[Player.playerHitpoints], 35000);
		}
	}
}