package com.carel.supervisor.presentation.refresh;

import com.carel.supervisor.presentation.alarms.AlarmList;
import com.carel.supervisor.presentation.bean.search.SearchAlarm;
import com.carel.supervisor.presentation.session.UserSession;


public class RefreshAlarmsSearch extends RefreshMaster
{
    private AlarmList alarmList = null;

    public RefreshAlarmsSearch()
    {
    }

    public void refresh(UserSession userSession) throws Exception
    {
    	if ((arData != null) && (arData[INDEX_REFRESH_TYPE] != null))
        {
	    	if (arData[INDEX_REFRESH_TYPE].equals(TYPE_REFRESH) &&
	                arData[INDEX_PAGE].equals("1"))
	        {
	            alarmList = new SearchAlarm().find(userSession, 1);
	
	            //	alarmList = new SearchAlarm().find(sessionUser);
	        } //if resfresh
	
	        if (arData[INDEX_REFRESH_TYPE].equals(TYPE_PAGE))
	        {
	            alarmList = new SearchAlarm().find(userSession,
	                    new Integer(arData[INDEX_PAGE]).intValue());
	            userSession.getCurrentUserTransaction().setProperty("AlSearch", arData[INDEX_PAGE]);
	            //alarmList.loadFromDataBase(userSession,new Integer(arData[INDEX_PAGE]).intValue());//,arData[INDEX_TYPE_PAGE_MODE]);
	        } //if page
        }
    }

    public String getHtmlData(UserSession userSession, String htmlObj)
    {
        if (alarmList != null)
        {
            return alarmList.getHTMLAlarmFindTableRefresh(htmlObj,
                userSession.getLanguage(),userSession);
        }

        return "";
    }
}
