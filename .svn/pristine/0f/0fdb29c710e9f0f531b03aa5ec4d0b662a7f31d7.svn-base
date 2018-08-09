package com.carel.supervisor.director.vscheduler;

import java.util.Enumeration;
import java.util.Vector;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;

import java.util.GregorianCalendar;

// schedule data object
// schedule is a collection of intervals
public class CSchedule {
	private CGroup objGroup; // parent
	private String strType;
	private int nYear;
	private int nMonth;
	private int nDay;
	private Vector<CInterval> objIntervals;
	private String strFlag;
	// exception related data
	public static final int ENABLED = 0x01;
	public static final int RUN_ONCE = 0x02;
	private boolean bRunOnce;
	private boolean bEnabled;
	
	private int idGroup;

	
	public CSchedule()
	{
		objGroup = null;
		objIntervals = new Vector<CInterval>();
		idGroup = 0;
	}
	

	public CSchedule(CGroup objGroup)
	{
		this.objGroup = objGroup;
		objIntervals = new Vector<CInterval>();
		idGroup = Integer.parseInt(objGroup.getId());		
	}
	
	
	// set parent group
	public void setGroup(CGroup objGroup)
	{
		this.objGroup = objGroup;
		idGroup = Integer.parseInt(objGroup.getId());
	}
	
	
	public CGroup getGroup()
	{
		return objGroup;
	}
	
	
	public CInterval getInterval(GregorianCalendar gc)
	{
		for(Enumeration<CInterval> e = objIntervals.elements(); e.hasMoreElements(); ) {
			CInterval objInterval = e.nextElement();
			if( objInterval.isStartTime(gc.get(GregorianCalendar.HOUR_OF_DAY), gc.get(GregorianCalendar.MINUTE)) )
				return objInterval;
			else if( !objGroup.getType().equals(CDataDef.strAnalogVal) &&
				objInterval.isStopTime(gc.get(GregorianCalendar.HOUR_OF_DAY), gc.get(GregorianCalendar.MINUTE))	) {
				CInterval objFakeInterval = new CInterval(objInterval.getSchedule());
				objFakeInterval.setFake();
				objFakeInterval.setStartNumber(objInterval.getStopNumber());
				objFakeInterval.setStopNumber(getStop(objInterval.getStopNumber()));
				CCommand objCommand = objFakeInterval.getCommand();
				objCommand.setDVal(!objInterval.getCommand().getDVal());
				objCommand.setAVal(objInterval.getCommand().getAVal());
				
				// filter midnight stop command if there is an interval starting at 00:00 on the next day
				if( objFakeInterval.getStartHour() == 23 && objFakeInterval.getStartMinute() == 59 ) {
					GregorianCalendar gcNextDay = (GregorianCalendar)gc.clone();
					gcNextDay.add(GregorianCalendar.MINUTE, 1);
					CSchedule objSchedule = objGroup.getSchedule(gcNextDay);
					if( !objSchedule.objIntervals.isEmpty() && objSchedule.objIntervals.firstElement().getStartNumber() == 0 )
						return null;
				}
					
				return objFakeInterval;
			}
		}
		return null;
	}
	
	
	public void loadDB(Record r)
	{
		if( strType.equals(CDataDef.strExceptionsVal) ) {
			setExceptionFlags(Integer.parseInt(r.get("flags").toString()));
			setYear(Integer.parseInt(r.get("idyear").toString()));
		}
	}
	
	
	public void updateDB()
	{
		if( strFlag.equals(CDataDef.strCleanVal) )
			return;
		removeIntervalsFromDB();
		addIntervalsToDB();
	}
	
	
	private void addIntervalsToDB()
	{
		try {
			String sql = null;
			Object params[] = null;
			if( strType.equals(CDataDef.strStandardVal) ) {
				for(Enumeration<CInterval> e = objIntervals.elements(); e.hasMoreElements();) {
					CInterval objInterval = e.nextElement();
					sql = "insert into vs_schedule values (?, ?, ?, ?, ?, ?)";
					params = new Object[] {	idGroup, nDay,
						objInterval.getStartNumber(),
						objInterval.getStopNumber(),
						objInterval.getCommand().getDVal(),
						objInterval.getCommand().getAVal()
					};
					DatabaseMgr.getInstance().executeStatement(sql, params);
				}
			}
			else if( strType.equals(CDataDef.strExceptionsVal) ) {
				// empty exception
				if( objIntervals.isEmpty() && !strFlag.equals(CDataDef.strRemovedVal) ) {
					sql = "insert into vs_exception values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
					params = new Object[] {	idGroup, nMonth, nDay, -1, -1, false, 0, getExceptionFlags(), nYear };
					DatabaseMgr.getInstance().executeStatement(sql, params);
				}
				
				for(Enumeration<CInterval> e = objIntervals.elements(); e.hasMoreElements();) {
					CInterval objInterval = e.nextElement();
					sql = "insert into vs_exception values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
					params = new Object[] {	idGroup, nMonth, nDay,
						objInterval.getStartNumber(),
						objInterval.getStopNumber(),
						objInterval.getCommand().getDVal(),
						objInterval.getCommand().getAVal(),
						getExceptionFlags(),
						nYear
					};
					DatabaseMgr.getInstance().executeStatement(sql, params);
				}
			}
			else;
		}
		catch(DataBaseException e) {
	    	LoggerMgr.getLogger(CSchedule.class).error(e);	
		}		
	}
	
	
	public void removeIntervalsFromDB()
	{
		try {
			String sql = null;
			Object params[] = null;
			if( strType.equals(CDataDef.strStandardVal) ) {
				sql = "delete from vs_schedule where idgroup=? and idday=?";
				params = new Object[] {	idGroup, nDay };
				DatabaseMgr.getInstance().executeStatement(sql, params);
			}
			else if( strType.equals(CDataDef.strExceptionsVal) ) {
				if( !bRunOnce ) {
					sql = "delete from vs_exception where idgroup=? and idmonth=? and idday=?";
					params = new Object[] {	idGroup, nMonth, nDay };
				}
				else {
					sql = "delete from vs_exception where idgroup=? and idyear=? and idmonth=? and idday=?";
					params = new Object[] {	idGroup, nYear, nMonth, nDay };
				}
				DatabaseMgr.getInstance().executeStatement(sql, params);
			}
			else;
		}
		catch(DataBaseException e) {
        	LoggerMgr.getLogger(CSchedule.class).error(e);	
		}		
	}
	
	
	public void updateFlags()
	{
		/* there is no need to disable RunOnce exception; year do this job for free
		if( bEnabled && bRunOnce ) {
			bEnabled = false;
			try {
				String sql = "update vs_exception set flags=? where idgroup=? and idyear=? and idmonth=? and idday=?";
				Object params[] = new Object[] { getExceptionFlags(), idGroup, nYear, nMonth, nDay };
				DatabaseMgr.getInstance().executeStatement(null, sql, params);
			}
			catch(DataBaseException e) {
	        	LoggerMgr.getLogger(CSchedule.class).error(e);	
			}
		}
		*/	
	}
	
	
	public void loadXML(XMLNode xmlSchedule)
	{
		// clear intervals
		clearIntervals();
		
		strType = xmlSchedule.getAttribute(CDataDef.strTypeAttr);
		if( strType.equals(CDataDef.strExceptionsVal) ) {
			nYear = Integer.parseInt(xmlSchedule.getAttribute(CDataDef.strYearAttr));
			nMonth = Integer.parseInt(xmlSchedule.getAttribute(CDataDef.strMonthAttr));
			bRunOnce = xmlSchedule.getAttribute(CDataDef.strRunAttr).equals(CDataDef.strRunOnceVal);
			bEnabled = xmlSchedule.getAttribute(CDataDef.strEnabledAttr).equals(CDataDef.strEnabledVal);
		}
		nDay = Integer.parseInt(xmlSchedule.getAttribute(CDataDef.strDayAttr));
		strFlag = xmlSchedule.getAttribute(CDataDef.strFlagAttr);
		XMLNode axmlNode[] = xmlSchedule.getNodes();
		if( axmlNode != null )
		for(int i = 0; i < axmlNode.length; i++) {
			if( axmlNode[i].getNodeName().equals(CDataDef.strIntervalNode) ) {
				CInterval objInterval = new CInterval(this);
				objInterval.loadXML(axmlNode[i]);
				addInterval(objInterval);
			}
		}				
	}
	
	
	public XMLNode getXML()
	{
		XMLNode xmlSchedule = new XMLNode(CDataDef.strScheduleNode, "");
		xmlSchedule.setAttribute(CDataDef.strTypeAttr, strType);
		if( strType.equals(CDataDef.strExceptionsVal) ) {
			xmlSchedule.setAttribute(CDataDef.strYearAttr, "" + nYear);
			xmlSchedule.setAttribute(CDataDef.strMonthAttr, "" + nMonth);
			xmlSchedule.setAttribute(CDataDef.strRunAttr, bRunOnce ? CDataDef.strRunOnceVal : CDataDef.strRepetitiveVal);
			xmlSchedule.setAttribute(CDataDef.strEnabledAttr, bEnabled ? CDataDef.strEnabledVal : CDataDef.strDisabledVal);
		}
		xmlSchedule.setAttribute(CDataDef.strDayAttr, "" + nDay);
		for(Enumeration<CInterval> e = objIntervals.elements(); e.hasMoreElements(); )
			xmlSchedule.addNode(e.nextElement().getXML());
		return xmlSchedule;
	}
	
	
	public void addInterval(CInterval objInterval)
	{
		objInterval.setSchedule(this);
		objIntervals.add(objInterval);
	}
	
	
	public void clearIntervals()
	{
		if( !objIntervals.isEmpty() )
			objIntervals.clear();
	}
	
	
	public void setYear(int n)
	{
		nYear = n;
	}
	
	
	public int getYear()
	{
		return nYear;
	}
	
	
	public void setMonth(int n)
	{
		nMonth = n;
	}
	
	
	public int getMonth()
	{
		return nMonth;
	}

	
	public void setDay(int n)
	{
		nDay = n;
	}
	
	
	public int getDay()
	{
		return nDay;
	}
	
	
	public void setType(String strType)
	{
		this.strType = strType;
	}
	
	
	public String getType()
	{
		return strType;
	}
	
	
	public void setExceptionFlags(int nFlags)
	{
		bEnabled = (nFlags & ENABLED) == ENABLED;
		bRunOnce = (nFlags & RUN_ONCE) == RUN_ONCE;
	}
	
	
	public int getExceptionFlags()
	{
		int nFlags = 0;
		if( bEnabled ) nFlags |= ENABLED;
		if( bRunOnce ) nFlags |= RUN_ONCE;
		return nFlags;
	}
	
	
	public boolean isEnabled()
	{
		return bEnabled;
	}
	
	
	public boolean isRunOnce()
	{
		return bRunOnce;
	}
	
	
	public boolean isRemovedSchedule()
	{
		return strFlag.equals(CDataDef.strRemovedVal);
	}
	
	
	public void setStops()
	{
		CInterval objPrevInterval = null;
		for(Enumeration<CInterval> e = objIntervals.elements(); e.hasMoreElements(); ) {
			CInterval objInterval = e.nextElement();
			if( objPrevInterval != null )
				objPrevInterval.setStopNumber(objInterval.getStartNumber());
			objPrevInterval = objInterval;
		}
		if( objPrevInterval != null )
			objPrevInterval.setStopNumber(24*60);
	}
	
	
	public int getStop(int nStart)
	{
		for(Enumeration<CInterval> e = objIntervals.elements(); e.hasMoreElements(); ) {
			CInterval objInterval = e.nextElement();
			if( objInterval.getStartNumber() >= nStart )
				return objInterval.getStartNumber();
		}
		return 24*60;
	}
	
	
	// app query
	public CInterval getNextInterval(GregorianCalendar gc)
	{
		int nTime = gc.get(GregorianCalendar.HOUR_OF_DAY) * 60 + gc.get(GregorianCalendar.MINUTE);
		
		for(Enumeration<CInterval> e = objIntervals.elements(); e.hasMoreElements(); ) {
			CInterval objInterval = e.nextElement();
			if( nTime < objInterval.getStopNumber() )
				return objInterval;
		}
		
		return null;
	}
	
	
	// debug
	public String toString()
	{
		StringBuffer strbuff = new StringBuffer();
		strbuff.append("schedule: " + nYear + ", " + nMonth + ", " + nDay + "\n"); 
		for(Enumeration<CInterval> e = objIntervals.elements(); e.hasMoreElements();) {
			CInterval objInterval = e.nextElement();
			strbuff.append("interval: ");
			strbuff.append(objInterval.getStartNumber());
			strbuff.append(", ");
			strbuff.append(objInterval.getStopNumber());
			strbuff.append(", ");
			strbuff.append(objInterval.getCommand().getDVal());
			strbuff.append(", ");
			strbuff.append(objInterval.getCommand().getAVal());
			strbuff.append("\n");
		}
		return strbuff.toString();
	}
}
