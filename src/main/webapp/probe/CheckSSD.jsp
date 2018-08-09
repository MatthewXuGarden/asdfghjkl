<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page import="com.carel.supervisor.base.system.SanKitW" %>
<%@ page import="com.carel.supervisor.base.config.ProductInfoMgr" %>
<%
	// 100 it is used by default to avoid flase alarms if SSD statisctics are not available	
	int nSpareBlocks = 100;
	String gssd = ProductInfoMgr.getInstance().getProductInfo().get("gssd");
	if( gssd != null && gssd.equals("true") ) {
		SanKitW.getInstance().execute();
		Integer n = SanKitW.getInstance().getSpareBlocksRemaining();
		if( n != null )
			nSpareBlocks = n.intValue();
	}
%>
<%=nSpareBlocks%>
<% } else { response.sendError(404); } %>