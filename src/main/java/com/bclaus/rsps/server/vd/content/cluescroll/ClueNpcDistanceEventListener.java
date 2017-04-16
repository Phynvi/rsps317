package com.bclaus.rsps.server.vd.content.cluescroll;

import com.bclaus.rsps.server.task.impl.NpcDistanceEventListener;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author lare96 <http://github.com/lare96>
 */
public class ClueNpcDistanceEventListener extends NpcDistanceEventListener {

	private NPC npc;
	private Player player;

	public ClueNpcDistanceEventListener(NPC npc, Player player) {
		super(npc, player);
		this.npc = npc;
		this.player = player;
	}

	@Override
	public void run() {
		NPCHandler.removeNpc(npc);

		if (!player.disconnected) {
			player.clueContainer = null;
			player.sendMessage("You wandered too far off! The boss left and he has taken the reward with him.");
		}
	}
}