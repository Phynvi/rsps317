package com.bclaus.rsps.server.vd;

import java.util.concurrent.TimeUnit;

import com.bclaus.rsps.server.vd.mobile.Entity;
import com.bclaus.rsps.server.vd.mobile.EntityType;
import com.bclaus.rsps.server.vd.mobile.MobileCharacterList;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;

/**
 * Represents a 'World' where we can update entities
 * 
 * @author Tim
 * @author Mobster
 * 
 */
public class World {

	/**
	 * A list of all players registered to the world
	 */
	public static final MobileCharacterList<Player> PLAYERS = new MobileCharacterList<>(1200);

	/**
	 * A lock to keep synchronization
	 */
	public static final Object LOCK = new Object();

	/**
	 * The update has been announced
	 */
	public static boolean updateAnnounced;

	/**
	 * An update is currently running
	 */
	public static boolean updateRunning;

	/**
	 * The seconds before the update takes place
	 */
	public static int updateSeconds;

	/**
	 * The time the update started
	 */
	public static long updateStartTime;

	/**
	 * Should kick all players online off so we can finish the update
	 */
	private static boolean kickAllPlayers = false;

	/**
	 * Gets the player by the name.
	 * 
	 * @param playerName
	 *            the player name.
	 * @return the player
	 */
	public static Player getPlayerByName(String playerName) {
		for (Player player : World.PLAYERS) {
			if (player == null)
				continue;
			if (player.playerName.equalsIgnoreCase(playerName)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Registers an entity to the world
	 * 
	 * @param entity
	 *            The {@link Entity} to register to the world
	 * @return If the entity can be registered to the world
	 */
	public static boolean register(Entity entity) {
		if (entity.getEntityType() == EntityType.PLAYER) {
			Player player = (Player) entity;
			if (World.PLAYERS.spaceLeft() == 0)
				return false;
			player.isActive = true;
			World.PLAYERS.add(player);
			return true;
		}

		return false;
	}

	/**
	 * Unregisters an entity from the world
	 * 
	 * @param entity
	 *            The {@link Entity} to unregister from the world
	 */
	public static void unregister(Entity entity) {
		if (entity.getEntityType() == EntityType.PLAYER) {
			Player player = (Player) entity;
			if (player.privateChat != 2) {
				for (Player target : World.PLAYERS) {
					if (target == null || !target.isActive)
						continue;
					target.getPA().updatePM(player.getIndex(), 0);
				}
			}
			player.destruct();
		}
	}

	/**
	 * Handles all of the updating for players
	 */
	public static void process() {
		synchronized (LOCK) {
			if (kickAllPlayers) {
				for (Player player : World.PLAYERS) {
					if (player != null) {
						player.disconnected = true;
					}
				}
			}
			for (int index = 0; index < World.PLAYERS.capacity(); index++) {
				Player player = World.PLAYERS.get(index);
				if (player == null || !player.isActive || !player.initialized)
					continue;
				try {
					if ((player.disconnected || !player.getSession().isOpen()) && player.getLastCombatAction().elapsed(TimeUnit.SECONDS.toMillis(30))) {
						PlayerUpdating.disconnect(player);
						unregister(player);
						World.PLAYERS.remove(player);
						continue;
					}
					player.preProcessing();
					player.processQueuedPackets();
					player.resetPacketsProcessed();
					player.process();
					player.postProcessing();
					player.getNextPlayerMovement();
				} catch (Exception e) {
					e.printStackTrace();
					player.disconnected = true;
				}
			}

			for (Player player : World.PLAYERS) {
				if (player == null || !player.isActive || !player.initialized)
					continue;

				try {
					player.update();
				} catch (Exception e) {
					e.printStackTrace();
					player.disconnected = true;
				}
			}
			
			if (updateRunning && !updateAnnounced) {
				updateAnnounced = true;
			}
			if (updateRunning && (System.currentTimeMillis() - updateStartTime > (updateSeconds * 1000))) {
				kickAllPlayers = true;
			}

			for (Player player : World.PLAYERS) {
				if (player == null || !player.isActive || !player.initialized)
					continue;
				try {
					player.clearUpdateFlags();
				} catch (Exception e) {
					e.printStackTrace();
					player.disconnected = true;
				}
			}
		}
	}

}
