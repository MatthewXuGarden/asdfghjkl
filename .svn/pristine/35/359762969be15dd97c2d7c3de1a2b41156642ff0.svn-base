package com.carel.supervisor.presentation.bo;

import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.helper.VirtualKeyboard;
import com.carel.supervisor.presentation.refresh.IRefresh;
import com.carel.supervisor.presentation.refresh.RefreshBean;
import com.carel.supervisor.presentation.refresh.RefreshBeanList;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class BEvnDtl extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    public BEvnDtl(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeRefreshTime()
    {
        Properties p = new Properties();

        return p;
    }
    
    protected Map initializeRefresh()
    {
        Map map = new HashMap();

        RefreshBean[] rb = 
            {
                new RefreshBean("PARAM", IRefresh.R_PARAMS, 0)
            };
        RefreshBeanList rbl = new RefreshBeanList(REFRESH_TIME, rb.length);
        rbl.setRefreshObj(rb);

        map.put("tab1name", rbl);

        return map;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "resizeTableTabEvent();");
        p.put("tab2name", "setdefault();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
    	String virtkey = "";
        //determino se � abilitata la VirtualKeyboard:
        if (VirtualKeyboard.getInstance().isOnScreenKey())
        {
        	virtkey = ";keyboard.js;";
        }
    	
    	Properties p = new Properties();
        p.put("tab1name", "evnview.js"+virtkey);
        p.put("tab2name", "note.js"+virtkey);

        return p;
    }
}
