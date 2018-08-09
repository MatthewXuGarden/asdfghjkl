package com.carel.supervisor.dataaccess.datalog.impl;

import java.util.Hashtable;
import java.util.Map;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class HsRelayBean 
{
	private static HsRelayBean ref = new HsRelayBean();
	private Map status = null;
	
	private HsRelayBean(){
		status = new Hashtable();
		load();
	}
	
	public static HsRelayBean getInstance() {
		return ref;
	}
	
	public synchronized void add(int idRelay)
	{
		Integer count = (Integer)status.remove(new Integer(idRelay));
		int iCount = 0;
		if(count == null)
		{
			count = new Integer(1);
			status.put(new Integer(idRelay),count);
			insert(idRelay);
		}
		else
		{
			iCount = count.intValue();
			iCount++;
			count = new Integer(iCount);
			status.put(new Integer(idRelay),count);
			update(idRelay,iCount);
		}
	}
	
	public synchronized boolean remove(int idRelay)
	{
		Integer count = (Integer)status.remove(new Integer(idRelay));
		boolean state = false;
		int iCount = 0;
		
		if(count != null)
		{
			iCount = count.intValue();
			iCount--;
			
			if(iCount > 0)
			{
				status.put(new Integer(idRelay),new Integer(iCount));
				update(idRelay,iCount);
			}
			else
			{
				state = true;
				delete(idRelay);
			}
		}
		return state;
	}
	
	public synchronized void reset(int[] idRelay)
	{
		for(int i=0; i<idRelay.length; i++)
			status.remove(new Integer(idRelay[i]));
		
		deleteMulti(idRelay);
	}
	
	private void load()
	{
		String sql = "select * from hsrelay";
		RecordSet rs = null;
		Record r = null;
		
		try
		{
			rs = DatabaseMgr.getInstance().executeQuery(null,sql);
			if(rs != null)
			{
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					if(r != null)
						status.put((Integer)r.get("idrelay"),(Integer)r.get("counter"));
				}
			}
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	private void insert(int idRelay)
	{
		String sql = "insert into hsrelay values(?,?)";
		
		try
		{
			DatabaseMgr.getInstance().executeStatement(null,sql,new Object[]{new Integer(idRelay),new Integer(1)});
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	private void update(int idRelay,int counter)
	{
		String sql = "update hsrelay set counter=? where idrelay=?";
		
		try
		{
			DatabaseMgr.getInstance().executeStatement(null,sql,new Object[]{new Integer(counter),new Integer(idRelay)});
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	private void delete(int idRelay)
	{
		String sql = "delete from hsrelay where idrelay=?";
		
		try
		{
			DatabaseMgr.getInstance().executeStatement(null,sql,new Object[]{new Integer(idRelay)});
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	private void deleteMulti(int[] idRelay)
	{
		String sql = "delete from hsrelay where idrelay in (";
		String param = "";
		Object[] par = new Object[idRelay.length];
		
		for(int i=0; i<idRelay.length; i++)
		{
			param += "?,";
			par[i] = new Integer(idRelay[i]);
		}
		param = param.substring(0,param.length()-1);
		sql = sql+param+")";
		
		try
		{
			DatabaseMgr.getInstance().executeStatement(null,sql,par);
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	public void reset()
	{
		String sql = "truncate hsrelay";
		try
		{
			DatabaseMgr.getInstance().executeStatement(null,sql,null);
			status = new Hashtable();
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}		
	}
}
