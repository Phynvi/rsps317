package com.bclaus.rsps.server.vd.npc.boss;

import com.bclaus.rsps.server.vd.npc.NPC;

/**
 * 
 * @author Jason MacKeigan
 * @date Oct 21, 2014, 5:00:51 PM
 */
public abstract class Boss {
	
	protected int npcId;
	
	public Boss(int npcId) {
		this.npcId = npcId;
	}
	
	public abstract void attack(NPC npc);
	
	public abstract int getProtectionDamage(ProtectionPrayer protectionPrayer, int damage);
	
	public abstract int getMaximumDamage(int attackType);
	
	public abstract int distanceRequired();
	
	public abstract int getAttackEmote();
	
	public abstract int getBlockEmote();
	
	public abstract int getDeathEmote();
	
	public abstract boolean isAggressive();
	
	public abstract boolean switchesAttackers();

}
