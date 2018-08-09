package com.carel.supervisor.dispatcher.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;


public class HSActionBeanList
{
    public static final int INSERT = 1;
    public static final int LOAD = 2;
    public static final int SEND = 3;
    public static final int DISCARD = 4;
    public static final int CANCEL = 5;
    public static final String ACT_NOP = "X";
    private HSActionBean[] actionList = null;

    public HSActionBean[] getActionList()
    {
        return this.actionList;
    }

    public boolean thereIsActions()
    {
        return ((this.actionList != null) && (this.actionList.length > 0));
    }

    public void loadActionList() throws Exception
    {
        RecordSet rs = null;
        Record r = null;
        ArrayList<HSActionBean> tmp = new ArrayList<HSActionBean>();

        Object[] prm = { new Integer(INSERT), ACT_NOP };
        rs = DatabaseMgr.getInstance().executeQuery(null, getSqlQuery(), prm);

        if (rs != null)
        {
            for (int i = 0; i < rs.size(); i++)
            {
                try
                {
                    r = rs.get(i);
                    tmp.add(new HSActionBean(r));
                }
                catch (Exception e)
                {
                    EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                        EventDictionary.TYPE_WARNING, "D008", null);

                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
            }
        }

        this.actionList = new HSActionBean[tmp.size()];

        for (int i = 0; i < this.actionList.length; i++)
        {
            this.actionList[i] = tmp.get(i);
        }
    }
    public static List<HSActionBean> getHSActionList(int[] ids)
    {
    	RecordSet rs = null;
    	Record r = null;
        ArrayList<HSActionBean> result = new ArrayList<HSActionBean>();
        String sql = "select h.idhsaction,h.pvcode,h.idsite,h.idaction,h.priority,h.status,h.idvariable,h.isalarm,"+
	        		"h.starttime,h.endtime,h.inserttime,h.lastupdate,a.code,a.actioncode,a.actiontype,a.parameters,a.template "+
	        		"from hsaction as h,cfaction as a "+
	        		"where h.idaction=a.idaction and h.idsite=1 and idhsaction in(";
	    Object[] param = new Object[ids.length];
		for (int i = 0; i < ids.length; i++)
	    {
			sql += "?";
	        if (i != (ids.length - 1))
	        {
	            sql += ",";
	        }
	        param[i] = new Integer(ids[i]);
	    }
		sql += ")";
		try{
			rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
	        if (rs != null)
	        {
	            for (int i = 0; i < rs.size(); i++)
	            {
	                try
	                {
	                    r = rs.get(i);
	                    result.add(new HSActionBean(r));
	                }
	                catch (Exception e)
	                {
	                    EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
	                        EventDictionary.TYPE_WARNING, "D008", null);
	
	                    Logger logger = LoggerMgr.getLogger(HSActionBeanList.class);
	                    logger.error(e);
	                }
	            }
	        }
		}
        catch(DataBaseException ex)
        {
        	EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                    EventDictionary.TYPE_WARNING, "D008", null);
            Logger logger = LoggerMgr.getLogger(HSActionBeanList.class);
            logger.error(ex);
        }
        return result;
    }
    public void updateToLoadActionList(int[] key)
    {
        updateActionList(LOAD, key);
    }

    public void updateToSendActionList(int[] key)
    {
    	updateToSendActionList(key, false);
    }
    
    //Se variable � true, significa che l'update dello stato proviene dal set di  una variabile
    //gestita in modo asincrono
    public void updateToSendActionList(int[] key, boolean variable)
    {
    	//Effetto la retrieve degli idvariable dalla coda del dispatcher a partire da idhsaction
    	String sql = "select idvariable from hsaction where idhsaction = ?";
    	Integer idvariable = null;
    	RecordSet recordset = null;
    	for(int i = 0; i < key.length; i++)
    	{
    		try
    		{
    			recordset = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{new Integer(key[i])});
    			idvariable = (Integer)recordset.get(0).get(0);
//    			Aggiorno tutti i relativi allarmi
    	    	if (!variable)
	    		{
    	    		AlarmCtrl.notifyPVPRO(idvariable);
	    		}
    		}
    		catch(Exception e)
    		{
    			//Vado avanti con gli altri
    		}
    	}
    	
        updateActionList(SEND, key);
    }

    public void updateToDiscardActionList(int[] key)
    {
        updateActionList(DISCARD, key);
    }

    public void updateToCancelActionList(int[] key)
    {
        updateActionList(CANCEL, key);
    }

    private void updateActionList(int state, int[] key)
    {
        try
        {
            int[] params = new int[key.length + 1];
            params[0] = state;

            for (int i = 1; i < params.length; i++)
            {
                params[i] = key[i - 1];
            }

            Object[] obj = getParameters(params);
            Object[] par = new Object[obj.length + 1];
            par[0] = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < obj.length; i++)
            {
                par[i + 1] = obj[i];
            }

            DatabaseMgr.getInstance().executeStatement(null, getUpdateQuery(state, key.length), par);
        }
        catch (Exception e)
        {
            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }
    }

    private String getUpdateQuery(int state, int num)
    {
        String upd = "";
        upd = "update hsaction set lastupdate=?,status=? ";

        if (num > 0)
        {
            upd += "where idhsaction in (";

            for (int i = 0; i < num; i++)
            {
                upd += "?,";
            }

            upd = upd.substring(0, upd.length() - 1);
            upd += ")";
        }

        return upd;
    }

    private String getSqlQuery()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(
            "select h.idhsaction,h.pvcode,h.idsite,h.idaction,h.priority,h.status,h.idvariable,h.isalarm,");
        sb.append(
            "h.starttime,h.endtime,h.inserttime,h.lastupdate,a.code,a.actioncode,a.actiontype,a.parameters,a.template ");
        sb.append("from hsaction as h,cfaction as a ");
        sb.append(
            "where h.status=? and a.actiontype !=? and h.idaction=a.idaction order by h.priority");

        return sb.toString();
    }

    private Object[] getParameters(int[] par)
    {
        Object[] op = new Object[par.length];

        for (int i = 0; i < op.length; i++)
        {
            op[i] = new Integer(par[i]);
        }

        return op;
    }
}
