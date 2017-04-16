package com.bclaus.rsps.server.vd.player.net;

import java.util.Iterator;
import java.util.Optional;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.rsa.ByteBuffer;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;

/**
 * Handles all of the player updating needs
 * 
 * @author Mobster
 *
 */
public class PlayerUpdating {

	/**
	 * A global {@link ByteBuffer} to write update blocks too
	 */
	public static ByteBuffer updateBlock = new ByteBuffer(new byte[Constants.BUFFER_SIZE]);
	public static String getPlayerCount;

	/**
	 * Announces a message to all online players
	 * 
	 * @param message
	 */
	public static void announce(String message) {
		World.PLAYERS.forEach(p -> p.sendMessage(message));
	}

	/**
	 * Sends a global push notification to all online players
	 * 
	 * @param message
	 */
	public static void sendGlobalPushNotification(String message) {
		World.PLAYERS.forEach(p -> p.sendPushNotification(message));
	}

	/**
	 * Gets a player by their username
	 * 
	 * @param playerName
	 *            The name of the player to look for
	 * @return
	 */
	
	public static Player getPlayerByName(String playerName) {
		return World.PLAYERS.search(p -> p.playerName.equalsIgnoreCase(playerName)).orElse(null);
	}

	/**
	 * Gets an {@link Optional} by the name of the player
	 * 
	 * @param name
	 *            The name of the player
	 * @return
	 */
	public static Optional<Player> getPlayer(String name) {
		return World.PLAYERS.search(p -> p.playerName.equalsIgnoreCase(name));
	}

	/**
	 * Gets a player by their username
	 * 
	 * @param name
	 *            The name of the player
	 * @return The player by their username
	 */
	public static Player getPlayers(String name) {
		return getPlayerByName(name);
	}
	

	/**
	 * Gets the total amount of players online
	 * 
	 * @return The total amount of players online
	 */
	public static int getPlayerCount() {
		return World.PLAYERS.size();
	}

	/**
	 * Gets the total amount of staff online
	 * 
	 * @return The total amount of staff online
	 */
	public static int getStaffCount() {
		int count = 0;
		for (Player player : World.PLAYERS) {
			if (player != null && player.playerRights > 0 && player.playerRights < 6) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Checks if a player is online
	 * 
	 * @param playerName
	 *            The name of the player
	 * @return If the player is online
	 */
	public static boolean isPlayerOn(String playerName) {
		return getPlayer(playerName).isPresent();
	}


	/**
	 * Updates a {@link Player} registered to the {@link World}
	 * 
	 * @param player
	 *            The {@link Player} to update the world for
	 * @param buffer
	 *            The buffer to write data too
	 */
	public static void updatePlayer(Player player, ByteBuffer buffer) {

		updateBlock.currentOffset = 0;
		if (World.updateRunning && !World.updateAnnounced) {
			buffer.createFrame(114);
			buffer.writeWordBigEndian(World.updateSeconds * 50 / 30);
		}

		updateThisPlayerMovement(player, buffer);
		boolean saveChatTextUpdate = player.isChatTextUpdateRequired();
		player.setChatTextUpdateRequired(false);
		appendPlayerUpdateBlock(player, updateBlock, false, true);
		player.setChatTextUpdateRequired(saveChatTextUpdate);

		buffer.writeBits(8, player.localPlayers.size());
		Iterator<Player> $it = player.localPlayers.iterator();
		while ($it.hasNext()) {
			Player target = $it.next();
			if (!player.didTeleport && !target.didTeleport && player.withinDistance(target) && !player.disconnected) {
				updatePlayerMovement(target, buffer);
				appendPlayerUpdateBlock(target, updateBlock, false, player.equals(target));
			} else {
				buffer.writeBits(1, 1);
				buffer.writeBits(2, 3);
				$it.remove();
			}
		}

		int amount = 0;

		for (Player target : World.PLAYERS) {
			if (amount == 15) {
				break;
			}
			if (player.localPlayers.size() >= 255) {
				break;
			}
			if (target == null || target.equals(player) || target.disconnected) {
				continue;
			}
			if (!player.withinDistance(target)) {
				continue;
			}
			if (player.localPlayers.add(target)) {
				addNewPlayer(player, target, buffer, updateBlock);
				amount++;
			}
		}

		if (updateBlock.currentOffset > 0) {
			buffer.writeBits(11, 2047);
			buffer.finishBitAccess();
			buffer.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			buffer.finishBitAccess();
		}

		buffer.endFrameVarSizeWord();
	}

	/**
	 * Handles disconnecting a player from the server and removing him from
	 * anything needing to be removed from
	 * 
	 * @param player
	 *            The {@link Player} to remove from the server
	 */
	public static void disconnect(Player player) {
		if (player.inTrade) {
			Player o = World.PLAYERS.get(player.tradeWith);
			if (o != null) {
				o.getTradeAndDuel().declineTrade();
				player.getTradeAndDuel().declineTrade();
			}
		}
		if (player.getPet().getNpc() != null && player.getPet().getPet() != null) {
			player.getPet().dispose();
		}
		if (player.duelStatus == 6) {
			player.getTradeAndDuel().claimStakedItems();
		}
		if (player.duelStatus == 5) {
			Player duelWith = World.PLAYERS.get(player.duelingWith);
			if (duelWith != null) {
				duelWith.getTradeAndDuel().duelVictory();
			}
			player.getPA().movePlayer(Constants.DUELING_RESPAWN_X, Constants.DUELING_RESPAWN_Y, 0);
		} else if (player.duelStatus >= 1 && player.duelStatus <= 4) {
			Player duelWith = World.PLAYERS.get(player.duelingWith);
			if (duelWith != null) {
				duelWith.getTradeAndDuel().declineDuel();
			}
			player.getTradeAndDuel().declineDuel();
		}
	}

	/**
	 * Checks if the player is a staff member
	 * 
	 * @param message
	 *            Not used
	 */
	public static void isStaffMember(String message) {
		for (Player player : World.PLAYERS) {
			if (player != null) {
				if (player.playerRights > 0 && player.playerRights < 6) {
					Player client = player;
					client.sendMessage("Player names:" + player.playerName);
				}
			}
		}
	}

	public static void sendMessageStaff(String message) {
		for (Player player : World.PLAYERS) {
			if (player != null) {
				if (player.playerRights > 0 && player.playerRights < 6) {
					player.canTele = true;
					Player client = player;
					client.sendMessage("<col=255>[STAFF MESSAGE] " + message + "</col>");
				}
			}
		}
	}

	public static void createStaffList(Player myPlayer) {
		boolean found = false;
		for (Player player : World.PLAYERS) {
			if (player != null && player.playerRights > 0 && player.playerRights < 6) {
				myPlayer.sendMessage("<img=" + player.playerRights + "></img>" + Misc.formatPlayerName(player.playerName) + " Is online");
				found = true;
			}
		}
		if (!found)
			myPlayer.sendMessage("There are currently no staff members online, please access the forums for more help.");
	}

	/**
	 * Writes the players update block to the {@link ByteBuffer}
	 * 
	 * @param player
	 *            The {@link Player}s update blocks to write to the buffer
	 * @param buffer
	 *            The {@link ByteBuffer} to write data too
	 * @param forceAppearance
	 *            Forces the player to update appearance
	 * @param samePlayer
	 *            Don't update if its the same player
	 */
	private static void appendPlayerUpdateBlock(Player player, ByteBuffer buffer, boolean forceAppearance, boolean samePlayer) {
		if (!player.updateRequired && !player.isChatTextUpdateRequired()) {
			return;
		}
		int updateMask = 0;
		if (player.getUpdateBlock() != null && !samePlayer && !forceAppearance) {
			buffer.writeBytes(player.getUpdateBlock().buffer, player.getUpdateBlock().currentOffset);
			return;
		}
		if (player.isMask100update()) {
			updateMask |= 0x100;
		}
		if (player.animationRequest != -1) {
			updateMask |= 0x8;
		}
		if (player.forcedChatUpdateRequired) {
			updateMask |= 0x4;
		}
		if (player.isChatTextUpdateRequired()) {
			updateMask |= 0x80;
		}
		if (player.isFaceUpdateRequired()) {
			updateMask |= 0x1;
		}
		if (player.isAppearanceUpdateRequired()) {
			updateMask |= 0x10;
		}
		if (player.FocusPointX != -1) {
			updateMask |= 0x2;
		}
		if (player.isHitUpdateRequired()) {
			updateMask |= 0x20;
		}

		if (player.hitUpdateRequired2) {
			updateMask |= 0x200;
		}

		ByteBuffer updateBlock = new ByteBuffer(new byte[500]);
		if (updateMask >= 0x100) {
			updateMask |= 0x40;
			updateBlock.writeByte(updateMask & 0xFF);
			updateBlock.writeByte(updateMask >> 8);
		} else {
			updateBlock.writeByte(updateMask);
		}

		// now writing the various update blocks itself - note that their order
		// crucial
		if (player.isMask100update()) {
			appendMask100Update(player, updateBlock);
		}
		if (player.animationRequest != -1) {
			appendAnimationRequest(player, updateBlock);
		}
		if (player.forcedChatUpdateRequired) {
			appendForcedChat(player, updateBlock);
		}
		if (player.isChatTextUpdateRequired()) {
			appendPlayerChatText(player, updateBlock);
		}
		if (player.isFaceUpdateRequired()) {
			appendFaceUpdate(player, updateBlock);
		}
		if (player.isAppearanceUpdateRequired()) {
			appendPlayerAppearance(player, updateBlock);
		}
		if (player.FocusPointX != -1) {
			appendSetFocusDestination(player, updateBlock);
		}
		if (player.isHitUpdateRequired()) {
			appendHitUpdate(player, updateBlock);
		}
		if (player.hitUpdateRequired2) {
			appendHitUpdate2(player, updateBlock);
		}
		if (!samePlayer && !forceAppearance) {
			player.setUpdateBlock(updateBlock);
		}
		buffer.writeBytes(updateBlock.buffer, updateBlock.currentOffset);
	}

	/**
	 * Adds a new player to the game world for the player
	 * 
	 * @param myPlayer
	 *            Your player thats having players added too
	 * @param otherPlayer
	 *            The other player your player is adding
	 * @param str
	 *            The {@link ByteBuffer} to write data too
	 * @param updateBlock
	 *            The {@link ByteBuffer} to write the update block on
	 */
	private static void addNewPlayer(Player myPlayer, Player otherPlayer, ByteBuffer str, ByteBuffer updateBlock) {
		int id = otherPlayer.getIndex();
		str.writeBits(11, id);
		str.writeBits(1, 1);
		boolean savedFlag = otherPlayer.isAppearanceUpdateRequired();
		boolean savedUpdateRequired = otherPlayer.updateRequired;
		otherPlayer.setAppearanceUpdateRequired(true);
		otherPlayer.updateRequired = true;
		appendPlayerUpdateBlock(otherPlayer, updateBlock, true, otherPlayer == myPlayer);
		otherPlayer.setAppearanceUpdateRequired(savedFlag);
		otherPlayer.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
		int z = otherPlayer.getAbsY() - myPlayer.getAbsY();
		if (z < 0) {
			z += 32;
		}
		str.writeBits(5, z);
		z = otherPlayer.getAbsX() - myPlayer.getAbsX();
		if (z < 0) {
			z += 32;
		}
		str.writeBits(5, z);
	}

	/**
	 * Updates this players movement
	 * 
	 * @param player
	 *            The {@link Player} to update the movement for
	 * @param str
	 *            The {@link ByteBuffer} to write the data on
	 */
	private static void updateThisPlayerMovement(Player player, ByteBuffer str) {// valid
		if (player.mapRegionDidChange) {
			str.createFrame(73);
			str.writeWordA(player.mapRegionX + 6);
			str.writeWord(player.mapRegionY + 6);
		}

		if (player.didTeleport) {
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);
			str.writeBits(2, 3);
			str.writeBits(2, player.heightLevel);
			str.writeBits(1, 1);
			str.writeBits(1, (player.updateRequired) ? 1 : 0);
			str.writeBits(7, player.currentY);
			str.writeBits(7, player.currentX);
			return;
		}

		if (player.dir1 == -1) {
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			player.isMoving = false;
			if (player.updateRequired) {
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
			if (player.DirectionCount < 50) {
				player.DirectionCount++;
			}
		} else {
			player.DirectionCount = 0;
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);

			if (player.dir2 == -1) {
				player.isMoving = true;
				str.writeBits(2, 1);
				str.writeBits(3, Misc.xlateDirectionToClient[player.dir1]);
				if (player.updateRequired) {
					str.writeBits(1, 1);
				} else {
					str.writeBits(1, 0);
				}
			} else {
				player.isMoving = true;
				str.writeBits(2, 2);
				str.writeBits(3, Misc.xlateDirectionToClient[player.dir1]);
				str.writeBits(3, Misc.xlateDirectionToClient[player.dir2]);
				if (player.updateRequired) {
					str.writeBits(1, 1);
				} else {
					str.writeBits(1, 0);
				}
			}
		}
	}

	/**
	 * Updates another players movement
	 * 
	 * @param player
	 *            The {@link Player} to update movement for
	 * @param str
	 *            The {@link ByteBuffer} To write data on
	 */
	private static void updatePlayerMovement(Player player, ByteBuffer str) {// valid
		if (player.dir1 == -1) {
			if (player.updateRequired || player.isChatTextUpdateRequired()) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else if (player.dir2 == -1) {
			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[player.dir1]);
			str.writeBits(1, (player.updateRequired || player.isChatTextUpdateRequired()) ? 1 : 0);
		} else {
			str.writeBits(1, 1);
			str.writeBits(2, 2);
			str.writeBits(3, Misc.xlateDirectionToClient[player.dir1]);
			str.writeBits(3, Misc.xlateDirectionToClient[player.dir2]);
			str.writeBits(1, (player.updateRequired || player.isChatTextUpdateRequired()) ? 1 : 0);
		}
	}

	/**
	 * Updates the graphics mask
	 * 
	 * @param player
	 *            The {@link Player} to update the mask for
	 * @param str
	 *            The {@link ByteBuffer} to write data on
	 */
	private static void appendMask100Update(Player player, ByteBuffer str) {
		str.writeWordBigEndian(player.mask100var1);
		str.writeDWord(player.mask100var2);
	}

	/**
	 * Updates the forced chat mask
	 * 
	 * @param player
	 *            The {@link Player} to update the mask for
	 * @param str
	 *            The {@link ByteBuffer} to write data on
	 */
	private static void appendForcedChat(Player player, ByteBuffer str) {
		str.writeString(player.forcedText);
	}

	/**
	 * Updates the public chat mask
	 * 
	 * @param player
	 *            The {@link Player} to update the mask for
	 * @param str
	 *            The {@link ByteBuffer} to write data on
	 */
	private static void appendPlayerChatText(Player player, ByteBuffer str) {
		str.writeWordBigEndian(((player.getChatTextColor() & 0xFF) << 8) + (player.getChatTextEffects() & 0xFF));
		str.writeByte(player.playerRights);
		str.writeByteC(player.getChatTextSize());
		str.writeBytes_reverse(player.getChatText(), player.getChatTextSize(), 0);
	}

	/**
	 * Updates the players appearance mask
	 * 
	 * @param player
	 *            The {@link Player} to update the mask for
	 * @param str
	 *            The {@link ByteBuffer} to write data on
	 */
	private static void appendPlayerAppearance(Player player, ByteBuffer str) {
		player.getPlayerProps().currentOffset = 0;
		player.getPlayerProps().writeByte(player.playerAppearance[0]);
		player.getPlayerProps().writeByte(player.headIcon);
		player.getPlayerProps().writeByte(player.playerRights);
		player.getPlayerProps().writeByte(player.headIconPk);
		if (!player.playerIsNPC) {
			if (player.playerEquipment[Player.playerHat] > 1) {
				player.getPlayerProps().writeWord(0x200 + player.playerEquipment[Player.playerHat]);
			} else {
				player.getPlayerProps().writeByte(0);
			}

			if (player.playerEquipment[Player.playerCape] > 1) {
				player.getPlayerProps().writeWord(0x200 + player.playerEquipment[Player.playerCape]);
			} else {
				player.getPlayerProps().writeByte(0);
			}

			if (player.playerEquipment[Player.playerAmulet] > 1) {
				player.getPlayerProps().writeWord(0x200 + player.playerEquipment[Player.playerAmulet]);
			} else {
				player.getPlayerProps().writeByte(0);
			}

			if (player.playerEquipment[Player.playerWeapon] > 1) {
				player.getPlayerProps().writeWord(0x200 + player.playerEquipment[Player.playerWeapon]);
			} else {
				player.getPlayerProps().writeByte(0);
			}

			if (player.playerEquipment[Player.playerChest] > 1) {
				player.getPlayerProps().writeWord(0x200 + player.playerEquipment[Player.playerChest]);
			} else {
				player.getPlayerProps().writeWord(0x100 + player.playerAppearance[2]);
			}

			if (player.playerEquipment[Player.playerShield] > 1) {
				player.getPlayerProps().writeWord(0x200 + player.playerEquipment[Player.playerShield]);
			} else {
				player.getPlayerProps().writeByte(0);
			}

			if (!player.isFullBody) {
				player.getPlayerProps().writeWord(0x100 + player.playerAppearance[3]);
			} else {
				player.getPlayerProps().writeByte(0);
			}

			if (player.playerEquipment[Player.playerLegs] > 1) {
				player.getPlayerProps().writeWord(0x200 + player.playerEquipment[Player.playerLegs]);
			} else {
				player.getPlayerProps().writeWord(0x100 + player.playerAppearance[5]);
			}

			if (!player.isFullHelm && !player.isFullMask) {
				player.getPlayerProps().writeWord(0x100 + player.playerAppearance[1]);
			} else {
				player.getPlayerProps().writeByte(0);
			}

			if (player.playerEquipment[Player.playerHands] > 1) {
				player.getPlayerProps().writeWord(0x200 + player.playerEquipment[Player.playerHands]);
			} else {
				player.getPlayerProps().writeWord(0x100 + player.playerAppearance[4]);
			}

			if (player.playerEquipment[Player.playerFeet] > 1) {
				player.getPlayerProps().writeWord(0x200 + player.playerEquipment[Player.playerFeet]);
			} else {
				player.getPlayerProps().writeWord(0x100 + player.playerAppearance[6]);
			}

			if (player.playerAppearance[0] != 1 && !player.isFullMask) {
				player.getPlayerProps().writeWord(0x100 + player.playerAppearance[7]);
			} else {
				player.getPlayerProps().writeByte(0);
			}

		} else {
			player.getPlayerProps().writeWord(-1);// Tells client that were
													// being a npc
			player.getPlayerProps().writeWord(player.playerNPCID);// send NpcID
		}

		player.getPlayerProps().writeByte(player.playerAppearance[8]);
		player.getPlayerProps().writeByte(player.playerAppearance[9]);
		player.getPlayerProps().writeByte(player.playerAppearance[10]);
		player.getPlayerProps().writeByte(player.playerAppearance[11]);
		player.getPlayerProps().writeByte(player.playerAppearance[12]);
		player.getPlayerProps().writeWord(player.playerStandIndex); // standAnimIndex
		player.getPlayerProps().writeWord(player.playerTurnIndex); // standTurnAnimIndex
																	// er
		player.getPlayerProps().writeWord(player.playerWalkIndex); // walkAnimIndex
		player.getPlayerProps().writeWord(player.playerTurn180Index); // turn180AnimIndex
		player.getPlayerProps().writeWord(player.playerTurn90CWIndex); // turn90CWAnimIndex
		player.getPlayerProps().writeWord(player.playerTurn90CCWIndex); // turn90CCWAnimIndex
		player.getPlayerProps().writeWord(player.playerRunIndex); // runAnimIndex
		player.getPlayerProps().writeString(player.loyaltyTitle); // loyalty
																	// title
		player.getPlayerProps().writeByte(player.prestigeTitle);
		player.getPlayerProps().writeQWord(Misc.playerNameToInt64(player.getName()));
		player.getPlayerProps().writeByte(player.combatLevel); // combat level
		player.getPlayerProps().writeWord(0);
		str.writeByteC(player.getPlayerProps().currentOffset);
		str.writeBytes(player.getPlayerProps().buffer, player.getPlayerProps().currentOffset, 0);
	}

	/**
	 * Updates the face entity mask
	 * 
	 * @param player
	 *            The {@link Player} to update the mask for
	 * @param str
	 *            The {@link ByteBuffer} to write data on
	 */
	private static void appendFaceUpdate(Player player, ByteBuffer str) {
		str.writeWordBigEndian(player.face);
	}

	/**
	 * Updates the animation mask
	 * 
	 * @param player
	 *            The {@link Player} to update the mask for
	 * @param str
	 *            The {@link ByteBuffer} to write data on
	 */
	private static void appendAnimationRequest(Player player, ByteBuffer str) {
		str.writeWordBigEndian((player.animationRequest == -1) ? 65535 : player.animationRequest);
		str.writeByteC(player.animationWaitCycles);
	}

	/**
	 * Updates the face position mask
	 * 
	 * @param player
	 *            The {@link Player} to update the mask for
	 * @param str
	 *            The {@link ByteBuffer} to write data on
	 */
	private static void appendSetFocusDestination(Player player, ByteBuffer str) {
		str.writeWordBigEndianA(player.FocusPointX);
		str.writeWordBigEndian(player.FocusPointY);
	}

	/**
	 * Updates the first hit mask
	 * 
	 * @param player
	 *            The {@link Player} to update the mask for
	 * @param str
	 *            The {@link ByteBuffer} to write data on
	 */
	private static void appendHitUpdate(Player player, ByteBuffer str) {
		if (player.primary == null)
			return;
		str.writeByte(player.primary.getDamage());
		str.writeByteA(player.primary.getType().getId());
		if (player.playerLevel[3] <= 0 && !player.isDead) {
			player.playerLevel[3] = 0;
			player.isDead = true;
			player.hasDied = true;
			Server.getTaskScheduler().schedule(new ScheduledTask(1, true) {

				@Override
				public void execute() {
					if (!player.isRegistered() || player.disconnected || !player.hasDied) {
						this.stop();
						return;
					}

					switch (player.countdown) {
					case 0:
						player.isDead = true;
						player.resetWalkingQueue();
						break;
					case 1:
						player.startAnimation(0x900);
						player.poisonDamage = -1;
						break;
					case 5:
						player.getPA().applyDead();
						break;
					case 6:
						player.getPA().giveLife();
						player.isDead = false;
						stop();
						break;
					}
					player.countdown++;

				}

				@Override
				public void onStop() {
					player.hasDied = false;
					player.countdown = 0;
				}
			});
		}
		str.writeByteC(player.playerLevel[3]); // Their current hp, for HP bar
		str.writeByte(Player.getLevelForXP(player.playerXP[3])); // Their max
																	// hp, for
																	// HP bar
	}

	/**
	 * Updates the second hit mask
	 * 
	 * @param player
	 *            The {@link Player} to update the mask for
	 * @param str
	 *            The {@link ByteBuffer} to write data on
	 */
	private static void appendHitUpdate2(Player player, ByteBuffer str) {
		if (player.secondary == null)
			return;
		str.writeByte(player.secondary.getDamage());
		str.writeByteS(player.secondary.getType().getId());
		if (player.playerLevel[3] <= 0 && !player.isDead) {
			player.playerLevel[3] = 0;
			player.hasDied = true;
			player.isDead = true;
			Server.getTaskScheduler().schedule(new ScheduledTask(1, true) {

				@Override
				public void execute() {
					if (!player.isRegistered() || player.disconnected || !player.hasDied) {
						stop();
						return;
					}
					switch (player.countdown) {
					case 0:
						player.isDead = true;
						player.resetWalkingQueue();
						break;
					case 1:
						player.startAnimation(0x900);
						player.poisonDamage = -1;
						break;
					case 5:
						player.getPA().applyDead();
						break;
					case 6:
						player.getPA().giveLife();
						player.isDead = false;
						stop();
						break;
					}
					player.countdown++;
				}

				@Override
				public void onStop() {
					player.hasDied = false;
					player.countdown = 0;
				}
			});
		}
		str.writeByte(player.playerLevel[3]);
		str.writeByteC(Player.getLevelForXP(player.playerXP[3]));
	}

}
