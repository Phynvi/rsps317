package com.bclaus.rsps.server.vd.content.teleport;

import com.bclaus.rsps.server.vd.world.Position;

/**
 * Represents a single teleport
 * 
 * @author Mobster
 *
 */
public class Teleport {

	/**
	 * The {@Link Position} the teleport will end
	 */
	private final Position location;

	/**
	 * The type of teleport this is
	 */
	private final TeleportType type;

	/**
	 * Constructs a new teleport
	 * 
	 * @param location
	 *            The end {@link Position} of the teleport
	 * @param type
	 *            The {@link TeleportType} this is
	 */
	public Teleport(Position location, TeleportType type) {
		this.location = location;
		this.type = type;
	}

	/**
	 * Gets the {@link Position} the teleport will end
	 * 
	 * @return The {@link Position} this teleport will end
	 */
	public Position getLocation() {
		return location;
	}

	/**
	 * Gets the {@link TeleportType} this teleport is
	 * 
	 * @return The {@link TeleportType} this teleport is
	 */
	public TeleportType getType() {
		return type;
	}

}
