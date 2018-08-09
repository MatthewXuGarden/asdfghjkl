<%@ page language="java" 
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.controller.database.TimeBandList"
import="com.carel.supervisor.controller.database.TimeBandBean"
import="com.carel.supervisor.controller.database.RuleBean"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.presentation.bean.rule.RuleBeanHelper"
import="com.carel.supervisor.dataaccess.datalog.impl.ConditionBeanList"
import="com.carel.supervisor.dataaccess.datalog.impl.ConditionBean"
import="com.carel.supervisor.presentation.bean.rule.ActionBeanList"
import="com.carel.supervisor.presentation.bean.ProfileBean"
import="java.util.*"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
int idsite= sessionUser.getIdSite();
String language = sessionUser.getLanguage();
LangService lan = LangMgr.getInstance().getLangService(language);
String jsession = request.getSession().getId();
String rule = lan.getString("alrsched","rule");
String description = lan.getString("alrsched","description");
String condition = lan.getString("alrsched","condition");
String timebands = lan.getString("alrsched","timebands");
String action = lan.getString("alrsched","action");
String delay = lan.getString("alrsched","delay");
String minutes = lan.getString("alrsched","minutes");
String noaction = lan.getString("alrsched","noaction");
String nodesc = lan.getString("alrsched","nodesc");
String nocond = lan.getString("alrsched","nocond");
String notimeband = lan.getString("alrsched","notimeband");
String enabled = lan.getString("alrsched","enabled");
String confirmruledel = lan.getString("alrsched","confirmruledel");
String noremrulefromide = lan.getString("ide","noremrulefromide");
String comment = "";

comment = lan.getString("alrsched","alrevn_comment");

//controllo permessi	
	String perm = "";
	int permission = sessionUser.getVariableFilter();
	if (permission==0)
		perm = "disabled";

	boolean protect = (permission==0)?true:false;
	
	 
String cmd = sessionUser.getPropertyAndRemove("cmd");



boolean isScheduled = false;
String aux_desc = "";
String aux_cond = "0";
String aux_time = "0";
String aux_action = "0";
String aux_delay = "";
String aux_enabled = "checked"; 

String s_idrule = sessionUser.getProperty("idrule");
if (s_idrule==null) s_idrule = "";



//TABELLA REGOLE
RuleBeanHelper ruleList = new RuleBeanHelper(new Integer(idsite));
Map ids = ruleList.getIds();

ruleList.setScreenH(sessionUser.getScreenHeight());
ruleList.setScreenW(sessionUser.getScreenWidth());
String ruletable = ruleList.getHTMLtable(idsite, BaseConfig.getPlantId(),language,"",140, 900, isScheduled,protect);
String disabIdeRule = "";

if (cmd!=null&&(cmd.equals("modify_rule")))
{
	int idrule = new Integer(s_idrule).intValue();
	RuleBean rule_to_modify = ruleList.get(((Integer)ids.get(new Integer(s_idrule))).intValue());
	aux_desc= rule_to_modify.getRuleCode();
	aux_cond= rule_to_modify.getIdCondition().toString();
	aux_time= rule_to_modify.getIdTimeband().toString();
	aux_action= rule_to_modify.getActionCode().toString();
	aux_delay= String.valueOf(rule_to_modify.getDelay()/60);
	if (rule_to_modify.getIsenabled().equals("TRUE"))
		aux_enabled = "checked";
	else
		aux_enabled = "";
	if (idrule<0)
	disabIdeRule = "disabled";
		
}

//COMBO CONDITIONS
String sel_cond = "";
ConditionBeanList condlist = new ConditionBeanList(idsite,language);
condlist.loadAllConditions(idsite);

StringBuffer comboConditions = new StringBuffer();
comboConditions.append("<OPTION value='0'>--------------</OPTION>");
ConditionBean tmpcond = null;
ConditionBean[] list_cond = condlist.getConditionList();

for (int i=0;i<list_cond.length;i++)
{
	tmpcond = list_cond[i];
	//show also rule editor condition
	//if (tmpcond.getIdCondition()>=0)
	//{
		if (tmpcond.getIdCondition()==Integer.parseInt(aux_cond))
			sel_cond="selected";
		else
			sel_cond ="";
		comboConditions.append("<OPTION "+sel_cond+" value='"+tmpcond.getIdCondition()+"'>"+tmpcond.getCodeCondition()+"</OPTION>");
	//}
}

//COMBO TIMEBANDS
TimeBandList timebandslist = new TimeBandList(null,BaseConfig.getPlantId(),new Integer(idsite));
SortedMap sortedtimebandslist = new TreeMap();
Integer[] tmpidstimeband = timebandslist.getIds();
TimeBandBean tmptimeband = null;
for (int i=0;i<timebandslist.size();i++)
{
	tmptimeband = timebandslist.get(tmpidstimeband[i]);	
	sortedtimebandslist.put(tmptimeband.getTimecode(),tmptimeband.getIdtimeband());
}


String sel_time = "";

StringBuffer comboTimebands = new StringBuffer();
comboTimebands.append("<OPTION value='0'>--------------</OPTION>");
//TimeBandBean tmptimeband = null;
Integer[] idstimeband = timebandslist.getIds();

for (int i=0;i<timebandslist.size();i++)
{	
	String keyTime = (String) (sortedtimebandslist.firstKey());
	Integer idtime = (Integer) (sortedtimebandslist.get(keyTime));
	tmptimeband = timebandslist.get(idtime);
	if (tmptimeband.getIdtimeband().intValue()==Integer.parseInt(aux_time))
		sel_time = "selected";
	else
		sel_time = "";
	comboTimebands.append("<OPTION "+sel_time+" value='"+tmptimeband.getIdtimeband()+"'>"+tmptimeband.getTimecode()+"</OPTION>");
	sortedtimebandslist.remove(keyTime);
	
}


//COMBO AZIONI
String sel_act = "";
ActionBeanList actionlist = new ActionBeanList(idsite, language, isScheduled);
StringBuffer comboActions = new StringBuffer();
comboActions.append("<OPTION value='0'>--------------</OPTION>");
Map actions = actionlist.getActionCodeMap();

SortedMap sortedaction = new TreeMap();
Iterator iteraction = actions.keySet().iterator();
while (iteraction.hasNext())
{
	int k=((Integer)iteraction.next()).intValue();
	String des=actionlist.getDescription(idsite,k);
	sortedaction.put(des,new Integer(k));
	
}

//Iterator iter = actions.keySet().iterator();
Iterator iter = sortedaction.keySet().iterator();
String[] tmp = null;

while (sortedaction.size()>0)
{	
		
	String d =(String) sortedaction.firstKey();
	int key=((Integer) sortedaction.get(d)).intValue();
	
	//Show also action from Rule Editor
	//if(key>=0){
		tmp = actionlist.getActiontypeByActionCode(idsite, key);
	
	    if (actionlist.countActionByActioncode(idsite, key) == 1)
	    {
	    	if (!(tmp[0]).equals("X"))
	    	{
				if (key==Integer.parseInt(aux_action))
					sel_act = "selected";
				else
					sel_act = "";
		    
				comboActions.append("<OPTION "+sel_act+" value='"+key+"'>"+actionlist.getDescription(idsite,key)+"</OPTION>");
			}
		}
		else
		{
			if (key==Integer.parseInt(aux_action))
					sel_act = "selected";
			else
				sel_act = "";		    
			comboActions.append("<OPTION "+sel_act+" value='"+key+"'>"+actionlist.getDescription(idsite,key)+"</OPTION>");
		}
	//}
		
	sortedaction.remove(d);
	
}	

String chkdbl = sessionUser.getPropertyAndRemove("alrschedchkdb");
String msgdouble = lan.getString("alrsched","chkdbl");

String isRemoved = sessionUser.getPropertyAndRemove("isremoved");
String notRemoved = lan.getString("alrsched","notRemoved");
boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
%>
<INPUT type="hidden" id='chkdbl' value='<%=chkdbl%>'/>
<INPUT type="hidden" id='msgdouble' value='<%=msgdouble%>'/>
<INPUT type="hidden" id='confirmruledel' value='<%=confirmruledel%>'/>
<INPUT type="hidden" id='noremrulefromide' name='noremrulefromide' value='<%=noremrulefromide%>'/>
<INPUT type="hidden" id='noaction' name='noaction' value='<%=noaction%>'/>
<INPUT type="hidden" id='nodesc' name='nodesc' value='<%=nodesc%>'/>
<INPUT type="hidden" id='nocond' name='nocond' value='<%=nocond%>'/>
<INPUT type="hidden" id='notimeband' name='notimeband' value='<%=notimeband%>'/>
<input type="hidden" id="rule_removed" value="<%=isRemoved%>"/>
<input type="hidden" id="not_removed" value="<%=notRemoved%>"/>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<p class="standardTxt"><%=comment%></p>
<table border="0" width="100%" height="90%" cellspacing="1" cellpadding="1">
<tr height="100%" valign="top" id="trRuleList">
		<td><%=ruletable%></td>
</tr>
<tr>
	<td>
		<FIELDSET class='field'>
<LEGEND class="standardTxt"><%=rule%></LEGEND>
<FORM id="frm_rules" name="frm_rules" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<INPUT type="hidden" id='cmd' name='cmd' value="<%=cmd%>"/>
<INPUT type="hidden" id='idrule' name='idrule' value="<%=s_idrule%>"/>
<TABLE border="0">
	<TR class="standardTxt">
		<TD><%=description%></TD>
		<TD class="standardTxt" colspan='2'><INPUT <%=perm%> <%=disabIdeRule%> id='description_txt' name='description_txt' class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" type="text" size="30" maxlength="30" style='width:200;' value='<%=aux_desc%>' onblur="noBadCharOnBlur(this,event);" onkeydown='checkBadChar(this,event);'/> *</TD>
		<TD><%=enabled%></TD>
		<TD><INPUT <%=perm%> type='checkbox' <%=aux_enabled%> id='enabled' name='enabled'></TD>
	</TR>
	<TR height="6"><TD>&nbsp;</TD></TR>
	<TR class="standardTxt">
		<TD><%=condition%> </TD>
		<TD class="standardTxt"><SELECT <%=perm%> <%=disabIdeRule%> id='idcondition' name='idcondition' class="standardTxt" style='width:200;'><%=comboConditions.toString()%></SELECT> *</TD>
		<TD width="20">&nbsp;</TD>
		<TD><%=timebands%> </TD>
		<TD class="standardTxt"><SELECT <%=perm%> <%=disabIdeRule%> id='idtimeband' name='idtimeband' class="standardTxt" style='width:200;'><%=comboTimebands.toString()%></SELECT> *</TD>
		<TD width="20">&nbsp;</TD>
		<TD><%=action%> </TD>
		<TD class="standardTxt"><SELECT <%=perm%> <%=disabIdeRule%> id='idaction' name='idaction' class="standardTxt" style='width:200;'><%=comboActions.toString()%></SELECT> *</TD>
	</TR>
	<TR height="6"><TD>&nbsp;</TD></TR>
	<TR class="standardTxt">
		<TD><%=delay%>:</TD>
		<TD><INPUT <%=perm%> <%=disabIdeRule%> id='delay' name='delay' class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" type="text" size="5" maxlength="5" value='<%=aux_delay%>' onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/> <%=minutes%></TD>
	</TR>
</TABLE>
</FORM>
</FIELDSET>
	</td>
</tr>
</table>


	

