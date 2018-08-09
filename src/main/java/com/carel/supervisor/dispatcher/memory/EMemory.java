package com.carel.supervisor.dispatcher.memory;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;


public class EMemory extends ZMemory
{
    private static final String SQL = "select * from cfioemail where idsite=1";
    private String modemid = "";
    private String modemtype = "";
    private String smtp = "";
    private String sender = "";
    private String type = "";
    private String provider = "";
    private String user = "";
    private String pass = "";
    private String encryption ="NONE";
    private String port = "25";
    
    public String getModemId()
    {
        return this.modemid;
    }

    public String getModemType()
    {
        return this.modemtype;
    }

    public String getProvider()
    {
        return provider;
    }

    public String getSender()
    {
        return sender;
    }

    public String getSmtp()
    {
        return smtp;
    }
    
    public String getPort()
    {
    	return port;
    }

    public String getType()
    {
        return type;
    }
    
    public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
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
                this.modemid = UtilBean.trim(r.get("modemid"));
                this.modemtype = UtilBean.trim(r.get("modemtype"));
                this.smtp = UtilBean.trim(r.get("smtp"));
                this.port = r.get("port").toString();
                this.sender = UtilBean.trim(r.get("sender"));
                this.type = UtilBean.trim(r.get("type"));
                this.provider = UtilBean.trim(r.get("provider"));
                this.setIdSite(((Integer) r.get("idsite")).intValue());
                this.setRetryNum(((Integer) r.get("retrynumber")).intValue());
                this.setRetryAfter(((Integer) r.get("retryafter")).intValue());
                this.user = UtilBean.trim(r.get("smtpuser"));
                this.pass = UtilBean.trim(r.get("smtppass"));
                this.encryption = UtilBean.trim(r.get("encryption"));
            }
        }
    }

    public String getFisicDeviceId()
    {
        return this.modemid;
    }
    
    public String getEncryption()
    {
        return this.encryption;
    }
    
}
