<%@ page language="java" pageEncoding="UTF-8"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.bean.GroupVarBean"
import="com.carel.supervisor.presentation.bean.GroupVarBeanList"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUserLoc = sessionUser.getCurrentUserTransaction();
	String jsession = request.getSession().getId();
	int idsite = sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	String group = trxUserLoc.getProperty("group");
	
	int idgroup = Integer.parseInt(group);
	
	LangService lang = LangMgr.getInstance().getLangService(language);
	
	String comment = "";
	String htmlTable = "";
	
	if (idgroup!=1)
	{
		comment = lang.getString("dtlview","devicecomment2");
		GroupVarBeanList vargroups = new GroupVarBeanList(); 
		vargroups.retrieveGroupVarByIdGroup(idsite,idgroup,language);
		htmlTable = vargroups.getHTMLGroupVarTable(sessionUser.getScreenWidth(),sessionUser.getScreenHeight(),language);
	}
	else
	{
		comment = lang.getString("groupvar","comment");
	}


%>
<p class='standardTxt'><%=comment%></p>
<FORM id="frm_groupvarparam" name="frm_groupvarparam" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<div><%=htmlTable%></div>
</FORM>
