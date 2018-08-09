package com.carel.supervisor.presentation.session;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import com.carel.supervisor.base.profiling.IProfiler;
import com.carel.supervisor.base.profiling.ProfilingMgr;
import com.carel.supervisor.base.profiling.UserProfile;
import com.carel.supervisor.controller.setfield.NotificationParam;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.bean.ProfileBean;
import com.carel.supervisor.presentation.bean.ProfileBeanList;
import com.carel.supervisor.presentation.menu.MenuNav;
import com.carel.supervisor.presentation.profile.ProfileGroupsBeanList;
import com.carel.supervisor.presentation.profile.ProfileMaps;
import com.carel.supervisor.presentation.profile.ProfileMapsBeanList;
import com.carel.supervisor.presentation.profile.ProfileRedirectBean;


public class UserSession implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2969147218501331518L;
	private int screenH = 768;
    private int screenW = 1024;
    private String userBrowser = null;
    private String userName = null;
    private String customerCode = null;
    private transient UserProfile userProfile = null;
    private String language = null;
    private String encoding = "";
    private String defaultLanguage = null;
    private String defaultLanguageDescription = null;
    private int idSite = -1;
    private String siteName = "";
    private transient GroupListBean groupListBean = null;
    private transient Properties properties = new Properties();
    private transient Throwable throwable = null;
    private UserTransaction userTrx = null;
    private boolean forceLogoutUser = false;
    private transient ProfileMaps profileMaps = null;
    private transient ProfileBean profileBean = null;
    private int profile = -1;
    private boolean localVisibility = true;
    private long lastTime = 0;
    
    private transient ProfileRedirectBean profileRedirect=new ProfileRedirectBean();
    private boolean isIncomingConn = false;
    private String sessionUserId = "";
    
    public UserSession(UserProfile userProfile)
    {
        this.userProfile = userProfile;

        if (this.userProfile.getSection(IProfiler.USER_SECTION) != null)
        {
            userName = this.userProfile.getSection(IProfiler.USER_SECTION)
                                       .getValue(ProfilingMgr.getInstance().getProfiler()
                                                             .getUserNameLabelField()).trim();
            profile = Integer.parseInt(userProfile.getSection(IProfiler.USER_SECTION)
                                                  .getValue(ProfilingMgr.getInstance().getProfiler()
                                                                        .getProfileNameLabelField())
                                                  .trim());

            if (-5 == profile)
            {
                userName = userName.substring(4);
            }
        }

        if (userName == null)
        {
            userName = "";
        }
        else
        {
            userName = userName.trim();
        }

        NotificationParam.getInstance().register(userName);
        // No force logout
        this.forceLogoutUser = false;

    }
    
    /*
     * Setta nella sessione utente se ci sono incoming connection
     */
    public void setIncomingConnections(boolean state) {
    	this.isIncomingConn = state;
    }
    
    public boolean isIncomingConnections() {
    	return this.isIncomingConn;
    }
    
    public boolean isAllowed(String s1, String s2)
    {
        return true;
    }

    public void setForceLogout(boolean state)
    {
        this.forceLogoutUser = state;
    }

    public boolean forceLogout()
    {
        return this.forceLogoutUser;
    }

    public void setUserBrowser(String type)
    {
        this.userBrowser = type;
    }
    
    public void setScreenWidth(String w) {
        try {
            this.screenW = Integer.parseInt(w);
            
        }
        catch(Exception e){}
    }
    
    public void setScreenHeight(String h) {
        try {
            this.screenH = Integer.parseInt(h);
            
        }
        catch(Exception e){}
    }
    
    public int getScreenHeight() {
        return this.screenH;
    }
    
    public int getScreenWidth() {
        return this.screenW;
    }

    public String getUserBrowser()
    {
        return this.userBrowser;
    }

    public String getUserName()
    {
        return userName;
    }

    public boolean isMenuActive(String menu)
    {
        return profileMaps.isMenuActive(menu);
    }

    public boolean isTabActive(String menu, String tabName)
    {
    	if (profileMaps.isMenuActive(menu))
    	{
    		return profileMaps.isTabActive(menu, tabName);
    	}
    	else
    		return false;
    }

    public boolean isNoteActive()
    {
        return profileMaps.isNoteActive();
    }

    public boolean isTabProtected(String menu, String tabName)
    {
        return profileMaps.isTabProtected(menu, tabName);
    }

    public boolean isButtonActive(String menu, String tabName, String buttonName)
    {
        return profileMaps.isButtonActive(menu, tabName, buttonName);
    }

    public boolean isWidgetActive(String menu, String tabName, String widget)
    {
        return profileMaps.isWidgetActive(menu, tabName, widget);
    }

    public String getWidgetStatus(String menu, String tabName, String widget)
    {
        return profileMaps.getWidgetStatus(menu, tabName, widget);
    }

    //  OLD METHOD: used only for retrocompatibility with old Layout Editor Maps
    public int getPermission(int code)
    {
    	if (code==ProfileBean.FUNCT_HACCP)
    	{
    		if (isTabActive("dtlview", "tab4name"))
    		{
    			return 1;
    		}
    		else
    			return 0;
    	}
    	else if (code==ProfileBean.FUNCT_HISTORICAL)
    	{
    		if (isTabActive("dtlview", "tab5name"))
    		{
    			return 1;
    		}
    		else
    			return 0;
    	}
    	else
    		return 0;
    }

    public String getEncoding()
    {
        return this.encoding;
    }

    public void setEncoding(String enc)
    {
        this.encoding = enc;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public boolean localVisibility()
    {
        return localVisibility;
    }

    public String getDefaultLanguage()
    {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage)
    {
        this.defaultLanguage = defaultLanguage;
    }

    public String getDefaultLanguageDescription()
    {
        return defaultLanguageDescription;
    }

    public void setDefaultLanguageDescription(String defaultLanguageDescription)
    {
        this.defaultLanguageDescription = defaultLanguageDescription;
    }

    public int getIdSite()
    {
        return idSite;
    }

    public void setIdSite(int idSite)
    {
        this.idSite = idSite;
    }

    public String getCustomerCode()
    {
        return customerCode;
    }

    public void setCustomerCode(String customerCode)
    {
        this.customerCode = customerCode;
    }

    public void loadGroup() throws Exception
    {
        ProfileMapsBeanList profileMapsBeanList = new ProfileMapsBeanList();
        profileMapsBeanList.load(profile);
        profileMaps = new ProfileMaps(profileMapsBeanList);

        ProfileGroupsBeanList profileGroupsBeanList = new ProfileGroupsBeanList();
        profileGroupsBeanList.load(profile, new Integer(1));

        groupListBean = new GroupListBean(idSite, language, profileGroupsBeanList);

        profileBean = ProfileBeanList.retrieveProfile(profile, idSite);
    }

    public GroupListBean getGroup()
    {
        return groupListBean;
    }

    public Transaction getTransaction()
    {
        return userTrx;
    }

    public String getProperty(String key)
    {
        return properties.getProperty(key);
    }

    public String getPropertyAndRemove(String key)
    {
        String value = properties.getProperty(key);
        properties.remove(key);

        return value;
    }

    public boolean containsProperty(String key)
    {
        return properties.containsKey(key);
    }

    public void removeProperty(String key)
    {
        properties.remove(key);
    }

    public void setProperty(String key, String value)
    {
        properties.setProperty(key, value);
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void clearProperties()
    {
        properties.clear();
    }

    // Gestione Errore
    public Throwable getThrowable()
    {
        return throwable;
    }

    public void setThrowable(Throwable throwable)
    {
        this.throwable = throwable;
    }

    public void removeThrowable()
    {
        throwable = null;
    }

    public boolean isThrowablePresent()
    {
        return (null != throwable);
    }

    // Transazione Utente
    public UserTransaction getRootUserTransaction()
    {
        return this.userTrx;
    }

    public UserTransaction getCurrentUserTransaction()
    {
        UserTransaction ut = this.userTrx;

        if (ut != null)
        {
            while (ut.hasChild())
            {
                ut = ut.getChild();
            }
        }

        return ut;
    }

    public void addNewUserTransaction(UserTransaction userTrx)
    {
        this.userTrx = userTrx;
    }

    public void addUserTransaction(UserTransaction userTrx)
    {
        if (this.userTrx == null)
        {
            this.userTrx = userTrx;
        }
        else
        {
            this.userTrx.addChild(userTrx);
        }
    }

    public String getNavMenu()
    {
        return MenuNav.buildNavMenu(this.userTrx, getLanguage());
    }

    public String getBack()
    {
        return MenuNav.getBack(this.userTrx, getLanguage());
    }

    public int getProfile()
    {
        return profile;
    }
    
    public String getProfileCode()
    {
        return profileBean.getCode();
    }

    public boolean getProfileNomenu()
    {
    	return profileBean.getNomenu();
    }
    public void setProfile(int profile)
    {
        this.profile = profile;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    public long getLastTime()
    {
    	return lastTime;
    }

    public void setLastTime(long lastTime)
    {
        this.lastTime = lastTime;
    }
    
    /*
     * Intervento:
     * Solo un utente alla volta
     */
    public void setSessionId(String sid) {
        this.sessionUserId = sid;
    }
    
    /*
     * Intervento:
     * Solo un utente alla volta
     */
    public String getSessionId() {
        return this.sessionUserId;
    }
    
    public int getVariableFilter()
    {
    	return this.profileBean.getVariableFilter();
    }

	public ProfileRedirectBean getProfileRedirect() {
		return profileRedirect;
	}

	public void setProfileRedirect(ProfileRedirectBean profileRedirect) {
		this.profileRedirect = profileRedirect;
	}
	
	public boolean isDeviceVisible(int iddevice)
	{
		List<Integer> noAccessGroup = this.profileBean.getForbiddenGroups();
		Integer idgroup = groupListBean.getDeviceStructureList().get(iddevice).getIdGroup();
		
		if (noAccessGroup!=null && noAccessGroup.contains(idgroup))
		{
			return false;
		}
		else
			return true;
	}
	
	
	
	
	
	
	
	
	
	
}
