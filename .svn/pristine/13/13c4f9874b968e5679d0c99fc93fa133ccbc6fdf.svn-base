<%@ page language="java" pageEncoding="UTF-8"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.devices.*"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.presentation.devices.DeviceList"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	int idsite = sessionUser.getIdSite();
	
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	
	// Per generazione combo in dettaglio
	GroupListBean groups = sessionUser.getGroup();
	int[] gids = groups.getIds();
	DeviceStructureList deviceStructureList = groups.getDeviceStructureList(); 
	int[] ids = deviceStructureList.retrieveIdsByGroupsId(gids);
	sessionUser.getTransaction().setIdDevices(ids);
	sessionUser.getTransaction().setIdDevicesCombo(ids);
	
	// Tabella Dispositivi
	DeviceListBean deviceList =  new DeviceListBean(idsite,language,gids,true);	
%>
<table border="0" width="100%" cellspacing="1" cellpadding="1">

	<%if (deviceList.size()>0){%>
	<tr id="deviceviewloading">
	<td style="text-align: center">
		<table width="100%" height="100%" cellspacing="1" cellpadding="0" class="table">
			<tr>
			<td style="text-align: center;">
			Loading <img src="images/ajax-loader_white.gif">
			</td>
			</tr>
		</table>
	</td>
	</tr>
	<tr>
	<td>
		<table id="deviceviewcontainer" 
		border="0" cellpadding="2" cellspacing="0" width="99%" height="100%" align="left">
		</table>
	</td>
	</tr>
	<%}else{%>
	<tr>
	<td>
		<table align="center"><tr><td><b><%=multiLanguage.getString("grpview","nodevice")%></b></td></tr></table>
	</td>
	</tr>
	<%}%>
</table>