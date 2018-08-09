package com.carel.supervisor.dataaccess.datalog.impl;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class CustomViewBeanList 
{
	public static CustomViewBean[] getCustomViewList(int idSite)
	{
		String sql = "select * from cfcustomdtl where idsite=?";
		RecordSet rs = null;
		Record r = null;
		CustomViewBean[] list = new CustomViewBean[0];
		
		try 
		{
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idSite)});
			if(rs != null)
			{
				list = new CustomViewBean[rs.size()];
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					if(r != null)
						list[i] = new CustomViewBean(r);
				}
			}
		} 
		catch (Exception e) 
		{
			Logger logger = LoggerMgr.getLogger(CustomViewBeanList.class);
			logger.error(e);
		}
		return list;
	}
	
	public static CustomViewBttBean[] getCustomViewBttList(int idSite)
	{
		String sql = "select * from cfcustombtt as a where idsite=? order by a.order";
		RecordSet rs = null;
		Record r = null;
		CustomViewBttBean[] list = new CustomViewBttBean[0];
		
		try 
		{
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idSite)});
			if(rs != null)
			{
				list = new CustomViewBttBean[rs.size()];
				for(int i=0; i<rs.size(); i++)
				{
					r = rs.get(i);
					if(r != null)
						list[i] = new CustomViewBttBean(r);
				}
			}
		} 
		catch (Exception e) 
		{
			Logger logger = LoggerMgr.getLogger(CustomViewBeanList.class);
			logger.error(e);
		}
		return list;
	}
	
	public static CustomVarphyBean[] loadCustomVariables(int idSite,int idDevice,String language)
	{
		String sql = "select l.description,l.longdescr,l.shortdescr,w.typevis,v.* "+
		             "from wcfvardisplay as w,cfvariable as v,cftableext as l "+
					 "where w.idsite=? and w.idvarmdl=v.idvarmdl and iddevice=? and l.idsite=? and l.languagecode=? " +
					 "and l.tablename=? and l.tableid=v.idvariable and v.idhsvariable is not null order by w.order asc"; 
		
		CustomVarphyBean[] list = new CustomVarphyBean[0];
		RecordSet rs = null;
		Object[] params = new Object[]{new Integer(idSite),new Integer(idDevice),new Integer(idSite),
									   language,"cfvariable"};
		
		try 
		{
			rs = DatabaseMgr.getInstance().executeQuery(null,sql,params);
			if(rs != null)
			{
				list = new CustomVarphyBean[rs.size()];
				for(int i=0; i<rs.size(); i++) 
					list[i] = new CustomVarphyBean(rs.get(i));
			}
		} 
		catch (Exception e) 
		{
			Logger logger = LoggerMgr.getLogger(CustomViewBeanList.class);
			logger.error(e);
		}
		
		return list;
	}
}
