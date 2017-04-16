package com.bclaus.rsps.server.vd.npc.boss.impl;

import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.boss.Boss;
import com.bclaus.rsps.server.vd.npc.boss.ProtectionPrayer;

public class Supreme extends Boss {

	public Supreme(int npcId) {
		super(npcId);
	}

	@Override
	public void attack(NPC npc) {
		npc.attackType = 1;
		switch (npc.attackType) {
			case 0: //Melee
				break;
			case 1: //Range
				npc.projectileId = 475;
				break;
			case 2: //Mage
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
		return attackType == 1 ? 25 : 25;
	}
	
	@Override
	public int distanceRequired() {
		return 8;
	}

	@Override
	public int getAttackEmote() {
		return 2855;
	}

	@Override
	public int getBlockEmote() {
		return -1;
	}

	@Override
	public int getDeathEmote() {
		return 2856;
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

