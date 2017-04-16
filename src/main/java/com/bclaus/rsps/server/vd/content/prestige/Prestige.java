package com.bclaus.rsps.server.vd.content.prestige;

import com.bclaus.rsps.server.vd.player.Player;

public class Prestige {

	private static byte[] reqSkills = { 0, 1, 2, 4, 5, 6 };
	private static byte[] resetSkills = { 0, 1, 2, 4, 5, 6 };
	static int reqLevelForTitle[] = { 4, 12, 21 };
	private static long clickDelay;
	private static double DOUBLE_TIME = 12000;
	private static final int MAX_PRESTIGE = 30;
	private static String[] titles = { "The Warrior", "The Fallen", "The Divine", "Adequate", "The Charm", "Restless", "6ft Under", "Lucky", "Adventurer" };
	private static int[] titleReq = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	public static int checkPrestige(Player player) {
		return player.prestigeLevel;
	}

	public static int getPoints(Player player) {
		return player.prestigePoints;
	}

	public static void clickTimer() {
		if (System.currentTimeMillis() - clickDelay < 10000) {
			return;
		}
	}

	private static void resetRewards(Player player) {
		player.isDoubleLocked = true;
		player.isSpecializationLocked = true;
		player.isTitleLocked = true;
		player.doubleExpTimer = -1;
	}

	private static void updateStrings(Player player) {
		player.getPA().sendFrame126("" + checkPrestige(player), 40050);
		if (getPoints(player) > 9) {
			player.getPA().sendFrame126("" + getPoints(player), 40055);
		} else {
			player.getPA().sendFrame126(" " + getPoints(player), 40055);
		}
	}

	public static void achievePrestige(Player player) {
		clickTimer();
		if (checkPrestige(player) == MAX_PRESTIGE) {
			player.sendMessage("Sorry but you have already achieved the max Prestige level.");
			player.getPA().removeAllWindows();
			return;
		}
		if (player.gameMode == 3) {
			player.sendMessage("Sorry you can't prestige as a Regular player");
			player.getPA().removeAllWindows();
			return;
		}
		if (player.getPA().getWearingAmount() > 0) {
			player.sendMessage("Sorry but you cannot wear items when prestiging.");
			player.getPA().removeAllWindows();
			return;
		}

		for (int i = 0; i < reqSkills.length; i++) {
			if (player.playerLevel[reqSkills[i]] == 99) {
				continue;
			} else {
				player.sendMessage("You don't have all 99's in the required skills.");
				player.getPA().closeAllWindows();
				return;
			}
		}
		for (int i = 0; i < resetSkills.length; i++) {
			player.playerLevel[resetSkills[i]] = 1;
			player.playerLevel[3] = 10;
			player.playerXP[resetSkills[i]] = player.getPA().getXPForLevel(1);
			player.playerXP[3] = player.getPA().getXPForLevel(11);
			player.getPA().refreshSkill(resetSkills[i]);
			player.getPA().refreshSkill(3);
			for (int j = 0; j < player.nonCombatSpecializations.length; j++) {
				if (resetSkills[i] == player.nonCombatSpecializations[j]) {
					player.playerLevel[player.nonCombatSpecializations[j]] = 99;
					player.playerXP[player.nonCombatSpecializations[j]] = player.getPA().getXPForLevel(100);
					player.getPA().refreshSkill(player.nonCombatSpecializations[j]);
					continue;
				}
			}
		}
		player.logoutDelay = System.currentTimeMillis();
		clickDelay = System.currentTimeMillis();
		player.prestigeLevel++;
		player.prestigePoints++;
		resetRewards(player);
		player.sendMessage("Congratulations you are now Prestige " + checkPrestige(player));
		updateTitle(player);
		prestigeInterface(player);
		prestigeInterfaceSubFrame(player, 0);
		player.getPA().updateCombatLevel();
	}

	public static void prestigeInterface(Player player) {
		updateLocks(player);
		updateStrings(player);
		prestigeInterfaceSubFrame(player, 6);
		player.getPA().showInterface(40000);
	}

	public static void areaAward(Player player) {
		clickTimer();
		if (checkPrestige(player) < 1) {
			player.sendMessage("Your prestige level is not high enough, this requires 1 level prestige.");
			return;
		}
		if (!player.isAreaLocked) {
			player.sendMessage("You have already unlocked the map acheivement.");
			return;
		}
		if (player.prestigePoints <= 0) {
			player.sendMessage("You need atleast one point to activate the prestige map add-on.");
			return;
		}
		player.prestigePoints--;
		player.isAreaLocked = false;
		player.sendMessage("You have unlocked a map that is only accessable to members of your prestige.");
		clickDelay = System.currentTimeMillis();
		updateLocks(player);
		updateStrings(player);
	}

	public static void titleReward(Player player) {
		clickTimer();
		if (checkPrestige(player) < 4) {
			player.sendMessage("Your prestige is not high enough, this requires level 4 prestige.");
			return;
		}
		if (!player.isTitleLocked) {
			return;
		}
		if (player.prestigePoints == 0) {
			player.sendMessage("You need atleast one point to activate the title reward.");
			return;
		}
		if (player.titleAchievements[0] > 0 && player.titleAchievements[1] > 0 && player.titleAchievements[2] > 0) {
			player.sendMessage("You already have all titles unlocked.");
			return;
		}
		player.getDH().sendDialogues(93, 1688);
	}

	public static void sendNonCombats(Player player) {
		for (int i = 0; i < player.nonCombatSpecializations.length; i++) {
			player.sendMessage("Non combat ID for index [" + i + "] = [" + player.nonCombatSpecializations[i] + "]");
		}
	}

	public void checkNonCombatSkill(Player player) {
		clickTimer();
		if (hasAllNonCombats(player)) {
			player.sendMessage("You have already unlocked all of the non-combat skills you are allowed to unlock.");
			return;
		}
		if (checkPrestige(player) < 7) {
			player.sendMessage("Your prestige is not high enough, this requires level 7 prestige.");
			return;
		}
		if (!player.isNonCombatSpecializationLocked) {
			return;
		}
		if (player.prestigePoints == 0) {
			player.sendMessage("You need atleast one point to receive a non-combat skill as a reward.");
			return;
		}
		if (player.getOutStream() != null) {
			player.clickedNonCombat = true;
			player.getOutStream().createFrame(187);
			player.flushOutStream();
		}

	}

	public static void sendTitleFrame(Player player, int num) {
		clickTimer();
		if (player.prestigeLevel >= reqLevelForTitle[num - 1]) {
			if (player.titleAchievements[num - 1] == 0) {
				player.titleAchievements[num - 1] = 1;
				player.prestigePoints--;
				// player.sendMessage("[Required Level: "+reqLevelForTitle[num -
				// 1]+"][Title Achievements: "+player.titleAchievements[num -
				// 1]+"]");
				player.getDH().sendStartInfo("Congratulations " + player.playerName + ",", "You have just received a prestigeous title that", "you will have full access to for your entire career.", "Thank you for your time and accompanyment, The Team.", titles[num - 1]);
				updateLocks(player);
			} else {
				player.sendMessage("Sorry but this title is already unlocked.");
				player.getPA().removeAllWindows();
			}
		} else {
			player.sendMessage("You don't have the level required. You need to be on Prestige " + reqLevelForTitle[num - 1] + ".");
			player.getPA().removeAllWindows();
		}
	}

	public void readSkillName(Player player, String skillName) {
		int skillId = player.getPA().getIdForSkillName(skillName);
		if (skillId > -1) {
			for (int i = 0; i < player.nonCombatSpecializations.length; i++) {
				for (int j = 0; j < reqSkills.length; j++) {
					if (skillId == reqSkills[j]) {
						player.sendMessage("This skill cannot be mastered in because it's one of the requirements to prestige.");
						return;
					}
				}
				if (player.nonCombatSpecializations[i] == skillId) {
					player.sendMessage("You already have this skill mastered for your entire career, choose another one.");
					return;
				}
				if (player.nonCombatSpecializations[i] > -1) {
					continue;
				}
				player.playerLevel[skillId] = 99;
				player.playerXP[skillId] = player.getPA().getXPForLevel(100);
				player.getPA().refreshSkill(skillId);
				player.nonCombatSpecializations[i] = skillId;
				player.prestigePoints--;
				updateLocks(player);
				prestigeInterface(player);
				player.sendMessage("You have mastered the skill " + player.getPA().getSkillNameForId(skillId) + " and will keep it through your career.");
				return;
			}
		} else {
			player.sendMessage("Unable to locate skill, please make sure you're typing the correct skill name.");
			updateLocks(player);
			prestigeInterface(player);
		}
	}

	public static void prestigeInterfaceSubFrame(Player player, int award) {
		/*
		 * Requir: 40067 Line 1: 40068 Line 2: 40069 Line 3: 40070
		 */
		switch (award) {
		case 0:
			player.getPA().sendFrame126("", 40067);
			player.getPA().sendFrame126("         Congratulations on Prestiging!", 40068);
			player.getPA().sendFrame126("", 40069);
			player.getPA().sendFrame126("", 40070);
			break;
		case 1:
			player.getPA().sendFrame126("Requirements", 40067);
			player.getPA().sendFrame126("                         Level 1 Prestige", 40068);
			player.getPA().sendFrame126("                         1 Prestige Point", 40069);
			player.getPA().sendFrame126("                  Cannot be already active", 40070);
			break;
		case 2:
			player.getPA().sendFrame126("Requirements", 40067);
			player.getPA().sendFrame126("                         Level 3 Prestige", 40068);
			player.getPA().sendFrame126("                         1 Prestige Point", 40069);
			player.getPA().sendFrame126("                  Cannot be already active", 40070);
			break;
		case 3:
			player.getPA().sendFrame126("Requirements", 40067);
			player.getPA().sendFrame126("                         Level 4 Prestige", 40068);
			player.getPA().sendFrame126("                         1 Prestige Point", 40069);
			player.getPA().sendFrame126("", 40070);
			break;
		case 4:
			player.getPA().sendFrame126("Requirements", 40067);
			player.getPA().sendFrame126("                         Level 5 Prestige", 40068);
			player.getPA().sendFrame126("                         1 Prestige Point", 40069);
			player.getPA().sendFrame126("", 40070);
			break;
		case 5:
			player.getPA().sendFrame126("Requirements", 40067);
			player.getPA().sendFrame126("                         Level 7 Prestige", 40068);
			player.getPA().sendFrame126("                         1 Prestige Point", 40069);
			player.getPA().sendFrame126("            Must have atleast one open slot", 40070);
			break;
		case 6:
			player.getPA().sendFrame126("Requirements", 40067);
			player.getPA().sendFrame126("Click on one of the reward titles to see the", 40068);
			player.getPA().sendFrame126("                    requirements needed", 40069);
			player.getPA().sendFrame126("", 40070);
			break;
		}
	}

	public static void unlockTitles(Player player) {
		for (int i = 0; i < player.titleAchievements.length; i++) {
			player.sendMessage("[Index=" + i + "] [Contents=" + player.titleAchievements[i] + "]");
			if (player.titleAchievements[i] == 0) {
				if (player.prestigeTitle > titleReq[i]) {
					player.titleAchievements[i] = 1;
					player.sendMessage("You have unlocked the title [" + titles[i] + "]");
				}
			}
		}
	}

	public static void doubleExperienceReward(Player player) {
		clickTimer();
		updateLocks(player);
		if (checkPrestige(player) == 0) {
			player.sendMessage("Your prestige level is not high enough, this requires level 1 prestige.");
			return;
		}
		if (!player.isDoubleLocked) {
			player.sendMessage("You already have double experience time left, don't waste it.");
			return;
		}
		if (player.prestigePoints == 0) {
			player.sendMessage("You need atleast one point to activate the double experience reward.");
			return;
		}
		player.prestigePoints--;
		player.doubleExpTimer = (long) DOUBLE_TIME;
		player.isDoubleLocked = false;
		clickDelay = System.currentTimeMillis();
		updateLocks(player);
		updateStrings(player);
		player.sendMessage("Double experience started, you have two hours.");
	}

	public static void specializationSkillReward(Player player) {
		clickTimer();
		updateLocks(player);
		if (player.prestigeLevel < 3) {
			player.sendMessage("Your prestige level is not high enough, this requires level 3 prestige.");
			return;
		}
		if (!player.isSpecializationLocked) {
			player.sendMessage("You have already purchesed one 99 to keep during this prestige.");
			return;
		}
		if (player.prestigePoints == 0) {
			player.sendMessage("You need atleast one point to activate the specialization reward.");
			return;
		}
		player.getDH().sendDialogues(92, 1688);
	}

	public static void specializeInSkill(Player player, int i) {
		clickTimer();
		switch (i) {
		case 0:
			player.playerLevel[0] = 99;
			player.playerXP[0] = player.getPA().getXPForLevel(100);
			player.getPA().refreshSkill(0);
			break;
		case 1:
			player.playerLevel[1] = 99;
			player.playerXP[1] = player.getPA().getXPForLevel(100);
			player.getPA().refreshSkill(1);
			break;
		case 2:
			player.playerLevel[2] = 99;
			player.playerXP[2] = player.getPA().getXPForLevel(100);
			player.getPA().refreshSkill(2);
			break;
		case 4:
			player.playerLevel[4] = 99;
			player.playerXP[4] = player.getPA().getXPForLevel(100);
			player.getPA().refreshSkill(4);
			break;
		case 6:
			player.playerLevel[6] = 99;
			player.playerXP[6] = player.getPA().getXPForLevel(100);
			player.getPA().refreshSkill(6);
			break;
		}
		player.getPA().updateCombatLevel();
		player.getPA().removeAllWindows();
		clickDelay = System.currentTimeMillis();
		player.getPA().requestUpdates();
		player.prestigePoints--;
		player.isSpecializationLocked = false;
		updateLocks(player);
		prestigeInterface(player);
	}

	public static void updateLocks(Player player) {
		if (player.prestigePoints <= 0) {
			for (int frame = 700; frame < 705; frame++) {
				player.getPA().sendFrame36(frame, 1);
			}
			return;
		}
		if (player.isDoubleLocked) {
			if (player.prestigeLevel >= 1) {
				player.getPA().sendFrame36(700, 0); // Bright
			} else {
				player.getPA().sendFrame36(700, 1); // Dim
			}
		} else {
			player.getPA().sendFrame36(700, 1);
		}
		if (player.isSpecializationLocked) {
			if (player.prestigeLevel >= 3) {
				player.getPA().sendFrame36(701, 0);
			} else {
				player.getPA().sendFrame36(701, 1);
			}
		} else {
			player.getPA().sendFrame36(701, 1);
		}
		if (player.isTitleLocked) {
			if (player.prestigeLevel < 4) {
				player.getPA().sendFrame36(702, 1);
				return;
			}
			if (player.titleAchievements[0] == 1 && player.titleAchievements[1] == 1 && player.titleAchievements[2] == 1) {
				player.getPA().sendFrame36(702, 1);
			} else {
				player.getPA().sendFrame36(702, 0);
			}
		}
		if (player.isAreaLocked) {
			if (player.prestigeLevel >= 5) {
				player.getPA().sendFrame36(703, 0);
			} else {
				player.getPA().sendFrame36(703, 1);
			}
		} else {
			player.getPA().sendFrame36(703, 1);
		}
		if (!hasAllNonCombats(player)) {
			if (player.prestigeLevel >= 7) {
				player.getPA().sendFrame36(704, 0);
			} else {
				player.getPA().sendFrame36(704, 1);
			}
		} else {
			player.getPA().sendFrame36(704, 1);
		}
	}

	public static void updateTitle(Player player) {
		if (player.prestigeLevel < 4) {
			if (player.prestigeTitle > 4) {
				// player.sendMessage("Incorrect title mis-match. Resetting title based on current prestige level.");
				setTitle(player, player.prestigeLevel);
			} else {
				// player.sendMessage("Setting title for player under level 4.");
				setTitle(player, player.prestigeLevel);
			}
		} else {
			if (player.prestigeTitle > 30) {
				if (player.prestigeTitle == 31 && hasTitle(player, 0)) {
					setTitle(player, 31);
				} else if (player.prestigeTitle == 32 && hasTitle(player, 1)) {
					setTitle(player, 32);
				} else if (player.prestigeTitle == 33 && hasTitle(player, 2)) {
					setTitle(player, 33);
				} else {
					setTitle(player, player.prestigeLevel);
				}
			} else {
				setTitle(player, player.prestigeLevel);
			}
		}
		player.getPA().requestUpdates();
	}

	public static int setTitle(Player player, int i) {
		return player.prestigeTitle = i;
	}

	public static boolean hasTitle(Player player, int i) {
		return player.titleAchievements[i] == 1;
	}

	public static boolean hasAllNonCombats(Player player) {
		int count = 0;
		for (int i = 0; i < player.nonCombatSpecializations.length; i++) {
			if (player.nonCombatSpecializations[i] > -1) {
				count++;
			}
		}
		if (count == 5) {
			return true;
		}
		return false;
	}

	public static void handleInterface(Player player) {
		player.getPA().showInterface(40600);
		player.getPA().sendFrame126("The Warrior", 40644);
		player.getPA().sendFrame126("The Fallen", 40648);
		player.getPA().sendFrame126("The Divine", 40652);
		player.getPA().sendFrame126("Adequate", 40656);
		player.getPA().sendFrame126("The Charm", 40660);
		player.getPA().sendFrame126("Restless", 40664);
		player.getPA().sendFrame126("6ft Under", 40668);
		player.getPA().sendFrame126("Lucky", 40672);
		player.getPA().sendFrame126("Hard Eight", 40676);
		player.getPA().sendFrame126("Nine Lives", 40680);
		player.getPA().sendFrame126("Adventurer", 40684);
	}
}