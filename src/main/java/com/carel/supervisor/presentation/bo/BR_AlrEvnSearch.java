package com.carel.supervisor.presentation.bo;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import java.util.Properties;


public class BR_AlrEvnSearch extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    public BR_AlrEvnSearch(String l)
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
        p.put("tab1name", "enableAction(1);R_AlrOnLoad();");
        p.put("tab2name", "enableAction(1);ESsetStateCal();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "r_alrsearch.js;calendar.js;");
        p.put("tab2name", "r_evnsearch.js;calendar.js;");

        return p;
    }

    public static String getComboAlarmCategory(int idsite, String language)
        throws DataBaseException
    {
        String sql = "select cfvarmdlgrp.idvargroup, cftableext.description from " +
            "cfvarmdlgrp,cftableext where cfvarmdlgrp.idsite=? and cfvarmdlgrp.isalarm='TRUE' and cftableext.idsite =" +
            "cfvarmdlgrp.idsite and cftableext.tablename='cfvarmdlgrp' " +
            "and cftableext.languagecode=? and cftableext.tableid = cfvarmdlgrp.idvargroup";

        RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql,
                new Object[] { new Integer(idsite), language });

        StringBuffer combo = new StringBuffer("<OPTION value=''>------------------</OPTION>");

        //int idvargroup = 0;
        String descr = "";

        for (int i = 0; i < rs.size(); i++)
        {
            //idvargroup = ((Integer) rs.get(i).get("idvargroup")).intValue();
            descr = rs.get(i).get("description").toString();
            combo.append("<OPTION value=''>" + descr + "</OPTION>");
        }

        return combo.toString();
    }
}
