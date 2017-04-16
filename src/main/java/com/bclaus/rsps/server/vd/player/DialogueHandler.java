package com.bclaus.rsps.server.vd.player;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.content.skills.impl.slayer.Slayer;
import com.bclaus.rsps.server.vd.player.account_type.Account;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

public class DialogueHandler {

	private Player c;

	public DialogueHandler(Player client) {
		this.c = client;
	}

	/**
	 * Handles all talking
	 * 
	 * @param dialogue
	 *            The dialogue you want to use
	 * @param npcId
	 *            The npc id that the chat will focus on during the chat
	 */
	public void sendDialogues(int dialogue, int npcId) {
		c.talkingNpc = npcId;
		switch (dialogue) {
		case 458:
			sendOption2("Obtain Max cape", "Don't Obtain max cape");
			c.dialogueAction = 458;
			break;
		case 697:
			c.dialogueAction = 697;
			sendOption4("Falador", "Varrock", "Lumbridge","Camelot");
			break;
		case 666:
			sendOption3("Kraken", "Vet'ion", "Scorpia" );
			c.dialogueAction = 666;
			break;
		case 1505:// Donator box
			sendNpcChat2("Are you sure you want", "to become a DemonRsps Regular Donator?", 932, "Fionella");
			c.nextChat = 1506;
			break;
		case 1506:
			sendOption2("Yes", "No");
			c.nextChat = 0;
			c.dialogueAction = 1599;
			break;
		case 1507:
			sendNpcChat2("Are you sure you want", "to become a DemonRsps Extreme Donator?", 932, "Fionella");
			c.nextChat = 1508;
			break;
		case 1508:
			sendOption2("Yes", "No");
			c.nextChat = 0;
			c.dialogueAction = 1600;
			break;
		case 142:
			sendStatement4("You activate the crystal and notice a glimmer from inside", "the center of the crystal. You start to remember a tale", "you heard when you were just a child. It's said the crystal has", "the ability to save a players current posistion in DemonRsps.");
			c.nextChat = 143;
			break;
		case 143:
			sendOption2("Yes", "No");
			c.dialogueAction = 49;
			break;
		case 77:
			sendNpcChat2("Due to lack of participation", "You've gained less points for this round.", c.talkingNpc, "Void Knight");
			c.nextChat = 0;
			break;
		case 78:
			sendNpcChat3("You couldn't take down all the portals in time.", "Please try harder next time, or ask more", "people to join your game.", c.talkingNpc, "Void Knight");
			c.nextChat = 0;
			break;
		case 79:
			sendNpcChat3("Congratulations " + c.playerName + "! you have taken", "down all the portals while keeping the Knight alive", "please accept this reward from us.", c.talkingNpc, "Void Knight");
			c.sendMessage("You have won the Pest Control game!");
			c.nextChat = 0;
			break;
		case 80:
			sendNpcChat2("Do not let the Void Knights health reach 0!", "You can regain health by destroying more monsters", c.talkingNpc, "Void Knight");
			c.sendMessage("You have won the Pest Control game!");
			c.nextChat = 81;
			break;
		case 81:
			sendNpcChat1("NOW GO AND DESTROY THOSE PORTALS!!!", c.talkingNpc, "Void Knight");
			c.nextChat = 0;
			break;
		case 82:
			sendNpcChat1("You call yourself a Knight?", c.talkingNpc, "Void Knight");
			c.nextChat = 0;
			break;
		case 83:
			sendNpcChat1("Hi welcome to Pest Control", c.talkingNpc, "Void Knight");
			c.nextChat = 84;
			break;
		case 84:
			sendNpcChat1("Would you like to open the Armor Shop or Exp Shop?", c.talkingNpc, "Void Knight");
			c.nextChat = 85;
			break;
		case 85:
			sendOption2("Void Knight Armor", "Experience Shop");
			c.dialogueAction = 85;
			break;
		case 0:
			c.talkingNpc = -1;
			c.getPA().removeAllWindows();
			c.nextChat = 0;
			break;
		case 1:
			sendStatement(c, "You found a hidden tunnel! Do you want to enter it?");
			c.dialogueAction = 1;
			c.nextChat = 2;
			break;
		case 2:
			sendOption2("Yea! I'm fearless!", "No way! That looks scary!");
			c.dialogueAction = 1;
			c.nextChat = 0;
			break;
		case 5:
			sendNpcChat4("Hello adventurer...", "My name is Kolodion, the master of this mage bank.", "Would you like to play a minigame in order ", "to earn points towards recieving magic related prizes?", c.talkingNpc, "Kolodion");
			c.nextChat = 6;
			break;
		case 6:
			sendNpcChat4("The way the game works is as follows...", "You will be teleported to the wilderness,", "You must kill mages to recieve points,", "redeem points with the chamber guardian.", c.talkingNpc, "Kolodion");
			c.nextChat = 15;
			break;

		case 11:
			sendNpcChat4("Hello!", "My name is Duradel and I am a master of the slayer skill.", "I can assign you a slayer task suitable to your combat level.", "Would you like a slayer task?", c.talkingNpc, "Duradel");
			c.nextChat = 12;
			break;
		case 12:
			sendOption2("Yes I would like a slayer task.", "No I would not like a slayer task.");
			c.dialogueAction = 5;
			break;
		case 13:
			sendNpcChat4("Hello!", "My name is Duradel and I am a master of the slayer skill.", "I see I have already assigned you a task to complete.", "Would you like me to give you an easier task?", c.talkingNpc, "Duradel");
			c.nextChat = 14;
			break;
		case 14:
			sendOption2("Yes I would like an easier task.", "No I would like to keep my task.");
			c.dialogueAction = 6;
			break;
		case 15:
			sendOption2("Yes I would like to play", "No, sounds too dangerous for me.");
			c.dialogueAction = 7;
			break;
		case 16:
			sendOption2("I would like to fix all my barrows.", "Reset Barrows KC");
			c.dialogueAction = 8;
			break;
		case 17:
			sendOption5("Air", "Mind", "Water", "Earth", "More");
			c.dialogueAction = 10;
			c.dialogueId = 17;
			c.teleAction = -1;
			break;
		case 18:
			sendOption5("Fire", "Body", "Cosmic", "Astral", "More");
			c.dialogueAction = 11;
			c.dialogueId = 18;
			c.teleAction = -1;
			break;
		case 19:
			sendOption5("Nature", "Law", "Death", "Blood", "More");
			c.dialogueAction = 12;
			c.dialogueId = 19;
			c.teleAction = -1;
			break;

		case 520:
			sendNpcChat2("Ah, hello", "I'm a master of Agility", c.talkingNpc, "Gnome Trainer");
			c.nextChat = 521;
			break;
		case 521:
			sendNpcChat2("I have the power to teleport", "you to my Agility Courses", c.talkingNpc, "Gnome Trainer");
			c.nextChat = 523;
			break;
		case 523:
			sendNpcChat1("You have to walk to the Wilderness agility Course", c.talkingNpc, "Gnome Trainer");
			c.nextChat = 524;
			break;
		case 524:
			TeleportExecutor.teleport(c, new Position(2474, 3437, 0));
			break;
		case 200:
			sendNpcChat1("Would you like a skill reset?", c.talkingNpc, "Brother Jered");
			c.nextChat = 211;
			break;
		case 211:
			sendNpcChat1("The current price is 1m per skill, and 7m for all skills", c.talkingNpc, "Brother Jered");
			c.nextChat = 212;
			break;
		case 212:
			sendOption4("Reset Attack", "Reset Strength", "Reset Defence", "More...");
			c.dialogueAction = 42;
			break;
		case 213:
			c.getDH().sendOption5("Reset Range", "Reset Prayer", "Reset Magic", "Reset All Skills", "Back...");
			c.teleAction = 43;
			break;
		case 281:
			c.getShops().openShop(10);
			break;
		case 587:
			c.getShops().openShop(14);
		case 420:
			sendNpcChat2("I can only reset your skill", "once you have removed all armour.", c.talkingNpc, "Brother Jered");
			c.nextChat = 0;
			break;
		case 290:
			sendNpcChat4("Hello there " + c.playerName + "!", " I have the ability to lock your exp!", "Ofcourse, you are able to unlock afterwards", "What would you like me to do?", c.talkingNpc, "Guide");
			c.nextChat = 291;
			break;
		case 291:
			sendOption2("Lock EXP", "Unlock EXP");
			c.dialogueAction = 100;
			break;
		case 292:
			if (c.lockedEXP) {
				sendNpcChat1("Your EXP has been unlocked!", c.talkingNpc, "Guide");
				c.lockedEXP = false;
			} else {
				sendNpcChat1("Your EXP is already unlocked!", c.talkingNpc, "Guide");
			}
			c.nextChat = 0;
			break;
		case 293:
			if (!c.lockedEXP) {
				sendNpcChat1("Your EXP has been locked!", c.talkingNpc, "Guide");
				c.lockedEXP = true;
			} else {
				sendNpcChat1("Your EXP is already locked!", c.talkingNpc, "Guide");
			}
			c.nextChat = 0;
			break;
		case 310:
			sendStatement(c, "Do you wish to be teleported to our Security guide?");
			c.nextChat = 311;
			break;
		case 311:
			sendOption2("Yes", "No");
			c.dialogueAction = 105;
			break;
		case 314:
			sendNpcChat2("Hello and welcome to DemonRsps", "Bank.", c.talkingNpc, "Banker");
			c.nextChat = 315;
			break;
		case 315:
			sendNpcChat2("Would you like for me to set a bank pin?", "or do you just wish to open your bank?", c.talkingNpc, "Banker");
			c.nextChat = 316;
			break;
		case 316:
			sendOption2("Bank Pin", "Open my bank");
			c.dialogueAction = 107;
			break;
		case 317:
			sendPlayerChat1("I would like to set a pin");
			c.nextChat = 318;
			break;
		case 318:
			sendNpcChat1("Are you sure?", c.talkingNpc, "Banker");
			c.nextChat = 319;
			break;
		case 319:
			sendNpcChat2("The only way to remove it is", "by requesting a removal on forums", c.talkingNpc, "Banker");
			c.nextChat = 320;
			break;
		case 320:
			sendNpcChat2("So it is important you write", "it down somewhere, and won't forget it.", c.talkingNpc, "Banker");
			c.nextChat = 321;
			break;
		case 321:
			sendOption2("Yes I'm sure", "I might have to think this through..");
			c.dialogueAction = 108;
			break;
		case 322:
			sendPlayerChat1("I might have to think this through..");
			c.nextChat = 0;
			break;
		case 469:
			sendOption4("Daggonoth King", "Chaos Elemental @bla@[@red@Wilderness@bla@]", "More", "");
			c.dialogueAction = 222;
			break;

		case 686:
			sendNpcChat2("Hi would you like to", "thieve at Rogues Den?", c.talkingNpc, "Harold");
			c.nextChat = 689;
			break;
		case 689:
			sendOption2("Teleport to Rogues Den", "Stay here");
			c.dialogueAction = 227;
			break;
		/** Brimhaven Dialogue **/
		case 698:
			sendPlayerChat1("Can I go through that door please?");
			c.nextChat = 699;
			break;
		case 699:
			sendNpcChat2("Most certainly, but I must charge you the sum of 875", "coins first.", c.talkingNpc, "Saniboch");
			c.nextChat = 700;
			break;
		case 700:
			sendOption3("Ok, here's 875 coins.", "Never mind.", "Why is it worth the entry cost?");
			c.dialogueAction = 230;
			break;
		case 701:
			sendNpcChat1("You can't go in there without paying me!", c.talkingNpc, "Saniboch");
			c.nextChat = 0;
			break;
		case 702:
			sendPlayerChat1("Never mind.");
			c.nextChat = 0;
			break;
		case 703:
			sendPlayerChat1("Why is it worth the entry cost?");
			c.nextChat = 704;
			break;
		case 706:
			sendPlayerChat1("Ok, here's 875 coins.");
			c.nextChat = 707;
			break;
		case 707:
			sendStatement(c, "You give Saniboch 875 coins.");
			c.nextChat = 708;
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 875);
			c.hasPaidBrim = true;
			break;
		case 708:
			sendNpcChat2("Many thanks. You may now pass the door. May your", "death be a glorious one!", c.talkingNpc, "Saniboch");
			c.nextChat = 0;
			break;
		case 709:
			sendPlayerChat1("I don't have the money at the moment.");
			c.nextChat = 710;
			break;
		case 710:
			sendNpcChat2("Well this is a dungeon for the more wealthy discerning", "adventurer, be gone with you riff raff.", c.talkingNpc, "Saniboch");
			c.nextChat = 711;
			break;
		case 711:
			sendPlayerChat2("But you don't even have clothes, how can you seriously", "call anyone riff raff");
			c.nextChat = 712;
			break;
		case 712:
			sendNpcChat1("Hummph.", c.talkingNpc, "Saniboch");
			c.nextChat = 0;
			break;
		case 734:
			sendNpcChat1("Hello, how about we change your appearance?", c.talkingNpc, "Makeover Mage");
			c.nextChat = 0;
			break;
		case 735:
			c.getPA().showInterface(3559);
			c.canChangeAppearance = true;
			break;

		/** Thieve **/
		case 990:
			sendNpcChat1("Welcome to Rouges Den", c.talkingNpc, "Martin");
			c.nextChat = 991;
			break;
		case 991:
			sendNpcChat2("This area is where the smallest of people turn", "into Master Thieves.", c.talkingNpc, "Martin");
			c.nextChat = 992;
			break;
		case 992:
			sendNpcChat2("click the chest north to", "get reward items for your acheivements.", c.talkingNpc, "Martin");
			c.nextChat = 993;
			break;
		case 993:
			sendNpcChat2("Also be sure to speak to me after", "you reach 79 Thieving. I will have a reward for you.", c.talkingNpc, "Martin");
			c.nextChat = 0;
			break;
		case 994:
			sendNpcChat2("Your skills have proven to be", "capable to steal from many greats.", c.talkingNpc, "Martin");
			c.nextChat = 995;
			break;
		case 995:
			sendNpcChat2("Please accept this as a reward from us", "Perhaps one day you'll reach my level technique.", c.talkingNpc, "Martin");
			c.nextChat = 996;
			break;
		case 996:
			final int[] rougeArmour = { 5553, 5554, 5555, 5556, 5557 };
			if (c.getItems().freeSlots() >= 5) {
				for (int i = 0; i < 5; i++) {
					c.getItems().addItem(rougeArmour[i], 1);
				}
				c.haveRouge = true;
				c.getPA().removeAllWindows();
			} else {
				sendNpcChat1("You don't have enough space for this.", c.talkingNpc, "Martin");
				c.nextChat = 0;
			}
			break;
		case 997:
			sendNpcChat2("Click the chest up north to", "get reward items for your acheivements.", c.talkingNpc, "Martin");
			c.nextChat = 993;
			break;
		/** Bank Settings **/
		case 1200:
			sendNpcChat1("Good day. How may I help you?", c.talkingNpc, "Banker");
			c.nextChat = 1201;
			break;
		case 1201:
			sendOption4("I'd like to access my bank account, please", "I'd like to check my my P I N settings.", "What is this place?", "(Remove this message permanently)");
			c.dialogueAction = 251;
			break;
		/** What is this place? **/
		case 1202:
			sendPlayerChat1("What is this place?");
			c.nextChat = 1203;
			break;
		case 1203:
			sendNpcChat2("This is a branch of the bank of DemonRsps. We have", "branches in many towns.", c.talkingNpc, "Banker");
			c.nextChat = 1204;
			break;
		case 1204:
			sendOption2("And what do you do?", "Didn't you used to be called the Bank of Varrock?");
			c.dialogueAction = 252;
			break;
		/** And what do you do? **/
		case 1205:
			sendPlayerChat1("And what do you do?");
			c.nextChat = 1206;
			break;
		case 1206:
			sendNpcChat3("We will look after your items and money for you.", "Leave your valuables with us if you want to keep them", "safe.", c.talkingNpc, "Banker");
			c.nextChat = 0;
			break;
		/** Didn't you used to be called the Bank of Varrock? **/
		case 1207:
			sendPlayerChat1("Didn't you used to be called the Bank of Varrock?");
			c.nextChat = 1208;
			break;
		case 1208:
			sendNpcChat4("Yes we did, but people kept coming into our", "branches outside of Varrock and telling us that our", "signs were wrong. They acted as if we didn't know", "what town we were in or something.", c.talkingNpc, "Banker");
			c.nextChat = 0;
			break;
		/**
		 * Note on P I N. In order to check your "Pin Settings. You must have
		 * enter your Bank Pin first
		 **/
		/** I don't know option for Bank Pin **/
		case 1209:
			sendStartInfo("Since you don't know your P I N, it will be deleted in @red@3 days@bla@. If you", "wish to cancel this change, you may do so by entering your P I N", "correctly next time you attempt to use your bank.", "", "");
			c.nextChat = 0;
			break;

		/** Deleting your P I N **/
		case 1210:
			sendOption2("Yes, I don't need a PIN anymore.", "Didn't you used to be called the Bank of Varrock?");
			break;
		/** Setting a P I N **/
		case 1211:
			sendOption2("Yes, I really want a bank PIN. I will NEVER forget it!", "No, I might forget it!");
			// c.nextChat = 1111;
			break;

		case 1212:
			sendNpcChat1("Hello, what can I do for you today?", c.talkingNpc, "Bank Teller");
			c.nextChat = 1213;
			break;

		case 1213:
			sendOption3("Remove/Add Bank Dialogue", "I wish to make a Bank Pin.", "Delete my Pin");
			c.dialogueAction = 253;
			break;

		case 1214:
			sendNpcChat1("Done, I've notified my bankers to stop speaking with you.", c.talkingNpc, "Bank Teller");
			c.removeBankMessage = true;
			c.nextChat = 0;
			break;

		case 1215:
			sendNpcChat1("I'll tell my bankers you wish to speak to them again.", c.talkingNpc, "Bank Teller");
			c.removeBankMessage = false;
			c.nextChat = 0;
			break;

		case 1216:
			sendNpcChat1("But you already have a PIN!", c.talkingNpc, "Bank Teller");
			c.nextChat = 0;
			break;

		case 1217:
			sendNpcChat2("Please enter your bank PIN first by", "opening a bank.", c.talkingNpc, "Bank Teller");
			c.nextChat = 0;
			break;

		case 1218:
			sendNpcChat2("Please enter your bank PIN first by", "opening a bank.", c.talkingNpc, "Bank Teller");
			c.nextChat = 0;
			break;
		case 1268:
			sendNpcChat1("Hello, would you like to buy a cape?", c.talkingNpc, "");
			c.nextChat = 1267;
			break;

		case 1267:
			sendOption2("Skilling Capes", "Quest Cape");
			c.dialogueAction = 262;
			break;

		case 1262:
			sendNpcChat1("Would you like to buy a Quest Cape for 1,000,000 GP?", c.talkingNpc, "");
			c.nextChat = 1263;
			break;
		case 1263:
			sendOption2("Yes", "No");
			c.dialogueAction = 261;
			break;
		case 1264:
			sendNpcChat1("Here you go.", c.talkingNpc, "");
			if (c.getItems().playerHasItem(995, 1000000)) {
				if (c.getItems().freeSlots() >= 2) {
					c.nextChat = 1265;
				} else {
					c.nextChat = 1266;
				}
			} else {
				c.nextChat = 1269;
			}
			break;
		case 1265:
			if (c.getItems().playerHasItem(995, 1000000)) {
				c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 1000000);
				c.getItems().addItem(9813, 1);
				c.getItems().addItem(9814, 1);
			}
			c.nextChat = 0;
			c.getPA().removeAllWindows();
			break;
		case 1266:
			sendNpcChat1("You don't have enough space", c.talkingNpc, "");
			c.nextChat = 0;
			break;
		case 1269:
			sendNpcChat1("Wait! You don't have enough cash", c.talkingNpc, "");
			c.nextChat = 0;
			break;

		case 1320:
			sendNpcChat1("Why hello there " + c.playerName + " I don't believe we've met before.", c.talkingNpc, "");
			c.nextChat = 1321;
			break;
		case 1321:
			sendNpcChat3("I'm the Woodcutting master of DemonRsps.", "Speak to me if you ever want to", "exchange supplies for WC Points.", c.talkingNpc, "");
			c.hasSpokenToWC = true;
			c.nextChat = 0;
			break;

		/**
		 * Note - Builders Outfit
		 */

		case 1332:
			sendNpcChat2(randomWCLine(Misc.random(3)), "What can I do for you today?", c.talkingNpc, "");
			c.nextChat = 1328;
			break;

		/**
		 * Options
		 * **/

		case 1328:
			sendOption4("Open Shop", "What are WC Points?", "Exchange Points", "Nevermind");
			c.dialogueAction = 230;
			break;

		case 1331:
			if (c.getItems().playerHasItem(1512, 10) && c.getItems().playerHasItem(1520, 10) && c.getItems().playerHasItem(1518, 10) && c.getItems().playerHasItem(1516, 10) && c.getItems().playerHasItem(1514, 10)) {
				c.getItems().deleteItem(1512, c.getItems().getItemSlot(1512), 10);
				c.getItems().deleteItem(1520, c.getItems().getItemSlot(1520), 10);
				c.getItems().deleteItem(1518, c.getItems().getItemSlot(1518), 10);
				c.getItems().deleteItem(1516, c.getItems().getItemSlot(1516), 10);
				c.getItems().deleteItem(1514, c.getItems().getItemSlot(1514), 10);
				c.wcPoints++;
				c.sendMessage("You have gained 1 Woodcutting Point. My WC Points" + c.wcPoints);
				c.getPA().removeAllWindows();
			} else {
				sendStartInfo("Please collect the following items:", "You need 10 Logs, 10 Maple Logs", "10 Willow Logs and 10 Magic and Yew Logs", "Afterward collect your points :)", "Woodcutting Shop");
			}
			break;

		/** Option 1 **/

		case 1330:
			c.getShops().openShop(38); // @todo Replace with Woodcutting Shop.
			break;

		/** Option 4 - Nevermind **/

		case 1329:
			sendPlayerChat1("Nevermind.");
			c.nextChat = 0;
			break;

		/** Option 3 - What are WC Points **/

		case 1322:
			sendPlayerChat1("What are WC Points?");
			c.nextChat = 1323;
			break;

		case 1323:
			sendNpcChat2("Ahah, well WC Points are a new addition to the server", "They simply stand for Woodcutting Points.", c.talkingNpc, "");
			c.nextChat = 1324;
			break;

		case 1324:
			sendNpcChat3("To obtain a Woodcutting Point you must bring me.", "10 Regular Logs, 10 Willow Logs, 10 Maple Logs", "10 Yew Logs and 10 Magic Logs", c.talkingNpc, "");
			c.nextChat = 1325;
			break;

		case 1325:
			sendNpcChat2("Please keep in mind that they all must", "be noted and given to me at the same time.", c.talkingNpc, "");
			c.nextChat = 1326;
			break;

		case 1326:
			sendPlayerChat1("Oh, sounds fair enough.");
			c.nextChat = 1327;
			break;

		case 1327:
			sendNpcChat1("Is there anything else I can do for you?", c.talkingNpc, "");
			c.nextChat = 1328;
			break;
		/**
		 * Welcome to VD
		 */

		case 1355:
			sendOption2("Climb Up", "Climb Down");
			c.dialogueAction = 1355;
			break;

		case 1356:
			sendOption2("Climb Up", "Climb Down");
			c.dialogueAction = 1356;
			break;
		case 1408:
			sendOption2("Burthrope Games Room", "Barbarian Outpost");
			c.dialogueAction = 1408;
			break;

		case 1409:
			sendOption2("Duel Arena", "Castle Wars");
			c.dialogueAction = 1409;
			break;

		case 1410:
			sendOption4("Edgeville", "Canifis", "Karamja", "Al Kharid");
			c.dialogueAction = 1410;
			break;
		case 1411:
			sendNpcChat1("'Ello, and what are you after then?", c.talkingNpc, "Vanakka");
			c.nextChat = 1412;
			break;
		case 1412:
			sendOption4("I need another assignment", "Tell me about Social-Slayer", "What do you have for sale?", "I need a stronger task..");
			c.nextChat = 0;
			c.dialogueAction = 1412;
			break;
		case 1413:
			sendNpcChat1("Your new task is to kill " + c.taskAmount + " " + Slayer.getTaskName(c.slayerTask) + ", good luck " + c.playerName, c.talkingNpc, "Vanakka");
			c.nextChat = 0;
			break;
		case 1414:
			if (Slayer.hasTask(c)) {
				sendNpcChat3("You currently have " + c.taskAmount + " " + Slayer.getTaskName(c.slayerTask) + " left to kill.", "If you would like I could give you an easier task.", "Although if I do this, you won't receive as many points.", c.talkingNpc, "Vanakka");
				c.nextChat = 1415;
			} else {
				Slayer.generateTask(c, null);
			}
			break;
		case 1419:
			sendNpcChat3("Social-Slayer is our new co-operative Slayer assignment", "Ever get bored of Slaying by yourself?", "Invite a friend to slay with you!", c.talkingNpc, "Vanakka");
			c.nextChat = 0;
			break;
		case 346:
			sendNpcChat1("Hello how can i help you", c.talkingNpc, "Mazchna");
			c.nextChat = 347;
			break;
		case 347:
			sendOption2("Where is the location of my task?", "Err, nothing nevermind.");
			c.dialogueAction = 1422;
			c.nextChat = 0;
			break;
		case 1415:
			sendOption2("Yes I would like to cancel my task", "No I would like to keep what i'm hunting");
			c.nextChat = 0;
			c.dialogueAction = 1413;
			break;
		case 1416:
			sendNpcChat2("Sorry but the task you have right now is easy.", "Please come back when you have finsihed it.", c.talkingNpc, "Vanakka");
			c.nextChat = 0;
			break;
		case 1417:
			sendNpcChat1("This requires 30 points, which you don't have yet", c.talkingNpc, "Vanakka");
			c.nextChat = 0;
			break;
		case 1418:
			if (Slayer.hasTask(c)) {
				sendNpcChat1("Your task can be found in the " + Slayer.getLocation(c.slayerTask) + ".", c.talkingNpc, "Vanakka");
			} else {
				sendNpcChat1("You don't have a task.", c.talkingNpc, "Vanakka");
			}
			c.nextChat = 0;
			break;
		case 1420:
			sendOption3("Regular Player 700 xp rate", "Iron man (Select Legend then talk to iron man instructor)", "Legend Mode 50 xp rate");
			c.dialogueAction = 1420;
			break;
		case 1425:
			sendStatement(c, "Would you like to enter the barrows tunnels?");
			c.nextChat = 1426;
			break;
		case 1426:
			sendOption2("Yes", "no");
			c.dialogueAction = 1426;
			c.nextChat = 0;
			break;
		case 1427:
			sendNpcChat4("Hello, my name is Ak-Haranu and i'm in charge of Prestiging.", "Prestiging lets players which have maximum levels", "in Attack, Strength, Defence, Range and Mage", "Proceed to the next level by resetting to level 1.", c.talkingNpc, "Ak-Haranu");
			c.nextChat = 1428;
			break;

		case 1428:// 94
			sendOption3("Proceed to the next prestige level", "Open the Prestige Store", "Change prestige title");
			c.dialogueAction = 1428;
			break;
		case 1429:
			sendOption5("Prestige level interpreted as a Roman Numeral", "The Warrior", "The Fallen", "The Divine", "I changed my mind");
			c.dialogueAction = 1429;
			break;
		case 1430:
			sendOption3("Noobie  - Level 1", "The Fallen - Level 4", "The Fallen - Level 7");
			c.dialogueAction = 2008;
			break;
		case 2007:
			sendOption3("Proceed to the dungeon", "Proceedto the Mining Area", "Leave");
			c.dialogueAction = 2007;
			break;
		case 2009:
			sendOption2("Are you ready to visit the chest room?", "No");
			c.dialogueAction = 2009;
			break;
		case 2017:
			sendOption2("Take a 30 second tutorial. (reccomended)", "Start playing now!");
			c.nextChat = 2018;
			break;
		case 2018:
			//sendNpcChat2("Hello there " + Misc.optimizeText(c.playerName) + ".", "I will tell you about the world of DemonRsps!.", 949, "Beginner Guide");
			//c.nextChat = 2019;
			this.sendNpcChat2("Welcome to DemonRsps!", "Please select your Game mode", 949, "Guide");
			c.getPA().movePlayer(3087, 3501, 0);
			c.finishedBeg = true;
			c.getPA().walkableInterface(-1);
			c.nextChat = 1420;
			break;

		case 2019:
			sendNpcChat2("You have entered a world known as " + Constants.SERVER_NAME + ".", "This world is different than the others you have entered.", 949, "Beginner Guide");
			c.nextChat = 2020;
			break;

		case 2020:
			sendNpcChat2("I'll tell you about the home first.", "The home in this world is EdgeVille.", 949, "Beginner Guide");
			c.nextChat = 2021;
			break;

		case 2021:
			sendNpcChat2("In Edgeville, you can trade with others in the market.", "The shops are located at home, accessed at home. or ::shop", 949, "Beginner Guide");
			c.getPA().movePlayer(3087, 3501, 0);
			c.nextChat = 2023;
			break;

		case 2023:
			this.sendNpcChat3("This is the Skilling Zone.", "However, there are some skills in " + Constants.SERVER_NAME + ",", " that cannot be trained here.", 949, "Beginner Guide");
			c.getPA().movePlayer(2849, 3431, 0);
			c.nextChat = 2024;
			break;

		case 2024:
			sendNpcChat2("There are many unique training zones.", "This is our recommended beginner training zone.", 949, "Beginner Guide");
			c.getPA().movePlayer(2785, 2786, 0);
			c.nextChat = 2025;
			break;

		case 2025:
			sendNpcChat2("This is known as a PVP Area", "These areas are dangerous, but rewarding!", 949, "Beginner Guide");
			c.getPA().movePlayer(3091, 3525, 0);
			c.getPA().walkableInterface(29000);
			c.nextChat = 2026;
			break;
		case 2026:
			sendNpcChat4("There are many unexplained features", "which haven't yet been showed to you", "but now it's time to explore.", "If you have questions use ::ticket", 949, "Beginner guide");
			c.getPA().movePlayer(3087, 3501, 0);
			c.nextChat = 2027;
			break;
		case 2027:
			this.sendNpcChat2("Enjoy playing " + Constants.SERVER_NAME + ", " + Misc.optimizeText(c.playerName) + "!", "Please select your Game mode", 949, "Beginner Guide");
			c.finishedBeg = true;
			c.getPA().walkableInterface(-1);
			c.nextChat = 1420;
			break;
		/**
		 * Warriors guild cyclops room chat.
		 */
		case 1450:
			sendNpcChat2("Good luck in their soldier", "I have released some cyclops!", 4289, "Kamfreena");
			c.nextChat = 1453;
			break;
		case 1453:
			c.sendMessage("You enter the Cyclops room.");
			c.getPA().movePlayer(2847, 3541, 2);
			c.getPA().closeAllWindows();
			/**
			 * We start a new event.
			 */
			Server.getTaskScheduler().schedule(new ScheduledTask(35) {

				@Override
				public void execute() {
					if (c == null || !c.inCyclopsRoom()) {
						this.stop();
						return;
					}
					if (!c.getItems().playerHasItem(8851)) {
						c.sendMessage("You are out of tokens!");
						c.getPA().movePlayer(2846, 3541, 2);
						stop();
						return;
					}
					c.sendMessage("10 of your tokens crumble away.");
					c.getItems().deleteItem2(8851, 10);
					super.stop();
				}

			}.attach(c));
			break;
		case 1454:
			sendNpcChat2("Hi there, Do you think you can help me?", "My base was ambushed by these.. Things..", 4983, "Mercenary");
			c.nextChat = 1455;
			break;
		case 1455:
			sendNpcChat2("If you can defeat them, I heard of a monster lerking behind", "Which drops amazing rewards!", 4983, "Mercenary");
			c.nextChat = 1456;
			break;
		case 1456:
			sendOption2("Ill try my best!", "No, Sorry it sounds too powerful for me!");
			c.dialogueAction = 1500;
			c.nextChat = -1;
			break;
		case 1457:
			sendNpcChat2("Wow i can't believe you did it!", "You have my gratitude thanks!", 4983, "Mercenary");
			c.nextChat = 1458;
			break;
		case 1458:
			sendNpcChat2("As a reward ill send you to the location of the boss!", "Hes extremely powerful, id reccomened a team!", 4983, "Mercenary");
			c.nextChat = 1459;
			break;
		case 1459:
			sendOption2("I'm Ready!", "No thanks");
			c.nextChat = -1;
			c.dialogueAction = 1501;
			break;
		case 1500:
			sendNpcChat1("Hey, there. Think you have what it takes to hunt?", 5110, "Hunter");
			c.nextChat = 1501;
			break;
		case 1501:
			sendOption2("Yeah, i'm up for anything!", "Nah, maybe another time..");
			c.nextChat = -1;
			break;
		case 1502:
			sendNpcChat2("Hey, there " + Misc.formatPlayerName(c.playerName) + " This is my store!", "You can redeem your points here or remove your status!", 924, "Osman");
			c.dialogueAction = 1503;
			c.nextChat = 1503;
			break;
		case 1503:
			sendOption3("Open Donator Store", "I want to change my rank colour!", "Close");
			c.nextChat = -1;
			break;
		case 1504:
			sendNpcChat2("So you're interested in removing your status I see?", "This will cost ya! 5M will do the trick! ", 924, "Osman");
			c.nextChat = 1511;
			break;
		case 1511:
			sendOption2("5M Sure!", "5M Are you crazy! No thanks!");
			c.dialogueAction = 1504;
			c.nextChat = -1;
			break;
		case 1512:
			sendNpcChat2("So you're interested in removing your status i see?", "This will cost ya! 10M will do the trick! ", 924, "Osman");
			c.nextChat = 1513;
			break;
		case 1513:
			sendOption2("10M Sure!", "10M Are you crazy! No thanks!");
			c.dialogueAction = 1505;
			c.nextChat = -1;
			break;
		case 1514:
			sendNpcChat("Good day mate, what can i do for you?", c.npcType, "Popular Miner");
			c.nextChat = 1515;
			break;
		case 1515:
			sendOption2("Can you take me to the mining area?", "Can you show me your store?");
			c.dialogueAction = 1506;
			c.nextChat = -1;
			break;
		case 1516:
			sendNpcChat1("I only speak to true Legend players.. Leave me be.", 454, "Sanfew");
			c.nextChat = -1;
			break;
		case 1517:
			sendNpcChat2("I thought id never see a true warrior!", "What brings you here my friend?", 454, "Crechure");
			c.nextChat = 1518;
			break;
		case 1518:
			sendPlayerChat2("I heard a wise man found a secret ingredient ", "recipe to create the ultimate potion.");
			c.nextChat = 1519;
			break;
		case 1519:
			sendNpcChat1("Damn Kaqemeex has been spreading rumours again...", 454, "Crechure");
			c.nextChat = 1520;
			break;
		case 1520:
			sendNpcChat2("Ill tell you what, you bring me these ingredients..", " 1 Clean-creature and a vial of water...", 454, "Crechure");
			c.nextChat = -1;
			break;
		case 1521:
			sendNpcChat1("I sell Loyalty titles, take your pick!", 924, "Osman");
			c.nextChat = 1522;
			c.dialogueAction = -1;
			break;
		case 1522:
			sendOption4("King", "Queen", "Loyal", "The Beast");
			c.dialogueAction = 1521;
			c.nextChat = -1;
			break;
		case 1523:
			sendOption2("Vote for Vote Points & Cash", "Vote for Double Exp");
			c.dialogueAction = 1522;
			c.nextChat = -1;
			break;
		case 1524:
			sendNpcChat2("Welcome to my store, here you can spend..", "your vote points!", 336, "Da Vinci");
			c.nextChat = 1525;
			break;
		case 1525:
			sendOption4("5 Points: 30 Minutes of Double EXP", "5 Points: 30 Minutes of Drop-rate Increased.", "8 Points: Bundle random selection", "6 Points: 30 Minutes of Double PKP.");
			c.dialogueAction = 1523;
			break;
		case 1526:
			c.dialogueAction = 1524;
			sendOption3("Check for dwarf-cannon pieces.", " Remove cannon status.", "Claim cannon-status");
			break;
		case 1527:
			sendNpcChat2("Would you like to remove your cannon-status for 20m?", "", 1837, "Dwarf");
			c.nextChat = 1528;
			break;
		case 1528:
			sendOption2("Yes please!", "No thanks..");
			c.dialogueAction = 1525;
			break;
		case 1529:
			sendNpcChat3("Hi there petal, I am a real freak for security", "I offer all of the lovely players here", "additional security if you're interested?", 2189, "Security Freak");
			c.nextChat = 1530;
			break;
		case 1530:
			sendNpcChat3("I'll tell you about this additional security, basically..", "When you create an account here with us..", "Your information is stored with us, and we can compare this..", 2189, "Security Freak");
			c.nextChat = 1531;
			break;
		case 1531:
			sendNpcChat3("To your previously logged in location, by comparing the details..", "From previous sessions.. This is a reccomended ", "feature we offer here..", 2189, "Security Freak");
			c.nextChat = 1532;
			break;
		case 1532:
			sendOption2("Enable additional security..", "Disable additional security..");
			c.dialogueAction = 1526;
			break;
		case 1533:
			sendNpcChat2("How dare you approach me ?..", "I am the master, be gone with you!", 4288, "Master");
			c.nextChat = -1;
			break;
		case 1534:
			sendNpcChat3("This is amazing! You have truely become..", "A warrior of c..", "Would you be interested in a completionist cape?", 4288, "Master");
			c.nextChat = 1535;
			break;
		case 1535:
			sendOption3("I'd like the cape please..", "Purchase a graceful set ", "Maybe another time..");
			c.dialogueAction = 1527;
			break;
		case 1536:
			sendNpcChat3("Amazing, the cape will cost you a fee of 50M", "This is because the cape is crafted with the..", "Finest skill and materials..", 4288, "Master");
			c.nextChat = 1537;
			break;
		case 1537:
			sendOption2("Purchase the cape for 50M.", "Come back later");
			c.dialogueAction = 1528;
			break;
		case 1538:
			sendOption4("Teleport to Vet'ion(WILD)", "Teleport to Venenatis(WILD)", "Teleport to Scorpia(WILD)", "Teleport to Callisto(WILD)");
			c.dialogueAction = 1529;
			break;
		case 1539:
			sendNpcChat2("Welcome to my General store, I have plenty..", "Of Supplies, I am also trading Tickets!", c.npcType, "General Store");
			c.nextChat = 1540;
			break;
		case 1540:
			sendOption2("Open the General store", "Enquire about the Ticket");
			c.dialogueAction = 1530;
			break;
		case 1541:
			sendPlayerChat1("What are these tickets you speak of?");
			c.nextChat = 1542;
			break;
		case 1542:
			sendNpcChat2("You can obtain tickets via the mystery box or..", "Killing the almighty Glod, I can exchange these tickets", c.npcType, "General Store");
			c.nextChat = 1543;
			break;
		case 1543:
			sendOption2("Sell one of your tickets.", "Purchase a ticket.");
			c.dialogueAction = 1531;
			break;
		case 1544:
			sendOption2("Claim Abyssal Whip", "Claim 2x Abyssal Whip");
			c.dialogueAction = 1544;
			break;
		case 1545:
			sendNpcChat3("The graceful set will cost you a fee of 250M", "This is because the gear is crafted with the..", "Finest skill and materials..", 4288, "Master");
			c.nextChat = 1546;
			break;
		case 1546:
			sendOption2("Purchase graceful for 250m", "Forget it");
			c.dialogueAction = 1547;
			break;
		/**
		 * Iron man Chat dialogues
		 */
		case 1600:
			if (!c.getAccount().getType().equals(Account.IRON_MAN_TYPE)) {
				sendNpcChat4("Hello " + c.playerName, "I am the iron man instructor. I can assist in", "training new and even veteran iron man accounts.", "Would you like to become an Iron Man?", c.talkingNpc, "Iron Man Instructor");
				c.nextChat = 1601;
			} else {
				sendNpcChat4("Welcome Iron Man " + Misc.capitalize(c.playerName), "", "", "", c.talkingNpc, "Iron Man Instructor");
				c.nextChat = -1;
			}
			break;

		case 1601:
			sendOption2("Yes, I would.", "No, I do not.");
			c.dialogueAction = 200;
			break;
		case 1602:
			sendStatement("@red@Warning: @bla@This will delete all of your items.", "If you click this there is no going back.", "Your bank, inventory, and equipped items will be lost.", "This is meant for players looking to start iron man.", "Do you want to do this?");
			c.nextChat = 1603;
			break;
		case 1603:
			sendOption2("Yes, delete all of my items.", "No, no no no.");
			c.dialogueAction = 201;
			break;
		/**
		 * End
		 */

		/**
		 * FAQ
		 */
		case 1604:
			sendNpcChat3("Hi There " + c.getName() + " I see you're", "Playing DemonRsps as an " + Constants.gameMode(c, c.gameMode), "I can help you get started here with..", 945, "Mr Talk A lot");
			c.nextChat = 1605;
			break;
		case 1605:
			sendNpcChat3("A lot of useful information", "I can assist you with some FAQ, just", "Let me know what you need to know", 945, "Mr Talk a lot");
			c.nextChat = 1606;
			break;
		case 1606:
			sendOption4("Getting started... 'the basics'", "Understanding the Game Modes", "Knowledge is power", "More");
			c.dialogueAction = 1606;
			break;
		case 1607:
			sendNpcChat3("Well, uh.. I can tell you everything i know", "If you still require assistance after", "speaking to me you can request support..", 945, "Mr Know it all");
			c.nextChat = 1608;
			break;
		case 1608:
			sendNpcChat3("We have a help-system in place known as", "The ticket system, you can request", "staff support by typing ::ticket reason", 945, "Mr Know it all");
			c.nextChat = 1609;
			break;
		case 1609:
			sendNpcChat4("This will notify any available staff members", "who will accept your ticket if they're available", "When you have received help you will be", "returned to your previous location", 945, "Mr Know it all");
			c.nextChat = 1610;
			break;
		case 1610:
			sendOption3("Learn the latest money makers", "Tip of the week", "Thanks for your help!");
			c.dialogueAction = 1607;
			break;
		case 1611:
			sendNpcChat4("To begin, I have 4 different methods that I ", "I would be happy to tell you about", "Firstly, Thieving is easy and simple", "To earn your first bucks, you can", 945, "Mr Finance");
			c.nextChat = 1612;
			break;
		case 1612:
			sendNpcChat4("Thieve till you earn around 5M Cash", "Then you can begin crafting uncut gems", "Till you can craft dragonstones", "Cutting dragonstone returns 3k profit per stone", 945, "Mr Finance");
			c.nextChat = 1613;
			break;
		case 1613:
			sendNpcChat4("Method 2, Begin training slayer till 65..", "then begin slaying dust devils which have", "a high tendency to drop worthy", "armour pieces which you can sell for money", 945, "Mr Finance");
			c.nextChat = 1614;
			break;
		case 1614:
			sendNpcChat4("Method 3 : Dragon killing! This is a good", "Money maker, Dragon bones can sell up to", "100k each (estimate) to players", "And around 30k to the general store", 945, "Mr Finance");
			c.nextChat = 1615;
			break;
		case 1615:
			sendNpcChat4("Method 4 : Bossing as always can be a good money maker", "We have plenty of bosses to explore..", "Including PVP Bosses which can drop very", "Valuable rewards, Refer to ::wiki for information.", 945, "Mr Finance");
			c.nextChat = 1616;
			break;
		case 1616:
			sendNpcChat4("Thanks for listening to me ramble on!", "If you think i could improve on any of my information", "Please make sure you post a suggestion", "and I will revise my lines for next time!", 945, "Mr Finance");
			c.nextChat = 1606;
			break;
		/**
		 * End
		 */
		case 1617:
			sendNpcChat2("Please choose which game mode", "You would like to find out about", 945, "Mr Smartass");
			c.nextChat = 1612;
			break;
		case 1618:
			sendOption4("Sir", "Lord", "Legend", "Iron man");
			break;
		case 1619:
			sendOption2("Abbysal Dungeon", "Essence Mine");
			c.dialogueAction = 1608;
			break;
		case 1620:
			sendNpcChat4("Hey there, Welcome.. I can show you a neat", "new tool in Victoious Destiny called the", "Boss tracker, you can track what", "Npcs you have killed here.. and earn rewards!", 5946, "Boss Manager");
			c.nextChat = 1621;
			break;
		case 1621:
			sendOption3("Check your boss tracker now", "Check your slayer tracker now", "Check for rewards");
			c.dialogueAction = 1609;
			break;
		case 1622:
			sendNpcChat4("Blimey, Check you out! What a worthy warrior", "I have a couple of rewards for you here", "and there will be more rewards soon", "When you achieve more kills you get more options!", 5946, "Boss Manager");
			c.nextChat = 1623;
			break;
		case 1623:
			sendOption4("Annihilator", "Boss Hunter", "The Destroyer", "Final Boss");
			c.dialogueAction = 1610;
			break;
		case 1624:
			sendOption3("Store / Weekly stats", "Purchase a PK title..(Redoing)", "Enable/Disable EP Drops");
			c.dialogueAction = 1611;
			break;

		case 1625:
			sendNpcChat4("You need to kill 150 Players for Junior Cadet", "250 for Sargeant", "500 for Commander", "750 for War-chief", 538, "Title guy..");
			c.nextChat = 1626;
			break;
		case 1626:
			sendOption4("Junior Cadet", "Sergeant", "Commander", "War-chief");
			c.dialogueAction = 1612;
			break;
		case 1627:
			sendNpcChat2("Hello there, I'm the market coordinator.", "Is there anything I can help you with?", c.talkingNpc, "Market Coordinator");
			c.nextChat = 1628;
			break;
		case 1628:
			sendOption5("What is the market center?", "How does offer uptime work?", "How do I access the interface?", "", "I'm fine, thank you.");
			c.dialogueAction = 1612;
			break;
		case 1629:
			sendNpcChat4("The market center revolves around a single interface.", "The interface allows players to make global offers.", "When wanting to buy, or sell an item, players can make", "a new offer, or check for an existing one by searching.", c.talkingNpc, "Market Coordinator");
			c.nextChat = 1628;
			break;
		case 1630:
			sendNpcChat4("Every offer has an expiry date. When that date is reached,", "Your offer is deleted from the list, and the slot is available.", "Unfortunately there are only 25 available slots per item.", "By default, you can make an offer for 1 day for free.", c.talkingNpc, "Market Coordinator");
			c.nextChat = 1631;
			break;
		case 1631:
			sendNpcChat4("When 25 offers exist but some are 1 day offers, you can", "buy thier slot by choosing another offer uptime.", "If all 25 offers are paid, you cannor make an offer.", "", c.talkingNpc, "Market Coordinator");
			c.nextChat = 1628;
			break;
		case 1632:
			sendNpcChat4("To work the Market searching system you have to type out the full.", "have to type out the full Name of an", "Item like 'Abyssal Whip' to find the correct item ID.", "Or you can type the Item's ID and it will work the same.", c.talkingNpc, "Market Coordinator");
			c.nextChat = 1628;
			break;
		case 1633:
			sendNpcChat2("Hi, there I can teleport you to my other farming spot", "Would you like to go ?", c.talkingNpc, "Master Farmer");
			c.nextChat = 1634;
			break;
		case 1634:
			sendOption2("Yes please", "No Thanks");
			c.dialogueAction = 1613;
			break;
		case 1635:
			sendNpcChat2("Hi, there we are now featuring boss slayer", "slayer tasks, are you interested?", c.talkingNpc, "Slayer Master");
			c.nextChat = 1636;
			break;
		case 1636:
			sendOption2("Yes please", "No Thanks");
			c.dialogueAction = 1614;
			break;
		case 1637:
			sendOption2("Open Store", "Check weekly stats");
			c.dialogueAction = 1615;
			break;
		case 1640:
			sendNpcChat3("Hi there, Would you like to try your luck", "Against a new boss? It's name", "Is Zulrah, Prepare to fight alone!", c.talkingNpc, "Explorer");
			c.nextChat = 1641;
			break;
		case 1643:
			sendNpcChat3("The fee to gain access to the room is currently 500k.", "You can trade 2.5m for 5 entries", "Or a 5m for 10 Entries", c.talkingNpc, "Shantay");
			c.nextChat = 1644;
			break;
		case 1644:
			sendOption3("Pay 500k", "Pay 2.5m", "Pay 5m");
			c.dialogueAction = 1617;
			break;
		case 1645:
			sendNpcChat3("Welcome to my Dungeon, Minion..", "I don't like you", "Leave me alone..", c.talkingNpc, "Dark Mage");
			break;
		case 1646:
			sendNpcChat2("Ooh, that staff is quite wonderful..", " How can i help you... ss", c.talkingNpc, "Dark Mage");
			c.nextChat = 1647;
			break;
		case 1647:
			sendOption2("Talk about the staff", "Go on your way");
			c.dialogueAction = 1618;
			break;
		case 1648:
			sendPlayerChat2("I heard you can empower my staff?", "For a fee of course..");
			c.nextChat = 1649;
			break;
		case 1649:
			sendNpcChat3("You heard right my kin", " As a god mage, I can give you this wish", "Bring me the following supplies", c.talkingNpc, "Dark Mage");
			c.nextChat = 1650;
			break;
		case 1650:
			sendNpcChat2("25 Million Coins, 3000 Death runes, 2500 Blood runes", "Will be enough my kin", c.talkingNpc, "Dark Mage");
			break;
		case 1651:
			sendNpcChat3("Hmmmm.. Fancy colours...", " I can combine your party hats to create", "A multi-coloured PHAT", c.talkingNpc, "Dark Mage");
			c.nextChat = 1653;
			break;
		case 1652:
			sendOption2("Charge your Trident", "Maybe later");
			c.dialogueAction = 1619;
			break;
		case 1653:
			sendOption2("Combine your PHATS, (UNREVERSABLE)", " Maybe later");
			c.dialogueAction = 1620;
			break;
		case 1654:
			sendNpcChat3("Silly human..", "Jungle demon...", "Try to kill this boss stupid human!", c.talkingNpc, "Elder Guard");
			c.nextChat = 1655;
			break;
		case 1655:
			sendOption2("I want to try killing the Jungle Demon", "Leave");
			c.dialogueAction = 1621;
			break;
		case 1656:
			sendOptionPotato5("Give Max Stats", "Wipe my Stats", "Start Fight Caves", "Open Bank", "Give me a QP cape");
			c.dialogueAction = 1622;
			break;
		case 1657:
			sendNpcChat3("Hello  " + Misc.optimizeText(c.playerName) + " my name is Otto.", "I am an expert in special weaponry repairs.", "What can I do for you?", c.talkingNpc, "Otto Godblessed");
			c.nextChat = 1659;
			break;
		case 1658: //voided
			sendNpcChat2("I could teach you how it's done,", "Although I require some form of payment.", c.talkingNpc, "Otto Godblessed");
			c.nextChat = 1659;
			break;
		case 1659:
			sendOption2("Upgrade Zamorakian spear", "Recharge Trident of the seas");
			c.dialogueAction = 1660;
			break;
		case 1660:
			sendNpcChat2("Welcome to the Lumber Yard  " + Misc.optimizeText(c.playerName) + ".", "I am the Sawmill operator.", c.talkingNpc, "Sawmill Operator");
			c.nextChat = 1661;
			break;
		case 1661:
			sendNpcChat2("This is where you can train the Construction skill.", "Add planks to the table north of here", c.talkingNpc, "Sawmill Operator");
			c.nextChat = 1662;
			break;
		case 1662:
			sendNpcChat2("You can pick up logs from the north east of here", "Then use a saw on them to craft them into Planks.", c.talkingNpc, "Sawmill Operator");
			c.nextChat = 1663;
			break;
		case 1663:
			sendNpcChat1("Climb the fence to the east whenever you're ready.", c.talkingNpc, "Sawmill Operator");
			c.nextChat = 1664;
			break;
		case 1664:
			sendNpcChat1("Do you have any questions?", c.talkingNpc, "Sawmill Operator");
			c.nextChat = 1665;
			break;
		case 1665:
			sendOption5("Where do I get planks?", "How do I get logs?", "Where do I get a Saw?", "Where is the table?", "I'm fine thanks.");
			c.dialogueAction = 1661;
			break;
		case 1666:
			sendNpcChat2("To obtain planks, use a saw on logs.", "Do you want anything else?", c.talkingNpc, "Sawmill Operator");
			c.nextChat = 1665;
			break;
		case 1667:
			sendNpcChat2("Chop down trees or pick up logs north east of here..", "Do you want anything else?", c.talkingNpc, "Sawmill Operator");
			c.nextChat = 1665;
			break;
		case 1668:
			sendNpcChat2("I can sell you a saw if you like, trade me.", "Do you want anything else?", c.talkingNpc, "Sawmill Operator");
			c.nextChat = 1665;
			break;
		case 1669:
			sendNpcChat2("The table is inside the lumber yard behind me..", "Do you want anything else?", c.talkingNpc, "Sawmill Operator");
			c.nextChat = 1665;
			break;
		case 1670:
			sendNpcChat1("You can get planks by Using a Saw on logs.", c.talkingNpc, "Sawmill Operator");
			break;
		case 1671:
			sendNpcChat2("Do you want to buy a Saw from me?", "It will cost 100,000 coins.", c.talkingNpc, "Sawmill Operator");
			c.nextChat = 1672;
			break;
		case 1672:
			sendOption2("Yes please!", "No thanks.");
			c.dialogueAction = 1663;
			break;
		case 1673:
			sendNpcChat2("Hi  " + Misc.optimizeText(c.playerName) + ", would you like to", "go to the hunting grounds?", c.talkingNpc, "Hunting Expert");
			c.nextChat = 1674;
			break;
		case 1674:
			sendOption2("Sure!", "No thanks.");
			c.dialogueAction = 1664;
			break;
		case 1675:
			sendNpcChat2("Hi  " + Misc.optimizeText(c.playerName) + ", would you like to", "go farming?", c.talkingNpc, "Ghost Farmer");
			c.nextChat = 1676;
			break;
		case 1676:
			sendOption2("Sure!", "No thanks.");
			c.dialogueAction = 1665;
			break;
		case 1677:
			sendNpcChat3("Hello " + Misc.optimizeText(c.playerName) + " we found the Legendary Snake!", "Zulrah!!", "Please help us we don't have much left...", c.talkingNpc, "Monk");
			c.nextChat = 1678;
			break;
		case 1678:
			sendStatement("Zulrah is a Solo only boss!", "You will lose items on death.", "Are you SURE you're ready to face her?");
			c.nextChat = 1679;
			break;
		case 1679:
			sendOption2("I will face Zulrah!", "Not right now");
			c.dialogueAction = 1622;
			break;
		case 1680:
			sendStatement("Speak to the cook in", "Lumbridge Castle to start", "the Cook's assistant quest.");
			break;
		case 1681:
			sendNpcChat2("Oh Dear! Oh dear! What am i going to do?", "I've ruined everything!!", c.talkingNpc, "Lumbridge Cook");
			c.nextChat = 1682;
			break;
		case 1682:
			sendPlayerChat2("What's wrong?", "Is there anything I can do to help?");
			c.nextChat = 1683;
			break;
		case 1683:
			sendNpcChat2("I've ruined the Duke's birthday cake!", "I need the correct ingredients for a cake!", c.talkingNpc, "Lumbridge Cook");
			c.nextChat = 1684;
			break;
		case 1684:
			sendPlayerChat1("I will get the ingredients for you.");
			c.nextChat = 1685;
			break;
		case 1685:
			sendNpcChat2("Thank you so much!","I'll need...", c.talkingNpc, "Lumbridge Cook");
			c.nextChat = 1686;
			break;
		case 1686:
			sendNpcChat3("1 Egg","1 Bucket of Milk", "1 Pot of flour", c.talkingNpc, "Lumbridge Cook");
			c.nextChat = 1687;
			break;
		case 1687:
			sendOption2("I can get you those!", "I can't help you.");
			c.dialogueAction = 1798;
			break;
		case 1688:
			sendNpcChat3("You haven't got the ingredients yet?","Come back when you have them!", "remember I need,", c.talkingNpc, "Lumbridge Cook");
			c.nextChat = 1689;
			break;
		case 1689:
			sendNpcChat3("1 Egg","1 Bucket of Milk", "1 Pot of flour", c.talkingNpc, "Lumbridge Cook");
			c.dialogueAction = -1;
			break;
		case 1690:
			sendPlayerChat1("I have the ingredients!");
			c.nextChat = 1691;
			break;
		case 1691:
			sendNpcChat2("Thank you so much!","Here's your reward.", c.talkingNpc, "Lumbridge Cook");
			c.nextChat = 1692;
			break;
		case 1692:
			sendOption2("No problem!", "I'll keep the ingredients for now.");
			c.dialogueAction = 1801;
			break;
		case 1693:
			sendStatement("I've spoken to the cook he needs", "1 Pot of flour, 1 Egg & 1 Bucket of Milk.");
			break;
		case 1695:
			sendNpcChat2("Thank you for helping me!", "The Duke was pleased with his cake.", c.talkingNpc, "Lumbridge Cook");
			c.nextChat = 1696;
			break;
		case 1696:
			sendPlayerChat1("No problem!");
			break;
		case 1697:
			sendNpcChat2("Hello  " + Misc.optimizeText(c.playerName) + " my name is Otto.", "I'll be happy to fix your Trident for you,", c.talkingNpc, "Otto Godblessed");
			c.nextChat = 1698;
			break;
		case 1698:
			sendNpcChat2("It will cost 250 Law runes,", " 1,000 Death runes and 3,000 Water runes.", c.talkingNpc, "Otto Godblessed");
			c.nextChat = 1699;
			break;
		case 1699:
			sendOption2("Yes please!", "No way, That's way too much!");
			c.dialogueAction = 1803;
			break;
		case 1700:
			sendNpcChat2("Hello  " + Misc.optimizeText(c.playerName) + " my name is Otto.", "I can repear your crystal equipment.", c.talkingNpc, "Otto Godblessed");
			c.nextChat = 1701;
			break;
		case 1701:
			sendNpcChat2("It will cost just 1 million coins,", "and the crystal seed.", c.talkingNpc, "Otto Godblessed");
			c.nextChat = 1702;
			break;
		case 1702:
			sendOption2("Yes please!", "No thanks.");
			c.dialogueAction = 1804;
			break;
		case 1703:
			sendPlayerChat1("Hello!");
			c.nextChat = 1704;
			break;
		case 1704:
			sendNpcChat2("Don't bother me right now...", "I'm still looking for the Zamorakian staff...", c.talkingNpc, "Dark Mage");
			c.nextChat = 1705;
			break;
		case 1705:
			sendPlayerChat1("Well maybe I can help you!");
			c.nextChat = 1706;
			break;
		case 1706:
			sendNpcChat2("YOU?! Help me? That's not a bad idea...", "Perhaps I could find it after all...", c.talkingNpc, "Dark Mage");
			c.nextChat = 1707;
			break;
		case 1707:
			sendNpcChat2("Umm... I mean... ", "'We' will find it.", c.talkingNpc, "Dark Mage");
			c.nextChat = 1708;
			break;
		case 1708:
			sendPlayerChat1("I'll get to work finding that Sceptre!");
			c.nextChat = 1709;
			break;
		case 1709:
			sendQuestOption2("I will help you!", "Not right now.");
			c.dialogueAction = 1805;
			break;
		case 1710:
			sendStatement("Speak to the Dark Mage in", "West Ardougne to Start", "the Underground Pass Quest.");
			c.nextChat = 1723;
			break;
		case 1711:
			sendStatement("I am Looking for a Zamorakian Staff", "I should try looking in the Cave", "In the far west of Ardougne.");
			break;
		case 1712:
			sendNpcChat3("You haven't got the staff!","I'll be waiting here adventurer...", "remember I need,", c.talkingNpc, "Dark Mage");
			c.nextChat = 1713;
			break;
		case 1713:
			sendPlayerChat1("I'm starting to regret this...");
			break;
		case 1714:
			sendNpcChat2("Thank you for your assistance ","I will be waiting here to charge that staff.", c.talkingNpc, "Dark Mage");
			c.nextChat = 1715;
			break;
		case 1715:
			sendPlayerChat1("Thank you!");
			break;
		case 1716:
			sendStatement("I entered the cave", "I should try looking in the Cave", "In the far west of Ardougne.");
			break;
		case 1717:
			sendNpcChat2("Hello  " + Misc.optimizeText(c.playerName) + ", my names Koftik.", "I'll be helping through this cavern.", c.talkingNpc, "Koftik");
			c.nextChat = 1718;
			break;
		case 1718:
			sendNpcChat2("I'll need you to get 2 ropes so we can", "pull this bridge down.", c.talkingNpc, "Koftik");
			c.nextChat = 1719;
			break;
		case 1719:
			sendNpcChat3("The bridge will pull itself back up again.", "so if you want to get back again i'll", "need 2 more ropes.", c.talkingNpc, "Koftik");
			c.nextChat = 1720;
			break;
		case 1720:
			sendQuestOption22("Get me over there!", "I'll stay here for awhile.");
			c.dialogueAction = 1807;
			break;
		case 1721:
			sendNpcChat2("Okay " + Misc.optimizeText(c.playerName) + " I've set up a permanent", "rope swing over this gap here.", c.talkingNpc, "Koftik");
			c.nextChat = 1722;
			break;
		case 1722:
			sendPlayerChat1("Thanks Koftik!");
			break;
		case 1723:
			sendStatement("Requirements:", "50 Thieving & 43 Prayer", "Ability to Fight 3 level 91 Strong Demons.");
			break;
		case 1724:
			sendNpcChat3("You've found it!! Well done Adventurer.","You actually managed to get through", "those caves alive!", c.talkingNpc, "Dark Mage");
			c.nextChat = 1725;
			break;
		case 1725:
			sendPlayerChat1("It wasn't easy.");
			c.nextChat = 1726;
			break;
		case 1726:
			sendNpcChat3("Well I suppose you want a reward now?","I'll let you keep that staff but it will", "need recharging often.", c.talkingNpc, "Dark Mage");
			c.nextChat = 1727;
			break;
		case 1727:
			sendNpcChat3("Come back to me with the staff and","i'll recharge it for you.", "Thank you " + Misc.optimizeText(c.playerName) + ".", c.talkingNpc, "Dark Mage");
			c.nextChat = 1728;
			break;
		case 1728:
			sendQuestOption23("Yes.", "No.");
			c.dialogueAction = 1810;
			break;
		case 1729:
			sendStatement("I've found Iban's Staff!", "Now I just need to repair it.", "It needs to be heated up from it's source.");
			break;
		case 1730:
			sendNpcChat2("Hello There! I'm looking for someone,", "to go upstairs and fix the lighthouse.", c.talkingNpc, "Larrissa");
			c.nextChat = 1731;
			break;
		case 1731:
			sendPlayerChat1("I'm sure I can help you out.");
			c.nextChat = 1732;
			break;
		case 1732:
			sendNpcChat1("You really would?", c.talkingNpc, "Larrissa");
			c.nextChat = 1733;
			break;
		case 1733:
			sendNpcChat2("There's something else.. down in the basement,", "We need a warrior to kill it it's huge!!", c.talkingNpc, "Larrissa");
			c.nextChat = 1734;
			break;
		case 1734:
			sendPlayerChat1("I'll help you out!");
			c.nextChat = 1735;
			break;
		case 1735:
			sendStatement("Requirements:", "64 Smithing &", "Ability to kill a level 120 Daggonoth Mother");
			c.nextChat = 1736;
			break;
		case 1736:
			sendQuestOption2("Yes.", "No.");
			c.dialogueAction = 1811;
			break;
		case 1737:
			sendStatement("Larrissa asked me to Fix the lighthouse", "and also defeat whatever is", "disturbing the basement.");
			break;
		case 1738:
			sendStatement("I can start this Quest by speaking to", "Larrissa who is located in the Lighthouse", "west of the Fremminik Provience.");
			break;
		case 1739:
			sendNpcChat2("Well done, Well done!!","You fixed my lighthouse!", c.talkingNpc, "Larrissa");
			c.nextChat = 1740;
			break;
		case 1740:
			sendNpcChat2("and you killed that beast in the basement.", "Here I found 3 books,", c.talkingNpc, "Larrissa");
			c.nextChat = 1741;
			break;
		case 1741:
			sendNpcChat1("Take one if you like!", c.talkingNpc, "Larrissa");
			c.nextChat = 1742;
			break;
		case 1742:
			sendQuestOption31("Zamorak","Guthix", "Saradomin");
			c.dialogueAction = 1812;
			break;
		default:
			break;
		}
	}

	private String randomWCLine(int random) {

		switch (random) {
		case 0:
			return "Ah, welcome back " + c.playerName + ".";
		case 1:
			return "Fine day isn't it?";
		case 2:
			return "How I love sunny days. Don't you?";
		}
		return "Ah, welcome back " + c.playerName + ".";
	}

	/*
	 * Information Box
	 */

	public void sendStartInfo(String text, String text1, String text2, String text3, String title) {
		c.getPA().sendFrame126(title, 6180);
		c.getPA().sendFrame126(text, 6181);
		c.getPA().sendFrame126(text1, 6182);
		c.getPA().sendFrame126(text2, 6183);
		c.getPA().sendFrame126(text3, 6184);
		c.getPA().sendFrame164(6179);
	}

	/*
	 * Options
	 */

	public void sendOption2(String s, String s1) {
		c.getPA().sendFrame126("Select an Option", 2460);
		c.getPA().sendFrame126(s, 2461);
		c.getPA().sendFrame126(s1, 2462);
		c.getPA().sendFrame164(2459);
	}
	
	public void sendQuestOption2(String s, String s1) {
		c.getPA().sendFrame126("Start Quest?", 2460);
		c.getPA().sendFrame126(s, 2461);
		c.getPA().sendFrame126(s1, 2462);
		c.getPA().sendFrame164(2459);
	}
	
	public void sendQuestOption22(String s, String s1) {
		c.getPA().sendFrame126("Give rope?", 2460);
		c.getPA().sendFrame126(s, 2461);
		c.getPA().sendFrame126(s1, 2462);
		c.getPA().sendFrame164(2459);
	}
	
	public void sendQuestOption23(String s, String s1) {
		c.getPA().sendFrame126("Finish Quest?", 2460);
		c.getPA().sendFrame126(s, 2461);
		c.getPA().sendFrame126(s1, 2462);
		c.getPA().sendFrame164(2459);
	}

	public void sendOption3(String s, String s1, String s2) {
		c.getPA().sendFrame126("Select an Option", 2470);
		c.getPA().sendFrame126(s, 2471);
		c.getPA().sendFrame126(s1, 2472);
		c.getPA().sendFrame126(s2, 2473);
		c.getPA().sendFrame164(2469);
	}
	
	public void sendQuestOption31(String s, String s1, String s2) {
		c.getPA().sendFrame126("Which book?", 2470);
		c.getPA().sendFrame126(s, 2471);
		c.getPA().sendFrame126(s1, 2472);
		c.getPA().sendFrame126(s2, 2473);
		c.getPA().sendFrame164(2469);
	}

	public void sendOption4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame126("Select an Option", 2481);
		c.getPA().sendFrame126(s, 2482);
		c.getPA().sendFrame126(s1, 2483);
		c.getPA().sendFrame126(s2, 2484);
		c.getPA().sendFrame126(s3, 2485);
		c.getPA().sendFrame164(2480);
	}

	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		c.getPA().sendFrame126("Select an Option", 2493);
		c.getPA().sendFrame126(s, 2494);
		c.getPA().sendFrame126(s1, 2495);
		c.getPA().sendFrame126(s2, 2496);
		c.getPA().sendFrame126(s3, 2497);
		c.getPA().sendFrame126(s4, 2498);
		c.getPA().sendFrame164(2492);
	}

	public void sendOptionPotato5(String s, String s1, String s2, String s3, String s4) {
		c.getPA().sendFrame126("Op1", 2493);
		c.getPA().sendFrame126(s, 2494);
		c.getPA().sendFrame126(s1, 2495);
		c.getPA().sendFrame126(s2, 2496);
		c.getPA().sendFrame126(s3, 2497);
		c.getPA().sendFrame126(s4, 2498);
		c.getPA().sendFrame164(2492);
	}
	
	public void sendOptionPotato2(String s, String s1, String s2, String s3, String s4) {
		c.getPA().sendFrame126("Op2", 2493);
		c.getPA().sendFrame126(s, 2494);
		c.getPA().sendFrame126(s1, 2495);
		c.getPA().sendFrame126(s2, 2496);
		c.getPA().sendFrame126(s3, 2497);
		c.getPA().sendFrame126(s4, 2498);
		c.getPA().sendFrame164(2492);
	}

	/*
	 * Statements
	 */

	public static void sendStatement(Player c, String s) { // 1 line click here
															// to continue chat
															// box interface
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}

	public void sendStatement(String line1, String line2) {
		c.getPA().sendFrame126(line1, 360);
		c.getPA().sendFrame126(line2, 361);
		c.getPA().sendFrame164(359);
	}

	public void sendStatement(String line1, String line2, String line3) {
		c.getPA().sendFrame126(line1, 364);
		c.getPA().sendFrame126(line2, 365);
		c.getPA().sendFrame126(line3, 366);
		c.getPA().sendFrame164(363);
	}

	public void sendStatement(String line1, String line2, String line3, String line4) {
		c.getPA().sendFrame126(line1, 369);
		c.getPA().sendFrame126(line2, 370);
		c.getPA().sendFrame126(line3, 371);
		c.getPA().sendFrame126(line4, 372);
		c.getPA().sendFrame164(368);
	}

	public void sendStatement(String line1, String line2, String line3, String line4, String line5) {
		c.getPA().sendFrame126(line1, 375);
		c.getPA().sendFrame126(line2, 376);
		c.getPA().sendFrame126(line3, 377);
		c.getPA().sendFrame126(line4, 378);
		c.getPA().sendFrame126(line5, 379);
		c.getPA().sendFrame164(374);
	}

	/*
	 * Npc Chatting
	 */

	public void sendNpcChat(String s, int ChatNpc, String name) {
		c.getPA().sendFrame200(4883, 591);
		c.getPA().sendFrame126(name, 4884);
		c.getPA().sendFrame126(s, 4885);
		c.getPA().sendFrame75(ChatNpc, 4883);
		c.getPA().sendFrame164(4882);
	}

	public void sendNpcChat1(String s, int ChatNpc, String name) {
		c.getPA().sendFrame200(4883, 591);
		c.getPA().sendFrame126(name, 4884);
		c.getPA().sendFrame126(s, 4885);
		c.getPA().sendFrame75(ChatNpc, 4883);
		c.getPA().sendFrame164(4882);
	}

	public void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		c.getPA().sendFrame200(4888, 591);
		c.getPA().sendFrame126(name, 4889);
		c.getPA().sendFrame126(s, 4890);
		c.getPA().sendFrame126(s1, 4891);
		c.getPA().sendFrame75(ChatNpc, 4888);
		c.getPA().sendFrame164(4887);
	}

	private void sendNpcChat3(String s, String s1, String s2, int ChatNpc, String name) {
		c.getPA().sendFrame200(4894, 591);
		c.getPA().sendFrame126(name, 4895);
		c.getPA().sendFrame126(s, 4896);
		c.getPA().sendFrame126(s1, 4897);
		c.getPA().sendFrame126(s2, 4898);
		c.getPA().sendFrame75(ChatNpc, 4894);
		c.getPA().sendFrame164(4893);
	}

	private void sendNpcChat4(String s, String s1, String s2, String s3, int ChatNpc, String name) {
		c.getPA().sendFrame200(4901, 591);
		c.getPA().sendFrame126(name, 4902);
		c.getPA().sendFrame126(s, 4903);
		c.getPA().sendFrame126(s1, 4904);
		c.getPA().sendFrame126(s2, 4905);
		c.getPA().sendFrame126(s3, 4906);
		c.getPA().sendFrame75(ChatNpc, 4901);
		c.getPA().sendFrame164(4900);
	}

	/*
	 * Player Chating Back
	 */

	public void sendPlayerChat1(String s) {
		c.getPA().sendFrame200(969, 591);
		c.getPA().sendFrame126(c.playerName, 970);
		c.getPA().sendFrame126(s, 971);
		c.getPA().sendFrame185(969);
		c.getPA().sendFrame164(968);
	}

	public void sendPlayerChat2(String s, String s1) {
		c.getPA().sendFrame200(974, 591);
		c.getPA().sendFrame126(c.playerName, 975);
		c.getPA().sendFrame126(s, 976);
		c.getPA().sendFrame126(s1, 977);
		c.getPA().sendFrame185(974);
		c.getPA().sendFrame164(973);
	}

	public void sendItemChat1(String header, String one, int item, int zoom) {
		c.getPA().sendFrame200(307, 591);
		c.getPA().sendFrame126(one, 308);
		c.getPA().sendFrame126(header, 4885);
		c.getPA().sendFrame246(307, zoom, item);
		c.getPA().sendFrame164(306);
		c.nextChat = 0;
	}

	public void sendItemChat2(String header, String one, String two, int item, int zoom) {
		c.getPA().sendFrame200(311, 591);
		c.getPA().sendFrame126(two, 312);
		c.getPA().sendFrame126(one, 313);
		c.getPA().sendFrame126(header, 4885);
		c.getPA().sendFrame246(311, zoom, item);
		c.getPA().sendFrame164(310);
		c.nextChat = 0;
	}

	public void sendItemChat3(String header, String one, String two, String three, int item, int zoom) {
		c.getPA().sendFrame246(4894, zoom, item);
		c.getPA().sendFrame126(header, 4895);
		c.getPA().sendFrame126(one, 4896);
		c.getPA().sendFrame126(two, 4897);
		c.getPA().sendFrame126(three, 4898);
		c.getPA().sendFrame164(4893);
	}

	public void sendStatement4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame126(s, 369);
		c.getPA().sendFrame126(s1, 370);
		c.getPA().sendFrame126(s2, 371);
		c.getPA().sendFrame126(s3, 372);
		c.getPA().sendFrame126("Click here to continue", 373);
		c.getPA().sendFrame164(368);
	}

	public void sendItemChat4(String header, String one, String two, String three, String four, int item, int zoom) {
		c.getPA().sendFrame246(4901, zoom, item);
		c.getPA().sendFrame126(header, 4902);
		c.getPA().sendFrame126(one, 4903);
		c.getPA().sendFrame126(two, 4904);
		c.getPA().sendFrame126(three, 4905);
		c.getPA().sendFrame126(four, 4906);
		c.getPA().sendFrame164(4900);
	}
}
