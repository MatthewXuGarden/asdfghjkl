package com.carel.supervisor.controller.status;

import java.util.Date;

import com.carel.supervisor.controller.database.RuleStateBean;
import com.carel.supervisor.controller.rule.Rule;


public class ToManageStatus extends AbstractStatus
{
    public ToManageStatus()
    {
        super(3);
    }

    public void execute(Rule rule, Date now) throws Exception
    {
    	// set toManagerStatus execute time 
    	rule.getContextStatus().setLastExecuteTime(now.getTime());
    	rule.getAction().toManageStatus(rule, now);	
    }
    
    public AbstractStatus next(ContextStatus contextState)
        throws Exception
    {
        RuleStateBean.update(contextState, StatusRegistry.alreadyManagedStatus);

        return StatusRegistry.alreadyManagedStatus;
    }
}
