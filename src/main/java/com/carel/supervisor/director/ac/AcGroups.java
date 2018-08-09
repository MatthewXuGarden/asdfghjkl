package com.carel.supervisor.director.ac;

import java.util.HashMap;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class AcGroups {

	private static HashMap<Integer,String> groups = null;
	
	public static String getGroupName(Integer iddev)
	{
		String groupName = "***";
		
		if (iddev != null)
		{
			if (groups == null)
				loadGroups();
			
			if (groups != null)
			{
				groupName = groups.get(iddev);
			}
		}
		
		return groupName;
	}
	
	public static void loadGroups()
	{
		String sql = "select * from ac_groups order by iddevmaster";
		
		RecordSet rs = null;
		Record rec = null;
		
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null, sql);
			
			if ((rs != null) && (rs.size() > 0))
			{
				groups = new HashMap<Integer,String>();
				
				for (int i=0; i<rs.size(); i++)
				{
					rec = rs.get(i);
					Integer idd = (Integer)rec.get("iddevmaster");
					String name = (String)rec.get("descr_grp");
					
					groups.put(idd, name);
				}
			}
		}
		catch (Exception e)
		{
			// PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcGroups.class.getName());
            logger.error(e);
		}
	}
	
}
