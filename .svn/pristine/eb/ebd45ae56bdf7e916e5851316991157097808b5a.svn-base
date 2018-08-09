package com.carel.supervisor.base.config;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.*;
import com.carel.supervisor.presentation.bo.helper.LineConfig;

import java.io.File;
import java.net.*;
import java.util.*;

public class BaseConfig
{
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(
            "com.carel.supervisor.base.config.supervisor");
    public static final String COMPANY_NAME = resourceBundle.getString("COMPANY_NAME");
    public static final String PRODUCT_NAME = resourceBundle.getString("PRODUCT_NAME");
    public static final String PRODUCT_VERSION = resourceBundle.getString("PRODUCT_VERSION");
    public static final String INITIAL_PROP_FILE = resourceBundle.getString("INITIAL_PROPERTY_FILE");
    public static final String LOW_LEVEL_DEBUG = resourceBundle.getString("LOW_LEVEL_DEBUG");
    public static final String PLANT_ID = resourceBundle.getString("PLANT_ID");
    public static final String ADVANCED_TYPE = "advanced";
    private static final String HARDWARE_PROPERTY_FILE = resourceBundle.getString("HARDWARE_PROPERTY_FILE");
    private static final String HWVERSION_FILE = resourceBundle.getString("HWVERSION_FILE");
    private static final String ARCH_FILE = resourceBundle.getString("ARCH_FILE");
    private static final String ARCH_MESSAGES = resourceBundle.getString("ARCH_MESSAGES");
    private static final String PVPRO_HOME = "PVPRO_HOME";
    private static final String POSTGRES_HOME = "PGHOME";
    private static final String CATALINA_HOME = "CATALINA_HOME";
    private static final String LIVE_TIME = "livetime";
    // external file places; used by FileDialog
    private static Properties places = new Properties();
    //private static final String USB_PLACE = "PVPRO_USB";
    //private static final String CFCARD_PLACE = "PVPRO_CFCARD";
    private static final String FTP_PLACE = "PVPRO_FTP";
    //private static final String NET_PLACE = "PVPRO_NETFOLDER";
    // demo variable
    private static final String DEMO = "PVPRO_DEMO";
    private static boolean demo = false;
    private static boolean debug = false;
    private static String plantId = null;
    private static long session = 1800000L; //timeout di sessione impostato a 30 minuti
    private static int liveTime = 900;
    private static boolean blockingError = false;
    private static Exception error = null;
    private static String path = null;
    private static Properties properties = null;
    private static Logger logger = null;
    private static IProductInfo productInfo = null;
    private static String logFile = "";
    private static String appHome = System.getenv(CATALINA_HOME) + File.separator + "webapps"+File.separator+"PlantVisorPRO"+File.separator;
    private static String appLogPath = System.getenv(CATALINA_HOME) + File.separator + "logs";
    private static String postgresHome = ""; 
    private static String temporaryFolder="PvPro"+File.separator+"TempExports";
    
    private static Properties propertiesHardware = null;
    private static Properties propertiesHWVersion = null;

    private BaseConfig()
    {
    }

    public final static void init() throws Exception
    {
        debug = (null != System.getProperty(LOW_LEVEL_DEBUG));

        path = System.getenv(PVPRO_HOME);

        if (null == path)
        {
            path = System.getProperty(PVPRO_HOME);
        }

        if (null == path)
        {
            path = "C:\\Carel\\"; //Solo per Windows
        }

        if (!path.endsWith(File.separator))
        {
        	path = path + File.separator;
        }
        logFile = path + "log" + File.separator + "Carel.log";
        LoggerMgr.initialize(logFile);

        
        try
        {
        	postgresHome = System.getenv(POSTGRES_HOME);
        	if(postgresHome == null)
        		postgresHome = "C:\\Program Files\\PostgreSQL\\8.2";
        	postgresHome += File.separator;
        }
        catch (Exception e)
        {
        	postgresHome = "C:\\Program Files\\PostgreSQL\\8.2"+ File.separator;
        }
        
        
        try
        {
            ResourceLoader.init();

            URL url = ResourceLoader.fromResource(INITIAL_PROP_FILE, path);
            properties = new Properties();
            properties.load(url.openStream());
            plantId = properties.getProperty(PLANT_ID);
            liveTime = Integer.parseInt(properties.getProperty(LIVE_TIME, "900"));
        }
        catch (Exception e)
        {
            logger = LoggerMgr.getLogger(BaseConfig.class);
            logger.fatal("Error in initial Property File", e);
            blockingError = true;
            error = e;
        }

        try
        {
            propertiesHardware = new Properties();
            URL url = ResourceLoader.fromResource(HARDWARE_PROPERTY_FILE, "c:\\");
            propertiesHardware.load(url.openStream());
        }
        catch (Exception e)
        {
            logger = LoggerMgr.getLogger(BaseConfig.class);
            logger.fatal("Error in initial Property File", e);
        }

        try
        {
        	propertiesHWVersion = new Properties();
        	URL url = ResourceLoader.fromResource(HWVERSION_FILE, "c:\\");
        	propertiesHWVersion.load(url.openStream());
        }
        catch(Exception e)
        {
        	logger = LoggerMgr.getLogger(BaseConfig.class);
        	logger.fatal("Error in initial c:\\HWversion.ini File",e);
        }
        // custom license, test only
        /*
        if( propertiesHardware != null ) {
        	String strProductCode = propertiesHardware.getProperty("product");
        	if( strProductCode != null && !strProductCode.isEmpty() ) {
        		String strCustomLicense = "com.carel.supervisor.director.customlicense."
        			+ strProductCode + "License";
        		try {
        			ClassLoader classLoader = BaseConfig.class.getClassLoader();
        			Class classCustomLicense = classLoader.loadClass(strCustomLicense);
        			classCustomLicense.getMethod("setLicense", new Class[] {}).invoke(classCustomLicense, new Object[] {});
        		}
        		catch(ClassNotFoundException e) {
        			LoggerMgr.getLogger(BaseConfig.class).info("no custom license defined for product " + strProductCode);
        		}
        		catch(Exception e) {
        			LoggerMgr.getLogger(BaseConfig.class).error(e);        			
        		}
        	}
        }
        */
        // obtain information about COM associated to RS485 serial lines
        try {
        	LineConfig.initRS485COMPorts();
        }
        catch(Exception e)
        {
        	logger = LoggerMgr.getLogger(BaseConfig.class);
            logger.error("Error during RS485 COM ports retrieve");
        }
        // demo
        try {
        	// demo mode it is allowed only on regular PCs
        	// no embedded RS485 means a regular PC
        	boolean bPC = LineConfig.getSerial485COM().isEmpty() // no InfoService 
        		|| (LineConfig.getSerial485COM().size() == 1 && LineConfig.getSerial485COM().get(new Integer(-1)) != null); // with InfoService
        	String strDemo = System.getenv(DEMO);
        	demo = (strDemo.equalsIgnoreCase("true") || strDemo.equals("1")) && bPC; 
        } catch(Exception e) {
        	demo = false;
        }
        
        if (!blockingError)
        {
            try
            {
                URL url = ResourceLoader.fromResource(properties.getProperty(ARCH_MESSAGES), path);
                Properties propertiesTmp = new Properties();
                propertiesTmp.load(url.openStream());
                CoreMessages.init(propertiesTmp);
            }
            catch (Exception e)
            {
                logger = LoggerMgr.getLogger(BaseConfig.class);
                logger.fatal("Error in Property Arch Message File", e);
                blockingError = true;
                error = e;
            }
        }

        if (!blockingError) //Carichiamo il file di configurazione architetturale SupervisorConfig.XML
        {
            try
            {
                String fileText = properties.getProperty(ARCH_FILE);
                URL urlService = ResourceLoader.fromResource(fileText, path);

                //File file = new File(urlService.getFile());
                XMLNode xmlNode = XMLNode.parse(urlService.openStream());
                ManagerLoader.load(xmlNode);

                //FileUtil.replaceFile(file, xmlNode.toString());
            }
            catch (Exception e)
            {
                logger = LoggerMgr.getLogger(BaseConfig.class);
                logger.fatal("Error in config File", e);
                blockingError = true;
                error = e;
            }
        }

        if (!blockingError) //Carichiamo le informazioni relative al prodotto
        {
            try
            {
                productInfo = ProductInfoMgr.getInstance().getProductInfo();
               try{ 
                session = Long.parseLong(productInfo.get("session"))*1000*60; //Imposto il timeout di sessione (espresso in minuti)
               }
                catch (NumberFormatException e) {
					session = 1800000L;
				}
            }
            catch (Exception e)
            {
                logger = LoggerMgr.getLogger(BaseConfig.class);
                logger.fatal("Error in Product info", e);
                blockingError = true;
                error = e;
            }
        }
        
        // external file places
        /*
        try {
        	places.setProperty("USB", System.getenv(USB_PLACE));
        } catch(Exception e) {
            logger = LoggerMgr.getLogger(BaseConfig.class);
            logger.warn(USB_PLACE + " environment variable not defined");
        }
        try {
        	places.setProperty("CFCARD", System.getenv(CFCARD_PLACE));
        } catch(Exception e) {
            logger = LoggerMgr.getLogger(BaseConfig.class);
            logger.warn(CFCARD_PLACE + " environment variable not defined");
        }
        */
        try {
        	places.setProperty("FTP", System.getenv(FTP_PLACE));
        } catch(Exception e) {
        	places.setProperty("FTP", "C:\\ftp");
            logger = LoggerMgr.getLogger(BaseConfig.class);
            logger.warn(FTP_PLACE + " environment variable not defined, C:\\ftp used by default");
        }
        /*
        try {
        	places.setProperty("NETFOLDER", System.getenv(NET_PLACE));
        } catch(Exception e) {
            logger = LoggerMgr.getLogger(BaseConfig.class);
            logger.warn(NET_PLACE + " environment variable not defined");
        }
        */
        
        if (blockingError)
        {
            throw error;
        }
    }

    public final static boolean isDebug()
    {
        return debug;
    }

    public final static String getPlantId()
    {
        return plantId;
    }

    public final static String getProperty(String key)
    {
        return properties.getProperty(key);
    }
    public final static String getHardwareProperty(String key)
    {
    	if(propertiesHardware != null)
    	{
    		return propertiesHardware.getProperty(key);
    	}
    	return null;
    }
    public final static String getProductInfo(String key)
    {
        return productInfo.get(key);
    }

    public static String getAppHome()
    {
    	String webappFolder = productInfo.get("webappfolder");
    	
    	if(webappFolder!= null && !webappFolder.equals(""))
    		return System.getenv(CATALINA_HOME) + File.separator + "webapps"+File.separator+webappFolder+File.separator;
    	else
    		return appHome;
    }
    
    public static String getPGHome()
    {
    	return postgresHome;
    }

    public static int getLiveTime()
    {
        return liveTime;
    }

    public static String getCarelPath()
    {
        return path;
    }

    public static String getLogFile()
    {
        return logFile;
    }
    
    public static String getAppLogPath()
    {
    	return appLogPath;
    }

	public static long getSession() {
		return session;
	}

	public static void setSession(long session) {
		BaseConfig.session = session;
	}
	
	public static Properties getFilePlaces() {
		return places;
	}

	public static String getTemporaryFolder() {
		return temporaryFolder;
	}
	
	public static boolean isDemo()
	{
		return demo;
	}
	
	public static String getProductCode()
	{
        if( propertiesHardware != null ) {
        	String strProductCode = propertiesHardware.getProperty("product");
        	if( strProductCode != null && !strProductCode.isEmpty() )
        		return strProductCode;
        }
        	
        return null;
	}
	public static String getReleaseHW()
	{
		if(propertiesHWVersion != null)
		{
			return propertiesHWVersion.getProperty("ReleaseHW")!=null?propertiesHWVersion.getProperty("ReleaseHW"):"";
		}
		return null;
	}
	public static String getHardwareVersion()
	{
		if(propertiesHWVersion != null)
		{
			StringBuffer buffer = new StringBuffer(); 
			buffer.append(propertiesHWVersion.getProperty("ReleaseHW")!=null?propertiesHWVersion.getProperty("ReleaseHW"):"");
			boolean isFirst = true;
			for(int i=1;i<100;i++)
			{
				String v = i<10?"0"+i:String.valueOf(i);
				if(propertiesHWVersion.containsKey("[upgrade"+v+"]"))
				{
					if(isFirst)
					{
						buffer.append("[");
						isFirst = false;
					}
					buffer.append("HardwareUpgrade"+v+",");
				}
			}
			String result = buffer.toString();
			if(!isFirst)
			{
				result = result.substring(0,result.length()-1)+"]";
			}
			return result;
		}
		return null;
	}
}
