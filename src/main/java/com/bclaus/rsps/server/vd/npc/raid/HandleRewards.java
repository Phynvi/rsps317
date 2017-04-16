/* package server.vd.npc.raid;

import java.util.ArrayList;

import World;
import Player;

public class HandleRewards {
	public static ArrayList<Player> killers = new ArrayList<Player>();

	private static Player player;

	public static boolean rewards = false;

	public static Player getPlayer() {
		return player;
	}

	public static void handleReward(Player c) {
		System.out.println("exectuing");
		for (int j = 0; j < World.PLAYERS.size(); j++) {
			if (World.PLAYERS.get(j) != null) {
				Player c2 = (Player) World.PLAYERS.get(j);
				if (c2.totalDamageDealt > 0)
					killers.add(c2);
			}
		}
		for (int j = 0; j < killers.size(); j++) {
			Player z = killers.get(j);
			if (z.totalDamageDealt > 1000 && z.totalDamageDealt < 4000)
				RaidRewards.getRandomItem(c, RaidRewards.LOW);
			else if (z.damagedealt > 4000)
				RaidRewards.getRandomItem(c, RaidRewards.HIGH);
			else
				z.sendMessage("You didn't do enough damage, better luck next time.");
			z.totalDamageDealt = 0;
		}
	}
} */