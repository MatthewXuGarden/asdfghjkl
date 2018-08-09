package com.carel.supervisor.presentation.bo.helper;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class DeviceStatus
{
    public DeviceStatus()
    {
    }
    
    public static String isEnabled(int iddev) throws Exception
    {
    	String sql = "select isenabled from cfdevice where iddevice = ?";
    	RecordSet rec = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(iddev)});
    	return rec.get(0).get(0).toString();
    }
}
