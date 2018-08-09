<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.presentation.ac.AcMaster"
	import="com.carel.supervisor.director.ac.AcManager"
	import="java.util.*"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	import="com.carel.supervisor.director.ac.AcProperties"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	int idsite= sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();
	
	boolean isRun = AcManager.getInstance().isRunning();
	String s_engine = "";
	if (isRun){
		s_engine = "on"; 
	}else{
		s_engine = "off"; 
	}
	
	String table = AcMaster.getAcMasterTable(language, idsite, sessionUser.getScreenHeight(), sessionUser.getScreenWidth());
	
	//load config
	AcProperties prop_ac = new AcProperties();
	int maxm = prop_ac.getProp("ac_maxmaster");
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>

<input type='hidden' id='maxl' value='<%=lan.getString("ac","maxlimitmaster")%>'/>
<input type='hidden' id='max_master' value='<%=maxm%>'/>
<input type='hidden' id='missingdefault' value='<%=lan.getString("ac","missingdefault") %>'/>
<input type='hidden' id='undermin' value='<%=lan.getString("ac","undermin") %>'/>
<input type='hidden' id='overmax' value='<%=lan.getString("ac","overmax") %>'/>
<input type='hidden' id='stopservice' value='<%=lan.getString("ac","stopservice")%>'/>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>
<input type='hidden' id='s_engine' value='<%=s_engine%>'/>

<div style="height:95%">
	<p class='standardTxt'><%=lan.getString("ac","comment3")%></p>	
	<form name="ac_frm" id="ac_frm" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
		<input type='hidden' id='cmd' name='cmd'/>
		<div style='overflow-X:auto;overflow-Y:auto'>
			<%=table%>
		</div>
	</form>
</div>

