<%@ page language="java" 
	
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.base.system.SystemInfoExt"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.base.config.ProductInfoMgr"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	
%>
<%@ page import="com.carel.supervisor.presentation.switchtech.SwitchVarList"  %>
<%	
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
LangService l = LangMgr.getInstance().getLangService(language);
int idsite = sessionUser.getIdSite();

//creazione combo switch
int num_switch = Integer.parseInt(ProductInfoMgr.getInstance().getProductInfo().get("switchnumber"));
StringBuffer comboswitch = new StringBuffer();
comboswitch.append("<option value='-1'>-------</option>\n");
for (int i=1;i<num_switch+1;i++)
{
	comboswitch.append("<option value='"+i+"'>SWITCH"+i+"</option>\n");
}

//creazione combo modelli dispositivi
	DevMdlBeanList devmdllist = new DevMdlBeanList();
	DevMdlBean[] devmdl = devmdllist.retrieve(idsite,language);
	StringBuffer combodev = new StringBuffer();
	combodev.append("<option value='-1'>----------------------------</option>\n");
	for (int i=0;i<devmdl.length;i++)
	{
		DevMdlBean tmp = devmdl[i];
		combodev.append("<OPTION value="+tmp.getIddevmdl()+">"+tmp.getDescription()+"</OPTION>\n");
	}

//popolare tabella di destra modelli variabili

StringBuffer var_mdl_tbl = new StringBuffer();


for (int i=0;i<7;i++)
{
		var_mdl_tbl.append("<TR class='Row1'>");
		var_mdl_tbl.append("<TD class='standardTxt' width='500'>&nbsp;</TD>");
		var_mdl_tbl.append("<TD class='standardTxt' width='35'><input class='standardTxt' type='text' style='width:35px'></TD>");
		var_mdl_tbl.append("<TD class='standardTxt' width='35'><input class='standardTxt' type='text' style='width:35px'></TD>");
		var_mdl_tbl.append("</TR>");
}
	

//popolare tabella di destra modelli allarmi

boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
String virtkey = "off";
if (OnScreenKey)
{
	virtkey = "on";
}
%>
<INPUT type="hidden" id="nullselected" value="<%=l.getString("dbllistbox","nullselected")%>"/>
<INPUT type="hidden" id="doubleElement" value="<%=l.getString("dbllistbox","doublelement")%>"/>
<INPUT type="hidden" id="selectswitchalert" value="<%=l.getString("switch","selectswitchalert")%>"/>
<INPUT type="hidden" id="missingvalues" value="<%=l.getString("switch","missingvalues")%>"/>
<INPUT type="hidden" id="confirmsave" value="<%=l.getString("switch","confirmsave")%>"/>
<INPUT type="hidden" id="commitdefault" value="<%=l.getString("switch","commitdefault")%>"/>
<input type='hidden' id='s_maxval' value="<%=l.getString("dtlview","s_maxval")%>"/>
<input type='hidden' id='s_minval' value="<%=l.getString("dtlview","s_minval")%>"/>
<input type='hidden' id='virtkeyboard' value='<%=virtkey%>' />
<input type="hidden" id="restart_required" value="<%=ut.remProperty("restart_required")%>">
<input type="hidden" id="alert_restart" value="<%=l.getString("switch","alert_restart")%>">
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>

<FORM id="frm_switch_mdl" name="frm_switch_mdl" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" name="var_to_post" id="var_to_post"/>
<INPUT type="hidden" name="alr_to_post" id="alr_to_post"/>

<table class='standardTxt' cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<TD>
			<p class='standardTxt'><%=l.getString("switch","pagecomment4")%></p>		
		</TD>
	</tr>
	<tr>
		<TD>
			<FIELDSET style='width:97%;'>
			<LEGEND class='standardTxt'><%=l.getString("switch","switch")%>
			</LEGEND>
			<table class='standardTxt' cellpadding="0" cellspacing="0" width="100%" border="0" >
				<tr><TD colspan="2">&nbsp;</TD></tr>
				<TR>
					<TD width="10%" valign="top"><%=l.getString("switch","selectswitch")%></TD>
					<TD width="90%" valign="top">
						<SELECT class='standardTxt' id='switch_id' name='switch_id' onchange='onChangeSwitch();' >
							<%=comboswitch.toString()%>
						</SELECT>
					</TD>
				</tr>
			</table>
			</FIELDSET>
		</TD>
	</tr>
	<tr>
		<TD>
			<table class='standardTxt' cellpadding="0" cellspacing="0" width="100%" border="0">
				<TR><td colspan="2">&nbsp;</td></TR>
				<TR>
					<TD width="20%"><%=l.getString("switch","device")%> &nbsp;</TD>
					<TD width="80%"><SELECT class='standardTxt' id='devmdl_id' onchange='onChangeDevMdl();' >
							<%=combodev.toString()%>
						</SELECT>
					</TD>
				</TR>
			</table>
		</TD>
	</tr>
	<tr><TD> &nbsp;</TD></tr>
	<tr valign="top" align="center">
		<TD width="100%">
			<table class='standardTxt' cellpadding="0" cellspacing="0" border='0' width="100%" height="300px">
				<TR height="1%"><td colspan="4">&nbsp;</td></TR>
				
				<TR height="1%">
					<td class='th' width="44%" align="center"><b><%=l.getString("switch","varlist")%></b></td>
					<TD width="10%">&nbsp;</TD>
					<TD align='center' width="44%">
						<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
							<tr class='th' ><td width="434px" align='center'><b><%=l.getString("switch","varconf")%></b></TD>
							   <TD class='th' align='center' width="39px"><b><%=l.getString("switch","eev")%></b></TD>
								<TD class='th' align='center' width="35px"><b><%=l.getString("switch","tev")%></b></TD>
								<TD class='th' align='center' width="21px"><b>&nbsp;</b></TD>
							</tr>
						</table>
					</TD>
					<TD width="10%">&nbsp;</TD>
				</TR>
				
				<tr>
					<td width="44%">
						<div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:100%;HEIGHT:200px;' onscroll='OnDivScroll(switch_varmdl,20);'>
							<select class='standardTxt'  id='switch_varmdl' style='width:100%;HEIGHT:200px;' multiple ondblclick="moveVarLeft2Right('switch_varmdl','switch_varmdl_table','devmdl_id');return false;">
							</select>
						</div>
					</td>
					<td width="10%" align="center">
						<TABLE border="0"  width="100%"  align="center">
							<TR>
								<TD align="center">
								  <img onclick="moveVarLeft2Right('switch_varmdl','switch_varmdl_table','devmdl_id');return false;" src="images/dbllistbox/arrowdx_on.png" align="middle" />
								</TD>
							</TR>
							<tr><td height="5px"></td></tr>
							<TR>
								<TD align="center">
								  <img onclick="remVar('switch_varmdl_table');return false;" src="images/dbllistbox/delete_on.png" align="middle" />
								</TD>
							</TR>
						</TABLE>
					</td>
					<td width="44%">
						<DIV style="HEIGHT:200px;width:100%; overflow:auto;background-color:cacaca;">
							<TABLE id="switch_varmdl_table" class="table" width="100%" cellspacing="1" cellpadding="1" border="0" >
							</TABLE> 
						</DIV> 
					</td>
					<TD width="10%">&nbsp;</TD>
				</tr>
				
				<TR height="1%"><td colspan="4">&nbsp;</td></TR>
				<TR height="1%">
					<td width="44%" class='th' align='center'><b><%=l.getString("switch","alarmlist")%></b></td>
					<TD width="10%">&nbsp;</TD>
					<TD width="44%" class='th' align='center'><b><%=l.getString("switch","alarmconf")%></b></TD>
					<TD width="10%">&nbsp;</TD>
				</TR>
				
				
				
				<tr>
					<td width="44%">
						<div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:100%;HEIGHT:200px;' onscroll='OnDivScroll(switch_alrmdl,20);'>
							<select class='standardTxt'  id='switch_alrmdl' style='width:100%;HEIGHT:200px;' multiple ondblclick="moveAlrLeft2Right('switch_alrmdl','switch_alrmdl_table','devmdl_id');return false;">
						</select>
						</div>
					</td>
					<td width="10%" >
						<TABLE border="0" width="100%" >
							<TR>
								<TD align="center">
								  <img onclick="moveAlrLeft2Right('switch_alrmdl','switch_alrmdl_table','devmdl_id');return false;" src="images/dbllistbox/arrowdx_on.png" align="middle" />
								</TD>
							</TR>
							<tr><td height="5px"></td></tr>
							<TR>
								<TD align="center">
								  <img onclick="remAlr('switch_alrmdl_table');return false;" src="images/dbllistbox/delete_on.png" align="middle" />
								</TD>
							</TR>
						</TABLE>
					</td>
					<td width="44%">
						<DIV style="HEIGHT:200px;width:100%; overflow:auto;background-color:cacaca;" >
							<TABLE id="switch_alrmdl_table" class="table"  width="100%"  cellspacing="1" cellpadding="1" border="0" >
							</TABLE>
						</DIV>
					</td>
					<TD width="10%">&nbsp;</TD>
				</tr>
			</table> 	
		</TD>
	</tr>
	
</table>
</form>