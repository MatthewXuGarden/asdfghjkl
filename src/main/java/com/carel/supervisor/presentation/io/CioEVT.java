package com.carel.supervisor.presentation.io;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;

public class CioEVT
{
	private int idconf = 0;
	private int idsite = 0;
	private String pathSound = "";
	private String ioteststatus = "";
	
	public CioEVT(int idsite) {
		this.idsite = idsite;
	}
		
	public void loadConfiguration()
	{
		String sql = "select * from cfioevent where idsite=?";
		Record r = null;
		Object[] param = {new Integer(this.idsite)};
		try 
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
			if(rs != null && rs.size() > 0)
			{
				r = rs.get(0);
				this.idconf = ((Integer)r.get("idevent")).intValue();
				this.pathSound = UtilBean.trim(r.get("pathsound"));
			}
			else
			{
				this.idconf = -1;
				this.pathSound = "";
				this.ioteststatus = "";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void loadTestStatus()
	{
		String sql = "select ioteststatus from cfioevent where idsite=-888 and idevent=-888";
		Record r = null;
		try 
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
			if(rs != null && rs.size() > 0)
			{
				r = rs.get(0);
				this.ioteststatus = UtilBean.trim(r.get("ioteststatus"));
			}
			else
			{
				this.ioteststatus = "";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public boolean saveConfiguration(int id,String path)
	{
		String sql = "";
		Object[] values = null;
		Timestamp curtime = new Timestamp(System.currentTimeMillis());
		boolean done = true;
		
		if(id > 0)
		{
			sql = "update cfioevent set pathsound=?,lastupdate=? where idevent=? and idsite=?";
			values = new Object[]{path,curtime,new Integer(id),new Integer(this.idsite)};
		}
		else
		{
			sql = "insert into cfioevent values(?,?,?,?,?)";
			Integer key;
			try {
				key = SeqMgr.getInstance().next(null,"cfioevent","idevent");
				values = new Object[]{key,new Integer(this.idsite),path,curtime,curtime};
			}
			catch (DataBaseException e) {
				e.printStackTrace();
				 done = false;
			}
		}
		
		if(done) {
			try {
				DatabaseMgr.getInstance().executeStatement(null, sql, values);
			} catch (DataBaseException e) {
				e.printStackTrace();
				 done = false;
			}
		}
		
		return done;
	}
	
	public int getIdconf() {
		return idconf;
	}

	public String getPathSound() {
		return this.pathSound;
	}
	public String getIoteststatus()
	{
		return this.ioteststatus;
	}
	public void setTestResult(boolean isTestSuccessful)
	{
		String sql = "";
		Object[] values = null;
		Timestamp curtime = new Timestamp(System.currentTimeMillis());

		sql = "select * from cfioevent where idevent=-888 and idsite=-888";
		Record r = null;
		try 
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql);
			if(rs != null && rs.size() > 0)
			{
				sql = "update cfioevent set ioteststatus=? where idevent=-888 and idsite=-888";
				if(isTestSuccessful == true)
				{
					values = new Object[]{"OK"};	
				}
				else
				{
					values = new Object[]{"FAIL"};
				}
					DatabaseMgr.getInstance().executeStatement(null, sql,values);
			}
			else
			{
				sql = "insert into cfioevent values(-888,-888,'',?,?,?)";
				if(isTestSuccessful == true)
				{
					values = new Object[]{curtime,curtime,"OK"};	
				}
				else
				{
					values = new Object[]{curtime,curtime,"FAIL"};
				}
					DatabaseMgr.getInstance().executeStatement(null, sql,values);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
