package com.carel.supervisor.controller.status;

import java.util.Date;

import com.carel.supervisor.controller.database.RuleStateBean;
import com.carel.supervisor.controller.rule.Rule;
import com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl;


public class CalledOffStatus extends AbstractStatus
{
    public CalledOffStatus()
    {
        super(5);
    }

    public void execute(Rule rule, Date now) throws Exception
    {
    	rule.getAction().calledOffStatus(rule, now);	
    }
    
    public AbstractStatus next(ContextStatus contextState)
        throws Exception
    {
        contextState.setActivationTime(0);
        RuleStateBean.remove(contextState);

//      Rimuoviamo l'allarme dalla tabella di controllo perchè rientrato
        AlarmCtrl.calledOff(contextState.getIdVar());
        
        return StatusRegistry.invalidStatus;
    }
}
