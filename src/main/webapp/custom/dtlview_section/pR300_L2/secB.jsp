<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
%>
<jsp:useBean id="CurrUnit" class="com.carel.supervisor.presentation.sdk.obj.CurrUnit" scope="session"/>

<div class="tdfisa">
	<%=CurrUnit.getVariable("Sys_On_L2").getRefreshableAssint("<img valign='middle' src='images/led/L0.gif'/> L2 - UNIT OFF;<img valign='middle' src='images/led/L1.gif'/> L2 - UNIT ON","***")%>
</div>