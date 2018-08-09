package com.carel.supervisor.presentation.bo;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.presentation.session.UserTransaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import supervisor.Master;


public class BMasterMaps extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    public BMasterMaps(String l)
    {
    	 super(l, REFRESH_TIME);
    }
    
    protected Map initializeRefresh()
	{
    	Map map = new HashMap();
		return map;
	}

    protected Properties initializeEventOnLoad() {
        Properties p = new Properties();
        p.setProperty("tab1name","initializeComponent();");
        return p;
    }

    protected Properties initializeJsOnLoad() {
        Properties p = new Properties();
        p.setProperty("tab1name","maps.js;../arch/arkustom.js");
        return p;
    }
    
    public void executePostAction(UserSession us, String tabName,
            Properties prop) throws Exception
    {
    	try
        {
            String cmdk = prop.getProperty("cmdk");
            if((cmdk != null) && (cmdk.equalsIgnoreCase("sdks")))
            {
            	 UserTransaction ut = us.getCurrentUserTransaction();
                if(ut != null)
                {
                    String sCurTab = ut.getCurrentTab();
                    String resource = ut.getProperty("resource");
                    if(resource != null && !resource.equalsIgnoreCase("dtlview"))
                    {
                        BDtlView tmpBxSet = new BDtlView(us.getLanguage());
                        tmpBxSet.executePostAction(us,sCurTab,prop);
                        tmpBxSet = null;
                    }
                }
            }
        }
        catch(Exception e) 
        {
            Logger logger = LoggerMgr.getLogger(Master.class);
            logger.error(e);
        }
    }
}
