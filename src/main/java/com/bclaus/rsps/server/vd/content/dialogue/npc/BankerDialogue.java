
/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.vd.content.dialogue.npc;

import com.bclaus.rsps.server.vd.content.dialogue.Dialogue;
import com.bclaus.rsps.server.vd.content.dialogue.Expression;
import com.bclaus.rsps.server.vd.content.dialogue.Type;
import com.bclaus.rsps.server.util.Misc;

public class BankerDialogue extends Dialogue {
	
	private static final int NPC_ID = 494;

	@Override
	protected void start(Object... parameters) {
		// we start with phase 0, we could have a simple dialogue taht does nothing just like this
		send(Type.NPC, NPC_ID, Expression.CALM_TALK1, "Hello there " + Misc.formatPlayerName(player.playerName) + ", How can I", "help you today?");
	}
	
	@Override
	public void next() {
		// now we can receive more dialogue
		if (phase == 0) {
			send(Type.PLAYER, Expression.HAPPY_JOYFUL, "Not much homie, I want in my bank");
			phase = 1;
		} else if (phase == 1) {
			send(Type.CHOICE, null, "Open Bank", "Nevermind");
		}
	}
	
	@Override
	public void select(int index) {
		if (phase == 1) {
			if (index == 1) {
				player.getPA().openUpBank();
			} else {
				stop();
			}
		}
	}

}
