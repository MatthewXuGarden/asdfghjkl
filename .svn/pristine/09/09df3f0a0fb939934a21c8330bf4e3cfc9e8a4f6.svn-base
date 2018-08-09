<%@ page language="java" 
import="java.sql.Timestamp"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.field.FieldConnectorMgr"
import="com.carel.supervisor.dataaccess.datalog.impl.AlarmLog"
import="com.carel.supervisor.dataaccess.datalog.impl.AlarmLogList"
import="com.carel.supervisor.controller.rule.RuleMgr"
import="com.carel.supervisor.presentation.alarms.AlarmList"
import="com.carel.supervisor.base.conversion.DateUtils"
import="com.carel.supervisor.base.conversion.StringUtility"
import="com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr"
%>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String idal = "";
	int idsite = sessionUser.getIdSite();
	String jsession = request.getSession().getId();
	String id = sessionUser.getProperty("id");
	String user = sessionUser.getUserName();
	String cmd = sessionUser.getPropertyAndRemove("cmd");
	
	String[] datas = null;
	if(id != null)
		datas = StringUtility.split(id,"_");
	
	if(datas != null)
	{
		idal = datas[0];
		try {idsite = Integer.parseInt(datas[1]);}catch(Exception e){}
	}

	//multilingua
	String s_id = lan.getString("alrview","id");
	String s_info = lan.getString("alrview", "info");
	String s_acktime = lan.getString("alrview", "acktime");
	String s_cancelledtime = lan.getString("alrview", "cancelledtime");
	String s_resettime = lan.getString("alrview", "resettime");
	String s_activefrom = lan.getString("alrview", "activefrom");
	String s_calledof = lan.getString("alrview", "calledof");
	String s_priority = lan.getString("alrview", "priority");
	String s_useraction = lan.getString("alrview", "user");
	String s_data = lan.getString("alrview", "data");
	
	String ackconfirm = lan.getString("alrview","ackconfirm");
	String deleteconfirm = lan.getString("alrview","deleteconfirm");
	String resetconfirm = lan.getString("alrview","resetconfirm");
		

	AlarmList alarmList = new AlarmList();
	AlarmLog alarmLog = null;

	AlarmLogList alarmLogList = alarmList.loadFromDataBaseByIdAlarm(sessionUser,Integer.parseInt(idal),idsite);
	alarmLog = alarmLogList.getByPosition(0);
	
	String priority = null;
	switch (Integer.parseInt(alarmLog.getPriority()))
	{
		case (1): priority = lan.getString("alrview","alarmstate1");break;
		case (2): priority = lan.getString("alrview","alarmstate2");break;
		case (3): priority = lan.getString("alrview","alarmstate3");break;
		case (4): priority = lan.getString("alrview","alarmstate4");break;
	}

	Timestamp startTime = alarmLog.getStarttime();
	Timestamp endTime = alarmLog.getEndtime();
	Timestamp ackTime = alarmLog.getAcktime();
	
	//Se Allarme ackabile e non è ackato => abilito pulsante e disabilito gli altri
	//Se allarme non è rientrato e non è stato cancellato => abilito solo il cancel
	//Se allarme non è rientrato e non è stato resettato => abilito solo il reset
	
	boolean isackable = SystemConfMgr.getInstance().get("priority_"+alarmLog.getPriority()).getValue().equals("TRUE");
	String state = "blank";	
	if (isackable && alarmLog.getAcktime()==null)
	{
		state="ack";
	}
	else
	{
		if (endTime == null && alarmLog.getDelactiontime()==null)
		{
			state="cancel";
		}
		else if (endTime == null && alarmLog.getResettime()==null)
		{
			state="reset";
		}
	}
	
	String notaIcon = ""; 
    try
    {
		NoteLogList listaNote = new NoteLogList();
		listaNote.retrieve(alarmLog.getSite(), "hsalarm", alarmLog.getId());
		
		if (listaNote.size() > 0)
		{
			String nota = Replacer.replace(listaNote.getNote(listaNote.size() - 1).getNote(), "%!", " ");
			notaIcon = "<img src='images/event/mininote.gif' title='" + nota + "' /> ";
		}
	}
    catch (Exception e)
    {
    	Logger logger = LoggerMgr.getLogger(this.getClass());
        logger.error(e);
	}
    
    String mandatoryError = sessionUser.getPropertyAndRemove("mandatoryNoteRequired");
    boolean noteRequired = false;
    if (mandatoryError!=null)
    	if (mandatoryError.equalsIgnoreCase("TRUE"))
    		noteRequired=true;
    String noteRequiredMessage = lan.getString("alrmng","noteRequiredMessage");
%>

<%@page import="com.carel.supervisor.dataaccess.datalog.impl.NoteLogList"%>
<%@page import="com.carel.supervisor.base.conversion.Replacer"%>
<%@page import="com.carel.supervisor.presentation.alarms.Alarm"%>
<%@page import="com.carel.supervisor.base.log.Logger"%>
<%@page import="com.carel.supervisor.base.log.LoggerMgr"%><INPUT type="hidden" id="ackconfirm" name="ackconfirm" value="<%=ackconfirm%>"/>
<INPUT type="hidden" id="deleteconfirm" name="deleteconfirm" value="<%=deleteconfirm%>"/>
<INPUT type="hidden" id="resetconfirm" name="resetconfirm" value="<%=resetconfirm%>"/>
<INPUT type="hidden" id="noteRequired" name="noteRequired" value="<%=noteRequiredMessage%>"/>
<INPUT type="hidden" id="state" name="state" value="<%=state%>"/>
<INPUT type="hidden" id="returned" name="returned" value="<%=endTime == null?"":endTime%>"/>
<FORM name="frm_alrview" id="frm_alrview" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" id="id" name="id" value="<%=id%>"/>
<INPUT type="hidden" id="cmd" name="cmd" value=""/> 
</FORM>
<p class="tdTitleTable"><%=notaIcon%> <%=alarmList.getAlarm(0).getAlarmDevice() +":  "+ alarmList.getAlarm(0).getAlarmVariable()%></p>
<FIELDSET>
<LEGEND class="StandardTxt"><%=s_info%></LEGEND>
<TABLE>
<TR height="10"><TD></TD></TR>
	<TR class="StandardTxt">
		<TD><%=s_id%>:</TD>
		<TD><%=idal%></TD>
		<TD width="170">&nbsp;</TD>
		<TD><%=s_priority%>:</TD>
		<TD class="statoAllarme<%=alarmLog.getPriority()%>" align="center"><b><%=priority%></b></TD>
	</TR>
	<TR height="15"><TD></TD></TR>
	<TR class="StandardTxt">
		<TD><%=s_activefrom%>:</TD>
		<TD class="td" align="right"> <%=DateUtils.date2String(startTime, "yyyy/MM/dd HH:mm:ss")%></TD>
		<TD width="60">&nbsp;</TD>
		<TD><%=s_calledof%>:</TD>
		<TD class="td" align="right"> <%=(null==endTime)?"----------":DateUtils.date2String(endTime, "yyyy/MM/dd HH:mm:ss")%></TD>
	</TR>
	<TR height="10"><TD></TD></TR>
</TABLE>
</FIELDSET>
<BR>
<FIELDSET>
<LEGEND class="StandardTxt"><%=s_acktime%></LEGEND>
<TABLE>
	<TR height="10"><TD></TD></TR>
	<TR class="StandardTxt">
		<TD><%=s_useraction%>:</TD>
		<TD class="td" align="right"><%=(null==alarmLog.getAckuser())?"----------":alarmLog.getAckuser()%></TD>
		<TD width="190">&nbsp;</TD>
		<TD><%=s_data%>:</TD>
		<TD class="td" align="right"><%=(null==ackTime)?"----------":DateUtils.date2String(ackTime, "yyyy/MM/dd HH:mm:ss")%></TD>
	</TR>
	<TR height="10"><TD></TD></TR>
</TABLE>
</FIELDSET>
<BR>
<FIELDSET>
<LEGEND class="StandardTxt"><%=s_cancelledtime%></LEGEND>
<TABLE>
	<TR height="10"><TD></TD></TR>
	<TR class="StandardTxt">
		<TD><%=s_useraction%>:</TD>
		<TD class="td" align="right"><%=(null==alarmLog.getDelactionuser())?"----------":alarmLog.getDelactionuser()%></TD>
		<TD width="190">&nbsp;</TD>
		<TD><%=s_data%>:</TD>
		<TD class="td" align="right"><%=(null==alarmLog.getDelactiontime())?"----------":DateUtils.date2String(alarmLog.getDelactiontime(), "yyyy/MM/dd HH:mm:ss")%></TD>
	</TR>
	<TR height="10"><TD></TD></TR>
</TABLE>
</FIELDSET>
<BR>
<FIELDSET>
<LEGEND class="StandardTxt"><%=s_resettime%></LEGEND>
<TABLE>
	<TR height="10"><TD></TD></TR>
	<TR class="StandardTxt">
		<TD><%=s_useraction%>:</TD>
		<TD class="td" align="right"><%=(null==alarmLog.getResetuser())?"----------":alarmLog.getResetuser()%></TD>
		<TD width="190">&nbsp;</TD>
		<TD><%=s_data%>:</TD>
		<TD class="td" align="right"><%=(null==alarmLog.getResettime())?"----------":DateUtils.date2String(alarmLog.getResettime(), "yyyy/MM/dd HH:mm:ss")%></TD>
	</TR>
	<TR height="10"><TD></TD></TR>
</TABLE>
</FIELDSET>
				
<% if (noteRequired) { %>	
	<SCRIPT type="text/javascript">
		alert(document.getElementById("noteRequired").value);
	</SCRIPT>
<%} %>
	

