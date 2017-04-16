/**
 * 
 */
package com.bclaus.rsps.server.vd.world;

/**
 * @author Tim http://rune-server.org/members/Someone
 * 
 */

public class Object {

	public int objectId;
	public int objectX;
	public int objectY;
	public int height;
	public int face;
	public int type;
	public int newId;
	public int tick;
	public boolean remove;

	public Object(int id, int x, int y, int height, int face, int type, int newId, int ticks) {
		this.objectId = id;
		this.objectX = x;
		this.objectY = y;
		this.height = height;
		this.face = face;
		this.type = type;
		this.newId = newId;
		this.tick = ticks;
		ObjectManager.addObject(this);
	}

	public Object(int id, Position p) {
		this(id, p.getX(), p.getY(), p.getZ(), 0, 10, -1, 0);
	}

	public int getNewId() {
		return newId;
	}

	public Position getPosition() {
		return new Position(objectX, objectY, height);
	}
}
