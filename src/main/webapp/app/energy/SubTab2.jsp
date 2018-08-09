<%@ page language="java"%>
<%@page import="com.carel.supervisor.plugin.base.Plugin"%>
<%@page import="com.carel.supervisor.plugin.energy.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Map"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.carel.supervisor.presentation.session.UserSession"%>
<%@page import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%@page import="com.carel.supervisor.presentation.session.UserTransaction"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.jfree.data.category.DefaultCategoryDataset"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%@page import="java.util.Calendar"%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	UserTransaction transactionUser = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String avgmultilang = lan.getString("energy","avg");
	String totmultilang = lan.getString("energy","enertot");
	
	Integer ngrp = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMGROUPS, 10);
	Integer nunit = EnergyMgr.getInstance().getIntegerProperty(EnergyConfiguration.NUMCONSUMERS, 10);
	EnergyMgr emgr = EnergyMgr.getInstance();
	EnergyConfiguration econf = emgr.getSiteConfiguration();
	
	// Proporzione grafico
	int graphWidthDef = 880;
	int screenWidthDef = 1024;
	int screenWidth = sessionUser.getScreenWidth();
	int grpWidthDimCalc = (graphWidthDef*screenWidth)/screenWidthDef;
	int grpWidthDimCalcCake = grpWidthDimCalc; //(350*screenWidth)/screenWidthDef;
	
	Calendar calendar = new GregorianCalendar();
	calendar.setMinimalDaysInFirstWeek(4);
	calendar.setFirstDayOfWeek(Calendar.MONDAY);
	// Current Week
	int curWeek = calendar.get(Calendar.WEEK_OF_YEAR);
	// Current Month
	int curMonth = calendar.get(Calendar.MONTH);
	curMonth++;
	// Current Year
	int curYear = calendar.get(Calendar.YEAR);
	calendar.set(Calendar.HOUR_OF_DAY, 0);
	calendar.set(Calendar.MINUTE, 0);
	calendar.set(Calendar.SECOND, 0);
	calendar.set(Calendar.MILLISECOND, 0);
	
	String[] astrMonthNames = {
		lan.getString("cal", "january"),
		lan.getString("cal", "february"),
		lan.getString("cal", "march"),
		lan.getString("cal", "april"),
		lan.getString("cal", "may"),
		lan.getString("cal", "june"),
		lan.getString("cal", "july"),
		lan.getString("cal", "august"),
		lan.getString("cal", "september"),
		lan.getString("cal", "october"),
		lan.getString("cal", "november"),
		lan.getString("cal", "december")	
	};
	
	String[] astrDayNames = { "",
		lan.getString("cal", "sun"),
		lan.getString("cal", "mon"),
		lan.getString("cal", "tue"),
		lan.getString("cal", "wed"),
		lan.getString("cal", "thu"),
		lan.getString("cal", "fri"),
		lan.getString("cal", "sat")	
	};
	
	String month_slot = "";
	for(int i=0;i<12;i++){
		month_slot += astrMonthNames[i]+",";
	}

	SimpleDateFormat yfmt = new SimpleDateFormat("yyyy");
	int firstSample = 0 ;
	int lastSample = 0 ;
    try{
    	firstSample = Integer.parseInt(yfmt.format(econf.getFirstSample()));
    	lastSample = Integer.parseInt(yfmt.format(econf.getLastSample()));
    }catch (Exception e){
    	firstSample = lastSample = curYear;
    }
    StringBuffer yearOptions = new StringBuffer();
    for(int year = firstSample; year <= lastSample; year++) {
    	yearOptions.append("<option value='" + year + "'");
    	if( year == curYear )
    		yearOptions.append(" selected");
    	yearOptions.append(">" + year + "</option>\n");
    }
	
	
	// active configuration
	String strActiveCfg = EnergyMgr.getInstance().getStringProperty("active_cfg");
	boolean bTimeSlotEnabled = strActiveCfg.equals("time_slot");
%>
<input type="hidden" value="<%=sessionUser.getSessionId() %>" id="sessionid">
<input type="hidden" value="<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"%>" id="basepath">
<%	
int size = 0;
try {
	size = EnergyMgr.getInstance().getSiteConfiguration().getConsumerList(language).size();
}
catch (Exception e) {
	size = 0;
}
if(size == 0) {
%>
<input type="hidden" value="false" id="plgnotcnf">
<table border="0" width="100%" cellpadding="1" cellspacing="1">
	<tr>
		<td align="center"><%=lan.getString("energy","pgtobedone") %></td>
	</tr>
</table> 
<%} else {
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	EnergyReport report = emgr.getWeeklyReport(curWeek);
	transactionUser.setAttribute("report", report);
	Timestamp dbegin = report.getBegin();
	String sbegin = sdf.format(dbegin);
	Timestamp dend = report.getEnd();
	String send = sdf.format(dend);
	// ** header **
	transactionUser.setAttribute("root", "site");
	transactionUser.setAttribute("reporttype", report.getType());
	transactionUser.setAttribute("from", dbegin);
	transactionUser.setAttribute("to", dend);
	transactionUser.setAttribute("step", report.getStep());
	// ** header **
	DefaultPieDataset dataset = new DefaultPieDataset();
	Long timevalue = System.currentTimeMillis();
	transactionUser.setAttribute("timevalue", timevalue);
	for (int i = 1; i <= ngrp; i++) {
		EnergyReportRecord energyReportRecordGroup = report.getReportRecord("group" + i);
		if (energyReportRecordGroup == null){
			continue;
		}
		dataset.setValue(report.getReportRecord("group"+i).getName(), report.getReportRecord("group"+i).getKw());
	}
	transactionUser.setAttribute("st2pie"+timevalue, dataset);

	// timeslot % piechart
	DefaultPieDataset datasetTS = new DefaultPieDataset();
	EnergyReportRecord recordTS = report.getReportRecord("site");
	double anPercentKWhTS[] = recordTS.getKWhTSinPercent();
	double anPercentCostTS[] = recordTS.getCostTSinPercent();
	if( anPercentKWhTS != null )
		for(int i = 0; i < EnergyProfile.TIMESLOT_NO; i++)
			datasetTS.setValue(EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(i).getName(), anPercentKWhTS[i]);
	transactionUser.setAttribute("st2pieB"+timevalue, datasetTS);
%>
<input type="hidden" value="<%=sbegin%>" id="datafromgraph">
<input type="hidden" value="<%=send%>" id="datatograph">

<input type="hidden" value="<%=avgmultilang%>" id="avgmultilang">
<input type="hidden" value="<%=totmultilang%>" id="totmultilang">

<input type="hidden" value="<%=grpWidthDimCalcCake%>" id="imgwidthcake">
<input type="hidden" value="<%=grpWidthDimCalc%>" id="imgwidthbar">

<input type="hidden" value="true" id="plgnotcnf">
<input type="hidden" value="true" id="plgennotreg">

<input type="hidden" value="<%=lan.getString("energy","other") %>" id="egotherdesc" />
<input type="hidden" value="<%=lan.getString("energy","time_slot")%>" id="time_slot" />
<input type="hidden" id="TIMESLOT_NO" name="TIMESLOT_NO" value="<%=EnergyProfile.TIMESLOT_NO%>">
<input type="hidden" value="<%=month_slot%>" id="month_slot" />
<form method="post"
	action="servlet/master;jsessionid=<%=sessionUser.getSessionId()%>"
	name="energycommand" id="energycommand">
	<input type="hidden" value="" id="cmd" name="cmd">
	<input type="hidden" value="<%=timevalue%>" id="imgid" name="imgid">
</form>

<fieldset class="field"><legend class="standardTxt"><%=lan.getString("energy","energrpstandardperiod")%></legend>
<table id="tableQuery" class="table" width="100%">
	<tr>
		<td class="td" colspan="3"><%=lan.getString("energy", "energroup")%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<select id="selectedgroup"  style="width:25%;" onChange="energyselectgroups(this.value)">
				<option id="site" value="0"><%=lan.getString("energy", "enersite")%></option>
				<%if(econf.getGroups()!=null && econf.getGroups().size()!=0){
				for(Iterator<EnergyGroup> itr = econf.getGroups().iterator();itr.hasNext();){ 
					EnergyGroup eg=itr.next();
					String strGroup = "";
					if( !eg.isEnabled() || eg.getConsumers().isEmpty() )
						continue;
				%>
				<option id="group<%=eg.getId()%>" value="<%=eg.getId()%>"><%=eg.getName()%></option>
				<%}}%>
			</select>
			<%StringBuffer sbScript = new StringBuffer();
				sbScript.append("<script>var objGroups = { group0: [[0, '']]\n");
				if(econf.getGroups()!=null && econf.getGroups().size()!=0){
					for(Iterator<EnergyGroup> itr = econf.getGroups().iterator();itr.hasNext();){ 
						EnergyGroup eg=itr.next();
						String strGroup = "";
						if( !eg.isEnabled() )
							continue;
						else {
							strGroup = ", 'group" + eg.getId() + "': [[0, '']";							
							for(Iterator<EnergyConsumer> itCons = eg.getConsumers().iterator();itCons.hasNext();){
								EnergyConsumer ec = itCons.next();
								strGroup += ", [";
								strGroup += ec.getIdconsumer();
								strGroup += ", '";
								strGroup += ec.getName();
								strGroup += "']";
							}
							strGroup += "]";
						}
						sbScript.append(strGroup);
						sbScript.append("\n");
					}
				}
				sbScript.append("} </script>");
			%>
			<%=sbScript.toString()%>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=lan.getString("energy", "enerconfigurationdevice")%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;			
			<select id="selectedcons" style="width:25%;">
				<option value="0"></option>
			</select>
		</td>		
	</tr>
	<tr style="height:10px;"><td colspan="3"></td></tr>
	<tr>
		<td class="th" width="33%"><%=lan.getString("energy","energrpstandardweeklyreport")%></td>
		<td class="th" width="34%"><%=lan.getString("energy","energrpstandardmonthlyreport")%></td>
		<td class="th" width="33%"><%=lan.getString("energy","energrpstandardyearlyreport")%></td>
	</tr>
	<tr class="Row1">
		<td class="td">
			<select name="week" id="week">
				<%for(int iWeek = 1; iWeek <= 52; iWeek++) {
					calendar.set(Calendar.WEEK_OF_YEAR, iWeek);
					calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					String strWeek = calendar.get(Calendar.DAY_OF_MONTH)
						+ " " + astrMonthNames[calendar.get(Calendar.MONTH)];
					calendar.add(Calendar.DAY_OF_YEAR, 6);
					strWeek += " - " + calendar.get(Calendar.DAY_OF_MONTH)
						+ " " + astrMonthNames[calendar.get(Calendar.MONTH)];
				%>
				<option value="<%=iWeek%>" <%=((iWeek==curWeek)?"selected":"") %>><%=strWeek%></option>
				<%}%>
			</select >
			&nbsp;
			<select id="w_year" name="w_year" onChange="changeYear(this.value)"><%=yearOptions.toString()%></select>
			&nbsp;
			<img src="images/actions/calculate_black.png" style="cursor: pointer" onclick="getReport('weekly','day','w_year',document.getElementById('week').value);"/>
		</td>
		<td class="td">
			<select name="month" id="month">
				<option value="1" <%=((curMonth==1)?"selected":"")%>><%=lan.getString("cal", "january")%></option>
				<option value="2" <%=((curMonth==2)?"selected":"")%>><%=lan.getString("cal", "february")%></option>
				<option value="3" <%=((curMonth==3)?"selected":"")%>><%=lan.getString("cal", "march")%></option>
				<option value="4" <%=((curMonth==4)?"selected":"")%>><%=lan.getString("cal", "april")%></option>
				<option value="5" <%=((curMonth==5)?"selected":"")%>><%=lan.getString("cal", "may")%></option>
				<option value="6" <%=((curMonth==6)?"selected":"")%>><%=lan.getString("cal", "june")%></option>
				<option value="7" <%=((curMonth==7)?"selected":"")%>><%=lan.getString("cal", "july")%></option>
				<option value="8" <%=((curMonth==8)?"selected":"")%>><%=lan.getString("cal", "august")%></option>
				<option value="9" <%=((curMonth==9)?"selected":"")%>><%=lan.getString("cal", "september")%></option>
				<option value="10" <%=((curMonth==10)?"selected":"")%>><%=lan.getString("cal", "october")%></option>
				<option value="11" <%=((curMonth==11)?"selected":"")%>><%=lan.getString("cal", "november")%></option>
				<option value="12" <%=((curMonth==12)?"selected":"")%>><%=lan.getString("cal", "december")%></option>
			</select>
			&nbsp;
			<select id="m_year" name="m_year"><%=yearOptions.toString()%></select>
			&nbsp;
			<img src="images/actions/calculate_black.png" style="cursor: pointer" onclick="getReport('monthly','day','m_year',document.getElementById('month').value);"/>
		</td>
		<td class="td">
			<select id="year" name="year"><%=yearOptions.toString()%></select>
			&nbsp;
			<img src="images/actions/calculate_black.png" style="cursor: pointer" onclick="getReport('yearly','month','',document.getElementById('year').value);"/>
		</td>
	</tr>
</table>
</fieldset>

<br><br><br>

<table style="width: 100%; border: 0px;" cellpadding="0" cellspacing="0" align="center">
	<tr>
		<td>
<!-- tabs -->
			<table id="table_tabs" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="25px">&nbsp;</td>
					<td id="tab_group" class="egtabselected2" style="width: 60px;text-align: center;" onmouseover="energtabyover(this)" onmouseout="energytabout(this)" onclick="switchTab(this)">
						<%=lan.getString("energy","energroup")%>
					</td>
					<td>&nbsp;</td>
					<td id="tab_kw" class="egtabnotselected2" style="width: 60px;text-align: center;" onmouseover="energtabyover(this)" onmouseout="energytabout(this)" onclick="switchTab(this)">
						<%=lan.getString("energy","enerkw") %>
					</td>
					<td>&nbsp;</td>
					<td id="tab_kwh" class="egtabnotselected2" style="width: 60px; text-align: center;" onmouseover="energtabyover(this)" onmouseout="energytabout(this)" onclick="switchTab(this)">
						<%=lan.getString("energy","enerkwh") %>
					</td>
					<%if(bTimeSlotEnabled) {%>
					<td>&nbsp;</td>
					<td id="tab_timeslot" class="egtabnotselected2" style="text-align: center; white-space: nowrap;" onmouseover="energtabyover(this)" onmouseout="energytabout(this)" onclick="switchTab(this)">
						<%=lan.getString("energy","time_slot")%>
					</td>
					<td>&nbsp;</td>
					<td id="tab_kwh_ts" class="egtabnotselected2" style="text-align: center; white-space: nowrap;" onmouseover="energtabyover(this)" onmouseout="energytabout(this)" onclick="switchTab(this)">
						<%=lan.getString("energy","kwh_ts") %>
					</td>
					<td>&nbsp;</td>
					<td id="tab_ts_kwh" class="egtabnotselected2" style="text-align: center; white-space: nowrap;" onmouseover="energtabyover(this)" onmouseout="energytabout(this)" onclick="switchTab(this)">
						<%=lan.getString("energy","ts_kwh") %>
					</td>
					<%}%>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0" width="99%" style="border: 3px solid #000000" align="center">
				<tr>
					<td>
<!-- standard reports -->					
					<table id="energy_reportpercent_1" cellspacing="0" cellpadding="0" style="width: 100%; height: 320px;  border: 0px solid #000000;" style="visibility: visible;display: block;">
						<tr class="th" style="height: 30px; font-size: 12px; font-weight: normal; text-align: center">
							<td>
								<%=lan.getString("energy","energrpconsumptionperc")%>
							</td>
						</tr>
						<tr>
							<td style="text-align: center;vertical-align: middle;padding: 3px;">
								<img id="imgCake" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?charttype=piechart&width=<%=grpWidthDimCalcCake %>&height=300&imgid=st2pie<%=timevalue%>" />
							</td>
						</tr>
						<tr>
							<td align="center">
								<div style="width:<%=grpWidthDimCalc %>px;overflow:auto;">
								<table width="100%" cellpadding="1" cellspacing="1" style="border: 0px solid #000000;" id="tableGroups">
									<tr><td height="0px;"></td></tr>
									<tr class="Row1">
										<td class="th" style="height: 30px; font-size: 10px; font-weight: normal;" align="center">
											<%=lan.getString("energy","energroup") %>
										</td>
										<td style="height: 30px; font-size: 10px; font-weight: normal;" class="th" align="center">
											%
										</td>
										<td class="th" align="center" style="height: 30px; font-size: 10px; font-weight: normal;">
											<%=lan.getString("energy","enerkw") %>
										</td>
										<td class="th" align="center" style="height: 30px; font-size: 10px; font-weight: normal;">
											<%=lan.getString("energy","enerkwh") %>
										</td>
										<td class="th" align="center" style="height: 30px; font-size: 10px; font-weight: normal;">
											<%=lan.getString("energy","enerkgco2") %>
										</td>
										<td class="th" align="center" style="height: 30px; font-size: 10px; font-weight: normal;">
											<%=lan.getString("energy","enercost") %>
										</td>
									</tr>
								</table>
								</div>
							</td>
						</tr>
					</table>
					
					<table id="energy_reportdetail_1" border="0" width="100%" cellpadding="0" cellspacing="0" style="visibility: visible;display: none;">
						<tr class="th" style="height: 30px; font-size: 12px; font-weight: normal; text-align: center">
							<td>
								<%=lan.getString("energy","energrpconsumptiondetailkw")%>
							</td>
						</tr>
						<tr>
							<td style="text-align: center;vertical-align: middle;padding: 3px;">
								<img id="imgLine" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?dtf=<%=sbegin%>&dtt=<%=send%>&charttype=line&width=<%=grpWidthDimCalc%>&height=300&imgid=st2barA" />
							</td>
						</tr>
						<tr>
							<td style="padding: 3px;" align="center">
								<div id="containerScrollKw" style="width:<%=grpWidthDimCalc %>px;overflow:auto;">
									<table width="100%" align="center" border="0" cellspacing="1" id="tablekw">
									<tr class="Row1">
										<td class="th"
											style="height: 30px; font-size: 10px; font-weight: normal;"
											align="center" width="15%">
											<%=lan.getString("energy","energroup") %>
										</td>
										<%for(Integer numcols = 1;numcols<=report.getIntervalsNumber();numcols++){%>
											<td class="th"
												style="height: 30px; font-size: 10px; font-weight: normal;"
												align="center">
												<%=numcols %>
											</td>
										<%}%>
										<td class="th"
											style="height: 30px; font-size: 10px; font-weight: normal;"
											align="center">
											<%=avgmultilang %>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
				
				<table id="energy_reportdetail_2" border="0" width="100%" cellpadding="0" cellspacing="0" style="visibility: visible;display: none;">
					<tr class="th" style="height: 30px; font-size: 12px; font-weight: normal; text-align: center">
						<td>
							<%=lan.getString("energy","energrpconsumptiodetailkwh") %>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;vertical-align: middle;padding: 3px;">
							<img id="imgBar" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?dtf=<%=sbegin%>&dtt=<%=send%>&charttype=barchart&width=<%=grpWidthDimCalc%>&height=300&imgid=st2barB" />
						</td>
					</tr>
					<tr>
						<td style="padding: 3px;" align="center">
							<div id="containerScrollKwh" style="width:<%=grpWidthDimCalc %>px;overflow:auto;">
							<table width="100%" align="center" border="0" cellspacing="1" id="tablekwh">
								<tr class="Row1">
									<td class="th"
										style="height: 30px; font-size: 10px; font-weight: normal;"
										align="center" width="15%">
										<%=lan.getString("energy","energroup") %>
									</td>
									<%for(Integer numcols = 1;numcols<=report.getIntervalsNumber();numcols++){%>
										<td class="th"
											style="height: 30px; font-size: 10px; font-weight: normal;"
											align="center">
											<%=numcols %>
										</td>
									<%}%>
									<td class="th"
										style="height: 30px; font-size: 10px; font-weight: normal;"
										align="center">
										<%=totmultilang %>
									</td>
								</tr>
							</table>
							</div>
						</td>
					</tr>
				</table>
<!-- timeslot reports -->
				<table id="energy_reportpercent_2" cellspacing="0" cellpadding="0" style="width: 100%; height: 320px;  border: 0px solid #000000;visibility: hidden;display: none;">
					<tr class="th" style="height: 30px; font-size: 12px; font-weight: normal; text-align: center">
						<td>
							<%=lan.getString("energy","energrpconsumptionperc") %>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;vertical-align: middle;padding: 3px;">
							<img id="imgCakeB" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?charttype=piechart_timeslot_color&width=<%=grpWidthDimCalcCake %>&height=300&imgid=st2pieB<%=timevalue%>" />
						</td>
					</tr>
					<tr>
						<td align="center" valign="top" >
							<div style="width:<%=grpWidthDimCalc %>px;overflow:auto;">
							<table width="100%" cellpadding="1" cellspacing="1" style="border: 0px solid #000000;" id="table_ts_perc">
								<tr><td height="0px;"></td></tr>
								<tr class="Row1">
									<td class="th" style="height: 30px; font-size: 10px; font-weight: normal;" align="center">
										<%=lan.getString("energy","time_slot")%>
									</td>
									<td style="height: 30px; font-size: 10px; font-weight: normal;" class="th" align="center">
										<%=lan.getString("energy","enerkwh")%> %
									</td>
									<td class="th" align="center" style="height: 30px; font-size: 10px; font-weight: normal;">
										<%=lan.getString("energy","enercost")%> %
									</td>
								</tr>
								<%for(Integer iTS = 0; iTS < EnergyProfile.TIMESLOT_NO; iTS++){%>
								<tr class="Row1">
									<td class="standardTxt" style="height: 21px" align="left">
									<%=EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(iTS).getName()%>
									</td>
									<td class="standardTxt" style="height: 21px" align="center">***</td>
									<td class="standardTxt" style="height: 21px" align="center">***</td>
								<%}%>
							</table>
							</div>
						</td>
					</tr>
				</table>

				<table id="energy_reportdetail_3" border="0" width="100%" cellpadding="0" cellspacing="0" style="visibility: visible;display: none;">
					<tr class="th" style="height: 30px; font-size: 12px; font-weight: normal; text-align: center">
						<td>
							<%=lan.getString("energy","kwh_ts_detail") %>
						</td>
					<tr>
					<td style="text-align: center;vertical-align: middle;padding: 3px;">
						<img id="imgBarC" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?dtf=<%=sbegin%>&dtt=<%=send%>&charttype=barchart&width=<%=grpWidthDimCalc%>&height=300&imgid=st2barC" />
					</td>
					</tr>
					<tr>
						<td style="padding: 3px;" align="center">
						<div id="containerScrollKwhTS" style="width:<%=grpWidthDimCalc %>px;overflow:auto;">
						<table width="100%" align="center" border="0" cellspacing="1" id="table_kwh_ts">
							<tr class="Row1">
								<td class="th" style="height: 30px; font-size: 10px; font-weight: normal;" align="center" width="15%">
									<%=lan.getString("energy","energroup") %>
								</td>
								<%for(Integer iTS = 0; iTS < EnergyProfile.TIMESLOT_NO; iTS++){%>
									<td class="th" style="height: 30px; font-size: 10px; font-weight: normal;" align="center">
										<%=EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(iTS).getName()%>
									</td>
								<%}%>
								<td class="th" style="height: 30px; font-size: 10px; font-weight: normal;" align="center">
									<%=totmultilang %>
								</td>
							</tr>
							<tr class="Row1"><td class="standardTxt" style="height: 20px" align="left"><%=lan.getString("energy","enerkwh")%></td>
							<%for(int iTS = 0; iTS <= EnergyProfile.TIMESLOT_NO; iTS++){%>
							<td class="standardTxt" style="height: 20px" align="center">***</td>
							<%}%>
							</tr>
							<tr class="Row1"><td class="standardTxt" style="height: 20px" align="left"><%=lan.getString("energy","enercost")%></td>
							<%for(int iTS = 0; iTS <= EnergyProfile.TIMESLOT_NO; iTS++){%>
							<td class="standardTxt" style="height: 20px" align="center">***</td>
							<%}%>
							</tr>
						</table>
						</div>
					</td>
				</tr>
				</table>

				<table id="energy_reportdetail_4" border="0" width="100%" cellpadding="0" cellspacing="0" style="visibility: visible;display: none;">
					<tr class="th" style="height: 30px; font-size: 12px; font-weight: normal; text-align: center">
						<td>
							<%=lan.getString("energy","ts_kwh_detail") %>
						</td>
					</tr>
					<tr>
						<td style="text-align: center;vertical-align: middle;padding: 3px;">
							<img id="imgBarD" src="SRVLCharts;jsessionid=<%=sessionUser.getSessionId()%>?dtf=<%=sbegin%>&dtt=<%=send%>&charttype=barchart_timeslot_color&width=<%=grpWidthDimCalc%>&height=300&imgid=st2barD" />
						</td>
					</tr>
					<tr>
						<td style="padding: 3px;" align="center">
							<div id="containerScrollTSKwh" style="width:<%=grpWidthDimCalc%>px;overflow:auto;">
							<table width="100%" align="center" border="0" cellspacing="1" id="table_ts_kwh">
							<tr class="Row1">
								<td class="th" style="height: 30px; font-size: 10px; font-weight: normal;" align="center" width="15%">
									<%=lan.getString("energy","time_slot")%>
								</td>
								<%for(Integer numcols = 1;numcols<=report.getIntervalsNumber();numcols++){%>
									<td class="th" style="height: 30px; font-size: 10px; font-weight: normal;" align="center"><%=numcols%></td>
								<%}%>
								<td class="th" style="height: 30px; font-size: 10px; font-weight: normal;" align="center"><%=totmultilang%></td>
							</tr>
							<%for(int iTS = 0; iTS < EnergyProfile.TIMESLOT_NO; iTS++){%>
							<tr class="Row1">
								<td class="standardTxt" style="height: 20px" align="left">
								<%=EnergyMgr.getInstance().getEnergyProfile().getTimeSlot(iTS).getName()%>
								</td>
								
								<%for(Integer numcols=1;numcols<=report.getIntervalsNumber();numcols++){%>
								<td class="standardTxt" style="height: 20px" align="center">***</td>
								<%}%>
								<td class="standardTxt" style="height: 20px" align="center">***</td>
							</tr>
							<%}//for iTS%>
							</table>
							</div>
						</td>
					</tr>
				</table>
				
				</td>
			</tr>
			</table>
		</td>
	</tr>
</table>
<%}%>