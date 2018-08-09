<%@ page language="java" import="java.util.*"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.*"
import="com.carel.supervisor.presentation.io.CioFAX"
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
	boolean permission = sessionUser.isButtonActive("setio", "tab1name","SaveConfIO");
	if (!permission)
		disabled = "disabled";
	
	// Get modem list
	CioFAX cio = new CioFAX(sessionUser.getIdSite());
	cio.loadConfiguration();
	
	int idConfiguration = cio.getIdconf();
	String modemLabel = cio.getModemId();
	int trynumber = cio.getTrynum();
	int retryafter = cio.getRetrynum();
	String centra = cio.getCentralino();
		
	String[][] modemList = cio.getModem();
	myBo.setScreenH(sessionUser.getScreenHeight());
	myBo.setScreenW(sessionUser.getScreenWidth());
	String tab = myBo.getBookAddress(language,"F");
	
	DispatcherBook[] addrBook = DispatcherMgr.getInstance().getReceiverInfoByType("F");
	int numAddr = addrBook.length;
	
	String msguser1 = multiLanguage.getString("setio","message1");
	String faxcomment = multiLanguage.getString("setio","faxcomment");
	String noremaddressfromide = multiLanguage.getString("ide","noremaddressfromide");
	String checkconf = multiLanguage.getString("setio","checkconf");	
	String automsgconf = multiLanguage.getString("setio","automaticmsg");
	String automsgconf2 = multiLanguage.getString("setio","automaticmsg2");
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey(); 
%>

<input type='hidden' id='noremaddressfromide' value='<%=noremaddressfromide%>'>
<input type="hidden" id="msgautomatic" value="<%=automsgconf%>">
<input type="hidden" id="msgautomatic2" value="<%=automsgconf2%>"/>
<input type="hidden" id="numAddr" value="<%=numAddr%>"/>
<form name="frmcfgfax" id="frmcfgfax" method="post" action="servlet/master;jsessionid=<%=jsession%>">
<input type="hidden" id="executetechact" name="executetechact" value="F">
<input type="hidden" name="iocfgtype" value="F">
<input type="hidden" name="iocfgid" value="<%= idConfiguration%>">
<input type="hidden" name="iomodemtype" value="A">
<input type="hidden" id="iomsguser" value="<%=msguser1%>">
<input type="hidden" id="isRemote" value="false">
<input type="hidden" id="checkconf" value="<%=checkconf%>">
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<p class="StandardTxt"><%=faxcomment%></p>

<table border="0" width="100%" cellspacing="1" cellpadding="1">

	<tr>
		<td>
			<fieldset class="field">
				<legend class="standardTxt"><%=multiLanguage.getString("setio","conf")%></legend>
				<table border="0" width="100%" cellspacing="5" cellpadding="1">
					<tr>
						<td class="standardTxt" width="15%"><%=multiLanguage.getString("setio","modem")%>:</td>
						<td class="standardTxt" width="35%">
							<select <%=disabled%> name="iomodeml" id="iomodeml" class="standardTxt" onchange="IoEnableTechFS();">
							<option value="nop">------------------------------------</option>
							<%for(int i=0; i<modemList.length; i++){%>
								<option value="<%=modemList[i][0]%>" <%= modemLabel.equalsIgnoreCase(modemList[i][0])?"selected":""%> ><%= modemList[i][0]%></option>
							<%}%>
							</select>
						</td>
						<td class="standardTxt" width="10%"><%=multiLanguage.getString("setio","centra")%>:</td>
					    <td width="40%"><input <%=disabled%> type="text" size="4" maxlength="6" name="iocentralino" id="iocentralino" value="<%= centra%>" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" onkeydown="checkOnlyAnalog(this,event);"/></td>
					</tr>
					<tr>
						<td class="standardTxt"><%=multiLanguage.getString("setio","numtry")%>:</td>
						<td class="standardTxt"><input <%=disabled%> type="text" size="4" maxlength="2" name="iotrynum" id="iotrynum" value="<%= trynumber>0?trynumber:0%>" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></td>
					</tr>
					<tr>
						<td class="standardTxt"><%=multiLanguage.getString("setio","retry")%>:</td>
						<td class="standardTxt"><input <%=disabled%> type="text" size="4" maxlength="2" name="ioretryafter" id="ioretryafter" value="<%= retryafter>0?retryafter:0%>" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/> <%=multiLanguage.getString("setio","min")%></td>
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
									<td><input <%=disabled%> type="text" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" name="ioflabel" id="ioflabel" size="35" maxlength="50" onkeydown="checkLettNum(this,event);"/></td>
								</tr>
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","addf")%>:</td>
									<td><input <%=disabled%> type="text" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" name="iofaddress" id="iofaddress" size="35" maxlength="30" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></td>
								</tr>
							</table>		
						</td>
						<td valign="middle" width="10%">
							<table width="100%" border="0" cellspacing="5" cellpadding="1">
								<tr>
									<td align="center">
										<img <%=(permission?"onclick=\"IO_addContatToBook();return false;\"":"")%> src=<%=(permission?"images/dbllistbox/arrowdx_on.png":"images/dbllistbox/arrowdx_off.png")%> align="middle"/>
									</td>
								</tr>	
								<tr>
									<td height="6px"></td>
								</tr>
								<tr>
									<td align="center">
									  <img <%=(permission?"onclick=\"IO_delContatToBook('F');return false;\"":"")%> src=<%=(permission?"images/dbllistbox/delete_on.png":"images/dbllistbox/delete_off.png")%> align="middle"/> 
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
<div id="BookTablePost" style="visibility:hidden;"></div>
</form>
<input type="hidden" value="<%=risultato%>" id="setiomsg"/>