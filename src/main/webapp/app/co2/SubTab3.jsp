<%@ page language="java" 
import="java.util.*"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.dbllistbox.*"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.session.Transaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.base.config.ProductInfoMgr"
import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBean"
import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.plugin.co2.*"
import="com.carel.supervisor.presentation.co2.*"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();
boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();

// rack
Integer idRack = (Integer)ut.removeAttribute("idRack");
if( idRack == null )
	idRack = 0;
String rackName = "";
RackBean rack = null;
RackBean[] racks = RackBean.retrieveRacksSelected(language);
for(int i = 0; i < racks.length; i++) {
	if( racks[i].getIdRack() == idRack ) {
		rack = racks[i];
		rackName = rack.getDescription();
	}
}

// backup device
DeviceListBean devs = new DeviceListBean(idsite, language);
int[] ids = devs.getIds();
StringBuffer sbDevice = new StringBuffer();
for(int i = 0; i < ids.length; i++) {
	DeviceBean dev = devs.getDevice(ids[i]);
	sbDevice.append("<option value=\"");
	sbDevice.append(dev.getIddevice());
	if( rack != null && rack.getIdBackupDevice() == dev.getIddevice() )
		sbDevice.append("\" selected>");
	else
		sbDevice.append("\">");
	sbDevice.append(dev.getDescription());
	sbDevice.append("</option>");
}

// backup variable
StringBuffer sbVariable = new StringBuffer();
if( rack != null && rack.getIdBackupDevice() > 0 ) {
	VarphyBeanList varList = new VarphyBeanList();
	VarphyBean[] vars = varList.getAllVarOfDevice(language, idsite, rack.getIdBackupDevice());
	for(int i = 0; i < vars.length; i++) {
		VarphyBean var = vars[i];
		if( var.getType() != 4 ) {
			sbVariable.append("<option value=\"");
			sbVariable.append(var.getId());
			if( rack.getIdBackupVariable() == var.getId() )
				sbVariable.append("\" selected>");
			else
				sbVariable.append("\">");
			sbVariable.append(var.getShortDescription());
			sbVariable.append("</option>");
		}
	}
}
%>
<%if( racks.length > 0 ) {%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<p class='standardTxt'></p>
<%=RackBean.getHTMLBackupTable(sessionUser)%>
<br>
<div style="display:<%=idRack > 0 ? "block" : "none"%>">
<fieldset class='field' style="width: 97%;">
<legend class='standardTxt'><%=lang.getString("co2", "offline_backup_condition")%></legend>
<form id="formBackup" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd">
<input type="hidden" id="idRack" name="idRack" value="<%=idRack%>">
<table class="table" width="100%">
	<tr>
		<td class="standardTxt"><%=lang.getString("co2", "selected_rack")%></td>
		<td><input id="name" name="name" type="text" size="50" value="<%=rackName%>" readonly></td>
		<td colspan="4">&nbsp;</td>
	</tr>
	<tr>
		<td class="standardTxt" colspan="6">&nbsp;</td>
	</tr>
	<tr>
		<td class="standardTxt"><%=lang.getString("co2", "backup_device")%></td>
		<td>
			<select id="device" name="device" onChange="onSelectBackupDevice(this.value)" style="width:325px;">
				<option value="0">----------</option><%=sbDevice.toString()%>
			</select>
		</td>
		<td class="standardTxt"><%=lang.getString("co2", "offline")%><input id="offline" name="offline" type="checkbox" <%=rack != null && rack.getOffline() ? "checked" : ""%>></td>
		<td class="standardTxt"><%=lang.getString("co2", "variable")%></td>
		<td colspan="2">
			<select id="variable" name="variable" style="width:325px;">
				<option value="0">----------</option><%=sbVariable.toString()%>
			</select>
		</td>
	</tr>
	<tr>
		<td class="standardTxt" colspan="6">&nbsp;</td>
	</tr>
	<tr>
		<td class="standardTxt" colspan="3">&nbsp;</td>
		<td class="standardTxt"><%=lang.getString("co2", "operation")%></td>
		<td>
			<select name="operation">
				<option value="=" <%=rack != null && rack.getOperation().equals("=") ? "selected" : ""%>>=</option>
				<option value="&gt;" <%=rack != null && rack.getOperation().equals(">") ? "selected" : ""%>>&gt;</option>
				<option value="&gt;=" <%=rack != null && rack.getOperation().equals(">=") ? "selected" : ""%>>&gt;=</option>
				<option value="&lt;" <%=rack != null && rack.getOperation().equals("<") ? "selected" : ""%>>&lt;</option>
				<option value="&lt;=" <%=rack != null && rack.getOperation().equals("<=") ? "selected" : ""%>>&lt;=</option>
				<option value="!=" <%=rack != null && rack.getOperation().equals("!=") ? "selected" : ""%>>!=</option>
			</select>
		</td>
		<td class="standardTxt"><%=lang.getString("co2", "constant")%>&nbsp;
			<input name="constant" type="text" maxlength="10" size="8" value="<%=rack != null ? rack.getConstant() : "0"%>">
		</td>
	</tr>
</table>
</form>
</fieldset>
</div>
<%} else {%>
<p class="mediumTxt">
	<b><%=lang.getString("co2", "no_rack_selected")%></b>
</p>
<%}%>
