package com.carel.supervisor.presentation.bo;

import java.util.Properties;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.presentation.bean.DeviceListBean;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.bean.GroupListBean;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;


public class BDeviceView extends BoMaster
{
	private static final long serialVersionUID = -4680136884100620816L;
	private static final int REFRESH_TIME = -1;

    public BDeviceView(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeRefreshTime()
    {
        Properties p = new Properties();

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        //p.put("tab1name","deviceview.js");
        p.put("tab1name","deviceview_n.js"); //integrazione plugin Winery
        return p;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name","startRefresh(true);");
        return p;
    }

	@Override
	public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception {
		String language = us.getLanguage();
		int idsite = us.getIdSite();
		StringBuffer ris = new StringBuffer();
		GroupListBean groupListBean = us.getGroup();
		int[] groups = groupListBean.getIds();;
    	DeviceStructureList deviceStructureList = groupListBean.getDeviceStructureList(); 
    	int[] ids = deviceStructureList.retrieveIdsByGroupsId(groups);
    	us.getTransaction().setIdDevices(ids);
    	us.getTransaction().setIdDevicesCombo(ids);

    	String limit = prop.getProperty("limit");
		String offst = prop.getProperty("offset");
		String idline = prop.getProperty("idline");
		try 
		{
			DeviceListBean deviceList = idline == null || idline.equals("0")
				? new DeviceListBean(idsite,language,groups,false,limit,offst)
				: new DeviceListBean(idsite,language,groups,false,limit,offst,idline);	
			if(deviceList != null)
			{
				ris.append("<response>");
				ris.append("<isMenuActive>"+"<![CDATA[" + us.isMenuActive("dtlview") + "]]>" + "</isMenuActive>");
				Boolean extrainfo = prop.getProperty("first")!=null?new Boolean(prop.getProperty("first")):new Boolean(false);
				ris.append(deviceList.getXmlRefreshDevice(extrainfo));
				ris.append("</response>");
			}
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}	
		return ris.toString();
	}
}
