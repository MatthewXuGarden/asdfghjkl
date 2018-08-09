<%@page import="com.carel.supervisor.presentation.svgmaps.SvgMapsUtils"%>
<%@page import="com.carel.supervisor.presentation.svgmaps.SvgMapsTranslator"%>
<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.director.packet.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	ServletHelper.validateSession(sessionUser);
	String jsession = request.getSession().getId();
	LangService lang = LangMgr.getInstance().getLangService(sessionUser.getLanguage());
	boolean bEnergy = PacketMgr.getInstance().isFunctionAllowed("energy");
	// check if there are svgmaps installed in PVPRO (yes: create link for maps)
	boolean svgmaps = SvgMapsUtils.checkSvgMaps();
	boolean maps_plugin = PacketMgr.getInstance().isFunctionAllowed("svgmaps");

%>
<html>
<head>
	<title>PVPRO</title>
	<base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="scripts/arch/Timer.js"></script>
	<script type="text/javascript" src="mobile/scripts/menu.js"></script>
</head>
<body bgcolor="#000000" style='margin:0px;' onLoad="onLoadMenu();">
	<input type="hidden" id="jsession" value="<%=jsession%>">
	<input type="hidden" id="exit_confirmation" value="<%=lang.getString("menu", "entry999")%>">
	<div style="display:none;" id="pvpro_time"></div>
	<table width="100%" border=0 valign='top' cellspacing="2" cellpadding="0" style="background-image:url(images/pvpro20/sfumatura.png); background-repeat:repeat-x;">
		<tr>
			<td width="*" align="center">
				<table>
					<tr>
						<%if( maps_plugin && svgmaps ) {%>
						<td align="center" width="80px">
							<img src="mobile/images/menu/svgmaps.png" style="cursor:pointer;" onClick="goToMaps()">
						</td>
						<%}%>
						<td align="center" width="80px">
							<img src="mobile/images/menu/map1.png" style="cursor:pointer;" onClick="onSelectPage('Devices.jsp')">			
						</td>
						<td align="center" width="80px">
							<img src="mobile/images/menu/alarms1.png" style="cursor:pointer;" onClick="onSelectPage('Alarms.jsp')">
						</td>
						<td align="center" width="80px">
							<img src="mobile/images/menu/events1.png" style="cursor:pointer;" onClick="onSelectPage('Events.jsp')">
						</td>
						<%if( bEnergy ) {%>
						<td align="center" width="80px">
							<img src="mobile/images/menu/energy1.png" style="cursor:pointer;" onClick="onSelectPage('Energy.jsp')">
						</td>
						<%}%>
<!-- 						<td align="center" width="80px"> -->
<!-- 							<img src="mobile/images/menu/help_on.png" style="cursor:pointer;" onClick="window.open('LOC_EN/PVPROloc_index.htm', '_blank')"> -->
<!-- 						</td> -->
						<td align="center" width="80px">
							<img src="mobile/images/menu/logout_on.png" style="cursor:pointer;" onClick="onLogout()">
						</td>
					</tr>
				</table>
			</td>
			<td align="center" width="60px">
				<img id="btnPrev" src="mobile/images/menu/prev1.png" style="cursor:pointer;display:none;" onClick="onBtnPrev()">			
			</td>
			<td align="center" width="60px">
				<img id="btnNext" src="mobile/images/menu/next1.png" style="cursor:pointer;display:none;" onClick="onBtnNext()">
			</td>
		</tr>
	</table>
</body>
</html>
