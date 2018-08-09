<%@ page language="java"
import="java.util.*"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.lucinotte.LNDays"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>

<%
UserSession sessUsr = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);

int idsite = sessUsr.getIdSite();

String language = sessUsr.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);

boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();

String jsession = request.getSession().getId();

HashMap<Integer, String> groups = LNDays.getGroupsMap();
// richiamo il metodo con due parametri aggiuntivi facoltativi per la tastiera virtuale
String table = LNDays.getHtmlExceptionDays(language, idsite, sessUsr.getScreenHeight(), sessUsr.getScreenWidth(), groups);

String menu_item = lan.getString("menu","lucinotte");
String ctrlval = lan.getString("lucinotte","ctrlval");
String lastrowempty = lan.getString("lucinotte","lastrowempty");
String datanotincal = lan.getString("lucinotte","datanotincal");
String addnewdate = lan.getString("lucinotte","adddate");
String combo_months = LNDays.getOptionsMonths(language, idsite, -1);
String combo_days = LNDays.getOptionsDays(-1);
String combo_grps = LNDays.getOptionsGroups(language, idsite, -1, groups);
%>

<input type="hidden" id="cmb_grps" value="<%=combo_grps%>" />
<input type="hidden" id="menu_item" value="<%=menu_item%>" />
<input type="hidden" id="cmb_months" value="<%=combo_months%>" />
<input type="hidden" id="cmb_days" value="<%=combo_days%>" />
<input type="hidden" id="ctrlval" value="<%=ctrlval%>" />
<input type="hidden" id="lastrowempty" value="<%=lastrowempty%>" />
<input type="hidden" id="datanotincal" value="<%=datanotincal%>" />
<input type="hidden" id="ctrlval" value="<%=lan.getString("lucinotte","ctrlval")%>" />
<input type="hidden" id="addnewdate" value="<%=addnewdate%>" />
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>

<p class='standardTxt'><%=lan.getString("lucinotte","comment5")%></p>	
<br />
<FORM name="lcntdays_frm" id="lcntdays_frm" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
<input type='hidden' id='cmd' name='cmd'/>
<%=table%>
</FORM>
