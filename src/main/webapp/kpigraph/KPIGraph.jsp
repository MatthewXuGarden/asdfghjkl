<%@page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangService"%>
<%@page import="com.carel.supervisor.dataaccess.language.LangMgr"%>
<%@page import="com.carel.supervisor.presentation.session.UserSession"%>
<%@page import="com.carel.supervisor.presentation.helper.ServletHelper"%>
<%
	//String path = request.getContextPath();
	String basePath = request.getParameter("basepath");
		//request.getScheme() + "://"
		//	+ request.getServerName() + ":" + request.getServerPort()
		//	+ path + "/";
	String xmlfile = request.getParameter("xmlfile");
	String swfPath = basePath + "kpigraph/KPIGraph.swf";
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	//sezione stringhe multilingua
	LangService lan = LangMgr.getInstance().getLangService(language);
%>
	
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>KPI</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<SCRIPT LANGUAGE="javascript">
<!--
var Element = "Background";
var forPrint = false;
function SendColor(sentcolor)
{
	if(!forPrint){
		Graph.SetColor(Element, parseInt(sentcolor, 16));
	} else {
		Graph.SetPrintColor(Element, parseInt(sentcolor, 16));
	}
}
var Graph;
// function calld by the KPIGraph when it's ready to receive commands
// we canot call back into the KPIGraph before returning from this function
function OnGraphReady()
{
	var ie = navigator.appName.indexOf("Microsoft") != -1;
	Graph = document['KPIGraph'];
	// set a small timeout for the graph init function and return
	setTimeout(InitGraph, 100);
}
function InitGraph()
{
	// set graph element colors for display
	Graph.SetColor("Background", 0xffffff);
	Graph.SetColor("ChartBg", 0xdddddd);
	Graph.SetColor("ChartAxis", 0x000000);
	Graph.SetColor("ChartGrid", 0x000000); // secondary grid color (OM / BM grid)
	Graph.SetColor("ChartScale", 0xdd0000);
	//Graph.SetColor("BtnPrint", 0xCCCCFF);
	//Graph.SetColor("BtnScroll", 0xCCCCFF);
	Graph.SetColor("BarDC", 0x00ff00); // this will also set color of primary (DC) grid
	Graph.SetColor("BarOM", 0xff0000);
	Graph.SetColor("BarBM", 0x0000ff);
	Graph.SetColor("BarText", 0x000000);
	Graph.SetColor("Text", 0x000000);
	
	// set graph element colors for print
	Graph.SetPrintColor("Background", 0xFFFFFF);
	Graph.SetPrintColor("ChartBg", 0xdddddd);
	Graph.SetPrintColor("ChartAxis", 0x000000);
	Graph.SetPrintColor("ChartGrid", 0x000000); // secondary grid color (OM / BM grid)
	Graph.SetPrintColor("ChartScale", 0x000000);
	Graph.SetPrintColor("BarDC", 0x00FF00); // this will also set color of primary (DC) grid
	Graph.SetPrintColor("BarOM", 0xFF0000);
	Graph.SetPrintColor("BarBM", 0x0000FF);
	Graph.SetPrintColor("BarText", 0x000000);
	Graph.SetPrintColor("Text", 0x000000);

	// set graph elements visibility
	Graph.SetVisibility("Grid1", true); // DC grid
	Graph.SetVisibility("Grid2", true); // OM and BM grid

	//set graph button string
	Graph.SetString("PrintPage","<%=lan.getString("kpigraph","printpage")%>");
	Graph.SetString("PrintAll","<%=lan.getString("kpigraph","printall")%>");
	// ask graph to load report data
	// this call should be after all SetColor  / SetPrintColor / SetVisibility calls
	Graph.Load("<%=basePath%>kpigraph/KPILoad.jsp?xmlfile=<%=""+new String(xmlfile)%>");
}
function ShowGrid1(bShow)
{
	//Graph.SetVisibility("Grid1", Check1.checked);
	Graph.SetVisibility("Grid1", bShow);
}
function ShowGrid2(bShow)
{
	//Graph.SetVisibility("Grid2", Check2.checked);
	Graph.SetVisibility("Grid2", bShow);
}

//-->
</SCRIPT>
	</head>
	<body>
		<div style="text-align: center;">
			<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
				codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0"
				width="800" height="600" id="KPIGraph" align="middle" VIEWASTEXT>
				<param name="allowScriptAccess" value="always" />
				<param name="movie" value="<%=swfPath%>" />
				<param name="quality" value="high" />
				<param name="bgcolor" value="#ffffff" />
				<embed src="<%=swfPath%>" quality="high"
					bgcolor="#ffffff" width="800" height="600" name="KPIGraph"
					align="middle" allowScriptAccess="sameDomain"
					type="application/x-shockwave-flash"
					pluginspage="http://www.macromedia.com/go/getflashplayer" />
			</object>
		</div>
		<!-- 
		<p>
			&nbsp;
		</p>
		<p>
			&nbsp;
		</p>
		<MAP NAME="webpal">
			<!--- Row 1 --- >
			<AREA COORDS="2,2,18,18" HREF="javascript:{SendColor('330000')}"
				onClick="SendColor('330000');return false" shape="RECT">
			<AREA COORDS="18,2,34,18" HREF="javascript:{SendColor('333300')}"
				onClick="SendColor('333300');return false" shape="RECT">
			<AREA COORDS="34,2,50,18" HREF="javascript:{SendColor('336600')}"
				onClick="SendColor('336600');return false" shape="RECT">
			<AREA COORDS="50,2,66,18" HREF="javascript:{SendColor('339900')}"
				onClick="SendColor('339900');return false" shape="RECT">
			<AREA COORDS="66,2,82,18" HREF="javascript:{SendColor('33CC00')}"
				onClick="SendColor('33CC00');return false" shape="RECT">
			<AREA COORDS="82,2,98,18" HREF="javascript:{SendColor('33FF00')}"
				onClick="SendColor('33FF00');return false" shape="RECT">
			<AREA COORDS="98,2,114,18" HREF="javascript:{SendColor('66FF00')}"
				onClick="SendColor('66FF00');return false" shape="RECT">
			<AREA COORDS="114,2,130,18" HREF="javascript:{SendColor('66CC00')}"
				onClick="SendColor('66CC00');return false" shape="RECT">
			<AREA COORDS="130,2,146,18" HREF="javascript:{SendColor('669900')}"
				onClick="SendColor('669900');return false" shape="RECT">
			<AREA COORDS="146,2,162,18" HREF="javascript:{SendColor('666600')}"
				onClick="SendColor('666600');return false" shape="RECT">
			<AREA COORDS="162,2,178,18" HREF="javascript:{SendColor('663300')}"
				onClick="SendColor('663300');return false" shape="RECT">
			<AREA COORDS="178,2,194,18" HREF="javascript:{SendColor('660000')}"
				onClick="SendColor('660000');return false" shape="RECT">
			<AREA COORDS="194,2,210,18" HREF="javascript:{SendColor('FF0000')}"
				onClick="SendColor('FF0000');return false" shape="RECT">
			<AREA COORDS="210,2,226,18" HREF="javascript:{SendColor('FF3300')}"
				onClick="SendColor('FF3300');return false" shape="RECT">
			<AREA COORDS="226,2,242,18" HREF="javascript:{SendColor('FF6600')}"
				onClick="SendColor('FF6600');return false" shape="RECT">
			<AREA COORDS="242,2,258,18" HREF="javascript:{SendColor('FF9900')}"
				onClick="SendColor('FF9900');return false" shape="RECT">
			<AREA COORDS="258,2,274,18" HREF="javascript:{SendColor('FFCC00')}"
				onClick="SendColor('FFCC00');return false" shape="RECT">
			<AREA COORDS="274,2,290,18" HREF="javascript:{SendColor('FFFF00')}"
				onClick="SendColor('FFFF00');return false" shape="RECT">
			<!--- Row 2 --- >
			<AREA COORDS="2,18,18,34" HREF="javascript:{SendColor('330033')}"
				onClick="SendColor('330033');return false" shape="RECT">
			<AREA COORDS="18,18,34,34" HREF="javascript:{SendColor('333333')}"
				onClick="SendColor('333333');return false" shape="RECT">
			<AREA COORDS="34,18,50,34" HREF="javascript:{SendColor('336633')}"
				onClick="SendColor('336633');return false" shape="RECT">
			<AREA COORDS="50,18,66,34" HREF="javascript:{SendColor('339933')}"
				onClick="SendColor('339933');return false" shape="RECT">
			<AREA COORDS="66,18,82,34" HREF="javascript:{SendColor('33CC33')}"
				onClick="SendColor('33CC33');return false" shape="RECT">
			<AREA COORDS="82,18,98,34" HREF="javascript:{SendColor('33FF33')}"
				onClick="SendColor('33FF33');return false" shape="RECT">
			<AREA COORDS="98,18,114,34" HREF="javascript:{SendColor('66FF33')}"
				onClick="SendColor('66FF33');return false" shape="RECT">
			<AREA COORDS="114,18,130,34" HREF="javascript:{SendColor('66CC33')}"
				onClick="SendColor('66CC33');return false" shape="RECT">
			<AREA COORDS="130,18,146,34" HREF="javascript:{SendColor('669933')}"
				onClick="SendColor('669933');return false" shape="RECT">
			<AREA COORDS="146,18,162,34" HREF="javascript:{SendColor('666633')}"
				onClick="SendColor('666633');return false" shape="RECT">
			<AREA COORDS="162,18,178,34" HREF="javascript:{SendColor('663333')}"
				onClick="SendColor('663333');return false" shape="RECT">
			<AREA COORDS="178,18,194,34" HREF="javascript:{SendColor('660033')}"
				onClick="SendColor('660033');return false" shape="RECT">
			<AREA COORDS="194,18,210,34" HREF="javascript:{SendColor('FF0033')}"
				onClick="SendColor('FF0033');return false" shape="RECT">
			<AREA COORDS="210,18,226,34" HREF="javascript:{SendColor('FF3333')}"
				onClick="SendColor('FF3333');return false" shape="RECT">
			<AREA COORDS="226,18,242,34" HREF="javascript:{SendColor('FF6633')}"
				onClick="SendColor('FF6633');return false" shape="RECT">
			<AREA COORDS="242,18,258,34" HREF="javascript:{SendColor('FF9933')}"
				onClick="SendColor('FF9933');return false" shape="RECT">
			<AREA COORDS="258,18,274,34" HREF="javascript:{SendColor('FFCC33')}"
				onClick="SendColor('FFCC33');return false" shape="RECT">
			<AREA COORDS="274,18,290,34" HREF="javascript:{SendColor('FFFF33')}"
				onClick="SendColor('FFFF33');return false" shape="RECT">
			<!--- Row 3 --- >
			<AREA COORDS="2,34,18,50" HREF="javascript:{SendColor('330066')}"
				onClick="SendColor('330066');return false" shape="RECT">
			<AREA COORDS="18,34,34,50" HREF="javascript:{SendColor('333366')}"
				onClick="SendColor('333366');return false" shape="RECT">
			<AREA COORDS="34,34,50,50" HREF="javascript:{SendColor('336666')}"
				onClick="SendColor('336666');return false" shape="RECT">
			<AREA COORDS="50,34,66,50" HREF="javascript:{SendColor('339966')}"
				onClick="SendColor('339966');return false" shape="RECT">
			<AREA COORDS="66,34,82,50" HREF="javascript:{SendColor('33CC66')}"
				onClick="SendColor('33CC66');return false" shape="RECT">
			<AREA COORDS="82,34,98,50" HREF="javascript:{SendColor('33FF66')}"
				onClick="SendColor('33FF66');return false" shape="RECT">
			<AREA COORDS="98,34,114,50" HREF="javascript:{SendColor('66FF66')}"
				onClick="SendColor('66FF66');return false" shape="RECT">
			<AREA COORDS="114,34,130,50" HREF="javascript:{SendColor('66CC66')}"
				onClick="SendColor('66CC66');return false" shape="RECT">
			<AREA COORDS="130,34,146,50" HREF="javascript:{SendColor('669966')}"
				onClick="SendColor('669966');return false" shape="RECT">
			<AREA COORDS="146,34,162,50" HREF="javascript:{SendColor('666666')}"
				onClick="SendColor('666666');return false" shape="RECT">
			<AREA COORDS="162,34,178,50" HREF="javascript:{SendColor('663366')}"
				onClick="SendColor('663366');return false" shape="RECT">
			<AREA COORDS="178,34,194,50" HREF="javascript:{SendColor('660066')}"
				onClick="SendColor('660066');return false" shape="RECT">
			<AREA COORDS="194,34,210,50" HREF="javascript:{SendColor('FF0066')}"
				onClick="SendColor('FF0066');return false" shape="RECT">
			<AREA COORDS="210,34,226,50" HREF="javascript:{SendColor('FF3366')}"
				onClick="SendColor('FF3366');return false" shape="RECT">
			<AREA COORDS="226,34,242,50" HREF="javascript:{SendColor('FF6666')}"
				onClick="SendColor('FF6666');return false" shape="RECT">
			<AREA COORDS="242,34,258,50" HREF="javascript:{SendColor('FF9966')}"
				onClick="SendColor('FF9966');return false" shape="RECT">
			<AREA COORDS="258,34,274,50" HREF="javascript:{SendColor('FFCC66')}"
				onClick="SendColor('FFCC66');return false" shape="RECT">
			<AREA COORDS="274,34,290,50" HREF="javascript:{SendColor('FFFF66')}"
				onClick="SendColor('FFFF66');return false" shape="RECT">
			<!--- Row 4 --- >
			<AREA COORDS="2,50,18,66" HREF="javascript:{SendColor('330099')}"
				onClick="SendColor('330099');return false" shape="RECT">
			<AREA COORDS="18,50,34,66" HREF="javascript:{SendColor('333399')}"
				onClick="SendColor('333399');return false" shape="RECT">
			<AREA COORDS="34,50,50,66" HREF="javascript:{SendColor('336699')}"
				onClick="SendColor('336699');return false" shape="RECT">
			<AREA COORDS="50,50,66,66" HREF="javascript:{SendColor('339999')}"
				onClick="SendColor('339999');return false" shape="RECT">
			<AREA COORDS="66,50,82,66" HREF="javascript:{SendColor('33CC99')}"
				onClick="SendColor('33CC99');return false" shape="RECT">
			<AREA COORDS="82,50,98,66" HREF="javascript:{SendColor('33FF99')}"
				onClick="SendColor('33FF99');return false" shape="RECT">
			<AREA COORDS="98,50,114,66" HREF="javascript:{SendColor('66FF99')}"
				onClick="SendColor('66FF99');return false" shape="RECT">
			<AREA COORDS="114,50,130,66" HREF="javascript:{SendColor('66CC99')}"
				onClick="SendColor('66CC99');return false" shape="RECT">
			<AREA COORDS="130,50,146,66" HREF="javascript:{SendColor('669999')}"
				onClick="SendColor('669999');return false" shape="RECT">
			<AREA COORDS="146,50,162,66" HREF="javascript:{SendColor('666699')}"
				onClick="SendColor('666699');return false" shape="RECT">
			<AREA COORDS="162,50,178,66" HREF="javascript:{SendColor('663399')}"
				onClick="SendColor('663399');return false" shape="RECT">
			<AREA COORDS="178,50,194,66" HREF="javascript:{SendColor('660099')}"
				onClick="SendColor('660099');return false" shape="RECT">
			<AREA COORDS="194,50,210,66" HREF="javascript:{SendColor('FF0099')}"
				onClick="SendColor('FF0099');return false" shape="RECT">
			<AREA COORDS="210,50,226,66" HREF="javascript:{SendColor('FF3399')}"
				onClick="SendColor('FF3399');return false" shape="RECT">
			<AREA COORDS="226,50,242,66" HREF="javascript:{SendColor('FF6699')}"
				onClick="SendColor('FF6699');return false" shape="RECT">
			<AREA COORDS="242,50,258,66" HREF="javascript:{SendColor('FF9999')}"
				onClick="SendColor('FF9999');return false" shape="RECT">
			<AREA COORDS="258,50,274,66" HREF="javascript:{SendColor('FFCC99')}"
				onClick="SendColor('FFCC99');return false" shape="RECT">
			<AREA COORDS="274,50,290,66" HREF="javascript:{SendColor('FFFF99')}"
				onClick="SendColor('FFFF99');return false" shape="RECT">
			<!--- Row 5 --- >
			<AREA COORDS="2,66,18,82" HREF="javascript:{SendColor('3300CC')}"
				onClick="SendColor('3300CC');return false" shape="RECT">
			<AREA COORDS="18,66,34,82" HREF="javascript:{SendColor('3333CC')}"
				onClick="SendColor('3333CC');return false" shape="RECT">
			<AREA COORDS="34,66,50,82" HREF="javascript:{SendColor('3366CC')}"
				onClick="SendColor('3366CC');return false" shape="RECT">
			<AREA COORDS="50,66,66,82" HREF="javascript:{SendColor('3399CC')}"
				onClick="SendColor('3399CC');return false" shape="RECT">
			<AREA COORDS="66,66,82,82" HREF="javascript:{SendColor('33CCCC')}"
				onClick="SendColor('33CCCC');return false" shape="RECT">
			<AREA COORDS="82,66,98,82" HREF="javascript:{SendColor('33FFCC')}"
				onClick="SendColor('33FFCC');return false" shape="RECT">
			<AREA COORDS="98,66,114,82" HREF="javascript:{SendColor('66FFCC')}"
				onClick="SendColor('66FFCC');return false" shape="RECT">
			<AREA COORDS="114,66,130,82" HREF="javascript:{SendColor('66CCCC')}"
				onClick="SendColor('66CCCC');return false" shape="RECT">
			<AREA COORDS="130,66,146,82" HREF="javascript:{SendColor('6699CC')}"
				onClick="SendColor('6699CC');return false" shape="RECT">
			<AREA COORDS="146,66,162,82" HREF="javascript:{SendColor('6666CC')}"
				onClick="SendColor('6666CC');return false" shape="RECT">
			<AREA COORDS="162,66,178,82" HREF="javascript:{SendColor('6633CC')}"
				onClick="SendColor('6633CC');return false" shape="RECT">
			<AREA COORDS="178,66,194,82" HREF="javascript:{SendColor('6600CC')}"
				onClick="SendColor('6600CC');return false" shape="RECT">
			<AREA COORDS="194,66,210,82" HREF="javascript:{SendColor('FF00CC')}"
				onClick="SendColor('FF00CC');return false" shape="RECT">
			<AREA COORDS="210,66,226,82" HREF="javascript:{SendColor('FF33CC')}"
				onClick="SendColor('FF33CC');return false" shape="RECT">
			<AREA COORDS="226,66,242,82" HREF="javascript:{SendColor('FF66CC')}"
				onClick="SendColor('FF66CC');return false" shape="RECT">
			<AREA COORDS="242,66,258,82" HREF="javascript:{SendColor('FF99CC')}"
				onClick="SendColor('FF99CC');return false" shape="RECT">
			<AREA COORDS="258,66,274,82" HREF="javascript:{SendColor('FFCCCC')}"
				onClick="SendColor('FFCCCC');return false" shape="RECT">
			<AREA COORDS="274,66,290,82" HREF="javascript:{SendColor('FFFFCC')}"
				onClick="SendColor('FFFFCC');return false" shape="RECT">
			<!--- Row 6 --- >
			<AREA COORDS="2,82,18,98" HREF="javascript:{SendColor('3300FF')}"
				onClick="SendColor('3300FF');return false" shape="RECT">
			<AREA COORDS="18,82,34,98" HREF="javascript:{SendColor('3333FF')}"
				onClick="SendColor('3333FF');return false" shape="RECT">
			<AREA COORDS="34,82,50,98" HREF="javascript:{SendColor('3366FF')}"
				onClick="SendColor('3366FF');return false" shape="RECT">
			<AREA COORDS="50,82,66,98" HREF="javascript:{SendColor('3399FF')}"
				onClick="SendColor('3399FF');return false" shape="RECT">
			<AREA COORDS="66,82,82,98" HREF="javascript:{SendColor('33CCFF')}"
				onClick="SendColor('33CCFF');return false" shape="RECT">
			<AREA COORDS="82,82,98,98" HREF="javascript:{SendColor('33FFFF')}"
				onClick="SendColor('33FFFF');return false" shape="RECT">
			<AREA COORDS="98,82,114,98" HREF="javascript:{SendColor('66FFFF')}"
				onClick="SendColor('66FFFF');return false" shape="RECT">
			<AREA COORDS="114,82,130,98" HREF="javascript:{SendColor('66CCFF')}"
				onClick="SendColor('66CCFF');return false" shape="RECT">
			<AREA COORDS="130,82,146,98" HREF="javascript:{SendColor('6699FF')}"
				onClick="SendColor('6699FF');return false" shape="RECT">
			<AREA COORDS="146,82,162,98" HREF="javascript:{SendColor('6666FF')}"
				onClick="SendColor('6666FF');return false" shape="RECT">
			<AREA COORDS="162,82,178,98" HREF="javascript:{SendColor('6633FF')}"
				onClick="SendColor('6633FF');return false" shape="RECT">
			<AREA COORDS="178,82,194,98" HREF="javascript:{SendColor('6600FF')}"
				onClick="SendColor('6600FF');return false" shape="RECT">
			<AREA COORDS="194,82,210,98" HREF="javascript:{SendColor('FF00FF')}"
				onClick="SendColor('FF00FF');return false" shape="RECT">
			<AREA COORDS="210,82,226,98" HREF="javascript:{SendColor('FF33FF')}"
				onClick="SendColor('FF33FF');return false" shape="RECT">
			<AREA COORDS="226,82,242,98" HREF="javascript:{SendColor('FF66FF')}"
				onClick="SendColor('FF66FF');return false" shape="RECT">
			<AREA COORDS="242,82,258,98" HREF="javascript:{SendColor('FF99FF')}"
				onClick="SendColor('FF99FF');return false" shape="RECT">
			<AREA COORDS="258,82,274,98" HREF="javascript:{SendColor('FFCCFF')}"
				onClick="SendColor('FFCCFF');return false" shape="RECT">
			<AREA COORDS="274,82,290,98" HREF="javascript:{SendColor('FFFFFF')}"
				onClick="SendColor('FFFFFF');return false" shape="RECT">
			<!--- Row 7 --- >
			<AREA COORDS="2,98,18,114" HREF="javascript:{SendColor('0000FF')}"
				onClick="SendColor('0000FF');return false" shape="RECT">
			<AREA COORDS="18,98,34,114" HREF="javascript:{SendColor('0033FF')}"
				onClick="SendColor('0033FF');return false" shape="RECT">
			<AREA COORDS="34,98,50,114" HREF="javascript:{SendColor('0066FF')}"
				onClick="SendColor('0066FF');return false" shape="RECT">
			<AREA COORDS="50,98,66,114" HREF="javascript:{SendColor('0099FF')}"
				onClick="SendColor('0099FF');return false" shape="RECT">
			<AREA COORDS="66,98,82,114" HREF="javascript:{SendColor('00CCFF')}"
				onClick="SendColor('00CCFF');return false" shape="RECT">
			<AREA COORDS="82,98,98,114" HREF="javascript:{SendColor('00FFFF')}"
				onClick="SendColor('00FFFF');return false" shape="RECT">
			<AREA COORDS="98,98,114,114" HREF="javascript:{SendColor('99FFFF')}"
				onClick="SendColor('99FFFF');return false" shape="RECT">
			<AREA COORDS="114,98,130,114" HREF="javascript:{SendColor('99CCFF')}"
				onClick="SendColor('99CCFF');return false" shape="RECT">
			<AREA COORDS="130,98,146,114" HREF="javascript:{SendColor('9999FF')}"
				onClick="SendColor('9999FF');return false" shape="RECT">
			<AREA COORDS="146,98,162,114" HREF="javascript:{SendColor('9966FF')}"
				onClick="SendColor('9966FF');return false" shape="RECT">
			<AREA COORDS="162,98,178,114" HREF="javascript:{SendColor('9933FF')}"
				onClick="SendColor('9933FF');return false" shape="RECT">
			<AREA COORDS="178,98,194,114" HREF="javascript:{SendColor('9900FF')}"
				onClick="SendColor('9900FF');return false" shape="RECT">
			<AREA COORDS="194,98,210,114" HREF="javascript:{SendColor('CC00FF')}"
				onClick="SendColor('CC00FF');return false" shape="RECT">
			<AREA COORDS="210,98,226,114" HREF="javascript:{SendColor('CC33FF')}"
				onClick="SendColor('CC33FF');return false" shape="RECT">
			<AREA COORDS="226,98,242,114" HREF="javascript:{SendColor('CC66FF')}"
				onClick="SendColor('CC66FF');return false" shape="RECT">
			<AREA COORDS="242,98,258,114" HREF="javascript:{SendColor('CC99FF')}"
				onClick="SendColor('CC99FF');return false" shape="RECT">
			<AREA COORDS="258,98,274,114" HREF="javascript:{SendColor('CCCCFF')}"
				onClick="SendColor('CCCCFF');return false" shape="RECT">
			<AREA COORDS="274,98,290,114" HREF="javascript:{SendColor('CCFFFF')}"
				onClick="SendColor('CCFFFF');return false" shape="RECT">
			<!--- Row 8 --- >
			<AREA COORDS="2,114,18,130" HREF="javascript:{SendColor('0000CC')}"
				onClick="SendColor('0000CC');return false" shape="RECT">
			<AREA COORDS="18,114,34,130" HREF="javascript:{SendColor('0033CC')}"
				onClick="SendColor('0033CC');return false" shape="RECT">
			<AREA COORDS="34,114,50,130" HREF="javascript:{SendColor('0066CC')}"
				onClick="SendColor('0066CC');return false" shape="RECT">
			<AREA COORDS="50,114,66,130" HREF="javascript:{SendColor('0099CC')}"
				onClick="SendColor('0099CC');return false" shape="RECT">
			<AREA COORDS="66,114,82,130" HREF="javascript:{SendColor('00CCCC')}"
				onClick="SendColor('00CCCC');return false" shape="RECT">
			<AREA COORDS="82,114,98,130" HREF="javascript:{SendColor('00FFCC')}"
				onClick="SendColor('00FFCC');return false" shape="RECT">
			<AREA COORDS="98,114,114,130" HREF="javascript:{SendColor('99FFCC')}"
				onClick="SendColor('99FFCC');return false" shape="RECT">
			<AREA COORDS="114,114,130,130"
				HREF="javascript:{SendColor('99CCCC')}"
				onClick="SendColor('99CCCC');return false" shape="RECT">
			<AREA COORDS="130,114,146,130"
				HREF="javascript:{SendColor('9999CC')}"
				onClick="SendColor('9999CC');return false" shape="RECT">
			<AREA COORDS="146,114,162,130"
				HREF="javascript:{SendColor('9966CC')}"
				onClick="SendColor('9966CC');return false" shape="RECT">
			<AREA COORDS="162,114,178,130"
				HREF="javascript:{SendColor('9933CC')}"
				onClick="SendColor('9933CC');return false" shape="RECT">
			<AREA COORDS="178,114,194,130"
				HREF="javascript:{SendColor('9900CC')}"
				onClick="SendColor('9900CC');return false" shape="RECT">
			<AREA COORDS="194,114,210,130"
				HREF="javascript:{SendColor('CC00CC')}"
				onClick="SendColor('CC00CC');return false" shape="RECT">
			<AREA COORDS="210,114,226,130"
				HREF="javascript:{SendColor('CC33CC')}"
				onClick="SendColor('CC33CC');return false" shape="RECT">
			<AREA COORDS="226,114,242,130"
				HREF="javascript:{SendColor('CC66CC')}"
				onClick="SendColor('CC66CC');return false" shape="RECT">
			<AREA COORDS="242,114,258,130"
				HREF="javascript:{SendColor('CC99CC')}"
				onClick="SendColor('CC99CC');return false" shape="RECT">
			<AREA COORDS="258,114,274,130"
				HREF="javascript:{SendColor('CCCCCC')}"
				onClick="SendColor('CCCCCC');return false" shape="RECT">
			<AREA COORDS="274,114,290,130"
				HREF="javascript:{SendColor('CCFFCC')}"
				onClick="SendColor('CCFFCC');return false" shape="RECT">
			<!--- Row 9 --- >
			<AREA COORDS="2,130,18,146" HREF="javascript:{SendColor('000099')}"
				onClick="SendColor('000099');return false" shape="RECT">
			<AREA COORDS="18,130,34,146" HREF="javascript:{SendColor('003399')}"
				onClick="SendColor('003399');return false" shape="RECT">
			<AREA COORDS="34,130,50,146" HREF="javascript:{SendColor('006699')}"
				onClick="SendColor('006699');return false" shape="RECT">
			<AREA COORDS="50,130,66,146" HREF="javascript:{SendColor('009999')}"
				onClick="SendColor('009999');return false" shape="RECT">
			<AREA COORDS="66,130,82,146" HREF="javascript:{SendColor('00CC99')}"
				onClick="SendColor('00CC99');return false" shape="RECT">
			<AREA COORDS="82,130,98,146" HREF="javascript:{SendColor('00FF99')}"
				onClick="SendColor('00FF99');return false" shape="RECT">
			<AREA COORDS="98,130,114,146" HREF="javascript:{SendColor('99FF99')}"
				onClick="SendColor('99FF99');return false" shape="RECT">
			<AREA COORDS="114,130,130,146"
				HREF="javascript:{SendColor('99CC99')}"
				onClick="SendColor('99CC99');return false" shape="RECT">
			<AREA COORDS="130,130,146,146"
				HREF="javascript:{SendColor('999999')}"
				onClick="SendColor('999999');return false" shape="RECT">
			<AREA COORDS="146,130,162,146"
				HREF="javascript:{SendColor('996699')}"
				onClick="SendColor('996699');return false" shape="RECT">
			<AREA COORDS="162,130,178,146"
				HREF="javascript:{SendColor('993399')}"
				onClick="SendColor('993399');return false" shape="RECT">
			<AREA COORDS="178,130,194,146"
				HREF="javascript:{SendColor('990099')}"
				onClick="SendColor('990099');return false" shape="RECT">
			<AREA COORDS="194,130,210,146"
				HREF="javascript:{SendColor('CC0099')}"
				onClick="SendColor('CC0099');return false" shape="RECT">
			<AREA COORDS="210,130,226,146"
				HREF="javascript:{SendColor('CC3399')}"
				onClick="SendColor('CC3399');return false" shape="RECT">
			<AREA COORDS="226,130,242,146"
				HREF="javascript:{SendColor('CC6699')}"
				onClick="SendColor('CC6699');return false" shape="RECT">
			<AREA COORDS="242,130,258,146"
				HREF="javascript:{SendColor('CC9999')}"
				onClick="SendColor('CC9999');return false" shape="RECT">
			<AREA COORDS="258,130,274,146"
				HREF="javascript:{SendColor('CCCC99')}"
				onClick="SendColor('CCCC99');return false" shape="RECT">
			<AREA COORDS="274,130,290,146"
				HREF="javascript:{SendColor('CCFF99')}"
				onClick="SendColor('CCFF99');return false" shape="RECT">
			<!--- Row 10 --- >
			<AREA COORDS="2,146,18,162" HREF="javascript:{SendColor('000066')}"
				onClick="SendColor('000066');return false" shape="RECT">
			<AREA COORDS="18,146,34,162" HREF="javascript:{SendColor('003366')}"
				onClick="SendColor('003366');return false" shape="RECT">
			<AREA COORDS="34,146,50,162" HREF="javascript:{SendColor('006666')}"
				onClick="SendColor('006666');return false" shape="RECT">
			<AREA COORDS="50,146,66,162" HREF="javascript:{SendColor('009966')}"
				onClick="SendColor('009966');return false" shape="RECT">
			<AREA COORDS="66,146,82,162" HREF="javascript:{SendColor('00CC66')}"
				onClick="SendColor('00CC66');return false" shape="RECT">
			<AREA COORDS="82,146,98,162" HREF="javascript:{SendColor('00FF66')}"
				onClick="SendColor('00FF66');return false" shape="RECT">
			<AREA COORDS="98,146,114,162" HREF="javascript:{SendColor('99FF66')}"
				onClick="SendColor('99FF66');return false" shape="RECT">
			<AREA COORDS="114,146,130,162"
				HREF="javascript:{SendColor('99CC66')}"
				onClick="SendColor('99CC66');return false" shape="RECT">
			<AREA COORDS="130,146,146,162"
				HREF="javascript:{SendColor('999966')}"
				onClick="SendColor('999966');return false" shape="RECT">
			<AREA COORDS="146,146,162,162"
				HREF="javascript:{SendColor('996666')}"
				onClick="SendColor('996666');return false" shape="RECT">
			<AREA COORDS="162,146,178,162"
				HREF="javascript:{SendColor('993366')}"
				onClick="SendColor('993366');return false" shape="RECT">
			<AREA COORDS="178,146,194,162"
				HREF="javascript:{SendColor('990066')}"
				onClick="SendColor('990066');return false" shape="RECT">
			<AREA COORDS="194,146,210,162"
				HREF="javascript:{SendColor('CC0066')}"
				onClick="SendColor('CC0066');return false" shape="RECT">
			<AREA COORDS="210,146,226,162"
				HREF="javascript:{SendColor('CC3366')}"
				onClick="SendColor('CC3366');return false" shape="RECT">
			<AREA COORDS="226,146,242,162"
				HREF="javascript:{SendColor('CC6666')}"
				onClick="SendColor('CC6666');return false" shape="RECT">
			<AREA COORDS="242,146,258,162"
				HREF="javascript:{SendColor('CC9966')}"
				onClick="SendColor('CC9966');return false" shape="RECT">
			<AREA COORDS="258,146,274,162"
				HREF="javascript:{SendColor('CCCC66')}"
				onClick="SendColor('CCCC66');return false" shape="RECT">
			<AREA COORDS="274,146,290,162"
				HREF="javascript:{SendColor('CCFF66')}"
				onClick="SendColor('CCFF66');return false" shape="RECT">
			<!--- Row 11 --- >
			<AREA COORDS="2,162,18,178" HREF="javascript:{SendColor('000033')}"
				onClick="SendColor('000033');return false" shape="RECT">
			<AREA COORDS="18,162,34,178" HREF="javascript:{SendColor('003333')}"
				onClick="SendColor('003333');return false" shape="RECT">
			<AREA COORDS="34,162,50,178" HREF="javascript:{SendColor('006633')}"
				onClick="SendColor('006633');return false" shape="RECT">
			<AREA COORDS="50,162,66,178" HREF="javascript:{SendColor('009933')}"
				onClick="SendColor('009933');return false" shape="RECT">
			<AREA COORDS="66,162,82,178" HREF="javascript:{SendColor('00CC33')}"
				onClick="SendColor('00CC33');return false" shape="RECT">
			<AREA COORDS="82,162,98,178" HREF="javascript:{SendColor('00FF33')}"
				onClick="SendColor('00FF33');return false" shape="RECT">
			<AREA COORDS="98,162,114,178" HREF="javascript:{SendColor('99FF33')}"
				onClick="SendColor('99FF33');return false" shape="RECT">
			<AREA COORDS="114,162,130,178"
				HREF="javascript:{SendColor('99CC33')}"
				onClick="SendColor('99CC33');return false" shape="RECT">
			<AREA COORDS="130,162,146,178"
				HREF="javascript:{SendColor('999933')}"
				onClick="SendColor('999933');return false" shape="RECT">
			<AREA COORDS="146,162,162,178"
				HREF="javascript:{SendColor('996633')}"
				onClick="SendColor('996633');return false" shape="RECT">
			<AREA COORDS="162,162,178,178"
				HREF="javascript:{SendColor('993333')}"
				onClick="SendColor('993333');return false" shape="RECT">
			<AREA COORDS="178,162,194,178"
				HREF="javascript:{SendColor('990033')}"
				onClick="SendColor('990033');return false" shape="RECT">
			<AREA COORDS="194,162,210,178"
				HREF="javascript:{SendColor('CC0033')}"
				onClick="SendColor('CC0033');return false" shape="RECT">
			<AREA COORDS="210,162,226,178"
				HREF="javascript:{SendColor('CC3333')}"
				onClick="SendColor('CC3333');return false" shape="RECT">
			<AREA COORDS="226,162,242,178"
				HREF="javascript:{SendColor('CC6633')}"
				onClick="SendColor('CC6633');return false" shape="RECT">
			<AREA COORDS="242,162,258,178"
				HREF="javascript:{SendColor('CC9933')}"
				onClick="SendColor('CC9933');return false" shape="RECT">
			<AREA COORDS="258,162,274,178"
				HREF="javascript:{SendColor('CCCC33')}"
				onClick="SendColor('CCCC33');return false" shape="RECT">
			<AREA COORDS="274,162,290,178"
				HREF="javascript:{SendColor('CCFF33')}"
				onClick="SendColor('CCFF33');return false" shape="RECT">
			<!--- Row 12 --- >
			<AREA COORDS="2,178,18,194" HREF="javascript:{SendColor('000000')}"
				onClick="SendColor('000000');return false" shape="RECT">
			<AREA COORDS="18,178,34,194" HREF="javascript:{SendColor('003300')}"
				onClick="SendColor('003300');return false" shape="RECT">
			<AREA COORDS="34,178,50,194" HREF="javascript:{SendColor('006600')}"
				onClick="SendColor('006600');return false" shape="RECT">
			<AREA COORDS="50,178,66,194" HREF="javascript:{SendColor('009900')}"
				onClick="SendColor('009900');return false" shape="RECT">
			<AREA COORDS="66,178,82,194" HREF="javascript:{SendColor('00CC00')}"
				onClick="SendColor('00CC00');return false" shape="RECT">
			<AREA COORDS="82,178,98,194" HREF="javascript:{SendColor('00FF00')}"
				onClick="SendColor('00FF00');return false" shape="RECT">
			<AREA COORDS="98,178,114,194" HREF="javascript:{SendColor('99FF00')}"
				onClick="SendColor('99FF00');return false" shape="RECT">
			<AREA COORDS="114,178,130,194"
				HREF="javascript:{SendColor('99CC00')}"
				onClick="SendColor('99CC00');return false" shape="RECT">
			<AREA COORDS="130,178,146,194"
				HREF="javascript:{SendColor('999900')}"
				onClick="SendColor('999900');return false" shape="RECT">
			<AREA COORDS="146,178,162,194"
				HREF="javascript:{SendColor('996600')}"
				onClick="SendColor('996600');return false" shape="RECT">
			<AREA COORDS="162,178,178,194"
				HREF="javascript:{SendColor('993300')}"
				onClick="SendColor('993300');return false" shape="RECT">
			<AREA COORDS="178,178,194,194"
				HREF="javascript:{SendColor('990000')}"
				onClick="SendColor('990000');return false" shape="RECT">
			<AREA COORDS="194,178,210,194"
				HREF="javascript:{SendColor('CC0000')}"
				onClick="SendColor('CC0000');return false" shape="RECT">
			<AREA COORDS="210,178,226,194"
				HREF="javascript:{SendColor('CC3300')}"
				onClick="SendColor('CC3300');return false" shape="RECT">
			<AREA COORDS="226,178,242,194"
				HREF="javascript:{SendColor('CC6600')}"
				onClick="SendColor('CC6600');return false" shape="RECT">
			<AREA COORDS="242,178,258,194"
				HREF="javascript:{SendColor('CC9900')}"
				onClick="SendColor('CC9900');return false" shape="RECT">
			<AREA COORDS="258,178,274,194"
				HREF="javascript:{SendColor('CCCC00')}"
				onClick="SendColor('CCCC00');return false" shape="RECT">
			<AREA COORDS="274,178,290,194"
				HREF="javascript:{SendColor('CCFF00')}"
				onClick="SendColor('CCFF00');return false" shape="RECT">
		</MAP>
		<FORM NAME="cpform">
			<TABLE CELLSPACING="5" CELLPADDING="15" BORDER="0" ALIGN="center"
				style='border: 1px solid black;'>
				<thead>
					<tr>
						<th colspan="2" align="center" style='border: 1px solid black;'>
							Personalizza layout
						</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td colspan="2">
							<table border="0" width="100%">
								<tr>
									<td>
										<INPUT type=checkbox id="Check1"
											onclick="ShowGrid1(this.checked);" NAME="Check1" />
										ShowDC Grid &nbsp;
									</td>
									<td>
										<INPUT type=checkbox id="Check2"
											onclick="ShowGrid2(this.checked);" NAME="Check2" />
										ShowOMBM Grid &nbsp;
									</td>
									<td>
										<INPUT type=checkbox id="Check3"
											onclick="forPrint = this.checked;" NAME="Check3" />
										Set print color
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<td>
										<INPUT TYPE="radio" NAME="flag"
											onClick="Element='Background';" CHECKED ID="Radio1" />
										Background
										<br>
									</td>
								</tr>
								<tr>
									<td>
										<INPUT TYPE="radio" NAME="flag" onClick="Element='ChartBg';"
											ID="Radio2" />
										ChartBg
										<br>
									</td>
								</tr>
								<tr>
									<td>
										<INPUT TYPE="radio" NAME="flag" onClick="Element='ChartAxis';"
											ID="Radio3" />
										ChartAxis
										<br>
									</td>
								</tr>
								<tr>
									<td>
										<INPUT TYPE="radio" NAME="flag"
											onClick="Element='ChartScale';" ID="Radio4" />
										ChartScale
										<br>
									</td>
								</tr>
								<tr>
									<td>
										<INPUT TYPE="radio" NAME="flag" onClick="Element='ChartGrid';"
											ID="Radio10" />
										ChartGrid
										<br>
									</td>
								</tr>
								<tr>
									<td>
										<INPUT TYPE="radio" NAME="flag" onClick="Element='BarDC';"
											ID="Radio5" />
										BarDC
										<br>
									</td>
								</tr>
								<tr>
									<td>
										<INPUT TYPE="radio" NAME="flag" onClick="Element='BarOM';"
											ID="Radio6" />
										BarOM
										<br>
									</td>
								</tr>
								<tr>
									<td>
										<INPUT TYPE="radio" NAME="flag" onClick="Element='BarBM';"
											ID="Radio7" />
										BarBM
										<br>
									</td>
								</tr>
								<tr>
									<td>
										<INPUT TYPE="radio" NAME="flag" onClick="Element='BarText';"
											ID="Radio8" />
										BarText
										<br>
									</td>
								</tr>
								<tr>
									<td>
										<INPUT TYPE="radio" NAME="flag" onClick="Element='Text';"
											ID="Radio9" />
										Text
										<br>
									</td>
								</tr>
							</table>
						</td>
						<TD align="center" valign="middle">
							<IMG SRC="/PlantVisorPRO/kpigraph/colors.gif" WIDTH="292"
								HEIGHT="196" BORDER="0" ALT="" USEMAP="#webpal">
						</TD>
					</tr>
				</tbody>
			</TABLE>
		</FORM>
		 -->
	</body>
</html>
