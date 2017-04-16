package com.bclaus.rsps.server.region;

/*
 * Project Insanity - Evolved v.3
 * StateObject.java
 */

public class StateObject {

	private final int objectType;
	private final int objectX;
	private final int objectY;
	private final int objectHeight;
	private final int objectFace;
	private final int objectStateChange;
	private final int objectVType;

	public StateObject(final int objectType, final int objectX, final int objectY, final int objectFace, final int objectHeight, final int objectStateChange, final int objectVType) {
		this.objectType = objectType;
		this.objectX = objectX;
		this.objectY = objectY;
		this.objectFace = objectFace;
		this.objectHeight = objectHeight;
		this.objectStateChange = objectStateChange;
		this.objectVType = objectVType;
	}

	public int getFace() {
		return objectFace;
	}

	public int getHeight() {
		return objectHeight;
	}

	public int getStatedObject() {
		return objectStateChange;
	}

	public int getType() {
		return objectType;
	}

	public int getVType() {
		return objectVType;
	}

	public int getX() {
		return objectX;
	}

	public int getY() {
		return objectY;
	}

}
