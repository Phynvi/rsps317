/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.vd.content.instanced.impl;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.content.instanced.InstancedAreaManager;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.player.Boundary;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.vd.content.instanced.SingleInstancedArea;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.task.ScheduledTask;

public class Zulrah {

	private SingleInstancedArea instance;

	/**
	 * @param player
	 */

	public void start(Player player) {
		instance = (SingleInstancedArea) InstancedAreaManager.getSingleton().createSingleInstancedArea(player, Boundary.ZULRAH);
		TeleportExecutor.teleport(player, new Position(3488, 3269, instance.getHeight()));
		spawnNextWave(player);
	}

	private final static int[][] COORDINATES = { { 3493, 3257 } };

	private static final int[][] NPCS = { {2042} };

	private int waveId = 0;

	public void spawnNextWave(Player player) {
		Server.getTaskScheduler().schedule(new ScheduledTask(6) {
			@Override
			public void execute() {
				this.stop();
			}

			@Override
			public void onStop() {
				int npcAmount = NPCS[waveId].length;
				for (int j = 0; j < npcAmount; j++) {
					int npc = NPCS[waveId][j];
					int X = COORDINATES[j][0];
					int Y = COORDINATES[j][1];
					NPCHandler.spawnNpc(player, npc, X, Y, instance.getHeight(), 0, 500, 45, 70, 215, true, false);
				}

			}
		}.attach(this));
	}

	public void spawnExplorer(Player player) {
		Server.getTaskScheduler().schedule(new ScheduledTask(3) {
			@Override
			public void execute() {
				this.stop();
			}

			@Override
			public void onStop() {
				NPCHandler.spawnNpc(player, 836, 2899, 3609, instance.getHeight(), 0, 0, 0, 0, 0, false, true);
			}

		}.attach(this));
	}

	public static int getHp(int npc) {
		switch (npc) {
		case 2042:
			return 500;
		}
		return 500;
	}

	public static int getMax(int npc) {
		switch (npc) {
		case 2042:
			return 45;
		}
		return 45;
	}

	public static int getAtk(int npc) {
		switch (npc) {
		case 2042:
			return 70;
		}
		return 70;
	}

	public static int getDef(int npc) {
		switch (npc) {
		case 2042:
			return 215;
		}
		return 215;
	}

	public SingleInstancedArea getInstance() {
		return instance;
	}

}
