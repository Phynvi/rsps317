package com.bclaus.rsps.server.vd.player.packets.impl;
/*
 * package server.game.player.packets.impl;
 * 
 * import Server; import server.game.clan.Clan; import
 * server.game.player.Client; import server.game.player.PlayerSave; import
 * server.game.player.packets.PacketType; import Misc;
 * 
 * 
 * public class ReceiveString implements PacketType {
 * 
 * public void processPacket(Client c, int packetType, int packetSize) { String
 * text = c.getInStream().readString(); int index = text.indexOf(","); int id =
 * Integer.parseInt(text.substring(0, index)); String string =
 * text.substring(index + 1); switch (id) { case 0: if (c.clan != null) {
 * c.clan.removeMember(c); c.lastClanChat = ""; } break; case 1: if
 * (string.length() == 0) { break; } else if (string.length() > 15) { string =
 * string.substring(0, 15); } Clan clan = c.clan; if (clan == null) {
 * Server.clanManager.create(c); clan = c.clan; } if (clan != null) { if
 * (clan.getFounder().toLowerCase().equals(c.playerName.toLowerCase())) {
 * clan.setTitle(string); c.getPA().sendFrame126(clan.getTitle(), 18306);
 * clan.save(); } } break; case 2: if (string.length() == 0) { break; } else if
 * (string.length() > 12) { string = string.substring(0, 12); } if
 * (string.equalsIgnoreCase(c.playerName)) { break; } if
 * (!PlayerSave.playerExists(string)) {
 * c.sendMessage("This player doesn't exist!"); break; } clan = c.clan; if
 * (!clan.getFounder().toLowerCase().equals(c.playerName.toLowerCase())) {
 * c.sendMessage("You do not have sufficient privileges to do this."); break; }
 * if (clan.isBanned(string)) {
 * c.sendMessage("You cannot promote a banned member."); break; } if (clan !=
 * null) { clan.setRank(Misc.formatPlayerName(string), 1);
 * c.getPA().setClanData(); clan.save(); } break; case 3: if (string.length() ==
 * 0) { break; } else if (string.length() > 12) { string = string.substring(0,
 * 12); } if (string.equalsIgnoreCase(c.playerName)) { break; } if
 * (!PlayerSave.playerExists(string)) {
 * c.sendMessage("This player doesn't exist!"); break; } clan = c.clan; if
 * (!clan.getFounder().toLowerCase().equals(c.playerName.toLowerCase())) {
 * c.sendMessage("You do not have sufficient privileges to do this."); break; }
 * if (clan.isRanked(string)) {
 * c.sendMessage("You cannot ban a ranked member."); break; } if (clan != null)
 * { clan.banMember(Misc.formatPlayerName(string)); c.getPA().setClanData();
 * clan.save(); } break; case 4: // Referal
 * 
 * break; case 5:
 * 
 * break; default: System.out.println("Received string: identifier=" + id +
 * ", string=" + string); break; } }
 * 
 * }
 */