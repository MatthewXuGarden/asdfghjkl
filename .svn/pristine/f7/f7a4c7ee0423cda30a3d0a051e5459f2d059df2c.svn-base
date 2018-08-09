package com.carel.supervisor.presentation.refresh;

import com.carel.supervisor.presentation.bean.search.SearchEvent;
import com.carel.supervisor.presentation.session.UserSession;


public class RefreshEventsSearch extends RefreshMaster
{
    private SearchEvent search = null;

    public RefreshEventsSearch()
    {
    }

    public void refresh(UserSession userSession) throws Exception
    {
    	if ((arData != null) && (arData[INDEX_REFRESH_TYPE] != null))
        {
	    	if (arData[INDEX_REFRESH_TYPE].equals(TYPE_REFRESH) &&
	                arData[INDEX_PAGE].equals("1"))
	        {
	            search = new SearchEvent();
	            search.find(userSession, 1);
	        } //if resfresh
	
	        if (arData[INDEX_REFRESH_TYPE].equals(TYPE_PAGE))
	        {
	            search = new SearchEvent();
	            search.find(userSession, new Integer(arData[INDEX_PAGE]).intValue());
	            userSession.getCurrentUserTransaction().setProperty("EvnPageSearch", arData[INDEX_PAGE]);
	        } //if page
        }
    }

    public String getHtmlData(UserSession userSession, String htmlObj)
    {
        if (search != null)
        {
            return search.getHTMLEventTableRefresh(htmlObj,
                userSession);
        }

        return "";
    }
}
