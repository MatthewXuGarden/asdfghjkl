package com.carel.supervisor.controller.time;

import java.util.Calendar;
import java.util.Date;


public class YearValidity extends TimeValidity
{
    private YearChain root = null;
    private boolean cyclic = false;

    //  1/10/2006-10|13:30-14:30;15:30-18:30,13|13:30-14:30;15:30-18:30
    public YearValidity(String data, boolean cyclic)
    {
        root = new YearChain(data);
        this.cyclic = cyclic;
    }

    public boolean isValid(Date now)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1; //We start to count months from 1 and not from 0 like java
        int year = 0;

        if (!cyclic)
        {
            year = cal.get(Calendar.YEAR);
        }

        return root.isValid(year, month, day, hour, minutes);
    }

    public StringBuffer getData()
    {
        return root.getData();
    }
}
