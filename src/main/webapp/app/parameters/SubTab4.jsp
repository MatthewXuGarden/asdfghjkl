<%@ page language="java"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr"
import="com.carel.supervisor.presentation.rule.FaxListBox"
import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersNotificationList"
import="com.carel.supervisor.base.config.ProductInfoMgr"
import="com.carel.supervisor.presentation.dbllistbox.DblListBox"
import="java.util.ArrayList"
import="com.carel.supervisor.presentation.dbllistbox.ListBoxElement"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
LangService langmgr = LangMgr.getInstance().getLangService(sessionUser.getLanguage());
LangService l=langmgr;
String jsession = request.getSession().getId();
String doubleElement = langmgr.getString("dbllistbox","doublelement");


boolean enablenotify = ParametersMgr.getParametersCFG().getEnablenotification();

String listOptions = "";
String listOptionsSave = "";

listOptions = ParametersNotificationList.getOptionListNotConf();
listOptionsSave = ParametersNotificationList.getOptionListConf();


String actionIncon = sessionUser.getPropertyAndRemove("actionIconG");

 
DblListBox profili = new DblListBox(ParametersMgr.getParametersCFG().getAllProfileListBox(),
		 							 ParametersMgr.getParametersCFG().getAuthProfileListBox()
		 							,false,true,true,null,true,50);
profili.setHeaderTable1(langmgr.getString("parameters","allprofiles"));
profili.setHeaderTable2(langmgr.getString("parameters","authprofiles"));

profili.setIdlistbox("profiliauth");
int width=880;
profili.setScreenW(width+20);
profili.setScreenH(400);

profili.setWidthListBox(470);
profili.setWidth_select(400);
profili.setHeight(400);

ParametersCFG pcfg = ParametersMgr.getParametersCFG();
boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>


<%@page import="com.carel.supervisor.plugin.parameters.ParametersMgr"%>

<%@page import="com.carel.supervisor.plugin.parameters.dataaccess.ParametersCFG"%><input type="hidden" id="actionincon" value="<%=actionIncon%>"/>
<input type="hidden" name="doubleElement" id="doubleElement" value="<%=doubleElement%>"/>
<input type="hidden" id="userkeybemptyerrormsg" value="<%=l.getString("parameters","userkeybemptyerrormsg") %>">
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>

<p class='standardTxt'><%=langmgr.getString("parameters","notification_comment")%></p>

<form id="param_notify" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<fieldset class="field"  style="width:97%;">
	<legend class="standardTxt"><%=langmgr.getString("parameters","notification_section")%></legend>
			<table class='standardTxt' cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td align="left" width="23%"><%=l.getString("parameters","checkinginterval") %>&nbsp;</td>
					<td>
						<select name="checkinterval" id="checkinterval" >
							<option value="120"  <% if (pcfg.getCheckinterval()==120)  {%>selected <%} %>><%=l.getString("parameters","4ore") %></option>
							<option value="180"  <% if (pcfg.getCheckinterval()==180)  {%>selected <%} %>><%=l.getString("parameters","6ore") %></option>
							<option value="240"  <% if (pcfg.getCheckinterval()==240)  {%>selected <%} %>><%=l.getString("parameters","8ore") %></option>
							<option value="360"  <% if (pcfg.getCheckinterval()==360)  {%>selected <%} %>><%=l.getString("parameters","12ore") %></option>
							<option value="720"  <% if (pcfg.getCheckinterval()==720)  {%>selected <%} %>><%=l.getString("parameters","24ore") %></option>
						</select>
					</td>
				</tr>
				<tr><td>&nbsp;</td><td></td></tr>
				<tr>
					<td align="left"><%=l.getString("parameters","usernametastiera") %>&nbsp;</td>
					<td>
						<input type="text" id="usertastiera" name="usertastiera" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" value="<%=pcfg.getUserTastiera() %>" onkeydown="checkLettNum(this,event);"></input>
					</td>
				</tr>	
			</table>
	</fieldset>
<p class="standardTxt"/>
<fieldset class="field" style="width:97%">		
	<legend class="standardTxt"><%=langmgr.getString("parameters","addresslist")%></legend>
	<input type="hidden" name="CMD" value="update_param_notify"/>
		<table border="0" width="100%" height="180px" cellspacing="1" cellpadding="1">
			<tr height="2%">
				<td colspan="5">
					<table border="0" cellpadding="1" cellspacing="1">
						<tr>
							<td class="standardTxt">
								<input type='checkbox' name="enablednotification" id="enablednotification" onclick="" value="0" 
					        		<%if (enablenotify) {%>checked<%} %> >
					         </td>
			        		<td class="standardTxt"><%=langmgr.getString("parameters","notification_enabled")%></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr height="2%">
				<td colspan="5">
					<table border="0" cellpadding="1" cellspacing="1">
						<tr>
						    <td class="standardTxt"><%=langmgr.getString("parameters","aggregatelabel")%></td>
							<td class="standardTxt">
							    <select  name="aggregatenotification" id="aggregatenotification">
							    	<option value="true"  <%if (ParametersMgr.getParametersCFG().getAggregatenotification()) {%>selected<%} %>> <%=langmgr.getString("parameters","aggregatenotification") %> </option>
							    	<option value="false" <%if (!ParametersMgr.getParametersCFG().getAggregatenotification()) {%>selected<%} %>><%=langmgr.getString("parameters","singlenotification") %> </option>
							    </select>
							             </td>
	
						</tr>
					</table>
				</td>
			</tr>
			<tr valign="top" height="2%">
				<td width="40%" class='th' align='center'><b><%=langmgr.getString("parameters","addressbook")%></b></td>
				<td width="5%"></td>
				<td width="40%" class='th' align='center'><b><%=langmgr.getString("parameters","addresslist")%></b></td>
			</tr>
			<tr valign="top">
				<td width="40%">
					<div id='divCollegeNamesA' style='overflow:auto;' onscroll="GuiOnDivScroll(list1,24);">
						<select class='selectB' multiple size="8" name="list1" id="list1" ondblclick="multipleto2(list1,null);" onscroll='GuiOnSelectFocus(this,24);'>
							<%=listOptions%>
						</select>
					</div>
				</td>
				<td valign="middle" align="center" width="5%">
					<table width="100%" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td align="center">
								<img onclick="multipleto2(list1,null);return false;" src="images/dbllistbox/arrowdx_on.png"/>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td align="center">
								<img onclick="multipleto1(list1,null);return false;" src="images/dbllistbox/arrowsx_on.png"/>
							</td>
						</tr>
					</table>
				</td>
				<td width="40%">
					<div id='divCollegeNamesB' style='overflow:auto;' onscroll="GuiOnDivScroll(list2,24);">
						<select class='selectB' multiple size="8" name="list2" id="list2" ondblclick="multipleto1(list1,null);" onscroll='GuiOnSelectFocus(this,24);'>
							<%=listOptionsSave%>
						</select>
					</div>
				</td>
			</tr>
		</table>
</fieldset>
<p class="standardTxt"/>
<fieldset class="field"  style="width:97%;">
	<legend class="standardTxt"><%=langmgr.getString("parameters","auth_profile_section")%></legend>			
	<table border="0" width="100%" cellspacing="1" height="150px" cellpadding="1">
		<tr valign="top" height="2%">
			<td width="40%" class='th' align='center'><b><%=langmgr.getString("parameters","allprofiles")%></b></td>
			<td width="5%"></td>
			<td width="40%" class='th' align='center'><b><%=langmgr.getString("parameters","authprofiles")%></b></td>
		</tr>
		<tr valign="top">
			<td width="40%">
				<div id='divCollegeNamesA_auth' style='overflow:auto;' onscroll="GuiOnDivScroll(list_auth1,24);">
					<select class='selectB' multiple size="8" name="list_auth1" id="list_auth1" ondblclick="multipleto2(list_auth1,null);" onscroll='GuiOnSelectFocus(this,24);' >
						<%=ParametersMgr.getParametersCFG().getNotAuthProfileListBoxHTML()%>
					</select>
				</div>
			</td>
			<td valign="middle" align="center" width="5%">
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td align="center">
							<img onclick="multipleto2(list_auth1,null);return false;" src="images/dbllistbox/arrowdx_on.png"/>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="center">
							<img onclick="multipleto1(list_auth1,null);return false;" src="images/dbllistbox/arrowsx_on.png"/>
						</td>
					</tr>
				</table>
			</td>
			<td width="40%">
				<div id='divCollegeNamesB_auth' style='overflow:auto;' onscroll="GuiOnDivScroll(list_auth2,24);">
					<select class='selectB' multiple size="8" name="list_auth2" id="list_auth2" ondblclick="multipleto1(list_auth1,null);" onscroll='GuiOnSelectFocus(this,24);'>
						<%=ParametersMgr.getParametersCFG().getAuthProfileListBoxHTML() %>
					</select>
				</div>
			</td>
		</tr>
	</table>			
</fieldset>
	<div id="guiPostDiv" style="visibility:hidden;display:none;"></div>
</form>		
