<%@ page language="java" 
import="java.util.*"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.session.Transaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.base.config.ProductInfoMgr"
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

// co2 models
ModelBean[] models = ModelBean.retrieveModels(language);

int nMaxMasterOfflineTime = CO2SavingManager.getInstance().getMaxMasterOfflineTime();
StringBuffer sbOfflineTime = new StringBuffer();
for(int i = 1; i <= 60; i++) {
	sbOfflineTime.append("<option value=\"");
	sbOfflineTime.append(String.valueOf(i));
	if( i == nMaxMasterOfflineTime )
		sbOfflineTime.append("\" selected>");
	else
		sbOfflineTime.append("\">");
	sbOfflineTime.append(String.valueOf(i));
	sbOfflineTime.append("</option>");
}
int nGroupOnSwitchDelay = CO2SavingManager.getInstance().getGroupOnSwitchDelay();
StringBuffer sbSwitchDelay = new StringBuffer();
for(int i = 1; i <= 60; i++) {
	sbSwitchDelay.append("<option value=\"");
	sbSwitchDelay.append(String.valueOf(i));
	if( i == nGroupOnSwitchDelay )
		sbSwitchDelay.append("\" selected>");
	else
		sbSwitchDelay.append("\">");
	sbSwitchDelay.append(String.valueOf(i));
	sbSwitchDelay.append("</option>");
}
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":""%>
<form id="formConfiguration" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<table class="table" width="98%">
	<tr class="th" height='22'>
		<th width="33%"><%=lang.getString("co2", "master_devices")%></th>
		<th width="33%"><%=lang.getString("co2", "master_variable")%></th>
		<th width="17%"><%=lang.getString("co2", "status_variable_off")%></th>
		<th width="17%" class="td">&nbsp;</th>
	</tr>
	<%for(int i = 0, row = 0; i < models.length; i++) if( models[i].isRack() ) { ModelBean model = models[i];%>
	<tr class="<%=row % 2 == 0 ? "Row1" : "Row2"%>">
		<td class="standardTxt">
			<%=model.getDevDescription()%>
			<input name="isRack<%=i%>" type="hidden" value="<%=model.isRack()%>">
			<input name="idCO2devmdl<%=i%>" type="hidden" value="<%=model.getIdCO2devmdl()%>">
		</td>
		<td class="standardTxt">
			<select name="varCode<%=i%>" style="width:100%;text-overflow:ellipsis;">
				<%=ModelBean.getVarOptions(language, model.getDevCode(), model.getVarCode(), false)%>
			</select>
			<input name="varCodeRack<%=i%>" type="hidden" value="<%=model.getVarCode()%>">
		</td>
		<td class="standardTxt" align="center">
			<input name="statusOff<%=i%>" type="text" value="<%=model.getStatusOff()%>" class="<%=(bOnScreenKey ? "keyboardInput" : "standardTxt")%>">
		</td>
	</tr>	
	<%row++; }%>
</table>

<br>

<table class="table" width="98%">
	<tr class="th" height='22'>
		<th width="33%"><%=lang.getString("co2", "slave_devices")%></th>
		<th width="33%"><%=lang.getString("co2", "master_variable")%></th>
		<th width="17%"><%=lang.getString("co2", "status_variable_off")%></th>
		<th width="17%"><%=lang.getString("co2", "status_variable_on")%></th>
	</tr>
	<%for(int i = 0, row = 0; i < models.length; i++) if( !models[i].isRack() ) { ModelBean model = models[i];%>
	<tr class="<%=row % 2 == 0 ? "Row1" : "Row2"%>">
		<td class="standardTxt" rowspan="2">
			<%=model.getDevDescription()%>
			<input name="isRack<%=i%>" type="hidden" value="<%=model.isRack()%>">
			<input name="idCO2devmdl<%=i%>" type="hidden" value="<%=model.getIdCO2devmdl()%>">
		</td>
		<td class="standardTxt">
			<select name="varCode<%=i%>" style="width:100%;text-overflow:ellipsis;">
				<%=ModelBean.getVarOptions(language, model.getDevCode(), model.getVarCode(), true)%>
			</select>
		</td>
		<td class="standardTxt" align="center">
			<input name="statusOff<%=i%>" type="text" value="<%=model.getStatusOff()%>" class="<%=(bOnScreenKey ? "keyboardInput" : "standardTxt")%>">
		</td>
		<td class="standardTxt" align="center">
			<input name="statusOn<%=i%>" type="text" value="<%=model.getStatusOn()%>" class="<%=(bOnScreenKey ? "keyboardInput" : "standardTxt")%>">
		</td>
	</tr>
	<tr class="<%=row % 2 == 0 ? "Row1" : "Row2"%>">
		<td class="standardTxt">
			<select name="var2Code<%=i%>" style="width:100%;text-overflow:ellipsis;" onChange="onSelectVar2(<%=i%>, this.value)">
				<option value=""></option>
				<%=ModelBean.getVarOptions(language, model.getDevCode(), model.getVar2Code(), true)%>
			</select>
		</td>
		<td class="standardTxt" align="center">
			<input id="status2Off<%=i%>" name="status2Off<%=i%>" type="text" value="<%=model.getStatus2Off()%>" class="<%=(bOnScreenKey ? "keyboardInput" : "standardTxt")%>">
		</td>
		<td class="standardTxt" align="center">
			<input id="status2On<%=i%>" name="status2On<%=i%>" type="text" value="<%=model.getStatus2On()%>" class="<%=(bOnScreenKey ? "keyboardInput" : "standardTxt")%>">
		</td>
	</tr>	
	<%row++; }%>
</table>

<br>
<fieldset style='width:97%;'>
<legend class='standardTxt'><%=lang.getString("co2", "settings")%></legend>
<table class="table">
	<tr class="standardTxt">
		<td style="white-space:nowrap;"><%=lang.getString("co2", "max_master_offline")%></td>
		<td>
			<select name="offlineTime">
				<%=sbOfflineTime.toString()%>
			</select>
		</td>
		<td width="10%"><%=lang.getString("co2", "min")%></td>
	</tr>
	<tr class="standardTxt">
		<td style="white-space:nowrap;"><%=lang.getString("co2", "device_switch_delay")%></td>
		<td>
			<select name="switchDelay">
				<%=sbSwitchDelay.toString()%>
			</select>
		</td>
		<td><%=lang.getString("co2", "min")%></td>
	</tr>
</table>
</fieldset>
<br><br>
<fieldset style='width:97%;'>
<legend class='standardTxt'><%=lang.getString("co2", "master_offline_action")%></legend>
<table class="table">
	<tr class="standardTxt">
		<td>
			<input name="offlineAction" type="radio" value="1" <%=CO2SavingManager.getInstance().getMasterOfflineAction() == CO2SavingManager.RUNNING_MODE ? "checked" : ""%>>
		</td>
		<td><%=lang.getString("co2", "running_mode")%></td>
	</tr>
	<tr class="standardTxt">
		<td>
			<input name="offlineAction" type="radio" value="0" <%=CO2SavingManager.getInstance().getMasterOfflineAction() == CO2SavingManager.SAFE_MODE ? "checked" : ""%>>
		</td>
		<td><%=lang.getString("co2", "safe_mode")%>
	</tr>
	<tr class="standardTxt">
		<td>
			<input name="offlineAction" type="radio" value="-1" <%=CO2SavingManager.getInstance().getMasterOfflineAction() == CO2SavingManager.CURRENT_MODE ? "checked" : ""%>>
		</td>
		<td><%=lang.getString("co2", "current_mode")%></td>
	</tr>
	</table>
</fieldset>
</form>

