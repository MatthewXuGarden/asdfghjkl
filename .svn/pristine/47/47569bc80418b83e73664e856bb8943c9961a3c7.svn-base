package com.carel.supervisor.base.conversion;

import java.util.Date;


public class DateDifference
{
    private static final long SECOND_IN_DAY = 86400;
    private static final long SECOND_IN_HOUR = 3600;
    private static final long SECOND_IN_MINUT = 60;
    private long dateDiff = 0;

    public DateDifference(Date date1, Date date2)
    {
        dateDiff = (date2.getTime() - date1.getTime()) / 1000;
    }

    public String format(String language)
    {
        StringBuffer buffer = new StringBuffer();
        long tmp = dateDiff;

        long days = tmp / SECOND_IN_DAY;
        tmp -= (days * SECOND_IN_DAY);

        long hours = tmp / SECOND_IN_HOUR;
        tmp -= (hours * SECOND_IN_HOUR);

        long minutes = tmp / SECOND_IN_MINUT;
        long seconds = tmp - (minutes * SECOND_IN_MINUT);

        buffer.append(days);
        buffer.append("dd ");

        buffer.append(hours);
        buffer.append("h ");

        buffer.append(minutes);
        buffer.append("m ");

        buffer.append(seconds);
        buffer.append("s");

        return buffer.toString();
    }
}
