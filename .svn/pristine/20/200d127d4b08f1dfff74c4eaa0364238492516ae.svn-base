package com.carel.supervisor.presentation.bo.helper;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.ide.dc.xmlDAO.ImportException;
import com.carel.supervisor.ide.dc.xmlDAO.XmlDAO;
import com.carel.supervisor.presentation.bean.ProfileBeanList;
import com.carel.supervisor.presentation.bean.UserBean;
import com.carel.supervisor.presentation.ldap.FunctionsDB;
import com.carel.supervisor.presentation.menu.MenuAction;
import com.carel.supervisor.presentation.menu.MenuTab;
import com.carel.supervisor.presentation.menu.TabObj;
import com.carel.supervisor.presentation.menu.configuration.MenuActionMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuTabMgr;

public class ProfileConfigImport extends XmlDAO{
	
	public static final String PROFILENAME = "profname";
	public static final String PROF_CODE = "profcode";
	public static final String PROF_MENU = "menu";
	public static final String PROF_TAB = "tab";
	public static final String PROF_BUT = "but";
	public static final String PROJECT = "Project";
	
	
	private Logger logger;
	private String filename = "";
	
	public ProfileConfigImport()
	{
		logger = LoggerMgr.getLogger(this.getClass());
	}
	public int loadBeanByXML(int idsite,Properties prop,String xmlPath,String lang,ProfileBeanList profile_list,boolean overwrite) 
	{
		int result = 0;
		int idprofile = 0;
		Integer iddelprofile = null;
		try{
			ProfileBeanList profiles = new ProfileBeanList(idsite,false);
	    	// Initializes common resources
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();	
			File inputXML = new File(xmlPath);
			Document doc = parser.parse(inputXML); 
			NodeList nodes = doc.getElementsByTagName(PROFILENAME);
			
			Node profilenode = nodes.item(0);
			String code = getAttrValByName(profilenode, PROF_CODE);
			String status = getAttrValByName(profilenode, "status");
			if(status == null){
				status = "1--";
			}
			boolean nomenuBol = false ;
			String nomenuStr = getAttrValByName(profilenode, "nomenu");
			if ( nomenuStr != null && nomenuStr.equalsIgnoreCase("true")){
				nomenuBol = true;
			}
			NodeList projectnode = doc.getElementsByTagName(PROJECT);
			Node vertionNode = projectnode.item(0);
			String xmlversion = getAttrValByName(vertionNode, "pvproversion");
			
			String fixver = BaseConfig.getProductInfo("fix");
            fixver = (fixver == null || fixver.equals(""))?"":"."+fixver;
            String version = BaseConfig.getProductInfo("version")+fixver;
            
            int vresult = compareVersion(version,xmlversion);
            
            if(vresult == -1){
	            return 4; // higher version forbit.
	        }
            if(vresult == 1){
	            result = 10; // to alert the xml version is lower.
	        }
            if(vresult == 2){
	            return 11; // to alert the xml version  is error.
	        }
            //ArrayList<UserBean> userList = null;
			for(int i=0;i<profile_list.size();i++){
				if(code.equals("System Administrator")){
					return 3;  // import admin forbit.
				}
				// to confirm overwrite or not, and  after confirm 
				if( profile_list.getProfile(i).getCode().equals(code) ){
					if(overwrite){
						iddelprofile = profile_list.getProfile(i).getIdprofile();
					}else{
						return 1;  // profiler is the same name, to confirm overwrite or not.
					}
				}
			}
			
//			idprofile = ProfileBeanList.addProfile( 1, "TEMP"+code, "1--", false);
			// longbow 2011-6-3  Bug 8201] Import/export user profiles mechanism does not manage the 'show menu' and the 'parameters filter' settings
			idprofile = ProfileBeanList.addProfile( 1, "TEMP"+code, status, nomenuBol);    
			loadProfileMaps(profilenode,idprofile);
			if(iddelprofile != null)//replace the old one
			{
				ProfileBeanList.deleteProfile(iddelprofile, idsite);  // to overwrite the same name profile.
				ProfileBeanList.updateProfileId(idprofile, iddelprofile,code);//use the old id
			}
			else//new one
			{
				ProfileBeanList.updateProfileId(idprofile, idprofile,code);//use the old id
				FunctionsDB.newLocalProfile(new Integer(idprofile));
			}
		}
		catch(Exception ex)
        {
        	logger.error("Error during profile import: "+ex.toString());
			ex.printStackTrace();
        	// delete corrupted profile from db
        	if( idprofile > 0 ) try {
        		ProfileBeanList.deleteProfile(idprofile, 1);
        	}
        	catch(DataBaseException e) {
        		logger.error(e.toString());
        	}
        	return 2;
        }
		return result;
	}
	
	private void changeUsersidprofile(ArrayList<UserBean> userList,int idprofile) {
		String sql_update = "UPDATE cfusers SET idprofile=? WHERE username=?";
		List<Object[]> ins = new ArrayList<Object[]>();
		for(int i=0;i<userList.size();i++){
			UserBean user = userList.get(i);
			ins.add(new Object[]{idprofile,user.getUsername() });
		}
		try {
			DatabaseMgr.getInstance().executeMultiStatement(null, sql_update, ins);
		} catch (DataBaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	private ArrayList<UserBean> getUsersByprofileid(int idprofile, int idsite){
//		ArrayList<UserBean> userlist = new ArrayList<UserBean>();
//		
//		String sql = "select * from cfusers where idprofile=?";
//        RecordSet rs;
//		try {
//			rs = DatabaseMgr.getInstance().executeQuery(null, sql,
//			        new Object[] { new Integer(idprofile) });
//			 for(int i=0;i<rs.size();i++ ){
//		        	userlist.add( new UserBean( rs.get(i) ) );
//		        }
//		} catch (DataBaseException e) {
//			e.printStackTrace();
//		}
//       
//
//       
//		return userlist;
//	}
	
	private int compareVersion(String version, String xmlversion) {
		
		String[] v = version.split("\\.");
		String[] vxml = xmlversion.split("\\.");
		int lengthOfShorter = v.length<=vxml.length ? v.length : vxml.length;
		for(int i=0;i<lengthOfShorter;i++){
			try{
				if(Integer.parseInt(v[i])!=Integer.parseInt(vxml[i]) ){
					return Integer.parseInt(v[i]) < Integer.parseInt(vxml[i])? -1 : 1;
				}
			}catch (NumberFormatException exp){
				return 2;
			}
		}
		if(v.length > lengthOfShorter){
			return -1;
		}
		if(vxml.length > lengthOfShorter){
			return 1;
		}

		return 0;
	}
	private void loadProfileMaps(Node profilenode,int idprofile)
	throws ImportException
	{
		try
		{
			String sql_insert = "insert into profilemaps values (?,?,?,?,?,?,?,?)";
			List<Object[]> ins = new ArrayList<Object[]>();
			Integer mapcode = new Integer(0);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			
			NodeList menus = ((Element)profilenode).getElementsByTagName(PROF_MENU);
			if(menus != null && menus.getLength() != 0){
				for(int i=0;i<menus.getLength();i++)
				{
					Node node = menus.item(i);
					String menu = getAttrValByName(node, PROF_MENU);
					ins.add(new Object[]{idprofile,mapcode,menu,"","","","",now});
				}
			}
			NodeList tabs = ((Element)profilenode).getElementsByTagName(PROF_TAB);
			if(tabs != null && tabs.getLength() != 0){
				for(int i=0;i<tabs.getLength();i++)
				{
					Node node = tabs.item(i);
					String code = getAttrValByName(node, PROF_TAB);
					String menu =code.split("_")[0];
					String tabname = code.split("_")[1];
					ins.add(new Object[]{idprofile,mapcode,menu,tabname,"","","",now});
				}
			}
			NodeList buts = ((Element)profilenode).getElementsByTagName(PROF_BUT);
			if(buts != null && buts.getLength() != 0){
				for(int i=0;i<buts.getLength();i++)
				{
					Node node = buts.item(i);
					String code = getAttrValByName(node, PROF_BUT);
					String menu =code.split("_")[0];
					String tabname = code.split("_")[1];
					String button = code.split("_")[2];
					MenuAction ma = MenuActionMgr.getInstance().getActMenuFor(menu, tabname);
					ins.add(new Object[]{idprofile,mapcode,menu,tabname,button,"","",now});
				}
			}
			DatabaseMgr.getInstance().executeMultiStatement(null, sql_insert, ins);
		}
		catch(Exception ex)
		{
			throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.ALARM);
		}
	}
	
	/*
	 * private void loadProfileMaps(Node profilenode,int idprofile)
	throws ImportException
	{
		try
		{
			String sql_insert = "insert into profilemaps values (?,?,?,?,?,?,?,?)";
			List<Object[]> ins = new ArrayList<Object[]>();
			Integer mapcode = new Integer(0);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			
			NodeList menus = ((Element)profilenode).getElementsByTagName(PROF_MENU);
			if(menus != null && menus.getLength() != 0){
				for(int i=0;i<menus.getLength();i++)
				{
					Node node = menus.item(i);
					String menu = getAttrValByName(node, PROF_MENU);
					ins.add(new Object[]{idprofile,mapcode,menu,"","","","",now});
				}
			}
			NodeList tabs = ((Element)profilenode).getElementsByTagName(PROF_TAB);
			if(tabs != null && tabs.getLength() != 0){
				for(int i=0;i<tabs.getLength();i++)
				{
					Node node = tabs.item(i);
					String code = getAttrValByName(node, PROF_TAB);
					String menu =code.split("_")[0];
					String t = code.split("_")[1];
					MenuTab mt = MenuTabMgr.getInstance().getTabMenuFor(menu);
					TabObj tab = mt.getTab(Integer.parseInt(t));
					String tabname = tab.getIdTab();
					ins.add(new Object[]{idprofile,mapcode,menu,tabname,"","","",now});
				}
			}
			
			NodeList buts = ((Element)profilenode).getElementsByTagName(PROF_BUT);
			if(buts != null && buts.getLength() != 0){
				for(int i=0;i<buts.getLength();i++)
				{
					Node node = buts.item(i);
					String code = getAttrValByName(node, PROF_BUT);
					String menu =code.split("_")[0];
					String t = code.split("_")[1].substring(3,4);
					MenuTab mt = MenuTabMgr.getInstance().getTabMenuFor(menu);
					TabObj tab = mt.getTab(Integer.parseInt(t));
					String tabname = tab.getIdTab();
					int b = Integer.parseInt(code.split("_")[2]);
					MenuAction ma = MenuActionMgr.getInstance().getActMenuFor(menu, tabname);
					ins.add(new Object[]{idprofile,mapcode,menu,tabname,ma.getAct(b).getName(),"","",now});
				}
			}
			DatabaseMgr.getInstance().executeMultiStatement(null, sql_insert, ins);
		}
		catch(Exception ex)
		{
			throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.ALARM);
		}
	}
	 */
	
	
	
	private void DeviceRead(Node profilenode)
	throws ImportException
{
	try
	{
		String devmdlcode = getAttrValByName(profilenode, Propagate.DEVICE_CODE);
	}
	catch(Exception ex)
	{
		throw new ImportException("xmlerr",ex,"XML format error. Tag: "+Propagate.PROFILENAME);
	}
}

	@Override
	protected void unmarshal(Document doc, XPathFactory xfactory)
			throws ImportException {
		// TODO Auto-generated method stub
		
	}
	public void setFilename(String filename) {
		// TODO Auto-generated method stub
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}

}
