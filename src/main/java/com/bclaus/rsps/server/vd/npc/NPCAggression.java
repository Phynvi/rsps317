package com.bclaus.rsps.server.vd.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * The static utility class that handles the behavior of aggressive NPCs within
 * a certain radius of players.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class NPCAggression {

	public static final Map<Integer, Integer> AGGRESSION = new HashMap<>();

	/**
	 * The absolute distance that players must be within to be targeted by
	 * aggressive NPCs.
	 */
	private static final int TARGET_DISTANCE = 10;
	private static final int COMBAT_LEVEL_TOLERANCE = 100;

	/**
	 * The sequencer that will prompt all aggressive NPCs to attack
	 * {@code player}.
	 * 
	 * @param player
	 *            the player that will be targeted by aggressive NPCs.
	 */
	public static void process(Player player) {
		for (NPC npc : player.localNpcs) {
			if (npc == null)
				continue;
			if (validate(npc, player))
				npc.killerId = player.getIndex();
		}
	}

	/**
	 * Determines if {@code npc} is able to target {@code player}.
	 * 
	 * @param npc
	 *            the npc trying to target the player.
	 * @param player
	 *            the player that is being targeted by the NPC.
	 * @return {@code true} if the player can be targeted, {@code false}
	 *         otherwise.
	 */
	private static boolean validate(NPC npc, Player p) {
		NPCHandler h = Server.npcHandler;
		if (p.underAttackBy > 0 || p.underAttackBy2 > 0 && !p.inMulti())
			return false;
		if (p.heightLevel != npc.heightLevel)
			return false;
		if (p.aggressionTolerance.elapsed(5, TimeUnit.MINUTES) && npc.combatLevel < COMBAT_LEVEL_TOLERANCE)
			return false;
		if (!h.goodDistance(p.absX, p.absY, npc.absX, npc.absY, AGGRESSION.getOrDefault(npc.npcType, TARGET_DISTANCE)))
			return false;
		if (!(h.isAggressive(npc) && !npc.underAttack && !npc.isDead && !h.switchesAttackers(npc) || h.isAggressive(npc) && !npc.underAttack && !npc.isDead && h.switchesAttackers(npc)))
			return false;
		return true;
	}
}
