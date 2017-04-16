package com.bclaus.rsps.server.vd.clan;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.PlayerSave;
import com.bclaus.rsps.server.vd.player.packets.PacketType;

public class ReceiveString implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		String text = c.getInStream().readString();
		int index = text.indexOf(",");
		int id = Integer.parseInt(text.substring(0, index));
		String string = text.substring(index + 1);
		switch (id) {
		case 0:
			if (c.clan != null) {
				c.clan.removeMember(c);
				c.lastClanChat = "";
			}
			break;
		case 1:
			if (string.length() == 0) {
				break;
			} else if (string.length() > 15) {
				string = string.substring(0, 15);
			}
			Clan clan = c.clan;
			if (clan == null) {
				Server.clanManager.create(c);
				clan = c.clan;
			}
			if (clan != null) {
				if (clan.getFounder().toLowerCase().equals(c.playerName.toLowerCase())) {
					clan.setTitle(string);
					c.getPA().sendFrame126(clan.getTitle(), 18306);
					clan.save();
				}
			}
			break;
		case 2:
			if (string.length() == 0) {
				break;
			} else if (string.length() > 12) {
				string = string.substring(0, 12);
			}
			if (string.equalsIgnoreCase(c.playerName)) {
				break;
			}
			if (!PlayerSave.playerExists(string)) {
				c.sendMessage("This player doesn't exist!");
				break;
			}
			clan = c.clan;
			if (!clan.getFounder().toLowerCase().equals(c.playerName.toLowerCase())) {
				c.sendMessage("You do not have sufficient privileges to do this.");
				break;
			}
			if (clan.isBanned(string)) {
				c.sendMessage("You cannot promote a banned member.");
				break;
			}
			if (clan != null) {
				clan.setRank(Misc.formatPlayerName(string), 1);
				c.getPA().setClanData();
				clan.save();
			}
			break;
		case 3:
			if (string.length() == 0) {
				break;
			} else if (string.length() > 12) {
				string = string.substring(0, 12);
			}
			if (string.equalsIgnoreCase(c.playerName)) {
				break;
			}
			if (!PlayerSave.playerExists(string)) {
				c.sendMessage("This player doesn't exist!");
				break;
			}
			clan = c.clan;
			if (!clan.getFounder().toLowerCase().equals(c.playerName.toLowerCase())) {
				c.sendMessage("You do not have sufficient privileges to do this.");
				break;
			}
			if (clan.isRanked(string)) {
				c.sendMessage("You cannot ban a ranked member.");
				break;
			}
			if (clan != null) {
				clan.banMember(Misc.formatPlayerName(string));
				c.getPA().setClanData();
				clan.save();
			}
			break;
		case 4: // Referal

			break;
		case 5:

			break;
		default:
			break;
		}
	}

}