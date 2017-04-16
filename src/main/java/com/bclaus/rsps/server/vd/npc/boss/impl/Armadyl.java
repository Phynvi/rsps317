
/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.vd.npc.boss.impl;

import com.bclaus.rsps.server.vd.npc.boss.ProtectionPrayer;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.boss.Boss;

public class Armadyl extends Boss {

	/**
	 * @param npcId
	 */
	public Armadyl(int npcId) {
		super(npcId);
	}

	@Override
	public void attack(NPC npc) {
		npc.attackType = 1;
		switch (npc.attackType) {
			case 0: //Melee
				break;
			case 1://Range
				npc.projectileId = 1203;
				break;
			case 2: // mage
				break;
		}
	}

	@Override
	public int getProtectionDamage(ProtectionPrayer protectionPrayer, int damage) {
		switch (protectionPrayer) {
			case MELEE:
				break;
			case RANGE:
				return damage / 2;
			case MAGE:
				break;
		}
		return damage;
	}

	@Override
	public int getMaximumDamage(int attackType) {
		return attackType == 1 ? 35: 18;
	}
	@Override
	public int distanceRequired() {
		return 6;
	}

	@Override
	public int getAttackEmote() {
		return 6977;
	}
	@Override
	public int getBlockEmote() {
		return -1;
	}

	@Override
	public int getDeathEmote() {
		return 1524;
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
