package com.bclaus.rsps.server.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 
 * @author Jason MacKeigan http://www.rune-server.org/members/jason
 * @date Jul 17, 2014, 8:39:34 AM
 */
public class DisplayName {

	public String display;
	public long time;

	private DisplayName(String display, long time) {
		this.display = display;
		this.time = time;
	}

	public static void init() {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split("\t");
				if (data == null || data.length < 3)
					continue;
				try {
					names.put(data[0], new DisplayName(data[1], Long.parseLong(data[2])));
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
					continue;
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (Entry<String, DisplayName> entry : names.entrySet()) {
				if (entry != null) {
					DisplayName d = entry.getValue();
					writer.write(entry.getKey() + "\t" + d.display + "\t" + d.time);
					writer.newLine();
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void add(String name, String display) {
		names.put(name, new DisplayName(display, System.currentTimeMillis()));
	}

	public static void remove(String name) {
		if (!names.containsKey(name))
			return;
		names.remove(name);
	}

	public static String getDisplayName(String name) {
		if (names.get(name) == null)
			return null;
		return names.get(name).display;
	}

	public static DisplayName getDisplay(String name) {
		if (names.get(name) == null)
			return null;
		return names.get(name);
	}

	public static boolean exists(String display) {
		File[] files = new File("./Data/characters/").listFiles();
		for (File file : files) {
			if (file == null)
				continue;
			if (file.getName().replaceAll(".txt", "").equalsIgnoreCase(display))
				return true;
		}
		for (Entry<String, DisplayName> entry : names.entrySet()) {
			if (entry == null)
				continue;
			DisplayName d = entry.getValue();
			if (entry.getKey().equalsIgnoreCase(display))
				return true;
			if (d.display.equalsIgnoreCase(display))
				return true;
		}
		return false;
	}

	public static boolean displayExists(String display) {
		for (Entry<String, DisplayName> entry : names.entrySet()) {
			if (entry == null)
				continue;
			DisplayName d = entry.getValue();
			if (d.display.equalsIgnoreCase(display))
				return true;
		}
		return false;
	}

	static File file;

	static HashMap<String, DisplayName> names = new HashMap<>();

	static {
		file = new File("./Data/data/displaynames.txt");
		try {
			if (!file.exists())
				file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
