package com.bclaus.rsps.server.vd.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.vd.objects.Objects;
import com.bclaus.rsps.server.vd.player.Player;
import org.omicron.jagex.runescape.CollisionMap;

import com.bclaus.rsps.server.vd.World;

/**
 * @author Sanity
 */

public class ObjectHandler {

	public void createAnObject(Player c, int id, int x, int y) {
		Objects OBJECT = new Objects(id, x, y, 0, 0, 10, 0);
		if (id == -1) {
			removeObject(OBJECT);
		} else {
			addObject(OBJECT);
		}
		Server.objectHandler.placeObject(OBJECT);
	}

	public void createAnObject(Player c, int id, int x, int y, int face) {
		Objects OBJECT = new Objects(id, x, y, 0, face, 10, 0);
		if (id == -1) {
			removeObject(OBJECT);
		} else {
			addObject(OBJECT);
		}
		Server.objectHandler.placeObject(OBJECT);
	}

	public static CopyOnWriteArrayList<Objects> globalObjects = new CopyOnWriteArrayList<Objects>();

	public ObjectHandler() {

		loadGlobalObjects("./data/cfg/global-objects.cfg");
		loadDoorConfig("./data/cfg/doors.cfg");
	}

	/**
	 * Adds object to list
	 */
	public void addObject(Objects object) {
		globalObjects.add(object); // i thi
	}

	/**
	 * Removes object from list
	 */
	public void removeObject(Objects object) {
		globalObjects.remove(object);
	}

	/**
	 * Does object exist
	 */
	public Objects objectExists(int objectX, int objectY, int objectHeight) {
		for (Objects o : globalObjects) {
			if (o.getObjectX() == objectX && o.getObjectY() == objectY && o.getObjectHeight() == objectHeight) {
				return o;
			}
		}
		return null;
	}

	public boolean objectExists(int id, int objectX, int objectY, int objectHeight) {
		for (Objects o : globalObjects)
			if (o.getObjectX() == objectX && o.getObjectY() == objectY && o.getObjectHeight() == objectHeight && o.getObjectId() == id)
				return true;
		return false;
	}

	public Objects fireExists(int id, int objectX, int objectY, int objectHeight) {
		for (Objects o : globalObjects) {
			if (o.objectId == id && o.getObjectX() == objectX && o.getObjectY() == objectY && o.getObjectHeight() == objectHeight) {
				return o;
			}
		}
		return null;
	}

	/**
	 * Update objects when entering a new region or logging in
	 */
	public static void updateObjects(Player c) {
		for (Objects o : globalObjects) {
			if (c != null) {
				if (c.heightLevel == o.getObjectHeight() && o.objectTicks == 0) {
					if (c.distanceToPoint(o.getObjectX(), o.getObjectY()) <= 60) {
						if (o.objectId != -1 && o.objectId != 100 && o.objectId != 2732 && o.objectId != 11406 && o.objectId != 11405 && o.objectId != 11406 && o.objectId != 20000 && o.objectId != 20001 && o.objectId != 2980 && o.objectId != 2981 && o.objectId != 2982 && o.objectId != 2983 && o.objectId != 2984 && o.objectId != 2985 && o.objectId != 2986 && o.objectId != 2987 && o.objectId != 2988) {
							c.getPA().object(o.getObjectId(), o.getObjectX(), o.getObjectY(), o.getObjectFace(), o.getObjectType());
						}
					}
				}
			}
		}
	}

	/**
	 * Creates the object for anyone who is within 60 squares of the object
	 */
	public void placeObject(Objects o) {
		ArrayList<Objects> toremove = new ArrayList<Objects>();
		for (Objects s : globalObjects) {
			if (s.getObjectX() == o.getObjectX() && s.getObjectY() == o.getObjectY()) {
				toremove.add(s);
			}
		}
		for (Objects s : toremove) {
			if (globalObjects.contains(s)) {
				globalObjects.remove(s);
			}
		}
		globalObjects.add(o);
		for (Player player : World.PLAYERS) {
			if (player == null) {
				continue;
			}
			if (player.heightLevel == o.getObjectHeight() && o.objectTicks == 0) {
				if (player.distanceToPoint(o.getObjectX(), o.getObjectY()) <= 60) {
					player.getPA().object(o.getObjectId(), o.getObjectX(), o.getObjectY(), o.getObjectFace(), o.getObjectType());
				}
			}
		}
	}

	public void process() {
		/*
		 * for (Objects globalObject : globalObjects) { if (globalObject !=
		 * null) { if (globalObject.objectTicks > 0) {
		 * globalObject.objectTicks--; } if (globalObject.objectTicks == 1) {
		 * Objects deleteObject = objectExists( globalObject.getObjectX(),
		 * globalObject.getObjectY(), globalObject.getObjectHeight());
		 * removeObject(deleteObject); globalObject.objectTicks = 0;
		 * placeObject(globalObject); removeObject(globalObject); if
		 * (isObelisk(globalObject.objectId)) { int index =
		 * getObeliskIndex(globalObject.objectId); if (activated[index]) {
		 * activated[index] = false; teleportObelisk(index); } } } }
		 * 
		 * }
		 */
	}

	public boolean loadGlobalObjects(String fileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader objectFile = null;
		try {
			objectFile = new BufferedReader(new FileReader("./" + fileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(fileName + ": file not found.");
			return false;
		}
		try {
			line = objectFile.readLine();
		} catch (IOException ioexception) {
			Misc.println(fileName + ": error loading file.");
			// return false;
		}
		while (!EndOfFile && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("object")) {
					Objects object = new Objects(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]), Integer.parseInt(token3[3]), Integer.parseInt(token3[4]), Integer.parseInt(token3[5]), 0);
					addObject(object);
				}
			} else {
				if (line.equals("[ENDOFOBJECTLIST]")) {
					try {
						objectFile.close();
					} catch (IOException ignored) {
					}
				}
			}
			try {
				line = objectFile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			objectFile.close();
		} catch (IOException ignored) {
		}
		return false;
	}

	/**
	 * Doors
	 */

	public static final int MAX_DOORS = 100;
	public static int[][] doors = new int[MAX_DOORS][5];
	public static int doorFace = 0;

	public void doorHandling(int doorId, int doorX, int doorY, int doorHeight) {
		for (int[] door : doors) {
			if (doorX == door[0] && doorY == door[1] && doorHeight == door[2]) {
				if (door[4] == 0) {
					doorId++;
				} else {
					doorId--;
				}
				for (Player p : World.PLAYERS) {
					if (p != null) {
						Player person = p;
						if (person != null) {
							if (person.heightLevel == doorHeight) {
								if (person.distanceToPoint(doorX, doorY) <= 60) {
									person.getPA().object(-1, door[0], door[1], 0, 0);
									CollisionMap.setFlag(0, door[0], door[1], 0);
									CollisionMap.setFlag(0, door[0], door[1] - 1, 0);
									CollisionMap.setFlag(0, door[0] - 1, door[1], 0);
									CollisionMap.setFlag(0, door[0], door[1] + 1, 0);
									CollisionMap.setFlag(0, door[0] + 1, door[1], 0);
									if (door[3] == 0 && door[4] == 1) {
										person.getPA().object(doorId, door[0], door[1] + 1, -1, 0);
									} else if (door[3] == -1 && door[4] == 1) {
										person.getPA().object(doorId, door[0] - 1, door[1], -2, 0);
									} else if (door[3] == -2 && door[4] == 1) {
										person.getPA().object(doorId, door[0], door[1] - 1, -3, 0);
									} else if (door[3] == -3 && door[4] == 1) {
										person.getPA().object(doorId, door[0] + 1, door[1], 0, 0);
									} else if (door[3] == 0 && door[4] == 0) {
										person.getPA().object(doorId, door[0] - 1, door[1], -3, 0);
									} else if (door[3] == -1 && door[4] == 0) {
										person.getPA().object(doorId, door[0], door[1] - 1, 0, 0);
									} else if (door[3] == -2 && door[4] == 0) {
										person.getPA().object(doorId, door[0] + 1, door[1], -1, 0);
									} else if (door[3] == -3 && door[4] == 0) {
										person.getPA().object(doorId, door[0], door[1] + 1, -2, 0);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public boolean loadDoorConfig(String fileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader objectFile = null;
		try {
			objectFile = new BufferedReader(new FileReader("./" + fileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(fileName + ": file not found.");
			return false;
		}
		try {
			line = objectFile.readLine();
		} catch (IOException ioexception) {
			Misc.println(fileName + ": error loading file.");
			// return false; Memory leak
		}
		int door = 0;
		while (!EndOfFile && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("door")) {
					doors[door][0] = Integer.parseInt(token3[0]);
					doors[door][1] = Integer.parseInt(token3[1]);
					doors[door][2] = Integer.parseInt(token3[2]);
					doors[door][3] = Integer.parseInt(token3[3]);
					doors[door][4] = Integer.parseInt(token3[4]);
					door++;
				}
			} else {
				if (line.equals("[ENDOFDOORLIST]")) {
					try {
						objectFile.close();
					} catch (IOException ignored) {
					}
				}
			}
			try {
				line = objectFile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			objectFile.close();
		} catch (IOException ignored) {
		}
		return false;
	}

	public final int IN_USE_ID = 14825;

}
