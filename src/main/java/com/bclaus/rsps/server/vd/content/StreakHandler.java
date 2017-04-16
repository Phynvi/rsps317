package com.bclaus.rsps.server.vd.content;

import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.net.PlayerUpdating;

/**
 * 
 * @author Pk Bowman
 *
 */
public class StreakHandler {
	
	private static final String[] messages = {
		"Put an end to them!", "Finish them!", "Show them who's boss.",
		"Go for their head!", "Get after them!", "They're unstoppable!",
		"Can they be stopped?", "They're a monster!", "Pure savagery!",
		"Who's next?"
	};
	
	public static void checkStreak(final Player killer, final Player killed) {
		int oppKs = killed.ks;
		killed.ks = 0;
		if (oppKs >= 3) {
			PlayerUpdating.announce("<col=255><shad=000000>" + killer.playerName + " has ended " + killed.playerName + "'s killstreak of " + oppKs);
			killer.pkPoints += oppKs;
			killer.sendMessage("Well done, you've ended a streak and receive an extra " + oppKs + " pkp");
		}
		killer.ks++;
		if (killer.ks == 3 || (killer.ks % 5 == 0 && killer.ks > 3)) {
			PlayerUpdating.announce("<col=255><shad=000000>" + killer.playerName + " is on a " + killer.ks + " killstreak. " + messages[Misc.random(messages.length - 1)]);
		}
	}
	
}
