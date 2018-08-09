package com.carel.supervisor.presentation.ac;

import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class AcSlaveMasterPres
{
	private Map<Integer,Integer> slaveMap = null;
	
	
	public AcSlaveMasterPres() throws DataBaseException
	{
		String sql = "select iddevslave,iddevmaster from ac_slave";
	    RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
	    
	    Integer idSlave = null;
	    Integer idMaster = null;
	    Record r = null;
	    if (rs!=null)
	    {
	    	slaveMap = new HashMap<Integer,Integer>();
	    	for (int i=0;i<rs.size();i++)
	    	{
	    		r = rs.get(i);
	    		idSlave = (Integer) r.get(0);
	    		idMaster = (Integer) r.get(1);
	    		if (!slaveMap.containsKey(idSlave))
	    		{
	    			slaveMap.put(idSlave,idMaster);
	    		}
	    	}
	    }
	    
	}
	
	public int getMasterId(int idSlave)
	{
		Integer res = slaveMap.get(idSlave);
		if (res!=null)
			return res;
		else
		return -1;
	}
	
}
