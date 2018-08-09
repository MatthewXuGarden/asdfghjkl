<%@ page language="java" pageEncoding="UTF-8"
import="com.carel.supervisor.dataaccess.language.LangService" 
import="com.carel.supervisor.presentation.session.UserSession" 
import="com.carel.supervisor.presentation.helper.ServletHelper" 
import="com.carel.supervisor.dataaccess.language.LangMgr" 
import="com.carel.supervisor.director.DirectorMgr" 
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.bean.*"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	int idsite= sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();

	boolean isDebugSessionOn = DirectorMgr.getInstance().isDebugSessionOn();
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	
	// Per generazione combo in dettaglio
	GroupListBean groups = sessionUser.getGroup();
	int[] gids = groups.getIds();
	DeviceStructureList deviceStructureList = groups.getDeviceStructureList(); 
	int[] ids = deviceStructureList.retrieveIdsByGroupsId(gids);
	sessionUser.getTransaction().setIdDevices(ids);
	sessionUser.getTransaction().setIdDevicesCombo(ids);
%>
      
<input type='hidden' id='isDebugSessionOn' value="<%=isDebugSessionOn %>"/>
<input type='hidden' id='txtconfirmstart' value="<%=lan.getString("debug","confirmstart") %>"/>
<input type='hidden' id='txtconfirmstop' value="<%=lan.getString("debug","confirmstop") %>"/>
<input type='hidden' id='txtconfirmreset' value="<%=lan.getString("debug","confirmreset") %>"/>
<input type='hidden' id='txtnodeviceselect' value="<%=lan.getString("debug","nodevselect") %>"/>
<input type='hidden' id='debugstarttime' value="<%=lan.getString("debug","debugstarttime") %>"/>
<input type='hidden' id='avgtime' value="<%=lan.getString("debug","avgtime")%>"/>
<input type="hidden" id="subtab" value="485">
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<input type='hidden' id='OnScreenKey' value='<%=OnScreenKey %>'/>

<table  width='100%' border=0>
	<tr>
		<td align='center' valign='top'>
			<table border="0" width="100%" cellspacing="1" cellpadding="1">
				<tr>
					<td width="65%"><div id="avg_title" class="standardTxt"></div>
	 							<table border=0 id="avg_polling_time" cellpadding=1 cellspacing=1>
	 							</table>
	 				</td>
					<td width="15%" id="debugTime" class="standardTxt" valign="top">&nbsp;</td>
					<td align="right" class="standardTxt" valign="top" noWrap style="padding-right:32px">
							<%=lan.getString("debug","timeout") %>:
							<select id="timeoutSel">
								<option value="600">10 <%=lan.getString("debug","minutes") %></option>
								<option value="1800">30 <%=lan.getString("debug","minutes") %></option>
								<option value="3600" selected="selected">1 <%=lan.getString("debug","hour") %></option>
								<option value="21600">6 <%=lan.getString("debug","hours") %></option>
								<option value="43200">12 <%=lan.getString("debug","hours") %></option>
								<option value="86400">1 <%=lan.getString("debug","day") %></option>
								<option value="172800">2 <%=lan.getString("debug","days") %></option>
							</select>
							<%=lan.getString("debug","refresh") %>:
							<select id="refreshSel"> 
								<option value="5">5 <%=lan.getString("debug","seconds") %></option>
								<option value="10" selected="selected">10 <%=lan.getString("debug","seconds") %></option>
								<option value="20">20 <%=lan.getString("debug","seconds") %></option>
								<option value="30">30 <%=lan.getString("debug","seconds") %></option>
								<option value="60">1 <%=lan.getString("debug","minutes") %></option>
							</select>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td valign='top'>
			<table border="0" class='sortable' cellspacing='1px' cellpadding='1px' id="sTab"  style="display:none;">
			<tr height="25">
				<td class=th align="center"><div style='WIDTH: 90px; HEIGHT: 18px; OVERFLOW: hidden; CURSOR: pointer'><b><%=lan.getString("debug","addr") %></b></div></td>
				<td class=th align="center"><div style='WIDTH: 150px; HEIGHT: 18px; OVERFLOW: hidden; CURSOR: pointer'><b><%=lan.getString("debug","desctype") %></b></div></td>
				<td class=th align="center"><div style='WIDTH: 150px; HEIGHT: 18px; OVERFLOW: hidden; CURSOR: pointer'><b><%=lan.getString("debug","desc") %></b></div></td>
				<td class=th align="center"><div style='WIDTH: 100px; HEIGHT: 18px; OVERFLOW: hidden; CURSOR: pointer'><b><%=lan.getString("debug","appcode") %></b></div></td>
				<td class=th align="center"><div style='WIDTH: 100px; HEIGHT: 18px; OVERFLOW: hidden; CURSOR: pointer'><b><%=lan.getString("debug","fwrelease") %></b></div></td>
				<td class=th align="center"><div style='WIDTH: 100px; HEIGHT: 18px; OVERFLOW: hidden; CURSOR: pointer'><b><%=lan.getString("debug","port") %></b></div></td>
				<td class=th align="center"><div style='WIDTH: 100px; HEIGHT: 18px; OVERFLOW: hidden; CURSOR: pointer'><b><%=lan.getString("debug","noanswercnt") %></b></div></td>
				<td class=th align="center"><div style='WIDTH: 100px; HEIGHT: 18px; OVERFLOW: hidden; CURSOR: pointer'><b><%=lan.getString("debug","errchkcnt") %></b></div></td>
				<td class=th align="center"><div style='WIDTH: 100px; HEIGHT: 18px; OVERFLOW: hidden; CURSOR: pointer'><b><%=lan.getString("debug","nooffline") %></b></div></td>	
			</tr> 
			</table> 
		</td>
	</tr>
</table>
