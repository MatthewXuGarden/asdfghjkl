package com.carel.supervisor.presentation.bo;

import com.carel.supervisor.presentation.bo.master.BoMaster;
import java.util.Properties;


public class BDevLogList extends BoMaster
{
    private static final int REFRESH_TIME = 10000;

    public BDevLogList(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "resizeTableDevLogList();");
        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "resizeTable.js");
        return p;
    }
}
