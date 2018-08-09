<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="supervisor.Login"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.dataaccess.dataconfig.DeviceInfo"
import="com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList"
import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBean"
import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList"
import="com.carel.supervisor.presentation.bean.rule.RelayBean"
import="com.carel.supervisor.presentation.bean.rule.RelayBeanList"
%>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	String jsession = sessionUser.getSessionId();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
		
	DeviceInfoList devinfo = new DeviceInfoList(null,"firstPV");
	DeviceInfo dev = devinfo.getByCode("-1.000");
	
	VarphyBean varRelay1 = VarphyBeanList.retrieveVarByCode(1,"DO1",dev.getId(),language);
	int varRelay1Id = varRelay1.getId();
	RelayBean relay1 = RelayBeanList.getRelayBeanByVariableid(1, language, varRelay1Id);
	String idRelay1 = Integer.toString(relay1.getIdrelay());
	
	
	VarphyBean varRelay2 = VarphyBeanList.retrieveVarByCode(1,"DO2",dev.getId(),language);
	int varRelay2Id = varRelay2.getId();
	RelayBean relay2 = RelayBeanList.getRelayBeanByVariableid(1, language, varRelay2Id);
	String idRelay2 = Integer.toString(relay2.getIdrelay());
	
	VarphyBean varRelay3 = VarphyBeanList.retrieveVarByCode(1,"DO3",dev.getId(),language);
	int varRelay3Id = varRelay3.getId();
	RelayBean relay3 = RelayBeanList.getRelayBeanByVariableid(1, language, varRelay3Id);
	String idRelay3 = Integer.toString(relay3.getIdrelay());
	
	//mutipleLanguage
	String alarmmanagcomment = multiLanguage.getString("wizard","alarmmanagcomment");
	String writecorrectemail = multiLanguage.getString("wizard","writecorrectemail");
	String testresult = multiLanguage.getString("wizard","testresult");
	String choosepriority = multiLanguage.getString("wizard","choosepriority");
	String fillSMTP = multiLanguage.getString("wizard","fillSMTP");
	String fillUSER = multiLanguage.getString("wizard","fillUSER");
	String fillPWD = multiLanguage.getString("wizard","fillPWD");
	String fillmoremail = multiLanguage.getString("wizard","fillmoremail");
	String fillmorefax = multiLanguage.getString("wizard","fillmorefax");
	String fillmorerelay = multiLanguage.getString("wizard","fillmorerelay");
	String fillmoresms = multiLanguage.getString("wizard","fillmoresms");
	String diffrules = multiLanguage.getString("wizard","diffrules");
	String noexist = multiLanguage.getString("wizard","noexist");
	String confmail = multiLanguage.getString("wizard","confmail");
	String conffax = multiLanguage.getString("wizard","conffax");
	String confsms = multiLanguage.getString("wizard","confsms");
	String confrelay = multiLanguage.getString("wizard","confrelay");
	String conntestresult = multiLanguage.getString("wizard","conntestresult");
	String sender = multiLanguage.getString("wizard","sender");
	String fillSENDER = multiLanguage.getString("wizard","fillSENDER");
	String emptyactionslist = multiLanguage.getString("wizard","emptyactionslist");
	
	String relactive = "Active"; //multiLanguage.getString("relaymgr","active");
	String relnoactive = "Not active"; //multiLanguage.getString("relaymgr","noact	ive");

	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<form name="frmcond" id="frmcond" method="post" action="servlet/master;jsessionid=<%=jsession%>">
<input type="hidden" id="isrestart" value="true">
<input type="hidden" name="idRule" id="idRule">
<input type="hidden" name="idCondition" id="idCondition" >
<input type="hidden" name="actionCode" id="actionCode">
<input type="hidden" name="idTimeBand" id="idTimeBand">

<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<input type='hidden' id='OnScreenKey' value='<%=OnScreenKey %>'/>
<input type="hidden" id="writecorrectemail" value="<%=writecorrectemail%>">
<input type='hidden' id='testresult' value='<%=testresult%>'/>
<input type='hidden' id='choosepriority' value='<%=choosepriority%>'/>
<input type='hidden' id='fillSMTP' value='<%=fillSMTP%>'/>
<input type='hidden' id='fillUSER' value='<%=fillUSER%>'/>
<input type='hidden' id='fillSENDER' value='<%=fillSENDER%>'/>
<input type='hidden' id='fillPWD' value='<%=fillPWD%>'/>
<input type='hidden' id='fillmoremail' value='<%=fillmoremail%>'/>
<input type='hidden' id='fillmorefax' value='<%=fillmorefax%>'/>
<input type='hidden' id='fillmorerelay' value='<%=fillmorerelay%>'/>
<input type='hidden' id='fillmoresms' value='<%=fillmoresms%>'/>
<input type='hidden' id='diffrules' value='<%=diffrules%>'/>
<input type='hidden' id='noexist' value='<%=noexist%>'/>
<input type='hidden' id='conntestresult' value='<%=conntestresult%>'/>
<input type='hidden' id='emptyactionslist' value='<%=emptyactionslist%>'/>
<input type='hidden' id='relay01' value='<%=multiLanguage.getString("wizard","relay")%>'/>
<input type='hidden' id='succ' value='<%=multiLanguage.getString("wizard","succ")%>'/>
<input type='hidden' id='fail' value='<%=multiLanguage.getString("wizard","fail")%>'/>
<input type='hidden' id='overwrite' value='<%=multiLanguage.getString("wizard","overwrite")%>'/>
<input type='hidden' id='inprocess' value='<%=multiLanguage.getString("wizard","inprocess")%>'/>
<input type='hidden' id='p_email' value='<%=multiLanguage.getString("wizard","email")%>'/>
<input type='hidden' id='p_fax' value='<%=multiLanguage.getString("wizard","fax")%>'/>
<input type='hidden' id='p_relay' value='<%=multiLanguage.getString("wizard","relay")%>'/>
<input type='hidden' id='p_sms' value='<%=multiLanguage.getString("wizard","sms")%>'/>
<input type='hidden' id='testall' value='<%=multiLanguage.getString("wizard","testall")%>'/>
<input type='hidden' id='internal_modem' value='<%=multiLanguage.getString("wizard","internal_modem")%>'/>
<input type='hidden' id='gsm_modem' value='<%=multiLanguage.getString("wizard","gsm_modem")%>'/>
<input type='hidden' id='sev_emails'/>
<input type='hidden' id='sev_faxs'/>
<input type='hidden' id='sev_smss'/>
<input type="hidden" id="act_mail">
<input type="hidden" id="act_fax">
<input type="hidden" id="act_relay">
<input type="hidden" id="act_sms">
<input type="hidden" id="sameActionTimeband">
<input type="hidden" id="standardTimeBand">

<table  width='100%' height='95%'>
	<tr>
		<td align='center' valign='top'>
			<table  width='100%'>
				<tr>
					<td class="standardTxt"><%=alarmmanagcomment %></td>
				</tr>
				<tr>
						<td class="standardTxt">
							<table width='100%' cellspacing='1px' cellpadding='1px' class="table">
								<tbody id="feedbackArea">
								</tbody>
							</table>
						</td>
				</tr>
				<tr>
					<td>
						<table  width='100%' cellspacing="0">
							<tr>
								<td width="33%" valign="top">
									<FIELDSET class="field">
										<LEGEND class="standardTxt"><%=multiLanguage.getString("wizard","conditions")%></LEGEND>
										<div>
										<table width="100%">
											<tr>
												<td class="standardTxt" colspan="2">
													<input type="radio" name="condtype" id="condtype1" onclick="changeType(true);" value="1"> <%=multiLanguage.getString("wizard","allalrcond")%>
												</td>
											</tr>
											<tr>
												<td class="standardTxt" valign="top">
													<input type="radio" name="condtype" id="condtype6" onclick="changeType(false);" value="6">  <%=multiLanguage.getString("wizard","priority")%>
												</td>
												<td class="standardTxt" valign="top" width="80%"> 
														<input type="checkbox" name="priority" id="priority1" value="1"><%=multiLanguage.getString("alrview","alarmstate1")%>
														<input type="checkbox" name="priority" id="priority2" value="2"><%=multiLanguage.getString("alrview","alarmstate2")%>
														<input type="checkbox" name="priority" id="priority3" value="3"><%=multiLanguage.getString("alrview","alarmstate3")%>
														<input type="checkbox" name="priority" id="priority4" value="4"><%=multiLanguage.getString("alrview","alarmstate4")%>
												</td>
											</tr>
											
										</table>
										</div>
									</FIELDSET>
								</td>
								
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td width="100%">
						<fieldset>
							<LEGEND class="standardTxt"><%=multiLanguage.getString("wizard","smsmodem")%></LEGEND>
							<table  width='100%' >
								<tr>
									<td width="550px" height="1px">
										<table width="100%" cellspacing=3 cellpadding=3 class='table'>
											<tr>
												<td class="standardTxt" width="10%"><%=multiLanguage.getString("wizard","sms")%></td>
												<td class="standardTxt" width="1px" valign='middle'>
													<input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="addSMS" id="addSMS" style="width:200px;" value="" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);">
												</td>
												<td style="padding-top:5px;" width="*">
													<img src="images/dbllistbox/arrowdx_on.png" style="cursor:pointer;" onclick="addNewRow('smsBody','smss','addSMS','sms_img','S');"/>
												</td>
											</tr>
										</table>
									</td>
									<td width="20px">
									</td>
									<td width="*" valign='top'>
										<table width="100%" cellspacing='0px' cellpadding='0px' class='table'>
											<tbody>
												<tr class='Row2'>
													<th width='*' class="standardTxt"><%=confsms%></th>
													<th width='20' id="sms_img"></th>
												</tr>
											</tbody>
											<tbody id="smsBody"></tbody>
										</table>
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td  width='100%'>
						<fieldset>
							<LEGEND class="standardTxt"><%=multiLanguage.getString("wizard","email")%></LEGEND>
							<table  width='100%' >
								<tr>
									<td width="550px" height="1px">
										<table width="100%" cellspacing="3" cellpadding="3">
											<tr>
												<td class="standardTxt" width="10%"><%=multiLanguage.getString("wizard","smtp")%>*</td>
												<td class="standardTxt" width="20%"><input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="smtp" id="smtp" style="width:200px;" value=""></td>
												<td class="standardTxt" width="10%"><%=multiLanguage.getString("setio","port")%>*</td>
												<td class="standardTxt" width="*"><input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="iomailport" id="iomailport" style="width:200px;" value=""></td>
											</tr>
											<tr>
												<td class="standardTxt"><%=multiLanguage.getString("wizard","sender")%>*</td>
												<td class="standardTxt"><input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="sender" id="sender" style="width:200px;" value="" <%=(OnScreenKey?"":" onblur='checkOnlyMail(this);'")%>/></td>
												<td class="standardTxt"><%=multiLanguage.getString("wizard","user")%></td>
												<td class="standardTxt"><input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="user" id="user" style="width:200px;" value=""></td>
											</tr>
											<tr>
												<td class="standardTxt" colspan="2"><%=multiLanguage.getString("setio","enc_auth")%>:&nbsp;
												<input type="checkbox" class='<%=(OnScreenKey?"keyboardInput":"standardTxt")%>' name="iomailencryption" id="iomailencryption" onClick="IoTechEEnc(this.checked)"/></td>
												<td class="standardTxt"><%=multiLanguage.getString("wizard","pwd")%></td>
												<td class="standardTxt"><input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="password" name="pwd" id="pwd" style="width:200px;" value=""></td>
											</tr>
										</table>
									</td>
									<td rowspan=2 width="20px">
									</td>
									<td rowspan=2 width="*" valign='top'>
										<table width="100%" cellspacing='0px' cellpadding='0px' class='table'>
											<tbody>
												<tr class='Row2'>
													<th width='*' class="standardTxt"><%=confmail%></th>
													<th width='20' id="mail_img"></th>
													</tr>
											</tbody>
											<tbody id="mailbody"></tbody>
										</table>
									</td>
								</tr>
								<tr>
									<td valign='middle'>
										<table width="100%" cellspacing=3 cellpadding=3 class='table'>
											<tr>
												<td class="standardTxt" width="10%"><%=multiLanguage.getString("wizard","email")%></td>
												<td class="standardTxt" width="1px" valign='middle'>
													<input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="addMail" id="addMail" style="width:200px;" value="" <%=(OnScreenKey?"":" onblur='checkOnlyMail(this);'")%>/>
												</td>
												<td style="padding-top:5px;" width="*">
													<img src="images/dbllistbox/arrowdx_on.png" style="cursor:pointer;" onclick="addNewRow('mailbody','emails','addMail','mail_img','E');"/>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset>
							<LEGEND class="standardTxt"><%=multiLanguage.getString("wizard","fax")%></LEGEND>
							<table  width='100%' >
								<tr>
									<td width="550px" height="1px">
										<table width="100%" cellspacing=3 cellpadding=3 class='table'>
											<tr>
												<td class="standardTxt" width="10%"><%=multiLanguage.getString("wizard","fax")%></td>
												<td class="standardTxt" width="1px" valign='middle'>
													<input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="addFAX" id="addFAX" style="width:200px;" value="" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);">
												</td>
												<td style="padding-top:5px;" width="*">
													<img src="images/dbllistbox/arrowdx_on.png" style="cursor:pointer;" onclick="addNewRow('faxBody','faxs','addFAX','fax_img','F');"/>
												</td>
											</tr>
										</table>
									</td>
									<td width="20px">
									</td>
									<td width="*" valign='top'>
										<table width="100%" cellspacing='0px' cellpadding='0px' class='table'>
											<tbody>
												<tr class='Row2'>
													<th width='*' class="standardTxt"><%=conffax%></th>
													<th width='20' id="fax_img"></th>
												</tr>
											</tbody>
											<tbody id="faxBody"></tbody>
										</table>
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset>
							<LEGEND class="standardTxt"><%=multiLanguage.getString("wizard","relay")%></LEGEND>
							<table  width='100%' >
								<tr>
									<td width="550px" height="1px">
									</td>
									<td width="20px">
									</td>
									<td width="*" valign='top'>
										<table  width='100%' cellspacing='0px' cellpadding='0px' class='table'>
											<tr class='Row2'>
												<th colspan="3" class="standardTxt"><%=confrelay%></th>
												<th width='20' id="relay_img"></th>
											</tr>
											<tr class='Row1'>
												<td class="standardTxt" width="100">
													<input type="checkbox" name="relay_1" id="relay_1" value="<%=idRelay1%>" onclick="changeStatus(this,'relay_1_o','relay_1_c')"><%=multiLanguage.getString("wizard","relay")%>1
												</td>
												<td class="standardTxt" width="100">
													<input type="radio" name="relay_1_oc" id="relay_1_o" value="1" checked><%= relactive%>
												</td>
												<td class="standardTxt">
													<input type="radio" name="relay_1_oc" id="relay_1_c" value="0"><%= relnoactive%>
												</td>
												<td></td>
											</tr>
											<tr class='Row1'>
												<td class="standardTxt">
													<input type="checkbox" name="relay_2" id="relay_2" value="<%=idRelay2%>" onclick="changeStatus(this,'relay_2_o','relay_2_c')"><%=multiLanguage.getString("wizard","relay")%>2
												</td>
												<td class="standardTxt">
													<input type="radio" name="relay_2_oc" id="relay_2_o" value="1" checked ><%= relactive%>
												</td>
												<td class="standardTxt">
													<input type="radio" name="relay_2_oc" id="relay_2_c" value="0"><%= relnoactive%>
												</td>
												<td></td>
											</tr>
											<tr class='Row1'>
												<td class="standardTxt">
													<input type="checkbox" name="relay_3" id="relay_3" value="<%=idRelay3%>" onclick="changeStatus(this,'relay_3_o','relay_3_c')"><%=multiLanguage.getString("wizard","relay")%>3
												</td>
												<td class="standardTxt">
													<input type="radio" name="relay_3_oc" id="relay_3_o" value="1" checked><%= relactive%>
												</td>
												<td class="standardTxt">
													<input type="radio" name="relay_3_oc" id="relay_3_c" value="0"><%= relnoactive%>
												</td>
												<td></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>