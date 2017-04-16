package com.bclaus.rsps.server.vd.items;

public class ItemList {
	public int itemId;
	public String itemName;
	public boolean tradeAble;
	public String itemDescription;
	public double ShopValue;
	public double LowAlch;
	public double HighAlch;
	public int targetSlot;
	public int[] Bonuses = new int[100];

	public ItemList(int _itemId) {
		itemId = _itemId;
	}

	public boolean isStackable() {
		return Item.itemStackable[itemId];
	}
}
