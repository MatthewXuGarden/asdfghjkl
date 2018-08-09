<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.presentation.bean.rule.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.presentation.dbllistbox.*"
import="com.carel.supervisor.presentation.rule.*"
import="com.carel.supervisor.dataaccess.datalog.impl.*"
import="java.util.*"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%
//page variables//
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();
UserTransaction ut = sessionUser.getCurrentUserTransaction();
String notremoveaction = ut.remProperty("notremoveaction");
String issched=ut.getProperty("folder").equalsIgnoreCase("setaction")?"FALSE":"TRUE";

//devices objects//
DeviceListBean devices = new DeviceListBean(idsite,language);
String devicesStr = lan.getString("booklet","devices");
String devmodels = lan.getString("booklet","devmodels");
DeviceBean deviceBean = null;
int[] devicesids = devices.getIds();

//variables objects//
//VarphyBean[] variablelist = new VarphyBeanList().getVarNotAlarmsRw(language, idsite, iddevice);
VarphyBean variableBean = null;

// REFACTORING popolare tabella da cfaction
int actioncode = Integer.parseInt(sessionUser.getProperty("actioncode"));
String action_description = ActionBeanList.getDescription(idsite,actioncode);
ActionBeanList actionlist = new ActionBeanList();
String param = actionlist.getActionParameters(idsite, actioncode, "V");

//page i18n strings//
String actionnotremoved = lan.getString("alrsched","actionnotremoved2");
String variable = lan.getString("action","variable");
String doubleElement = lan.getString("dbllistbox","doublelement");
String nullselected = lan.getString("dbllistbox","nullselected");
String setto = lan.getString("action","setto");
String value = lan.getString("action","value");
String action = lan.getString("setaction","action");
String device = lan.getString("setaction","device");
String variableavailable = lan.getString("setaction","variableavailable");
String variableconfigured = lan.getString("setaction","variableconfigured");
String forcesetvalue = lan.getString("setaction","forcesetvalue");
String nomodactionfromide = lan.getString("ide","nomodactionfromide");
String selectdevice=lan.getString("setaction","selectdevice");
//20090119 - BUG #5289 RF Check on max elements
String maxvariableerror = lan.getString("dbllistbox", "maxelements");
//new 
String seldev = lan.getString("setaction", "seldev");
String destination = lan.getString("setaction", "destination");
String source = lan.getString("setaction", "source");
String configured = lan.getString("setaction", "configured");
String constant = lan.getString("setaction", "const");
String variables = lan.getString("setaction", "variables");
String descsrc = lan.getString("setaction", "descsrc");
String descdest = lan.getString("setaction", "descdest");
String delall = lan.getString("setaction", "delall");
String del = lan.getString("setaction", "del");
String delalltooltip = lan.getString("setaction", "delalltooltip");
String deltooltip = lan.getString("setaction", "deltooltip");
String novarconf = lan.getString("setaction", "novarconf");
String selsourceerror = lan.getString("setaction", "selsourceerror");
String seldestinationerror = lan.getString("setaction", "seldestinationerror");
String duplicateerror = lan.getString("setaction", "duplicateerror");
String sourcedesterror = lan.getString("setaction", "sourcedesterror");
String differenttypeerror = lan.getString("setaction", "differenttypeerror");
String setactionerror = lan.getString("setaction", "setactionerror");
String confirmremove = lan.getString("setaction", "confirmremove");
String confirmremoveall = lan.getString("setaction", "confirmremoveall");
boolean profile_booklet = true;
boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>

<%@page import="com.carel.supervisor.base.log.LoggerMgr"%>
<%@page import="com.carel.supervisor.presentation.bo.BSetAction"%>
<INPUT type="hidden" name="maxvariablesitems" id="maxvariablesitems" value="<%=RuleConstants.MAX_VARIABLE_ITEMS%>"/>
<!--page i18n strings BEGIN-->
<INPUT type="hidden" name="nomodactionfromide" id="nomodactionfromide" value="<%=nomodactionfromide%>"/>
<INPUT type="hidden" name="notremoveaction" id="notremoveaction" value="<%=notremoveaction%>"/>
<INPUT type="hidden" name="actionnotremoved" id="actionnotremoved" value="<%=actionnotremoved%>"/>
<INPUT type="hidden" name="forcesetvalue" id="forcesetvalue" value="<%=forcesetvalue%>"/>
<INPUT type="hidden" name="doubleElement" id="doubleElement" value="<%=doubleElement%>"/>
<INPUT type="hidden" name="nullselected" id="nullselected" value="<%=nullselected%>"/>
<INPUT type="hidden" name="maxvariableerror" id="maxvariableerror" value="<%=maxvariableerror%>"/>
<INPUT type="hidden" name="delall" id="delall" value="<%=delall%>"/>
<!-- new -->
<INPUT type="hidden" name="novarconf" id="novarconf" value="<%=novarconf%>"/>
<INPUT type="hidden" name="del" id="del" value="<%=del%>"/>
<INPUT type="hidden" name="deltooltip" id="deltooltip" value="<%=deltooltip%>"/>
<INPUT type="hidden" name="selsourceerror" id="selsourceerror" value="<%=selsourceerror%>"/>
<INPUT type="hidden" name="seldestinationerror" id="seldestinationerror" value="<%=seldestinationerror%>"/>
<INPUT type="hidden" name="duplicateerror" id="duplicateerror" value="<%=duplicateerror%>"/>
<INPUT type="hidden" name="sourcedesterror" id="sourcedesterror" value="<%=sourcedesterror%>"/>
<INPUT type="hidden" name="differenttypeerror" id="differenttypeerror" value="<%=differenttypeerror%>"/>
<INPUT type="hidden" name="setactionerror" id="setactionerror" value="<%=setactionerror%>"/>
<INPUT type="hidden" name="confirmremove" id="confirmremove" value="<%=confirmremove%>"/>
<INPUT type="hidden" name="confirmremoveall" id="confirmremoveall" value="<%=confirmremoveall%>"/>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>
<!--page i18n strings END-->

<!--<FORM id="frm_set_variable" name="frm_set_variable" action="servlet/master;jsessionid=< %=jsession%>" method="post">-->
<!--<INPUT type="hidden" name="iddevice" id="iddevice" value="< %=iddevice%>"/>-->
<!--<INPUT type="hidden" name="param" id="param" value=""/>-->


<table border="0" cellpadding="1" cellspacing="5" width="98%" >
	<tr height="5%">
		<td colspan="3"><p class="tdTitleTable"><%=action%> <%=action_description%></p></td>
	</tr>
	<tr height="*" valign="top">
		<TD width="49%" valign="top">
			<FIELDSET class="source_fieldset" style="width: 100%;padding: 4px;height: 100%">
				<LEGEND class="standardTxt" style="padding: 4px;"><%=destination %></LEGEND>
				<table width="100%" class="table" cellpadding="2" cellspacing="1">
				<%--  
					<tr>
						<TD class='standardTxt'><%=device%>:</TD>
						<td width="95%">
							<SELECT id='device_dest' name='device_dest' style="width: 100%;"
							onchange="reload_actions(this,'variable_combo_dest','rw',0);">
								<OPTION><%=selectdevice %></OPTION>
							<% for (int i=0;i<devices.size();i++){
								deviceBean = devices.getDevice(devicesids[i]);
								%>
								<OPTION value='<%=deviceBean.getIddevice()%>'><%=deviceBean.getDescription()%></OPTION>
							<% } %>
							</SELECT>
						</td>
					</tr>
					--%>
					<tr class='standardTxt'>
									<td width="25%" align="left">
										<input  onclick='getDevList(this);' type='radio' id='d' name='devmodl' checked="checked" /><%=devicesStr%>
									</td>
									<td width="25%" align="right" style="white-space:nowrap">
										<input  onclick='emptyComponents(this);' type='radio' id='m' name='devmodl' /><%=devmodels%>
									</td>
									<td width="50%" align="left">
										<select disabled onchange='changeDevMdl(this);' class='standardTxt' id='model' name='model' disabled style="width:100%;" />
		<!--								< %=combodev.toString()%>-->
										</select>
									</td>
					</tr>
					<tr >
					<td class='standardTxt' width="100%" valign="top"  colspan="3">
									<div id="div_dev">
										<select  id="devLst" size="10" onclick="dblClickDevList(this);" ondblclick="dblClickDevList(this);"   class='selectB' style='width:100%;'>
											<% for (int i=0;i<devices.size();i++){
												deviceBean = devices.getDevice(devicesids[i]);
												%>
												<OPTION value='<%=deviceBean.getIddevice()%>' class="<%=i%2==0?"Row1":"Row2" %>" id="dev<%=deviceBean.getIddevice() %>"><%=deviceBean.getDescription()%></OPTION>
											<% } %>
										</select>
									</div>
									</td>
					</tr>			
					<tr><td class='standardTxt' style='height:20px;'><%=variables %></td></tr>
					<TR>
						<TD width="100%" colspan="3">
							<div id="div_var">
								<SELECT id='variable_combo_dest' name='variable_combo_dest' size='10' style='OVERFLOW:auto;width:100%;'>
								</SELECT>
							</div>
						</TD>
					</TR>											
				</table>
			</fieldset>
		</td>
		<td width="1%">&nbsp;</td>
		<TD width="49%" valign="top" >
			<FIELDSET class="source_fieldset" style="width: 100%;height: 100%;padding: 4px;">
				<LEGEND id="srcVarLegend" class="standardTxt" style="padding: 4px;"><%=source %></LEGEND>
				<TABLE width="100%" cellpadding="0" cellspacing="2">
					<tr>
						<td width="100%">
							<table width="100%" class="table">
								<tr>
									<td class='standardTxt' valign="middle"><%=constant %>: 
										<input id="value_source" name="value_source" 
										class=<%=(OnScreenKey?"keyboardInput":"standardTxt")%> type="text" maxlength="5" 
										style="width:20%; text-align: right;" onblur="checkOnlyAnalogOnBlur(this);" 
										onkeydown="checkOnlyAnalog(this,event);makenoselection();"/>
									</td>
								</tr>
								<script type="text/javascript">
									if (navigator.userAgent.toLowerCase().indexOf('firefox') > -1) {
										document.getElementById("value_source").style.height=26;
									}
								</script>
							</table>
						</td>
					</tr>
					<TR>
						<td>
							<table width="100%" class="table">
								<tr>
									<TD class='standardTxt'><%=device%>:</TD>
									<td width="95%">
										<SELECT id='device_source' name='device_source' style="width: 100%;"
										onchange="reload_actions(this,'variable_combo_source','-',0);">
											<OPTION><%=selectdevice %></OPTION>
										<% for (int i=0;i<devices.size();i++){
											deviceBean = devices.getDevice(devicesids[i]);
											%>
											<OPTION value='<%=deviceBean.getIddevice()%>'><%=deviceBean.getDescription()%></OPTION>
										<% } %>
										</SELECT>
									</td>
								</tr>
								<tr height="30px">
									<td class='standardTxt'>
										<%=variables %>
									</td>
								</tr>
								<TR>
									<TD width="100%" colspan="2">
										<SELECT id='variable_combo_source' name='variable_combo_source' size='18' class="selectB" style='OVERFLOW:auto;width:100%;' onclick="makeempty();" ondblclick="add_variable();">
										</SELECT>
									</TD>
								</TR>
							</table>
						</td>
					</tr>
				</table>
			</FIELDSET>
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<FIELDSET class="dest_fieldset" style="width: 100%;padding: 4px;">
				<LEGEND class="standardTxt" style="padding: 4px;"><%=configured %></LEGEND>
				<form id="frm_set_variable" name="frm_set_variable" action="servlet/master;jsessionid=<%=jsession%>" method="post">
					<INPUT type="hidden" name="actioncode" id="actioncode" value="<%=actioncode%>"/>
					<INPUT type="hidden" name="cmd" id="cmd" value="save_setvar_action"/>
					<INPUT type="hidden" name="param" id="param" value=""/>
					<INPUT type="hidden" name="sched" id="sched" value="<%=issched %>"/>
					<INPUT type="hidden" name="action_description" id="action_description" value="<%=action_description%>"/>
					<table align='center' id="setactionvariables" border="0" cellpadding="1" cellspacing="1" width="98%" class="table">
						<tr class="Row1">
							<td width="24%" class="th" ><%=devicesStr%></td>
							<td width="24%" class="th"><%=descdest %></td>
							<td width="24%" class="th" ><%=devicesStr%></td>
							<td width="24%" class="th"><%=descsrc %></td>
							<td width="4%" class="th" onclick="delall()" style="text-align: center;cursor: pointer;" title="<%=deltooltip %>">
								<img src="images/dbllistbox/delete_on.png" alt="<%=delall %>" />
							</td>
						</tr>
						<%if(param==null || param.equals("")) { %>
						<tr class="Row1" id="nonerow">
							<td colspan="6" class="standardTxt" style="text-align: center"><%=novarconf %></td>
						</tr>
						<%} else {
							String[] paramArray = param.split(";");
							Integer source_id;
							Integer dest_id;
							String source_desc;
							String dest_desc;
							VarphyBean source_var;
							VarphyBean dest_var;
							for (int i=0;i<paramArray.length;i++) {
								try{
									
									

									String[] singleparam = paramArray[i].split("=");  //sub_param[0]=Id variabile sub_param[1]=valore settato
									dest_id = new Integer(singleparam[0]);
									dest_var = VarphyBeanList.retrieveVarById(idsite, dest_id, language);
									dest_desc = dest_var.getShortDescription();
									if (dest_desc!=null)
									{%>
							<tr class="Row<%=i%2==0?1:2 %>" >
								<td width="24%" class="standardTxt" id="<%=(""+dest_var.getDevice()+"_"+ dest_id)%>" ><%=devices.getDevice(dest_var.getDevice()).getDescription()%></td>
								<td width="24%" class="standardTxt"><%=dest_desc%></td>
								
								<% 
									if(singleparam[1].startsWith("id"))
									{
										source_id = new Integer(singleparam[1].substring(2));
										source_var = VarphyBeanList.retrieveVarById(idsite, source_id, language);
										%>
										<td width="24%" class="standardTxt" id="<%=source_var.getDevice()+"_"+singleparam[1].substring(2)%>" ><%= devices.getDevice(source_var.getDevice()).getDescription()%></td>
										<td width="24%" class="standardTxt"><%=source_var.getShortDescription()%></td>
								<%}else{%>
								<td width="24%" class="standardTxt" id="-1"></td>
							    <td width="24%" class="standardTxt" ><%=singleparam[1] %></td>
								<%} %>
								
								<td width="4%" class="standardTxt" onclick="removerow(this,true)" 
									style="text-align: center;cursor: pointer;" title="<%=deltooltip %>">
									<img src="images/actions/removesmall_on_black.png" alt="<%=del %>" />
								</td>
							</tr>								
									<%
									}
									
								} catch(Exception e)
								{
									LoggerMgr.getLogger(BSetAction.class).error(e);
								}
							}
						} %>
					</table>
				</form>
			</fieldset>
		</td>
	</tr>
</table>
