package com.carel.supervisor.remote.engine.impl;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;

import com.carel.supervisor.remote.engine.master.ExpMaster;

public class ExpEvent extends ExpMaster 
{
	private static final String SQL = "select * from hsevent where lastupdate > ? and idsite = ?";
	
	public ExpEvent(String db,String lang,String tab,long last)
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
				ps.setTimestamp(1,new Timestamp(last));
				ps.setInt(2,1);
				
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
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		this.closeOutput(out);
	}
}
