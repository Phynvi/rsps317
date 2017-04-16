package com.bclaus.rsps.server.region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Project Insanity - Evolved v.3
 * ObjectManager.java
 */

public class RegionObject {

	private static List<StateObject> stateChanges = new ArrayList<StateObject>();
	private static Map<Integer, CachedObject> cachedObjects = new HashMap<Integer, CachedObject>();
	private static Map<Integer, int[]> objectSizes = new HashMap<Integer, int[]>();
	private static List<VariableObject> varObjects = new ArrayList<VariableObject>();

	public static void appendStateChange(final int objectType, final int objectX, final int objectY, final int objectHeight, final int objectFace, final int objectChangeState, final int objectVType) {
		RegionObject.stateChanges.add(new StateObject(objectType, objectX, objectY, objectFace, objectHeight, objectChangeState, objectVType));
	}

	public static void appendVarObject(final int objectType, final int objectX, final int objectY, final int objectHeight, final int objectFace) {
		RegionObject.varObjects.add(new VariableObject(objectType, objectX, objectY, objectFace, objectHeight));
	}

	public static void changeOrientation(final int objectX, final int objectY, final int objectHeight, final int newO) {
		if (RegionObject.cachedObjects.size() == 0) {
			RegionObject.loadCachedObjects();
		}
		final int key = (objectHeight << 30) + (objectX << 15) + objectY;
		if (RegionObject.cachedObjects.get(key) != null) {
			RegionObject.cachedObjects.get(key).changeOrientation(newO);
		}
	}

	public static int getOrientation(final int objectX, final int objectY, final int objectHeight) {
		if (RegionObject.cachedObjects.size() == 0) {
			RegionObject.loadCachedObjects();
		}
		final int key = (objectHeight << 30) + (objectX << 15) + objectY;
		return RegionObject.cachedObjects.get(key) != null ? RegionObject.cachedObjects.get(key).getOrientation() : 0;
	}

	public static StateObject getStateObject(final int objectX, final int objectY, final int objectHeight, final int objectId) {
		for (final StateObject so : RegionObject.stateChanges) {
			if (so == null) {
				continue;
			}
			if (so.getHeight() != objectHeight) {
				continue;
			}
			if (so.getStatedObject() == objectId && so.getX() == objectX && so.getY() == objectY) {
				return so;
			}
		}
		return null;
	}

	public static int getType(final int objectX, final int objectY, final int objectHeight) {
		if (RegionObject.cachedObjects.size() == 0) {
			RegionObject.loadCachedObjects();
		}
		final int key = (objectHeight << 30) + (objectX << 15) + objectY;
		return RegionObject.cachedObjects.get(key) != null ? RegionObject.cachedObjects.get(key).getType() : 10;
	}

	public static boolean isCachedObject(final int objectX, final int objectY, final int objectHeight, final int objectId) {
		for (final StateObject so : RegionObject.stateChanges) {
			if (so == null) {
				continue;
			}
			if (so.getHeight() != objectHeight) {
				continue;
			}
			if (so.getStatedObject() == objectId && so.getX() == objectX && so.getY() == objectY) {
				return true;
			}
		}
		return false;
	}

	private static void loadCachedObjects() {
		try {
			final java.io.File f = new java.io.File("./data/object_data");
			final java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.FileInputStream(f));
			final int length = (int) f.length() / 8;
			for (int index = 0; index < length; index++) {
				RegionObject.cachedObjects.put(dis.readInt(), new CachedObject(dis.readUnsignedShort(), dis.readByte(), dis.readByte()));
			}
			dis.close();
			int key = (0 << 30) + (2658 << 15) + 2639;
			RegionObject.cachedObjects.put(key, new CachedObject(14315, 10, 0));
			key = (0 << 30) + (3091 << 15) + 3504;
			RegionObject.cachedObjects.put(key, new CachedObject(4388, 10, 0));
			key = (0 << 30) + (3094 << 15) + 3504;
			RegionObject.cachedObjects.put(key, new CachedObject(4408, 10, 0));
			key = (0 << 30) + (3097 << 15) + 3504;
			RegionObject.cachedObjects.put(key, new CachedObject(2387, 10, 0));
			key = (0 << 30) + (3092 << 15) + 3487;
			RegionObject.cachedObjects.put(key, new CachedObject(6552, 10, 0));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadObjectSizes() {
		try {
			final java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.FileInputStream("./data/object_size_config"));
			final int size = (int) (new java.io.File("./data/object_size_config").length() / 2);
			for (int index = 0; index < size; index++) {
				final int bitPart = dis.readShort();
				dis.close();
				RegionObject.objectSizes.put(index, new int[] { bitPart >> 8 & 0xff, bitPart & 0xff });
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean objectExists(final int objectX, final int objectY, final int objectHeight) {
		if (RegionObject.cachedObjects.size() == 0) {
			RegionObject.loadCachedObjects();
		}
		final int key = (objectHeight << 30) + (objectX << 15) + objectY;
		return RegionObject.cachedObjects.get(key) != null;
	}

	public static boolean objectExists(final int objectX, final int objectY, final int objectHeight, final int objectId) {
		if (RegionObject.cachedObjects.size() == 0) {
			RegionObject.loadCachedObjects();
		}
		final int key = (objectHeight << 30) + (objectX << 15) + objectY;
		return RegionObject.cachedObjects.get(key) != null ? RegionObject.cachedObjects.get(key).getId() == objectId : false;
	}

	public static void removeStateChange(final int objectType, final int objectX, final int objectY, final int objectHeight) {
		for (int index = 0; index < RegionObject.stateChanges.size(); index++) {
			final StateObject so = RegionObject.stateChanges.get(index);
			if (so == null) {
				continue;
			}
			if (so.getX() == objectX && so.getY() == objectY && so.getHeight() == objectHeight && so.getType() == objectType || so.getStatedObject() == objectType) {
				RegionObject.stateChanges.remove(index);
				break;
			}
		}
	}

	public static void removeVarObject(final int objectType, final int objectX, final int objectY, final int objectHeight) {
		for (int index = 0; index < RegionObject.varObjects.size(); index++) {
			final VariableObject vo = RegionObject.varObjects.get(index);
			if (vo == null) {
				continue;
			}
			if (vo.getType() == objectType && vo.getX() == objectX && vo.getY() == objectY && vo.getHeight() == objectHeight) {
				RegionObject.varObjects.remove(index);
				break;
			}
		}
	}

	public static boolean stateHasChanged(final int objectType, final int objectX, final int objectY, final int objectHeight) {
		for (final StateObject so : RegionObject.stateChanges) {
			if (so.getHeight() != objectHeight) {
				continue;
			}
			if (so.getX() == objectX && so.getY() == objectY && so.getType() == objectType) {
				return true;
			}
		}
		return false;
	}

	public static boolean varObjectExists(final int objectX, final int objectY, final int objectHeight) {
		for (final VariableObject vo : RegionObject.varObjects) {
			if (vo.getHeight() != objectHeight) {
				continue;
			}
			if (vo.getX() == objectX && vo.getY() == objectY) {
				return true;
			}
		}
		return false;
	}

	public static boolean withinDoorRange(final int objectType, final int objectX, final int objectY, final int playerX, final int playerY, final int atHeight) {
		if (RegionObject.objectSizes.size() == 0) {
			RegionObject.loadObjectSizes();
		}
		final boolean isOpen = RegionObject.isCachedObject(objectX, objectY, atHeight, objectType);
		if (isOpen) {
			final StateObject so = RegionObject.getStateObject(objectX, objectY, atHeight, objectType);
			final int face = so.getFace();
			if (face == 1 || face == 3) {
				return playerX >= objectX - 1 && playerX <= objectX + 1 && playerY == objectY;
			} else {
				return playerY >= objectY - 1 && playerY <= objectY + 1 && playerX == objectX;
			}
		} else {
			final int face = RegionObject.getOrientation(objectX, objectY, atHeight);
			if (face == 1 || face == 3) {
				return playerX >= objectX - 1 && playerX <= objectX + 1 && playerY == objectY;
			} else {
				return playerY >= objectY - 1 && playerY <= objectY + 1 && playerX == objectX;
			}
		}
	}

	public static boolean withinRange(final int objectType, final int objectX, final int objectY, final int playerX, final int playerY, final int atHeight) {
		if (RegionObject.objectSizes.size() == 0) {
			RegionObject.loadObjectSizes();
		}
		int sizeX = 1;
		int sizeY = 1;
		if (RegionObject.objectSizes.get(objectType) != null) {
			sizeX = RegionObject.objectSizes.get(objectType)[0];
			sizeY = RegionObject.objectSizes.get(objectType)[1];
		}
		final int face = RegionObject.getOrientation(objectX, objectY, atHeight);
		if (face == 1 || face == 3) {
			final int tempX = sizeX;
			sizeX = sizeY;
			sizeY = tempX;
		}
		final java.awt.Rectangle objectField = new java.awt.Rectangle(objectX, objectY, sizeX, sizeY);
		final java.awt.Rectangle playerField = new java.awt.Rectangle(objectX - 1, objectY - 1, sizeX + 2, sizeY + 2);
		return playerField.contains(playerX, playerY) && !objectField.contains(playerX, playerY);
	}

}
