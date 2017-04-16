package com.bclaus.rsps.server.vd.items;

import com.bclaus.rsps.server.vd.world.Position;

public class GroundItem {

	public int itemId;
	public int itemX;
	public int itemY;
	public int itemAmount;
	public int hideTicks;
	public int removeTicks;
	public int heightLevel;
	public String ownerName;

	public GroundItem(int id, int x, int y, int height, int amount, int hideTicks, String name) {
		this.itemId = id;
		this.itemX = x;
		this.itemY = y;
		this.heightLevel = height;
		this.itemAmount = amount;
		this.hideTicks = hideTicks;
		this.ownerName = name;
	}

	public Position position() {
		return new Position(itemX, itemY, heightLevel);
	}

	public int getItemId() {
		return this.itemId;
	}

	public int getItemX() {
		return this.itemX;
	}

	public int getItemY() {
		return this.itemY;
	}

	public int getHeightLevel() {
		return this.heightLevel;
	}

	public int getItemAmount() {
		return this.itemAmount;
	}

	public String getName() {
		return this.ownerName;
	}
}