package com.bclaus.rsps.server.vd.content.minigames;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.content.achievements.AchievementType;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.vd.content.achievements.Achievements;

public class Barrows {

	/**
	 * Variables used for reward.
	 */
	public static int Barrows[] = { 2680, 4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759 };
	public static int Runes[] = { 558, 560, 562, 565, 4740 };
	public static int Pots[] = { 121, 123, 125, 127, 119, 2428, 2430, 2434, 2432, 2444 };
	public static boolean cantWalk;

	/**
	 * Getting random barrow pieces.
	 * 
	 * @return
	 */
	public static int randomBarrows() {
		return Barrows[(int) (Math.random() * Barrows.length)];
	}

	/**
	 * Getting random runes.
	 * 
	 * @return
	 */
	public static int randomRunes() {
		return Runes[(int) (Math.random() * Runes.length)];
	}

	/**
	 * All of the barrow data.
	 */
	public static int[][] barrowData = {
	/** ID Coffin X Y Stair X Y */
	{ 2026, 6771, 3556, 9716, 6703, 3574, 3297 }, /** Dharoks */
	{ 2030, 6823, 3575, 9706, 6707, 3557, 3297 }, /** Veracs */
	{ 2025, 6821, 3557, 9700, 6702, 3565, 3288 }, /** Ahrims */
	{ 2029, 6772, 3568, 9685, 6706, 3554, 3282 }, /** Torags */
	{ 2027, 6773, 3537, 9703, 6704, 3577, 3282 }, /** Guthans */
	{ 2028, 6822, 3549, 9682, 6705, 3566, 3275 } /** Karils */
	};

	/**
	 * All of the spade data
	 */
	public static int[][] spadeData = {
	/** X Y X1 Y1 toX toY */
	{ 3553, 3301, 3561, 3294, 3578, 9706 }, { 3550, 3287, 3557, 3278, 3568, 9683 }, { 3561, 3292, 3568, 3285, 3557, 9703 }, { 3570, 3302, 3579, 3293, 3556, 9718 }, { 3571, 3285, 3582, 3278, 3534, 9704 }, { 3562, 3279, 3569, 3273, 3546, 9684 }, };

	/**
	 * Spade digging data
	 */
	public static void spadeDigging(Player c) {
		if (c.inArea(spadeData[0][0], spadeData[0][1], spadeData[0][2], spadeData[0][3])) {
			c.getPA().movePlayer(spadeData[0][4], spadeData[0][5], 3);
			c.sendMessage("You've broken into the Crypt!");
		} else if (c.inArea(spadeData[1][0], spadeData[1][1], spadeData[1][2], spadeData[1][3])) {
			c.getPA().movePlayer(spadeData[1][4], spadeData[1][5], 3);
			c.sendMessage("You've broken into the Crypt!");
		} else if (c.inArea(spadeData[2][0], spadeData[2][1], spadeData[2][2], spadeData[2][3])) {
			c.getPA().movePlayer(spadeData[2][4], spadeData[2][5], 3);
			c.sendMessage("You've broken into the Crypt!");
		} else if (c.inArea(spadeData[3][0], spadeData[3][1], spadeData[3][2], spadeData[3][3])) {
			c.getPA().movePlayer(spadeData[3][4], spadeData[3][5], 3);
			c.sendMessage("You've broken into the Crypt!");
		} else if (c.inArea(spadeData[4][0], spadeData[4][1], spadeData[4][2], spadeData[4][3])) {
			c.getPA().movePlayer(spadeData[4][4], spadeData[4][5], 3);
			c.sendMessage("You've broken into the Crypt!");
		} else if (c.inArea(spadeData[5][0], spadeData[5][1], spadeData[5][2], spadeData[5][3])) {
			c.getPA().movePlayer(spadeData[5][4], spadeData[5][5], 3);
			c.sendMessage("You've broken into the Crypt!");
		}
	}

	/**
	 * Stair data
	 */
	public static void useStairs(Player c) {
		switch (c.objectId) {
		case 6703:
			c.getPA().movePlayer(barrowData[0][5], barrowData[0][6], 0);
			break;
		case 6707:
			c.getPA().movePlayer(barrowData[1][5], barrowData[1][6], 0);
			break;
		case 6702:
			c.getPA().movePlayer(barrowData[2][5], barrowData[2][6], 0);
			break;
		case 6706:
			c.getPA().movePlayer(barrowData[3][5], barrowData[3][6], 0);
			break;
		case 6704:
			c.getPA().movePlayer(barrowData[4][5], barrowData[4][6], 0);
			break;
		case 6705:
			c.getPA().movePlayer(barrowData[5][5], barrowData[5][6], 0);
			break;
		}
	}

	public static void checkCoffins(Player c) {
		if (c.barrowsKillCount < 5) {
			c.sendMessage("You still have to kill the following brothers:");
			if (c.barrowsNpcs[2][1] == 0) {
				c.sendMessage("- Karils");
			}
			if (c.barrowsNpcs[3][1] == 0) {
				c.sendMessage("- Guthans");
			}
			if (c.barrowsNpcs[1][1] == 0) {
				c.sendMessage("- Torags");
			}
			if (c.barrowsNpcs[5][1] == 0) {
				c.sendMessage("- Ahrims");
			}
			if (c.barrowsNpcs[0][1] == 0) {
				c.sendMessage("- Veracs");
			}
			c.getPA().removeAllWindows();
		} else if (c.barrowsKillCount == 5) {
			NPCHandler.spawnNpc(c, 2026, c.getX(), c.getY() + 2, 3, 0, 120, 25, 200, 200, true, true);
			c.getPA().removeAllWindows();
		} else if (c.barrowsKillCount > 5) {
			c.getPA().movePlayer(3551, 9694, 0);
			c.sendMessage("You teleport to the chest.");
			c.getPA().removeAllWindows();
		}
	}

	/**
	 * Grabs the reward based on random chance depending on what your killcount
	 * is.
	 */
	public static void reward(Player c) {
		c.getItems().addItem(randomRunes(), Misc.random(150) + 100);
		c.getItems().addItem(randomRunes(), Misc.random(150) + 100);
		if (c.playerRights >= 7) {
			if (Misc.random(4) == 1)
				c.getItems().addItem(randomBarrows(), 1);
			c.sendMessage("As you open the chest you feel a sudden urgh of luck..");
		} else {
			if (c.barrowsKillCount >= 5) {
				if (Misc.random(5) == 1)
					c.getItems().addItem(randomBarrows(), 1);
					//PlayerUpdating.announce("<shad=000000><col=FF5E00>News: " + c.playerName + " has just received a rare item from the Barrows minigame!");
			}
		}
		c.barrowsChestCount += 1;
		c.sendMessage("Your Barrows chest count is: <col=ff0033>" + c.barrowsChestCount);
		Achievements.increase(c, AchievementType.BARROWS_CHEST_COUNT, 1);
		TeleportExecutor.teleport(c, new Position(3565, 3290, 0));
	}

	/**
	 * Checking if you have killed all of the brothers.
	 * 
	 * @return
	 */
	public static boolean checkBarrows(Player c) {
		if (c.barrowsNpcs[2][1] == 2 || c.barrowsNpcs[3][1] == 2 || c.barrowsNpcs[1][1] == 2 || c.barrowsNpcs[5][1] == 2 || c.barrowsNpcs[0][1] == 2 || c.barrowsNpcs[4][1] == 2) {
			return true;
		}
		return true;
	}

	/**
	 * Using the chest.
	 */
	public static void useChest(Player c) {
		if (!checkBarrows(c)) {
			c.sendMessage("You haven't killed all the brothers!");
			return;
		}
		if (c.barrowsKillCount == 5) {
			if (c.barrowsNpcs[4][1] == 0) {
				NPCHandler.spawnNpc(c, 2026, c.getX(), c.getY() - 1, 0, 0, 120, 25, 200, 200, true, true);
			}
			c.barrowsNpcs[4][1] = 1;
		}
		if (c.barrowsKillCount > 5 && checkBarrows(c)) {
			if (c.getItems().freeSlots() >= 4) {
				reward(c);
				resetBarrows(c);
				int canTryMinigame = Misc.random(4);
				if (canTryMinigame == 0) {
					cantWalk = true;
					challengeMinigame(c);
				}
			} else {
				c.sendMessage("You need more inventory slots to open the chest.");
			}
		}
	}

	public static void fixAllBarrows(Player c) {
		int totalCost = 0;
		int cashAmount = c.getItems().getItemAmount(995);
		for (int j = 0; j < c.playerItems.length; j++) {
			boolean breakOut = false;
			for (int i = 0; i < c.getItems().brokenBarrows.length; i++) {
				if (c.playerItems[j] - 1 == c.getItems().brokenBarrows[i][1]) {
					if (totalCost + 80000 > cashAmount) {
						breakOut = true;
						c.sendMessage("You have run out of money.");
						break;
					} else {
						totalCost += 80000;
					}
					c.playerItems[j] = c.getItems().brokenBarrows[i][0] + 1;
				}
			}
			if (breakOut)
				break;
		}
		if (totalCost > 0)
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995), totalCost);
	}

	public static void challengeMinigame(final Player c) {
		c.getDH().sendDialogues(23, 1);
		Server.getTaskScheduler().schedule(new ScheduledTask(18) {
			@Override
			public void execute() {
				stop();
			}

			@Override
			public void onStop() {
				c.getDH().sendDialogues(24, 1);
			}
		}.attach(c));
	}

	/**
	 * Resetting the minigame.
	 */
	public static void resetBarrows(Player c) {
		c.barrowsNpcs[0][1] = 0;
		c.barrowsNpcs[1][1] = 0;
		c.barrowsNpcs[2][1] = 0;
		c.barrowsNpcs[3][1] = 0;
		c.barrowsNpcs[4][1] = 0;
		c.barrowsNpcs[5][1] = 0;
		c.barrowsKillCount = 0;
		c.getPA().sendFrame126("Karils", 16135);
		c.getPA().sendFrame126("Guthans", 16134);
		c.getPA().sendFrame126("Torags", 16133);
		c.getPA().sendFrame126("Ahrims", 16132);
		c.getPA().sendFrame126("Veracs", 16131);
		c.getPA().sendFrame126("Dharoks", 16130);
	}

	public static final int[][] BROTHER_AREA = { { 3563, 3285, 3567, 3291 }, { 3563, 3273, 3568, 3278 }, { 3573, 3295, 3578, 3300 }, { 3554, 3294, 3560, 3300 }, { 3551, 3280, 3556, 3285 }, { 3575, 3280, 3580, 3284 }, };
	public static final int[][] PLAYER_ENTRE = { { 3557, 9703 }, { 3546, 9684 }, { 3556, 9718 }, { 3578, 9706 }, { 3568, 9683 }, { 3534, 9704 }, };

	public static boolean digToBrother(final Player c) {
		c.startAnimation(830);
		for (int i = 0; i < BROTHER_AREA.length; i++) {
			if (c.absX >= BROTHER_AREA[i][0] && c.absX <= BROTHER_AREA[i][2] && c.absY >= BROTHER_AREA[i][1] && c.absY <= BROTHER_AREA[i][3]) {

				c.getPA().movePlayer(c.teleportToX = PLAYER_ENTRE[i][0], c.teleportToY = PLAYER_ENTRE[i][1], 3);
				return true;
			}
		}
		return false;
	}

}