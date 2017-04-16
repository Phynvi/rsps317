package com.bclaus.rsps.server.vd.content.combat.melee;

import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.items.ItemAssistant;

public class MeleeData {

	public static void resetPlayerAttack(Player c) {
		c.usingMagic = false;
		c.faceUpdate(0);
		c.npcIndex = 0;
		c.playerIndex = 0;
		c.getPA().resetFollow();
		c.getPA().sendFrame126("", 35000);
	}

	public static boolean usingHally(Player c) {
		switch (c.playerEquipment[Player.playerWeapon]) {
		case 3190:
		case 3192:
		case 3194:
		case 3196:
		case 3198:
		case 3200:
		case 3202:
		case 3204:
			return true;

		default:
			return false;
		}
	}

	public static void getPlayerAnimIndex(Player c, String weaponName) {
		if(c.playerIsNPC) {
			return;
		}
		c.playerStandIndex = 0x328;
		c.playerTurnIndex = 0x337;
		c.playerWalkIndex = 0x333;
		c.playerTurn180Index = 0x334;
		c.playerTurn90CWIndex = 0x335;
		c.playerTurn90CCWIndex = 0x336;
		c.playerRunIndex = 0x338;

		if (weaponName.contains("halberd") || weaponName.contains("hasta") || weaponName.contains("guthan") || weaponName.contains("sceptre")) {
			c.playerStandIndex = 809;
			c.playerWalkIndex = 1146;
			c.playerRunIndex = 1210;
			return;
		}
		if (weaponName.contains("salamander")) {
			c.playerStandIndex = 5246;
			c.playerWalkIndex = 5248;
			return;
		}
		if (weaponName.startsWith("basket")) {
			c.playerStandIndex = 1836;
			c.playerWalkIndex = 1836;
			c.playerRunIndex = 1836;
		}
		if (weaponName.contains("boxing")) {
			c.playerStandIndex = 3677;
			c.playerWalkIndex = 3680;
		}
		if (weaponName.contains("sled")) {
			c.playerStandIndex = 1461;
			c.playerWalkIndex = 1468;
			c.playerRunIndex = 1467;
			return;
		}
		if (weaponName.contains("dharok")) {
			c.playerStandIndex = 0x811;
			c.playerWalkIndex = 2064;
			return;
		}
		if (weaponName.contains("ahrim")) {
			c.playerStandIndex = 809;
			c.playerWalkIndex = 1146;
			c.playerRunIndex = 1210;
			return;
		}
		if (weaponName.contains("verac")) {
			c.playerStandIndex = 1832;
			c.playerWalkIndex = 1830;
			c.playerRunIndex = 1831;
			return;
		}
		if (weaponName.contains("wand") || weaponName.contains("staff")) {
			c.playerStandIndex = 809;
			c.playerRunIndex = 1210;
			c.playerWalkIndex = 1146;
			return;
		}
		if (weaponName.contains("karil")) {
			c.playerStandIndex = 2074;
			c.playerWalkIndex = 2076;
			c.playerRunIndex = 2077;
			return;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("saradomin sw")||weaponName.contains("abyssal bludgeon")) {
			c.playerStandIndex = 7047;
			c.playerWalkIndex = 7046;
			c.playerRunIndex = 7039;
			c.playerTurnIndex = 7044;
			c.playerTurn180Index = 7044;
			c.playerTurn90CWIndex = 7044;
			c.playerTurn90CCWIndex = 7044;
			return;
		}
		if (weaponName.contains("bow")) {
			c.playerStandIndex = 808;
			c.playerWalkIndex = 819;
			c.playerRunIndex = 824;
			return;
		}

		switch (c.playerEquipment[Player.playerWeapon]) {
		case 4151:
			//13758, 13764, 13740, 13741
		case 13758:
		case 13764:
		case 13740:
		case 13741:
		case 15052:
		case 15053:
		case 15343:
		case 3281:
			c.playerWalkIndex = 1660;
			c.playerRunIndex = 1661;
			break;
		case 8004:
		case 7960:
			c.playerStandIndex = 2065;
			c.playerWalkIndex = 2064;
			break;
		case 6528:
			c.playerStandIndex = 0x811;
			c.playerWalkIndex = 2064;
			c.playerRunIndex = 1664;
			break;
		case 4153:
			c.playerStandIndex = 1662;
			c.playerWalkIndex = 1663;
			c.playerRunIndex = 1664;
			break;
		case 10887:
			c.playerStandIndex = 5869;
			c.playerWalkIndex = 5867;
			c.playerRunIndex = 5868;
			break;
		case 11694:
		case 11696:
		case 11730:
		case 11698:
		case 11700:
			c.playerStandIndex = 7047;
			c.playerWalkIndex = 7046;
			c.playerRunIndex = 7039;
			c.playerTurnIndex = 7044;
			c.playerTurn180Index = 7044;
			c.playerTurn90CWIndex = 7044;
			c.playerTurn90CCWIndex = 7044;
			break;
		case 1305:
			c.playerStandIndex = 809;
			break;
		}
	}

	public static int getWepAnim(Player c, String weaponName) {
		if (c.playerEquipment[Player.playerWeapon] <= 0) {
			switch (c.fightMode) {
			case 0:
				return 422;
			case 2:
				return 423;
			case 1:
				return 422;
			}
		}
		if (weaponName.contains("dart")) {
			return c.fightMode == 2 ? 582 : 6600;
		}
		if (weaponName.contains("salamander")) {
			return 5247;
		}

		if (weaponName.contains("knife") || weaponName.contains("javelin") || weaponName.contains("thrownaxe")) {
			return 806;
		}
		if(weaponName.contains("blowpipe")) {
			return 884;
		}
		// if (weaponName.contains("cross") || weaponName.contains("c'bow")) {
		// return 4230;
		// }
		if (weaponName.contains("karil")) {
			return 2075;
		}
		if (weaponName.contains("halberd") || weaponName.endsWith("dead")) {
			return 440;
		}
		if (weaponName.startsWith("dragon dagger")) {
			return 402;
		}
		if (weaponName.endsWith("dagger")) {
			return 412;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("aradomin sword")) {
			switch (c.fightMode) {
			case 0:// attack
				return 7042;
			case 2:// str
				return 7041;
			case 1:// def
				return 7049;
			case 3:// crush
				return 7048;
			}
		}
		if (weaponName.contains("dharok")||weaponName.contains("bludgeon")) {
			switch (c.fightMode) {
			case 0:// attack
				return 2067;
			case 2:// str
				return 2067;
			case 1:// def
				return 2067;
			case 3:// crush
				return 2066;
			}
		}
		if (weaponName.contains("sword")) {
			return 451;
		}
		if (weaponName.contains("karil")) {
			return 2075;
		}
		if (weaponName.contains("bow") && !weaponName.contains("'bow") || weaponName.contains("seercull")) {
			return 426;
		}
		if (weaponName.contains("'bow")) {
			return 4230;
		}
		if (weaponName.contains("hasta") || weaponName.contains("spear")) {
			return 2080;
		}
		switch (c.playerEquipment[Player.playerWeapon]) { // if you don't want
															// to use
															// strings
		case 6522:
			return 2614;
		case 10034:
		case 10033:
			return 2779;
		case 5698:
			return 376;
		case 8004:
		case 7960:
			return 2075;

		case 4153: // granite maul
			return 1665;
		case 4726: // guthan
			return 2080;
		case 4747: // torag
			return 0x814;
		case 4710: // ahrim
			return 406;
		case 4755: // verac
			return 2062;
		case 4734: // karil
			return 2075;
		case 4151:
		case 13758:
		case 13764:
		case 13740:
		case 13741:
		case 15052:
		case 15053:
		case 15343:
		case 3281:
			return 1658;
		case 6528:
			return 2661;
		case 10887:
			return 5865;
		default:
			return 451;
		}
	}

	public static int getBlockEmote(Player c) {
		String shield = ItemAssistant.getItemName(c.playerEquipment[Player.playerShield]).toLowerCase();
		String weapon = ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase();
		if (shield.contains("defender"))
			return 4177;
		if (shield.contains("2h"))
			return 7050;
		if (shield.contains("book") && (weapon.contains("wand")))
			return 420;
		if (shield.contains("shield"))
			return 1156;
		switch (c.playerEquipment[Player.playerWeapon]) {
		case 4755:
			return 2063;
		case 15241:
			return 1156;// 12156
		case 13899:
			return 1156;// 13042
		case 18355:
			return 1156;// 13046
		case 14484:
			return 397;
		case 11716:
		case 15597:
			return 420;// 12008
		case 4153:
			return 1666;
		case 4151:
		case 13758:
		case 13764:
		case 13740:
		case 13741:
		case 15052:
		case 15053:

		case 15343:
		case 3281:
			return 1659;
		case 15201:
			return 1156;// 12806
		case 18349:
			return 1156;// 12030
		case 18353:
			return 1156;// 13054
		case 18351:
			return 1156;// 13042

		case 11694:
		case 11698:
		case 11700:
		case 11696:
		case 11730:
			return 7050;
		case -1:
			return 424;
		default:
			return 424;
		}
	}

	public static int getAttackDelay(Player c, String s) {
		if (c.usingMagic) {
			switch (Player.MAGIC_SPELLS[c.spellId][0]) {
			case 12871: // ice blitz
			case 13023: // shadow barrage
			case 12891: // ice barrage
				return 5;

			default:
				return 5;
			}
		}
		if (c.playerEquipment[Player.playerWeapon] == -1)
			return 4;// unarmed
		switch (c.playerEquipment[Player.playerWeapon]) {
		case 11235:
			return 9;
		case 11730:
			return 4;
		case 6528:
			return 7;
		case 10033:
		case 10034:
			return 5;
		case 3281:
			return 3;
		}
		if (s.endsWith("greataxe"))
			return 7;
		else if (s.equals("torags hammers"))
			return 5;
		else if (s.equals("barrelchest anchor"))
			return 7;
		else if (s.equals("guthans warspear"))
			return 5;
		else if (s.equals("veracs flail"))
			return 5;
		else if (s.equals("ahrims staff"))
			return 6;
		else if (s.contains("staff")) {
			if (s.contains("zamarok") || s.contains("guthix") || s.contains("saradomian") || s.contains("slayer") || s.contains("ancient"))
				return 4;
			else
				return 5;
		} else if (s.contains("bow")) {
			if (s.contains("composite") || s.equals("seercull"))
				return 5;
			else if (s.contains("aril"))
				return 4;
			else if (s.contains("Ogre"))
				return 8;
			else if (s.contains("short") || s.contains("hunt") || s.contains("sword"))
				return 4;
			else if (s.contains("long") || s.contains("crystal"))
				return 6;
			else if (s.contains("'bow"))
				return 7;

			return 5;
		} else if (s.contains("dagger"))
			return 4;
		else if (s.contains("godsword") || s.contains("2h"))
			return 6;
		else if (s.contains("longsword"))
			return 5;
		else if (s.contains("sword"))
			return 4;
		else if (s.contains("scimitar"))
			return 4;
		else if (s.contains("mace"))
			return 5;
		else if (s.contains("battleaxe"))
			return 6;
		else if (s.contains("pickaxe"))
			return 5;
		else if (s.contains("thrownaxe"))
			return 5;
		else if (s.contains("axe"))
			return 5;
		else if (s.contains("warhammer"))
			return 6;
		else if (s.contains("2h"))
			return 7;
		else if (s.contains("spear"))
			return 5;
		else if (s.contains("claw"))
			return 4;
		else if (s.contains("halberd"))
			return 7;
		else if (s.equals("granite maul"))
			return 7;
		else if (s.equals("toktz-xil-ak"))// sword
			return 4;
		else if (s.equals("tzhaar-ket-em"))// mace
			return 5;
		else if (s.equals("tzhaar-ket-om"))// maul
			return 7;
		else if (s.equals("toktz-xil-ek"))// knife
			return 4;
		else if (s.equals("toktz-xil-ul"))// rings
			return 4;
		else if (s.equals("toktz-mej-tal"))// staff
			return 6;
		else if (s.contains("whip"))
			return 4;
		else if (s.contains("hardcore"))
			return 2;
		else if(s.contains("blowpipe"))
			return 3;
		else if (s.contains("dart"))
			return 3;
		else if (s.contains("knife"))
			return 3;
		else if (s.contains("javelin"))
			return 6;
		return 5;
	}

	public static int getHitDelay(Player c, int i, String weaponName) {
		if (c.usingMagic) {
			switch (Player.MAGIC_SPELLS[c.spellId][0]) {
			case 12891:
				return 4;
			case 12871:
				return 6;
			default:
				return 4;
			}
		} else {
			if (weaponName.contains("dart")) {
				return 3;
			}
			if (weaponName.contains("knife") || weaponName.contains("javelin") || weaponName.contains("thrownaxe")) {
				return 3;
			}
			if (weaponName.contains("cross") || weaponName.contains("c'bow")) {
				return 4;
			}
			if (weaponName.contains("bow") && !c.dbowSpec) {
				return 4;
			} else if (c.dbowSpec) {
				return 4;
			}

			switch (c.playerEquipment[Player.playerWeapon]) {
			case 6522: // Toktz-xil-ul
				return 3;
			case 10887:
				return 3;
			case 10034:
			case 10033:
				return 3;
			default:
				return 2;
			}
		}
	}

	public static int npcDefenceAnim(int i) {
		int type = NPCHandler.NPCS.get(i).npcType;
		switch (type) {
		case 100:
			return 6189;
		case 101:
			return 6183;
		case 3835:
			return 6232;
		case 2037:
			return 5489;
		case 5529:
			return 5783;
		case 5219:
		case 5218:
			return 5096;
		case 5235:
			return 5395;
		case 10127:
			return 13170;
		case 10057:
			return 10818;
		case 5904:
			return 6330;
		case 5903:
			return 6346;
		case 9463:
		case 9465:
		case 9467:
			return 12792;
		case 6624:
			return 7413;
		case 6619:
			return 7443;
		case 6649:
			return 7469;
		case 6646:
			return 7462;
		case 3836:
			return 6237;
		case 2783:
			return 2732;
		case 8133:
			return 10058;
		case 10141:
			return 13601;
		case 8349:
			return 10923;
		case 9947:
			return 13771;
		case 6260:
			return 7061;
		case 6261:
		case 6263:
		case 6265:
			return 6155;
		case 6222:
			return 6974;
		case 6223:
		case 6225:
		case 6227:
			return 6952;
		case 6203:
			return 6944;
		case 6204:
		case 6206:
		case 6208:
			return 65;
		case 6247:
			return 6966;
		case 6248:
			return 6375;
		case 6250:
			return 7017;
		case 4972: // giant roc
			return 5026;
		case 4971: // baby roc
			return 5032;
		case 6252:
			return 4311;
		case 6229:
		case 6230:
		case 6231:
		case 6232:
		case 6233:
		case 6234:
		case 6235:
		case 6236:
		case 6237:
		case 6238:
		case 6239:
		case 6240:
		case 6241:
		case 6242:
		case 6243:
		case 6244:
		case 6245:
		case 6246:
			return 6952;
		case 6267:
			return 360;
		case 6268:
			return 2933;
		case 6269:
		case 6270:
			return 4651;
		case 6271:
		case 6272:
		case 6273:
		case 6274:
			return 4322;
		case 6275:
			return 165;
		case 6276:
		case 6277:
		case 6278:
			return 4322;
		case 6279:
		case 6280:
			return 6183;
		case 6281:
			return 6136;
		case 6282:
			return 6189;
		case 6283:
			return 6183;
		case 6210:
			return 6578;
		case 6211:
			return 170;
		case 6212:
		case 6213:
			return 6538;
		case 6215:
			return 1550;
		case 6216:
		case 6217:
			return 1581;
		case 6218:
			return 4301;
		case 6258:
			return 2561;
		case 10775:
			return 13154;
		case 113:
			return 129;
		case 114:
			return 360;
		case 3058:
			return 2937;
		case 3061:
			return 2961;
		case 3063:
			return 2937;
		case 3065:
			return 2720;
		case 3066:
			return 2926;
		case 1265:
			return 1313;
		case 118:
			return 100;
		case 2263:
			return 2181;
		case 82:
		case 84:
		case 752:
		case 3064:
		case 83: // lesser
			return 65;
		case 3347:
		case 3346:
			return 3325;
		case 1192:
			return 1244;
		case 3062:
			return 2953;
		case 3060:
			return 2964;
		case 2892: // Spinolyp
		case 2894: // Spinolyp
		case 2896: // Spinolyp
			return 2869;
		case 1624:
		case 4179:
			return 1555;
		case 3200:
			return 3148;
		case 1354:
		case 1341:
		case 2455:// dagannoth
		case 2454:
		case 2456:
			return 1340;
		case 127:
			return 186;
		case 119:
			return 100;
		case 2881: // supreme
		case 2882: // prime
		case 2883: // rex
			return 2852;
		case 3452:// penance queen
			return 5413;
		case 2745:
			return 2653;
		case 2743:
			return 2645;
		case 1590:// metal dragon
		case 1591:
		case 1592:
			return 89;
		case 2598:
		case 2599:
		case 2600:
		case 2610:
		case 2601:
		case 2602:
		case 2603:
		case 2606:// tzhaar-xil
		case 2591:
		case 2604:// tzhar-hur
			return 2606;
		case 66:
		case 67:
		case 168:
		case 169:
		case 162:
		case 68:// gnomes
			return 193;
		case 160:
		case 161:
			return 194;
		case 163:
		case 164:
			return 193;
		case 438:
		case 439:
		case 440:
		case 441:
		case 442: // Tree spirit
		case 443:
			return 95;
		case 391:
		case 392:
		case 393:
		case 394:
		case 395:// river troll
		case 396:
			return 285;
		case 413:
		case 414:
		case 415:
		case 416:
		case 417:// rock golem
		case 418:
			return 154;

		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
		case 1158: // kalphite
			return 1186;
		case 1160: // kalphite
			return 1179;
		case 2734:
		case 2627:// tzhaar
			return 2622;
		case 2630:
		case 2629:
		case 2736:
		case 2738:
			return 2626;
		case 2631:
		case 2632:
			return 2629;
		case 2741:
			return 2635;

		case 908:
			return 129;
		case 909:
			return 147;
		case 911:
			return 65;

		case 1459:// monkey guard
			return 1403;

		case 1633: // pyrefiend
		case 3406:
			return 1581;

		case 1612:// banshee
			return 1525;

		case 1648:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1653:
		case 1654:
		case 1655:
		case 1656:
		case 1657:// crawling hand
			return 1591;

		case 1604:
		case 1605:
		case 1606:
		case 1607:// aberrant specter
			return 1509;

		case 1618:
		case 1619:// bloodveld
			return 1550;

		case 1643:
		case 1644:
		case 1645:
		case 1646:
		case 1647:// infernal mage
			return 430;

		case 1613:// nechryael
			return 1529;

		case 1610:
		case 1611:// gargoyle
			return 1519;

		case 1615:// abyssal demon
			return 1537;

		case 1770:
		case 1771:
		case 1772:
		case 1773:
		case 2678:
		case 2679:
		case 1774:
		case 1775:
		case 1776:// goblins
			return 312;

		case 132: // monkey
			return 221;

		case 1030:
		case 1031:
		case 1032:
		case 1033:
		case 1034:
		case 1035: // wolfman
			return 6538;

		case 1456:// monkey archer
			return 1395;

		case 108:// scorpion
		case 1477:
			return 247;
		case 107:
		case 144:
			return 6255;

		case 1125:// dad
			return 285;

		case 1096:
		case 1097:
		case 1098:
		case 1942:
		case 1101:// troll
		case 1106:
			return 285;
		case 1095:
			return 285;

		case 123:
		case 122:// hobgoblin
			return 165;

		case 49:// hellhound
		case 142:
		case 95:
			return 6578;

		case 1593:
		case 152:
		case 45:
		case 1558: // wolf
		case 1954:
			return 76;

		case 89:
			return 6375;
		case 133: // unicorns
			return 290;

		case 105:// bear
			return 4921;

		case 74:
		case 75:
		case 76:
			return 5574;

		case 73:
		case 751: // zombie
		case 77:
			return 300;

		case 60:
		case 64:
		case 59:
		case 61:
		case 63:
		case 134:
		case 2035: // spider
		case 62:
		case 1009:
			return 147;

		case 103:
		case 749:
		case 104:
		case 655:
		case 491: // ghost
			return 124;

		case 1585:
		case 110: // giant
			return 4671;
		case 111:
			return 4671;
		case 117:
		case 116:
		case 112:
			return 4651;

		case 50: // kbd
			return 89;

		case 941:// green dragon
		case 55:
		case 742:
		case 1589:
		case 53:
		case 52:
		case 54:
			return 89;
		case 2889:
			return 2860;
		case 81: // cow
		case 397:
			return 5850;

		case 708: // imp
			return 170;

		case 86:
		case 87:
			return 139;
		case 47:// rat
			return 2706;
		case 2457:
			return 2366;
		case 128: // snake
		case 1479:
			return 276;

		case 1017:
		case 2693:
		case 41: // chicken
			return 55;

		case 90:
		case 91:
		case 92:
		case 93: // skeleton
			return 261;
		case 1:
			return 424;
		default:
			return -1;
		}
	}
}