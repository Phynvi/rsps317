package com.bclaus.rsps.server.vd.content.teleport;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.vd.content.minigames.PestControl;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.task.ScheduledTask;

/**
 * Executes all of the teleporting for players
 * 
 * @author Mobster
 *
 */
public class TeleportExecutor {

	/**
	 * Starts a teleport process for the player with just a location using the
	 * players current magic book to determine the type
	 * 
	 * @param player
	 *            The {@link Player} trying to teleport
	 * @param location
	 *            The {@link Position} the player is teleporting too
	 */
	public static void teleport(Player player, Position location) {
		TeleportType type = player.playerMagicBook == 2 ? TeleportType.NORMAL : player.playerMagicBook == 1 ? TeleportType.ANCIENT : TeleportType.NORMAL;
		teleport(player, new Teleport(location, type), true);
	}
	public static void teleport(Player player, Position location, CityTeleport teleport) {
		TeleportType type = player.playerMagicBook == 2 ? TeleportType.NORMAL : player.playerMagicBook == 1 ? TeleportType.ANCIENT : TeleportType.NORMAL;
		teleport(player, new Teleport(location, type), true);
	}
	
	/**
	 * Starts a teleport process for the player
	 * 
	 * @param player
	 *            The {@link Player} attempting to teleport
	 * @param teleport
	 *            The {@link Teleport} type to the location
	 */
	public static void teleport(Player player, Teleport teleport) {
		player.sendMessage("Here");
		teleport(player, teleport, true);
	}

	/**
	 * Starts a teleport process for the player
	 * 
	 * @param player
	 *            The {@link Player} attempting to teleport
	 * @param teleport
	 *            The {@link Teleport} to the location
	 * @param requirements
	 *            Check requirements before attempting to teleport
	 */
	public static void teleport(Player player, Teleport teleport, boolean requirements) {
		if (player.teleTimer > 0) {
			// still teleporting, don't attempt to teleport again
			return;
		}

		/**
		 * Check if we need to perform requirements on our teleport, if so
		 * validate our teleport, this allows us to perform an unchecked
		 * teleport for teleports such as the lever in the wilderness
		 */
		if (requirements && !canTeleport(player)) {
			return;
		}
		
		/* Override requirements */
		if (System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return;
		}

		/**
		 * The start animation of our teleport
		 */
		final int startAnim = teleport.getType().getStartAnimation();

		/**
		 * The end animation of our teleport
		 */
		final int endAnim = teleport.getType().getEndAnimation();

		/**
		 * The start graphic of our teleport
		 */
		int startGraphic = teleport.getType().getStartGraphic();

		/**
		 * The end graphic of our teleport
		 */
		final int endGraphic = teleport.getType().getEndGraphic();

		/**
		 * The initial delay of our teleport, the time it takes to start the
		 * animation till your able to walk again
		 */
		final int initialDelay = teleport.getType().getStartDelay() + teleport.getType().getEndDelay();

		/**
		 * Check if we need to play our start animation
		 */
		if (startAnim != -1) {
			player.startAnimation(startAnim);
		}

		/**
		 * Check if we need to play our start graphic
		 */
		if (startGraphic != -1) {
			if (startGraphic > 65535) {
				// differentiate between height levels
				startGraphic = (startGraphic - 65535);
				player.gfx100(startGraphic);
			} else {
				player.gfx0(startGraphic);
			}
		}

		player.getCombat().resetPlayerAttack();
		player.stopMovement();
		player.getPA().removeAllWindows();
		player.npcIndex = player.playerIndex = 0;
		player.faceUpdate(-1);
		player.teleTimer = initialDelay;
		Server.getTaskScheduler().schedule(new ScheduledTask(1) {

			/**
			 * A modifiable end gfx due to the nature of having to finalize the
			 * included variables so we can differentiate between the height
			 * levels of the graphic
			 */
			int endGfx = endGraphic;

			@Override
			public void execute() {
				player.teleTimer--;
				if (player.teleTimer == initialDelay - teleport.getType().getStartDelay()) {

					/**
					 * Finalize our location by setting our coordinates
					 */
					player.getPA().movePlayer(teleport.getLocation().getX(), teleport.getLocation().getY(), teleport.getLocation().getZ());

					/**
					 * Check if we need to play our end animation
					 */
					if (endAnim != -1) {
						player.startAnimation(endAnim);
					}

					/**
					 * Check if we need to play our end graphic
					 */
					if (endGfx != -1) {
						if (endGfx > 65535) {
							// differentiate between height levels
							endGfx = (endGfx - 65535);
							player.gfx100(endGfx);
						} else {
							player.gfx0(endGfx);
						}
					}

				}
				
				if (player.teleTimer == 0) {
					player.teleTimer = -1;
					player.teleporting = false;
					stop();
				}
			}

		}.attach(player));
	}

	/**
	 * Determines if the player can teleport under the current circumstances
	 * 
	 * @param player
	 *            The {@link Player} attempting to teleport
	 * @param teleport
	 *            The {@link Teleport} the player is currently doing
	 * @return If the player can teleport
	 */
	public static boolean canTeleport(Player player) {
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
		if(player.isInJail()) {
			player.sendMessage("You cannot teleport out of jail.");
			return false;
		}
		if (player.isDead || player.teleporting || player.stopPlayerPacket) {
			return false;
		}
		/*if (PestControl.isInGame(player)) {
			player.sendMessage("You can't teleport from a Pest Control Game!");
			return false;
		}*/
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
		if (player.playerIndex > 0 || player.npcIndex > 0) {
			player.getCombat().resetPlayerAttack();
		}
		player.teleporting = true;
		player.getPA().removeAllWindows();
		player.dialogueAction = -1;
		player.npcIndex = 0;
		player.playerIndex = 0;
		player.faceUpdate(0);
		return true;
	}

	/**
	 * Gets the message sent when attempting to teleport while teleblocked
	 * 
	 * @param player
	 *            The {@link Player} attempting to teleport
	 * @return A converted message of the {@link Player}s teleblock delay
	 */
	private static String getTeleblockMessage(Player player) {
		long remaining = player.teleBlockLength - (System.currentTimeMillis() - player.teleBlockDelay);
		long left = (long) Math.ceil(remaining / 60000.0);
		return "You are teleblocked for the next " + (left == 0 ? 1 : left) + " minutes";
	}

}
