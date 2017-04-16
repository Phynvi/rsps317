package com.bclaus.rsps.server.region;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import com.bclaus.rsps.server.Server;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.npc.NPC;

/*
 * Project Insanity - Evolved v.3
 * Region.java
 */

public class Region {
	
	private static final Logger logger = Logger.getLogger(Region.class.getName());
	
	/**
	 * A map containing each region as the key, and a Collection of
	 * real world objects as the value. 
	 */
	private static HashMap<Integer, ArrayList<WorldObject>> worldObjects = new HashMap<>();
	
	/**
	 * Determines if an object is real or not. If the Collection of regions
	 * and real objects contains the properties passed in the parameters then
	 * the object will be determined real
	 * @param id	the id of the object
	 * @param x		the x coordinate of the object
	 * @param y		the y coordinate of the object
	 * @param height	the height of the object
	 * @return
	 */
	public static boolean isWorldObject(int id, int x, int y, int height) {
		Region region = getRegion(x, y);
		if (Objects.isNull(region)) {
			return true;
		}
		Collection<WorldObject> regionObjects = worldObjects.get(region.id);
		if (Objects.isNull(regionObjects)) {
			return true;
		}
		Optional<WorldObject> exists = regionObjects.stream().
				filter(object -> object.id == id && object.x == x && object.y == y
						&& object.height == height).findAny();
		return exists.isPresent();
	}
	
	/**
	 * Adds a {@link WorldObject} to the {@link worldObjects} map based on the
	 * x, y, height, and identification of the object.
	 * @param id		the id of the object
	 * @param x			the x position of the object
	 * @param y			the y position of the object
	 * @param height	the height of the object
	 */
	public static void addWorldObject(int id, int x, int y, int height) {
		Region region = getRegion(x, y);
		if (Objects.isNull(region)) {
			return;
		}
		int regionId = region.id;
		if (worldObjects.containsKey(regionId)) {
			ArrayList<WorldObject> objects = worldObjects.get(regionId);
			if (objects.stream().anyMatch(object -> object.getId() == id 
					&& object.getX() == x && object.getY() == y && object.getHeight() == height)) {
				return;
			}
			worldObjects.get(regionId).add(new WorldObject(id, x, y, height));
		} else {
			ArrayList<WorldObject> object = new ArrayList<>();
			object.add(new WorldObject(id, x, y, height));
			worldObjects.put(regionId, object);
		}
	}

	public Region() {

	}

	public static boolean pathBlocked(NPC nAtk, Player cAtk, NPC nVic, Player cVic) {

		double offsetX = 0;
		double offsetY = 0;
		if (nAtk != null && cAtk == null) {
			if (nVic != null && cVic == null) {
				offsetX = Math.abs(nAtk.getX() - nVic.getX());
				offsetY = Math.abs(nAtk.getY() - nVic.getY());
			} else if (nVic == null && cVic != null) {
				offsetX = Math.abs(nAtk.getX() - cVic.getX());
				offsetY = Math.abs(nAtk.getY() - cVic.getY());
			}
		} else if (nAtk == null && cAtk != null) {
			if (nVic != null && cVic == null) {
				offsetX = Math.abs(cAtk.getX() - nVic.getX());
				offsetY = Math.abs(cAtk.getY() - nVic.getY());
			} else if (nVic == null && cVic != null) {
				offsetX = Math.abs(cAtk.getX() - cVic.getX());
				offsetY = Math.abs(cAtk.getY() - cVic.getY());
			}
		}

		int distance = TileControl.calculateDistance(nAtk, cAtk, nVic, cVic);

		if (distance == 0) {
			return true;
		}

		offsetX = offsetX > 0 ? offsetX / distance : 0;
		offsetY = offsetY > 0 ? offsetY / distance : 0;

		int[][] path = new int[distance][5];

		int curX = 0;
		int curY = 0;
		if (nAtk != null && cAtk == null) {
			curX = nAtk.getX();
			curY = nAtk.getY();
		} else if (nAtk == null && cAtk != null) {
			curX = cAtk.getX();
			curY = cAtk.getY();
		}
		int next = 0;
		int nextMoveX = 0;
		int nextMoveY = 0;

		double currentTileXCount = 0.0;
		double currentTileYCount = 0.0;

		while (distance > 0) {
			distance--;
			nextMoveX = 0;
			nextMoveY = 0;
			if (nVic != null && cVic == null) {
				if (curX > nVic.getX()) {
					currentTileXCount += offsetX;
					if (currentTileXCount >= 1.0) {
						nextMoveX--;
						curX--;
						currentTileXCount -= offsetX;
					}
				} else if (curX < nVic.getX()) {
					currentTileXCount += offsetX;
					if (currentTileXCount >= 1.0) {
						nextMoveX++;
						curX++;
						currentTileXCount -= offsetX;
					}
				}
				if (curY > nVic.getY()) {
					currentTileYCount += offsetY;
					if (currentTileYCount >= 1.0) {
						nextMoveY--;
						curY--;
						currentTileYCount -= offsetY;
					}
				} else if (curY < nVic.getY()) {
					currentTileYCount += offsetY;
					if (currentTileYCount >= 1.0) {
						nextMoveY++;
						curY++;
						currentTileYCount -= offsetY;
					}
				}
			} else if (nVic == null && cVic != null) {
				if (curX > cVic.getX()) {
					currentTileXCount += offsetX;
					if (currentTileXCount >= 1.0) {
						nextMoveX--;
						curX--;
						currentTileXCount -= offsetX;
					}
				} else if (curX < cVic.getX()) {
					currentTileXCount += offsetX;
					if (currentTileXCount >= 1.0) {
						nextMoveX++;
						curX++;
						currentTileXCount -= offsetX;
					}
				}
				if (curY > cVic.getY()) {
					currentTileYCount += offsetY;
					if (currentTileYCount >= 1.0) {
						nextMoveY--;
						curY--;
						currentTileYCount -= offsetY;
					}
				} else if (curY < cVic.getY()) {
					currentTileYCount += offsetY;
					if (currentTileYCount >= 1.0) {
						nextMoveY++;
						curY++;
						currentTileYCount -= offsetY;
					}
				}
			}
			path[next][0] = curX;
			path[next][1] = curY;
			if (nAtk != null && cAtk == null) {
				path[next][2] = nAtk.heightLevel;
			} else if (nAtk == null && cAtk != null) {
				path[next][2] = cAtk.heightLevel;
			}
			path[next][3] = nextMoveX;
			path[next][4] = nextMoveY;
			next++;
		}
		for (int i = 0; i < path.length; i++) {
			if (!Region.getClipping(path[i][0], path[i][1], path[i][2], path[i][3], path[i][4])) {
				return true;
			}
		}
		return false;
	}

	private void addClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips[height] == null) {
			clips[height] = new int[64][64];
		}
		clips[height][x - regionAbsX][y - regionAbsY] |= shift;
	}

	private int getClip(int x, int y, int height) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips[height] == null) {
			return 0;
		}
		return clips[height][x - regionAbsX][y - regionAbsY];
	}

	public static boolean canAttack(Player a, NPC b) {
		return canMove(a.getX(), a.getY(), b.getX(), b.getY(), a.heightLevel % 4, 1, 1);
	}

	public static boolean canAttackPlayer(Player a, Player b) {
		return canMove(a.getX(), a.getY(), b.getX(), b.getY(), a.heightLevel % 4, 1, 1);
	}

	public static boolean pathBlocked(Player entity, int dirX, int dirY) {
		for (int x = entity.getX(); x < entity.getX() + 1; x++) {
			for (int y = entity.getY(); y < entity.getY() + 1; y++) {
				if (!Region.getClipping(x, y, entity.heightLevel, dirX, dirY)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean canMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
		int diffX = endX - startX;
		int diffY = endY - startY;
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int ii = 0; ii < max; ii++) {
			int currentX = endX - diffX;
			int currentY = endY - diffY;
			for (int i = 0; i < xLength; i++) {
				for (int i2 = 0; i2 < yLength; i2++)
					if (diffX < 0 && diffY < 0) {
						if ((getClipping((currentX + i) - 1, (currentY + i2) - 1, height) & 0x128010e) != 0 || (getClipping((currentX + i) - 1, currentY + i2, height) & 0x1280108) != 0 || (getClipping(currentX + i, (currentY + i2) - 1, height) & 0x1280102) != 0)
							return false;
					} else if (diffX > 0 && diffY > 0) {
						if ((getClipping(currentX + i + 1, currentY + i2 + 1, height) & 0x12801e0) != 0 || (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0 || (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0)
							return false;
					} else if (diffX < 0 && diffY > 0) {
						if ((getClipping((currentX + i) - 1, currentY + i2 + 1, height) & 0x1280138) != 0 || (getClipping((currentX + i) - 1, currentY + i2, height) & 0x1280108) != 0 || (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0)
							return false;
					} else if (diffX > 0 && diffY < 0) {
						if ((getClipping(currentX + i + 1, (currentY + i2) - 1, height) & 0x1280183) != 0 || (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0 || (getClipping(currentX + i, (currentY + i2) - 1, height) & 0x1280102) != 0)
							return false;
					} else if (diffX > 0 && diffY == 0) {
						if ((getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0)
							return false;
					} else if (diffX < 0 && diffY == 0) {
						if ((getClipping((currentX + i) - 1, currentY + i2, height) & 0x1280108) != 0)
							return false;
					} else if (diffX == 0 && diffY > 0) {
						if ((getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0)
							return false;
					} else if (diffX == 0 && diffY < 0 && (getClipping(currentX + i, (currentY + i2) - 1, height) & 0x1280102) != 0)
						return false;

			}

			if (diffX < 0)
				diffX++;
			else if (diffX > 0)
				diffX--;
			if (diffY < 0)
				diffY++;
			else if (diffY > 0)
				diffY--;
		}

		return true;
	}

	private static void addClipping(int x, int y, int height, int shift) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		if (regionIdTable[regionId] != null) {
			regionIdTable[regionId].addClip(x, y, height, shift);
			return;
		}
	}

	public static Region[] regionIdTable;

	public static void buildTable(int count) {
		regionIdTable = new Region[count];
		for (Region r : regions) {
			regionIdTable[r.id()] = r;
		}
	}

	private static Region[] regions;
	private int id;
	private int[][][] clips = new int[4][][];
	private boolean members = false;
	private RSObject[][][] objects;

	public Region(int id, boolean members) {
		this.id = id;
		this.members = members;
		objects = new RSObject[4][][];
	}

	public int id() {
		return id;
	}

	public boolean members() {
		return members;
	}

	public static boolean isMembers(int x, int y, int height) {
		if (x >= 3272 && x <= 3320 && y >= 2752 && y <= 2809)
			return false;
		if (x >= 2640 && x <= 2677 && y >= 2638 && y <= 2679)
			return false;
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		if (regionIdTable[regionId] != null) {
			return regionIdTable[regionId].members();
		}
		return false;
	}

	public static boolean blockedShot(int x, int y, int z) {
		return (getClipping(x, y, z) & 0x20000) == 0;
	}

	private static void addClippingForVariableObject(int x, int y, int height, int type, int direction, boolean flag) {
		if (type == 0) {
			if (direction == 0) {
				addClipping(x, y, height, 128 + (flag ? 0x20000 : 0));
				addClipping(x - 1, y, height, 8 + (flag ? 0x20000 : 0));
			} else if (direction == 1) {
				addClipping(x, y, height, 2 + (flag ? 0x20000 : 0));
				addClipping(x, y + 1, height, 32 + (flag ? 0x20000 : 0));
			} else if (direction == 2) {
				addClipping(x, y, height, 8 + (flag ? 0x20000 : 0));
				addClipping(x + 1, y, height, 128 + (flag ? 0x20000 : 0));
			} else if (direction == 3) {
				addClipping(x, y, height, 32 + (flag ? 0x20000 : 0));
				addClipping(x, y - 1, height, 2 + (flag ? 0x20000 : 0));
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				addClipping(x, y, height, 1);
				addClipping(x - 1, y, height, 16);
			} else if (direction == 1) {
				addClipping(x, y, height, 4);
				addClipping(x + 1, y + 1, height, 64);
			} else if (direction == 2) {
				addClipping(x, y, height, 16);
				addClipping(x + 1, y - 1, height, 1);
			} else if (direction == 3) {
				addClipping(x, y, height, 64);
				addClipping(x - 1, y - 1, height, 4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				addClipping(x, y, height, 130);
				addClipping(x - 1, y, height, 8);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 1) {
				addClipping(x, y, height, 10);
				addClipping(x, y + 1, height, 32);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 2) {
				addClipping(x, y, height, 40);
				addClipping(x + 1, y, height, 128);
				addClipping(x, y - 1, height, 2);
			} else if (direction == 3) {
				addClipping(x, y, height, 160);
				addClipping(x, y - 1, height, 2);
				addClipping(x - 1, y, height, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addClipping(x, y, height, 65536);
					addClipping(x - 1, y, height, 4096);
				} else if (direction == 1) {
					addClipping(x, y, height, 1024);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 2) {
					addClipping(x, y, height, 4096);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 3) {
					addClipping(x, y, height, 16384);
					addClipping(x, y - 1, height, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addClipping(x, y, height, 512);
					addClipping(x - 1, y + 1, height, 8192);
				} else if (direction == 1) {
					addClipping(x, y, height, 2048);
					addClipping(x + 1, y + 1, height, 32768);
				} else if (direction == 2) {
					addClipping(x, y, height, 8192);
					addClipping(x + 1, y + 1, height, 512);
				} else if (direction == 3) {
					addClipping(x, y, height, 32768);
					addClipping(x - 1, y - 1, height, 2048);
				}
			} else if (type == 2) {
				if (direction == 0) {
					addClipping(x, y, height, 66560);
					addClipping(x - 1, y, height, 4096);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 1) {
					addClipping(x, y, height, 5120);
					addClipping(x, y + 1, height, 16384);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 2) {
					addClipping(x, y, height, 20480);
					addClipping(x + 1, y, height, 65536);
					addClipping(x, y - 1, height, 1024);
				} else if (direction == 3) {
					addClipping(x, y, height, 81920);
					addClipping(x, y - 1, height, 1024);
					addClipping(x - 1, y, height, 4096);
				}
			}
		}
	}

	private static void addClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag) {
		int clipping = 256;
		if (flag) {
			clipping += 0x20000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				addClipping(i, i2, height, clipping);
			}
		}
	}

	public static void addObject(int objectId, int x, int y, int height, int type, int direction) {
		ObjectDef def = ObjectDef.getObjectDef(objectId);
		if (type < 4)
			def.setSolid(objectId);
		if (def == null) {
			return;
		}
		int xLength;
		int yLength;
		if (direction != 1 && direction != 3) {
			xLength = def.xLength();
			yLength = def.yLength();
		} else {
			xLength = def.yLength();
			yLength = def.xLength();
		}
		if (type == 22) {
			if (def.hasActions() && def.aBoolean767()) {
				addClipping(x, y, height, 0x200000);
			}
		} else if (type >= 9) {
			if (def.aBoolean767()) {
				addClippingForSolidObject(x, y, height, xLength, yLength, def.solid());
			}
		} else if (type >= 0 && type <= 3) {
			if (def.aBoolean767()) {
				addClippingForVariableObject(x, y, height, type, direction, def.solid());
			}
		}
	}

	public static int getClipping(int x, int y, int height) {
		try {
			if (height > 3)
				height = 0;
			int regionX = x >> 3;
			int regionY = y >> 3;
			int regionId = ((regionX / 8) << 8) + (regionY / 8);
			if (regionIdTable[regionId] != null) {
				return regionIdTable[regionId].getClip(x, y, height);
			}
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	public static boolean getClipping(int x, int y, int height, int moveTypeX, int moveTypeY) {
		try {
			if (height > 3)
				height = 0;
			int checkX = (x + moveTypeX);
			int checkY = (y + moveTypeY);
			if (moveTypeX == -1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280108) == 0;
			else if (moveTypeX == 1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280180) == 0;
			else if (moveTypeX == 0 && moveTypeY == -1)
				return (getClipping(x, y, height) & 0x1280102) == 0;
			else if (moveTypeX == 0 && moveTypeY == 1)
				return (getClipping(x, y, height) & 0x1280120) == 0;
			else if (moveTypeX == -1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x128010e) == 0 && (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0 && (getClipping(checkX - 1, checkY, height) & 0x1280102) == 0);
			else if (moveTypeX == 1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x1280183) == 0 && (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0 && (getClipping(checkX, checkY - 1, height) & 0x1280102) == 0);
			else if (moveTypeX == -1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x1280138) == 0 && (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0 && (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0);
			else if (moveTypeX == 1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x12801e0) == 0 && (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0 && (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0);
			else {

				return false;
			}
		} catch (Exception e) {
			
			return true;
		}
	}

	public static boolean isBlocked(int x, int y, int z, Direction direction) {
		final int size = 0;
		switch (direction) {
		case NORTH:
			return (getClipping(x, y + size, z) & direction.getClipMask()) != 0;
		case EAST:
			return (getClipping(x + size, y, z) & direction.getClipMask()) != 0;
		case SOUTH:
			return (getClipping(x, y - size, z) & direction.getClipMask()) != 0;
		case WEST:
			return (getClipping(x - size, y, z) & direction.getClipMask()) != 0;
		case NORTH_EAST:
			return (getClipping(x + size, y + size, z) & direction.getClipMask()) != 0;
		case NORTH_WEST:
			return (getClipping(x - size, y + size, z) & direction.getClipMask()) != 0;
		case SOUTH_EAST:
			return (getClipping(x + size, y - size, z) & direction.getClipMask()) != 0;
		case SOUTH_WEST:
			return (getClipping(x - size, y - size, z) & direction.getClipMask()) != 0;
		default:
			return true;
		}
	}

	public static Region[] getRegions() {
		return regions;
	}

	public static void setRegions(Region[] set) {
		regions = set;
	}

	public static void load() {
		try {
			long start = System.currentTimeMillis();
			File f = new File("./data/world/map_index");
			byte[] buffer = new byte[(int) f.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			ByteStream in = new ByteStream(buffer);
			int size = in.length() / 6;
			Region.setRegions(new Region[size]);
			int[] regionIds = new int[size];
			int[] mapGroundFileIds = new int[size];
			int[] mapObjectsFileIds = new int[size];
			for (int i = 0; i < size; i++) {
				regionIds[i] = in.getUShort();
				mapGroundFileIds[i] = in.getUShort();
				mapObjectsFileIds[i] = in.getUShort();
			}
			int highest = 0;
			for (int i = 0; i < size; i++) {
				Region.getRegions()[i] = new Region(regionIds[i], true);
				if (regionIds[i] > highest)
					highest = regionIds[i];
			}
			buildTable(highest + 1);
			for (int i = 0; i < size; i++) {
				byte[] file1 = getBuffer(new File("./data/world/map/" + mapObjectsFileIds[i] + ".gz"));
				byte[] file2 = getBuffer(new File("./data/world/map/" + mapGroundFileIds[i] + ".gz"));
				if (file1 == null || file2 == null) {
					continue;
				}
				if (mapObjectsFileIds[i] == 1963) {
					continue;// XXX: does not load
				}
				try {
					loadMaps(regionIds[i], new ByteStream(file1), new ByteStream(file2));
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("Error loading map region: " + regionIds[i] + ", ids: " + mapObjectsFileIds[i] + " and " + mapGroundFileIds[i]);
				}
			}
			System.out.println("Maps took "+TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start) + " to start.");
			logger.info("Loaded " + size + " Maps.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Replaces an object's id with another.
	 * 
	 * @param id
	 *            the object id.
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the z coordinate.
	 */
	public void replaceObject(int objectId, int x, int y, int z) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;

		if (z > 3) {
			z = z % 4;
		}

		if (objects[z] == null) {
			return;
		}

		if (objects[z][x - regionAbsX][y - regionAbsY] == null) {
			return;
		}

		objects[z][x - regionAbsX][y - regionAbsY].setId(objectId);
	}

	/**
	 * Fetches a region after they are sorted by id.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @return the region.
	 */
	/*public static Region getRegion(int x, int y) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		System.out.println(x + ":" + y + ":" + regionX + ":" + regionY + ":" + regionId);
		if (regionId > regionIdTable.length - 1) {
			return null;
		}

		if (regionId < 0) {
			System.out.println("FATAL CLIPPING ERROR: regionId < 0");
			System.exit(0);
		}

		System.out.println("Region: " + regions[regionId]);

		if (regionIdTable[regionId] == null) {
			return null;
		}

		return regionIdTable[regionId];
	}*/
	
	public static Region getRegion(int x, int y) {
		if(Server.localHost) {
			return null;
		}
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;
		for (Region region : regions) {
			if (region.id() == regionId) {
				return region;
			}
		}
		return null;
	}

	public static RSObject getObject(int x, int y, int z) {
		Region region = getRegion(x, y);

		if (region == null) {
			return null;
		}

		int regionAbsX = (region.id >> 8) << 6;
		int regionAbsY = (region.id & 0xff) << 6;

		if (z > 3) {
			z = z % 4;
		}

		if (region.objects[z] == null) {
			return null;
		}

		return region.objects[z][x - regionAbsX][y - regionAbsY];
	}

	/**
	 * Tells you whether an object exists at this point in time.
	 * 
	 * @param id
	 *            the object id.
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the z coordinate.
	 * @return whether the object exists
	 */
	public boolean objectExists(int objectId, int x, int y, int z) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;

		if (z > 3) {
			z = z % 4;
		}

		if (objects[z] == null) {
			return false;
		}

		return objects[z][x - regionAbsX][y - regionAbsY] != null && objects[z][x - regionAbsX][y - regionAbsY].getId() == objectId;
	}

	/**
	 * Adds an object to this region.
	 * 
	 * @param object
	 *            the object.
	 */
	public void addObject(RSObject object) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;

		int z = object.getZ();

		if (z > 3) {
			z = z % 4;
		}

		if (objects[z] == null) {
			objects[z] = new RSObject[64][64];
		}

		objects[z][object.getX() - regionAbsX][object.getY() - regionAbsY] = object;
	}

	public void removeObject(RSObject object) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;

		int z = object.getZ();

		if (z > 3) {
			z = z % 4;
		}

		if (objects[z] == null) {
			return;
		}

		objects[z][object.getX() - regionAbsX][object.getY() - regionAbsY] = null;
	}

	public static byte[] getBuffer(File f) throws Exception {
		if (!f.exists()) {
			return null;
		}
		byte[] buffer = new byte[(int) f.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(f));
		dis.readFully(buffer);
		dis.close();
		byte[] gzipInputBuffer = new byte[999999];
		int bufferlength = 0;
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(buffer));
		do {
			if (bufferlength == gzipInputBuffer.length) {
				System.out.println("Error inflating data.\nGZIP buffer overflow.");
				break;
			}
			int readByte = gzip.read(gzipInputBuffer, bufferlength, gzipInputBuffer.length - bufferlength);
			if (readByte == -1) {
				break;
			}
			bufferlength += readByte;
		} while (true);
		byte[] inflated = new byte[bufferlength];
		System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferlength);
		buffer = inflated;
		if (buffer.length < 10) {
			return null;
		}
		return buffer;
	}

	private static void loadMaps(int regionId, ByteStream str1, ByteStream str2) {
		int absX = (regionId >> 8) * 64;
		int absY = (regionId & 0xff) * 64;
		int[][][] someArray = new int[4][64][64];
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					while (true) {
						int v = str2.getUByte();
						if (v == 0) {
							break;
						} else if (v == 1) {
							str2.skip(1);
							break;
						} else if (v <= 49) {
							str2.skip(1);
						} else if (v <= 81) {
							someArray[i][i2][i3] = v - 49;
						}
					}
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					if ((someArray[i][i2][i3] & 1) == 1) {
						int height = i;
						if ((someArray[1][i2][i3] & 2) == 2) {
							height--;
						}
						if (height >= 0 && height <= 3) {
							addClipping(absX + i2, absY + i3, height, 0x200000);
						}
					}
				}
			}
		}
		int objectId = -1;
		int incr;
		while ((incr = str1.getUSmart()) != 0) {
			objectId += incr;
			int location = 0;
			int incr2;
			while ((incr2 = str1.getUSmart()) != 0) {
				location += incr2 - 1;
				int localX = (location >> 6 & 0x3f);
				int localY = (location & 0x3f);
				int height = location >> 12;
				int objectData = str1.getUByte();
				int type = objectData >> 2;
				int direction = objectData & 0x3;
				if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
					continue;
				}
				if ((someArray[1][localX][localY] & 2) == 2) {
					height--;
				}
				if (height >= 0 && height <= 3) {
					addObject(objectId, absX + localX, absY + localY, height, type, direction);
					addWorldObject(objectId, absX + localX, absY + localY, height);
				}
			}
		}
	}

	public static final byte[] ReadFile(String s) {
		try {
			File file = new File(s);
			int i = (int) file.length();
			byte abyte0[] = new byte[i];
			DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(s)));
			datainputstream.readFully(abyte0, 0, i);
			datainputstream.close();
			return abyte0;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public static boolean canAttack(Player a, Player b) {
		return canMove(a.getX(), a.getY(), b.getX(), b.getY(), a.heightLevel % 4, 1, 1);
	}

	public static boolean canAttack(NPC npc, Player p) {
		return canMove(npc.getX(), npc.getY(), p.getX(), p.getY(), npc.heightLevel % 4, npc.getSize(), npc.getSize());
	}

	public static boolean canAttackNPC(Player a, NPC npc) {
		return canMove(a.getX(), a.getY(), npc.getX(), npc.getY(), a.heightLevel % 4, 1, 1);
	}

}
