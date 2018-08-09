package com.carel.supervisor.remote.engine.impl;

import java.sql.Timestamp;
import java.util.StringTokenizer;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.remote.engine.master.ImpMaster;

public class ImpNote extends ImpMaster 
{
	public static final int NUM_COL = 9;
	
	public ImpNote(int idsite,String na,String db,String lang)
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
	
	private void insert(String idNote,String pvCode,String tname,String tid,String note,String user,
						String starttime,String lastupdate)
		throws Exception
	{
		String sql = "insert into hsnote values(?,?,?,?,?,?,?,?,?)";
		Object[] param = new Object[9];
		int idx = 0;
		
		Integer iNote = null;
		Integer iTId = null;
		Timestamp sta= null;
		Timestamp upd= null;
				
		try {
			iNote = new Integer(idNote);
			param[idx++] = iNote;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		param[idx++] = pvCode;
		param[idx++] = new Integer(this.idSite);
		param[idx++] = tname;
		
		try {
			iTId = new Integer(tid);
			param[idx++] = iTId;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
		param[idx++] = note;
		param[idx++] = user;
		
		try {
			sta = new Timestamp(Long.parseLong(starttime));
			param[idx++] = sta;
		}
		catch(Exception e) {
			param[idx++] = null;
		}
		
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
			update(iNote,pvCode,tname,iTId,note,user,sta,upd);
		}
	}
	
	private void update(Integer iNote,String pvCode,String name,Integer tid,String note,String user,Timestamp sta,Timestamp upd)
		throws Exception
	{
		
		String sql = "update hsnote set pvcode=?,tablename=?,tableid=?,note=?,usernote=?,starttime=?,lastupdate=?" +
					 " where idnote=? and idsite=?";
		
		Object[] param = {pvCode,name,tid,note,user,sta,upd,iNote,new Integer(this.idSite)};
		
		DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
	}
}
