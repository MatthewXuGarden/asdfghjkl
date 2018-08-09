package com.carel.supervisor.remote.engine.impl;

import java.sql.Timestamp;
import java.util.StringTokenizer;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.remote.engine.master.ImpMaster;

public class ImpEvent extends ImpMaster 
{
	public static final int NUM_COL = 9;
	
	public ImpEvent(int idsite,String na,String db,String lang)
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
					insert(arData[0],arData[1],arData[3],arData[4],arData[5],arData[6],arData[7],arData[8]);
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
	
	private void insert(String idEvent,String pvCode,String type,String category,String message,String user,
						String parameter,String lastupdate)
		throws Exception
	{
		String sql = "insert into hsevent values(?,?,?,?,?,?,?,?,?)";
		Object[] param = new Object[9];
		int idx = 0;
		
		Integer iEvent = null;
		Integer iType = null;
		Timestamp upd= null;
				
		try {
			iEvent = new Integer(idEvent);
			param[idx++] = iEvent;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		param[idx++] = pvCode;
		param[idx++] = new Integer(this.idSite);
		
		try {
			iType = new Integer(type);
			param[idx++] = iType;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		param[idx++] = category;
		param[idx++] = message;
		param[idx++] = user;
		param[idx++] = parameter;
		
		try {
			upd = new Timestamp(Long.parseLong(lastupdate));
			param[idx++] = upd;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		try {
			DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
		}
		catch(DataBaseException dbe) {
			update(iEvent,pvCode,iType,category,message,user,parameter,upd);
		}
	}
	
	private void update(Integer idEvent,String pvCode,Integer type,String category,String message,String user,String params,Timestamp upd)
		throws Exception
	{
		
		String sql = "update hsevent set pvcode=?,type=?,categorycode=?,messagecode=?,userevent=?,parameters=?,lastupdate=?" +
					 " where idevent=? and idsite=?";
		
		Object[] param = {pvCode,type,category,message,user,params,upd,idEvent,new Integer(this.idSite)};
		
		DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
	}
}
