<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"

%>
<%@ page import="com.carel.supervisor.presentation.bo.BTimeTable" %>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();	
	String jsession = request.getSession().getId(); 
	
	//Label Multilingua
	LangService lan = LangMgr.getInstance().getLangService(language);
	String detail = lan.getString("timetable","fascia");
	String nomesito = lan.getString("r_siteaccess","timesite");
	String listafasce = lan.getString("timetable","timelist");
	String enable = lan.getString("r_siteaccess","timesiteenable");
	String notselected = lan.getString("timetable","emptyvalue");
	String confirmfasciasitedel = lan.getString("rmtimetable","confirmfasciasitedel");
	String description = lan.getString("rmtimetable","descriptionass");
	String duplicatefasciasite = lan.getString("rmtimesite","duplicatefasciasite");
	
	String btime = ((BTimeTable)sessionUser.getCurrentUserTransaction().getBoTrx()).getHTMLTable("tab2name", language, "rmtimesite", 200, 905);
	
%>

<FIELDSET class="field">
<p class="StandardTxt"><%=description%></p>
<%=btime%>

<FORM id="frm_fascia_info" name="frm_fascia_info" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" id="cmd" name="cmd"/>
<INPUT type="hidden" id="notselected" value="<%=notselected%>"/>
<INPUT type="hidden" id="id" name="id" value=""/>
<INPUT type="hidden" id="confirmfasciasitedel" value="<%=confirmfasciasitedel%>">
<INPUT type="hidden" id="duplicatefasciasite" value="<%=duplicatefasciasite%>">

<LEGEND class="standardTxt"><%=detail%></LEGEND>
<TABLE class='standardTxt' width="100%" height="100%" border="0">
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>
	<TR>
		<TD valign="top" class="standardTxt" width="30%"><%=nomesito%>&nbsp;
				<select id="site"  name="site"><%=BTimeTable.createSelectSite()%></select>&nbsp;*
		</TD>
		<td valign="top" class="standardTxt" width="40%"><%=listafasce%>&nbsp;
				<select id="fascie"  name="fascie"><%=BTimeTable.createSelectFascie()%></select>&nbsp;*
		</td>
		<td valign="top" class="standardTxt" width="30%"><%=enable%>&nbsp;&nbsp;
				<input type="checkbox" id="abilita" name="abilita" checked="checked"/>
		</td>
	</tr>	
</TABLE>
</FORM>
</FIELDSET>
