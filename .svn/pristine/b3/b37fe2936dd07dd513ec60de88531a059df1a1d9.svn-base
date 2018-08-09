package com.carel.supervisor.base.conversion;

import java.text.*;
import java.util.*;


public class DateUtils
{
    private DateUtils()
    {
    }

    public static Date resetTime(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);

        return cal.getTime();
    }

    public static Date resetDate(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.YEAR, 0);

        return cal.getTime();
    }

    public static Date setTime(Date date, int hours, int minutes, int seconds)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, seconds);

        return cal.getTime();
    }

    public static Date createDate(int day, int month, int year)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);

        return resetTime(cal.getTime());
    }

    public static Date createDayWeek(int day, int hours, int minutes,
        int seconds)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, day);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, seconds);

        return cal.getTime();
    }

    public static Date createDayMonth(int day, int hours, int minutes,
        int seconds)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, seconds);

        return cal.getTime();
    }

    public static Date createDayYear(int year, int month, int day, int hours,
        int minutes, int seconds)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, seconds);

        return cal.getTime();
    }

    public static Date createDate(int seconds, int minutes, int hours, int day,
        int month, int year)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, seconds);

        return cal.getTime();
    }

    public static String date2String(Date date, String formParam)
    {
        String code = "";
    	if (null != date)
        {
        	SimpleDateFormat formatter = new SimpleDateFormat(formParam);
            code = formatter.format(date);
        }
    	return code;
    }
}
