package com.bclaus.rsps.server.vd.content.dialogue.teleport;

import com.bclaus.rsps.server.vd.content.dialogue.Dialogue;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.vd.content.dialogue.Type;

/**
 * Handles all of the PvP teleport dialogue choices
 * @author Mobster
 *
 */
public class PvPTeleportDialogue extends Dialogue {
	
	/**
	 * An array of the names for the PvP teleport dialogues
	 */
	private static final String[] OPTIONS_1 = new String[] {
		"Varrock PvP", 
		"Mage Bank (@gre@1vs1@bla@)", 
		"Chaos Temple (@red@Multi@bla@)",
		"Graveyard",
		"Castle Drags"
	};
	
	/**
	 * The positions of the PvP teleport locations
	 */
	private static final int[][] OPTION_1_TELEPORT = new int[][] {
		{ 3244, 3515, 0 },
		{ 2539, 4716, 0 },
		{ 3240, 3611, 0 },
		{ 3141, 3690, 0 },
		{ 3005, 3628, 0 },
	};

	@Override
	protected void start(Object... parameters) {
		send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTIONS_1[0], OPTIONS_1[1], OPTIONS_1[2], OPTIONS_1[3], OPTIONS_1[4]);
		phase = 0;
	}
	
	@Override
	public void select(int index) {
		TeleportExecutor.teleport(player, new Position(OPTION_1_TELEPORT[index - 1][0], OPTION_1_TELEPORT[index - 1][1], OPTION_1_TELEPORT[index - 1][2]));
	}

}
