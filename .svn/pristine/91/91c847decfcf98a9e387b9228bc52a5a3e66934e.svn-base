package com.carel.supervisor.plugin.optimum;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.*;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
	

// integrate NightFreeCooling algorithm with PVPRO db and device variables
public class NightFreeCoolingBean extends NightFreeCooling {
	// enabled
	private int idAlgorithm = 1;
	private String nameAlgorithm = "";
	private boolean bEnabled = false;
	
	// PVPRO variables assigned to Start/Stop algorithm
	private int idVarTemperatureSetpoint = 0;	// variable id for fTemperatureSetpoint input parameter
	private int idVarInternalTemperature = 0;	// variable id for fInternalTemperature input parameter
	private int idVarExternalTemperature = 0;	// variable id for fExternalTemperature input parameter
	private int idVarHumiditySetpoint = 0;		// variable id for fHumiditySetpoint input parameter
	private int idVarInternalHumidity = 0;		// variable id for fInternalHumidity input parameter
	private int idVarExternalHumidity = 0;		// variable id for fExternalHumidity input parameter
	private int idVarUnitOnOff = 0;				// variable id for nUnitOnOff input parameter
	private int idVarUnitOnOffCmd = 0;			// variable id for nUnitOnOffCmd output parameter
	private final int MAX_SLAVES = 3;			// max number of slaves
	private final int SLAVE_DELAY = 5000;		// use a small delay between turning AC ON/OFF to avoid power peeks
	private int nSlaveNo = 0;					// number of slaves
	private int anUnitOnOff[] = new int[MAX_SLAVES];		// array with slave states
	private int aidVarUnitOnOff[] = new int[MAX_SLAVES];	// array with variable id for nUnitOnOff for each slave
	private int aidVarUnitOnOffCmd[] = new int[MAX_SLAVES];	// array with variable id for nUnitOnOffCmd for each slave
	
	// schedule
	private boolean bSunriseSchedule = false;	// use sunrise time when is true
	
	private final int INVALID_TIME = -1;
	
	// user name from event list
	private static final String strUserName = "Smart Night Purge";

	
	// return an array with all algorithms from db
	public static NightFreeCoolingBean[] getNightFreeCoolingList()
	{
		NightFreeCoolingBean[] aAlg = null;
		try {
			String sql = "select idalg from opt_nightfreecooling_settings where name = 'AlgorithmId' order by idalg";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			aAlg = new NightFreeCoolingBean[rs.size()];
			for(int i = 0; i < rs.size(); i++)
				aAlg[i] = new NightFreeCoolingBean((Integer)rs.get(i).get(0));
		} catch(DataBaseException e) {
			LoggerMgr.getLogger("NightFreeCoolingBean").error(e);
		}
		return aAlg;
	}
	
	
	// new algorithm bean
	public NightFreeCoolingBean()
	{	
		// default values
		super(23.0f, 60, 0);
		fTemperatureDeadband	= 1.0f;
		nHumidityDeadband		= 10;
		nMaxTimeStart			= 120;
		fFanConsumption			= 10.0f;
		nFanCapacity			= 1000;
		nInternalHumidity		= 50;
		nExternalHumidity		= 50;
	}
	
	
	// load algorithm bean from db using id
	public NightFreeCoolingBean(int idAlgorithm)
	{	
		super(23.0f, 60, 0);
		this.idAlgorithm = idAlgorithm;
		loadParameters();
	}
	
	
	public boolean isEnabled()
	{
		return bEnabled;
	}
	
	
	public void setEnabled(boolean b)
	{
		try {
			String strValue = b ? "1" : "0";
			DatabaseMgr.getInstance().executeStatement("update opt_nightfreecooling_settings set value = ? where name = ?",
				new Object[] { strValue, "Enabled" });
			bEnabled = b;
		} catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}


	public int getAlgorithId()
	{
		return idAlgorithm;
	}
	
	
	public String getAlgorithmName()
	{
		return nameAlgorithm;
	}
	
	
	public int getTemperatureSetpointVar()
	{
		return this.idVarTemperatureSetpoint;
	}

	public int getInternalTemperatureVar()
	{
		return this.idVarInternalTemperature;
	}
		
	public int getExternalTemperatureVar()
	{
		return this.idVarExternalTemperature;
	}

	public int getHumiditySetpointVar()
	{
		return this.idVarHumiditySetpoint;
	}

	public int getInternalHumidityVar()
	{
		return this.idVarInternalHumidity;
	}
		
	public int getExternalHumidityVar()
	{
		return this.idVarExternalHumidity;
	}
	
	public int getUnitOnOffVar()
	{
		return this.idVarUnitOnOff;
	}	
	
	public int getUnitOnOffCmdVar()
	{
		return this.idVarUnitOnOffCmd;
	}
	
	public int getUnitOnOff(int i)
	{
		if( i > 0 )
			return anUnitOnOff[i-1];
		else
			return nUnitOnOff;
	}
	
	public int getUnitOnOffVar(int i)
	{
		if( i > 0 )
			return aidVarUnitOnOff[i-1];
		else
			return idVarUnitOnOff;
	}
	
	public int getUnitOnOffCmdVar(int i)
	{
		if( i > 0 )
			return aidVarUnitOnOffCmd[i-1];
		else
			return idVarUnitOnOffCmd;
	}

	public synchronized void setTimeOff(Date d)
	{
		if(d==null)
		{
			nTimeOff = INVALID_TIME;
			bExistsSunriseTime = false;
			return;
		}
			
		nTimeOff = d.getHours() * 60 + d.getMinutes();
		bExistsSunriseTime = true;
	}
	
	public int getTimeOffHour()
	{
		return (bExistsSunriseTime || !bSunriseSchedule)?(nTimeOff / 60):INVALID_TIME;
	}
	
	public int getTimeOffMinute()
	{
		return (bExistsSunriseTime || !bSunriseSchedule)?(nTimeOff % 60):INVALID_TIME;
	}
		
	public int getComputedTimeOnHour()
	{
		return getComputedTimeOn() / 60;
	}
	
	public int getComputedTimeOnMinute()
	{
		return getComputedTimeOn() % 60;
	}
	
	public boolean existAnticipatedStartTime()
	{
		return bExistStartTimeResult;
	}
	
	public boolean isStartCalcInProgress()
	{
		return bStartCalcInProgress;
	}

	public boolean isStartVentilation()
	{
		return bStartVentilation;
	}
	
	public boolean isSunriseSchedule()
	{
		return bSunriseSchedule;
	}
	
	public int getSlaveNo()
	{
		return nSlaveNo;
	}
	
	public boolean existsSunriseTime()
	{
		return bExistsSunriseTime;
	}
	
	public int getSummerStartMonth()
	{
		return nSummerStartMonth;
	}
	
	public int getSummerStartDay()
	{
		return nSummerStartDay;
	}
	
	public int getSummerEndMonth()
	{
		return nSummerEndMonth;
	}
	
	public int getSummerEndDay()
	{
		return nSummerEndDay;
	}
	
	public synchronized void execute()
	{
		// algorithm not started
		if( !bEnabled )
			return;
		
		NumberFormat df = DecimalFormat.getInstance();
		df.setMaximumFractionDigits(1);
		
		// update TemperatureSetpoint with PVPRO variable value
		if( idVarTemperatureSetpoint > 0 ) {
			float fValue;
			try {
				fValue = ControllerMgr.getInstance().getFromField(idVarTemperatureSetpoint).getCurrentValue();
				if( !Float.isNaN(fValue) )
					fTemperatureSetpoint = Float.parseFloat( df.format(fValue));
			}
			catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		// update InternalTemperature with PVPRO variable value
		if( idVarInternalTemperature > 0 ) {
			float fValue;
			try {
				fValue = ControllerMgr.getInstance().getFromField(idVarInternalTemperature).getCurrentValue();
				if( !Float.isNaN(fValue) )
					fInternalTemperature = Float.parseFloat( df.format(fValue));
			}
			catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		// update ExternalTemperature with PVPRO variable value
		if( idVarExternalTemperature > 0 ) {
			float fValue;
			try {
				fValue = ControllerMgr.getInstance().getFromField(idVarExternalTemperature).getCurrentValue();
				if( !Float.isNaN(fValue) )
					fExternalTemperature = Float.parseFloat( df.format(fValue));
			}
			catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		// update HumiditySetpoint with PVPRO variable value
		if( idVarHumiditySetpoint > 0 ) {
			int nValue;
			try {
				nValue = (int)ControllerMgr.getInstance().getFromField(idVarHumiditySetpoint).getCurrentValue();
				if( !Float.isNaN(nValue) )
					nHumiditySetpoint = nValue;
			}
			catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		// update InternalHumidity with PVPRO variable value
		if( idVarInternalHumidity > 0 ) {
			float fValue;
			try {
				fValue = ControllerMgr.getInstance().getFromField(idVarInternalHumidity).getCurrentValue();
				if( !Float.isNaN(fValue) )
					nInternalHumidity = (int)fValue;
			}
			catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		// update ExternalHumidity with PVPRO variable value
		if( idVarExternalHumidity  > 0 ) {
			float fValue;
			try {
				fValue = ControllerMgr.getInstance().getFromField(idVarExternalHumidity).getCurrentValue();
				if( !Float.isNaN(fValue) )
					nExternalHumidity = (int)fValue;
			}
			catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}

		// update UnitOnOff with PVPRO variable value
		readVentilationUnitStatus();
		
		// execute Start/Stop algorithm
		super.execute();
	}
	
	
	public synchronized void loadParameters()
	{
		try {
			// schedule
			// !bSunriseSchedule used to avoid loading of simple schedule on algorithm params
			// when sunrise schedule it is enabled
			if( !bSunriseSchedule ) // if the sunrise time is set by the user, then the correspoing flag is set to TRUE
			{
				nTimeOff = 0;
				bExistsSunriseTime = true;
			}
			else // otherwise the flag is set to FALSE by default (the right value is then set by the 'setTimeOff' method
				 // that is called at every thread 'cycle')
				bExistsSunriseTime = false;
				
			// feedback
			afEntalpyDifferenceStart.clear();
			anElapsedTimeStart.clear();
			// retrieve parameters
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from opt_nightfreecooling_settings where idalg=? order by name",
				new Object[] { idAlgorithm });
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				String strName = r.get("name").toString();
				String strValue = r.get("value").toString();
				if( strName.equals("AlgorithmId") )
					idAlgorithm = Integer.parseInt(strValue);
				else if( strName.equals("Name") )
					nameAlgorithm = strValue;
				else if( strName.equals("Enabled") )
					bEnabled = Integer.parseInt(strValue) != 0;
				else if( strName.equals("TemperatureSetpoint") )
					fTemperatureSetpoint = Float.parseFloat(strValue);
				else if( strName.equals("HumiditySetpoint") ) try {
					nHumiditySetpoint = Integer.parseInt(strValue);
				} catch(NumberFormatException e) {
					nHumiditySetpoint = (int)Float.parseFloat(strValue);
				}
				else if( strName.equals("TemperatureDeadband") )
					fTemperatureDeadband = Float.parseFloat(strValue);
				else if( strName.equals("HumidityDeadband") ) try {
					nHumidityDeadband = Integer.parseInt(strValue);
				} catch(NumberFormatException e) {
					nHumidityDeadband = (int)Float.parseFloat(strValue);
				}
				else if( strName.equals("MaxTimeStart") ) try {
					nMaxTimeStart = Integer.parseInt(strValue);
				} catch(NumberFormatException e) {
					nMaxTimeStart = (int)Float.parseFloat(strValue);
				}
				else if( strName.equals("InternalHumidity") ) try {
					nInternalHumidity = Integer.parseInt(strValue);
				} catch(NumberFormatException e) {
					nInternalHumidity = (int)Float.parseFloat(strValue);
				}
				else if( strName.equals("ExternalHumidity") ) try {
					nExternalHumidity = Integer.parseInt(strValue);
				} catch(NumberFormatException e) {
					nExternalHumidity = (int)Float.parseFloat(strValue);
				}
				else if( strName.equals("VarTemperatureSetpoint") )
					idVarTemperatureSetpoint = Integer.parseInt(strValue);
				else if( strName.equals("VarInternalTemperature") )
					idVarInternalTemperature = Integer.parseInt(strValue);
				else if( strName.equals("VarExternalTemperature") )
					idVarExternalTemperature = Integer.parseInt(strValue);
				else if( strName.equals("VarHumiditySetpoint") )
					idVarHumiditySetpoint = Integer.parseInt(strValue);
				else if( strName.equals("VarInternalHumidity") )
					idVarInternalHumidity = Integer.parseInt(strValue);
				else if( strName.equals("VarExternalHumidity") )
					idVarExternalHumidity = Integer.parseInt(strValue);
				else if( strName.equals("VarUnitOnOff") )
					idVarUnitOnOff = Integer.parseInt(strValue);
				else if( strName.equals("VarUnitOnOffCmd") )
					idVarUnitOnOffCmd = Integer.parseInt(strValue);
				else if( strName.equals("SlaveNo") )
					nSlaveNo = Integer.parseInt(strValue);
				else if( strName.equals("VarUnitOnOff1") )
					aidVarUnitOnOff[0] = Integer.parseInt(strValue);
				else if( strName.equals("VarUnitOnOffCmd1") )
					aidVarUnitOnOffCmd[0] = Integer.parseInt(strValue);
				else if( strName.equals("VarUnitOnOff2") )
					aidVarUnitOnOff[1] = Integer.parseInt(strValue);
				else if( strName.equals("VarUnitOnOffCmd2") )
					aidVarUnitOnOffCmd[1] = Integer.parseInt(strValue);
				else if( strName.equals("VarUnitOnOff3") )
					aidVarUnitOnOff[2] = Integer.parseInt(strValue);
				else if( strName.equals("VarUnitOnOffCmd3") )
					aidVarUnitOnOffCmd[2] = Integer.parseInt(strValue);
				else if( strName.equals("TimeOffHour") && !bSunriseSchedule )
					nTimeOff += Integer.parseInt(strValue) * 60;
				else if( strName.equals("TimeOffMinute") && !bSunriseSchedule )
					nTimeOff += Integer.parseInt(strValue);
				else if( strName.equals("SummerStartMonth") )
					nSummerStartMonth = Integer.parseInt(strValue);
				else if( strName.equals("SummerStartDay") )
					nSummerStartDay = Integer.parseInt(strValue);
				else if( strName.equals("SummerEndMonth") )
					nSummerEndMonth = Integer.parseInt(strValue);
				else if( strName.equals("SummerEndDay") )
					nSummerEndDay = Integer.parseInt(strValue);
				else if( strName.startsWith("ElapsedTimeStart") )
					anElapsedTimeStart.add(Integer.parseInt(strValue));
				else if( strName.startsWith("EnthalpyDifferenceStart") )
					afEntalpyDifferenceStart.add(Float.parseFloat(strValue));
				else if( strName.startsWith("FanConsumption") )
					fFanConsumption = Float.parseFloat(strValue);
				else if( strName.startsWith("FanCapacity") ) try {
					nFanCapacity = Integer.parseInt(strValue);
				} catch(NumberFormatException e) {
					nFanCapacity = (int)Float.parseFloat(strValue);
				}
				else if( strName.equals("SunriseSchedule") )
					bSunriseSchedule = strValue.equals("1");
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public synchronized void saveParameters(Properties prop)
	{
		idAlgorithm = Integer.parseInt(prop.getProperty("AlgorithmId"));
		if( idAlgorithm == 0 ) try {
			idAlgorithm = SeqMgr.getInstance().next(null, "opt_nfc_settings", "idalg");
			nameAlgorithm = prop.getProperty("Name");
			prop.setProperty("Enabled", "0");
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
			return;
		}
		
		Enumeration<Object> it = prop.keys();
		while( it.hasMoreElements() ) {
			String strName = (String)it.nextElement();
			String strValue = prop.getProperty(strName);
			if( strName.equals("AlgorithmId") )
				strValue = String.valueOf(idAlgorithm);
			try {
				// to avoid updating db with bad values
				Float.parseFloat(strValue);
				// to make sure it is considered on param loading
				if( strName.equals("SunriseSchedule") )
					bSunriseSchedule = strValue.equals("1");
			}
			catch(NumberFormatException e) {
				if( strName.equals("Name") )
					nameAlgorithm = strValue;
				else
					continue;
			}
			try {
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from opt_nightfreecooling_settings where idalg = ? and name = ?;",
					new Object[] { idAlgorithm, strName });
				if( rs.size() > 0 )
					DatabaseMgr.getInstance().executeStatement("update opt_nightfreecooling_settings set value = ? where idalg = ? and name = ?",
						new Object[] { strValue, idAlgorithm, strName });
				else
					DatabaseMgr.getInstance().executeStatement("insert into opt_nightfreecooling_settings values(?, ?, ?)",
						new Object[] { idAlgorithm, strName, strValue });
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		
		// update algorithm parameters
		loadParameters();
	}
	
	
	public void resetAllFlags()
	{
		nAnticipatedTimeStart = 0;
		bExistStartTimeResult = false;
		bStartCalcInProgress = false;
	}
	
	
	protected synchronized void saveFeedbackParameters(String strName, Vector aoParams)
	{
		try {
			DatabaseMgr.getInstance().executeStatement("delete from opt_nightfreecooling_settings where idalg = ? and name like '" + strName + "%';",
				new Object[] { idAlgorithm } );
			for(int i = 0; i < aoParams.size(); i++)
				DatabaseMgr.getInstance().executeStatement("insert into opt_nightfreecooling_settings values(?, ?, ?)",
					new Object[] { idAlgorithm, strName + i, aoParams.get(i).toString() });
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	protected synchronized void changeVentilationState(int nState)
	{
		if( nUnitOnOff == nUNIT_OFFLINE )
			return;
		
		// master
		if( idVarUnitOnOffCmd > 0 ) {
			SetContext objContext = new SetContext();
			String lang = "EN_en";
    		try
    		{
    			lang = LangUsedBeanList.getDefaultLanguage(1);
    		}
    		catch (Exception e)
    		{
    		}
    		objContext.setLanguagecode(lang);
			objContext.setUser(strUserName);
			objContext.setLoggable(true);
			objContext.setCallback(new BackGroundCallBack());
			SetWrp sw = objContext.addVariable(idVarUnitOnOffCmd, nState); // manage reverse logic on this place if it is required
			sw.setCheckChangeValue(true);
			SetDequeuerMgr.getInstance().add(objContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
		}

		if( nUnitOnOff == nUNIT_ON ) // only log values at the 'TURN-ON' action
			NightFreeCoolingLog.logParameters(
				idAlgorithm,
				nUnitOnOff,
				fInternalTemperature,
				fExternalTemperature,
				nInternalHumidity,
				nExternalHumidity,
				nAnticipatedTimeStart,
				fInternalEntalpy,
				fExternalEntalpy
			);
		
		// slaves
		for(int i = 0; i < nSlaveNo; i++) {
			if( aidVarUnitOnOffCmd[i] > 0 ) {
				try {
					Thread.sleep(SLAVE_DELAY);
				} catch(InterruptedException e) {
				}
				SetContext objContext = new SetContext();
				String lang = "EN_en";
	    		try
	    		{
	    			lang = LangUsedBeanList.getDefaultLanguage(1);
	    		}
	    		catch (Exception e)
	    		{
	    		}
	    		objContext.setLanguagecode(lang);
				objContext.setUser(strUserName);
				objContext.setLoggable(true);
				objContext.setCallback(new BackGroundCallBack());
				SetWrp swSlave = objContext.addVariable(aidVarUnitOnOffCmd[i], nState); // manage reverse logic on this place if it is required
				swSlave.setCheckChangeValue(true);
				SetDequeuerMgr.getInstance().add(objContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
			}
		}
		
	}
	
	private void readVentilationUnitStatus()
	{
		// master
		if( idVarUnitOnOff > 0 ) {
			float fValue;
			try {
				fValue = ControllerMgr.getInstance().getFromField(idVarUnitOnOff).getCurrentValue();
				if( !Float.isNaN(fValue) ) {
					nUnitOnOff = fValue != 0 ? nUNIT_ON : nUNIT_OFF;
				}
			}
			catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		// slaves
		for(int i = 0; i < nSlaveNo; i++) {
			if( aidVarUnitOnOff[i] > 0 ) {
				float fValue;
				try {
					fValue = ControllerMgr.getInstance().getFromField(aidVarUnitOnOff[i]).getCurrentValue();
					if( Float.isNaN(fValue) )
						anUnitOnOff[i] = nUNIT_OFFLINE;
					else
						anUnitOnOff[i] = fValue != 0 ? nUNIT_ON : nUNIT_OFF;
				}
				catch (Exception e) {
					anUnitOnOff[i] = nUNIT_OFFLINE;
				}
			}
		}
	}
	
	
	public boolean isConfigured()
	{
		return getUnitOnOffVar() != 0
			&& getUnitOnOffCmdVar() != 0
			&& getInternalTemperatureVar() != 0
			&& getExternalTemperatureVar() != 0;
	}
	

	public void delete()
	{
		try {
			String sql = "DELETE FROM opt_nightfreecooling_settings WHERE idalg = ?;";
			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { idAlgorithm });
			NightFreeCoolingLog.delete(idAlgorithm);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	public boolean isOutsideWorkingDates()
	{
		return super.isOutsideWorkingDates();
	}
}
