package com.carel.supervisor.director.ac;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class AcProperties {
    
	private Map<String,Integer> properties = null;
	
    public static final int minCycleTime = 600; //tempo minimo di ciclo x propagazione = 10 min.
    
	public AcProperties()
	{
		String sql = "select * from ac_config";
		try
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
			Record r = null;
			properties = new HashMap<String,Integer>();
			for (int i=0;i<rs.size();i++)
			{
				r = rs.get(i);
				properties.put(r.get(0).toString(),(Integer)r.get(1));
			}
		}
		catch (Exception e)
		{
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcProperties.class);
            logger.error(e);
		}
	}
	
	public int getProp(String key)
	{
		return properties.get(key);
	}
	
	public static void updateProp(String key, Integer value)
	{
		String sql = "update ac_config set value=? where key=?";
		try 
		{
			DatabaseMgr.getInstance().executeStatement(null,sql,new Object[]{new Integer(value),key});
		}
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcProperties.class);
            logger.error(e);;
		}
	}
}
