<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page language="java" isErrorPage="true" 
import="java.io.*"
import="com.carel.supervisor.presentation.session.* "
import="com.carel.supervisor.presentation.helper.*"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.dataconfig.ProductInfo"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.bean.FileDialogBean"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String browser  = sessionUser.getUserBrowser();
	String jsession = request.getSession().getId();
	String language =  sessionUser.getLanguage();
	LangService langmgr = LangMgr.getInstance().getLangService(language);

	String send_ok = langmgr.getString("support","success_send");
	String export_ok = langmgr.getString("support","success_export");
	String error = langmgr.getString("mgr","error");
	String conf = langmgr.getString("mgr","chkconf");
	String mailsent = langmgr.getString("support","success_send");
	
	ProductInfo pi = new ProductInfo();
	pi.load();
	String s_email_dest = pi.get("supportemail");

	FileDialogBean fileDlg = new FileDialogBean(request);
	
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	
%>

<html>
<head>
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">
  <link rel="stylesheet" href="stylesheet/plantVisor<%=browser%>.css" type="text/css" />
  <script type="text/javascript" src="scripts/app/error.js"></script>
  <script type="text/javascript" src="scripts/arch/FileDialog.js"></script>
</head>

<%=fileDlg.renderFileDialog()%>
<input type='hidden' id='vkeytype' value='PVPro' />
<input type='hidden' id='save_confirm' value='<%=langmgr.getString("fdexport","exportconfirm") %>' />
<input type='hidden' id='save_error' value="<%=langmgr.getString("fdexport","exporterror") %>" />
<form id="frm_error" action="servlet/error;jsessionid=<%=jsession%>" method="post">
<input type='hidden' id='mail_error_conf' value="<%=error + " - " + conf %>" />
<input type='hidden' id='mail_error' value="<%=error %>" />
<input type='hidden' id='mail_sent' value="<%=mailsent %>" />
	<input type='hidden' name='error_cmd' id='error_cmd' />
	<table border="0" width="100%" cellpadding="0" cellspacing="0" >
		<tr height="20">
			<td colspan="2" class="th" align="left"><b><%=langmgr.getString("support","message")%></b></td>
		</tr>
		<tr><td><p>&nbsp;</p></td></tr>
		<tr class='standardTxt' height="45px">
			<td width='60%' class='table'>
				<fieldset class="field">
				<table class='standardTxt' border='0' width="100%" align="left">
					<tr>
						<td colspan="3"><%=langmgr.getString("support","save")%></td>
					</tr>
					<tr height="50">
						<td width="45%"><%=langmgr.getString("support","support_email")%>:</td>
						<td width="50%"><input <%=(OnScreenKey?"class='keyboardInput'":"")%> class='standardTxt' type='text' name='dest_email' id='dest_email' style="width: 90%;" value='<%=s_email_dest%>' /></td>
						<td width="5%"/>
					</tr>	
					<tr>
						<td><%=langmgr.getString("support","comment")%>:</td>
						<td><textarea <%=(OnScreenKey?"class='keyboardInput'":"")%> class='standardTxt' name='mail_body' id='mail_body' rows="7" style="resize: none; overflow:auto; width: 90%;"></textarea></td>
						<td><img title='<%=langmgr.getString("support","tooltip_send")%>' style='cursor:pointer' src = "images/actions/email_on_black.png" onclick="if(document.getElementById('mail_body').value!=''){sendLogOnMail();}"/></td>
					</tr>				
				</table> 
				</fieldset>
			</td>
			<td width='40%'><span id='msg1'></span></td>
		</tr>
		<tr height="12">
			<td></td>
		</tr>
		<tr>
			<td width='60%'>
				<fieldset class="field">
				<table class='standardTxt' border='0' width="100%" align="left">
					<tr class='standardTxt' height="45px">
						<td width="45%"><%=langmgr.getString("support","send")%></td>
						<td width='50%'/>
						<td align="left" width='5%'><img title='<%=langmgr.getString("support","tooltip_export")%>'  style='cursor:pointer' src="images/actions/export_on_black.png" onclick="fdSaveFile('','zip',savelog_savefile)"/></td>
					</tr>
				</table>
				</fieldset>
			</td>
			<td width='40%'></td>
		</tr>
	</table>
</form>
</body>
</html>
