package com.bclaus.rsps.server.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Jason MacKeigan(http://www.rune-server.org/members/jason)
 * This was very poorly written in 2013 and should be rewritten
 * as soon as possible.
 */
public class SimpleDate {
	
	private static int year, month, day, hour, minute, dayOfWeek;
	private static boolean isBeforeTwelve;
	private static int hourOfDay;
	private static Calendar calendar;
	private static long lastUpdate;
	
	public static void getInstance() {
		calendar = GregorianCalendar.getInstance();
	}
	
	public static void pulse() {
		if(System.currentTimeMillis() - lastUpdate < 30000)
			return;
		SimpleDate.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		month++;
		if(month > 12)
			month = 1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		hour = calendar.get(Calendar.HOUR);
		minute = calendar.get(Calendar.MINUTE);
		dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		isBeforeTwelve = hourOfDay >= 12 ? false : true;
		lastUpdate = System.currentTimeMillis();
	}
	
	public static int getHourOfDay() {
		return hourOfDay;
	}
	
	public static int getLastDayOfMonth(int month) {
		switch(month){
		case 2:
			return 28;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
			
		default:
			return 31;
		}
	}
	
	public static boolean isWeekend() {
		return dayOfWeek == 1 || dayOfWeek == 7
				|| SimpleDate.getDay() > 6 && SimpleDate.getDay() < 9 && SimpleDate.getMonth() == 2;
	}
	
	public static String timeToString() {
		String amPm = SimpleDate.isBeforeTwelve() ? "AM" : "PM";
		String zero =  getMinute() < 10 ? "0" : "";
		return SimpleDate.getStringRepresentationOfDay(SimpleDate.getDayOfWeek())+"," +
				" "+SimpleDate.getStringRepresentationOfMonth(SimpleDate.getMonth())
				+ " " + SimpleDate.getDay()+ ", " +SimpleDate.getHour()+":"+zero+""+SimpleDate.getMinute()+""+amPm+"";
	}
	
	public static String toString(int year, int month, int day, int hour, int minute) {
		String amPm = SimpleDate.isBeforeTwelve() ? "AM" : "PM";
		String zero =  getMinute() < 10 ? "0" : "";
		return SimpleDate.getStringRepresentationOfMonth(month)
				+ " " + day+ ", " +hour+":"+zero+""+minute+""+amPm+"";
	}
	
	public static String toString(int year, int month, int day) {
		return SimpleDate.getStringRepresentationOfMonth(month) + " " + day + SimpleDate.getDaySuffix(day)+", "+year;
	}
	
	public static boolean passed(int month, int day) {
		if(SimpleDate.month == month && SimpleDate.day > day)
			return true;
		if(SimpleDate.month > month)
			return true;
		return false;
	}
	
	public static boolean passed(int year, int month, int day, int hour, int minute) {
		if(SimpleDate.year > year)
			return true;
		if(SimpleDate.year == year && SimpleDate.month > month)
			return true;
		if(SimpleDate.year == year && SimpleDate.month == month && SimpleDate.day > day)
			return true;
		if(SimpleDate.year == year && SimpleDate.month == month && SimpleDate.day == day && SimpleDate.hour > hour)
			return true;
		if(SimpleDate.year == year && SimpleDate.month == month && SimpleDate.day == day && SimpleDate.hour == hour && SimpleDate.minute >= minute) {
			return true;
		}
		return false;
	}
	
	public static String getStringRepresentationOfMonth(int month) {
		switch(month) {
		case 1:
			return "January";
		case 2:
			return "February";
		case 3:
			return "March";
		case 4:
			return "April";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December";
		}
		return "Undefined";	
	}
	
	public static String getFutureDate(int days, int hours) {
		int year = getYear();
		int month = getMonth();
		int day = getDay();
		int hour = getHourOfDay();
		if(month == 12)
			if(day + days > getLastDayOfMonth(12))
				year++;
		if(hours + hour > 23) {
			day++;
			hour = (hours + hour) % 23;
		} else
			hour = (hours + hour);
		if(days + day > getLastDayOfMonth(month) && month != 12) {
			day = (days + day) % getLastDayOfMonth(month);
			month++;
		} else
			day = day + days;
		return year+"/"+month+"/"+day+"/"+hour;
	}
	
	public static String getStringRepresentationOfDay(int day) {
		switch(day) {
			case 1:
				return "Sunday";
			case 2:
				return "Monday";
			case 3:
				return "Tuesday";
			case 4:
				return "Wed";
			case 5:
				return "Thurs";
			case 6:
				return "Friday";
			case 7:
				return "Saturday";
		}
		return "Undefined";
	}
	
	public static String getDaySuffix(int day) {
		switch(day) {
			case 1:
			case 21:
			case 31:
				return "st";
			case 2:
			case 22:
				return "nd";
			case 3:
			case 23:
				return "rd";
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
			case 30:
				return "th";
		}
		return "";
	}
	
	public static int getDayOfWeek() {
		return dayOfWeek;
	}
	
	public static int getYear() {
		return year;
	}
	
	public static int getMonth() {
		return month;
	}
	
	public static int getDay() {
		return day;
	}
	
	public static int getHour() {
		return hour;
	}
	
	public static int getMinute() {
		return minute;
	}

	public static boolean isBeforeTwelve() {
		return isBeforeTwelve;
	}

	public static void setBeforeTwelve(boolean isBeforeTwelve) {
		SimpleDate.isBeforeTwelve = isBeforeTwelve;
	}
}
