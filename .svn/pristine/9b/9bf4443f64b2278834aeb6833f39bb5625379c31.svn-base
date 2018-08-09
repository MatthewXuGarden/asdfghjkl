package com.carel.supervisor.remote.engine.impl;

import java.sql.Timestamp;
import java.util.StringTokenizer;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.remote.engine.master.ImpMaster;


public class ImpGroup extends ImpMaster 
{
	public static final int NUM_COL = 7;
	public ImpGroup(int idsite,String na,String db,String lang)
	{
		super(idsite,na,db,lang);
	}
	
	protected void innerImport(String data)
	{
		String[] arData = null;
		int idx = 0;
		int idTab = 0;
		long time = 0;
		
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
					insert(arData[0],arData[2],arData[3],arData[4],arData[5]);
					try{
						try {
							idTab = Integer.parseInt(arData[0]);
						}
						catch(Exception e){}
						try {
							time = Long.parseLong(arData[5]);
						}
						catch(Exception e){}
						descriptionIns(idTab,arData[6],time);
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
		String sql = "delete from cfgroup where idsite=?";
		Object[] param = new Object[]{new Integer(this.idSite)};
		try {
			DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
			sql = "delete from cftableext where idsite=? and tablename=?";
			param = new Object[]{new Integer(this.idSite),"cfgroup"};
			DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
			ris = true;
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}
	
	private void insert(String idGroup,String idArea,String code,String isGlob,String lastUp)
		throws Exception
	{
		String sql = "insert into cfgroup values(?,?,?,?,?,?)";
		Object[] param = new Object[6];
		Integer iGrop = null;
		Integer iArea = null;
		long lLast = 0;
		
		try {
			iGrop = new Integer(idGroup);
			param[0] = iGrop;
		}
		catch(Exception e){param[0] = null;}
		
		param[1] = new Integer(this.idSite);
		
		try {
			iArea = new Integer(idArea);
			param[2] = iArea;
		}
		catch(Exception e){param[2] = null;}
		
		param[3] = code;
		param[4] = isGlob;
		
		try {
			lLast = Long.parseLong(lastUp);
		}
		catch(Exception e){
			lLast = System.currentTimeMillis();
		}
		
		param[5] = new Timestamp(lLast);
		
		try {
			DatabaseMgr.getInstance().executeStatement(this.db,sql,param);
		}
		catch(Exception e){
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
	}
}
