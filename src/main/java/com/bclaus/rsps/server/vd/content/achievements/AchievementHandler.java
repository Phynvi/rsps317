package com.bclaus.rsps.server.vd.content.achievements;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.content.achievements.Achievements.Achievement;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.NPCHandler;

public class AchievementHandler {
	Player player;
	public int currentInterface;
	private int[][] amountRemaining = new int[3][100];
	private boolean[][] completed = new boolean[3][100];

	private int points;

	/**
	 * Warning, this is saved based on the position of each item. When adding to
	 * this array, you must add to the end, not to the beginning.
	 * 
	 * ItemId, Placeholder, Cost In Points
	 */
	private int boughtItems[][] = { { 12350, -1, 13 }, { 12351, -1, 13 }, { 12352, -1, 13 }, { 12353, -1, 13 }, { 7582, -1, 20 },

	};

	public AchievementHandler(Player player) {
		this.player = player;
	}

	public void print(BufferedWriter writer, int tier) {
		try {
			for (Achievements.Achievement achievement : Achievement.ACHIEVEMENTS) {
				if (achievement.getTier().ordinal() == tier) {
					writer.write(achievement.name().toLowerCase() + " = " + amountRemaining[tier][achievement.getId()] + "\t" + completed[tier][achievement.getId()]);
					writer.newLine();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void read(String name, int tier, int amount, boolean state) {
		for (Achievements.Achievement achievement : Achievements.Achievement.ACHIEVEMENTS) {
			if (achievement.getTier().ordinal() == tier) {
				if (achievement.name().toLowerCase().equals(name)) {
					this.setComplete(tier, achievement.getId(), state);
					this.setAmountRemaining(tier, achievement.getId(), amount);
					break;
				}
			}
		}
	}

	public void drawInterface(int tier) {
		int arrowX = tier == 0 ? 0 : tier == 1 ? 75 : 150;
		int scrollId = tier == 0 ? 49101 : tier == 1 ? 51101 : 53101;
		player.getPA().sendFrame70(0 + arrowX, 0, 49015);
		player.getPA().sendFrame171(tier == 0 ? 0 : 1, 49100);
		player.getPA().sendFrame171(tier == 1 ? 0 : 1, 51100);
		player.getPA().sendFrame171(tier == 2 ? 0 : 1, 53100);
		player.getPA().sendFrame126(Integer.toString(this.getPoints()), 49016);
		for (Achievement achievement : Achievement.ACHIEVEMENTS) {
			if (achievement.getTier().ordinal() == tier) {
				int amount = getAmountRemaining(achievement.getTier().ordinal(), achievement.getId());
				if (amount > achievement.getAmount())
					amount = achievement.getAmount();
				player.getPA().sendFrame126(Integer.toString(achievement.getPoints()), scrollId + 300 + achievement.getId());
				player.getPA().sendFrame126(achievement.name().toUpperCase().replaceAll("_", " "), scrollId + 400 + achievement.getId());
				player.getPA().sendFrame126(achievement.getDescription(), scrollId + 500 + achievement.getId());
				player.getPA().sendFrame126(amount + "/" + achievement.getAmount(), scrollId + 700 + achievement.getId());
			}
		}
		player.getPA().showInterface(49000);
	}

	public void kill(NPC npc) {
		String name = NPCHandler.getNpcListName(npc.npcType).toLowerCase().replaceAll("_", " ");
		Achievements.increase(player, AchievementType.KILL_NPC, 1);
		List<String> checked = new ArrayList<String>();
		for (Achievement achievement : Achievement.ACHIEVEMENTS) {
			if (!achievement.getType().name().toLowerCase().contains("kill"))
				continue;
			if (achievement.getType().name().toLowerCase().replaceAll("_", " ").replaceAll("kill ", "").equalsIgnoreCase(name)) {
				if (checked.contains(achievement.getType().name().toLowerCase().replaceAll("_", " ").replaceAll("kill ", "")))
					continue;
				Achievements.increase(player, achievement.getType(), 1);
				checked.add(achievement.getType().name().toLowerCase().replaceAll("_", " ").replaceAll("kill ", ""));
			}
		}
	}

	public boolean hasCompletedAll() {
		int amount = 0;
		for (Achievement achievement : Achievement.ACHIEVEMENTS) {
			if (isComplete(achievement.getTier().ordinal(), achievement.getId()))
				amount++;
		}
		return amount == Achievements.getMaximumAchievements();
	}

	public boolean completedTier(AchievementTier tier) {
		for (Achievement achievement : Achievement.ACHIEVEMENTS)
			if (achievement.getTier() == tier)
				if (!isComplete(achievement.getTier().ordinal(), achievement.getId()))
					return false;
		return true;
	}

	public boolean isComplete(int tier, int index) {
		return completed[tier][index];
	}

	public boolean setComplete(int tier, int index, boolean state) {
		return this.completed[tier][index] = state;
	}

	public int getAmountRemaining(int tier, int index) {
		return amountRemaining[tier][index];
	}

	public void setAmountRemaining(int tier, int index, int amountRemaining) {
		this.amountRemaining[tier][index] = amountRemaining;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isAchievementItem(int itemId) {
		for (int i = 0; i < boughtItems.length; i++)
			if (boughtItems[i][0] == itemId)
				return true;
		return false;
	}

	public boolean hasBoughtItem(int itemId) {
		for (int i = 0; i < boughtItems.length; i++)
			if (boughtItems[i][0] == itemId)
				if (boughtItems[i][1] != -1)
					return true;
		return false;
	}

	public void setBoughtItem(int itemId) {
		for (int i = 0; i < boughtItems.length; i++)
			if (boughtItems[i][0] == itemId)
				boughtItems[i][1] = 1;
	}

	public int[][] getBoughtItems() {
		return this.boughtItems;
	}

	public int getCost(int itemId) {
		for (int i = 0; i < boughtItems.length; i++) {
			if (itemId == boughtItems[i][0]) {
				return boughtItems[i][2];
			}
		}
		return Integer.MAX_VALUE;
	}

	public void setBoughtItem(int index, int value) {
		this.boughtItems[index][1] = value;
	}

}
