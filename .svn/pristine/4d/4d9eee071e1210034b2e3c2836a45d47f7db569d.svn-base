<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.bo.master.BoMaster"
	import="com.carel.supervisor.base.factory.FactoryObject"
	import="com.carel.supervisor.presentation.bean.GroupListBean"
	import="com.carel.supervisor.presentation.bean.DeviceStructureList"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.ProductInfoMgr"
	import="com.carel.supervisor.director.guardian.GuardianCheck"
	import="com.carel.supervisor.presentation.assistance.GuardianConfig"
	import="com.carel.supervisor.presentation.bean.rule.ActionBeanList"
	import="com.carel.supervisor.presentation.bo.helper.GuardianHelper"
	import="com.carel.supervisor.dataaccess.support.Information"
	import="com.carel.supervisor.presentation.bo.BSystem"
	import="com.carel.supervisor.director.DirectorMgr"
	import="com.carel.supervisor.device.DeviceStatusMgr"
	import="com.carel.supervisor.plugin.parameters.ParametersMgr"
	import="com.carel.supervisor.base.system.PvproInfo"
	import="com.carel.supervisor.presentation.bean.DeviceDetectBean"
	import="com.carel.supervisor.dataaccess.event.EventMgr"
	import="java.text.MessageFormat"
	import="com.carel.supervisor.dataaccess.dataconfig.ProductInfo"
	import="com.carel.supervisor.presentation.bean.DeviceListBean"
	import="com.carel.supervisor.base.conversion.Replacer"
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
	UserTransaction ut = new UserTransaction("BSystem", "");
	BoMaster oMaster = (BoMaster)FactoryObject.newInstance("com.carel.supervisor.presentation.bo." + ut.getTrxName(),
		new Class[] { String.class }, new Object[] { sessionUser.getLanguage() });
	ut.setBoTrx(oMaster);
	ut.setProperty("curTab", "tab6name");
	sessionUser.addNewUserTransaction(ut);
	
	String sessionName= "alrglb";
	GroupListBean groups = sessionUser.getGroup();
	int[] gids = groups.getIds();
	DeviceStructureList deviceStructureList = groups.getDeviceStructureList(); 
	int[] ids = deviceStructureList.retrieveIdsByGroupsId(gids);
	sessionUser.getTransaction().setIdDevices(ids);
	boolean hasAlarm = DeviceStatusMgr.getInstance().existAlarm(ids);
        
	String desc1 = lang.getString("menu","mgr");
	String[] engine_click = new String[3];
	engine_click[0] = "nop&folder=mgr&bo=BSystem&type=menu&desc=";
	engine_click[1] = "1";
	engine_click[2] = "&resource=../wizard/SubTab2.jsp&curTab=tab1name";
	String[] guardian_click = new String[3];
	guardian_click[0] = "nop&folder=mgr&bo=BSystem&type=menu&desc=";
	guardian_click[1] = "1";
	guardian_click[2] = "&resource=SubTab5.jsp&curTab=tab5name";
	String[] site_click = new String[3];
	site_click[0] = "nop&folder=siteview&bo=BSiteView&type=menu&desc=";
	site_click[1] = "1";
	site_click[2] = "&resource=SubTab1.jsp&curTab=tab1name";
	
	// no need for links on mobile
	boolean engine_link = false;	//sessionUser.isTabActive("mgr","tab1name");
	boolean guardian_link = false;	//sessionUser.isTabActive("mgr","tab5name");
	boolean site_link = false;		//sessionUser.isTabActive("siteview","tab1name");
	
	String txtMessages = lang.getString("multimsg","message");
	String txtGuardian = lang.getString("mgr","tab5name");
	String txtLicenseOn = lang.getString("top","licenseOn");
	String txtLicenseOff = lang.getString("top","licenseOff");

	//system page
	String htmlAlarmsList = "";
	String htmlMessageHeader = "<table width='100%' class='tbMessage' cellpadding=0 cellspacing=0>";
	String htmlMessageFooter = "</table>";
	String htmlMessage = "";
	int i=1;
	String engineMsg = "";
	int engine_code = ServletHelper.messageToNotify();
	switch(engine_code)
	{
		case 1: 
			engineMsg = lang.getString("top", "message4");
			break;
		case 2: 
			engineMsg = lang.getString("top", "message2");
			break;
		case 3: 
			engineMsg = lang.getString("top", "message1");
			break;
	}
	String tdclassName = "";
	if(engineMsg != "")
	{
		htmlMessage += "<tr><td "+tdclassName+" align='center' "+(engine_link?"onclick=link('"+engine_click[0]+"','"+engine_click[1]+"','"+engine_click[2]+"')":"")+" "+(i++%2==0?"":"class='Row2'")+">"+engineMsg+"</td></tr>";
	}
	String guardianMsg = "";
	int[] guardian_code = DirectorMgr.getInstance().getGuardian_code();
	if(guardian_code[0] == 1 || guardian_code[0] == 2)
	{
		guardianMsg = lang.getString("top","message3");
	}
	else if(guardian_code[0] == 4)
	{
		guardianMsg = "guardianPRO countdown "+lang.getString("guardian","countdown")+" "+guardian_code[1];
	}
	if(guardianMsg != "")
	{
		htmlMessage += "<tr><td "+tdclassName+" align='center' "+(guardian_link?"onclick=link('"+guardian_click[0]+"','"+guardian_click[1]+"','"+guardian_click[2]+"')":"")+"  "+(i++%2==0?"":"class='Row2'")+">"+guardianMsg+"</td></tr>";
	}
	if(!DirectorMgr.getInstance().isPvproValid())
	{
		if(Information.getInstance().isTrialValid())
		{
			htmlMessage += "<tr><td "+tdclassName+" align='center' "+(engine_link?"onclick=link('"+engine_click[0]+"','"+engine_click[1]+"','"+engine_click[2]+"') ":"")+(i++%2==0?"":"class='Row2'")+">"+txtLicenseOn+"</td></tr>";
			String tmp = EventMgr.getInstance().retriveMessage(1, lang.getLanguage(), "S029");
			// the Replacer.replace method is used to allow the format function 
			// escapes the "'" character and shows correctly the countdown int parameter
			tmp = Replacer.replace(tmp,"'","''");
			String countdownmsg = MessageFormat.format(tmp, Information.getInstance().getCountDown());
			htmlMessage += "<tr><td "+tdclassName+" align='center' "+(engine_link?"onclick=link('"+engine_click[0]+"','"+engine_click[1]+"','"+engine_click[2]+"') ":"")+(i++%2==0?"":"class='Row2'")+">"+countdownmsg+"</td></tr>";
		}
	  	else
	  		htmlMessage += "<tr><td "+tdclassName+" align='center' "+(engine_link?"onclick=link('"+engine_click[0]+"','"+engine_click[1]+"','"+engine_click[2]+"') ":"")+(i++%2==0?"":"class='Row2'")+">"+txtLicenseOff+"</td></tr>";
	}
	if(	ParametersMgr.isMotorStopped() ) {
		boolean param_link = sessionUser.isTabActive("parameters","tab1name");
		String[] param_click = new String[3];
		param_click[0] = "nop&folder=parameters&bo=BParameters&type=menu&desc=";
		param_click[1] = "1";
		param_click[2] = "&resource=../parameters/SubTab1.jsp&curTab=tab1name";
		String strParamOff = lang.getString("parameters", "stoppedpluginicon");
		htmlMessage += "<tr><td "+tdclassName+" align='center' "+(param_link?"onclick=link('"+param_click[0]+"','"+param_click[1]+"','"+param_click[2]+"') ":"")+(i++%2==0?"":"class='Row2'")+">"+strParamOff+"</td></tr>";
	}

	if( PvproInfo.getInstance().getLicenseOverload() > 0 ) {
		String strLicense = lang.getString("siteview", "maxlicenselabel");
		htmlMessage += "<tr><td "+tdclassName+" align='center' "+(site_link?"onclick=link('"+site_click[0]+"','"+site_click[1]+"','"+site_click[2]+"') ":"")+(i++%2==0?"":"class='Row2'")+">"+strLicense+"</td></tr>";
   	}
	
	if( PvproInfo.getInstance().isLoggingOverload() ) {
		String strLoggingOverload = lang.getString("mgr", "sysoverload1") + "<BR>" + lang.getString("mgr", "sysoverload2") + PvproInfo.getInstance().getLoggingOverload();
		htmlMessage += "<tr><td "+tdclassName+" align='center' " + (i++%2==0?"":"class='Row2'")+">" + strLoggingOverload + "</td></tr>";
	}
	if( DeviceDetectBean.isDetection() ) {
		String strDeviceDetection = lang.getString("mgr", "device_detection");
		htmlMessage += "<tr><td "+tdclassName+" align='center' " + (i++%2==0?"":"class='Row2'")+">" + strDeviceDetection + "</td></tr>";
	}
	String strBTAlert = PvproInfo.getInstance().getBTAlert();
	if( strBTAlert.length() > 0 ) {
		String strScheduledBackup = lang.getString("multimsg", "BTAlert1") + strBTAlert;
		htmlMessage += "<tr><td "+tdclassName+" align='center' " + (i++%2==0?"":"class='Row2'")+">" + strScheduledBackup + "</td></tr>";
	}
	
	if (!htmlMessage.equals(""))
		htmlAlarmsList = htmlMessageHeader + htmlMessage + htmlMessageFooter;
	
	BSystem bSystem = (BSystem)sessionUser.getCurrentUserTransaction().getBoTrx();
	String safetytable = bSystem.getHTMLSafetyLevel(lang.getLanguage());
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
	<script type="text/javascript" src="scripts/app/system.js"></script>
</head>

<body onLoad="MTstopServerComm(); forceReloadPoller();">

<table id="tableTop" class="tableTop">
	<tr>
		<td class="standardTxt"><b><%=sessionUser.getSiteName()%></b></td>
	</tr>
</table>

<input type='hidden' id='txtGuardian' value='<%=txtGuardian%>'/>
<input type="hidden" id="desc1" value="<%=desc1 %>"/>

<center>
<table border="0" cellspacing="1" cellpadding="1">
	<%if( !htmlAlarmsList.isEmpty() ) {%>	
	<tr>
		<td class="tdTitleTable"><%=txtMessages%></td>
	</tr>
	<tr>
		<td>
			<%=htmlAlarmsList%>
		</td>
	</tr>
	<%}%>	
	<tr>
		<td height='10px'></td>
	</tr>
	<tr>
		<td class="tdTitleTable" id="tdguardian">
		</td>
	</tr>
	<tr>
		<td align="center" id="tdsafetylevel">
			<%=safetytable%>
		</td>
	</tr>
</table>
</center>

<jsp:include page="Messages.jsp" flush="true" />
</body>
</html>
