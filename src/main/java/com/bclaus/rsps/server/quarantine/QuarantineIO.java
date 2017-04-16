package com.bclaus.rsps.server.quarantine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bclaus.rsps.server.util.FileUtils;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;

/**
 * 
 * @author Jason MacKeigan http://www.rune-server.org/members/jason
 * @date Jul 3, 2014, 11:47:04 PM
 */
public class QuarantineIO {
	/**
	 * Contains the list of entries that are read upon initialization, and the
	 * entries that are added during runtime.
	 */
	private static CopyOnWriteArrayList<QuarantineEntry> entries = new CopyOnWriteArrayList<QuarantineEntry>();
	
	/**
	 * The file that we write and read from to obtain information about entries
	 * 
	 */
	private static File quarantineFile = new File(FileUtils.getSanctionDirectory() + File.separator + "QuarantinedData.txt");

	/**
	 * Adds a new QuarantineEntry object to the list
	 * 
	 * @param name
	 *            The players name
	 * @param ip
	 *            The players internet protocol address
	 * @param identity
	 *            The identity of the player
	 * @param severity
	 *            The severity of the quarantine
	 */
	public static void add(String name, String identity, int severity) {
		if (contains(name) && getHighestSeverityLevel(name) < severity) {
			for (QuarantineEntry entry : entries) {
				if (entry == null)
					continue;
				if (entry.getName().equalsIgnoreCase(name)) {
					entry.setSeverity(severity);
					break;
				}
			}
			return;
		}
		entries.add(new QuarantineEntry(name, identity, severity));
	}

	/**
	 * Removes an existing QuarantineEntry object, or multiple existing
	 * QuarantineEntry objects from the list
	 * 
	 * @param name
	 *            The players name
	 * @param ip
	 *            The players internet protocol address
	 * @param identity
	 *            The identity of the player
	 */
	public static void remove(String name, String identity) {
		ArrayList<QuarantineEntry> tempEntries = new ArrayList<QuarantineEntry>();
		for (QuarantineEntry entry : entries) {
			if (entry == null)
				continue;
			if (entry.getName().equalsIgnoreCase(name))
				tempEntries.add(entry);
			else if (entry.getIdentity().equalsIgnoreCase(identity))
				tempEntries.add(entry);

		}
		if (entries.size() > 0)
			entries.removeAll(tempEntries);
	}

	/**
	 * Removes an existing QuarantineEntry object, or multiple existing
	 * QuarantineEntry objects from the list
	 * 
	 * @param name
	 *            The players name
	 * @param ip
	 *            The players internet protocol address
	 * @param identity
	 *            The identity of the player
	 */
	public static void remove(String name) {
		ArrayList<QuarantineEntry> tempEntries = new ArrayList<QuarantineEntry>();
		ArrayList<String> matches = new ArrayList<String>();
		for (QuarantineEntry entry : entries) {
			if (entry == null)
				continue;
			if (entry.getName().equalsIgnoreCase(name)) {
				matches.add(name);
				matches.add(entry.getIdentity());
				tempEntries.add(entry);
				continue;
			}
		}
		for (QuarantineEntry entry : entries) {
			if (entry == null)
				continue;
			if (matches == null)
				break;
			for (String match : matches) {
				if (match == null)
					continue;
				if (match.equalsIgnoreCase(entry.getName()))
					tempEntries.add(entry);
				else if (match.equalsIgnoreCase(entry.getIdentity()))
					tempEntries.add(entry);
			}
		}
		for (Player player : World.PLAYERS) {
			if (player == null)
				continue;
			if (!player.getQuarantine().isQuarantined())
				continue;
			for (String match : matches) {
				if (player.playerName.equalsIgnoreCase(match)  || player.getIdentity().equalsIgnoreCase(match)) {
					player.getQuarantine().setQuarantined(false);
					player.sendMessage("You are no longer in Quarantine.");
					break;
				}
			}
		}
		if (tempEntries.size() > 0)
			entries.removeAll(tempEntries);
	}

	/**
	 * Determins whether or not the data passed exists withing the list
	 * 
	 * @param name
	 *            The name of the player
	 * @param ip
	 *            The players internet protocol address
	 * @param identity
	 *            The severity of the quarantine
	 * @return
	 */
	public static boolean contains(String name, String identity) {
		for (QuarantineEntry entry : entries) {
			if (entry == null)
				continue;
			if (entry.getName().equalsIgnoreCase(name) || entry.getIdentity().equalsIgnoreCase(identity))
				return true;
			
		}
		return false;
	}

	/**
	 * Determins whether or not the data passed exists withing the list
	 * 
	 * @param name
	 *            The name of the player
	 * @return
	 */
	public static boolean contains(String name) {
		for (QuarantineEntry entry : entries) {
			if (entry == null)
				continue;
			if (entry.getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}

	/**
	 * Retrieves the severity of a specific quarantine
	 * 
	 * @param name
	 *            The name of the player
	 * @param ip
	 *            The players internet protocol address
	 * @param identity
	 *            The severity of the quarantine
	 * @return
	 */
	public static int getSeverityLevel(String name, String identity ) {
		for (QuarantineEntry entry : entries) {
			if (entry == null)
				continue;
			if (entry.getName().equalsIgnoreCase(name) && entry.getIdentity().equalsIgnoreCase(identity))
				return entry.getSeverity();
		}
		for (QuarantineEntry entry : entries) {
			if (entry == null)
				continue;
			if (entry.getName().equalsIgnoreCase(name) || entry.getIdentity().equalsIgnoreCase(identity))
				return entry.getSeverity();
		}
		return -1;
	}

	public static int getHighestSeverityLevel(String name) {
		int highestLevel = -1;
		for (QuarantineEntry entry : entries) {
			if (entry == null)
				continue;
			if (entry.getName().equalsIgnoreCase(name))
				if (entry.getSeverity() > highestLevel)
					;
			highestLevel = entry.getSeverity();
		}
		return highestLevel;
	}

	public static int getHighestSeverityLevel(String name, String identity) {
		int highestLevel = -1;
		for (QuarantineEntry entry : entries) {
			if (entry == null)
				continue;
			if (entry.getName().equalsIgnoreCase(name) || entry.getIdentity().equalsIgnoreCase(identity))
				if (entry.getSeverity() > highestLevel)
					;
			highestLevel = entry.getSeverity();
		}
		return highestLevel;
	}

	/**
	 * Writes specific information from every quarantine to a file stored within
	 * the servers directory
	 */
	public static void write() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(quarantineFile))) {
			for (QuarantineEntry entry : entries) {
				if (entry == null)
					continue;
				writer.write(entry.getName() + "\t\t" + entry.getIdentity() + "\t\t" + entry.getSeverity());
				writer.newLine();
			}
			writer.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Reads specific information about quarantines and stores them in a list
	 * for further use during runtime
	 */
	public static void read() {
		try (BufferedReader reader = new BufferedReader(new FileReader(quarantineFile))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split("\t\t");
				if (data.length < 2)
					continue;
				add(data[0], data[1], Integer.parseInt(data[2]));
			}
			reader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
