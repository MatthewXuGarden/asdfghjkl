<%@ page language="java"  pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.bo.master.BoMaster"
	import="com.carel.supervisor.base.factory.FactoryObject"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.devices.*"
	import="com.carel.supervisor.presentation.mobile.*"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.datalog.impl.AlarmLog"
	import="com.carel.supervisor.dataaccess.datalog.impl.AlarmLogList"
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
	int idLine = 0;
	String strIdLine = request.getParameter("idline");
	if( strIdLine != null )
		idLine = Integer.parseInt(strIdLine);
	
	// create transaction
	UserTransaction ut = new UserTransaction("BAlrGlb", "");
	BoMaster oMaster = (BoMaster)FactoryObject.newInstance("com.carel.supervisor.presentation.bo." + ut.getTrxName(),
		new Class[] { String.class }, new Object[] { sessionUser.getLanguage() });
	ut.setBoTrx(oMaster);
	ut.setProperty("curTab", idAlarmTab == 1 ? "tab2name" : "tab1name");
	sessionUser.addNewUserTransaction(ut);
	
	String sitename = sessionUser.getSiteName();
	String sessionName= "alrglb";
	String alarmcomment1 = lang.getString(sessionName, "alarmcomment1");
	
	DeviceListBean deviceListBean = new DeviceListBean(sessionUser.getIdSite(), sessionUser.getLanguage());
    int[] ids = deviceListBean.getIds();	
	// line filter
	if( idLine > 0 ) {
		int[] aux = new int[ids.length];
		int n = 0;
		for(int i = 0; i < ids.length; i++)
			if( deviceListBean.getDevice(ids[i]).getIdline() == idLine )
				aux[n++] = ids[i];
		ids = new int[n];
		System.arraycopy(aux, 0, ids, 0, n);
	}
	sessionUser.getTransaction().setIdDevices(ids);
               
	String priority1woNote = sessionUser.getPropertyAndRemove("priority1woNote");
	boolean pr1 = (priority1woNote!=null) && (priority1woNote.equals("TRUE"));
	String priority2woNote = sessionUser.getPropertyAndRemove("priority2woNote");
	boolean pr2 = (priority2woNote!=null) && (priority2woNote.equals("TRUE"));
	String priority3woNote = sessionUser.getPropertyAndRemove("priority3woNote");
	boolean pr3 = (priority3woNote!=null) && (priority3woNote.equals("TRUE"));
	String priority4woNote = sessionUser.getPropertyAndRemove("priority4woNote");
	boolean pr4 = (priority4woNote!=null) && (priority4woNote.equals("TRUE"));
	
	boolean noteRequired = pr1 || pr2 || pr3 || pr4;
	String messagePr1 = lang.getString("alrmng","noteReqPr1");
	String messagePr2 = lang.getString("alrmng","noteReqPr2");
	String messagePr3 = lang.getString("alrmng","noteReqPr3");
	String messagePr4 = lang.getString("alrmng","noteReqPr4");
	String message = "";
	if (pr1) message+=messagePr1+"\n";
	if (pr2) message+=messagePr2+"\n";
	if (pr3) message+=messagePr3+"\n";
	if (pr4) message+=messagePr4+"\n";
	
	LineBeanList lineList = new LineBeanList();
	LineBean[] lines = lineList.retrieveLines(sessionUser.getIdSite());
	StringBuffer sbSelectOptions = new StringBuffer(); 
	for(int i = 0; i < lines.length; i++) {
		int id = lines[i].getIdline();
		if( id > 0 ) {
			sbSelectOptions.append("<option value='" + id + (id == idLine ? "' selected>" : "'>"));
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
	<script type="text/javascript" src="mobile/scripts/arch/table/ListView.js"></script>
	<script type="text/javascript" src="mobile/scripts/arch/table/ListViewFisa.js"></script>
	<script type="text/javascript" src="mobile/scripts/arch/table/ListViewDyn.js"></script>
	<script type="text/javascript" src="scripts/arch/PM.js"></script>
	<script type="text/javascript" src="scripts/arch/table/ListViewSort.js"></script>
	<script type="text/javascript" src="scripts/arch/comm/Communication.js"></script>
	<script type="text/javascript" src="scripts/app/applmask.js"></script>
	<script type="text/javascript" src="scripts/arch/jt_.js"/></script>
	<script type="text/javascript" src="mobile/scripts/arch/util.js"/></script>
	<script type="text/javascript" src="mobile/scripts/app/alrgbl.js"></script>
	<script type="text/javascript" src="mobile/scripts/alarms.js"></script>
</head>

<body onLoad="MTstopServerComm(); adjustContainer(); forceReloadPoller();">
<table id="tableTop" class="tableTop">
	<tr>
		<td>
			<select id="selectLine" name="selectLine" class="selectTop" onChange="onSelectLine(this.value)">
			<option value="0" <%=0 == idLine ? "selected" : ""%>><%=lang.getString("mobile", "all_lines")%></option>
			<%=sbSelectOptions.toString()%>
			</select>
			&nbsp;
			<select id="selectAlarmTable" name="selectAlarmTable" class="selectTop" onChange="onSelectAlarmTable(this.value)">
				<option value="0"<%=idAlarmTab==0 ? " selected" : ""%>><%=lang.getString("mobile", "active_alarms")%></option>
				<option value="1"<%=idAlarmTab==1 ? " selected" : ""%>><%=lang.getString("mobile", "reset_alarms")%></option>
			</select>
		</td>
	</tr>
</table>

<INPUT type="hidden" id="okmessage" name="okmessage" value="<%=lang.getString("alrglb", "okmessage")%>" /> 
<INPUT type="hidden" id="komessage" name="komessage" value="<%=lang.getString("alrglb", "komessage")%>" />
<INPUT type="hidden" id="confirmackall" name="confirmackall" value="<%=lang.getString("alrglb", "confirmackall")%>" />
<INPUT type="hidden" id="confirmresetall" name="confirmresetall" value="<%=lang.getString("alrglb", "confirmresetall")%>" />
<INPUT type="hidden" id="confirmdeleteall" name="confirmdeleteall" value="<%=lang.getString("alrglb", "confirmdeleteall")%>" /> 
<INPUT type="hidden" id="noalarmtodelete" name="noalarmtodelete" value="<%=lang.getString("alrglb", "noalarmtodelete")%>" /> 
<INPUT type="hidden" id="noalarmtoreset" name="noalarmtoreset" value="<%=lang.getString("alrglb", "noalarmtoreset")%>" /> 
<INPUT type="hidden" name="numalrmack" value="" />
<INPUT type="hidden" id="sitename" value="<%=sitename%>" />
<INPUT type="hidden" id="noteRequiredError" value="<%=message%>" />
<INPUT type="hidden" id="pr_<%=lang.getString("alrview", "alarmstate1")%>" value="1" />
<INPUT type="hidden" id="pr_<%=lang.getString("alrview", "alarmstate2")%>" value="2" />
<INPUT type="hidden" id="pr_<%=lang.getString("alrview", "alarmstate3")%>" value="3" />
<INPUT type="hidden" id="pr_<%=lang.getString("alrview", "alarmstate4")%>" value="4" />
<INPUT type="hidden" id="idLine" value="<%=idLine%>">
<INPUT type="hidden" id="alarmTable" value="<%=idAlarmTab%>">

<%if( idAlarmTab == 0 ) {
	AlarmList alarms = new AlarmList(lang.getString(sessionName, "activeal"));
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
	alarms.setHeight(360);
	
	String htmlTable1= alarms.getHTMLAlarmTable("TA", sessionUser.getLanguage(), sessionUser);
	int num_ack = alarms.getNumAck();
%>
<INPUT type="hidden" name="numalr" value="<%=alarms.getListaAllarmi().size()%>" />
<INPUT type="hidden" id="num_ack" value="<%=num_ack%>" />
<INPUT type="hidden" id="LWCtDataName1_priority_col" value="<%=alarms.getPriorityCol()%>" />

<div id="AlrList" style="width:98%;margin-left:5px;margin-right:5px;">
<%=htmlTable1%>
</div>
<%} if( idAlarmTab == 1 ) {
	AlarmCalledOfList alarmsCalledOf = new AlarmCalledOfList(lang.getString(sessionName,"calledofal"));

	String alarm_page = "";
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
	alarmsCalledOf.setHeight(360);

	String htmlTable2=alarmsCalledOf.getHTMLAlarmCalledOfTable("TalarmCalledOf",sessionUser.getLanguage(),sessionUser);
%>
<div id="AlrList" style="width:98%;margin-left:5px;margin-right:5px;">
<%=htmlTable2%>
</div>
<%} %>

<jsp:include page="Messages.jsp" flush="true" />
</body>
</html>