package com.carel.supervisor.presentation.refresh;

import com.carel.supervisor.presentation.devices.DeviceList;
import com.carel.supervisor.presentation.session.UserSession;


public class RefreshDevices extends RefreshMaster
{
    private DeviceList deviceList = null;

    public RefreshDevices()
    {
    }

    public void refresh(UserSession userSession) throws Exception
    {
        if ((arData != null) && (arData[INDEX_REFRESH_TYPE] != null))
        {
            if (arData[INDEX_REFRESH_TYPE].equalsIgnoreCase(TYPE_REFRESH) &&
                    (arData[INDEX_PAGE] != null) && arData[INDEX_PAGE].equalsIgnoreCase("1"))
            {
                deviceList = new DeviceList(userSession, 1);
            }

            if (arData[INDEX_REFRESH_TYPE].equalsIgnoreCase(TYPE_PAGE))
            {
                deviceList = new DeviceList(userSession, new Integer(arData[INDEX_PAGE]).intValue()); //,arData[INDEX_TY
            }
        }
    }

    public String getHtmlData(UserSession userSession, String htmlObj)
    {
        if (deviceList != null)
        {
            return deviceList.getHTMLDeviceTableRefresh(htmlObj, userSession.getLanguage(),userSession);
        }

        return "";
    }
}
