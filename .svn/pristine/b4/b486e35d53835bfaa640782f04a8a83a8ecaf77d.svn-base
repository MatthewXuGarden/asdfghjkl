package com.carel.supervisor.director.vscheduler;

import java.util.Vector;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.setfield.SetContext;
import java.sql.Timestamp;
import com.carel.supervisor.controller.setfield.SetWrp;
import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;

import java.util.GregorianCalendar;


public class CDevice {
	private int idDevice;
	private int idDevMdl;
	private int idGroup;
	private Vector<CVariable> objVariables;
	String strType;
	Timestamp objTimestamp;
	CInterval objLastInterval;
	private boolean bWait;
	//private int nWaitCounter;
	private boolean bError;
	private int nErrCounter;
	private static final int ERROR_CYCLES = 30; // 10 minutes
	private boolean bAutoReset;
	private boolean bCheck;
	
	
	public CDevice(int idDevice, int idDevMdl, int idGroup, Timestamp timestamp)
	{
		this.idDevice = idDevice;
		this.idDevMdl = idDevMdl;
		this.idGroup = idGroup;
		objVariables = new Vector<CVariable>();
		strType = CDataDef.strUndefVal;
		objTimestamp = timestamp;
		objLastInterval = null;
		bWait = false;
		//nWaitCounter = 0;
		bError = false;
		nErrCounter = 0;
		bAutoReset = false;
		bCheck = true;
	}
	
	
	public int getIdDevice()
	{
		return idDevice;
	}
	
	
	public int getIdDevMdl()
	{
		return idDevMdl;
	}
	
	
	public String getType()
	{
		return strType;
	}
	
	
	public void setVarInfo(int idVar, int nType, int nFlags, boolean bCheck)
	{
		// update device type
		if( strType.equals(CDataDef.strUndefVal) ) {
			switch( nType ) {
				case VariableInfo.TYPE_ANALOGIC:	strType = CDataDef.strAnalogVal; break;
				case VariableInfo.TYPE_INTEGER:		strType = CDataDef.strAnalogVal; break; 
				case VariableInfo.TYPE_DIGITAL:
				default:							strType = CDataDef.strDigitalVal; break;
			}
		}
		else {
			if( (strType.equals(CDataDef.strDigitalVal) && (nType == VariableInfo.TYPE_ANALOGIC || nType == VariableInfo.TYPE_INTEGER))
				|| (strType.equals(CDataDef.strAnalogVal) && nType == VariableInfo.TYPE_DIGITAL) )
				strType = CDataDef.strMixedVal;
		}
		
		CVariable var = new CVariable(idVar, nType, nFlags);
		objVariables.add(var);
		
		if( !bCheck )
			this.bCheck = bCheck;
	}
	
	
	private boolean isFlag(int nFlag)
	{
		for(int v = 0; v < objVariables.size(); v++) {
			CVariable var = objVariables.get(v); 
			if( var.IsDigital() && var.IsFlag(nFlag) )
				return true;
		}
		return false;
	}
	
	
	public synchronized void run(CInterval objInterval, SetContext objContext)
	{
		// return traps
		if( !bAutoReset ) {
			// error trap
			if( bError ) {
				if( --nErrCounter <= 0 || objInterval.getStartNumber() != objLastInterval.getStartNumber() )
					bError = false;
				else
					return;
			}
			// wait trap
			if( bWait ) {
				//nWaitCounter++;
				if( objInterval.getStartNumber() != objLastInterval.getStartNumber() )
					bWait = false; // just in case the response doesn't appear and a new command arrived
				else
					return;
			}
			// time trap
			GregorianCalendar gc = new GregorianCalendar();
			gc.set(GregorianCalendar.HOUR_OF_DAY, objInterval.getStartHour());
			gc.set(GregorianCalendar.MINUTE, objInterval.getStartMinute());
			gc.set(GregorianCalendar.SECOND, 0);
			Timestamp objStartTimestamp = new Timestamp(gc.getTime().getTime());
			if( objTimestamp.after(objStartTimestamp) )
				return;
		} // if( !bAutoReset )
		//else if( bWait )
		//	nWaitCounter++;
		// else continue with reset sequence
		CCommand objCommand = objInterval.getCommand();
		// set analog variable
		if( strType.equals(CDataDef.strMixedVal) || strType.equals(CDataDef.strAnalogVal) ) {
			if( !(objInterval.isFake() || bAutoReset) ) {
				for(int v = 0; v < objVariables.size(); v++) {
					CVariable var = objVariables.get(v);
					if( var.IsAnalog() ) {
						int idVar = var.GetIdVar();
						SetWrp sw = objContext.addVariable(idVar, objCommand.getAVal());
						sw.setCheckChangeValue(bCheck);
					}
				}
				bWait = true;
				//nWaitCounter = 0;
			}
		}
		// set/reset digital variable
		if( strType.equals(CDataDef.strDigitalVal) || strType.equals(CDataDef.strMixedVal) ) {
			if( isFlag(CDataDef.nAutoReset) ) {
				if( objInterval.isFake() ) {
					bAutoReset = false;
					for(int v = 0; v < objVariables.size(); v++) {
						CVariable var = objVariables.get(v);
						if( var.IsDigital() ) {
							int idVar = var.GetIdVar();
							int nDVal = var.IsFlag(CDataDef.nReverseLogic)
								? objCommand.getDVal() ? 0 : 1
								: objCommand.getDVal() ? 1 : 0;
							SetWrp sw = objContext.addVariable(idVar, nDVal);
							sw.setCheckChangeValue(bCheck);
						}
					}
					bWait = true;
					//nWaitCounter = 0;
				}
				else {
					for(int v = 0; v < objVariables.size(); v++) {
						CVariable var = objVariables.get(v);
						if( var.IsDigital() ) {
							if( var.IsFlag(CDataDef.nAutoReset) ) {
								int idVar = var.GetIdVar();
								int nDVal = bAutoReset
									? var.IsFlag(CDataDef.nReverseLogic)
										? objCommand.getDVal() ? 0 : 1
										: objCommand.getDVal() ? 1 : 0
									: var.IsFlag(CDataDef.nReverseLogic)
										? objCommand.getDVal() ? 1 : 0
										: objCommand.getDVal() ? 0 : 1;
								SetWrp sw = objContext.addVariable(idVar, nDVal);
								sw.setCheckChangeValue(bCheck);
							}
							else if( !bAutoReset ) {
								int idVal = var.GetIdVar();
								int nDVal = var.IsFlag(CDataDef.nReverseLogic)
									? objCommand.getDVal() ? 0 : 1
									: objCommand.getDVal() ? 1 : 0;
								SetWrp sw = objContext.addVariable(idVal, nDVal);
								sw.setCheckChangeValue(bCheck);
							}
						}
					}
					bWait = true;
					bAutoReset =! bAutoReset;
					//if( bAutoReset )
					//	nWaitCounter = 0;
				}
			} // if( isFlag(CDataDef.nAutoReset) )
			else {
				for(int v = 0; v < objVariables.size(); v++) {
					CVariable var = objVariables.get(v);
					if( var.IsDigital() ) {
						int idVal = var.GetIdVar();
						int nDVal = var.IsFlag(CDataDef.nReverseLogic)
							? objCommand.getDVal() ? 0 : 1
							: objCommand.getDVal() ? 1 : 0;
						SetWrp sw = objContext.addVariable(idVal, nDVal);
						sw.setCheckChangeValue(bCheck);
					}
				}
				bWait = true;
				//nWaitCounter = 0;
			}
		}
		objLastInterval = objInterval;
	}
	
	
	public synchronized void execute(CCommand objCommand, SetContext objContext)
	{
		if( objCommand.isFake() ) {
			// reset digital variable
			if( objCommand.getType().equals(CDataDef.strDigitalVal) || objCommand.getType().equals(CDataDef.strMixedVal) ) {
				if( isFlag(CDataDef.nAutoReset) ) {
					for(int v = 0; v < objVariables.size(); v++) {
						CVariable var = objVariables.get(v);
						if( var.IsDigital() && var.IsFlag(CDataDef.nAutoReset) ) {
							int idVar = var.GetIdVar();
							int nDVal = var.IsFlag(CDataDef.nReverseLogic)
								? objCommand.getDVal() ? 0 : 1
								: objCommand.getDVal() ? 1 : 0;
							SetWrp sw = objContext.addVariable(idVar, nDVal);
							sw.setCheckChangeValue(bCheck);
						}
					}
				}
			}
		}
		else {
			// set analog variable
			if( objCommand.getType().equals(CDataDef.strMixedVal) || objCommand.getType().equals(CDataDef.strAnalogVal) ) {
				for(int v = 0; v < objVariables.size(); v++) {
					CVariable var = objVariables.get(v);
					if( var.IsAnalog() ) {
						int idVar = var.GetIdVar();
						SetWrp sw = objContext.addVariable(idVar, objCommand.getAVal());
						sw.setCheckChangeValue(bCheck);
					}
				}
			}
			// set digital variable
			if( objCommand.getType().equals(CDataDef.strDigitalVal) || objCommand.getType().equals(CDataDef.strMixedVal) ) {
				for(int v = 0; v < objVariables.size(); v++) {
					CVariable var = objVariables.get(v);
					if( var.IsDigital() ) {
						int idVar = var.GetIdVar();
						int nDVal = var.IsFlag(CDataDef.nReverseLogic)
							? objCommand.getDVal() ? 0 : 1
							: objCommand.getDVal() ? 1 : 0;
						if( var.IsFlag(CDataDef.nAutoReset) ) {
							nDVal = (nDVal == 1 ? 0 : 1);
							objCommand.setHold();
						}
						SetWrp sw = objContext.addVariable(idVar, nDVal);
						sw.setCheckChangeValue(bCheck);
					}
				}
			}
		}
	}
	
	
	public synchronized void onError()
	{
		if( isFlag(CDataDef.nAutoReset) && bAutoReset )
			return;
		
		if( bWait ) {
			//nErrCounter = ERROR_CYCLES - nWaitCounter;
			nErrCounter = 1000000; // outside interval range
			bError = true;
		}
		//bWait = false;
	}
	
	
	public synchronized void onRunEnd(GregorianCalendar gc)
	{
		if( isFlag(CDataDef.nAutoReset) && bAutoReset )
			return;
		
		if( bWait ) {
			if( bError ) {
				// init error counter (all devices at the same time when operation ends)
				nErrCounter = ERROR_CYCLES;
			}
			else {
				objTimestamp = new Timestamp(gc.getTime().getTime());
				try {
					String sql = "update vs_groupdevs set \"timestamp\"=? where idgroup=? and iddevice=?";
					Object params[] = new Object[] { objTimestamp, idGroup, idDevice };
					DatabaseMgr.getInstance().executeStatement(null, sql, params);
				}
				catch(DataBaseException e) {
		        	LoggerMgr.getLogger(CDevice.class).error(e);
				}
			}
			bWait = false;
		}
	}
}
