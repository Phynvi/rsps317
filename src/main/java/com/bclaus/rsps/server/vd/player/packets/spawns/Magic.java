package com.bclaus.rsps.server.vd.player.packets.spawns;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.account_type.Account;

public class Magic {

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

		Enchantment("Enchantment", new int[][] { { 20, 50 } },
				new int[][] { { 1704, AMULET }, { 7400, HAT }, { 7399, BODY }, { 7398, LEGS }, { 3842, SHIELD },{ 2414, CAPE }, { 4095, GLOVES }, { 4097, BOOTS }, {4675, WEAPON} },
				new int[][] { { 556, 300 }, { 560, 300 }, { 562, 300 }, { 554, 300 }, { 555, 300 }, { 557, 300 },{565,300}, {3042, 2}, {391, 19}}),


		Mystic("Mystic", new int[][] { { 20, 20 } }, 
				new int[][] { { 1704, AMULET }, { 4089, HAT }, { 4091, BODY }, { 4093, LEGS }, { 3842, SHIELD },{ 2414, CAPE },{ 4095, GLOVES }, { 4097, BOOTS }, {1401, WEAPON} },
				new int[][] { { 556, 300 }, { 560, 300 }, { 562, 300 }, { 554, 300 }, { 555, 300 }, { 557, 300 }, {565,300}, {3042, 2}, {391, 19}}),


		WIZARD("Wizard", new int[][] { { 1, 1 } }, 
				new int[][] { { 579, HAT }, { 1704, AMULET }, { 6107, BODY }, { 6108, LEGS }, { 2579, BOOTS }, { 2414, CAPE }, { 1379, WEAPON }, { 3840, SHIELD } },
				new int[][] { { 556, 300 }, { 560, 300 }, { 562, 300 }, { 554, 300 }, { 555, 300 }, { 557, 300 } , {3042, 2}, {391, 20}});

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

	public Magic(Player Client) {
		this.c = Client;
	}

	// still don't see why you would need to just get all the values for enums.
	public Sets forSet() {
		for (Sets set : Sets.values()) {
			if (c.playerLevel[1] >= set.getRequirement()[0][0] && c.playerLevel[0] >= set.getRequirement()[0][1] && c.playerLevel[6] >= set.getRequirement()[0][1]) {
				c.sendMessage("You get" + set.getName() + " set, because of your stat levels.");
				return set;
			} else
				System.out.println("Set: " + set + " Can not be equipped.");
		}
		return null;
	}

	Sets set = null;

	public void getMageSet() {
		set = forSet();
		if(c.getAccount().getType().alias().equals(Account.IRON_MAN_TYPE.alias())) {
			c.sendMessage("Iron Man accounts can not cheat!");
			return;
		}
		if (c.getItems().wearingItems()) {
			c.sendMessage("Please bank your equipped items.");
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
