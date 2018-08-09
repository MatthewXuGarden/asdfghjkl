<%@ page language="java"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.kpi.KpiMgr"
import="com.carel.supervisor.dataaccess.db.RecordSet"
import="java.util.GregorianCalendar"
import="java.util.Calendar"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.bean.FileDialogBean"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	String language = sessionUser.getLanguage();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
	String grp = sessionUser.getProperty("kpigrp");
	Integer idgrp = new Integer(grp);
	String grpdesc = sessionUser.getProperty("kpigrpdesc");
	RecordSet rs = KpiMgr.getGroupConf(idgrp);
	RecordSet rslogic = KpiMgr.getLogicGroupConf(idgrp);
	GregorianCalendar gc = new GregorianCalendar();
	Integer min = null;
	Integer minp = null;
	Integer max = null;
	Integer maxp = null;
	if (rs != null && rs.size() > 0)
	{
		min = (Integer) rs.get(0).get("min");
		minp = (Integer) rs.get(0).get("minperc");
		max = (Integer) rs.get(0).get("max");
		maxp = (Integer) rs.get(0).get("maxperc");
	}
	else if (rslogic != null && rslogic.size() > 0)
	{
		min = (Integer) rslogic.get(0).get("min");
		minp = (Integer) rslogic.get(0).get("minperc");
		max = (Integer) rslogic.get(0).get("max");
		maxp = (Integer) rslogic.get(0).get("maxperc");
	}
	FileDialogBean fileDlg = new FileDialogBean(request);
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<%=fileDlg.renderFileDialog()%>
<INPUT type="hidden" id="kpicmd" name="kpicmd" value="kpiresult" />
<INPUT type="hidden" id="kpiresult" name="kpiresult" value="error" />
<INPUT type="hidden" id="kpiresultcomp" name="kpiresultcomp" value='<%=multiLanguage.getString("kpiresult","kpicomputing")%>' />
<INPUT type="hidden" id="kpigrp" name="kpigrp" value="<%=grp%>" />
<INPUT type="hidden" id="kpifinished" name="kpifinished" value="<%=multiLanguage.getString("kpiresult","finished1")%>" />
<INPUT type="hidden" id="invaliddate" name="invaliddate" value="<%=multiLanguage.getString("kpiresult","invaliddate")%>" />
<INPUT type="hidden" id="errmessage" name="errorerror" value="<%=sessionUser.getPropertyAndRemove("error")%>" />
<INPUT type="hidden" id="percentageerror" name="percentageerror" value="<%=multiLanguage.getString("kpi","percentageerror")%>" />
<INPUT type="hidden" id="emptyfieldserror" name="emptyfieldserror" value="<%=multiLanguage.getString("kpiresult","emptyfieldserror")%>" />
<INPUT type="hidden" id="minmaxerror" name="minmaxerror" value="<%=multiLanguage.getString("kpi","minmaxerror") %>" />
<INPUT type='hidden' id='save_confirm' value='<%=multiLanguage.getString("fdexport","exportconfirm") %>' />
<INPUT type='hidden' id='save_error' value="<%=multiLanguage.getString("fdexport","exporterror") %>" />
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>

<INPUT type="hidden" id="reportkpistart" name="reportkpistart" value="" />
<INPUT type="hidden" id="reportkpistop"  name="reportkpistop"  value="" />

<p class='standardTxt'><%=multiLanguage.getString("kpiresult","comment1")%></p>
<table border="0" width="98%" cellspacing="1" cellpadding="1">
	<TBODY>
		<TR>
			<TD align="center" valign="middle">
				<FORM>
					<FIELDSET  class='field'>
						<LEGEND id='kpicurrentgroup' class="standardTxt">
							<%=grpdesc%>&nbsp;
						</LEGEND>
						<DIV id="kpiconfcontainer" style="visibility: hidden;display: none;">
<%
						for (int kw = 0; kw < rs.size(); kw++)
						{
							Integer iddddv = (Integer)rs.get(kw).get("iddevice");
%>
						<DIV id="<%=rs.get(kw).get("iddevice").toString()%>" style="visibility: hidden;display: none;">
							<INPUT type="hidden" id="<%=iddddv%>grp" name="<%=iddddv%>grp" value="<%=rs.get(kw).get("idgrp").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>min" name="<%=iddddv%>min" value="<%=rs.get(kw).get("min").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>minperc" name="<%=iddddv%>minperc" value="<%=rs.get(kw).get("minperc").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>max" name="<%=iddddv%>max" value="<%=rs.get(kw).get("max").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>maxperc" name="<%=iddddv%>maxperc" value="<%=rs.get(kw).get("maxperc").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>iddevmdl" name="<%=iddddv%>iddevmdl" value="<%=rs.get(kw).get("iddevmdl").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>mastervarmdl" name="<%=iddddv%>mastervarmdl" value="<%=rs.get(kw).get("mastervarmdl").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>defvarmdl" name="<%=iddddv%>defvarmdl" value="<%=rs.get(kw).get("defvarmdl").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>solenoidvarmdl" name="<%=iddddv%>solenoidvarmdl" value="<%=rs.get(kw).get("solenoidvarmdl").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>iddevice" name="<%=iddddv%>iddevice" value="<%=rs.get(kw).get("iddevice").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>vmid" name="<%=iddddv%>vmid" value="<%=rs.get(kw).get("vmid").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>vdid" name="<%=iddddv%>vdid" value="<%=rs.get(kw).get("vdid").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>vsid" name="<%=iddddv%>vsid" value="<%=rs.get(kw).get("vsid").toString()%>" />
						</DIV>
<%
						}
%>
<%
						for (int kw = 0; kw < rslogic.size(); kw++)
						{
							Integer iddddv = (Integer)rslogic.get(kw).get("iddevmdl");
%>
						<DIV id="<%=rslogic.get(kw).get("iddevmdl").toString()%>" style="visibility: hidden;display: none;">
							<INPUT type="hidden" id="<%=iddddv%>grp" name="<%=iddddv%>grp" value="<%=rslogic.get(kw).get("idgrp").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>min" name="<%=iddddv%>min" value="<%=rslogic.get(kw).get("min").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>minperc" name="<%=iddddv%>minperc" value="<%=rslogic.get(kw).get("minperc").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>max" name="<%=iddddv%>max" value="<%=rslogic.get(kw).get("max").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>maxperc" name="<%=iddddv%>maxperc" value="<%=rslogic.get(kw).get("maxperc").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>iddevmdl" name="<%=iddddv%>iddevmdl" value="<%=rslogic.get(kw).get("iddevmdl").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>mastervarmdl" name="<%=iddddv%>mastervarmdl" value="<%=rslogic.get(kw).get("mastervarmdl").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>defvarmdl" name="<%=iddddv%>defvarmdl" value="<%=rslogic.get(kw).get("defvarmdl").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>solenoidvarmdl" name="<%=iddddv%>solenoidvarmdl" value="<%=rslogic.get(kw).get("solenoidvarmdl").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>iddevice" name="<%=iddddv%>iddevice" value="<%=rslogic.get(kw).get("iddevmdl").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>vmid" name="<%=iddddv%>vmid" value="<%=rslogic.get(kw).get("mastervarmdl").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>vdid" name="<%=iddddv%>vdid" value="<%=rslogic.get(kw).get("defvarmdl").toString()%>" />
							<INPUT type="hidden" id="<%=iddddv%>vsid" name="<%=iddddv%>vsid" value="<%=rslogic.get(kw).get("solenoidvarmdl").toString()%>" />
						</DIV>
<%
						}
%>
						</DIV>	
						<TABLE align="center" border="0" cellpadding="2" cellspacing="2" width="100%">
							<TR>
								<TD width="45%">
									<TABLE border="0">
										<TR>
											<TD>
												<TABLE border="0" cellpadding="5" cellspacing="3">
													<TR>
														<TD class="standardTxt">
															<%=multiLanguage.getString("kpiresult", "enddate")%>
														</TD>
														<TD width="200px" class="standardTxt" align="center" valign="middle">
															<SELECT id='dd' name='dd' class="standardTxt" style="text-align: right;">
																<%for (int i = 1; i <= 31; i++)
			{%>
																<OPTION <%=(i==gc.get(Calendar.DAY_OF_MONTH)?"selected=\"selected\"":"")%> value="<%=i%>">
																	<%=i%>
																</OPTION>
																<%}
%>
															</SELECT>
															/
															<SELECT id="mm" name='mm' class="standardTxt" style="text-align: right;">
																<%for (int i = 1; i <= 12; i++)
			{%>
																<OPTION <%=((i-1)==gc.get(Calendar.MONTH)?"selected=\"selected\"":"")%> value="<%=i%>">
																	<%=i%>
																</OPTION>
																<%}

			%>
															</SELECT>
															/
															<INPUT id="yyyy" type="text" name="yyyy" <%=(OnScreenKey ? "class='keyboardInput'" : "class='standardTxt'")%> style="width: 50px;text-align: right;" value="<%=gc.get(Calendar.YEAR)%>" onkeydown='checkOnlyNumber(this,event);' onblur='onlyNumberOnBlur(this);'/>
														</TD>
														<TD class="standardTxt">
															<%=multiLanguage.getString("kpiresult", "endhour")%>
														</TD>
														<TD class="standardTxt" align="center" valign="middle">
															<SELECT id="hh" class="standardTxt" style="text-align: right;">
																<%for (int i = 0; i < 24; i++)
			{%>
																<OPTION <%=(i==gc.get(Calendar.HOUR_OF_DAY)?"selected=\"selected\"":"")%> value="<%=i%>">
																	<%=i<10?"0":""%><%=i%>
																</OPTION>
																<%}

			%>
															</SELECT>
														</TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
										<TR>
											<TD>
												<TABLE border="0" cellpadding="5" cellspacing="3">
													<TR>
														<TD class="standardTxt">
															<%=multiLanguage.getString("kpiresult", "timewindow")%>
														</TD>
														<TD>
															<SELECT id='timewin' name="timewin" class="standardTxt" style="width:90px;">
																<OPTION value="h_1">
																	1
																	<%=multiLanguage.getString("kpiresult", "hour")%>
																</OPTION>
																<OPTION value="h_3">
																	3
																	<%=multiLanguage.getString("kpiresult", "hours")%>
																</OPTION>
																<OPTION value="h_6">
																	6
																	<%=multiLanguage.getString("kpiresult", "hours")%>
																</OPTION>
																<OPTION value="h_12">
																	12
																	<%=multiLanguage.getString("kpiresult", "hours")%>
																</OPTION>
																<OPTION value="g_1">
																	1
																	<%=multiLanguage.getString("kpiresult", "day")%>
																</OPTION>
																<OPTION value="g_3">
																	3
																	<%=multiLanguage.getString("kpiresult", "days")%>
																</OPTION>
																<OPTION value="g_7">
																	7
																	<%=multiLanguage.getString("kpiresult", "days")%>
																</OPTION>
																<OPTION value="g_15">
																	15
																	<%=multiLanguage.getString("kpiresult", "days")%>
																</OPTION>
																<OPTION value="m_1">
																	1
																	<%=multiLanguage.getString("kpiresult", "month")%>
																</OPTION>
																<OPTION value="m_3">
																	3
																	<%=multiLanguage.getString("kpiresult", "months")%>
																</OPTION>
																<OPTION value="m_6">
																	6
																	<%=multiLanguage.getString("kpiresult", "months")%>
																</OPTION>
															</SELECT>
														</TD>
													</TR>
												</TABLE>
											</TD>
										</TR>
									</TABLE>
								</TD>
								<TD width="35%" align="center" valign="middle">
									<TABLE border="0" cellpadding="5" cellspacing="3" width="90%" class="table">
										<TR>
											<TD class="standardTxt" align="center" width="25%">
												<%=multiLanguage.getString("kpiresult", "min")%>
											</TD>
											<TD class="standardTxt" align="center" width="25%">
												<%=multiLanguage.getString("kpiresult", "minp")%>
											</TD>
											<TD class="standardTxt" align="center" width="25%">
												<%=multiLanguage.getString("kpiresult", "max")%>
											</TD>
											<TD class="standardTxt" align="center" width="25%">
												<%=multiLanguage.getString("kpiresult", "maxp")%>
											</TD>
										</TR>
										<TR>
											<TD align="center">
												<INPUT type="text" id="min" name="min" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" style="width: 40px;text-align: right;" value="<%=min!=null?min:""%>" onkeydown='checkOnlyAnalog(this,event);' onblur='checkOnlyAnalogOnBlur(this);'>
											</TD>
											<TD align="center">
												<INPUT type="text" id="minp" name="minp" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" style="width: 40px;text-align: right;" value="<%=minp!=null?minp:""%>" onkeydown='checkOnlyAnalog(this,event);' onblur='checkOnlyAnalogOnBlur(this);'>
											</TD>
											<TD align="center">
												<INPUT type="text" id="max" name="max" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" style="width: 40px;text-align: right;" value="<%=max!=null?max:""%>" onkeydown='checkOnlyAnalog(this,event);' onblur='checkOnlyAnalogOnBlur(this);'>
											</TD>
											<TD align="center">
												<INPUT type="text" id="maxp" name="maxp" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" style="width: 40px;text-align: right;" value="<%=maxp!=null?maxp:""%>" onkeydown='checkOnlyAnalog(this,event);' onblur='checkOnlyAnalogOnBlur(this);'>
											</TD>
										</TR>
									</TABLE>
								</TD>
								<TD width="20%" align="center" valign="middle">
									<TABLE border="0" cellpadding="5" cellspacing="3" width="80%" class="table">
										<TR>
											<TD class="standardTxt" align="center" width="50%">
												<%=multiLanguage.getString("kpiresult", "defrost")%>
											</TD>
											<TD class="standardTxt" align="center" width="50%">
												<%=multiLanguage.getString("kpiresult", "solenoid")%>
											</TD>
										</TR>
										<TR>
											<TD align="center">
												<INPUT type="checkbox" id="def" name="def" style="wi dth: 25px;heig ht: 25px;" checked="checked">
											</TD>
											<TD align="center">
												<INPUT type="checkbox" id="sol" name="sol" style="wid th: 25px;he ight: 25px;" checked="checked">
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>
						</TABLE>
					</FIELDSET>
				</FORM>
			</TD>
		</TR>
		<TR>
			<TD>
				<TABLE id="kpiresultstable" border="0" width="98%" class='table' style="visibility: hidden;display: none;">
					<THEAD>
						<TR>
							<TH width='*' class='th'><%=multiLanguage.getString("kpiresult","rdesc")%></TH>
							<TH width='10%' class='th'><%=multiLanguage.getString("pChrono","setpoint")%></TH>
							<TH width='5%' class='th'><%=multiLanguage.getString("kpiresult","rtotal")%></TH>
							<TH width='5%' class='th'><%=multiLanguage.getString("kpiresult","rtunder")%></TH>
							<TH width='5%' class='th'><%=multiLanguage.getString("kpiresult","rpunder")%></TH>
							<TH width='5%' class='th'><%=multiLanguage.getString("kpiresult","rtover")%></TH>
							<TH width='5%' class='th'><%=multiLanguage.getString("kpiresult","rpover")%></TH>
							<TH width='5%' class='th'><%=multiLanguage.getString("kpiresult","rmean")%></TH>
							<!-- TH width='5%' class='th'>< %=multiLanguage.getString("kpiresult","rdc")%></TH -->
							<TH width='5%' class='th'><%=multiLanguage.getString("kpiresult","rtdef")%></TH>
							<TH width='5%' class='th'><%=multiLanguage.getString("kpiresult","rpdef")%></TH>
							<TH width='5%' class='th'><%=multiLanguage.getString("kpiresult","rtsol")%></TH>
							<TH width='5%' class='th'><%=multiLanguage.getString("kpiresult","rdc")%></TH>
							<!-- TH width='5%' class='th'>< %=multiLanguage.getString("kpiresult","rpsol")%></TH-->
						</TR>

					</THEAD>
					<TBODY>
					</TBODY>

				</TABLE>
			</TD>
		</TR>
		<tr><td>&nbsp;</td></tr>
		<tr><td>
<table id="legendTable" width="98%" class='table' style="visibility: hidden;display: none;" align="center">
  <tr>
    <td class="standardTxt" width="12%"><b><%=multiLanguage.getString("kpiresult","rtotal")%></b>:</td>
    <td class="standardTxt"><%=multiLanguage.getString("kpiresult","rtotal2")%></td>
    <td class="standardTxt" width="12%"><b><%=multiLanguage.getString("kpiresult","rmean")%></b>:</td>
    <td class="standardTxt"><%=multiLanguage.getString("kpiresult","rmean2")%></td>
  </tr>
  <tr>
    <td class="standardTxt" width="12%"><b><%=multiLanguage.getString("kpiresult","rtunder")%></b>:</td>
    <td class="standardTxt"><%=multiLanguage.getString("kpiresult","rtunder2")%></td>
    <td class="standardTxt" width="12%"><b><%=multiLanguage.getString("kpiresult","rtdef")%></b>:</td>
    <td class="standardTxt"><%=multiLanguage.getString("kpiresult","rtdef2")%></td>
  </tr>
  <tr>
    <td class="standardTxt" width="12%"><b><%=multiLanguage.getString("kpiresult","rpunder")%></b>:</td>
    <td class="standardTxt"><%=multiLanguage.getString("kpiresult","rpunder2")%></td>
    <td class="standardTxt" width="12%"><b><%=multiLanguage.getString("kpiresult","rpdef")%></b>:</td>
    <td class="standardTxt"><%=multiLanguage.getString("kpiresult","rpdef2")%></td>
  </tr>
  <tr>
    <td class="standardTxt" width="12%"><b><%=multiLanguage.getString("kpiresult","rtover")%></b>:</td>
    <td class="standardTxt"><%=multiLanguage.getString("kpiresult","rtover2")%></td>
    <td class="standardTxt" width="12%"><b><%=multiLanguage.getString("kpiresult","rtsol")%></b>:</td>
    <td class="standardTxt"><%=multiLanguage.getString("kpiresult","rtsol2")%></td>
  </tr>
  <tr>
    <td class="standardTxt" width="12%"><b><%=multiLanguage.getString("kpiresult","rpover")%></b>:</td>
    <td class="standardTxt"><%=multiLanguage.getString("kpiresult","rpover2")%></td>
    <td class="standardTxt" width="12%"><b><%=multiLanguage.getString("kpiresult","rdc")%></b>:</td>
    <td class="standardTxt"><%=multiLanguage.getString("kpiresult","rdc2")%></td>
  </tr>
</table>
		</td></tr>
	</TBODY>
</table>
