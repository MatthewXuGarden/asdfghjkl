package com.carel.supervisor.presentation.refresh;

import com.carel.supervisor.presentation.events.ParamsList;
import com.carel.supervisor.presentation.session.UserSession;


public class RefreshParams extends RefreshMaster
{
    private ParamsList paramsList = null;

    public RefreshParams()
    {
    }

    public void refresh(UserSession userSession) throws Exception
    {
    	if ((arData != null) && (arData[INDEX_REFRESH_TYPE] != null))
        {
	    	/*if (arData[INDEX_REFRESH_TYPE].equals(TYPE_REFRESH) &&
	                arData[INDEX_PAGE].equals("1"))
	        {
	            paramsList = new ParamsList();
	            paramsList.loadFromDataBase(userSession, 1); //,TYPE_LEFT);
	        } //if resfresh
	*/
	        if (arData[INDEX_REFRESH_TYPE].equals(TYPE_PAGE))
	        {
	            paramsList = new ParamsList();
	            paramsList.loadFromDataBase(userSession,
	                new Integer(arData[INDEX_PAGE]).intValue()); //,arData[INDEX_TYPE_PAGE_MODE]);
	        } //if page
        }
    }

    public String getHtmlData(UserSession userSession, String htmlObj)
    {
        if (paramsList != null)
        {
            return paramsList.getHtmlRefresh(userSession);
        }

        return "";
    }
}
