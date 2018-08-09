<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.bean.GroupListBean"
import="com.carel.supervisor.presentation.bean.DeviceStructureList"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.datalog.impl.AlarmLog"
import="com.carel.supervisor.presentation.alarms.AlarmCalledOfList"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	String sitename = sessionUser.getSiteName();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String sessionName= "alrglb";
	String alarmcomment2 = lan.getString(sessionName,"alarmcomment2");
	  
	GroupListBean groups = sessionUser.getGroup();
	int[] gids = groups.getIds();
	DeviceStructureList deviceStructureList = groups.getDeviceStructureList(); 
	int[] ids = deviceStructureList.retrieveIdsByGroupsId(gids);
	sessionUser.getTransaction().setIdDevices(ids);
               
	AlarmCalledOfList alarmsCalledOf = new AlarmCalledOfList(lan.getString(sessionName,"calledofal"));
	//caricare la pagina dov'ero prima se il parametro è settato in sessione Biolo
	String alarm_page = "";
	alarm_page = sessionUser.getPropertyAndRemove("AlCalledOfPageNumber");
	if (null==alarm_page)
		alarmsCalledOf.loadCalledOfFromDataBase(sessionUser,1);
	else
	{
		int t_page = Integer.parseInt(alarm_page);
		alarmsCalledOf.loadCalledOfFromDataBase(sessionUser,t_page);
		alarmsCalledOf.setPageNumber(t_page);
	}
	alarmsCalledOf.setScreenH(sessionUser.getScreenHeight());
	alarmsCalledOf.setScreenW(sessionUser.getScreenWidth());
	alarmsCalledOf.setHeight(340);
	alarmsCalledOf.setWidth(900);

	String htmlTable2=alarmsCalledOf.getHTMLAlarmCalledOfTable("TalarmCalledOf",language,sessionUser);
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<INPUT type="hidden" id="sitename" value="<%=sitename%>" />
<INPUT type="hidden" id="LWCtDataName2_priority_col" value="<%=alarmsCalledOf.getPriorityCol()%>" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate1")%>" value="1" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate2")%>" value="2" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate3")%>" value="3" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate4")%>" value="4" />

<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>

<table border="0" width="90%" height="90%" cellspacing="1" cellpadding="1">
	<tr height="1%" valign="top">
		<td class="StandardTxt"><%=alarmcomment2%></td>
	</tr>	
	<tr height="*" valign="top" id="trAlrList">
		<td><%=htmlTable2%></td>
	</tr>
</table>
