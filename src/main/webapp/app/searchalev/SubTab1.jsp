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
import="com.carel.supervisor.presentation.alarmsevents.*"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	String sitename = sessionUser.getSiteName();
	String jsession = request.getSession().getId();
	
	int idsite = sessionUser.getIdSite();
	
	LangService lan = LangMgr.getInstance().getLangService(language);
	
	AlarmEventList alevList = null;
	
	String export = lan.getString("searchalev","export");
	String alarm = lan.getString("searchalev","alarm");
	String event = lan.getString("searchalev","event");
	String both = lan.getString("searchalev","both");
	String foundalev = lan.getString("searchalev","foundalev");
	String from = lan.getString("searchalev","from");
	String description = lan.getString("searchalev","description");
	String device= lan.getString("searchalev","device");
	String user = lan.getString("searchalev","user");
	String to = lan.getString("searchalev","to");
	String priority = lan.getString("devdetail","priority");
	
	//salvataggio paginazione Biolo param:AlSearch
	String alarm_page = "";
	alarm_page = sessionUser.getPropertyAndRemove("AlSearch");
	int pag = 1;
	if (null!=alarm_page)
	{
		pag = Integer.parseInt(alarm_page);
		
	}
	
	alevList = new SearchAlarmEvent().find(sessionUser,pag,true,true,true);   
	
	alevList.setPageNumber(pag);
	alevList.setScreenH(sessionUser.getScreenHeight());
	alevList.setScreenW(sessionUser.getScreenWidth());	
	alevList.setWidth(900);
	alevList.setHeight(340);
	
	alevList.setTitle(lan.getString("searchalev","foundalev"));
	String htmlAlarmTable = "";
	
	String limit_search = sessionUser.getPropertyAndRemove("limit_search");
	
	if (alevList.numAlarmEvent()>0)
		htmlAlarmTable = alevList.getHTMLAlarmEventFindTable("AlarmEventFound",sessionUser);
	else if (limit_search!=null && limit_search.equalsIgnoreCase("ko"))
		htmlAlarmTable = "<p class='tdTitleTable' align='center'>"+lan.getString("search","s_overlimit")+"</p>";
	else 
		htmlAlarmTable = "<p class='tdTitleTable' align='center'>"+lan.getString("searchal","noalarm")+"</p>";
		
	String disabled = (alevList==null||alevList.numAlarmEvent()==0)?"disabled":"";
	
	FileDialogBean fileDlg = new FileDialogBean(request);

%>

<input type='hidden' id='str_priority' value='<%=priority%>' />
<input type='hidden' id='limit_search' value='<%=limit_search%>' />
<input type='hidden' id='s_overlimit' value='<%=lan.getString("search","s_overlimit") %>' />
<input type='hidden' id='save_confirm' value='<%=lan.getString("fdexport","exportconfirm") %>' />
<input type='hidden' id='save_error' value="<%=lan.getString("fdexport","exporterror") %>" />

<iframe id="saveiframe" width="0" height="0" frameborder="0"></iframe>   
<input id="action" name="action" type="hidden" value=""/>
<input type="hidden" id="sitename" value="<%=sitename%>" />


<FIELDSET class='field' style='width:96%' ><LEGEND class="standardTxt" style="font-weight:bold"><%=export%></LEGEND>
	<TABLE class="standardTxt" border="0">
		<TR>
			<TD><input type="checkbox" id="alarm_chk" name="alarm_chk" onclick="checkExport('alarm_chk','event_chk');" <%=disabled%>><%=alarm%></TD>
			<TD><input type="checkbox" id="event_chk" name="event_chk" onclick="checkExport('alarm_chk','event_chk');" <%=disabled%>><%=event%></TD>
		</TR>
	</TABLE>
</FIELDSET>

<table border="0" width="97%" height="87%" cellspacing="1" cellpadding="1">
	<tr height="100%" valign="top" id="trAlrList">
		<td><%=htmlAlarmTable%></td>
	</tr>
</table>

<%=fileDlg.renderFileDialog()%>



