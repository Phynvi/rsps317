package com.bclaus.rsps.server.vd.items;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;

public class Item {

	public static final int TROWEL = 5325, RAKE = 5341, SPADE = 952, WEED = 6055, COMPOST = 6032, SUPERCOMPOST = 6034, BUCKET = 1925, SECATEURS = 5329, MAGIC_SECATEURS = 7409, SEED_DIPPER = 5343, PLANT_CURE = 6036, VIAL = 229;

	public int id;
	public int count;

	public Item(int id) {
		this(id, 1);
	}

	public Item(int id, int count) {
		this.id = id;
		this.count = count;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Item) {
			Item item = (Item) obj;

			if (id == item.id && count == item.count) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if {@code item} is valid. In other words, determines if
	 * {@code item} is not {@code null} and the {@link Item#id} and
	 * {@link Item#count} are above {@code 0}.
	 *
	 * @param item
	 *            the item to determine if valid.
	 * @return {@code true} if the item is valid, {@code false} otherwise.
	 */
	public static boolean valid(Item item) {
		return item != null && item.id > 0 && item.count > 0;
	}

	public int getId() {
		return id;
	}

	public void setCount(int i) {
		this.count = i;
	}

	public int getCount() {
		return count;
	}

	@Override
	public Item clone() {
		return new Item(id, count);
	}

	@Override
	public String toString() {
		return "ITEM[id= " + id + ", count= " + count + "]";
	}

	public static boolean playerCape(int itemId) {
		String[] data = { "cloak", "cape", "ava's", "accumulator", "Cape", "cape", "Diving apparatus", "Bonesack", "skillcape(t)", "Summoning skillcape(t)" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String aData : data) {
			if (item.endsWith(aData) || item.contains(aData)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerBoots(int itemId) {
		String[] data = { "Shoes", "shoes", "boots", "sandals", "Sandals", "Boots", "Primal boots", "Santa costume boots", "greaves", "Chicken feet", "Flippers" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String aData : data) {
			if (item.endsWith(aData) || item.contains(aData)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerGloves(int itemId) {
		String[] data = { "Gloves", "gloves", "glove", "Glove", "gauntlets", "Gauntlets", "vambraces", "vamb", "Primal gauntlets", "Santa costume gloves", "bracelet", "bracers" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String aData : data) {
			if (item.endsWith(aData) || item.contains(aData)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerShield(int itemId) {
		String[] data = { "spirit", "kiteshield", "book", "Kiteshield", "shield", "Shield", "Kite", "kite", "defender", "xil", "Lit bug lantern", "Primal Kiteshield", "Book of balance" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String aData : data) {
			if (item.endsWith(aData) || item.contains(aData)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerAmulet(int itemId) {
		String[] data = { "amulet", "Amulet", "necklace", "Necklace", "Pendant", "pendant", "Symbol", "symbol", "scarf", "stole", "Strung rabbit foot" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String aData : data) {
			if (item.endsWith(aData) || item.contains(aData)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerArrows(int itemId) {
		String[] data = { "Arrows", "arrows", "Arrow", "arrow", "Bolts", "bolts", "Bolt", "bolt" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String aData : data) {
			if (item.endsWith(aData) || item.contains(aData)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerRings(int itemId) {
		String[] data = { "ring", "rings", "Ring", "Rings", };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String aData : data) {
			if (item.endsWith(aData) || item.contains(aData)) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerHats(int itemId) {
		String[] data = { "boater", "cowl", "peg", "coif", "helm", "mitre", "Bearhead", "Chicken head", "coif", "mask", "hat", "headband", "hood", "Coif", "headdress", "disguise", "cavalier", "full helm", "tiara", "Void mage helm", "Void ranger helm", "Void melee helm", "helmet", "Hat", "ears", "partyhat", "helm(t)", "helm (t)", "beret", "facemask", "sallet", "helm (g)", "wig", "hat(g)", "hat(t)", "bandana", "Helm", "Earmuffs", "Sleeping cap", };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String aData : data) {
			if ((item.endsWith(aData) || item.contains(aData)) && itemId != 4214) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerLegs(int itemId) {
		String[] data = { "tassets", "chaps", "bottoms", "gown", "trousers", "Yak-hide legs", "platelegs", "robe", "plateskirt", "legs", "leggings", "shorts", "Skirt", "skirt", "cuisse", "Trousers", "Agile legs", "Primal platelegs", "Primal plateskirt", "Larupia legs", "Decorative_top", "Decorative_legs", "Santa costume legs", "Pantaloons", "Chicken legs", "slacks" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String aData : data) {
			if ((item.endsWith(aData) || item.contains(aData)) && (!item.contains("top") && (!item.contains("robe (g)") && (!item.contains("robe (t)") && (!item.contains("Wizard robe")))))) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerBody(int itemId) {
		String[] data = { "body", "top", "Priest gown", "apron", "shirt", "d'hide", "dragonhide", "platebody", "robetop", "body(g)", "body(t)", "Yak-hide plate", "Yak-hide plate", "Wizard robe (g)", "Wizard robe (t)", "body", "brassard", "blouse", "tunic", "leathertop", "Saradomin plate", "chainbody", "hauberk", "Shirt", "torso", "chestplate", "Agile top", "Primal platebody", "Santa costume top", "Wizard Robe (g)", "Chicken wings" };
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for (String aData : data) {
			if (item.endsWith(aData) || item.contains(aData)) {
				item1 = true;
			}
		}
		return item1;
	}

	private static String[] fullbody = { "top", "chestplate", "shirt", "platebody", "Ahrims robetop", "Saradomin d'hide", "Guthix dragonhide", "Zamorak d'hide", "Karils leathertop", "brassard", "Robe top", "robetop", "Wizard robe", "ele' blouse", "platebody (t)", "platebody (g)", "chestplate", "robe (g)", "robe (t)", "Rock-shell plate", "torso", "hauberk", "Dragon chainbody", "Vesta's Chainbody", "Statius' Platebody", "Chicken wings", "Morrigan's leather body", "Prince tunic", "Agile top", "Primal platebody", "Dragon platebody",

	};

	private static String[] fullhat = { "med helm", "coif", "Dharok's helm", "hood", "Initiate helm", "Spiny helmet", "Tyras helm", "Coif", "Helm of neitiznot", "Armadyl helmet", "Berserker helm", "Lunar helm", "Void mage helm", "Void ranger helm", "Void melee helm", "Archer helm", "Farseer helm", "Warrior helm", "Void", "Slayer helmet", "Primal full helm", "helm(g)", "helm(t)", "Tyras helm" };

	private static String[] fullmask = { "full helm", "mask", "Verac's helm", "Guthan's helm", "Karil's coif", "mask", "Torag's helm", "sallet", "Saradomin helm", "Chicken head" };

	public static boolean isFullBody(int itemId) {
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (String aFullbody : fullbody) {
			if (weapon.endsWith(aFullbody) || weapon.contains(aFullbody)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFullHelm(int itemId) {
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (String aFullhat : fullhat) {
			if (weapon.endsWith(aFullhat) && itemId != 2631) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Increment the count by 1.
	 */
	public void incrementAmount() {
		if ((count + 1) > Integer.MAX_VALUE) {
			return;
		}
		count++;
	}

	/**
	 * Decrement the count by 1.
	 */
	public void decrementAmount() {
		if ((count - 1) < 0) {
			return;
		}
		count--;
	}

	/**
	 * Increment the count by the argued count.
	 * 
	 * @param count
	 *            the count to increment by.
	 */
	public void incrementAmountBy(int count) {
		if ((this.count + count) > Integer.MAX_VALUE) {
			this.count = Integer.MAX_VALUE;
		} else {
			this.count += count;
		}
	}

	/**
	 * Decrement the count by the argued count.
	 * 
	 * @param count
	 *            the count to decrement by.
	 */
	public void decrementAmountBy(int count) {
		if ((this.count - count) < 1) {
			this.count = 0;
		} else {
			this.count -= count;
		}
	}

	public static boolean isFullMask(int itemId) {
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (String aFullmask : fullmask) {
			if (weapon.endsWith(aFullmask) && itemId != 2631) {
				return true;
			}
		}
		return false;
	}

	public static String getItemName(int id) {
		for (int j = 0; j < Server.itemHandler.ItemList.length; j++) {
			if (Server.itemHandler.ItemList[j] != null)
				if (Server.itemHandler.ItemList[j].itemId == id)
					return Server.itemHandler.ItemList[j].itemName;
		}
		return null;
	}

	public static boolean[] itemStackable = new boolean[Constants.ITEM_LIMIT];
	public static boolean[] itemIsNote = new boolean[Constants.ITEM_LIMIT];
	public static int[] targetSlots = new int[Constants.ITEM_LIMIT];
	static {
		int counter = 0;
		int c;

		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/stackable.dat"));
			while ((c = dataIn.read()) != -1) {
				itemStackable[counter] = c != 0;
				counter++;
			}
			dataIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		counter = 0;

		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/notes.dat"));
			while ((c = dataIn.read()) != -1) {
				itemIsNote[counter] = c == 0;
				counter++;
			}
			dataIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		counter = 0;
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/equipment.dat"));
			while ((c = dataIn.read()) != -1) {
				targetSlots[counter++] = c;
			}
			dataIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getItemShopValue(final Player c, int itemId) {
		if (itemId < 0) {
			return 0;
		}
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null)
				if (Item.itemIsNote[itemId] == true && itemId != 0) {
					itemId = itemId - 1;
				}
			if (Server.itemHandler.ItemList[i] != null && Server.itemHandler.ItemList[i].itemId == itemId)
				return (int) Server.itemHandler.ItemList[i].ShopValue;
		}
		return 250;
	}

	public ItemList getDefinition() {
		return ItemAssistant.getItemDef(id).orElse(null);
	}

	public Item copy() {
		return new Item(getId(), getCount());
	}
}