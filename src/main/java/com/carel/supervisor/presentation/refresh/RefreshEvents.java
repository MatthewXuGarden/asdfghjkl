package com.carel.supervisor.presentation.refresh;

import com.carel.supervisor.presentation.events.EventList;
import com.carel.supervisor.presentation.session.UserSession;


public class RefreshEvents extends RefreshMaster
{
    private EventList eventList = null;

    public RefreshEvents()
    {
    }

    public void refresh(UserSession userSession) throws Exception
    {
    	if ((arData != null) && (arData[INDEX_REFRESH_TYPE] != null))
        {
	    	if (arData[INDEX_REFRESH_TYPE].equals(TYPE_REFRESH) &&
	                arData[INDEX_PAGE].equals("1"))
	        {
	            eventList = new EventList();
                if( PAGE_ROWS < arData.length && arData[PAGE_ROWS] != null ) {
                	Integer page_rows = new Integer(arData[PAGE_ROWS]);
                	eventList.setPageRows(page_rows);
                }
	            eventList.loadFromDataBase(userSession, 1); //,TYPE_LEFT);
	            
	            //eventList.loadFromDataBase(userSession, new Integer(arData[INDEX_PAGE]).intValue());
	        } //if resfresh
	
	        if (arData[INDEX_REFRESH_TYPE].equals(TYPE_PAGE))
	        {
	            eventList = new EventList();
                if( PAGE_ROWS < arData.length && arData[PAGE_ROWS] != null ) {
                	Integer page_rows = new Integer(arData[PAGE_ROWS]);
                	eventList.setPageRows(page_rows);
                }
	            eventList.loadFromDataBase(userSession,
	                new Integer(arData[INDEX_PAGE]).intValue()); //,arData[INDEX_TYPE_PAGE_MODE]);
	            userSession.getCurrentUserTransaction().setProperty("EvnPage", arData[INDEX_PAGE]);
	        } //if page
        }
    }

    public String getHtmlData(UserSession userSession, String htmlObj)
    {
        if (eventList != null)
        {
            return eventList.getHTMLEventTableRefresh(htmlObj,
                userSession);
        }

        return "";
    }
}
