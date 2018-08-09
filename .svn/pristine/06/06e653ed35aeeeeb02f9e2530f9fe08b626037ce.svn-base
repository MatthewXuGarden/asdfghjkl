package com.carel.supervisor.presentation.bo;

import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import java.util.Properties;


public class BAlrEvnSearch extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    public BAlrEvnSearch(String l)
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
        p.put("tab1name", "enableAction(1);ASonLoad();");
        p.put("tab2name", "enableAction(1);ESsetStateCal();");
        p.put("tab3name", "enableAction(1);ASonLoad();");
        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        //20091126-simon.zhang
        //append the virtual keyboard
        p.put("tab1name", "alrsearch.js;calendar.js;keyboard.js;");
        p.put("tab2name", "evnsearch.js;calendar.js;keyboard.js;");
        p.put("tab3name", "alrevnsearch.js;calendar.js;keyboard.js;");
        return p;
    }
    
    public static String getComboAlarmCategory(int idsite,String language,String selectedGroup) throws DataBaseException
    {
    	String sql = "select cfvarmdlgrp.idvargroup, cftableext.description from " +
    			"cfvarmdlgrp,cftableext where cfvarmdlgrp.idsite=? and cfvarmdlgrp.isalarm='TRUE' and cftableext.idsite =" +
    			"cfvarmdlgrp.idsite and cftableext.tablename='cfvarmdlgrp' " +
    			"and cftableext.languagecode=? and cftableext.tableid = cfvarmdlgrp.idvargroup";
    
    	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idsite),language});
    
    	StringBuffer combo = new StringBuffer("<OPTION value='"+selectedGroup+",,'>------------------</OPTION>");
    	int idvargroup = 0;
    	String descr = "";
    	for (int i=0;i<rs.size();i++)
    	{
    		idvargroup = ((Integer) rs.get(i).get("idvargroup")).intValue();
    		descr = rs.get(i).get("description").toString();
    		combo.append("<OPTION value='"+selectedGroup+",,"+idvargroup+"'>"+descr+"</OPTION>");
    	}
    	
    	return combo.toString();
    }
}
