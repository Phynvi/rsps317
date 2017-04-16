package com.bclaus.rsps.server.vd.content.instanced.impl;

import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.task.ScheduledTask;

public class InstanceFloorReset extends ScheduledTask {

	public InstanceFloorReset() {
		super(600);
	}

	@Override
	public void execute() {
		for (Player player : World.PLAYERS)
			if(player != null) {
				player.instanceFloorReset();
			}
	}
}
