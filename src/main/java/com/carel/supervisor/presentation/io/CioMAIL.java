package com.carel.supervisor.presentation.io;

import java.sql.Timestamp;

import com.carel.supervisor.base.conversion.StringUtility;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dispatcher.comm.layer.DispLayMail;
import com.carel.supervisor.dispatcher.comm.layer.DispLayer;

public class CioMAIL
{
	private int idconf = 0;
	private int idsite = 0;
	private String smtp = "";
	private int port = 25;
	private String sender = "";
	private String provider = "";
	private String type = "";
	private int trynum = 0;
	private int retrynum = 0;
	
	private String user = "";
	private String pass = "";
	
	private String encryption = "NONE"; // could be NONE,SSL,TLS
	
	public CioMAIL(int idsite) {
		this.idsite = idsite;
	}
	
	public void loadConfiguration()
	{
		String sql = "select * from cfioemail where idsite=?";
		Record r = null;
		Object[] param = {new Integer(this.idsite)};
		try 
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
			if(rs != null && rs.size() > 0)
			{
				r = rs.get(0);
				this.idconf = ((Integer)r.get("idemail")).intValue();
				this.smtp = UtilBean.trim(r.get("smtp"));
				this.port = ((Integer)r.get("port")).intValue();
				this.sender = UtilBean.trim(r.get("sender"));
				this.type = UtilBean.trim(r.get("type"));
				this.provider = UtilBean.trim(r.get("provider"));
				this.trynum = ((Integer)r.get("retrynumber")).intValue();
				this.retrynum = ((Integer)r.get("retryafter")).intValue();
				this.user = UtilBean.trim(r.get("smtpuser"));
				this.pass = UtilBean.trim(r.get("smtppass"));
				this.encryption = UtilBean.trim(r.get("encryption"));
			}
			else
			{
				this.idconf = -1;
				this.smtp = "";
				this.port = 25;
				this.sender = "";
				this.type = "";
				this.provider = "";
				this.trynum = -1;
				this.retrynum = -1;
				this.user = "";
				this.pass = "";
				encryption = "NONE";
			}
		}
		catch(Exception e) 
		{
			Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
		}
	}
	
	
	public boolean saveConfiguration(int id,String smtp,String sender,String type,String provider,int trynum,int retry)
	{
		String sql = "";
		Object[] values = null;
		Timestamp curtime = new Timestamp(System.currentTimeMillis());
		boolean done = true;
		String[] param = null;
		String modemP = "";
		
		try 
		{
			param = StringUtility.split(provider,"|");
			provider = param[0];
			provider = provider.trim();
			modemP = param[1];
			modemP = modemP.trim();
		}
		catch(Exception e) 
		{
			provider = "";
			modemP = "";
		}
		
		if(id > 0)
		{
			sql = "update cfioemail set modemid=?,smtp=?,sender=?,type=?,provider=?,retrynumber=?,retryafter=?,lastupdate=?,smtpuser=?,smtppass=?,encryption=?,port=? where idemail=? and idsite=?";
			values = new Object[]{modemP,smtp,sender,type,provider,new Integer(trynum),new Integer(retry),curtime,this.user,this.pass,this.encryption,this.port,new Integer(id),new Integer(this.idsite)};
		}
		else
		{
			sql = "insert into cfioemail values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			Integer key;
			try 
			{
				key = SeqMgr.getInstance().next(null,"cfiomail","idmail");
				values = new Object[]{key,new Integer(this.idsite),modemP,"A",
									  smtp,sender,type,provider,new Integer(trynum),new Integer(retry),
									  curtime,curtime,this.user,this.pass,this.encryption,this.port};
			}
			catch (DataBaseException e) 
			{
				Logger logger = LoggerMgr.getLogger(this.getClass());
	            logger.error(e);
				done = false;
			}
		}
		
		if(done) 
		{
			try 
			{
				DatabaseMgr.getInstance().executeStatement(null, sql, values);
			} 
			catch (DataBaseException e) 
			{
				Logger logger = LoggerMgr.getLogger(this.getClass());
	            logger.error(e);
				done = false;
			}
		}
		return done;
	}
	
	public int getIdConf() {
		return this.idconf;
	}
	
	public String getSmtp() {
		return this.smtp;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public String getType() {
		return type;
	}
	
	public String getProviderId() {
		return this.provider;
	}
	
	public String getSender() {
		return this.sender;
	}
	
	public int getTrynum() {
		return trynum;
	}
	
	public int getRetrynum() {
		return retrynum;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String[][] getProvider()
	{
		DispLayer layer = new DispLayMail();
		return layer.getFisicChannel("E");
	}
	
	public String getEncryption() {
		return encryption;
	}

	public void setEncryption(String encryption) {
		this.encryption = encryption;
	}
	
}
