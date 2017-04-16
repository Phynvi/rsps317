package com.bclaus.rsps.server.vd.content.teleport;

/**
 * Represents a single teleport type
 * 
 * @author Mobster
 *
 */
public enum TeleportType {

	/**
	 * A normal spell teleport type
	 */
	NORMAL(4, 2, 714, 715, (65535 + 111), -1),

	/**
	 * The ancient spell teleport type
	 */
	ANCIENT(4, 0, 1979, -1, 392, -1),

	/**
	 * The lunar spell teleport type
	 */
	LUNAR(6, 0, -1, -1, -1, 65535),

	/**
	 * The teleport tablet teleport type
	 */
	TABLET(1, 0, 4731, 65535, 678, 65535);

	/**
	 * The delay before the teleport ends
	 */
	private final int startDelay, endDelay;

	/**
	 * The start and end animation of the teleport
	 */
	private final int startAnim, endAnim;

	/**
	 * The start and end graphic of the teleport
	 */
	private final int startGfx, endGfx;

	private TeleportType(int startDelay, int endDelay, int startAnim, int endAnim, int startGfx, int endGfx) {
		this.startDelay = startDelay;
		this.endDelay = endDelay;
		this.startAnim = startAnim;
		this.endAnim = endAnim;
		this.startGfx = startGfx;
		this.endGfx = endGfx;
	}

	/**
	 * The delay before the teleport ends
	 * 
	 * @return The delay before the teleport ends
	 */
	public int getStartDelay() {
		return startDelay;
	}

	/**
	 * The delay before the player can walk after the teleport
	 * 
	 * @return The delay before the player can walk after the teleport
	 */
	public int getEndDelay() {
		return endDelay;
	}

	/**
	 * Gets the start animation for the teleport
	 * 
	 * @return The animation to display at the start of the teleport
	 */
	public int getStartAnimation() {
		return startAnim;
	}

	/**
	 * Gets the end animation for the teleport
	 * 
	 * @return The animation to display at the end of the teleport
	 */
	public int getEndAnimation() {
		return endAnim;
	}

	/**
	 * Gets the start graphic for the teleport
	 * 
	 * @return The graphic to display at the beginning of the teleport
	 */
	public int getStartGraphic() {
		return startGfx;
	}

	/**
	 * Gets the end graphic for the teleport
	 * 
	 * @return The graphic to display at the end of the teleport
	 */
	public int getEndGraphic() {
		return endGfx;
	}

}