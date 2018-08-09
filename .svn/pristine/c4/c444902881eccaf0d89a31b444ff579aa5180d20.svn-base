package com.carel.supervisor.plugin.fs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.FSSetCallback;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.controller.setfield.SetWrp;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.presentation.events.DevicesList;
import com.carel.supervisor.presentation.fs.FSRackAux;
import com.carel.supervisor.presentation.fs.FSRackBean;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLElement;
import com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement;


public class FSManager
{
    private static final String MAX_RACKS = "maxracks";
    private static final String MAX_UNITS = "maxunits";
    private static final String MAX_NUM_RETRY = "maxnumretry";
    private static final String RETRY_INTERVAL = "retryinterval";
    private static final String POLLING = "polling";
    private static final String AUTOSTART = "start";
    private static final Float DUMMY_GRAD = 1000F;
	
    //campi inizializzati da db
    private static Integer maxracks = 8;
    private static Integer maxunits = 150;
    private static Integer maxnumretry = 10;
    private static Integer retryinterval = 5;
    private static Integer polling = 5; //freq campionamento solenoidi in sec.
    private static Integer start = 0;
    private String defaultLanguage = "EN_en";
    private FSClock clock = null;
    private FSRack[] racks = null;
    private HashMap<Integer, Variable> fieldvariables;
    
    private int offlinecounter = 0;
    
    // new algorithm
    private static Integer Sb;
    public static final Integer SB_STATUS = 10;
    private static Integer t;
    //private static Integer Q;
    private static Integer YELLOW_Q;
    private static Integer ORANGE_Q;
    private static Integer RED_Q;
    
    public static final Integer STATUS_SIZE = 9;
    public static final Integer NULL = 0;
    public static final Integer SOLENOIDNULL = 1;
    public static final Integer GREEN = 2;
    public static final Integer YELLOW = 3;
    public static final Integer YELLOW_OFFLINE = 4;
    public static final Integer ORANGE = 5;
    public static final Integer ORANGE_OFFLINE = 6;
    public static final Integer RED = 7;
    public static final Integer RED_OFFLINE = 8;
    
    public static final Integer STATUS_NULL = 0;
    public static final Integer STATUS_SOLENOIDNULL = 2;
    public static final Integer STATUS_GREEN = 5;
    public static final Integer STATUS_YELLOW = 10;
    public static final Integer STATUS_YELLOW_OFFLINE = 11;
    public static final Integer STATUS_ORANGE = 15;
    public static final Integer STATUS_ORANGE_OFFLINE = 16;
    public static final Integer STATUS_RED = 20;
    public static final Integer STATUS_RED_OFFLINE = 21;
    
    Map<Integer,String> rackDescriptionMap = null;
    // single instance
    private static FSManager myInstance = new FSManager();
    
    
    private FSManager()
    {
    	try {
    		init();
    	} catch(Exception e) {
    		LoggerMgr.getLogger(this.getClass()).error(e);
    	}
    }
   
    
    public static FSManager getInstance()
    {
    	return myInstance;
    }

    
    private void init() throws DataBaseException
    {
    	defaultLanguage = loadDefaultLanguage();
        this.racks = FSRack.getRacks(defaultLanguage);
        if(racks != null && racks.length>0)
        {
	        for(int i = 0; i < racks.length; i++) {
	        	if( racks[i].isNewAlg() ) {
	        		racks[i].setClock(t);
	        	}
	        }
        }
        try{
        	rackDescriptionMap = new HashMap<Integer,String>();
        	String sql = "select fsrack.idrack,t1.description as rdesc,t2.description as vdesc,fsrack.new_alg from fsrack,cftableext as t1,cftableext as t2, " +
    		"cfdevice,cfvariable where fsrack.iddevice=cfdevice.iddevice and cfdevice.iscancelled=? and " +
    		"t1.languagecode =? and t1.tablename='cfdevice' and t1.tableid=fsrack.iddevice and t1.idsite=1 and " +
    		"cfvariable.iddevice=fsrack.iddevice and cfvariable.idvariable=fsrack.setpoint and cfvariable.iscancelled='FALSE' and " +
    		"t2.tableid=cfvariable.idvariable and t2.tablename='cfvariable' and t2.languagecode=? and t2.idsite=1";
		    Object[] params = new Object[3];
		    params[0] = "FALSE";
		    params[1] = this.defaultLanguage;
		    params[2] = this.defaultLanguage;
		
		    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);    
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
		    	Integer idrack = (Integer)r.get("idrack");
		    	String descr_rack = r.get("rdesc").toString();
		    	String sp_code = r.get("vdesc").toString();
		    	rackDescriptionMap.put(idrack, descr_rack + " (" + sp_code + ")");
			}
        }catch(Exception ex){}
    }
    
    
    private String loadDefaultLanguage() throws DataBaseException
    {
    	String sql = "select languagecode from cfsiteext where isdefault=?";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{"TRUE"});
    	return rs.get(0).get(0).toString();
    }

    
    public synchronized void configurationChanged(String user)
    {
		try {
	    	if( isRunning() ) {
	    		stopFS(user);
	    		startFS(user);
	    	}
	    	else {
	    			init();
	    	}
		} catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
    }
    
    
    //*** START PLUGIN ***
    public synchronized void startFS(String user) throws DataBaseException
    {
    	init();
        if (this.racks != null)
        {
            if (clock == null)
            {
                clock = new FSClock(this, polling);
            }

            if (clock.isStopped())
            {
                
            	clock.startPoller();
            }

            if (clock.isStarted())
            {
                //log plugin partito FS01
            	EventMgr.getInstance().info(1,user,"fs","FS01",null);
            }
        }
        else
        {
        }
    }
    
    // *** stop plugin ***
    public synchronized void stopFS(String user)
    {
    	stopFS(user,true);
    }
    
    synchronized public void stopFS(String user ,boolean needDequeueAll){
		if(clock != null)
    		clock.stopPoller();
        clock = null;
        if(needDequeueAll)
        	dequeueAll(); // elimina eventuali elementi pendenti del FS dalla coda dei set
        LoggerMgr.getLogger(this.getClass().getName()).info(this.getClass().getName() + " :  DEQUEUE_OK");
        
        // Log stop plugin FS02
        EventMgr.getInstance().info(1,user,"fs","FS02",null);	
    }

    protected void dequeueAll()
	{
		SetDequeuerMgr.getInstance().dequeueAllByPriority(PriorityMgr.getInstance().getPriority(this.getClass().getName()));
	}
    
    public synchronized FSClock getClock()
    {
        return clock;
    }

    public synchronized void setClock(FSClock clock)
    {
        this.clock = clock;
    }

    public synchronized FSRack[] getRacks()
    {
        return racks;
    }
    public boolean hasSolenoidNull(FSRack rack)
    {
    	for (FSRack temp: racks)
        {
        	 if(rack.getId_rack().intValue() == temp.getId_rack().intValue())
        	 {
        		 return temp.isSolenoidNullError();
        	 }
        }
    	return false;
    }
    public int[] getRackSatus(FSRack rack)
    {
    	for (FSRack temp: racks)
        {
        	 if(rack.getId_rack().intValue() == temp.getId_rack().intValue())
        	 {
        		 rack = temp;
        		 break;
        	 }
        }
    	int[] leds = new int[STATUS_SIZE];
	    for(int j=0;j<STATUS_SIZE;j++)
	    {
	    	leds[j] = 0;
	    }
		for(FSUtil util:rack.getUtils())
		{
			int status = util.getLatestLedStatus();
			if(status == STATUS_NULL)
				leds[NULL]++;
			else if(status == STATUS_SOLENOIDNULL)
				leds[SOLENOIDNULL]++;
			else if(status == STATUS_GREEN)
				leds[GREEN]++;
			else if(status == STATUS_YELLOW)
				leds[YELLOW]++;
			else if(status == STATUS_YELLOW_OFFLINE)
				leds[YELLOW_OFFLINE]++;
			else if(status == STATUS_ORANGE)
				leds[ORANGE]++;
			else if(status == STATUS_ORANGE_OFFLINE)
				leds[ORANGE_OFFLINE]++;
			else if(status == STATUS_RED)
				leds[RED]++;
			else if(status == STATUS_RED_OFFLINE)
				leds[RED_OFFLINE]++;
		}
		return leds;
    }
    public synchronized FSRack getRackByIdRack(int idrack)
    {
        for (int i=0;i<racks.length;i++)
        {
        	if (idrack==this.racks[i].getId_rack().intValue())
        	{
        		return this.racks[i];
        	}
        }
        return null;
    }
    
    public static Integer getMaxnumretry() {
		return maxnumretry;
	}

	public static Integer getMaxracks() {
		return maxracks;
	}

	public static Integer getMaxunits() {
		return maxunits;
	}

	public static Integer getPolling() {
		return polling;
	}

	public static Integer getRetryinterval() {
		return retryinterval;
	}
	
	public static Integer getStart() {
		return start;
	}

	public static void setStart(Integer start) {
		FSManager.start = start;
	}

	//storicizzazione su finestra temporale ciclica in memoria di ogni solenoide
    public synchronized void samplingSolenoidStatus(FSRack[] racks)
    {
        FSRack rack = null;
        FSUtil util = null;
        Boolean val = null;
        Boolean rev_logic = null;
        float value = -1;
        FSUtil[] utils = null;
        
        for (int i = 0; i < racks.length; i++)
        {
        	rack = racks[i];
        	
        	if( rack.isNewAlg() && rack.getClock() < t )
        		continue; // for new alg there is no need to sample at each cycle
        	else
        		rack.setSamplingTime();
        	
        	List<Integer> solenoidNullUtils = new ArrayList<Integer>(); // utilities doesn't have solenoid variable
        	utils = rack.getUtils();

            for (int j = 0; j < utils.length; j++)
            {
            	util = utils[j];
            	rev_logic = rack.isNewAlg() ? false : util.getReverse_logic();
            	try
                {
                	//MPXPRO version check, version<3300 doesn't support smooth line
            		if( rack.isNewAlg())
                	{
            			rack.setSolenoidNullError(false);
            			VarphyBean versionV = VarphyBeanList.retrieveVarByCode(1, "FW rel", util.getIdutil(), this.defaultLanguage);
            			if(versionV != null)
            			{
            				Variable v = ControllerMgr.getInstance().getFromField(versionV);
            				if(v.getCurrentValue() != Float.NaN && v.getCurrentValue()<3300)
            				{
            					solenoidNullUtils.add(util.getIdutil());
            					util.setSolenoidNull(true);
            				}
            				else if(v.getCurrentValue()>=3300)
            				{
            					versionV = VarphyBeanList.retrieveVarByCode(1, "PSM", util.getIdutil(), this.defaultLanguage);
            					if(versionV != null)
            					{
            						v = ControllerMgr.getInstance().getFromField(versionV);
            						if(v.getCurrentValue() != Float.NaN && v.getCurrentValue() == 0)
            						{
            							solenoidNullUtils.add(util.getIdutil());
                    					util.setSolenoidNull(true);
            						}
            					}
            				}
            			}
                	}
            		Variable v = fieldvariables.get(util.getIdsolenoid());
            		if(v==null)
            			continue;
            		v.getRetriever().retrieve(v);
                    value = v.getCurrentValue();
                    
                    if (value >= 0.99f)
                    {
                    	if (!rev_logic)
                    		val = true;
                    	else
                    		val = false;
                    }
                    else if (value <= 0.01f)
                    {
                    	if (!rev_logic)
                    		val = false;
                    	else
                    		val = true;
                    }
                    else
                    {
                    	val = null;  //quando dal campo arriva Nan, cio� non tiro su un valore consistente
                    	offlinecounter++;
                    }
                    
                    if( rack.isNewAlg() ) {
                    	util.setD(val);
                    }
                    else
                    	util.getStatus().add(val); //aggiungo il boolean nella finestra temporale
                }
                catch (Exception e)
                {
                	val = null;
                	offlinecounter++;
                	Logger logger = LoggerMgr.getLogger(FSManager.class);
                    logger.error(e);
                }
            }
            if(offlinecounter/utils.length>=10)
            {
//            	stopFS("admin");
//            	try {
//					startFS("admin");
//				} catch (DataBaseException e) {
//					e.printStackTrace();
//				}
            	clock.loadcache();
				offlinecounter=0;
            }
            if(rack.isNewAlg() && solenoidNullUtils.size()>0)
            {
            	rack.setSolenoidNullError(true);
            	EventMgr.getInstance().warning(1, "System", "fs", "FS12",
                		new Object[] { DevicesList.logDevices(solenoidNullUtils), solenoidNullUtils.size(), getRackDescription(rack) });
            }
        }
    }
    
    /* Il seguente metodo viene eseguito ad ogni ciclo di clock. 
     * Ad ogni ciclo vengono verificati gli stati dei solenoidi di ogni centrale e campionati.
     * Se al ciclo di clock corrente � l'ora di effettuare il calcolo del Duty Cycle per una centrale, si effettua l'algoritmo
     * con conseguente modifica del setpoint della centrale
     */
    
    public synchronized void executeFSPressureControl(long sleep) throws Exception
    {
        FSRack rack = null;
        Integer r_clock = null;
        String[] dc_partial = null;
        FSUtil[] utils = null;
        String result = null;
        
        // *** SAMPLING SOLENOIDI ***
       	samplingSolenoidStatus(racks);
        
        //ciclo sulle *** CENTALI ***
        for (int i = 0; i < racks.length; i++)
        {
        	rack = racks[i];
            r_clock = rack.getClock();
            
            if( rack.isNewAlg() ) {
            	if(r_clock >= t ) {
            		if(!rack.getFirstSet() )
            		{
                    	try
                    	{
        	        		securityRackSet(rack);
        	            	rack.setFirstSet(true);
        				} 
                    	catch (Exception e)
        				{
                    		Logger logger = LoggerMgr.getLogger(FSManager.class);
                            logger.error(e);
        				}
            		}
            		utils = rack.getUtils();
            		
            	    int[] leds = new int[STATUS_SIZE];
            	    for(int j=0;j<STATUS_SIZE;j++)
            	    {
            	    	leds[j] = 0;
            	    }
            		for(FSUtil util:utils)
            		{
            			int status = util.getLatestLedStatus();
            			if(status == STATUS_NULL)
            				leds[NULL]++;
            			else if(status == STATUS_SOLENOIDNULL)
            				leds[SOLENOIDNULL]++;
            			else if(status == STATUS_GREEN)
            				leds[GREEN]++;
            			else if(status == STATUS_YELLOW)
            				leds[YELLOW]++;
            			else if(status == STATUS_YELLOW_OFFLINE)
            				leds[YELLOW_OFFLINE]++;
            			else if(status == STATUS_ORANGE)
            				leds[ORANGE]++;
            			else if(status == STATUS_ORANGE_OFFLINE)
            				leds[ORANGE_OFFLINE]++;
            			else if(status == STATUS_RED)
            				leds[RED]++;
            			else if(status == STATUS_RED_OFFLINE)
            				leds[RED_OFFLINE]++;
            		}
            		
            		result = newAlgorithmCalculateResult(leds,rack);
            		
	                if( !FSCalculateDC.INVARIATE.equalsIgnoreCase(result) ) {
						SetContext setContext = new SetContext();
						setContext.setUser("System");
						setContext.setLanguagecode(this.defaultLanguage);
						setContext.setLoggable(false);
						setContext.setCallback(new FSSetCallback());
						FSCalculateDC.setRack(rack, result, setContext);
						SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
	                }
	                Variable v = FSCalculateDC.getRackSetpointVar(rack);
	            	if(v != null)
	            	{
	            		rack.changeSetpoint(v.getCurrentValue());
	            	}
            		rack.setClock(0);
            	}
            	else {
            		rack.setClock(r_clock + (int)(sleep/1000));
            	}
            }
            else {
                //primo set centrale: set minimo
                if (!rack.getFirstSet())
                {
                	try
                	{
    	        		securityRackSet(rack);
    	            	rack.setFirstSet(true);
    				} 
                	catch (Exception e)
    				{
                		Logger logger = LoggerMgr.getLogger(FSManager.class);
                        logger.error(e);
    				}
                }
            	
	            //controllo se il rack � a regime, cio� la finestra di storico � piena
	            if (!rack.getReady())
	            {
	            	if (r_clock>=rack.getTimewindow())
	            	{
	            		rack.setReady(true);
	            		//log finestra storico carica, si inizia a funzionare
	            	}
	            }
	            
	            // se a regime, controllo se � l'ora di eseguire il controllo sulla singola istanza di Rack  
	            if (rack.getReady()&&r_clock > rack.getWaittime())
	            {
	                utils = rack.getUtils();
	                dc_partial = new String[rack.numberOfUtils()];
	
	                // ciclo sui *** BANCHI ***
	                for (int j = 0; j < rack.numberOfUtils(); j++)
	                {
	                    dc_partial[j] = FSCalculateDC.calculatePartialDC(utils[j],rack.getMaxofftime());
	                }
	
	                // in base ai DC dei vari banchi, traggo conclusioni su come agire sul set
	                result = FSCalculateDC.calculateRackDC(dc_partial,rack.getMaxoffutil(),getRackDescription(rack));
	                // modifica del setpoint della centrale, se rientra nei criteri di max e min
	                if (!"inv".equalsIgnoreCase(result))
	                {
	                	SetContext setContext = new SetContext();
	                    setContext.setUser("System");
	                    setContext.setLanguagecode(this.defaultLanguage);
	                    setContext.setLoggable(false);
	                	setContext.setCallback(new FSSetCallback());
		                FSCalculateDC.setRack(rack, result, setContext);
		                SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
	                }
	                rack.setClock(0);
	            }
	            else
	            {
	                //incremento il tempo passato
	                rack.setClock(r_clock + (int) (sleep/1000));
	            }
            }
        }
    }
    
    public String newAlgorithmCalculateResult(int[] leds,FSRack rack)
    {
    	int Maxoffutil = rack.getMaxoffutil();
    	if(rack.isSolenoidNullError())
    		return FSCalculateDC.SOLENOIDNULL;
    	if(leds[NULL]>=Maxoffutil)
    		return FSCalculateDC.OFFLINE;
		//step 1, check decrease
		if((leds[RED]+leds[RED_OFFLINE])>=RED_Q)
		{
			EventMgr.getInstance().info(1, "System", "fs", "FS14",
            		new Object[] { getRackDescription(rack),leds[RED]+leds[RED_OFFLINE] });
			return FSCalculateDC.DECREMENT;
		}
		//step 2, check inv
		if((leds[RED]+leds[RED_OFFLINE])>0 ||(leds[ORANGE]+leds[ORANGE_OFFLINE])>=ORANGE_Q || (leds[YELLOW]+leds[YELLOW_OFFLINE])>=YELLOW_Q)
		{
			EventMgr.getInstance().info(1, "System", "fs", "FS15",
            		new Object[] { getRackDescription(rack),leds[RED]+leds[RED_OFFLINE],leds[ORANGE]+leds[ORANGE_OFFLINE],leds[YELLOW]+leds[YELLOW_OFFLINE] });
			return FSCalculateDC.INVARIATE;
		}
		//step 3, check inc
		EventMgr.getInstance().info(1, "System", "fs", "FS16",
        		new Object[] { getRackDescription(rack),leds[RED]+leds[RED_OFFLINE],leds[ORANGE]+leds[ORANGE_OFFLINE],leds[YELLOW]+leds[YELLOW_OFFLINE] });
		return FSCalculateDC.INCREMENT;
    }
    
    public static void loadFSProperties() throws DataBaseException
    {
    	String sql = "select * from fsconfig";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,null);
    	Record r  = null;
    	Map<String,Integer> fsProperties = new HashMap<String,Integer>();
    	if (rs!=null)
    	{
    		for (int i = 0 ; i<rs.size();i++)
    		{
    			r = rs.get(i);
    			fsProperties.put(r.get(0).toString(),(Integer) r.get(1));
    		}
    	}
    	maxracks = fsProperties.get(MAX_RACKS);
    	maxunits = fsProperties.get(MAX_UNITS);
    	maxnumretry = fsProperties.get(MAX_NUM_RETRY);
    	retryinterval = fsProperties.get(RETRY_INTERVAL);
    	polling = fsProperties.get(POLLING);
    	start = fsProperties.get(AUTOSTART);
    	
    	// new algorithm
    	Sb = fsProperties.get("Sb");
    	t = fsProperties.get("t");
    	//Q = fsProperties.get("Q");
    	YELLOW_Q = fsProperties.get("YELLOW_Q");
    	ORANGE_Q = fsProperties.get("ORANGE_Q");
    	RED_Q = fsProperties.get("RED_Q");
    }
    
    public static Integer getSb()
    {
    	return Sb;
    }
    
    public static Integer getT()
    {
    	return t;
    }
    
    
    
//    public static Integer getQ()
//    {
//    	return Q;
//    }
    
    public static Integer getYELLOW_Q() {
		return YELLOW_Q;
	}


	public static Integer getORANGE_Q() {
		return ORANGE_Q;
	}


	public static Integer getRED_Q() {
		return RED_Q;
	}


	public synchronized boolean isRunning()
    {
    	if (clock!=null)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public static void securityRackSet(FSRack rack) throws Exception
    {
    	//retrieve dati necessari
    	boolean old_type = "old".equalsIgnoreCase(rack.getAux())?true:false;
//    	Float min_set = old_type?rack.getId_minset():ControllerMgr.getInstance().getFromField(rack.getId_minset().intValue()).getCurrentValue();
//    	Float grad = old_type?rack.getId_gradient():ControllerMgr.getInstance().getFromField(rack.getId_gradient().intValue()).getCurrentValue();

    	Float min_set;
    	Float grad;
    	
    	if(old_type)
    	{
        	min_set = rack.getId_minset();
        	grad = rack.getId_gradient();
    	}
    	else
    	{
    		Variable v1 = getInstance().fieldvariables.get(rack.getId_minset().intValue());
    		Variable v2 = getInstance().fieldvariables.get(rack.getId_gradient().intValue());
    		if(v1==null || v2==null){
    			return;
    		}
    		v1.getRetriever().retrieve(v1);
    		v2.getRetriever().retrieve(v2);
    		if(Float.isNaN(v1.getCurrentValue()) || Float.isNaN(v2.getCurrentValue())){
	        	return;
    		}
        	min_set = v1.getCurrentValue();
        	grad = v2.getCurrentValue();
    	}
    	
    	SetContext set_c;
    	SetWrp sw;
    	//set gradiente dummy per permettere il set
    	if (!old_type)
    	{
    		set_c = new SetContext();
    		String lang = "EN_en";
    		try
    		{
    			lang = LangUsedBeanList.getDefaultLanguage(1);
    		}
    		catch (Exception e)
    		{
    		}
    		set_c.setLanguagecode(lang);
        	set_c.setLoggable(false);
        	set_c.setCallback(new FSSetCallback());
        	sw = set_c.addVariable(rack.getId_gradient().intValue(),DUMMY_GRAD);
			sw.setCheckChangeValue(false);					
        	SetDequeuerMgr.getInstance().add(set_c);
    	}
    	try{
    		Thread.sleep(3000l);
    	}catch(Exception e){}
    	
    	//set del setpoint al minimo consentito
    	set_c = new SetContext();
    	String lang = "EN_en";
		try
		{
			lang = LangUsedBeanList.getDefaultLanguage(1);
		}
		catch (Exception e)
		{
		}
		set_c.setLanguagecode(lang);
    	set_c.setLoggable(false);
    	set_c.setCallback(new FSSetCallback());
    	sw = set_c.addVariable(rack.getId_setpoint().intValue(),min_set);
    	//rack.changeSetpoint(min_set);
		sw.setCheckChangeValue(false);					
    	SetDequeuerMgr.getInstance().add(set_c);
    	
    	//ritorno al gradiente corretto dopo il setpoint portato al minimo
    	if (!old_type)
    	{
        	set_c = new SetContext();
    		set_c.setLanguagecode(lang);
        	set_c.setLoggable(false);
        	set_c.setCallback(new FSSetCallback());
        	sw = set_c.addVariable(rack.getId_gradient().intValue(),grad);
			sw.setCheckChangeValue(false);					
        	SetDequeuerMgr.getInstance().add(set_c);
    	}
    }

	public synchronized HashMap<Integer, Variable> getFieldvariables() {
		return fieldvariables;
	}
	
	public synchronized void setFieldvariables(HashMap<Integer, Variable> fv) {
		fieldvariables = fv;
	}
	
	public static boolean isDeviceInRack(int iddevice, int idRack) throws DataBaseException {
        String sql = "select * from fsutil where ? IN (select idutil from fsutil where idrack = ? )";
        Object[] param = new Object[2];
        param[0] = new Integer(iddevice);
        param[1] = new Integer(idRack);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        int[] result = new int[rs.size()];

        if (result.length > 0)
        	return true;
        else
        	return false;		
	}

	public String getRackDescription(FSRack rack)
	{
		if(rackDescriptionMap != null)
		{
			return rackDescriptionMap.get(rack.getId_rack());
		}
		return rack.getDescription();
	}
	public static void updateFSProperty(String key, Integer value)
		throws DataBaseException 
	{
        String sql = "update fsconfig set value = ? where key = ?;";
        DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { value, key });
	}
}
