package com.carel.supervisor.presentation.polling;


import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class TimeSiteList {
	
	protected TimeSite[] timeInfo = null;
    protected Map timeById = new HashMap();
	
	public static TimeSite[] retriveTimeSiteJoin() throws DataBaseException
    {
    	String sql = "select rmtimesite.id,rmtimetable.name as namef,cfsite.name as names,status from rmtimesite inner join cfsite on cfsite.idsite = rmtimesite.idsite " +
    			" inner join rmtimetable on rmtimetable.idrmtimetable = rmtimesite.idrmtimetable where cfsite.idsite > 1 order by cfsite.idsite";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
    	
    	TimeSite[] fasce = new TimeSite[rs.size()];
    	for (int i=0;i<rs.size();i++)
    	{
    		fasce[i] = new TimeSite(rs.get(i),true);
    	}
    	
    	return fasce;
    }
}
