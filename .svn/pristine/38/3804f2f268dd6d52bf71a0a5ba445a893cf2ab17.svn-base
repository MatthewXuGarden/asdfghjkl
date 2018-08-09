<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.IProductInfo"
import="com.carel.supervisor.base.config.ProductInfoMgr"
import="supervisor.Login"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.report.PrinterMgr2" 
import="com.carel.supervisor.presentation.io.CioPrinter"
import="com.carel.supervisor.controller.database.RuleBean"
import="com.carel.supervisor.controller.database.RuleListBean"
import="com.carel.supervisor.presentation.bo.BWizard"
import="com.carel.supervisor.presentation.bean.rule.ActionBeanList"
import="com.carel.supervisor.presentation.bean.rule.ActionBean"
import="com.carel.supervisor.dataaccess.datalog.impl.ReportBean"
import="com.carel.supervisor.dataaccess.datalog.impl.ReportBeanList"
%>

<%
	UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = userSession.getCurrentUserTransaction();
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	String language = userSession.getLanguage();
	String jsession = userSession.getSessionId();
	LangService lan = LangMgr.getInstance().getLangService(language); 
	
	//printer
	CioPrinter cio = new CioPrinter(userSession.getIdSite());
	String strDefaultPrinter = CioPrinter.GetDefaultPrinter();
	String astrPrinters[] = CioPrinter.GetPrinters();
	String strReportPrinter = cio.getReportPrinter();
	boolean reportChecked = cio.isReportPrinter();
	String opt_report_printers = "";
	if(reportChecked)
	{
		opt_report_printers = "<option value='--'>----</option>";
	}
	else
	{
		opt_report_printers = "<option value='--' checked>----</option>";
	}
	if( strDefaultPrinter.length() > 0 ) {
		String strSelected = "";
		if(reportChecked)
		{
			strSelected = strDefaultPrinter.equals(strReportPrinter) ? "selected" : "";
		}
		opt_report_printers += "<option value='" + strDefaultPrinter + "' " + strSelected + ">" + strDefaultPrinter + "</option>";
	}
	for(int i = 0; i < astrPrinters.length; i++) {
		String strSelected = "";
		if( !astrPrinters[i].equals(strDefaultPrinter) ) {
			if(reportChecked)
			{
				strSelected = astrPrinters[i].equals(strReportPrinter) ? "selected" : "";
			}
			opt_report_printers += "<option value='" + astrPrinters[i] + "' " + strSelected + ">" + astrPrinters[i] + "</option>";
		}
	}
	
	//daily time
	StringBuffer bodySelectHoursFrom= new StringBuffer();
	for(int i=0;i<24;i++)
	{
		if(i<10)
			bodySelectHoursFrom.append("\n<option value=\"0"+i+"\">0"+i+"</option>");
		else
			bodySelectHoursFrom.append("\n<option value=\""+i+"\">"+i+"</option>");
	}
	StringBuffer bodySelectMinuts= new StringBuffer();
	bodySelectMinuts.append("\n<option value=\"00\">00</option>");
	for(int i=1;i<6;i++)
	{
		bodySelectMinuts.append("\n<option value=\""+i*10+"\">"+i*10+"</option>");	
	}
	
	String now = lan.getString("report","now");
%>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<!-- 0:add  1:modify -->
<input type='hidden' id='cmd' value=''/>
<input type="hidden" id="isrestart" value="true">
<input type='hidden' id='now' value='<%=now %>'/>
<input type='hidden' id="div_warning_name" value='<%=lan.getString("report","warning_name") %>'/>
<input type='hidden' id="wizardconfigfinish" value='<%= lan.getString("wizard","wizardconfigfinish") %>'/>
<input type="hidden" id="writecorrectemail" value="<%=lan.getString("setio","writecorrectemail")%>">
<input type="hidden" id="actionnotremoved" value="<%=lan.getString("alrsched","actionnotremoved2")%>">
<input type="hidden" id="noremovemsg" value="<%=lan.getString("timebandsMsg","noremovemsg")%>">
<input type='hidden' id='OnScreenKey' value='<%=OnScreenKey %>'/>
<input type='hidden' id='idreport' value=''/>
<input type='hidden' id='idaction' value=''/>
<input type='hidden' id='idtimeBand' value=''/>
<!--  <input type='hidden' id='action_param' value=''/> -->
<input type='hidden' id='sev_emails'/>
<table  width='100%' cellpadding=3 cellspacing=2 border=0>
	<tr>
		<td class="standardTxt" colspan=4><%=lan.getString("wizard","haccpreportcomment") %></td>
	</tr>
	<tr>
		<td>
			<FIELDSET class="field">
			<LEGEND class="standardTxt"><%=lan.getString("wizard","setting")%></LEGEND>
			<div>
				<table width="100%" border=0>
					<tr>
						<td class='standardTxt' width='18%'><%=lan.getString("report","name") %>
						</td>
						<td class='standardTxt' width='1px'>
							<input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="report_name" id="report_name" value="<%=lan.getString("wizard","dailyreport")%>" size="37" maxLength="40" class="standardTxt" onchange="checkActiverule();"/>
						</td>
						<td width='5%'>
						</td>
						<td class='standardTxt' width='13%'>
						</td>
						<td class='standardTxt'>
							<select class="standardTxt" disabled name="layoutSelect" id="layoutSelect" style="display:none;">
							</select>
							<input type='hidden' id="layout" name='layout' value=''/>
						</td>
					</tr>
					<tr>
						<td class='standardTxt'><%=lan.getString("wizard","activerule") %></td>
						<td align="left"><input type="checkbox" id="activerule" value="1"></td>
					</tr>
					<tr>
						<td class='standardTxt'><%=lan.getString("wizard","time") %>
						</td>
						<td class='standardTxt' align='left'>
							<table width='100%' cellpadding=0 cellspacing=0 border=0>
								<tr>
									<td class='standardTxt'>
										<select class="standardTxt" disabled name="intervalSelect" id="intervalSelect" style="display:none;" >
										</select>
										<input type='hidden' id="interval" name='interval' value=''/>
										<div id='dailyTimebind'>
											<select id="hour_from" class='standardTxt' onchange="checkActiverule();"><%=bodySelectHoursFrom.toString()%></select>
											:
											<select id="minut_from" class='standardTxt' onchange="checkActiverule();"><%=bodySelectMinuts.toString()%></select>
										</div>
										<div id='otherTimebind' >	
										</div>
									</td>
								</tr>
							</table>	
						</td>
						<td>
						</td>
						<td class='standardTxt'>
						</td>
						<td class='standardTxt'>
							<select disabled class='standardTxt' id="outputSelect" style="display:none;">
							</select>
							<input type='hidden' id="output" name="output" value=''/>
						</td>
					</tr>
					<tr style="display:none;">
						<td class='standardTxt'><%=lan.getString("report","haccp") %>
						</td>
						<td class='standardTxt'>
							<input disabled type="checkbox" checked id="is_haccpCheckbox"/>
							<input type='hidden' id="is_haccp" name='is_haccp' value=''/>
						</td>
						<td>
						</td>
						<td class='standardTxt'>&nbsp;</td>
						<td class='standardTxt'>&nbsp;</td>
					</tr>
					<tr style="display:none;">
						<td class='standardTxt'><%=lan.getString("report","frequency") %>
						</td>
						<td class='standardTxt'>
							<select class="standardTxt" disabled name="frequencySelect" id="frequencySelect">
							</select>
							<input type='hidden' id="frequency" name="frequency" value=''/>
						</td>
						<td>
						</td>
						<td class='standardTxt'>&nbsp;</td>
						<td class='standardTxt'>&nbsp;</td>
					</tr>
					<tr>
						<td class='standardTxt'><%=lan.getString("wizard", "selectprinter") %>
						</td>
						<td class='standardTxt'>
							<select class="standardTxt" id="report_printerSelect" onchange="checkActiverule();">
								<%=opt_report_printers%>
							</select>
						</td>
						<td>
						</td>
						<td class='standardTxt'>&nbsp;</td>
						<td class='standardTxt'>&nbsp;</td>
					</tr>
					<tr>
						<td class='standardTxt' valign='top'><%=lan.getString("wizard","sendbyemail") %></td>
						<td align='right'>
							<table width='100%' cellpadding=0 cellspacing=0 border=0>
								<tr>
									<td class="standardTxt" width="200px;">
										<input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="addMail" id="addMail" style="width:200px;" value="" <%=(OnScreenKey?"":" onblur='checkOnlyMail(this);'")%>/>
									</td>
									<td style="padding-top:2px;">
										<img src="images/actions/addsmall_on_black.png" style="cursor:pointer;" onclick="addNewRow('mailbody','emails','addMail',null,'E');"/>
									</td>
								</tr>
							</table>
						</td>
						<td>
						</td>
						<td class='standardTxt'>&nbsp;</td>
						<td class='standardTxt'>&nbsp;</td>
					</tr>
				</table>
			</div>
			</FIELDSET>
		</td>
	</tr>
	<tr>
		<td colspan="4">
			<table width="97%" cellspacing='1px' cellpadding='0px' class='table'>
				<tbody>
					<tr class='th'>
						<th width='*'><%=lan.getString("wizard","confmail")%></th>
						<th width='20' id="mail_img"></th>
					</tr>
				</tbody>
				<tbody  id="mailbody"></tbody>
			</table>		
		</td>
	</tr>
</table>