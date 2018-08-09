<%@ page language="java"%>
<%@page import="com.carel.supervisor.presentation.session.*"%>
<%@page import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%@page import="com.carel.supervisor.presentation.helper.VirtualKeyboard"%>
<%@page import="java.util.*"%>
<%@page import="com.carel.supervisor.plugin.base.Plugin"%>
<%@page import="com.carel.supervisor.presentation.bean.*"%>
<%@page import="com.carel.supervisor.plugin.energy.*"%>
<%@page import="com.carel.supervisor.presentation.vscheduler.*"%>
<%@page import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBean"%>
<%@page import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
int idsite= sessionUser.getIdSite();
String jsession = request.getSession().getId();	
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);

Integer idModel = (Integer)ut.removeAttribute("idModel");
if( idModel == null )
	idModel = 0;
EnergyModel model = new EnergyModel(idModel);

//VS categories used to render device models
VSCategory cat = new VSCategory(idsite, language);
cat.setScreenSize(sessionUser.getScreenWidth(), sessionUser.getScreenHeight());

boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();	
%>

<%= (bOnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>
<input type='hidden' id='confirm' value='<%=lang.getString("energy","confirm")%>'>
<input type='hidden' id='alert_req_var' value='<%=lang.getString("energy","alert_req_var")%>'>
<input type="hidden" id="type1" name="type1" value="<%=lang.getString("vs", "type1")%>">
<input type="hidden" id="type2" name="type2" value="<%=lang.getString("vs", "type2")%>">
<input type="hidden" id="type3" name="type3" value="<%=lang.getString("vs", "type3")%>">
<input type="hidden" id="type4" name="type4" value="<%=lang.getString("vs", "type4")%>">

<%=EnergyModel.getHtmlModelTable(sessionUser)%>


<div id="divModel" style="display:<%=model.getIdModel() > 0 ? "block" : "none"%>;">
<form id="frm_model" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<fieldset class="field">
<legend class="standardTxt"><%=lang.getString("energy", "edit")%></legend>
<input id="cmd" name="cmd" type="hidden">
<input id="idModel" name="idModel" type="hidden" value="<%=model.getIdModel()%>">
<table id="tabModel" class="table" width="100%'">
<tr class="row">
	<th class="th" width="25%"><%=lang.getString("energy", "energy_meter_model")%></th>
	<th class="th" width="25%"><%=lang.getString("energy", "device_model")%></th>
	<th class="th" width="25%"><%=lang.getString("energy", "power")%></td>
	<th class="th" width="25%"><%=lang.getString("energy", "active_energy")%></th>
</tr>
<tr valign="middle">
	<td><input id="name" name="name" type="text" size="32" maxlength="64" value="<%=model.getName()%>" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
	<td><span id="span_idDevMdl" class="standardTxt"><%=EnergyModel.getDevMdlName(language, model.getIdDevMdl())%></span><input id="idDevMdl" name="idDevMdl" type="hidden" value="<%=model.getIdDevMdl()%>"></td>
	<td>
		<table width="100%"><tr><td>
		<span id="span_idVarMdlKw" class="standardTxt"><%=EnergyModel.getVarMdlName(language, model.getIdVarMdlKw())%></span><input id="idVarMdlKw" name="idVarMdlKw" type="hidden" value="<%=model.getIdVarMdlKw()%>">
		</td>
		<td width="*" align="right">
			<input type = "button" id="btn_idVarMdlKw"  onClick="selectVar('idVarMdlKw');"  class="autodetect" value="<%=lang.getString("opt_startstop", "selectVar")%>">
		</td></tr></table>
	</td>
	<td>
		<table width="100%"><tr><td>
		<span id="span_idVarMdlKwh" class="standardTxt"><%=EnergyModel.getVarMdlName(language, model.getIdVarMdlKwh())%></span><input id="idVarMdlKwh" name="idVarMdlKwh" type="hidden" value="<%=model.getIdVarMdlKwh()%>">
		</td>
		<td width="*" align="right">
			<input type = "button" id="btn_idVarMdlKwh"  onClick="selectVar('idVarMdlKwh');"  class="autodetect" value="<%=lang.getString("opt_startstop", "selectVar")%>">
		</td></tr></table>
	</td>
</tr>
</table>
</fieldset>
</form>
<script>
// align tabModel columns with model list table columns
//var tabModel = document.getElementById("tabModel");
//tabModel.rows[0].cells[0].setAttribute('width', parseInt(aSize[0], 10) + 5 + "px");
//tabModel.rows[0].cells[1].setAttribute('width', parseInt(aSize[1], 10) + 9 + "px");
//tabModel.rows[0].cells[2].setAttribute('width', parseInt(aSize[2], 10) + 9 + "px");
//tabModel.rows[0].cells[3].setAttribute('width', parseInt(aSize[3], 10) + 9 + "px");
</script>
</div>


<div id="divVarSelection" style="display:none;">
<fieldset class='field' style="height:240px;">		
	<legend class='standardTxt'><%=lang.getString("vs","settings")%></legend>
	<table width="100%">
		 <tr>
			<td width="90%">&nbsp;</td>
			<TD align="center" width="5%">
				<input type = "button" id="btnSetVar"  onClick="setVar();"  class="autodetect" value="<%=lang.getString("opt_startstop", "setVar")%>" >
			</TD>
			<TD align="center" width="5%">
				<input type = "button" id="btnSelCancel"  onClick="onSelCancel();"  class="autodetect" value="<%=lang.getString("opt_startstop", "cancel")%>" >
			</TD>
		</tr>
	</table>
	<table width="100%">
		<tr>
			<td><%=cat.getHTMLDevMdlTable()%></td>
			<td><%=cat.getHTMLVarMdlTable()%></td>
		</tr>
	</table>
</fieldset>
</div>
