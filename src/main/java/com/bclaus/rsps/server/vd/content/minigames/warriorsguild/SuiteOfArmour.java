package com.bclaus.rsps.server.vd.content.minigames.warriorsguild;

import java.util.HashMap;
import java.util.Map;

/**
 * An enum containing the Suite of Armours.
 * 
 * @author Tim
 *
 */
public enum SuiteOfArmour {
	BRONZE(4278, new int[] { 1075, 1117, 1155 }, new int[] { 10, 2, 5, 10 }), IRON(4279, new int[] { 1067, 1115, 1153 }, new int[] { 20, 3, 10, 20 }), STEEL(4280, new int[] { 1069, 1119, 1157 }, new int[] { 40, 4, 20, 40 }), BLACK(4281, new int[] { 1077, 1125, 1165 }, new int[] { 60, 6, 30, 60 }), MITHRIL(4282, new int[] { 1071, 1121, 1159 }, new int[] { 160, 8, 40, 80 }), ADAMANT(4283, new int[] { 1073, 1123, 1161 }, new int[] { 210, 10, 60, 120 }), RUNITE(4284, new int[] { 1079, 1127, 1163 }, new int[] { 250, 12, 80, 160 });

	/**
	 * The Suite of Armour id.
	 */
	private int id;

	/**
	 * The armour piece ids.
	 */
	private int[] armourPieces;

	/**
	 * The Suite of Armour stats.
	 */
	private int[] stats;

	/**
	 * Creates a new Suite of Armour instance.
	 * 
	 * @param id
	 *            the Suite of Armour id.
	 * @param armourPieces
	 *            the armour piece ids.
	 * @param stats
	 *            the Suite of Armour stats.
	 */
	private SuiteOfArmour(int id, int[] armourPieces, int[] stats) {
		this.id = id;
		this.armourPieces = armourPieces;
		this.stats = stats;
	}

	/**
	 * Gets the Suite of Armour id.
	 * 
	 * @return the Suite of Armour id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the armour piece ids.
	 * 
	 * @return the armour piece ids.
	 */
	public int[] getArmourPieces() {
		return armourPieces;
	}

	/**
	 * Gets the Suite of Armour stats.
	 * 
	 * @return the Suite of Armour stats.
	 */
	public int getStat(int index) {
		return stats[index];
	}

	/**
	 * A map containing all of the Suite of Armours.
	 */
	private static Map<Integer, SuiteOfArmour> suiteOfArmours = new HashMap<Integer, SuiteOfArmour>();

	static {
		for (SuiteOfArmour suite : SuiteOfArmour.values()) {
			for (int id : suite.getArmourPieces()) {
				suiteOfArmours.put(id, suite);
			}
		}
	}

	/**
	 * Gets the suite of armour from the armour id.
	 */
	public static SuiteOfArmour forId(int id) {
		return suiteOfArmours.get(id);
	}

}
