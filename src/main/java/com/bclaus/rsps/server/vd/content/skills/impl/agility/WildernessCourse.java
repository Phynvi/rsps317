package com.bclaus.rsps.server.vd.content.skills.impl.agility;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.content.skills.impl.SkillHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.task.ScheduledTask;

public class WildernessCourse extends SkillHandler {

	/**
	 * Main method
	 */

	public static void handleObject(int objectId, Player p) {
		if (!isObstacle(objectId))
			return;
		switch (objectId) {

		case 2288:
			handlePipe(p);
			break;

		case 2283:
			handleRope(p);
			break;

		case 2311:
			handleStone(p);
			break;

		case 2297:
			handleLog(p);
			break;

		case 2328:
			handleRocks(p);
			break;

		}
	}

	/**
	 * Restores the player details after doing the obstacle
	 */

	public static void resetAgilityWalk(final Player c) {
		c.isRunning2 = true;
		c.getPA().sendFrame36(173, 1);
		c.playerWalkIndex = 0x333;
		c.getPA().requestUpdates();
	}

	/**
	 * Moves the player to a coordinate with a asigned animation
	 */

	private static void specialMove(final Player c, final int walkAnimation, final int x, final int y) {
		c.isRunning2 = false;
		c.getPA().sendFrame36(173, 0);
		c.playerWalkIndex = walkAnimation;
		c.getPA().requestUpdates();
		c.getPA().walkTo(x, y);
	}

	/**
	 * Checks if its a obstacle
	 */

	public static boolean isObstacle(int i) {
		switch (i) {
		case 2288: // pipe
		case 2283: // rope
		case 2311: // stone
		case 2297: // log
		case 2328: // rocks
			return true;
		}
		return false;
	}

	/**
	 * Checks if the player has passed all obstacles
	 */

	public static boolean isFinished(Player p) {
		if (p.finishedWildPipe && p.finishedWildRope && p.finishedWildStone && p.finishedWildLog && p.finishedWildRocks) {
			return true;
		}
		return false;
	}

	/**
	 * Obstacle methods
	 */

	public static void handlePipe(final Player p) {
		if (p.doingAgility) {
			return;
		}
		if (p.playerLevel[Player.playerAgility] <= 38) {
			p.sendMessage("You need 49 Agility to do this course.");
			return;
		}
		p.freezeTimer = 25;
		p.doingAgility = true;
		specialMove(p, 844, 0, 13);
		Server.getTaskScheduler().schedule(new ScheduledTask(8) {
			public void execute() {
				if (p.absY == 3950) {
					this.stop();
				}
				this.stop();
			}

			@Override
			public void onStop() {
				p.startAnimation(844);
				resetAgilityWalk(p);
				p.finishedWildPipe = true;
				p.doingAgility = false;
				p.getPA().addSkillXP(5 * AGILITY_XP, Player.playerAgility);
			}
		}.attach(p));
	}

	public static void handleRope(final Player p) {
		if (p.doingAgility) {
			return;
		}
		if(p.absY > 3953) {
			return;
		}
		if (p.playerLevel[Player.playerAgility] <= 38) {
			p.sendMessage("You need 49 Agility to do this course.");
			return;
		}
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			public void execute() {
				if (p.absY >= 3953) {
					p.doingAgility = true;
					p.turnPlayerTo(3005, 3958);
					p.startAnimation(751);
					p.count++;
				}
				if (p.count > 2) {
					this.stop();
				}

			}

			public void onStop() {
				resetAgilityWalk(p);
				p.getPA().movePlayer(3005, 3958, 0);
				p.finishedWildRope = true;
				p.doingAgility = false;
				p.count = 0;
			}
		}.attach(p));
	}
	public static void agilityWalk(final Player c, final int walkAnimation, final int x, final int y) {
		c.isRunning2 = false;
		c.getPA().sendFrame36(173, 0);
		c.playerWalkIndex = walkAnimation;
		c.getPA().requestUpdates();
		c.getPA().walkTo(x, y);
	}

	public static void handleStone(final Player c) {
		if(c.doingAgility) {
			return;
		}
		c.doingAgility = true;
		agilityWalk(c, 769, -6, 0);
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {

			int cycle = 0;

			@Override
			public void execute() {
				if (c.disconnected) {
					this.stop();
				}
				if (this.cycle == 6) {
					this.stop();
				}
				this.cycle++;
			}

			@Override
			public void onStop() {
				c.doingAgility = false;
				c.getPA().addSkillXP(75 * Constants.AGILITY_EXPERIENCE, Player.playerAgility);
				resetAgilityWalk(c);
			}
		}.attach(c));
	}

	public static void handleLog(final Player p) {
		if (p.doingAgility) {
			return;
		}
		if (p.playerLevel[Player.playerAgility] <= 38) {
			p.sendMessage("You need 49 Agility to do this course.");
			return;
		}
		p.doingAgility = true;
		specialMove(p, 762, -7, 0);
		Server.getTaskScheduler().schedule(new ScheduledTask(7) {
			public void execute() {
				this.stop();
			}

			@Override
			public void onStop() {
				resetAgilityWalk(p);
				p.getPA().addSkillXP(20 * AGILITY_XP, Player.playerAgility);
				p.finishedWildLog = true;
				p.doingAgility = false;
			}
		}.attach(p));
	}

	public static void handleRocks(final Player p) {
		if (p.playerLevel[Player.playerAgility] <= 38) {
			p.sendMessage("You need 49 Agility to do this course.");
			return;
		}
		if (p.absY >= 3937) {
			p.getPA().movePlayer(2994, 3932, 0);
			p.finishedWildRocks = true;
			p.finishedWildStone = true;
			if (isFinished(p)) {
				p.getPA().addSkillXP(125000, Player.playerAgility);
				p.sendMessage("You have completed the full wilderness agility course.");
				p.sendMessage("You have been rewarded with 125,000 Agility experience!");
				p.sendMessage("You recieve 5 Agility points.");
				p.agilityPoints += 5;
				p.getPA().loadAllQuests(p);
			} else {
				p.getPA().addSkillXP(15 * AGILITY_XP, Player.playerAgility);
			}
			p.finishedWildPipe = false;
			p.finishedWildRope = false;
			p.finishedWildStone = false;
			p.finishedWildLog = false;
			p.finishedWildRocks = false;
		}
	}

}
