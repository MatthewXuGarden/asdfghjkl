<%@ page language="java" 
	import="com.carel.supervisor.presentation.session.*"
	import="com.carel.supervisor.presentation.helper.*"
	import="com.carel.supervisor.dataaccess.language.*"
  import="com.carel.supervisor.presentation.bean.*"
  import="com.carel.supervisor.dataaccess.dataconfig.*"
  import="com.carel.supervisor.presentation.fs.*"
%>
<%@ page import="com.carel.supervisor.plugin.fs.FSManager" %>
<%@ page import="com.carel.supervisor.plugin.fs.FSRack" %>
<%@page import="com.carel.supervisor.plugin.base.Plugin"%>

<%
  UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
  String language = sessionUser.getLanguage();
  LangService lan = LangMgr.getInstance().getLangService(language);
  String jsession = request.getSession().getId();
			
	//messaggio da dare se non posso fare rimozione
	String control = sessionUser.getPropertyAndRemove("control");
	String msgempty = lan.getString("fs","noracksavailable"); 
	if (control==null) control="";	
  
  FSRackAux[] racks_available = FSRackBean.retrieveFreeRackOfSite(language);
  int height = sessionUser.getScreenHeight();
  int width = sessionUser.getScreenWidth();
  String html = FSRackBean.getHTMLTable(racks_available,language);
  int screen_width = sessionUser.getScreenWidth();
  int screen_height = sessionUser.getScreenHeight();

%>

<p class='standardTxt'><%=lan.getString("fs","comment3") %></p>
<br>
<input type='hidden' id='overlimit' value='<%=lan.getString("fs","overlimit") %>' /> 
<%if(racks_available!=null && racks_available.length>0) {%>
	<form id="formRacksConfigured" name="formRacksConfigured" action="servlet/master;jsessionid=<%=jsession%>" method="post">
		<div align="center">
			<%=html %>
		</div>
	</form>	
<% } else { %>
	<p class="mediumTxt" id="noracks"><b><%=msgempty %></b></p>
<% } %>
