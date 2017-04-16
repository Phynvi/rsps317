package com.bclaus.rsps.server.vd.clan;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.PlayerSave;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.util.Misc;

public class InterfaceAction implements PacketType {

	@SuppressWarnings("null")
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int id = c.getInStream().readUnsignedWord();
		int action = c.getInStream().readUnsignedWord();
		c.sendMessage(id + ", " + action);
		switch (id) {
		case 18304:
			if (action == 1) {
				if (c.clan.getFounder().toLowerCase().equals(c.playerName.toLowerCase())) {
					c.clan.delete();
					c.getPA().setClanData();
				} else {
					c.sendMessage("You do not have sufficient privileges to do this.");
				}
			}
			break;
		case 18307:
		case 18310:
		case 18313:
		case 18316:
		case 18157:
			Clan clan = c.clan;
			if (clan != null) {
				if (clan.getFounder().toLowerCase().equals(c.playerName.toLowerCase())) {
					if (id == 18307) {
						clan.setRankCanJoin(action == 0 ? -1 : action);
					} else if (id == 18310) {
						clan.setRankCanTalk(action == 0 ? -1 : action);
					} else if (id == 18313) {
						clan.setRankCanKick(action == 0 ? -1 : action);
					} else if (id == 18316) {
						clan.setRankCanBan(action == 0 ? -1 : action);

					}
					String title = "";
					if (id == 18307) {
						title = clan.getRankTitle(clan.whoCanJoin) + (clan.whoCanJoin > Clan.Rank.ANYONE && clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
					} else if (id == 18310) {
						title = clan.getRankTitle(clan.whoCanTalk) + (clan.whoCanTalk > Clan.Rank.ANYONE && clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
					} else if (id == 18313) {
						title = clan.getRankTitle(clan.whoCanKick) + (clan.whoCanKick > Clan.Rank.ANYONE && clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
					} else if (id == 18316) {
						title = clan.getRankTitle(clan.whoCanBan) + (clan.whoCanBan > Clan.Rank.ANYONE && clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
					} else if (id == 18146) {
						title = clan.getRankTitle(clan.whoCanBan) + (clan.whoCanBan > Clan.Rank.ANYONE && clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
					}
					c.getPA().sendFrame126(title, id + 2);
				}
			}
			break;

		default:
			break;
		}
		if (id >= 18323 && id < 18423) {
			Clan clan = c.getPA().getClan();
			if (clan != null && clan.rankedMembers != null && !clan.rankedMembers.isEmpty()) {
				if (clan.getFounder().toLowerCase().equals(c.playerName.toLowerCase())) {
					String member = clan.rankedMembers.get(id - 18323);
					switch (action) {
					case 0:
						clan.demote(member);
						break;
					default:
						clan.setRank(member, action);
						break;
					}
					c.getPA().setClanData();
				}
			}
		}
		if (id >= 18424 && id < 18524) {
			Clan clan = c.getPA().getClan();
			if (clan != null && clan.bannedMembers != null && !clan.bannedMembers.isEmpty()) {
				String member = clan.bannedMembers.get(id - 18424);
				switch (action) {
				case 0:
					clan.unbanMember(member);
					break;
				}
				c.getPA().setClanData();
			}
		}
		if (id >= 18144 && id < 18244) {
			for (int index = 0; index < 100; index++) {
				if (id == index + 18144) {
					String member = c.clan.activeMembers.get(id - 18144);
					switch (action) {
					case 0:
						if (c.clan.isFounder(c.playerName)) {
							c.getPA().showInterface(18300);
						}
						break;
					case 1:
						if (member.equalsIgnoreCase(c.playerName)) {
							c.sendMessage("You can't kick yourself!");
						} else {
							if (c.clan.canKick(c.playerName)) {
								c.clan.kickMember(member);
							} else {
								c.sendMessage("You do not have sufficient privileges to do this.");
							}
						}
						break;
					case 2:
						if (member.length() == 0) {
							break;
						} else if (member.length() > 12) {
							member = member.substring(0, 12);
						}
						if (member.equalsIgnoreCase(c.playerName)) {
							break;
						}
						if (!PlayerSave.playerExists(member)) {
							c.sendMessage("This c doesn't exist!");
							break;
						}
						Clan clan = c.getPA().getClan();
						if(clan != null) {
							continue;
						}
						if (clan.isRanked(member)) {
							c.sendMessage("You cannot ban a ranked member.");
							break;
						}
						if (clan != null) {
							if (clan.canBan(c.playerName)) {
								clan.banMember(Misc.formatPlayerName(member));
								c.getPA().setClanData();
								clan.save();
							} else {
								c.sendMessage("You do not have sufficient privileges to do this.");
							}
						}
						break; // ok done one more thing
					}
					break;
				}
			}
		}
	}
}