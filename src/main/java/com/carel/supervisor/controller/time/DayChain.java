package com.carel.supervisor.controller.time;

import com.carel.supervisor.base.conversion.StringUtility;


public class DayChain
{
    private HourChain hourChain = null;
    private int[] day = null;
    private DayChain next = null;

    //  1+10|13:30-14:30;15:30-18:30,13|13:30-14:30;15:30-18:30
    //Solo 1, 10 e 13
    public DayChain(String data)
    {
        String token = data;
        int pos = data.indexOf(',');

        if (-1 != pos)
        {
            token = data.substring(0, pos);
        }

        String[] s = StringUtility.split(token, "|");

        if (-1 == s[0].indexOf('+'))
        {
        	day = new int[1];
        	day[0] = Integer.parseInt(s[0].trim());   
        }
        else
        {
            String[] days = StringUtility.split(s[0], "+");
            day = new int[days.length];
            for(int i = 0; i < days.length; i++)
            {
            	day[i] = Integer.parseInt(days[i].trim());
            }
        }

        hourChain = new HourChain(s[1]);

        if (-1 != pos)
        {
            String nextToken = data.substring(pos + 1);
            next = new DayChain(nextToken);
        }
    }

    public boolean isValid(int day, int hour, int minutes)
    {
        boolean code = false;
    	if (check(day))
        {
            if (hourChain.isValid(hour, minutes))
            {
                code = true;
            }
            else if (null != next)
            {
                code = next.isValid(day, hour, minutes);
            }	
        }
    	else if (null != next)
        {
            code = next.isValid(day, hour, minutes);
        }
        return code;
    }

    private boolean check(int dayActual)
    {
    	for (int i = 0; i < day.length; i++)
    	{
    		if (day[i] == dayActual)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * @return: MonthChain
     */
    public DayChain next()
    {
        return next;
    }

    /**
     * @param next
     */
    public void setNext(DayChain next)
    {
        this.next = next;
    }

    //  1+10|13:30-14:30;15:30-18:30,13|13:30-14:30;15:30-18:30
    public StringBuffer getData()
    {
        StringBuffer buffer = new StringBuffer();

        for(int i = 0; i < day.length; i++)
        {
        	buffer.append(day[i]);
            if (i < day.length - 1)
            {
            	buffer.append("+");
            }	
        }
        buffer.append("|");
        buffer.append(hourChain.getData());

        if (null != next)
        {
            buffer.append(",");
            buffer.append(next.getData());
        }

        return buffer;
    }
}
