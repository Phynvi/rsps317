package com.bclaus.rsps.server.vd.content.consumables;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.items.ItemAssistant;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.task.ScheduledTask;

/*
 * @author Goten
 */
public class Potions {

	public static void handlePotion(final Player c, int itemId, int slot) {
		if (c.duelRule[5]) {
			c.sendMessage("You may not drink potions in this duel.");
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0) {
			return;
		}
		if (System.currentTimeMillis() - c.potDelay >= 1200) {
			if (itemId >= 151 && itemId <= 155 || itemId == 2438) {
				if (!Potions.drinkCreatureCombat(c, itemId, itemId == 2438 ? 151 : itemId == 151 ? 153 : itemId == 153 ? 155 : 229, slot))
					return;
			}
			c.potDelay = System.currentTimeMillis();
			c.attackTimer++;
			final String item = Item.getItemName(itemId);
			c.sendMessage("You drink some of your " + Item.getItemName(itemId) + ".");
			c.startAnimation(829);
			String m = "";
			if (item.endsWith("(4)")) {
				m = "You have 3 doses of potion left.";
			} else if (item.endsWith("(3)")) {
				m = "You have 2 doses of potion left.";
			} else if (item.endsWith("(2)")) {
				m = "You have 1 dose of potion left.";
			} else if (item.endsWith("(1)")) {
				m = "You have finished your potion.";
			}
			final String m1 = m;
			Server.getTaskScheduler().schedule(new ScheduledTask(1) {
				@Override
				public void execute() {
					c.sendMessage(m1);
					stop();
				}
			}.attach(c));
			switch (itemId) {
			case 3040:
				drinkMagicPot(c,itemId,3042,slot,6,false);
				break;
				case 3042:
				drinkMagicPot(c,itemId,3044,slot,6,false);
				break;
				case 3044:
				drinkMagicPot(c,itemId,3046,slot,6,false);
				break;	
				case 3046:
				drinkMagicPot(c,itemId,229,slot,6,false);
				break;
			case 2438:
				drinkCreatureCombat(c, itemId, 151, slot);
				break;
			case 151:
				drinkCreatureCombat(c, itemId, 153, slot);
				break;
			case 153:
				drinkCreatureCombat(c, itemId, 155, slot);
				break;
			case 155:
				drinkCreatureCombat(c, itemId, 229, slot);
				break;
			case 2450:
				doTheBrewzam(c, itemId, 189, slot);
				break;
			case 189:
				doTheBrewzam(c, itemId, 191, slot);
				break;
			case 191:
				doTheBrewzam(c, itemId, 193, slot);
				break;
			case 193:
				doTheBrewzam(c, itemId, 229, slot);
				break;
			case 2452: // antifires
				antifirePot(c, itemId, 2454, slot);
				break;
			case 2454:
				antifirePot(c, itemId, 2456, slot);
				break;
			case 2456:
				antifirePot(c, itemId, 2458, slot);
				break;
			case 2458:
				antifirePot(c, itemId, 229, slot);
				break;
			case 6685: // brews
				doTheBrew(c, itemId, 6687, slot);
				break;
			case 6687:
				doTheBrew(c, itemId, 6689, slot);
				break;
			case 6689:
				doTheBrew(c, itemId, 6691, slot);
				break;
			case 6691:
				doTheBrew(c, itemId, 229, slot);
				break;
			case 2436:
				drinkStatPotion(c, itemId, 145, slot, 0, true); // sup attack
				break;
			case 145:
				drinkStatPotion(c, itemId, 147, slot, 0, true);
				break;
			case 147:
				drinkStatPotion(c, itemId, 149, slot, 0, true);
				break;
			case 149:
				drinkStatPotion(c, itemId, 229, slot, 0, true);
				break;
			case 2440:
				drinkStatPotion(c, itemId, 157, slot, 2, true); // sup str
				break;
			case 157:
				drinkStatPotion(c, itemId, 159, slot, 2, true);
				break;
			case 159:
				drinkStatPotion(c, itemId, 161, slot, 2, true);
				break;
			case 161:
				drinkStatPotion(c, itemId, 229, slot, 2, true);
				break;
			case 2444:
				drinkStatPotion(c, itemId, 169, slot, 4, false); // range pot
				break;
			case 169:
				drinkStatPotion(c, itemId, 171, slot, 4, false);
				break;
			case 171:
				drinkStatPotion(c, itemId, 173, slot, 4, false);
				break;
			case 173:
				drinkStatPotion(c, itemId, 229, slot, 4, false);
				break;
			case 2432:
				drinkStatPotion(c, itemId, 133, slot, 1, false); // def pot
				break;
			case 133:
				drinkStatPotion(c, itemId, 135, slot, 1, false);
				break;
			case 135:
				drinkStatPotion(c, itemId, 137, slot, 1, false);
				break;
			case 137:
				drinkStatPotion(c, itemId, 229, slot, 1, false);
				break;
			case 113:
				drinkStatPotion(c, itemId, 115, slot, 2, false); // str pot
				break;
			case 115:
				drinkStatPotion(c, itemId, 117, slot, 2, false);
				break;
			case 117:
				drinkStatPotion(c, itemId, 119, slot, 2, false);
				break;
			case 119:
				drinkStatPotion(c, itemId, 229, slot, 2, false);
				break;
			case 2428:
				drinkStatPotion(c, itemId, 121, slot, 0, false); // attack pot
				break;
			case 121:
				drinkStatPotion(c, itemId, 123, slot, 0, false);
				break;
			case 123:
				drinkStatPotion(c, itemId, 125, slot, 0, false);
				break;
			case 125:
				drinkStatPotion(c, itemId, 229, slot, 0, false);
				break;
			case 2442:
				drinkStatPotion(c, itemId, 163, slot, 1, true); // super def pot
				break;
			case 163:
				drinkStatPotion(c, itemId, 165, slot, 1, true);
				break;
			case 165:
				drinkStatPotion(c, itemId, 167, slot, 1, true);
				break;
			case 167:
				drinkStatPotion(c, itemId, 229, slot, 1, true);
				break;
			case 3024:
				drinkPrayerPot(c, itemId, 3026, slot, true); // sup restore
				break;
			case 3026:
				drinkPrayerPot(c, itemId, 3028, slot, true);
				break;
			case 3028:
				drinkPrayerPot(c, itemId, 3030, slot, true);
				break;
			case 3030:
				drinkPrayerPot(c, itemId, 229, slot, true);
				break;
			case 10925:
				drinkPrayerPot(c, itemId, 10927, slot, true); // sanfew serums
				curePoison(c, 300000, false);
				break;
			case 10927:
				drinkPrayerPot(c, itemId, 10929, slot, true);
				curePoison(c, 300000, false);
				break;
			case 10929:
				drinkPrayerPot(c, itemId, 10931, slot, true);
				curePoison(c, 300000, false);
				break;
			case 10931:
				drinkPrayerPot(c, itemId, 229, slot, true);
				curePoison(c, 300000, false);
				break;
			case 2434:
				drinkPrayerPot(c, itemId, 139, slot, false); // pray pot
				break;
			case 139:
				drinkPrayerPot(c, itemId, 141, slot, false);
				break;
			case 141:
				drinkPrayerPot(c, itemId, 143, slot, false);
				break;
			case 143:
				drinkPrayerPot(c, itemId, 229, slot, false);
				break;
			case 2446:
				drinkAntiPoison(c, itemId, 175, slot, 100000, false); // anti
																		// poisons
				break;
			case 175:
				drinkAntiPoison(c, itemId, 177, slot, 100000, false);
				break;
			case 177:
				drinkAntiPoison(c, itemId, 179, slot, 100000, false);
				break;
			case 179:
				drinkAntiPoison(c, itemId, 229, slot, 100000, false);
				break;
			case 2448:
				drinkAntiPoison(c, itemId, 181, slot, 300000, true); // anti
																		// poisons
				break;
			case 181:
				drinkAntiPoison(c, itemId, 183, slot, 300000, true);
				break;
			case 183:
				drinkAntiPoison(c, itemId, 185, slot, 300000, true);
				break;
			case 185:
				drinkAntiPoison(c, itemId, 229, slot, 300000, true);
				break;
			/** Energy Potions **/
			case 3008:
				energyPotion(c, itemId, 3010, slot);
				break;
			case 3010:
				energyPotion(c, itemId, 3012, slot);
				break;
			case 3012:
				energyPotion(c, itemId, 3014, slot);
				break;
			case 3014:
				energyPotion(c, itemId, 229, slot);
				break;
			/** Super Energy Potions **/
			case 3016:
				energyPotion(c, itemId, 3018, slot);
				break;
			case 3018:
				energyPotion(c, itemId, 3020, slot);
				break;
			case 3020:
				energyPotion(c, itemId, 3022, slot);
				break;
			case 3022:
				energyPotion(c, itemId, 229, slot);
				break;
			}
		}
	}

	private static void energyPotion(Player c, int itemId, int replaceItem, int slot) {
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		if (itemId >= 3008 && itemId <= 3014) {
			c.runEnergy += 20;
		} else {
			c.runEnergy += 40;
		}
		if (c.runEnergy > 100) {
			c.runEnergy = 100;
		}
		c.getPA().sendFrame126(c.runEnergy + "%", 149);
	}

	public static void drinkAntiPoison(Player c, int itemId, int replaceItem, int slot, long delay, boolean immunity) {
		// c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		curePoison(c, delay, immunity);
	}

	public static void curePoison(Player c, long delay, boolean immunity) {
		c.poisonDamage = 0;
		if (immunity)
			c.poisonImmune.reset();
		c.lastPoisonSip = System.currentTimeMillis();
		c.sendMessage("You have cured your Poison");
	}

	public static boolean drinkCreatureCombat(Player c, int itemId, int replaceItem, int slot) {
		if (c.inWild()) {
			c.sendMessage("You cannot drink this potion in the wilderness.");
			return false;
		}
		if (c.getCreaturePotionTimer() > 0) {
			c.sendMessage("The effect from the last dose are still in your system!");
			return false;
		}
		for (int i = 0; i < 7; i++) {
			if (i == 3 || i == 5)
				continue;
			if (Player.getLevelForXP(c.playerXP[i]) < 75) {
				c.sendMessage("You need atleast level 75 in attack, strength, defence, range and mage to drink this.");
				return false;
			}
		}
		for (int i = 0; i < 7; i++) {
			if (i != 3 && i != 5) {
				c.playerLevel[i] = Player.getLevelForXP(c.playerXP[i]) + 26;
				c.getPA().refreshSkill(i);
			}
		}
		c.setCreaturePotionTimer(100);
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		return true;
	}

	public static void resetCreatureCombat(Player player) {
		for (int i = 0; i < 7; i++) {
			if (i != 3 && i != 5) {
				player.playerLevel[i] = Player.getLevelForXP(player.playerXP[i]);
				player.getPA().refreshSkill(i);
			}
		}
		player.sendMessage("<col=ff0033>The effects of your Super combat potion effects have worn off.");
		player.setCreaturePotionTimer(0);
	}
	public Player c;
	
	public static void enchanceMagic(Player c, int skillID, boolean sup) {
		c.playerLevel[skillID] += getBoostedMagic(c, skillID, sup);
		c.getPA().refreshSkill(skillID);
	}
	
	public static int getBoostedMagic(Player c, int skill, boolean sup) {
		int increaseBy = 0;
		if (sup)
			increaseBy = (int)(Player.getLevelForXP(c.playerXP[skill])*.06);
		else
			increaseBy = (int)(Player.getLevelForXP(c.playerXP[skill])*.06);
		if (c.playerLevel[skill] + increaseBy > Player.getLevelForXP(c.playerXP[skill]) + increaseBy + 1) {
			return Player.getLevelForXP(c.playerXP[skill]) + increaseBy - c.playerLevel[skill];
		}
		return increaseBy;
	}
	public static void drinkStatPotion(Player c, int itemId, int replaceItem, int slot, int stat, boolean sup) {
		// c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		enchanceStat(c, stat, sup);
	}

	public static void drinkPrayerPot(Player c, int itemId, int replaceItem, int slot, boolean rest) {
		// c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		c.playerLevel[5] += (Player.getLevelForXP(c.playerXP[5]) * .33);
		if (rest)
			c.playerLevel[5] += 1;
		if (c.playerLevel[5] > Player.getLevelForXP(c.playerXP[5]))
			c.playerLevel[5] = Player.getLevelForXP(c.playerXP[5]);
		c.getPA().refreshSkill(5);
		if (rest)
			restoreStats(c);
	}

	public static void restoreStats(Player c) {
		for (int j = 0; j <= 6; j++) {
			if (j == 5 || j == 3)
				continue;
			if (c.playerLevel[j] < Player.getLevelForXP(c.playerXP[j])) {
				c.playerLevel[j] += (Player.getLevelForXP(c.playerXP[j]) * .33);
				if (c.playerLevel[j] > Player.getLevelForXP(c.playerXP[j])) {
					c.playerLevel[j] = Player.getLevelForXP(c.playerXP[j]);
				}
				c.getPA().refreshSkill(j);
				c.getPA().setSkillLevel(j, c.playerLevel[j], c.playerXP[j]);
			}
		}
	}

	public static void doTheBrewzam(Player c, int itemId, int replaceItem, int slot) {
		// c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		int[] toDecrease = { 1, 3 };
		for (int tD : toDecrease) {
			c.playerLevel[tD] -= getBrewStat(c, tD, .10);
			if (c.playerLevel[tD] < 0)
				c.playerLevel[tD] = 1;
			c.getPA().refreshSkill(tD);
			c.getPA().setSkillLevel(tD, c.playerLevel[tD], c.playerXP[tD]);
		}
		c.playerLevel[0] += getBrewStat(c, 0, .20);
		if (c.playerLevel[0] > (Player.getLevelForXP(c.playerXP[0]) * 1.2 + 1)) {
			c.playerLevel[0] = (int) (Player.getLevelForXP(c.playerXP[0]) * 1.2);
		}
		c.playerLevel[2] += getBrewStat(c, 2, .12);
		if (c.playerLevel[2] > (Player.getLevelForXP(c.playerXP[2]) * 1.2 + 1)) {
			c.playerLevel[2] = (int) (Player.getLevelForXP(c.playerXP[2]) * 1.2);
		}
		c.playerLevel[5] += getBrewStat(c, 5, .10);
		if (c.playerLevel[5] > (Player.getLevelForXP(c.playerXP[5]) * 1.2 + 1)) {
			c.playerLevel[5] = (int) (Player.getLevelForXP(c.playerXP[5]) * 1.2);
		}
		c.getPA().refreshSkill(0);
		c.getPA().refreshSkill(2);
		c.getPA().refreshSkill(5);
		c.damage(new Hit(9));
	}

	public static void antifirePot(Player c, int itemId, int replaceItem, int slot) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.antiFirePot = true;
		c.antiFirePotion();
		c.sendMessage("<col=ff0033>Your immunity against dragon fire has been increased.");
		c.getItems().resetItems(3214);

	}

	public static void doTheBrew(Player c, int itemId, int replaceItem, int slot) {
		if (c.duelRule[6]) {
			c.sendMessage("You may not eat in this duel.");
			return;
		}
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		int[] toDecrease = { 0, 2, 4, 6 };
		for (int tD : toDecrease) {
			c.playerLevel[tD] -= getBrewStat(c, tD, .10);
			if (c.playerLevel[tD] < 0)
				c.playerLevel[tD] = 1;
			c.getPA().refreshSkill(tD);
			c.getPA().setSkillLevel(tD, c.playerLevel[tD], c.playerXP[tD]);
		}
		c.playerLevel[1] += getBrewStat(c, 1, .20);
		if (c.playerLevel[1] > (Player.getLevelForXP(c.playerXP[1]) * 1.2 + 1)) {
			c.playerLevel[1] = (int) (Player.getLevelForXP(c.playerXP[1]) * 1.2);
		}
		c.getPA().refreshSkill(1);

		c.playerLevel[3] += getBrewStat(c, 3, .15);
		if (c.playerLevel[3] > (Player.getLevelForXP(c.playerXP[3]) * 1.17 + 1)) {
			c.playerLevel[3] = (int) (Player.getLevelForXP(c.playerXP[3]) * 1.17);
		}
		c.getPA().refreshSkill(3);
	}

	public static void enchanceStat(Player c, int skillID, boolean sup) {
		c.playerLevel[skillID] += getBoostedStat(c, skillID, sup);
		c.getPA().refreshSkill(skillID);
	}

	public static int getBrewStat(Player c, int skill, double amount) {
		return (int) (Player.getLevelForXP(c.playerXP[skill]) * amount);
	}
	public static void drinkMagicPot(Player c, int itemId, int replaceItem, int slot, int stat, boolean sup) {
		c.startAnimation(829);
		c.playerItems[slot] = replaceItem + 1;
		c.getItems().resetItems(3214);
		enchanceMagic(c, stat,sup);
		
	}
	public static int getBoostedStat(Player c, int skill, boolean sup) {
		int increaseBy;
		if (sup)
			increaseBy = (int) (Player.getLevelForXP(c.playerXP[skill]) * .20);
		else
			increaseBy = (int) (Player.getLevelForXP(c.playerXP[skill]) * .13) + 1;
		if (c.playerLevel[skill] + increaseBy > Player.getLevelForXP(c.playerXP[skill]) + increaseBy + 1) {
			return Player.getLevelForXP(c.playerXP[skill]) + increaseBy - c.playerLevel[skill];
		}
		return increaseBy;
	}

	public static boolean isPotion(Player c, int itemId) {
		c.getItems();
		String name = ItemAssistant.getItemName(itemId);
		return name.contains("(4)") || name.contains("(3)") || name.contains("(2)") || name.contains("(1)");
	}

	public static boolean potionNames(Player c, int itemId) {
		c.getItems();
		String name = ItemAssistant.getItemName(itemId);
		return name.endsWith("potion(4)") || name.endsWith("potion(3)") || name.endsWith("potion(2)") || name.endsWith("potion(1)") || name.contains("saradomin brew") || name.contains("zamorak brew");
	}

	/**
	 * UNUSED
	 */
	public static void createCreaturePotion(Player player) {
		int[] items = { 145, 157, 163, 169, 3042 };
		for (int item : items) {
			if (!player.getItems().playerHasItem(item)) {
				player.getDH().sendStatement("This potion requires a super strength, attack, and defence potion.", "It also requires a normal ranging and magic potion.", "All potions must be three (3) dose.", "You are missing a " + Item.getItemName(item) + ".");
				player.nextChat = -1;
			}
		}
	}
}