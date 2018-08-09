<%@ page language="java" 
	
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.base.system.SystemInfoExt"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersList"
	import="com.carel.supervisor.plugin.parameters.dataaccess.Parameter"	
	import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersCFG"	
%>
<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
LangService l = LangMgr.getInstance().getLangService(language);
int idsite = sessionUser.getIdSite();

ParametersCFG pcfg = ParametersMgr.getParametersCFG();

String status_string = ParametersMgr.getInstance().isRunning() ? l.getString("parameters","pluginrunning") : l.getString("parameters","pluginnotrunning");
%>

<%@page import="com.carel.supervisor.plugin.parameters.ParametersMgr"%>


<p class='standardTxt'>
<%=l.getString("parameters","general_cfg_comment")%></p>



<br>
<input type="hidden" id="userkeybemptyerrormsg" value="<%=l.getString("parameters","userkeybemptyerrormsg") %>">
<form id="param_gen" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<FIELDSET style='width:98%'>
<input type="hidden" name="CMD" value="update_general_cfg"/>
<LEGEND class='standardTxt'><%=l.getString("parameters","generalconfiguration") %> </LEGEND>
<br/>
<table class='standardTxt' cellpadding="0" cellspacing="0" width="100%">

	<tr><td>&nbsp;</td><td></td></tr>
	<tr>
		<td align="right">
			<%=l.getString("parameters","checkinginterval") %>&nbsp;
		</td>
		<td>
			<select name="checkinterval" id="checkinterval" >
				<option value="120"  <% if (pcfg.getCheckinterval()==120)  {%>selected <%} %>><%=l.getString("parameters","4ore") %></option>
				<option value="180"  <% if (pcfg.getCheckinterval()==180)  {%>selected <%} %>><%=l.getString("parameters","6ore") %></option>
				<option value="240"  <% if (pcfg.getCheckinterval()==240)  {%>selected <%} %>><%=l.getString("parameters","8ore") %></option>
				<option value="360"  <% if (pcfg.getCheckinterval()==360)  {%>selected <%} %>><%=l.getString("parameters","12ore") %></option>
				<option value="720" <% if (pcfg.getCheckinterval()==720)  {%>selected <%} %>><%=l.getString("parameters","24ore") %></option>
			</select>
		</td>
	</tr>
	<tr><td>&nbsp;</td><td></td></tr>
	<tr>
		<td align="right"><%=l.getString("parameters","usernametastiera") %>&nbsp;
		</td>
		<td>
			<input type="text" id="usertastiera" name="usertastiera" value="<%=pcfg.getUserTastiera() %>" onkeydown="checkLettNum(this,event);"></input>
		</td>
	</tr>

	
</table>
<br/>


</FIELDSET>
<FIELDSET>
<br/>
	<LEGEND class='standardTxt'><%=l.getString("parameters","startandstop_section") %> </LEGEND>
	<table class='standardTxt' width="100%" >
		<TR>
		<TD align="right"  width="40%">
		<%=l.getString("parameters","enableparameters") %> &nbsp;
		</TD>
		
		<td>
			<input type="checkbox" name="enabledstatus" <% if (pcfg.getEnabled())  {%>checked <%} %> ></input>
		</td>
		
	</TR>
		<tr>
			<td width="40%" align="right"><%=l.getString("parameters","startandstop_status") %>: &nbsp;
			</td>

			<td><%=status_string %>
			</td>
		</tr>
		<tr><td>&nbsp;</td><td></td></tr>	
		<tr>
			<td align="right"><%=l.getString("parameters","startandstop_actions") %>: &nbsp;
			</td>
			<td>
			<table class='standardTxt'>
				<tr>
					<td width="60">&nbsp;</td>
					<td width="60">&nbsp;</td>
					<td width="60">&nbsp;</td>
				</tr>
<%if (ParametersMgr.getInstance().isRunning() ) { %>

				<tr>
					<td style="text-align:center">
						<img src="./images/actions/start_off.png"  border="0" style="cursor:pointer;" />
					<td style="text-align:center">
						<img src="./images/actions/stop_on.png" onclick="parameters_stop();" border="0" style="cursor:pointer;"  />
					<td style="text-align:center">
						<img src="./images/actions/restart_on.png" onclick="parameters_restart();"  border="0" style="cursor:pointer;" />
					</td>
				</tr>	


<%} else if(ParametersMgr.getParametersCFG().getEnabled()) { %>
			<tr>
					<td style="text-align:center">
						<img src="./images/actions/start_on.png" onclick="parameters_start();"  border="0" style="cursor:pointer;" /> 
					<td style="text-align:center">
						<img src="./images/actions/stop_off.png" border="0" style="cursor:pointer;"  />
					<td style="text-align:center">
						<img src="./images/actions/restart_off.png" border="0" style="cursor:pointer;" />
					</td>
				</tr>	
<%} else {%>

			<tr>
					<td style="text-align:center">
						<img src="./images/actions/start_off.png"  border="0" style="cursor:pointer;" />
					<td style="text-align:center">
						<img src="./images/actions/stop_off.png" border="0" style="cursor:pointer;"  />
					<td style="text-align:center">
						<img src="./images/actions/restart_off.png" border="0" style="cursor:pointer;" />
					</td>
				</tr>	
<%} %>
				<tr>
					<td style="text-align:center">
						<%=l.getString("parameters","start") %>
					<td style="text-align:center">
						<%=l.getString("parameters","stop") %>
					<td style="text-align:center">
						<%=l.getString("parameters","restart") %>
					</td>
				</tr> 
			</table>

			</td>
		</tr>
	</table>

</FIELDSET>
</form>
<form id="param_start_stop" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" name="CMD" value="start_stop">
<input type="hidden" name="param_action" value="">
	
</form>
