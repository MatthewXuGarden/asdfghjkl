package com.carel.supervisor.presentation.sdk.obj;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.carel.supervisor.presentation.bean.DeviceStructure;
import com.carel.supervisor.presentation.bean.DeviceStructureList;
import com.carel.supervisor.presentation.helper.ServletHelper;
import com.carel.supervisor.presentation.session.UserSession;

public class CurrNode
{
    private UserSession sessionUser = null;
    private Map<String,CurrUnit> deviceMap = new HashMap<String, CurrUnit>();
    private Map<Integer,CurrUnit> deviceMapById = new HashMap<Integer, CurrUnit>();
    
    public CurrNode(){
    }
    
    public void setCurrentSession(UserSession us) {
        this.sessionUser = us;
    }
    
    /**
     * PVP 2.0
     * @param HttpServletRequest: HTTP request
     */
    public void setReq(HttpServletRequest req) {
		this.sessionUser = ServletHelper.retrieveSession(req.getRequestedSessionId(), req);
	}
    
    /**
     * PVP 2.0
     * @param deviceCode: Device Code
     * 
     * @return CurrUnit
     */
    public CurrUnit getDevice(String deviceCode)
    {
    	CurrUnit cUnit = null;
    	if(this.sessionUser != null)
        {
    		DeviceStructureList deviceStructureList = this.sessionUser.getGroup().getDeviceStructureList();
    		int devNum = deviceStructureList.getDeviceNum();
    		for(int i=0; i<devNum; i++)
    		{
    			if(deviceStructureList.getDeviceAt(i).getCode() != null && 
    			   deviceStructureList.getDeviceAt(i).getCode().equals(deviceCode))
    			{
    				if (!deviceMapById.containsKey(deviceStructureList.getDeviceAt(i).getIdDevice()))
    				{
	    				cUnit = new CurrUnit(deviceStructureList.getDeviceAt(i).getIdDevice());
	    				cUnit.setCurrentSession(this.sessionUser,true);
	    				deviceMapById.put(cUnit.getId(), cUnit);
	    		    	deviceMap.put(deviceCode, cUnit);
	    		    	break;
    				}
    				else
    				{
    					cUnit = deviceMapById.get(deviceStructureList.getDeviceAt(i).getIdDevice());
    					break;
    				}
    			}
    		}
        }
    	
    	return cUnit;
    }
    
    public int getLenght()
    {
        int num = 0;
        if(this.sessionUser != null)
        {
            try 
            {
                DeviceStructureList deviceStructureList = this.sessionUser.getGroup().getDeviceStructureList();
                num = deviceStructureList.getDeviceNum();
            }
            catch(Exception e) {
                
            }
        }
        return num; 
    }
    
    public CurrUnit getCurrUnitAt(int idx)
    {
        DeviceStructureList deviceStructureList = this.sessionUser.getGroup().getDeviceStructureList();
        DeviceStructure ds = deviceStructureList.getDeviceAt(idx);
        CurrUnit curUnit = new CurrUnit();
        if(ds != null)
        {
            this.sessionUser.setProperty("iddev",""+ds.getIdDevice());
            curUnit.setCurrentSession(this.sessionUser);
        }
        return curUnit;
    }
    
    public CurrUnit getCurrUnitById(int id)
    {
        DeviceStructureList deviceStructureList = this.sessionUser.getGroup().getDeviceStructureList();
        DeviceStructure ds = deviceStructureList.get(id);
        CurrUnit curUnit = new CurrUnit();
        if(ds != null)
        {
            this.sessionUser.setProperty("iddev",""+ds.getIdDevice());
            curUnit.setCurrentSession(this.sessionUser);
        }
        return curUnit;
   }
    
   public CurrUnit getDeviceFromCurrNodeByCode(String deviceCode)
   {
	   return deviceMap.get(deviceCode);
   }
   
   public CurrUnit getDeviceFromCurrNodeById(Integer id)
   {
	   return deviceMapById.get(id);
   }
   
   public Map<Integer,CurrUnit> getDeviceMap()
   {
	   return deviceMapById;
   }
}
