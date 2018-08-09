<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.presentation.bean.rule.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.presentation.dbllistbox.*"
import="com.carel.supervisor.presentation.rule.*"
import="com.carel.supervisor.base.config.*"
import="java.util.*"

%>
<%@ page import="com.carel.supervisor.presentation.bean.rule.ActionBeanList" %>
<%@ page import="com.carel.supervisor.dataaccess.datalog.impl.ReportBeanList" %>
<%@ page import="com.carel.supervisor.dataaccess.datalog.impl.ReportBean" %>
<%

UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();
UserTransaction ut = sessionUser.getCurrentUserTransaction();
String notremoveaction = ut.remProperty("notremoveaction");
String actionnotremoved = lan.getString("alrsched","actionnotremoved2");

String print = lan.getString("setaction","print");
String enableprint = lan.getString("setaction","enableprint");
String window = lan.getString("setaction","window");
String enablewindow = lan.getString("setaction","enablewindow");
String action = lan.getString("setaction","action");
String nomodactionfromide = lan.getString("ide","nomodactionfromide");
String remote = lan.getString("setaction2","tab7name");
String report_store = lan.getString("setaction2","report_store");
String report_send = lan.getString("setaction2","report_send");
String report_achive_check = lan.getString("setaction","report_achive_check");
String report_send_check = lan.getString("setaction","report_send_check");
String report_box_check = lan.getString("setaction","report_box_check");
String midnightModeStr = lan.getString("setaction","midnight_mode");

//retrieve da sessione

int actioncode = Integer.parseInt(sessionUser.getProperty("actioncode"));
String action_description = ActionBeanList.getDescription(idsite,actioncode);

//Stampa e window

ActionBeanList actions = new ActionBeanList();
String is_checked_print = "";
if (actions.existAction(idsite,actioncode,"P"))
{
	is_checked_print="checked";
}

String is_checked_window = "";
if (actions.existAction(idsite,actioncode,"W"))
{
	is_checked_window="checked";
}


//Reload
String s_idreport = "-1";
int combo_active = 1;
String report_store_checked = "";
String report_send_checked = "";
String email_visibility = "hidden";
String action_type;
if( actions.existAction(idsite,actioncode,"Pr") )
    action_type = "Pr";
else if( actions.existAction(idsite,actioncode,"X") )
    action_type = "X";
else
	action_type = null;
if( action_type != null )
{
    s_idreport = actions.getActionTemplate(idsite,actioncode,action_type);
    String params = actions.getActionParameters(idsite,actioncode,action_type);
    String aparams[] = params.split(";");
    if( aparams.length >= 2 ) {
        report_store_checked    = Integer.parseInt(aparams[0]) == 1 ? "checked" : "";
        report_send_checked     = Integer.parseInt(aparams[1]) == 1 ? "checked" : "";
        email_visibility        = Integer.parseInt(aparams[1]) == 1 ? "visible" : "hidden";
    }
}

int idreport = Integer.parseInt(s_idreport);


//COMBO 

ReportBeanList reportList = new ReportBeanList();

StringBuffer combo = new StringBuffer();
String haccp_sel = "";

Map<Integer, ReportBean> report = ReportBeanList.retrieveReportForScheduledAction(idsite);

ReportBean tmp = null;
Iterator iter = report.keySet().iterator();
int cont = 0;
Integer key = null;

combo.append("<OPTION value='0'>------------</OPTION>");
int reportCount = 0;
while (iter.hasNext())
{
	reportCount++;
    key = (Integer) iter.next();
    tmp = report.get(key);
    combo.append("<OPTION "+(idreport==key.intValue()?"selected":"")+" value='"+tmp.getIdreport()+"'>"+tmp.getCode()+"</OPTION>");
}


String email = lan.getString("action","email");
String doubleElement = lan.getString("dbllistbox","doublelement");
String nullselected = lan.getString("dbllistbox","nullselected");
//20090119 - BUG 5289 RF Check on max elements
String maxelements = lan.getString("dbllistbox", "maxelements");

IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
String dailyReportMidnightStyle =  p_info.get("daily_report_midnight_style");
String midnightModeChecked = "checked";
if("false".equalsIgnoreCase(dailyReportMidnightStyle))
		midnightModeChecked = "";
//EMail
EmailListBox.setScreenH(sessionUser.getScreenHeight());
EmailListBox.setScreenW(sessionUser.getScreenWidth());
String emaillistbox = new EmailListBox().getEmailListBox(idsite,actioncode,language,action_type != null ? action_type : "Pr");
%>
<p class="tdTitleTable"><%=action%> <%=action_description%></p>

<p class="standardTxt"></p>
<INPUT type="hidden" name="nomodactionfromide" id="nomodactionfromide" value="<%=nomodactionfromide%>"/>
<INPUT type="hidden" name="notremoveaction" id="notremoveaction" value="<%=notremoveaction%>"/>
<INPUT type="hidden" name="actionnotremoved" id="actionnotremoved" value="<%=actionnotremoved%>"/>
<FORM id="frm_set_print_window" name="frm_set_print_window" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" name="cmd" id="cmd" value=""/>
<INPUT type="hidden" name="actioncode" id="actioncode" value="<%=actioncode%>"/>
<INPUT type="hidden" name="action_description" id="action_description" value="<%=action_description%>"/>
<INPUT type="hidden" id="report_achive_check" value="<%=report_achive_check%>"/>
<INPUT type="hidden" id="report_send_check" value="<%=report_send_check%>"/>
<INPUT type="hidden" id="report_box_check" value="<%=report_box_check%>"/>
<INPUT type="hidden" name="param" id="param" value=""/>
<FIELDSET class='field'>
<LEGEND class="standardTxt"><%=window%></LEGEND>
<TABLE>
	<TR valign="middle">
		<TD><INPUT <%=is_checked_window%> type="checkbox" id="chk_window" name="chk_window"></TD>
		<TD class="standardTxt"><%=enablewindow%></TD>
	</TR>
</TABLE>
</FIELDSET>

<p class="standardTxt"></p>
<FIELDSET class='field'>
<LEGEND class="standardTxt"><%=print%></LEGEND>
<TABLE>
    <TR valign="middle">
        <TD><INPUT <%=is_checked_print%> type="checkbox" id="chk_print" name="chk_print"></TD>
        <TD class="standardTxt"><%=enableprint%></TD>
    </TR>
</TABLE>
</FIELDSET>
<p class="standardTxt"></p>
<FIELDSET class='field'>
<LEGEND class="standardTxt"><%=remote%></LEGEND>
<TABLE>
    <TR class='standardTxt'>
        <TD>
            <SELECT class='standardTxt' id='report' name='report' style='width:200px'><%=combo.toString()%></SELECT>
        </TD>
    </TR>
    <TR height="10px"><TD></TD></TR>
    <TR class='standardTxt'>
        <TD width="33%"><INPUT TYPE="checkbox" id='report_store' name='report_store'  <%=report_store_checked%> <%= ( reportCount==0 ?"disabled":"" )%>> <%=report_store%></TD>
    </TR>
    <TR class='standardTxt'>
        <TD width="33%"><INPUT TYPE="checkbox" id='report_send' name='report_send' onClick="showLayer('EMailRecipients', checked)" <%=report_send_checked%> <%= ( reportCount==0 ?"disabled":"" )%> > <%=report_send%></TD>
    </TR>
    <TR class='standardTxt'>
        <TD width="33%"><INPUT TYPE="checkbox" name='midnight_mode' <%=midnightModeChecked%> <%= ( reportCount==0 ?"disabled":"" )%>> <%=midnightModeStr%></TD>
    </TR>
    <TR height="10px"><TD></TD></TR>
</TABLE>
</FIELDSET>
<p class="standardTxt"></p>
<div id="EMailRecipients" style="visibility:<%=email_visibility%>">
<table border="0" cellpadding="1" cellspacing="1" width="100%">
    <tr>
        <td>
            <FIELDSET class='field'>
            <LEGEND class="standardTxt"><%=email%></LEGEND>
	            <div style="height:96%;">
	                <%=emaillistbox%>
	            </div>
            </FIELDSET>
        </td>
    </tr>
</table>
</div>
</FORM>