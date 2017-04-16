package com.bclaus.rsps.server.vd.content.market;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.items.Item;


public class MarketIO {
	
	static HashMap<Integer, MarketEntry[]> entries = new HashMap<Integer, MarketEntry[]>();
	
	static final String FOLDER_PATH = "./Data/market/";
	
	public static void read() {
		File[] folder = new File(FOLDER_PATH + "items/").listFiles();
		for(File itemFile : folder) {
			try (BufferedReader reader = new BufferedReader(new FileReader(itemFile))) {
				String line;
				String[] values;
				while((line = reader.readLine()) != null) {
					if(line.startsWith("//")) continue;
					MarketEntry temp = new MarketEntry();
					values = line.split("\t");
					if(values.length == 7) {
						temp.setPlayerName(values[0]);
						temp.setItemId(Integer.parseInt(values[1]));
						temp.setPlayerInternetProtocol(values[2]);
						temp.setItemAmount(Integer.parseInt(values[3]));
						temp.setItemPrice(Integer.parseInt(values[4]));
						temp.setDate(values[5]);
						temp.setUptime(Integer.parseInt(values[6]));
						temp.setItemName(itemFile.getName());
						addEntry(temp);
					}
				}
				reader.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	public static void write() {
		ArrayList<String> names = new ArrayList<String>();
		for(int itemId = 0; itemId < Constants.ITEM_LIMIT; itemId++) {
			if(entries.containsKey(itemId)) {
				names.clear();
				File itemFile = new File(FOLDER_PATH + "items/"+ Item.getItemName(itemId));
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(itemFile))) {
					writer.write("//Name\tItemId\tIP\tItem Amount\tItem Price\tExpiry Date\tTotal Uptime");
					writer.newLine();
					for(MarketEntry entry : getEntries(itemId)) {
						if(entry != null) {
							if(names.contains(entry.getPlayerName())) {
								removeEntry(entry);
								continue;
							}
							names.add(entry.getPlayerName());
							writer.write(entry.getPlayerName() + "\t" + entry.getItemId() + "\t" + entry.getPlayerInternetProtocol()+ "\t"
									+ entry.getItemAmount() + "\t" + entry.getItemPrice()+ "\t" + entry.getExpiryDate()+ "\t" + entry.getUptime());
							writer.newLine();
						}
					}
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void addEntry(MarketEntry entry) {
		if(!entries.containsKey(entry.getItemId())) {
			MarketEntry[] newEntry = new MarketEntry[25];
			newEntry[0] = entry;
			entries.put(entry.getItemId(), newEntry);
		} else {
			MarketEntry[] oldEntries = getEntries(entry.getItemId());
			for(MarketEntry e : oldEntries)
				if(e != null && e.getPlayerName().equalsIgnoreCase(entry.getPlayerName()))
					return;
			for(int i = 0; i < oldEntries.length; i++) {
				if(oldEntries[i] == null) {
					oldEntries[i] = entry;
					break;
				}
			}
		}
	}
	
	public static boolean entryContains(int itemId, String name) {
		MarketEntry[] oldEntries = getEntries(itemId);
		if(oldEntries == null)
			return false;
		for(MarketEntry entry : oldEntries)
			if(entry != null && entry.getPlayerName() != null && entry.getPlayerName().equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	public static int getMarketSize(int itemId) {
		MarketEntry[] entries = getEntries(itemId);
		if(entries == null)
			return 0;
		int total = 0;
		for(MarketEntry e : entries)
			if(e != null)
				total++;
		return total;
	}
	
	public static void removeEntry(MarketEntry entry) {
		MarketEntry[] oldEntries = getEntries(entry.getItemId());
		if(oldEntries == null)
			return;
		for(int i = 0; i < oldEntries.length; i++) {
			if(oldEntries[i] != null) {
				if(oldEntries[i].getPlayerName().equalsIgnoreCase(entry.getPlayerName())) {
					oldEntries[i] = null;
					break;
				}
			}
		}
	}
	
	public static MarketEntry[] getEntries(int itemId) {
		for(Entry<Integer, MarketEntry[]> singleEntry : entries.entrySet())
			if(singleEntry.getKey() == itemId)
				return singleEntry.getValue();
		return null;
	}

}