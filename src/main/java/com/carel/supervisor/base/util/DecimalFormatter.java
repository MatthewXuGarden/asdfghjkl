package com.carel.supervisor.base.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

import com.carel.supervisor.dataaccess.datalog.impl.ReportExportConfigList;

public class DecimalFormatter 
{
	public static DecimalFormat getCSVFormatter()
	{
		ReportExportConfigList conf = new ReportExportConfigList();
		Map<String,String> map = conf.loadMap();
		
		String decimalLength = map.get(ReportExportConfigList.DECIMAL_LENGTH);
		Integer decimal = null;
		if(decimalLength != null && decimalLength.length()>0)
			decimal = Integer.valueOf(decimalLength);
		String groupSeparator = map.get(ReportExportConfigList.GROUP_SEPARATOR);
		groupSeparator = groupSeparator == null || groupSeparator.length()==0?null:groupSeparator;
		String decimalSeparator = map.get(ReportExportConfigList.DECIMAL_SEPARATOR);
		decimalSeparator = decimalSeparator == null || decimalSeparator.length()==0?".":decimalSeparator;
		String format = "";
		if(groupSeparator != null && groupSeparator.length()>0)
			format = "#,##0.";
		else
			format += "###0.";
		if(decimal == null)
			format += "######";
		else if(decimal>0)
			for(int i=0;i<decimal;i++)
			{
				format += "0";
			}
		else if(decimal  == 0)
			format = format.substring(0,format.length()-1);
		Locale currentLocale = new Locale("en", "US"); 
		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(currentLocale); 
		if(decimalSeparator != null && decimalSeparator.length()>0)
			unusualSymbols.setDecimalSeparator(decimalSeparator.toCharArray()[0]);
		if(groupSeparator != null && groupSeparator.length()>0)
			unusualSymbols.setGroupingSeparator(groupSeparator.toCharArray()[0]); 
		DecimalFormat formatter = new DecimalFormat(format, unusualSymbols); 
		formatter.setGroupingSize(3); 
		return formatter; 
	}
	public static DecimalFormat getCSVFormatterNoDecimal()
	{
		ReportExportConfigList conf = new ReportExportConfigList();
		Map<String,String> map = conf.loadMap();
		String groupSeparator = map.get(ReportExportConfigList.GROUP_SEPARATOR);
		groupSeparator = groupSeparator == null || groupSeparator.length()==0?null:groupSeparator;
		String decimalSeparator = map.get(ReportExportConfigList.DECIMAL_SEPARATOR);
		decimalSeparator = decimalSeparator == null || decimalSeparator.length()==0?".":decimalSeparator;
		String format = "";
		if(groupSeparator != null && groupSeparator.length()>0)
			format = "#,##0";
		else
			format += "###0";
		Locale currentLocale = new Locale("en", "US"); 
		DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(currentLocale); 
		if(groupSeparator != null && groupSeparator.length()>0)
			unusualSymbols.setGroupingSeparator(groupSeparator.toCharArray()[0]); 
		DecimalFormat formatter = new DecimalFormat(format, unusualSymbols); 
		formatter.setGroupingSize(3); 
		return formatter; 
	}
	public static String getValue(DecimalFormat formatter,DecimalFormat nodecimalFormatter,String value)
	{
		DecimalFormat f = formatter;
		if("***".equals(value) )
			return value;
		else if("NaN".equals(value))
			return value;
		//Kevin, if it is digital or integer, will not apply DecimalFormat
		if(value.indexOf(".")<0 || value.endsWith(".0"))
			f = nodecimalFormatter;
		try
		{
			Double dValue = Double.valueOf(value);
			return f.format(dValue);
		}
		catch(Exception ex)
		{
			return value;
		}
	}
}
