<%@ page language="java" import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.dataconfig.UtilBean"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.bean.rule.ActionBeanList"
import="com.carel.supervisor.presentation.bean.ProfileBean"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<% 
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
String isremoved = ut.remProperty("isremoved");
int idsite= sessionUser.getIdSite();
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);

//controllo permessi	
	String perm = "";
	int permission = sessionUser.getVariableFilter();
	if (permission==0)
		perm = "disabled";
		
	boolean permict = (permission==0)?false:true;

String t = sessionUser.getProperty("sched");
boolean issched = t.equalsIgnoreCase("TRUE");

String name = lan.getString("action","name");
String nodescription = lan.getString("action","nodescription");
String description = lan.getString("action","description");
String actioncomment1 = lan.getString("action","actioncomment1");
String actioncomment2 = lan.getString("action","actioncomment2");

String actionnotremoved = lan.getString("alrsched","actionnotremoved");
String noremactionfromide = lan.getString("ide","noremactionfromide");
String confirmactiondel = lan.getString("alrsched","confirmactiondel");

ActionBeanList actions = new ActionBeanList(idsite,language,issched,true);
actions.setScreenH(sessionUser.getScreenHeight());
actions.setScreenW(sessionUser.getScreenWidth());
String htmlActionTable = actions.getHTMLActionTable(idsite,language,"",150,900,issched,permict);

String dCode = "F";
dCode = sessionUser.getPropertyAndRemove("duplicatecode");
boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey(); 
if(dCode == null)
	dCode = "F";
%>
<input type="hidden" id="duplicatecode" value="<%=dCode%>">
<input type="hidden" id="duplicatecodemsg" value="<%=lan.getString("alrsched","msgduplicate")%>">
<INPUT type="hidden" id="confirmactiondel" value="<%=confirmactiondel%>"/>
<INPUT type="hidden" name="noremactionfromide" id="noremactionfromide" value="<%=noremactionfromide%>"/>
<INPUT type="hidden" name="actionnotremoved" id="actionnotremoved" value="<%=actionnotremoved%>"/>
<INPUT type="hidden" name="isremoved" id="isremoved" value="<%=isremoved%>"/>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<p class="StandardTxt"><%=actioncomment1%></p>
<INPUT type="hidden" name="nodescription" id="nodescription" value="<%=nodescription%>"/>
<FORM id="frm_rules_actions" name="frm_rules_actions" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<table border="0" width="100%" height="89%" cellspacing="1" cellpadding="1">
<tr height="100%" valign="top" id="trActionList">
		<td><%=htmlActionTable%></td>
</tr>
<TR><td>&nbsp;</td></TR>
<TR>
	<TD>
		<p class="StandardTxt"><%=actioncomment2%></p>
		<FIELDSET class='field'>
		<LEGEND class="standardTxt"><%=description%></LEGEND>
		<TABLE border="0">
			<TR class="standardTxt">
				<TD><%=name%></TD><TD class="standardTxt"><INPUT <%=perm%> name="description_txt" id="description_txt" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' type="text" size="30" maxlength="30" onblur="noBadCharOnBlur(this,event);" onkeydown='checkBadChar(this,event);'/> *</TD>
			</TR>
		</TABLE>
		</FIELDSET>
	</TD>
</TR>
</table>
<INPUT type="hidden" name="cmd" id="cmd" value=""/>
<INPUT type="hidden" name="action_to_remove" id="action_to_remove" value=""/>
</FORM>



