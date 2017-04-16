package com.bclaus.rsps.server.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Logs player related data
 * 
 * @author Mobster
 *
 */
public class PlayerLogging {

	/**
	 * The log directory which we can use to log data
	 */
	public static final File LOG_DIRECTORY = new File("./data/logs/");

	static {
		/**
		 * If the directory doesn't exist, make it
		 */
		if (!LOG_DIRECTORY.exists()) {
			LOG_DIRECTORY.mkdir();
		}
	}

	/**
	 * A thread pool to handle logging queries, no need to keep synchronization
	 * of anything as we're simply logging data, not modifying
	 */
	private static final Executor service = Executors.newCachedThreadPool();

	public static void write(String log, File file) {
		service.execute(() -> {

			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
				writer.write(log);
				writer.newLine();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		});

	}

}
