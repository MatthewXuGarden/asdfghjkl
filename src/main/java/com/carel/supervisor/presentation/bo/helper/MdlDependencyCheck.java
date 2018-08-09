package com.carel.supervisor.presentation.bo.helper;

import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class MdlDependencyCheck {


	public static boolean checkMdlInKPI(int idDevMdl) throws Exception
	{
		String sql = "select iddevmdl from kpiconf group by iddevmdl";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		for (int i = 0; i < rs.size(); i++)
		{
			if(((Integer)rs.get(i).get(0)) == idDevMdl)
				return true;
		}
		return false;
	}
	
	public static boolean checMdlInkLN(int idDevMdl) throws Exception
	{
		String sql = "select iddevmdl from ln_varmdl group by iddevmdl";
		RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
		
		for (int i = 0; i < rs.size(); i++)
		{
			if(((Integer)rs.get(i).get(0)) == idDevMdl)
				return true;
		}
		return false;
		
	}
	
	public static boolean checkMdlInDP(int idDevMdl) throws Exception
	{
		// retrieve iddevmdl from code 
        String sql = "select code from cfdevmdl where iddevmdl=?";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idDevMdl });
        String mdlcode = (String) rs.get(0).get("code");
		
		sql = "select code from ac_master_mdl group by code";
		
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);

		for(int i = 0; i < rs.size(); i++)
		{
			if(((String)rs.get(i).get(0)).equals(mdlcode))
				return true;
		}
		
		sql = "select code from ac_slave_mdl group by code";
		
		rs = DatabaseMgr.getInstance().executeQuery(null, sql);

		for(int i = 0; i < rs.size(); i++)
		{
			if(((String)rs.get(i).get(0)).equals(mdlcode))
				return true;
		}
		return false;
	}


}
