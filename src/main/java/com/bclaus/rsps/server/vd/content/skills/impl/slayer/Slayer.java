/**
 * 
 */
package com.bclaus.rsps.server.vd.content.skills.impl.slayer;

import java.util.ArrayList;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author Jason @ http://www.rune-server.org/members/jason
 * @objective The main reason why i'm starting this on a clean slate is to
 *            incorporate as many features involved with RS Slayer as possible.
 * 
 *            Edits by Tim http://rune-server.org/members/Someone
 */
public class Slayer {

	public static final int EASY_TASK = 1, MEDIUM_TASK = 2, HARD_TASK = 3, EXTREME_TASK = 4;
	public static ArrayList<Integer> easyTask = new ArrayList<Integer>();
	public static ArrayList<Integer> mediumTask = new ArrayList<Integer>();
	public static ArrayList<Integer> hardTask = new ArrayList<Integer>();
	public static ArrayList<Integer> extremeTask = new ArrayList<Integer>();

	public enum Task {

		/**
		 * Easy
		 */
		Otherworldly_being(126, 1, 1, "Edgeville Dungeon"), 
		SKELETON(92, 1, 1, "Edgeville Dungeon"), 
		PYREFIEND(1633, 30, 1, "Fremennik Slayer Dungeon"), 
		CAVE_CRAWLER(1600, 10, 1, "Fremennik Slayer Dungeon"), 
		CRAWLING_HAND(1648, 5, 1, "Slayer Tower 1st Floor"), 
		DAGGANOTH(1341, 1, 1, "Waterbirth Island"), 
		EARTH_WARRIOR(124, 1, 1, "Edgeville Dungeon"), 
		GHOST(103, 1, 1, "Taverly Dungeon"), 
		ROCKSLUG(1622, 20, 1, "Fremennik Slayer Dungeon"),

		/**
		 * Medium Task
		 */
		TUROTH(1632, 55, 2, "Relleka Dungeon"), 
		CAVE_HORROR(4357, 58, 2, "Stronghold Security"),
		FLESH_CRAWLER(4389, 1, 2, "Stronghold Security"), 
		ANKOUS(4383, 76, 2, "Stronghold security"), 
		RED_DRAGON(53, 1, 2, "Brimhaven Dungeon"), 
		COCKATRICE(1620, 25, 2, "Fremennik Slayer Dungon"), 
		BLACK_DEMON(84, 1, 2, "Taverly Dungeon"), 
		DUST_DEVIL(1624, 65, 2, "Slayer Tower 2nd Floor"), 
		ELF_WARRIOR(1183, 1, 2, "Crash Island"), 
		BANSHEE(1612, 15, 2, "Slayer Tower 1st Floor"), 
		BASILISK(1616, 40, 2, "Fremennik Slayer Dungeon"), 
		BLOODVELD(1618, 50, 2, "Slayer Tower 2nd Floor"), 
		BLUE_DRAGON(55, 1, 2, "Taverly Dungeon"), 
		FIRE_GIANT(110, 1, 2, "Brimhaven Dungeon"), 
		GREATER_DEMON(83, 1, 2, "Brimhaven Dungeon"),  
		GREEN_DRAGON(941, 1, 2, "The Wilderness"), 
		ICE_GIANT(111, 1, 2, "Mith Dragons Teleport- Shortcut"), 
		ICE_WARRIOR(125, 1, 2, "Mith Dragons Teleport- Shortcut"), 
		INFERNAL_MAGE(1643, 45, 2, "Slayer Tower 2nd Floor"),  
		JELLY(1637, 52, 2, "Fremennik Slayer Dungeon"), 
		LESSER_DEMON(82, 1, 2, "Karamja Dungeon"), 
		HILL_GIANT(117, 1, 2, "Edgeville Dungeon"), 
		MOSS_GIANT(112, 1, 2, "Brimhaven Dungeon"),
		/**
		 * Hard
		 */
		CAVE_KRAKENS(3848, 87, 3, "Monster Teleport - Cave krakens"),
		ZAMORAK_WARRIOR(6363, 1, 3, "Stronghold Security"), 
		JUNGLE_DEMONS(1472, 1, 3, "Ape Atoll"), 
		CAVE_HORRORS(4357, 58, 3, "Stronghold Security"), 
		ANKOU(4383, 76, 3, "Stronghold security"),
		STEEL_DRAGON(3590, 1, 3, "Brimhaven Dungeon"), 
		ABYSSAL_DEMON(1615, 85, 3, "Slayer Tower 3rd Floor"), 
		CAVE_KRAKEN(3848, 87, 3, "Monster Teleport - Cave krakens"), 
		DARK_BEAST(2783, 90, 3, "Slayer Tower 3rd Floor"), 
		GARGOYLE(1610, 75, 3, "Slayer Tower 3rd Floor"), 
		GREATER_DEMON2(83, 1, 3, "Brimhaven Dungeon"), 
		GREEN_DRAGON2(941, 1, 3, "The Wilderness"), 
		HELLHOUND(49, 1, 3, "Taverly Dungeon"), 
		JELLY2(1637, 52, 3, "Fremennik Slayer Dungeon"),
		IRON_DRAGON(1591, 1, 3, "Brimhaven Dungeon"), 
		BLUE_DRAGON2(55, 1, 3, "Taverly Dungeon"), 
		KURASK(1608, 70, 3, "Relleka Dungeon"), 
		NECHRYAELS(1613, 80, 3, "Slayer Tower"),
		SPECTRES(1604, 60, 3, "Slayer Tower 2nd Floor"),

		/**
		 * Elite
		 */
		BANDOS_BOSS(6260, 1, 4, "Godwars Dungeon"), 
		ARMADYL_BOSS(6222, 1, 4, "Godwars Dungeon"), 
		ZAMORAK_BOSS(6203, 1, 4, "Godwars Dungeon"), 
		SARADOMIN_BOSS(6247, 1, 4, "Godwars Dungeon"), 
		BARRELCHEST(5666, 1, 4, "Bosses teleports"), 
		DAGANNOTH_SUPREME(2881, 1, 4, "Dagganoth Lair"), 
		DAGANNOTH_PRIME(2882, 1, 4, "Dagganoth Lair"), 
		DAGANNOTH_REX(2883, 1, 4, "Dagganoth Lair"), 
		KALPHITE_QUEEN(1160, 1, 4, "Kalphite Lair"), 
		KRAKEN(3847, 1, 4, "Kraken Teleport"),
		JUNGLE_DEMON(1472, 1, 4, "Ape atoll"),
		VENENATIS(4173, 1, 4, "Wilderness"),
		SCORPIA(4172, 1, 4, "Wilderness"),
		VETION(4175, 1, 4, "Wilderness"),
		CALISTO(4174, 1, 4, "Wilderness"),
		PENANCE(5247, 1, 4, "Wilderness"),
		JAD(2745, 1, 4, "Fight Caves Minigame");

		

		private int npcId, levelReq, diff;
		private String location;

		private Task(int npcId, int levelReq, int difficulty, String location) {
			this.npcId = npcId;
			this.levelReq = levelReq;
			this.location = location;
			this.diff = difficulty;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getLevelReq() {
			return levelReq;
		}

		public int getDifficulty() {
			return diff;
		}

		public String getLocation() {
			return location;
		}
	}

	public static void resizeTable(Player c, int difficulty) {
		if (easyTask.size() > 0 || extremeTask.size() > 0 || hardTask.size() > 0 || mediumTask.size() > 0) {
			easyTask.clear();
			mediumTask.clear();
			hardTask.clear();
			extremeTask.clear();
		}
		for (Task slayerTask : Task.values()) {
			if (slayerTask.getDifficulty() == EASY_TASK) {
				if (c.playerLevel[18] >= slayerTask.getLevelReq()) {
					easyTask.add(slayerTask.getNpcId());
					c.slayerBossTask = false;
				}
				continue;
			} else if (slayerTask.getDifficulty() == MEDIUM_TASK) {
				if (c.playerLevel[18] >= slayerTask.getLevelReq()) {
					mediumTask.add(slayerTask.getNpcId());
				}
				continue;
			} else if (slayerTask.getDifficulty() == HARD_TASK) {
				if (c.playerLevel[18] >= slayerTask.getLevelReq()) {
					hardTask.add(slayerTask.getNpcId());
				}
				continue;
			} else if (slayerTask.getDifficulty() == EXTREME_TASK) {
				if (c.playerLevel[18] >= slayerTask.getLevelReq()) {
					extremeTask.add(slayerTask.getNpcId());
				}
			}
		}
	}

	public static int getRequiredLevel(int npcId) {
		for (Task task : Task.values()) {
			if (task.npcId == npcId) {
				return task.levelReq;
			}
		}
		return -1;
	}

	public static String getLocation(int npcId) {
		for (Task task : Task.values()) {
			if (task.npcId == npcId) {
				return task.location;
			}
		}
		return "";
	}

	public static boolean isSlayerNpc(int npcId) {
		for (Task task : Task.values()) {
			if (task.getNpcId() == npcId) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSlayerTask(Player c, int npcId) {
		if (isSlayerNpc(npcId)) {
			if (c.slayerTask == npcId) {
				return true;
			}
		}
		return false;
	}

	public static int getDifficulty(int npcId) {
		try {
			for (Task task : Task.values()) {
				if (task.npcId == npcId) {
					return task.getDifficulty();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	public static String getTaskName(int npcId) {
		for (Task task : Task.values()) {
			if (task.getNpcId() == npcId) {
				return task.name().replaceAll("_", " ").replaceAll("2", "").toLowerCase();
			}
		}
		return "";
	}

	public int getTaskId(String name) {
		for (Task task : Task.values()) {
			if (task.name() == name) {
				return task.npcId;
			}
		}
		return -1;
	}

	public static boolean hasTask(Player c) {
		return c.slayerTask > 0 || c.taskAmount > 0;
	}

	public static void appendSocialSlayerTask(Player c, Player partner) {
		int slayerLevel;
		if (c.playerLevel[18] >= partner.playerLevel[18]) {
			slayerLevel = partner.playerLevel[18];
			c.sendMessage("" + c.slayerBossTask);
			if (c.slayerBossTask)
				giveMasterTask(c, partner);
			else
				giveTask(c, partner, slayerLevel);
		} else {
			slayerLevel = c.playerLevel[18];
			if (c.slayerBossTask)
				giveMasterTask(c, partner);
			else
				giveTask(c, partner, slayerLevel);
		}
	}

	public static void giveMasterTask(Player c, Player partner) {
		int given = 0;
		int chooseMonster = 0;
		chooseMonster = Misc.random(MASTER_TASKS.length - 1);
		given = MASTER_TASKS[chooseMonster][0];
		if (partner == null)
			c.slayerTask = given;
		else {
			partner.slayerTask = given;
			c.slayerTask = given;
		}
		if (partner == null) {
			c.taskAmount = Misc.random(12) + 8;
			c.getDH().sendDialogues(9090, 9085);
		} else {
			int taskAmount = 10 + Misc.random(15);
			c.taskAmount = taskAmount;
			partner.taskAmount = taskAmount;
			c.getDH().sendDialogues(9090, 9085);
			partner.getDH().sendDialogues(9090, 9085);
		}
	}

	public static void giveTask(Player c, Player partner, int slayerLevel) {
		int slayerLevel2 = 1;
		if (partner == null)
			slayerLevel2 = Player.getLevelForXP(c.playerXP[18]);
		else
			slayerLevel2 = slayerLevel;
		if (c.taskAmount < 1) {
			if (slayerLevel2 < 25)
				giveTask(c, 1, partner);
			else if (slayerLevel2 >= 25 && slayerLevel2 < 70)
				giveTask(c, 2, partner);
			else if (slayerLevel2 >= 70 && slayerLevel2 <= 99)
				giveTask(c, 3, partner);
			else
				giveTask(c, 2, partner);
		} else {
			c.sendMessage("You already have a task!");
		}
	}

	public static void generateBossTask(Player c, Player partner) {
		if (hasTask(c) && !c.needsNewTask) {
			c.getDH().sendDialogues(1414, 1596);
			return;
		}
		c.slayerBossTask = true;
		int taskLevel = getSlayerDifficulty(c);
		for (Task slayerTask : Task.values()) {
			if (slayerTask.getDifficulty() == taskLevel) {
				if (c.playerLevel[18] >= slayerTask.getLevelReq()) {
					resizeTable(c, taskLevel);
					if (!c.needsNewTask) {
						int task = getRandomTask(taskLevel);
						for (int i = 0; i < c.removedTasks.length; i++) {
							if (task == c.removedTasks[i]) {
								c.sendMessage("Unavailable task: " + task);
								generateTask(c, null);
								return;
							}
						}
						c.slayerTask = task;
						c.taskAmount = getTaskAmount(taskLevel);
					} else {
						int task = getRandomTask(getDifficulty(taskLevel - 1));
						for (int i = 0; i < c.removedTasks.length; i++) {
							if (task == c.removedTasks[i]) {
								c.sendMessage("Unavailable task: " + task);
								generateTask(c, null);
								return;
							}
						}
						c.slayerTask = task;
						c.taskAmount = getTaskAmount(getDifficulty(c.slayerTask) - 1);
						c.needsNewTask = false;
					}
					c.getDH().sendDialogues(1413, 1597);
					c.sendMessage("You have been assigned " + c.taskAmount + " " + getTaskName(c.slayerTask) + ", Good luck " + c.playerName + ".");
					return;
				}
			}
		}
	}

	public static void generateTask(Player c, Player partner) {
		if (hasTask(c) && !c.needsNewTask) {
			c.getDH().sendDialogues(1414, 1596);
			return;
		}
		if (hasTask(c) && c.needsNewTask) {
			int difficulty = getDifficulty(c.slayerTask);
			if (difficulty == EASY_TASK) {
				c.getDH().sendDialogues(1416, 1597);
				c.needsNewTask = false;
				return;
			}
		}
		int taskLevel = getSlayerDifficulty(c);
		for (Task slayerTask : Task.values()) {
			if (slayerTask.getDifficulty() == taskLevel) {
				if (c.playerLevel[18] >= slayerTask.getLevelReq()) {
					resizeTable(c, taskLevel);
					if (!c.needsNewTask) {
						int task = getRandomTask(taskLevel);
						for (int i = 0; i < c.removedTasks.length; i++) {
							if (task == c.removedTasks[i]) {
								c.sendMessage("Unavailable task: " + task);
								generateTask(c, null);
								return;
							}
						}
						c.slayerTask = task;
						c.taskAmount = getTaskAmount(taskLevel);
					} else {
						int task = getRandomTask(getDifficulty(taskLevel - 1));
						for (int i = 0; i < c.removedTasks.length; i++) {
							if (task == c.removedTasks[i]) {
								c.sendMessage("Unavailable task: " + task);
								generateTask(c, null);
								return;
							}
						}
						c.slayerTask = task;
						c.taskAmount = getTaskAmount(getDifficulty(c.slayerTask) - 1);
						c.needsNewTask = false;
					}
					c.getDH().sendDialogues(1413, 1597);
					c.sendMessage("You have been assigned " + c.taskAmount + " " + getTaskName(c.slayerTask) + ", Good luck " + c.playerName + ".");
					return;
				}
			}
		}
	}

	public static int getTaskAmount(int diff) {
		switch (diff) {
		case 1:
			return 22 + Misc.random(45);
		case 2:
			return 22 + Misc.random(45);
		case 3:
			return 22 + Misc.random(35);
		case 4:
			return 8 + Misc.random(29);
		}
		return 20 + Misc.random(45);
	}

	public static int getRandomTask(int diff) {
		if (diff == EASY_TASK) {
			return easyTask.get(Misc.random(easyTask.size() - 1));
		} else if (diff == MEDIUM_TASK) {
			return mediumTask.get(Misc.random(mediumTask.size() - 1));
		} else if (diff == HARD_TASK) {
			return hardTask.get(Misc.random(hardTask.size() - 1));
		} else if (diff == EXTREME_TASK) {
			return extremeTask.get(Misc.random(extremeTask.size() - 1));
		}
		return easyTask.get(Misc.random(easyTask.size() - 1));
	}

	public static int getSlayerDifficulty(Player c) {
		if (c.combatLevel > 0 && c.combatLevel <= 45) {
			return EASY_TASK;
		} else if (c.combatLevel > 45 && c.combatLevel <= 90) {
			return MEDIUM_TASK;
		} else if (c.slayerBossTask) {
			return EXTREME_TASK;
		} else if (c.combatLevel > 90) {
			return HARD_TASK;
		}
		return EASY_TASK;
	}

	public static void giveTask(Player c, int taskLevel, Player socialPartner) {
		try {
			int given = 0;
			int chooseMonster = 0;
			if (taskLevel == 1) {
				chooseMonster = Misc.random(LOW_TASKS.length - 1);
				given = LOW_TASKS[chooseMonster][0];
			} else if (taskLevel == 2) {
				chooseMonster = Misc.random(MED_TASKS.length - 1);
				given = MED_TASKS[chooseMonster][0];
			} else if (taskLevel == 3) {
				chooseMonster = Misc.random(HIGH_TASKS.length - 1);
				given = HIGH_TASKS[chooseMonster][0];
			}
			if (c.playerLevel[18] < getMonsterLevelRequirement(given)) {
				if (socialPartner == null)
					giveTask(c, taskLevel, null);
				else
					giveTask(c, taskLevel, socialPartner);
				return;
			}
			if (socialPartner != null) {
				int amount = 60 + Misc.random(40);
				socialPartner.slayerTask = given;
				socialPartner.taskAmount = amount;
				c.slayerTask = given;
				c.taskAmount = amount;
			} else {
				c.slayerTask = given;
				c.taskAmount = Misc.random(20) + 20;
			}
			if (socialPartner == null) {
				c.sendMessage("You have been assigned to kill " + c.taskAmount + " " + NPCHandler.getNpcListName(given).replace("_", " ") + "'s as a slayer task.");

			} else {
				c.sendMessage("You have been assigned to kill " + c.taskAmount + " " + NPCHandler.getNpcListName(given).replace("_", " ") + "'s as a social slayer task.");

				socialPartner.sendMessage("You have been assigned to kill " + c.taskAmount + " " + NPCHandler.getNpcListName(given).replace("_", " ") + "'s as a social slayer task.");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static int getMonsterLevelRequirement(int npcId) {
		for (int i = 0; i < LOW_TASKS.length; i++)
			if (LOW_TASKS[i][0] == npcId)
				return LOW_TASKS[i][1];
		for (int i = 0; i < MED_TASKS.length; i++)
			if (MED_TASKS[i][0] == npcId)
				return MED_TASKS[i][1];
		for (int i = 0; i < HIGH_TASKS.length; i++)
			if (HIGH_TASKS[i][0] == npcId)
				return HIGH_TASKS[i][1];
		for (int i = 0; i < MASTER_TASKS.length; i++)
			if (MASTER_TASKS[i][0] == npcId)
				return MASTER_TASKS[i][1];
		return 1;
	}

	private static final int LOW_TASKS[][] = { { 1648, 5 }, { 103, 1 }, { 78, 1 }, { 18, 1 }, { 101, 1 }, { 181, 1 }, { 1265, 1 }, { 1612, 15 }, { 1632, 20 }, { 1632, 10 } };

	private static final int MED_TASKS[][] = { { 1643, 45 }, { 1612, 15 }, { 119, 1 }, { 82, 1 }, { 52, 1 }, { 117, 1 }, { 112, 1 }, { 1589, 1 }, { 110, 1 }, { 1620, 25 }, { 1633, 30 }, { 1616, 40 }, { 1637, 52 }, { 1618, 50 }, { 1624, 65 }, { 1615, 75 }, { 2783, 90 }, };

	private static final int HIGH_TASKS[][] = { { 54, 75 }, { 1591, 82 }, { 1610, 75 }, { 1613, 80 }, { 2783, 90 }, { 1615, 85 }, { 1592, 92 }, { 55, 1 }, { 84, 1 }, { 49, 1 }, { 1183, 1 }, { 2265, 1 }, { 2264, 1 }, { 941, 1 }, { 63, 1 }, { 53, 1 }, { 1341, 1 }, { 1608, 1 }, { 1637, 52 }, { 1618, 50 }, { 1624, 65 }, { 1615, 75 }, { 2783, 90 }, };

	public static final int MASTER_TASKS[][] = { { 6260, 1 }, { 6222, 1 }, { 6203, 1 }, {6247, 1}, {5666, 1}, {2881, 1}, {2882, 1},{2883,1}, {1160, 1}, {3847, 1}, {4174, 1}, {4173, 1}, {4172, 1}, {4175, 1}, {5247, 1}};

	public static boolean isWearingBlackMask(Player c) {
		return c.playerEquipment[Player.playerHat] >= 8901 && c.playerEquipment[Player.playerHat] <= 8919;
	}

	public static void handleInterface(Player player, String shop) {
		if (shop.equalsIgnoreCase("buy")) {
			player.getPA().sendFrame126("Slayer Points: " + player.slaypoints, 27108);
			player.getPA().showInterface(27100);
		} else if (shop.equalsIgnoreCase("learn")) {
			player.getPA().sendFrame126("Slayer Points: " + player.slaypoints, 22208);
			player.getPA().showInterface(22200);
		} else if (shop.equalsIgnoreCase("assignment")) {
			player.getPA().sendFrame126("Slayer Points: " + player.slaypoints, 24208);
			updateCurrentlyRemoved(player);
			player.getPA().showInterface(24200);
		}
	}

	public static void cancelTask(Player player) {
		if (!hasTask(player)) {
			player.sendMessage("You must have a task to cancel first.");
			return;
		}
		if (player.slaypoints < 30) {
			player.sendMessage("This requires atleast 30 slayer points, which you don't have.");
			return;
		}
		player.slayerBossTask = false;
		player.sendMessage("You have cancelled your current task of " + player.taskAmount + " " + getTaskName(player.slayerTask) + ".");
		player.slayerTask = -1;
		player.taskAmount = 0;
		player.slaypoints -= 30;
	}

	public static void removeTask(Player player) {
		int counter = 0;
		if (!hasTask(player)) {
			player.sendMessage("You must have a task to remove first.");
			return;
		}
		if (player.slaypoints < 100) {
			player.sendMessage("This requires atleast 100 slayer points, which you don't have.");
			return;
		}
		for (int i = 0; i < player.removedTasks.length; i++) {
			if (player.removedTasks[i] != -1) {
				counter++;
			}
			if (counter == 4) {
				player.sendMessage("You don't have any open slots left to remove tasks.");
				return;
			}
			if (player.removedTasks[i] == -1) {
				player.removedTasks[i] = player.slayerTask;
				player.slaypoints -= 100;
				player.slayerTask = -1;
				player.taskAmount = 0;
				player.sendMessage("Your current slayer task has been removed, you can't obtain this task again.");
				updateCurrentlyRemoved(player);
				return;
			}
		}
	}

	public static void updatePoints(Player player) {
		player.getPA().sendFrame126("Slayer Points: " + player.slaypoints, 41011);
		player.getPA().sendFrame126("Slayer Points: " + player.slaypoints, 41511);
		player.getPA().sendFrame126("Slayer Points: " + player.slaypoints, 42011);
	}

	public static void updateCurrentlyRemoved(Player player) {
		int line[] = { 24215, 24216, 24217, 24218 };
		for (int i = 0; i < player.removedTasks.length; i++) {
			if (player.removedTasks[i] != -1) {
				// c.getPA().sendFrame126(getTaskName(c.removedTasks[i]),
				// line[i]);
				player.getPA().sendFrame126(Server.npcHandler.getNpcName(player.removedTasks[i]), line[i]);
			} else {
				player.getPA().sendFrame126("", line[i]);
			}
		}
	}

	public static void buySlayerExperience(Player player) {
		if (System.currentTimeMillis() - player.buySlayerTimer < 500) {
			return;
		}
		if (player.slaypoints < 50) {
			player.sendMessage("You need at least 50 slayer points to purchase Experience");
			player.sendMessage("Regulars receive 100k exp, legends 50k and Iron Men 250k.");
			return;
		}
		player.buySlayerTimer = System.currentTimeMillis();
		player.slaypoints -= 50;
		if (player.gameMode == 0) {
			player.getPA().addSkillXP(100000, 18);
			player.sendMessage("You spend 50 slayer points and gain 100k experience in slayer.");
		}
		if (player.gameMode == 2) {
			player.getPA().addSkillXP(50000, 18);
			player.sendMessage("You spend 50 slayer points and gain 50k experience in slayer.");
		}
		if (player.gameMode == 3) {
			player.getPA().addSkillXP(50000, 18);
			player.sendMessage("You spend 50 slayer points and gain 50k experience in slayer.");
		}
		player.getPA().refreshSkill(18);
		updatePoints(player);
	}

	public static void buySlayerDart(Player player) {
		if (System.currentTimeMillis() - player.buySlayerTimer < 500) {
			return;
		}
		if (player.slaypoints < 35) {
			player.sendMessage("You need at least 35 slayer points to buy Slayer darts.");
			return;
		}
		if (player.getItems().freeSlots() < 2 && !player.getItems().playerHasItem(560) && !player.getItems().playerHasItem(558)) {
			player.sendMessage("You need at least 2 free lots to purchase this.");
			return;
		}

		player.buySlayerTimer = System.currentTimeMillis();
		player.slaypoints -= 35;
		player.sendMessage("You spend 35 slayer points and aquire 250 casts of Slayer darts.");
		player.getItems().addItem(558, 1000);
		player.getItems().addItem(560, 250);
		updatePoints(player);
	}

	public static void buyCape(Player player) {
		if (System.currentTimeMillis() - player.buySlayerTimer < 500) {
			return;
		}
		if (player.slaypoints < 300) {
			player.sendMessage("You need at least 300 slayer points to buy an untrimmed cape.");
			return;
		}
		if (player.getItems().freeSlots() < 1 && !player.getItems().playerHasItem(4160)) {
			player.sendMessage("You need at least 1 free lot to purchase this.");
			return;
		}
		player.buySlayerTimer = System.currentTimeMillis();
		player.slaypoints -= 300;
		player.sendMessage("You spend 300 slayer points and aquire an untrimmed slayer-cape.");
		player.getItems().addItem(9786, 1);
		updatePoints(player);
	}

	private static boolean disable = true;

	public static void buyRing(Player player) {
		if (disable) {
			player.sendMessage("The ring has not yet been released");
			return;
		}
		if (System.currentTimeMillis() - player.buySlayerTimer < 1000) {
			return;
		}
		if (player.slaypoints < 100) {
			player.sendMessage("You need at least 100 slayer points to buy a ring of Slaying.");
			return;
		}
		player.buySlayerTimer = System.currentTimeMillis();
		player.slaypoints -= 100;
		player.sendMessage("You spend 100 slayer points and aquire a ring of Slaying!.");
		player.getItems().addItem(5841, 1);
		updatePoints(player);
	}

	public static void buySlayerHelm(Player player) {
		if (System.currentTimeMillis() - player.buySlayerTimer < 1000) {
			return;
		}
		if (player.slaypoints < 500) {
			player.sendMessage("You need at least 500 slayer points to buy a Slayer Helmet.");
			return;
		}
		player.buySlayerTimer = System.currentTimeMillis();
		player.slaypoints -= 500;
		player.sendMessage("You spend 500 slayer points and aquire a a Slayer helmet.");
		player.getItems().addItem(13263, 1);
		updatePoints(player);
	}
}
