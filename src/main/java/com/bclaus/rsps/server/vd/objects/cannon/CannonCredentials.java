package com.bclaus.rsps.server.vd.objects.cannon;

import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author lare96
 */
public class CannonCredentials {

	@SuppressWarnings("unused")
	private Player player;
	private Setup setupStage = Setup.NOTHING;
	private Cannon cannon;

	public CannonCredentials(Player player) {
		this.player = player;
	}

	public boolean hasCannon() {
		return setupStage != Setup.NOTHING;
	}

	public Setup getSetupStage() {
		return setupStage;
	}

	public void setSetupStage(Setup setupStage) {
		this.setupStage = setupStage;
	}

	public Cannon getCannon() {
		return cannon;
	}

	public void setCannon(Cannon cannon) {
		this.cannon = cannon;
	}
}