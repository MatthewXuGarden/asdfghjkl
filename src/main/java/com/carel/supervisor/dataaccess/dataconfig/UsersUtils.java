package com.carel.supervisor.dataaccess.dataconfig;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;

public class UsersUtils {
    
    public static String getUsersListAsCombo()
    {
        StringBuffer combo = new StringBuffer();
        combo.append("<select name='txtUser' id='txtUser' style='font-family:Tahoma,Verdana;font-size:8pt;'>\n");
        combo.append("<option selected value='unknown'> --------------- </option>\n");
        
        RecordSet rs = null;

        String sql = "select username from cfusers order by username";
        
        try
        {
            rs = DatabaseMgr.getInstance().executeQuery(null, sql, null);
        }
        catch (Exception e)
        {
            // PVPro-generated catch block:
            Logger logger = LoggerMgr.getLogger(UsersUtils.class);
            logger.error(e);
        }
        
        // visto che, al minimo, c'è sempre almeno 1 utente:
        for (int i = 0; i < rs.size(); i++)
        {
            combo.append("<option value='");
            combo.append(rs.get(i).get(0).toString());
            combo.append("'> ");
            combo.append(rs.get(i).get(0).toString());
            combo.append(" </option>\n");
        }
        
        combo.append("</select>\n");
        return combo.toString();
    }

}
