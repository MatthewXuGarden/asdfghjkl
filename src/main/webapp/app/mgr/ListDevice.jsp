<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="com.carel.supervisor.field.FieldConnectorMgr" %>
<%@ page import="com.carel.supervisor.field.dataconn.impl.DataConnCAREL" %>
<%@ page import="com.carel.supervisor.field.types.PER_INFO" %>
<%@ page import="com.carel.supervisor.field.types.ExtUnitInfoT" %>
<%@ page import="com.carel.supervisor.dataaccess.dataconfig.DataConfigMgr" %>
<%@ page import="com.carel.supervisor.dataaccess.dataconfig.DeviceInfoList" %>
<%@ page import="com.carel.supervisor.dataaccess.dataconfig.DeviceInfo" %>

<%
    String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+request.getServerName()+":"+request.getServerPort()+path + "/";
	DataConnCAREL dataconn = ((DataConnCAREL)FieldConnectorMgr.getInstance().getDataCollector().getDataConnector("CAREL"));
	DeviceInfoList device = (DeviceInfoList)DataConfigMgr.getInstance().getConfig("cfdev");
	int num = device.size();
	DeviceInfo dev = null;
	int N = 0;

	for(int i = 0; i < num; i++)
	{
		dev = device.get(i);
		//solo i dev fisici hanno LineInfo() not null
		if ((dev.getLineInfo() != null) && ("CAREL".equals(dev.getLineInfo().getTypeProtocol())))
		{
			N++;
		}
	}
	PER_INFO perinfo = null;
	ExtUnitInfoT extUnitInfoT = null;
%>
<html>
	<head>
    	<base href="<%=basePath%>">
    	<meta http-equiv="pragma" content="no-cache">
    	<meta http-equiv="cache-control" content="no-cache">
    	<meta http-equiv="expires" content="0">
		<link rel="stylesheet" href="stylesheet/plantVisorIE.css" type="text/css" />
	</head>
	
	<script language="javascript">
	function reload() {
		window.location.reload( true );	
	}
	</script>	 
	
	<body bgcolor="#eaeaea" scroll="no">
	
	<div style="position: absolute; top: 0; left: 0; z-index: 2;">
	<TABLE border="0" width="100%">
		<TR>
			<td align="center"><button onclick="reload();">REFRESH</button></td>
		</TR>
	</TABLE>
	</div>
	<br />
	<div id="supermegacontainer" style="height:100%;width:100%;overflow:auto;position:relative;z-index:1;">	
	<TABLE border="1" class='table' width="100%">
	<TR>
		<TD style='background-color:808080' align='center'>cID</TD>
		<TD style='background-color:808080' align='center'>Line</TD>
		<TD style='background-color:808080' align='center'>Application code</TD>
		<TD style='background-color:808080' align='center'>Software release</TD>
		<TD style='background-color:808080' align='center'>No answer Cnt</TD>
		<TD style='background-color:808080' align='center'>ErrChkCnt</TD>
		<TD style='background-color:808080' align='center'>PerifType UnitType</TD>
		<TD style='background-color:808080' align='center'>No 485Device LwmNotFound</TD>
		<TD style='background-color:808080' align='center'>SwRelease min-Max</TD>
	</TR>
	<% for(int i = 1; i <= N; i++)
	{
		perinfo = dataconn.getPeriphericalInfo((short)i);
	%>
	<TR>
		<TD><%=(int) perinfo.getIdent() & 0xFF%></TD>
		<TD><%=perinfo.getLineNumber() + 1%></TD>
		<TD><%=perinfo.getApplicCode()%></TD>
		<TD><%=perinfo.getUnitSoftRelease()/10%></TD>
		<TD><%=perinfo.getNoAnswerCnt()%></TD>
		<TD><%=perinfo.getErrChkCnt()%></TD>
		<TD align="center"><%=perinfo.getPerifType()%>;<%=perinfo.getUnitType()%></TD>
		<TD align="center"><%=perinfo.getNo485Device()%>;<%=perinfo.getLwmNotFound()%></TD>
		<TD align="center"><%=perinfo.getUnitSoftReleaseMin()%>-<%=perinfo.getUnitSoftReleaseMax()%></TD>
	</TR>
	<%}%>
	</TABLE>

<BR>

<TABLE border="1" class='table' width="100%">
	<TR>
		<TD style='background-color:808080' align='center'>ActualSize</TD>
		<TD style='background-color:808080' align='center'>Type</TD>
		<TD style='background-color:808080' align='center'>FwRelease</TD>
		<TD style='background-color:808080' align='center'>ProtocolCap</TD>
		<TD style='background-color:808080' align='center'>HwCode</TD>
		<TD style='background-color:808080' align='center'>ExportIfaceCode</TD>
		<TD style='background-color:808080' align='center'>ApplicationRelease</TD>
		<TD style='background-color:808080' align='center'>ApplicationCode</TD>
		<TD style='background-color:808080' align='center'>CustomerCode</TD>
		<TD style='background-color:808080' align='center'>USMaxVar</TD>
		<TD style='background-color:808080' align='center'>BMaxVar</TD>
	</TR>
	<% 
	short returnCode = 0;
	for(int i = 1; i <= N; i++)
	{
		extUnitInfoT = dataconn.getPeriphericalInfoEx((short)i);
		returnCode = extUnitInfoT.getReturnCode();
		if (1 == returnCode)
		{
		%>
	<TR>
		<TD><%=extUnitInfoT.getActualSize()%></TD>
		<TD><%=extUnitInfoT.getType()%></TD>
		<TD><%=extUnitInfoT.getFwRelease()%></TD>
		<TD><%=extUnitInfoT.getProtocolCap()%></TD>
		<TD><%=extUnitInfoT.getHwCode()%></TD>
		<TD><%=extUnitInfoT.getExportIfaceCode()%></TD>
		<TD><%=extUnitInfoT.getApplicationRelease()%></TD>
		<TD><%=extUnitInfoT.getApplicationCode()%></TD>
		<TD><%=extUnitInfoT.getCustomerCode()%></TD>
		<TD><%=extUnitInfoT.getUSMaxVar()%></TD>
		<TD><%=extUnitInfoT.getBMaxVar()%></TD>
	</TR>
	<%	}
		else
		{
		%>
		<TR>
			<TD>***</TD>
			<TD>***</TD>
			<TD>***</TD>
			<TD>***</TD>
			<TD>***</TD>
			<TD>***</TD>
			<TD>***</TD>
			<TD>***</TD>
			<TD>***</TD>
			<TD>***</TD>
			<TD>***</TD>
		</TR>
	<%	
		}
	}
	%>
	</TABLE>
  </div>	
  </body>
</html>