package com.carel.supervisor.remote.engine.impl;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.remote.engine.master.ExpMaster;

public class ExpDevice extends ExpMaster
{
	private static final String SQL = 
		"select cfdevice.iddevice,cfdevmdl.code as devmdlcode,cfdevice.idsite,cfdevice.islogic,cfdevice.iddevmdl," +
		"cfdevice.idline,cfdevice.address,cfdevice.littlendian,cfdevice.code,cfdevice.imagepath,cfdevice.idgroup," +
		"cfdevice.globalindex,cfdevice.isenabled,cfdevice.iscancelled,cfdevice.inserttime,cfdevice.lastupdate,cftableext.description " +
		"from cfdevmdl,cfdevice,cftableext " +
		"where cfdevmdl.iddevmdl = cfdevice.iddevmdl and cfdevice.iddevice = cftableext.tableid and cftableext.tablename='cfdevice' " +
		" and cftableext.idsite=? and cftableext.languagecode=? and cfdevice.idsite=? and (cfdevice.lastupdate > ? or cftableext.lastupdate >? )";
		
	public ExpDevice(String db,String lang,String tab,long last)
	{
		super(db,lang,tab,last);
	}
	
	protected void innerExport(Connection con,String lang,long last)
	{
		OutputStream out = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		StringBuffer sb = new StringBuffer();
		String decodeVal = "";
		
		try
		{
			ps = con.prepareStatement(SQL);
			if(ps != null)
			{
				ps.setInt(1,1);
				ps.setString(2,lang);
				ps.setInt(3,1);
				ps.setTimestamp(4,new Timestamp(last));
				ps.setTimestamp(5,new Timestamp(last));
				
				rs = ps.executeQuery();
				if(rs != null)
				{
					out = this.openOutput();
										
					try
					{
						meta = rs.getMetaData();
						
						while(rs.next())
						{
							for(int j=1; j<=meta.getColumnCount(); j++) {
								decodeVal = decodeColumValue(meta.getColumnType(j),meta.getColumnName(j),rs);
								sb.append(decodeVal);
							}
							sb.append("\n");
							try {
								out.write(sb.toString().getBytes("UTF-8"));
								sb = new StringBuffer();
							}
							catch(Exception e) {
							}
						}
					}
					catch(Exception e) {
						e.printStackTrace();
						Logger logger = LoggerMgr.getLogger(this.getClass());
						logger.error(e);
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		this.closeOutput(out);
	}
}
