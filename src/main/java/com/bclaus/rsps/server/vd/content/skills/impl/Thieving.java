package com.bclaus.rsps.server.vd.content.skills.impl;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

/**
 * Class Thieving Handles Thieving
 * 
 * @author PapaDoc 00:28 01/09/2010
 */

public class Thieving extends SkillHandler {

	public static void stealFromStall(final Player c, final int id, final int amount, final int xp, final int level, final int i, final int x, final int y, final int face) {
		if (c.playerLevel[Player.playerThieving] < level) {
			c.sendMessage("You need a thieving level of " + level + " to thieve from this stall.");
			return;
		}
		if (c.getItems().freeSlots() == 0 && !c.getItems().isStackable(id)) {
			c.sendMessage("You need at least 1 free slot to steal from this stall.");
			return;
		}
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void execute() {
				if (System.currentTimeMillis() - c.lastThieve < 2500) {
					stop();
					return;
				}
				if ((System.currentTimeMillis() - c.lastRandom) > 600000) {
					if (Misc.random(50) == Misc.random(50)) {
						c.executeRandom();
						return;
					}
				}
				c.startAnimation(832);
				c.getPA().addSkillXP(xp * (Constants.THIEVING_EXPERIENCE * c.diffLevel), Player.playerThieving);
				c.lastThieve = System.currentTimeMillis();
				c.getItems().addItem(id, amount);
				c.sendMessage("You steal some " + c.getItems().getItemName(id) + ".");
				if (Misc.random(12) == 0) {
					c.thievePoints += 1;
					c.sendMessage("You successfully gain 1 thieving point and now have " + c.thievePoints + ".");
				}
				stop();
			}
		}.attach(c));
	}

}