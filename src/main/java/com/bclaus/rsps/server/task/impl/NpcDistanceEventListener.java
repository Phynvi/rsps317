package com.bclaus.rsps.server.task.impl;

import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.task.EventListener;

/**
 * An {@link EventListener} implementation that will remove an npc from the
 * world if it is not in distance of a certain player.
 * 
 * @author lare96
 */
public abstract class NpcDistanceEventListener extends EventListener {

	/** The npc that will be "tracked". */
	private NPC npc;

	/** The player that the npc must be close to. */
	private Player player;

	/**
	 * Create a new {@link NpcDistanceEventListener}.
	 * 
	 * @param npc
	 *            the npc that will be "tracked".
	 * @param player
	 *            the player that the npc must be close to.
	 */
	public NpcDistanceEventListener(NPC npc, Player player) {
		this.npc = npc;
		this.player = player;
	}

	@Override
	public boolean listenFor() {
		if (npc.isDead || NPCHandler.NPCS.get(npc.getIndex()) == null) {
			stop();
			return true;
		}

		return npc.getPosition().withinDistance(player.getPosition(), 7) && !player.disconnected;
	}
}