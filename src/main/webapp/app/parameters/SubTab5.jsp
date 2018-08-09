<%@ page language="java" 
	
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.base.system.SystemInfoExt"
	import="com.carel.supervisor.base.config.BaseConfig"
	
	import="com.carel.supervisor.presentation.dbllistbox.DblListBox"
	import="java.util.ArrayList"
	import="com.carel.supervisor.presentation.dbllistbox.ListBoxElement"
	
	import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersList"
	import="com.carel.supervisor.plugin.parameters.dataaccess.Parameter"	
	import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersCFG"	
	import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersEventsList"	
	
	import="com.carel.supervisor.presentation.bean.DeviceBean"
	import="com.carel.supervisor.presentation.bean.DeviceListBean"
%>
<%@page import="com.carel.supervisor.presentation.bean.DevMdlBeanList"%>
<%@page import="com.carel.supervisor.presentation.bean.DevMdlBean"%>

<%
	///Pagina Variabili Controllo Parametri 

	UserSession sessionUser = ServletHelper.retrieveSession(request
			.getRequestedSessionId(), request);
	String jsession = request.getSession().getId();
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	int idsite = sessionUser.getIdSite();

	String context = lan.getString("parameters", "context");

	//PRIMO SELECT DEVICE IN CANNA AL SUPERVISORE
	// method invocation cahnged to hide 'Internal IO' device
	// Nicola Compagno 25032010
	DeviceListBean devs = new DeviceListBean(idsite, language, true);
	DeviceBean tmp_dev = null;
	int[] ids = devs.getIds();
	StringBuffer div_dev = new StringBuffer();
	int device = 0;
	for (int i = 0; i < devs.size(); i++) {
		tmp_dev = devs.getDevice(ids[i]);
		div_dev.append("<OPTION  class='"+(i%2==0?"Row1":"Row2")+" '"
				+ ((device == tmp_dev.getIddevice()) ? "selected" : "")
				+ " value='" + tmp_dev.getIddevice() + "' id='dev" + tmp_dev.getIddevice() + "'>"
				+ tmp_dev.getDescription() + "</OPTION>\n");
	}

	//devicemodel
	DevMdlBeanList devmdllist = new DevMdlBeanList();
	
	// method invocation cahnged to hide 'Internal IO' model
	// Nicola Compagno 25032010
	DevMdlBean[] devmdl = devmdllist.retrieveDevMdl(idsite, language, true);
	StringBuffer combodev = new StringBuffer();
	StringBuffer typedev = new StringBuffer();
	combodev.append("<OPTION value='-1'>-------------------</OPTION>");
	String ss = "";
	for (int i = 0; i < devmdl.length; i++) {
		DevMdlBean tmp = devmdl[i];
		combodev.append("<OPTION value=" + tmp.getIddevmdl() + " " + ss
				+ ">" + tmp.getDescription() + "</OPTION>\n");
	}
%>

<%@page import="com.carel.supervisor.plugin.parameters.ParametersMgr"%>

<FORM id="parameters_variable_form" action="servlet/master;jsessionid=<%=jsession%>" method="post">

<p class='standardTxt'><%=lan.getString("parameters", "varlist_comment")%></p>

<table class='standardTxt' cellpadding="0" cellspacing="0" width="100%" >
		<TR height="*">
		<td>
		<FIELDSET class='field' style="height:40%; width: 98%;">		
		<LEGEND class='standardTxt'><%=context%></LEGEND>
		<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" height="100%">
			<tr height="10%">
				<td>
					<TABLE border="0" cellpadding="1" cellspacing="1" width="98%" height="100%" align="center">
						<TR class='standardTxt'>
							<TD width="10%" align="left">
								<INPUT onclick='reload_actions(1);' type='radio' id='d' name='devmodl' checked="checked"/><%=lan.getString("parameters", "devices")%></TD>
							<TD width="20%" align="right">
								<INPUT  onclick="enableModelSelection();" type='radio' id='m' name='devmodl'/><%=lan.getString("parameters", "device_models")%></TD>
							<TD width="55%" align="left">
								<SELECT onchange='reload_actions(2)' class='standardTxt' id='model' name='model' disabled style='width:275px;'>
								<%=combodev.toString()%>
								</SELECT>
							</TD>
							<TD width="15%" align="right">
									<img src="images/actions/addsmall_on_black.png" style="cursor:pointer;" onclick="addDevVar();"/>
							</TD>
						</TR>
					</TABLE>
				</td>
			</tr>
			<tr><td>
				<table width="100%">
					<tr>
						<td  class='th' width="45%"><%=lan.getString("parameters", "devices")%></td>
						<td></td>
						<td  class='th' width="45%" ><%=lan.getString("parameters", "variables")%></td>
					</tr>
					<tr>
					<td width="48%" >
						<div id="div_dev">
							<select name=sections id="dev" size=10 onclick="reload_actions(0);" class='selectB'>
							<%=div_dev.toString()%>
							</select>
						</div>
					 </td>
					 <td>
					 </td>
					<td width="48%">
						<div id="div_var">
							<select name=sections id='var' multiple size=10  class='selectB'>
							</select>
						</div>
					 </td>
				 </tr>
			 </table>
			 </td>
			 </tr>
		   </table>
		</FIELDSET>
		</td>
	</TR>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>
		<FIELDSET class='field' style="height: 30%; width: 98%;">
			<LEGEND class='standardTxt'><%=lan.getString("parameters", "current_configuration")%></LEGEND>
			<table cellpadding="0" cellspacing="1" align="left" style="width:98%;">
				<tr height='18px'>
					<th class="th" align="center" style="width:47.5%;"><div ><%=lan.getString("parameters", "devices") %></div></th>
					<th class="th" align="center" style="width:47.5%;"><div ><%=lan.getString("parameters", "variables") %></div></th>
					<th class="th" align="center" style="width:5%;"><div ></div></th>
				</tr>
			</table>
			<span class="clr"></span>
			<div id="div_table_global_vars" style="width:99%;height:160pt; overflow:auto;">

			</div>
		</FIELDSET>
		</td>
	</tr>
	<tr height="15px"><td>&nbsp;</td></tr>
</table>
		
	<input style="display:none"  type="text" name="variables" id="variables" value=""  />
	<input style="display:none"  type="text" name="command" id="command" value=""  />
	<input style="display:none"  type="text" name="currentvarlist" id="currentvarlist" value=""  />
	<div style="display:none" id="div_warning_max_variables" ><%=lan.getString("parameters", "warning_max_variables")%></div>	
	<input type="hidden" name="deleteallparamquestion" id="deleteallparamquestion" value="<%=lan.getString("parameters", "deleteallparamquestion")%>" />
</FORM>
