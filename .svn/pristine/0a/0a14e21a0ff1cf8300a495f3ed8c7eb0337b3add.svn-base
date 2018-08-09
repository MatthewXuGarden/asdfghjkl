package com.carel.supervisor.presentation.refresh;

import com.carel.supervisor.presentation.alarms.AlarmCalledOfList;
import com.carel.supervisor.presentation.session.UserSession;


public class RefreshRecalAlarms extends RefreshMaster
{
    private AlarmCalledOfList alarmCalled = null;

    public RefreshRecalAlarms()
    {
    }

    public void refresh(UserSession userSession) throws Exception
    {
    	if ((arData != null) && (arData[INDEX_REFRESH_TYPE] != null))
        {
	    	if (arData[INDEX_REFRESH_TYPE].equals(TYPE_REFRESH) &&
	                arData[INDEX_PAGE].equals("1"))
	        {
	            this.alarmCalled = new AlarmCalledOfList();
                if( PAGE_ROWS < arData.length && arData[PAGE_ROWS] != null ) {
                	Integer page_rows = new Integer(arData[PAGE_ROWS]);
                	alarmCalled.setPageRows(page_rows);
                }
	            this.alarmCalled.loadCalledOfFromDataBase(userSession, 1); //,TYPE_LEFT);
	        } //if resfresh
	
	        if (arData[INDEX_REFRESH_TYPE].equals(TYPE_PAGE))
	        {
	            this.alarmCalled = new AlarmCalledOfList();
                if( PAGE_ROWS < arData.length && arData[PAGE_ROWS] != null ) {
                	Integer page_rows = new Integer(arData[PAGE_ROWS]);
                	alarmCalled.setPageRows(page_rows);
                }
	            this.alarmCalled.loadCalledOfFromDataBase(userSession,
	                new Integer(arData[INDEX_PAGE]).intValue()); //,arData[INDEX_TYPE_PAGE_MODE]);
	            userSession.getCurrentUserTransaction().setProperty("AlCalledOfPageNumber", arData[INDEX_PAGE]);
	        } //if page
        }
    }

    public String getHtmlData(UserSession userSession, String htmlObj)
    {
        if (alarmCalled != null)
        {
            return alarmCalled.getHTMLAlarmCalledOfTableRefresh(htmlObj,
                userSession.getLanguage(),userSession);
        }

        return "";
    }
}
