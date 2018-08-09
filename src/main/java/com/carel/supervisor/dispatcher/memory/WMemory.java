package com.carel.supervisor.dispatcher.memory;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class WMemory extends ZMemory
{
    private static final String SQL = "select idsite,pathsound from cfioevent where idsite=1";
    private String pathsound = "";

    public String getPathSound()
    {
        return this.pathsound;
    }

    public void storeConfiguration() throws Exception
    {
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, SQL, null);
        Record r = null;

        if ((rs != null) && (rs.size() > 0))
        {
            r = rs.get(0);

            if (r != null)
            {
                this.setIdSite(((Integer) r.get("idsite")).intValue());
                this.pathsound = UtilBean.trim(r.get("pathsound"));
                this.setRetryNum(0);
                this.setRetryAfter(0);
            }
        }
    }

    public String getFisicDeviceId()
    {
        return "";
    }
}
