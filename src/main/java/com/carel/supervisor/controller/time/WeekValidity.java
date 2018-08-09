package com.carel.supervisor.controller.time;

import java.util.Calendar;
import java.util.Date;


public class WeekValidity extends TimeValidity
{
    private DayChain root = null;

    public WeekValidity(String data)
    {
        root = new DayChain(data);
    }

    /*
     * SUNDAY:1
     * MONDAY:2
     * TUESDAY:3
     * WEDNESDAY:4
     * THURSDAY:5
     * FRIDAY:6
     * SATURDAY:7
     *
     */
    public boolean isValid(Date now)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int day = cal.get(Calendar.DAY_OF_WEEK);

        return root.isValid(day, hour, minutes);
    }

    public StringBuffer getData()
    {
        return root.getData();
    }
}
