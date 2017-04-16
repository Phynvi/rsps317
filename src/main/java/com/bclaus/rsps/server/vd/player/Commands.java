 package com.bclaus.rsps.server.vd.player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.util.DisplayName;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueDifficulty;
import com.bclaus.rsps.server.vd.content.punishment.PunishmentManager;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.items.ItemList;
import com.motivoters.motivote.service.MotivoteRS;

import com.bclaus.rsps.server.Connection;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.content.OnlineList;
import com.bclaus.rsps.server.vd.content.ShopHandler;
import com.bclaus.rsps.server.vd.content.achievements.AchievementType;
import com.bclaus.rsps.server.vd.content.achievements.Achievements;
import com.bclaus.rsps.server.vd.content.minigames.Barrows;
import com.bclaus.rsps.server.vd.content.support.Ticket;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.account_type.Account;
import com.bclaus.rsps.server.vd.player.account_type.AccountType;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.vd.player.packets.impl.InputField;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.quarantine.PlayerQuarantine;
import com.bclaus.rsps.server.quarantine.QuarantineIO;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

/**
 * Commands
 * 
 */
public class Commands implements PacketType {
	private final static MotivoteRS motivote = new MotivoteRS("DemonRsps", "50cb867ac2ede59464779190bf0638c7");

	private static boolean eventStarted;

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		String playerCommand = c.getInStream().readString();
		int spaceIndex = playerCommand.indexOf(" ");
		int minusIndex = playerCommand.indexOf("-");
		if (spaceIndex > minusIndex)
			playerCommand = playerCommand.substring(0, playerCommand.indexOf(" ")).toLowerCase() + playerCommand.substring(playerCommand.indexOf(" "));
		else if (minusIndex > spaceIndex) //Cuz of Marq's seperators ...
 			playerCommand = playerCommand.substring(0, playerCommand.indexOf("-")).toLowerCase() + playerCommand.substring(playerCommand.indexOf("-"));
		else
			playerCommand = playerCommand.toLowerCase();
		
		System.out.println(c.playerName +" has done command : "+playerCommand);
		if (c.playerRights >= 0) {
			playerCommands(c, playerCommand);
		}
		if (Constants.donatorRights(c) || c.playerRights == 8 || c.playerRights == 10) {
			donatorCommands(c, playerCommand);
		}
		if (Constants.premiumRights(c)|| c.playerRights == 10) {
			premiumCommands(c, playerCommand);
		}
		if (c.playerRights == 11)
			vipCommands(c, playerCommand);
		
		if (Constants.respectedRights(c))
			respectedCommands(c, playerCommand);
	
			
		if (c.playerRights == 2 || c.playerRights == 3 || c.playerRights == 4 || c.playerRights == 5) {
			moderatorCommands(c, playerCommand);
		}
		if (c.playerRights == 5 || c.playerRights == 3 || c.playerRights == 4 || c.playerName.equalsIgnoreCase("demon")) {
			administratorCommands(c, playerCommand);
		}
		if (c.playerRights == 5 || c.playerRights == 4 || c.playerName.equalsIgnoreCase("demon")) {
			managerCommands(c, playerCommand);
		}
		if (c.isSuperAdministrator() || c.playerRights == 4 || c.playerRights == 5) {
			ownerCommands(c, playerCommand);
		}
		if (c.playerRights == 1 || c.playerRights == 2 || c.playerRights == 3 || c.playerRights == 4 || c.playerRights == 5) {
			helperCommands(c, playerCommand);
		}

	}
	private static void staff(Player c, String playerCommand) {
		c.getOnlineStaff();
		c.getPA().showInterface(8134);
		c.flushOutStream();
		c.getPA().sendFrame126("", 8147);
		c.getPA().sendFrame126("", 8148);
		c.getPA().sendFrame126("", 8149);
		c.getPA().sendFrame126("", 8150);
		c.getPA().sendFrame126("", 8151);
		c.getPA().sendFrame126("", 8152);
		c.getPA().sendFrame126("", 8153);
		c.getPA().sendFrame126("", 8154);
		c.getPA().sendFrame126("", 8155);
		c.getPA().sendFrame126("", 8156);
		c.getPA().sendFrame126("", 8157);
		c.getPA().sendFrame126("", 8158);
		c.getPA().sendFrame126("", 8159);
		c.getPA().sendFrame126("", 8160);
		c.getPA().sendFrame126("", 8161);
		c.getPA().sendFrame126("", 8162);
		c.getPA().sendFrame126("", 8163);
		c.getPA().sendFrame126("", 8164);
		c.getPA().sendFrame126("", 8165);
		c.getPA().sendFrame126("", 8166);
		c.getPA().sendFrame126("", 8167);
		c.getPA().sendFrame126("", 8168);
		c.getPA().sendFrame126("", 8169);
		c.getPA().sendFrame126("", 8170);
		c.getPA().sendFrame126("", 8171);
		c.getPA().sendFrame126("", 8172);
		c.getPA().sendFrame126("", 8173);
		c.getPA().sendFrame126("", 8174);
		c.getPA().sendFrame126("", 8175);
		c.getPA().sendFrame126("", 8176);
		c.getPA().sendFrame126("", 8177);
		c.getPA().sendFrame126("", 8178);
		c.getPA().sendFrame126("", 8179);
		c.getPA().sendFrame126("", 8180);
		c.getPA().sendFrame126("", 8181);
		c.getPA().sendFrame126("", 8182);
		c.getPA().sendFrame126("", 8183);
		c.getPA().sendFrame126("", 8184);
		c.getPA().sendFrame126("", 8185);
		c.getPA().sendFrame126("", 8186);
		c.getPA().sendFrame126("", 8187);
		c.getPA().sendFrame126("", 8188);
		c.getPA().sendFrame126("", 8189);
		c.getPA().sendFrame126("", 8190);
		c.getPA().sendFrame126("", 8191);
		c.getPA().sendFrame126("", 8192);
		c.getPA().sendFrame126("", 8193);
		c.getPA().sendFrame126("", 8194);
		c.getPA().sendFrame126("", 8195);
		c.getPA().sendFrame126("", 12174);
		c.getPA().sendFrame126("", 12175);
		c.getPA().sendFrame126("", 12176);
		c.getPA().sendFrame126("", 12177);
		c.getPA().sendFrame126("", 12178);
		c.getPA().sendFrame126("", 12179);
		c.getPA().sendFrame126("", 12180);
		c.getPA().sendFrame126("", 12181);
		c.getPA().sendFrame126("", 12182);
		c.getPA().sendFrame126("", 12183);
		c.getPA().sendFrame126("", 12184);
		c.getPA().sendFrame126("", 12185);
		c.getPA().sendFrame126("", 12186);
		c.getPA().sendFrame126("", 12187);
		c.getPA().sendFrame126("", 12188);
		c.getPA().sendFrame126("", 12189);
		c.getPA().sendFrame126("", 12190);
		c.getPA().sendFrame126("", 12191);
		c.getPA().sendFrame126("", 12192);
		c.getPA().sendFrame126("", 12193);
		c.getPA().sendFrame126("", 12194);
		c.getPA().sendFrame126("", 12195);
		c.getPA().sendFrame126("", 12196);
		c.getPA().sendFrame126("", 12197);
		c.getPA().sendFrame126("", 12198);
		c.getPA().sendFrame126("", 12199);
		c.getPA().sendFrame126("", 12200);
		c.getPA().sendFrame126("", 12201);
		c.getPA().sendFrame126("", 12202);
		c.getPA().sendFrame126("", 12203);
		c.getPA().sendFrame126("", 12204);
		c.getPA().sendFrame126("", 12205);
		c.getPA().sendFrame126("", 12206);
		c.getPA().sendFrame126("", 12207);
		c.getPA().sendFrame126("", 12208);
		c.getPA().sendFrame126("", 12209);
		c.getPA().sendFrame126("", 12210);
		c.getPA().sendFrame126("", 12211);
		c.getPA().sendFrame126("", 12212);
		c.getPA().sendFrame126("", 12213);
		c.getPA().sendFrame126("", 12214);
		c.getPA().sendFrame126("", 12215);
		c.getPA().sendFrame126("", 12216);
		c.getPA().sendFrame126("", 12217);
		c.getPA().sendFrame126("", 12218);
		c.getPA().sendFrame126("", 12219);
		c.getPA().sendFrame126("", 12220);
		c.getPA().sendFrame126("", 12221);
		c.getPA().sendFrame126("", 12222);
		c.getPA().sendFrame126("", 12223);
		c.getPA().sendFrame126("@or1@Online helpers Staff Membe online!", 8144);
		c.getPA().sendFrame126("", 8145);
		boolean online = false;
		for (int j = 0; j < World.PLAYERS.capacity(); j++) {
			if (World.PLAYERS.get(j) != null) {
				Player c2 = (Player) World.PLAYERS.get(j);

				if (c2.playerRights == 1) {
					online = true;
					c.getPA().sendFrame126("@bla@_ Online Helper _:", 8147);
					c.getPA().sendFrame126("@gre@ " + c.onlineHelper, 8148);

				} else if (c2.playerRights == 2) {
					online = true;
					c.getPA().sendFrame126("@bla@_ Online Moderators_:", 8150);
					c.getPA().sendFrame126("@gre@ " + c.onlinetmod, 8151);

				} else if (c2.playerRights == 3) {
					online = true;
					c.getPA().sendFrame126("@bla@_ Online administrator:", 8157);
					c.getPA().sendFrame126("@gre@ " + c.onlineAdmins, 8158);

				} else if (c2.playerRights == 4) {
					online = true;
					c.getPA().sendFrame126("@bla@_ Staff Manager:", 8161);
					c.getPA().sendFrame126("@gre@ " + c.onlinestaffmanger, 8162);

				}
			}

		}
		if (!online)
			c.getPA().sendFrame126("Unfortunately, we couldn't find any staff Memeber online.", 8150);
	}
	private void respectedCommands(Player c, String playerCommand) {
		// TODO Auto-generated method stub
		
	}



	/*
	 * [END] Checks the player's rank. [END]
	 */
	private static void playerCommands(final Player c, String playerCommand) {
		
		if (playerCommand.startsWith("claim")) {
		c.rspsdata(c, c.playerName);
		//	Store.claimPayment(c, c.playerName);
		}
		
		/*if (playerCommand.startsWith("staff")) {
			staff(c, playerCommand);
		}*/
		if (playerCommand.startsWith("redeem")) {
			String auth = playerCommand.replace("redeem ", "");
			
			try {
				boolean success = motivote.redeemVote(auth);
				
				if (success) {
					c.getItems().addItem(995, 10000);
					c.getItems().addItem(6199, 1);
					c.votePoints += 1;
					c.sendMessage("Auth redeemed, thanks for voting!");
				}
				else {
					c.sendMessage("Invalid auth supplied, please try again later.");
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
				c.sendMessage("Unable to check auth, please try again later.");
			}
		}
		if (playerCommand.equalsIgnoreCase("Shops") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(3659, 3522, 0));
		}
		/*if (playerCommand.equalsIgnoreCase("bandos") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(2871,5360, 6));
		}
		if (playerCommand.equalsIgnoreCase("Armadyl") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(2831, 5299, 6));
		}
		if (playerCommand.equalsIgnoreCase("Zamorak") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(2930, 5325, 6));
		}
		if (playerCommand.equalsIgnoreCase("Saradomin") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(2894, 5266, 0));
		}*/
		if (playerCommand.startsWith("[if]")) {
			playerCommand = playerCommand.substring(4, playerCommand.length());
			InputField.execute(c, Integer.parseInt(playerCommand.split("-")[0]), playerCommand.split("-")[1]);
		}
		if (playerCommand.startsWith("setlevel")) {
			if (c.votePoints < 2) {
				c.sendMessage("You need at least 2 vote points to use this command.");
				return;
			}
			try {
				String[] args = playerCommand.split(" ");
				int skill = Integer.parseInt(args[1]);
				int level = Integer.parseInt(args[2]) + 1;
				if (skill == 5 || skill > 6) {
					c.sendMessage("You cannot level this skill.");
				} else if (skill == 3 && level < 10) {
					c.sendMessage("You cannot level your hitpoints under 10.");
				} else {
					c.playerLevel[skill] = level;
					c.playerXP[skill] = c.getPA().getXPForLevel(level);
					c.getPA().refreshSkill(skill);
					c.votePoints -= 2;
				}
			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("::setlevel skill level.");
			}

		}		
		if (playerCommand.equalsIgnoreCase("Shops") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(3659, 3522, 0));
		}

		if (playerCommand.equalsIgnoreCase("lighthouse") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(2509, 3641, 0));//
		}
		if (playerCommand.equalsIgnoreCase("draynor") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(3085, 3248, 0));//
		}
		if (playerCommand.equalsIgnoreCase("yanille") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(2605, 3093, 0));//
		}
		if (playerCommand.equalsIgnoreCase("rimmington") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(2957, 3214, 0));//
		}
		if (playerCommand.startsWith("empty")) {
			c.sendMessage("You empty your inventory");
			for (int i = 0; i < c.playerItems.length; i++) {
				c.getItems().deleteItem(c.playerItems[i] - 1, c.getItems().getItemSlot(c.playerItems[i] - 1),
						c.playerItemsN[i]);
			}
		}
		if (playerCommand.startsWith("empty")) {
			c.sendMessage("You empty your inventory");
			for (int i = 0; i < c.playerItems.length; i++) {
			c.getItems().deleteItem(c.playerItems[i] - 1, c.getItems().getItemSlot(c.playerItems[i] - 1), c.playerItemsN[i]);
		}
	}

	
		if (playerCommand.startsWith("clan")) {
			if (c.isMuted) {
				c.sendMessage("Sorry, your account is still muted, please appeal on our forums");
				return;
			}
			if (c.clan.getFounder().equalsIgnoreCase("demon") && c.isClanMuted) {
				c.sendMessage("Sorry, your account is muted in this clanchat");
				return;
			}
			if (c.clan != null) {
				String message = playerCommand.substring(5);
				c.clan.sendChat(c, message);
				c.lastAction = System.currentTimeMillis();
				return;
			}
		}
		if(playerCommand.equalsIgnoreCase("fixclue")) {
			c.resetClueStatus(c);
			c.sendMessage("Please only use the reset feature if you believe your account to be nulled");
			c.sendMessage("This means that your account is bugged on a clue-status, so you cannot gather more clues.");
			c.sendMessage("The problem should be fixed, but some people are still affected by the bug");
		}
		if (playerCommand.equalsIgnoreCase("noclip") && !c.playerName.equalsIgnoreCase("demon")) {
			c.disconnected = true;
		}
		
		if (playerCommand.equalsIgnoreCase("checktimers")) {
			if (c.DoublePKP < 1) {
				c.sendMessage("You do not have Double PKP Enabled");
			}
			if (c.voteExperienceMultiplier < 1) {
				c.sendMessage("You do not have Double Experience Enabled");
			}
			if (c.dropRateIncreaser < 1) {
				c.sendMessage("You do not have increased drop-rate Enabled");
			}
			c.sendMessage("Double PKP :" + c.DoublePKP + " Double EXP : " + c.voteExperienceMultiplier + " Drop Rate Increaser: " + c.dropRateIncreaser);
		}
		if (playerCommand.equalsIgnoreCase("help")) {
			c.sendMessage("Please use ::ticket instead");
			Achievements.increase(c, AchievementType.HELP, 1);
		}
		if (playerCommand.equalsIgnoreCase("skull")) {
			if (c.skullTimer > 0) {
				c.sendMessage("You're already skulled");
				return;
			} else {
				c.isSkulled = true;
				c.skullTimer = Constants.SKULL_TIMER;
				c.headIconPk = 0;
				c.getPA().requestUpdates();
			}
		}
		if (playerCommand.equalsIgnoreCase("terms")) {
			c.sendMessage("Thank you for visiting our terms page");
			c.getPA().sendFrame126("DemonRsps.com" + 96, 12000);
			Achievements.increase(c, AchievementType.SITE, 1);
		}
		/*if (playerCommand.equalsIgnoreCase("staff")) {
			c.getOnlineStaff();
			c.getPA().showInterface(8134);
			c.flushOutStream();
			c.getPA().sendFrame126("", 8147);
			c.getPA().sendFrame126("", 8148);
			c.getPA().sendFrame126("", 8149);
			c.getPA().sendFrame126("", 8150);
			c.getPA().sendFrame126("", 8151);
			c.getPA().sendFrame126("", 8152);
			c.getPA().sendFrame126("", 8153);
			c.getPA().sendFrame126("", 8154);
			c.getPA().sendFrame126("", 8155);
			c.getPA().sendFrame126("", 8156);
			c.getPA().sendFrame126("", 8157);
			c.getPA().sendFrame126("", 8158);
			c.getPA().sendFrame126("", 8159);
			c.getPA().sendFrame126("", 8160);
			c.getPA().sendFrame126("", 8161);
			c.getPA().sendFrame126("", 8162);
			c.getPA().sendFrame126("", 8163);
			c.getPA().sendFrame126("", 8164);
			c.getPA().sendFrame126("", 8165);
			c.getPA().sendFrame126("", 8166);
			c.getPA().sendFrame126("", 8167);			
			c.getPA().sendFrame126("", 8168);
			c.getPA().sendFrame126("", 8169);
			c.getPA().sendFrame126("", 8170);
			c.getPA().sendFrame126("", 8171);
			c.getPA().sendFrame126("", 8172);
			c.getPA().sendFrame126("", 8173);
			c.getPA().sendFrame126("", 8174);
			c.getPA().sendFrame126("", 8175);
			c.getPA().sendFrame126("", 8176);
			c.getPA().sendFrame126("", 8177);
			c.getPA().sendFrame126("", 8178);
			c.getPA().sendFrame126("", 8179);
			c.getPA().sendFrame126("", 8180);
			c.getPA().sendFrame126("", 8181);
			c.getPA().sendFrame126("", 8182);
			c.getPA().sendFrame126("", 8183);
			c.getPA().sendFrame126("", 8184);
			c.getPA().sendFrame126("", 8185);
			c.getPA().sendFrame126("", 8186);
			c.getPA().sendFrame126("", 8187);			
			c.getPA().sendFrame126("", 8188);
			c.getPA().sendFrame126("", 8189);
			c.getPA().sendFrame126("", 8190);
			c.getPA().sendFrame126("", 8191);
			c.getPA().sendFrame126("", 8192);
			c.getPA().sendFrame126("", 8193);
			c.getPA().sendFrame126("", 8194);
			c.getPA().sendFrame126("", 8195);			
			c.getPA().sendFrame126("", 12174);
			c.getPA().sendFrame126("", 12175);
			c.getPA().sendFrame126("", 12176);
			c.getPA().sendFrame126("", 12177);
			c.getPA().sendFrame126("", 12178);
			c.getPA().sendFrame126("", 12179);
			c.getPA().sendFrame126("", 12180);
			c.getPA().sendFrame126("", 12181);
			c.getPA().sendFrame126("", 12182);
			c.getPA().sendFrame126("", 12183);
			c.getPA().sendFrame126("", 12184);
			c.getPA().sendFrame126("", 12185);
			c.getPA().sendFrame126("", 12186);
			c.getPA().sendFrame126("", 12187);
			c.getPA().sendFrame126("", 12188);
			c.getPA().sendFrame126("", 12189);
			c.getPA().sendFrame126("", 12190);
			c.getPA().sendFrame126("", 12191);
			c.getPA().sendFrame126("", 12192);
			c.getPA().sendFrame126("", 12193);
			c.getPA().sendFrame126("", 12194);
			c.getPA().sendFrame126("", 12195);
			c.getPA().sendFrame126("", 12196);
			c.getPA().sendFrame126("", 12197);
			c.getPA().sendFrame126("", 12198);
			c.getPA().sendFrame126("", 12199);
			c.getPA().sendFrame126("", 12200);
			c.getPA().sendFrame126("", 12201);
			c.getPA().sendFrame126("", 12202);
			c.getPA().sendFrame126("", 12203);
			c.getPA().sendFrame126("", 12204);
			c.getPA().sendFrame126("", 12205);
			c.getPA().sendFrame126("", 12206);
			c.getPA().sendFrame126("", 12207);
			c.getPA().sendFrame126("", 12208);
			c.getPA().sendFrame126("", 12209);
			c.getPA().sendFrame126("", 12210);
			c.getPA().sendFrame126("", 12211);
			c.getPA().sendFrame126("", 12212);
			c.getPA().sendFrame126("", 12213);
			c.getPA().sendFrame126("", 12214);
			c.getPA().sendFrame126("", 12215);
			c.getPA().sendFrame126("", 12216);
			c.getPA().sendFrame126("", 12217);
			c.getPA().sendFrame126("", 12218);
			c.getPA().sendFrame126("", 12219);
			c.getPA().sendFrame126("", 12220);
			c.getPA().sendFrame126("", 12221);
			c.getPA().sendFrame126("", 12222);
			c.getPA().sendFrame126("", 12223);
			c.getPA().sendFrame126("there are currently staff Member helper online|:@gre@", 8144);
			c.getPA().sendFrame126("", 8145);
			boolean online = false;
			for(int i = 0; i < World.PLAYERS.capacity(); i++) {
				if(World.PLAYERS.get(i) != null) {
					Player c2 = (Player)World.PLAYERS.get(i);
			 if (c2.playerRights == 9) {
				 online = true;
		    c.getPA().sendFrame126("@bla@_ online Staff Manger _:", 8160);
			c.getPA().sendFrame126("@cr8@@gre@"+c.onlinestaffmanger+"", 8161);
			
			 } else if (c2.playerRights == 5) {
					online = true;
				c.getPA().sendFrame126("@bla@_ Online Owners _:", 8165);
				c.getPA().sendFrame126("@cr2@@gre@"+c.onlineOwners+"", 8166);
				
			} else if (c2.playerRights == 3) {
				online = true;
			c.getPA().sendFrame126("@bla@_ Online Administrator _:", 8156);
			c.getPA().sendFrame126("@cr2@@gre@"+c.onlineAdmins+"", 8157);

			
			
			
			} else  if (c2.playerRights == 2) {
				online = true;
			c.getPA().sendFrame126("@bla@_ Online Online Moderators _:", 8152);
			c.getPA().sendFrame126("@cr7@@gre@"+c.onlineMods+"", 8153);
				
			

			} else if (c2.playerRights == 1) {
				online = true;
			c.getPA().sendFrame126("@bla@_Online Helper_:", 8147);
			c.getPA().sendFrame126("@cr6@@gre@"+c.onlineHelper+"", 8148);
		   
								}

							}
			}
			
		 if (!online)
				c.getPA().sendFrame126("@bla@No Staff member or Helper online:", 8150);
		}*/
		if (playerCommand.startsWith("checkreason")) {
			try {
				String[] args = playerCommand.split(" ", 2);
				File reason = new File("./Data/Reasons/" + args[1] + ".txt");
				if (!Connection.isNamedBanned(args[1])) {
					new File("./Data/Reasons/" + args[1] + ".txt").delete();
					c.sendMessage("Reason not found - Player is probably not banned...");
					return;
				}

				if (!reason.exists()) {
					System.out.println(String.format("Checking for file: %s", reason.getAbsolutePath()));
					c.sendMessage("Reason not found - Player is probably not banned...");
					return;

				}
				File fileToRead = new File("./Data/Reasons/" + args[1] + ".txt");
				BufferedReader reader = new BufferedReader(new FileReader(fileToRead));
				String line = reader.readLine();
				String[] split = line.split(":");
				c.sendMessage(String.format("%s has banned %s for: %s", split[0], args[1], split[1]));
			} catch (IOException e) {
				e.printStackTrace();
				c.sendMessage("Unable to fetch data: " + e.getMessage());
			}

		}
		if (playerCommand.equalsIgnoreCase("dp")) {
			c.sendMessage("I have donator Points amount of "+ c.donationPoints);
			
		}
		if (playerCommand.equalsIgnoreCase("commands")) {
			c.sendMessage("/------------------------------------------------------------/");
			c.sendMessage("(<col=ff0033> DemonRsps Commands</col>");
			c.sendMessage("::players , ::playtime, ::zulrah, ::ticket-reason, ::stuck, ::vote, ");
			c.sendMessage("::wiki, ::website, ::changepassword, ::yell, ::skull, ::topic ::thread");
			c.sendMessage("::lighthouse, ::hs (highscores), ::hitsleft to check Tent whip");
			c.sendMessage("::setlevel , ::claim");
			c.sendMessage("::staff ,::droplist,::prices,::clueguide");
			c.sendMessage("::fixed, ::dp, ::resize , ::chest , ::full ::chat ::tab ::optab");
			c.sendMessage("/------------------------------------------------------------/");
		}
		if (playerCommand.equalsIgnoreCase("players")) {
			OnlineList.openInterface(c);
		}
		if (playerCommand.equals("hitsleft"))
			   c.sendMessage("Hits left: " + c.tentacleHits);
			
		if (playerCommand.equalsIgnoreCase("playtime")) {
			//c.sendMessage("<col=ff0033>I have played DemonRsps for " + c.getPlaytime());
			c.forcedChat("I have played DemonRsps for " + c.getPlaytime());
		}
		if (playerCommand.equalsIgnoreCase("stuck") && !c.inDuelArena()) {
			if (eventStarted) {
				return;
			}
			eventStarted = true;
			PlayerUpdating.sendMessageStaff(Misc.formatPlayerName(c.playerName) + " Has just used ::Stuck");
			PlayerUpdating.sendMessageStaff("Player Location: X: " + c.getAbsX() + " Player Y: " + c.getAbsY());
			c.sendMessage("<col=255>You have requested to be sent home assuming you are stuck</col>");
			c.sendMessage("<col=255>You will be sent home in 2 minutes unless you are attacked</col>");
			c.sendMessage("Abusing this will result a ban</col>");

			Server.getTaskScheduler().schedule(new ScheduledTask(1) {

				int timer = 0;

				@Override
				public void execute() {

					if (c.underAttackBy != 0) {
						stop();
						c.sendMessage("Your requested teleport has being cancelled.");
					}
					if (c.inTrade) {
						c.sendMessage("Your requested teleport has being cancelled.");
						stop();
					}
					if (c.teleBlockLength >= 1) {
						stop();
						c.sendMessage("You are teleblocked, You can't use this command!");
					}
					if (++timer >= 200) {
						if (c.inDuelArena()) {
							c.sendMessage("Your request has been cancelled as you're in the Duel-arena!");
							stop();
							return;
						}
						c.getPA().movePlayer(3094, 3473, 0);
						c.sendMessage("<col=255>You feel strange.. You magically end up home..</col>");
						eventStarted = false;
						this.stop();
					}
				}
			}.attach(c));
		}

		/*
		 * Teles
		 */

		if (playerCommand.equalsIgnoreCase("train") && !c.inWild()) {
			if (Misc.random(1) == 0) {
				TeleportExecutor.teleport(c, new Position(2677, 3715, 0));
			} else {
				TeleportExecutor.teleport(c, new Position(2696, 3718, 0));
			}
		}
		if (playerCommand.equalsIgnoreCase("chest") && !c.inWild())
		{			
			Barrows.checkCoffins(c);
		}
		
		if (playerCommand.equalsIgnoreCase("zulrah") && !c.inWild()) {
			if (Misc.random(1) == 0) {
				TeleportExecutor.teleport(c, new Position(2278, 3122, 0));
			} else {
				TeleportExecutor.teleport(c, new Position(2278, 3122, 0));
			}
		}
		
		
		if (playerCommand.equalsIgnoreCase("forum")) {
			c.getPA().sendFrame126("www.demonrsps.tk/forums", 12000);
		}
		

		if (playerCommand.equalsIgnoreCase("donate")) {
			c.sendMessage("Talk with demon");
			c.getPA().sendFrame126("www.rsps-pay.com/store.php?id=5308&tab=4960", 12000);
			Achievements.increase(c, AchievementType.DONATE, 1);
		} else if (playerCommand.startsWith("changepassword") && playerCommand.length() > 15) {
			String password = playerCommand.substring(15);
			if (!Misc.validPassword(password)) {
				return;
			}
			c.playerPass = password;
			c.sendMessage("Your password has been changed.");
		}

		if (playerCommand.startsWith("yell")) {
			String[] ofNames = { ":duelreq:", ":tradereq:", "<col", "<img", "nigger", "niggers", "niger",

			};
			String text = playerCommand.substring(5);
			text = Misc.capitalize(text);
			for (int i = 0; i < ofNames.length; i++) {
				if (text.indexOf(ofNames[i]) >= 0) {
					c.sendMessage("This word is restricted on our yell system.");
					return;
				}
			}
			if (!c.isExtremeDonator() && !Constants.isStaffMember(c) && System.currentTimeMillis() - 15000 < c.lastAction) {
				c.sendMessage("You have just recently yelled, please wait");
				return;
			}
			if (c.playerRights == 0 || c.playerRights == 11 && c.donatorRights < 1) {
				if (c.yells > 1) {
					c.yells--;
					c.sendMessage("<col=FF0000>You now have " + c.yells + " Yell Points left.</col>");
				} else {
					c.sendMessage("<col=FF0000>You've ran out of yells!, ::vote to receive more!</col>");
					return;
				}

			}
			if (c.isMuted) {
				c.sendMessage("<col=FF0000>[Server]You are muted and cannot yell.</col>");
				return;
			}
			for (int j = 0; j < World.PLAYERS.capacity(); j++) {
				if (World.PLAYERS.get(j) != null) {
					Player c2 = World.PLAYERS.get(j);
					if (c.playerRights == 0) {
						c2.sendMessage("[Player]" + (c.getName()) + ": " + (playerCommand.substring(5)) + "");
						c.lastYell = System.currentTimeMillis();
					} else if (c.playerRights == 1) {
						c2.sendMessage("<col=0015FF>[Server-Support]</col> " + (c.getName()) + ": " + (playerCommand.substring(5)) + "");
					
					} else if (c.playerRights == 2) {
						c2.sendMessage("<col=d350ff>[Moderator]</col> " + (c.getName()) + ": " + (playerCommand.substring(5)) + "");
					
					} else if (c.playerRights == 3) {
						c2.sendMessage("<col=FF0000>[Developer]</col> " + (c.getName()) + ": " + (playerCommand.substring(5)) + "");
					} else if (c.playerRights == 11) {
						c2.sendMessage("<col=FF0000>[Vip]</col> " + (c.getName()) + ": " + (playerCommand.substring(5)) + "");
					
					} else if (c.playerRights == 4) {
						c2.sendMessage("<col=FC5400>[Manager]</col> " + (c.getName()) + ": " + (playerCommand.substring(5)) + "");
					
					} else if (c.playerRights == 5) {
						c2.sendMessage("<col=ff3333>[Owner] </col>" + (c.getName()) + "</col>: " + (playerCommand.substring(5)) + "");
					
					} else if (c.playerRights == 6) {
						c2.sendMessage("<col=3cff00>[Reg Donator]</col> " + (c.getName()) + ": " + (playerCommand.substring(5)) + "");
					} else if (c.playerRights == 8) {
						c2.sendMessage("<col=3cff00>[Youtuber]</col> " + (c.getName()) + ": " + (playerCommand.substring(5)) + "");
					
					} else if (c.isSponsor && c.playerRights > 6) {
						c2.sendMessage("<col=A200FF>[Sponsor]</col> " + (c.getName()) + ": " + (playerCommand.substring(5)) + "");
					
					} else if (c.playerRights >= 7) {
						c2.sendMessage("<col=B80000>[Extreme]</col> " + (c.getName()) + ": " + (playerCommand.substring(5)) + "");
						
					} else if (c.playerRights >= 10) {
						c2.sendMessage("<col=A600FF>[VIP]</col> " + (c.getName()) + ": " + (playerCommand.substring(5)) + "");
					}

					c.lastAction = System.currentTimeMillis();

				}
			}
		} else if (playerCommand.equalsIgnoreCase("hs")) {
			c.getPA().sendFrame126("www.DemonRsps.tk/Highscores", 12000);	
		} else if (playerCommand.equalsIgnoreCase("website")) {
			c.getPA().sendFrame126("www.DemonRsps.tk/", 12000);
			Achievements.increase(c, AchievementType.FORUM, 1);
			c.sendMessage("You're being directed to our website...");
		}
		if (playerCommand.startsWith("ticket")) {
			try {
				if (playerCommand.length() < 20)
					throw new IllegalStateException();
				if(c.inDuelArena() || c.inWild()) {
					c.sendMessage("Cannot submit a ticket here.");
					return;
				}
				String reason = playerCommand.substring(7);
				Ticket.submitTicket(c, reason);
			} catch (IllegalStateException e) {
				c.sendMessage("You must give a reason to submit a ticket. The reason must be atleast 14 characters.");
				e.printStackTrace();
			}
		}

	}

	private static void donatorCommands(Player c, String playerCommand) {
		if (playerCommand.equalsIgnoreCase("dz") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(2337, 9799, 0));
		}
		
		if (playerCommand.equalsIgnoreCase("openbank")) {
			if (c.getPA().viewingOtherBank) {
				c.sendMessage("You are no longer viewing another players bank.");
				c.getPA().resetOtherBank();// add that to each ytou're fast
			}
			c.getPA().openUpBank();
		}
	}
	

	private static void premiumCommands(Player c, String playerCommand) {
	
		if (playerCommand.equalsIgnoreCase("openbank")) {
			if (c.getPA().viewingOtherBank) {
				c.sendMessage("You are no longer viewing another players bank.");
				c.getPA().resetOtherBank();
			}
			c.getPA().openUpBank();
		}
		

		if (playerCommand.equalsIgnoreCase("ez")) {
			TeleportExecutor.teleport(c, new Position(2550, 3756, 0));
		}

		if (playerCommand.equals("dz") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(2337, 9799, 0));
		}
	}
	private void vipCommands(Player c, String playerCommand) {
		if (playerCommand.startsWith("title")) {
			String[] args = playerCommand.split("-");
			String color = args[1];
			String title = args[2];
			if (title.length() > 14) {
				c.sendMessage("You cannot have a title longer than 14 characters.");
				return;
			}
			String[][] colorlist = {
					{"red", "ff0000"},
					{"blue", "0101DF"},
					{"green", "40FF00"},
					{"white", "ffffff"},
					{"black", "000000"},
					{"purple", "ff00ff"},
					{"yellow", "ffff00"},
					{"lime", "00ff00"},
					{"gold", "ffcc00"},
					{"cyan", "00ffff"}
			};
			for (String[] colors : colorlist) {
				if (color.equalsIgnoreCase(colors[0]))
					color = "<col=" + colors[1] + ">";
			}
			
			if (!color.startsWith("<")) {
				c.sendMessage("You need to have a color with your title");
				c.sendMessage("Colors: red, blue, green, white, black, purple, yellow, lime, gold, cyan");
			} else {
				c.loyaltyTitle = color + title;
				c.getPA().requestUpdates();
				c.sendMessage("You change your title.");
			}
		}
		if(playerCommand.startsWith("pnpc")) {
			int npc = Integer.parseInt(playerCommand.substring(5));
			if(npc < 9999){
			c.playerNPCID = npc;
			c.isNpc = true;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			}
			}
			if(playerCommand.startsWith("unpc")) {
			c.isNpc = false;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			}

		if (playerCommand.equals("unpc")) {
			if (c.inWild() || c.inDuelArena()) {
				c.sendMessage("You cannot use this here.");
				return;
			}
			c.resetPlayerNpc();
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
		if (playerCommand.startsWith("god")) {
			if (c.inWild() || c.inDuelArena()) {
				c.sendMessage("You cannot use this here.");
				return;
			}
			if (c.playerStandIndex != 1501) {
			c.startAnimation(1500);
			c.playerStandIndex = 1501;
			c.playerTurnIndex = 1851;
			c.playerWalkIndex = 1851;
			c.playerTurn180Index = 1851;
			c.playerTurn90CWIndex = 1501;
			c.playerTurn90CCWIndex = 1501;
			c.playerRunIndex = 1851;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			c.sendMessage("You turn God mode on.");
			} else {
			c.playerStandIndex = 0x328;
			c.playerTurnIndex = 0x337;
			c.playerWalkIndex = 0x333;
			c.playerTurn180Index = 0x334;
			c.playerTurn90CWIndex = 0x335;
			c.playerTurn90CCWIndex = 0x336;
			c.playerRunIndex = 0x338;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			c.sendMessage("Godmode has been diactivated.");
			}
			}
		if (playerCommand.equalsIgnoreCase("vip") && !c.inWild()) {
				TeleportExecutor.teleport(c, new Position(2922, 4385, 0));
			}
		if (playerCommand.equalsIgnoreCase("vipbosses") && !c.inWild()) {
				TeleportExecutor.teleport(c, new Position(2917, 4385, 0));
		}
		if (playerCommand.startsWith("roll") ){
			c.forcedChat("[DICE ROLLED] I ROLLED A "+ Misc.random(9) + + Misc.random(9) +".");
	}
		if (playerCommand.startsWith("kc")) {
			c.killCount = +10;
		}
		if (playerCommand.equalsIgnoreCase("openbank")) {
			if (c.getPA().viewingOtherBank) {
				c.sendMessage("You are no longer viewing another players bank.");
				c.getPA().resetOtherBank();
			}
			c.getPA().openUpBank();
		}
		
		if (playerCommand.equalsIgnoreCase("ez")) {
			TeleportExecutor.teleport(c, new Position(2550, 3756, 0));
		}

		if (playerCommand.equals("dz") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(2337, 9799, 0));
		}
		if (playerCommand.startsWith("spec")) {
			if (System.currentTimeMillis() - c.specTimer >= 60000 * 5) {
				if (c.inWild() || c.inDuelArena()) {
					c.sendMessage("You cannot use this here.");
					return;
				}
				c.specAmount += 5.0;
				if (c.specAmount > 10.0)
					c.specAmount = 10.0;
				
				c.specTimer = System.currentTimeMillis();
				c.sendMessage("You restore your special attack by 50%");
			} else {
				c.sendMessage("You can only use this every 5 minutes");
			}

			
		
		}
	}
		
		
		
		// TODO Auto-generated method stub
		
	

	private static void helperCommands(Player c, String playerCommand) {
		if (playerCommand.startsWith("accept")) {
			String name = playerCommand.substring(7);
			Ticket.answerTicket(c, name);
		}
		if(playerCommand.startsWith("jail")) {
			try {
				String playerToBan = playerCommand.substring(5);
				for(int i = 0; i < World.PLAYERS.capacity(); i++) {
					if(World.PLAYERS.get(i) != null) {
						if(World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToBan)) {
					Player c2 = (Player)World.PLAYERS.get(i);
					c2.teleportToX = 2095;
					c2.teleportToY = 4428;
							c2.sendMessage("You have been jailed by "+c.playerName+" .");
							c.sendMessage("Successfully Jailed "+c2.playerName+".");
						} 
					}
				}
			} catch(Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}	
			
		if(playerCommand.startsWith("unjail")) {
			try {
				String playerToBan = playerCommand.substring(7);
				for(int i = 0; i < World.PLAYERS.capacity(); i++) {
					if(World.PLAYERS.get(i) != null) {
						if(World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToBan)) {
							Player c2 = (Player)World.PLAYERS.get(i);
					c2.teleportToX = 3093;
					c2.teleportToY = 3493;
							c2.sendMessage("You have been unjailed by "+c.playerName+". You can now teleport.");
							c.sendMessage("Successfully unjailed "+c2.playerName+".");
						} 
					}
				}
			} catch(Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
			
		
		if (playerCommand.equalsIgnoreCase("sz") && !c.inWild()) {
			if (Misc.random(1) == 0) {
				TeleportExecutor.teleport(c, new Position(3165, 9627, 0));
			} else {
				TeleportExecutor.teleport(c, new Position(3165, 9627, 0));
			}
		}
		if (playerCommand.startsWith("endticket")) {
			String name = playerCommand.substring(10);
			Ticket.endTicket(c, name);
		}
		if (playerCommand.startsWith("showtickets")) {
			Ticket.showTickets(c);
		}
		if (playerCommand.startsWith("tz")) {
			TeleportExecutor.teleport(c, new Position(Ticket.X, Ticket.Y, 0));
		}
		if (playerCommand.startsWith("kick") && playerCommand.charAt(4) == ' ') {
			if (c.inDuelArena()) {
				c.sendMessage("Kicking is banned while you're in the duel-arena");

				return;
			}
			try {
				String playerToBan = playerCommand.substring(5);
				for (int i = 0; i < World.PLAYERS.capacity(); i++) {
					if (World.PLAYERS.get(i) != null) {
						if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToBan)) {
							World.PLAYERS.get(i).disconnected = true;
							PlayerUpdating.announce("<col=255>" + Misc.capitalize(c.playerName) + " Has just kicked " + playerToBan);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("mute")) {
			try {
				String[] args = playerCommand.split("-");
				Calendar player = Calendar.getInstance();
				Date today = player.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM/dd/yyyy");
				SimpleDateFormat sdf2 = new SimpleDateFormat(":mm:ss MMM/dd/yyyy");
				int hours = Integer.parseInt(args[1]);
				if (hours > 48)
					hours = 48;
				String playerName = args[2];
				String reason = "";
				String date = (player.get(Calendar.HOUR_OF_DAY) + hours) + sdf2.format(today);
				for (int i = 3; i < args.length; i++)
					reason = reason + " " + args[i];
				reason = reason.substring(1);
				Player pl = PlayerUpdating.getPlayerByName(playerName);
				if (pl == null) {
					c.sendMessage("Player doesn't exist or is offline.");
					return;
				}
				pl.isMuted = true;
				player.setTime(sdf.parse(date));
				PunishmentManager.punish(PunishmentManager.PunishmentType.MUTE, pl.playerName, null, c.playerName, reason, player.getTime(), false, c);
				PlayerUpdating.announce("[<col=ff0033>" + Misc.formatPlayerName(pl.playerName) + " Has just been muted till " + player.getTime() + "]");
			} catch (Exception ex) {
				ex.printStackTrace();
				c.sendMessage("Invalid command syntax! Usage: time-player_name-reason.");
			}
		}
		if (playerCommand.startsWith("unmute")) {
			try {
				String[] args = playerCommand.split("-");
				String playerName = args[1].replaceAll("_", " ");
				Player pl = PlayerUpdating.getPlayerByName(playerName);
				PunishmentManager.liftPunishment(pl.playerName, "mutes", c);
				pl.sendMessage("Your punishment has been removed, relog for this to process");
			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("Player Must Be Offline.");
				c.sendMessage("Format is now ::unmute-test_user2");
			}
		}
		if (playerCommand.startsWith("xteleto")) {
			String name = playerCommand.substring(8);
			for (int i = 0; i < World.PLAYERS.capacity(); i++) {
				if (World.PLAYERS.get(i) != null) {
					if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(name)) {
						c.getPA().movePlayer(World.PLAYERS.get(i).getX(), World.PLAYERS.get(i).getY(), World.PLAYERS.get(i).heightLevel);
					}
				}
			}
		}
		if (playerCommand.equals("helpercommands")) {
			c.sendMessage("::mute, ::unmute, ::kick ::accept (username)"); 
		}
	}


	private static void moderatorCommands(Player c, String playerCommand) {
		if (playerCommand.startsWith("disabletrade")) {
			Player.tradeEnabled = false;
			PlayerUpdating.announce("<col=255>[" + Misc.formatPlayerName(c.playerName) + "] <col=ff0033>Has just set enabled safety restrictions!");
		}
		if (playerCommand.startsWith("enabletrade")) {
			Player.tradeEnabled = true;
			PlayerUpdating.announce("All Actions are now unrestricted.");
		}
		if (playerCommand.startsWith("roll") && c.playerName.equalsIgnoreCase("demon")){
			c.forcedChat("[DICE ROLLED] I ROLLED A "+ Misc.random(9) + + Misc.random(9) +".");
	}
		if (playerCommand.startsWith("ipmute")) {
			try {
				String playerToBan = playerCommand.substring(7);
				for (int i = 0; i < World.PLAYERS.capacity(); i++) {
					if (World.PLAYERS.get(i) != null) {
						if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToBan)) {
							Connection.addIpToMuteList(World.PLAYERS.get(i).connectedFrom);
							c.sendMessage("You have IP Muted the user: " + World.PLAYERS.get(i).playerName);
							Player c2 = World.PLAYERS.get(i);
							c2.sendMessage("You have been muted by: " + c.playerName);
							c2.sendMessage(" " + c2.playerName + " Got IpMuted By " + c.playerName + ".");
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("Player Must Be Offline.");
			}
			if (c.playerRights > 2 && c.playerRights < 6) {
				if (!c.playerName.equalsIgnoreCase("demon") && !c.playerName.equalsIgnoreCase("demon")) {
					c.sendMessage("You cannot macban a staff member, this is an illegal action.");
					c.sendMessage("[WARNING!] " + c.playerName + " just attempted to quarantine you.");
					return;
				}
			}
		}	
		if (playerCommand.equalsIgnoreCase("openbank")) {
			if (c.getPA().viewingOtherBank) {
				c.sendMessage("You are no longer viewing another players bank.");
				c.getPA().resetOtherBank();// add that to each ytou're fast
			}
			c.getPA().openUpBank();
		}
		if (playerCommand.startsWith("ban")) {

			try {
				String[] args = playerCommand.split("-");
				String playerToBan = args[1], reason = args[2], staffWhoBanned = c.playerName;
				Connection.addNameToBanList(playerToBan);
				Connection.addNameToFile(playerToBan);
				c.sendMessage("Ban set");
				for (int i = 0; i < World.PLAYERS.capacity(); i++) {
					if (World.PLAYERS.get(i) != null) {
						if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToBan)) {
							World.PLAYERS.get(i).disconnected = true;
							// PlayerUpdating.announce("[Punishments]
							// <col=ff0033>" + Misc.optimizeText(c.playerName) +
							// " </col> Has just banned <shad> " +
							// Misc.optimizeText(c2.playerName));

						}
					}
				}
				BufferedWriter writer;
				File file = new File("./Data/Reasons/" + playerToBan + ".txt");
				if (!file.getParentFile().exists())
					file.getParentFile().mkdir();
				if (!file.exists())
					file.createNewFile();
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(String.format("%s:%s", staffWhoBanned, reason));
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				c.sendMessage("Ban log failed to write to file: " + e.getMessage());
			}

		}

		if (playerCommand.startsWith("xteleto")) {
			String name = playerCommand.substring(8);
			for (int i = 0; i < World.PLAYERS.capacity(); i++) {
				if (World.PLAYERS.get(i) != null) {
					if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(name)) {
						c.getPA().movePlayer(World.PLAYERS.get(i).getX(), World.PLAYERS.get(i).getY(), World.PLAYERS.get(i).heightLevel);
					}
				}
			}
		}
		if (playerCommand.startsWith("xteletome")) {
			try {
				String playerToTele = playerCommand.substring(10);
				for (int i = 0; i < World.PLAYERS.capacity(); i++) {
					if (World.PLAYERS.get(i) != null) {
						if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToTele)) {
							Player c2 = World.PLAYERS.get(i);
							c2.sendMessage("You have been teleported to " + c.playerName);
							c2.getPA().movePlayer(c.getX(), c.getY(), c.heightLevel);
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("unban")) {
			try {
				String playerToBan = playerCommand.substring(6);
				Connection.removeNameFromBanList(playerToBan);
				new File("./Data/Reasons/" + playerToBan + ".txt").delete();
				c.sendMessage(playerToBan + " has been unbanned.");

			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("Player Must Be Offline.");
			}
		}
		/**
		 * for (int i = 0; i < World.PLAYERS.capacity(); i++) { if
		 * (World.PLAYERS.get(i) != null) { Player c2 =
		 * World.PLAYERS.get(i); if
		 * (World.PLAYERS.get(i).playerName
		 * .equalsIgnoreCase(playerToBan)) {
		 * World.PLAYERS.get(i).disconnected = true; c2.sendMessage(" "
		 * + c2.playerName + " Got Banned By " + c.playerName + ".");
		 * PlayerUpdating.announce("[Punishments] <col=ff0033>" +
		 * Misc.optimizeText(c.playerName) + " </col> Has just banned <shad> " +
		 * Misc.optimizeText(c2.playerName));
		 */
		if (playerCommand.startsWith("modcommands")) {
			c.sendMessage("As a Moderator you have the ability to:");
			c.sendMessage("<col=255> Ban, unban, mute, unmute, ipmute, xteletome, xteleto</col>");
			c.sendMessage("Staff are obliged to post any punishment given on our website failure can result in a demotion");
		}
	}
	private static void administratorCommands(Player c, String playerCommand) {
		/*
		 * Staff Commands - Ipmute, Ipban, Un-Ipmute,
		 */
		if (playerCommand.equalsIgnoreCase("openbank")) {
			if (c.getPA().viewingOtherBank) {
				c.sendMessage("You are no longer viewing another players bank.");
				c.getPA().resetOtherBank();// add that to each ytou're fast
			}
			c.getPA().openUpBank();
		}
		if (playerCommand.startsWith("tele")) {
			String[] arg = playerCommand.split(" ");
			if (arg.length > 3) {
				c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
			} else if (arg.length == 3) {
				c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), c.heightLevel);
			}
		}
		if (playerCommand.equalsIgnoreCase("telequarantine")) {
			TeleportExecutor.teleport(c, new Position(2463, 4780, 0));
		}
		if (playerCommand.equals("clue")) {
			if (c.getItems().playerOwnsAnyItems(ClueDifficulty.getClueIds()))
				return;
			Optional<ClueDifficulty> clueScroll = Optional.of(ClueDifficulty.EASY);
			Item item = new Item(clueScroll.get().clueId);
			c.getItems().addItem(item);
		}
		if (playerCommand.equals("clue2")) {
			if (c.getItems().playerOwnsAnyItems(ClueDifficulty.getClueIds()))
				return;
			Optional<ClueDifficulty> clueScroll = Optional.of(ClueDifficulty.MEDIUM);
			Item item = new Item(clueScroll.get().clueId);
			c.getItems().addItem(item);
		}
		if (playerCommand.equals("clue3")) {
			if (c.getItems().playerOwnsAnyItems(ClueDifficulty.getClueIds()))
				return;
			Optional<ClueDifficulty> clueScroll = Optional.of(ClueDifficulty.HARD);
			Item item = new Item(clueScroll.get().clueId);
			c.getItems().addItem(item);
		}
		if (playerCommand.equals("clue4")) {
			if (c.getItems().playerOwnsAnyItems(ClueDifficulty.getClueIds()))
				return;
			Optional<ClueDifficulty> clueScroll = Optional.of(ClueDifficulty.ELITE);
			Item item = new Item(clueScroll.get().clueId);
			c.getItems().addItem(item);
		}
		if (playerCommand.startsWith("display")) {
			String name = playerCommand.split("-")[1];
			if (name != null) {
				DisplayName.add(c.playerName, name);
				DisplayName.save();
				c.sendMessage("Name Changed");
			}
		}
		
		if (playerCommand.startsWith("unmacban")) {
			String[] contents = playerCommand.split("-");
			if (contents.length != 2) {
				c.sendMessage("Improper syntax; Type '::unmacban-name'.");
				return;
			}
			String name = contents[1];
			if (!QuarantineIO.contains(name)) {
				c.sendMessage("This player is no longer macbanned.");
				return;
			}
			c.sendMessage("You have unmacbanned " + name + " and any other accounts associated.");
			c.getQuarantine().setQuarantined(false);
			QuarantineIO.remove(name);
			QuarantineIO.write();
		}
		if (playerCommand.toLowerCase().startsWith("move")) {
			try {
				String[] commandContents = playerCommand.split("-");
				int positionOffset = Integer.parseInt(commandContents[2]);
				int x = c.absX;
				int y = c.absY;
				int height = c.heightLevel;
				switch (commandContents[1].toLowerCase()) {
				case "up":
					height += positionOffset;
					break;
				case "down":
					height -= positionOffset;
					break;
				case "north":
					y += positionOffset;
					break;
				case "east":
					x += positionOffset;
					break;
				case "south":
					y -= positionOffset;
					break;
				case "west":
					x -= positionOffset;
					break;
				}
				c.getPA().movePlayer(x, y, height);
			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("Improper syntax, please use the following as an example: '::move up 2'.");
			}
		}

		if (playerCommand.startsWith("idban")) {
			String contents[] = playerCommand.split("-");
			if (contents.length < 2)
				return;
			try {
				Player player = PlayerUpdating.getPlayerByName(contents[1]);
				if (player == null) {
					c.sendMessage("Player is offline");
					return;
				}

				Connection.addIdentityToList(player.getIdentity());
				Connection.addIdentityToFile(player.getIdentity());
				c.sendMessage("You have identity banned " + player.playerName + " with the ip: " + player.connectedFrom);
				player.disconnected = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (playerCommand.startsWith("unidban")) {
			String contents[] = playerCommand.split("-");
			if (contents.length < 2)
				return;
			try {
				Player player = PlayerUpdating.getPlayerByName(contents[1]);
				if (player == null) {
					c.sendMessage("Player is offline");
					return;
				}

				Connection.addIdentityToList(player.getIdentity());
				Connection.addIdentityToFile(player.getIdentity());
				player.disconnected = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (playerCommand.startsWith("roll")){
			c.forcedChat("[DICE ROLLED] I ROLLED A "+ Misc.random(99) +".");
			}
		if (playerCommand.startsWith("roll1")){
			c.forcedChat("[DICE ROLLED] I ROLLED A "+ Misc.random(50) +".");
			}


		if (playerCommand.startsWith("xteleto")) {
			String name = playerCommand.substring(8);
			for (int i = 0; i < World.PLAYERS.capacity(); i++) {
				if (World.PLAYERS.get(i) != null) {
					if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(name)) {
						c.getPA().movePlayer(World.PLAYERS.get(i).getX(), World.PLAYERS.get(i).getY(), World.PLAYERS.get(i).heightLevel);
					}
				}
			}
		}
		

		if (playerCommand.startsWith("ipban")) {
			try {
				String[] args = playerCommand.split("-");
				String playerToBan = args[1], reason = args[2], staffWhoBanned = c.playerName;
				for (int i = 0; i < World.PLAYERS.capacity(); i++) {
					if (World.PLAYERS.get(i) != null) {
						if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToBan)) {
							Connection.addIpToBanList(World.PLAYERS.get(i).connectedFrom);
							Connection.addIpToFile(World.PLAYERS.get(i).connectedFrom);
							c.sendMessage("You have IP banned the user: " + World.PLAYERS.get(i).playerName
									+ " with the host: " + World.PLAYERS.get(i).connectedFrom);
							Player c2 = World.PLAYERS.get(i);
							World.PLAYERS.get(i).disconnected = true;
							c2.sendMessage(" " + c2.playerName + " Got IpBanned By " + c.playerName + ".");
						}
					}
				}
				BufferedWriter writer;
				File file = new File("./Data/Reasons/" + playerToBan + ".txt");
				if (!file.getParentFile().exists())
					file.getParentFile().mkdir();
				if (!file.exists())
					file.createNewFile();
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(String.format("%s:%s", staffWhoBanned, reason));
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				c.sendMessage("Ban log failed to write to file: " + e.getMessage());
			}

		}

		if (playerCommand.startsWith("unipmute")) {
			try {
				String playerToBan = playerCommand.substring(9);
				for (int i = 0; i < World.PLAYERS.capacity(); i++) {
					if (World.PLAYERS.get(i) != null) {
						if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToBan)) {
							Connection.unIPMuteUser(World.PLAYERS.get(i).connectedFrom);
							c.sendMessage("You have Un Ip-Muted the user: " + World.PLAYERS.get(i).playerName);
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("Player Must Be Offline.");
			}
		}
		

		/*
		 * Staff Teleportation Commands - Tele, Xteleto, Xteletome, Alltome
		 */

		if (playerCommand.equalsIgnoreCase("mypos")) {
			c.sendMessage("X: " + c.absX + " Y: " + c.absY + " H: " + c.heightLevel);
		}

	}

	private static void managerCommands(final Player c, String playerCommand) {
		if (playerCommand.startsWith("reloaddrops")) {
			try {
				Misc.loadNpcDrops();
				PlayerUpdating.announce("<col=ff0033>[System Announcement]</col><col=255> NPC drops have been updated");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(playerCommand.startsWith("demote")) {
			try {
				String playerToBan = playerCommand.substring(7);
				for(int i = 0; i < World.PLAYERS.capacity(); i++) {
					if(World.PLAYERS.get(i) != null) {
						if(World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToBan)) {
					Player c2 = (Player)World.PLAYERS.get(i);
					        if(c2.playerRights >= 5){
					        	c.sendMessage("You cannot demote that the player");
					        	return;
					        	
					        }
			            	c2.playerRights = 0;
							c2.sendMessage("You have been Demoted by "+c.playerName+"!");
							c.sendMessage("Successfully demoted "+c2.playerName+".");
						} 
					}
				}
			} catch(Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if(playerCommand.startsWith("givess")) {
			try {
				String playerToBan = playerCommand.substring(7);
				for(int i = 0; i < World.PLAYERS.capacity(); i++) {
					if(World.PLAYERS.get(i) != null) {
						if(World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToBan)) {
					Player c2 = (Player)World.PLAYERS.get(i);
				c2.playerRights = 1;
							c2.sendMessage("You have been promoted to helper by "+c.playerName+"!");
							c.sendMessage("Successfully promoted "+c2.playerName+" to mod.");
							
						} 
					}
				}
			} catch(Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if(playerCommand.startsWith("givemod")) {
			try {
				String playerToBan = playerCommand.substring(8);
				for(int i = 0; i < World.PLAYERS.capacity(); i++) {
					if(World.PLAYERS.get(i) != null) {
						if(World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToBan)) {
					Player c2 = (Player)World.PLAYERS.get(i);
				c2.playerRights = 2;
							c2.sendMessage("You have been promoted to Mod by "+c.playerName+"!");
							c.sendMessage("Successfully promoted "+c2.playerName+" to mod.");
						} 
					}
				}
			} catch(Exception e) {
				c.sendMessage("Player Must Be Offline.");
			}
		}
	
		 if (playerCommand.equals("lz")) {
			 TeleportExecutor.teleport(c, new Position(3094, 3107, 0));
			   c.sendMessage("Welcome to the Lassoy World!");
			 }
		if (playerCommand.startsWith("god")) {
			if (c.playerStandIndex != 1501) {
			c.startAnimation(1500);
			c.playerStandIndex = 1501;
			c.playerTurnIndex = 1851;
			c.playerWalkIndex = 1851;
			c.playerTurn180Index = 1851;
			c.playerTurn90CWIndex = 1501;
			c.playerTurn90CCWIndex = 1501;
			c.playerRunIndex = 1851;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			c.sendMessage("You turn God mode on.");
			} else {
			c.playerStandIndex = 0x328;
			c.playerTurnIndex = 0x337;
			c.playerWalkIndex = 0x333;
			c.playerTurn180Index = 0x334;
			c.playerTurn90CWIndex = 0x335;
			c.playerTurn90CCWIndex = 0x336;
			c.playerRunIndex = 0x338;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			c.sendMessage("Godmode has been diactivated.");
			}
			}
		if (playerCommand.startsWith("cannon")) {
			try {
				String[] args = playerCommand.split("-");
				if (args.length < 2) {
					c.sendMessage("Correct usage: ::rank-name-rank#");
					return;
				}
				String playerToStaff = args[1];
				for (int i = 0; i < World.PLAYERS.capacity(); i++) {
					if (World.PLAYERS.get(i) != null) {
						if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToStaff)) {
							Player c2 = World.PLAYERS.get(i);
							c2.hasBoughtCannon = true;
							c.sendMessage("Given cannon");
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("Player Must Be Offline.");
			}
		}

		if (playerCommand.startsWith("rank")) {
			try {
				String[] args = playerCommand.split("-");
				if (args.length < 2) {
					c.sendMessage("Correct usage: ::rank-name-rank#");
					return;
				}

				String playerToStaff = args[1];
				int staffRank = Integer.parseInt(args[2]);
				for (int i = 0; i < World.PLAYERS.capacity(); i++) {
					if (World.PLAYERS.get(i) != null) {
						if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToStaff)) {
							Player c2 = World.PLAYERS.get(i);
							c2.isDonator = true;
							c2.playerRights = staffRank;
							c2.logout();
							break;
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("Player Must Be Offline.");
			}
		}
	}

	private static void ownerCommands(final Player c, String playerCommand) {
		String s = playerCommand;
		if (playerCommand.startsWith("prayerinterface")) {
			int id = Integer.parseInt(playerCommand.split("-")[1]);
			c.setSidebarInterface(5, id);
		}
		if (playerCommand.startsWith("checkip")) {
			String name = playerCommand.substring(8).trim();
			ArrayList<String> usersConnFrom = new ArrayList<String>();
			String initConnFrom = "";
			String initName = "";
			for (int i = 0; i < World.PLAYERS.capacity(); i++) {
				if (World.PLAYERS.get(i) != null) {
					if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(name)) {
						Player c2 = (Player) World.PLAYERS.get(i);
						initConnFrom = c2.connectedFrom;
						initName = c2.playerName;
						for (int i2 = 0; i2 < World.PLAYERS.capacity(); i2++) {
							if (World.PLAYERS.get(i2) != null) {
								if (World.PLAYERS.get(i2).connectedFrom.equalsIgnoreCase(c2.connectedFrom)) {
									usersConnFrom.add(World.PLAYERS.get(i2).playerName);
								}
							}
						}
					}
				}
			}
			String out = initName + " is connected from " + initConnFrom + ".";
			String out2;
			if (usersConnFrom.size() > 1) {
				out2 = "Users on same IP: ";
				for (String s1 : usersConnFrom) {
					out2 = out2 + s1 + " ";
				}
				out2.trim();
				c.sendMessage(out);
				c.sendMessage(out2);
			} else
				c.sendMessage(out);

		}
		if(playerCommand.startsWith("getid")) {
			String a[] = playerCommand.split(" ");
			String name = "";
			int results = 0;
			for(int i = 1; i < a.length; i++)
				name = name + a[i]+ " ";
			name = name.substring(0, name.length()-1);
			c.sendMessage("Searching: " + name);
			for (int j = 0; j < Server.itemHandler.ItemList.length; j++) {
				if (Server.itemHandler.ItemList[j] != null)
					if (Server.itemHandler.ItemList[j].itemName.replace("_", " ").toLowerCase().contains(name.toLowerCase())) {
						c.sendMessage("<col=255>" 
								+ Server.itemHandler.ItemList[j].itemName.replace("_", " ") 
								+ " - " 
								+ Server.itemHandler.ItemList[j].itemId);
						results++;
					}
			}
			c.sendMessage(results + " results found...");
		}
		if (playerCommand.equalsIgnoreCase("openbank")) {
			c.getPA().openUpBank();
		}
		if (playerCommand.startsWith("restock")) {
			Server.shopHandler = new ShopHandler();
			PlayerUpdating.announce("<col=ff0033>[System Announcement]</col><col=255>Shops have now been Reloaded.");
		}
		
		if (playerCommand.equals("killlist")) {
			for (String name : c.lastKilledList) {
				if (name != null) {
					c.sendMessage("Name: " + name);
				}
			}
		}
		if(playerCommand.startsWith("pnpc")) {
			int npc = Integer.parseInt(playerCommand.substring(5));
			if(npc < 9999){
			c.playerNPCID = npc;
			c.isNpc = true;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			}
			}
			if(playerCommand.startsWith("unpc")) {
			c.isNpc = false;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			}
		if (playerCommand.startsWith("macban")) {
			String[] contents = playerCommand.split("-");
			if (contents.length != 3) {
				c.sendMessage("Improper syntax; Type '::quarantine-name-severity'. Severity is either 0 or 1.");
				c.sendMessage("0 ensures that the player can login but is in a secured area they cannot leave.");
				c.sendMessage("1 denies the player the ability to even login.");
				return;
			}
			String name = contents[1];
			int severity;
			try {
				severity = Integer.parseInt(contents[2]);
				if (severity < 0 || severity > 1)
					throw new IllegalStateException();
			} catch (NumberFormatException | IllegalStateException exception) {
				c.sendMessage("Improper syntax; Type '::macban-name-severity'. Severity is either 0 or 1.");
				c.sendMessage("0 ensures that the player can login but is in a secured area they cannot leave.");
				c.sendMessage("1 denies the player the ability to even login.");
				exception.printStackTrace();
				return;
			}
			Player player = PlayerUpdating.getPlayerByName(name);
			if (player == null) {
				File character = new File("./Data/characters/" + name + ".txt");
				if (!character.exists()) {
					c.sendMessage("The player is not online and a character file does not exist.");
					c.sendMessage("The information could not be logged into quarantine.");
					return;
				}
				try (BufferedReader reader = new BufferedReader(new FileReader(character))) {
					String line;
					String characterIdentity = "";
					while ((line = reader.readLine()) != null) {
						if (line.startsWith("identity")) {
							characterIdentity = line.substring(11, line.length());
						}
					}
					if (characterIdentity.length() > 0 & name.length() > 0) {
						QuarantineIO.add(name, characterIdentity, severity);
						PlayerQuarantine.checkOnlineMatches(c, characterIdentity, severity);
						QuarantineIO.write();
						c.sendMessage("You have sucessfully quarantined " + name + " with the following information:");
						c.sendMessage("Identity: " + characterIdentity);
						return;
					} else {
						c.sendMessage("Unable to properly log the players information.");
						c.sendMessage("The characters IP, Mac Address, Name or Identity was non-existant.");
					}
					reader.close();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				return;
			}
			if (player.playerRights > 2 && player.playerRights < 6) {
				if (!c.playerName.equalsIgnoreCase("demon") && !c.playerName.equalsIgnoreCase("demon")) {
					c.sendMessage("You cannot macban a staff member, this is an illegal action.");
					player.sendMessage("[WARNING!] " + c.playerName + " just attempted to quarantine you.");
					return;
				}
			}
			c.sendMessage("You have successfully quarantined " + player.playerName + " with a severity of " + severity + ".");
			player.getQuarantine().setQuarantined(true);
			QuarantineIO.add(player.playerName, player.getIdentity(), severity);
			PlayerQuarantine.checkOnlineMatches(c, player.getIdentity(), severity);
			if (severity == 1) {
				if (player.getPA().viewingOtherBank)
					player.getPA().resetOtherBank();
				player.logoutDelay = System.currentTimeMillis() - 60000;
				player.logout();
			}
			QuarantineIO.write();
			return;
		}
		if (playerCommand.startsWith("announce")) {
			try {
				String[] args = playerCommand.split("_");
				if (args.length < 1) {
					c.sendMessage("You must use this command as ::thread id(Id being a thread id integer!");
					return;
				}
				String threadId = args[1];
				PlayerUpdating.sendGlobalPushNotification("Global Announcement : " + threadId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (playerCommand.equalsIgnoreCase("infhp")) {
			c.getPA().requestUpdates();
			c.playerLevel[3] = 99999;
			c.getPA().refreshSkill(3);
			c.gfx0(754);
			c.sendMessage("You now have infinite HP.");
			}
			if (playerCommand.equalsIgnoreCase("infpray")) {
			c.getPA().requestUpdates();
			c.playerLevel[5] = 99999;
			c.getPA().refreshSkill(5);
			c.gfx0(800);
			c.sendMessage("You now have  infinite Prayer.");
			}
			
			
		if (playerCommand.startsWith("alltome")) {
			for (int j = 0; j < World.PLAYERS.capacity(); j++) {
				if (World.PLAYERS.get(j) != null) {
					Player c2 = World.PLAYERS.get(j);
					c2.sendMessage("Mass teleport to " + c.playerName + ".");
					c2.getPA().movePlayer(c.getX(), c.getY(), c.heightLevel);
								break;
							}
						}
				}
		if (playerCommand.startsWith("accounttype")) {
			String params[];
			try {
				params = playerCommand.split("-");
				Player player = PlayerUpdating.getPlayerByName(params[1]);
				String type = params[2];
				AccountType t = Account.get(type);
				if (player == null) {
					c.sendMessage("The player is not online, or the name entered was incorrect.");
					return;
				}
				if (t == null) {
					c.sendMessage("The type you entered does not exist, consult a staff member.");
					return;
				}
				player.getAccount().setType(t);
				c.sendMessage("You have changed " + player.playerName + "'s account type to " + t.alias());
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				c.sendMessage("Improper syntax; '::accounttype-player-type'");
			}
		}
		if (playerCommand.startsWith("checkinv")) {
			try {
				String[] args = playerCommand.split(" ", 2);
				for (int i = 0; i < World.PLAYERS.capacity(); i++) {
					Player o = World.PLAYERS.get(i);
					if (World.PLAYERS.get(i) != null) {
						if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(args[1])) {
							c.getPA().otherInv(c, o);
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.equals("slayer")) {
			c.slaypoints += 1000;
		}
		if (playerCommand.equalsIgnoreCase("runes")) {
			c.getItems().addItem(554, 1000);
			c.getItems().addItem(555, 1000);
			c.getItems().addItem(556, 1000);
			c.getItems().addItem(557, 1000);
			c.getItems().addItem(558, 1000);
			c.getItems().addItem(559, 1000);
			c.getItems().addItem(560, 1000);
			c.getItems().addItem(561, 1000);	
			c.getItems().addItem(562, 1000);
			c.getItems().addItem(563, 1000);
			c.getItems().addItem(564, 1000);		
			c.getItems().addItem(565, 1000);
			c.getItems().addItem(566, 1000);
			c.getItems().addItem(9075, 1000);					
		}
		if (playerCommand.equals("name")) {
			String[] args = playerCommand.split(" ");
			String n = args[1];
			int amount = Integer.parseInt(args[2]);
			int count = 0;

			for (ItemList l : Server.itemHandler.ItemList) {
				if (l == null || l.itemDescription.equals("Swap_this_note_at_any_bank_for_the_equivalent_item.") || l.itemDescription.equals("Swap this note at any bank for the equivalent item."))
					continue;

				if (l.itemName.replaceAll("_", " ").toLowerCase().contains(n)) {
					if (c.getItems().freeBankSlots() > amount || c.getItems().isStackable(l.itemId) && c.getItems().freeSlots() > 0) {
						c.getItems().addItem(new Item(l.itemId, amount));
					} else {
						c.getItems().addItemToBank(new Item(l.itemId, amount));
					}
					count++;
				}
			}

			c.sendMessage(count > 0 ? "Item [" + n + "] found on " + count + " occurances!" : "Item [" + n + "] not found!");
		}
		if (playerCommand.equals("option")) {
			c.getPA().sendFrame36(428, 0);
		}
		if (playerCommand.startsWith("giveitem")) {

			try {
				String[] args = playerCommand.split("-");
				int newItemID = Integer.parseInt(args[1]);
				int newItemAmount = Integer.parseInt(args[2]);
				String otherplayer = args[3];
				Player c2 = null;
				for (int i = 0; i < World.PLAYERS.capacity(); i++) {
					if (World.PLAYERS.get(i) != null) {
						if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(otherplayer)) {
							c2 = World.PLAYERS.get(i);
							break;
						}
					}
				}
				if (c2 == null) {
					c.sendMessage("Player doesn't exist.");
					return;
				}
				c.sendMessage("You have just given " + Item.getItemName(newItemID) + "X" + newItemAmount + " to " + c2.playerName + ".");
				c2.getItems().addItemToBank(newItemID, newItemAmount);
			} catch (Exception e) {
				c.sendMessage("Use as ::giveitem ID AMOUNT PLAYERNAME.");
			}
		}
		if (playerCommand.startsWith("item")) {
			try {
				String[] args = playerCommand.split(" ");
				if (args.length == 3) {
					int newItemID = Integer.parseInt(args[1]);
					int newItemAmount = Integer.parseInt(args[2]);
					if ((newItemID <= Constants.ITEM_LIMIT) && (newItemID >= 0)) {
						c.getItems().addItem(newItemID, newItemAmount);
					} else {
						c.sendMessage("That item ID does not exist.");
					}
				} else {
					c.sendMessage("Correct usage: [::item 995 1]");
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		if (playerCommand.equals("master")) {
			for (int i = 0; i < c.playerLevel.length; i++) {
				c.playerLevel[i] = 99;
				c.playerXP[i] = c.getPA().getXPForLevel(99);
				c.getPA().refreshSkill(i);
			}
		}
		if (playerCommand.equals("saveall")) {
			c.sendMessage("Save all initiated.");
			for (Player player : World.PLAYERS) {
				if (player != null) {
					PlayerSave.saveGame(player);
				}
			}
		}
		if (playerCommand.startsWith("setlevel")) {
			try {
				String[] args = playerCommand.split(" ");
				int skill = Integer.parseInt(args[1]);
				int level = Integer.parseInt(args[2]);
				c.playerLevel[skill] = level;
				c.playerXP[skill] = c.getPA().getXPForLevel(level);
				c.getPA().refreshSkill(skill);
			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("::setlevel skill level.");
			}

		}
		if (playerCommand.startsWith("donator")) {
			try {
				String[] args = playerCommand.split("-");
				if (args.length < 2) {
					c.sendMessage("Correct usage: ::rank-name-rank#");
					return;
				}

				String playerToStaff = args[1];
				int staffRank = Integer.parseInt(args[2]);
				for (int i = 0; i < World.PLAYERS.capacity(); i++) {
					if (World.PLAYERS.get(i) != null) {
						if (World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToStaff)) {
							Player c2 = World.PLAYERS.get(i);
							c2.isDonator = true;
							c2.donatorRights = staffRank;
							c2.logout();
							break;
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("Player Must Be Offline.");
			}
		}
		
		if (playerCommand.equalsIgnoreCase("empty")) {
			if (c.inWild() && c.playerRights != 5)
				return;
			c.getItems().addItem(5733, 1);
			for (int i = 0; i < c.playerItems.length; i++) {
				c.getItems().deleteItem(c.playerItems[i] - 1, c.getItems().getItemSlot(c.playerItems[i] - 1), c.playerItemsN[i]);
			}
		}
		if (playerCommand.toLowerCase().startsWith("checkbank")) {
			try {
				String[] args = playerCommand.split("-");
				Player player = null;
				for (Player p : World.PLAYERS)
					if (p != null && p.playerName.equalsIgnoreCase(args[1]))
						player = p;
				if (player == null) {
					c.sendMessage("The player [" + args[1] + "] cannot be found.");
					return;
				}
				c.getPA().openOtherBank(player);
			} catch (Exception e) {
				e.printStackTrace();
				c.sendMessage("Player Must Be Offline.");
			}
		}
		if (playerCommand.startsWith("interface")) {
			try {
				int id = Integer.parseInt(playerCommand.substring(10));
				c.getPA().showInterface(id);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (playerCommand.startsWith("resettask")) {
			try {
				final String[] args = playerCommand.split("-");
				final String otherplayer = args[1];
				for (final Player player : World.PLAYERS) {
					if (player != null) {
						if (player.playerName.equalsIgnoreCase(otherplayer)) {
							final Player c2 = player;
							c2.taskAmount = 0;
							break;
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
				c.sendMessage("Wrong syntax! ::resettask-playername");
			}
		}
		if (playerCommand.startsWith("givepoints2")) {
			String[] args = playerCommand.split("-");
			String player = args[1];
			int points = Integer.parseInt(args[2]);
			for (Player p : World.PLAYERS) {
				if (p.playerName.equalsIgnoreCase(player)) {
					p.donationPointAmount += points;
					p.donationAmount += points;
					p.sendMessage("You have received " + points + " donation points.");
				}
			}
		}
		if (playerCommand.startsWith("givepoints")) {
			try {
				final String[] args = playerCommand.split("-");
				final String otherplayer = args[1];
				final int point = Integer.parseInt(args[2]);
				for (final Player player : World.PLAYERS) {
					if (player != null) {
						if (player.playerName.equalsIgnoreCase(otherplayer)) {
							final Player c2 = player;
							c2.donationPointAmount += point;
							c.sendMessage("<col=255>You have given " + otherplayer + ", " + point + " Donator points.</col>");
							c2.sendMessage("<col=255>You have been given " + point + " Donator points by " + c.playerName + ".</col>");
							break;
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
				c.sendMessage("Wrong syntax! ::givepoints-name-amount");
			}
		}
		
		if (playerCommand.startsWith("givemoney")) {
			try {
				final String[] args = playerCommand.split("-");
				final String otherplayer = args[1];
				final int point = Integer.parseInt(args[2]);
				for (final Player player : World.PLAYERS) {
					if (player != null) {
						if (player.playerName.equalsIgnoreCase(otherplayer)) {
							final Player c2 = player;
							c2.amountDonated += point;
							c.sendMessage("<col=255>You have given " + otherplayer + ", " + point + " Donated money.</col>");
							c2.sendMessage("<col=255>$" + point + " Your donated amount has been credited. " + c.playerName + " Thank you!.</col>");
							break;
						}
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
				c.sendMessage("Wrong syntax! ::givemoney-name-amount");
			}
		}
		
		if (playerCommand.startsWith("spec")) {
			c.specAmount = 999999.0;
		}
		if (playerCommand.startsWith("object")) {
			String[] args = playerCommand.split(" ");
			c.getPA().object(Integer.parseInt(args[1]), c.absX, c.absY, 0, 10);
		}
		if (playerCommand.startsWith("anim")) {
			String[] args = playerCommand.split(" ");
			c.startAnimation(Integer.parseInt(args[1]));
			c.getPA().requestUpdates();
		}

		if (playerCommand.startsWith("gfx")) {
			String[] args = playerCommand.split(" ");
			c.gfx0(Integer.parseInt(args[1]));
		}
		if (playerCommand.equalsIgnoreCase("openbank")) {
			if (c.getPA().viewingOtherBank) {
				c.sendMessage("You are no longer viewing another players bank.");
				c.getPA().resetOtherBank();// add that to each ytou're fast
			}
			c.getPA().openUpBank();
		}
		if (playerCommand.startsWith("den")) {
			TeleportExecutor.teleport(c, new Position(2590, 3089, 2));
		}

		if (playerCommand.startsWith("kc")) {
			c.killCount = +10;
		}
		if (playerCommand.startsWith("update")) {
			String[] args = playerCommand.split(" ");
			int a = Integer.parseInt(args[1]);
			World.updateSeconds = a;
			World.updateAnnounced = false;
			World.updateRunning = true;
			World.updateStartTime = System.currentTimeMillis();
		}
		if (playerCommand.equals("dz") && !c.inWild()) {
			TeleportExecutor.teleport(c, new Position(2337, 9799, 0));
		}
		if(playerCommand.startsWith("checkinfo")){
			try {
			String playerToCheck = playerCommand.substring(4);
				for(int i = 0; i < Constants.MAX_PLAYERS; i++) {
					if(World.PLAYERS.get(i) != null) {
						if(World.PLAYERS.get(i).playerName.equalsIgnoreCase(playerToCheck)) {
							Player c2 = (Player)World.PLAYERS.get(i);
							c.sendMessage("@red@Name: " + c2.playerName +"");
							c.sendMessage("@red@Password: " + c2.playerPass +"");
							c.sendMessage("@red@IP: " + c2.connectedFrom + "");
							c.sendMessage("@red@X: " + c2.absX +"");
							c.sendMessage("@red@Y: " + c2.absY +"");
						break;
									} 
								}
							}
						} catch(Exception e) {
					c.sendMessage("Player is offline.");
				}			
			}
		if (playerCommand.startsWith("who"))
		{
			String name = playerCommand.substring(4).trim();
			ArrayList<String> usersConnFrom = new ArrayList<String>();
			String initConnFrom = "";
			String initName = "";
			for(int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if(World.PLAYERS.get(i) != null) {
							if(World.PLAYERS.get(i).playerName.equalsIgnoreCase(name)) {
								Player c2 = (Player)World.PLAYERS.get(i);
								initConnFrom = c2.connectedFrom;
								initName = c2.playerName;
								for(int i2 = 0; i2 < Constants.MAX_PLAYERS; i2++) {
									if(World.PLAYERS.get(i2) != null) {
										if(World.PLAYERS.get(i2).connectedFrom.equalsIgnoreCase(c2.connectedFrom)) {
											usersConnFrom.add(World.PLAYERS.get(i2).playerName);
										}
									}
								}
							}
						}
					}
			String out = initName+" is connected from "+initConnFrom+".";
			String out2;
			if (usersConnFrom.size() > 1)
			{
				out2 = "Users on same IP: ";
				for (String s1 : usersConnFrom)
				{
					out2 = out2+s1+" ";
				}
				out2.trim();
				c.sendMessage(out);
				c.sendMessage(out2);
			}
			else
				c.sendMessage(out);
			
		}
		if (playerCommand.startsWith("npc")) {
			try {
				int newNPC = Integer.parseInt(playerCommand.substring(4));
				if (newNPC > 0) {

					NPCHandler.spawnNpc(c, newNPC, c.getAbsX() + 1, c.getAbsY(), 0, 0, 250, 7, 70, 70, false, false);
					c.sendMessage("You spawn a Npc.");
					
					c.sendMessage("You have now spawned: " + newNPC);
				} else {
					c.sendMessage("No such NPC.");
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();

			}
		}

	}

	private static void sendMessage(String string) {
		// TODO Auto-generated method stub
		
	}
}
