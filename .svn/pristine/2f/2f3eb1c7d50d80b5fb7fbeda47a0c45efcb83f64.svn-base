
<%@ page language="java" pageEncoding="UTF-8"

	import="java.util.*"
	
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.director.graph.GraphConstant"

%>

<%

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
String browser  = sessionUser.getUserBrowser();
String jsession = request.getSession().getId();

int idsite = sessionUser.getIdSite();

String language = sessionUser.getLanguage();
LangService langService = LangMgr.getInstance().getLangService(language);

	String title = langService.getString("graphvariable","opengraphdialog");
	String minut5Label = langService.getString("graphdata1","minut5");
	String minut15Label = langService.getString("graphdata1","minut15");
	String minut30Label = langService.getString("graphdata1","minut30");
	String hourLabel = langService.getString("graphdata1","hour");
	String hour6Label = langService.getString("graphdata1","hour6");
	String hour12Label = langService.getString("graphdata1","hour12");
	
	String dayLabel = langService.getString("graphdata1","day");
	String weekLabel = langService.getString("graphdata1","week");
	String monthLabel = langService.getString("graphdata1","month");

	String timePeriodLabel = langService.getString("graphdata1","timeperiod");
	String yearLabel = langService.getString("graphdata1","year");
	String month1Label = langService.getString("graphdata1","month1");
	String day1Label = langService.getString("graphdata1","day1");
	String hour1Label = langService.getString("graphdata1","hour1");
	String deviceListLabel = langService.getString("graphdata1","deviceList");

	String gen = langService.getString("cal","january");
	String feb = langService.getString("cal","february");
	String mar = langService.getString("cal","march");
	String apr = langService.getString("cal","april");
	String mag = langService.getString("cal","may");
	String giu = langService.getString("cal","june");
	String lug = langService.getString("cal","july");
	String ago = langService.getString("cal","august");
	String set = langService.getString("cal","september");
	String ott = langService.getString("cal","october");
	String nov = langService.getString("cal","november");
	String dic = langService.getString("cal","december");

	StringBuffer year = new StringBuffer();
	for(int i = 2000; i < 2050; i++){
		year.append("<option value=\"");
		year.append(i);
		year.append("\">");
		year.append(i);
		year.append("</option>");
	}//for
	
	StringBuffer day = new StringBuffer();
	for(int i = 1; i < 32; i++){
		day.append("<option value=\"");
		day.append(i);
		day.append("\">");
		day.append(i<10?"0"+i:""+i);
		day.append("</option>");
	}//for
	
	StringBuffer hour = new StringBuffer();
	for(int i = 0; i < 24; i++){
		hour.append("<option value=\"");
		hour.append(i);
		hour.append("\">");
		hour.append(i<10?"0"+i:""+i);
		hour.append("</option>");
	}//for
	
	StringBuffer minut = new StringBuffer();
	for(int i = 0; i < 60; i++){
		minut.append("<option value=\"");
		minut.append(i);
		minut.append("\">");
		minut.append(i<10?"0"+i:""+i);
		minut.append("</option>");
	}//for

	//String comboDevices = sessionUser.getProperty("listaDevices");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
  
  <head>
    
    <base href="<%=basePath%>" />
    
    <title><%=title%></title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="description" content="Graph Dialog Window">
    
    <link rel="stylesheet" type="text/css" href="stylesheet/plantVisor<%=browser%>.css">
		
	<!-- >script type="text/javascript" src="scripts/app/graphdialog_support.js"></script -->
		
  </head>

<body style="background-color:#FFFFFF">

<table border="0" width="95%" cellspacing="0" cellpadding="0" class="table">
	<tr height="30px">
		<td width="30%"><%=timePeriodLabel%></td>
		<td width="70%">
			<select style="width:50%;font-size:18px;" id="GDtimeperiod" name="GDtimeperiod" onchange="GDchangeTimePeriod()">
				<option value="<%=GraphConstant.MINUTS5%>"><%=minut5Label%></option>
				<option value="<%=GraphConstant.MINUTS15%>"><%=minut15Label%></option>
				<option value="<%=GraphConstant.MINUTS30%>"><%=minut30Label%></option>
				<option value="<%=GraphConstant.HOUR%>"><%=hourLabel%></option>
				<option value="<%=GraphConstant.HOURS6%>"><%=hour6Label%></option>
				<option value="<%=GraphConstant.HOURS12%>"><%=hour12Label%></option>
				<option value="<%=GraphConstant.DAY%>"><%=dayLabel%></option>
				<option value="<%=GraphConstant.WEEK%>"><%=weekLabel%></option>
				<option value="<%=GraphConstant.MONTH%>"><%=monthLabel%></option>
				<option value="-1"> -------------------- </option>
			</select>
		</td>
	</tr>
	<tr style="height:15px;">
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr height="30px">
		<td><%=yearLabel%>
		</td>
		<td align="left">
			<select name="GDyear" id="GDyear" style="width:50%;font-size:18px;"><%=year.toString()%></select>                
		</td>
	</tr>
	<tr style="height:15px;">
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr height="30px">
		<td><%=month1Label%></td>
		<td align="left">
			<select name="GDmonth" id="GDmonth" style="width:50%;font-size:18px;">
				<option value="1"><%=gen%></option>
				<option value="2"><%=feb%></option>
				<option value="3"><%=mar%></option>
				<option value="4"><%=apr%></option>
				<option value="5"><%=mag%></option>
				<option value="6"><%=giu%></option>
				<option value="7"><%=lug%></option>
				<option value="8"><%=ago%></option>
				<option value="9"><%=set%></option>
				<option value="10"><%=ott%></option>
				<option value="11"><%=nov%></option>
				<option value="12"><%=dic%></option>
			</select>
		</td>
	</tr>
	<tr style="height:15px;">
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr height="30px">
		<td><%=day1Label%>
		</td>
		<td align="left">
			<select name="GDday" id="GDday" style="width:22%;font-size:18px;"><%=day.toString()%></select>      
		</td>
	</tr>
	<tr style="height:15px;">
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr height="30px">
		<td><%=hour1Label%></td>
		<td align="left">
			<select name="GDhour" id="GDhour" style="width:22%;font-size:18px;" ><%=hour.toString()%></select> : <select name="GDminut" id="GDminut" style="width:22%;font-size:18px;"><%=minut.toString()%></select>     
		</td>
	</tr>
	<tr style="height:15px;">
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr style="height:25px;">
		<td><%=deviceListLabel%></td>
		<td align="left">
			<div id="GDdeviceName" name="GDdeviceName"></div>
		</td>
	</tr>
	<tr style="height:15px;">
		<td colspan="2">&nbsp;</td>
	</tr>
</table>
<table align="center">
	<tr>
		<td>&nbsp;</td>
		<td align="center" class="groupCategory" style="width:175px;height:30px;font-size:15px;" onclick="goBack2Graph();"><b><%=langService.getString("graphvariable","goback2graph")%></b></td>
		<td>&nbsp;</td>
	</tr>
</table>

</body>
</html>
