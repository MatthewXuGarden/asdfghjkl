<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.*"
import="com.carel.supervisor.presentation.io.CioDIAL"
import="com.carel.supervisor.presentation.io.CioRAS"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.presentation.bo.BSetIo"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.bean.ProfileBean"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.base.config.ProductInfoMgr"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUserLoc = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	BSetIo myBo = (BSetIo)trxUserLoc.getBoTrx();
	myBo.setScreenH(sessionUser.getScreenHeight());
	myBo.setScreenW(sessionUser.getScreenWidth());
	String jsession = request.getSession().getId();
	String risultato = trxUserLoc.remProperty("state");
	if(risultato != null && risultato.equalsIgnoreCase("ko"))
		risultato = multiLanguage.getString("setio","message");
	else
		risultato = "";
	
	String dispInModem = "hidden";
		
	//controllo permessi	
	String disabled = "";
	boolean permission = sessionUser.isButtonActive("setio", "tab4name","SaveConfIO");
	if (!permission)
		disabled = "disabled";
	
	// Get modem list
	CioDIAL ras = new CioDIAL(sessionUser.getIdSite());
	ras.loadConfiguration();
	
	// Get modem list
	CioRAS dial = new CioRAS(sessionUser.getIdSite());
	
	int idConfiguration = ras.getIdconf();
	String modemLabel = ras.getModemLabel();
	String user = ras.getUser();
	String number = ras.getNumber();
	int trynumber = ras.getRetryNum();
	int retryafter = ras.getRetryAfter();
	String centra = ras.getCentralino();
			
	String[][] modemList = ras.getModem();
	
	String[][] modemListR = dial.getModem();
	String[] modemListRa = dial.getConfigModem();
	
	boolean found = false;
	
	for(int i=0; i<modemListR.length; i++)
	{
		found = false;
		for(int j=0; j<modemListRa.length; j++)			
		{
			if(modemListR[i][0].equalsIgnoreCase(modemListRa[j]))
			{
				found = true;			
				break;
			}
		}
		
		if(found)
			modemListR[i][1] = "checked";
	}
		
	String tab = myBo.getBookAddress(language,"D");
	
	String msguser1 = multiLanguage.getString("setio","message1");
	String rascomment = multiLanguage.getString("setio","rascomment");
	String r_rascomment = multiLanguage.getString("setio","r_rascomment");
	String noremaddressfromide = multiLanguage.getString("alrsched","noremaddressfromide");
	String checkconf = multiLanguage.getString("setio","checkconf");
	
	String str = ProductInfoMgr.getInstance().getProductInfo().get("needRemoteACK");
	boolean needRemoteACK = Boolean.valueOf(str!=null?str:"false");
	
	// Msg Reboot
	String msgreboot = multiLanguage.getString("setio","rasreboot");
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<input type='hidden' id='msgrasreboot' value='<%=msgreboot%>'>
<input type='hidden' id='noremaddressfromide' value='<%=noremaddressfromide%>'>
<form name="frmcfgras" id="frmcfgras" method="post" action="servlet/master;jsessionid=<%=jsession%>">
<input type="hidden" name="iocfgtype" value="D">
<input type="hidden" name="iocfgid" value="<%= idConfiguration%>">
<input type="hidden" name="iomodemtype" value="A">
<input type="hidden" id="iomsguser" value="<%=msguser1%>">
<input type="hidden" id="isRemote" value="false">
<input type="hidden" id="checkconf" value="<%=checkconf%>">
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>

<p class="StandardTxt"><%=rascomment%></p>
<table border="0" width="100%" cellspacing="1" cellpadding="1">
	<tr>
		<td>
			<fieldset class="field">
				<legend class="standardTxt"><%=multiLanguage.getString("setio","conf")%></legend>
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td width="100%" valign="top">
							<table border="0" width="100%" cellspacing="5" cellpadding="1">
								<tr>
									<td class="standardTxt" width="15%"><%=multiLanguage.getString("setio","modem")%>:</td>
									<td class="standardTxt" width="35%">
										<select <%=disabled%> name="iomodeml" id="iomodeml" class="standardTxt">
										<option value="LAN">------------------------------------</option>
										<%for(int i=0; i<modemList.length; i++){%>
											<option value="<%=modemList[i][0]%>" <%= modemLabel.equalsIgnoreCase(modemList[i][0])?"selected":""%> ><%= modemList[i][0]%></option>
										<%}%>
										</select>
									</td>
								    <td class="standardTxt" width="10%" align="right"><%=multiLanguage.getString("setio","centra")%>:</td>
								    <td width="40%"><input <%=disabled%> type="text" size="4" maxlength="6" name="iocentralino" id="iocentralino" value="<%= centra%>" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" onkeydown="checkOnlyAnalog(this,event);"/></td>
								</tr>							
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","numtry")%>:</td>
									<td class="standardTxt"><input <%=disabled%> type="text" size="4" maxlength="2" name="iotrynum" id="iotrynum" value="<%= trynumber>0?trynumber:0%>" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/></td>
								</tr>
								<tr>
									<td class="standardTxt"><%=multiLanguage.getString("setio","retry")%>:</td>
									<td class="standardTxt"><input <%=disabled%> type="text" size="4" maxlength="2" name="ioretryafter" id="ioretryafter" value="<%= retryafter>0?retryafter:0%>" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/> <%=multiLanguage.getString("setio","min")%></td>
									<td class="standardTxt" width="20%" align="right"><%=multiLanguage.getString("setio","needRemoteACK") %></td>
									<td width="30%"><input type="checkbox" name="needRemoteACK" <%=needRemoteACK?"checked":"" %>></td>
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
									<td class="standardTxt"><%=multiLanguage.getString("setio","addf")%>&nbsp;/&nbsp;IP:</td>
									<td><input <%=disabled%> type="text" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' name="iofaddress" id="iofaddress" size="35" maxlength="50" onblur="checkOnlyAnalogOnBlur(this,event);" onkeydown="checkOnlyAnalog(this,event);" /></td>
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
									  <img <%=(permission?"onclick=\"IO_delContatToBook('D');return false;\"":"")%> src=<%=(permission?"images/dbllistbox/delete_on.png":"images/dbllistbox/delete_off.png")%> align="middle"/> 
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