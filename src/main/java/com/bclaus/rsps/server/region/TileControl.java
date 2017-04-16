/**
 * 
 */
package com.bclaus.rsps.server.region;

import com.bclaus.rsps.server.vd.npc.NPC;
import com.bclaus.rsps.server.vd.player.Player;

/**
 * @author Tim http://rune-server.org/members/Someone
 * 
 */
public class TileControl {

	public static Tile generate(int x, int y, int z) {
		return new Tile(x, y, z);
	}

	public static Tile[] getTiles(NPC n, Player c) {

		int size = 1, tileCount = 0;

		if (n != null && c == null) {
			size = 1;
		}

		Tile[] tiles = new Tile[size * size];

		if (tiles.length == 1) {
			if (n != null && c == null) {
				tiles[0] = generate(n.getX(), n.getY(), n.heightLevel);
			} else if (n == null && c != null) {
				tiles[0] = generate(c.getX(), c.getY(), c.heightLevel);
			}
		} else {
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					if (n != null && c == null) {
						tiles[tileCount++] = generate(n.getX() + x, n.getY() + y, n.heightLevel);
					} else if (n == null && c != null) {
						tiles[tileCount++] = generate(c.getX() + x, c.getY() + y, c.heightLevel);
					}
				}
			}
		}
		return tiles;
	}

	public static Tile[] getTiles(NPC n, Player c, int[] location) {

		int size = 1, tileCount = 0;

		if (n != null && c == null) {
			size = 1;
		}

		Tile[] tiles = new Tile[size * size];

		if (tiles.length == 1)
			tiles[0] = generate(location[0], location[1], location[2]);
		else {
			for (int x = 0; x < size; x++)
				for (int y = 0; y < size; y++)
					tiles[tileCount++] = generate(location[0] + x, location[1] + y, location[2]);
		}
		return tiles;
	}

	public static int calculateDistance(NPC n, Player c, NPC nFol, Player cFol) {

		int[] location = {};
		Tile[] tiles = {};

		if (n != null && c == null) {
			tiles = getTiles(n, null);
			location = currentLocation(n, null);
		} else if (n == null && c != null) {
			tiles = getTiles(null, c);
			location = currentLocation(null, c);
		}

		int[] pointer = new int[tiles.length];

		int lowestCount = 20, count = 0;

		for (Tile newTiles : tiles) {
			if (newTiles.getTile() == location)
				pointer[count++] = 0;
			else
				pointer[count++] = calculateDistance(newTiles, nFol, cFol);
		}
		for (int i = 0; i < pointer.length; i++)
			if (pointer[i] < lowestCount)
				lowestCount = pointer[i];

		return lowestCount;
	}

	public static int calculateDistance(Tile location, NPC n, Player c) {
		int X = 0;
		int Y = 0;

		if (n != null && c == null) {
			X = Math.abs(location.getTile()[0] - n.getX());
			Y = Math.abs(location.getTile()[1] - n.getY());
		} else if (n == null && c != null) {
			X = Math.abs(location.getTile()[0] - c.getX());
			Y = Math.abs(location.getTile()[1] - c.getY());
		}
		return X > Y ? X : Y;
	}

	public static int calculateDistance(int[] location, NPC n, Player c) {
		int X = 0;
		int Y = 0;

		if (n != null && c == null) {
			X = Math.abs(location[0] - n.getX());
			Y = Math.abs(location[1] - n.getY());
		} else if (n == null && c != null) {
			X = Math.abs(location[0] - c.getX());
			Y = Math.abs(location[1] - c.getY());
		}
		return X > Y ? X : Y;
	}

	public static int calculateDistance(int[] location, int[] other) {
		int X = Math.abs(location[0] - other[0]);
		int Y = Math.abs(location[1] - other[1]);
		return X > Y ? X : Y;
	}

	public static int[] currentLocation(NPC n, Player c) {
		int[] currentLocation = new int[3];
		if (n != null || c != null) {
			if (n != null && c == null) {
				currentLocation[0] = n.getX();
				currentLocation[1] = n.getY();
				currentLocation[2] = n.heightLevel;
			} else if (n == null && c != null) {
				currentLocation[0] = c.getX();
				currentLocation[1] = c.getY();
				currentLocation[2] = c.heightLevel;
			}
		}
		return currentLocation;
	}

	public static int[] currentLocation(Tile tileLocation) {

		int[] currentLocation = new int[3];

		if (tileLocation != null) {
			currentLocation[0] = tileLocation.getTile()[0];
			currentLocation[1] = tileLocation.getTile()[1];
			currentLocation[2] = tileLocation.getTile()[2];
		}
		return currentLocation;
	}
}
