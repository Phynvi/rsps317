package com.bclaus.rsps.server.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * The purpose of this class is to represent a current date in time, and request
 * for it to be updated every certain amount of cycles.
 * 
 * @author Jason MacKeigan
 * @date Oct 26, 2014, 10:57:39 PM
 */
public class SimpleCalendar {

	/**
	 * The current date, represented with the use of the DateFormat class
	 */
	private java.util.Calendar date = GregorianCalendar.getInstance();

	/**
	 * Representation of the current date
	 */
	DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	/**
	 * Constructs a new calendar and tells it to automatically update however
	 * often we need it to.
	 * 
	 * @param cycles
	 *            the number of cycles
	 */
	public SimpleCalendar(TimeUnit unit, SimpleDateFormat formatter) {
		this.formatter = formatter;
	}

	/**
	 * Returns the date representation in years, months, and days
	 * 
	 * @return the date in years, months, and days
	 */
	public String getYMD() {
		date = GregorianCalendar.getInstance();
		return reformat(new SimpleDateFormat("yyyy/MM/dd"));
	}

	/**
	 * Returns the date representation in hours, minutes, and seconds
	 * 
	 * @return the hour, minute and second of the day
	 */
	public String getHMS() {
		date = GregorianCalendar.getInstance();
		return reformat(new SimpleDateFormat("HH:mm:ss"));
	}

	/**
	 * Returns a string representation of the current date and time
	 * 
	 * @return the formatted date and time
	 */
	@Override
	public String toString() {
		date = GregorianCalendar.getInstance();
		return formatter.format(date.getTime());
	}

	/**
	 * Returns a new unfamiliar representation of the date
	 * 
	 * @param formatter
	 *            the format of the date
	 * @return the date reformatted
	 */
	public String reformat(SimpleDateFormat formatter) {
		date = GregorianCalendar.getInstance();
		return formatter.format(date.getTime());
	}

	/**
	 * Returns the date object for this calendar
	 * 
	 * @return the date
	 */
	public java.util.Calendar getInstance() {
		date = GregorianCalendar.getInstance();
		return date;
	}
}
