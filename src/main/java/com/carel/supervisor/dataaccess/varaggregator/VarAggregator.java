package com.carel.supervisor.dataaccess.varaggregator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.carel.supervisor.dataaccess.datalog.impl.VarphyBean;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.bo.BDevDetail;


public class VarAggregator
{
    List<VarphyBean> varList = new ArrayList<VarphyBean>();

    public VarAggregator(int idsite, String language, int iddevice)
    throws DataBaseException
    {
    	this(idsite, language, iddevice, false);
    }
    
    public VarAggregator(int idsite, String language, int iddevice, boolean toPropagate)
        throws DataBaseException
    {
        //lista padri
        ArrayList<VarphyBean> master = new ArrayList<VarphyBean>();
        String sql =
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and " +
            "cftableext.tablename='cfvariable' and cftableext.languagecode = ?  " +
            "and cfvariable.idsite = ? and cfvariable.idhsvariable is not null and cfvariable.iddevice = ? and cfvariable.type != 4 and cfvariable.iscancelled=? " +
            "and cftableext.idsite = ? ";
            
        if (!toPropagate)
        	sql += "order by cftableext.shortdescr, cftableext.description"; //vecchia versione per ordinare descriz. in pagina
        else
            sql += "order by cfvariable.idvarmdl"; //quando propago devo far corrispondere idvarmdl e non short+description
        
        Object[] param = new Object[5];
        param[0] = language;
        param[1] = new Integer(idsite);
        param[2] = new Integer(iddevice);
        param[3] = "FALSE";
        param[4] = new Integer(idsite);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        for (int i = 0; i < rs.size(); i++)
        {
            master.add(new VarphyBean(rs.get(i)));
        }

        //lista figli
        List<VarphyBean> slave = new ArrayList<VarphyBean>();
        sql = "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and " +
            "cftableext.tablename='cfvariable' and cftableext.languagecode = ?  " +
            "and cfvariable.idsite = ? and cfvariable.idhsvariable is null and cfvariable.iddevice = ? and cfvariable.type != 4 and cfvariable.iscancelled=? " +
            //"and cftableext.idsite = ? order by cftableext.shortdescr, cftableext.description";
            "and cftableext.idsite = ? ";
        
        if (!toPropagate)
        	sql += "order by cftableext.shortdescr, cftableext.description";
        else
            sql += "order by cfvariable.idvarmdl";
        
        param = new Object[5];
        param[0] = language;
        param[1] = new Integer(idsite);
        param[2] = new Integer(iddevice);
        param[3] = "FALSE";
        param[4] = new Integer(idsite);

        rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        for (int i = 0; i < rs.size(); i++)
        {
            slave.add(new VarphyBean(rs.get(i)));
        }

        VarphyBean tmp = null;
        VarphyBean son = null;
        int idhs = -1;

        for (int i = 0; i < master.size(); i++) //scorro lista padri
        {
            tmp = master.get(i);

            if (tmp.getIdhsvariable().intValue() != 0) //se variabile ha un figlio
            {
                idhs = tmp.getIdhsvariable().intValue();

                if (idhs != -1)
                {
                    for (int j = 0; j < slave.size(); j++) //scorro lista figli finch� trovo il figlio corrispondente
                    {
                        son = slave.get(j);

                        if (son.getId().intValue() == idhs)
                        {
                            tmp.setSon(son);
                            slave.remove(j);

                            break;
                        }
                    }
                }
            }
        }

        varList = master;
    }

    public static VarphyBean[] retrieveByPriority(int idsite, String language,
        int iddevice) throws DataBaseException
    {
        List<VarphyBean> varList = new ArrayList<VarphyBean>();
        String sql =
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and " +
            "cftableext.tablename='cfvariable' and cftableext.languagecode = ? and cfvariable.idhsvariable is not null " +
            "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.type != 4 and cfvariable.iscancelled=? " +
            "and cftableext.idsite = ? order by cfvariable.todisplay, cfvariable.priority,cftableext.description";
        Object[] param = new Object[5];
        param[0] = language;
        param[1] = new Integer(idsite);
        param[2] = new Integer(iddevice);
        param[3] = "FALSE";
        param[4] = new Integer(idsite);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
        VarphyBean o = null;

        for (int i = 0; i < rs.size(); i++)
        {
            o = new VarphyBean(rs.get(i));
            varList.add(o);
        }

        VarphyBean[] list = new VarphyBean[varList.size()];
        list = varList.toArray(list);

        return list;
    }

    public static VarphyBean[] retrieveByDescription(int idsite, String language, int iddevice) throws DataBaseException
    {
        List<VarphyBean> varList = new ArrayList<VarphyBean>();
        String sql =
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and " +
            "cftableext.tablename='cfvariable' and cftableext.languagecode = ? and cfvariable.idhsvariable is not null " +
            "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.type != 4 and cfvariable.iscancelled=? " +
            "and cftableext.idsite = ? order by cftableext.description";
        Object[] param = new Object[5];
        param[0] = language;
        param[1] = new Integer(idsite);
        param[2] = new Integer(iddevice);
        param[3] = "FALSE";
        param[4] = new Integer(idsite);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
        VarphyBean o = null;

        for (int i = 0; i < rs.size(); i++)
        {
            o = new VarphyBean(rs.get(i));
            varList.add(o);
        }

        VarphyBean[] list = new VarphyBean[varList.size()];
        list = varList.toArray(list);

        return list;
    }

    
    public static VarphyBean[] retrieveByPriorityAndDescription(int idsite, String language, int iddevice) throws DataBaseException
    {
        List<VarphyBean> varList = new ArrayList<VarphyBean>();
        String sql =
            "select cfvariable.*, cftableext.description, cftableext.longdescr,cftableext.shortdescr from cfvariable " +
            "inner join cftableext on cfvariable.idvariable=cftableext.tableid and " +
            "cftableext.tablename='cfvariable' and cftableext.languagecode = ? and cfvariable.idhsvariable is not null " +
            "and cfvariable.idsite = ? and cfvariable.iddevice = ? and cfvariable.type != 4 and cfvariable.iscancelled=? " +
            "and cftableext.idsite = ? order by cfvariable.priority, cftableext.description";
        Object[] param = new Object[5];
        param[0] = language;
        param[1] = new Integer(idsite);
        param[2] = new Integer(iddevice);
        param[3] = "FALSE";
        param[4] = new Integer(idsite);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
        VarphyBean o = null;

        for (int i = 0; i < rs.size(); i++)
        {
            o = new VarphyBean(rs.get(i));
            varList.add(o);
        }

        VarphyBean[] list = new VarphyBean[varList.size()];
        list = varList.toArray(list);

        return list;
    }    
    
    
    public VarphyBean[] getVarList()
    {
        VarphyBean[] listVar = new VarphyBean[varList.size()];
        listVar = varList.toArray(listVar);

        return listVar; //ritorno lista var
    }

    public static short getKeyMax(int idsite, int idvariable, int freq)
        throws DataBaseException
    {
        String sql = "select keymax from buffer where idsite = ? and idvariable = ?";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idvariable);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        short keymax = ((Short) rs.get(0).get("keymax")).shortValue();

        return keymax;
    }

    public static int getPeriod(int idsite, int idvariable, int freq)
        throws DataBaseException
    {
        short keymax = getKeyMax(idsite, idvariable, freq);
        int period = (int) ((keymax * freq * 64) / 1.1);

        return period;
    }
    
    public static int getPeriod(int keymax,int freq)
    {
    	return (int) ((keymax * freq * 64) / 1.1);
    }

    public static void updateIsHACCP(int idsite, int idvar, String ishaccp)
	    throws DataBaseException
	{
	    String sql = "update cfvariable set ishaccp = ?, frequency = ?, delta = ? where idsite = ? and idvariable = ?";
	    Object[] param = new Object[5];
	    param[0] = ishaccp;
	    param[1] = (ishaccp.equals("TRUE")) ? new Integer(BDevDetail.HACCP_FREQ) : null;
	    param[2] = (ishaccp.equals("TRUE")) ? new Integer(-1) : null;
	    param[3] = new Integer(idsite);
	    param[4] = new Integer(idvar);
	
	    DatabaseMgr.getInstance().executeStatement(null, sql, param);
	}

    public static void updateFrequency(int idsite, int idvar, int freq)
        throws DataBaseException
    {
        String sql = "update cfvariable set frequency = ?, lastupdate = ? where idsite = ? and idvariable = ?";
        Object[] param = new Object[4];

        if (freq == 0)
        {
            param[0] = null;
        }
        else
        {
            param[0] = new Integer(freq);
        }

        param[1] = new Timestamp(System.currentTimeMillis());
        param[2] = new Integer(idsite);
        param[3] = new Integer(idvar);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }

    public static void updateIdHsVariable(int idsite, int idmaster, int idslave)
        throws DataBaseException
    {
        String sql = "update cfvariable set idhsvariable = ?, lastupdate = ? where idsite = ? and idvariable = ?";
        Object[] param = new Object[4];
        param[0] = new Integer(idslave);
        param[1] = new Timestamp(System.currentTimeMillis());
        param[2] = new Integer(idsite);
        param[3] = new Integer(idmaster);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }

    public static void cancelHsVariable(int idsite, int idmaster, int idslave)
        throws DataBaseException
    {
        //setto iscancelled = TRUE la slave
        String sql = "update cfvariable set iscancelled = ?, lastupdate = ? where idsite = ? and idvariable = ?";
        Object[] param = new Object[4];
        param[0] = "TRUE";
        param[1] = new Timestamp(System.currentTimeMillis());
        param[2] = new Integer(idsite);
        param[3] = new Integer(idslave);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);

        //setto a null il campo idhsvariable della master
        sql = "update cfvariable set idhsvariable = ?, lastupdate = ? where idsite = ? and idvariable = ?";
        param = new Object[4];
        param[0] = new Integer(-1);
        param[1] = new Timestamp(System.currentTimeMillis());
        param[2] = new Integer(idsite);
        param[3] = new Integer(idmaster);
        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }

    public static boolean existInBuffer(int idsite, int idvariable)
        throws DataBaseException
    {
        String sql = "select count(1) from buffer where idsite = ? and idvariable = ?";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idvariable);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);

        if (((Integer) rs.get(0).get("count")).intValue() == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static int getBufferKeyActual(int idsite, int idvariable)
        throws DataBaseException
    {
        String sql = "select keyactual from buffer where idsite = ? and idvariable = ?";
        Object[] param = new Object[2];
        param[0] = new Integer(idsite);
        param[1] = new Integer(idvariable);

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, param);
        int n = ((Short) rs.get(0).get("keyactual")).intValue();
        return n;
    }

    public static void updateFreqAndVarMin(int idsite, int idvar, int freq,
        double varmin) throws DataBaseException
    {
        String sql = "update cfvariable set frequency = ?,delta = ?, lastupdate = ? where idsite = ? and idvariable = ?";
        Object[] param = new Object[5];

        if (freq == 0)
        {
            param[0] = null;
        }
        else
        {
            param[0] = new Integer(freq);
        }

        if (varmin == 0)
        {
            param[1] = new Double(0);
        }
        else
        {
            param[1] = new Double(varmin);
        }

        param[2] = new Timestamp(System.currentTimeMillis());
        param[3] = new Integer(idsite);
        param[4] = new Integer(idvar);

        DatabaseMgr.getInstance().executeStatement(null, sql, param);
    }

    public static boolean isVaribleInReorder(int idsite, int idvariable)
        throws DataBaseException
    {
        String sql = "select count(1) from reorder where idsite = ? and idvariable = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), new Integer(idvariable) });

        int count = ((Integer) rs.get(0).get("count")).intValue();

        if (count == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    
    // get correct period when new period was set in reorder table (not in buffer table)
    public static int getNewPeriod(int idsite, int idvariable)
    throws DataBaseException
    {
        String sql = "select * from reorder where idsite = ? and idvariable = ?";
        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[] { idsite, idvariable });
        return ((Integer)rs.get(0).get("historicalperiod")).intValue();
    }
    
}
