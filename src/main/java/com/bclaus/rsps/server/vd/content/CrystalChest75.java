package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.PlayerSave;

/*
 * @author Liberty / Robbie
 */

public class CrystalChest75 {

	private static final int[] CHEST_REWARDS = { 1079, 1093, 526, 1969, 371,
			2363, 451, 1079, 1093, 526, 1969, 371, 2363, 451, 1079, 1093, 526, 1969, 371, 2363, 451,
			6916, 6918, 6920, 6922, 6924, 4087, 761, 2704, 2697, 2693, 11943, 1079, 1093, 526, 1969, 371,
			2363, 451, 1079, 1093, 526, 1969, 371, 2363, 451, 1079, 1093, 526, 1969, 371, 2363, 451,
			6916, 6918, 6920, 6922, 6924, 4087, 761, 2704, 2697, 2693, 11943, 1079, 1093, 526, 1969, 371,
			2363, 451, 1079, 1093, 526, 1969, 371, 2363, 451, 1079, 1093, 526, 1969, 371, 2363, 451,
			6916, 6918, 6920, 6922, 6924, 4087, 761, 2704, 2697, 2693, 11943, 1079, 1093, 526, 1969, 371,
			2363, 451, 1079, 1093, 526, 1969, 371, 2363, 451, 1079, 1093, 526, 1969, 371, 2363, 451,
			6916, 6918, 6920, 6922, 6924, 4087, 761, 2704, 2697, 2693, 11943, 6549 };
	public static int seeds[] = { 5291, 5292, 5293, 5294, 5295, 5296, 5297,
			5298, 5299, 5300, 5301, 5302, 5303, 5304 };
	public static int tele[] = { 8007, 8008, 8009, 8010, 8011, 8012 };
	public static final int[] KEY_HALVES = { 985, 987 };
	public static final int KEY = 989;
	private static final int DRAGONSTONE = 1631;
	private static final int OPEN_ANIMATION = 881;

	public static int randomSeeds() {
		return seeds[(int) (Math.random() * seeds.length)];
	}

	public static int randomTele() {
		return tele[(int) (Math.random() * tele.length)];
	}

	public static void makeKey(Player c) {
		if (c.getItems().playerHasItem(toothHalf(), 1, 0)
				&& c.getItems().playerHasItem(loopHalf(), 1, 0)) {
			c.getItems().deleteItem( toothHalf(), 1);
			
			c.getItems().deleteItem( loopHalf(), 1);
		
			c.getItems().addItem( KEY, 1);
		}
	}

	public static boolean canOpen(Player c) {
		
		if (c.getItems().playerHasItem( KEY)) {
			return true;
		} else {
			c.sendMessage("The chest is locked");
			return false;
		}
	}

	public static void searchChest(final Player c, final int id, final int x,
			final int y) {
		if (c == null) {
			return;
		}
		if (canOpen(c) && c.chestDelay == 0) {
			c.sendMessage("You unlock the chest with your key.");
			c.getItems().deleteItem( KEY, 1);
			c.startAnimation(OPEN_ANIMATION);
			c.getItems().addItemToBank( DRAGONSTONE, 1);
			c.getItems().addItemToBank( 995, Misc.random(8230));
			c.getItems().addItemToBank( 536, Misc.random(7));
			c.getItems().addItemToBank( 385, Misc.random(20));
			c.getItems().addItemToBank( 
					CHEST_REWARDS[Misc.random(getLength() - 1)], 1);
			c.getItems();
			c.getItems().addItemToBank( randomSeeds(), Misc.random(5) + 2);
			c.getItems();
			c.getItems().addItemToBank( randomTele(), Misc.random(10));
			c.sendMessage("You find some treasure in the chest.");
			c.sendMessage("@red@The Items have been placed in your bank!");
			PlayerSave.saveGame(c);
			c.chestDelay = 4;
		}
	}

	public static int getLength() {
		return CHEST_REWARDS.length;
	}

	public static int toothHalf() {
		return KEY_HALVES[0];
	}

	public static int loopHalf() {
		return KEY_HALVES[1];
	}
}