/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.vd.npc.boss.impl;

import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.boss.Boss;
import com.bclaus.rsps.server.vd.npc.boss.ProtectionPrayer;
import com.bclaus.rsps.server.util.Misc;

public class KBD extends Boss {

	/**
	 * @param npcId
	 */
	public KBD(int npcId) {
		super(npcId);
	}

	@Override
	public void attack(NPC npc) {
		npc.attackType = Misc.random(1) == 0 ? 3 : 0;
		switch (npc.attackType) {
		case 0: // Melee
			npc.projectileId = -1; // melee
			npc.endGfx = -1;
			break;
		case 3:
			int random = Misc.random(4);
			switch (random) {
			case 0:
				npc.projectileId = 393; // red
				npc.endGfx = 430;
				break;
			case 1:
				npc.projectileId = 394; // green
				npc.endGfx = 429;
				break;
			case 2:
				npc.projectileId = 395; // white
				npc.endGfx = 431;
				break;
			case 3:
				npc.projectileId = 396; // blue
				npc.endGfx = 428;
				break;
			}
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
			return damage / 2;
		}
		return damage;
	}

	@Override
	public int getMaximumDamage(int attackType) {
		return attackType == 0 ? 25 : 45;
	}

	@Override
	public int distanceRequired() {
		return 6;
	}

	@Override
	public int getAttackEmote() {
		return 90;
	}

	@Override
	public int getBlockEmote() {
		return -1;
	}

	@Override
	public int getDeathEmote() {
		return 92;
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
