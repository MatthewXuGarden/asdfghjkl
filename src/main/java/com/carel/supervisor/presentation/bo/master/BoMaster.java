package com.carel.supervisor.presentation.bo.master;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.presentation.refresh.RefreshBean;
import com.carel.supervisor.presentation.refresh.RefreshBeanList;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.script.EnumerationMgr;
import com.carel.supervisor.script.ExpressionMgr;
import com.carel.supervisor.script.special.Special;


public abstract class BoMaster implements IMaster,Serializable
{
    private String defaultTab							= "";
    private String defaultRes							= "";
    private Properties eventOnLoad						= null;
    private Properties jsOnLoad							= null;
    private Properties keyCommit						= null;
    private Map refresh									= null;
    
    protected ExpressionMgr expression					= null;
    protected EnumerationMgr enumeration				= null;
    protected Special special							= null;
    
    // docType it used to get access to modern browser capabilities
    public static final String DOCTYPE_STRICT		= "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"\n\"http://www.w3.org/TR/html4/strict.dtd\">";
    protected static final String DOCTYPE_TRANSITIONAL	= "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n\"http://www.w3.org/TR/html4/loose.dtd\">";
    protected static final String DOCTYPE_FRAMESET		= "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\"\n\"http://www.w3.org/TR/html4/frameset.dtd\">";
    private Properties docType							= null;
    
    
    public BoMaster(String lang)
    {
        this.defaultTab = "tab1name";
        this.defaultRes = "SubTab1.jsp";
        this.eventOnLoad = initializeEventOnLoad();
        this.jsOnLoad = initializeJsOnLoad();
        this.refresh = initializeRefresh();
        this.keyCommit = initializeCommitKey();
        this.docType = initializeDocType();
    }

    public BoMaster(String lang, int refTime)
    {
        this(lang);
    }

    public String getRefreshTime(String tabname)
    {
        String ret = "-1";
        RefreshBeanList rBeanList = (RefreshBeanList) this.refresh.get(tabname);

        if (rBeanList != null)
        {
            ret = String.valueOf(rBeanList.getRefreshTime());
        }

        return ret;
    }

    protected void setDefaultResource(String res)
    {
        this.defaultRes = res;
    }

    public String getDefaulResource() throws Exception
    {
        return this.defaultRes;
    }

    protected void setDefautlTabName(String tname)
    {
        this.defaultTab = tname;
    }

    public String getDefaulTabName() throws Exception
    {
        return this.defaultTab;
    }

    public String getEventOnLoad(String tabName) throws Exception
    {
        if (this.eventOnLoad != null)
        {
            return this.eventOnLoad.getProperty(tabName);
        }
        else
        {
            return "";
        }
    }

    public String[] getJavascript(String tabName) throws Exception
    {
        String[] arJs = null;

        if (this.jsOnLoad != null)
        {
            String sJs = this.jsOnLoad.getProperty(tabName);

            if (sJs != null)
            {
                arJs = sJs.split(";");
            }
            else
            {
                arJs = new String[0];
            }
        }
        else
        {
            arJs = new String[0];
        }

        return arJs;
    }

    public RefreshBean[] getRefreshObj(String tabname)
        throws Exception
    {
        RefreshBeanList rbl = (RefreshBeanList) this.refresh.get(tabname);

        if (rbl != null)
        {
            return rbl.getRefreshObj();
        }
        else
        {
            return new RefreshBean[0];
        }
    }

    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
    }
    
    public String getCommitAction(String tabName) 
    {
    	String ret = this.keyCommit.getProperty(tabName);
    	if(ret == null || ret.equalsIgnoreCase(""))
    		ret = "NOP";
    	return ret;
    }
    
    public String getDocType(String tabName)
    {
    	if( docType != null ) {
    		String strDocType = docType.getProperty(tabName);
    		if( strDocType != null )
    			return strDocType;
    	}
    	return "";
    }
    
    protected Map initializeRefresh()
    {
        return new HashMap();
    }
    
    protected Properties initializeCommitKey()
    {
    	return new Properties();
    }
    
    protected abstract Properties initializeEventOnLoad();

    protected abstract Properties initializeJsOnLoad();
    
    protected Properties initializeDocType()
    {
    	return null;
    }
    
    public void loadFilter(int idDevice,int idDevMdl,String lang) {}
    
    // Add for Device Details Page
    public ExpressionMgr getExpressionMgr() {
    	return this.expression;
    }
    //  Add for Device Details Page
    public EnumerationMgr getEnumerationMgr() {
    	return this.enumeration;
    }
    //  Add for Device Details Page
    public Special getSpecial() {
    	return this.special;
    }
    
    public String executeDataAction(UserSession us, String tabName,
            Properties prop) throws Exception
    {
    	return "<response status='OK'></response>";
    }
}
