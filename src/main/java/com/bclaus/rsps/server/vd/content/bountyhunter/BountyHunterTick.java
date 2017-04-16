package com.bclaus.rsps.server.vd.content.bountyhunter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.task.ScheduledTask;

public final class BountyHunterTick extends ScheduledTask {

	private List<String> bountyHunters = new CopyOnWriteArrayList<>();
	private int updateTick = 0;
	private int targetTick = 0;

	public BountyHunterTick() {
		super(1, false);
		updateTick = 25;
		targetTick = 350;//thats fine.
	}

	@Override
	public void execute() {
		for (Player player : World.PLAYERS) {
			if ((player == null) || !player.inWild()) {
				continue;
			}
			

			if (!bountyHunters.contains(player.getName())) {
				bountyHunters.add(player.getName());
			}
		}

		if (bountyHunters.isEmpty() || (bountyHunters.size() == 0)) {
			return;
		}

		for (Iterator<String> it = new LinkedList<String>(bountyHunters).iterator(); it.hasNext(); ) {
		
			String name = it.next();

			if (name == null) {
				it.remove();
				bountyHunters.remove(name);
				continue;
			}

			Optional<Player> maybePlayer = BountyHunter.getByUsername(name);
			Player player = maybePlayer.orElse(null);

			if (player == null) {
				it.remove();
				bountyHunters.remove(name);
				continue;
			}

			if (player.getBountyHunter().hasTarget()) {
				if (player.inWild() && (player.getBountyHunter().getLeftWildernessTime() != 0)) {
					player.getBountyHunter().setLeftWilderness(0);
				}

				if (!player.inWild() && (player.getBountyHunter().getLeftWildernessTime() == 0)) {
					player.getBountyHunter().setLeftWilderness(System.currentTimeMillis() + 120_000);
					player.sendMessage("You have 2 minutes to return to the Wilderness before you lose your target.");
				}

				if (!player.inWild() && player.getBountyHunter().timeOver(0)) {
					player.getBountyHunter().setLeftWilderness(0);
					player.sendMessage("You have abandoned your target.");
					player.getBountyHunter().avoidTarget();
					it.remove();
					bountyHunters.remove(name);
					continue;
				}

				if (!player.inWild() && player.getBountyHunter().timeOver(60000)
						&& !player.getBountyHunter().timeOver(59000)) {
					player.sendMessage("You have one minute to return to the Wilderness before you lose your target.");
				}

				if (updateTick <= 0) {
					player.getBountyHunter().updateInterface();
				}
			} else {
				if (!player.inWild()) {
					it.remove();
					bountyHunters.remove(name);
					continue;
				}

				if ((player.getBountyHunter().getTargetName() != null)) {
					player.sendMessage("Your target is no longer avaliable.");
					player.getBountyHunter().reset();
					continue;
				}

				if (targetTick <= 0) {
					player.getBountyHunter().searchNextTarget(bountyHunters);
				}
			}
		}

		if (updateTick <= 0) {
			updateTick = 25;
		}

		if (targetTick <= 0) {
			targetTick = 10;
		}

		targetTick--;
		updateTick--;
	}


}
