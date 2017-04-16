/**
 * 
 */
package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.vd.player.Player;

public class VotingShop {

	private static final int BUTTON_IDS[] = { 175207, 175208, 175209, 175210, 175211, 175212, 175213, 175214, 175215, 175216, 175217, 175218, 175219, 175220, 175221 };
	private static final int REQ_POINTS[] = { 10, 40, 50, 5, 1, 10, 50, 1, 1, 50, 1, 1, 1, 1, 1 };
	private static boolean[] clickedButton = { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };
	private static final int[] PENANCE_SET = { 10548, 10551, 10552, 10553, 10555 };
	private static final int[] VOID_SET = { 8839, 11665, 11664, 11663, 8840, 8842 };
	private static final int[][] BARRAGE_SET = { { 555, 300 }, { 560, 200 }, { 565, 100 } };
	private static final int[][] VENGANCE_SET = { { 9075, 200 }, { 560, 100 }, { 557, 500 }, };
	private static int currentSlot = -1;

	public static boolean isButton(Player c, int id) {
		for (int i = 0; i < BUTTON_IDS.length; i++) {
			if (BUTTON_IDS[i] == id || id == 175202) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasRequiredPoints(Player c, int index) {
		if (c.votePoints >= REQ_POINTS[index]) {
			return true;
		}
		return false;
	}

	public static void resetButtons() {
		for (int i = 0; i < clickedButton.length; i++) {
			clickedButton[i] = false;
		}
	}

	public static void clickButton(int buttonId) {
		for (int i = 0; i < BUTTON_IDS.length; i++) {
			if (BUTTON_IDS[i] == buttonId) {
				clickedButton[i] = true;
				currentSlot = i;
			}
		}
	}

	public static int getPurchaseCost() {
		for (int i = 0; i < REQ_POINTS.length; i++) {
			if (currentSlot == i) {
				return REQ_POINTS[i];
			}
		}
		return 1;
	}

	public static void operateButton(Player c, int buttonId) {
		switch (buttonId) {
		case 175207:
			updateDetails(c, "Disabled ATM");
			break;
		case 175208:
			updateDetails(c, "Entire penance set.");
			break;
		case 175209:
			updateDetails(c, "1x Fire Cape.");
			break;
		case 175210:
			updateDetails(c, "500,000 gp.");
			break;
		case 175211:
			updateDetails(c, "25 Big bones");
			break;
		case 175212:
			updateDetails(c, "Disabled atm!");
			break;
		case 175213:
			updateDetails(c, "Entire void set.");
			break;
		case 175214:
			updateDetails(c, "50x Barrage Casts.");
			break;
		case 175215:
			updateDetails(c, "50x Vengance Casts.");
			break;
		case 175216:
			updateDetails(c, "Karis Dagger.");
			break;
		case 175217:
			updateDetails(c, "5x Slayer points.");
			break;
		case 175218:
			updateDetails(c, "5x Pest control points.");
			break;
		case 175219:
			updateDetails(c, "10x Assault points.");
			break;
		case 175220:
			updateDetails(c, "Disabled");
			break;
		case 175221:
			updateDetails(c, "5x Mage points.");
			break;
		case 175202:
			buyReward(c);
			break;
		}
		resetButtons();
		if (buttonId != 17502) {
			clickButton(buttonId);
		}
	}

	private static void buyReward(Player c) {
		if (System.currentTimeMillis() - c.buyFromVoteShopDelay < 3000) {
			c.sendMessage("Please wait 3 seconds before buying anything else.");
			return;
		}
		if (currentSlot == -1) {
			c.sendMessage("You have not chosen anything.");
			return;
		}
		if (!hasRequiredPoints(c, currentSlot)) {
			c.sendMessage("You do not have the required points to purchase this reward.");
			return;
		}
		/*
		 * if (currentSlot == 0) { if (c.barrowsLoot > 0) { c.sendMessage(
		 * "You already have this enabled, you must wait until the time runs up."
		 * ); return; } } if (currentSlot == 5) { if (c.voteExperienceMultiplier
		 * > 0) { c.sendMessage(
		 * "You already have this enabled, you must wait until the time runs up."
		 * ); return; } }
		 */
		if (currentSlot == 1) {
			if (c.getItems().freeSlots() <= 4) {
				c.sendMessage("Sorry but you don't have enough free slots for this set. You need 5.");
				return;
			}
		}
		if (currentSlot >= 2 && currentSlot <= 4 || currentSlot == 9) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("Sorry but you need atleast one free slot for this item.");
				return;
			}
		}
		if (currentSlot == 6) {
			if (c.getItems().freeSlots() <= 5) {
				c.sendMessage("Sorry but you don't have enough free slots for this set. You need 6.");
				return;
			}
		}
		if (currentSlot == 7 || currentSlot == 8) {
			if (c.getItems().freeSlots() <= 2) {
				c.sendMessage("Sorry but you don't have enough free slots for this set. You need 3.");
				return;
			}
		}
		c.buyFromVoteShopDelay = System.currentTimeMillis();
		c.votePoints -= getPurchaseCost();
		addReward(c, currentSlot);
		c.sendMessage("You spend " + getPurchaseCost() + " vote points and obtain a reward for voting! Please vote again soon.");
		updateDetails(c, "Click a reward for further details.");
		currentSlot = -1;
		updatePoints(c);
	}

	private static void addReward(Player c, int slot) {
		switch (slot) {
		/*
		 * case 0: c.barrowsLoot = 6000; c.barrowsLoot2 = true; c.sendMessage(
		 * "<col=255,0,255>You now have a better chance at getting loot from barrows.</col>"
		 * ); break;
		 */
		case 1:
			for (int i = 0; i < PENANCE_SET.length; i++) {
				c.getItems().addItem(PENANCE_SET[i], 1);
			}
			break;
		case 2:
			c.getItems().addItem(6570, 1);
			break;
		case 3:
			c.getItems().addItem(995, 500000);
			break;
		case 4:
			c.getItems().addItem(533, 25);
			break;
		/*
		 * case 5: c.voteExperienceMultiplier = 6000; c.sendMessage(
		 * "You now receive 1.5x more exp in all skills when gaining experience for one hour."
		 * ); break;
		 */
		case 6:
			for (int i = 0; i < VOID_SET.length; i++) {
				c.getItems().addItem(VOID_SET[i], 1);
			}
			break;
		case 7:
			for (int i = 0; i < BARRAGE_SET.length; i++) {
				c.getItems().addItem(BARRAGE_SET[i][0], BARRAGE_SET[i][1]);
			}
			break;
		case 8:
			for (int i = 0; i < VENGANCE_SET.length; i++) {
				c.getItems().addItem(VENGANCE_SET[i][0], VENGANCE_SET[i][1]);
			}
			break;
		case 9:
			c.getItems().addItem(10581, 1);
			break;
		case 10:
			c.slaypoints += 5;
			break;
		case 11:
			c.pcPoints += 5;
			break;
		case 12:
			c.assaultPoints += 10;
			break;
		case 13:
			// c.pkPoints += 5;
			break;
		case 14:
			c.magePoints += 10;
			break;
		}
	}

	private static void updatePoints(Player c) {
		c.getPA().sendFrame126("Vote Points: " + c.votePoints, 45026);
	}

	private static void updateDetails(Player c, String s) {
		c.getPA().sendFrame126(s, 45025);
	}

	public static void showRewardInterface(Player c) {
		c.getDH().sendDialogues(1524, 336);
	}

	public static void showAboutInterface(Player c) {
		c.getPA().showInterface(44750);
	}
}
