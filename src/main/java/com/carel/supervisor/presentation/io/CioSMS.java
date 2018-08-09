package com.carel.supervisor.presentation.io;

import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dispatcher.comm.layer.DispLaySms;
import com.carel.supervisor.dispatcher.comm.layer.DispLayer;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;

import java.sql.Timestamp;

public class CioSMS
{
    private int idconf = 0;
    private int idsite = 0;
    private String modem = "";
    private String type = "";
    private int provider = 0;
    private String call = "";
    private int trynum = 0;
    private int retrynum = 0;
    private String centralino = "";
    private String providerlb = "";
    
    public static final String GSM_MODEM = "GSM_modem";
    public static final String GSM_PROVIDER = "GSM_provider_wizard";

    public CioSMS(int idsite)
    {
        this.idsite = idsite;
    }

    public void loadConfiguration()
    {
        String sql = "select * from cfiosms where idsite=?";
        Record r = null;
        Object[] param = { new Integer(this.idsite) };

        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

            if ((rs != null) && (rs.size() > 0))
            {
                r = rs.get(0);
                this.idconf = ((Integer) r.get("idsms")).intValue();
                this.modem = UtilBean.trim(r.get("modemid"));
                this.type = UtilBean.trim(r.get("modemtype"));
                this.provider = ((Integer) r.get("providerid")).intValue();
                this.call = UtilBean.trim(r.get("calltype"));
                this.trynum = ((Integer) r.get("retrynumber")).intValue();
                this.retrynum = ((Integer) r.get("retryafter")).intValue();
                this.centralino = UtilBean.trim(r.get("centralino"));
                this.providerlb = UtilBean.trim(r.get("providerlb"));
            }
            else
            {
                this.idconf = -1;
                this.modem = "";
                this.type = "";
                this.provider = -1;
                this.call = "";
                this.trynum = -1;
                this.retrynum = -1;
                this.centralino = "";
                this.providerlb = "";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getIdConf()
    {
        return this.idconf;
    }

    public String getLabelModem()
    {
        return this.modem;
    }

    public String getType()
    {
        return type;
    }

    public int getProviderId()
    {
        return this.provider;
    }

    public String getCall()
    {
        return call;
    }

    public int getTrynum()
    {
        return trynum;
    }

    public int getRetrynum()
    {
        return retrynum;
    }

    public String getCentralino()
    {
        return this.centralino;
    }

    public String getProviderLb()
    {
        return this.providerlb;
    }

    public String[][] getProvider()
    {
        String[][] providerList = null;
        String sql = "select idprovider,labelprovider from cfprovidersms where idsite=? order by labelprovider";
        Record r = null;
        Object[] param = { new Integer(this.idsite) };

        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

            if (rs != null)
            {
                providerList = new String[rs.size()][2];

                for (int i = 0; i < providerList.length; i++)
                {
                    r = rs.get(i);
                    providerList[i][0] = String.valueOf(((Integer) r.get("idprovider")).intValue());
                    providerList[i][1] = UtilBean.trim(r.get("labelprovider"));
                }
            }
        }
        catch (DataBaseException e)
        {
            e.printStackTrace();
            providerList = new String[0][0];
        }

        return providerList;
    }

    public String[][] getModem()
    {
        DispLayer layer = new DispLaySms();

        return layer.getFisicChannel("S");
    }
    public boolean checkMedemExist(String modem)
	{
		String[][] modems = getModem();
		for(int i=0;i<modems.length;i++)
		{
			if(modem.equals(modems[i][0]))
				return true;
		}
		return false;
	}
    public boolean saveConfiguration(int id, String modem, String type, int provider,
        String calltype, int trynum, int retry, String centra, String lbpro)
    {
        String sql = "";
        Object[] values = null;
        Timestamp curtime = new Timestamp(System.currentTimeMillis());
        boolean done = true;

        if (id > 0)
        {
            sql = "update cfiosms set modemid=?,modemtype=?,providerid=?,providerlb=?,calltype=?,retrynumber=?,retryafter=?,centralino=?,lastupdate=? where idsms=? and idsite=?";
            values = new Object[]
                {
                    modem, type, new Integer(provider), lbpro, calltype, new Integer(trynum),
                    new Integer(retry), centra, curtime, new Integer(id), new Integer(this.idsite)
                };
        }
        else
        {
            sql = "insert into cfiosms values(?,?,?,?,?,?,?,?,?,?,?,?)";

            Integer key;

            try
            {
                key = SeqMgr.getInstance().next(null, "cfiosms", "idsms");
                values = new Object[]
                    {
                        key, new Integer(this.idsite), modem, type, new Integer(provider), lbpro,
                        calltype, new Integer(trynum), new Integer(retry), centra, curtime, curtime
                    };
            }
            catch (DataBaseException e)
            {
                e.printStackTrace();
                done = false;
            }
        }

        if (done)
        {
            try
            {
                DatabaseMgr.getInstance().executeStatement(null, sql, values);
            }
            catch (DataBaseException e)
            {
                e.printStackTrace();
                done = false;
            }
        }

        return done;
    }

    public boolean removeConfiguration(int id)
    {
        String sql = "";
        Object[] values = null;

        boolean done = true;
		
		try
		{
			//controllo esistenza azione sms:
			done = !(ActionBeanList.existsActionType(this.idsite, "S"));
		}
		catch (DataBaseException e1)
		{
			done = false;
		}

        if ((id > 0) && (done))
        {
            sql = "delete from cfiosms where idsms=? and idsite=?";
            values = new Object[] { new Integer(id), new Integer(this.idsite) };

            try
            {
                DatabaseMgr.getInstance().executeStatement(null, sql, values);
            }
            catch (DataBaseException e)
            {
                e.printStackTrace();
                done = false;
            }
        }

        return done;
    }
}
