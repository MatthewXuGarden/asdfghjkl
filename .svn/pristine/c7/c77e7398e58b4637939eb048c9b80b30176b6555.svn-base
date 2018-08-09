package com.carel.supervisor.remote.engine.impl;

import java.sql.Timestamp;
import java.util.StringTokenizer;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.remote.engine.master.ImpMaster;

public class ImpAlarm extends ImpMaster 
{
	public static final int NUM_COL = 16;
	
	public ImpAlarm(int idsite,String na,String db,String lang)
	{
		super(idsite,na,db,lang);
	}
	
	protected void innerImport(String data)
	{
		String[] arData = null;
		int idx = 0;
		
		if(data != null)
		{
			if(data.endsWith(SEP))
				data = data.substring(0,data.length()-SEP.length());
			
			StringTokenizer st = new StringTokenizer(data,SEP);
			if(st != null)
			{
				arData = new String[st.countTokens()];
				while(st.hasMoreTokens()){
					arData[idx] = decodeValue((String)st.nextToken());
					idx++;
				}
			}
			
			if(arData != null && arData.length == NUM_COL) {
				try {
					insert(arData[0],arData[1],arData[3],arData[4],arData[5],arData[6],arData[7],arData[8],
						   arData[9],arData[10],arData[11],arData[12],arData[13],arData[14],arData[15]);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected boolean clear() {
		return true;
	}
	
	private void insert(String idAlarm,String pvCode,String idDevice,String idVariable,String priority,String isLogic,
						String starttime,String endtime,String ackuser,String acktime,String deluser,String deltime,
						String resetuser,String resettime,String lastupdate)
		throws Exception
	{
		String sql = "insert into hsalarm values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] param = new Object[16];
		int idx = 0;
		
		Integer iAlarm = null;
		Integer iDevice = null;
		Integer iVariable = null;
		Timestamp start = null;
		Timestamp end = null;
		Timestamp ack = null;
		Timestamp del = null;
		Timestamp res = null;
		Timestamp up = null;
		
		try {
			iAlarm = new Integer(idAlarm);
			param[idx++] = iAlarm;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		param[idx++] = pvCode;
		param[idx++] = new Integer(this.idSite);
		
		try {
			iDevice = new Integer(idDevice);
			param[idx++] = iDevice;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		try {
			iVariable = new Integer(idVariable);
			param[idx++] = iVariable;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		param[idx++] = priority;
		param[idx++] = isLogic;
		
		try {
			start = new Timestamp(Long.parseLong(starttime));
			param[idx++] = start;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		try {
			end = new Timestamp(Long.parseLong(endtime));
			param[idx++] = end;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		param[idx++] = ackuser;
		
		try {
			ack = new Timestamp(Long.parseLong(acktime));
			param[idx++] = ack;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		param[idx++] = deluser;
		
		try {
			del = new Timestamp(Long.parseLong(deltime));
			param[idx++] = del;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		param[idx++] = resetuser;
		
		try {
			res = new Timestamp(Long.parseLong(resettime));
			param[idx++] = res;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		try {
			up = new Timestamp(Long.parseLong(lastupdate));
			param[idx++] = up;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
					
		try {
			DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
		}
		catch(DataBaseException dbe) {
			update(iAlarm,pvCode,iDevice,iVariable,priority,isLogic,start,end,ackuser,ack,deluser,del,resetuser,res,up);
		}
	}
	
	private void update(Integer idAlarm,String pvCode,Integer idDevice,Integer idVar,String prio,String isLogic,Timestamp start,
			Timestamp end,String ackuser,Timestamp ack,String deluser,Timestamp del,String resuser,Timestamp res,Timestamp upd)
		throws Exception
	{
		
		String sql = "update hsalarm set pvcode=?,iddevice=?,idvariable=?,priority=?,islogic=?,starttime=?,endtime=?," +
					 "ackuser=?,acktime=?,delactionuser=?,delactiontime=?,resetuser=?,resettime=?,lastupdate=? " + 
					 "where idalarm=? and idsite=?";
		
		Object[] param = {pvCode,idDevice,idVar,prio,isLogic,start,end,ackuser,ack,deluser,del,resuser,res,upd,
						  idAlarm,new Integer(this.idSite)};
		
		DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
	}
}
