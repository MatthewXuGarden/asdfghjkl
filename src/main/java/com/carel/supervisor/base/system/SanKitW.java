package com.carel.supervisor.base.system;
import java.io.*;
import java.util.HashMap;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.script.ScriptInvoker;


// execute SanKitW and parse its output
public class SanKitW {
	private static SanKitW instance = new SanKitW();
	private static String exeFileName = "tools\\SanKit\\Win\\sankitW.exe";
	private static String logFileName = "tools\\SanKit\\Win\\sankitW.log";
	private static String curDirectory = "Services\\manual\\logs";
	private static String exeCmdSwitch = "-sa";
	private HashMap<String, SanKitAttr> attributes = new HashMap<String, SanKitAttr>();
	
	
	private SanKitW()
	{
	}
	
	
	public static SanKitW getInstance()
	{
		return instance;
	}
	
	
	public synchronized void execute()
	{
		LoggerMgr.getLogger(this.getClass()).info("execute SSD statistics");
		ScriptInvoker script = new ScriptInvoker();
		script.setCurrentDirectory(BaseConfig.getCarelPath() + curDirectory);
		try {
			script.execute(new String[] { BaseConfig.getCarelPath() + exeFileName
				, exeCmdSwitch }
				, BaseConfig.getCarelPath() + logFileName
			);
			parseAttributes();
			logAttributes();
		} catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public synchronized SanKitAttr getAttribute(String id)
	{
		return attributes.get(id);
	}

	
	public synchronized Integer getSpareBlocksRemaining()
	{
		SanKitAttr attribute = attributes.get("232"); // % of Spare Blocks Remaining
		if( attribute != null )
			return attribute.value;
		else
			return null;
	}
	

	public synchronized Integer getPowerOnHoursCount()
	{
		SanKitAttr attribute = attributes.get("009"); // Power-On Hours Count
		if( attribute != null )
			return Integer.parseInt(attribute.raw_value);
		else
			return null;
	}
	
	
	public synchronized Integer getTotalLBAsWritten()
	{
		SanKitAttr attribute = attributes.get("241"); // Total LBA's Written
		if( attribute != null )
			return Integer.parseInt(attribute.raw_value);
		else
			return null;
	}

	
	private void parseAttributes()
	{
		if( !attributes.isEmpty() )
			attributes.clear();
		
		File log = new File(BaseConfig.getCarelPath() + logFileName);
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(log)));
			String line = null;
			while( (line = reader.readLine()) != null ) {
				String[] astr = line.split("\\|");
				if( astr.length == 7 ) {
					int i = 0;
					try {
						Integer.parseInt(fixNumber(astr[i].trim())); // id is a number for a valid attribute
						try {
							SanKitAttr attribute	= new SanKitAttr();
							attribute.id			= astr[i++].trim();
							attribute.description	= astr[i++].trim();
							attribute.flag			= Integer.parseInt(fixNumber(astr[i++].trim()), 16);
							attribute.value			= Integer.parseInt(fixNumber(astr[i++].trim()));
							attribute.worst			= Integer.parseInt(fixNumber(astr[i++].trim()));
							attribute.thres			= Integer.parseInt(fixNumber(astr[i++].trim()));
							attribute.raw_value		= astr[i++].trim();
							attributes.put(attribute.id, attribute);
						} catch(NumberFormatException e) {
							LoggerMgr.getLogger(this.getClass()).error(e);
						}
					} catch(NumberFormatException e) {
						// invalid attribute
					}
				}
			}
			reader.close();
		} catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);			
		}
		
		log.delete();
	}
	
	
	private void logAttributes()
	{
		if( attributes.isEmpty() ) {
			LoggerMgr.getLogger(this.getClass()).info("unable to parse attributes from SSD statistics");
		}
		else {
			SanKitAttr attribute = null;
			attribute = attributes.get("232"); // % of Spare Blocks Remaining
			if( attribute != null )
				LoggerMgr.getLogger(this.getClass()).info(attribute.description + " = " + attribute.value);
			attribute = attributes.get("009"); // Power-On Hours Count
			if( attribute != null )
				LoggerMgr.getLogger(this.getClass()).info(attribute.description + " = " + attribute.raw_value);
			attribute = attributes.get("241"); // Total LBA's Written
			if( attribute != null )
				LoggerMgr.getLogger(this.getClass()).info(attribute.description + " = " + attribute.raw_value);
		}
	}
	

	private static String fixNumber(String str)
	{
		while( str.length() > 1
			&& (str.startsWith("0") || str.startsWith("x")) )
			str = str.substring(1);
		return str;
	}
}
