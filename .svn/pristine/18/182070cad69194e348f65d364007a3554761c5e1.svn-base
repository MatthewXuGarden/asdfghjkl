<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.alarms.*"
import="com.carel.supervisor.presentation.devices.*"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.presentation.devices.DeviceList"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.field.FieldConnectorMgr"
import="com.carel.supervisor.presentation.alarms.AlarmCalledOfList"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	String sitename = sessionUser.getSiteName();
	
	int idSite = sessionUser.getIdSite(); 
	int idDev = -1;
	if(idSite < 0)
		idSite = 1;
	
	try{
		idDev  = Integer.parseInt(sessionUser.getProperty("iddev"));
	}
	catch(NumberFormatException e){}
	
	sessionUser.getTransaction().setIdDevices(new int[]{idDev});

    LangService lan = LangMgr.getInstance().getLangService(language);
    ParamDetail paramDetail = new ParamDetail(sessionUser);
	String name = paramDetail.getNameTable(sessionUser,idDev);
	String devicecomment3 = lan.getString("dtlview","devicecomment3");
	
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
	
	alarms.setHeight(154);
	alarms.setWidth(900);
	String htmlTable1= alarms.getHTMLAlarmTable("TA",language,sessionUser);

	AlarmCalledOfList alarmsCalledOf = new AlarmCalledOfList(lan.getString("alrglb","calledofal"));
	//caricare la pagina dov'ero prima se il parametro è settato in sessione Biolo
	alarm_page = "";
	alarm_page = sessionUser.getPropertyAndRemove("AlCalledOfPageNumber");
	if (null==alarm_page)
		alarmsCalledOf.loadCalledOfFromDataBase(sessionUser,1);
	else
	{
		int t_page = Integer.parseInt(alarm_page);
		alarmsCalledOf.loadCalledOfFromDataBase(sessionUser,t_page);
		alarmsCalledOf.setPageNumber(t_page);
	}
	
	alarmsCalledOf.setScreenW(sessionUser.getScreenWidth());
	alarmsCalledOf.setScreenH(sessionUser.getScreenHeight());

	alarmsCalledOf.setHeight(154);
	alarmsCalledOf.setWidth(900);
	String htmlTable2=alarmsCalledOf.getHTMLAlarmCalledOfTable("TalarmCalledOf",language,sessionUser);
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<INPUT type="hidden" id="sitename" value="<%=sitename%>" />
<INPUT type="hidden" id="LWCtDataName1_priority_col" value="<%=alarms.getPriorityCol()%>" />
<INPUT type="hidden" id="LWCtDataName2_priority_col" value="<%=alarmsCalledOf.getPriorityCol()%>" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate1")%>" value="1" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate2")%>" value="2" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate3")%>" value="3" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate4")%>" value="4" />
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>

<table border="0" width="100%" height="85%" cellspacing="1" cellpadding="1">
	<tr>
		<td class="tdTitleTable"><%=name%></td>
	</tr>
	<tr>
		<td><%=htmlTable1%></td>
	</tr>
	<tr>
		<td><%=htmlTable2%></td>
	</tr>
</table>