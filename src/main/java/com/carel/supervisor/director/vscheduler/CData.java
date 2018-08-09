package com.carel.supervisor.director.vscheduler;

import java.util.Enumeration;
import java.util.Vector;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.*;
import java.util.GregorianCalendar;


// main data object
// data object is a collection of categories
public class CData {
	protected int idSite;
	protected String strLanguage;

	private String strVersion;	// xml data version
	private Vector<CCategory> objCategories;
	
	protected boolean bExecutive;
	

	public CData()
	{
		idSite = 1;
		strLanguage = "EN_en";
		strVersion = CDataDef.strVersionVal;
		objCategories = new Vector<CCategory>();
		bExecutive = true;
	}

	
	public CData(int idSite, String strLanguage)
	{
		this.idSite = idSite;
		this.strLanguage = strLanguage;
		strVersion = CDataDef.strVersionVal;
		objCategories = new Vector<CCategory>();
		bExecutive = true;
	}
	
	
	public void run(GregorianCalendar gc)
	{
		for(Enumeration<CCategory> e = objCategories.elements(); e.hasMoreElements();)
			e.nextElement().run(gc);		
	}
	
	
	public void execute(CCommand objCommand)
	{
		for(Enumeration<CCategory> e = objCategories.elements(); e.hasMoreElements();)
			e.nextElement().execute(objCommand);		
	}
	
	
	// fetch categories from DB
	public void loadDB(boolean bExecutive)
	{
		this.bExecutive = bExecutive;
		if( !objCategories.isEmpty() )
			objCategories.clear();
		
		try {
			String sql = "select idcategory from vs_category where app is NULL order by idcategory";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			for(int i = 0; i < rs.size(); i++) {
				String strIdCat = rs.get(i).get(0).toString();
				CCategory objCat = new CCategory(this);
				objCat.loadDB(strIdCat);
				objCategories.add(objCat);
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(CData.class).error(e);
		}
	}

	
	public void loadUIDB()
	{
		loadDB(false);
	}
	
	
	// fetch app related categories from DB
	public void loadDB(String app)
	{
		this.bExecutive = false;
		if( !objCategories.isEmpty() )
			objCategories.clear();
		
		try {
			String sql = "select idcategory from vs_category where app = ? order by idcategory";
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { app });
			for(int i = 0; i < rs.size(); i++) {
				String strIdCat = rs.get(i).get(0).toString();
				CCategory objCat = new CCategory(this);
				objCat.loadDB(strIdCat);
				objCategories.add(objCat);
			}
		}
		catch(DataBaseException e) {
			LoggerMgr.getLogger(CData.class).error(e);
		}
	}

	
	// app query
	public CInterval getNextInterval(String strIdGroup, GregorianCalendar gc)
	{
		for(Enumeration<CCategory> e = objCategories.elements(); e.hasMoreElements();) {
			CCategory objCategory = e.nextElement(); 
			CGroup objGroup = objCategory.getGroup(strIdGroup);
			if( objGroup != null )
				return objGroup.getNextInterval(gc);
		}

		return null;
	}
	
	
	public boolean updateDB()
	{
		for(Enumeration<CCategory> e = objCategories.elements(); e.hasMoreElements();)
			e.nextElement().updateDB();
		
		return !objCategories.isEmpty();
	}
	
	
	public void loadXML(String strXML, String strUserName)
	{
		if( !objCategories.isEmpty() )
			objCategories.clear();
		
		try
		{
			XMLNode xmlData = XMLNode.parse(strXML);
			if( xmlData.getNodeName().equals(CDataDef.strRootNode) ) {
				strVersion = xmlData.getAttribute(CDataDef.strVersionAttr);
				if( strVersion.equals(CDataDef.strVersionVal) ) {
					XMLNode axmlNode[] = xmlData.getNodes();
					if( axmlNode != null )
					for(int i = 0; i < axmlNode.length; i++) {
						String strNodeName = axmlNode[i].getNodeName(); 
						if( strNodeName.equals(CDataDef.strCategoryNode) ) {
							CCategory objCat = new CCategory(this);
							objCat.loadXML(axmlNode[i]);
							objCategories.add(objCat);
						}
						else if( strNodeName.equals(CDataDef.strCommandNode) ) {
							CCommand objCommand = new CCommand();
							objCommand.setUserName(strUserName);
							objCommand.loadXML(axmlNode[i]);
							SchedulerHook.onCommand(objCommand);
						}
						else if( strNodeName.equals(CDataDef.strLogNode) ) {
							String strType = axmlNode[i].getAttribute(CDataDef.strTypeAttr);
							String strMessage = axmlNode[i].getAttribute(CDataDef.strMessageAttr);
							LogMessage(strType, strMessage);
						}
					}
				}
				else {
					String strErr = "different xml versions: server " + CDataDef.strVersionVal + " / flash " + strVersion;
					LoggerMgr.getLogger(CData.class).error(strErr);
				}
			}
		}
		catch (Exception e)	{
			LoggerMgr.getLogger(CData.class).error(e);
			LoggerMgr.getLogger(CData.class).info(strXML);
		}
	}
	
	
	public String getTextXML()
	{
		return getXML().getStringBuffer().toString();
	}
	

	public XMLNode getXML()
	{
		XMLNode xmlData = new XMLNode(CDataDef.strRootNode, "");
		xmlData.setAttribute(CDataDef.strVersionAttr, strVersion);
		for(Enumeration<CCategory> e = objCategories.elements(); e.hasMoreElements();)
			xmlData.addNode(e.nextElement().getXML());
		return xmlData;
	}

	
	public int getIdSite()
	{
		return idSite;
	}
	
	
	public String getLanguage()
	{
		return strLanguage;
	}
	
	
	public boolean isExecutive()
	{
		return bExecutive;
	}
	
	
	public void LogMessage(String strType, String strMessage)
	{
		if( strType.equals(CDataDef.strErrorVal) )
			LoggerMgr.getLogger(CData.class).error(strMessage);
		else if( strType.equals(CDataDef.strWarningVal) )
			LoggerMgr.getLogger(CData.class).warn(strMessage);
		else
			LoggerMgr.getLogger(CData.class).info(strMessage);
	}
}
