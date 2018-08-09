<%@ page language="java"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.plugin.algorithmpro.AlgorithmProMgr"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.plugin.base.Plugin"
%>

<%
UserSession us = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = us.getCurrentUserTransaction();
String jsession = request.getSession().getId();

LangService lan = LangMgr.getInstance().getLangService(us.getLanguage());
String sStatus = lan.getString("algopro","sStatus");
String sStart = lan.getString("algopro","sStart");
String sStop = lan.getString("algopro","sStop");
String sNoObjs = lan.getString("algopro","sNoObjs");
String sConfAct = lan.getString("algopro","sConfAct");

String[] names = AlgorithmProMgr.getInstance().retrieveAlgoListName();
boolean obj1Present = false;
boolean obj2Present = false;
boolean obj3Present = false;

String name1 = "";
String name2 = "";
String name3 = "";

String status1 = "";
String start1 = "";
String stop1 = "";

String status2 = "";
String start2 = "";
String stop2 = "";

String status3 = "";
String start3 = "";
String stop3 = "";

if(names.length >= 1)
{
	obj1Present = true;
	name1 = names[0];
	status1 = AlgorithmProMgr.getInstance().getAlgoStatus(name1);
	
	if(status1 != null)
	{
		if(status1.equals("1"))
		{
			status1 = "<b>RUNNING</b>";
			start1 = "<img src='images/actions/stop_on_black.png' onclick='algoAction(1);' border='1' style='cursor:pointer'/>";
			stop1 = "<img src='images/actions/start_off.png' border='1'/>";
		}
		else
		{
			if(status1.equals("0"))
			{
				status1 = "<b>STOP</b>";
				start1 = "<img src='images/actions/stop_off.png' border='1' />";
				stop1 = "<img src='images/actions/start_on_black.png' onclick='algoAction(1);' border='1' style='cursor:pointer'/>";
			}
			else
			{
				status1 = "<b>PENDING</b>";
				start1 = "<img src='images/actions/stop_off.png' border='1'/>";
				stop1 = "<img src='images/actions/start_off.png' border='1'/>";
			}
					
		}
	}
}
if(names.length >= 2)
{
	obj2Present = true;
	name2 = names[1];
	status2 = AlgorithmProMgr.getInstance().getAlgoStatus(name2);
	if(status2 != null)
	{
		if(status2.equals("1"))
		{
			status2 = "<b>RUNNING</b>";
			start2 = "<img src='images/actions/stop_on_black.png' onclick='algoAction(2);' style='cursor:pointer' border='1'/>";
			stop2 = "<img src='images/actions/start_off.png' border='1'/>";
		}
		else
		{
			if(status2.equals("0"))
			{
				status2 = "<b>STOP</b>";
				start2 = "<img src='images/actions/stop_off.png' border='1'/>";
				stop2 = "<img src='images/actions/start_on_black.png' onclick='algoAction(2);' style='cursor:pointer' border='1'/>";
			}
			else
			{
				status2 = "<b>PENDING</b>";
				start2 = "<img src='images/actions/stop_off.png' border='1'/>";
				stop2 = "<img src='images/actions/start_off.png' border='1'/>";
			}
					
		}
	}
}
if(names.length == 3)
{
	obj3Present = true;
	name3 = names[2];
	status3 = AlgorithmProMgr.getInstance().getAlgoStatus(name3);
	if(status3 != null)
	{
		if(status3.equals("1"))
		{
			status3 = "<b>RUNNING</b>";
			start3 = "<img src='images/actions/stop_on_black.png' onclick='algoAction(3);' border='1' style='cursor:pointer'/>";
			stop3 = "<img src='images/actions/start_off.png' border='1'/>";
		}
		else
		{
			if(status3.equals("0"))
			{
				status3 = "<b>STOP</b>";
				start3 = "<img src='images/actions/stop_off.png' border='1'/>";
				stop3 = "<img src='images/actions/start_on_black.png' onclick='algoAction(3);' border='1' style='cursor:pointer'/>";
			}
			else
			{
				status3 = "<b>PENDING</b>";
				start3 = "<img src='images/actions/stop_off.png' border='1'/>";
				stop3 = "<img src='images/actions/start_off.png' border='1'/>";
			}
					
		}
	}
}

%>

<FORM name="frm_algopro" id="frm_algopro" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="algocmd" name="algocmd" value="algoaction" />
<input type="hidden" id="algoobj" name="algoobj" value="" />

<input type="hidden" id="sConfAct" name="sConfAct" value="<%=sConfAct%>" />
<input type="hidden" id="algoobj1" name="algoobj1" value="<%=name1%>" />
<input type="hidden" id="algoobj2" name="algoobj2" value="<%=name2%>" />
<input type="hidden" id="algoobj3" name="algoobj3" value="<%=name3%>" />

<table border="0" width="100%" cellpadding="2" cellspacing="2">
	<tr>
		<!-- Object 01 -->
		<td width="33%">
			<% if(obj1Present) { %>
			<FIELDSET class='field' style="height:50%">		
			<LEGEND class='standardTxt'><b><%=names[0]%></b></LEGEND>
				<table cellpadding="1" cellspacing="2" width="100%">
					<tr>
						<td  class='standardTxt'><b><%=AlgorithmProMgr.getInstance().getAlgoDescription(names[0]) %></b></td>
					</tr>
					<tr><td><br/></td>
					</tr>
					<tr>
						<td class='standardTxt'><%=sStart %>: <%=AlgorithmProMgr.getInstance().getAlgoStartTime(names[0]) %></td>
					</tr>
					<tr>
						<td class='standardTxt'><%=sStop %>: <%=AlgorithmProMgr.getInstance().getAlgoStopTime(names[0]) %></td>
					</tr>
					<tr>
						<td>
							<table cellpadding="1" cellspacing="2" width="50%">
								<tr>
									<td class='standardTxt'><%=status1 %></td>
									<td class='standardTxt'><%=stop1 %></td>
									<td class='standardTxt'><%=start1 %></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</FIELDSET>
			
			<table cellpadding="1" cellspacing="2" width="100%">
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 1</b></LEGEND>
							<span id="stateInfo1" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo1(names[0]) %></span>
						</FIELDSET>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 2</b></LEGEND>
							<span id="stateInfo2" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo2(names[0]) %></span>
						</FIELDSET>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 3</b></LEGEND>
							<span id="stateInfo3" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo3(names[0]) %></span>
						</FIELDSET>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 4</b></LEGEND>
							<span id="stateInfo4" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo4(names[0]) %></span>
						</FIELDSET>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 5</b></LEGEND>
							<span id="stateInfo5" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo5(names[0]) %></span>
						</FIELDSET>
					</td>
				</tr>
			</table>
			<%} %>
		</td>
		
		<td width="33%">
			<% if(obj2Present) { %>
			<FIELDSET class='field' style="height:50%">		
				<LEGEND class='standardTxt'><b><%=names[1]%></b></LEGEND>
				<table cellpadding="1" cellspacing="2" width="100%">
					<tr>
						<td class='standardTxt'><b><%=AlgorithmProMgr.getInstance().getAlgoDescription(names[1]) %></b></td>
					</tr>
					<tr><td><br/></td>
					<tr>
						<td class='standardTxt'><%=sStart %>: <%=AlgorithmProMgr.getInstance().getAlgoStartTime(names[1]) %></td>
					</tr>
					<tr>
						<td class='standardTxt'><%=sStop %>: <%=AlgorithmProMgr.getInstance().getAlgoStopTime(names[1]) %></td>
					</tr>
					<tr>
						<td>
							<table cellpadding="1" cellspacing="2" width="50%">
								<tr>
									<td class='standardTxt'><%=status2 %></td>
									<td class='standardTxt'><%=stop2 %></td>
									<td class='standardTxt'><%=start2 %></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</FIELDSET>
			
			<table cellpadding="1" cellspacing="2" width="100%">
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 1</b></LEGEND>
							<span id="stateInfo1" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo1(names[1]) %></span>
						</FIELDSET>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 2</b></LEGEND>
							<span id="stateInfo2" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo2(names[1]) %></span>
						</FIELDSET>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 3</b></LEGEND>
							<span id="stateInfo3" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo3(names[1]) %></span>
						</FIELDSET>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 4</b></LEGEND>
							<span id="stateInfo4" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo4(names[1]) %></span>
						</FIELDSET>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 5</b></LEGEND>
							<span id="stateInfo5" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo5(names[1]) %></span>
						</FIELDSET>
					</td>
				</tr>
			</table>
			<%} %>
		</td>
		
		<td width="33%">
			<% if(obj3Present) { %>
			<FIELDSET class='field' style="height:50%">		
			<LEGEND class='standardTxt'><b><%=names[2]%></b></LEGEND>
				<table cellpadding="1" cellspacing="2" width="100%">
					<tr>
						<td class='standardTxt'><b><%=AlgorithmProMgr.getInstance().getAlgoDescription(names[2]) %></b></td>
					</tr>
					<tr><td><br/></td>
					<tr>
						<td class='standardTxt'><%=sStart %>: <%=AlgorithmProMgr.getInstance().getAlgoStartTime(names[2]) %></td>
					</tr>
					<tr>
						<td class='standardTxt'><%=sStop %>: <%=AlgorithmProMgr.getInstance().getAlgoStopTime(names[2]) %> </td>
					</tr>
					<tr>
						<td>
							<table cellpadding="1" cellspacing="2" width="50%">
								<tr>
									<td class='standardTxt'><%=status3 %></td>
									<td class='standardTxt'><%=stop3 %></td>
									<td class='standardTxt'><%=start3 %></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</FIELDSET>
			
			<table cellpadding="1" cellspacing="2" width="100%">
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 1</b></LEGEND>
							<span id="stateInfo1" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo1(names[2]) %></span>
						</FIELDSET>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 2</b></LEGEND>
							<span id="stateInfo2" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo2(names[2]) %></span>
						</FIELDSET>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 3</b></LEGEND>
							<span id="stateInfo3" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo3(names[2]) %></span>
						</FIELDSET>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 4</b></LEGEND>
							<span id="stateInfo4" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo4(names[2]) %></span>
						</FIELDSET>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<FIELDSET class='field' style="height:50%">		
							<LEGEND class='standardTxt'><b><%=sStatus %> 5</b></LEGEND>
							<span id="stateInfo5" class="standardTxt"><%=AlgorithmProMgr.getInstance().getAlgoStateInfo5(names[2]) %></span>
						</FIELDSET>
					</td>
				</tr>
			</table>
			<%} %>
		</td>
	</tr>
</table>
<%if(names.length == 0) {%>
<table border="0" width="100%" cellpadding="2" cellspacing="2">
	<tr>
		<td align="center" class="standardTxt"><%= sNoObjs%></td>
	</tr>
</table>
<%} %>
</FORM>
