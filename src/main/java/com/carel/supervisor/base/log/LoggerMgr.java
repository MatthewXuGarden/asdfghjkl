package com.carel.supervisor.base.log;

import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;


public final class LoggerMgr
{
    private static HashMap<String, Logger> loggers = new HashMap<String, Logger>();
    private static Logger rootLogger = null;
    private static boolean initialized = false;
    public static final LEVEL DEBUG = new LEVEL(org.apache.log4j.Level.DEBUG);
    public static final LEVEL INFO = new LEVEL(org.apache.log4j.Level.INFO);
    public static final LEVEL WARN = new LEVEL(org.apache.log4j.Level.WARN);
    public static final LEVEL ERROR = new LEVEL(org.apache.log4j.Level.ERROR);
    public static final LEVEL FATAL = new LEVEL(org.apache.log4j.Level.FATAL);

    public static synchronized void initialize(String fileNameLog)
    {
        org.apache.log4j.BasicConfigurator.resetConfiguration();

        Layout layout = new PatternLayout("%d %5p [%t] - %m%n");

        RollingFileAppender appender = null;

        try
        {
            //appender = new DailyRollingFileAppender(layout, fileNameLog, "'.'yyyy-MM-dd");
            appender = new RollingFileAppender(layout, fileNameLog, true);
            // max size of each log file
            appender.setMaxFileSize("10MB");
            // max number of log files
            appender.setMaxBackupIndex(20);
        }
        catch (Exception e)
        {
            e.printStackTrace();
//            appender = new ConsoleAppender(layout);
        }

        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getRootLogger();
        logger.addAppender(appender);
        logger.setLevel(Level.INFO);
        
        initialized = true;
    }

    public static synchronized void initialize(Properties properties)
    {
        PropertyConfigurator.configure(properties);
        initialized = true;
    }

    private static void requireInit()
    {
    	if (!initialized)
    	{
    		initialize("Carel.log");
    	}
    }
    
    protected static LEVEL getLevel(org.apache.log4j.Level level)
    {
        LEVEL l = null;

        switch (level.toInt())
        {
        case org.apache.log4j.Level.DEBUG_INT:
            l = DEBUG;

            break;

        case org.apache.log4j.Level.INFO_INT:
            l = INFO;

            break;

        case org.apache.log4j.Level.WARN_INT:
            l = WARN;

            break;

        case org.apache.log4j.Level.ERROR_INT:
            l = ERROR;

            break;

        case org.apache.log4j.Level.FATAL_INT:
            l = FATAL;

            break;

        default:
            l = null;

            break;
        }

        return l;
    }

    public static synchronized Logger getRootLogger()
    {
        requireInit();

        if (null == rootLogger)
        {
            rootLogger = new Logger();
        }

        return rootLogger;
    }

    public static synchronized Logger getLogger(String selector)
    {
        requireInit();

        Logger oLogger = loggers.get(selector);

        if (oLogger == null)
        {
            oLogger = new Logger(selector);
            loggers.put(selector, oLogger);
        }

        return oLogger;
    }

    public static Logger getLogger(Class<?> className)
    {
        return getLogger(className.getName());
    }

    @SuppressWarnings("unchecked")
	public static final class LEVEL implements Comparable
    {
        protected org.apache.log4j.Level apacheLevel = null;

        private LEVEL(org.apache.log4j.Level apacheLevelPar)
        {
            apacheLevel = apacheLevelPar;
        }

        public int compareTo(Object o)
        {
            if (this == o)
            {
                return 0;
            }

            org.apache.log4j.Level al = ((LEVEL) o).apacheLevel;

            if (apacheLevel.equals(al))
            {
                return 0;
            }

            if (apacheLevel.isGreaterOrEqual(al))
            {
                return 1;
            }

            return -1;
        }

        public boolean equals(Object o)
        {
            return (compareTo(o) == 0);
        }
        

        @Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((apacheLevel == null) ? 0 : apacheLevel.hashCode());
			return result;
		}

		

		public String toString()
        {
            String s = "Unspecified";

            if (this == LoggerMgr.DEBUG)
            {
                s = "DEBUG";
            }
            else if (this == LoggerMgr.INFO)
            {
                s = "INFO";
            }
            else if (this == LoggerMgr.WARN)
            {
                s = "WARN";
            }
            else if (this == LoggerMgr.ERROR)
            {
                s = "ERROR";
            }
            else if (this == LoggerMgr.FATAL)
            {
                s = "FATAL";
            }

            return s;
        }
    }
}
