package com.carel.supervisor.presentation.bo;

import com.carel.supervisor.presentation.bean.rule.RelayBeanList;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class BRelayMgr extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    public BRelayMgr(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeRefreshTime()
    {
        Properties p = new Properties();

        return p;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "onload();resizeTableTab1();startRelayRefresh();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "relay.js;");

        return p;
    }

    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
        int idsite = us.getIdSite();
        String language = us.getLanguage();
        String idsactive = us.getProperty("idsactive");
        int tmp = -1;

        if (!idsactive.equals("noactive"))
        {
            String[] ids_to_reset = idsactive.split(";");
            List ids = new ArrayList();

            for (int i = 0; i < ids_to_reset.length; i++)
            {
                tmp = Integer.parseInt(ids_to_reset[i]);

                if (us.getPropertyAndRemove("chr_" + tmp) != null)
                {
                    ids.add(new Integer(tmp));
                }
            }

            RelayBeanList relais = new RelayBeanList(idsite, language);
            int[] i_ids = new int[ids.size()];

            for (int i = 0; i < ids.size(); i++)
            {
                i_ids[i] = ((Integer) ids.get(i)).intValue();
            }

            if (i_ids.length > 0)
            {
                relais.resetRelay(idsite, language, us.getUserName(), true,
                    i_ids);
            }
        }
    }
}
