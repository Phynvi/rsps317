package com.bclaus.rsps.server.vd.objects;

/**
 * Represents a single game object
 * 
 * @author Faris
 * 
 */
public class GameObject {

	/**
	 * The id of the object
	 */
	private final int objectId;

	/**
	 * The replacement id of the object
	 */
	private int replacementId;

	/**
	 * How long the object will exist
	 */
	private int lifeCycle;

	private boolean expireable;

	public GameObject(int objectId) {
		this.objectId = objectId;
		this.expireable = false;
	}

	/**
	 * Constructs a new game object
	 * 
	 * @param location
	 * @param objectId
	 * @param replacementId
	 */
	public GameObject(int objectId, int replacementId, int lifeCycle) {
		this.objectId = objectId;
		this.replacementId = replacementId;
		this.lifeCycle = lifeCycle;
		this.expireable = true;
	}

	/**
	 * Returns the objects id
	 * 
	 * @return
	 */
	public int getObjectId() {
		return objectId;
	}

	/**
	 * Returns the replacement object id
	 * 
	 * @return
	 */
	public int getReplacementId() {
		return replacementId;
	}

	/**
	 * Returns how long the object will exist
	 * 
	 * @return
	 */
	public int getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(int cycle) {
		this.lifeCycle = cycle;
	}

	public boolean isExpireable() {
		return expireable;
	}

}
