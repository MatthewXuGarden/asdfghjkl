package com.carel.supervisor.plugin.optimum;

import java.text.*;
import java.util.*;
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
import com.carel.supervisor.director.vscheduler.CData;
import com.carel.supervisor.director.vscheduler.CInterval;


// integrate StartStop algorithm with PVPRO db and device variables
public class StartStopBean extends StartStop {
	// enabled
	private int idAlgorithm = 1;
	private String nameAlgorithm = "";
	private boolean bEnabled = false;
	
	// PVPRO variables assigned to Start/Stop algorithm
	private int idVarTemperatureSetpoint = 0;	// variable id for fTemperatureSetpoint input parameter
	private int idVarInternalTemperature = 0;	// variable id for fInternalTemperature input parameter
	private int idVarExternalTemperature = 0;	// variable id for fExternalTemperature input parameter
	private int idVarUnitOnOff = 0;				// variable id for nUnitOnOff output parameter
	private int idVarUnitOnOffCmd = 0;			// variable id for nUnitOnOffCmd output parameter
	private int idVarSummerWinter = 0;			// variable id for nSummerWinter input parameter
	private boolean bSummerWinterRevLogic = false;
	private final int MAX_SLAVES = 3;			// max number of slaves
	private final int SLAVE_DELAY = 10000;		// use a small delay between turning AC ON/OFF to avoid power peeks
	private int nSlaveNo = 0;					// number of slaves
	private int anUnitOnOff[] = new int[MAX_SLAVES];		// array with slave states
	private int aidVarUnitOnOff[] = new int[MAX_SLAVES];	// array with variable id for nUnitOnOff for each slave
	private int aidVarUnitOnOffCmd[] = new int[MAX_SLAVES];	// array with variable id for nUnitOnOffCmd for each slave
	
	// schedule
	public static final String strAppId = "opt_startstop";	// VS app id used by optimum Start/Stop
	private static CData scheduler = null;		// VS data, single instance for all Start/Stop beans
	private boolean bAdvancedSchedule = true;	// use VS when is true
	private String strCategoryId = "";			// VS category id assigned to this bean
	private String strGroupId = "";				// VS group id assigned to this bean
	
    // counter (time balance)
	private int nRealTimeStart = 0;				// contains time difference between scheduled start time and AC start time  
	private int nRealTimeStop = 0;				// contains time difference between scheduled stop time and AC stop time 
	private int nCounterValue = 0;				// minutes counter
	private String strCounterDate = "";			// since
	
	// user name from event list
	private static final String strUserName = "HVAC Smart Start";
	
	
	// return an array with all algorithms from db
	public static StartStopBean[] getStartStopList()
	{
		StartStopBean[] aAlg = null;
		try {
			String sql = "select idalg from opt_startstop_settings where name = 'AlgorithmId' order by idalg";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			aAlg = new StartStopBean[rs.size()];
			for(int i = 0; i < rs.size(); i++)
				aAlg[i] = new StartStopBean((Integer)rs.get(i).get(0));
		} catch(DataBaseException e) {
			LoggerMgr.getLogger("StartStopBean").error(e);
		}
		return aAlg;
	}
	
	
	// new algorithm bean
	public StartStopBean()
	{	
		super(23.0f, 0.5f, 1.0f, 0, 0, 0, 120, 0, 120, AUTO);
		if( scheduler == null ) {
			scheduler = new CData();
			scheduler.loadDB("opt_startstop");
		}
		if( bAdvancedSchedule )
			updateTimeBand();
	}


	// load algorithm bean from db using id
	public StartStopBean(int idAlgorithm)
	{	
		super(23.0f, 0.5f, 1.0f, 0, 0, 0, 120, 0, 120, AUTO);
		this.idAlgorithm = idAlgorithm;
		loadParameters();
		if( scheduler == null ) {
			scheduler = new CData();
			scheduler.loadDB("opt_startstop");
		}
		if( bAdvancedSchedule )
			updateTimeBand();
	}
	
	
	public boolean isEnabled()
	{
		return bEnabled;
	}
	
	
	public void setEnabled(boolean b)
	{
		try {
			String strValue = b ? "1" : "0";
			DatabaseMgr.getInstance().executeStatement("update opt_startstop_settings set value = ? where idalg = ? and name = ?",
				new Object[] { strValue, idAlgorithm, "Enabled" });
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

	
	public int getSummerWinterVar()
	{
		return this.idVarSummerWinter;
	}
	
	
	public boolean isSummerWinterRevLogic()
	{
		return bSummerWinterRevLogic;
	}
	
	
	public int getInternalTemperatureVar()
	{
		return this.idVarInternalTemperature;
	}
	
	
	public int getExternalTemperatureVar()
	{
		return this.idVarExternalTemperature;
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

	
	public int getTimeOnHour()
	{
		return nTimeOn / 60;
	}
	

	public int getTimeOnMinute()
	{
		return nTimeOn % 60;
	}
	

	public int getTimeOffHour()
	{
		return nTimeOff / 60;
	}
	

	public int getTimeOffMinute()
	{
		return nTimeOff % 60;
	}
	
	
	public int getComputedTimeOnHour()
	{
		return getComputedTimeOn() / 60;
	}
	

	public int getComputedTimeOnMinute()
	{
		return getComputedTimeOn() % 60;
	}
	
	
	public int getComputedTimeOffHour()
	{
		return getComputedTimeOff() / 60;
	}
	

	public int getComputedTimeOffMinute()
	{
		return getComputedTimeOff() % 60;
	}
	
	
	public boolean isAdvancedSchedule()
	{
		return bAdvancedSchedule;
	}
	
	public boolean existAnticipatedStartTime()
	{
		return bExistStartTimeResult;
	}
	
	public boolean existAnticipatedStopTime()
	{
		return bExistStopTimeResult;
	}
	
	public boolean getStartCalcInProgress()
	{
		return bStartCalcInProgress;
	}
	
	public boolean getStopCalcInProgress()
	{
		return bStopCalcInProgress;
	}
	
	public boolean getStartActionDone()
	{
		return bStartActionDone;
	}
	
	public boolean getStopActionDone()
	{
		return bStopActionDone;
	}
	
	public int getSlaveNo()
	{
		return nSlaveNo;
	}
	
	public synchronized void execute()
	{
		// algorithm not started
		if( !bEnabled )
			return;

		// 1st execution cycle
		if( strCounterDate.isEmpty() )
			resetCounter();
		
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
		// update SummerWinter with PVPRO variable value
		if( idVarSummerWinter > 0 ) {
			float fValue;
			try {
				fValue = ControllerMgr.getInstance().getFromField(idVarSummerWinter).getCurrentValue();
				if( !Float.isNaN(fValue) )
					if( bSummerWinterRevLogic)
						nSummerWinter = fValue != 0 ? SUMMER : WINTER;
					else
						nSummerWinter = fValue != 0 ? WINTER : SUMMER;
			}
			catch (Exception e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		
		// update UnitOnOff with PVPRO variable value
		readACunitStatus();
		
		// execute Start/Stop algorithm
		super.execute();
	}
	
	
	public synchronized void loadParameters()
	{
		try {
			// schedule params
			// !bAdvancedSchedule used to avoid loading of simple schedule on algorithm params
			// when advanced schedule it is enabled
			if( !bAdvancedSchedule ) {
				nTimeOn = 0;
				nTimeOff = 0;
			}
			// feedback
			afTemperatureDifferenceStart.clear();
			afTemperatureDifferenceStop.clear();
			anElapsedTimeStart.clear();
			anElapsedTimeStop.clear();
			// retrieve parameters
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from opt_startstop_settings where idalg=? order by name",
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
				else if( strName.equals("SummerWinter") )
					nSummerWinter = Integer.parseInt(strValue);
				else if( strName.equals("SummerWinterRevLogic") )
					bSummerWinterRevLogic = "1".equals(strValue);
				else if( strName.equals("DiffStartSetpoint") )
					fDiffStartSetpoint = Float.parseFloat(strValue);
				else if( strName.equals("DiffStopSetpoint") )
					fDiffStopSetpoint = Float.parseFloat(strValue);
				else if( strName.equals("MinTimeStart") ) try {
					nMinTimeStart = Integer.parseInt(strValue);
				} catch(NumberFormatException e) {
					nMinTimeStart = (int)Float.parseFloat(strValue);
				}
				else if( strName.equals("MaxTimeStart") ) try {
					nMaxTimeStart = Integer.parseInt(strValue);
				} catch(NumberFormatException e) {
					nMaxTimeStart = (int)Float.parseFloat(strValue);
				}
				else if( strName.equals("MinTimeStop") ) try {
					nMinTimeStop = Integer.parseInt(strValue);
				} catch(NumberFormatException e) {
					nMinTimeStop = (int)Float.parseFloat(strValue);
				}
				else if( strName.equals("MaxTimeStop") ) try {
					nMaxTimeStop = Integer.parseInt(strValue);
				} catch(NumberFormatException e) {
					nMaxTimeStop = (int)Float.parseFloat(strValue);
				}
				else if( strName.equals("VarTemperatureSetpoint") )
					idVarTemperatureSetpoint = Integer.parseInt(strValue);
				else if( strName.equals("VarSummerWinter") )
					idVarSummerWinter = Integer.parseInt(strValue);
				else if( strName.equals("VarInternalTemperature") )
					idVarInternalTemperature = Integer.parseInt(strValue);
				else if( strName.equals("VarExternalTemperature") )
					idVarExternalTemperature = Integer.parseInt(strValue);
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
				else if( strName.equals("TimeOnHour") && !bAdvancedSchedule )
					nTimeOn += Integer.parseInt(strValue) * 60;
				else if( strName.equals("TimeOnMinute") && !bAdvancedSchedule )
					nTimeOn += Integer.parseInt(strValue);
				else if( strName.equals("TimeOffHour") && !bAdvancedSchedule )
					nTimeOff += Integer.parseInt(strValue) * 60;
				else if( strName.equals("TimeOffMinute") && !bAdvancedSchedule )
					nTimeOff += Integer.parseInt(strValue);
				else if( strName.startsWith("TemperatureDifferenceStart") )
					afTemperatureDifferenceStart.add(Float.parseFloat(strValue));
				else if( strName.startsWith("TemperatureDifferenceStop") )
					afTemperatureDifferenceStop.add(Float.parseFloat(strValue));
				else if( strName.startsWith("ElapsedTimeStart") )
					anElapsedTimeStart.add(Integer.parseInt(strValue));
				else if( strName.startsWith("ElapsedTimeStop") )
					anElapsedTimeStop.add(Integer.parseInt(strValue));
				else if( strName.equals("AdvancedSchedule") )
					bAdvancedSchedule = strValue.equals("1");
				else if( strName.equals("CategoryId") )
					strCategoryId = strValue;
				else if( strName.equals("GroupId") )
					strGroupId = strValue;
				else if( strName.equals("CounterValue") )
					nCounterValue = Integer.parseInt(strValue);
				else if( strName.equals("CounterDate") )
					strCounterDate = strValue;
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
			idAlgorithm = SeqMgr.getInstance().next(null, "opt_startstop_set", "idalg");
			nameAlgorithm = prop.getProperty("Name");
			// create VS category/group for new algorithm
			int idCat = SeqMgr.getInstance().next(null, "vs_category_optimum", "idcategory");
			strCategoryId = String.valueOf(idCat);
			String nameCat = nameAlgorithm.length() > 32 ? nameAlgorithm.substring(0, 31) : nameAlgorithm;
			String sql = "INSERT INTO vs_category values(?, ?, 'empty.png', 'opt_startstop')";
			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { idCat, nameCat });
			sql = "INSERT INTO vs_group values(DEFAULT, ' ', ?, 1, 'd');";
			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { idCat });
			strGroupId = DatabaseMgr.getInstance().executeQuery(null, "select max(idgroup) from vs_group;").get(0).get(0).toString();
			prop.setProperty("Enabled", "0");
			prop.setProperty("CategoryId", strCategoryId);
			prop.setProperty("GroupId", strGroupId);
			prop.setProperty("CounterValue", String.valueOf(nCounterValue));
			prop.setProperty("CounterDate", strCounterDate);			
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
			}
			catch(NumberFormatException e) {
				if( strName.equals("Name") )
					nameAlgorithm = strValue;
				else if( !strName.equals("CounterDate") )
					continue;
			}
			try {
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from opt_startstop_settings where idalg = ? and name = ?;",
					new Object[] { idAlgorithm, strName });
				if( rs.size() > 0 )
					DatabaseMgr.getInstance().executeStatement("update opt_startstop_settings set value = ? where idalg = ? and name = ?",
						new Object[] { strValue, idAlgorithm, strName });
				else
					DatabaseMgr.getInstance().executeStatement("insert into opt_startstop_settings values(?, ?, ?)",
						new Object[] { idAlgorithm, strName, strValue });
			}
			catch(DataBaseException e) {
				LoggerMgr.getLogger(this.getClass()).error(e);
			}
		}
		
		// save VS category name
		try {
			String nameCat = nameAlgorithm.length() > 32 ? nameAlgorithm.substring(0, 31) : nameAlgorithm;
			String sql = "UPDATE vs_category SET name = ? where idcategory = ?;";
			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { nameCat, Integer.parseInt(strCategoryId) });
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
		
		// update algorithm parameters
		loadParameters();
	}
	
	
	protected synchronized void saveFeedbackParameters(String strName, Vector aoParams)
	{
		try {
			DatabaseMgr.getInstance().executeStatement("delete from opt_startstop_settings where idalg = ? and name like '" + strName + "%';",
				new Object[] { idAlgorithm } );
			for(int i = 0; i < aoParams.size(); i++)
				DatabaseMgr.getInstance().executeStatement("insert into opt_startstop_settings values(?, ?, ?)",
					new Object[] { idAlgorithm, strName + i, aoParams.get(i).toString() });
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	protected synchronized void changeACstate(int nState)
	{
		if( nUnitOnOff == nUNIT_OFFLINE )
			return;

		// compute real pre-start/pre-stop values
		if( nState == nUNIT_ON )
			nRealTimeStart = nTimeOn - nCurrentTime;
		if( nState == nUNIT_OFF ) {
			nRealTimeStop = nTimeOff - nCurrentTime;
			updateCounter();
		}
		
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
		
		StartStopLog.logParameters(
			idAlgorithm,
			nState,
			fInternalTemperature,
			fExternalTemperature,
			nState == nUNIT_ON && nAnticipatedTimeStart != nRealTimeStart ? nRealTimeStart : nAnticipatedTimeStart,
			nState == nUNIT_OFF && nAnticipatedTimeStop != nRealTimeStop ? nRealTimeStop : nAnticipatedTimeStop
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

		// switch to the next VS interval
		if( bAdvancedSchedule && nState == nUNIT_OFF )
		{
			if(getNextTimeBand())
				resetAnticipatedTimes();
		}
	}
	
	
	public boolean getNextTimeBand()
	{
		GregorianCalendar gCal = new GregorianCalendar();
		gCal.set(GregorianCalendar.HOUR_OF_DAY, (int)(nTimeOff / 60));
		gCal.set(GregorianCalendar.MINUTE, (int)(nTimeOff % 60));
		
		CInterval objInterval = scheduler.getNextInterval(strGroupId, gCal);
		
		if( objInterval != null ) {
			/*
			if( idVarTemperatureSetpoint <= 0 ) 
				fTemperatureSetpoint = objInterval.getCommand().getAVal();
			*/
			if(nTimeOn != objInterval.getStartNumber() || nTimeOff != objInterval.getStopNumber())
			{
				nTimeOn = objInterval.getStartNumber();
				nTimeOff = objInterval.getStopNumber();
				return true;
			}
		}
		return false;
	}
	
	
	public void updateTimeBand()
	{
		CInterval objInterval = scheduler.getNextInterval(strGroupId, new GregorianCalendar());
		if( objInterval != null ) {
			/*
			if( idVarTemperatureSetpoint <= 0 ) 
				fTemperatureSetpoint = objInterval.getCommand().getAVal();
			*/

			nTimeOn = objInterval.getStartNumber();
			nTimeOff = objInterval.getStopNumber();
		}
	}
	
	
	public void resetAllFlags()
	{
		nAnticipatedTimeStart = 0;
		nAnticipatedTimeStop = 0;
		bExistStartTimeResult = false;
		bExistStopTimeResult = false;
		bStartCalcInProgress = false;
		bStopCalcInProgress = false;
		bStartActionDone = false;
		bStopActionDone = false;
		bSaveStart = false;
		bSaveStop = false;
	}
	

	public void resetAnticipatedTimes()
	{
		nAnticipatedTimeStart = 0;
		nAnticipatedTimeStop = 0;
	}
	
	public void resetTimeBand()
	{
		CInterval objInterval = scheduler.getNextInterval(strGroupId, new GregorianCalendar());
		if( objInterval == null )
		{
			nTimeOn = 0;
			nTimeOff = 0;
		}
	}
	
	
	private void readACunitStatus()
	{
		// master
		if( idVarUnitOnOff > 0 ) {
			float fValue;
			try {
				fValue = ControllerMgr.getInstance().getFromField(idVarUnitOnOff).getCurrentValue();
				if( Float.isNaN(fValue) )
					nUnitOnOff = nUNIT_OFFLINE;
				else
					nUnitOnOff = fValue != 0 ? nUNIT_ON : nUNIT_OFF;
			}
			catch (Exception e) {
				nUnitOnOff = nUNIT_OFFLINE;
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
			String sql = "DELETE FROM opt_startstop_settings WHERE idalg = ?;";
			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { idAlgorithm });
			sql = "DELETE FROM vs_category WHERE idcategory = ?;";
			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { Integer.parseInt(strCategoryId) });
			StartStopLog.delete(idAlgorithm);
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public static synchronized void schedulerChanged()
	{
		if( scheduler != null )
			scheduler.loadDB("opt_startstop");
	}
	
	
	public String getCategoryId()
	{
		return strCategoryId;
	}

	
	public String getGroupId()
	{
		return strGroupId;
	}
	
	
	public void resetCounter()
	{
		nCounterValue = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		strCounterDate = sdf.format(new java.util.Date());
		try {
			String sql = "update opt_startstop_settings set value = ? where idalg = ? and name = ?";
			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { String.valueOf(nCounterValue), idAlgorithm, "CounterValue" });
			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { strCounterDate, idAlgorithm, "CounterDate" });			
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	private void updateCounter()
	{
		nCounterValue += (nRealTimeStop - nRealTimeStart);
		try {
			String sql = "update opt_startstop_settings set value = ? where idalg = ? and name = ?";
			DatabaseMgr.getInstance().executeStatement(sql, new Object[] { String.valueOf(nCounterValue), idAlgorithm, "CounterValue" });
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(this.getClass()).error(e);
		}
	}
	
	
	public int getCounterValue()
	{
		return nCounterValue;
	}
	
	
	public String getCounterDate()
	{
		return strCounterDate;
	}
}
