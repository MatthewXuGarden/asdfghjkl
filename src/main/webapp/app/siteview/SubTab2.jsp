<%@ page language="java"  pageEncoding="UTF-8" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.bo.helper.VarDependencyList"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.presentation.bean.ParamBean"
import="com.carel.supervisor.base.config.*"
import="java.util.*"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	int idsite = sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService lang = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();
	boolean bOnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	String cssVirtualKeyboardClass = (bOnScreenKey ? ' '+VirtualKeyboard.getInstance().getCssClass() : "");


	ParamBean beanParam = (ParamBean)ut.getAttribute("ParamBean");
	Integer idDevMdl = 0;
	Integer idVarGroup = 0;
	if( beanParam != null ) {
		idDevMdl = beanParam.getIdDevMdl();
		idVarGroup = beanParam.getIdVarGroup();
	}
	
	StringBuffer sbDevMdl = new StringBuffer();
	DevMdlBeanList listDevMdl = new DevMdlBeanList();
	DevMdlBean[] abeanDevMdl = listDevMdl.retrieve(idsite, language);
	for(int i = 0; i < abeanDevMdl.length; i++) {
		DevMdlBean beanDevMdl = abeanDevMdl[i];
		if( beanDevMdl.getCode().equals("Internal IO") )
			continue;
		sbDevMdl.append("<option value=\"" + beanDevMdl.getIddevmdl()
			+ (beanDevMdl.getIddevmdl() == idDevMdl ? "\" selected>" : "\">"));
		sbDevMdl.append(beanDevMdl.getDescription());
		sbDevMdl.append("</option>\n");
	}
	
	String strParamGroups = "";
	String strParamTable = "";
	StringBuffer sbPriority = new StringBuffer();
	if( beanParam != null ) {
		strParamGroups = beanParam.getHtmlParamGroups(sessionUser.getScreenWidth());
		strParamTable = beanParam.getHtmlParamTable(sessionUser.getScreenWidth(), sessionUser.getScreenHeight());
		for(int i = ParamBean.DEFAULT_PRIORITY; i <= ParamBean.MAX_PRIORITY; i++) {
			sbPriority.append("<option value='" + i + "'>");
			if( i > ParamBean.DEFAULT_PRIORITY )
				sbPriority.append(String.valueOf(i));
			sbPriority.append("</option>");
		}
	}
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<input id="confirm_all_pri" type="hidden" value="<%=lang.getString("siteview", "confirm_all_pri")%>">
<p class="standardTxt"><%=lang.getString("siteview", "param_pri_desc")%></p>
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>
<input type="hidden" id="vk_state" value="<%= (OnScreenKey) ? '1' : '0' %>" />
<form id="frm_var_pri" name="frm_var_pri" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd">
<input type="hidden" id="idVarGroup" name="idVarGroup" value="<%=idVarGroup%>">
<input type="hidden" id="listVar" name="listVar">
<input type="hidden" id="listPri" name="listPri">
<table class="table">
	<tr>
		<td class="td">
			<%=lang.getString("siteview", "typedev")%>
		</td>
		<td class="td">
			<div id="layer_device" style="position:relative; left:0px; top:10px;">
				<select id="idDevMdl" name="idDevMdl" onchange="onSelectDevMdl(this.value, 0)" style="width:500px;" class="standardTxt">
				<option value="0"></option>
				<%=sbDevMdl.toString()%>
				</select>
			</div>
			<div id="layer_device_search" class="standardTxt layerDeviceSearch">
				<input type='text' class="standardTxt<%=cssVirtualKeyboardClass%>" name="device_search" id='device_search' value="">
			</div>
		</td>
	</tr>
</table>
</form>
<%if( beanParam != null ) {%>
<table>
	<tr>
		<td><%=strParamGroups%></td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td><%=strParamTable%></td>
	</tr>
	<tr>
		<td align="right" class="td">
			<%=lang.getString("siteview", "change_all_pri")%>&nbsp;
			<select onChange='onSelectPriority(this.value)'>
				<%=sbPriority.toString()%>
			</select>
		</td>
</table>
<%}%>
