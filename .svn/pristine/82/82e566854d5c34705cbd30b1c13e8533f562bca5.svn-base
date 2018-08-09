package com.carel.supervisor.presentation.refresh;

import com.carel.supervisor.presentation.devices.AbstractDtlDevice;
import com.carel.supervisor.presentation.devices.DeviceDetail;
import com.carel.supervisor.presentation.devices.ReadOnlyDtlDevice;
import com.carel.supervisor.presentation.session.UserSession;


public class RefreshDevicesDetail extends RefreshMaster
{
    private DeviceDetail dd = null;
	private AbstractDtlDevice absDev = null;
	
    public RefreshDevicesDetail(){}

    public void refresh(UserSession userSession) throws Exception
    {
        if ((arData != null) && (arData[INDEX_REFRESH_TYPE] != null))
        {
        	int idDev = -1;
        	try {
        		idDev  = Integer.parseInt(userSession.getProperty("iddev"));
        	}
        	catch(NumberFormatException e){}
        	
        	dd = new DeviceDetail();
        	dd.setIdDevice(idDev);
        	dd.loadDeviceVariable(userSession.getLanguage(),userSession.getIdSite());
        	
        	absDev = new ReadOnlyDtlDevice(userSession,userSession.getLanguage(),dd.getIdDevice());
        	absDev.profileVariables(dd.getVariablesList());
        }
    }

    public String getHtmlData(UserSession userSession, String htmlObj)
    {
    	if(absDev != null)
    		return absDev.refreshVariables(htmlObj);
    	
        return "";
    }
}
