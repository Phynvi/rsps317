package com.bclaus.rsps.server.region;

/*
 * Project Insanity - Evolved v.3
 * VariableObject.java
 */

public class VariableObject {

	private final int type;
	private final int x;
	private final int y;
	private final int z;
	private final int face;

	public VariableObject(final int type, final int x, final int y, final int z, final int face) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.face = face;
	}

	public int getFace() {
		return face;
	}

	public int getHeight() {
		return z;
	}

	public int getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
