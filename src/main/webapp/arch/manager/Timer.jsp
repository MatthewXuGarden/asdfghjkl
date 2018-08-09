<%	com.carel.supervisor.presentation.session.UserSession sessionUser = com.carel.supervisor.presentation.helper.ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	if( sessionUser != null && sessionUser.getProfileCode().equals("System Administrator") ) {
%>
<%@ page import="com.carel.supervisor.base.timer.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.carel.supervisor.base.timer.impl.TimerContainerDummy" %>
<%@ page import="com.carel.supervisor.base.config.BaseConfig" %>
<%
  String sRefresh = request.getParameter("refresh");
  String sStart = request.getParameter("start");
  String sStop = request.getParameter("stop");

  String sClear = request.getParameter("clear");
  String sLevel = request.getParameter("level");
  boolean bLow = false;
  boolean bMedium = false;
  boolean bHigh = false;
  boolean bActivated = false;

	ITimerController timerControl = TimerMgr.getInstance().getController();
    
    
  if (null != sLevel)
  {
        
    if (!sLevel.equals(""))
    {
      if (sLevel.equals("low"))
      {
    	timerControl.setGlobalLevel(TimerMgr.LOW);
        bLow = true;
      }
      else if (sLevel.equals("medium"))
      {
        timerControl.setGlobalLevel(TimerMgr.MEDIUM);
        bMedium = true;
      }
      else if (sLevel.equals("high"))
      {
        timerControl.setGlobalLevel(TimerMgr.HIGH);
        bHigh = true;
      }
    }
  }
  else
  {
    if (timerControl.checkCondition(TimerMgr.LOW))
      bLow = true;
    else if (timerControl.checkCondition(TimerMgr.MEDIUM))
      bMedium = true;
    else if (timerControl.checkCondition(TimerMgr.HIGH))
      bHigh = true;
  }

  if (null != sStart)
  {
    timerControl.activate();
    bActivated = true;
  }

  if (null != sStop)
  {
    timerControl.deActivate();
    bActivated = false;
  }

  bActivated = timerControl.isActivated();

  try
  {
    if (sClear.equals("true"))
    {
      timerControl.activate();
    }
  }
  catch(Exception e)
  {
  }

  if (null != sClear)
  {
    if (!sClear.equals(""))
    {
      timerControl.clear();
    }
  }

  ITimerContainer timerContainer = TimerMgr.getInstance().getContainer();
  String[] names = null;
        
	if (null != sRefresh)
  {
    names = timerContainer.getTimersName();
  }
%>
<html>
<head>
<title> Performance Monitor </title>
<meta http-equiv="Content-Type" content="text/html;charset iso-8859-1"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
</head>
<script>
</script>
<BODY bgcolor="#eaeaea" text="">
<center>
  <form name="frmMain" id="frmMain" method="post" action="Timer.jsp">
    <table width="75%" border="1" cellspacing="0" bordercolor="#CCCCCC">
      <tr align="center" valign="middle">
        <td colspan="2" bgcolor="#808080">PERFORMANCE MONITOR</td>
      </tr>
      <tr>
        <td colspan="2">
    </table>
  <p>&nbsp;</p>
  <table width="50%" border="1">
    <tr>
      <td width="33%">Status</td>
      <td  width="33%">
        <div align="right"><b>
          <%=(bActivated?"ACTIVE":"DEACTIVE")%></b>
        </div>
      </td>
      <td  width="33%" align="right">
      <table width="100%" >
        <tr>
          <td width="50%" align="center">
            <input type="submit" name="start" value="start">
          </td>
          <td width="50%" align="center">
            <input type="submit" name="stop" value="stop">
          </td>
        </tr>
      </table>
        </td>
    </tr>
    <tr>
      <td>Level</td>
      <td>
        <div align="right">
          <select name="level">
            <option value="low" <%=(bLow?"selected":"")%>>low</option>
            <option value="medium" <%=(bMedium?"selected":"")%>>medium</option>
            <option value="high" <%=(bHigh?"selected":"")%>>high</option>
          </select>
        </div>
      </td>
      <td>
        <div align="right">
          <input type="submit" name="ok" value="submit">
        </div>
      </td>
    </tr>
    <tr>
    <td>
    	Activation Time
    </td>
    	<%=TimerMgr.getInstance().getActivationTime()%>
    <td>
    </td>
    </tr>
  </table>
  </form>
  <p>&nbsp;</p>
  <form name="frmRefresh" id="frmRefresh" method="post" action="Timer.jsp">
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
      <td bgcolor="#808080" align="center">Timer</td>
      <td bgcolor="#808080" align="center">Count</td>
	  <td bgcolor="#808080" align="center">Total Time (msec)</td>
	  <td bgcolor="#808080" align="center">Avarage Time (msec)</td>
	  <td bgcolor="#808080" align="center">Max Time (msec)</td>
	  <td bgcolor="#808080" align="center">Min Time (msec)</td>
    </tr>
    <%
      if (null != names)
      {
          Arrays.sort(names);
          ITimerData timerData = null;
        for (int i = 0; i < names.length; i++)
        {
            timerData = timerContainer.getData(names[i]);
    %>
            <tr>
            <td><div align="left"><%=names[i]%></div></td>
            <td><div align="right"><%=timerData.count()%></div></td>
            <td><div align="right"><%=timerData.totalTime()%></div></td>
            <td><div align="right"><%=timerData.average()%></div></td>
            <td><div align="right"><%=timerData.max()%></div></td>
            <td><div align="right"><%=timerData.min()%></div></td>
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