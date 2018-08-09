<%@ page language="java" pageEncoding="UTF-8"
import="com.carel.supervisor.dataaccess.language.LangService" 
import="com.carel.supervisor.presentation.session.UserSession" 
import="com.carel.supervisor.presentation.helper.ServletHelper" 
import="com.carel.supervisor.dataaccess.language.LangMgr" 
import="com.carel.supervisor.director.DirectorMgr" 
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.base.config.ProductInfoMgr"
import="com.carel.supervisor.presentation.bean.MbMasterDebugBean"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.bean.FileDialogBean"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	int idsite= sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService lang = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();

	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	
	String strDebug = ProductInfoMgr.getInstance().getProductInfo().get("modbus_debug");
	String strDigVarGap = ProductInfoMgr.getInstance().getProductInfo().get("modbus_digvar_gap");
	String strNumVarGap = ProductInfoMgr.getInstance().getProductInfo().get("modbus_numvar_gap");
	String strLVarCycle = ProductInfoMgr.getInstance().getProductInfo().get("modbus_lvar_cycle");
	String strHDevThreshold = ProductInfoMgr.getInstance().getProductInfo().get("modbus_hdev_threshold");
	MbMasterDebugBean beanDebug = new MbMasterDebugBean(sessionUser);	
%>
<input type="hidden" id="remoteFolder" name="remoteFolder" value="<%=BaseConfig.getCarelPath() + "PvPro\\TempExports\\"%>">
      
<%=(new FileDialogBean(request)).renderFileDialog()%>

<form id="frm_set_debug_level" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd">
<input type="hidden" id="exp" name="exp">
<table class="standardTxt">
<tr>
	<td><%=lang.getString("debug", "mb_debug_level")%></td>
	<td><select id="debug_level" name="debug_level">
		<option value="0" <%="0".equals(strDebug) ? "selected" : ""%>><%=lang.getString("debug", "mb_debug_0")%></option>
		<option value="1" <%="1".equals(strDebug) ? "selected" : ""%>><%=lang.getString("debug", "mb_debug_1")%></option>
		<option value="2" <%="2".equals(strDebug) ? "selected" : ""%>><%=lang.getString("debug", "mb_debug_2")%></option>
		<option value="3" <%="3".equals(strDebug) ? "selected" : ""%>><%=lang.getString("debug", "mb_debug_3")%></option>
		</select>
	</td>
	<td style="width:10px;">&nbsp;</td>
	<td style="display:none"><%=lang.getString("debug", "mb_digvar_gap")%></td>
	<td style="display:none"><select id="digvar_gap" name="digvar_gap">
		<option value="0" <%="0".equals(strDigVarGap) ? "selected" : ""%>>0</option>
		<option value="8" <%="8".equals(strDigVarGap) ? "selected" : ""%>>8</option>
		<option value="16" <%="16".equals(strDigVarGap) ? "selected" : ""%>>16</option>
		<option value="24" <%="24".equals(strDigVarGap) ? "selected" : ""%>>24</option>
		<option value="32" <%="32".equals(strDigVarGap) ? "selected" : ""%>>32</option>
		</select>
	</td>
	<td style="width:10px;">&nbsp;</td>
	<td style="display:none"><%=lang.getString("debug", "mb_numvar_gap")%></td>
	<td style="display:none"><select id="numvar_gap" name="numvar_gap">
		<option value="0" <%="0".equals(strNumVarGap) ? "selected" : ""%>>0</option>
		<option value="1" <%="1".equals(strNumVarGap) ? "selected" : ""%>>1</option>
		<option value="2" <%="2".equals(strNumVarGap) ? "selected" : ""%>>2</option>
		<option value="3" <%="3".equals(strNumVarGap) ? "selected" : ""%>>3</option>
		<option value="4" <%="4".equals(strNumVarGap) ? "selected" : ""%>>4</option>
		</select>
	</td>
	<td style="width:10px;">&nbsp;</td>
	<td style="display:none"><%=lang.getString("debug", "mb_lvar_cycle")%></td>
	<td style="display:none"><select id="lvar_cycle" name="lvar_cycle">
		<option value="1" <%="1".equals(strLVarCycle) ? "selected" : ""%>>1</option>
		<option value="2" <%="2".equals(strLVarCycle) ? "selected" : ""%>>2</option>
		<option value="3" <%="3".equals(strLVarCycle) ? "selected" : ""%>>3</option>
		<option value="4" <%="4".equals(strLVarCycle) ? "selected" : ""%>>4</option>
		<option value="5" <%="5".equals(strLVarCycle) ? "selected" : ""%>>5</option>
		</select>
	</td>
	<td style="width:10px;">&nbsp;</td>
	<td style="display:none"><%=lang.getString("debug", "mb_hdev_threshold")%></td>
	<td style="display:none"><select id="hdev_threshold" name="hdev_threshold">
		<option value="10" <%="10".equals(strHDevThreshold) ? "selected" : ""%>>10</option>
		<option value="20" <%="20".equals(strHDevThreshold) ? "selected" : ""%>>20</option>
		<option value="30" <%="30".equals(strHDevThreshold) ? "selected" : ""%>>30</option>
		<option value="40" <%="40".equals(strHDevThreshold) ? "selected" : ""%>>40</option>
		<option value="50" <%="50".equals(strHDevThreshold) ? "selected" : ""%>>50</option>
		<option value="60" <%="60".equals(strHDevThreshold) ? "selected" : ""%>>60</option>
		<option value="70" <%="70".equals(strHDevThreshold) ? "selected" : ""%>>70</option>
		<option value="80" <%="80".equals(strHDevThreshold) ? "selected" : ""%>>80</option>
		<option value="90" <%="90".equals(strHDevThreshold) ? "selected" : ""%>>90</option>
		<option value="100" <%="100".equals(strHDevThreshold) ? "selected" : ""%>>100</option>
		</select>
	</td>
</tr>
</table>
</form>

<%=beanDebug.renderDebugLog()%>
