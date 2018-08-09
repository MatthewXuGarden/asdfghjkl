<%@ page language="java"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.presentation.bo.BR_AlrEvnSearch"
	import="com.carel.supervisor.dataaccess.dataconfig.SiteInfoList"
	import="com.carel.supervisor.dataaccess.dataconfig.SiteInfo"
%>
<%@ page import="com.carel.supervisor.presentation.bo.helper.SiteListHelper" %>

<%

UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
String language = sessionUser.getLanguage();
int idsite = sessionUser.getIdSite();
String jsession = request.getSession().getId();

//parametri per funzionalià combobox

String dataselect = sessionUser.getProperty("dataselect");
if (dataselect==null) dataselect = "week";

//sezione multilingua
LangService lan = LangMgr.getInstance().getLangService(language);

String period = lan.getString("alrsearch","period");
String context = lan.getString("alrsearch","context");
String lastweek = lan.getString("alrsearch","lastweek");
String lastmonth = lan.getString("alrsearch","lastmonth");
String last3month = lan.getString("alrsearch","last3month");
String from = lan.getString("alrsearch","from");
String to = lan.getString("alrsearch","to");
String s_site = lan.getString("r_alrevnsearch","site");
String s_category = lan.getString("evnsearch", "category");

String alarmFound = lan.getString("searchal","alarmfound");  //comparirà sulla navigazione della pagina
														//risulati.inviata tramite JS

//multilingua per calendario

String dom = lan.getString("cal","sun");
String lun = lan.getString("cal","mon");
String ma = lan.getString("cal","thu");
String mer = lan.getString("cal","wed");
String gio = lan.getString("cal","tue");
String ven = lan.getString("cal","fri");
String sab = lan.getString("cal","sat");

String gen = lan.getString("cal","january");
String feb = lan.getString("cal","february");
String mar = lan.getString("cal","march");
String apr = lan.getString("cal","april");
String mag = lan.getString("cal","may");
String giu = lan.getString("cal","june");
String lug = lan.getString("cal","july");
String ago = lan.getString("cal","august");
String set = lan.getString("cal","september");
String ott = lan.getString("cal","october");
String nov = lan.getString("cal","november");
String dic = lan.getString("cal","december");

String date1higher2 = lan.getString("report","date1higher2");


//POPOLAZIONE COMBOBOX SITI 
StringBuffer comboSite = new StringBuffer();
SiteInfo[] sitelist = SiteInfoList.retriveRemoteSite();
SiteInfo tmp = null;
comboSite.append("<OPTION value='-1'>---------------------</OPTION>");
for (int i=0;i<sitelist.length;i++)
{
	tmp = sitelist[i];
	comboSite.append("<OPTION value='"+tmp.getId()+"'>"+tmp.getName()+"</OPTION>");
}

//POPLAZIONE COMBOBOX ALARMS


String comboCategory = "";
comboCategory = BR_AlrEvnSearch.getComboAlarmCategory(idsite,language);
	
//combo categorie di allarmi


%>
<input type='hidden' id='reload_from' value='<%=sessionUser.getProperty("datefrom")%>'/>
<input type='hidden' id='reload_to' value='<%=sessionUser.getProperty("dateto")%>'/>
<input type=hidden id="alarmFound" value="<%=alarmFound%>"/>
<INPUT type='hidden' id='date1higher2' value="<%=date1higher2%>"/>
<FORM id="frm_r_alrsearch" name="frm_r_alrsearch" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<table border="0" width="100%" align="center" class="standardTxt">
 	<TR>
	   <TD>
		<fieldset class="field"><legend><%=period%></legend>
			<TABLE border="0" width="100%" cellspacing="1" cellpadding="1"> 
				<TR>
					<TD>
						<TABLE class="standardTxt">
							<TR><TD><input id="week" <%=(dataselect.equals("week"))?"checked":""%>  type="radio" name="period" value="lastweek" onclick="disableCal()"><%=lastweek%></TD></TR>
						    <TR><TD><input id="month" <%=(dataselect.equals("month"))?"checked":""%> type="radio" name="period" value="lastmonth" onclick="disableCal()"><%=lastmonth%></TD></TR>
						    <TR><TD><input id="3month" <%=(dataselect.equals("3month"))?"checked":""%> type="radio" name="period" value="last3month" onclick="disableCal()"><%=last3month%></TD></TR>
						</TABLE>
					</TD>
					<TD valign="top">
						<TABLE>
							<TR>
								<TD class="standardTxt"  width="30px"></TD>
								<TD class="standardTxt" width="60px"><input id="fromto" <%=(dataselect.equals("fromto"))?"checked":""%> type="radio" name="period" value="from" onclick="enableCal()"><%=from%></TD>
								<TD class="standardTxt" align="center" valign="top">
									<input type="hidden" name="tester" id="tester" value=""/>
									<input class="standardTxt" disabled type="text" name="tester_year" id="tester_year" value="" size="4" maxLength="4" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'/>
									<select class="standardTxt" disabled name="tester_month" id="tester_month">
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
									<input class="standardTxt" disabled type="text" name="tester_day" id="tester_day" value="" size="2" maxLength="2" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'/>
									
									<p></p>
									<div id="cal_tester_display"></div>
									<script type="text/javascript">
										var arDay = new Array("<%=dom%>","<%=lun%>","<%=ma%>","<%=mer%>","<%=gio%>","<%=ven%>","<%=sab%>");
										var arMonth = new Array("<%=gen%>","<%=feb%>","<%=mar%>","<%=apr%>","<%=mag%>","<%=giu%>","<%=lug%>","<%=ago%>","<%=set%>","<%=ott%>","<%=nov%>","<%=dic%>");
										cal1 = new Calendar ("cal1", "tester", new Date(), arDay, arMonth);
										renderCalendar(cal1);
									</script>
								</TD>
								<TD class="standardTxt" align="center" width="40px"><%=to%></TD>
								<TD class="standardTxt" align="center" valign="top">
									<input type="hidden" name="tester2" id="tester2" value=""/>
									<input class="standardTxt" disabled type="text" name="tester2_year" id="tester2_year" value="" size="4" maxLength="4" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'/>
									<select class="standardTxt" disabled name="tester2_month" id="tester2_month">
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
									<input class="standardTxt" disabled type="text" name="tester2_day" id="tester2_day" value="" size="2" maxLength="2" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);' />
									
									<p/>
									<div id="cal_tester2_display"></div>
									<script type="text/javascript">
										var arDay = new Array("<%=dom%>","<%=lun%>","<%=ma%>","<%=mer%>","<%=gio%>","<%=ven%>","<%=sab%>");
										var arMonth = new Array("<%=gen%>","<%=feb%>","<%=mar%>","<%=apr%>","<%=mag%>","<%=giu%>","<%=lug%>","<%=ago%>","<%=set%>","<%=ott%>","<%=nov%>","<%=dic%>");
										cal2 = new Calendar ("cal2", "tester2", new Date(), arDay, arMonth);
										renderCalendar(cal2);
									</script>
								
								</TD>
							</TR>	
						</TABLE>
					</TD>
				</TR>     
			</TABLE>
			</fieldset>
		</TD>
	</TR>
		<TR height=20px"></TR>
  <TR>
		<TD>
		<FIELDSET class="field"><LEGEND><%=context%></LEGEND>
			<TABLE class="standardTxt">
				<TR>
					<TD><%=s_site%></TD>
					<TD><SELECT class="standardTxt" id="id_site" name="id_site" style='width:300;'><%=comboSite.toString()%></SELECT></TD>
					<TD width="100px"></TD>
					<TD><%=s_category%></TD>
					<TD><SELECT class="standardTxt" id="category" name="category" style='width:300;'><%=comboCategory%></SELECT></TD>
				</TR>
			</TABLE>
		</FIELDSET>
		</TD>
	</TR>
</table>
</FORM>
