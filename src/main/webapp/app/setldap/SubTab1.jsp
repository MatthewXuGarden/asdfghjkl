<%@ page language="java"  pageEncoding="UTF-8"
import="com.carel.supervisor.presentation.helper.ServletHelper"
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.session.UserSession"
import="com.carel.supervisor.presentation.session.UserTransaction"
import="com.carel.supervisor.dataaccess.language.LangMgr"
import="com.carel.supervisor.dataaccess.language.LangService"
import="com.carel.supervisor.presentation.ldap.UsersList"
import="com.carel.supervisor.presentation.bean.ProfileBeanList"
import="com.carel.supervisor.base.config.IProductInfo"
import="com.carel.supervisor.base.config.ProductInfoMgr" 
%>
<%@ page import="com.carel.supervisor.presentation.bo.BUserProfile" %>
<%@ page import="com.carel.supervisor.presentation.ldap.DBProfiler" %>
<%@ page import="com.carel.supervisor.presentation.ldap.UsersList" %>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction trxUserLoc = sessionUser.getCurrentUserTransaction();
	String language = sessionUser.getLanguage();
	int idsite = sessionUser.getIdSite();
	LangService langService = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();
	//Stringhe per la pagina in lingua
	String user=langService.getString("ldapPage","user");
	String profile=langService.getString("ldapPage","profile");
	String password=langService.getString("ldapPage","password");
	String cpassword=langService.getString("ldapPage","cpassword");
	String noremuser=langService.getString("setldap","noremuser");
	String doubleuser = sessionUser.getPropertyAndRemove("doubleuser");
	String carel_exist = sessionUser.getPropertyAndRemove("carel_exist");
	
	String s_carel_adv= langService.getString("setldap","carel_adv");
	String s_doubleuser= langService.getString("setldap","s_doubleuser");
	String s_carel_exist= langService.getString("setldap","s_carel_exist");
	String s_nomodadminuser = langService.getString("setldap","s_nomodadminuser");
	String s_deleteuser = langService.getString("setldap","s_deleteuser");
	String s_insertname = langService.getString("setldap","s_insertname");
	String s_insertpassw = langService.getString("setldap","s_insertpassw");
	String s_confirmpassw = langService.getString("setldap","s_confirmpassw");
	String s_tooshortpassw = langService.getString("setldap","s_passwtooshort");
	String s_strictpassw = langService.getString("setldap","s_strictpassw");
	String s_selectprofile = langService.getString("setldap","s_selectprofile");
	String usercomment = langService.getString("setldap","usercomment");
	String profilecomment = langService.getString("ldapPage","profilecomment");
	
	// Alessandro : aggiunto codice per impostare la classe css per la tastiera virtuale
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	//String cssVirtualKeyboardClass = (OnScreenKey ? ','+VirtualKeyboard.getInstance().getCssClass() : "");	
	
	UsersList usersList=new UsersList();
	usersList.setTitle(user);

	usersList.loadData();
	usersList.setScreenH(sessionUser.getScreenHeight());
	usersList.setScreenW(sessionUser.getScreenWidth());
	usersList.setWidth(880);
	usersList.setHeight(130);

	
	String htmlUsersTable = usersList.getHTMLUsersTable("userTable",language,idsite);
	
	ProfileBeanList profile_list = new ProfileBeanList(sessionUser.getIdSite(),true);
	//
	//ArrayList profiles=(ArrayList)ActionLDAP.getProfileInformation().get("grn");
	StringBuffer select=new StringBuffer("\n<select  onchange='onchange_prof();' class='standardTxt' name=\"profile\" id=\"profile\" style='width:200;'>");
	
	select.append("\n<option value='-1'>----------------</option>");
	for(int i=0;i<profile_list.size();i++)
	{
		if (profile_list.getProfile(i).getIdprofile().intValue()!=-4)   //non presento profilo OFFLINE USER
		{
			select.append("\n<option value=\""+profile_list.getProfile(i).getIdprofile()+"\">"+profile_list.getProfile(i).getCode());
		}
	}		 
	select.append("</select>");
	
	String carel_prefix = DBProfiler.CAREL_PREFIX;
	
	//2009-12-22, add account policy, Kevin
	String days = langService.getString("graphvariable","rangedayslabel");
	String unblock = langService.getString("setldap","unblock");
	String standard = BUserProfile.STANDARD;
	String strict = BUserProfile.STRICT;
	String policy = "";
	String loginretry = "";
	String expirationdate = "";
	IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
	policy = product.get(BUserProfile.POLICY);
	if(policy == null || policy.equals(BUserProfile.STRICT) == false)
	{
		policy = BUserProfile.STANDARD;
	}
	if(policy != BUserProfile.STANDARD)
	{
		loginretry = product.get(BUserProfile.LOGINRETRY);
		if(loginretry == null || loginretry.equals(""))
		{
			loginretry = "0";
		}
		expirationdate = product.get(BUserProfile.EXPIRATIONDATE);
		if(expirationdate == null || expirationdate.equals(""))
		{
			expirationdate = "0";
		}
	}
	//--end
	//2009-12-30, add remote support checkbox, Kevin
	String remotesupport = product.get(BUserProfile.REMOTESUPPORT);
	if(remotesupport == null || remotesupport.equals("1") == false)
	{
		remotesupport = "0";
	}
	//-end
%>
<input type="hidden" id="carel_adv" value="<%=s_carel_adv%>"/>
<input type="hidden" id="s_carel_exist" value="<%=s_carel_exist%>"/>
<input type="hidden" id="carel_exist" value="<%=carel_exist%>"/>
<input type="hidden" id="carel_prefix" value="<%=carel_prefix%>"/>
<input type="hidden" id="s_doubleuser" value="<%=s_doubleuser%>"/>
<input type="hidden" id="s_nomodadminuser" value="<%=s_nomodadminuser%>"/>
<input type="hidden" id="s_deleteuser" value="<%=s_deleteuser%>"/>
<input type="hidden" id="s_insertname" value="<%=s_insertname%>"/>
<input type="hidden" id="s_insertpassw" value="<%=s_insertpassw%>"/>
<input type="hidden" id="s_confirmpassw" value="<%=s_confirmpassw%>"/>
<input type="hidden" id="s_tooshortpassw" value="<%=s_tooshortpassw%>" />
<input type="hidden" id="s_strictpassw" value="<%=s_strictpassw%>" />
<input type="hidden" id="s_selectprofile" value="<%=s_selectprofile%>"/>
<!--  2009-12-22, add account policy, Kevin -->
<input type="hidden" id="s_policy" value="<%=policy %>"/>
<input type="hidden" id="s_loginretry" value="<%=loginretry %>"/>
<input type="hidden" id="s_expirationdate" value="<%=expirationdate %>"/>
<input type="hidden" id="s_standard" value="<%=standard %>"/>
<input type="hidden" id="s_strict" value="<%=strict %>"/>
<input type="hidden" id="s_remotesupport" value="<%=remotesupport %>"/>
<!-- end -->
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>

<form name="frm_ldapuser" id="frm_ldapuser" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<table border="0" width="98%" cellspacing="1" cellpadding="1">
	<tr>
		<td>
			<fieldset class="field">
				<legend class="standardTxt"><%=langService.getString("setldap","policy") %></legend>
				<div class="insideFieldsetTxt" style="padding:5px 9px">
					<div class='standardTxt'><%=profilecomment%></div>
				</div>
				<table border=0>
					<tr>
						<td>
							<input name="policytype" type="radio" value="<%=standard %>"  onclick="policytypechange('<%=standard %>')"/>
						</td>
						<td class='standardTxt'>
							<%=langService.getString("setldap","standardp") %>
						</td>
					</tr>
					<tr>
						<td class='standardTxt'>
							<input name="policytype" type="radio" value="<%=strict %>"   onclick="policytypechange('<%=strict %>')"/>
						</td>
						<td class='standardTxt'>
							<%=langService.getString("setldap","safep") %>
						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							<table border=0>
								<tr>
									<td class='standardTxt' width='160px'><%=langService.getString("setldap","expirdate") %>
									</td>
									<td><select name="expirationSel" id="expirationSel" class='standardTxt' style='width:100;' onchange="policyCombochanged();">
											<option value="0"><%=langService.getString("setldap","never") %></option>
											<option value="30">30 <%=days %></option>
											<option value="60">60 <%=days %></option>
											<option value="90">90 <%=days %></option>
										</select>
									</td>
								</tr>
								<tr>
									<td class='standardTxt'><%= langService.getString("setldap","logonretry") %>
									</td>
									<td><select name="logonretriesSel" id='logonretriesSel' class='standardTxt' style='width:100;' onchange="policyCombochanged();">
											<option value="0"><%=langService.getString("setldap","nolimit") %></option>
											<option value="5">5</option>
											<option value="10">10</option>
										</select>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</fieldset>	
		</td>
	</tr>
	<tr>
		<td height="10px"></td>
	</tr>
	<tr>
		<td>
			<fieldset class="field">
				<legend class="standardTxt"><%=langService.getString("setldap","remotesupport") %></legend>
				<table>
					<tr>
						<td>
							<input type="checkbox" id="remotesupport" name="remotesupport" onclick="remotesupportchange();"/>
						</td>
						<td class="standardTxt">
							<%=langService.getString("setldap","allowremote") %>
						</td>
					</tr>
				</table>
			</fieldset>		
		</td>
	</tr>
	<tr>
		<td height="10px"></td>
	</tr>
	<tr>
		<td class='standardTxt'><!-- <%=usercomment%>  --></td>
	</tr>
	<tr valign="top" id="trUserList">
		<td><%=htmlUsersTable%></td>
	</tr>
	<tr>
		<td height="5px"></td>
	</tr>
	<tr>
		<td>
			<input type="hidden" id="policychanged" name="policychanged" value="0"/>
			<input type="hidden" id="remotesupportchanged" name="remotesupportchanged" value="0"/>
			<input type="hidden" id="doubleuser" value="<%=doubleuser%>" />
			<input type="hidden" id="cmd" name="cmd" value="" />
			<input type="hidden" id="rowselected" name="rowselected" value="" />
			<input type="hidden" id="noremuser" name="noremuser" value="<%=noremuser%>" />
			<input type="hidden" id="user_to_modify" name="user_to_modify" value="" />
			<fieldset class="field">
				<legend  class="standardTxt"><%=user%></legend>
				<table  class="standardTxt" border=0>
					<tr>
						<td height="10px">&nbsp;</td>
					</tr>
					<tr>
						<td width="15%"><%=user%></td>
						<td width="25%"><input  style='width:200;' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> type="text"  name="user" id="user" onblur='noBadCharOnBlur(this,event);' onkeydown='checkBadChar(this,event);'/></td>
						<td width="5%">&nbsp;</td>
						<td width="15%"><%=password%></td>
						<td width="25%"><input style='width:200;' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> type="password"  name="password" id="password" onblur='noBadCharOnBlur(this,event);' onkeydown='checkBadChar(this,event);'/></td>
						<td width="5%">&nbsp;</td>
						<td width="10%" style='display:none'><input type = "button" id="unblockbutton" disabled value = "<%=unblock %>" onclick="unblock();"/></td>
					</tr>
				
					<tr>
						<td><%=profile%></td>
						<td  class="standardTxt"><%=select.toString()%></td>
				<!--	<input type="text"  name="profile" id="profile"/>-->
						<td width="5%">&nbsp;</td>
						<td><%=cpassword%></td>
						<td><input style='width:200;' <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> type="password"  name="cpassword" id="cpassword" onblur='noBadCharOnBlur(this,event);' onkeydown='checkBadChar(this,event);'/></td>
						<td colspan=2></td>
					</tr>
					<tr>
						<td height="10px">&nbsp;</td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>			
</form>
