<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.presentation.fs.*"
import="com.carel.supervisor.plugin.fs.*"
import="com.carel.supervisor.controller.ControllerMgr"
import="java.util.Date"
import="java.text.SimpleDateFormat"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	String jsession = request.getSession().getId();
	boolean isProtected = ut.isTabProtected();
	int idsite= sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	
	int idrack = Integer.parseInt(sessionUser.getProperty("idrack"));
	FSRack rack = FSRackBean.getActualRackFromDB(idrack,language);

	boolean old = "old".equalsIgnoreCase(rack.getAux())?true:false; 
	int height = sessionUser.getScreenHeight();
	int width = sessionUser.getScreenWidth();
	
	String rack_table = FSStatus.getRackStatusTable(1, FSManager.getInstance().getRackByIdRack(rack.getId_rack()), 
		language,height,width, sessionUser);
	int[] anCounters = new int[FSManager.SB_STATUS];
	String util_table = FSStatus.getStatusTable(rack,language,height,width, sessionUser, anCounters);
	String combo_racks = FSRackBean.getComboRacks(idrack,language);
	
	String strSamplingTime = "***";
	Date dtSamplingTime = FSManager.getInstance().getRackByIdRack(rack.getId_rack()).getSamplingTime(); 
	if( dtSamplingTime != null ) {
		SimpleDateFormat dtFormat = new SimpleDateFormat("HH:mm");
		strSamplingTime = dtFormat.format(dtSamplingTime);
	}
	
    int[] leds = FSManager.getInstance().getRackSatus(rack);
    boolean solenoidNull = FSManager.getInstance().hasSolenoidNull(rack);
%>
	<script type="text/javascript" src="mobile/scripts/jquery/jquery-1.6.4.min.js"></script>
	<p class='standardTxt'></p>
	
	<FORM name="frm_fsdtl" id="frm_fsdtl" action="servlet/master;jsessionid=<%=jsession%>" method="post">
		<INPUT type='hidden' id='idrack' name='idrack'>
		<INPUT type='hidden' id='idutil' name='idutil'>
		<INPUT type='hidden' id='cmd' name='cmd'>
	</FORM> 		
	
	<p class='standardTxt'><b><%=lan.getString("fsdetail","rack")%> &nbsp; 	<%=combo_racks%></b><%if( rack.isNewAlg()&& solenoidNull ) {out.print(lan.getString("fsdetail","solenoidnull"));} %></p>

	
	
	<TABLE class="standardTxt" height="20%" width='100%' border='0' cellpadding="1" cellspacing="5">
		<TR>
			<TD valign="top" width="25%"><FIELDSET class='field'>
				<LEGEND class='standardTxt'><%=lan.getString("fsdetail","inforack") %></LEGEND>
				<TABLE cellpadding="1" cellspacing="4" class='standardTxt'>
<%if( rack.isNewAlg() ) {%>
						<TR>
							<TD width="15%"><b><%=lan.getString("fsdetail", "last_sampling") %></b></TD>
							<TD width="5%" align="right"><b><span id="last_sampling"><%=strSamplingTime%></span></b></TD>
							<TD width="5%" align="left">&nbsp;</TD>
						</TR>
<%}%>					
						<TR>
							<TD width="15%"><b><%=lan.getString("fsdetail","currentset") %></b></TD>
							<TD width="5%" align="right"><b><span id="current_setpoint"><%=ControllerMgr.getInstance().getFromField(rack.getId_setpoint().intValue()).getFormattedValue()%></span></b></TD>
							<TD width="5%" align="left"><b><%=ControllerMgr.getInstance().getFromField(rack.getId_setpoint().intValue()).getInfo().getMeasureunit()%></b></TD>
						</TR>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail","minset") %></TD>
							<TD width="5%" align="right"><span id="minimum_setpoint"><%=old?rack.getId_minset():ControllerMgr.getInstance().getFromField(rack.getId_minset().intValue()).getFormattedValue()%></span></TD>
							<TD width="5%" align="left"> <%=old?rack.getId_minset():ControllerMgr.getInstance().getFromField(rack.getId_minset().intValue()).getInfo().getMeasureunit()%></TD>
						</TR>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail","maxset") %></TD>
							<TD width="5%" align="right"><span id="maximum_setpoint"><%=old?rack.getId_maxset():ControllerMgr.getInstance().getFromField(rack.getId_maxset().intValue()).getFormattedValue()%></span></TD>
							<TD width="5%" align="left"><%=old?rack.getId_maxset():ControllerMgr.getInstance().getFromField(rack.getId_maxset().intValue()).getInfo().getMeasureunit()%></TD>
						</TR>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail","grad") %></TD>
							<TD width="5%" align="right"><span id="gradient"><%=old?rack.getId_gradient():ControllerMgr.getInstance().getFromField(rack.getId_gradient().intValue()).getFormattedValue()%></span></TD>
							<TD width="5%" align="left"><%=old?rack.getId_gradient():ControllerMgr.getInstance().getFromField(rack.getId_gradient().intValue()).getInfo().getMeasureunit()%></TD>
						</TR>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail","nutils") %></TD>
							<TD width="5%" align="right"><span id="number_of_utilities"><%=rack.getUtils().length%></span></TD>
							<TD width="5%" align="left"></TD>
						</TR>
<%if( rack.isNewAlg() ) {%>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail","samples_number") %></TD>
							<TD width="5%" align="right"><span id="samples_number"><%=FSManager.getSb()%></span></TD>
							<TD width="5%" align="left">&nbsp;</TD>
						</TR>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail","sample_period") %></TD>
							<TD width="5%" align="right"><span if="sample_period"><%=FSManager.getT() / 60%></span></TD>
							<TD width="5%" align="left">min</TD>
						</TR>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail","time_window") %></TD>
							<TD width="5%" align="right"><span id="time_window"><%=FSManager.getT() / 60 * FSManager.getSb()%></span></TD>
							<TD width="5%" align="left">min</TD>
						</TR>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail", "yellowstatus")%></TD>
							<TD width="5%" align="right"><span id="yellow_q"><%=FSManager.getYELLOW_Q()%></span></TD>
							<TD width="5%" align="left">&nbsp;</TD>
						</TR>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail", "orangestatus")%></TD>
							<TD width="5%" align="right"><span id="orange_q"><%=FSManager.getORANGE_Q()%></span></TD>
							<TD width="5%" align="left">&nbsp;</TD>
						</TR>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail", "redstatus")%></TD>
							<TD width="5%" align="right"><span id="red_q"><%=FSManager.getRED_Q()%></span></TD>
							<TD width="5%" align="left">&nbsp;</TD>
						</TR>
<%} else {%>			
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail","timewindow") %></TD>
							<TD width="5%" align="right"><span id="time_window_dc"><%=rack.getTimewindow()/60%></span></TD>
							<TD width="5%" align="left">min</TD>
						</TR>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail","freqdc") %></TD>
							<TD width="5%" align="right"><span id="frequency_dc"><%=rack.getWaittime()/60%></span></TD>
							<TD width="5%" align="left">min</TD>
						</TR>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail","tmaxoffline") %></TD>
							<TD width="5%" align="right"><span id="maximum_offline_time"><%=rack.getMaxofftime()%></span></TD>
							<TD width="5%" align="left">%</TD>
						</TR>
						<TR>
							<TD width="15%"><%=lan.getString("fsdetail","maxnutiloffline") %></TD>
							<TD width="5%" align="right"><span id="maximum_offline_utilities"><%=rack.getMaxoffutil()%></span></TD>
							<TD width="5%" align="left"></TD>
						</TR>
<%}%>						
				</TABLE>
				</FIELDSET>
			</TD>
			<TD valign="top" width="75%">
<%if( rack.isNewAlg() ) {%>
				<img style="float:right;cursor:pointer" title="hide/show" src='images/button/eye.png' onclick="newAlgorithmObj.hideCol(3)"/>
				<script>
				var newAlgorithmObj = {
					hideCol:function(colIndex) {
						$("#tabRacks tr").each(function () {
					    	$(this).find("th:gt("+colIndex+")").toggle();
						});
						$("#tabUtil tr").each(function () {
					    	$(this).find("th:gt("+colIndex+")").toggle();
						});
						$("#TableData0 tr").each(function () {
					    	$(this).find("td:gt("+colIndex+")").toggle();
						});
						$("#TableData1 tr").each(function () {
					    	$(this).find("td:gt("+colIndex+")").toggle();
						});
					},
					remain:function(colIndex)
					{
						if($("#tabRacks tr th:eq("+(colIndex+1)+")").is(":hidden"))
						{
							$("#TableData0 tr").each(function () {
						    	$(this).find("td:gt("+colIndex+")").hide();
							});
							$("#TableData1 tr").each(function () {
						    	$(this).find("td:gt("+colIndex+")").hide();
							});
						}
					}
				}
				</script>
				<FIELDSET class='field'>
				<LEGEND class='standardTxt'><%=lan.getString("fsdetail","racks") %></LEGEND>
				<%=rack_table%>
				</FIELDSET>
<%}%>
				<FIELDSET class='field'>
				<LEGEND class='standardTxt'><%=lan.getString("fsdetail","utils") %></LEGEND>
				<TABLE>
					<TR valign="middle">
						<TD valign="middle" align="right" class='standardTxt' width="98%"><%=lan.getString("fsdetail", rack.isNewAlg() ? "reset_counters" : "resetallutils") %></td>
						<td width="*"><img onclick='reset_all()' style='cursor:pointer' src='images/button/reset_on_black.png'/></TD>
					</TR>
					<TR>
						<TD colspan="2"><%=util_table%></TD>
					</TR>			
				</TABLE>
				</FIELDSET>
				<%if( rack.isNewAlg() ) {%>
				<FIELDSET class='field'>
				<LEGEND class='standardTxt'><%=lan.getString("fsdetail","block_fs") %></LEGEND>
						<table id="tabNumUtils" class="table">
							<tr height="22px" class="th">
								<td width="100px"></td>
								<td align="center" width="100px"><b><%=lan.getString("fsdetail", "yellow")%></b></td>
								<td align="center" width="100px"><b><%=lan.getString("fsdetail", "orange")%></b></td>
								<td align="center" width="100px"><b><%=lan.getString("fsdetail", "red")%></b></td>
							</tr>
							<tr class="td">
								<td align="center"><b><%=lan.getString("fsdetail","online") %></b></td>
								<td align="center"><b id="yellow_count"><%=leds[FSManager.YELLOW] %></b></td>
								<td align="center"><b id="orange_count"><%=leds[FSManager.ORANGE] %></b></td>
								<td align="center"><b id="red_count"><%=leds[FSManager.RED] %></b></td>
							</tr>
							<tr class="td">
								<td align="center"><b><%=lan.getString("fsdetail","offline") %></b></td>
								<td align="center"><b id="yellow_offline_count"><%=leds[FSManager.YELLOW_OFFLINE] %></b></td>
								<td align="center"><b id="orange_offline_count"><%=leds[FSManager.ORANGE_OFFLINE] %></b></td>
								<td align="center"><b id="red_offline_count"><%=leds[FSManager.RED_OFFLINE] %></b></td>
							</tr>
						</table>
					</FIELDSET>
				<%}%>		
			</TD>
		</TR>
	</TABLE>