package com.carel.supervisor.plugin.algorithmpro.obj;

import java.io.File;
import java.sql.Timestamp;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.plugin.algorithmpro.alias.AliasMgr;
import com.carel.supervisor.plugin.algorithmpro.field.FieldMgr;
import com.carel.supervisor.plugin.algorithmpro.log.AlgoProLogger;


public abstract class AlgorithmPro implements IAlgorithmPro
{
	private AlgoProLogger logger = null;
	private AlgoSleep sleep = AlgoSleep.MINUTE_1;
	private boolean isWorking = true;
	private String name = "";
	
	/*
	 * Variable for keep informations about the state of the object.
	 * They will be displayed to the user throw the GUI.
	 */
	private Timestamp startInfo = null;
	private Timestamp stopInfo = null;
	
	private String stateInfo1 = "";
	private String stateInfo2 = "";
	private String stateInfo3 = "";
	private String stateInfo4 = "";
	private String stateInfo5 = "";
	
	public AlgorithmPro()
	{
		this.name = "UNKNOWNAME";
		this.isWorking = true;
	}
	
	public AlgorithmPro(String name) 
	{
		this.name = name;
		this.isWorking = true;
	}
	
	public void writeLog(Object msg,int type) {
		if(this.logger != null)
			this.logger.writeLog(msg, type);
	}
	
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
		this.logger = new AlgoProLogger(System.getenv("PVPRO_HOME") + File.separator + 
				AliasMgr.ALGO_FOLDER + File.separator + this.name + ".log");
	}
	
	public final void setWork(boolean work) {
		this.isWorking = work;
	}
	
	public final boolean getWork() {
		return this.isWorking;
	}
	
	public final void setSleep(AlgoSleep aSleep) {
		this.sleep = aSleep;
	}
	
	public final long getSleep() {
		return sleep.getTime();
	}

	public final AlgoSleep getAlgoSleep() {
		return this.sleep;
	}
	
	public void setValue(String deviceAlias,String variableAlias,float value)
	{
		try {
			FieldMgr.setOnField(deviceAlias, variableAlias, value, this.name);
		} 
		catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	public float getValue(String deviceAlias,String variableAlias)
	{
		Float value = Float.NaN;
		try 
		{
			deviceAlias = deviceAlias.toUpperCase();
			value = FieldMgr.getFromField(deviceAlias, variableAlias);
		} 
		catch (Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
			value = Float.NaN;
		}
		return value.floatValue();
	}
	
	/*
	 * Default
	 */
	public boolean shutdown() {
		return true;
	}

	/*
	 * Default
	 */
	public boolean startup() {
		return true;
	}

	/**
	 * Keep trace about the start time of the object
	 */
	public final void markStartTime() {
		this.startInfo = new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * Keep trace about the start time of the object
	 */
	public final void markStopTime() {
		this.stopInfo = new Timestamp(System.currentTimeMillis());
	}

	public final Timestamp getStartTime() {
		return this.startInfo;
	}

	public final Timestamp getStopTime() {
		return this.stopInfo;
	}

	public String getStateInfo1() {
		return this.stateInfo1;
	}

	public String getStateInfo2() {
		return this.stateInfo2;
	}

	public String getStateInfo3() {
		return this.stateInfo3;		
	}
	
	public String getStateInfo4() {
		return this.stateInfo4;		
	}
	
	public String getStateInfo5() {
		return this.stateInfo5;		
	}

	public void setStateInfo1(String value) {
		this.stateInfo1 = getNowFormatted() + value;
	}

	public void setStateInfo2(String value) {
		this.stateInfo2 = getNowFormatted() +value;
	}

	public void setStateInfo3(String value) {
		this.stateInfo3 = getNowFormatted() +value;
	}
	
	public void setStateInfo4(String value) {
		this.stateInfo4 = getNowFormatted() +value;
	}
	
	public void setStateInfo5(String value) {
		this.stateInfo5 = getNowFormatted() +value;
	}
	
	private String getNowFormatted()
	{
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String sNow = now.toString();
		sNow = "[" + sNow + "] ";
		return sNow;
	}
}
