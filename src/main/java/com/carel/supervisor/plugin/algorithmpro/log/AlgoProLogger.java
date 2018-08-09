package com.carel.supervisor.plugin.algorithmpro.log;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class AlgoProLogger 
{
	public static int DEBUG = org.apache.log4j.Level.DEBUG_INT;
	public static int INFO = org.apache.log4j.Level.INFO_INT;
	public static int WARN = org.apache.log4j.Level.WARN_INT;
	public static int ERORR = org.apache.log4j.Level.ERROR_INT;
	
	private org.apache.log4j.Logger logger = null;
	private String dimensione = "1000KB";
	private int maxFileNum = 3;
	
	public AlgoProLogger(String fileName)
	{
		Layout layout = new PatternLayout("%d %5p [%t] - %m%n");
		RollingFileAppender appender = null;
		try
        {
            appender = new RollingFileAppender(layout, fileName,true);
            appender.setMaxFileSize(dimensione);
            appender.setMaxBackupIndex(maxFileNum);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        
        logger = org.apache.log4j.Logger.getLogger(fileName);
        logger.addAppender(appender);
        logger.setLevel(Level.INFO);
	}
	
	public void writeLog(Object message,int level)
	{
		switch (level)
        {
        	case org.apache.log4j.Level.DEBUG_INT:
        		this.logger.debug(message);
        		break;

        	case org.apache.log4j.Level.INFO_INT:
        		this.logger.info(message);
        		break;

        	case org.apache.log4j.Level.WARN_INT:
        		this.logger.warn(message);
        		break;

        	case org.apache.log4j.Level.ERROR_INT:
        		this.logger.error(message);
        		break;
        }
	}
}
