package com.carel.supervisor.controller.rule;

import com.carel.supervisor.action.AbstractAction;
import com.carel.supervisor.controller.database.RuleBean;
import com.carel.supervisor.controller.status.AbstractStatus;
import com.carel.supervisor.controller.status.StatusRegistry;
import com.carel.supervisor.controller.time.TimeValidity;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.VariableMgr;
import java.sql.Timestamp;


public class RemoteRule extends Rule
{
    public RemoteRule(RuleBean ruleBean, TimeValidity timeValidity, AbstractAction action)
    {
        super(ruleBean, VariableMgr.getInstance().getById(-1), timeValidity, action);
        contextStatus.setStatus(true); //Sono pronto a attivarmi
    }

    public RemoteRule(Integer idRule, int delay, Variable variable, TimeValidity timeValidity,
        AbstractAction action)
    {
        super(idRule, delay, VariableMgr.getInstance().getById(-1), timeValidity, action);
        contextStatus.setStatus(true); //Sono pronto a attivarmi
    }

    public RemoteRule(Integer idRule, Integer frequency, Integer idSite, Integer priority,
        int delay, Variable variable, TimeValidity timeValidity, AbstractAction action)
    {
        super(idRule, frequency, idSite, priority, delay, VariableMgr.getInstance().getById(-1),
            timeValidity, action);
        contextStatus.setStatus(true); //Sono pronto a attivarmi
    }

    public synchronized void executeActions(long actualTime)
        throws Exception
    {
    	//Retrieve dei dati da tabella di supporto e confronto con quello presente in tabella dei log
            
    	boolean status = RemoteRuleHelper.getInstance().existSomething(idRule);
        contextStatus.setStatus(status);
        executeActionsInner(actualTime);
    }

    private void executeActionsInner(long actualTime) throws Exception
    {
        contextStatus.setActualTime(actualTime);

        Timestamp now = new Timestamp(actualTime);
        boolean timebandOk = timeValidity.isValid(now);
        contextStatus.setTimebandOk(timebandOk);

        AbstractStatus nextStatus = null;

        while (!actualStatus.equals(nextStatus))
        {
            if (null != nextStatus)
            {
                actualStatus = nextStatus;
            }

            nextStatus = actualStatus.next(contextStatus);

            if (!nextStatus.equals(actualStatus))
            {
                nextStatus.execute(this, now);
            }
            else
            {
            	if (nextStatus.equals(StatusRegistry.alreadyManagedStatus))
            	{
            		contextStatus.setStatus(false);
            	}
            }
        }
    }
}
