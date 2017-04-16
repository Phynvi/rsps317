package com.bclaus.rsps.server.vd.content.skills.impl.crafting;

import java.util.HashMap;
import java.util.Map;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author Acquittal
 **/

public class Crafting {

	private final static int[][] RINGS = { // Ring, Gem, Level, XP
	{ 1635, -1, 5, 15 }, { 1637, 1607, 0, 40 }, { 1639, 1605, 27, 55 }, { 1641, 1603, 34, 70 }, { 1643, 1601, 43, 85 }, { 1645, 1615, 55, 100 }, { 6575, 6573, 67, 115 } };
	private static int[][] NECKLACES = { { 1654, -1, 6, 20 }, { 1656, 1607, 22, 55 }, { 1658, 1605, 29, 60 }, { 1660, 1603, 40, 75 }, { 1662, 1601, 56, 90 }, { 1664, 1615, 72, 105 }, { 6577, 6573, 82, 120 } };
	private static int[][] AMULETS = { { 1673, -1, 8, 30 }, { 1675, 1607, 24, 65 }, { 1677, 1605, 31, 70 }, { 1679, 1603, 50, 85 }, { 1681, 1601, 70, 100 }, { 1683, 1615, 80, 150 }, { 6579, 6573, 90, 165 } };

	private final static int[][] MOULD_INTERFACE_IDS = {
	/* Rings */
	{ 1635, 1637, 1639, 1641, 1643, 1645, 6575 },
	/* Neclece */
	{ 1654, 1656, 1658, 1660, 1662, 1664, 6577 },
	/* amulet */
	{ 1673, 1675, 1677, 1679, 1681, 1683, 6579 }

	};

	public static void mouldInterface(Player c) {
		c.getPA().showInterface(4161);
		/* Rings */
		if (c.getItems().playerHasItem(1592, 1)) {
			for (int i = 0; i < MOULD_INTERFACE_IDS[0].length; i++) {
				c.getPA().sendFrame34(MOULD_INTERFACE_IDS[0][i], i, 4233, 1);
			}
			c.getPA().sendFrame34(1643, 4, 4233, 1);
			c.getPA().sendFrame126("", 4230);
			c.getPA().sendFrame246(4229, 0, -1);
		} else {
			c.getPA().sendFrame246(4229, 120, 1592);
			for (int i = 0; i < MOULD_INTERFACE_IDS[0].length; i++) {
				c.getPA().sendFrame34(-1, i, 4233, 1);
			}
			c.getPA().sendFrame126("You need a ring mould to craft rings.", 4230);
		}
		/* Necklace */
		if (c.getItems().playerHasItem(1597, 1)) {
			for (int i = 0; i < MOULD_INTERFACE_IDS[1].length; i++) {
				c.getPA().sendFrame34(MOULD_INTERFACE_IDS[1][i], i, 4239, 1);
			}
			c.getPA().sendFrame34(1662, 4, 4239, 1);
			c.getPA().sendFrame246(4235, 0, -1);
			c.getPA().sendFrame126("", 4236);
		} else {
			c.getPA().sendFrame246(4235, 120, 1597);
			c.getPA().sendFrame126("You need a necklace mould to craft necklaces", 4236);
			for (int i = 0; i < MOULD_INTERFACE_IDS[1].length; i++) {
				c.getPA().sendFrame34(-1, i, 4239, 1);
			}
		}
		/* Amulets */
		if (c.getItems().playerHasItem(1595, 1)) {
			for (int i = 0; i < MOULD_INTERFACE_IDS[2].length; i++) {
				c.getPA().sendFrame34(MOULD_INTERFACE_IDS[2][i], i, 4245, 1);
			}
			c.getPA().sendFrame34(1681, 4, 4245, 1);
			c.getPA().sendFrame246(4241, 0, -1);
			c.getPA().sendFrame126("", 4242);
		} else {
			c.getPA().sendFrame246(4241, 120, 1595);
			c.getPA().sendFrame126("You need a amulet mould to craft necklaces", 4242);
			for (int i = 0; i < MOULD_INTERFACE_IDS[2].length; i++) {
				c.getPA().sendFrame34(-1, i, 4245, 1);
			}
		}
	}

	public static void mouldItem(Player c, int item, int amount) {
		int done = 0;

		final int GOLD_BAR = 2357;

		boolean isRing = false;
		boolean isNeck = false;
		boolean isAmulet = false;
		int gem = 0;
		int itemAdd = -1;
		int xp = 0;
		int lvl = 1;
		for (int i = 0; i < 7; i++) {
			if (item == RINGS[i][0]) {
				isRing = true;
				itemAdd = RINGS[i][0];
				gem = RINGS[i][1];
				lvl = RINGS[i][2];
				xp = RINGS[i][3];
				break;
			}
			if (item == NECKLACES[i][0]) {
				isNeck = true;
				itemAdd = NECKLACES[i][0];
				gem = NECKLACES[i][1];
				lvl = NECKLACES[i][2];
				xp = NECKLACES[i][3];
				break;
			}
			if (item == AMULETS[i][0]) {
				isAmulet = true;
				itemAdd = AMULETS[i][0];
				gem = AMULETS[i][1];
				lvl = AMULETS[i][2];
				xp = AMULETS[i][3];
				break;
			}
		}
		if (!isRing && !isNeck && !isAmulet) {
			return;
		}
		if (c.playerLevel[Player.playerCrafting] >= lvl) {
			if (c.getItems().getItemName(itemAdd).toLowerCase().contains("gold") && !c.getItems().playerHasItem(GOLD_BAR, 1) || !c.getItems().playerHasItem(GOLD_BAR, 1)) {
				c.sendMessage("You need a Gold bar to make this.");
				return;
			} else if (!c.getItems().playerHasItem(gem, 1) && c.getItems().playerHasItem(GOLD_BAR, 1)) {
				c.sendMessage(getRequiredMessage(c.getItems().getItemName(gem)));
				return;
			}
			c.getPA().removeAllWindows();
			while ((done < amount) && (c.getItems().getItemName(gem).toLowerCase().contains("unarmed") && c.getItems().playerHasItem(GOLD_BAR, 1) || c.getItems().playerHasItem(gem, 1) && c.getItems().playerHasItem(GOLD_BAR, 1))) {
				c.getItems().deleteItem(gem, 1);
				c.getItems().deleteItem(GOLD_BAR, 1);
				c.getItems().addItem(itemAdd, 1);
				c.getPA().addSkillXP(30 * Constants.CRAFTING_EXPERIENCE, Player.playerCrafting);
				c.getPA().refreshSkill(Player.playerCrafting);
				done++;
			}
			if (done == 1) {
				c.sendMessage("You craft the gold and gem together to form a " + c.getItems().getItemName(itemAdd) + ".");
			} else if (done > 1) {
				c.sendMessage("You craft the gold and gem together to form " + done + " " + c.getItems().getItemName(itemAdd) + "'s.");
			}
		} else {
			c.sendMessage("You need a Crafting level of " + lvl + " to craft this.");
			return;
		}
	}

	public static String getRequiredMessage(String item) {
		if (item.startsWith("A") || item.startsWith("E") || item.startsWith("I") || item.startsWith("O") || item.startsWith("U")) {
			return "You need a Gold bar and an " + item + " to make this.";
		} else {
			return "You need a Gold bar and a " + item + " to make this.";
		}
	}

	public enum LeatherCrafting {

		LEATHERVAMBS(1741, 1063, 1, 3, 1), LEATHERCHAPS(1741, 1095, 14, 25, 1), LEATHERBODY(1741, 1129, 18, 27, 1),

		GREENVAMBS(1745, 1065, 57, 62, 1), GREENCHAPS(1745, 1099, 60, 124, 2), GREENBODY(1745, 1135, 63, 186, 3),

		BLUEVAMBS(2505, 2487, 66, 70, 1), BLUECHAPS(2505, 2493, 68, 140, 2), BLUEBODY(2505, 2499, 71, 210, 3),

		REDVAMBS(2507, 2489, 73, 78, 1), REDCHAPS(2507, 2495, 75, 156, 2), REDBODY(2507, 2501, 77, 234, 3),

		BLACKVAMBS(2509, 2491, 79, 86, 1), BLACKCHAPS(2509, 2497, 82, 172, 2), BLACKBODY(2509, 2503, 84, 258, 3);

		private int leatherId, outcome, reqLevel, XP, reqAmt;

		private LeatherCrafting(int leatherId, int outcome, int reqLevel, int XP, int reqAmt) {
			this.leatherId = leatherId;
			this.outcome = outcome;
			this.reqLevel = reqLevel;
			this.XP = XP;
			this.reqAmt = reqAmt;
		}

		public int getLeather() {
			return leatherId;
		}

		public int getOutcome() {
			return outcome;
		}

		public int getReqLevel() {
			return reqLevel;
		}

		public int getXP() {
			return XP;
		}

		public int getReqAmt() {
			return reqAmt;
		}

		private static final Map<Integer, LeatherCrafting> lea = new HashMap<Integer, LeatherCrafting>();

		public static LeatherCrafting forId(int id) {
			return lea.get(id);
		}

		static {
			for (LeatherCrafting l : LeatherCrafting.values()) {
				lea.put(l.getOutcome(), l);
			}
		}

	}

	static int[][] leathers = { { 1741, 1095, 1063, 1129 }, { 1745, 1099, 1065, 1135 }, { 2505, 2493, 2487, 2499 }, { 2507, 2495, 2489, 2501 }, { 2509, 2497, 2491, 2503 } };

	public static void openLeather(Player c, int hide) {
		for (int i = 0; i < leathers.length; i++) {
			if (leathers[i][0] == hide) {
				c.getPA().sendFrame164(8880); // leather
				c.getPA().sendFrame126("What would you like to maked?", 8879);
				c.getPA().sendFrame246(8884, 250, leathers[i][1]); // middle
				c.getPA().sendFrame246(8883, 250, leathers[i][2]); // left
																	// picture
				c.getPA().sendFrame246(8885, 250, leathers[i][3]); // right pic
				c.getPA().sendFrame126("Vambs", 8889);
				c.getPA().sendFrame126("Chaps", 8897);
				c.getPA().sendFrame126("Body", 8893);
			}
		}
		c.craftingLeather = true;
		c.hideId = hide;
	}

	public static void handleLeather(Player c, int item1, int item2) {
		openLeather(c, (item1 == 1733) ? item2 : item1);
	}

	public static void handleCraftingClick(Player c, int clickId) {
		switch (clickId) {
		case 34185: // Vambs
			switch (c.hideId) {
			case 1741:
				craftLeather(c, 1063); // Leather vambs
				break;
			case 1745:
				craftLeather(c, 1065); // Green d'hide vambs
				break;
			case 2505:
				craftLeather(c, 2487); // Blue d'hide vambs
				break;
			case 2507:
				craftLeather(c, 2489); // Red d'hide vambs
				break;
			case 2509:
				craftLeather(c, 2491); // Black d'hide vambs
				break;
			}
			break;
		case 34189:
			switch (c.hideId) {
			case 1741:
				craftLeather(c, 1095); // Leather chaps
				break;
			case 1745:
				craftLeather(c, 1099); // Green d'hide chaps
				break;
			case 2505:
				craftLeather(c, 2493); // Blue d'hide chaps
				break;
			case 2507:
				craftLeather(c, 2495); // Red d'hide chaps
				break;
			case 2509:
				craftLeather(c, 2497); // Black d'hide chaps
				break;
			}
			break;
		case 34193:
			switch (c.hideId) {
			case 1741:
				craftLeather(c, 1129); // Leather body
				break;
			case 1745:
				craftLeather(c, 1135); // Green d'hide body
				break;
			case 2505:
				craftLeather(c, 2499); // Blue d'hide body
				break;
			case 2507:
				craftLeather(c, 2501); // Red d'hide body
				break;
			case 2509:
				craftLeather(c, 2503); // Black d'hide body
				break;
			}
			break;
		}
	}

	public static void craftLeather(Player c, int id) {
		LeatherCrafting lea = LeatherCrafting.forId(id);
		if (lea == null) {
			return;
		}
		if (c.playerLevel[Player.playerCrafting] >= lea.getReqLevel()) {
			if (c.getItems().playerHasItem(lea.getLeather(), lea.getReqAmt())) {
				c.startAnimation(1249);
				c.getItems().deleteItem(lea.getLeather(), lea.getReqAmt());
				c.getItems().addItem(lea.getOutcome(), 1);
				c.getPA().addSkillXP(30 * Constants.CRAFTING_EXPERIENCE, Player.playerCrafting);
				resetCrafting(c);
			} else {
				c.sendMessage("You do not have enough items to craft this item.");
			}
		} else {
			c.sendMessage("You need a crafting level of " + lea.getReqLevel() + " to craft this item.");
		}
		c.getPA().removeAllWindows();
	}

	public static void resetCrafting(Player c) {
		c.craftingLeather = false;
		c.hideId = -1;
	}

}