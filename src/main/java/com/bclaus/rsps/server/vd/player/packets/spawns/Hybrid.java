package com.bclaus.rsps.server.vd.player.packets.spawns;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.account_type.Account;

public class Hybrid {
	public static Player c;

	public Hybrid(Player c) {
		Hybrid.c = c;
	}

	static int HAT = 0;
	static int CAPE = 1;
	static int AMULET = 2;
	static int WEAPON = 3;
	static int BODY = 4;
	static int SHIELD = 5;
	static int LEGS = 7;
	static int GLOVES = 9;
	static int BOOTS = 10;
	static int RING = 12;
	static int ARROWS = 13;

	public enum Sets {
		ENCHANTMENT("Enchantment", new int[][] { { 55, 70, 60 } }, new int[][] { { 1725, AMULET }, { 10828, HAT }, { 7399, BODY }, { 7398, LEGS }, { 3842, SHIELD }, { 2414, CAPE }, { 7462, GLOVES }, { 4097, BOOTS }, { 4675, WEAPON }, { 2550, RING } }, new int[][] { { 4587, 1 }, { 1127, 1 }, { 1079, 1 }, { 8850, 1 }, { 4131, 1 }, { 1215, 1 }, { 6687, 2 }, { 3026, 2 }, { 145, 1 }, { 157, 1 }, { 391, 13 }, { 565, 500 }, { 555, 1000 }, { 560, 800 } }),

		MYSTIC("Mystic", new int[][] { { 20, 70, 60 } }, new int[][] { { 1704, AMULET }, { 4089, HAT }, { 4091, BODY }, { 4093, LEGS }, { 3842, SHIELD }, { 2414, CAPE }, { 4095, GLOVES }, { 4097, BOOTS }, { 1401, WEAPON }, { 2550, RING } }, new int[][] { { 4587, 1 }, { 1127, 1 }, { 1079, 1 }, { 8850, 1 }, { 4131, 1 }, { 1215, 1 }, { 6687, 2 }, { 3026, 2 }, { 145, 1 }, { 157, 1 }, { 391, 13 }, { 565, 500 }, { 555, 1000 }, { 560, 800 } });

		public int requirements[][];
		public int equip[][];
		public int inventory[][];
		private String name;

		Sets(String name, int requirements[][], int equip[][], int inventory[][]) {
			this.name = name;
			this.requirements = requirements;
			this.equip = equip;
			this.inventory = inventory;
		}
		public String getName() {
			return name;
		}
		public int[][] getInventory() {
			return inventory;
		}

		public int[][] getRequirements() {
			return requirements;
		}

		public int[][] getEquip() {
			return equip;
		}

		/**
		 * Build constructor
		 */

	}

	public Sets readRequierments() {
		/**
		 * Values for Enum Constants
		 */
		// def, mage, (attack,str)
		for (Sets set : Sets.values()) {
			if (c.playerLevel[1] >= set.getRequirements()[0][0] // def
					&& c.playerLevel[0] >= set.getRequirements()[0][2] // attack
					&& c.playerLevel[2] >= set.getRequirements()[0][2] // str
					&& c.playerLevel[6] >= set.getRequirements()[0][1]) {// mage
																			// //
																			// {
				c.sendMessage("You get" + set.getName() + " set, because of your stat levels.");
			}
			return set;
		}
		return null;
	}

	Sets set = null;

	public void getHybridSet() {
		set = readRequierments();
		if (c.getItems().wearingItems()) {
			c.sendMessage("Please bank your equipped items.");
			return;
		}
		if(c.getAccount().getType().alias().equals(Account.IRON_MAN_TYPE.alias())) {
			c.sendMessage("Iron Man accounts can not cheat!");
			return;
		}
		if ((c.underAttackBy > 1 || c.underAttackBy2 > 1)) {
			c.sendMessage("You can't use a preset while in combat.");
			return;
		}
		if (c.inWild()) {
			c.sendMessage("You can't use this in wild.");
			return;
		}
		if (set == null) {
			c.sendMessage("returned null");
			return;
		}
		for (int i = 0; i < set.getEquip().length; i++) {
			c.getItems().setEquipment(set.getEquip()[i][0], 1, set.getEquip()[i][1]);
		}
		for (int i = 0; i < set.getInventory().length; i++) {
			c.getItems().addItem(set.getInventory()[i][0], set.getInventory()[i][1]);
		}
	}

}
