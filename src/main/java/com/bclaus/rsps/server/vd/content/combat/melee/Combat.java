package com.bclaus.rsps.server.vd.content.combat.melee;

import com.bclaus.rsps.server.vd.player.Player;

public class Combat {

	/**
	 * Sets a check so the players max hit will be based on the used items when
	 * the attack got performed.
	 */
	public static void setArmourCheck(Player c) {
		for (int armourSlot = 0; armourSlot < c.lastWeapon.length; armourSlot++) {
			c.lastWeapon[armourSlot] = c.playerEquipment[armourSlot];
		}
	}

	/**
	 * Checks the used item when the attack got performed.
	 */
	public static int getArmour(Player c, int slot) {
		return c.lastWeapon[slot];
	}
}