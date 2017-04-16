package com.bclaus.rsps.server.vd.content.dialogue.teleport;

import com.bclaus.rsps.server.vd.content.dialogue.Dialogue;
import com.bclaus.rsps.server.vd.content.dialogue.Type;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.world.Position;

/**
 * Handles all of the PvP teleport dialogue choices
 * @author Antony
 *
 */
/**
 * Handles all of the PvP teleport dialogue choices
 * @author Antony
 *
 */
public class RandomTeleportDialogue extends Dialogue {
	
	/**
	 * An array of the names for the PvP teleport dialogues
	 */
	private static final String[] OPTION_1 = { "Jungle Demons", "Cave Kraken", "Mithril Dragons/Ice Warriors", "Skeletal Wyverns", "[More]" };

	private static final String[] OPTION_2 = { "Pack Yaks", "Bats {Slayer}", "Earth Warriors", "Taverly Dungeon", "Back" };
	

	/**
	 * An array for all corresponding dialogue strings which holds all the
	 * teleport locations.
	 */
	private static final int[][] OPTION_1_TELEPORT = { { 2641, 4565, 1 }, // JD
			{ 2393, 4684, 0 }, // CK
			{ 3050, 9589, 0 }, // MD
			{ 2865, 9920, 0 }, // Wyverns
			{ 0, 0, 0 } // More
	};

	private static final int[][] OPTION_2_TELEPORT = { { 2325, 3799, 0 }, // Yaks																			
			{ 3134, 9910, 0 }, // edge
			{ 3124, 9974, 0 }, // earth warrior
			{ 2906, 9671, 0 }, // Taverly
			{ 0, 0, 0 } // More
	};

	@Override
	protected void start(Object... parameters) {
		send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTION_1[0], OPTION_1[1], OPTION_1[2], OPTION_1[3], OPTION_1[4]);
		phase = 0;
	}

	@Override
	public void select(int index) {
		System.out.println("Phase: " + phase + " index : " + index);
		if (phase == 0) {
			if (index == 5) {
				phase = 1;
				send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTION_2[0], OPTION_2[1], OPTION_2[2], OPTION_2[3], OPTION_2[4]);
			} else {
				TeleportExecutor.teleport(player, new Position(OPTION_1_TELEPORT[index - 1][0], OPTION_1_TELEPORT[index - 1][1], OPTION_1_TELEPORT[index - 1][2]));
			}
		} else if (phase == 1) {
			if (index == 5) {
				phase = 2;
			} else {
				TeleportExecutor.teleport(player, new Position(OPTION_2_TELEPORT[index - 1][0], OPTION_2_TELEPORT[index - 1][1], OPTION_2_TELEPORT[index - 1][2]));
			}
		
		}
	}
}