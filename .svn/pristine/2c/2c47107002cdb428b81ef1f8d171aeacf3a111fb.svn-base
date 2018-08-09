package com.carel.supervisor.presentation.copydevice;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.system.SystemInfoExt;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.MultiCastCallBack;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;

public class BroadcastThread extends Thread
{
	private final long RAM_MIN = 33;
	private final int CPU_MIN = 30;
	
	private Logger log = LoggerMgr.getLogger(this.getClass());
	private String user = "";
	
	private String sql = "";
	private int[] ids_devices = new int[0];
	private Map values = new HashMap();
	
	public BroadcastThread()
	{
        this("Broadcast Thread");
    }
	
	public BroadcastThread(String str)
	{
        	super(str);
    }
	
    public void run()
    {
        try
        {
        	int nVars = 0;
        	
        	RecordSet recordset = null;
	        Record r = null;
	        VariableInfo vbean = null;
	        
	        Object[] tmp = null;
	        String valnow = "";
        	
	        //EventMgr.getInstance().log(new Integer(1), user, "Action", EventDictionary.TYPE_INFO, "W050", null);
        	LoggerMgr.getLogger(BroadcastThread.class).info("Broadcast parameters enqueue START");
	        
        	for(int i = 0; i < ids_devices.length; i++)
	        {
        		 //controllo stato sistema (cpu e ram):
        		//while ((RamFree() < RAM_MIN) && (CpuFree() < CPU_MIN))
//        		int freeCpu = CpuFree();
//        		System.out.println("*****---***** Free cpu:"+freeCpu);
//        		while (freeCpu < 60) {        			
//        			System.out.println("++++++++++++++++++++ Current Free CPU:"+freeCpu);
//        			System.out.println("Sleep for 5 sec.");
//        			sleep((long) 5000);
//        			freeCpu = CpuFree();
//        		}
	        	
        		// The following is a fix, since the former system performance handling was
        		// faulty. 
        		sleep(7000);
        		
	        	tmp = new Object[]{new Integer(ids_devices[i])};
	        	nVars = 0; //conteggio variabili da propagare per il device in esame
	        	
	        	// creo un setContext x ogni device:
	        	SetContext setContext = new SetContext();
	        	String lang = "EN_en";
	    		try
	    		{
	    			lang = LangUsedBeanList.getDefaultLanguage(1);
	    		}
	    		catch (Exception e)
	    		{
	    		}
	    		setContext.setLanguagecode(lang);
	        	setContext.setCallback(new MultiCastCallBack());
		        setContext.setUser(user);
	        	
	        	try
	        	{
		        	recordset = DatabaseMgr.getInstance().executeQuery(null, sql, tmp);
		        	
		        	for(int j = 0; j < recordset.size(); j++)
		        	{
		        		r = recordset.get(j);
		        		valnow = "";
		        		
		        		vbean = new VariableInfo(r);
		        		valnow = ControllerMgr.getInstance().getFromField(vbean.getId().intValue()).getFormattedValue(); //idvariable

		        		//accodo solo var che non sono off-line:
		        		if ((valnow != null) && (!"***".equals(valnow)) && (!"".equals(valnow)))
		        		{
		        			setContext.addVariable(ControllerMgr.getInstance().retrieve(vbean), Float.parseFloat((String)values.get(vbean.getModel())));
		        			nVars++; //variabile da settare accodata
		        		}
		        		
		        		//Aggiungere variabili zippate LDAC TO DO
		        	}
	        	}
	        	catch(Exception e)
	        	{
	        		LoggerMgr.getLogger(BroadcastThread.class).error(e);
	        	}
	        	
	        	if (nVars > 0) //se ho vars da propagare
	        	{
	        		//attesa sul semaforo booleano della SetDequeuerMgr
	        		while (SetDequeuerMgr.getInstance().isWorking()) {
	        			Thread.sleep(500);
	        		}
	        		
	        		// accodo un setContext per ogni device:
	        		SetDequeuerMgr.getInstance().add(setContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
	        	}
	        	else //nessuna var da propagare: es. device off-line
	        	{
	        		String device = "";
	        		String langDef = "";
	        		
	        		try
	        		{
						langDef = LangUsedBeanList.getDefaultLanguage(1);
					}
	        		catch (Exception e1)
	        		{
	        			langDef = "EN_en";
					}
	        		
	        		String sql = "select description from cftableext where tablename='cfdevice' and languagecode='"+langDef+"' and " +
						" tableid="+ids_devices[i]+" and idsite=1";
					
	        		try
					{
						RecordSet rec = DatabaseMgr.getInstance().executeQuery(null, sql, null);
						device = (String)rec.get(0).get(0);
					}
					catch(Exception e)
					{
						device = "***";
					}
	        		
	        		EventMgr.getInstance().warning(new Integer(1), setContext.getUser(), "Action",
	    					"W056", new Object[] {""+setContext.getID(), device + " :  off-line"});
	        	}
	        }
        	
        	//EventMgr.getInstance().log(new Integer(1), user, "Action", EventDictionary.TYPE_INFO, "W051", null);
        	LoggerMgr.getLogger(BroadcastThread.class).info("Broadcast parameters enqueue END");
        
        }
        catch (Exception e)
        {
        	LoggerMgr.getLogger(BroadcastThread.class).error(e); //Broadcast parameters enqueue ERROR
        	EventMgr.getInstance().log(new Integer(1), user, "Action", EventDictionary.TYPE_INFO, "W055", null);
        }
    }
    
    public void setSQL(String sql2exe)
    {
    	this.sql = sql2exe;
    }
    
    public void setIdsDevs(int[] ids)
    {
    	this.ids_devices = ids;
    }
    
    public void setUsr(String usr)
    {
    	this.user = usr;
    }
    
    public void setValues(HashMap vals2Set)
    {
    	this.values = vals2Set;
    }
    
    private long RamFree()
    {
    	String usedS = SystemInfoExt.getInstance().getRamPerUsage();
    	usedS = usedS.substring(0, usedS.length() -1);
    	usedS = usedS.trim();
    	long used = 0;
		
    	try
    	{
			used = Long.parseLong(usedS);
		}
    	catch (Exception e)
    	{
    		LoggerMgr.getLogger(BroadcastThread.class).error(e);
		}
    	
    	return (100 - used);
    }
    
    private int CpuFree()
    {
    	String val = SystemInfoExt.getInstance().getCpuUsage();
    	int iVal = 0;
    	
    	try
    	{	
    		iVal = Integer.parseInt(val);
    	}
    	catch(Exception e)
    	{
    		LoggerMgr.getLogger(BroadcastThread.class).error(e);
    	}
    	
    	iVal = 100 - iVal;
    	
    	return iVal;
    }
}
