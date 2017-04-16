package com.bclaus.rsps.server.vd.content.skills.impl;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.items.ItemAssistant;

public class RuneCrafting extends SkillHandler {
	
	private static int RUNE_ESS = 1436;
	private static int PURE_ESS = 7936;

	public static void craftRunesOnAltar(Player c, int requiredLevel, int exp, int item, int x2, int x3, int x4) {
		int essAmount = 0;
		if (c.playerLevel[20] < requiredLevel) {
			c.sendMessage("You don't have a high enough runecrafting level to craft these runes");
			c.sendMessage("You at least need a runecrafting level of " + requiredLevel + ".");
			c.getPA().sendStatement("You don't have a high enough runecrafting level to craft these runes!");
			return;
		}
		if(c.getItems().playerHasItem(PURE_ESS)) {
			c.gfx100(186);
			c.startAnimation(791);
			if ((c.playerLevel[20]) >= 0 && (c.playerLevel[20] < x2)) {
				essAmount = c.getItems().getItemAmount(PURE_ESS);
			}
			if (c.playerLevel[20] >= x2 && c.playerLevel[20] < x3) {
				essAmount = c.getItems().getItemAmount(PURE_ESS) * 2;
			}
			if (c.playerLevel[20] >= x3 && c.playerLevel[20] < x4) {
				essAmount = c.getItems().getItemAmount(PURE_ESS) * 3;
			}
			if (c.playerLevel[20] >= x4) {
				essAmount = c.getItems().getItemAmount(PURE_ESS) * 4;
			}
			for (int i = 0; i < 29; i++) {
				c.getItems().deleteItem(PURE_ESS, c.getItems().getItemSlot(PURE_ESS), i);
			}
			if (c.diffLevel != 100)
				c.getPA().addSkillXP((exp * essAmount) * (RUNECRAFTING_XP * c.diffLevel), 20);
			else
				c.getPA().addSkillXP((exp * essAmount), 20);
			c.getItems().addItem(item, essAmount);
			c.sendMessage("You bind the temple's power into " + essAmount + " " + ItemAssistant.getItemName(item) + "s.");
			c.getPA().closeAllWindows();
		} else {
			if (!c.getItems().playerHasItem(RUNE_ESS)) {
				c.sendMessage("You need some rune essence to craft runes!");
				return;
			}
			c.gfx100(186);
			c.startAnimation(791);
			if ((c.playerLevel[20]) >= 0 && (c.playerLevel[20] < x2)) {
				essAmount = c.getItems().getItemAmount(RUNE_ESS);

			}
			if (c.playerLevel[20] >= x2 && c.playerLevel[20] < x3) {
				essAmount = c.getItems().getItemAmount(RUNE_ESS) * 2;
			}
			if (c.playerLevel[20] >= x3 && c.playerLevel[20] < x4) {
				essAmount = c.getItems().getItemAmount(RUNE_ESS) * 3;
			}
			if (c.playerLevel[20] >= x4) {
				essAmount = c.getItems().getItemAmount(RUNE_ESS) * 4;
			}
			for (int i = 0; i < 29; i++) {
				c.getItems().deleteItem(RUNE_ESS, c.getItems().getItemSlot(RUNE_ESS), i);
			}
			if (c.diffLevel != 100)
				c.getPA().addSkillXP((exp * essAmount) * (RUNECRAFTING_XP * c.diffLevel), 20);
			else
				c.getPA().addSkillXP((exp * essAmount), 20);
			c.getItems().addItem(item, essAmount);
			c.sendMessage("You bind the temple's power into " + essAmount + " " + ItemAssistant.getItemName(item) + "s.");
			c.getPA().closeAllWindows();
		}
		}
}