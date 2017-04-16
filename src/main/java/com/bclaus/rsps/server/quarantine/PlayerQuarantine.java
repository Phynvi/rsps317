package com.bclaus.rsps.server.quarantine;

import com.bclaus.rsps.server.vd.player.Boundary;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;

/**
 * 
 * @author Jason MacKeigan http://www.rune-server.org/members/jason
 * @date Jul 5, 2014, 8:18:17 AM
 */
public class PlayerQuarantine {

	Player player;
	private boolean quarantined;

	/**
	 * The PlayerQuarantine constructor
	 * 
	 * @param player
	 *            The player object
	 */
	public PlayerQuarantine(Player player) {
		this.player = player;
		this.quarantined = QuarantineIO.contains(player.playerName, player.getIdentity());
	}

	public static void checkOnlineMatches(Player owner, String identity, int severity) {
		for (Player p : World.PLAYERS) {
			if (p == null)
				continue;
			if (identity.equalsIgnoreCase(p.getIdentity())) {
				owner.sendMessage("You have sucessfully quarantined " + p.playerName + " as a result of a quarantine punishment.");
				p.getQuarantine().setQuarantined(true);
				QuarantineIO.add(p.playerName, p.getIdentity(), severity);
				if (severity == 1) {
					p.logoutDelay = System.currentTimeMillis() - 60000;
					p.logout();
				}
			}
		}
	}

	/**
	 * The function that will handle certain flags and operations that the
	 * quarantine requires every 600 milliseconds.
	 */
	public void process() {
		if (!quarantined)
			return;
		if (!Boundary.isInBounds(player, Boundary.QUARANTINE))
			player.getPA().movePlayer(2463, 4782, 0);
	}
	
	

	/**
	 * Determines if the player is quarantined
	 * 
	 * @return If the player is quarantined
	 */
	public boolean isQuarantined() {
		return quarantined;
	}

	/**
	 * Sets the state of the quarantine
	 * 
	 * @param quarantined
	 *            The state of the quarantine
	 */
	public void setQuarantined(boolean quarantined) {
		this.quarantined = quarantined;
	}

}
