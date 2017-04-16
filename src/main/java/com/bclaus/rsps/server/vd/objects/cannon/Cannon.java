package com.bclaus.rsps.server.vd.objects.cannon;

import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.world.Object;
import com.bclaus.rsps.server.vd.npc.NPC;

/**
 * @author lare96
 */
public class Cannon extends Object {

	private Player player;

	public Cannon(Player player) {
		super(6, player.getPosition());
		this.player = player;
	}

	private FireDirection direction = FireDirection.NORTH;
	private boolean currentlyFiring;
	private int ammunition;

	/**
	 * Handles the firing of the cannonball projectile.
	 * 
	 * @param player
	 *            the player firing this cannon.
	 * @param cannon
	 *            the cannon being fired.
	 * @param npc
	 *            the target being fired at.
	 */
	public void fireProjectile(NPC npc) {
		// Position offset = new Position(((getPosition().getX() - npc
		// .getPosition().getX()) * -1 ), ((getPosition().getY() - npc
		// .getPosition().getY()) * -1));
		//
		// player.getPA().createPlayersProjectile(getPosition(), offset, 50,
		// 100, 53, 20, 20, -npc.npcId + 1, 30);

		int oX = getPosition().getX() + 1;
		int oY = getPosition().getY() + 1;
		int offX = ((oX - npc.absX) * -1);
		int offY = ((oY - npc.absY) * -1);

		player.getPA().createPlayersProjectile(oX, oY, offY, offX, 50, 100, 53, 20, 20, -npc.getIndex() + 1, 30);
	}

	public void fireRotation() {
		switch (direction) {
		case NORTH:
			direction = FireDirection.NORTH_EAST;
			break;
		case NORTH_EAST:
			direction = FireDirection.EAST;
			break;
		case EAST:
			direction = FireDirection.SOUTH_EAST;
			break;
		case SOUTH_EAST:
			direction = FireDirection.SOUTH;
			break;
		case SOUTH:
			direction = FireDirection.SOUTH_WEST;
			break;
		case SOUTH_WEST:
			direction = FireDirection.WEST;
			break;
		case WEST:
			direction = FireDirection.NORTH_WEST;
			break;
		case NORTH_WEST:
			direction = FireDirection.NORTH;
			break;
		}
	}

	public void decrementAmmunition() {
		ammunition--;
	}

	/**
	 * @return the currentlyFiring
	 */
	public boolean isCurrentlyFiring() {
		return currentlyFiring;
	}

	/**
	 * @param currentlyFiring
	 *            the currentlyFiring to set
	 */
	public void setCurrentlyFiring(boolean currentlyFiring) {
		this.currentlyFiring = currentlyFiring;
	}

	/**
	 * @return the ammunition
	 */
	public int getAmmunition() {
		return ammunition;
	}

	/**
	 * @param ammunition
	 *            the ammunition to set
	 */
	public void setAmmunition(int ammunition) {
		this.ammunition = ammunition;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the direction
	 */
	public FireDirection getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(FireDirection direction) {
		this.direction = direction;
	}
}