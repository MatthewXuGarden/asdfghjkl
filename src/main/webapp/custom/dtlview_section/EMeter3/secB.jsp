<%@page import="com.carel.supervisor.presentation.session.UserSession"%>
<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.dataaccess.language.*"
	import="com.carel.supervisor.plugin.energy.*"
	import="java.util.*"
	import="org.jfree.data.category.DefaultCategoryDataset"
%>
<jsp:useBean id="CurrUnit" class="com.carel.supervisor.presentation.sdk.obj.CurrUnit" scope="session"/>
<%
	UserSession us = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	LangService lang = LangMgr.getInstance().getLangService(us.getLanguage());

	EnergyMgr emgr = EnergyMgr.getInstance();
	if( emgr.getRunning() ) {
		EnergyConfiguration ecfg = emgr.getSiteConfiguration();
		Integer numgrp = emgr.getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10);
		Integer numconsumer = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);

		int idMeter = CurrUnit.getId();
		EnergyConsumer ecMeter = emgr.consumerLookup(idMeter);
		
		// show special fields only if the meter it is configured on energy plugin
		if( ecMeter != null ) {
			EnergyReport report = null;
			EnergyReportRecord record = null;
			String strReportTag = ecMeter.getIdgroup() == -1 && ecMeter.getIdconsumer() == -1
			? "site"
			: "cons" + ecMeter.getIdgroup() + "." + ecMeter.getIdconsumer();
			String[] astrDayNames = { "",
				lang.getString("cal", "mon"),
				lang.getString("cal", "tue"),
				lang.getString("cal", "wed"),
				lang.getString("cal", "thu"),
				lang.getString("cal", "fri"),
				lang.getString("cal", "sat"),
				lang.getString("cal", "sun")
			};
			
			// daily report
			report = ecMeter.getIdgroup() == -1 && ecMeter.getIdconsumer() == -1
				? emgr.getDailyReport()
				: emgr.getConsDailyReport(ecMeter.getIdgroup(), ecMeter.getIdconsumer());
			record = report.getReportRecord(strReportTag);
			String strDailyKWh = record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh());
			
			// current week report
			Long timestamp = System.currentTimeMillis();
			GregorianCalendar cal = new GregorianCalendar();
			cal.setMinimalDaysInFirstWeek(4);
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			int nWeek = cal.get(Calendar.WEEK_OF_YEAR);
			report = ecMeter.getIdgroup() == -1 && ecMeter.getIdconsumer() == -1
				? emgr.getWeeklyReport(nWeek)
				: emgr.getConsWeeklyReport(nWeek, ecMeter.getIdgroup(), ecMeter.getIdconsumer());
			record = report.getReportRecord(strReportTag);
			String strCurrentWeekKWh = record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh());
			DefaultCategoryDataset dsCurrentWeek = new DefaultCategoryDataset();
			us.getCurrentUserTransaction().setAttribute("cw" + timestamp, dsCurrentWeek);
			for(int i = 1; i <= report.getIntervalsNumber(); i++) {
				record = report.getReportRecord("d" + i + "." + strReportTag);
				dsCurrentWeek.addValue(record.getKwh(), record.getName(), astrDayNames[i]);
			}
					
			// previous week report
			report = ecMeter.getIdgroup() == -1 && ecMeter.getIdconsumer() == -1
				? emgr.getWeeklyReport(nWeek - 1)
				: emgr.getConsWeeklyReport(nWeek - 1, ecMeter.getIdgroup(), ecMeter.getIdconsumer());
			record = report.getReportRecord(strReportTag);
			String strPreviousWeekKWh = record.getKwh().equals(Float.NaN) ? "***" : EGUtils.formatkwh(record.getKwh());
			DefaultCategoryDataset dsPreviousWeek = new DefaultCategoryDataset();
			us.getCurrentUserTransaction().setAttribute("pw" + timestamp, dsPreviousWeek);
			for(int i = 1; i <= report.getIntervalsNumber(); i++) {
				record = report.getReportRecord("d" + i + "." + strReportTag);
				dsPreviousWeek.addValue(record.getKwh(), record.getName(), astrDayNames[i]);
			}
%>
<div class='tdfisa' style='text-align:left;'><%=lang.getString("energy", "active_energy")%> - <%=lang.getString("energy", "current_day")%></div>
<div class='lcd'>
<div class='lcdValue'><b><%=strDailyKWh%></b></div><div class='lcdMeasure'>[kWh]</div>
<div class='clr'></div>
</div>

<div class='tdfisa' style='text-align:left;'><%=lang.getString("energy", "active_energy")%> - <%=lang.getString("energy", "current_week")%></div>
<div class='lcd'>
<div class='lcdValue'><b><%=strCurrentWeekKWh%></b></div><div class='lcdMeasure'>[kWh]</div>
<div class='clr'></div>
</div>
<div class='lcd' style='text-align:center;'><img src="SRVLCharts;jsessionid=<%=us.getSessionId()%>?charttype=smallbarchart&width=290&height=80&imgid=cw<%=timestamp%>"></div>

<div class='tdfisa' style='text-align:left;'><%=lang.getString("energy", "active_energy")%> - <%=lang.getString("energy", "previous_week")%></div>
<div class='lcd'>
<div class='lcdValue'><b><%=strPreviousWeekKWh%></b></div><div class='lcdMeasure'>[kWh]</div>
</div>
<div class='lcd' style='text-align:center;'><img src="SRVLCharts;jsessionid=<%=us.getSessionId()%>?charttype=smallbarchart&width=290&height=80&imgid=pw<%=timestamp%>"></div>

<%} // if( emgr.getRunning() )
} // if( ecMeter != null )
%>
