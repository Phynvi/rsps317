
/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.vd.npc.boss.impl;

import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.boss.Boss;
import com.bclaus.rsps.server.vd.npc.boss.ProtectionPrayer;

public class Bandos extends Boss {

	/**
	 * @param npcId
	 */
	public Bandos(int npcId) {
		super(npcId);
	}

	@Override
	public void attack(NPC npc) {
		npc.attackType = Misc.random(4) == 0 ? 1 : 0;
		switch (npc.attackType) {
			case 0: //Melee
				npc.endGfx = -1;
				npc.projectileId = -1;
				break;
			case 1://Range
					npc.endGfx = 1211;
					npc.projectileId = 288;
				break;
		}
	}

	@Override
	public int getProtectionDamage(ProtectionPrayer protectionPrayer, int damage) {
		switch (protectionPrayer) {
			case MELEE:
				return damage / 2;
			case RANGE:
				return damage / 2;
			case MAGE:
				break;
		}
		return damage;
	}

	@Override
	public int getMaximumDamage(int attackType) {
		return attackType == 0 ? 45 : 15;
	}
	@Override
	public int distanceRequired() {
		return 8;
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
		return true;
	}

	@Override
	public boolean switchesAttackers() {
		return true;
	}


}
