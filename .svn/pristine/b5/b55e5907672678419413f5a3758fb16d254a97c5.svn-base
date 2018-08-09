<%@ page language="java" 

import="java.util.Properties"

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.session.Transaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.lucinotte.LNGroups"
import="com.carel.supervisor.presentation.lucinotte.LNUtils"
import="com.carel.supervisor.presentation.lucinotte.LNScheduler"

%>

<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
UserTransaction ut = sessionUser.getCurrentUserTransaction();
Transaction transaction = sessionUser.getTransaction();
boolean isProtected = ut.isTabProtected();
String jsession = request.getSession().getId();

int idsite = sessionUser.getIdSite();

String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);

//int idgrp = -1; // gruppo standard == nessun gruppo
int timef = -1;

Properties properties = transaction.getSystemParameter();
if(properties != null)
{		
//	if(properties.get("idlcntgrp") != null)
//	{
//		idgrp = Integer.parseInt((String)properties.get("idlcntgrp"));
//	}
	
	if(properties.get("timef") != null)
	{
		timef = Integer.parseInt((String)properties.get("timef"));
	}
}
else
{
	properties = new Properties();
}

//se non viene passato dal b.o. lo recupera dal d.b.:
if (timef == -1)
{
	timef = LNUtils.getTimeFormat();
}

//String combo24hours = LNUtils.combo24Hours();
//String comboAPhours = LNUtils.comboAPHours();
//String combo_hours = "";

//if (timef == 0)
//	combo_hours = LNUtils.combo24Hours();
//else
//	combo_hours = LNUtils.comboAPHours();

//String combo_grp = LNGroups.getComboGroups(language, idgrp);

transaction.setSystemParameter(null);

String attivo = lan.getString("lucinotte","attivo");

//giorni della settimana:
/*
String lun = lan.getString("cal","mon");
String mar = lan.getString("cal","thu");
String mer = lan.getString("cal","wed");
String gio = lan.getString("cal","tue");
String ven = lan.getString("cal","fri");
String sab = lan.getString("cal","sat");
String dom = lan.getString("cal","sun");
*/

String ore = lan.getString("graphvariable","rangehourslabel");
String gruppo = lan.getString("lucinotte","gruppo");
String copysched = lan.getString("lucinotte","copysched");
String resetsched = lan.getString("lucinotte","resetsched");
String confirmreset = lan.getString("lucinotte","confirmreset");
String confirmcopy = lan.getString("lucinotte","confirmcopy");

String tabella = LNScheduler.getHtmlScheduler(language, idsite, timef);
%>

<input type="hidden" id="ctrlval" value="<%=lan.getString("lucinotte","ctrlval")%>" />
<input type="hidden" id="wantChangeDev" value="<%=lan.getString("lucinotte","wantChangeDev")%>" />
<input type="hidden" id="changeGrp" name="changeGrp" value="false" />
<input type="hidden" id="ore" value="<%=ore%>" />
<input type="hidden" id="copysched" value="<%=copysched%>" />
<input type="hidden" id="resetsched" value="<%=resetsched%>" />
<input type="hidden" id="confirmreset" value="<%=confirmreset%>" />
<input type="hidden" id="confirmcopy" value="<%=confirmcopy%>" />
<input type="hidden" id="group" value="<%=gruppo%>" />
<input type="hidden" id="day" value="<%=lan.getString("graphdata1","day1")%>" />

<!--  <div class='standardTxt'><%=lan.getString("lucinotte","comment4")%></div> -->	

<FORM name="lcnt_frm" id="lcnt_frm" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
<input type="hidden" id="cmd" name="cmd"/>
<input type="hidden" id="timetype" name="timetype" value="<%=timef%>" />
<%=tabella%>
</FORM>
