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
LightsBean bean = OptimumManager.getInstance().getLights(); 

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
String DevNameDay = "------";
String VarNameDay = "------";
if( bean.getDayVar() > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getDayVar());
	DevNameDay = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getDayVar() });
	if( vars.length > 0 )
		VarNameDay = vars[0].getShortDescription();
}
String DevNameNight = "------";
String VarNameNight = "------";
if( bean.getNightVar() > 0 ) {
	DeviceBean beanDevice = devs.retrieveDeviceOwner(bean.getNightVar());
	DevNameNight = beanDevice.getDescription();
	VarphyBean[] vars = varlist.getListVarByIds(idsite, language, new int[] { bean.getNightVar() });
	if( vars.length > 0 )
		VarNameNight = vars[0].getShortDescription();
}

boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<%=bOnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":""%>
<form id="frm_opt_settings" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<fieldset class="field"><legend class="standardTxt"><%=lang.getString("opt_lights", "alg_params")%></legend>

<table class="table" width="100%">
	<tr>
		<td class="th" width="35%"><%=lang.getString("opt_lights", "param")%></td>
		<td class="th" width="35%"><%=lang.getString("opt_lights", "value")%></td>
		<td class="th" width="*"><%=lang.getString("opt_lights", "unit")%></td>
		<td class="th" width="*"><%=lang.getString("opt_lights", "variable_select")%></td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_lights", "latitude")%></td>
		<td class="td"><input type="text" id="Latitude" name="Latitude" value="<%=bean.getLatitude()%>" maxlength="16" size="8" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_lights", "degrees")%></td>
		<td class="td">&nbsp;</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_lights", "longitude")%></td>
		<td class="td"><input type="text" id="Longitude" name="Longitude" value="<%=bean.getLongitude()%>" maxlength="16" size="8" class="<%=bOnScreenKey?"keyboardInput":"strandardTxt"%>"></td>
		<td class="td"><%=lang.getString("opt_lights", "degrees")%></td>
		<td class="td">&nbsp;</td>
	</tr>
	<tr class="Row1">
		<td class="td"><%=lang.getString("opt_lights", "day_var")%></td>
		<td class="td"><span id="NameDay"><%=DevNameDay!="------" ? (DevNameDay + " --> " + VarNameDay):"------"%></span>&nbsp;<input type="hidden" id="VarDay" name="VarDay" value="<%=bean.getDayVar()%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnDay" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('Day')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_lights", "selectVar")%></div>
			</div>
		</td>
	</tr>
	<tr class="Row2">
		<td class="td"><%=lang.getString("opt_lights", "night_var")%></td>
		<td class="td"><span id="NameNight"><%=DevNameNight!="------" ? (DevNameNight + " --> " + VarNameNight):"------"%></span>&nbsp;<input type="hidden" id="VarNight" name="VarNight" value="<%=bean.getNightVar()%>"></td>
		<td class="td">&nbsp;</td>
		<td class="td">
			<div id="btnNight" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="selectVar('Night')">
				<div class="groupCategory_small_boldtext"><%=lang.getString("opt_lights", "selectVar")%></div>
			</div>
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
						<div class="groupCategory_small_boldtext"><%=lang.getString("opt_lights", "setVar")%></div>
					</div>
				</TD>
				<TD align="center" width="5%">
					<div id="btnSelCancel" class="groupCategory_small" style="width: 140px; height: 30px;" onClick="onSelCancel()">
						<div class="groupCategory_small_boldtext"><%=lang.getString("opt_lights", "cancel")%></div>
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

<input type='hidden' id='alert_req_var' value='<%=lang.getString("opt_lights","alert_req_var")%>'>