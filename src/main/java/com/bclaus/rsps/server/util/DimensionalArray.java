package com.bclaus.rsps.server.util;

/**
 * 
 * 
 * @author <a href="http://www.rune-server.org/members/Stand+Up/">Stan</a>
 */
public final class DimensionalArray {

	/**
	 * The value for this {@link DimensionalArray}.
	 */
	private final int value;
	
	/**
	 * The key for this {@link DimensionalArray}.
	 */
	private final int key;
	
	/**
	 * Creates a new instance of the 2 value pairs.
	 * 
	 * @param value
	 * 		The value for this instanced {@link DimensionalArray}
	 * @param key
	 * 		The key for this instanced {@link DimensionalArray}
	 */
	public DimensionalArray(int value, int key) {
		this.value = value;
		this.key = key;
	}

	/**
	 * Gets the encapsulated field of value.
	 * @return the value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Gets the encapsulated field of key.
	 * @return the key.
	 */
	public int getKey() {
		return key;
	}
}
