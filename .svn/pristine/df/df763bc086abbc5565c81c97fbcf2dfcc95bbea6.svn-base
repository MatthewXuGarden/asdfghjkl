<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.devices.DeviceList"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.dataaccess.event.*"
import="com.carel.supervisor.presentation.bean.search.SearchEvent"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	String sitename = sessionUser.getSiteName();
	LangService lan = LangMgr.getInstance().getLangService(language);

	SearchEvent search = new SearchEvent(lan.getString("searchev","eventfound"));
	search.setScreenH(sessionUser.getScreenHeight());
	search.setScreenW(sessionUser.getScreenWidth());
	
	//salvataggio paginazione Biolo param:AlSearch
	String event_page = "";
	event_page = sessionUser.getPropertyAndRemove("EvnPageSearch");
	int pag = 1;
	if (null!=event_page)
	{
		pag = Integer.parseInt(event_page);
		
	}
	
	search.find(sessionUser,pag);
	
	search.setPageNumber(pag);
	search.setWidth(900);
	search.setHeight(355);
	String table = "";

	String limit_search = sessionUser.getPropertyAndRemove("limit_search");

	if (search.getNumOfEvents()>0)
		table = search.getHTMLEventTable("EventTable",language,sessionUser);
	else if (limit_search!=null && limit_search.equalsIgnoreCase("ko"))
		table = "<p class='tdTitleTable' align='center'>"+lan.getString("search","s_overlimit")+"</p>";
	else
		table = "<p class='tdTitleTable' align='center'>"+lan.getString("searchal","noevent")+"</p>";
		
		FileDialogBean fileDlg = new FileDialogBean(request);
%>
<input type='hidden' id='limit_search' value='<%=limit_search%>' />
<input type='hidden' id='s_overlimit' value='<%=lan.getString("search","s_overlimit") %>' />
 <input type='hidden' id='save_confirm' value='<%=lan.getString("fdexport","exportconfirm") %>' />
<input type='hidden' id='save_error' value="<%=lan.getString("fdexport","exporterror") %>" />
 <INPUT type="hidden" id="sitename" value="<%=sitename%>" />
 
<table border="0" height="100%" width="100%" cellspacing="1" cellpadding="1">
	<tr height="100%" valign="top" id="trEvnList">
		<td><%=table%></td>
	</tr>
</table>
 <%=fileDlg.renderFileDialog()%>