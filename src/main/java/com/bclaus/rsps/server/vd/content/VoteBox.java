package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.event.Event;
import com.bclaus.rsps.server.vd.event.EventContainer;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.event.EventManager;

/*
rewritten by Goten
 */

public class VoteBox {

	public static boolean Canusebox = true;
	
	public static int Common [] = 
	{2677, 2678, 6619, 12360, 8850 ,4131 ,2572}; // Add more item Id's
	
	public static int Uncommon [] = 
	{10828, 4587, 4586, 9185, 4087, 2678, 8850 ,10394 ,11663, 11664, 11665, 6573, }; // Add more item Id's
	
	public static int Rare [] = 
	{6575, 6573, 6570, 11690, 11732, 1837, 4087, 2679, 11663, 8839, 8840, 8841, 8842, 11664, 11665, 2680 ,10525 ,15587 ,4745 ,4749 ,4751 ,4753 ,4755 ,4757 ,4759 ,4724 ,4726 ,4728 ,11728 ,4734}; // Add more item Id's

	public static int GenerateMyrsteryPrize(final Player c) {
		 EventManager.getSingleton().addEvent(new Event() {
			int BoxTimer = 2;
			int Coins = 50000 + Misc.random(25000);
			public void execute(EventContainer Box) {
				Canusebox = false;
				if (BoxTimer == 2) {
					c.sendMessage("Calculating prize...");
				}
				if (BoxTimer == 0) {
					c.getItems().addItem(995, Coins);
					int Random = Misc.random(100);
					if (Random <= 64) {
						c.getItems().addItem(Common[(int) (Math.random() * Common.length)], 1);
						c.sendMessage("You have recieved a common @bla@item and "+ Coins +" coins.");
					} else if (Random >= 65 && Random <= 89) {
						c.getItems().addItem(Uncommon[(int) (Math.random() * Uncommon.length)], 1);
						c.sendMessage("You have recieved an uncommon item and "+ Coins +" coins.");
					} else if (Random >= 90 && Random <= 100) {
						c.getItems().addItem(Rare[(int) (Math.random() * Rare.length)], 1);
						c.sendMessage("You have recieved a rare item and "+ Coins +" coins.");
					}
				}
				if (c == null || BoxTimer <= 0) {
				   	Box.stop();
					Canusebox = true;
                    return; 
				}
				if (BoxTimer >= 0) {
					BoxTimer--;
				}
			}
		}, 1000);
		return Common[(int) (Math.random() * Common.length)];
	}
	
	public static void Open(int itemID, Player c) {
		if (itemID == 3062) {
			if (c.getItems().freeSlots() > 1) {
				if (Canusebox == true) {
					c.getItems().deleteItem(3062, 1);
					GenerateMyrsteryPrize(c);
				} else {
					c.sendMessage("Please wait while your current process is finished.");
				}
			} else {
				c.sendMessage("You need atleast 2 slots to open the Mystery box.");
			}
		}
	}
	
}