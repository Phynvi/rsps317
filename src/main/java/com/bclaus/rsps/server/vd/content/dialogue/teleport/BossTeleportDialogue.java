package com.bclaus.rsps.server.vd.content.dialogue.teleport;

import com.bclaus.rsps.server.vd.content.dialogue.Dialogue;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.vd.content.dialogue.Type;

public class BossTeleportDialogue extends Dialogue {

	/**
	 * An array for all the dialogue strings.
	 */
	private static final String[] OPTION_1 = { "Barrelchest", "Godwars Dungeon", "Kalphite Queen", "King Black Dragon [@red@Wildy@bla@]", "[More]" };

	private static final String[] OPTION_2 = { "Chaos Elemental [@red@Wildy@bla@]", "Dagannoth Kings", "Giant Mole", "Kraken", "[More]" };
	
	private static final String[] OPTION_3 = { "Vet'ion {WILD}", "Scorpia {WILD}", "Venenatis{WILD}", "Callisto{WILD}", "[More]" };

	private static final String[] OPTION_4 = { "Glod", "Ice queen", "[Back]" };

	/**
	 * An array for all corresponding dialogue strings which holds all the
	 * teleport locations.
	 */
	private static final int[][] OPTION_1_TELEPORT = { 
			{ 2983, 9516, 1 }, // Barrelchest
			{ 2871, 5317, 2 }, // Godwars Dungeon
			{ 3480, 9484, 0 }, // Kalphite Queen
			{ 3007, 3849, 0 }, // King Black Dragon
			{ 0, 0, 0 } // More
	};

	private static final int[][] OPTION_2_TELEPORT = { 
			{ 3295, 3921, 0 }, // Chaos Elemental
			{ 1891, 4409, 0 }, // Dagannoth King
			{ 1771, 5167, 0 }, // Giant Mole
			{ 2340, 3686, 0 }, // Kraken
			{ 0, 0, 0 } // More
	};
	private static final int[][] OPTION_3_TELEPORT = { 
			{ 3213, 3796, 0 }, // Chaos
			{ 3232, 3941, 0 }, // Dagannoth King
			{ 3369, 3742, 0 }, // Giant Mole
			{ 3294, 3839, 0 }, // Calisto
			{ 0, 0, 0 } // More
	};
	private static final int[][] OPTION_4_TELEPORT = {
		{ 2787, 9337, 0 }, // Glod
		{ 3036, 9542, 0 }, // Ice Queen
		{ 3087, 3503, 0 } // More
};


	protected void start(Object... parameters) {
		send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTION_1[0], OPTION_1[1], OPTION_1[2], OPTION_1[3], OPTION_1[4]);
		phase = 0;
	}


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
				send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTION_3[0], OPTION_3[1], OPTION_3[2], OPTION_3[3], OPTION_3[4]);
			} else {
				TeleportExecutor.teleport(player, new Position(OPTION_2_TELEPORT[index - 1][0], OPTION_2_TELEPORT[index - 1][1], OPTION_2_TELEPORT[index - 1][2]));
			}
		} else if (phase == 2) {
			if (index == 5) {
				phase = 3;
				send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTION_4[0], OPTION_4[1]);
			} else {
				TeleportExecutor.teleport(player, new Position(OPTION_3_TELEPORT[index - 1][0], OPTION_3_TELEPORT[index - 1][1], OPTION_3_TELEPORT[index - 1][2]));
			}
		} else if (phase == 3) {
			if(index == 3) {
				phase = 0;
				send(Type.CHOICE, DEFAULT_OPTION_TITLE, OPTION_1[0], OPTION_1[1], OPTION_1[2], OPTION_1[3], OPTION_1[4]);
			} else {
				TeleportExecutor.teleport(player, new Position(OPTION_4_TELEPORT[index - 1][0], OPTION_4_TELEPORT[index - 1][1]));
			}
		}
	}
}