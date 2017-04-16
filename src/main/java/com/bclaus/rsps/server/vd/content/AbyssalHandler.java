package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.Position;

public class AbyssalHandler {

	public static void handleAbyssalTeleport(Player c, int objectId) {
		switch (objectId) {
		case 7147:// added
			c.getPA().movePlayer(3030, 4842, 0);
			break;
		case 7133:// added nature
			TeleportExecutor.teleport(c, new Position(2395, 4841, 0));
			break;
		case 7132: // cosmic
			TeleportExecutor.teleport(c, new Position(2144, 4831, 0));
			break;
		case 7129: // fire
			TeleportExecutor.teleport(c, new Position(2585, 4836, 0));
			break;
		case 7130: // earth
			TeleportExecutor.teleport(c, new Position(2658, 4839, 0));
			break;
		case 7131: // body
			TeleportExecutor.teleport(c, new Position(2525, 4830, 0));
			break;
		case 7140: // mind
			TeleportExecutor.teleport(c, new Position(2786, 4834, 0));
			break;
		case 7139: // air
			TeleportExecutor.teleport(c, new Position(2844, 4837, 0));
			break;
		case 7138: // soul
			c.sendMessage("Debug");
			break;
		case 7141: // blood
			c.sendMessage("Debug");
			break;
		case 7137: // water
			TeleportExecutor.teleport(c, new Position(2722, 4833, 0));
			break;
		case 7136: // death
			TeleportExecutor.teleport(c, new Position(2205, 4834, 0));
			break;
		case 7135:
			TeleportExecutor.teleport(c, new Position(2464, 4830, 0));
			break;
		default:
			c.sendMessage("If you see this message, please PM an Administrator.");
			break;
		}
		c.sendMessage("As you click the object, you teleport to a mystical place...");
	}

}
