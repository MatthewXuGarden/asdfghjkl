package com.carel.supervisor.dataaccess.function;

import java.util.GregorianCalendar;

public class CarelFunction
{
    public static long calcYear(Long value)
    {
        long rval = value.longValue();
        if(rval != 0)
        {
            long HACCPCurrentYear = 0;
            long CurrentYear = 0;
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(System.currentTimeMillis());
            CurrentYear = gc.get(GregorianCalendar.YEAR);
            CurrentYear-=2000;
            
            HACCPCurrentYear = CurrentYear+14;
            HACCPCurrentYear = HACCPCurrentYear%15;
            HACCPCurrentYear++;
            
            rval = CurrentYear + rval - HACCPCurrentYear;
            
            if((rval - HACCPCurrentYear) > 0)
                rval -= 15;
        }
        return new Long(rval);
    }
}
