package com.carel.supervisor.remote.bean;

import java.sql.Timestamp;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class SynchBeanList 
{
	public static String[][] getTableSynch(String db)
	{
		String sql = "select * from rmtablesynch order by idtable";
		String[][] lista = null;
		
		try 
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(db,sql,null);
			Record r = null;
			if(rs != null)
			{
				lista = new String[rs.size()][2];
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					if(r != null)
					{
						lista[i][0] = UtilBean.trim(r.get("idtable"));
						lista[i][1] = UtilBean.trim(r.get("tablename"));
					}
				}
			}
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(SynchBeanList.class);
			logger.error(e);
		}
		return lista;
	}
	
	public static void insTableConf(String db,int idsite,int idtable)
	{
		String sql = "insert into rmtableconf values(?,?,?)";
		Object[] param = {new Integer(idsite),new Integer(idtable),new Timestamp(System.currentTimeMillis())};
		try {
			DatabaseMgr.getInstance().executeStatement(db,sql,param);
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(SynchBeanList.class);
			logger.error(e);
		}
	}
	
	public static String[] getTableConf(String db,int idsite)
	{
		String sql = "select rmtablesynch.nametable as tname from rmtablesynch,rmtableconf "+
					 "where rmtableconf.idsite=? and rmtablesynch.idtable=rmtableconf.idtable";
		String[] lista = new String[0];
		Object[] param = {new Integer(idsite)};
		
		try 
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(db,sql,param);
			Record r = null;
			if(rs != null)
			{
				lista = new String[rs.size()];
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					if(r != null)
						lista[i]= UtilBean.trim(r.get("tname"));
				}
			}
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(SynchBeanList.class);
			logger.error(e);
		}
		
		return lista;
	}
	
	public static String[] getTableId(String db,int idsite)
	{
		String sql = "select idtable from rmtableconf where idsite = ?";
		String[] lista = new String[0];
		Object[] param = {new Integer(idsite)};
		
		try 
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(db,sql,param);
			Record r = null;
			if(rs != null)
			{
				lista = new String[rs.size()];
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					if(r != null)
						lista[i]= r.get("idtable").toString();
				}
			}
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(SynchBeanList.class);
			logger.error(e);
		}
		
		return lista;
	}
	
	public static void delTableConf(String db,int idsite)
	{
		String sql = "delete from rmtableconf where idsite=?";
		Object[] param = {new Integer(idsite)};
		try {
			DatabaseMgr.getInstance().executeStatement(db,sql,param);
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(SynchBeanList.class);
			logger.error(e);
		}
	}
}
