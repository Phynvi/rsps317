
/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.vd.npc.boss.impl;

import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.boss.Boss;
import com.bclaus.rsps.server.vd.npc.boss.ProtectionPrayer;

public class Jungle_Demon extends Boss {

	/**
	 * @param npcId
	 */
	public Jungle_Demon(int npcId) {
		super(npcId);
	}
	@Override
	public void attack(NPC npc) {
		npc.attackType = 0;
	}
	@Override
	public int getProtectionDamage(ProtectionPrayer protectionPrayer, int damage) {
		switch(protectionPrayer) {
		case MELEE:
			return damage / 2;
		default:
			break;
		}
		return damage;
	}
	@Override
	public int getMaximumDamage(int attackType) {
		return attackType == 0 ? 20: 20;
	}
	
	@Override
	public int distanceRequired() {
		return 4;
	}
	@Override
	public int getAttackEmote() {
		return 64;
	}
	@Override
	public int getBlockEmote() {
		return -1;
	}
	@Override
	public int getDeathEmote() {
		return -1;
	}
	@Override
	public boolean isAggressive() {
		return false;
	}
	@Override
	public boolean switchesAttackers() {
		return false;
	}

}
