/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bclaus.rsps.server.vd.content.combat.melee;

import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.items.ItemAssistant;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;

/**
 * 
 * @author Tim/Someone
 */
public class MeleeSpecial {

	public static boolean checkSpecAmount(Player c, int weapon) {
		switch (weapon) {
		case 1249:
		case 1215:
		case 1231:
		case 5680:
		case 5698:
		case 14611:
		case 1305:
		case 10887:
		case 1434:
			if (c.specAmount >= 2.5) {
				c.specAmount -= 2.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 4151:
		case 13758:
		case 13764:
		case 13740:
		case 13741:
		case 15052:
		case 15053:
		case 3281:
		case 11694:
		case 11698:
		case 4153:
		case 14484:
		case 13047:
		case 13576:
		case 13049:
		case 13045:
			if (c.specAmount >= 5) {
				c.specAmount -= 5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 3204:
			if (c.specAmount >= 3) {
				c.specAmount -= 3;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 1377:
		case 11696:
		case 11730:
		case 7158:
			if (c.specAmount >= 10) {
				c.specAmount -= 10;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 4587:
		case 14610:
		case 859:
		case 861:
		case 14614:
		case 11235:
		case 11700:
			if (c.specAmount >= 5.5) {
				c.specAmount -= 5.5;
				c.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		default:
			return true; // incase u want to test a weapon
		}
	}

	public static void activateSpecial(Player c, int weapon, int i) {

		if (NPCHandler.NPCS.get(i) == null && c.npcIndex > 0) {
			return;
		}
		if (World.PLAYERS.get(i) == null && c.playerIndex > 0) {
			return;
		}
		c.doubleHit = false;
		c.specEffect = 0;
		c.projectileStage = 0;
		c.specMaxHitIncrease = 2;
		if (c.npcIndex > 0) {
			c.oldNpcIndex = i;
		} else if (c.playerIndex > 0) {
			c.oldPlayerIndex = i;
			World.PLAYERS.get(i).underAttackBy = c.getIndex();
			World.PLAYERS.get(i).logoutDelay = System.currentTimeMillis();
			World.PLAYERS.get(i).singleCombatDelay = System.currentTimeMillis();
			World.PLAYERS.get(i).killerId = c.getIndex();
		}
		switch (weapon) {
		case 10887:
			c.gfx0(1027);
			c.startAnimation(5870);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.specDamage = 1.20;
			c.specAccuracy = 1.85;
			break;
			
		case 13576:
			c.gfx0(1213);
			c.startAnimation(406);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.specDamage = 1.60;
			c.specAccuracy = 1.99;
			break;

		case 1305: // dragon long
			c.gfx100(248);
			c.startAnimation(1058);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.specAccuracy = 1.10;
			c.specDamage = 1.20;
			break;
		case 1231:
			c.gfx100(252);
			c.startAnimation(1062);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.doubleHit = true;
			c.specAccuracy = 2.70;
			c.specDamage = 1.20;
			break;
		case 1215: // dragon daggers
		case 5680:
		case 5698:
		case 14611:
			c.gfx100(252);
			c.startAnimation(1062);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.doubleHit = true;
			c.specAccuracy = 1.60;
			c.specDamage = 1.20;
			break; //abyssal dagger
		case 13047:
		case 13049:
			c.gfx100(252);
			c.startAnimation(1062);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.doubleHit = true;
			c.specAccuracy = 1.60;
			c.specDamage = 1.20;
			break;
		case 11730:
			c.gfx100(1224);
			c.startAnimation(7072);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.doubleHit = true;
			c.ssSpec = true;
			c.specAccuracy = 1.45;
			c.specDamage = 1.3;
			break;

		case 14484: // Dragon claws
			c.startAnimation(2068);
			c.usingClaws = true;
			c.doubleHit = true;
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			break;
		case 3281:
		case 15052:
		case 15053:
		case 4151: // whip
			if (NPCHandler.NPCS.get(i) != null) {
				NPCHandler.NPCS.get(i).gfx100(341);
			}
			c.specAccuracy = 1.10;
			c.startAnimation(1658);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			break;
			
		case 15343: //limewhip
			if (NPCHandler.NPCS.get(i) != null) {
			}
			c.specAccuracy = 1.10;
			c.specEffect = 4;
			c.startAnimation(1658);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			break;

		case 11694: // ags
			c.startAnimation(7074);
			c.specDamage = 1.375;
			c.specAccuracy = 2.40;
			c.gfx0(1222);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			break;
		case 13045: // bludgeon
			c.startAnimation(3157);
			c.specDamage = 1.10;
			c.specAccuracy = 3.10;
			c.gfx0(197);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			break;
		case 7158:
			c.startAnimation(3157);
			c.specDamage = 1.395;
			c.specAccuracy = 1.90;
			c.gfx0(1225);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			break;
		case 11700:
			c.startAnimation(7070);
			c.gfx0(1221);
			c.specDamage = 1.35;
			c.specAccuracy = 1.25;
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.specEffect = 2;
			break;

		case 11696:
			c.startAnimation(7073);
			c.gfx0(1223);
			c.specDamage = 1.20;
			c.specAccuracy = 2.1;
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.specEffect = 3;
			break;

		case 11698:
			c.startAnimation(7071);
			c.gfx0(1220);
			c.specAccuracy = 1.55;
			c.specDamage = 1.35;
			c.specEffect = 4;
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			break;

		case 1249:
			c.startAnimation(405);
			c.gfx100(253);
			if (c.playerIndex > 0) {
				Player o = World.PLAYERS.get(i);
				o.getPA().getSpeared(c.absX, c.absY);
			}
			break;

		case 3204: // d hally
			c.gfx100(282);
			c.startAnimation(1203);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			if (NPCHandler.NPCS.get(i) != null && c.npcIndex > 0) {
				if (!Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(i).getX(), NPCHandler.NPCS.get(i).getY(), 1)) {
					c.doubleHit = true;
				}
			}
			if (World.PLAYERS.get(i) != null && c.playerIndex > 0) {
				if (!Player.goodDistance(c.getX(), c.getY(), World.PLAYERS.get(i).getX(), World.PLAYERS.get(i).getY(), 1)) {
					c.doubleHit = true;
					c.delayedDamage2 = Misc.random(c.getCombat().calculateMeleeMaxHit());
				}
			}
			break;

		case 4153: // maul
			c.startAnimation(1667);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.specDamage = 1.45;
			c.specAccuracy = 2.00;
			c.gfx100(337);
			break;
		case 14610:
		
		case 4587: // dscimmy
			c.gfx100(347);
			c.specEffect = 1;
			c.startAnimation(1872);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			break;

		case 1434: // mace
			c.startAnimation(1060);
			c.gfx100(251);
			c.specMaxHitIncrease = 3;
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase()) + 1;
			c.specDamage = 1.45;
			c.specAccuracy = 1.85;
			break;

		case 859: // magic long
			c.usingBow = true;
			c.bowSpecShot = 3;
			c.rangeItemUsed = c.playerEquipment[Player.playerArrows];
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.startAnimation(426);
			c.gfx100(250);
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			c.projectileStage = 1;
			if (c.fightMode == 2) {
				c.attackTimer--;
			}
			break;
		case 14614:
			c.usingBow = true;
			c.bowSpecShot = 1;
			c.rangeItemUsed = c.playerEquipment[Player.playerArrows];
			c.getItems().deleteArrow();
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.startAnimation(884);
			c.hitDelay = 3;
			c.projectileStage = 1;
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			if (c.fightMode == 2) {
				c.attackTimer--;
			}
			if (c.playerIndex > 0) {
				c.getCombat().fireProjectilePlayer();
			} else if (c.npcIndex > 0) {
				c.getCombat().fireProjectileNpc();
			}
			c.specAccuracy = 1.45;
			c.specDamage = 1.65;
			break;
		case 861: // magic short
			c.usingBow = true;
			c.bowSpecShot = 1;
			c.rangeItemUsed = c.playerEquipment[Player.playerArrows];
			c.getItems().deleteArrow();
			c.getItems().deleteArrow();
			c.lastWeaponUsed = weapon;
			c.startAnimation(1074);
			c.hitDelay = 3;
			c.projectileStage = 1;
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			if (c.fightMode == 2) {
				c.attackTimer--;
			}
			if (c.playerIndex > 0) {
				c.getCombat().fireProjectilePlayer();
			} else if (c.npcIndex > 0) {
				c.getCombat().fireProjectileNpc();
			}
			break;

		case 11235: // dark bow
			c.usingBow = true;
			c.dbowSpec = true;
			c.rangeItemUsed = c.playerEquipment[Player.playerArrows];
			c.getItems().deleteArrow();
			c.getItems().deleteArrow();
			if (c.playerIndex > 0) {
				c.getItems().dropArrowPlayer();
			} else if (c.npcIndex > 0) {
				c.getItems().dropArrowNpc();
			}
			c.lastWeaponUsed = weapon;
			c.hitDelay = 3;
			c.startAnimation(426);
			c.projectileStage = 1;
			c.gfx100(c.getCombat().getRangeStartGFX());
			c.hitDelay = c.getCombat().getHitDelay(i, ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase());
			if (c.fightMode == 2) {
				c.attackTimer--;
			}
			if (c.playerIndex > 0) {
				c.getCombat().fireProjectilePlayer();
			} else if (c.npcIndex > 0) {
				c.getCombat().fireProjectileNpc();
			}
			c.specAccuracy = 1.35;
			c.specDamage = 1.55;
			break;
		}
		c.delayedDamage = Misc.random(c.getCombat().calculateMeleeMaxHit());
		c.delayedDamage2 = Misc.random(c.getCombat().calculateMeleeMaxHit());
		c.usingSpecial = false;
		c.getItems().updateSpecialBar();
	}
}