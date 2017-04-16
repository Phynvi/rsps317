package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;
import com.bclaus.rsps.server.util.Misc;

/**
 * 
 * @author Jason http://www.rune-server.org/members/jason
 * @date Nov 26, 2013
 */
/**
 * Add reward of noted items like sharks, pots, veng runes or barrage runes
 */
public class PlayerKillstreak {

	private Player player;
	public static final int MAXIMUM_ENTRIES = 6;

	public static final int[][] KILLSTREAKS = { { 2, 2, 25000 }, { 4, 3, 50000 }, { 7, 4, 100000 }, { 10, 5, 200000 } };

	public PlayerKillstreak(Player player) {
		this.player = player;
	}

	public void resetKillstreak(Player other) {
		if (player.killStreak > 1) {
			player.sendMessage("Your " + player.killStreak + " player killstreak has been reset, better luck next time.");
			if (player.killStreak > 1) {
				other.pkPoints += calculatePkPoints();
				other.sendMessage("You receive an additional " + calculatePkPoints() + " pk points because " + player.playerName + " was on a killstreak.");
			}
		}
		player.killStreak = 0;
	}

	public int calculatePkPoints() {
		if (player.isSponsor) {
			player.sendMessage("<col=CC0099>As a sponsor you receive an additional amount of PKP.");
			if (player.killStreak > 1 && player.killStreak < 4)
				return 125;
			else if (player.killStreak >= 4 && player.killStreak < 7)
				return 155;
			else if (player.killStreak >= 7 && player.killStreak < 10)
				return 180;
			else if (player.killStreak >= 10)
				return 225;
		} else {
			if (DoubleExperience.isDoubleExperience() || player.DoublePKP > 0) {
				if (player.killStreak > 1 && player.killStreak < 4)
					return 100;
				else if (player.killStreak >= 4 && player.killStreak < 7)
					return 130;
				else if (player.killStreak >= 7 && player.killStreak < 10)
					return 160;
				else if (player.killStreak >= 10)
					return 200;
			}
			if (player.killStreak > 1 && player.killStreak < 4)
				return 50;
			else if (player.killStreak >= 4 && player.killStreak < 7)
				return 65;
			else if (player.killStreak >= 7 && player.killStreak < 10)
				return 80;
			else if (player.killStreak >= 10)
				return 100;
		}
		if (DoubleExperience.isDoubleExperience()  || player.isSponsor) {
			return 50;
		} else {
			return 25;
		}
	}

	public void addKillstreak(Player killed) {
		player.killStreak++;
		for (int[] i : KILLSTREAKS) {
			if (player.killStreak == i[0]) {
				Server.itemHandler.createGroundItem(player, 995, player.absX, player.absY, player.heightLevel, i[2]);
				player.sendMessage("Your killstreak has increased to <col=255>" + i[0] + "</col>. You now receive <col=255>" + calculatePkPoints() + "</col> pk points for every kill.");
			}
		}
		if (player.killStreak > player.highestKillStreak) {
			player.highestKillStreak = player.killStreak;
			player.sendMessage("Congratulations your highest killstreak is now <col=255>" + player.highestKillStreak + "</col>.");
		}
		if (player.killStreak > 1)
			PlayerUpdating.announce("<col=255>" + Misc.formatPlayerName(player.playerName) + " <col=ff0033>Killed<col=255> " + killed.playerName + "</col> and is now on a killstreak of <col=ff0033>" + player.killStreak + "</col>.");
	}

}
