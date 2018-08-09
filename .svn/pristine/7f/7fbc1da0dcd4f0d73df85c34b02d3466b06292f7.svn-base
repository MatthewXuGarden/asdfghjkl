<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.presentation.bean.rule.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.presentation.dbllistbox.*"
import="com.carel.supervisor.presentation.rule.*"
import="java.util.*"

%>
<%@ page import="com.carel.supervisor.presentation.rule.FaxListBox" %>

<%

UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();
UserTransaction ut = sessionUser.getCurrentUserTransaction();
String notremoveaction = ut.remProperty("notremoveaction");
String actionnotremoved = lan.getString("alrsched","actionnotremoved2");

String fax = lan.getString("action","fax");
String doubleElement = lan.getString("dbllistbox","doublelement");
String nullselected = lan.getString("dbllistbox","nullselected");
//20090119 - BUG 5289 RF Check on max elements
String maxelements = lan.getString("dbllistbox", "maxelements");

String action = lan.getString("setaction","action");
String nomodactionfromide = lan.getString("ide","nomodactionfromide");

//retrieve da sessione
int actioncode = Integer.parseInt(sessionUser.getProperty("actioncode"));
ActionBeanList actionList = new ActionBeanList();
String action_description = ActionBeanList.getDescription(idsite,actioncode);

//FAX
FaxListBox faxlistboxobj = new FaxListBox();
faxlistboxobj.setScreenH(sessionUser.getScreenHeight());
faxlistboxobj.setScreenW(sessionUser.getScreenWidth());
String faxlistbox=faxlistboxobj.getFaxListBox(idsite,actioncode, language);

String modemAvailable = FaxListBox.isDeviceConf(language);
String modemAvailableString = FaxListBox.GetDeviceConf(idsite,language);
%>
<input type="hidden" name="listacomm" id="listacomm" value="<%=modemAvailable%>"/>
<INPUT type="hidden" name="nomodactionfromide" id="nomodactionfromide" value="<%=nomodactionfromide%>"/>
<INPUT type="hidden" name="nullselected" id="nullselected" value="<%=nullselected%>"/>

<INPUT type="hidden" name="notremoveaction" id="notremoveaction" value="<%=notremoveaction%>"/>
<INPUT type="hidden" name="actionnotremoved" id="actionnotremoved" value="<%=actionnotremoved%>"/>
<INPUT type="hidden" name="doubleElement" id="doubleElement" value="<%=doubleElement%>"/>
<INPUT type="hidden" name="maxelements" id="maxelements" value="<%=maxelements%>"/>

<FORM id="frm_set_fax" name="frm_set_fax" action="servlet/master;jsessionid=<%=jsession%>" method="post">

<INPUT type="hidden" name="action_description" id="action_description" value="<%=action_description%>"/>
<INPUT type="hidden" name="cmd" id="cmd" value=""/>
<INPUT type="hidden" name="param" id="param" value=""/>
<INPUT type="hidden" name="actioncode" id="actioncode" value="<%=actioncode%>"/>

<table border="0" cellpadding="1" cellspacing="1" width="100%" height="95%">
	<tr height="5%">
		<td><p class="tdTitleTable"><%=action%> <%=action_description%></p></td>
	</tr>
	<tr height="*" valign="top" id="trContFax">
		<td>
			<FIELDSET class='field' style="height:95%">
			<LEGEND class="standardTxt"><%=fax%></LEGEND>
				<div style="height:98%;">
					<%=faxlistbox%>
				</div>
			</FIELDSET>
		</td>
	</tr>
	<tr height="5%">
		<td align="center" class="standardTxt"><font color="#FF0000">
		<%if(modemAvailable.equals("")){%>
				<%=modemAvailableString%>
		<%}else{%>
				<%=modemAvailable %><%} %></font></td>
	</tr>
</table>
</FORM>