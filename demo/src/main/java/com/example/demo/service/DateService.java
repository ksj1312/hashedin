package com.example.demo.service;

import java.util.Calendar;
import java.util.Date;

public class DateService {

	public static Date getFirstDay(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		if (months == 0)
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3 - 3);
		else
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - months);

		cal.set(Calendar.HOUR, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		return cal.getTime();
	}
	
	public static Date getLastDay(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (months == 0)
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) / 3 * 3 + 2 - 3);
		else
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - months);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		return cal.getTime();
	}
}
