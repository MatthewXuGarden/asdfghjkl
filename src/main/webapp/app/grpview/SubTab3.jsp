<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.alarms.*"
import="com.carel.supervisor.presentation.devices.*"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.presentation.devices.DeviceList"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%@ page import="com.carel.supervisor.presentation.session.UserSession" %>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUserLoc = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	String sitename = sessionUser.getSiteName();
	String group = trxUserLoc.getProperty("group");
	int[] gids = new int[1];
	gids[0] = Integer.valueOf(group);

	GroupListBean groups = sessionUser.getGroup();
	DeviceStructureList deviceStructureList = groups.getDeviceStructureList(); 
	int[] ids = deviceStructureList.retrieveIdsByGroupsId(gids);
	sessionUser.getTransaction().setIdDevices(ids);

	LangService lan = LangMgr.getInstance().getLangService(language);
	 
	// Table Allarm
	AlarmList alarms = new AlarmList(lan.getString("alrglb","activeal"));
	//caricare la pagina dov'ero prima se il parametro ?settato in sessione Biolo
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
	alarms.setHeight(162);
	alarms.setWidth(900);
	alarms.setScreenH(sessionUser.getScreenHeight());
	alarms.setScreenW(sessionUser.getScreenWidth());
	String htmlTable1= alarms.getHTMLAlarmTable("TAlarm",language,sessionUser);

	// Table Allarm 
	AlarmCalledOfList alarmsCalledOf = new AlarmCalledOfList(lan.getString("alrglb","calledofal"));
	
	//caricare la pagina dov'ero prima se il parametro ?settato in sessione Biolo
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
	//alarmsCalledOf.loadCalledOfFromDataBase(sessionUser,1);
	alarmsCalledOf.setHeight(162);
	alarmsCalledOf.setWidth(900);
	alarmsCalledOf.setScreenH(sessionUser.getScreenHeight());
	alarmsCalledOf.setScreenW(sessionUser.getScreenWidth());
	String htmlTable2=alarmsCalledOf.getHTMLAlarmCalledOfTable("TAlarmCalledOf",language,sessionUser);
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

<table border="0" width="100%" height="100%" cellspacing="1" cellpadding="1">
	<tr height="50%" valign="top" id="trActAlrList">
		<td><%=htmlTable1%></td>
	</tr>
	<tr height="50%" valign="top" id="trRclAlrList">
		<td><%=htmlTable2%></td>
	</tr>
</table>