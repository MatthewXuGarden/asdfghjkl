<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.*"
	import="com.carel.supervisor.presentation.bo.BTestIo"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
    import="com.carel.supervisor.base.config.BaseConfig"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	int idSite = sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();
	String lb01 = multiLanguage.getString("testio","fax");
	String lb02 = multiLanguage.getString("testio","sms");
	String lb03 = multiLanguage.getString("testio","email");
	String lb04 = multiLanguage.getString("testio","print");
	String lb05 = multiLanguage.getString("testio","rele");
	String lb06 = multiLanguage.getString("testio","win");
	String lb07 = multiLanguage.getString("testio","rem");
	
	String comment = multiLanguage.getString("testio","comment");
	String tested = sessionUser.getPropertyAndRemove("tested");
	String testresult = multiLanguage.getString("testio","testresult");
	
	String strTranReportPrinter = multiLanguage.getString("setio", "report_printer");
	String strTranAlarmPrinter = multiLanguage.getString("setio", "alarm_printer");
	
	String relactive = "Active" ;//multiLanguage.getString("relaymgr","active");
	String relnoactive = "Not active"; //multiLanguage.getString("relaymgr","noactive");
%>
<form name="frmtio" id="frmtio"  method="post" action="servlet/master;jsessionid=<%=jsession%>">
<input type="hidden" id="tioaddress" name="tioaddress" value=""/>
<input type='hidden' id='tested' name='tested' value='<%=tested%>'/>
<input type='hidden' id='testresult' value='<%=testresult%>'/>
<p class="StandardTxt"><%=comment%></p>
<table border="0" cellspacing="1" cellpadding="1" width="100%">
	<tr>
		<td>
			<fieldset class="field">
			<legend class="standardTxt"><%=multiLanguage.getString("testio","lista")%></legend>
			<table border="0" cellspacing="5" cellpadding="1" width="80%">
				<tr>
					<td class="standardTxt" width="25%">
						<input type="radio" name="tio" id="tiof" value="F" onclick="Tio_changeDevice(this);" checked><%=lb01%>
					</td>
					<td class="standardTxt" width="25%">
						<input type="radio" name="tio" id="tios" value="S" onclick="Tio_changeDevice(this);"><%=lb02%>
					</td>
					<td class="standardTxt" width="25%">
						<input type="radio" name="tio" id="tioe" value="E" onclick="Tio_changeDevice(this);"><%=lb03%>
					</td>
					<td class="standardTxt" width="25%">
						<input type="radio" name="tio" id="tiod" value="D" onclick="Tio_changeDevice(this);"><%=lb07%>
					</td>
				</tr>
				<tr>
					<td class="standardTxt">
						<input type="radio" name="tio" id="tiop" value="P" onclick="Tio_changeDevice(this);"><%=lb04%>
					</td>
					<td class="standardTxt">
						<input type="radio" name="tio" id="tior" value="L" onclick="Tio_changeDevice(this);"><%=lb05%>
					</td>
					<td class="standardTxt">
						<input type="radio" name="tio" id="tiow" value="W" onclick="Tio_changeDevice(this);"><%=lb06%>
					</td>
					<td></td>
				</tr>
			</table>
			</fieldset>
		</td>
	</tr>
	<tr>
		<td style="height:10px;"></td>
	</tr>
	<!-- riattivare la sezione successiva quando verrà implementato il test in cui l'utente 
	     digita in un textfield il destinatario -->
	<!--  tr>
		<td>
			<fieldset class="field">
			<legend class="standardTxt"><%=multiLanguage.getString("testio","mod")%></legend>
			<table border="0" cellspacing="5" cellpadding="1" width="50%">
				<tr>
					<td class="standardTxt" width="33%">
						<input type="radio" name="tiotype" id="tiog" value="guidato" onclick="Tio_changeModalita(this);" checked><%=multiLanguage.getString("testio","modg")%>
					</td>
					<td class="standardTxt" width="33%">
						<div id="tmpnolib" style="visibility:hidden;">
						<input type="radio" name="tiotype" id="tiol" value="libero" onclick="Tio_changeModalita(this);" ><%=multiLanguage.getString("testio","modl")%>
						</div>
					</td>
					<td width="33%"></td>
				</tr>
			</table>
			</fieldset>
		</td>
	</tr -->
	<tr>
		<td>
			<fieldset class="field">
			<legend class="standardTxt"><%=multiLanguage.getString("testio","dest")%></legend>
			<table border="0" cellspacing="1" cellpadding="1" width="50%">
				<tr>
					<td class="standardTxt">
						<div id="libero" style="visibility:hidden;display:none;">
							<input type="text" name="taddrlib" id="taddrlib" size="50" value="" disabled="true"/>
						</div>
						<div id="guidato" style="visibility:visible;display:block;">
							<div id="F" style="visibility:visible;display:block;">
								<select name="taddrgui" id="taddrgui" onchange="setAddress(this);">
									<%= BTestIo.createSelect("F")%>
								</select>
							</div>	
							<div id="S" style="visibility:hidden;display:none;">
								<select name="taddrgui" id="taddrgui" onchange="setAddress(this);">
									<%= BTestIo.createSelect("S")%>
								</select>
							</div>
							<div id="E" style="visibility:hidden;display:none;">
								<select name="taddrgui" id="taddrgui" onchange="setAddress(this);">
									<%= BTestIo.createSelect("E")%>
								</select>
							</div>
							<div id="D" style="visibility:hidden;display:none;">
								<select name="taddrgui" id="taddrgui" onchange="setAddress(this);">
									<%= BTestIo.createSelect("D")%>
								</select>
							</div>
							<div id="P" style="visibility:hidden;display:none;">
								<select name="taddrgui" id="taddrgui" onchange="setAddress(this);">
									<option value="0">---------------</option> 
									<option value="REPORT"><%=strTranReportPrinter%></option>
									<option value="ALARM"><%=strTranAlarmPrinter%></option>
								</select>
							</div>
							<div id="W" style="visibility:hidden;display:none;">
								<select name="taddrgui" id="taddrgui" onchange="setAddress(this);">
									<option value="0">---------------</option> 
								</select>
							</div>
							<div id="L" style="visibility:hidden;display:none;">
								<table border="0" width="95%" cellspacing="1" cellpadding="1">
									<tr>
										<td>
											<select name="taddrgui" id="taddrgui" onchange="setAddress(this);">
												<%= BTestIo.createSelectRelay(idSite,language)%>	
											</select>
										</td>
										<td>
											<select name="trelevalue">
												<option value="NOACT"><%=relnoactive %></option>
												<option value="ACT"><%=relactive %></option>
											</select>
										</td>
									</tr>
								</table>
							</div>
						</div>	
					</td>
				</tr>
			</table>
			</fieldset>
		</td>
	</tr>
</table>
</form>