package com.carel.supervisor.presentation.sdk.obj;

import javax.servlet.http.HttpServletRequest;

import com.carel.supervisor.presentation.bean.ProfileBean;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;

public class CurrUser
{
    private UserSession sessionUser = null;
    
    private String userName = "";
    private String userProfile = "";
    private String userBrowser = "";
    private int screenHeight = 0;
    private int screenWidth;
    // Since v2.1.0
    private String userLanguage;
    
    public CurrUser(){
    }
    
    public void setCurrentSession(UserSession us) 
    {
        this.sessionUser = us;
        initCurUserAttribute();
    }
    
    /**
     * PVP 2.0
     * @param HttpServletRequest: HTTP request
     */
	public void setReq(HttpServletRequest req) 
	{
		this.sessionUser = ServletHelper.retrieveSession(req.getRequestedSessionId(), req);
		initCurUserAttribute();
	}

	private void initCurUserAttribute()
    {
    	this.userName = this.sessionUser.getUserName();
    	this.userProfile = this.sessionUser.getProfileCode();
    	this.userBrowser = this.sessionUser.getUserBrowser();
    	this.screenWidth = this.sessionUser.getScreenWidth();
    	this.screenHeight = this.sessionUser.getScreenHeight();
    	this.userLanguage = this.sessionUser.getLanguage();
    }
    
	 /**
     * @since v2.1.0
     */
    public String getUserLanguage() {
    	return userLanguage;
    }
    
    public String getUserName() {
		return userName;
	}

	public String getUserProfile() {
		return userProfile;
	}

	public String getUserBrowser() {
		return userBrowser;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public boolean HaveRight(int r)
    {
        boolean ret = false;
        switch(r)
        {
            case 1:
                ret = haveServicesRight();
                break;
            case 2:
                ret = haveManufacturerRight();
                break;
        }
        return ret;
    }
    
    public boolean haveServicesRight()
    {
    	boolean can_set = sessionUser.isButtonActive("dtlview","tab1name","subtab2name");
        return ((sessionUser.getVariableFilter()==ProfileBean.FILTER_SERVICES || 
        	sessionUser.getVariableFilter()==ProfileBean.FILTER_MANUFACTURER) && can_set)?true:false;
    }
    
    public boolean haveManufacturerRight() {
    	boolean can_set = sessionUser.isButtonActive("dtlview","tab1name","subtab2name");
        return ((sessionUser.getVariableFilter()==ProfileBean.FILTER_MANUFACTURER) && can_set)?true:false;
    }
}
