package com.carel.supervisor.remote.engine.master;

import java.io.LineNumberReader;
import java.sql.Timestamp;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;

public abstract class ImpMaster 
{
	protected static final String SEP = "$?";
	protected static final String NULL = "PV_NULL";
	protected static final String EMPTY = "PV_EMPTY";
	protected int idSite = 0;
	protected String db = "";
	protected String lang = "";
	protected String tabN = "";
	private LineNumberReader lnr = null;
	
	public ImpMaster(int idsite,String name,String db,String lang)
	{
		this.idSite = idsite;
		this.db = db;
		this.lang = lang;
		this.tabN = name;
		this.lnr = null;
	}
	
	public void setInpuStream(LineNumberReader r) {
		this.lnr = r;
	}
	
	public boolean importData()
	{
		boolean ris = true;
		String line = null;
		if(this.lnr != null) {
			try {
				if(clear()) {
					do
					{
						line = lnr.readLine();
						innerImport(line);
					}
					while(line != null);
				}
			}
			catch(Exception e) {
				ris = false;
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
		return ris;
	}
	
	protected void descriptionIns(int tabId,String desc,long time)
	{
		String sql = "insert into cftableext values(?,?,?,?,?,?,?,?)";
		Timestamp lupd = new Timestamp(time);
		Object[] param = {new Integer(this.idSite),this.lang,this.tabN,new Integer(tabId),desc,null,null,lupd};
		try {
			DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	protected void descriptionUpd(int tabId,String desc,long time)
	{
		String sql = "update cftableext set description=?,lastupdate=? where idsite=? and languagecode=? and " +
					"tablename=? and tableid=?";
		
		Timestamp lupd = new Timestamp(time);
		Object[] param = {desc,lupd,new Integer(this.idSite),this.lang,this.tabN,new Integer(tabId)};
		try {
			DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
	
	protected String decodeValue(String value)
	{
		if(value != null)
		{
			if(value.equalsIgnoreCase(NULL))
				value = null;
			else if(value.equalsIgnoreCase(EMPTY))
				value = "";
		}
		return value;
	}
	
	protected abstract void innerImport(String data);
	protected abstract boolean clear();
}
