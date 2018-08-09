package com.carel.supervisor.controller.time;

import java.util.Calendar;
import java.util.Date;


public class MonthValidity extends TimeValidity
{
    private DayChain root = null;

    public MonthValidity(String data)
    {
        root = new DayChain(data);
    }

    public boolean isValid(Date now)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return root.isValid(day, hour, minutes);
    }

    public StringBuffer getData()
    {
        return root.getData();
    }
}
