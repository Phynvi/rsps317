package com.bclaus.rsps.server.vd.npc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueDifficulty;
import com.bclaus.rsps.server.vd.content.cluescroll.ClueScrollHandler;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.mobile.MobileCharacterList;
import com.bclaus.rsps.server.vd.npc.boss.Bosses;
import com.bclaus.rsps.server.vd.npc.impl.KQ;
import com.bclaus.rsps.server.vd.npc.pets.Pet;
import com.bclaus.rsps.server.vd.player.Boundary;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.content.achievements.AchievementType;
import com.bclaus.rsps.server.vd.content.achievements.Achievements;
import com.bclaus.rsps.server.vd.content.minigames.PestControl;
import com.bclaus.rsps.server.vd.content.minigames.warriorsguild.SuiteOfArmour;
import com.bclaus.rsps.server.vd.content.minigames.warriorsguild.WarriorsGuild;
import com.bclaus.rsps.server.vd.content.skills.impl.hunter.HunterHandler;
import com.bclaus.rsps.server.vd.content.skills.impl.slayer.Slayer;
import com.bclaus.rsps.server.vd.content.skills.impl.slayer.SocialSlayerData;
import com.bclaus.rsps.server.vd.items.Item;
import com.bclaus.rsps.server.vd.mobile.Hit;
import com.bclaus.rsps.server.vd.mobile.PoisonCombatTask;
import com.bclaus.rsps.server.vd.npc.boss.Boss;
import com.bclaus.rsps.server.vd.npc.boss.ProtectionPrayer;
import com.bclaus.rsps.server.vd.npc.boss.challenge.ChallengeBoss;
import com.bclaus.rsps.server.vd.npc.boss.challenge.Rewards;
import com.bclaus.rsps.server.vd.npc.drops.DropTable;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.util.RandomGen;

public final class NPCHandler {

	public static final MobileCharacterList<NPC> NPCS = new MobileCharacterList<>(2000);

	public NPCHandler() {
		for (int i = 0; i < NPCS.capacity(); i++) {
			NPCDefinitions.getDefinitions()[i] = null;
		}

		loadNPCList("./data/cfg/npc.cfg");
		loadAutoSpawn("./data/cfg/spawn-config.cfg");
	}

	public static boolean isAlive(NPC npc, Boundary boundary) {
		if (npc != null)
			if (Boundary.isInBounds(npc, boundary))
				return true;
		return false;
	}

	public static NPC getNpc(int npcType) {
		for (NPC npc : NPCS)
			if (npc != null && npc.npcType == npcType && !npc.needRespawn)
				return npc;
		return null;
	}

	public static void removeNpc(NPC npc) {
		npc.setAbsX(0);
		npc.setAbsY(0);
	}

	public void multiAttackGfx(int i) {
		if (NPCHandler.NPCS.get(i).projectileId < 0) {
			return;
		}
		for (int j = 0; j < World.PLAYERS.capacity(); j++) {
			if (World.PLAYERS.get(j) != null) {
				Player c = World.PLAYERS.get(j);
				if (c.heightLevel != NPCHandler.NPCS.get(i).heightLevel) {
					continue;
				}
				if (Player.goodDistance(c.getAbsX(), c.getAbsY(), NPCHandler.NPCS.get(i).getAbsX(), NPCHandler.NPCS.get(i).getAbsY(), 15)) {
					int nX = NPCHandler.NPCS.get(i).getX() + offset(i);
					int nY = NPCHandler.NPCS.get(i).getY() + offset(i);
					int pX = c.getX();
					int pY = c.getY();
					int offX = (nY - pY) * -1;
					int offY = (nX - pX) * -1;
					c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i), NPCHandler.NPCS.get(i).projectileId, 43, 31, -c.getIndex() - 1, 65);
				}
			}
		}
	}

	public boolean switchesAttackers(NPC npc) {
		if (npc == null)
			return false;
		if (Bosses.isBoss(npc.npcType)) {
			return Bosses.get(npc.npcType).switchesAttackers();
		}
		switch (npc.npcType) {
		case 3847:
		case 3943:
		case 4972:
		case 3649: // halloween event npc, change this!
		case 2551:
		case 2552:
		case 2553:
		case 2559:
		case 2560:
		case 2561:
		case 2563:
		case 2564:
		case 2565:
		case 2894:
		case 6263:
		case 6265:
		case 6261:
		case 6208:
			return true;

		}

		return false;
	}

	static RandomGen random = new RandomGen();

	public boolean isAggressive(NPC npc) {
		if (Bosses.isBoss(npc.npcType)) {
			return Bosses.get(npc.npcType).isAggressive();
		}
		if (PestControl.npcIsPCMonster(npc.npcType)) {
			return true;
		}
		switch (npc.npcType) {
		case 1265:
		case 4189:
		case 4383:
		case 912:
		case 913:
		case 914:
		case 3943:
		case 3847:
		case 4972:
		case 4971:
		case 1158:
		case 1160:
		case 795:
		case 6223:
		case 6225:
		case 6227:
		case 6204:
		case 6206:
		case 1604:
		case 6208:
		case 6252:
		case 6250:
		case 6248:
		case 6263:
		case 6265:
		case 6261:
		case 1459:
		case 50:
		case 742:
		case 1926: // Bandit ^ Leave
		case 6222:
		case 6259:
		case 5363:
		case 1471:
		//case 2060:
		case 4186:
		case 5905:
		case 2883:
			return true;
		}

		return npc.inWild() && npc.MaxHP > 0;
	}

	
	public static NPC spawnNpc(Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon) {
		NPC npc = new NPC(npcType);
		npc.absX = x;
		npc.absY = y;
		npc.makeX = x;
		npc.makeY = y;
		npc.heightLevel = heightLevel;
		npc.walkingType = WalkingType;
		npc.HP = HP;
		npc.MaxHP = HP;
		npc.maxHit = maxHit;
		npc.attack = attack;
		npc.defence = defence;
		npc.spawnedBy = c.getIndex();
		if (attackPlayer) {
			npc.underAttack = true;
			if (c != null) {
				npc.killerId = c.getIndex();
			}
		}
		NPCHandler.NPCS.add(npc);
		if (npcType >= 4278 && npcType <= 4284) {
			npc.forceChat("I'M ALIVE!");
			npc.forceAnim(4410);
		}
		if (Pet.isPet(npc.npcType)) {
			npc.underAttack = true;
			npc.killerId = c.getIndex();
			c.getPet().setNpc(npc);
		}
		if (headIcon) {
			c.getPA().drawHeadicon(1, npc.getIndex(), 0, 0);
		}
		return npc;
	}

	public static void spawnNpc2(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence) {
		NPC newNPC = new NPC(npcType);
		newNPC.setAbsX(x);
		newNPC.setAbsY(y);
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		// newNPC.combatLevel = NpcList[slot].npcCombat;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		NPCHandler.NPCS.add(newNPC);
	}

	public static Optional<NPC> spawnNpc3(Player c, int npcType, int x, int y, int heightLevel, ClueDifficulty d) {
		NPCDefinitions def = NPCDefinitions.getDefinitions()[npcType];
		if (d == ClueDifficulty.EASY) {
			return Optional.of(spawnNpc(c, npcType, x, y, heightLevel, 0, def.getNpcHealth(), 10, 25, 0, true, true));
		} else if (d == ClueDifficulty.MEDIUM) {
			return Optional.of(spawnNpc(c, npcType, x, y, heightLevel, 0, def.getNpcHealth(), 15, 50, 25, true, true));
		} else if (d == ClueDifficulty.HARD) {
			return Optional.of(spawnNpc(c, npcType, x, y, heightLevel, 0, def.getNpcHealth(), 20, 75, 99, true, true));
		} else if (d == ClueDifficulty.ELITE) {
			return Optional.of(spawnNpc(c, npcType, x, y, heightLevel, 0, def.getNpcHealth(), 35, 99, 120, true, true));
		}
		return Optional.empty();
	}

	public NPC spawnNpc(int id, int x, int y, int heightLevel) {
		NPC newNPC = new NPC(id);
		newNPC.setAbsX(x);
		newNPC.setAbsY(y);
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = 0;
		newNPC.HP = 50;
		newNPC.MaxHP = 50;
		// newNPC.combatLevel = NpcList[slot].npcCombat;
		newNPC.maxHit = 0;
		newNPC.attack = 0;
		newNPC.defence = 0;
		NPCHandler.NPCS.add(newNPC);
		return newNPC;
	}

	/**
	 * Emotes
	 */
	public static int getAttackEmote(int i) {
		if (Bosses.isBoss(NPCHandler.NPCS.get(i).npcType)) {
			return Bosses.get(NPCHandler.NPCS.get(i).npcType).getAttackEmote();
		}
		if (NPCHandler.NPCS.get(i).npcType >= 3732 && NPCHandler.NPCS.get(i).npcType <= 3741) {
			return 3901;
		}
		
		if (NPCHandler.NPCS.get(i).npcType >= 3742 && NPCHandler.NPCS.get(i).npcType <= 3746) {
			return 3915;
		}
		if (NPCHandler.NPCS.get(i).npcType >= 3747 && NPCHandler.NPCS.get(i).npcType <= 3751) {
			return 3908;
		}
		if (NPCHandler.NPCS.get(i).npcType >= 3752 && NPCHandler.NPCS.get(i).npcType <= 3761) {
			return 3880;
		}
		if (NPCHandler.NPCS.get(i).npcType >= 3762 && NPCHandler.NPCS.get(i).npcType <= 3771) {
			return 3920;
		}
		if (NPCHandler.NPCS.get(i).npcType >= 3772 && NPCHandler.NPCS.get(i).npcType <= 3776) {
			return 3896;
		}

		switch (NPCHandler.NPCS.get(i).npcType) {
		case 1632:
			return 1595;
		case 1608:
			return 1512;
		case 1604:
			return 1507;
		case 4189:
			return 3538;
		case 4357:
			return 577;
		case 4389: // Flesh crawler
			return 1184;

		case 6300:
			return 6866;
		case 3847:
			if (NPCHandler.NPCS.get(i).attackType == 2) {
				return 3991;
			} else {
				return 3992;
			}
		case 126:
			return 2310;
		case 3068:
			return 2982;
		case 4972: // giant roc
			return 5024;
		case 4971: // baby roc
			return 5031;
		case 3493:
			return 3501;
		case 2060:// slash bash
			return 359;
		case 5902:// inadequacy
			return 6318;
		case 5903:
			return 6344;
		case 3494:
			return 1750;
		case 3496:
			return 3508;
		case 3497:
			return 1341;
		case 1471:
			return 5519;
		case 1465:
			return 5521;
		case 1459:
			return 1402;
		case 2550:
			if (NPCHandler.NPCS.get(i).attackType == 0) {
				return 7060;
			} else {
				return 7063;
			}
		case 1593:
		case 49:
			return 6579;
		case 3340:
			return 3312;
		case 2892:
		case 2894:
			return 2868;
		case 1158: // kalphite queen
			if (NPCHandler.NPCS.get(i).attackType == 2) {
				return 6240;
			}
			if (NPCHandler.NPCS.get(i).attackType == 0) {
				return 6241;
			} else {
				return 6240;
			}
		case 1160: // kalphite queen form 2
			if (NPCHandler.NPCS.get(i).attackType == 2) {
				return 6237;
			}
			if (NPCHandler.NPCS.get(i).attackType == 0) {
				return 6235;
			} else {
				return 6237;
			}
		case 103:// Ghost
		case 655:// Tree Spirit
			return 123;
		case 6248:
			return 6376;
		case 6250:
			return 7018;
		case 6252:
			return 7009;
		case 6225:
		case 6227:
		case 6223:
			return 6953;
		case 2627:
			return 2621;
		case 1600:
			return 227;
		case 2630:
			return 2625;
		case 6263:
			return 6154;
		case 6261:
		case 6265:
			return 6154;
		case 2631:
			return 2633;
		case 2741:
			return 2637;
		case 2746:
			return 2637;
		case 2607:
			return 2611;
		case 2743:// 360
			return 2647;
			// bandos gwd
		case 2551:
		case 2552:
		case 2553:
			return 6154;
			// end of gwd
			// arma gwd
		case 2558:
			return 3505;
		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
			return 6223;
		case 2560:
			return 6953;
		case 2559:
			return 6952;
		case 2561:
			return 6954;
		case 6222:
			return 6977;
		case 6273:
			return 4320;
		case 6267:
			return 359;
		case 6268:
			return 2930;
		case 6269:
			return 4652;
		case 6270:
			return 4652;
		case 6271:
			return 4320;
		case 6272:
			return 4320;
		case 6274:
			return 4320;
			// End Godwars
		case 2562:
			return 6964;
		case 2563:
			return 6376;
		case 2564:
			return 7018;
		case 2565:
			return 7009;
			// end of sara gwd
		case 13: // wizards
			return 711;

		
		case 4179:
		case 1624:
			return 1557;

		case 1648:
			return 1590;

		case 2783: // dark beast
			return 2732;

		case 1615: // abby demon
			return 1537;

		case 1613: // nech
			return 1528;

		case 1610:
		case 1611: // garg
			return 1519;

		case 1616: // basilisk
			return 1546;

		case 92:
		case 90: // skele
			return 5485;

		case 50:// drags
		case 5363:
		case 742:
		case 53:
		case 54:
		case 55:
		case 941:
		case 4682:
		case 5362:
		case 1590:
		case 1591:
		case 1592:
		case 3590:
			return 80;

		case 124: // earth warrior
			return 390;

		case 803: // monk
			return 422;

		case 52: // baby drag
			return 25;

		case 58: // Shadow Spider
		case 59: // Giant Spider
		case 60: // Giant Spider
		case 61: // Spider
		case 62: // Jungle Spider
		case 63: // Deadly Red Spider
		case 64: // Ice Spider
		case 134:
			return 143;

		case 105: // Bear
		case 106: // Bear
			return 41;

		case 412:
		case 78:
			return 4915;
			
		case 5247://Penance Queen
			return 5411;	

		case 47:
		case 2033: // rat
			return 138;

		case 2031: // bloodworm
			return 2070;

		case 101: // goblin
			return 6184;

		case 5529:
		case 81: // cow
			return 5849;

		case 21: // hero
			return 451;

		case 41: // chicken
		case 2463:
		case 2464:
		case 2465:
		case 2466:
		case 2467:
		case 2468:
			return 5387;

		case 9: // guard
		case 32: // guard
		case 20: // paladin
			return 451;

		case 1338: // dagannoth
		case 1340:
		case 2454:
		case 1351:
		case 1342:
			return 1341;

		case 19: // white knight
			return 406;

		case 110:
		case 111: // ice giant
		case 117:
		case 112:
		case 1582:
		case 4291:
			return 4651;

		case 2452:
			return 1312;

		case 2889:
			return 2859;

		case 118:
		case 119:
			return 99;

		case 6204:
		case 6206:
		case 6208:
		case 82:// Lesser Demon
		case 83:// Greater Demon
		case 84:// Black Demon
			return 64;

		case 1267:
		case 1265:
			return 1312;

		case 125: // ice warrior
		case 178:
			return 451;

		case 123:
		case 122:
			return 164;

		case 2028: // karil
			return 2075;

		case 2025: // ahrim
			return 729;

		case 2026: // dharok
			return 2067;

		case 2027: // guthan
			return 2080;

		case 2029: // torag
			return 0x814;

		case 2030: // verac
			return 2062;
		case 3200:
		case 2837:
			return 3146;

		case 2745:
			if (NPCHandler.NPCS.get(i).attackType == 2) {
				return 2656;
			} else if (NPCHandler.NPCS.get(i).attackType == 1) {
				return 2652;
			} else if (NPCHandler.NPCS.get(i).attackType == 0) {
				return 2655;
			}

		default:
			return 0x326;
		}
	}

	public int getDeadEmote(int i) {
		Player c = World.PLAYERS.get(NPCHandler.NPCS.get(i).killedBy);
		if (c != null) {
			if (NPCHandler.NPCS.get(i).npcType == c.slayerTask && !c.slayerBossTask) {
				c.getPA().addSkillXP(NPCHandler.NPCS.get(i).MaxHP * Constants.SLAYER_EXPERIENCE, 18);
				c.taskAmount--;
			}

			if (ChallengeBoss.spawned == true) {
				switch (NPCHandler.NPCS.get(i).npcType) {
				case 3847:
					Rewards.random(c, Rewards.HIGH);
					break;
				case 4175:
					Rewards.random(c, Rewards.MEDIUM);
					break;
				case 4172:
					Rewards.random(c, Rewards.LOW);
					break;
				}
				ChallengeBoss.spawned = false;
			}
			if (!c.isSocialSlaying() && !c.slayerBossTask && c.taskAmount < 1 && NPCHandler.NPCS.get(i).npcType == c.slayerTask) {
				c.getPA().addSkillXP((NPCHandler.NPCS.get(i).MaxHP * 8) * Constants.SLAYER_EXPERIENCE, 18);
				int points = Slayer.getDifficulty(c.slayerTask) * 4;
				c.slayerTask = -1;
				c.slaypoints += points;
				c.sendMessage("You completed your slayer task. You obtain " + points + " slayer points. Please talk to Vanakka.");
				Achievements.increase(c, AchievementType.SLAYER, 1);
				c.getPA().loadAllQuests(c);
			}
			if (c.slayerBossTask && NPCHandler.NPCS.get(i).npcType == c.slayerTask) {
				c.getPA().addSkillXP(NPCHandler.NPCS.get(i).MaxHP * 36, 18);
				c.taskAmount--;
			}
			if (c.slayerBossTask && c.taskAmount < 1 && NPCHandler.NPCS.get(i).npcType == c.slayerTask) {
				c.sendMessage("You completed your slayer task. You obtain 24 slayer points. Please talk to Vanakka.");
				c.slayerBossTask = false;
				c.slaypoints += 24;
				c.slayerTask = -1;
				c.getPA().loadAllQuests(c);
			}
			if (c.taskAmount <= 0) {
				if (c.isSocialSlaying()) {
					SocialSlayerData.finishTask(c);
					return i;
				}
			}
			if (c.isSocialSlaying() && NPCHandler.NPCS.get(i).npcType == c.slayerTask) {
				if (c.getSocialSlayer() != null && c.getSocialSlayer().getPartner() != null) {
					c.getSocialSlayer().getPartner().taskAmount--;
					c.setSocialSlayerKills(c.getSocialSlayerKills() + 1);
				}
			}
		}
		if (Bosses.isBoss(NPCHandler.NPCS.get(i).npcType) && NPCHandler.NPCS.get(i).npcType != 2042) {
			return Bosses.get(NPCHandler.NPCS.get(i).npcType).getDeathEmote();
		}
		if (NPCHandler.NPCS.get(i).npcType >= 3732 && NPCHandler.NPCS.get(i).npcType <= 3741) {
			return 3903;
		}
		if (NPCHandler.NPCS.get(i).npcType >= 3742 && NPCHandler.NPCS.get(i).npcType <= 3746) {
			return 3917;
		}
		if (NPCHandler.NPCS.get(i).npcType >= 3747 && NPCHandler.NPCS.get(i).npcType <= 3751) {
			return 3909;
		}
		if (NPCHandler.NPCS.get(i).npcType >= 3752 && NPCHandler.NPCS.get(i).npcType <= 3761) {
			return 3881;
		}
		if (NPCHandler.NPCS.get(i).npcType >= 3762 && NPCHandler.NPCS.get(i).npcType <= 3771) {
			return 3922;
		}
		if (NPCHandler.NPCS.get(i).npcType >= 3772 && NPCHandler.NPCS.get(i).npcType <= 3776) {
			return 3894;
		}
		switch (NPCHandler.NPCS.get(i).npcType) {
		case 2042:
		case 2043:
		case 2044:
			return 5072;
		case 4188:
		case 4189:
			return -1;
		case 4186:
			c.getZulrah().spawnExplorer(c);
			NPCHandler.NPCS.get(i).stage = 0;
			return -1;

		case 4357:
			return 4236;
		case 4389:
			return 1190;
		case 4382:
			return 819;
		case 912:
		case 913:
		case 914:
			KQ.appendMageCount(i);
			return -1;
		case 3068:
			return 645;
		case 6267:
		case 6268:
		case 6269:
		case 6270:
		case 6271:
		case 6272:
		case 6273:
		case 6274:
			KQ.appendKillCount(i);
			return 4321;
		case 253:
		case 258:
			KQ.appendAssaultCount(i);
			return 2304;
		case 1158:
			KQ.spawnSecondForm(i);
			return 6242;
		case 3847:
			return 3993;
		case 4972: // giant roc
			return 5027;
		case 4971: // baby roc
			return 5033;
		case 2030:
		case 2029:
		case 2028:
		case 2027:
		case 2026:
		case 2025:
			KQ.killedBarrow(i);
			KQ.killedCrypt(i);
			return 2304;
		case 3493:
			return 3503;
		case 2060:// slash bash
			return 361;
		case 5902:// inadequacy
			bossKilled(i);
			return 6322;
		case 5903://gotta use this boss
			return 6343;
		case 3494://flamebeed
			return 1752;
		case 3496:
			return 3509;
		case 3497:
			return 1342;
		case 1471:
			return 5521;
		case 1465:
			return 1384;
		case 3340:
			return 3310;
		case 1593:
		case 49:
			return 6576;
		case 92:
			return 5491;
		case 6248:
			return 6377;
		case 6250:
			return 7016;
		case 6252:
			return 7011;
		case 6225:
		case 6227:
		case 6223:
			return 6956;
		case 1600:
			return 228;
		case 1265:
			return 1314;
		case 6263:
			return 6156;
		case 6265:
			return 6156;
		case 1459:
			return 1404;
		case 110:
		case 117:
			return 4651;
		case 2562:
			return 6965;
		case 2563:
			return 6377;
		case 2564:
			return 7016;
		case 2565:
			return 7011;
		case 2551:
		case 2552:
		case 2553:
			return 6156;
		case 2550:
			return 7062;
		case 6222:
		case 1612: // banshee
			return 1524;
		case 2558:
			return 3503;
		case 2559:
		case 2560:
		case 2561:
			return 6956;
		case 2607:
			return 2607;
		case 2627:
			return 2620;
		case 2630:
			return 2627;
		case 2631:
			return 2630;
		case 2738:
			return 2627;
		case 2741:
			return 2638;
		case 2746:
			return 2638;
		case 2743:
			return 2646;
		case 2745:
			handleJadDeath(i);
			return 2654;
		case 6142:
		case 6143:
		case 6144:
		case 6145:
		case 6150:
		case 6151:
		case 6152:
		case 6153:
			return -1;
		case 3200:
		case 2837:
			return 3147;
		case 2035: // spider
			return 146;
		case 47:
		case 2033: // rat
			return 141;
		case 2031: // bloodvel
			return 2073;
		case 101: // goblin
			return 6182;
		case 5529:
		case 81: // cow
			return 5851;
		case 41: // chicken
			return 5389;
		case 1338: // dagannoth
		case 1340:
		case 1342:
		case 1351:
		case 2454:
			return 1342;
		case 111: // ice giant
			return 131;
		case 125: // ice warrior
			return 843;
		case 751:// Zombies!!
			return 302;
		case 1626:
		case 1627:
		case 1628:
		case 1629:
		case 1630:
		case 1631:
		case 1632: // turoth!
			return 1597;
		case 1616: // basilisk
			return 1548;
		case 1653: // hand
			return 1590;
		case 6206:
		case 6208:
		case 82:// demons
		case 83:
		case 84:
			return 67;

		case 1605:// abby spec
			return 1508;

		case 51:// baby drags
		case 52:
		case 1589:
		case 3376:
			return 28;

		case 1610:
		case 1611:
			return 1518;

		case 1618:
		case 1619:
			return 1553;

		case 1620:
		case 1621:
			return 1563;

		case 2783:
			return 2733;

		case 1615:
			return 1538;

		case 1624:
		case 4179:
			return 1558;

		case 1613:
			return 1530;

		case 1633:
		case 1634:
		case 1635:
		case 1636:
			return 1580;

		case 1648:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1654:
		case 1655:
		case 1656:
		case 1657:
			return 1590;

		case 100:
		case 102:
			return 313;

		case 105:
		case 106:
			return 44;

		case 412:
		case 78:
			return 4917;

		case 122:
		case 123:
			return 167;

		case 58:
		case 59:
		case 60:
		case 61:
		case 62:
		case 63:
		case 64:
		case 134:
			return 146;

		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
			return 6230;

		case 103:
		case 104:
			return 5542;

		case 118:
		case 119:
			return 102;

		case 50:// drags
		case 5363:
		case 742:
		case 53:
		case 54:
		case 55:
		case 941:
		case 4682:
		case 5362:
		case 1590:
		case 1591:
		case 1592:
		case 3590:
			return 92;
		case 5993:// Experiment No.2
			return 6512;

		case 6212:// Werewolf
		case 6213:// Werewolf
			return 6537;

		case 6285:// Warped Terrorbird
		case 6293:// Warped Terrorbird
			return 7109;

		case 6296:// Warped Tortoise
		case 6297:// Warped Tortoise
			return 7091;

		case 5229:// Penance ranger
		case 5230:// Penance ranger
		case 5231:// Penance ranger
		case 5232:// Penance ranger
		case 5233:// Penance ranger
		case 5234:// Penance ranger
		case 5235:// Penance ranger
		case 5236:// Penance ranger
		case 5237:// Penance ranger
			return 5397;

		case 5247:// Penance Queen
			return 5412;

		case 75:// Zombie
		case 6763:// Dried Zombie
			return 5569;

		case 5248:// Queen Spawn
			return 5093;

		case 5452:// Icelord
		case 5453:// Icelord
		case 5454:// Icelord
		case 5455:// Icelord
			return 5726;

		case 5627:// Sorebones
		case 5628:// Sorebones
			return 5649;

		case 5691:// Undead Lumberjack
		case 5699:// Undead Lumberjack
		case 5707:// Undead Lumberjack
		case 5715:// Undead Lumberjack
		case 5723:// Undead Lumberjack
		case 5731:// Undead Lumberjack
		case 5739:// Undead Lumberjack
		case 5747:// Undead Lumberjack
			return 5972;

		case 5750:// Cave Bug
			return 6081;

		case 5906:// A doubt
			return 6315;

		case 3066:// Zombie Champion
			return 5580;

		case 3313:// Tanglefoot
			return 3263;

		case 4397:// Catablepon
		case 4398:// Catablepon
		case 4399:// Catablepon
			return 4270;

		case 4418:// Gorak
		case 6218:// Gorak
			return 4302;

		case 4527:// Suqah
			return 4389;

		case 4893:// Giant Lobster
			return 6267;

		case 5176:// Ogre Shaman
		case 5181:// Ogre Shaman
		case 5184:// Ogre Shaman
		case 5187:// Ogre Shaman
		case 5190:// Ogre Shaman
		case 5193:// Ogre Shaman
			return 361;

		case 5214:// Penance Fighter
		case 5215:// Penance Fighter
		case 5216:// Penance Fighter
		case 5217:// Penance Fighter
		case 5218:// Penance Fighter
		case 5219:// Penance Fighter
			return 5098;

		case 1831:// Cave Slime
			return 1792;

		case 907:// Kolodion
		case 910:// Kolodion
		case 2497:// Tribesman
			return 714;

		case 1676:// Experiment
			return 1628;

		case 10100:// Bulwark Beast
			return 13005;

		case 1677:// Experiment
			return 1618;

		case 9463:// Ice Strykewyrm
		case 9465:// Desert Strykewyrm
		case 9467:// Jungle Strykewyrm
			return 12793;

		case 1678:// Experiment
			return 1611;

		case 8596:// Avatar Of Destruction
			return 11199;

		case 6645:// Revenant Cyclops
			return 7454;

		case 6998:// Revenant Dragon
			return 8593;

		case 6691:// Revenant Dark Beast
			return 7468;

		case 6647:// Revenant Demon
			return 7475;

		case 6688:// Revenant Hellhound
			return 7461;

		case 6622:// Revenant Pyrefiend
		case 6621:// Revenant Icefiend
			return 7484;

		case 6623:// Revenant Vampire
			return 7428;

		case 6611:// Revenant Knight
			return 7442;

		case 6615:// Revenant Ork
			return 7416;

		case 6606:// Revenant Icefiend
			return 7397;

		case 6605:// Revenant Goblin
			return 7448;

		case 6604:// Revenant Imp
			return 7408;

		case 10126:// Unholy Cursebearer
			return 13171;

		case 7480:// Tumeken's Shadow
			return 11629;

		case 112:// Moss Giant
			return 4653;

		case 1250:// Fiyr Shade
			return 1285;

		case 9172:// Aquanite
			return 12039;

		case 2889:// Rock Lobster
			return 2862;

		case 2457:// Wallaski
			return 2367;

		case 8281:// Ballance Elemental
		case 8282:// Ballance Elemental
		case 8283:// Ballance Elemental
			return 10679;

		case 3498:// Gelatinoth Mother
		case 3499:// Gelatinoth Mother
		case 3500:// Gelatinoth Mother
		case 3501:// Gelatinoth Mother
		case 3502:// Gelatinoth Mother
			return 1342;

		case 8777:// Handcannonneer
			return 12181;

		case 5250:// Scarab Mage
			return 7616;

		case 7808:// Mummy Warrior
			return 5555;

		case 6753:// Mummy
			return 5555;

		case 7797:// Kurask Overlord
			return 9440;

		case 8324:// Elite Black Knight
			return 836;

		case 10815:// New Red Dragon
		case 10607:// New Green Dragon
		case 10224:// New Black Dragon
			return 13153;

		case 8528:// Nomad
			return 12694;

		case 8597:// Avatar Of Creation
		case 9437:// Decaying Avatar
			return 11204;

		case 1160:// Kalphite Queen
			return 6233;

		case 10775:// Frost Dragon
			return 13153;

		case 7133:// Bork
			return 8756;

		case 7135:// Ork Legion
			return 8761;

		case 8321:// Elite Dark Mage
			return 2304;

		case 5666:// Barrelchest
			return 5898;

		case 6247:// Commander Zilyana
			return 6965;

		case 8133:// Corpreal Beast
			return 10050;

		case 8349:// Tormented Demon
			return 10924;

		case 6261:// Seargent Strongstack
			return 6156;

		case 6260:// General Graardor
			return 7062;

		case 2892:// Spinolyp
		case 2894:// Spinolyp
			return 2865;

		case 2881:// Dagannoth Supreme
		case 2882:// Dagannoth Prime
		case 2883:// Dagannoth Rex
			return 2856;

		case 1472:// Jungle Demon
			return 67;

		default:
			return 2304;
		}
	}

	/**
	 * Attack delays
	 */
	public int getNpcDelay(int i) {
		switch (NPCHandler.NPCS.get(i).npcType) {
		case 2025:
		case 2028:
		case 5996:
			return 7;
		case 2745:
			return 8;
		case 6260:
			return 7;
		case 6204:
			return 8;
		case 6208:
		case 2042:
		case 4186:
			return 8;
		case 2882:
		case 2881:
		case 2883:
			return 7;
		default:
			return 5;
		}
	}

	/**
	 * Hit delays
	 */
	public int getHitDelay(int i) {
		switch (NPCHandler.NPCS.get(i).npcType) {
		case 1158:
		case 1160:
			if (NPCHandler.NPCS.get(i).attackType == 1 || NPCHandler.NPCS.get(i).attackType == 2) {
				return 3;
			} else {
				return 2;
			}
		case 2745:
			if (NPCHandler.NPCS.get(i).attackType == 1 || NPCHandler.NPCS.get(i).attackType == 2) {
				return 5;
			} else {
				return 2;
			}

		case 2025:
			return 4;
		case 2028:
			return 3;
		case 6260:
			return 13;
		case 6265:
			return 9;
		case 6263:
			return 5;
		case 6203:
			return 15;
		case 6204:
			return 8;
		case 6208:
			return 8;
		case 4175:
		case 4172:
		case 4173:
		case 4174:
			return 6;

		default:
			return 2;
		}
	}

	/**
	 * Npc respawn time
	 */

	public int getRespawnTime(int i) {
		switch (NPCHandler.NPCS.get(i).npcType) {
		case 3943:
		case 1160:
			return -1;
		case 6260:
		case 6263:
		case 6265:
		case 6250:
		case 6247:
		case 6252:
		case 6248:
		case 6222:
		case 6223:
		case 6225:
		case 6227:
		case 3847:
			return 13;
		case 2881:
		case 2882:
		case 2883:
		case 1926:
		case 2060:
		case 5996:
		case 4175:
		case 4172:
		case 4173:
		case 4174:
		case 2042:
		case 4186:
		case 4189:
			return 13;
		case 1158:
			return 150;
		case 6142:
		case 6143:
		case 6144:
		case 6145:
		case 6150:
		case 6151:
		case 6152:
		case 6153:
			return 500;
		default:
			return 30;
		}
	}

	public void newNPC(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence) {
		NPC newNPC = new NPC(npcType);
		newNPC.setAbsX(x);
		newNPC.setAbsY(y);
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		NPCHandler.NPCS.add(newNPC);
	}

	public void newNPCList(int npcType, String npcName, int combat, int HP) {

		NPCDefinitions newNPCList = new NPCDefinitions(npcType);
		newNPCList.setNpcName(npcName);
		newNPCList.setNpcCombat(combat);
		newNPCList.setNpcHealth(HP);
		NPCDefinitions.getDefinitions()[npcType] = newNPCList;
	}

	public void process() {
		for (int i = 0; i < NPCS.capacity(); i++) {
			if (NPCHandler.NPCS.get(i) != null) {
				NPCHandler.NPCS.get(i).clearUpdateFlags();
				if (Pet.isPet(NPCHandler.NPCS.get(i).npcType)) {
					Player player = World.PLAYERS.get(NPCHandler.NPCS.get(i).spawnedBy);
					if (player != null) {
						if (player.getPet().getPet() != null && player.getPet().getNpc() != null) {
							World.PLAYERS.get(NPCHandler.NPCS.get(i).spawnedBy).getPet().process();
							continue;
						}
					}
				}
				HunterHandler.checkTrap(NPCHandler.NPCS.get(i));
				try {
					if (NPCHandler.NPCS.get(i).actionTimer > 0) {
						NPCHandler.NPCS.get(i).actionTimer--;
					}
					if (NPCHandler.NPCS.get(i).whose > 0) {
						if (World.PLAYERS.get(NPCHandler.NPCS.get(i).whose) == null) {
							NPCHandler.NPCS.remove(NPCHandler.NPCS.get(i));
							return;
						}
						NPCHandler.NPCS.get(i).facePlayer(NPCHandler.NPCS.get(i).whose);
						if (Math.abs(World.PLAYERS.get(NPCHandler.NPCS.get(i).whose).getX() - NPCHandler.NPCS.get(i).getX()) > 5 || Math.abs(World.PLAYERS.get(NPCHandler.NPCS.get(i).whose).getY() - NPCHandler.NPCS.get(i).getY()) > 5) {
						} else {
							followPlayer(i, NPCHandler.NPCS.get(i).whose);
						}
					}
					if (NPCHandler.NPCS.get(i).freezeTimer > 0) {
						NPCHandler.NPCS.get(i).freezeTimer--;
					}

					if (NPCHandler.NPCS.get(i).hitDelayTimer > 0) {
						NPCHandler.NPCS.get(i).hitDelayTimer--;
					}

					if (NPCHandler.NPCS.get(i).hitDelayTimer == 1) {
						NPCHandler.NPCS.get(i).hitDelayTimer = 0;
						applyDamage(i);
					}

					if (NPCHandler.NPCS.get(i).attackTimer > 0) {
						NPCHandler.NPCS.get(i).attackTimer--;
					}
					if (NPCHandler.NPCS.get(i).spawnedBy > 0 && (World.PLAYERS.get(NPCHandler.NPCS.get(i).spawnedBy) == null || World.PLAYERS.get(NPCHandler.NPCS.get(i).spawnedBy).heightLevel != NPCHandler.NPCS.get(i).heightLevel || World.PLAYERS.get(NPCHandler.NPCS.get(i).spawnedBy).isDead || !Player.goodDistance(NPCHandler.NPCS.get(i).getX(), NPCHandler.NPCS.get(i).getY(), World.PLAYERS.get(NPCHandler.NPCS.get(i).spawnedBy).getX(), World.PLAYERS.get(NPCHandler.NPCS.get(i).spawnedBy).getY(), 20))) {
						NPCHandler.NPCS.remove(i);
					}

					if (NPCHandler.NPCS.get(i) == null)
						continue;

					if (System.currentTimeMillis() - NPCHandler.NPCS.get(i).lastDamageTaken > 5000) {
						NPCHandler.NPCS.get(i).underAttackBy = 0;
					}
					/*if (NPCHandler.NPCS.get(i).walkingType >= 0) {
						switch (NPCHandler.NPCS.get(i).walkingType) {

						case 1:
							if (Misc.random(3) == 1 && !NPCHandler.NPCS.get(i).walkingHome) {
								int MoveX = 0;
								int MoveY = 0;
								int Rnd = Misc.random(9);
								if (Rnd == 1) {
									MoveX = 1;
									MoveY = 1;
								} else if (Rnd == 2) {
									MoveX = -1;
								} else if (Rnd == 3) {
									MoveY = -1;
								} else if (Rnd == 4) {
									MoveX = 1;
								} else if (Rnd == 5) {
									MoveY = 1;
								} else if (Rnd == 6) {
									MoveX = -1;
									MoveY = -1;
								} else if (Rnd == 7) {
									MoveX = -1;
									MoveY = 1;
								} else if (Rnd == 8) {
									MoveX = 1;
									MoveY = -1;
								}
								if (MoveX == 1) {
									if (NPCHandler.NPCS.get(i).absX + MoveX < NPCHandler.NPCS.get(i).makeX + 1) {
										NPCHandler.NPCS.get(i).moveX = MoveX;
									} else {
										NPCHandler.NPCS.get(i).moveX = 0;
									}
								}
								if (MoveX == -1) {
									if (NPCHandler.NPCS.get(i).absX - MoveX > NPCHandler.NPCS.get(i).makeX - 1) {
										NPCHandler.NPCS.get(i).moveX = MoveX;
									} else {
										NPCHandler.NPCS.get(i).moveX = 0;
									}
								}
								if (MoveY == 1) {
									if (NPCHandler.NPCS.get(i).absY + MoveY < NPCHandler.NPCS.get(i).makeY + 1) {
										NPCHandler.NPCS.get(i).moveY = MoveY;
									} else {
										NPCHandler.NPCS.get(i).moveY = 0;
									}
								}
								if (MoveY == -1) {
									if (NPCHandler.NPCS.get(i).absY - MoveY > NPCHandler.NPCS.get(i).makeY - 1) {
										NPCHandler.NPCS.get(i).moveY = MoveY;
									} else {
										NPCHandler.NPCS.get(i).moveY = 0;
									}
								}
								NPCHandler.NPCS.get(i).getNextNPCMovement(i);
								NPCHandler.NPCS.get(i).updateRequired = true;
							}
							break;

						case 5:
							NPCHandler.NPCS.get(i).turnNpc(NPCHandler.NPCS.get(i).absX - 1, NPCHandler.NPCS.get(i).absY);
							break;

						case 4:
							NPCHandler.NPCS.get(i).turnNpc(NPCHandler.NPCS.get(i).absX + 1, NPCHandler.NPCS.get(i).absY);
							break;

						case 3:
							NPCHandler.NPCS.get(i).turnNpc(NPCHandler.NPCS.get(i).absX, NPCHandler.NPCS.get(i).absY - 1);
							break;
						case 2:
							NPCHandler.NPCS.get(i).turnNpc(NPCHandler.NPCS.get(i).absX, NPCHandler.NPCS.get(i).absY + 1);
							break;
						}
					}*/
				
					if ((NPCHandler.NPCS.get(i).killerId > 0 || NPCHandler.NPCS.get(i).underAttack) && !NPCHandler.NPCS.get(i).walkingHome && retaliates(NPCHandler.NPCS.get(i).npcType)) {
						if (!NPCHandler.NPCS.get(i).isDead) {
							int p = NPCHandler.NPCS.get(i).killerId;
							if (World.PLAYERS.get(p) != null) {
								Player c = World.PLAYERS.get(p);
								followPlayer(i, c.getIndex());
								if (NPCHandler.NPCS.get(i) == null) {
									continue;
								}
								if (NPCHandler.NPCS.get(i).attackTimer == 0) {
									if (c != null) {
										attackPlayer(c, i);
									}
								}

							} else {
								NPCHandler.NPCS.get(i).killerId = 0;
								NPCHandler.NPCS.get(i).underAttack = false;
								NPCHandler.NPCS.get(i).facePlayer(0);
							}
						}
					}

					if (NPCHandler.NPCS.get(i) == null) {
						continue;
					}
					boolean groupNpc = NPCGroupRespawn.isInGroup(NPCHandler.NPCS.get(i).npcType);
					if (NPCHandler.NPCS.get(i).isDead) {
						if (NPCHandler.NPCS.get(i).actionTimer == 3 && NPCHandler.NPCS.get(i).applyDead && !NPCHandler.NPCS.get(i).needRespawn && NPCHandler.NPCS.get(i).npcType != 1158 && NPCHandler.NPCS.get(i).npcType != 3847 && NPCHandler.NPCS.get(i).npcType != 3943) {
						}
						if (NPCHandler.NPCS.get(i).actionTimer == 0 && !NPCHandler.NPCS.get(i).applyDead && !NPCHandler.NPCS.get(i).needRespawn) {
							NPCHandler.NPCS.get(i).updateRequired = true;
							NPCHandler.NPCS.get(i).facePlayer(0);
							NPCHandler.NPCS.get(i).killedBy = getNpcKillerId(i);
							if (!NPCHandler.NPCS.get(i).noDeathEmote) {
								NPCHandler.NPCS.get(i).animNumber = getDeadEmote(i); // dead
								NPCHandler.NPCS.get(i).animUpdateRequired = true;
							}
							NPCHandler.NPCS.get(i).freezeTimer = 0;
							NPCHandler.NPCS.get(i).applyDead = true;
							resetPlayersInCombat(i);
						} else if (NPCHandler.NPCS.get(i).actionTimer == 0 && NPCHandler.NPCS.get(i).applyDead && !NPCHandler.NPCS.get(i).needRespawn) {
							NPCHandler.NPCS.get(i).needRespawn = true;
							NPCHandler.NPCS.get(i).actionTimer = getRespawnTime(i); // respawn
							Player p = World.PLAYERS.get(NPCHandler.NPCS.get(i).killerId);
							/*if (p != null) {
								if (p.playerRights == 0 && new Random().nextInt(100) <= 3) {
									dropItems(i);
								} else if (p.playerRights == 6 && new Random().nextInt(100) <= 5) {
									dropItems(i);
								} else if (p.playerRights == 7 && new Random().nextInt(100) <= 7) {
									dropItems(i);
								} else if (p.playerRights == 11 && new Random().nextInt(100) <= 15) {
									dropItems(i);
								} else if (p.playerRights == 5 && new Random().nextInt(100) <= 100) {
									dropItems(i);
								
								
								}
							}*/
							dropItems(i); // npc drops items!
							NPCHandler.NPCS.get(i).setAbsX(NPCHandler.NPCS.get(i).makeX);
							NPCHandler.NPCS.get(i).setAbsY(NPCHandler.NPCS.get(i).makeY);
							NPCHandler.NPCS.get(i).HP = NPCHandler.NPCS.get(i).MaxHP;
							if (!NPCHandler.NPCS.get(i).noDeathEmote) {
								NPCHandler.NPCS.get(i).animNumber = 0x328;
								NPCHandler.NPCS.get(i).animUpdateRequired = true;
							}
							NPCHandler.NPCS.get(i).animUpdateRequired = true;
							if (groupNpc) {
								NPCHandler.NPCS.get(i).actionTimer = -1;
							}
						} else if (NPCHandler.NPCS.get(i).actionTimer == 0 && NPCHandler.NPCS.get(i).needRespawn) {
							if (NPCHandler.NPCS.get(i).spawnedBy > 0) {
								NPCHandler.NPCS.remove(i);
							} else {
								int old1 = NPCHandler.NPCS.get(i).npcType;
								int old2 = NPCHandler.NPCS.get(i).makeX;
								int old3 = NPCHandler.NPCS.get(i).makeY;
								int old4 = NPCHandler.NPCS.get(i).heightLevel;
								int old5 = NPCHandler.NPCS.get(i).walkingType;
								int old6 = NPCHandler.NPCS.get(i).MaxHP;
								int old7 = NPCHandler.NPCS.get(i).maxHit;
								int old8 = NPCHandler.NPCS.get(i).attack;
								int old9 = NPCHandler.NPCS.get(i).defence;
								NPCHandler.NPCS.remove(i);
								newNPC(old1, old2, old3, old4, old5, old6, old7, old8, old9);
							}
						}
						if (groupNpc) {
							NPCGroupRespawn group = NPCGroupRespawn.getGroup(NPCHandler.NPCS.get(i).npcType);
							Collection<NPC> list = new ArrayList<>(group.getNpcs().length);
							boolean allDead = true;
							for (NPC npc : NPCS) {
								if (npc == null) {
									continue;
								}
								if (!group.contains(NPCHandler.NPCS.get(i).npcType)) {
									continue;
								}
								if (!Boundary.isInBounds(npc, group.getBoundary())) {
									continue;
								}
								if (npc.actionTimer > 0 && npc.actionTimer < group.getRespawnTime()) {
									allDead = false;
									break;
								}
								if (!npc.isDead || npc.actionTimer != -1) {
									allDead = false;
									break;
								}

								list.add(npc);
							}
							if (allDead) {
								for (NPC npc : list) {
									npc.actionTimer = group.getRespawnTime();
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Npc killer id?
	 */
	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int p = 1; p < World.PLAYERS.capacity(); p++) {
			if (World.PLAYERS.get(p) != null) {
				if (World.PLAYERS.get(p).lastNpcAttacked == npcId) {
					if (World.PLAYERS.get(p).totalDamageDealt > oldDamage) {
						oldDamage = World.PLAYERS.get(p).totalDamageDealt;
						killerId = p;

					}
					World.PLAYERS.get(p).totalDamageDealt = 0;
				}
			}
		}
		return killerId;
	}

	/**
     * 
     */

	public static void bossKilled(int i) {
		Player player = World.PLAYERS.get(NPCHandler.NPCS.get(i).spawnedBy);
		if (player != null) {
			TeleportExecutor.teleport(player, new Position(3091, 3486, 0));
			player.getDH().sendDialogues(1457, 4983);
			player.Mercenary = true;
		}
	}

	public static void handleJadDeath(int i) {
		Player player = World.PLAYERS.get(NPCHandler.NPCS.get(i).spawnedBy);
		if (player != null) {
			player.getItems().addItem(6570, 1);
			player.getPA().resetTzhaar();
			player.waveId = 300;
		}
	}

	public boolean dead;

	public boolean isDead() {
		return dead;
	}

	public void dropItems(int i) {
		if (NPCHandler.NPCS.get(i).killedBy == -1) {
			return;
		}
		Player c = World.PLAYERS.get(NPCHandler.NPCS.get(i).killedBy);
		if (c == null) {
			return;
		}

		NPC npc = NPCHandler.NPCS.get(i);

		if (npc != null) {
			c.getBossDeathTracker().add(NPCHandler.getNpcListName(NPCHandler.NPCS.get(i).npcType).replaceAll("_", " "));
			if (NPCHandler.NPCS.get(i).npcType == c.slayerTask)
				c.getSlayerDeathTracker().add(NPCHandler.getNpcListName(NPCHandler.NPCS.get(i).npcType).replaceAll("_", " "));
		}
		c.getAchievements().kill(NPCHandler.NPCS.get(i));
		// get the drop table
		NPCDropAnnouncement dropAnnouncement = new NPCDropAnnouncement(NPCHandler.NPCS.get(i));
		// drop the items
		Pet.drop(c, NPCHandler.NPCS.get(i).npcType);

		if (!ClueScrollHandler.npcDrop(c, NPCHandler.NPCS.get(i))) {
			if (!ClueScrollHandler.calculateDrop(c, npc, false)) {

			}
			// if (ChallengeBoss.spawned == true &&
			// NPCHandler.NPCS.get(i).npcType == 2042) {
			// ChallengeBoss.spawned = false;
			// Rewards.random(c, Rewards.ZULRAH);
			// System.out.println("Death completed. Spawned: " +
			// ChallengeBoss.spawned + "");
			// }
			DropTable t = DropTable.getDrops().get(NPCHandler.NPCS.get(i).npcType);
			Item[] items = DropTable.calculateDrops(t, c);
			for (Item item : items) {
				if (item == null) {
					continue;
				}

				if (NPCHandler.NPCS.get(i).npcType == 3847 || NPCHandler.NPCS.get(i).npcType == 2042 || NPCHandler.NPCS.get(i).npcType == 2043 || NPCHandler.NPCS.get(i).npcType == 2044 || NPCHandler.NPCS.get(i).npcType == 3848 || NPCHandler.NPCS.get(i).npcType == 3943) {
					Server.itemHandler.createGroundItem(c, item.getId(), c.absX, c.absY, c.heightLevel, item.getCount());
				} else {
					Server.itemHandler.createGroundItem(c, item.getId(), npc.absX, npc.absY, npc.heightLevel, item.getCount());
					dropAnnouncement.announce(c, item.getId(), item.getCount());
				}
			}
		}
		/*
		 * Drops the armour for the suit of armour.
		 */
		for (SuiteOfArmour suite : SuiteOfArmour.values()) {
			if (suite.getId() == NPCHandler.NPCS.get(i).getId()) {
				for (int id : suite.getArmourPieces()) {
					Server.itemHandler.createGroundItem(c, id, NPCHandler.NPCS.get(i).absX, NPCHandler.NPCS.get(i).absY, c.getHeight(), 1);
				}
			}
		}

		/*
		 * Drops the defenders for the cyclops.
		 */
		if (NPCHandler.NPCS.get(i).npcType == 4291 && c.inCyclopsRoom()) {
			int random = Misc.random(35);
			final String defender = WarriorsGuild.getNextDefender(c);
			final int id = WarriorsGuild.getNextDefenderId(defender);
			if (random == 1) {
				Achievements.increase(c, AchievementType.DEFENDER, 1);
				c.sendMessage("<col=255>You notice a defender drop on the floor");
				Server.itemHandler.createGroundItem(c, id, NPCHandler.NPCS.get(i).absX, NPCHandler.NPCS.get(i).absY, c.getHeight(), 1);
			}
		}
	}

	/**
	 * Resets players in combat
	 */
	public void resetPlayersInCombat(int i) {
		for (int j = 0; j < World.PLAYERS.capacity(); j++) {
			if (World.PLAYERS.get(j) != null) {
				if (World.PLAYERS.get(j).underAttackBy2 == i) {
					World.PLAYERS.get(j).underAttackBy2 = 0;
				}
			}
		}
	}

	/**
	 * Npc names
	 * 
	 */
	public String getNpcName(int npcId) {
		if (npcId <= -1) {
			return "None";
		}
		if (NPCDefinitions.getDefinitions()[npcId] == null) {
			return "None";
		}
		return NPCDefinitions.getDefinitions()[npcId].getNpcName();
	}

	/**
	 * Npc Follow Player
	 */
	public static int GetMove(int Place1, int Place2) {
		if ((Place1 - Place2) == 0) {
			return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean followPlayer(int i) {
		switch (NPCHandler.NPCS.get(i).npcType) {
		case 2892:
		case 2894:
		case 2883:
		case 2882:
		case 2881:
			return false;
		}
		return true;
	}

	public void followPlayer(int i, int playerId) {
		if (World.PLAYERS.get(playerId) == null) {
			return;
		}
		if (World.PLAYERS.get(playerId).isDead) {
			NPCHandler.NPCS.get(i).facePlayer(0);
			NPCHandler.NPCS.get(i).randomWalk = true;
			NPCHandler.NPCS.get(i).underAttack = false;
			return;
		}
		if (!followPlayer(i)) {
			NPCHandler.NPCS.get(i).facePlayer(playerId);
			return;
		}

		int playerX = World.PLAYERS.get(playerId).getAbsX();
		int playerY = World.PLAYERS.get(playerId).getAbsY();
		NPCHandler.NPCS.get(i).randomWalk = false;
		if (goodDistance(NPCHandler.NPCS.get(i).getX(), NPCHandler.NPCS.get(i).getY(), playerX, playerY, distanceRequired(i))) {
			return;
		}
		if ((NPCHandler.NPCS.get(i).spawnedBy > 0) || ((NPCHandler.NPCS.get(i).getAbsX() < NPCHandler.NPCS.get(i).makeX + Constants.NPC_FOLLOW_DISTANCE) && (NPCHandler.NPCS.get(i).getAbsX() > NPCHandler.NPCS.get(i).makeX - Constants.NPC_FOLLOW_DISTANCE) && (NPCHandler.NPCS.get(i).getAbsY() < NPCHandler.NPCS.get(i).makeY + Constants.NPC_FOLLOW_DISTANCE) && (NPCHandler.NPCS.get(i).getAbsY() > NPCHandler.NPCS.get(i).makeY - Constants.NPC_FOLLOW_DISTANCE))) {
			if (NPCHandler.NPCS.get(i).heightLevel == World.PLAYERS.get(playerId).heightLevel) {
				if (World.PLAYERS.get(playerId) != null && NPCHandler.NPCS.get(i) != null) {
					if (playerY < NPCHandler.NPCS.get(i).getAbsY()) {
						NPCHandler.NPCS.get(i).moveX = GetMove(NPCHandler.NPCS.get(i).getAbsX(), playerX);
						NPCHandler.NPCS.get(i).moveY = GetMove(NPCHandler.NPCS.get(i).getAbsY(), playerY);
					} else if (playerY > NPCHandler.NPCS.get(i).getAbsY()) {
						NPCHandler.NPCS.get(i).moveX = GetMove(NPCHandler.NPCS.get(i).getAbsX(), playerX);
						NPCHandler.NPCS.get(i).moveY = GetMove(NPCHandler.NPCS.get(i).getAbsY(), playerY);
					} else if (playerX < NPCHandler.NPCS.get(i).getAbsX()) {
						NPCHandler.NPCS.get(i).moveX = GetMove(NPCHandler.NPCS.get(i).getAbsX(), playerX);
						NPCHandler.NPCS.get(i).moveY = GetMove(NPCHandler.NPCS.get(i).getAbsY(), playerY);
					} else if (playerX > NPCHandler.NPCS.get(i).getAbsX()) {
						NPCHandler.NPCS.get(i).moveX = GetMove(NPCHandler.NPCS.get(i).getAbsX(), playerX);
						NPCHandler.NPCS.get(i).moveY = GetMove(NPCHandler.NPCS.get(i).getAbsY(), playerY);
					} else if (playerX == NPCHandler.NPCS.get(i).getAbsX() || playerY == NPCHandler.NPCS.get(i).getAbsY()) {
						int o = Misc.random(3);
						switch (o) {
						case 0:
							NPCHandler.NPCS.get(i).moveX = GetMove(NPCHandler.NPCS.get(i).getAbsX(), playerX);
							NPCHandler.NPCS.get(i).moveY = GetMove(NPCHandler.NPCS.get(i).getAbsY(), playerY + 1);
							break;

						case 1:
							NPCHandler.NPCS.get(i).moveX = GetMove(NPCHandler.NPCS.get(i).getAbsX(), playerX);
							NPCHandler.NPCS.get(i).moveY = GetMove(NPCHandler.NPCS.get(i).getAbsY(), playerY - 1);
							break;

						case 2:
							NPCHandler.NPCS.get(i).moveX = GetMove(NPCHandler.NPCS.get(i).getAbsX(), playerX + 1);
							NPCHandler.NPCS.get(i).moveY = GetMove(NPCHandler.NPCS.get(i).getAbsY(), playerY);
							break;

						case 3:
							NPCHandler.NPCS.get(i).moveX = GetMove(NPCHandler.NPCS.get(i).getAbsX(), playerX - 1);
							NPCHandler.NPCS.get(i).moveY = GetMove(NPCHandler.NPCS.get(i).getAbsY(), playerY);
							break;
						}
					}
					NPCHandler.NPCS.get(i).facePlayer(playerId);
					NPCHandler.NPCS.get(i).getNextNPCMovement(i);
					NPCHandler.NPCS.get(i).facePlayer(playerId);
					NPCHandler.NPCS.get(i).updateRequired = true;
				}
			}
		} else {
			NPCHandler.NPCS.get(i).facePlayer(0);
			NPCHandler.NPCS.get(i).randomWalk = true;
			NPCHandler.NPCS.get(i).underAttack = false;
		}
	}

	/**
	 * load spell
	 */
	public void loadSpell2(int i) {
		NPCHandler.NPCS.get(i).attackType = 3;
		int random = Misc.random(3);
		if (random == 0) {
			NPCHandler.NPCS.get(i).projectileId = 393; // red
			NPCHandler.NPCS.get(i).endGfx = 430;
		} else if (random == 1) {
			NPCHandler.NPCS.get(i).projectileId = 394; // green
			NPCHandler.NPCS.get(i).endGfx = 429;
		} else if (random == 2) {
			NPCHandler.NPCS.get(i).projectileId = 395; // white
			NPCHandler.NPCS.get(i).endGfx = 431;
		} else if (random == 3) {
			NPCHandler.NPCS.get(i).projectileId = 396; // blue
			NPCHandler.NPCS.get(i).endGfx = 428;
		}
	}

	boolean hitThrough;

	public void loadSpell(Player c, int i) {
		if (Bosses.isBoss(NPCHandler.NPCS.get(i).npcType)) {
			Bosses.get(NPCHandler.NPCS.get(i).npcType).attack(NPCHandler.NPCS.get(i));
			return;
		}
		switch (NPCHandler.NPCS.get(i).npcType) {
		case 2892:
			NPCHandler.NPCS.get(i).projectileId = 94;
			NPCHandler.NPCS.get(i).attackType = 2;
			NPCHandler.NPCS.get(i).endGfx = 95;
			break;
		case 2894:
			NPCHandler.NPCS.get(i).projectileId = 298;
			NPCHandler.NPCS.get(i).attackType = 1;
			break;
		case 126:
			NPCHandler.NPCS.get(i).projectileId = 95;
			NPCHandler.NPCS.get(i).attackType = 2;
			break;
		// kalphite queen form 1
		case 3847:
			int ran2 = Misc.random(2);
			switch (ran2) {
			case 1:
				c.gfx100(162);
				NPCHandler.NPCS.get(i).attackType = 2;
				{
				}
				break;
			}

		case 795:
			int ran = Misc.random(5);
			switch (ran) {
			case 0:
				NPCHandler.NPCS.get(i).attackType = 2;
			case 1:
				NPCHandler.NPCS.get(i).attackType = 2;
			case 2:
				NPCHandler.NPCS.get(i).attackType = 2;
				break;
			case 3:
			case 4:
				c.gfx100(367);
				c.freezeTimer = 10;
				NPCHandler.NPCS.get(i).forceChat("Ice Barrage!");
				NPCHandler.NPCS.get(i).attackType = 2;
				break;
			case 5:
				c.gfx100(369);
				c.freezeTimer = 10;
				NPCHandler.NPCS.get(i).forceChat("Ice Blitz!");
				NPCHandler.NPCS.get(i).attackType = 2;
				break;
			}
		case 5905:
			c.gfx100(367);
			NPCHandler.NPCS.get(i).attackType = 2;
			break;
		case 5247:
			if (Misc.random(2) == 1) {
				NPCHandler.NPCS.get(i).attackType = 1;
				NPCHandler.NPCS.get(i).forceChat("Be pierced by my hands!");
				NPCHandler.NPCS.get(i).endGfx = 281;
				NPCHandler.NPCS.get(i).projectileId = 473;
				NPCHandler.NPCS.get(i).hitDelayTimer = 3;
				NPCHandler.NPCS.get(i).attackTimer = 3;
				c.freezeTimer = 2;
			} else if (Misc.random(2) == 1) {
				NPCHandler.NPCS.get(i).attackType = 2;
				c.gfx100(369);
				c.freezeTimer = 10;
				NPCHandler.NPCS.get(i).forceChat("RAWRRRGHHH! Freeze, human!");
				NPCHandler.NPCS.get(i).hitDelayTimer = 3;
				NPCHandler.NPCS.get(i).attackTimer = 3;
			}
			break;
		case 1158:
			for (int j = 0; j < World.PLAYERS.capacity(); j++) {
				if (World.PLAYERS.get(j) != null) {
					// Client c = (Client)PlayerHandler.PLAYERS.get(j];
					int kq1;
					if (goodDistance(NPCHandler.NPCS.get(i).getAbsX(), NPCHandler.NPCS.get(i).getAbsY(), c.getAbsX(), c.getAbsY(), 2)) {
						kq1 = Misc.random(2);
					} else {
						kq1 = Misc.random(1);
					}
					if (kq1 == 0) {
						NPCHandler.NPCS.get(i).projectileId = 280; // mage
						NPCHandler.NPCS.get(i).endGfx = 281;
						NPCHandler.NPCS.get(i).attackType = 2;
					} else if (kq1 == 1) {
						NPCHandler.NPCS.get(i).attackType = 1; // range
						NPCHandler.NPCS.get(i).endGfx = 281;
						NPCHandler.NPCS.get(i).projectileId = 473;
					} else if (kq1 == 2) {
						NPCHandler.NPCS.get(i).attackType = 0; // melee
						NPCHandler.NPCS.get(i).projectileId = -1;
					}
				}
			}
			break;
		// kalphite queen form 2
		case 1160:
			for (int j = 0; j < World.PLAYERS.capacity(); j++) {
				if (World.PLAYERS.get(j) != null) {
					// Client c = (Client)PlayerHandler.PLAYERS.get(j];
					int kq1;
					if (goodDistance(NPCHandler.NPCS.get(i).getAbsX(), NPCHandler.NPCS.get(i).getAbsY(), c.getAbsX(), c.getAbsY(), 2)) {
						kq1 = Misc.random(2);
					} else {
						kq1 = Misc.random(1);
					}
					if (kq1 == 0) {
						NPCHandler.NPCS.get(i).projectileId = 280; // mage
						NPCHandler.NPCS.get(i).endGfx = 281;
						NPCHandler.NPCS.get(i).attackType = 2;
					} else if (kq1 == 1) {
						NPCHandler.NPCS.get(i).attackType = 1; // range
						NPCHandler.NPCS.get(i).endGfx = 281;
						NPCHandler.NPCS.get(i).projectileId = 473;
					} else if (kq1 == 2) {
						NPCHandler.NPCS.get(i).attackType = 0; // melee
						NPCHandler.NPCS.get(i).projectileId = -1;
					}
				}
			}
			break;

		/*
		 * Better Dragons
		 */
		case 5363: // Mithril-Dragon
		case 54: // Black-Dragon
		case 55: // Blue-Dragon
		case 1591:
		case 1592:
		case 941: // Green-Dragon
		case 53:
		case 1590:
		case 4682:
		case 3068:
		case 5362:
			int random1 = Misc.random(3);
			switch (random1) {
			case 1:
				NPCHandler.NPCS.get(i).projectileId = 393; // red
				NPCHandler.NPCS.get(i).endGfx = 430;
				NPCHandler.NPCS.get(i).attackType = 3;
				break;
			default:
				NPCHandler.NPCS.get(i).projectileId = -1; // melee
				NPCHandler.NPCS.get(i).endGfx = -1;
				NPCHandler.NPCS.get(i).attackType = 0;
				break;
			}
			break;

		case 3590:
		case 50:
		case 742:
			int random = Misc.random(4);
			switch (random) {
			case 0:
				NPCHandler.NPCS.get(i).projectileId = 393; // red
				NPCHandler.NPCS.get(i).endGfx = 430;
				NPCHandler.NPCS.get(i).attackType = 3;
				break;
			case 1:
				NPCHandler.NPCS.get(i).projectileId = 394; // green
				NPCHandler.NPCS.get(i).endGfx = 429;
				NPCHandler.NPCS.get(i).attackType = 3;
				break;
			case 2:
				NPCHandler.NPCS.get(i).projectileId = 395; // white
				NPCHandler.NPCS.get(i).endGfx = 431;
				NPCHandler.NPCS.get(i).attackType = 3;
				break;
			case 3:
				NPCHandler.NPCS.get(i).projectileId = 396; // blue
				NPCHandler.NPCS.get(i).endGfx = 428;
				NPCHandler.NPCS.get(i).attackType = 3;
				break;
			case 4:
				NPCHandler.NPCS.get(i).projectileId = -1; // melee
				NPCHandler.NPCS.get(i).endGfx = -1;
				NPCHandler.NPCS.get(i).attackType = 0;
				break;
			}
			break;
		// arma npcs
		case 6223:
		case 2561:
			NPCHandler.NPCS.get(i).attackType = 0;
			break;
		case 6225:
		case 6227:
		case 2560:
			NPCHandler.NPCS.get(i).attackType = 1;
			NPCHandler.NPCS.get(i).projectileId = 1190;
			break;
		case 6222:
		case 2559:
			NPCHandler.NPCS.get(i).attackType = 2;
			NPCHandler.NPCS.get(i).projectileId = 1203;
			break;
		case 1604:
			NPCHandler.NPCS.get(i).attackType = 2;
			NPCHandler.NPCS.get(i).projectileId = 1000;
			NPCHandler.NPCS.get(i).endGfx = 1001;
			break;
		case 2558:
			random = Misc.random(1);
			NPCHandler.NPCS.get(i).attackType = 1 + random;
			if (NPCHandler.NPCS.get(i).attackType == 1) {
				NPCHandler.NPCS.get(i).projectileId = 1197;
			} else {
				NPCHandler.NPCS.get(i).attackType = 2;
				NPCHandler.NPCS.get(i).projectileId = 1198;
			}
			break;
		// sara npcs
		case 6208:
		case 2562: // sara
			random = Misc.random(1);
			if (random == 0) {
				NPCHandler.NPCS.get(i).attackType = 2;
				NPCHandler.NPCS.get(i).endGfx = 1224;
				NPCHandler.NPCS.get(i).projectileId = -1;
			} else if (random == 1) {
				NPCHandler.NPCS.get(i).attackType = 0;
			}
			break;
		case 6248:
		case 2563: // star
			NPCHandler.NPCS.get(i).attackType = 0;
			break;
		case 6250:
		case 2564: // growler
			NPCHandler.NPCS.get(i).attackType = 2;
			NPCHandler.NPCS.get(i).projectileId = 1203;
			break;
		case 6204:
		case 6252:
		case 2565: // bree
			NPCHandler.NPCS.get(i).attackType = 1;
			NPCHandler.NPCS.get(i).projectileId = 9;
			break;
		case 6265:
			NPCHandler.NPCS.get(i).attackType = 2;
			NPCHandler.NPCS.get(i).endGfx = 1202;
			NPCHandler.NPCS.get(i).projectileId = 9;
			break;
		case 6263:
			NPCHandler.NPCS.get(i).attackType = 1;
			NPCHandler.NPCS.get(i).endGfx = 1202;
			NPCHandler.NPCS.get(i).projectileId = 9;
			break;
		// pvp bosses
		case 1913:
			random = Misc.random(2);
			if (random == 0 || random == 1) {
				NPCHandler.NPCS.get(i).attackType = 0;
			} else {
				NPCHandler.NPCS.get(i).attackType = 1;
				NPCHandler.NPCS.get(i).endGfx = 1211;
				NPCHandler.NPCS.get(i).projectileId = 288;
			}
		case 1914:
			random = Misc.random(2);
			if (random == 0 || random == 1) {
				NPCHandler.NPCS.get(i).attackType = 0;
			} else {
				NPCHandler.NPCS.get(i).attackType = 1;
				NPCHandler.NPCS.get(i).endGfx = 1211;
				NPCHandler.NPCS.get(i).projectileId = 288;
			}
		case 1974:
			random = Misc.random(2);
			if (random == 0 || random == 1) {
				NPCHandler.NPCS.get(i).attackType = 0;
			} else {
				NPCHandler.NPCS.get(i).attackType = 1;
				NPCHandler.NPCS.get(i).endGfx = 1211;
				NPCHandler.NPCS.get(i).projectileId = 288;
			}
		case 1977:
			random = Misc.random(2);
			if (random == 0 || random == 1) {
				NPCHandler.NPCS.get(i).attackType = 0;
			} else {
				NPCHandler.NPCS.get(i).attackType = 1;
				NPCHandler.NPCS.get(i).endGfx = 1211;
				NPCHandler.NPCS.get(i).projectileId = 288;
			}
			break;
		// bandos npcs
		case 2550:
		case 2551:
			NPCHandler.NPCS.get(i).attackType = 0;
			break;
		case 2552:
			NPCHandler.NPCS.get(i).attackType = 2;
			NPCHandler.NPCS.get(i).projectileId = 1203;
			break;
		case 2553:
			NPCHandler.NPCS.get(i).attackType = 1;
			NPCHandler.NPCS.get(i).projectileId = 1206;
			break;
		case 2025:
			NPCHandler.NPCS.get(i).attackType = 2;
			int r = Misc.random(3);
			if (r == 0) {
				NPCHandler.NPCS.get(i).gfx100(158);
				NPCHandler.NPCS.get(i).projectileId = 159;
				NPCHandler.NPCS.get(i).endGfx = 160;
			}
			if (r == 1) {
				NPCHandler.NPCS.get(i).gfx100(161);
				NPCHandler.NPCS.get(i).projectileId = 162;
				NPCHandler.NPCS.get(i).endGfx = 163;
			}
			if (r == 2) {
				NPCHandler.NPCS.get(i).gfx100(164);
				NPCHandler.NPCS.get(i).projectileId = 165;
				NPCHandler.NPCS.get(i).endGfx = 166;
			}
			if (r == 3) {
				NPCHandler.NPCS.get(i).gfx100(155);
				NPCHandler.NPCS.get(i).projectileId = 156;
			}
			break;
		case 912:
		case 913:
		case 914:
			NPCHandler.NPCS.get(i).attackType = 2;
			break;
		case 2028:
			NPCHandler.NPCS.get(i).attackType = 1;
			NPCHandler.NPCS.get(i).projectileId = 27;
			break;
		case 2837:
			int r3 = Misc.random(5);
			if (r3 == 0) {
				NPCHandler.NPCS.get(i).attackType = 1;
				NPCHandler.NPCS.get(i).gfx100(550);
				NPCHandler.NPCS.get(i).projectileId = 551;
				NPCHandler.NPCS.get(i).endGfx = 552;
				// c.getPA().chaosElementalEffect(c, 1);
			} else if (r3 == 1) {
				NPCHandler.NPCS.get(i).attackType = 2;
				NPCHandler.NPCS.get(i).gfx100(553);
				NPCHandler.NPCS.get(i).projectileId = 554;
				NPCHandler.NPCS.get(i).endGfx = 555;
				// c.getPA().chaosElementalEffect(c, 0);
			} else {
				NPCHandler.NPCS.get(i).attackType = 0;
				NPCHandler.NPCS.get(i).gfx100(556);
				NPCHandler.NPCS.get(i).projectileId = 557;
				NPCHandler.NPCS.get(i).endGfx = 558;
			}
		case 2745:
			int r4 = 0;
			// if (goodDistance(NPCHandler.NPCS.get(i).absX,
			// NPCHandler.NPCS.get(i).absY,
			// PlayerHandler.PLAYERS.get(NPCHandler.NPCS.get(i).spawnedBy].absX,
			// PlayerHandler.PLAYERS.get(NPCHandler.NPCS.get(i).spawnedBy].absY,
			// 1))
			// r4 = Misc.random(2);
			// else
			r4 = Misc.random(1);
			if (r4 == 0) {
				NPCHandler.NPCS.get(i).attackType = 2;
				NPCHandler.NPCS.get(i).endGfx = 157;
				NPCHandler.NPCS.get(i).projectileId = 448;
			} else if (r4 == 1) {
				NPCHandler.NPCS.get(i).attackType = 1;
				NPCHandler.NPCS.get(i).endGfx = 451;
				NPCHandler.NPCS.get(i).projectileId = -1;
			} else if (r4 == 2) {
				NPCHandler.NPCS.get(i).attackType = 0;
				NPCHandler.NPCS.get(i).projectileId = -1;
			}
			break;

		case 3200:
			int r2 = Misc.random(5);
			if (r2 == 0) {
				NPCHandler.NPCS.get(i).attackType = 1;
				NPCHandler.NPCS.get(i).gfx100(550);
				NPCHandler.NPCS.get(i).projectileId = 551;
				NPCHandler.NPCS.get(i).endGfx = 552;
				// c.getPA().chaosElementalEffect(c, 1);
			} else if (r2 == 1) {
				NPCHandler.NPCS.get(i).attackType = 2;
				NPCHandler.NPCS.get(i).gfx100(553);
				NPCHandler.NPCS.get(i).projectileId = 554;
				NPCHandler.NPCS.get(i).endGfx = 555;
				// c.getPA().chaosElementalEffect(c, 0);
			} else {
				NPCHandler.NPCS.get(i).attackType = 0;
				NPCHandler.NPCS.get(i).gfx100(556);
				NPCHandler.NPCS.get(i).projectileId = 557;
				NPCHandler.NPCS.get(i).endGfx = 558;
			}
			break;
		case 4972:
			int roc = Misc.random(1);
			switch (roc) {
			case 0:
				NPCHandler.NPCS.get(i).attackType = 1;
				break;
			case 1:
				NPCHandler.NPCS.get(i).attackType = 2;
				break;
			}
		case 2631:
			NPCHandler.NPCS.get(i).attackType = 1;
			NPCHandler.NPCS.get(i).projectileId = 443;
			break;
		}

	}

	/**
	 * Distanced required to attack
	 */
	public int distanceRequired(int i) {
		if (Bosses.isBoss(NPCHandler.NPCS.get(i).npcType)) {
			return Bosses.get(NPCHandler.NPCS.get(i).npcType).distanceRequired();
		}
		switch (NPCHandler.NPCS.get(i).npcType) {
		case 795:
			if (NPCHandler.NPCS.get(i).attackType == 2) {
				return 8;
			}
			
		case 3847:
		case 3943:
		case 2060:
		case 4186:
		case 5996:
			return 12;
		case 6263:
		case 2025:
		case 2028:
		case 5905:
		case 4971:
		case 4972:
		case 1158:
		case 1160:
		case 4175:
		case 4174:
		case 4173:
		case 4172:
			return 8;
		case 742:
		case 3590:
		case 2562:
			return 7;
		case 5247:
		case 3200:// chaos ele
		case 2837:
		case 2743:
		case 2631:
		case 2745:
		case 3649: // halloween event npc, change this!
		case 6222:
			return 8;
		case 6223:
		case 6225:
		case 6227:
			return 20;
		case 6259:
			return 15;
		case 2552:
		case 2553:
		case 2556:
		case 2557:
		case 2558:
		case 2559:
		case 2560:
		case 2564:
		case 2565:
		case 6265:
		case 6252:
		case 6250:
		case 6248:
		case 6208:
		case 6204:
			return 15;
			// things around dags
		case 2892:
		case 2894:
			return 10;
		case 1604:
			return 3;
		default:
			return 1;
		}
	}

	public int followDistance(int i) {
		switch (NPCHandler.NPCS.get(i).npcType) {

		}
		return 0;

	}

	public int getProjectileSpeed(int i) {
		switch (NPCHandler.NPCS.get(i).npcType) {
		case 2881:
		case 2882:
		case 3200:
		case 2837:
			return 85;
		case 1158:
		case 1160:
			return 90;
		case 2745:
			return 130;

		case 50:
		case 742:
		case 3590:
			return 90;

		case 2025:
			return 85;

		case 2028:
			return 80;

		default:
			return 85;
		}
	}

	/**
	 * NPC Attacking Player
	 */
	public void attackPlayer(Player c, int i) {
		if (NPCHandler.NPCS.get(i) != null) {
			if (NPCHandler.NPCS.get(i).isDead) {
				return;
			}
			if (!NPCHandler.NPCS.get(i).inMulti() && NPCHandler.NPCS.get(i).underAttackBy > 0 && NPCHandler.NPCS.get(i).underAttackBy != c.getIndex()) {
				NPCHandler.NPCS.get(i).killerId = 0;
				return;
			}
			if (!NPCHandler.NPCS.get(i).inMulti() && (c.underAttackBy > 0 || (c.underAttackBy2 > 0 && c.underAttackBy2 != i))) {
				NPCHandler.NPCS.get(i).killerId = 0;
				return;
			}
			if (NPCHandler.NPCS.get(i).heightLevel != c.heightLevel) {
				NPCHandler.NPCS.get(i).killerId = 0;
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				NPCHandler.NPCS.get(i).killerId = 0;
				return;
			}
			c.combatCountdown = 10;
			NPCHandler.NPCS.get(i).facePlayer(c.getIndex());
			boolean special = false;// specialCase(c,i);
			if (goodDistance(NPCHandler.NPCS.get(i).getX(), NPCHandler.NPCS.get(i).getY(), c.getX(), c.getY(), distanceRequired(i)) || special) {
				if (!c.isDead) {
					NPCHandler.NPCS.get(i).facePlayer(c.getIndex());
					NPCHandler.NPCS.get(i).attackTimer = getNpcDelay(i);
					NPCHandler.NPCS.get(i).hitDelayTimer = getHitDelay(i);
					NPCHandler.NPCS.get(i).attackType = 0;
					if (special) {
						loadSpell2(i);
					} else {
						loadSpell(c, i);
					}
					if (NPCHandler.NPCS.get(i).attackType == 3) {
						NPCHandler.NPCS.get(i).hitDelayTimer += 2;
					}
					if (NPCHandler.NPCS.get(i).projectileId > 0) {
						int nX = NPCHandler.NPCS.get(i).getX() + offset(i);
						int nY = NPCHandler.NPCS.get(i).getY() + offset(i);
						int pX = c.getX();
						int pY = c.getY();
						int offX = (nY - pY) * -1;
						int offY = (nX - pX) * -1;
						c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i), NPCHandler.NPCS.get(i).projectileId, 43, 31, -c.getIndex() - 1, 65);
					}
					c.underAttackBy2 = i;
					c.singleCombatDelay2 = System.currentTimeMillis();
					NPCHandler.NPCS.get(i).oldIndex = c.getIndex();
					startAnimation(getAttackEmote(i), i);
					c.getPA().removeAllWindows();
				}
			}
		}
	}

	public int offset(int i) {
		switch (NPCHandler.NPCS.get(i).npcType) {
		case 2042:
		case 2043:
		case 2044:
			return 5068;
		case 4173:
		case 4172:
		case 4174:
		case 4175:
			return 10;
		case 3590:
		case 50:
		case 742:
			return 2;
		case 6260:
		case 6165:
			return 5;
		case 6223:
		case 6225:
		case 6227:
			return 10;
		case 2881:
		case 2882:
			return 1;
		case 2745:
		case 2743:
			return 1;
		case 1158:
		case 1160:
			return 2;
		}
		return 0;
	}

	public boolean retaliates(int npcType) {
		return !(npcType > 1531 && npcType < 1536) && (npcType < 6141 || npcType > 6153 && !(npcType >= 2440 && npcType <= 2446));
	}

	public void applyDamage(int i) {

		if (NPCHandler.NPCS.get(i) != null) {
			if (World.PLAYERS.get(NPCHandler.NPCS.get(i).oldIndex) == null) {
				return;
			}
			if (NPCHandler.NPCS.get(i).isDead) {
				return;
			}
			Player c = World.PLAYERS.get(NPCHandler.NPCS.get(i).oldIndex);
			// if (c.playerIndex <= 0 && c.npcIndex <= 0 && c.autoRetaliate) {
			// c.npcIndex = i;
			// }
			if (c.playerIndex <= 0 && c.npcIndex <= 0 || c.npcIndex == i) {
				if (c.autoRetaliate)
					c.npcIndex = i;
			}
			if (c.attackTimer <= 3 || c.attackTimer == 0 && c.npcIndex == 0 && c.oldNpcIndex == 0) {
				c.startAnimation(c.getCombat().getBlockEmote());
			}
			if (!c.isDead) {
				boolean isBoss = Bosses.isBoss(NPCHandler.NPCS.get(i).npcType);
				Boss boss = Bosses.get(NPCHandler.NPCS.get(i).npcType);
				int damage = 0;
				if (NPCHandler.NPCS.get(i).attackType == 0) {
					damage = Misc.random(NPCHandler.NPCS.get(i).maxHit);
					if (isBoss) {
						damage = Misc.random(boss.getMaximumDamage(NPCHandler.NPCS.get(i).attackType));
					}
					if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) > Misc.random(NPCHandler.NPCS.get(i).attack)) {
						damage = 0;
					}
					if (c.prayerActive[18]) {
						if (isBoss) {
							damage = Misc.random(boss.getProtectionDamage(ProtectionPrayer.MELEE, damage));
						} else {
							damage = 0;
						}
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
				}

				if (NPCHandler.NPCS.get(i).attackType == 1) {
					damage = Misc.random(NPCHandler.NPCS.get(i).maxHit);
					if (isBoss) {
						damage = Misc.random(boss.getMaximumDamage(NPCHandler.NPCS.get(i).attackType));
					}
					if (10 + Misc.random(c.getCombat().calculateRangeDefence()) > Misc.random(NPCHandler.NPCS.get(i).attack)) {
						damage = 0;
					}
					if (c.prayerActive[17]) {
						if (isBoss) {
							damage = Misc.random(boss.getProtectionDamage(ProtectionPrayer.RANGE, damage));
						} else {
							damage = 0;
						}
					}
				}
				if (NPCHandler.NPCS.get(i).attackType == 2) {
					damage = Misc.random(NPCHandler.NPCS.get(i).maxHit);
					if (isBoss) {
						damage = Misc.random(boss.getMaximumDamage(NPCHandler.NPCS.get(i).attackType));
					}
					boolean magicFailed = false;
					if (10 + Misc.random(c.getCombat().mageDef()) > Misc.random(NPCHandler.NPCS.get(i).attack) && c.spellId != 52) {
						damage = 0;
						magicFailed = true;
					}
					if (c.prayerActive[16]) {
						if (isBoss) {
							damage = Misc.random(boss.getProtectionDamage(ProtectionPrayer.MAGE, damage));
						} else {
							damage = 0;
						}
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
					magicFailed = damage == 0 ? true : false;
					if (NPCHandler.NPCS.get(i).endGfx > 0 && (!magicFailed)) {
						c.gfx100(NPCHandler.NPCS.get(i).endGfx);
					} else {
						c.gfx100(85);
					}
				}
				if (NPCHandler.NPCS.get(i).attackType == 3) { // fire breath
					int anti = c.getPA().antiFire();
					if (anti == 0) {
						if (NPCHandler.NPCS.get(i).npcType == 5363) {
							damage = 20 + Misc.random(20);
						} else {
							damage = Misc.random(30) + 10;
						}
						c.sendMessage("You are badly burnt by the dragon fire!");
					} else if (anti == 1) {
						damage = Misc.random(10);
					} else if (anti == 2) {
						damage = Misc.random(5);
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
					c.gfx100(NPCHandler.NPCS.get(i).endGfx);
				}
				if (c.playerEquipment[Player.playerShield] == 15000 && Misc.random(5) == 0) {
					if (c.playerLevel[5] > 0) {
						if (c.playerLevel[3] > 4) {
							double damageRecieved = damage * 0.85;
							int prayerLost = (int) (damage * 0.3);
							if (c.playerLevel[5] >= prayerLost) {
								damage = (int) damageRecieved;
								c.playerLevel[5] -= prayerLost;
								if (c.playerLevel[5] < 0)
									c.playerLevel[5] = 0;
								c.playerLevel[5] -= prayerLost;
								c.gfx0(247);
								c.getPA().refreshSkill(5);
							}
						}
					}
				}
				if (c.playerEquipment[Player.playerShield] == 15002 && Misc.random(6) == 2) {
					double damageRecieved = damage * 0.91;
					damage = (int) damageRecieved;
					c.gfx0(247);
				}
				c.logoutDelay = System.currentTimeMillis(); // logout delay
				if (c.playerEquipment[Player.playerHat] != 14613)
					PoisonCombatTask.getPoisonType(NPCHandler.NPCS.get(i).npcType).ifPresent(c::poison);
				c.damage(new Hit(damage));
				c.getPA().sendFrame126(Integer.toString(c.playerLevel[3]), 4016);
				if (c.playerLevel[3] <= 0) {
					c.getPA().refreshSkill(3);
					c.updateRequired = true;
				}
			}
		}
	}

	public static void startAnimation(int animId, int i) {
		NPCHandler.NPCS.get(i).animNumber = animId;
		NPCHandler.NPCS.get(i).animUpdateRequired = true;
		NPCHandler.NPCS.get(i).updateRequired = true;
	}

	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return Math.sqrt(Math.pow(objectX - playerX, 2) + Math.pow(objectY - playerY, 2)) <= distance;
	}

	public boolean loadAutoSpawn(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			fileex.printStackTrace();
			// return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
			// return false; Memory leak?
		}
		while (!EndOfFile && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]), Integer.parseInt(token3[3]), Integer.parseInt(token3[4]), getNpcListHP(Integer.parseInt(token3[0])), Integer.parseInt(token3[5]), Integer.parseInt(token3[6]), Integer.parseInt(token3[7]));

				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ignored) {
						ignored.printStackTrace();
					}
					// return true;
				}
			}
			try {
				// this server is so crap lol
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ignored) {
		}
		return false;
	}

	public int getNpcListHP(int npcId) {
		if (npcId <= -1) {
			return 0;
		}
		if (NPCDefinitions.getDefinitions()[npcId] == null) {
			return 0;
		}
		return NPCDefinitions.getDefinitions()[npcId].getNpcHealth();

	}

	public static String getNpcListName(int npcId) {
		if (npcId <= -1) {
			return "None";
		}
		if (NPCDefinitions.getDefinitions()[npcId] == null) {
			return "None";
		}
		return NPCDefinitions.getDefinitions()[npcId].getNpcName();
	}

	public boolean loadNPCList(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			fileex.printStackTrace();
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
			// return false; Memory leakz
		}
		while (!EndOfFile && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("npc")) {
					newNPCList(Integer.parseInt(token3[0]), token3[1], Integer.parseInt(token3[2]), Integer.parseInt(token3[3]));
				}
			} else {
				if (line.equals("[ENDOFNPCLIST]")) {
					try {
						characterfile.close();
						return false;
					} catch (IOException ignored) {
						ignored.printStackTrace();
					}
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				ioexception1.printStackTrace();
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ignored) {
			ignored.printStackTrace();
		}
		return false;
	}

	public static boolean isBandosNpc(int i) {
		return NPCHandler.NPCS.get(i).npcType >= 6260 && NPCHandler.NPCS.get(i).npcType <= 6265;
	}

	public static boolean isArmadylNpc(int i) {
		return NPCHandler.NPCS.get(i).npcType >= 6222 && NPCHandler.NPCS.get(i).npcType <= 6227;
	}

	public static boolean KQnpc(int i) {
		switch (NPCHandler.NPCS.get(i).npcType) {
		case 1158:
			return true;
		}
		return false;
	}

}
