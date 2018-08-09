<%@page import="com.carel.supervisor.plugin.energy.EnergyConsumer"%>
<%@ page language="java"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.devices.DeviceList"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList"
	import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBean"
	import="com.carel.supervisor.presentation.bo.BAlrEvnSearch"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.plugin.energy.EnergyMgr"
	import="com.carel.supervisor.plugin.energy.EnergyConfiguration"
	import="com.carel.supervisor.plugin.base.Plugin"
	import="com.carel.supervisor.presentation.bean.FileDialogBean"
	import="com.carel.supervisor.plugin.energy.EnergyReport"
	import="com.carel.supervisor.director.packet.PacketMgr"	
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	String language = sessionUser.getLanguage();
	// int idsite = sessionUser.getIdSite();
	LangService lan = LangMgr.getInstance().getLangService(language);
	FileDialogBean fileDlg = new FileDialogBean(request);
%>
<%	
int size = 0;
try {
	size = EnergyMgr.getInstance().getSiteConfiguration().getConsumerList(language).size();
}
catch (Exception e) {
	size = 0;
}
if(size == 0)
{
%>
<input type="hidden" value="false" id="plgnotcnf">
<table border="0" width="100%" cellpadding="9" cellspacing="1">
	<tr>
		<td align="center" style="font-size:12pt;"><%=lan.getString("energy","pgtobedone") %></td>
	</tr>
</table>  
<%
	} 
	else 
	{
	String from = lan.getString("alrsearch", "from");
	String to = lan.getString("alrsearch", "to");

	String dom = lan.getString("cal", "sun");
	String lun = lan.getString("cal", "mon");
	String ma = lan.getString("cal", "tue");
	String mer = lan.getString("cal", "wed");
	String gio = lan.getString("cal", "thu");
	String ven = lan.getString("cal", "fri");
	String sab = lan.getString("cal", "sat");

	String gen = lan.getString("cal", "january");
	String feb = lan.getString("cal", "february");
	String mar = lan.getString("cal", "march");
	String apr = lan.getString("cal", "april");
	String mag = lan.getString("cal", "may");
	String giu = lan.getString("cal", "june");
	String lug = lan.getString("cal", "july");
	String ago = lan.getString("cal", "august");
	String set = lan.getString("cal", "september");
	String ott = lan.getString("cal", "october");
	String nov = lan.getString("cal", "november");
	String dic = lan.getString("cal", "december");

	String date1higher2 = lan.getString("report", "date1higher2");
%>
<input type="hidden" value="true" id="plgnotcnf">
<input type="hidden" value="true" id="plgennotreg">
<input type='hidden' id='reload_from' value='<%=sessionUser.getProperty("datefrom")%>' />
<input type='hidden' id='reload_to' value='<%=sessionUser.getProperty("dateto")%>' />
<INPUT type='hidden' id='date1higher2' value="<%=date1higher2%>" />
<input type='hidden' id='save_confirm' value='<%=lan.getString("fdexport","exportconfirm") %>' />
<input type='hidden' id='save_error' value="<%=lan.getString("fdexport","exporterror") %>" />
<%
	EnergyConfiguration econf = EnergyMgr.getInstance().getSiteConfiguration();
	int dimension = econf.getGroups().size();
	int numgroups = new Integer(econf.getSiteProperty(EnergyConfiguration.NUMGROUPS,"10"));
%>
<%= fileDlg.renderFileDialog() %>
<form id="energyexportreport" action="servlet/master;jsessionid=<%=sessionUser.getSessionId()%>" method="post">
<table border="0" width="100%" align="center" cellspacing="20"cellpadding="5">
	<TR>
		<TD>
				<TABLE border="0" width="50%" cellspacing="0" cellpadding="0"
					align="center">
					<TR>
						<TD valign="top" colspan="1" align="center">
							<TABLE border="0" align="center" width="100%" cellspacing="5" cellpadding="5">
								<TR>
									<TD class="standardTxt"><%=from%></TD>
									<TD class="standardTxt" align="center">
										<table border="0" width="100%">
											<tr>
												<td colspan="3" align="center" valign="top">
													<div id="cal_energyfrom_display"></div>
												</td>
											</tr>
											<tr>
												<td align="center">
													<input type="hidden" name="energyfrom" id="energyfrom" value="" />
													<input class="standardTxt" disabled type="text"
														name="energyfrom_day" id="energyfrom_day" value="" size="2"
														maxLength="2" onblur="onlyNumberOnBlur(this);"
														onkeydown='checkOnlyNumber(this,event);' />
												</td>
												<td align="center">
													<select class="standardTxt" disabled name="energyfrom_month"
														id="energyfrom_month">
														<option value="1"><%=gen%></option>
														<option value="2"><%=feb%></option>
														<option value="3"><%=mar%></option>
														<option value="4"><%=apr%></option>
														<option value="5"><%=mag%></option>
														<option value="6"><%=giu%></option>
														<option value="7"><%=lug%></option>
														<option value="8"><%=ago%></option>
														<option value="9"><%=set%></option>
														<option value="10"><%=ott%></option>
														<option value="11"><%=nov%></option>
														<option value="12"><%=dic%></option>
													</select>
												</td>
												<td align="center">
													<input class="standardTxt" disabled type="text"
														name="energyfrom_year" id="energyfrom_year" value="" size="4"
														maxLength="4" onblur="onlyNumberOnBlur(this);"
														onkeydown='checkOnlyNumber(this,event);' />
												</td>
											</tr>
										</table>
										<script type="text/javascript">
											var arDayfrom = new Array("<%=dom%>","<%=lun%>","<%=ma%>","<%=mer%>","<%=gio%>","<%=ven%>","<%=sab%>");
											var arMonthfrom = new Array("<%=gen%>","<%=feb%>","<%=mar%>","<%=apr%>","<%=mag%>","<%=giu%>","<%=lug%>","<%=ago%>","<%=set%>","<%=ott%>","<%=nov%>","<%=dic%>");
											var cal1 = new Calendar ("cal1", "energyfrom", new Date(), arDayfrom, arMonthfrom);
											renderCalendar(cal1);
										</script>
									</TD>
									<TD class="standardTxt" align="center"><%=to%></TD>
									<TD class="standardTxt" align="center">
										<table border="0" align="center" width="100%">
											<tr>
												<td colspan="3" align="center" valign="top">
													<div id="cal_energyto_display"></div>
												</td>
											</tr>
											<tr>
												<td align="center">
													<input type="hidden" name="energyto" id="energyto" value="" />
													<input class="standardTxt" disabled type="text"
														name="energyto_day" id="energyto_day" value="" size="2"
														maxLength="2" onblur="onlyNumberOnBlur(this);"
														onkeydown='checkOnlyNumber(this,event);' />
												</td>
												<td align="center">
													<select class="standardTxt" disabled name="energyto_month"
														id="energyto_month">
														<option value="1"><%=gen%></option>
														<option value="2"><%=feb%></option>
														<option value="3"><%=mar%></option>
														<option value="4"><%=apr%></option>
														<option value="5"><%=mag%></option>
														<option value="6"><%=giu%></option>
														<option value="7"><%=lug%></option>
														<option value="8"><%=ago%></option>
														<option value="9"><%=set%></option>
														<option value="10"><%=ott%></option>
														<option value="11"><%=nov%></option>
														<option value="12"><%=dic%></option>
													</select>
												</td>
												<td align="center">
													<input class="standardTxt" disabled type="text"
														name="energyto_year" id="energyto_year" value="" size="4"
														maxLength="4" onblur="onlyNumberOnBlur(this);"
														onkeydown='checkOnlyNumber(this,event);' />
												</td>
											</tr>
										</table>
										<script type="text/javascript">
										var arDayto = new Array("<%=dom%>","<%=lun%>","<%=ma%>","<%=mer%>","<%=gio%>","<%=ven%>","<%=sab%>");
										var arMonthto = new Array("<%=gen%>","<%=feb%>","<%=mar%>","<%=apr%>","<%=mag%>","<%=giu%>","<%=lug%>","<%=ago%>","<%=set%>","<%=ott%>","<%=nov%>","<%=dic%>");
										var cal2 = new Calendar ("cal2", "energyto", new Date(), arDayto, arMonthto);
										renderCalendar(cal2);
									</script>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<tr>
						<td align="center"> 
						<table class="table" width="100%" cellspacing="0" cellpadding="5">
							<tr class="Row1">
								<td class="standardTxt" align="right">
									<%=lan.getString("energy","enerdataexportstep") %>
								</td>
								<td class="standardTxt" align="left">
									<select id="sceltaGranularita" name="sceltaGranularita" style="width: 100px;"> 
										<option value="<%=EnergyReport.HOUR %>"> 
											<%=lan.getString("energy","report"+EnergyReport.HOUR) %> 
										</option> 
										<option value="<%=EnergyReport.DAY %>"> 
											<%=lan.getString("energy","report"+EnergyReport.DAY) %>  
										</option> 
										<option value="<%=EnergyReport.MONTH %>"> 
											<%=lan.getString("energy","report"+EnergyReport.MONTH) %>
										</option> 
									</select>
								</td>
								<td class="standardTxt" align="right">
									<table width="80%" align="center" cellpadding="5" cellspacing="0" border=0>
										<tbody>
										<tr class="Row1">
											<td class="standardTxt" width="30%" align='left'>
												<input id="rep1" type="radio" value="<%=EnergyReport.SITE %>" name="exporttype" checked="checked"
												onclick="changereporttype(this)"><%=lan.getString("energy","enersite") %>
											</td>
											<td class="standardTxt" width="35%">&nbsp;</td>
										</tr>
										<tr class="Row1">
											<td class="standardTxt" align='left'>
												<input id="rep2" type="radio" value="<%=EnergyReport.GROUP %>" name="exporttype"
												onclick="changereporttype(this)"><%=lan.getString("energy","energroup") %>
											</td>
											<td class="standardTxt">
												<select id="repgrplistgrp" name="group" style="width: 340px" disabled="disabled"> 
												<%for(int i=0;i<=numgroups;i++){
												if(econf.getGroup(i)!=null && econf.getGroup(i).isEnabled()){ %>
												<option value="<%=econf.getGroup(i).getId()%>">
													<%=econf.getGroup(i).getName()%> 
												</option> 
												<%}}%> 
												</select>
											</td>
										</tr>
<%if( PacketMgr.getInstance().isFunctionAllowed("kpi") ) {%>										
										<tr class="Row1">
											<td class="standardTxt">
												<input id="rep3" type="radio" value="coldrental" name="exporttype"
												onclick="changereporttype(this)"><%=lan.getString("energy","meter")%>
												(<%=lan.getString("energy","tab7name")%>)
											</td>
											<td class="standardTxt">
												<select id="repconslistcons" name="consvar" style="width: 340px" disabled="disabled">
												<%=EnergyConsumer.getHtmlConsumerOptions(sessionUser)%>
												</select>
											</td>
										</tr>
<%}%>
										</tbody>
									</table> 
								</td>
							</tr>
						</table>
						</td>
					</tr>
				</TABLE>
		</TD>
	</TR>
</table>
</form>
<%}%>