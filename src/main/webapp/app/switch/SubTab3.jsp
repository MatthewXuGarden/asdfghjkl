<%@ page language="java" 
	
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.base.system.SystemInfoExt"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.base.config.ProductInfoMgr"
	import="com.carel.supervisor.presentation.switchtech.SwitchConfigList"
	import="com.carel.supervisor.presentation.switchtech.SwitchConfig"
	import="com.carel.supervisor.presentation.switchtech.SwitchDev"
	import="com.carel.supervisor.presentation.switchtech.SwitchDevList"
	import="com.carel.supervisor.presentation.bean.DeviceBean"
	import="com.carel.supervisor.presentation.dbllistbox.DblListBox"
	import="java.util.ArrayList"
	
%>

<%	
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
String jsession = request.getSession().getId();
String language = sessionUser.getLanguage();
LangService l = LangMgr.getInstance().getLangService(language);

int idsite = sessionUser.getIdSite();

//creazione combo switch
int[] ids = SwitchDevList.getSwitchVarConfigured(idsite);
int num_switch = ids.length;
StringBuffer comboswitch = new StringBuffer();
comboswitch.append("<option value='-1'>----------------------</option>\n");
for (int i=0;i<num_switch;i++)
{
	//comboswitch.append("<option value='"+(i+1)+"'>"+switch_list[i].getDescription()+"</option>\n");
	comboswitch.append("<option value='"+ids[i]+"'>SWITCH"+ids[i]+"</option>\n");
}

DblListBox dblListBox = new DblListBox(new ArrayList(), new ArrayList(),
		false, true, true, null, true);
dblListBox.setIdlistbox("iddev_conf");
dblListBox.setHeaderTable1(l.getString("switch", "devlist"));
dblListBox.setHeaderTable2(l.getString("switch", "devconf"));
dblListBox.setScreenW(sessionUser.getScreenWidth());
dblListBox.setScreenH(sessionUser.getScreenHeight());
dblListBox.setHeight(450);
dblListBox.setWidthListBox(400);
String listBoxStr = dblListBox.getHtmlDblListBox();	
%>
<INPUT type="hidden"  id="nullselected" value="<%=l.getString("dbllistbox","nullselected")%>"/>
<INPUT type="hidden"  id="doubleElement" value="<%=l.getString("dbllistbox","doublelement")%>"/>
<INPUT type="hidden" id="missingdescription" value="<%=l.getString("switch","missingdescription")%>"/>
<input type="hidden" id="restart_required" value="<%=ut.remProperty("restart_required")%>">
<input type="hidden" id="alert_restart" value="<%=l.getString("switch","alert_restart")%>">
<FORM id="frm_switch_dev" name="frm_switch_dev" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" name="param" id="param"/>
<table class='standardTxt' cellpadding="0" cellspacing="0" border="0" width="100%" height="80%">
	<TR height="5%">
		<td>
			<p class='standardTxt'><%=l.getString("switch","pagecomment3")%></p>
		</td>
	</TR>
	<TR height="1%"><td> &nbsp;</td></TR>
	<TR height="15%">
		<td>
			<FIELDSET style='width:97%'>
			<LEGEND class='standardTxt'><%=l.getString("switch","switch")%></LEGEND>
			<table class='standardTxt' cellpadding="0" cellspacing="0" width="100%" >
				<TR>
					<TD>
						<table class='standardTxt' cellpadding="0" cellspacing="0">
							<tr>
								<TD><%=l.getString("switch","selectswitch")%>&nbsp;</TD>
								<TD><SELECT class='standardTxt' id='switch_id' name='switch_id' onchange='onChangeSwitch();'>
										<%=comboswitch.toString()%>
									</SELECT>
								</TD>
								<TD width="100px">&nbsp;</TD>
					<TD align="right"><%=l.getString("switch","desc")%>&nbsp;</TD>
					<TD align="center"><input name='switch_description' id='switch_description' class='standardTxt' type='text' maxlength="40" style='width:300px;'></TD> 
							</tr>
						</table>
					</TD>
				</TR>
			</table>
			</FIELDSET>
		</td>
	</TR>
	<TR height="*" valign="top" align="center">
		<td>
			<%=listBoxStr %>
		</td>
	</TR>
</table>





</form>