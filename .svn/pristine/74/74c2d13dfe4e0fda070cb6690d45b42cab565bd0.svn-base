<%@ page language="java" 
	
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.base.system.SystemInfoExt"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.switchtech.SwitchConfigList"
	import="com.carel.supervisor.presentation.switchtech.SwitchConfig"
	import="com.carel.supervisor.plugin.switchtech.SwitchMgr"
	import="com.carel.supervisor.plugin.switchtech.Switch"
	
%>
<%	
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
LangService l = LangMgr.getInstance().getLangService(language);
int idsite = sessionUser.getIdSite();

//creazione combo switch
SwitchConfigList switch_list = new SwitchConfigList(idsite);
int num_switch = switch_list.size();
StringBuffer comboswitch = new StringBuffer();
comboswitch.append("<option value='-1'>----------------------</option>\n");
SwitchConfig tmp = null;
for (int i=0;i<num_switch;i++)
{
	tmp = switch_list.getSwitch(i);
	comboswitch.append("<option value='"+tmp.getIdswitch()+"'>"+tmp.getDescription()+"</option>\n");
}

SwitchMgr mgr = SwitchMgr.getInstance();
int num = mgr.getSwitchNumber();
Switch tmp_sw = null;
StringBuffer str = new StringBuffer();
for (int i=0;i<num;i++)
{
	tmp_sw = mgr.getSwitch(i);
	if (tmp_sw!=null&&tmp_sw.getId_var_startalarm()!=-1)
	{
		str.append(tmp_sw.getDescription());
		str.append(", ");
	}
}
String alarmSwitch = str.toString();
Boolean hasAlarm = false;
if (alarmSwitch.equals(""))
{
	alarmSwitch = "<TD style='color:GREEN'><B>"+l.getString("switch","noswitchalarm")+"</B></TD>";
}
else
{
	alarmSwitch = "<TD style='color:RED'><B>"+alarmSwitch.substring(0,alarmSwitch.length()-2)+"</B></TD>";
	hasAlarm = true;
}

%>

<input type='hidden' id='s_hours' value='<%=l.getString("switch","hours")%>'>
<input type='hidden' id='s_eev' value='<%=l.getString("switch","eev")%>'>
<input type='hidden' id='s_tev' value='<%=l.getString("switch","tev")%>'>
<input type='hidden' id='s_enabled' value='<%=l.getString("switch","enabled")%>'>
<input type='hidden' id='s_disabled' value='<%=l.getString("switch","disabled")%>'>
<input type='hidden' id='s_started' value='<%=l.getString("mgr","active")%>'>
<input type='hidden' id='s_stopped' value='<%=l.getString("mgr","disactive")%>'>
<input type='hidden' id='s_noswitchalarm' value='<%=l.getString("switch","noswitchalarm")%>'>
<input type='hidden' id='s_autoswitch' value='<%=l.getString("switch","autoswitch")%>'>
<input type='hidden' id='s_manualswitch' value='<%=l.getString("switch","manualswitch")%>'>
<input type='hidden' id='s_nextswitchalarm' value='<%=l.getString("switch","nextswitchalarm") %>'>
<input type='hidden' id='s_hasAlarm' value='<%=hasAlarm %>'>




<p class='standardTxt'><%=l.getString("switch","pagecomment1")%></p>

<FIELDSET style='width:97%'><LEGEND class='standardTxt'><%=l.getString("grpview","alarms") %></LEGEND>
<table class='standardTxt' cellpadding="0" cellspacing="0" width="100%" >
	<TR>
		<TD>
			<table class='standardTxt' cellpadding="0" cellspacing="0">
			<tr height="15px"><td>&nbsp;</td></tr>
				<tr>
					<td width="250px"><%=l.getString("switch","switchonalarm")%>:&nbsp;</td>
					 <%=alarmSwitch%>
				</tr>
			</table>
		</TD>
	</TR>
	<tr height="15px"><td>&nbsp;</td></tr>
</table>
</FIELDSET>
<br>
<FIELDSET style='width:97%;' ><LEGEND class='standardTxt'><%=l.getString("switch","detail")%></LEGEND>
<table class='standardTxt' cellpadding="0" cellspacing="0" border='0'>
	<tr height="15px"><td>&nbsp;</td></tr>
	<tr align="left">
		<TD width="250px"><%=l.getString("switch","switchinstance")%></TD>
		<TD align="left"><SELECT class='standardTxt' id='switch_id' name='switch_id' onchange='onChangeSwitch();'>
				<%=comboswitch.toString()%>
			</SELECT>
		</TD>
	</tr>
	<tr height="15px"><td>&nbsp;</td></tr>
	<TR>
		<TD><%=l.getString("switch","status")%>:</TD>
		<TD  align="left"><b><p id='status'></p></b></TD>
	</TR>
	<tr height="15px"><td>&nbsp;</td></tr>
	<TR>
		<TD><%=l.getString("switch","techinuse")%>:</TD>
		<TD  align="left"><b> <p id='tech' ></p></b></TD>
	</TR>
	<tr height="15px"><td>&nbsp;</td></tr>
	<tr>
		<TD width="250px"><b><p id='typeswitch'></p></b></TD>
		<TD width="150px" align="left"><b><p id='autoswitch'></p></b></TD>
		<TD width="30px"></TD>
		<TD width="250px" id="range"><%=l.getString("switch","range")%>:</TD>
		<TD width="150px" id="range2"><b><div id='hour'></div></b></TD>
	</TR>
	<tr height="15px"><td>&nbsp;</td></tr>
	<TR>
		<TD><%=l.getString("switch","devicenumber")%>:</TD>
		<TD><b><p id='devicenumber'></p></b></TD>
		<TD></TD>
		<TD></TD>
		<TD></TD>
	</TR>
 	<tr height="15px"><td>&nbsp;</td></tr>
	<TR id="times">
		<TD><%=l.getString("switch","lastswitch")%>:</TD>
		<TD><b><p id='lastswitch'></p></b></TD>
		<TD></TD>
		<TD><%=l.getString("switch","nextswitch")%>:</TD>
		<TD><b><p id='nextswitch'></p></b></TD>
	</TR>	
</table>
</FIELDSET>