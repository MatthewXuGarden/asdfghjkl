<%@ page language="java" pageEncoding="UTF-8"
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
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUserLoc = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	String sitename = sessionUser.getSiteName();
	
	
	int[] ids = sessionUser.getTransaction().getIdDevices();
	boolean displyTable = ((ids.length>0)?true:false);
	
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	String sessionName="grpview";
	
	// Tabella Dispositivi
	sessionUser.removeProperty("maxgrpvars2disp");
	DeviceList deviceList = new DeviceList(sessionUser,multiLanguage.getString(sessionName,"device"));
	deviceList.setScreenW(sessionUser.getScreenWidth());
	deviceList.setScreenH(sessionUser.getScreenHeight());
	deviceList.setHeight(100);
	deviceList.setWidth(900);
	String htmlTable1  = "";
	if (deviceList.sizeDevices()>0)
		htmlTable1 = deviceList.getHTMLDeviceTable("TDevice",language,sessionUser);
	
	// Tabella Allarmi
	AlarmList alarms = new AlarmList(multiLanguage.getString(sessionName,"alarms"));
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
	alarms.setHeight(80);
	alarms.setWidth(900);
	String htmlTable2= alarms.getHTMLAlarmTable("TAlarm",language,sessionUser);
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey(); 
%>
<INPUT type="hidden" id="sitename" value="<%=sitename%>" />
<INPUT type="hidden" id="LWCtDataName1_priority_col" value="<%=alarms.getPriorityCol()%>" />
<INPUT type="hidden" id="pr_<%=multiLanguage.getString("alrview", "alarmstate1")%>" value="1" />
<INPUT type="hidden" id="pr_<%=multiLanguage.getString("alrview", "alarmstate2")%>" value="2" />
<INPUT type="hidden" id="pr_<%=multiLanguage.getString("alrview", "alarmstate3")%>" value="3" />
<INPUT type="hidden" id="pr_<%=multiLanguage.getString("alrview", "alarmstate4")%>" value="4" />
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>
<table border="0" width="100%" height="90%" cellspacing="1" cellpadding="1">
	<%if(displyTable){%>
	<tr valign="top" id="trDevList">
		<td><%=htmlTable1%></td>
	</tr>
	<tr valign="top" id="trAlrList">
		<td><%=htmlTable2%></td>
	</tr>
	<%}else{%>
	<tr height="100%" valign="top">
		<td class="tdTitleTable" align="center"><%=multiLanguage.getString(sessionName,"nodevice")%></td>
	</tr>
	<%}%>
</table>