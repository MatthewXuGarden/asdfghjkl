package com.carel.supervisor.controller.status;

import java.util.Date;

import com.carel.supervisor.controller.database.RuleStateBean;
import com.carel.supervisor.controller.rule.Rule;
import com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl;


public class ActiveStatus extends AbstractStatus
{
    public ActiveStatus()
    {
        super(1);
    }
    
    public void execute(Rule rule, Date now) throws Exception
    {
    	rule.getAction().activeStatus(rule, now);	
    }
    
    public void executeAfterRetrieve(Rule rule, Date now) throws Exception
    {
    	rule.getAction().aRactiveStatus(rule, now);	
    }
    
    public AbstractStatus next(ContextStatus contextState)
        throws Exception
    {
        if (!contextState.isStatus())
        {
            RuleStateBean.remove(contextState);
            contextState.setActivationTime(0);
            //Rimuoviamo l'allarme dalla tabella di controllo perch� rientrato
            AlarmCtrl.calledOff(contextState.getIdVar());
            return StatusRegistry.invalidStatus;
        }
        else
        {
            if (contextState.isManualCancel())
            {
            	contextState.setActualStatusBeforeManualReset(StatusRegistry.activeStatus);
                contextState.setManualCancel(false);
                RuleStateBean.update(contextState,
                    StatusRegistry.alreadyManagedStatus);
                
                //Rimuoviamo l'allarme dalla tabella di controllo perch� resettato dall'utente
                AlarmCtrl.userReset(contextState.getIdVar());
                
                return StatusRegistry.alreadyManagedStatus;
            }
            else
            {
                if ((contextState.getActualTime() -
                        contextState.getActivationTime()) < contextState.getDelay())
                {
                    return StatusRegistry.activeStatus;
                }
                else
                {
                    RuleStateBean.update(contextState,
                        StatusRegistry.readyToManageStatus);

                    return StatusRegistry.readyToManageStatus;
                }
            }
        }
    }
}
