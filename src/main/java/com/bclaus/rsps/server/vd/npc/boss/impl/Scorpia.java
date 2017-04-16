package com.bclaus.rsps.server.vd.npc.boss.impl;

import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.boss.ProtectionPrayer;
import com.bclaus.rsps.server.vd.npc.boss.Boss;

public class Scorpia extends Boss {

	public Scorpia(int npcId) {
		super(npcId);
	}

	@Override
	public void attack(NPC npc) {
		npc.attackType = Misc.random(2);
		switch (npc.attackType) {
			case 0: //Melee
				break;
			case 1: //Range
				break;
			case 2: //Mage
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
				return damage / 2;
		}
		return damage;
	}

	@Override
	public int getMaximumDamage(int attackType) {
		return attackType == 1 ? 35 : attackType == 2 ? 45 : 30;
	}
	
	@Override
	public int distanceRequired() {
		return 10;
	}

	@Override
	public int getAttackEmote() {
		return 6254;
	}

	@Override
	public int getBlockEmote() {
		return -1;
	}

	@Override
	public int getDeathEmote() {
		return 6256;
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

