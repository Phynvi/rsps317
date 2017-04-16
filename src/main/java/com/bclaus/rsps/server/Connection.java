package com.bclaus.rsps.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.bclaus.rsps.server.util.FileUtils;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * Connection Check Class
 * 
 * @author Ryan / Lmctruck30
 * 
 */

public class Connection {

	public static Collection<String> bannedIps = new ArrayList<String>();
	public static Collection<String> bannedNames = new ArrayList<String>();
	public static Collection<String> mutedIps = new ArrayList<String>();
	public static Collection<String> mutedNames = new ArrayList<String>();
	public static Collection<String> starterRecieved1 = new ArrayList<String>();
	public static Collection<String> starterRecieved2 = new ArrayList<String>();
	public static Collection<String> loginLimitExceeded = new ArrayList<String>();
	public static Collection<String> bannedIds = new ArrayList<String>();
	public static Collection<String> marketBans = new ArrayList<String>();
	public static Collection<String[]> connectedAddresses = new ArrayList<String[]>();

	private static final File IDENTITY_BANS = new File(FileUtils.getSanctionDirectory(), "identitybanned.txt");
	private static final File USERS_BANNED = new File(FileUtils.getSanctionDirectory(), "usersbanned.txt");
	private static final File USERS_MUTED = new File(FileUtils.getSanctionDirectory(), "usersmuted.txt");
	private static final File MARKET_BANS = new File(FileUtils.getSanctionDirectory(), "marketbans.txt");
	private static final File IP_MUTES = new File(FileUtils.getSanctionDirectory(), "ipsmuted.txt");
	private static final File IP_BANS = new File(FileUtils.getSanctionDirectory(), "ipsbanned.txt");
	private static final File HOST_BANS = new File(FileUtils.getSanctionDirectory(), "bannedhostlist.txt");

	/**
	 * Adds the banned usernames and ips from the text file to the ban list
	 **/
	public static void initialize() {
		try {
			if (!IDENTITY_BANS.exists()) {
				IDENTITY_BANS.createNewFile();
			}
			if (!USERS_BANNED.exists()) {
				USERS_BANNED.createNewFile();
			}
			if (!USERS_MUTED.exists()) {
				USERS_MUTED.createNewFile();
			}
			if (!MARKET_BANS.exists()) {
				MARKET_BANS.createNewFile();
			}
			if (!IP_MUTES.exists()) {
				IP_MUTES.createNewFile();
			}
			if (!IP_BANS.exists()) {
				IP_BANS.createNewFile();
			}
			if (!HOST_BANS.exists()) {
				HOST_BANS.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		banUsers();
		banIps();
		appendStarters();
		appendStarters2();
		muteUsers();
		muteIps();
		bannedIds();
		loadMarketBans();
	}

	public static void unMuteUser(String name) {
		mutedNames.remove(name);
		deleteFromFile(USERS_MUTED, name);
	}

	public static void unIPMuteUser(String name) {
		mutedIps.remove(name);
		deleteFromFile(IP_MUTES, name);
	}

	public static void removeMarketBan(String name) {
		mutedNames.remove(name);
		deleteFromFile(MARKET_BANS, name);
	}

	/**
	 * Adding Ban IP
	 **/
	public static void addIpToBanList(String IP) {
		bannedIps.add(IP);
	}

	public static void addIdentityToList(String IP) {
		bannedIds.add(IP);
	}

	public static void addIpToMuteList(String IP) {
		mutedIps.add(IP);
		addIpToMuteFile(IP);
	}

	public static boolean isIdBanned(String id) {
		return bannedIds.contains(id);
	}

	/**
	 * Contains Ban IP
	 **/
	public static boolean isIpBanned(String IP) {
		return bannedIps.contains(IP);
	}

	public static boolean isMarketBanned(String name) {
		return marketBans.contains(name);
	}

	public static void removeIdentityBan(String identity) {
		bannedIds.remove(identity);
		deleteFromFile(IDENTITY_BANS, identity);
	}

	/**
	 * Removing Ban IP
	 **/
	public static void removeIpFromBanList(String IP) {
		bannedIps.remove(IP);
	}

	/**
	 * Adding banned username
	 **/
	public static void addNameToBanList(String name) {
		bannedNames.add(name.toLowerCase());
	}

	public static void addNametoMarketList(String name) {
		marketBans.add(name.toLowerCase());
	}

	public static void addNameToIdBanList(String name) {
		bannedIds.add(name.toLowerCase());
	}

	public static void addNameToMuteList(String name) {
		mutedNames.add(name.toLowerCase());
		addUserToFile(name);
	}

	public static void addMarketBan(String name) {
		marketBans.add(name.toLowerCase());
		addUserToFile(name);
	}

	/**
	 * Removing banned username
	 **/
	public static void removeNameFromBanList(String name) {
		bannedNames.remove(name.toLowerCase());
		deleteFromFile(USERS_BANNED, name);
	}

	public static void deleteFromFile(File file, String name) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(file));
			ArrayList<String> contents = new ArrayList<String>();
			while (true) {
				String line = r.readLine();
				if (line == null) {
					break;
				} else {
					line = line.trim();
				}
				if (!line.equalsIgnoreCase(name)) {
					contents.add(line);
				}
			}
			r.close();
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			for (String line : contents) {
				w.write(line, 0, line.length());
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
	}

	/**
	 * Contains banned username
	 **/
	public static boolean isNamedBanned(String name) {
		return bannedNames.contains(name.toLowerCase());
	}

	public static void bannedIds() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(IDENTITY_BANS));
			String data;
			try {
				while ((data = in.readLine()) != null) {
					Connection.addIdentityToList(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads all usernames from text file then adds them all to the ban list
	 **/
	public static void banUsers() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(USERS_BANNED));
			String data;
			try {
				while ((data = in.readLine()) != null) {
					addNameToBanList(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void muteUsers() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(USERS_MUTED));
			String data;
			try {
				while ((data = in.readLine()) != null) {
					mutedNames.add(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads all the Ips from text file then adds them all to ban list
	 **/
	public static void banIps() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(IP_BANS));
			String data;
			try {
				while ((data = in.readLine()) != null) {
					addIpToBanList(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void muteIps() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(IP_MUTES));
			String data;
			try {
				while ((data = in.readLine()) != null) {
					mutedIps.add(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final void loadMarketBans() {
		try {
			final File banDirectory = new File(FileUtils.getMarketDirectory(), "/bans/");
			if (!banDirectory.exists()) {
				banDirectory.mkdir();
			}
			final File marketFile = new File(banDirectory.getPath(), "marketbans.txt");
			try {
				if (!marketFile.exists()) {
					marketFile.createNewFile();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			BufferedReader in = new BufferedReader(new FileReader(marketFile));
			String data;
			try {
				while ((data = in.readLine()) != null) {
					marketBans.add(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void appendStarters() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("./data/starters/firststarterrecieved.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					starterRecieved1.add(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void appendStarters2() {
		try {
			BufferedReader in = new BufferedReader(new FileReader("./data/starters/secondstarterrecieved.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					starterRecieved2.add(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addIpToStarter1(String IP) {
		starterRecieved1.add(IP);
		addIpToStarterList1(IP);
	}

	public static void addIpToStarter2(String IP) {
		starterRecieved2.add(IP);
		addIpToStarterList2(IP);
	}

	public static void addIpToStarterList1(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("./data/starters/firststarterrecieved.txt", true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addIpToStarterList2(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("./data/starters/secondstarterrecieved.txt", true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean hasRecieved1stStarter(String IP) {
		if (starterRecieved1.contains(IP)) {
			return true;
		}
		return false;
	}

	public static boolean hasRecieved2ndStarter(String IP) {
		if (starterRecieved2.contains(IP)) {
			return true;
		}
		return false;
	}

	/**
	 * Writes the username into the text file - when using the ::ban playername
	 * command
	 **/
	public static void addNameToFile(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(USERS_BANNED, true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addNameToMarketFile(String Name) {
		try {
			final File banDirectory = new File(FileUtils.getMarketDirectory(), "/bans/");
			if (!banDirectory.exists()) {
				banDirectory.mkdir();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(banDirectory, "usersbanned.txt"), true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addUserToFile(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(USERS_MUTED, true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addIdentityToFile(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(IDENTITY_BANS, true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the IP into the text file - use ::ipban username
	 * 
	 * @param Name
	 *            Name to add
	 */

	public static void addHostToFile(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(HOST_BANS, true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addIpToFile(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(IP_BANS, true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addIpToMuteFile(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(IP_MUTES, true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isMuted(Player c) {
		boolean b = mutedNames.contains(c.playerName.toLowerCase()) || mutedIps.contains(c.connectedFrom) || c.muted;
		return b;
	}

}