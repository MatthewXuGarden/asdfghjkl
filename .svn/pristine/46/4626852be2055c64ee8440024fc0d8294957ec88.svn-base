package com.carel.supervisor.presentation.io;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dispatcher.comm.layer.DispLayDialUp;
import com.carel.supervisor.dispatcher.comm.layer.DispLayer;
import com.carel.supervisor.presentation.bean.rule.ActionBeanList;
import com.carel.supervisor.remote.bean.IncomingBeanList;


public class CioDIAL
{
    private int idconf = 0;
    private int idsite = 0;
    private String modemid = "";
    private String modemtype = "";
    private String user = "";
    private String number = "";
    private int trynum = 0;
    private int retrynum = 0;
    private String centralino = "";

    public CioDIAL(int idsite)
    {
        this.idsite = idsite;
    }
    
    public boolean insertForIncoming(String idDevice)
    {
    	boolean ris = false;
    	try {
    		IncomingBeanList.insertDeviceForConfig(null,this.idsite,idDevice);
    		ris = true;
    	}
    	catch(Exception e){}
    	return ris;
    }
    
    public String[] retriveDevice(Properties prop)
    {
    	String key = "";
    	String val = "";
    	String[] ret = new String[0];
    	List lista = new ArrayList();
    	
    	if(prop != null)
    	{
    		Iterator i = prop.keySet().iterator();
    		while (i.hasNext())
    		{
    			key = (String) i.next();
    			if ((key != null) && key.startsWith("inmo"))
    			{
    				val = prop.getProperty(key);
    				lista.add(val);
    			}
    		}
    	}
    	
    	ret = new String[lista.size()];
    	for(int i=0; i<ret.length; i++)
    		ret[i] = (String)lista.get(i);
    	
    	return ret;
    }
    
    /*
    public boolean insertForIncomingRemote(Properties prop)
    {
    	boolean ris = false;
    	String key = "";
    	String val = "";
    	
    	if(prop != null)
    	{
    		Iterator i = prop.keySet().iterator();
    		while (i.hasNext())
    		{
    			key = (String) i.next();
    			if ((key != null) && key.startsWith("inmo"))
    			{
    				val = prop.getProperty(key);
    				insertForIncoming(val);
    			}
    		}
    	}
    	return ris;
    }
    */
    
    public void loadConfiguration()
    {
        String sql = "select * from cfioras where idsite=?";
        Record r = null;
        Object[] param = { new Integer(this.idsite) };

        try
        {
            RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

            if ((rs != null) && (rs.size() > 0))
            {
                r = rs.get(0);
                this.idconf = ((Integer) r.get("idras")).intValue();
                this.modemid = UtilBean.trim(r.get("modemid"));
                this.modemtype = UtilBean.trim(r.get("modemtype"));
                this.user = UtilBean.trim(r.get("dialuser"));
                this.number = UtilBean.trim(r.get("dialnumber"));
                this.trynum = ((Integer) r.get("retrynumber")).intValue();
                this.retrynum = ((Integer) r.get("retryafter")).intValue();
                this.centralino = UtilBean.trim(r.get("centralino"));
            }
            else
            {
                this.idconf = -1;
                this.modemid = "";
                this.modemtype = "";
                this.user = "";
                this.number = "";
                this.trynum = -1;
                this.retrynum = -1;
                this.centralino = "";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean saveConfiguration(int id, String modemid, String type, String user,
        String number, int trynum, int retry, String centra)
    {
        String sql = "";
        Object[] values = null;
        Timestamp curtime = new Timestamp(System.currentTimeMillis());
        boolean done = true;

        if (id > 0)
        {
            sql = "update cfioras set modemid=?,modemtype=?,dialnumber=?,dialuser=?,retrynumber=?,retryafter=?,centralino=?,lastupdate=? where idras=? and idsite=?";
            values = new Object[]
                {
                    modemid, modemtype, number, user, new Integer(trynum), new Integer(retry),
                    centra, curtime, new Integer(id), new Integer(this.idsite)
                };
        }
        else
        {
            sql = "insert into cfioras values(?,?,?,?,?,?,?,?,?,?,?)";

            Integer key;

            try
            {
                key = SeqMgr.getInstance().next(null, "cfioras", "idras");
                values = new Object[]
                    {
                        key, new Integer(this.idsite), modemid, type, number, user,
                        new Integer(trynum), new Integer(retry), centra, curtime, curtime
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
			//controllo esistenza azione ras/remoto:
        	boolean action = ActionBeanList.existsActionType(this.idsite, "D");
			
			//se esiste azione ras/remoto, controllo se via tel e/o ip:
			if (action)
			{
				//ctrl parameters azioni ras/remoto x presenza di n°tel e non solo ip
				done = !(ActionBeanList.existsParamType(this.idsite, "D"));
			}
		}
		catch (DataBaseException e1)
		{
			done = false;
			
			Logger logger = LoggerMgr.getLogger(CioDIAL.class);
			logger.error(e1);
		}
        
        // elimino conf. se non esiste azione bloccante e se fra i parametri non c'è nessun n°tel
		if ((id > 0) && (done))
        {
            sql = "delete from cfioras where idras=? and idsite=?";
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

    public int getIdconf()
    {
        return idconf;
    }

    public String getModemLabel()
    {
        return modemid;
    }

    public String getModemtype()
    {
        return modemtype;
    }

    public String getNumber()
    {
        return number;
    }

    public String getUser()
    {
        return user;
    }

    public int getRetryNum()
    {
        return this.trynum;
    }

    public int getRetryAfter()
    {
        return this.retrynum;
    }

    public String getCentralino()
    {
        return this.centralino;
    }

    public String[][] getModem()
    {
        DispLayer layer = new DispLayDialUp();
        return layer.getFisicChannel("D");
    }
    
    public String[][] getUsedModem()
    {
    	DispLayer layer = new DispLayDialUp();
    	return layer.getUsedFisicChannel();
    }
    
    public String[][] getModemListRemote()
    {
    	String[][] list = getModem();
    	String[][] used = getUsedModem();
    	String[][] retu = null;
    	List l = new ArrayList();
    	boolean found = false;
    	
    	if(list != null && used != null)
    	{
    		for(int i=0; i<list.length; i++)
    		{
    			found = false;
    			for(int j=0; j<used.length; j++)
    			{
    				if(list[i][0].equalsIgnoreCase(used[j][0]))
    				{
    					found = true;
    					break;
    				}
    			}
    			
    			if(!found)
    				l.add(list[i][0]);
    			
    			
    		}
    		
    		if((l.size() == 0) && (getModemLabel().length() > 0))
    			l.add(getModemLabel());
    	}
    	
    	retu = new String[l.size()][2];
    	for(int i=0; i<l.size(); i++)
    	{
    		retu[i][0] = (String)l.get(i);
    		retu[i][1] = "";
    	}
    	return retu;
    }
}
