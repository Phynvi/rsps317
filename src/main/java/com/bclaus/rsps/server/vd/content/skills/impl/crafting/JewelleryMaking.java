package com.bclaus.rsps.server.vd.content.skills.impl.crafting;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.player.Player;

public class JewelleryMaking extends CraftingData {

	public static void jewelryMaking(Player c, String type, int itemId, int amount) {
		switch (type) {
		case "RING":
			for (int i = 0; i < jewelryData.RINGS.item.length; i++) {
				if (itemId == jewelryData.RINGS.item[i][1]) {
					mouldJewelry(c, jewelryData.RINGS.item[i][0], itemId, amount, jewelryData.RINGS.item[i][2], jewelryData.RINGS.item[i][3]);
					break;
				}
			}
			break;
		case "NECKLACE":
			for (int i = 0; i < jewelryData.NECKLACE.item.length; i++) {
				if (itemId == jewelryData.NECKLACE.item[i][1]) {
					mouldJewelry(c, jewelryData.NECKLACE.item[i][0], itemId, amount, jewelryData.NECKLACE.item[i][2], jewelryData.NECKLACE.item[i][3]);
					break;
				}
			}
			break;
		case "AMULET":
			for (int i = 0; i < jewelryData.AMULETS.item.length; i++) {
				if (itemId == jewelryData.AMULETS.item[i][1]) {
					mouldJewelry(c, jewelryData.AMULETS.item[i][0], itemId, amount, jewelryData.AMULETS.item[i][2], jewelryData.AMULETS.item[i][3]);
					break;
				}
			}
			break;
		}
	}

	private static int time;

	private static void mouldJewelry(final Player c, final int required, final int itemId, final int amount, final int level, final int xp) {
		if (c.playerSkilling[12] == true) {
			c.sendMessage("You are already crafting Jewellery.");
			return;
		}
		if (c.playerLevel[12] < level) {
			c.sendMessage("You need a crafting level of " + level + " to mould this item.");
			return;
		}
		if (!c.getItems().playerHasItem(2357)) {
			c.sendMessage("You need a gold bar to mould this item.");
			return;
		}
		final String itemRequired = c.getItems().getItemName(required);
		if (!c.getItems().playerHasItem(required)) {
			c.sendMessage("You need " + ((itemRequired.startsWith("A") || itemRequired.startsWith("E") || itemRequired.startsWith("O")) ? "an" : "a") + " " + itemRequired.toLowerCase() + " to mould this item.");
			return;
		}
		time = amount;
		c.getPA().removeAllWindows();
		final String itemName = c.getItems().getItemName(itemId);
		c.startAnimation(899);
		c.playerSkilling[12] = true;
		Server.getTaskScheduler().schedule(new ScheduledTask(2) {
			@Override
			public void execute() {
				if (c.playerSkilling[12] == true) {
					if (time == 0) {
						this.stop();
						return;
					}
					if (c.disconnected)
						this.stop();
					if (c.getItems().playerHasItem(2357) && c.getItems().playerHasItem(required)) {
						c.getItems().deleteItem(2357, 1);
						c.getItems().deleteItem(required, 1);
						c.getItems().addItem(itemId, 1);
						c.startAnimation(899);
						c.getPA().addSkillXP(xp * Constants.CRAFTING_EXPERIENCE, 12);
						time--;
						c.sendMessage("You make " + ((itemName.startsWith("A") || itemName.startsWith("E") || itemName.startsWith("O")) ? "an" : "a") + " " + itemName.toLowerCase());
					}
				} else {
					this.stop();
				}
			}

			@Override
			public void onStop() {
				c.playerSkilling[12] = false;
			}
		});
	}

	public static void stringAmulet(Player c, final int itemUsed, final int usedWith) {
		final int amuletId = (itemUsed == 1759 ? usedWith : itemUsed);
		for (final amuletData a : amuletData.values()) {
			if (amuletId == a.getAmuletId()) {
				c.getItems().deleteItem(1759, 1);
				c.getItems().deleteItem(amuletId, 1);
				c.getItems().addItem(a.getProduct(), 1);
				c.getPA().addSkillXP(50 * Constants.CRAFTING_EXPERIENCE, 12);
			}
		}
	}

	public static void jewelryInterface(Player c) {
		for (jewelryData i : jewelryData.values()) {
			if (c.getItems().playerHasItem(1592)) {

				for (int j = 0; j < i.item.length; j++) {
					if (c.getItems().playerHasItem(jewelryData.RINGS.item[j][0])) {
						c.getPA().sendFrame34(jewelryData.RINGS.item[j][1], j, 4233, 1);
					} else {
						c.getPA().sendFrame34(-1, j, 4233, 1);
					}

					c.getPA().sendFrame126("", 4230);
					c.getPA().sendFrame246(4229, 0, -1);
				}
			} else {
				c.getPA().sendFrame246(4229, 120, 1592);
				for (int j = 0; j < i.item.length; j++) {
					c.getPA().sendFrame34(-1, j, 4233, 1);
				}
				c.getPA().sendFrame126("You need a ring mould to craft rings.", 4230);

			}
			if (c.getItems().playerHasItem(1597)) {
				for (int j = 0; j < i.item.length; j++) {
					if (c.getItems().playerHasItem(jewelryData.NECKLACE.item[j][0])) {
						c.getPA().sendFrame34(jewelryData.NECKLACE.item[j][1], j, 4239, 1);
					} else {
						c.getPA().sendFrame34(-1, j, 4239, 1);
					}
					c.getPA().sendFrame126("", 4236);
					c.getPA().sendFrame246(4235, 0, -1);
				}
			} else {
				c.getPA().sendFrame246(4235, 120, 1597);
				for (int j = 0; j < i.item.length; j++) {
					c.getPA().sendFrame34(-1, j, 4239, 1);
				}
				c.getPA().sendFrame126("You need a necklace mould to craft necklaces.", 4236);
			}
			if (c.getItems().playerHasItem(1595)) {
				for (int j = 0; j < i.item.length; j++) {
					if (c.getItems().playerHasItem(jewelryData.AMULETS.item[j][0])) {
						c.getPA().sendFrame34(jewelryData.AMULETS.item[j][1], j, 4245, 1);
					} else {
						c.getPA().sendFrame34(-1, j, 4245, 1);
					}
					c.getPA().sendFrame126("", 4242);
					c.getPA().sendFrame246(4241, 0, -1);
				}
			} else {
				c.getPA().sendFrame246(4235, 120, 1597);
				for (int j = 0; j < i.item.length; j++) {
					c.getPA().sendFrame34(-1, j, 4245, 1);
				}
				c.getPA().sendFrame126("You need an amulet mould to craft amulets.", 4242);
			}
		}
		c.getPA().showInterface(4161);
	}
}