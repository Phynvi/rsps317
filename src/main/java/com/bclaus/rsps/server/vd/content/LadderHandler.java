package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author ViolationX
 */
public class LadderHandler {

	public static void climbLadder(final Player c, final int x, final int y, final int currentHeight, final int heightLevel) {
		if (currentHeight != c.heightLevel) {
			return;
		}
		if (!c.cantClimbLadder && c.heightLevel == currentHeight) {
			c.startAnimation(828);
			c.cantClimbLadder = true;
			Server.getTaskScheduler().schedule(new ScheduledTask(2) {
				@Override
				public void execute() {
					if (c != null && c.outStream != null)
						c.getPA().movePlayer(x, y, heightLevel);
					super.stop();
				}
			}.attach(c));

			Server.getTaskScheduler().schedule(new ScheduledTask(3) {
				@Override
				public void execute() {
					this.stop();
				}

				@Override
				public void onStop() {
					if (c != null && c.outStream != null)
						c.cantClimbLadder = false;
				}
			}.attach(c)); // this?
		}
	}

	public final static int[] Coordinates = { 2400, 3107, 2399, 3100, 2430, 3081, 2369, 3126 };

	public static void climbStairs(final Player c, final int x, final int y, final int currentHeight, final int heightLevel) {
		if (currentHeight != c.heightLevel) {
			return;
		}
		if (!c.cantClimbLadder && c.heightLevel == currentHeight) {
			c.cantClimbLadder = true;
			if (c != null && c.outStream != null)
				c.getPA().movePlayer(x, y, heightLevel);
			Server.getTaskScheduler().schedule(new ScheduledTask(3) {
				@Override
				public void execute() {
					this.stop();
				}

				@Override
				public void onStop() {
					if (c != null && c.outStream != null)
						c.cantClimbLadder = false;
				}
			}.attach(c));
		}
	}
}
