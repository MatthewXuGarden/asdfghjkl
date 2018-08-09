<%@ page language="java" pageEncoding="UTF-8"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.presentation.bean.BookletConfBean"
	import="com.carel.supervisor.presentation.bean.ProfileBean"
	import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
%>
<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	boolean isProtected = ut.isTabProtected();
	String language = sessionUser.getLanguage();
	int idsite = sessionUser.getIdSite();
	String jsession = request.getSession().getId();

	//stringhe multilingua
	LangService lang = LangMgr.getInstance().getLangService(language);
	String devparam = lang.getString("booklet","devparam");
	String images_import_on = "images/actions/import_on_black.png";
	String images_import_off = "images/actions/import_off.png";
	
	String htmlCabinetTable = BookletCabinetListPres.getHTMLCabinetTable(idsite, language,"");
	
	String devices = lang.getString("booklet","devices");
	String devmodels = lang.getString("booklet","devmodels");
	
	// BIOLO retrieve profile permission for booklet  0:none   1: read   2: readwrite
	boolean profile_booklet = sessionUser.isButtonActive("booklet","tab2name","Save");   
	
	FileDialogBean fileDlg = new FileDialogBean(request);
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey(); 	
%>
<%=fileDlg.renderFileDialog()%>
<input type="hidden" id="profile_booklet" value="<%=profile_booklet%>" />
<input type="hidden" id="subtab" value="subtab2">
<input type="hidden" id="cabinet_validate" value='<%=lang.getString("booklet","cabinet_validate") %>'>
<input type="hidden" id="devices_validate" value='<%=lang.getString("booklet","devices_validate") %>'>
<input type="hidden" id="file_validate" value='<%=lang.getString("booklet","file_validate") %>'>
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='PVPro' />":"")%>
<TABLE border="0" >
		<TR valign="top" >
			<TD valign="top"><%=htmlCabinetTable%></TD>
		</TR>
</TABLE>
<form name="uploadform" id="uploadform" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
	<input type="hidden" name="iddevices" id="iddevices" value=""/>
	<input type="hidden" name="cmd" value="save"/>
	<input type="hidden" name="id" id="id" value=""/>
	<table border="0" cellpadding="1" cellspacing="1" width="90%" height="100%">
		<tr height="1%">
			<td>
				<table border="0" cellpadding="1" cellspacing="1" width="100%" height="100%" align="center">
					<tr>
						<td style="height:5px"></td>
					</tr>
					<tr class='standardTxt'>
						<td  width="6%" align="left">
							<%=lang.getString("booklet","cabinet") %>
						</td>
						<td  width="20%" align="left">
							<input <%=!profile_booklet?"disabled":""%> type='text' id='cabinet' name='cabinet'/>
						</td>
						<td width="20%" align="right">
							<%=lang.getString("booklet","file") %>
						</td>
						<td width="20%">
							<input type="hidden" name="tipofile" value="booklet_cabinet" />
							<%=fileDlg.inputLoadFile("fileupload", "*", "size='30%' onChange=enabImport('fileupload','button','*');", isProtected)%>
							<img id="button" style="display:none"/>
						</td>
						<td width="34%" id="fileNameTD"></td>
					</tr>
					<tr>
						<td align="right">
							<input <%=!profile_booklet?"disabled":""%> onclick='getDevList(this);' type='radio' id='d' name='devmodl' checked="checked" />
						</td>
						<td>
							<%=devices%>
						</td>
						<td align="right">
							<input <%=!profile_booklet?"disabled":""%> onclick='emptyComponents(this);' type='radio' id='m' name='devmodl' /><%=devmodels%>
						</td>
						<td align="left">
							<select <%=!profile_booklet?"disabled":""%> onchange='changeDevMdl(this);' class='standardTxt' id='model' name='model' disabled style="width:275px;" />
<!--								< %=combodev.toString()%>-->
							</select>
						</td> 
						<td align="right">
							<img src="images/actions/addsmall_on_black.png" style="cursor:pointer;" onclick="multipleAddDevice();"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr height="*">
			<td valign="top">
				<table align="center" width="100%" class="table">
					<tr>
						<td class='th' width="49%"><%=devices%></td>
						<td width="2%">&nbsp;</td>
						<td class='th' width="49%"><%=devices %></td>
					</tr>
					<tr>
						<td height="100%" valign="top">
						<div id="div_dev">
							<select <%=!profile_booklet?"disabled":""%> id="devLst" size="10" onchange="" ondblclick="dblClickDevList(this);" class='selectB'>
							</select>
						</div>
						</td>
						<td>&nbsp;</td>
						<td height="100%" valign="top">
						<div id="div_var">
							<select <%=!profile_booklet?"disabled":""%> id="paramLst" size="10" multiple onchange="" ondblclick="dblClickParamList(this);"  class='selectB'>
							</select>
						</div>
						</td>
					</tr>
				</table>
			 </td>
		</tr>
	</table>
</form>
<FORM id="post_form" action="servlet/master;jsessionid=<%=jsession%>" method="post">
<input type="hidden" name="cmd" id="post_cmd" value=""/>
<input type="hidden" name="id" id="post_id" value=""/>
</FORM>