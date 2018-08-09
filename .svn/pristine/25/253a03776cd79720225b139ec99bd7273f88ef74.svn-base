<%@ page language="java" pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.presentation.bean.BookletConfBean"
	import="com.carel.supervisor.presentation.bean.ProfileBean"
	import="com.carel.supervisor.presentation.bean.*"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	int idsite = sessionUser.getIdSite();
	String jsession = request.getSession().getId();

	//stringhe multilingua
	LangService lang = LangMgr.getInstance().getLangService(language);
	String devparam = lang.getString("booklet","devparam");
	String siteinfo = lang.getString("booklet","siteinfo");
	String siteconf = lang.getString("booklet","siteconf");
	String schedash = lang.getString("booklet","schedash");
	String userconf = lang.getString("booklet","userconf");
	String activealarm = lang.getString("booklet","activealarm");
	String plugmodule = lang.getString("booklet","plugmodule");
	String notes = lang.getString("booklet","notes");
	String export = lang.getString("booklet","export");
	String devices = lang.getString("booklet","devices");
	String variables = lang.getString("booklet","variables");
	String devmodels = lang.getString("booklet","devmodels");
	String parameter = lang.getString("booklet","parameter");
	String curconf = lang.getString("booklet","curconf");
	String option = lang.getString("booklet","option");
	String context = lang.getString("booklet","context");
	String add = lang.getString("booklet","add");
	String openreport = lang.getString("booklet","openreport");
//	String novarselected = lang.getString("datatransfer","nullselected");
//	String nodevselected = lang.getString("siteview","selcaract");
	String operationend = lang.getString("datatransfer","serverok");
	String alreadypres = lang.getString("datatransfer","dupl");
	String attention = lang.getString("booklet","attention");
	String confirmremove = lang.getString("setaction", "confirmremove");
	String confirmremoveall = lang.getString("setaction", "confirmremoveall");
	String delall = lang.getString("setaction", "delall");
	String del = lang.getString("setaction", "del");
	String delalltooltip = lang.getString("setaction", "delalltooltip");
	String deltooltip = lang.getString("setaction", "deltooltip");
	String warnmax = lang.getString("parameters", "warning_max_variables");
	
	// BIOLO retrieve profile permission for booklet  0:none   1: read   2: readwrite
	boolean profile_booklet = sessionUser.isButtonActive("booklet","tab1name","Save");   
	
	FileDialogBean fileDlg = new FileDialogBean(request);
%>

<input type="hidden" id="profile_booklet" value="<%=profile_booklet%>" />
<input type="hidden" id="openreport" value="<%=openreport%>" />
<!--<input type="hidden" id="novarselected" value="< %=novarselected%>" />-->
<!--<input type="hidden" id="nodevselected" value="< %=nodevselected%>" />-->
<input type="hidden" id="operationend" value="<%=operationend%>" />
<input type="hidden" id="alreadypres" value="<%=alreadypres%>" />
<input type="hidden" id="attention" value="<%=attention%>" />

<input type="hidden" id="_devices" value="<%=devices%>" />
<input type="hidden" id="_variables" value="<%=variables%>" />
<input type="hidden" id="maxvariables" value="<%=BookletConfBean.MAXENTRIES%>" />

<input type="hidden" name="deleteallparamquestion" id="deleteallparamquestion" value="<%=confirmremoveall%>" />

<INPUT type="hidden" name="delall" id="delall" value="<%=delall%>"/>
<INPUT type="hidden" name="delalltooltip" id="delalltooltip" value="<%=delall%>"/>

<input type="hidden" id="warning_max_variables" name="warning_max_variables" value="<%=warnmax%>" />

<input type='hidden' id='save_confirm' value='<%=lang.getString("fdexport","exportconfirm") %>' />
<input type='hidden' id='save_error' value="<%=lang.getString("fdexport","exporterror") %>" />

<FORM id="frm_report" action="servlet/master;jsessionid=<%=jsession%>" method="post">
	<div id="divBookletBox" style="width:100%;">
		<div id="divBookletOptions" style="height:0px;visibility:hidden">
			<fieldset>
				<legend class='standardTxt'><%=option%></legend>
				<p class="bookletOptions">
					<input <%=!profile_booklet?"disabled":""%> type='checkbox' id='devparam' name='devparam' value='devparam' checked="checked" />
					<label for="devparam"><%=devparam%></label>
				</p>
				<p class="bookletOptions">
					<input <%=!profile_booklet?"disabled":""%> type='checkbox' id='userconf' name='userconf' value='userconf' checked="checked" />
					<label for="userconf"><%=userconf%></label>
				</p>
				<p class="bookletOptions">
					<input <%=!profile_booklet?"disabled":""%> type='checkbox' id='siteinfo' name='siteinfo' value='siteinfo' checked="checked" />
					<label for="siteinfo"><%=siteinfo%></label>
				</p>
				<p class="bookletOptions">
					<input <%=!profile_booklet?"disabled":""%> type='checkbox' id='activealarm' name='activealarm' value='activealarm' checked="checked" />
					<label for="activealarm"><%=activealarm%></label>
				</p>
				<p class="bookletOptions">
					<input <%=!profile_booklet?"disabled":""%> type='checkbox' id='siteconf' name='siteconf' value='siteconf' checked="checked" />
					<label for="siteconf"><%=siteconf%></label>
				</p>
				<p class="bookletOptions">
					<input <%=!profile_booklet?"disabled":""%> type='checkbox' id='plugmodule' name='plugmodule' value='plugmodule' checked="checked" />
					<label for="plugmodule"><%=plugmodule%></label>
				</p>
				<p class="bookletOptions">
					<input <%=!profile_booklet?"disabled":""%> type='checkbox' id='schedash' name='schedash' value='schedash' checked="checked" />
					<label for="schedash"><%=schedash%></label>
				</p>
				<p class="bookletOptions">
					<input <%=!profile_booklet?"disabled":""%> type='checkbox' id='notes' name='notes' value='notes' checked="checked" />
					<label for="notes"><%=notes%></label>
				</p>
			</fieldset>
		</div>
		
		<div id="divBookletSettings" style="width:100%;">
			<fieldset class='field' style="height:80%">		
				<legend class='standardTxt'><%=context%></legend>
				<table border="0" cellpadding="1" cellspacing="1" width="100%" height="100%">
					<tr height="1%">
						<td>
							<table border="0" cellpadding="1" cellspacing="1" width="100%" height="100%" align="center">
								<tr class='standardTxt'>
									<td width="10%" align="left">
										<input <%=!profile_booklet?"disabled":""%> onclick='getDevList(this);' type='radio' id='d' name='devmodl' checked="checked" /><%=devices%>
									</td>
									<td width="10%" align="right" style="white-space:nowrap">
										<input <%=!profile_booklet?"disabled":""%> onclick='emptyComponents(this);' type='radio' id='m' name='devmodl' /><%=devmodels%>
									</td>
									<td width="55%" align="left">
										<select <%=!profile_booklet?"disabled":""%> onchange='changeDevMdl(this);' class='standardTxt' id='model' name='model' disabled style="width:275px;" />
										</select>
									</td>
									<td width="25%" align="right">
										<img src="images/actions/addsmall_on_black.png" style="cursor:pointer;" onclick="dblClickParamList(this);"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr height="*">
						<td valign="top">
							<table align="center" width="100%" class="table">
								<tr>
									<td class='th' width="49%"><%=devices%></td>
									<td width="2%">&nbsp;</td>
									<td class='th' width="49%"><%=variables %></td>
								</tr>
								<tr>
									<td height="100%" valign="top">
									<div id="div_dev">
										<select <%=!profile_booklet?"disabled":""%> id="devLst" size="10" onclick="dblClickDevList(this);" class='standardTxt' style='width:100%;'>
										</select>
									</div>
									</td>
									<td>&nbsp;</td>
									<td height="100%" valign="top">
									<div id="div_var">
										<select <%=!profile_booklet?"disabled":""%> id="paramLst" size="10" multiple ondblclick="dblClickParamList(this);" class='standardTxt' style='width:100%;'>
										</select>
									</div>
									</td>
								</tr>
							</table>
						 </td>
					</tr>
				</table>
			</fieldset>	
		</div>
		<div>&nbsp;</div>
		<div id="divBookletCurrConf">
			<fieldset class='field' style="height:30%">
				<legend class='standardTxt'><%=curconf%></legend>
				<div id="divdevvarTab">
				</div>
			</fieldset>	
		</div>
		
	</div>
</form>

<form id="reload" action="servlet/master;jsessionid=<%=jsession%>" method="post">
	<input type="hidden" name="cmd" id="cmd" value="reload">
</form>

<%=fileDlg.renderFileDialog()%>
