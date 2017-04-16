package com.bclaus.rsps.server.vd.content;

import java.util.ArrayList;
import java.util.HashMap;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.Player;

public class RandomSelection extends ScheduledTask {
	private Player player;
	private RewardType type;
	private Item requiredItem;
	private int amountOfItemsReceived;

	static final int CYCLE_EVENT_ID = Integer.MAX_VALUE - 1;

	private static HashMap<RewardType, ArrayList<Item>> rewardItems = new HashMap<>();

	static {
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(new Item(995, 5000000 + Misc.random3(10000000)));
		items.add(new Item(5608, 1));
		items.add(new Item(9470, 1));
		items.add(new Item(14506, 1));
		items.add(new Item(14502, 1));
		items.add(new Item(15584, 1));
		items.add(new Item(15588, 1));
		items.add(new Item(14504, 1));
		items.add(new Item(14505, 1));
		items.add(new Item(15584, 1));
		items.add(new Item(14507, 1));
		items.add(new Item(15381, 1));
		items.add(new Item(15380, 1));
		items.add(new Item(15388, 1));
		items.add(new Item(15387, 1));
		items.add(new Item(13220, 1));
		items.add(new Item(15577, 1));
		items.add(new Item(15386, 1));
		items.add(new Item(15385, 1));
		items.add(new Item(15384, 1));
		items.add(new Item(15383, 1));
		items.add(new Item(15495, 1));
		items.add(new Item(15494, 1));
		items.add(new Item(15493, 1));
		items.add(new Item(15492, 1));
		items.add(new Item(15491, 1));
		items.add(new Item(15490, 1));
		items.add(new Item(15499, 1));
		items.add(new Item(15498, 1));
		items.add(new Item(15497, 1));
		items.add(new Item(1037, 1));
		items.add(new Item(15813, 1));
		items.add(new Item(4565, 1));
		items.add(new Item(4732, 1));
		items.add(new Item(4734, 1));
		items.add(new Item(4736, 1));
		items.add(new Item(4738, 1));
		items.add(new Item(4716, 1));
		items.add(new Item(4718, 1));
		items.add(new Item(4720, 1));
		items.add(new Item(4722, 1));
		items.add(new Item(4745, 1));
		items.add(new Item(4747, 1));
		items.add(new Item(4749, 1));
		items.add(new Item(4751, 1));
		items.add(new Item(4724, 1));
		items.add(new Item(4726, 1));
		items.add(new Item(4728, 1));
		items.add(new Item(4730, 1));
		items.add(new Item(4708, 1));
		items.add(new Item(4710, 1));
		items.add(new Item(4712, 1));
		items.add(new Item(4714, 1));
		items.add(new Item(4753, 1));
		items.add(new Item(4755, 1));
		items.add(new Item(4757, 1));
		items.add(new Item(4759, 1));
		items.add(new Item(11728, 1));
		items.add(new Item(11732, 1));
		items.add(new Item(565, 5000));
		items.add(new Item(560, 5000));
		rewardItems.put(RewardType.MYSTERY_BOX, items);
	}

	public enum RewardType {
		MYSTERY_BOX
	}

	public RandomSelection(Player player, RewardType type, Item requiredItem, int amountOfItemsReceived) {
		this.player = player;
		this.type = type;
		this.requiredItem = requiredItem;
		this.amountOfItemsReceived = amountOfItemsReceived;
	}

	public void create() {
		if (!playerMeetsRequirements())
			return;
		Server.getTaskScheduler().schedule(new RandomSelection(player, type, requiredItem, amountOfItemsReceived));
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
			player.sendMessage("" + item.id);
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
