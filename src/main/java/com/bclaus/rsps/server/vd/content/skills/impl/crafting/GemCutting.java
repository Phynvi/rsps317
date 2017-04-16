package com.bclaus.rsps.server.vd.content.skills.impl.crafting;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.Constants;

public class GemCutting extends CraftingData {

	public static enum cutGemData {

		SAPPHIRE(1623, 1607, 20, 50, 888), EMERALD(1621, 1605, 27, 67, 889), RUBY(1619, 1603, 34, 85, 887), DIAMOND(1617, 1601, 43, 107.5, 886), DRAGONSTONE(1631, 1615, 55, 137.5, 885), ONYX(6571, 6573, 67, 168, 885), /**
		 * 
		 * Need correct animation ID
		 **/
		OPAL(1625, 1609, 1, 12, 890), JADE(1627, 1611, 13, 20, 891), RED_TOPAZ(1629, 1613, 16, 25, 892);

		private int uncut, cut, level, animation;
		private double xp;

		private cutGemData(final int uncut, final int cut, final int level, final double xp, final int animation) {
			this.uncut = uncut;
			this.cut = cut;
			this.level = level;
			this.xp = xp;
			this.animation = animation;
		}

		public int getUncut() {
			return uncut;
		}

		public int getCut() {
			return cut;
		}

		public int getLevel() {
			return level;
		}

		public double getXP() {
			return xp;
		}

		public int getAnimation() {
			return animation;
		}
	}

	public static void cutGem(final Player c, final int itemUsed, final int usedWith) {
		if (c.playerSkilling[12] == true) {
			return;
		}
		final int itemId = (itemUsed == 1755 ? usedWith : itemUsed);
		for (final cutGemData g : cutGemData.values()) {
			if (itemId == g.getUncut()) {
				if (c.playerLevel[12] < g.getLevel()) {
					c.sendMessage("You need a crafting level of " + g.getLevel() + " to cut this gem.");
					return;
				}
				if (!c.getItems().playerHasItem(itemId)) {
					return;
				}
				c.playerSkilling[12] = true;
				c.startAnimation(g.getAnimation());
				Server.getTaskScheduler().schedule(new ScheduledTask(4) {

					@Override
					public void execute() {
						if (c.disconnected)
							this.stop();
						if (c.playerSkilling[12] == true) {
							if (c.getItems().playerHasItem(itemId)) {
								c.getItems().deleteItem(itemId, 1);
								c.getItems().addItem(g.getCut(), 1);
								c.getPA().addSkillXP((int) g.getXP() * Constants.CRAFTING_EXPERIENCE, 12);
								c.sendMessage("You cut the " + c.getItems().getItemName(itemId).toLowerCase() + ".");
								c.startAnimation(g.getAnimation());
							} else {
								this.stop();
							}
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