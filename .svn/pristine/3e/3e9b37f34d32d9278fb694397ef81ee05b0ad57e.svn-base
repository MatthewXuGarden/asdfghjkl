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
	import="com.carel.supervisor.plugin.energy.*"
	import="java.util.*"
	import="org.jfree.data.general.*"
	import="org.jfree.data.category.*"
	import="org.jfree.chart.*"
	import="org.jfree.chart.plot.*"
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
	UserTransaction ut = new UserTransaction("BEnergy", "");
	BoMaster oMaster = (BoMaster)FactoryObject.newInstance("com.carel.supervisor.presentation.bo." + ut.getTrxName(),
		new Class[] { String.class }, new Object[] { sessionUser.getLanguage() });
	ut.setBoTrx(oMaster);
	ut.setProperty("curTab", "tab1name");
	ut.setProperty("resource", "/mobile/Energy.jsp");
	sessionUser.addNewUserTransaction(ut);
	
	// plugin status
	EnergyMgr emgr = EnergyMgr.getInstance();
	Boolean energyrunning = emgr.getStartedplugin();
	Boolean schedrunning = emgr.getActiveRunning();
	Boolean loggerrunning = emgr.getLoggerRunning();
	EnergyConfiguration ecfg = emgr.getSiteConfiguration();
	Integer nMaxGroup = emgr.getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10);
	Integer nMaxConsumer = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
	int nConsumers = 0;
	try {
		nConsumers = ecfg.getConsumerList().size();
	}
	catch (Exception e) {
	}
	Boolean bConfig = nConsumers > 0;
	Boolean bRunning = loggerrunning;
	 
	// active configuration
	Boolean bTimeslot = false;
	String strTimeslot = "***";
	String strKWhCost = "***";
	String strActiveCfg = emgr.getStringProperty("active_cfg");
	if( strActiveCfg.equals("time_slot") ) {
		bTimeslot = true;
		EnergyProfile ep = emgr.getEnergyProfile();
		int iTS = ep.getCurrentTimeSlotIndex();
		EPTimeSlot currentTimeSlot = ep.getTimeSlot(iTS);
		strTimeslot = currentTimeSlot.getName();
		strKWhCost = EGUtils.formatcost(currentTimeSlot.getCost());
	}
	
	// retrieve values for black/green panels 
	EnergyReport report = null;
	EnergyReportRecord record = null;
	String strReportTag = "site";
	int nAvg;
	GregorianCalendar cal = new GregorianCalendar();
	cal.setMinimalDaysInFirstWeek(4);
	cal.setFirstDayOfWeek(Calendar.MONDAY);
	int nYear = cal.get(Calendar.YEAR);
	int nMonth = cal.get(Calendar.MONTH) + 1;
	int nWeek = cal.get(Calendar.WEEK_OF_YEAR);
	int nDay = cal.get(Calendar.DAY_OF_MONTH);
	int nDow = cal.get(Calendar.DAY_OF_WEEK) - 1;
	if( nDow == 0 )
		nDow = 7;

	// current month report
	report = emgr.getMonthlyReport(nMonth, nYear);
	record = report.getReportRecord(strReportTag);
	String strCurrentMonthKWh = record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh());
	String strCurrentMonthKgCO2 = record.getKgco2().equals(Float.NaN) ? "***" : EGUtils.formatkgco2(record.getKgco2());
	
	// previous month report
	report = emgr.getMonthlyReport(nMonth == 1 ? 12 : nMonth - 1, nMonth == 1 ? nYear - 1 : nYear);
	record = report.getReportRecord(strReportTag);
	String strPreviousMonthKWh = record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh());
	
	// current week report
	report = emgr.getWeeklyReport(nWeek);
	record = report.getReportRecord(strReportTag);
	String strCurrentWeekKWh = record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh());
	String strCurrentWeekKgCO2 = record.getKgco2().equals(Float.NaN) ? "***" : EGUtils.formatkgco2(record.getKgco2());
	
	// previous week report
	report = emgr.getWeeklyReport(nWeek - 1);
	record = report.getReportRecord(strReportTag);
	String strPreviousWeekKWh = record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh());
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
	<script type="text/javascript" src="mobile/scripts/app/energy.js"></script>
</head>

<body onLoad="MTstopServerComm(); adjustContainer(); forceReloadPoller(); onLoadDashboard();">
<table id="tableTop" class="tableTop">
	<tr valign="middle">
		<td class="standardTxt" width="*"><b><%=lang.getString("menu", "energy")%></b></td>
		<td width="26px"><img src="<%=bConfig && !bRunning ? "mobile/images/actions/start_on.png" : "mobile/images/actions/start_off.png"%>" style="cursor:pointer;" onClick="<%=bConfig && !bRunning ? "onCommand('start', 'confirmstart')" : ""%>"></td>
		<td class="standardTxt"><%=lang.getString("button", "start")%></td>
		<td width="26px"><img src="<%=bConfig && bRunning ? "mobile/images/actions/stop_on.png" : "mobile/images/actions/stop_off.png"%>" style="cursor:pointer;" onClick="<%=bConfig && bRunning ? "onCommand('stop', 'confirmstop')" : ""%>"></td>
		<td class="standardTxt"><%=lang.getString("button", "stop")%></td>
		<td width="26px"><img src="mobile/images/actions/refresh_on.png" style="cursor:pointer;" onClick="onCommand('refresh')"></td>
		<td class="standardTxt"><%=lang.getString("button", "refresha")%></td>
	</tr>
</table>

<input type="hidden" value="<%=sessionUser.getSessionId()%>" id="sessionid">
<input type="hidden" value="<%=lang.getString("energy","confirmstart")%>" id="confirmstart">
<input type="hidden" value="<%=lang.getString("energy","confirmstop") %>" id="confirmstop">
<input type="hidden" value="true" id="plgnotcnf">
<input type="hidden" value="true" id="plgennotreg">
<%if( bConfig ) {
	Map<String, EnergyReportRecord> list = emgr.getInstantReport();
	DefaultPieDataset dataset = new DefaultPieDataset();
	long timevalue = System.currentTimeMillis();
	Integer numgrp = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMGROUPS,10);
	for(int i = 1; i <= numgrp; i++) {
		EnergyReportRecord energyReportRecordGroup = list.get("group" + i);
		if( energyReportRecordGroup != null )
			dataset.setValue(list.get("group"+i).getName(), list.get("group"+i).getKwh());
	}
	EnergyReportRecord energyReportRecordOther = list.get("other"); 
	if( energyReportRecordOther != null )
		dataset.setValue(lang.getString("energy", "other"), energyReportRecordOther.getKwh());
	ut.setAttribute("st1pie"+timevalue, dataset);
%>
<form id="energycommand" name="energycommand" method="post" action="servlet/master;jsessionid=<%=sessionUser.getSessionId()%>">
	<input type="hidden" value="" id="cmd" name="cmd">
	<input type="hidden" value="<%=energyrunning%>" id="run" name="run">
	<input type="hidden" value="<%=schedrunning%>" id="schedrun" name="schedrun">
	<input type="hidden" value="<%=loggerrunning%>" id="logrun" name="logrun">
</form>
<table width="99%">
	<tr valign="top">
		<td width="70%">
			<table width="100%">
				<tr>
					<td align="center">
						<img alt="Chart" align="middle" height="270" width="500" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?charttype=piechart&width=500&height=270&um=kWh&imgid=st1pie<%=timevalue%>">			
					</td>
				</tr>
				<tr>
					<td align="center">
					
<table id="energyreporttable" width="99%" align="center" cellspacing="0" cellpadding="5" class="table">
	<tr>
		<td class="th"
			style="height: 26px; font-size: 14px; font-weight: bold;vertical-align: baseline;"
			align="center" width="40%" colspan=3><%=lang.getString("energy","enerdescription") %>
			<br>
		</td>
		<td class="th"
			style="height: 26px; font-size: 14px; font-weight: bold;vertical-align: baseline;"
			align="center" width="10%" colspan=2>
			<%=lang.getString("energy","enerkw") %>
		</td>
		<td class="th"
			style="height: 26px; font-size: 14px; font-weight: bold;vertical-align: baseline;"
			align="center" width="10%" colspan=2>
			<%=lang.getString("energy","enerkwh") %>
		</td>
		<td class="th"
			style="height: 26px; font-size: 14px; font-weight: bold;vertical-align: baseline;"
			align="center" width="10%" colspan=2>
			<%=lang.getString("energy","enerkgco2") %>
		</td>
	</tr>
	<%
		if (list != null) {
			EnergyReportRecord energyReportRecordSite = list.get("site");
	%>
	<tr class="Row1">
		<td class="standardTxt tdenergy tdenergyr" style="font-size: 12px;font-weight: bold;color: blue;"
			align="left" colspan=3><%=lang.getString("energy","enersitetotal") %>
		</td>
		<td class="standardTxt tdenergy" style="font-size: 12px;font-weight: bold;color: blue;"
			align="right"><%=(energyReportRecordSite.getKw().equals(Float.NaN)) ? "***"
							: EGUtils.formatkw(energyReportRecordSite.getKw())%></td>
		<td class="tdenergy">
			&nbsp;
		</td>
		<td class="tdenergy standardTxt" style="font-size: 12px;font-weight: bold;color: blue;"
			align="right"><%=(energyReportRecordSite.getKwh().equals(Float.NaN)) ? "***"
							: EGUtils.formatkwh(energyReportRecordSite.getKwh())%></td>
		<td class="tdenergy tdenergyr">
			&nbsp;
		</td>
		<td class="tdenergy standardTxt" style="10px; font-size: 12px;font-weight: bold;color: blue;"
			align="right"><%=(energyReportRecordSite.getKgco2().equals(Float.NaN)) ? "***"
							: EGUtils.formatkgco2(energyReportRecordSite.getKgco2())%></td>
		<td class="tdenergy">
			&nbsp;
		</td>
	</tr>

	<%
		for (int i = 1; i <= numgrp; i++) {
			EnergyReportRecord energyReportRecordGroup = list.get("group" + i);
			if (energyReportRecordGroup == null){
				continue;
			}
	%>

	<tr class="Row1">
		<td class="tdenergy">
			&nbsp;
		</td>
		<td class="tdenergy tdenergyr" align="left" style="font-size: 12px;font-weight: bold;"
			colspan=2>
			<%=energyReportRecordGroup.getName()%>
		</td>
		<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
			align="right" width="10%"><b><%=(energyReportRecordGroup.getKw().equals(Float.NaN)) ? "***"
								: EGUtils.formatkw(energyReportRecordGroup.getKw())%></b></td>
		<td class="tdenergy tdenergyr">
			&nbsp;
		</td>
		<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
			align="right" width="10%"><%=(energyReportRecordGroup.getKwh().equals(Float.NaN)) ? "***"
								: EGUtils.formatkwh(energyReportRecordGroup.getKwh())%></td>
		<td class="tdenergy tdenergyr">
			&nbsp;
		</td>
		<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
			align="right" width="10%"><%=(energyReportRecordGroup.getKgco2().equals(Float.NaN)) ? "***"
								: EGUtils.formatkgco2(energyReportRecordGroup.getKgco2())%></td>
		<td class="tdenergy tdenergyr">
			&nbsp;
		</td>
	</tr>

	<%
		Integer numconsumer = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS,10);
		for (int a = 1; a <= numconsumer; a++) {
			EnergyReportRecord energyReportRecordConsumer = list.get("cons" + i + "." + a);
			if (energyReportRecordConsumer == null)
				continue;
	%>

	<tr class="Row1">
		<td class="tdenergy">
			&nbsp;
		</td>
		<td class="tdenergy">
			&nbsp;
		</td>
		<td class="tdenergy tdenergyr" align="left" style="font-size: 12px;"><%=energyReportRecordConsumer.getName()%></td>
		<td class="tdenergy standardTxt tdenergyr" style="font-size: 10px;text-align: right;" colspan=2>
			<%=(energyReportRecordConsumer.getKw().equals(Float.NaN)) ? "***" : EGUtils.formatkw(energyReportRecordConsumer.getKw())%>
		</td>
		<td class="tdenergy standardTxt tdenergyr" style="font-size: 10px;text-align: right;" colspan=2>
			<%=(energyReportRecordConsumer.getKwh().equals(Float.NaN)) ? "***"
									: EGUtils.formatkwh(energyReportRecordConsumer.getKwh())%></td>
		<td class="tdenergy standardTxt tdenergyr" style="font-size: 10px;text-align: right;" colspan=2>
			<%=(energyReportRecordConsumer.getKgco2().equals(Float.NaN)) ? "***"
									: EGUtils.formatkgco2(energyReportRecordConsumer.getKgco2())%></td>
	</tr>
	<%
		}
	}
	energyReportRecordOther = list.get("other"); 
		if(energyReportRecordOther!=null){
	%>
			<tr class="Row1">
				<td class="tdenergy">
					&nbsp;
				</td>
				<td class="tdenergy tdenergyr" align="left" style="font-size: 12px;font-weight: bold;"
					colspan=2>
					<%=lang.getString("energy","other") %>
				</td>
				<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
					align="right" width="10%">---</td>
				<td class="tdenergy tdenergyr">
					&nbsp;
				</td>
				<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
					align="right" width="10%"><%=(energyReportRecordOther.getKwh().equals(Float.NaN)) ? "***"
										: EGUtils.formatkwh(energyReportRecordOther.getKwh())%></td>
				<td class="tdenergy tdenergyr">
					&nbsp;
				</td>
				<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
					align="right" width="10%"><%=(energyReportRecordOther.getKgco2().equals(Float.NaN)) ? "***"
										: EGUtils.formatkgco2(energyReportRecordOther.getKgco2())%></td>
				<td class="tdenergy tdenergyr">
					&nbsp;
				</td>
			</tr>
	<%
		}
	}
	%>
</table>

					</td>
				</tr>
			</table>
		</td>
		<td width="30%">
			<div class="dashboard">
				<!-- rounded top -->
				<div class="topleftcorner"></div><div class="toprightcorner"></div>
				<div class="homeVars">
			 		<table class='DevHomeVarsTable'>
			 			<tr>
							<td class='tdfisa' style='text-align:left;'><%=lang.getString("energy", "total_energy")%> <%=lang.getString("energy", "current_month")%></td>
						</tr>
						<tr>
							<td class='lcd'>
								<div class='lcdValue'><b><%=strCurrentMonthKWh%></b></div>
								<div class='lcdMeasure'>[<%=lang.getString("energy", "enerkwh")%>]</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class='tdfisa' style='text-align:left;'><%=lang.getString("energy", "total_energy")%> <%=lang.getString("energy", "previous_month")%></td>
						</tr>
						<tr>
							<td class='lcd'>
								<div class='lcdValue'><b><%=strPreviousMonthKWh%></b></div>
								<div class='lcdMeasure'>[<%=lang.getString("energy", "enerkwh")%>]</div>
							</td>
						</tr>
						<tr>
							<td colspan=2" class='tdfisa' style='text-align:left;'><%=lang.getString("energy", "total_energy")%> <%=lang.getString("energy", "current_week")%></td>
						</tr>
						<tr>
							<td class='lcd'>
								<div class='lcdValue'><b><%=strCurrentWeekKWh%></b></div>
								<div class='lcdMeasure'>[<%=lang.getString("energy", "enerkwh")%>]</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class='tdfisa' style='text-align:left;'><%=lang.getString("energy", "total_energy")%> <%=lang.getString("energy", "previous_week")%></td>
						</tr>
						<tr>
							<td class='lcd'>
								<div class='lcdValue'><b><%=strPreviousWeekKWh%></b></div>
								<div class='lcdMeasure'>[<%=lang.getString("energy", "enerkwh")%>]</div>
							</td>
						</tr>
						<%if( bTimeslot) {%>
						<tr>
							<td class='tdfisa' style='text-align:left;'><%=lang.getString("energy", "current_ts")%></td>
						</tr>
						<tr>
							<td class='lcd'>
								<div class='lcdValue'><b><%=strTimeslot%></b> / <b><%=strKWhCost%></b></div>
								<div class='lcdMeasure'>[<%=ecfg.getSiteProperty("currency","")%>]</div>
							</td>
						</tr>
						<%} // if( bTimeslot)%>
					</table>
				</div>
				<!-- rounded bottom -->
				<div class="bottomleftcorner"></div><div class="bottomrightcorner"></div>
			</div>
			<br>
			<div class="dashboard">
				<!-- rounded top -->
				<div class="eg_topleftcorner"></div><div class="eg_toprightcorner"></div>
				<div class="homeVars">
			 		<table class='eg_GreenVarsTable'>
			 			<tr>
			 				<td class='eg_GreenLabel' style='text-align:left;'>CO2 <%=lang.getString("energy", "current_month")%></td>
			 			</tr>
			 			<tr>	
							<td class='eg_GreenLcd'>
								<div class='eg_GreenLcdValue'><b><%=strCurrentMonthKgCO2%></b></div>
								<div class='eg_GreenLcdMeasure'>[<%=lang.getString("energy", "kgco2perkw")%>]</div>
							</td>
						</tr>
			 			<tr>
			 				<td class='eg_GreenLabel' style='text-align:left;'>CO2 <%=lang.getString("energy", "current_week")%></td>
			 			</tr>
			 			<tr>
							<td class='eg_GreenLcd'>
								<div class='eg_GreenLcdValue'><b><%=strCurrentWeekKgCO2%></b></div>
								<div class='eg_GreenLcdMeasure'>[<%=lang.getString("energy", "kgco2perkw")%>]</div>
							</td>
						</tr>
					</table>
			 	</div>
				<!-- rounded bottom -->
				<div class="eg_bottomleftcorner"></div><div class="eg_bottomrightcorner"></div>
			</div>
		</td>
	</tr>
</table>
<%} else {%>
<input type="hidden" value="false" id="plgnotcnf">
<table border="0" width="100%" cellpadding="1" cellspacing="1">
	<tr>
		<td align="center"><%=lang.getString("energy","pgtobedone") %></td>
	</tr>
</table>
<form id="energycommand" name="energycommand" method="post" action="servlet/master;jsessionid=<%=sessionUser.getSessionId()%>">
	<input type="hidden" value="" id="cmd" name="cmd">
</form>
<%}%>

<jsp:include page="Messages.jsp" flush="true" />
</body>
</html>