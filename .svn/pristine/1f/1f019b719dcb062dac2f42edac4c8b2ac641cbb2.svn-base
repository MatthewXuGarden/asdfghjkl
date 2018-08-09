package com.carel.supervisor.controller.rule;

import com.carel.supervisor.action.AbstractAction;
import com.carel.supervisor.controller.database.RuleBean;
import com.carel.supervisor.controller.status.AbstractStatus;
import com.carel.supervisor.controller.time.TimeValidity;
import com.carel.supervisor.field.Variable;
import com.carel.supervisor.field.VariableMgr;
import java.sql.Timestamp;


public class ScheduledRule extends Rule
{
    public ScheduledRule(RuleBean ruleBean, TimeValidity timeValidity, AbstractAction action)
    {
        super(ruleBean, VariableMgr.getInstance().getById(0), timeValidity, action);
        contextStatus.setStatus(true); //Sono pronto a attivarmi
    }

    public ScheduledRule(Integer idRule, int delay, Variable variable, TimeValidity timeValidity,
        AbstractAction action)
    {
        super(idRule, delay, VariableMgr.getInstance().getById(0), timeValidity, action);
        contextStatus.setStatus(true); //Sono pronto a attivarmi
    }

    public ScheduledRule(Integer idRule, Integer frequency, Integer idSite, Integer priority,
        int delay, Variable variable, TimeValidity timeValidity, AbstractAction action)
    {
        super(idRule, frequency, idSite, priority, delay, VariableMgr.getInstance().getById(0),
            timeValidity, action);
        contextStatus.setStatus(true); //Sono pronto a attivarmi
    }

    public synchronized void executeActions(long actualTime)
        throws Exception
    {
        Timestamp now = new Timestamp(actualTime); //Verifico se sono ok in fasci temporale
        boolean timebandOk = timeValidity.isValid(now);
        contextStatus.setTimebandOk(timebandOk);

        if (timebandOk)
        {
            if (contextStatus.isStatus()) //Parto
            {
                contextStatus.setActualTime(actualTime);

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
                }

                contextStatus.setStatus(false); // non mi devo più attivare nei giri successivi
            }
        }
        else
        {
            if (!contextStatus.isStatus()) //Termino
            {
                contextStatus.setActualTime(actualTime);

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
                }
            }

            contextStatus.setStatus(true); //Sono pronto a attivarmi
        }
    }
}
