package com.bclaus.rsps.server.vd.npc.boss.impl;

import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.boss.Boss;
import com.bclaus.rsps.server.vd.npc.boss.ProtectionPrayer;
//import server.legacy.npc.boss.challenge.ChallengeBoss;
import com.bclaus.rsps.server.util.Misc;

public class Zulrah extends Boss {

	public Zulrah(int npcId) {
		super(npcId);
	}

	@Override
	public void attack(NPC npc) {
		//if (ChallengeBoss.spawned == true) {
		//	return;
		//}
		npc.attackType = Misc.random(1) == 0 ? 1 : 0;
		if (npc.stage < 10) {
			npc.attackType = 2;
			switch (npc.attackType) {
			case 2:
				npc.projectileId = 162;
				npc.endGfx = 477;
				break;
			}
		}
		npc.stage++;
		if (npc.stage == 10) {
			switch (npc.attackType) {
			case 1: // Range
				npc.projectileId = 475;
				npc.endGfx = 196;
				break;
			}
			npc.requestTransform(2043);
			npc.HP += 50;
			npc.forceChat("I cannot be defeated!");
		}
		if (npc.stage >= 20) {
			switch (npc.attackType) {
			case 2: //mage
				npc.projectileId = 162;
				npc.endGfx = 477;
				break;
			}
			npc.requestTransform(2044);
			npc.forceChat("Feel my wrath!");
			npc.stage = 0;
		}
	}

	@Override
	public int getProtectionDamage(ProtectionPrayer protectionPrayer, int damage) {
		switch (protectionPrayer) {
		case MELEE:
			return damage / 30;
		case RANGE:
			return damage / 30;
		case MAGE:
			return damage / 30;

		}
		return damage;
	}

	@Override
	public int getMaximumDamage(int attackType) {
		return attackType == 2 ? 40 : attackType == 1 ? 30 : 45;
	}

	@Override
	public int distanceRequired() {
		return 8;
	}

	@Override
	public int getAttackEmote() {
		return 5068;
	}

	@Override
	public int getBlockEmote() {
		return -1;
	}

	@Override
	public int getDeathEmote() {
		return 5072;
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
