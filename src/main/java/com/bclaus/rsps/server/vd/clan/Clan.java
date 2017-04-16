package com.bclaus.rsps.server.vd.clan;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import com.bclaus.rsps.server.vd.World;

/**
 * This class stores all information about the clan. This includes active
 * members, banned members, ranked members and their ranks, clan title, and clan
 * founder. All clan joining, leaving, and moderation/setup is also handled in
 * this class.
 * 
 * @author Galkon
 * 
 */
public class Clan {

	/**
	 * Adds a member to the clan.
	 * 
	 * @param player
	 */
	public void addMember(Player player) {
		if (activeMembers.contains(player.playerName)) {
			player.sendMessage(player.playerName + " is already in this clan chat!");
			return;
		}
		if (isBanned(player.playerName)) {
			player.sendMessage("<col=FF0000>You are currently banned from this clan chat.</col>");
			return;
		}
		if (whoCanJoin > Rank.ANYONE && !isFounder(player.playerName)) {
			if (getRank(player.playerName) < whoCanJoin) {
				player.sendMessage("Only " + getRankTitle(whoCanJoin) + "s+ may join this chat.");
				return;
			}
		}
		player.clan = this;
		player.lastClanChat = getFounder();
		activeMembers.add(player.playerName);
		player.getPA().sendFrame126("Leave chat", 18135);
		player.getPA().sendFrame126("Talking in: <col=FFFF64>" + getTitle() + "</col>", 18139);
		player.getPA().sendFrame126("Owner: <col=FFFFFF>" + Misc.formatPlayerName(getFounder()) + "</col>", 18140);
		player.sendMessage("Now talking in clan chat <col=FFFF64><shad=0>" + getTitle() + "</shad></col>.");
		player.sendMessage("To talk, start each line of chat with the / symbol.");
		updateMembers();
	}

	/**
	 * Removes the player from the clan.
	 * 
	 * @param player
	 */
	public void removeMember(Player player) {
		if (!activeMembers.contains(player.playerName)) {
			player.sendMessage(player.playerName + " is not in this clan chat!");
			return;
		}
		Iterator<String> $it = activeMembers.iterator();
		while ($it.hasNext()) {
			String name = $it.next();
			if (name.equalsIgnoreCase(player.playerName)) {
				player.clan = null;
				resetInterface(player);
				$it.remove();
			}
		}
		updateMembers();
	}

	/**
	 * Removes the player from the clan.
	 * 
	 * @param player
	 */
	public void removeMember(String name) {
		removeMember(PlayerUpdating.getPlayerByName(name));
	}

	/**
	 * Updates the members on the interface for the player.
	 * 
	 * @param player
	 */
	public void updateInterface(Player player) {
		player.getPA().sendFrame126("Talking in: <col=FFFF64>" + getTitle() + "</col>", 18139);
		player.getPA().sendFrame126("Owner: <col=FFFFFF>" + Misc.formatPlayerName(getFounder()) + "</col>", 18140);
		Collections.sort(activeMembers);
		for (int index = 0; index < 100; index++) {
			if (index < activeMembers.size()) {
				player.getPA().sendFrame126("<clan=" + getRank(activeMembers.get(index)) + ">" + Misc.formatPlayerName(activeMembers.get(index)), 18144 + index);
			} else {
				player.getPA().sendFrame126("", 18144 + index);
			}
		}
	}

	/**
	 * Updates the interface for all members.
	 */
	public void updateMembers() {
		for (Player clanmate : World.PLAYERS) {
			if (clanmate == null) {
				continue;
			}
			if (activeMembers != null) {
				if (activeMembers.contains(clanmate.playerName))
					updateInterface(clanmate);
			}
		}
	}

	/**
	 * Resets the clan interface.
	 * 
	 * @param player
	 */
	public void resetInterface(Player player) {
		player.getPA().sendFrame126("Join chat", 18135);
		player.getPA().sendFrame126("Talking in: Not in chat", 18139);
		player.getPA().sendFrame126("Owner: None", 18140);
		for (int index = 0; index < 100; index++) {
			player.getPA().sendFrame126("", 18144 + index);
		}
	}

	/**
	 * Sends a message to the clan.
	 * 
	 * @param player
	 * @param message
	 */
	public void sendChat(Player player, String message) {
		String[] sensor = { "nigger", "paki" };
		if (getRank(player.playerName) < whoCanTalk) {
			player.sendMessage("Only " + getRankTitle(whoCanTalk) + "s+ may talk in this chat.");
			return;
		}
		if (player.getQuarantine().isQuarantined()) {
			player.sendMessage("You cannot use the Clan-chat while quarantined");
			return;
		}
		for (Player clanmate : World.PLAYERS) {
			if (clanmate == null) {
				continue;
			}
			for (String split : message.split(" ")) {
				for (String censored : sensor) {
					if (censored.equalsIgnoreCase(split) || (censored + "s").equalsIgnoreCase(split)) {
						String replace = "";
						for (int i = 0; i < censored.length(); i++)
							replace = replace + "*";
						message = message.replaceAll(censored, replace);
					}
				}
			}
			if (activeMembers.contains(clanmate.playerName))
				clanmate.sendClan(player.getName(), message, getTitle(), World.PLAYERS.get(player.getIndex()).playerRights);
		}
	}

	/**
	 * Sends a message to the clan.
	 * 
	 * @param player
	 * @param message
	 */
	public void sendMessage(String message) {
		for (Player clanmate : World.PLAYERS) {
			if (clanmate == null) {
				continue;
			}
			if (clanmate.getQuarantine().isQuarantined()) {
				clanmate.sendMessage("You are quarantined making you unable to do this action.");
				return;
			}
			if (activeMembers.contains(clanmate.playerName)) {
				clanmate.sendMessage(message);
			}
		}
	}

	/**
	 * Sets the rank for the specified name.
	 * 
	 * @param name
	 * @param rank
	 */
	public void setRank(String name, int rank) {
		if (rankedMembers.contains(name)) {
			ranks.set(rankedMembers.indexOf(name), rank);
		} else {
			rankedMembers.add(name);
			ranks.add(rank);
		}
		save();
	}

	/**
	 * Demotes the specified name.
	 * 
	 * @param name
	 */
	public void demote(String name) {
		if (!rankedMembers.contains(name)) {
			return;
		}
		int index = rankedMembers.indexOf(name);
		rankedMembers.remove(index);
		ranks.remove(index);
		save();
	}

	/**
	 * Gets the rank of the specified name.
	 * 
	 * @param name
	 * @return
	 */
	public int getRank(String name) {
		name = Misc.formatPlayerName(name);
		if (rankedMembers.contains(name)) {
			return ranks.get(rankedMembers.indexOf(name));
		}
		if (isFounder(name)) {
			return Rank.OWNER;
		}
		if (isFriend(getFounder(), name)) {
			return Rank.FRIEND;
		}
		return -1;
	}

	/**
	 * Tells use whether or not the specified name has the friend added.
	 * 
	 * @param name
	 * @param friend
	 * @return
	 */
	public static boolean isFriend(String name, String friend) {
		long nameLong = Misc.playerNameToInt64(friend);
		Player player = PlayerUpdating.getPlayerByName(name);
		if (player == null)
			return false;
		long[] friends = player.friends;// getFriends(name);
		if (friends != null && friends.length > 0) {
			for (int index = 0; index < friends.length; index++) {
				if (friends[index] == nameLong) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Can they kick?
	 * 
	 * @param name
	 * @return
	 */
	public boolean canKick(String name) {
		if (isFounder(name)) {
			return true;
		}
		if (getRank(name) >= whoCanKick) {
			return true;
		}
		return false;
	}

	/**
	 * Can they ban?
	 * 
	 * @param name
	 * @return
	 */
	public boolean canBan(String name) {
		if (isFounder(name)) {
			return true;
		}
		if (getRank(name) >= whoCanBan) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the specified name is the founder.
	 * 
	 * @param name
	 * @return
	 */
	public boolean isFounder(String name) {
		if (getFounder().equalsIgnoreCase(name)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the specified name is a ranked user.
	 * 
	 * @param name
	 * @return
	 */
	public boolean isRanked(String name) {
		name = Misc.formatPlayerName(name);
		if (rankedMembers.contains(name)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the specified name is banned.
	 * 
	 * @param name
	 * @return
	 */
	public boolean isBanned(String name) {
		name = Misc.formatPlayerName(name);
		if (bannedMembers.contains(name)) {
			return true;
		}
		return false;
	}

	/**
	 * Mutes the name in the Clanchat
	 * 
	 * @Founder Tim
	 * @Link Commands if (c.clan.getFounder().equalsIgnoreCase("tim") &&
	 *       c.isClanMuted) {
	 *       c.sendMessage("Sorry, your account is muted in this clanchat");
	 *       return; }
	 * 
	 * @param name
	 */
	public void kickMember(String name) {
		if (!activeMembers.contains(name)) {
			return;
		}
		if (name.equalsIgnoreCase(getFounder())) {
			return;
		}
		Player player = PlayerUpdating.getPlayerByName(name);
		if (player != null) {
			if (player.isClanMuted) {
				player.isClanMuted = false;
				player.sendMessage("You have being unmuted from the clanchat");
			} else {
				player.isClanMuted = true;
				player.sendMessage("You have been  muted from the clan chat.");
			}
		}
		if (!player.isClanMuted)
			sendMessage(Misc.formatPlayerName(name) + " has been muted from the clan chat.");
		else
			sendMessage(Misc.formatPlayerName(name) + " has been unmuted from the clan chat.");

	}

	/**
	 * Bans the name from entering the clan chat.
	 * 
	 * @param name
	 */
	public void banMember(String name) {
		name = Misc.formatPlayerName(name);
		if (bannedMembers.contains(name)) {
			return;
		}
		if (name.equalsIgnoreCase(getFounder())) {
			return;
		}
		if (isRanked(name)) {
			return;
		}
		removeMember(name);
		bannedMembers.add(name);
		save();
		Player player = PlayerUpdating.getPlayerByName(name);
		if (player != null && player.clan == this) {
			player.sendMessage("You have been banned from the clan chat.");
		}
		sendMessage(Misc.formatPlayerName(name) + " has been banned from the clan chat.");
	}

	/**
	 * Unbans the name from the clan chat.
	 * 
	 * @param name
	 */
	public void unbanMember(String name) {
		name = Misc.formatPlayerName(name);
		if (bannedMembers.contains(name)) {
			bannedMembers.remove(name);
			save();
		}
	}

	/**
	 * Saves the clan.
	 */
	public void save() {
		Server.clanManager.save(this);
		updateMembers();
	}

	/**
	 * Deletes the clan.
	 */
	public void delete() {
		for (String name : activeMembers) {
			removeMember(name);
		}
		for (int index = 0; index < activeMembers.size(); index++) {
			Player player = PlayerUpdating.getPlayerByName(activeMembers.get(index));
			if (player != null) {
				removeMember(player);
				player.sendMessage("The clan you were in has been deleted.");
			}
		}
		Server.clanManager.delete(this);
	}

	/**
	 * Creates a new clan for the specified player.
	 * 
	 * @param player
	 */
	public Clan(Player player) {
		setTitle(player.playerName + "'s Clan");
		setFounder(player.playerName.toLowerCase());
	}

	/**
	 * Creates a new clan for the specified title and founder.
	 * 
	 * @param title
	 * @param founder
	 */
	public Clan(String title, String founder) {
		setTitle(title);
		setFounder(founder);
	}

	/**
	 * Gets the founder of the clan.
	 * 
	 * @return
	 */
	public String getFounder() {
		return founder;
	}

	/**
	 * Sets the founder.
	 * 
	 * @param founder
	 */
	public void setFounder(String founder) {
		this.founder = founder;
	}

	/**
	 * Gets the title of the clan.
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 * @return
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * The title of the clan.
	 */
	public String title;

	/**
	 * The founder of the clan.
	 */
	public String founder;

	/**
	 * The active clan members.
	 */
	public LinkedList<String> activeMembers = new LinkedList<String>();

	/**
	 * The banned members.
	 */
	public LinkedList<String> bannedMembers = new LinkedList<String>();

	/**
	 * The ranked clan members.
	 */
	public LinkedList<String> rankedMembers = new LinkedList<String>();

	/**
	 * The clan member ranks.
	 */
	public LinkedList<Integer> ranks = new LinkedList<Integer>();

	/**
	 * The clan ranks.
	 * 
	 * @author Galkon
	 * 
	 */
	public static class Rank {
		public final static int ANYONE = -1;
		public final static int FRIEND = 0;
		public final static int RECRUIT = 1;
		public final static int CORPORAL = 2;
		public final static int SERGEANT = 3;
		public final static int LIEUTENANT = 4;
		public final static int CAPTAIN = 5;
		public final static int GENERAL = 6;
		public final static int OWNER = 7;
	}

	/**
	 * Gets the rank title as a string.
	 * 
	 * @param rank
	 * @return
	 */
	public String getRankTitle(int rank) {
		switch (rank) {
		case -1:
			return "Anyone";
		case 0:
			return "Friend";
		case 1:
			return "Recruit";
		case 2:
			return "Corporal";
		case 3:
			return "Sergeant";
		case 4:
			return "Lieutenant";
		case 5:
			return "Captain";
		case 6:
			return "General";
		case 7:
			return "Only Me";
		}
		return "";
	}

	/**
	 * Sets the minimum rank that can join.
	 * 
	 * @param rank
	 */
	public void setRankCanJoin(int rank) {
		whoCanJoin = rank;
	}

	/**
	 * Sets the minimum rank that can talk.
	 * 
	 * @param rank
	 */
	public void setRankCanTalk(int rank) {
		whoCanTalk = rank;
	}

	/**
	 * Sets the minimum rank that can kick.
	 * 
	 * @param rank
	 */
	public void setRankCanKick(int rank) {
		whoCanKick = rank;
	}

	/**
	 * Sets the minimum rank that can ban.
	 * 
	 * @param rank
	 */
	public void setRankCanBan(int rank) {
		whoCanBan = rank;
	}

	/**
	 * The ranks privileges require (joining, talking, kicking, banning).
	 */
	public int whoCanJoin = Rank.ANYONE;
	public int whoCanTalk = Rank.ANYONE;
	public int whoCanKick = Rank.GENERAL;
	public int whoCanBan = Rank.OWNER;

}