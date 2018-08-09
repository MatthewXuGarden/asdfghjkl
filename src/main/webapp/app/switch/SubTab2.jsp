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

StringBuffer combohour = new StringBuffer();
combohour.append("<option value='-1'>------</option>\n");
for (int i=0;i<24;i++)
{
	if (i<10)
		combohour.append("<option value='0"+i+"'>0"+i+":00</option>\n");
	else
		combohour.append("<option value='"+i+"'>"+i+":00</option>\n");
}
String selected_switch_id = sessionUser.getCurrentUserTransaction().remProperty("selected_switch_id");
%>
<input type='hidden' id='selectswitchalert' value='<%=l.getString("switch","selectswitchalert")%>'>
<input type='hidden' id='power_on' value='<%=l.getString("mgr","start")%>'>
<input type='hidden' id='power_off' value='<%=l.getString("mgr","stop")%>'>
<input type='hidden' id='s_started' value='<%=l.getString("mgr","active")%>'>
<input type='hidden' id='s_stopped' value='<%=l.getString("mgr","disactive")%>'>
<input type='hidden' id='s_canstart' value='<%=l.getString("switch","canstart")%>'>
<input type='hidden' id='s_alerthour' value='<%=l.getString("switch","alerthour")%>'>
<input type='hidden' id='s_alertstarthour' value='<%=l.getString("switch","alertstarthour")%>'>
<input type='hidden' id='selected_switch_id' value='<%=selected_switch_id%>'>

<p class='standardTxt'><%=l.getString("switch","pagecomment2")%></p>

<FORM id="frm_config" name="frm_config" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type='hidden' name='isauto' id='isauto' >
<input type='hidden' name='cmd' id='cmd' >
<FIELDSET style='width:97%'><LEGEND class='standardTxt'><%=l.getString("switch","switch")%></LEGEND>
<table class='standardTxt' cellpadding="0" cellspacing="0" width="100%" >
	<TR>
		<TD>
			<table class='standardTxt' cellpadding="0" cellspacing="0" border='0'>
				<tr height="35px">
					<TD><%=l.getString("switch","selectswitch")%>&nbsp;</TD>
					<TD><SELECT class='standardTxt' id='switch_id' name='switch_id' onchange='onChangeSwitch();'>
							<%=comboswitch.toString()%>
						</SELECT>
					</TD>
					<TD  width="25%" align="center"><b><p id='status'></p></b></TD>
					<TD width="35%" align='center'>
						<div id='power_button'>
							<img title="<%=l.getString("mgr","start")%>" name='startbtn' style="cursor:pointer" src='images/actions/start_on.png'/>
							&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
							<img title="<%=l.getString("mgr","stop")%>" name='stopbtn' style="cursor:pointer" src='images/actions/stop_on.png'/>
						</div>
					</TD>
				</tr>
			</table>
		</TD>
	</TR>
</table>
</FIELDSET>
<table class='standardTxt' cellpadding="0" cellspacing="0" width="100%" >
	<TR height="10px"><td>&nbsp;</td></TR>
	<TR>
		<TD>
			<FIELDSET style='width:97%'>
			<LEGEND><%=l.getString("switch","autoswitch")%></LEGEND>
			<TABLE class='standardTxt' cellpadding="0" cellspacing="0"  border='0' width="100%">
				<TR height="40px">
					<TD width="25%">
						<INPUT type='radio' name='autoswitch' id='auto' onclick="switch_change_mode('auto');"/>
						<%=l.getString("switch","autoswitch")%>
					</TD>
					
					<TD width="25%"><%=l.getString("switch","hour")%>&nbsp;
						<input id='obj_hour' name='obj_hour' type='text' class='standardTxt' style='width:60px;' maxlength="4">
					</TD>
					
					<TD width="25%"><%=l.getString("switch","starthour")%>&nbsp;
						<select class='standardTxt' id='obj_starthour' name='obj_starthour'>
							<%=combohour.toString()%>
						</select>
					</TD>
				    <TD width="25%" ><%=l.getString("switch","starttype")%>&nbsp;
						<select class='standardTxt' id='obj_starttype' name='obj_starttype'>
							<option value="eev"><%=l.getString("switch","eev")%></option>
							<option value='tev'><%=l.getString("switch","tev")%></option>
						</select>
					</td>
				</TR>
			</TABLE>
			</FIELDSET>
		</TD>
	</TR>
	<TR height="10px"><td>&nbsp;</td></TR>
	<TR>
		<TD>
			<FIELDSET style='width:97%'>
			<LEGEND><%=l.getString("switch","manualswitch")%></LEGEND>
			<TABLE class='standardTxt' cellpadding="0" cellspacing="0" border='0' width="100%">
				<TR height="40px">
					<TD width="25%">
						<INPUT type='radio' name='autoswitch' id='manual' onclick="switch_change_mode('manual');"/>
					    &nbsp;<%=l.getString("switch","manualswitch")%>
				    </TD>
				    <TD  width="25%">
						<%=l.getString("switch","tech")%>: &nbsp;
						<select class='standardTxt' id='obj_manualtype' name='obj_manualtype'>
							<option value="eev"><%=l.getString("switch","eev")%></option>
							<option value='tev'><%=l.getString("switch","tev")%></option>
						</select>
					</td>
					<TD  width="50%">&nbsp;</TD>
				</TR>
			</TABLE>
			</FIELDSET>
		</TD>
	</TR>
	<TR height="10px"><td>&nbsp;</td></TR>
	<TR>
		<TD>
			<FIELDSET style='width:97%'><legend class='standardTxt'><%=l.getString("switch","anomaliesmanage")%></legend>
			
			<TABLE class='standardTxt' cellpadding="0" cellspacing="0" border='0' width="100%">
				<TR height="40px">
					<TD width="25%">
						<INPUT type='checkbox' name='autoresume' id='autoresume' onclick='onClickAutoresume();'/>
					    &nbsp;<%=l.getString("switch","autoresume")%>
				    </TD>
				    <TD width="25%" class='standardTxt'>
						<input id='obj_skipalarm' name='obj_skipalarm' type='checkbox' class='standardTxt' onclick="onClickSkipAlarm();"/>
						<%=l.getString("switch","skipalarm")%>&nbsp;
					</TD>
				    <TD width="50%" >
				    	<%=l.getString("switch","alarmwait")%>&nbsp;
					    <input id='obj_alarm_wait' name='obj_alarm_wait' type='text' class='standardTxt' style='width:60px;' maxlength="4" /> <%=l.getString("switch","minuts")%>
					</TD> 
					
				</TR>
			</TABLE>
			</FIELDSET>
		</TD>
	</TR>
</table>
