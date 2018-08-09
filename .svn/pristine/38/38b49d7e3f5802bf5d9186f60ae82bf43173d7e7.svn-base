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
	
	import="com.carel.supervisor.presentation.dbllistbox.DblListBox"
	import="java.util.ArrayList"
	
	import="com.carel.supervisor.presentation.dbllistbox.ListBoxElement"
	import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersEventsList"	
%>
<%@page import="com.carel.supervisor.plugin.parameters.ParametersMgr"%>
<%@page import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersPhotoRow"%>
<%@page import="com.carel.supervisor.presentation.bean.ProfileBeanList"%>
<%@page import="com.carel.supervisor.presentation.bean.ProfileBean"%>

<%	
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
LangService l = LangMgr.getInstance().getLangService(language);
int idsite = sessionUser.getIdSite();

ParametersPhotoRow[] foto = ParametersMgr.getParametersPhoto().readPhoto(language);
String status_string = ParametersMgr.getInstance().isRunning() ? l.getString("parameters","pluginrunning") : l.getString("parameters","pluginnotrunning");
boolean dtlviewEnabled = sessionUser.isMenuActive("dtlview");
%>

<p class='standardTxt'>
	<%=l.getString("parameters","status_list_comment")%>
</p>


<%if (ParametersMgr.canModify(sessionUser) )
{ %>
<FIELDSET style='width:97%;'>
	<LEGEND class='standardTxt'><%=l.getString("parameters","generalconfiguration") %> </LEGEND>
	<form id="param_gen" action="servlet/master;jsessionid=<%=jsession%>" method="post">
		<input type="hidden" name="CMD" value="update_general_cfg"/>
		<input type="hidden" id="usertastiera" name="usertastiera" value="<%=ParametersMgr.getParametersCFG().getUserTastiera()%>" />
		<input type="hidden" id="checkinterval" name="checkinterval" value="<%=ParametersMgr.getParametersCFG().getCheckinterval() %>" />
		
		<table width="100%">
			<tr class='standardTxt'>
				<td class='table' width="*">
					<b><%=l.getString("parameters","enableparameters") %></b>
					<input type="checkbox" name="enabledstatus" onclick="parameters_general_save();" onchange="parameters_general_save();" <% if (ParametersMgr.getParametersCFG().getEnabled())  {%>checked  <%} %> ></input>
				</td>
				<td class='actionTD' width="35%" align="left">
					<table class='standardTxt' width="60%">
						<%if (ParametersMgr.getInstance().isRunning() ) { %>	
						<tr>
							<td style="text-align:left;width: 33%;" >
								<img src="./images/actions/start_off.png"  border="0" style="cursor:pointer;" />
							</td>
							<td style="text-align:left;width: 33%;">
								<img src="./images/actions/stop_on_black.png" onclick="parameters_stop();" border="0" style="cursor:pointer;"  />
							</td>
							<td style="text-align:left;width: 33%;">
								<img src="./images/actions/restart_on_black.png" onclick="parameters_restart();"  border="0" style="cursor:pointer;" />
							</td>
						</tr>	
						<%} else if(ParametersMgr.getParametersCFG().getEnabled()) { %>
						<tr>
							<td style="text-align:left;width: 33%;">
								<img src="./images/actions/start_on_black.png" onclick="parameters_start();"  border="0" style="cursor:pointer;" />
							</td> 
							<td style="text-align:left;width: 33%;">
								<img src="./images/actions/stop_off.png" border="0" style="cursor:pointer;"  />
							</td>
							<td style="text-align:left;width: 33%;">
								<img src="./images/actions/restart_off.png" border="0" style="cursor:pointer;" />
							</td>
						</tr>	
						<%} else {%>
						<tr>
							<td style="text-align:left;width: 33%;">
								<img src="./images/actions/start_off.png"  border="0" style="cursor:pointer;" />
							</td>
							<td style="text-align:left;width: 33%;">
								<img src="./images/actions/stop_off.png" border="0" style="cursor:pointer;"  />
							</td>
							<td style="text-align:left;width: 33%;">
								<img src="./images/actions/restart_off.png" border="0" style="cursor:pointer;" />
							</td>
						</tr>	
						<%} %>
					</table>
				</td>
				<td class='table' width="20%" align='center' width="30%" <%=( ParametersMgr.getInstance().isRunning()? "style='color:GREEN;'":"style='color:RED;'")%> >
					<B><%=status_string %></B>
				</td>
			</tr>
		</table>
	</form>
</FIELDSET>
<p class="standardTxt"/>
<%} %>
<FIELDSET <%if (ParametersMgr.canModify(sessionUser) )
{ %>  style='width:97%;height:60%;'  <%} else { %>   style='width:97%;height:80%;'  <%} %>
 ><LEGEND class='standardTxt'><%=l.getString("parameters","statusparameters") %></LEGEND>

	<table class="table" width="99%">
		<tr>
			<td class="th" width="33%"> <%=l.getString("parameters","device") %> </td> 
			<td class="th" width="53%"> <%=l.getString("parameters","variable") %>  </td> 
			<td class="th" width="*"> <%=l.getString("parameters","currentvalue") %> </td>
		</tr>
	</table>
	<div style="height: 94%; overflow-y:scroll;">
	<table class="table" width="100%">
		<%for(int i =0 ; i<foto.length;i++) { %>
		<tr class='<%=i%2==0?"Row1":"Row2" %>'>
			<td class="td" width="33%"><%if(dtlviewEnabled){%><a href="javascript:void(0)" style="font-weight:normal" onclick="top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev=<%=foto[i].getIddevice() %>&desc=ncode01')"><%=foto[i].getDev_descr()%></a><%}else{out.print(foto[i].getDev_descr());}%> </td> 
			<td class="td" width="53%"> <%=foto[i].getVar_descr()%> </td> 
			<td class="td" width="*" id="var_<%=foto[i].getIdvariable()%>"><%=foto[i].getPhotovalue().equals(Float.NaN) ? ParametersMgr.FLOAT_NAN_REPLACER : foto[i].getPhotovalue() %></td> 	 
		</tr>	
		<%} %>
	</table>
</div>

<FORM id="param_photo_form" action="servlet/master;jsessionid=<%=jsession%>" method="post">
	<input type="hidden" name="CMD" id="CMD" value="parameters_photo" />
</FORM>
<form id="param_start_stop" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" name="CMD" value="start_stop">
<input type="hidden" id="param_action" name="param_action" value="">
	
</form>
</FIELDSET>
