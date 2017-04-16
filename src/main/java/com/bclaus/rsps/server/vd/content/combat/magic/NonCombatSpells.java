package com.bclaus.rsps.server.vd.content.combat.magic;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.player.Player;

public class NonCombatSpells extends MagicRequirements {

	public static void attemptDate(Player c, int action) {
		switch (action) {
		case 4135:
			bonesToBase(c, 15, new int[] { EARTH, WATER, NATURE }, new int[] { 2, 2, 1 }, new int[] { 526, 1963 });
			break;
		case 62005:
			bonesToBase(c, 60, new int[] { NATURE, WATER, EARTH }, new int[] { 2, 4, 4 }, new int[] { 526, 6883 });
			break;
		case 118034:
			plankMake(c, 86, new int[] { NATURE, WATER, EARTH }, new int[] { 3, 15, 1 }, new int[] { 1511, 960 });
			break;
		}
	}

	private static void bonesToBase(Player c, int levelReq, int[] runes, int[] amount, int[] item) {
		if (!hasRequiredLevel(c, levelReq)) {
			c.sendMessage("You need to have a magic level of levelReq to cast this spell.");
			return;
		}
		if (!hasRunes(c, new int[] { runes[0], runes[1], runes[2] }, new int[] { amount[0], amount[1], amount[2] })) {
			return;
		}
		if ((!c.getItems().playerHasItem(item[0], 1))) {
			c.sendMessage("You need some " + c.getItems().getItemName(item[0]) + " to cast this spell!");
			return;
		}
		c.getItems().replaceItem(c, item[0], item[1]);
		c.gfx100(141);
		c.startAnimation(722);
		c.getPA().addSkillXP(100 * c.getItems().getItemAmount(item[0]), 6);
		c.sendMessage("You use your magic power to convert bones into " + c.getItems().getItemName(item[1]).toLowerCase().toLowerCase() + "" + (item[1] != 1963 ? ("e") : ("")) + "s!");
		c.getCombat().resetPlayerAttack();
	}

	public static void superHeatItem(Player c, int itemID) {
		if (!hasRequiredLevel(c, 65)) {
			c.sendMessage("You need to have a magic level of 65 to cast Bake pie.");
			return;
		}
		if (!hasRunes(c, new int[] { FIRE, NATURE }, new int[] { 4, 2 })) {
			return;
		}
		int[][] data = { { 2317, 1, -1, 1, 2323, 53 }, // TIN
		};
		for (int i = 0; i < data.length; i++) {
			if (itemID == data[i][0]) {
				if (!c.getItems().playerHasItem(data[i][2], data[i][3])) {
					c.sendMessage("You haven't got enough " + c.getItems().getItemName(data[i][2]).toLowerCase() + " to cast this spell!");
					return;
				}
				c.getItems().deleteItem(FIRE, 4);
				c.getItems().deleteItem(NATURE, 1);
				c.getItems().deleteItem(itemID, c.getItems().getItemSlot(itemID), 1);
				for (int lol = 0; lol < data[i][3]; lol++) {
					c.getItems().deleteItem(data[i][2], c.getItems().getItemSlot(data[i][2]), 1);
				}
				c.getItems().addItem(data[i][4], 1);
				c.getPA().addSkillXP(Constants.SMITHING_EXPERIENCE * 8, Player.playerSmithing);
				c.startAnimation(725);
				c.gfx100(148);
				c.getPA().sendFrame106(6);
				return;
			}
		}
		c.sendMessage("You can only cast superheat item on ores!");
	}
	
	public static void bakePie(Player c, int itemID) {
		if (!hasRequiredLevel(c, 65)) {
			c.sendMessage("You need to have a magic level of 65 to cast Bake Pie.");
			return;
		}
		if (!hasRunes(c, new int[] { ASTRAL, FIRE, WATER }, new int[] { 1, 5, 4 })) {
			return;
		}
		int[][] data = { { 2317, 1, -1, 1, 2323, 53 }, // Apple Pie
				{ 7944, 1, -1, 1, 7946, 53 }, // Monkfish
				{ 2319, 1, -1, 1, 2325, 53 }, // Meat pie
				{ 2321, 1, -1, 1, 2327, 53 }, // Meat pie
		};
		for (int i = 0; i < data.length; i++) {
			if (itemID == data[i][0]) {
				if (!c.getItems().playerHasItem(data[i][2], data[i][3])) {
					c.sendMessage("You haven't got a pie to cast this spell!");
					return;
				}
				c.getItems().deleteItem(FIRE, 5);
				c.getItems().deleteItem(ASTRAL, 1);
				c.getItems().deleteItem(WATER, 4);
				c.getItems().deleteItem(itemID, c.getItems().getItemSlot(itemID), 1);
				for (int lol = 0; lol < data[i][3]; lol++) {
					c.getItems().deleteItem(data[i][2], c.getItems().getItemSlot(data[i][2]), 1);
				}
				c.getItems().addItem(data[i][4], 1);
				c.getPA().addSkillXP(Constants.COOKING_EXPERIENCE * 8, Player.playerCooking);
				c.getPA().addSkillXP(16000, 6);
				c.startAnimation(4413);
				c.gfx100(746);
				c.getPA().sendFrame106(6);
				return;
			}
		}
		c.sendMessage("You can only cast Bake pie on pies or monkfish!");
	}
	
	private static void plankMake(Player c, int levelReq, int[] runes, int[] amount, int[] item) {
		if (!hasRequiredLevel(c, levelReq)) {
			c.sendMessage("You need to have a magic level of 86 to cast Plank Make.");
			return;
		}
		if (!hasRunes(c, new int[] { runes[0], runes[1], runes[2] }, new int[] { amount[0], amount[1], amount[2] })) {
			return;
		}
		if ((!c.getItems().playerHasItem(item[0], 1))) {
			c.sendMessage("You need some " + c.getItems().getItemName(item[0]) + " to cast this spell!");
			return;
		}
		c.getItems().replaceItem(c, item[0], item[1]);
		c.gfx100(141);
		c.startAnimation(722);
		c.getPA().addSkillXP(100 * c.getItems().getItemAmount(item[0]), 6);
		c.sendMessage("You use your magic power to convert all your logs into planks.");
		c.getCombat().resetPlayerAttack();
	}
}