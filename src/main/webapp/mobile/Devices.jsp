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

	// create transaction
	UserTransaction ut = new UserTransaction("BDeviceView", "");
	BoMaster oMaster = (BoMaster)FactoryObject.newInstance("com.carel.supervisor.presentation.bo." + ut.getTrxName(),
		new Class[] { String.class }, new Object[] { sessionUser.getLanguage() });
	ut.setBoTrx(oMaster);
	ut.setProperty("curTab", "tab1name");
	sessionUser.addNewUserTransaction(ut);

	LineBeanList lineList = new LineBeanList();
	LineBean[] lines = lineList.retrieveLines(sessionUser.getIdSite());
	StringBuffer sbSelectOptions = new StringBuffer(); 
	for(int i = 0; i < lines.length; i++) {
		int idLine = lines[i].getIdline();
		if( idLine > 0 ) {
			sbSelectOptions.append("<option value='" + idLine + "'>");
			sbSelectOptions.append(lang.getString("mobile", "line_no") + lines[i].getCode() + " " + lines[i].getComport());
			sbSelectOptions.append("</option>\n");
		}
	}
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
	<script type="text/javascript" src="mobile/scripts/app/deviceview_n.js"></script>
</head>

<body onLoad="MTstopServerComm(); adjustContainer(); onLoadDevices(); forceReloadPoller();">
<table id="tableTop" class="tableTop">
	<tr>
		<td>
			<select id="selectLine" name="selectLine" class="selectTop" onChange="onSelectLine(this.value)">
			<option value="0"><%=lang.getString("mobile", "all_lines")%></option>
			<%=sbSelectOptions.toString()%>
			</select>
		</td>
	</tr>
</table>

<div id="divContainerTrx" class="divContainerTrx">
	<table id="deviceviewcontainer" border="0" cellpadding="2" cellspacing="0" width="100%" align="left">
	</table>
</div>

<jsp:include page="Messages.jsp" flush="true" />
</body>
</html>
