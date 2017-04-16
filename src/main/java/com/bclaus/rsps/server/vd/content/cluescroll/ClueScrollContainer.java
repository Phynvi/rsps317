package com.bclaus.rsps.server.vd.content.cluescroll;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Optional;
import java.util.Queue;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.util.Chance;
import com.bclaus.rsps.server.util.Misc;

public final class ClueScrollContainer {

	private final Player player;
	public final Queue<ClueScroll> stages = new ArrayDeque<>();

	public ClueScrollContainer(Player player, ClueScroll... stages) {
		this.player = player;
		Collections.addAll(this.stages, stages);
	}

	public void current(int id) {
		ClueScroll c = stages.peek();
		if (c == null) {
			player.clueContainer = null;
			player.getItems().deleteItem(id);
			return;
		}
		player.getPA().showInterface(c.interfaceId());
	}

	public void next(int id) {
		Optional<ClueDifficulty> clueScroll = ClueDifficulty.getDifficulty(id);
		clueScroll.ifPresent(c -> {
			if (player.getItems().playerHasItem(id)) {
				player.getItems().deleteItem(id);
				stages.poll();
				if (stages.peek() == null) {
					if (Chance.COMMON.successful(Misc.RANDOM)) {
						c.createBoss(player);
					} else {
						player.bossDifficulty = c;
						ClueScrollHandler.giveReward(player);
					}
					return;
				}
				player.getItems().addItem(id, 1);
				player.sendMessage("You dig for treasure and find another clue!");
			}
		});
	}
}
