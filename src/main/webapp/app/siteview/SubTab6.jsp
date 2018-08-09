<%@ page language="java"  pageEncoding="UTF-8" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.bo.helper.VarDependencyList"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.presentation.tabmenu.Tab"
import="com.carel.supervisor.presentation.tabmenu.MenuBuilder"
import="com.carel.supervisor.presentation.bo.helper.DeviceStatus"
import="com.carel.supervisor.presentation.bo.helper.PortInfo"
import="com.carel.supervisor.presentation.bo.helper.LineConfig"
import="com.carel.supervisor.base.config.*"
import="java.util.*"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	int idsite = sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();
	SiteInfoList sites = new SiteInfoList();
	SiteInfo site = sites.getById(idsite);

	// Alessandro : aggiunta tastiera virtuale
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	String cssVirtualKeyboardClass = (OnScreenKey ? ' '+VirtualKeyboard.getInstance().getCssClass() : "");
	VarDependencyList  vdl = new VarDependencyList();
	LineBeanList lineList = new LineBeanList();
	String devs = lineList.getHTMLDepdtTable(sessionUser,vdl);
	//String depdMsgs = lineList.allVariablesDependencies(sessionUser,vdl);	
	String depdMsgs = lineList.getVarDpdt(sessionUser,lan,vdl);
%>
<form id="frm_set_line" name="frm_set_line" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd" value="remove_dpdt"/>
<input type="hidden" id="devicesToRemoved" name="devicesToRemoved" value=""/>

<input type='hidden' name='nodev' id='nodev' value='<%=lan.getString("debug","nodevselect")%>' />
<input type='hidden' name='dltdevcfm' id='dltdevcfm' value='<%=lan.getString("datatransfer","slaveDeviceDelete")%>' />

		
<table border="0" width="97%" height="90%" cellspacing="0" cellpadding="0" >
	<%-- 
		<TR >
			<TD>
				<p class="StandardTxt"><%=lan.getString("vardpd","pgdesc") %></p>
			</TD>
		</TR>
	 --%>
	<tr  >
		<td style="width:100%; height:40%;">
			<fieldset class='field' style="width:100%;height:90%;" >     
				<legend class='standardTxt'><%=lan.getString("deviceview","tab1name") %></legend>
				<div style="height:95%;">
					<%=devs%>
				</div>
			</fieldset>
		</td>
	</tr>
	<tr><td><p>&nbsp;</p></td></tr>
	<tr>
		<td style="width:100%; height: 50%;">
			<%=depdMsgs %>
		</td>
	</tr> 
</table>

</form>
