package com.carel.supervisor.controller.database;

import com.carel.supervisor.dataaccess.db.*;
import java.util.*;


public class ActionList
{
    private Map actions = new HashMap();

    public ActionList(String dbId, String plantId, Integer idSite)
        throws DataBaseException
    {
        String sql = "select * from cfaction where pvcode = ? and idsite = ? order by actioncode";
        RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(dbId, sql,
                new Object[] { plantId, idSite });
        Record record = null;
        ActionBean actionBean = null;
        ActionBean previousActionBean = null;

        for (int i = 0; i < recordSet.size(); i++)
        {
            record = recordSet.get(i);
            actionBean = new ActionBean(record);

            if (null == previousActionBean)
            {
                previousActionBean = actionBean;
                actions.put(new Integer(actionBean.getActioncode()), actionBean);
            }
            else
            {
                if (previousActionBean.getActioncode() == actionBean.getActioncode())
                {
                    previousActionBean.setNext(actionBean);
                    previousActionBean = actionBean;
                }
                else
                {
                    previousActionBean = actionBean;
                    actions.put(new Integer(actionBean.getActioncode()),
                        actionBean);
                }
            }
        }
    }

    public ActionBean get(Integer id)
    {
        return (ActionBean) actions.get(id);
    }

    public int size()
    {
        return actions.size();
    }
}
