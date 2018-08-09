package com.carel.supervisor.controller.time;

import com.carel.supervisor.base.conversion.StringUtility;


public class YearChain
{
    private HourChain hourChain = null;
    private int dayMin = 0;
    private int yearMin = 0;
    private int monthMin = 0;
    private int dayMax = 0;
    private int yearMax = 0;
    private int monthMax = 0;
    private YearChain next = null;

    //  1/10/2006|13:30-14:30;15:30-18:30,2/10/2006-11/12/2006|13:30-14:30;15:30-18:30
    //  1/10|13:30-14:30;15:30-18:30,2/10-9/12|13:30-14:30;15:30-18:30
    public YearChain(String data)
    {
    	String token = data;
        int pos = data.indexOf(',');

        if (-1 != pos)
        {
            token = data.substring(0, pos);
        }

        String[] s = StringUtility.split(token, "|");

        if (-1 == s[0].indexOf('-')) //No interval
        {
        	
        	String[] token2 = StringUtility.split(s[0], "/");

            this.dayMin = Integer.parseInt(token2[0].trim());
            this.monthMin = Integer.parseInt(token2[1].trim());

            if (token2.length > 2)
            {
                this.yearMin = Integer.parseInt(token2[2].trim());
            }
        	
            this.dayMax = this.dayMin;
            this.monthMax = this.monthMin;
            this.yearMax = this.yearMin;
        }
        else //Interval
        {
            String[] token3 = StringUtility.split(s[0], "-");
            
            String[] token2 = StringUtility.split(token3[0].trim(), "/");
            this.dayMin = Integer.parseInt(token2[0].trim());
            this.monthMin = Integer.parseInt(token2[1].trim());

            if (token2.length > 2)
            {
                this.yearMin = Integer.parseInt(token2[2].trim());
            }
        	
            token2 = StringUtility.split(token3[1], "/");
            this.dayMax = Integer.parseInt(token2[0].trim());
            this.monthMax = Integer.parseInt(token2[1].trim());

            if (token2.length > 2)
            {
                this.yearMax = Integer.parseInt(token2[2].trim());
            }
        }

        hourChain = new HourChain(s[1]);

        if (-1 != pos)
        {
            String nextToken = data.substring(pos + 1);
            next = new YearChain(nextToken);
        }	
    }

    public YearChain(int dayMin, int monthMin, int yearMin, int dayMax, int monthMax, int yearMax, HourChain hourChain)
    {
        this.dayMin = dayMin;
        this.monthMin = monthMin;
        this.yearMin = yearMin;
        this.dayMax = dayMax;
        this.monthMax = monthMax;
        this.yearMax = yearMax;
        this.hourChain = hourChain;
    }

    public boolean isValid(int year, int month, int day, int hour, int minutes)
    {
    	boolean code = false;
    	if ((this.dayMin <= day) && (this.dayMax >= day) && 
    		(this.monthMin <= month) && (this.monthMax >= month) &&
    		(this.yearMin <= year) && (this.yearMax >= year))
        {
            if (hourChain.isValid(hour, minutes))
            {
                code = true;
            }
            else if (null != next)
            {
                code = next.isValid(year, month, day, hour, minutes);
            }	
        }
    	else if (null != next)
        {
            code = next.isValid(year, month, day, hour, minutes);
        }
        return code;
    }

    //  1/10/2006|13:30-14:30;15:30-18:30,2/10/2006-11/12/2006|13:30-14:30;15:30-18:30
    //  1/10|13:30-14:30;15:30-18:30,2/10-9/12|13:30-14:30;15:30-18:30
    public StringBuffer getData()
    {
    	
    	StringBuffer buffer = new StringBuffer();

    	buffer.append(dayMin);
        buffer.append("/");
        buffer.append(monthMin);

        if (0 != yearMin)
        {
            buffer.append("/");
            buffer.append(yearMin);
        }
        
        if ((dayMax != dayMin) || (monthMax != monthMin) || (yearMax != yearMin))
        {
        	buffer.append("-");
        	buffer.append(dayMax);
        	buffer.append("/");
            buffer.append(monthMax);

            if (0 != yearMax)
            {
                buffer.append("/");
                buffer.append(yearMax);
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
