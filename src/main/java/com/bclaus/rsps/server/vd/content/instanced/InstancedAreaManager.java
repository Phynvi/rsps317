package com.bclaus.rsps.server.vd.content.instanced;

import java.util.HashMap;
import java.util.Map;

import com.bclaus.rsps.server.vd.player.Boundary;
import com.bclaus.rsps.server.vd.player.Player;
/**
 * A class that manages all {@link InstancedArea} objects created. 
 * 
 * @author Jason MacKeigan
 * @date Jan 28, 2015, 1:07:55 PM
 */
public class InstancedAreaManager {
	
	/**
	 * A single instance of this class for global usage
	 */
	private static final InstancedAreaManager SINGLETON = new InstancedAreaManager();

	/**
	 * The maximum height of any one instance
	 */
	private static final int MAXIMUM_HEIGHT = 4 * 1000;
	
	/**
	 * A mapping of all {@InstancedArea} objects that are being operated on
	 * and are active.
	 */
	private Map<Integer, InstancedArea> active = new HashMap<>();
	
	/**
	 * A private empty {@link InstancedAreaManager} constructor exists to ensure that
	 * no other instance of this class can be created from outside this class.
	 */
	private InstancedAreaManager() { }
	
	/**
	 * Creates a new {@link SingleInstancedArea} object with the given params
	 * @param player	the player for this instanced area
	 * @param boundary	the boundary of the area
	 * @return	null if no height can be found for this area, otherwise the new 
	 * {@link SingleInstancedArea} object will be returned.
	 */
	public InstancedArea createSingleInstancedArea(Player player, Boundary boundary) {
		int height = getNextOpenHeight();
		if (height == -1) {
			return null;
		}
		if(height >= 20) {
			
		}
		SingleInstancedArea area = new SingleInstancedArea(player, boundary, height);
		active.put(height, area);
		return area;
	}
	
	/**
	 * Determines if the {@link InstancedArea} paramater exists within
	 * the mapping of active {@link InstancedArea} objects and can be 
	 * disposed of.
	 *  
	 * @param area	the instanced area 
	 * @return		true if the area exists in the mapping with the same height level
	 * 				and the same reference
	 */
	public boolean disposeOf(InstancedArea area) {
		int height = area.getHeight();
		if(active.isEmpty()) {
			return false;
		}
		if (!active.containsKey(height)) {
			return false;
		}
		InstancedArea found = active.get(height);
		if (!found.equals(area)) {
			return false;
		}
		active.remove(height);
		return true;
	}

	/**
	 * Retrieves an open height level by sifting through the mapping and attempting
	 * to retrive the lowest height level.
	 * @return	
	 * 		the next lowest, open height level will be returned otherwise -1
	 * 		will be returned. When -1 is returned it signifies that there are
	 * 		no heights open from 0 to {@link MAXIMUM_HEIGHT}.
	 */
	private int getNextOpenHeight() {
		for (int height = 0; height < MAXIMUM_HEIGHT; height += 4) {
			if (active.containsKey(height)) {
				continue;
			}
			return height;
		}
		return -1;
	}
	
	/**
	 * Retrieves the single instance of this class
	 * @return	the single instance
	 */
	public static InstancedAreaManager getSingleton() {
		return SINGLETON;
	}

}
