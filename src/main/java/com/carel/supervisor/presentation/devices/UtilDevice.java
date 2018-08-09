package com.carel.supervisor.presentation.devices;

import com.carel.supervisor.device.DeviceStatusMgr;
import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.session.UserSession;


public class UtilDevice
{
	private static final int MAX_DESC_LEN = 55;
	
    private UtilDevice()
    {
    }

    public static String getLedColor(Integer idDevice)
    {
        String s = "";

        //    	Order is important into the following lines
        if (!DeviceStatusMgr.getInstance().isOffLineDevice(idDevice))
        {
            s = "1";
        }

        if (DeviceStatusMgr.getInstance().isAlarm(idDevice))
        {
            s = "2";
        }

        if (DeviceStatusMgr.getInstance().isOffLineDevice(idDevice))
        {
            s = "0";
        }

        if (DeviceStatusMgr.getInstance().isDisabled(idDevice))
        {
            s = "3";
        }

        return s;
    }

    public static String getDeviceCombo(UserSession session)
    {
        StringBuffer html = new StringBuffer("");
        DeviceStructure tmp_dev = null;
        DeviceStructureList deviceStructureList = session.getGroup().getDeviceStructureList();

        //String s_group = session.getCurrentUserTransaction().getProperty("group");
        //int idgroup = Integer.parseInt(s_group);
        int[] ids = session.getTransaction().getIdDevicesCombo(); //deviceStructureList.retrieveIdsByGroupId(idgroup);
        
        //add by Kevin, if ids.length == 0, means dtlview is not opened from global, so show all the devices in device combo 
        if(ids.length == 0)
        {
        	try
        	{
        		ids = deviceStructureList.retrieveIdsByGroupId(0);
        	}
        	catch(Exception ex)
        	{}
        }
        String s_iddev = session.getProperty("iddev");
        int id_select = Integer.parseInt(s_iddev);
        String selected = "";

        if ((ids != null) && (ids.length > 0))
        {
            html.append(
            "<select class='devices_combo' id='combo_dev' onchange='document.getElementById(\"combo_dev\").disabled=true;go_to_device();'>");

            for (int i = 0; i < ids.length; i++)
            {
                selected = "";
                tmp_dev = deviceStructureList.get(ids[i]);

                if (tmp_dev.getIdDevice() == id_select)
                {
                    selected = "selected";
                }

                html.append("<option " + selected + " value='" + tmp_dev.getIdDevice() + "'><b>");
                
                // truncate the description if longer than 55 characters
                String desc = tmp_dev.getDescription();
                if (desc.length()> MAX_DESC_LEN){               	
                	String shortDesc = desc.substring(0, MAX_DESC_LEN-1)+ " ... ";
                	html.append(shortDesc);
                } else {
                	html.append(desc);
                }
                html.append("</b></option>/n");
            }

            html.append("</select>");
        }

        return html.toString();
    }
}
