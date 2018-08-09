<%@ page language="java"  pageEncoding="UTF-8"
import="com.carel.supervisor.presentation.helper.ServletHelper" 
import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
import="com.carel.supervisor.presentation.session.UserSession" 
import="com.carel.supervisor.dataaccess.language.LangMgr" 
import="com.carel.supervisor.dataaccess.language.LangService" 
import="com.carel.supervisor.presentation.bean.ProfileBeanList"  
import="com.carel.supervisor.presentation.bean.GroupBean"
import="com.carel.supervisor.presentation.bean.GroupListBean"
import="com.carel.supervisor.base.config.BaseConfig"
import="com.carel.supervisor.base.config.IProductInfo"
import="com.carel.supervisor.base.config.ProductInfoMgr"
import="com.carel.supervisor.presentation.ldap.FunctionalityList"
import="com.carel.supervisor.presentation.bean.FileDialogBean"
 %>

<%@ page import="com.carel.supervisor.presentation.profile.ProfileGroupsBeanList" %><%@ page import="com.carel.supervisor.base.config.BaseConfig" %><%@ page import="org.postgresql.core.BaseConnection" %> 

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	int idsite = sessionUser.getIdSite();
	String language = sessionUser.getLanguage();
	LangService langService = LangMgr.getInstance().getLangService(language);
	String jsession = request.getSession().getId();
	
	String pv_type = BaseConfig.getProductInfo("type");
	boolean group_info = true;
	
	String desc=langService.getString("ldapPage","desc");
	String profile=langService.getString("ldapPage","profile");
	String usedprofile = sessionUser.getPropertyAndRemove("usedprofile");
	String doubleprofile = sessionUser.getPropertyAndRemove("doubleprofile");
	
	String overwriteProf = sessionUser.getPropertyAndRemove("overwriteProf");
	String filename = sessionUser.getPropertyAndRemove("filename");
	String loadxmlerror = sessionUser.getPropertyAndRemove("loadxmlerror");
	String isadmin = sessionUser.getPropertyAndRemove("isadmin");
	String higherVersion = sessionUser.getPropertyAndRemove("higherVersion");
	String lowerVersion = sessionUser.getPropertyAndRemove("lowerVersion");
	String errorVersion = sessionUser.getPropertyAndRemove("errorVersion");
	
	String s_higherVersion =langService.getString("setldap","higherVersion");
	String s_lowerVersion =langService.getString("setldap","lowerVersion");
	String s_errorVersion =langService.getString("setldap","errorVersion");
	
	String s_profileused =langService.getString("setldap","s_profileused");
	String s_nomodadminprofile =langService.getString("setldap","s_nomodadminprofile");
	String s_noremadminprofile=langService.getString("setldap","s_noremadminprofile");
	String s_noremfactoryprofile=langService.getString("setldap","s_noremfactoryprofile");
	String s_confirmdeleteprofile=langService.getString("setldap","s_confirmdeleteprofile");
	//String s_area = langService.getString("setldap","area");
	String s_group = langService.getString("setldap","group");
	String s_ldapcomment = langService.getString("setldap","ldapcomment");
	String s_enable = langService.getString("setldap","enable");
	String s_insertdescription = langService.getString("setldap","insertdescription");
	String s_no_carel_prof = langService.getString("setldap","no_carel_prof");
	String adminimportforbit = langService.getString("setldap","adminimportforbit");
	String adminexportforbit = langService.getString("setldap","adminexportforbit");
	String save_confirm = langService.getString("fdexport","exportconfirm");
	String uploadfail = langService.getString("setldap","errorparse");
	
	ProfileBeanList profile_list = new ProfileBeanList(sessionUser.getIdSite(),false);
	profile_list.setScreenH(sessionUser.getScreenHeight());
	profile_list.setScreenW(sessionUser.getScreenWidth());
	String htmlProfilesTable = profile_list.getHtmlProfileTable(105,880,language,"profiletable");
	
	// Alessandro : aggiunto codice per impostare la classe css per la tastiera virtuale
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	//String cssVirtualKeyboardClass = (OnScreenKey ? ','+VirtualKeyboard.getInstance().getCssClass() : "");	
	
	FunctionalityList functList=new FunctionalityList();
	functList.setScreenH(sessionUser.getScreenHeight());
	functList.setScreenW(sessionUser.getScreenWidth());
	
	functList.setWidth(900);
	if (group_info)
		functList.setHeight(200);  // ADVANCED: with group table
	else
		functList.setHeight(320);  // not ADVANCED: without group table
		
	String htmlFunctTable = functList.getHTMLFunctTable("functTable",language,sessionUser);

	//AreaDbBeanList areas = new AreaDbBeanList();
	//areas.retrieveAreas(null,idsite,language);
	GroupListBean groups = new GroupListBean();
	
	//StringBuffer areaTable = new StringBuffer();
	StringBuffer groupTable = new StringBuffer();
	
	int idgroup = -1;
	
	String gs = null;
	String idsGroups = "";
	
	GroupBean[] group = groups.retrieveAllGroupsNoGlobal(idsite,language); 
	
	groupTable.append("<table class='table' id='grouptable' width='800' cellspacing='1' cellpadding='1'>");
	for (int j=0;j<group.length;j++)
	{
		idgroup = group[j].getGroupId();
		idsGroups = idsGroups + idgroup + "-";
		groupTable.append("<tr class='Row1' onmouseover=\"javaScript:this.style.cursor='pointer'\"><td class='standardTxt' align='center' width='100px'>");
		groupTable.append("<input checked type='checkbox' id='g"+idgroup+"' name='g"+idgroup+"' />");
		groupTable.append("</td><td class='standardTxt' width='700px'>");
		groupTable.append(group[j].getDescription());
		groupTable.append("</td></tr>");
	}
	groupTable.append("</table>");
	if (!idsGroups.equals(""))
		idsGroups = idsGroups.substring(0,idsGroups.length()-1);

	String s_doubleprofile = langService.getString("setldap","doubleprofile");
	
	//Software version check. In case of BE and LE versions, kpi module doesn't exist
	IProductInfo productInfo = ProductInfoMgr.getInstance().getProductInfo();
	String ver = productInfo.get("cp");
	Integer funct_number = IProfiler.FUNCTION_NUMBER;
	FileDialogBean fileDlg = new FileDialogBean(request);
	
	String s_overwriteProf = langService.getString("setldap","overwriteProf");
	boolean bLocal = FileDialogBean.isLocal(request);
	
%>

<%@page import="com.carel.supervisor.base.profiling.IProfiler"%><input type="hidden" id="s_doubleprofile" value="<%=s_doubleprofile%>"/>
<input type="hidden" id="doubleprofile" value="<%=doubleprofile%>"/>
<input type="hidden" id="no_carel_prof" value="<%=s_no_carel_prof%>"/>
<input type="hidden" id="s_insertdescription" value="<%=s_insertdescription%>"/>
<input type="hidden" id="idsgroups" value="<%=idsGroups%>"/>
<input type="hidden" id="s_profileused" value="<%=s_profileused%>"/>
<input type="hidden" id="s_nomodadminprofile" value="<%=s_nomodadminprofile%>"/>
<input type="hidden" id="s_noremadminprofile" value="<%=s_noremadminprofile%>"/>
<input type="hidden" id="s_noremfactoryprofile" value="<%=s_noremfactoryprofile%>"/>
<input type="hidden" id="s_confirmdeleteprofile" value="<%=s_confirmdeleteprofile%>"/>
<input type="hidden" id="funct_number" value="<%=funct_number%>"/>
<input type='hidden' id='adminexportforbit' value="<%=adminexportforbit%>">
<input type='hidden' id='save_confirm' value='<%=save_confirm %>' />
<input type="hidden" id="overwriteProf" value="<%=overwriteProf%>"/>
<input type="hidden" id="s_overwriteProf" value="<%=s_overwriteProf%>"/>
<input type='hidden' id='loadxmlerror' value="<%=loadxmlerror%>">
<input type='hidden' id='filename' value="<%=filename%>">
<input type='hidden' id='isadmin' value="<%=isadmin%>">
<input type='hidden' id='adminimportforbit' value="<%=adminimportforbit%>">
<input type='hidden' id='uploadfail' value="<%=uploadfail%>">

<input type='hidden' id='higherVersion' value="<%=higherVersion%>">
<input type='hidden' id='lowerVersion' value="<%=lowerVersion%>">
<input type='hidden' id='errorVersion' value="<%=errorVersion%>">

<input type='hidden' id='s_higherVersion' value="<%=s_higherVersion%>">
<input type='hidden' id='s_lowerVersion' value="<%=s_lowerVersion%>">
<input type='hidden' id='s_errorVersion' value="<%=s_errorVersion%>">
<input type='hidden' id='bLocal' value="<%=bLocal %>"/>



<%= fileDlg.renderFileDialog() %>
<%= (OnScreenKey? "<input type='hidden' id='vkeytype' value='PVPro' />" : "") %>
<input type="hidden" id="vk_state" value="<%= (OnScreenKey) ? '1' : '0' %>" />

<form name="frm_acl" id="frm_acl"  action="servlet/master;jsessionid=<%=jsession%>" method="post">
	<table border="0" cellspacing="1" cellpadding="1" width="98%">
	<tr valign="top" id="trUserList1">
		<td><%=htmlProfilesTable%></td>
	</tr>
	<tr><td style="font-size:2pt;">&nbsp;</td></tr>
	   <tr>
	   	<td width="100%">
			<input type="hidden" id="language" name="language" value="<%=language%>"/>
			<input type="hidden" id="usedprofile" value="<%=usedprofile%>"/>
			<input type="hidden" id="cmd" name="cmd" value="user"/>
			<input type="hidden" id="rowselected" name="rowselected" value="" />
		
			<fieldset class="profileField" >
			<legend class='standardTxt'><%=profile%></legend>
				<table border="0"  width='100%' class="standardTxt">
				<tr>
					<td rowspan="2" width="45%"><%=desc%> &nbsp; <input <%=(OnScreenKey?"class='keyboardInput'":"class='standardTxt'")%> type="text"  id="desc" name="desc" disabled maxlength="30" size="30"  onblur="noBadCharOnBlur(this,event);" onkeydown="checkBadChar(this,event);"/></td>
					<td width='10%'><%=langService.getString("setldap","menuvisible")%> </td>
					<td align='left'><input type="checkbox"  id="menuvisible" name="menuvisible" checked disabled value="true"/></td>
				</tr>
				<tr>
					<td class='standardTxt'><%=langService.getString("setldap","paramfilter")%> </td>
					<td align='left'>
						<select name='param_filter' id='param_filter'>
							<option value='1'>Services</option>
							<option value='2'>Manufacturer</option>
						</select>
					</td>
				</tr>
				</table>
			</fieldset>
		</td>
	</tr>
	<tr><td style="font-size:2pt;">&nbsp;</td></tr>
	<tr>
		<td>
			<fieldset class="profileField" >
			<legend class='standardTxt'><%=langService.getString("setldap","rwsetting")%></legend>
				<table border="0" cellspacing="1" cellpadding="1" border=1 width="90%">
				<tr class="th">
					<td width='35%' class="tdmini" align='center'><b><%=langService.getString("setldap","page")%></b></td>
					<td width='65%' class="tdmini" align='center'><b><%=langService.getString("setldap","actions")%></b></td>
				</tr>
				<tr valign="top" id="trUserPrivList">
					<td colspan=2><%=htmlFunctTable%></td>
				</tr>
				</table>
			</fieldset>
		</td>
	</tr>
	<tr><td style="height:3pt"></td></tr>
	<tr>
		<td>
			<table border="0" align="left">
			<tr>
				<td>
					<div class="divEnGroupBox"> 
						<div>
							<table class="table" width="100%" cellspacing="1" cellpadding="1">
								<tr class="th"> 
									<td class="tdmini" align='center' width="100px"><b><%=s_enable%></b></td>
									<td class="tdmini" align='center' width="700px"><b><%=s_group%></b></td>
								</tr>
							</table>
						</div>
						<div id="divgroups" class="divEnGroupList">
							<%=groupTable.toString()%>		
						</div>
					</div> 
				</td>
			</tr>
			</table>
		</td>
	</tr>
  	</table>
</form>

<div  id="fdFieldRemote" name="fdFieldRemote" class="uploadWin" >
	
	<div id="uploadwinheader" class="uploadWinHeader">
		<div class="uploadWinClose" onclick="fdRemoteCancel();">X</div>
		<%=langService.getString("filedialog","load")%>
	</div>
	<div id="uploadwinbody" class="uploadWinBody">
		<div>
		  <form name="profuploadform" id="profuploadform" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
			<div id="uploadwinotherpath">
				<div id="oplabel" style="width:100px;"><span id="otherasDIV"><%=langService.getString("r_siteaccess","chanel")%></span></div>
				<div id="oploadbox"><span id="other_span_load"><%= fileDlg.inputLoadFileForProfile() %></span></div>
			</div>
			<div id="uploadwinbuttons">
				<table border="0">
					<tr>
						<td class="groupCategory_small" style="width:110px;height:30px;" onclick="document.getElementById('profuploadform').submit();"><%=langService.getString("graphvariable","goback2graph")%></td>
						<td class="groupCategory_small" style="width:110px;height:30px;" onclick="fdRemoteCancel()"><%=langService.getString("filedialog","cancel")%></td>
					</tr>
				</table>
			</div>
		  </form>
		</div>	
	</div>
	
</div>



