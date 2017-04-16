package com.bclaus.rsps.server.vd.content.achievements;

import java.util.EnumSet;
import java.util.Set;

import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;

/**
 * 
 * @author Jason http://www.rune-server.org/members/jason
 * @date Mar 26, 2014
 */
public class Achievements {

	public enum Achievement {
		/**
		 * Tier 1
		 */
		LEARN_THE_ROPES(0, AchievementTier.TIER_1, AchievementType.ROPES, null, "Complete the beginner tutorial", 1, 1), RESPECT_THE_AUTHORATA(1, AchievementTier.TIER_1, AchievementType.SITE, null, "Type ::terms to agree to our TOS", 1, 1), SMALL_TIME_VOTER(2, AchievementTier.TIER_1, AchievementType.VOTE, null, "Vote atleast once and receive a reward", 1, 1), PESKY_PROTECTION(3, AchievementTier.TIER_1, AchievementType.PROTECTION, null, "Play over an hour to get rid of player-protection", 1, 1), PEST_CONTROL(4, AchievementTier.TIER_1, AchievementType.PEST_CONTROL, null, "Win a game of Pest control", 1, 1), HELP_CENTRE(5, AchievementTier.TIER_1, AchievementType.HELP, null, "Request help via ::help", 1, 1), CREATE_A_CLAN(6, AchievementTier.TIER_1, AchievementType.CLAN, null, "Create your own clanchat", 1, 1), PVM_NOVICE(7, AchievementTier.TIER_1, AchievementType.SLAYER, null, "Complete your first slayer task", 1, 1), TEAM_PLAYER(8, AchievementTier.TIER_1, AchievementType.SOCIAL_SLAYER, null, "Complete a social-slayer task with a friend", 1, 1), START_OF_SOMETHING_NEW(9, AchievementTier.TIER_1, AchievementType.FRIENDS, null, "Add your first friend on DemonRsps", 1, 1), BECOME_INVOLVED(10, AchievementTier.TIER_1, AchievementType.FORUM, null, "Access our website via ::website", 1, 1), WHAT_ARE_THE_CHANCES(11, AchievementTier.TIER_1, AchievementType.PVP, null, "Get your first PvP drop", 1, 1), NEW_FOND_SKILL(12, AchievementTier.TIER_1, AchievementType.HUNTER, null, "Lay down 100 traps", 100, 1), STORE_POLICY(13, AchievementTier.TIER_1, AchievementType.DONATE, null, "Check out our donation page", 1, 1), CRAB_SLAYER(14, AchievementTier.TIER_1, AchievementType.KILL_ROCK_CRAB, null, "Kill 100 level 13 rock crabs", 100, 1), DEMON_SLAYER(15, AchievementTier.TIER_1, AchievementType.KILL_ABYSSAL_DEMON, null, "Kill 100 level 124 abyssal demons", 100, 1), DRAGON_MEAL(16, AchievementTier.TIER_1, AchievementType.KILL_BLACK_DRAGON, null, "Kill 75 level 227 Black dragons. ", 75, 1), CREEPY_NECHS(17, AchievementTier.TIER_1, AchievementType.KILL_NECHRYAEL, null, "Kill 75 level 115 Nechryael's ", 75, 1), FISHERMAN(18, AchievementTier.TIER_1, AchievementType.SHARK_FISHER, null, "Fish 100 sharks", 100, 1), BREWER(19, AchievementTier.TIER_1, AchievementType.ZAMORAK_BREWER, null, "Create 100 Zamorak brews", 100, 1), TRACKER(20, AchievementTier.TIER_1, AchievementType.TRACKER, null, "Open up the tracker using ROW", 1, 1),
		BARROWS_CHEST_COUNT(21, AchievementTier.TIER_1, AchievementType.BARROWS_CHEST_COUNT, null, "Open 5 Barrows chests", 5, 1),
		ONE_EYED_FREAK(22, AchievementTier.TIER_1, AchievementType.DEFENDER, null, "Obtain 1 Defender from cyclops", 1, 1),
		EXPLORING_THE_MAP(23, AchievementTier.TIER_1, AchievementType.CLUE_SCROLL, null, "Complete 1 clue of any difficulty", 1, 1),
		THE_CLOUDED_VIEW(24, AchievementTier.TIER_1, AchievementType.MEDIUM_CLUE, null, "Complete 5 medium clue scrolls", 5, 1),
		THE_MISTY_WEATHER(25, AchievementTier.TIER_1, AchievementType.HARD_CLUE, null, "Complete 5 Hard clue scrolls", 5, 1),
		THE_ROCKY_ROAD(26, AchievementTier.TIER_1, AchievementType.ELITE_CLUE, null, "Complete 5 Elite clue scrolls", 5, 1),
		/**
		 * Tier 2
		 */
		MEDIUM_TIME_VOTER(0, AchievementTier.TIER_2, AchievementType.VOTE, null, "vote atleast 25 times and claim your rewards", 25, 1), CRAB_DESTROYER(1, AchievementTier.TIER_2, AchievementType.KILL_ROCK_CRAB, null, "Kill 500 level 13 rock crabs", 500, 3), DEMON_DESTROYER(2, AchievementTier.TIER_2, AchievementType.KILL_ABYSSAL_DEMON, null, "Kill 500 level 124 abyssal demons", 500, 3), DRAGON_DINNER(3, AchievementTier.TIER_2, AchievementType.KILL_BLACK_DRAGON, null, "Kill 325 level 227 Black dragons", 325, 3), A_NECHRAYEL(4, AchievementTier.TIER_2, AchievementType.KILL_NECHRYAEL, null, "Kill 325 Level 115 Nechryaels", 325, 3),
		HARDENING_THE_ARMOUR(5, AchievementTier.TIER_2, AchievementType.BARROWS_CHEST_COUNT, null, "Open 50 Barrow chests", 50, 2), RAN_HALFWAY(6, AchievementTier.TIER_2, AchievementType.CLUE_SCROLL, null, "Complete 25 clues of any difficulty", 25, 2),
		/**
		 * Tier 3
		 */

		CRAB_ASASSIN(0, AchievementTier.TIER_3, AchievementType.KILL_ROCK_CRAB, null, "Kill 1000 level 13 rock crabs", 1000, 5), DEMON_ASSASIN(1, AchievementTier.TIER_3, AchievementType.KILL_ABYSSAL_DEMON, null, "Kill 1000 level 124 abyssal demons", 1000, 5), DEATH_BOW(2, AchievementTier.TIER_3, AchievementType.KILL_DARK_BEAST, null, "Kill 350 level 182 Dark-beasts ", 350, 5),
		PVP_OH(3, AchievementTier.TIER_3, AchievementType.KILL_VENENATIS, null, "Kill 5 lvl 464 Veneatis", 5, 5), AN_ODD_FELLOW(4, AchievementTier.TIER_3, AchievementType.KILL_VETION, null, "Kill 5 lvl 454 Vetion", 5, 5), SCORPION_KING(5, AchievementTier.TIER_3, AchievementType.KILL_SCORPIA, null, "Kill 5 level 452 Scorpias", 5, 5), BEAR_GRYLL(6, AchievementTier.TIER_3, AchievementType.KILL_CALLISTO, null, "Kill 5 level 452 Callistos", 5, 5),
		MAXIMUM_STRENGTH(7, AchievementTier.TIER_3, AchievementType.BARROWS_CHEST_COUNT, null, "Open 250 Barrow chests", 250, 5),
		JUNGLE_RUNNER(8, AchievementTier.TIER_3, AchievementType.KILL_JUNGLE_DEMON, null, "Kill 50 Jungle Demons", 50, 5),
		RUNNER_UP(9, AchievementTier.TIER_3, AchievementType.DEFENDER, null, "Obtain 5 Defenders from Cyclops", 5, 5),
		THE_MARATHON(10, AchievementTier.TIER_3, AchievementType.CLUE_SCROLL, null, "Complete 50 clue scrolls of any difficulty", 50, 5);

		private AchievementTier tier;
		private AchievementRequirement requirement;
		private AchievementType type;
		private String description;
		private int amount, identification, points;

		Achievement(int identification, AchievementTier tier, AchievementType type, AchievementRequirement requirement, String description, int amount, int points) {
			this.identification = identification;
			this.tier = tier;
			this.type = type;
			this.requirement = requirement;
			this.description = description;
			this.amount = amount;
			this.points = points;
		}

		public int getId() {
			return identification;
		}

		public AchievementTier getTier() {
			return tier;
		}

		public AchievementType getType() {
			return type;
		}

		public AchievementRequirement getRequirement() {
			return requirement;
		}

		public String getDescription() {
			return description;
		}

		public int getAmount() {
			return amount;
		}

		public int getPoints() {
			return points;
		}

		public static final Set<Achievement> ACHIEVEMENTS = EnumSet.allOf(Achievement.class);

		public static Achievement getAchievement(AchievementTier tier, int ordinal) {
			for (Achievement achievement : ACHIEVEMENTS)
				if (achievement.getTier() == tier && achievement.ordinal() == ordinal)
					return achievement;
			return null;
		}

		public static boolean hasRequirement(Player player, AchievementTier tier, int ordinal) {
			for (Achievement achievement : ACHIEVEMENTS) {
				if (achievement.getTier() == tier && achievement.ordinal() == ordinal) {
					if (achievement.getRequirement() == null)
						return true;
					if (achievement.getRequirement().isAble(player))
						return true;
				}
			}
			return false;
		}
	}

	public static void increase(Player player, AchievementType type, int amount) {
		for (Achievement achievement : Achievement.ACHIEVEMENTS) {
			if (achievement.getType() == type) {
				if (achievement.getRequirement() == null || achievement.getRequirement().isAble(player)) {
					int currentAmount = player.getAchievements().getAmountRemaining(achievement.getTier().ordinal(), achievement.getId());
					int tier = achievement.getTier().ordinal();
					if (currentAmount < achievement.getAmount() && !player.getAchievements().isComplete(achievement.getTier().ordinal(), achievement.getId())) {
						player.getAchievements().setAmountRemaining(tier, achievement.getId(), currentAmount + amount);
						if ((currentAmount + amount) >= achievement.getAmount()) {
							String name = achievement.name().toLowerCase().replaceAll("_", " ");
							player.getAchievements().setComplete(tier, achievement.getId(), true);
							player.getAchievements().setPoints(achievement.getPoints() + player.getAchievements().getPoints());
							player.sendMessage("Achievement completed on tier " + (tier + 1) + ": '" + achievement.name().toLowerCase().replaceAll("_", " ") + "' and receive " + achievement.getPoints() + " point(s).");
							PlayerUpdating.announce("<col=ff0033>[ACHIEVEMENT] </col>" + Misc.formatPlayerName(player.playerName) + " completed the achievement " + name + " on tier " + (tier + 1) + ".");
						}
					}
				}
			}
		}
	}

	public static void reset(Player player, AchievementType type) {
		for (Achievement achievement : Achievement.ACHIEVEMENTS) {
			if (achievement.getType() == type) {
				if (achievement.getRequirement() == null || achievement.getRequirement().isAble(player)) {
					if (!player.getAchievements().isComplete(achievement.getTier().ordinal(), achievement.getId())) {
						player.getAchievements().setAmountRemaining(achievement.getTier().ordinal(), achievement.getId(), 0);
					}
				}
			}
		}
	}

	public static void complete(Player player, AchievementType type) {
		for (Achievement achievement : Achievement.ACHIEVEMENTS) {
			if (achievement.getType() == type) {
				if (achievement.getRequirement() != null && achievement.getRequirement().isAble(player) && !player.getAchievements().isComplete(achievement.getTier().ordinal(), achievement.getId())) {
					int tier = achievement.getTier().ordinal();
					// String name = achievement.name().replaceAll("_", " ");
					player.getAchievements().setAmountRemaining(tier, achievement.getId(), achievement.getAmount());
					player.getAchievements().setComplete(tier, achievement.getId(), true);
					player.getAchievements().setPoints(achievement.getPoints() + player.getAchievements().getPoints());
					player.sendMessage("Achievement completed on tier " + (tier + 1) + ": '" + achievement.name().toLowerCase().replaceAll("_", " ") + "' and receive " + achievement.getPoints() + " point(s).");

				}
			}
		}
	}

	public static void checkIfFinished(Player player) {
		if (player.getAchievements().hasCompletedAll()) {
			player.canWearCape = true;
		}
	}

	public static int getMaximumAchievements() {
		return Achievement.ACHIEVEMENTS.size();
	}
}
