package com.carel.supervisor.dispatcher;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.FatalHandler;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.io.SocketComm;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.director.DirectorMgr;
import com.carel.supervisor.dispatcher.book.DispatcherBook;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.dispatcher.engine.LineStatus;
import com.carel.supervisor.dispatcher.engine.sms.SMSProviderMgr;
import com.carel.supervisor.dispatcher.main.DispatcherDQMgr;
import com.carel.supervisor.dispatcher.main.DispatcherMonitor;
import com.carel.supervisor.dispatcher.main.DispatcherPoller;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.ZMemory;


public class DispatcherMgr implements IInitializable
{
    private static final String ND_CONFIG = "config";
    private static final String ND_REGISTRY = "registry";
    private static final String ND_TEMPLATE = "template";
    private static DispatcherMgr dispatcherMgr = new DispatcherMgr();
    private boolean initialized = false;
    private Properties confProp = null;
    private Properties regyPropDesc = null;
    private Properties regyPropActions = null;
    private Properties regyPropMemory = null;
    private Properties template = null;
    private DispatcherPoller dPoller = null;
    private LineStatus lineStatus = null;
    private boolean serviceDialActive = false;

    // For guardian
    private long lastCodeOneCheck = 0L;
    private long lastCodeTwoCheck = 0L;
    private Map pingTable = new HashMap();

    private DispatcherMgr()
    {
        this.confProp = new Properties();
        this.regyPropDesc = new Properties();
        this.regyPropActions = new Properties();
        this.regyPropMemory = new Properties();
        this.template = new Properties();
        this.lineStatus = new LineStatus();
        this.serviceDialActive = false;
    }

    public static DispatcherMgr getInstance()
    {
        return dispatcherMgr;
    }

    public void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        if (!initialized)
        {
            try
            {
                // Load Address Book
                DispatcherBookList.getInstance().reloadReceivers();

                // Load Configuration
                loadConfiguration(xmlStatic.getNode(ND_CONFIG));

                // Load Channel
                loadRegistry(xmlStatic.getNode(ND_REGISTRY));

                // Load Template
                loadTemplate(xmlStatic.getNode(ND_TEMPLATE));

                // Write SMSProvider
                if (writeSmsProvider())
                {
                    SMSProviderMgr spm = new SMSProviderMgr();
                    spm.loadProvider();
                    spm.writeProvider(getProviderPath(), getProviderName());
                }

                loadDeviceInMemory();

                // Load IO Configuration in memory
                try
                {
                    DispMemMgr.getInstance().storeAllConfiguration(this.regyPropMemory);
                    EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Config",
                        EventDictionary.TYPE_INFO, "D016", null);
                }
                catch (Exception e) {}
                
                /*
                 * Start Dispatcher Service
                 */ 
                this.startService(false);
            }
            catch (Exception ex) {
                FatalHandler.manage(this, "...", ex);
            }

            this.initialized = true;
        }
    }
    
    public void loadDeviceInMemory()
    {
    	try
        {
            // Command for load device list in memory
        	int port = 1980;
        	try{
        		port = Integer.parseInt(BaseConfig.getProductInfo("info"));
        	}catch(Exception e){
        		// Take default port
        	}
        	
            String resp = SocketComm.sendCommand("localhost", port, "LDL;F;S;D;R;E");

            if ((resp != null) && resp.equalsIgnoreCase("OK"))
            {
                DispMemMgr.getInstance().loadMachineDevice(port,new String[]{"F","S","D","R","E"});
                EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Config",
                    EventDictionary.TYPE_INFO, "D020", null);
            }
            else
            {
            	EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Config",
                        EventDictionary.TYPE_ERROR, "D021", null);
            }
        }
        catch (RuntimeException e1)
        {
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Config",
                EventDictionary.TYPE_ERROR, "D021", null);
        }
    }
    
    /*
     * Start Fisic Device Poller
     */
    public void startFisicDevice(String idFisDev)
    {
    	DispatcherDQMgr.getInstance().startFisicDevice(idFisDev,getTimeStart(),getPeriod(),getAlgoTime());
    }

    /*
     * Stop Fisic Device Poller
     */
    public void stopFisicDevice(String idFisDev)
    {
        DispatcherDQMgr.getInstance().stopFisicDevice(idFisDev);
    }
    
    public DispatcherBook getReceiverInfo(int key)
    {
        return DispatcherBookList.getInstance().getReceiver(key);
    }

    public DispatcherBook[] getReceiverInfoByType(String type)
    {
        return DispatcherBookList.getInstance().getReceiversByType(type);
    }

    public boolean writeSmsProvider()
    {
        return Boolean.valueOf(this.confProp.getProperty("smsproviderwrite")).booleanValue();
    }

    public String getUser4Remote()
    {
        return this.confProp.getProperty("rlogin");
    }

    public String getPass4Remote()
    {
        return this.confProp.getProperty("rpassw");
    }

    public String getTout4Remote()
    {
        return this.confProp.getProperty("rtiout");
    }

    public String getProviderPath()
    {
        return BaseConfig.getCarelPath() + this.confProp.getProperty("smsproviderpath") +
        File.separator;
    }

    public String getPrintServerPort()
    {
        return this.confProp.getProperty("printport");
    }

    public String getProviderName()
    {
        return this.confProp.getProperty("smsprovidername");
    }

    public String getTimeStart()
    {
        return this.confProp.getProperty("timeafterstart");
    }

    public String getPeriod()
    {
        return this.confProp.getProperty("timeread");
    }

    public boolean startDispatcher()
    {
        return Boolean.valueOf(this.confProp.getProperty("startonload")).booleanValue();
    }

    public String getTemplatePath()
    {
        return this.confProp.getProperty("templatepath");
    }

    public String getRepositoryPath()
    {
        return BaseConfig.getCarelPath() + this.confProp.getProperty("repository") +
        File.separator;
    }

    public boolean serviceExternal()
    {
        return Boolean.valueOf(this.confProp.getProperty("useservices")).booleanValue();
    }

    public String getServicesPath()
    {
        return BaseConfig.getCarelPath() + this.confProp.getProperty("services") + File.separator;
    }

    public String getCertificatePath()
    {
        return BaseConfig.getCarelPath() + this.confProp.getProperty("certificate");
    }

    public String getTestFor(String type)
    {
        return this.confProp.getProperty(type);
    }

    public String decodeActionType(String action)
    {
        return this.regyPropDesc.getProperty(action);
    }

    public boolean updateBookAddress(DispatcherBook[] list)
    {
        return DispatcherBookList.getInstance().updateAddressBook(list);
    }

    public Properties getRegistryActions()
    {
        return this.regyPropActions;
    }

    public String getObjectForTemplate(String template)
    {
        return this.template.getProperty(template);
    }

    public boolean typeHasMemory(String type)
    {
        String sMem = this.regyPropMemory.getProperty(type);
        boolean ret = true;

        if ((sMem != null) && sMem.equalsIgnoreCase("NULL"))
        {
            ret = false;
        }

        return ret;
    }

    private String getAlgoTime()
    {
        return this.confProp.getProperty("timealgo");
    }

    private void loadConfiguration(XMLNode xmlConf) throws Exception
    {
        if (xmlConf == null)
        {
            throw new Exception("NO DISPATCHER CONFIG NODE");
        }

        XMLNode[] listConfEl = xmlConf.getNodes();

        for (int i = 0; i < listConfEl.length; i++)
        {
            this.confProp.put(listConfEl[i].getAttribute("name"),
                listConfEl[i].getAttribute("value"));
        }
    }

    private void loadRegistry(XMLNode xmlRegy) throws Exception
    {
        if (xmlRegy == null)
        {
            throw new Exception("NO DISPATCHER REGISTRY NODE");
        }

        XMLNode[] listRegfEl = xmlRegy.getNodes();

        for (int i = 0; i < listRegfEl.length; i++)
        {
            this.regyPropDesc.put(listRegfEl[i].getAttribute("type"),
                listRegfEl[i].getAttribute("desc"));
            this.regyPropActions.put(listRegfEl[i].getAttribute("type"),
                listRegfEl[i].getAttribute("action"));
            this.regyPropMemory.put(listRegfEl[i].getAttribute("type"),
                listRegfEl[i].getAttribute("memory"));
        }
    }

    private void loadTemplate(XMLNode xmlTmpl) throws Exception
    {
        if (xmlTmpl == null)
        {
            throw new Exception("NO DISPATCHER TEMPLATE NODE");
        }

        XMLNode[] list = xmlTmpl.getNodes();

        for (int i = 0; i < list.length; i++)
        {
            this.template.put(list[i].getAttribute("type"), list[i].getAttribute("object"));
        }
    }

    /*
     * Status remote connection
     */
    public void resetDeviceStatus(String ipRemote)
    {
        this.lineStatus.resetStatusByIp(ipRemote);
    }

    public void setDeviceStatus(String device)
    {
        this.lineStatus.resetStatus(device);
    }

    public void setDeviceStatus(String device, String ip)
    {
        if ((ip != null) && (ip.trim().length() == 0))
        {
            this.lineStatus.resetStatus(device);
        }
        else
        {
            this.lineStatus.setStatus(device, ip);
        }
    }

    public boolean isDeviceFree(String device)
    {
        return this.lineStatus.freeStatus(device);
    }

    // New Dial with external services
    public void setServiceDialState(boolean state)
    {
        this.serviceDialActive = state;
    }

    public boolean getServiceDialState()
    {
        return this.serviceDialActive;
    }

    /*
     * Guardian
     * Last time of dispatcher queues activity
     */
    // VECCHIO METODO NON PIU' USATO
    public void setLastCheckCodeOne(long l)
    {
        this.lastCodeOneCheck = l;
    }
    // VECCHIO METODO NON PIU' USATO
    public void setLastCheckCodeTwo(long l)
    {
        this.lastCodeTwoCheck = l;
    }
    // VECCHIO METODO NON PIU' USATO
    public long getLastCheckCodeOne()
    {
        return this.lastCodeOneCheck;
    }
    // VECCHIO METODO NON PIU' USATO
    public long getLastCheckCodeTwo()
    {
        return this.lastCodeTwoCheck;
    }

    public void increasePingTable(String idDevice)
    {
        Integer i = (Integer) this.pingTable.remove(idDevice);
        int val = 0;

        if(i != null)
        	val = i.intValue();
            
        if(val > 4)
        {
        	EventMgr.getInstance().log(new Integer(1),"Dispatcher","Action",
        							   EventDictionary.TYPE_ERROR,"D057",new Object[]{idDevice});
        }
        
        this.pingTable.put(idDevice, new Integer(++val));
    }
    
    public void clearPingTable(String idDevice)
    {
    	this.pingTable.remove(idDevice);
    }

    public boolean pingExceded(int num)
    {
        Integer in = null;
        boolean ris = false;

        if (this.pingTable != null)
        {
            Iterator i = this.pingTable.values().iterator();

            while (i.hasNext())
            {
                in = (Integer) i.next();
                if ((in != null) && (in.intValue() > num))
                {
                    ris = true;
                    break;
                }
            }
        }

        return ris;
    }
    
    
    /*
     * StartDispatcher
     * 
     * Tramite questo metodo viene fatto partire il thread 
     * del dispatcher sulla primca coda (hsaction).
     */
    public void startService(boolean withDevice)
    {
    	try
    	{
	    	if (startDispatcher())
	    	{
	    		dPoller = new DispatcherPoller(getPeriod(), getTimeStart());
	    		if(this.dPoller != null)
	    			dPoller.startDispatcherPoller();
	    		
	    		if(withDevice)
	    		{
		    		Iterator i = this.regyPropMemory.keySet().iterator();
		            String type = "";
		            ZMemory zMem = null;
		            
		            while (i.hasNext())
		            {
		                type = (String) i.next();
		                zMem = DispMemMgr.getInstance().readConfiguration(type);
		                if(zMem != null)
		                	this.startFisicDevice(zMem.getFisicDeviceId());
		            }
                    
		            //2010-9-16, removed by Kevin. add reloadReceivers() in DirectorMgr.getInstance().reloadConfiguration()
                    /*
                     *  Ricarico addressbook.
                     *  Intervento introdotto per l'import della configurazione regole     
                     */
//                    try
//                    {
//                        DispatcherBookList.getInstance().reloadReceivers();
//                    }
//                    catch(Exception e){}
	    		}
	    		
	    		// Start DispatcherMonitor per sonde guardiano
	    		DispatcherMonitor.getInstance().startMonitor();
	    	}
	    	else
	            EventMgr.getInstance().log(new Integer(1),"Dispatcher","Start",EventDictionary.TYPE_WARNING,"D007",null);
    	}
    	catch (Exception e) {
        	EventMgr.getInstance().log(new Integer(1),"Dispatcher","Start",EventDictionary.TYPE_ERROR,"D030",null);
        }
    }
    
    /*
     * StopDispatcher
     * 
     * Tramite questo metodo viene fermato il thread 
     * del dispatcher sulla primca coda (hsaction).
     */
    public void stopService()
    {
    	if(this.dPoller != null)
    	{
    		this.dPoller.stopDispatcherPoller();
    		this.dPoller = null;
    		
    		DispatcherDQMgr.getInstance().stopDevices();
    	}
    	
    	// Stop DispatcherMonitor
    	DispatcherMonitor.getInstance().stopMonitor();
    }
    
    /*
     * IsDispatcheRunnig
     * 
     * Tramite questo metodo viene identificato lo stato del dispatcher.
     */
    public boolean isServiceRunning()
    {
    	return this.dPoller != null;
    }

    //2011-1-6, Kevin Ge, reload configuration after I/O modem changed
    public void reloadConfiguration()
    {
    	int maxSecsWait = 60;
    	int msecsWaiting = 0 ;
    	
    	if (isServiceRunning())
        {
			stopService();
            DispMemMgr.getInstance().storeAllConfiguration(this.regyPropMemory);
            if (!DirectorMgr.getInstance().isStopped())
            {
                if (!isServiceRunning())
                {
                	// before starting DispatcherMgr again, wait until DispatcherMonitor is stopped
                	// Nicola Compagno 22/4/2011
                	while(DispatcherMonitor.getInstance().isMonitorActive())
                	{
                		try
                		{
                			Thread.sleep(50);
                			msecsWaiting += 50;
                		
                			if(msecsWaiting > maxSecsWait * 1000)
                			{
                				LoggerMgr.getLogger(this.getClass()).error("Error on DispatcherMgr restart - stopService command does not terminate");
                				return;
                			}
                		}
                		catch (Exception e)
                		{
                		}
                	}
                	startService(true);
                }
            }
        }
    }
}
