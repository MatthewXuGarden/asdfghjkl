package com.carel.supervisor.controller.status;

import java.util.Date;

import com.carel.supervisor.controller.rule.Rule;

public abstract class AbstractStatus
{
    private int code = 0;

    public AbstractStatus(int code)
    {
        this.code = code;
    }

    public abstract AbstractStatus next(ContextStatus contextState) throws Exception;
    
    public abstract void execute(Rule rule, Date now) throws Exception;
    
    public void executeAfterRetrieve(Rule rule, Date now) throws Exception
    {
    	//Empty implementation
    }
    
    public boolean equals(Object abstractState)
    {
        if (null == abstractState)
        {
        	return false;
        }
    	AbstractStatus value = (AbstractStatus) abstractState;

        return (value.code == this.code);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		return result;
	}

}
