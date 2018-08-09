package com.carel.supervisor.dispatcher.action;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dispatcher.bean.HSActionQBean;
import com.carel.supervisor.dispatcher.bean.HSActionQBeanList;
import java.sql.Timestamp;


public class RAction extends DispatcherAction
{
    public RAction(Integer key, Integer id, Integer pri, Integer sta, String rec, Timestamp itime,
        Timestamp utime, String tmpl, String type, Boolean isalarm, Integer idvar,
        Timestamp startTime, Timestamp endTime)
    {
        super(key, id, pri, sta, rec, itime, utime, tmpl, type, isalarm, idvar, startTime, endTime);
    }

    protected String[] initializedRecepients(String recepient)
    {
        return new String[0];
    }

    public int[] putActionInQueue() throws Exception
    {
        Integer keyact = ((Integer) this.getKeyAction().get(0));
        String actkey = String.valueOf(keyact.intValue());

        HSActionQBeanList actionQList = new HSActionQBeanList();
        HSActionQBean actionQ = null;
        Integer key = null;
        String[] receiver = this.getRecepients();
        boolean allOk = true;

        try
        {
            key = SeqMgr.getInstance().next(null, "hsactionqueue", "idhsactionqueue");
            actionQ = new HSActionQBean(key.intValue(), this.getNameSite(), this.getIdSite(),
                    this.getPriority(), this.getRetryNum(), this.getRetryAfter(),
                    this.getFisicDevice(), this.getTypeAction(), 1, "", "", receiver[0], actkey);

            actionQList.addAction(actionQ);
        }
        catch (Exception e)
        {
            allOk = false;

            Logger logger = LoggerMgr.getLogger(this.getClass());
            logger.error(e);
        }

        if (allOk)
        {
            allOk = actionQList.insertActions();
        }

        if (allOk)
        {
            return new int[] { keyact.intValue() };
        }
        else
        {
            return new int[0];
        }
    }
}
