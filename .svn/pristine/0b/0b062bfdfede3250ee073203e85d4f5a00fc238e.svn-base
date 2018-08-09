package com.carel.supervisor.controller.time;

import com.carel.supervisor.base.conversion.StringUtility;


public class HourChain
{
    private int minutesTotMin = 0;
    private int minutesTotMax = 0;
    private HourChain next = null;

    //13:30-14:30;15:30-18:30
    public HourChain(String data)
    {
        String token = data;
        int pos = data.indexOf(';');

        if (-1 != pos)
        {
            token = data.substring(0, pos);
        }

        String[] s = StringUtility.split(token, "-");
        String[] first = StringUtility.split(s[0].trim(), ":");
        String[] second = StringUtility.split(s[1].trim(), ":");
        int hourMin = Integer.parseInt(first[0].trim());
        int minutesMin = Integer.parseInt(first[1].trim());
        int hourMax = Integer.parseInt(second[0].trim());
        int minutesMax = Integer.parseInt(second[1].trim());
        minutesTotMax = (hourMax * 60) + minutesMax;
        minutesTotMin = (hourMin * 60) + minutesMin;
        if (minutesTotMax == minutesTotMin)
        {
        	minutesTotMax = minutesTotMax + 5; //Per fasce con strat e end uguali. Operazione one-shot 
        }
        if (-1 != pos)
        {
            String nextToken = data.substring(pos + 1);
            next = new HourChain(nextToken);
        }
    }

    public HourChain(int hourMin, int minutesMin, int hourMax, int minutesMax)
    {
        minutesTotMax = (hourMax * 60) + minutesMax;
        minutesTotMin = (hourMin * 60) + minutesMin;
    }

    public boolean isValid(int hour, int minutes)
    {
        boolean code = false;
    	int time = (hour * 60) + minutes;

        if ((time <= minutesTotMax) && (time >= minutesTotMin))
        {
            code = true;
        }
        else
        {
	        if (null != next)
	        {
	        	code = next.isValid(hour, minutes);
	        }
        }
        return code;
    }

    public HourChain next()
    {
        return this.next;
    }

    public void setNext(HourChain next)
    {
        this.next = next;
    }

    //  13:30-14:30;13:30-14:30
    public StringBuffer getData()
    {
        StringBuffer buffer = new StringBuffer();
        int hourMin = minutesTotMin / 60;
        int minutesMin = minutesTotMin % 60;
        int hourMax = minutesTotMax / 60;
        int minutesMax = minutesTotMax % 60;
        buffer.append(hourMin);
        buffer.append(":");
        buffer.append(minutesMin);
        buffer.append("-");
        buffer.append(hourMax);
        buffer.append(":");
        buffer.append(minutesMax);

        if (null != next)
        {
            buffer.append(";");
            buffer.append(next.getData());
        }

        return buffer;
    }
}
