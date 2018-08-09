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

String[] astrMonthNames = {
		lang.getString("cal", "january"),
		lang.getString("cal", "february"),
		lang.getString("cal", "march"),
		lang.getString("cal", "april"),
		lang.getString("cal", "may"),
		lang.getString("cal", "june"),
		lang.getString("cal", "july"),
		lang.getString("cal", "august"),
		lang.getString("cal", "september"),
		lang.getString("cal", "october"),
		lang.getString("cal", "november"),
		lang.getString("cal", "december")	
	};

Integer idAlgorithm = (Integer)ut.getAttribute("idalg");
if( idAlgorithm == null )
	idAlgorithm = 1;
NightFreeCoolingBean bean = OptimumManager.getInstance().getNightFreeCooling(idAlgorithm); 

// combo algorithms
StringBuffer sbAlgList = new StringBuffer(); 
NightFreeCoolingBean[] aAlg = NightFreeCoolingBean.getNightFreeCoolingList();
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
String DevNameHumiditySetpoint = "------";
String VarNameHumiditySetpoint = "------";
if( bean.getHumiditySetpointVar() > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getHumiditySetpointVar());
	DevNameHumiditySetpoint = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getHumiditySetpointVar() });
	if( vars.length > 0 )
		VarNameHumiditySetpoint = vars[0].getShortDescription();
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
String DevNameInternalHumidity = "------";
String VarNameInternalHumidity = "------";
if( bean.getInternalHumidityVar() > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getInternalHumidityVar());
	DevNameInternalHumidity = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getInternalHumidityVar()});
	if( vars.length > 0 )
		VarNameInternalHumidity = vars[0].getShortDescription();
}
String DevNameExternalHumidity = "------"; 
String VarNameExternalHumidity = "------";
if( bean.getExternalHumidityVar() > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getExternalHumidityVar());
	DevNameExternalHumidity = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getExternalHumidityVar() });
	if( vars.length > 0 )
		VarNameExternalHumidity = vars[0].getShortDescription();
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

StringBuffer sbSummerStartMonth = new StringBuffer();
for(int i = 1; i < 13; i++) {
	sbSummerStartMonth.append("<option value='" + i + (i == bean.getSummerStartMonth() ? "' selected>" : "'>"));
	sbSummerStartMonth.append(astrMonthNames[i-1]);
	sbSummerStartMonth.append("</option>\n");
}
StringBuffer sbSummerStartDay = new StringBuffer();
for(int i = 1; i < 32; i++) {
	sbSummerStartDay.append("<option value='" + i + (i == bean.getSummerStartDay() ? "' selected>" : "'>"));
	sbSummerStartDay.append(i);
	sbSummerStartDay.append("</option>\n");
}

StringBuffer sbSummerEndMonth = new StringBuffer();
for(int i = 1; i < 13; i++) {
	sbSummerEndMonth.append("<option value='" + i + (i == bean.getSummerEndMonth() ? "' selected>" : "'>"));
	sbSummerEndMonth.append(astrMonthNames[i-1]);
	sbSummerEndMonth.append("</option>\n");
}
StringBuffer sbSummerEndDay = new StringBuffer();
for(int i = 1; i < 32; i++) {
	sbSummerEndDay.append("<option value='" + i + (i == bean.getSummerEndDay() ? "' selected>" : "'>"));
	sbSummerEndDay.append(i);
	sbSummerEndDay.append("</option>\n");
}

boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<input type="hidden" id="max_instances" value="<%=ProductInfoMgr.getInstance().getProductInfo().get("opt_nightfreecooling_instances")%>">
<input type="hidden" id="max_alert" value="<%=lang.getString("opt_nightfreecooling", "max_alert")%>">
<input type='hidden' id='configured' value='<%=bean.isConfigured()%>'>
<input id="LightsConfigured" type="hidden" value="<%=OptimumManager.getInstance().getLights().isConfigured()%>">
<form id="frm_opt_nightfc_settings" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input id="cmd" name="cmd" type="hidden">
<fieldset class="field"><legend class="standardTxt"><%=lang.getString("opt_nightfreecooling", "alg_params")%></legend>
<table class="table" width="100%">
	<tr>
		<td class="standardTxt"><%=lang.getString("opt_nightfreecooling", "alg_name")%></td>
		<td class="standardTxt"><input id="Name" name="Name" type="text" size="48" maxlength="32" value="<%=bean.getAlgorithmName()%>"></td>
		<td class="standardTxt"><%=lang.getString("opt_nightfreecooling", "alg_list")%></td>
		<td class="standardTxt"><select id="AlgorithmId" name="AlgorithmId" onChange="onSelectAlgorithm(this.value)">
			<%if( idAlgorithm == 0 ) {%><option value="0" selected><%=lang.getString("opt_nightfreecooling", "new_alg")%></option><%}%>
			<%=sbAlgList.toString()%>
		</select></td>
	</tr>
</table>
<table id="tabParams" class="table" width="100%">
	<tr>
		<td class="th" width="35%"><%=lang.getString("opt_nightfreecooling", "param")%></td>
		<td class="th" width="35%"><%=lang.getString("opt_nightfreecooling", "value")%></td>
		<td class="th" width="*"><%=lang.getString("opt_nightfreecooling", "unit")%></td>
		<td class="th" width="*"><%=lang.getString("opt_nightfreecooling", "variable_select")%></td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "UnitOnOff_config_master")%></td>
		<td class="td"><span id="NameUnitOnOff"><%=DevNameUnitOnOff!="------" ? (DevNameUnitOnOff + " --> " + VarNameUnitOnOff):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOff" name="VarUnitOnOff" value="<%=bean.getUnitOnOffVar()%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOff" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOff')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "UnitOnOffCommand_config_master")%></td>
		<td class="td"><span id="NameUnitOnOffCmd"><%=DevNameUnitOnOffCmd!="------" ? (DevNameUnitOnOffCmd + " --> " + VarNameUnitOnOffCmd):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOffCmd" name="VarUnitOnOffCmd" value="<%=bean.getUnitOnOffCmdVar()%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOffCmd" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOffCmd')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
		</td>
	</tr>
	
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "slave_no")%></td>
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
		<td class="td"><%=lang.getString("opt_nightfreecooling", "Slave")%> 1 - <%=lang.getString("opt_nightfreecooling", "UnitOnOff_config")%></td>
		<td class="td"><span id="NameUnitOnOff1"><%=DevNameUnitOnOff1!="------" ? (DevNameUnitOnOff1 + " --> " + VarNameUnitOnOff1):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOff1" name="VarUnitOnOff1" value="<%=bean.getUnitOnOffVar(1)%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOff1" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOff1')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "Slave")%> 1 - <%=lang.getString("opt_nightfreecooling", "UnitOnOffCommand_config")%></td>
		<td class="td"><span id="NameUnitOnOffCmd1"><%=DevNameUnitOnOffCmd1!="------" ? (DevNameUnitOnOffCmd1 + " --> " + VarNameUnitOnOffCmd1):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOffCmd1" name="VarUnitOnOffCmd1" value="<%=bean.getUnitOnOffCmdVar(1)%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOffCmd1" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOffCmd1')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "Slave")%> 2 - <%=lang.getString("opt_nightfreecooling", "UnitOnOff_config")%></td>
		<td class="td"><span id="NameUnitOnOff2"><%=DevNameUnitOnOff2!="------" ? (DevNameUnitOnOff2 + " --> " + VarNameUnitOnOff2):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOff2" name="VarUnitOnOff2" value="<%=bean.getUnitOnOffVar(2)%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOff2" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOff2')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "Slave")%> 2 - <%=lang.getString("opt_nightfreecooling", "UnitOnOffCommand_config")%></td>
		<td class="td"><span id="NameUnitOnOffCmd2"><%=DevNameUnitOnOffCmd2!="------" ? (DevNameUnitOnOffCmd2 + " --> " + VarNameUnitOnOffCmd2):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOffCmd2" name="VarUnitOnOffCmd2" value="<%=bean.getUnitOnOffCmdVar(2)%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOffCmd2" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOffCmd2')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "Slave")%> 3 - <%=lang.getString("opt_nightfreecooling", "UnitOnOff_config")%></td>
		<td class="td"><span id="NameUnitOnOff3"><%=DevNameUnitOnOff3!="------" ? (DevNameUnitOnOff3 + " --> " + VarNameUnitOnOff3):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOff3" name="VarUnitOnOff3" value="<%=bean.getUnitOnOffVar(3)%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOff3" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOff3')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "Slave")%> 3 - <%=lang.getString("opt_nightfreecooling", "UnitOnOffCommand_config")%></td>
		<td class="td"><span id="NameUnitOnOffCmd3"><%=DevNameUnitOnOffCmd3!="------" ? (DevNameUnitOnOffCmd3 + " --> " + VarNameUnitOnOffCmd3):"------"%></span>&nbsp;<input type="hidden" id="VarUnitOnOffCmd3" name="VarUnitOnOffCmd3" value="<%=bean.getUnitOnOffCmdVar(3)%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnUnitOnOffCmd3" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('UnitOnOffCmd3')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
		</td>
	</tr>
	
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "InternalTemperature")%></td>
		<td class="td"><span id="NameInternalTemperature"><%=DevNameInternalTemperature!="------" ? (DevNameInternalTemperature + " --> " + VarNameInternalTemperature):"------"%></span>&nbsp;<input type="hidden" id="VarInternalTemperature" name="VarInternalTemperature" value="<%=bean.getInternalTemperatureVar()%>"></td>
		<td class="td"><%=lang.getString("opt_nightfreecooling", "degrees")%></td>
		<td class="td">
			<div id="btnInternalTemperature" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('InternalTemperature')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "ExternalTemperature")%></td>
		<td class="td"><span id="NameExternalTemperature"><%=DevNameExternalTemperature!="------" ? (DevNameExternalTemperature + " --> " + VarNameExternalTemperature):"------"%></span>&nbsp;<input type="hidden" id="VarExternalTemperature" name="VarExternalTemperature" value="<%=bean.getExternalTemperatureVar()%>"></td>
		<td class="td"><%=lang.getString("opt_nightfreecooling", "degrees")%></td>
		<td class="td">
			<div id="btnExternalTemperature" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('ExternalTemperature')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "InternalHumidity")%></td>
		<td class="td">
			<input type="text" id="InternalHumidity" name="InternalHumidity" value="<%=bean.getInternalHumidity()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>">
			<span id="NameInternalHumidity"><%=DevNameInternalHumidity!="------" ? (DevNameInternalHumidity + " --> " + VarNameInternalHumidity):""%></span>&nbsp;
			<input type="hidden" id="VarInternalHumidity" name="VarInternalHumidity" value="<%=bean.getInternalHumidityVar()%>">			
		</td>
		<td class="td"><%=lang.getString("opt_nightfreecooling", "percentage")%></td>
		<td class="td">
		<table cellpadding="0" cellspacing="0">
			<tr><td>
			<div id="btnInternalHumidity" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('InternalHumidity')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
			</td><td>
			<div id="btnClearInternalHumidity" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="clearVar('InternalHumidity')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "clearVar")%></div>
			</div>
			</td></tr>
			</table>
		</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "ExternalHumidity")%></td>
		<td class="td">
			<input type="text" id="ExternalHumidity" name="ExternalHumidity" value="<%=bean.getExternalHumidity()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>">
			<span id="NameExternalHumidity"><%=DevNameExternalHumidity!="------" ? (DevNameExternalHumidity + " --> " + VarNameExternalHumidity):""%></span>&nbsp;
			<input type="hidden" id="VarExternalHumidity" name="VarExternalHumidity" value="<%=bean.getExternalHumidityVar()%>">			
		</td>
		<td class="td"><%=lang.getString("opt_nightfreecooling", "percentage")%></td>
		<td class="td">
			<table cellpadding="0" cellspacing="0">
			<tr><td>
			<div id="btnExternalHumidity" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('ExternalHumidity')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
			</td><td>
			<div id="btnClearExternalHumidity" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="clearVar('ExternalHumidity')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "clearVar")%></div>
			</div>
			</td></tr>
			</table>
		</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "TemperatureSetpoint")%></td>
		<td class="td">
			<input type="text" id="TemperatureSetpoint" name="TemperatureSetpoint" value="<%=bean.getTemperatureSetpoint()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>">
			<span id="NameTemperatureSetpoint"><%=DevNameTemperatureSetpoint!="------" ? (DevNameTemperatureSetpoint + " --> " + VarNameTemperatureSetpoint):""%></span>&nbsp;
			<input type="hidden" id="VarTemperatureSetpoint" name="VarTemperatureSetpoint" value="<%=bean.getTemperatureSetpointVar()%>">			
		</td>
		<td class="td"><%=lang.getString("opt_nightfreecooling", "degrees")%></td>
		<td class="td">
			<table cellpadding="0" cellspacing="0">
			<tr><td>
			<div id="btnTemperatureSetpoint" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('TemperatureSetpoint')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
			</td><td>
			<div id="btnClearTemperatureSetpoint" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="clearVar('TemperatureSetpoint')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "clearVar")%></div>
			</div>
			</td></tr>
			</table>
		</td>
	</tr>
		<tr class="Row1">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "HumiditySetpoint")%></td>
		<td class="td">
			<input type="text" id="HumiditySetpoint" name="HumiditySetpoint" value="<%=bean.getHumiditySetpoint()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>">
			<span id="NameHumiditySetpoint"><%=DevNameHumiditySetpoint!="------" ? (DevNameHumiditySetpoint + " --> " + VarNameHumiditySetpoint):""%></span>&nbsp;
			<input type="hidden" id="VarHumiditySetpoint" name="VarHumiditySetpoint" value="<%=bean.getHumiditySetpointVar()%>">			
		</td>
		<td class="td"><%=lang.getString("opt_nightfreecooling", "percentage")%></td>
		<td class="td">
			<table cellpadding="0" cellspacing="0">
			<tr><td>
			<div id="btnHumiditySetpoint" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('HumiditySetpoint')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "selectVar")%></div>
			</div>
			</td><td>
			<div id="btnClearHumiditySetpoint" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="clearVar('HumiditySetpoint')">
			<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "clearVar")%></div>
			</div>
			</td></tr>
			</table>
		</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "TemperatureDeadband")%></td>
		<td class="td"><input type="text" name="TemperatureDeadband" value="<%=bean.getTemperatureDeadband()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_nightfreecooling", "degrees")%></td>
		<td class="td">&nbsp;</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "HumidityDeadband")%></td>
		<td class="td"><input type="text" name="HumidityDeadband" value="<%=bean.getHumidityDeadband()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_nightfreecooling", "percentage")%></td>
		<td class="td">&nbsp;</td>		
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "MaxTimeStart")%></td>
		<td class="td"><input type="text" name="MaxTimeStart" value="<%=bean.getMaxTimeStart()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_nightfreecooling", "minutes")%></td>
		<td class="td" colspan="3">&nbsp;</td>		
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "FanConsumption")%></td>
		<td class="td"><input type="text" name="FanConsumption" value="<%=bean.getFanConsumption()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_nightfreecooling", "Kwh")%></td>
		<td class="td">&nbsp;</td>		
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "FanCapacity")%></td>
		<td class="td"><input type="text" name="FanCapacity" value="<%=bean.getFanCapacity()%>" maxlength="5" size="3" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_nightfreecooling", "m3h")%></td>
		<td class="td"></td>		
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "SummerStartDate")%></td>
		<td class="td">
			<select name="SummerStartMonth" id="SummerStartMonth"><%=sbSummerStartMonth.toString()%></select>
			/
			<select name="SummerStartDay" id="SummerStartDay"><%=sbSummerStartDay.toString()%></select>
		</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "SummerEndDate")%></td>
		<td class="td">
			<select name="SummerEndMonth" id="SummerEndMonth"><%=sbSummerEndMonth.toString()%></select>
			/
			<select name="SummerEndDay" id="SummerEndDay"><%=sbSummerEndDay.toString()%></select>
		</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_nightfreecooling", "TimeOff_conf")%></td>
		<td class="td">
			<input type="hidden" id="SunriseSchedule" name="SunriseSchedule" value="<%=bean.isSunriseSchedule()?1:0%>">
			<span id="TimeOffSelection" <%if(bean.isSunriseSchedule()){%> style="display:none;" <%}%> >
			<select name="TimeOffHour"><%=sbTimeOffHour.toString()%></select>
			:
			<select name="TimeOffMinute"><%=sbTimeOffMinute.toString()%></select>
			</span>
			<span id="AutoOffIcon" <%if(!bean.isSunriseSchedule()){%> style="display:none;" <%}%>>
				<img src="images/optimum/sunrise.png">
			</span>
		</td>
		<td>&nbsp;</td>
		<td class="td">
			<table cellpadding="0" cellspacing="0">
			<tr><td>
			<div id="btnManualOff" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="onSunriseSchedule(false)">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "manual_off")%></div>
			</div>
			</td><td>
			<div id="btnAutoOff" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="onSunriseSchedule(true)">
			<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "auto_off")%></div>
			</div>
			</td></tr>
			</table>
		</td>		
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
						<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "setVar")%></div>
					</div>
				</TD>
				<TD align="center" width="5%">
					<div id="btnSelCancel" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="onSelCancel()">
						<div class="groupCategory_small_boldtext"><%=lang.getString("opt_nightfreecooling", "cancel")%></div>
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
<input type='hidden' id='confirm' value='<%=lang.getString("opt_startstop","confirm")%>'>
<input type='hidden' id='alert_req_var' value='<%=lang.getString("opt_nightfreecooling","alert_req_var")%>'>
<input type='hidden' id='alert_err_start_date' value='<%=lang.getString("opt_nightfreecooling","alert_err_start_date")%>'>
<input type='hidden' id='alert_err_end_date' value='<%=lang.getString("opt_nightfreecooling","alert_err_end_date")%>'>
<input type='hidden' id='alert_err_order_date' value='<%=lang.getString("opt_nightfreecooling","alert_err_order_date")%>'>

