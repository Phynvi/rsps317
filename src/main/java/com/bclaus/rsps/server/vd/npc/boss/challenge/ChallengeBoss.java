package com.bclaus.rsps.server.vd.npc.boss.challenge;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.task.ScheduledTask;

public enum ChallengeBoss {

	// high
	KRAKEN(3847, 2464, 4782, 1500, 45, 250, 70, 2000000),

	// med
	VETION(4175, 2464, 4782, 500, 33, 300, 200, 750000),

	// low
	SCORPIANO(4172, 2464, 4782, 200, 50, 200, 120, 500000);

	ChallengeBoss(int npcId, int x, int y, int Hp, int maxHit, int attack, int defence, int cost) {
		this.npcId = npcId;
		this.x = x;
		this.y = y;
		this.Hp = Hp;
		this.maxHit = maxHit;
		this.attack = attack;
		this.defence = defence;
		this.cost = cost;
	}

	private final int npcId;

	private final int x;

	private final int y;

	private final int Hp;

	private final int maxHit;

	private final int attack;

	private final int defence;

	private final int cost;

	public static int getCost(ChallengeBoss cost) {
		return cost.cost;
	}

	public static boolean spawned = false;

	public static void spawnSignleMonster(Player c, ChallengeBoss monster) {
		int height = c.getIndex() * 4;
		c.getPA().movePlayer(2460, 4773, height);
		System.out.println("Height: " + height);
		c.sendMessage("The boss battle will begin in 10 seconds");
		spawned = true; //is this really needed? - Bowman
		Server.getTaskScheduler().submit(new ScheduledTask(10, false) {
			@Override
			public void execute() {
				//if (spawned == true) {
					NPCHandler.spawnNpc(c, monster.npcId, monster.x, monster.y, c.getIndex() * 4, 1, monster.Hp, monster.maxHit, monster.attack, monster.defence, true, false);
					this.stop();
				//}
			}
		});
	}
}
