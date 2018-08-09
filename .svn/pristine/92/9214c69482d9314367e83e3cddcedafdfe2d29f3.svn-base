<%	com.carel.supervisor.presentation.session.UserSession sessionUser = com.carel.supervisor.presentation.helper.ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	if( sessionUser != null && sessionUser.getProfileCode().equals("System Administrator") ) {
%>
<%@page import="java.text.DecimalFormat"%>
<%@ page import="com.carel.supervisor.dataaccess.monitor.StatementMgr" %>
<%
  String sRefresh = request.getParameter("refresh");
  String clear = request.getParameter("clear");
  Object[][] values = null;
  if (null != sRefresh)
  {
  	values = StatementMgr.getInstance().result();
  }
  
  if (null != clear)
  {
  	StatementMgr.getInstance().clear();
  }
  
%>
<html>
<head>
<title> Statement Monitor </title>
<meta http-equiv="Content-Type" content="text/html;charset iso-8859-1"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
</head>
<script>
</script>
<BODY bgcolor="white" text="">
<center>
  <form name="frmMain" id="frmMain"  method="post" action="Statement.jsp">
  <TABLE width="75%" border="1" cellspacing="0" bordercolor="#CCCCCC">
      <TR align="center" valign="middle">
        <TD colspan="2" bgcolor="#808080">STATEMENT MONITOR</TD>
      </TR>
      <TR>
        <TD colspan="2">
    </TD></TR></TABLE><P>&nbsp;</P>
  </form>
  <p>&nbsp;</p>
  <form name="frmRefresh" id="frmRefresh" method="post" action="Statement.jsp">
  <table border="1">
    <tr>
      <td><div align="right">
          <input type="submit" name="refresh" value="refresh">
        </div></td>
        <td><div align="right">
          <input type="submit" name="clear" value="clear">
        </div></td>
    </tr>
  </table>
  </form>
  <p>&nbsp;</p>
  <table border="1">
    <tr>
      <td bgcolor="#808080" align="center">Count</td>
      <td bgcolor="#808080" align="center">Total Time</td>
      <td bgcolor="#808080" align="center">Average</td>
      <td bgcolor="#808080" align="center">Max</td>
      <td bgcolor="#808080" align="center">Min</td>
      <td bgcolor="#808080" align="center" style="width: 30%;">Statement</td>
    </tr>
    <%
      if (null != values)
      {
    	DecimalFormat df = new DecimalFormat("#.0");
        for (int i = 0; i < values[0].length; i++)
        {
    %>
            <tr>
            <td><div align="right"><%=values[1][i]%></div></td>
            <td><div align="right"><%=values[2][i]%></div></td>
            <td><div align="right"><%=df.format((float)((Long)values[2][i]).longValue()/((Long)values[1][i]).longValue())%></div></td>
            <td><div align="right"><%=values[3][i]%></div></td>
            <td><div align="right"><%=values[4][i]%></div></td>
            <td><div align="left" style="overflow:auto; width:30%;"><%=values[0][i]%></div></td>
            </tr>
    <%
          }
      }
    %>
  </table>
</center>
</BODY>
</html>
<%} else if( sessionUser != null ) response.sendError(403);
	else response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
%>