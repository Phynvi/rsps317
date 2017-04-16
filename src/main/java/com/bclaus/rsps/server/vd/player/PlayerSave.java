package com.bclaus.rsps.server.vd.player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.bclaus.rsps.server.util.FileUtils;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueDifficulty;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueScrollContainer;
import com.bclaus.rsps.server.vd.npc.NPCDeathTracker;
import com.bclaus.rsps.server.vd.npc.NPCSlayerDeathTracker;
import com.bclaus.rsps.server.vd.npc.pets.Pet;
import com.bclaus.rsps.server.vd.player.account_type.Account;
import com.bclaus.rsps.server.vd.player.account_type.AccountType;
import com.google.common.collect.Iterables;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueScroll;
import com.bclaus.rsps.server.vd.items.bank.BankItem;

public class PlayerSave {
	
	/**
	 * Loading
	 * 
	 * @throws IOException
	 */
	public static int loadGame(Player p, String playerName, String playerPass) {
		File file = new File(FileUtils.getCharacterDirectory(), playerName + ".txt");
		if (!file.exists()) {
			Misc.println(playerName + ": character file not found.");
			p.newPlayer = false;
			return 0;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			int mode = 0;
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				int spot = line.indexOf("=");
				if (spot > -1) {

					String key = line.substring(0, spot).trim();
					String value = line.substring(spot + 1).trim();
					String[] values = value.split("\t");

					switch (mode) {
					case 1:
						if (key.equals("character-password")) {
							if (playerPass.equalsIgnoreCase(value) || Misc.basicEncrypt(playerPass).equals(value) || Misc.md5Hash(playerPass).equals(value)) {
								playerPass = value;
							} else {
								return 3;
							}
						}
						break;
					case 2:
						if (key.equals("character-height")) {
							p.heightLevel = Integer.parseInt(value);
						} else if (key.equals("identity")) {
							p.setIdentity(value);
						} else if (key.equals("lastClanChat")) {
							p.lastClanChat = value;
						} else if (key.equals("lockedEXP")) {
							p.lockedEXP = Boolean.parseBoolean(value);
						} else if (key.equals("slayerBossTask")) {
							p.slayerBossTask = Boolean.parseBoolean(value);
						} else if (line.startsWith("KC")) {
							p.originalKillCount = Integer.parseInt(value);
						} else if (line.startsWith("DC")) {
							p.originalDeathCount = Integer.parseInt(value);
						} else if (line.startsWith("ks")) {
							p.ks = Integer.parseInt(value);
						} else if (line.startsWith("OKC")) {
							p.playerKillCount = Integer.parseInt(value);
						} else if (line.startsWith("ODC")) {
							p.playerDeathCount = Integer.parseInt(value);
						} else if (line.startsWith("canClaim")) {
							p.canClaim = Boolean.parseBoolean(value);
						} else if (line.startsWith("displayNameClaim")) {
							p.displayNameClaim = Boolean.parseBoolean(value);
						} else if (line.startsWith("hasBoughtCannon")) {
							p.hasBoughtCannon = Boolean.parseBoolean(value);
						} else if (line.startsWith("additional-security")) {
							p.securityEnabled = Boolean.parseBoolean(value);
						} else if (line.startsWith("hasDisabledEpDrops")) {
							p.hasDisabledEpDrops = Boolean.parseBoolean(value);
						} else if (key.equals("isSponsor")) {
							p.isSponsor = Boolean.parseBoolean(value);
						} else if (key.equals("teleportRequired")) {
							p.teleportRequired = Boolean.parseBoolean(value);
						} else if (key.equals("character-posx")) {
							p.teleportToX = (Integer.parseInt(value) <= 0 ? 3091 : Integer.parseInt(value));
						} else if (key.equals("character-posy")) {
							p.teleportToY = (Integer.parseInt(value) <= 0 ? 3502 : Integer.parseInt(value));
						} else if (key.equals("character-rights")) {
							p.playerRights = Integer.parseInt(value);
						} else if (key.equals("donator-rights")) {
							p.donatorRights = Integer.parseInt(value);
						} else if (key.equals("barrowsChestCount")) {
							p.barrowsChestCount = Integer.parseInt(value);
						} else if (key.equals("zulrahEntries")) {
							p.zulrahEntries = Integer.parseInt(value);
						} else if (key.equals("playerX")) {
							p.playerX = Integer.parseInt(value);
						} else if (key.equals("playerY")) {
							p.playerY = Integer.parseInt(value);
						} else if (key.equals("playerZ")) {
							p.playerZ = Integer.parseInt(value);
						} else if (key.equals("lastLoginDate")) {
							p.lastLoginDate = Integer.parseInt(value);
						} else if (key.equals("crystal-bow-shots")) {
							p.crystalBowArrowCount = Integer.parseInt(value);
						} else if (key.equals("dartsLoaded")) {
							p.dartsLoaded = Integer.parseInt(value);
						} else if (key.equals("runedartsLoaded")) {
							p.runedartsLoaded = Integer.parseInt(value);
						} else if (key.equals("skull-timer")) {
							p.skullTimer = Integer.parseInt(value);
						} else if (key.equals("magic-book")) {
							p.playerMagicBook = Integer.parseInt(value);
						} else if (key.equals("brother-info")) {
							p.barrowsNpcs[Integer.parseInt(values[0])][1] = Integer.parseInt(values[1]);
						} else if (key.equals("special-amount")) {
							p.specAmount = Double.parseDouble(value);
						} else if (key.equals("as-points")) {
							p.assaultPoints = Integer.parseInt(value);
						} else if (key.equals("skill-points")) {
							p.skillPoints = Integer.parseInt(value);
						} else if (key.equals("FMPoints")) {
							p.fireMakingPoints = Integer.parseInt(value);
						} else if (key.equals("wcPoints")) {
							p.wcPoints = Integer.parseInt(value);
						} else if (key.equals("agilityPoints")) {
							p.agilityPoints = Integer.parseInt(value);
						} else if (key.equals("cookQP")) {
							p.cookQP = Integer.parseInt(value);
						} else if (key.equals("undergroundQP")) {
							p.undergroundQP = Integer.parseInt(value);
						} else if (key.equals("horrorQP")) {
							p.horrorQP = Integer.parseInt(value);
						} else if (key.equals("dragonQP")) {
							p.dragonQP = Integer.parseInt(value);
						} else if (key.equals("dtQP")) {
							p.dtQP = Integer.parseInt(value);
						} else if (key.equals("slay-points")) {
							p.slaypoints = Integer.parseInt(value);
						} else if (key.equals("isSocialSlaying")) {
							p.setSocialSlaying(Boolean.parseBoolean(value));
						} else if (key.equals("socialSlayerPoints")) {
							p.setSocialSlayerPoints(Integer.parseInt(value));
						} else if (key.equals("socialSlayerKills")) {
							p.setSocialSlayerKills(Integer.parseInt(value));
						} else if (key.equals("socialSlayerDisconnections")) {
							p.setSocialSlayerDisconnections(Integer.parseInt(value));
						} else if (key.equals("socialSlayerPlayer")) {
							p.setLastSocialPlayer(value);
						} else if (key.equals("tentacleHits")) {
							p.tentacleHits = Integer.parseInt(value);
						} else if (key.equals("lastPlayerKilled")) {
							p.lastPlayerKilled = value;
						} else if (key.equals("lastIPKilled")) {
							p.lastIPKilled = value;
						} else if (key.equals("modeType")) {
							p.playerMode = Integer.parseInt(value);;
						} else if (key.equals("canUseFoodOnPlayer")) {
							p.setCanUseFoodOnPlayer(Boolean.parseBoolean(value));
						} else if (key.equals("canUsePotionOnTask")) {
							p.setCanSharePotion(Boolean.parseBoolean(value));
						} else if (key.equals("removedTask0")) {
							p.removedTasks[0] = Integer.parseInt(value);
						} else if (key.equals("removedTask1")) {
							p.removedTasks[1] = Integer.parseInt(value);
						} else if (key.equals("removedTask2")) {
							p.removedTasks[2] = Integer.parseInt(value);
						} else if (key.equals("removedTask3")) {
							p.removedTasks[3] = Integer.parseInt(value);
						} else if (key.equals("run-energy")) {
							p.runEnergy = Integer.parseInt(value);
						} else if (key.equals("clue-container")) {
							try {
								if (value == null || !value.equals("null")) {
									List<ClueScroll> list = new ArrayList<>();
									for (int i = 0; i < values.length; i++)
										list.add(ClueScroll.valueOf(values[i]));
									p.clueContainer = new ClueScrollContainer(p, Iterables.toArray(list, ClueScroll.class));
								}
							} catch (IllegalArgumentException e) {
								p.clueContainer = null;
							}
						} else if (key.equals("clue-reward")) {
							if (!value.equals("null"))
								p.bossDifficulty = ClueDifficulty.valueOf(value);
						} else if (key.equals("easy-clue")) {
							p.easyClue = Integer.parseInt(value);
						} else if (key.equals("medium-clue")) {
							p.mediumClue = Integer.parseInt(value);
						} else if (key.equals("hard-clue")) {
							p.hardClue = Integer.parseInt(value);
						} else if (key.equals("elite-clue")) {
							p.eliteClue = Integer.parseInt(value);
						} else if (key.equals("DonorPoints")) {
							p.DonorPoints = Integer.parseInt(value);
						} else if (key.equals("isDonator")) {
							p.isDonator = Boolean.parseBoolean(value);
						} else if (key.equals("amountDonated")) {
							p.amountDonated = Integer.parseInt(value);
						} else if (key.equals("symbolchanger")) {
							p.changedSymbol = Boolean.parseBoolean(value);
						} else if (key.equals("hasSpokenToWC")) {
							p.hasSpokenToWC = Boolean.parseBoolean(value);
						} else if (key.equals("MercenaryDone")) {
							p.Mercenary = Boolean.parseBoolean(value);
						} else if (key.equals("teleportStone")) {
							int[] pos = new int[] { Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]) };
							p.setTeleportStonePos(pos);
						} else if (key.equals("pinRegisteredDeleteDay")) {
							p.pinDeleteDateRequested = Integer.parseInt(value);
						} else if (key.equals("requestPinDelete")) {
							p.requestPinDelete = Boolean.parseBoolean(value);
						} else if (key.equals("removeBankMessage")) {
							p.removeBankMessage = Boolean.parseBoolean(value);
						} else if (key.equals("barrows-killcount")) {
							p.barrowsKillCount = Integer.parseInt(value);
						} else if (key.equals("teleblock-length")) {
							p.teleBlockDelay = System.currentTimeMillis();
							p.teleBlockLength = Integer.parseInt(value);
						} else if (key.equals("pc-points")) {
							p.pcPoints = Integer.parseInt(value);
						} else if (key.equals("slayerTask")) {
							p.slayerTask = Integer.parseInt(value);
						} else if (key.equals("taskAmount")) {
							p.taskAmount = Integer.parseInt(value);
						} else if (key.equals("slayerTaskLevel")) {
							p.slayerTaskLevel = Integer.parseInt(value);
						} else if (key.equals("magePoints")) {
							p.magePoints = Integer.parseInt(value);
						} else if (key.equals("votePoints")) {
							p.votePoints = Integer.parseInt(value);
						}  else if (key.equals("injail")) {
						    p.inJail = Boolean.parseBoolean(value);
						} else if (key.equals("voteExperienceMultiplier")) {
							p.voteExperienceMultiplier = Long.parseLong(value);
						} else if (key.equals("dropRateIncreaser")) {
							p.dropRateIncreaser = Long.parseLong(value);
						} else if (key.equals("DoublePKP")) {
							p.DoublePKP = Long.parseLong(value);
						} else if (key.equals("thievePoints")) {
							p.thievePoints = Integer.parseInt(value);
						} else if (key.equals("amount-donated")) {
							p.setDonationAmount(Double.parseDouble(value));
						} else if (key.equals("donationPointAmount")) {
							p.setDonationPoints(Integer.parseInt(value));
						} else if (key.equals("mode")) {
							p.diffLevel = Integer.parseInt(value);
						} else if (key.equals("haveRouge")) {
							p.haveRouge = Boolean.parseBoolean(value);
						} else if (key.equals("pkpoints")) {
							p.pkPoints = Integer.parseInt(value);
						} else if (key.equals("autoRet")) {
							p.autoRetaliate = Boolean.parseBoolean(value);
						} else if (key.equals("poisonDamage")) {
							p.poisonDamage = Integer.parseInt(value);
						} else if (key.equals("barrowskillcount")) {
							p.barrowsKillCount = Integer.parseInt(value);
						} else if (key.equals("zkill")) {
							p.zkill = Integer.parseInt(value);
						} else if (key.equals("wave")) {
							p.waveId = Integer.parseInt(value);
						} else if (key.equals("quickprayer")) {
							for (int j = 0; j < values.length; j++) {
								p.getQuick().getNormal()[j] = Boolean.parseBoolean(values[j]);
							}
						} else if (key.equals("void")) {
							for (int j = 0; j < values.length; j++) {
								p.voidStatus[j] = Integer.parseInt(values[j]);
							}
						} else if (key.equals("gwkc")) {
							p.killCount = Integer.parseInt(value);
						} else if (key.equals("fightMode")) {
							p.fightMode = Integer.parseInt(value);
						} else if (key.equals("altarRestore")) {
							p.canRestoreSpecial = Boolean.parseBoolean(value);
						} else if (key.equals("yells")) {
							p.yells = Integer.parseInt(value);
						} else if (key.equals("modeTut")) {
							p.modeTut = Boolean.parseBoolean(value);
						} else if (key.equals("splitChat")) {
							p.splitChat = Boolean.parseBoolean(value);
						} else if (key.equals("isClanMuted")) {
							p.isClanMuted = Boolean.parseBoolean(value);
						} else if (key.equals("hasClaimedStarter")) {
							p.hasClaimedStarter = Boolean.parseBoolean(value);
						} else if (key.equals("gameMode")) {
							p.gameMode = Integer.parseInt(value);
							switch (p.gameMode) {
							case 0:
							case 1:
								p.gameModeTitle = "";
							case 2:
								p.gameModeTitle = "Legend";
								break;
							}
						} else if (key.equals("pickup-delay")) {
							p.pickupDelay = Long.parseLong(value);
						} else if (key.equals("play-time")) {
							p.pTime = Integer.parseInt(value);
						} else if (key.equals("loyaltyTitle")) {
							p.loyaltyTitle = value;
						} else if (key.equals("creature-potion")) {
							p.setCreaturePotionTimer(Integer.parseInt(value));
						} else if (key.equals("achievement-points")) {
							p.getAchievements().setPoints(Integer.parseInt(value));
						} else if (key.equals("achievement-items")) {
							for (int i = 0; i < values.length; i++)
								if (i < p.getAchievements().getBoughtItems().length - 1)
									p.getAchievements().setBoughtItem(i, Integer.parseInt(values[i]));
						} else if (key.equals("option-display-name")) {
							p.getGameOptions().setDisplayNameActive(Boolean.parseBoolean(value));
						} else if (key.equals("bank-pin")) {
							p.getBankPin().setPin(value);
						} else if (key.equals("bank-pin-cancellation")) {
							p.getBankPin().setAppendingCancellation(Boolean.parseBoolean(value));
						} else if (key.equals("bank-pin-cancellation-delay")) {
							p.getBankPin().setCancellationDelay(Long.parseLong(value));
						} else if (key.equals("account-type")) {
							AccountType type = Account.get(value);
							if (type != null)
								p.getAccount().setType(type);
						} else if (key.startsWith("item")) {
							int slot = Integer.parseInt(values[0]);
							int itemId = Integer.parseInt(values[1]);
							int amount = Integer.parseInt(values[2]);
							p.resourceItemId[slot] = itemId;
							p.resourceItemAmount[slot] = amount;
						}

						for (int i = 0; i < p.shownDefender.length; i++) {
							if (key.equals("shownDefender[" + i + "]")) {
								p.shownDefender[i] = Boolean.parseBoolean(value);
							}
						}

						break;
					case 3:
						if (key.equals("character-equip")) {
							p.playerEquipment[Integer.parseInt(values[0])] = Integer.parseInt(values[1]);
							p.playerEquipmentN[Integer.parseInt(values[0])] = Integer.parseInt(values[2]);
						}
						break;
					case 4:
						if (key.equals("character-look")) {
							p.playerAppearance[Integer.parseInt(values[0])] = Integer.parseInt(values[1]);
						}
						break;
					case 5:
						if (key.equals("character-skill")) {
							p.playerLevel[Integer.parseInt(values[0])] = Integer.parseInt(values[1]);
							p.playerXP[Integer.parseInt(values[0])] = Integer.parseInt(values[2]);
						}
						break;
					case 6:
						if (key.equals("character-item")) {
							p.playerItems[Integer.parseInt(values[0])] = Integer.parseInt(values[1]);
							p.playerItemsN[Integer.parseInt(values[0])] = Integer.parseInt(values[2]);
						}
						break;
					case 7:
						if (key.equals("character-bank")) {
							p.bankItems[Integer.parseInt(values[0])] = Integer.parseInt(values[1]);
							p.bankItemsN[Integer.parseInt(values[0])] = Integer.parseInt(values[2]);
							p.getBank().getBankTab()[0].add(new BankItem(Integer.parseInt(values[1]), Integer.parseInt(values[2])));
						} else if (key.equals("bank-tab")) {
							int tabId = Integer.parseInt(values[0]);
							int itemId = Integer.parseInt(values[1]);
							int itemAmount = Integer.parseInt(values[2]);
							p.getBank().getBankTab()[tabId].add(new BankItem(itemId, itemAmount));
						}
						break;
					case 8:
						if (key.equals("character-friend")) {
							p.friends[Integer.parseInt(values[0])] = Long.parseLong(values[1]);
						}
						break;
					case 9:
						if (key.equals("character-ignore")) {
							p.ignores[Integer.parseInt(values[0])] = Long.parseLong(values[1]);
						}
						break;
					case 10:
						if (key.startsWith("last-killed"))
							p.lastKilledList.add(value);
						break;
					case 11:
						if (key.equals("Prestige")) {
							p.prestigeLevel = Integer.parseInt(value);
						} else if (key.equals("Points")) {
							p.prestigePoints = Integer.parseInt(value);
						} else if (key.equals("PrestigeTitle")) {
							p.prestigeTitle = Integer.parseInt(value);

						}
						break;

					case 12:
						break;

					case 13:
						if (key.equals("wilderness-potential")) {
							p.setWildernessPotential(Double.parseDouble(value));
						} else if (key.equals("killstreak")) {
							p.killStreak = Integer.parseInt(value);
						} else if (key.equals("max-killstreak")) {
							p.highestKillStreak = Integer.parseInt(value);
						}
						break;
					case 15:
						if (values.length < 2)
							continue;
						p.getAchievements().read(key, 0, Integer.parseInt(values[0]), Boolean.parseBoolean(values[1]));
						break;
					case 16:
						if (values.length < 2)
							continue;
						p.getAchievements().read(key, 1, Integer.parseInt(values[0]), Boolean.parseBoolean(values[1]));
						break;
					case 17:
						if (values.length < 2)
							continue;
						p.getAchievements().read(key, 2, Integer.parseInt(values[0]), Boolean.parseBoolean(values[1]));
						break;

					case 19:
						NPCDeathTracker.NPCName name = NPCDeathTracker.NPCName.get(key);
						if (name != null) {
							p.getBossDeathTracker().tracker.put(name, Integer.parseInt(value));
						}
						NPCSlayerDeathTracker.NPCNAMES names = NPCSlayerDeathTracker.NPCNAMES.get(key);
						if (names != null) {
							p.getSlayerDeathTracker().tracking.put(names, Integer.parseInt(value));
						}
						break;
					case 20:
						int id = 0;
						boolean owned = false;
						try {
							id = Integer.parseInt(key);
							owned = Boolean.parseBoolean(value);
						} catch (NumberFormatException nfe) {
							nfe.printStackTrace();
						}
						Pet pet = Pet.get(id);
						if (pet != null) {
							p.getPet().setOwnedPet(id, owned);
						}
						break;

					}
				} else {
					if (line.equals("[ACCOUNT]")) {
						mode = 1;
					} else if (line.equals("[CHARACTER]")) {
						mode = 2;
					} else if (line.equals("[EQUIPMENT]")) {
						mode = 3;
					} else if (line.equals("[LOOK]")) {
						mode = 4;
					} else if (line.equals("[SKILLS]")) {
						mode = 5;
					} else if (line.equals("[ITEMS]")) {
						mode = 6;
					} else if (line.equals("[BANK]")) {
						mode = 7;
					} else if (line.equals("[FRIENDS]")) {
						mode = 8;
					} else if (line.equals("[IGNORES]")) {
						mode = 9;
					} else if (line.equals("[KILLS]")) {
						mode = 10;
					} else if (line.equals("[PRESTIGE]")) {
						mode = 11;
					} else if (line.equals("[ACHIEVEMENTS]")) {
						mode = 12;
					} else if (line.equals("[PVP]")) {
						mode = 13;
					} else if (line.equals("[DEGRADEABLE]")) {
						mode = 14;
					} else if (line.equals("[ACHIEVEMENTS-TIER-1]")) {
						mode = 15;
					} else if (line.equals("[ACHIEVEMENTS-TIER-2]")) {
						mode = 16;
					} else if (line.equals("[ACHIEVEMENTS-TIER-3]")) {
						mode = 17;
					} else if (line.equals("[KILLCOUNT-KILLS]")) {
						mode = 18;
					} else if (line.equals("[BOSS-TRACKER]")) {
						mode = 19;
					} else if (line.equals("[NPC-PETS]")) {
						mode = 20;
					} else if (line.equals("SLAYER-TRACKER")) {
						mode = 21;

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 13;
	}

	/**
	 * Saving
	 * 
	 * @throws IOException
	 */
	public static boolean saveGame(Player p) {

		if (!p.saveFile || p.newPlayer || !p.saveCharacter) {
			return false;
		}
		if (p.playerName == null || World.PLAYERS.get(p.getIndex()) == null) {
			return false;
		}
		p.playerName = p.playerName2;
		final int time = ((int) (p.teleBlockDelay - System.currentTimeMillis() + p.teleBlockLength));
		final int tbTime = time > 300000 || time < 0 ? 0 : time;
		File file = new File(FileUtils.getCharacterDirectory(), p.playerName + ".txt");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

			/* ACCOUNT */
			writer.write("[ACCOUNT]", 0, 9);
			writer.newLine();
			writer.write("character-username = ", 0, 21);
			writer.write(p.playerName, 0, p.playerName.length());
			writer.newLine();
			writer.write("character-password = ", 0, 21);
			String passToWrite = Misc.md5Hash(p.playerPass);
			writer.write(passToWrite, 0, passToWrite.length());
			writer.newLine();

			/* CHARACTER */
			writer.write("[CHARACTER]", 0, 11);
			writer.newLine();
			writer.write("identity = " + p.getIdentity());
			writer.newLine();
			writer.write("account-type = " + p.getAccount().getType().alias());
			writer.newLine();
			writer.write("lastClanChat = ");
			writer.write(p.lastClanChat, 0, p.lastClanChat.length());
			writer.newLine();
			writer.write("character-height = ", 0, 19);
			writer.write(Integer.toString(p.heightLevel), 0, Integer.toString(p.heightLevel).length());
			writer.newLine();
			writer.write("character-posx = ", 0, 17);
			writer.write(Integer.toString(p.getAbsX()), 0, Integer.toString(p.getAbsX()).length());
			writer.newLine();
			writer.write("character-posy = ", 0, 17);
			writer.write(Integer.toString(p.getAbsY()), 0, Integer.toString(p.getAbsY()).length());
			writer.newLine();
			writer.write("character-rights = ", 0, 19);
			writer.write(Integer.toString(p.playerRights), 0, Integer.toString(p.playerRights).length());
			writer.newLine();
			writer.write("modeType = ", 0, 11);
			writer.write(Integer.toString(p.playerMode), 0, Integer.toString(p.playerMode).length());
			writer.newLine();

			
			writer.write("donator-rights = ");
			writer.write(Integer.toString(p.donatorRights), 0, Integer.toString(p.donatorRights).length());
			writer.newLine();
			writer.write("barrowsChestCount = ");
			writer.write(Integer.toString(p.barrowsChestCount));
			writer.newLine();
			writer.write("zulrahEntries = ");
			writer.write(Integer.toString(p.zulrahEntries));
			writer.newLine();
			writer.write("tentacleHits = ");
			writer.write(Integer.toString(p.tentacleHits));
			writer.newLine();
			writer.write("lastPlayerKilled = ");
			writer.write(p.lastPlayerKilled);
			writer.newLine();
			writer.write("lastIPKilled = ");
			writer.write(p.lastIPKilled);
			writer.newLine();
			writer.write("playerX = ", 0, 10);
			writer.write(Integer.toString(p.playerX), 0, Integer.toString(p.playerX).length());
			writer.newLine();
			writer.write("playerY = ", 0, 10);
			writer.write(Integer.toString(p.playerY), 0, Integer.toString(p.playerY).length());
			writer.newLine();
			writer.write("playerZ = ", 0, 10);
			writer.write(Integer.toString(p.playerZ), 0, Integer.toString(p.playerZ).length());
			writer.newLine();
			writer.write("lastLoginDate = ", 0, 16);
			writer.write(Integer.toString(p.lastLoginDate), 0, Integer.toString(p.lastLoginDate).length());
			writer.newLine();
			writer.write("KC = ", 0, 4);
			writer.write(Integer.toString(p.originalKillCount), 0, Integer.toString(p.originalKillCount).length());
			writer.newLine();
			writer.write("DC = ", 0, 4);
			writer.write(Integer.toString(p.originalDeathCount), 0, Integer.toString(p.originalDeathCount).length());
			writer.newLine();
			writer.write("ks = " + p.ks);
			writer.newLine();
			writer.write("OKC = ");
			writer.write(Integer.toString(p.playerKillCount));
			writer.newLine();
			writer.write("ODC = ");
			writer.write(Integer.toString(p.playerDeathCount));
			writer.newLine();
			writer.write("canClaim = " + Boolean.toString(p.canClaim));
			writer.newLine();
			writer.write("displayNameClaim = " + Boolean.toString(p.displayNameClaim));
			writer.newLine();
			writer.write("hasBoughtCannon = " + Boolean.toString(p.hasBoughtCannon));
			writer.newLine();
			writer.write("additional-security = " + Boolean.toString(p.securityEnabled));
			writer.newLine();
			writer.write("hasDisabledEpDrops = " + Boolean.toString(p.hasDisabledEpDrops));
			writer.newLine();
			writer.write("isSponsor = " + Boolean.toString(p.isSponsor));
			writer.newLine();
			writer.write("teleportRequired = " + Boolean.toString(p.teleportRequired));
			writer.newLine();
			writer.write("pickup-delay = " + p.pickupDelay);
			writer.newLine();
			writer.write("lockedEXP = ", 0, 12);
			writer.write(Boolean.toString(p.lockedEXP), 0, Boolean.toString(p.lockedEXP).length());
			writer.newLine();
			writer.write("slayerBossTask = ");
			writer.write(Boolean.toString(p.slayerBossTask));
			writer.newLine();
			writer.write("crystal-bow-shots = ", 0, 20);
			writer.write(Integer.toString(p.crystalBowArrowCount), 0, Integer.toString(p.crystalBowArrowCount).length());
			writer.newLine();
			writer.write("dartsLoaded = ", 0, 14);
			writer.write(Integer.toString(p.dartsLoaded), 0, Integer.toString(p.dartsLoaded).length());
			writer.newLine();
			writer.write("runedartsLoaded = ", 0, 18);
			writer.write(Integer.toString(p.runedartsLoaded), 0, Integer.toString(p.runedartsLoaded).length());
			writer.newLine();
			writer.write("skull-timer = ", 0, 14);
			writer.write(Integer.toString(p.skullTimer), 0, Integer.toString(p.skullTimer).length());
			writer.newLine();
			writer.write("magic-book = ", 0, 13);
			writer.write(Integer.toString(p.playerMagicBook), 0, Integer.toString(p.playerMagicBook).length());
			writer.newLine();
			for (int b = 0; b < p.barrowsNpcs.length; b++) {
				writer.write("brother-info = ", 0, 15);
				writer.write(Integer.toString(b), 0, Integer.toString(b).length());
				writer.write("	", 0, 1);
				writer.write(p.barrowsNpcs[b][1] <= 1 ? Integer.toString(0) : Integer.toString(p.barrowsNpcs[b][1]), 0, Integer.toString(p.barrowsNpcs[b][1]).length());
				writer.newLine();
			}
			writer.write("special-amount = ", 0, 17);
			writer.write(Double.toString(p.specAmount), 0, Double.toString(p.specAmount).length());
			writer.newLine();
			writer.write("barrows-killcount = ", 0, 20);
			writer.write(Integer.toString(p.barrowsKillCount), 0, Integer.toString(p.barrowsKillCount).length());
			writer.newLine();
			writer.write("zkill = ", 0, 8);
			writer.write(Integer.toString(p.zkill), 0, Integer.toString(p.zkill).length());
			writer.newLine();
			
			writer.write("teleblock-length = ", 0, 19);
			writer.write(Integer.toString(tbTime), 0, Integer.toString(tbTime).length());
			writer.newLine();
			writer.write("pc-points = ", 0, 12);
			writer.write(Integer.toString(p.pcPoints), 0, Integer.toString(p.pcPoints).length());
			writer.newLine();
			writer.write("as-points = ", 0, 12);
			writer.write(Integer.toString(p.assaultPoints), 0, Integer.toString(p.assaultPoints).length());
			writer.newLine();
			// castlewars
			writer.write("skill-points = ", 0, 15);
			writer.write(Integer.toString(p.skillPoints), 0, Integer.toString(p.skillPoints).length());
			writer.newLine();
			
			/**
			 * donator shit
			 */
			writer.write("amount-donated = ", 0, 17);
			writer.write(Double.toString(p.getDonationAmount()), 0, Double
					.toString(p.getDonationAmount()).length());
			writer.newLine();
			writer.write("donationPointAmount = ", 0, 22);
			writer.write(Integer.toString(p.getDonationPoints()), 0, Integer
					.toString(p.getDonationPoints()).length());
			writer.newLine();
			/**
			 * end of donator shit
			 */
			
			writer.write("FMPoints = ", 0, 11);
			writer.write(Integer.toString(p.fireMakingPoints), 0, Integer.toString(p.fireMakingPoints).length());
			writer.newLine();
			writer.write("wcPoints = ", 0, 11);
			writer.write(Integer.toString(p.wcPoints), 0, Integer.toString(p.wcPoints).length());
			writer.newLine();
			writer.write("slay-points = ", 0, 14);
			writer.write(Integer.toString(p.slaypoints), 0, Integer.toString(p.slaypoints).length());
			writer.newLine();
			writer.write("agilityPoints = ", 0, 15);
			writer.write(Integer.toString(p.agilityPoints), 0, Integer.toString(p.agilityPoints).length());
			writer.newLine();
			writer.write("cookQP = ", 0, 9);
			writer.write(Integer.toString(p.cookQP), 0, Integer.toString(p.cookQP).length());
			writer.newLine();
			writer.write("undergroundQP = ", 0, 16);
			writer.write(Integer.toString(p.undergroundQP), 0, Integer.toString(p.undergroundQP).length());
			writer.newLine();
			writer.write("horrorQP = ", 0, 11);
			writer.write(Integer.toString(p.horrorQP), 0, Integer.toString(p.horrorQP).length());
			writer.newLine();
			writer.write("dragonQP = ", 0, 11);
			writer.write(Integer.toString(p.dragonQP), 0, Integer.toString(p.dragonQP).length());
			writer.newLine();
			writer.write("dtQP = ", 0, 7);
			writer.write(Integer.toString(p.dtQP), 0, Integer.toString(p.dtQP).length());
			writer.newLine();
			writer.write("run-energy = ", 0, 13);
			writer.write(Integer.toString(p.runEnergy), 0, Integer.toString(p.runEnergy).length());
			writer.newLine();
			writer.write("clue-container = ");
			if (p.clueContainer == null || p.clueContainer.stages.peek() == null) {
				writer.write("null");
			} else {
				for (ClueScroll c : p.clueContainer.stages)
					writer.write(c.name() + "\t");
			}
			writer.newLine();
			writer.write("clue-reward = ");
			writer.write(p.bossDifficulty == null ? "null" : p.bossDifficulty.name());
			writer.newLine();
			writer.write("easy-clue = ");
			writer.write(Integer.toString(p.easyClue));
			writer.newLine();
			writer.write("medium-clue = ");
			writer.write(Integer.toString(p.mediumClue));
			writer.newLine();
			writer.write("hard-clue = ");
			writer.write(Integer.toString(p.hardClue));
			writer.newLine();
			writer.write("elite-clue = ");
			writer.write(Integer.toString(p.eliteClue));
			writer.newLine();
			writer.write("hasSpokenToWC = ", 0, 16);
			writer.write(Boolean.toString(p.hasSpokenToWC), 0, Boolean.toString(p.hasSpokenToWC).length());
			writer.newLine();
			writer.write("isDonator = ");
			writer.write(Boolean.toString(p.isDonator), 0, Boolean.toString(p.isDonator).length());
			writer.newLine();
			writer.write("changedSymbol = ");
			writer.write(Boolean.toString(p.changedSymbol), 0, Boolean.toString(p.changedSymbol).length());
			writer.newLine();
			writer.write("MercenaryDone =");
			writer.write(Boolean.toString(p.Mercenary), 0, Boolean.toString(p.Mercenary).length());
			writer.newLine();
			writer.write("teleportStone = " + p.getTeleportStonePos()[0] + "\t" + p.getTeleportStonePos()[1] + "\t" + p.getTeleportStonePos()[2]);
			writer.newLine();
			writer.write("pinRegisteredDeleteDay = ", 0, 25);
			writer.write(Integer.toString(p.pinDeleteDateRequested), 0, Integer.toString(p.pinDeleteDateRequested).length());
			writer.newLine();
			writer.write("requestPinDelete = ", 0, 19);
			writer.write(Boolean.toString(p.requestPinDelete), 0, Boolean.toString(p.requestPinDelete).length());
			writer.newLine();
			writer.write("removeBankMessage = ", 0, 20);
			writer.write(Boolean.toString(p.removeBankMessage), 0, Boolean.toString(p.removeBankMessage).length());
			writer.newLine();
			writer.write("slayerTask = ", 0, 13);
			writer.write(Integer.toString(p.slayerTask), 0, Integer.toString(p.slayerTask).length());
			writer.newLine();
			writer.write("taskAmount = ", 0, 13);
			writer.write(Integer.toString(p.taskAmount), 0, Integer.toString(p.taskAmount).length());
			writer.newLine();
			writer.write("slayerTaskLevel = ", 0, 18);
			writer.write(Integer.toString(p.slayerTaskLevel), 0, Integer.toString(p.slayerTaskLevel).length());
			writer.newLine();
			writer.write("magePoints = ", 0, 13);
			writer.write(Integer.toString(p.magePoints), 0, Integer.toString(p.magePoints).length());
			writer.newLine();
			writer.write("votePoints = ", 0, 13);
			writer.write(Integer.toString(p.votePoints), 0, Integer.toString(p.votePoints).length());
			writer.newLine();
			writer.write("inJail = ", 0, 9);
			writer.write(Boolean.toString(p.inJail), 0, Boolean.toString(p.inJail).length());
			writer.newLine();
		    writer.write("voteExperienceMultiplier = ", 0, 27);
			writer.write(Long.toString(p.voteExperienceMultiplier));
			writer.newLine();
			writer.write("dropRateIncreaser = ");
			writer.write(Long.toString(p.dropRateIncreaser));
			writer.newLine();
			writer.write("DoublePKP = ");
			writer.write(Long.toString(p.DoublePKP));
			writer.newLine();
			writer.write("thievePoints = ", 0, 15);
			writer.write(Integer.toString(p.thievePoints), 0, Integer.toString(p.thievePoints).length());
			writer.newLine();
			writer.write("mode = ", 0, 7);
			writer.write(Integer.toString(p.diffLevel), 0, Integer.toString(p.diffLevel).length());
			writer.newLine();
			writer.write("haveRouge = ", 0, 11);
			writer.write(Boolean.toString(p.haveRouge), 0, Boolean.toString(p.haveRouge).length());
			writer.newLine();
			writer.write("poisonDamage = ");
			writer.write("" + p.poisonDamage);
			writer.newLine();
			writer.write("pkpoints = ", 0, 11);
			writer.write(Integer.toString(p.pkPoints), 0, Integer.toString(p.pkPoints).length());
			writer.newLine();
			writer.write("autoRet = ", 0, 10);
			writer.write(Boolean.toString(p.autoRetaliate));
			writer.newLine();
			writer.write("barrowskillcount = ", 0, 19);
			writer.write(Integer.toString(p.barrowsKillCount), 0, Integer.toString(p.barrowsKillCount).length());
			writer.newLine();
			writer.write("wave = ", 0, 7);
			writer.write(Integer.toString(p.waveId), 0, Integer.toString(p.waveId).length());
			writer.newLine();
			writer.write("gwkc = ", 0, 7);
			writer.write(Integer.toString(p.killCount), 0, Integer.toString(p.killCount).length());
			writer.newLine();
			writer.write("fightMode = ", 0, 12);
			writer.write(Integer.toString(p.fightMode), 0, Integer.toString(p.fightMode).length());
			writer.newLine();

			writer.write("void = ", 0, 7);
			String toWrite = p.voidStatus[0] + "\t" + p.voidStatus[1] + "\t" + p.voidStatus[2] + "\t" + p.voidStatus[3] + "\t" + p.voidStatus[4];
			writer.write(toWrite);
			writer.newLine();

			writer.write("quickprayer = ", 0, 14);
			String quick = "";
			for(int i = 0; i < p.getQuick().getNormal().length; i++) {
				quick += p.getQuick().getNormal()[i]+"\t";
			}
			writer.write(quick);
			writer.newLine();
			writer.write("DonorPoints = ");
			writer.write(Integer.toString(p.DonorPoints));
			writer.newLine();
			writer.write("isDonator = ");
			writer.write(Boolean.toString(p.isDonator), 0, Boolean.toString(p.isDonator).length());
			writer.newLine();
			writer.write("amountDonated = ");
			writer.write(Integer.toString(p.amountDonated));
			writer.newLine();
			writer.write("yells = ");
			writer.write(Integer.toString(p.yells));
			writer.newLine();
			writer.write("altarRestore =");
			writer.write(Boolean.toString(p.canRestoreSpecial));
			writer.write("modeTut = ");
			writer.write(Boolean.toString(p.modeTut), 0, Boolean.toString(p.modeTut).length());
			writer.newLine();
			writer.write("splitChat = ");
			writer.write(Boolean.toString((p.splitChat)));
			writer.newLine();
			writer.write("isClanMuted = ");
			writer.write(Boolean.toString(p.isClanMuted));
			writer.newLine();
			writer.write("hasClaimedStarter = ");
			writer.write(Boolean.toString((p.hasClaimedStarter)));
			writer.newLine();
			writer.write("gameMode = ", 0, 11);
			writer.write(Integer.toString(p.gameMode), 0, Integer.toString(p.gameMode).length());
			writer.newLine();
			writer.write("isSocialSlaying = " + p.isSocialSlaying());
			writer.newLine();
			writer.write("socialSlayerKills = " + p.getSocialSlayerKills());
			writer.newLine();
			writer.write("socialSlayerPoints = " + p.getSocialSlayerPoints());
			writer.newLine();
			writer.write("socialSlayerPlayer = " + p.getLastSocialPlayer());
			writer.newLine();
			writer.write("socialSlayerDisconnections = " + p.getSocialSlayerDisconnections());
			writer.newLine();
			writer.write("canUseFoodOnPlayer = " + p.isCanUseFoodOnPlayer());
			writer.newLine();
			writer.write("canUsePotionOnTask = " + p.isCanSharePotion());
			writer.newLine();
			for (int i = 0; i < p.removedTasks.length; i++) {
				writer.write("removedTask" + i + " = ", 0, 15);
				writer.write(Integer.toString(p.removedTasks[i]), 0, Integer.toString(p.removedTasks[i]).length());
				writer.newLine();
			}
			writer.newLine();
			writer.write("play-time = ", 0, 12);
			writer.write(Integer.toString(p.pTime), 0, Integer.toString(p.pTime).length());
			writer.newLine();
			writer.write("loyaltyTitle = ", 0, 15);
			writer.write(p.loyaltyTitle, 0, p.loyaltyTitle.length());
			writer.newLine();
			writer.write("creature-potion = " + p.getCreaturePotionTimer());
			writer.newLine();
			writer.write("achievement-points = " + p.getAchievements().getPoints());
			writer.newLine();
			writer.write("achievement-items = ");
			for (int i = 0; i < p.getAchievements().getBoughtItems().length; i++) {
				writer.write("" + p.getAchievements().getBoughtItems()[i][1] + ((i == p.getAchievements().getBoughtItems().length - 1) ? "" : "\t"));
			}
			writer.newLine();
			writer.write("option-display-name = " + p.getGameOptions().isDisplayNameActive());
			writer.newLine();
			for (int i = 0; i < p.shownDefender.length; i++) {
				writer.write("shownDefender[" + i + "] = ", 0, 19);
				writer.write(Boolean.toString(p.shownDefender[i]), 0, Boolean.toString(p.shownDefender[i]).length());
				writer.newLine();
			}
			writer.write("bank-pin = " + p.getBankPin().getPin());
			writer.newLine();
			writer.write("bank-pin-cancellation = " + p.getBankPin().isAppendingCancellation());
			writer.newLine();
			writer.write("bank-pin-cancellation-delay = " + p.getBankPin().getCancellationDelay());
			writer.newLine();
			writer.newLine();
			writer.write("[RESOURCE-BAG]");
			writer.newLine();
			for (int i = 0; i < p.resourceItemId.length; i++) {
				writer.write("item = " + i + "\t" + p.resourceItemId[i] + "\t" + p.resourceItemAmount[i]);
				writer.newLine();
			}
			writer.newLine();
			
			writer.newLine();
			
		
			writer.newLine();

			/* EQUIPMENT */
			writer.write("[EQUIPMENT]", 0, 11);
			writer.newLine();
			for (int i = 0; i < p.playerEquipment.length; i++) {
				writer.write("character-equip = ", 0, 18);
				writer.write(Integer.toString(i), 0, Integer.toString(i).length());
				writer.write("	", 0, 1);
				writer.write(Integer.toString(p.playerEquipment[i]), 0, Integer.toString(p.playerEquipment[i]).length());
				writer.write("	", 0, 1);
				writer.write(Integer.toString(p.playerEquipmentN[i]), 0, Integer.toString(p.playerEquipmentN[i]).length());
				writer.write("	", 0, 1);
				writer.newLine();
			}
			writer.newLine();

			/* LOOK */
			writer.write("[LOOK]", 0, 6);
			writer.newLine();
			for (int i = 0; i < p.playerAppearance.length; i++) {
				writer.write("character-look = ", 0, 17);
				writer.write(Integer.toString(i), 0, Integer.toString(i).length());
				writer.write("	", 0, 1);
				writer.write(Integer.toString(p.playerAppearance[i]), 0, Integer.toString(p.playerAppearance[i]).length());
				writer.newLine();
			}
			writer.newLine();

			/* SKILLS */
			writer.write("[SKILLS]", 0, 8);
			writer.newLine();
			for (int i = 0; i < p.playerLevel.length; i++) {
				writer.write("character-skill = ", 0, 18);
				writer.write(Integer.toString(i), 0, Integer.toString(i).length());
				writer.write("	", 0, 1);
				writer.write(Integer.toString(p.playerLevel[i]), 0, Integer.toString(p.playerLevel[i]).length());
				writer.write("	", 0, 1);
				writer.write(Integer.toString(p.playerXP[i]), 0, Integer.toString(p.playerXP[i]).length());
				writer.newLine();
			}
			writer.newLine();

			/* ITEMS */
			writer.write("[ITEMS]", 0, 7);
			writer.newLine();
			for (int i = 0; i < p.playerItems.length; i++) {
				if (p.playerItems[i] > 0) {
					writer.write("character-item = ", 0, 17);
					writer.write(Integer.toString(i), 0, Integer.toString(i).length());
					writer.write("	", 0, 1);
					writer.write(Integer.toString(p.playerItems[i]), 0, Integer.toString(p.playerItems[i]).length());
					writer.write("	", 0, 1);
					writer.write(Integer.toString(p.playerItemsN[i]), 0, Integer.toString(p.playerItemsN[i]).length());
					writer.newLine();
				}
			}
			writer.newLine();

			writer.write("[BANK]", 0, 6);
			writer.newLine();
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < Constants.BANK_SIZE; j++) {
					if (j > p.getBank().getBankTab()[i].size() - 1)
						break;
					BankItem item = p.getBank().getBankTab()[i].getItem(j);
					if (item == null)
						continue;
					writer.write("bank-tab = " + i + "\t" + item.getId() + "\t" + item.getAmount());
					writer.newLine();
				}
			}

			writer.newLine();
			writer.newLine();

			/* FRIENDS */
			writer.write("[FRIENDS]", 0, 9);
			writer.newLine();
			for (int i = 0; i < p.friends.length; i++) {
				if (p.friends[i] > 0) {
					writer.write("character-friend = ");
					writer.write(Integer.toString(i), 0, Integer.toString(i).length());
					writer.write("	", 0, 1);
					writer.write("" + p.friends[i]);
					writer.newLine();
				}
			}
			writer.newLine();
			/*
			 * Prestiging
			 */
			writer.write("[PRESTIGE]", 0, 10);
			writer.newLine();
			writer.write("Prestige =  ", 0, 11);
			writer.write(Integer.toString(p.prestigeLevel), 0, Integer.toString(p.prestigeLevel).length());
			writer.newLine();
			writer.write("Points = ", 0, 9);
			writer.write(Integer.toString(p.prestigePoints), 0, Integer.toString(p.prestigePoints).length());
			writer.newLine();
			writer.write("PrestigeTitle = ", 0, 16);
			writer.write(Integer.toString(p.prestigeTitle), 0, Integer.toString(p.prestigeTitle).length());
			writer.newLine();

			/* IGNORES */
			writer.write("[IGNORES]", 0, 9);
			writer.newLine();
			for (int i = 0; i < p.ignores.length; i++) {
				if (p.ignores[i] > 0) {
					if (p.ignores[i] > 0) {
						writer.write("character-ignore = ");
						writer.write(Integer.toString(i), 0, Integer.toString(i).length());
						writer.write("\t");
						writer.write("" + p.ignores[i]);
						writer.newLine();
					}
				}
			}
			writer.newLine();
			writer.newLine();
			writer.write("[KILLS]");
			writer.newLine();
			for (int i = 0; i < p.lastKilledList.size(); i++) {
				if (p.lastKilledList.get(i) != null && !p.lastKilledList.get(i).equalsIgnoreCase("null")) {
					writer.write("last-killed = " + p.lastKilledList.get(i));
					writer.newLine();
				}
			}
			writer.newLine();
			writer.write("[DEGRADEABLE]");
			writer.newLine();
			writer.write("[PVP]");
			writer.newLine();
			writer.write("wilderness-potential = " + Double.toString(p.getWildernessPotential()));
			writer.newLine();
			writer.write("killstreak = " + Integer.toString(p.killStreak));
			writer.newLine();
			writer.write("max-killstreak = " + Integer.toString(p.highestKillStreak));
			writer.newLine();

			/**
			 * Achievements
			 */
			writer.newLine();
			writer.write("[ACHIEVEMENTS-TIER-1]");
			writer.newLine();
			p.getAchievements().print(writer, 0);
			writer.newLine();
			writer.newLine();
			writer.write("[ACHIEVEMENTS-TIER-2]");
			writer.newLine();
			p.getAchievements().print(writer, 1);
			writer.newLine();
			writer.newLine();
			writer.write("[ACHIEVEMENTS-TIER-3]");
			writer.newLine();
			p.getAchievements().print(writer, 2);
			writer.newLine();

			writer.write("[BOSS-TRACKER]");
			writer.newLine();
			for (Entry<NPCDeathTracker.NPCName, Integer> entry : p.getBossDeathTracker().tracker.entrySet()) {
				if (entry != null) {
					if (entry.getValue() > 0) {
						writer.write(entry.getKey().toString() + " = " + entry.getValue());
						writer.newLine();
					}
				}
			}
			writer.newLine();
			writer.write("[SLAYER-TRACKER]");
			writer.newLine();
			for (Entry<NPCSlayerDeathTracker.NPCNAMES, Integer> entrys : p.getSlayerDeathTracker().tracking.entrySet()) {
				if (entrys != null) {
					if (entrys.getValue() > 0) {
						writer.write(entrys.getKey().toString() + " = " + entrys.getValue());
						writer.newLine();
					}
				}
			}
			writer.newLine();
			writer.write("[NPC-PETS]");
			writer.newLine();
			for (Pet pet : Pet.values()) {
				writer.write(pet.getItemId() + " = " + p.getPet().isPetOwner(pet.getItemId()));
				writer.newLine();
			}
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean passwordMatches(String name, String password) throws IOException {
		if (!playerExists(name)) {
			return false;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(new File("./data/characters/" + name + ".txt")))) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				int spot = line.indexOf("=");
				if (spot > -1) {
					String key = line.substring(0, spot).trim();
					String value = line.substring(spot + 1).trim();
					if (key != null && value != null) {
						if (key.equals("character-password")) {
							reader.close();
							return password.equalsIgnoreCase(value) || Misc.basicEncrypt(password).equals(value) || Misc.md5Hash(password).equals(value);
						}
					}
				}
			}
			reader.close();
		}
		return false;
	}

	public static boolean playerExists(String name) {
		File file = null;
		file = new File("./data/characters/" + name + ".txt");
		return file != null && file.exists();
	}
}