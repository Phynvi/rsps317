package com.bclaus.rsps.server.vd.content.teleport;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.content.minigames.PestControl;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.Position;

public enum CityTeleport {

	FALADOR(2964, 3378, "Falador"), VARROCK(3210, 3424, "Varrock"), LUMBRIDGE(3222, 3218, "Lumbridge"), CAMELOT(2757, 3477, "Camelot");

	private int x;
	private int y;
	private String location;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getLocation() {
		return location;
	}

	CityTeleport(int x, int y, String location) {
		this.x = x;
		this.y = y;
		this.location = location;
	}

	public boolean canTeleport(Player player) {
		if (System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return false;
		}
		if (!player.startTele2) {
			if (player.inWild() && player.wildLevel > Constants.NO_TELEPORT_WILD_LEVEL) {
				if (player.InKbd()) {
				} else {
					player.sendMessage("You can't teleport above level " + Constants.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
					return false;
				}
			}
		}
		if (player.isDead || player.teleporting || player.stopPlayerPacket) {
			return false;
		}
		if (PestControl.isInPcBoat(player)) {
			player.sendMessage("Please exit the boat before teleporting out.");
			return false;
		}
		if (player.playerIsNPC) {
			player.sendMessage("You can't teleport like this!");
			return false;
		}
		if (player.inDuelScreen) {
			player.sendMessage("Please close the interface before teleporting");
			return false;
		}
		if (player.duelStatus == 5) {
			player.sendMessage("You can't teleport during a duel!");
			return false;
		}
		if (System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return false;
		}
		player.teleporting = true;
		player.getPA().removeAllWindows();
		player.dialogueAction = -1;
		player.npcIndex = 0;
		player.playerIndex = 0;
		player.faceUpdate(0);
		return true;
	}

	public void executeTeleport(Player player, CityTeleport teleport) {
		if (canTeleport(player)) {
			TeleportExecutor.teleport(player, new Teleport(new Position(teleport.getX(), teleport.getY(), 0), TeleportType.NORMAL), false);
		}
	}

}
