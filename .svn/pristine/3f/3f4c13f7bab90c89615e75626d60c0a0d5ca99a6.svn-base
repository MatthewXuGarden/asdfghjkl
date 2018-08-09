package com.carel.supervisor.presentation.refresh;

import com.carel.supervisor.presentation.alarms.AlarmList;
import com.carel.supervisor.presentation.session.UserSession;


public class RefreshAlarms extends RefreshMaster
{
    private AlarmList alarmList = null;

    public RefreshAlarms()
    {
    }

    public void refresh(UserSession userSession) throws Exception
    {
        if ((arData != null) && arData.length>INDEX_REFRESH_TYPE &&(arData[INDEX_REFRESH_TYPE] != null))
        {
            if (arData[INDEX_REFRESH_TYPE].equalsIgnoreCase(TYPE_REFRESH) &&
                    (arData[INDEX_PAGE] != null) &&
                    arData[INDEX_PAGE].equalsIgnoreCase("1"))
            {
                alarmList = new AlarmList();
                if(arData[ISLINK].equalsIgnoreCase("false")){
                	alarmList.setLink(false);
                }
                if( PAGE_ROWS < arData.length && arData[PAGE_ROWS] != null ) {
                	Integer page_rows = new Integer(arData[PAGE_ROWS]);
                	alarmList.setPageRows(page_rows);
                }
                alarmList.loadFromDataBase(userSession, 1); //,TYPE_LEFT);
            }

            if (arData[INDEX_REFRESH_TYPE].equalsIgnoreCase(TYPE_PAGE))
            {
                alarmList = new AlarmList();
                if(arData[ISLINK].equalsIgnoreCase("false")){
                	alarmList.setLink(false);
                }
                if( PAGE_ROWS < arData.length && arData[PAGE_ROWS] != null ) {
                	Integer page_rows = new Integer(arData[PAGE_ROWS]);
                	alarmList.setPageRows(page_rows);
                }
                alarmList.loadFromDataBase(userSession,
                    new Integer(arData[INDEX_PAGE]).intValue()); //,arData[INDEX_TYPE_PAGE_MODE]);
                userSession.getCurrentUserTransaction().setProperty("AlPageNumber", arData[INDEX_PAGE]);
                
            }
        }
    }

    public String getHtmlData(UserSession userSession, String htmlObj)
    {
        if (alarmList != null)
        {
            return alarmList.getHTMLAlarmTableRefresh(htmlObj,
                userSession.getLanguage(),userSession);
        }

        return "";
    }
}
