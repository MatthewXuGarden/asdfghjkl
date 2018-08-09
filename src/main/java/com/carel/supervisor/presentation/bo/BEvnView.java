package com.carel.supervisor.presentation.bo;

import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.refresh.IRefresh;
import com.carel.supervisor.presentation.refresh.RefreshBean;
import com.carel.supervisor.presentation.refresh.RefreshBeanList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class BEvnView extends BoMaster
{
    private static final int REFRESH_TIME = 30000;

    public BEvnView(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Map initializeRefresh()
    {
        Map map = new HashMap();

        RefreshBean[] rb = 
            {
                new RefreshBean("EventTable", IRefresh.R_EVENTS, 0)
            };
        RefreshBeanList rbl = new RefreshBeanList(REFRESH_TIME, rb.length);
        rbl.setRefreshObj(rb);

        map.put("tab1name", rbl);

        return map;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name","enableAction(1);resizeTableTab1();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "evnview.js;keyboard.js;");
        
        return p;
    }
}
