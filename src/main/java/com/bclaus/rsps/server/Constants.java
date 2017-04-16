package com.bclaus.rsps.server;

import com.bclaus.rsps.server.vd.player.Player;

public class Constants {

	
	/**
	 * Are the MYSQL services enabled?
	 */
	public static boolean MYSQL_ENABLED = false; 
	public static boolean HIGHSCORES_ENABLED = true;
	
	/**
	 * The current version of the client. Used to notify player to update
	 * client.
	 */
	public static final int CLIENT_VERSION = 37;
	
	public static final String SERVER_NAME = "DemonRsps";
	
	public static boolean legendPlayer(Player c) {
		return c.gameMode == 2;
	}
	public static boolean regularPlayer(Player c) {
		return c.gameMode == 0;
	}
	
	
	/**
	 * The max amount of players until your server is full.
	 */
	public static final int MAX_PLAYERS = 2000;
	
	public static String getDonatorRank(Player p) {
		switch (p.donatorRights) {
		case 1: return "regular";
		case 2: return "extreme";
		}
		
		return "none";
	}
	public static boolean isStaffMember(Player c) {
		return c.playerRights >= 1 && c.playerRights <= 5;
	}
	public static boolean isOwner(Player c) {
		return c.playerRights == 5;
	}
	public static boolean isIronMan(Player c) {
		return c.playerRights == 9;
	}

	public static boolean premiumRights(Player c) {
		return c.donatorRights == 2;
	}

	public static boolean donatorRights(Player c) {
		return c.donatorRights == 1 ;
	}
	public static boolean vipRights(Player c) {
		return c.donatorRights == 4;
	}
	public static boolean respectedRights(Player c) {
		return c.donatorRights == 5;
	}

	private static String[] titles = { "Not Prestiged", "[I]", "[II]", "[III]", "[IV]", "[V]", "(VI)", "[VII]", "[VIII]", "[IX]", "[X]", "[XI]", "[XII]", "[XIII]", "[XIV]", "[XV]", "[XVI]", "[XVII]", "[XVIII]", "[XIX]", "[XX]", "[XXI]", "[XXII]", "[XXIII]", "[XXIV]", "[XXV]", "[XXVI]", "[XXVII]", "[XXVIII]", "[XXIX]", "[XXX]", "The Warrior", "The Fallen", "The Divine", "Adequate", "The Charm", "Restless", "6ft Under", "Lucky", "Hard Eight", "Nine lives", "Adventurer",

	};

	public static String getRank(Player c, int prestige) {
		return titles[prestige];
	}

	private static String[] RIGHTS_TO_STRING = { "<col=666666>Player</col>", "<col=255>Helper</col>", "<col=F7F2E0>Moderator</col>", "<col=F7FE2E><shad=222222>Developer</shad></col>", "<col=CC9933><shad=222222>Manager</col>", "<col=00FF00><shad=000000>Server Owner</shad></col>", "<col=0B610B>Donator</col>", "<col=DF013A>Promoter</col>", "", "<col=FF6600>Iron Man<shad=669999>", };

	private static String EXTREME_DONATOR() {
		return "<col=FFC200><shad=222222>Extreme Donator</col>";
	}
	private static String SPONSOR_DONATOR() {
		return "<col=FFC200>Sponsor Donator</col>";
	}
	
	private static String[] MODE_TO_STRING = {"Regular"," Legend ", "Iron Man"};
	
	public static String gameMode(Player c, int mode) {
		switch (mode) {
		case 0:
		case 1: 
			return MODE_TO_STRING[0];
		case 2:
			return MODE_TO_STRING[1];
		case 3:
			return MODE_TO_STRING[2];
//presume iron is 3? but thats what we put.
		}
		return Integer.toString(c.gameMode);
	}

	public static String rank(Player c, int rights) {
		if (c.isExtremeDonator && !Constants.isStaffMember(c)) {
			return EXTREME_DONATOR();
		}
		if(c.isSponsor &&  !Constants.isStaffMember(c)) {
			return SPONSOR_DONATOR();
		}
		switch (rights) {
		case 0:
			return RIGHTS_TO_STRING[0];
		case 1:
			return RIGHTS_TO_STRING[1];
		case 2:
			return RIGHTS_TO_STRING[2];
		case 3:
			return RIGHTS_TO_STRING[3];
		case 4:
			return RIGHTS_TO_STRING[4];
		case 5:
			return RIGHTS_TO_STRING[5];
		case 6:
			return RIGHTS_TO_STRING[6];
		case 7:
			return RIGHTS_TO_STRING[6];
		case 8:
			return RIGHTS_TO_STRING[7];
		case 9:
			return RIGHTS_TO_STRING[9];
		case 10:
		case 11:
		case 12:
			return RIGHTS_TO_STRING[7];
		}
		return Integer.toString(c.playerRights);
	}
	public static final boolean DISABLE_LIST_RESIZING = true;
	public static final int ITEM_LIMIT = 16000;
	public static final int CYCLE_TIME = 600;
	public static final int MAXITEM_AMOUNT = Integer.MAX_VALUE;
	public static final int BANK_SIZE = 350;
	public static boolean stakingIsEnabled = true;
	public static final int CONNECTION_DELAY = 100;
	public static final int IPS_ALLOWED = 1024;
	public static final int[] ITEM_SELLABLE = { 15053, 2677, 2678, 2679, 2680,8850, 15496, 7462, 15812, 9330, 9705, 9706, 1464, 4053, 4046, 4050, 4054, 9813, 9814, 11039, 7585, 7583, 7582, 6555, 1555, 1556, 1557, 1558, 1559, 1560, 4049, 4042, 4041, 4037, 4039, 4045, 4067, 4055, 6864, 6874, 6038, 6873, 6872, 7927, 7928, 7929, 7930, 7931, 7932, 7933, 9948, 9949, 9950, 991, 989, 275, 11279, 3842, 3844, 3840, 6529, 8844, 8845, 8846, 8851, 8847, 8848, 8849, 8850, 10548, 10547, 10549, 10550, 10552, 10553, 10554, 7458, 6570, 2528, 12469, 10499, 7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, 7453, 8839, 8840, 8842, 11663, 11664, 11665, 9748, 9754, 9751, 6872, 9769, 9757, 9760, 9763, 12010, 12011, 9802, 9808, 9784, 9799, 9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768, 10499, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 995, 10551, 10548, 3281, 13913, 13919, 13910, 13916, 13900, 13903, 13906, 13924, 6, 8, 10, 12, 15563, 15560, 15559, 15561, 15568, 15562, 15558, 15555, 15556, 15557, 15564, 15566 }; // what
	public static final int[] ITEM_TRADEABLE = { 1409, 15053, 2677, 2678, 2679, 2680, 6, 8, 10, 12, 8850, 15496, 7462, 9330, 9705, 9706, 1464, 4054, 4046, 4053, 9813, 9814, 11039, 7585, 7583, 7582, 6555, 1555, 1556, 1557, 1558, 1559, 1560, 4049, 4042, 4041, 4037, 4039, 4045, 4067, 4055, 4513, 4514, 4515, 4516, 4508, 4509, 4510, 4511, 4512, 4503, 4504, 4505, 4506, 4507, 4068, 4069, 4070, 4071, 4071, 6864, 6874, 6038, 6873, 7927, 7928, 7929, 7930, 7931, 7932, 7933, 9948, 9949, 9950, 991, 989, 275, 11279, 3842, 3844, 3840, 8844, 6529, 12010, 8851, 12011, 8845, 8846, 8847, 8848, 8849, 7458, 6570, 2528, 12469, 10499, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, 7453, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799, 9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 10548, 10551, 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 3281, 13913, 13919, 13910, 13916, 13900, 13903, 13906, 13924, 13868, 15812,15563, 15560, 15559, 15561, 15568, 15562, 15558, 15555, 15556, 15557, 15564, 15566, 15567, 15378, 15377, 15376, 15375, 15374, 15373, 13262 , 8839, 8840, 8842, 8841, 10611, 10663, 11663, 11664, 11665, 11674, 11675, 11676, 13263};
	public static final int[] UNDROPPABLE_ITEMS = { 1409, 15053, 2677, 2678, 2679, 2680, 7462, 6570, 2528, 12469, 10499, 8850, 15496, 10551, 10548, 7462, 6, 8, 10, 12, 15812, 15563, 15560, 15559, 15561, 15568, 15562, 15558, 15555, 15556, 15557, 15564, 15566, 15567 }; 
	public static final int[] FUN_WEAPONS = { 4151, 5698, 14611, 1231 };

/*
 * Packet Abusing Fix
 */
	 public static boolean sendServerPackets = false;
	
	public static final boolean ADMIN_DROP_ITEMS = false; 
	public static final int START_LOCATION_X = 3093; // start here 3222
	public static final int START_LOCATION_Y = 3104; // 3218
	public static final int RESPAWN_X = 3087; // when dead respawn here
	public static final int RESPAWN_Y = 3502;
	public static final int DUELING_RESPAWN_X = 3362; // when dead in duel area
	public static final int DUELING_RESPAWN_Y = 3263;
	public static final int RANDOM_DUELING_RESPAWN = 5; // random coords
	public static final int NO_TELEPORT_WILD_LEVEL = 21; // level you can't tele
	public static final int SKULL_TIMER = 1200; // how long does the skull last?
	public static final boolean SINGLE_AND_MULTI_ZONES = true; // multi and
	public static final boolean COMBAT_LEVEL_DIFFERENCE = true; // wildy levels
	public static final boolean itemRequirements = true; // attack, def, str,
	public static final int MELEE_EXP_RATE = 80;
	public static final int RANGE_EXP_RATE = 90;
	public static final int MAGIC_EXP_RATE = 90;
	public static final double SERVER_EXP_BONUS = 2;
	public static final double DROP_BONUS = 0.25; //25%
	public static final int INCREASE_SPECIAL_AMOUNT = 18500; // how fast your
	public static final int GOD_SPELL_CHARGE = 300000; // how long does god
	public static final int SAVE_TIMER = 30; // save every 30 seconds
	public static final int NPC_RANDOM_WALK_DISTANCE = 10; // the square created
	public static final int NPC_FOLLOW_DISTANCE = 10; // how far can the npc
	public static final int[] UNDEAD_NPCS = { 90, 91, 92, 93, 94, 103, 104, 73, 74, 75, 76, 77 }; // undead
	public static final int BUFFER_SIZE = 10000;
	public static final int MAX_PACKETS_PROCESS = 10;
	public static final int WOODCUTTING_EXPERIENCE = 20;// Done
	public static final int MINING_EXPERIENCE = 22;// Done
	public static final int SMITHING_EXPERIENCE = 55;// Done
	public static final int FARMING_EXPERIENCE = 15;// Done
	public static final int FIREMAKING_EXPERIENCE = 17;// Done
	public static final int HERBLORE_EXPERIENCE = 12;// Done
	public static final int FISHING_EXPERIENCE = 28;// Done
	public static final int CONSTRUCTION_EXPERIENCE = 10; //need to make
	public static final int AGILITY_EXPERIENCE = 80;// Done
	public static final int PRAYER_EXPERIENCE = 18;// Done
	public static final int RUNECRAFTING_EXPERIENCE = 15;// Done
	public static final int CRAFTING_EXPERIENCE = 13;// Done
	public static final int THIEVING_EXPERIENCE = 25;// Done
	public static final int SLAYER_EXPERIENCE = 23;// Done
	public static final int COOKING_EXPERIENCE = 14;// Done
	public static final int FLETCHING_EXPERIENCE = 50;// Done
	public static final int HUNTER_EXPERIENCE = 35;
	public static final int GENERAL_EXPERIENCE = 25;
	public static final boolean PRAYER_LEVEL_REQUIRED = true;
	public static boolean PRAYER_POINTS_REQUIRED = true;
	public static final boolean CRYSTAL_BOW_DEGRADES = true;
	public static final boolean CORRECT_ARROWS = true;
	public static final boolean MAGIC_LEVEL_REQUIRED = true;
	public static final boolean RUNES_REQUIRED = true;
	public static final boolean Party_Room_Disabled = true;
	public static final int[] WEBS_CANNOT = { 9185, 839, 845, 847, 851, 855, 859, 841, 843, 849, 853, 857, 861, 4212, 4214, 4215, 11235, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 6724, 4734, 4934, 4935, 4936, 4937, 1379, 1381, 1383, 1385, 1387, 1389 };
	public static final int PLANTING_POT = 2272, RAKING = 2273, SPADE = 830, COMPOST = 2283, KNEEL = 1331, STAND_UP = 1332, PRUNING = 2275, CHECK_HEALTH = 832, SEED_DIBBING = 2291, CURING = 2288, PICKING_HERB = 2282;
	public static final char VALID_CHARS[] = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',', '"', '[', ']', '|', '?', '/', '`' };
	public static final int[] DEGRADABLE_ITEMS = { 13913, 13919, 13910, 13916, 13900, 13903, 13868, 13924, 12346, 15053, 14614 };
}
