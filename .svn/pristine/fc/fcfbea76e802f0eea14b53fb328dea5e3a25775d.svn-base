package com.carel.supervisor.presentation.io;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dispatcher.comm.layer.DispLayFax;
import com.carel.supervisor.dispatcher.comm.layer.DispLayer;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;

public class CioFAX 
{
	private int idconf = 0;
	private int idsite = 0;
	private String modem = "";
	private String type = "";
	private int trynum = 0;
	private int retrynum = 0;
	private String centralino = "";
	
	public static final String INTERNAL_MODEM = "Internal_modem";
	
	public CioFAX(int idsite) {
		this.idsite = idsite;
	}
	
	public void loadConfiguration()
	{
		String sql = "select * from cfiofax where idsite=?";
		Record r = null;
		Object[] param = {new Integer(this.idsite)};
		try 
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,param);
			if(rs != null && rs.size() > 0)
			{
				r = rs.get(0);
				this.idconf = ((Integer)r.get("idfax")).intValue();
				this.modem = UtilBean.trim(r.get("modemid"));
				this.type = UtilBean.trim(r.get("modemtype"));
				this.trynum = ((Integer)r.get("trynumber")).intValue();
				this.retrynum = ((Integer)r.get("retryafter")).intValue();
				this.centralino = UtilBean.trim(r.get("centralino"));
			}
			else
			{
				this.idconf = -1;
				this.modem = "";
				this.type = "";
				this.trynum = -1;
				this.retrynum = -1;
				this.centralino = "";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getCentralino() {
		return this.centralino;
	}
	
	public int getIdconf() {
		return idconf;
	}

	public String getModemId() {
		return this.modem;
	}

	public int getRetrynum() {
		return retrynum;
	}

	public int getTrynum() {
		return trynum;
	}

	public String getType() {
		return type;
	}

	public String[][] getModem()
	{
		DispLayer layer = new DispLayFax();
		return layer.getFisicChannel("F");
	}
	public boolean checkMedemExist(String modem)
	{
		String[][] modems = getModem();
		for(int i=0;i<modems.length;i++)
		{
			if(modem.equals(modems[i][0]))
				return true;
		}
		return false;
	}
	public boolean saveConfiguration(int id,String modem,String type,int trynum,int retry,String centra)
	{
		String sql = "";
		Object[] values = null;
		Timestamp curtime = new Timestamp(System.currentTimeMillis());
		boolean done = true;
		
		if(id > 0)
		{
			sql = "update cfiofax set modemid=?,modemtype=?,trynumber=?,retryafter=?,centralino=?,lastupdate=? where idfax=? and idsite=?";
			values = new Object[]{modem,type,new Integer(trynum),new Integer(retry),centra,curtime,new Integer(id),new Integer(this.idsite)};
		}
		else
		{
			sql = "insert into cfiofax values(?,?,?,?,?,?,?,?,?)";
			Integer key;
			try {
				key = SeqMgr.getInstance().next(null,"cfiofax","idfax");
				values = new Object[]{key,new Integer(this.idsite),modem,type,new Integer(trynum),new Integer(retry),centra,curtime,curtime};
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
	
	public boolean removeConfiguration(int id)
	{
		String sql = "";
		Object[] values = null;
		
		boolean done = true;
		
		try
		{
			//controllo esistenza azione fax:
			done = !(ActionBeanList.existsActionType(this.idsite, "F"));
		}
		catch (DataBaseException e1)
		{
			done = false;
			e1.printStackTrace();
		}
		
		if((id > 0) && (done))
		{
			sql = "delete from cfiofax where idfax=? and idsite=?";
			values = new Object[]{new Integer(id),new Integer(this.idsite)};
		
			try
			{
				DatabaseMgr.getInstance().executeStatement(null, sql, values);
			}
			catch (DataBaseException e)
			{
				e.printStackTrace();
				done = false;
			}
		}
		
		return done;
	}
	public void setTestResult(int idaddrbook, boolean isTestSuccessful)
	{
		String sql = "update cfaddrbook set ioteststatus=? where idaddrbook=?";
		Object[] values = null;
		try
		{
			if(isTestSuccessful == true)
			{
				values = new Object[]{"OK",new Integer(idaddrbook)};	
			}
			else
			{
				values = new Object[]{"FAIL", new Integer(idaddrbook)};
			}
			DatabaseMgr.getInstance().executeStatement(null, sql,values);
		}
		catch (DataBaseException e)
		{
			e.printStackTrace();
		}
	}
}
