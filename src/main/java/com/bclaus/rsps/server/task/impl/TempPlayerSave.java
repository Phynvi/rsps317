/**
 *
 * @author Antony
 */
package com.bclaus.rsps.server.task.impl;

import com.bclaus.rsps.server.task.ScheduledTask;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.PlayerSave;

public final class TempPlayerSave extends ScheduledTask {

	public TempPlayerSave() {
		super(750);
	}

	@Override
	public void execute() {
		for (Player player : World.PLAYERS) {
			if(player != null) {
				PlayerSave.saveGame(player);
			} 
		}

	}

}