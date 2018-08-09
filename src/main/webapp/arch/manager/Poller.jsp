<%@ page language="java"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String jsession = request.getSession().getId();
%>

<html>
<head>
	<base href="<%=basePath%>">
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">
  <script>
  var id = 0;
  
  function startThreadPoller()
  {
	var lock = top.frames['body'].frames['bodytab'].R_lock;
  	if((lock != undefined) && (lock != 0))
  	{
  		actionPollerRefresh();
  	}
  }
  
  function actionPollerRefresh()
  {
  	
  		createDivPollerDataRefresh();
	  	document.frmpoller.submit();
  }

  function actionPollerPage(){
  	
			createDivPollerDataPage();
	  	document.frmpoller.submit();
  }

  
  
  function createDivPollerDataRefresh()
  {
  	var oLswTmp = null;
  	var sHtml = "";
   	if(top.frames['body'].frames['bodytab'].oLswContainer != null)
  	{
  		for(var i=0; i<top.frames['body'].frames['bodytab'].oLswContainer.getSize(); i++)
  		{
  			oLswTmp = top.frames['body'].frames['bodytab'].oLswContainer.getLswByIdx(i);
  			if(oLswTmp)
  				sHtml += oLswTmp.getDataToPostRefresh();
  		
  		}
  		document.getElementById("divpollerdata").innerHTML = sHtml;
  	}
  }
  
  function createDivPollerDataPage()
  {
  	var oLswTmp = null;
  	var sHtml = "";
   	if(top.frames['body'].frames['bodytab'].oLswContainer != null)
  	{
  		for(var i=0; i<top.frames['body'].frames['bodytab'].oLswContainer.getSize(); i++)
  		{
  			oLswTmp = top.frames['body'].frames['bodytab'].oLswContainer.getLswByIdx(i);
  			if(oLswTmp)
  				sHtml += oLswTmp.getDataToPostPage();
  		
  		}
  		document.getElementById("divpollerdata").innerHTML = sHtml;
  	}
  }
  
  
  
  </script>
</head>
<body>
<form method="post" id="frmpoller" name="frmpoller" action="servlet/Refresh;jsessionid=<%=jsession%>" target="Receiver">
<div id="divpollerdata"></div>
</form>
</body>
</html>
