package com.bclaus.rsps.server.vd.content.skills.impl.crafting;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.player.Player;

public class LeatherMaking extends CraftingData {

	public static void craftLeatherDialogue(final Player c, final int itemUsed, final int usedWith) {
		for (final leatherData l : leatherData.values()) {
			final int leather = (itemUsed == 1733 ? usedWith : itemUsed);
			if (leather == l.getLeather()) {
				if (l.getLeather() == 1741) {
					c.getPA().showInterface(2311);
					c.leatherType = leather;
					return;
				}
				String[] name = { "Body", "Chaps", "Bandana", "Boots", "Vamb", };
				if (l.getLeather() == 6289) {
					c.getPA().sendFrame164(8938);
					c.getPA().itemOnInterface(8941, 180, 6322);
					c.getPA().itemOnInterface(8942, 180, 6324);
					c.getPA().itemOnInterface(8943, 180, 6326);
					c.getPA().itemOnInterface(8944, 180, 6328);
					c.getPA().itemOnInterface(8945, 180, 6330);
					for (int i = 0; i < name.length; i++) {
						c.getPA().sendFrame126(name[i], 8949 + (i * 4));
					}
					c.leatherType = leather;
					return;
				}
			}
		}
		for (final leatherDialogueData d : leatherDialogueData.values()) {
			final int leather = (itemUsed == 1733 ? usedWith : itemUsed);
			String[] name = { "Vamb", "Chaps", "Body", };
			if (leather == d.getLeather()) {
				c.getPA().sendFrame164(8880);
				c.getPA().itemOnInterface(8883, 180, d.getVamb());
				c.getPA().itemOnInterface(8885, 180, d.getChaps());
				c.getPA().itemOnInterface(8884, 180, d.getBody());
				for (int i = 0; i < name.length; i++) {
					c.getPA().sendFrame126(name[i], 8889 + (i * 4));
				}
				c.leatherType = leather;
				return;
			}
		}
	}

	private static int amount;

	public static void craftLeather(final Player c, final int buttonId) {
		if (c.playerSkilling[12] == true) {
			return;
		}
		for (final leatherData l : leatherData.values()) {
			if (buttonId == l.getButtonId(buttonId)) {
				if (c.leatherType == l.getLeather()) {
					if (c.playerLevel[12] < l.getLevel()) {
						c.sendMessage("You need a crafting level of " + l.getLevel() + " to make this.");
						c.getPA().removeAllWindows();
						return;
					}
					if (!c.getItems().playerHasItem(1734)) {
						c.sendMessage("You need some thread to make this.");
						c.getPA().removeAllWindows();
						return;
					}
					if (!c.getItems().playerHasItem(c.leatherType, l.getHideAmount())) {
						c.sendMessage("You need " + l.getHideAmount() + " " + c.getItems().getItemName(c.leatherType).toLowerCase() + " to make " + c.getItems().getItemName(l.getProduct()).toLowerCase() + ".");
						c.getPA().removeAllWindows();
						return;
					}
					c.startAnimation(1249);
					c.getPA().removeAllWindows();
					c.playerSkilling[12] = true;
					amount = l.getAmount(buttonId);
					Server.getTaskScheduler().schedule(new ScheduledTask(5) {
						@Override
						public void execute() {
							if (c.playerSkilling[12] == true) {
								if (!c.getItems().playerHasItem(1734)) {
									c.sendMessage("You have run out of thread.");
									this.stop();
									return;
								}
								if (!c.getItems().playerHasItem(c.leatherType, l.getHideAmount())) {
									c.sendMessage("You have run out of leather.");
									this.stop();
									return;
								}
								if (amount == 0) {
									this.stop();
									return;
								}
								c.getItems().deleteItem(1734, c.getItems().getItemSlot(1734), 1);
								c.getItems().deleteItem(c.leatherType, l.getHideAmount());
								c.getItems().addItem(l.getProduct(), 1);
								c.sendMessage("You make " + ((c.getItems().getItemName(l.getProduct()).contains("body")) ? "a" : "some") + " " + c.getItems().getItemName(l.getProduct()) + ".");
								c.getPA().addSkillXP(Constants.CRAFTING_EXPERIENCE * (int) l.getXP(), 12);
								c.startAnimation(1249);
								amount--;
								if (!c.getItems().playerHasItem(1734)) {
									c.sendMessage("You have run out of thread.");
									this.stop();
									return;
								}
								if (!c.getItems().playerHasItem(c.leatherType, l.getHideAmount())) {
									c.sendMessage("You have run out of leather.");
									this.stop();
									return;
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
			}
		}
	}
}