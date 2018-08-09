<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.presentation.fs.FSStatus"

%>
<%@ page import="com.carel.supervisor.presentation.fs.FSConfig" %>
<%@ page import="com.carel.supervisor.plugin.fs.FSManager" %>
<%@ page import="com.carel.supervisor.plugin.fs.FSRack" %>
<%@ page import="com.carel.supervisor.presentation.bean.DeviceBean" %>
<%@ page import="com.carel.supervisor.presentation.devices.DeviceList" %>
<%@ page import="com.carel.supervisor.presentation.bean.DeviceListBean" %>
<%@ page import="com.carel.supervisor.presentation.fs.FSRackBean" %>
<%@ page import="com.carel.supervisor.presentation.bean.ProfileBean" %>
<%@ page import="com.carel.supervisor.director.DirectorMgr" %>
<%@page import="com.carel.supervisor.plugin.base.Plugin"%>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	String jsession = request.getSession().getId();
	boolean isProtected = ut.isTabProtected();
	int idsite= sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
    		
	FSRack[] racks = FSRack.getRacks(language);
	//0:ok
	//5:aux=new, verify conf
	//10:aux=old, config fsdetail
	int rack_to_set = 0;
	if (racks!=null&&racks.length!=0)
	{
		for (int i=0;i<racks.length;i++)
		{
			if (racks[i].getId_minset().floatValue()==0&&racks[i].getId_maxset().floatValue()==0&&racks[i].getId_gradient().floatValue()==0)
			{
				if(racks[i].getAux().equals("new"))
				{
					rack_to_set = 5;
				}
				else if(racks[i].getAux().equals("old"))
				{
					rack_to_set = 10;
				}
				break;
			}
		}
	}
	
	FSRack[] racks_engine = FSManager.getInstance().getRacks();	
	String html_body = "<table><tr><td><p class='mediumTxt'><b>"+lan.getString("fs","noracks")+"</b></p></td></tr></table>";
    
    FSStatus.cleanDBTables();
    if (!DirectorMgr.getInstance().isMustCreateProtocolFile())
	{
		if (racks!=null)
		 	html_body = FSStatus.getOverviewTable(racks,language,sessionUser.getScreenHeight(),sessionUser.getScreenWidth());
	}
	
	boolean run = FSManager.getInstance().isRunning();
	String msg = "";
	if (run)
	{
		msg = "<TD style='color:GREEN;'><b>"+lan.getString("fs","pok")+"</b></TD>"; 
	}
	else
	{
		msg = "<TD style='color:RED;'><b>"+lan.getString("fs","pko")+"</b></TD>"; 
	}
%>

<% if (DirectorMgr.getInstance().isMustCreateProtocolFile()) {%>
	<p class='standardTxt'><%=lan.getString("dtlview","restarttoview")%></p>
<%}else { %>

	<p class='standardTxt'><%=lan.getString("fs","comment1")%></p>
	<input type='hidden' id='rack_to_set' value='<%=rack_to_set%>'/>
	<input type='hidden' id='verifyconf' value='<%=lan.getString("fs","verifyconf")%>' />
	<input type='hidden' id='auxoldconfig' value='<%=lan.getString("fs","auxoldconfig")%>' />
	<FORM name="frm_fs" id="frm_fs" action="servlet/master;jsessionid=<%=jsession%>" method="post">
		<INPUT type='hidden' id='cmd' name='cmd'/>
		<input type="hidden" name="archtabtoload" id="archtabtoload" value="0"/> 
	</FORM> 		
	
	<%if(racks!=null){%>
	<fieldset class='field'>
	<LEGEND class='standardTxt'><%=lan.getString("fs","pstatus")%></LEGEND>
	<TABLE width="100%">
		<TR class='standardTxt'>
			<TD width="35%" align='center'>
				<img name='startbtn' id='startbtn' <%=run?"src='images/actions/start_off.png'":"style='cursor:pointer' onclick='start_fs();' src=\"images/actions/start_on_black.png\""%> />
					&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
				<img name='stopbtn' id='stopbtn' <%=run?"src='images/actions/stop_on_black.png' style='cursor:pointer' onclick='stop_fs();'":"src='images/actions/stop_off.png\'"%> />
			</TD>
			<%=msg%>
		</TR>
	</TABLE>
	</fieldset>		
	
	<br></br>
	<%}%>

	<%=html_body%>
	
<%}%>
	
		