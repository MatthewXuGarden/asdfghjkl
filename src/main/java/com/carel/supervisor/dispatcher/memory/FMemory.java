package com.carel.supervisor.dispatcher.memory;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class FMemory extends ZMemory
{
    private static final String SQL = "select idsite,modemid,modemtype,trynumber,retryafter,centralino from cfiofax where idsite=1";
    private String modemid = "";
    private String modemtype = "";
    private String centralino = "";

    public String getModemId()
    {
        return this.modemid;
    }

    public String getModemType()
    {
        return this.modemtype;
    }

    public String getCentralino()
    {
        return this.centralino;
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
                this.modemid = UtilBean.trim(r.get("modemid"));
                this.modemtype = UtilBean.trim(r.get("modemtype"));
                this.centralino = UtilBean.trim(r.get("centralino"));
                this.setRetryNum(((Integer) r.get("trynumber")).intValue());
                this.setRetryAfter(((Integer) r.get("retryafter")).intValue());
            }
        }
    }

    public String getFisicDeviceId()
    {
        return this.modemid;
    }
}
