<%@ page language="java" pageEncoding="UTF-8"
	import="java.util.ArrayList" 
	import="java.util.List"
	import="java.util.Date" 	
	import="com.carel.supervisor.presentation.dbllistbox.ListBoxElement"
	import="com.carel.supervisor.presentation.dbllistbox.DblListBox"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.bean.DeviceListBean"
	import="com.carel.supervisor.presentation.dateselect.DateSelect"
	import="com.carel.supervisor.presentation.bean.DeviceBean"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList"
	import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBean"	
	import="java.sql.Timestamp"
	import="com.carel.supervisor.base.xml.XMLNode"
	import="java.net.URL"
	import="com.carel.supervisor.base.config.ResourceLoader"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%@ page import="com.carel.supervisor.base.conversion.DateUtils" %>
<%@ page import="com.carel.supervisor.report.TemplateMgr2" %>
<%@ page import="com.carel.supervisor.report.PrinterMgr2" %>
<%@ page import="com.carel.supervisor.report.Report" %>
<%@page import="com.carel.supervisor.presentation.bean.ReportBeanListPres"%>
<%@page import="com.carel.supervisor.presentation.bean.DevMdlBeanList"%>
<%@page import="com.carel.supervisor.presentation.bean.DevMdlBean"%>


<%@page import="com.carel.supervisor.presentation.bean.BookletListBean"%>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	int idsite = sessionUser.getIdSite();
	String jsession = request.getSession().getId();
	LangService lan = LangMgr.getInstance().getLangService(language);
	
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey(); 		
	
	ReportBeanListPres.setScreenW(sessionUser.getScreenWidth());
	ReportBeanListPres.setScreenH(sessionUser.getScreenHeight());
	String htmlReportTable = ReportBeanListPres.getHTMLReportTable(idsite, language,"");
	
	
	//Combo Frequenze
	StringBuffer combo_frequency = new StringBuffer();
	combo_frequency.append("<option selected value='0'>-------------------</option>");
	combo_frequency.append("<option  value='1'> 5 s </option>");
	combo_frequency.append("<option  value='2'> 30 s </option>");
	combo_frequency.append("<option  value='3'> 1 m </option>");
	combo_frequency.append("<option  value='4'> 5 m </option>");
	combo_frequency.append("<option  value='5'> 10 m </option>");
	combo_frequency.append("<option  value='6'> 15 m </option>");
	combo_frequency.append("<option  value='7'> 30 m </option>");
	combo_frequency.append("<option  value='8'> 1 h </option>");
	combo_frequency.append("<option  value='9'> 12 h </option>");
	combo_frequency.append("<option  value='10'> 24 h </option>");
	combo_frequency.append("<option  value='-1'>");
	combo_frequency.append(lan.getString("report","custom"));
	combo_frequency.append("</option>");
	
	
	String dataselect = sessionUser.getPropertyAndRemove("dataselect");
	
	if (dataselect==null) dataselect = "week";
	
	//Combo Layout
	StringBuffer combo_layout = new StringBuffer();
	combo_layout.append("<option selected value='-99'>-------------------</option>");
	String []templateList=PrinterMgr2.getInstance().getTemplateMgr().getTemplateList();
	for(int i=0;i<templateList.length;i++){
		if((!templateList[i].contains("PDF_Alarm")) && (!templateList[i].contains("PDF_Alive")))
		{
			combo_layout.append("<option  value='");
			combo_layout.append(templateList[i]);
			combo_layout.append("'>");
			combo_layout.append(templateList[i]);
			combo_layout.append(" </option>");
		}//if
	}//for
	
	//Combo Interval
	StringBuffer combo_interval = new StringBuffer();
	combo_interval.append("<option  value='-99' >-------------------</option>");
	combo_interval.append("<option  value='0' >");
	combo_interval.append(lan.getString("report","now"));
	combo_interval.append("</option>");
	combo_interval.append("<option  value='86400' >");
	combo_interval.append(lan.getString("report","daily"));
	combo_interval.append("</option>");
	combo_interval.append("<option  value='604800' >");
	combo_interval.append(lan.getString("report","weekly"));
	combo_interval.append("</option>");
	combo_interval.append("<option  value='-1' >");
	combo_interval.append(lan.getString("report","custom"));
	combo_interval.append("</option>");
	// Alessandro : keeps tracking of last interval option and of options count
	int numIntervalOptions = 5;
	String customIntervalValue = "-1";
	String customIntervalText = lan.getString("report","custom");	
	
	// hour
	StringBuffer combo_hour = new StringBuffer();
	for(int i=0; i<24; i++) {
		if( i<10 )
			combo_hour.append("\n<option value=\"0"+i+"\">0"+i+"</option>");
		else
			combo_hour.append("\n<option value=\""+i+"\">"+i+"</option>");
	}
	// minute
	StringBuffer combo_minute = new StringBuffer();
	for(int i=0; i<60; i+=5) {
		if( i<10 )
			combo_minute.append("\n<option value=\"0"+i+"\">0"+i+"</option>");
		else
			combo_minute.append("\n<option value=\""+i+"\">"+i+"</option>");
	}
	
	//Combo Models
	StringBuffer combo_device_models = new StringBuffer();
	
	//PRIMO SELECT DEVICE IN CANNA AL SUPERVISORE
	DeviceListBean devs = new DeviceListBean(idsite,language);
	DeviceBean tmp_dev = null;
	int[] ids = devs.getIds();
	StringBuffer div_dev = new StringBuffer();
	div_dev.append("<select onclick=\"reload_actions(0);\" id=\"dev\" name='sections'  size='10' class='selectB'>");
	int device=0;
	for (int i=0;i<devs.size();i++){
		tmp_dev = devs.getDevice(ids[i]);
		div_dev.append("<option "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"' class='"+(i%2==0?"Row1":"Row2" )+"'>"+tmp_dev.getDescription()+"</option>\n");
	}
	div_dev.append("</select>");
	/////////////////////////////////////////////////////ASDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD//////////////
	
	
	//devicemodel
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
	
	String filepath = ut.remProperty("r_path");
	String maxlimit = ut.remProperty("maxlimit");
	String r_involved = ut.remProperty("r_involved");
	String basePath = BaseConfig.getCarelPath()+BaseConfig.getTemporaryFolder()+"\\";
	
	String rusure = lan.getString("report","rusure");
	String s_delete = lan.getString("report","s_delete");
	String r_action = lan.getString("report","r_action");
	String doublecode = lan.getString("report","doublecode");
	String insert_var = lan.getString("reportconf","insertavariable");
	int maxsamples = Report.MAX_SAMPLES;
	
%>
<input type='hidden' id='r_involved' value="<%=r_involved%>" />
<input type='hidden' id='s_delete' value="<%=s_delete%>" />
<input type='hidden' id='r_action' value="<%=r_action%>" />
<input type='hidden' id='rusure' value="<%=rusure%>" />
<input type='hidden' id='r_path' value="<%=filepath%>" />
<input type='hidden' id='basePath' value="<%=basePath%>" />
<input type='hidden' id='maxlimit' value="<%=maxlimit%>" />
<input type='hidden' id='maxsamples' value="<%=maxsamples%>" />
<input type='hidden' id='doublecode' value="<%=doublecode%>" />
<input type='hidden' id='insert_var' value="<%=insert_var%>" />
<input type='hidden' id='maxelements' value="<%=lan.getString("dbllistbox","maxelements")%>" />
<input type='hidden' id='confirmremoveall' value="<%=lan.getString("setaction","confirmremoveall")%>" />
<!-- Alessandro : keep tracking of interval options count -->
<input type='hidden' id='numintervaloptions' value="<%=numIntervalOptions%>" />
<input type='hidden' id='customIntervalValue' value="<%=customIntervalValue%>" />
<input type='hidden' id='customIntervalText' value="<%=customIntervalText%>" />

<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<div style='display: none; visibility: hidden;'>
	<select name="fulllayoutlist" id="fulllayoutlist">
		<%= combo_layout.toString()%>
	</select>
</div>

<FORM id="frm_report" action="servlet/master;jsessionid=<%=jsession%>" method="post">

	<TABLE border="0" >
		<TR valign="top" >
			<TD valign="top"><%=htmlReportTable%></TD>
		</TR>
	</TABLE>

	<table border="0" width="100%" style="padding-bottom: 20px;">
		<tr valign="middle">
			<td width="11%" class="standardTxt">
				<%=lan.getString("report","name") %>
			</td>
			<td width="28%" class="standardTxt">
				<input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="report_name" id="report_name" value="" size="40" maxLength="40" style="font-family:Tahoma,Verdana;font-size:8pt;" />
			</td>
			<td width="11%" class="standardTxt">
				<%=lan.getString("report","layout") %>
			</td>
			<td width="20%" class="standardTxt" >
				<select class="standardTxt" name="layout" id="layout">
					<%=combo_layout.toString()%>
				</select>
			</td>
			<td width="*" class="standardTxt" rowspan="3">
			<div id="layer_timeTable" style="visibility:hidden;">
				<table width="100%" height="100%" border="0">
					<tr valign="middle">
						<td align="center" >
						<select id="timeTableHour"  ><%=combo_hour%></select>:<select id="timeTableMinute" ><%=combo_minute%></select>
						<input type="hidden" id="timeTableValue" name="timeTableValue"> 
						</td>
						<td align="center" rowspan="3"> 
							<div id="timeTablePeriods">
								<div> 
									<table class='table' width="100%">
										<tbody>
										<tr class="th"> 
											<td id="THtimeTable00" class="tdmini" width="25%">#</td>
											<td id="THtimeTable01" class="tdmini" width="74%"><%=lan.getString("report","time")%></td>
										</tr>
										</tbody>
									</table>
								</div>
								<div id="timeTable" style="height:110px"> 
									<table style="table-layout: fixed;"><tbody></tbody></table>
								</div>
							</div>
						</td>
					</tr>
					<tr valign="middle">
					<td align="center">
						<img onclick="timeAdd()" src="images/dbllistbox/arrowdx_on.png" align="middle"><br>
					</td>
					</tr>
					<tr valign="middle">
					<td align="center">
						<img onclick="timeDelete()" src="images/dbllistbox/delete_on.png" align="middle">
					</td>
					</tr>
				</table>
			</div>			
			</td>
		</tr>
		<tr valign="middle">
			<td class="standardTxt">
				<%=lan.getString("report","interval") %>
			</td>
			<td class="standardTxt" >
				<select class="standardTxt" name="interval" id="interval" onchange="changeInterval();" >
					<%= combo_interval.toString()%>
				</select>
			</td>
			<td class="standardTxt">
				<%=lan.getString("report","frequency") %>
			</td>
			<td class="standardTxt" >
				<select class="standardTxt" disabled name="frequency" id="frequency" onChange="onSelectFrequency(options[options.selectedIndex].value)">
					<%=combo_frequency.toString()%>
				</select>
			</td>
		</tr>
		<tr valign="middle">
			<td class="standardTxt"><%=lan.getString("report","haccp") %>
			</td>
			<td>
			<input disabled class="standardTxt"  type="checkbox" name="is_haccp" value="" id="is_haccp"  size="40" maxLength="40" onclick="cleanAllSettingTables()" />
			</td>
			<td class="standardTxt"><%=lan.getString("report","output") %>
			</td>
			<td class="standardTxt">
			<select name="output" id="output" class='standardTxt' onchange='changeOutput()'>
					<option value="PDF">PDF</option>
					<option value="CSV">CSV</option>
					<option value="HTML">HTML</option>
					</select>
			</td>
		</tr>
	</table>


	<FIELDSET class='field'>		
		<LEGEND class='standardTxt'><%=lan.getString("report","context")%></LEGEND>
		<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" align="center">
			<TR class='standardTxt'>
				<TD width="11%" align="left">
					<INPUT onclick='reload_actions(1);' type='radio' id='d' name='devmodl' checked="checked"/><%=lan.getString("report","devices")%></TD>
				<TD width="*" align="left">
					<INPUT  onclick="enableModelSelection();" type='radio' id='m' name='devmodl'/><%=lan.getString("report","device_models")%>
					&nbsp;
					<SELECT onchange='reload_actions(2)' class='standardTxt' id='model' name='model' disabled style='width:275px;'>
					<%=combodev.toString()%>
					</SELECT>
				</TD>
				<TD width="5%" align="right">
						<img src="images/actions/addsmall_on_black.png" style="cursor:pointer;" onclick="addDevVar();">
				</TD>
			</TR>
		</TABLE>
		<table width="100%" cellpadding="0" cellspacing="2">
			<tr height="10px"></tr>
			<tr>
				<td  class='th' width="49%"><%=lan.getString("report","devices")%></td>
				<td  class='th' width="49%" ><%=lan.getString("report","variables")%></td>
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
					<select name=sections id='var' multiple size=10  class='selectB'>
					</select>
				</div>
			 </td>
			<td width="2%">&nbsp;</td>
		 </tr>
		</table>
	</FIELDSET>

	<p/>
	<FIELDSET class='field'>
		<LEGEND class='standardTxt'><%=lan.getString("report","current_configuration")%></LEGEND>
		<div>
			<table cellspacing="1" cellpadding="0">
				<tr class='th'>
					<th style="display:none"></th>
					<th><div style="width:250px"><%=lan.getString("report","devices")%></div></th>
					<th><div style="width:580px"><%=lan.getString("report","variables")%></div></th>
					<th><div style="width:70px"><img src='images/dbllistbox/delete_on.png' alt='<%=lan.getString("setaction", "delall")%>' onClick="deleteAllRows()"></div></th>
				</tr>
			</table>
		</div>
		<div id="div_table_global_vars" style="width:100%;height:80pt; overflow:auto;"> 
			<table id='table_global_vars' cellspacing="1" cellpadding="0" border="0">
			<tbody id='table_body_global_vars'>
			</tbody>
			</table>
		</div>
	</FIELDSET>

	<input style="display:none"  type="text" name="variables" id="variables" value=""  />
	<input style="display:none"  type="text" name="command" id="command" value=""  />
	<input style="display:none"  type="text" name="idReportSelect" id="idReportSelect" value=""  />
	<div style="display:none" id="tab" ><%=ut.getCurrentTab().trim()%></div>
	<div style="display:none" id="div_warning_name" ><%=lan.getString("report","warning_name") %></div>
	<div style="display:none" id="div_warning_frequency" ><%=lan.getString("report","warning_frequency") %></div>
	<div style="display:none" id="div_warning_max_variables" ><%=lan.getString("report","warning_max_variables") %></div>
	<div style="display:none" id="div_warning_interval" ><%=lan.getString("report","warning_interval") %></div>
	<div style="display:none" id="div_warning_layout" ><%=lan.getString("report","warning_layout") %></div>
	<div style="display:none" id="over_limits" ><%=lan.getString("report","over_limits") %></div>
</FORM>

<FORM id="frm_download" action="servlet/ServDownload;jsessionid=<%=jsession%>" method="post">

</FORM>