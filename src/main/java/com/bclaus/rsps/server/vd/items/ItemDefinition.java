/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bclaus.rsps.server.vd.items;

import java.util.LinkedList;

/**
 *
 * @author Tim/Someone
 */
public class ItemDefinition {
	private static LinkedList<ItemDefinition> itemDefinitions = new LinkedList<ItemDefinition>();

	private static int maxItemId;

	public static int getMaxItemId() {
		return maxItemId;
	}

	static {
		loadItemDefinitions();
	}

	public static void loadItemDefinitions() {
		// for reloading at runtime.
		if (!itemDefinitions.isEmpty()) {
			itemDefinitions.clear();
		}
	}

	public static ItemDefinition getItem(int id) {
		for (ItemDefinition itemDefinition : itemDefinitions) {
			if (itemDefinition.id == id) {
				return itemDefinition;
			}
		}
		return new ItemDefinition();
	}

	public static ItemDefinition getItem(String name) {
		for (ItemDefinition itemDefinition : itemDefinitions) {
			if (itemDefinition.getName().equalsIgnoreCase(name)) {
				return itemDefinition;
			}
		}
		return new ItemDefinition();
	}

	public enum SlotNames {
		Hat(0), Cape(1), Amulet(2), Weapon(3), Chest(4), Shield(5), Legs(7), Hands(9), Feet(10), Ring(12), Arrows(13);

		private int id;

		private SlotNames(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	private int id;
	private ItemDefinition parent;
	private String name;
	private int value;
	private int[] bonuses;
	private byte slot;
	private int childId;
	private boolean stackable;
	private boolean fullBody;
	private boolean fullHat;
	private boolean fullMask;

	public ItemDefinition(int id, String name, int value, int[] bonuses, byte slot, int childId, BitTwiddler twiddler) {
		this.id = id;
		this.name = name;

		this.bonuses = bonuses;
		this.slot = slot;
		this.childId = childId;
		this.stackable = twiddler.bitSet(2);
		this.fullBody = twiddler.bitSet(3);
		this.fullHat = twiddler.bitSet(4);
		this.fullMask = twiddler.bitSet(5);
	}

	public ItemDefinition(ItemDefinition parent) {
		this.id = parent.childId;
		this.parent = parent;
		this.stackable = true;
		this.childId = -1;
	}

	public ItemDefinition() {
		this.id = -1;
		this.parent = null;
		this.name = "";
		this.bonuses = new int[12];
		this.childId = -1;
	}

	public int getId() {
		return id;
	}

	public int getParentId() {
		return parent.id;
	}

	public String getName() {
		return parent != null ? parent.name : name;
	}

	public int getValue() {
		return parent != null ? parent.value : value;
	}

	public int[] getBonuses() {
		return bonuses;
	}

	public byte getSlot() {
		return slot;
	}

	public int getChildId() {
		return childId;
	}

	public boolean stackable() {
		return stackable;
	}

	public boolean isChild() {
		return (parent != null);
	}

	public boolean hasChild() {
		return (childId > -1);
	}

	public boolean fullBody() {
		return fullBody;
	}

	public boolean fullHat() {
		return fullHat;
	}

	public boolean fullMask() {
		return fullMask;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
