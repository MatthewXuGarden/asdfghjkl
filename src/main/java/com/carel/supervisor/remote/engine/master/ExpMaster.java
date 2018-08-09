package com.carel.supervisor.remote.engine.master;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;

public abstract class ExpMaster 
{
	protected static final String SEP = "$?";
	protected static final String NULL = "PV_NULL";
	protected static final String EMPTY = "PV_EMPTY";
	private String sPath= "";
	private String lang = "";
	private String db = "";
	private String tab = "";
	private long time = 0;
	private long last = 0;
	
	public ExpMaster(String db,String lang,String tab,long last) {
		this.lang = lang;
		this.db = db;
		this.tab = tab;
		this.last = last;
		this.time = System.currentTimeMillis();
	}
	
	public void setPath(String path) {
		this.sPath = path;
	}
	
	public boolean export(long timeElab)
	{
		boolean ris = false;
		this.time = timeElab;
		Connection con = null;
		try {
			con = DatabaseMgr.getInstance().getConnection(this.db);
			innerExport(con,this.lang,last);
			ris = true;
			DatabaseMgr.getInstance().closeConnection(this.db,con);
		}
		catch(Exception e) {
			try {
				if(con != null)
					DatabaseMgr.getInstance().closeConnection(db,con);
			}
			catch(Exception a) {
			}
		}
		return ris;
	}
	
	protected OutputStream openOutput()
	{
		File f = null;
		ZipOutputStream zipOut = null;
		
		try {
			f = new File(this.sPath);
			if(!f.exists())
				f.mkdirs();
			
			zipOut = new ZipOutputStream(new BufferedOutputStream(
										 new FileOutputStream(this.sPath+this.time+"_"+this.tab+".zip")));
	        zipOut.setLevel(9);
	        zipOut.putNextEntry(new ZipEntry(this.tab));
		}
		catch(Exception e) {
			zipOut = null;
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return zipOut;
	}
	
	protected void closeOutput(OutputStream out)
	{
		if(out != null)
		{
			try {
				out.flush();
				out.close();
			}
			catch(Exception e) {
				Logger logger = LoggerMgr.getLogger(this.getClass());
				logger.error(e);
			}
		}
	}
	
	protected String decodeColumValue(int colType,String colName,ResultSet rs)
		throws Exception
	{
		String ret = "";
		Timestamp time = null;
		
		switch(colType)
		{
			case Types.CHAR:
			case Types.VARCHAR:
				ret = rs.getString(colName);
				if(ret != null)
					ret = (ret.trim().length()==0?EMPTY:ret.trim());
				else
					ret = NULL;
				break;
				
			case Types.INTEGER:
			case Types.NUMERIC:
				try {
					ret = String.valueOf(rs.getInt(colName));
				}
				catch(Exception e){
					ret = NULL;
				}
				break;
				
			case Types.TIMESTAMP:
				time = rs.getTimestamp(colName);
				if(time != null)
					ret = String.valueOf(time.getTime());
				else
					ret = NULL;
				break;
		}
		
		return ret+SEP;
	}
	
	protected abstract void innerExport(Connection con,String lang,long lastConn);
}
