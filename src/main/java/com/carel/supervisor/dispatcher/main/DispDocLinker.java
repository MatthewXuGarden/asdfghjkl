package com.carel.supervisor.dispatcher.main;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class DispDocLinker
{
    public static void insertDoc(int site,Integer idEvent,String type,String document)
    {
        String sql = "insert into hsdocdispsent values(?,?,?,?)";
        
        try
        {
            DatabaseMgr.getInstance().executeStatement(null,sql,
                                      new Object[]{new Integer(site),idEvent,document,type});
        } 
        catch(Exception e)
        {
            Logger logger = LoggerMgr.getLogger(DispDocLinker.class);
            logger.error(e);
        }
        
    }
    
    public static String[] retriveDoc(int idSite,int idEvent)
    {
        String sql = "select type,document from hsdocdispsent where idsite=? and idevent=?";
        String[] ret = null;
        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idSite),new Integer(idEvent)});
            if(rs != null && rs.size() == 1)
            {
                ret = new String[2];
                ret[0] = UtilBean.trim(rs.get(0).get("type"));
                ret[1] = UtilBean.trim(rs.get(0).get("document"));
            }
        } 
        catch(Exception e)
        {
            Logger logger = LoggerMgr.getLogger(DispDocLinker.class);
            logger.error(e);
        }
        return ret;
    }
}
