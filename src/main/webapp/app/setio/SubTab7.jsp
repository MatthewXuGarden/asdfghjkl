<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.io.CioPrinter"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.presentation.bean.ProfileBean"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String jsession = request.getSession().getId();
	String language = sessionUser.getLanguage();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	String strTranReportPrinter = multiLanguage.getString("setio", "report_printer");
	String strTranAlarmPrinter = multiLanguage.getString("setio", "alarm_printer");
	String strTranEnablePrinting = multiLanguage.getString("setio", "enable_printing");
	
	String disabled = "";
	boolean permission = sessionUser.isButtonActive("setio", "tab7name","SaveConfIO");
	if (!permission)
		disabled = "disabled";

	CioPrinter cio = new CioPrinter(sessionUser.getIdSite());
	String strDefaultPrinter = CioPrinter.GetDefaultPrinter();
	String astrPrinters[] = CioPrinter.GetPrinters();
	// report printer
	String strReportPrinterChecked = cio.isReportPrinter() ? "checked" : "";
	String strReportPrinter = cio.getReportPrinter();
	String opt_report_printers = "";
	if( strDefaultPrinter.length() > 0 ) {
		String strSelected = strDefaultPrinter.equals(strReportPrinter) ? "selected" : ""; 
		opt_report_printers = "<option value='" + strDefaultPrinter + "' " + strSelected + ">" + strDefaultPrinter + "</option>";
	}
	for(int i = 0; i < astrPrinters.length; i++) {
		if( !astrPrinters[i].equals(strDefaultPrinter) ) {
			String strSelected = astrPrinters[i].equals(strReportPrinter) ? "selected" : ""; 
			opt_report_printers += "<option value='" + astrPrinters[i] + "' " + strSelected + ">" + astrPrinters[i] + "</option>";
		}
	}
	// alarm printer
	String strAlarmPrinterChecked = cio.isAlarmPrinter() ? "checked" : "";
	String strAlarmPrinter = cio.getAlarmPrinter();
	String opt_alarm_printers = "";
	if( strDefaultPrinter.length() > 0 ) {
		String strSelected = strDefaultPrinter.equals(strAlarmPrinter) ? "selected" : ""; 
		opt_alarm_printers = "<option value='" + strDefaultPrinter + "' " + strSelected + ">" + strDefaultPrinter + "</option>";
	}
	for(int i = 0; i < astrPrinters.length; i++) {
		if( astrPrinters[i].compareTo(strDefaultPrinter) != 0 ) {
			String strSelected = astrPrinters[i].equals(strAlarmPrinter) ? "selected" : ""; 
			opt_alarm_printers += "<option value='" + astrPrinters[i] + "' " + strSelected + ">" + astrPrinters[i] + "</option>";
		}
	}
%>
<form name="frmcfgprinter" id="frmcfgprinter" method="post" action="servlet/master;jsessionid=<%=jsession%>">
<table border="0" width="100%" cellspacing="1" cellpadding="1">

<tr><td><fieldset class="field">
<legend class="standardTxt"><%=strTranReportPrinter%></legend>
<table width="100%">
	<tr><td style="height:5px"></td></tr>
	<tr>
		<td class="standardTxt" width="40%"><select name="sel_report_printer" <%=disabled%>><%=opt_report_printers%></select></td>
		<td class="standardTxt"><input name="cb_report_printer" type="checkbox" <%=strReportPrinterChecked%> <%=disabled%>>&nbsp;<%=strTranEnablePrinting%></td>
	</tr>
	<tr><td style="height:5px"></td></tr>
</table>
</fieldset></td></tr>

<tr><td style="height:10px"></td></tr>

<tr><td><fieldset class="field">
<legend class="standardTxt"><%=strTranAlarmPrinter%></legend>
<table width="100%">
	<tr><td style="height:5px"></td></tr>
	<tr>
		<td class="standardTxt" width="40%"><select name="sel_alarm_printer" <%=disabled%>><%=opt_alarm_printers%></select></td>
		<td class="standardTxt"><input name="cb_alarm_printer" type="checkbox" <%=strAlarmPrinterChecked%> <%=disabled%>>&nbsp;<%=strTranEnablePrinting%></td>
	</tr>
	<tr><td style="height:5px"></td></tr>
</table>
</fieldset></td></tr>

</table>
</form>
