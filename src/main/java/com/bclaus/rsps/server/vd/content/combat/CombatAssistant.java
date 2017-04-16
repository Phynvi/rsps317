/**
 * 
 */
package com.bclaus.rsps.server.vd.content.combat;

import com.bclaus.rsps.server.vd.content.combat.magic.MagicData;
import com.bclaus.rsps.server.vd.content.combat.melee.MeleeRequirements;
import com.bclaus.rsps.server.vd.content.combat.range.RangeExtras;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.content.combat.magic.MagicExtras;
import com.bclaus.rsps.server.vd.content.combat.magic.MagicMaxHit;
import com.bclaus.rsps.server.vd.content.combat.magic.MagicRequirements;
import com.bclaus.rsps.server.vd.content.combat.melee.CombatPrayer;
import com.bclaus.rsps.server.vd.content.combat.melee.MeleeData;
import com.bclaus.rsps.server.vd.content.combat.melee.MeleeExtras;
import com.bclaus.rsps.server.vd.content.combat.melee.MeleeMaxHit;
import com.bclaus.rsps.server.vd.content.combat.melee.MeleeSpecial;
import com.bclaus.rsps.server.vd.content.combat.range.RangeData;
import com.bclaus.rsps.server.vd.content.combat.range.RangeMaxHit;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.util.Misc;

public class CombatAssistant {

	private Player c;

	public CombatAssistant(Player Client) {
		this.c = Client;
	}

	public int[][] slayerReqs = { {3848, 87}, {4357, 58},{4383, 76}, { 1648, 5 }, { 1612, 15 }, { 1643, 45 }, { 1618, 50 }, { 1624, 65 }, { 1610, 75 }, { 1613, 80 }, { 1615, 85 }, { 2783, 90 } };

	public boolean goodSlayer(int i) {
		for (int j = 0; j < slayerReqs.length; j++) {
			if (slayerReqs[j][0] == NPCHandler.NPCS.get(i).npcType) {
				if (slayerReqs[j][1] > c.playerLevel[Player.playerSlayer]) {
					c.sendMessage("You need a slayer level of " + slayerReqs[j][1] + " to harm this NPC.");
					
					return false;
				}
				if(NPCHandler.NPCS.get(i).npcType == 3848 && c.slayerTask != 3848)  {
					c.sendMessage("You must have this npc as a slayer-task to attack");
					return false;
					
				}
			}
		}
		return true;
	}

	public void handleGmaul() {
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
			} else if (Player.goodDistance(c.getX(), c.getY(), NPCHandler.NPCS.get(c.npcIndex).getX(), NPCHandler.NPCS.get(c.npcIndex).getY(), c.getCombat().getRequiredDistance())) {
				if (c.getCombat().checkSpecAmount(4153)) {
					boolean hit = Misc.random(c.getCombat().calculateMeleeAttack()) > Misc.random(NPCHandler.NPCS.get(c.npcIndex).defence);
					int damage = 0;
					if (hit) {
						damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
						NPCHandler.NPCS.get(c.npcIndex).damage(new Hit(damage));
						c.startAnimation(1667);
						c.gfx100(340);
					}
				}
			}
		}
	}

	public void resetPlayerAttack() {
		MeleeData.resetPlayerAttack(c);
	}

	public int getCombatDifference(int combat1, int combat2) {
		return MeleeRequirements.getCombatDifference(combat1, combat2);
	}

	public int getKillerId(int playerId) {
		return MeleeRequirements.getKillerId(c, playerId);
	}

	public boolean checkReqs() {
		return MeleeRequirements.checkReqs(c);
	}

	public boolean checkMultiBarrageReqs(int i) {
		return MagicExtras.checkMultiBarrageReqs(c, i);
	}

	public int getRequiredDistance() {
		return MeleeRequirements.getRequiredDistance(c);
	}

	public void multiSpellEffectNPC(int npcId, int damage) {
		MagicExtras.multiSpellEffectNPC(c, npcId, damage);
	}

	public boolean checkMultiBarrageReqsNPC(int i) {
		return MagicExtras.checkMultiBarrageReqsNPC(i);
	}

	public void appendMultiBarrageNPC(int npcId, boolean splashed) {
		MagicExtras.appendMultiBarrageNPC(c, npcId, splashed);
	}

	public void attackNpc(int i) {
		AttackNPC.attackNpc(c, i);
	}

	public void delayedHit(final Player c, final int i, Item item) {
		AttackNPC.delayedHit(c, i, item);
	}

	public void applyNpcMeleeDamage(int i, int damageMask, int damage) {
		AttackNPC.applyNpcMeleeDamage(c, i, damageMask, damage);
	}

	public void attackPlayer(int i) {
		AttackPlayer.attackPlayer(c, i);
	}

	public void playerDelayedHit(final Player c, final int i, Item item) {
		AttackPlayer.playerDelayedHit(c, i, item);
	}

	public void applyPlayerMeleeDamage(int i, int damageMask, int damage) {
		AttackPlayer.applyPlayerMeleeDamage(c, i, damageMask, damage);
	}

	public void addNPCHit(int i, Player c, Item item) {
		AttackNPC.addNPCHit(i, c, item);
	}

	public void applyPlayerHit(Player c, final int i, Item item) {
		AttackPlayer.applyPlayerHit(c, i, item);
	}

	public void fireProjectileNpc() {
		RangeData.fireProjectileNpc(c);
	}

	public void fireProjectilePlayer() {
		RangeData.fireProjectilePlayer(c);
	}

	public boolean usingCrystalBow() {
		return c.playerEquipment[Player.playerWeapon] >= 4212 && c.playerEquipment[Player.playerWeapon] <= 4223;
	}

	public boolean multis() {
		return MagicData.multiSpells(c);
	}

	public void appendMultiBarrage(int playerId, boolean splashed) {
		MagicExtras.appendMultiBarrage(c, playerId, splashed);
	}

	public void multiSpellEffect(int playerId, int damage) {
		MagicExtras.multiSpellEffect(c, playerId, damage);
	}

	public void applySmite(int index, int damage) {
		MeleeExtras.applySmite(c, index, damage);
	}

	public boolean usingDbow() {
		return c.playerEquipment[Player.playerWeapon] == 11235;
	}

	public boolean usingHally() {
		return MeleeData.usingHally(c);
	}

	public void getPlayerAnimIndex(String weaponName) {
		if (weaponName.endsWith("(S)")) {
			c.playerStandIndex = 11973;
			c.playerWalkIndex = 11975;
			c.playerRunIndex = 1661;
			return;
		}
		MeleeData.getPlayerAnimIndex(c, weaponName);
	}

	public int getWepAnim(String weaponName) {
		if(c.playerEquipment[c.playerWeapon] <= 0
		|| weaponName.endsWith("(S)")) {
			switch(c.fightMode) {
				case 0:
				return 422;			
				case 2:
				return 423;			
				case 1:
				return 451;
			}
		}
		return MeleeData.getWepAnim(c, weaponName);
	}

	public int getBlockEmote() {
		return MeleeData.getBlockEmote(c);
	}

	public int getAttackDelay(String s) {
		return MeleeData.getAttackDelay(c, s);
	}

	public int getHitDelay(int i, String weaponName) {
		return MeleeData.getHitDelay(c, i, weaponName);
	}

	public int npcDefenceAnim(int i) {
		return MeleeData.npcDefenceAnim(i);
	}

	public int calculateMeleeAttack() {
		return MeleeMaxHit.calculateMeleeAttack(c);
	}

	public int bestMeleeAtk() {
		return MeleeMaxHit.bestMeleeAtk(c);
	}

	public int calculateMeleeMaxHit() {
		return MeleeMaxHit.calculateMeleeMaxHit(c);
	}

	public boolean fullVeracs() {
		return MeleeMaxHit.fullVeracs(c);
	}
	public boolean fullDharok(){
		return MeleeMaxHit.fullDharok(c);
	}

	public boolean fullGuthans() {
		return MeleeMaxHit.fullGuthans(c);
	}

	public int calculateMeleeDefence() {
		return MeleeMaxHit.calculateMeleeDefence(c);
	}

	public int bestMeleeDef() {
		return MeleeMaxHit.bestMeleeDef(c);
	}

	public void handleDfs() {
		MeleeExtras.handleDfs(c);
	}

	public void handleDfsNPC() {
		MeleeExtras.handleDfsNPC(c);
	}

	public void appendVengeanceNPC(int otherPlayer, int damage) {
		MeleeExtras.appendVengeanceNPC(c, otherPlayer, damage);
	}

	public void appendVengeance(int otherPlayer, int damage) {
		MeleeExtras.appendVengeance(c, otherPlayer, damage);
	}

	public void applyRecoilNPC(int damage, int i) {
		MeleeExtras.applyRecoilNPC(c, damage, i);
	}

	public void applyRecoil(int damage, int i) {
		MeleeExtras.applyRecoil(c, damage, i);
	}

	public void applySpiritShield(int damage, int i) {
		MeleeExtras.applySpiritEffects(c, damage, i);
	}

	public void removeRecoil(Player c) {
		MeleeExtras.removeRecoil(c);
	}

	public void handleGmaulPlayer() {
		MeleeExtras.graniteMaulSpecial(c);
	}

	public void activateSpecial(int weapon, int i) {
		MeleeSpecial.activateSpecial(c, weapon, i);
	}

	public boolean checkSpecAmount(int weapon) {
		return MeleeSpecial.checkSpecAmount(c, weapon);
	}

	public int calculateRangeAttack() {
		return RangeMaxHit.calculateRangeAttack(c);
	}

	public int calculateRangeDefence() {
		return RangeMaxHit.calculateRangeDefence(c);
	}

	public int rangeMaxHit() {
		return RangeMaxHit.maxHit(c);
	}

	public int getRangeStr(int i) {
		return RangeData.getRangeStr(i);
	}

	public int getRangeStartGFX() {
		return RangeData.getRangeStartGFX(c);
	}

	public int getRangeProjectileGFX() {
		return RangeData.getRangeProjectileGFX(c);
	}

	public int correctBowAndArrows() {
		return RangeData.correctBowAndArrows(c);
	}

	public int getProjectileShowDelay() {
		return RangeData.getProjectileShowDelay(c);
	}

	public int getProjectileSpeed() {
		return RangeData.getProjectileSpeed(c);
	}

	public void crossbowSpecial(Player c, int i) {
		RangeExtras.crossbowSpecial(c, i);
	}

	public void appendMutliChinchompa(int npcId) {
		RangeExtras.appendMutliChinchompa(c, npcId);
	}

	public boolean properBolts() {
		return usingBolts(c.playerEquipment[Player.playerArrows]);
	}
	public boolean properDarts() {
		return usingDarts(c.playerEquipment[Player.playerArrows]);
	}

	public boolean properBoltRacks() {
		return usingRackBolts(c.playerEquipment[Player.playerArrows]);
	}

	public void castOtherVengeance() {
		// CastOnOther.castOtherVengeance(c);
	}

	public boolean usingRackBolts(int i) {
		return (i == 4740);
	}

	public boolean usingBolts(int i) {
		return (i >= 9140 && i <= 9145) || i >= 9334 && i <= 9344 || (i >= 9236 && i <= 9245);
	}
	public boolean usingDarts(int i) {
		return (i >= 806 && i <= 817);
	}

	public int mageAtk() {
		return MagicMaxHit.mageAttack(c);
	}

	public int mageDef() {
		return MagicMaxHit.mageDef(c);
	}

	public int magicMaxHit() {
		return MagicMaxHit.magiMaxHit(c);
	}

	public boolean wearingStaff(int runeId) {
		return MagicRequirements.wearingStaff(c, runeId);
	}

	public boolean checkMagicReqs(int spell) {
		return MagicRequirements.checkMagicReqs(c, spell);
	}

	public int getFreezeTime() {
		return MagicData.getFreezeTime(c);
	}

	public int getStartHeight() {
		return MagicData.getStartHeight(c);
	}

	public int getEndHeight() {
		return MagicData.getEndHeight(c);
	}

	public int getStartDelay() {
		return MagicData.getStartDelay(c);
	}

	public int getStaffNeeded() {
		return MagicData.getStaffNeeded(c);
	}

	public boolean godSpells() {
		return MagicData.godSpells(c);
	}

	public int getEndGfxHeight() {
		return MagicData.getEndGfxHeight(c);
	}

	public int getStartGfxHeight() {
		return MagicData.getStartGfxHeight(c);
	}

	public void handlePrayerDrain() {
		CombatPrayer.handlePrayerDrain(c);
	}

	public void reducePrayerLevel() {
		CombatPrayer.reducePrayerLevel(c);
	}

	public void resetPrayers() {
		CombatPrayer.resetPrayers(c);
	}

	public void activatePrayer(int i) {
		CombatPrayer.activatePrayer(c, i);
	}
}
