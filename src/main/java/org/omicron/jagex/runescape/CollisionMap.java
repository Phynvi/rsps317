package org.omicron.jagex.runescape;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class CollisionMap {
    public static final byte NORTH_WEST_BLOCKED = 1;
    public static final byte NORTH_BLOCKED = 2;
    public static final byte NORTH_EAST_BLOCKED = 4;
    public static final byte EAST_BLOCKED = 8;
    public static final byte SOUTH_EAST_BLOCKED = 16;
    public static final byte SOUTH_BLOCKED = 32;
    public static final byte SOUTH_WEST_BLOCKED = 64;
    public static final byte WEST_BLOCKED = -128;
    public static final byte COMPLETELY_BLOCKED = -1;
    private static final byte[][][] collisionFlags = new byte[4][][];

    public static void load(String file) throws IOException {
        DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
        for (int z = 0; z < collisionFlags.length; ++z) {
            int xSize = inputStream.readUnsignedShort();
            CollisionMap.collisionFlags[z] = new byte[xSize][];
            for (int x = 0; x < xSize; ++x) {
                int ySize = inputStream.readUnsignedShort();
                CollisionMap.collisionFlags[z][x] = new byte[ySize];
                inputStream.readFully(collisionFlags[z][x]);
                if (ySize != 0) continue;
                CollisionMap.collisionFlags[z][x] = new byte[10000];
            }
        }
    }

    public static void setFlag(int z, int x, int y, int mask) {
        CollisionMap.collisionFlags[z][x][y] = (byte)mask;
    }

    private static int getFlag(int z, int x, int y) {
        if (z < 0 || z > 3) {
            return -1;
        }
        if (x < 0 || x >= collisionFlags[z].length) {
            return -1;
        }
        if (y < 0 || y >= collisionFlags[z][x].length) {
            return -1;
        }
        return collisionFlags[z][x][y];
    }

    public static boolean isNorthBlocked(int z, int x, int y) {
        return (CollisionMap.getFlag(z, x, y) & 2) != 0;
    }

    public static boolean isSouthBlocked(int z, int x, int y) {
        return (CollisionMap.getFlag(z, x, y) & 32) != 0;
    }

    public static boolean isEastBlocked(int z, int x, int y) {
        return (CollisionMap.getFlag(z, x, y) & 8) != 0;
    }

    public static boolean isWestBlocked(int z, int x, int y) {
        return (CollisionMap.getFlag(z, x, y) & -128) != 0;
    }

    public static boolean isNorthWestBlocked(int z, int x, int y) {
        return (CollisionMap.getFlag(z, x, y) & 1) != 0;
    }

    public static boolean isSouthWestBlocked(int z, int x, int y) {
        return (CollisionMap.getFlag(z, x, y) & 64) != 0;
    }

    public static boolean isNorthEastBlocked(int z, int x, int y) {
        return (CollisionMap.getFlag(z, x, y) & 4) != 0;
    }

    public static boolean isSouthEastBlocked(int z, int x, int y) {
        return (CollisionMap.getFlag(z, x, y) & 16) != 0;
    }
}