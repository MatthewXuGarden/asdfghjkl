package com.carel.supervisor.action;

import com.carel.supervisor.controller.rule.Rule;
import java.util.Date;


public class ActionsGroup extends AbstractAction
{
    private AbstractAction next = null;
    private AbstractAction abstractAction = null;

    public ActionsGroup(AbstractAction abstractAction)
    {
        super();
        this.abstractAction = abstractAction;
    }

    public void setNext(AbstractAction next)
    {
        this.next = next;
    }

    public void invalidStatus(Rule rule, Date now) throws Exception
    {
    	abstractAction.invalidStatus(rule, now);

        if (null != next)
        {
            next.invalidStatus(rule, now);
        }
    }
	
	public void activeStatus(Rule rule, Date now) throws Exception
    {
		abstractAction.activeStatus(rule, now);

        if (null != next)
        {
            next.activeStatus(rule, now);
        }
    }

    public void readyToManageStatus(Rule rule, Date now) throws Exception
    {
    	abstractAction.readyToManageStatus(rule, now);

        if (null != next)
        {
            next.readyToManageStatus(rule, now);
        }
    }

    public void toManageStatus(Rule rule, Date now) throws Exception
    {
    	abstractAction.toManageStatus(rule, now);

        if (null != next)
        {
            next.toManageStatus(rule, now);
        }
    }
    
    public void alreadyManagedStatus(Rule rule, Date now) throws Exception
    {
    	abstractAction.alreadyManagedStatus(rule, now);

        if (null != next)
        {
            next.alreadyManagedStatus(rule, now);
        }
    }
    
    public void calledOffStatus(Rule rule, Date now) throws Exception
    {
    	abstractAction.calledOffStatus(rule, now);

        if (null != next)
        {
            next.calledOffStatus(rule, now);
        }
    }
    
    public void blockedStatus(Rule rule, Date now) throws Exception
    {
    	abstractAction.blockedStatus(rule, now);

        if (null != next)
        {
            next.blockedStatus(rule, now);
        }
    }
   
}
