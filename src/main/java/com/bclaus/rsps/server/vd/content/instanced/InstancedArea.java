package com.bclaus.rsps.server.vd.content.instanced;

import com.bclaus.rsps.server.vd.player.Boundary;

public abstract class InstancedArea {
	
	/**
	 * The boundary or location for this instanced area
	 */
	protected Boundary boundary;
	
	/**
	 * The height of this area
	 */
	protected int height;
	
	/**
	 * Creates a new area with a boundary
	 * @param boundary	the boundary or location
	 * @param height	the height of the area
	 */
	public InstancedArea(Boundary boundary, int height) {
		this.boundary = boundary;
		this.height = height;
	}
	
	/**
	 * Determines the height of this area
	 * @return	the height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * The boundary or location of this instanced area
	 * @return	the boundary
	 */
	public Boundary getBoundary() {
		return boundary;
	}

}
