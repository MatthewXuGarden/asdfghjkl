<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.devices.DeviceList"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.events.EventList"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	String sitename = sessionUser.getSiteName();
	LangService lan = LangMgr.getInstance().getLangService(language);
	
	EventList eventList = new EventList(lan.getString("evnview","event"));
	String evnPag = "";
	evnPag = sessionUser.getPropertyAndRemove("EvnPage");
	if (null==evnPag)
		eventList.loadFromDataBase(sessionUser,1);
	else
	{
		int t_page = Integer.parseInt(evnPag);
		eventList.loadFromDataBase(sessionUser,t_page);
		eventList.setPageNumber(t_page);
		
	}
	eventList.setScreenW(sessionUser.getScreenWidth());
	eventList.setScreenH(sessionUser.getScreenHeight());
	eventList.setHeight(340);
	eventList.setWidth(900);
	
	String table = eventList.getHTMLEventTable("EventTable",language,sessionUser);
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	
%>
<INPUT type="hidden" id="sitename" value="<%=sitename%>" />
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>


<table border="0" width="100%" height="90%" cellspacing="1" cellpadding="1">
	<tr height="100%" valign="top" id="trEnvList">
		<td><%=table%></td>
	</tr>
</table>