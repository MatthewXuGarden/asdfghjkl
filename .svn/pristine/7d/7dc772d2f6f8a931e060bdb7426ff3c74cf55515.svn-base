<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page language="java" isErrorPage="true" 
import="com.carel.supervisor.presentation.session.* "
import="com.carel.supervisor.presentation.helper.*"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.alarms.AlarmMngTable"
import="java.io.*"
import="com.carel.supervisor.presentation.bean.ProfileBean"
import="com.carel.supervisor.dataaccess.dataconfig.*"
%>

<%
String path = request.getContextPath();
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
String jsession = request.getSession().getId();
String language =  sessionUser.getLanguage();
LangService lang = LangMgr.getInstance().getLangService(language);


String table = AlarmMngTable.getHtmlAlarmMngTable(900,135,language,sessionUser);

int permission = sessionUser.isButtonActive("siteview", "tab4name","setAlarm")?2:1;

String multiacknotelabel = lang.getString("alrmng","multiacknotelabel");
String stickylabel = lang.getString("alrmng","stickylabel");
String missingfield = lang.getString("devdetail","missingfield");


String sticky = (SystemConfMgr.getInstance().get("stickyalarms").getValue().equals("TRUE"))?"checked":"";
String multiack = SystemConfMgr.getInstance().get("multiacknote").getValue();
if (multiack==null)
	multiack="";
else multiack=multiack.trim();

boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
String cssVirtualKeyboardClass = (OnScreenKey ? ' '+VirtualKeyboard.getInstance().getCssClass() : "");
%>

<p class='standardTxt' ><%=lang.getString("siteview","alarm_mgr_comment")%></p>
<SCRIPT type="text/javascript">

	function controlAndCheck(check1,valore1,check2,valore2){
		input = document.getElementById(check1);
		if (input){			
			val = input.checked;
			//se l'ack non è settato, levo la selezione sulle note obbligatorie
			if (val==valore1){
				oth = document.getElementById(check2);
				oth.checked=valore2;
			}
		}		
	}

	function decheckMandNote(ind){
		controlAndCheck("c_"+ind,false,"na_"+ind,false);
	}

	function checkAck(ind){
		controlAndCheck("na_"+ind,true,"c_"+ind,true);
	}

	function decheckSticky(ind){
		controlAndCheck("c_"+ind,false,"stickyalarms",false);
	}

	function checkAllAck(){
		controlAndCheck("stickyalarms",true,"c_"+1,true);
		controlAndCheck("stickyalarms",true,"c_"+2,true);
		controlAndCheck("stickyalarms",true,"c_"+3,true);
		controlAndCheck("stickyalarms",true,"c_"+4,true);
	}
	function checkResendEnable(obj){
		var mydisabled = null;
		if(obj.checked){
			mydisabled = false;
		}else{
			mydisabled = true;
		}
		
		document.getElementById("resend_frequency").disabled = mydisabled;
		document.getElementById("resend_times").disabled = mydisabled;
		
		document.getElementById("resend_priority_1").disabled = mydisabled;
		document.getElementById("resend_priority_2").disabled = mydisabled;
		document.getElementById("resend_priority_3").disabled = mydisabled;
		document.getElementById("resend_priority_4").disabled = mydisabled;
		
		document.getElementById("resend_channel_F").disabled = mydisabled;
		document.getElementById("resend_channel_E").disabled = mydisabled;
		document.getElementById("resend_channel_S").disabled = mydisabled;
		document.getElementById("resend_channel_R").disabled = mydisabled;
		
	}
	function maxminvaluecheck(id,min_permitted,max_permitted)
	{
		
		var set = document.getElementById(id).value;
		if (set != "")
		{
			//var set = obj.value;
			if (min_permitted != null && min_permitted != "")
			{
				if (Number(set) < Number(min_permitted))
				{
					alert(document.getElementById("s_minval").value + min_permitted);
					document.getElementById(id).value = "";
					//add this because maybe the input will be hidden
					window.onerror = killErrors;
					try
					{
						document.getElementById(id).focus();
					}
					catch(err)
					{
					}
					finally
					{
						window.onerror = null;
					}
					return false;
				}
			}
			if (max_permitted != null && max_permitted != "")
			{
				if (Number(set) > Number(max_permitted))
				{
					alert(document.getElementById("s_maxval").value + max_permitted);
					document.getElementById(id).value = "";
					//add this because maybe the input will be hidden
					window.onerror = killErrors;
					try
					{
						document.getElementById(id).focus();
					}
					catch(err)
					{
					}
					finally
					{
						window.onerror = null;
					}
					return false;
				}
			}
			return true;
		}
		return true;
	}
	
</SCRIPT>
<style>
.resend div{float:left;}
.resend div.desc{width:80px; text-align:right;}
.resend div.checkb{width:20px;}
.resend div.inputarea{width:200px;padding-left:10px;}
.resend div.secondrow{padding-top:5px;}
</style>
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>
<FORM id="frm_alarm_mgr" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" id="cmd" name="cmd" value="save_alarm_conf"/>
<INPUT type="hidden" id="permission" name="permission" value="<%=permission%>"/>
<input type='hidden' id='s_maxval' value="<%=lang.getString("dtlview","s_maxval")%>"/>
<input type='hidden' id='s_minval' value="<%=lang.getString("dtlview","s_minval")%>"/>
<input type="hidden" id="missingfield" value="<%=missingfield%>"/>
<div style="width: 100%">
<%=table%>
<%
SystemConf cfg = SystemConfMgr.getInstance().get("resend_enable");
String resendEnable =  "";
if(cfg != null)
	resendEnable = cfg.getValue().equals("TRUE")?"checked":"";

cfg = SystemConfMgr.getInstance().get("resend_frequency");
String c_freq = null;
if(cfg != null)
	c_freq = cfg.getValue();
c_freq = c_freq == null ?"10":c_freq;

cfg = SystemConfMgr.getInstance().get("resend_times");
String c_times = null;
if(cfg != null)
	c_times = cfg.getValue();
c_times = c_times == null ?"15":c_times;

String c_p_1 =  "";
cfg = SystemConfMgr.getInstance().get("resend_priority_1");
if(cfg != null)
	c_p_1 = cfg.getValue().equals("TRUE")?"checked":"";
String c_p_2 =  "";
cfg = SystemConfMgr.getInstance().get("resend_priority_2");
if(cfg != null)
	c_p_2 = cfg.getValue().equals("TRUE")?"checked":"";
String c_p_3 =  "";
cfg = SystemConfMgr.getInstance().get("resend_priority_3");
if(cfg != null)
	c_p_3 = cfg.getValue().equals("TRUE")?"checked":"";
String c_p_4 =  "";
cfg = SystemConfMgr.getInstance().get("resend_priority_4");
if(cfg != null)
	c_p_4 = cfg.getValue().equals("TRUE")?"checked":"";

String c_c_e =  "";
cfg = SystemConfMgr.getInstance().get("resend_channel_E");
if(cfg != null)
	c_c_e = cfg.getValue().equals("TRUE")?"checked":"";
String c_c_f =  "";
cfg = SystemConfMgr.getInstance().get("resend_channel_F");
if(cfg != null)
	c_c_f = cfg.getValue().equals("TRUE")?"checked":"";
String c_c_s =  "";
cfg = SystemConfMgr.getInstance().get("resend_channel_S");
if(cfg != null)
	c_c_s = cfg.getValue().equals("TRUE")?"checked":"";
String c_c_r =  "";
cfg = SystemConfMgr.getInstance().get("resend_channel_R");
if(cfg != null)
	c_c_r = cfg.getValue().equals("TRUE")?"checked":"";

String disable = resendEnable.equals("checked")?"":"disabled = true";
%>
</div>
<div style="width: 99%;padding-top: 15px;"">
	<div style="width: 49.6%;float:left;">
		<FIELDSET class="field" style="height:41px;">
			<LEGEND class="standardTxt"><%=multiacknotelabel%></LEGEND>
			<input type="text" name="multiacknote" id="multiacknote" class="standardTxt <%=cssVirtualKeyboardClass %>" maxlength="60" size="60" value="<%=multiack%>" />
		</FIELDSET>
	</div>
	<div style="width: 49.6%;float:right;">
		<FIELDSET class="field" style="height:41px;">
			<LEGEND class="standardTxt"><%=stickylabel%></LEGEND>
			<input type="checkbox" name="stickyalarms" id="stickyalarms" <%=sticky%> onclick="checkAllAck();" />
		</FIELDSET>
	</div>
</div>
<div style="width: 99%;clear: both;padding-top: 15px;">
<FIELDSET class="field">
		<LEGEND class="standardTxt"><%=  lang.getString("siteview","notifyresend")%></LEGEND>
		<div class="standardTxt">
			<div style="width:22%;float:left;padding-top:10px;" class="resend">
				<%=  lang.getString("siteview","enable")%><input type="checkbox" id="resend_enable" name = "resend_enable" <%=resendEnable%> onclick="checkResendEnable(this);" style="vertical-align: top;">
			</div>
			<div style="width:33%;float:left;" class="resend">
					<div class="desc"><%=  lang.getString("reportconf","period")%></div><div class="inputarea">
						<select id="resend_frequency" name="resend_frequency" class="standardTxt"  <%=disable%>>
							<option <%= c_freq.equals("10")?"selected":"" %> value="10">10</option>
							<option <%= c_freq.equals("15")?"selected":"" %> value="15">15</option>
							<option <%= c_freq.equals("20")?"selected":"" %> value="20">20</option>
							<option <%= c_freq.equals("30")?"selected":"" %> value="30">30</option>
							<option <%= c_freq.equals("60")?"selected":"" %> value="60">60</option>
						</select> <%=  lang.getString("siteview","minuts")%></div>
					<div class="clearer"></div>
					<div class="desc secondrow"><%=  lang.getString("setio","numtry")%></div><div class="inputarea secondrow"><input type="text"  id="resend_times" name="resend_times" <%=disable%> class="<%=cssVirtualKeyboardClass %>" style="width:40px;height:18px;" maxlength="3" size="5" value="<%=c_times%>" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);" ></div>
			</div>
			<div style="width:45%;float:left;" class="resend">
					<div class="desc"><%=  lang.getString("alrview","alarmstate1")%></div><div class="checkb"><input type="checkbox" id="resend_priority_1" name="resend_priority_1" <%=disable%> <%=c_p_1%> ></div>
					<div class="desc"><%= lang.getString("alrview","alarmstate2")%></div><div class="checkb"><input type="checkbox"  id="resend_priority_2" name="resend_priority_2" <%=disable%>  <%=c_p_2%> ></div>
					<div class="desc"><%=  lang.getString("alrview","alarmstate3")%></div><div class="checkb"><input type="checkbox" id="resend_priority_3" name="resend_priority_3" <%=disable%>  <%=c_p_3%> ></div>
					<div class="desc"><%=  lang.getString("alrview","alarmstate4")%></div><div class="checkb" style="width:*;overflow:hidden;"><input type="checkbox" id="resend_priority_4" name="resend_priority_4" <%=disable%>  <%=c_p_4%> ></div>
					<div class="clearer"></div>
					<div class="desc"><%= lang.getString("testio","email") %></div><div class="checkb"><input type="checkbox" id="resend_channel_E" name="resend_channel_E" <%=disable%> <%=c_c_e%> ></div>
					<div class="desc"><%= lang.getString("testio","sms") %></div><div class="checkb"><input type="checkbox"id="resend_channel_S" name="resend_channel_S" <%=disable%> <%=c_c_s%>> </div>
					<div class="desc" style="display: none"><%= lang.getString("testio","fax") %></div><div class="checkb" style="display: none"><input type="checkbox" id="resend_channel_F" name="resend_channel_F" <%=disable%> <%=c_c_f%>  ></div>
					<div class="desc"  style="display: none"><%= lang.getString("testio","rem") %></div><div class="checkb" style="display: none"><input type="checkbox"id="resend_channel_R" name="resend_channel_R" <%=disable%> <%=c_c_r%>> </div>
			</div>
		</div>
		
</FIELDSET>
</div>

<!--  SCRIPT type="text/javascript">
<!--
	var thisbodyx,thisbodyx;
	if (self.innerHeight) // all except Explorer
	{
		thisbodyx = self.innerWidth;
		thisbodyx = self.innerHeight;
	}
	else if (document.documentElement && document.documentElement.clientHeight) // Explorer 6 Strict Mode
	{
		thisbodyx = document.documentElement.clientWidth;
		thisbodyx = document.documentElement.clientHeight;
	}
	else if (document.body) // other Explorers
	{
		thisbodyx = document.body.clientWidth;
		thisbodyx = document.body.clientHeight;
	}
	document.getElementById('LWCtDataName0').style.height = thisbodyx-200;
//-- >
</SCRIPT  -->
</FORM>