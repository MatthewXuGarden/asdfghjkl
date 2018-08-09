package com.carel.supervisor.remote.bean;

import java.sql.Timestamp;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ImportBeanList 
{
	public ImportBeanList()
	{
	}
	
	public boolean insertImport(String db,int idsite,String idimport)
	{
		boolean ris = false;
		String sql = "insert into cfsiteimport values(?,?,?,?,?)";
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Object[] p = {new Integer(idsite),idimport,"FALSE",time,time};
		try {
			DatabaseMgr.getInstance().executeStatement(db,sql,p);
			ris = true;
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}
	
	public ImportBean[] loadImport(String db)
	{
		String sql = "select * from cfsiteimport where state=? order by inserttime asc";
		ImportBean[] imports = null;
		try {
			RecordSet rs = DatabaseMgr.getInstance().executeQuery(db,sql,new Object[]{"FALSE"});
			if(rs != null)
			{
				imports = new ImportBean[rs.size()];
				for(int i=0; i<rs.size(); i++)
					imports[i] = new ImportBean(rs.get(i));
			}
		}
		catch(Exception e) {
			imports = new ImportBean[0];
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return imports;
	}
	
	public boolean updateImport(String db,String idimport,int idsite)
	{
		boolean ris = false;
		String sql = "update cfsiteimport set state=?,lastupdate=? where idsite=? and idimport=?";
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Object[] p = {"TRUE",time,new Integer(idsite),idimport};
		
		try 
		{
			DatabaseMgr.getInstance().executeStatement(db,sql,p);
			ris = true;
		}
		catch(Exception e) {
			Logger logger = LoggerMgr.getLogger(this.getClass());
			logger.error(e);
		}
		return ris;
	}
}
