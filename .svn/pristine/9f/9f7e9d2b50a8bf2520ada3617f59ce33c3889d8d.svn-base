<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.bo.master.BoMaster"
	import="com.carel.supervisor.base.factory.FactoryObject"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.devices.*"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.mobile.*"
	import="com.carel.supervisor.presentation.devices.DeviceList"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.director.graph.GraphConstant"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	if( !ServletHelper.validateSession(sessionUser) )
		response.sendRedirect("Logout.jsp");
	String jsession = request.getSession().getId();
	LangService lang = LangMgr.getInstance().getLangService(sessionUser.getLanguage());

	// get alarm table
	int idAlarmTab = 0;
	String strAlarmTab = request.getParameter("at");
	if( strAlarmTab != null )
		idAlarmTab = Integer.parseInt(strAlarmTab);
	
	// create transaction
	UserTransaction ut = new UserTransaction("BDtlView", "");
	BoMaster oMaster = (BoMaster)FactoryObject.newInstance("com.carel.supervisor.presentation.bo." + ut.getTrxName(),
		new Class[] { String.class }, new Object[] { sessionUser.getLanguage() });
	ut.setBoTrx(oMaster);
	ut.setProperty("curTab", "tab3name");
	ut.setProperty("resource", "/mobile/DeviceAlarms.jsp");
	sessionUser.addNewUserTransaction(ut);
	
	int idDev = Integer.parseInt(sessionUser.getProperty("iddev"));
	sessionUser.getTransaction().setIdDevices(new int[] { idDev });
	DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
	DeviceStructure deviceStructure = deviceStructureList.get(idDev);
	String strDeviceDescription = "";
	if( deviceStructure != null ) {
		strDeviceDescription = deviceStructure.getDescription();
		oMaster.loadFilter(idDev, deviceStructure.getIdDevMdl(), sessionUser.getLanguage());
	}
	
	AlarmList alarms = new AlarmList(lang.getString("alrglb","activeal"));
	alarms.setLink(false);
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
	alarms.setWidth(640);
	alarms.setHeight(340);
	String htmlTable1 = alarms.getHTMLAlarmTable("TA",sessionUser.getLanguage(),sessionUser);

	AlarmCalledOfList alarmsCalledOf = new AlarmCalledOfList(lang.getString("alrglb","calledofal"));
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
	
	alarmsCalledOf.setScreenW(sessionUser.getScreenWidth());
	alarmsCalledOf.setScreenH(sessionUser.getScreenHeight());
	alarmsCalledOf.setWidth(640);
	alarmsCalledOf.setHeight(340);
	String htmlTable2 = alarmsCalledOf.getHTMLAlarmCalledOfTable("TalarmCalledOf",sessionUser.getLanguage(),sessionUser);
%>
<html>

<head>
	<title>PVPRO</title>
	<base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
	<meta name="format-detection" content="telephone=no">
	<link rel="stylesheet" href="mobile/stylesheet/mobile.css" type="text/css">
	<script type="text/javascript" src="scripts/arch/jscolor/jscolor.js"></script>
  	<script type="text/javascript" src="scripts/arch/MenuTab.js"></script>
  	<script type="text/javascript" src="scripts/arch/MenuAction.js"></script>
	<script type="text/javascript" src="scripts/arch/Refresh.js"></script>
	<script type="text/javascript" src="scripts/arch/MaskInOut.js"></script>
	<script type="text/javascript" src="mobile/scripts/arch/table/ListView.js"></script>
	<script type="text/javascript" src="mobile/scripts/arch/table/ListViewFisa.js"></script>
	<script type="text/javascript" src="mobile/scripts/arch/table/ListViewDyn.js"></script>
	<script type="text/javascript" src="scripts/arch/PM.js"></script>
	<script type="text/javascript" src="scripts/arch/table/ListViewSort.js"></script>
	<script type="text/javascript" src="scripts/arch/comm/Communication.js"></script>
	<script type="text/javascript" src="scripts/app/applmask.js"></script>
	<script type="text/javascript" src="scripts/arch/jt_.js"/></script>
	<script type="text/javascript" src="mobile/scripts/arch/util.js"/></script>
	<script type="text/javascript" src="mobile/scripts/app/dtlview.js"></script>
	<script type="text/javascript" src="mobile/scripts/device.js"></script>
</head>

<body onLoad="MTstopServerComm(); adjustContainer(); forceReloadPoller();">
<table id="tableTop" class="tableTop">
	<tr>
		<td>
			<select id="selectPage" name="selectPage" class="selectTop" onChange="onSelectPage(this.value)">
				<option value="DeviceMain.jsp"><%=lang.getString("mobile", "main")%></option>
				<option value="DeviceReadOnly.jsp"><%=lang.getString("mobile", "all_RO")%></option>
				<option value="DeviceParameters.jsp"><%=lang.getString("mobile", "parameters")%></option>
				<option value="DeviceAlarms.jsp" selected><%=lang.getString("mobile", "alarms")%></option>
				<option value="DeviceGraph.jsp?type=<%=GraphConstant.TYPE_HACCP%>"><%=lang.getString("dtlview", "tab4name")%></option>
				<option value="DeviceGraph.jsp?type=<%=GraphConstant.TYPE_HISTORICAL%>"><%=lang.getString("dtlview", "tab5name")%></option>
			</select>
			&nbsp;
			<select id="selectAlarmTable" name="selectAlarmTable" class="selectTop" onChange="onSelectAlarmTable(this.value)">
				<option value="0"<%=idAlarmTab==0 ? " selected" : ""%>><%=lang.getString("mobile", "active_alarms")%></option>
				<option value="1"<%=idAlarmTab==1 ? " selected" : ""%>><%=lang.getString("mobile", "reset_alarms")%></option>
			</select>
		</td>
	</tr>
</table>
<table width="100%">
	<tr>
		<td width="*" class="standardTxt"><b><%=strDeviceDescription%></b></td>
	</tr>
</table>
<INPUT type="hidden" id="LWCtDataName1_priority_col" value="<%=alarms.getPriorityCol()%>" />
<INPUT type="hidden" id="LWCtDataName2_priority_col" value="<%=alarmsCalledOf.getPriorityCol()%>" />
<INPUT type="hidden" id="pr_<%=lang.getString("alrview", "alarmstate1")%>" value="1" />
<INPUT type="hidden" id="pr_<%=lang.getString("alrview", "alarmstate2")%>" value="2" />
<INPUT type="hidden" id="pr_<%=lang.getString("alrview", "alarmstate3")%>" value="3" />
<INPUT type="hidden" id="pr_<%=lang.getString("alrview", "alarmstate4")%>" value="4" />
<div id="divActiveAlarms" style="display:block;width:98%;margin-left:5px;margin-right:5px;">
<%=htmlTable1%>
</div>
<div id="divResetAlarms" style="display:none;width:98%;margin-left:5px;margin-right:5px;">
<%=htmlTable2%>
</div>

<jsp:include page="Messages.jsp" flush="true" />
</body>
</html>
