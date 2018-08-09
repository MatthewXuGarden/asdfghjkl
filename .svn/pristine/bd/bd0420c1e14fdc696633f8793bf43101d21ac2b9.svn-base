package com.carel.supervisor.presentation.sdk.util;

import java.text.DecimalFormat;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.presentation.helper.ServletHelper;

public class Sfera 
{
    public static String assint(float value, String decod,String off)
    {
        String ret = "";
        int idx = -1;
        
        if(!Float.isNaN(value))
            idx = (int)value;
        
        String[] elem = StringUtility.split(decod,";");
        if(elem != null)
        {
            try {
                ret = elem[idx];
            }
            catch(Exception e){
                ret = off;
            }
        }
        return ret;
    }
    
    public static String assint(String value, String decod, String off) 
    {
        float f = Float.NaN;
        try {
            if(value != null && value.equalsIgnoreCase("***"))
                f = -1;
            else
                f = Float.parseFloat(value);
        } 
        catch(Exception e){
        }
        return assint(f,decod,off);
    }
    
    public static String assint(String value, String decod) 
    {
        return assint(value,decod,""); 
    }
    
    public static String assint(float value, String decod) 
    {
        return assint(value,decod,"");
    }
    
    public static String format(String value,String pattern) 
    {
        float f = Float.NaN;
        try {
            f = Float.parseFloat(value);
            return format(f, pattern);
        } 
        catch(Exception e){}
        return value;
    }
    
    public static String format(float value,String pattern)
    {
        DecimalFormat format = new DecimalFormat();
        try
        {
	        if(pattern.startsWith("+"))
	        {
	        	format.setNegativePrefix("-");
	        	format.setPositivePrefix("+");
	        	pattern = pattern.substring(1);
	        }
	        else
	        {
	        	format.setNegativePrefix("-");
	        }
	        
	        if(pattern.endsWith("g"))
	        {
	        	format.setGroupingUsed(true);
	        	pattern = pattern.substring(0,pattern.length()-1);
	        }
	        else
	        {
	        	format.setGroupingUsed(false);
	        }

	        if(pattern.charAt(0)=='0')
	        {
	        	int a = pattern.indexOf(".");
	        	if(a<=1)
	        	{
	        		return "***";
	        	}
	        	String s1 = pattern.substring(1, a);
	        	format.setMinimumIntegerDigits(Integer.parseInt(s1));        	
	        }
	        
	        if(pattern.endsWith("f"))
	        {
	        	pattern = pattern.substring(0,pattern.length()-1);
	        	String s2 = pattern.substring(pattern.indexOf(".")+1,pattern.length());
	        	int b = Integer.parseInt(s2);
	        	format.setMinimumFractionDigits(b);
	        	format.setMaximumFractionDigits(b);
	        }
	        else
	        {
	        	format.setMinimumFractionDigits(0);
	        	format.setMaximumFractionDigits(0);
	        }
	        return format.format(value);
        }
        catch(Exception exc)
        {
        	return "***";
        }
    }
    
    /**
     * SINCE PVP 2.0
     * @param value
     * @param pattern
     * @return
     */
    public static String formatValue(String value,String pattern)
    {
    	String ret = "***";
    	float f = Float.NaN;
        try 
        {
            f = Float.parseFloat(value);
            return formatValue(f, pattern);
        } 
        catch(Exception e){
        	ret = "***";
        }
    	return ret;
    }
    
    /**
     * SINCE PVP 2.0
     * @param value
     * @param pattern
     * @return
     */
    public static String formatValue(float value,String pattern)
    {
    	String ret = "***";
    	try
    	{
    		DecimalFormat format = new DecimalFormat(pattern);
    		ret = format.format(value);
    	}
    	catch(Exception e){
    		ret = "***";
    	}
    	return ret;
    }
    /*
     * 0:run
     * 1:debug
     * 2:stopped
     * 3:must restart
     */
    public static int getEngineStatus()
    {
    	return ServletHelper.messageToNotify();
    }
}
