package com.carel.supervisor.field.dataconn;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.CoreMessages;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.dataconfig.LineInfo;
import com.carel.supervisor.dataaccess.dataconfig.LineInfoList;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.director.packet.PacketMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.dataconn.impl.DataConnBase;
import com.carel.supervisor.field.dataconn.impl.DataConnEMPTY;


public class DataCollector extends InitializableBase
{
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String CLASSCONN = "classConn";
    private static final String COMPONENT = "component";
    private HashMap<String,IDataConnector> connectors = new HashMap<String,IDataConnector>();
    private ArrayList<String> connectorNames = new ArrayList<String>();
    private boolean bDllInitialized = false;
    
    public DataCollector()
    {
    }

    public void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        String name = null;
        String type = null;
        String classConn = null;
        XMLNode xmlTmp = null;

        for (int i = 0; i < xmlStatic.size(); i++)
        {
            xmlTmp = xmlStatic.getNode(i);

            if (xmlTmp.getNodeName().equals(COMPONENT))
            {
                name = retrieveAttribute(xmlTmp, NAME, "FLDE0001",
                        String.valueOf(i));
                classConn = retrieveAttribute(xmlTmp, CLASSCONN, "FLDE0001",
                        String.valueOf(i));
                type = retrieveAttribute(xmlTmp, TYPE, "FLDE0001",
                        String.valueOf(i));

                try
                {
                    IDataConnector dataConnector = (IDataConnector) FactoryObject.newInstance(classConn);
                    IDataConnector customLicenses = (IDataConnector)customLicenseInstance(type);
                    if (customLicenses!=null)
            		{
            			dataConnector = customLicenses;
            		}
                    ((IInitializable) dataConnector).init(xmlTmp);
                    dataConnector.setName(name);
                    connectors.put(type, dataConnector);
                    connectorNames.add(type);
                }
                catch (Exception e)
                {
                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
            }
        }
    }
    private Object customLicenseInstance(String type)
	{
    	Object custom_class = null;
		
		try 
		{
			String strProductCode = BaseConfig.getProductCode();
			if(strProductCode != null && strProductCode.length()>0)
				strProductCode = strProductCode.substring(0,strProductCode.length()-5);
			String strClassName = 
				"com.carel.supervisor.field.dataconn.impl.DataConn"
				+type+ (strProductCode != null ? strProductCode : "Custom");  
			
			custom_class = Class.forName(strClassName).newInstance();
		}
		catch (Exception e) {
		}
		return custom_class;
	}
    public void initDllDriver()
    {
    	if (null != connectors)
        {
            Iterator<String> iterator = connectorNames.iterator();
            String key = null;
            IDataConnector dataConnector = null;
            LineInfoList lineInfoList = (LineInfoList)DataConfigMgr.getInstance().getConfig("cfline");
            
            while (iterator.hasNext())
            {
                key = iterator.next();
                dataConnector = (IDataConnector) connectors.get(key);

                if (null != dataConnector)
                {
                    //Check if there is a line with this protocol
                	dataConnector.removeBlockingError();
                	if (protocolIsPresent(lineInfoList, key))
                	{
                		dataConnector.initDriver();
                	}
                	else
                	{
                		dataConnector.setBlockingError();
                	}
                }
                else //Dataconn not found
                {
                    driverNotPresent(key);
                    connectors.put(key, new DataConnEMPTY());
                }
            }
        }
    	bDllInitialized = true;
    }
    
    public void initDriverByType(String type)
    {
    	if (null != connectors)
        {
            Iterator<String> iterator = connectors.keySet().iterator();
            String key = null;
            IDataConnector dataConnector = null;
            LineInfoList lineInfoList = (LineInfoList)DataConfigMgr.getInstance().getConfig("cfline");
            
            while (iterator.hasNext())
            {
                key = iterator.next();
                if(!key.equals(type))
                {
                	continue;
                }
                dataConnector = (IDataConnector) connectors.get(key);

                if (null != dataConnector)
                {
                    //Check if there is a line with this protocol
                	dataConnector.removeBlockingError();
                	if (protocolIsPresent(lineInfoList, key))
                	{
                		dataConnector.initDriver();
                	}
                	else
                	{
                		dataConnector.setBlockingError();
                	}
                }
                else //Dataconn not found
                {
                    driverNotPresent(key);
                    connectors.put(key, new DataConnEMPTY());
                }
                break;
            }
        }
        else
        {
            //(PLANT VISOR REMOTO)
        }
    }
    private boolean protocolIsPresent(LineInfoList lineInfoList, String protocol)
    {
    	LineInfo lineInfo = null;
    	for (int i = 0; i < lineInfoList.size(); i++)
    	{
    		lineInfo = lineInfoList.get(i);
    		if (protocol.equals(lineInfo.getTypeProtocol()))
			{
    			return true;
			}
    	}
    	return false;
    }
    
    public void loadDllDriver()
    {
        if (null != connectors)
        {
            Iterator<String> iterator = connectors.keySet().iterator();
            String key = null;
            IDataConnector dataConnector = null;

            while (iterator.hasNext())
            {
                key = iterator.next();
                dataConnector = (IDataConnector) connectors.get(key);

                if (null != dataConnector)
                {
                    dataConnector.loadDllDriver();
                }
                else //Dataconn not found
                {
                    driverNotPresent(key);
                    connectors.put(key, new DataConnEMPTY());
                }
            }
        }
        else
        {
            //(PLANT VISOR REMOTO)
        }
    }

    public void closeDllDriver()
    {
        bDllInitialized = false;
    	if (null != connectors)
        {
            Iterator<String> iterator = connectors.keySet().iterator();
            String key = null;
            IDataConnector dataConnector = null;

            while (iterator.hasNext())
            {
                key = iterator.next();
                dataConnector = (IDataConnector) connectors.get(key);

                if (null != dataConnector)
                {
                    dataConnector.closeDriver();
                }
            }
        }
        else
        {
            //(PLANT VISOR REMOTO)
        }
    }

    public void writeProtocol()
    {
        if (null != connectors)
        {
            Iterator<String> iterator = connectors.keySet().iterator();
            String key = null;
            IDataConnector dataConnector = null;

            while (iterator.hasNext())
            {
                key = iterator.next();
                dataConnector = (IDataConnector) connectors.get(key);

                if (null != dataConnector)
                {
                    try
                    {
                        dataConnector.writeProtocol();
                    }
                    catch (Exception e)
                    {
                        Logger logger = LoggerMgr.getLogger(this.getClass());
                        logger.error(e);
                    }
                }
            }
        }
        else
        {
            //(PLANT VISOR REMOTO)
        }
    }
    public void writeProtocol(int[] selectedDeviceid,String type)
    {
        if (null != connectors)
        {
            Iterator<String> iterator = connectors.keySet().iterator();
            String key = null;
            IDataConnector dataConnector = null;

            while (iterator.hasNext())
            {
                key = iterator.next();
                if(!key.equals(type))
                {
                	continue;
                }
                dataConnector = (IDataConnector) connectors.get(key);

                if (null != dataConnector)
                {
                    try
                    {
                        dataConnector.writeProtocol(selectedDeviceid);
                    }
                    catch (Exception e)
                    {
                        Logger logger = LoggerMgr.getLogger(this.getClass());
                        logger.error(e);
                    }
                }
                break;
            }
        }
        else
        {
            //(PLANT VISOR REMOTO)
        }
    }

    //********************************    
    //      Methods used only by ONLINE
    //********************************
    public void getFromField(List<Variable> variables)
    {
        Variable variable = null;

        for (int i = 0; i < variables.size(); i++)
        {
            variable = variables.get(i);
            getFromField(variable);
        }
    }

    public void setOnField(List<Variable> variables)
    {
        Variable variable = null;

        for (int i = 0; i < variables.size(); i++)
        {
            variable = variables.get(i);

            if (!variable.isDeviceOffLine()) //solo se il device � on-line
            {
            	if (!variable.getFormattedValue().equals("***")) //solo se la var � on-line
                {
            		setOnFieldInner(variable);
                }
            }
        }
    }

    public int setOnField(Variable variable)
    {
        if (!variable.isDeviceOffLine()) //solo se il device � on-line
        {
            if (!variable.getFormattedValue().equals("***")) //solo se la var � on-line
            {
            	return setOnFieldInner(variable);
            }
            else
            {
            	return DataConnBase.SET_TIMEOUT;
            }
        }
        else
        {
        	return DataConnBase.SET_DEVICE_OFFLINE;
        }
    }
    
    /** multiplexed variables management */
    public int setZippedOnField(Variable variable)
    {
        if (!variable.isDeviceOffLine()) 
        {
            if (!Float.isNaN(variable.getCurrentValue()))
            {
            	return setOnFieldInner(variable);
            }
            else
            {
            	return DataConnBase.SET_TIMEOUT;
            }
        }
        else
        {
        	return DataConnBase.SET_DEVICE_OFFLINE;
        }
    }    

    public IDataConnector getDataConnector(String protocolType)
    {
        IDataConnector dataConnector = null;
        dataConnector = (IDataConnector) connectors.get(protocolType);

        if (null != dataConnector)
        {
            return dataConnector;
        }
        else //Dataconn not found
        {
            driverNotPresent(protocolType);

            return new DataConnEMPTY();
        }
    }

    private int setOnFieldInner(Variable variable)
    {
        IDataConnector dataConnector = null;
        String type = variable.getInfo().getProtocolType();
        dataConnector = (IDataConnector) connectors.get(type);

        if (bDllInitialized && null != dataConnector)
        {
            if (!dataConnector.isBlockingError())
            {
                return dataConnector.setOnField(variable);
            }
            else
            {
            	return DataConnBase.BLOCKING_ERROR;
            }
        }
        else //Dataconn not found
        {
            if( null == dataConnector )
            	driverNotPresent(type);
            return DataConnBase.FATAL_ERROR;
        }
    }

    public void getFromField(Variable variable)
    {
    	IDataConnector dataConnector = null;
        String type = variable.getInfo().getProtocolType();
        dataConnector = (IDataConnector) connectors.get(type);

        if (bDllInitialized && null != dataConnector)
        {
            if (!dataConnector.isBlockingError())
            {
                dataConnector.retrieve(variable);
            }
        }
        else //Dataconn not found
        {
        	if( null == dataConnector )        	
        		driverNotPresent(type);
        }
    }

    private void driverNotPresent(String type)
    {
        EventMgr.getInstance().log(new Integer(1), "Field", "Config",
                EventDictionary.TYPE_ERROR, "F001", type);
    	Logger logger = LoggerMgr.getLogger(this.getClass());
        logger.error(CoreMessages.format("FLDE0009", type));
       	connectors.put(type, new DataConnEMPTY());
    }
}
