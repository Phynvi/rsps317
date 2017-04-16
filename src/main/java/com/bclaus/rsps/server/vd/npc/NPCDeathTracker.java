package com.bclaus.rsps.server.vd.npc;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.util.Misc;

/**
 * 
 * @author Jason MacKeigan
 * @date Jul 25, 2014, 10:43:59 PM
 */
public class NPCDeathTracker {

	private Player player;
	public HashMap<NPCName, Integer> tracker = new HashMap<NPCName, Integer>();

	public HashMap<NPCName, Integer> getTracker() {
		return tracker;
	}

	public NPCDeathTracker(Player player) {
		this.player = player;
	}

	public void add(String name) {
		for (NPCName boss : NPCName.NAMES) {
			if (boss.toString().equalsIgnoreCase(name)) {
				int kills = (tracker.get(boss) == null ? 0 : tracker.get(boss)) + 1;
				tracker.put(boss, kills);
				player.sendMessage("<col=255>You have killed</col> <col=FF0000>" + kills + "</col> <col=255>" + boss.toString() + ".</col>");
				break;
			}
		}
	}

	public enum NPCName {
		KING_BLACK_DRAGON, GENERAL_GRAARDOR, COMMANDER_ZILYANA, KREE_ARRA, KRIL_TSUTSAROTH, DAGANNOTH_REX, DAGANNOTH_SUPREME, DAGANNOTH_PRIME, GLOD, MITHRIL_DRAGON, BARRELCHEST, GIANT_ROC, SKELETAL_WYVERN, CHAOS_ELEMENTAL, SLASH_BASH, KALPHITE_QUEEN, KRAKEN, GIANT_MOLE, SCORPIA, VENENATIS, CALLISTO, VETION,
		ZULRAH, JUNGLE_DEMON, ABYSSAL_DEMON, DARK_BEAST, PENANCE_QUEEN, NECHRYAEL;

		@Override
		public String toString() {
			String name = this.name().toLowerCase();
			name = name.replaceAll("_", " ");
			name = Misc.ucFirst(name);
			return name;
		}

		static final Set<NPCName> NAMES = EnumSet.allOf(NPCName.class);

		public static NPCName get(String name) {
			for (NPCName boss : NAMES) {
				if (boss.toString().equalsIgnoreCase(name))
					return boss;
			}
			return null;
		}

		// proper formatting of the name for display on interfaces
		public String format() {
			return Misc.capitalize(name().toLowerCase().replaceAll("_", " ").trim());
		}
	}

}
