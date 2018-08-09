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
import="com.carel.supervisor.plugin.optimum.*"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.controller.ControllerMgr"
import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBean"
import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();
Integer idAlgorithm = (Integer)ut.getAttribute("idalg");
if( idAlgorithm == null )
	idAlgorithm = 1;
StartStopBean bean = OptimumManager.getInstance().getStartStop(idAlgorithm); 

// combo algorithms
StringBuffer sbAlgList = new StringBuffer(); 
StartStopBean[] aAlg = StartStopBean.getStartStopList();
for(int i = 0; i < aAlg.length; i++) {
	sbAlgList.append("<option value=\"");
	sbAlgList.append(String.valueOf(aAlg[i].getAlgorithId()));
	if( aAlg[i].getAlgorithId() == idAlgorithm )
		sbAlgList.append("\" selected>");
	else
		sbAlgList.append("\">");
	sbAlgList.append(aAlg[i].getAlgorithmName());
	sbAlgList.append("</option>\n");
}

// combo devices/models
DeviceListBean devs = new DeviceListBean(idsite,language);
DeviceBean tmp_dev = null;
int[] ids = devs.getIds();
StringBuffer div_dev = new StringBuffer();
div_dev.append("<select onclick=\"reload_actions(0);\" ondblclick=\"reload_actions(0);\" id=\"dev\" name='sections'  size='10' class='standardTxt' style='width:100%;' >");
int device=0;
for (int i=0;i<devs.size();i++){
	tmp_dev = devs.getDevice(ids[i]);
	div_dev.append("<option "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"' class= '"+((i%2==0)?"Row1":"Row2")+"'>"+tmp_dev.getDescription()+"</option>\n");
}
div_dev.append("</select>");

DevMdlBeanList devmdllist = new DevMdlBeanList();
DevMdlBean[] devmdl = devmdllist.retrieveDevMdl(idsite,language);
StringBuffer combodev = new StringBuffer();
StringBuffer typedev = new StringBuffer();
combodev.append("<option value='-1'>-------------------</option>");
String ss="";
for (int i=0;i<devmdl.length;i++)
{
	DevMdlBean tmp = devmdl[i];
	combodev.append("<option value="+tmp.getIddevmdl()+" "+ss+">"+tmp.getDescription()+"</option>\n");
}	

VarphyBeanList varlist = new VarphyBeanList();
String DevNameTemperatureSetpoint = "------";
String VarNameTemperatureSetpoint = "------";
if( bean.getTemperatureSetpointVar() > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getTemperatureSetpointVar());
	DevNameTemperatureSetpoint = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getTemperatureSetpointVar() });
	if( vars.length > 0 )
		VarNameTemperatureSetpoint = vars[0].getShortDescription();
}
String DevNameInternalTemperature = "------";
String VarNameInternalTemperature = "------";
if( bean.getInternalTemperatureVar() > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getInternalTemperatureVar());
	DevNameInternalTemperature = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getInternalTemperatureVar() });
	if( vars.length > 0 )
		VarNameInternalTemperature = vars[0].getShortDescription();
}
String DevNameExternalTemperature = "------"; 
String VarNameExternalTemperature = "------";
if( bean.getExternalTemperatureVar() > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getExternalTemperatureVar());
	DevNameExternalTemperature = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getExternalTemperatureVar() });
	if( vars.length > 0 )
		VarNameExternalTemperature = vars[0].getShortDescription();
}
String DevNameSummerWinter = "------";
String VarNameSummerWinter = "------";
if( bean.getSummerWinterVar() > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getSummerWinterVar());
	DevNameSummerWinter = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getSummerWinterVar() });
	if( vars.length > 0 )
		VarNameSummerWinter = vars[0].getShortDescription();
}

// master
String DevNameUnitOnOff = "------"; 
String VarNameUnitOnOff = "------";
if( bean.getUnitOnOffVar() > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getUnitOnOffVar());
	DevNameUnitOnOff = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getUnitOnOffVar() });
	if( vars.length > 0 )
		VarNameUnitOnOff = vars[0].getShortDescription();
}
String DevNameUnitOnOffCmd = "------";
String VarNameUnitOnOffCmd = "------";
if( bean.getUnitOnOffCmdVar() > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getUnitOnOffCmdVar());
	DevNameUnitOnOffCmd = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getUnitOnOffCmdVar() });
	if( vars.length > 0 )
		VarNameUnitOnOffCmd = vars[0].getShortDescription();
}
// slaves
String DevNameUnitOnOff1 = "------"; 
String VarNameUnitOnOff1 = "------";
if( bean.getUnitOnOffVar(1) > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getUnitOnOffVar(1));
	DevNameUnitOnOff1 = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getUnitOnOffVar(1) });
	if( vars.length > 0 )
		VarNameUnitOnOff1 = vars[0].getShortDescription();
}
String DevNameUnitOnOffCmd1 = "------";
String VarNameUnitOnOffCmd1 = "------";
if( bean.getUnitOnOffCmdVar(1) > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getUnitOnOffCmdVar(1));
	DevNameUnitOnOffCmd1 = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getUnitOnOffCmdVar(1) });
	if( vars.length > 0 )
		VarNameUnitOnOffCmd1 = vars[0].getShortDescription();
}
String DevNameUnitOnOff2 = "------"; 
String VarNameUnitOnOff2 = "------";
if( bean.getUnitOnOffVar(2) > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getUnitOnOffVar(2));
	DevNameUnitOnOff2 = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getUnitOnOffVar(2) });
	if( vars.length > 0 )
		VarNameUnitOnOff2 = vars[0].getShortDescription();
}
String DevNameUnitOnOffCmd2 = "------";
String VarNameUnitOnOffCmd2 = "------";
if( bean.getUnitOnOffCmdVar(2) > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getUnitOnOffCmdVar(2));
	DevNameUnitOnOffCmd2 = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getUnitOnOffCmdVar(2) });
	if( vars.length > 0 )
		VarNameUnitOnOffCmd2 = vars[0].getShortDescription();
}
String DevNameUnitOnOff3 = "------"; 
String VarNameUnitOnOff3 = "------";
if( bean.getUnitOnOffVar(3) > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getUnitOnOffVar(3));
	DevNameUnitOnOff3 = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getUnitOnOffVar(3) });
	if( vars.length > 0 )
		VarNameUnitOnOff3 = vars[0].getShortDescription();
}
String DevNameUnitOnOffCmd3 = "------";
String VarNameUnitOnOffCmd3 = "------";
if( bean.getUnitOnOffCmdVar(3) > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getUnitOnOffCmdVar(3));
	DevNameUnitOnOffCmd3 = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getUnitOnOffCmdVar(3) });
	if( vars.length > 0 )
		VarNameUnitOnOffCmd3 = vars[0].getShortDescription();
}

// schedule
StringBuffer sbTimeOnHour = new StringBuffer();
for(int i = 0; i < 24; i++) {
	sbTimeOnHour.append("<option value='" + i + (i == bean.getTimeOnHour() ? "' selected>" : "'>"));
	sbTimeOnHour.append((i < 10 ? "0" : "") + i);
	sbTimeOnHour.append("</option>\n");
}
StringBuffer sbTimeOnMinute = new StringBuffer();
for(int i = 0; i < 60; i++) {
	sbTimeOnMinute.append("<option value='" + i + (i == bean.getTimeOnMinute() ? "' selected>" : "'>"));
	sbTimeOnMinute.append((i < 10 ? "0" : "") + i);
	sbTimeOnMinute.append("</option>\n");
}
StringBuffer sbTimeOffHour = new StringBuffer();
for(int i = 0; i < 24; i++) {
	sbTimeOffHour.append("<option value='" + i + (i == bean.getTimeOffHour() ? "' selected>" : "'>"));
	sbTimeOffHour.append((i < 10 ? "0" : "") + i);
	sbTimeOffHour.append("</option>\n");
}
StringBuffer sbTimeOffMinute = new StringBuffer();
for(int i = 0; i < 60; i++) {
	sbTimeOffMinute.append("<option value='" + i + (i == bean.getTimeOffMinute() ? "' selected>" : "'>"));
	sbTimeOffMinute.append((i < 10 ? "0" : "") + i);
	sbTimeOffMinute.append("</option>\n");
}

boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<input type="hidden" id="max_instances" value="<%=ProductInfoMgr.getInstance().getProductInfo().get("opt_startstop_instances")%>">
<input type="hidden" id="max_alert" value="<%=lang.getString("opt_startstop", "max_alert")%>">
<form id="frm_opt_settings" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input id="cmd" name="cmd" type="hidden">
<fieldset class="field"><legend class="standardTxt"><%=lang.getString("opt_startstop", "alg_params")%></legend>
<table class="table" width="100%">
	<tr>
		<td class="standardTxt"><%=lang.getString("opt_startstop", "alg_name")%></td>
		<td class="standardTxt"><input id="Name" name="Name" type="text" size="48" maxlength="32" value="<%=bean.getAlgorithmName()%>"></td>
		<td class="standardTxt"><%=lang.getString("opt_startstop", "alg_list")%></td>
		<td class="standardTxt"><select id="AlgorithmId" name="AlgorithmId" onChange="onSelectAlgorithm(this.value)">
			<%if( idAlgorithm == 0 ) {%><option value="0" selected><%=lang.getString("opt_startstop", "new_alg")%></option><%}%>
			<%=sbAlgList.toString()%>
		</select></td>
	</tr>
</table>
<table id="tabParams" class="table" width="100%">
	<tr>
		<td class="th" width="35%"><%=lang.getString("opt_startstop", "param")%></td>
		<td class="th" width="35%"><%=lang.getString("opt_startstop", "value")%></td>
		<td class="th" width="*"><%=lang.getString("opt_startstop", "unit")%></td>
		<td class="th" width="*"><%=lang.getString("opt_startstop", "variable_select")%></td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_startstop", "UnitOnOff_config_master")%></td>
		<td class="td"><span id="NameUnitOnOff"><%=DevNameUnitOnOff!="------" ? (DevNameUnitOnOff + " --> " + VarNameUnitOnOff):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOff" name="VarUnitOnOff" value="<%=bean.getUnitOnOffVar()%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOff" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOff')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_startstop", "UnitOnOffCommand_config_master")%></td>
		<td class="td"><span id="NameUnitOnOffCmd"><%=DevNameUnitOnOffCmd!="------" ? (DevNameUnitOnOffCmd + " --> " + VarNameUnitOnOffCmd):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOffCmd" name="VarUnitOnOffCmd" value="<%=bean.getUnitOnOffCmdVar()%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOffCmd" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOffCmd')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "selectVar")%></div>
			</div>
		</td>
	</tr>

	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_startstop", "slave_no")%></td>
		<td class="td"><select id="SlaveNo" name="SlaveNo" onChange="onSlaveNo(this.value)">
			<option value="0" <%=bean.getSlaveNo() == 0 ? "selected" : ""%>>0</option>
			<option value="1" <%=bean.getSlaveNo() == 1 ? "selected" : ""%>>1</option>
			<option value="2" <%=bean.getSlaveNo() == 2 ? "selected" : ""%>>2</option>
			<option value="3" <%=bean.getSlaveNo() == 3 ? "selected" : ""%>>3</option>
		</select></td>
		<td class="td">&nbsp;</td>
		<td class="td">&nbsp;</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_startstop", "Slave")%> 1 - <%=lang.getString("opt_startstop", "UnitOnOff_config")%></td>
		<td class="td"><span id="NameUnitOnOff1"><%=DevNameUnitOnOff1!="------" ? (DevNameUnitOnOff1 + " --> " + VarNameUnitOnOff1):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOff1" name="VarUnitOnOff1" value="<%=bean.getUnitOnOffVar(1)%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOff1" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOff1')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_startstop", "Slave")%> 1 - <%=lang.getString("opt_startstop", "UnitOnOffCommand_config")%></td>
		<td class="td"><span id="NameUnitOnOffCmd1"><%=DevNameUnitOnOffCmd1!="------" ? (DevNameUnitOnOffCmd1 + " --> " + VarNameUnitOnOffCmd1):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOffCmd1" name="VarUnitOnOffCmd1" value="<%=bean.getUnitOnOffCmdVar(1)%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOffCmd1" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOffCmd1')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_startstop", "Slave")%> 2 - <%=lang.getString("opt_startstop", "UnitOnOff_config")%></td>
		<td class="td"><span id="NameUnitOnOff2"><%=DevNameUnitOnOff2!="------" ? (DevNameUnitOnOff2 + " --> " + VarNameUnitOnOff2):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOff2" name="VarUnitOnOff2" value="<%=bean.getUnitOnOffVar(2)%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOff2" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOff2')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_startstop", "Slave")%> 2 - <%=lang.getString("opt_startstop", "UnitOnOffCommand_config")%></td>
		<td class="td"><span id="NameUnitOnOffCmd2"><%=DevNameUnitOnOffCmd2!="------" ? (DevNameUnitOnOffCmd2 + " --> " + VarNameUnitOnOffCmd2):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOffCmd2" name="VarUnitOnOffCmd2" value="<%=bean.getUnitOnOffCmdVar(2)%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOffCmd2" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOffCmd2')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_startstop", "Slave")%> 3 - <%=lang.getString("opt_startstop", "UnitOnOff_config")%></td>
		<td class="td"><span id="NameUnitOnOff3"><%=DevNameUnitOnOff3!="------" ? (DevNameUnitOnOff3 + " --> " + VarNameUnitOnOff3):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOff3" name="VarUnitOnOff3" value="<%=bean.getUnitOnOffVar(3)%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOff3" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOff3')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_startstop", "Slave")%> 3 - <%=lang.getString("opt_startstop", "UnitOnOffCommand_config")%></td>
		<td class="td"><span id="NameUnitOnOffCmd3"><%=DevNameUnitOnOffCmd3!="------" ? (DevNameUnitOnOffCmd3 + " --> " + VarNameUnitOnOffCmd3):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOffCmd3" name="VarUnitOnOffCmd3" value="<%=bean.getUnitOnOffCmdVar(3)%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOffCmd3" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOffCmd3')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "selectVar")%></div>
			</div>
		</td>
	</tr>

	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_startstop", "InternalTemperature")%></td>
		<td class="td"><span id="NameInternalTemperature"><%=DevNameInternalTemperature!="------" ? (DevNameInternalTemperature + " --> " + VarNameInternalTemperature):"------"%></span>&nbsp;<input type="hidden" id="VarInternalTemperature" name="VarInternalTemperature" value="<%=bean.getInternalTemperatureVar()%>"></td>
		<td class="td"><%=lang.getString("opt_startstop", "degrees")%></td>
		<td class="td">
			<div id="btnInternalTemperature" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('InternalTemperature')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_startstop", "ExternalTemperature")%></td>
		<td class="td"><span id="NameExternalTemperature"><%=DevNameExternalTemperature!="------" ? (DevNameExternalTemperature + " --> " + VarNameExternalTemperature):"------"%></span>&nbsp;<input type="hidden" id="VarExternalTemperature" name="VarExternalTemperature" value="<%=bean.getExternalTemperatureVar()%>"></td>
		<td class="td"><%=lang.getString("opt_startstop", "degrees")%></td>
		<td class="td">
			<div id="btnExternalTemperature" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('ExternalTemperature')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_startstop", "TemperatureSetpoint")%></td>
		<td class="td">
			<input type="text" id="TemperatureSetpoint" name="TemperatureSetpoint" value="<%=bean.getTemperatureSetpoint()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>">
			<span id="NameTemperatureSetpoint"><%=DevNameTemperatureSetpoint!="------" ? (DevNameTemperatureSetpoint + " --> " + VarNameTemperatureSetpoint):""%></span>&nbsp;
			<input type="hidden" id="VarTemperatureSetpoint" name="VarTemperatureSetpoint" value="<%=bean.getTemperatureSetpointVar()%>">
		</td>
		<td class="td"><%=lang.getString("opt_startstop", "degrees")%></td>
		<td class="td">
			<table cellpadding="0" cellspacing="0">
			<tr><td>
			<div id="btnTemperatureSetpoint" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('TemperatureSetpoint')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "selectVar")%></div>
			</div>
			</td><td>
			<div id="btnClearTemperatureSetpoint" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="clearVar('TemperatureSetpoint')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "clearVar")%></div>
			</div>
			</td></tr>
			</table>
		</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_startstop", "summer_winter")%></td>
		<td class="td">
			<select id="SummerWinter" name="SummerWinter">
				<option value="2" <%=bean.getSummerWinter() == 2 ? "selected" : ""%>><%=lang.getString("opt_startstop", "auto")%></option>
				<option value="0" <%=bean.getSummerWinter() == 0 ? "selected" : ""%>><%=lang.getString("opt_startstop", "summer")%></option>
				<option value="1" <%=bean.getSummerWinter() == 1 ? "selected" : ""%>><%=lang.getString("opt_startstop", "winter")%></option>
			</select>
			<span id="NameSummerWinter"><%=DevNameSummerWinter!="------" ? (DevNameSummerWinter + " --> " + VarNameSummerWinter):""%></span>&nbsp;
			<input type="hidden" id="VarSummerWinter" name="VarSummerWinter" value="<%=bean.getSummerWinterVar()%>">
		</td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<table cellpadding="0" cellspacing="0">
			<tr><td>
			<div id="btnSummerWinter" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('SummerWinter')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "selectVar")%></div>
			</div>
			</td><td>
			<div id="btnClearSummerWinter" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="clearVar('SummerWinter')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "clearVar")%></div>
			</div>
			</td></tr>
			</table>
		</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_startstop", "summer_winter_rev_logic")%></td>
		<td class="td">
			<input id="cbSummerWinterRevLogic" type="checkbox" onClick="onSummerWinterRevLogic(this.checked)" <%=bean.isSummerWinterRevLogic() ? "checked" : ""%> <%=bean.getSummerWinterVar() == 0 ? "disabled" : ""%>>
			<input id="SummerWinterRevLogic" name="SummerWinterRevLogic" type="hidden" value="<%=bean.isSummerWinterRevLogic() ? "1" : "0"%>">
		</td>
		<td class="td" colspan="2">&nbsp;</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_startstop", "DiffStartSetpoint")%></td>
		<td class="td"><input type="text" id="DiffStartSetpoint" name="DiffStartSetpoint" value="<%=bean.getDiffStartSetpoint()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_startstop", "degrees")%></td>
		<td class="td" colspan="3">&nbsp;</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_startstop", "DiffStopSetpoint")%></td>
		<td class="td"><input type="text" id="DiffStopSetpoint" name="DiffStopSetpoint" value="<%=bean.getDiffStopSetpoint()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_startstop", "degrees")%></td>
		<td class="td">&nbsp;</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_startstop", "MinTimeStart")%></td>
		<td class="td"><input type="text" id="MinTimeStart" name="MinTimeStart" value="<%=bean.getMinTimeStart()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_startstop", "minutes")%></td>
		<td class="td">&nbsp;</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_startstop", "MaxTimeStart")%></td>
		<td class="td"><input type="text" id="MaxTimeStart" name="MaxTimeStart" value="<%=bean.getMaxTimeStart()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_startstop", "minutes")%></td>
		<td class="td">&nbsp;</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_startstop", "MinTimeStop")%></td>
		<td class="td"><input type="text" id="MinTimeStop" name="MinTimeStop" value="<%=bean.getMinTimeStop()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_startstop", "minutes")%></td>
		<td class="td">&nbsp;</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_startstop", "MaxTimeStop")%></td>
		<td class="td"><input type="text" id="MaxTimeStop" name="MaxTimeStop" value="<%=bean.getMaxTimeStop()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_startstop", "minutes")%></td>
		<td class="td">&nbsp;</td>
	</tr>	
</table>
</fieldset>
</form>

<div id="divVarSelection" style="display:none;">
<fieldset class="field">
	<legend class="standardTxt"><%=lang.getString("datatransfer", "var_list")%></legend>
		<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" align="center">
			<TR class='standardTxt' valign="middle">
				<TD width="12%" align="left">
					<INPUT onclick='reload_actions(1);' type='radio' id='d' name='devmodl' checked="checked"/><%=lang.getString("datatransfer","devices")%></TD>
				<TD width="*" align="left">
					<INPUT onclick="enableModelSelection();" type='radio' id='m' name='devmodl'/><%=lang.getString("datatransfer","device_models")%>
					&nbsp;
					<SELECT onchange='reload_actions(2)' class='standardTxt' id='model' name='model' disabled style='width:275px;'>
					<%=combodev.toString()%>
					</SELECT>
				</TD>
				<TD align="center" width="5%">
					<div id="btnSetVar" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="setVar()">
						<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "setVar")%></div>
					</div>
				</TD>
				<TD align="center" width="5%">
					<div id="btnSelCancel" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="onSelCancel()">
						<div class="groupCategory_small_boldtext"><%=lang.getString("opt_startstop", "cancel")%></div>
					</div>
				</TD>
			</TR>
		</TABLE>
		<table width="100%" cellpadding="0" cellspacing="2">
			<tr height="10px"></tr>
			<tr>
				<td  class='th' width="49%"><%=lang.getString("datatransfer","devices")%></td>
				<td  class='th' width="49%" ><%=lang.getString("datatransfer","variables")%></td>
				<th width='2%'>&nbsp;</th>
			</tr>
			<tr>
			<td width="49%" >
				<div id="div_dev">
					<%=div_dev.toString()%>
				</div>
			 </td>
			<td width="49%">
				<div id="div_var">
					<select name=sections id='var' multiple size=10  class='standardTxt'  style='width:100%;'>
					</select>
				</div>
			 </td>
			<td width="2%">&nbsp;</td>
		 </tr>
		</table>
</fieldset>
</div>

<input type='hidden' id='configured' value='<%=bean.isConfigured()%>'>
<input type='hidden' id='confirm' value='<%=lang.getString("opt_startstop","confirm")%>'>
<input type='hidden' id='alert_req_var' value='<%=lang.getString("opt_startstop","alert_req_var")%>'>
<input type='hidden' id='out_of_range' value='<%=lang.getString("opt_startstop","out_of_range")%>'>
