package com.carel.supervisor.presentation.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class DeviceStructureList implements Serializable
{
    private List devices = new ArrayList();
    private Map devicesMap = new HashMap();
    private Map varGroups = new HashMap();

    public DeviceStructureList()
    {
    }

    public int size()
    {
        return devices.size();
    }

    public void clear()
    {
        devices.clear();
        devicesMap.clear();
    }
    
    public void loadVarGroups(int site, String language) throws Exception
    {
    	String sql = "select cfvarmdlgrp.idvargroup, cftableext.description from cfvarmdlgrp inner join cftableext" +
    			" on cfvarmdlgrp.idvargroup=cftableext.tableid where cftableext.idsite = ? and cftableext.languagecode = ?" +
    			" and cftableext.tablename='cfvarmdlgrp'";
    	RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(site), language});
    	Record record = null;
    	for (int i = 0; i < recordset.size(); i++)
    	{
    		record = recordset.get(i);
    		varGroups.put((Integer)record.get(0),(String)record.get(1));
    	}
    }
    
    public String getVarGroups(Integer code)
    {
    	return (String)varGroups.get(code);
    }
    
    public int[] retrieveIdsByGroupsId(int[] groupId)
    {
        List deviceList = new ArrayList();

        for (int i = 0; i < devices.size(); i++)
        {
            for (int j = 0; j < groupId.length; j++)
            {
                if (((DeviceStructure) devices.get(i)).getIdGroup() == groupId[j])
                {
                    deviceList.add((DeviceStructure) devices.get(i));
                }
            }
        }

        int[] ids = new int[deviceList.size()];

        for (int i = 0; i < deviceList.size(); i++)
        {
            ids[i] = ((DeviceStructure) deviceList.get(i)).getIdDevice();
        }

        return ids;
    }

    //if groupId == 0 , search all
    public int[] retrieveIdsByGroupId(int groupId)
        throws Exception //da finire
    {
        List deviceList = new ArrayList();
        DeviceStructure device = null;

        for (int i = 0; i < devices.size(); i++)
        {
            device = (DeviceStructure) devices.get(i);
            
            // if group is 0 or 1 , show all device
            if (0 == groupId || 1 == groupId)
            {
                deviceList.add((DeviceStructure) devices.get(i));
            }
            else
            {
                if (device.getIdGroup() == groupId)
                {
                    deviceList.add((DeviceStructure) devices.get(i));
                }
            }
        }

        int[] ids = new int[deviceList.size()];

        for (int i = 0; i < deviceList.size(); i++)
        {
            ids[i] = ((DeviceStructure) deviceList.get(i)).getIdDevice();
        }

        return ids;
    }

    public DeviceStructure get(int idDevice)
    {
        return (DeviceStructure) devicesMap.get(new Integer(idDevice));
    }

    public void addDevice(DeviceStructure deviceStructure)
    {
        devices.add(deviceStructure);
        devicesMap.put(new Integer(deviceStructure.getIdDevice()),deviceStructure);
    }
    
    /*
     * SDK
     */
    public int getDeviceNum()
    {
        if(devices != null)
            return devices.size();
        else
            return 0;
    }
    
    public DeviceStructure getDeviceAt(int idx)
    {
        DeviceStructure ret = null;
        try {
            ret = (DeviceStructure)devices.get(idx);
        }
        catch(Exception e) {
        }
         
        return ret;
    }
}
