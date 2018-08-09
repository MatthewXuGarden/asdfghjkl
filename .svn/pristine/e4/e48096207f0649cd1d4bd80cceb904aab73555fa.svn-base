package com.carel.supervisor.presentation.bo;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfo;
import com.carel.supervisor.dataaccess.dataconfig.SiteInfoList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import com.carel.supervisor.remote.manager.RemoteMgr;
import java.util.Properties;


public class BRSiteAcces extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    public BRSiteAcces(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "openForBrowsOnline();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "rsiteacces.js");

        return p;
    }

    public void executePostAction(UserSession us, String tabName, Properties prop)
        throws Exception
    {
        String acty = prop.getProperty("curaction");
        String site = prop.getProperty("cursite");
        String phone = prop.getProperty("curphone");
        String type = prop.getProperty("curtype");
        String inst = prop.getProperty("curinstal");
        String ris = "";

        int idSite = 0;
        try {
            idSite = Integer.parseInt(site);
        }catch (Exception e){}

        if ((acty != null) && acty.equalsIgnoreCase("closelocal"))
        {
            RemoteMgr.getInstance().closeLocalSite(idSite);
        }
        else if ((acty != null) && acty.equalsIgnoreCase("openlocal"))
        {
            try
            {
            	ris = RemoteMgr.getInstance().openLocalSite(idSite, phone, type, inst);
            }
            catch (Exception e) {
                Logger logger = LoggerMgr.getLogger(this.getClass());
                logger.error(e);
            }
        }

        if(acty != null)
        {
	        us.getCurrentUserTransaction().setProperty("comefrompost", "T");
	        us.getCurrentUserTransaction().setProperty("curinstal", inst);
	        us.getCurrentUserTransaction().setProperty("curtype", type);
	        us.getCurrentUserTransaction().setProperty("ipforlan", ris);
        }
    }

    public SiteInfo[] getSiteList()
    {
        SiteInfo[] localSite = null;

        try
        {
            localSite = SiteInfoList.retriveRemoteSite();
        }
        catch (Exception e)
        {
        }

        return localSite;
    }
}
