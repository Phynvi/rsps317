/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.task.impl;

import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.player.Player;

public final class AltarRestore extends ScheduledTask {

	public AltarRestore() {
		super(500);
	}

	@Override
	public void execute() {
		for (Player player : World.PLAYERS) {
			if(player != null) {
				player.resetDamageReceived();
				player.canRestoreSpecial = true;
			} 
		}

	}

}