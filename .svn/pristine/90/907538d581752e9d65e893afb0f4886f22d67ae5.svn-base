package com.carel.supervisor.presentation.refresh;

import com.carel.supervisor.presentation.alarms.AlarmList;
import com.carel.supervisor.presentation.bean.ReportBeanListPres;
import com.carel.supervisor.presentation.session.UserSession;


public class RefreshPrint extends RefreshMaster
{
	 private ReportBeanListPres reportBean = null;

	    public RefreshPrint()
	    {
	    }

	    public void refresh(UserSession userSession) throws Exception
	    {
	        if ((arData != null) && (arData[INDEX_REFRESH_TYPE] != null))
	        {
	            if (arData[INDEX_REFRESH_TYPE].equalsIgnoreCase(TYPE_REFRESH) &&
	                    (arData[INDEX_PAGE] != null) &&
	                    arData[INDEX_PAGE].equalsIgnoreCase("1"))
	            {
	            	reportBean = new ReportBeanListPres();
	            	reportBean.loadFromDataBase(userSession, 1); //,TYPE_LEFT);
	            }

	            if (arData[INDEX_REFRESH_TYPE].equalsIgnoreCase(TYPE_PAGE))
	            {
	            	reportBean = new ReportBeanListPres();
	            	reportBean.loadFromDataBase(userSession,
	                    new Integer(arData[INDEX_PAGE]).intValue()); //,arData[INDEX_TYPE_PAGE_MODE]);
	                userSession.getCurrentUserTransaction().setProperty("AlPageNumber", arData[INDEX_PAGE]);
	                
	            }
	        }
        }

    public String getHtmlData(UserSession userSession, String htmlObj)
    {
        if (reportBean != null)
        {
            return reportBean.getHTMLHSPrintTableRefresh(userSession.getIdSite(), userSession.getLanguage(), "");
        }

        return "";
    }
}
