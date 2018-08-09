<%@ 
page language="java" import="java.util.*" pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.copydevice.PageImpExp"
	import="com.carel.supervisor.presentation.bean.GroupListBean"
	import="com.carel.supervisor.presentation.bean.DeviceListBean"
	import="com.carel.supervisor.presentation.bean.DeviceBean"
	import="com.carel.supervisor.presentation.bo.helper.Propagate"
%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
String browser  = sessionUser.getUserBrowser();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
int idsite = sessionUser.getIdSite();
StringBuffer combo_dev = new StringBuffer();
GroupListBean groups = sessionUser.getGroup();
int[] ids_group = groups.getIds();
DeviceListBean deviceList =  new DeviceListBean(idsite,language,ids_group);

int id_master = Integer.parseInt(sessionUser.getProperty("iddev"));
DeviceBean tmp_dev = null;
DeviceBean master = deviceList.getDevice(id_master);
int[] ids = deviceList.getIds();
combo_dev.append("<OPTION value='-1'>-------------------</OPTION>");
Set<Integer> notenabled = Propagate.canPropagateGraphConf(idsite,language,master.getIddevice(),ids);
for (int i=0;i<deviceList.size();i++)
{
	tmp_dev = deviceList.getDevice(ids[i]);
	if (master.getIddevice()!=tmp_dev.getIddevice()&&
		master.getIddevmdl()==tmp_dev.getIddevmdl()&&
		!notenabled.contains(tmp_dev.getIddevice()))
	{
		combo_dev.append("<OPTION value='"+tmp_dev.getIddevice()+"'>"+tmp_dev.getDescription()+"</OPTION>");
	}
}
%>
<script>

function getMasterSlave()
{
	var master = document.getElementById("master").value;
	var slave = document.getElementById("slave").value;
	
	if (slave==-1)
	{
		alert(document.getElementById("noselecteddevice").value);
		return null;
	}
	else
	{
		return master+";"+slave; 
	}
}
</script>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title><%=lan.getString("propagate","titlemodalwindow")%></title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
       
    <link rel="stylesheet" type="text/css" href="stylesheet/plantVisor<%=browser%>.css">
    <script type="text/javascript" src="scripts/app/propagate.js"></script>
   
  </head>
  <input type='hidden' id='master' value='<%=id_master%>'>
  <input type='hidden' id='noselecteddevice' value='<%=lan.getString("propagate","noselecteddevice")%>'>
  <body style='background-color: #EAEAEA;'>
  	<br>
  	<TABLE>
  		<TR>
  			<TD class='standardTxt'><b><%=lan.getString("propagate","selectdevice")%></b></TD>
  		</TR>
  		<TR>
  			<TD>
  				<select style='width:400px' id='slave'>
  					<%=combo_dev.toString()%>
  				</select>		
  			</TD>
  			<TD>
  				<img style='cursor:pointer;' src='images/actions/save_on.png' title='<%=lan.getString("dtlview","save")%>' onclick="window.returnValue=getMasterSlave();window.close();"/>
  			</TD>
  		</TR>
  	</TABLE> 
  </body>
</html>

