package com.bclaus.rsps.server.vd.npc.boss.impl;

import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.boss.Boss;
import com.bclaus.rsps.server.vd.npc.boss.ProtectionPrayer;

public class Prime extends Boss {

	public Prime(int npcId) {
		super(npcId);
	}

	@Override
	public void attack(NPC npc) {
		npc.attackType = 2;
		switch (npc.attackType) {
			case 0: //Melee
				break;
			case 1: //Range
				break;
				
			case 2: //Mage
				npc.projectileId = 162;
				npc.endGfx = 477;
				break;
		}
	}

	@Override
	public int getProtectionDamage(ProtectionPrayer protectionPrayer, int damage) {
		switch (protectionPrayer) {
			case MELEE:
				break;
			case RANGE:
				break;
			case MAGE:
				return damage / 2;
		}
		return damage;
	}

	@Override
	public int getMaximumDamage(int attackType) {
		return attackType == 2 ? 25 : 25;
	}
	
	@Override
	public int distanceRequired() {
		return 8;
	}

	@Override
	public int getAttackEmote() {
		return 2854;
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

