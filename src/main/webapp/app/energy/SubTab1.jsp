<%@ page language="java"%>
<%@page import="com.carel.supervisor.presentation.session.UserSession"%>
<%@page import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%@page import="com.carel.supervisor.plugin.energy.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.carel.supervisor.plugin.base.Plugin"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.io.OutputStream"%>
<%@page import="org.jfree.data.general.*"%>
<%@page import="org.jfree.data.category.*"%>
<%@page import="org.jfree.chart.*"%>
<%@page import="org.jfree.chart.plot.*"%>
<%@page import="com.carel.supervisor.presentation.session.UserTransaction"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	UserTransaction transactionUser = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	LangService lang = LangMgr.getInstance().getLangService(language);
	SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
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
		nConsumers = ecfg.getConsumerList(language).size();
	}
	catch (Exception e) {
	}
	Boolean bConfig = nConsumers > 0;
	 
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
	//nAvg = nDay;
	//String strCurrentMonthKWhAvg = nAvg > 0 && !record.getKwh().equals(Float.NaN) ? EGUtils.formatkwh(record.getKwh() / nAvg) : "***";
	
	// previous month report
	report = emgr.getMonthlyReport(nMonth == 1 ? 12 : nMonth - 1, nMonth == 1 ? nYear - 1 : nYear);
	record = report.getReportRecord(strReportTag);
	String strPreviousMonthKWh = record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh());
	//nAvg = report.getIntervalsNumber();
	//String strPreviousMonthKWhAvg = nAvg > 0 && !record.getKwh().equals(Float.NaN) ? EGUtils.formatkwh(record.getKwh() / nAvg) : "***";
	
	// current week report
	report = emgr.getWeeklyReport(nWeek);
	record = report.getReportRecord(strReportTag);
	String strCurrentWeekKWh = record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh());
	String strCurrentWeekKgCO2 = record.getKgco2().equals(Float.NaN) ? "***" : EGUtils.formatkgco2(record.getKgco2());
	//nAvg = nDow;
	//String strCurrentWeekKWhAvg = nAvg > 0 && !record.getKwh().equals(Float.NaN) ? EGUtils.formatkwh(record.getKwh() / nAvg) : "***";
	
	// previous week report
	report = emgr.getWeeklyReport(nWeek - 1);
	record = report.getReportRecord(strReportTag);
	String strPreviousWeekKWh = record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh());
	//nAvg = report.getIntervalsNumber();
	//String strPreviousWeekKWhAvg = nAvg > 0 && !record.getKwh().equals(Float.NaN) ? EGUtils.formatkwh(record.getKwh() / nAvg) : "***";
%>
<input type="hidden" value="<%=sessionUser.getSessionId()%>" id="sessionid">
<input type="hidden" value="<%=lang.getString("energy","confirmstart")%>" id="confirmstart">
<input type="hidden" value="<%=lang.getString("energy","confirmstop") %>" id="confirmstop">
<input type="hidden" value="<%=lang.getString("energy","confirmreset") %>" id="confirmreset">
<input type="hidden" value="<%=lang.getString("energy","confirmresetfeedback") %>" id="confirmresetfeedback">
<input type="hidden" value="true" id="plgnotcnf">
<input type="hidden" value="true" id="plgennotreg">
<%if( bConfig ) {
	Map<String, EnergyReportRecord> list = emgr.getInstantReport();
	DefaultPieDataset dataset = new DefaultPieDataset();
	long timevalue = System.currentTimeMillis();
	Integer numgrp = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMGROUPS,10);
	for (int i = 1; i <= numgrp; i++) {
		EnergyReportRecord energyReportRecordGroup = list.get("group" + i);
		if( energyReportRecordGroup != null )
			dataset.setValue(list.get("group"+i).getName(), list.get("group"+i).getKwh());
	}
	EnergyReportRecord energyReportRecordOther = list.get("other"); 
	if( energyReportRecordOther != null )
		dataset.setValue(lang.getString("energy","other"), energyReportRecordOther.getKwh());
	transactionUser.setAttribute("st1pie"+timevalue, dataset);
%>
<form method="post"	action="servlet/master;jsessionid=<%=sessionUser.getSessionId()%>" name="energycommand" id = "energycommand">
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
					<td width="15%" valign="top">
						<p>
							<%if(EnergyMgr.getInstance().getRunning() && EnergyMgr.getInstance().getActiveRunning()){ %>
							<img src="images/energy/energy_on.gif">
							<%}else{ %>
							<img src="images/energy/energy_off.png">
							<%} %>
						</p>
						<font size="2"><%=lang.getString("energy","enerfirstdata")%><br>
						<%=ecfg.getFirstSample()!=null ? sdfDateTime.format(ecfg.getFirstSample()) : "***"%><br></font> 
						<font size="2"><%=lang.getString("energy","enerlastdata")%><br>
						<%=ecfg.getLastSample()!=null ? sdfDateTime.format(ecfg.getLastSample()) : "***"%></font>
					</td>				
					<td width="*" align="center">
						<img alt="Chart" align="middle" height="270" width="500" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?charttype=piechart&width=500&height=270&um=kWh&imgid=st1pie<%=timevalue%>">			
					</td>
					<td width="15%" valign="top">&nbsp;</td>					
				</tr>
				<tr>
					<td align="center" colspan="3">
					
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
		<!--		
		<td class="th"
			style="height: 26px; font-size: 14px; font-weight: bold;vertical-align: baseline;"
			align="center" width="10%" colspan=2>
			<%//=lang.getString("energy","enerperctot") %>
		</td>
		<td class="th"
			style="height: 26px; font-size: 14px; font-weight: bold;vertical-align: baseline;"
			align="center" width="10%" colspan=2>
			<%//=lang.getString("energy","enerpercrel") %>
		</td>
		-->		
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
		<td class="th"
			style="height: 26px; font-size: 14px; font-weight: bold;vertical-align: baseline;"
			align="center" width="10%" colspan=2>
			<%=lang.getString("energy","energyreset") %>
		</td>
		<!--
		<td class="th"
			style="height: 26px; font-size: 14px; font-weight: bold;vertical-align: baseline;"
			align="center" width="10%" colspan=2>
			<%//=lang.getString("energy","enercost") %>
		</td>
 		-->		
	</tr>
	<%
		if (list != null) {
			EnergyReportRecord energyReportRecordSite = list.get("site");
	%>
	<tr class="Row1">
		<td class="standardTxt tdenergy tdenergyr" style="font-size: 12px;font-weight: bold;color: blue;"
			align="left" colspan=3>
			<% if(list.get("other") != null) 
					out.print(lang.getString("energy","enersitetotal"));
				else
					out.print(lang.getString("energy","sumofgroup"));
			%>
		</td>
		<td class="standardTxt tdenergy" style="font-size: 12px;font-weight: bold;color: blue;"
			align="right"><%=(energyReportRecordSite.getKw().equals(Float.NaN)) ? "***"
							: EGUtils.formatkw(energyReportRecordSite.getKw())%></td>
		<td class="tdenergy">
			&nbsp;
		</td>
		<!-- 
		<td class="standardTxt tdenergy" style="font-size: 12px;font-weight: bold;color: blue;"
			align="right"><%//=(energyReportRecordSite.getKw().equals(Float.NaN)) ? "***" : "100 %"%></td>
		<td class="tdenergy">
			&nbsp;
		</td>
		<td class="tdenergy standardTxt" style="font-size: 12px;font-weight: bold;color: blue;"
			align="right"><%//=(energyReportRecordSite.getKw().equals(Float.NaN)) ? "---" : "---"%></td>
		<td class="tdenergy tdenergyr">
			&nbsp;
		</td>
 		-->			
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
		<td class="tdenergy standardTxt" style="10px; font-size: 12px;font-weight: bold;color: blue;"
			align="right">
			<% if(list.get("other") == null) {%>
			<input type="checkbox" name="resetParAll" id="resetParAll" value="all" onclick="selectCheckbox('all','','')">
			<%}else{ %>
			<input type="checkbox" name="resetGroup_1%>" id="resetGroup_1" value="<%=energyReportRecordSite.getId()%>,-1" onclick="selectCheckbox('site')">
			<%} %>
			</td>
		<td class="tdenergy">
			&nbsp;
		</td>
		
		<!--
		<td class="tdenergy standardTxt" style="font-size: 12px;font-weight: bold;color: blue;"
			align="right"><%//=(energyReportRecordSite.getCost().equals(Float.NaN)) ? "***" : EGUtils.formatcost(energyReportRecordSite.getCost())%>
		</td>
		<td class="tdenergy">
			&nbsp;
		</td>
		-->
	</tr>

	<%
		int groupNO = 0;
		for (int i = -1; i <= numgrp; i++) {
			EnergyReportRecord energyReportRecordGroup = list.get("group" + i);
			if (energyReportRecordGroup == null){
				continue;
			}
			if( i > 0 ) {
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
		<!--
		<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
			align="right" width="10%">
			<%//=(energyReportRecordGroup.getKw().equals(Float.NaN) || energyReportRecordSite.getKw().equals(Float.NaN)) ? "***" : EGUtils.formatperc(energyReportRecordGroup.getKw() / energyReportRecordSite.getKw() * 100)%>
			%
		</td>
		<td class="tdenergy tdenergyr">
			&nbsp;
		</td>
		<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
			align="right" width="10%"><%//=(energyReportRecordGroup.getKw().equals(Float.NaN)) ? "***" : "100 %"%></td>
		<td class="tdenergy tdenergyr">
			&nbsp;
		</td>
		-->
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
		<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
			align="right" width="10%">
			<input type="checkbox" name="resetGroup<%=groupNO%>" id="resetGroup<%=groupNO%>" value="<%=energyReportRecordGroup.getId()%>" onclick="selectCheckbox('group','resetGroup<%=groupNO%>','')">
			</td>
		<td class="tdenergy tdenergyr">
			&nbsp;
		</td>
		<!--
		<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
			align="right" width="10%"><%//=(energyReportRecordGroup.getCost().equals(Float.NaN)) ? "***" : EGUtils.formatcost(energyReportRecordGroup.getCost())%>
		</td>
		<td class="tdenergy">
			&nbsp;
		</td>
		-->
	</tr>

	<%}
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
		<!--
		<td class="tdenergy standardTxt tdenergyr" style="font-size: 10px;text-align: right;" colspan=2>
			<%//=(energyReportRecordConsumer.getKw().equals(Float.NaN) || energyReportRecordGroup.getKw().equals(Float.NaN) || energyReportRecordSite.getKw().equals(Float.NaN)) ? "***" : EGUtils.formatperc(energyReportRecordConsumer.getKw() / energyReportRecordSite.getKw() * 100)%>
			%
		</td>
		<td class="tdenergy standardTxt tdenergyr" style="font-size: 10px;text-align: right;" colspan=2>
			<%//=(energyReportRecordConsumer.getKw().equals(Float.NaN) || energyReportRecordGroup.getKw().equals(Float.NaN) || energyReportRecordSite.getKw().equals(Float.NaN)) ? "***" : EGUtils.formatperc(energyReportRecordConsumer.getKw() / energyReportRecordGroup.getKw() * 100)%>
			%
		</td>
		-->
		<td class="tdenergy standardTxt tdenergyr" style="font-size: 10px;text-align: right;" colspan=2>
			<%=(energyReportRecordConsumer.getKwh().equals(Float.NaN)) ? "***"
									: EGUtils.formatkwh(energyReportRecordConsumer.getKwh())%></td>
		<td class="tdenergy standardTxt tdenergyr" style="font-size: 10px;text-align: right;" colspan=2>
			<%=(energyReportRecordConsumer.getKgco2().equals(Float.NaN)) ? "***"
									: EGUtils.formatkgco2(energyReportRecordConsumer.getKgco2())%></td>
		<td class="tdenergy standardTxt tdenergyr" style="font-size: 10px;text-align: right;" >
			<input type="checkbox" name="resetGroup<%=groupNO%>" id="resetGroup<%=groupNO%>" value="<%=energyReportRecordGroup.getId()%>,<%=energyReportRecordConsumer.getId()%>" onclick="selectCheckbox('device','resetGroup<%=groupNO%>','<%=energyReportRecordConsumer.getId()%>')">
			</td>
		<td class="tdenergy standardTxt tdenergyr">
			&nbsp;
		</td>
		<!-- 
		<td class="tdenergy standardTxt" style="font-size: 10px;text-align: right;" colspan=2>
			<%//=(energyReportRecordConsumer.getCost().equals(Float.NaN)) ? "***"	: EGUtils.formatcost(energyReportRecordConsumer.getCost())%>
		</td>
 		-->									
	</tr>
	
	<%
		}
		groupNO++;
	}
	%>
	<input type="hidden" name="groupNo" id="groupNo" value="<%=groupNO%>">
	<%
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
				<!--
				<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
					align="right" width="10%">
					<b><%//=(energyReportRecordOther.getKw().equals(Float.NaN)) ? "***"	: EGUtils.formatkw(energyReportRecordOther.getKw())%></b>
				</td>
				<td class="tdenergy tdenergyr">
					&nbsp;
				</td>
				<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
					align="right" width="10%">
					<%//=(energyReportRecordSite.getKw().equals(Float.NaN) || energyReportRecordOther.getKw().equals(Float.NaN)) ? "***" : EGUtils.formatperc(energyReportRecordOther.getKw()	/ energyReportRecordSite.getKw() * 100)%>
					%
				</td>
				<td class="tdenergy tdenergyr">
					&nbsp;
				</td>
				-->
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
				<td class="tdenergy standardTxt" style="font-size: 11px;font-weight: bold;"
					align="right" width="10%">
					<%//=(energyReportRecordOther.getCost().equals(Float.NaN)) ? "***" : EGUtils.formatcost(energyReportRecordOther.getCost())%>
				</td>
				<td class="tdenergy">
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
							<!-- 
							<td class='tdfisa' style='text-align:left;width:50%;'><%=lang.getString("energy", "avg_per_day")%></td>
							-->
						</tr>
						<tr>
							<td class='lcd'>
								<div class='lcdValue'><b><%=strCurrentMonthKWh%></b></div>
								<div class='lcdMeasure'>[<%=lang.getString("energy", "enerkwh")%>]</div>
							</td>
							<!--
							<td class='lcd' style='width:50%;'>
								<div class='lcdValue'><b><%//=strCurrentMonthKWhAvg%></b></div>
								<div class='lcdMeasure'>[<%//=lang.getString("energy", "enerkwh")%>]</div>
							</td>
							-->
						</tr>
						<tr>
							<td colspan="2" class='tdfisa' style='text-align:left;'><%=lang.getString("energy", "total_energy")%> <%=lang.getString("energy", "previous_month")%></td>
						</tr>
						<tr>
							<td class='lcd'>
								<div class='lcdValue'><b><%=strPreviousMonthKWh%></b></div>
								<div class='lcdMeasure'>[<%=lang.getString("energy", "enerkwh")%>]</div>
							</td>
							<!--
							<td class='lcd' style='width:50%;'>
								<div class='lcdValue'><b><%//=strPreviousMonthKWhAvg%></b></div>
								<div class='lcdMeasure'>[<%//=lang.getString("energy", "enerkwh")%>]</div>
							</td>
							-->
						</tr>
						<tr>
							<td colspan=2" class='tdfisa' style='text-align:left;'><%=lang.getString("energy", "total_energy")%> <%=lang.getString("energy", "current_week")%></td>
						</tr>
						<tr>
							<td class='lcd'>
								<div class='lcdValue'><b><%=strCurrentWeekKWh%></b></div>
								<div class='lcdMeasure'>[<%=lang.getString("energy", "enerkwh")%>]</div>
							</td>
							<!--
							<td class='lcd' style='width:50%;'>
								<div class='lcdValue'><b><%//=strCurrentWeekKWhAvg%></b></div>
								<div class='lcdMeasure'>[<%//=lang.getString("energy", "enerkwh")%>]</div>
							</td>
							-->
						</tr>
						<tr>
							<td colspan="2" class='tdfisa' style='text-align:left;'><%=lang.getString("energy", "total_energy")%> <%=lang.getString("energy", "previous_week")%></td>
						</tr>
						<tr>
							<td class='lcd'>
								<div class='lcdValue'><b><%=strPreviousWeekKWh%></b></div>
								<div class='lcdMeasure'>[<%=lang.getString("energy", "enerkwh")%>]</div>
							</td>
							<!-- 
							<td class='lcd' style='width:50%;'>
								<div class='lcdValue'><b><%//=strPreviousWeekKWhAvg%></b></div>
								<div class='lcdMeasure'>[<%//=lang.getString("energy", "enerkwh")%>]</div>
							</td>
							-->
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
<%}%>
