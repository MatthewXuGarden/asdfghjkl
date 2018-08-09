<%@ page language="java" import="java.util.*" 
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.helper.*"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.HTMLSimpleElement"
	import="com.carel.supervisor.dataaccess.language.*"
	import="com.carel.supervisor.presentation.tabmenu.*"
		import="com.carel.supervisor.presentation.bo.BR_SiteList"
	import="com.carel.supervisor.dataaccess.dataconfig.*"
	import="java.sql.Timestamp"
%>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	int idsite = sessionUser.getIdSite();
	LangService lang = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();
	
	String s_name = lang.getString("r_sitelist","name");
	String s_type = lang.getString("r_sitelist","type");
	
	String s_synchconfig = lang.getString("r_sitelist","synchconfig");
	String s_synch = lang.getString("r_sitelist","synch");
	String s_nosync = lang.getString("r_sitelist","nosync");
	String s_plantsync = lang.getString("r_sitelist","plantsync");
	String s_alarmsync = lang.getString("r_sitelist","alarmsync");
	String s_eventsync = lang.getString("r_sitelist","eventsync");
	String s_notesync = lang.getString("r_sitelist","notesync");
	String s_datatosync = lang.getString("r_sitelist","datatosync");
	String syncrcomment = lang.getString("r_sitelist","syncrcomment");
	
	String name_load = "";	
	BR_SiteList.setScreenH(sessionUser.getScreenHeight());
	BR_SiteList.setScreenW(sessionUser.getScreenWidth());
	String sync_htmltable = BR_SiteList.getHTMLSyncTable(900,200,language);

%>
<table border="0" width="100%" height="98%" cellspacing="1" cellpadding="1">
<tr><td>
		<p class='standardTxt'><%=syncrcomment%></p>
</td></tr>
<tr><td>&nbsp;</td></tr>
<tr height="100%" valign="top" id="trDataSync"><td>
<%=sync_htmltable%>
</td></tr>
<tr><td>
<FORM id='frm_sync' action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type='hidden' id='idsite_to_set' name='idsite_to_set'>
<INPUT type='hidden' id='sync_params' name='sync_params'>
<FIELDSET class='field'>
<LEGEND class='standardTxt'><%=s_synchconfig%></LEGEND>
	<TABLE>
		<TR height="10px"><TD></TD></TR>
		<TR class='standardTxt'>
			<TD><%=s_name%>:</TD>	
			<TD class='menu' id='sitename' ></TD>
		</TR>
		<TR height="10px"><TD></TD></TR>
	</TABLE>
	<TABLE>
		<TR class='standardTxt'>
			<TD><%=s_datatosync%></TD>
		</TR>
	</TABLE>
	<TABLE>
		<TR class='standardTxt'>
			<TD><INPUT type='checkbox' id='opt0' name='opt0' onclick='no_sync(this)'/></TD>	
			<TD><%=s_nosync%></TD>
			<TD width="150px"></TD>	
			<TD><INPUT type='checkbox' id='opt1' name='opt1' onclick='plant_sync(this)'/>  </TD>	
			<TD><%=s_plantsync%></TD>
			<TD width="150px"></TD>	
			<TD><INPUT type='checkbox' id='opt2' name='opt2' onclick='alr_evn_note(this)'/>  </TD>	
			<TD><%=s_alarmsync%></TD>
		</TR>
		<TR height="5px"><TD></TD></TR>
		<TR class='standardTxt'>
			<TD><INPUT type='checkbox' id='opt3' name='opt3' onclick='alr_evn_note(this)'/>  </TD>	
			<TD><%=s_eventsync%></TD>
			<TD width="150px"></TD>	
			<TD><INPUT type='checkbox' id='opt4' name='opt4' onclick='alr_evn_note(this)'/>  </TD>	
			<TD><%=s_notesync%></TD>
		</TR>
		<TR height="5px"><TD></TD></TR>
	</TABLE>
</FIELDSET>
</td></tr>
</table>