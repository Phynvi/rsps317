/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.vd.npc.boss.impl;

import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.boss.Boss;
import com.bclaus.rsps.server.vd.npc.boss.ProtectionPrayer;
import com.bclaus.rsps.server.util.Misc;

public class Saradomin extends Boss {

	/**
	 * @param npcId
	 */
	public Saradomin(int npcId) {
		super(npcId);
	}

	@Override
	public void attack(NPC npc) {
		npc.attackType = Misc.random(4) == 0 ? 2 : 0;
		switch (npc.attackType) {
		case 0: // Melee
			npc.endGfx = -1;
			break;
		case 1:// Range
			break;
		case 2: // mage
			npc.endGfx = 1224;
			break;
		}
	}

	@Override
	public int getProtectionDamage(ProtectionPrayer protectionPrayer, int damage) {
		switch (protectionPrayer) {
		case MELEE:
			return damage / 2;
		case RANGE:
			break;
		case MAGE:
			return damage / 2;
		}
		return damage;
	}

	@Override
	public int getMaximumDamage(int attackType) {
		return attackType == 0 ? 25 : 40;
	}

	@Override
	public int distanceRequired() {
		return 8;
	}

	@Override
	public int getAttackEmote() {
		return 6964;
	}

	@Override
	public int getBlockEmote() {
		return -1;
	}

	@Override
	public int getDeathEmote() {
		return 6965;
	}

	@Override
	public boolean isAggressive() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean switchesAttackers() {
		return true;
	}

}
