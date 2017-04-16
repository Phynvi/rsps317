package com.bclaus.rsps.server.vd.content;

import java.util.ArrayList;
import java.util.HashMap;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

public class RandomPVPSelection extends ScheduledTask {
	private Player player;
	private RewardType type;
	private Item requiredItem;
	private int amountOfItemsReceived;

	static final int CYCLE_EVENT_ID = Integer.MAX_VALUE - 1;

	private static HashMap<RewardType, ArrayList<Item>> rewardItems = new HashMap<>();

	static {
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(new Item(13913, 1));
		items.add(new Item(13919, 1));
		items.add(new Item(13910, 1));
		items.add(new Item(13916, 1));
		items.add(new Item(13900, 1));
		items.add(new Item(13903, 1));
		items.add(new Item(13868, 1));
		items.add(new Item(13924, 1));
		rewardItems.put(RewardType.MYSTERY_BOX, items);
	}

	public enum RewardType {
		MYSTERY_BOX
	}

	public RandomPVPSelection(Player player, RewardType type, Item requiredItem, int amountOfItemsReceived) {
		this.player = player;
		this.type = type;
		this.requiredItem = requiredItem;
		this.amountOfItemsReceived = amountOfItemsReceived;
	}

	public void create() {
		if (!playerMeetsRequirements())
			return;
		Server.getTaskScheduler().schedule(new RandomPVPSelection(player, type, requiredItem, amountOfItemsReceived));
	}

	@Override
	public void execute() {
		if (player == null) {
			this.stop();
			return;
		}
		if (!playerMeetsRequirements()) {
			this.stop();
			return;
		}
		if (!rewardItems.containsKey(type)) {
			this.stop();
			return;
		}
		player.getItems().deleteItem2(requiredItem.id, requiredItem.amount);
		ArrayList<Item> items = rewardItems.get(type);
		if (items == null || items.size() < 1) {
			this.stop();
			return;
		}
		ArrayList<Item> rewardItems = new ArrayList<Item>();
		while (amountOfItemsReceived > 0) {
			rewardItems.add(items.get(Misc.random(items.size() - 1)));
			amountOfItemsReceived--;
		}
		for (Item item : rewardItems) {
			player.getItems().addItem(item.id, item.amount);
		}

		this.stop();
	}

	private boolean playerMeetsRequirements() {
		if (!player.getItems().playerHasItem(requiredItem.id, requiredItem.amount)) {
			player.sendMessage("You do not have the required item to do this action.");
			return false;
		}
		if (player.getItems().freeSlots() < amountOfItemsReceived) {
			player.sendMessage("You do not have the space required to do this action.");
			return false;
		}
		return true;
	}

	public static class Item {
		int id, amount;

		public Item(int id, int amount) {
			this.id = id;
			this.amount = amount;
		}
	}
}
