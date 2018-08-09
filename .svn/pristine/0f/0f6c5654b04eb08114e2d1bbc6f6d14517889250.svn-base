package com.carel.supervisor.controller.rule;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class RemoteRuleHelper
{
    private static RemoteRuleHelper me = new RemoteRuleHelper();
    private Map status = new HashMap();
    
	private RemoteRuleHelper()
    {
    }
	
	public static RemoteRuleHelper getInstance()
	{
		return me;
	}
	
	public void load() throws Exception
	{
		String sql = "select * from cfruleremote";
		RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql);
		Record record = null;
		Integer idRule = null;
		Timestamp time = null;
		for(int i = 0; i < recordset.size(); i++)
		{
			record = recordset.get(i);
			idRule = (Integer)record.get(0);
			time = (Timestamp)record.get(1);
			status.put(idRule, time);
		}
	}
	
	public void refresh(Integer idRule, Date now) throws Exception
	{
		String sql = "update cfruleremote set lastcheck = ? where idrule = ?";
		DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{now,idRule});
		status.put(idRule, (Timestamp)now);
	}
	
	public Timestamp getLastCheck(Integer idRule) throws Exception
	{
		Timestamp time = (Timestamp)status.get(idRule);
		if (null == time) //Significa che non ho nessuna regola 
		{
			time = new Timestamp(System.currentTimeMillis());
			String sql = "insert into cfruleremote values (?,?)";
			DatabaseMgr.getInstance().executeStatement(null, sql, new Object[]{idRule,time});
			status.put(idRule, (Timestamp)time);
		}
		return time;
	}
	
	public boolean existSomething(Integer idRule) throws Exception
	{
		Timestamp time = getLastCheck(idRule);
		String sql = "select count(1) from hsalarm where lastupdate > ?";
		RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{time});
		if (((Integer)recordset.get(0).get(0)).intValue() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
