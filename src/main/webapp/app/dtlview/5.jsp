<%@ page language="java"
	import="com.carel.supervisor.presentation.alarms.AlarmList"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.dataaccess.language.LangService"
    import="com.carel.supervisor.dataaccess.language.LangMgr"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);	
	String sitename = sessionUser.getSiteName();
	LangService lan = LangMgr.getInstance().getLangService(sessionUser.getLanguage());
	AlarmList alarms = new AlarmList(lan.getString("alrglb","activeal"));
	//caricare la pagina dov'ero prima se il parametro è settato in sessione Biolo
	String alarm_page = "";
	alarm_page = sessionUser.getPropertyAndRemove("AlPageNumber");
	if (null==alarm_page)
		alarms.loadFromDataBase(sessionUser,1);
	else
	{
		int t_page = Integer.parseInt(alarm_page);
		alarms.loadFromDataBase(sessionUser,t_page);
		alarms.setPageNumber(t_page);
	}
	alarms.setScreenW(sessionUser.getScreenWidth());
	alarms.setScreenH(sessionUser.getScreenHeight());
	alarms.setHeight(130);
	alarms.setWidth(900);
	alarms.setLink(false);
    String htmlTableAlarms = alarms.getHTMLAlarmTable("TAlarmDd",sessionUser.getLanguage(),sessionUser);
	
%>
<INPUT type="hidden" id="LWCtDataName1_priority_col" value="<%=alarms.getPriorityCol()%>" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate1")%>" value="1" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate2")%>" value="2" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate3")%>" value="3" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate4")%>" value="4" />
<%=htmlTableAlarms%>