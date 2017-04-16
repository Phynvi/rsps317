package com.bclaus.rsps.server.vd.player.account_type.impl;

import java.util.Arrays;
import java.util.List;

import com.bclaus.rsps.server.vd.items.bank.BankTab;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.account_type.Account;
import com.bclaus.rsps.server.vd.player.account_type.AccountType;

/**
 * Represents an iron man account, a type chosen by a player.
 * 
 * @author Jason MacKeigan
 * @date Sep 11, 2014, 8:26:32 PM
 */
public class IronManAccount extends AccountType {

	@Override
	public String alias() {
		return "Iron Man";
	}

	@Override
	public int getPrivilege() {
		return 9;
	}

	@Override
	public boolean unownedDropsVisible() {
		return false;
	}

	@Override
	public boolean tradingPermitted() {
		return false;
	}

	@Override
	public boolean shopAccessible(int shopId) {
		return shopId == 20 || shopId == 66 || shopId == 29 || shopId == 17 || shopId == 31 || shopId == 32 || shopId == 21 || shopId == 36  || shopId == 70 || shopId == 61 || shopId == 14 || shopId == 30 || shopId == 69 || shopId == 18 || shopId == 56 || shopId == 5 || shopId == 33 || shopId == 61 || shopId == 91 || shopId == 57 || shopId == 90 ? true : false;
	}

	@Override
	public boolean changable() {
		return false;
	}

	@Override
	public boolean dropAnnouncementVisible() {
		return false;
	}

	@Override
	public boolean stakeItems() {
		return false;
	}

	@Override
	public List<String> attackableTypes() {
		return Arrays.asList(Account.IRON_MAN_TYPE.alias());
	}

	static final boolean meetsRequirement(Player player, int requirementId) {
		switch (requirementId) {
		case 1:
			for (int i = 0; i < player.playerXP.length; i++) {
				int level = Player.getLevelForXP(player.playerXP[i]);
				if (i == 3 && level != 10 || i != 3 && level != 1) {
					return false;
				}
			}
			break;

		case 2:
			for (BankTab tab : player.getBank().getBankTab()) {
				if (tab.size() > 0) {
					return false;
				}
			}
			if (player.getItems().freeSlots() != 28)
				return false;
			if (player.getItems().wearingItems())
				return false;
			break;

		case 3:
			if (player.gameMode != 2)
				return false;
			break;
		}
		return true;
	}

	public static void drawInterface(Player player) {
		player.getPA().sendFrame126((meetsRequirement(player, 1) ? "@gre@" : "@red@") + "1. Skills must be level 1 with the exception of 10 HP.", 51008);
		player.getPA().sendFrame126((meetsRequirement(player, 2) ? "@gre@" : "@red@") + "2. Account must have 0 items.", 51009);
		player.getPA().sendFrame126((meetsRequirement(player, 3) ? "@gre@" : "@red@") + "3. Account must be on game mode: Legendary.", 51010);
		player.getPA().showInterface(51000);
	}

	public static void confirm(Player player) {
		if (player.getAccount().getType().alias().equals(Account.IRON_MAN_TYPE.alias())) {
			player.sendMessage("You are already an iron man, you do not need to do this.");
			return;
		}
		if (!player.getAccount().getType().alias().equals(Account.REGULAR_TYPE.alias())) {
			player.sendMessage("Your account cannot be any other type than regular.");
			return;
		}
		if (player.gameMode < 2) {
			player.sendMessage("You do not meet one of the requirements above.");
			return;
		}
		for (int i = 0; i < 3; i++) {
			if (!meetsRequirement(player, i)) {
				player.sendMessage("You do not meet one of the requirements above.");
				return;
			}
		}
		player.pTime = 0;
		player.getItems().deleteAllItems();
		player.getBank().deleteAll();
		player.getAccount().setType(Account.IRON_MAN_TYPE);
		player.loyaltyTitle = "Iron Man";
		player.playerRights = 9;
		player.gameMode = 3;
		player.getItems().sendItemToAnyTab(995, 50000);
		player.getDH().sendNpcChat2("You are now recruited as an Iron Man. Welcome to", "our small community, best of luck on your adventure.", 4200, "Iron Man Instructor");
		player.nextChat = -1;
	}

}
