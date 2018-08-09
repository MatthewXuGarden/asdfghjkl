package com.carel.supervisor.dispatcher.memory;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class SMemory extends ZMemory
{
    private static final String SQL = "select idsite,modemid,modemtype,providerid,providerlb,calltype,retrynumber,retryafter,centralino from cfiosms where idsite=1";
    private String modemid = "";
    private String modemtype = "";
    private int provider = 0;
    private String calltype = "";
    private String centralino = "";
    private String providerlb = "";

    public String getModemId()
    {
        return this.modemid;
    }

    public String getModemType()
    {
        return this.modemtype;
    }

    public String getCalltype()
    {
        return calltype;
    }

    public int getProviderId()
    {
        return this.provider;
    }

    public String getCentralino()
    {
        return this.centralino;
    }

    public String getProviderLb()
    {
        return this.providerlb;
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
                this.provider = ((Integer) r.get("providerid")).intValue();
                this.calltype = UtilBean.trim(r.get("calltype"));
                this.setRetryNum(((Integer) r.get("retrynumber")).intValue());
                this.setRetryAfter(((Integer) r.get("retryafter")).intValue());
                this.centralino = UtilBean.trim(r.get("centralino"));
                this.providerlb = UtilBean.trim(r.get("providerlb"));
            }
        }
    }

    public String getFisicDeviceId()
    {
        return this.modemid;
    }
}
