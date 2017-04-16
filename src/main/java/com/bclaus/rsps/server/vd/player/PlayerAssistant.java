package com.bclaus.rsps.server.vd.player;

import static com.bclaus.rsps.server.Constants.SINGLE_AND_MULTI_ZONES;

import java.awt.Color;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.region.Region;
import com.bclaus.rsps.server.vd.content.DoubleExperience;
import com.bclaus.rsps.server.vd.content.StreakHandler;
import com.bclaus.rsps.server.vd.content.combat.magic.Enchanting;
import com.bclaus.rsps.server.vd.content.minigames.FightCaves;
import com.bclaus.rsps.server.vd.content.skills.impl.Fletching;
import com.bclaus.rsps.server.vd.content.skills.impl.crafting.Crafting;
import com.bclaus.rsps.server.vd.event.CycleEventContainer;
import com.bclaus.rsps.server.vd.event.CycleEventHandler;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import com.bclaus.rsps.server.vd.world.Position;
import org.omicron.jagex.runescape.CollisionMap;

import com.bclaus.rsps.server.Connection;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.clan.Clan;
import com.bclaus.rsps.server.vd.content.KillcountList;
import com.bclaus.rsps.server.vd.content.PVPAssistant;
import com.bclaus.rsps.server.vd.content.achievements.AchievementType;
import com.bclaus.rsps.server.vd.content.achievements.Achievements;
import com.bclaus.rsps.server.vd.content.combat.PKHandler;
import com.bclaus.rsps.server.vd.content.combat.magic.NonCombatSpells;
import com.bclaus.rsps.server.vd.content.minigames.PestControl;
import com.bclaus.rsps.server.vd.content.skills.impl.slayer.SocialSlayerData;
import com.bclaus.rsps.server.vd.event.CycleEvent;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.items.ItemAssistant;
import com.bclaus.rsps.server.vd.items.bank.BankTab;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.objects.cannon.CannonManager;

public class PlayerAssistant {

	private final Player player;

	public PlayerAssistant(Player Client) {
		this.player = Client;
	}
	/**
	 * Announcements that are called every 5 minutes.
	 */
	public void announcements() {
		if (player.offMessages) {
			return;
		}
		CycleEventHandler.addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player.offMessages) {
					container.stop();
				}
				int random = 0;
				random = Misc.random(17);
				if (!player.offMessages) {
					if (random == 0) {
						player.sendMessage("[Server]<col=800000> Join the Clanchat 'DemonRsps'.");
					} else if (random == 1) {
						player.sendMessage("[Server]<col=800000> Do you want to becoming a staff Member? make Applications on forums.");
					} else if (random == 2) {
						player.sendMessage(
								"<col=255>Click on screenshot button, to take a screenshot, the image is saved at the");
						player.sendMessage("<col=800000>same location as the Cache.");
					} else if (random == 3) {
						player.sendMessage("[Server]<col=800000> dwarfcannon may help you in boss!");
					} else if (random == 4) {
						player.sendMessage("[Server]<col=800000>  Do you have any suggestions? feel free to post it on forums!");
					} else if (random == 5) {
						player.sendMessage("[Server]<col=800000>  Report any glitches you come across to staff members.");
					} else if (random == 6) {
						player.sendMessage(" [Server]<col=800000> ::vote to get us more players and get vote points!");
					} else if (random == 7) {
						player.sendMessage("[Server]<col=800000>  Please be friendly to everyone in the clanchat.");
					} else if (random == 8) {
						player.sendMessage("[Server] <col=800000> Did you know? donate keep server alive ::donate!");
					} else if (random == 9) {
						player.sendMessage(
								"[Server] <col=800000> Need any help ? please pm any staff member Online.");
					
					}
				}
			}

			@Override
			public void stop() {

			}
		}, 500);
	}

	public static void stopSkilling(Player c) {
		if (c.fletchItem > 0)
			Fletching.resetFletching(c);
	}

	public void updateClanChatInterface(int x) {
		for (int i = x; i < 100; i++) {
			sendFrame126("", 18144 + x);
		}
	}
	

	public void replaceEquipment(int slot, int replaceItem) {
		if (player.playerEquipment[slot] > 0) {
			player.playerEquipment[slot] = replaceItem;
			if (replaceItem <= 0) {
				player.playerEquipmentN[slot] = 0;
			}
			resetAnimation();
			player.getItems().updateSlot(slot);
			player.getItems().resetBonus();
			player.getItems().getBonus();
			player.getItems().writeBonus();
			player.updateRequired = true;
		}
	}

	public int getSheathed(int id) {
		switch (id) {
		case 11600:
			return 11694;
		case 11694:
			return 11600;
		}
		return -1;
	}

	public int getSheathSlot(int id) {
		switch (id) {
		case -1:
			return 5; /* Shield Slot */
		}
		return 3;
	}

	public void handleSheath(int id) {
		switch (id) {
		case 11600:
		case 11694:
			replaceEquipment(getSheathSlot(id), getSheathed(id));
			break;
		}
	}

	public void playerWalk(int x, int y) {
		PathFinder.getPathFinder().findRoute(player, x, y, true, 1, 1);
	}

	public void sendInterfaceTextColor(Color colour, int frame) {
		int r = (colour.getRed() >> 3) & 0x1F;
		int g = (colour.getGreen() >> 3) & 0x1F;
		int b = (colour.getBlue() >> 3) & 0x1F;
		player.getOutStream().createFrame(122);
		player.getOutStream().writeWordBigEndianA(frame);
		player.getOutStream().writeWordBigEndianA((r << 10) | (g << 5) | b);
		player.flushOutStream();
	}

	public void sendString(final String s, final int id) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(126);
			player.getOutStream().writeString(s);
			player.getOutStream().writeWordA(id);
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}

	}

	public void sendChatBoxInterface(int Frame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(164);
			player.getOutStream().writeWordBigEndian_dup(Frame);
			// c.flushOutStream();
		}

	}

	public void clearClanChatInterface(int x, boolean flag) {
		if (flag) {
			sendFrame126("Talking in: ", 18139);
			sendFrame126("Owner: ", 18140);
		}
		for (int i = 18144; i < 18144 + x; i++) {
			sendFrame126("", i);
		}
	}

	public void resetSkill(int skillID, int level) {
		if (!player.inWild()) {
			player.dialogueAction = -1;
			for (int j = 0; j < player.playerEquipment.length; j++) {
				if (player.playerEquipment[j] > 0) {
					player.getPA().closeAllWindows();
					player.getDH().sendDialogues(420, 970);
					return;
				}
			}
			if (!player.getItems().playerHasItem(995, 1000000)) {
				player.getDH().sendDialogues(214, player.npcType);
				player.sendMessage("@red@You don't have enough cash to reset! You need 1m");
				player.getPA().closeAllWindows();
				return;
			}
			player.getItems().deleteItem(995, player.getItems().getItemSlot(995), 1000000);
			player.playerXP[skillID] = player.getPA().getXPForLevel(level) + 5;
			player.playerLevel[skillID] = player.getPA().getLevelForXP(player.playerXP[skillID]);
			player.getPA().refreshSkill(skillID);
			player.getPA().closeAllWindows();
			player.sendMessage("You have successfully reset your " + quickChat[skillID]);
		}
	}

	public void sendStatement(String s) {
		sendFrame126(s, 357);
		sendFrame126("Click here to continue", 358);
		sendFrame164(356);
	}

	public void movePlayer1(int x, int y) {
		player.resetWalkingQueue();
		player.teleportToX = x;
		player.teleportToY = y;
		requestUpdates();
	}

	public void createProjectile3(int casterY, int casterX, int offsetY, int offsetX, int gfxMoving, int StartHeight,
			int endHeight, int speed, int AtkIndex) {
		for (int i = 1; i < World.PLAYERS.capacity(); i++) {
			if (World.PLAYERS.get(i) != null) {
				Player p = World.PLAYERS.get(i);
				if (p.WithinDistance(player.absX, player.absY, p.absX, p.absY, 60)) {
					if (p.heightLevel == player.heightLevel) {
						if (World.PLAYERS.get(i) != null && !World.PLAYERS.get(i).disconnected) {
							p.outStream.createFrame(85);
							p.outStream.writeByteC((casterY - (p.mapRegionY * 8)) - 2);
							p.outStream.writeByteC((casterX - (p.mapRegionX * 8)) - 3);
							p.outStream.createFrame(117);
							p.outStream.writeByte(50);
							p.outStream.writeByte(offsetY);
							p.outStream.writeByte(offsetX);
							p.outStream.writeWord(AtkIndex);
							p.outStream.writeWord(gfxMoving);
							p.outStream.writeByte(StartHeight);
							p.outStream.writeByte(endHeight);
							p.outStream.writeWord(51);
							p.outStream.writeWord(speed);
							p.outStream.writeByte(16);
							p.outStream.writeByte(64);
						}
					}
				}
			}
		}
	}

	/**
	 * MulitCombat icon
	 * 
	 * @param i1
	 *            0 = off 1 = on
	 */
	public void multiWay(int i1) {
		if (player != null) {
			player.outStream.createFrame(61);
			player.outStream.writeByte(i1);
			player.updateRequired = true;
			player.setAppearanceUpdateRequired(true);
		}
	}

	public void clearClanChat() {
		player.clanId = -1;
		sendFrame126("Talking in: ", 18139);
		sendFrame126("Owner: ", 18140);
		for (int j = 18144; j < 18244; j++) {
			sendFrame126("", j);
		}
	}

	public void resetAutoCast() {
		player.autocastId = 0;
		player.onAuto = false;
		player.autocasting = false;
		sendFrame36(108, 0);
	}

	public void sendFrame126(String s, int id) {
		if (!player.checkPacket126Update(s, id) && id != 56306 && id != 59507) {
			return;
		}
		if (player.getOutStream() != null) {
			player.getOutStream().createFrameVarSizeWord(126);
			player.getOutStream().writeString(s);
			player.getOutStream().writeWordA(id);
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}

	public String[] quickChat = new String[] { "Attack", "Defence", "Strength", "Range", "Prayer", "Hitpoints", "Magic",
			"Cooking", "Mining", "Smithing", "Fishing", "Woodcutting", " ", "Thieving", "Farming", "Runecrafting",
			"Firemaking", "Agility", "Crafting", "Fletching", "Herblore", "Hunting" };

	public void quickChat(int chatId, int levelId) {
		player.forcedText = "My " + quickChat[chatId] + " level is " + Player.getLevelForXP(player.playerXP[levelId])
				+ ".";
		player.forcedChatUpdateRequired = true;
		player.updateRequired = true;
		player.lastAction = System.currentTimeMillis();
	}

	public void setSkillLevel(int skillNum, int currentLevel, int XP) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(134);
			player.getOutStream().writeByte(skillNum);
			player.getOutStream().writeDWord_v1(XP);
			player.getOutStream().writeByte(currentLevel);
			player.flushOutStream();
		}
	}

	public void sendFrame106(int sideIcon) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(106);
			player.getOutStream().writeByteC(sideIcon);
			player.flushOutStream();
			requestUpdates();
		}
	}

	public void sendFrame107() {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(107);
			player.flushOutStream();
		}
	}

	public void sendFrame36(int id, int state) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(36);
			player.getOutStream().writeWordBigEndian(id);
			player.getOutStream().writeByte(state);
			player.flushOutStream();
		}
	}

	public void sendFrame185(int Frame) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(185);
			player.getOutStream().writeWordBigEndianA(Frame);
		}
	}

	public void showInterface(int interfaceid) {
		if (player != null) {
			if (player.inTrade) {
				player.logout();
				if (World.PLAYERS.get(player.tradeWith) != null)
					World.PLAYERS.get(player.tradeWith).logout();
				return;
			}
			if (player.inDuelScreen) {
				player.getPA().closeAllWindows();
				return;
			}
			if (player.getOutStream() != null) {
				player.getOutStream().createFrame(97);
				player.getOutStream().writeWord(interfaceid);
				player.flushOutStream();
				player.openInterface = interfaceid;
			}
		}
	}

	public void sendFrame248(int MainFrame, int SubFrame) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(248);
			player.getOutStream().writeWordA(MainFrame);
			player.getOutStream().writeWord(SubFrame);
			player.flushOutStream();
			player.openInterface = MainFrame;
		}
	}

	public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(246);
			player.getOutStream().writeWordBigEndian(MainFrame);
			player.getOutStream().writeWord(SubFrame);
			player.getOutStream().writeWord(SubFrame2);
			player.flushOutStream();
		}
	}

	public void sendFrame171(int MainFrame, int SubFrame) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(171);
			player.getOutStream().writeByte(MainFrame);
			player.getOutStream().writeWord(SubFrame);
			player.flushOutStream();
		}
	}

	public void sendFrame200(int MainFrame, int SubFrame) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(200);
			player.getOutStream().writeWord(MainFrame);
			player.getOutStream().writeWord(SubFrame);
			player.flushOutStream();
		}
	}

	public void sendFrame70(int i, int o, int id) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(70);
			player.getOutStream().writeWord(i);
			player.getOutStream().writeWordBigEndian(o);
			player.getOutStream().writeWordBigEndian(id);
			player.flushOutStream();
		}
	}

	public void sendFrame75(int MainFrame, int SubFrame) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(75);
			player.getOutStream().writeWordBigEndianA(MainFrame);
			player.getOutStream().writeWordBigEndianA(SubFrame);
			player.flushOutStream();
		}
	}

	public void sendFrame164(int Frame) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(164);
			player.getOutStream().writeWordBigEndian_dup(Frame);
			player.flushOutStream();
		}
	}

	public void setChatOptions(int publicChat, int privateChat, int tradeBlock) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(206);
			player.getOutStream().writeByte(publicChat);
			player.getOutStream().writeByte(privateChat);
			player.getOutStream().writeByte(tradeBlock);
			player.flushOutStream();
		}
	}

	public void sendFrame87(int id, int state) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(87);
			player.getOutStream().writeWordBigEndian_dup(id);
			player.getOutStream().writeDWord_v1(state);
			player.flushOutStream();
		}
	}

	public void sendPM(long name, int rights, byte[] chatmessage, int messagesize) {
		if (player == null)
			return;
		if (player.getOutStream() != null) {
			player.getOutStream().createFrameVarSize(196);
			player.getOutStream().writeQWord(name);
			player.getOutStream().writeDWord(player.lastChatId++);
			player.getOutStream().writeByte(rights);
			player.getOutStream().writeBytes(chatmessage, messagesize, 0);
			player.getOutStream().endFrameVarSize();
			player.flushOutStream();
			Misc.textUnpack(chatmessage, messagesize);
			Misc.longToPlayerName(name);
		}
	}

	public void createPlayerHints(int type, int id) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(254);
			player.getOutStream().writeByte(type);
			player.getOutStream().writeWord(id);
			player.getOutStream().write3Byte(0);
			player.flushOutStream();
		}
	}

	public void createObjectHints(int x, int y, int height, int pos) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(254);
			player.getOutStream().writeByte(pos);
			player.getOutStream().writeWord(x);
			player.getOutStream().writeWord(y);
			player.getOutStream().writeByte(height);
			player.flushOutStream();
		}
	}

	public void loadPM(long playerName, int world) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			if (world != 0) {
				world += 9;
			} else {
				world += 1;
			}
			player.getOutStream().createFrame(50);
			player.getOutStream().writeQWord(playerName);
			player.getOutStream().writeByte(world);
			player.flushOutStream();
		}
	}

	public void removeAllWindows() {
		player.teleportAction = "";
		player.dialogueAction = -1;
		player.teleAction = -1;
		player.openInterface = -1;
		player.isBanking = false;
		player.dialogue().interrupt();
		if (player.getOutStream() != null && player != null) {
			resetVariables();
			player.getOutStream().createFrame(219);
			player.flushOutStream();
		}
	}

	public void closeAllWindows() {
		player.teleportAction = "";
		player.dialogueAction = -1;
		player.teleAction = -1;
		player.openInterface = -1;
		player.isBanking = false;
		player.dialogue().interrupt();
		if (player.getOutStream() != null && player != null) {
			player.getPA().resetVariables();
			player.getOutStream().createFrame(219);
			player.flushOutStream();
		}
	}

	public void sendFrame34(int id, int slot, int column, int amount) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.outStream.createFrameVarSizeWord(34); // init item to smith
															// screen
			player.outStream.writeWord(column); // Column Across Smith Screen
			player.outStream.writeByte(4); // Total Rows?
			player.outStream.writeDWord(slot); // Row Down The Smith Screen
			player.outStream.writeWord(id + 1); // item
			player.outStream.writeByte(amount); // how many there are?
			player.outStream.endFrameVarSizeWord();
		}
	}

	public void walkableInterface(int id) {

		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(208);
			player.getOutStream().writeWordBigEndian_dup(id);
			player.flushOutStream();
		}
	}

	public int mapStatus = 0;

	public void sendFrame99(int state) { // used for disabling map
		// synchronized (c) {
		if (player.getOutStream() != null) {
			if (mapStatus != state) {
				mapStatus = state;
				player.getOutStream().createFrame(99);
				player.getOutStream().writeByte(state);
				player.flushOutStream();
			}
		}
	}

	/**
	 * Reseting animations for everyone
	 */

	public void frame1() {
		// synchronized (c) {
		for (int i = 0; i < World.PLAYERS.capacity(); i++) {
			if (World.PLAYERS.get(i) != null) {
				Player person = World.PLAYERS.get(i);
				if (person != null) {
					if (person.getOutStream() != null && !person.disconnected) {
						if (player.distanceToPoint(person.getX(), person.getY()) <= 25) {
							person.getOutStream().createFrame(1);
							person.flushOutStream();
							requestUpdates();
						}
					}
				}
			}
		}
	}

	/**
	 * Creating projectile
	 */
	public void createProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC((y - (player.getMapRegionY() * 8)) - 2);
			player.getOutStream().writeByteC((x - (player.getMapRegionX() * 8)) - 3);
			player.flushOutStream();
			player.getOutStream().createFrame(117);
			player.getOutStream().writeByte(angle);
			player.getOutStream().writeByte(offY);
			player.getOutStream().writeByte(offX);
			player.getOutStream().writeWord(lockon);
			player.getOutStream().writeWord(gfxMoving);
			player.getOutStream().writeByte(startHeight);
			player.getOutStream().writeByte(endHeight);
			player.getOutStream().writeWord(time);
			player.getOutStream().writeWord(speed);
			player.getOutStream().writeByte(16);
			player.getOutStream().writeByte(64);
			player.flushOutStream();
		}
	}

	public void createProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC((y - (player.getMapRegionY() * 8)) - 2);
			player.getOutStream().writeByteC((x - (player.getMapRegionX() * 8)) - 3);
			player.getOutStream().createFrame(117);
			player.getOutStream().writeByte(angle);
			player.getOutStream().writeByte(offY);
			player.getOutStream().writeByte(offX);
			player.getOutStream().writeWord(lockon);
			player.getOutStream().writeWord(gfxMoving);
			player.getOutStream().writeByte(startHeight);
			player.getOutStream().writeByte(endHeight);
			player.getOutStream().writeWord(time);
			player.getOutStream().writeWord(speed);
			player.getOutStream().writeByte(slope);
			player.getOutStream().writeByte(64);
			player.flushOutStream();
		}
	}

	// projectiles for everyone within 25 squares
	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time) {
		// synchronized (c) {
		for (int i = 0; i < World.PLAYERS.capacity(); i++) {
			Player p = World.PLAYERS.get(i);
			if (p != null) {
				Player person = p;
				if (person.getOutStream() != null) {
					if (person.distanceToPoint(x, y) <= 25) {
						if (p.heightLevel == player.heightLevel) {
							person.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time);
						}
					}
				}
			}
		}
	}

	public void createPlayersProjectile(Position p, Position offset, int angle, int speed, int gfxMoving,
                                        int startHeight, int endHeight, int lockon, int time) {
		createPlayersProjectile(p.getX(), p.getY(), offset.getX(), offset.getY(), angle, speed, gfxMoving, startHeight,
				endHeight, lockon, time);
	}

	public void createPlayersProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized (c) {
		for (int i = 0; i < World.PLAYERS.capacity(); i++) {
			Player p = World.PLAYERS.get(i);
			if (p != null) {
				Player person = p;
				if (person.getOutStream() != null) {
					if (person.distanceToPoint(x, y) <= 25) {
						person.getPA().createProjectile2(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
								endHeight, lockon, time, slope);
					}
				}
			}
		}
	}

	/**
	 * * GFX
	 */
	public void stillGfx(int id, int x, int y, int height, int time) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC(y - (player.getMapRegionY() * 8));
			player.getOutStream().writeByteC(x - (player.getMapRegionX() * 8));
			player.getOutStream().createFrame(4);
			player.getOutStream().writeByte(0);
			player.getOutStream().writeWord(id);
			player.getOutStream().writeByte(height);
			player.getOutStream().writeWord(time);
			player.flushOutStream();
		}
	}

	// creates gfx for everyone
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {
		// synchronized (c) {
		for (int i = 0; i < World.PLAYERS.capacity(); i++) {
			Player p = World.PLAYERS.get(i);
			if (p != null) {
				Player person = p;
				if (person.getOutStream() != null) {
					if (person.distanceToPoint(x, y) <= 25) {
						person.getPA().stillGfx(id, x, y, height, time);
					}
				}
			}
		}
	}

	public void sendAllObjectAnimation(Position position, int animation, int type, int orientation) {
		for (Player p : World.PLAYERS) {
			if (p == null)
				continue;

			// if (p.getPosition().isViewableFrom(position)) {
			p.getOutStream().createFrame(85);
			p.getOutStream().writeByteC(position.getY() - (p.mapRegionY * 8));
			p.getOutStream().writeByteC(position.getX() - (p.mapRegionX * 8));
			// p.flushOutStream();
			p.getOutStream().createFrame(160);
			p.getOutStream().writeByteS(((0 & 7) << 4) + (0 & 7));
			p.getOutStream().writeByteS((type << 2) + (orientation & 3));
			p.getOutStream().writeWordA(animation);
			// p.flushOutStream();
			// }
		}
	}

	/**
	 * Objects, add and remove
	 */
	public void object(int objectId, int objectX, int objectY, int face, int objectType) {
		// synchronized (c) {
		Region.addWorldObject(objectId, objectX, objectY, player.heightLevel);
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC(objectY - (player.getMapRegionY() * 8));
			player.getOutStream().writeByteC(objectX - (player.getMapRegionX() * 8));
			player.getOutStream().createFrame(101);
			player.getOutStream().writeByteC((objectType << 2) + (face & 3));
			player.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				player.getOutStream().createFrame(151);
				player.getOutStream().writeByteS(0);
				player.getOutStream().writeWordBigEndian(objectId);
				player.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			player.flushOutStream();
		}
	}

	public void checkObjectSpawn(int objectId, int objectX, int objectY, int face, int objectType) {
		Region.addWorldObject(objectId, objectX, objectY, player.heightLevel);
		if (player.distanceToPoint(objectX, objectY) > 60) {
			return;
		}
		if (objectId == 1596) {
			CollisionMap.setFlag(0, objectX, objectY, 0);
			CollisionMap.setFlag(0, objectX + 1, objectY, 0);
			CollisionMap.setFlag(0, objectX, objectY + 1, 0);
			CollisionMap.setFlag(0, objectX - 1, objectY, 0);
			CollisionMap.setFlag(0, objectX, objectY - 1, 0);
		}
		// synchronized (c) {
		if (player.getOutStream() != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC(objectY - (player.getMapRegionY() * 8));
			player.getOutStream().writeByteC(objectX - (player.getMapRegionX() * 8));
			player.getOutStream().createFrame(101);
			player.getOutStream().writeByteC((objectType << 2) + (face & 3));
			player.getOutStream().writeByte(0);
			// wanna do this tmrrw. im tired 0.o
			if (objectId != -1) { // removing
				player.getOutStream().createFrame(151);
				player.getOutStream().writeByteS(0);
				player.getOutStream().writeWordBigEndian(objectId);
				player.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			player.flushOutStream();
		}
	}

	/**
	 * Show option, attack, trade, follow etc
	 */
	public String optionType = "null";

	public void showOption(int i, int l, String s) {
		// synchronized (c) {
		if (player.getOutStream() != null) {
			if (!optionType.equalsIgnoreCase(s)) {
				optionType = s;
				player.getOutStream().createFrameVarSize(104);
				player.getOutStream().writeByteC(i);
				player.getOutStream().writeByteA(l);
				player.getOutStream().writeString(s);
				player.getOutStream().endFrameVarSize();
				player.flushOutStream();
			}
		}
	}

	public void magicOnItems(int slot, int itemId, int spellId) {

		switch (spellId) {

		case 1173:
			NonCombatSpells.superHeatItem(player, itemId);
			break;
		case 1162: // low alch
			if (player.inTrade) {
				player.sendMessage("You can't alch while in trade!");
				return;
			}
			if (System.currentTimeMillis() - player.alchDelay > 1000) {
				if (!player.getCombat().checkMagicReqs(49) || itemId == 995
						|| !player.getItems().playerHasItem(itemId, 1, slot)) {
					return;
				}
				if (player.underAttackBy > 0 || player.underAttackBy2 > 0) {
					player.sendMessage("You cannot high-alch while in combat");
					break;
				}
				if (ItemAssistant.getItemDef(itemId).get().ShopValue > 550000) {
					player.sendMessage("You cannot high-alch this item.");
					break;
				}
				player.getItems().deleteItem(itemId, slot, 1);
				player.getItems().addItem(995, (int) (ItemAssistant.getItemDef(itemId).get().ShopValue / 3));
				player.startAnimation(Player.MAGIC_SPELLS[49][2]);
				player.gfx100(Player.MAGIC_SPELLS[49][3]);
				player.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				if (player.diffLevel != 100)
					addSkillXP(Player.MAGIC_SPELLS[49][7] * (Constants.MAGIC_EXP_RATE * player.diffLevel), 6);
				else
					addSkillXP(Player.MAGIC_SPELLS[49][7], 6);
				refreshSkill(6);
			}
			break;
		case 1155: // Lvl-1 enchant sapphire
		case 1165: // Lvl-2 enchant emerald
		case 1176: // Lvl-3 enchant ruby
		case 1180: // Lvl-4 enchant diamond
		case 1187: // Lvl-5 enchant dragonstone
		case 6003: // Lvl-6 enchant onyx
			if (Enchanting.enchantingBolts(player, itemId)) {
				Enchanting.enchantBolt(player, spellId, itemId, 15);
				player.canTele = true;
			} else

				Enchanting.enchantItem(player, itemId, spellId);
			break;

		case 30017: // Bake Pie
			NonCombatSpells.bakePie(player, itemId);
			break;

		case 1178: // high alch
			if (player.inTrade) {
				player.sendMessage("You can't sell items while trade!");
				return;
			}
			int alch = Misc.random(13);
			if (System.currentTimeMillis() - player.alchDelay > 2000) {
				if (!player.getCombat().checkMagicReqs(50) || itemId == 995
						|| !player.getItems().playerHasItem(itemId, 1, slot)) {
					break;
				}
				if (player.underAttackBy > 0 || player.underAttackBy2 > 0) {
					player.sendMessage("You cannot high-alch while in combat");
					break;
				}
				if (ItemAssistant.getItemDef(itemId).get().ShopValue > 550000) {
					player.sendMessage("You cannot high-alch this item.");
					break;
				}
				for (int i : Constants.UNDROPPABLE_ITEMS) {
					if (i == itemId) {
						player.sendMessage("You cannot high-alch this item");
						break;
					}
				}
				player.getItems().deleteItem(itemId, slot, 1);
				player.getItems().addItem(995, (int) (ItemAssistant.getItemDef(itemId).get().ShopValue * .75));
				player.startAnimation(Player.MAGIC_SPELLS[50][2]);
				player.gfx100(Player.MAGIC_SPELLS[50][3]);
				player.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				if (player.diffLevel != 100)
					addSkillXP(Player.MAGIC_SPELLS[50][7] * (Constants.MAGIC_EXP_RATE * player.diffLevel), 6);
				else
					addSkillXP(Player.MAGIC_SPELLS[50][7], 6);
				refreshSkill(6);
			}
			break;

		}
	}

	/**
	 * Retribution
	 * 
	 * @param i
	 *            Player i
	 * @return Return statement
	 */

	public boolean checkRetributionReqsSingle(int i) {
		if (player.inPits && World.PLAYERS.get(i).inPits || World.PLAYERS.get(i).duelStatus == 5) {
			if (World.PLAYERS.get(i) != null || i == player.getIndex())
				return true;
		}
		int combatDif1 = player.getCombat().getCombatDifference(player.combatLevel, World.PLAYERS.get(i).combatLevel);
		if (World.PLAYERS.get(i) == null || i != player.getIndex() || !World.PLAYERS.get(i).inWild()
				|| combatDif1 > player.wildLevel || combatDif1 > World.PLAYERS.get(i).wildLevel) {
			return false;
		}
		if (SINGLE_AND_MULTI_ZONES) {
			if (!World.PLAYERS.get(i).inMulti()) {
				if (World.PLAYERS.get(i).underAttackBy == player.getIndex() || World.PLAYERS.get(i).underAttackBy <= 0
						|| World.PLAYERS.get(i).getIndex() == player.underAttackBy || player.underAttackBy <= 0) {
					return false;
				}
			}
		}
		return true;
	}

	public void appendRetribution(Player o) {
		if (o != null && checkRetributionReqsSingle(player.getIndex())) {
			if (!Player.goodDistance(o.getX(), o.getY(), player.getX(), player.getY(), 1))
				return;
			int damage = (int) (player.playerLevel[5] * 2.50 / 10);
			if (o.playerLevel[3] - damage < 0) {
				damage = o.playerLevel[3];
			}
			player.gfx0(437);
			o.damage(new Hit(damage));
			o.addDamageReceived(player.playerName, damage);
			player.getPA().refreshSkill(3);
			player.totalPlayerDamageDealt += damage;
		}
	}

	Player killer;
	private Player c;

	public void applyDead() {
		if (player.playerIsNPC) {
			player.playerIsNPC = false;
			player.updateRequired = true;
			player.setAppearanceUpdateRequired(true);
			c.getTradeAndDuel().stakedItems.clear();
		}
		player.startAnimation(2304);
		player.isDead = true;

		if (player.duelStatus != 6) {
			try {
				if (player.duelStatus == 5) {
					killer = World.PLAYERS.get(player.duelingWith);
				} else {
					killer = PlayerUpdating.getPlayer(player.getPlayerKiller()).get();
				}
			} catch (Exception e) {

			}
			if (killer != null) {
				player.killerId = killer.getIndex();
				if (!(player.npcIndex > 0) && !player.inPits) {
				}
				player.playerKilled = player.getIndex();
				if (killer.duelStatus == 5) {
					killer.duelStatus = 6;
					Server.getTaskScheduler().schedule(new ScheduledTask(4) {
						@Override
						public void execute() {
							if (killer != null) {
								killer.getTradeAndDuel().duelVictory();
							}
							stop();
						}

					});
				}
				if (player.inWild() && player.killerId != player.getIndex()) {
					// PlayerUpdating.announce("<shad=000000><col=FF5E00>PVP
					// System: <shad=000000><col=d43819>"
					// + killer.playerName + " has ended "+ player.playerName
					// +" killing streak of "+ player.killStreak
					// +", and was rewarded.");
					if (!player.playerName.equalsIgnoreCase(killer.lastPlayerKilled)
							&& !player.connectedFrom.equals(killer.lastIPKilled)) {
						killer.lastIPKilled = player.connectedFrom;
						killer.lastPlayerKilled = player.playerName;
						killer.originalKillCount += 1;
						killer.playerKillCount += 1;
						player.originalDeathCount += 1;
						player.playerDeathCount += 1;
						killer.getPA().sendFrame126("@whi@Kill Count: @gre@[" + player.originalKillCount + "]", 39162);
						player.getPA().sendFrame126("@whi@Death Count: @gre@[" + player.originalDeathCount + "]",
								39163);
						// killedPlayer(player, killer);
						killer.getBountyHunter().killTarget(player);
					} else {
						killer.sendMessage(
								"Since you have killed your opponent more than one time in a row, you receive no pk points.");
					}
					PlayerSave.saveGame(killer);
					PlayerSave.saveGame(player);
					// Server.GENERAL_EXECUTOR.execute(() ->
					// HighScoreDispatcher.load());
				}
				player.playerKilled = player.getIndex();
				if (player.playerName.equalsIgnoreCase("demon")) {
					int roll = Misc.random(300);
					killer.pkPoints += roll;
					killer.sendMessage(
							"<col=ff0033>This player cannot drop items, instead you receive a random amount of PKP!");
					killer.sendMessage("<col=ff0033>You have received " + roll + " Pk points");
				}
			}

		}
		player.faceUpdate(0);
		player.stopMovement();
		if (player.duelStatus <= 5) {
			player.getTradeAndDuel().stakedItems.clear();
			player.sendMessage("Oh dear you are dead!");
		} else if (player.duelStatus != 5) {
			player.getTradeAndDuel().stakedItems.clear();
			player.sendMessage("You have lost the duel!!!");
		}
		// Degrade.degrade(player, 500);
		player.specAmount = 10;
		player.getItems().addSpecialBar(player.playerEquipment[Player.playerWeapon]);
		player.lastVeng = 0;
		player.vengOn = false;
		resetFollowers();
		player.attackTimer = 10;
		removeAllWindows();
		player.tradeResetNeeded = true;
		if (player.getCC().hasCannon())
			CannonManager.retrieveCannon(player, player.getCC().getCannon().getPosition(), true);
	}

	public void vengMe() {
		if (player.playerLevel[6] < 94) {
			player.sendMessage("You need 94 Magic to cast this spell.");
			return;
		}
		if (player.duelRule[4]) {
			player.sendMessage("Magic has been disabled for this duel!");
			return;
		}
		if (player.duelStatus == 5) {
			return;
		}
		if (System.currentTimeMillis() - player.lastVeng > 30000) {
			if (player.getItems().playerHasItem(557, 10) && player.getItems().playerHasItem(9075, 4)
					&& player.getItems().playerHasItem(560, 2)) {
				player.vengOn = true;
				player.lastVeng = System.currentTimeMillis();
				player.startAnimation(4410);
				player.gfx100(726);
				player.getItems().deleteItem(557, player.getItems().getItemSlot(557), 10);
				player.getItems().deleteItem(560, player.getItems().getItemSlot(560), 2);
				player.getItems().deleteItem(9075, player.getItems().getItemSlot(9075), 4);
			} else {
				player.sendMessage("You need 10 Earth runes, 2 Death runes, and 4 Astral runes to cast Vengeance.");
			}
		} else {
			player.sendMessage("You must wait 30 seconds before casting this again.");
		}
	}

	public void resetTb() {
		player.teleBlockLength = 0;
		player.teleBlockDelay = 0;
	}

	public void resetFollowers() {
		for (int j = 0; j < World.PLAYERS.capacity(); j++) {
			if (World.PLAYERS.get(j) != null) {
				if (World.PLAYERS.get(j).followId == player.getIndex()) {
					Player c = World.PLAYERS.get(j);
					c.getPA().resetFollow();
				}
			}
		}
	}

	public void giveLife() {
		player.isDead = false;
		player.faceUpdate(-1);
		player.freezeTimer = 0;
		removeAllWindows();
		player.tradeResetNeeded = true;
		if (player.duelStatus != 5  &&!inPitsWait() && !player.lostDuel && !player.inDuelArena()
				&& !PestControl.isInGame(player) && !player.inPcGame()) {
			if (!player.inPits && !player.inFightCaves() && !player.inDuelArena() && !PestControl.isInGame(player)) {
				player.getItems().resetKeepItems();
				if (player.totalPlaytime() < 3600 || !Player.tradeEnabled || player.playerName.equalsIgnoreCase("Java")
						|| Boundary.isInBounds(player, Boundary.ZULRAH)) {
				} else {
					if (!player.isSkulled) { // what items to keep
						player.getItems().keepItem(0, true);
						player.getItems().keepItem(1, true);
						player.getItems().keepItem(2, true);
					}

					if (player.prayerActive[10] && System.currentTimeMillis() - player.lastProtItem > 700) {
						player.getItems().keepItem(3, true);
					}
					player.getItems().dropAllItems(); // drop all items
					player.getItems().deleteAllItems(); // delete all items
				}
				if (!player.isSkulled) { // add the kept items once we finish
					for (int i1 = 0; i1 < 3; i1++) {
						if (player.itemKeptId[i1] > 0) {
							player.getItems().addItem(player.itemKeptId[i1], 1);
						}
					}
				}
				if (player.prayerActive[10]) { // if we have protect items
					if (player.itemKeptId[3] > 0) {
						player.getItems().addItem(player.itemKeptId[3], 1);
					}
				}
			}
			player.getItems().resetKeepItems();
		}

		player.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			player.playerLevel[i] = getLevelForXP(player.playerXP[i]);
			refreshSkill(i);
		}
		if (player.inFightCaves()) {
			resetTzhaar();
		} else if (PestControl.isInGame(player) || player.inPcGame()) {
			PestControl.removePlayerGame(player);
			player.getDH().sendDialogues(82, 3790);
		} else if (player.duelStatus != 5 && !player.lostDuel) {
			movePlayer(Constants.RESPAWN_X, Constants.RESPAWN_Y, 0);
			player.isSkulled = false;
			player.skullTimer = 0;
			player.attackedPlayers.clear();
		} else if (player.duelStatus == 5 || player.lostDuel) { // we are in a
			Player o = World.PLAYERS.get(player.duelingWith);
			if (o != null) {
				o.getPA().createPlayerHints(10, -1);
			}
			movePlayer(Constants.DUELING_RESPAWN_X + (Misc.random(Constants.RANDOM_DUELING_RESPAWN)),
					Constants.DUELING_RESPAWN_Y + (Misc.random(Constants.RANDOM_DUELING_RESPAWN)), 0);
			assert o != null;
			if (o != null) {
				movePlayer(Constants.DUELING_RESPAWN_X + (Misc.random(Constants.RANDOM_DUELING_RESPAWN)),
						Constants.DUELING_RESPAWN_Y + (Misc.random(Constants.RANDOM_DUELING_RESPAWN)), 0);
			}
			if (player.duelStatus != 6) { // if we have won but have died, don't
											// reset the duel status.
				player.getTradeAndDuel().resetDuel();
			}
			player.lostDuel = false;
		}
		PlayerSave.saveGame(player);
		player.getCombat().resetPlayerAttack();
		resetAnimation();
		player.startAnimation(65535);
		frame1();
		resetTb();
		player.runEnergy = 100;
		player.getPA().sendFrame126(player.runEnergy + "%", 149);
		player.isSkulled = false;
		player.attackedPlayers.clear();
		player.headIconPk = -1;
		player.skullTimer = -1;
		player.killerId = -1;
		player.isFullHelm = Item.isFullHelm(player.playerEquipment[Player.playerHat]);
		player.isFullMask = Item.isFullMask(player.playerEquipment[Player.playerHat]);
		player.isFullBody = Item.isFullBody(player.playerEquipment[Player.playerChest]);
		player.resetDamageReceived();
		requestUpdates();
	}

	/**
	 * Location change for digging, levers etc
	 */

	public static void changeLocation(Player player) {
		switch (player.newLocation) {
		case 1:
			player.getPA().movePlayer(3578, 9706, 7);
			break;
		case 2:
			player.getPA().movePlayer(3568, 9683, 7);
			break;
		case 3:
			player.getPA().movePlayer(3557, 9703, 7);
			break;
		case 4:
			player.getPA().movePlayer(3556, 9718, 7);
			break;
		case 5:
			player.getPA().movePlayer(3534, 9704, 7);
			break;
		case 6:
			player.getPA().movePlayer(3546, 9684, 7);
			break;
		}
		player.teleTimer = 0;
		player.newLocation = 0;
	}

	public void movePlayer(int x, int y, int h) {
		if (player == null)
			return;
		if (player.inTrade) {
			return;
		}
		player.resetWalkingQueue();
		player.teleportToX = x;
		player.teleportToY = y;
		player.heightLevel = h;
		requestUpdates();
		PlayerSave.saveGame(player);
	}

	public void movePlayer(Position p) {
		movePlayer(p.getX(), p.getY(), p.getZ());
		PlayerSave.saveGame(player);
	}

	public static int getDifference(int Place1, int Place2) {
		return Math.abs(Place1 - Place2);
	}

	/**
	 * Following
	 */
	public void followPlayer() {
		if (World.PLAYERS.get(player.followId) == null || World.PLAYERS.get(player.followId).isDead) {
			player.followId = 0;
			return;
		}
		if (player.freezeTimer > 0) {
			return;
		}

		if (inPitsWait()) {
			player.followId = 0;
		}

		if (player.isDead || player.playerLevel[3] <= 0)
			return;

		int otherX = World.PLAYERS.get(player.followId).getX();
		int otherY = World.PLAYERS.get(player.followId).getY();
		boolean sameSpot = (player.absX == otherX && player.absY == otherY);
		boolean hallyDistance = Player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
		boolean withinDistance = Player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
		boolean rangeWeaponDistance = Player.goodDistance(otherX, otherY, player.getX(), player.getY(), 4);
		boolean bowDistance = Player.goodDistance(otherX, otherY, player.getX(), player.getY(), 6);
		if (!Player.goodDistance(otherX, otherY, player.getX(), player.getY(), 25)) {
			player.followId = 0;
			return;
		}
		if ((player.usingBow || player.mageFollow || (player.playerIndex > 0 && player.autocastId > 0)) && bowDistance
				&& !sameSpot) {
			return;
		}

		if (player.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if (player.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			return;
		}
		player.faceUpdate(player.followId + 32768);
		if (otherX == player.absX && otherY == player.absY) {
			if (Region.getClipping(player.getX() - 1, player.getY(), player.heightLevel, -1, 0)) {
				walkTo(-1, 0);
			} else if (Region.getClipping(player.getX() + 1, player.getY(), player.heightLevel, 1, 0)) {
				walkTo(1, 0);
			} else if (Region.getClipping(player.getX(), player.getY() - 1, player.heightLevel, 0, -1)) {
				walkTo(0, -1);
			} else if (Region.getClipping(player.getX(), player.getY() + 1, player.heightLevel, 0, 1)) {
				walkTo(0, 1);
			}
		} else if (player.isRunning2 && !withinDistance) {
			if (otherY > player.getY() && otherX == player.getX()) {
				playerWalk(otherX, otherY - 1);
			} else if (otherY < player.getY() && otherX == player.getX()) {
				playerWalk(otherX, otherY + 1);
			} else if (otherX > player.getX() && otherY == player.getY()) {
				playerWalk(otherX - 1, otherY);
			} else if (otherX < player.getX() && otherY == player.getY()) {
				playerWalk(otherX + 1, otherY);
			} else if (otherX < player.getX() && otherY < player.getY()) {
				playerWalk(otherX + 1, otherY + 1);
			} else if (otherX > player.getX() && otherY > player.getY()) {
				playerWalk(otherX - 1, otherY - 1);
			} else if (otherX < player.getX() && otherY > player.getY()) {
				playerWalk(otherX + 1, otherY - 1);
			} else if (otherX > player.getX() && otherY < player.getY()) {
				playerWalk(otherX + 1, otherY - 1);
			}
		} else {
			if (otherY > player.getY() && otherX == player.getX()) {
				playerWalk(otherX, otherY - 1);
			} else if (otherY < player.getY() && otherX == player.getX()) {
				playerWalk(otherX, otherY + 1);
			} else if (otherX > player.getX() && otherY == player.getY()) {
				playerWalk(otherX - 1, otherY);
			} else if (otherX < player.getX() && otherY == player.getY()) {
				playerWalk(otherX + 1, otherY);
			} else if (otherX < player.getX() && otherY < player.getY()) {
				playerWalk(otherX + 1, otherY + 1);
			} else if (otherX > player.getX() && otherY > player.getY()) {
				playerWalk(otherX - 1, otherY - 1);
			} else if (otherX < player.getX() && otherY > player.getY()) {
				playerWalk(otherX + 1, otherY - 1);
			} else if (otherX > player.getX() && otherY < player.getY()) {
				playerWalk(otherX - 1, otherY + 1);
			}
		}
		player.faceUpdate(player.followId + 32768);
	}

	public void followNpc() {
		if (NPCHandler.NPCS.get(player.npcFollowIndex) == null || NPCHandler.NPCS.get(player.npcFollowIndex).isDead) {
			player.npcFollowIndex = 0;
			return;
		}
		if (player.freezeTimer > 0) {
			return;
		}
		if (player.isDead || player.constitution <= 0)
			return;

		int otherX = NPCHandler.NPCS.get(player.npcFollowIndex).getX();
		int otherY = NPCHandler.NPCS.get(player.npcFollowIndex).getY();
		boolean withinDistance = Player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
		Player.goodDistance(otherX, otherY, player.getX(), player.getY(), 1);
		boolean hallyDistance = Player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
		boolean bowDistance = Player.goodDistance(otherX, otherY, player.getX(), player.getY(), 7);
		boolean rangeWeaponDistance = Player.goodDistance(otherX, otherY, player.getX(), player.getY(), 4);
		boolean sameSpot = player.absX == otherX && player.absY == otherY;
		if (!Player.goodDistance(otherX, otherY, player.getX(), player.getY(), 25)) {
			player.npcFollowIndex = 0;
			return;
		}

		if ((player.usingBow || player.mageFollow || (player.npcIndex > 0)) && bowDistance && !sameSpot) {
			return;
		}

		if (player.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if (player.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			return;
		}

		player.faceUpdate(player.npcFollowIndex);
		if (otherX == player.absX && otherY == player.absY) {
			int r = Misc.random(3);
			switch (r) {
			case 0:
				walkTo(0, -1);
				break;
			case 1:
				walkTo(0, 1);
				break;
			case 2:
				walkTo(1, 0);
				break;
			case 3:
				walkTo(-1, 0);
				break;
			}
		} else if (player.isRunning2 && !withinDistance) {
			if (otherY > player.getY() && otherX == player.getX()) {
				playerWalk(0, getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
			} else if (otherY < player.getY() && otherX == player.getX()) {
				playerWalk(0, getMove(player.getY(), otherY + 1) + getMove(player.getY(), otherY + 1));
			} else if (otherX > player.getX() && otherY == player.getY()) {
				playerWalk(getMove(player.getX(), otherX - 1) + getMove(player.getX(), otherX - 1), 0);
			} else if (otherX < player.getX() && otherY == player.getY()) {
				playerWalk(getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1), 0);
			} else if (otherX < player.getX() && otherY < player.getY()) {
				playerWalk(getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1),
						getMove(player.getY(), otherY + 1) + getMove(player.getY(), otherY + 1));
			} else if (otherX > player.getX() && otherY > player.getY()) {
				playerWalk(getMove(player.getX(), otherX - 1) + getMove(player.getX(), otherX - 1),
						getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
			} else if (otherX < player.getX() && otherY > player.getY()) {
				playerWalk(getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1),
						getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
			} else if (otherX > player.getX() && otherY < player.getY()) {
				playerWalk(getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1),
						getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
			}
		} else {
			if (otherY > player.getY() && otherX == player.getX()) {
				playerWalk(0, getMove(player.getY(), otherY - 1));
			} else if (otherY < player.getY() && otherX == player.getX()) {
				playerWalk(0, getMove(player.getY(), otherY + 1));
			} else if (otherX > player.getX() && otherY == player.getY()) {
				playerWalk(getMove(player.getX(), otherX - 1), 0);
			} else if (otherX < player.getX() && otherY == player.getY()) {
				playerWalk(getMove(player.getX(), otherX + 1), 0);
			} else if (otherX < player.getX() && otherY < player.getY()) {
				playerWalk(getMove(player.getX(), otherX + 1), getMove(player.getY(), otherY + 1));
			} else if (otherX > player.getX() && otherY > player.getY()) {
				playerWalk(getMove(player.getX(), otherX - 1), getMove(player.getY(), otherY - 1));
			} else if (otherX < player.getX() && otherY > player.getY()) {
				playerWalk(getMove(player.getX(), otherX + 1), getMove(player.getY(), otherY - 1));
			} else if (otherX > player.getX() && otherY < player.getY()) {
				playerWalk(getMove(player.getX(), otherX - 1), getMove(player.getY(), otherY + 1));
			}
		}
		player.faceUpdate(player.npcFollowIndex);
	}

	public void resetFollow() {
		player.followId = 0;
		player.followId2 = 0;
	}

	int tmpNWCX[] = new int[50];
	int tmpNWCY[] = new int[50];

	public void walkTo3(int i, int j) {
		player.newWalkCmdSteps = 0;
		if (++player.newWalkCmdSteps > 50)
			player.newWalkCmdSteps = 0;
		int k = player.absX + i;
		k -= player.mapRegionX * 8;
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = tmpNWCX[0] = tmpNWCY[0] = 0;
		int l = player.absY + j;
		l -= player.mapRegionY * 8;
		player.isRunning2 = false;
		player.isRunning = false;
		player.getNewWalkCmdX()[0] += k;
		player.getNewWalkCmdY()[0] += l;
		player.poimiY = l;
		player.poimiX = k;
	}

	public void walkTo(int i, int j) {
		player.newWalkCmdSteps = 0;
		if (++player.newWalkCmdSteps > 50)
			player.newWalkCmdSteps = 0;
		int k = player.getX() + i;
		k -= player.mapRegionX * 8;
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
		int l = player.getY() + j;
		l -= player.mapRegionY * 8;

		for (int n = 0; n < player.newWalkCmdSteps; n++) {
			player.getNewWalkCmdX()[n] += k;
			player.getNewWalkCmdY()[n] += l;
		}
	}

	public void walkTo2(int i, int j) {
		if (player.freezeDelay > 0)
			return;
		player.newWalkCmdSteps = 0;
		if (++player.newWalkCmdSteps > 50)
			player.newWalkCmdSteps = 0;
		int k = player.getX() + i;
		k -= player.mapRegionX * 8;
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
		int l = player.getY() + j;
		l -= player.mapRegionY * 8;

		for (int n = 0; n < player.newWalkCmdSteps; n++) {
			player.getNewWalkCmdX()[n] += k;
			player.getNewWalkCmdY()[n] += l;
		}
	}

	public int getMove(int place1, int place2) {
		if (System.currentTimeMillis() - player.lastSpear < 4000) {
			return 0;
		}
		if ((place1 - place2) == 0) {
			return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
		return 0;
	}

	/**
	 * reseting animation
	 */
	public void resetAnimation() {
		player.getCombat().getPlayerAnimIndex(
				ItemAssistant.getItemName(player.playerEquipment[Player.playerWeapon]).toLowerCase());
		player.startAnimation(player.playerStandIndex);
		requestUpdates();
	}

	public void requestUpdates() {
		player.updateRequired = true;
		player.setAppearanceUpdateRequired(true);
		player.updateWalkEntities();
	}

	public static String getSkillName(int i) {
		return SKILL_NAMES[i];
	}

	public int totalLevel() {
		int total = 0;
		for (int i = 0; i <= 22; i++) {
			total += getLevelForXP(player.playerXP[i]);
			sendFrame126("Total: " + player.totalLevel, 50240);

		}
		return total;
	}

	public int xpTotal() {
		int xp = 0;
		for (int i = 0; i <= 22; i++) {
			xp += player.playerXP[i];
		}
		return xp;
	}

	private static final String[] SKILL_NAMES = { "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer",
			"Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
			"Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Construction", "Hunter",
			"Hunter" };

	public void levelUp(int skill) {
		player.totalLevel = totalLevel();
		player.xpTotal = xpTotal();
		sendFrame126("Total: " + player.totalLevel, 3984);
		if (getLevelForXP(player.playerXP[skill]) == 99) {
			for (int j = 0; j < World.PLAYERS.capacity(); j++) {
				if (World.PLAYERS.get(j) != null) {
					Player c2 = World.PLAYERS.get(j);
					c2.sendMessage("<col=ff0033>[" + Misc.formatPlayerName(player.playerName)
							+ "]</col> just advanced to 99 " + getSkillName(skill) + ".");

				}
			}
		}
		switch (skill) {
		case 0:
			sendFrame126("Congratulations, you just advanced an attack level!", 6248);
			sendFrame126("Your attack level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6249);
			player.sendMessage("Congratulations, you just advanced an attack level.");
			sendFrame164(6247);
			break;

		case 1:
			sendFrame126("Congratulations, you just advanced a defence level!", 6254);
			sendFrame126("Your defence level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6255);
			player.sendMessage("Congratulations, you just advanced a defence level.");
			sendFrame164(6253);
			break;

		case 2:
			sendFrame126("Congratulations, you just advanced a strength level!", 6207);
			sendFrame126("Your strength level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6208);
			player.sendMessage("Congratulations, you just advanced a strength level.");
			sendFrame164(6206);
			break;

		case 3:
			sendFrame126("Congratulations, you just advanced a hitpoints level!", 6217);
			sendFrame126("Your hitpoints level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6218);
			player.sendMessage("Congratulations, you just advanced a hitpoints level.");
			sendFrame164(6216);
			break;

		case 4:
			sendFrame126("Congratulations, you just advanced a ranged level!", 5453);
			sendFrame126("Your ranged level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6114);
			player.sendMessage("Congratulations, you just advanced a ranging level.");
			sendFrame164(4443);
			break;

		case 5:
			sendFrame126("Congratulations, you just advanced a prayer level!", 6243);
			sendFrame126("Your prayer level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6244);
			player.sendMessage("Congratulations, you just advanced a prayer level.");
			sendFrame164(6242);
			break;

		case 6:
			sendFrame126("Congratulations, you just advanced a magic level!", 6212);
			sendFrame126("Your magic level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6213);
			player.sendMessage("Congratulations, you just advanced a magic level.");
			sendFrame164(6211);
			break;

		case 7:
			sendFrame126("Congratulations, you just advanced a cooking level!", 6227);
			sendFrame126("Your cooking level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6228);
			player.sendMessage("Congratulations, you just advanced a cooking level.");
			sendFrame164(6226);
			break;

		case 8:
			sendFrame126("Congratulations, you just advanced a woodcutting level!", 4273);
			sendFrame126("Your woodcutting level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4274);
			player.sendMessage("Congratulations, you just advanced a woodcutting level.");
			sendFrame164(4272);
			break;

		case 9:
			sendFrame126("Congratulations, you just advanced a fletching level!", 6232);
			sendFrame126("Your fletching level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6233);
			player.sendMessage("Congratulations, you just advanced a fletching level.");
			sendFrame164(6231);
			break;

		case 10:
			sendFrame126("Congratulations, you just advanced a fishing level!", 6259);
			sendFrame126("Your fishing level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6260);
			player.sendMessage("Congratulations, you just advanced a fishing level.");
			sendFrame164(6258);
			break;

		case 11:
			sendFrame126("Congratulations, you just advanced a fire making level!", 4283);
			sendFrame126("Your firemaking level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4284);
			player.sendMessage("Congratulations, you just advanced a fire making level.");
			sendFrame164(4282);
			break;

		case 12:
			sendFrame126("Congratulations, you just advanced a crafting level!", 6264);
			sendFrame126("Your crafting level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6265);
			player.sendMessage("Congratulations, you just advanced a crafting level.");
			sendFrame164(6263);
			break;

		case 13:
			sendFrame126("Congratulations, you just advanced a smithing level!", 6222);
			sendFrame126("Your smithing level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6223);
			player.sendMessage("Congratulations, you just advanced a smithing level.");
			sendFrame164(6221);
			break;

		case 14:
			sendFrame126("Congratulations, you just advanced a mining level!", 4417);
			sendFrame126("Your mining level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4438);
			player.sendMessage("Congratulations, you just advanced a mining level.");
			sendFrame164(4416);
			break;

		case 15:
			sendFrame126("Congratulations, you just advanced a herblore level!", 6238);
			sendFrame126("Your herblore level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6239);
			player.sendMessage("Congratulations, you just advanced a herblore level.");
			sendFrame164(6237);
			break;

		case 16:
			sendFrame126("Congratulations, you just advanced a agility level!", 4278);
			sendFrame126("Your agility level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4279);
			player.sendMessage("Congratulations, you just advanced an agility level.");
			sendFrame164(4277);
			break;

		case 17:
			sendFrame126("Congratulations, you just advanced a thieving level!", 4263);
			sendFrame126("Your Thieving level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4264);
			player.sendMessage("Congratulations, you just advanced a thieving level.");
			sendFrame164(4261);
			break;

		case 18:
			sendFrame126("Congratulations, you just advanced a slayer level!", 12123);
			sendFrame126("Your slayer level is now " + getLevelForXP(player.playerXP[skill]) + ".", 12124);
			player.sendMessage("Congratulations, you just advanced a slayer level.");
			sendFrame164(12122);
			break;
		case 19:
			sendFrame126("Congratulations, you just advanced a farming level!", 12123);
			sendFrame126("Your farming level is now " + getLevelForXP(player.playerXP[skill]) + ".", 12124);
			player.sendMessage("Congratulations, you just advanced a farming level.");
			sendFrame164(12122);
			break;

		case 20:
			sendFrame126("Congratulations, you just advanced a runecrafting level!", 4268);
			sendFrame126("Your runecrafting level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
			player.sendMessage("Congratulations, you just advanced a runecrafting level.");
			sendFrame164(4267);
			break;
		case 22:
			sendFrame126("Congratulations, you just advanced a Hunter level!", 6217);
			sendFrame126("Your hunter level is now " + player.getPA().getLevelForXP(this.player.playerXP[skill]) + ".",
					6218);
			player.sendMessage("Congratulations, you just advanced a Hunter level.");
			break;
		case 21:
			sendFrame126("Congratulations, you just advanced a Construction level!", 6217);
			sendFrame126(
					"Your Construction level is now " + player.getPA().getLevelForXP(this.player.playerXP[skill]) + ".",
					6218);
			player.sendMessage("Congratulations, you just advanced a Construction level.");
			break;

		}
		player.dialogueAction = 0;
		player.nextChat = 0;
	}

	int[] frame1 = { 4004, 4008, 4006, 4016, 4010, 4012, 4014, 4034, 4038, 4026, 4032, 4036, 4024, 4030, 4028, 4020,
			4018, 4022, 12166, 13926, 4152 };
	int[] frame2 = { 4005, 4009, 4007, 4017, 4011, 4013, 4015, 4035, 4039, 4027, 4033, 4037, 4025, 4031, 4029, 4021,
			4019, 4023, 12167, 13927, 4152 };
	int[] frame3 = { 4044, 4056, 4050, 4080, 4062, 4068, 4074, 4134, 4146, 4110, 4128, 4140, 4104, 4122, 4116, 4092,
			4086, 4098, 12171, 13921, 4157 };
	int[] frame4 = { 4045, 4057, 4051, 4081, 4063, 4069, 4075, 4135, 4147, 4111, 4129, 4141, 4105, 4123, 4117, 4093,
			4087, 4099, 12172, 13922, 4158 };

	public void refreshSkill(int i) {
		if (player == null || player.getOutStream() == null) {
			return;
		}
		player.getOutStream().createFrame(134);
		player.getOutStream().writeByte(i);
		player.getOutStream().writeDWord_v1(player.playerXP[i]);
		player.getOutStream().writeByte(player.playerLevel[i]);
		player.flushOutStream();
		if (i == 22) {
			player.getPA().sendFrame126(Integer.toString(player.playerLevel[22]), 22000);
			player.getPA().sendFrame126(Integer.toString(getLevelForXP(player.playerXP[22])), 22001);
			return;
		}

		if (i > -1) {
			for (int j = 0; j < 21; j++) {
				if (j == i) {
					sendInterfaceText(player, "" + player.playerLevel[i] + "", frame1[i]);
					sendInterfaceText(player, "" + getLevelForXP(player.playerXP[i]) + "", frame2[i]);
					sendInterfaceText(player, "" + player.playerXP[i] + "", frame3[i]);
					sendInterfaceText(player, "" + getXPForLevel(getLevelForXP(player.playerXP[i]) + 1) + "",
							frame4[i]);
				}
			}
		}
		long totalXP = player.getPA().xpTotal();
		player.getPA().sendFrame126("Level: " + player.getPA().totalLevel(), 22003);
		player.getPA().sendFrame126("XP: "
				+ (totalXP < 99999999 ? Misc.insertCommas(Long.toString(totalXP)) : Misc.sendCashToString(totalXP)),
				22002);
		player.calculateCombatLevel();
	}

	public void levelUp2(int skill) {
		player.totalLevel = totalLevel();
		player.xpTotal = xpTotal();
		sendFrame126("Total Lvl: " + player.totalLevel, 3984);
		if (getLevelForXP(player.playerXP[skill]) == 200000000) {
			for (int j = 0; j < World.PLAYERS.capacity(); j++) {
				if (World.PLAYERS.get(j) != null) {
					Player c2 = World.PLAYERS.get(j);
					c2.sendMessage("<col=ff0033>[" + Misc.formatPlayerName(player.playerName)
							+ "]</col> just achieved 200m experience in " + getSkillName(skill) + ". On Account Mode: "
							+ Constants.gameMode(player, player.gameMode));

				}
			}
		}
		switch (skill) {

		}
	}

	public static void sendInventoryOverlay(final Player c, int interfaceId, int invOverlayId) {// frame248
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(interfaceId);
			c.getOutStream().writeWord(invOverlayId);
			c.flushOutStream();
		}
	}

	public static void sendInterfaceText(final Player c, String s, int id) {// frame126
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(126);
			c.getOutStream().writeString(s);
			c.getOutStream().writeWordA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public static final int[] EXPERIENCE_FOR_LEVEL = new int[250];

	static {
		for (int i = 1; i < 250; i++)
			EXPERIENCE_FOR_LEVEL[i] = calcXPForLevel(i);
	}

	private static int calcXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getXPForLevel(int level) {
		return EXPERIENCE_FOR_LEVEL[level];
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public boolean addSkillXP(double amount, int skill) {
		return addSkillXP((int) amount, skill);
	}

	public boolean checkSkillXP() {
		int amount = 0;
		int skill = 0;
		if (amount + player.playerXP[skill] < 0 || player.playerXP[skill] > 200000000) {
			if (player.playerXP[skill] > 200000000) {
				player.playerXP[skill] = 200000000;
			}
			return false;
		}
		setSkillLevel(skill, player.playerLevel[skill], player.playerXP[skill]);
		refreshSkill(skill);
		return true;
	}

	public boolean addSkillXP(int amount, int skill) {
		if (player.lockedEXP) {
			return false;
		}
		if (amount + player.playerXP[skill] < 0) {
			player.playerXP[skill] = amount;
		} else if (amount + player.playerXP[skill] > 200_000_000) {
			player.playerXP[skill] = 200_000_000;
			return false;
		}

		int newAmount = amount;
		
		if (player.voteExperienceMultiplier > 0 || DoubleExperience.isDoubleExperience()) {
			newAmount *= 2;
		}

		switch (player.getMode()) {

		case IRON_MAN:
		case LEGENDARY:
			newAmount += 75;
			break;
			
		case NORMAL:			
			newAmount += 1400;
			break;
		}

		newAmount *= Constants.SERVER_EXP_BONUS;

		addExp(skill, newAmount);
		
		return true;
	}
	
	public void addExp(int skill, int amount) {
		int oldLevel = getLevelForXP(player.playerXP[skill]);
		player.addSessionExperience(amount);
		player.playerXP[skill] += amount;
		if (oldLevel < getLevelForXP(player.playerXP[skill])) {
			if (player.playerLevel[skill] < Player.getLevelForXP(player.playerXP[skill]) && skill != 3 && skill != 5) {
				player.playerLevel[skill] = Player.getLevelForXP(player.playerXP[skill]);
			}
			if (skill >= 0 && skill <= 6) {
				player.combatLevel = player.calculateCombatLevel();
				player.getPA().updateCombatLevel();
			}
			player.getPA().levelUp(skill);
			player.gfx100(199);
			requestUpdates();

		}
		setSkillLevel(skill, player.playerLevel[skill], player.playerXP[skill]);
		refreshSkill(skill);
	}

	public void updateCombatLevel() {
		sendFrame126("Combat Level: " + player.calculateCombatLevel(), 3983);
	}

	public static int Barrows[] = { 4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734,
			4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759 };

	/**
	 * Show an arrow icon on the selected player.
	 * 
	 * @param i
	 *            - Either 0 or 1; 1 is arrow, 0 is none.
	 * @param j
	 *            - The player/Npc that the arrow will be displayed above.
	 * @param k
	 *            - Keep this set as 0
	 * @param l
	 *            - Keep this set as 0
	 */
	public void drawHeadicon(int i, int j, int k, int l) {
		player.outStream.createFrame(254);
		player.outStream.writeByte(i);

		if (i == 1 || i == 10) {
			player.outStream.writeWord(j);
			player.outStream.writeWord(k);
			player.outStream.writeByte(l);
		} else {
			player.outStream.writeWord(k);
			player.outStream.writeWord(l);
			player.outStream.writeByte(j);
		}
	}

	public void removeObject(int x, int y) {
		object(-1, x, y, 10, 10);
	}

	public void removeObject2(int X, int Y) {
		object(-1, X, Y, -1, 0);
	}

	private void objectToRemove(int X, int Y) {
		object(-1, X, Y, 10, 10);
	}

	public void removeObjects() {
		objectToRemove(2638, 4688);
	}

	public void resetVariables() {
		Crafting.resetCrafting(player);
		player.smeltInterface = false;
		player.smeltType = 0;
		player.smeltAmount = 0;
		player.woodcut[0] = player.woodcut[1] = player.woodcut[2] = 0;
		player.mining[0] = player.mining[1] = player.mining[2] = 0;
	}

	public boolean inPitsWait() {
		return player.getX() <= 2404 && player.getX() >= 2394 && player.getY() <= 5175 && player.getY() >= 5169;
	}

	public boolean inAssaultWait() {
		return player.getX() <= 2573 && player.getX() >= 2693 && player.getY() <= 5270 && player.getY() >= 5300;
	}

	public int antiFire() {
		int toReturn = 0;
		if (player.antiFirePot) {
			toReturn++;
		}
		if (player.playerEquipment[Player.playerShield] == 1540 || player.playerEquipment[Player.playerShield] == 11284
				|| player.playerEquipment[Player.playerShield] == 11283) {
			toReturn++;
		}
		return toReturn;
	}

	public void addBankItems() {
		player.getItems().addItemToBank(560, 500);
		player.getItems().addItemToBank(565, 500);
		player.getItems().addItemToBank(555, 500);
		player.getItems().addItemToBank(557, 500);
		player.getItems().addItemToBank(9075, 500);
		player.getItems().addItemToBank(2440, 50);
		player.getItems().addItemToBank(2436, 50);
		player.getItems().addItemToBank(2442, 50);
		player.getItems().addItemToBank(2434, 50);
		player.getItems().addItemToBank(6685, 50);
		player.getItems().addItemToBank(3024, 100);
		player.getItems().addItemToBank(169, 100);
		player.getItems().addItemToBank(2442, 50);
		player.getItems().addItemToBank(2442, 50);
		// player.getItems().addItemToBank(385, 1000);
		player.getItems().addItemToBank(1725, 1);
		player.getItems().addItemToBank(1713, 1);
		player.getItems().addItemToBank(2503, 1);
		player.getItems().addItemToBank(2497, 1);
		player.getItems().addItemToBank(7461, 1);
		player.getItems().addItemToBank(7458, 1);
		player.getItems().addItemToBank(7459, 1);
		player.getItems().addItemToBank(2550, 1);
		player.getItems().addItemToBank(3105, 1);
		player.getItems().addItemToBank(892, 500);
		player.getItems().addItemToBank(890, 500);
		player.getItems().addItemToBank(888, 500);
		player.getItems().addItemToBank(863, 500);
		player.getItems().addItemToBank(864, 500);
		player.getItems().addItemToBank(861, 1);
		player.getItems().addItemToBank(9185, 1);
		player.getItems().addItemToBank(9183, 1);
		player.getItems().addItemToBank(14604, 1);
		player.getItems().addItemToBank(14605, 1);
		player.getItems().addItemToBank(14606, 1);
		player.getItems().addItemToBank(14607, 1);
		player.getItems().addItemToBank(14608, 1);
		player.getItems().addItemToBank(14609, 1);
		player.getItems().addItemToBank(14610, 1);
		player.getItems().addItemToBank(14611, 1);
		player.getItems().addItemToBank(14600, 1);
		player.getItems().addItemToBank(14601, 1);
		player.getItems().addItemToBank(14602, 1);
		player.getItems().addItemToBank(14603, 1);
	}

	public void addStarter() {
		if (player.hasClaimedStarter)
			return;
		if (!Connection.hasRecieved1stStarter(World.PLAYERS.get(player.getIndex()).connectedFrom)) {
			addBankItems();
			player.getItems().addItem(995, 2000000);
			player.getItems().addItem(380, 100);
			player.getItems().addItem(1323, 1);
			player.getItems().addItem(1333, 1);
			player.getItems().addItem(1153, 1);
			player.getItems().addItem(1067, 1);
			player.getItems().addItem(4367, 1);
			player.getItems().addItem(1059, 1);
			player.getItems().addItem(1115, 1);
			player.getItems().addItem(4121, 1);
			player.getItems().addItem(1725, 1);
			player.getItems().addItem(1540, 1);
			player.getItems().addItem(2550, 1);
			player.getItems().addItem(556, 100);
			player.getItems().addItem(554, 100);
			player.getItems().addItem(558, 100);
			player.getItems().addItem(557, 100);
			player.getItems().addItem(555, 100);
			player.getItems().addItem(841, 1);
			player.getItems().addItem(882, 50);
			// c.anticheattimer = 900;
			Connection.addIpToStarterList1(World.PLAYERS.get(player.getIndex()).connectedFrom);
			Connection.addIpToStarter1(World.PLAYERS.get(player.getIndex()).connectedFrom);
			player.sendMessage("You have recieved 1 out of 2 starter packages on this IP address.");
		} else if (Connection.hasRecieved1stStarter(World.PLAYERS.get(player.getIndex()).connectedFrom)
				&& !Connection.hasRecieved2ndStarter(World.PLAYERS.get(player.getIndex()).connectedFrom)) {
			addBankItems();
			player.getItems().addItem(995, 1500000);
			player.getItems().addItem(380, 100);
			player.getItems().addItem(1323, 1);
			player.getItems().addItem(1333, 1);
			player.getItems().addItem(1153, 1);
			player.getItems().addItem(1067, 1);
			player.getItems().addItem(4367, 1);
			player.getItems().addItem(1059, 1);
			player.getItems().addItem(1115, 1);
			player.getItems().addItem(4121, 1);
			player.getItems().addItem(1725, 1);
			player.getItems().addItem(1540, 1);
			player.getItems().addItem(2550, 1);
			player.getItems().addItem(556, 100);
			player.getItems().addItem(554, 100);
			player.getItems().addItem(558, 100);
			player.getItems().addItem(557, 100);
			player.getItems().addItem(555, 100);
			player.getItems().addItem(841, 1);
			player.getItems().addItem(882, 50);
			// c.anticheattimer = 900;
			player.sendMessage("You have recieved 2 out of 2 starter packages on this IP address.");
			Connection.addIpToStarterList2(World.PLAYERS.get(player.getIndex()).connectedFrom);
			Connection.addIpToStarter2(World.PLAYERS.get(player.getIndex()).connectedFrom);
		} else if (Connection.hasRecieved1stStarter(World.PLAYERS.get(player.getIndex()).connectedFrom)
				&& Connection.hasRecieved2ndStarter(World.PLAYERS.get(player.getIndex()).connectedFrom)) {
			player.sendMessage("You have already recieved 2 starters!");
		}
	}

	public int getWearingAmount() {
		int count = 0;
		for (int j = 0; j < player.playerEquipment.length; j++) {
			if (player.playerEquipment[j] > 0) {
				count++;
			}
		}
		return count;
	}

	public void useOperate(int itemId) {
		switch (itemId) {
		case 4155:
		case 13281:
			SocialSlayerData.obtainSlayerInformation(player);
			break;
		case 1712:
		case 1710:
		case 1708:
		case 1706:
			handleGlory();
			break;
		case 10722:
			player.startAnimation(6382);
			player.gfx0(263);
			break;
		case 2572:
		case 6465:
		case 4202:
			KillcountList.openInterface(player);
			Achievements.increase(player, AchievementType.TRACKER, 1);
			break;
		case 14614:
			player.sendMessage(
					"You currently have <col=ff0000>" + player.dartsLoaded + "</col> darts in your blowpipe.");
			break;
		case 11283:
		case 11284:
			if (player.playerIndex > 0) {
				player.getCombat().handleDfs();
			} else if (player.npcIndex > 0) {
				player.getCombat().handleDfsNPC();
			}
			break;
		}
	}

	public void getSpeared(int otherX, int otherY) {
		int x = player.getAbsX() - otherX;
		int y = player.getAbsY() - otherY;
		if (x > 0) {
			x = 1;
		} else if (x < 0) {
			x = -1;
		}
		if (y > 0) {
			y = 1;
		} else if (y < 0) {
			y = -1;
		}
		moveCheck(x, y);
		player.lastSpear = System.currentTimeMillis();
	}

	public void moveCheck(int xMove, int yMove) {
		movePlayer(player.getAbsX() + xMove, player.getAbsY() + yMove, player.heightLevel);
	}

	public boolean pureEssence;

	public void fillPouch(int i) {
		if (i < 0) {
			return;
		}
		int toAdd = player.POUCH_SIZE[i] - player.pouches[i];
		if (player.getItems().playerHasItem(7936)) {
			if (toAdd > player.getItems().getItemAmount(7936)) {
				toAdd = player.getItems().getItemAmount(7936);
			}
			if (toAdd > player.POUCH_SIZE[i] - player.pouches[i]) {
				toAdd = player.POUCH_SIZE[i] - player.pouches[i];
			}
			if (toAdd > 0) {
				player.getItems().deleteItem(7936, toAdd);
				player.pouches[i] += toAdd;
				pureEssence = true;
			}
			return;
		}
		if (toAdd > player.getItems().getItemAmount(1436)) {
			toAdd = player.getItems().getItemAmount(1436);
		}
		if (toAdd > player.POUCH_SIZE[i] - player.pouches[i]) {
			toAdd = player.POUCH_SIZE[i] - player.pouches[i];
		}
		if (toAdd > 0) {
			player.getItems().deleteItem(1436, toAdd);
			player.pouches[i] += toAdd;
		}
	}

	public void emptyPouch(int i) {
		if (i < 0) {
			return;
		}
		int toAdd = player.pouches[i];
		if (toAdd > player.getItems().freeSlots()) {
			toAdd = player.getItems().freeSlots();
		}
		if (pureEssence) {
			player.getItems().addItem(7936, toAdd);
			player.pouches[i] -= toAdd;
			pureEssence = false;
			return;
		}
		if (toAdd > 0) {
			player.getItems().addItem(1436, toAdd);
			player.pouches[i] -= toAdd;
		}
	}

	public void fixAllBarrows() {
		int totalCost = 0;
		int cashAmount = player.getItems().getItemAmount(995);
		for (int j = 0; j < player.playerItems.length; j++) {
			boolean breakOut = false;
			for (int i = 0; i < player.getItems().brokenBarrows.length; i++) {
				if (player.playerItems[j] - 1 == player.getItems().brokenBarrows[i][1]) {
					if (totalCost + 250000 > cashAmount) {
						breakOut = true;
						player.sendMessage("You need atleast 250,000 gold to repair your Barrow items.");
						break;
					} else {
						totalCost += 250000;
					}
					player.playerItems[j] = player.getItems().brokenBarrows[i][0] + 1;
				}
			}
			if (breakOut) {
				break;
			}
		}
		if (totalCost > 0) {
			player.getItems().deleteItem(995, player.getItems().getItemSlot(995), totalCost);
		}
	}

	public void handleLoginText() {
		this.sendFrame126("Teleport name", 13037); // paddewwa
		this.sendFrame126("Monster Teleport", 13038); // paddewwa description
		this.sendFrame126("Teleport name", 13047); // senntisten
		this.sendFrame126("Description", 13048); // senntisten description
		this.sendFrame126("Teleport name", 13055); // kharyll
		this.sendFrame126("Description", 13056); // kharyll description
		this.sendFrame126("Teleport name", 13063); // lassar
		this.sendFrame126("Description", 13064); // lassar description
		this.sendFrame126("Teleport name", 13071); // dareeyak
		this.sendFrame126("Description", 13072); // dareeyak description
		this.sendFrame126("Teleport name", 13081); // carrallanger
		this.sendFrame126("Description", 13082); // carralanger description
		this.sendFrame126("Teleport name", 13089); // annakarl
		this.sendFrame126("Description", 13090); // annakarl description
		this.sendFrame126("Teleport name", 13097); // ghorrock
		this.sendFrame126("Description", 13098); // ghorrock description
		// *
		this.sendFrame126("Teleport name", 1300); // varrock
		this.sendFrame126("Description", 1301); // varrock description
		this.sendFrame126("Teleport name", 1325); // lumbridge
		this.sendFrame126("Description", 1326); // lumbridge description
		this.sendFrame126("Teleport name", 1350); // falador
		this.sendFrame126("Description", 1351); // falador description
		this.sendFrame126("Teleport name", 1382); // camelot
		this.sendFrame126("Description", 1383); // camelot description
		this.sendFrame126("Teleport name", 1415); // ardougne
		this.sendFrame126("Description", 1416); // ardougne description
		this.sendFrame126("Teleport name", 1454); // watchtower
		this.sendFrame126("Description", 1455); // watchtower description
		this.sendFrame126("Teleport name", 7457); // trollheim
		this.sendFrame126("Description", 7458); // trollheim description
		this.sendFrame126("Teleport name", 18472); // ape atoll
		this.sendFrame126("Description", 18473); // ape atoll description

	}

	public void itemOnInterface(int interfaceChild, int zoom, int itemId) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(246);
			player.getOutStream().writeWordBigEndian(interfaceChild);
			player.getOutStream().writeWord(zoom);
			player.getOutStream().writeWord(itemId);
			player.flushOutStream();
		}
	}

	public void destroyItem(int itemId) {
		String itemName = ItemAssistant.getItemName(itemId);
		player.getItems().deleteItem(itemId, 1);
		player.sendMessage("Your " + itemName + " vanishes as you drop it on the ground.");
		player.destroyItem = 0;
	}

	public void resetTzhaar() {
		player.waveId = -1;
		movePlayer(2438, 5168, 0);
	}

	public void enterCaves() {
		movePlayer(2413, 5117, player.getIndex() * 4);
		player.waveId = 0;
		player.tzhaarToKill = -1;
		player.tzhaarKilled = -1;
		Server.getTaskScheduler().schedule(new ScheduledTask(10) {
			@Override
			public void execute() {
				this.stop();
			}

			@Override
			public void onStop() {
				FightCaves.spawnNextWave(World.PLAYERS.get(player.getIndex()));

			}
		}.attach(player));
	}

	public void handleWeaponStyle() {
		if (player.fightMode == 0) {
			sendFrame36(43, player.fightMode);
		} else if (player.fightMode == 1) {
			sendFrame36(43, 3);
		} else if (player.fightMode == 2) {
			sendFrame36(43, 1);
		} else if (player.fightMode == 3) {
			sendFrame36(43, 2);
		}
	}

	public static void killedPlayer(Player player, Player killer) {
		if (player.equals(killer)) {
			System.out.println("The victim is the player when killed, this could be the issue.");
		}
		// player.getKillstreak().resetKillstreak(killer);
		if (player.inWild()) {
			PVPAssistant.dropItem(killer, player.absX, player.absY);
		}
		if (PKHandler.isSameConnection(killer, player) && killer.playerRights != 5) {
			killer.sendMessage("<col=ff0033>You're on the same connection as this player and cannot receive PKP");
			return;
		}
		if (!player.connectedFrom.equals("127.0.0.1") && PKHandler.hasKilledRecently(player.connectedFrom, killer)
				|| PKHandler.hasKilledRecently(player.getMacAddress(), killer)
				|| PKHandler.hasKilledRecently(player.playerName, killer)) {
			killer.sendMessage("You have killed " + player.playerName + " recently. You do not receive PK points.");
			killer.sendMessage("You must kill " + (PKHandler.MAXIMUM_ENTRIES - PKHandler.getAmountInList(killer))
					+ " more unique players to receive points.");
			return;
		} else {
			killer.sendMessage("Well done, you've defeated " + player.playerName);
			killer.pkPoints++;
			StreakHandler.checkStreak(killer, player);
			PKHandler.addKilledEntry(player.connectedFrom, killer);
			PKHandler.addKilledEntry(player.playerName, killer);
			PKHandler.addKilledEntry(player.getMacAddress(), killer);
			return;
		}

	}

	public static void activateTeleportCrystal(Player player, int itemId) {
		if (!player.getItems().playerHasItem(itemId)) {
			player.sendMessage("You need a teleport crystal to do this.");
			return;
		}
		if (player.inWild()) {
			player.sendMessage("You feel something blocking your magical powers");
			return;
		}
		if (player.inDuelArena()) {
			player.sendMessage("You cannot use this here");
			return;
		}
		if (player.getTeleportStonePos()[0] == 0 || player.getTeleportStonePos()[1] == 0) {
			player.getDH().sendDialogues(142, 0);
		}
		if (player.getTeleportStonePos()[0] > 0 && player.getTeleportStonePos()[1] > 0) {
			if (player.inArea(player.getTeleportStonePos()[0] - 30, player.getTeleportStonePos()[1] - 30,
					player.getTeleportStonePos()[0] + 30, player.getTeleportStonePos()[1] + 30)) {
				player.sendMessage(
						"The crystal won't work since you're already in the facinity of the stone's teleport.");
			} else {
				player.getItems().deleteItem2(itemId, 1);
				if (itemId != 6102) {
					player.getItems().addItem(itemId + 1, 1);
					player.sendMessage("You mysteriously teleport to a familiar location, but forgot where you are.");
				} else {
					player.sendMessage("The crystal that you once used, has crumbled to dust.");
				}
				player.getPA().movePlayer(player.getTeleportStonePos()[0], player.getTeleportStonePos()[1],
						player.getTeleportStonePos()[2]);
				player.setTeleportStonePos(new int[] { 0, 0, 0 });
			}
		}
	}

	public void sendFrame34a(int frame, int item, int slot, int amount) {
		player.outStream.createFrameVarSizeWord(34);
		player.outStream.writeWord(frame);
		player.outStream.writeByte(slot);
		player.outStream.writeWord(item + 1);
		player.outStream.writeByte(255);
		player.outStream.writeDWord(amount);
		player.outStream.endFrameVarSizeWord();
	}

	public void openUpBank() {
		if (player.getBankPin().isLocked() && player.getBankPin().getPin().trim().length() > 0) {
			player.getBankPin().open(2);
			player.isBanking = false;
			return;
		}
		if (player.takeAsNote)
			sendFrame36(115, 1);
		else
			sendFrame36(115, 0);

		if (player.inWild()) {
			player.sendMessage("You can't bank in the wilderness!");
			return;
		}
		if (player.inTrade || player.tradeStatus == 1) {
			Player o = World.PLAYERS.get(player.tradeWith);
			if (o != null) {
				o.getTradeAndDuel().declineTrade(true);
			}
		}
		if (player.getBank().getBankSearch().isSearching()) {
			player.getBank().getBankSearch().reset();
		}
		player.getPA().sendFrame126("Search", 58113);
		if (player.duelStatus == 1) {
			Player o = World.PLAYERS.get(player.duelingWith);
			if (o != null) {
				o.getTradeAndDuel().resetDuel();
			}
		}
		if (player.getOutStream() != null && player != null) {
			player.isBanking = true;
			player.getItems().resetItems(5064);
			player.getItems().resetBank();
			player.getItems().resetTempItems();
			player.getOutStream().createFrame(248);
			player.getOutStream().writeWordA(5292);// ok perfect
			player.getOutStream().writeWord(5063);
			player.flushOutStream();
			player.getPA().sendFrame126(player.getName() + "'s Bank", 58064);
		}
	}

	public boolean viewingOtherBank;
	BankTab[] backupTabs = new BankTab[9];

	public void resetOtherBank() {
		for (int i = 0; i < backupTabs.length; i++)
			player.getBank().setBankTab(i, backupTabs[i]);
		viewingOtherBank = false;
		removeAllWindows();
		player.isBanking = false;
		player.getBank().setCurrentBankTab(player.getBank().getBankTab()[0]);
		player.getItems().resetBank();
		player.sendMessage("You are no longer viewing another players bank.");
	}

	public void openOtherBank(Player otherPlayer) {
		if (otherPlayer == null)
			return;

		if (player.getPA().viewingOtherBank) {
			player.getPA().resetOtherBank();
		}
		if (otherPlayer.getPA().viewingOtherBank) {
			player.sendMessage("That player is viewing another players bank, take note.");
		}
		for (int i = 0; i < backupTabs.length; i++)
			backupTabs[i] = player.getBank().getBankTab(i);
		for (int i = 0; i < otherPlayer.getBank().getBankTab().length; i++)
			player.getBank().setBankTab(i, otherPlayer.getBank().getBankTab(i));
		player.getBank().setCurrentBankTab(player.getBank().getBankTab(0));
		viewingOtherBank = true;
		openUpBank();
	}

	/**
	 * Sets the clan information for the player's clan.
	 */

	public boolean isIgnored(final long player) {
		for (final long i : this.player.ignores) {
			if (i != 0) {
				if (player == i) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isInPM(final long l) {
		for (final long friend : this.player.friends) {
			if (friend != 0) {
				if (l == friend) {
					return true;
				}
			}
		}
		return false;
	}

	public void setPrivateMessaging(final int i) { // friends and ignore list
		// status
		// synchronized(c) {
		if (this.player.getOutStream() != null && this.player != null) {
			this.player.getOutStream().createFrame(221);
			this.player.getOutStream().writeByte(i);
			this.player.flushOutStream();
		}
		// }
	}

	public void logIntoPM() {
		this.setPrivateMessaging(2);
		for (int i1 = 0; i1 < World.PLAYERS.capacity(); i1++) {
			final Player p = World.PLAYERS.get(i1);
			if (p != null && p.isActive) {
				final Player o = p;
				if (o != null) {
					o.getPA().updatePM(this.player.getIndex(), 1);
				}
			}
		}
		boolean pmLoaded = false;
		for (final long friend : this.player.friends) {
			if (friend != 0) {
				for (int i2 = 1; i2 < World.PLAYERS.capacity(); i2++) {
					final Player p = World.PLAYERS.get(i2);
					if (p != null && p.isActive && Misc.playerNameToInt64(p.playerName) == friend) {
						final Player o = p;
						if (o != null) {
							if (this.player.playerRights >= 3 || p.privateChat == 0 || p.privateChat == 1
									&& o.getPA().isInPM(Misc.playerNameToInt64(this.player.playerName))) {
								this.loadPM(friend, 1);
								pmLoaded = true;
							}
							break;
						}
					}
				}
				if (!pmLoaded) {
					this.loadPM(friend, 0);
				}
				pmLoaded = false;
			}
			for (int i1 = 1; i1 < World.PLAYERS.capacity(); i1++) {
				final Player p = World.PLAYERS.get(i1);
				if (p != null && p.isActive) {
					final Player o = p;
					if (o != null) {
						o.getPA().updatePM(this.player.getIndex(), 1);
					}
				}
			}
		}
	}

	public void updatePM(final int pID, final int world) { // used for private
		// chat updates
		final Player p = World.PLAYERS.get(pID);
		if (p == null || p.playerName == null || p.playerName.equals("null")) {
			return;
		}
		final Player o = p;
		final long l = Misc.playerNameToInt64(World.PLAYERS.get(pID).playerName);

		if (p.privateChat == 0) {
			for (final long friend : this.player.friends) {
				if (friend != 0) {
					if (l == friend) {
						this.loadPM(l, world);
						return;
					}
				}
			}
		} else if (p.privateChat == 1) {
			for (final long friend : this.player.friends) {
				if (friend != 0) {
					if (l == friend) {
						if (o.getPA().isInPM(Misc.playerNameToInt64(this.player.playerName))) {
							this.loadPM(l, world);
							return;
						} else {
							this.loadPM(l, 0);
							return;
						}
					}
				}
			}
		} else if (p.privateChat == 2) {
			for (final long friend : this.player.friends) {
				if (friend != 0) {
					if (l == friend && this.player.playerRights < 3) {
						this.loadPM(l, 0);
						return;
					}
				}
			}
		}
	}

	/**
	 * Sets the clan information for the player's clan.
	 */
	public Clan getClan() {
		if (Server.clanManager.clanExists(this.player.playerName)) {
			return Server.clanManager.getClan(this.player.playerName);
		}
		return null;
	}

	public void setClanData() {
		boolean exists = Server.clanManager.clanExists(player.playerName);
		if (!exists || player.clan == null) {
			sendFrame126("Join chat", 18135);
			sendFrame126("Talking in: Not in chat", 18139);
			sendFrame126("Owner: None", 18140);
		}
		if (!exists) {
			sendFrame126("Chat Disabled", 18306);
			String title = "";
			for (int id = 18307; id < 18317; id += 3) {
				if (id == 18307) {
					title = "Anyone";
				} else if (id == 18310) {
					title = "Anyone";
				} else if (id == 18313) {
					title = "General+";
				} else if (id == 18316) {
					title = "Only Me";
				}
				sendFrame126(title, id + 2);
			}
			for (int index = 0; index < 100; index++) {
				sendFrame126("", 18323 + index);
			}
			for (int index = 0; index < 100; index++) {
				sendFrame126("", 18424 + index);
			}
			return;
		}
		Clan clan = Server.clanManager.getClan(player.playerName);
		sendFrame126(clan.getTitle(), 18306);
		String title = "";
		for (int id = 18307; id < 18317; id += 3) {
			if (id == 18307) {
				title = clan.getRankTitle(clan.whoCanJoin)
						+ (clan.whoCanJoin > Clan.Rank.ANYONE && clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18310) {
				title = clan.getRankTitle(clan.whoCanTalk)
						+ (clan.whoCanTalk > Clan.Rank.ANYONE && clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18313) {
				title = clan.getRankTitle(clan.whoCanKick)
						+ (clan.whoCanKick > Clan.Rank.ANYONE && clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18316) {
				title = clan.getRankTitle(clan.whoCanBan)
						+ (clan.whoCanBan > Clan.Rank.ANYONE && clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
			}
			sendFrame126(title, id + 2);
		}
		if (clan.rankedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.rankedMembers.size()) {
					sendFrame126("<clan=" + clan.ranks.get(index) + ">" + clan.rankedMembers.get(index), 18323 + index);
				} else {
					sendFrame126("", 18323 + index);
				}
			}
		}
		if (clan.bannedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.bannedMembers.size()) {
					sendFrame126(clan.bannedMembers.get(index), 18424 + index);
				} else {
					sendFrame126("", 18424 + index);
				}
			}
		}
	}

	public final String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic",
			"Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
			"Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Summoning",
			"Construction", "Dungeoneering" };

	public int getIdForSkillName(String name) {
		for (int i = 0; i < SKILL_NAME.length; i++) {
			if (name.equalsIgnoreCase(SKILL_NAME[i])) {
				return i;
			}
		}
		return -1;
	}

	public String getSkillNameForId(int skillID) {
		for (int i = 0; i < SKILL_NAME.length; i++) {
			if (skillID == i) {
				return SKILL_NAME[i];
			}
		}
		return "none";
	}

	public void walkToNegative(final int i, final int j) {
		this.player.newWalkCmdSteps = 0;
		if (++this.player.newWalkCmdSteps > 50) {
			this.player.newWalkCmdSteps = 0;
		}
		int k = this.player.getX() - i;
		k -= this.player.mapRegionX * 8;
		this.player.getNewWalkCmdX()[0] = this.player.getNewWalkCmdY()[0] = 0;
		int l = this.player.getY() - j;
		l -= this.player.mapRegionY * 8;

		for (int n = 0; n < this.player.newWalkCmdSteps; n++) {
			this.player.getNewWalkCmdX()[n] += k;
			this.player.getNewWalkCmdY()[n] += l;
		}
	}

	public void chooseItem5(String q1, String n1, String n2, String n3, String n4, String n5, int i1, int i2, int i3,
			int i4, int i5) {
		player.getPA().sendFrame126(n1, 8949);
		player.getPA().sendFrame126(n2, 8953);
		player.getPA().sendFrame126(n3, 8957);
		player.getPA().sendFrame126(n4, 8961);
		player.getPA().sendFrame126(n5, 8965);
		player.getPA().sendFrame126(q1, 8966);
		player.getPA().sendFrame246(8941, 190, i1);
		player.getPA().sendFrame246(8942, 190, i2);
		player.getPA().sendFrame246(8943, 190, i3);
		player.getPA().sendFrame246(8944, 190, i4);
		player.getPA().sendFrame246(8945, 190, i5);
		player.getPA().sendFrame164(8938);
	}

	public void sendConfig(final int id, final int state) {
		if (this.player.getOutStream() != null && this.player != null) {
			if (state < 128) {
				this.player.getOutStream().createFrame(36);
				this.player.getOutStream().writeWordBigEndian(id);
				this.player.getOutStream().writeByte(state);
			} else {
				this.player.getOutStream().createFrame(87);
				this.player.getOutStream().writeWordBigEndian_dup(id);
				this.player.getOutStream().writeDWord_v1(state);
			}
			this.player.flushOutStream();
		}
	}

	public void handleGlory() {
		player.getDH().sendOption4("Edgeville", "Canifis", "Karamja", "Al Kharid");
		player.usingGlory = true;
	}

	public void gloryDegrade() {
		if (player.gdegradeNow == true) {
			if (player.gloryValue == 3) {
				if (player.getItems().playerHasItem(1712, 1)) {
					player.getItems().addItem(1710, 1);
					player.getItems().deleteItem(1712, 1);
					player.gloryValue = 4;
					player.gdegradeNow = false;
				}
			}
			if (player.gloryValue == 2) {
				if (player.getItems().playerHasItem(1710, 1)) {
					player.getItems().addItem(1708, 1);
					player.getItems().deleteItem(1710, 1);
					player.gloryValue = 4;
					player.gdegradeNow = false;
				}
			}
			if (player.gloryValue == 1) {
				if (player.getItems().playerHasItem(1708, 1)) {
					player.getItems().addItem(1706, 1);
					player.getItems().deleteItem(1708, 1);
					player.gloryValue = 4;
					player.gdegradeNow = false;
				}
			}
			if (player.gloryValue == 0) {
				if (player.getItems().playerHasItem(1706, 1)) {
					player.getItems().addItem(1704, 1);
					player.getItems().deleteItem(1706, 1);
					player.sendMessage("Your amulet is out of charges! Recharge location not added yet.");
					player.gdegradeNow = false;
				}
			}
		}
	}

	public void displayReward(Item... items) {
		player.outStream.createFrameVarSizeWord(53);
		player.outStream.writeWord(6963);
		player.outStream.writeWord(items.length);

		for (Item item : items) {
			if (item.count > 254) {
				player.outStream.writeByte(255);
				player.outStream.writeDWord_v2(item.count);
			} else {
				player.outStream.writeByte(item.count);
			}
			if (item.id > 0) {
				player.outStream.writeWordBigEndianA(item.id + 1);
			} else {
				player.outStream.writeWordBigEndianA(0);
			}
		}
		player.outStream.endFrameVarSizeWord();
		player.flushOutStream();
		player.getPA().showInterface(6960);
	}

	public void sendItems(Item... items) {
		player.outStream.createFrameVarSizeWord(53);
		player.outStream.writeWord(6963);
		player.outStream.writeWord(items.length);

		for (Item item : items) {
			if (item.count > 254) {
				player.outStream.writeByte(255);
				player.outStream.writeDWord_v2(item.count);
			} else {
				player.outStream.writeByte(item.count);
			}
			if (item.id > 0) {
				player.outStream.writeWordBigEndianA(item.id + 1);
			} else {
				player.outStream.writeWordBigEndianA(0);
			}
		}
		player.outStream.endFrameVarSizeWord();
		player.flushOutStream();
		player.getPA().showInterface(6960);
	}

	/**
	 * @param i
	 */
	public void stunEnemy(int i) {
		player.gfx100(254);
	}

	public void drainPray(int i) {
		int prayerDrained = Misc.random(15) + 5;
		if (player.playerLevel[5] < prayerDrained)
			player.playerLevel[5] = 0;
		else
			player.playerLevel[5] -= prayerDrained;
		player.sendMessage("Venenatis drains your prayer.");
	}

	public int backupInvItems[] = new int[28];
	public int backupInvItemsN[] = new int[28];

	public void otherInv(Player c, Player o) {
		if (o == c || o == null || c == null)
			return;
		for (int i = 0; i < o.playerItems.length; i++) {
			backupInvItems[i] = c.playerItems[i];
			c.playerItemsN[i] = c.playerItemsN[i];
			c.playerItemsN[i] = o.playerItemsN[i];
			c.playerItems[i] = o.playerItems[i];
		}
		c.getItems().updateInventory();

		for (int i = 0; i < o.playerItems.length; i++) {
			c.playerItemsN[i] = backupInvItemsN[i];
			c.playerItems[i] = backupInvItems[i];
		}
	}

	public void setInterfaceTextColor(int interfaceId, int red, int green, int blue) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(122);
			player.getOutStream().writeWordBigEndianA(interfaceId);
			player.getOutStream().writeWordBigEndianA(red << 10 | green << 5 | blue);
			player.flushOutStream();
		}
	}

	public void loadAllQuests(Player c) {
		sendFrame126("Account Information:", 29161);

		sendFrame126("@or2@PK Points: @gre@[" + c.pkPoints + "]", 29169);
		sendFrame126("@or2@Kills: @gre@[" + c.originalKillCount + "]", 29163);
		sendFrame126("@or2@Agility Points:@gre@[" + c.agilityPoints + "]", 29164);
		sendFrame126("@or2@Pest Control Points: @gre@[" + c.pcPoints + "]", 29165);
		sendFrame126("@or2@Slayer Points: @gre@[" + c.slaypoints + "]", 29166);
		sendFrame126("@or2@Vote Points: @gre@[" + c.votePoints + "]", 29167);
		sendFrame126("@or2@Donation Total: @gre@ [$" + c.amountDonated + "]", 29168);
		sendFrame126("@or2@PK Points: @gre@[" + c.pkPoints + "]", 29169);
		;

		/*
		 * if (c.cookQP == 0) { sendFrame126("Cook's Assistant", 29171); } else
		 * if (c.cookQP == 1) { sendFrame126("@yel@Cook's Assistant", 29171); }
		 * else if (c.cookQP >= 2) { sendFrame126("@gre@Cook's Assistant",
		 * 29171); } if (c.undergroundQP == 0) { sendFrame126("Underground Pass"
		 * , 29172); } else if (c.undergroundQP == 1) { sendFrame126(
		 * "@yel@Underground Pass", 29172); } else if (c.undergroundQP == 2) {
		 * sendFrame126("@yel@Underground Pass", 29172); } else if
		 * (c.undergroundQP >= 5) { sendFrame126("@gre@Underground Pass",
		 * 29172); } if (c.horrorQP == 0) { sendFrame126("Horror from the Deep",
		 * 29173); } else if (c.horrorQP == 1) { sendFrame126(
		 * "@yel@Horror from the Deep", 29173); } else if (c.horrorQP == 2) {
		 * sendFrame126("@yel@Horror from the Deep", 29173); } else if
		 * (c.horrorQP >= 3) { sendFrame126("@gre@Horror from the Deep", 29173);
		 * } if (c.dtQP == 0) { sendFrame126("Desert Treasure", 29174); } else
		 * if (c.dtQP == 1) { sendFrame126("@yel@Desert Treasure", 29174); }
		 * else if (c.dtQP == 2) { sendFrame126("@yel@Desert Treasure", 29174);
		 * } else if (c.dtQP >= 3) { sendFrame126("@gre@Desert Treasure",
		 * 29174); } if (c.dragonQP == 0) { sendFrame126("Dragon Slayer",
		 * 29175); } else if (c.dragonQP == 1) { sendFrame126(
		 * "@yel@Dragon Slayer", 29175); } else if (c.dragonQP == 2) {
		 * sendFrame126("@yel@Dragon Slayer", 29175); } else if (c.dragonQP >=
		 * 3) { sendFrame126("@gre@Dragon Slayer", 29175); }
		 */
	}

	public void removeAllItems() {
		// TODO Auto-generated method stub

	}
	public int getTotalBagItems() {
		int amount = 0;
		for (int i = 0; i < c.bagItems.length; i++) {
			if (c.bagItems[i] != 0) {
				amount++;
			}
		}
		return amount;
	}
	public void storeBagItems(int itemId, int amount) {
		if (c.getItems().playerHasItem(itemId, amount)) {
			int count = 0;
			c.sendMessage("You have stored the item: @red@"+c.getItems().getItemName(itemId)+"@bla@ amount: @red@"+amount+"X");
			
			for (int i = 0; i < c.bagItems.length; i++) {
				for (int b = 0; b < amount; i++) {
					if (c.bagItems[i] == 0) {
						c.bagItems[i] = itemId;
						c.getItems().deleteItem(itemId, amount);
						count++;
						if (c.getPA().getTotalBagItems() > 26) {
							c.sendMessage("Your can't put more items in your bag, it is full");
							return;
						}
						if (count >= amount) {
							return;
						}
					}
				}
			}
		} else {
			c.sendMessage("You dont have that amount");
		}
		c.tempIdHolder = -1;
		c.isUsingBag= false;
	}
}
