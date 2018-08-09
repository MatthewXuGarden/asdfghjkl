package com.carel.supervisor.controller;

import java.util.HashSet;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class VarCmd
{
	private HashSet<Integer> map = new HashSet<Integer>();
	private static VarCmd me = new VarCmd();
	
    private VarCmd()
    {
    	loadVars();
    }
    
    private void loadVars()
    {
    	try
    	{
    		String sql = "select idvarmdl from cfvarcmd";
    		RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql);
    		Integer id = null;
    		for(int i = 0; i < recordset.size(); i++)
    		{
    			id = (Integer)recordset.get(i).get(0);
    			map.add(id);
    		}
    	}
    	catch(Exception e)
    	{
    		LoggerMgr.getLogger(this.getClass()).error(e);
    	}
    }
    
    public static VarCmd getInstance()
    {
    	return me;
    }
    
    public void refreshVars()
    {
    	map.clear();
    	loadVars();
    }
    
    public boolean contains(Integer id)
    {
    	return map.contains(id);
    }
    
}
