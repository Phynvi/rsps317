package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.player.Player;

/*
 * @author Tim
 */

public class SheepShear {

	public static void doAction(final Player c) {
		if (c.getItems().playerHasItem(5603)) {
			c.startAnimation(893);
			Server.getTaskScheduler().schedule(new ScheduledTask(2) {

				@Override
				public void execute() {
					c.getItems().addItem(1737, 1);
					super.stop();
				}

			}.attach(c));

		} else {
			c.sendMessage("You need a pair of shears to shear this sheep");
		}
	}

}
