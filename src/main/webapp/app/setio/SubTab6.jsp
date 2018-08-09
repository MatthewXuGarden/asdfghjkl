<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.bean.rule.RelayBean"
import="com.carel.supervisor.presentation.bean.rule.RelayBeanList"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.presentation.bean.ProfileBean"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.field.InternalRelayMgr"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String jsession = request.getSession().getId();
	String language = sessionUser.getLanguage();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	
	//controllo permessi	
	String disabled = "";
	boolean permission = sessionUser.isButtonActive("setio", "tab6name","SaveConfIO");
	if (!permission)
		disabled = "disabled";
		
	RelayBean[] relayInternalIO = null;
	int idRelay = -1;
	String t_disabled = "";
	String isEmpty = "true";
	StringBuffer sbIdList = new StringBuffer();
	relayInternalIO = RelayBeanList.getInternalIORelayBeans(sessionUser.getIdSite(),language);
	if(relayInternalIO != null)
	{
		for(int i=0; i<relayInternalIO.length; i++)
		{
			sbIdList.append(((RelayBean)relayInternalIO[i]).getIdrelay());
			if(i < relayInternalIO.length -1)
				sbIdList.append(",");
		}
	}
	
	boolean[] isSafetyRelay = InternalRelayMgr.getInstance().isSafetyRelay();
	String attivo = multiLanguage.getString("setio","attivo");
	String reset = multiLanguage.getString("setio","reset");
	String time = multiLanguage.getString("setio","time");
	String show = multiLanguage.getString("setio","show");
	String seconds = multiLanguage.getString("setio","seconds");
	String manual = multiLanguage.getString("setio","manual");
	String automatic = multiLanguage.getString("setio","automatic");
	String timed = multiLanguage.getString("setio","timed");
	String relay = multiLanguage.getString("setio","tab6name");
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<form name="frmcfgrelay" id="frmcfgrelay" method="post" action="servlet/master;jsessionid=<%=jsession%>">
<input type="hidden" name="idcontainer" id="idcontainer" value="<%=sbIdList.toString()%>"/>
<input type="hidden" id="isEmpty" value="<%=isEmpty%>"/>
<input type="hidden" id="disabled" value="<%=disabled%>"/>
<input type="hidden" id="manual" value="<%=manual %>"/>
<input type="hidden" id="automatic" value="<%=automatic %>"/>
<input type="hidden" id="timed" value="<%=timed %>"/>
<input type="hidden" id="relay" value="<%=relay %>"/>
<input type="hidden" id="attivo" value="<%=attivo %>"/>
<input type="hidden" id="reset" value="<%=reset %>"/>
<input type="hidden" id="time" value="<%=time %>"/>
<input type="hidden" id="seconds" value="<%=seconds %>"/>
<input type="hidden" id="show" value="<%=show %>"/>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>
<input type='hidden' id='OnScreenKey' value='<%=OnScreenKey %>' />


<FIELDSET class="field">
<LEGEND class="standardTxt"><%=multiLanguage.getString("setio","internalrelaytitle")%></LEGEND>
<table class="table" border="0" width="99%" cellspacing="1" cellpadding="0">
	<tr>
		<td style="height:5px"></td>
	</tr>
	<tr>
		<td class="th" width="35%" align='center'><b><%=relay %></b></td>
		<td class="th" width="18%" align="center"><b><%=attivo%></b></td>
		<td class="th" width="18%" align="center"><b><%=reset%></b></td>
		<td class="th" width="18%" align="center"><b><%=time%>(<%=seconds %>)</b></td>
		<td class="th" align="center"><b><%=show%></b></td>
	</tr>
	<%if ((relayInternalIO != null) && (relayInternalIO.length > 0)) {
		for(int i=0; i<relayInternalIO.length; i++) {%>
			<%idRelay = ((RelayBean)relayInternalIO[i]).getIdrelay();%>
			<tr class="<%=(i%2==0?"Row1":"Row2") %>">
				<td class="standardTxt">
					<%=((RelayBean)relayInternalIO[i]).getDeviceDesc()%> -> <%=((RelayBean)relayInternalIO[i]).getDescription()%>
				</td>
				<td class="standardTxt" align="center">
					<select name="RA_<%=idRelay%>" id="RA_<%=idRelay%>" class="standardTxt">
						<option value="0" <%=((!isSafetyRelay[i] && ((RelayBean)relayInternalIO[i]).getActivestate()==0)?"selected":"")%>>0</option>
						<option value="1" <%=((!isSafetyRelay[i] && ((RelayBean)relayInternalIO[i]).getActivestate()==1)?"selected":"")%>>1</option>
						<option value="-1" <%=(isSafetyRelay[i]?"selected":"")%>>Safe mode</option>
					</select>
				</td>
				<%t_disabled = disabled;
				if("".equals(disabled) && !((RelayBean)relayInternalIO[i]).getResettype().equalsIgnoreCase("T"))
				{
					t_disabled = "disabled";
				}
				%>
				<td class="td" align="center">
					<select <%=disabled%> name="RR_<%=idRelay%>" id="RR_<%=idRelay%>" class="standardTxt" onchange="IO_clearRelayResetField(<%=idRelay%>);">
						<option value="M" <%=((((RelayBean)relayInternalIO[i]).getResettype().equalsIgnoreCase("M"))?"selected":"")%>><%=manual%></option>
						<option value="A" <%=((((RelayBean)relayInternalIO[i]).getResettype().equalsIgnoreCase("A"))?"selected":"")%>><%=automatic%></option>
						<option value="T" <%=((((RelayBean)relayInternalIO[i]).getResettype().equalsIgnoreCase("T"))?"selected":"")%>><%=timed%></option>
					</select>
				</td>
				<td class="td" align="center">
					<input <%=t_disabled%> type="text" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" size="4" maxlength="4" id="RT_<%=idRelay%>" name="RT_<%=idRelay%>" 
					       value="<%=((((RelayBean)relayInternalIO[i]).getResettime()==-1)?"":((RelayBean)relayInternalIO[i]).getResettime())%>" 
					       onblur="onlyNumberOnBlur(this);" onkeydown="checkOnlyNumber(this,event);"/>
				</td>
				<td class="td" align="center">
					<input <%=disabled%> <%=(((RelayBean)relayInternalIO[i]).getShow())?"checked":""%> type="checkbox" name="RS_<%=idRelay%>" id="RS_<%=idRelay%>"/>
				</td>
		    </td>
		</tr>
		<%}
	    } else {%>
	    <table align='center'><tr><td><b><%=multiLanguage.getString("relaymgr", "norelay")%></b></td></tr></table>
	  <%}%>
</table>
</FIELDSET>
<p/>
<FIELDSET class="field">
<LEGEND class="standardTxt"><%=multiLanguage.getString("setio","otherrelaytitle")%></LEGEND>
<table border="0" id="relaycontainer" width="99%" cellspacing="1" cellpadding="0">
	<tr>
		<td style="height:5px"></td>
	</tr>
</table>
</FIELDSET>
</form>