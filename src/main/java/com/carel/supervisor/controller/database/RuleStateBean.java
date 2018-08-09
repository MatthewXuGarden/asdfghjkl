package com.carel.supervisor.controller.database;

import com.carel.supervisor.controller.status.AbstractStatus;
import com.carel.supervisor.controller.status.ContextStatus;
import com.carel.supervisor.controller.status.SavedStatus;
import com.carel.supervisor.controller.status.StatusRegistry;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import java.sql.Timestamp;
import java.util.*;


public class RuleStateBean
{
    private static final String IDRULE = "idrule";
    private static final String IDVAR = "idvar";
    private static final String INSERTTIME = "inserttime";
    private static final String STATUS = "status";
    private Integer idRule = null;
    private Integer idVar = null;
    private Timestamp inserttime = null;
    private int status = 0;

    public RuleStateBean(Record record)
    {
        idRule = (Integer) record.get(IDRULE);
        idVar = (Integer) record.get(IDVAR);
        inserttime = (Timestamp) record.get(INSERTTIME);
        status = ((Integer) record.get(STATUS)).intValue();
    }

    public static void save(ContextStatus contextState, AbstractStatus abstractState)
        throws Exception
    {
        if (contextState.isPersistent())
        {
            String sql = "INSERT into rulestate values (?,?,?,?)";
            Object[] values = new Object[4];
            values[0] = contextState.getIdVar();
            values[1] = contextState.getIdRule();
            values[2] = new Integer(StatusRegistry.get(abstractState));
            values[3] = new Timestamp(contextState.getActivationTime());
            DatabaseMgr.getInstance().executeStatement(null, sql, values);
        }
    }

    public static void update(ContextStatus contextState, AbstractStatus abstractState)
        throws Exception
    {
        if (contextState.isPersistent())
        {
            String sql = "UPDATE rulestate set status = ?, inserttime = ? where idvar = ? and idrule = ?";
            Object[] values = new Object[4];
            values[0] = new Integer(StatusRegistry.get(abstractState));
            values[1] = new Timestamp(contextState.getActivationTime());
            values[2] = contextState.getIdVar();
            values[3] = contextState.getIdRule();
            DatabaseMgr.getInstance().executeStatement(null, sql, values);
        }
    }

    public static void remove(ContextStatus contextState)
        throws Exception
    {
        if (contextState.isPersistent())
        {
            String sql = "DELETE from rulestate where idrule = ? and idvar = ?";
            Object[] values = new Object[2];
            values[0] = contextState.getIdRule();
            values[1] = contextState.getIdVar();
            DatabaseMgr.getInstance().executeStatement(null, sql, values);
        }
    }

    public static void remove(Integer idvar, Integer idrule) throws Exception
    {
        String sql = "DELETE from rulestate where idrule = ? and idvar = ?";
        Object[] values = new Object[2];
        values[0] = idrule;
        values[1] = idvar;
        DatabaseMgr.getInstance().executeStatement(null, sql, values);
    }

    public static void removeAll() throws Exception
    {
        String sql = "DELETE from rulestate";
        DatabaseMgr.getInstance().executeStatement(null, sql, null);
    }

    public static List<SavedStatus> retrieveStatus() throws Exception
    {
        String sql = "SELECT * from  rulestate order by idvar, idrule";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql);
        List<SavedStatus> list = new ArrayList<SavedStatus>();

        if ((null != recordset) && (0 < recordset.size()))
        {
            Record record = null;
            RuleStateBean ruleStateBean = null;
            SavedStatus savedState = null;

            for (int i = 0; i < recordset.size(); i++)
            {
                record = recordset.get(i);
                ruleStateBean = new RuleStateBean(record);
                savedState = new SavedStatus(ruleStateBean);
                list.add(savedState);
            }
        }

        return list;
    }

    /**
     * @return: int
     */
    public Integer getIdRule()
    {
        return idRule;
    }

    /**
     * @param idRule
     */
    public void setIdRule(Integer idRule)
    {
        this.idRule = idRule;
    }

    /**
     * @return: Timestamp
     */
    public Date getInserttime()
    {
        return inserttime;
    }

    /**
     * @param inserttime
     */
    public void setInserttime(Timestamp inserttime)
    {
        this.inserttime = inserttime;
    }

    /**
     * @return: int
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(int status)
    {
        this.status = status;
    }

    /**
     * @return: Integer
     */
    public Integer getIdVar()
    {
        return idVar;
    }

    /**
     * @param idVar
     */
    public void setIdVar(Integer idVar)
    {
        this.idVar = idVar;
    }
}
