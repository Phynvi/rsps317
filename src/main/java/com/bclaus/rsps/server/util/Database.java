package com.bclaus.rsps.server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	
	private Connection conn;
	private Statement stmt;
	
	private String host;
	private String user;
	private String pass;
	private String database;
	private int timeout;
	
	public Database(String host, String user, String pass, String database) {
		this.host = host;
		this.user = user;
		this.pass = pass;
		this.database = database;
		this.timeout = 2500;
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public Statement getStatement() {
		return stmt;
	}
	
	public boolean init() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database+"?connectTimeout="+timeout, user, pass);
			return true;
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.err.println("[Database] Failed to connect! Reason: "+e.getMessage().split("\n")[0]+"");
			return false;
		}
	}
	
	public boolean initBatch() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.conn = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+database+"?connectTimeout="+timeout+"&rewriteBatchedStatements=true", user, pass);
			return true;
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.err.println("[Database] Failed to connect! Reason: "+e.getMessage().split("\n")[0]+"");
			return false;
		}
	}

	public int executeUpdate(String query) {
        try {
        	this.stmt = this.conn.createStatement(1005, 1008);
            int results = stmt.executeUpdate(query);
            return results;
        } catch (SQLException ex) {
        	System.err.println("[Database] Update failed! Reason: "+ex.getMessage().split("\n")[0]+"");
        }
        return -1;
    }
	
	public ResultSet executeQuery(String query) {
        try {
        	this.stmt = this.conn.createStatement(1005, 1008);
            ResultSet results = stmt.executeQuery(query);
            return results;
        } catch (SQLException ex) {
        	System.err.println("[Database] Query failed! Reason: "+ex.getMessage().split("\n")[0]+"");
        }
        return null;
    }
	
	public PreparedStatement prepare(String query) throws SQLException {
		return conn.prepareStatement(query);
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	/**
	 * Sets the timeout of the connection in milliseconds
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public void destroyAll() {
        try {
    		conn.close();
        	conn = null;
        	if (stmt != null) {
    			stmt.close();
        		stmt = null;
        	}
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
	
	
}
