package com.bclaus.rsps.server.vd.player.impl;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.task.ScheduledTask;

public class UpdateTime extends ScheduledTask {

	public UpdateTime() {
		super(60);
	}
	
	String mes = "<col=ee134a><shad=000000>";

	@Override
	public void execute() {
		for (Player player : World.PLAYERS) {
			if (player != null) {
				player.getPA().sendFrame126("" + mes + Server.CALENDAR.toString() +"", 39180);
			}
		}
				
	}

}