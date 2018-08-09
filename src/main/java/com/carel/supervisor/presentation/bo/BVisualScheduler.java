package com.carel.supervisor.presentation.bo;

import java.util.Properties;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.vscheduler.*;
import com.carel.supervisor.presentation.bean.DeviceBean;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.VarMdlBean;
import com.carel.supervisor.presentation.bean.VarMdlBeanList;
import com.carel.supervisor.director.vscheduler.*;


public class BVisualScheduler extends BoMaster {
	
	private static final int REFRESH_TIME = -1;
	
	
	public BVisualScheduler(String l)
    {
        super(l, REFRESH_TIME);
    }
	
	
	protected Properties initializeEventOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "VisualSchedulerInit();");
		p.put("tab2name", "onGroupsLoad();");
		p.put("tab3name", "onCategoriesLoad();");
		return p;
	}
	
	
	protected Properties initializeJsOnLoad()
	{
		Properties p = new Properties();
		p.put("tab1name", "vs.js;keyboard.js;");
		p.put("tab2name", "dbllistbox.js;vs.js;keyboard.js;");
		p.put("tab3name", "vs.js;../arch/FileDialog.js;keyboard.js;");
		return p;
	}
	
	
	protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab1name", DOCTYPE_STRICT);
		p.put("tab3name", DOCTYPE_STRICT);
		return p;
    }

	
    public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
    {
    	if( tabName.equalsIgnoreCase("tab2name") )
        	executeGroupAction(us, prop);
        else if( tabName.equalsIgnoreCase("tab3name") )
        	executeCategoryAction(us, prop);
    }
    
    
    public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
    {
		String strAction = prop.getProperty("action");
    	if( tabName.equalsIgnoreCase("tab2name") ) {
    		if( strAction.equals("dev") )
    			return getGroupDevResponse(us, prop);
    		if( strAction.equals("var") )
    			return getGroupVarResponse(us, prop);
    		else if( strAction.equals("group") )
    			return getGroupResponse(us, prop);
    	 }
   		 else if( tabName.equalsIgnoreCase("tab3name") ) {
   			if( strAction.equals("var") )
   				return getVarMdlResponse(us, prop);
   			else if( strAction.equals("map") )
   				return getCatMapResponse(us, prop);
   		 }
    	String strResponse = "<response/>";
    	return strResponse;
    }
    
    
    private void executeGroupAction(UserSession us, Properties prop)
    {
    	VSGroup objGroup = new VSGroup();
    	String strCmd = prop.getProperty("cmd");
    	if( strCmd.equals("add") || strCmd.equals("save") ) {
    		String strName = prop.getProperty("name");
    		int idCategory = Integer.parseInt(prop.getProperty("category"));
    		String strDevs = prop.getProperty("devs");
    		int idCmdVar = Integer.parseInt(prop.getProperty("cmd_var"));
    		int nCmdFlags = 0;
    		String strReverseLogic = prop.getProperty("cmd_reverse_logic");
    		if( strReverseLogic.equalsIgnoreCase("true") )
    			nCmdFlags |= CDataDef.nReverseLogic;
    		int anDevs[] = null;
    		if( strDevs.length() > 0 ) {
    			String astrDevs[] = strDevs.split(",");
    			anDevs = new int[astrDevs.length];
    			for(int i = 0; i < anDevs.length; i++)
    				anDevs[i] = Integer.parseInt(astrDevs[i]);
    		}
    		else {
    			anDevs = new int[0];
    		}
    		if( strCmd.equals("save") ) {
        		String strId = prop.getProperty("id");
        		int idGroup = Integer.parseInt(strId);
        		objGroup.updateGroup(idGroup, strName, idCategory, anDevs, idCmdVar, nCmdFlags);
    		}
    		else {
    			objGroup.addGroup(strName, idCategory, anDevs, idCmdVar, nCmdFlags);
    		}
    	}
    	else if( strCmd.equals("remove") ) {
    		String strId = prop.getProperty("id");
    		int id = Integer.parseInt(strId);
    		objGroup.removeGroup(id);
    	}
    	SchedulerHook.dbChanged();
    }
    
    
    private void executeCategoryAction(UserSession us, Properties prop)
    {
    	VSCategory objCat = new VSCategory();
    	String strCmd = prop.getProperty("cmd");
    	if( strCmd.equals("add") || strCmd.equals("save") ) {
    		String strName = prop.getProperty("name");
    		String strSymbol = prop.getProperty("symbol");
    		String strMap = prop.getProperty("map");
    		int anDevVarMdl[][] = null;
    		if( strMap.length() > 0 ) {
	    		String astrDevVarMdl[] = strMap.split(";");
	    		anDevVarMdl = new int[astrDevVarMdl.length][];
	    		for(int i = 0; i < anDevVarMdl.length; i++) {
	    			String astrTuple[] = astrDevVarMdl[i].split(",");
	    			anDevVarMdl[i] = new int[astrTuple.length];
	    			for(int j = 0; j < anDevVarMdl[i].length; j++)
	    				if( j < 2 )
	    					anDevVarMdl[i][j] = Integer.parseInt(astrTuple[j]);
	    				else
	    					anDevVarMdl[i][j] = astrTuple[j].equalsIgnoreCase("true") ? 1 : 0;
	    		}
    		}
    		else {
    			anDevVarMdl = new int[0][];
    		}
    		if( strCmd.equals("save") ) {
        		String strId = prop.getProperty("id");
        		int id = Integer.parseInt(strId);
        		objCat.updateCategory(id, strName, strSymbol, anDevVarMdl);
    		}
    		else {
    			objCat.addCategory(strName, strSymbol, anDevVarMdl);
    		}
    		SchedulerHook.dbChanged();
    	}
    	else if( strCmd.equals("remove") ) {
    		String strId = prop.getProperty("id");
    		int id = Integer.parseInt(strId);
    		objCat.removeCategory(id);
    		SchedulerHook.dbChanged();
    	}
    	else if( strCmd.equals("import") ) {
    		String strFileName = prop.getProperty("impexp");
			if( strFileName != null && !strFileName.isEmpty() && objCat.importCategories(strFileName) ) {
				LangService lang = LangMgr.getInstance().getLangService(us.getLanguage());
				StringBuffer strResponse = new StringBuffer();
				strResponse.append(lang.getString("vs", "import_success"));
				strResponse.append("\n");
				String strTemp = objCat.getNoDevMdls();
				if( strTemp != null ) {
					strResponse.append("\n");
					strResponse.append(lang.getString("vs", "import_no_devmdl"));
					strResponse.append("\n");
					strResponse.append(strTemp);
				}
				strTemp = objCat.getEmptyCat();
				if( strTemp != null ) {
					strResponse.append("\n");
					strResponse.append(lang.getString("vs", "import_empty_cat"));
					strResponse.append("\n");
					strResponse.append(strTemp);
				}				
				us.setProperty("vs_alert", strResponse.toString());
				SchedulerHook.dbChanged();
			}
			else
				us.setProperty("vs_alert", LangMgr.getInstance().getLangService(us.getLanguage()).getString("vs", "import_failure") + strFileName);
    	}
    	else if( strCmd.equals("export") ) {
    		String strFileName = prop.getProperty("impexp");
    		if( strFileName != null && !strFileName.isEmpty() && objCat.exportCategories(strFileName) )
    			us.setProperty("vs_alert", LangMgr.getInstance().getLangService(us.getLanguage()).getString("vs", "export_success") + strFileName);
    		else
    			us.setProperty("vs_alert", LangMgr.getInstance().getLangService(us.getLanguage()).getString("vs", "export_failure") + strFileName);
    	}
    }
    
    
    private String getGroupDevResponse(UserSession us, Properties prop)
    {
    	StringBuffer strbufResponse = new StringBuffer();
       	strbufResponse.append("<response>\n");
       	try {
       		int idCat = Integer.parseInt(prop.getProperty("id"));
       		DeviceListBean beanDevList = new DeviceListBean(us.getIdSite(), us.getLanguage());
       		int idDevs[] = beanDevList.getIds();
       		if( idDevs != null ) {
       			VSCategory cat = new VSCategory();
       			for(int i = 0; i < idDevs.length; i++) {
       				DeviceBean beanDevice = DeviceListBean.retrieveSingleDeviceById(us.getIdSite(), idDevs[i], us.getLanguage());
       				if( cat.isDevAvailable(idCat, beanDevice.getIddevmdl(), beanDevice.getIddevice()) ) {
       					strbufResponse.append("<dev id=\"");
       					strbufResponse.append("" + beanDevice.getIddevice());
       					strbufResponse.append("\" name=\"");
       					strbufResponse.append(VSBase.xmlEscape(beanDevice.getDescription()));
       					strbufResponse.append("\" var_no=\"");
       					strbufResponse.append("" + VSGroup.getDeviceVariablesNo(beanDevice.getIddevice(), idCat));
       					strbufResponse.append("\"/>\n");
       				}
       			}
       		}
       	}
		catch(DataBaseException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
		}
	    strbufResponse.append("</response>");
    	return strbufResponse.toString();    	
    }

    
    private String getGroupVarResponse(UserSession us, Properties prop)
    {
    	StringBuffer strbufResponse = new StringBuffer();
       	strbufResponse.append("<response>\n");
       	try {
       		// retrieve digital and alarm variables
       		int idDevice = Integer.parseInt(prop.getProperty("id"));
       		String sql = "select idvariable, code, type, description from cfvariable inner join cftableext"
       			+ " on cfvariable.idvariable=cftableext.tableid"
       			+ " where cftableext.idsite=? and languagecode=? and iddevice=?"
       			+ " and tablename='cfvariable' and idhsvariable is not null and type in (1,4)";
       		Object params[] = new Object[] { us.getIdSite(), us.getLanguage(), idDevice };
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
   			for(int i = 0; i < rs.size(); i++) {
   				Record r = rs.get(i);
				strbufResponse.append("<var id=\"");
       			strbufResponse.append(r.get("idvariable").toString());
       			strbufResponse.append("\" name=\"");
       			strbufResponse.append(VSBase.xmlEscape(r.get("description").toString()));
       			strbufResponse.append("\" code=\"");
       			strbufResponse.append(VSBase.xmlEscape(r.get("code").toString()));
       			strbufResponse.append("\" type=\"");
       			strbufResponse.append(r.get("type").toString());
       			strbufResponse.append("\"/>\n");
       		}
       	}
		catch(DataBaseException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
		}
	    strbufResponse.append("</response>");
    	return strbufResponse.toString();    	
    }
    
    
    private String getGroupResponse(UserSession us, Properties prop)
    {
       	StringBuffer strbufResponse = new StringBuffer();
       	strbufResponse.append("<response>\n");
       	try {
	       	int idGroup = Integer.parseInt(prop.getProperty("id"));
	       	int idCat = 0;
	       	String sql = "select * from vs_group where idgroup = " + idGroup;
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			if( rs.size() == 1 ) {
				Record r = rs.get(0);
				strbufResponse.append("<group id=\"");
				strbufResponse.append(r.get("idgroup").toString());
				strbufResponse.append("\" name=\"");
				strbufResponse.append(VSBase.xmlEscape(r.get("name").toString()));
				strbufResponse.append("\" idcat=\"");
				String strIdCat = r.get("idcategory").toString();
				strbufResponse.append(strIdCat);
				idCat = Integer.parseInt(strIdCat);
				strbufResponse.append("\" />\n");
			}
			sql = "select iddevice from vs_groupdevs where idgroup=" + idGroup;
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			for(int i = 0; i < rs.size(); i++) {
				int idDevice = (Integer)rs.get(i).get("iddevice");
				try {
					DeviceBean beanDevice = DeviceListBean.retrieveSingleDeviceById(us.getIdSite(), idDevice, us.getLanguage());
					strbufResponse.append("<groupdev id=\"");
					strbufResponse.append("" + idDevice);
					strbufResponse.append("\" name=\"");
					strbufResponse.append(VSBase.xmlEscape(beanDevice.getDescription()));
   					strbufResponse.append("\" var_no=\"");
   					strbufResponse.append("" + VSGroup.getDeviceVariablesNo(idDevice, idCat));
					strbufResponse.append("\" />\n");
				}
				catch(Exception e) {
					// device not found; remove it from group
					VSGroup group = new VSGroup();
					group.removeDevice(idDevice);
				}
			}
			sql = "select idvariable, flags from vs_groupcmds where idgroup = " + idGroup;
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			for(int i = 0; i < rs.size(); i++) {
				int idCmdVar = (Integer)rs.get(i).get("idvariable");
				int nCmdFlags = (Integer)rs.get(i).get("flags");
				String strDevice = "";
				String strVariable = "";
				sql = "select description from cftableext where idsite=? and languagecode=?"
					+ " and tablename='cfdevice' and tableid = (select iddevice from cfvariable where idvariable = ?)";				
				Object params[] = new Object[] { us.getIdSite(), us.getLanguage(), idCmdVar };
				RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null, sql, params);
				if( rs2.size() > 0 )
					strDevice = rs2.get(0).get(0).toString();
				sql = "select description from cftableext where idsite=? and languagecode=?"
					+ " and tablename='cfvariable' and tableid = ?";				
				rs2 = DatabaseMgr.getInstance().executeQuery(null, sql, params);
				if( rs2.size() > 0 )
					strVariable = rs2.get(0).get(0).toString();
				strbufResponse.append("<cmdvar id=\"" + idCmdVar);
				strbufResponse.append("\" name=\"");
				strbufResponse.append(strDevice + " - " + strVariable);
				strbufResponse.append("\" reverse_logic=\"");
				strbufResponse.append((nCmdFlags & CDataDef.nReverseLogic) == CDataDef.nReverseLogic ? "true" : "false");
				strbufResponse.append("\"/>\n");
			}
       		DeviceListBean beanDevList = new DeviceListBean(us.getIdSite(), us.getLanguage());
       		int idDevs[] = beanDevList.getIds();
       		if( idDevs != null ) {
       			VSCategory cat = new VSCategory();
       			for(int i = 0; i < idDevs.length; i++) {
       				DeviceBean beanDevice = DeviceListBean.retrieveSingleDeviceById(us.getIdSite(), idDevs[i], us.getLanguage());
       				if( cat.isDevAvailable(idCat, beanDevice.getIddevmdl(), beanDevice.getIddevice()) ) {
       					strbufResponse.append("<dev id=\"");
       					strbufResponse.append("" + beanDevice.getIddevice());
       					strbufResponse.append("\" name=\"");
       					strbufResponse.append(VSBase.xmlEscape(beanDevice.getDescription()));
       					strbufResponse.append("\" var_no=\"");
       					strbufResponse.append("" + VSGroup.getDeviceVariablesNo(beanDevice.getIddevice(), idCat));
       					strbufResponse.append("\"/>\n");
       				}
       			}
       		}
       	}
		catch(DataBaseException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
		}
		
 	    strbufResponse.append("</response>");
    	return strbufResponse.toString();    	
    }
    
  
    private String getVarMdlResponse(UserSession us, Properties prop)
    {
       	StringBuffer strbufResponse = new StringBuffer();
       	
       	strbufResponse.append("<response>\n");
       	try {
	       	int idDevMdl = Integer.parseInt(prop.getProperty("id")); 
       		VarMdlBean aVarMdl[] = VarMdlBeanList.retrieveVSVarMdls(us.getIdSite(), idDevMdl, us.getLanguage());
	       	for(int i = 0; i < aVarMdl.length; i++) {
	       		strbufResponse.append("<varmdl id=\"");
	       		strbufResponse.append("" + aVarMdl[i].getIdvarmdl());
	       		strbufResponse.append("\" code=\"");
	       		strbufResponse.append("" + aVarMdl[i].getCode());
	       		strbufResponse.append("\" name=\"");
	       		strbufResponse.append("" + VSBase.xmlEscape(aVarMdl[i].getDescription()));
	       		strbufResponse.append("\" type=\"");
	       		strbufResponse.append("" + aVarMdl[i].getType());
	       		strbufResponse.append("\"/>\n");
	       	}
       	}
		catch(DataBaseException e) {
			e.printStackTrace();
		}
	    strbufResponse.append("</response>");
    	return strbufResponse.toString();
    }
    

    private String getCatMapResponse(UserSession us, Properties prop)
    {
       	StringBuffer strbufResponse = new StringBuffer();
       	strbufResponse.append("<response>\n");
       	try {
	       	int idCat = Integer.parseInt(prop.getProperty("id"));
	       	String sql = "select * from vs_category where idcategory = " + idCat;
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			if( rs.size() == 1 ) {
				Record r = rs.get(0);
				strbufResponse.append("<cat id=\"");
				strbufResponse.append(r.get("idcategory").toString());
				strbufResponse.append("\" name=\"");
				strbufResponse.append(VSBase.xmlEscape(r.get("name").toString()));
				strbufResponse.append("\" symbol=\"");
				strbufResponse.append(r.get("symbol").toString());
				strbufResponse.append("\" devmdls=\"");
				
				String strDevMdls = "";
				sql = "select iddevmdl from cfdevice where iddevice in "
					+ "(select iddevice from vs_group inner join vs_groupdevs on vs_group.idgroup=vs_groupdevs.idgroup "
					+ "where idcategory=?)";
				rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idCat });
				for(int i = 0; i < rs.size(); i++) {
					if( i > 0 )
						strDevMdls += ",";
					strDevMdls += rs.get(i).get(0).toString();
				}
				
				strbufResponse.append(strDevMdls);				
				strbufResponse.append("\" />\n");
				
			}
	       	sql = "select vs_categorymap.*, "
				+ "(select cftableext.description from cftableext where vs_categorymap.iddevmdl=cftableext.tableid and cftableext.languagecode = ? and cftableext.tablename='cfdevmdl' and cftableext.idsite = ?) as devname, "
				+ "(select cftableext.description from cftableext where vs_categorymap.idvarmdl=cftableext.tableid and cftableext.languagecode = ? and cftableext.tablename='cfvarmdl' and cftableext.idsite = ?) as varname, "
				+ "(select cfvarmdl.type from cfvarmdl where vs_categorymap.idvarmdl=cfvarmdl.idvarmdl and cfvarmdl.idsite = ?) as type "
				+ "from vs_categorymap where idcategory = ? order by devname, varname";
			Object params[] = new Object[] {
				us.getLanguage(), us.getIdSite(),
				us.getLanguage(), us.getIdSite(),
				us.getIdSite(),
				idCat
			};
			rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
			for(int i = 0; i < rs.size(); i++) {
				strbufResponse.append("<catmap iddevmdl=\"");
				Record r = rs.get(i);
				strbufResponse.append(r.get("iddevmdl").toString());
				strbufResponse.append("\" devname=\"");
				strbufResponse.append(VSBase.xmlEscape(r.get("devname").toString()));
				strbufResponse.append("\" idvarmdl=\"");
				strbufResponse.append(r.get("idvarmdl").toString());
				strbufResponse.append("\" varname=\"");
				strbufResponse.append(VSBase.xmlEscape(r.get("varname").toString()));
				strbufResponse.append("\" type=\"");
				String strType = r.get("type").toString();
				strbufResponse.append(strType);
				strbufResponse.append("\"");
				if( strType.equals("1") ) { // digital
					int nFlags = Integer.parseInt(r.get("flags").toString());
					strbufResponse.append(" reverselogic=\"");
					strbufResponse.append((nFlags & CDataDef.nReverseLogic) != 0 ? "true" : "false");
					strbufResponse.append("\"");
					strbufResponse.append(" autoreset=\"");
					strbufResponse.append((nFlags & CDataDef.nAutoReset) != 0 ? "true" : "false");
					strbufResponse.append("\"");
				}
				strbufResponse.append("/>\n");
			}
       	}
		catch(DataBaseException e) {
			e.printStackTrace();
		}
	    strbufResponse.append("</response>");
    	return strbufResponse.toString();   	
    }
}
