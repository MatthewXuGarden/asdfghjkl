<%@ page language="java" 
import="com.carel.supervisor.presentation.session.*"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.presentation.bean.search.*"
import="com.carel.supervisor.presentation.helper.*"
import="com.carel.supervisor.presentation.tabmenu.*"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.devices.DeviceList"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.field.*"
import="com.carel.supervisor.presentation.alarms.*"
%>
<%

	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	String sitename = sessionUser.getSiteName();
	
	int idsite = sessionUser.getIdSite();
	
	LangService lan = LangMgr.getInstance().getLangService(language);
	
	AlarmList alarmList = null;
	
	//salvataggio paginazione Biolo param:AlSearch
	String alarm_page = "";
	alarm_page = sessionUser.getPropertyAndRemove("AlSearch");
	int pag = 1;
	if (null!=alarm_page)
	{
		pag = Integer.parseInt(alarm_page);
		
	}
	
	alarmList = new SearchAlarm().find(sessionUser,pag);   

	alarmList.setPageNumber(pag);
	alarmList.setScreenH(sessionUser.getScreenHeight());
	alarmList.setScreenW(sessionUser.getScreenWidth());	
	alarmList.setWidth(900);
	alarmList.setHeight(365);
	alarmList.setTitle(lan.getString("searchal","alarmfound"));
	String htmlAlarmTable = "";
	
	String limit_search = sessionUser.getPropertyAndRemove("limit_search");
	
	if (alarmList.numAlarm()>0)
		htmlAlarmTable = alarmList.getHTMLAlarmFindTable("AlarmFound",language,sessionUser);
	else if (limit_search!=null && limit_search.equalsIgnoreCase("ko"))
		htmlAlarmTable = "<p class='tdTitleTable' align='center'>"+lan.getString("search","s_overlimit")+"</p>";
	else
		htmlAlarmTable = "<p class='tdTitleTable' align='center'>"+lan.getString("searchal","noalarm")+"</p>";
		
	FileDialogBean fileDlg = new FileDialogBean(request);
%>
<input type='hidden' id='limit_search' value='<%=limit_search%>' />
<input type='hidden' id='s_overlimit' value='<%=lan.getString("search","s_overlimit") %>' />
<INPUT type="hidden" id="sitename" value="<%=sitename%>" />
<input type='hidden' id='save_confirm' value='<%=lan.getString("fdexport","exportconfirm") %>' />
<input type='hidden' id='save_error' value="<%=lan.getString("fdexport","exporterror") %>" />
<INPUT type="hidden" id="LWCtDataName1_priority_col" value="<%=alarmList.getPriorityCol()%>" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate1")%>" value="1" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate2")%>" value="2" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate3")%>" value="3" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate4")%>" value="4" />

<table border="0" width="100%" height="100%" cellspacing="1" cellpadding="1">
	<tr height="100%" valign="top" id="trAlrList">
		<td><%=htmlAlarmTable%></td>
	</tr>
</table>
 <%=fileDlg.renderFileDialog()%>
 