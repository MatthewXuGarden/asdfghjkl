package com.carel.supervisor.controller.time;

import java.util.Calendar;
import java.util.Date;


public class DayValidity extends TimeValidity
{
    private HourChain root = null;

    public DayValidity(String data)
    {
        root = new HourChain(data);
    }

    public boolean isValid(Date now)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);

        return root.isValid(hour, minutes);
    }

    public StringBuffer getData()
    {
        return root.getData();
    }
}
