package com.carel.supervisor.action;

import java.sql.Timestamp;
import java.util.Date;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.controller.rule.Rule;
import com.carel.supervisor.controller.status.AlreadyManagedStatus;
import com.carel.supervisor.dataaccess.dataconfig.SystemConf;
import com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr;
import com.carel.supervisor.dataaccess.dataconfig.UtilBean;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.db.SeqMgr;


public class ActionLog extends AbstractAction
{
    private Integer idAction = null;

    public ActionLog(Integer idAction)
    {
        super();
        this.idAction = idAction;
    }

    public void calledOffStatus(Rule rule, Date now) throws Exception
    {
        String sql = "INSERT into hsaction values (?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params = new Object[12];
        params[0] = SeqMgr.getInstance().next(null, "hsaction", "idhsaction");
        params[1] = BaseConfig.getPlantId();
        params[2] = new Integer(1); //IDSITE del locale
        params[3] = idAction;
        params[4] = rule.getIdVar();
        params[5] = UtilBean.writeBoolean(rule.isAlarmForActionLog());
        params[6] = new Timestamp(rule.getStartTime());
        params[7] = now;
        params[8] = rule.getPriority();
        params[9] = new Integer(1); //INSERT
        params[10] = now;
        params[11] = now;
        DatabaseMgr.getInstance().executeStatement(null, sql, params);
    }

    public void toManageStatus(Rule rule, Date now) throws Exception
    {
    	//kevin, 2014-8-26
    	//if it is resendNotification
    	//need to check if the action's channel need to resend notification
    	if(  rule.getContextStatus().isResendNotification()!=null &&
    			rule.getContextStatus().isResendNotification())
    	{
    			String sql = "select actiontype from cfaction where idaction=? ";
    			Object[] params = new Object[]{idAction};
				RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, params);
				if(rs.size()>0){
					SystemConf cfg = SystemConfMgr.getInstance().get("resend_channel_"+(String)rs.get(0).get(0));
					if (cfg == null || (cfg != null && !cfg.getValue().equals("TRUE"))){
							return;
					}
				}
    	}
    		
        String sql = "INSERT into hsaction values (?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params = new Object[12];
        params[0] = SeqMgr.getInstance().next(null, "hsaction", "idhsaction");
        params[1] = BaseConfig.getPlantId();
        params[2] = new Integer(1); //IDSITE del locale
        params[3] = idAction;
        params[4] = rule.getIdVar();
        params[5] = UtilBean.writeBoolean(rule.isAlarmForActionLog());
        params[6] = new Timestamp(rule.getStartTime());
        params[7] = null;
        params[8] = rule.getPriority();
        params[9] = new Integer(1); //INSERT
        params[10] = now;
        params[11] = now;
        DatabaseMgr.getInstance().executeStatement(null, sql, params);
    }
    
    public void blockedStatus(Rule rule, Date now) throws Exception
    {
    	String sql = "select * from cfaction where actiontype='L' and idaction = ? ";
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql, new Object[]{this.idAction});
    	
        if (rs.size()>0)   //IF THERE IS A RELE ACTION
        
        // if rule is associated to RELAY action, we have the same code of "calledOffStatus" method 
        // to set relay to initial condition. 
        {
         //works only with "Automatic" reset type
        	if(rule.getContextStatus().getActualStatusBeforeManualReset() instanceof AlreadyManagedStatus)
        		calledOffStatus(rule,now);
        }
    	
    }
}
