package com.bclaus.rsps.server.vd.player.impl;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.task.ScheduledTask;

public class RestoreStats extends ScheduledTask {

	public RestoreStats() {
		super(120);
	}

	@Override
	public void execute() {
		for (Player player : World.PLAYERS)
			if (player != null)
				for (int level = 0; level < player.playerLevel.length; level++) {
					if (player.playerLevel[level] < Player.getLevelForXP(player.playerXP[level])) {
						if (level != 5) { // prayer doesn't restore
							player.playerLevel[level] += 1;
							player.getPA().setSkillLevel(level, player.playerLevel[level], player.playerXP[level]);
							player.getPA().refreshSkill(level);
						}
					} else if (player.playerLevel[level] > Player.getLevelForXP(player.playerXP[level]) && player.getCreaturePotionTimer() <= 0) {
						player.playerLevel[level] -= 1;
						player.getPA().setSkillLevel(level, player.playerLevel[level], player.playerXP[level]);
						player.getPA().refreshSkill(level);
					}
				}
	}

}