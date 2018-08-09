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
	import="com.carel.supervisor.presentation.bean.FileDialogBean"
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
	
	
	//multilingua per calendario

	String dom = lan.getString("cal","sun");
	String lun = lan.getString("cal","mon");
	String ma = lan.getString("cal","tue");
	String mer = lan.getString("cal","wed");
	String gio = lan.getString("cal","thu");
	String ven = lan.getString("cal","fri");
	String sab = lan.getString("cal","sat");

	String gen = lan.getString("cal","january");
	String feb = lan.getString("cal","february");
	String mar = lan.getString("cal","march");
	String apr = lan.getString("cal","april");
	String mag = lan.getString("cal","may");
	String giu = lan.getString("cal","june");
	String lug = lan.getString("cal","july");
	String ago = lan.getString("cal","august");
	String set = lan.getString("cal","september");
	String ott = lan.getString("cal","october");
	String nov = lan.getString("cal","november");
	String dic = lan.getString("cal","december");
	
	

	String from = lan.getString("alrsearch","from");
	String to = lan.getString("alrsearch","to");
	String context = lan.getString("report","context");
	
	
	
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
	div_dev.append("<select onchange=\"\" ondblclick=\"reload_actions(0);\" id=\"dev\" name='sections'  size='10' class='standardTxt' style='width:100%;' >");
	int device=0;
	for (int i=0;i<devs.size();i++){
		tmp_dev = devs.getDevice(ids[i]);
		div_dev.append("<option "+((device==tmp_dev.getIddevice())?"selected":"")+" value='"+tmp_dev.getIddevice()+"'>"+tmp_dev.getDescription()+"</option>\n");
	}
	div_dev.append("</select>");
	/////////////////////////////////////////////////////ASDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD//////////////
	
	
	//stringhe multilingua
	
	
	
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
	String commit_btn = ut.remProperty("commit_btn");	
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
	FileDialogBean fileDlg = new FileDialogBean(request);
	
%>
<input type='hidden' id='r_involved' value="<%=r_involved%>" />
<input type='hidden' id='s_delete' value="<%=s_delete%>" />
<input type='hidden' id='r_action' value="<%=r_action%>" />
<input type='hidden' id='rusure' value="<%=rusure%>" />
<input type='hidden' id='r_path' value="<%=filepath%>" />
<input type='hidden' id='commit_btn' value="<%=commit_btn%>" />
<input type='hidden' id='basepath' value="<%=basePath%>" />
<input type='hidden' id='maxlimit' value="<%=maxlimit%>" />
<input type='hidden' id='maxsamples' value="<%=maxsamples%>" />
<input type='hidden' id='doublecode' value="<%=doublecode%>" />
<input type='hidden' id='insert_var' value="<%=insert_var%>" />
<input type='hidden' id='maxelements' value="<%=lan.getString("dbllistbox","maxelements")%>" />
<input type='hidden' id='save_confirm' value="<%=lan.getString("fdexport","exportconfirm") %>" />
<input type='hidden' id='save_error' value="<%=lan.getString("fdexport","exporterror") %>" />


<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<div style='display: none; visibility: hidden;'>
	<select name="fulllayoutlist" id="fulllayoutlist">
		<%= combo_layout.toString()%>
	</select>
</div>

<form id="frm_report" action="servlet/master;jsessionid=<%=jsession%>" method="post">

<table border="0" >
	<tr valign="top" >
		<td valign="top"><%=htmlReportTable%></td>
	</tr>
</table>
	
<br>

<table>
<tr valign="top" class="standardTxt" >
<td>
<div id='calendar1_to_hide' style='visibility:hidden; display:none;' >
<table>
	<tr>
		<td class="standardTxt" align="center" width="50px">
			<div id='s_from' style='visibility:hidden; display:none;'><%=from%></div>
			<div id='s_to' style='visibility:hidden; display:none;'><%=to%></div>
		</td>
		<td class="standardTxt" align="center" valign="top">
			<input type="hidden" name="tester" id="tester" value=""/>
			<input class="standardTxt" disabled type="text" name="tester_year" id="tester_year" value="" size="4" maxLength="4" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'/>
			<select class="standardTxt" disabled name="tester_month" id="tester_month">
				<option value="1"><%=gen%></option>
				<option value="2"><%=feb%></option>
				<option value="3"><%=mar%></option>
				<option value="4"><%=apr%></option>
				<option value="5"><%=mag%></option>
				<option value="6"><%=giu%></option>
				<option value="7"><%=lug%></option>
				<option value="8"><%=ago%></option>
				<option value="9"><%=set%></option>
				<option value="10"><%=ott%></option>
				<option value="11"><%=nov%></option>
				<option value="12"><%=dic%></option>
			</select>
			<input class="standardTxt" disabled type="text" name="tester_day" id="tester_day" value="" size="2" maxLength="2" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'/>
			<p></p>
			<div id="cal_tester_display"></div>
		</td>
		<td class="standardTxt" align="center" width="50px" id='to_to_hide' style='visibility:hidden; display:none;'>
			<%=to%>
		</td>
		<td class="standardTxt" align="center" valign="top" id='calendar2_to_hide' style='visibility:hidden; display:none;'>
			<input type="hidden" name="tester2" id="tester2" value=""/>
			<input class="standardTxt" disabled type="text" name="tester2_year" id="tester2_year" value="" size="4" maxLength="4" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'/>
			<select class="standardTxt" disabled name="tester2_month" id="tester2_month">
				<option value="1"><%=gen%></option>
				<option value="2"><%=feb%></option>
				<option value="3"><%=mar%></option>
				<option value="4"><%=apr%></option>
				<option value="5"><%=mag%></option>
				<option value="6"><%=giu%></option>
				<option value="7"><%=lug%></option>
				<option value="8"><%=ago%></option>
				<option value="9"><%=set%></option>
				<option value="10"><%=ott%></option>
				<option value="11"><%=nov%></option>
				<option value="12"><%=dic%></option>
			</select>
			<input class="standardTxt" disabled type="text" name="tester2_day" id="tester2_day" value="" size="2" maxLength="2" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);' />
			<p></p>
			<div id="cal_tester2_display"></div>
	     </td>
		</tr>	
	</table>
</div>
</td>
<td width="5px">&nbsp;</td>
<td align="center"">
	<table border="0" id='freq_to_hide' style='visibility:hidden;' cellpadding="2" cellspacing="2">
		<tr valign="top">
			<td class="standardTxt">
				<%=lan.getString("report","frequency")%>
				&nbsp;
				<select class="standardTxt" name="frequency" id="frequency" onChange="onSelectFrequency(options[options.selectedIndex].value)">
					<%= combo_frequency.toString()%>
				</select>
				<p></p>
		<div id="layer_timeTable">
			<table width="100%" height="100%" border="0">
				<tr valign="middle">
					<td align="center" >
						<select id="timeTableHour"><%=combo_hour%></select>:<select id="timeTableMinute"><%=combo_minute%></select>
						<input type="hidden" id="timeTableValue" name="timeTableValue"> 
					</td>
					<td align="center" rowspan="3"> 
						<div id="timeTablePeriods">
							<div> 
								<table class="table" style="width:100%">
									<tbody>
									<tr class="th"> 
										<td id="THtimeTable00" class="tdmini" width="25%">#</td>
										<td id="THtimeTable01" class="tdmini" width="74%"><%=lan.getString("report","time")%></td>
									</tr>
									</tbody>
								</table>
							</div>
							<div id="timeTable" style="height:300px"> 
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
		</td></tr>		
	</table>
</td>
</tr>	
</table>

	<input style="display:none"  type="text" name="variables" id="variables" value=""  />
	<input style="display:none"  type="text" name="command" id="command" value=""  />
	<input style="display:none"  type="text" name="islocal" id="islocal" value=""  />
	<input style="display:none"  type="text" name="exportfilepath" id="exportfilepath" value=""  />
	<input style="display:none"  type="text" name="exportfilename" id="exportfilename" value=""  />
	<input style="display:none"  type="text" name="idReportSelect" id="idReportSelect" value=""  />
	<div style="display:none" id="tab" ><%=ut.getCurrentTab().trim()%></div>
	<div style="display:none" id="div_warning_name" ><%=lan.getString("report","warning_name") %></div>
	<div style="display:none" id="div_warning_frequency" ><%=lan.getString("report","warning_frequency") %></div>
	<div style="display:none" id="div_warning_max_variables" ><%=lan.getString("report","warning_max_variables") %></div>
	<div style="display:none" id="div_warning_interval" ><%=lan.getString("report","warning_interval") %></div>
	<div style="display:none" id="div_warning_layout" ><%=lan.getString("report","warning_layout") %></div>
	<div style="display:none" id="over_limits" ><%=lan.getString("report","over_limits") %></div>
</form>
<%=fileDlg.renderFileDialog()%>
<form id="frm_download" action="servlet/ServDownload;jsessionid=<%=jsession%>" method="post">

</form>
<!-- javascript here because canlender.js will use cal1 -->
<script language="javascript">
function renderCalendar1()
{
	var arDay = new Array("<%=dom%>","<%=lun%>","<%=ma%>","<%=mer%>","<%=gio%>","<%=ven%>","<%=sab%>");
	var arMonth = new Array("<%=gen%>","<%=feb%>","<%=mar%>","<%=apr%>","<%=mag%>","<%=giu%>","<%=lug%>","<%=ago%>","<%=set%>","<%=ott%>","<%=nov%>","<%=dic%>");
	cal1 = new Calendar ("cal1", "tester", new Date(), arDay, arMonth);
	renderCalendar(cal1);
}
function renderCalendar1ForWeekly()
{
	var arDay = new Array("<%=dom%>","<%=lun%>","<%=ma%>","<%=mer%>","<%=gio%>","<%=ven%>","<%=sab%>");
	var arMonth = new Array("<%=gen%>","<%=feb%>","<%=mar%>","<%=apr%>","<%=mag%>","<%=giu%>","<%=lug%>","<%=ago%>","<%=set%>","<%=ott%>","<%=nov%>","<%=dic%>");
	var dateO = new Date();
	dateO.setDate(dateO.getDate()-7)
	cal1 = new Calendar ("cal1", "tester", dateO, arDay, arMonth);
	renderCalendar(cal1);
}
function renderCalendar2()
{
	var arDay = new Array("<%=dom%>","<%=lun%>","<%=ma%>","<%=mer%>","<%=gio%>","<%=ven%>","<%=sab%>");
	var arMonth = new Array("<%=gen%>","<%=feb%>","<%=mar%>","<%=apr%>","<%=mag%>","<%=giu%>","<%=lug%>","<%=ago%>","<%=set%>","<%=ott%>","<%=nov%>","<%=dic%>");
	cal2 = new Calendar ("cal2", "tester2", new Date(), arDay, arMonth);
	renderCalendar(cal2);
}

renderCalendar1();
renderCalendar2();
</script>