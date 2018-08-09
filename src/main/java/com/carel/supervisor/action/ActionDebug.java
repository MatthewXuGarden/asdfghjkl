package com.carel.supervisor.action;

import java.util.Date;

import com.carel.supervisor.controller.rule.Rule;


public class ActionDebug extends AbstractAction
{
    public void invalidStatus(Rule rule, Date now) throws Exception
    {
    	System.out.println("INVALID STATUS");
    }
	
	public void activeStatus(Rule rule, Date now) throws Exception
    {
		System.out.println("ACTIVE STATUS");
    }

    public void readyToManageStatus(Rule rule, Date now) throws Exception
    {
    	System.out.println("READY TO MANAGE STATUS");
    }

    public void toManageStatus(Rule rule, Date now) throws Exception
    {
    	System.out.println("TO MANAGE STATUS");
    }
    
    public void alreadyManagedStatus(Rule rule, Date now) throws Exception
    {
    	System.out.println("ALREADY MANAGED STATUS");
    }
    
    public void calledOffStatus(Rule rule, Date now) throws Exception
    {
    	System.out.println("CALLED OFF STATUS");
    }
    
    public void blockedStatus(Rule rule, Date now) throws Exception
    {
    	System.out.println("BLOCKED STATUS");
    }
}
