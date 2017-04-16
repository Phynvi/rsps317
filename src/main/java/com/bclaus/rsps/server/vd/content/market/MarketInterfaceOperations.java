package com.bclaus.rsps.server.vd.content.market;
/*package server.game.content.market;
import Connection;
import server.game.player.Player;
import server.game.player.PlayerHandler;
import Misc;

public class MarketInterfaceOperations {
	
	public static void open(Player player) {
		player.getPA().showInterface(54000);
	}
	
	public static void reset(Player player) { 
		player.getPA().sendFrame34a(54011, -1, 0, 0);
		player.getPA().sendFrame126("", 54012);
		player.getPA().sendFrame126("", 54013);
		player.getMarket().setMarketSearchItem(null);
		player.getMarket().setMarketOfferUptime(null);
		updateUptimeButton(player, false, false, false);
	}
	
	public static void clickButton(Player player, int buttonId) {
		if(buttonId >= 211091 && buttonId <= 212600) {
			MarketEntry[] results = player.getMarket().getSearchResults();
			if(results == null)
				return;
			for(MarketEntry entry : results) {
				if(entry != null) {
					if(buttonId == 211092 && entry.getSearchIndex() == 0 || buttonId == 211102 && entry.getSearchIndex() == 1 
							|| buttonId > 211102 && entry.getSearchIndex() == (buttonId - 211092) / 10
								|| buttonId == 212006 && entry.getSearchIndex() == 0
									|| buttonId > 212005 && entry.getSearchIndex() == ((buttonId - 212006) / 10) + 17) {
						if(isRemovalVisible(player, entry)) {
							MarketIO.removeEntry(entry);
							MarketIO.write();
							buildSearchResults(player);
							player.sendMessage("The offer has been removed from the database.");
							break;
						}
					}
				}
			}
			return;
		}
		switch(buttonId) {
			case 211016:
				player.getPA().closeAllWindows();
				break;
			case 211001:
				updateUptimeButton(player, true, false, false);
				player.getMarket().setMarketOfferUptime(MarketOfferUptime.ONE_DAY);
				break;
			case 211002:
				updateUptimeButton(player, false, true, false);
				player.getMarket().setMarketOfferUptime(MarketOfferUptime.THREE_DAYS);
				break;
			case 211003:
				updateUptimeButton(player, false, false, true);
				player.getMarket().setMarketOfferUptime(MarketOfferUptime.FIVE_DAYS);
				break;
			case 211014:
				if(!meetsOfferRequirements(player))
					return;
				player.getMarket().getMarketEntry().setBuying(true);
				player.getMarket().getMarketEntry().setPlayerName(player.playerName);
				player.getMarket().getMarketEntry().setPlayerInternetProtocol(player.connectedFrom);
				player.getMarket().getMarketEntry().setItemId(player.getMarket().getMarketSearchItem().getItem().itemId);
				player.getMarket().getMarketEntry().setUptime(player.getMarket().getMarketOfferUptime().getLifetime());
				if(player.getMarket().getMarketOfferUptime().getCost() > 0)
					player.getItems().deleteItem2(995, player.getMarket().getMarketOfferUptime().getCost());
				//player.getMarket().getMarketEntry().setDate(Date.getFutureDate(player.getMarket().getMarketOfferUptime().getLifetime(), 0));
				MarketIO.addEntry(player.getMarket().getMarketEntry());
				new MarketMessage(true, player.playerName, player.getMarket().getMarketSearchItem().getItem().itemName, player.getMarket().getMarketEntry().getItemAmount(), player.getMarket().getMarketEntry().getItemPrice()).send();
				buildSearchResults(player);
				MarketIO.write();
				break;
			case 211015:
				if(!meetsOfferRequirements(player))
					return;
				player.getMarket().getMarketEntry().setBuying(false);
				player.getMarket().getMarketEntry().setPlayerName(player.playerName);
				player.getMarket().getMarketEntry().setPlayerInternetProtocol(player.connectedFrom);
				player.getMarket().getMarketEntry().setItemId(player.getMarket().getMarketSearchItem().getItem().itemId);
				player.getMarket().getMarketEntry().setUptime(player.getMarket().getMarketOfferUptime().getLifetime());
				if(player.getMarket().getMarketOfferUptime().getCost() > 0)
					player.getItems().deleteItem2(995, player.getMarket().getMarketOfferUptime().getCost());
			//	player.getMarket().getMarketEntry().setDate(Date.getFutureDate(player.getMarket().getMarketOfferUptime().getLifetime(), 0));
				MarketIO.addEntry(player.getMarket().getMarketEntry());
				new MarketMessage(false, player.playerName, player.getMarket().getMarketSearchItem().getItem().itemName, player.getMarket().getMarketEntry().getItemAmount(), player.getMarket().getMarketEntry().getItemPrice()).send();
				buildSearchResults(player);
				MarketIO.write();
				break;
				
			case 210246:
				buildSearchResults(player);
				break;
		}
	}
	
	public static void updateUptimeButton(Player player, boolean visible1, boolean visible2, boolean visible3) {
		player.getPA().sendFrame36(700, visible1 == true ? 1 : 0);
		player.getPA().sendFrame36(701, visible2 == true ? 1 : 0);
		player.getPA().sendFrame36(702, visible3 == true ? 1 : 0);
	}
	
	public static boolean meetsOfferRequirements(Player player) {
		if(Connection.isMarketBanned(player.playerName)) {
			player.sendMessage("You are banned from the market center for abusing it.");
			return false;
		}
		if(!meetsSearchRequirements(player)) {
			player.sendMessage("Search for an item in the field below.");
			return false;
		}
		if(MarketIO.entryContains(player.getMarket().getMarketSearchItem().getItem().itemId, player.playerName)) {
			player.sendMessage("You already have an offer available for this item.");
			return false;
		}
		if(player.getMarket().getMarketOfferUptime() == null) {
			player.sendMessage("Choose an offer uptime, by default you can choose 1 day for free.");
			return false;
		}
		if(player.getMarket().getMarketOfferUptime().getCost() > 0 && !player.getItems().playerHasItem(995, player.getMarket().getMarketOfferUptime().getCost())) {
			player.sendMessage("You need atleast "+player.getMarket().getMarketOfferUptime().getCost()+" gp in your inventory to make this offer.");
			return false;
		}
		if(player.getMarket().getMarketEntry().getItemPrice() <= 0) {
			player.sendMessage("Enter a price for the item you are buying, or selling.");
			return false;
		}
		if(player.getMarket().getMarketEntry().getItemAmount() <= 0) {
			player.sendMessage("Enter the amount of the item you are buying, or selling.");
			return false;
		}
		if(!player.getTradeAndDuel().tradeable(player.getMarket().getMarketEntry().getItemId())) {
			player.sendMessage("This item is not tradable, you cannot sell this in the market.");
			return false;
		}
		if(MarketIO.getMarketSize(player.getMarket().getMarketSearchItem().getItem().itemId) >= 25) {
			MarketEntry[] entries = MarketIO.getEntries(player.getMarket().getMarketSearchItem().getItem().itemId);
			if(entries != null) {
				for(MarketEntry entry : entries) {
					if(entry != null) {
						if(entry.getUptime() == MarketOfferUptime.ONE_DAY.getLifetime()) {
							if(player.getMarket().getMarketOfferUptime().getLifetime() > MarketOfferUptime.ONE_DAY.getLifetime()) {
								MarketIO.removeEntry(entry);
								return true;
							} else {
								player.sendMessage("This item currently has 25 offers, some of which are not paid for.");
								player.sendMessage("If you want you can pay to increase your offers uptime and buy out a spot.");
								return false;
							}
						}
					}
				}
				player.sendMessage("This item currently has 25 offers which are paid for. You must wait till one expires.");
				return false;
			}
		}
		return true;
	}
	
	public static boolean meetsSearchRequirements(Player player) {
		if(Connection.isMarketBanned(player.playerName)) {
			player.sendMessage("You are banned from the market center for abusing it.");
			return false;
		}
		if(player.getMarket().getMarketSearchItem() == null || player.getMarket().getMarketSearchItem().getName() == null || ItemLoader.getItemId(player.getMarket().getMarketSearchItem().getName()) == 0) {
			player.sendMessage("You must first enter an existing item name in the search field.");
			return false;
		}
		if(System.currentTimeMillis() - player.getMarket().getLastMarketResults() < 10000) {
			player.sendMessage("You can only load search results every 10 seconds.");
			return false;
		}
		return true;
	}
	
	public static void searchItem(Player player, String name) {
		if(name.length() < 0)
			return;
		MarketSearchItem search = new MarketSearchItem(name);
		if(search.exists()) {
			player.getMarket().getMarketEntry().setItemName(search.getItem().itemName);
			player.getPA().sendFrame126(Misc.ucFirst(search.getItem().itemName) + " ("+search.getItem().itemId+")", 54012);
			player.getPA().sendFrame126(search.getItem().itemDescription.substring(0, search.getItem().itemDescription.length() < 35 ? search.getItem().itemDescription.length() : 35), 54013);
			player.getPA().sendFrame34a(54011, search.getItem().itemId, 0, 1);
			player.getMarket().setMarketSearchItem(search);
		} else {
			if(player.getMarket().getMarketSearchItem() != null && player.getMarket().getMarketSearchItem().getName() != null) {
				player.getMarket().getMarketSearchItem().setName(null);
				player.getPA().sendFrame34a(54011, -1, 0, 0);
			}
		}
	}
	
	public static void buildSearchResults(Player player) {
		if(!meetsSearchRequirements(player))
			return;
		MarketEntry[] results = MarketIO.getEntries(player.getMarket().getMarketSearchItem().getItem().itemId);
		if(results == null) {
			player.sendMessage("No offers to buy or sell this item have been made yet.");
			return;
		}
		player.getMarket().setSearchResults(results);
		int id = 0;
		boolean requiresUpdate = false;
		for(MarketEntry entry : results) {
			try {
				if(entry != null) {
					try {
						String[] date = entry.getExpiryDate().split("/");
						int year = Integer.parseInt(date[0]);
						int month = Integer.parseInt(date[1]);
						int day = Integer.parseInt(date[2]);
						int hour = Integer.parseInt(date[3]);
						if(Date.passed(year, month, day, hour, 0)) {
							MarketIO.removeEntry(entry);
							requiresUpdate = true;
							continue;
						}
					} catch (NumberFormatException nfe) {
						nfe.printStackTrace();
						continue;
					}
					int offset = 54100 + (id * 10);
					entry.setSearchIndex(id);
					player.getPA().sendFrame171(0, offset);
					player.getPA().sendFrame126(PlayerHandler.isPlayerOn(entry.getPlayerName()) ? "Online" : "Offline", offset + 2);
					if(PlayerHandler.isPlayerOn(entry.getPlayerName()))
						player.getPA().setInterfaceTextColor(offset + 2, 8, 151, 11);
					else
						player.getPA().setInterfaceTextColor(offset + 2, 180, 11, 9);
					player.getPA().sendFrame126(entry.getPlayerName(), offset + 3);
					player.getPA().sendFrame126(Misc.insertCommas(Integer.toString(entry.getItemAmount())) + " x "+Misc.insertCommas(Integer.toString(entry.getItemPrice()))+" GP", offset + 4);
					player.getPA().sendFrame126(entry.getUptime() + " " + (entry.getUptime() == 1 ? "Day" : "Days") + " - " + entry.getExpiryDate(), offset + 5);
					player.getPA().sendFrame126(entry.isBuying() ? "[B]" : "[S]", offset + 6);
					player.getPA().sendFrame171(isRemovalVisible(player, entry) ? 0 : 1, offset + 7);
					id++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(requiresUpdate)
			MarketIO.write();
		for(; id < 25; id++) {
			player.getPA().sendFrame171(1, 54100 + (id * 10));
		}
	}
	
	public static void clearSearchResults(Player player) {
		for(int i = 0; i < 25; i++)
			player.getPA().sendFrame171(1, 54100 + (i * 10));
			
	}
	
	private static boolean isRemovalVisible(Player player, MarketEntry entry) {
		if(entry == null|| player == null)
			return false;
		if(entry.getPlayerName().equalsIgnoreCase(player.playerName))
			return true;
		*//**
		 * The commented-out condition below when enabled would allow a certain type of player
		 * to view the removal button, and use it. 
		 *//*
		//if(player.isJuniorModerator() || player.isSeniorModerator() || player.isAdministrator() || player.isDeveloper())
			//return true;
		return false;
	}

}*/