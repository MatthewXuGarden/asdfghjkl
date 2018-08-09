package com.carel.supervisor.presentation.bo.helper;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class SiteListHelper
{
	private Map site = new HashMap();
	
    public SiteListHelper()
    {
    }
    
    public void load() throws Exception
    {
    	String sql = "select idsite,name from cfsite";
    	RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, null);
    	Record record = null;
    	Integer id = null;
    	String name = null;
    	for(int i = 0; i < recordset.size(); i++)
    	{
    		record = recordset.get(i);
    		id = (Integer)record.get(0);
    		name = (String)record.get(1);
    		site.put(id,name);
    	}
    }
    
    public String getName(Integer id)
    {
    	return (String)site.get(id);
    }
}
