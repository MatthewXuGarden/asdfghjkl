<% if( com.carel.supervisor.director.guardian.GuardianCheck.isGuardian(request.getParameter("key")) ) { %>
<%@ page language="java" pageEncoding="UTF-8" 
import="com.carel.supervisor.base.config.BaseConfig"
%>
<%@ page import="com.carel.supervisor.base.system.SystemInfoExt" %>
<%@ page import="com.carel.supervisor.base.conversion.StringUtility" %>
<%@ page import="com.carel.supervisor.base.conversion.Replacer" %>
<%
//Espresso in Kb
String disk = BaseConfig.getProperty("dbdisk");
if ((null == disk) || (disk.equals("")))
{
	disk = "c:";
}
String usageTmp = SystemInfoExt.getInstance().getDiskUsage(disk);
String[] tmp = StringUtility.split(usageTmp,";");
tmp[0] = tmp[0].substring(0, tmp[0].length() -2).trim();
tmp[1] = tmp[1].substring(0, tmp[1].length() -2).trim();
tmp[0] = Replacer.replace(tmp[0],".","");
tmp[1] = Replacer.replace(tmp[1],".","");
tmp[0] = Replacer.replace(tmp[0],",","");
tmp[1] = Replacer.replace(tmp[1],",","");
double free = Double.parseDouble(tmp[0]);
double total = free+Double.parseDouble(tmp[1]);
%>
<%=((long)((free/total)*100))%>
<% } else { response.sendError(404); } %>