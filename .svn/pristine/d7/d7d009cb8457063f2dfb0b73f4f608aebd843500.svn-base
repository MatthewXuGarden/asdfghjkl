<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.bo.master.BoMaster"
	import="com.carel.supervisor.base.factory.FactoryObject"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.devices.*"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBean"
	import="com.carel.supervisor.dataaccess.varaggregator.VarAggregator"
	import="com.carel.supervisor.device.DeviceStatusMgr"
	import="com.carel.supervisor.device.DeviceStatus"
	import="com.carel.supervisor.director.graph.GraphConstant"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="devdtl" class="com.carel.supervisor.presentation.devices.DeviceDetail" scope="request"/>
<jsp:useBean id="CurrUnit" class="com.carel.supervisor.presentation.sdk.obj.CurrUnit" scope="session"/>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	if( !ServletHelper.validateSession(sessionUser) )
		response.sendRedirect("Logout.jsp");
	String jsession = request.getSession().getId();
	LangService lang = LangMgr.getInstance().getLangService(sessionUser.getLanguage());

	// create transaction
	UserTransaction ut = new UserTransaction("BDtlView", "");
	BoMaster oMaster = (BoMaster)FactoryObject.newInstance("com.carel.supervisor.presentation.bo." + ut.getTrxName(),
		new Class[] { String.class }, new Object[] { sessionUser.getLanguage() });
	ut.setBoTrx(oMaster);
	ut.setProperty("curTab", "tab1name");
	ut.setProperty("resource", "/mobile/DeviceMain.jsp");
	sessionUser.addNewUserTransaction(ut);
	
	// put iddev on session properties
	String strIdDevice = request.getParameter("iddev");
	if( strIdDevice != null )
		sessionUser.setProperty("iddev", strIdDevice);
	int idDev = Integer.parseInt(sessionUser.getProperty("iddev"));
	sessionUser.getTransaction().setIdDevices(new int[] { idDev });
	
	DeviceStatus status = DeviceStatusMgr.getInstance().getDeviceStatus(new Integer(idDev));
	boolean offline = status == null ? true : !status.getStatus();
	
	DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
	DeviceStructure deviceStructure = deviceStructureList.get(idDev);
	if( deviceStructure != null ) {
		devdtl.setIdDevice(idDev);
		devdtl.setIdDevMdl(deviceStructure.getIdDevMdl());
		devdtl.setTitle(deviceStructure.getDescription());
		devdtl.setImage(deviceStructure.getImageDevice());
		devdtl.loadDeviceVariable(sessionUser.getLanguage(),sessionUser.getIdSite());
		oMaster.loadFilter(idDev, deviceStructure.getIdDevMdl(), sessionUser.getLanguage());
	}

	// get variable table
	int idVarTab = 0;
	String strVarTab = request.getParameter("vt");
	if( strVarTab != null )
		idVarTab = Integer.parseInt(strVarTab);
	
	CurrUnit.setCurrentSession(ServletHelper.retrieveSession(request.getRequestedSessionId(), request));
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
	<script type="text/javascript" src="scripts/arch/table/ListView.js"></script>
	<script type="text/javascript" src="scripts/arch/table/ListViewFisa.js"></script>
	<script type="text/javascript" src="scripts/arch/table/ListViewDyn.js"></script>
	<script type="text/javascript" src="scripts/arch/PM.js"></script>
	<script type="text/javascript" src="scripts/arch/table/ListViewSort.js"></script>
	<script type="text/javascript" src="scripts/arch/comm/Communication.js"></script>
	<script type="text/javascript" src="scripts/app/applmask.js"></script>
	<script type="text/javascript" src="scripts/arch/jt_.js"/></script>
	<script type="text/javascript" src="mobile/scripts/arch/util.js"/></script>
	<script type="text/javascript" src="scripts/app/dtlview.js"></script>
	<script type="text/javascript" src="mobile/scripts/device.js"></script>
</head>

<body onLoad="MTstopServerComm(); adjustContainer(); dtlmain_onload(); unlockModUser(); forceReloadPoller();">
<table id="tableTop" class="tableTop">
	<tr>
		<td>
			<select id="selectPage" name="selectPage" class="selectTop" onChange="onSelectPage(this.value)">
				<option value="DeviceMain.jsp"><%=lang.getString("mobile", "main")%></option>
				<option value="DeviceReadOnly.jsp" selected><%=lang.getString("mobile", "all_RO")%></option>
				<option value="DeviceParameters.jsp"><%=lang.getString("mobile", "parameters")%></option>
				<option value="DeviceAlarms.jsp"><%=lang.getString("mobile", "alarms")%></option>
				<option value="DeviceGraph.jsp?type=<%=GraphConstant.TYPE_HACCP%>"><%=lang.getString("dtlview", "tab4name")%></option>
				<option value="DeviceGraph.jsp?type=<%=GraphConstant.TYPE_HISTORICAL%>"><%=lang.getString("dtlview", "tab5name")%></option>
			</select>
		</td>
	</tr>
</table>
<div id="divContainerTrx" class="divContainerTrx">
<input type="hidden" id="statusoffline" value="<%=offline%>">
<input type='hidden' id='virtkeyboard' value='off'>
<input type='hidden' id='open' value="<%=lang.getString("htmlfisa","open")%>"/>
<input type='hidden' id='close' value="<%=lang.getString("htmlfisa","close")%>"/>
<input type="hidden" id="topdesc" value="<%=lang.getString("navbar","ncode11")%>"/>
<input type='hidden' id='s_maxval' value="<%=lang.getString("dtlview","s_maxval")%>"/>
<input type='hidden' id='s_minval' value="<%=lang.getString("dtlview","s_minval")%>"/>
<%
	ReadOnlyAllDtlDevice filterAllRO = new ReadOnlyAllDtlDevice(sessionUser, sessionUser.getLanguage(), devdtl.getIdDevice());
	VarphyBean[] vars = VarAggregator.retrieveByPriorityAndDescription(sessionUser.getIdSite(), sessionUser.getLanguage(), devdtl.getIdDevice());
	filterAllRO.profileVariables(vars);
%>
<table width="100%">
	<tr>
		<td width="15px"><%=CurrUnit.getRefreshableStatusAssint("<img valign='middle' src='images/led/LB0.gif'/>;<img valign='middle' src='images/led/LB1.gif'/>;<img valign='middle' src='images/led/LB2.gif'/>;<img valign='middle' src='images/led/LB3.gif'/>")%></td>
		<td width="*" class="standardTxt"><b><%=devdtl.getTitle()%></b></td>
		<td width="24px"><img src="mobile/images/actions/refresh_on_black.png" style="cursor:pointer;" onClick="onSelectPage('DeviceReadOnly.jsp')"></td>
		<td width="60px" class="standardTxt"><b>Refresh</b></td>
	</tr>
	<tr>
		<td colspan="4"><%=filterAllRO.renderVariablesOldMethod("rodtlview")%></td>
	</tr>
</table>
</div>
<jsp:include page="Messages.jsp" flush="true" />
</body>
</html>
