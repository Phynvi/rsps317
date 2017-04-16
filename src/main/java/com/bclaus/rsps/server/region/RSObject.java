/**
 * 
 */
package com.bclaus.rsps.server.region;

/**
 * @author Tim http://rune-server.org/members/Someone
 *
 */
public class RSObject {

	private int x;
	private int y;
	private int z;
	private int id;
	private final int masterId;
	private int type;
	private int direction;

	public RSObject(int x, int y, int z, int id, int type, int direction) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
		masterId = id;
		this.type = type;
		this.direction = direction;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMasterId() {
		return masterId;
	}

	public int getType() {
		return type;
	}

	public int getFace() {
		return direction;
	}
}
