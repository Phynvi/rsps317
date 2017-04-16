package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.Connection;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.content.achievements.AchievementType;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.packets.PacketType;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.content.achievements.Achievements;

public class PrivateMessaging implements PacketType {

	public final int ADD_FRIEND = 188, SEND_PM = 126, REMOVE_FRIEND = 215, CHANGE_PM_STATUS = 95, REMOVE_IGNORE = 74, ADD_IGNORE = 133;

	@Override
	public void processPacket(final Player c, final int packetType, final int packetSize) {
		switch (packetType) {

		case ADD_FRIEND:
			c.friendUpdate = true;
			final long friendToAdd = c.getInStream().readQWord();
			boolean canAdd = true;

			for (final long friend : c.friends) {
				if (friend != 0 && friend == friendToAdd) {
					canAdd = false;
					c.sendMessage(friendToAdd + " is already on your friends list.");
				}
			}
			if (canAdd == true) {
				for (int i1 = 0; i1 < c.friends.length; i1++) {
					if (c.friends[i1] == 0) {
						c.friends[i1] = friendToAdd;
						for (int i2 = 1; i2 < World.PLAYERS.capacity(); i2++) {
							if (World.PLAYERS.get(i2) != null && World.PLAYERS.get(i2).isActive && Misc.playerNameToInt64(World.PLAYERS.get(i2).playerName) == friendToAdd) {
								final Player o = World.PLAYERS.get(i2);
								if (o != null) {
									if (World.PLAYERS.get(i2).privateChat == 0 || World.PLAYERS.get(i2).privateChat == 1 && o.getPA().isInPM(Misc.playerNameToInt64(c.playerName))) {
										c.getPA().loadPM(friendToAdd, 1);
										Achievements.increase(c, AchievementType.FRIENDS, 1);
										break;
									}
								}
							}
						}
						break;
					}
				}
			}
			break;
		case SEND_PM:
			final long sendMessageToFriendId = c.getInStream().readQWord();
			final byte pmchatText[] = new byte[100];
			final int pmchatTextSize = (byte) (packetSize - 8);
			c.getInStream().readBytes(pmchatText, pmchatTextSize, 0);
			if (Connection.isMuted(c)) {
				break;
			}
			final long myName = Misc.playerNameToInt64(c.playerName);
			boolean pmSent = false;
			for (final long element : c.friends) {
				if (element == sendMessageToFriendId) {

					for (int i2 = 1; i2 < World.PLAYERS.capacity(); i2++) {
						if (World.PLAYERS.get(i2) != null && World.PLAYERS.get(i2).isActive && Misc.playerNameToInt64(World.PLAYERS.get(i2).playerName) == sendMessageToFriendId) {
							final Player o = World.PLAYERS.get(i2);
							if (o != null) {
								if (c.playerRights >= 2 || World.PLAYERS.get(i2).privateChat == 0 || World.PLAYERS.get(i2).privateChat == 1 && !o.getPA().isIgnored(myName)) {
									o.getPA().sendPM(myName, c.playerRights, pmchatText, pmchatTextSize);
									pmSent = true;
								}
							}
							break;
						}
					}
					if (!pmSent) {
						c.sendMessage("That player is currently offline.");
						break;
					}
				}
			}
			break;

		case REMOVE_FRIEND:
			c.friendUpdate = true;
			final long friendToRemove = c.getInStream().readQWord();

			for (int i1 = 0; i1 < c.friends.length; i1++) {
				if (c.friends[i1] == friendToRemove) {
					for (int i2 = 1; i2 < World.PLAYERS.capacity(); i2++) {
						final Player o = World.PLAYERS.get(i2);
						if (o != null) {
							if (c.friends[i1] == Misc.playerNameToInt64(World.PLAYERS.get(i2).playerName)) {
								o.getPA().updatePM(c.getIndex(), 0);
								break;
							}
						}
					}
					c.friends[i1] = 0;
					break;
				}
			}
			break;

		case REMOVE_IGNORE:
			c.friendUpdate = true;
			final long ignore = c.getInStream().readQWord();
			for (int i = 0; i < c.ignores.length; i++) {
				if (c.ignores[i] == ignore) {
					c.ignores[i] = 0;
					break;
				}
			}
			break;

		case CHANGE_PM_STATUS:
			c.getInStream().readUnsignedByte();
			c.privateChat = c.getInStream().readUnsignedByte();
			c.getInStream().readUnsignedByte();
			for (int i1 = 1; i1 < World.PLAYERS.capacity(); i1++) {
				if (World.PLAYERS.get(i1) != null && World.PLAYERS.get(i1).isActive == true) {
					final Player o = World.PLAYERS.get(i1);
					if (o != null) {
						o.getPA().updatePM(c.getIndex(), 1);
					}
				}
			}
			break;

		case ADD_IGNORE:
			c.friendUpdate = true;

			final long ignoreAdd = c.getInStream().readQWord();
			for (int i = 0; i < c.ignores.length; i++) {
				if (c.ignores[i] == 0) {
					c.ignores[i] = ignoreAdd;
					break;
				}
			}
			break;
		}
	}

}
