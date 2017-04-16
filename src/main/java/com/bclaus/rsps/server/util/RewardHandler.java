/*package server.util;

import AchievementType;
import Achievements;
import Player;
import PlayerHandler;
import PlayerUpdating;
import MotivoteHandler;
import Reward;



public class RewardHandler extends MotivoteHandler<Reward> {
	@Override
	public void onCompletion(Reward reward) {

		int itemID = -1;

		if (reward.rewardName().equalsIgnoreCase("gold")) {
			itemID = 995;
		}
		if (PlayerUpdating.isPlayerOn(reward.username())) {
			Player p = PlayerUpdating.getPlayers(reward.username());
			if (p != null && p.isActive == true) {
				synchronized (p) {
					if (reward.amount() > 0) {
						p.getItems().addItemToBank(itemID, reward.amount());
						p.sendMessage("You've received your vote reward! Congratulations, annd your item has been added to the bank!");
						PlayerUpdating.announce("<col=ff0033>[VOTE]</col> " + Misc.capitalize(p.playerName) + " Has just voted and received a reward");
						p.votePoints += 5;
						p.getItems().addItemToBank(7269, 1);
						p.sendMessage("We have also placed a voting scroll in your bank to redeem for 5 vote points!");
						p.getItems().addItemToBank(3062, 1);
						reward.complete();
						Achievements.increase(p, AchievementType.VOTE, 1);
						if (Misc.random(2) == 0) {
							Player.voteRewardSystem(p);
						}
					} else {
						p.sendMessage("Could not give you your reward item, try creating space.");
					}
				}
			}
		}

	}
}*/