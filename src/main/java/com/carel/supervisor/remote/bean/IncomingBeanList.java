package com.carel.supervisor.remote.bean;

import java.sql.Timestamp;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class IncomingBeanList 
{
	public static void removeAllDeviceForIncoming(String db,int idsite)
		throws Exception
	{
		String sql = "delete from cfincoming where idsite=?";
		Object[] param = {new Integer(idsite)};
		DatabaseMgr.getInstance().executeStatement(db,sql,param);
	}
	
	public static void disableAllDeviceForIncoming(String db,int idsite)
		throws Exception
	{
		String sql = "update cfincoming set state=? where idsite=?";
		Object[] param = {"FALSE",new Integer(idsite)};
		DatabaseMgr.getInstance().executeStatement(db,sql,param);
	}
	
	public static void updateDeviceForIncoming(String db,int idsite,String device)
		throws Exception
	{
		String sql = "update cfincoming set state=? where idsite=? and idmodem=?";
		Object[] param = {"TRUE",new Integer(idsite),device};
		DatabaseMgr.getInstance().executeStatement(db,sql,param);
	}
	
	public static void insertDeviceConfiged(String db,int idsite,String device)
		throws Exception
	{
		String sql = "insert into cfincoming values(?,?,?,?,?)";
		Timestamp t = new Timestamp(System.currentTimeMillis());
		Object[] param = {new Integer(idsite),device,"TRUE",t,t};
		DatabaseMgr.getInstance().executeStatement(db,sql,param);
	}
	
	public static void insertDeviceForConfig(String db,int idsite,String device)
		throws Exception
	{
		String sql = "insert into cfincoming values(?,?,?,?,?)";
		Timestamp t = new Timestamp(System.currentTimeMillis());
		Object[] param = {new Integer(idsite),device,"FALSE",t,t};
		DatabaseMgr.getInstance().executeStatement(db,sql,param);
	}
	
	public static IncomingBean[] getDisableIncomingDevice(String db) {
		return prvGetIncomingDevice(db,"FALSE");
	}
	
	public static IncomingBean[] getEnableIncomingDevice(String db) {
		return prvGetIncomingDevice(db,"TRUE");
	}
	
	private static IncomingBean[] prvGetIncomingDevice(String db,String state)
	{
		IncomingBean[] inBean = null;
		Object[] param = null;
		String sql = "select * from cfincoming";
		if(state != null) {
			sql += " where state=?";
			param = new Object[]{state};
		}
				
		try 
		{
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(db,sql,param);
			if(rs != null)
			{
				inBean = new IncomingBean[rs.size()];
				for(int i=0; i<rs.size(); i++)
					inBean[i] = new IncomingBean(rs.get(i));
			}
		}
		catch(Exception e){
			inBean = new IncomingBean[0];
		}
		
		return inBean;
	}
}
