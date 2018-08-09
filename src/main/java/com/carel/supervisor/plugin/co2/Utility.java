package com.carel.supervisor.plugin.co2;

import java.util.*;

import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.setfield.*;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.setfield.SetWrp;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.device.DeviceStatusMgr;


public class Utility {
	private int idDevice;
	// 1st variable
	private int idVariable1		= 0;
	private float fStatus1Off	= 0;
	private float fStatus1On	= 0;
	// 2nd variable
	private int idVariable2		= 0;
	private float fStatus2Off	= 0;
	private float fStatus2On	= 0;
	
	
	public Utility(int idDevice)
	{
		this.idDevice = idDevice;
		loadUtility();
	}
	
	
	public void turnOn(SetContext objContext)
	{
		SetWrp sw = objContext.addVariable(idVariable1, fStatus1On);
		sw.setCheckChangeValue(false);
		if( idVariable2 > 0 ) {
			sw = objContext.addVariable(idVariable2, fStatus2On);
			sw.setCheckChangeValue(false);
		}
	}
	
	
	public void turnOff(SetContext objContext)
	{
		SetWrp sw = objContext.addVariable(idVariable1, fStatus1Off);
		sw.setCheckChangeValue(false);
		if( idVariable2 > 0 ) {
			sw = objContext.addVariable(idVariable2, fStatus2Off);
			sw.setCheckChangeValue(false);
		}
	}

	
	public int getUtilityStatus()
	{
		try {
			float fValue = ControllerMgr.getInstance().getFromField(idVariable1).getCurrentValue();
			if( !Float.isNaN(fValue) ) {
				if( fValue == fStatus1On )
					return Status.STATUS_RUNNING_CONDITION;
				else if( fValue == fStatus1Off )
					return Status.STATUS_SAFE_CONDITION;
				else
					return Status.STATUS_NOT_AVAILABLE;
			}
			else {
				return Status.STATUS_NOT_AVAILABLE;
			}
		} catch (Exception e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return Status.STATUS_NOT_AVAILABLE;
		}
	}
	
	
	private void loadUtility()
	{
		String sqlMdl = "SELECT * FROM co2_devmdl WHERE devcode="
			+ "(SELECT code FROM cfdevmdl WHERE iddevmdl = (SELECT iddevmdl FROM cfdevice WHERE iddevice=?))"
			+ " AND israck=FALSE;";
		String sqlVar = "SELECT idvariable FROM cfvariable WHERE iddevice=? AND code=?"
			+ " ORDER BY idvariable;";
    	try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sqlMdl, new Object[] { idDevice });
			if( rs.size() > 0 ) {
				Record r = rs.get(0);
				// 1st variable
				String varCode = r.get("var1").toString();
				fStatus1Off = Float.parseFloat(r.get("var2").toString());
				fStatus1On = Float.parseFloat(r.get("var3").toString());
				rs = DatabaseMgr.getInstance().executeQuery(null, sqlVar, new Object[] { idDevice, varCode });
				if( rs.size() > 0 )
					idVariable1 = (Integer)rs.get(0).get(0);
				// 2nd variable
				varCode = (String)r.get("var4");
				if( varCode != null ) {
					fStatus2Off = Float.parseFloat(r.get("var5").toString());
					fStatus2On = Float.parseFloat(r.get("var6").toString());
					rs = DatabaseMgr.getInstance().executeQuery(null, sqlVar, new Object[] { idDevice, varCode });
					if( rs.size() > 0 )
						idVariable2 = (Integer)rs.get(0).get(0);
				}
			}
			else {
				LoggerMgr.getLogger(this.getClass()).error("Unable to find safe restore utility model for iddevice = " + idDevice);
			}
		} catch (DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	public boolean isOffline()
	{
		return DeviceStatusMgr.getInstance().isOffLineDevice(idDevice);
	}
}
