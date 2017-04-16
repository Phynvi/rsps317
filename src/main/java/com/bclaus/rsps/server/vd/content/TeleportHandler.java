package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.Position;

/**
 * 
 * @author CrazeLuL
 *
 */

public class TeleportHandler {
	
	
	/**
	 * 
	 * @param c Handles the clicking of the tabs for the interfaces to switch
	 * @param actionButtonId
	 * @return
	 */
	public static boolean handleTabButtons(Player c, int actionButtonId) {
		switch (actionButtonId) {
		case 64122:
			c.getPA().showInterface(16500);
			break;
		case 64123:
			c.getPA().showInterface(16700);
			break;
		case 64124:
			c.getPA().showInterface(16900);
			break;
		case 64125:
			c.getPA().showInterface(19100);
			break;
		case 10143:
			c.getPA().closeAllWindows();
			break;
		default:
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param c Sends the strings so that they load up
	 */
	public static void sendStrings(Player c) {
		sendMonsterStrings(c);
		sendBossStrings(c);
		sendDungeonStrings(c);
		sendTeleportStrings(c);
	}
	
	
	/**
	 * Handles the texts on the teleport interface
	 * @param player
	 */
	public static void sendTeleportStrings(Player player) {
		String[] teleports = { "TESTER", "Open", "TEST", "Open", "Open", "Open", "Open", "Open", "Open",
				"Pest Control", "TzHaar Caves", "Party Room", "Warriors Gui1ld", "Zombies", "@red@Varrock PK", "@red@Castle PK", "@red@Mage Bank",
				"@red@Green Dragons", "@red@Rev Dungeon", "@red@Greater Demons", "Market", };
		for (int i = 0; i < 63; i += 3) {
			player.getPA().sendString(teleports[i / 3], 17104 + i);
		}
	}

	/**
	 * Handles the text sent on the dungeon interface
	 * @param player
	 */
	public static void sendDungeonStrings(Player player) {
		String[] locs = { "Barrows", "Duel Arena", "Pest Control", "Barbarian Assault", "Fight Cave" };
		String[] insides = {
				"Click here to teleport to Barrows Minigame",
				"Click here to teleport to Duel Arena",
				"Click here to teleport to Pest Control",
				"Click here to teleport to Barbarian Assault",
				"Click here to teleport to Fight Cave" };
		for (int i = 0; i < 20; i += 4) {
			player.getPA().sendString(locs[i / 4], 16906 + i);
			player.getPA().sendString(insides[i / 4], 16907 + i);
		}
	}

	/**
	 * Handles the text on the bosses interface
	 * @param c
	 */
	public static void sendBossStrings(Player c) {
		String[] gwdBosses = new String[] { "Varrock PvP", "Mage Bank (@gre@1vs1@bla@)", "Chaos Temple (@red@Multi@bla@)", "Graveyard", "Castle Drags" };
		String[] normalBosses = new String[] { "Barrelchest", "Godwars Dungeon", "Kalphite Queen", "King Black Dragon (wildy)", "Chaos Elemental (wildy)", "Dagannoth Kings",
				"Giant Mole", "Zulrah", "Open", "Open" };
		String[] normalBosses2 = new String[] { "Kraken", "Vetion(wildy)", "Scorpia(wildy)", "Venenatis(wildy)", "Glod", "Callisto (wildy)",
				"Ice queen ", "Close (Click here)", "Open", "Open", };

		for (int i = 0; i < 15; i += 3) {
			c.getPA().sendString(gwdBosses[i / 3], 16722 + i);
		}

		for (int i = 0; i < 30; i += 3) {
			c.getPA().sendString(normalBosses[i / 3], 16772 + i);
		}

		for (int i = 0; i < 30; i += 3) {
			c.getPA().sendString(normalBosses2[i / 3], 16802 + i);
		}

	}

	/**
	 * Sends the monster strings
	 * @param c
	 */
	public static void sendMonsterStrings(Player c) {
		sendMonsterStrings1(c);
		sendMonsterStrings2(c);
		sendMonsterStrings3(c);
	}

	public static void sendMonsterStrings1(Player c) {
		String[] slayerMonsters = new String[] { "Nechryaels", "Hell Hounds", "Abyssal Demons", "Dark Beasts", "Cave Horror", "Greater Demons",
				"Ghosts", "Otherworldy Beings", "Elf Warrior", "Jelly", "Kurask", "Moss Giant", "Black Demons", "Iron/Steel Drags", "Ankous" };
		for (int i = 0; i < 45; i += 3) {
			c.getPA().sendString(slayerMonsters[i / 3], 16522 + i);
		}
	}

	public static void sendMonsterStrings2(Player c) {
		String[] normalMonsters = new String[] { "Skeletal Wyverns", "Pack Yaks", "Bats", "Earth Warrior", "Ape Atoll", "Lunar Island", "Hill Giant",
				"Open", "Open", "Open" };
		for (int i = 0; i < 30; i += 3) {
			c.getPA().sendString(normalMonsters[i / 3], 16572 + i);
		}
	}

	public static void sendMonsterStrings3(Player c) {
		String[] normalMonsters2 = new String[] { "Jungle Demons", "Cave Kraken", "Mithril Dragons", "Crash Island", "Rock Crabs", "Otherworldy Beings",
				"", "Open", "Open", "Open" };
		for (int i = 0; i < 30; i += 3) {
			c.getPA().sendString(normalMonsters2[i / 3], 16602 + i);
		}
	}

	/**
	 * Handles the button clicking of all the interfaces
	 * buttons for teleporting etc.
	 * @param c
	 * @param actionButtonId
	 * @return
	 */
	public static boolean handleButtons(Player c, int actionButtonId) {
		if (handleMonsters(c, actionButtonId)) {
			return true;
		} else if (handleBosses(c, actionButtonId)) {
			return true;
		} else if (handleDungeons(c, actionButtonId)) {
			return true;
		} else if (handleTeleports(c, actionButtonId)) {
			return true;
		}
		return false;
	}

	/**
	 * Handles the slayer monsters teleport buttons
	 * @param c
	 * @param actionButtonId
	 * @return
	 */
	public static boolean handleSlayerMonsters(Player c, int actionButtonId) {
		switch (actionButtonId) {
		case 64137:
			c.sendMessage("dust devil");
			break;
		case 641333:
			c.sendMessage("gargoyle");
		default:
			return false;
		}
		return true;
	}

	/**
	 * Handles the regular monsters teleport buttons
	 * @param c
	 * @param actionButtonId
	 * @return
	 */
	public static boolean handleRegular(Player c, int actionButtonId) {
		switch (actionButtonId) {
		case 64186:
			c.sendMessage("Skeletal Wyverns");
			TeleportExecutor.teleport(c, new Position(2865, 9920, 0));
			break;
		case 64189:
			c.sendMessage("Pack Yaks");
			TeleportExecutor.teleport(c, new Position(2325, 3799, 0));
			break;
		case 64192:
			c.sendMessage("Bats");
			TeleportExecutor.teleport(c, new Position(3134, 9910, 0));
			break;
		case 64195:
			c.sendMessage("Earth Warriors");
			TeleportExecutor.teleport(c, new Position(3124, 9975, 0));
			break;
		case 64216:
			c.sendMessage("Jungle Demons");
			TeleportExecutor.teleport(c, new Position(2641, 4565, 0));
			break;
		case 64219:
			c.sendMessage("Cave Kraken");
			TeleportExecutor.teleport(c, new Position(2393, 4684, 0));
			break;
		case 64222:
			c.sendMessage("Mithril Dragons");
			TeleportExecutor.teleport(c, new Position(3050, 9589, 0));
			break;
		case 64225:
			c.sendMessage("Crash Island");
			TeleportExecutor.teleport(c, new Position(2894, 2727, 0));
			break;
		case 64198:
			c.sendMessage("Ape Atoll");
			TeleportExecutor.teleport(c, new Position(2794, 2786, 0));
			break;
		case 64228:
			c.sendMessage("Rock Crabs");
			TeleportExecutor.teleport(c, new Position(2705, 3716, 0));
			break;
		case 64231:
			c.sendMessage("OtherWorldy Beings");
			TeleportExecutor.teleport(c, new Position(3121, 9909, 0));
			break;
		case 64136:
			c.sendMessage("Nechryaels");
			TeleportExecutor.teleport(c, new Position(3440, 3564, 2));
			break;
		case 64139:
			c.sendMessage("Hellhounds");
			TeleportExecutor.teleport(c, new Position(2858, 9841, 0));
			break;
		case 64142:
			c.sendMessage("Abyssal Demons");
			TeleportExecutor.teleport(c, new Position(3418, 3567, 0));
			break;
		case 64148:
			c.sendMessage("cavehorror");
			TeleportExecutor.teleport(c, new Position(1870, 5190, 0));
			break;
		case 64145:
			c.sendMessage("Dark Beasts");
			TeleportExecutor.teleport(c, new Position(2905, 9684, 0));
			break;
		case 64157:
			c.sendMessage("Ghosts");
			TeleportExecutor.teleport(c, new Position(2905, 9849, 0));
			break;
		case 64160:
			c.sendMessage("Otherworldy Beings");
			TeleportExecutor.teleport(c, new Position(3119, 9909, 0));
			break;
		case 64163:
			c.sendMessage("Elf Warriors");
			TeleportExecutor.teleport(c, new Position(2895, 2725, 0));
			break;
		case 64166:
			c.sendMessage("Jelly");
			TeleportExecutor.teleport(c, new Position(2704, 10027, 0));
			break;
		case 64169:
			c.sendMessage("Kurask");
			TeleportExecutor.teleport(c, new Position(2699, 10000, 0));
			break;
		case 64172:
			c.sendMessage("Moss Giants");
			TeleportExecutor.teleport(c, new Position(2643, 9536, 0));
			break;
		case 64175:
			c.sendMessage("Black Demons");
			TeleportExecutor.teleport(c, new Position(2710, 9485, 0));
			break;
		case 64178:
			c.sendMessage("Iron/Steel Drags");
			TeleportExecutor.teleport(c, new Position(2722, 9442, 0));
			break;
		default:
			return false;
		}
		return true;
	}

	/**
	 * Handles the monsters teleport buttons
	 * @param c
	 * @param actionButtonId
	 * @return
	 */
	public static boolean handleMonsters(Player c, int actionButtonId) {
		if (handleSlayerMonsters(c, actionButtonId)) {
			return true;
			}
		 else if (handleRegular(c, actionButtonId)) {
			return true;
		}
		
		return false;
	}
	/**
	 * Handles the bosses teleport buttons
	 * @param c
	 * @param actionButtonId
	 * @return
	 */
	public static boolean handleBosses(Player c, int actionButtonId) {
		if (handleGwd(c, actionButtonId)) {
			return true;
		} else if (handleOther(c, actionButtonId)) {
			return true;
		}
		return false;
	}
	/**
	 * Handles the gwd monsters teleport buttons
	 * @param c
	 * @param actionButtonId
	 * @return
	 */
	public static boolean handleGwd(Player c, int actionButtonId) {
		switch (actionButtonId) {

		case 65080:
			c.sendMessage("bandos");
			if (c.getItems().playerHasItem(995, 10000000)) {
				c.getItems().deleteItem(995, 10000000);
				TeleportExecutor.teleport(c, new Position(2973, 5354, 0));
				} else {
				c.sendMessage("You may not enter! you need 10,000,000 coins to come here");
				}
			break;
		case 65083:
			c.sendMessage("Saradomin");
			if (c.getItems().playerHasItem(995, 10000000)) {
				c.getItems().deleteItem(995, 10000000);
				TeleportExecutor.teleport(c, new Position(2897, 5269, 0));
				} else {
				c.sendMessage("You may not enter! you need 10,000,000 coins to come here");
				}
			break;
		case 65086:
			c.sendMessage("Zamorak");
			if (c.getItems().playerHasItem(995, 10000000)) {
				c.getItems().deleteItem(995, 10000000);
				TeleportExecutor.teleport(c, new Position(2928, 5323, 0));
				} else {
				c.sendMessage("You may not enter! you need 10,000,000 coins to come here");
				}
			break;
		case 65089:
			c.sendMessage("Armardyl");
			if (c.getItems().playerHasItem(995, 10000000)) {
				c.getItems().deleteItem(995, 10000000);
				TeleportExecutor.teleport(c, new Position(2825, 5302, 0));
				} else {
				c.sendMessage("You may not enter! you need 10,000,000 coins to come here");
				}
			break;
		default:
			return false;
		}
		return false;
	}
	/**
	 * Handles the other teleport buttons
	 * @param c
	 * @param actionButtonId
	 * @return
	 */
	public static boolean handleOther(Player c, int actionButtonId) {
		switch (actionButtonId) {
		case 65130:
			c.sendMessage("Barrelchest");
			TeleportExecutor.teleport(c, new Position(2983, 9516, 0));
			break;
		case 65160:
			c.sendMessage("Kraken");
			TeleportExecutor.teleport(c, new Position(2430, 3686, 0));
			break;
		case 65133:
			c.sendMessage("Godwars Dungeon");
			TeleportExecutor.teleport(c, new Position(2871, 5317, 0));
			break;
		case 65136:
			c.sendMessage("Kalphite Queen");
			TeleportExecutor.teleport(c, new Position(3480, 9484, 0));
			break;
		case 65139:
			c.sendMessage("King Black Dragon (wildy)");
			TeleportExecutor.teleport(c, new Position(33295, 3921, 0));
			break;
		case 65142:
			c.sendMessage("Chaos Elemental (wildy)");
			TeleportExecutor.teleport(c, new Position(3295, 3921, 0));
			break;
		case 65145:
			c.sendMessage("Dagannoth Kings");
			TeleportExecutor.teleport(c, new Position(1908, 4367, 0));
			break;
		}
		return true;
	}
	/**
	 * Handles the dungeon monsters teleport buttons
	 * @param c
	 * @param actionButtonId
	 * @return
	 */
	public static boolean handleDungeons(Player c, int actionButtonId) {
		switch (actionButtonId) {
		case 66008:
			c.sendMessage("taverly");
			TeleportExecutor.teleport(c, new Position(3207, 3214, 0));
			break;
		case 66012:
			c.sendMessage("new mystic");
			TeleportExecutor.teleport(c, new Position(3207, 3214, 0));
		default:
			
			return false;
		}
		return true;
	}
	/**
	 * Handles the city teleport buttons
	 * @param c
	 * @param actionButtonId
	 * @return
	 */
	public static boolean handleTeleports(Player c, int actionButtonId) {
		switch (actionButtonId) {
		case 66206:
			c.sendMessage("lumbridge");
			break;
		case 66209:
			c.sendMessage("varrock");
		default:
			return false;
		}
		return true;

	}
}
