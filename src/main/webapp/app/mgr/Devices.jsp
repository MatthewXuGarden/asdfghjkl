<%@ page language="java" pageEncoding="UTF-8"
import="com.carel.supervisor.presentation.bean.DeviceListBean"
import="com.carel.supervisor.presentation.bean.DeviceBean"
import="com.carel.supervisor.dataaccess.db.DatabaseMgr"
import="com.carel.supervisor.dataaccess.db.RecordSet" %>

<%
String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+request.getServerName()+":"+request.getServerPort()+path + "/";
	String jsession = request.getSession().getId();
	DeviceListBean deviceListBean= new DeviceListBean(1,"EN_en");
	int []idDeviceList=deviceListBean.getIds();
	StringBuffer selectDevice= new StringBuffer();
	for(int i=0;i<(idDeviceList!=null?idDeviceList.length:0);i++)
	{
		selectDevice.append("<option value=\"");
		selectDevice.append(idDeviceList[i]);
		selectDevice.append("\">");
		selectDevice.append(deviceListBean.getDevice(idDeviceList[i]).getDescription());
		selectDevice.append("</option>\n");
	}//for
	
	//Combo lingua
	StringBuffer selectLanguage= new StringBuffer();
	RecordSet recordSet = DatabaseMgr.getInstance().executeQuery(null,"select languagecode,description from cflanguage");
	for(int i=0;i<(recordSet!=null?recordSet.size():0);i++)
	{
		selectLanguage.append("<option value=\"");
		selectLanguage.append(recordSet.get(i).get(0));
		selectLanguage.append("\">");
		selectLanguage.append(recordSet.get(i).get(1));
		selectLanguage.append("</option>\n");
	
	}//for
	
%>


<html>
  <head>
  
    <base href="<%=basePath%>">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    
     <link rel="stylesheet" href="stylesheet/plantVisorIE.css" type="text/css" />
  </head>
 
  <body bgcolor="#eaeaea" >
    <form name="frmDeviceList" id="frmDeviceList" action="servlet/Assistance;jsessionid=<%=jsession%>" method="post" target="Variables">
    <table  width="100%">
		<tr align="center" valign="middle">
	    	<td width="100%">
	    		<table width="100%" >
	    			<tr>
		    			<td width="35%"></td>
						<td width="30%" bgcolor="#808080" align="center">Assistance</td>
						<td width="35%"></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
	    	<td>
	    		<table width="100%" >
	    			<tr><td>&nbsp;</td></tr>
	    			<tr><td>&nbsp;</td></tr>
	    			<tr><td>&nbsp;</td></tr>
	    			<tr>
	    				<td width="10%">Device</td>
	    				<td width="40%">
	    					<select name="device">
	    						<%=selectDevice%>
	    					</select>
	    				</td>
    				</tr>
	    			<tr>
	    				<td width="10%">Language</td>
	    				<td width="40%">
	    					<select name="language">
	    						<%=selectLanguage%>
	    					</select>
	    				</td>
	    				<td width="10%">
	    					<input type="submit" width="100%" name="ok" value="Load Data">
	    				</td>
	    				<td width="40%">&nbsp;
	    				</td>
	    			</tr>
				</table>
	    	</td>
    	</tr>
		</table>
	  </form>
  </body>
</html>
