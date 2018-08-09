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

public class ExpVariable extends ExpMaster
{
	private static final String SQL = 
		"select cfvariable.*,cftableext.description from cfvariable inner join cftableext on " +
		"cfvariable.idvariable = cftableext.tableid and cftableext.idsite=? and cftableext.tablename='cfvariable' "+
		" and cftableext.languagecode=? and cfvariable.type=? and cfvariable.idsite=? and (cfvariable.lastupdate > ? or cftableext.lastupdate > ?)";
	
	private static final String SQL_2 = "select hs.idvariable,v.pvcode,v.idsite,v.iddevice,v.islogic,v.idvarmdl,v.functioncode,v.code,v.type,v.addressin, "+
		"v.addressout,v.vardimension,v.varlength,v.bitposition,v.signed,v.decimal,v.todisplay,v.buttonpath,v.priority,v.readwrite, "+
		"v.minvalue,v.maxvalue,v.defaultvalue,v.measureunit,v.idvargroup,v.imageon,v.imageoff,hs.frequency,hs.delta,hs.delay, "+
		"v.isonchange,v.ishaccp,v.isactive,v.iscancelled,v.grpcategory,hs.idhsvariable,v.inserttime,v.lastupdate,cftableext.description  "+
	"from cfvariable as hs  "+
	"inner join cfvariable as v on v.idhsvariable=hs.idvariable  "+
	"inner join cftableext on  "+
		"v.idvariable = cftableext.tableid and cftableext.idsite=? and cftableext.tablename='cfvariable'  "+
		"and cftableext.languagecode=? and v.idsite=? "+
	"inner join cftransfervar on "+
		"hs.idvariable=cftransfervar.idvar and cftransfervar.idsite=? "+
	"where (cftransfervar.lastupdate>? or v.lastupdate>? or cftableext.lastupdate>? or hs.lastupdate>?)";
   
	private static final String SQL_3 = 
		"select cfvariable.*,cftableext.description from cfvariable inner join cftableext on " +
		"cfvariable.idvariable = cftableext.tableid and cftableext.idsite=? and cftableext.tablename='cfvariable' "+
		" and cfvariable.idsite=? and cftableext.languagecode=? and (cfvariable.lastupdate > ? or cftableext.lastupdate > ?) and cfvariable.idhsvariable is not null";

	public ExpVariable(String db,String lang,String tab,long last)
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
		
		//export variabili di allarme
		try
		{
			ps = con.prepareStatement(SQL);
			if(ps != null)
			{
				ps.setInt(1,1);
				ps.setString(2,lang);
				ps.setInt(3,4);
				ps.setInt(4,1);
				ps.setTimestamp(5,new Timestamp(last));
				ps.setTimestamp(6,new Timestamp(last));
				
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
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		//export variabili non di allarme
		try
		{
			ps = con.prepareStatement(SQL_2);
			if(ps != null)
			{
				ps.setInt(1,1);  //idsite
				ps.setString(2,lang);  //lingua
				ps.setInt(3,1);  //idsite
				ps.setInt(4,1);  //idsite
				ps.setTimestamp(5,new Timestamp(last));
				ps.setTimestamp(6,new Timestamp(last));
				ps.setTimestamp(7,new Timestamp(last));
				ps.setTimestamp(8,new Timestamp(last));
								
				rs = ps.executeQuery();
				if(rs != null)
				{
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
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		
		//LDAC FOR NOVACOOP 12/07/2011 START
		//export variabili per trend/gateway
			
		try
		{
			ps = con.prepareStatement(SQL_3);
			if(ps != null)
			{
				ps.setInt(1,1);  //idsite
				ps.setInt(2,1);  //idsite
				ps.setString(3,lang);  //lingua
				ps.setTimestamp(4,new Timestamp(last));
				ps.setTimestamp(5,new Timestamp(last));
				
				rs = ps.executeQuery();
				if(rs != null)
				{
					try
					{
						meta = rs.getMetaData();
						while(rs.next())
						{
							if (ExpDataMgr.getInstance().idvarMdlPresent(rs.getInt("idvarmdl")))
							{
								for(int j=1; j<=meta.getColumnCount(); j++) 
								{
									decodeVal = decodeColumValue(meta.getColumnType(j),meta.getColumnName(j),rs);
									sb.append(decodeVal);
								}
								sb.append("\n");
								try 
								{
									out.write(sb.toString().getBytes("UTF-8"));
									sb = new StringBuffer();
								}
								catch(Exception e) 
								{
									
								}
							}
						}
						
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		
		
		//LDAC FOR NOVACOOP 12/07/2011 END
		
		this.closeOutput(out);
	}
}
