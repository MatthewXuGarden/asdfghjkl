<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.bo.helper.VariableHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.dataaccess.datalog.impl.*"
import="com.carel.supervisor.presentation.widgets.table.htmlcreate.element.*"

%>
<%@ page import="com.carel.supervisor.presentation.bo.helper.VarDetailHelper" %>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	int idsite= sessionUser.getIdSite();
	String jsession = request.getSession().getId();
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	boolean isProtected = ut.isTabProtected();
	String nospace = ut.remProperty("nospace");
	String historerror = ut.remProperty("historerror");

	String language = sessionUser.getLanguage();
	LangService lan = LangMgr.getInstance().getLangService(language);
	
	// Alessandro : aggiunto supporto alla Virtual Keyboard
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	
	// id device
	int iddev = Integer.parseInt(sessionUser.getProperty("iddev"));
	
	DeviceBean device= DeviceListBean.retrieveSingleDeviceById(idsite,iddev, language);
    String descr= device.getDescription();   
	
	//tabella	
	String table = VarDetailHelper.gethistorycalTable(idsite,iddev,language,isProtected,sessionUser.getScreenWidth(),sessionUser.getScreenHeight());
	
	//Stringhe per alert
	String confhistory = lan.getString("devdetail","confhistory");
	String longoperation = lan.getString("devdetail","longoperation");
    String configerror = lan.getString("devdetail","configerror");
    String time = lan.getString("devdetail","time");
    String variation = lan.getString("devdetail","variation");
    String devconfcomment3 = lan.getString("devdetail","devconfcomment3");	
    String s_nospace = lan.getString("devdetail","s_nospace");	
	
%>

<input type="hidden" id="s_week" value='<%=lan.getString("devdetail","week")%>'/>
<input type="hidden" id="s_month" value='<%=lan.getString("devdetail","month")%>'/>
<input type="hidden" id="s_month2" value='<%=lan.getString("devdetail","month2")%>'/>
<input type="hidden" id="s_month6" value='<%=lan.getString("devdetail","month6")%>'/>
<input type="hidden" id="s_year" value='<%=lan.getString("devdetail","year")%>'/>
<input type="hidden" id="s_year15" value='<%=lan.getString("devdetail","year15")%>'/>
<input type="hidden" id="s_year2" value='<%=lan.getString("devdetail","year2")%>'/>
<input type='hidden' id='vk_state' value='<%= (OnScreenKey) ? "1" : "0" %>' />

<INPUT type="hidden" id="nospace" name="nospace" value="<%=nospace%>" >
<INPUT type="hidden" id="historerror" name="historerror" value="<%=historerror%>" >
<INPUT type="hidden" id="s_nospace" name="s_nospace" value="<%=s_nospace%>" >
<INPUT type="hidden" id="configerror" name="configerror" value="<%=configerror%>" >
<INPUT type="hidden" id="longoperation" name="longoperation" value="<%=longoperation%>" >
<INPUT type="hidden" id="log_save_confirm" value="<%=lan.getString("devdetail","log_save_confirm")%>" >
<FORM id="frm_dev_var_save" name="frm_dev_var_save" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" id="cmd" name="cmd" >
<INPUT type="hidden" id="iddev" name="iddev" value="<%=iddev%>" >
<table border="0" width="100%" cellspacing="1" cellpadding="1" >
	<tr>
	<td>
			<p class="tdTitleTable"><%=descr%></p>
	</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
	<td>
			<p class="standardTxt"><%=devconfcomment3%></p>
	</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<TR valign="top" id="trVarList">
		<TD>
			<%=table%>
		</TD>
	</TR>
</table>
</FORM>