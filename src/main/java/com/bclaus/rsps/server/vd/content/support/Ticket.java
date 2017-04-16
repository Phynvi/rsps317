package com.bclaus.rsps.server.vd.content.support;

import com.bclaus.rsps.server.Constants;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.content.teleport.TeleportExecutor;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.PlayerSave;
import com.bclaus.rsps.server.vd.world.Position;

/**
 * Ticket.java
 * 
 * @author Stuart <stuart.perera>
 * 
 */

public class Ticket {

	/**
	 * Coordinates for the ticket zone
	 */
	public final static int SOUTHWEST_X = 2008, SOUTHWEST_Y = 4820;

	public final static int NORTHEAST_X = 2029, NORTHEAST_Y = 4829;

	/**
	 * Coordinates to where the player and staff member will teleport to answer
	 * a ticket.
	 */
	public final static int X = 2017;
	public final static int Y = 4825;

	/**
	 * Method for player to submit a ticket
	 * 
	 * @param p
	 *            Instance of the Player
	 * @param reason
	 *            Reason the ticket was submitted.
	 */
	public static void submitTicket(final Player p, final String reason) {
		if (p.filedTicket) {
			p.sendMessage("You still have an active ticket opened! You may not file a new one");
			p.sendMessage("until a staff member has closed your old one.");
			return;
		}
		int submitCount = 0;
		p.reason = reason;
		p.playerX = p.absX;
		p.playerY = p.absY;
		p.playerZ = p.heightLevel;
		p.filedTicket = true;
		p.ticketFiler = p.playerName;
		p.ticketAnswered = false;
		for (Player player : World.PLAYERS) {
			if (player != null) {
				if (Constants.isStaffMember(player)) {
					player.sendMessage("<col=255>" + Misc.formatPlayerName(p.playerName) + " </col> has requested your help. Respond using ::accept playerName");
					player.sendMessage("Their reason for submitting a ticket is the following:");
					player.sendMessage("<col=ff0033>" + p.reason);
					submitCount++;
				}
			}
		}
		if (submitCount == 0) {
			p.sendMessage("There are no staff available to answer tickets at this time!");
			return;
		} else {
			p.sendMessage("Your ticket has been submitted. Please be patient while a staff member responds.");
		}
		PlayerSave.saveGame(p);
	}

	public static void answerTicket(final Player p, final String playerName) {
		for (Player oP : World.PLAYERS) {
			if (p != null && oP != null) {
				if (oP.ticketFiler == null)
					continue;
				if (oP.inTicket()) {
					p.sendMessage("This player is already receiving support");
					return;
				}
				if (!oP.ticketAnswered) {
					if (oP.ticketFiler.equalsIgnoreCase(playerName)) {
						if (oP.inWild()) {
							p.sendMessage("This player is in the wilderness, please have them leave to answer...");
							return;
						}
						oP.sendMessage("You've been teleported because a staff member is now answering your ticket.");
						oP.playerX = oP.absX;
						oP.playerY = oP.absY;
						oP.playerZ = oP.heightLevel;
						oP.ticketAnswered = true;
						PlayerSave.saveGame(oP);
						p.getPA().movePlayer(X - 1, Y, 0);
						oP.getPA().movePlayer(X, Y, 0);
					}
				}

			}
		}
	}

	public static void endTicket(final Player p, final String playerName) {
		for (Player oP : World.PLAYERS) {
			if (p != null && oP != null) {
				if (oP.ticketFiler == null)
					continue;
				if (oP.ticketFiler.equalsIgnoreCase(playerName)) {
					TeleportExecutor.teleport(oP, new Position(oP.playerX, oP.playerY, oP.playerZ));
					oP.sendMessage("We hope your ticket was fully answered! Have a nice day. :D");
				}
				oP.filedTicket = false;
				oP.ticketAnswered = false;
				oP.ticketFiler = "";
			}
		}
	}

	public static void showTickets(final Player p) {
		for (Player oP : World.PLAYERS) {
			if (p != null && oP != null) {
				if (World.PLAYERS.capacity() == 1) {
					continue;
				}
				if (oP.filedTicket) {
					p.sendMessage("Name: " + oP.playerName + ", Reason: " + oP.reason);
					return;
				} else {
				}
			}
		}
	}

}