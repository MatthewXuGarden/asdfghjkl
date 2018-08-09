package com.carel.supervisor.dataaccess.dataconfig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.reorder.ReorderFrequency;
import com.carel.supervisor.dataaccess.reorder.ReorderInformation;


public class VariableInfoList extends AbstractBindable
{
	private static int NUM_REORDER_QUEUE = 100;
	protected VariableInfo[] variableInfo = null;

	protected Map variableInfoById = new HashMap();
    
    protected VariableInfoList()
    {	
    }
    
    public VariableInfoList(String dbId, String plantId)
        throws Exception
    {
        super();
        
        List<VariableInfo> tmp = new ArrayList<VariableInfo>();
        ReorderInformation reorderQueue = new ReorderInformation(NUM_REORDER_QUEUE);
        ReorderFrequency reorderFrequency = new ReorderFrequency(reorderQueue);
        reorderFrequency.startReorderHistorical();
        
        String sql = 
        "SELECT cfvariable.*,buffer.keymax,buffer.keyactual,buffer.isturn " +
        "FROM cfvariable,buffer WHERE " +
        "(cfvariable.idvariable=buffer.idvariable) " +
        "and (cfvariable.idsite=buffer.idsite) " +
        "and frequency is not null and frequency != 0 and iscancelled='FALSE' order by islogic";
        
        Connection con = null;
        ResultSet rs = null;
        
        try
        {
	        con = DatabaseMgr.getInstance().getConnection(null);
	        rs = con.createStatement().executeQuery(sql);
	        
	        while(rs.next())
	        {
	        	tmp.add(new VariableInfo(rs));
	        }
	        
	        variableInfo = new VariableInfo[tmp.size()];
	
	        for(int i=0; i<tmp.size(); i++)
	        {
	            variableInfo[i] = tmp.get(i);
	            variableInfoById.put(variableInfo[i].getId(),variableInfo[i]);
	        }
        }
        catch(Exception e) {
        	throw new Exception(e);
        }
        finally
        {
        	try 
        	{
        		if(rs != null)
        			rs.close();
            	DatabaseMgr.getInstance().closeConnection(null,con);
        	}
        	catch(Exception ex1){
        		throw new Exception(ex1);
        	}
        }
    }

    public void bind(Map registry) throws Exception
    {
        DeviceInfoList devInfoList = (DeviceInfoList) registry.get("cfdev");
        DeviceInfo devInfo = null;

        for (int i = 0; i < variableInfo.length; i++)
        {
            devInfo = devInfoList.getByIdDevice(variableInfo[i].getDevice());
            variableInfo[i].setDeviceInfo(devInfo);
        }
    }

    public int size()
    {
        return variableInfo.length;
    }

    public VariableInfo get(int pos)
    {
        return variableInfo[pos];
    }

    public VariableInfo getById(int idVar)
    {
        return (VariableInfo) variableInfoById.get(new Integer(idVar));
    }

    public void clear()
    {
        for (int i = 0; i < variableInfo.length; i++)
        {
            variableInfo[i] = null;
        }

        variableInfoById.clear();
        variableInfo = null;
        variableInfoById = null;
    }
}
