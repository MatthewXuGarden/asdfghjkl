package com.carel.supervisor.director.vscheduler;

import java.util.Enumeration;
import java.util.Vector;
import java.util.Properties;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.xml.*;
import java.util.GregorianCalendar;


// category data object
// category is a collection of groups
public class CCategory {
	private CData objData; // parent
	private String strName;
	private String strId;
	private String strIcon;
	Vector<CGroup> objGroups;
	Vector<Properties> objCatMap;

	
	public CCategory()
	{
		objData = null;
		objGroups = new Vector<CGroup>();
		objCatMap = new Vector<Properties>();
	}
	
	
	public CCategory(CData objData)
	{
		this.objData = objData;
		objGroups = new Vector<CGroup>();
		objCatMap = new Vector<Properties>();
	}
	
	
	public String getId()
	{
		return strId;
	}
	
	
	// set parent data
	public void setData(CData objData)
	{
		this.objData = objData;
	}
	
	
	public CData getData()
	{
		return objData;
	}


	public void run(GregorianCalendar gc)
	{
		for(Enumeration<CGroup> e = objGroups.elements(); e.hasMoreElements();)
			e.nextElement().run(gc);
	}

	
	public void execute(CCommand objCommand)
	{
		if( !objCommand.getIdCat().equals(strId) )
			return;
		
		for(Enumeration<CGroup> e = objGroups.elements(); e.hasMoreElements();)
			e.nextElement().execute(objCommand);
	}
	
	
	public void setVarInfo(CDevice objDevice)
	{
		try {
			int anDevMap[] = getDevMap(objDevice.getIdDevMdl());
			for(int i = 1; i <= anDevMap[0]; i++) {
				Properties prop = objCatMap.get(anDevMap[i]);
				String sql = "select idvariable from cfvariable where iddevice=? and idvarmdl=? order by idvariable;";
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
						new Object[] {objDevice.getIdDevice(), Integer.parseInt(prop.getProperty("idvarmdl"))});
				if( rs.size() >= 1 ) {
					objDevice.setVarInfo(Integer.parseInt(rs.get(0).get(0).toString()),
						Integer.parseInt(prop.getProperty("type")),
						Integer.parseInt(prop.getProperty("flags")),
						prop.getProperty("check").equals("true"));
				}
			}
		} catch(Exception e) {
        	LoggerMgr.getLogger(CCategory.class).error(e);	
		}
	}
	
	
	private int[] getDevMap(int idDevMdl)
	{
		int anDevMap[] = new int[256];
		anDevMap[0] = 0;
		
		for(int i = 0; i < objCatMap.size(); i++) {
			if( Integer.parseInt(objCatMap.get(i).getProperty("iddevmdl")) == idDevMdl )
				anDevMap[++anDevMap[0]] = i;
		}
		
		return anDevMap;
	}
	
	
	public void loadDB(String strIdCat)
	{
		if( !objCatMap.isEmpty() )
			objCatMap.clear();
		if( !objGroups.isEmpty() )
			objGroups.clear();
		
		strId = strIdCat;
		try {
			// retrieve category
			String sql = "select * from vs_category where idcategory=" + strId;
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			if( rs.size() == 1 ) {
				Record r = rs.get(0);
				strName = r.get("name").toString();
				strIcon = r.get("symbol").toString();
				
				if( objData.isExecutive() ) {
					// retrieve category map
			       	sql = "select vs_categorymap.*, "
						//+ "(select cftableext.description from cftableext where vs_categorymap.iddevmdl=cftableext.tableid and cftableext.languagecode = ? and cftableext.tablename='cfdevmdl' and cftableext.idsite = ?) as devname, "
						//+ "(select cftableext.description from cftableext where vs_categorymap.idvarmdl=cftableext.tableid and cftableext.languagecode = ? and cftableext.tablename='cfvarmdl' and cftableext.idsite = ?) as varname, "
						+ "(select cfvarmdl.type from cfvarmdl where vs_categorymap.idvarmdl=cfvarmdl.idvarmdl and cfvarmdl.idsite = ?) as type "
						+ "from vs_categorymap where idcategory = ?";
						//+ "order by devname, varname";
					Object params[] = new Object[] {
						//getData().getLanguage(), getData().getIdSite(),
						//getData().getLanguage(), getData().getIdSite(),
						getData().getIdSite(),
						Integer.parseInt(strId)
					};
					rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
					for(int i = 0; i < rs.size(); i++) {
						Record rMap = rs.get(i);
						Properties prop = new Properties();
						prop.setProperty("iddevmdl", rMap.get("iddevmdl").toString());
						//prop.setProperty("devname", rMap.get("devname").toString());
						prop.setProperty("idvarmdl", rMap.get("idvarmdl").toString());
						//prop.setProperty("varname", rMap.get("varname").toString());
						prop.setProperty("type", rMap.get("type").toString());
						prop.setProperty("flags", rMap.get("flags").toString());
						// cache cfvarcmd information
						String sqlVarCmd = "select * from cfvarcmd where idvarmdl=" + prop.getProperty("idvarmdl");
						RecordSet rsVarCmd = DatabaseMgr.getInstance().executeQuery(null, sqlVarCmd);
						prop.setProperty("check", rsVarCmd.size() > 0 ? "false" : "true");
						objCatMap.add(prop);
					}
				}
				
				// retrieve groups
				sql = "select idgroup from vs_group where idcategory=" + strId + " order by name";
				rs = DatabaseMgr.getInstance().executeQuery(null, sql);
				for(int i = 0; i < rs.size(); i++) {
					String strIdGroup = rs.get(i).get(0).toString();
					CGroup objGroup = new CGroup(this);
					objGroup.loadDB(strIdGroup);
					objGroups.add(objGroup);
					
					Thread.sleep(10); // to prevent cpu overload
				}
			}
		}
		catch(DataBaseException e) {
        	LoggerMgr.getLogger(CCategory.class).error(e);	
		}
		catch(Exception e) {
			LoggerMgr.getLogger(CCategory.class).error(e);
		}
	}
	
	
	public void updateDB()
	{
		for(Enumeration<CGroup> e = objGroups.elements(); e.hasMoreElements();)
			e.nextElement().updateDB();
	}
	
	
	public void loadXML(XMLNode xmlCategory)
	{
		if( !objGroups.isEmpty() )
			objGroups.clear();
		
		strId = xmlCategory.getAttribute(CDataDef.strIdAttr);
		XMLNode axmlNode[] = xmlCategory.getNodes();
		if( axmlNode != null )
		for(int i = 0; i < axmlNode.length; i++) {
			if( axmlNode[i].getNodeName().equals(CDataDef.strGroupNode) ) {
				CGroup objGroup = new CGroup(this);
				objGroup.loadXML(axmlNode[i]);
				objGroups.add(objGroup);
			}
		}
	}
	
	
	public XMLNode getXML()
	{
		XMLNode xmlCategory = new XMLNode(CDataDef.strCategoryNode, "");
		xmlCategory.setAttribute(CDataDef.strIdAttr, strId);
		xmlCategory.setAttribute(CDataDef.strNameAttr, strName);
		xmlCategory.setAttribute(CDataDef.strIconAttr, strIcon);
		for(Enumeration<CGroup> e = objGroups.elements(); e.hasMoreElements();)
			xmlCategory.addNode(e.nextElement().getXML());
		return xmlCategory;
	}
	
	
	// app query
	public CGroup getGroup(String strIdGroup)
	{
		for(Enumeration<CGroup> e = objGroups.elements(); e.hasMoreElements();) {
			CGroup objGroup = e.nextElement(); 
			if( objGroup.getId().equals(strIdGroup) )
				return objGroup;
		}
		
		return null;
	}
}
