package com.carel.supervisor.dispatcher.action;

import com.carel.supervisor.dataaccess.datalog.impl.AlarmLogList;
import com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList;
import com.carel.supervisor.dataaccess.language.LangMgr;
import com.carel.supervisor.dataaccess.language.LangService;
import com.carel.supervisor.dispatcher.bean.HSActionBeanList;
import com.carel.supervisor.dispatcher.engine.sound.DispWinAlarm;
import com.carel.supervisor.dispatcher.memory.DispMemMgr;
import com.carel.supervisor.dispatcher.memory.WMemory;
import java.sql.Timestamp;
import java.util.List;


public class WAction extends DispatcherAction
{
    public WAction(Integer key, Integer id, Integer pri, Integer sta, String rec, Timestamp itime,
        Timestamp utime, String tmpl, String type, Boolean isalarm, Integer idvar, Timestamp start,
        Timestamp end)
    {
        super(key, id, pri, sta, rec, itime, utime, tmpl, type, isalarm, idvar, start, end);
    }

    protected String[] initializedRecepients(String recepient)
    {
        return new String[0];
    }

    public int[] putActionInQueue() throws Exception
    {
        int num = 0;
        int idAct = ((Integer) this.getKeyAction().get(0)).intValue();

        // Only on start alarm. Not recall alarm START
        boolean doit = true;
        List l = getEndTime();

        if (l != null)
        {
            for (int i = 0; i < l.size(); i++)
            {
                if (l.get(i) != null)
                {
                    doit = false;

                    break;
                }
            }
        }

        // END
        if (doit)
        {
            String lang = "EN_en";
            String l1 = "User";
            String l2 = "Password";
            String l3 = "Active alarms";
            String l4 = "Stop";

            try
            {
                lang = LangUsedBeanList.getDefaultLanguage(1);

                LangService multiLanguage = LangMgr.getInstance().getLangService(lang);
                l1 = multiLanguage.getString("winalarm", "lb1");
                l2 = multiLanguage.getString("winalarm", "lb2");
                l3 = multiLanguage.getString("winalarm", "lb3");
                l4 = multiLanguage.getString("winalarm", "lb4");
            }
            catch (Exception e)
            {
            }

            try
            {
                num = new AlarmLogList().getTotActiveAlarms(null, this.getNameSite(),
                        this.getIdSite());
            }
            catch (Exception e)
            {
            }

            WMemory zMem = (WMemory) DispMemMgr.getInstance().readConfiguration("W");
            String ps = "";

            if (zMem != null)
            {
                ps = zMem.getPathSound();
            }

            DispWinAlarm.getInstance().startWinAlarm(num, ps, l1, l2, l3, l4);
        }

        HSActionBeanList actionbean = new HSActionBeanList();
        actionbean.updateToSendActionList(new int[] { idAct });

        return new int[0];
    }
}
