package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.items.Item;

/**
 * @author Someonez
 * @author NewKid
 */
public class ItemOnDeath {

	public static void activateItemsOnDeath(Player c) {
		if (!c.inItemOnDeath) {
			c.inItemOnDeath = true;
			StartBestItemScan(c);
			c.EquipStatus = 0;

			for (int k = 0; k < 4; k++)
				sendFrame34a(c, 10494, -1, k, 1);
			for (int k = 0; k < 39; k++)
				sendFrame34a(c, 10600, -1, k, 1);

			if (c.WillKeepItem1 > 0)
				sendFrame34a(c, 10494, c.WillKeepItem1, 0, c.WillKeepAmt1);
			if (c.WillKeepItem2 > 0)
				sendFrame34a(c, 10494, c.WillKeepItem2, 1, c.WillKeepAmt2);
			if (c.WillKeepItem3 > 0)
				sendFrame34a(c, 10494, c.WillKeepItem3, 2, c.WillKeepAmt3);
			if (c.WillKeepItem4 > 0 && c.prayerActive[10])
				sendFrame34a(c, 10494, c.WillKeepItem4, 3, 1);

			for (int ITEM = 0; ITEM < 28; ITEM++) {
				if (c.playerItems[ITEM] - 1 > 0 && !(c.playerItems[ITEM] - 1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot) && !(c.playerItems[ITEM] - 1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot) && !(c.playerItems[ITEM] - 1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot) && !(c.playerItems[ITEM] - 1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot)) {
					sendFrame34a(c, 10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM]);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0 && (c.playerItems[ITEM] - 1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot) && c.playerItemsN[ITEM] > c.WillKeepAmt1) {
					sendFrame34a(c, 10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM] - c.WillKeepAmt1);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0 && (c.playerItems[ITEM] - 1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot) && c.playerItemsN[ITEM] > c.WillKeepAmt2) {
					sendFrame34a(c, 10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM] - c.WillKeepAmt2);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0 && (c.playerItems[ITEM] - 1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot) && c.playerItemsN[ITEM] > c.WillKeepAmt3) {
					sendFrame34a(c, 10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM] - c.WillKeepAmt3);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0 && (c.playerItems[ITEM] - 1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot) && c.playerItemsN[ITEM] > 1) {
					sendFrame34a(c, 10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM] - 1);
					c.EquipStatus += 1;
				}
			}
			for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
				if (c.playerEquipment[EQUIP] > 0 && !(c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP + 28 == c.WillKeepItem1Slot) && !(c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP + 28 == c.WillKeepItem2Slot) && !(c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP + 28 == c.WillKeepItem3Slot) && !(c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP + 28 == c.WillKeepItem4Slot)) {
					sendFrame34a(c, 10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP]);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0 && (c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP + 28 == c.WillKeepItem1Slot) && c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt1 > 0) {
					sendFrame34a(c, 10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP] - c.WillKeepAmt1);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0 && (c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP + 28 == c.WillKeepItem2Slot) && c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt2 > 0) {
					sendFrame34a(c, 10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP] - c.WillKeepAmt2);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0 && (c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP + 28 == c.WillKeepItem3Slot) && c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt3 > 0) {
					sendFrame34a(c, 10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP] - c.WillKeepAmt3);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0 && (c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP + 28 == c.WillKeepItem4Slot) && c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - 1 > 0) {
					sendFrame34a(c, 10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP] - 1);
					c.EquipStatus += 1;
				}
			}
			ResetKeepItems(c);
			c.getPA().showInterface(17100);
		}
	}

	public static void ResetKeepItems(Player c) {
		c.WillKeepAmt1 = -1;
		c.WillKeepItem1 = -1;
		c.WillKeepAmt2 = -1;
		c.WillKeepItem2 = -1;
		c.WillKeepAmt3 = -1;
		c.WillKeepItem3 = -1;
		c.WillKeepAmt4 = -1;
		c.WillKeepItem4 = -1;
	}

	public static void StartBestItemScan(Player c) {
		if (c.isSkulled && !c.prayerActive[10]) {
			ItemKeptInfo(c, 0);
			return;
		}
		FindItemKeptInfo(c);
		ResetKeepItems(c);
		BestItem1(c);
	}

	public static void FindItemKeptInfo(Player c) {
		if (c.isSkulled && c.prayerActive[10])
			ItemKeptInfo(c, 1);
		else if (!c.isSkulled && !c.prayerActive[10])
			ItemKeptInfo(c, 3);
		else if (!c.isSkulled && c.prayerActive[10])
			ItemKeptInfo(c, 4);
	}

	public static void ItemKeptInfo(Player c, int Lose) {
		for (int i = 17109; i < 17131; i++) {
			c.getPA().sendFrame126("", i);
		}
		c.getPA().sendFrame126("Items you will keep on death:", 17104);
		c.getPA().sendFrame126("Items you will lose on death:", 17105);
		c.getPA().sendFrame126("Player Information", 17106);
		c.getPA().sendFrame126("Max items kept on death:", 17107);
		c.getPA().sendFrame126("~ " + Lose + " ~", 17108);
		c.getPA().sendFrame126("The normal amount of", 17111);
		c.getPA().sendFrame126("items kept is three.", 17112);
		switch (Lose) {
		case 0:
		default:
			c.getPA().sendFrame126("Items you will keep on death:", 17104);
			c.getPA().sendFrame126("Items you will lose on death:", 17105);
			c.getPA().sendFrame126("You're marked with a", 17111);
			c.getPA().sendFrame126("@red@skull. @lre@This reduces the", 17112);
			c.getPA().sendFrame126("items you keep from", 17113);
			c.getPA().sendFrame126("three to zero!", 17114);
			break;
		case 1:
			c.getPA().sendFrame126("Items you will keep on death:", 17104);
			c.getPA().sendFrame126("Items you will lose on death:", 17105);
			c.getPA().sendFrame126("You're marked with a", 17111);
			c.getPA().sendFrame126("@red@skull. @lre@This reduces the", 17112);
			c.getPA().sendFrame126("items you keep from", 17113);
			c.getPA().sendFrame126("three to zero!", 17114);
			c.getPA().sendFrame126("However, you also have", 17115);
			c.getPA().sendFrame126("the @red@Protect @lre@Items prayer", 17116);
			c.getPA().sendFrame126("active, which saves you", 17117);
			c.getPA().sendFrame126("one extra item!", 17118);
			break;
		case 3:
			c.getPA().sendFrame126("Items you will keep on death(if not skulled):", 17104);
			c.getPA().sendFrame126("Items you will lose on death(if not skulled):", 17105);
			c.getPA().sendFrame126("You have no factors", 17111);
			c.getPA().sendFrame126("affecting the items you", 17112);
			c.getPA().sendFrame126("keep.", 17113);
			break;
		case 4:
			c.getPA().sendFrame126("Items you will keep on death(if not skulled):", 17104);
			c.getPA().sendFrame126("Items you will lose on death(if not skulled):", 17105);
			c.getPA().sendFrame126("You have the @red@Protect", 17111);
			c.getPA().sendFrame126("@red@Item @lre@prayer active,", 17112);
			c.getPA().sendFrame126("which saves you one", 17113);
			c.getPA().sendFrame126("extra item!", 17114);
			break;
		}
	}

	public static void BestItem1(Player c) {
		int BestValue = 0;
		int NextValue;
		int ItemsContained = 0;
		c.WillKeepItem1 = 0;
		c.WillKeepItem1Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (c.playerItems[ITEM] > 0) {
				ItemsContained += 1;
				NextValue = (int) Math.floor(Item.getItemShopValue(c, c.playerItems[ITEM] - 1));
				if (NextValue > BestValue) {
					BestValue = NextValue;
					c.WillKeepItem1 = c.playerItems[ITEM] - 1;
					c.WillKeepItem1Slot = ITEM;
					if (c.playerItemsN[ITEM] > 2 && !c.prayerActive[10]) {
						c.WillKeepAmt1 = 3;
					} else if (c.playerItemsN[ITEM] > 3 && c.prayerActive[10]) {
						c.WillKeepAmt1 = 4;
					} else {
						c.WillKeepAmt1 = c.playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (c.playerEquipment[EQUIP] > 0) {
				ItemsContained += 1;
				NextValue = (int) Math.floor(Item.getItemShopValue(c, c.playerEquipment[EQUIP]));
				if (NextValue > BestValue) {
					BestValue = NextValue;
					c.WillKeepItem1 = c.playerEquipment[EQUIP];
					c.WillKeepItem1Slot = EQUIP + 28;
					if (c.playerEquipmentN[EQUIP] > 2 && !c.prayerActive[10]) {
						c.WillKeepAmt1 = 3;
					} else if (c.playerEquipmentN[EQUIP] > 3 && c.prayerActive[10]) {
						c.WillKeepAmt1 = 4;
					} else {
						c.WillKeepAmt1 = c.playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!c.isSkulled && ItemsContained > 1 && (c.WillKeepAmt1 < 3 || (c.prayerActive[10] && c.WillKeepAmt1 < 4))) {
			BestItem2(c, ItemsContained);
		}
	}

	public static void BestItem2(Player c, int ItemsContained) {
		int BestValue = 0;
		int NextValue;
		c.WillKeepItem2 = 0;
		c.WillKeepItem2Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (c.playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(Item.getItemShopValue(c, c.playerItems[ITEM] - 1));
				if (NextValue > BestValue && !(ITEM == c.WillKeepItem1Slot && c.playerItems[ITEM] - 1 == c.WillKeepItem1)) {
					BestValue = NextValue;
					c.WillKeepItem2 = c.playerItems[ITEM] - 1;
					c.WillKeepItem2Slot = ITEM;
					if (c.playerItemsN[ITEM] > 2 - c.WillKeepAmt1 && !c.prayerActive[10]) {
						c.WillKeepAmt2 = 3 - c.WillKeepAmt1;
					} else if (c.playerItemsN[ITEM] > 3 - c.WillKeepAmt1 && c.prayerActive[10]) {
						c.WillKeepAmt2 = 4 - c.WillKeepAmt1;
					} else {
						c.WillKeepAmt2 = c.playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (c.playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(Item.getItemShopValue(c, c.playerEquipment[EQUIP]));
				if (NextValue > BestValue && !(EQUIP + 28 == c.WillKeepItem1Slot && c.playerEquipment[EQUIP] == c.WillKeepItem1)) {
					BestValue = NextValue;
					c.WillKeepItem2 = c.playerEquipment[EQUIP];
					c.WillKeepItem2Slot = EQUIP + 28;
					if (c.playerEquipmentN[EQUIP] > 2 - c.WillKeepAmt1 && !c.prayerActive[10]) {
						c.WillKeepAmt2 = 3 - c.WillKeepAmt1;
					} else if (c.playerEquipmentN[EQUIP] > 3 - c.WillKeepAmt1 && c.prayerActive[10]) {
						c.WillKeepAmt2 = 4 - c.WillKeepAmt1;
					} else {
						c.WillKeepAmt2 = c.playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!c.isSkulled && ItemsContained > 2 && (c.WillKeepAmt1 + c.WillKeepAmt2 < 3 || (c.prayerActive[10] && c.WillKeepAmt1 + c.WillKeepAmt2 < 4))) {
			BestItem3(c, ItemsContained);
		}
	}

	private static void BestItem3(Player c, int ItemsContained) {
		int BestValue = 0;
		int NextValue;
		c.WillKeepItem3 = 0;
		c.WillKeepItem3Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (c.playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(Item.getItemShopValue(c, c.playerItems[ITEM] - 1));
				if (NextValue > BestValue && !(ITEM == c.WillKeepItem1Slot && c.playerItems[ITEM] - 1 == c.WillKeepItem1) && !(ITEM == c.WillKeepItem2Slot && c.playerItems[ITEM] - 1 == c.WillKeepItem2)) {
					BestValue = NextValue;
					c.WillKeepItem3 = c.playerItems[ITEM] - 1;
					c.WillKeepItem3Slot = ITEM;
					if (c.playerItemsN[ITEM] > 2 - (c.WillKeepAmt1 + c.WillKeepAmt2) && !c.prayerActive[10]) {
						c.WillKeepAmt3 = 3 - (c.WillKeepAmt1 + c.WillKeepAmt2);
					} else if (c.playerItemsN[ITEM] > 3 - (c.WillKeepAmt1 + c.WillKeepAmt2) && c.prayerActive[10]) {
						c.WillKeepAmt3 = 4 - (c.WillKeepAmt1 + c.WillKeepAmt2);
					} else {
						c.WillKeepAmt3 = c.playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (c.playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(Item.getItemShopValue(c, c.playerEquipment[EQUIP]));
				if (NextValue > BestValue && !(EQUIP + 28 == c.WillKeepItem1Slot && c.playerEquipment[EQUIP] == c.WillKeepItem1) && !(EQUIP + 28 == c.WillKeepItem2Slot && c.playerEquipment[EQUIP] == c.WillKeepItem2)) {
					BestValue = NextValue;
					c.WillKeepItem3 = c.playerEquipment[EQUIP];
					c.WillKeepItem3Slot = EQUIP + 28;
					if (c.playerEquipmentN[EQUIP] > 2 - (c.WillKeepAmt1 + c.WillKeepAmt2) && !c.prayerActive[10]) {
						c.WillKeepAmt3 = 3 - (c.WillKeepAmt1 + c.WillKeepAmt2);
					} else if (c.playerEquipmentN[EQUIP] > 3 - c.WillKeepAmt1 && c.prayerActive[10]) {
						c.WillKeepAmt3 = 4 - (c.WillKeepAmt1 + c.WillKeepAmt2);
					} else {
						c.WillKeepAmt3 = c.playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!c.isSkulled && ItemsContained > 3 && c.prayerActive[10] && ((c.WillKeepAmt1 + c.WillKeepAmt2 + c.WillKeepAmt3) < 4)) {
			BestItem4(c);
		}
	}

	private static void BestItem4(Player c) {
		int BestValue = 0;
		int NextValue;
		c.WillKeepItem4 = 0;
		c.WillKeepItem4Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (c.playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(Item.getItemShopValue(c, c.playerItems[ITEM] - 1));
				if (NextValue > BestValue && !(ITEM == c.WillKeepItem1Slot && c.playerItems[ITEM] - 1 == c.WillKeepItem1) && !(ITEM == c.WillKeepItem2Slot && c.playerItems[ITEM] - 1 == c.WillKeepItem2) && !(ITEM == c.WillKeepItem3Slot && c.playerItems[ITEM] - 1 == c.WillKeepItem3)) {
					BestValue = NextValue;
					c.WillKeepItem4 = c.playerItems[ITEM] - 1;
					c.WillKeepItem4Slot = ITEM;
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (c.playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(Item.getItemShopValue(c, c.playerEquipment[EQUIP]));
				if (NextValue > BestValue && !(EQUIP + 28 == c.WillKeepItem1Slot && c.playerEquipment[EQUIP] == c.WillKeepItem1) && !(EQUIP + 28 == c.WillKeepItem2Slot && c.playerEquipment[EQUIP] == c.WillKeepItem2) && !(EQUIP + 28 == c.WillKeepItem3Slot && c.playerEquipment[EQUIP] == c.WillKeepItem3)) {
					BestValue = NextValue;
					c.WillKeepItem4 = c.playerEquipment[EQUIP];
					c.WillKeepItem4Slot = EQUIP + 28;
				}
			}
		}
	}

	private static void sendFrame34a(Player c, int frame, int item, int slot, int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

}
