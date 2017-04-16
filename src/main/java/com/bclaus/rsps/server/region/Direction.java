package com.bclaus.rsps.server.region;

/**
 * Represents a direction a {@link GameCharacter} can face.
 *
 * @author relex lawl
 */
public enum Direction {

	/**
	 * The north-west direction.
	 */
	NORTH_WEST(0x1280108),

	/**
	 * The north direction.
	 */
	NORTH(0x1280120),

	/**
	 * The north-east direction.
	 */
	NORTH_EAST(0x12801e0),

	/**
	 * The west direction.
	 */
	WEST(0x1280108),

	/**
	 * The east direction.
	 */
	EAST(0x1280180),

	/**
	 * The south-west direction.
	 */
	SOUTH_WEST(0x128010e),

	/**
	 * The south direction.
	 */
	SOUTH(0x1280102),

	/**
	 * The south-east direction.
	 */
	SOUTH_EAST(0x1280183);

	private Direction(int clipMask) {
		this.clipMask = clipMask;
		this.name = toString().toLowerCase().replaceAll("_", " ");
	}

	private final int clipMask;

	private final String name;

	public int getClipMask() {
		return clipMask;
	}

	public String getName() {
		return name;
	}
}
