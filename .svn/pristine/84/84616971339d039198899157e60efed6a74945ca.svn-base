package com.carel.supervisor.dispatcher.main;

import com.carel.supervisor.base.factory.FactoryObject;
import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.event.EventDictionary;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.dispatcher.DispatcherMgr;
import com.carel.supervisor.dispatcher.action.DispatcherAction;
import com.carel.supervisor.dispatcher.bean.HSActionBean;
import java.sql.Timestamp;
import java.util.Properties;


public class DispatcherFormatter
{
    private DispatcherAction[] listDispActions = null;

    public DispatcherFormatter(HSActionBean[] listAction)
        throws Exception
    {
        if (listAction != null)
        {
            Properties prop = DispatcherMgr.getInstance().getRegistryActions();
            listDispActions = new DispatcherAction[listAction.length];

            DispatcherAction dispAction = null;
            String implClass = "";
            int counter = 0;

            // Convert action row db into DispatcherAction objects
            for (int i = 0; i < listAction.length; i++)
            {
                implClass = prop.getProperty(listAction[i].getActiontype());
                dispAction = initializeObject(implClass, listAction[i]);

                if (dispAction != null)
                {
                    // Set site id
                    dispAction.setIdSite(listAction[i].getIdsite());

                    // Set site name
                    dispAction.setNameSite(listAction[i].getPvcode());

                    // Set action code
                    dispAction.setActionName(listAction[i].getCodeDesc());

                    listDispActions[i] = dispAction;
                }

                try
                {
                    Object[] p = { listAction[i].getCodeDesc() };
                    EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                        EventDictionary.TYPE_INFO, "D032", p);
                }
                catch (Exception e)
                {
                }
            }

            // Merge same action
            for (int i = 0; i < (listDispActions.length - 1); i++)
            {
                if (listDispActions[i] != null)
                {
                    for (int j = i + 1; j < listDispActions.length; j++)
                    {
                        if (listDispActions[j] != null)
                        {
                            if (listDispActions[i].compareAction(listDispActions[j]))
                            {
                                listDispActions[i].includeAction(listDispActions[j]);
                                listDispActions[j] = null;
                                counter++;
                            }
                        }
                    }
                }
            }

            DispatcherAction[] tmp = new DispatcherAction[listDispActions.length - counter];
            int idx = 0;

            for (int i = 0; i < listDispActions.length; i++)
            {
                if (listDispActions[i] != null)
                {
                    tmp[idx++] = listDispActions[i];
                }
            }

            // Replace Actions List
            listDispActions = tmp;
            tmp = null;
        }
    }

    public DispatcherAction[] getActionsToDisp()
    {
        return this.listDispActions;
    }

    public void initData()
    {
        if (this.listDispActions != null)
        {
            for (int i = 0; i < this.listDispActions.length; i++)
            {
                String tPath = "";

                try
                {
                    tPath = DispatcherMgr.getInstance().getTemplatePath();

                    if ((tPath != null) && !tPath.equalsIgnoreCase(""))
                    {
                        if (this.listDispActions[i] != null)
                        {
                            this.listDispActions[i].buildTemplate(tPath);
                        }
                    }
                    else
                    {
                        EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                            EventDictionary.TYPE_ERROR, "D010", null);
                    }
                }
                catch (Exception e)
                {
                    EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                        EventDictionary.TYPE_ERROR, "D011", new Object[] { tPath });

                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
            }
        }
    }

    private DispatcherAction initializeObject(String classe, HSActionBean p)
    {
        DispatcherAction objRet = null;

        Class<?>[] cls = 
            {
                Integer.class, Integer.class, Integer.class, Integer.class, String.class,
                Timestamp.class, Timestamp.class, String.class, String.class, Boolean.class,
                Integer.class, Timestamp.class, Timestamp.class
            };

        Object[] obj = 
            {
                new Integer(p.getIdhsaction()), new Integer(p.getIdaction()),
                new Integer(p.getPriority()), new Integer(p.getStatus()), p.getParameters(),
                p.getInserttime(), p.getLastupdate(), p.getTemplate(), p.getActiontype(),
                new Boolean(p.isIsalarm()), new Integer(p.getIdvariable()), p.getStart(), p.getEnd()
            };

        try
        {
            objRet = (DispatcherAction) FactoryObject.newInstance(classe, cls, obj);
        }
        catch (Exception e)
        {
            Object[] par = { p.getActiontype() };
            EventMgr.getInstance().log(new Integer(1), "Dispatcher", "Action",
                EventDictionary.TYPE_ERROR, "D009", par);

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        return objRet;
    }
}
