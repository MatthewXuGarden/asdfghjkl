package com.carel.supervisor.controller.status;

import java.util.Date;

import com.carel.supervisor.controller.database.RuleStateBean;
import com.carel.supervisor.controller.rule.Rule;
import com.carel.supervisor.dataaccess.alarmctrl.AlarmCtrl;


public class ReadyToManageStatus extends AbstractStatus
{
    public ReadyToManageStatus()
    {
        super(2);
    }

    public void execute(Rule rule, Date now) throws Exception
    {
    	rule.getAction().readyToManageStatus(rule, now);	
    }
    
    public void executeAfterRetrieve(Rule rule, Date now) throws Exception
    {
    	rule.getAction().aRalreadyManagedStatus(rule, now);	
    }
    
    public AbstractStatus next(ContextStatus contextState)
        throws Exception
    {
        if (!contextState.isStatus())
        {
            contextState.setActivationTime(0);
            RuleStateBean.remove(contextState);
            //Rimuoviamo l'allarme dalla tabella di controllo perch� rientrato
            AlarmCtrl.calledOff(contextState.getIdVar());
            
            return StatusRegistry.invalidStatus;
        }
        else
        {
            if (contextState.isManualCancel())
            {
            	contextState.setActualStatusBeforeManualReset(StatusRegistry.readyToManageStatus);
                contextState.setManualCancel(false);
                RuleStateBean.update(contextState,
                    StatusRegistry.alreadyManagedStatus);

//              Rimuoviamo l'allarme dalla tabella di controllo perch� resettato dall'utente
                AlarmCtrl.userReset(contextState.getIdVar());
                
                return StatusRegistry.alreadyManagedStatus;
            }
            else
            {
                if (contextState.isTimebandOk())
                {
                    return StatusRegistry.toManageStatus;
                }
                else
                {
                    return StatusRegistry.readyToManageStatus;
                }
            }
        }
    }
}
