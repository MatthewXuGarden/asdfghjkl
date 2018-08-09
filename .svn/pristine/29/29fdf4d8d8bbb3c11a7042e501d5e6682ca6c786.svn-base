package com.carel.supervisor.action;

import com.carel.supervisor.base.config.BaseConfig;
import com.carel.supervisor.controller.database.*;


public class ActionFactory
{
    private ActionFactory()
    {
    }

    public static AbstractAction createAction(ActionBean actionBean)
    {
        if (null != actionBean.getNext())
        {
            ActionsGroup abstractAction = factoryGroup(actionBean.getIdAction());
            abstractAction.setNext(createAction(actionBean.getNext()));

            return abstractAction;
        }
        else
        {
            AbstractAction abstractAction = factory(actionBean.getIdAction());

            return abstractAction;
        }
    }

    public static ActionsGroup factoryGroup(Integer idAction)
    {
        return new ActionsGroup(factory(idAction));
    }

    public static AbstractAction factory(Integer idAction)
    {
    	return new ActionLog(idAction);
    }
}
