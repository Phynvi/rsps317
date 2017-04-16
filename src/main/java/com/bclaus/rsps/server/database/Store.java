package com.bclaus.rsps.server.database;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.bclaus.rsps.server.vd.player.Player;

// TODO: Auto-generated Javadoc
/**
 * The Class Store.
 */
public class Store {

	/**
	 * @author: Pax M
	 */

	/** The connection. */
	private static Connection connection;

	/** The las connection. */
	private static long lasConnection = System.currentTimeMillis();
	static {
		createConnection();
	}

	/**
	 * Claim payment.
	 *
	 * @param p
	 *            the p
	 * @param name
	 *            the name
	 */
	public static void claimPayment(final Player p, final String name) {
		try {
			System.out.println("1: " + name);
			if (System.currentTimeMillis() - lasConnection > 10000) {
				System.out.println("21");
				destroyConnection();
				createConnection();
				lasConnection = System.currentTimeMillis();
			}
			Statement s = connection.createStatement();
			String name2 = name.replaceAll(" ", "_");
			String query = "SELECT * FROM itemstore WHERE username = '" + name + "'";
			ResultSet rs = s.executeQuery(query);
			boolean claimed = false;
			while (rs.next()) {
				int prod = Integer.parseInt(rs.getString("productid"));
				int price = Integer.parseInt(rs.getString("price"));

				System.out.println("prod: " + prod + " price: " + price);
				if (prod == 1 && price == 5) {
					p.gfx100(199);
					claimed = true;
					p.donationPoints = +500;
					p.amountDonated = +5;
					// }
					final String FILE_PATH = "Data/orders/";
					try {
						DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(FILE_PATH + p.playerName + ".txt", true));
						writer.write("[" + dateFormat.format(cal.getTime()) + ", IP: " + p.connectedFrom
								+ "] received 500 Donator points, value: $5 USD.");
						writer.newLine();
						writer.flush();
						writer.close();
					} catch (IOException e) {
						System.err.println(e);
					}
				} else if (prod == 2 && price == 10) {
					p.gfx100(199);
					claimed = true;
					p.amountDonated = +10;
					p.donationPoints = +1000;

					final String FILE_PATH = "Data/logs/orders/";
					try {
						DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(FILE_PATH + p.playerName + ".txt", true));
						writer.write("[" + dateFormat.format(cal.getTime()) + ", IP: " + p.connectedFrom
								+ "] received 1000 Donator points, value: $10 USD.");
						writer.newLine();
						writer.flush();
						writer.close();
					} catch (IOException e) {
						System.err.println(e);
					}
				} else if (prod == 3 && price == 25) {
					p.gfx100(199);
					claimed = true;
					p.amountDonated = +25;
					p.donationPoints = +0;
					final String FILE_PATH = "Data/logs/orders/";
					try {
						DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(FILE_PATH + p.playerName + ".txt", true));
						writer.write("[" + dateFormat.format(cal.getTime()) + ", IP: " + p.connectedFrom
								+ "] received 2500 Donator points, value: $25 USD.");
						writer.newLine();
						writer.flush();
						writer.close();
					} catch (IOException e) {
						System.err.println(e);
					}
				} else if (prod == 4 && price == 50) {
					p.gfx100(199);
					claimed = true;
					p.amountDonated = +50;
					p.donationPoints = +5000;

					final String FILE_PATH = "Data/logs/orders/";
					try {
						DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(FILE_PATH + p.playerName + ".txt", true));
						writer.write("[" + dateFormat.format(cal.getTime()) + ", IP: " + p.connectedFrom
								+ "] received 5000 Donator points, value: $50 USD.");
						writer.newLine();
						writer.flush();
						writer.close();
					} catch (IOException e) {
						System.err.println(e);
					}
				} else if (prod == 5 && price == 100) {
					p.gfx100(199);
					claimed = true;
					p.amountDonated = +100;
					p.donationPoints = +10000;
					final String FILE_PATH = "Data/logs/orders/";
					try {
						DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(FILE_PATH + p.playerName + ".txt", true));
						writer.write("[" + dateFormat.format(cal.getTime()) + ", IP: " + p.connectedFrom
								+ "] received 10000 Donator points, value: $100 USD.");
						writer.newLine();
						writer.flush();
						writer.close();
					} catch (IOException e) {
						System.err.println(e);
					}
				}
				if (claimed) {
					s.execute("DELETE FROM `itemstore` WHERE `username` = '" + name2 + "';");
					p.sendMessage("@red@Thank you for your purchase, your order has been delivered.");
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	/**
	 * Creates the connection.
	 */
	public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			connection = DriverManager.getConnection("jdbc:mysql://localhost/store", "root",
					"3654462");
		} catch (Exception e) {
			// e.printStackTrace();
		}

	}

	/**
	 * Destroy connection.
	 */
	public static void destroyConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
	}

	/**
	 * Inits the.
	 */
	public static void init() {
		createConnection();
		System.out.println("Initializing store...");
	}
}