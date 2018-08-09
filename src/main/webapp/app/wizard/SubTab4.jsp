<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList"
import="com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr"
import="com.carel.supervisor.presentation.bo.BWizard"
%>

<%
	UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = userSession.getCurrentUserTransaction();
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	String language = userSession.getLanguage();
	String jsession = userSession.getSessionId();
	LangService lan = LangMgr.getInstance().getLangService(language); 
	
	DeviceInfoList deviceInfoList = (DeviceInfoList) DataConfigMgr.getInstance().getConfig("cfdev");
	int screen_h = userSession.getScreenHeight();
	String setparameterscomment = lan.getString("wizard","setparameterscomment");
%>

<script type="text/javascript" src="scripts/app/../arch/util.js"></script>
<script type="text/javascript" src="scripts/app/../arch/actb/common.js"></script>

<input type="hidden" id="isrestart" value="true">
<input type='hidden' id='virtkeyboard' value='<%=OnScreenKey?"on":"off" %>'/>
<input type='hidden' id='s_maxval' value="<%=lan.getString("dtlview","s_maxval")%>"/>
<input type='hidden' id='s_minval' value="<%=lan.getString("dtlview","s_minval")%>"/>
<input type='hidden' id='careldefault' value="<%=lan.getString("wizard","careldefault")%>"/>
<input type='hidden' id='varcode' value="<%=lan.getString("wizard","varcode")%>"/>
<input type='hidden' id='value' value="<%=lan.getString("wizard","value")%>"/>
<input type='hidden' id='close' value="<%=lan.getString("wizard","close")%>"/>
<input type='hidden' id='details' value="<%=lan.getString("wizard","details")%>"/>
<input type='hidden' id='setting' value="<%=lan.getString("wizard","setting")%>"/>
<input type='hidden' id='description' value="<%=lan.getString("wizard","description")%>"/>
<input type='hidden' id='scr_h' value="<%=screen_h%>"/>

<table  width='100%' border=0>
<tr>
	<td class="standardTxt">
		<p class="standardTxt"><%=setparameterscomment %></p>
	</td>
</tr>
<%
if(deviceInfoList.size()>0 && !(deviceInfoList.size() == 1 && deviceInfoList.getByCode("-1.000")!=null)){
%>
<tr>
	<td align="right">
		<table width="20%">
			<tr>
				<td id="tdfirstpage" class="Menu_HeadOver"><img src="images/lsw/sxsx_off.png"/></td>
				<td id="tdprepage" class="Menu_HeadOver"><img src="images/lsw/sx_off.png"/></td>
				<td id="pagestatus"></td>
				<td id="tdnextpage" class="Menu_HeadOver"><img src="images/lsw/dx_off.png"/></td>
				<td id="tdlastpage" class="Menu_HeadOver"><img src="images/lsw/dxdx_off.png"/></td>
			</tr>
		</table>
	</td>
</tr>
<tr id="deviceviewloading">
	<td style="text-align: center">
		<table width="100%" height="100%" cellspacing="1" cellpadding="0" class="table">
			<tr>
			<td style="text-align: center;">
			Loading <img src="images/ajax-loader_white.gif">
			</td>
			</tr>
		</table>
	</td>
</tr>	
<%} %>
<tr>
	<td>
		<div id="param_div" style="width:100%;height:400px; overflow:auto;">
			<table  width='100%' id='tablecontainer' cellpadding=2 cellspacing=12 border=0>
			</table>
		</div>
	</td>
</tr>
</table>