/**
 * 
 */
package com.bclaus.rsps.server.vd.content.minigames.mercenary;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.DialogueHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.task.ScheduledTask;

/**
 * @author Tim http://rune-server.org/members/Someone
 *
 */
public class Mercenary {

	static boolean gameStarted = true;
	private static int bossId = 5902, minionId = 5903;

	static int[] bossCoords = { 2187, 3153, 0, 2187, 3147, 0, 3087, 3500, 0 };

	public static void startGame(Player player) {
		player.getPA().movePlayer(bossCoords[3], bossCoords[4], bossCoords[5]);
		DialogueHandler.sendStatement(player, "You're now in the dungeon.");
		minionSpawn(player);
		bossSpawn(player);
	}

	public static void minionSpawn(final Player c) {
		Server.getTaskScheduler().schedule(new ScheduledTask(3) {
			@Override
			public void execute() {
				NPCHandler.spawnNpc(c, minionId, bossCoords[0], bossCoords[1], 0, 0, 140, 45, 100, 200, true, true);
				stop();
			}

			@Override
			public void onStop() {
				DialogueHandler.sendStatement(c, "The minion has just spawned!");
				gameStarted = true;
			}
		}.attach(c));
	}

	public static void bossSpawn(final Player player) {
		Server.getTaskScheduler().schedule(new ScheduledTask(35) {
			@Override
			public void execute() {
				DialogueHandler.sendStatement(player, "The boss is about to spawn get ready!");
				NPCHandler.spawnNpc(player, bossId, bossCoords[0], bossCoords[1], 0, 0, 240, 45, 300, 300, true, true);
				stop();
			}

			@Override
			public void onStop() {
				DialogueHandler.sendStatement(player, "Watch out, Inadequacy is here!");
				gameStarted = true;
			}
		}.attach(player));
	}

}
