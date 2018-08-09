package com.carel.supervisor.presentation.bo;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.session.UserSession;

public class BLcNt_GrpDtl extends BoMaster
{
    private static final int REFRESH_TIME = 10000;

    public BLcNt_GrpDtl(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Map initializeRefresh()
    {
        Map map = new HashMap();
        return map;
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "enableAction(1);load_tab1();");
        
        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "lcnt_grpdtl.js");
        
        return p;
    }

    protected Properties initializeCommitKey()
    {
        Properties p = new Properties();
        return p;
    }

    public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
    {
        // ...
    }
    
    public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
    {
        StringBuffer xmlresponse = new StringBuffer("\n<response>");
        xmlresponse.append("</response>");
        return xmlresponse.toString();
    }
}