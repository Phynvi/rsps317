package com.bclaus.rsps.server.vd.player.packets.spawns;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.account_type.Account;

public class Range {
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
	public static Player c;

	public Range(Player c) {
		Range.c = c;
	}

	public enum Sets {
		BLACK("Black", new int[][] { { 40, 70 }, { 40, 70 }, { 40, 70 }, { 40, 70 }, { 40, 70 }, { 40, 70 }, { 40, 70 }, { 40, 70 }, { 40, 70 } }, new int[][] { { 2491, GLOVES, 1 }, { 2497, LEGS, 1 }, { 2503, BODY, 1 }, { 892, ARROWS, 500 }, { 1167, HAT, 1 }, { 10499, CAPE, 1 }, { 861, WEAPON, 1 }, { 6328, BOOTS, 1 }, { 1731, AMULET, 1 } }, new int[][] { { 169, 2 }, { 9185, 1 }, { 9244, 100 }, { 391, 24 } }),

		RED("Red", new int[][] { { 40, 60 }, { 40, 60 }, { 40, 60 }, { 40, 60 }, { 40, 60 }, { 40, 60 }, { 40, 60 }, { 40, 60 }, { 40, 60 }, { 40, 60 } }, new int[][] { { 2489, GLOVES, 1 }, { 2495, LEGS, 1 }, { 1731, AMULET, 1 }, { 892, ARROWS, 500 }, { 2501, BODY, 1 }, { 1167, HAT, 1 }, { 10499, CAPE, 1 }, { 861, WEAPON, 1 }, { 6328, BOOTS, 1 }, { 1731, AMULET, 1 } }, new int[][] { { 169, 2 }, { 9185, 1 }, { 9244, 100 }, { 391, 24 } }),

		BLUE("Blue", new int[][] { { 40, 50 }, { 40, 50 }, { 40, 50 }, { 40, 50 }, { 40, 50 }, { 40, 50 }, { 40, 50 }, { 40, 50 }, { 40, 50 }, { 40, 50 } }, new int[][] { { 2487, GLOVES, 1 }, { 2493, LEGS, 1 }, { 1731, AMULET, 1 }, { 892, ARROWS, 500 }, { 2499, BODY, 1 }, { 1167, HAT, 1 }, { 10499, CAPE, 1 }, { 861, WEAPON, 1 }, { 6328, BOOTS, 1 }, { 1731, AMULET, 1 } }, new int[][] { { 169, 2 }, { 9185, 1 }, { 9244, 100 }, { 391, 24 }, }),

		GREEN("Green", new int[][] { { 40, 40 }, { 40, 40 }, { 40, 40 }, { 40, 40 }, { 40, 40 }, { 40, 40 }, { 40, 40 }, { 40, 40 }, { 40, 40 } },

		new int[][] { { 1167, HAT, 1 }, { 1099, LEGS, 1 }, { 1731, AMULET, 1 }, { 890, ARROWS, 500 }, { 1135, BODY, 1 }, { 1065, GLOVES, 1 }, { 857, WEAPON, 1 }, { 6328, BOOTS, 1 }, { 1731, AMULET, 1 } },

		new int[][] { { 169, 2 }, { 391, 26 } }),

		LEATHER("Leather", new int[][] { { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 } }, new int[][] { { 1167, HAT, 1 }, { 1063, GLOVES, 1 }, { 1731, AMULET, 1 }, { 882, ARROWS, 500 }, { 1095, LEGS, 1 }, { 1129, BODY, 1 }, { 841, WEAPON, 1 }, { 1061, BOOTS, 1 }, { 1731, AMULET, 1 } }, new int[][] { { 169, 2 }, { 391, 26 }, { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 } });

		private int[][] requirement;
		private int[][] equip;
		private int[][] inventory;
		private String name;

		Sets(String name, int[][] requirement, int[][] equip, int inventory[][]) {
			this.name = name;
			this.requirement = requirement;
			this.equip = equip;
			this.inventory = inventory;
		}

		public int[][] getInventory() {
			return inventory;
		}

		public int[][] getRequirement() {
			return requirement;
		}

		public int[][] getEquip() {
			return equip;
		}
		public String getName() {
			return name;
		}

	}

	public Sets readReaquired() {
		for (Sets set : Sets.values()) {
			if (c.playerLevel[1] >= set.getRequirement()[0][0] && c.playerLevel[4] >= set.getRequirement()[0][1]) {
				c.sendMessage("You get" + set.getName() + " set, because of your stat levels.");
				return set;
			}
		}
		return null;
	}

	public Sets set = null;

	public void getRangeSet() {
		set = readReaquired();
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
		for (int i = 0; i < set.getRequirement().length; i++) {
			c.getItems().setEquipment(set.getEquip()[i][0], set.getEquip()[i][2], set.getEquip()[i][1]);
		}
		for (int i = 0; i < set.getInventory().length; i++) {
			c.getItems().addItem(set.getInventory()[i][0], set.getInventory()[i][1]);
		}
	}
}