package com.carel.supervisor.presentation.refresh;

import com.carel.supervisor.presentation.session.UserSession;


public interface IRefresh
{
    public static final int R_DEVICE = 0;
    public static final int R_ALARMS = 1;
    public static final int R_ALARMSCALL = 2;
    public static final int R_EVENTS = 3;
    public static final int R_ALARMSSEARCH = 4;
    public static final int R_EVENTSSEARCH = 5;
    public static final int R_DEVICEDETAIL = 6;
    //public static final int R_FLASHLED = 7;
    public static final int R_PARAMS = 8;
    public static final int R_ALARMSEVENTS = 9;
    public static final int R_PARAMETERS = 10;
    public static final int R_PRINT = 11;
    public static final int R_REPORT = 12;
    
    
    public static final String R_DEVICESOBJ = "com.carel.supervisor.presentation.refresh.RefreshDevices";
    public static final String R_ALARMSOBJ = "com.carel.supervisor.presentation.refresh.RefreshAlarms";
    public static final String R_RECALARMSOBJ = "com.carel.supervisor.presentation.refresh.RefreshRecalAlarms";
    public static final String R_EVENTOBJ = "com.carel.supervisor.presentation.refresh.RefreshEvents";
    public static final String R_ALARMSSEARCHOBJ = "com.carel.supervisor.presentation.refresh.RefreshAlarmsSearch";
    public static final String R_EVENTSSEARCHOBJ = "com.carel.supervisor.presentation.refresh.RefreshEventsSearch";
    public static final String R_DEVICESDETAILOBJ  = "com.carel.supervisor.presentation.refresh.RefreshDevicesDetail";
    //public static final String R_FLASHLEDOBJ  = "com.carel.supervisor.presentation.refresh.RefreshFlashLed";
    public static final String R_PARAMSOBJ  = "com.carel.supervisor.presentation.refresh.RefreshParams";
    public static final String R_ALARMSEVENTSOBJ = "com.carel.supervisor.presentation.refresh.RefreshAlarmsEventsSearch";
    public static final String R_PARAMETERSOBJ = "com.carel.supervisor.plugin.parameters.refresh.RefreshParameters";
    public static final String R_PRINTOBJ = "com.carel.supervisor.presentation.refresh.RefreshPrint";
    public static final String R_REPORTOBJ = "com.carel.supervisor.presentation.refresh.RefreshReport";
    
    public void refresh(UserSession userSession) throws Exception;

    public String getHtmlData(UserSession userSession, String idHtmlObj)
        throws Exception;

    public void setTableData(String tData) throws Exception;
}
