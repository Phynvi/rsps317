/*package server.vd.npc.raid;

import Player;
import Misc;

public enum RaidRewards {
	HIGH(new int[][] { { 11694, 1 }, { 1050, 1 } }),

	LOW(new int[][] { { 1147, 1 }, { 1149, 1 } });

	private final int[][] itemID;

	RaidRewards(int[][] itemID) {
		this.itemID = itemID;
	}

	private int[][] getItemId() {
		return itemID;
	}

	public static int getRandomItem(Player c, RaidRewards risk) {
		int randomItem = risk.getItemId()[Misc.random(risk.getItemId().length - 1)][0];
		System.out.println("Random Chosen " + randomItem);
		return randomItem;
	}
	/*
	 * public static void main(String args[]) { }
	 */

