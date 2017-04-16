package com.bclaus.rsps.server.vd.content;

import java.util.ArrayList;
import java.util.HashMap;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

/**
 * Vote reward selection
 * 
 * @author Antony
 */
public class VoteRewardSelection extends ScheduledTask {
	
	/**
	 * Purpose of class
	 * 
	 * To select a list of Objects from a list and give the item.
	 * 
	 */

	private static HashMap<RewardType, ArrayList<Item>> rewardItems = new HashMap<>();


	static {
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(new Item(6686, 100));
		items.add(new Item(384, 50));
		items.add(new Item(390, 50));
		items.add(new Item(2435, 15));
		items.add(new Item(2441, 15));
		items.add(new Item(2437, 15));
		items.add(new Item(3025, 15));
		items.add(new Item(3145, 30));
		items.add(new Item(15596, 1));
		rewardItems.put(RewardType.VOTE_REWARD, items);
	}
	private Player player;
	private RewardType reward;
	private int amountOfItemsReceived;

	public VoteRewardSelection(Player player, RewardType reward, int amountOfItemsReceived) {
		this.player = player;
		this.reward = reward;
		this.amountOfItemsReceived = amountOfItemsReceived;

	}

	public void create() {
		Server.getTaskScheduler().schedule(new VoteRewardSelection(player, reward, amountOfItemsReceived));
	}
	public enum RewardType {
		VOTE_REWARD
	}

	@Override
	public void execute() {
		if (player == null) {
			this.stop();
			return;
		}
		if(!rewardItems.containsKey(reward)) {
			this.stop();
			return;
		}
		ArrayList<Item> items = rewardItems.get(reward);
		if(items == null || items.size() < 1)
		{
			this.stop();
			return;
		}
		ArrayList<Item> rewardItems = new ArrayList<Item>();
		while (amountOfItemsReceived > 0)
		{
			rewardItems.add(items.get(Misc.random(items.size() - 1)));
			amountOfItemsReceived--;
		}
		for(Item item : rewardItems)
		{
			if(player.getItems().freeSlots() != 0) {
				player.getItems().addItem(item.id, item.amount);
			} else {
				player.getItems().addItemToBank(item.id, item.amount);
			}
			PlayerUpdating.announce(Misc.capitalize("<col=ff0033>[" + Misc.capitalize(player.playerName)) + "]<col=363> Has just received, one or more "  + player.getItems().getItemName(item.id) + " From voting on our website!");
		} 
		
		this.stop();
	}

	public static class Item {
		int id, amount;

		public Item(int id, int amount) {
			this.id = id;
			this.amount = amount;
		}
	}

}
