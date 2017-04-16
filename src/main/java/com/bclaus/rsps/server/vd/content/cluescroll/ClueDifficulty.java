package com.bclaus.rsps.server.vd.content.cluescroll;

import java.util.Arrays;
import java.util.Optional;

import com.bclaus.rsps.server.Server;
import org.omicron.jagex.runescape.CollisionMap;

import com.bclaus.rsps.server.vd.items.IntervalItem;
import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.util.Misc;

/**
 * @author lare96 <http://github.com/lare96>
 */
public enum ClueDifficulty {
	
	
	EASY(2677, 1, 2, 1, 3, ClueScrollHandler.EASY_CLUE_REWARDS, new int[] { 391, 392, 425, 426, 413, 414, 438, 439 }) {
		@Override
		public boolean dropClue(Player player, NPC npc) {
			return npc.combatLevel >= 0 && npc.combatLevel <= 40;
		}
	},
	MEDIUM(2678, 2, 4, 2, 4, ClueScrollHandler.MEDIUM_CLUE_REWARDS, new int[] { 393, 394, 427, 428, 415, 416, 440, 441 }) {
		@Override
		public boolean dropClue(Player player, NPC npc) {
			return npc.combatLevel > 40 && npc.combatLevel <= 100;
		}
	},
	HARD(2679, 4, 6, 3, 5, ClueScrollHandler.HARD_CLUE_REWARDS, new int[] { 395, 396, 429, 430, 417, 418, 442, 443 }) {
		@Override
		public boolean dropClue(Player player, NPC npc) {
			return npc.combatLevel > 100 && !Arrays.stream(ClueScrollHandler.ELITE_CLUE_DROPS).anyMatch(id -> id == npc.npcType);
		}
	},
	ELITE(2680, 7, 10, 5, 9, ClueScrollHandler.ELITE_CLUE_REWARDS, new int[] { 395, 396, 429, 430, 417, 418, 442, 443 }) {
		@Override
		public boolean dropClue(Player player, NPC npc) {
			return Arrays.stream(ClueScrollHandler.ELITE_CLUE_DROPS).anyMatch(id -> id == npc.npcType);
		}
	};
	

	public final int clueId;
	public final int minLeft;
	public final int maxLeft;
	public final int minReward;
	public final int maxReward;
	public final IntervalItem[] rewards;
	public final int[] bosses;

	private ClueDifficulty(int clueId, int minLeft, int maxLeft, int minReward, int maxReward, IntervalItem[] rewards, int[] bosses) {
		this.clueId = clueId;
		this.minLeft = minLeft;
		this.maxLeft = maxLeft;
		this.minReward = minReward;
		this.maxReward = maxReward;
		this.rewards = rewards;
		this.bosses = bosses;
	}


	public abstract boolean dropClue(Player player, NPC npc);

	public void createBoss(Player player) {
		int boss = Misc.randomElement(bosses);
		Optional<NPC> npc = Optional.empty();

		if (!CollisionMap.isEastBlocked(player.heightLevel, player.absX - 1, player.absY)) {
			npc = NPCHandler.spawnNpc3(player, boss, player.absX - 1, player.absY, player.heightLevel, this);
			npc.ifPresent(n -> Server.getTaskScheduler().schedule(new ClueNpcDistanceEventListener(n, player)));
		} else if (!CollisionMap.isWestBlocked(player.heightLevel, player.absX + 1, player.absY)) {
			npc = NPCHandler.spawnNpc3(player, boss, player.absX + 1, player.absY, player.heightLevel, this);
			npc.ifPresent(n -> Server.getTaskScheduler().schedule(new ClueNpcDistanceEventListener(n, player)));
		} else {
			npc = NPCHandler.spawnNpc3(player, boss, player.absX, player.absY, player.heightLevel, this);
			npc.ifPresent(n -> Server.getTaskScheduler().schedule(new ClueNpcDistanceEventListener(n, player)));
		}

		npc.ifPresent(n -> {
			n.forceChat("Die human scum!");
			player.sendMessage("You must kill the clue scroll guardian.");
			player.bossDifficulty = this;
			n.forClue = true;
		});
	}

	public static Optional<ClueDifficulty> determineClue(Player player, NPC npc) {
		return Arrays.stream(values()).filter(c -> c.dropClue(player, npc)).findAny();
	}

	public static Optional<ClueDifficulty> getDifficulty(int clueId) {
		return Arrays.stream(values()).filter(c -> c.clueId == clueId).findFirst();
	}

	public static boolean isClue(int id) {
		return Arrays.stream(values()).anyMatch(c -> c.clueId == id);
	}

	public static int[] getClueIds() {
		return Arrays.stream(values()).mapToInt(i -> i.clueId).toArray();
	}
}
