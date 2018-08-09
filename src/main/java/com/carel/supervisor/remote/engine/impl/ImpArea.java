package com.carel.supervisor.remote.engine.impl;

import java.sql.Timestamp;
import java.util.StringTokenizer;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.remote.engine.master.ImpMaster;


public class ImpArea extends ImpMaster 
{
	public static final int NUM_COL = 6;
	public ImpArea(int idsite,String na,String db,String lang)
	{
		super(idsite,na,db,lang);
	}
	
	protected void innerImport(String data)
	{
		String[] arData = null;
		int idx = 0;
		int idArea = 0;
		long time =0;
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
					insert(arData[0],arData[2],arData[3],arData[4]);
					try{
						try{
							idArea = Integer.parseInt(arData[0]);
						}
						catch(Exception e){}
						try{
							time = Long.parseLong(arData[4]);
						}
						catch(Exception e){}
						
						descriptionIns(idArea,arData[5],time);
					}
					catch(Exception e){
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected boolean clear()
	{
		boolean ris = false;
		String sql = "delete from cfarea where idsite=?";
		Object[] param = {new Integer(this.idSite)};
		try {
			DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
			sql = "delete from cftableext where idsite=? and tablename=?";
			param = new Object[]{new Integer(this.idSite),"cfarea"};
			DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
			ris = true;
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}
	
	private void insert(String idArea,String code,String isGlob,String lastUp)
		throws Exception
	{
		Integer iArea = null;
		long lLast = 0;
		Object[] param = new Object[5];
		
		try {
			iArea = new Integer(idArea);
		}
		catch(Exception e){
		}
		param[0] = iArea;
		param[1] = new Integer(this.idSite);
		param[2] = code;
		param[3] = isGlob;
		try {
			lLast = Long.parseLong(lastUp);
		}
		catch(Exception e) {
			lLast = System.currentTimeMillis();
		}
		param[4] = new Timestamp(lLast);
		
		String sql = "insert into cfarea values(?,?,?,?,?)";
		DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
	}
}
