<%@ page language="java" 

import="java.util.*"

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.ac.AcMaster"
import="com.carel.supervisor.director.ac.AcProcess"
import="com.carel.supervisor.presentation.ac.MasterBeanList"
import="com.carel.supervisor.director.ac.AcManager"
import="com.carel.supervisor.director.ac.AcProperties"

%>

<%

UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();

//allineo struttura modulo con struttura linee sito se motore DP è off:
if ((new AcProperties()).getProp("ac_running") == 0)
	AcProcess.ctrl_ac_tables();

MasterBeanList l = new MasterBeanList();
String table = l.getHTMLOverviewTable(language);

boolean isRun = AcManager.getInstance().isRunning();
String s_engine = "";
if (isRun)
{
	s_engine = "<TD style='color:GREEN;'>"+lan.getString("ac","acrun")+"</TD>"; 
}
else
{
	s_engine = "<TD style='color:RED;'>"+lan.getString("ac","acstop")+"</TD>"; 
}

%>

<input type="hidden" id="noslaves" name="noslaves" value="<%=lan.getString("ac","noslaves")%>"/>
<input type="hidden" id="linkname" name="noslaves" value="<%=lan.getString("ac","linkname")%>"/>

<p class="standardTxt"><%=lan.getString("ac","comment1")%></p>	

<FORM name="ac_frm" id="ac_frm" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
<input type="hidden" id="cmd" name="cmd"/>
<%if(l.getNumberOfMaster()!=0){%>
	<fieldset class="field">
	<LEGEND  class="standardTxt">&nbsp;<span><%=lan.getString("ac","acstatus")%></span>&nbsp;</LEGEND>

	<TABLE width="100%">
		<TR class="standardTxt">
			
			<!--<TD width="35%" align='center'>
				<img name='startbtn' id='startbtn' <%=isRun?"src='images/actions/start_off.png'":"style='cursor:pointer' onclick='start_ac();' src=\"images/actions/start_on_black.png\""%> />
					&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
				<img name='stopbtn' id='stopbtn' <%=isRun?"src='images/actions/stop_on_black.png' style='cursor:pointer' onclick='stop_ac();'":"src='images/actions/stop_off.png\'"%> />
			</TD>-->
			
			<TD><%=s_engine%></TD>
		</TR>
	</TABLE>
	
	</fieldset>		
<%}%>
</FORM>
<BR /> 
<%=table%>
