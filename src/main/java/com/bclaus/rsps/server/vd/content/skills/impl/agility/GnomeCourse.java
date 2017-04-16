package com.bclaus.rsps.server.vd.content.skills.impl.agility;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.content.skills.impl.SkillHandler;
import com.bclaus.rsps.server.vd.player.Player;

public class GnomeCourse extends SkillHandler {

	/**
	 * Main method
	 */

	public static void handleObject(int objectId, Player p) {
		if (!isObstacle(objectId))
			return;
		switch (objectId) {

		case 2295:
			handleLog(p);
			p.freezeTimer = 2;
			break;

		case 2285:
			handleNet1(p);
		p.freezeTimer = 2;
			break;

		case 2313:
			handleBranch1(p);
			p.freezeTimer = 2;
			break;

		case 2312:
			handleRope(p);
			p.freezeTimer = 2;
			break;

		case 2314:
		case 2315:
			handleBranch2(p);
			p.freezeTimer = 2;
			break;

		case 2286:
			handleNet2(p);
			p.freezeTimer = 2;
			break;

		case 154:
		case 4058:
			handlePipe(p);
			p.freezeTimer = 15;
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
		c.doingAgility = false;
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
		case 2295: // log
		case 2285: // net1
		case 2313: // branch1
		case 2312: // rope
		case 2314: // branch2
		case 2315: // branch2
		case 2286: // net2
		case 154: // pipe left
		case 4058: // pipe right
			return true;
		}
		return false;
	}

	/**
	 * Checks if the player has passed all obstacles
	 */

	public static boolean isFinished(Player p) {
		if (p.finishedLog && p.finishedNet1 && p.finishedBranch1 && p.finishedRope && p.finishedBranch2 && p.finishedNet2 && p.finishedPipe) {
			return true;
		}
		return false;
	}

	/**
	 * Obstacle methods
	 */

	public static void handleLog(final Player p) {
		if (p.doingAgility) {
			return;
		}
		p.doingAgility = true;
		specialMove(p, 762, 0, -7);
		Server.getTaskScheduler().schedule(new ScheduledTask(7) {
			@Override
			public void execute() {
				if (p.absY == 3429) {
					this.stop();
				}
				this.stop();
			}

			@Override
			public void onStop() {
				resetAgilityWalk(p);
				p.getPA().addSkillXP(12 * AGILITY_XP, Player.playerAgility);
				p.freezeTimer = 2;
				p.finishedLog = true;
				p.doingAgility = false;
			}
		}.attach(p));
	}

	public static void handleNet1(final Player p) {
		if (p.doingAgility) {
			return;
		}
		p.doingAgility = true;
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void execute() {
				this.stop();
			}

			@Override
			public void onStop() {
				p.freezeTimer = 2;
				p.startAnimation(828);
				p.getPA().movePlayer(p.absX, 3424, 1);
				p.getPA().addSkillXP((int) 7.5 * AGILITY_XP, Player.playerAgility);
				p.doingAgility = false;
				p.finishedNet1 = true;
			}
		}.attach(p));
	}

	public static void handleBranch1(final Player p) {
		p.freezeTimer = 2;
		p.startAnimation(828);
		p.getPA().movePlayer(2473, 3420, 2);
		p.getPA().addSkillXP(5 * AGILITY_XP, Player.playerAgility);
		p.finishedBranch1 = true;
	}

	public static void handleRope(final Player p) {
		if (p.doingAgility) {
			return;
		}
		p.doingAgility = true;
		specialMove(p, 762, 6, 0);
		Server.getTaskScheduler().schedule(new ScheduledTask(6) {
			@Override
			public void execute() {
				if (p.absX == 2483) {
					this.stop();
				}
				this.stop();
			}

			@Override
			public void onStop() {
				resetAgilityWalk(p);
				p.getPA().addSkillXP(7 * AGILITY_XP, Player.playerAgility);
				p.finishedRope = true;
				p.doingAgility = false;
			}
		}.attach(p));
	}

	public static void handleBranch2(final Player p) {
		// p.setAnimation(Animation.create(828));
		p.freezeTimer = 2;
		p.startAnimation(828);
		p.getPA().movePlayer(p.absX, p.absY, 0);
		p.getPA().addSkillXP(5 * AGILITY_XP, Player.playerAgility);
		p.finishedBranch2 = true;
	}

	public static void handleNet2(final Player p) {
		if (p.doingAgility) {
			return;
		}
		if(p.absY == 3425) {
			p.doingAgility = true;
			p.startAnimation(828);
			Server.getTaskScheduler().schedule(new ScheduledTask(4) {
				@Override
				public void execute() {
					p.getPA().movePlayer(p.absX, 3427, 0);
					this.stop();
				}

				@Override
				public void onStop() {
					p.turnPlayerTo(p.absX, 3426);
					p.getPA().addSkillXP(8 * AGILITY_XP, Player.playerAgility);
					p.finishedNet2 = true;
					p.doingAgility = false;
				}
			}.attach(p));
		}
	}

	public static void handlePipe(final Player p) {
		if (p.doingAgility) {
			return;
		}
		if (p.absX != 2484 && p.absY != p.objectY - 1) {
			p.getPA().walkTo(2484 - p.absX, (p.objectY - 1) - p.absY);
			return;
		}
		if (p.absX == 2484 && p.absY == 3430) {
			p.freezeTimer = 20;
			p.doingAgility = true;
			specialMove(p, 844, 0, 7);
			Server.getTaskScheduler().schedule(new ScheduledTask(4) {
				@Override
				public void execute() {
					if (p.absY == 3437) {
						this.stop();
					}
					this.stop();
				}

				@Override
				public void onStop() {
					p.startAnimation(844);
					p.freezeTimer = 2;
					p.finishedPipe = true;
					resetAgilityWalk(p);
					if (isFinished(p)) {
						p.getPA().addSkillXP(25000, Player.playerAgility);
						p.sendMessage("You have completed the full gnome agility course.");
						p.sendMessage("You gain 1 agility point.");
						p.agilityPoints += 1;
						p.getPA().loadAllQuests(p);
					} else {
						p.getPA().addSkillXP(7 * AGILITY_XP, Player.playerAgility);
					}
					p.finishedLog = false;
					p.finishedNet1 = false;
					p.finishedBranch1 = false;
					p.finishedRope = false;
					p.finishedBranch2 = false;
					p.finishedNet2 = false;
					p.finishedPipe = false;
				}
			}.attach(p));
		}
	}

}
