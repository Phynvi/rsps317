package com.bclaus.rsps.server.vd.content.dialogue;

import java.util.HashMap;
import java.util.Map;

import com.bclaus.rsps.server.vd.content.dialogue.npc.BankerDialogue;
import com.bclaus.rsps.server.vd.content.dialogue.teleport.BossTeleportDialogue;
import com.bclaus.rsps.server.vd.content.dialogue.teleport.MinigameTeleportDialogue;
import com.bclaus.rsps.server.vd.content.dialogue.teleport.MonsterTeleportDialogue;
import com.bclaus.rsps.server.vd.content.dialogue.teleport.SkillingTeleportDialogue;
import com.bclaus.rsps.server.vd.content.dialogue.teleport.PvPTeleportDialogue;
import com.bclaus.rsps.server.vd.content.dialogue.teleport.RandomTeleportDialogue;

/**
 * A repository to contain all of the dialogues
 *
 * @author Erik Eide
 */
public class DialogueRepository {

	private static final Map<String, Dialogue> dialogues;

	static {
		/*
		 * This will suffice for now
		 */
		dialogues = new HashMap<>();
		
		dialogues.put("PVP_TELEPORTS", new PvPTeleportDialogue());
		dialogues.put("MONSTER_TELEPORTS", new MonsterTeleportDialogue());
		dialogues.put("BANKER_DIALOGUE", new BankerDialogue());
		dialogues.put("SKILLING_DIALOGUE", new SkillingTeleportDialogue());
		dialogues.put("BOSS_TELEPORT_DIALOGUE", new BossTeleportDialogue());
		dialogues.put("MINIGAME_DIALOGUE", new MinigameTeleportDialogue());
		dialogues.put("RANDOM_TELEPORTS", new RandomTeleportDialogue());
	}

	public static Map<String, Dialogue> getDialogues() {
		return dialogues;
	}

	static Dialogue getDialogue(final String name) {
		return dialogues.get(name);
	}

}