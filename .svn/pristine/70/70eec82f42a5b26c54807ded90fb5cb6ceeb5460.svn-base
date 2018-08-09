package com.carel.supervisor.presentation.refresh;

import com.carel.supervisor.presentation.alarmsevents.AlarmEventList;
import com.carel.supervisor.presentation.bean.search.SearchAlarmEvent;
import com.carel.supervisor.presentation.session.UserSession;


public class RefreshAlarmsEventsSearch extends RefreshMaster
{
    private AlarmEventList alevList = null;

    public RefreshAlarmsEventsSearch()
    {
    }

    public void refresh(UserSession userSession) throws Exception
    {
    	if ((arData != null) && (arData[INDEX_REFRESH_TYPE] != null))
        {
	    	if (arData[INDEX_REFRESH_TYPE].equals(TYPE_REFRESH) &&
	                arData[INDEX_PAGE].equals("1"))
	        {
	            alevList = new SearchAlarmEvent().find(userSession, 1, true,true,true);
	
	            //	alevList = new SearchAlarm().find(sessionUser);
	        } //if resfresh
	
	        if (arData[INDEX_REFRESH_TYPE].equals(TYPE_PAGE))
	        {
	            alevList = new SearchAlarmEvent().find(userSession,
	                    new Integer(arData[INDEX_PAGE]).intValue(), true, true, true);
	            userSession.getCurrentUserTransaction().setProperty("AlSearch", arData[INDEX_PAGE]);
	            //alevList.loadFromDataBase(userSession,new Integer(arData[INDEX_PAGE]).intValue());//,arData[INDEX_TYPE_PAGE_MODE]);
	        } //if page
        }
    }

    public String getHtmlData(UserSession userSession, String htmlObj)
    {
        if (alevList != null)
        {
            return alevList.getHTMLAlarmEventFindTableRefresh(htmlObj,
                userSession);
        }

        return "";
    }
}
