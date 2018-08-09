<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.IProductInfo"
import="com.carel.supervisor.base.config.ProductInfoMgr"
import="supervisor.Login"

%>

<%
	UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = userSession.getCurrentUserTransaction();
	String language = userSession.getLanguage();
	String jsession = userSession.getSessionId();
	//String comefrom=userSession.getProperty("comefrom");
	LangService lan = LangMgr.getInstance().getLangService(language); 
	
	IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
	String wizarddone = product.get(Login.WIZARDDONE);
	String checked;
	if(wizarddone.equals("0"))
	{
		checked = "";
	}
	else
	{
		checked = "checked";
	}
	String welcome1 = lan.getString("wizard","welcome1");
	String welcome2 = lan.getString("wizard","welcome2");
	String start = lan.getString("wizard","start");
	String exit = lan.getString("wizard","exit");
	String wizardhideconfirm = lan.getString("wizard","wizardhideconfirm");
%>
<input type="hidden" id="isrestart" value="true">
<table  width='100%' height='100%' border=0>
	<tr>
		<td align='center' valign='top'>
			<table width='98%' height='60%' border=0>
				<tr>
					<td><img src="images/pvpro_touch.jpg"></td>
					<td class='standardTxt'>
						<span class='wizardwelcome'><%= welcome1 %></span>
						<br/><%= welcome2%>
						<br/><br/>
							<input type="checkbox" id='wizarddonebox' <%=checked%> onclick="wizarddone();" ><%= wizardhideconfirm%>					
					</td>
					<td><img src="images/pvpro_case.jpg"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
