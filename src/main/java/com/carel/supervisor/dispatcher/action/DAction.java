package com.carel.supervisor.dispatcher.action;

import com.carel.supervisor.base.log.Logger;
import com.carel.supervisor.base.log.LoggerMgr;
import com.carel.supervisor.dataaccess.db.SeqMgr;
import com.carel.supervisor.dispatcher.bean.HSActionQBean;
import com.carel.supervisor.dispatcher.bean.HSActionQBeanList;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


public class DAction extends DispatcherAction
{
    public static final String SPLIT = ";";

    public DAction(Integer key, Integer id, Integer pri, Integer sta, String rec, Timestamp itime,
        Timestamp utime, String tmpl, String type, Boolean isalarm, Integer idvar,
        Timestamp startTime, Timestamp endTime)
    {
        super(key, id, pri, sta, rec, itime, utime, tmpl, type, isalarm, idvar, startTime, endTime);
    }

    protected String[] initializedRecepients(String recepient)
    {
        String[] recRet = new String[0];

        if (recepient != null)
        {
            recRet = recepient.split(SPLIT);
        }

        Arrays.sort(recRet);

        return recRet;
    }

    public int[] putActionInQueue() throws Exception
    {
        List keyact = this.getKeyAction();
        int[] ret = new int[0];

        String grpactid = "";

        for (int j = 0; j < keyact.size(); j++)
        {
            grpactid += (((Integer) keyact.get(j)).intValue() + ",");
        }

        grpactid = grpactid.substring(0, grpactid.length() - 1);

        HSActionQBeanList actionQList = new HSActionQBeanList();
        HSActionQBean actionQ = null;
        Integer key = null;
        String[] receiver = this.getRecepients();
        boolean allOk = true;
        
        if (receiver != null)
        {
            for (int i = 0; i < receiver.length; i++)
            {
                try
                {
                    key = SeqMgr.getInstance().next(null, "hsactionqueue", "idhsactionqueue");
                    actionQ = new HSActionQBean(key.intValue(), this.getNameSite(),
                            this.getIdSite(), this.getPriority(), this.getRetryNum(),
                            this.getRetryAfter(), this.getFisicDevice(), this.getTypeAction(), 1,
                            "", "", receiver[i], grpactid);

                    actionQList.addAction(actionQ);
                }
                catch (Exception e)
                {
                    allOk = false;

                    Logger logger = LoggerMgr.getLogger(this.getClass());
                    logger.error(e);
                }
            }

            if (allOk)
                allOk = actionQList.insertActions();

            if (allOk)
            {
                ret = new int[keyact.size()];

                for (int i = 0; i < ret.length; i++)
                {
                    ret[i] = ((Integer) keyact.get(i)).intValue();
                }
            }
        }

        return ret;
    }
}
