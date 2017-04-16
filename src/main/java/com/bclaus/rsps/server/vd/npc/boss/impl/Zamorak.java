
/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.vd.npc.boss.impl;

import com.bclaus.rsps.server.vd.npc.boss.ProtectionPrayer;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.boss.Boss;

public class Zamorak extends Boss {

	/**
	 * @param npcId
	 */
	public Zamorak(int npcId) {
		super(npcId);
	}

	@Override
	public void attack(NPC npc) {
		npc.attackType = 0;
		switch (npc.attackType) {
			case 0: //Melee
				break;
			case 1://Range
				break;
			case 2: // mage
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
				break;
		}
		return damage;
	}

	@Override
	public int getMaximumDamage(int attackType) {
		return attackType == 0 ? 45: 20;
	}
	@Override
	public int distanceRequired() {
		return 4;
	}

	@Override
	public int getAttackEmote() {
		return 7060;
	}
	@Override
	public int getBlockEmote() {
		return -1;
	}

	@Override
	public int getDeathEmote() {
		return 7062;
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
