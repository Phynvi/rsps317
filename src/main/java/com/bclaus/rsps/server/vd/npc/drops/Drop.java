package com.bclaus.rsps.server.vd.npc.drops;

import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.npc.NPC;

/**
 * A single drop that can be placed on the ground after an {@link NPC} dies.
 * 
 * @author lare96
 */
public class Drop {

	/** The item id that will be dropped. */
	private int id;

	/** The item count that will be dropped. */
	private int count;

	/** The chance of this item being dropped. */
	private double chance;

	/**
	 * Create a new {@link Drop}.
	 * 
	 * @param item
	 *            the item that will be dropped.
	 * @param chance
	 *            the chance of this item being dropped.
	 */
	public Drop(Item item, double chance) {
		this.id = item.getId();
		this.count = item.getCount();
		this.chance = chance;
	}

	/**
	 * Gets the item that will be dropped.
	 * 
	 * @return the item that will be dropped.
	 */
	public Item getItem() {
		return new Item(id, count);
	}

	/**
	 * Gets the chance of this item being dropped.
	 * 
	 * @return the chance of this item being dropped.
	 */
	public double getChance() {
		return chance;
	}
}
