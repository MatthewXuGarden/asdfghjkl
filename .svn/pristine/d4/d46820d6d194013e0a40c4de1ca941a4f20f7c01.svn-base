package com.carel.supervisor.plugin.co2;

import java.util.*;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.*;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class Group {
	private int idGroup;
	private String strName;
	private boolean bEnabled;
	private int nDelayCounter		= 0;
	private boolean bOn				= false;
	private boolean bOff			= false;
	private int nGroupStatus		= Status.STATUS_NOT_AVAILABLE;
	private Vector<Utility> utilities;
	
	
	public Group(int idGroup)
	{
		this.idGroup = idGroup;
		loadGroup();
		loadUtilities();
	}
	
	
	public int getIdGroup()
	{
		return idGroup;
	}
	
	
	public boolean isEnabled()
	{
		return bEnabled;
	}
	
	
	public int getGroupStatus()
	{
		return nGroupStatus;
	}
	
	
	public void turnOn(int nDelay)
	{
		bOn = true;
		bOff = false;
		nDelayCounter = nDelay;
		LoggerMgr.getLogger(this.getClass()).info("Safe Restore: request running condition (ON) on group " + strName + " in " + nDelay + " minutes");		
	}
	
	
	public void turnOff()
	{
		bOff = true;
		bOn = false;
		LoggerMgr.getLogger(this.getClass()).info("Safe Restore: request safe condition (OFF) on group " + strName);		
	}
	
	
	// init group
	public void init()
	{
		int nRunningCondition = 0;
		int nSafeCondition = 0;
		int nNotAvailable = 0;
		for(Iterator<Utility> it = utilities.iterator(); it.hasNext();) {
			int nStatus = it.next().getUtilityStatus();
			if( nStatus == Status.STATUS_RUNNING_CONDITION )
				nRunningCondition++;
			else if( nStatus == Status.STATUS_SAFE_CONDITION )
				nSafeCondition++;
			else
				nNotAvailable++;
		}
		if( nRunningCondition > nSafeCondition && nRunningCondition > nNotAvailable )
			nGroupStatus = Status.STATUS_RUNNING_CONDITION;
		else if( nSafeCondition >= nRunningCondition && nSafeCondition > nNotAvailable )
			nGroupStatus = Status.STATUS_SAFE_CONDITION;
		else
			nGroupStatus = Status.STATUS_NOT_AVAILABLE;
	}
	
	
	// execute Safe restore algorithm
	public void execute()
	{
		if( (bOn && nDelayCounter == 0) || bOff ) {
			// set variable for each device from the group
			SetContext objContext = new SetContext();
			String lang = "EN_en";
			try	{
				lang = LangUsedBeanList.getDefaultLanguage(1);
			}
			catch (Exception e)	{
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
			objContext.setLanguagecode(lang);
			objContext.setUser("Safe Restore");
			objContext.setCallback(new BackGroundCallBack());
			if( bOn ) {
				nGroupStatus = Status.STATUS_RUNNING_CONDITION;
				for(Iterator<Utility> it = utilities.iterator(); it.hasNext();)
					it.next().turnOn(objContext);
				LoggerMgr.getLogger(this.getClass()).info("Safe Restore: set running condition (ON) on group " + strName);		
			}
			else if( bOff ) {
				nGroupStatus = Status.STATUS_SAFE_CONDITION;
				for(Iterator<Utility> it = utilities.iterator(); it.hasNext();)
					it.next().turnOff(objContext);
				LoggerMgr.getLogger(this.getClass()).info("Safe Restore: set safe condition (OFF) on group " + strName);		
			}
			if( objContext.numDevice() > 0 )
				SetDequeuerMgr.getInstance().add(objContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
			bOn = bOff = false;
		}
		
		if( nDelayCounter > 0 )
			nDelayCounter--;
	}
	
	
	private void loadGroup()
	{
		String sql = "SELECT * FROM co2_group WHERE idgroup=?;";
    	try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idGroup });
			if( rs.size() > 0 ) {
				Record r = rs.get(0);
				strName = (String)r.get("name");
				bEnabled = (Boolean)r.get("enabled");
				if( !bEnabled )
					nGroupStatus = Status.STATUS_GROUP_DISABLED;
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	private void loadUtilities()
	{
		utilities = new Vector<Utility>();
    	String sql = "SELECT * FROM co2_grouputilities WHERE idgroup=?;";
    	try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idGroup });
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				int idDevice = (Integer)r.get("iddevice");
				Utility utility = new Utility(idDevice);
				utilities.add(utility);
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	public boolean hasUtilityOffline()
	{
		for(Iterator<Utility> it = utilities.iterator(); it.hasNext();) 
		{
			Utility util = it.next();
			if(util.isOffline())
				return true;
		}
		return false;
	}
}
