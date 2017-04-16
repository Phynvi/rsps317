package com.bclaus.rsps.potion;

import com.bclaus.rsps.server.vd.player.Player;

/**
 * 
 * @author Jason MacKeigan (http://www.rune-server.org/members/jason)
 * @since  July 28th, 2013, 4:32PM
 *
 */
public class Decanting {
	
	public static void startDecanting(Player c) {
		for(Potion p : Potion.values()) {
			int full = p.getFullId();
			int half = p.getHalfId();
			int quarter = p.getQuarterId();
			int threeQuarters = p.getThreeQuartersId();
			int totalDoses = 0;
			int remainder = 0;
			int totalEmptyPots = 0;
			if(c.getItems().playerHasItem(threeQuarters)) {
				totalDoses += (3 * c.getItems().getItemAmount(threeQuarters));
				totalEmptyPots += c.getItems().getItemAmount(threeQuarters);
				c.getItems().deleteItem(threeQuarters, c.getItems().getItemAmount(threeQuarters));
			}
			if(c.getItems().playerHasItem(half)) {
				totalDoses += (2 * c.getItems().getItemAmount(half));
				totalEmptyPots += c.getItems().getItemAmount(half);
				c.getItems().deleteItem(half, c.getItems().getItemAmount(half));
			}
			if(c.getItems().playerHasItem(quarter)) {
				totalDoses += (1 * c.getItems().getItemAmount(quarter));
				totalEmptyPots += c.getItems().getItemAmount(quarter);
				c.getItems().deleteItem(quarter, c.getItems().getItemAmount(quarter));
			}
			if(totalDoses > 0) {
				if(totalDoses >= 4)
					c.getItems().addItem(full, totalDoses / 4);
				else if(totalDoses == 3)
					c.getItems().addItem(threeQuarters, 1);
				else if(totalDoses == 2)
					c.getItems().addItem(half, 1);
				else if(totalDoses == 1)
					c.getItems().addItem(quarter, 1);
				if((totalDoses % 4) != 0) {
					totalEmptyPots -= 1;
					remainder = totalDoses % 4;
					if(remainder == 3)
						c.getItems().addItem(threeQuarters, 1);
					else if(remainder == 2)
						c.getItems().addItem(half, 1);
					else if(remainder == 1)
						c.getItems().addItem(quarter, 1);
				}
				totalEmptyPots -= (totalDoses / 4);
				c.getItems().addItem(229, totalEmptyPots);
			}
		}
	}


}