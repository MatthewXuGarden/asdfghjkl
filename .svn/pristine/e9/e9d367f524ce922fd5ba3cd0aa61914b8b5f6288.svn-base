<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.datalog.impl.ConditionBeanList"
	import="com.carel.supervisor.presentation.bean.rule.ConditionBeanListPres"
	import="com.carel.supervisor.presentation.bean.ProfileBean"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUserLoc = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	int idSite = sessionUser.getIdSite();
	String jsession = request.getSession().getId();
	LangService multiLanguage = LangMgr.getInstance().getLangService(language);
    LangService lan = LangMgr.getInstance().getLangService(language);
   
	//add by kevin
	if(sessionUser.getProperty("hcondgecmd") != null && sessionUser.getProperty("hcondgeid") != null &&
			sessionUser.getPropertyAndRemove("fromdashboard")!= null)
	{
		trxUserLoc.getBoTrx().executePostAction(sessionUser,"tab5name",sessionUser.getProperties());
	}
//controllo permessi	
	String perm = "";
	int permission = sessionUser.getVariableFilter();
	if (permission==0)
		perm = "disabled";
	
	boolean permict = (permission==0)?false:true;
		
	String conditioncomment1 = lan.getString("alrsched","conditioncomment1");
	String noremcondfromide = lan.getString("ide","noremcondfromide");
	String nomodcondfromide = lan.getString("ide","nomodcondfromide");
	String confirmconddel = lan.getString("alrsched","confirmconddel");
	
	String requiredfield = lan.getString("alrsched","requiredfield");
	String setrequiredvar = lan.getString("alrsched","setrequiredvar");
	String setcostantreq = lan.getString("alrsched","setcostantreq");
	String setoperationreq = lan.getString("alrsched","setoperationreq");
	String consterror = lan.getString("alrsched","consterror");
	
	
	String actcmd = trxUserLoc.remProperty("hcondgecmd");
	String curselcond = trxUserLoc.remProperty("hcondgeid");
	String curdevice = trxUserLoc.remProperty("hcondgecurdev");
	String curvariable = trxUserLoc.remProperty("hcondgecurvar");
	String curdevicefun = trxUserLoc.remProperty("hcondgecurdevfun");
	String curvariablefun = trxUserLoc.remProperty("hcondgecurvarfun");
	String curdescription = trxUserLoc.remProperty("conddesc");
	String curvalanal = trxUserLoc.remProperty("condgecostva");
	String curvaldigi = trxUserLoc.remProperty("condgecostvd");
	String disablecondgedev2 = trxUserLoc.remProperty("disablecondgedev2");
	
	if(curvaldigi.length() == 0)
		curvaldigi = "0";
	
	String curtype = trxUserLoc.remProperty("condgetype");
	if(curtype.length() == 0)
		curtype = "1";
	
	String isdigit = trxUserLoc.remProperty("hcondgeisdigit");
	if(isdigit.length() == 0)
		isdigit = "F";
	
	String curfunc = trxUserLoc.remProperty("hcondgecurfun");
	if(curfunc.length() == 0)
		curfunc = "0";	
		
	ConditionBeanListPres condBean = new ConditionBeanListPres(idSite,language);
	condBean.setScreenH(sessionUser.getScreenHeight());
	condBean.setScreenW(sessionUser.getScreenWidth());
	condBean.loadGeneralConditions();
	
	String htmltable = condBean.getTableConditionGen(multiLanguage,permict);
	String optselect = condBean.getTypeSelect("3",curdevice);
	String optselect2 = condBean.getTypeSelect("3","");
	String optvars = condBean.getAllNotAlarmVars(curdevice,curvariable,false,isdigit);
	String optvars2 = optvars;
	
	if(actcmd.equalsIgnoreCase("devfun") || actcmd.equalsIgnoreCase("get")) {
		optselect2 = condBean.getTypeSelect("3",curdevicefun);
		optvars2 = condBean.getAllNotAlarmVars(curdevicefun,curvariablefun,true,isdigit);
	}
	
	String s_removed = trxUserLoc.remProperty("notremovecond").toString();
	if (s_removed==null) s_removed = "";
	String conditionnotremoved = lan.getString("alrsched","conditionnotremoved");
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey(); 	
	
%>
<input type="hidden" id="conditionnotremoved" value="<%=conditionnotremoved%>"/> 
<input type="hidden" id="s_removed" value="<%=s_removed%>"/> 
<input type="hidden" id="confirmconddel" value="<%=confirmconddel%>"/> 
<input type="hidden" id="duplicatecodemsg" value="<%=lan.getString("alrsched","msgduplicate")%>">
<input type="hidden" id="noremcondfromide" value="<%=noremcondfromide%>"/>
<input type="hidden" id="nomodcondfromide" value="<%=nomodcondfromide%>"/>
<input type="hidden" id="requiredfield" value="<%=requiredfield%>"/>
<input type="hidden" id="setrequiredvar" value="<%=setrequiredvar%>"/>
<input type="hidden" id="setcostantreq" value="<%=setcostantreq%>"/>
<input type="hidden" id="setoperationreq" value="<%=setoperationreq%>"/>
<input type="hidden" id="consterror" value="<%=consterror%>"/>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<form name="frmcondge" id="frmcondge" method="post" action="servlet/master;jsessionid=<%=jsession%>">
<input type="hidden" name="hcondgecmd" id="hcondgecmd" value="<%=actcmd%>"/>
<input type="hidden" name="hcondgeid" id="hcondgeid" value="<%=curselcond%>"/>
<input type="hidden" name="hcondgecurdev" id="hcondgecurdev" value="<%=curdevice%>"/>
<input type="hidden" name="hcondgecurvar" id="hcondgecurvar" value="<%=curvariable%>"/>
<input type="hidden" name="hcondgecurdevfun" id="hcondgecurdevfun" value="<%=curdevicefun%>"/>
<input type="hidden" name="hcondgecurvarfun" id="hcondgecurvarfun" value="<%=curvariablefun%>"/>
<input type="hidden" name="hcondgeisdigit" id="hcondgeisdigit" value="<%=isdigit%>"/>
<input type="hidden" name="hcondgecurfun" id="hcondgecurfun" value="<%=curfunc%>"/>
<input type="hidden" id="permict" value="<%=(permict)?"true":"false"%>"/>
<p class="StandardTxt"><%=conditioncomment1%></p>
<table border="0" height="87%" width="100%" cellspacing="1" cellpadding="1">
	<tr height="100%" valign="top" id="trCondEvn">
		<td><%=htmltable%></td>
	</tr>
	<tr>
		<td style="height:10px"></td>
	</tr>
	<tr>
		<td>
			<fieldset class="field" style='width:97.35%;'>
				<legend class="standardTxt"><%=multiLanguage.getString("alrsched","cond")%></legend>
				<table width="100%" border="0">
					<tr>
						<td class="standardTxt" width="8%"><%=multiLanguage.getString("alrsched","desc")%>:</td>
						<td width="35%">
							<input <%=perm%> class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" type="text" name="conddesc" id="conddesc" style="width:300px;" maxlength="30" value="<%=curdescription%>" onblur="noBadCharOnBlur(this,event);" onkeydown="checkBadChar(this,event);"> *
						</td>
						<td width="8%">&nbsp;</td>
						<td width="*">&nbsp;</td>
					</tr>

					<tr><td style="height:10px" colspan="4"></td></tr>
					<tr>
						<td class="standardTxt"><%=multiLanguage.getString("alrsched","dev")%>:</td>
						<td>	
							<select <%=perm%> id="condgedev" name="condgedev" class="standardTxt" style="width:300px;" onchange="changeDeviceGen(this);">
								<%=optselect%>
							</select>&nbsp;*
						</td>
					
						<td class="standardTxt"><%=multiLanguage.getString("alrsched","var")%>:</td>
						<td>
							<select <%=perm%> id="condgevar" name="condgevar" class="standardTxt" style="width:300px;" onchange="changeVariableGen(this);">
								<%=optvars%>
							</select>&nbsp;*
						</td>
					</tr>

					<tr>
						<td style="height:10px" colspan="4"></td>
					</tr>
					<tr>
						<td class="standardTxt" ><%=multiLanguage.getString("alrsched","ope")%>:</td>
						<td colspan="2">
							<table cellspacing="0">
								<tr>
									<td>
										<div id="condgetdivoptd" style="visibility:hidden;display:none;">
											<select <%=perm%> id="idcondgeoptdigit" name="condgeoptdigit" class="standardTxt">
												<option value="0" <%=(curfunc.equalsIgnoreCase("0")?"selected":"")%>>----------</option>
												<option value="AND" <%=(curfunc.equalsIgnoreCase("AND")?"selected":"")%>>AND</option> 
												<option value="OR" <%=(curfunc.equalsIgnoreCase("OR")?"selected":"")%>>OR</option>
												<option value="EQ" <%=(curfunc.equalsIgnoreCase("EQ")?"selected":"")%>>EQUALS</option>
											</select>
										</div>
										<div id="condgetdivopt" style="visibility:visible;display:block;">
											<select <%=perm%> id="idcondgeoptanal" name="condgeoptanal" class="standardTxt">
												<option value="0" <%=(curfunc.equalsIgnoreCase("0")?"selected":"")%>>----------</option>
												<option value="GR" <%=(curfunc.equalsIgnoreCase("GR")?"selected":"")%>>></option> 
												<option value="LS" <%=(curfunc.equalsIgnoreCase("LS")?"selected":"")%>><</option>
												<option value="EQ" <%=(curfunc.equalsIgnoreCase("EQ")?"selected":"")%>>=</option>
											</select>
										</div>
									</td>
									<td width="40" class="standardTxt"><%=multiLanguage.getString("alrsched","on")%>:</td>
									<td width="400" class="standardTxt">
										<table width="95%" border="0" cellspacing="1" cellpadding="1">
											<tr>
												<td class="standardTxt"><input <%=perm%> onclick="changeGeType(this);" class="standardTxt" type="radio" value="1" name="condgetype" id="condgetypev" <%=(curtype.equals("1")?"checked":"")%>/><%=multiLanguage.getString("alrsched","var")%></td>
												<td class="standardTxt"><input <%=perm%> onclick="changeGeType(this);" class="standardTxt" type="radio" value="5" name="condgetype" id="condgetypec" <%=(curtype.equals("5")?"checked":"")%>/><%=multiLanguage.getString("alrsched","kost")%></td>
												<td>
													<div id="condgedivcosta" align="left" >
														<input <%=perm%> class="<%=(OnScreenKey?"keyboardInput":"standardTxt")%>" type="text" size="10" maxlength="10" name="condgecostva" id="condgecostva" value="<%=curvalanal%>" onblur="isNumeric();"/> 
													</div>
													<div id="condgedivcostd" align="left" >
														<select <%=perm%> name="condgecostvd" id="condgecostvd" class="standardTxt">
															<option value="0" <%=(curvaldigi.equalsIgnoreCase("0.0")?"selected":"")%>>0</option>
															<option value="1" <%=(curvaldigi.equalsIgnoreCase("1.0")?"selected":"")%>>1</option> 
														</select>
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td style="height:10px" colspan="4">
					</tr>
					<tr>
							<div id="condgedivvar">

									<td class="standardTxt"><%=multiLanguage.getString("alrsched","dev")%>:</td>
									<td>
										<select <%=perm%> <%=disablecondgedev2%> id="condgedev2" name="condgedev2" class="standardTxt" style="width:300px;" onchange="changeDeviceGenFun(this);">
											<%=optselect2%>
										</select>
									</td>
								
									<td class="standardTxt"><%=multiLanguage.getString("alrsched","var")%>:</td>
									<td>
										<select <%=perm%> <%=disablecondgedev2%> id="condgevar2" name="condgevar2" class="standardTxt" style="width:300px;" onchange="">
											<%=optvars2%>
										</select>
									</td>
							</div>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>
</form>