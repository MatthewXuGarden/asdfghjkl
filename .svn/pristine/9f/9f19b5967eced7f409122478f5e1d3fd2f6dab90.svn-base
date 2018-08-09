package com.carel.supervisor.plugin.co2;

import java.util.*;
import com.carel.supervisor.base.log.*;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.*;
import com.carel.supervisor.dataaccess.db.*;
import com.carel.supervisor.dataaccess.event.*;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.presentation.events.DevicesList;


public class CO2SavingManager implements Runnable
{
    // single instance
    private static CO2SavingManager myInstance = new CO2SavingManager();
    private static final int OFFLINE_NOACTION_DELAY = 10; // 10 minutes
    
    // parameters
    private boolean bEnabled;
    private int nMaxMasterOfflineTime;	// in minutes
    private int nMasterOfflineAction;	// 1 - running mode, 0 - safe mode, -1 current mode
    public static final int RUNNING_MODE	= 1;
    public static final int SAFE_MODE		= 0;
    public static final int CURRENT_MODE	= -1;
    private int nGroupOnSwitchDelay;	// in minutes
    private Date pluginStartTime = null;
    
    // racks
    private Vector<Rack> racks;
    
    // thread
    private boolean bRun = false;
    private int nClock = 10 * 1000;		// 10 seconds, there are 6 thread cycles / minute
    private Thread threadAlg = null;
    
    
    private CO2SavingManager()
    {
    }
   
    
    public static CO2SavingManager getInstance()
    {
    	return myInstance;
    }

    
    public void setMaxMasterOfflineTime(int n)
    {
    	nMaxMasterOfflineTime = n;
    	setParam("max_master_offline_time", String.valueOf(nMaxMasterOfflineTime));
    }

    
    public int getMaxMasterOfflineTime()
    {
    	return nMaxMasterOfflineTime;
    }
    

    public int getMaxMasterOfflineCycles()
    {
    	return nMaxMasterOfflineTime * 6; // there are 6 cycles / minute
    }
    
    
    public void setMasterOfflineAction(int n)
    {
    	nMasterOfflineAction = n;
    	setParam("master_offline_action", String.valueOf(nMasterOfflineAction));
    }

    
    public int getMasterOfflineAction()
    {
    	return nMasterOfflineAction;
    }
    
    
    public void setGroupOnSwitchDelay(int n)
    {
    	nGroupOnSwitchDelay = n;
    	setParam("group_on_switch_delay", String.valueOf(nGroupOnSwitchDelay));
    }
    
    
    public int getGroupOnSwitchDelay()
    {
    	return nGroupOnSwitchDelay;
    }
    
    
    public int getGroupOnSwitchCycles()
    {
    	return nGroupOnSwitchDelay * 6; // there are 6 cycles / minute
    }

    
    public void run() {
    	// initialization
    	if( bRun ) {
    		pluginStartTime = new Date();
			init();
    	}
    	
    	// execution cycles
		while( bRun ) {
			execute();
			try {
				Thread.sleep(nClock);
			}
			catch(InterruptedException e) {
			}
		}
		
		// done
		threadAlg = null;
		pluginStartTime = null;
	}

	
    // initialize algorithm entities
    public synchronized void init()
    {
    	for(Iterator<Rack> it = racks.iterator(); it.hasNext();)
    		it.next().init();
    }

    
	// execute CO2 Saving algorithm for each rack configured
    public synchronized void execute()
    {
    	for(Iterator<Rack> it = racks.iterator(); it.hasNext();)
    		it.next().execute();
    }
    
    
    public synchronized void enable(boolean b)
    {
    	bEnabled = b;
    	setParam("enabled", String.valueOf(bEnabled));
    }
    
    
    // *** START PLUGIN ***
    public synchronized void start(String user)
    {
    	loadConfig();
    	loadRacks();
    	if(racks.size()>0)
    	{
			bRun = true;
	    	threadAlg = new Thread(this, "co2saving");
	    	threadAlg.start();
	       	EventMgr.getInstance().info(1, user, "co2", "CO201", null);
    	}
    }
    
    
    // *** STOP PLUGIN ***
    public synchronized void stop(String user)
    {
    	bRun = false;
    	if( threadAlg != null )
    		threadAlg.interrupt();
    	EventMgr.getInstance().info(1, user, "co2", "CO202", null);
    }
    
    public synchronized void configurationChanged(String user)
    {
		try {
	    	if( isRunning() ) {
	    		stop(user);
	    		start(user);
	    	}
	    	else {
	    		init();
	    	}
		} catch(Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
    }
    
    public boolean isEnabled()
    {
    	return bEnabled;
    }
    
    
    public boolean isRunning()
    {
    	return bRun;
    }
    
    
    public synchronized void quickRunning()
    {
    	if( bRun) {
    		if(isDuringNoActionPeriod())
    			return;
	    	for(Iterator<Rack> it = racks.iterator(); it.hasNext();)
	    		it.next().quickRunning();
    	}
    }
    
    
    //the next 10 minutes after plugin started is "No Action" period
    public boolean isDuringNoActionPeriod()
    {
		if(this.pluginStartTime != null)
		{
			Calendar c = Calendar.getInstance();
			c.setTime(this.pluginStartTime);
			c.add(Calendar.MINUTE, OFFLINE_NOACTION_DELAY);
			if(c.getTime().after(new Date()))
				return true;
		}
		return false;
    }
    
    
    public void loadConfig()
    {
    	bEnabled = getParam("enabled").equalsIgnoreCase("true");
    	nMaxMasterOfflineTime = Integer.parseInt(getParam("max_master_offline_time"));
    	nMasterOfflineAction = Integer.parseInt(getParam("master_offline_action"));
    	nGroupOnSwitchDelay = Integer.parseInt(getParam("group_on_switch_delay"));
    }
    
    
    private String getParam(String param)
    {
    	String value = null;
    	String sql = "SELECT val FROM co2_config WHERE param = ?;";
    	try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { param });
			if( rs.size() > 0 )
				value = rs.get(0).get(0).toString();
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
    	return value;
    }
    

    private void setParam(String param, String value)
    {
    	String sql = "UPDATE co2_config SET val = ? WHERE param = ?;";
    	try {
			DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { value, param });
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
    }
    
    
    private void loadRacks()
    {
    	racks = new Vector<Rack>();
    	//only load Rack with group accociated
    	String sql = "select distinct co2_rack.* from co2_rack "+
    		"inner join co2_rackgroups on co2_rackgroups.idrack=co2_rack.idrack";
    	try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				int idRack = (Integer)r.get("idrack");
				int idCO2devmdl = (Integer)r.get("idco2devmdl");
				int idDevice = (Integer)r.get("iddevice");
				int idVariable = (Integer)r.get("idvariable");
				// backup configuration
				int idBackupDevice = (Integer)r.get("idbackupdevice");
				int idBackupVariable = (Integer)r.get("idbackupvariable");
				boolean bBackupOffline = (Boolean)r.get("offline");
				String strOperation = (String)r.get("operation");
				float fConstant = (Float)r.get("constant");
				Rack rack = new Rack(idRack, idCO2devmdl, idDevice, idVariable,
					idBackupDevice, idBackupVariable, bBackupOffline, strOperation, fConstant);
				racks.add(rack);
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
    }
    
    
    public synchronized int getRackStatus(int idRack)
    {
    	for(Iterator<Rack> it = racks.iterator(); it.hasNext();) {
    		Rack rack = it.next();
    		if( rack.getIdRack() == idRack )
    			return rack.getRackStatus();
    	}
    	return 0;
    }
    public int getRackNumAtSafeMode()
    {
    	int num = 0;
    	if(racks == null)
    		return num;
    	for(Iterator<Rack> it = racks.iterator(); it.hasNext();) {
    		Rack rack = it.next();
    		if(rack.getRackStatus() == Status.STATUS_SAFE_CONDITION)
    			num++;
    	}
    	return num;
    }
    public int getRackNumInBackupCondition()
    {
    	int num = 0;
    	if(racks == null)
    		return num;
    	for(Iterator<Rack> it = racks.iterator(); it.hasNext();) {
    		Rack rack = it.next();
    		if(rack.isInBackupCondition())
    			num++;
    	}
    	return num;
    }
    
    
    public synchronized int getGroupStatus(int idRack, int idGroup)
    {
    	for(Iterator<Rack> it = racks.iterator(); it.hasNext();) {
    		Rack rack = it.next();
    		if( rack.getIdRack() == idRack )
    			return rack.getGroupStatus(idGroup);
    	}
    	return Status.STATUS_NOT_AVAILABLE;
    }
    
    
    public synchronized Vector<Vector<Status>> getStatusTable()
    {
    	Vector<Vector<Status>> statusTable = new Vector<Vector<Status>>();
    	for(Iterator<Rack> it = racks.iterator(); it.hasNext();) {
    		Vector<Status> rackStatusTable = it.next().getStatusTable();
    		statusTable.add(rackStatusTable);
    	}
    	return statusTable;
    }
}
