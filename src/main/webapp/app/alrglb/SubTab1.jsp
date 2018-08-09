<%@ page language="java"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.bean.GroupListBean"
import="com.carel.supervisor.presentation.bean.DeviceStructureList"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.datalog.impl.AlarmLog"
import="com.carel.supervisor.dataaccess.datalog.impl.AlarmLogList"
import="com.carel.supervisor.presentation.alarms.AlarmList"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	String sitename = sessionUser.getSiteName();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String sessionName= "alrglb";
	String alarmcomment1 = lan.getString(sessionName,"alarmcomment1");
	
	GroupListBean groups = sessionUser.getGroup();
	int[] gids = groups.getIds();
	DeviceStructureList deviceStructureList = groups.getDeviceStructureList(); 
	int[] ids = deviceStructureList.retrieveIdsByGroupsId(gids);
	sessionUser.getTransaction().setIdDevices(ids);
               
	AlarmList alarms = new AlarmList(lan.getString(sessionName,"activeal"));
	
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
	alarms.setScreenH(sessionUser.getScreenHeight());
	alarms.setScreenW(sessionUser.getScreenWidth());
	alarms.setHeight(340);
	alarms.setWidth(900);
	
	String htmlTable1= alarms.getHTMLAlarmTable("TA",language, sessionUser);
	int num_ack = alarms.getNumAck();
	
	String priority1woNote = sessionUser.getPropertyAndRemove("priority1woNote");
	boolean pr1 = (priority1woNote!=null) && (priority1woNote.equals("TRUE"));
	String priority2woNote = sessionUser.getPropertyAndRemove("priority2woNote");
	boolean pr2 = (priority2woNote!=null) && (priority2woNote.equals("TRUE"));
	String priority3woNote = sessionUser.getPropertyAndRemove("priority3woNote");
	boolean pr3 = (priority3woNote!=null) && (priority3woNote.equals("TRUE"));
	String priority4woNote = sessionUser.getPropertyAndRemove("priority4woNote");
	boolean pr4 = (priority4woNote!=null) && (priority4woNote.equals("TRUE"));
	
	boolean noteRequired = pr1 || pr2 || pr3 || pr4;
	String messagePr1 = lan.getString("alrmng","noteReqPr1");
	String messagePr2 = lan.getString("alrmng","noteReqPr2");
	String messagePr3 = lan.getString("alrmng","noteReqPr3");
	String messagePr4 = lan.getString("alrmng","noteReqPr4");
	String message = "";
	if (pr1) message+=messagePr1+"\n";
	if (pr2) message+=messagePr2+"\n";
	if (pr3) message+=messagePr3+"\n";
	if (pr4) message+=messagePr4+"\n";
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>

<%@page import="com.carel.supervisor.presentation.alarms.AlarmMngTable"%>

<INPUT type="hidden" id="okmessage" name="okmessage" value="<%=lan.getString("alrglb", "okmessage")%>" /> 
<INPUT type="hidden" id="komessage" name="komessage" value="<%=lan.getString("alrglb", "komessage")%>" />
<INPUT type="hidden" id="confirmackall" name="confirmackall" value="<%=lan.getString("alrglb", "confirmackall")%>" />
<INPUT type="hidden" id="confirmresetall" name="confirmresetall" value="<%=lan.getString("alrglb", "confirmresetall")%>" />
<INPUT type="hidden" id="confirmdeleteall" name="confirmdeleteall" value="<%=lan.getString("alrglb", "confirmdeleteall")%>" /> 
<INPUT type="hidden" id="noalarmtodelete" name="noalarmtodelete" value="<%=lan.getString("alrglb", "noalarmtodelete")%>" /> 
<INPUT type="hidden" id="noalarmtoreset" name="noalarmtoreset" value="<%=lan.getString("alrglb", "noalarmtoreset")%>" /> 
<INPUT type="hidden" name="numalr" value="<%=alarms.getListaAllarmi().size()%>" />
<INPUT type="hidden" name="numalrmack" value="" />
<INPUT type="hidden" id="sitename" value="<%=sitename%>" />
<INPUT type="hidden" id="noteRequiredError" value="<%=message%>" />
<INPUT type="hidden" id="num_ack" value="<%=num_ack%>" />
<INPUT type="hidden" id="LWCtDataName1_priority_col" value="<%=alarms.getPriorityCol()%>" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate1")%>" value="1" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate2")%>" value="2" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate3")%>" value="3" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate4")%>" value="4" />

<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>

<table border="0" width="90%" height="90%" cellspacing="1" cellpadding="1">
	<tr height="1%" valign="top">
		<td class="StandardTxt"><%=alarmcomment1%></td>
	</tr>
	<tr height="*" valign="top" id="trAlrList">		
	  <td><%=htmlTable1%></td>
	</tr>
</table>

<% if (noteRequired) { %>	
	<SCRIPT type="text/javascript">
		setTimeout("alert(document.getElementById('noteRequiredError').value)",100);
	</SCRIPT>
<%} %>
