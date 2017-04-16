package com.bclaus.rsps.server.vd.player;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.util.DisplayName;
import com.bclaus.rsps.server.vd.content.DoubleExperience;
import com.bclaus.rsps.server.vd.content.QuickPrayer;
import com.bclaus.rsps.server.vd.content.ShopAssistant;
import com.bclaus.rsps.server.vd.content.TeleportHandler;
import com.bclaus.rsps.server.vd.content.bountyhunter.BountyHunter;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueDifficulty;
import com.bclaus.rsps.server.vd.content.combat.CombatAssistant;
import com.bclaus.rsps.server.vd.content.combat.CombatDamage;
import com.bclaus.rsps.server.vd.content.dialogue.DialogueManager;
import com.bclaus.rsps.server.vd.content.instanced.InstancedAreaManager;
import com.bclaus.rsps.server.vd.content.punishment.PunishmentManager;
import com.bclaus.rsps.server.vd.content.skills.impl.slayer.SocialSlayer;
import com.bclaus.rsps.server.vd.items.bank.Bank;
import com.bclaus.rsps.server.vd.npc.pets.PlayerPet;
import com.bclaus.rsps.server.vd.objects.cannon.CannonCredentials;
import com.bclaus.rsps.server.vd.player.packets.spawns.Melee;
import com.bclaus.rsps.server.vd.player.packets.spawns.Range;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.database.Highscores;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.clan.Clan;
import com.bclaus.rsps.server.vd.content.PVPAssistant;
import com.bclaus.rsps.server.vd.content.PlayerKillstreak;
import com.bclaus.rsps.server.vd.content.StarterHandler.ModeData;
import com.bclaus.rsps.server.vd.content.VoteRewardSelection;
import com.bclaus.rsps.server.vd.content.achievements.AchievementHandler;
import com.bclaus.rsps.server.vd.content.achievements.AchievementType;
import com.bclaus.rsps.server.vd.content.achievements.Achievements;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueScrollContainer;
import com.bclaus.rsps.server.vd.content.instanced.impl.Zulrah;
import com.bclaus.rsps.server.vd.content.minigames.PestControl;
import com.bclaus.rsps.server.vd.content.skills.impl.farming.Farming;
import com.bclaus.rsps.server.vd.content.skills.impl.slayer.SocialSlayerData;
import com.bclaus.rsps.server.vd.content.support.Ticket;
import com.bclaus.rsps.server.vd.content.teleport.CityTeleport;
import com.bclaus.rsps.server.vd.event.CycleEventHandler;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.items.ItemAssistant;
import com.bclaus.rsps.server.vd.items.UseItem;
import com.bclaus.rsps.server.vd.items.bank.BankPin;
import com.bclaus.rsps.server.vd.mobile.EntityType;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.vd.mobile.MobileCharacter;
import com.bclaus.rsps.server.vd.mobile.MobileCharacterType;
import com.bclaus.rsps.server.vd.mobile.PoisonCombatTask;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.NPCAggression;
import com.bclaus.rsps.server.vd.npc.NPCDeathTracker;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.npc.NPCSlayerDeathTracker;
import com.bclaus.rsps.server.vd.objects.cannon.CannonManager;
import com.bclaus.rsps.server.vd.player.account_type.Account;
import com.bclaus.rsps.server.vd.player.net.NpcUpdating;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import com.bclaus.rsps.server.vd.player.packets.PacketHandler;
import com.bclaus.rsps.server.vd.player.packets.spawns.Hybrid;
import com.bclaus.rsps.server.vd.player.packets.spawns.Magic;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.login.Packet;
import com.bclaus.rsps.server.login.Packet.Type;
import com.bclaus.rsps.server.quarantine.PlayerQuarantine;
import com.bclaus.rsps.server.rsa.ByteBuffer;
import com.bclaus.rsps.server.rsa.ISAACRandomGen;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.util.Stopwatch;
import com.bclaus.rsps.server.util.SystemTimer;

public class Player extends MobileCharacter {

	private final QuickPrayer quick = new QuickPrayer();

	private HashMap<String, ArrayList<CombatDamage>> damageReceived = new HashMap<>();
	public boolean EP_ACTIVE;
	/**
	 * Sends the dialogue to our player
	 */
	private final DialogueManager dialogue = new DialogueManager(this);
	public int pkp, KC, DC, ks;
	public String lastPlayerKilled = "";
	public String lastIPKilled = "";

	private ModeData mode;

	int playerMode = 0;
	public int[] damageTaken = new int[Constants.MAX_PLAYERS];

	public void setModeInt() {
		switch (mode) {
		case IRON_MAN:
			playerMode = 3;
			break;
		case LEGENDARY:
			playerMode = 2;
			break;
		case NORMAL:
			playerMode = 1;
			break;
		default:
			playerMode = 1;
			break;

		}
	}

	public final ArrayList<String> onlineAdmins = new ArrayList<String>();
	public final ArrayList<String> onlineMods = new ArrayList<String>();
	public final ArrayList<String> onlinetmod = new ArrayList<String>();
	public final ArrayList<String> onlineHelper = new ArrayList<String>();
	public final ArrayList<String> onlineOwners = new ArrayList<String>();
	public final ArrayList<String> onlinestaffmanger = new ArrayList<String>();

	public void getOnlineStaff() {
		onlineAdmins.clear();
		onlineMods.clear();
		onlinetmod.clear();
		onlineHelper.clear();
		onlinestaffmanger.clear();
		onlineOwners.clear();
		for (Player p : World.PLAYERS) {
			if (p != null) {
				Player staff = (Player) p;
				if (staff.playerRights == 2 && staff.privateChat == 0) {
					onlineMods.add(staff.playerName);
				}
				if (staff.playerRights == 3 && staff.privateChat == 0) {
					onlineAdmins.add(staff.playerName);
				}
				if (staff.playerRights == 5 && staff.privateChat == 0) {
					onlineOwners.add(staff.playerName);
				}

				if (staff.playerRights == 1 && staff.privateChat == 0) {
					onlineHelper.add(staff.playerName);
				}
				if (staff.playerRights == 4 && staff.privateChat == 0) {
					onlinestaffmanger.add(staff.playerName);
				}

			}
		}
	}

	public void rspsdata(Player c, String username) {
		try {
			username = username.replaceAll(" ", "_");
			String secret = "ac9edbbe0533cef12e50fd6fb6cfde52"; // YOUR SECRET
																// KEY!
			String email = "demonrsps@gmail.com"; // This is the one you use to
													// login into RSPS-PAY
			URL url = new URL("http://rsps-pay.com/includes/listener.php?username=" + username + "&secret=" + secret
					+ "&email=" + email);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String results = reader.readLine();
			if (results.toLowerCase().contains("!error:")) {

			} else {
				String[] ary = results.split(",");
				for (int i = 0; i < ary.length; i++) {
					switch (ary[i]) {
					case "0":
						// donation was not found tell the user that!
						break;
					case "19281":
						c.donationPoints += 250;
						c.sendMessage("@red@You have received 250 Donate Points, Thank you for Donator" + c.playerName);
						break;
					case "19282":
						c.donationPoints += 500;
						c.sendMessage("@red@You have received 500 Donate Points, Thank you for Donator" + c.playerName);
						break;
					case "19283":
						c.donationPoints += 1000;
						c.sendMessage(
								"@red@You have received 1000 Donate Points, Thank you for Donator" + c.playerName);
						break;
					}
				}
			}
		} catch (IOException e) {
		}
	}

	public void rspsdata1(Player c, String username) {
		try {
			username = username.replaceAll(" ", "_");
			String secret = "acff1af62d0f91f4be73f4857552d70c"; // YOUR SECRET
																// KEY!
			String email = "skyrsps@gmail.com"; // This is the one you use to
												// login into RSPS-PAY
			URL url = new URL("http://rsps-pay.com/includes/listener.php?username=" + username + "&secret=" + secret
					+ "&email=" + email);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String results = reader.readLine();
			if (results.toLowerCase().contains("!error:")) {

			} else {
				String[] ary = results.split(",");
				for (int i = 0; i < ary.length; i++) {
					switch (ary[i]) {
					case "12351":
						c.donationPoints += 100;
						c.sendMessage("@red@You have received 100 Donate Points, Thank you for Donator" + c.playerName);
						break;
					case "12353":
						c.donationPoints += 500;
						c.sendMessage("@red@You have received 500 Donate Points, Thank you for Donator" + c.playerName);
						break;
					case "12354":
						c.donationPoints += 1000;
						c.sendMessage(
								"@red@You have received 1000 Donate Points, Thank you for Donator" + c.playerName);
						break;
					case "12920":
						c.playerRights = 10;
						c.donatorRights = 2;
						c.amountDonated = 30;
						c.sendMessage("Thanks for donate " + c.playerName + " the Item in your bank.");

						break;
					case "12921":
						c.getItems().addItem(6830, 1);
						c.sendMessage("Thanks for donate " + c.playerName + ".");
						break;
					case "12922":
						c.getItems().addItem(6831, 1);
						c.sendMessage("Thanks for donate " + c.playerName + ".");
						break;

					}
				}
			}
		} catch (IOException e) {
		}
	}

	public void setModeEnum() {
		if (playerMode == 0 || playerMode == -1) {
			setMode(ModeData.NORMAL);
		} else if (playerMode == 2) {
			setMode(ModeData.LEGENDARY);
		} else if (playerMode == 3) {
			setMode(ModeData.IRON_MAN);
		}

	}

	/**
	 * Temp solution to clue-scroll bug - Allows player to reset his clue status
	 * - Allowing them to continue to gather clues
	 * 
	 * @param player
	 */
	public void resetClueStatus(Player player) {
		if (player.getItems().playerOwnsAnyItems(ClueDifficulty.getClueIds())) {
			player.sendMessage("It seems you have a clue scroll, please complete it.");
			return;
		}
		if (player.bossDifficulty != null) {
			player.bossDifficulty = null;
		}
		if (player.clueContainer != null) {
			player.clueContainer = null;
		}
	}

	public int modeSelected = 0;

	public void openModeInterface() {
		String[] modeDescriptions = {
				"-700 xp rate\\n-No increase drop rates.\\n\\nThis game mode is for those who want to get straight at \\nit, with fast level training you can start \\nSlaying/Bossing/Killing/Skilling fast.",
				"-50 xp rate\\n-15% Drop Rate Increase\\n\\nThis is for those who want a bit more of a challenge\\nwith lower xp rates than regular mode,\\nthis creates a slower training experience but you get the\\nbenfit of a drop rate increase!",
				"-50 xp rate\\n-15% Drop Rate Increase\\n\\nThis is for those players who want a real challenge,\\nand want to earn it all themselves with no trading\\nand no buying from stores. This is the ultimate challenge\\nto Complete on DemonRsps!", };
		String[] modeTitles = { "Regular Mode Description", "Legendary Mode Description",
				"Iron Man Mode Description", };
		getPA().sendFrame126(modeTitles[modeSelected], 31510);
		getPA().sendFrame126(modeDescriptions[modeSelected], 31511);

	}

	public int getKillStreak() {
		return killStreak;
	}

	public void setKillStreak(int killStreak) {
		this.killStreak = killStreak;
	}

	public Stopwatch sql = new Stopwatch();
	private int sessionExperience;

	public int getSessionExperience() {
		return sessionExperience;
	}

	public int getInstancedHeight() {
		return getIndex() * 4;
	}

	/**
	 * Determines if the player is a super administrator
	 * 
	 * @return
	 */
	public boolean isSuperAdministrator() {
		switch (playerName.toLowerCase()) {
		case "demon":

			return true;
		}

		return false;
	}

	/*
	 * public Item[] getInventory() { List<Item> list = new LinkedList<>(); for
	 * (int i = 0; i < playerItems.length; i++) { list.add(new
	 * Item(playerItems[i], playerItemsN[i])); } return (Item[]) list.toArray();
	 * }
	 */

	/*
	 * public void updateshop(final int i) { final Player p =
	 * World.PLAYERS.get(this.getIndex()); p.getShops().resetShop(this, i); }
	 */
	private ShopAssistant shopAssistant = new ShopAssistant(this);

	public void updateshop(final int i) {
		final Player p = World.PLAYERS.get(this.getIndex());
		p.getShops().resetShop(i);
	}

	public ShopAssistant getShops() {
		return shopAssistant;
	}

	public int[] resourceItemId = new int[28];
	public int[] resourceItemAmount = new int[28];
	public boolean canDeposit = false;
	public int antiqueSelect = 0;
	public int chance;
	public boolean attackable;
	public boolean inTask = false;
	public String targetName;
	public long specTimer;
	public int playerId = -1;
	public int EP, EP_MINUTES, targetPercentage, targetIndex, safeTimer = 1000, logoutTimer, dropWealth;

	/**
	 * Timers
	 */
	private final SystemTimer sqlTimer = new SystemTimer();

	/**
	 * End System Timers
	 */

	public int addSessionExperience(int experience) {
		return this.sessionExperience += experience;
	}

	/**
	 * Agility variables
	 */
	/**
	 * Agility
	 */

	public boolean doingAgility = false;

	/**
	 * Obstacle Variables
	 */

	public boolean finishedLog = false;
	public boolean finishedNet1 = false;
	public boolean finishedBranch1 = false;
	public boolean finishedRope = false;
	public boolean finishedBranch2 = false;
	public boolean finishedNet2 = false;
	public boolean finishedPipe = false;

	public boolean finishedBarbRope = false;
	public boolean finishedBarbLog = false;
	public boolean finishedBarbNet = false;
	public boolean finishedBarbLedge = false;
	public boolean finishedBarbStairs = false;
	public boolean finishedBarbWall1 = false;
	public boolean finishedBarbWall2 = false;
	public boolean finishedBarbWall3 = false;

	public boolean finishedWildPipe = false;
	public boolean finishedWildRope = false;
	public boolean finishedWildStone = false;
	public boolean finishedWildLog = false;
	public boolean finishedWildRocks = false;

	public long pickupDelay;
	public boolean usedItemOnBag;
	public int zulrahEntries = 0;
	public int barrowsChestCount = 0;
	private Stopwatch lastCombatAction = new Stopwatch();
	/**
	 * Represents an account and is associated to just this player
	 */
	private Account account;

	/**
	 * Returns the account associated to this player
	 * 
	 * @return the account
	 */
	public Account getAccount() {
		if (account == null)
			account = new Account(this);
		return account;
	}

	private InterfaceManager interfaceManager = new InterfaceManager(this);
	public static Player players[] = new Player[Constants.MAX_PLAYERS];

	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}

	public PlayerAssistant getPlayerAssistant() {
		return playerAssistant;
	}

	public boolean canDeleteTask = false;

	/**
	 * The PlayerPet object associated with the player
	 */
	private PlayerPet pet;

	/**
	 * Returns the instance of the PlayerPet object associated with the player
	 * 
	 * @return the instance of the pet
	 */
	public boolean slayerBossTask;

	public PlayerPet getPet() {
		if (pet == null)
			pet = new PlayerPet(this);
		return this.pet;
	}

	private BankPin pin;

	public BankPin getBankPin() {
		if (pin == null)
			pin = new BankPin(this);
		return pin;
	}

	public int tick = 0;

	// dart load
	public int dartsLoaded;
	public int runedartsLoaded;

	// clue scroll
	public ClueScrollContainer clueContainer;
	public ClueDifficulty bossDifficulty;
	public int randomClueReward = 0;
	public int easyClue = 0;
	public int mediumClue = 0;
	public int hardClue = 0;
	public int eliteClue = 0;
	/**
	 * Sponsor Addition - donator
	 */
	public boolean isSponsor;
	private Bank bank;

	public Bank getBank() {
		if (bank == null)
			bank = new Bank(this);
		return bank;
	}

	public String reason;
	public boolean filedTicket = false, ticketAnswered;
	public String ticketFiler;

	public boolean inTicket() {
		return this.absX > Ticket.SOUTHWEST_X && this.absX < Ticket.NORTHEAST_X && this.absY > Ticket.SOUTHWEST_Y
				&& this.absY < Ticket.NORTHEAST_Y;
	}

	public Stopwatch aggressionTolerance = new Stopwatch();
	public long DoublePKP = 0;
	private GameOptions gameOptions;
	private GameOptionsInterface gameOptionsInterface;

	private NPCDeathTracker bossTracker;

	public NPCDeathTracker getBossDeathTracker() {
		if (bossTracker == null)
			bossTracker = new NPCDeathTracker(this);
		return bossTracker;
	}

	private NPCSlayerDeathTracker slayerTracker;
	private Zulrah zulrah;

	public boolean enteredGuild;

	public Zulrah getZulrah() {
		if (zulrah == null)
			zulrah = new Zulrah();
		return zulrah;
	}

	public NPCSlayerDeathTracker getSlayerDeathTracker() {
		if (slayerTracker == null)
			slayerTracker = new NPCSlayerDeathTracker(this);
		return slayerTracker;
	}

	public GameOptions getGameOptions() {
		if (gameOptions == null)
			gameOptions = new GameOptions(this);
		return gameOptions;
	}

	public GameOptionsInterface getGameOptionsInterface() {
		if (gameOptionsInterface == null)
			gameOptionsInterface = new GameOptionsInterface(this);
		return gameOptionsInterface;
	}

	/**
	 * Determines the players display name wether it be the temp or permanent
	 * name
	 * 
	 * @return The players name
	 */
	public String getName() {
		return this.displayName == null ? Misc.formatPlayerName(this.playerName)
				: this.displayName != null && gameOptions.isDisplayNameActive() ? this.displayName
						: Misc.formatPlayerName(this.playerName);
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String name) {
		this.displayName = name;
	}

	private String macAddress = "";

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getMacAddress() {
		return this.macAddress;
	}

	public static boolean tradeEnabled = true;

	public void DoublePKPTimer() {
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void execute() {
				if (DoublePKP > 0) {
					DoublePKP--;
					if (DoublePKP == 0) {
						DoublePKP = -1;
						this.stop();
					}
				}
			}

			@Override
			public void onStop() {
				sendMessage("You no longer receive double the PK points");

			}
		}.attach(this));
	}

	public boolean isInJail() {
		if (absX >= 2065 && absX <= 2111 && absY >= 4415 && absY <= 4455) {
			return true;
		}
		return false;
	}

	public void DropRateTimer() {
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void execute() {
				if (dropRateIncreaser > 0) {
					dropRateIncreaser--;
					if (dropRateIncreaser == 0) {
						dropRateIncreaser = -1;
						this.stop();
					}
				}
			}

			@Override
			public void onStop() {
				sendMessage("You no longer receive the increased drop-rate");

			}
		}.attach(this));
	}

	public void DoubleExperienceTimer() {
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void execute() {
				if (voteExperienceMultiplier > 0) {
					voteExperienceMultiplier--;
					if (voteExperienceMultiplier == 0) {
						voteExperienceMultiplier = -1;
						this.stop();
					}
				}
			}

			@Override
			public void onStop() {
				sendMessage("You no longer receive the 2.0X experience boost you did before.");
				getPA().sendFrame126("<col=FFFFFF>Double Exp Activated : @red@ False", 39176);

			}
		}.attach(this));
	}

	/**
	 * The PlayerQuarantine object that stores valuable information about the
	 * state of this players quarantine if it exists.
	 */
	private PlayerQuarantine quarantine;

	public Position getPosition() {
		return new Position(absX, absY, heightLevel);
	}

	/**
	 * Retrieves the PlayerQuarantine object
	 * 
	 * @return The quarantine object
	 */
	public PlayerQuarantine getQuarantine() {
		if (quarantine == null)
			quarantine = new PlayerQuarantine(this);
		return quarantine;
	}

	public LinkedList<Long> friendsList = new LinkedList<Long>();
	public LinkedList<Long> ignoreList = new LinkedList<Long>();

	private AchievementHandler achievementHandler;

	public AchievementHandler getAchievements() {
		if (achievementHandler == null)
			achievementHandler = new AchievementHandler(this);
		return achievementHandler;
	}

	public long lastBankDeposit;

	public void antiFirePotion() {
		Server.getTaskScheduler().schedule(new ScheduledTask(600) {

			@Override
			public void execute() {
				antiFirePot = false;
				sendMessage("<col=ff0033>Your resistance to dragon fire has worn off.</col>");
				super.stop();
			}
		}.attach(this));
	}

	public ByteBuffer inStream = null, outStream = null;
	private Channel session;

	private final Queue<Packet> queuedPackets = new LinkedList<Packet>();
	public boolean antiFirePot = false;
	public int timeOutCounter = 0, previousDamage;

	public Clan clan = null;
	public String lastClanChat = "demon";
	private boolean isSocialSlaying;
	private int socialSlayerKills, socialSlayerPoints, socialSlayerDisconnections = 0;
	public int level1 = 0, level2 = 0, level3 = 0;
	private String lastSocialPlayer;
	private boolean canUseFoodOnPlayer, canSharePotion;
	public Player socialSlayerInviteClient;
	public boolean canTele = false;
	public int donatorRights;
	public long flaxTimer;
	public boolean doingBone;
	public int flaxCycles;
	public int npcTransformId = -1;
	public boolean[] apeAtollCourse = new boolean[7];
	public int fletchDelay = -1, fletchAmount = -1, arrowShaft = 52, fletchItem = -1, fletchIndex = -1,
			fletchingEventId = 5566;
	public String fletchThis = "";
	public int[] fletchSprites = { -1, -1, -1, -1, -1 };
	public boolean startedFletchCycle = false, isFletching = false, needsFletchDelay = false;
	public long lastFletch = 0;
	public boolean agilityEmote = false;
	public int barbObsticle = 0, gnomeObsticle = 0, objectDistance;
	public long voteExperienceMultiplier;
	private String identity;
	public PunishmentManager.PunishmentReport muteReport;
	public String loyaltyTitle = "";
	public String gameModeTitle = "";
	public boolean[] shownDefender = new boolean[8];
	public ArrayList<String> lastKilledList = new ArrayList<String>();
	public ArrayList<String> lastKilledPlayers = new ArrayList<String>();
	public int damage1, damage2;
	public int totalLevel, xpTotal;
	private double wildernessPotential;
	public int killStreak;
	public int highestKillStreak;
	public int potentialStage;
	private boolean pvpInterfaceVisible = true;
	public boolean canClaim;
	public boolean displayNameClaim;
	public long lastEearningPotential;

	/*
	 * Random Event spawns
	 */
	public boolean golemSpawned = false, treeSpawned = false;

	/*
	 * Assault
	 */

	public boolean isTeleporting;
	public boolean isFullBody = false;
	public boolean isFullHelm = false;
	public boolean isFullMask = false;
	public static boolean[] isSkilling = new boolean[25];
	public int pTime;
	public int barrowsKill = 0;
	public long barrowsLoot = 0;
	public boolean voteActive = false;
	public boolean modeTut = false;
	public int makeTimes;
	public int boneId;
	public int events;
	public boolean playerisSmelting;
	/*
	 * Prestige
	 */
	public int prestigeTitle = 0;
	public int prestigePoints = 0;
	public int currentTitle = 0;
	public int prestigeLevel = 0;

	public boolean lootshareEnabled = false, lootshare2Enabled = false;
	public int gameMode;
	public long lastTeleport;
	public long lastHerbPicked;

	private int queuedAnimation = -1;

	public int getQueuedAnimation() {
		return queuedAnimation;
	}

	public void setQueuedAnimation(int id) {
		queuedAnimation = id;
	}

	public int queuedWeaponStrength = -1;
	public String clanPass = null;
	public String currentTime, date;
	public ArrayList<Integer> attackedPlayers = new ArrayList<Integer>();
	public ArrayList<String> lastConnectedFrom = new ArrayList<String>();
	public ArrayList<Integer> addPlayerList = new ArrayList<Integer>();
	public int addPlayerSize = 0;
	public boolean inProcess = false;
	/*
	 * Prestige variables
	 */
	public int titleAchievements[] = { 0, 0, 0 };
	public int nonCombatSpecializations[] = { -1, -1, -1, -1, -1 };
	public boolean isDoubleLocked = true;
	public long doubleExpTimer;
	public boolean isSpecializationLocked = true;
	public boolean isTitleLocked = true;
	public boolean isAreaLocked = true;
	public boolean isNonCombatSpecializationLocked = true;
	public boolean clickedNonCombat = false;
	public boolean knowsPrestiging = false;

	public int getLocalX() {
		return getX() - 8 * getMapRegionX();
	}

	public int getLocalY() {
		return getY() - 8 * getMapRegionY();
	}

	public Object getDistanceEvent() {
		return distanceEvent;
	}

	/**
	 * End Assault
	 */

	public long miscTimer, buyFromVoteShopDelay;
	public String duelName;
	public int playerKillCount;
	public int playerDeathCount;
	public int WillKeepAmt1, WillKeepAmt2, WillKeepAmt3, WillKeepAmt4, WillKeepItem1, WillKeepItem2, WillKeepItem3,
			WillKeepItem4, WillKeepItem1Slot, WillKeepItem2Slot, WillKeepItem3Slot, WillKeepItem4Slot,
			originalKillCount, originalDeathCount, EquipStatus, amountDonated;

	public boolean autoRetaliate, usingGlory, isHarvesting, inItemOnDeath, isMuted, isClanMuted, cantClimbLadder,
			sendCookingMessage, teleTab, hasSpokenToWC, startTele2, lostDuel, removeBankMessage, requestPinDelete,
			haveRouge, playerIsFletching, initialized, disconnected, RebuildNPCList, isActive, isSkulled, cantTeleport,
			isMorphed, playerFletch, newPlayer, hasMultiSign, saveCharacter, splitChat, chatEffects = true, acceptAid,
			autocasting, mageFollow, dbowSpec, craftingLeather, properLogout, maxNextHit, ssSpec, vengOn, msbSpec,
			stopPlayerPacket, playerIsNPC, canChangeAppearance, stopPlayerSkill, playerIsMining, playerIsCooking,
			playerIsFishing, playerIsWoodcutting, trainingPrayer, playerIsFiremaking, playerStun, mageAllowed,
			usingPrayer, doubleHit, usingSpecial, usingRangeWeapon, usingBow, usingMagic, castingMagic, duelRequested,
			magicFailed, isMoving, walkingToItem, isShopping, updateShop, forcedChatUpdateRequired, tradeAccepted,
			goodTrade, inTrade, tradeRequested, tradeResetNeeded, tradeConfirmed, tradeConfirmed2, canOffer,
			acceptTrade, acceptedTrade, isRunning2 = true, saveFile, takeAsNote, smeltInterface, partyChest,
			rulesAccepted, onAuto = false, hasPaidBrim, firstGame, finishedBeg, friendUpdate = false, Mercenary = false,
			isBankingAll, isDonator = false, changedSymbol = false, spokenToGnome, inDuelScreen, didTeleport,
			mapRegionDidChange, createItems, canShear, eggCollected, spokenTo, usingClaws, rareDrop;
	public long friends[] = new long[200], ignores[] = new long[200];
	public int leatherType, hunterLevelReq, hideId, yells = 0, duelLevel, playerScore, arrowsLeft, diffLevel = 2,
			breadID, lastClickedButton, wcPoints, oreVariable, prayerLevelReq, fireMakingPoints, timesLit,
			pinDeleteDateRequested, thieveWait, DonorPoints, thievePoints, agilityPoints, cookQP, horrorQP,
			undergroundQP, dtQP, dragonQP, homeTele, homeTeleDelay, zkill, votePoints, doAmount, slayerTaskLevel,
			destroyItem, getPheasent, correctDrill, lastX, lastY, questPoints, dtMax, dtAtk, dtDef, desertT, saveDelay,
			playerKilled, pkPoints, skillPoints, totalPlayerDamageDealt, killedBy, lastChatId = 1, privateChat,
			friendSlot, dialogueId, gloryValue, newLocation, specEffect, specBarId, attackLevelReq, defenceLevelReq,
			strengthLevelReq, undergroundReq, horrorReq, cookReq, rangeLevelReq, magicLevelReq, skullTimer,

			slaypoints, removedTasks[] = { -1, -1, -1, -1 }, votingPoints, playerTradeWealth, nextChat, talkingNpc = -1,
			dialogueAction, lastLoginDate, autocastId, followDistance, npcFollowIndex, barrageCount, delayedDamage,
			delayedDamage2, pcPoints, assaultPoints, magePoints, lastArrowUsed = -1, clanId = 1, pcDamage, xInterfaceId,
			xRemoveId, xRemoveSlot, waveId, teleotherType, frozenBy, teleAction, bonusAttack, lastNpcAttacked,
			killCount, actionTimer, height, woodcuttingTree, clawDelay, sslot, familiar, teleGrabItem, teleGrabX,
			teleGrabY, duelCount, underAttackBy, underAttackBy2, wildLevel, teleTimer, saveTimer, teleBlockLength,
			poisonDelay, npcId2, memberStatus, playerNPCID = 555, flowerIndex = -1, playerBankPin, barrowsKillCount,
			slayerTask, taskAmount, prayerId = -1, headIcon = -1, bountyIcon, specMaxHitIncrease, freezeDelay,
			freezeTimer = -6, killerId, playerIndex, oldPlayerIndex, lastWeaponUsed, projectileStage,
			crystalBowArrowCount, playerMagicBook, teleGfx, teleEndAnimation, teleHeight, teleX, teleY, rangeItemUsed,
			killingNpcIndex, totalDamageDealt, oldNpcIndex, fightMode, attackTimer, npcIndex, npcClickIndex, npcType,
			castingSpellId, oldSpellId, spellId, hitDelay, bowSpecShot, clickNpcType, clickObjectType, objectId,
			itemUsedOn, objectX, objectY, pItemX, pItemY, pItemId, myShopId, tradeStatus, tradeWith, attackAnim,
			animationRequest = -1, animationWaitCycles, combatLevel, apset, actionID, wearItemTimer, wearId, wearSlot,
			interfaceId, XremoveSlot, XinterfaceID, XremoveID, Xamount, wcTimer, miningTimer, fishTimer, smeltType,
			smeltAmount, smeltTimer, tutorial = 15, duelTimer, duelTeleX, duelTeleY, duelSlot, duelSpaceReq, duelOption,
			duelingWith, duelStatus, headIconPk = -1, headIconHints, reduceSpellId, ecoStarter, dir1 = -1, dir2 = -1,
			poimiX, poimiY, tzhaarToKill = 0, tzhaarKilled = 0, tzKekSpawn = 0, tzKekTimer = 0;
	boolean lockedEXP;
	public boolean inJail = false;
	public boolean playerBFishing, finishedBarbarianTraining;
	public boolean needsNewTask = false;

	private int creaturePotionTimer = -1;

	public boolean WithinDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public long lastPlayerMove, lastRequest, lastPoison, lastPoisonSip, lastSpear, lastProtItem, dfsDelay, lastVeng,
			lastYell, lastHelp, teleGrabDelay, protMageDelay, protMeleeDelay, protRangeDelay, lastAction, lastThieve,
			lastLockPick, alchDelay, specDelay = System.currentTimeMillis(), duelDelay, teleBlockDelay, godSpellDelay,
			singleCombatDelay, singleCombatDelay2, reduceStat, restoreStatsDelay, logoutDelay, buryDelay, foodDelay,
			kwuamDelay, potDelay, specialDelay, agilityDelay, lastFire, lastPlant, reportDelay, lastDagChange = -1,
			stopPrayerDelay, stepDelay, prayerDelay, buySlayerTimer;
	public Stopwatch poisonImmune = new Stopwatch();
	public int runEnergy = 100;
	public long lastRunDrain, lastRunRecovery;
	public boolean ancientstele = false;
	public boolean teleporting = false;
	public int teleportDelay = -1;
	public int teleToX = 0;
	public int teleToY = 0;
	public int newHeight = 0;
	public int tentacleHits = 5000;
	public boolean oldSpec;
	public boolean rangeEndGFXHeight;
	public boolean isBanking = false;
	public int rangeEndGFX;
	public boolean ignoreDefence;
	public int[] bagItems = new int[27];
	public boolean usingOtherRangeWeapons;
	public boolean usingArrows;
	public boolean usingCross;
	public boolean multiAttacking;
	public double crossbowDamage;
	public int boltDamage;
	public boolean protectItem;
	public int dfsCount;
	public int recoilHits;
	public boolean magicDef;
	public boolean spellSwap;
	public int playerFollowIndex = 0;
	public String pinStatus = "No PIN Set";
	public String clanName, properName;
	public int[] voidStatus = new int[5];
	public int[] itemKeptId = new int[4];
	public int[] pouches = new int[6];
	public final int[] POUCH_SIZE = { 3, 6, 9, 12, 3, 5, 9 };
	public boolean[] invSlot = new boolean[28], equipSlot = new boolean[14];
	public double specAmount = 0;
	public double specAccuracy = 1;
	public double specDamage = 1;
	public double prayerPoint = 1.0;
	public boolean[] duelRule = new boolean[22];
	public final int[] DUEL_RULE_ID = { 1, 2, 16, 32, 64, 128, 256, 512, 1024, 4096, 8192, 16384, 32768, 65536, 131072,
			262144, 524288, 2097152, 8388608, 16777216, 67108864, 134217728 };
	public int playerAppearance[] = new int[13];
	public int[] playerBonus = new int[12];
	public int[] woodcut = new int[3];
	public int[] cookingProp = new int[7];
	public int[] fletchingProp = new int[10];
	public int[] cookingCoords = new int[2];
	public int[] mining = new int[3];
	public int[][] playerSkillProp = new int[20][15];
	public boolean[] playerSkilling = new boolean[20];
	public int[] farm = new int[2];

	public static int[] BOWS = { 14614, 10160, 12349, 9185, 839, 845, 847, 851, 855, 859, 841, 843, 849, 9705, 853, 857,
			861, 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 11235, 6724, 4734, 4934, 4935, 4936,
			4937, 10146, 10147, 10148 };
	public static int[] ARROWS = { 882, 884, 886, 888, 890, 892, 78, 4740, 11212, 9140, 9141, 4142, 9143, 9144, 9240,
			9241, 9242, 9243, 9244, 9245, 9706, 811 };
	public static int[] NO_ARROW_DROP = { 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 4734, 4934,
			4935, 4936, 4937, 868 };
	public static int[] OTHER_RANGE_WEAPONS = { 863, 864, 865, 866, 867, 868, 869, 806, 807, 808, 809, 810, 811, 825,
			826, 827, 828, 829, 830, 800, 801, 802, 803, 804, 805, 6522, 10033, 10034 };
	public final int[] TOXIC_BLOWPIPE = { 14614 };

	public static int[][] MAGIC_SPELLS = { { 1152, 1, 711, 90, 91, 92, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0, 993 }, // wind
																												// strike
			{ 1154, 5, 711, 93, 94, 95, 4, 7, 555, 1, 556, 1, 558, 1, 0, 0, 211 }, // water
																					// strike
			{ 1156, 9, 711, 96, 97, 98, 6, 9, 557, 2, 556, 1, 558, 1, 0, 0, 0 }, // earth
																					// strike
			{ 1158, 13, 711, 99, 100, 101, 8, 11, 554, 3, 556, 2, 558, 1, 0, 0, 0 }, // fire
																						// strike
			{ 1160, 17, 711, 117, 118, 119, 9, 13, 556, 2, 562, 1, 0, 0, 0, 0, 0 }, // wind
																					// bolt
			{ 1163, 23, 711, 120, 121, 122, 10, 16, 556, 2, 555, 2, 562, 1, 0, 0, 0 }, // water
																						// bolt
			{ 1166, 29, 711, 123, 124, 125, 11, 20, 556, 2, 557, 3, 562, 1, 0, 0, 0 }, // earth
																						// bolt
			{ 1169, 35, 711, 126, 127, 128, 12, 22, 556, 3, 554, 4, 562, 1, 0, 0, 0 }, // fire
																						// bolt
			{ 1172, 41, 711, 132, 133, 134, 13, 25, 556, 3, 560, 1, 0, 0, 0, 0, 0 }, // wind
																						// blast
			{ 1175, 47, 711, 135, 136, 137, 14, 28, 556, 3, 555, 3, 560, 1, 0, 0, 0 }, // water
																						// blast
			{ 1177, 53, 711, 138, 139, 140, 15, 31, 556, 3, 557, 4, 560, 1, 0, 0, 0 }, // earth
																						// blast
			{ 1181, 59, 711, 129, 130, 131, 16, 35, 556, 4, 554, 5, 560, 1, 0, 0, 0 }, // fire
																						// blast
			{ 1183, 62, 727, 158, 159, 160, 17, 36, 556, 5, 565, 1, 0, 0, 0, 0, 0 }, // wind
																						// wave
			{ 1185, 65, 727, 161, 162, 163, 18, 37, 556, 5, 555, 7, 565, 1, 0, 0, 0 }, // water
																						// wave
			{ 1188, 70, 727, 164, 165, 166, 19, 40, 556, 5, 557, 7, 565, 1, 0, 0, 0 }, // earth
																						// wave
			{ 1189, 75, 727, 155, 156, 157, 20, 42, 556, 5, 554, 7, 565, 1, 0, 0, 0 }, // fire
																						// wave

			{ 1153, 3, 716, 102, 103, 104, 0, 13, 555, 3, 557, 2, 559, 1, 0, 0, 0 }, // confuse
			{ 1157, 11, 716, 105, 106, 107, 0, 20, 555, 3, 557, 2, 559, 1, 0, 0, 0 }, // weaken
			{ 1161, 19, 716, 108, 109, 110, 0, 29, 555, 2, 557, 3, 559, 1, 0, 0, 0 }, // curse
			{ 1542, 66, 729, 167, 168, 169, 0, 76, 557, 5, 555, 5, 566, 1, 0, 0, 0 }, // vulnerability
			{ 1543, 73, 729, 170, 171, 172, 0, 83, 557, 8, 555, 8, 566, 1, 0, 0, 0 }, // enfeeble
			{ 1562, 80, 729, 173, 174, 107, 0, 90, 557, 12, 555, 12, 556, 1, 0, 0, 0 }, // stun

			{ 1572, 20, 710, 177, 178, 181, 0, 30, 557, 3, 555, 3, 561, 2, 0, 0, 0 }, // bind
			{ 1582, 50, 710, 177, 178, 180, 2, 60, 557, 4, 555, 4, 561, 3, 0, 0, 0 }, // snare
			{ 1592, 79, 710, 177, 178, 179, 4, 90, 557, 5, 555, 5, 561, 4, 0, 0, 0 }, // entangle

			{ 1171, 39, 724, 145, 146, 147, 15, 25, 556, 2, 557, 2, 562, 1, 0, 0, 0 }, // crumble
																						// undead
			{ 1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0, 0 }, // iban
																					// blast
			{ 12037, 50, 1576, 327, 328, 329, 19, 30, 560, 1, 558, 4, 0, 0, 0, 0, 0 }, // magic
																						// dart

			{ 1190, 60, 811, 0, 0, 76, 20, 60, 554, 2, 565, 2, 556, 4, 0, 0, 0 }, // sara
																					// strike
			{ 1191, 60, 811, 0, 0, 77, 20, 60, 554, 1, 565, 2, 556, 4, 0, 0, 0 }, // cause
																					// of
																					// guthix
			{ 1192, 60, 811, 0, 0, 78, 20, 60, 554, 4, 565, 2, 556, 1, 0, 0, 0 }, // flames
																					// of
																					// zammy

			{ 12445, 85, 1819, 0, 344, 345, 0, 65, 563, 1, 562, 1, 560, 1, 0, 0, 0 }, // teleblock

			// Ancient Spells
			{ 12939, 50, 1978, 0, 384, 385, 13, 30, 560, 2, 562, 2, 554, 1, 556, 1, 0 }, // smoke
																							// rush
			{ 12987, 52, 1978, 0, 378, 379, 14, 31, 560, 2, 562, 2, 566, 1, 556, 1, 0 }, // shadow
																							// rush
			{ 12901, 56, 1978, 0, 0, 373, 15, 33, 560, 2, 562, 2, 565, 1, 0, 0, 0 }, // blood
																						// rush
			{ 12861, 58, 1978, 0, 360, 361, 16, 34, 560, 2, 562, 2, 555, 2, 0, 0, 0 }, // ice
																						// rush
			{ 12963, 62, 1979, 0, 0, 389, 19, 36, 560, 2, 562, 4, 556, 2, 554, 2, 0 }, // smoke
																						// burst
			{ 13011, 64, 1979, 0, 0, 382, 20, 37, 560, 2, 562, 4, 556, 2, 566, 2, 0 }, // shadow
																						// burst
			{ 12919, 68, 1979, 0, 0, 376, 21, 39, 560, 2, 562, 4, 565, 2, 0, 0, 0 }, // blood
																						// burst
			{ 12881, 70, 1979, 0, 0, 363, 22, 40, 560, 2, 562, 4, 555, 4, 0, 0, 0 }, // ice
																						// burst
			{ 12951, 74, 1978, 0, 386, 387, 23, 42, 560, 2, 554, 2, 565, 2, 556, 2, 0 }, // smoke
																							// blitz
			{ 12999, 76, 1978, 0, 380, 381, 24, 43, 560, 2, 565, 2, 556, 2, 566, 2, 0 }, // shadow
																							// blitz
			{ 12911, 80, 1978, 0, 374, 375, 25, 45, 560, 2, 565, 4, 0, 0, 0, 0, 0 }, // blood
																						// blitz
			{ 12871, 82, 1978, 366, 0, 367, 26, 46, 560, 2, 565, 2, 555, 3, 0, 0, 0 }, // ice
																						// blitz
			{ 12975, 86, 1979, 0, 0, 391, 27, 48, 560, 4, 565, 2, 556, 4, 554, 4, 0 }, // smoke
																						// barrage
			{ 13023, 88, 1979, 0, 0, 383, 28, 49, 560, 4, 565, 2, 556, 4, 566, 3, 0 }, // shadow
																						// barrage
			{ 12929, 92, 1979, 0, 0, 377, 29, 51, 560, 4, 565, 4, 566, 1, 0, 0, 0 }, // blood
																						// barrage
			{ 12891, 94, 1979, 0, 0, 369, 30, 52, 560, 4, 565, 2, 555, 6, 0, 0, 0 }, // ice
																						// barrage

			{ -1, 80, 811, 301, 0, 0, 0, 0, 554, 3, 565, 3, 556, 3, 0, 0, 0 }, // charge
			{ -1, 21, 712, 112, 0, 0, 0, 10, 554, 3, 561, 1, 0, 0, 0, 0, 0 }, // low
																				// alch
			{ -1, 55, 713, 113, 0, 0, 0, 20, 554, 5, 561, 1, 0, 0, 0, 0, 0 }, // high
																				// alch
			{ -1, 33, 728, 142, 143, 144, 0, 35, 556, 1, 563, 1, 0, 0, 0, 0, 0 }, // telegrab
			{ 1175, 47, 791, 0, 136, 137, 14, 28, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // water
			{ 1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0, 0 }, // iban
			// blast
			// blast
			// water
			// blast

	};

	public boolean isAutoButton(int button) {
		for (int j = 0; j < autocastIds.length; j += 2) {
			if (autocastIds[j] == button) {
				return true;
			}
		}
		return false;
	}

	public boolean skillCapeEquipped() {
		for (int i = 10639; i < 12171; i++) {
			for (int j = 9747; j < 9814; j++) {
				if (playerEquipment[playerCape] == i || playerEquipment[playerCape] == j
						|| playerEquipment[playerCape] == 10662 || playerEquipment[playerCape] == 9949
						|| playerEquipment[playerCape] == 10646 || playerEquipment[playerCape] == 9948
						|| playerEquipment[playerCape] == 12170) {
					return true;
				}
			}
		}
		return false;
	}

	public int[] autocastIds = { 51133, 32, 51185, 33, 51091, 34, 24018, 35, 51159, 36, 51211, 37, 51111, 38, 51069, 39,
			51146, 40, 51198, 41, 51102, 42, 51058, 43, 51172, 44, 51224, 45, 51122, 46, 51080, 47, 7038, 0, 7039, 1,
			7040, 2, 7041, 3, 7042, 4, 7043, 5, 7044, 6, 7045, 7, 7046, 8, 7047, 9, 7048, 10, 7049, 11, 7050, 12, 7051,
			13, 7052, 14, 7053, 15, 47019, 27, 47020, 25, 47021, 12, 47022, 13, 47023, 14, 47024, 15 };

	public void assignAutocast(int button) {
		for (int j = 0; j < autocastIds.length; j++) {
			if (autocastIds[j] == button) {
				Player c = World.PLAYERS.get(this.getIndex());
				autocasting = true;
				autocastId = autocastIds[j + 1];
				c.getPA().sendFrame36(108, 1);
				c.setSidebarInterface(0, 328);
				break;
			}
		}
	}

	public int[][] barrowsNpcs = { { 2030, 0 }, // verac
			{ 2029, 0 }, // toarg
			{ 2028, 0 }, // karil
			{ 2027, 0 }, // guthan
			{ 2026, 0 }, // dharok
			{ 2025, 0 } // ahrim
	};

	public final int[] REDUCE_SPELL_TIME = { 250000, 250000, 250000, 500000, 500000, 500000 };
	public long[] reduceSpellDelay = new long[6];
	public final int[] REDUCE_SPELLS = { 1153, 1157, 1161, 1542, 1543, 1562 };
	public boolean[] canUseReducingSpell = { true, true, true, true, true, true };

	public final int[] PRAYER_DRAIN_RATE = { 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
			500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500 };
	public final int[] PRAYER_LEVEL_REQUIRED = { 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 37, 40, 43,
			44, 45, 46, 49, 52, 60, 70 };
	public final int[] PRAYER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25 };
	public final String[] PRAYER_NAME = { "Thick Skin", "Burst of Strength", "Clarity of Thought", "Sharp Eye",
			"Mystic Will", "Rock Skin", "Superhuman Strength", "Improved Reflexes", "Rapid Restore", "Rapid Heal",
			"Protect Item", "Hawk Eye", "Mystic Lore", "Steel Skin", "Ultimate Strength", "Incredible Reflexes",
			"Protect from Magic", "Protect from Missiles", "Protect from Melee", "Eagle Eye", "Mystic Might",
			"Retribution", "Redemption", "Smite", "Chivalry", "Piety" };
	public final int[] PRAYER_GLOW = { 83, 84, 85, 700, 701, 86, 87, 88, 89, 90, 91, 702, 703, 92, 93, 94, 95, 96, 97,
			704, 705, 98, 99, 100, 706, 707 };
	public final int[] PRAYER_HEAD_ICONS = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 0,
			-1, -1, 3, 5, 4, -1, -1 };

	public boolean[] prayerActive = { false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };

	/**
	 * Fight Pits
	 */
	public boolean inPits;
	public int pitsStatus;

	/**
	 * SouthWest, NorthEast, SouthWest, NorthEast
	 * 
	 * @param x
	 *            The x axis.
	 * @param y
	 *            The y axis.
	 * @param x1
	 *            The x1 axis.
	 * @param y1
	 *            The y1 axis.
	 * @return Return statement.
	 */

	public boolean safeAreas(int x, int y, int x1, int y1) {
		return (getAbsX() >= x && getAbsX() <= x1 && getAbsY() >= y && getAbsY() <= y1);
	}

	public boolean InKbd() {
		return (safeAreas(2250, 4672, 2296, 4721));
	}

	public boolean inCyclopsRoom() { // 2875, 3543
		return ((inArea(2837, 2875, 3543, 3557) || inArea(2846, 3534, 2877, 3549)) && !(absX == 2847 && absY == 3537)
				&& heightLevel == 2);
	}

	public boolean inSkillArea() {
		return (safeAreas(2849, 3425, 2862, 3434));
	}

	public boolean altars() {
		return (safeAreas(3090, 3506, 3097, 3506));
	}

	public boolean inBarrows() {
		return (safeAreas(3520, 9652, 3598, 9750));
	}

	public boolean arenas() {
		return (safeAreas(3331, 3243, 3390, 3259));
	}

	public boolean inBank() {
		return safeAreas(3091, 3488, 3098, 3499);
	}

	public boolean inArena() {
		return safeAreas(3331, 3205, 3391, 3260);
	}

	public boolean inDuelArena() {
		return (safeAreas(3322, 3195, 3394, 3291)) || (safeAreas(3311, 3223, 3323, 3248));
	}

	public boolean inPcGame() {
		return getAbsX() >= 2624 && getAbsX() <= 2690 && getAbsY() >= 2550 && getAbsY() <= 2619;
	}

	// return absX >= 2631 && absX <= 2681 && absY >= 2561 && absY <= 2621;
	public boolean inFightCaves() {
		return absX >= 2360 && absX <= 2445 && absY >= 5045 && absY <= 5125;
	}

	public boolean inHunterZone() {
		return absX >= 2918 && absX <= 2964 && absY >= 2942 && absY <= 3012;
	}

	public boolean inArea(int x1, int y1, int x2, int y2) {
		if (absX > x1 && absX < x2 && absY > y1 && absY < y2) {
			return true;
		}
		return false;
	}

	public boolean inArea3(int x, int y, int x1, int y1) {
		return getAbsX() > x && getAbsX() < x1 && getAbsY() < y && getAbsY() > y1;
	}

	public boolean inArea2(int x1, int y1, int x2, int y2) {
		if (absX >= x1 && absX <= x2 && absY >= y1 && absY <= y2) {
			return true;
		}
		return false;
	}

	// public int safeTimer;
	// public String playerOpponent = "";

	public boolean inWild() {
		/*
		 * if (this.safeTimer > 0 && this.playerOpponent.length() > 0) return
		 * true;
		 */
		return (safeAreas(2941, 3521, 3392, 3966)) || (safeAreas(2941, 9920, 3392, 10366))
				|| (safeAreas(2250, 4672, 2296, 4721));
	}

	public boolean isInGWD() {
		return (safeAreas(2840, 5270, 2920, 5360) && heightLevel == 6);
	}

	public boolean inMulti() {
		return (getAbsX() >= 3136 && getAbsX() <= 3327 && getAbsY() >= 3519 && getAbsY() <= 3607)
				|| (getAbsX() >= 3190 && getAbsX() <= 3327 && getAbsY() >= 3648 && getAbsY() <= 3839)
				|| (getAbsX() >= 2625 && getAbsX() <= 2685 && getAbsY() >= 2550 && getAbsY() <= 2620) || // Pest
																											// Control
				(getAbsX() >= 2700 && getAbsX() <= 2790 && getAbsY() >= 9000 && getAbsY() <= 9200)
				|| (getAbsX() >= 2864 && getAbsX() <= 2877 && getAbsY() >= 5348 && getAbsY() <= 5374) || // bandos
				(getAbsX() >= 2917 && getAbsX() <= 2937 && getAbsY() >= 5315 && getAbsY() <= 5332) || // zammy
				(getAbsX() >= 2884 && getAbsX() <= 2991 && getAbsY() >= 5255 && getAbsY() <= 5278) || // sara
				(getAbsX() >= 2821 && getAbsX() <= 2844 && getAbsY() >= 5292 && getAbsY() <= 5311) || // armadyl
				(getAbsX() >= 3200 && getAbsX() <= 3390 && getAbsY() >= 3840 && getAbsY() <= 3967)
				|| (getAbsX() >= 2968 && getAbsX() <= 2988 && getAbsY() >= 9512 && getAbsY() <= 9523) || // barrelchest
				(getAbsX() >= 2992 && getAbsX() <= 3007 && getAbsY() >= 3912 && getAbsY() <= 3967)
				|| (getAbsX() >= 2946 && getAbsX() <= 2959 && getAbsY() >= 3816 && getAbsY() <= 3831)
				|| (getAbsX() >= 3008 && getAbsX() <= 3199 && getAbsY() >= 3856 && getAbsY() <= 3903)
				|| (getAbsX() >= 3008 && getAbsX() <= 3071 && getAbsY() >= 3600 && getAbsY() <= 3711)
				|| (getAbsX() >= 3072 && getAbsX() <= 3327 && getAbsY() >= 3608 && getAbsY() <= 3647)
				|| (getAbsX() >= 2624 && getAbsX() <= 2690 && getAbsY() >= 2550 && getAbsY() <= 2619)
				|| (getAbsX() >= 2371 && getAbsX() <= 2422 && getAbsY() >= 5062 && getAbsY() <= 5117)
				|| (getAbsX() >= 2896 && getAbsX() <= 2927 && getAbsY() >= 3595 && getAbsY() <= 3630)
				|| (getAbsX() >= 2892 && getAbsX() <= 2932 && getAbsY() >= 4435 && getAbsY() <= 4464)
				|| (getAbsX() >= 2256 && getAbsX() <= 2287 && getAbsY() >= 4680 && getAbsY() <= 4711)
				|| (getAbsX() >= 2378 && getAbsX() <= 2415 && getAbsY() >= 5133 && getAbsY() <= 5167)
				|| (getAbsX() >= 2572 && getAbsX() <= 2584 && getAbsY() >= 9887 && getAbsY() <= 9889)
				|| (getAbsX() >= 2512 && getAbsX() <= 2540 && getAbsY() >= 4633 && getAbsY() <= 4659)
				|| (getAbsX() >= 2770 && getAbsX() <= 2798 && getAbsY() >= 9321 && getAbsY() <= 9340)
				|| (getAbsX() >= 2315 && getAbsX() <= 2354 && getAbsY() >= 3693 && getAbsY() <= 3716)
				|| (getAbsX() >= 3357 && getAbsX() <= 3383 && getAbsY() >= 3721 && getAbsY() <= 3749)
				|| (getAbsX() >= 3093 && getAbsX() <= 3118 && getAbsY() >= 3922 && getAbsY() <= 3947)
				|| (getAbsX() >= 2785 && getAbsX() <= 2809 && getAbsY() >= 2775 && getAbsY() <= 2795)
				|| (getAbsX() >= 1855 && absX <= 1921 && absY >= 5181 && absY <= 5251)
				|| (getAbsX() >= 2894 && getAbsX() <= 2924 && getAbsY() >= 3596 && getAbsY() <= 3630)
				|| (getAbsX() >= 2660 && getAbsX() <= 2730 && getAbsY() >= 3707 && getAbsY() <= 3737)
				|| (getAbsX() >= 2880 && getAbsX() <= 3005 && getAbsY() >= 4360 && getAbsY() <= 4415);

	}

	public String connectedFrom = "";
	public String globalMessage = "";
	public String playerName = null;
	public String playerName2 = null;
	private String displayName = null;
	public String playerPass = null;
	public int playerRights;
	public int playerItems[] = new int[28];
	public int playerItemsN[] = new int[28];

	public int bankItems[] = new int[Constants.BANK_SIZE];
	public int bankItemsN[] = new int[Constants.BANK_SIZE];

	public int playerStandIndex = 0x328;
	public int playerTurnIndex = 0x337;
	public int playerWalkIndex = 0x333;
	public int playerTurn180Index = 0x334;
	public int playerTurn90CWIndex = 0x335;
	public int playerTurn90CCWIndex = 0x336;
	public int playerRunIndex = 0x338;

	public static final int playerHat = 0;
	public static final int playerCape = 1;
	public static final int playerAmulet = 2;
	public static final int playerWeapon = 3;
	public static final int playerChest = 4;
	public static final int playerShield = 5;
	public static final int playerLegs = 7;
	public static final int playerHands = 9;
	public static final int playerFeet = 10;
	public static final int playerRing = 12;
	public static final int playerArrows = 13;

	public static final int playerAttack = 0;
	public static final int playerDefence = 1;
	public static final int playerStrength = 2;
	public static final int playerHitpoints = 3;
	public static final int playerRanged = 4;
	public static final int playerPrayer = 5;
	public static final int playerMagic = 6;
	public static final int playerCooking = 7;
	public static final int playerWoodcutting = 8;
	public static final int playerFletching = 9;
	public static final int playerFishing = 10;
	public static final int playerFiremaking = 11;
	public static final int playerCrafting = 12;
	public static final int playerSmithing = 13;
	public static final int playerMining = 14;
	public static final int playerHerblore = 15;
	public static final int playerAgility = 16;
	public static final int playerThieving = 17;
	public static final int playerSlayer = 18;
	public static final int playerFarming = 19;
	public static final int playerRunecrafting = 20;
	public static final int playerConstruction = 21;
	public static final int playerHunting = 22;

	public int[] playerEquipment = new int[14];
	public int[] playerEquipmentN = new int[14];
	public int[] lastWeapon = new int[14];
	public int[] playerLevel = new int[25];
	public int[] playerXP = new int[25];

	public void resetWalkingQueue() {
		wQueueReadPtr = wQueueWritePtr = 0;
		for (int i = 0; i < walkingQueueSize; i++) {
			walkingQueueX[i] = currentX;
			walkingQueueY[i] = currentY;
		}
	}

	/**
	 * 
	 * @param player
	 *            Execute vote reward system - HashMap
	 * @VoteRewardSelection $link$
	 */
	public static void voteRewardSystem(Player player) {
		new VoteRewardSelection(player, VoteRewardSelection.RewardType.VOTE_REWARD, 1).create();

	}

	private static boolean chance2;

	/**
	 * 
	 * @param player
	 * @return boolean chance2
	 */
	public static boolean voteReardDeclarationCheck(Player player) {
		return chance2;
	}

	public void addToWalkingQueue(int x, int y) {
		int next = (wQueueWritePtr + 1) % walkingQueueSize;
		if (next == wQueueWritePtr)
			return;
		walkingQueueX[wQueueWritePtr] = x;
		walkingQueueY[wQueueWritePtr] = y;
		wQueueWritePtr = next;
	}

	public Set<Player> localPlayers = new LinkedHashSet<>(255);
	public Set<NPC> localNpcs = new LinkedHashSet<>(255);

	public boolean withinDistance(Player otherPlr) {
		if (heightLevel != otherPlr.heightLevel) {
			return false;
		}
		int deltaX = otherPlr.getAbsX() - getAbsX(), deltaY = otherPlr.getAbsY() - getAbsY();
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(NPC npc) {
		if (heightLevel != npc.heightLevel) {
			return false;
		}
		if (npc.needRespawn) {
			return false;
		}
		int deltaX = npc.getAbsX() - getAbsX(), deltaY = npc.getAbsY() - getAbsY();
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public int distanceToPoint(int pointX, int pointY) {
		return (int) Math.sqrt(Math.pow(getAbsX() - pointX, 2) + Math.pow(getAbsY() - pointY, 2));
	}

	public int mapRegionX, mapRegionY;
	public int currentX, currentY;

	public int playerSE = 0x328;
	public int playerSEW = 0x333;
	public int playerSER = 0x334;

	public boolean storing = false;
	public final int walkingQueueSize = 50;

	public int wQueueReadPtr = 0;
	public int wQueueWritePtr = 0;
	public boolean isRunning = true;
	public int teleportToX = -1, teleportToY = -1;

	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance)
				&& (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	public int finalDestX, finalDestY;
	public boolean walkingToObject;

	public boolean destinationReached() {
		// if(absX-getMapRegionX()*8 == finalDestX && absY-getMapRegionY()*8 ==
		// finalDestY && walkingToObject) {
		Position player = new Position(absX, absY, heightLevel);
		Position object = new Position(objectX, objectY, heightLevel);
		if (player.equals(object) || player.withinDistance(object, objectDistance)) {
			return true;
		} else {
			return false;
		}
	}

	public int getNextWalkingDirection() {
		if (wQueueReadPtr == wQueueWritePtr)
			return -1;
		int dir;
		do {
			dir = Misc.direction(currentX, currentY, walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);
			if (dir == -1) {
				wQueueReadPtr = (wQueueReadPtr + 1) % walkingQueueSize;
			} else if ((dir & 1) != 0) {
				resetWalkingQueue();
				return -1;
			}
		} while ((dir == -1) && (wQueueReadPtr != wQueueWritePtr));
		if (dir == -1)
			return -1;
		dir >>= 1;
		currentX += Misc.directionDeltaX[dir];
		currentY += Misc.directionDeltaY[dir];
		absX += Misc.directionDeltaX[dir];
		absY += Misc.directionDeltaY[dir];
		updateWalkEntities();
		return dir;
	}

	private int distance(int x, int y, int x2, int y2) {
		int distX = Math.abs(x - x2);
		int distY = Math.abs(y - y2);
		if (distX == distY) {
			return distX + 1;
		}
		return distX > distY ? distX : distY;
	}

	public int getNextFollowDirection() {
		if (freezeTimer > 0) {
			return -1;
		}
		if (isDead || playerLevel[3] <= 0) {
			return -1;
		}
		MobileCharacter mobile;
		if (npcFollowIndex > 0) {
			mobile = NPCHandler.NPCS.get(npcFollowIndex);
		} else {
			mobile = World.PLAYERS.get(playerFollowIndex);
		}
		if (mobile == null) {
			return -1;
		}
		int lastX = mobile.getLastX();
		int lastY = mobile.getLastY();
		if (getAbsX() == lastX && getAbsY() == lastY) {
			return -1;
		}
		int x, y;
		int diffX = lastX - (x = getAbsX());
		int diffY = lastY - (y = getAbsY());
		int dir = Misc.direction(diffX, diffY);
		if (dir == -1) {
			return -1;
		}
		dir >>= 1;
		int z = heightLevel % 3;
		int dirX = Misc.directionDeltaX[dir];
		int dirY = Misc.directionDeltaY[dir];
		if (Math.abs(diffX) >= Math.abs(diffY)) {
			if (xDirectionBlocked(z, x, y, dirX)) {
				dirX = 0;
			}
			if (yDirectionBlocked(z, x, y, dirY)) {
				dirY = 0;
			}
		} else {
			if (yDirectionBlocked(z, x, y, dirY)) {
				dirY = 0;
			}
			if (xDirectionBlocked(z, x, y, dirX)) {
				dirX = 0;
			}
		}

		if (dirX == 0) {
			if (dirY == 0) {
				return -1;
			}
		} else {
			if (dirY != 0) {
				if (xyDirectionBlocked(z, x, y, dirX, dirY)) {
					if (Math.abs(diffX) >= Math.abs(diffY)) {
						dirY = 0;
					} else {
						dirX = 0;
					}
				}
			}
		}

		int positionX = getAbsX() + dirX;
		int positionY = getAbsY() + dirY;
		if (positionX == mobile.getAbsX() && positionY == mobile.getAbsY()) {
			return -1;
		}
		currentX += dirX;
		currentY += dirY;
		setAbsX(positionX);
		setAbsY(positionY);
		return Misc.direction(dirX, dirY) >> 1;
	}

	public int getNextCombatDirection() {
		if (freezeTimer > 0) {
			return -1;
		}
		if (isDead || playerLevel[3] <= 0) {
			return -1;
		}
		MobileCharacter mobile;
		if (npcFollowIndex > 0) {
			mobile = NPCHandler.NPCS.get(npcFollowIndex);
		} else {
			mobile = World.PLAYERS.get(playerFollowIndex);
		}
		int destX = mobile.getAbsX();
		int destY = mobile.getAbsY();
		if (absX == destX && absY == destY) {
			if (!CollisionMap.isSouthBlocked(heightLevel, absX, absY + 1)) {
				destY += 1;
			} else if (!CollisionMap.isNorthBlocked(heightLevel, absX, absY - 1)) {
				destY -= 1;
			} else if (!CollisionMap.isWestBlocked(heightLevel, absX + 1, absY)) {
				destX += 1;
			} else if (!CollisionMap.isEastBlocked(heightLevel, absX - 1, absY)) {
				destX -= 1;
			} else {
				return -1;
			}
		} else {
			if (distance(getAbsX(), getAbsY(), mobile.getAbsX(), mobile.getAbsY()) <= followDistance) {
				return -1;
			}
		}
		int x = getAbsX();
		int y = getAbsY();
		int diffX = destX - x;
		int diffY = destY - y;
		int z = heightLevel % 3;
		if (Math.abs(diffX) == 1 && Math.abs(diffY) == 1) {
			if (xDirectionBlocked(z, x, y, diffX)) {
				diffX = 0;
				if (yDirectionBlocked(z, x, y, diffY)) {
					diffY = 0;
				}
			} else {
				diffY = 0;
			}
		}
		int dir = Misc.direction(diffX, diffY);
		if (dir == -1) {
			return -1;
		}
		dir >>= 1;
		int dirX = Misc.directionDeltaX[dir];
		int dirY = Misc.directionDeltaY[dir];
		if (Math.abs(diffX) >= Math.abs(diffY)) {
			if (xDirectionBlocked(z, x, y, dirX)) {
				dirX = 0;
			}
			if (yDirectionBlocked(z, x, y, dirY)) {
				dirY = 0;
			}
		} else {
			if (yDirectionBlocked(z, x, y, dirY)) {
				dirY = 0;
			}
			if (xDirectionBlocked(z, x, y, dirX)) {
				dirX = 0;
			}
		}
		if (dirX == 0) {
			if (dirY == 0) {
				return -1;
			}
		} else {
			if (dirY != 0) {
				if (xyDirectionBlocked(z, x, y, dirX, dirY)) {
					if (Math.abs(diffX) >= Math.abs(diffY)) {
						dirY = 0;
					} else {
						dirX = 0;
					}
				}
			}
		}
		int positionX = getAbsX() + dirX;
		int positionY = getAbsY() + dirY;
		if (positionX == mobile.getAbsX() && positionY == mobile.getAbsY()) {
			return -1;
		}
		currentX += dirX;
		currentY += dirY;
		setAbsX(positionX);
		setAbsY(positionY);
		return Misc.direction(dirX, dirY) >> 1;
	}

	private static boolean xDirectionBlocked(int z, int x, int y, int dirX) {
		switch (dirX) {
		case 1:
			return CollisionMap.isWestBlocked(z, x + 1, y);
		case -1:
			return CollisionMap.isEastBlocked(z, x - 1, y);
		}
		return false;
	}

	private static boolean yDirectionBlocked(int z, int x, int y, int dirY) {
		switch (dirY) {
		case 1:
			return CollisionMap.isSouthBlocked(z, x, y + 1);
		case -1:
			return CollisionMap.isNorthBlocked(z, x, y - 1);
		}
		return false;
	}

	private static boolean xyDirectionBlocked(int z, int x, int y, int dirX, int dirY) {
		int newX = x + dirX;
		int newY = y + dirY;
		switch (dirX) {
		case 1:
			switch (dirY) {
			case 1:
				return CollisionMap.isSouthWestBlocked(z, newX, newY);
			case -1:
				return CollisionMap.isNorthWestBlocked(z, newX, newY);
			}
		case -1:
			switch (dirY) {
			case 1:
				return CollisionMap.isSouthEastBlocked(z, newX, newY);
			case -1:
				return CollisionMap.isNorthEastBlocked(z, newX, newY);
			}
		}
		throw new AssertionError();
	}

	public void getNextPlayerMovement() {
		mapRegionDidChange = false;
		didTeleport = false;
		dir1 = dir2 = -1;
		if (teleportToX != -1 && teleportToY != -1) {
			mapRegionDidChange = true;
			if (mapRegionX != -1 && mapRegionY != -1) {
				int relX = teleportToX - mapRegionX * 8, relY = teleportToY - mapRegionY * 8;
				if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8 && relY < 11 * 8)
					mapRegionDidChange = false;
			}
			if (mapRegionDidChange) {
				mapRegionX = (teleportToX >> 3) - 6;
				mapRegionY = (teleportToY >> 3) - 6;
			}
			currentX = teleportToX - 8 * mapRegionX;
			currentY = teleportToY - 8 * mapRegionY;
			absX = teleportToX;
			absY = teleportToY;
			resetWalkingQueue();

			teleportToX = teleportToY = -1;
			didTeleport = true;
			updateWalkEntities();
		} else {
			dir1 = getNextWalkingDirection();
			if (dir1 == -1)
				return;
			if (isRunning) {
				dir2 = getNextWalkingDirection();
				runEnergy -= 1;
			}
			int deltaX = 0, deltaY = 0;
			if (currentX < 2 * 8) {
				deltaX = 4 * 8;
				mapRegionX -= 4;
				mapRegionDidChange = true;
			} else if (currentX >= 11 * 8) {
				deltaX = -4 * 8;
				mapRegionX += 4;
				mapRegionDidChange = true;
			}
			if (currentY < 2 * 8) {
				deltaY = 4 * 8;
				mapRegionY -= 4;
				mapRegionDidChange = true;
			} else if (currentY >= 11 * 8) {
				deltaY = -4 * 8;
				mapRegionY += 4;
				mapRegionDidChange = true;
			}

			if (mapRegionDidChange) {
				currentX += deltaX;
				currentY += deltaY;
				for (int i = 0; i < walkingQueueSize; i++) {
					walkingQueueX[i] += deltaX;
					walkingQueueY[i] += deltaY;
				}
			}
		}
	}

	public int DirectionCount = 0;
	public boolean appearanceUpdateRequired = true;
	public int genieSelect = 0;
	public boolean isDead = false;

	private ByteBuffer playerProps = new ByteBuffer(new byte[300]);

	public int calculateCombatLevel() {
		int j = getLevelForXP(playerXP[playerAttack]);
		int k = getLevelForXP(playerXP[playerDefence]);
		int l = getLevelForXP(playerXP[playerStrength]);
		int i1 = getLevelForXP(playerXP[playerHitpoints]);
		int j1 = getLevelForXP(playerXP[playerPrayer]);
		int k1 = getLevelForXP(playerXP[playerRanged]);
		int l1 = getLevelForXP(playerXP[playerMagic]);
		int combatLevel = (int) (((k + i1) + Math.floor(j1 / 2)) * 0.25D) + 1;
		double d = (j + l) * 0.32500000000000001D;
		double d1 = Math.floor(k1 * 1.5D) * 0.32500000000000001D;
		double d2 = Math.floor(l1 * 1.5D) * 0.32500000000000001D;
		if (d >= d1 && d >= d2) {
			combatLevel += d;
		} else if (d1 >= d && d1 >= d2) {
			combatLevel += d1;
		} else if (d2 >= d && d2 >= d1) {
			combatLevel += d2;
		}
		return combatLevel;

	}

	public static int getLevelForXP(int exp) {
		int points = 0;
		int output;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 99;
	}

	private boolean chatTextUpdateRequired = false;
	private byte chatText[] = new byte[4096];
	private byte chatTextSize = 0;
	private int chatTextColor = 0;
	private int chatTextEffects = 0;

	public void forcedChat(String text) {
		forcedText = text;
		forcedChatUpdateRequired = true;
		updateRequired = true;
		setAppearanceUpdateRequired(true);
	}

	public String forcedText = "null";

	/**
	 * Graphics
	 */

	public int mask100var1 = 0;
	public int mask100var2 = 0;
	private boolean mask100update = false;

	public void gfx100(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 6553600;
		setMask100update(true);
		updateRequired = true;
	}

	public void gfx0(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 65536;
		setMask100update(true);
		updateRequired = true;
	}

	public boolean wearing2h() {
		Player c = this;
		String s = ItemAssistant.getItemName(c.playerEquipment[playerWeapon]);
		if (s.contains("2h")) {
			return true;
		} else if (s.contains("godsword")) {
			return true;
		}
		return false;
	}

	/**
	 * Animations
	 * 
	 * @param animId
	 *            The animation id.
	 */
	public void startAnimation(int animId) {
		animationRequest = animId;
		animationWaitCycles = 0;
		updateRequired = true;
	}

	public void startAnimation(int animId, int time) {
		animationRequest = animId;
		animationWaitCycles = time;
		updateRequired = true;
	}

	/**
	 * Face Update
	 */

	private boolean faceUpdateRequired = false;
	public int face = -1;
	public int FocusPointX = -1, FocusPointY = -1;

	public void faceUpdate(int index) {
		face = index;
		setFaceUpdateRequired(true);
		updateRequired = true;
	}

	public void turnPlayerTo(int pointX, int pointY) {
		FocusPointX = 2 * pointX + 1;
		FocusPointY = 2 * pointY + 1;
		updateRequired = true;
	}

	public boolean hasDied = false;

	private void hasDied() {
		hasDied = true;
		Server.getTaskScheduler().schedule(new ScheduledTask(1, true) {

			@Override
			public void execute() {
				if (!isRegistered() || !hasDied) {
					stop();
					return;
				}
				switch (countdown) {
				case 0:
					isDead = true;
					resetWalkingQueue();
					break;
				case 1:
					startAnimation(0x900);
					poisonDamage = -1;
					break;
				case 5:
					getPA().applyDead();
					break;
				case 6:
					getPA().giveLife();
					isDead = false;
					stop();
					break;
				}
				countdown++;
			}

			@Override
			public void onStop() {
				hasDied = false;
				countdown = 0;
			}
		}.attach(this));
	}

	private ByteBuffer updateBlock = null;

	public boolean forceMovementUpdateRequired = false;
	private int x1 = -1;
	private int y1 = -1;
	private int x2 = -1;
	private int y2 = -1;
	private int speed1 = -1;
	private int speed2 = -1;
	private int direction = -1;

	public void setForceMovement(final int x2, final int y2, boolean x1, boolean y1, final int speed1, final int speed2,
			final int direction, final int time, final int emote, final int teleX, final int teleY, final int height) {
		canWalk = false;
		this.x1 = currentX;
		this.y1 = currentY;
		this.x2 = x1 ? currentX + x2 : currentX - x2;
		this.y2 = y1 ? currentY + y2 : currentY - y2;
		this.speed1 = speed1;
		this.speed2 = speed2;
		this.direction = direction;
		updateRequired = true;
		forceMovementUpdateRequired = true;
		this.startAnimation(emote);
		this.getCombat().getPlayerAnimIndex(ItemAssistant.getItemName(playerEquipment[playerWeapon]).toLowerCase());
		Server.getTaskScheduler().schedule(new ScheduledTask((x2 + y2) * time) {
			@Override
			public void execute() {
				updateRequired = true;
				forceMovementUpdateRequired = false;
				canWalk = true;
				super.stop();
				getPA().movePlayer(teleX, teleY, height);
			}
		}.attach(this));
	}

	public boolean canWalk = true;

	public void appendMask400Update(ByteBuffer str) {
		str.writeByteS(x1);
		str.writeByteS(y1);
		str.writeByteS(x2);
		str.writeByteS(y2);
		str.writeWordBigEndianA(speed1);
		str.writeWordA(speed2);
		str.writeByteS(direction);
	}

	public void clearUpdateFlags() {
		forceMovementUpdateRequired = false;
		updateRequired = false;
		setChatTextUpdateRequired(false);
		setAppearanceUpdateRequired(false);
		setHitUpdateRequired(false);
		hitUpdateRequired2 = false;
		forcedChatUpdateRequired = false;
		setMask100update(false);
		animationRequest = -1;
		FocusPointX = -1;
		FocusPointY = -1;
		setFaceUpdateRequired(false);
		face = 65535;
		setUpdateBlock(null);
		super.clear();
	}

	public void stopMovement() {
		if (teleportToX <= 0 && teleportToY <= 0) {
			teleportToX = absX;
			teleportToY = absY;
		}
		newWalkCmdSteps = 0;
		getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] = travelBackY[0] = 0;
		getNextPlayerMovement();
	}

	private int newWalkCmdX[] = new int[walkingQueueSize];
	private int newWalkCmdY[] = new int[walkingQueueSize];
	public int newWalkCmdSteps = 0;
	private boolean newWalkCmdIsRunning = false;
	protected int travelBackX[] = new int[walkingQueueSize];
	protected int travelBackY[] = new int[walkingQueueSize];
	protected int numTravelBackSteps = 0;

	public void preProcessing() {
		newWalkCmdSteps = 0;
	}

	public void postProcessing() {
		if (newWalkCmdSteps > 0) {
			int firstX = getNewWalkCmdX()[0], firstY = getNewWalkCmdY()[0];
			int lastDir = 0;
			boolean found = false;
			numTravelBackSteps = 0;
			int ptr = wQueueReadPtr;
			int dir = Misc.direction(currentX, currentY, firstX, firstY);
			if (dir != -1 && (dir & 1) != 0) {
				do {
					lastDir = dir;
					if (--ptr < 0)
						ptr = walkingQueueSize - 1;

					travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
					travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
					dir = Misc.direction(walkingQueueX[ptr], walkingQueueY[ptr], firstX, firstY);
					if (lastDir != dir) {
						found = true;
						break;
					}

				} while (ptr != wQueueWritePtr);
			} else
				found = true;
			if (!found) {
			} else {
				wQueueWritePtr = wQueueReadPtr;

				addToWalkingQueue(currentX, currentY);

				if (dir != -1 && (dir & 1) != 0) {

					for (int i = 0; i < numTravelBackSteps - 1; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
					int wayPointX2 = travelBackX[numTravelBackSteps - 1],
							wayPointY2 = travelBackY[numTravelBackSteps - 1];
					int wayPointX1, wayPointY1;
					if (numTravelBackSteps == 1) {
						wayPointX1 = currentX;
						wayPointY1 = currentY;
					} else {
						wayPointX1 = travelBackX[numTravelBackSteps - 2];
						wayPointY1 = travelBackY[numTravelBackSteps - 2];
					}

					dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2, wayPointY2);
					if (dir == -1 || (dir & 1) != 0) {
					} else {
						dir >>= 1;
						found = false;
						int x = wayPointX1, y = wayPointY1;
						while (x != wayPointX2 || y != wayPointY2) {
							x += Misc.directionDeltaX[dir];
							y += Misc.directionDeltaY[dir];
							if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
								found = true;
								break;
							}
						}
						if (!found) {
						} else
							addToWalkingQueue(wayPointX1, wayPointY1);
					}
				} else {
					for (int i = 0; i < numTravelBackSteps; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
				}

				for (int i = 0; i < newWalkCmdSteps; i++) {
					addToWalkingQueue(getNewWalkCmdX()[i], getNewWalkCmdY()[i]);
				}

			}

			isRunning = isNewWalkCmdIsRunning() || isRunning2;
		}
	}

	public int getMapRegionX() {
		return mapRegionX;
	}

	public int getMapRegionY() {
		return mapRegionY;
	}

	public int getX() {
		return getAbsX();
	}

	public int getHeight() {
		return heightLevel;
	}

	public int getY() {
		return getAbsY();
	}

	public boolean inPcBoat() {
		return getAbsX() >= 2660 && getAbsX() <= 2663 && getAbsY() >= 2638 && getAbsY() <= 2643;
	}

	public boolean inKalphiteLair() {
		return getAbsX() >= 3461 && getAbsX() <= 3494 && getAbsY() >= 9476 && getAbsY() <= 9506;
	}

	public boolean FightPitsArea() {
		return (getAbsX() >= 2378 && getAbsX() <= 2415 && getAbsY() >= 5133 && getAbsY() <= 5167)
				|| (getAbsX() >= 2394 && getAbsX() <= 2404 && getAbsY() >= 5169 && getAbsY() <= 5174);
	}

	public void setHitUpdateRequired(boolean hitUpdateRequired) {
		this.hitUpdateRequired = hitUpdateRequired;
	}

	public void setHitUpdateRequired2(boolean hitUpdateRequired2) {
		this.hitUpdateRequired2 = hitUpdateRequired2;
	}

	public boolean isHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired2() {
		return hitUpdateRequired2;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setChatTextEffects(int chatTextEffects) {
		this.chatTextEffects = chatTextEffects;
	}

	public int getChatTextEffects() {
		return chatTextEffects;
	}

	public void setChatTextSize(byte chatTextSize) {
		this.chatTextSize = chatTextSize;
	}

	public byte getChatTextSize() {
		return chatTextSize;
	}

	public void setChatTextUpdateRequired(boolean chatTextUpdateRequired) {
		this.chatTextUpdateRequired = chatTextUpdateRequired;
	}

	public boolean isChatTextUpdateRequired() {
		return chatTextUpdateRequired;
	}

	public void setChatText(byte chatText[]) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatTextColor(int chatTextColor) {
		this.chatTextColor = chatTextColor;
	}

	public void setNewWalkCmdX(int newWalkCmdX[]) {
		this.newWalkCmdX = newWalkCmdX;
	}

	public int[] getNewWalkCmdX() {
		return newWalkCmdX;
	}

	public void setNewWalkCmdY(int newWalkCmdY[]) {
		this.newWalkCmdY = newWalkCmdY;
	}

	public int[] getNewWalkCmdY() {
		return newWalkCmdY;
	}

	public void setNewWalkCmdIsRunning(boolean newWalkCmdIsRunning) {
		this.newWalkCmdIsRunning = newWalkCmdIsRunning;
	}

	public boolean isNewWalkCmdIsRunning() {
		return newWalkCmdIsRunning;
	}

	public int getChatTextColor() {
		return chatTextColor;
	}

	public void setInStreamDecryption(ISAACRandomGen inStreamDecryption) {
	}

	public void setOutStreamDecryption(ISAACRandomGen outStreamDecryption) {
	}

	public void putInCombat(int attacker) {
		underAttackBy = attacker;
		logoutDelay = System.currentTimeMillis();
		singleCombatDelay = System.currentTimeMillis();
	}

	public void appendRedemption() {
		Player c = World.PLAYERS.get(getIndex());
		if (prayerActive[22]) {
			playerLevel[3] += (int) (getLevelForXP(playerXP[5]) * .25);
			playerLevel[5] = 0;
			c.getPA().refreshSkill(3);
			c.getPA().refreshSkill(5);
			gfx0(436);
			c.getCombat().resetPrayers();
		}
	}

	public long getTotalXp() {
		long total = 0;
		for (int exp : playerXP) {
			total += exp;
		}
		return total;
	}

	public int getRealTotal() {
		int total = 0;
		for (int exp : playerXP) {
			total += getLevelForXP(exp);
		}
		return total;
	}

	public int walkingQueueX[] = new int[walkingQueueSize], walkingQueueY[] = new int[walkingQueueSize];

	private int lastRegionHeight;

	public int setLastRegionHeight(int height) {
		return this.lastRegionHeight = height;
	}

	public int getLastRegionHeight() {
		return this.lastRegionHeight;
	}

	public String getIdentity() {
		return identity;
	}

	public String setIdentity(String identity) {
		return this.identity = identity;
	}

	/**
	 * @return
	 */
	private int teleportStonePos[] = new int[3];

	public int[] getTeleportStonePos() {
		return teleportStonePos;
	}

	public int[] setTeleportStonePos(int[] newPos) {
		return teleportStonePos = newPos;
	}

	public boolean isSocialSlaying() {
		return isSocialSlaying;
	}

	public void setSocialSlaying(boolean isSocialSlaying) {
		this.isSocialSlaying = isSocialSlaying;
	}

	public int getSocialSlayerKills() {
		return socialSlayerKills;
	}

	public void setSocialSlayerKills(int socialSlayerKills) {
		this.socialSlayerKills = socialSlayerKills;
	}

	public int getSocialSlayerPoints() {
		return socialSlayerPoints;
	}

	public void setSocialSlayerPoints(int socialSlayerPoints) {
		this.socialSlayerPoints = socialSlayerPoints;
	}

	public int getSocialSlayerDisconnections() {
		return socialSlayerDisconnections;
	}

	public void setSocialSlayerDisconnections(int socialSlayerDisconnections) {
		this.socialSlayerDisconnections = socialSlayerDisconnections;
	}

	public String getLastSocialPlayer() {
		return lastSocialPlayer;
	}

	public void setLastSocialPlayer(String lastSocialPlayer) {
		this.lastSocialPlayer = lastSocialPlayer;
	}

	public boolean isCanUseFoodOnPlayer() {
		return canUseFoodOnPlayer;
	}

	public void setCanUseFoodOnPlayer(boolean canUseFoodOnPlayer) {
		this.canUseFoodOnPlayer = canUseFoodOnPlayer;
	}

	public boolean isCanSharePotion() {
		return canSharePotion;
	}

	public void setCanSharePotion(boolean canSharePotion) {
		this.canSharePotion = canSharePotion;
	}

	public PunishmentManager.PunishmentReport getMuteReport() {
		return muteReport;
	}

	public void setMuteReport(PunishmentManager.PunishmentReport muteReport) {
		this.muteReport = muteReport;
	}

	public double getWildernessPotential() {
		return wildernessPotential;
	}

	public void setWildernessPotential(double wildernessPotential) {
		this.wildernessPotential = wildernessPotential;
	}

	public boolean warnedPlayer = false;

	public boolean isPvpInterfaceVisible() {
		return pvpInterfaceVisible;
	}

	public void setPvpInterfaceVisible(boolean pvpInterfaceVisible) {
		this.pvpInterfaceVisible = pvpInterfaceVisible;
	}

	public int getCreaturePotionTimer() {
		return creaturePotionTimer;
	}

	public void setCreaturePotionTimer(int creaturePotionTimer) {
		this.creaturePotionTimer = creaturePotionTimer;
	}

	public void mapData(boolean s) {
		outStream.createFrame(99);
		outStream.writeByte(s ? 2 : 0);
	}

	public int lastClickedNpc;

	public Player(Channel s) {
		super(MobileCharacterType.PLAYER);
		for (int i = 0; i < playerItems.length; i++) {
			playerItems[i] = 0;
		}
		for (int i = 0; i < playerItemsN.length; i++) {
			playerItemsN[i] = 0;
		}

		for (int i = 0; i < playerLevel.length; i++) {
			if (i == 3) {
				playerLevel[i] = 10;
			} else {
				playerLevel[i] = 1;
			}
		}
		for (int i = 0; i < playerXP.length; i++) {
			if (i == 3) {
				playerXP[i] = 1300;
			} else {
				playerXP[i] = 0;
			}
		}
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			bankItems[i] = 0;
		}

		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			bankItemsN[i] = 0;
		}

		playerAppearance[0] = 0; // gender
		playerAppearance[1] = 0; // head
		playerAppearance[2] = 18;// Torso
		playerAppearance[3] = 26; // arms
		playerAppearance[4] = 33; // hands
		playerAppearance[5] = 36; // legs
		playerAppearance[6] = 42; // feet
		playerAppearance[7] = 10; // beard
		playerAppearance[8] = 0; // hair colour
		playerAppearance[9] = 0; // torso colour
		playerAppearance[10] = 0; // legs colour
		playerAppearance[11] = 0; // feet colour
		playerAppearance[12] = 0; // skin colour
		apset = 0;
		actionID = 0;
		playerEquipment[playerHat] = -1;
		playerEquipment[playerCape] = -1;
		playerEquipment[playerAmulet] = -1;
		playerEquipment[playerChest] = -1;
		playerEquipment[playerShield] = -1;
		playerEquipment[playerLegs] = -1;
		playerEquipment[playerHands] = -1;
		playerEquipment[playerFeet] = -1;
		playerEquipment[playerRing] = -1;
		playerEquipment[playerArrows] = -1;
		playerEquipment[playerWeapon] = -1;
		heightLevel = 0;
		teleportToX = Constants.START_LOCATION_X;
		teleportToY = Constants.START_LOCATION_Y;
		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
		this.session = s;
		outStream = new ByteBuffer(new byte[Constants.BUFFER_SIZE]);
		outStream.currentOffset = 0;
		inStream = new ByteBuffer(new byte[Constants.BUFFER_SIZE]);
		inStream.currentOffset = 0;
	}

	@Override
	public Hit decrementHP(Hit hit) {
		int damage = hit.getDamage();
		if (damage > this.playerLevel[3])
			damage = this.playerLevel[3];
		this.playerLevel[3] -= damage;
		this.getPA().refreshSkill(3);
		int difference = playerLevel[3] - damage;
		if (difference <= getLevelForXP(playerXP[3]) / 10 && difference > 0) {
			appendRedemption();
		}
		return new Hit(damage, hit.getType());
	}

	public void flushOutStream() {
		if (outStream == null || !session.isOpen() || disconnected || (outStream.currentOffset == 0)) {
			return;
		}
		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		session.writeAndFlush(new Packet(-1, Type.FIXED, Unpooled.wrappedBuffer(temp)));
		outStream.currentOffset = 0;
	}

	public void sendClan(String name, String message, String clan) {
		outStream.createFrameVarSizeWord(217);
		outStream.writeString(name);
		outStream.writeString(message);
		outStream.writeString(clan);
		outStream.writeWord(0);
		outStream.endFrameVarSize();
	}

	public void sendClan(String name, String message, String clan, int rights) {
		outStream.createFrameVarSizeWord(217);
		outStream.writeString(name);
		outStream.writeString(message);
		outStream.writeString(clan);
		outStream.writeWord(rights);
		outStream.endFrameVarSize();
	}

	public static final int PACKET_LENGTHS[] = { 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			-1, 0, 0, 0, 4, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			1, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, -1, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			6, 10, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 4, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};

	public boolean teleportRequired = false;

	public void teleportRequired(Player player) {
		Server.getTaskScheduler().schedule(new ScheduledTask(3) {
			@Override
			public void execute() {
				if (teleportRequired) {
					player.getPA().movePlayer(3087, 3500, 0);
					this.stop();
				}
				if (!teleportRequired)
					this.stop();
			}

			@Override
			public void onStop() {
				teleportRequired = false;

			}
		}.attach(this));
	}

	public void destruct() {

		if (getPA().viewingOtherBank) {
			sendMessage("You are no longer viewing another players bank.");
			getPA().resetOtherBank();// add that to each ytou're fast
		}
		if (session == null) {
			return;
		}
		CycleEventHandler.getSingleton().stopEvents(this);
		if (this.inCyclopsRoom()) {
			this.getPA().movePlayer(0, 0, 0);
		}
		if (Boundary.isInBounds(this, Boundary.ZULRAH)) {
			teleportRequired = true;
		}
		if (this.getPet().getNpc() != null && this.getPet().getPet() != null) {
			this.getPet().dispose();
		}
		if (PestControl.isInPcBoat(this) || PestControl.isInGame(this)) {
			PestControl.removePlayerGame(this);
		}
		if (this.clan != null) {
			this.clan.removeMember(this);
		}
		if (this.duelStatus == 6) {
			getTradeAndDuel().claimStakedItems();
		}
		if (this.isSocialSlaying()) {
			this.slayerBossTask = false;
		}
		if (getCC().hasCannon())
			CannonManager.retrieveCannon(this, getCC().getCannon().getPosition(), true);
		Server.getTaskScheduler().stopAll(this);
		Misc.println("LOGGED OUT2: " + playerName + "");
		if (zulrah != null && zulrah.getInstance() != null)
			InstancedAreaManager.getSingleton().disposeOf(zulrah.getInstance());
		saveFile = true;
		disconnected = true;
		saveCharacter = true;
		PlayerSave.saveGame(this);
		session.close();
		session = null;
		inStream = null;
		outStream = null;
		isActive = false;
		localPlayers.clear();
		absX = absY = mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
		World.PLAYERS.remove(this);
	}

	public final void sendMessage(String s) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.writeByte(0);
			outStream.endFrameVarSize();
		}
	}

	public final void sendMessage(String s, Melee.Sets set) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.writeByte(0);
			outStream.endFrameVarSize();
		}
	}

	public void sendMessage(boolean b) {
		sendMessage(Boolean.toString(b));
	}

	public void sendPushNotification(String text) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(text);
			outStream.writeByte(1);
			outStream.endFrameVarSize();
		}
	}

	public void setSidebarInterface(int menuId, int form) {
		if (getOutStream() != null) {
			outStream.createFrame(71);
			outStream.writeWord(form);
			outStream.writeByteA(menuId);
		}
	}

	/**
	 * 
	 * @param id
	 * @param
	 */

	public void setConfig(int id, int state) {
		if (this.splitChat) {
			this.getPA().sendFrame36(502, 1);
			this.getPA().sendFrame36(287, 1);
		} else {
			this.splitChat = false;
			this.getPA().sendFrame36(502, 0);
			this.getPA().sendFrame36(287, 0);
		}
	}

	private void loadInterfaces() {
		int[] test = { 2423, 3917, 638, 3213, 1644, 5608, -1, 18128, 5065, 5715, 2449, 904, 147, 962 };
		for (int i = 0; i < 14; i++) {
			setSidebarInterface(i, test[i]);
		}
		switch (playerMagicBook) {
		case 0:
			setSidebarInterface(6, 1151); // modernl
			break;
		case 1:
			setSidebarInterface(6, 12855); // ancient
			break;
		case 2:
			setSidebarInterface(6, 29999); // lunar
			break;
		}
	}

	public boolean isExtremeDonator = false;
	public boolean hasClaimedStarter;

	public void initialize() {
		if (!inWild())
			for (int j = 0; j < World.PLAYERS.capacity(); j++) {
				if (j == getIndex()) {
					continue;
				}
				if (World.PLAYERS.get(j) != null) {
					if (World.PLAYERS.get(j).playerName.equalsIgnoreCase(playerName)) {
						disconnected = true;
						return;
					}
				}
			}
		/*
		 * Write our index to the client before we initialize our player
		 */
		outStream.createFrame(249);
		outStream.writeByteA(0);
		outStream.writeWordBigEndianA(getIndex());
		flushOutStream();

		setModeEnum();

		if (this.getMode() == null) {
			this.setMode(ModeData.NORMAL);
		}

		if (this.totalPlaytime() > 10000)
			Achievements.increase(this, AchievementType.PROTECTION, 1);
		Achievements.checkIfFinished(this);
		if (this.amountDonated > 250) {
			this.isExtremeDonator = true;
		}
		if (teleportRequired) {
			teleportRequired(this);
		}
		TeleportHandler.sendStrings(this);
		this.fightMode = 2;
		if (!this.isMaxed()) {
			if (this.playerEquipment[Player.playerCape] == 15812 || this.playerEquipment[Player.playerCape] == 15377) {
				if (this.getItems().freeSlots() > 0)
					this.getItems().removeItem(Player.playerCape);
			}
			if (this.playerEquipment[Player.playerHat] == 15378) {
				if (this.getItems().freeSlots() > 0)
					this.getItems().removeItem(Player.playerHat);
			}
			if (this.playerEquipment[Player.playerChest] == 15376) {
				if (this.getItems().freeSlots() > 0)
					this.getItems().removeItem(Player.playerChest);
			}
			if (this.playerEquipment[Player.playerLegs] == 15375) {
				if (this.getItems().freeSlots() > 0)
					this.getItems().removeItem(Player.playerLegs);
			}
			if (this.playerEquipment[Player.playerHands] == 15374) {
				if (this.getItems().freeSlots() > 0)
					this.getItems().removeItem(Player.playerHands);
			}
			if (this.playerEquipment[Player.playerFeet] == 15373) {
				if (this.getItems().freeSlots() > 0)
					this.getItems().removeItem(Player.playerFeet);
			}

		}
		if (getBankPin().requiresUnlock()) {
			getBankPin().open(2);
		}
		for (int i = 0; i < 24; i++) {
			if (this.playerXP[i] > 200_000_000) {
				this.playerXP[i] = 200_000_000;
			}
		}
		this.getPA().checkSkillXP();
		this.muteReport = new PunishmentManager.PunishmentReport();
		this.muteReport.check(this.playerName, "mutes");
		this.getPA().logIntoPM();
		combatLevel = calculateCombatLevel();
		totalLevel = getPA().totalLevel();
		setConfig(166, 4);
		for (int i = 0; i < 24; i++) {
			getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
			getPA().refreshSkill(i);
		}
		for (int p = 0; p < PRAYER.length; p++) {
			prayerActive[p] = false;
			getPA().sendFrame36(PRAYER_GLOW[p], 0);
		}
		isFullHelm = Item.isFullHelm(playerEquipment[playerHat]);
		isFullMask = Item.isFullMask(playerEquipment[playerHat]);
		isFullBody = Item.isFullBody(playerEquipment[playerChest]);
		getPA().handleWeaponStyle();
		getPA().handleLoginText();
		getPA().sendFrame36(108, 0);// resets autocast button
		getPA().sendFrame36(172, 1);
		getPA().sendFrame107(); // reset screen
		getPA().setChatOptions(0, 0, 0); // reset private messaging options
		// getPA().sendFrame126(":prayer:prayer", -1);
		loadInterfaces();
		getPA().announcements();
		getGameOptions().updateUI();
		this.setLastRegionHeight(this.getHeight());
		getPA().sendFrame126(runEnergy + "%", 149);
          
		sendMessage("Welcome to " + Constants.SERVER_NAME + ".");
		getPA().showOption(4, 0, "Follow");
		getPA().showOption(5, 0, "Trade");
		getItems();
		getItems().resetItems(3214);
		getItems().sendWeapon(playerEquipment[playerWeapon], ItemAssistant.getItemName(playerEquipment[playerWeapon]));
		getItems().resetBonus();
		getItems().getBonus();
		getItems().writeBonus();
		for (int equip = 0; equip < playerEquipment.length; equip++) {
			getItems().setEquipment(playerEquipment[equip], playerEquipmentN[equip], equip);
		}
		if (totalPlaytime() > 10) {
			modeTut = true;
		}
		if (this.amountDonated > 250 && !Constants.isStaffMember(this)) {
			this.playerRights = 8;
		}
		if (this.getAccount().getType().equals(Account.IRON_MAN_TYPE.alias()) && this.gameMode != 3) {
			this.gameMode = 3;
		}
		getItems().addSpecialBar(playerEquipment[playerWeapon]);
		Misc.println("[REGISTERED]: " + playerName + "");
		displayName = DisplayName.getDisplayName(playerName) == null ? null : DisplayName.getDisplayName(playerName);
		PlayerUpdating.updatePlayer(this, outStream);
		NpcUpdating.updateNPC(this, outStream);
		if (this.getQuarantine().isQuarantined())
			modeTut = true;
		if (!modeTut && !hasClaimedStarter && !this.getAccount().getType().equals(Account.IRON_MAN_TYPE.alias())) {
			PlayerUpdating.announce("<col=255>[ " + Misc.optimizeText(this.playerName)
					+ " ]</col> Has joined DemonRsps for the first time.");
			this.getPA().addStarter();
			this.hasClaimedStarter = true;
			getDH().sendDialogues(2018, 1);
			openModeInterface();
		}
		getPA().resetFollow();
		getPA().clearClanChat();
		Achievements.checkIfFinished(this);
		canChangeAppearance = true;
		if (autoRetaliate) {
			getPA().sendFrame36(172, 1);
		} else {
			getPA().sendFrame36(172, 0);
		}
		if (DoubleExperience.isDoubleExperience()) {
			this.sendMessage("<col=ff0033>Double Experience weekend is Currently Active!");
			this.sendMessage("<col=ff0033>All Donations are invested into the server!");
		}
		if (muted) {
			sendMessage("You have been muted, you may appeal this punishment on our website.");
		}
		if (isSocialSlaying()) {
			this.setSocialSlayerDisconnections(this.getSocialSlayerDisconnections() + 1);
			if (this.getSocialSlayerDisconnections() == 2) {
				SocialSlayerData.finalizeSlayer(this);
				sendMessage(
						"<col=255>You are no longer social slaying with your previous partner. Too many disconnections.</col>");
			} else {
				if (this.getLastSocialPlayer() != null) {
					getDH().sendOption3(
							"@red@Would you like to try and re-join your previous partner in social slayer?", "Yes",
							"No");
					this.dialogueAction = 503;
					this.nextChat = 0;
				}
			}
		}
		/*
		 * if (this.getRights() != 1 && this.getRights() != 2 &&
		 * this.getRights() != 3 && this.getRights() != 4 && this.getRights() !=
		 * 5) { getPA().sendFrame126(
		 * "<col=FFFFFF>Amount Donated: <col=00CC00> [" +
		 * this.getDonationAmount() + "$]", 39165); getPA().sendFrame126(
		 * "<col=FFFFFF>Donation Points: <col=00CC00> [" +
		 * this.getDonationPoints() + "$]", 39166); }
		 */

		/*
		 * getPA().sendFrame126("@gr2@ <col=FFFFFF><shad=255>Legacy", 39155);
		 * getPA().sendFrame126("@or1@Player Info:", 39161);
		 * getPA().sendFrame126("<col=FFFFFF>Rank : <col=00CC00> " +
		 * Constants.rank(this, this.playerRights), 39162);
		 * getPA().sendFrame126("<col=FFFFFF>Game mode: <col=00CC00> [" +
		 * Constants.gameMode(this, this.gameMode) + "]", 39163);
		 * getPA().sendFrame126("<col=FFFFFF>Prestige Level: <col=FF6600>" +
		 * Constants.getRank(this, this.prestigeTitle), 39164);
		 * getPA().sendFrame126("<col=FFFFFF>Amount Donated: <col=00CC00> [" +
		 * this.getDonationAmount() + "$]", 39165); getPA().sendFrame126(
		 * "<col=FFFFFF>Donation Points: <col=00CC00> [" +
		 * this.getDonationPoints() + "$]", 39166); getPA().sendFrame126(
		 * "<col=FFFFFF>Kills: <col=00CC00> [" + this.originalKillCount + "]",
		 * 39167); getPA().sendFrame126("<col=FFFFFF>Deaths: <col=00CC00> [" +
		 * this.originalDeathCount + "]", 39168); getPA().sendFrame126(
		 * "<col=FFFFFF>KDR: <col=00CC00> [" + PVPAssistant.displayRatio(this) +
		 * "]", 39169); getPA().sendFrame126(
		 * "<col=FFFFFF>Current Killstreak: <col=00CC00> [" + this.killStreak +
		 * "]", 39170); getPA().sendFrame126(
		 * "<col=FFFFFF>My highest Streak: <col=00CC00> [" +
		 * this.highestKillStreak + "]", 39171); getPA().sendFrame126(
		 * "<col=FFFFFF>Pk Points:<col=00CC00>  [" + this.pkPoints + "]",
		 * 39172); getPA().sendFrame126(
		 * "<col=FFFFFF>Slayer Points :<col=00CC00>  [" + this.slaypoints + "]",
		 * 39173); getPA().sendFrame126("<col=FFFFFF>Vote Points <col=00CC00> ["
		 * + votePoints + "]", 39174); getPA().sendFrame126(
		 * "<col=FFFFFF>Pest Control Points: <col=00CC00> [" + this.pcPoints +
		 * "]", 39175);
		 */
		getPA().loadAllQuests(this);

		// if not in clan -- join default
		// if not created -- make
		// if has joined clan -- rejoin
		// check if clan avail

		if (lastClanChat.length() < 1) {
			this.sendMessage(
					"<col=ff0033>We noticed you aren't in a clanchat, so we added you to the community clanchat!");
			lastClanChat = "demon";
		}
		if (lastClanChat != null && lastClanChat.length() > 0) {
			Clan clan = Server.clanManager.getClan(lastClanChat);
			if (clan != null) {
				clan.addMember(this);
			}
			this.isMuted = false;
			this.setMuteReport(getMuteReport());
			if (this.getMuteReport() != null && this.getMuteReport().exists() && this.getMuteReport().isActive()) {
				this.isMuted = true;
				this.sendMessage("You are muted therefore you cannot talk,");
				this.sendMessage(this.getMuteReport().getExpireMessage());
				return;
			}
			if (this.getMuteReport().exists()) {
				Player pl = PlayerUpdating.getPlayerByName(playerName);
				PunishmentManager.liftPunishment(pl.playerName, "mutes", this);
				this.sendMessage("Your punishment has been lifted");
			}
			Achievements.increase(this, AchievementType.ROPES, 1);

		}
		if (this.voteExperienceMultiplier > 0) {
			this.DoubleExperienceTimer();
		}
		if (this.dropRateIncreaser > 0) {
			this.DropRateTimer();
		}
		if (this.DoublePKP > 0) {
			this.DoublePKPTimer();
		}
		if (Constants.isStaffMember(this)) {
			PlayerUpdating.announce("Staff Member: " + Constants.rank(this, this.playerRights) + " <col=255>"
					+ Misc.formatPlayerName(playerName) + " </col></shad> Has just logged in! ");
		}
		if (this.securityEnabled && this.getBankPin().isLocked() && this.totalPlaytime() > 12000) {
			// this.getBankPin().open(1);
		}
		if (this.getBankPin().isLocked() && this.getBankPin().getPin().trim().length() > 0) {
			this.getBankPin().open(2);
			this.playerStun = true;
			this.aggressionTolerance.stop();
			this.isBanking = false;
		}
		if (poisonDamage > 0)
			Server.getTaskScheduler().schedule(new PoisonCombatTask(this));
		// this.sendMessage("<col=255>To find out the latest update please do
		// ::topic 2987 (S)");
		correctPlayerCoordinatesOnLogin();
		update();
	}

	private void correctPlayerCoordinatesOnLogin() {
		Server.getTaskScheduler().schedule(new ScheduledTask(3) {

			@Override
			public void execute() {
				if (inCyclopsRoom()) {
					getPA().movePlayer(2846, 3541, 2);
				}
				if (arenas()) {
					getPA().movePlayer(Constants.DUELING_RESPAWN_X + Misc.random(2),
							Constants.DUELING_RESPAWN_Y + Misc.random(3), 0);
				}
				stop();
			}

		}.attach(this));
	}

	public void returnTitle() {
		if (this.loyaltyTitle.length() < 1) {
			if (this.gameMode == 1) {
				this.loyaltyTitle = "Regular";
			}
			if (this.gameMode == 2) {
				this.loyaltyTitle = "The Legendary";
			}
			if (this.gameMode == 3) {
				this.loyaltyTitle = "Iron Man";
			}
		}
	}

	public void update() {
		PlayerUpdating.updatePlayer(this, outStream);
		NpcUpdating.updateNPC(this, outStream);
		flushOutStream();
	}

	public void logout() {
		this.getPA().checkSkillXP();
		// When logging out as an owner Mike, This will make it so your profile
		// or mine, wont update on highscores.
		if (!(playerRights == 5)) {
			new Thread(new Highscores(this)).start();
		}

		if (this.getPA().viewingOtherBank) {
			this.sendMessage("You are no longer viewing another players bank.");
			this.getPA().resetOtherBank();
			return;
		}
		if (System.currentTimeMillis() - logoutDelay > 10000) {
			CycleEventHandler.getSingleton().stopEvents(this);
			if (this.inCyclopsRoom()) {
				this.getPA().movePlayer(0, 0, 0);
			}
			if (this.inTrade) {
				tradeAndDuel.declineTrade();
			}
			if (this.clan != null) {
				this.clan.removeMember(this);
			}
			if (this.getPet().getNpc() != null && this.getPet().getPet() != null) {
				this.getPet().dispose();
			}
			if (!this.getLastCombatAction().elapsed(TimeUnit.SECONDS.toMillis(30))) {
				this.sendMessage("You recently engaged in a player versus player fight, please wait.");
				return;
			}
			if (this.attackable || this.inTask) {
				this.sendMessage("You're currently under an attack-timer..(Someone attempted to attack you)");
				this.sendMessage("Please enter your pin to remove this block.");
				return;
			}
			outStream.createFrame(109);
			properLogout = true;

		} else {
			sendMessage("You must wait a few seconds to logout after Trading, and in Combat.");
		}
	}

	public int getTotalExperience() {
		int experience = 0;
		for (int i = 0; i < playerXP.length; i++) {
			experience += playerXP[i];
		}
		return experience;
	}

	public int getTotalLevel() {
		int level = 0;
		for (int i = 0; i < playerLevel.length; i++) {
			level += playerLevel[i];
		}
		return level;
	}

	public int packetSize = 0, packetType = -1;

	public void instanceFloorReset() {
		if (zulrah != null) {
			if (!Boundary.isInBounds(this, Boundary.ZULRAH)) {
				InstancedAreaManager.getSingleton().disposeOf(zulrah.getInstance());
			}
		}
	}

	public void process() {
		if (clickObjectType > 0 && destinationReached())
			handleObjectAction();
		miscProcessing();
		combatProcessing();
		farming.farmingProcess();
		getQuarantine().process();
		NPCAggression.process(this);

	}

	private CannonCredentials cr = new CannonCredentials(this);

	public CannonCredentials getCC() {
		return cr;
	}

	public ByteBuffer getInStream() {
		return inStream;
	}

	public ByteBuffer getOutStream() {
		return outStream;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}

	public TradeLog getTradeLog() {
		return tradeLog;
	}

	public TradeAndDuel getTradeAndDuel() {
		return tradeAndDuel;
	}

	public CombatAssistant getCombat() {
		return combatAssistant;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}

	public Channel getSession() {
		return session;
	}

	public void queueMessage(Packet arg1) {
		synchronized (queuedPackets) {
			queuedPackets.add(arg1);
		}
	}

	/**
	 * The amount of packets processed this cycle
	 */
	private int packetsProcessed;

	/**
	 * Processes incoming packets for the player
	 */
	public void processQueuedPackets() {
		Packet p = null;
		while ((p = queuedPackets.poll()) != null) {
			inStream.currentOffset = 0;
			packetType = p.getOpcode();
			packetSize = p.getLength();
			inStream.buffer = p.getPayload().array();
			if (packetType > 0) {
				PacketHandler.processPacket(this, packetType, packetSize);
				packetsProcessed++;
			}
			timeOutCounter = 0;
			if (packetsProcessed >= Constants.MAX_PACKETS_PROCESS) {
				queuedPackets.clear();
				break;
			}
		}
	}

	/**
	 * Resets the amount of packets processed this cycle
	 */
	public void resetPacketsProcessed() {
		packetsProcessed = 0;
	}

	public void miscProcessing() {
		if (pTime < Integer.MAX_VALUE) {
			pTime++;
		}
		if (followId > 0) {
			getPA().followPlayer();
		} else if (followId2 > 0) {
			getPA().followNpc();
		}
	}

	public int countdown;
	public int combatCountdown = 10;

	public void combatProcessing() {

		getCombat().handlePrayerDrain();
		if (System.currentTimeMillis() - singleCombatDelay > 5300) {
			underAttackBy = 0;
		}
		if (System.currentTimeMillis() - singleCombatDelay >= 60000) {
			resetDamageReceived();
		}
		if (System.currentTimeMillis() - singleCombatDelay2 > 5300) {
			underAttackBy2 = 0;
		}
		if (isDead || playerLevel[3] <= 0) {
			if (!hasDied) {
				hasDied();
			}

		}
		if (skullTimer > 0) {
			skullTimer--;
			if (skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				getPA().requestUpdates();
			}
		}
		if (freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (World.PLAYERS.get(frozenBy) == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!goodDistance(getAbsX(), getAbsY(), World.PLAYERS.get(frozenBy).getAbsX(),
						World.PLAYERS.get(frozenBy).getAbsY(), 20)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}
		if (hitDelay > 0) {
			hitDelay--;
		}
		if (hitDelay == 1) {
			if (oldNpcIndex > 0) {
				getCombat().delayedHit(this, oldNpcIndex, new Item(this.playerEquipment[3], this.playerEquipmentN[3]));
			}
			if (oldPlayerIndex > 0) {
				getCombat().playerDelayedHit(this, oldPlayerIndex,
						new Item(this.playerEquipment[3], this.playerEquipmentN[3]));
			}
		}
		if (attackTimer > 0) {
			attackTimer--;
		}
		if (attackTimer > 0 && specEffect < 1) {
			lastUsedSpecial = false;
		}
		if (attackTimer > 0 && specEffect > 0 && !lastUsedSpecial) {
			attackTimer = 0;
			lastUsedSpecial = true; // try now msg me if it works or not
		}
		if (attackTimer == 1) {
			if (npcIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcIndex);
			}
			if (playerIndex > 0) {
				getCombat().attackPlayer(playerIndex);
			}
		} else if (attackTimer <= 0 && (npcIndex > 0 || playerIndex > 0)) {
			if (npcIndex > 0) {
				attackTimer = 0;
				getCombat().attackNpc(npcIndex);
			} else if (playerIndex > 0) {
				attackTimer = 0;
				getCombat().attackPlayer(playerIndex);
			}
		}

	}

	public void updateWalkEntities() {
		if (this.inWild()) {
			int modY = getAbsY() > 6400 ? getAbsY() - 6400 : getAbsY();
			wildLevel = (((modY - 3520) / 8) + 1);
			getPA().walkableInterface(197);
			if (this.isPvpInterfaceVisible()) {
				// getPA().sendFrame171(0, 29005);
				// getPA().sendFrame171(1, 29007);
				// getPA().sendFrame171(1, 29012);
				getPA().showOption(3, 0, "Attack");
				// wildLevel = (((modY - 3520) / 8) + 1);
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
				// getPA().sendFrame171(0, 29022);
				// getPA().sendFrame171(1, 29020);
				// getPA().sendFrame126(Integer.toString(wildLevel), 29010);
				// getPA().sendFrame126(Integer.toString((int)
				// getWildernessPotential()), 29009);
				PVPAssistant.updateStatistics(this);
			} else if (!this.inWild()) {
				// getPA().sendFrame171(1, 29005);
				// getPA().sendFrame171(1, 29007);
				// getPA().sendFrame171(0, 29012);

			} else {
				if (!this.inWild())
					getPA().showOption(3, 0, "null");
				// getPA().sendFrame171(1, 29005);
				// getPA().sendFrame171(1, 29007);
				// getPA().sendFrame171(1, 29012);
				// getPA().sendFrame171(0, 29020);
				// getPA().sendFrame171(1, 29022);
				// getPA().sendFrame126("", 29002);
				// getPA().sendFrame126("", 29003);
				// getPA().sendFrame126("", 29004);
				// getPA().sendFrame126("", 29014);
				// getPA().sendFrame126("", 29015);
				// getPA().sendFrame126("", 29010);
			}
			// getPA().walkableInterface(29000);
		} else if (inDuelArena()) {
			getPA().walkableInterface(201);
			if (duelStatus == 5) {
				getPA().showOption(3, 0, "Attack");
			} else {
				getPA().showOption(3, 0, "Challenge");
			}
		} else if (inPcBoat()) {
			getPA().walkableInterface(21119);
		} else if (inPcGame()) {
			getPA().walkableInterface(21100);
		} else if (inBarrows()) {
			mapData(false);
			getPA().walkableInterface(16128);
			getPA().sendFrame126("" + barrowsKillCount, 16137);
			if (barrowsNpcs[2][1] == 2) {
				getPA().sendFrame126("@red@Karils", 16135);
			}
			if (barrowsNpcs[3][1] == 2) {
				getPA().sendFrame126("@red@Guthans", 16134);
			}
			if (barrowsNpcs[1][1] == 2) {
				getPA().sendFrame126("@red@Torags", 16133);
			}
			if (barrowsNpcs[5][1] == 2) {
				getPA().sendFrame126("@red@Ahrims", 16132);
			}
			if (barrowsNpcs[0][1] == 2) {
				getPA().sendFrame126("@red@Veracs", 16131);
			}
			if (barrowsNpcs[4][1] == 2) {
				getPA().sendFrame126("@red@Dharoks", 16130);
			}
		} else {
			getPA().walkableInterface(-1);
			getPA().showOption(3, 0, "Null");
		}
		if (hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getPA().multiWay(-1);
		} else if (!hasMultiSign && inMulti()) {
			hasMultiSign = true;
			getPA().multiWay(1);
		}

	}

	public void handleObjectAction() {
		walkingToObject = false;
		turnPlayerTo(objectX, objectY);
		if (clickObjectType == 1)
			getActions().firstClickObject(objectId, objectX, objectY);
		else if (clickObjectType == 2)
			getActions().secondClickObject(objectId, objectX, objectY);
		else if (clickObjectType == 3)
			getActions().thirdClickObject(objectId, objectX, objectY);
		else if (clickObjectType == 4)
			UseItem.ItemonObject(this, objectId, objectX, objectY, itemUsedOn);
	}

	public int constitution;
	public int followId;
	public int followId2;
	public String teleportAction = "null";
	public long lastButton;

	/*
	 * Bank searching
	 */

	public int pollLineEdit;
	public int pollOption;
	public String pollTitle, pollOption1, pollOption2, pollOption3, pollOption4;
	public long lastPollSuggestion;
	public boolean muted;

	public static void sendStatement(Player c, String s) {
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}

	private SocialSlayer socialSlayer;

	public int totalPlaytime() {
		return (pTime);
	}

	public SocialSlayer getSocialSlayer() {
		return socialSlayer;
	}

	public SocialSlayer setSocialSlayer(Player c, Player partner) {
		return socialSlayer = new SocialSlayer(c, partner);
	}

	public SocialSlayer setSocialSlayerNull() {
		return this.socialSlayer = null;
	}

	public String getPlaytime() {
		int DAY = (totalPlaytime() / 144000);
		int HR = (totalPlaytime() / 6000) - (DAY * 24);
		int MIN = (totalPlaytime() / 100) - (DAY * 1440) - (HR * 60);
		return (DAY + " Day " + HR + " Hr " + MIN + " Min");
	}

	private Map<Integer, TinterfaceText> interfaceText = new HashMap<Integer, TinterfaceText>();

	public class TinterfaceText {
		public int id;
		public String currentState;

		public TinterfaceText(String s, int id) {
			this.currentState = s;
			this.id = id;
		}

	}

	public boolean checkPacket126Update(String text, int id) {
		if (id == 56306 || id == 59507 || id >= 51008 && id <= 51010) {
			return true;
		}
		if (!interfaceText.containsKey(id)) {
			interfaceText.put(id, new TinterfaceText(text, id));
		} else {

			TinterfaceText t = interfaceText.get(id);
			if (text.equals(t.currentState)) {
				return false;
			}
			t.currentState = text;
		}
		return true;
	}

	private PlayerKillstreak killstreak = new PlayerKillstreak(this);

	public PlayerKillstreak getKillstreak() {
		return this.killstreak;
	}

	public PlayerKillstreak setPlayerKillstreak(PlayerKillstreak pks) {
		return this.killstreak = pks;
	}

	public void agilityDelay(final int Emote, final int X, final int Y, final int H, final int Req, final int amtEXP,
			final String message) {
		if (this.playerLevel[16] >= Req) {
			this.startAnimation(Emote);
			this.agilityEmote = true;
			this.getPA().addSkillXP(amtEXP, Player.playerAgility);
			Server.getTaskScheduler().schedule(new ScheduledTask(2) {
				@Override
				public void execute() {
					getPA().movePlayer(X, Y, H);
					Player.this.agilityEmote = false;
					super.stop();
				}
			}.attach(this));
		} else {
			this.sendMessage("You Need " + Req + " Agility To Do This Obsticle");
		}
	}

	public Farming getFarming() {
		return farming;
	}

	public int getFarmingSeedId(int index) {
		return farmingSeedId[index];
	}

	public void setFarmingSeedId(int index, int farmingSeedId) {
		this.farmingSeedId[index] = farmingSeedId;
	}

	public int getFarmingTime(int index) {
		return this.farmingTime[index];
	}

	public void setFarmingTime(int index, int farmingTime) {
		this.farmingTime[index] = farmingTime;
	}

	public int getFarmingState(int index) {
		return farmingState[index];
	}

	public void setFarmingState(int index, int farmingState) {
		this.farmingState[index] = farmingState;
	}

	public int getFarmingHarvest(int index) {
		return farmingHarvest[index];
	}

	public void setFarmingHarvest(int index, int farmingHarvest) {
		this.farmingHarvest[index] = farmingHarvest;
	}

	public boolean isMaxed() {
		int skill = 0;
		for (int i = 0; i < 22; i++) {
			if (this.getAchievements().hasCompletedAll() && this.gameMode >= 2
					&& getLevelForXP(this.playerXP[i]) == 99) {
				skill++;
			}
			if (this.prestigeLevel > 0 && this.getAchievements().hasCompletedAll() && this.gameMode >= 0) {
				skill = 22;
			}
		}
		return (skill == 22);
	}

	public int getChunckX() {
		return (absX >> 6);
	}

	public int getChunckY() {
		return (absY >> 6);
	}

	public int getRegionId() {
		return ((getChunckX() << 8) + getChunckY());
	}

	/*
	 * public MusicManager getMusicManager() { return musicManager; }
	 */

	private int[] farmingSeedId = new int[7];
	private int[] farmingTime = new int[7];
	private int[] farmingState = new int[7];
	private int[] farmingHarvest = new int[7];
	private Farming farming = new Farming(this);
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private TradeAndDuel tradeAndDuel = new TradeAndDuel(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private TradeLog tradeLog = new TradeLog(this);
	public long buttonDelay;
	public int openInterface = -1;
	public boolean hasBoughtCannon;
	public boolean canWearCape;
	public long dropRateIncreaser;
	public int playerX, playerY, playerZ;
	public boolean securityEnabled = true;
	public long spinDelay;
	public int regionId;
	// public final MusicManager musicManager = new MusicManager(this);
	public boolean lastUsedSpecial;
	public boolean hasDisabledEpDrops;

	public boolean cantWear;

	public boolean isSmelting;

	public int count;

	public boolean canRestoreSpecial;

	public int specRestore = 0;

	public int herbloreLevelReq;

	public void addDamageReceived(String player, int damage) {
		if (damage <= 0) {
			return;
		}
		CombatDamage combatDamage = new CombatDamage(damage);
		if (damageReceived.containsKey(player)) {
			damageReceived.get(player).add(new CombatDamage(damage));
		} else {
			damageReceived.put(player, new ArrayList<CombatDamage>(Arrays.asList(combatDamage)));
		}
	}

	public void updateLastCombatAction() {
		lastCombatAction.reset();
	}

	public Stopwatch getLastCombatAction() {
		return lastCombatAction;
	}

	public void resetDamageReceived() {
		damageReceived.clear();
	}

	public String getPlayerKiller() {
		String killer = null;
		int totalDamage = 0;
		for (Entry<String, ArrayList<CombatDamage>> entry : damageReceived.entrySet()) {
			String player = entry.getKey();
			ArrayList<CombatDamage> damageList = entry.getValue();
			int damage = 0;
			for (CombatDamage cd : damageList) {
				if (System.currentTimeMillis() - cd.getTimeInMillis() < 90000) {
					damage += cd.getDamage();
				}
			}
			if (totalDamage == 0 || damage > totalDamage || killer == null) {
				totalDamage = damage;
				killer = player;
			}
		}
		return killer;
	}

	public QuickPrayer getQuick() {
		return quick;
	}

	public DialogueManager dialogue() {
		return dialogue;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.PLAYER;
	}

	public ByteBuffer getUpdateBlock() {
		return updateBlock;
	}

	public void setUpdateBlock(ByteBuffer updateBlock) {
		this.updateBlock = updateBlock;
	}

	public boolean isMask100update() {
		return mask100update;
	}

	public void setMask100update(boolean mask100update) {
		this.mask100update = mask100update;
	}

	public boolean isFaceUpdateRequired() {
		return faceUpdateRequired;
	}

	public void setFaceUpdateRequired(boolean faceUpdateRequired) {
		this.faceUpdateRequired = faceUpdateRequired;
	}

	public ByteBuffer getPlayerProps() {
		return playerProps;
	}

	public int donationPoints;
	public double donationAmount;

	public int getDonationPoints;
	public int shopIndex;

	/**
	 *
	 * @return donationPoints on file
	 */
	public int getDonationPoints() {
		return this.donationPoints;
	}

	/**
	 * sets the donationpoint amount using i variable
	 * 
	 * @param i
	 */
	public void setDonationPoints(int i) {
		this.donationPoints = i;
	}

	public double getDonationAmount() {
		return amountDonated;
	}

	public int getRights() {
		return this.playerRights;
	}

	public void setDonationAmount(double donationAmount) {
		this.donationAmount = donationAmount;
	}

	public static void setRights(int i) {
		// TODO Auto-generated method stub

	}

	public String agilityPoints() {
		// TODO Auto-generated method stub
		return null;
	}

	private final BountyHunter bounty_hunter = new BountyHunter(this);

	public boolean shopping;

	public BountyHunter getBountyHunter() {
		return bounty_hunter;
	}

	private CityTeleport cityTeleport;

	public CityTeleport getCityTeleport() {
		return cityTeleport;
	}

	public int damagedealt;

	private Hybrid hybrid = new Hybrid(this);
	private Melee melee = new Melee(this);
	private Range range = new Range(this);
	private Magic magic = new Magic(this);

	public boolean usingToxicBlowpipe;

	public int petID;

	public Object playerNpcId;

	public boolean isNpc;

	public boolean gdegradeNow = false;

	public int donationPointAmount;

	public Hybrid getHybrid() {
		return hybrid;
	}

	/**
	 * Timer getters/setters
	 */
	public SystemTimer getSqlTimer() {
		return sqlTimer;
	}

	/**
	 * End Timers getters/setters
	 */
	public Melee getMelee() {
		return melee;
	}

	public Range getRange() {
		return range;
	}

	public Magic getMagic() {
		return magic;
	}

	public int getColorSelect() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void resetPlayerNpc() {
		// TODO Auto-generated method stub

	}

	public Object getMarket() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isExtremeDonator() {
		return (isExtremeDonator || (donatorRights >= 2 && playerRights >= 7));
	}

	private final String[] randomMessages = { "pie", "kebab", "chocolate", "bagel", "triangle", "square", "bread" };

	private final int[][] responseButtons = { { 63013, 0 }, { 63014, 1 }, { 63015, 2 }, { 63009, 3 }, { 63010, 4 },
			{ 63011, 5 }, { 63012, 6 } };

	public void executeRandom() {
		getPA().sendFrame126("", 16131);
		getPA().showInterface(16135);
		foodType = Misc.random(6);
		getPA().sendFrame126("Please select the " + randomMessages[foodType] + " for a cash reward!", 16145);
	}

	public void checkResponse(int button) {
		for (int i = 0; i < responseButtons.length; i++) {
			if (responseButtons[i][0] == button) {
				if (responseButtons[i][1] == foodType) {
					sendMessage("Congratulations! You've completed the random event.");
					getPA().closeAllWindows();
					getItems().addItem(995, Misc.random(250000));
					return;
				}

				/**
				 * You can add other things here, such as a teleport or
				 * something. This is where it fails and they click the wrong
				 * food type.
				 */
				sendMessage("Please select the " + randomMessages[foodType] + " to continue");
			}
		}
	}

	public int foodType = -1;
	public long lastRandom;

	public int[] fishingProp;

	public boolean offMessages;

	public int chestDelay;

	public int tempIdHolder;

	public boolean isUsingBag;

	public int getGameMode() {
		return gameMode;
	}

	public ModeData getMode() {
		return mode;
	}

	public void setMode(ModeData mode) {
		this.mode = mode;
	}

	public int getId() {
		return playerId;
	}

}