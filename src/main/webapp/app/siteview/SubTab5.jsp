<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page language="java" isErrorPage="true" 
import="com.carel.supervisor.presentation.session.* "
import="com.carel.supervisor.presentation.helper.*"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.bean.DeviceListBean"
import="com.carel.supervisor.presentation.bean.DeviceBean"
import="com.carel.supervisor.presentation.bo.master.IMaster"
import="com.carel.supervisor.presentation.bean.ProfileBean"
import="com.carel.supervisor.presentation.bo.BSiteView"
import="com.carel.supervisor.dispatcher.DispatcherMgr"
%>
<%
String path = request.getContextPath();
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
String jsession = request.getSession().getId();
String language =  sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);
int idsite = sessionUser.getIdSite();

String pv_type = BaseConfig.getProductInfo("type");

DescriptionTree description_tree=new DescriptionTree();
description_tree.setScreenH(sessionUser.getScreenHeight());
description_tree.setScreenW(sessionUser.getScreenWidth());
int permission = sessionUser.isButtonActive("siteview", "tab6name","Save")?2:1;
boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
String cssVK = (OnScreenKey ? ','+VirtualKeyboard.getInstance().getCssClass() : "");	

String htmlFunctTable = description_tree.getHTMLFunctTable("functTable",language,sessionUser,OnScreenKey,975,320);


%>



<%@page import="com.carel.supervisor.presentation.ldap.FunctionalityList"%>
<%@page import="com.carel.supervisor.presentation.menu.DescriptionTree"%><input type="hidden" id="permission" name="permission" value="<%=permission%>"/>

<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>
<input type="hidden" id="vk_state" value="<%= (OnScreenKey) ? '1' : '0' %>" />

<p class='standardTxt' ><%=lang.getString("siteview","menu_desc_title")%></p>

<form id="frm_menu_desc" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type='hidden' name='cmd' value='menu_desc' />
<input type='hidden' name='d_section5_val' id='d_section5_val' value='<%=lang.getString("menu","section5")%>' />
<input type='hidden' name='d_section2_val' id='d_section2_val' value='<%=lang.getString("menu","section2")%>' />
<input type='hidden' name='d_section3_val' id='d_section3_val' value='<%=lang.getString("menu","section3")%>' />
<input type='hidden' name='d_section4_val' id='d_section4_val' value='<%=lang.getString("menu","section4")%>' />
<input type='hidden' name='d_section7_val' id='d_section7_val' value='<%=lang.getString("menu","section7")%>' />
<input type='hidden' name='d_section8_val' id='d_section8_val' value='<%=lang.getString("menu","section8")%>' />
<input type='hidden' name='d_section9_val' id='d_section9_val' value='<%=lang.getString("menu","section9")%>' />

<fieldset style='width:97%'>
<legend class='standardTxt'><%=lang.getString("siteview","main_menu_descr")%></legend>
<table width='80%' align='center' border=0>
	<tr>
		<td colspan=2 align='center' width="14%"><img src='images/menusx/new/map1.png' /></td>
		<td colspan=2 align='center' width="14%"><img src='images/menusx/new/allarm-event1.png' /></td>
		<td colspan=2 align='center' width="14%"><img src='images/menusx/new/report1.png' /></td>
		<td colspan=2 align='center' width="16%"><img src='images/menusx/new/configuration1.png' /></td>
		<td colspan=2 align='center' width="14%"><img src='images/menusx/new/calendar1.png' /></td>
		<td colspan=2 align='center' width="14%"><img src='images/menusx/new/function1.png' /></td>
		<td colspan=2 align='center' width="14%"><img src='images/menusx/new/eco-hvac1.png' /></td>
	</tr>
	<tr>
		<td align='right' width="1%"><img style='cursor:pointer' onclick="d_edit('d_section5',true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>" src='images/actions/Edit_on_black.png' /></td>
		<td class='standardTxt' id='d_section5' onclick='save_last(this)' ><b><%=lang.getString("menu","section5")%></b></td>
		
		<td align='right' width="1%"><img style='cursor:pointer' onclick="d_edit('d_section2',true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>" src='images/actions/Edit_on_black.png' /></td>
		<td class='standardTxt' id='d_section2' onclick='save_last(this)' ondblclick='d_edit(this,true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>'><b><%=lang.getString("menu","section2")%></b></td>
		
		<td align='right' width="1%"><img style='cursor:pointer' onclick="d_edit('d_section3',true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>" src='images/actions/Edit_on_black.png' /></td>
		<td class='standardTxt' id='d_section3' onclick='save_last(this)' ondblclick='d_edit(this,true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>'><b><%=lang.getString("menu","section3")%></b></td>
		
		<td align='right' width="1%"><img style='cursor:pointer' onclick="d_edit('d_section4',true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>" src='images/actions/Edit_on_black.png' /></td>
		<td class='standardTxt' id='d_section4' onclick='save_last(this)' ondblclick='d_edit(this,true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>'><b><%=lang.getString("menu","section4")%></b></td>
		
		<td align='right' width="1%"><img style='cursor:pointer' onclick="d_edit('d_section7',true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>" src='images/actions/Edit_on_black.png' /></td>
		<td class='standardTxt' id='d_section7' onclick='save_last(this)' ondblclick='d_edit(this,true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>'><b><%=lang.getString("menu","section7")%></b></td>
		
		<td align='right' width="1%"><img style='cursor:pointer' onclick="d_edit('d_section8',true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>" src='images/actions/Edit_on_black.png' /></td>
		<td class='standardTxt' id='d_section8' onclick='save_last(this)' ondblclick='d_edit(this,true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>'><b><%=lang.getString("menu","section8")%></b></td>

		<td align='right' width="1%"><img style='cursor:pointer' onclick="d_edit('d_section9',true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>" src='images/actions/Edit_on_black.png' /></td>
		<td class='standardTxt' id='d_section9' onclick='save_last(this)' ondblclick='d_edit(this,true);<%=(OnScreenKey?"buildKeyboardInputs();":"")%>'><b><%=lang.getString("menu","section9")%></b></td>
	</tr>
</table>
</fieldset>
<br></br>

<%=htmlFunctTable%>
 
</form>
