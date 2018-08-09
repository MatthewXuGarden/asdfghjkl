package com.carel.supervisor.action;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.controller.rule.RemoteRuleHelper;
import com.carel.supervisor.controller.rule.Rule;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.Record;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import java.sql.Timestamp;
import java.util.Date;


public class ActionRemote extends AbstractAction
{
    private Integer idAction = null;

    public ActionRemote(Integer idAction)
    {
        super();
        this.idAction = idAction;
    }

    public void calledOffStatus(Rule rule, Date now) throws Exception
    {
    }
    
    public void alreadyManagedStatus(Rule rule, Date now) throws Exception
    {
    	RemoteRuleHelper.getInstance().refresh(rule.getIdRule(), now);
    }
    
    public void toManageStatus(Rule rule, Date now) throws Exception
    {
    	Timestamp t = RemoteRuleHelper.getInstance().getLastCheck(rule.getIdRule());
        String sql = "select idsite, idvariable, starttime, endtime from hsalarm where lastupdate>?";
        RecordSet recordset = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{t});
        Record record = null;
        Object[][] params = new Object[recordset.size()][12];
        sql = "INSERT into hsaction values (?,?,?,?,?,?,?,?,?,?,?,?)";
        for (int i = 0; i < recordset.size(); i++)
        {
            record = recordset.get(i);
            params[i][0] = SeqMgr.getInstance().next(null, "hsaction", "idhsaction");
            params[i][1] = BaseConfig.getPlantId();
            params[i][2] = (Integer)record.get("idsite");
            params[i][3] = idAction;
            params[i][4] = (Integer)record.get("idvariable");
            params[i][5] = UtilBean.writeBoolean(true);
            params[i][6] = (Timestamp)record.get("starttime");
            params[i][7] = (Timestamp)record.get("endtime");
            params[i][8] = rule.getPriority();
            params[i][9] = new Integer(1); //INSERT
            params[i][10] = now;
            params[i][11] = now;
        }
        if (0 < recordset.size())
        {
        	DatabaseMgr.getInstance().executeMultiStatement(null, sql, params);
        }
    }
}
