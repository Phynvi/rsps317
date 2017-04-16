package com.bclaus.rsps.server.vd.npc.drops;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.Constants;

/**
 * Will hold the entire table of drops as well as various functions to handle
 * these tables for a single {@link NPC}.
 * 
 * @author lare96
 */
public class DropTable {

	/** Holds all of the drop tables for each npc. */
	private static Map<Integer, DropTable> drops = new HashMap<>();

	/**
	 * The secure random instance that will be used to determine if items can be
	 * dropped.
	 */
	private static SecureRandom roll = new SecureRandom();

	/** The npc who this table will apply to. */
	private int npcId;

	/** The table of dynamic drops. */
	private Drop[] commonTable;

	/** The table of selective drops. */
	private Drop[] rareTable;

	/**
	 * Create a new {@link DropTable}.
	 * 
	 * @param npcId
	 *            the npc who this table will apply to.
	 * @param commonTable
	 *            the table of dynamic drops.
	 * @param rareTable
	 *            the table of selective drops.
	 */
	public DropTable(int npcId, Drop[] commonTable, Drop[] rareTable) {
		this.npcId = npcId;
		this.commonTable = commonTable;
		this.rareTable = rareTable;
	}

	/**
	 * Calculates the drops that will be placed on the ground for the specified
	 * table.
	 * 
	 * @param table
	 *            the table to calculate the drops for.
	 * @param player
	 *            the player to drop the items for.
	 * @return the drops that will be placed on the ground.
	 */
	
	public static Item[] calculateDrops(DropTable table, Player p) {
		double percentBoost = Constants.DROP_BONUS;
		if (p.isExtremeDonator())
			percentBoost += 0.10;
		if ((p.playerRights == 10 && p.donatorRights == 4) || p.gameMode == 2)
			percentBoost += 0.15;
		return calculateDrops(table, p, percentBoost);
		
	}
	public static Item[] calculateDrops(DropTable table, Player player, double boost) {

		/** This npc has no drop table. */
		if (table == null) {

			/** So just drop bones. */
			return new Item[] { new Item(526) };
		}

		/** Create the array of items. */
		Item[] items = new Item[table.getCommonTable().length + 1];
		int i = 0;
		// double boost = boostChance(player);

		/** Iterate through the common drops. */
		for (Drop drop : table.getCommonTable()) {
			if (drop == null) {
				continue;
			}

			double res = Math.round(roll.nextDouble() * 1000.0) / 1000.0;
			res *= (1.00 - boost);

			/** Add all selected drops. */
			if ((res <= drop.getChance())) {
				items[i++] = new Item(drop.getItem().getId(), Misc.randomNoZero(drop.getItem().getCount()));
			}
		}

		/** If we do not have a rare table then return here. */
		if (table.getRareTable().length < 1) {
			return items;
		}

		/** Otherwise select ONE rare item to drop. */
		Drop selective = table.getRareTable()[(int) (Math.random() * table.getRareTable().length)];

		/** If the item has no value then return. */
		if (selective == null) {
			return items;
		}

		/** Otherwise calculate and add the drop if selected. */
		double res = Math.round(roll.nextDouble() * 1000.0) / 1000.0;
		res *= (1.00 - boost);

		if ((res /*- boost*/) <= selective.getChance()) {
			items[i++] = new Item(selective.getItem().getId(), Misc.randomNoZero(selective.getItem().getCount()));

			PlayerUpdating.announce("<shad=000000><col=FF5E00>News: " + Misc.formatPlayerName(player.playerName) + " has just received x" + selective.getItem().getCount() + " " + Item.getItemName(selective.getItem().getId()));

			if (player.playerEquipment[Player.playerRing] == 4202) {
				player.sendMessage("<shad=000000><col=19FFFF> Your ring of Charos shines brightly.");
			}
			if (player.dropRateIncreaser > 0) {
				player.sendMessage("You feel your increased-drop rate bonus assist you..");
			}
			if (player.playerEquipment[Player.playerRing] == 6465) {
				player.sendMessage("<shad=000000><col=00E600>Your ring of Charos(A) Shines brightly...</col>");
			}
			if (player.playerEquipment[Player.playerRing] == 2572) {
				player.sendMessage("<shad=000000><col=00E600>Your ring of Charos(A) Shines brightly...</col>");
			}
		}

		/** Return will all of the items. */
		return items;
	}

	/**
	 * Calculates the rare drops that will be placed on the ground for the
	 * specified table and prints them off.
	 * 
	 * @param npc
	 *            the npc to calculate the drops for.
	 * @param debug
	 *            if all of the percentages should be printed off.
	 */
	public static void debugCalculateDrops(int npc, boolean debug) {
		DropTable table = drops.get(npc);
		if (table == null || table.getRareTable() == null || table.getRareTable().length == 0)
			return;

		for (int i = 0; i < 1000000; i++) {

			/** Otherwise select ONE rare item to drop. */
			Drop selective = table.getRareTable()[(int) (Math.random() * table.getRareTable().length)];

			/** If the item has no value then return. */
			if (selective == null) {
				continue;
			}

			/** Otherwise calculate and add the drop if selected. */
			double res = Math.round(roll.nextDouble() * 1000.0) / 1000.0;

			if (debug) {
			}
			if ((res /*- boost*/) <= selective.getChance()) {
				// System.exit(0);
				return;
			}
		}
	}

	/**
	 * Boosts the chance of getting a drop.
	 * 
	 * @param player
	 *            the player who's chance will be boosted.
	 * @return the amount the chance is boosted by.
	 */
	
	  private static double boostChance(Player player) { if
	 (player.playerEquipment[Player.playerRing] == 4202) { return 0.05; }
	  if (player.isDonator) { return 0.60; }
	 if(player.playerEquipment[Player.playerRing] == 6465) { return 0.10; }
	 if(player.playerEquipment[Player.playerRing] == 2572) { return 0.03; }
	 if(player.gameMode == 2 || player.gameMode == 3 || player.playerRights == 10 ) { return 0.07; } 
	 return 0.7; }
	 

	/**
	 * Gets the npc who this table will apply to.
	 * 
	 * @return the npc who this table will apply to.
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * Gets the table of dynamic drops.
	 * 
	 * @return the table of dynamic drops.
	 */
	public Drop[] getCommonTable() {
		return commonTable;
	}

	/**
	 * Gets the table of selective drops.
	 * 
	 * @return the table of selective drops.
	 */
	public Drop[] getRareTable() {
		return rareTable;
	}

	/**
	 * Gets all of the drop tables for each npc.
	 * 
	 * @return all of the drop tables for each npc.
	 */
	public static Map<Integer, DropTable> getDrops() {
		return drops;
	}
}
