<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="com.carel.supervisor.presentation.session.UserSession" %>
<%@ page import="com.carel.supervisor.presentation.helper.ServletHelper" %>
<%@ page import="com.carel.supervisor.presentation.devices.ReadOnlyAllDtlDevice" %>
<%@ page import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBean" %>
<%@ page import="com.carel.supervisor.dataaccess.varaggregator.VarAggregator" %>
<%@ page import="com.carel.supervisor.dataaccess.language.LangMgr" %>
<%@ page import="com.carel.supervisor.presentation.bean.DeviceListBean" %>
<!-- jsp:useBean id="devdtl" class="com.carel.supervisor.presentation.devices.DeviceDetail" scope="request" /-->
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	int idDevice = 0;
	try 
	{
		idDevice = Integer.parseInt(sessionUser.getProperty("iddev"));
	}
	catch(Exception e){}
	ReadOnlyAllDtlDevice filterVariable = new ReadOnlyAllDtlDevice(sessionUser,sessionUser.getLanguage(),idDevice);
	VarphyBean[] vv = VarAggregator.retrieveByPriorityAndDescription(sessionUser.getIdSite(), sessionUser.getLanguage(), idDevice);
	filterVariable.profileVariables(vv);
	String descr = DeviceListBean.retrieveSingleDeviceById(sessionUser.getIdSite(), Integer.parseInt(sessionUser.getProperty("iddev")), sessionUser.getLanguage()).getDescription();
	String pagedescr = LangMgr.getInstance().getLangService(sessionUser.getLanguage()).getString("rodtlview","comment1");
	String goback = LangMgr.getInstance().getLangService(sessionUser.getLanguage()).getString("rodtlview","goback");
	String detaildevicecol3 = LangMgr.getInstance().getLangService(sessionUser.getLanguage()).getString("dtlview", "detaildevicecol3");
	String col5 = LangMgr.getInstance().getLangService(sessionUser.getLanguage()).getString("dtlview","col5");
	String detaildevicecol0 = LangMgr.getInstance().getLangService(sessionUser.getLanguage()).getString("dtlview", "detaildevicecol0");
%>

<TABLE cellpadding="2" cellspacing="1" width="99%">
	<TR>
		<TD width="*"  valign="middle">
			<P class="tdTitleTable"><%=descr%></P>
		</TD>
		<TD align="right" width="20%">
			<TABLE border="0" cellpadding="0" cellspacing="0" width="170px" height="30px" onclick="goback(<%=idDevice%>);" style="cursor: pointer;">
				<TR>
					<TD width="170px" height="30px" class="groupCategory" align="right">
					<%=goback%>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height="5px" colspan="2"></TD></TR>
	<TR>
		<TD width="*"  valign="middle" colspan="2">
			<P class="standardTxt"><%=pagedescr%></P>
		</TD>
	</TR>
	<TR><TD height="5px" colspan="2"></TD></TR>
	<TR>
		<TD colspan="2">
			<%=filterVariable.renderVariables2("rodtlview",sessionUser)%>
		</TD>
	</TR>
</TABLE>
