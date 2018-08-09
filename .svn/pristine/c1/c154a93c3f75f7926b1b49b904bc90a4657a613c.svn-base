<%@ page language="java" 

import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.presentation.bean.*"
import="com.carel.supervisor.presentation.bean.rule.*"
import="com.carel.supervisor.dataaccess.dataconfig.*"
import="com.carel.supervisor.presentation.dbllistbox.*"
import="com.carel.supervisor.presentation.rule.*"
import="com.carel.supervisor.dataaccess.datalog.impl.*"
import="java.util.*"

%>
<%@ page import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList" %>

<%

UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();
UserTransaction ut = sessionUser.getCurrentUserTransaction();
String notremoveaction = ut.remProperty("notremoveaction");
String actionnotremoved = lan.getString("alrsched","actionnotremoved2");

String relay = lan.getString("action","relay");
String doubleElement = lan.getString("dbllistbox","doublelement");
String nullselected = lan.getString("dbllistbox","nullselected");
//20090119 - BUG #5289 RF Check on max elements
String maxelements = lan.getString("dbllistbox", "maxelements");
String s_relay = lan.getString("setaction","relay");
String s_status = lan.getString("setaction","status");
String action = lan.getString("setaction","action");
String relayavailable = lan.getString("setaction","relayavailable");
String relayconfigured = lan.getString("setaction","relayconfigured");

String nomodactionfromide = lan.getString("ide","nomodactionfromide");

String active = "Active"; //lan.getString("relaymgr","active");
String noactive = "Not active"; //lan.getString("relaymgr","noactive");


//retrieve da sessione
int actioncode = Integer.parseInt(sessionUser.getProperty("actioncode"));
ActionBeanList actionList = new ActionBeanList();
String action_description = ActionBeanList.getDescription(idsite,actioncode);

//RELAY
String html = "<SELECT id='digital' name='digital'><OPTION selected value='1'>"+active+"</OPTION><OPTION value='0'>"+noactive+"</OPTION></SELECT>";

//String relaylistbox = new RelayListBox().getRelayListBox(idsite,language,html);
//combo relay

//DeviceListBean devices = new DeviceListBean(idsite,language);
RelayBeanList relaylist = new RelayBeanList(idsite, language);
StringBuffer comborelay = new StringBuffer();
String ids_relay = "";
//String color = "cacaca";
//if (relaylist.size()!=0)
//	color = "EFF1FE";
int rows = 100;

comborelay.append("<div id='divCollegeNames' style='OVERFLOW:auto;WIDTH:100%;height:100%' onscroll=\"OnDivScroll(relay_combo,"+rows+");\">");
comborelay.append("<SELECT class='selectB' id='relay_combo' multiple name='relay_combo' style='width:100%;height:100%' ondblclick='addRelayMaxItemsChk(" + RuleConstants.MAX_RELAY_ITEMS + ")'>");
 
RelayBean tmp = null;
int idvar = 0;
String device_desc = "";
for (int i=0;i<relaylist.size();i++)
{
	tmp = relaylist.getRelayBean(i);
	idvar = tmp.getIdvariable();
	//VarphyBean a = VarphyBeanList.retrieveVarById(idsite,idvar,language);
	//if (a!=null)
	//	device_desc = devices.getDevice(a.getDevice().intValue()).getDescription(); 
	
	comborelay.append("<OPTION value='"+tmp.getIdrelay()+"' class='"+((i%2==0)?"Row1":"Row2")+"'>"+tmp.getDeviceDesc()+" -> "+ tmp.getDescription()+"</OPTION>");
	ids_relay = ids_relay + tmp.getIdrelay() + ";";
}
comborelay.append("</SELECT>");
comborelay.append("</div>");

if (!ids_relay.equals("")) ids_relay = ids_relay.substring(0,ids_relay.length()-1);

//popolare tabella
ActionBeanList actionlist = new ActionBeanList();
String param = actionlist.getActionParameters(idsite, actioncode, "L");
String[] paramArray = param.split(";");
StringBuffer table_var_state = new StringBuffer();

String temp_desc = "";
if (!param.equals(""))
{
	for (int i=0;i<paramArray.length;i++)
	{
		String[] sub_param = paramArray[i].split("=");
		
		idvar = -1;
		if (relaylist.getRelayBeanById(Integer.parseInt(sub_param[0]))!=null)
		{
			RelayBean tmp_rel = relaylist.getRelayBeanById(Integer.parseInt(sub_param[0]));
			temp_desc = tmp_rel.getDescription();
			idvar = tmp_rel.getIdvariable();
			//VarphyBean a = VarphyBeanList.retrieveVarById(idsite,idvar,language);
			//if (a!=null)
			//	device_desc = devices.getDevice(a.getDevice().intValue()).getDescription(); 
			table_var_state.append("<TR class='Row1' style='font-size:10pt;' onclick='selectedLine(this,\"relaytable\");return false;' ondblclick='delRelay()'>");
			table_var_state.append("<TD width='80%'>"+tmp_rel.getDeviceDesc()+" -> "+temp_desc+"</TD>");
			table_var_state.append("<TD width='20%'>"+ ((sub_param[1].equals("1"))?active:noactive)+"</TD>");
			table_var_state.append("<input type='hidden'  id='hr_"+sub_param[0]+"' value='"+sub_param[1]+"'/></TR>");
		}
	}
	
}

%>

<INPUT type="hidden" id="ids_relay" value="<%=ids_relay%>"/>
<INPUT type="hidden" id="active" value="<%=active%>"/>
<INPUT type="hidden" id="noactive" value="<%=noactive%>"/>
<INPUT type="hidden" name="nomodactionfromide" id="nomodactionfromide" value="<%=nomodactionfromide%>"/>
<INPUT type="hidden" name="notremoveaction" id="notremoveaction" value="<%=notremoveaction%>"/>
<INPUT type="hidden" name="actionnotremoved" id="actionnotremoved" value="<%=actionnotremoved%>"/>
<INPUT type="hidden" name="nullselected" id="nullselected" value="<%=nullselected%>"/>
<INPUT type="hidden" name="doubleElement" id="doubleElement" value="<%=doubleElement%>"/>
<INPUT type="hidden" name="maxelements" id="maxelements" value="<%=maxelements%>"/>

<FORM id="frm_set_relay" name="frm_set_relay" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" name="cmd" id="cmd" value=""/>
<INPUT type="hidden" name="actioncode" id="actioncode" value="<%=actioncode%>"/>
<INPUT type="hidden" name="action_description" id="action_description" value="<%=action_description%>"/>
<INPUT type="hidden" name="param" id="param" value=""/>

<table border="0" cellpadding="1" cellspacing="1" width="100%" height="95%">
	<tr height="5%">
		<td><p class="tdTitleTable"><%=action%> <%=action_description%></p></td>
	</tr>
	<tr height="*" valign="top" id="trContFax">
		<td>
			<FIELDSET class='field'>
				<LEGEND class="standardTxt"><%=relay%></LEGEND>
				<!-- tabelle -->
				<TABLE align='center' border="0" width="100%">
					<TR>
					<TD valign="top" width="45%">
						<TABLE width="100%" height='100%' cellspacing='1' cellpadding='1' align="center" border="0">
							<TR>
								<TD id='headerrealayconf' class='th' width='90%' align='center'><b> <%=relayavailable%></b></TD>
								<TD id='headerstatus' class='th' width='10%' align='center'><b><%=s_status%></b></TD>
							</TR>
							<TR>
								<TD width='80%' height='100%'><%=comborelay.toString()%></TD>
								<TD width='20%' valign="top"><%=html%></TD>
							</TR>
						</TABLE>
					</TD>
					<TD width="10%" align="center" valign='middle'> 
						<TABLE width='100%' border='0' celpadding='1' cellspacing='1' align="center">
							<TR>
								<TD align="center">
								  <img onclick="addRelayMaxItemsChk(<%=RuleConstants.MAX_RELAY_ITEMS%>);return false;" src="images/dbllistbox/arrowdx_on.png" align="middle" />
								</TD>
							</TR>
							<tr><td>&nbsp;</td></tr>
							<TR>
								<TD align="center">
								  <img onclick="delRelay();return false;" src="images/dbllistbox/delete_on.png" align="middle" />
								</TD>
									</TR>
							</TABLE>
					</TD>
					<TD valign="top" width="45%" >
					<TABLE align="center" width="100%" border='0' cellspacing="0" cellpadding="0">
						<TR>
							<TD id="headertd" width="*">
								<DIV style="width:100%;background-color:cacaca;">
									<TABLE class='table' class='table' width="100%" cellspacing='1' cellpadding='0'>
													<TR class='th'>
														<TD id='headerrelayconf2' align='center' width='80%'><b><%=relayconfigured%></b></TD>
														<TD id='headerstatus2' align='center' width='20%'><b><%=s_status%></b></TD>
													</TR>
									</TABLE>
								</DIV>
						</TR>
						<TR>
							<TD>
								<DIV id='relaydiv' style="width:100%;height:100px; overflow:auto;background-color:cacaca;" >
									<TABLE id="relaytable" class="table" align="center" width="100%" >
										<%=table_var_state.toString()%>
									</TABLE>
								</DIV>
							</TD>	
						</TR>			
					</TABLE>
					</TD>
					</TR>
				</TABLE>
			</FIELDSET>
		</td>
	</tr>	
</table>
</FORM>
<SCRIPT type="text/javascript">
<!--
	var thisbodyx,thisbodyy;
	if (self.innerHeight) // all except Explorer
	{
		thisbodyx = self.innerWidth;
		thisbodyy = self.innerHeight;
	}
	else if (document.documentElement && document.documentElement.clientHeight) // Explorer 6 Strict Mode
	{
		thisbodyx = document.documentElement.clientWidth;
		thisbodyy = document.documentElement.clientHeight;
	}
	else if (document.body) // other Explorers
	{
		thisbodyx = document.body.clientWidth;
		thisbodyy = document.body.clientHeight;
	}
	if(thisbodyy>250){
			document.getElementById('divCollegeNames').style.height = thisbodyy-250;
		
		//	alert(document.getElementById('relaydiv').style.height);
			document.getElementById('relaydiv').style.height = thisbodyy-250;
		//	document.getElementById('relaydiv').style.height = document.getElementById('divCollegeNames').style.height ;
		}
 -->
</SCRIPT>

