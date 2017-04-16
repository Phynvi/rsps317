package com.bclaus.rsps.server.util;

import java.io.File;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.bclaus.rsps.server.vd.npc.drops.Drop;
import com.bclaus.rsps.server.vd.world.Position;
import com.bclaus.rsps.server.vd.npc.NPCAggression;
import com.bclaus.rsps.server.vd.npc.drops.DropTable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Misc {
	private static int range;
	public static final RandomGen RANDOM = new RandomGen();
	private static final String VALID_PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789243 ";

	
	public static boolean validPassword(String password) {
		for (Character c : password.toCharArray()) {
			if (!validPasswordCharacter(c))
				return false;
		}
		return true;
	}
	public static boolean validPasswordCharacter(int key) {
		for (int i2 = 0; i2 < VALID_PASSWORD_CHARACTERS.length(); i2++) {
			if (key == VALID_PASSWORD_CHARACTERS.charAt(i2))
				return true;
		}
		return false;
	}
	
	public static String longToPlayerName(long l) {
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = playerNameXlateTable[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static int getCurrentHP(int i, int i1, int i2) {
		double x = (double) i / (double) i1;
		return (int) Math.round(x * i2);
	}

	public static String format(long num) {
		return NumberFormat.getInstance().format(num);
	}
	
	public static String getFilteredInput(String input) {
		if (input.contains("\r")) {
			input = input.replaceAll("\r", "");
		}
		
		return input;
	}

	/**
	 * Capitalizes the first character of the argued string. Any leading or
	 * trailing whitespace in the argued string should be trimmed before using
	 * this method.
	 * 
	 * @param s
	 *            the string to capitalize.
	 * @return the capitalized string.
	 */
	public static String capitalize(String s) {
		return s.substring(0, 1).toUpperCase().concat(s.substring(1, s.length()));
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive
	 * <code>min</code> and inclusive <code>max</code> excluding the specified
	 * numbers within the {@code excludes} array.
	 * 
	 * @param min
	 *            The minimum inclusive number.
	 * @param max
	 *            The maximum inclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If {@code max - min + 1} is less than <tt>0</tt>.
	 * @see {@link #inclusiveRandom(int, int)}.
	 */
	public static int inclusiveRandomExcludes(int min, int max, int... exclude) {
		Arrays.sort(exclude);

		int result = inclusiveRandom(min, max);
		while (Arrays.binarySearch(exclude, result) >= 0) {
			result = inclusiveRandom(min, max);
		}

		return result;
	}

	public static <E> E randomElement(E[] array) {
		return array[(int) (rand.nextDouble() * array.length)];
	}

	public static int randomElement(int[] array) {
		return array[(int) (rand.nextDouble() * array.length)];
	}

	public static <E> E randomElement(List<? extends E> list) {
		return list.get((int) (rand.nextDouble() * list.size()));
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive
	 * <code>min</code> and inclusive <code>max</code>.
	 * 
	 * @param min
	 *            The minimum inclusive number.
	 * @param max
	 *            The maximum inclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If {@code max - min + 1} is less than <tt>0</tt>.
	 * @see {@link #exclusiveRandom(int)}.
	 */
	public static int inclusiveRandom(int min, int max) {
		if (max < min) {
			max = min + 1;
		}
		return exclusiveRandom((max - min) + 1) + min;
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive
	 * <code>min</code> and exclusive <code>max</code>.
	 * 
	 * <br>
	 * <br>
	 * This method is thread-safe. </br>
	 * 
	 * @param min
	 *            The minimum inclusive number.
	 * @param max
	 *            The maximum exclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If the specified range is less <tt>0</tt>
	 * 
	 *             <p>
	 *             We use {@link ThreadLocalRandom#current()} to produce this
	 *             random {@code int}, it is faster than a standard
	 *             {@link Random} instance as we do not have to wait on
	 *             {@code AtomicLong}.
	 *             </p>
	 */
	public static int exclusiveRandom(int min, int max) {
		if (max <= min) {
			max = min + 1;
		}
		return rand.nextInt((max - min)) + min;
	}

	public static final Random rand = new Random();

	/**
	 * Returns a pseudo-random {@code int} value between inclusive <tt>0</tt>
	 * and exclusive <code>range</code>.
	 * 
	 * <br>
	 * <br>
	 * This method is thread-safe. </br>
	 * 
	 * @param range
	 *            The exclusive range.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If the specified range is less <tt>0</tt>
	 * 
	 *             <p>
	 *             We use {@link ThreadLocalRandom#current()} to produce this
	 *             random {@code int}, it is faster than a standard
	 *             {@link Random} instance as we do not have to wait on
	 *             {@code AtomicLong}.
	 *             </p>
	 */
	public static int exclusiveRandom(int range) {
		return exclusiveRandom(0, range);
	}

	/**
	 * Returns the delta coordinates. Note that the returned Position is not an
	 * actual position, instead it's values represent the delta values between
	 * the two arguments.
	 * 
	 * @param a
	 *            the first position.
	 * @param b
	 *            the second position.
	 * @return the delta coordinates contained within a position.
	 */
	public static Position delta(Position a, Position b) {
		return new Position(b.getX() - a.getX(), b.getY() - a.getY());
	}

	/*	*//**
	 * Parse the all of the data for npc drops.
	 * 
	 * @throws Exception
	 *             if any errors occur while parsing this file.
	 */
	public static void loadNpcDrops() throws Exception {
		JsonParser parser = new JsonParser();
		JsonArray array = (JsonArray) parser.parse(new FileReader(new File("./data/npc_drops.json")));
		final Gson builder = new GsonBuilder().create();

		for (int i = 0; i < array.size(); i++) {
			JsonObject reader = (JsonObject) array.get(i);
			int id = -1;
			Drop[] common = null;
			Drop[] rare = null;

			if (reader.has("id")) {
				id = reader.get("id").getAsInt();
			}
			if (reader.has("common")) {
				common = builder.fromJson(reader.get("common").getAsJsonArray(), Drop[].class);
			}
			if (reader.has("rare")) {
				rare = builder.fromJson(reader.get("rare").getAsJsonArray(), Drop[].class);
			}

			if (id < 1) {
				throw new IllegalStateException("Invalid npc id: " + id);
			}

			DropTable.getDrops().put(id, new DropTable(id, Objects.requireNonNull(common), Objects.requireNonNull(rare)));
		}
	}

	public static JsonLoader loadNpcAggression() {
		return new JsonLoader("./Data/npc_aggression.json") {
			@Override
			public void load(JsonObject reader, Gson builder) {
				int[] ids = builder.fromJson(reader.get("ids"), int[].class);
				int distance = reader.get("distance").getAsInt();
				Arrays.stream(ids).forEach(i -> NPCAggression.AGGRESSION.put(i, distance));
			}
		};
	}

	public static String md5Hash(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String formatPlayerName(String str) {
		str = ucFirst(str);
		str.replace("_", " ");
		return str;
	}

	public static String longToReportPlayerName(long l) {
		int i = 0;
		final char ac[] = new char[12];
		while (l != 0L) {
			final long l1 = l;
			l /= 37L;
			ac[11 - i++] = Misc.playerNameXlateTable[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static String basicEncrypt(String s) {
		String toReturn = "";
		for (int j = 0; j < s.length(); j++) {
			toReturn += (int) s.charAt(j);
		}
		return toReturn;
	}

	public static String longToPlayerName2(long l) {
		int i = 0;
		char ac[] = new char[99];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = playerNameXlateTable[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static final char playerNameXlateTable[] = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public static String format(int num) {
		return NumberFormat.getInstance().format(num);
	}

	public static String ucFirst(String str) {
		str = str.toLowerCase();
		if (str.length() > 1) {
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
		return str;
	}

	public static void println(String str) {
		System.out.println(str);
	}

	public static int hexToInt(byte data[], int offset, int len) {
		int temp = 0;
		int i = 1000;
		for (int cntr = 0; cntr < len; cntr++) {
			int num = (data[offset + cntr] & 0xFF) * i;
			temp += num;
			if (i > 1) {
				i = i / 1000;
			}
		}
		return temp;
	}

	public static int random(int range) {
		return (int) (java.lang.Math.random() * (range + 1));
	}

	public static int random3(int range) {
		return (int) ((java.lang.Math.random() * range));
	}

	public static int randomNoZero(int range) {
		int val = random(range);

		if (val == 0)
			val++;

		return val;
	}

	public static long playerNameToInt64(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z') {
				l += (1 + c) - 65;
			} else if (c >= 'a' && c <= 'z') {
				l += (1 + c) - 97;
			} else if (c >= '0' && c <= '9') {
				l += (27 + c) - 48;
			}
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}

	private static char decodeBuf[] = new char[4096];

	/*
	 * public static String textUnpack(byte packedData[], int size) { int idx =
	 * 0, highNibble = -1; for (int i = 0; i < size * 2; i++) { int val =
	 * packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf; if (highNibble == -1) { if
	 * (val < 13) { decodeBuf[idx++] = xlateTable[val]; } else { highNibble =
	 * val; } } else { decodeBuf[idx++] = xlateTable[((highNibble << 4) + val) -
	 * 195]; highNibble = -1; } }
	 * 
	 * 
	 * return new String(decodeBuf, 0, idx); }
	 */
	public static String textUnpack(final byte packedData[], final int size) {
		int idx = 0, highNibble = -1;
		for (int i = 0; i < size * 2; i++) {
			final int val = packedData[i / 2] >> 4 - 4 * (i % 2) & 0xf;
			if (highNibble == -1) {
				if (val < 13) {
					Misc.decodeBuf[idx++] = Misc.xlateTable[val];
				} else {
					highNibble = val;
				}
			} else {
				Misc.decodeBuf[idx++] = Misc.xlateTable[(highNibble << 4) + val - 195];
				highNibble = -1;
			}
		}
		return new String(Misc.decodeBuf, 0, idx);
	}

	public static String optimizeText(String text) {
		char buf[] = text.toCharArray();
		boolean endMarker = true;
		for (int i = 0; i < buf.length; i++) {
			char c = buf[i];
			if (endMarker && c >= 'a' && c <= 'z') {
				buf[i] -= 0x20;
				endMarker = false;
			}
			if (c == '.' || c == '!' || c == '?') {
				endMarker = true;
			}
		}
		return new String(buf, 0, buf.length);
	}

	public static char xlateTable[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']', '>', '<', '^', '/', '_' };

	public static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1, 1, -1, 0, 1 };
	public static final byte[] DIRECTION_DELTA_Y = new byte[] { 1, 1, 1, 0, 0, -1, -1, -1 };

	public static int direction(int dx, int dy) {

		if (dx < 0) {
			if (dy < 0) {
				if (dx < dy) {
					return 11;
				}
				if (dx > dy) {
					return 9;
				}
				return 10;
			}
			if (dy > 0) {
				if (-dx < dy) {
					return 15;
				}
				if (-dx > dy) {
					return 13;
				}
				return 14;
			}
			return 12;
		}
		if (dx > 0) {
			if (dy < 0) {
				if (dx < -dy) {
					return 7;
				}
				if (dx > -dy) {
					return 5;
				}
				return 6;
			}
			if (dy > 0) {
				if (dx < dy) {
					return 1;
				}
				if (dx > dy) {
					return 3;
				}
				return 2;
			}
			return 4;
		}
		if (dy < 0) {
			return 8;
		}
		if (dy > 0) {
			return 0;
		}
		return -1;
	}

	public static int direction(int srcX, int srcY, int destX, int destY) {
		int dx = destX - srcX, dy = destY - srcY;

		if (dx < 0) {
			if (dy < 0) {
				if (dx < dy) {
					return 11;
				}
				if (dx > dy) {
					return 9;
				}
				return 10;
			}
			if (dy > 0) {
				if (-dx < dy) {
					return 15;
				}
				if (-dx > dy) {
					return 13;
				}
				return 14;
			}
			return 12;
		}
		if (dx > 0) {
			if (dy < 0) {
				if (dx < -dy) {
					return 7;
				}
				if (dx > -dy) {
					return 5;
				}
				return 6;
			}
			if (dy > 0) {
				if (dx < dy) {
					return 1;
				}
				if (dx > dy) {
					return 3;
				}
				return 2;
			}
			return 4;
		}
		if (dy < 0) {
			return 8;
		}
		if (dy > 0) {
			return 0;
		}
		return -1;
	}

	public static byte directionDeltaX[] = new byte[] { 0, 1, 1, 1, 0, -1, -1, -1 };
	public static byte directionDeltaY[] = new byte[] { 1, 1, 0, -1, -1, -1, 0, 1 };

	public static byte xlateDirectionToClient[] = new byte[] { 1, 2, 4, 7, 6, 5, 3, 0 };

	public static int random(int i, int i0) {
		return (int) (java.lang.Math.random() * (range + 1));
	}

	public static int getIndex(int id, int[] array) {
		for (int i = 0; i < array.length; i++)
			if (id == array[i])
				return i;
		return -1;
	}

	public static String getNumberFormat(int number) {
		return NumberFormat.getInstance().format(number);
	}

	public static final String sendCashToString(long j) {
		if (j >= 0 && j < 10000)
			return String.valueOf(j);
		else if (j >= 10000 && j < 10000000)
			return j / 1000 + "K";
		else if (j >= 10000000 && j < 999999999)
			return j / 1000000 + "M";
		else
			return Long.toString(j);
	}

	public static String insertCommas(String str) {
		if (str.length() < 4) {
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) + "," + str.substring(str.length() - 3, str.length());
	}

	/**
	 * An inclusive or exclusive interval.
	 * 
	 * @author lare96
	 */
	public static class Interval {

		/** The starting point. */
		private int start;

		/** The ending point. */
		private int end;

		/**
		 * Creates a new inclusive {@link Interval}.
		 * 
		 * @param start
		 *            the starting point.
		 * @param end
		 *            the ending point.
		 * @return the inclusive interval.
		 */
		public Interval inclusiveInterval(int start, int end) {
			if (start > end) {
				throw new IllegalArgumentException("End value must be higher than start value!");
			}

			this.start = start;
			this.end = end;
			return this;
		}

		/**
		 * Creates a new exclusive {@link Interval}.
		 * 
		 * @return the exclusive interval.
		 */
		public Interval exclusiveInterval(int start, int end) {
			if (start > end) {
				throw new IllegalArgumentException("End value must be higher than start value!");
			}

			this.start = start + 1;
			this.end = end - 1;
			return this;
		}

		private static Random r = new Random();

		/**
		 * Gets a random value based on the interval.
		 * 
		 * @return the random value.
		 */
		public int calculate() {
			int difference = end - start;

			return (start + r.nextInt(difference));
		}

		/**
		 * The starting point.
		 * 
		 * @return the starting point.
		 */
		public int getStart() {
			return start;
		}

		/**
		 * The ending point.
		 * 
		 * @return the ending point.
		 */
		public int getEnd() {
			return end;
		}
	}

	public static boolean arrayEquals(int element, int[] array) {
		for (int i : array)
			if (i == element)
				return true;
		return false;
	}
	   public static String toProperCase(String s) {
           String temp=s.trim();
           String spaces="";
           if(temp.length()!=s.length()) {
           int startCharIndex=s.charAt(temp.indexOf(0));
           spaces=s.substring(0,startCharIndex);
           }
           temp=temp.substring(0, 1).toUpperCase() +
           spaces+temp.substring(1).toLowerCase()+" ";
           return temp;
       }

		public static int inclusiveRandom(int range) {
			return (int) (ThreadLocalRandom.current().nextDouble() * (range + 1));
		}
}