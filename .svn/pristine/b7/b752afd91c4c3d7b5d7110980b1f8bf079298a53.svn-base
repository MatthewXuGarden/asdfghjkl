package com.carel.supervisor.director.vscheduler;

import java.util.Hashtable;

import com.carel.supervisor.controller.ControllerMgr;
import com.carel.supervisor.controller.priority.PriorityMgr;
import com.carel.supervisor.controller.setfield.SetContext;
import com.carel.supervisor.controller.setfield.SetDequeuerMgr;
import com.carel.supervisor.controller.setfield.VSCallBack;
import java.util.Vector;
import java.util.Enumeration;

import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.*;
import java.util.GregorianCalendar;


// group data object
public class CGroup {
	private CCategory objCategory; // parent
	private String strId;
	private String strName;
	private String strType;
	private Hashtable<String, CSchedule> objStandard;
	private Hashtable<String, CSchedule> objExceptions;
	private Vector<CDevice> objDevices;
	private CSchedule objLastException;
	private static final int ENABLED = 0x01;
	private boolean bEnabled;
	private int idCmdVar;
	private int nCmdFlags;
	private int nCmdVal;

	
	public CGroup()
	{
		objCategory = null;
		objStandard = new Hashtable<String, CSchedule>();
		objExceptions = new Hashtable<String, CSchedule>();
		objDevices = new Vector<CDevice>();
		objLastException = null;
		idCmdVar = 0;
		nCmdFlags = 0;
		nCmdVal = -1; // -1 used to prevent a false transition at initialization
	}

	
	public CGroup(CCategory objCategory)
	{
		this.objCategory = objCategory;
		objStandard = new Hashtable<String, CSchedule>();
		objExceptions = new Hashtable<String, CSchedule>();
		objDevices = new Vector<CDevice>();
		idCmdVar = 0;
		nCmdFlags = 0;
		nCmdVal = -1; // -1 used to prevent a false transition at initialization
	}	
	
	
	// set parent category
	public void setCategory(CCategory objCategory)
	{
		this.objCategory = objCategory;
	}

	
	public CCategory getCategory()
	{
		return objCategory;
	}
	
	
	public void run(GregorianCalendar gc)
	{
		if( bEnabled ) {
			CInterval objInterval = null;
			int nYear = gc.get(GregorianCalendar.YEAR);
			int nMonth = gc.get(GregorianCalendar.MONTH) + 1;
			int nDay = gc.get(GregorianCalendar.DAY_OF_MONTH);
			if( isException(nYear, nMonth, nDay) ) {
				CSchedule objSchedule = getException(nYear, nMonth, nDay);
				objInterval = objSchedule.getInterval(gc);
				if( objSchedule != objLastException ) {
					if( objLastException != null )
						objLastException.updateFlags();
					objLastException = objSchedule;
				}
			}
			else if( isException(nMonth, nDay) ) {
				CSchedule objSchedule = getException(nMonth, nDay);
				objInterval = objSchedule.getInterval(gc);
				if( objSchedule != objLastException ) {
					if( objLastException != null )
						objLastException.updateFlags();
					objLastException = objSchedule;
				}
			}
			else {
				int nDOW = gc.get(GregorianCalendar.DAY_OF_WEEK) - 1;
				if( nDOW == 0 )
					nDOW = 7;
				CSchedule objSchedule = getStandard(nDOW);
				objInterval = objSchedule.getInterval(gc);
			}
			if( objInterval != null ) {
				// set variables for each device from the group
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
				objContext.setUser(CDataDef.MY_NAME);
				objContext.setCallback(new VSCallBack(this, gc));
				for(Enumeration<CDevice> e = objDevices.elements(); e.hasMoreElements();)
					e.nextElement().run(objInterval, objContext);
				if( objContext.numDevice() > 0 )
					SetDequeuerMgr.getInstance().add(objContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
			}
			// update exception flags
			if( objLastException != null
				&& (objLastException.getDay() != nDay || objLastException.getMonth() != nMonth || objLastException.getYear() != nYear) ) {
				objLastException.updateFlags();
				objLastException = null;
			}
		} // if( bEnabled )
		
		if( idCmdVar > 0 ) {
	    	try {
				// get command from field
	    		float fVal = ControllerMgr.getInstance().getFromField(idCmdVar).getCurrentValue();
	    		if( !Float.isNaN(fVal) ) {
		    		int nVal = (int)fVal;
		    		if( nCmdVal != -1 ) {
			    		if( nVal != nCmdVal ) { // transition detected
			    			nCmdVal = nVal;
							CCommand objCommand = new CCommand(CDataDef.strDigitalVal);
							objCommand.setUserName(CDataDef.MY_NAME);
							objCommand.setIdCat(objCategory.getId());
							objCommand.setIdGroup(strId);
							String strCmd;
							if( (nCmdFlags & CDataDef.nReverseLogic) == CDataDef.nReverseLogic ) { 
								objCommand.setDVal(nCmdVal == 0);
								strCmd = nCmdVal == 0 ? "On" : "Off";
							}
							else {
								objCommand.setDVal(nCmdVal == 1);
								strCmd = nCmdVal == 1 ? "On" : "Off";
							}
							EventMgr.getInstance().info(new Integer(1), CDataDef.MY_NAME, "Action", "VS06", new Object[] { 0, strName, strCmd });
							SchedulerHook.onCommand(objCommand);
			    		}
		    		}
		    		else {
		    			nCmdVal = nVal;
		    		}
	    		}
            } catch(Exception e) {
            	// nothing to do, maybe next time
            }
		}
	}
	
	
	public void execute(CCommand objCommand)
	{
		if( !objCommand.getIdGroup().equals(strId) )
			return;
		
		// set variables for each device from the group
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
		objContext.setUser(objCommand.getUserName());
		objContext.setCallback(new VSCallBack(this, new GregorianCalendar()));
		for(Enumeration<CDevice> e = objDevices.elements(); e.hasMoreElements();)
			e.nextElement().execute(objCommand, objContext);
		if( objContext.numDevice() > 0 )
			SetDequeuerMgr.getInstance().add(objContext, PriorityMgr.getInstance().getPriority(this.getClass().getName()));
	}
	
	
	public void loadDB(String strIdGroup)
	{
		if( !objDevices.isEmpty() )
			objDevices.clear();
		if( !objStandard.isEmpty() )
			objStandard.clear();
		if( !objExceptions.isEmpty() )
			objExceptions.clear();
		objLastException = null;
		
		strId = strIdGroup;
		try {
			String sql = "select * from vs_group where idgroup=" + strId;
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			if( rs.size() == 1 ) {
				Record r = rs.get(0);
				strName = r.get("name").toString();
				strType = CDataDef.strUndefVal;
				setFlags(Integer.parseInt(r.get("flags").toString()));

				if( objCategory.getData().isExecutive() ) {
					// retrieve devices
					sql = "select * from vs_groupdevs where idgroup=" + strId;
					rs = DatabaseMgr.getInstance().executeQuery(null, sql);
					if( rs.size() > 0 ) {
						int idSite = objCategory.getData().getIdSite();
						for(int i = 0; i < rs.size(); i++) {
							Record rdev = rs.get(i);
							int idDevice = Integer.parseInt(rdev.get("iddevice").toString());
							java.sql.Timestamp timestamp = (java.sql.Timestamp)rdev.get("timestamp");
							try {
								sql = "select iddevmdl from cfdevice where idsite=? and iddevice=? and iscancelled='FALSE'";
								RecordSet rsDevice = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] {idSite, idDevice});
								if( rsDevice.size() == 1 ) {
									CDevice objDevice = new CDevice(idDevice, Integer.parseInt(rsDevice.get(0).get(0).toString()), Integer.parseInt(strId), timestamp);
									objCategory.setVarInfo(objDevice);
									objDevices.add(objDevice);
									if( strType.equals(CDataDef.strUndefVal) )
										strType = objDevice.getType();
									else if( !strType.equals(objDevice.getType()) )
										strType = CDataDef.strMixedVal;
								}
							}
							catch(DataBaseException e) {
								LoggerMgr.getLogger(CGroup.class).error(e);
							}
						}
					}
					
					// cache group type for front end usage
					sql = "update vs_group set \"type\"=? where idgroup=?";
					DatabaseMgr.getInstance().executeStatement(null, sql,
						new Object[] { strType, Integer.parseInt(strId) });
				}
				else {
					Object objType = r.get("type");
					if( objType != null )
						strType = objType.toString();
				}
				
				// retrieve schedules
				loadDBSchedules();
				
				// retrieve commands
				loadDBCmds();
			}
		}
		catch(DataBaseException e) {
        	LoggerMgr.getLogger(CGroup.class).error(e);	
		}		
	}
	
	
	public void updateDB()
	{
		updateFlags(); // let the possibility to enable/disable the group even if it is undefined
		if( strType.equals(CDataDef.strUndefVal) )
			return;
		for(Enumeration<CSchedule> e = objStandard.elements(); e.hasMoreElements();)
			e.nextElement().updateDB();
		for(Enumeration<CSchedule> e = objExceptions.elements(); e.hasMoreElements();)
			e.nextElement().updateDB();
	}
	
	
	public void loadXML(XMLNode xmlGroup)
	{
		// clear schedules
		if( !objStandard.isEmpty() )
			objStandard.clear();
		if( !objExceptions.isEmpty() )
			objExceptions.clear();

		strId = xmlGroup.getAttribute(CDataDef.strIdAttr);
		strType = xmlGroup.getAttribute(CDataDef.strTypeAttr);
		bEnabled = xmlGroup.getAttribute(CDataDef.strEnabledAttr).equals(CDataDef.strEnabledVal);
		XMLNode axmlNode[] = xmlGroup.getNodes();
		if( axmlNode != null )
		for(int i = 0; i < axmlNode.length; i++) {
			if( axmlNode[i].getNodeName().equals(CDataDef.strScheduleNode) ) {
				CSchedule objSchedule = new CSchedule(this);
				objSchedule.loadXML(axmlNode[i]);
				if( objSchedule.isRemovedSchedule() ) // remove schedules before updating db
					objSchedule.removeIntervalsFromDB();
				else
					addSchedule(objSchedule);
			}
		}		
	}
	

	public XMLNode getXML()
	{
		XMLNode xmlGroup = new XMLNode(CDataDef.strGroupNode, "");
		xmlGroup.setAttribute(CDataDef.strIdAttr, strId);
		xmlGroup.setAttribute(CDataDef.strNameAttr, strName);
		xmlGroup.setAttribute(CDataDef.strTypeAttr, strType);
		xmlGroup.setAttribute(CDataDef.strEnabledAttr, bEnabled ? CDataDef.strEnabledVal : CDataDef.strDisabledVal);
		for(Enumeration<CSchedule> e = objStandard.elements(); e.hasMoreElements(); ) {
			CSchedule objSchedule = e.nextElement();
			xmlGroup.addNode(objSchedule.getXML());
		}
		for(Enumeration<CSchedule> e = objExceptions.elements(); e.hasMoreElements(); ) {
			CSchedule objSchedule = e.nextElement();
			xmlGroup.addNode(objSchedule.getXML());
		}
		return xmlGroup;
	}
	
	
	public String getType()
	{
		return strType;
	}
	
	
	private void loadDBSchedules()
	{
		// clear schedules
		if( objStandard.size() > 0 )
			objStandard.clear();
		if( objExceptions.size() > 0 )
			objExceptions.clear();
		
		try {
			CSchedule objSchedule = null;
			// retrieve standard schedules
			String sql = "select * from vs_schedule where idgroup=" + strId + " order by idday, start";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			int nLastDay = 0;
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				int nDay = Integer.parseInt(r.get("idday").toString());
				if( nDay != nLastDay ) {
					nLastDay = nDay;
					// add a fake time stop to analog intervals
					if( objSchedule != null && strType.equals(CDataDef.strAnalogVal) )
						objSchedule.setStops();					
					objSchedule = getStandard(nDay);
					objSchedule.clearIntervals();
				}
				CInterval objInterval = new CInterval(objSchedule);
				objInterval.loadDB(r);
				objSchedule.addInterval(objInterval);
			}
			// add a fake time stop to analog intervals (for the last schedule)
			if( objSchedule != null && strType.equals(CDataDef.strAnalogVal) )
				objSchedule.setStops();
			objSchedule = null;
			// retrieve exceptions
			sql = "select * from vs_exception where idgroup=" + strId + " order by idyear, idmonth, idday, start";
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			int nLastYear = 0;
			int nLastMonth = 0;
			nLastDay = 0;
			for(int i = 0; i < rs.size(); i++) {
				Record r = rs.get(i);
				int nYear = Integer.parseInt(r.get("idyear").toString());
				int nMonth = Integer.parseInt(r.get("idmonth").toString());
				int nDay = Integer.parseInt(r.get("idday").toString());
				int nFlags = Integer.parseInt(r.get("flags").toString());
				if( nYear != nLastYear || nMonth != nLastMonth || nDay != nLastDay ) {
					nLastYear = nYear;
					nLastMonth = nMonth;
					nLastDay = nDay;
					// add a fake time stop to analog intervals
					if( objSchedule != null && strType.equals(CDataDef.strAnalogVal) )
						objSchedule.setStops();
					if( (nFlags & CSchedule.RUN_ONCE) != 0 )
						objSchedule = getException(nYear, nMonth, nDay);
					else
						objSchedule = getException(nMonth, nDay);
					objSchedule.clearIntervals();
				}
				objSchedule.loadDB(r);
				CInterval objInterval = new CInterval(objSchedule);
				objInterval.loadDB(r);
				if( objInterval.getStartNumber() >= 0 )
					objSchedule.addInterval(objInterval);
			}
			// add a fake time stop to analog intervals (for the last exception)
			if( objSchedule != null && strType.equals(CDataDef.strAnalogVal) )
				objSchedule.setStops();					
		}
		catch(DataBaseException e) {
        	LoggerMgr.getLogger(CGroup.class).error(e);	
		}		
	}
	
	
	void loadDBCmds()
	{
		// clear cmd
		idCmdVar = 0;
		try {
			// retrieve cmd variable
			String sql = "select * from vs_groupcmds where idgroup=" + strId;
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			if( rs.size() > 0 ) {
				Record r = rs.get(0);
				idCmdVar = (Integer)r.get("idvariable");
				nCmdFlags = (Integer)r.get("flags");
			}
		}
		catch(DataBaseException e) {
        	LoggerMgr.getLogger(CGroup.class).error(e);	
		}		
	}
	
	
	private void addSchedule(CSchedule objSchedule)
	{
		if( objSchedule.getType().equals(CDataDef.strStandardVal) ) {
			objStandard.put("d" + objSchedule.getDay(), objSchedule);
		}
		else if( objSchedule.getType().equals(CDataDef.strExceptionsVal) ) {
			if( objSchedule.isRunOnce() )
				objExceptions.put("y" + objSchedule.getYear() + "m" + objSchedule.getMonth() + "d" + objSchedule.getDay(), objSchedule);
			else
				objExceptions.put("m" + objSchedule.getMonth() + "d" + objSchedule.getDay(), objSchedule);
		}
	}
	
	
	private CSchedule getStandard(int nDay)
	{
		String strSchedule = "d" + nDay;
		CSchedule objSchedule = objStandard.get(strSchedule);
		if( objSchedule == null ) {
			objSchedule = new CSchedule(this);
			objSchedule.setType(CDataDef.strStandardVal);
			objSchedule.setDay(nDay);
			objStandard.put(strSchedule, objSchedule);
		}
		return objSchedule;
	}

	
	private boolean isException(int nMonth, int nDay)
	{
		String strSchedule = "m" + nMonth + "d" + nDay;
		CSchedule objSchedule = objExceptions.get(strSchedule);
		return objSchedule != null && objSchedule.isEnabled();
	}


	private boolean isException(int nYear, int nMonth, int nDay)
	{
		String strSchedule = "y" + nYear + "m" + nMonth + "d" + nDay;
		CSchedule objSchedule = objExceptions.get(strSchedule);
		return objSchedule != null && objSchedule.isEnabled();
	}
	
	
	private CSchedule getException(int nMonth, int nDay)
	{
		String strSchedule = "m" + nMonth + "d" + nDay;
		CSchedule objSchedule = objExceptions.get(strSchedule);
		if( objSchedule == null ) {
			objSchedule = new CSchedule(this);
			objSchedule.setType(CDataDef.strExceptionsVal);
			objSchedule.setMonth(nMonth);
			objSchedule.setDay(nDay);
			objExceptions.put(strSchedule, objSchedule);
		}
		return objSchedule;
	}
	
	
	private CSchedule getException(int nYear, int nMonth, int nDay)
	{
		String strSchedule = "y" + nYear + "m" + nMonth + "d" + nDay;
		CSchedule objSchedule = objExceptions.get(strSchedule);
		if( objSchedule == null ) {
			objSchedule = new CSchedule(this);
			objSchedule.setType(CDataDef.strExceptionsVal);
			objSchedule.setYear(nYear);
			objSchedule.setMonth(nMonth);
			objSchedule.setDay(nDay);
			objExceptions.put(strSchedule, objSchedule);
		}
		return objSchedule;
	}
	
	
	public CSchedule getSchedule(GregorianCalendar gc)
	{
		CSchedule objSchedule = null;

		int nYear = gc.get(GregorianCalendar.YEAR);
		int nMonth = gc.get(GregorianCalendar.MONTH) + 1;
		int nDay = gc.get(GregorianCalendar.DAY_OF_MONTH);
		if( isException(nYear, nMonth, nDay) ) {
			objSchedule = getException(nYear, nMonth, nDay);
		}
		else if( isException(nMonth, nDay) ) {
			objSchedule = getException(nMonth, nDay);
		}
		else {
			int nDOW = gc.get(GregorianCalendar.DAY_OF_WEEK) - 1;
			if( nDOW == 0 )
				nDOW = 7;
			objSchedule = getStandard(nDOW);
		}
		
		return objSchedule;
	}
	
	
	public String getId()
	{
		return strId;
	}
	
	
	public String getName()
	{
		return strName;
	}
	
	
	public void setFlags(int nFlags)
	{
		bEnabled = (nFlags & ENABLED) == ENABLED;
	}
	
	
	public int getFlags()
	{
		int nFlags = 0;
		if( bEnabled ) nFlags |= ENABLED;
		return nFlags;
	}
	
	
	public boolean isEnabled()
	{
		return bEnabled;
	}	

	
	public void updateFlags()	
	{
		try {
			String sql = "update vs_group set flags=? where idgroup=?";
			Object params[] = new Object[] { getFlags(), Integer.parseInt(strId) };
			DatabaseMgr.getInstance().executeStatement(null, sql, params);
		}
		catch(DataBaseException e) {
        	LoggerMgr.getLogger(CGroup.class).error(e);	
		}		
	}
	

	// app query
	public CInterval getNextInterval(GregorianCalendar gc)
	{
		CSchedule objSchedule = getSchedule(gc);
		if( objSchedule != null )
			return objSchedule.getNextInterval(gc);
			
		return null;
	}
	
	
	// notifications
	public void onError(int idDevice)
	{
		for(Enumeration<CDevice> e = objDevices.elements(); e.hasMoreElements();) {
			CDevice objDevice = e.nextElement();
			if( objDevice.getIdDevice() == idDevice ) {
				objDevice.onError();
				break;
			}
		}
	}
	
	
	public void onRunEnd(GregorianCalendar gc)
	{
		for(Enumeration<CDevice> e = objDevices.elements(); e.hasMoreElements();)
			e.nextElement().onRunEnd(gc);
	}
}
