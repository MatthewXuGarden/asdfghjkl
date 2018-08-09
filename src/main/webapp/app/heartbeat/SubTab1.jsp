<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"

%>
<%@ page import="com.carel.supervisor.presentation.bo.BTimeTable" %>
<%

	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();	
	String jsession = request.getSession().getId();
	
	
	//Label Multilingua
	LangService lan = LangMgr.getInstance().getLangService(language);
	String nomefascia = lan.getString("timetable","fascia");
	String desc = lan.getString("timetable","name");
	String from=lan.getString("timebandsPage","from");
	String to=lan.getString("timebandsPage","to");
	String dateMsg=lan.getString("timebandsMsg","datemsg");
  String NomeFasciaMsg = lan.getString("timetable","fasciamsg");
	String confirmfasciadel = lan.getString("timetable","confirmfasciadel");
	String intervallo = lan.getString("timetable","intervallo");
	String emptyvalue = lan.getString("timetable","emptyvalue");
	String isremovable = lan.getString("timetable","isremovable");
  String duplicatevalue = lan.getString("timetable","duplicatevalue");
	String description = lan.getString("timetable","description");
	String minuts = lan.getString("timetable","minuts");
	String slot = lan.getString("timetable","slot");
  String slotmissing = lan.getString("timetable","slotmissing");
	String confirmfasciaupdate = lan.getString("timetable","confirmfasciaupdate");
  String slotNonCons = lan.getString("timetable","notslotconsec");

	BTimeTable.setScreenW(sessionUser.getScreenWidth());
	BTimeTable.setScreenH(sessionUser.getScreenHeight());
	String btime = ((BTimeTable)sessionUser.getCurrentUserTransaction().getBoTrx()).getHTMLTable("tab1name", language, "rmtimetable", 150, 906);
	
	StringBuffer bodySelectMinutesFrom= new StringBuffer();
	bodySelectMinutesFrom.append("\n<option value=\"-1"+"\">--");
	for(int i=0;i<60;i++){
		if(i<10)
			bodySelectMinutesFrom.append("\n<option value=\"0"+i+"\">0"+i);
		else
			bodySelectMinutesFrom.append("\n<option value=\""+i+"\">"+i);
	}//for
	
	StringBuffer bodySelectHoursFrom= new StringBuffer();
	bodySelectHoursFrom.append("\n<option value=\"-1"+"\">--");
	for(int i=0;i<24;i++){
		if(i<10)
			bodySelectHoursFrom.append("\n<option value=\"0"+i+"\">0"+i);
		else
			bodySelectHoursFrom.append("\n<option value=\""+i+"\">"+i);
	}//for
	
	
	StringBuffer bodySelectRange= new StringBuffer();
	//bodySelectRange.append("\n<option value=\"00\">00");
	for(int i=1;i <= 4;i++){
		bodySelectRange.append("\n<option value=\""+i*15+"\">"+i*15);		
	}
	
	StringBuffer bodySelectEmpty = new StringBuffer();
		bodySelectEmpty.append("\n<option value=\"-1"+"\">&nbsp;--&nbsp;");

%>

<table border="0" width="100%" height="97%" cellspacing="1" cellpadding="1">
<TR>
	<td><p class="StandardTxt"><%=description%></p></td>
</TR>
<TR>
	<TD>&nbsp;</TD>
</TR>
<tr height="100%" valign="top" id="trList">
	<td><%=btime%></td>
</tr>
<TR>
	<TD>&nbsp;</TD>
</TR>
<tr>
<td>
<FORM id="frm_fascia_info" name="frm_fascia_info" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" id="cmd" name="cmd"/>
<INPUT type="hidden" id="maxRigheTime" name="maxRigheTime" value="4"/>
<INPUT type="hidden" id="removeFascia" name="removeFascia" value=""/>
<INPUT type="hidden" id="confirmfasciadel" value="<%=confirmfasciadel%>"/>
<INPUT type="hidden" id="emptyvalue" value="<%=emptyvalue%>"/>
<INPUT type="hidden" id="isremovable" value="<%=isremovable%>"/>
<INPUT type="hidden" id="duplicatevalue" value="<%=duplicatevalue%>"/> 
<INPUT type="hidden" id="missingSlot" value="<%=slotmissing%>"/>
<INPUT type="hidden" id="slotName" value="<%=slot%>"/>
<INPUT type="hidden" id="confirmfasciaupdate" value="<%=confirmfasciaupdate%>"/>
<INPUT type="hidden" id="slotNonCons" value="<%=slotNonCons%>"/>

<FIELDSET class="field">
<LEGEND class="standardTxt"><%=nomefascia%></LEGEND>
<TABLE class='standardTxt' width="100%" height="100%" border="0">
	<TR>
		<TD valign="top" class="standardTxt" width="40%"><%=desc%>&nbsp;&nbsp;&nbsp;
					<input class="standardTxt" type="text"  name="desc" id="desc" maxlength="35" size="29" onblur="noBadCharOnBlur(this,event);" onkeydown='checkBadChar(this,event);'/>&nbsp;*
		</TD>
		<td valign="top" class="standardTxt"><%=intervallo%>&nbsp;&nbsp;
				<select id="range"  name="range" onchange=changeRange()><%=bodySelectRange.toString()%></select>&nbsp;&nbsp;<%=minuts%>
		</td>
	</tr>	
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
</TABLE>
<TABLE class='standardTxt' width="100%" height="100%" border="0">
	<tr>
		 <td class="standardTxt" align="left" width="30%">
			<div align="left"><%=slot%>&nbsp;&nbsp;1*&nbsp;&nbsp;&nbsp; <%=from%>
				<select id="hourfrom_1"  name="hourfrom_1" onchange=setRange(this,'ore')><%=bodySelectHoursFrom.toString()%>
				</select>&nbsp;:&nbsp;
				<select id="minutfrom_1" name="minutfrom_1" onchange=setRange(this,'minuti')><%=bodySelectMinutesFrom.toString()%>
				</select>
			</div>	
		</td>
		<td class="standardTxt" align="left" width="20%"><%=to%>&nbsp;&nbsp;&nbsp;
			<input type="text" readonly class="standardTxt" name="hour_1" id="hour_1" maxlength="2" size="3" />
			&nbsp;:&nbsp;
			<input type="text" readonly class="standardTxt" name="minutes_1" id="minutes_1" maxlength="2" size="3" />
		</td>
		 <td class="standardTxt" align="right" width="30%">
		 	<div align="right"><%=slot%>&nbsp;&nbsp;2&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%=from%>
				<select id="hourfrom_2"  name="hourfrom_2" onchange=setRange(this,'ore')><%=bodySelectHoursFrom.toString()%>
				</select>&nbsp;:&nbsp;
				<select id="minutfrom_2" name="minutfrom_2" onchange=setRange(this,'minuti') ><%=bodySelectMinutesFrom.toString()%>
				</select>
			</div>
		</td>
		<td class="standardTxt" align="center" width="20%"><%=to%>&nbsp;&nbsp;&nbsp;
			<input type="text" readonly class="standardTxt" name="hour_2" id="hour_2" maxlength="2" size="3" />
			&nbsp;:&nbsp;
			<input type="text" readonly class="standardTxt" name="minutes_2" id="minutes_2" maxlength="2" size="3" />
		</td>
	</tr>
	<tr>
		 <td class="standardTxt" align="left" width="30%">
		 	<div align="left"><%=slot%>&nbsp;&nbsp;3&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%=from%>
				<select id="hourfrom_3"  name="hourfrom_3" onchange=setRange(this,'ore')><%=bodySelectHoursFrom.toString()%>
				</select>&nbsp;:&nbsp;
				<select id="minutfrom_3" name="minutfrom_3" onchange=setRange(this,'minuti')><%=bodySelectMinutesFrom.toString()%>
				</select>
			</div>
		</td>
		<td class="standardTxt" align="left" width="20%"><%=to%>&nbsp;&nbsp;&nbsp;
			<input type="text" readonly class="standardTxt" name="hour_3" id="hour_3" maxlength="2" size="3" />
			&nbsp;:&nbsp;
			<input type="text" readonly class="standardTxt" name="minutes_3" id="minutes_3" maxlength="2" size="3" />
		</td>
		 <td class="standardTxt" align="right" width="30%">
			<div align="right"><%=slot%>&nbsp;&nbsp;4&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%=from%>
				<select id="hourfrom_4"  name="hourfrom_4" onchange=setRange(this,'ore')><%=bodySelectHoursFrom.toString()%>
				</select>&nbsp;:&nbsp;
				<select id="minutfrom_4" name="minutfrom_4" onchange=setRange(this,'minuti')><%=bodySelectMinutesFrom.toString()%>
				</select>
			</div>
		</td>
		<td class="standardTxt" align="center" width="20%"><%=to%>&nbsp;&nbsp;&nbsp;
	    	<input type="text" readonly class="standardTxt" name="hour_4" id="hour_4" maxlength="2" size="3" />
	    	&nbsp;:&nbsp;
	    	<input type="text" readonly class="standardTxt" name="minutes_4" id="minutes_4" maxlength="2" size="3" />
		</td>
	</TR>
</TABLE>
</FIELDSET>
</form>
</td>
</tr>
</table>
<div style="display:none;">
	<div id="dateMsg"><%=dateMsg%></div>
	<div id="NomeFasciaMsg"><%=NomeFasciaMsg%></div>
</div>
