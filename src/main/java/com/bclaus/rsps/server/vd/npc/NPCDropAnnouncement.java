package com.bclaus.rsps.server.vd.npc;

import java.util.EnumSet;
import java.util.Set;

import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.items.ItemAssistant;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;

/**
 * 
 * @author Jason MacKeigan
 * @date Aug 13, 2014, 10:32:24 AM
 */
public class NPCDropAnnouncement {
	NPC npc;

	public NPCDropAnnouncement(NPC npc) {
		this.npc = npc;
	}

	public void announce(Player player, int itemId, int amount) {
		if (this.npc == null)
			return;
		if (!player.getAccount().getType().dropAnnouncementVisible())
			return;
		NPCName npc = NPCName.get(NPCHandler.getNpcListName(this.npc.npcType).replaceAll("_", " "));
		if (npc != null) {
			for (Player regionalPlayer : World.PLAYERS) {
				if (regionalPlayer == null) {
					continue;
				}
				if (regionalPlayer.distanceToPoint(this.npc.getX(), this.npc.getY()) < 40 && regionalPlayer.getAccount().getType().dropAnnouncementVisible()) {
					regionalPlayer.sendMessage("<col=0B6121>" + Misc.capitalize(player.playerName) + " has received " + amount + "x " + ItemAssistant.getItemName(itemId) + "</col>.");
				}
			}
		}
	}

	public enum NPCName {
		KING_BLACK_DRAGON, GENERAL_GRAARDOR, COMMANDER_ZILYANA, KREE_ARRA, KRIL_TSUTSAROTH, DAGANNOTH_REX, DAGANNOTH_SUPREME, DAGANNOTH_PRIME, GLOD, BARRELCHEST, GIANT_ROC, CHAOS_ELEMENTAL, SLASH_BASH, KALPHITE_QUEEN, KRAKEN, GIANT_MOLE, SCORPIA, VENEATIS, CALLISTO, VETION
		, ZULRAH;

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
	}

}
