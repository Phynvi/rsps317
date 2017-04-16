package com.bclaus.rsps.server.util;

import java.io.File;

/**
 * A file utility class to handle file related needs
 * 
 * @author Mobster
 *
 */
public class FileUtils {

	/**
	 * The directory for the market
	 */
	private static final File MARKET_DIRECTORY = new File("./data/market/");

	/**
	 * The directory for sanction
	 */
	private static final File SANCTION_DIRECTORY = new File("./data/bans/");

	/**
	 * The directory for the characters
	 */
	private static final File CHARACTER_DIRECTORY = new File("./data/characters/");

	static {
		if (!MARKET_DIRECTORY.exists()) {
			MARKET_DIRECTORY.mkdir();
		}
		if (!SANCTION_DIRECTORY.exists()) {
			SANCTION_DIRECTORY.mkdir();
		}
		if (!CHARACTER_DIRECTORY.exists()) {
			CHARACTER_DIRECTORY.mkdir();
		}
	}

	/**
	 * Gets the path for the market directory
	 * 
	 * @return The path for the market directory
	 */
	public static String getMarketDirectory() {
		return MARKET_DIRECTORY.getPath();
	}

	/**
	 * Gets the path for the sanction directory
	 * 
	 * @return The path for the sanction directory
	 */
	public static String getSanctionDirectory() {
		return SANCTION_DIRECTORY.getPath();
	}

	/**
	 * Gets the directory for the characters
	 * 
	 * @return The directory for the characters
	 */
	public static String getCharacterDirectory() {
		return CHARACTER_DIRECTORY.getPath();
	}
}
