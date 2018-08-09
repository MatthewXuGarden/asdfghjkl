package com.carel.supervisor.director.vscheduler;

import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.base.xml.*;


// interval data object
public class CInterval {
	private CSchedule objSchedule; // parent
	private int nStartHour;
	private int nStartMinute;
	private int nStopHour;
	private int nStopMinute;
	private CCommand objCommand;
	private boolean bFake;

	
	public CInterval()
	{
		objSchedule = null;
		objCommand = new CCommand();
		bFake = false;
	}
	
	
	public CInterval(CSchedule objSchedule)
	{
		this.objSchedule = objSchedule;
		objCommand = new CCommand(objSchedule.getGroup().getType());
		bFake = false;
	}

	
	// set parent schedule
	public void setSchedule(CSchedule objSchedule)
	{
		this.objSchedule = objSchedule;
		objCommand.setType(objSchedule.getGroup().getType());
	}
	
	
	public CSchedule getSchedule()
	{
		return objSchedule;
	}
	
	
	public CCommand getCommand()
	{
		return objCommand;
	}
	
	
	public boolean isStartTime(int nHour, int nMinute)
	{
		int nCurrentNumber = nHour * 60 + nMinute;
		return nCurrentNumber >= getStartNumber() && nCurrentNumber < getStopNumber();
	}
	

	public boolean isStopTime(int nHour, int nMinute)
	{
		int nCurrentNumber = nHour * 60 + nMinute;
		return nCurrentNumber >= getStopNumber() && nCurrentNumber < objSchedule.getStop(getStopNumber());
	}
	
	
	public void loadDB(Record r)
	{
		setStartNumber(Integer.parseInt(r.get("start").toString()));
		if( objCommand.getType().equals(CDataDef.strDigitalVal) || objCommand.getType().equals(CDataDef.strMixedVal) ) {
			setStopNumber(Integer.parseInt(r.get("stop").toString()));
			objCommand.setDVal(r.get("dval").toString().equalsIgnoreCase("true"));
		}
		if( objCommand.getType().equals(CDataDef.strMixedVal) || objCommand.getType().equals(CDataDef.strAnalogVal) ) {
			if( objCommand.getType().equals(CDataDef.strAnalogVal) ) {
				nStopHour = nStartHour;
				nStopMinute = nStartMinute;
			}
			objCommand.setAVal(Float.parseFloat(r.get("aval").toString()));
		}
	}
	
	
	public void loadXML(XMLNode xmlInterval)
	{
		setStartString(xmlInterval.getAttribute(CDataDef.strStartAttr));
		if( objCommand.getType().equals(CDataDef.strDigitalVal) || objCommand.getType().equals(CDataDef.strMixedVal) ) {
			setStopString(xmlInterval.getAttribute(CDataDef.strStopAttr));
			//nDVal = Integer.parseInt(xmlInterval.getAttribute(CDataDef.strDigitalValAttr));
			objCommand.setDVal(true); // always active
		}
		if( objCommand.getType().equals(CDataDef.strMixedVal) || objCommand.getType().equals(CDataDef.strAnalogVal) ) {
			if( objCommand.getType().equals(CDataDef.strAnalogVal) ) {
				nStopHour = nStartHour;
				nStopMinute = nStartMinute;
			}
			objCommand.setAVal(Float.parseFloat(xmlInterval.getAttribute(CDataDef.strAnalogValAttr)));
		}
	}
	
	
	public XMLNode getXML()
	{
		XMLNode xmlInterval = new XMLNode(CDataDef.strIntervalNode, "");
		xmlInterval.setAttribute(CDataDef.strStartAttr, nStartHour + ":" + nStartMinute);
		if( objCommand.getType().equals(CDataDef.strDigitalVal) || objCommand.getType().equals(CDataDef.strMixedVal) ) {
			xmlInterval.setAttribute(CDataDef.strStopAttr, nStopHour + ":" + nStopMinute);
			xmlInterval.setAttribute(CDataDef.strDigitalValAttr, objCommand.getDVal() ? "1" : "0");
		}
		if( objCommand.getType().equals(CDataDef.strMixedVal) || objCommand.getType().equals(CDataDef.strAnalogVal) ) {
			xmlInterval.setAttribute(CDataDef.strAnalogValAttr, (new Float(objCommand.getAVal())).toString());
		}
		return xmlInterval;
	}	
	
	
	public void setStartNumber(int n)
	{
		if( n >= 0 ) {
			nStartHour = n / 60;
			nStartMinute = n % 60;
		}
		else {
			nStartHour = 0;
			nStartMinute = n;
		}
	}

	
	public int getStartNumber()
	{
		return nStartHour * 60 + nStartMinute;
	}

	
	private void setStartString(String str)
	{
		String astr[] = str.split(":");
		if( astr.length == 2 ) {
			nStartHour = Integer.parseInt(astr[0]);
			nStartMinute = Integer.parseInt(astr[1]);
		}
	}
	
	
	public int getStartHour()
	{
		return nStartHour;
	}
	
	
	public int getStartMinute()
	{
		return nStartMinute;
	}
	
	
	public void setStopNumber(int n)
	{
		if( n >= 0 ) {
			nStopHour = n / 60;
			nStopMinute = n % 60;
		}
		else {
			nStartHour = 0;
			nStartMinute = n;
		}
	}
	

	public int getStopNumber()
	{
		return nStopHour * 60 + nStopMinute;
	}
	
	
	private void setStopString(String str)
	{
		String astr[] = str.split(":");
		if( astr.length == 2 ) {
			nStopHour = Integer.parseInt(astr[0]);
			nStopMinute = Integer.parseInt(astr[1]);
		}
	}
	

	public int getStopHour()
	{
		return nStopHour;
	}
	
	
	public int getStopMinute()
	{
		return nStopMinute;
	}
	
	
	public void setFake()
	{
		bFake = true;
	}
	
	
	public boolean isFake()
	{
		return bFake;
	}
}
