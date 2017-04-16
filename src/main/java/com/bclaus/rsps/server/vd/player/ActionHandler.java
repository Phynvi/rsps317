package com.bclaus.rsps.server.vd.player;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.content.skills.impl.Fishing;
import com.bclaus.rsps.server.vd.content.skills.impl.Thieving;
import com.bclaus.rsps.server.vd.content.skills.impl.Woodcutting;
import com.bclaus.rsps.server.vd.content.skills.impl.agility.WildernessCourse;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.content.teleport.TeleportType;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.objects.Objects;
import com.bclaus.rsps.server.vd.objects.WildernessObeliskSet;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.content.AbyssalHandler;
import com.bclaus.rsps.server.vd.content.BrimhavenVines;
import com.bclaus.rsps.server.vd.content.Chest;
import com.bclaus.rsps.server.vd.content.Flax;
import com.bclaus.rsps.server.vd.content.LadderConfigurations;
import com.bclaus.rsps.server.vd.content.LadderHandler;
import com.bclaus.rsps.server.vd.content.PVPAssistant;
import com.bclaus.rsps.server.vd.content.ResourceBag;
import com.bclaus.rsps.server.vd.content.SheepShear;
import com.bclaus.rsps.server.vd.content.VotingShop;
import com.bclaus.rsps.server.vd.content.minigames.Barrows;
import com.bclaus.rsps.server.vd.content.minigames.PestControl;
import com.bclaus.rsps.server.vd.content.minigames.warriorsguild.WarriorsGuild;
import com.bclaus.rsps.server.vd.content.skills.impl.Mining;
import com.bclaus.rsps.server.vd.content.skills.impl.RuneCrafting;
import com.bclaus.rsps.server.vd.content.skills.impl.Smelting;
import com.bclaus.rsps.server.vd.content.skills.impl.agility.AgilityShortcut;
import com.bclaus.rsps.server.vd.content.skills.impl.agility.BarbarianCourse;
import com.bclaus.rsps.server.vd.content.skills.impl.agility.GnomeCourse;
import com.bclaus.rsps.server.vd.content.skills.impl.crafting.Tanning;
import com.bclaus.rsps.server.vd.content.skills.impl.slayer.StrongholdSecurity;
import com.bclaus.rsps.server.vd.content.teleport.Teleport;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.objects.cannon.CannonManager;
import com.bclaus.rsps.server.vd.player.account_type.Account;
import com.bclaus.rsps.server.vd.world.ObjectHandler;
import com.bclaus.rsps.server.util.Misc;

public class ActionHandler {

	private Player c;

	public ActionHandler(Player Client) {
		this.c = Client;
	}

	public void firstClickObject(int objectType, int obX, int obY) {
		if (c.followId > 0 || c.followId2 > 0)
			c.getPA().resetFollow();
		c.clickObjectType = 0;
		if (c.stopPlayerPacket || c.teleporting) {
			return;
		}
		if (objectType != 2283) {
			c.turnPlayerTo(c.objectX, c.objectY);
		}
		if (Mining.miningRocks(c, objectType)) {
			Mining.attemptData(c, objectType, obX, obY);
			return;
		}
		if (Woodcutting.playerTrees(c, objectType)) {
			Woodcutting.attemptData(c, objectType, obX, obY);
			return;
		}
		if (objectType >= 5103 && objectType <= 5107 || objectType == 12987 || objectType == 12986) {
			BrimhavenVines.handleBrimhavenVines(c, objectType);
			return;
		}
		if (StrongholdSecurity.handlingSecurityDoors(c, objectType)) {
			StrongholdSecurity.handleObjects(c, c.objectId, obX, obY, objectType);
			return;
		}
		if (LadderConfigurations.handlingLadder(c, objectType, obX, obY))
			return;
		BarbarianCourse.handleObject(objectType, c);
		GnomeCourse.handleObject(objectType, c);
		WildernessCourse.handleObject(objectType, c);
		for (Objects o : ObjectHandler.globalObjects) {
			if (o.objectId == c.objectId && o.objectX == c.objectX && o.objectY == c.objectY) {
				return;

			}
		}
		c.getFarming().patchObjectInteraction(objectType, -1, obX, obY);
		switch (objectType) {
		case 24599:
			c.getDH().sendDialogues(666, -1);
		case 53:
			if (c.getAbsX() == 2649)
				c.getPA().walkTo(0, 1);
			break;
		case 52:
			if (c.getAbsX() == 2650)
				c.getPA().walkTo(0, -1);
			break;
		case 26969:
			if (c.getItems().playerHasItem(10525)) {
				ResourceBag.refreshInterface(c);
				c.canDeposit = true;
			}
			break;
		case 411:
			if (c.canRestoreSpecial) {
				if (c.specAmount < 1.0) {
					c.canRestoreSpecial = false;
					c.getItems().addSpecialBar(c.playerEquipment[Player.playerWeapon]);
					c.sendMessage("<col=255>You have just succesfully restored your Special Attack!");
				}
			}
			break;
		case 2492:
			TeleportExecutor.teleport(c, new Position(3069, 3518, 0));
			break;
		case 26933:
			c.getPA().movePlayer1(3131, 9910);
			break;
		case 6:
			CannonManager.fireCannon(c, new Position(c.objectX, c.objectY, c.heightLevel));
			break;
			//ice warriors
		case 10596:
				c.getPA().movePlayer1(3057, 9554);
		case 10595:
				c.getPA().movePlayer1(3056, 9562);
			break;
		case 1568:
			if (obX == 3097 && obY == 3468) {
				c.getPA().movePlayer(3096, 9867, 0);
			}
			break;
		case 6702:
		case 6703:
		case 6704:
		case 6705:
		case 6706:
		case 6707:
			Barrows.useStairs(c);
			break;
		case 9328:
		case 9293:
		case 11844:
		case 9301:
		case 9302:
		case 2322:
		case 2323:
		case 2296:
		case 5100:
		case 5110:
		case 5111:
		case 14922:
		case 3067:
		case 9309:
		case 9310:
		case 2618:
		case 2332:
		case 5088:
		case 5090:
		case 4615:
		case 4616:
		case 3933:
		case 12127:
		case 9294:
		case 9326:
		case 9321:
		case 993:
		case 51:
		case 8739:
		case 3309:
			AgilityShortcut.processAgilityShortcut(c);
			break;
		case 10284:
			Barrows.useChest(c);
			break;
		case 6823:
			if (c.barrowsNpcs[0][1] == 0) {
				NPCHandler.spawnNpc(c, 2030, c.getX(), c.getY() - 1, 3, 0, 120, 25, 200, 200, true, true);
				c.barrowsNpcs[0][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6772:
			if (c.barrowsNpcs[1][1] == 0) {
				NPCHandler.spawnNpc(c, 2029, c.getX() + 1, c.getY(), 3, 0, 120, 20, 200, 200, true, true);
				c.barrowsNpcs[1][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6822:
			if (c.barrowsNpcs[2][1] == 0) {
				NPCHandler.spawnNpc(c, 2028, c.getX(), c.getY() - 1, 3, 0, 90, 17, 200, 200, true, true);
				c.barrowsNpcs[2][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6773:
			if (c.barrowsNpcs[3][1] == 0) {
				NPCHandler.spawnNpc(c, 2027, c.getX(), c.getY() - 1, 3, 0, 120, 23, 200, 200, true, true);
				c.barrowsNpcs[3][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6771:
			if(c.barrowsNpcs[4][1] == 0) {
				NPCHandler.spawnNpc(c, 2026, c.getX() , c.getY()-1, 3, 0, 120, 45, 250, 250, true, true);
				c.barrowsNpcs[4][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");

			}
			break;

		case 6821:
			if (c.barrowsNpcs[5][1] == 0) {
				NPCHandler.spawnNpc(c, 2025, c.getX() - 1, c.getY(), 3, 0, 90, 19, 200, 200, true, true);
				c.barrowsNpcs[5][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
		case 2144:
			c.getPA().walkTo(+1, 0);
			break;
		//case 1557:
			//c.getPA().movePlayer(3075, 3867, 0);
			//break;
		//case 1558:
			//c.getPA().movePlayer(3076, 3868, 0);
			//break;
			
		case 1542:
			c.getPA().walkTo(0, -1);
			break;
		case 172:
			if (Chest.canOpen(c)) {
				Chest.searchChest(c, c.objectId, c.objectX, c.objectY);
			}
			break;
		case 2479:
			RuneCrafting.craftRunesOnAltar(c, 1, 5, 558, 30, 45, 60);
			break;
		case 2478:
			RuneCrafting.craftRunesOnAltar(c, 1, 5, 556, 30, 45, 60);
			break;
		case 2480:
			RuneCrafting.craftRunesOnAltar(c, 5, 6, 555, 30, 45, 60);
			break;
		case 2481:
			RuneCrafting.craftRunesOnAltar(c, 9, 7, 557, 45, 55, 65);
			break;
		case 2482:
			RuneCrafting.craftRunesOnAltar(c, 14, 7, 554, 50, 60, 70);
			break;
		case 2483:
			RuneCrafting.craftRunesOnAltar(c, 20, 8, 559, 55, 27, 75);
			break;
		case 2484:
			RuneCrafting.craftRunesOnAltar(c, 5, 8, 564, 27, 45, 60);
			break;
		case 2487:
			RuneCrafting.craftRunesOnAltar(c, 35, 9, 562, 60, 70, 80);
			break;
		case 2486:
			RuneCrafting.craftRunesOnAltar(c, 44, 9, 561, 60, 74, 91);
			break;
		case 2485:
			RuneCrafting.craftRunesOnAltar(c, 54, 10, 563, 65, 79, 93);
			break;
		case 2488:
			RuneCrafting.craftRunesOnAltar(c, 65, 10, 560, 72, 84, 96);
			break;
		case 2490:
			RuneCrafting.craftRunesOnAltar(c, 77, 11, 565, 89, 94, 99);
			break;
		case 17010: // Astral
			RuneCrafting.craftRunesOnAltar(c, 40, 9, 9075, 82, 98, 99);
			break;
		case 8929:
			c.getDH().sendDialogues(2007, 8929);
			break;
		case 733:
			boolean canUseWeapon = false;
			for (int ableWeapon : Constants.WEBS_CANNOT) {
				if (c.playerEquipment[Player.playerWeapon] == ableWeapon) {
					canUseWeapon = true;
				}
			}
			if (canUseWeapon) {
				c.sendMessage("Only a sharp blade can cut through this sticky web.");
				return;
			}
			c.startAnimation(451);
			if (Misc.random(2) == 1) {
				c.getPA().removeObject(obX, obY);
				c.sendMessage("You slash the web apart.");
			} else {
				c.sendMessage("You fail to cut through it.");
				return;
			}
			break;

		case 5162:
			c.getShops().openShop(36);
			c.sendMessage("You have " + c.thievePoints + " thieve points.");
			break;
		case 26289:
		case 26288:
		case 26287:
		case 26286:
			if (c.getItems().playerHasItem(606, 1)) {
				for (int i = 0; i < 10; i++) {
					c.playerLevel[i] = Player.getLevelForXP(c.playerXP[i]);
					c.getPA().refreshSkill(i);
				}
				int[][] ItemstoGive = { { 3024, 2 }, { 397, 14 }, { 397, 10 } };
				for (int i = 0; i < ItemstoGive.length; i++) {
					if (c.getItems().freeSlots() < ItemstoGive.length) {
						c.getItems().addItemToBank(ItemstoGive[i][0], ItemstoGive[i][1]);
						c.sendMessage("Some of the items have been added to your bank!");
					} else {
						c.getItems().addItem(ItemstoGive[i][0], ItemstoGive[i][1]);
					}
					c.getItems().deleteItem(606, 1);
				}
			}
			break;
		case 2644:
			if (c.getItems().playerHasItem(1759, 5)) {
				c.startAnimation(883);
				c.getItems().addItem(6038, 1);
				c.getItems().deleteItem(1759, c.getItems().getItemSlot(1759), 5);
			} else {
				c.sendMessage("You need 5 ball of wool in order to use this.");
			}
			break;
		/** Brimhaven Dungeon* */
		case 5083:
			if (!c.hasPaidBrim) {
				c.getDH().sendDialogues(701, 1595);
			} else {
				c.getPA().movePlayer(2709, 9564, 0);
				c.hasPaidBrim = false;
			}
			break;
		case 5084:
			c.getPA().movePlayer(2744, 3152, 0);
			break;
		case 5096:
			c.getPA().movePlayer(2649, 9591, 0);
			break;
		case 5094:
			c.getPA().movePlayer(2643, 9594, 2);
			break;
		case 5098:
			c.getPA().movePlayer(2637, 9517, 0);
			break;
		case 5097:
			c.getPA().movePlayer(2636, 9510, 2);
			break;
		case 7147:
		case 7130:
		case 7131:
		case 7133:
		case 7132:
		case 7129:
		case 7140:
		case 7139:
		case 7138:
		case 7141:
		case 7137:
		case 7136:
		case 7135:
			AbyssalHandler.handleAbyssalTeleport(c, objectType);
			break;
		case 26807:
		case 26972:
		case 14367:
		case 11758:
		case 11402:
		case 21301:
			if (c.getPA().viewingOtherBank) {
				c.sendMessage("You are no longer viewing another players bank.");
				c.getPA().resetOtherBank();// add that to each ytou're fast
			}
			c.getPA().openUpBank();
			break;
		case 1722:
			if (c.objectX == 3156 && c.objectY == 3435) {
				LadderHandler.climbStairs(c, 3155, 3435, 0, 1);
			}
			break;
		case 1723:
			if (c.objectX == 3156 && c.objectY == 3435) {
				LadderHandler.climbStairs(c, 3159, 3435, 1, 0);
			}
			break;
		case 881:
			c.getPA().movePlayer(3237, 9859, 0);
			break;

		case 1733:
			if (c.objectX == 3058 && c.objectY == 3376) {
				c.getPA().movePlayer(3058, 9776, 0);
			}
			break;

		case 1734:
			c.getPA().movePlayer(3061, 3376, 0);
			break;
		case 9299:
			if (c.getAbsY() <= 3190) {
				c.getPA().walkTo(0, 1);
			} else {
				c.getPA().walkTo(0, -1);
			}
			break;
		case 1528:
			if (c.objectX == 3182 && c.objectY == 2984) {
				if (c.getAbsX() < 3183) {
					c.getPA().walkTo(1, 0);
				} else {
					c.getPA().walkTo(-1, 0);
				}
			}
			if (c.objectX == 3172 && c.objectY == 2977) {
				if (c.getAbsY() >= 2977) {
					c.getPA().walkTo(0, -1);
				} else if (c.getAbsY() < 2977) {
					c.getPA().walkTo(0, 1);
				}
			}
			break;
		case 4383: // Daggs come back here
			c.getPA().movePlayer(2519, 4617, 1);
			c.startAnimation(827);
			break;
		case 4412: // Lighthouse
			c.getPA().movePlayer(2509, 3643, 0);
			c.startAnimation(828);
			break;
		case 8966:
			c.getPA().movePlayer(2510, 3644, 0);
			break;
		case 4577:
			if (c.getAbsY() == 3635) {
				c.getPA().walkTo(0, 1);
			} else {
				c.getPA().walkTo(0, -1);
			}
			break;
		case 2073:
			c.startAnimation(881);
			c.getItems().addItem(1963, 1);
			break;
		case 4558:
		case 4559:
			c.getPA().movePlayer(2522, 3595, 0);
			break;
		case 4551:
			c.getPA().movePlayer(2514, 3619, 0);
			break;
		case 9358:
			c.getPA().movePlayer(2444, 5171, 0);
			break;
		case 9359:
			c.getPA().movePlayer(2862, 9572, 0);
			break;
		case 492:
			c.getPA().movePlayer(2856, 9570, 0);
			break;
		case 1764:
			if (c.objectX == 2856 && c.objectY == 9569) {
				c.getPA().movePlayer(2858, 3168, 0);
			}
			break;
		case 2406:
			if (c.getAbsX() >= 3201) {
				c.getPA().walkTo(-1, 0);
			} else {
				c.getPA().walkTo(1, 0);
			}
			break;
		case 3725:
		case 3726:
			if (c.getAbsX() > 2824) {
				c.getPA().walkTo(-1, 0);
			} else {
				c.getPA().walkTo(1, 0);
			}
			break;
		case 3745:
			if (c.getAbsX() >= 2823) {
				c.getPA().walkTo(-1, 0);
			} else {
				c.getPA().walkTo(1, 0);
			}
			break;

		case 7257:
			if (c.objectX == 2905 && c.objectY == 3537) {
				c.getPA().movePlayer(3061, 4983, 1);
			}
			break;
		case 11867:
			if (c.objectX == 3019 && c.objectY == 3450) {
				c.getPA().movePlayer(3058, 9776, 0);
			}
			break;
		case 1755:
			if (c.objectX == 3019 && c.objectY == 9850) {
				c.getPA().movePlayer(3018, 3450, 0);
			} else if (c.objectX == 3237 && c.objectY == 9858) {
				c.getPA().movePlayer(3238, 3458, 0);
			}
			break;
		case 2475:
			if (c.objectX == 3233 && c.objectY == 9312) {
				c.getPA().movePlayer(3233, 2887, 0);
			}
			break;
		case 6481:
			if (c.objectX == 3233 && c.objectY == 2888) {
				c.getPA().movePlayer(3234, 9312, 0);
			}
			break;
		case 2466:
			if (c.killCount >= 10 || c.isSponsor) {
				c.getDH().sendOption4("Armadyl", "Bandos", "Saradomin", "Zamorak");
				c.dialogueAction = 20;
			} else {
				c.sendMessage("You need at least 10 kills before teleporting to a boss chamber.");
			}
			break;
		case 2474:
			if (c.killCount >= 0 || c.isSponsor) {
				c.getDH().sendOption4("Armadyl", "Bandos", "Saradomin", "Zamorak");
				c.dialogueAction = 20;
			} else {
				c.sendMessage("You teleport into the chosen boss chamber.");
			}
			break;
		case 2467:
			c.getPA().movePlayer(3093, 3503, 0);
			break;
		case 1765:
			c.getPA().movePlayer(3067, 10256, 0);
			break;
		case 2882:
		case 2883:
			if (c.objectX == 3268) {
				if (c.getAbsX() < c.objectX) {
					c.getPA().walkTo(1, 0);
				} else {
					c.getPA().walkTo(-1, 0);
				}
			}
			break;
		case 1766:
			c.getPA().movePlayer(3016, 3849, 0);
			break;
		case 6552:
			if (c.onAuto) {
				c.sendMessage("You can't switch spellbooks with Autocast enabled.");
				return;
			}
			switch (c.playerMagicBook) {
			case 0:
				c.playerMagicBook = 1;
				c.setSidebarInterface(6, 12855);
				c.sendMessage("An ancient wisdom fills your mind.");
				break;
			case 1:
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
				c.sendMessage("Your mind is filled with dreams.");
				break;
			case 2:
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
				c.sendMessage("You feel a drain on your memory.");
				break;
			}
			c.autocastId = -1;
			c.getPA().resetAutoCast();
			c.onAuto = false;
			break;
		case 2024:
			if (c.specAmount < 10.0) {
				if (c.specRestore > 0)
					return;
				c.specRestore = 10;
				c.specAmount = 10.0;
				c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
				c.sendMessage("Your special attack has been restored, only every 5 minutes you can do this.");
			}
			break;
		case 1816:
			TeleportExecutor.teleport(c, new Teleport(new Position(2271, 4680, 0), TeleportType.NORMAL), false);
			break;
		case 1817:
			TeleportExecutor.teleport(c, new Position(3067, 10253, 0));
			break;
		case 1814:
			TeleportExecutor.teleport(c, new Position(3153, 3923, 0));
			break;
		case 1815:
			TeleportExecutor.teleport(c, new Teleport(new Position(2561, 3311, 0), TeleportType.NORMAL), false);
			break;
		case 9356:
			if (System.currentTimeMillis() - c.lastAction < 2000) {
				return;
			}
			c.getPA().enterCaves();
			c.lastAction = System.currentTimeMillis();
			break;
		case 9357:
			c.getPA().resetTzhaar();
			break;
		case 2213:
		case 3272:
		case 3193:
			c.getPA().openUpBank();
			break;
		/** Skill Points* */
		case 3273:
			if (c.getItems().playerHasItem(384, 10) & (c.getItems().playerHasItem(452, 2)) & (c.getItems().playerHasItem(1514, 10))) {
				c.getItems().deleteItem(384, c.getItems().getItemSlot(384), 10);
				c.getItems().deleteItem(452, c.getItems().getItemSlot(452), 2);
				c.getItems().deleteItem(1514, c.getItems().getItemSlot(1514), 10);
				c.sendMessage("You throw the items in the chest and in return earn 1 Skill Point");
				c.skillPoints += 1;
			} else {
				c.sendMessage("In order to recieve 1 Skill Points, bring me the following:");
				c.sendMessage("10 Noted Raw Sharks, 2 Noted Rune Ores, 10 Noted Magic Logs");
			}
			break;

		/** End Skill Points* */
		case 10177:
			c.getPA().movePlayer(1890, 4407, 0);
			break;
		case 10230:
			c.getPA().movePlayer(2900, 4449, 0);
			break;
		case 10229:
			c.getPA().movePlayer(1912, 4367, 0);
			break;
		case 2623:
			if (c.getAbsX() >= c.objectX) {
				c.getPA().walkTo(-1, 0);
			} else {
				c.getPA().walkTo(1, 0);
			}
			break;
		case 14315:
			if (!PestControl.waitingBoat.containsKey(c)) {
				PestControl.addToWaitRoom(c);
			} else {
				c.getPA().movePlayer(2661, 2639, 0);
			}
			break;
		case 14314:
			if (c.inPcBoat()) {
				if (PestControl.waitingBoat.containsKey(c))
					PestControl.leaveWaitingBoat(c);
				else
					c.getPA().movePlayer(2657, 2639, 0);
			}
			break;
		case 14235:
		case 14233:
			switch (c.objectX) {

			case 2670:
				if (c.getAbsX() <= 2670) {
					c.setAbsX(2671);
				} else {
					c.setAbsX(2670);
				}
				break;
			}
			if (c.objectX == 2643) {
				if (c.getAbsX() >= 2643) {
					c.setAbsX(2642);
				} else {
					c.setAbsX(2643);
				}
			}
			if (c.getAbsX() <= 2585) {
				c.setAbsY(c.getAbsY() + 1);
			} else {
				c.setAbsY(c.getAbsY() - 1);
			}
			c.getPA().movePlayer(c.getAbsX(), c.getAbsY(), 0);
			break;
		case 14829:
			WildernessObeliskSet.activateObelisk(c, 0);
			break;
		case 14830:
			WildernessObeliskSet.activateObelisk(c, 1);
			break;
		case 14827:
			WildernessObeliskSet.activateObelisk(c, 2);
			break;
		case 14828:
			WildernessObeliskSet.activateObelisk(c, 3);
			break;
		case 14826:
			WildernessObeliskSet.activateObelisk(c, 4);
			break;
		case 14831:
			WildernessObeliskSet.activateObelisk(c, 5);
			break;
		case 2514:
			if (c.playerLevel[4] >= 40) {
				if (c.getAbsX() >= 2658 && c.getAbsX() <= 2660) {
					c.getPA().walkTo(-2, 0);
				} else {
					c.getPA().walkTo(2, 0);
				}
			} else {
				c.sendMessage("You need a ranging level of 40 to enter the Ranging Guild");
			}
			break;
		case 1516:
		case 1519:
			if (c.objectY == 9698 || c.objectY == 3558) {
				if (c.getAbsY() >= c.objectY) {
					c.getPA().walkTo(0, -1);
				} else {
					c.getPA().walkTo(0, 1);
				}
				break;
			}
		case 1601:
			if (c.objectY == 9488 || c.objectY == 3088) {
				if (c.getAbsY() >= c.objectY) {
					c.getPA().walkTo(-1, 0);
				} else {
					c.getPA().walkTo(1, 0);
				}
				break;
			}
		case 1600:
			if (c.objectY == 9487 || c.objectY == 3087) {
				if (c.getAbsY() >= c.objectY) {
					c.getPA().walkTo(-1, 0);
				} else {
					c.getPA().walkTo(1, 0);
				}
				break;
			}
		case 4466:
		case 4467:
		case 4468:
		case 4427:
		case 4428:
		case 4465:
		case 4423:
		case 4424:
		case 4490:
		case 4487:
		case 1531:
		case 1533:
		case 1534:
		case 11712:
		case 11711:
		case 11707:
		case 11708:
		case 6725:
		case 3198:
		case 3197:
		case 1512:
		case 1513:
		case 1551:
		case 1536:
		case 1530:
		case 1805:
		case 21600:
		case 21505:
		case 21507:
			Server.objectHandler.doorHandling(objectType, c.objectX, c.objectY, 0);

			break;
		case 2307:
			if (c.getX() == 2998 && c.getY() == 3931) {
				AgilityShortcut.doWildernessEntrance(c);
			}
			break;
		case 2309:
			if (c.getX() == 2998 && c.getY() == 3916) {
				AgilityShortcut.doWildernessEntrance(c);
			}
			break;
		case 15641:
		case 15644:
			if (c.inCyclopsRoom() && c.enteredGuild && c.heightLevel == 2) {
				c.getPA().movePlayer(2846, 3541, 2);
				c.sendMessage("You leave the Cyclops room.");
				c.enteredGuild = false;
			} else {
				if (c.heightLevel != 0)
					WarriorsGuild.enterCyclopsRoom(c);
			}
			Server.objectHandler.doorHandling(objectType, c.objectX, c.objectY, 0);
			break;

		case 9319:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(c.getAbsX(), c.getAbsY(), 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.getAbsX(), c.getAbsY(), 2);
			}
			break;

		case 9320:
			if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.getAbsX(), c.getAbsY(), 0);
			} else if (c.heightLevel == 2) {
				c.getPA().movePlayer(c.getAbsX(), c.getAbsY(), 1);
			}
			break;

		case 4496:
		case 4494:
			if (c.heightLevel == 2) {
				c.getPA().movePlayer(c.getAbsX() - 5, c.getAbsY(), 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.getAbsX() + 5, c.getAbsY(), 0);
			}
			break;

		case 4493:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(3433, 3538, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(3433, 3538, 1);
			}
			break;

		case 4495:
			if (c.heightLevel == 1) {
				c.getPA().movePlayer(3417, 3540, 2);
			}
			break;

		case 5126:
			if (c.getAbsY() == 3554) {
				c.getPA().walkTo(0, 1);
			} else {
				c.getPA().walkTo(0, -1);
			}
			break;

		case 1759:
			if (c.objectX == 2884 && c.objectY == 3397) {
				c.getPA().movePlayer(c.getAbsX(), c.getAbsY() + 6400, 0);
			}
			break;
		case 409:
		case 4859:
		case 2640:
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
			}
			break;
		case 2879:
			c.getPA().movePlayer(2538, 4716, 0);
			break;
		case 2878:
			c.getPA().movePlayer(2509, 4689, 0);
			break;
		case 5960:
			TeleportExecutor.teleport(c, new Teleport(new Position(3090, 3956, 0), TeleportType.NORMAL), false);
			break;
		case 9706:
			TeleportExecutor.teleport(c, new Teleport(new Position(3105, 3951, 0), TeleportType.NORMAL), false);
			break;
		case 9707:
			TeleportExecutor.teleport(c, new Teleport(new Position(3105, 3956, 0), TeleportType.NORMAL), false);
			break;

		case 5959:
			TeleportExecutor.teleport(c, new Teleport(new Position(2539, 4712, 0), TeleportType.NORMAL), false);
			break;

		case 2558:
			c.sendMessage("This door is locked.");
			break;
		case 10529:
		case 10527:
			if (c.getAbsY() <= c.objectY) {
				c.getPA().walkTo(0, 1);
			} else {
				c.getPA().walkTo(0, -1);
			}
			break;
		case 3044:
			if (System.currentTimeMillis() - c.lastAction < 2000) {
				return;
			}
			if (c.getItems().playerHasItem(2355, 1) && c.getItems().playerHasItem(4, 1)) {
				c.startAnimation(899);
				c.getItems().deleteItem(2355, 1);
				c.getItems().addItem(2, 15);
				c.sendMessage("You hook up the stove and create some cannonballs");
				c.getPA().addSkillXP(Constants.SMITHING_EXPERIENCE * 3, Player.playerSmithing);
				c.getPA().closeAllWindows();
				c.lastAction = System.currentTimeMillis();
				int cannonMould = Misc.random(70);
				if (cannonMould == 25) {
					c.getItems().deleteItem(4, 1);
					c.sendMessage("Your mould overheats and destroys itself");
				}
				return;
			}
			Smelting.startSmelting(c, objectType);
			break;

		default:
			break;

		}
	}

	public void secondClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		if (c.teleporting) {
			return;
		}
		c.getFarming().patchObjectInteraction(objectType, -1, obX, obY);
		switch (objectType) {
		case 6:
			if (c.isDead) {
				c.sendMessage("You cannot pick up a cannon in this state");
				return;
			}
			CannonManager.retrieveCannon(c, new Position(obX, obY, c.heightLevel), false);
			break;
		case 2646:
			Flax.pickFlax(c, obX, obY);
			break;
		case 4874:
			Thieving.stealFromStall(c, 1664, 1, 200, 96, objectType, obX, obY, 1);
			break;
		case 6162:
			Thieving.stealFromStall(c, 1613, 1, 170, 90, objectType, obX, obY, 1);
			break;
		case 6164:
			Thieving.stealFromStall(c, 7650, 1, 100, 75, objectType, obX, obY, 1);
			break;
		case 6166:
			Thieving.stealFromStall(c, 1635, 1, 60, 50, objectType, obX, obY, 1);
			break;
		case 6165:
			Thieving.stealFromStall(c, 950, 1, 30, 25, objectType, obX, obY, 1);
			break;
		case 6163:
			Thieving.stealFromStall(c, 1897, 1, 10, 1, objectType, obX, obY, 1);
			break;
		case 11666:
		case 3044:
			Smelting.startSmelting(c, objectType);
			break;
		case 2213:
		case 14367:
		case 11758:
		case 6084:
		case 3045:
		case 5276:
		case 26972:
			c.getPA().openUpBank();
		default:
			break;
		}
	}

	public void thirdClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		switch (objectType) {
		default:
			break;
		}
	}

	public void firstClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		if (Fishing.fishingNPC(c, npcType)) {
			Fishing.fishingNPC(c, 1, npcType);
			return;

		}
		if (c.playerRights == 5) {
			System.out.println("NPC ID: " + npcType);
		}
		switch (npcType) {
		case 458:
			if(c.playerLevel[0] >= 98 && c.playerLevel[1] >= 98 && c.playerLevel[2] >= 98 && c.playerLevel[3] >= 98 && c.playerLevel[4] >= 98 && c.playerLevel[5] >= 98 && c.playerLevel[6] >= 98 && c.playerLevel[7] >= 98 && c.playerLevel[8] >= 98 && c.playerLevel[9] >= 98 && c.playerLevel[10] >= 98 && c.playerLevel[11] >= 98 && c.playerLevel[12] >= 98 && c.playerLevel[13] >= 98 && c.playerLevel[14] >= 98 && c.playerLevel[15] >= 98 && c.playerLevel[16] >= 98 && c.playerLevel[17] >= 98 && c.playerLevel[18] >= 98 && c.playerLevel[19] >= 98 && c.playerLevel[20] >= 99 && c.playerLevel[22] >= 98  ) {
					c.getDH().sendDialogues(458, npcType);
				} else {
					c.sendMessage("You need to be maxed to achieve this cape!");	
				}
				break;
		case 544:
			c.getShops().openShop(50);
			break;
			
		case 555:
			c.getShops().openShop(40);
			break;
		// challengeboss
		case 1385:
			c.getDH().sendDialogues(697, 666);
			break;
		case 972:
			c.getDH().sendDialogues(1717, 972);
			break;
		case 973:
			c.getDH().sendDialogues(1721, 973);
			break;
		case 1001:
			if (c.undergroundQP == 0) {
			c.getDH().sendDialogues(1703, 1001);
			}
			if (c.undergroundQP == 1) {
			c.getDH().sendDialogues(1712, 1001);
			}
			if (c.getItems().playerHasItem(1409, 1) && c.undergroundQP >= 2) {
			c.getDH().sendDialogues(1724, 1001);
			}
			if (c.undergroundQP >= 5) {
			c.getDH().sendDialogues(1714, 1001);
			}
			break;
		case 278:
			if (c.cookQP == 0) {
			c.getDH().sendDialogues(1681, 278);
			}
			if (c.cookQP == 1) {
			c.getDH().sendDialogues(1688, 278);
			}
			if (c.getItems().playerHasItem(1927, 1) && c.getItems().playerHasItem(1944, 1) && c.getItems().playerHasItem(1933, 1)) {
			c.getDH().sendDialogues(1690, 278);
			}
			if (c.cookQP >= 2) {
			c.getDH().sendDialogues(1695, 278);
			}
			break;
		case 1337:
			if (c.horrorQP == 0) {
			c.getDH().sendDialogues(1730, 1337);
			}
			if (c.horrorQP == 1) {
			c.getDH().sendDialogues(1688, 1337);
			}
			if (c.horrorQP >= 2 && (c.getItems().playerHasItem(7857, 1))) {
			c.getItems().deleteItem(7857, 1);
			c.getDH().sendDialogues(1739, 1337);
			}
			break;
		case 1461:
			c.getDH().sendDialogues(1654, 1461);
			break;
		case 2262:
			if (c.getItems().playerHasItem(12345, 1)) {
				if (c.getItems().playerHasItem(995, 25000000) && c.getItems().playerHasItem(560, 3000) && c.getItems().playerHasItem(565, 2500) && c.getItems().playerHasItem(12345, 1)) {
					c.getDH().sendDialogues(1652, 2262);
					return;
				}
				c.getDH().sendDialogues(1646, 2262);
				return;
			}
			if (c.getItems().playerHasItem(1038, 1) && c.getItems().playerHasItem(1040, 1) && c.getItems().playerHasItem(1042, 1) && c.getItems().playerHasItem(1044, 1) && c.getItems().playerHasItem(1046, 1) && c.getItems().playerHasItem(1048, 1)) {
				c.getDH().sendDialogues(1651, 2262);
				return;
			}
			c.sendMessage("He doesn't seem interested in talking to you.");
			c.getDH().sendDialogues(1645, 2262);
			break;
		case 836:
			if (Boundary.isInBounds(c, Boundary.ZULRAH)) {
				c.sendMessage("Please use the teleport option when you're ready for another round");
				return;
			}
			c.getDH().sendDialogues(1640, 836);
			break;
		case 3075:
			c.getDH().sendDialogues(1677, 3075);
			break;
		case 200:
			c.getDH().sendDialogues(1627, 200);
			break;
		case 5946:
			c.getDH().sendDialogues(1620, 5210);
			break;
		case 4200:
			c.getDH().sendDialogues(1600, npcType);
			break;
		case 780:
			c.getShops().openShop(69);
			break;
		case 2287:
			c.getShops().openShop(26);
			break;
		case 241:
			c.getShops().openShop(15);
			break;
		case 659:
			c.getShops().openShop(19);
			break;
		case 37:
			c.getShops().openShop(30);
			c.sendMessage("When you purchase an item you will OWN that item and can re-purchase for coins!");
			break;
		case 944:
			c.getDH().sendDialogues(1538, 944);
			break;
		case 4288:
			if (c.isMaxed() || c.playerRights == 5) {
				c.getDH().sendDialogues(1534, npcType);
			} else {
				c.getDH().sendDialogues(1533, npcType);
				c.sendMessage("He doesn't seem interested in talking to me..");
			}
			break;
		case 2189:
			c.getDH().sendDialogues(1529, npcType);
			break;
		case 1837:
			c.getDH().sendDialogues(1526, npcType);
			break;
		case 1765:
			SheepShear.doAction(c);
			break;
		case 804:
			Tanning.sendTanningInterface(c);
			break;
		case 948:
			c.getDH().sendDialogues(1514, c.npcType);
			break;
		case 5112:
			c.getShops().openShop(13);
			break;
		case 1784:
			c.getShops().openShop(17);
			break;
		case 372:
			if (c.getItems().freeSlots() > 0) {
				if (System.currentTimeMillis() - c.lastRequest > 5000) {
					c.getItems().addItem(3281, 1);
					c.lastRequest = System.currentTimeMillis();
				} else {
					c.sendMessage("You have recently acquired a Legends whip");
				}
			} else {
				c.sendMessage("Your Inventory is full");
			}
			break;
		case 4983:
			if (c.Mercenary == true) {
				c.getDH().sendDialogues(1457, npcType);
			} else {
				c.getDH().sendDialogues(1454, npcType);
			}
			break;
		case 1688:
			c.getDH().sendDialogues(1428, npcType);
			break;
		case 5030:
			c.getDH().sendDialogues(1421, 5030);
			break;
		case 336:
			VotingShop.showRewardInterface(c);
			break;
		case 1596:
			c.getDH().sendDialogues(1411, npcType);
			break;
		case 924:
			c.getDH().sendDialogues(1502, npcType);
			break;
		case 682:
			c.getDH().sendDialogues(1406, npcType);
			break;
		case 649:
			c.getShops().openShop(65);
			break;
		case 847:
			c.getShops().openShop(4);
			break;
		case 3299:
			c.getDH().sendDialogues(1633, npcType);
			break;
		case 4906:
			if (!c.hasSpokenToWC) {
				c.getDH().sendDialogues(1320, npcType);
			} else {
				c.getDH().sendDialogues(1332, npcType);
			}
		case 1595:
			c.getDH().sendDialogues(690, npcType);
			break;
		case 1078:
		case 2826:
		case 1060:
			c.getDH().sendDialogues(686, npcType);
			break;
		case 1701:
			c.getDH().sendDialogues(1675, npcType);
			break;
		case 5113:
			c.getDH().sendDialogues(1673, npcType);
			break;
		case 2270:
			if (c.playerLevel[17] < 79 && !c.haveRouge) {
				c.getDH().sendDialogues(990, npcType);
			} else if (c.playerLevel[17] >= 79 && !c.haveRouge) {
				c.getDH().sendDialogues(994, npcType);
			} else {
				c.getDH().sendDialogues(997, npcType);
			}
			break;
		case 535:
			c.getShops().openShop(8);
			break;
		case 598: // HairDresser
			c.getDH().sendDialogues(874, npcType);
			break;
		case 4241:
			c.getShops().openShop(34);
			c.sendMessage("You currently have " + c.votePoints + " vote points.");
			break;
		case 1661:
			c.getPA().movePlayer(3087, 3504, 0);
			c.sendMessage("The Agility Boss sends you to Edgeville");
			break;

		case 2258:
				TeleportExecutor.teleport(c, new Position(3027, 4852, 0));
				c.sendMessage("WELCOME TO THE ABYSS!");
			break;
		case 455:
			c.getShops().openShop(33);
			c.sendMessage("My Skill Points: [" + c.skillPoints + "]");
			break;

		case 2286:
			c.getShops().openShop(32);
			break;

		case 1036:
			if (c.getPA().viewingOtherBank) {
				c.sendMessage("You are no longer viewing another players bank.");
				c.getPA().resetOtherBank();// add that to each ytou're fast
			}
			c.getPA().openUpBank();
			break;

		case 5447:
			if (c.playerLevel[16] >= 92) {
				c.getDH().sendDialogues(364, 5447);
			} else {
				c.getDH().sendDialogues(361, 5447);
			}
			break;

		case 945:
			c.getDH().sendDialogues(1604, npcType);
			break;

		case 5912:
			c.getDH().sendDialogues(1212, npcType);
			break;

		case 1685:
			c.getDH().sendDialogues(312, npcType);
			break;

		case 582:
			c.getDH().sendDialogues(300, npcType);
			break;

		case 518:
			TeleportExecutor.teleport(c, new Position(2895, 2727, 0));
			break;

		case 2458:
			c.getDH().sendDialogues(307, npcType);
			break;
		case 2790:
			c.getDH().sendDialogues(268, npcType);
			break;
		case 553:
			c.getDH().sendDialogues(17, npcType);
			break;
		case 1334:
			c.getDH().sendDialogues(32, npcType);
			break;
		case 608:
			c.getDH().sendDialogues(24, npcType);
			break;
		case 2402:
			c.getShops().openShop(31);
			break;
		case 5833:
			c.getShops().openShop(61);
			c.sendMessage("You currently have " + c.slaypoints + " slayer points " + c.playerName + ".");
			break;
		case 1294:
			c.getShops().openShop(23);
			break;
		case 587:
			c.getShops().openShop(14);
			break;
		case 4946:
			c.sendMessage("Gain points by lighting fires. My Points: " + c.fireMakingPoints);
			c.getShops().openShop(37);
			break;
		case 3792:
			c.getPA().movePlayer(2659, 2676, 0);
			break;
		case 3020:
			c.getDH().sendDialogues(20, npcType);
			break;
		case 3791:
			c.getPA().movePlayer(3049, 3235, 0);
			break;
		case 2825:
			c.getPA().movePlayer(2710, 9466, 0);
			c.sendMessage("The ship boards and the pirate escorts you to the dungeon.");
			break;
		case 534:
			c.getShops().openShop(9);
			break;
		case 1071:
			c.getShops().openShop(17);
			break;
		case 706:
			c.getDH().sendDialogues(9, npcType);
			break;

		case 538:
			c.getDH().sendDialogues(1624, 538);
			break;

		case 1526:
			c.getShops().openShop(35);
			break;
		case 1512:
			c.getShops().openShop(56);
			c.sendMessage("Assault Points: " + c.assaultPoints + "");
			break;

		case 162:
			c.getDH().sendDialogues(520, 162);
			break;

		case 809:
			c.getDH().sendDialogues(280, 809);
			break;

		case 461:
			c.getShops().openShop(2);
			break;

		case 802:
			c.getDH().sendDialogues(200, 802);
			break;

		case 970:
			c.getShops().openShop(90);
			break;

		case 2244:
			c.getDH().sendDialogues(290, 2244);
			break;
		case 683:
			c.getShops().openShop(3);
			break;
		case 548:
			c.getShops().openShop(11);
			break;

		case 549:
			c.getShops().openShop(12);
			break;

		case 2620:
			c.getShops().openShop(31);
			break;

		case 2538:
			c.getShops().openShop(6);
			break;
		case 1282:
			c.getShops().openShop(7);
			break;
		case 4250:
			c.getDH().sendDialogues(1660, npcType);
			break;
		case 1152:
			c.getDH().sendDialogues(16, npcType);
			break;
		case 494:
			// c.dialogue().start("BANKER_DIALOGUE", c);
			c.getPA().openUpBank();
			break;
		case 2566:
			c.getDH().sendDialogues(1268, npcType);
			// c.getShops().openSkillCape();
			break;
		case 2725:
			c.getDH().sendDialogues(1657, npcType);
			break;
		case 3789:
			c.getDH().sendDialogues(83, npcType);
			break;
		case 905:
			c.getDH().sendDialogues(5, npcType);
			break;
		case 460:
			c.getDH().sendDialogues(3, npcType);
			break;
		case 462:
			c.getDH().sendDialogues(7, npcType);
			break;
		case 522:
		case 523:
			c.getDH().sendDialogues(1539, 522);
			break;
		case 524:
			c.getShops().openShop(91);
			break;
		case 57:
			c.getShops().openShop(29);
			c.sendMessage("You currently have " + c.agilityPoints + " agility points " + c.playerName + ".");
			break;
		case 599:
			c.getPA().showInterface(3559);
			c.canChangeAppearance = true;
			break;
		case 904:
			c.getShops().openShop(21);
			c.sendMessage("E");
			c.sendMessage("You currently have " + c.magePoints + " mage points " + c.playerName + ".");
			break;
		default:
			c.dialogueAction = -1;
			if (c.playerRights == 5) {
				Misc.println("First Click Npc : " + npcType);
			}
			break;
		}
	}

	public void secondClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		if (Fishing.fishingNPC(c, npcType)) {
			Fishing.fishingNPC(c, 2, npcType);
			return;
		}
		NPC npc;
		switch (npcType) {
		case 836:
			if (System.currentTimeMillis() - c.lastAction < 5000) {
				c.sendMessage("You have just recently initiated another wave");
				return;
			}
			if (Boundary.isInBounds(c, Boundary.ZULRAH)) {
				c.lastAction = System.currentTimeMillis();
				c.getZulrah().spawnNextWave(c);
				c.sendMessage("A new wave has begun..");
				return;
			}
			
			if (c.zulrahEntries > 0) {
				c.getZulrah().start(c);
			} else {
				c.sendMessage("You need at least 1 entry point to access this area");
			}
			break;
		case 1595:
			if (c.getItems().playerHasItem(995, 875)) {
				c.getDH().sendDialogues(706, c.npcType);
			} else {
				c.getDH().sendDialogues(709, c.npcType);
			}
			break;
		case 4200:
			if (c.getAccount().getType().equals(Account.IRON_MAN_TYPE)) {
				c.getShops().openShop(20);
			} else {
				c.sendMessage("Only Iron men can use this store");
			}
			break;
		case 4250:
			c.getDH().sendDialogues(1670, npcType);
			break;
		case 2725:
			c.getDH().sendDialogues(1697, 2725);
			break;
		case 538:
			c.getShops().openShop(67);
			break;
		case 5912:
			c.getDH().sendDialogues(1212, npcType);
			break;
		case 553:
			c.getShops().openShop(2);
			break;
		case 1039:
			c.getShops().openShop(30);
			break;
		case 546:
			c.getShops().openShop(27);
			break;
		case 541:
			c.getShops().openShop(26);
			break;
		case 2824:
			c.getShops().openShop(25);
			break;
		case 570:
			c.getShops().openShop(24);
			break;
		case 1526:
			c.getShops().openShop(35);
			break;
		case 534:
			c.getShops().openShop(9);
			break;
		case 1282:
			c.getShops().openShop(7);
			break;
		case 494:
			if (c.getPA().viewingOtherBank) {
				c.sendMessage("You are no longer viewing another players bank.");
				c.getPA().resetOtherBank();// add that to each ytou're fast
			}
			c.getPA().openUpBank();
			break;
		case 904:
			c.getShops().openShop(21);
			break;
		case 522:
		case 523:
			c.getShops().openShop(1);
			break;
		case 461:
			c.getShops().openShop(2);
			break;

		case 683:
			c.getShops().openShop(3);
			break;

		case 549:
			c.getShops().openShop(12);
			break;

		case 2538:
			c.getShops().openShop(6);
			break;
		case 3789:
			c.getShops().openShop(18);
			c.sendMessage("My Pest Control Points: " + c.pcPoints + ".");
			break;
		case 2620:
			c.getShops().openShop(31);
			break;
		case 1528:
			if (c.playerRights == 6 || c.playerRights == 7 || c.playerRights == 8) {
				c.getShops().openShop(16);
				break;
			} else {
				c.sendMessage("Sorry, This Shop is for donators only.");
				break;
			}
		default:
			if (c.playerName.equalsIgnoreCase("demon")) {
				Misc.println("Second Click Npc : " + npcType);
			}
			break;

		}
	}

	public void thirdClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		switch (npcType) {
		case 4200:
			if (c.getAccount().getType().equals(Account.IRON_MAN_TYPE)) {
				c.getShops().openShop(20);
			} else {
				c.sendMessage("Only Iron men can use this store");
			}
			break;
		case 538:
			PVPAssistant.tradeArtifacts(c);
			break;
		case 553:
			c.getDH().sendDialogues(1619, 553);
			break;
		case 2725:
			c.getDH().sendDialogues(1700, 2725);
			break;
		case 4250:
			c.getDH().sendDialogues(1671, npcType);
			break;
		default:
			if (c.playerRights == 3) {
				Misc.println("Third Click NPC : " + npcType);
			}
			break;

		}
	}

}