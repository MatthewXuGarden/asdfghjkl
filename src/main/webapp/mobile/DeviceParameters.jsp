<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.bo.master.BoMaster"
	import="com.carel.supervisor.base.factory.FactoryObject"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.devices.*"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.devices.DeviceList"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.presentation.comboset.ComboParamMap"
	import="com.carel.supervisor.device.DeviceStatusMgr"
	import="com.carel.supervisor.device.DeviceStatus"
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

	// get parameter table
	int idParamTab = 0;
	String strParamTab = request.getParameter("pt");
	if( strParamTab != null )
		idParamTab = Integer.parseInt(strParamTab);
	
	// create transaction
	UserTransaction ut = new UserTransaction("BDtlView", "");
	BoMaster oMaster = (BoMaster)FactoryObject.newInstance("com.carel.supervisor.presentation.bo." + ut.getTrxName(),
		new Class[] { String.class }, new Object[] { sessionUser.getLanguage() });
	ut.setBoTrx(oMaster);
	ut.setProperty("curTab", "tab2name");
	ut.setProperty("resource", "/mobile/DeviceParameters.jsp");
	sessionUser.addNewUserTransaction(ut);
	
	int idDev = Integer.parseInt(sessionUser.getProperty("iddev"));
	DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
	DeviceStructure deviceStructure = deviceStructureList.get(idDev);
	String strDeviceDescription = "";
	if( deviceStructure != null ) {
		strDeviceDescription = deviceStructure.getDescription();
		oMaster.loadFilter(idDev, deviceStructure.getIdDevMdl(), sessionUser.getLanguage());
	}
	ParamDetail paramDetail = new ParamDetail(sessionUser);
	String strSelectOptions = paramDetail.getSelectOptions(sessionUser.getIdSite(), idDev, sessionUser.getLanguage(), idParamTab);
	
	DeviceStatus status = DeviceStatusMgr.getInstance().getDeviceStatus(new Integer(idDev));
	boolean offline = status == null ? true : !status.getStatus();

	// combo parameters
	ComboParamMap c = new ComboParamMap();
    c.loadDeviceConf(idDev, sessionUser.getLanguage());
	sessionUser.getCurrentUserTransaction().setObjProperty("comboparam", c);
    //check set permission of parameters
	int permission = sessionUser.getVariableFilter()==ProfileBean.FILTER_SERVICES ? 2 : 0;
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
	<script type="text/javascript" src="mobile/scripts/app/dtlview_param.js"></script>
	<script type="text/javascript" src="mobile/scripts/device.js"></script>
</head>

<body onLoad="MTstopServerComm(); adjustContainer(65); onLoadDeviceParameters(); forceReloadPoller();">
<table id="tableTop" class="tableTop">
	<tr>
		<td>
			<select id="selectPage" name="selectPage" class="selectTop" onChange="onSelectPage(this.value)">
				<option value="DeviceMain.jsp"><%=lang.getString("mobile", "main")%></option>
				<option value="DeviceReadOnly.jsp"><%=lang.getString("mobile", "all_RO")%></option>
				<option value="DeviceParameters.jsp" selected><%=lang.getString("mobile", "parameters")%></option>
				<option value="DeviceAlarms.jsp"><%=lang.getString("mobile", "alarms")%></option>
				<option value="DeviceGraph.jsp?type=<%=GraphConstant.TYPE_HACCP%>"><%=lang.getString("dtlview", "tab4name")%></option>
				<option value="DeviceGraph.jsp?type=<%=GraphConstant.TYPE_HISTORICAL%>"><%=lang.getString("dtlview", "tab5name")%></option>
			</select>
			&nbsp;
			<select id="selectParamTable" name="selectParamTable" class="selectTop" onChange="onSelectParamTable(this.value)">
			<%=strSelectOptions%>
			</select>
		</td>
	</tr>
</table>
<table width="100%">
	<tr>
		<td width="*" class="standardTxt"><b><%=strDeviceDescription%></b></td>
		<td width="24px"><img src="mobile/images/actions/params_on_black.png" style="cursor:pointer;" onClick="dtlviewSetVars();"></td>
		<td width="60px" class="standardTxt"><b>Set</b></td>		
	</tr>
</table>

<div id="divContainerTrx" class="divContainerTrx">
<input type="hidden" id="s_notallowedchar" value="<%=lang.getString("mask","notallowedchar")%>"/>
<input type="hidden" id="offline" value="<%=offline%>">
<input type='hidden' id='virtkeyboard' value="off">
<input type='hidden' id='s_maxval' value="<%=lang.getString("dtlview","s_maxval")%>"/>
<input type='hidden' id='s_minval' value="<%=lang.getString("dtlview","s_minval")%>"/>
<input type='hidden' id='changegrpmsg' name='changegrpmsg' value="<%=lang.getString("usrmsg","msg03")%>">
<input type="hidden" id="permission" value="<%=permission%>">

<form name="frmdtlset" id="frmdtlset" method="post" action="servlet/master;jsessionid=<%=jsession%>">
<input type='hidden' id='cmd' name='cmd' value="set">
<input type='hidden' id='iddev' name='iddev' value="<%=idDev%>"> 
<input type='hidden' id='ids_toset' name='ids_toset' value="">
<input type='hidden' id='current_grp' name='current_grp' value="<%=idParamTab%>">

<table border='0' cellpadding="0" cellspacing="0" width="99%"> 
	<tr>
		<td>
		<div id="div_head">					
			<table border='0' id='h_table' class='table' cellpadding="0" cellspacing="1" width="99%">
				<tr class='th'>
					<td width="12%" align="center"><b><%=lang.getString("dtlview","col1")%></b></td>
					<td width="18%" align="center"><b><%=lang.getString("dtlview","col2")%></b></td>
					<td width="9%" align="center"><b><%=lang.getString("dtlview","col3")%></b></td>
					<td width="12%" align="center"><b><%=lang.getString("dtlview","col5")%></b></td>
					<td width="*" align="center"><b><%=lang.getString("dtlview","col4")%></b></td>
				</tr>
			</table>
		</div>
		<div id='div_head2' style="display:none;">
			<table border='0' id='h2_table' class='table' cellpadding="0" cellspacing="1" width="100%">
				<tr class='th'>
					<td width="25%" align="center"><b><%=lang.getString("dtlview","col1")%></b></td>
					<td width="162px" align="center"><b><%=lang.getString("dtlview","col2")%></b></td>
					<td width="*" align="center"><b><%=lang.getString("dtlview","col4")%></b></td>
				</tr>
			</table>
		</div>								
		</td>
	</tr>
	<tr>
		<td>
			<div id='div_params' style="width:100%;background-color:cacaca;"></div>					
		</td>
	</tr>
</table>
<input type="hidden" id="pt" name="pt" value="<%=idParamTab%>">
</form>
<div id='div_conf' style='display:none;'></div>
<div id='div_link'></div>
<div id='div_mm' style='display:none;'></div>
<div id="longdescdiv" style='display:none;' class="standardTxt"></div>
</div>

<jsp:include page="Messages.jsp" flush="true" />
</body>
</html>
