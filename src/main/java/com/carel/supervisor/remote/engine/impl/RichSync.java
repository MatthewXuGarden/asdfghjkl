package com.carel.supervisor.remote.engine.impl;

import java.util.Iterator;
import java.util.LinkedList;

import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dispatcher.enhanced.ProExportRecord;

public class RichSync
{
	public static void importRichSync(LinkedList<ProExportRecord> pexp,int idsite)
	{
//		for(Iterator<ProExportRecord> i=pexp.iterator();i.hasNext();)
//		{
//			ProExportRecord per = i.next();
//			try
//			{
//				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from cfdevice where idsite="+idsite+" and " +
//						"globalindex="+per.getGid()+" and iscancelled='FALSE'");
//				if(rs.size()==1)
//				{
//					int iddev = (Integer)rs.get(0).get("iddevice");
//					DatabaseMgr.getInstance().executeStatement(null,"delete from cfvariable where idsite="+idsite+" and iddevice="+iddev,null);
//				}
//			}
//			catch(Exception e)
//			{
//				LoggerMgr.getLogger(RichSync.class).error(e);
//			}
//		}
		
		for(Iterator<ProExportRecord> i=pexp.iterator();i.hasNext();)
		{
			ProExportRecord per = i.next();
			try
			{
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, "select * from cfdevice where idsite="+idsite+
					" and globalindex="+per.getGid()+" and iscancelled='FALSE'");
				if(rs.size()==1)
				{
					int iddev = (Integer)rs.get(0).get("iddevice");
					RecordSet rs2 = DatabaseMgr.getInstance().executeQuery(null, "select * from cfvariable where idsite="+idsite+
						" and iddevice="+iddev+" and addressin="+per.getAddress()+" and type="+per.getType()+
						" and decimal = "+per.getPowerdegree()+" and iscancelled='FALSE'");
					if(rs2.size()==0) //no var
					{
						int idvar = SeqMgr.getInstance().next(null, "cfvariable", "idvariable"); 
						DatabaseMgr.getInstance().executeStatement(null, "insert into cfvariable (idvariable, pvcode, idsite, iddevice, islogic, code, type, " +
							"todisplay, isonchange, ishaccp, isactive, iscancelled, inserttime, lastupdate, addressin, addressout, decimal) " +
							"values " +
							"("+idvar+", 'firstPV', "+idsite+", "+iddev+", 'FALSE', '---', "+per.getType()+", 'FALSE', 'FALSE', 'FALSE', 'FALSE', 'FALSE', " +
							"current_timestamp, current_timestamp, "+per.getAddress()+", "+per.getAddress()+", "+per.getPowerdegree()+")",null);
						DatabaseMgr.getInstance().executeStatement(null, "insert into cftableext values ("+idsite+", 'EN_en', 'cfvariable', " +
								idvar+" , '"+per.getDescription()+"' , Null, Null,current_timestamp)",null);
					}
					else	// sì var
					{
//						DatabaseMgr.getInstance().executeStatement(null, "update cfvariable set pvcode='firstPV', "+
//							"idsite="+idsite+", islogic='FALSE', code = '---', type="+per.getType()+", todisplay='FALSE', isonchange='FALSE', ishaccp='FALSE', "+
//							"isactive='FALSE', iscancelled='FALSE', lastupdate=current_timestamp, addressin="+per.getAddress()+", "+
//							"addressout="+per.getAddress()+", decimal="+per.getPowerdegree()+" where idvariable="+rs2.get(0).get("idvariable"), null);
						try{
						DatabaseMgr.getInstance().executeStatement(null, "update cftableext set description='"+per.getDescription()+"' , lastupdate=current_timestamp " +
							"where idsite="+idsite+" and tablename='cfvariable' and tableid="+rs2.get(0).get("idvariable"), null);
						}catch(Exception e)
						{
							DatabaseMgr.getInstance().executeStatement(null, "insert into cftableext values ("+idsite+", 'EN_en', 'cfvariable', " +
											rs2.get(0).get("idvariable")+" , '"+per.getDescription()+"' , Null, Null,current_timestamp)",null);
						}
					}
				}
			} catch (DataBaseException e)
			{
				LoggerMgr.getLogger(RichSync.class).error(e);
				e.printStackTrace();
			}
		}
	}
}
