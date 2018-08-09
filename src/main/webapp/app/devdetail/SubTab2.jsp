<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.dataaccess.datalog.impl.*"
import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.*"

%>
<%@ page import="com.carel.supervisor.presentation.bo.helper.VarDetailHelper" %>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	int idsite= sessionUser.getIdSite();
	String jsession = request.getSession().getId();
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	boolean isProtected = ut.isTabProtected();
	String nospace = ut.remProperty("nospace");
	
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	
    // id device
	int iddev = Integer.parseInt(sessionUser.getProperty("iddev"));
	
	//DeviceListBean devList= new DeviceListBean(idsite,language);
    DeviceBean device= DeviceListBean.retrieveSingleDeviceById(idsite,iddev, language);
	String descr= device.getDescription();
	
	
	
	String freqalarm = lan.getString("devdetail","freqalarm");
	String nozerofreq = lan.getString("devdetail","nozerofreq");
	String devconfcomment2 = lan.getString("devdetail","devconfcomment2");
	String s_nospace = lan.getString("devdetail","s_nospace");
		
	String table = VarDetailHelper.getHTMLAlarmsTable(idsite,iddev,language,sessionUser.getScreenWidth(),sessionUser.getScreenHeight(),isProtected);
	
%>
<INPUT type="hidden" id="nospace" name="nospace" value="<%=nospace%>" >
<INPUT type="hidden" id="s_nospace" name="s_nospace" value="<%=s_nospace%>" >
<INPUT type="hidden" id="nozerofreq" name="nozerofreq" value="<%=nozerofreq%>" >
<FORM id="frm_dev_alr_save" name="frm_dev_alr_save" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" id="cmd" name="cmd" >
<INPUT type="hidden" id="iddev" name="iddev" value="<%=iddev%>" >
<table border="0" width="99%" height="95%" cellspacing="1" cellpadding="1">
	<tr>
	<td>
			<p class="tdTitleTable"><%=descr%></p>
	</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<TR>
	<TD><p class="standardTxt"><%=devconfcomment2%></p></TD>
	</TR>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<TD>
			<TABLE border="0" height="10%">
				<TR>
					<td class="standardTxt"><%=freqalarm%>:</td>
					<td class="standardTxt">
						<select <%=(isProtected?"disabled":"")%> id="frequency" name="frequency" >
							<option value='5'>5s</option>
							<option value='10'>10s</option>
							<option value='15'>15s</option>
							<option value='30'>30s</option>
							<option value='60'>1m</option>
							<option value='120'>2m</option>
							<option value='300'>5m</option>
							<option value='600'>10m</option>
							<option value='900'>15m</option>
							<option value='1800'>30m</option>
						</select>
					</td>
				</TR>	
			</TABLE>
		</TD>
	</tr>
    <tr><td>
      <table border="0" width="100%" height="98%" cellspacing="1" cellpadding="1">
        <tr height="100%" valign="top" id="trDevList">
		  <td><%=table%></td>
        </tr>
      </table>
    </td></tr>
</table>
</FORM>