<%@ page language="java" import="java.util.*" 
	import="com.carel.supervisor.presentation.session.*"
	import="com.carel.supervisor.presentation.helper.*"
	import="com.carel.supervisor.dataaccess.language.*"
	import="com.carel.supervisor.dataaccess.datalog.impl.*"
	import="com.carel.supervisor.presentation.alarms.*"
	import="com.carel.supervisor.base.conversion.*"
	import="com.carel.supervisor.dataaccess.event.*"
	import="com.carel.supervisor.presentation.events.*"
	import="com.carel.supervisor.base.conversion.StringUtility"
	import="com.carel.supervisor.base.conversion.Replacer"
	import="java.sql.*" 
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"	
	import="com.carel.supervisor.base.util.*"
%>
<%@ page import="com.carel.supervisor.base.conversion.DateUtils" %>
<%@ page import="com.carel.supervisor.base.conversion.StringUtility" %>
<%

UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
String language = sessionUser.getLanguage();
int idsite = sessionUser.getIdSite();
String id = sessionUser.getProperty("id");

String[] datas = null;
if(id != null)
	datas = StringUtility.split(id,"_");

if(datas != null)
{
	id = datas[0];
	try {idsite = Integer.parseInt(datas[1]);}catch(Exception e){}
}

//Multilingua
LangService lan = LangMgr.getInstance().getLangService(language);

String s_data = lan.getString("evnview","data");
String s_user = lan.getString("evnview","user");
String s_category = lan.getString("evnview","category");
String s_description = lan.getString("evnview","description");
String s_id = lan.getString("evnview","id");
String s_evndtl = lan.getString("evnview","evndtl");

//Retrieve dati
EventMgr temp = EventMgr.getInstance();
Event event = temp.retriveEventById(Integer.parseInt(id),idsite,language);
String table = "";
//x corretta visualizzazione path dei backup:
String msg = Replacer.replace(event.getMessage(), "\\\\", "\\");
if (ParamsList.containsParams(event.getMessagecode(),sessionUser))
{
	ParamsList p = new ParamsList();
	p.setScreenH(sessionUser.getScreenHeight());
	p.setScreenW(sessionUser.getScreenWidth());
	p.setHeight(260);
	p.setWidth(900);
	String par = event.getParameters();
	String[]tmp = StringUtility.split(par,";");
	p.loadFromDataBase(sessionUser,1,new Integer(tmp[0]));
	table = p.getHtml(sessionUser);
} else if( DevicesList.containsDevices(event.getMessagecode()) ) {
	String strParams = event.getParameters();
	String[] astrParams = StringUtility.split(strParams, ";");
	DevicesList list = new DevicesList();
	list.loadFromDataBase(Integer.parseInt(astrParams[0]), sessionUser.getIdSite(), sessionUser.getLanguage());
	table = list.getHtmlDeviceTable(sessionUser.getScreenWidth(), sessionUser.getScreenHeight());
}
boolean showParameters = temp.needShowParameters(event.getMessagecode(),event.getParameters());
boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey(); 
%>
<p class="tdTitleTable"><%=msg%></p>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>
<FIELDSET class='field'>
<LEGEND class="StandardTxt"><%=s_evndtl%></LEGEND>
<TABLE border="0" width="100%" cellspacing="1" cellpadding="1">
	<TR height="2%"><TD></TD></TR>
	<TR class="StandardTxt">
		<TD><%=s_id%>:</TD>
		<TD width="20" >&nbsp;</TD>
		<TD><%=event.getIdevent()%></TD>
	    <TD width="230" >&nbsp;</TD>
		<TD><%=s_data%>:</TD>
		<TD width="20" >&nbsp;</TD>
		<TD><%=DateUtils.date2String(event.getLastupdate(),"yyyy/MM/dd HH:mm:ss")%></TD>
	</TR>
	<TR height="2%"><TD></TD></TR>
	<TR class="StandardTxt">
		<TD><%=s_user%>:</TD>
		<TD width="20" >&nbsp;</TD>
		<TD> <%=event.getUser()%></TD>
		<TD width="230" >&nbsp;</TD>
		<TD><%=s_category%>:</TD>
		<TD width="20" >&nbsp;</TD>
		<TD><%=event.getCategory()%></TD>
	</TR>
	<TR height="2%"><TD></TD></TR>
	<%if(showParameters){ %>
	<tr class="StandardTxt"><td><%=s_evndtl%></td>
	    <td colspan="6"><%=UtilityString.replaceBadChars4XML(event.getParameters()) %></td>
	</tr>
	<TR height="2%"><TD></TD></TR>
	<%} %>
</TABLE>
</FIELDSET>

<table border="0" width="100%" height="70%" cellspacing="1" cellpadding="1">
	<tr height="100%" valign="top" id="trEventList">
		<td><%=table%></td>
	</tr>
</table>
