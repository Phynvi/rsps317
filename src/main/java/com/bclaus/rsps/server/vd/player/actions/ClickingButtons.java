package com.bclaus.rsps.server.vd.player.actions;

import static java.lang.System.currentTimeMillis;

import java.util.Optional;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.util.DisplayName;
import com.bclaus.rsps.server.vd.content.ItemOnDeath;
import com.bclaus.rsps.server.vd.content.QuickPrayer;
import com.bclaus.rsps.server.vd.content.TeleportationHandler;
import com.bclaus.rsps.server.vd.content.VotingShop;
import com.bclaus.rsps.server.vd.content.combat.CombatStyle;
import com.bclaus.rsps.server.vd.content.skills.impl.Fletching;
import com.bclaus.rsps.server.vd.content.skills.impl.crafting.Crafting;
import com.bclaus.rsps.server.vd.content.skills.impl.crafting.CraftingData;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.items.ItemList;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.account_type.impl.IronManAccount;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.content.KillcountList;
import com.bclaus.rsps.server.vd.content.LadderHandler;
import com.bclaus.rsps.server.vd.content.PVPAssistant;
import com.bclaus.rsps.server.vd.content.RandomSelection2;
import com.bclaus.rsps.server.vd.content.SlayerKillCountList;
import com.bclaus.rsps.server.vd.content.StarterHandler;
import com.bclaus.rsps.server.vd.content.combat.melee.PrayerClicks;
import com.bclaus.rsps.server.vd.content.minigames.Barrows;
import com.bclaus.rsps.server.vd.content.minigames.mercenary.Mercenary;
import com.bclaus.rsps.server.vd.content.prestige.Prestige;
import com.bclaus.rsps.server.vd.content.skills.impl.Cooking;
import com.bclaus.rsps.server.vd.content.skills.impl.RuneCrafting;
import com.bclaus.rsps.server.vd.content.skills.impl.Smelting;
import com.bclaus.rsps.server.vd.content.skills.impl.Smithing;
import com.bclaus.rsps.server.vd.content.skills.impl.crafting.LeatherMaking;
import com.bclaus.rsps.server.vd.content.skills.impl.crafting.Tanning;
import com.bclaus.rsps.server.vd.content.skills.impl.slayer.Slayer;
import com.bclaus.rsps.server.vd.content.skills.impl.slayer.SocialSlayerData;
import com.bclaus.rsps.server.vd.content.teleport.CityTeleport;
import com.bclaus.rsps.server.vd.items.GameItem;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.items.ItemAssistant;
import com.bclaus.rsps.server.vd.items.bank.BankItem;
import com.bclaus.rsps.server.vd.items.bank.BankPin;
import com.bclaus.rsps.server.vd.items.bank.BankTab;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.npc.boss.challenge.ChallengeBoss;
import com.bclaus.rsps.server.vd.player.DialogueHandler;
import com.bclaus.rsps.server.vd.player.PlayerSave;
import com.bclaus.rsps.server.vd.player.account_type.Account;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

/**
 * Clicking most buttons
 */
public class ClickingButtons implements PacketType {

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		int buttonId = Misc.hexToInt(c.getInStream().buffer, 0, packetSize);

		if (c.isDead) {
			return;
		}
		if (c.playerRights == 5) {
			c.sendMessage("ActionButtonID = <col=FF0000>" + buttonId + "</col>, DialogueID = <col=FF0000>"
					+ c.dialogueAction);
		}
		if (c.playerRights == 3) {
			c.sendMessage("ActionButtonID = <col=FF0000>" + buttonId + "</col>, DialogueID = <col=FF0000>"
					+ c.dialogueAction);
		}
		if (c.playerName.equalsIgnoreCase("aki")) {
			System.out.println("ActionButtonID = " + buttonId + ", DialogueID = " + c.dialogueAction);
		}
		if (c.getQuarantine().isQuarantined()) {
			c.sendMessage("You are quarantined making you unable to do this action.");
			return;
		}

		if (StarterHandler.handleButton(c, buttonId)) {
			return;
		}
		if (TeleportationHandler.handle(c, buttonId)) {
			return;
		}
		// c.getMusicManager().handleMusicButtons(actionButtonId);
		c.getGameOptionsInterface().performAction(buttonId);
		// Lodestone.handleButtons(c, buttonId);
		Crafting.handleCraftingClick(c, buttonId);
		LeatherMaking.craftLeather(c, buttonId);
		PrayerClicks.handleClicks(c, buttonId);
		Smelting.getBar(c, buttonId);
		CombatStyle.switchCombatType(c, buttonId);
		QuickPrayer.clickButton(c, buttonId);
		for (int l = 0; l < Fletching.otherButtons.length; l++) {
			if (buttonId == Fletching.otherButtons[l][0]) {
				Fletching.handleFletchingClick(c, buttonId);
				return;
			}
		}
		for (CraftingData.tanningData t : CraftingData.tanningData.values()) {
			if (buttonId == t.getButtonId(buttonId)) {
				Tanning.tanHide(c, buttonId);
				return;
			}
		}
		if (VotingShop.isButton(c, buttonId)) {
			VotingShop.operateButton(c, buttonId);
			return;
		}
		switch (buttonId) {
		// case 113243:
		// c.getHybrid().getHybridSet();
		// break;
		// case 113244:
		// c.getMelee().getMeleeSet();
		// break;
		// case 113245:
		// c.getRange().getRangeSet();
		// break;
		// //case 113246:
		// c.getMagic().getMageSet();
		// break;

		case 19136:
			QuickPrayer.toggle(c);
			break;
		case 113243:
			if (c.cookQP == 0) {
				c.getDH().sendDialogues(1680, -1);
			} else if (c.cookQP == 1) {
				c.getDH().sendDialogues(1693, -1);
			} else if (c.cookQP >= 2) {
				c.sendMessage("Congratulations, You have completed this Quest!");
			}
			break;
		case 113244:
			if (c.undergroundQP == 0) {
				c.getDH().sendDialogues(1710, -1);
			} else if (c.undergroundQP == 1) {
				c.getDH().sendDialogues(1711, -1);
			} else if (c.undergroundQP == 2) {
				c.getDH().sendDialogues(1729, -1);
			} else if (c.undergroundQP >= 5) {
				c.sendMessage("Congratulations , You have completed this Quest!");
			}
			break;
		case 113245:
			if (c.horrorQP == 0) {
				c.getDH().sendDialogues(1738, -1);
			} else if (c.horrorQP == 1) {
				c.getDH().sendDialogues(1737, -1);
			} else if (c.horrorQP >= 2) {
				c.sendMessage("Congratulations , You have completed this Quest!");
			}
			break;
		case 113246:
			if (c.dragonQP == 0) {
				c.getDH().sendDialogues(1738, -1);
			} else if (c.dragonQP == 1) {
				c.getDH().sendDialogues(1737, -1);
			} else if (c.dragonQP >= 2) {
				c.sendMessage("Congratulations , You have completed this Quest!");
			}
			break;
		case 113247:
			if (c.dtQP == 0) {
				c.getDH().sendDialogues(1738, -1);
			} else if (c.dtQP == 1) {
				c.getDH().sendDialogues(1737, -1);
			} else if (c.dtQP >= 2) {
				c.sendMessage("Congratulations , You have completed this Quest!");
			}
			break;
		case 75007:
			c.sendMessage("Please use the enchantment spells, instead of this");
			break;
		case 19137:
			c.setSidebarInterface(5, 17200);
			break;
		case 199067:
			IronManAccount.confirm(c);
			break;
		case 199071:
			c.getDH().sendNpcChat2("If you ever change your mind, come back.",
					"There is always a spot for new recruits.", 4200, "Iron Man Instructor");
			c.nextChat = -1;
			break;
		case 199075:
			c.getDH().sendDialogues(1602, -1);
			break;
		case 158193:
			if (c.prestigeLevel > 0) {
				c.loyaltyTitle = "The Warrior";
				c.sendMessage("You have succesfully changed your title!");
			} else
				c.sendMessage("You need to Prestige at least once to acquire this title!");
			break;
		case 158197:
			if (c.prestigeLevel > 1)
				c.loyaltyTitle = "The Fallen";
			else
				c.sendMessage("You need to Prestige at least twice to acquire this title!");
			break;
		case 158201:
			if (c.prestigeLevel > 2)
				c.loyaltyTitle = "The Divine";
			else
				c.sendMessage("You need to Prestige at least three times to acquire this title!");
			break;
		case 158205:
			if (c.prestigeLevel > 3)
				c.loyaltyTitle = "Adequate";
			else
				c.sendMessage("You need to Prestige at least four times to acquire this title!");
			break;
		case 158209:
			if (c.prestigeLevel > 4)
				c.loyaltyTitle = "The Charm";
			else
				c.sendMessage("You need to Prestige at least five times to acquire this title!");
			break;
		case 158213:
			if (c.prestigeLevel > 5)
				c.loyaltyTitle = "Restless";
			else
				c.sendMessage("You need to Prestige at least Six times to acquire this title!");
			break;
		case 158217:
			if (c.prestigeLevel > 6)
				c.loyaltyTitle = "6Ft Under";
			else
				c.sendMessage("You need to Prestige at least Seven times to acquire this title!");
			break;
		case 158221:
			if (c.prestigeLevel > 7)
				c.loyaltyTitle = "Lucky";
			else
				c.sendMessage("You need to Prestige at least eight times to acquire this title!");
			break;
		case 158255:
			if (c.prestigeLevel > 8)
				c.loyaltyTitle = "Hard Eight";
			else
				c.sendMessage("You need to Prestige at least eight times to acquire this title!");
			break;
		case 158229:
			if (c.prestigeLevel > 8)
				c.loyaltyTitle = "Nine Lives";
			else
				c.sendMessage("You need to Prestige at least eight times to acquire this title!");
			break;
		case 158233:
			if (c.prestigeLevel > 9)
				c.loyaltyTitle = "Nine Lives";
			else
				c.sendMessage("You need to Prestige at least eight times to acquire this title!");
			break;

		case 74212:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
				return;
			}
			c.getGameOptions().showUI();
			break;

		case 219238:
			break;
		case 152155:
			if (!c.displayNameClaim && c.playerRights == 5 | c.playerRights == 6 | c.playerRights == 7
					| c.playerRights == 8 | c.playerRights == 9) {
				c.sendMessage("This feature only exists for our supporters, please donate to use this.");
				return;
			}
			break;
		case 219255:
			if (c.loyaltyTitle.length() > 0) {
				c.loyaltyTitle = "";
				c.sendMessage("You have disabled your Title. It will no longer show up.");
			} else {
				c.returnTitle();
				c.sendMessage("Your game mode title has reset to default");
			}
			break;
		case 219245:
			if (!c.displayNameClaim && c.playerRights == 5 | c.playerRights == 6 | c.playerRights == 7
					| c.playerRights == 8 | c.playerRights == 9) {
				c.sendMessage("This feature only exists for our supporters, please donate to use this.");
				return;
			}
			if (c.getDisplayName() == null || DisplayName.getDisplayName(c.playerName) == null) {
				c.sendMessage("You must have a display name to toggle it first.");
				return;
			}
			c.getGameOptions().setDisplayNameActive(!c.getGameOptions().isDisplayNameActive());
			c.getGameOptions().updateUI();
			break;

		case 113228:
			c.getPA().showInterface(49000);
			break;
		/*
		 * case 191109: c.getPA().showInterface(49000);
		 * 
		 * break;
		 */
		case 191109:
			c.getAchievements().currentInterface = 0;
			c.getAchievements().drawInterface(0);
			break;
		case 114083:
			c.setSidebarInterface(2, 638);
			break;

		case 191112:
			c.getAchievements().currentInterface = 1;
			c.getAchievements().drawInterface(1);
			break;

		case 191115:
			c.getAchievements().currentInterface = 2;
			c.getAchievements().drawInterface(2);
			break;
		case 136184:
			c.getAchievements().drawInterface(c.getAchievements().currentInterface);
			break;
		case 113093:
		case 113095:
			c.setPvpInterfaceVisible(c.isPvpInterfaceVisible() ? false : true);
			c.updateWalkEntities();
			break;
		case 29031:
			c.getDH().sendOption2("Zulrah Camp", "Cave Krakens");
			c.dialogueAction = 13;
			break;

		case 20174:
			c.getPA().closeAllWindows();
			BankPin pin = c.getBankPin();
			if (pin.getPin().length() <= 0)
				c.getBankPin().open(1);
			else if (!pin.getPin().isEmpty() && !pin.isAppendingCancellation())
				c.getBankPin().open(3);
			else if (!pin.getPin().isEmpty() && pin.isAppendingCancellation())
				c.getBankPin().open(4);
			break;

		case 153009:
			c.forcedChat("My highest killstreak from killing players is " + c.highestKillStreak);
			break;
		case 153006:
			c.forcedChat("I have killed " + c.originalKillCount + " Players in a PvP area.");
			break;
		case 153003:
			c.forcedChat("I have " + c.agilityPoints + " Agility points.");
			break;
		case 153007:
			c.forcedChat("I have died to " + c.originalDeathCount + " Players in a PvP area.");
			break;
		case 153005:
			c.forcedChat("My Current KD Ratio is : " + PVPAssistant.displayRatio(c) + " From killing players");
			break;
		case 152251:
			if (System.currentTimeMillis() - c.buttonDelay < 25000) {
				c.sendMessage("You only just recently used this action");
				return;
			}
			c.forcedChat("I am playing DemonRsps as a " + Constants.gameMode(c, c.gameMode));
			c.buttonDelay = System.currentTimeMillis();
			break;
		case 153010:
			if (System.currentTimeMillis() - c.buttonDelay < 25000) {
				c.sendMessage("You only just recently used this action");
				return;
			}
			c.forcedChat("I have donated a total of $" + c.amountDonated + " to DemonRsps!");
			c.buttonDelay = System.currentTimeMillis();
			break;
		case 153008:
			if (System.currentTimeMillis() - c.buttonDelay < 25000) {
				c.sendMessage("You only just recently used this action");
				return;
			}
			c.forcedChat("My Current Killstreak is " + c.killStreak + "");
			c.buttonDelay = System.currentTimeMillis();
			break;
		case 152254:
			if (System.currentTimeMillis() - c.buttonDelay < 25000) {
				c.sendMessage("You only just recently used this action");
				return;
			}
			c.forcedChat("I have " + c.pkPoints + " Pk points.");
			c.buttonDelay = System.currentTimeMillis();
			break;
		case 152255:
			if (System.currentTimeMillis() - c.buttonDelay < 25000) {
				c.sendMessage("You only just recently used this action");
				return;
			}
			c.forcedChat("I have " + c.slaypoints + " Slayer Points.");
			c.buttonDelay = System.currentTimeMillis();
			break;
		case 153001:
			if (System.currentTimeMillis() - c.buttonDelay < 25000) {
				c.sendMessage("You only just recently used this action");
				return;
			}
			c.forcedChat("I have " + c.votePoints + " Voting points.");
			c.buttonDelay = System.currentTimeMillis();
			break;
		case 153002:
			if (System.currentTimeMillis() - c.buttonDelay < 25000) {
				c.sendMessage("You only just recently used this action");
				return;
			}
			c.forcedChat("I have " + c.getDonationPoints + " Donator points.");
			c.buttonDelay = System.currentTimeMillis();
			break;
		case 153000:
			if (System.currentTimeMillis() - c.buttonDelay < 25000) {
				c.sendMessage("You only just recently used this action");
				return;
			}
			c.forcedChat("I have " + c.pcPoints + " Pest Control points");
			c.buttonDelay = System.currentTimeMillis();
			break;

		/*
		 * Bank Searching
		 */
		case 226162:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (!c.isBanking)
				return;
			/*
			 * if (System.currentTimeMillis() - c.lastBankDeposit < 3000)
			 * return;
			 */
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			// c.lastBankDeposit = System.currentTimeMillis();
			for (int slot = 0; slot < c.playerItems.length; slot++) {
				if (c.playerItems[slot] > 0 && c.playerItemsN[slot] > 0) {
					c.getItems().addToBank(c.playerItems[slot] - 1, c.playerItemsN[slot], false);
				}
			}
			c.getItems().updateInventory();
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			break;

		case 226170:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (!c.isBanking)
				return;
			/*
			 * if (System.currentTimeMillis() - c.lastBankDeposit < 3000)
			 * return;
			 */
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				if (c.openInterface != 59500) {
					c.getBankPin().open(2);
				}
				return;
			}
			// c.lastBankDeposit = System.currentTimeMillis();
			for (int slot = 0; slot < c.playerEquipment.length; slot++) {
				if (c.playerEquipment[slot] > 0 && c.playerEquipmentN[slot] > 0) {
					c.getItems().addEquipmentToBank(c.playerEquipment[slot], slot, c.playerEquipmentN[slot], false);
					c.getItems().wearItem(-1, 0, slot);
					c.isFullHelm = Item.isFullHelm(c.playerEquipment[Player.playerHat]);
					c.isFullMask = Item.isFullMask(c.playerEquipment[Player.playerHat]);
					c.isFullBody = Item.isFullBody(c.playerEquipment[Player.playerChest]);
				}
			}
			c.getItems().updateInventory();
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			break;

		case 226186:
		case 226198:
		case 226209:
		case 226220:
		case 226231:
		case 226242:
		case 226253:
		case 227008:
		case 227019:
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				if (c.openInterface != 59500) {
					c.getBankPin().open(2);
				}
				return;
			}
			int tabId = buttonId == 226186 ? 0
					: buttonId == 226198 ? 1
							: buttonId == 226209 ? 2
									: buttonId == 226220 ? 3
											: buttonId == 226231 ? 4
													: buttonId == 226242 ? 5
															: buttonId == 226253 ? 6
																	: buttonId == 227008 ? 7
																			: buttonId == 227019 ? 8 : -1;
			if (tabId <= -1 || tabId > 8)
				return;
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset(tabId);
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			BankTab tab = c.getBank().getBankTab(tabId);
			if (tab.getTabId() == c.getBank().getCurrentBankTab().getTabId())
				return;
			if (tab.size() <= 0 && tab.getTabId() != 0) {
				c.sendMessage("Drag an item into the new tab slot to create a tab.");
				return;
			}
			c.getBank().setCurrentBankTab(tab);
			c.getPA().openUpBank();
			break;

		case 226197:
		case 226208:
		case 226219:
		case 226230:
		case 226241:
		case 226252:
		case 227007:
		case 227018:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				if (c.openInterface != 59500) {
					c.getBankPin().open(2);
				}
				return;
			}
			tabId = buttonId == 226197 ? 1
					: buttonId == 226208 ? 2
							: buttonId == 226219 ? 3
									: buttonId == 226230 ? 4
											: buttonId == 226241 ? 5
													: buttonId == 226252 ? 6
															: buttonId == 227007 ? 7 : buttonId == 227018 ? 8 : -1;
			tab = c.getBank().getBankTab(tabId);
			if (tab == null || tab.getTabId() == 0 || tab.size() == 0) {
				c.sendMessage("You cannot collapse this tab.");
				return;
			}
			if (tab.size() + c.getBank().getBankTab()[0].size() >= Constants.BANK_SIZE) {
				c.sendMessage("You cannot collapse this tab. The contents of this tab and your");
				c.sendMessage("main tab are greater than " + Constants.BANK_SIZE + " unique items.");
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			for (BankItem item : tab.getItems()) {
				c.getBank().getBankTab()[0].add(item);
			}
			tab.getItems().clear();
			if (tab.size() == 0) {
				c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
			}
			c.getPA().openUpBank();
			break;

		case 226185:
		case 226196:
		case 226207:
		case 226218:
		case 226229:
		case 226240:
		case 226251:
		case 227006:
		case 227017:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				if (c.openInterface != 59500) {
					c.getBankPin().open(2);
				}
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			tabId = buttonId == 226185 ? 0
					: buttonId == 226196 ? 1
							: buttonId == 226207 ? 2
									: buttonId == 226218 ? 3
											: buttonId == 226229 ? 4
													: buttonId == 226240 ? 5
															: buttonId == 226251 ? 6
																	: buttonId == 227006 ? 7
																			: buttonId == 227017 ? 8 : -1;
			tab = c.getBank().getBankTab(tabId);
			long value = 0;
			if (tab == null || tab.size() == 0)
				return;
			for (BankItem item : tab.getItems()) {
				Optional<ItemList> itemDef = ItemAssistant.getItemDef(item.getId() - 1);
				if (itemDef.isPresent()) {
					long tempValue = (long) (item.getId() - 1 == 995 ? 1 : itemDef.get().ShopValue);
					value += tempValue * item.getAmount();
				}
			}
			c.sendMessage("<col=255>The total networth of tab " + tab.getTabId() + " is </col><col=600000>"
					+ Misc.insertCommas(Long.toString(value)) + " gp</col>.");
			break;

		case 22024:
		case 86008:
			c.getPA().openUpBank();
			break;

		case 152244:
			c.getAchievements().drawInterface(0);
			break;
		case 2161:
			if (!c.inWild()) {
				TeleportExecutor.teleport(c, new Position(3207, 3214, 0));
			}
			break;
		case 1599:// donor box
			if (c.playerRights != 6) {
				c.getItems().deleteItem(6830, 1);
				c.playerRights = 6;
				c.getPA().closeAllWindows();
				c.sendMessage("<col=255>You are now a VD Regular Donator!</col>");
			} else if (c.playerRights == 6) {
				c.sendMessage("<col=255>You're already a VD Regular Donator.</col>");
				c.getPA().closeAllWindows();
			}
			break;
		case 1588:// premium box
			if (c.playerRights != 7) {
				c.getItems().deleteItem(6831, 1);
				c.playerRights = 7;
				c.getPA().closeAllWindows();
				c.sendMessage("<col=255>You are now a VD Extreme Donator!</col>");
			} else if (c.playerRights == 7) {
				c.sendMessage("<col=255>You're already an VD Extreme Donator.</col>");
				c.getPA().closeAllWindows();
			}
			break;
		case 28176:
			if (!c.inWild()) {
				TeleportExecutor.teleport(c, new Position(3190, 3359, 0));
			}
			break;
		case 28177:
			c.getDH().sendDialogues(777, 1);
			break;
		case 28179:
			if (!c.inWild()) {
				TeleportExecutor.teleport(c, new Position(2007, 4431, 0));
			}
			break;
		case 28180:
			if (c.combatLevel < 65 && c.questPoints < 3) {
				c.getDH().sendDialogues(974, c.npcType);
			} else {
				c.getDH().sendDialogues(975, c.npcType);
			}
			break;
		case 11015:
			if (c.gameMode >= 2) {
				c.sendMessage("This is exclusive to Regular players only");
				return;
			}
			if (c.getItems().playerHasItem(2528, 1)) {
				c.sendMessage("" + c.genieSelect);
				c.getPA().addSkillXP(52000, c.genieSelect);
				c.getItems().deleteItem(2528, c.getItems().getItemSlot(2528), 1);
				c.genieSelect = 0;
				c.getPA().closeAllWindows();
			}
			break;
		case 10252:
			c.sendMessage("You select Attack");
			c.genieSelect = 0;
			break;
		case 10253:
			c.sendMessage("You select Strength");
			c.genieSelect = 2;
			break;
		case 10254:
			c.sendMessage("You select Ranged");
			c.genieSelect = 4;
			break;
		case 10255:
			c.sendMessage("You select Magic");
			c.genieSelect = 6;
			break;
		case 11000:
			c.sendMessage("You select Defence");
			c.genieSelect = 1;
			break;
		case 11001:
			c.sendMessage("You select Hitpoints");
			c.genieSelect = 3;
			break;

		case 23132: // unmorph
			c.isMorphed = false;
			c.setSidebarInterface(1, 3917);
			c.setSidebarInterface(2, 638);
			c.setSidebarInterface(3, 3213);
			c.setSidebarInterface(4, 1644);
			c.setSidebarInterface(5, 5608);
			if (c.playerMagicBook == 0) {
				c.setSidebarInterface(6, 1151);
			} else if (c.playerMagicBook == 1) {
				c.setSidebarInterface(6, 12855);
			} else if (c.playerMagicBook == 2) {
				c.setSidebarInterface(6, 29999);
			}
			c.setSidebarInterface(7, 18128);
			c.setSidebarInterface(8, 5065);
			c.setSidebarInterface(9, 5715);
			c.setSidebarInterface(10, 2449);
			c.setSidebarInterface(11, 904);
			c.setSidebarInterface(12, 147);
			c.setSidebarInterface(13, 962);
			c.setSidebarInterface(0, 2423);
			if (c.playerEquipment[Player.playerRing] == 7927) {
				c.getItems().deleteEquipment(c.playerEquipment[Player.playerRing], Player.playerRing);
				c.getItems().addItem(7927, 1);
			}
			c.playerIsNPC = false;
			c.updateRequired = true;
			c.playerStandIndex = 0x328;
			c.playerTurnIndex = 0x337;
			c.playerWalkIndex = 0x333;
			c.playerTurn180Index = 0x334;
			c.playerTurn90CWIndex = 0x335;
			c.playerTurn90CCWIndex = 0x336;
			c.playerRunIndex = 0x338;
			c.setAppearanceUpdateRequired(true);
			break;
		case 150:
		case 89061:
		case 93202:
		case 94051:
			if (c.autoRetaliate)
				c.sendMessage("You turn your auto-retaliate off!");
			else
				c.sendMessage("You turn your auto-retaliate on!");
			c.autoRetaliate = !c.autoRetaliate;
			break;
		case 33206:
			c.getPA().quickChat(0, 0);
			break;
		case 33212:
			c.getPA().quickChat(1, 1);
			break;
		case 33209:
			c.getPA().quickChat(2, 2);
			break;
		case 33215:
			c.getPA().quickChat(3, 4);
			break;
		case 33218:
			c.getPA().quickChat(4, 5);
			break;
		case 33221:
			c.getPA().quickChat(6, 6);
			break;
		case 33224:
			c.getPA().quickChat(15, 20);
			break;
		case 33207:
			c.getPA().quickChat(5, 3);
			break;
		case 33210:
			c.getPA().quickChat(17, 16);
			break;
		case 33213:
			c.getPA().quickChat(20, 15);
			break;
		case 33216:
			c.getPA().quickChat(13, 17);
			break;
		case 33219:
			c.getPA().quickChat(18, 12);
			break;
		case 33222:
			c.getPA().quickChat(19, 9);
			break;
		case 73141:
			c.getPA().quickChat(21, 22);
			break;
		case 33208:
			c.getPA().quickChat(8, 14);
			break;
		case 33211:
			c.getPA().quickChat(9, 13);
			break;
		case 33214:
			c.getPA().quickChat(10, 10);
			break;
		case 33217:
			c.getPA().quickChat(7, 7);
			break;
		case 33220:
			c.getPA().quickChat(16, 11);
			break;
		case 33223:
			c.getPA().quickChat(11, 8);
			break;
		case 54104:
			c.getPA().quickChat(14, 19);
			break;

		case 59205:
		case 195101:
		case 47130:
			if (c.taskAmount == 0) {
				c.forcedText = "You do not have a current task please speak to Mazchna in Edgeville.";
			} else if (c.taskAmount >= 1) {
				c.forcedText = "I must slay another " + c.taskAmount + " "
						+ NPCHandler.getNpcListName(c.slayerTask).replaceAll("_", " ") + "'s.";
			}
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
			c.buttonDelay = System.currentTimeMillis();
			break;

		case 49228:
			c.getDH().sendDialogues(1253, 0);
			break;

		case 9190:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(1)) {
					break;
				}
			}
			if (c.teleAction == 670) {
				TeleportExecutor.teleport(c, new Position(3131, 3642, 0));
				c.sendMessage("Beware of other players!");
			}
			switch (c.teleAction) {
			case 50:
				TeleportExecutor.teleport(c, new Position(3565, 3316, 0));
				c.sendMessage(
						"<col=255>Make sure you visit the Barrows tunnel last otheriwse you may get glitched!</col>");
				break;
			case 1:

				TeleportExecutor.teleport(c, new Position(1860, 5245, 0));
				break;
			case 9:
				TeleportExecutor.teleport(c, new Position(3087, 3504, 0));
				break;
			case 222:
				TeleportExecutor.teleport(c, new Position(1891, 4409, 0));
				break;
			case 43:
				c.getPA().resetSkill(4, 0);
				break;
			}
			switch (c.dialogueAction) {
			case 1612:
				c.getDH().sendDialogues(1629, 200);
				break;
			case 1661:
				c.getDH().sendDialogues(1666, 200);
				break;
			case 1662:
				c.getDH().sendDialogues(1662, 200);
				break;
			case 27:
				Prestige.specializeInSkill(c, 0);
				break;

			case 10:
				TeleportExecutor.teleport(c, new Position(2845, 4832, 0));
				break;
			case 11:
				TeleportExecutor.teleport(c, new Position(2587, 4836, 0));
				break;
			case 12:
				TeleportExecutor.teleport(c, new Position(2398, 4841, 0));
				break;
			case 103:
				TeleportExecutor.teleport(c, new Position(2444, 5170, 0));
				c.sendMessage(
						"Enter the cave to battle Jad. Or kill the Thzaar for Tokuls which are used in the shop.");
				break;
			case 231:
				TeleportExecutor.teleport(c, new Position(2698, 3206, 0));
				break;
			case 1622:
				c.sendMessage("You have been given max stats.");
				for (int i = 0; i < c.playerLevel.length; i++) {
					c.playerLevel[i] = 99;
					c.playerXP[i] = c.getPA().getXPForLevel(99);
					c.getPA().refreshSkill(i);
				}
				break;
			}
			break;
		case 9191:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(2)) {
					break;
				}
			}
			if (c.teleAction == 670) {
				TeleportExecutor.teleport(c, new Position(2394, 4682, 0));
			}
			switch (c.teleAction) {
			case 50:
				TeleportExecutor.teleport(c, new Position(2662, 2650, 0));
				break;
			case 9:
				TeleportExecutor.teleport(c, new Position(3223, 3218, 0));
				break;
			case 43:
				c.getPA().resetSkill(5, 0);
				break;
			}
			switch (c.dialogueAction) {
			case 1612:
				c.getDH().sendDialogues(1630, 200);
				break;
			case 1661:
				c.getDH().sendDialogues(1667, 200);
				break;
			case 10:
				TeleportExecutor.teleport(c, new Position(2788, 4835, 0));
				break;
			case 11:
				TeleportExecutor.teleport(c, new Position(2527, 4833, 0));
				break;
			case 103:
				TeleportExecutor.teleport(c, new Position(2605, 3153, 0));
			case 231:
				TeleportExecutor.teleport(c, new Position(3537, 3448, 0));
				break;
			case 12:
				TeleportExecutor.teleport(c, new Position(2464, 4834, 0));
				break;
			case 1622:
				for (int i = 0; i < c.playerLevel.length; i++) {
					c.playerLevel[i] = 1;
					c.playerXP[i] = c.getPA().getXPForLevel(1);
					c.getPA().refreshSkill(i);
				}
				break;
			}
			break;

		case 9192:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(3)) {
					break;
				}
			}
			if (c.teleAction == 670) {
				TeleportExecutor.teleport(c, new Position(2865, 9921, 0));
			}
			switch (c.teleAction) {
			case 1:
				TeleportExecutor.teleport(c, new Position(2794, 2786, 0));
				break;
			case 50:
				TeleportExecutor.teleport(c, new Position(2444, 5170, 0));
				break;
			case 9:
				TeleportExecutor.teleport(c, new Position(3214, 3424, 0));
				break;
			case 43:
				c.getPA().resetSkill(6, 0);
				break;
			}
			Object player;
			switch (c.dialogueAction) {
			case 1612:
				c.getDH().sendDialogues(1632, 200);
				break;
			case 1661:
				c.getDH().sendDialogues(1667, 200);
				break;
			case 10:
				TeleportExecutor.teleport(c, new Position(2713, 4836, 0));
				break;
			case 11:
				TeleportExecutor.teleport(c, new Position(2162, 4833, 0));
				break;
			case 12:
				TeleportExecutor.teleport(c, new Position(2207, 4836, 0));
				break;
			case 103:
				TeleportExecutor.teleport(c, new Position(2846, 3541, 0));
				break;
			case 231:
				TeleportExecutor.teleport(c, new Position(2786, 3762, 0));
				break;
			case 1622:
				c.getPA().enterCaves();
				break;
			// todo
			}
			break;

		case 9193:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(4)) {
					break;
				}
			}
			if (c.teleAction == 670) {
				TeleportExecutor.teleport(c, new Position(3205, 9362, 0));
			}
			switch (c.teleAction) {
			case 1:
				TeleportExecutor.teleport(c, new Position(2894, 2727, 0));
				// c.getDH().sendDialogues(309, 0);
				break;
			case 50:
				TeleportExecutor.teleport(c, new Position(3366, 3266, 0));
				break;
			case 9:
				TeleportExecutor.teleport(c, new Position(2964, 3378, 0));
				break;
			case 43:
				if (c.getItems().playerHasItem(995, 7000000)) {
					for (int i = 0; i < 7; i++) {
						if (i != 3)
							c.getPA().resetSkill(i, 0);
					}
					c.getPA().resetSkill(3, 10);
				} else {
					c.sendMessage("@red@You don't have enough cash to reset! You need 7m");
					c.getPA().closeAllWindows();
				}
				break;
			}

			switch (c.dialogueAction) {
			case 500:
				c.getDH().sendOption2("Yes", "No");
				c.dialogueAction = 501;
				break;
			case 1661:
				c.getDH().sendDialogues(1669, 200);
				break;
			case 222:
				TeleportExecutor.teleport(c, new Position(3047, 9544, 0));
				break;
			case 10:
				TeleportExecutor.teleport(c, new Position(2660, 4839, 0));
				break;
			case 11:
				TeleportExecutor.teleport(c, new Position(2155, 3864, 0));
				break;
			case 12:
				RuneCrafting.craftRunesOnAltar(c, 77, 11, 565, 89, 94, 99);
				c.getPA().removeAllWindows();
				break;
			case 103:
				TeleportExecutor.teleport(c, new Position(2399, 5177, 0));
				break;
			case 231:
				TeleportExecutor.teleport(c, new Position(2534, 2965, 0));
				break;
			case 1622:
				c.getPA().openUpBank();
				break;
			// todo
			}
			break;
		case 9194:
			if (c.teleAction == 670) {
				c.sendMessage("Coming soon.");
				c.getPA().closeAllWindows();
			}
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(5)) {
					break;
				}
			}
			switch (c.teleAction) {
			case 50:
				TeleportExecutor.teleport(c, new Position(3047, 9544, 0));
				break;
			case 9:
				TeleportExecutor.teleport(c, new Position(2525, 4777, 0));
				break;
			case 43:
				c.getDH().sendOption4("Reset Attack", "Reset Strength", "Reset Defence", "More...");
				c.dialogueAction = 42;
				break;
			}
			switch (c.dialogueAction) {
			case 500:
				c.slayerBossTask = true;
				c.setSocialSlayer(c, c.socialSlayerInviteClient);
				c.getSocialSlayer().getPartner().setSocialSlayer(c.socialSlayerInviteClient, c);
				c.setLastSocialPlayer(c.getSocialSlayer().getPartner().playerName);
				c.getSocialSlayer().getPartner().setLastSocialPlayer(c.playerName);
				c.getSocialSlayer().getPartner().setSocialSlayerDisconnections(0);
				c.setSocialSlayerDisconnections(0);
				c.getSocialSlayer().getPartner().sendMessage(
						c.playerName + " has accepted your request, you are now social slaying with him/her.");
				c.getSocialSlayer().getClient().sendMessage(
						"You are now social slaying with " + c.getSocialSlayer().getPartner().playerName + ".");
				c.getSocialSlayer().createSocialTask();
				c.getPA().removeAllWindows();
				break;
			case 1661:
				c.getPA().closeAllWindows();
				break;
			case 10:
			case 11:
				c.dialogueId++;
				c.getDH().sendDialogues(c.dialogueId, 0);
				break;
			case 12:
				c.dialogueId = 17;
				c.getDH().sendDialogues(c.dialogueId, 0);
				break;
			case 231:
				TeleportExecutor.teleport(c, new Position(2902, 2981, 0));
				break;
			case 1622:
				c.getItems().addItem(10662, 1);
				break;
			}
			break;

		case 71074:

			break;
		case 83093:
			c.dialogueAction = 2525;
			c.getDH().sendOption2("Show kept items on death", "Show Equipment bonuses");
			System.out.println("this is being called");
			break;
		case 15147:
			if (c.smeltInterface) {
				c.smeltType = 2349;
				c.smeltAmount = 1;
				Smithing.startSmelting(c, c.smeltType);
			}
			break;

		case 15151:
			if (c.smeltInterface) {
				c.smeltType = 2351;
				c.smeltAmount = 1;
				Smithing.startSmelting(c, c.smeltType);
			}
			break;

		case 15159:
			if (c.smeltInterface) {
				c.smeltType = 2353;
				c.smeltAmount = 1;
				Smithing.startSmelting(c, c.smeltType);

			}
			break;

		case 29017:
			if (c.smeltInterface) {
				c.smeltType = 2359;
				c.smeltAmount = 1;
				Smithing.startSmelting(c, c.smeltType);

			}
			break;

		case 29022:
			if (c.smeltInterface) {
				c.smeltType = 2361;
				c.smeltAmount = 1;
				Smithing.startSmelting(c, c.smeltType);

			}
			break;

		case 29026:
			if (c.smeltInterface) {
				c.smeltType = 2363;
				c.smeltAmount = 1;
				Smithing.startSmelting(c, c.smeltType);

			}
			break;

		case 58253:
			c.getItems().writeBonus();
			break;

		case 39178:
			c.startAnimation(65535);
			c.getPA().removeAllWindows();
			break;

		case 59004:
			c.getPA().removeAllWindows();
			break;
		case 70212:
			if (c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
				return;
			}
			c.getPA().showInterface(18300);
			break;
		case 62137:
			if (c.clanId >= 0) {
				c.sendMessage("You are already in a clan.");
				break;
			}
			if (c.getOutStream() != null) {
				c.getOutStream().createFrame(187);
				c.flushOutStream();
			}
			break;

		case 9167:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(1)) {
					break;
				}
			}
			if (c.dialogueAction == 1812) {
				c.horrorQP += 5;
				c.getItems().addItem(3842, 1);
				c.sendMessage("Congratulations , You have completed this Quest!");
				c.getPA().loadAllQuests(c);
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 2007) {
				TeleportExecutor.teleport(c, new Position(2513, 4645, 0));
				c.getPA().removeAllWindows();
			}

			if (c.dialogueAction == 666) {
				if (c.getItems().playerHasItem(995, ChallengeBoss.getCost(ChallengeBoss.KRAKEN))) {
					ChallengeBoss.spawnSignleMonster(c, ChallengeBoss.KRAKEN);
					c.getItems().deleteItem(995, ChallengeBoss.getCost(ChallengeBoss.KRAKEN));
				} else
					c.sendMessage("<col=698296>You need 2 mil to attempt this challenge!");
				c.getPA().closeAllWindows();
			}
			switch (c.dialogueAction) {
			case 1503:
				c.sendMessage("<col=698296>You currently have " + c.getDonationPoints
						+ " Donator Points! 1$ = 100 Donator Points.");
				c.getShops().openShop(66);
				break;
			case 2287:
				c.sendMessage("You currently have " + c.getDonationPoints + " Donator Points!");
				c.getShops().openShop(26);
				break;
			case 1527:
				c.getDH().sendDialogues(1536, 4288);
				break;
			case 1622: // XXX
				if (Misc.random(1) == 0) {
					TeleportExecutor.teleport(c, new Position(2677, 3715, 0));
				} else {
					TeleportExecutor.teleport(c, new Position(2696, 3718, 0));
				}
				c.dialogueAction = -1;

				break;
			case 1621:
				TeleportExecutor.teleport(c, new Position(3220, 3218, 0));
				break;
			case 1617:
				if (c.getItems().playerHasItem(995, 500000)) {
					c.getItems().deleteItem(995, 500000);
					c.zulrahEntries += 1;
					c.sendMessage("You now have <col=ff0033>" + c.zulrahEntries + "</col> Zulrah entry points. ");
				} else {
					c.sendMessage("You need at least 500K GP to purchase an entry point");
				}
				c.getPA().closeAllWindows();
				break;
			case 1609:
				KillcountList.openInterface(c);
				c.sendMessage("Did you know you can open this interface by operating a ring of wealth?");
				break;
			case 1611:
				c.getDH().sendDialogues(1637, 538);
				break;
			case 1607:
				c.getDH().sendDialogues(1611, 945);
				break;
			case 1524:
				if (!c.hasBoughtCannon) {
					c.sendMessage("You must purchase the cannon-addition to use this feature");
				} else {
					if (c.getItems().bankContains(6) || c.getItems().playerHasItem(6)) {
						c.sendMessage("You already own this piece");
					} else {
						c.getItems().addItemToBank(6, 1);
					}
					if (c.getItems().bankContains(8) || c.getItems().playerHasItem(8)) {
						c.sendMessage("You already own this piece");
					} else {
						c.getItems().addItemToBank(8, 1);
					}
					if (c.getItems().bankContains(10) || c.getItems().playerHasItem(10)) {
						c.sendMessage("You already own this piece");
					} else {
						c.getItems().addItemToBank(10, 1);
					}
					if (c.getItems().bankContains(12) || c.getItems().playerHasItem(12)) {
						c.sendMessage("You already own this piece");
					} else {
						c.getItems().addItemToBank(12, 1);
					}
				}
				c.getPA().closeAllWindows();
				break;
			case 1412:
				Slayer.generateTask(c, null);
				break;
			case 1420:
				c.gameMode = 0;
				c.modeTut = true;
				c.getPA().removeAllWindows();
				c.sendMessage("Welcome to DemonRsps, this mode is unable to use our prestige feature");
				c.sendMessage("You also cannot be an Iron man. That feature is exclusive to Legendary players only.");
				c.appearanceUpdateRequired = true;
				c.updateRequired = true;
				c.getPA().showInterface(3559);
				c.canChangeAppearance = true;
				break;
			case 1428:
				Prestige.achievePrestige(c);
				break;
			case 1395:
				c.getDH().sendDialogues(1396, c.npcType);
				break;
			case 1391:
				c.getDH().sendDialogues(1392, c.npcType);
				break;
			case 271:
				c.getDH().sendDialogues(1304, c.npcType);
				break;
			case 253:
				if (!c.removeBankMessage) {
					c.getDH().sendDialogues(1214, c.npcType);
				} else {
					c.getDH().sendDialogues(1215, c.npcType);
				}
				break;
			case 14:
				TeleportExecutor.teleport(c, new Position(2980, 3866, 0));
				break;
			case 230:
				if (c.getItems().playerHasItem(995, 875)) {
					c.getDH().sendDialogues(706, c.npcType);
				} else {
					c.getDH().sendDialogues(709, c.npcType);
				}
				break;
			case 234:
				c.getDH().sendDialogues(743, c.npcType);
				break;
			case 235:
				c.getDH().sendDialogues(753, c.npcType);
				break;
			case 239:
				c.getDH().sendDialogues(827, c.npcType);
				break;
			case 240:
				c.getDH().sendDialogues(843, c.npcType);
				break;
			}
			break;

		case 9168:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(2)) {
					break;
				}
			}
			if (c.dialogueAction == 1812) {
				c.horrorQP += 5;
				c.getItems().addItem(3844, 1);
				c.sendMessage("Congratulations , You have completed this Quest!");
				c.getPA().loadAllQuests(c);
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 666) {
				if (c.getItems().playerHasItem(995, ChallengeBoss.getCost(ChallengeBoss.VETION))) {
					c.getItems().deleteItem(995, ChallengeBoss.getCost(ChallengeBoss.VETION));
					ChallengeBoss.spawnSignleMonster(c, ChallengeBoss.VETION);
				} else
					c.sendMessage("<col=698296>You need 750k to attempt this challenge!");
				c.getPA().closeAllWindows();
			}
			if (c.dialogueAction == 1412 || c.dialogueAction == 1413) {
				c.getDH().sendDialogues(1418, 1596);
			}
			switch (c.dialogueAction) {
			case 1503:
				c.sendMessage("Coming very soon.");
				c.getPA().closeAllWindows();
				break;
			case 1527:
				c.getDH().sendDialogues(1545, 4288);
				break;
			case 1622: // XXX
				TeleportExecutor.teleport(c, new Position(3123, 9909, 0));
				break;
			case 1621:
				TeleportExecutor.teleport(c, new Position(2278, 3122, 0));
				break;
			case 1617:
				if (c.getItems().playerHasItem(995, 2500000)) {
					c.getItems().deleteItem(995, 2500000);
					c.zulrahEntries += 5;
					c.sendMessage("You now have <col=ff0033>" + c.zulrahEntries + "</col> Zulrah entry points. ");
				} else {
					c.sendMessage("You need at least 2.5m GP to gain 5 entry points.");
				}
				c.getPA().closeAllWindows();
				break;
			case 1609:
				SlayerKillCountList.openInterface(c);
				c.sendMessage("You must kill a slayer-npc for them to appear on this list");
				break;
			case 1611:
				c.getDH().sendDialogues(1625, 538);
				break;
			case 1428:
				c.getDH().sendDialogues(1544, 1688);
				break;
			case 1524:
				if (c.hasBoughtCannon) {
					c.getDH().sendDialogues(1527, c.npcType);
				} else {
					c.sendMessage("You must purchase the dwarf-multi cannon package to use this feature");
					c.getPA().closeAllWindows();
				}
				break;
			case 1412:
				c.getDH().sendDialogues(1419, c.npcType);
				break;
			case 503:
				for (Player p : World.PLAYERS) {
					if (p != null) {
						Player partner = p;
						if (partner.playerName.equalsIgnoreCase(c.getLastSocialPlayer())) {
							if (partner != null) {
								if (partner.isSocialSlaying() && partner.getSocialSlayer() != null) {
									if (partner.getSocialSlayer().getPartner().playerName
											.equalsIgnoreCase(c.playerName)) {
										if (c.slayerTask == partner.slayerTask) {
											c.taskAmount = partner.taskAmount;
											c.sendMessage(
													"Your task amount has been lowered to " + partner.taskAmount + ".");
											c.setSocialSlayer(c, partner);
											c.sendMessage("You are still social slaying with your partner "
													+ partner.playerName + ".");
											partner.sendMessage(c.playerName
													+ " is still social slaying with you now that they're online.");
											c.getPA().removeAllWindows();
										} else {
											c.sendMessage("That player is no longer on the same task as you.");
											SocialSlayerData.finalizeSlayer(c);
											return;
										}
									} else {
										c.sendMessage("Your previous parter is social slaying with someone else now.");
										SocialSlayerData.finalizeSlayer(c);
										return;
									}
								} else {
									c.sendMessage("That player is no longer social slaying.");
									SocialSlayerData.finalizeSlayer(c);
									c.getPA().removeAllWindows();
								}
							}
						}
					}
				}
				break;
			case 2007:
				TeleportExecutor.teleport(c, new Position(2916, 9912, 0));
				break;
			case 1395:
				c.getDH().sendDialogues(1400, c.npcType);
				break;
			case 1391:
				c.getDH().sendDialogues(1397, c.npcType);
				break;
			case 271:
				c.getDH().sendDialogues(1308, c.npcType);
				break;
			case 14:
				TeleportExecutor.teleport(c, new Position(3357, 3721, 0));
				break;
			case 230:
				c.getDH().sendDialogues(702, c.npcType);
				break;
			case 234:
				c.getDH().sendDialogues(761, c.npcType);
				break;
			case 235:
				c.getDH().sendDialogues(749, c.npcType);
				break;
			case 239:
				c.getDH().sendDialogues(828, c.npcType);
				break;
			case 240:
				c.getDH().sendDialogues(834, c.npcType);
				break;
			}
			break;

		case 9169:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(3)) {
					break;
				}
			}
			if (c.dialogueAction == 1812) {
				c.horrorQP += 5;
				c.getItems().addItem(3840, 1);
				c.getPA().loadAllQuests(c);
				c.sendMessage("Congratulations , You have completed this Quest!");
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 666) {
				if (c.getItems().playerHasItem(995, ChallengeBoss.getCost(ChallengeBoss.SCORPIANO))) {
					c.getItems().deleteItem(995, ChallengeBoss.getCost(ChallengeBoss.SCORPIANO));
					ChallengeBoss.spawnSignleMonster(c, ChallengeBoss.SCORPIANO);
					c.getPA().removeAllWindows();
				} else

					c.sendMessage("<col=698296>You need 500k to attempt this challenge!");
				c.getPA().closeAllWindows();
			}
			switch (c.dialogueAction) {
			case 1503:
				c.getPA().closeAllWindows();
				c.sendMessage("Thanks for looking at the donator shop.");
				break;
			case 1622:
				TeleportExecutor.teleport(c, new Position(3558, 9945, 0));
				c.dialogueAction = -1;
				break;
			case 1621:
				TeleportExecutor.teleport(c, new Position(2400, 4681, 0));
				break;
			case 1617:
				if (c.getItems().playerHasItem(995, 5000000)) {
					c.getItems().deleteItem(995, 5000000);
					c.zulrahEntries += 10;
					c.sendMessage("You now have <col=ff0033>" + c.zulrahEntries + "</col> Zulrah entry points. ");
				} else {
					c.sendMessage("You need at least 5m GP to gain 10 entry points.");
				}
				c.getPA().closeAllWindows();
				break;
			case 1609:
				c.getDH().sendDialogues(1623, c.npcType);
				break;
			case 1611:
				if (c.hasDisabledEpDrops) {
					c.sendMessage(
							"EP drops are now enabled, you will now receive drops based on your EP in varrock & falador");
					c.sendMessage("You can speak to me again to disable these drops again.");
					c.hasDisabledEpDrops = false;
				} else {
					c.sendMessage(
							"You have just disabled your EP drops. This means you can collect EP and get higher rewards");
					c.hasDisabledEpDrops = true;
				}
				c.getPA().closeAllWindows();
				break;
			case 1420:
				c.gameMode = 2;
				c.loyaltyTitle = "The Legendary";
				c.modeTut = true;
				c.getPA().removeAllWindows();
				c.sendMessage("You have selected the hardest Difficiulty");
				c.sendMessage("You receive a Personal whip.");
				c.getItems().addItem(3281, 1);
				c.sendMessage("You have completed the achievement Learn the ropes, by completing the tutorial.");
				c.appearanceUpdateRequired = true;
				c.updateRequired = true;
				break;
			case 1524:
				if (c.getItems().playerHasItem(6829, 1)) {
					c.getItems().deleteItem(6829, 1);
					c.hasBoughtCannon = true;
					c.sendMessage("You can now claim your cannon");
				} else {
					c.sendMessage("You do not have any status boxes to claim!");
				}
				c.getPA().closeAllWindows();
				break;
			case 503:
				SocialSlayerData.finalizeSlayer(c);
				c.getPA().removeAllWindows();
				c.sendMessage("You have decided to discontinue any chance of re-joining your social slayer friend.");
				break;
			case 1428:
				Prestige.handleInterface(c);
				break;
			case 1395:
				c.getDH().sendDialogues(1402, c.npcType);
				break;
			case 1391:
				c.getDH().sendDialogues(1402, c.npcType);
				break;
			case 271:
				c.getDH().sendDialogues(1303, c.npcType);
				break;
			case 14:
				TeleportExecutor.teleport(c, new Position(2541, 4714, 0));
				break;
			case 230:
				c.getDH().sendDialogues(703, c.npcType);
				break;
			case 234:
				c.getDH().sendDialogues(763, c.npcType);
				break;
			case 235:
				c.getDH().sendDialogues(752, c.npcType);
				break;
			case 239:
				c.getDH().sendDialogues(829, c.npcType);
				break;
			case 240:
				c.getDH().sendDialogues(837, c.npcType);
				break;
			}
			break;

		case 9178:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(1)) {
					break;
				}
			}
			switch (c.dialogueAction) {
			// fally
			case 697:
				TeleportExecutor.teleport(c, new Position(CityTeleport.FALADOR));
				;
				break;
			case 1612:
				if (c.originalKillCount > 150) {
					c.sendMessage("You have succesfully purchased this title");
					c.sendMessage(" from your account..");
					c.loyaltyTitle = "<col=CC0099>Junior Cadet";
				} else {
					c.sendMessage("Unfortunately this Title requires 150 Pk kills, you only have" + c.originalKillCount
							+ " kills.");
				}
				c.getPA().closeAllWindows();
				break;
			case 1610:
				c.loyaltyTitle = "Annihilator";
				c.sendMessage("Your title is now changed");
				c.getPA().closeAllWindows();
				break;
			case 1606:
				c.getDH().sendDialogues(1607, 945);
				break;
			case 1529:
				TeleportExecutor.teleport(c, new Position(3208, 3767, 0));
				c.sendMessage("Run directly north from this location!");
				break;
			case 1523:
				if (c.votePoints > 4) {
					c.voteExperienceMultiplier += 3000;
					c.votePoints -= 5;
					c.sendMessage("You have just purchased Double Experience.. Relog to activate");
				} else {
					c.sendMessage("You need at least 5 Voting points to purchase this!");
				}
				c.getPA().closeAllWindows();
				break;
			case 1521:
				if (c.canClaim) {
					c.loyaltyTitle = "King";
					c.canClaim = false;
					c.sendMessage("Thanks for your continued support! Relog for your title!");
				}
				break;
			case 1503:
				c.sendMessage("<col=698296>You currently have " + c.getDonationPoints
						+ " Donator Points! 1$ = 100 Donator Points.");
				c.getShops().openShop(66);
				break;
			case 1422:
				c.getDH().sendDialogues(1423, 5030);
				break;
			case 1412:
				Slayer.generateTask(c, null);
				break;
			case 230:
				c.getDH().sendDialogues(1330, c.npcType);
				break;
			case 251:
				c.getPA().openUpBank();
				break;
			case 20:
				if (c.getPet() != null)
					c.getPet().dispose();
				TeleportExecutor.teleport(c, new Position(2825, 5302, 6));
				c.killCount = 0;
				break;
			case 42:
				c.getPA().resetSkill(0, 0);
				break;
			case 106:
				TeleportExecutor.teleport(c, new Position(2539, 4716, 0));
				break;
			case 110:
				c.getDH().sendDialogues(328, c.npcType);
				break;
			case 111:
				c.getDH().sendDialogues(351, c.npcType);
				break;
			case 222:
				TeleportExecutor.teleport(c, new Position(1891, 4409, 0));
				break;
			case 223:
				c.getDH().sendDialogues(482, c.npcType);
				break;
			case 228:
				c.getDH().sendDialogues(698, c.npcType);
				break;
			case 229:
				TeleportExecutor.teleport(c, new Position(2716, 9135, 0));
				break;
			case 232:
				c.getShops().openShop(13);
				break;
			case 237:
				c.getDH().sendDialogues(784, 1552);
				break;
			case 246:
				c.getDH().sendDialogues(913, c.npcType);
				break;
			}
			break;

		case 9179:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(2)) {
					break;
				}
			}
			switch (c.dialogueAction) {
			// Varrock
			case 697:
				TeleportExecutor.teleport(c, new Position(CityTeleport.VARROCK));
				break;
			case 1612:
				if (c.originalKillCount > 250) {
					c.sendMessage("You have succesfully earned this title, For killing 250 players");
					c.sendMessage(" from your account..");
					c.loyaltyTitle = "<col=CC0099>Sergeant";
				} else {
					c.sendMessage("Unfortunately this Title requires you to killl 250 players");
				}
				c.getPA().closeAllWindows();
				break;
			case 1610:
				if (KillcountList.getTotalKills(c) > 2100) {
					c.loyaltyTitle = "Boss Hunter";
					c.sendMessage("Your title is now 'Boss Hunter'");
				} else {
					c.sendMessage("You need at least 2100 total boss kills!");

				}
				c.getPA().closeAllWindows();
				break;
			case 1606:
				c.getDH().sendDialogues(1611, 945);
				break;
			case 1529:
				TeleportExecutor.teleport(c, new Position(3360, 3715, 0));
				c.sendMessage("Move directly north from this location!");
				break;
			case 1523:
				if (c.votePoints > 4) {
					c.logout();
					c.dropRateIncreaser += 3000;
					c.votePoints -= 5;
					c.sendMessage("You have just purchased increased Drop-rate.. Relog to activate");
				} else {
					c.sendMessage("You need at least 5 Voting points to purchase this!");
				}
				c.getPA().closeAllWindows();
				break;
			case 1521:
				if (c.canClaim) {
					c.loyaltyTitle = "Queen";
					c.canClaim = false;
					c.sendMessage("Thanks for your continued support! Relog for your title!");
				}
				break;
			case 1503:
				c.sendMessage("Feature coming soon");
				break;
			case 1412:
				c.getDH().sendDialogues(1419, c.talkingNpc);
				break;
			case 1422:
				c.getShops().openShop(56);
				break;
			case 230:
				c.getDH().sendDialogues(1322, c.npcType);
				break;
			case 3:
				TeleportExecutor.teleport(c, new Position(3243, 3513, 0));
				break;
			case 20:
				if (c.getPet() != null) {
					c.getPet().dispose();
				}
				TeleportExecutor.teleport(c, new Position(2873, 5354, 6));
				c.killCount = 0;
				break;
			case 42:
				c.getPA().resetSkill(2, 0);
				break;
			case 103:
				TeleportExecutor.teleport(c, new Position(2846, 3541, 0));
				break;
			case 106:
				TeleportExecutor.teleport(c, new Position(3244, 3512, 0));
				break;
			case 110:
				c.getDH().sendDialogues(332, c.npcType);
				break;
			case 111:
				c.getDH().sendDialogues(353, c.npcType);
				break;
			case 115:
				TeleportExecutor.teleport(c, new Position(2477, 10147, 0));
				break;
			case 222:
				TeleportExecutor.teleport(c, new Position(3295, 3921, 0));
				break;
			case 223:
				c.getDH().sendDialogues(477, c.npcType);
				break;
			case 228:
				c.getDH().sendDialogues(695, c.npcType);
				break;
			case 229:
				c.getDH().sendDialogues(309, 0);
				// TeleportExecutor.teleport(c, 2744, 3149, 0);
				break;
			case 232:
				c.getDH().sendDialogues(717, c.npcType);
				break;
			case 237:
				c.getDH().sendDialogues(807, 1552);
				break;
			case 246:
				c.getDH().sendDialogues(917, c.npcType);
				break;
			}
			break;

		case 9180:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(3)) {
					break;
				}
			}
			switch (c.dialogueAction) {
			// Lumby
			case 697:
				TeleportExecutor.teleport(c, new Position(CityTeleport.LUMBRIDGE));
				;
				break;
			case 1612:
				if (c.originalKillCount > 500) {
					c.sendMessage("You have succesfully earned this title, For killing 500 players");
					c.sendMessage(" from your account..");
					c.loyaltyTitle = "<col=CC0099>Commander";
				} else {
					c.sendMessage("Unfortunately this Title requires you to killl 500 players");
				}
				c.getPA().closeAllWindows();
				break;
			case 1610:
				if (KillcountList.getTotalKills(c) > 3200) {
					c.loyaltyTitle = "The Destroyer";
					c.sendMessage("Your title is now 'The destroyer'");
				} else {
					c.sendMessage("You need at least 3200 total boss kills for this title!");

				}
				c.getPA().closeAllWindows();
				break;
			case 1529:
				TeleportExecutor.teleport(c, new Position(3228, 3922, 0));
				c.sendMessage("Move directly north from this positon!");
				break;
			case 1523:
				if (c.votePoints > 7) {
					c.votePoints -= 7;
					RandomSelection2.addItem(c);
				} else {
					c.sendMessage("You do not have enough vote points");
				}
				c.getPA().closeAllWindows();
				break;
			case 1521:
				if (c.canClaim) {
					c.loyaltyTitle = "[Loyal]";
					c.canClaim = false;
					c.sendMessage("Thanks for your continued support! Relog for your title!");
				}
				break;
			case 1412:
				Slayer.handleInterface(c, "buy");
				break;
			case 1422:
				c.sendMessage("You currently have " + c.assaultPoints + " Assault Points");
				break;
			case 229:
				TeleportExecutor.teleport(c, new Position(2893, 3412, 0));
				c.sendMessage("Welcome to Taverly, head directly south to reach Taverly dungeon.");
				break;
			case 230:
				c.getDH().sendDialogues(1331, c.npcType);
				break;
			case 3:
				TeleportExecutor.teleport(c, new Position(3363, 3676, 0));
				break;
			case 20:
				if (c.getPet() != null)
					c.getPet().dispose();
				TeleportExecutor.teleport(c, new Position(2897, 5269, 0));
				c.killCount = 0;
				break;
			case 42:
				c.getPA().resetSkill(1, 0);
				break;
			case 103:
				TeleportExecutor.teleport(c, new Position(2736, 3471, 0));
				break;
			case 106:
				TeleportExecutor.teleport(c, new Position(3240, 3611, 0));
				break;
			case 110:
				c.getDH().sendDialogues(343, c.npcType);
				break;
			case 222:
				TeleportExecutor.teleport(c, new Position(1771, 5167, 0));
				break;
			case 223:
				c.getDH().sendDialogues(480, c.npcType);
				break;
			case 228:
				c.getDH().sendDialogues(694, c.npcType);
				break;
			case 232:
				if (c.playerLevel[22] == 99) {
					c.getDH().sendDialogues(727, 5113);
				} else {
					c.getDH().sendDialogues(731, 5113);
				}
				break;
			case 237:
				c.getDH().sendDialogues(343, c.npcType);
				break;
			case 246:
				c.getDH().sendDialogues(905, c.npcType);
				break;
			case 251:
				c.getDH().sendDialogues(1202, 494);
				break;
			}
			break;

		case 9181:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(4)) {
					break;
				}
			}
			switch (c.dialogueAction) {
			// cammy
			case 697:
				TeleportExecutor.teleport(c, new Position(CityTeleport.CAMELOT));
				break;
			case 1612:
				if (c.originalKillCount > 750) {
					c.sendMessage("You have succesfully earned this title for killing over 750 Pkers");
					c.loyaltyTitle = "<col=CC0099>War-Chief";
				} else {
					c.sendMessage("Unfortunately this Title requires you to have killed over 750 players");
				}
				c.getPA().closeAllWindows();
				break;
			case 1610:
				if (KillcountList.getTotalKills(c) > 5000) {
					c.sendMessage("Your title is now 'The final boss' ");
					c.loyaltyTitle = "Final Boss";

				} else {
					c.sendMessage("You need at least 5000 Total boss kills!");
				}
				c.getPA().closeAllWindows();
				break;
			case 1529:
				TeleportExecutor.teleport(c, new Position(3291, 3808, 0));
				c.sendMessage("Run directly north from this position");
				break;
			case 1523:
				if (c.votePoints > 4) {
					c.DoublePKP += 3000;
					c.votePoints -= 6;
					c.sendMessage("You have just purchased Double PKP.. Relog to activate");
				} else {
					c.sendMessage("You need at least 5 Voting points to purchase this!");
				}
				c.getPA().closeAllWindows();
				break;
			case 222:
				TeleportExecutor.teleport(c, new Position(2340, 3686, 0));
				break;
			case 1521:
				if (c.canClaim) {
					c.loyaltyTitle = "The Beast";
					c.canClaim = false;
					c.sendMessage("Thanks for your continued support! Relog for your title!");
				} else {
					c.sendMessage("You have nothing to claim");
					c.getPA().closeAllWindows();
				}
				break;
			case 1503:
				if (c.canClaim) {
					c.getDH().sendDialogues(1521, c.npcType);
				} else {
					c.sendMessage("You have nothing to claim");
					c.getPA().closeAllWindows();
				}

				break;
			case 1412:
				c.getDH().sendDialogues(1635, 1596);
				break;

			case 229:
				TeleportExecutor.teleport(c, new Position(2744, 3149, 0));
				break;
			case 230:
				c.getDH().sendDialogues(1329, c.npcType);
				break;
			case 251:
				c.removeBankMessage = true;
				c.getPA().removeAllWindows();
				c.sendMessage("If you wish to re-enable these messages, speak to the Banker at Falador.");
				break;
			case 3:
				TeleportExecutor.teleport(c, new Position(2540, 4716, 0));
				break;
			case 20:
				if (c.getPet() != null)
					c.getPet().dispose();
				TeleportExecutor.teleport(c, new Position(2928, 5323, 6));
				c.killCount = 0;
				break;
			case 42:
				c.getDH().sendDialogues(213, c.npcType);
				break;
			case 103:
				c.getDH().sendOption5("Barrows", "Pest Control", "TzHaar Cave", "Duel Arena", "More");
				c.teleAction = 2;
				break;
			case 111:
				c.getDH().sendDialogues(355, c.npcType);
				break;
			case 223:
				c.getDH().sendDialogues(483, c.npcType);
				break;
			case 232:
				c.getDH().sendDialogues(726, c.npcType);
				break;
			case 237:
				c.getDH().sendDialogues(795, 1552);
				break;
			case 228:
				c.getDH().sendDialogues(692, c.npcType);
				break;
			case 246:
				c.getDH().sendDialogues(726, c.npcType);
				break;
			}
			break;

		case 1093:
		case 1094:
		case 1097:
			c.onAuto = true;
			if (c.autocastId > 0) {
				c.getPA().resetAutoCast();
			} else {
				if (c.playerMagicBook == 1) {
					if (c.playerEquipment[Player.playerWeapon] == 4675
							|| c.playerEquipment[Player.playerWeapon] == 14604
							|| c.playerEquipment[Player.playerWeapon] == 15021
							|| c.playerEquipment[Player.playerWeapon] == 6914
							|| c.playerEquipment[Player.playerWeapon] == 13868
							|| c.playerEquipment[Player.playerWeapon] == 12345
							|| c.playerEquipment[Player.playerWeapon] == 12460) {
						c.setSidebarInterface(0, 1689);
					} else {
						c.sendMessage("You can't autocast ancients with this staff.");
					}
				} else if (c.playerMagicBook == 0) {
					if (c.playerEquipment[Player.playerWeapon] == 4170
							|| c.playerEquipment[Player.playerWeapon] == 12346) {
						c.setSidebarInterface(0, 12050);
						c.sendMessage("h");
					} else {
						c.setSidebarInterface(0, 1829);
					}
				}

			}
			break;

		case 9157:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(1)) {
					break;
				}
			}
			if (c.teleAction == 650) {
				TeleportExecutor.teleport(c, new Position(3087, 3501, 0));
			}
			if (c.dialogueAction == 458) {
				c.getItems().addItem(13329, 1);
				c.getItems().addItem(13330, 1);
				c.sendMessage("Congratulations! You've obtained the Max Cape!");
				c.getPA().closeAllWindows();
				return;
			}
			if (c.dialogueAction == 1528) {
				if (c.getItems().playerHasItem(995, 50000000)) {
					c.getItems().deleteItem2(995, 50000000);
					c.getItems().addItem(15812, 1);
				} else {
					c.sendMessage("You do not have enough money to purchase this.");
				}
				return;
			}
			if (c.dialogueAction == 2009) {
				Barrows.checkCoffins(c);
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 1798) {
				c.cookQP += 1;
				c.getPA().loadAllQuests(c);
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 1807 && c.getItems().playerHasItem(954, 2)) {
				c.getItems().deleteItem(954, 2);
				c.getPA().movePlayer1(2442, 9717);
				c.sendMessage("You give Koftik the 2 ropes and he walks further ahead.");
				c.sendMessage("Maybe you should go and meet up with him...");
				PlayerSave.saveGame(c);
				c.getPA().loadAllQuests(c);
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 1805) {
				c.undergroundQP += 1;
				c.getPA().removeAllWindows();
				c.getPA().loadAllQuests(c);
				return;
			}
			if (c.dialogueAction == 1810) {
				c.undergroundQP += 5;
				PlayerSave.saveGame(c);
				c.getPA().loadAllQuests(c);
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 1801) {
				c.getPA().showInterface(297);
				c.getItems().playerHasItem(1933, 1);
				c.getItems().playerHasItem(1927, 1);
				c.getItems().playerHasItem(1944, 1);
				c.getItems().deleteItem(1933, 1);
				c.getItems().deleteItem(1927, 1);
				c.getItems().deleteItem(1944, 1);
				c.getItems().addItem(995, 3000000);
				PlayerSave.saveGame(c);
				c.getPA().loadAllQuests(c);
				c.cookQP += 2;
				c.sendMessage("Congratulations! You've completed the Cook's Assistant quest!");
				return;
			}
			if (c.dialogueAction == 1811) {
				c.horrorQP += 1;
				c.getItems().addItem(2347, 1);
				c.getItems().addItem(960, 2);
				c.getItems().addItem(2353, 2);
				c.getPA().loadAllQuests(c);
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 1622) {
				c.getZulrah().start(c);
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 1663) {
				if (c.getItems().playerHasItem(995, 100000)) {
					c.getItems().deleteItem(995, 100000);
					c.getItems().addItem(8794, 1);
					c.getPA().removeAllWindows();
				} else if (!c.getItems().playerHasItem(995, 100000)) {
					c.sendMessage("You need 100,000 Coins to purchase a saw from the Operator.");
					c.getPA().removeAllWindows();
					return;
				}
			}
			if (c.dialogueAction == 1660) {
				if (c.getItems().playerHasItem(11716, 1) && c.getItems().playerHasItem(995, 2000000)) {
					c.getItems().deleteItem(11716, 1);
					c.getItems().deleteItem(995, 2000000);
					c.getItems().addItem(15597, 1);
					c.getPA().removeAllWindows();
				} else if (!c.getItems().playerHasItem(11716, 1) || !c.getItems().playerHasItem(995, 2000000)) {
					c.sendMessage("You need 2,000,000 Coins and a Zamorakian Spear for Otto to teach you.");
					c.getPA().removeAllWindows();
					return;
				}
			}
			if (c.dialogueAction == 1804) {
				if (c.getItems().playerHasItem(995, 1000000) && c.getItems().playerHasItem(4207, 1)) {
					c.getItems().deleteItem(995, 1000000);
					c.getItems().deleteItem(4207, 1);
					c.getItems().addItem(4212, 1);
					c.getPA().removeAllWindows();
				} else if (!c.getItems().playerHasItem(995, 1000000) || !c.getItems().playerHasItem(4207, 1)) {
					c.sendMessage("You need 1,000,000 Coins and a Crystal seed for a new Crystal bow.");
					c.getPA().removeAllWindows();
					return;
				}
			}
			if (c.dialogueAction == 1803) {
				if (c.getItems().playerHasItem(560, 1000) && c.getItems().playerHasItem(555, 3000)
						&& c.getItems().playerHasItem(563, 250) && c.getItems().playerHasItem(12345, 1)) {
					c.getItems().deleteItem(560, 1000);
					c.getItems().deleteItem(555, 3000);
					c.getItems().deleteItem(563, 250);
					c.getItems().deleteItem(12345, 1);
					c.getItems().addItem(12346, 1);
					c.getPA().removeAllWindows();
				} else if (!c.getItems().playerHasItem(12345, 1) || !c.getItems().playerHasItem(563, 250)
						|| !c.getItems().playerHasItem(555, 3000) || !c.getItems().playerHasItem(560, 1000)) {
					c.sendMessage("You need your empty Trident and the runes for Otto to charge it.");
					c.getPA().removeAllWindows();
					return;
				}
			}
			if (c.dialogueAction == 201) {
				c.getBank().deleteAll();
				c.getItems().deleteAllItems();
				c.getItems().deleteEquipment();
				IronManAccount.drawInterface(c);
				c.sendMessage("If your account had any items, they have all been deleted.");
				c.isFullHelm = Item.isFullHelm(c.playerEquipment[Player.playerHat]);
				c.isFullMask = Item.isFullMask(c.playerEquipment[Player.playerHat]);
				c.isFullBody = Item.isFullBody(c.playerEquipment[Player.playerChest]);
				return;
			}
			if (c.dialogueAction == 200) {
				if (c.getAccount().getType().equals(Account.IRON_MAN_TYPE)) {
					DialogueHandler.sendStatement(c, "You are already an Iron Man, there is no need for this.");
					c.nextChat = -1;
					return;
				}
				IronManAccount.drawInterface(c);
				return;
			}
			if (c.teleportAction.equalsIgnoreCase("faladorteleport")) {
				TeleportExecutor.teleport(c, new Position(2995, 3385, 0));
				break;
			}
			if (c.dialogueAction == 1413) {
				c.needsNewTask = true;
				Slayer.generateTask(c, null);
			}
			if (c.dialogueAction == 1500) {
				Mercenary.startGame(c);
			}
			if (c.dialogueAction == 49) {
				if (c.getItems().playerHasItem(6099) || c.getItems().playerHasItem(6100)
						|| c.getItems().playerHasItem(6101) || c.getItems().playerHasItem(6102)) {
					c.setTeleportStonePos(new int[] { c.absX, c.absY, c.heightLevel });
					c.getPA().removeAllWindows();
					c.sendMessage(
							"The crystal starts to glow and you feel cold winters breeze flow through your veins.");
					c.sendMessage("[NOTICE] After using the teleport once, you are required to set another tele spot!");
				} else {
					c.getPA().removeAllWindows();
					c.sendMessage("You need a teleport crystal to do this.");
				}
			}

			if (c.dialogueAction == 1501) {
				TeleportExecutor.teleport(c, new Position(2771, 9341, 0));
			}
			switch (c.dialogueAction) {
			case 1547:
				if (c.isMaxed() || c.playerRights == 5) {
					if (c.getItems().playerHasItem(995, 250000000)) {
						if (c.getItems().freeSlots() > 7) {
							c.getItems().deleteItem(995, 250000000);
							c.getItems().addItem(15378, 1);
							c.getItems().addItem(15377, 1);
							c.getItems().addItem(15376, 1);
							c.getItems().addItem(15375, 1);
							c.getItems().addItem(15374, 1);
							c.getItems().addItem(15373, 1);
						} else {
							c.sendMessage("You do not have enough inventory space..");
						}
					} else {
						c.sendMessage("You need to be maxed and have at least 50M in your inventory");
					}
					c.getPA().closeAllWindows();
				}
				break;
			case 1528:
				if (c.isMaxed()) {
					if (c.getItems().playerHasItem(995, 50000000)) {
						c.getItems().deleteItem(995, 50000000);
						c.getItems().addItem(15812, 1);
					} else {
						c.sendMessage("You need to be maxed and have at least 50M in your inventory");
					}
					c.getPA().closeAllWindows();
				}
				break;
			case 1621:
				TeleportExecutor.teleport(c, new Position(2641, 4565, 1));
				break;
			case 1620:
				if (c.getItems().playerHasItem(1038, 1) && c.getItems().playerHasItem(1040, 1)
						&& c.getItems().playerHasItem(1042, 1) && c.getItems().playerHasItem(1044, 1)
						&& c.getItems().playerHasItem(1046, 1) && c.getItems().playerHasItem(1048, 1)) {
					c.getItems().deleteItem(1038, 1);
					c.getItems().deleteItem(1040, 1);
					c.getItems().deleteItem(1042, 1);
					c.getItems().deleteItem(1044, 1);
					c.getItems().deleteItem(1046, 1);
					c.getItems().deleteItem(1048, 1);
					c.getItems().addItem(15598, 1);
					c.getPA().closeAllWindows();
					c.sendMessage("The mage quickly snatches the phats off you");
				}
				break;
			case 1619:
				c.getItems().deleteItem(995, 25000000);
				c.getItems().deleteItem(560, 3000);
				c.getItems().deleteItem(565, 2500);
				c.getItems().deleteItem(12345, 1);
				c.getItems().addItem(12346, 1);
				c.sendMessage("The mage swiftly accepts your deal and returns a fully powered trident");
				c.getPA().closeAllWindows();
				break;
			case 1618:
				c.getDH().sendDialogues(1648, 2262);
				break;
			case 1615:
				c.getShops().openShop(67);
				break;
			case 1614:
				Slayer.generateBossTask(c, null);
				break;
			case 1613:
				TeleportExecutor.teleport(c, new Position(3056, 3310, 0));
				break;
			case 1608:
				TeleportExecutor.teleport(c, new Position(3041, 4842, 0));
				break;
			case 1544:
				switch (c.gameMode) {
				case 0:
					break;
				case 1:
					if (c.prestigeLevel > 4) {
						c.getItems().sendItemToAnyTab(4151, 1);
						c.sendMessage("You can now unlock an abysaal whip via prestiging");
					} else
						c.sendMessage("As a Regular you need to prestige 5 times for this feature!");
					break;
				case 2:
				case 3:
					if (c.prestigeLevel > 2) {
						c.getItems().sendItemToAnyTab(4151, 1);
						c.sendMessage("You can now unlock an abyssal whip from prestige!");
					} else
						c.sendMessage("On Legend, you need to prestige 3 times for this feature!");
					break;
				}

				c.getPA().closeAllWindows();
				break;
			case 1530:
				c.getShops().openShop(1);
				break;
			case 1531:
				if (c.getItems().playerHasItem(5023, 1)) {
					c.getItems().deleteItem(5023, 1);
					c.getItems().addItem(995, 101000000);
					c.sendMessage("You exchange your ticket for 101M");

				} else {
					c.sendMessage("You do not have any tickets");
				}
				c.getPA().closeAllWindows();
				break;
			case 1526:
				c.securityEnabled = true;
				c.sendMessage("Additional security is now activated! consider yourself safe!");
				c.getPA().closeAllWindows();
				break;
			case 1525:
				if (c.getItems().playerHasItem(995, 20000000) && c.getItems().freeSlots() > 2) {
					c.getItems().deleteItem(995, 20000000);
					c.hasBoughtCannon = false;
					c.getItems().addItem(6829, 1);
					c.getItems().addItem(2, 1000);
					c.sendMessage(
							"You have received your cannon-status box, you can use this to re-claim your cannon-status.");
				} else {
					c.sendMessage("You need at least 20m & two inventory spaces to remove  your status.");
				}
				c.getPA().closeAllWindows();
				break;
			case 13:
				TeleportExecutor.teleport(c, new Position(2278, 3122, 0));
				break;
			case 2525:
				if (c.inTrade) {
					c.getTradeAndDuel().declineTrade(true);
					return;
				}
				ItemOnDeath.activateItemsOnDeath(c);
				break;
			case 10:
				if (!c.warnedPlayer) {
					c.warnedPlayer = true;
					return;
				}
				TeleportExecutor.teleport(c, new Position(3212, 3436, 0));
				break;
			case 1506:
				TeleportExecutor.teleport(c, new Position(3049, 9770, 0));
				break;
			case 1505:
				c.sendMessage("Sorry you can no longer remove your status");
				break;
			case 1504:
				c.sendMessage("Sorry you can no longer remove your status.");
				break;
			case 501:
				c.setSocialSlayer(c, c.socialSlayerInviteClient);
				c.getSocialSlayer().getPartner().setSocialSlayer(c.socialSlayerInviteClient, c);
				c.setLastSocialPlayer(c.getSocialSlayer().getPartner().playerName);
				c.getSocialSlayer().getPartner().setLastSocialPlayer(c.playerName);
				c.getSocialSlayer().getPartner().setSocialSlayerDisconnections(0);
				c.setSocialSlayerDisconnections(0);
				c.getSocialSlayer().getPartner().sendMessage(
						c.playerName + " has accepted your request, you are now social slaying with him/her.");
				c.getSocialSlayer().getClient().sendMessage(
						"You are now social slaying with " + c.getSocialSlayer().getPartner().playerName + ".");
				c.getSocialSlayer().createSocialTask();
				c.getPA().removeAllWindows();
				break;
			case 1422:
				c.getDH().sendDialogues(1418, c.npcType);
				break;
			case 1599:
				if (c.getItems().playerHasItem(6830)) {
					c.getItems().deleteItem(6830, 1);
					c.isDonator = true;
					c.donatorRights = 1;
					c.playerRights = 6;
					c.disconnected = true;
				} else {
					c.sendMessage("Seems you do not have a donator box!");

				}
				break;
			case 1600:
				if (c.getItems().playerHasItem(6831)) {
					c.getItems().deleteItem(6831, 1);
					c.isDonator = true;
					c.donatorRights = 2;
					c.playerRights = 7;
					c.disconnected = true;
				} else {
					c.sendMessage("Seems you do not have an Extreme box!");

				}

				break;
			case 1407:
				if (c.playerLevel[4] >= 40) {
					TeleportExecutor.teleport(c, new Position(2668, 3421, 0));
				} else {
					c.sendMessage("You need a Ranged level of at least 40 to enter the Guild.");
					c.getPA().removeAllWindows();
				}
				break;
			case 1364:
				c.getDH().sendDialogues(1365, c.npcType);
				break;
			case 1355:
				if (c.heightLevel == 1) {
					LadderHandler.climbLadder(c, c.absX, c.absY, 1, 2);
					c.getPA().closeAllWindows();
				}
				break;
			case 1356:
				if (c.heightLevel == 1) {
					LadderHandler.climbStairs(c, c.absX, c.absY, 1, 0);
					c.getPA().closeAllWindows();
				}
				break;
			case 85:
				c.getShops().openShop(18);
				c.sendMessage("My Pest Control Points: " + c.pcPoints + ".");
				break;
			case 1351:
				if (c.skillPoints >= 5) {
					c.getItems().addItem(299, 100);
					c.skillPoints -= 5;
					c.getDH().sendDialogues(1353, c.npcType);
				} else {
					c.sendMessage("You don't have enough skill points!"); // mithrilseeds
					c.getPA().closeAllWindows();
				}
				break;
			case 261:
				c.getDH().sendDialogues(1264, 2566);
				break;
			/*
			 * case 262: c.getShops().openSkillCape(); break;
			 */
			case 262:
				c.getShops().openSkillCape();
				break;
			case 260:
				if (!c.inWild()) {
					TeleportExecutor.teleport(c, new Position(3215, 3461, 0));
				}
				break;
			case 258:
				c.getDH().sendDialogues(1226, 648);
				break;
			case 252:
				c.getDH().sendDialogues(1205, 494);
				break;
			case 1:
				int r = 4;
				switch (r) {
				case 0:
					c.getPA().movePlayer(3534, 9677, 0);
					break;

				case 1:
					c.getPA().movePlayer(3534, 9712, 0);
					break;

				case 2:
					c.getPA().movePlayer(3568, 9712, 0);
					break;

				case 3:
					c.getPA().movePlayer(3568, 9677, 0);
					break;
				case 4:
					c.getPA().movePlayer(3551, 9694, 0);
					break;
				}
				break;
			case 2:
				c.getPA().movePlayer(2507, 4717, 0);
				break;
			case 8:
				c.getPA().fixAllBarrows();
				c.getPA().removeAllWindows();
				break;
			case 100:
				c.getDH().sendDialogues(293, 2244);
				break;
			case 101:
				c.getDH().sendDialogues(303, 582);
				break;
			case 102:
				TeleportExecutor.teleport(c, new Position(3284, 3368, 0));
				break;
			case 104:
				TeleportExecutor.teleport(c, new Position(3429, 3538, 0));
				break;
			case 105:
				TeleportExecutor.teleport(c, new Position(2420, 4446, 0));
				break;
			case 107:
				c.getDH().sendDialogues(317, 5912);
				break;
			case 113:
				c.getDH().sendDialogues(335, c.npcType);
				break;
			case 116:

				break;
			case 117:
				c.getDH().sendDialogues(372, c.npcType);
				break;
			case 118:
				c.getDH().sendDialogues(385, c.npcType);
				break;
			case 119:
				c.getDH().sendDialogues(394, c.npcType);
				break;
			case 120:
				c.getDH().sendDialogues(395, c.npcType);
				break;
			case 121:
				c.getDH().sendDialogues(429, c.npcType);
				break;
			case 224:
				c.getDH().sendDialogues(497, c.npcType);
				break;
			case 225:
				c.getDH().sendDialogues(632, c.npcType);
				break;
			case 226:
				c.getDH().sendDialogues(673, c.npcType);
				break;
			case 233:
				c.getDH().sendDialogues(739, c.npcType);
				break;
			case 236:
				if (!c.inWild()) {
					TeleportExecutor.teleport(c, new Position(2957, 3275, 0));
				}
				break;
			case 238:
				c.getDH().sendDialogues(821, 1552);
				break;
			case 241:
				c.getDH().sendDialogues(858, c.npcType);
				break;
			case 242:
				c.getDH().sendDialogues(866, c.npcType);
				break;
			case 243:
				c.getDH().sendDialogues(880, c.npcType);
				break;
			case 244:
				TeleportExecutor.teleport(c, new Position(3333, 3346, 0));
				break;
			case 247:
				c.getDH().sendDialogues(966, c.npcType);
				break;
			case 248:
				TeleportExecutor.teleport(c, new Position(2643, 10026, 0));
				break;
			case 249:
				if (!c.inWild()) {
					TeleportExecutor.teleport(c, new Position(3078, 3421, 0));
				}
				break;
			case 250:
				TeleportExecutor.teleport(c, new Position(3055, 9586, 0));
				break;
			case 245:
				if (c.getItems().playerHasItem(995, 265)) {
					c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 265);
					c.getDH().sendDialogues(897, c.npcType);
				} else {
					c.getDH().sendDialogues(899, c.npcType);
				}
				break;
			case 227:
				TeleportExecutor.teleport(c, new Position(3046, 4969, 1));
				c.sendMessage("Welcome to Rogue's Den.");
				break;
			case 1665:
				TeleportExecutor.teleport(c, new Position(2806, 3463, 0));
				c.sendMessage("The Ghost farmer teleports you to the farming patches.");
				break;
			case 1664:
				TeleportExecutor.teleport(c, new Position(2956, 2997, 0));
				c.sendMessage("The Hunting Expert Teleports you to the Hunting Grounds.");
				break;
			case 1337:
				TeleportExecutor.teleport(c, new Position(2474, 3437, 0));
				break;
			}
			break;

		case 9158:
			if (c.dialogue().isActive()) {
				if (c.dialogue().select(2)) {
					break;
				}
			}
			if (c.teleAction == 650) {
				TeleportExecutor.teleport(c, new Position(3087, 3514, 0));
			}
			if (c.dialogueAction == 458) {
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 1798) {
				c.getPA().removeAllWindows();
				return;
			}
			if (c.dialogueAction == 200) {
				c.getDH().sendNpcChat2("If you ever change your mind, come back.",
						"There is always a spot for new recruits.", 4200, "Iron Man Instructor");
				c.nextChat = -1;
				return;
			}
			if (c.dialogueAction == 1660) {
				c.getPA().removeAllWindows();
				if (c.getItems().playerHasItem(12345) && c.getItems().playerHasItem(995, 5000000)
						&& c.getItems().playerHasItem(555, 7000) && c.getItems().playerHasItem(556, 5000)
						&& c.getItems().playerHasItem(565, 1000)) {

					c.getItems().deleteItem2(995, 5000000);
					c.getItems().deleteItem2(555, 7000);
					c.getItems().deleteItem2(556, 5000);
					c.getItems().deleteItem2(565, 1000);
					c.getItems().deleteItem(12345, 1);
					c.getItems().addItem(12346, 1);

				} else {
					c.sendMessage("Cannot recharge. Requires: 1 trident, 5M coins, 3K Water wave spell runes.");
				}
				return;
			}
			if (c.dialogueAction == 1413) {
				c.getDH().sendDialogues(1412, 1596);
			}
			if (c.dialogueAction == 1500 || c.dialogueAction == 201) {
				c.getPA().closeAllWindows();
			}
			if (c.teleportAction.equalsIgnoreCase("faladorteleport")) {
				TeleportExecutor.teleport(c, new Position(2966, 3386, 0));
				break;
			}
			switch (c.dialogueAction) {
			case 8:
				Barrows.resetBarrows(c);
				c.sendMessage("You have succesfully reset your Barrows KC.");
				c.getPA().closeAllWindows();
				break;
			case 13:
				TeleportExecutor.teleport(c, new Position(2400, 4681, 0));
				break;
			case 1609:
				if (KillcountList.getTotalKills(c) > 1299) {
					c.getDH().sendDialogues(1622, 5210);

				} else {
					c.sendMessage("You need at least 1300 total boss kills to check our store");
					c.getPA().closeAllWindows();
				}
				break;
			case 1608:
				TeleportExecutor.teleport(c, new Position(2891, 4810, 0));
				break;
			case 1544:
				switch (c.gameMode) {
				case 0:
					break;
				case 1:
					if (c.prestigeLevel > 3) {
						c.getPet().setOwnedPet(15570, true);
						c.getItems().sendItemToAnyTab(15570, 1);
						c.sendMessage("You have now Unlocked Pet Verac!");

					} else
						c.sendMessage("As a lord you need to prestige 5 times for this feature!");
					break;
				case 2:
				case 3:
					if (c.prestigeLevel > 0) {
						c.getPet().setOwnedPet(15570, true);
						c.getItems().sendItemToAnyTab(15570, 1);
						c.sendMessage("You have unlocked a Verac pet!");
					} else
						c.sendMessage("On Legendary, you need to prestige 1 time for this feature!");
					break;
				}
				c.getPA().closeAllWindows();
				break;
			case 1531:
				if (c.getItems().playerHasItem(995, 101000000)) {
					c.getItems().addItem(5023, 1);
					c.getItems().deleteItem(995, 101000000);
					c.sendMessage("You exchange 101M for a 100M Ticket");
				} else {
					c.sendMessage("You need at least 101M to purchase a ticket");
				}
				c.getPA().closeAllWindows();
				break;
			case 1530:
				c.getDH().sendDialogues(1541, 522);
				break;
			case 1526:
				c.securityEnabled = false;
				c.sendMessage("You have de-activated your additional security! be careful!");
				c.getPA().closeAllWindows();
				break;
			case 1525:
				c.getPA().closeAllWindows();
				break;
			case 262:
				c.getPA().closeAllWindows();
				if (c.canWearCape) {
					if (c.getItems().playerHasItem(995, 100000)) {
						c.getItems().deleteItem(995, 100000);
						c.getItems().addItemToBank(9813, 1);
						c.getItems().addItemToBank(9814, 1);
					} else {
						c.sendMessage("You need at least 100k to purchase this cape");
					}
				} else {
					c.sendMessage("You need to complete all achievements before purchasing this cape");
				}
				break;
			case 2525:
				if (c.inTrade) {
					c.getTradeAndDuel().declineTrade(true);
					return;
				}
				c.getPA().showInterface(15106);
				break;
			case 10:
				if (!c.warnedPlayer) {
					c.warnedPlayer = true;
					return;
				}
				TeleportExecutor.teleport(c, new Position(2964, 3378, 0));
				break;
			case 1522:
				c.getPA().sendFrame126("www.vd-rsps.com/vote", 12000);
				c.sendMessage("If you are not directed to our site, http://vd-rsps.com/vote");
				break;
			case 1506:
				c.getShops().openShop(70);
				c.getPA().closeAllWindows();
				break;
			case 1505:
				c.getPA().closeAllWindows();
				break;
			case 1504:
				c.getPA().closeAllWindows();
				break;
			case 501:
				c.getPA().removeAllWindows();
				c.socialSlayerInviteClient = null;
				break;
			case 1407:
			case 1357:
			case 1364:
				c.getPA().removeAllWindows();
				break;
			case 1355:
				if (c.heightLevel == 1) {
					LadderHandler.climbLadder(c, c.absX, c.absY, 1, 0);
					c.getPA().closeAllWindows();
				}
				break;
			case 1356:
				if (c.heightLevel == 1) {
					LadderHandler.climbStairs(c, c.absX, c.absY, 1, 0);
					c.getPA().closeAllWindows();

				}
				break;
			case 1351:
				c.getDH().sendDialogues(1352, c.npcType); // mithrilseeds
				break;
			case 261:
				c.getPA().removeAllWindows();
				break;
			case 260:
				c.getPA().removeAllWindows();
				break;
			case 258:
				c.getDH().sendDialogues(1232, 648);
				break;
			case 252:
				c.getDH().sendDialogues(1207, 494);
				break;
			case 100:
				c.getDH().sendDialogues(292, 2244);
				break;
			case 101:
				c.getDH().sendDialogues(304, 582);
				break;
			case 107:
				c.getPA().openUpBank();
				break;
			case 104:
				TeleportExecutor.teleport(c, new Position(2794, 9996, 0));
				break;
			case 108:
				c.getDH().sendDialogues(322, 5912);
				break;
			case 117:
				c.getDH().sendDialogues(373, c.npcType);
				break;
			case 118:
				c.getDH().sendDialogues(385, c.npcType);
				break;
			case 119:
				c.getDH().sendDialogues(393, c.npcType);
				break;
			case 121:
				c.getDH().sendDialogues(428, c.npcType);
				break;
			case 224:
				c.getDH().sendDialogues(498, c.npcType);
				break;
			case 225:
				c.getDH().sendDialogues(633, c.npcType);
				break;
			case 226:
				c.getDH().sendDialogues(675, c.npcType);
				break;
			case 238:
				c.getDH().sendDialogues(820, 1552);
				break;
			case 233:
				c.getDH().sendDialogues(759, c.npcType);
				break;
			case 241:
				c.getDH().sendDialogues(857, c.npcType);
				break;
			case 242:
				c.getDH().sendDialogues(868, c.npcType);
				break;
			case 243:
				c.getDH().sendDialogues(881, c.npcType);
				break;
			case 244:
				c.getDH().sendDialogues(885, c.npcType);
				break;
			case 245:
				c.getDH().sendDialogues(896, c.npcType);
				break;
			case 247:
				c.getDH().sendDialogues(967, c.npcType);
				break;
			case 248:
				c.getDH().sendDialogues(967, c.npcType);
				break;
			case 249:
				c.getPA().removeAllWindows();
				c.sendMessage("You have chosen not to start this quest.");
				break;
			case 250:
				c.getDH().sendDialogues(985, c.npcType);
				break;
			case 1337:
				if (c.playerLevel[16] >= 60) {
					TeleportExecutor.teleport(c, new Position(3003, 3934, 0));
				} else {
					c.getDH().sendDialogues(359, 162);
					c.sendMessage("You need an Agility level of [@red@60@bla@] to use this course");
				}
				break;
			}
			break;

		/** Specials **/

		case 29199:
			c.specBarId = 7636; // the special attack text - sendframe126(S P E
								// C I A L A T T A C K, c.specBarId);
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 30108:

			c.specBarId = 7812;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();

			break;

		case 29163:
			c.specBarId = 7611;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 33033:
			c.specBarId = 8505;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29049:
			if (c.playerEquipment[Player.playerWeapon] == 4153) {
				c.specBarId = 7486;
				c.getCombat().handleGmaul();
			} else {
				c.specBarId = 7486;
				c.usingSpecial = !c.usingSpecial;
				c.getItems().updateSpecialBar();
			}
			break;

		case 29063:
			if (c.duelRule[10] && c.duelStatus == 5) {
				c.sendMessage("Special attacks have been disabled during this duel!");
				return;
			}
			if (c.getCombat().checkSpecAmount(c.playerEquipment[Player.playerWeapon])) {
				c.gfx0(246);
				c.forcedChat("Raarrrrrgggggghhhhhhh!");
				c.startAnimation(1056);
				c.playerLevel[2] = Player.getLevelForXP(c.playerXP[2])
						+ (Player.getLevelForXP(c.playerXP[2]) * 15 / 100);
				c.getPA().refreshSkill(2);
				c.getItems().updateSpecialBar();
			} else {
				c.sendMessage("You don't have the required special energy to use this attack.");
			}
			break;

		case 48023:
			c.specBarId = 12335;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		case 48034:
		case 29138:
			c.specBarId = 7586;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29124:
			c.specBarId = 7561;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29238:
			c.specBarId = 7686;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		/** Dueling **/
		case 26065: // no forfeit
		case 26040:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(0);
			break;

		case 26066: // no movement
		case 26048:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(1);
			break;

		case 26069: // no range
		case 26042:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(2);
			break;

		case 26070: // no melee
		case 26043:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(3);
			break;

		case 26071: // no mage
		case 26041:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(4);
			break;

		case 26072: // no drinks
		case 26045:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(5);
			break;

		case 26073: // no food
		case 26046:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(6);
			break;

		case 26074: // no prayer
		case 26047:
			c.getCombat().resetPrayers();
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(7);
			break;

		case 26076: // obsticals
		case 26075:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(8);
			break;

		case 2158: // fun weapons
		case 2157:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(9);
			break;

		case 30136: // sp attack
		case 30137:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(10);
			break;

		case 53245: // no helm
			c.duelSlot = 0;
			c.getTradeAndDuel().selectRule(11);
			break;

		case 53246: // no cape
			c.duelSlot = 1;
			c.getTradeAndDuel().selectRule(12);
			break;

		case 53247: // no ammy
			c.duelSlot = 2;
			c.getTradeAndDuel().selectRule(13);
			break;

		case 53249: // no weapon.
			c.duelSlot = 3;
			c.getTradeAndDuel().selectRule(14);
			break;

		case 53250: // no body
			c.duelSlot = 4;
			c.getTradeAndDuel().selectRule(15);
			break;

		case 53251: // no shield
			c.duelSlot = 5;
			c.getTradeAndDuel().selectRule(16);
			break;

		case 53252: // no legs
			c.duelSlot = 7;
			c.getTradeAndDuel().selectRule(17);
			break;

		case 53255: // no gloves
			c.duelSlot = 9;
			c.getTradeAndDuel().selectRule(18);
			break;

		case 53254: // no boots
			c.duelSlot = 10;
			c.getTradeAndDuel().selectRule(19);
			break;

		case 53253: // no rings
			c.duelSlot = 12;
			c.getTradeAndDuel().selectRule(20);
			break;

		case 53248: // no arrows
			c.duelSlot = 13;
			c.getTradeAndDuel().selectRule(21);
			break;

		case 26018:
			Player o = World.PLAYERS.get(c.duelingWith);
			if (o == null) {
				c.getTradeAndDuel().declineDuel();
				return;
			}
			if (c.duelStatus == 5) {
				return;
			}
			if (c.duelRule[0] && c.duelRule[1]) {
				c.sendMessage("Either turn off No Forfeit or No Movement!");
				break;
			}

			if (c.duelRule[2] && c.duelRule[3] && c.duelRule[4]) {
				c.sendMessage("You won't be able to attack the player with the rules you have set.");
				break;
			}
			c.duelStatus = 2;
			if (c.duelStatus == 2) {
				c.getPA().sendFrame126("Waiting for other player...", 6684);
				o.getPA().sendFrame126("Other player has accepted.", 6684);
			}
			if (o.duelStatus == 2) {
				o.getPA().sendFrame126("Waiting for other player...", 6684);
				c.getPA().sendFrame126("Other player has accepted.", 6684);
			}

			if (c.duelStatus == 2 && o.duelStatus == 2) {
				c.canOffer = false;
				o.canOffer = false;
				c.duelStatus = 3;
				o.duelStatus = 3;
				c.getTradeAndDuel().confirmDuel();
				o.getTradeAndDuel().confirmDuel();
			}
			break;

		case 25120:
			if (c.duelStatus == 5) {
				break;
			}
			final Player o1 = World.PLAYERS.get(c.duelingWith);
			if (o1 == null) {
				c.getTradeAndDuel().declineDuel();
				return;
			}
			c.duelStatus = 4;
			c.poisonDamage = 0;
			Server.getTaskScheduler().schedule(new ScheduledTask(1) {
				@Override
				public void execute() {
					if (System.currentTimeMillis() - c.duelDelay > 800 && c.duelCount > 0) {
						if (c.duelCount != 1) {
							c.forcedChat("" + (--c.duelCount));
							c.duelDelay = System.currentTimeMillis();
						} else {
							c.resetDamageReceived();
							c.forcedChat("FIGHT!");
							c.duelCount = 0;
							o1.duelCount = 0;
							o1.forcedChat("FIGHT");
						}
					}
					if (c.duelCount == 0) {
						stop();
					}
				}
			}.attach(c));
			if (o1.duelStatus == 4 && c.duelStatus == 4) {
				c.getTradeAndDuel().startDuel();
				o1.getTradeAndDuel().startDuel();
				o1.duelCount = 4;
				c.duelCount = 4;
				c.duelDelay = currentTimeMillis();
				o1.duelDelay = currentTimeMillis();
			} else {
				c.getPA().sendFrame126("Waiting for other player...", 6571);
				o1.getPA().sendFrame126("Other player has accepted", 6571);
			}
			break;

		case 4169: // god spell charge
			c.usingMagic = true;
			if (!c.getCombat().checkMagicReqs(48)) {
				break;
			}

			if (currentTimeMillis() - c.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
				c.sendMessage("You still feel the charge in your body!");
				break;
			}
			c.godSpellDelay = currentTimeMillis();
			c.sendMessage("You feel charged with a magical power!");
			c.gfx100(Player.MAGIC_SPELLS[48][3]);
			c.startAnimation(Player.MAGIC_SPELLS[48][2]);
			c.usingMagic = false;
			break;

		case 153:
		case 152:
		case 74214:
			c.isRunning2 = !c.isRunning2;
			c.isRunning = !c.isRunning;
			int frame = c.isRunning2 ? 1 : 0;
			c.getPA().sendFrame36(173, frame);
			break;

		case 9154:
			c.logout();
			break;
		case 86086:
		case 94138:
		case 86186:
			if (c.interfaceId != 27100) {
				Slayer.handleInterface(c, "buy");
			}
			break;
		case 105223:
		case 94139:
		case 94039:
			if (c.interfaceId != 22200) {
				Slayer.handleInterface(c, "learn");
			}
			break;
		case 86194:
		case 86094:
			c.getPA().closeAllWindows();
			break;
		case 86088:
		case 94040:
		case 86188:
			if (c.interfaceId != 22300) {
				Slayer.handleInterface(c, "assignment");
			}
			break;

		case 94155:
			c.removedTasks[0] = -1;
			Slayer.updateCurrentlyRemoved(c);
			break;
		case 94156:
			c.removedTasks[1] = -1;
			Slayer.updateCurrentlyRemoved(c);
			break;

		case 94157:
			c.removedTasks[2] = -1;
			Slayer.updateCurrentlyRemoved(c);
			break;

		case 94158:
			c.removedTasks[3] = -1;
			Slayer.updateCurrentlyRemoved(c);
			break;
		case 94147:
			Slayer.cancelTask(c);
			break;
		case 94149:
			Slayer.removeTask(c);
			break;
		case 86195:
			Slayer.buySlayerHelm(c);
			break;
		case 105231:
			Slayer.buySlayerExperience(c);
			break;
		case 105237:
			Slayer.buyRing(c);
			break;
		case 105233:
			Slayer.buySlayerDart(c);
			break;
		case 105235:
			Slayer.buyCape(c);
			break;
		case 105230:
			c.getPA().closeAllWindows();
			break;
		case 226154:
			c.takeAsNote = !c.takeAsNote;// rerun that
			break;
		case 53152:
			Cooking.getAmount(c, 1);
			break;
		case 53151:
			Cooking.getAmount(c, 5);
			break;
		case 53150:
			Cooking.getAmount(c, 10);
			break;
		case 53149:
			Cooking.getAmount(c, 28);
			break;

		// lunar home teleport

		case 30000:
			TeleportExecutor.teleport(c, new Position(3087, 3503, 0));
			c.teleAction = 9;
			break;

		case 75010:
		case 84237:
		case 117048:
			TeleportExecutor.teleport(c, new Position(3087, 3503, 0));
			/*
			 * c.getDH().sendOption2("Home Teleport", "Edgeville Pking");
			 * c.teleAction = 650;
			 */
			break;
		case 50253:
		case 4146:
		case 117112:
			// c.getDH().sendOption5("Stronghold Security", "Training zones",
			// "Ape Atoll (Dangerous)", "Crash Island",
			// "More monsters + slayer");
			// c.teleAction = 1;
			// c.dialogue().start("MONSTER_TELEPORTS", (Object) null);
			c.getPA().showInterface(16500);
			break;
		/**
		 * monster teleport
		 */
		case 64130:
			c.getPA().closeAllWindows();
			break;
		case 64204:
			TeleportExecutor.teleport(c, new Position(2911, 9728, 0));

			break;
		case 64178:
			TeleportExecutor.teleport(c, new Position(1862, 5221, 0));

			break;
		case 64136:
			TeleportExecutor.teleport(c, new Position(3439, 3567, 2));
			break;
		case 64139:
			TeleportExecutor.teleport(c, new Position(2857, 9836, 0));
			break;
		case 64142:
			TeleportExecutor.teleport(c, new Position(3425, 3568, 2));
			break;
		case 64145:
			TeleportExecutor.teleport(c, new Position(3421, 3566, 2));
			break;
		case 64148:
			TeleportExecutor.teleport(c, new Position(1861, 5191, 0));
			break;
		case 64151:
			TeleportExecutor.teleport(c, new Position(2632, 9506, 2));
			break;
		case 64154:
			TeleportExecutor.teleport(c, new Position(3241, 9915, 0));
			break;
		case 64157:
			TeleportExecutor.teleport(c, new Position(3125, 9909, 0));
			break;
		case 64160:
			TeleportExecutor.teleport(c, new Position(2898, 2731, 0));
			break;
		case 64163:
			TeleportExecutor.teleport(c, new Position(2705, 10030, 0));
			break;
		case 64166:
			TeleportExecutor.teleport(c, new Position(2702, 9992, 0));
			break;
		case 64169:
			TeleportExecutor.teleport(c, new Position(2647, 9487, 0));
			break;
		case 64172:
			TeleportExecutor.teleport(c, new Position(2701, 9490, 0));
			break;
		case 64175:
			TeleportExecutor.teleport(c, new Position(2715, 9439, 0));
			break;
		case 64186:
			TeleportExecutor.teleport(c, new Position(2865, 9925, 0));
			break;

		case 64189:
			TeleportExecutor.teleport(c, new Position(2325, 3799, 0));
			break;
		case 64192:
			TeleportExecutor.teleport(c, new Position(3134, 9910, 0));
			break;
		case 64195:
			TeleportExecutor.teleport(c, new Position(3124, 9974, 0));
			break;

		case 64198:
			TeleportExecutor.teleport(c, new Position(2805, 2787, 0));
			break;
		case 64201:
			TeleportExecutor.teleport(c, new Position(2320, 3791, 0));
			break;

		case 64216:
			TeleportExecutor.teleport(c, new Position(2658, 4575, 1));
			break;
		case 64219:
			TeleportExecutor.teleport(c, new Position(2393, 4684, 0));
			break;
		case 64222:
			TeleportExecutor.teleport(c, new Position(3050, 9589, 0));
			break;
		case 64225:
			TeleportExecutor.teleport(c, new Position(2898, 2731, 0));
			break;
		case 64228:
			TeleportExecutor.teleport(c, new Position(2696, 3718, 0));
			break;
		case 64231:
			TeleportExecutor.teleport(c, new Position(3125, 9909, 0));
			break;

		/**
		 * end
		 */
		case 50245:
		case 4143:
			c.getPA().closeAllWindows();
			c.sendMessage("Please talk to Sailor at home to teleports to Any citys");
			break;
		case 51013:
		case 6004:

		case 117210:
			// c.dialogue().start("SKILLING_DIALOGUE", (Object) null);
			c.getPA().showInterface(15900);

			break;
		/**
		 * Skilling teleports
		 */
		case 62032:
			TeleportExecutor.teleport(c, new Position(2818, 3462, 0));
			break;
		case 62036:
			TeleportExecutor.teleport(c, new Position(2956, 2997, 0));
			break;
		case 62040:
			TeleportExecutor.teleport(c, new Position(2924, 3173, 0));
			break;
		case 62044:
			TeleportExecutor.teleport(c, new Position(2849, 3431, 0));
			break;
		/**
		 * end
		 */
		case 50235:

		case 4140:
		case 118018:
			// c.dialogue().start("PVP_TELEPORTS", (Object) null);
			c.getPA().showInterface(16700);
			break;
		/**
		 * pk teleports
		 */
		case 65080:
			TeleportExecutor.teleport(c, new Position(3244, 3515, 0));
			break;
		case 65083:
			TeleportExecutor.teleport(c, new Position(2539, 4716, 0));
			break;
		case 65086:
			TeleportExecutor.teleport(c, new Position(3240, 3611, 0));
			break;
		case 65089:
			TeleportExecutor.teleport(c, new Position(3141, 3690, 0));
			break;
		case 65092:
			TeleportExecutor.teleport(c, new Position(3005, 3628, 0));
			break;
		case 6005:
		case 51023:
		case 117186:
			// c.dialogue().start("MINIGAME_DIALOGUE", (Object) null);
			c.getPA().showInterface(16900);
			break;

		/**
		 * other teleports
		 */
		case 66008:
			TeleportExecutor.teleport(c, new Position(3565, 3313, 0));
			break;
		case 66012:
			TeleportExecutor.teleport(c, new Position(3366, 3266, 0));
			break;
		case 66016:
			TeleportExecutor.teleport(c, new Position(2662, 2650, 0));
			break;
		case 66024:
			TeleportExecutor.teleport(c, new Position(2444, 5170, 0));
			break;
		case 66020:
			TeleportExecutor.teleport(c, new Position(2605, 3153, 0));
			break;
		case 4150:
		case 51005:
		case 51039:
		case 117154:
			// c.dialogue().start("BOSS_TELEPORT_DIALOGUE", (Object) null);
			// c.teleAction = 7;
			c.getPA().showInterface(16700);
			break;
		/**
		 * boss teleport new interface
		 */
		case 65130:// Barrelchest
			TeleportExecutor.teleport(c, new Position(2983, 9516, 1));
			break;
		case 65133:// Godwars Dungeon
			TeleportExecutor.teleport(c, new Position(2871, 5317, 2));
			break;
		case 65136:// Kalphite Queen
			TeleportExecutor.teleport(c, new Position(3480, 9484, 0));
			break;
		case 65139: // King Black Dragon
			TeleportExecutor.teleport(c, new Position(3007, 3849, 0));
			break;
		case 65142: // Chaos Elemental
			TeleportExecutor.teleport(c, new Position(3295, 3921, 0));
			break;
		case 65145:// Dagannoth King
			TeleportExecutor.teleport(c, new Position(1891, 4409, 0));
			break;
		case 65148:// Giant Mole
			TeleportExecutor.teleport(c, new Position(1771, 5167, 0));
			break;
		case 65151:// zulrah
			if (Misc.random(1) == 0) {
				TeleportExecutor.teleport(c, new Position(2278, 3122, 0));
			} else {
				TeleportExecutor.teleport(c, new Position(2278, 3122, 0));
			}
			break;
		case 65160:// Kraken
			TeleportExecutor.teleport(c, new Position(2340, 3686, 0));
			break;
		case 65163:// vent
			TeleportExecutor.teleport(c, new Position(3213, 3796, 0));
			break;
		case 65166:// scrop
			TeleportExecutor.teleport(c, new Position(3232, 3941, 0));
			break;
		case 65169:// v
			TeleportExecutor.teleport(c, new Position(3369, 3742, 0));
			break;
		case 65172:// Glod
			TeleportExecutor.teleport(c, new Position(2787, 9337, 0));
			break;
		case 65175:// Calisto
			TeleportExecutor.teleport(c, new Position(3294, 3839, 0));
			break;
		case 65178:// Ice Queen
			TeleportExecutor.teleport(c, new Position(3036, 9542, 0));
			break;
		case 65181:// Closed windos
			c.getPA().closeAllWindows();
			break;
		case 72038:
		case 51031:
			c.getDH().sendOption3("Teleport to Training zone", "Teleport to Ice Queen", "Teleport to Cave-Krakens.");
			c.dialogueAction = 1621;
			break;
		case 13094:
			c.getTradeAndDuel().declineTrade(true);
			break;
		case 13220:
			Player op = World.PLAYERS.get(c.tradeWith);
			if (c.inTrade) {
				if (!op.acceptedTrade && !c.acceptedTrade) {
					c.getTradeAndDuel().declineTrade(true);
				}
			}
			break;
		case 13092:
			Player ot = World.PLAYERS.get(c.tradeWith);
			if (ot == null) {
				c.getTradeAndDuel().declineTrade(false);
				break;
			}
			c.getPA().sendFrame126("Waiting for other player...", 3431);
			ot.getPA().sendFrame126("Other player has accepted", 3431);
			c.goodTrade = true;
			ot.goodTrade = true;
			for (GameItem item : c.getTradeAndDuel().offeredItems) {
				if (item.id > 0) {
					if (ot.getItems().freeSlots() < c.getTradeAndDuel().offeredItems.size()) {
						c.sendMessage(ot.playerName + " only has " + ot.getItems().freeSlots()
								+ " free slots, please remove "
								+ (c.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots()) + " items.");
						ot.sendMessage(c.playerName + " has to remove "
								+ (c.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots())
								+ " items or you could offer them "
								+ (c.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots()) + " items.");
						c.goodTrade = false;
						ot.goodTrade = false;
						c.getPA().sendFrame126("Not enough inventory space...", 3431);
						ot.getPA().sendFrame126("Not enough inventory space...", 3431);
						break;
					} else {
						c.getPA().sendFrame126("Waiting for other player...", 3431);
						ot.getPA().sendFrame126("Other player has accepted", 3431);
						c.goodTrade = true;
						ot.goodTrade = true;
					}
				}
			}
			if (c.inTrade && !c.tradeConfirmed && ot.goodTrade && c.goodTrade) {
				c.tradeConfirmed = true;
				if (ot.tradeConfirmed) {
					c.getTradeAndDuel().confirmScreen();
					ot.getTradeAndDuel().confirmScreen();
					break;
				}

			}
			break;

		case 13218:
			c.tradeAccepted = true;
			Player ot1 = World.PLAYERS.get(c.tradeWith);
			if (ot1 == null) {
				c.getTradeAndDuel().declineTrade(false);
				c.sendMessage("Trade declined as the other player has disconnected.");
				break;
			}
			if (c.inTrade && c.tradeConfirmed && ot1.tradeConfirmed && !c.tradeConfirmed2) {
				c.tradeConfirmed2 = true;
				if (ot1.tradeConfirmed2) {
					c.acceptedTrade = true;
					ot1.acceptedTrade = true;
					c.getTradeAndDuel().giveItems();
					ot1.getTradeAndDuel().giveItems();
					break;
				}
				ot1.getPA().sendFrame126("Other player has accepted.", 3535);
				c.getPA().sendFrame126("Waiting for other player...", 3535);
			}
			break;
		case 154:
		case 74108:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			if (!c.skillCapeEquipped()) {
				c.sendMessage("You need a skill cape equiped to perform this animation.");
				return;
			}
			int capes[] = { 9784, 9763, 9793, 9796, 9766, 9781, 9799, 9790, 9802, 9808, 9748, 9754, 9811, 9778, 9787,
					9775, 9760, 9757, 9805, 9772, 9769, 9751, 9949, 9813, 12170 };
			int emotes[] = { 4937, 4939, 4941, 4943, 4947, 4949, 4951, 4953, 4955, 4957, 4959, 4961, 4963, 4965, 4967,
					4969, 4979, 4973, 4975, 4977, 4971, 4981, 5158, 4945, 8525 };
			int gfx[] = { 812, 813, 814, 815, 817, 818, 819, 820, 821, 822, 823, 824, 825, 826, 827, 835, 829, 832, 831,
					830, 833, 828, 907, 816, 1515 };
			for (int i = 0; i < capes.length; i++) {
				if (c.playerEquipment[Player.playerCape] == capes[i]
						|| c.playerEquipment[Player.playerCape] == capes[i] - 1) {
					c.startAnimation(emotes[i]);
					c.gfx0(gfx[i]);
					c.buttonDelay = System.currentTimeMillis();
				}
			}
			break;

		/* Player Options */
		case 3189:
		case 74184:
			if (!c.splitChat) {
				c.splitChat = true;
				c.getPA().sendFrame36(502, 1);
				c.getPA().sendFrame36(287, 1);
			} else {
				c.splitChat = false;
				c.getPA().sendFrame36(502, 0);
				c.getPA().sendFrame36(287, 0);
			}
			break;
		case 3147:
		case 74180:
			if (!c.chatEffects) {
				c.chatEffects = true;
				c.getPA().sendFrame36(501, 1);
				c.getPA().sendFrame36(171, 0);
			} else {
				c.chatEffects = false;
				c.getPA().sendFrame36(501, 0);
				c.getPA().sendFrame36(171, 1);
			}
			break;
		case 48176:
		case 74188:
			if (!c.acceptAid) {
				c.acceptAid = true;
				c.getPA().sendFrame36(503, 1);
				c.getPA().sendFrame36(427, 1);
			} else {
				c.acceptAid = false;
				c.getPA().sendFrame36(503, 0);
				c.getPA().sendFrame36(427, 0);
			}
			break;
		case 74192:
			if (!c.isRunning2) {
				c.isRunning2 = true;
				c.getPA().sendFrame36(504, 1);
				c.getPA().sendFrame36(173, 1);
			} else {
				c.isRunning2 = false;
				c.getPA().sendFrame36(504, 0);
				c.getPA().sendFrame36(173, 0);
			}
			break;
		case 74201:// brightness1
			c.getPA().sendFrame36(505, 1);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 1);
			break;
		case 74203:// brightness2
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 1);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 2);
			break;

		case 74204:// brightness3
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 1);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 3);
			break;

		case 74205:// brightness4
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 1);
			c.getPA().sendFrame36(166, 4);
			break;
		case 74206:// area1
			c.getPA().sendFrame36(509, 1);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74207:// area2
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 1);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74208:// area3
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 1);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74209:// area4
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 1);
			break;
		case 168:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(855);
			break;
		case 169:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(856);
			break;
		case 162:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(857);
			break;

		case 165:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			if (c.playerEquipment[0] == 10392) {
				c.startAnimation(5315);
			} else {
				c.startAnimation(859);
			}
			c.buttonDelay = System.currentTimeMillis();
			break;
		case 73013:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(2414);
			c.gfx0(1537);
			break;
		case 161:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(860);
			break;
		case 170:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(861);
			break;
		case 171:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(862);
			break;
		case 163:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(863);
			break;
		case 167:

			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			if (c.playerEquipment[Player.playerHat] == 10392) {
				c.startAnimation(5315);
			} else {
				c.startAnimation(864);
			}
			c.buttonDelay = System.currentTimeMillis();
			break;
		case 172:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(865);
			break;
		case 166:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			if (c.playerEquipment[7] == 10394) {
				c.startAnimation(5316);
			} else {
				c.startAnimation(866);
			}
			c.buttonDelay = System.currentTimeMillis();
			break;
		case 52050:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(2105);
			break;
		case 52051:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(2106);
			break;
		case 52052:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(2107);
			break;
		case 52053:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(2108);
			break;
		case 52054:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(2109);
			break;

		case 52055:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(2110);
			break;
		case 52056:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			if (c.playerEquipment[0] == 10398) {
				c.startAnimation(5313);
			} else {
				c.startAnimation(2111);
			}
			break;
		case 52057:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(2112);
			break;
		case 52058:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(2113);
			break;
		case 43092:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(0x558);
			c.gfx0(574);
			break;
		case 2155:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(0x46B);
			break;
		case 25103:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(0x46A);
			break;
		case 25106:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(0x469);
			break;
		case 2154:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(0x468);
			break;
		case 52071:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(0x84F);
			break;
		case 52072:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(0x850);
			break;
		case 59062:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(2836);
			break;

		case 72032:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(3544);
			break;
		case 72033:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(3543);
			break;
		case 72254:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(6111);
			break;
		case 73004:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(7272);
			c.gfx0(1244);
			break;
		case 73012:
			if (System.currentTimeMillis() - c.buttonDelay < 3000) {
				return;
			}
			c.buttonDelay = System.currentTimeMillis();
			c.startAnimation(7531);
			break;
		/* END OF EMOTES */
		case 118098:
			if (c.playerLevel[1] >= 40) {
				c.getPA().vengMe();
			} else {
				c.sendMessage("You need a defense level of 40 to Veng.");
			}
			break;

		case 55095:
			c.getPA().destroyItem(c.destroyItem);
			break;
		case 55096:
			c.getPA().closeAllWindows();
			break;

		case 10143:
			c.getPA().closeAllWindows();
			break;

		case 7217:
		case 7212:
		case 24017:
			c.getPA().resetAutoCast();
			c.getItems().sendWeapon(c.playerEquipment[Player.playerWeapon],
					c.getItems().getItemName(c.playerEquipment[Player.playerWeapon]));
			break;
		}
		if (c.isAutoButton(buttonId)) {
			c.assignAutocast(buttonId);
		}
		c.lastClickedButton = buttonId;
	}
}
