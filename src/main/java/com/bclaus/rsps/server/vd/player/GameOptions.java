package com.bclaus.rsps.server.vd.player;

/**
 * 
 * @author Jason MacKeigan http://www.rune-server.org/members/jason
 * @date Jul 18, 2014, 4:24:51 AM
 */
public class GameOptions {

	Player player;
	private boolean displayNameActive;

	public GameOptions(Player player) {
		this.player = player;
	}

	public void updateUI() {
		player.getPA().sendFrame36(State.DISPLAY_NAME.getState(), displayNameActive == true ? 1 : 0);
	}

	public void showUI() {
		player.getPA().sendFrame126(player.getDisplayName() == null ? "" : player.getDisplayName() != null && player.getDisplayName().length() > 0 ? player.getDisplayName() : "", 56306);
		player.getPA().showInterface(56200);
	}

	public boolean isDisplayNameActive() {
		return displayNameActive;
	}

	public void setDisplayNameActive(boolean displayNameActive) {
		this.displayNameActive = displayNameActive;
	}

	enum State {
		HITMARKS(950),
		DISPLAY_NAME(951);

		int state;

		private State(int state) {
			this.state = state;
		}

		int getState() {
			return this.state;
		}
	}

}
