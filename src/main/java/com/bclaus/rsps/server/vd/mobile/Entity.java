package com.bclaus.rsps.server.vd.mobile;

public abstract class Entity {
	
	private int index;
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public abstract EntityType getEntityType();

}
