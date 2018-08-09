package com.carel.supervisor.plugin.co2;

import java.util.*;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class Rack {
	private int idRack;
	private int idCO2devmdl			= 0;
	private int idDevice			= 0;
	private int idVariable			= 0;
	private float fValue			= Float.NaN;
	private boolean bOnline			= true;
	private int nOfflineCounter		= 0;
	private float fStatusOff		= 0;
	private int nRackStatus			= Status.STATUS_NOT_AVAILABLE;
	private Vector<Group> groups;
	// backup configuration
	private int idBackupDevice		= 0;
	private int idBackupVariable	= 0;
	private boolean bBackupOffline	= false;
	private String strOperation		= "=";
	private float fConstant			= 0;
	
	private int idBackupOfflineVariable = 0;
	
	
	public Rack(int idRack, int idCO2devmdl, int idDevice, int idVariable,
		int idBackupDevice, int idBackupVariable, boolean bBackupOffline, String strOperation, float fConstant)
	{
		this.idRack				= idRack;
		this.idCO2devmdl 		= idCO2devmdl;
		this.idDevice			= idDevice;
		this.idVariable			= idVariable;
		this.idBackupDevice		= idBackupDevice;
		this.idBackupVariable	= idBackupVariable;
		this.bBackupOffline		= bBackupOffline;
		this.strOperation		= strOperation;
		this.fConstant			= fConstant;
		loadRack();
		loadGroups();
	}
	

	public int getIdRack()
	{
		return idRack;
	}
	
	
	public int getRackStatus()
	{
		return nRackStatus;
	}
	
	
	// init rack
	public void init()
	{
		CO2SavingManager manager = CO2SavingManager.getInstance();
		try {
			fValue = ControllerMgr.getInstance().getFromField(idVariable).getCurrentValue();
			if( !Float.isNaN(fValue) ) {
				bOnline = true;
				nRackStatus = fValue == fStatusOff ? Status.STATUS_SAFE_CONDITION : Status.STATUS_RUNNING_CONDITION;
				//turn On/OFF when rack init, otherwise the turn ON/OFF wait until value changed
				if(nRackStatus == Status.STATUS_SAFE_CONDITION)
				{
					if(!(manager.isDuringNoActionPeriod() && hasGroupOffline()))
					{
						LoggerMgr.getLogger(this.getClass()).info("Safe Restore: online transition on rack id " + idRack + ", safe condition, value=" + fValue);		
						turnOff();
					}
				}
				else if(nRackStatus == Status.STATUS_RUNNING_CONDITION)
				{
					if(!(manager.isDuringNoActionPeriod() && hasGroupOffline()))
					{
						LoggerMgr.getLogger(this.getClass()).info("Safe Restore: running condition on rack id " + idRack + ", value=" + fValue);		
						turnOn();
					}
				}
			}
			else {
				bOnline = false;
				nRackStatus = Status.STATUS_NOT_AVAILABLE;
			}
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		// init rack groups
		for(Iterator<Group> it = groups.iterator(); it.hasNext();)
			it.next().init();
	}
	
	
	// execute CO2 Saving algorithm
	public void execute()
	{
		CO2SavingManager manager = CO2SavingManager.getInstance();
		try {
			Variable currentVariable = ControllerMgr.getInstance().getFromField(idVariable); 
			float fCurrentValue = currentVariable.getCurrentValue();

			if( !Float.isNaN(fCurrentValue)  ) {
				if( bOnline ) {
					if( fCurrentValue != fValue
						&& (fCurrentValue == fStatusOff || fValue == fStatusOff) )
						if( fCurrentValue == fStatusOff ) {
							if(!(manager.isDuringNoActionPeriod() && hasGroupOffline()))
							{
								LoggerMgr.getLogger(this.getClass()).info("Safe Restore: safe condition on rack id " + idRack + ", value=" + fCurrentValue);		
								turnOff();	// safe transition
							}
						}
						else {
							if(!(manager.isDuringNoActionPeriod() && hasGroupOffline()))
							{
								LoggerMgr.getLogger(this.getClass()).info("Safe Restore: running condition on rack id " + idRack + ", value=" + fCurrentValue);		
								turnOn();	// running transition
							}
						}
				}
				else {
					// offline to online transition
					bOnline = true;
					if( fCurrentValue == fStatusOff ) {
						if(!(manager.isDuringNoActionPeriod() && hasGroupOffline()))
						{
							LoggerMgr.getLogger(this.getClass()).info("Safe Restore: online transition on rack id " + idRack + ", safe condition, value=" + fCurrentValue);		
							turnOff();	// switch groups to safe condition
						}
					}
					else {
						if(!(manager.isDuringNoActionPeriod() && hasGroupOffline()))
						{
							LoggerMgr.getLogger(this.getClass()).info("Safe Restore: online transition on rack id " + idRack + ", running condition, value=" + fCurrentValue);		
							turnOn();	// switch groups to running condition
						}
					}
				}
			}
			else {
				//for online->offline change
				if(bOnline)
					nOfflineCounter = 0;
				bOnline = false;
				if(nOfflineCounter==0 && isBackupCondition() ) {
					// start offline counter, means can start to change mode
					nOfflineCounter = 1;
					nRackStatus = Status.STATUS_NOT_AVAILABLE;
					LoggerMgr.getLogger(this.getClass()).info("Safe Restore: offline transition on rack id " + idRack + ", start offline counter");
				}
				else if(nOfflineCounter>0){
					//start to change mode, have to wait some offline cycles
					//if during waiting offline cycles, isBackupCondition fails, will not wait any more
					if( nOfflineCounter < CO2SavingManager.getInstance().getMaxMasterOfflineCycles() && isBackupCondition()) {
						if( ++nOfflineCounter == CO2SavingManager.getInstance().getMaxMasterOfflineCycles() ) {
							if( CO2SavingManager.getInstance().getMasterOfflineAction() == CO2SavingManager.RUNNING_MODE ) {
								LoggerMgr.getLogger(this.getClass()).info("Safe Restore: offline counter ends on rack id " + idRack + ", running condition");
								turnOn(); // switch groups to running condition
							}
							else if( CO2SavingManager.getInstance().getMasterOfflineAction() == CO2SavingManager.SAFE_MODE ) {
								LoggerMgr.getLogger(this.getClass()).info("Safe Restore: offline counter ends on rack id " + idRack + ", safe condition");
								turnOff(); // switch groups to safe condition
							}
							else {
								LoggerMgr.getLogger(this.getClass()).info("Safe Restore: offline counter ends on rack id " + idRack + ", maintain current condition");
								// maintain current condition
							}
							//after run the mode, nOfflineCounter==CO2SavingManager.getInstance().getMaxMasterOfflineCycles()
						}
					}
					//backup condition not satisfied any more
					else if(nOfflineCounter < CO2SavingManager.getInstance().getMaxMasterOfflineCycles() && !isBackupCondition())
						nOfflineCounter = 0;
				}
			}
			
			fValue = fCurrentValue;
		} catch (Exception e) {
			// nothing todo
		}
		
	    // execute CO2 Saving algorithm for each group associated with this rack
		for(Iterator<Group> it = groups.iterator(); it.hasNext();) {
			Group group = it.next();
			if( group.isEnabled() )
				group.execute();
		}
	}

	
	public void quickRunning()
	{
		if( bOnline )
			nRackStatus = Status.STATUS_RUNNING_CONDITION;
		for(Iterator<Group> it = groups.iterator(); it.hasNext();) {
			Group group = it.next();
			if( group.isEnabled() )
				group.turnOn(0);
		}
	}
	
	
	private void turnOn()
	{
		if( bOnline )
			nRackStatus = Status.STATUS_RUNNING_CONDITION;
		int i = 0;
		for(Iterator<Group> it = groups.iterator(); it.hasNext();) {
			Group group = it.next();
			if( group.isEnabled() ) {
				group.turnOn(i * CO2SavingManager.getInstance().getGroupOnSwitchCycles());
				i++;
			}
		}
	}
	
	
	private void turnOff()
	{
		if( bOnline )
			nRackStatus = Status.STATUS_SAFE_CONDITION;
		for(Iterator<Group> it = groups.iterator(); it.hasNext();)
			it.next().turnOff();
	}
	
	
	private void loadRack()
	{
		String sqlMdl = "SELECT * FROM co2_devmdl WHERE idco2devmdl=?;";
		/*
		String sqlVar = "SELECT idvariable FROM cfvariable WHERE iddevice=? AND code=?"
			+ " ORDER BY idvariable;";
		*/
    	try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sqlMdl, new Object[] { idCO2devmdl });
			if( rs.size() > 0 ) {
				Record r = rs.get(0);
				String varCode = r.get("var1").toString();
				fStatusOff = Float.parseFloat(r.get("var2").toString());
				/*
				rs = DatabaseMgr.getInstance().executeQuery(null, sqlVar, new Object[] { idDevice, varCode });
				if( rs.size() > 0 )
					idVariable = (Integer)rs.get(0).get(0);
				*/
			}
			else {
				LoggerMgr.getLogger(this.getClass()).error("Unable to find safe restore rack model for iddevice = " + idDevice);
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		
		// retrieve offline variable for backup device
		// only don't have backup variable, otherwise we use backup variable to check offline
		if( idBackupDevice > 0 && idBackupVariable == 0 && bBackupOffline ) {
			String sql = "SELECT idvariable FROM cfvariable WHERE iddevice=? AND code='OFFLINE';";
			try {
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idBackupDevice });
				if( rs.size() > 0 )
					idBackupOfflineVariable = (Integer)rs.get(0).get(0);
			} catch (DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
	}
	
	
	private void loadGroups()
	{
    	groups = new Vector<Group>();
    	String sql = "SELECT * FROM co2_rackgroups WHERE idrack=?;";
    	try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idRack });
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				int idGroup = (Integer)r.get("idgroup");
				Group group = new Group(idGroup);
				groups.add(group);
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
    }
	
	
	public synchronized int getGroupStatus(int idGroup)
	{
		for(Iterator<Group> it = groups.iterator(); it.hasNext();) {
			Group group = it.next();
			if( group.getIdGroup() == idGroup )
				return group.getGroupStatus();
		}
		return Status.STATUS_NOT_AVAILABLE;
	}
	
	
	public synchronized Vector<Status> getStatusTable()
	{
		Vector<Status> statusTable = new Vector<Status>();
		// rack status
		Status status = new Status();
		status.id = idRack;
		status.st = nRackStatus;
		statusTable.add(status);
		// group status
		for(Iterator<Group> it = groups.iterator(); it.hasNext();) {
			Group group = it.next();
			status = new Status();
			status.id = group.getIdGroup();
			status.st = group.getGroupStatus();
			statusTable.add(status);
		}
		return statusTable;
	}
	
	
	private boolean isBackupCondition()
	{
		if( idBackupDevice > 0 && (idBackupVariable>0 || idBackupOfflineVariable>0)) {
			try {
				//don't have backup Variable, only check offline
				if(idBackupVariable == 0)
				{
					Variable offlineVariable = ControllerMgr.getInstance().getFromField(idBackupOfflineVariable);
					if(offlineVariable != null)
						return offlineVariable.isDeviceOffLine();
					else
						return true;
				}
				else
				{
					Variable backupVariable = ControllerMgr.getInstance().getFromField(idBackupVariable);
					if( bBackupOffline && backupVariable.isDeviceOffLine())
						return true;
					float fBackupValue = backupVariable.getCurrentValue();
					if( strOperation.equals("=") )
						return fBackupValue == fConstant;
					else if( strOperation.equals(">") )
						return fBackupValue > fConstant;
					else if( strOperation.equals(">=") )
						return fBackupValue >= fConstant;
					else if( strOperation.equals("<") )
						return fBackupValue < fConstant;
					else if( strOperation.equals("<=") )
						return fBackupValue <= fConstant;
					else
						return fBackupValue != fConstant;
				}
			} catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
				return true;
			}
		}
		else {
			LoggerMgr.getLogger(this.getClass()).info("Safe restore: no offline backup condition on rack id " + idRack);
			return true;
		}
	}
	public boolean isInBackupCondition()
	{
		if(!bOnline && nOfflineCounter>0)
			return true;
		else
			return false;
	}
	public boolean hasGroupOffline()
	{
		for(Iterator<Group> it = groups.iterator(); it.hasNext();) {
			Group group = it.next();
			if(group.hasUtilityOffline())
				return true;
		}
		return false;
	}
}
