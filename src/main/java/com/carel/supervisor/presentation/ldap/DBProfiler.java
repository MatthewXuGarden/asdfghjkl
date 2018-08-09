package com.carel.supervisor.presentation.ldap;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import supervisor.SRVLAccount;

import com.carel.supervisor.base.config.IProductInfo;
import com.carel.supervisor.base.config.InitializableBase;
import com.carel.supervisor.base.config.InvalidConfigurationException;
import com.carel.supervisor.base.config.ProductInfoMgr;
import com.carel.supervisor.base.crypter.Crypter;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.base.profiling.IProfiler;
import com.carel.supervisor.base.profiling.ProfileException;
import com.carel.supervisor.base.profiling.SectionProfile;
import com.carel.supervisor.base.profiling.UserCredential;
import com.carel.supervisor.base.profiling.UserProfile;
import com.carel.supervisor.base.xml.XMLNode;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.presentation.bean.GroupBean;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.bean.ProfileBeanList;
import com.carel.supervisor.presentation.bo.BUserProfile;
import com.carel.supervisor.presentation.defaultconf.Defaulter;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.menu.MenuAction;
import com.carel.supervisor.presentation.menu.MenuSection;
import com.carel.supervisor.presentation.menu.MenuTab;
import com.carel.supervisor.presentation.menu.MenuVoce;
import com.carel.supervisor.presentation.menu.TabObj;
import com.carel.supervisor.presentation.menu.configuration.MenuActionMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuConfigMgr;
import com.carel.supervisor.presentation.menu.configuration.MenuTabMgr;
import com.carel.supervisor.presentation.tabmenu.TabList;


public class DBProfiler extends InitializableBase implements IProfiler
{
    public static final String CAREL_PREFIX = "pvp_";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String CRYPTING_METHOD = "cryptingMethod";
    private static final String USERNAME_LABELFIELD = "username";
    public static final String PASSWORD_LABELFIELD = "password";
    private static final String BADLOGONS_LABELFIELD = "badlogons";
    private static final String LASTPWDCHANGE_LABELFIELD = "lastpwdchange";
    public static final String USERPROFILE_LABELFIELD = "idprofile";
    public static final String PROFILE_EXCEPTION_BLOCKED = "blocked";
    public static final String PROFILE_EXCEPTION_EXPIRED = "expired";
    public static final String PROFILE_EXCEPTION_SAMEPWD = "samepwd";
    public static final String PROFILE_EXCEPTION_HIDDENNOTALLOWED = "hiddennotallowed";
    
    private static String RemoteSystem = "RemoteSystem";
    
    private boolean initialized = false;
    private String cryptingMethod = null;
    private String tokenValidity = "";

    public UserProfile getUserProfile(UserCredential userCredential)
        throws ProfileException, Exception
    {
    	RecordSet rs = checkUser(userCredential.getUserName(),userCredential.getUserPassword());
        
    	UserProfile userProfile = new UserProfile();
        SectionProfile sectionProfile = new SectionProfile();
        userProfile.addSection(IProfiler.USER_SECTION, sectionProfile);

        Record record = rs.get(0);
        sectionProfile.setValue(USERNAME_LABELFIELD,
            (String) record.get(USERNAME_LABELFIELD));
        sectionProfile.setValue(USERPROFILE_LABELFIELD,
            ((Integer) record.get(USERPROFILE_LABELFIELD)).toString());

        return userProfile;
    } //getUserProfile

    public void init(XMLNode xmlStatic) throws InvalidConfigurationException
    {
        if (!initialized)
        {
            Properties properties = retrieveProperties(xmlStatic, NAME, VALUE,
                    "BSSE0002");
            cryptingMethod = retrieveAttribute(properties, CRYPTING_METHOD,
                    "BSSE0002");
            initialized = true;
        } //if
    } //init

    public String getUserNameLabelField()
    {
        return USERNAME_LABELFIELD;
    } //getUserNameLabelField

    public String getProfileNameLabelField()
    {
        return USERPROFILE_LABELFIELD;
    } //getProfileNameLabelField

    public ArrayList[] getUserInformation() throws Exception
    {
		//2009-12-23, add badlogons , by Kevin
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,
            "SELECT iduser,username,password,idprofile,badlogons FROM cfusers where idprofile!=(-4) and iduser >= 0 order by upper (username)");
        ArrayList user = new ArrayList();
        ArrayList profile = new ArrayList();
        ArrayList blocked = new ArrayList();

        IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
		String policytype = product.get(BUserProfile.POLICY);
		int logonretry = 0;
		try
		{
			logonretry = Integer.parseInt(product.get(BUserProfile.LOGINRETRY));
		}
		catch(Exception ex)
		{}
        for (int i = 0; i < rs.size(); i++)
        {
            user.add(UtilBean.trim(rs.get(i).get(USERNAME_LABELFIELD)));
            profile.add(rs.get(i).get(USERPROFILE_LABELFIELD).toString());
            Record record = rs.get(i);
            try
            {
            	checkBlocked(record);
            	blocked.add("FALSE");
            }
            catch(ProfileException pe)
            {
            	blocked.add("TRUE");
            }
        } //for

        return new ArrayList[] { user, profile,blocked };
    }
    public void removeUser(Properties properties) throws Exception
    {
    	//2009-12-23, add account policy, Kevin
        updateAccountPolicy(properties);
        //--end
        //2009-12-30, add remote support, Kevin
        updateRemoteSupport(properties);
        //-end
        
        String userName = ((String) properties.getProperty(INPUT_ROW_SELECTED)).split(
                ";")[0];
        String profile = ((String) properties.getProperty(INPUT_ROW_SELECTED)).split(
                ";")[1];

        if ((userName != null) && (!userName.equals("")))
        {
            DatabaseMgr.getInstance().executeStatement(null,
                "DELETE FROM cfusers WHERE username=?",
                new Object[] { userName });
        }

        //log eliminazione utente
        String user = properties.getProperty("userlogged");

        if (profile.equalsIgnoreCase("-5"))
        {
            userName = userName.substring(4, userName.length());
        }

        EventMgr.getInstance().info(new Integer(1), user, "Config", "PROF003",
            userName);
    } //removeUser

    public boolean addUser(Properties properties) throws Exception
    {
    	//2009-12-23, add account policy, Kevin
        updateAccountPolicy(properties);
        //--end
        String userName = properties.getProperty(INPUT_USER);
        //2009-12-30, add remote support, Kevin
        updateRemoteSupport(properties);
        //-end
        
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,
                "SELECT 1 FROM cfusers WHERE LOWER(username)=?",
                new Object[] { userName.toLowerCase() });

        if (rs.size() == 1)
        {
            return false;
        }

        String password = Crypter.encryptMD(properties.getProperty(
                    INPUT_PASSWORD), cryptingMethod);
        Integer profile = new Integer(properties.getProperty(INPUT_PROFILE));

        if (profile.intValue() == -5) //se utente carel ci metto prefisso per non far entrare utilizzatori con profilo carel
        {
            userName = CAREL_PREFIX + userName;
            rs = DatabaseMgr.getInstance().executeQuery(null,
                    "SELECT 1 FROM cfusers WHERE idprofile=?",
                    new Object[] { new Integer(-5) });

            if (rs.size() == 1)
            {
                return false;
            }
        }

        SeqMgr o = SeqMgr.getInstance();
        DatabaseMgr.getInstance().executeStatement(null,
            "INSERT INTO cfusers VALUES(?,?,?,?)",
            new Object[]
            {
                o.next(null, "cfusers", "iduser"), userName, password, profile
            });

        //log nuovo utente
        String user = properties.getProperty("userlogged");

        if (profile.intValue() == -5)
        {
            userName = userName.substring(4, userName.length());
        }

        EventMgr.getInstance().info(new Integer(1), user, "Config", "PROF001",
            userName);

        return true;
    } //addUser
    public String encryptPassword(String password)
    	throws Exception
    {
    	return Crypter.encryptMD(password, cryptingMethod);
    }
    public boolean modifyUser(Properties properties) throws Exception
    {
    	//2009-12-23, add account policy, Kevin
        updateAccountPolicy(properties);
        //--end
        //2009-12-30, add remote support, Kevin
        updateRemoteSupport(properties);
        //-end
        String userName = properties.getProperty(INPUT_USER_TO_MODIFY);
        //2009-12-23, add account policy, Kevin
        //when the user click "save", maybe he/she only want to save policy
        if(userName.equals(""))
        {
        	return true;
        }
        //--end
        String strPassword = properties.getProperty(INPUT_PASSWORD);
        String password = Crypter.encryptMD(strPassword, cryptingMethod);
        Integer profile = new Integer((properties.getProperty(INPUT_PROFILE) != null)
                ? properties.getProperty(INPUT_PROFILE) : "0");

        if (profile.intValue() == -5) //se utente carel ci metto prefisso per non far entrare utilizzatori con profilo carel
        {
            String new_userName = CAREL_PREFIX + userName;
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,
                    "SELECT 1 FROM cfusers WHERE idprofile=?",
                    new Object[] { new Integer(-5) });

            if (rs.size() == 1)
            {
                return false;
            }

            if( strPassword.isEmpty() )
            	DatabaseMgr.getInstance().executeStatement(null,
            			"UPDATE cfusers SET idprofile=?,username=? WHERE username=?",
            			new Object[] { profile, new_userName, userName });
            else
            	DatabaseMgr.getInstance().executeStatement(null,
            			"UPDATE cfusers SET password=?,idprofile=?,username=? WHERE username=?",
            			new Object[] { password, profile, new_userName, userName });
            this.updateBadlogon(new_userName, 0);
            return true;
        }
        Timestamp time = new Timestamp(System.currentTimeMillis());
        if( strPassword.isEmpty() )
        	DatabaseMgr.getInstance().executeStatement(null,
        			"UPDATE cfusers SET idprofile=?,lastpwdchange=? WHERE username=?",
        			new Object[] { profile, time, userName });
        else
        	DatabaseMgr.getInstance().executeStatement(null,
        			"UPDATE cfusers SET password=?,idprofile=?,lastpwdchange=? WHERE username=?",
        			new Object[] { password, profile, time, userName });

        this.updateBadlogon(userName, 0);
        String user = properties.getProperty("userlogged");
        
        //log modifica utente
        EventMgr.getInstance().info(new Integer(1), user, "Config",
                "PROF005", userName);
        
        return true;
    } //modifyUser
    //2009-12-23, add account policy, Kevin
    public void updateAccountPolicy(Properties prop)
	throws Exception
	{
    	String policyChanged = prop.getProperty("policychanged");
        if(policyChanged != null && policyChanged.equals("1"))
        {
			String policytype = prop.getProperty("policytype");
			String loginretry = null;
			String expirationdate = null;
			IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
			if(policytype.equals(BUserProfile.STANDARD))
			{
				product.set(BUserProfile.POLICY, policytype);
				product.set(BUserProfile.LOGINRETRY, "0");
				product.set(BUserProfile.EXPIRATIONDATE, "0");
			}
			else if(policytype.equals(BUserProfile.STRICT))
			{
				loginretry = prop.getProperty("logonretriesSel");
				expirationdate = prop.getProperty("expirationSel");
				product.set(BUserProfile.POLICY, policytype);
				product.set(BUserProfile.LOGINRETRY, loginretry);
				product.set(BUserProfile.EXPIRATIONDATE, expirationdate);
			}
        }
	}
    //end
    //2009-12-30, add remote support, Kevin
    private void updateRemoteSupport(Properties prop)
    	throws Exception
    {
    	String remotesupportChanged = prop.getProperty("remotesupportchanged");
    	if(remotesupportChanged != null && remotesupportChanged.equals("1"))
    	{
    		IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
    		String checked = prop.getProperty("remotesupport");
    		if(checked != null)
    		{
    			product.set(BUserProfile.REMOTESUPPORT, "1");
    		}
    		else
    		{
    			product.set(BUserProfile.REMOTESUPPORT, "0");
    		}
    			
    	}
    }
   
    public boolean removeProfile(Properties properties)
    throws Exception
	{
	    String s_idprofile = properties.getProperty("rowselected");
	    int idprofile = Integer.parseInt(s_idprofile);
	    int idsite = ((Integer) properties.get("idSite")).intValue();
	
	    boolean isProfileUsed = ProfileBeanList.isProfileUsed(idprofile);
	
	    if (isProfileUsed)
	    {
	        return false;
	    }
	    else
	    {
	        String tmp_descr = BUserProfile.getProfileDescr(idprofile);
	        FunctionsDB.removeLocalProfile(new Integer(idprofile));
	        ProfileBeanList.deleteProfile(idprofile, idsite);
	
	        //log rimozione profilo
	        String user = properties.getProperty("userlogged");
	        EventMgr.getInstance().info(new Integer(1), user, "Config",
	            "PROF004", tmp_descr);
	
	        return true;
	    }
	}

	public void addProfile(Properties properties) throws Exception
	{
		int idsite = ((Integer) properties.get("idSite")).intValue();
		String desc = properties.getProperty(PROFILE_DESCRIPTION);
		String lang = properties.getProperty("language");
		GroupListBean groups = new GroupListBean();
		String s_param = properties.getProperty("param_filter");
		//2010-5-21, by Kevin
		boolean nomenu = !("true".equalsIgnoreCase(properties.getProperty("menuvisible")));
		String s_group = "";

		GroupBean[] group = groups.retrieveAllGroupsNoGlobal(idsite, lang);
		int idgroup = -1;
		for (int j = 0; j < group.length; j++) {
			idgroup = group[j].getGroupId();
			if (properties.getProperty("g" + idgroup) == null) {
				// profilegroups: insert idprofile,site,idgroup
				s_group = s_group + idgroup + ";";
			}
		}

		if (!s_group.equals("")) {
			s_group = s_group.substring(0, s_group.length() - 1);
		}

		s_param = s_param + "--" + s_group; // parametri profilo
															// aree removed by Kevin
															// disab+gruppi
															// disab

		// aggiunta profilo
		int idprofile = ProfileBeanList.addProfile(idsite, desc, s_param, nomenu);
		FunctionsDB.newLocalProfile(new Integer(idprofile));
		for (int j = 0; j < group.length; j++) {
			idgroup = group[j].getGroupId();
			if (properties.getProperty("g" + idgroup) == null) {
				// profilegroups: insert idprofile,site,idgroup
				FunctionsDB.disableGroup(new Integer(idprofile), new Integer(idsite), new Integer(1), new Integer(idgroup));
			}
		}
		MenuSection[] listSection = MenuConfigMgr.getInstance().getMenuSection();
		String menu = "";
		String sql_insert = "insert into profilemaps values (?,?,?,?,?,?,?,?)";
		Integer code = new Integer(0);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		List<Object[]> ins = new ArrayList<Object[]>();
		
		//add "global" (not in configuration.xml)
    	MenuVoce global_voce = new MenuVoce("grpview","nop&folder=grpview&bo=BGrpView&type=menu",-1);
    	insertProfileMaps(properties, ins, global_voce.getName(), now, idprofile, code);
    	
		for (int i = 0; i < listSection.length; i++) {
			MenuSection mn = listSection[i];
			for (int j = 0; j < mn.getListVoci().length; j++) {
				MenuVoce section = mn.getListVoci()[j];
				menu = section.getName();
				insertProfileMaps(properties, ins, menu, now, idprofile, code);
				if (FunctionalityHelper.hasChild(menu) != null) {
					menu = FunctionalityHelper.hasChild(menu).split(";")[0];
					insertProfileMaps(properties, ins, menu, now, idprofile, code);
				}
			}
		}
		if (ins != null && ins.size() != 0) {
			DatabaseMgr.getInstance().executeMultiStatement(null, sql_insert, ins);
		}

		try {
			Defaulter.insertDefaultForNewProfile(1, idprofile);
		} catch (Exception e) {
		}
	}

	public void modifyProfile(Properties properties) throws Exception
	{
		String desc = properties.getProperty(PROFILE_DESCRIPTION);
		int idprofile = Integer.parseInt(properties.getProperty("rowselected"));
		String lang = properties.getProperty("language");
		int idsite = ((Integer) properties.get("idSite")).intValue();

		IProductInfo productInfo = ProductInfoMgr.getInstance().getProductInfo();
		String s_param = properties.getProperty("param_filter");
		//2010-5-21, by Kevin
		boolean nomenu = !("true".equalsIgnoreCase(properties.getProperty("menuvisible")));
		GroupListBean groups = new GroupListBean();
		String s_group = "";
		GroupBean[] group = groups.retrieveAllGroupsNoGlobal(idsite, lang);
		int idgroup = -1;

		for (int j = 0; j < group.length; j++) {
			idgroup = group[j].getGroupId();
			if (properties.getProperty("g" + idgroup) == null) {
				// profilegroups: insert idprofile,site,idgroup
				s_group = s_group + idgroup + ";";
			}
		}
		if (!s_group.equals("")) {
			s_group = s_group.substring(0, s_group.length() - 1);
		}
		s_param = s_param + "--" + s_group; // parametri profilo
															// aree removed by kevin
															// disab+gruppi
															// disab
		ProfileBeanList.updateProfile(idsite, idprofile, desc, s_param,nomenu);

		// PROFILEMAPS
		// delete
		String sql_delete = "delete from profilemaps where idprofile=?";
		DatabaseMgr.getInstance().executeStatement(null, sql_delete, new Object[] { idprofile });

		// new insert
		MenuSection[] listSection = MenuConfigMgr.getInstance().getMenuSection();
		String menu = "";
		String sql_insert = "insert into profilemaps values (?,?,?,?,?,?,?,?)";
		Integer code = new Integer(0);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		List<Object[]> ins = new ArrayList<Object[]>();
		
		//add "global" (not in configuration.xml)
    	MenuVoce global_voce = new MenuVoce("grpview","nop&folder=grpview&bo=BGrpView&type=menu",-1);
    	insertProfileMaps(properties, ins, global_voce.getName(), now, idprofile, code);
    	
		for (int i = 0; i < listSection.length; i++) {
			MenuSection mn = listSection[i];
			for (int j = 0; j < mn.getListVoci().length; j++) {
				MenuVoce section = mn.getListVoci()[j];
				menu = section.getName();

				insertProfileMaps(properties, ins, menu, now, idprofile, code);

				if (FunctionalityHelper.hasChild(menu) != null) {
					menu = FunctionalityHelper.hasChild(menu).split(";")[0];
					insertProfileMaps(properties, ins, menu, now, idprofile, code);
				}
			}
		}
		if (ins != null && ins.size() != 0) {
			DatabaseMgr.getInstance().executeMultiStatement(null, sql_insert, ins);
		}

		// UPDATE SEZIONE GRUPPI
		String sql = "delete from profilegroups where idsite=? and idprofile=?";
		DatabaseMgr.getInstance().executeStatement(null, sql, new Object[] { new Integer(idsite), new Integer(idprofile) });
		for (int j = 0; j < group.length; j++) {
			idgroup = group[j].getGroupId();

			if (properties.getProperty("g" + idgroup) == null) {
				// profilegroups: insert idprofile,site,idarea,idgroup
				FunctionsDB.disableGroup(new Integer(idprofile), new Integer(idsite), new Integer(1), new Integer(idgroup));
			}
		}
	}
    
    private void insertProfileMaps(Properties properties, List<Object[]> ins, String menu, Timestamp now, int idprofile, Integer code)
    {
    	String tabname = "";
    	TabObj tab = null;
    	MenuAction ma = null;
    	MenuTab mt = null;
    
    	if (properties.getProperty("check_"+menu)==null && !TabList.TECH_TAB.equals(menu))   // menu not checked??  
		{
			//System.out.println(menu+"\n");//insert menu
    		ins.add(new Object[]{idprofile,code,menu,"","","","",now});
		}
		else
		{
			mt = MenuTabMgr.getInstance().getTabMenuFor(menu);
			for (int t=0;t<mt.getNumTab();t++)
	    	{
				tab = mt.getTab(t);
				tabname = tab.getIdTab();
				if ((properties.getProperty("check_"+menu+"_"+t)==null && !TabList.TECH_TAB.equals(menu))  ||
						(properties.getProperty("check_"+menu+"_"+t)==null && TabList.TECH_TAB.equals(menu) && !"tab1name".equals(tabname)))  //tab not checked ???
		    	{
					//System.out.println(menu+" _ " +tabname+"\n");//insert menu// insert menu-tab
					ins.add(new Object[]{idprofile,code,menu,tabname,"","","",now});
		    	}
		    	else
		    	{
			    	ma = MenuActionMgr.getInstance().getActMenuFor(menu, tabname);
			    	if (ma!=null)
			    	{
    			   		for (int b=0;b<ma.getNumAct();b++)
    			   		{
    			   			if (properties.getProperty("s_button_"+menu+"_tab"+t+"name_"+b).equalsIgnoreCase("off"))   // button not checked
    			    		{
    			   				//System.out.println(menu+" _ " +tabname+" _ " +ma.getAct(b).getName()+"\n");//insert menu// insert menu-tab //insert menu-tab-button  s_button_energy_tab0name_2
    			   				ins.add(new Object[]{idprofile,code,menu,tabname,ma.getAct(b).getName(),"","",now});
    			    		}
			    		}
		    		}
		    	}
			}
		}
    }
    
    
    
    
    public RecordSet checkUser(String user,String pass)
	throws ProfileException, Exception
	{
		Record record = getUserByUsername(user);
		if(record == null)
		{
			throw new ProfileException("UserName or Password Incorrect");
		}
		//if it is hidden user, will not check blocked, by Kevin
		if(user.equals(BUserProfile.HIDDENUSER) == false)
		{
			checkBlocked(record);
		}
		else
		{
			checkHiddenUserValid();
		}
		if(cryptingMethod == null || cryptingMethod.length() == 0)
			cryptingMethod = "SHA-1";
		Object[] objects = new Object[] {user, Crypter.encryptMD(pass,cryptingMethod) };
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT username,idprofile,lastpwdchange FROM cfusers ");
		sql.append("WHERE username=? ");
		sql.append("AND password=? ");
	
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql.toString(),objects);
		int badlogons = getBadlogons(record);
		if (rs == null || rs.size() != 1)
		{
			//2010-10-11, Kevin, only increase badlogon flag when "strict" and "logon retries>0"
			if(needToIncreaseBadlogon())
				updateBadlogon(user,++badlogons);
			throw new ProfileException("UserName or Password Incorrect");
		}
		updateBadlogon(user,0);
		return rs;
	}
    private boolean needToIncreaseBadlogon()
    {
    	IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
    	String policytype = product.get(BUserProfile.POLICY);
    	int logonretry = 0;
		try
		{
			logonretry = Integer.parseInt(product.get(BUserProfile.LOGINRETRY));
		}
		catch(Exception ex)
		{}
		if(policytype.equals(BUserProfile.STRICT) && logonretry > 0)
			return true;
		return false;
    }
    private void checkHiddenUserValid()
    	throws ProfileException
    {
    	IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
    	String remotesupport = product.get(BUserProfile.REMOTESUPPORT);
    	if(remotesupport == null || remotesupport.equals("1") == false)
    	{
    		throw new ProfileException(PROFILE_EXCEPTION_HIDDENNOTALLOWED);
    	}
    }
    
    public static int getBadlogons(Record record)
    {
    	int badlogons = 0;
        try
		{
			badlogons = ((Integer)record.get(BADLOGONS_LABELFIELD)).intValue();
		}
		catch(Exception ex)
		{}
		return badlogons;
    }
    public static void checkBlocked(Record record)
    	throws ProfileException
    {
    	IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
    	String policytype = product.get(BUserProfile.POLICY);
    	int logonretry = 0;
		try
		{
			logonretry = Integer.parseInt(product.get(BUserProfile.LOGINRETRY));
		}
		catch(Exception ex)
		{}
		int badlogons = 0;
        try
		{
			badlogons = ((Integer)record.get(BADLOGONS_LABELFIELD)).intValue();
		}
		catch(Exception ex)
		{}
		if(policytype.equals(BUserProfile.STRICT) && logonretry > 0 && badlogons>=logonretry)
		{
			throw new ProfileException(PROFILE_EXCEPTION_BLOCKED);
		}
    }
    public static boolean checkAccountExpired(String user)
    	throws DataBaseException
    {
    	//if it is hidden user, will not check expired, by Kevin
    	if(user.equals(BUserProfile.HIDDENUSER))
    	{
    		return false;
    	}
    	boolean result = false;
    	IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
		String policytype = product.get(BUserProfile.POLICY);
		int expirationdate = 0;
		try
		{
			expirationdate = Integer.parseInt(product.get(BUserProfile.EXPIRATIONDATE));
		}
		catch(Exception ex)
		{}
		if(policytype.equals(BUserProfile.STRICT) && expirationdate>0)
		{
			Record record = getUserByUsername(user);
	    	Timestamp lastpwdchange = (Timestamp)record.get(LASTPWDCHANGE_LABELFIELD);
	    	if(lastpwdchange == null)
	    	{
	    		lastpwdchange = updateLastpwdchange(user);
	    	}
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	String str = df.format(lastpwdchange);
    		try 
    		{
				Date last = df.parse(str);
				Date now = new Date();
				Calendar c = Calendar.getInstance();
		        c.setTime(last);
		        c.add(Calendar.DATE, expirationdate);
		        last = c.getTime();
		        if(now.after(last))
		        {
		        	return true;
		        }
		        else
		        {
		        	return false;
		        }

			} catch (Exception e) 
			{
				e.printStackTrace();
			}
	    	
		}
    	return result;
    }
    public static Timestamp updateLastpwdchange(String user)
    	throws DataBaseException
    {
    	String sql = "UPDATE cfusers set lastpwdchange=? where username=?";
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	Object[] objects = new Object[]{now,user};
    	DatabaseMgr.getInstance().executeStatement(null, sql, objects);
    	return now;
    }
	public static Record getUserByUsername(String user)
		throws DataBaseException
	{
		String sql = "SELECT * from cfusers where username=?";
		Object[] objects = new Object[]{user};
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,objects);
		if(rs.size()>0)
		{
			return rs.get(0);
		}
		else
		{
			return null;
		}
	}
	public void updateBadlogon(String user, int badlogon)
		throws DataBaseException
	{
		String sql = "UPDATE cfusers set badlogons=? where username=?";
		Object[] objects = new Object[]{badlogon,user};
		DatabaseMgr.getInstance().executeStatement(null, sql, objects);
	}
	
	/**
	 * It Checks Remote system's credentials 
	 * and generate authentication token.
	 */
	public String checkRemoteCredential(String sIdent, String sPassw)
	{
		tokenValidity = "";
		try
        {
			SiteInfo si = SiteInfoList.retrieveSiteById(1);
			if (si != null && (si.getCode() != null && si.getCode().equals(sIdent) && 
				si.getPassword() != null && si.getPassword().equals(sPassw)))
			{
				tokenValidity = ""+System.currentTimeMillis();
			}
			else
			{
				tokenValidity = "ERRORAUTH";
			}
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
            Logger logger = LoggerMgr.getLogger(SRVLAccount.class);
            logger.error(e);
        }
		return tokenValidity;
	}
	
	/**
	 * User Management from Remote System
	 * @param idsite 
	 */
	public String manageUserXmlData(int idsite, String xdata)
	{
		StringBuffer ret =  new StringBuffer();
		
		if (!isRemoteManagementActive()){
			ret = new StringBuffer("DISABLED_ERROR");
			return ret.toString();
		}
		
		try 
		{
			XMLNode nData = XMLNode.parse(xdata);
			if(nData != null)
			{
				String chkToken = nData.getAttribute("tkn");
				if(tokenValidity.trim().length() > 0 && chkToken != null && chkToken.trim().equalsIgnoreCase(tokenValidity))
				{
					XMLNode[] users = nData.getNodes("user");
					if(users != null)
					{
						String action = "";
						String account = "";
						String password = "";
						String profile = "";
						
						Properties prop = new Properties();
						
						for(int i=0; i<users.length; i++)
						{
							try 
							{
								prop = new Properties();
								action = users[i].getAttribute("act");
								account = users[i].getNode("ac").getTextValue();
								password = users[i].getNode("pw").getTextValue();
								profile = users[i].getNode("pr").getTextValue();
								
								//find if the user already exists
								Record r = getUserByUsername(account);
								
								// ADD NEW USER
								if(
										(action != null && action.equals("ADD") && r==null)
										||
										(action != null && action.equals("UPD") && r==null)
									)
								{									
									//check if the profile id exists
									ProfileBeanList db_profiles = new ProfileBeanList(idsite,true);
									db_profiles.getProfileById(Integer.parseInt(profile)).getCode();

									prop.put(INPUT_USER, account);
									prop.put(INPUT_PASSWORD, password);
									prop.put(INPUT_PROFILE, profile);
									prop.put("userlogged", RemoteSystem);
									this.addUser(prop);
								}
								// UPD USER
								else if(
										(action != null && action.equals("ADD") && r!=null)
										||
										(action != null && action.equals("UPD") && r!=null)										
									)
								{
									//check if the profile id exists
									ProfileBeanList db_profiles = new ProfileBeanList(idsite,true);
									db_profiles.getProfileById(Integer.parseInt(profile)).getCode();

									prop.put(INPUT_USER_TO_MODIFY, account);
									prop.put(INPUT_PASSWORD, password);
									prop.put(INPUT_PROFILE, profile);
									prop.put("userlogged", RemoteSystem);
									this.modifyUser(prop);
								}
								// DEL USER
								else if(
										(action != null && action.equals("DEL")) && r!=null
										)
								{
									prop.put(INPUT_ROW_SELECTED, account+";"+profile);
									prop.put("userlogged", RemoteSystem);
									this.removeUser(prop);
								}
								ret.append(account+";"+action+";OK\n");
							}
							catch(Exception eIn)
							{
								eIn.printStackTrace();
								Logger logger = LoggerMgr.getLogger(DBProfiler.class);
					            logger.error(eIn);
					            ret.append(account+";"+action+";KO\n");
							}
						}
					}
				}
				else
				{
					ret = new StringBuffer("TOKEN_ERROR");
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Logger logger = LoggerMgr.getLogger(DBProfiler.class);
            logger.error(e);
            ret = new StringBuffer("GENERAL_ERROR");
		}
		return ret.toString();
	}

	public boolean isRemoteManagementActive() {
		boolean r=false;
		
		String sql = "select value from productinfo where key='remoteusermngm'";

	    RecordSet rs = null;
	        
	    try
	    {
	       rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
	    }
	    catch (Exception e)
	    {
	       // PVPro-generated catch block:
	       Logger logger = LoggerMgr.getLogger(VirtualKeyboard.class);
	       logger.error(e);
	    }
	        
	    if ((rs != null) && (rs.size() > 0))
	    {
	    	r = ("on".equals(rs.get(0).get(0).toString()));
        }
		return r;
	}
}
