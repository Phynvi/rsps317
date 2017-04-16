package com.bclaus.rsps.server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connections {

	private Connection con;
	private Statement stmt;
	private String db = null;

	public Connections(final String db) {
		this.db = db;
	}

	public ResultSet query(final String s) {
		try {
			if (s.toLowerCase().startsWith("select")) {
				final ResultSet rs = this.stmt.executeQuery(s);
				return rs;
			}
			this.stmt.execute(s);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void destroyConnection() {
		try {
			this.con.close();
			this.stmt.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.con = DriverManager.getConnection("jdbc:mysql://vdrspsco/" + this.db, "vdrspsco_home", "FTZtpSDESaOz");
			this.stmt = this.con.createStatement();
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

}
