<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="java.util.Properties"
	import="com.carel.supervisor.presentation.bo.BRDataTransfer"

%>
<%	

UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
LangService l = LangMgr.getInstance().getLangService(language);

StringBuffer combohour = new StringBuffer();


BRDataTransfer bo = ((BRDataTransfer)sessionUser.getCurrentUserTransaction().getBoTrx());
//lettura file properties
Properties p = bo.getProperties();

int failsleeptime = Integer.parseInt(p.getProperty("failsleeptime"))/60000;
int successsleeptime = Integer.parseInt(p.getProperty("successsleeptime"))/60000;
int pausesitecount = Integer.parseInt(p.getProperty("pausesitecount"));
int pausesleeptime = Integer.parseInt(p.getProperty("pausesleeptime"))/60000;
int siteretrytimes = Integer.parseInt(p.getProperty("siteretrytimes"));
int processretrytimes = Integer.parseInt(p.getProperty("processretrytimes"));
int maxconnectiontime = Integer.parseInt(p.getProperty("maxconnectiontime"))/60000;
int chunksize = Integer.parseInt(p.getProperty("chunksize"))/1024;


int time = bo.getStarthour();

for (int i=0;i<24;i++)
{
	String checked = "";
	if (time==i)
	{
		checked="selected";
	}
	if (i<10)
		combohour.append("<option "+checked+" value='0"+i+"'>0"+i+":00</option>\n");
	else
		combohour.append("<option "+checked+" value='"+i+"'>"+i+":00</option>\n");
}

%>

<p class='standardTxt'><%=l.getString("r_datatransfer","tab3comment")%></p>
<br>
<form action="servlet/master;jsessionid=<%=jsession%>" method="post" id='amm_form' name='amm_form'>
<fieldset class='field'>
<table class='standardTxt' cellpadding="0" cellspacing="0" border='0'>
<tr height="40px">
	<td width="50%"><%=l.getString("r_datatransfer","pausefault")%></td>
	<td width="10%" align="left">&nbsp;<input name='failsleeptime' id='failsleeptime' class='standardTxt' type='text' size="3" value='<%=failsleeptime%>'/></td>
	<td width="40%" align="left"><%=l.getString("r_datatransfer","minuts")%></td>
</tr>
<tr height="40px">
	<td width="50%"><%=l.getString("r_datatransfer","pausesuccess")%></td>
	<td width="10%" align="left">&nbsp;<input name='successsleeptime' id='successsleeptime' class='standardTxt' type='text' size="3" value='<%=successsleeptime%>'/></td>
	<td width="40%" align="left"><%=l.getString("r_datatransfer","minuts")%></td>
</tr>
<tr height="40px">
	<td width="50%"><%=l.getString("r_datatransfer","pausegroup")%></td>
	<td width="10%" align="left">&nbsp;<input name='pausesleeptime' id='pausesleeptime' class='standardTxt' type='text' size="3" value='<%=pausesleeptime%>'/></td>
	<td width="40%" align="left"><%=l.getString("r_datatransfer","minuts")%></td>
</tr>
<tr height="40px">
	<td width="50%"><%=l.getString("r_datatransfer","sitegroup")%></td>
	<td width="10%" align="left">&nbsp;<input name='pausesitecount' id='pausesitecount' class='standardTxt' type='text' size="3" value='<%=pausesitecount%>'/></td>
	<td width="40%" align="left"></td>
</tr>
<tr height="40px">
	<td width="50%"><%=l.getString("r_datatransfer","siteretry")%></td>
	<td width="10%" align="left">&nbsp;<input name='siteretrytimes' id='siteretrytimes' class='standardTxt' type='text' size="3" value='<%=siteretrytimes%>'/></td>
	<td width="40%" align="left"></td>
</tr>
<!--  tr height="40px">
	<td width="50%"><%=l.getString("r_datatransfer","processretry")%></td>
	<td width="10%" align="left">&nbsp;<input name='processretrytimes' id='processretrytimes' class='standardTxt' type='text' size="3" value='<%=processretrytimes%>'/></td>
	<td width="40%" align="left"></td>
</tr -->
<tr height="40px">
	<td width="50%"><%=l.getString("r_datatransfer","starttime")%></td>
	<td width="10%" align="left">&nbsp;<select name='starthour' id='starthour' class='standardTxt'><%=combohour.toString()%></td>
	<td width="40%" align="left"></td>
</tr>
<tr height="40px">
	<td width="50%"><%=l.getString("r_datatransfer","connectiontimeout")%></td>
	<td width="10%" align="left">&nbsp;<input name='maxconnectiontime' id='maxconnectiontime' class='standardTxt' type='text' size="3"  value='<%=maxconnectiontime%>'/></td>
	<td width="40%" align="left"><%=l.getString("r_datatransfer","minuts")%></td>
</tr>
<!--  tr height="40px">
	<td width="50%"><%=l.getString("r_datatransfer","maxdimension")%></td>
	<td width="10%" align="left">&nbsp;<input name='chunksize' id='chunksize' class='standardTxt' type='text' size="3"  value='<%=chunksize%>'/></td>
	<td width="40%" align="left"><%=l.getString("r_datatransfer","kbyte")%></td>
</tr -->
</table> 
</fieldset>
</form>
