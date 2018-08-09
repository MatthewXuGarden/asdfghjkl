<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
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
<%@ page import="com.carel.supervisor.presentation.rule.SmsListBox" %>
<%

UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();
UserTransaction ut = sessionUser.getCurrentUserTransaction();
String notremoveaction = ut.remProperty("notremoveaction");
String actionnotremoved = lan.getString("alrsched","actionnotremoved2");

String email = lan.getString("action","email");
String doubleElement = lan.getString("dbllistbox","doublelement");
String nullselected = lan.getString("dbllistbox","nullselected");
//20090119 - BUG #5289 RF Check on max elements
String maxelements = lan.getString("dbllistbox", "maxelements");
String action = lan.getString("setaction","action");
String nomodactionfromide = lan.getString("ide","nomodactionfromide");

boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();

//retrieve da sessione
int actioncode = Integer.parseInt(sessionUser.getProperty("actioncode"));
ActionBeanList actionList = new ActionBeanList();
String action_description = ActionBeanList.getDescription(idsite,actioncode);

//E-Mail
EmailListBox emaillistboxobj = new EmailListBox();
emaillistboxobj.setScreenH(sessionUser.getScreenHeight());
emaillistboxobj.setScreenW(sessionUser.getScreenWidth());
String emaillistbox =emaillistboxobj.getEmailListBox(idsite,actioncode,language);

String modemAvailableString = emaillistboxobj.GetDeviceConf(idsite,language);
%>
<INPUT type="hidden" name="nomodactionfromide" id="nomodactionfromide" value="<%=nomodactionfromide%>"/>
<INPUT type="hidden" name="nullselected" id="nullselected" value="<%=nullselected%>"/>
<INPUT type="hidden" name="notremoveaction" id="notremoveaction" value="<%=notremoveaction%>"/>
<INPUT type="hidden" name="actionnotremoved" id="actionnotremoved" value="<%=actionnotremoved%>"/>
<INPUT type="hidden" name="doubleElement" id="doubleElement" value="<%=doubleElement%>"/>
<INPUT type="hidden" name="maxelements" id="maxelements" value="<%=maxelements%>"/>
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>

<FORM id="frm_set_email" name="frm_set_email" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" name="cmd" id="cmd" value=""/>
<INPUT type="hidden" name="param" id="param" value=""/>
<INPUT type="hidden" name="actioncode" id="actioncode" value="<%=actioncode%>"/>
<INPUT type="hidden" name="action_description" id="action_description" value="<%=action_description%>"/>

<table border="0" cellpadding="1" cellspacing="1" width="100%" height="95%">
	<tr height="5%">
		<td><p class="tdTitleTable"><%=action%> <%=action_description%></p></td>
	</tr>
	<tr height="*" valign="top" id="trContFax">
		<td>
			<FIELDSET class='field' style="height:92%">
				<LEGEND class="standardTxt"><%=email%></LEGEND>
				<div style="height:98%;">
					<%=emaillistbox%>
				</div>
			</FIELDSET>
		</td>
	</tr>
	<tr height="5%">
		<td align="left"><font color= "#FF0000"><%=modemAvailableString%></font></td>
	</tr>
</table>
</FORM>




