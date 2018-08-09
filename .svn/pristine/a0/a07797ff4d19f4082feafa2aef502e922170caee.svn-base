<%@ page language="java" pageEncoding="UTF-8"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.bo.master.IMaster"
import="com.carel.supervisor.presentation.tabmenu.MenuBuilder"
import="com.carel.supervisor.presentation.menu.configuration.MenuTabMgr"
%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String jsession = request.getSession().getId();
	
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUser = null;
	String pathMap = "";
	if(sessionUser != null)
	{
		trxUser = sessionUser.getCurrentUserTransaction();
		pathMap = trxUser.getProperty("pathmap");
	}
	
	if((pathMap==null)||(pathMap==""))
		pathMap="1.jsp";


	String pageLoad= "../../app/lepreview/"+pathMap;	
	
%>

<html>
<base href="<%=basePath%>">
<body>
	<table>
		<tr colspan='2'>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td><jsp:include page="<%=pageLoad%>" flush="true" /></td>
		</tr>
	</table>
</body>
</html>



	 	  