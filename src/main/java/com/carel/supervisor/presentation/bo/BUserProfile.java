package com.carel.supervisor.presentation.bo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.base.conversion.DateUtils;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.profiling.IProfiler;
import com.carel.supervisor.base.profiling.ProfilingMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.ldap.DBProfiler;
import com.carel.supervisor.presentation.ldap.FunctionalityHelper;
import com.carel.supervisor.presentation.menu.MenuAction;
import com.carel.supervisor.presentation.menu.MenuSection;
import com.carel.supervisor.presentation.menu.MenuTab;
import com.carel.supervisor.presentation.menu.MenuVoce;
import com.carel.supervisor.presentation.menu.TabObj;
import com.carel.supervisor.presentation.menu.configuration.MenuActionMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuConfigMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuTabMgr;
import com.carel.supervisor.presentation.profile.ProfileMaps;
import com.carel.supervisor.presentation.profile.ProfileMapsBeanList;
import com.carel.supervisor.presentation.session.UserSession;


public class BUserProfile extends BoMaster
{
    private static final int REFRESH_TIME = -1;
    private static final int REMOVE_USER = 0;
    private static final int ADD_USER = 1;
    private static final int MODIFY_USER = 2;
    private static final int REMOVE_PROFILE = 3;
    private static final int ADD_PROFILE = 4;
    private static final int MODIFY_PROFILE = 5;
    private static final int UNBLOCK = 6;
    public static final int FUNCTION_NUMBER = 12;
    private static final String INPUT_COMMAND = "cmd";
    
    public static final String POLICY = "user_accountpolicy";
    public static final String STANDARD = "standard";
    public static final String STRICT = "strict";
    
    public static final String LOGINRETRY = "user_loginretries";
    public static final String EXPIRATIONDATE = "user_expirationdate";
    public static final String REMOTESUPPORT = "user_remotesupport";

    public static final String HIDDENUSER = "Ruser";

    /*private static final String INPUT_USER = "user";
    private static final String INPUT_USER_TO_MODIFY = "user_to_modify";
    private static final String INPUT_PROFILE = "profile";
    private static final String INPUT_PASSWORD = "password";
    private static final String PROFILE_DESCRIPTION = "desc";*/
    public BUserProfile(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Map initializeRefresh()
    {
        Map map = new HashMap();

        return map;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.setProperty("tab1name", "initialize();resizeTableUsers();");
        p.setProperty("tab2name", "initialize();resizeTableUsersPriv();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.setProperty("tab1name", "ldap.js;keyboard.js;");
        p.setProperty("tab2name", "acl.js;keyboard.js;../arch/FileDialog.js;");

        return p;
    }
    
    protected Properties initializeDocType()
    {
		Properties p = new Properties();
		p.put("tab2name", DOCTYPE_STRICT);
		return p;
    }
    public static String getProfileDescr(int profile_id) throws DataBaseException
    {
    	String sql = "SELECT code FROM profilelist where idprofile = ? ";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(profile_id)});
    	
    	 if (rs.size() == 1)
         {
             return rs.get(0).get("code").toString();
         }
         else
         {
             return "";
         }
    	
    }
    
    boolean existProfile(String profile_code) throws DataBaseException
    {
        String sql = "SELECT 1 FROM profilelist where LOWER(code) = ? ";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { profile_code });

        if (rs.size() == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    boolean existProfile(String profile_code, Integer idprofile)
        throws DataBaseException
    {
        String sql = "SELECT 1 FROM profilelist where LOWER(code) = ? and idprofile != ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { profile_code, idprofile });

        if (rs.size() == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public String executeDataAction(UserSession us, String tabName,
    	    Properties prop) throws Exception
    {
    	StringBuffer xmlresponse = new StringBuffer("<response>");
    	String action = prop.getProperty("action_type");
    	
    	if (action.equalsIgnoreCase("load_profile"))
    	{
    		String id = prop.getProperty("idprofile");
    		//ProfileBean pb = ProfileBeanList.retrieveProfile(Integer.parseInt(id),us.getIdSite());
    		ProfileMapsBeanList profileMapsBeanList = new ProfileMapsBeanList();
            profileMapsBeanList.load(Integer.parseInt(id));
            ProfileMaps profileMaps = new ProfileMaps(profileMapsBeanList);
            
            FunctionalityHelper.init();
            
        	MenuSection[] listSection = MenuConfigMgr.getInstance().getMenuSection();
        	
        	String menu = "";
        	
        	//add "global" (not in configuration.xml)
        	MenuVoce global_voce = new MenuVoce("grpview","nop&folder=grpview&bo=BGrpView&type=menu",-1);
        	xmlresponse = insertResponse(global_voce.getName(), xmlresponse, profileMaps);
        	
        	for(int i=0; i<listSection.length; i++)
    		{
        		MenuSection mn = listSection[i];
        		for (int j=0;j<mn.getListVoci().length;j++)
        		{
        			MenuVoce section = mn.getListVoci()[j];
        			menu = section.getName();
        			
        			xmlresponse = insertResponse(menu, xmlresponse, profileMaps);
        			
        			if (FunctionalityHelper.hasChild(menu)!=null)
        			{
        				menu = FunctionalityHelper.hasChild(menu).split(";")[0];
        				xmlresponse = insertResponse(menu, xmlresponse, profileMaps);
        			}
        		}
    		}
        	
        	//xmlresponse.append("<pf filter='"+pb.getStatus()+"'></pf>");
    	}
    	else if(action.equalsIgnoreCase("export_profile")){
    		String id = prop.getProperty("idprofile");
    		
    		String status = getProfileStates(id);
    		boolean nomenu = getProfileNoMenu(id);
    		
    		String profileName = getProfileName(id);
    		ProfileMapsBeanList profileMapsBeanList = new ProfileMapsBeanList();
            profileMapsBeanList.load(Integer.parseInt(id));
            ProfileMaps profileMaps = new ProfileMaps(profileMapsBeanList);
            FunctionalityHelper.init();
        	MenuSection[] listSection = MenuConfigMgr.getInstance().getMenuSection();
        	String menu = "";
        	StringBuffer profexpXML = new StringBuffer();
        	//add "global" (not in configuration.xml)
        	MenuVoce global_voce = new MenuVoce("grpview","nop&folder=grpview&bo=BGrpView&type=menu",-1);
//        	profexpXML = insertResponse(global_voce.getName(), profexpXML, profileMaps);
        	profexpXML = insertResponseForXmlExport(global_voce.getName(), profexpXML, profileMaps);
        	
        	for(int i=0; i<listSection.length; i++)
    		{
        		MenuSection mn = listSection[i];
        		for (int j=0;j<mn.getListVoci().length;j++)
        		{
        			MenuVoce section = mn.getListVoci()[j];
        			menu = section.getName();
//        			profexpXML = insertResponse(menu, profexpXML, profileMaps);
        			profexpXML = insertResponseForXmlExport(menu, profexpXML, profileMaps);
        			if (FunctionalityHelper.hasChild(menu)!=null)
        			{
        				menu = FunctionalityHelper.hasChild(menu).split(";")[0];
//        				profexpXML = insertResponse(menu, profexpXML, profileMaps);
        				profexpXML = insertResponseForXmlExport(menu, profexpXML, profileMaps);
        			}
        		}
    		}
        	String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
            String fixver = BaseConfig.getProductInfo("fix");
            fixver = (fixver == null || fixver.equals(""))?"":"."+fixver;
            String projectHead = "<Project pvproversion=\""+BaseConfig.getProductInfo("version")+fixver+
            		"\" docdate=\""+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date())+
            		"\" >\n";
            
            
            projectHead += "<profname profcode = '"+profileName+"' status ='"+status+"' nomenu = '"+nomenu+"'>\n";
            
            
            String projectTail = "</profname>\n</Project>\n";
            String local = prop.getProperty("local");
            String file = "";
            String idprofile = prop.getProperty("idprofile");
            if("true".equalsIgnoreCase(local))
            {
            	String path = prop.getProperty("path");
            	file = path;
            }
            else
            {
            	file = BaseConfig.getCarelPath() + BaseConfig.getTemporaryFolder() + File.separator + 
            			profileName+"_"+DateUtils.date2String(new Date(), "yyyyMMddhhmmss")+".PCFG";
            }
            xmlresponse.append("<file><![CDATA["+file+"]]></file>");
            try{
    	        BufferedWriter XMLwriter = null;
    	        XMLwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
    	      	XMLwriter.write(xmlHead);
    	      	XMLwriter.write(projectHead);
    	      	XMLwriter.write(profexpXML.toString());
    			XMLwriter.write(projectTail);
    			XMLwriter.flush();
    			XMLwriter.close();
            }
    		catch(Exception e)
            {
    			xmlresponse.append("<file><![CDATA[ERROR]]></file>");
            }
    	}
    	else if( action.equals("clean_profile") ) {
    		String path = prop.getProperty("path");
    		if( path != null && !path.isEmpty() ) try {
    			File file = new File(path);
    			file.delete();
    		}
			catch (Exception e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			} 
			us.removeProperty("overwriteProf2");
    	}
    	xmlresponse.append("</response>");
    	return xmlresponse.toString();
    }
    
    private String getProfileName(String idprofile) throws Exception{
		 String sql = "select code from profilelist where idprofile = ? order by code";
	     RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
	             new Object[] { new Integer(idprofile) });
	     Record record = recordset.get(0);
    	 return record.get(0).toString();
    }
    
    private String getProfileStates(String idprofile) throws Exception{
		 String sql = "select status from profilelist where idprofile = ? order by code";
	     RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
	             new Object[] { new Integer(idprofile) });
	     Record record = recordset.get(0);
   	 return record.get(0).toString();
   }
    
    private boolean getProfileNoMenu(String idprofile) throws Exception{
		 String sql = "select nomenu from profilelist where idprofile = ? order by code";
	     RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql,
	             new Object[] { new Integer(idprofile) });
	     Record record = recordset.get(0);
  	 return (Boolean)record.get(0) ;
  }
    
    private StringBuffer insertResponse(String menu, StringBuffer xmlresponse,ProfileMaps profileMaps)
    {
    	String tabname = "";
    	TabObj tab = null;
    	MenuAction ma = null;
    	MenuTab mt = null;
    	
    	if (FunctionalityHelper.isVisible(menu))
		{
			if (!profileMaps.isMenuActive(menu))
				xmlresponse.append("<menu menu='"+menu+"'></menu>\n");
			else
			{
				mt = MenuTabMgr.getInstance().getTabMenuFor(menu);
				for (int t=0;t<mt.getNumTab();t++)
		    	{
					tab = mt.getTab(t);
					tabname = tab.getIdTab();
		    		if (!profileMaps.isTabActive(menu, tabname))
		    		{
		    			xmlresponse.append("<tab tab='"+menu+"_"+t+"'></tab>\n");
		    		}
		    		else
		    		{
			    		ma = MenuActionMgr.getInstance().getActMenuFor(menu, tabname);
			    		if (ma!=null)
			    		{
    			    		for (int b=0;b<ma.getNumAct();b++)
    			    		{
    			    			if (!profileMaps.isButtonActive(menu, tabname, ma.getAct(b).getName()))
    			    			{
    			    				xmlresponse.append("<but but='"+menu+"_"+"tab"+t+"name"+"_"+b+"'></but>\n");
    			    			}
    			    		}
			    		}
		    		}
		    	}
			}
		}
    	return xmlresponse;
    }

    private StringBuffer insertResponseForXmlExport(String menu, StringBuffer xmlresponse,ProfileMaps profileMaps)
    {
    	String tabname = "";
    	TabObj tab = null;
    	MenuAction ma = null;
    	MenuTab mt = null;
    	
    	if (FunctionalityHelper.isVisible(menu))
		{
			if (!profileMaps.isMenuActive(menu))
				xmlresponse.append("<menu menu='"+menu+"'></menu>\n");
			else
			{
				mt = MenuTabMgr.getInstance().getTabMenuFor(menu);
				for (int t=0;t<mt.getNumTab();t++)
		    	{
					tab = mt.getTab(t);
					tabname = tab.getIdTab();
		    		if (!profileMaps.isTabActive(menu, tabname))
		    		{
		    			xmlresponse.append("<tab tab='"+menu+"_"+tabname+"'></tab>\n");
		    		}
		    		else
		    		{
			    		ma = MenuActionMgr.getInstance().getActMenuFor(menu, tabname);
			    		if (ma!=null)
			    		{
    			    		for (int b=0;b<ma.getNumAct();b++)
    			    		{
    			    			if (!profileMaps.isButtonActive(menu, tabname, ma.getAct(b).getName()))
    			    			{
    			    				xmlresponse.append("<but but='"+menu+"_"+tabname+"_"+ma.getAct(b).getName()+"'></but>\n");
    			    			}
    			    		}
			    		}
		    		}
		    	}
			}
		}
    	return xmlresponse;
    }
    
    private void unblock(Properties prop)
    	throws DataBaseException
    {
    	String user = prop.getProperty(IProfiler.INPUT_USER_TO_MODIFY);
    	DBProfiler dbProfiler = new DBProfiler();
    	dbProfiler.updateBadlogon(user, 0);
    }
    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
        prop.put("idSite", new Integer(us.getIdSite()));

        IProfiler profiler = ProfilingMgr.getInstance().getProfiler();

        boolean insertUser = true;
        boolean deleteProfile = true;
        boolean modifyUser = true;
        boolean insertProfile = true;
        
        prop.setProperty("userlogged",us.getUserName());

        int cmdType = new Integer(prop.getProperty(INPUT_COMMAND)).intValue();

        switch (cmdType)
        {
        case REMOVE_USER:
            profiler.removeUser(prop);

            break;

        case ADD_USER:
            insertUser = profiler.addUser(prop);

            break;

        case MODIFY_USER:
            modifyUser = profiler.modifyUser(prop);

            break;

        case UNBLOCK:
        	unblock(prop);
        	break;
        	
        case REMOVE_PROFILE:
            deleteProfile = profiler.removeProfile(prop);

            break;

        case ADD_PROFILE:
        {
            String profileCode = prop.getProperty(IProfiler.PROFILE_DESCRIPTION);

            if (existProfile(profileCode.toLowerCase()))
            {
                insertProfile = false;
            }
            else
            {
                profiler.addProfile(prop);
                //log nuovo profilo
               
                EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(), "Config",
                        "PROF002", prop.getProperty(IProfiler.PROFILE_DESCRIPTION) );
            }

            break;
        }

        case MODIFY_PROFILE:
        {
            String profileCode = prop.getProperty(IProfiler.PROFILE_DESCRIPTION);
            Integer idprofile = new Integer(prop.getProperty("rowselected"));

            if (existProfile(profileCode.toLowerCase(), idprofile))
            {
                insertProfile = false;
            }
            else
            {
                profiler.modifyProfile(prop);
                
                //log modifica profilo
                EventMgr.getInstance().info(new Integer(us.getIdSite()), us.getUserName(), "Config",
                        "PROF006", prop.getProperty(IProfiler.PROFILE_DESCRIPTION) );
            }

            break;
        }
        } //switch

        if (insertUser == false)
        {
            String userName = prop.getProperty("user");
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,
                    "SELECT 1 FROM cfusers WHERE LOWER(username)=?",
                    new Object[] { userName.toLowerCase() });

            if (rs.size() == 1)
            {
                us.setProperty("doubleuser", "yes");
            }

            else
            {
                us.setProperty("carel_exist", "yes");
            }
        }

        if (modifyUser == false)
        {
            us.setProperty("carel_exist", "yes");
        }

        if (deleteProfile == false)
        {
            us.setProperty("usedprofile", "yes");
        }

        if (insertProfile == false)
        {
            us.setProperty("doubleprofile", "yes");
        }
    } //executePostAction
} //Class BLDAP
