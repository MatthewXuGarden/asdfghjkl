package com.carel.supervisor.controller.status;

import java.util.Date;

import com.carel.supervisor.controller.database.RuleStateBean;
import com.carel.supervisor.controller.rule.Rule;


public class InvalidStatus extends AbstractStatus
{
    public InvalidStatus()
    {
        super(0);
    }

    public void execute(Rule rule, Date now) throws Exception
    {
    	rule.getAction().invalidStatus(rule, now);	
    }
    
    public AbstractStatus next(ContextStatus contextState)
        throws Exception
    {
        if (!contextState.isStatus())
        {
            //RuleStateBean.remove(contextState);
            return StatusRegistry.invalidStatus;
        }
        else
        {
        	//reset the acknowledge flag
        	contextState.setAcknowledged(false);
            // reset if need re-send alarm notification
        	contextState.setResendNotification(null);
            contextState.setActivationTime(contextState.getActualTime());
            RuleStateBean.save(contextState, StatusRegistry.activeStatus);

            return StatusRegistry.activeStatus;
        }
    }
}
