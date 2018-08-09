<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.datalog.impl.ConditionBeanList"
	import="com.carel.supervisor.presentation.bean.rule.ConditionBeanListPres"
	import="com.carel.supervisor.presentation.bean.ProfileBean"
	import="com.carel.supervisor.dataaccess.db.RecordSet"
	import="com.carel.supervisor.dataaccess.db.DatabaseMgr"
	import="com.carel.supervisor.dataaccess.datalog.impl.*"
	import="java.util.ArrayList"
	import="java.util.List"
	import="com.carel.supervisor.presentation.dbllistbox.*"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	import="com.carel.supervisor.presentation.bean.*"
%>
<%

	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUserLoc = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	int idSite = sessionUser.getIdSite();
	String jsession = request.getSession().getId();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
  	LangService lan = LangMgr.getInstance().getLangService(language);
	String alarmconditioncomment = lan.getString("alrsched","alarmconditioncomment");
	String noremcondfromide = lan.getString("ide","noremcondfromide");
	String nomodcondfromide = lan.getString("ide","nomodcondfromide");
	String allfields = lan.getString("alrsched","allfields");
	String variabledouble = lan.getString("alrsched","variabledouble");
	String confirmconddel = lan.getString("alrsched","confirmconddel");
	
	//add by kevin
	if(sessionUser.getProperty("hcondactcmd") != null && sessionUser.getProperty("hcondid") != null &&
			sessionUser.getPropertyAndRemove("fromdashboard")!= null)
	{
		trxUserLoc.getBoTrx().executePostAction(sessionUser,"tab2name",sessionUser.getProperties());
	}
	//controllo permessi	
	String perm = "";
	int permission = sessionUser.getVariableFilter();
	if (permission==0)
		perm = "disabled";
	
	boolean permict = (permission==0)?false:true;
	
	 
	String actcommand = trxUserLoc.remProperty("hcondactcmd");
	String desccondition = trxUserLoc.remProperty("hconddesc");
	if(desccondition != null)
		desccondition = desccondition.trim();
		
	String command = trxUserLoc.remProperty("hcondcmd");
	String type = trxUserLoc.remProperty("hcondtype");
	String combo = trxUserLoc.remProperty("hcombovalue");
	String data = trxUserLoc.remProperty("hconddata");
	String idcond = "";
	if(type.equals("3"))//if it is variable, keep hcondid in user transaction for Lsw2
		idcond = trxUserLoc.getProperty("hcondid");
	else//if it is not variable(all alarms or priority), remove hcondid from transaction
		idcond = trxUserLoc.remProperty("hcondid");

//Start alarmP 2007/06/27 Fixing
String[] check = new String[]{"","","",""};

if(type.equals("6")&&(!idcond.equals("")))
{
	String sql="select idvariable from cfvarcondition where idcondition=?";
	RecordSet rs = DatabaseMgr.getInstance().executeQuery(null,sql,new Object[]{new Integer(idcond)});
	
	int prioritypos = -1;
	
	if (rs!=null && rs.size()>0)
	{
		prioritypos = ((Integer)rs.get(0).get("idvariable")).intValue() - 1;
		
		if ((prioritypos>=0)&&(prioritypos<=3))
			check[prioritypos]="selected";
	}
}
//End

	if(type == null || type.length() == 0 || type.equalsIgnoreCase("0"))
		type="1";

	
	ConditionBeanListPres condBean = new ConditionBeanListPres(idSite,language);
	condBean.loadAlarmConditions();
	condBean.setScreenH(sessionUser.getScreenHeight());
	condBean.setScreenW(sessionUser.getScreenWidth());
	String htmltable = condBean.getTableCondition(multiLanguage,permict);
	
	String optselect = condBean.getTypeSelect(type,combo);
    
    DevMdlBeanList devmdllist = new DevMdlBeanList();
	DevMdlBean[] devmdl = devmdllist.retrieveDevMdl(idSite,language);
	StringBuffer combodev = new StringBuffer();
	combodev.append("<option value='-1'>-------------------</option>");
	String ss="";
	for (int i=0;i<devmdl.length;i++)
	{
		DevMdlBean tmp = devmdl[i];
		combodev.append("<option value="+tmp.getIddevmdl()+" "+ss+">"+tmp.getDescription()+"</option>\n");
	}
	
	String s_removed = trxUserLoc.remProperty("notremovecond").toString();
	if (s_removed==null) s_removed = "";
	String conditionnotremoved = lan.getString("alrsched","conditionnotremoved");
	String defaultConditionRemove = lan.getString("alrsched","defaultConditionRemove");
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey(); 	
	
%>
<input type="hidden" id="defaultConditionRemove" value="<%=defaultConditionRemove%>"/> 
<input type="hidden" id="conditionnotremoved" value="<%=conditionnotremoved%>"/> 
<input type="hidden" id="s_removed" value="<%=s_removed%>"/> 
<input type="hidden" id="confirmconddel" value="<%=confirmconddel%>"/>
<input type="hidden" id="duplicatecodemsg" value="<%=lan.getString("alrsched","msgduplicate")%>">
<input type="hidden" id="variabledouble" value="<%=variabledouble%>"/>
<input type="hidden" id="allfields" value="<%=allfields%>"/>
<input type="hidden" id="noremcondfromide" value="<%=noremcondfromide%>"/>
<input type="hidden" id="nomodcondfromide" value="<%=nomodcondfromide%>"/>
<form name="frmcond" id="frmcond" method="post" action="servlet/master;jsessionid=<%=jsession%>">
<input type="hidden" name="hcondactcmd" id="hcondactcmd" value="<%=actcommand%>"/>
<input type="hidden" name="hcondvariabili" id="hcondvariabili" value=""/>
<input type="hidden" name="hcondid" id="hcondid" value="<%=idcond%>"/>
<input type="hidden" name="hcondcmd" id="hcondcmd" value="nop"/>
<input type="hidden" name="hcondtype" id="hcondtype" value="<%=type%>"/>
<input type="hidden" name="hconddesc" id="hconddesc" value=""/>
<input type="hidden" name="hcombovalue" id="hcombovalue" value=""/>
<input type="hidden" name="hconddata" id="hconddata" value=""/>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<table border="0" width="100%" cellspacing="1" cellpadding="1">
	<TR>
		<TD>
			<p class="StandardTxt"><%=alarmconditioncomment%></p>
		</TD>
	</TR>
	
	<tr>
		<td><%=htmltable%></td>
	</tr>

	<tr height="5px"><td/></tr>
	<tr>
		<td>
			<fieldset class='field'>
			
				<legend class="standardTxt"><%=multiLanguage.getString("alrsched","cond")%></legend>

				<table border="0" width="100%" cellspacing="5" cellpadding="1">
					<tr>
						<td class="standardTxt" width="10%"><%=multiLanguage.getString("alrsched","desc")%></td>
						<td class="standardTxt" width="90%"><input <%=perm%> class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" type="text" name="conddesc" id="conddesc" size="30" maxlength="30" value="<%=desccondition%>" onblur="noBadCharOnBlur(this,event);" onkeydown='checkBadChar(this,event);'/> *</td>
					</tr>
					<tr>
						<td colspan="2">
							<table border="0" cellspacing="1" cellpadding="1">
								<tr>
									<td class="standardTxt" width="5" align="center"><input <%=perm%> type="radio" name="condtype" id="condtype1" value="1" onclick="changeType(this);" <%=(type.equalsIgnoreCase("1")?"checked":"")%>/></td>
									<td class="standardTxt" width="120" align="left"><nobr><%=multiLanguage.getString("alrsched","tip1")%></nobr></td>				
									<td width="10">&nbsp;</td>
									<td class="standardTxt" width="5" align="right"><input <%=perm%> type="radio" name="condtype" id="condtype6" value="6" onclick="changeType(this);" <%=(type.equalsIgnoreCase("6")?"checked":"")%>/></td>									
									<td class="standardTxt" width="80" align="left"><nobr><%=multiLanguage.getString("alrsched","setprt")%></nobr></td>		
									<td class="standardTxt" width="140" align="left" >
											<select style='width:100px' id="priority" name="priority" class="standardTxt" 
											<%=(!type.equalsIgnoreCase("6")?"disabled=true":"")%>>
											<option value='1' <%=check[0]%>><%=multiLanguage.getString("alrview","alarmstate1")%></option>
											<option value='2' <%=check[1]%>><%=multiLanguage.getString("alrview","alarmstate2")%></option>
											<option value='3' <%=check[2]%>><%=multiLanguage.getString("alrview","alarmstate3")%></option>
											<option value='4' <%=check[3]%>><%=multiLanguage.getString("alrview","alarmstate4")%></option>
											</select>
									</td>
									<td width="10">&nbsp;</td>
									<td class="standardTxt" width="5" align="center"><input <%=perm%> type="radio" name="condtype" id="condtype3" value="3" onclick="changeType(this);" <%=(type.equalsIgnoreCase("3")?"checked":"")%>/></td>
									<td class="standardTxt" width="120" align="left"><nobr><%=multiLanguage.getString("alrsched","tip3")%></nobr></td>
								</tr>
								<tr>	
									<td class="standardTxt" width="650" align="left" colspan="9">
										<input <%=perm%> type="radio" name="condtype" id="condtype3_model" value="33" onclick="changeType(this);"/><%=multiLanguage.getString("datatransfer","device_models")%>
										&nbsp;
										<SELECT onchange='reload_actions(2)' class='standardTxt' id='model' name='model' disabled style='width:350px;'>
										<%=combodev.toString()%>
										</SELECT> 
									</td>
								</tr>
							</table>	
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table width="100%" cellpadding="0" cellspacing="1">
								<tr>
									<td  class='th' width="49%"><%=lan.getString("report","devices")%></td>
									<td  class='th' width="49%" ><%=lan.getString("report","variables")%></td>
									<th width='2%' align='right'><img id="imgAddVar" name="imgAddVar" src="images/actions/addsmall_off.png" style="cursor:pointer;" onclick="addAlarmToVarSel()"></th>
								</tr>
								<tr>
								<td>
									<div id="div_dev">
										<select name='devsel' id='devsel' size=10  class='standardTxt'  style='width:100%;'>
										</select>
									</div>
								 </td>
								<td>
									<div id="div_var">
										<select name='varsel' id='varsel' multiple size=10  class='standardTxt'  style='width:100%;'>
										</select>
									</div>
								 </td>
							 </tr>
							</table>
						</td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
	<tr height="10px"><td></td></tr>
	<tr>
		<td>
			<fieldset class="field">
				<legend class="standardTxt"><%=multiLanguage.getString("datatransfer", "curr_conf")%></legend>
					<table width="100%">
						 <tr>
						  <td width="98%">
						  		<%=condBean.getAlarmVariable(sessionUser,multiLanguage) %>
						  </td>
						  <td align="right" valign="top"><img id="imgRemoveVar" src="images/actions/removesmall_off.png" style="cursor:pointer;" onclick="removeAllAlarms()"></td>
						 </tr>
					</table>
			</fieldset>
		</td>
	</tr>
</table>
</form>

<form name="frmcondact" id="frmcondact" method="post" action="servlet/master;jsessionid=<%=jsession%>">
	
</form>