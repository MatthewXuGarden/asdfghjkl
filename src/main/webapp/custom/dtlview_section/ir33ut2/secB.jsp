<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
%>
<jsp:useBean id="CurrUnit" class="com.carel.supervisor.presentation.sdk.obj.CurrUnit" scope="session"/>

<div class="tdfisa">
	<%=CurrUnit.getVariable("D36").getRefreshableAssint("<img src='images/led/L1.gif'> UNIT ON;<img src='images/led/L0.gif'> UNIT OFF","***")%>
</div>