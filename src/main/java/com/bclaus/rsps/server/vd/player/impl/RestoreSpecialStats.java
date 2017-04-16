package com.bclaus.rsps.server.vd.player.impl;

import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.content.consumables.Potions;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;

public class RestoreSpecialStats extends ScheduledTask {

	public RestoreSpecialStats() {
		super(8);
	}

	private int counter = 0;

	@Override
	public void execute() {
		counter++;
		for (Player player : World.PLAYERS) {
			if (player != null) {
				if (player.getCreaturePotionTimer() > 0) {
					if (!player.inWild() && !player.isDead) {
						for (int i = 0; i < 7; i++) {
							if (i != 3 && i != 5) {
								if (player.playerLevel[i] < Player.getLevelForXP(player.playerXP[i]) + 26) {
									player.playerLevel[i] = Player.getLevelForXP(player.playerXP[i]) + 26;
									player.getPA().refreshSkill(i);
								}
							}
						}
						player.setCreaturePotionTimer(player.getCreaturePotionTimer() - 1);
						if (player.getCreaturePotionTimer() == 20) {
							player.sendMessage("<col=ff0033>Your Super combat potion has almost expired!");
						}
						if (player.getCreaturePotionTimer() == 0) {
							Potions.resetCreatureCombat(player);
						}
					} else {
						Potions.resetCreatureCombat(player);
					}
				}

				if (counter >= 4) {
					if (player.specAmount < 10) {
						player.specAmount += .5;
						if (player.specAmount > 10) {
							player.specAmount = 10;
						}
						player.getItems().addSpecialBar(player.playerEquipment[Player.playerWeapon]);
					}
				}
			}
		}
		if (counter >= 4)
			counter = 0;
	}
}
