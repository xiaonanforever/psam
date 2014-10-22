package com.topdot.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static final String FORMAT = "yyyy/MM/dd/HH/mm/ss";
	
	public static String getDateTimeString(Date date) {
		if (date == null) {
			return null;
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.format(date);
		}
		
	}
	
	public static String getDateTimeMsString(Date date) {
		if (date == null) {
			return null;
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			return df.format(date);
		}
		
	}
	
	public static Date parseDateTime(String date) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.parse(date);
	}
	
	@SuppressWarnings("deprecation")
	public static Date addSeconds(Date date, int seconds) {
		date.setSeconds(date.getSeconds() + seconds);
		return date;
	}
	
}
