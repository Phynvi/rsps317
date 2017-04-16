package com.bclaus.rsps.server.database;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bclaus.rsps.server.Constants;

/**
 * @author Gabriel Hannason
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MySQLController {
	
	public static final ExecutorService SQL_SERVICE = Executors.newSingleThreadExecutor();   
	
	public static void toggle() {
		if(Constants.MYSQL_ENABLED) {
			MySQLProcessor.terminate();
			CONTROLLER = null;
			DATABASES = null;
			Constants.MYSQL_ENABLED = false;
		} else if(!Constants.MYSQL_ENABLED) { 
			init();
			Constants.MYSQL_ENABLED = true;
		}
	}

	private static MySQLController CONTROLLER;
	private static com.bclaus.rsps.server.database.impl.Store STORE = new com.bclaus.rsps.server.database.impl.Store();
	
	public static void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch(Exception e) {
			e.printStackTrace();
		}
		CONTROLLER = new MySQLController();
	}

	public static MySQLController getController() {
		return CONTROLLER;
	}

	public static com.bclaus.rsps.server.database.impl.Store getStore() {
		return STORE;
	}

	public enum Database {
		HIGHSCORES,
		RECOVERY,
		GRAND_EXCHANGE;
	}

	/* NON STATIC CLASS START */

	private static MySQLDatabase[] DATABASES = new MySQLDatabase[2];

	public MySQLDatabase getDatabase(Database database) {
		return DATABASES[database.ordinal()];
	}

	
	public MySQLController() {
		/* DATABASES */
		DATABASES = new MySQLDatabase[]{
				
				new MySQLDatabase("51.254.212.77", 3306, "DemonRsps_store", "DemonRsps_store2", "elkadim1"),
		};
		
	
		/*
		 * Start the process
		 */
		MySQLProcessor.process();
	}

	private static class MySQLProcessor {

		private static boolean running;
		
		private static void terminate() {
			running = false;
		}

		public static void process() {
			if(running) {
				return;
			}
			running = true;
			SQL_SERVICE.submit(new Runnable() {
				public void run() {
					try {
						while(running) {
							if(!Constants.MYSQL_ENABLED) {
								terminate();
								return;
							}
							for(MySQLDatabase database : DATABASES) {
								
								if(!database.active) {
									continue;
								}

								if(database.connectionAttempts >= 5) {
									database.active = false;
								}

								Connection connection = database.getConnection();
								try {
									connection.createStatement().execute("/* ping */ SELECT 1");
								} catch (Exception e) {
									database.createConnection();
								}
							}
							Thread.sleep(25000);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
