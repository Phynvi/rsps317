package com.bclaus.rsps.server.vd.player.packets.impl;

import com.bclaus.rsps.server.util.DisplayName;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.items.bank.BankPin;
import com.bclaus.rsps.server.vd.player.Player;

public class InputField {

	public static void execute(Player player, int id, String text) {
		if (player.getQuarantine().isQuarantined()) {
			player.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}
		switch (id) {
		case 56306:
		case 40056:
			if (player.playerRights > 0 && player.playerRights < 5) {
				player.sendMessage("You are not permitted to change your display name.");
				return;
			}
			if (!player.displayNameClaim && player.playerRights != 5) {
				player.sendMessage("This feature only exists for our supporters, please donate to use this.");
				return;
			}
			if (text.length() < 1) {
				player.sendMessage("Your display name must be atleast 1 character.");
				return;
			}
			if (text.length() > 12) {
				player.sendMessage("Your display name cannot exceed 12 characters.");
				return;
			}
			if (text.equalsIgnoreCase(player.playerName)) {
				player.sendMessage("If you wish to disable your display name, just click the toggle button.");
				return;
			}
			if (DisplayName.exists(text)) {
				player.sendMessage("That display name is already taken, please choose another.");
				return;
			}
			if (DisplayName.getDisplayName(player.playerName) != null) {
				if (System.currentTimeMillis() - DisplayName.getDisplay(player.playerName).time < (60 * 1000 * 60 * 24) && player.playerRights != 5) {
					player.sendMessage("You can only change your display name every 24 hours.");
					return;
				}
			}
			if (player.getItems().playerHasItem(995, 5000000)) {
				player.getItems().deleteItem(995, 5000000);
			} else {
				player.sendMessage("You need at least 5M to change your display name");
				return;
			}
			text = text.toLowerCase();
			text = Misc.ucFirst(text);
			player.setDisplayName(text);
			player.sendMessage("You have sucessfully changed your display name to " + text + ".");
			player.sendMessage("A sum of 5M has been deducted from your account");
			DisplayName.add(player.playerName, text);
			DisplayName.save();
			break;

		case 58063:
			if (player.getPA().viewingOtherBank) {
				player.getPA().resetOtherBank();
				return;
			}
			if (player.isBanking) {
				player.getBank().getBankSearch().setText(text);
				player.getBank().setLastSearch(System.currentTimeMillis());
				if (text.length() > 2) {
					player.getBank().getBankSearch().updateItems();
					player.getBank().setCurrentBankTab(player.getBank().getBankSearch().getTab());
					player.getItems().resetBank();
					player.getBank().getBankSearch().setSearching(true);
				} else {
					if (player.getBank().getBankSearch().isSearching())
						player.getBank().getBankSearch().reset();
					player.getBank().getBankSearch().setSearching(false);
				}
			}
			break;

		case 59507:
			if (player.getBankPin().getPinState() == BankPin.PinState.CREATE_NEW)
				player.getBankPin().create(text);
			else if (player.getBankPin().getPinState() == BankPin.PinState.UNLOCK)
				player.getBankPin().unlock(text);
			else if (player.getBankPin().getPinState() == BankPin.PinState.CANCEL_PIN)
				player.getBankPin().cancel(text);
			else if (player.getBankPin().getPinState() == BankPin.PinState.CANCEL_REQUEST)
				player.getBankPin().cancel(text);
			break;

		default:
			if (player.playerRights == 5)
				player.sendMessage("Input field: (" + id + ", " + text + ")");
			break;

		}
	}

}
