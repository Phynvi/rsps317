package com.bclaus.rsps.server.vd.content.minigames.warriorsguild;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * Handles the Warriors Guild minigame.
 * 
 * @author Tim
 *
 */
public class WarriorsGuild {

	/**
	 * Handles the armour on animator.
	 * 
	 * @param player
	 *            the player.
	 * @param id
	 *            the item used id.
	 */
	public static void handleAnimator(Player player, int id, int x, int y) {
		final SuiteOfArmour suite = SuiteOfArmour.forId(id);
		if (suite == null) {
			player.sendMessage("Nothing interesting happens.");
			return;
		}
		if (!hasRequiredItems(player, suite)) {
			player.sendMessage("You don't have all of the required items to use the animator!");
			return;
		}
		animateSuite(player, suite, x, y);
	}

	/**
	 * Animates a new Suite of Armour.
	 * 
	 * @param player
	 *            the player.
	 * @param suite
	 *            the suite of armour.
	 */
	private static void animateSuite(final Player player, final SuiteOfArmour suite, final int x, final int y) {

		/**
		 * We remove the armour pieces.
		 */
		for (int piece : suite.getArmourPieces()) {
			player.getItems().deleteItem(piece, 1);
		}

		// TODO force movement packet back 3 spots

		/**
		 * We face the player towards the animator.
		 */
		player.turnPlayerTo(x, y);

		/**
		 * We start a new event.
		 */
		Server.getTaskScheduler().schedule(new ScheduledTask(5) {
			@Override
			public void execute() {
				NPCHandler.spawnNpc(player, suite.getId(), x, y + 2, 0, 0, suite.getStat(0), suite.getStat(1), suite.getStat(2), suite.getStat(3), true, true);
				this.stop();
			}

		}.attach(player));

	}

	/**
	 * Enters the cyclops room.
	 * 
	 * @param player
	 *            the player.
	 */
	public static void enterCyclopsRoom(final Player player) {

		/**
		 * We check if the player has found any defenders and we show it to
		 * Kamfreena.
		 */
		int[] defenders = { 8844, 8845, 8846, 8847, 8848, 8849, 8850, 15496 };
		for (int i = 0; i < defenders.length; i++) {
			if (player.getItems().playerHasItem(defenders[i]) || player.playerEquipment[Player.playerShield] == defenders[i]) {
				player.shownDefender[i] = true;
			}
		}

		/**
		 * We check if the player has tokens.
		 */
		if (!player.getItems().playerHasItem(8851, 100)) {
			player.sendMessage("You need 100 tokens to enter the Cyclops room!");
			return;
		}
		
		player.enteredGuild = true;

		/**
		 * We open the proper Kamfreena dialogue.
		 */
		player.getDH().sendDialogues(player.shownDefender[0] ? 1452 : 1450, 4289);

	}

	/**
	 * Gets the name of the next defender.
	 * 
	 * @param player
	 *            the player.
	 * @return the name.
	 */
	public static int getNextDefenderId(String s) {
		String[] defenders = new String[] { "Bronze", "Iron", "Steel", "Black", "Mithril", "Adamant", "Runite", "Dragon" };
		int[] defenderIds = { 8844, 8845, 8846, 8847, 8848, 8849, 8850, 15496 };
		for (int i = 0; i < defenders.length; i++) {
			if (defenders[i] == s) {
				return defenderIds[i];
			}
		}
		return -1;
	}
	
	public static boolean hasDefender(int itemId) {
		switch(itemId) {
		case 8850:
		case 15496:
		case 8849:
		case 8848:
		case 8847:
		case 8846:
			return true;
		}
		return false;
	}

	/**
	 * Gets the name of the next defender.
	 * 
	 * @param player
	 *            the player.
	 * @return the name.
	 */
	public static String getNextDefender(Player player) {
		String[] defenders = new String[] { "Bronze", "Iron", "Steel", "Black", "Mithril", "Adamant", "Runite", "Dragon" };
		for (int i = 6; i <= player.shownDefender.length && i > -1; i--) {
			if (player.shownDefender[i]) {
				return defenders[i == 7 ? i : i + 1];
			}
		}
		return "Bronze";
	}

	/**
	 * Checks if the player has the required items to animate.
	 * 
	 * @param player
	 *            the player.
	 * @param suite
	 *            the suite of armour.
	 */
	private static boolean hasRequiredItems(Player player, SuiteOfArmour suite) {
		for (int piece : suite.getArmourPieces()) {
			if (!player.getItems().playerHasItem(piece)) {
				return false;
			}
		}
		return true;
	}

}
