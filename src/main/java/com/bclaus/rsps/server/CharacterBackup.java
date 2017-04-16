
package com.bclaus.rsps.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CharacterBackup {

	public static String CHARACTER_FOLDER = "./Data/characters/"; 
	public static String BACKUP_FOLDER = "./Data/characterbackup/" 
			+ getDate() + ".zip";

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM dd yyyy");
		Date date = new Date();
		String currentDate = dateFormat.format(date);
		return currentDate;
	}

	/**
	 * Inits the.
	 */
	public static void init() {
		File folder = new File(CHARACTER_FOLDER);
		File zipped = new File(BACKUP_FOLDER);
		if (!zipped.exists()) {
			try {
				if (folder.list().length == 0) {
					System.out.println("The folder is empty.");
					return;
				}
				//zipDirectory(folder, zipped);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static final void zip(File directory, File base, ZipOutputStream zos)
			throws IOException {
		File[] files = directory.listFiles();
		byte[] buffer = new byte[20000];
		int read = 0;
		for (int i = 0, n = files.length; i < n; i++) {
			if (files[i].isDirectory()) {
				zip(files[i], base, zos);
			} else {
				FileInputStream in = new FileInputStream(files[i]);
				ZipEntry entry = new ZipEntry(files[i].getPath().substring(
						base.getPath().length() + 1));
				zos.putNextEntry(entry);
				while (-1 != (read = in.read(buffer))) {
					zos.write(buffer, 0, read);
				}
				in.close();
			}
		}
	}
	
}	

	//public static final void zipDirectory(File folder, File zf)
			//throws IOException {
		//*ZipOutputStream zipped = new ZipOutputStream(new FileOutputStream(zf));
		//*zip(folder, folder, zipped);
		//*zipped.close();
		//*System.out.println("Finished character backup.");
	//*}
