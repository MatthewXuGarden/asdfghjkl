package com.carel.supervisor.director.ac;

import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class AcConfigBeanList
{
	private List<AcConfigBean> list = new ArrayList<AcConfigBean>();
	
	public AcConfigBeanList()
	{
        //verifico tabelle "ac_master" e "ac_slave" per consistenza configurazioni:
        boolean changed = AcProcess.ctrl_ac_tables();
        
		//String sql = "select * from ac_slave ";
        //seleziono solo gli slaves dei master abilitati alla propagazione:
        String sql = "select * from ac_slave where iddevmaster IN (select iddevmaster from ac_master where enabled=1) order by iddevmaster";
        
        RecordSet rs = null;
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(AcConfigBeanList.class);
            logger.error(e);
        }
        
        AcConfigBean tmp = null;
        
        if ((rs != null) && (rs.size() > 0))
        {
        	for (int i = 0; i < rs.size(); i++)
        	{
        		tmp = new AcConfigBean(rs.get(i));
        		list.add(tmp);
        	}
        }
	}
	
	public int size()
	{
		return list.size();
	}
	
	public AcConfigBean get(int index)
	{
		return list.get(index);
	}
}
