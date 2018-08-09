package com.carel.supervisor.controller.time;

import com.carel.supervisor.controller.database.TimeBandBean;


public class TimeValidityFactory
{
    private TimeValidityFactory()
    {
    }

    public static TimeValidity createTime(TimeBandBean timeBandBean)
    {
        int type = timeBandBean.getTimetype();
        String data = timeBandBean.getTimeband();
        TimeValidity value = null;
        switch (type)
        {
        case TimeBandBean.DAYLY:
        	value = new DayValidity(data);
        	break;
        case TimeBandBean.WEEKLY:
        	value = new WeekValidity(data);
        	break;
        case TimeBandBean.MONTHLY:
        	value = new MonthValidity(data);
        	break;
        case TimeBandBean.YEAR_ONE_SHOT:
        	value = new YearValidity(data, false);
        	break;
        case TimeBandBean.YEAR_REPEAT:
        	value = new YearValidity(data, true);
        	break;
        default:
        	value = new AlwaysValidity();
        }
        return value;
    }
}
