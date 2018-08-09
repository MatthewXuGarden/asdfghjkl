<%@ page language="java" pageEncoding="UTF-8"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.bean.rule.RelayBeanList"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	int idsite = sessionUser.getIdSite();
	String jsession = request.getSession().getId();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	
	// retrieve table header strings
	String rel_active = "Active"; //lan.getString("relaymgr", "active");
	String rel_idle = "Not active";  //multiLanguage.getString("relaymgr","noactive");
	String rel_offline = multiLanguage.getString("code","-101");
		
	// Relay table
	RelayBeanList relays = new RelayBeanList(idsite,language);
	relays.setScreenH(sessionUser.getScreenHeight());
	relays.setScreenW(sessionUser.getScreenWidth());
	
	// dimensions are related to 1024 * 768
	// automatically changed fit to different screen resolution 
	int tablewidht = 900;
	int tableheight = 400;
	int [] columnsSize = new int [] {450, 250, 100};
	
	// get relay table
	// this table is periodically refreshed
	String htmlTable = relays.getHTMLRelayTableRefresh(900, 400, columnsSize, idsite, language);
    String isEmpty = "true";
    if ((relays!= null) && (relays.size()>0))
  	{
  	  isEmpty="false";
    }
	
%>
<% if (isEmpty.equals("false")) { %>
<p class='standardTxt'><%=multiLanguage.getString("relaymgr","reset_relay_comment")%></p>
<% }%>

<INPUT type='hidden' id="rel_active" value="<%=rel_active%>">
<INPUT type='hidden' id="rel_idle" value="<%=rel_idle%>">
<INPUT type='hidden' id="rel_offline" value="<%=rel_offline%>">
<FORM id="frm_reset_relay" name="frm_reset_relay" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="isEmpty" value="<%=isEmpty%>"/>
	<%=htmlTable%>
</FORM>
