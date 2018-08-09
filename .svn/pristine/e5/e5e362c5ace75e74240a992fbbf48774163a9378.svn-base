<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.*"
import="com.carel.supervisor.presentation.io.CioEVT"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.presentation.bean.ProfileBean"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.bean.FileDialogBean"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();
	// Get modem list
	CioEVT evt = new CioEVT(sessionUser.getIdSite());
	evt.loadConfiguration();
	
	//controllo permessi	
	String disabled = "";
	boolean permission = sessionUser.isButtonActive("setio", "tab5name","SaveConfIO");
	if (!permission)
		disabled = "disabled";
	
	int idConfiguration = evt.getIdconf();
	String path = evt.getPathSound();
	String windowcomment = multiLanguage.getString("setio","windowcomment");
	
	FileDialogBean fileDlg = new FileDialogBean(request);
%>
<%=fileDlg.renderFileDialog()%>

<form name="frmcfgevent" id="frmcfgevent" method="post" action="servlet/ServUpload;jsessionid=<%=jsession%>" enctype='<%=fileDlg.enctype()%>'>
<input type="hidden" name="tipofile" value="wav" />
<input type="hidden" name="iocfgtype" value="E" />
<input type="hidden" name="iocfgid" value="<%= idConfiguration%>" />
<input type="hidden" id="msgtouser" value="<%=multiLanguage.getString("setio","msgusr")%>" />
<input type="hidden" id="confirmdel" name="confirmdel" value="<%=multiLanguage.getString("setio","message1")%>" />
<input type="hidden" id="nosoundfile" name="nosoundfile" value="<%=multiLanguage.getString("r_sitelist","nosync")%>" />
<input type="hidden" id="cmd" name="cmd" value="" />

<p class="StandardTxt"><%=windowcomment%></p>

<table border="0" width="100%" cellspacing="1" cellpadding="1">
	<tr>
		<td style="height:5px"></td>
	</tr>
	<tr>
		<td>
			<fieldset class="field">
				<legend class="standardTxt"><%=multiLanguage.getString("setio","conf")%></legend>
				<br/>
				<table border="0" width="100%" cellspacing="1" cellpadding="1">
					<tr>
						<td class="standardTxt"><%=multiLanguage.getString("setio","curr")%>:</td>
						<td>
							<input <%=disabled%> class="standardTxt" type="text" readonly size="80" name="iocfgevpath" id="iocfgevpath" value="<%=path%>"/>
						</td>
					</tr>
					<tr>
						<td class="standardTxt"><%=multiLanguage.getString("setio","sound")%>:</td>
						<td><div id="diviocfgsound" name="diviocfgsound">
						<%=fileDlg.inputLoadFile("iocfgsound", "wav", "size='80'", !permission)%>
						</div></td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>
</form>