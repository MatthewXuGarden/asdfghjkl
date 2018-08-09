<%@ page language="java"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.bean.GroupListBean"
import="com.carel.supervisor.presentation.bean.DeviceStructureList"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.datalog.impl.AlarmLog"
import="com.carel.supervisor.dataaccess.datalog.impl.AlarmLogList"
import="com.carel.supervisor.presentation.alarms.AlarmList"
import="com.carel.supervisor.presentation.alarms.AlarmMngTable"
import="com.carel.supervisor.base.config.ProductInfoMgr"
import="com.carel.supervisor.director.guardian.GuardianCheck"
import="com.carel.supervisor.presentation.assistance.GuardianConfig"
import="com.carel.supervisor.presentation.bean.rule.ActionBeanList"
import="com.carel.supervisor.presentation.bo.helper.GuardianHelper"
import="com.carel.supervisor.dataaccess.support.Information"
import="com.carel.supervisor.presentation.bo.BSystem"
import="com.carel.supervisor.director.DirectorMgr"
import="com.carel.supervisor.device.DeviceStatusMgr"
import="com.carel.supervisor.plugin.parameters.ParametersMgr"
import="com.carel.supervisor.base.system.PvproInfo"
import="com.carel.supervisor.presentation.bean.DeviceDetectBean"
import="com.carel.supervisor.dataaccess.event.EventMgr"
import="java.text.MessageFormat"
import="com.carel.supervisor.dataaccess.dataconfig.ProductInfo"
import="com.carel.supervisor.presentation.bean.DeviceListBean"
import="com.carel.supervisor.base.conversion.Replacer"
import="com.carel.supervisor.plugin.co2.CO2SavingManager"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	String language = sessionUser.getLanguage();
	int idSite = sessionUser.getIdSite();
	String sitename = sessionUser.getSiteName();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String sessionName= "alrglb";
	
	GroupListBean groups = sessionUser.getGroup();
	int[] gids = groups.getIds();
	DeviceStructureList deviceStructureList = groups.getDeviceStructureList(); 
	int[] ids = deviceStructureList.retrieveIdsByGroupsId(gids);
	sessionUser.getTransaction().setIdDevices(ids);
	boolean hasAlarm = DeviceStatusMgr.getInstance().existAlarm(ids);
        
	String alarm_table_name = "";
	if(hasAlarm)
	{
		alarm_table_name = "<a href='javascript:void(0);' "+(sessionUser.isMenuActive("alrglb")?"onclick=top.frames['manager'].loadTrx('nop&folder=alrglb&bo=BAlrGlb&type=click') ":"")+"><div class='tdTitleTable'><img src='images/system/right.png' border=0 style='vertical-align:bottom; padding-right: 4px; padding-bottom: 1px;'/>"+lan.getString(sessionName,"activeal")+"</div></a>";
	}
	else
	{
		alarm_table_name = lan.getString(sessionName,"activeal");
	}
	AlarmList alarms = new AlarmList("");
	alarms.loadFromDataBase(sessionUser,1);
	alarms.setScreenH(sessionUser.getScreenHeight());
	alarms.setScreenW(sessionUser.getScreenWidth());
	alarms.setHeight(166);
	alarms.setWidth(788);
	alarms.setLink(true);
	alarms.setShowAckCancel(false);
	String htmlTable1= alarms.getHTMLAlarmTable("TA",language, sessionUser,10,false);
	
	String desc1 = lan.getString("menu","mgr");
	String[] engine_click = new String[3];
	engine_click[0] = "nop&folder=mgr&bo=BSystem&type=menu&desc=";
	engine_click[1] = "1";
	engine_click[2] = "&resource=../wizard/SubTab2.jsp&curTab=tab1name";
	String[] guardian_click = new String[3];
	guardian_click[0] = "nop&folder=mgr&bo=BSystem&type=menu&desc=";
	guardian_click[1] = "1";
	guardian_click[2] = "&resource=SubTab5.jsp&curTab=tab5name";
	String[] site_click = new String[3];
	site_click[0] = "nop&folder=siteview&bo=BSiteView&type=menu&desc=";
	site_click[1] = "1";
	site_click[2] = "&resource=SubTab1.jsp&curTab=tab1name";
	String[] safetyrestore_click = new String[3];
	safetyrestore_click[0] = "nop&folder=co2&bo=BCO2Saving&type=menu&desc=";
	safetyrestore_click[1] = "1";
	safetyrestore_click[2] = "&resource=SubTab1.jsp&curTab=tab1name";
	
	boolean engine_link = sessionUser.isTabActive("mgr","tab1name");
	boolean guardian_link = sessionUser.isTabActive("mgr","tab5name");
	boolean site_link = sessionUser.isTabActive("siteview","tab1name");
	boolean safetyrestore_link = sessionUser.isTabActive("co2", "tab1name");
	
	String txtMessages = lan.getString("multimsg","message");
	String txtGuardian = lan.getString("mgr","tab5name");
	String txtLicenseOn = lan.getString("top","licenseOn");
	String txtLicenseOff = lan.getString("top","licenseOff");

	//system page
	String htmlAlarmsList = "";
	String htmlMessageHeader = "<table class='tbMessage' cellpadding=0 cellspacing=0>";
	String htmlMessageFooter = "</table>";
	String htmlMessage = "";
	int i=1;
	String engineMsg = "";
	int engine_code = ServletHelper.messageToNotify();
	switch(engine_code)
	{
		case 1: 
			engineMsg = lan.getString("top", "message4");
			break;
		case 2: 
			engineMsg = lan.getString("top", "message2");
			break;
		case 3: 
			engineMsg = lan.getString("top", "message1");
			break;
	}
	String tdclassName = " onmouseover=msgmouseover(this,true);  onmouseout=msgmouseover(this,false); ";
	if(engineMsg != "")
	{
		htmlMessage += "<tr><td "+tdclassName+" align='center' "+(engine_link?"onclick=link('"+engine_click[0]+"','"+engine_click[1]+"','"+engine_click[2]+"')":"")+" "+(i++%2==0?"":"class='Row2'")+">"+engineMsg+"</td></tr>";
	}
	String guardianMsg = "";
	int[] guardian_code = DirectorMgr.getInstance().getGuardian_code();
	if(guardian_code[0] == 1 || guardian_code[0] == 2)
	{
		guardianMsg = lan.getString("top","message3");
	}
	else if(guardian_code[0] == 4)
	{
		guardianMsg = "guardianPRO countdown "+lan.getString("guardian","countdown")+" "+guardian_code[1];
	}
	if(guardianMsg != "")
	{
		htmlMessage += "<tr><td "+tdclassName+" align='center' "+(guardian_link?"onclick=link('"+guardian_click[0]+"','"+guardian_click[1]+"','"+guardian_click[2]+"')":"")+"  "+(i++%2==0?"":"class='Row2'")+">"+guardianMsg+"</td></tr>";
	}
	if(!DirectorMgr.getInstance().isPvproValid())
	{
		if(Information.getInstance().isTrialValid())
		{
			htmlMessage += "<tr><td "+tdclassName+" align='center' "+(engine_link?"onclick=link('"+engine_click[0]+"','"+engine_click[1]+"','"+engine_click[2]+"') ":"")+(i++%2==0?"":"class='Row2'")+">"+txtLicenseOn+"</td></tr>";
			String tmp = EventMgr.getInstance().retriveMessage(1,language,"S029");
			// the Replacer.replace method is used to allow the format function 
			// escapes the "'" character and shows correctly the countdown int parameter
			tmp = Replacer.replace(tmp,"'","''");
			String countdownmsg = MessageFormat.format(tmp, Information.getInstance().getCountDown());
			htmlMessage += "<tr><td "+tdclassName+" align='center' "+(engine_link?"onclick=link('"+engine_click[0]+"','"+engine_click[1]+"','"+engine_click[2]+"') ":"")+(i++%2==0?"":"class='Row2'")+">"+countdownmsg+"</td></tr>";
		}
	  	else
	  		htmlMessage += "<tr><td "+tdclassName+" align='center' "+(engine_link?"onclick=link('"+engine_click[0]+"','"+engine_click[1]+"','"+engine_click[2]+"') ":"")+(i++%2==0?"":"class='Row2'")+">"+txtLicenseOff+"</td></tr>";
	}
	if(	ParametersMgr.isMotorStopped() ) {
		boolean param_link = sessionUser.isTabActive("parameters","tab1name");
		String[] param_click = new String[3];
		param_click[0] = "nop&folder=parameters&bo=BParameters&type=menu&desc=";
		param_click[1] = "1";
		param_click[2] = "&resource=../parameters/SubTab1.jsp&curTab=tab1name";
		String strParamOff = lan.getString("parameters", "stoppedpluginicon");
		htmlMessage += "<tr><td "+tdclassName+" align='center' "+(param_link?"onclick=link('"+param_click[0]+"','"+param_click[1]+"','"+param_click[2]+"') ":"")+(i++%2==0?"":"class='Row2'")+">"+strParamOff+"</td></tr>";
	}

	if( PvproInfo.getInstance().getLicenseOverload() > 0 ) {
		String strLicense = lan.getString("siteview", "maxlicenselabel");
		htmlMessage += "<tr><td "+tdclassName+" align='center' "+(site_link?"onclick=link('"+site_click[0]+"','"+site_click[1]+"','"+site_click[2]+"') ":"")+(i++%2==0?"":"class='Row2'")+">"+strLicense+"</td></tr>";
   	}
	
	if( PvproInfo.getInstance().isLoggingOverload() ) {
		String strLoggingOverload = lan.getString("mgr", "sysoverload1") + "<BR>" + lan.getString("mgr", "sysoverload2") + PvproInfo.getInstance().getLoggingOverload();
		htmlMessage += "<tr><td "+tdclassName+" align='center' " + (i++%2==0?"":"class='Row2'")+">" + strLoggingOverload + "</td></tr>";
	}
	if( DeviceDetectBean.isDetection() ) {
		String strDeviceDetection = lan.getString("mgr", "device_detection");
		htmlMessage += "<tr><td "+tdclassName+" align='center' " + (i++%2==0?"":"class='Row2'")+">" + strDeviceDetection + "</td></tr>";
	}
	String strBTAlert = PvproInfo.getInstance().getBTAlert();
	if( strBTAlert.length() > 0 ) {
		String strScheduledBackup = lan.getString("multimsg", "BTAlert1") + strBTAlert;
		htmlMessage += "<tr><td "+tdclassName+" align='center' " + (i++%2==0?"":"class='Row2'")+">" + strScheduledBackup + "</td></tr>";
	}
	int num = CO2SavingManager.getInstance() != null?CO2SavingManager.getInstance().getRackNumAtSafeMode():0;
	if(num>0)
	{
		String safetyRestore = lan.getString("menu","co2");
		String rackinsafemode = lan.getString("mgr", "rackinsafemode");
		rackinsafemode = MessageFormat.format(rackinsafemode, new Object[]{num});  
		htmlMessage += "<tr><td "+tdclassName+" align='center' "+(safetyrestore_link?"onclick=link('"+safetyrestore_click[0]+"','"+safetyrestore_click[1]+"','"+safetyrestore_click[2]+"') ":"")+(i++%2==0?"":"class='Row2'")+">"+safetyRestore+":"+rackinsafemode+ "</td></tr>";
	}
	num = CO2SavingManager.getInstance() != null?CO2SavingManager.getInstance().getRackNumInBackupCondition():0;
	if(num>0)
	{
		String safetyRestore = lan.getString("menu","co2");
		String rackinbackupcondition = lan.getString("mgr", "rackinbackupcondition");
		rackinbackupcondition = MessageFormat.format(rackinbackupcondition, new Object[]{num});  
		htmlMessage += "<tr><td "+tdclassName+" align='center' "+(safetyrestore_link?"onclick=link('"+safetyrestore_click[0]+"','"+safetyrestore_click[1]+"','"+safetyrestore_click[2]+"') ":"")+(i++%2==0?"":"class='Row2'")+">"+safetyRestore+":"+rackinbackupcondition+ "</td></tr>";
	}
	
	if (!htmlMessage.equals(""))
		htmlAlarmsList = htmlMessageHeader + htmlMessage + htmlMessageFooter;
	
	BSystem bSystem = (BSystem)sessionUser.getCurrentUserTransaction().getBoTrx();
	String safetytable = bSystem.getHTMLSafetyLevel(language);
%>
<input type='hidden' id='txtGuardian' value='<%=txtGuardian%>'/>
<input type="hidden" id="desc1" value="<%=desc1 %>"/>
<INPUT type="hidden" id="LWCtDataName1_priority_col" value="<%=alarms.getPriorityCol()%>" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate1")%>" value="1" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate2")%>" value="2" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate3")%>" value="3" />
<INPUT type="hidden" id="pr_<%=lan.getString("alrview", "alarmstate4")%>" value="4" />

<table border="0" width="100%" cellspacing="1" cellpadding="1">
	<% if(!htmlAlarmsList.equals("")){ %>
	<tr>
		<td class="tdTitleTable"><%=txtMessages %></td>
	</tr>
	<tr>
		<td>
			<%=htmlAlarmsList %>
		</td>
	</tr>
	<tr>
		<td height='10px'></td>
	</tr>
	<% } %>
	<tr>
		<td class="tdTitleTable"><%=alarm_table_name %></td>
	</tr>
	<tr height="*" valign="top" id="trAlrList">		
	  <td><%=htmlTable1%></td>
	</tr>
	<tr>
		<td class="tdTitleTable" id="tdguardian">
		</td>
	</tr>
	<tr>
		<td id="tdsafetylevel">
			<%=safetytable %>
		</td>
	</tr>
</table>
