package com.carel.supervisor.controller.status;

import java.util.Date;

import com.carel.supervisor.controller.database.RuleStateBean;
import com.carel.supervisor.controller.rule.Rule;
import com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl;


public class BlockedStatus extends AbstractStatus
{
    public BlockedStatus()
    {
        super(6);
    }

    public void execute(Rule rule, Date now) throws Exception
    {
    	rule.getAction().blockedStatus(rule, now);	
    }
    
    public void executeAfterRetrieve(Rule rule, Date now) throws Exception
    {
    	rule.getAction().aRblockedStatus(rule, now);	
    }
    
    public AbstractStatus next(ContextStatus contextState)
        throws Exception
    {
        if (contextState.isStatus())
        {
            return StatusRegistry.blockedStatus;
        }
        else
        {
            RuleStateBean.remove(contextState);
            contextState.setActivationTime(0);

//          Rimuoviamo l'allarme dalla tabella di controllo perchè rientrato
            AlarmCtrl.calledOff(contextState.getIdVar());
            
            return StatusRegistry.invalidStatus;
        }
    }
}
