<%@ page language="java" 

import="java.util.Properties"

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.presentation.session.Transaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.ac.AcMaster"
import="com.carel.supervisor.presentation.ac.MasterBeanList"
import="com.carel.supervisor.presentation.ac.AcSlave"
import="com.carel.supervisor.director.ac.AcProperties"
import="com.carel.supervisor.director.ac.AcManager"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>

<%
UserSession sessUsr = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
Transaction transaction = sessUsr.getTransaction();
int idsite= sessUsr.getIdSite();


boolean gest_motore = sessUsr.getVariableFilter()==2;

String language = sessUsr.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();

//retrieve ac properties
AcProperties ac_prop = new AcProperties();
int clock = ac_prop.getProp("ac_clock");
//trasformazione secondi in minuti
clock /= 60;

MasterBeanList l = new MasterBeanList();
String legend = l.getHTMLList(language);
String table = "";
if (l.getNumberOfMaster()>0)
{
	table = AcSlave.getAcSlaveTable(language, sessUsr.getIdSite(), sessUsr.getScreenHeight(), sessUsr.getScreenWidth()); 
}

boolean isRun = AcManager.getInstance().isRunning();
String s_engine = "";
if (isRun)
{
	s_engine = "on"; 
}
else
{
	s_engine = "off"; 
}

String str_stato_save_slaves = "";
boolean stato_save_slaves = true;

Properties properties = transaction.getSystemParameter();
if(properties != null)
{		
	if(properties.get("save_slaves_risult") != null)
	{
		stato_save_slaves = Boolean.parseBoolean((String)properties.get("save_slaves_risult"));
	}
}
else
{
	properties = new Properties();
}

int tempo_min = AcProperties.minCycleTime;
tempo_min /= 60;

if (! stato_save_slaves)
{
	str_stato_save_slaves = lan.getString("ac","badsaveslaves");
}

String confermasave = lan.getString("ac","confermasave");
String inputno_ok = lan.getString("ac","inputno_ok");

transaction.setSystemParameter(null);
boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>

<input type='hidden' id='s_engine' value='<%=s_engine%>'/>
<input type='hidden' id='mintime' value='<%=lan.getString("ac","mintime") %>'/>
<input type="hidden" id="confermasave" value="<%=confermasave%>" />
<input type='hidden' id='save_slaves_ris' value='<%=str_stato_save_slaves%>'/>
<input type='hidden' id='gest_motore' value='<%=gest_motore%>'/>
<input type='hidden' id='inputno_ok' value='<%=inputno_ok%>'/>
<input type='hidden' id='tempo_min' value='<%=tempo_min%>'/>
<input type="hidden" id="selectallradio" value="<%=lan.getString("lucinotte","selectallradio")%>" />
<input type="hidden" id="deselectallradio" value="<%=lan.getString("lucinotte","deselectallradio")%>" />
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>

<div class='standardTxt'><%=lan.getString("ac","comment2")%></div>	

<%if (l.getNumberOfMaster()>0) {%>

	<form name="ac_frm" id="ac_frm" action="servlet/master;jsessionid=<%=jsession%>" method="post" onsubmit="submitSlaveConf();"> 
	<input type="hidden" id="cmd" name="cmd"/>
	<table width="98%">
		<tr valign="top">
			<td align='left'>
				<fieldset class="field" style='width:99%'>
					<legend class="standardTxt"><span><%=lan.getString("ac","legend")%></span></legend>
					<table width="98%">
						<tr>
							<td width="70%"><%=legend%></td>
							<td width="30%" align="right" valign="top" class="standardTxt">
								<div style="padding-top:8px;padding-left:5px;">
									<%=lan.getString("ac","controltime")%>
									<input class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" type="text" id="ver_time" name="ver_time" size="5" value="<%=clock%>"/>
									<%=lan.getString("ac","minuts")%>
								</div>
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>
	<div>
		<%=table%>
	</div>
	</form>
	
<% } else { %>

	<br />
	<p class="tdTitleTable"><%=lan.getString("ac","nomasteravailable")%></p>
<%}%>
