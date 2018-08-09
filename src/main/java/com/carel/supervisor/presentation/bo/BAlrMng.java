package com.carel.supervisor.presentation.bo;

import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;
import java.util.Properties;


public class BAlrMng extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    public BAlrMng(String l)
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

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();

        return p;
    }

    public void executePostAction(UserSession us, String tabName,
        Properties prop) throws Exception
    {
    }
}
