/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.vd.content.dialogue.teleport;

import com.bclaus.rsps.server.vd.content.dialogue.Dialogue;
import com.bclaus.rsps.server.vd.content.dialogue.Type;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.world.Position;

public class MonsterTeleportDialogue extends Dialogue {

	/**
	 * An array of the names for the Monster teleport dialogues
	 */
	private static final String[] OPTIONS_1 = new String[] { "Ape Atoll", "Crash Island", "Stronghold of Security", "Training Zones", "[More]" };

	private static final String[] OPTIONS_2 = new String[] { "Ape Atoll Dungeon", "Brimhaven Dungeon", "Slayer Dungeons", "Taverly Dungeon", "[More]" };

	private static final String[] OPTIONS_3 = new String[] { "Rock Crabs", "Otherworldly Beings", "Experiments", "[Back]" };

	private static final String[] OPTIONS_4 = new String[] { "Fremennik Slayer Dungeon", "Slayer Tower", "[Back]" };

	private static final String[] OPTIONS_5 = new String[] { "Lunar Island", "Edgeville Dungeon", "[Back]" };
	/**
	 * The positions of the Monster teleport locations
	 */

	private static final int[][] OPTION_1_TELEPORT = new int[][] { { 2794, 2786, 0 }, // Ape
																						// Atoll
			{ 2894, 2727, 0 }, // Crash Island
			{ 1860, 5245, 0 }, // Stronghold of Security
			{ 0, 0, 0 }, // Training Zones
			{ 0, 0, 0 }, };

	private static final int[][] OPTION_2_TELEPORT = new int[][] { { 2716, 9135, 0 }, // Ape
																						// Atoll
																						// Dungeon
			{ 2744, 3149, 0 }, // Brimhaven Dungeon
			{ 0, 0, 0 }, // Slayer Dungeons
			{ 2884, 9798, 0 }, // Taverly Dungeon
			{ 0, 0, 0 }, // Back
	};

	private static final int[][] OPTIONS_3_TELEPORT = new int[][] { { 2696, 3718, 0 }, // Otherworldly
			{ 2696, 3718, 0 }, // Experiments
			{ 3558, 9945, 0 } // Back
	};

	private static final int[][] OPTIONS_4_TELEPORT = new int[][] { { 2794, 9996, 0 }, // Fremennik
																						// Slayer
																						// Dungeon
			{ 3429, 3538, 0 }, // Slayer Tower
			{ 0, 0, 0 } // Back
	};
	private static final int[][] OPTIONS_5_TELEPORT = new int[][] { { 2324, 3804, 0 }, { 3132, 9909, 0 }, // Lunar
																											// Island
			{ 0, 0, 0 } // Back
	};

	@Override
	protected void start(Object... parameters) {
		send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTIONS_1[0], OPTIONS_1[1], OPTIONS_1[2], OPTIONS_1[3], OPTIONS_1[4]);
		phase = 0;
	}

	@Override
	public void select(int index) {
		System.out.println("Phase: " + phase + " Index: " + index);
		if (phase == 0) {
			if (index == 4) {
				send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTIONS_3[0], OPTIONS_3[1], OPTIONS_3[2], OPTIONS_3[3]);
				phase = 2;
			} else if (index == 5) { // Option 5
				send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTIONS_2[0], OPTIONS_2[1], OPTIONS_2[2], OPTIONS_2[3], OPTIONS_2[4]);
				phase = 1;
			} else {
				TeleportExecutor.teleport(player, new Position(OPTION_1_TELEPORT[index - 1][0], OPTION_1_TELEPORT[index - 1][1], OPTION_1_TELEPORT[index - 1][2]));
			}
		} else if (phase == 1) { // Second load of options
			if (index == 3) { // Option 3
				phase = 3; // Send to Slayer Dungeon teleports.
				send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTIONS_4[0], OPTIONS_4[1], OPTIONS_4[2]);
			} else if (index == 5) {
				phase = 4; // Send to first load of teleport options
				send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTIONS_5[0], OPTIONS_5[1], OPTIONS_5[2]);
			} else {
				TeleportExecutor.teleport(player, new Position(OPTION_2_TELEPORT[index - 1][0], OPTION_2_TELEPORT[index - 1][1], OPTION_2_TELEPORT[index - 1][2]));
			}
		} else if (phase == 2) {
			if (index == 4) {
				phase = 0;
				send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTIONS_1[0], OPTIONS_1[1], OPTIONS_1[2], OPTIONS_1[3], OPTIONS_1[4]);
			} else {
				TeleportExecutor.teleport(player, new Position(OPTIONS_3_TELEPORT[index - 1][0], OPTIONS_3_TELEPORT[index - 1][1], OPTIONS_3_TELEPORT[index - 1][2]));
			}
		} else if (phase == 3) {
			if (index == 3) {
				phase = 1;
				send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTIONS_2[0], OPTIONS_2[1], OPTIONS_2[2], OPTIONS_2[3], OPTIONS_2[4]);
			} else {
				TeleportExecutor.teleport(player, new Position(OPTIONS_4_TELEPORT[index - 1][0], OPTIONS_4_TELEPORT[index - 1][1]));
			}
		} else if (phase == 4) {
			if (index == 3) {
				phase = 0;
				send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTIONS_1[0], OPTIONS_1[1], OPTIONS_1[2], OPTIONS_1[3], OPTIONS_1[4]);
			} else
				TeleportExecutor.teleport(player, new Position(OPTIONS_5_TELEPORT[index - 1][0], OPTIONS_5_TELEPORT[index - 1][1]));
		}
	}
}