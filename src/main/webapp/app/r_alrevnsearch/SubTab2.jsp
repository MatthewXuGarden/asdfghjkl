<%@ page language="java"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.devices.DeviceList"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.event.EventMgr"
	import="com.carel.supervisor.dataaccess.event.Category"
	import="com.carel.supervisor.dataaccess.dataconfig.SiteInfo"
	import="com.carel.supervisor.dataaccess.dataconfig.SiteInfoList"
%>

<%UserSession sessionUser = ServletHelper.retrieveSession(request
					.getRequestedSessionId(), request);
			String language = sessionUser.getLanguage();
			int idsite = sessionUser.getIdSite();

			//sezione multilingua
			LangService lan = LangMgr.getInstance().getLangService(language);
			String from = lan.getString("evnsearch", "from");
			String to = lan.getString("evnsearch", "to");
			String site = lan.getString("r_alrevnsearch", "site");
			String category = lan.getString("evnsearch", "category");
			String description = lan.getString("evnsearch", "description");
			String search = lan.getString("evnsearch", "search");
			String searchparam = lan.getString("evnsearch", "searchparam");
			String period = lan.getString("evnsearch", "period");
			String context = lan.getString("evnsearch", "context");
			String descriptionPage = lan.getString("searchev", "eventfound");
			String lastweek = lan.getString("alrsearch", "lastweek");
			String lastmonth = lan.getString("alrsearch", "lastmonth");
			String last3month = lan.getString("alrsearch", "last3month");

			//multilingua per calendario

			String dom = lan.getString("cal", "sun");
			String lun = lan.getString("cal", "mon");
			String ma = lan.getString("cal", "thu");
			String mer = lan.getString("cal", "wed");
			String gio = lan.getString("cal", "tue");
			String ven = lan.getString("cal", "fri");
			String sab = lan.getString("cal", "sat");

			String gen = lan.getString("cal", "january");
			String feb = lan.getString("cal", "february");
			String mar = lan.getString("cal", "march");
			String apr = lan.getString("cal", "april");
			String mag = lan.getString("cal", "may");
			String giu = lan.getString("cal", "june");
			String lug = lan.getString("cal", "july");
			String ago = lan.getString("cal", "august");
			String set = lan.getString("cal", "september");
			String ott = lan.getString("cal", "october");
			String nov = lan.getString("cal", "november");
			String dic = lan.getString("cal", "december");

			String dataselect = sessionUser.getProperty("dataselect");
			if (dataselect == null)
				dataselect = "week";

			
			String EScategory = sessionUser.getProperty("EScategory");
			if (EScategory == null)
				EScategory = "";

			//retrieve per combobox
			EventMgr events = EventMgr.getInstance();
			Category[] cat = events.retrieveCategory(idsite, language);

			//popolazione combobox categorie
			StringBuffer categCombo = new StringBuffer("");
			categCombo.append("<OPTION value=\"\">------------</OPTION>");
			String def = "";
			for (int i = 0; i < cat.length; i++) {
				if (EScategory.equals(cat[i].getCategorycode()))
					def = "selected";
				else
					def = "";
				categCombo.append("<OPTION " + def + " value="
						+ cat[i].getCategorycode() + ">"
						+ cat[i].getCategory() + "</OPTION>");
			}
			String categValue = categCombo.toString();
			
			
//POPOLAZIONE COMBOBOX SITI 
	StringBuffer comboSite = new StringBuffer();
	SiteInfo[] sitelist = SiteInfoList.retriveSites();
	SiteInfo tmp = null;
	comboSite.append("<OPTION value='-1'>---------------------</OPTION>");
	for (int i=0;i<sitelist.length;i++)	
	{
		tmp = sitelist[i];
		comboSite.append("<OPTION value='"+tmp.getId()+"'>"+tmp.getName()+"</OPTION>");
	}
String date1higher2 = lan.getString("report","date1higher2");


%>
<input type='hidden' id='reload_from' value='<%=sessionUser.getProperty("datefrom")%>'/>
<input type='hidden' id='reload_to' value='<%=sessionUser.getProperty("dateto")%>'/>
<INPUT type='hidden' id='date1higher2' value="<%=date1higher2%>"/>
<input type=hidden id="ESdescriptionPage" value="<%=descriptionPage%>"></input>
<table width="100%" align="center" class="standardTxt">
	<TR>
		<TD>
		<fieldset class="field"><legend><%=period%></legend>
		<FORM name="ESdata" id="ESdata">
		<TABLE>
			<TR>
				<TD>
				<TABLE class="standardTxt">
					<TR>
						<TD><input id="week"
							<%=(dataselect.equals("week"))?"checked":""%> type="radio"
							name="period" value="lastweek" onclick="disableCal()"><%=lastweek%></TD>
					</TR>
					<TR>
						<TD><input id="month"
							<%=(dataselect.equals("month"))?"checked":""%> type="radio"
							name="period" value="lastmonth" onclick="disableCal()"><%=lastmonth%></TD>
					</TR>
					<TR>
						<TD><input id="3month"
							<%=(dataselect.equals("3month"))?"checked":""%> type="radio"
							name="period" value="last3month" onclick="disableCal()"><%=last3month%></TD>
					</TR>
				</TABLE>
				</TD>
				<TD valign="top">
				<TABLE class="standardTxt">
					<TR>
						<TD width="30px"></TD>
						<TD width="60px"><input id="fromto" <%=(dataselect.equals("fromto"))?"checked":""%> type="radio" name="period" value="from" onclick="enableCal()"><%=from%></TD>
						<TD align="center" valign="top"><input type="hidden" name="tester" id="tester" value="" />
						<input class="standardTxt" disabled type="text"
							name="tester_year" id="tester_year" value="" size="4" maxLength="4" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'/>
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
						
						<p><p/>
						<div id="cal_tester_display"></div>
						<script type="text/javascript">
										var arDay = new Array("<%=dom%>","<%=lun%>","<%=ma%>","<%=mer%>","<%=gio%>","<%=ven%>","<%=sab%>");
										var arMonth = new Array("<%=gen%>","<%=feb%>","<%=mar%>","<%=apr%>","<%=mag%>","<%=giu%>","<%=lug%>","<%=ago%>","<%=set%>","<%=ott%>","<%=nov%>","<%=dic%>");
										cal1 = new Calendar ("cal1", "tester", new Date(), arDay, arMonth);
										renderCalendar(cal1);
									</script></TD>
						<TD align="center" width="40px"><%=to%></TD>
						<TD align="center" valign="top"><input type="hidden" name="tester2"
							id="tester2" value="" />
						<input class="standardTxt" disabled type="text"
							name="tester2_year" id="tester2_year" value="" size="4" maxLength="4" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);'/>
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
						<input class="standardTxt" disabled
							type="text" name="tester2_day" id="tester2_day" value="" size="2" maxLength="2" onblur="onlyNumberOnBlur(this);" onkeydown='checkOnlyNumber(this,event);' />
						
						<p><p/>
						<div id="cal_tester2_display"></div>
						<script type="text/javascript">
										var arDay = new Array("<%=dom%>","<%=lun%>","<%=ma%>","<%=mer%>","<%=gio%>","<%=ven%>","<%=sab%>");
										var arMonth = new Array("<%=gen%>","<%=feb%>","<%=mar%>","<%=apr%>","<%=mag%>","<%=giu%>","<%=lug%>","<%=ago%>","<%=set%>","<%=ott%>","<%=nov%>","<%=dic%>");
										cal2 = new Calendar ("cal2", "tester2", new Date(), arDay, arMonth);
										renderCalendar(cal2);
									</script></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</FORM>
		</fieldset>
		</TD>
	</TR>
	<TR height=20px"></TR>
	<TR>
		<TD>
		<FIELDSET class="field"><LEGEND><%=context%></LEGEND>
		<TABLE>
			<TR>
				<TD class="standardTxt"><%=site%></TD>
				<TD><SELECT class="standardTxt" type="text" id="ESid_site" style='width:300;'/>
					<%=comboSite.toString()%></SELECT>
					</TD>
				<TD width="100px"></TD>
				<TD align="right" class="standardTxt" ><%=category%></TD>
				<TD><SELECT class="standardTxt" id="EScategory" style='width:300;'/>
					<%=categValue%></TD>
			</TR>
		</TABLE>
		</FIELDSET>
		</TD>
</table>

