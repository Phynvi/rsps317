package com.bclaus.rsps.server.vd.content.skills.impl;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author Acquittal
 **/

public class Cooking extends SkillHandler {

	public static void cookThisFood(Player p, int i, int object) {
		switch (i) {
		case 317:
			cookFish(p, i, 40, 1, 323, 315, object);
			break;
		case 321:
			cookFish(p, i, 40, 1, 323, 319, object);
			break;
		case 327:
			cookFish(p, i, 50, 1, 369, 325, object);
			break;
		case 345:
			cookFish(p, i, 60, 5, 357, 347, object);
			break;
		case 353:
			cookFish(p, i, 70, 10, 357, 355, object);
			break;
		case 335:
			cookFish(p, i, 80, 15, 343, 333, object);
			break;
		case 341:
			cookFish(p, i, 85, 18, 343, 339, object);
			break;
		case 349:
			cookFish(p, i, 90, 20, 343, 351, object);
			break;
		case 331:
			cookFish(p, i, 100, 25, 343, 329, object);
			break;
		case 359:
			cookFish(p, i, 110, 30, 367, 361, object);
			break;
		case 361:
			cookFish(p, i, 120, 30, 367, 365, object);
			break;
		case 363:
			cookFish(p, i, 130, 46, 367, 365, object);
			break;
		case 377:
			cookFish(p, i, 160, 40, i + 4, i + 2, object);
			break;
		case 371:
			cookFish(p, i, 195, 45, i + 4, i + 2, object);
			break;
		case 383:
			cookFish(p, i, 240, 80, i + 4, i + 2, object);
			break;
		case 395:
			cookFish(p, i, 269, 82, i + 4, i + 2, object);
			break;
		case 389:
			cookFish(p, i, 305, 91, i + 4, i + 2, object);
			break;
		case 3142:
			cookFish(p, i, 350, 99, i + 4, i + 2, object);
			break;
		default:
			p.sendMessage("You can't cook this!");
			break;
		}
	}

	private static int fishStopsBurning(int i) {
		switch (i) {
		case 317:
			return 15;
		case 321:
			return 20;
		case 327:
			return 38;
		case 345:
			return 37;
		case 353:
			return 45;
		case 335:
			return 50;
		case 341:
			return 39;
		case 349:
			return 52;
		case 331:
			return 58;
		case 359:
			return 63;
		case 377:
			return 50;
		case 363:
			return 80;
		case 371:
			return 86;
		case 7944:
			return 90;
		case 383:
			return 94;
		default:
			return 99;
		}
	}

	private static void cookFish(Player c, int itemID, int xpRecieved, int levelRequired, int burntFish, int cookedFish, int object) {
		if (!hasRequiredLevel(c, 7, levelRequired, "cooking", "cook this")) {
			return;
		}

		int chance = c.playerLevel[7];
		if (c.playerEquipment[Player.playerHands] == 775) {
			chance = c.playerLevel[7] + 8;
		}
		if (chance <= 0) {
			chance = Misc.random(5);
		}
		c.playerSkillProp[7][0] = itemID;
		c.playerSkillProp[7][1] = xpRecieved * COOKING_XP;
		c.playerSkillProp[7][2] = levelRequired;
		c.playerSkillProp[7][3] = burntFish;
		c.playerSkillProp[7][4] = cookedFish;
		c.playerSkillProp[7][5] = object;
		c.playerSkillProp[7][6] = chance;
		if(!c.stopPlayerSkill)
		viewCookInterface(c, itemID);
	}

	public static void getAmount(Player c, int amount) {
		int item = c.getItems().getItemAmount(c.playerSkillProp[7][0]);
		if (amount > item) {
			amount = item;
		}
		c.doAmount = amount;
		cookTheFish(c);
	}

	public static void resetCooking(Player c) {
		c.playerSkilling[7] = false;
		c.stopPlayerSkill = false;
		c.startAnimation(65535);
		for (int i = 0; i < 6; i++) {
			c.playerSkillProp[7][i] = -1;
		}
	}

	private static void viewCookInterface(Player c, int item) {
		c.getPA().sendFrame164(1743);
		c.getPA().sendFrame246(13716, 190, item);
		c.getPA().sendFrame126("\\n\\n\\n\\n\\n" + c.getItems().getItemName(item) + "", 13717);
	}

	private static void cookTheFish(final Player c) {
		if (c.playerSkilling[7]) {
			return;
		}
		c.getPA().closeAllWindows();
		c.playerSkilling[7] = true;
		c.stopPlayerSkill = true;
		c.getPA().removeAllWindows();
		if (c.playerSkillProp[7][5] > 0) {
			c.startAnimation(c.playerSkillProp[7][5] == 25465 ? 897 : 896);
		}
		Server.getTaskScheduler().schedule(new ScheduledTask(2) {
			@Override
			public void execute() {
				if (!c.playerSkilling[7]) {
					return;
				}
				c.getItems().deleteItem(c.playerSkillProp[7][0], c.getItems().getItemSlot(c.playerSkillProp[7][0]), 1);
				if (c.playerLevel[7] >= fishStopsBurning(c.playerSkillProp[7][0]) || Misc.random(c.playerSkillProp[7][6]) > Misc.random(c.playerSkillProp[7][2])) {
					c.sendMessage("You successfully cook the " + c.getItems().getItemName(c.playerSkillProp[7][0]).toLowerCase() + ".");
					c.getPA().addSkillXP(c.playerSkillProp[7][1], 7);
					c.getItems().addItem(c.playerSkillProp[7][4], 1);
				} else {
					c.sendMessage("Oops! You accidentally burnt the " + c.getItems().getItemName(c.playerSkillProp[7][0]).toLowerCase() + "!");
					c.getItems().addItem(c.playerSkillProp[7][3], 1);
				}
				deleteTime(c);
				if (!c.getItems().playerHasItem(c.playerSkillProp[7][0], 1) || c.doAmount <= 0) {
					this.stop();
				}
				if (!c.stopPlayerSkill) {
					this.stop();
				}
			}

			@Override
			public void onStop() {
				resetCooking(c);
			}
		}.attach(c));
		Server.getTaskScheduler().schedule(new ScheduledTask(4) {

			@Override
			public void execute() {
				if (c.playerSkillProp[7][5] > 0) {
					c.startAnimation(c.playerSkillProp[7][5] == 25465 ? 897 : 896);
				}
				if (!c.stopPlayerSkill) {
					this.stop();
				}
			}
		}.attach(c));
	}
}