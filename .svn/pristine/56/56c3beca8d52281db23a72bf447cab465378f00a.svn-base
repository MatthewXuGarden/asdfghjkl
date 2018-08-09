package com.carel.supervisor.field;

import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.field.dataconn.DataCollector;


public class FieldConnectorMgr implements IInitializable
{
    private static boolean initialized = false;
    private static boolean loaded = false;
    private static FieldConnectorMgr m_oMe = new FieldConnectorMgr();
    private DataCollector dataCollector = null;
    private Logger logger = null;
    private static final String CARELPROTOCOLTYPE = "CAREL";
    public static final String INTERNALIOTYPE = "FTD2IO";

    private FieldConnectorMgr()
    {
        logger = LoggerMgr.getLogger(this.getClass());
    }

    public static FieldConnectorMgr getInstance()
    {
        return m_oMe;
    }

    public synchronized void init(XMLNode xmlStatic)
        throws InvalidConfigurationException
    {
        if (!initialized)
        {
            dataCollector = new DataCollector();
            logger.info("[FIELD DRIVERS START INIT]");
            dataCollector.init(xmlStatic);
            dataCollector.loadDllDriver();
            logger.info("[FIELD DRIVERS END INIT]");
            initialized = true;
        }
    }

    public DataCollector getDataCollector()
    {
        return dataCollector;
    }

    public synchronized boolean isLoaded()
    {
        return loaded;
    }

    public synchronized void initDataCollector()
    {
        dataCollector.initDllDriver();
    }
    
    public synchronized void initCARELCollector()
    {
    	dataCollector.initDriverByType(CARELPROTOCOLTYPE);
    }
    public synchronized void writeCARELProtocolForDebug(int[] selectedDeviceid)
    {
    	dataCollector.writeProtocol(selectedDeviceid, CARELPROTOCOLTYPE);
    }
    public void writeProtocol()
    {
        dataCollector.writeProtocol();
    }

    public void closeDllDriver()
    {
        dataCollector.closeDllDriver();
    }
} //Class FieldConnectorManager
