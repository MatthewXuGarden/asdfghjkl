<%@page import="com.carel.supervisor.presentation.session.UserTransaction"%>
<%@page import="com.carel.supervisor.presentation.helper.VirtualKeyboard"%>
<%@page import="com.carel.supervisor.dataaccess.db.DatabaseMgr"%>
<%@page import="com.carel.supervisor.presentation.session.UserSession"%>
<%@page import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%@page import="com.carel.supervisor.dataaccess.db.RecordSet"%>
<%@page import="com.carel.supervisor.plugin.energy.*"%>
<%@page import="com.carel.supervisor.plugin.base.Plugin"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="com.carel.supervisor.plugin.energy.*"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%@page import="com.carel.supervisor.base.config.BaseConfig"%>
<%@page import="com.carel.supervisor.presentation.bean.FileDialogBean"%>


<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
Boolean energyrunning = EnergyMgr.getInstance().getStartedplugin();
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);

//multilingua per calendario
String dom = lang.getString("cal","sun");
String lun = lang.getString("cal","mon");
String ma = lang.getString("cal","tue");
String mer = lang.getString("cal","wed");
String gio = lang.getString("cal","thu");
String ven = lang.getString("cal","fri");
String sab = lang.getString("cal","sat");
String gen = lang.getString("cal","january");
String feb = lang.getString("cal","february");
String mar = lang.getString("cal","march");
String apr = lang.getString("cal","april");
String mag = lang.getString("cal","may");
String giu = lang.getString("cal","june");
String lug = lang.getString("cal","july");
String ago = lang.getString("cal","august");
String set = lang.getString("cal","september");
String ott = lang.getString("cal","october");
String nov = lang.getString("cal","november");
String dic = lang.getString("cal","december");

String jsession = request.getSession().getId();
boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();	

EnergyMgr mgr = EnergyMgr.getInstance();
EnergyConfiguration econf = mgr.getSiteConfiguration();
EnergyProfile ep = mgr.getEnergyProfile();

//alert
String strAlert = sessionUser.getProperty("impexp_alert");
if( strAlert != null )
	sessionUser.removeProperty("impexp_alert");
else
	strAlert = "";

// subtab selection
UserTransaction ut = sessionUser.getCurrentUserTransaction(); 
String selLayer = ut.remProperty("selLayer");
String iTimeTable = ut.remProperty("iTimeTable");

// active configuration
String strActiveCfg = EnergyMgr.getInstance().getStringProperty("active_cfg");
String chkFixedCost = strActiveCfg.equals("fixed_cost") ? "checked" : "";
String chkTimeSlot = strActiveCfg.equals("time_slot") ? "checked" : "";

boolean bTabEnabled = strActiveCfg.equals("time_slot");
%>
<input type="hidden" id="remoteFolder" name="remoteFolder" value="<%=BaseConfig.getCarelPath() + "PvPro\\TempExports\\"%>">
<input type="hidden" id="TIMESLOT_NO" name="TIMESLOT_NO" value="<%=EnergyProfile.TIMESLOT_NO%>">
<input type="hidden" id="TIMEPERIOD_NO" name="TIMEPERIOD_NO" value="<%=EnergyProfile.TIMEPERIOD_NO%>">
<input type="hidden" id="alert" name="alert" value="<%=strAlert%>">
<input type="hidden" id="alert_cost" name="alert_cost" value="<%=lang.getString("energy","alert_cost")%>">
<input type="hidden" id="alert_period" name="alert_period" value="<%=lang.getString("energy","alert_period")%>">
<input type="hidden" id="active_cfg" name="active_cfg" value="<%=strActiveCfg%>">
<input type="hidden" id="fc2ts" name="fc2ts" value='<%=lang.getString("energy","fc2ts")%>'>
<input type="hidden" id="ts2fc" name="ts2fc" value='<%=lang.getString("energy","ts2fc")%>'>

<%=(new FileDialogBean(request)).renderFileDialog()%>
<div id="uploadwin" class="uploadWin">
	<div id="uploadwinheader" class="uploadWinHeader">
		<div class="uploadWinClose" onclick="document.getElementById('uploadwin').style.display='none';">X</div>
		<%=lang.getString("energy", "icon5tooltip")%>
	</div>
	<div id="uploadwinbody" class="uploadWinBody">
		<div>
		  <form id="frm_upload_xml" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='multipart/form-data'>
            <input type="hidden" name="tipofile" value="EnergyProfile" />
			<div id="uploadwinotherpath">
				<div id="oplabel" style="width:100px;"><span id="otherasDIV"><%=lang.getString("filedialog", "filename")%></span></div>
				<div id="oploadbox"><span id="other_span_load"><input type="file" class="mybutton" id="upload_file" name="upload_file" size='30%'></span></div>
				<div id="opsavebox"><span id="other_span_save"></span></div>		
			</div>
			<div id="uploadwinbuttons">
				<table border="0">
					<tr>
						<td class="groupCategory_small" style="width:110px;height:30px;" onclick="submit_file();"><%=lang.getString("filedialog", "ok")%></td>
						<td class="groupCategory_small" style="width:110px;height:30px;" onclick="document.getElementById('uploadwin').style.display='none'"><%=lang.getString("filedialog", "cancel")%></td>
					</tr>
				</table>
			</div>
		  </form>
		</div>	
	</div>
</div>
<form id="frm_energy_impexp" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd">
<input type="hidden" id="impexp" name="impexp">
<input type="hidden" id="xml" name="xml">
<input type="hidden" id="selLayer" name="selLayer" value="<%=selLayer%>">
<input type="hidden" id="iTimeTable" name="iTimeTable" value="<%=iTimeTable%>">
<fieldset class="field">
<legend class="standardTxt"><%=lang.getString("energy","configuration")%></legend>
<table id="tabActiveCfg" border="0" width="100%" align="center" cellspacing="1" cellpadding="0">
	<tr valign="middle">
		<td class="standardTxt" colspan="3" align="right"><%=lang.getString("energy","active_cfg")%></td>
		<td class="standardTxt" colspan="3">
			&nbsp;&nbsp;
			<input type="radio" name="active_cfg" value="fixed_cost" onClick="onActiveConfiguration(this.value)" <%=chkFixedCost%>>&nbsp;<%=lang.getString("energy","fixed_cost_cfg")%>
			&nbsp;&nbsp;
			<input type="radio" name="active_cfg" value="time_slot" onClick="onActiveConfiguration(this.value)" <%=chkTimeSlot%>>&nbsp;<%=lang.getString("energy","time_slot_cfg")%>
		</td>
	</tr>
	<tr><td colspan="6">&nbsp;</td></tr>
	<tr valign="middle">
		<td style="visibility:<%=bTabEnabled ? "hidden" : "visible"%>;" class="standardTxt" align="right"><%=lang.getString("energy","enerconfigurationcostperkw")%></td>
		<td style="visibility:<%=bTabEnabled ? "hidden" : "visible"%>;" class="standardTxt">&nbsp;
			<input id="valCost" name="kwcost" size="10" value='<%=econf.getSiteProperty("cost","")%>'
				<%=(bOnScreenKey?"class='keyboardInput'":"class='standardTxt'")%>
				onKeyPress="return filterInput(1, event, true);">
		</td>
		<td class="standardTxt" align="right"><%=lang.getString("energy","enerconfigurationcurrency")%></td>
		<td>&nbsp;
			<input id="valValue" name="currency" size="4" value='<%=econf.getSiteProperty("currency","")%>' 
				<%=(bOnScreenKey?"class='keyboardInput'":"class='standardTxt'")%>>
		</td>
		<td class="standardTxt" align="right"><%=lang.getString("energy","kgco2perkw")%></td>
		<td>&nbsp;
			<input id="valkgco2" name="kgco2" size="10" value='<%=econf.getSiteProperty("kgco2","")%>' 
				<%=(bOnScreenKey?"class='keyboardInput'":"class='standardTxt'")%>
				onKeyPress="return filterInput(1, event, true);">
		</td>
	</tr>
	<tr><td colspan="6">&nbsp;</td></tr>
	<tr>
		<td class="standardTxt" align="right"><%=lang.getString("energy","report_header")%></td>
		<td>&nbsp;
			<input id="header" name="header" size="40" maxlenght="64" value='<%=econf.getSiteProperty("header","")%>' <%=(bOnScreenKey?"class='keyboardInput'":"class='standardTxt'")%>>
		</td>
		<td colspan="2">&nbsp;</td>
		<td class="standardTxt" align="right"><%=lang.getString("energy","report_footer")%></td>
		<td>&nbsp;
			<input id="footer" name="footer" size="40" maxlength="64" value='<%=econf.getSiteProperty("footer","")%>' <%=(bOnScreenKey?"class='keyboardInput'":"class='standardTxt'")%>>
		</td>
	</tr>		
</table>
</fieldset>
</form>

<br>

<script>
var aColors = new Array('<%=ep.getTimeSlot(0).getColor()%>', '<%=ep.getTimeSlot(1).getColor()%>', '<%=ep.getTimeSlot(2).getColor()%>', '<%=ep.getTimeSlot(3).getColor()%>','<%=ep.getTimeSlot(4).getColor()%>', '<%=ep.getTimeSlot(5).getColor()%>', '<%=ep.getTimeSlot(6).getColor()%>', '<%=ep.getTimeSlot(7).getColor()%>');
<%=ep.getTimePeriodsJS()%>
</script>

<div id="divTimeslotCfg" style="display:<%=bTabEnabled ? "block" : "none"%>;">
<table id="tableMenu" width="99%">
<tr bgcolor="lightgray" style="cursor:hand;">
<th onClick="onSelectLayer('time_slot')" class='egtabselected2'><%=lang.getString("energy","time_slot")%></th>
<th onClick="onSelectLayer('time_table', 0)" class='egtabnotselected2'><%=lang.getString("energy","table_01")%></th>
<th onClick="onSelectLayer('time_table', 1)" class='egtabnotselected2'><%=lang.getString("energy","table_02")%></th>
<th onClick="onSelectLayer('time_table', 2)" class='egtabnotselected2'><%=lang.getString("energy","table_03")%></th>
<th onClick="onSelectLayer('time_table', 3)" class='egtabnotselected2'><%=lang.getString("energy","table_04")%></th>
<th onClick="onSelectLayer('time_table', 4)" class='egtabnotselected2'><%=lang.getString("energy","table_05")%></th>
<th onClick="onSelectLayer('time_table', 5)" class='egtabnotselected2'><%=lang.getString("energy","table_06")%></th>
<th onClick="onSelectLayer('time_table', 6)" class='egtabnotselected2'><%=lang.getString("energy","table_07")%></th>
<th onClick="onSelectLayer('time_table', 7)" class='egtabnotselected2'><%=lang.getString("energy","table_08")%></th>
<th onClick="onSelectLayer('time_table', 8)" class='egtabnotselected2'><%=lang.getString("energy","table_09")%></th>
<th onClick="onSelectLayer('time_table', 9)" class='egtabnotselected2'><%=lang.getString("energy","table_10")%></th>
<th onClick="onSelectLayer('time_table', 10)" class='egtabnotselected2'><%=lang.getString("energy","table_11")%></th>
<th onClick="onSelectLayer('time_table', 11)" class='egtabnotselected2'><%=lang.getString("energy","table_12")%></th>
<th onClick="onSelectLayer('time_table', 12)" class='egtabnotselected2'><%=lang.getString("energy","table_ex")%></th>
<th onClick="onSelectLayer('exception_days')" class='egtabnotselected2'><%=lang.getString("energy","ex_days")%></th>
</table>
<br><br>

<div id="time_slot" style="display:block;visibility:show;">
<table width="99%"><tr><td align="center">
<table id="tableTimeSlotDefs" border="1" cellspacing="0" class="standardTxt">
<tr class="th" height="25"><th>&nbsp;&nbsp;<%=lang.getString("energy","time_slot")%>&nbsp;&nbsp;</th><th><%=lang.getString("energy","kwh_cost")%></th><th>&nbsp;&nbsp;<%=lang.getString("energy","color")%>&nbsp;&nbsp;</th></tr>
<tr><th><%=ep.getTimeSlot(0).getName()%></th><td><input id="ts0cost" type="text" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>" value="<%=ep.getTimeSlot(0).getCost()%>"></td><td bgcolor="<%=ep.getTimeSlot(0).getColor()%>">&nbsp;</td>
<tr><th><%=ep.getTimeSlot(1).getName()%></th><td><input id="ts1cost" type="text" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>" value="<%=ep.getTimeSlot(1).getCost()%>"></td><td bgcolor="<%=ep.getTimeSlot(1).getColor()%>">&nbsp;</td>
<tr><th><%=ep.getTimeSlot(2).getName()%></th><td><input id="ts2cost" type="text" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>" value="<%=ep.getTimeSlot(2).getCost()%>"></td><td bgcolor="<%=ep.getTimeSlot(2).getColor()%>">&nbsp;</td>
<tr><th><%=ep.getTimeSlot(3).getName()%></th><td><input id="ts3cost" type="text" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>" value="<%=ep.getTimeSlot(3).getCost()%>"></td><td bgcolor="<%=ep.getTimeSlot(3).getColor()%>">&nbsp;</td>
<tr><th><%=ep.getTimeSlot(4).getName()%></th><td><input id="ts4cost" type="text" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>" value="<%=ep.getTimeSlot(4).getCost()%>"></td><td bgcolor="<%=ep.getTimeSlot(4).getColor()%>">&nbsp;</td>
<tr><th><%=ep.getTimeSlot(5).getName()%></th><td><input id="ts5cost" type="text" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>" value="<%=ep.getTimeSlot(5).getCost()%>"></td><td bgcolor="<%=ep.getTimeSlot(5).getColor()%>">&nbsp;</td>
<tr><th><%=ep.getTimeSlot(6).getName()%></th><td><input id="ts6cost" type="text" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>" value="<%=ep.getTimeSlot(6).getCost()%>"></td><td bgcolor="<%=ep.getTimeSlot(6).getColor()%>">&nbsp;</td>
<tr><th><%=ep.getTimeSlot(7).getName()%></th><td><input id="ts7cost" type="text" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>" value="<%=ep.getTimeSlot(7).getCost()%>"></td><td bgcolor="<%=ep.getTimeSlot(7).getColor()%>">&nbsp;</td>
</table> 
</td></tr></table>
</div>

<div id="time_table" style="display:none;visibility:hidden;">
<div id="time_period" style="display:none;visibility:hidden;">
<table class="standardTxt" width="20%">
<tr><th colspan="6" align="left"><%=lang.getString("energy","time_period")%></th></tr>
<tr><td><%=lang.getString("energy","date_from")%></td>
<td><%=lang.getString("energy","day")%></td>
<td><select id="dayBegin" onChange="onDayBeginChanged(this.value)">
<option value="1">01</option>
<option value="2">02</option>
<option value="3">03</option>
<option value="4">04</option>
<option value="5">05</option>
<option value="6">06</option>
<option value="7">07</option>
<option value="8">08</option>
<option value="9">09</option>
<option value="10">10</option>
<option value="11">11</option>
<option value="12">12</option>
<option value="13">13</option>
<option value="14">14</option>
<option value="15">15</option>
<option value="16">16</option>
<option value="17">17</option>
<option value="18">18</option>
<option value="19">19</option>
<option value="20">20</option>
<option value="21">21</option>
<option value="22">22</option>
<option value="23">23</option>
<option value="24">24</option>
<option value="25">25</option>
<option value="26">26</option>
<option value="27">27</option>
<option value="28">28</option>
<option value="29">29</option>
<option value="30">30</option>
<option value="31">31</option>
</select>
</td>
<td align="center">/</td>
<td><%=lang.getString("energy","month")%></td>
<td><select id="monthBegin" onChange="onMonthBeginChanged(this.value)">
<option value="0"><%=lang.getString("cal", "january")%></option>
<option value="1"><%=lang.getString("cal", "february")%></option>
<option value="2"><%=lang.getString("cal", "march")%></option>
<option value="3"><%=lang.getString("cal", "april")%></option>
<option value="4"><%=lang.getString("cal", "may")%></option>
<option value="5"><%=lang.getString("cal", "june")%></option>
<option value="6"><%=lang.getString("cal", "july")%></option>
<option value="7"><%=lang.getString("cal", "august")%></option>
<option value="8"><%=lang.getString("cal", "september")%></option>
<option value="9"><%=lang.getString("cal", "october")%></option>
<option value="10"><%=lang.getString("cal", "november")%></option>
<option value="11"><%=lang.getString("cal", "december")%></option>
</select>
</td>
</tr>
<tr><td><%=lang.getString("energy","date_to")%></td>
<td><%=lang.getString("energy","day")%></td>
<td><select id="dayEnd" onChange="onDayEndChanged(this.value)">
<option value="1">01</option>
<option value="2">02</option>
<option value="3">03</option>
<option value="4">04</option>
<option value="5">05</option>
<option value="6">06</option>
<option value="7">07</option>
<option value="8">08</option>
<option value="9">09</option>
<option value="10">10</option>
<option value="11">11</option>
<option value="12">12</option>
<option value="13">13</option>
<option value="14">14</option>
<option value="15">15</option>
<option value="16">16</option>
<option value="17">17</option>
<option value="18">18</option>
<option value="19">19</option>
<option value="20">20</option>
<option value="21">21</option>
<option value="22">22</option>
<option value="23">23</option>
<option value="24">24</option>
<option value="25">25</option>
<option value="26">26</option>
<option value="27">27</option>
<option value="28">28</option>
<option value="29">29</option>
<option value="30">30</option>
<option value="31">31</option>
</select>
</td>
<td align="center">/</td>
<td><%=lang.getString("energy","month")%></td>
<td><select id="monthEnd" onChange="onMonthEndChanged(this.value)">
<option value="0"><%=lang.getString("cal", "january")%></option>
<option value="1"><%=lang.getString("cal", "february")%></option>
<option value="2"><%=lang.getString("cal", "march")%></option>
<option value="3"><%=lang.getString("cal", "april")%></option>
<option value="4"><%=lang.getString("cal", "may")%></option>
<option value="5"><%=lang.getString("cal", "june")%></option>
<option value="6"><%=lang.getString("cal", "july")%></option>
<option value="7"><%=lang.getString("cal", "august")%></option>
<option value="8"><%=lang.getString("cal", "september")%></option>
<option value="9"><%=lang.getString("cal", "october")%></option>
<option value="10"><%=lang.getString("cal", "november")%></option>
<option value="11"><%=lang.getString("cal", "december")%></option>
</select>
</td>
</tr>
</table> 
<br>
</div>

<table id="tableTimeSlots" width="99%" border="1" cellspacing="0" class="standardTxt">
<tr class="th" height="25">
<th><%=lang.getString("energy", "day_hour")%></th>
<th>00:00</th><th>01:00</th><th>02:00</th><th>03:00</th><th>04:00</th><th>05:00</th><th>06:00</th><th>07:00</th><th>08:00</th><th>09:00</th><th>10:00</th><th>11:00</th>
<th>12:00</th><th>13:00</th><th>14:00</th><th>15:00</th><th>16:00</th><th>17:00</th><th>18:00</th><th>19:00</th><th>20:00</th><th>21:00</th><th>22:00</th><th>23:00</th>
<td></td><td align="center"><%=lang.getString("energy","set_row")%></td>
</tr>
<tr><th><%=lang.getString("cal", "mon")%></th><%=EGUtils.getTimeTableHtmlRow(ep, 0)%></tr>
<tr><th><%=lang.getString("cal", "tue")%></th><%=EGUtils.getTimeTableHtmlRow(ep, 1)%></tr>
<tr><th><%=lang.getString("cal", "wed")%></th><%=EGUtils.getTimeTableHtmlRow(ep, 2)%></tr>
<tr><th><%=lang.getString("cal", "thu")%></th><%=EGUtils.getTimeTableHtmlRow(ep, 3)%></tr>
<tr><th><%=lang.getString("cal", "fri")%></th><%=EGUtils.getTimeTableHtmlRow(ep, 4)%></tr>
<tr><th><%=lang.getString("cal", "sat")%></th><%=EGUtils.getTimeTableHtmlRow(ep, 5)%></tr>
<tr><th><%=lang.getString("cal", "sun")%></th><%=EGUtils.getTimeTableHtmlRow(ep, 6)%></tr>
<tr height="4"><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
<tr><td align="center"><%=lang.getString("energy","set_col")%></th><%=EGUtils.getTimeTableHtmlRow(ep, 7)%></tr>
</table>
<br>
<table>
<tr>
<td><img id="btnCopy" name="btnCopy" alt="<%=lang.getString("energy","copy")%>" src="images/energy/copy_normal.png" style="cursor:hand;" onClick="onSlotTableCopy()"></td>
<td><img id="btnPaste" name="btnPaste" alt="<%=lang.getString("energy","paste")%>" src="images/energy/paste_disabled.png" style="cursor:hand;" onClick="onSlotTablePaste()"></td>
</tr>
</table>
</div>

<div id="exception_days" style="display:none;visibility:hidden;">
<table border="0" cellspacing="0" width="99%" class="standardTxt">
<tr valign="middle">
	<td class="standardTxt" align="center" width="40%"><input type="hidden" name="tester" id="tester" value=""/>
		<input class="standardTxt" disabled type="text" name="tester_year" id="tester_year" value="" size="4" maxLength="4" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'/>
		<select class="standardTxt" disabled name="tester_month" id="tester_month">
			<option value="0"><%=gen%></option>
			<option value="1"><%=feb%></option>
			<option value="2"><%=mar%></option>
			<option value="3"><%=apr%></option>
			<option value="4"><%=mag%></option>
			<option value="5"><%=giu%></option>
			<option value="6"><%=lug%></option>
			<option value="7"><%=ago%></option>
			<option value="8"><%=set%></option>
			<option value="9"><%=ott%></option>
			<option value="10"><%=nov%></option>
			<option value="11"><%=dic%></option>
		</select>
		<input class="standardTxt" disabled type="text" name="tester_day" id="tester_day" value="" size="2" maxLength="2" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'/>
		<div id="cal_tester_display"></div>
		<script type="text/javascript">
			var arDay = new Array("<%=dom%>","<%=lun%>","<%=ma%>","<%=mer%>","<%=gio%>","<%=ven%>","<%=sab%>");
			var arMonth = new Array("<%=gen%>","<%=feb%>","<%=mar%>","<%=apr%>","<%=mag%>","<%=giu%>","<%=lug%>","<%=ago%>","<%=set%>","<%=ott%>","<%=nov%>","<%=dic%>");
			cal1 = new Calendar ("cal1", "tester", new Date(), arDay, arMonth);
			renderCalendar(cal1);
		</script>
	</td>
	<td align="center" width="*"><img onclick="onAddExDay()" src="images/dbllistbox/arrowdx_on.png" style="cursor:hand;">
	<br><br><img onclick="onDeleteExDay()" src="images/dbllistbox/delete_on.png" style="cursor:hand;">
	</td>	
	<td align="center"  width="40%">
		<%=ep.getExDaysHtmlTable(language)%>
	</td>	
</tr>
</table>
</div>

</div>
