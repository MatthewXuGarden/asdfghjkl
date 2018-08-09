<%@ page language="java" pageEncoding="UTF-8"
import="com.carel.supervisor.dataaccess.language.LangService" 
import="com.carel.supervisor.presentation.session.UserSession" 
import="com.carel.supervisor.presentation.helper.ServletHelper" 
import="com.carel.supervisor.dataaccess.language.LangMgr" 
import="com.carel.supervisor.director.DirectorMgr" 
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	int idsite= sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();

	boolean isDebugSessionOn = DirectorMgr.getInstance().isDebugSessionOn();
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<style>
	.th{position:relative;}
	.nopadding{cursor:pointer;padding:0px;width:20px;}
	.hideicon{
	background-image:url(images/icon/showhide.png);
	background-position: 20px 0px;
	WIDTH: 20px;
	height: 20px;
	CURSOR: pointer;
	position:absolute;
	left:15px;
	top:0px;
	}
	.showicon{width:20px;height:45px;
	background-image:url(images/icon/showhide.png);
	background-repeat: no-repeat;
	background-position: 0px 0px;}
	.titleDiv{
	HEIGHT: 25px; OVERFLOW: hidden; CURSOR: pointer;padding-top:20px;
	}
</style>
<input type='hidden' id='isDebugSessionOn' value="<%=isDebugSessionOn %>"/>
<input type='hidden' id='txtconfirmstart' value="<%=lan.getString("debug","confirmstart") %>"/>
<input type='hidden' id='txtconfirmstop' value="<%=lan.getString("debug","confirmstop") %>"/>
<input type='hidden' id='txtconfirmreset' value="<%=lan.getString("debug","confirmreset") %>"/>
<input type='hidden' id='txtnodeviceselect' value="<%=lan.getString("debug","nodevselect") %>"/>
<input type='hidden' id='debugstarttime' value="<%=lan.getString("debug","debugstarttime") %>"/>
<input type="hidden" id="subtab" value="thermo">
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<input type='hidden' id='OnScreenKey' value='<%=OnScreenKey %>'/>

<table  width='100%' height='100%' border=0>
	<tr height="20px">
		<td align='center' valign='top'>
			<TABLE border="0" width="100%" cellspacing="1" cellpadding="1">
				<TR>
					<td id="debugTime" class="standardTxt">&nbsp;</td>
					<td align="right" class="standardTxt" noWrap style="padding-right:32px">
							<%=lan.getString("debug","refresh") %>:
							<select id="refreshSel"> 
								<option value="5">5 <%=lan.getString("debug","seconds") %></option>
								<option value="10" selected="selected">10 <%=lan.getString("debug","seconds") %></option>
								<option value="20">20 <%=lan.getString("debug","seconds") %></option>
								<option value="30">30 <%=lan.getString("debug","seconds") %></option>
								<option value="60">1 <%=lan.getString("debug","minutes") %></option>
							</select>
					</td>
				</TR>
			</TABLE>
		</td>
	</tr>
	<tr>
		<td valign='top'>
			<TABLE border="0" class='sortable' cellspacing='1px' cellpadding='1px' id="sTab"  style="display:none;">
			<TR height="45">
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","addr") %></b></DIV></TD>
				<TD class=th align="center"><DIV style='WIDTH: 160px;' class="titleDiv"><b><%=lan.getString("debug","desctype") %></b></DIV></TD>
				<td class=th align="center"><div style='WIDTH: 160px;' class="titleDiv"><b><%=lan.getString("debug","desc") %></b></div></td>
				<TD class="th nopadding" align="center" style="display:none" title="<%=lan.getString("debug","st")%>"><div class="showicon" columns="4" ></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","st")%></b></DIV><div class="hideicon" columns="4"></div></TD>
				<TD class="th nopadding" align="center" style="display:none" title="<%=lan.getString("debug","treg")%>"><div class="showicon" columns="6,7"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","treg")%></b></DIV><div class="hideicon" columns="6,7"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","difftreg_st")%></b></DIV></TD>
				<TD class="th nopadding" align="center" style="display:none" title="<%=lan.getString("debug","toff")%>"><div class="showicon" columns="9,10,11"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","toff")%></b></DIV><div class="hideicon" columns="9,10,11"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","ton")%></b></DIV></TD>	
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","diffton_toff")%></b></DIV></TD>
				<TD class="th nopadding" align="center" style="display:none" title="<%=lan.getString("debug","tdef")%>"><div class="showicon" columns="13,14"></div></TD>	
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","tdef")%></b></DIV><div class="hideicon" columns="13,14"></div></TD>	
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","difftoff_tdef")%></b></DIV></TD>
				<TD class="th nopadding" align="center" style="display:none" title="<%=lan.getString("debug","tsat")%>"><div class="showicon" columns="16,17,18,19,20,21"></div></TD>	
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","tsat")%></b></DIV><div class="hideicon" columns="16,17,18,19,20,21"></div></TD>	
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","tasp")%></b></DIV></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","sh")%></b></DIV></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","shset")%></b></DIV></TD>	
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","diffsh_shset")%></b></DIV></TD>	
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b>%<%=lan.getString("debug","valv")%></b></DIV></TD>
				<TD class="th nopadding" align="center" style="display:none" title="<%=lan.getString("debug","req")%>"><div class="showicon" columns="23"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","req")%></b></DIV><div class="hideicon" columns="23"></div></TD>
				<TD class="th nopadding" align="center" style="display:none" title="<%=lan.getString("debug","defr")%>"><div class="showicon" columns="25"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","defr")%></b></DIV><div class="hideicon" columns="25"></div></TD>
				<TD class="th nopadding" align="center" style="display:none" title="<%=lan.getString("debug","cop")%>"><div class="showicon" columns="27,28,29"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","cop")%></b></DIV><div class="hideicon" columns="27,28,29"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","cooling")%></b></DIV></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","consumption")%></b></DIV></TD>
				<TD class="th nopadding" align="center" style="display:none" title="<%=lan.getString("debug","th2o_in")%>"><div class="showicon" columns="31,32,33"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","th2o_in")%></b></DIV><div class="hideicon" columns="31,32,33"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","th2o_out")%></b></DIV></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","h2o_diff")%></b></DIV></TD>
				<TD class="th nopadding" align="center" style="display:none" title="<%=lan.getString("debug","comp_speed")%>"><div class="showicon" columns="35"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","comp_speed")%></b></DIV><div class="hideicon" columns="35"></div></TD>
				<TD class="th nopadding" align="center" style="display:none" title="<%=lan.getString("debug","liq_inj")%>"><div class="showicon" columns="37"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","liq_inj")%></b></DIV><div class="hideicon" columns="37"></div></TD>
				<TD class="th nopadding" align="center" style="display:none" title="<%=lan.getString("debug","envelope")%>"><div class="showicon" columns="39"></div></TD>
				<TD class=th align="center"><DIV style='WIDTH: 40px;' class="titleDiv"><b><%=lan.getString("debug","envelope")%></b></DIV><div class="hideicon" columns="39"></div></TD>		
			</TR>
			</TABLE>
		</td>
	</tr>
</table>
