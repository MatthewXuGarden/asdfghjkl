<%@ page language="java" import="java.util.*" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.*"
import="com.carel.supervisor.presentation.io.CioMAIL"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.presentation.bean.ProfileBean"
import="com.carel.supervisor.presentation.bo.BSetIo"
import="com.carel.supervisor.dispatcher.book.DispatcherBook"
import="com.carel.supervisor.dispatcher.DispatcherMgr"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr"
import="com.carel.supervisor.dataaccess.dataconfig.SystemConf"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUserLoc = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	BSetIo myBo = (BSetIo)trxUserLoc.getBoTrx();
	String jsession = request.getSession().getId();
	String risultato = trxUserLoc.remProperty("state");
	if(risultato != null && risultato.equalsIgnoreCase("ko"))
		risultato = multiLanguage.getString("setio","message");
	else
		risultato = "";
		
	//controllo permessi	
	String disabled = "";
	boolean permission = sessionUser.isButtonActive("setio", "tab3name","SaveConfIO");
	if (!permission)
		disabled = "disabled";
		
	
	CioMAIL mail = new CioMAIL(sessionUser.getIdSite());
	mail.loadConfiguration();
	
	int idConfiguration = mail.getIdConf();
	String smtp = mail.getSmtp();
	int port = mail.getPort();
	String sender = mail.getSender();
	String type = mail.getType();
	String provider = mail.getProviderId();
	int trynumber = mail.getTrynum();
	int retryafter = mail.getRetrynum();
	
	String smtpUser = mail.getUser();
	String smtpPass = mail.getPass();
	
	String encryption = mail.getEncryption().equalsIgnoreCase("NONE") ? "" : "checked";
	
	String[][] listprovider = mail.getProvider();
	myBo.setScreenH(sessionUser.getScreenHeight());
	myBo.setScreenW(sessionUser.getScreenWidth());
	String tab = myBo.getBookAddress(language,"E");
	
	DispatcherBook[] addrBook = DispatcherMgr.getInstance().getReceiverInfoByType("E");
	int numAddr = addrBook.length;
	
	String dispDiv = "";
	if(type.equalsIgnoreCase("L") || type.equalsIgnoreCase(""))
		dispDiv = "disabled";
	
	String msguser1 = multiLanguage.getString("setio","message1");
	String emailcomment = multiLanguage.getString("setio","emailcomment");
	String noremaddressfromide = multiLanguage.getString("alrsched","noremaddressfromide");
	String controlconfig = multiLanguage.getString("setio","controlconfig");
	String automsgconf = multiLanguage.getString("setio","automaticmsg");
	String writecorrectemail = multiLanguage.getString("setio","writecorrectemail");
	String automsgconf2 = multiLanguage.getString("setio","automaticmsg2");
	// User e Pass per SMTP authentication
	String user=multiLanguage.getString("ldapPage","user");
	String password=multiLanguage.getString("ldapPage","password");
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	
	String requestUsername = multiLanguage.getString("setldap","s_insertname");
	String requestPassword = multiLanguage.getString("setldap","s_insertpassw");
	
	String logEnable = "";
	SystemConf log = SystemConfMgr.getInstance().get("email_log");
	if(log !=null){
		logEnable = (log.getValue().equals("TRUE"))?"checked":"";
	}
%>
<input type="hidden" id="writecorrectemail" value="<%=writecorrectemail%>">
<input type="hidden" id="msgautomatic" value="<%=automsgconf%>">
<input type="hidden" id="msgautomatic2" value="<%=automsgconf2%>"/>
<input type='hidden' id='controlconfig' value='<%=controlconfig%>'>
<input type='hidden' id='noremaddressfromide' value='<%=noremaddressfromide%>'>
<input type="hidden" id="numAddr" value="<%=numAddr%>"/>
<form name="frmcfgmail" id="frmcfgmail" method="post" action="servlet/master;jsessionid=<%=jsession%>">
<input type="hidden" id="executetechact" name="executetechact" value="F">
<input type="hidden" name="iocfgtype" value="M">
<input type="hidden" name="iocfgid" value="<%= idConfiguration%>">
<input type="hidden" id="iomsguser" value="<%=msguser1%>">
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>

<input type="hidden" id="requestUsername" value="<%= requestUsername%>">
<input type="hidden" id="requestPassword" value="<%= requestPassword%>">

<p class="StandardTxt"><%=emailcomment%></p>

<table border="0" width="100%" cellspacing="1" cellpadding="1">

	<tr>
		<td>
			<fieldset class="field">
				<legend class="standardTxt"><%=multiLanguage.getString("setio","conf")%></legend>
				
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td width="50%" valign="top">
							<table border="0" width="100%" cellspacing="5" cellpadding="2">
								<tr>
									<td class="standardTxt" width="30%"><%=multiLanguage.getString("setio","smtp")%>:</td>
									<td class="standardTxt" width="70%"><input <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' name="iomailsmtp" id="iomailsmtp" size="25" maxlength="100" value="<%=smtp%>" onblur="IoEnableTechE();"/> *
									&nbsp;<%=multiLanguage.getString("setio","port")%>:&nbsp;<input <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' name="iomailport" id="iomailport" size="5" maxlength="5" value="<%=port%>" onblur="IoEnableTechE();"/> *
									</td>
								</tr>
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","mit")%>:</td>
									<td class="standardTxt"><input <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' name="iomailsender" id="iomailsender" size="25" maxlength="100" value="<%=sender%>" <%=(OnScreenKey?"":" onblur='checkOnlyMail(this);'")%>/> *</td>
								</tr>
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","enc_auth")%>:</td>
									<td class="standardTxt"><input <%=disabled%> type="checkbox" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' name="iomailencryption" id="iomailencryption" <%=encryption%> onblur="IoEnableTechE();" onClick="IoTechEEnc(this.checked)"/></td>
								</tr>
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","numtry")%>:</td>
									<td class="standardTxt"><input <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' size="4" maxlength="2" name="iotrynum" id="iotrynum" value="<%= trynumber>0?trynumber:0%>" onblur="onlyNumberOnBlur(this);" onkeydown="checkOnlyNumber(this,event);"/></td>
								</tr>
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","retry")%>:</td>
									<td class="standardTxt"><input <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' size="4" maxlength="2" name="ioretryafter" id="ioretryafter" value="<%= retryafter>0?retryafter:0%>" onblur="onlyNumberOnBlur(this);" onkeydown="checkOnlyNumber(this,event);"/> <%=multiLanguage.getString("setio","min")%></td>
								</tr>
							</table>
						</td>
						<td width="50%" valign="top">
							<table border="0" width="100%" cellspacing="5" cellpadding="2">
								<tr>
									<td class="standardTxt" width="30%"><%=multiLanguage.getString("setio","coll")%>:</td>
									<td class="standardTxt"><nobr>
										<input <%=disabled%> onclick="IO_changeMailSend(this);" type="radio" name="iomailtype" id="iomailtypeL" value="L" <%= (type.equalsIgnoreCase("L")||type.equalsIgnoreCase(""))?"checked":""%> >LAN
										<input <%=disabled%> onclick="IO_changeMailSend(this);" type="radio" name="iomailtype" id="iomailtypeD" value="D" <%= type.equalsIgnoreCase("D")?"checked":""%> >DialUp
										</nobr>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<div id="setioDiv" style="visibility:visible;">
											<table border="0" width="100%" cellspacing="0" cellpadding="0">
												<tr>
													<td class="standardTxt" width="30.5%"><%=multiLanguage.getString("setio","prov")%>:</td>
													<td>
														<select <%=disabled%> name="iomailprovider" id="iomailprovider" class="standardTxt" <%=dispDiv%>>
														<option value="nop">------------------------------------</option>
															<%for(int i=0; i<listprovider.length; i++){%>
															<option value="<%=listprovider[i][1]%>|<%=listprovider[i][0]%>" <%= provider.equalsIgnoreCase(listprovider[i][1])?"selected":""%> ><%= listprovider[i][1]%></option>
															<%}%>
														</select>
													</td>
												</tr>
											</table>
										</div>
									</td>
								</tr>
								
								<tr>
									<td class="standardTxt" width="30%"><%=user%>:</td>
									<td class="standardTxt">
										<input <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' 
											   name="iomailsenderuser" id="iomailsenderuser" 
											   size="25" maxlength="100" value="<%=smtpUser%>" onblur=""/>
									</td>
								</tr>
								
								<tr>
									<td class="standardTxt" width="30%"><%=password%>:</td>
									<td class="standardTxt">
										<input <%=disabled%> type="password" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' 
											name="iomailsenderpass" id="iomailsenderpass" size="25" maxlength="100" 
											value="<%=smtpPass%>" onblur=""/>
									</td>
								</tr>
								
								<tr>
									<td class="standardTxt" width="30%"><%=multiLanguage.getString("setio","log")%>:</td>
									<td class="standardTxt">
										<input type="checkbox" name="email_log" id="email_log" <%=logEnable %>/>
									</td>
								</tr>
								
							</table>	
						</td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
	<tr>
		<td style="height:5px"></td>
	</tr>
	<tr>
		<td>
			<fieldset class="field">
				<legend class="standardTxt"><%=multiLanguage.getString("setio","book")%></legend>
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td valign="middle" width="30%">
							<table border="0" width="100%" cellspacing="5" cellpadding="1">
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","rif")%>:</td>
									<td><input <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' name="ioflabel" id="ioflabel" size="35" maxlength="50" onpaste="" onkeydown="checkLettNum(this,event);"/></td>
								</tr>
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","add")%>:</td>
									<td><input <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' name="iofaddress" id="iofaddress" size="35" maxlength="50" <%=(OnScreenKey?"":" onblur='checkOnlyMail(this);'")%>/></td>
								</tr>
							</table>		
						</td>
						<td valign="middle" width="10%">
							<table width="100%" border="0" cellspacing="5" cellpadding="1">
								<tr>
									<td align="center">
										<img <%=(permission?"onclick=\"IO_addContatToBook(true);return false;\"":"")%> src=<%=(permission?"images/dbllistbox/arrowdx_on.png":"images/dbllistbox/arrowdx_off.png")%>  align="middle"/>
									</td>
								</tr>	
								<tr>
									<td height="6px"></td>
								</tr>
								<tr>
									<td align="center">
									  <img <%=(permission?"onclick=\"IO_delContatToBook('E');return false;\"":"")%> src=<%=(permission?"images/dbllistbox/delete_on.png":"images/dbllistbox/delete_off.png")%> align="middle"/> 
									</td>
								</tr>
							</table>
						</td>
						<td valign="top" width="60%"><%= tab%></td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>
<div id="BookTablePost" style="visibility:hidden;display:none;"></div>
<input type="hidden" value="<%=risultato%>" id="setiomsg"/>
</form>
