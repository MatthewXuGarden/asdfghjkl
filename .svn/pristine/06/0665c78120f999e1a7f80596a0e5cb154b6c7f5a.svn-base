package com.carel.supervisor.presentation.bo;

import java.util.Properties;

import com.carel.supervisor.base.util.UtilityString;
import com.carel.supervisor.dataaccess.db.DataBaseException;
import com.carel.supervisor.dataaccess.db.DatabaseMgr;
import com.carel.supervisor.dataaccess.db.RecordSet;
import com.carel.supervisor.dataaccess.event.EventMgr;
import com.carel.supervisor.plugin.fs.FSManager;
import com.carel.supervisor.presentation.bo.master.BoMaster;
import com.carel.supervisor.presentation.fs.FSRackBean;
import com.carel.supervisor.presentation.fs.FSUtilAux;
import com.carel.supervisor.presentation.fs.FSUtilBean;
import com.carel.supervisor.plugin.fs.FSRack;
import com.carel.supervisor.presentation.session.UserSession;
import java.util.Map;

public class BFSuction extends BoMaster
{
    private static final int REFRESH_TIME = -1;

    public BFSuction(String l)
    {
        super(l, REFRESH_TIME);
    }

    protected Properties initializeEventOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "init_gauges();");
        p.put("tab2name", "init_t2();");
        p.put("tab3name", "init_t3();");

        return p;
    }

    protected Properties initializeJsOnLoad()
    {
        Properties p = new Properties();
        p.put("tab1name", "fsstatus.js");
        p.put("tab2name", "dbllistbox.js;fsutils.js");
        p.put("tab3name", "fsracks.js");

        return p;
    }

    public void executePostAction(UserSession us, String tabName, Properties prop) throws Exception
    {
    	String username = us.getUserName();
    	if (tabName.equalsIgnoreCase("tab1name"))
    	{
    		String cmd = us.getProperty("cmd");
    		
    		if ("start_fs".equalsIgnoreCase(cmd))
    		{
    			FSManager.loadFSProperties();
    			FSManager.getInstance().startFS(username);
    			setAutostart(true);
    		}
    		else if ("stop_fs".equalsIgnoreCase(cmd))
    		{
    			FSManager.getInstance().stopFS(username);
    			setAutostart(false);
    		}
    	}
    	else if (tabName.equalsIgnoreCase("tab2name"))
        {
            FSUtilBean.saveUtilAssociation(us,prop);
            EventMgr.getInstance().info(1,username,"fs","FS07",null);
            FSManager.getInstance().configurationChanged(username);
        }
    	else if (tabName.equalsIgnoreCase("tab3name"))
    	{
        	FSRackBean.saveRacksSelected(prop);
        	EventMgr.getInstance().info(1,username,"fs","FS06",null);
    	}
    }

    public String executeDataAction(UserSession us, String tabName, Properties prop) throws Exception
    {
    	StringBuffer strResponse = new StringBuffer();
    	strResponse.append("<response>");
        String action = prop.getProperty("action", "");
        
        if( action.equalsIgnoreCase("select_rack") ) {
        	String id = prop.getProperty("id");
        	FSRack rack = FSRackBean.getActualRackFromDB(Integer.parseInt(id), us.getLanguage());
        	strResponse.append("<rack id=\"" + id + "\" new_alg=\"" + rack.isNewAlg() + "\">");
        	String sql = "select idutil from fsutil where idrack=" + id;
        	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null, sql);
        	if( rs.size() > 0 ) {
        		Map<Integer, String> map = FSUtilBean.getUtilsDescription(us.getLanguage());
        		for(int i = 0; i < rs.size(); i++) {
        			Integer idUtil = (Integer)rs.get(i).get(0);
        			strResponse.append("<util id=\"" + idUtil.toString() + "\" name=\"" + UtilityString.replaceBadChars4XML(map.get(idUtil)) + "\"/>");
        		}
        	}
        	strResponse.append("</rack>");
        	strResponse.append("<utilities used=\"" + FSUtilBean.getOtherUtils(Integer.parseInt(id)) + "\">");
        	FSUtilAux utils[] = FSUtilBean.retrieveFreeUtils(us.getLanguage());
        	for(int i = 0; i < utils.length; i++) {
        		strResponse.append("<util id=\"");
        		strResponse.append(utils[i].getUtil().getIddevice());
        		strResponse.append("\" name=\"");
        		String desc = utils[i].getUtil().getDescription();
        		desc = UtilityString.replaceBadChars4XML(desc);
        		strResponse.append(desc);
        		strResponse.append("\"/>");
        	}
        	strResponse.append("</utilities>");
        }
        
        strResponse.append("</response>");
    	return strResponse.toString();
    }
    
    private static void setAutostart(boolean auto) throws DataBaseException
    {
    	String sql = "update fsconfig set value=? where key=?";
    	Object[] param = new Object[2];
    	param[0] = auto?new Integer(1):new Integer(0);
    	param[1] = "start";
    	DatabaseMgr.getInstance().executeStatement(null,sql,param);
    }
}
