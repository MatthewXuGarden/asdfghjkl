package com.carel.supervisor.dataaccess.datalog.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ReportExportConfigList 
{
	Map<String,String> map = null;
	
	public static String SEPARATOR	= "separator";
	public static String DECIMAL_LENGTH = "decimal_length";
	public static String GROUP_SEPARATOR = "group_separator";
	public static String DECIMAL_SEPARATOR = "decimal_separator";
	
	public Map<String,String> loadMap()
	{
		map = new HashMap<String,String>();
		String sql = "select * from cfreportexportconf";
		try 
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			for(int i=0;i<rs.size();i++)
			{
				Record rec = rs.get(i);
				String key = rec.get("key").toString();
				String value = rec.get("value").toString();
				map.put(key, value);
			}
		} catch (DataBaseException e) 
		{
			Logger logger = LoggerMgr.getLogger(ReportBeanList.class);
			logger.error(e);
		}
		return map;
	}
	public void save(String[] keys, String[] values) throws DataBaseException
	{
		Object[][] params = new Object[keys.length][3];
        String sql = "UPDATE cfreportexportconf set value=?,lastupdate=? where key=?";
        for (int i = 0; i < keys.length; i++)
        {
            String key = keys[i];
            String value = values[i];
            params[i][0] = value;
            params[i][1] = new Timestamp(System.currentTimeMillis());
            params[i][2] = key;
        }
        if (keys.length>0)
        {
        	DatabaseMgr.getInstance().executeMultiStatement(null, sql, params);
        }
	}
}
