<%@ page language="java"  
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.*"
import="com.carel.supervisor.presentation.io.CioSMS"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.presentation.bo.BSetIo" 
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.bean.ProfileBean"
import="com.carel.supervisor.dispatcher.book.DispatcherBook"
import="com.carel.supervisor.dispatcher.DispatcherMgr"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
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
	boolean permission = sessionUser.isButtonActive("setio", "tab2name","SaveConfIO");
	if (!permission)
		disabled = "disabled";
		
	
	
	// Get modem list
	CioSMS cioSms = new CioSMS(sessionUser.getIdSite());
	cioSms.loadConfiguration();
	
	int idConfiguration = cioSms.getIdConf();
	String modemLabel = cioSms.getLabelModem();
	String type = cioSms.getType();
	int provider = cioSms.getProviderId();
	String call = cioSms.getCall();
	int trynumber = cioSms.getTrynum();
	int retryafter = cioSms.getRetrynum();
	String centra = cioSms.getCentralino();
	
	String[][] modemList = cioSms.getModem();
	String[][] providerList = cioSms.getProvider();
	
	myBo.setScreenH(sessionUser.getScreenHeight());
	myBo.setScreenW(sessionUser.getScreenWidth());
	String tab = myBo.getBookAddress(language,"S");
	
	DispatcherBook[] addrBook = DispatcherMgr.getInstance().getReceiverInfoByType("S");
	int numAddr = addrBook.length;
	
	String msguser1 = multiLanguage.getString("setio","message1");
	String smscomment = multiLanguage.getString("setio","smscomment");
	
	String isGSM = "false";
	if(type.equalsIgnoreCase("G"))
		isGSM = "true";
		
	String noremaddressfromide = multiLanguage.getString("alrsched","noremaddressfromide");
	String checkconf = multiLanguage.getString("setio","checkconf");
	String automsgconf = multiLanguage.getString("setio","automaticmsg");
	String automsgconf2 = multiLanguage.getString("setio","automaticmsg2");
	String national = multiLanguage.getString("setio","national");
	String international = multiLanguage.getString("setio","international");
	String analogic = multiLanguage.getString("setio","analogic");
	String sms70alert = multiLanguage.getString("setio","sms70alert");
	
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>

<input type='hidden' id='noremaddressfromide' value='<%=noremaddressfromide%>'>
<input type="hidden" id="msgautomatic" value="<%=automsgconf%>">
<input type="hidden" id="msgautomatic2" value="<%=automsgconf2%>"/>
<input type="hidden" id="numAddr" value="<%=numAddr%>"/>
<form name="frmcfgsms" id="frmcfgsms" method="post" action="servlet/master;jsessionid=<%=jsession%>">
<input type="hidden" id="executetechact" name="executetechact" value="F">
<input type="hidden" name="iocfgtype" value="S">
<input type="hidden" name="iocfgid" value="<%= idConfiguration%>">
<input type="hidden" id="iolblprovider" name="iolblprovider" value=""/>
<input type="hidden" id="iomsguser" value="<%=msguser1%>">
<input type="hidden" id="isRemote" value="false">
<input type="hidden" id="checkconf" value="<%=checkconf%>">
<input type="hidden" id="sms70alert" value="<%=sms70alert%>">
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<p class="StandardTxt"><%=smscomment%></p>

<table border="0" width="100%" cellspacing="1" cellpadding="1">
	<tr>
		<td>
			<fieldset class="field">
				<legend class="standardTxt"><%=multiLanguage.getString("setio","conf")%></legend>
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td width="50%" valign="top">
							<table border="0" width="100%" cellspacing="5" cellpadding="1">
								<tr>
									<td class="standardTxt" width="30%"><%=multiLanguage.getString("setio","modem")%>:</td>
									<td class="standardTxt" width="70%">
										<select <%=disabled%> name="iomodeml" id="iomodeml" class="standardTxt" onchange="IoEnableTechFS();">
										<option value="nop">------------------------------------</option>
										<%for(int i=0; i<modemList.length; i++){%>
											<option value="<%=modemList[i][0]%>" <%= modemLabel.equalsIgnoreCase(modemList[i][0])?"selected":""%> ><%= modemList[i][0]%></option>
										<%}%>
										</select>
									</td>
								</tr>
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","tipo")%>:</td>
									<td class="standardTxt"><nobr>
										<input  <%=disabled%> onclick="IO_changeSMS(this);" type="radio" name="iomodemtype" id="iomodemtypeA" value="A" <%= (type.equalsIgnoreCase("A")||type.equalsIgnoreCase(""))?"checked":""%> > <%= analogic %>
										<input <%=disabled%> onclick="IO_changeSMS(this);" type="radio" name="iomodemtype" id="iomodemtypeG" value="G" <%= type.equalsIgnoreCase("G")?"checked":""%> >GSM
										</nobr>
									</td>
								</tr>
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","prov")%>:</td>
									<td class="standardTxt">
										<select  <%=disabled%> name="iosmsprovider" id="iosmsprovider" class="standardTxt" onchange="setLabelProvider();">
										<%for(int i=0; i<providerList.length; i++){%>
											<option value="<%=providerList[i][0]%>" <%= String.valueOf(provider).equalsIgnoreCase(providerList[i][0])?"selected":""%> ><%= providerList[i][1]%></option>
										<%}%>
										</select>
									</td>
								</tr>
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","call")%>:</td>
									<td>
										<select  <%=disabled%> name="iosmscall" id="iosmscall" class="standardTxt" <%=(isGSM.equalsIgnoreCase("true")?"disabled=true":"")%>>
											<option value="N" <%= call.equalsIgnoreCase("N")?"selected":""%> > <%= national %> </option>
											<option value="I" <%= call.equalsIgnoreCase("I")?"selected":""%> > <%= international %> </option>
										</select>
									</td>
								</tr>
							</table>
						</td>
						<td width="50%" valign="top">
							<table border="0" width="100%" cellspacing="5" cellpadding="1">
								<tr>
									<td class="standardTxt" width="30%"><%=multiLanguage.getString("setio","centra")%>:</td>
									<td><input  <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' size="4" maxlength="10" name="iocentralino" id="iocentralino" value="<%=centra%>" onkeydown="checkOnlyAnalog(this,event);"/></td>
								<tr>
								<tr>
									<td></td>
								</tr>
								<tr>
									<td class="standardTxt" width="30%"><%=multiLanguage.getString("setio","numtry")%>:</td>
									<td class="standardTxt"><input  <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' size="4" maxlength="2" name="iotrynum" id="iotrynum" value="<%= trynumber>0?trynumber:0%>" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></td>
								</tr>
								<tr>
									<td class="standardTxt" width="30%"><%=multiLanguage.getString("setio","retry")%>:</td>
									<td class="standardTxt"><input <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' size="4" maxlength="2" name="ioretryafter" id="ioretryafter" value="<%= retryafter>0?retryafter:0%>" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/> <%=multiLanguage.getString("setio","min")%></td>
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
									<td><input <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' name="ioflabel" id="ioflabel" size="35" maxlength="50" onkeydown="checkLettNum(this,event);" /></td>
								</tr>
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","addf")%>:</td>
									<td><input <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' name="iofaddress" id="iofaddress" size="35" maxlength="30" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></td>
								</tr>
							</table>		
						</td>
						<td valign="middle" width="10%">
							<table width="100%" border="0" cellspacing="5" cellpadding="1">
								<tr>
									<td align="center">
										<img <%=(permission?"onclick=\"IO_addContatToBook();return false;\"":"")%>  src=<%=(permission?"images/dbllistbox/arrowdx_on.png":"images/dbllistbox/arrowdx_off.png")%>  align="middle"/>
									</td>
								</tr>	
								<tr>
									<td height="6px"></td>
								</tr>
								<tr>
									<td align="center">
									  <img <%=(permission?"onclick=\"IO_delContatToBook('S');return false;\"":"")%> src=<%=(permission?"images/dbllistbox/delete_on.png":"images/dbllistbox/delete_off.png")%> align="middle"/> 
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
