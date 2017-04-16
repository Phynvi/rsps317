package com.bclaus.rsps.server.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An {@link PrintStream} that formats <code>System.out</code> and
 * <code>System.err</code> in my own format.
 * 
 * @author Thomas le Godais
 */
public class ConsoleStream extends PrintStream {

	/**
	 * A formatter used for formating a date instance.
	 */
	private SimpleDateFormat simpleDateFormat;

	/**
	 * Constructs a new {@code ConsoleStream} instance.
	 * 
	 * @param out
	 *            The outputstream to modify.
	 */
	public ConsoleStream(OutputStream out) {
		super(out);
		simpleDateFormat = new SimpleDateFormat();
	}

	@Override
	public void println(String message) {
		Throwable throwable = new Throwable();
		String className = throwable.getStackTrace()[1].getFileName().replace(
				".java", "");
		String formatted = "[" + format(new Date()) + "][" + className + "]: ";
		super.println(formatted + message);
	}

	/**
	 * Formats an {@link Date} used the {@link SimleDateFormat} pre-sets.
	 * 
	 * @param date
	 *            The date in which we're formating.
	 */
	private String format(Date date) {
		return simpleDateFormat.format(date);
	}
}
