package com.carel.supervisor.controller.status;

import com.carel.supervisor.controller.database.RuleStateBean;


public class SavedStatus
{
    private long activationTime = 0;
    private Integer idRule = null;
    private Integer idVar = null;
    private AbstractStatus abstractState = null;

    public SavedStatus(RuleStateBean ruleStateBean)
    {
        idRule = ruleStateBean.getIdRule();
        idVar = ruleStateBean.getIdVar();
        activationTime = ruleStateBean.getInserttime().getTime();
        abstractState = StatusRegistry.get(ruleStateBean.getStatus());
    }

    /**
     * @return: AbstractState
     */
    public AbstractStatus getState()
    {
        return abstractState;
    }

    /**
     * @return: long
     */
    public long getActivationTime()
    {
        return activationTime;
    }

    /**
     * @return: Integer
     */
    public Integer getIdRule()
    {
        return idRule;
    }

    /**
     * @return: Integer
     */
    public Integer getIdVar()
    {
        return idVar;
    }
}
