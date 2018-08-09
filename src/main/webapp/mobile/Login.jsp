<%@ page language="java" pageEncoding="UTF-8"
	import="java.util.Locale"
	import="java.util.ArrayList"
	import="com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList"
	import="com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.dataaccess.dataconfig.ProductInfo"
	import="com.carel.supervisor.base.config.IProductInfo"
	import="com.carel.supervisor.base.config.ProductInfoMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.dataaccess.dataconfig.UsersUtils"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="supervisor.Login"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.bo.BUserProfile"
	import="com.carel.supervisor.presentation.ldap.DBProfiler"
	import="com.carel.supervisor.dataaccess.dataconfig.SiteInfoList"
	import="com.carel.supervisor.dataaccess.dataconfig.SiteInfo"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<%
	Locale.setDefault(Locale.ENGLISH);
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String jsession = request.getSession().getId();
	HttpSession httpSession = request.getSession();
	String msg = (String)httpSession.getAttribute(ServletHelper.PARAM_ERROR);
	httpSession.removeAttribute(ServletHelper.PARAM_ERROR);
	if(msg == null)
		msg = "";	
	StringBuffer sb = new StringBuffer("");
	LangUsedBeanList langused = new LangUsedBeanList();
	LangUsedBean[] lista = null;
	boolean error = false;
	String sCode = "";
	String sLang = "";
	String sEnco = "";
	String loginLanguage = BaseConfig.getProductInfo("login_language");
	boolean quickDashboardLink = Boolean.parseBoolean(BaseConfig.getProductInfo("quickdashboardlink"));
	
	
	try
	{
		lista = langused.retrieveAllLanguage(1);
	}
	catch(Exception e)
	{
		error = true;
		try 
		{
			sCode = LangUsedBeanList.getDefaultLanguage(1);
			sLang = LangUsedBeanList.getDefaultLanguageDescription(1);
			sEnco = LangUsedBeanList.getDefaultLanguageEncoding(1);
		}
		catch(Exception e2)
		{
			sCode = "EN_en";
			sLang = "English";
			sEnco = "UTF-8";
		}	
	}
	String message = "";
	try
	{
		message = ServletHelper.messageToNotify(LangUsedBeanList.getDefaultLanguage(1));
	}
	catch(Exception e)
	{
		message = ServletHelper.messageToNotify("EN_en");
	}	
	if(!error) {
		String login_language = BaseConfig.getProductInfo("login_language");
		login_language = (login_language==null?"":login_language.trim());
		for(int i=0; i<lista.length; i++){
			sb.append("<option value=\""+lista[i].getLangcode()+"^"+lista[i].getLangEncoding()+"\" "+  ( (login_language.equalsIgnoreCase(lista[i].getLangcode().trim()))?"selected":"" ) +">"+lista[i].getLangdescription()+"</option>");
		}
	}
	else
		sb.append("<option value=\""+sCode+"^"+sEnco+"\">"+sLang+"</option>");	
		
	//retrieve immagine sfondo login
	IProductInfo p_info = ProductInfoMgr.getInstance().getProductInfo();
	String path_image = p_info.get("img");
	
	if (path_image==null) 
		path_image = "login.jpg";
	else
		path_image = path_image.replace("\\","/");  //x firefox sennò non accetta il path
	
	String usrCombo = "";
	usrCombo = UsersUtils.getUsersListAsCombo();
	
	// Controllo user manual
	// Valido solo nel caso di EN come default
	String usManLang = p_info.get("usmanlang");
	if(usManLang == null || usManLang.length()== 0)
		usManLang = "EN";
	
	if(!usManLang.equalsIgnoreCase("EN"))
	{
		String curManLang = lista[0].getLangcode().substring(0,2);
		if(curManLang.equalsIgnoreCase("EN"))
			usManLang = "EN";
		else	
			if(curManLang != null && curManLang.equalsIgnoreCase(usManLang))
				usManLang = curManLang;
	}
	// End
	
	//String language = (!lista[0].getLangcode().substring(0,2).toUpperCase().equals("IT")?"EN":"IT");
	String back = "display: none"; // Alessandro : added string "display: " cause needs to remove 'display: block' and leaving only 'display: none'
	String s_cellspacing = "0";
	String b_cellspacing = "5";
	String width = "40%";
	String cmd = "";
	String userName = "";
	String newdisplay = "";
	String pwddisplay = "";
	String showsubmit1 = "";
	String disabletxtUser = "";
	String password = "Password";
	String login = "Login";
	String cancel = "Cancel";
	String stdpageslink = "Go to standard pages";
	boolean sessionValid = false;
	UserSession userSession = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	sessionValid = ServletHelper.validateSession(userSession);
	// get site name 
	SiteInfoList sites = new SiteInfoList();
	SiteInfo site = sites.getById(1);
	String site_name = site.getName();
	// Alessandro : retrieve language
	String language = "";
	if(sessionValid)
		language = userSession.getLanguage();
	else
		language = LangUsedBeanList.getDefaultLanguage(1);
	LangService lan = LangMgr.getInstance().getLangService(language);
	// Alessandro : setting changepassword strings
	String oldPassword = lan.getString("login","oldpassword");
	String newPassword = lan.getString("login","newpassword");
	String confPassword = lan.getString("login","confpassword");
	String cancleChangePW = lan.getString("filedialog","cancel");
	String submitChangePW = lan.getString("button","save");
	// Alessandro : for js login script
	String enterPassword = lan.getString("login","enterpassword");
	String standardPassword = lan.getString("login","standardpassword");
	String strictPassword = lan.getString("login","strictpassword");
	String confPwdIncorrect = lan.getString("login","confpwdincorrect");
	
    IProductInfo pi = ProductInfoMgr.getInstance().getProductInfo();
    String pwdchangedone = pi.get(Login.PWDCHANGEDONE);
    String profiletype = pi.get(BUserProfile.POLICY);
    String usercomboboxFlag = pi.get(ProductInfo.USERCOMBOBOX);
    String onclick = "";
    if(BUserProfile.STRICT.equals(profiletype))
    {
    	onclick = "return checkFields('strict');";
    }
    else
    {
    	onclick = "return checkFields('standard');";
    }
    if(pwdchangedone == null || !pwdchangedone.equals("0"))
    {
    	pwdchangedone = "1";
    }
    String usercombobox = "";
    if(!"true".equalsIgnoreCase(usercomboboxFlag))
    {
    	usercomboboxFlag = "false";
    }
    else
    {
    	DBProfiler profile = new DBProfiler();
    	ArrayList[] list = profile.getUserInformation();
    	ArrayList users = list[0];
    	usercombobox = "<select name='txtUser' id='txtUser' style='width:160px' tabIndex='1'>";
    	for(int i=0;i<users.size();i++)
    	{
    		String selected = users.get(i).equals("admin")?" selected ":"";
    		usercombobox += "<option value='"+users.get(i)+"'"+selected+">"+users.get(i)+"</option>";
    	}
    	usercombobox += "</select>";
    }
    //first time
    if(pwdchangedone.equals("0"))
    {
    	cmd = "firsttime";
    	userName = "admin";
    	//newdisplay = "block"; //Alessandro : 'block' style generates misalignment between labels and textboxes
    	newdisplay = "";
    	pwddisplay = "display: none"; //Alessandro : deleting of string 'block' forces adding string 'display: ' in order to avoid to have "style='display: '"
    	disabletxtUser = "disabled";
    	login = "Submit";
    	showsubmit1 = "display:none";
    	//width = "35%";
   		s_cellspacing = "3";
    	b_cellspacing = "3";
    }
    //change password
    else if(sessionValid == true)
    {
    	String changepwd = (String)httpSession.getAttribute(Login.CHANGEPWD);
    	httpSession.removeAttribute(Login.CHANGEPWD);
    	if(changepwd != null)
    	{
	    	cmd = "changepwd";
	    	userName = userSession.getUserName();
	    	newdisplay = "";
	    	pwddisplay = "";
	    	disabletxtUser = "disabled";
	    	password = oldPassword; // Alessandro: removed the static string and placed db text
//	    	login = "Submit";
	    	login = submitChangePW;
	    	cancel = cancleChangePW;
    		s_cellspacing = "2";
	    	b_cellspacing = "2";
	    	
	    	//back = "block";
	    	back = "";
	    	showsubmit1 = "display:none";
	    	//width="35%";
    	}
    	else //already login, but try to login again
    	{
    		cmd = "normal";
        	userName = "";
        	newdisplay = "display: none";
        	pwddisplay = "";
        	disabletxtUser = "";
    	}
    }
    //normal
    else
    {
    	String pwdexpired = (String)httpSession.getAttribute(DBProfiler.PROFILE_EXCEPTION_EXPIRED);
    	httpSession.removeAttribute(DBProfiler.PROFILE_EXCEPTION_EXPIRED);
    	String user = (String)httpSession.getAttribute(Login.USER);
    	httpSession.removeAttribute(Login.USER);
    	if(pwdexpired != null && user != null)
    	{
    		cmd = "pwdexpired";
        	userName = user;
        	newdisplay = "";
        	pwddisplay = "display: none";
        	disabletxtUser = "disabled";
	    	b_cellspacing = "2";
        	login = "Submit";
        	showsubmit1 = "display:none";
        	//width = "35%";
    	}
    	else
    	{
	    	cmd = "normal";
	    	userName = "";
	    	newdisplay = "display: none";
	    	pwddisplay = "";
	    	disabletxtUser = "";
    	}
    }
    
 	// license agreement
	String strLicense = ProductInfoMgr.getInstance().getProductInfo().get("license_agreement");
	boolean bLicense = strLicense != null && (strLicense.equalsIgnoreCase("yes") || strLicense.equalsIgnoreCase("true"));
	String strLiceseUrl = "../../licenses/" + (language.equals("IT_it") ? "license_IT.htm" : "license_EN.htm");
	if( !bLicense )
		cmd = "license";
%>
<html>
<head>
	<title>PVPRO</title>
	<base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
	<meta name="format-detection" content="telephone=no">
	<link rel="stylesheet" href="mobile/stylesheet/mobile.css" type="text/css">
	<script type="text/javascript" src="mobile/scripts/arch/Login.js"></script>
	<script type="text/javascript" src="scripts/arch/comm/Communication.js"></script>
	<script type="text/javascript" src="scripts/app/applmask.js"></script>
	<script type="text/javascript" src="scripts/arch/MaskInOut.js"></script>
	<script type="text/javascript" src="mobile/scripts/arch/util.js"/></script>
</head>
<body bgcolor="black" onload="initLoginPage('<%=cmd %>');" SCROLL="no" style="margin:0">
<form name="loginfrm" id="loginfrm" action="servlet/login;jsessionid=<%=jsession%>" method="post" target="_top">
<input type="hidden" name="browser" id="browser">
<input type="hidden" name="screenw" id="screenw">
<input type="hidden" name="screenh" id="screenh">
<input type="hidden" id="cmd" name="cmd" value="<%=cmd %>">
<input type="hidden" name="pagetype" id="pagetype" value="mobile"/>
<input type="hidden" id="jsession" name="jsession" value='<%=jsession %>'>
<input type="hidden" name="txtEnterPassword" id="txtEnterPassword" value="<%=enterPassword %>">
<input type="hidden" name="txtStandardPassword" id="txtStandardPassword" value="<%=standardPassword %>">
<input type="hidden" name="txtStrictPassword" id="txtStrictPassword" value="<%=strictPassword %>">
<input type="hidden" name="txtConfPwdIncorrect" id="txtConfPwdIncorrect" value="<%=confPwdIncorrect %>">
<input type='hidden' id='wincloseconfirm' value='<%=lan.getString("top","exitconfirm")%>'/>

<% if(disabletxtUser.equals("disabled")) {%>
<input type="hidden" name="txtUser" value="<%=userName %>">
<%} %>

<% if( bLicense ) { %>
<br>
<div id="divLogin">
	<table id="tabLogin" border="0" cellspacing="0" cellpadding="0" width="450px" height="380px" background="mobile/images/login.jpg">
		<tr>
			<td height="120px" colspan="2" align="center" valign="middle">
				<table border="0" cellpadding="1" cellspacing="1" width="100%">
					<tr>
						<td width="40%" align="center">
							<div id="pvppmstatus" style="position:relative;">
							</div>
						</td>
						<td width="40%" align="center">
							<div id="alarmdivlogin" style="position:relative;visibility:hidden;">
								<img src="images/menusx/multi_icon.png" border="0" <%if(quickDashboardLink){%> onclick="alarmViewAccess();" <%}%> />
							</div>
						</td>
						<td width="20%" align="left">
							<div id="guardivlogin" style="position:relative;visibility:hidden;">
								<img id="imgguardivlogin" src="images/gpro/guardian.gif" border="0"/>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td height="45%">&nbsp;</td>
			<td height="45%" align="center" valign="middle">
				<table border="0" cellspacing="<%=b_cellspacing %>" cellpadding="<%=b_cellspacing %>" width="90%">
					<tr>
						<td align="left" style="font-family:Tahoma;font-size:10pt;">Site</td>
						<td align="left" style="font-family:Tahoma;font-size:10pt;">
							<%= site_name %>
						</td>
					</tr>
					<tr>
						<td align="left" style="font-family:Tahoma;font-size:10pt;">User</td>
						<td align="left" >
							<%if("true".equalsIgnoreCase(usercomboboxFlag) && !disabletxtUser.equals("disabled")){%>
								<%=usercombobox %>
							<%}else {%>
							<input type="text" name="txtUser" <%=disabletxtUser%> id="txtUser" value="<%=userName %>" style="font-family:Tahoma;font-size:10pt;width:160;"  tabIndex="1" />
							<%} %>
						</td>
					</tr>
					<tr style="<%=pwddisplay %>" id="pwdisplayMark">
						<td align="left" style="font-family:Tahoma;font-size:10pt;"><%=password %></td>
						<td align="left">
							<input type="password" name=txtPassword id="txtPassword" value="" style="font-family:Tahoma;font-size:10pt;width:160;" tabIndex="2" />
						</td>
					</tr>
					<tr style="<%=newdisplay %>">
						<td align="left" style="font-family:Tahoma;font-size:10pt;"><%=newPassword %></td>
						<td align="left">
							<input type="password" name=npassword id="npassword" value="" style="font-family:Tahoma;font-size:10pt;width:160;" onblur='noBadCharOnBlur(this,event);' onkeydown="checkBadChar(this,event);" tabIndex="3" />
						</td>
					</tr>
					<tr style="<%=newdisplay %>" >
						<td align="left" style="font-family:Tahoma;font-size:10pt;"><%=confPassword %></td>
						<td align="left">
							<input type="password" name=cpassword id="cpassword" value="" style="font-family:Tahoma;font-size:10pt;width:160;" onblur='noBadCharOnBlur(this,event);' onkeydown="checkBadChar(this,event);" tabIndex="4" />
						</td>
					</tr>
					<tr>
						<td align="left" style="font-family:Tahoma;font-size:10pt;">Language</td>
						<td align="left" style="font-family:Tahoma;font-size:10pt;">
							<select name="txtLanguage" onfocus="lanOnfocus();" onkeydown="languageKeydownNormal(this,event);" id="txtLanguage" style="font-family:Tahoma;font-size:10pt;" tabIndex="20" >
		      					<%=sb.toString()%>
		      				</select>
						</td>
						<td align='left' style="<%=showsubmit1 %>">
							<div id="lgnbttprg">
								<input type="submit"  id="loginArea" name="btnLogin"  style="border:0px;cursor:hand;width:57px;height:20px;background-image:url('images/login_bt.png');background-color: transparent;color:white;cursor:pointer;" value="<%=login %>" onclick="<%=onclick %>" tabIndex="10" />
							</div>
						</td>
					</tr>
					<tr>
						<td align="left" colspan="2">
							<table border="0" cellspacing="<%=s_cellspacing %>" cellpadding="<%=s_cellspacing %>" width="100%" height="100%">
								<tr>
									<td align="left" style="font-family:Tahoma;font-size:8pt;"><div style="display:block;" id="message"><font color="#FF0000"><%=msg%></font></div></td>
									<td align="right">
										<div style="display:none;" id="lgnmsgprg">
											<p style="font-family:Tahoma;font-size:8pt;color:red;">Login in progress...</p>
										</div>		
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td height="10%">&nbsp;</td>
			<td height="10%">&nbsp;</td>
		</tr>
		<tr>
			<td height="20%" align="right" valign="middle" style="font-size:10pt;color:red;font-weight:bold;"><%=message%></td>
			<td height="20%" align="right" valign="middle" style="padding-right:15px"><a href="arch/login/Login.jsp"><%=stdpageslink%></a></td>
		</tr>
	</table>
</div>	
</form>
<% } else { %>
<br><br><br>
<table width="80%" height="90%" align="center">
<tr height="85%"><td bgcolor="white">
<div style="width:100%;height:100%;align:center;overflow:auto;">
<jsp:include page="<%=strLiceseUrl%>" flush="true" />
</div>
</td></tr>
<tr><td align="center" class="standardTxt">
<form name="licensefrm" id="licensefrm" action="servlet/login;jsessionid=<%=jsession%>" method="post">
<input type="hidden" id="cmd" name="cmd" value="<%=cmd%>">
<input type="hidden" name="pagetype" id="pagetype" value="mobile"/>
<span style="color:white;"><%=lan.getString("license", "i_accept")%></span>
<input type="checkbox" id="license_agreement" name="license_agreement">
<input type="submit" style="border:0px;cursor:hand;width:57px;height:20px;background-image:url('images/login_bt.png');background-color: transparent;color:white;" value="<%=lan.getString("license", "submit")%>"/>
</form>
</td></tr>
</table>
<% } %>
</body>
</html>