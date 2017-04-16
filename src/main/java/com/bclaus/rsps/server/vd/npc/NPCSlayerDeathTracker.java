package com.bclaus.rsps.server.vd.npc;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * 
 * @author Jason MacKeigan
 * @date Jul 25, 2014, 10:43:59 PM
 */
public class NPCSlayerDeathTracker {

	private Player player;
	public HashMap<NPCNAMES, Integer> tracking = new HashMap<NPCNAMES, Integer>();

	public HashMap<NPCNAMES, Integer> getTracker() {
		return tracking;
	}

	public NPCSlayerDeathTracker(Player player) {
		this.player = player;
	}

	public void add(String name) {
		for (NPCNAMES boss : NPCNAMES.NAMES) {
			if (boss.toString().equalsIgnoreCase(name)) {
				int kills = (tracking.get(boss) == null ? 0 : tracking.get(boss)) + 1;
				tracking.put(boss, kills);
				player.sendMessage("<col=255>You have killed</col> <col=FF0000>" + kills + "</col> <col=255>" + boss.toString() + ".</col>");
				break;
			}
		}
	}

	public enum NPCNAMES {
		BATTLE_MAGE, OTHERWORDLY_BEING, YAK, SKELETON, PYREFIEND, BAT, GIANT_BAT, CAVE_CRAWLER, CRAWLING_HAND,
		DAGGANOTH, EARTH_WARRIOR, GHOST, ROCKSLUG, RED_DRAGON, COCKATRICE, BLACK_DEMON, DUST_DEVIL, ELF_WARRIOR, BANSHEE, BASILISK, BLOODVELD, BLUE_DRAGON, FIRE_GIANT,
		GREATER_DEMON, ICE_GIANT, GREEN_DRAGON, INFERNAL_MAGE, ICE_WARRIOR, JELLY, LESSER_DEMON, HILL_GIANT, MOSS_GIANT, STEEL_DRAGON, ABYSSAL_DEMON, BRONZE_DRAGON, BLACK_DRAGON, DARK_BEAST,GARGOYLE, HELLHOUND, IRON_DRAGON, KURASK,
		NECHRYAELS, CAVE_KRAKEN;

		@Override
		public String toString() {
			String name = this.name().toLowerCase();
			name = name.replaceAll("_", " ");
			name = Misc.ucFirst(name);
			return name;
		}

		static final Set<NPCNAMES> NAMES = EnumSet.allOf(NPCNAMES.class);

		public static NPCNAMES get(String name) {
			for (NPCNAMES boss : NAMES) {
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
