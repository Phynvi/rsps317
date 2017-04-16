package com.bclaus.rsps.server.vd.player;

import java.util.concurrent.CopyOnWriteArrayList;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueDifficulty;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.items.GameItem;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.items.ItemAssistant;
import com.bclaus.rsps.server.vd.npc.pets.Pet;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

public class TradeAndDuel {

	private final Player c;

	public TradeAndDuel(Player Client) {
		this.c = Client;
	}

	/**
	 * Trading
	 */

	public CopyOnWriteArrayList<GameItem> offeredItems = new CopyOnWriteArrayList<>();

	private String formatPlayerName(String str) {
		String str1 = Misc.ucFirst(str);
		str1.replace("_", " ");
		return str1;
	}

	public void requestTrade(int id) {

		Player o = World.PLAYERS.get(id);
		if (o == null || c.getIndex() < 0 || id == c.getIndex() || c.tradeWith == c.getIndex()) {
			return;
		}
		/*if(c.playerRights == 4){
			c.sendMessage("Because i'm can spawn i can't trade");
			return;
		}*/
		if (c.underAttackBy > 0 || c.underAttackBy2 > 0 || o.underAttackBy > 0 || o.underAttackBy2 > 0) {
			return;
		}
		if (!Player.tradeEnabled) {
			c.sendMessage("The server is currently restricting all trades");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			if (c.openInterface != 59500) {
				c.getBankPin().open(2);
			}
			return;
		}
		if (!c.getAccount().getType().tradingPermitted()) {
			c.sendMessage("You are not permitted to trade beacuse of a restriction on your account.");
			return;
		}
		if (!o.getAccount().getType().tradingPermitted()) {
			c.sendMessage("This player is not permitted to trade beacuse of a restriction on their account.");
			return;
		}
		/*if (PKHandler.isSameConnection(c, o) && c.playerRights != 5) {
			c.sendMessage("You cannot trade your own connection!");
			return;
		}*/
		if (c.inWild()) {
			c.sendMessage("You cannot request a trade in this area");
			return;
		}
		if (c.inDuelArena()) {
			c.sendMessage("You cannot request a trade in this area");
			return;
		}
		if (c.absX == o.absX && c.absY == o.absY) {
			c.sendMessage("You cannot trade while standing on someone");
			return;
		}
		c.tradeWith = id;

		/*if (o.totalPlaytime() < 3000) {
			c.sendMessage("You can't request a trade if the other person hasn't played for atleast 30 minutes.");
			return;
		}*/
		/*if (c.totalPlaytime() < 3000) {
			c.sendMessage("You can't request a trade without playing for atleast 30 minutes.");
			return;
		}*/

		if (o.duelStatus == 6 || o.inTrade || o.isBanking || o.playerIsNPC || o.tradeStatus > 0) {
			c.sendMessage("This player is currently busy.");
			return;
		}
		if (!c.inTrade && o.tradeRequested && o.tradeWith == c.getIndex()) {
			c.getTradeAndDuel().openTrade();
			o.getTradeAndDuel().openTrade();
		} else if (!c.inTrade) {
			c.tradeRequested = true;
			c.sendMessage("Sending trade request...");
			o.sendMessage(c.playerName + ":tradereq:");
		}
	}

	public void openTrade() {
		Player o = World.PLAYERS.get(c.tradeWith);
		if (o == null || c.tradeWith == c.getIndex() || c.duelStatus == 6 || c.isBanking || c.inDuelScreen) {
			return;
		}
		c.inTrade = true;
		c.canOffer = true;
		c.tradeStatus = 1;
		o.getPA().sendFrame126("" + formatPlayerName(c.playerName) + "", 3451);
		c.getPA().sendFrame126("" + formatPlayerName(o.playerName) + "", 3451);
		o.getPA().sendFrame126("Trading with: " + formatPlayerName(c.playerName) + "", 3417);
		c.getPA().sendFrame126("Trading with: " + formatPlayerName(o.playerName) + "", 3417);
		c.tradeRequested = false;
		c.getItems().resetItems(3322);
		resetTItems(3415);
		resetOTItems(3416);
		String out = o.playerName;
		if (o.playerRights == 1) {
			out = "@cr1@" + out;
		} else if (o.playerRights == 2) {
			out = "@cr2@" + out;
		}
		displayWAndI(c);
		c.getPA().sendFrame126("", 3431);
		c.getPA().sendFrame126("Are you sure you want to make this trade?", 3535);
		c.getPA().sendFrame248(3323, 3321);
	}

	public void displayWAndI(Player c) {
		Player o = World.PLAYERS.get(c.tradeWith);
		c.getPA().sendFrame126(formatPlayerName(o.playerName) + " has\\n " + o.getItems().freeSlots() + " free\\n inventory slots.", 23505);
		o.getPA().sendFrame126(formatPlayerName(c.playerName) + " has\\n " + c.getItems().freeSlots() + " free\\n inventory slots.", 23505);
	}

	public void resetTItems(int WriteFrame) {
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(WriteFrame);
		int len = offeredItems.toArray().length;
		int current = 0;
		c.getOutStream().writeWord(len);
		for (GameItem item : offeredItems) {
			if (item.amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(item.amount);
			} else {
				c.getOutStream().writeByte(item.amount);
			}
			c.getOutStream().writeWordBigEndianA(item.id + 1);
			current++;
		}
		if (current < 27) {
			for (int i = current; i < 28; i++) {
				c.getOutStream().writeByte(1);
				c.getOutStream().writeWordBigEndianA(-1);
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public boolean fromTrade(int itemID, int amount) {
		Player o = World.PLAYERS.get(c.tradeWith);
		if (o == null || !c.inTrade || !c.canOffer) {
			declineTrade();
			return false;
		}
		falseTradeConfirm();
		if (!Item.itemStackable[itemID]) {
			if (amount > 28) {
				amount = 28;
			}
			for (int a = 0; a < amount; a++) {
				for (GameItem item : offeredItems) {
					if (item.id == itemID) {
						if (!item.stackable) {
							offeredItems.remove(item);
							c.getItems().addItem(itemID, 1);
						} else {
							if (item.amount > amount) {
								item.amount -= amount;
								c.getItems().addItem(itemID, amount);
							} else {
								amount = item.amount;
								offeredItems.remove(item);
								c.getItems().addItem(itemID, amount);
							}
						}
						break;
					}
				}
			}
		}
		for (GameItem item : offeredItems) {
			if (item.id == itemID) {
				if (!item.stackable) {
				} else {
					if (item.amount > amount) {
						item.amount -= amount;
						c.getItems().addItem(itemID, amount);
					} else {
						amount = item.amount;
						offeredItems.remove(item);
						c.getItems().addItem(itemID, amount);
					}
				}
				break;
			}
		}
		falseTradeConfirm();
		c.getItems().resetItems(3322);
		resetTItems(3415);
		o.getTradeAndDuel().resetOTItems(3416);
		displayWAndI(c);
		c.getPA().sendFrame126("", 3431);
		o.getPA().sendFrame126("", 3431);
		return true;
	}

	public boolean tradeable(int itemId) {
		for (int i : Constants.ITEM_TRADEABLE) {
			if (i == itemId) {
				return false;
			}
		}
		return true;
	}

	private boolean falseTradeConfirm() {
		Player o = World.PLAYERS.get(c.tradeWith);
		return c.tradeConfirmed = o.tradeConfirmed = false;
	}

	public boolean tradeItem(int itemID, int fromSlot, int amount) {
		Player o = World.PLAYERS.get(c.tradeWith);
		if (o == null || c.playerItems[fromSlot] - 1 != itemID) {
			return false;
		}
		if (amount <= 0)
			return false;
		if (ClueDifficulty.isClue(itemID)) {
			c.sendMessage("You cannot trade clue scrolls!");
			return false;
		}
		if (itemID == 2714) {
			c.sendMessage("You cannot trade clue reward caskets!");
			return false;
		}
		if (c.getAchievements().isAchievementItem(itemID)) {
			c.sendMessage("You cannot trade an achievement reward item!");
			return false;
		}
		if (Pet.get(itemID) != null) {
			c.sendMessage("You cannot trade a pet item!");
			return false;
		}
		if (!c.getAccount().getType().tradingPermitted()) {
			c.sendMessage("You are not permitted to trade beacuse of a restriction on your account.");
			return false;
		}
		if (!o.getAccount().getType().tradingPermitted()) {
			c.sendMessage("This player is not permitted to trade beacuse of a restriction on their account.");
			return false;
		}
		for (int i : Constants.ITEM_TRADEABLE) {
			if (i == itemID) {
				c.sendMessage("You can't trade this item.");
				return false;
			}
		}
		falseTradeConfirm();
		if (!Item.itemStackable[itemID] && !Item.itemIsNote[itemID]) {
			for (int a = 0; a < amount && a < 28; a++) {
				if (c.getItems().playerHasItem(itemID, 1)) {
					offeredItems.add(new GameItem(itemID, 1));
					c.getItems().deleteItem(itemID, c.getItems().getItemSlot(itemID), 1);
				}
			}
			c.getItems().resetItems(3322);
			resetTItems(3415);
			o.getTradeAndDuel().resetOTItems(3416);
			displayWAndI(c);
			c.getPA().sendFrame126("", 3431);
			o.getPA().sendFrame126("", 3431);
		}
		if (c.getItems().getItemCount(itemID) < amount) {
			amount = c.getItems().getItemCount(itemID);
			if (amount == 0 || !c.getItems().playerHasItem(itemID, amount)) {
				return false;
			}
		}
		if (!c.inTrade || !c.canOffer) {
			declineTrade();
			return false;
		}
		if (Item.itemStackable[itemID] || Item.itemIsNote[itemID]) {
			boolean inTrade = false;
			for (GameItem item : offeredItems) {
				if (item.id == itemID) {
					inTrade = true;
					item.amount += amount;
					c.getItems().deleteItem2(itemID, amount);
					break;
				}
			}

			if (!inTrade) {
				offeredItems.add(new GameItem(itemID, amount));
				c.getItems().deleteItem2(itemID, amount);
			}
		}
		c.getItems().resetItems(3322);
		resetTItems(3415);
		o.getTradeAndDuel().resetOTItems(3416);
		displayWAndI(c);
		c.getPA().sendFrame126("", 3431);
		o.getPA().sendFrame126("", 3431);
		return true;
	}

	public void resetTrade() {
		offeredItems.clear();
		c.inTrade = false;
		c.tradeWith = 0;
		c.canOffer = true;
		c.tradeConfirmed = false;
		c.tradeConfirmed2 = false;
		c.acceptedTrade = false;
		c.getPA().removeAllWindows();
		c.tradeResetNeeded = false;
		c.getPA().sendFrame126("Are you sure you want to make this trade?", 3535);
	}

	public void declineTrade() { // seems like the fix i did
		Player o = World.PLAYERS.get(c.tradeWith);
		if (o == null) {
			c.tradeStatus = 0;
			declineTrade(true);
			return;
		}
		o.tradeStatus = 0;
		c.tradeStatus = 0;
		declineTrade(true);
	}

	public void declineTrade(boolean tellOther) {
		c.getPA().removeAllWindows();
		Player o = World.PLAYERS.get(c.tradeWith);
		if (o == null) {
			return;
		}
		if (tellOther) {
			o.getTradeAndDuel().declineTrade(false);
		}
		for (GameItem item : offeredItems) {
			if (item.amount < 1) {
				continue;
			}
			if (item.stackable) {
				c.getItems().addItem(item.id, item.amount);
			} else {
				for (int i = 0; i < item.amount; i++) {
					c.getItems().addItem(item.id, 1);
				}
			}
		}
		c.canOffer = true;
		c.tradeConfirmed = false;
		c.tradeConfirmed2 = false;
		offeredItems.clear();
		c.inTrade = false;
		c.tradeWith = 0;
	}

	public void resetOTItems(int WriteFrame) {
		Player o = World.PLAYERS.get(c.tradeWith);
		if (o == null) {
			return;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(WriteFrame);
		int len = o.getTradeAndDuel().offeredItems.toArray().length;
		int current = 0;
		c.getOutStream().writeWord(len);
		for (GameItem item : o.getTradeAndDuel().offeredItems) {
			if (item.amount > 254) {
				c.getOutStream().writeByte(255); // item's stack count. if
													// over 254, write byte
													// 255
				c.getOutStream().writeDWord_v2(item.amount);
			} else {
				c.getOutStream().writeByte(item.amount);
			}
			c.getOutStream().writeWordBigEndianA(item.id + 1); // item id
			current++;
		}
		if (current < 27) {
			for (int i = current; i < 28; i++) {
				c.getOutStream().writeByte(1);
				c.getOutStream().writeWordBigEndianA(-1);
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public void confirmScreen() {
		Player o = World.PLAYERS.get(c.tradeWith);
		if (o == null) {
			return;
		}
		if (o.tradeWith != c.getIndex()) {
			declineTrade();
			return;
		}
		c.canOffer = false;
		c.getItems().resetItems(3214);
		String SendTrade = "Absolutely nothing!";
		String SendAmount;
		int Count = 0;
		for (GameItem item : offeredItems) {
			if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@(" + Misc.format(item.amount) + ")";
				} else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + (item.amount / 1000000) + " million @whi@(" + Misc.format(item.amount) + ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}
				if (Count == 0) {
					c.getItems();
					SendTrade = ItemAssistant.getItemName(item.id);
				} else {
					c.getItems();
					SendTrade = SendTrade + "\\n" + ItemAssistant.getItemName(item.id);
				}

				if (item.stackable) {
					SendTrade = SendTrade + " x " + SendAmount;
				}
				Count++;
			}
		}

		c.getPA().sendFrame126(SendTrade, 3557);
		SendTrade = "Absolutely nothing!";
		SendAmount = "";
		Count = 0;

		for (GameItem item : o.getTradeAndDuel().offeredItems) {
			if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@(" + Misc.format(item.amount) + ")";
				} else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + (item.amount / 1000000) + " million @whi@(" + Misc.format(item.amount) + ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}
				// SendAmount = SendAmount;
				if (Count == 0) {
					c.getItems();
					SendTrade = ItemAssistant.getItemName(item.id);
				} else {
					c.getItems();
					SendTrade = SendTrade + "\\n" + ItemAssistant.getItemName(item.id);
				}
				if (item.stackable) {
					SendTrade = SendTrade + " x " + SendAmount;
				}
				Count++;
			}
		}
		c.getPA().sendFrame126(SendTrade, 3558);
		c.getPA().sendFrame248(3443, 197);
	}

	public void giveItems() {
		c.logoutDelay = System.currentTimeMillis();
		Player o = World.PLAYERS.get(c.tradeWith);
		if (o == null) {
			return;
		}
		if (c.disconnected || o.disconnected) {
			c.getTradeAndDuel().declineTrade(true);
			o.getTradeAndDuel().declineTrade(true);
			return;
		}
		String items = "Items Received: ";
		try {
			for (GameItem item : o.getTradeAndDuel().offeredItems) {
				if (item.id > 0) {
					c.getItems().addItem(item.id, item.amount);
					c.getTradeLog().tradeReceived(ItemAssistant.getItemName(item.id), item.amount);
					o.getTradeLog().tradeGive(ItemAssistant.getItemName(item.id), item.amount);
					if (ItemAssistant.getItemName(item.id) != null) {
					
						items = items + " " + ItemAssistant.getItemName(item.id) + " x" + item.amount;
					}
				}

			}
			c.getPA().removeAllWindows();
			c.tradeResetNeeded = true;
			Server.getTaskScheduler().schedule(new ScheduledTask(1) {
				@Override
				public void execute() {
					if (c.inTrade && c.tradeResetNeeded) {
						Player o = World.PLAYERS.get(c.tradeWith);
						if (o != null) {
							if (o.tradeResetNeeded) {
								c.getTradeAndDuel().resetTrade();
								o.getTradeAndDuel().resetTrade();
								c.tradeStatus = 0;
								o.tradeStatus = 0;
							}
						}
						this.stop();
					}
				}

				@Override
				public void onStop() {
					c.tradeResetNeeded = false;
				}
			}.attach(c));
			PlayerSave.saveGame(c);

		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
	}

	/**
	 * Dueling
	 */

	public CopyOnWriteArrayList<GameItem> otherStakedItems = new CopyOnWriteArrayList<GameItem>();
	public CopyOnWriteArrayList<GameItem> stakedItems = new CopyOnWriteArrayList<GameItem>();

	public void requestDuel(int id) {
		if (id == c.getIndex()) {
			return;
		}
		if (c.totalPlaytime() < 6000) {
			c.sendMessage("You need to play at least an hour before dueling other players");
			return;
		}
		if (!Player.tradeEnabled) {
			c.sendMessage("The server is currently restricting all duels");
			return;
		}
		resetDuel();
		resetDuelItems();
		c.duelingWith = id;
		Player o = World.PLAYERS.get(id);
		if (o.inDuelScreen || o.inTrade || o.isBanking || o.playerIsNPC || o.duelStatus == 6 || o.duelStatus > 0) {
			c.sendMessage("This player is currently busy.");
			return;
		}
		if (c.absX == o.absX && c.absY == o.absY) {
			c.sendMessage("You cannot request a duel while standing on top of them");
			return;
		}
		/*if (PKHandler.isSameConnection(c, o) && c.playerRights != 5) {
			c.sendMessage("You cannot duel a player on the same connection");
			// return;
		}*/
		if (c.totalPlaytime() < 16000) {
			c.sendMessage("You need to play at least 2 hours before using this feature.");
			return;
		}
		if (c.duelStatus > 0) {
			c.sendMessage("You must decline this duel before accepting another one!");
			return;
		}
		if (c.gameMode == 3) {
			c.sendMessage("Players on the Iron Man difficulty are not able to duel.");
			o.sendMessage("A player on the Iron Man difficulty is trying to duel with you.");
			return;
		}
		c.duelRequested = true;
		if (c.duelStatus == 0 && o.duelStatus == 0 && c.duelRequested && o.duelRequested && c.duelingWith == o.getIndex() && o.duelingWith == c.getIndex()) {
			if (Player.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), 1)) {
				if (c.duelStatus == 0) {
					c.getTradeAndDuel().openDuel();
					o.getTradeAndDuel().openDuel();
				} else {
					c.sendMessage("You must decline this duel before accepting another one!");
				}
			} else {
				c.sendMessage("You need to get closer to your opponent to start the duel.");
			}

		} else {
			c.sendMessage("Sending duel request...");
			o.sendMessage(c.playerName + ":duelreq:");
		}
	}

	public void openDuel() {
		Player o = World.PLAYERS.get(c.duelingWith);
		if (o == null || c.inTrade || c.duelStatus == 6 || c.isBanking) {
			return;
		}
		c.sendMessage("Note : Fun-weapon option enables DDS & Whip ONLY");
		c.inDuelScreen = true;
		c.duelStatus = 1;
		refreshduelRules();
		refreshDuelScreen();
		c.canOffer = true;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			sendDuelEquipment(c.playerEquipment[i], c.playerEquipmentN[i], i);
		}
		c.getPA().sendFrame126("Dueling with: " + o.playerName + " (level-" + o.combatLevel + ")", 6671);
		c.getPA().sendFrame126("", 6684);
		c.getPA().sendFrame248(6575, 3321);
		c.getItems().resetItems(3322);

	}

	public void sendDuelEquipment(int itemId, int amount, int slot) {
		if (itemId != 0) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(13824);
			c.getOutStream().writeByte(slot);
			c.getOutStream().writeWord(itemId + 1);

			if (amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(amount);
			} else {
				c.getOutStream().writeByte(amount);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void refreshduelRules() {
		for (int i = 0; i < c.duelRule.length; i++) {
			c.duelRule[i] = false;
		}
		c.getPA().sendFrame87(286, 0);
		c.duelOption = 0;
	}

	public void refreshDuelScreen() {
		Player o = World.PLAYERS.get(c.duelingWith);
		if (o == null) {
			return;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(6669);
		c.getOutStream().writeWord(stakedItems.toArray().length);
		int current = 0;
		for (GameItem item : stakedItems) {
			if (item.amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(item.amount);
			} else {
				c.getOutStream().writeByte(item.amount);
			}
			if (item.id > Constants.ITEM_LIMIT || item.id < 0) {
				item.id = Constants.ITEM_LIMIT;
			}
			c.getOutStream().writeWordBigEndianA(item.id + 1);

			current++;
		}

		if (current < 27) {
			for (int i = current; i < 28; i++) {
				c.getOutStream().writeByte(1);
				c.getOutStream().writeWordBigEndianA(-1);
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();

		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(6670);
		c.getOutStream().writeWord(o.getTradeAndDuel().stakedItems.toArray().length);
		current = 0;
		for (GameItem item : o.getTradeAndDuel().stakedItems) {
			if (item.amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(item.amount);
			} else {
				c.getOutStream().writeByte(item.amount);
			}
			if (item.id > Constants.ITEM_LIMIT || item.id < 0) {
				item.id = Constants.ITEM_LIMIT;
			}
			c.getOutStream().writeWordBigEndianA(item.id + 1);
			current++;
		}

		if (current < 27) {
			for (int i = current; i < 28; i++) {
				c.getOutStream().writeByte(1);
				c.getOutStream().writeWordBigEndianA(-1);
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public boolean stakeItem(int itemID, int fromSlot, int amount) {
		if (c.playerItemsN[fromSlot] < amount) {
			c.getItems();
			c.sendMessage("You don't have enough " + ItemAssistant.getItemName(itemID) + " in this slot to offer.");
			return false;
		}
		if (ClueDifficulty.isClue(itemID)) {
			c.sendMessage("You cannot stake clue scrolls!");
			return false;
		}
		if (Pet.get(itemID) != null) {
			c.sendMessage("You cannot trade a pet item!");
			return false;
		}
		if (itemID == 2714) {
			c.sendMessage("You cannot stake clue reward caskets!");
			return false;
		}
		for (int i : Constants.ITEM_TRADEABLE) {
			if (i == itemID) {
				c.sendMessage("You can't stake this item.");
				return false;
			}
		}
		if (c.inTrade || !c.inDuelScreen || !c.canOffer) {
			return false;
		}
		if (c.playerItems[fromSlot] - 1 != itemID) {
			return false;
		}
		if (!c.getItems().playerHasItem(itemID, amount)) {
			return false;
		}
		if (amount <= 0)
			return false;
		Player o = World.PLAYERS.get(c.duelingWith);
		if (o == null) {
			declineDuel();
			return false;
		}
		if (o.duelStatus <= 0 || c.duelStatus <= 0) {
			declineDuel();
			o.getTradeAndDuel().declineDuel();
			return false;
		}
		if (!c.getAccount().getType().stakeItems()) {
			c.sendMessage("Your account type does not permit you to stake items.");
			return false;
		}
		if (!o.getAccount().getType().stakeItems()) {
			c.sendMessage("This players account type does not permit them to stake items.");
			return false;
		}
		changeDuelStuff();
		if (!Item.itemStackable[itemID]) {
			for (int a = 0; a < amount; a++) {
				if (c.getItems().playerHasItem(itemID, 1)) {
					stakedItems.add(new GameItem(itemID, 1));
					c.getItems().deleteItem(itemID, c.getItems().getItemSlot(itemID), 1);
				}
			}
			c.getItems().resetItems(3214);
			c.getItems().resetItems(3322);
			o.getItems().resetItems( 3214);
			o.getItems().resetItems( 3322);
			refreshDuelScreen();
			o.getTradeAndDuel().refreshDuelScreen();
			c.getPA().sendFrame126("", 6684);
			o.getPA().sendFrame126("", 6684);
		}

		if (!c.getItems().playerHasItem(itemID, amount)) {
			return false;
		}
		if (Item.itemStackable[itemID] || Item.itemIsNote[itemID]) {
			boolean found = false;
			for (GameItem item : stakedItems) {
				if (item.id == itemID) {
					found = true;
					item.amount += amount;
					c.getItems().deleteItem(itemID, fromSlot, amount);
					break;
				}
			}
			if (!found) {
				c.getItems().deleteItem(itemID, fromSlot, amount);
				stakedItems.add(new GameItem(itemID, amount));
			}
		}

		c.getItems().resetItems(3214);
		c.getItems().resetItems(3322);
		o.getItems().resetItems( 3214);
		o.getItems().resetItems( 3322);
		refreshDuelScreen();
		o.getTradeAndDuel().refreshDuelScreen();
		c.getPA().sendFrame126("", 6684);
		o.getPA().sendFrame126("", 6684);
		return true;
	}

	public boolean fromDuel(int itemID, int fromSlot, int amount) {
		Player o = World.PLAYERS.get(c.duelingWith);
		if (o == null) {
			declineDuel();
			return false;
		}
		if (!c.getItems().playerHasItem(itemID, amount)) {
			return false;
		}
		if (c.playerItems[fromSlot] - 1 != itemID) {
			return false;
		}
		if (c.duelStatus == 0 || c.duelStatus == 5) {
			c.getTradeAndDuel().stakedItems.clear();
			return false;
		}
		if (o.duelStatus == 6 || c.duelStatus > 2 && c.duelStatus < 7) {
			return false;
		}
		if (o.duelStatus <= 0 || c.duelStatus <= 0) {
			declineDuel();
			o.getTradeAndDuel().declineDuel();
			return false;
		}
		if (Item.itemStackable[itemID]) {
			if (c.getItems().freeSlots() - 1 < (c.duelSpaceReq)) {
				c.sendMessage("You have too many rules set to remove that item.");
				return false;
			}
		}

		if (!c.canOffer && !c.inDuelScreen) {
			return false;
		}

		changeDuelStuff();
		boolean goodSpace = true;
		if (!Item.itemStackable[itemID]) {
			for (int a = 0; a < amount; a++) {
				for (GameItem item : stakedItems) {
					if (item.id == itemID) {
						if (!item.stackable) {
							if (c.getItems().freeSlots() - 1 < (c.duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							if (c.duelStatus == 0 || c.duelStatus == 5) {
								c.getTradeAndDuel().stakedItems.clear();
								return false;
							}
							stakedItems.remove(item);
							c.getItems().addItem(itemID, 1);
						} else {
							if (c.getItems().freeSlots() - 1 < (c.duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							if (item.amount > amount) {
								item.amount -= amount;
								c.getItems().addItem(itemID, amount);
							} else {
								if (c.getItems().freeSlots() - 1 < (c.duelSpaceReq)) {
									goodSpace = false;
									break;
								}
								amount = item.amount;
								stakedItems.remove(item);
								c.getItems().addItem(itemID, amount);
							}
						}
						break;
					}
					o.duelStatus = 1;
					c.duelStatus = 1;
					c.getItems().resetItems(3214);
					c.getItems().resetItems(3322);
					o.getItems().resetItems( 3214);
					o.getItems().resetItems( 3322);
					c.getTradeAndDuel().refreshDuelScreen();
					o.getTradeAndDuel().refreshDuelScreen();
					o.getPA().sendFrame126("", 6684);
				}
			}
		}

		for (GameItem item : stakedItems) {
			if (item.id == itemID) {
				if (!item.stackable) {
				} else {
					if (item.amount > amount) {
						item.amount -= amount;
						c.getItems().addItem(itemID, amount);
					} else {
						amount = item.amount;
						stakedItems.remove(item);
						c.getItems().addItem(itemID, amount);
					}
				}
				break;
			}
		}
		o.duelStatus = 1;
		c.duelStatus = 1;
		c.getItems().resetItems(3214);
		c.getItems().resetItems(3322);
		o.getItems().resetItems( 3214);
		o.getItems().resetItems( 3322);
		c.getTradeAndDuel().refreshDuelScreen();
		o.getTradeAndDuel().refreshDuelScreen();
		o.getPA().sendFrame126("", 6684);
		if (!goodSpace) {
			c.sendMessage("You have too many rules set to remove that item.");
			return true;
		}
		return true;
	}

	public void confirmDuel() {
		Player o = World.PLAYERS.get(c.duelingWith);
		if (o == null) {
			declineDuel();
			return;
		}
		String itemId = "";
		for (GameItem item : stakedItems) {
			if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				c.getItems();
				itemId += ItemAssistant.getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
			} else {
				c.getItems();
				itemId += ItemAssistant.getItemName(item.id) + "\\n";
			}
		}
		c.getPA().sendFrame126(itemId, 6516);
		itemId = "";
		for (GameItem item : o.getTradeAndDuel().stakedItems) {
			if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				c.getItems();
				itemId += ItemAssistant.getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
			} else {
				c.getItems();
				itemId += ItemAssistant.getItemName(item.id) + "\\n";
			}
		}
		c.getPA().sendFrame126(itemId, 6517);
		c.getPA().sendFrame126("", 8242);
		for (int i = 8238; i <= 8253; i++) {
			c.getPA().sendFrame126("", i);
		}
		c.getPA().sendFrame126("Hitpoints will be restored.", 8250);
		c.getPA().sendFrame126("Boosted stats will be restored.", 8238);
		if (c.duelRule[8]) {
			c.getPA().sendFrame126("There will be obstacles in the arena.", 8239);
		}

		c.getPA().sendFrame126("", 8240);
		c.getPA().sendFrame126("", 8241);

		String[] rulesOption = { "Players cannot forfeit!", "Players cannot move.", "Players cannot use range.", "Players cannot use melee.", "Players cannot use magic.", "Players cannot drink pots.", "Players cannot eat food.", "Players cannot use prayer." };

		int lineNumber = 8242;
		for (int i = 0; i < 8; i++) {
			if (c.duelRule[i]) {
				c.getPA().sendFrame126("" + rulesOption[i], lineNumber);
				lineNumber++;
			}
		}
		c.getPA().sendFrame126("", 6571);
		c.getPA().sendFrame248(6412, 197);
	}

	public void startDuel() {
		if (c.disconnected) {
			duelVictory();
		}
		c.inDuelScreen = false;
		Player o = World.PLAYERS.get(c.duelingWith);
		if (o == null || o.disconnected) {
			duelVictory();

		}
		if (o != null) {
			c.duelName = o.playerName;
			c.duelLevel = o.combatLevel;
		} else {
			c.duelName = "???";
			c.duelLevel = 3;
		}
		c.headIconHints = 2;
		if (c.duelRule[7]) {
			for (int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows
				c.prayerActive[p] = false;
				c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);
			}
			c.headIcon = -1;
			c.getPA().requestUpdates();
		}
		if (c.duelRule[11]) {
			c.getItems().removeItem(0);
		}

		if (c.duelRule[12]) {
			c.getItems().removeItem(1);
		}
		if (c.duelRule[13]) {
			c.getItems().removeItem(2);
		}
		if (c.duelRule[14]) {
			c.getItems().removeItem(3);
		}
		if (c.duelRule[15]) {
			c.getItems().removeItem(4);
		}
		if (c.duelRule[16]) {
			c.getItems().removeItem(5);
		}
		if (c.duelRule[17]) {
			c.getItems().removeItem(7);
		}
		if (c.duelRule[18]) {
			c.getItems().removeItem(9);
		}
		if (c.duelRule[19]) {
			c.getItems().removeItem(10);
		}
		if (c.duelRule[20]) {
			c.getItems().removeItem(12);
		}
		if (c.duelRule[21]) {
			c.getItems().removeItem(13);
		}
		c.getItems();
		if ((c.duelRule[16] && c.getItems().is2handed(ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase(), c.playerEquipment[Player.playerWeapon]))) {
			c.getItems().removeItem(3);
		}
		c.duelStatus = 5;
		c.getPA().removeAllWindows();
		c.specAmount = 10;
		c.getItems().addSpecialBar(c.playerEquipment[Player.playerWeapon]);
		c.lastVeng = 0;
		c.vengOn = false;

		if (c.duelRule[8]) {
			if (c.duelRule[1]) {
				c.getPA().movePlayer(c.duelTeleX, c.duelTeleY, 0);
			} else {
				c.getPA().movePlayer(3366 + Misc.random(12), 3246 + Misc.random(6), 0);
			}
		} else {
			if (c.duelRule[1]) {
				c.getPA().movePlayer(c.duelTeleX, c.duelTeleY, 0);
			} else {
				c.getPA().movePlayer(3335 + Misc.random(12), 3246 + Misc.random(6), 0);
			}
		}
		if (o != null) {
			c.getPA().createPlayerHints(10, o.getIndex());
		}
		c.getPA().showOption(3, 0, "Attack");
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = c.getPA().getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		if (o != null) {
			for (GameItem item : o.getTradeAndDuel().stakedItems) {
				otherStakedItems.add(new GameItem(item.id, item.amount));
			}
		}
		PlayerSave.saveGame(c);
		c.getPA().requestUpdates();
	}

	public void duelVictory() {
		Player o = World.PLAYERS.get(c.duelingWith);
		c.duelStatus = 6;
		c.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = c.getPA().getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		if (c.isSkulled) {
			c.isSkulled = false;
			c.skullTimer = 0;
			c.headIconPk = -1;
			c.getPA().requestUpdates();
		}
		c.getPA().refreshSkill(3);
		duelRewardInterface();
		c.getPA().sendFrame126(c.duelName, 6840);
		c.getPA().sendFrame126("" + c.duelLevel, 6839);
		c.getPA().showInterface(6733);
		claimStakedItems();
		c.getPA().movePlayer(Constants.DUELING_RESPAWN_X + (Misc.random(Constants.RANDOM_DUELING_RESPAWN)), Constants.DUELING_RESPAWN_Y + (Misc.random(Constants.RANDOM_DUELING_RESPAWN)), 0);
		c.getPA().requestUpdates();
		c.getPA().createPlayerHints(10, -1);
		c.canOffer = true;
		c.poisonDamage = 0;
		c.duelSpaceReq = 0;
		c.duelingWith = 0;
		c.getCombat().resetPlayerAttack();
		c.duelRequested = false;
		PlayerSave.saveGame(c);
		if (o != null)
			PlayerSave.saveGame(o);
	}

	public void duelRewardInterface() {
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(6822);
		c.getOutStream().writeWord(otherStakedItems.toArray().length);
		for (GameItem item : otherStakedItems) {
			if (item.amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(item.amount);
			} else {
				c.getOutStream().writeByte(item.amount);
			}
			if (item.id > Constants.ITEM_LIMIT || item.id < 0) {
				item.id = Constants.ITEM_LIMIT;
			}
			c.getOutStream().writeWordBigEndianA(item.id + 1);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public void claimStakedItems() {
		for (GameItem item : otherStakedItems) {
			if (item.id > 0 && item.amount > 0) {
				if (Item.itemStackable[item.id]) {
					if (!c.getItems().addItem(item.id, item.amount)) {
						Server.itemHandler.createGroundItem(c, item.id, 3363, 3269, c.heightLevel, item.amount);
					}
				} else {
					int amount = item.amount;
					for (int a = 1; a <= amount; a++) {
						if (!c.getItems().addItem(item.id, 1)) {
							Server.itemHandler.createGroundItem(c, item.id, 3363, 3269, c.heightLevel, 1);
						}
					}
				}
			}
		}
		for (GameItem item : stakedItems) {
			if (item.id > 0 && item.amount > 0) {
				if (Item.itemStackable[item.id]) {
					if (!c.getItems().addItem(item.id, item.amount)) {
						Server.itemHandler.createGroundItem(c, item.id, 3363, 3269, c.heightLevel, item.amount);
					}
				} else {
					int amount = item.amount;
					for (int a = 1; a <= amount; a++) {
						if (!c.getItems().addItem(item.id, 1)) {
							Server.itemHandler.createGroundItem(c, item.id, 3363, 3269, c.heightLevel, 1);
						}
					}
				}
			}
		}
		resetDuel();
		resetDuelItems();
		PlayerSave.saveGame(c);
		c.duelStatus = 0;
	}

	public void declineDuel() {
		declineDuel(true);
	}

	public void declineDuel(boolean tellOther) {
		c.getPA().removeAllWindows();
		Player o = World.PLAYERS.get(c.duelingWith);
		if (o == null || o.duelStatus == 6) {
			return;
		}
		o.getPA().removeAllWindows();
		// o.sendMessage("Other player declined trade!");
		if (tellOther) {
			o.getTradeAndDuel().declineDuel(false);
			o.getTradeAndDuel().c.getPA().removeAllWindows();
		}
		c.canOffer = true;
		c.duelStatus = 0;
		c.duelingWith = 0;
		c.duelSpaceReq = 0;
		c.inDuelScreen = false;
		c.duelRequested = false;
		for (GameItem item : stakedItems) {
			if (item.amount < 1) {
				continue;
			}
			if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				c.getItems().addItem(item.id, item.amount);
			} else {
				c.getItems().addItem(item.id, 1);
			}
		}
		stakedItems.clear();
		for (int i = 0; i < c.duelRule.length; i++) {
			c.duelRule[i] = false;
		}
	}

	public void resetDuel() {
		if (c.isDead) {
			c.lostDuel = true;
		}
		c.getPA().showOption(3, 0, "Challenge");
		c.headIconHints = 0;
		for (int i = 0; i < c.duelRule.length; i++) {
			c.duelRule[i] = false;
		}
		c.inDuelScreen = false;
		c.getPA().createPlayerHints(10, -1);
		c.canOffer = true;
		c.duelStatus = 0;
		c.duelSpaceReq = 0;
		c.duelingWith = 0;
		c.getPA().requestUpdates();
		c.getCombat().resetPlayerAttack();
		c.duelRequested = false;
	}

	public void resetDuelItems() {
		stakedItems.clear();
		otherStakedItems.clear();
	}

	public void changeDuelStuff() {
		Player o = World.PLAYERS.get(c.duelingWith);
		if (o == null) {
			return;
		}
		o.duelStatus = 1;
		c.duelStatus = 1;
		o.getPA().sendFrame126("", 6684);
		c.getPA().sendFrame126("", 6684);
	}

	public void selectRule(int i) {
		Player o = World.PLAYERS.get(c.duelingWith);
		if (o == null || !c.canOffer) {
			return;
		}
		changeDuelStuff();
		o.duelSlot = c.duelSlot;
		if (i >= 11 && c.duelSlot > -1) {
			if (c.playerEquipment[c.duelSlot] > 0) {
				if (!c.duelRule[i]) {
					c.duelSpaceReq++;
				} else {
					c.duelSpaceReq--;
				}
			}
			if (o.playerEquipment[o.duelSlot] > 0) {
				if (!o.duelRule[i]) {
					o.duelSpaceReq++;
				} else {
					o.duelSpaceReq--;
				}
			}
		}
		c.getItems();
		c.getItems();
		if (i == 16 && (c.getItems().is2handed(ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase(), c.playerEquipment[Player.playerWeapon]) && c.getItems().freeSlots() == 0) || (o.getItems().is2handed(ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase(), c.playerEquipment[Player.playerWeapon]) && o.getItems().freeSlots() == 0)) {
			c.sendMessage("You or your opponent don't have the required space to set this rule.");
			return;
		}
		if (i >= 11) {
			if (c.getItems().freeSlots() < (c.duelSpaceReq) || o.getItems().freeSlots() < (o.duelSpaceReq)) {
				c.sendMessage("You or your opponent don't have the required space to set this rule.");
				if (c.playerEquipment[c.duelSlot] > 0) {
					c.duelSpaceReq--;
				}
				if (o.playerEquipment[o.duelSlot] > 0) {
					o.duelSpaceReq--;
				}
				return;
			}
		}

		if (!c.duelRule[i]) {
			c.duelRule[i] = true;
			c.duelOption += c.DUEL_RULE_ID[i];
		} else {
			c.duelRule[i] = false;
			c.duelOption -= c.DUEL_RULE_ID[i];
		}

		c.getPA().sendFrame87(286, c.duelOption);
		o.duelOption = c.duelOption;
		o.duelRule[i] = c.duelRule[i];
		o.getPA().sendFrame87(286, o.duelOption);

		if (c.duelRule[8]) {
			if (c.duelRule[1]) {
				c.duelTeleX = 3366 + Misc.random(12);
				o.duelTeleX = c.duelTeleX - 1;
				c.duelTeleY = 3246 + Misc.random(6);
				o.duelTeleY = c.duelTeleY;
			}
		} else {
			if (c.duelRule[1]) {
				c.duelTeleX = 3335 + Misc.random(12);
				o.duelTeleX = c.duelTeleX - 1;
				c.duelTeleY = 3246 + Misc.random(6);
				o.duelTeleY = c.duelTeleY;
			}
		}

	}

	public void fromTrade(int xRemoveId, int xRemoveSlot, int i) {
		// TODO Auto-generated method stub

	}

}
