package com.bclaus.rsps.server.vd.npc.raid;

import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.task.ScheduledTask;

public class Raid extends ScheduledTask {

	public static boolean dead = false;
	public static boolean first = false;

	public Raid() {
		super(70);
		System.out.println("dsad");
	}

	public static Boss spawn = Boss.random();

	public static int getSpawn() {
		return spawn.getID();
	}

	public static String getName() {
		return spawn.getName();
	}

	@Override
	public void execute() {
		if (!dead && first) {
			System.out.println("The last raid boss has yet to die! Kill him at: " + spawn.getMessage());
			return;
		}

		if (!first)
			first = true;
		dead = false;
		System.out.println("first spawn");
		NPCHandler.spawnNpc2(getSpawn(), spawn.getCoordinateX(), spawn.getCoordinateY(), 0, 0, spawn.getHP(), spawn.getMaxHit(), spawn.getAttack(), spawn.getDefence());
		
	}
}
