package com.bclaus.rsps.server.vd.content;

import java.util.Calendar;

/**
 * @author Tim
 * 
 */
public class DoubleExperience {

	/*public static boolean isDoubleExperience() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
				//|| Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
	}*/
	
	public static boolean isDoubleExperience() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		return day == Calendar.FRIDAY || day == Calendar.SATURDAY || day == Calendar.SUNDAY;
	}
}
