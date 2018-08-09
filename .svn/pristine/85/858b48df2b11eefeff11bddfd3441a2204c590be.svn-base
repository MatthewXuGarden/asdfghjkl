package com.carel.supervisor.dispatcher.memory;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class DMemory extends ZMemory
{
    private static final String SQL = "select idsite,modemid,modemtype,dialnumber,dialuser,retrynumber,retryafter,centralino from cfioras where idsite=1";
    private String modemid = "";
    private String modemtype = "";
    private String number = "";
    private String user = "";
    private String centralino = "";

    public String getModemId()
    {
        return this.modemid;
    }

    public String getModemType()
    {
        return this.modemtype;
    }

    public String getNumber()
    {
        return this.number;
    }

    public String getUser()
    {
        return this.user;
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
                this.number = UtilBean.trim(r.get("dialnumber"));
                this.user = UtilBean.trim(r.get("dialuser"));
                this.centralino = UtilBean.trim(r.get("centralino"));
                this.setRetryNum(((Integer) r.get("retrynumber")).intValue());
                this.setRetryAfter(((Integer) r.get("retryafter")).intValue());
            }
        }
    }

    public String getFisicDeviceId()
    {
        return this.modemid;
    }
}
