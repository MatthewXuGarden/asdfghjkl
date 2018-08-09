<%@ page language="java"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.bean.GroupListBean"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.devices.DeviceList"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList"
	import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBean"
	import="com.carel.supervisor.presentation.bo.BAlrEvnSearch"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
String language = sessionUser.getLanguage();
int idsite = sessionUser.getIdSite();

String pv_type = BaseConfig.getProductInfo("type");
//boolean group_info = (pv_type.equalsIgnoreCase(BaseConfig.ADVANCED_TYPE))?true:false;
boolean group_info = true;

//parametri per funzionalià combobox
GroupListBean groups = sessionUser.getGroup();

String selectedGroup = sessionUser.getPropertyAndRemove("selectedGroup");  
if (selectedGroup==null || selectedGroup.equals("undefined")) 
	selectedGroup= String.valueOf(groups.getGlobalGroup().getGroupId());

String selectedDevice = sessionUser.getPropertyAndRemove("selectedDevice");  
if (selectedDevice==null || selectedDevice.equals("undefined")) selectedDevice=""; 

String selectedAlarm = sessionUser.getPropertyAndRemove("selectedAlarm");  
if (selectedAlarm==null || selectedAlarm.equals("undefined")) selectedAlarm=""; 

String resetcombo = sessionUser.getPropertyAndRemove("reset");
if (resetcombo==null) resetcombo = "";

String dataselect = sessionUser.getPropertyAndRemove("dataselect");
if (dataselect==null) dataselect = "week";

String xhvalue = sessionUser.getPropertyAndRemove("xhval");
if (xhvalue == null) xhvalue = "24";

String prioritySel = sessionUser.getPropertyAndRemove("prioritySel");
if (prioritySel == null) prioritySel = "";

String chkack= sessionUser.getPropertyAndRemove("chkack"); if (chkack==null) chkack = "false";
String chkdel= sessionUser.getPropertyAndRemove("chkdel"); if (chkdel==null) chkdel = "false";
String chkres= sessionUser.getPropertyAndRemove("chkres"); if (chkres==null) chkres = "false";
String userack=  sessionUser.getPropertyAndRemove("userack"); if (userack==null) userack = "";
String userdel=  sessionUser.getPropertyAndRemove("userdel"); if (userdel==null) userdel = "";
String userres=  sessionUser.getPropertyAndRemove("userres"); if (userres==null) userres = "";
String userevent = sessionUser.getPropertyAndRemove("userevent"); if (userevent==null) userevent = "";

//sezione multilingua
LangService lan = LangMgr.getInstance().getLangService(language);

String period = lan.getString("alrsearch","period");
String context = lan.getString("alrsearch","context");
String lastxh1 = lan.getString("alrsearch","last");
String lastxh2 = lan.getString("graphvariable","rangehourslabel"); //hours
String lastweek = lan.getString("alrsearch","lastweek");
String lastmonth = lan.getString("alrsearch","lastmonth");
String last3month = lan.getString("alrsearch","last3month");
String from = lan.getString("alrsearch","from");
String to = lan.getString("alrsearch","to");
String group = lan.getString("alrsearch","group");
String device= lan.getString("alrsearch","device");
String alarm = lan.getString("alrsearch","alarm");
String action = lan.getString("alrsearch","action");
String ack = lan.getString("alrsearch","ack");
String delete = lan.getString("alrsearch","delete");
String reset = lan.getString("alrsearch","reset");
String user = lan.getString("alrsearch","user");
String category = lan.getString("evnsearch", "category");
String notValidHours = lan.getString("alrsearch","notvalidhours");
String alarmFound = lan.getString("searchalev","foundalev");  //comparirà sulla navigazione della pagina
														//risulati.inviata tramite JS

//multilingua per calendario

String dom = lan.getString("cal","sun");
String lun = lan.getString("cal","mon");
String ma = lan.getString("cal","tue");
String mer = lan.getString("cal","wed");
String gio = lan.getString("cal","thu");
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


int idsgroups[] =groups.getIds();  

StringBuffer groupCombo = new StringBuffer("");
groupCombo.append("<OPTION value=\""+ groups.getGlobalGroup().getGroupId()+",,,1\">------------------</OPTION>\n");

for (int i=0;i<idsgroups.length;i++)
{
	String def = null;
	if (selectedGroup.equals(String.valueOf(idsgroups[i])))
		def="selected";
	else
		def="";
		
	if (!groups.get(idsgroups[i]).isGlobal())  //per non riscrivere la prima riga
	groupCombo.append("<OPTION "+def+" value=\""+idsgroups[i]+",,,1\" >"+ groups.get(idsgroups[i]).getDescription()+"</OPTION>\n");
}
String groupValue = groupCombo.toString();

//POPOLAZIONE COMBOBOX DEVICE

StringBuffer deviceCombo = new StringBuffer("");

if (resetcombo.equals(String.valueOf(1)))
	deviceCombo.append("<OPTION selected value=\""+selectedGroup+",,,0\">------------------</OPTION>\n");
else 
	deviceCombo.append("<OPTION value=\""+selectedGroup+",,,0\">------------------</OPTION>\n");

int idsdevices[]= null;
int groupSelected = 0;

if (selectedGroup.equals(""))
	groupSelected = groups.getGlobalGroup().getGroupId();
else
	groupSelected = Integer.parseInt(selectedGroup);
	
idsdevices = groups.getDeviceStructureList().retrieveIdsByGroupId(groupSelected);

for (int i=0;i<idsdevices.length;i++)
{
	String def = null;
	if ((selectedDevice.equals(String.valueOf(idsdevices[i])))&& (!resetcombo.equals(String.valueOf(1))))
		def ="selected";
	else
		def ="";
	deviceCombo.append("<OPTION "+def+" value=\""+selectedGroup+","+idsdevices[i]+",,0\" >"+groups.getDeviceStructureList().get(idsdevices[i]).getDescription()+"</OPTION>\n");
}

String deviceValue = deviceCombo.toString();

//POPOLAZIONE COMBOBOX ALARMS

StringBuffer alarmCombo = new StringBuffer();
alarmCombo.append("<OPTION value='"+selectedGroup+","+selectedDevice+",,'>------------------</OPTION>\n");

int deviceSelected=-1;
String alarmValue = null;
boolean alarmCheck = false;
if (!resetcombo.equals(String.valueOf(1)))
{
	if (!selectedDevice.equals(""))
	{
		deviceSelected = Integer.parseInt(selectedDevice);
		VarphyBeanList varlist = new VarphyBeanList();
		VarphyBean[] alarmList = varlist.getAlarmVarPhy(language,idsite, deviceSelected);
		String select = null;
		for (int i=0;i<alarmList.length;i++)
		{
			if (selectedAlarm.equals(String.valueOf(alarmList[i].getId())))
			{
				select="selected";
				alarmCheck = true;
				
			}
			else 
				select="";
			alarmCombo.append("<OPTION "+select+" value=\""+selectedGroup+","+selectedDevice+","+ alarmList[i].getId()+",0\">"+alarmList[i].getShortDescription()+" ["+lan.getString("alrview", "alarmstate"+alarmList[i].getPriority())+"]</OPTION>\n");
		}
	
		alarmValue = alarmCombo.toString();
	}
	else
	{
		alarmValue = BAlrEvnSearch.getComboAlarmCategory(idsite,language,selectedGroup);
	}
}
else
	{
		alarmValue = BAlrEvnSearch.getComboAlarmCategory(idsite,language,selectedGroup);
	}

String date1higher2 = lan.getString("report","date1higher2");


//POPOLAZIONE COMBOBOX PRIORITY
String prival = "";
String priorityList = "<option "+("0".equals(prioritySel)?"selected":"")+" value=''>------------------</option>";

for (int i = 1; i <= 4; i++)
{
	prival = Integer.toString(i);
	priorityList += "<option "+(prival.equals(prioritySel)?"selected":"")+" value='"+prival+"'>"+lan.getString("alrview", "alarmstate"+prival)+"</option>";
}

//retrieve per combobox
EventMgr events = EventMgr.getInstance();
Category[] cat = events.retrieveCategory(idsite, language);

//popolazione combobox categorie
String EScategory = sessionUser.getPropertyAndRemove("EScategory");
if (EScategory == null)
	EScategory = "";
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



boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
String virtkey = "off";
if (OnScreenKey)
{
	virtkey = "on";
}
String onblurstr = OnScreenKey?"checkOnlyNumber(this,event);":"checkOnlyNumber(this,event);checkMax(this);";
	/*
	priorityList += "<option "+("1".equals(prioritySel)?"selected":"")+" value='1'>"+lan.getString("alrview", "alarmstate1")+"</option>";	
	priorityList += "<option "+("2".equals(prioritySel)?"selected":"")+" value='2'>"+lan.getString("alrview", "alarmstate2")+"</option>";
	priorityList += "<option "+("3".equals(prioritySel)?"selected":"")+" value='3'>"+lan.getString("alrview", "alarmstate3")+"</option>";
	priorityList += "<option "+("4".equals(prioritySel)?"selected":"")+" value='4'>"+lan.getString("alrview", "alarmstate4")+"</option>";
	*/
%>

<%@page import="com.carel.supervisor.dataaccess.event.EventMgr"%>
<%@page import="com.carel.supervisor.dataaccess.event.Category"%>
<input type='hidden' id='reload_from' value='<%=sessionUser.getProperty("datefrom")%>'/>
<input type='hidden' id='reload_to' value='<%=sessionUser.getProperty("dateto")%>'/>
<INPUT type='hidden' id='date1higher2' value="<%=date1higher2%>"/>
<input type=hidden id="notValidHours" value="<%=notValidHours%>"/>
<input type=hidden id="alarmFound" value="<%=alarmFound%>"/>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<input type='hidden' id='virtkeyboard' value='<%=virtkey%>' />
<table border="0" width="100%" align="center" class="standardTxt">
 	<TR>
	   <TD>
	   <FORM name="data" id="data">
		<fieldset class='field'><legend><%=period%></legend>
			<TABLE border="0" width="100%" cellspacing="1" cellpadding="1" > 
				<TR>
					<TD>
						<TABLE class="standardTxt">
							<TR>
								<TD>
									<input id="xh" <%=(dataselect.equals("xh"))?"checked":""%>  type="radio" name="period" value="lastxh" onclick="disableCal()"><%=lastxh1%>
									<input type="text" class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" id="xhval" name="xhval" value="<%=xhvalue%>" maxLength="2" size="2" onclick="document.getElementById('xh').checked='checked';" onchange="document.getElementById('xh').checked='checked';" onblur="<%=onblurstr %>" onkeydown="checkOnlyNumber(this,event);"/>
									<%=lastxh2%>
								</TD>
							</TR>
							<TR><TD><input id="week" <%=(dataselect.equals("week"))?"checked":""%>  type="radio" name="period" value="lastweek" onclick="disableCal()"><%=lastweek%></TD></TR>
						    <TR><TD><input id="month" <%=(dataselect.equals("month"))?"checked":""%> type="radio" name="period" value="lastmonth" onclick="disableCal()"><%=lastmonth%></TD></TR>
						    <TR><TD><input id="3month" <%=(dataselect.equals("3month"))?"checked":""%> type="radio" name="period" value="last3month" onclick="disableCal()"><%=last3month%></TD></TR>
						</TABLE>
					</TD>
					<TD valign="top">
						<TABLE>
							<TR>
								<TD class="standardTxt"  width="30px"></TD>
								<TD class="standardTxt" width="60px"><input id="fromto" <%=(dataselect.equals("fromto"))?"checked":""%> type="radio" name="period" value="from" onclick="<%=(OnScreenKey?"cal1.viewOnly= false;cal2.viewOnly= false;":"enableCal()")%>"><%=from%></TD>
								<TD class="standardTxt" align="center" valign="top"><input type="hidden" name="tester" id="tester" value=""/>
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

									<div id="cal_tester_display"></div>
									<script type="text/javascript">
										var arDay = new Array("<%=dom%>","<%=lun%>","<%=ma%>","<%=mer%>","<%=gio%>","<%=ven%>","<%=sab%>");
										var arMonth = new Array("<%=gen%>","<%=feb%>","<%=mar%>","<%=apr%>","<%=mag%>","<%=giu%>","<%=lug%>","<%=ago%>","<%=set%>","<%=ott%>","<%=nov%>","<%=dic%>");
										cal1 = new Calendar ("cal1", "tester", new Date(), arDay, arMonth);
										renderCalendar(cal1);
									</script>
								</TD>
								<TD class="standardTxt" align="center" width="40px"><%=to%></TD>
								<TD class="standardTxt" align="center" valign="top"><input type="hidden" name="tester2" id="tester2" value=""/>
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
   	</FORM>
   			</TD>
	</TR>
  	<TR>
		<TD>
		<FIELDSET class='field'><LEGEND><%=context%></LEGEND>
			<TABLE class="standardTxt">
				
				<TR>
					<% if(group_info){%>
						<TD><%=group%></TD>
						<TD><SELECT class="standardTxt" onchange="ASreloadPage(this);" id="group" name="group" style='width:350;'><%=groupValue%></SELECT></TD>
						<TD width="40px"></TD>
					<%} else {%>
						<input type='hidden' id='group' name='group' value='<%=groupValue%>'>
					<%}%>
					<TD class="standardTxt"><%=user%></TD>
					<TD><input class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" type="text" name="userevent" value="<%=userevent%>" id="userevent" onkeydown='checkBadChar(this,event)' maxlength="30" size='30'></TD>
				</TR>
				<TR></TR>
				<TR>
					<TD><%=device%></TD>
					<TD><SELECT class="standardTxt" onchange="ASreloadPage(this);" id="device" name="device" style='width:350;'><%=deviceValue%></SELECT></TD>
					<TD width="40px"></TD>
					<TD><%=category%></TD>
					<TD><SELECT class="standardTxt" id="EScategory" style='width:300;'>
						<%=categValue%>
						</SELECT>
					</TD>
				</TR>			
			</TABLE>
		</FIELDSET>
		</TD>
	</TR>
<form id="reload"></form>	
</table>
