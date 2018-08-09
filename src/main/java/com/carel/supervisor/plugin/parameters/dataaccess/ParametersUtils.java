package com.carel.supervisor.plugin.parameters.dataaccess;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class ParametersUtils {

	public static Integer getProfileId(String username){
		Integer idprofile = null;
		String sql="select idprofile from cfusers where username = ? ";
		Object[] o = new Object[1];
		o[0]=username;
		try {
			RecordSet rs =  DatabaseMgr.getInstance().executeQuery(null, sql, o);
			
			if (rs.size()>0){
				idprofile  =(Integer) rs.get(0).get("idprofile");	
			}
			else {
				//se lo user non c'è nella cfuser vuol dire che è uno user di sistema...
				idprofile = null;				
			}
		} catch (DataBaseException e) {
			Logger logger = LoggerMgr.getLogger(ParametersUtils.class);
			logger.error(e);
		}
		return idprofile;
	}
}
