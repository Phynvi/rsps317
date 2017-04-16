package com.bclaus.rsps.server.vd.npc.boss.impl;

import com.bclaus.rsps.server.vd.npc.boss.ProtectionPrayer;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.boss.Boss;

public class Cave_Kraken extends Boss {

	public Cave_Kraken(int npcId) {
		super(npcId);
	}

	@Override
	public void attack(NPC npc) {
		npc.attackType = 2;
		switch (npc.attackType) {
		case 2: // Mage
			npc.projectileId = 254;
			break;
		}
	}

	@Override
	public int getProtectionDamage(ProtectionPrayer protectionPrayer, int damage) {
		switch (protectionPrayer) {
			case RANGE:
				return damage;
			case MELEE:
				return damage;
			case MAGE:
				return damage;
		}
		return damage;
	}

	@Override
	public int getMaximumDamage(int attackType) {
		return attackType == 2 ? 25 : 15;
	}

	@Override
	public int distanceRequired() {
		return 12;
	}

	@Override
	public int getAttackEmote() {
		return 3991;
	}

	@Override
	public int getBlockEmote() {
		return -1;
	}

	@Override
	public int getDeathEmote() {
		return 3993;
	}

	@Override
	public boolean isAggressive() {
		return true;
	}

	@Override
	public boolean switchesAttackers() {
		return false;
	}

}
