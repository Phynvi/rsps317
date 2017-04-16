package com.bclaus.rsps.server.vd.player.packets.spawns;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.account_type.Account;

public class Melee {

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

	public enum Sets {// you can put arrays inside the enum constatns?
		RUNE("Rune", new int[][] { { 40, 60 } }, new int[][] { { 10828, HAT }, { 2550, RING }, { 4587, WEAPON }, { 1127, BODY }, { 1079, LEGS }, { 8850, SHIELD }, { 7462, GLOVES }, { 1725, AMULET }, { 4131, BOOTS } }, new int[][] { { 1215, 1 }, { 9075, 100 }, { 557, 300 }, { 560, 500 }, { 6687, 2 }, { 3026, 2 }, { 145, 1 }, { 157, 1 }, { 391, 18 } }),

		ADAMANT("Adamaent", new int[][] { { 30, 30 } }, new int[][] { { 1073, LEGS }, { 1123, BODY }, { 1161, HAT }, { 2550, RING }, { 1725, AMULET }, { 3105, BOOTS }, { 1331, WEAPON } }, new int[][] { { 1317, 1 }, { 9075, 100 }, { 557, 300 }, { 560, 500 }, { 6687, 2 }, { 3026, 2 }, { 145, 1 }, { 157, 1 }, { 391, 18 } }),

		MITHRIL("Mithril", new int[][] { { 20, 20 } }, new int[][] { { 1071, LEGS }, { 1121, BODY }, { 7459, GLOVES }, { 3105, BOOTS }, { 2550, RING }, { 1725, AMULET }, { 8848, SHIELD }, { 1329, WEAPON }, { 1159, HAT } }, new int[][] { { 6687, 2 }, { 3026, 2 }, { 145, 1 }, { 157, 1 }, { 391, 22 } }),

		BRONZE("Bronze", new int[][] { { 1, 1 } }, new int[][] { { 1139, HAT }, { 1189, SHIELD }, { 3105, BOOTS }, { 1117, BODY }, { 1075, LEGS }, { 7459, GLOVES }, { 1321, WEAPON }, { 2550, RING }, { 1725, AMULET } }, new int[][] { { 145, 1 }, { 157, 1 }, { 391, 26 }, });

		private int[][] requirement;
		private int[][] equip;
		private int[][] inventory;
		private String name;

		Sets(String name, int requirement[][], int[][] equip, int[][] inventory) {
			this.name = name;
			this.requirement = requirement;
			this.equip = equip;
			this.inventory = inventory;// now its being assigned the inventory
										// value :?.
		}

		public String getName() {
			return name;
		}

		public int[][] getRequirement() {
			return requirement;
		}

		public int[][] getEquip() {
			return equip;// this makes sense so far, video covered it.
		}

		public int[][] getInventory() {
			return inventory;
		}

	}

	public Player c;

	public Melee(Player Client) {
		this.c = Client;
	}

	// still don't see why you would need to just get all the values for enums.
	public Sets forSet() {
		for (Sets set : Sets.values()) {
			if (c.playerLevel[1] >= set.getRequirement()[0][0] && c.playerLevel[0] >= set.getRequirement()[0][1]) {
				c.sendMessage("You get" + set.getName() + " set, because of your stat levels.");
				return set;
			} else
				c.sendMessage("Set: " + set + " Can not be equipped.");
		}
		return null;
	}

	Sets set = null;

	public void getMeleeSet() {
		set = forSet();
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
