package com.carel.supervisor.dataaccess.datalog.impl;

import java.util.Properties;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class IdeStringBeanList
{
    public static Properties getIdeLabels(int idsite,int iddevice)
    {
        Properties prop = new Properties();
        String sql = "select * from cflabelide where idsite=? and iddevice=?";
        
        try 
        {
            String key = "";
            String val = "";
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,
                           new Object[]{new Integer(idsite),new Integer(iddevice)});
            Record r = null;
            if(rs != null)
            {
                for(int i=0; i<rs.size(); i++)
                {
                    r = rs.get(i);
                    if(r != null);
                    {
                        key = UtilBean.trim(r.get("key"));
                        val = UtilBean.trim(r.get("value"));
                        if(key != null)
                        {
                            if(val == null)
                                val = "";
                            prop.put(key,val);
                        }
                    }
                }
            }
        } 
        catch (Exception e) {
            Logger logger = LoggerMgr.getLogger(IdeStringBeanList.class);
            logger.error(e);
        }
        
        return prop;
    }
    
    public static void setIdeStrings(int idsite,int iddevice,String[] keys,String[] value)
    {
        StringBuffer sb = new StringBuffer("delete from cflabelide where idsite=? and iddevice=? and key in (");
        for(int i=0; i<keys.length; i++)
        {
            sb.append("?");
            if(i < keys.length-1)
                sb.append(",");
        }
        sb.append(");");
        int idx = -1;
        Object[] params = new Object[keys.length+2];
        params[++idx] = new Integer(idsite);
        params[++idx] = new Integer(iddevice);
        for(int i=0; i<keys.length; i++)
            params[++idx] = keys[i];
        
        try
        {
            DatabaseMgr.getInstance().executeStatement(null,sb.toString(),params);
            sb = new StringBuffer("insert into cflabelide values(?,?,?,?);");
            params = new Object[4];
            params[0] = new Integer(idsite);
            params[1] = new Integer(iddevice);
            for(int i=0; i<keys.length; i++)
            {
                try
                {
                    params[2] = keys[i];
                    params[3] = value[i];
                    DatabaseMgr.getInstance().executeStatement(null,sb.toString(),params);
                }
                catch (Exception e) {
                    Logger logger = LoggerMgr.getLogger(IdeStringBeanList.class);
                    logger.error(e);
                }
            }
            
        } 
        catch (Exception e) {
            Logger logger = LoggerMgr.getLogger(IdeStringBeanList.class);
            logger.error(e);
        }
    }
}
