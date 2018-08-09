<%@ page language="java"  pageEncoding="UTF-8"
import="com.carel.supervisor.presentation.session.* "
import="com.carel.supervisor.presentation.helper.*"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.dataaccess.dataconfig.ProductInfo"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.dataaccess.dataconfig.SystemConfMgr"
import="java.io.*"
import="com.carel.supervisor.presentation.bo.helper.GuardianHelper"
import="com.carel.supervisor.director.guardian.CheckersMgr"
import="com.carel.supervisor.base.config.BaseConfig" 
import="com.carel.supervisor.presentation.assistance.GuardianConfig"
import="com.carel.supervisor.base.config.ProductInfoMgr"
%>
<%
String path = request.getContextPath();
UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
String jsession = request.getSession().getId();

String language =  sessionUser.getLanguage();
LangService langmgr = LangMgr.getInstance().getLangService(language);

String whereIsGu = SystemConfMgr.getInstance().get("machine").getValue();
boolean yes = false;

if(whereIsGu != null)
{
	if(whereIsGu.equalsIgnoreCase("localhost") || whereIsGu.equalsIgnoreCase("127.0.0.1"))
		yes = true;
}
String listOptions = "";
String listOptionsSave = "";

if(yes) {
	listOptions = GuardianConfig.getOptionList();
	listOptionsSave = GuardianConfig.getOptionListConf();
}
String gsn = GuardianConfig.getGardianSignal();
if(gsn == null)
	gsn = "checked";
else
	gsn = "";	
String doubleElement = langmgr.getString("dbllistbox","doublelement");
String nullselected = langmgr.getString("dbllistbox","nullselected");
String disableDays = langmgr.getString("vs","reqfields");

// Alessandro : aggiunto codice per impostare la classe css per la tastiera virtuale
boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
//String cssVirtualKeyboardClass = (OnScreenKey ? ','+VirtualKeyboard.getInstance().getCssClass() : "");	


String cmd = sessionUser.getPropertyAndRemove("cmd");
if (cmd==null) cmd="";
String iddevTmp = sessionUser.getPropertyAndRemove("guard_iddev");
int iddev = 0;
if ((null != iddevTmp) && (!"".equals(iddevTmp)))
{
	iddev = Integer.parseInt(iddevTmp);
}

String combo = GuardianHelper.createCombo(language, iddev);
String varOptions = "";
String varOptionsSave = "";
String params = sessionUser.getPropertyAndRemove("params");
if (params==null) params ="";
varOptions = GuardianHelper.getOptionList(language, iddev,  sessionUser);
if (!cmd.equals("reload"))
{
	varOptionsSave = GuardianHelper.getOptionListConf(language,iddev,sessionUser);
}
else
{
	varOptionsSave = GuardianHelper.getOptionListConfReload(language,iddev,params,sessionUser);
}
String msgremember = langmgr.getString("mgr","msgrem5");

String alarm_check = SystemConfMgr.getInstance().get("checkalarm").getValue();
String checked = alarm_check.equals("TRUE")?"checked":"";

/*
 *	Guardian Snooze
 */
String snChecked = ""; 
String daySnooze = ProductInfoMgr.getInstance().getProductInfo().get("gsnooze");
if(daySnooze != null)
	snChecked = "checked";
else 
	daySnooze = "";

/*
 *	Guardian Variables K
 */
String gvk = ProductInfoMgr.getInstance().getProductInfo().get("gvark");
if(gvk == null)
	gvk = "checked";
else
	gvk = "";	

/*
 *	Check SSD
 */
String gssd = ProductInfoMgr.getInstance().getProductInfo().get("gssd");
String ssdChecked = gssd != null && gssd.equals("true") ? "checked" : "";

//2010-6-2, byKevin, to identify from load or save
String saveclick = sessionUser.getPropertyAndRemove("saveclick");
if(saveclick != null)
{
	saveclick = "true";
}
else
{
	saveclick = "";
}
%>

<input type="hidden" id="msgermember" value="<%=msgremember%>">
<input type="hidden" name="nullselected" id="nullselected" value="<%=nullselected%>"/>
<input type="hidden" name="doubleElement" id="doubleElement" value="<%=doubleElement%>"/>
<input type="hidden" id="disableDays" value="<%=disableDays%>"/>
<input type="hidden" id="saveclick" value="<%=saveclick %>"/>
<input type='hidden' id='restartengine' value='<%=langmgr.getString("wizard","restartengine")%>'/>
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>

<table border="0" width="100%" cellpadding="1" cellspacing="1" height="1">
<tr height="1%">
	<td>
		<p class='standardTxt'><%=langmgr.getString("guardian","page_comment")%></p>
	</td>
</tr>

<tr>
	<td>
			<form id="frm_guardian" action="servlet/master;jsessionid=<%=jsession%>" method="post">
			<input type='hidden' id='params' name='params' value='' />
			<input type="hidden" name="guard_iddev" id="guard_iddev" value="<%=iddev%>"/>
			<input type="hidden" name="values" id="values" />
			<input type="hidden" name="cmd" id="cmd" />
		    
			<table border="0" width="99%" cellpadding="1" cellspacing="1" height="100%">
				<tr height="10%">
			  	  <td align="left">
			  	  
			  	  	<fieldset class="field">
			  	  		<legend class="standardTxt"><b><%=langmgr.getString("guardian","guardianstatus")%></b></legend>
			        	<table border="0" cellpadding="1" cellspacing="1" width="90%">
					    <tr>
				      		<td class='standardTxt'><%=langmgr.getString("mgr","guardianstatus")%></td>
				      		<% boolean isRunning = CheckersMgr.getInstance().isGuardianRunning();%>
				  			<td class='standardTxt' <%=(isRunning ? "style='color:GREEN;'":"style='color:RED;'")%> >
								<b><%=(isRunning ? langmgr.getString("mgr","guardianstatusok"): langmgr.getString("mgr","guardianstatusko"))%></b>
				  			</td>
				      		<td>&nbsp;&nbsp;&nbsp;</td>
				      		<td class='standardTxt'><input type='checkbox' name="szac" id="szac"  <%=snChecked%> ></td>
				      		<td class='standardTxt'>
				      			<%=langmgr.getString("guardian","snoozemap")%> <input <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%>  onblur="onlyNumberOnBlur(this);"  onkeydown='checkOnlyNumber(this,event);' type="text" size="2" maxlength="2" name="szdd" id="szdd" value="<%=daySnooze%>">
				      		</td>
				        </tr>
			            </table>			  	  		
			  	  	</fieldset>
			  	  </td>
			  	 </tr>
				 <tr><td><p class="standardTxt" style="height:10px;"/></td></tr>

				<tr>
					<td height=1>
					<%if(yes) {%>
						<fieldset class="field">
						<legend class="standardTxt"><b><%=langmgr.getString("guardian","notifyconf")%></b></legend>
							<table border="0" width="100%" height="100%" cellspacing="1" cellpadding="1">
								<tr height="2%">
									<td colspan="5">
										<table border="0" cellpadding="1" cellspacing="1">
											<tr>
												<td class="standardTxt"><input type='checkbox' name="gch" id="gch" onclick="" value="0" <%=gsn%>></td>
						        				<td class="standardTxt"><%=langmgr.getString("guardian","actchannel")%></td>
											</tr>
										</table>
									</td>
								</tr>
								
								<tr valign="top" height="2%">
									<td width="45%" class='th' align='center'><b><%=langmgr.getString("setio","book")%></b></td>
									<td width="10%"></td>
									<td width="45%" class='th' align='center'><b><%=langmgr.getString("guardian","list")%></b></td>
								</tr>
								
								<tr valign="top" height="*">
									<td width="45%">
										<select class='selectB' multiple size="8" name="list1" id="list1" ondblclick="multipleto2(list1,null);">
											<%=listOptions%>
										</select>
									</td>
									<td valign="middle" align="center" width="10%">
										<table width="100%" cellpadding="0" cellspacing="0" border="0">
											<tr>
												<td align="center">
													<img onclick="multipleto2(list1,null);return false;" src="images/dbllistbox/arrowdx_on.png"/>
												</td>
											</tr>
											<tr>
												<td><p class="standardTxt">&nbsp;</p></td>
											</tr>
											<tr>
												<td align="center">
													<img onclick="multipleto1(list1,null);return false;" src="images/dbllistbox/arrowsx_on.png"/>
												</td>
											</tr>
										</table>
									</td>
									<td width="45%">
										<select class='selectB' multiple size="8" name="list2" id="list2" ondblclick="multipleto1(list1,null);">
											<%=listOptionsSave%>
										</select>
									</td>
								</tr>
							</table>		
						</fieldset>
						
						<div id="guiPostDiv" style="visibility:hidden;display:none;"></div>
						
					<%}else{%>
						<table width="100%" height="100%" border="0" cellspacing="1" cellpadding="1">
							<tr valign="top" height="100%">
								<td>GUARDIAN is on another computer.</td>
							</tr>
						</table>
					<%}%>
					</td>
				</tr>
				<tr><td><p class="standardTxt" style="height:10px;"/></td></tr>
			    <tr>
			  	  	<td height=1>			    
			    	<fieldset class="field">
		                <table border="0" width="100%" height="100%" cellspacing="1" cellpadding="1">
		                	<tr height="2%">			    	
	      						<td class='standardTxt' width="20%">
	      							<input <%=checked%> name='check_ver_alarm' id='check_ver_alarm' class='standardTxt' type='checkbox' value='a'>
	      							<%=langmgr.getString("mgr","enablealarm")%>
	      						</td>
					      		<td class='standardTxt' width="20%" style="display:none;">
					      			<input type='checkbox' name="gssd" id="gssd"  <%=ssdChecked%> >
					      			<%=langmgr.getString("guardian", "check_ssd")%>
					      		</td>
					      		<td width="*">&nbsp;</td>
	      					</tr>
	      				</table>
	      			</fieldset>
	      			</td>	    
			    </tr>
			    <tr><td><p class="standardTxt" style="height:10px;"/></td></tr>
			    <tr>
			  	  <td height=1>
			  		<fieldset class="field">
			  			<legend class="standardTxt"><b><%=langmgr.getString("guardian","varconf")%></b></legend>
			                <table border="0" width="100%" height="100%" cellspacing="1" cellpadding="1">
			                	<tr height="2%">
						  			<td class="standardTxt" colspan="3"><input type='checkbox' name="gvk" id="gvk" onclick="" value="0" <%=gvk%>><span style="margin-left:10px"><%=langmgr.getString("guardian","actsonde")%></span></td>
						  		</tr>				                
								<tr height="2%">
									<td colspan="3">
										<table border="0" cellpadding="1" cellspacing="1">
											<tr>
												<td width="50%"><%=combo%></td>
											</tr>
										</table>
									</td>
								</tr>
								
								<tr valign="top" height="2%">
									<td width="45%" class='th' align='center'><b><%=langmgr.getString("guardian","header1")%></b></td>
									<td width="10%"></td>
									<td width="45%" class='th' align='center'><b><%=langmgr.getString("guardian","header2")%></b></td>
								</tr>
								
								<tr valign="top" height="*">
									<td width="45%">
										<select class='selectB' multiple size="8" name="variable1" id="variable1" ondblclick="multipleto2(variable1,combo);">
											<%=varOptions%>
										</select>
									</td>
									<td valign="middle" align="center" width="10%">
										<table width="100%" cellpadding="0" cellspacing="0" border="0">
											<tr>
												<td align="center">
													<img onclick="multipleto2(variable1,combo);return false;" src="images/dbllistbox/arrowdx_on.png"/>
												</td>
											</tr>
											<tr>
												<td>&nbsp;</td>
											</tr>
											<tr>
												<td align="center">
													<img onclick="multipleto1(variable1,combo);return false;" src="images/dbllistbox/arrowsx_on.png"/>
												</td>
											</tr>
										</table>
									</td>
									<td width="45%">
										<select class='selectB' multiple size="8" name="variable2" id="variable2" ondblclick="multipleto1(variable1,combo);">
											<%=varOptionsSave%>
										</select>
									</td>
								</tr>
					        <tr>
								<td colspan=3>
									<table align="center" border="0" width="100%" cellpadding="2" cellspacing="0" >
						      	      <tr>
								      <td width="25%" class='standardTxt' align='right'><%=langmgr.getString("guardian","varping")%></td>
								      <td width="25%">
									    	<input maxlength="3" size='5' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> type="text" name="varping" id="varping" value="<%=(int)(SystemConfMgr.getInstance().get("varping").getValueNum()/60)%>" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/>
								      </td>
					                  <td width="40%" class='standardTxt' align='right'><%=langmgr.getString("guardian","errorping")%></td>
								      <td width="10%">
									    	<input <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> size='5' type="text" name="errorping" id="errorping" value="<%=(int)SystemConfMgr.getInstance().get("errorping").getValueNum()%>" onkeydown="checkOnlyNumber(this,event);" onblur="onlyNumberOnBlur(this);"/>
								      </td>
						              </tr>
					                </table>
								</td>
					        </tr>
							</table>
			        </fieldset>
				  </td>
			    </tr>
 
			</table>
			<input type='hidden' name='error_cmd' />
		</form>

	</td>
</tr>
</table>


	
