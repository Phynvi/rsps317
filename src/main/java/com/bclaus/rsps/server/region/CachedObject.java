package com.bclaus.rsps.server.region;

/*
 * Project Insanity - Evolved v.3
 * Clicp.java
 */

public class CachedObject {

	private final int objectId;
	private final int objectType;
	private int objectOrientation;

	public CachedObject(final int objectId, final int objectType, final int objectOrientation) {
		this.objectId = objectId;
		this.objectType = objectType;
		this.objectOrientation = objectOrientation;
	}

	public void changeOrientation(final int o) {
		objectOrientation = o;
	}

	public int getId() {
		return objectId;
	}

	public int getOrientation() {
		return objectOrientation;
	}

	public int getType() {
		return objectType;
	}

}
