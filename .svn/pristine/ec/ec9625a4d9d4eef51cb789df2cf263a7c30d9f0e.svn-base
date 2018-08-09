package com.carel.supervisor.director;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.config.IInitializable;
import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.system.PvproInfo;
import com.carel.supervisor.base.timer.ILocalTimer;
import com.carel.supervisor.base.timer.TimerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.LiveStatus;
import com.carel.supervisor.controller.VDMappingMgr;
import com.carel.supervisor.controller.database.zipped.VariableZippedManager;
import com.carel.supervisor.controller.function.Function;
import com.carel.supervisor.controller.rule.RuleMgr;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr;
import com.carel.supervisor.dataaccess.dataconfig.ProductInfo;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dataaccess.history.FDBQueue;
import com.carel.supervisor.dataaccess.history.HistoryMgr;
import com.carel.supervisor.dataaccess.history.Writer;
import com.carel.supervisor.dataaccess.support.Information;
import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.director.guardian.CheckersMgr;
import com.carel.supervisor.director.guardian.GuardianVarDispFilter;
import com.carel.supervisor.director.maintenance.MaintenanceMgr;
import com.carel.supervisor.director.module.ModuleMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.book.DispatcherBookList;
import com.carel.supervisor.field.FieldConnectorMgr;
import com.carel.supervisor.field.InternalRelayMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.VariableMgr;
import com.carel.supervisor.field.dataconn.DataCollector;
import com.carel.supervisor.field.dataconn.impl.DataConnCAREL;
import com.carel.supervisor.presentation.assistance.GuardianConfig;
import com.carel.supervisor.presentation.bean.DeviceDetectBean;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;
import com.carel.supervisor.presentation.bo.BSystem;
import com.carel.supervisor.presentation.bo.helper.GuardianHelper;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.report.PrinterMgr2;
import com.carel.supervisor.script.EnumerationMgr;

public class DirectorMgr extends InitializableBase implements IInitializable
{
	private final static String EN_en = "EN_en";
    private static final String SYSTEM_USER = "System";
    private final static String INITIAL_SIZE_QUEUE = "initialSizeQueue";
    private final static String MAX_STEP_NUMBER = "maxStepNumber";
    private final static String NAME = "name";
    private final static String VALUE = "value";
    private static DirectorMgr me = new DirectorMgr();
    private static boolean done = false;
    private SetPollingVarList setPollingVarList = null;
    private int freqGCD = 0;
    private int maxStepNumber = 0;
    private PolicyDequeue policy = null;
    private Logger logger = LoggerMgr.getLogger(this.getClass());
    private DataCollector dataCollector = null;
    private FDBQueue queue = null; //Coda che contiene elementi da scrivere nelle tabelle buffere ed hsvariable
    private Writer writer = null;
    private boolean blockingError = false;
    private boolean initialized = false;
    private MainThread mainThread = null;
    private DebugThread debugThread = null;
    private boolean mustRestart = false;
    private boolean mustCreateProtocolFile = false; 
    private Map<Integer, Variable> variableGuardian = new HashMap<Integer, Variable>();
    //2010-2-25, add by Kevin, to show if debug session is on or not
    private boolean isDebugSessionOn = false;
    private String debugStartTime="";
    private HashMap<Integer,String[]> offlineMap=new HashMap<Integer,String[]>();
    private Date CarelDLLIniteTime = null;
    private boolean bConfigurationLoaded = false;
    
    
    public synchronized boolean isDebugSessionOn() {
		return isDebugSessionOn;
	}

	public synchronized void setDebugSessionOn(boolean isDebugSessionOn) {
		this.isDebugSessionOn = isDebugSessionOn;
	}
	
	//2010-2-26, add by Kevin, to show the CAREL  selected for debug
	private int[] globalindex = new int[0];
    public int[] getGlobalindex() {
		return globalindex;
	}
    //2010-3-1, add by Kevin, to show the timeout, unitmeaure second
    private int timeout = 0;
    private Timer timer = null;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	//2010-4-12, add by Kevin, to show the valid guardian notification channel
	private int guardian_valid_num = 0;
	public int getGuardian_valid_num() {
		return guardian_valid_num;
	}
	//2010-4-12, add by Kevin, to show valid alarm notification channel
	private int alarm_valid_num = 0;
	public int getAlarm_valid_num() {
		return alarm_valid_num;
	}
	//2010-4-12, add by Kevin, to show guardian status
	private int[] guardian_code = null;	
	public int[] getGuardian_code() {
		return guardian_code;
	}
	private boolean pvproValid = false;
	public boolean isPvproValid() {
		return pvproValid;
	}
	public void setPvproValid(boolean valid) {
		this.pvproValid = valid;
	}
	private boolean keepAlive = false;
	public boolean isKeepAlive()
	{
		return this.keepAlive;
	}
	
	private DirectorMgr()
    {
    }

    public static DirectorMgr getInstance()
    {
        return me;
    }

    public synchronized void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        Properties prop = retrieveProperties(xmlStatic, NAME, VALUE, "BSSE0002");
        String initial = retrieveAttribute(prop, INITIAL_SIZE_QUEUE, "BSSE0002");
        int initialSize = Integer.parseInt(initial);
        initial = retrieveAttribute(prop, MAX_STEP_NUMBER, "BSSE0002");
        maxStepNumber = Integer.parseInt(initial);
        policy = new PolicyDequeue(prop);
        queue = new FDBQueue(initialSize);
    }

    public synchronized void loadConfiguration()
    {
        try
        {
            ILocalTimer localTimer = TimerMgr.getTimer("[DIRECTOR][CONFIG]", TimerMgr.HIGH);
            localTimer.start();
            blockingError = false;
            HistoryMgr.getInstance().load();
            
            // Load zipped variables
            VariableZippedManager.getInstance().load();

            SetPollingVarListFactory setPollingVarListFactory = null;

            dataCollector = FieldConnectorMgr.getInstance().getDataCollector();
            FieldConnectorMgr.getInstance().writeProtocol();
            FieldConnectorMgr.getInstance().initDataCollector();
            DeviceStatusMgr.getInstance().loadPhysic(dataCollector);
            setPollingVarListFactory = new SetPollingVarListFactory();
            setPollingVarListFactory.init(maxStepNumber);
            ControllerMgr.getInstance().load();
            
            //load enumeration maps (labels for variables values)
			EnumerationMgr.getInstance().init();

            Map<Integer, Function> functions = ControllerMgr.getInstance().getFunctions();
            DeviceStatusMgr.getInstance().loadLogic(functions);
            ControllerMgr.getInstance().refreshStatus();
            setPollingVarListFactory.assign(dataCollector, functions);
            logger = LoggerMgr.getLogger(this.getClass());
            setPollingVarList = setPollingVarListFactory.createSetPollingVarList();
            freqGCD = setPollingVarListFactory.getGCDFreq();

            //Estrazione device con stato disabilitato
            //Comunicazione a regole e dispositivi
            Integer[] idDevice = extractDeviceDeactive();
            deActiveDevice(idDevice);

            writer = new Writer(queue);

            while(mainThread!=null && !mainThread.ended())
            {
            	try {
                	Thread.sleep(1000l);
				} catch (Exception e) {	}
            }
            mainThread = new MainThread();
            mainThread.init(setPollingVarList, freqGCD, queue, writer, policy);
            initialized = true;
            EventMgr.getInstance().log(new Integer(1), SYSTEM_USER, "Config",
                EventDictionary.TYPE_INFO, "S005", null);

            localTimer.stop();
            
            bConfigurationLoaded = true;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            logger.error(e);
            EventMgr.getInstance().log(new Integer(1), SYSTEM_USER, "Config",
                EventDictionary.TYPE_ERROR, "S006", null);
            blockingError = true;
        }
    }

    public boolean isInitialized()
    {
        return initialized;
    }
    
    
    
    public boolean isMustCreateProtocolFile() {
		return mustCreateProtocolFile;
	}

	public synchronized void stopPVPRO()
    {
        if (!done)
        {
            LiveStatus.update();
            EventMgr.getInstance().log(new Integer(1), "System", "Stop", EventDictionary.TYPE_INFO,
                "S010", null);

            try
            {
                if (DirectorMgr.getInstance().isInitialized())
                {
                    DirectorMgr.getInstance().stopEngine();
                }

                DirectorMgr.getInstance().discargeConfiguration();
                EventMgr.getInstance().log(new Integer(1), "System", "Stop",
                    EventDictionary.TYPE_INFO, "S008", null);
            }
            catch (Exception e)
            {
            	e.printStackTrace();
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }

            done = true;
        }
    }

    public void deActiveDevice(Integer[] idDevice)
    {
        DeviceStatusMgr.getInstance().setDeviceActive(idDevice, false);
    }

    public void activeDevice(Integer[] idDevice)
    {
        DeviceStatusMgr.getInstance().setDeviceActive(idDevice, true);
    }

    public synchronized void mustRestart()
    {
        mustRestart = true;
    }

    public synchronized boolean isMustRestart()
    {
        return this.mustRestart;
    }

    public synchronized void mustCreateProtocolFile()
    {
        mustCreateProtocolFile = true;
    }

    public synchronized void discargeConfiguration() throws Exception
    {
        initialized = false;
        blockingError = false;
        
        // Nicola Compagno 20100609
        // set Internal Relay to default 'Idle' values
        InternalRelayMgr.getInstance().resetDefaultValues();
        
        VariableMgr.getInstance().clear();
        RuleMgr.getInstance().clear();
        ControllerMgr.getInstance().clear();
        DataConfigMgr.getInstance().clear();
        SeqMgr.getInstance().close();
    }

    public void reloadConfiguration()
    {
        reloadConfiguration(SYSTEM_USER);
    }

    public synchronized void reloadConfiguration(String userName)
    {
        if( !bConfigurationLoaded ) {
        	loadConfiguration();
        	return;
        }
    	
    	try
        {
            ILocalTimer localTimer = TimerMgr.getTimer("[DIRECTOR][CONFIG]", TimerMgr.HIGH);
            localTimer.start();
            initialized = false;
            blockingError = false;
            this.setDebugSessionOn(false);
            
            // Nicola Compagno 20100609
            // set Internal Relay to default 'Idle' values
            //removed by Kevin at 2014-9-19, reset it at engine stop
            //InternalRelayMgr.getInstance().resetDefaultValues();

            // Aggiunto in caso di non partenza del motore all'avvio.
            // Causa: Fine TrialPeriod e mancata licenza.
            try
            {
                HistoryMgr.getInstance().close();
            }
            catch (Exception eh)
            {
            	eh.printStackTrace();
                // Empty
            }

            DatabaseMgr.getInstance().resetConnections(null);
            HistoryMgr.getInstance().load();
            
            //Load zipped variable
            VariableZippedManager.getInstance().load();

            VariableMgr.getInstance().clear();
            RuleMgr.getInstance().clear();
            ControllerMgr.getInstance().clear();
            DataConfigMgr.getInstance().reload();
            VDMappingMgr.getInstance().clear();

            if (mustCreateProtocolFile)
            {
                //                  aggiornamento file di configurazione INI
                FieldConnectorMgr.getInstance().writeProtocol();
                FieldConnectorMgr.getInstance().initDataCollector();
                mustCreateProtocolFile = false;
            }
            //by Kevin 2014-9-22, DataConnFTD2IO(Internal IO) need to reload relay status when engine start
            //because when engine stop, will reset engine status to idle
            else
            {
            	DataCollector dataCollector = FieldConnectorMgr.getInstance().getDataCollector();
            	if(dataCollector != null)
            		dataCollector.initDriverByType(FieldConnectorMgr.INTERNALIOTYPE);
            }

            dataCollector = FieldConnectorMgr.getInstance().getDataCollector();
            DeviceStatusMgr.getInstance().loadPhysic(dataCollector);

            SetPollingVarListFactory setPollingVarListFactory = new SetPollingVarListFactory();
            setPollingVarListFactory.init(maxStepNumber);
            ControllerMgr.getInstance().load();
            
            //load enumeration maps (labels for variables values)
			EnumerationMgr.getInstance().init();

            Map<Integer, Function> functions = ControllerMgr.getInstance().getFunctions();
            DeviceStatusMgr.getInstance().loadLogic(functions);
            ControllerMgr.getInstance().refreshStatus();
            setPollingVarListFactory.assign(dataCollector, functions);
            setPollingVarList = setPollingVarListFactory.createSetPollingVarList();
            freqGCD = setPollingVarListFactory.getGCDFreq();

            //          Estrazione device con stato disabilitato
            //Comunicazione a regole e dispositivi
            Integer[] idDevice = extractDeviceDeactive();
            deActiveDevice(idDevice);
            
            //template reload
            try
            {
            	PrinterMgr2.getInstance().reload();
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            	LoggerMgr.getLogger(this.getClass()).error(e.getMessage());
            }

            writer = new Writer(queue);

            if (null != mainThread)
            {
                mainThread.stopPoller();
            }

            while(mainThread!=null && !mainThread.ended())
            {
            	try {
                	Thread.sleep(1000l);
				} catch (Exception e) {	}
            }
            mainThread = new MainThread();
            mainThread.init(setPollingVarList, freqGCD, queue, writer, policy);
            initialized = true;
            EventMgr.getInstance().log(new Integer(1), SYSTEM_USER, "Config",
                EventDictionary.TYPE_INFO, "S005", null);

            localTimer.stop();
            //2010-9-16, add by Kevin. load addressbook when reload
            DispatcherBookList.getInstance().reloadReceivers();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            logger.error(e);
            blockingError = true;
            EventMgr.getInstance().log(new Integer(1), SYSTEM_USER, "Config",
                EventDictionary.TYPE_ERROR, "S006", null);
        }
    }

    public boolean isBlockingError()
    {
        return blockingError;
    }

    public void startEngine()
    {
        startEngine(SYSTEM_USER);
    }

    public void stopEngine()
    {
        stopEngine(SYSTEM_USER);
    }

    private void innerGuardian(String user)
    {
        List<Integer> vars = CheckersMgr.getInstance().start(user);
        Integer id = null;
        variableGuardian.clear();

        for (int i = 0; i < vars.size(); i++)
        {
            Variable var = null;
            id = (Integer) vars.get(i);

            try
            {
                var = ControllerMgr.getInstance().getFromField(id.intValue());
            }
            catch (Exception e)
            {
            	e.printStackTrace();
                LoggerMgr.getLogger(this.getClass()).error(e);
            }

            variableGuardian.put(id, var);
        }
    }

    public synchronized void startEngine(String user)
    {
    	setCarelDLLIniteTime();
        innerGuardian(user);
        
        if (null != mainThread)
        {
            if (mainThread.isStopped() || !mainThread.isAliveThread())
            {
                if (!isBlockingError())
                {
                    if (!initialized)
                    {
                        reloadConfiguration();
                    }

                    startInner(user);
                }
                else //Blocking error
                {
                    reloadConfiguration();
                    startInner(user);
                }
            }
        }
        else
        {
            if (!isBlockingError())
            {
                if (!initialized)
                {
                    reloadConfiguration();
                }

                startInner(user);
            }
            else //Blocking error
            {
                reloadConfiguration();
                startInner(user);
            }
        }

        MaintenanceMgr.getInstance().startChecker();
        SetDequeuerMgr.getInstance().startSetter();
        
        /*
    	 * START DEI MODULI
    	 */
        if(user != null && !user.equalsIgnoreCase(SYSTEM_USER))
        {
        	try {
        		ModuleMgr.getInstance().hookModuleAndStart();
        	}catch(Exception e){
        		e.printStackTrace();
        		logger.error("DirectorMgr. ERROR IN MODULES STARTUP: "+e);
        	}
        }
        
    	//stato flag x file *.FHS
        String msg = "";
        
        IProductInfo pinfo = ProductInfoMgr.getInstance().getProductInfo();
        
        try
        {
			msg = pinfo.get(ProductInfo.FHSFILES);
			
			if ((msg == null) || ("no".equals(msg)))
				msg = "disabled";
			else
				msg = "enabled";
		}
        catch (Exception e)
        {
        	e.printStackTrace();
			msg = e.getMessage();
		}
        
        Logger logger = LoggerMgr.getLogger("CONFIG");
        logger.info("FHS files saving -> " + msg);
        
        // Ricarico memoria per filtro allarmi gestiti dal guardianPRO
        try {
        	GuardianVarDispFilter.getInstance().relaod();
        }
        catch(Exception e) {
        	e.printStackTrace();
        	logger.error("ERROR IN GUARDIAN PROBES RELOAD"+e);
        }
        this.updateAllWhenLoad();
    }

    private void startInner(String user)
    {
        if (null != mainThread)
        {
            if (!isBlockingError())
            {
                mainThread.setVarGuardian(variableGuardian);
                mainThread.startPoller();
                mustRestart = false;

                EventMgr.getInstance().log(new Integer(1), user, "Start",
                    EventDictionary.TYPE_INFO, "S001", null);
            }
            else
            {
                EventMgr.getInstance().log(new Integer(1), user, "Start",
                    EventDictionary.TYPE_ERROR, "S004", null);
            }
        }
    }

    private Integer[] extractDeviceDeactive()
    {
        try
        {
            String sql = "select iddevice from cfdevice where iscancelled='FALSE' and isenabled='FALSE'";
            RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql);
            Integer[] idDevice = new Integer[recordset.size()];

            for (int i = 0; i < recordset.size(); i++)
            {
                idDevice[i] = (Integer) recordset.get(i).get(0);
            }

            return idDevice;
        }
        catch (DataBaseException e)
        {
        	e.printStackTrace();
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);

            return new Integer[0];
        }
    }

    public synchronized void stopEngine(String user)
    {
    	//Kevin, 2014-9-19
    	//set Internal Relay to default values
    	InternalRelayMgr.getInstance().resetDefaultValues();
    	
    	SetDequeuerMgr.getInstance().stopSetter();
    	
    	/*
    	 * STOP DEI MODULI
    	 */
    	try {
    		ModuleMgr.getInstance().hookModuleAndStop();
    	}catch(Exception e){
    		logger.debug("STOPPING MODULES.");
    		e.printStackTrace();
    	}
    	
    	// END
        if (null != mainThread)
        {
            if (mainThread.isStarted() || mainThread.isAliveThread())
            {
                mainThread.stopPoller();
                mainThread.stopThread();
                
                // stopping AlrmLedMgr thread
                // Nicola Compagno 90042010
                mainThread.stopAlrmLedMgr();

                try
                {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                }

                if (mustCreateProtocolFile)
                {
                	try {
						mainThread.join(30000);
					} catch (InterruptedException e1) {
						LoggerMgr.getLogger(this.getClass()).error(e1);
					}
	                FieldConnectorMgr.getInstance().closeDllDriver();
	
	                try
	                {
	                    Thread.sleep(3000);
	                }
	                catch (InterruptedException e)
	                {
	                }
                }
                
                DeviceStatusMgr.getInstance().resetAlarms();

                EventMgr.getInstance().log(new Integer(1), user, "Stop",
                    EventDictionary.TYPE_WARNING, "S002", null);

                initialized = false;
                LiveStatus.update();
                mustRestart = false;
            }
        }

        CheckersMgr.getInstance().stop();

        MaintenanceMgr.getInstance().stopChecker();
       
    }

    public boolean isStarted()
    {
        if (null == mainThread)
        {
            return false;
        }
        else
        {
            return mainThread.isStarted();
        }
    }

    public boolean isStopped()
    {
        if (null == mainThread)
        {
            return true;
        }
        else
        {
            return mainThread.isStopped();
        }
    }

    public Variable getVarGuardian(Integer id)
    {
        if (null != variableGuardian)
        {
            return (Variable) variableGuardian.get(id);
        }
        else
        {
            return null;
        }
    }

    public int getMaxStepNumber()
    {
        return maxStepNumber;
    }
    public void startDebugSession(int[] selectedDeviceid,int timeout,String userName)
    {
    	try{
    		this.stopEngineByDeubug(userName);
    	}catch(Exception ex){logger.error(ex);}
    	DataConnCAREL carel = (DataConnCAREL)FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAREL");
		carel.closeDriver();
    	FieldConnectorMgr.getInstance().writeCARELProtocolForDebug(selectedDeviceid);
    	carel.initDriver();
    	this.setDebugSessionOn(true);
    	debugThread = new DebugThread();
    	try{debugThread.startPoller();}
    	catch(Exception e){}
    	reloadGlobalindex();
    	this.setTimeout(timeout);
    	this.timerCountdown(userName);
    	setCarelDLLIniteTime();
    }
    public void stopDebugSession(String userName)
    {
    	debugThread.stopPoller();
    	while(!debugThread.ended())
    	{
    		try{debugThread.sleep(500);}catch(Exception ex){}
    	}
    	DataConnCAREL carel = (DataConnCAREL)FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAREL");
		carel.closeDriver();
    	this.setDebugSessionOn(false);
    	this.mustCreateProtocolFile();
    	try{this.startEngineByDebug(userName);}
    	catch(Exception e){e.printStackTrace();logger.error(e);}
    }
    public void resetDebugSession(String userName)
    {
    	if(this.isDebugSessionOn == false)
    	{
    		this.mustCreateProtocolFile = true;
    		try{
    			this.stopEngineByDeubug(userName);
    		}
    		catch(Exception e){logger.error(e);}
        	try{
        		this.startEngineByDebug(userName);
        	}
        	catch(Exception e){logger.error(e);}
    	}
    	else
    	{
    		debugThread.stopPoller();
        	while(!debugThread.ended())
        	{
        		try{debugThread.sleep(500);}catch(Exception ex){}
        	}
        	DataConnCAREL carel = (DataConnCAREL)FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAREL");
    		carel.closeDriver();
    		carel.initDriver();
	    	debugThread = new DebugThread();
	    	debugThread.startPoller();
    	}
    	setCarelDLLIniteTime();
    }
    private void reloadGlobalindex()
    {
    	if(this.isDebugSessionOn == true)
    	{
    		try
    		{
    			String filename = System.getenv("windir")+File.separator+"system32"+File.separator+"DRIVER.CCT";
    			FileReader reader = new FileReader(filename);
    			BufferedReader cct = new BufferedReader(reader);
    			String globalindexstr = "";
    			while(true)
    			{
    				String line = cct.readLine();
    				if(line == null)
    				{
    					break;
    				}
    				String[] info = line.split(" ");
    				if(info.length == 3)
    				{
    					globalindexstr += info[0]+" ";
    				}
    			}
    			if(globalindexstr.length()>0)
    			{
    				String[] temp = globalindexstr.split(" ");
    				globalindex = new int[temp.length];
    				for(int i=0;i<temp.length;i++)
    				{
    					globalindex[i] = Integer.valueOf(temp[i]);
    				}
    			}
    			reader.close();
    			cct.close();
    		}
    		catch(Exception ex)
    		{
    			logger.debug("CST:NO_PROBLEM DirectorMgr. Catch in reloadGlobalindex:"+ex);
    			this.globalindex = new int[0];
    		}
    	}
    }
    private void timerCountdown(String userName)
    {
        timer = new Timer();
        final String name = userName;
        TimerTask tt=new TimerTask() 
        { 
            public void run() 
            {
            	timer.cancel();
            	if(isDebugSessionOn == true)
            	{
            		stopDebugSession(name);
            	}
            }
        };
        timer.schedule(tt, this.timeout*1000);
    }

	public String getDebugStartTime() {
		return debugStartTime;
	}

	public void setDebugStartTime(String debugStartTime) {
		this.debugStartTime = debugStartTime;
	}

	public HashMap<Integer, String[]> getOfflineMap() {
		return offlineMap;
	}

	public void setOfflineMap(HashMap<Integer, String[]> offlineMap) {
		this.offlineMap = offlineMap;
	}
	public void updateAllWhenLoad()
	{
		this.updateGuardianCode();
		this.updateKeepAlive();
		this.updatePvproValid();
		this.updateVaidGuardianNum();
		this.updateValidAlarmNum();
	}
	public void updateValidAlarmNum()
	{
		try
		{
			ActionBeanList actionBeanList = new ActionBeanList();
			actionBeanList.getEnabledAction(1,EN_en,false,true);
			this.alarm_valid_num = actionBeanList.checkAlarmNotificationChannel(1, EN_en);
		}
		catch(Exception ex)
		{
		}
	}
	public void updateVaidGuardianNum()
	{
		this.guardian_valid_num = GuardianConfig.checkNotificationChannelNumber();
	}
	public void updateGuardianCode()
	{
		this.guardian_code = GuardianHelper.getGuardianMessageCode();
	}
	public void updateKeepAlive()
	{
		try
		{
			ActionBeanList actionBeanList = new ActionBeanList();
			actionBeanList.getEnabledAction(1,EN_en,true,true);
			this.keepAlive = actionBeanList.checkKeepAlive();
		}
		catch(Exception ex)
		{
		}
	}
	public void updatePvproValid()
	{
		this.pvproValid = Information.getInstance().valid();
	}
	public static void main(String[] args)
	{
		try
		{
			BaseConfig.init();
			//FieldConnectorMgr.getInstance().closeDllDriver();
	    	FieldConnectorMgr.getInstance().initDataCollector();
		}
		catch(Exception ex)
		{}
	}
	private void setCarelDLLIniteTime()
	{
		CarelDLLIniteTime = new Date();
	}
	public Date getCarelDLLInitTime()
	{
		return CarelDLLIniteTime;
	}
	public boolean canShowAvgPollingTime()
	{
		if(CarelDLLIniteTime == null)
			return true;
		long diff = new Date().getTime()-CarelDLLIniteTime.getTime();
		//after 30 seconds, show AvgPollingTime
		if(diff/1000>30)
			return true;
		else
			return false;
	}
	//start engine copied from Bsystem
	private void startEngineByDebug(String userName)
	throws Exception
	{
        if( !Information.getInstance().canStartEngine() ) {
            EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S028",null);
            return;
        }
        if( DeviceDetectBean.isDetection() )
        	return;
        if( PvproInfo.getInstance().getLicenseOverload() > 0 ) {
    		EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S038",null);
    		return;
    	}
        PvproInfo.getInstance().resetLoggedVariables();
    	if( PvproInfo.getInstance().isLoggingOverload() ) {
            EventMgr.getInstance().log(new Integer(1),"System","Start",EventDictionary.TYPE_WARNING,"S037",null);
    		return;
    	}  	
    	if (DirectorMgr.getInstance().isStopped())
        {
            DirectorMgr.getInstance().reloadConfiguration(userName);
            DirectorMgr.getInstance().startEngine(userName);

            if (!DirectorMgr.getInstance().isStopped())
            {
                if (!DispatcherMgr.getInstance().isServiceRunning())
                {
                    DispatcherMgr.getInstance().startService(true);
                }
            }
        }
    	Thread.sleep(1000L);
	}
	//stop engine copied from Bsystem
	private void stopEngineByDeubug(String userName)
		throws Exception
	{
		long sleep = 1000L;
        // Stop motore
        if (DirectorMgr.getInstance().isStarted())
        {
            DirectorMgr.getInstance().stopEngine(userName);
            sleep = 10000L;
        }

        // Rest macchina a stati
        ControllerMgr.getInstance().reset();

        // Stop dispatcher
        if (DispatcherMgr.getInstance().isServiceRunning())
        {
            DispatcherMgr.getInstance().stopService();
            sleep = 10000L;
        }

        Thread.sleep(sleep);
	}
}
