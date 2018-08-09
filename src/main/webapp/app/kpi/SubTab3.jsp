<%@ page language="java"%>
<%@ page import="com.carel.supervisor.presentation.session.UserSession"%>
<%@ page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@ page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%@ page import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%@ page import="com.carel.supervisor.dataaccess.db.RecordSet" %>
<%@ page import="com.carel.supervisor.presentation.kpi.KpiMgr" %>
<%@ page import="com.carel.supervisor.dataaccess.db.Record" %>
<%@ page import="com.carel.supervisor.presentation.helper.VirtualKeyboard" %>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	LangService multiLanguage = LangMgr.getInstance().getLangService(sessionUser.getLanguage());
	String jsession = request.getSession().getId();
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>
<INPUT type="hidden" id="errmessage" name="errmessage" value="<%=multiLanguage.getString("kpi","errmessage")%>" />
<form id="kpi_page3_form" name="kpi_page3_form" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" id="kpicmd" name="kpicmd" value="kpiconf2" />
<INPUT type="hidden" id="percentageerror" name="percentageerror" value="<%=multiLanguage.getString("kpi","percentageerror")%>" />
<INPUT type="hidden" id="minmaxerror" name="minmaxerror" value="<%=multiLanguage.getString("kpi","minmaxerror") %>" />
<INPUT type="hidden" id="errmessage" name="errorerror" value="<%=sessionUser.getPropertyAndRemove("error")%>" />
<p class='standardTxt'><%=multiLanguage.getString("kpi","comment3")%></p>
<table border="0" width="99%" height="95%" cellspacing="1" cellpadding="1">
<%
RecordSet rs = KpiMgr.getGroups();
if(rs!=null && rs.size()>0)
{
for(int i=0;i<KpiMgr.grpNum();i++)
{
	int noconf=0;
	Record grouprecord=rs.get(i);
	String idgrp = rs.get(i).get("idgrp").toString();
%>

	<TR>
		<TD valign="top">
		        <TABLE width="100%" cellpadding="2" cellspacing="2">
			      <TR>
			        <TD class="standardTxt" style="font-size: 12px;">
			        <b><%=multiLanguage.getString("kpi", "kpigrplegend")%>: <%=rs.get(i).get("name").toString()%></b>
			        <INPUT type="hidden" id="grp<%=i%>" name="grp<%=i%>" value="<%=idgrp%>" />
			        </TD>
			      </TR> 
			    </TABLE>
				<TABLE width="100%" cellpadding="2" cellspacing="2">
					<TR>
						<TD>
							<TABLE class="table" width="100%" cellpadding="2" cellspacing="2"> <!-- style="border: 1px solid #0000aa;"  -->
							<THEAD>
								<TR style="border: 1px solid #0000aa" >
									<TH class='th' width="*">
										<%=multiLanguage.getString("kpi", "kpidevdesc")%>
									</TH>
									<TH class='th' width="25%">
										<%=multiLanguage.getString("kpi", "kpimastervar")%>
									</TH>
									<TH class='th' width="25%">
										<%=multiLanguage.getString("kpi", "kpidefrostvar")%>
									</TH>
									<TH class='th' width="25%">
										<%=multiLanguage.getString("kpi", "kpisolenoidvar")%>
									</TH>
								</TR>
							</THEAD>
							<TBODY>
<%
RecordSet rrss = KpiMgr.getDevicesModel(idgrp, sessionUser.getLanguage());
if(rrss!=null && rrss.size()!=0)
{
	for(int krs=0;krs<rrss.size();krs++)
	{
		Record r = rrss.get(krs);
		String iddm = r.get("iddevmdl").toString();
%>
							<TR>
							<TD class="standardTxt">
								<%=r.get("description")%>
							</TD> 
							<TD class="standardTxt">
							<SELECT id='mv_<%=idgrp%>_<%=iddm%>' name='mv_<%=idgrp%>_<%=iddm%>' style="width:100%" class="standardTxt">
							<OPTION value="---">---</OPTION>
<%
RecordSet rsa = KpiMgr.getMastervar(iddm, sessionUser.getLanguage());
if(rsa!=null)
for(int rsai=0;rsai<rsa.size();rsai++)
{
	out.print("<option value='"+rsa.get(rsai).get("idvarmdl")+"' "+
	(rsa.get(rsai).get("idvarmdl").toString().equalsIgnoreCase(r.get("mastervarmdl").toString())?"selected='selected' ":"")+
	">"+rsa.get(rsai).get("description")+"</option>");
}
%>
							</SELECT>
							</TD> 
							<TD class="standardTxt">
							<SELECT id='dv_<%=idgrp%>_<%=iddm%>' name='dv_<%=idgrp%>_<%=iddm%>' style="width:100%" class="standardTxt">
							<OPTION value="---">---</OPTION>
<%
RecordSet rsb = KpiMgr.getDefrost(iddm, sessionUser.getLanguage());//KpiMgr.getDefrost(iddm, sessionUser.getLanguage());
if(rsb!=null)
for(int rsbi=0;rsbi<rsb.size();rsbi++)
{
	out.print("<option value='"+rsb.get(rsbi).get("idvarmdl")+"' "+
	(rsb.get(rsbi).get("idvarmdl").toString().equalsIgnoreCase(r.get("defvarmdl").toString())?"selected='selected' ":"")+
	">"+rsb.get(rsbi).get("description")+"</option>");
}
%>
							</SELECT>
							</TD> 
							<TD class="standardTxt">
							<SELECT id='sv_<%=idgrp%>_<%=iddm%>' name='sv_<%=idgrp%>_<%=iddm%>' style="width:100%" class="standardTxt">
							<OPTION value="---">---</OPTION>
<%
RecordSet rsc = rsb;//KpiMgr.getSolenoid(iddm, sessionUser.getLanguage());
if(rsc!=null)
for(int rsci=0;rsci<rsc.size();rsci++)
{
	out.print("<option value='"+rsc.get(rsci).get("idvarmdl")+"' "+
	(rsc.get(rsci).get("idvarmdl").toString().equalsIgnoreCase(r.get("solenoidvarmdl").toString())?"selected='selected' ":"")+
	">"+rsc.get(rsci).get("description")+"</option>");
}
%>
							</SELECT>
							</TD> 
							</TR>
<%
}
}
else
{
noconf++;
%>
							<!--  TR>
							<TD class="standardTxt" colspan="4" align="center">
								< %=multiLanguage.getString("kpi", "kpinonedev")% >
							</TD> 
							</TR-->
<%
}
%>
							</TBODY>
							<TBODY>
<%
RecordSet rrssll = KpiMgr.getLogicDevices(idgrp, sessionUser.getLanguage());
if(rrssll!=null && rrssll.size()!=0)
{
	for(int krs=0;krs<rrssll.size();krs++)
	{
		Record r = rrssll.get(krs);
		String iddm = r.get("idlogic").toString();
%>
							<TR>
							<TD class="standardTxt">
								<%=r.get("description")%>
							</TD> 
							<TD class="standardTxt">
							<SELECT id='mv_<%=idgrp%>_<%=iddm%>' name='mv_<%=idgrp%>_<%=iddm%>' style="width:100%" class="standardTxt">
							<OPTION value="---">---</OPTION>
<%
RecordSet rsa = KpiMgr.getLogicMasterVar(iddm, sessionUser.getLanguage());
if(rsa!=null)
for(int rsai=0;rsai<rsa.size();rsai++)
{
	out.print("<option value='"+rsa.get(rsai).get("idvariable")+"' "+
	(rsa.get(rsai).get("idvariable").toString().equalsIgnoreCase(r.get("mastervarmdl").toString())?"selected='selected' ":"")+
	">"+rsa.get(rsai).get("description")+"</option>");
}
%>
							</SELECT>
							</TD> 
							<TD class="standardTxt">
							<SELECT id='dv_<%=idgrp%>_<%=iddm%>' name='dv_<%=idgrp%>_<%=iddm%>' style="width:100%" class="standardTxt">
							<OPTION value="---">---</OPTION>
<%
RecordSet rsb = KpiMgr.getLogicDefrost(iddm, sessionUser.getLanguage());//KpiMgr.getDefrost(iddm, sessionUser.getLanguage());
if(rsb!=null)
for(int rsbi=0;rsbi<rsb.size();rsbi++)
{
	out.print("<option value='"+rsb.get(rsbi).get("idvariable")+"' "+
	(rsb.get(rsbi).get("idvariable").toString().equalsIgnoreCase(r.get("defvarmdl").toString())?"selected='selected' ":"")+
	">"+rsb.get(rsbi).get("description")+"</option>");
}
%>
							</SELECT>
							</TD> 
							<TD class="standardTxt">
							<SELECT id='sv_<%=idgrp%>_<%=iddm%>' name='sv_<%=idgrp%>_<%=iddm%>' style="width:100%" class="standardTxt">
							<OPTION value="---">---</OPTION>
<%
RecordSet rsc = rsb;//KpiMgr.getSolenoid(iddm, sessionUser.getLanguage());
if(rsc!=null)
for(int rsci=0;rsci<rsc.size();rsci++)
{
	out.print("<option value='"+rsc.get(rsci).get("idvariable")+"' "+
	(rsc.get(rsci).get("idvariable").toString().equalsIgnoreCase(r.get("solenoidvarmdl").toString())?"selected='selected' ":"")+
	">"+rsc.get(rsci).get("description")+"</option>");
}
%>
							</SELECT>
							</TD> 
							</TR>
<%
}
}
else
{
noconf++;
%>
							<!-- TR>
							<TD class="standardTxt" colspan="4" align="center">
								< %=multiLanguage.getString("kpi", "kpinonedev")% >
							</TD> 
							</TR-->
<%
}
if(noconf>=2)
{
%>
							<TR>
							<TD class="standardTxt" colspan="4" align="center">
								<%=multiLanguage.getString("kpi", "kpinonedev")%>
							</TD> 
							</TR>
<%
}
%>
							</TBODY>
							</TABLE>
						</TD>
					</TR>
					<TR>
						<TD>
							<TABLE width="100%" align="center" cellpadding="3" cellspacing="0">
								<TR>
									<TD style="border-left: 1px solid #CCCCCC;border-top: 1px solid #CCCCCC;border-bottom: 1px solid #CCCCCC;" class='standardTxt'>
										<%=multiLanguage.getString("kpi", "kpimin")%>:
									</TD>
									<TD style="border-top: 1px solid #CCCCCC;border-bottom: 1px solid #CCCCCC;" align="center" width="5%">
										<INPUT type="text" id="min_<%=idgrp%>" name="min_<%=idgrp%>" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' style="width: 100%" onkeydown="checkOnlyAnalog(this,event);" onblur="checkOnlyAnalogOnBlur(this);" value="<%=grouprecord.get("min").toString()%>"/>
									</TD>
									<TD style="border-top: 1px solid #CCCCCC;border-bottom: 1px solid #CCCCCC;" width="*">
										&nbsp;
									</TD>
									<TD style="border-left: 1px solid #CCCCCC;border-top: 1px solid #CCCCCC;border-bottom: 1px solid #CCCCCC;" class='standardTxt'>
										<%=multiLanguage.getString("kpi", "kpiminperc")%>:
									</TD>
									<TD style="border-top: 1px solid #CCCCCC;border-bottom: 1px solid #CCCCCC;" align="center" width="5%">
										<INPUT type="text" id="minp_<%=idgrp%>" name="minp_<%=idgrp%>" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' style="width: 100%" onkeydown="checkOnlyAnalog(this,event);" onblur="checkOnlyAnalogOnBlur(this);" value="<%=grouprecord.get("minperc").toString()%>"/>
									</TD>
									<TD style="border-top: 1px solid #CCCCCC;border-bottom: 1px solid #CCCCCC;" class='standardTxt' align="center">
										%
									</TD>
									<TD style="border-left: 1px solid #CCCCCC;border-top: 1px solid #CCCCCC;border-bottom: 1px solid #CCCCCC;" class='standardTxt' width="*">
										<%=multiLanguage.getString("kpi", "kpimax")%>:
									</TD>
									<TD style="border-top: 1px solid #CCCCCC;border-bottom: 1px solid #CCCCCC;" align="center" width="5%">
										<INPUT type="text" id="max_<%=idgrp%>" name="max_<%=idgrp%>" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' style="width: 100%" onkeydown="checkOnlyAnalog(this,event);" onblur="checkOnlyAnalogOnBlur(this);" value="<%=grouprecord.get("max").toString()%>" />
									</TD>
									<TD style="border-top: 1px solid #CCCCCC;border-bottom: 1px solid #CCCCCC;" width="*">
										&nbsp;
									</TD>
									<TD style="border-left: 1px solid #CCCCCC;border-top: 1px solid #CCCCCC;border-bottom: 1px solid #CCCCCC;" class='standardTxt' width="*">
										<%=multiLanguage.getString("kpi", "kpimaxperc")%>:
									</TD>
									<TD style="border-top: 1px solid #CCCCCC;border-bottom: 1px solid #CCCCCC;" align="center" width="5%">
										<INPUT type="text" id="maxp_<%=idgrp%>" name="maxp_<%=idgrp%>" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' style="width: 100%" onkeydown="checkOnlyAnalog(this,event);" onblur="checkOnlyAnalogOnBlur(this);" value="<%=grouprecord.get("maxperc").toString()%>" />
									</TD>
									<TD style="border-top: 1px solid #CCCCCC;border-bottom: 1px solid #CCCCCC;border-right: 1px solid #CCCCCC;" class='standardTxt' align="center">
										%
									</TD>
								</TR>
							</TABLE>

						</TD>
					</TR>
				</TABLE>
		</TD>
	</TR>
<%
if(i<KpiMgr.grpNum())
{
%>
	<TR>
		<TD height="5px">
			&nbsp;
		</TD>
	</TR>
<%
}
}
}
else
{
%>
	<tr height="100%" valign="top">
		<td class="tdTitleTable" align="center"><%=multiLanguage.getString("kpi","noconfigured")%></td>
	</tr>
<%
}
%>

</table>
</form>
