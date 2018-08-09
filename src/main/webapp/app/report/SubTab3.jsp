<%@ page language="java" pageEncoding="UTF-8"
	import="java.util.Map"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	import="com.carel.supervisor.dataaccess.datalog.impl.ReportExportConfigList"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	int idsite = sessionUser.getIdSite();
	String jsession = request.getSession().getId();
	LangService lan = LangMgr.getInstance().getLangService(language);
	
	ReportExportConfigList conf = new ReportExportConfigList();
	Map<String,String> map = conf.loadMap();
	String separator = map.get(ReportExportConfigList.SEPARATOR);
	String decimalLength = map.get(ReportExportConfigList.DECIMAL_LENGTH);
	String groupSeparator = map.get(ReportExportConfigList.GROUP_SEPARATOR);
	String decimalSeparator = map.get(ReportExportConfigList.DECIMAL_SEPARATOR);
		
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey(); 		
	
%>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>

<FORM id="frm_export_configure" action="servlet/master;jsessionid=<%=jsession%>" method="post">
	<FIELDSET class='field'>		
		<LEGEND class='standardTxt'><%=lan.getString("report","csv_setting")%></LEGEND>
		<TABLE border="0" cellpadding="1" cellspacing="1" width="100%" align="center">
			<tr>
				<td height="10px"></td>
			</tr>
			<TR class='standardTxt'>
				<TD width="15%" align="left">
					<%=lan.getString("report",ReportExportConfigList.SEPARATOR)%>
				<TD width="25%" align="left">
					<input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="<%=ReportExportConfigList.SEPARATOR %>" style="width:60px;height:20px;" value="<%=separator %>"/>
				</TD>
				<TD width="15%" align="left">
					<%=lan.getString("report",ReportExportConfigList.DECIMAL_LENGTH)%>
				</TD>
				<TD width="*" align="left">
					<input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="<%=ReportExportConfigList.DECIMAL_LENGTH %>" style="width:60px;height:20px;" value="<%=decimalLength %>" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/>
				</TD>
			</TR>
			<TR class='standardTxt'>
				<TD align="left">
					<%=lan.getString("report",ReportExportConfigList.GROUP_SEPARATOR)%>
				<TD align="left">
					<input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="<%=ReportExportConfigList.GROUP_SEPARATOR %>" style="width:60px;height:20px;" value="<%=groupSeparator %>"/>
				</TD>
				<TD align="left">
					<%=lan.getString("report",ReportExportConfigList.DECIMAL_SEPARATOR)%>	
				</TD>
				<TD align="left">
					<input <%=(OnScreenKey?"class='keyboardInput'":"")%> type="text" name="<%=ReportExportConfigList.DECIMAL_SEPARATOR %>" style="width:60px;height:20px;" value="<%=decimalSeparator %>"/>	
				</TD>
			</TR>
			<tr>
				<td height="10px"></td>
			</tr>
		</TABLE>
	</FIELDSET>
</FORM>