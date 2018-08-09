<%	com.carel.supervisor.presentation.session.UserSession sessionUser = com.carel.supervisor.presentation.helper.ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	if( sessionUser != null && sessionUser.getProfileCode().equals("System Administrator") ) {
%>
<%@ page import="com.carel.supervisor.base.timer.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.carel.supervisor.base.timer.impl.TimerContainerDummy" %>
<%@ page import="com.carel.supervisor.base.config.BaseConfig" %>
<%@ page import="com.carel.supervisor.director.DirectorMgr" %>
<%
  String sStart = request.getParameter("start");
  String sStop = request.getParameter("stop");
  String sLoad = request.getParameter("load");

  if ((null != sStart) && (!sStart.equals("")))
  	DirectorMgr.getInstance().startEngine();
  	
  if ((null != sStop) && (!sStop.equals("")))
  	DirectorMgr.getInstance().stopEngine();
  	
  if ((null != sLoad) && (!sLoad.equals("")))
  	DirectorMgr.getInstance().reloadConfiguration();
  	
  boolean bActivated = false;
  boolean bLoad = false;
  bActivated = DirectorMgr.getInstance().isStarted();
  bLoad = DirectorMgr.getInstance().isInitialized();
       
%>
<html>
<head>
<title> Stop/Start </title>
<meta http-equiv="Content-Type" content="text/html;charset iso-8859-1"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
</head>
<script>
</script>
<BODY bgcolor="#eaeaea" text="">
<center>
  <FORM name="frmMain" id="frmMain" method="post" action="Stop.jsp">
    <TABLE width="75%" border="1" cellspacing="0" bordercolor="#CCCCCC">
      <TR align="center" valign="middle">
        <TD colspan="2" bgcolor="#808080">STOP/START</TD>
      </TR>
      <TR>
        <TD colspan="2">
    </TD></TR></TABLE>
  <P>&nbsp;</P>
  <TABLE width="50%" border="1">
    <TR>
      <TD width="33%">Status</TD>
      <TD width="33%">
        <DIV align="right"><B>
          <%=(bActivated?"ACTIVE":"DEACTIVE")%></B>
        </DIV>
      </TD>
      <TD width="33%" align="right">
      <TABLE width="100%">
        <TR>
          <TD width="50%" align="center">
            <INPUT type="submit" name="start" value="start">
          </TD>
          <TD width="50%" align="center">
            <INPUT type="submit" name="stop" value="stop">
          </TD>
        </TR>
      </TABLE>
        </TD>
    </TR>
    <TR>
    <TD width="33%">Config</TD>
      <TD width="33%">
        <DIV align="right"><B>
          <%=(bLoad?"OK":"KO")%></B>
        </DIV>
      </TD>
      <TD width="33%" align="right">
      <TABLE width="100%">
        <TR>
          <TD width="50%" align="center">
            <INPUT type="submit" name="load" value="load">
          </TD>
        </TR>
      </TABLE>
        </TD>
    </TR>
    <TR>
    </TR>
  </TABLE>
  </FORM><P>&nbsp;</P><P>&nbsp;</P>
</center>
</BODY>
</html>
<%} else if( sessionUser != null ) response.sendError(403);
	else response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
%>