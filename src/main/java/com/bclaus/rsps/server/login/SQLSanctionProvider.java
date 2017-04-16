package com.bclaus.rsps.server.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class SQLSanctionProvider {

	private static final String SQL_DATABASE_NAME = "vdrspsco_home";
	private static final String SQL_PASSWORD = "FTZtpSDESaOz";
	private static final String SQL_SERVER = "legacy-317";
	private static final String SQL_USERNAME = "vdrspsco_home";

	private Connection connection = null;
	private boolean sqlErrorOccured = false;

	public boolean[] check(String username, String address) {
		// if (address.equals("127.0.0.1")) {
		// return new boolean[2];
		// }

		boolean banned = false;
		boolean muted = false;

		try {
			if (checkConnection()) {
				StringBuilder bldr = new StringBuilder();

				bldr.append("SELECT UNIX_TIMESTAMP() as now, id, type, UNIX_TIMESTAMP(expire) as expire ");
				bldr.append("FROM sanctions ");
				bldr.append("WHERE ((name = ?) ");
				bldr.append("OR (ipaddress IS NOT NULL AND ipaddress = ?)) ");
				bldr.append("AND closed = 0 ");
				bldr.append("ORDER BY id DESC");
				PreparedStatement statement = connection.prepareStatement(bldr.toString());
				statement.setString(1, username);
				statement.setString(2, address);
				ResultSet result = statement.executeQuery();

				while (result.next()) {
					int id = result.getInt("id");
					long now = result.getLong("now");
					long expire = result.getLong("expire");
					String type = result.getString("type");

					if ((expire != 0) && (now >= expire)) {
						PreparedStatement statement2 = connection.prepareStatement("UPDATE sanctions SET closed = 1 WHERE id = ?");
						statement2.setInt(1, id);
						statement2.executeUpdate();
						statement2.close();
					} else if ("ban".equals(type)) {
						banned = true;
					} else if ("mute".equals(type)) {
						muted = true;
					}
				}

				statement.close();
			} else {
				return null;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			sqlErrorOccured = true;
			return null;
		}

		return new boolean[] { banned, muted };
	}

	/**
	 * Ensure SQL connection is open.
	 * 
	 * @return
	 */
	private boolean checkConnection() throws SQLException {
		if (sqlErrorOccured || (connection == null)) {
			try {
				connection = DriverManager.getConnection("jdbc:mysql://" + SQL_SERVER + '/' + SQL_DATABASE_NAME, SQL_USERNAME, SQL_PASSWORD);
				sqlErrorOccured = false;
				return true;
			} catch (SQLException ex) {
				sqlErrorOccured = true;
				throw ex;
			}
		}

		return (connection != null) && !connection.isClosed();
	}

}