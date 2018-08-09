<%@ page language="java"
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession"
	import="com.carel.supervisor.presentation.devices.ParamDetail"
	import="com.carel.supervisor.dataaccess.language.LangService"
    import="com.carel.supervisor.dataaccess.language.LangMgr"
    import="com.carel.supervisor.presentation.copydevice.PageImpExp"
    import="com.carel.supervisor.device.DeviceStatusMgr"
    import="com.carel.supervisor.device.DeviceStatus"
    import="com.carel.supervisor.director.DirectorMgr.*"
    import="com.carel.supervisor.presentation.bean.*"
	import="com.carel.supervisor.presentation.session.UserTransaction"
	import="com.carel.supervisor.presentation.bean.DeviceStructure"
    import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
    import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBean"
    import="com.carel.supervisor.dataaccess.datalog.impl.VarphyBeanList"
    import="com.carel.supervisor.base.config.*"	
%>
<%@ page import="com.carel.supervisor.director.DirectorMgr" %>
<%@ page import="java.util.Properties" %>
<%@ page import="com.carel.supervisor.presentation.comboset.ComboParamMap" %>

<%
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(),request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();    
	String language = sessionUser.getLanguage();
    LangService lan = LangMgr.getInstance().getLangService(language);
	String devicecomment2 = lan.getString("dtlview","devicecomment2");
	String changegrpmsg = lan.getString("usrmsg","msg03");
	String jsession = request.getSession().getId();
	int idSite = sessionUser.getIdSite();
	if(idSite < 0)
		idSite = 1;
		
	int idDev = -1;
	try{
		idDev  = Integer.parseInt(sessionUser.getProperty("iddev"));
	}
	catch(NumberFormatException e){}
		
	String cmd = sessionUser.getPropertyAndRemove("cmd");
	String export_cmd = sessionUser.getPropertyAndRemove("export_cmd");
	String export_type= sessionUser.getPropertyAndRemove("export_type");
	String inner = sessionUser.getPropertyAndRemove("inner");
	if(inner == null) inner = "";
	String imp_file = sessionUser.getPropertyAndRemove("imp_file");
	if (imp_file==null) imp_file="";
	
	ParamDetail paramDetail = new ParamDetail(sessionUser);
	//String name = paramDetail.getNameTable(sessionUser,idDev);
	//String htmltb = "";
	
	//htmltb = paramDetail.buildHtmlFisa(name,imp_file, language);;
	
	String exported = lan.getString("dtlview","exported");
	String notexported = lan.getString("dtlview","notexported");
	String imported = lan.getString("dtlview","imported");
	String notimported = lan.getString("dtlview","notimported");
	String offline1 = lan.getString("dtlview","offline1");
	String offline2 = lan.getString("dtlview","offline2");
	String s_copyall = lan.getString("dtlview","copyall");
	String s_notcopyall = lan.getString("dtlview","notcopyall");
	
	DeviceStatus status = DeviceStatusMgr.getInstance().getDeviceStatus(new Integer(idDev));
	boolean offline = false;
	if (status==null)
	{
		offline = true;
	}
	else
	{
		offline = (!status.getStatus());
	}
	
	//add by Kevin, if there isn't writable variable, disable all the action buttons
	boolean hasWritableVariable = false;
	int[] iArDevice = { idDev };
	VarphyBean[] listVarphy = VarphyBeanList.getListVarWritable(language,idSite, iArDevice);
	if(listVarphy != null && listVarphy.length>0)
	{
		hasWritableVariable = true;
	}
	//--

	//####################################
	//   NUOVA PAGINA
	//####################################
	int screen_w = sessionUser.getScreenWidth();
	int screen_h = sessionUser.getScreenHeight();
	String html_groups = paramDetail.getHTMLGroupsHeader(idSite,idDev,screen_w,language);
	String current_grp = sessionUser.getCurrentUserTransaction().remProperty("current_grp");
	
	if (DirectorMgr.getInstance().isMustCreateProtocolFile())
	{
	
	}
	
	DeviceStructureList deviceStructureList = sessionUser.getGroup().getDeviceStructureList();
	DeviceStructure deviceStructure = deviceStructureList.get(idDev);
	
	String exportName = "";
	if(deviceStructure != null)
	{
		String dev_code = PageImpExp.getDeviceCode(idSite,idDev);
		exportName = "Param_"+dev_code+"_"+deviceStructure.getCode();
		ut.getBoTrx().loadFilter(idDev, deviceStructure.getIdDevMdl(), sessionUser.getLanguage());
	}
	
	String larg = "7%";
	String virtkey = "off";
	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	if (OnScreenKey)
	{
		virtkey = "on";
		larg = "8%";
	}
	
	// SET VARIABILI TRAMITE COMBO
		ComboParamMap c = new ComboParamMap();
        c.loadDeviceConf(idDev,language);
		sessionUser.getCurrentUserTransaction().setObjProperty("comboparam",c);
    // FINE
    
    
    //check set permission of parameters
    
	int permission = 0;
	if (sessionUser.getVariableFilter()==ProfileBean.FILTER_SERVICES)
	{
		permission = 2;	
	}
	FileDialogBean fileDlg = new FileDialogBean(request);
	boolean bLocal = FileDialogBean.isLocal(request);
	// set value with note
	String strValueNote = ProductInfoMgr.getInstance().getProductInfo().get("value_note");
%>
 <%=fileDlg.renderFileDialog()%>
<% if (DirectorMgr.getInstance().isMustCreateProtocolFile()) {%>
	
<%@page import="com.carel.supervisor.presentation.bean.ProfileBean"%><p class='standardTxt'><%=lan.getString("dtlview","restarttoview")%></p>
	<input type='hidden' id='js_control'/>
<%}else { %>
<input type='hidden' id='jsession' value="<%=jsession%>"/>
<input type='hidden' id='exported' value="<%=exported%>"/>
<input type='hidden' id='notexported' value="<%=notexported%>"/>
<input type='hidden' id='imported' value="<%=imported%>"/>
<input type='hidden' id='notimported' value="<%=notimported%>"/>
<input type='hidden' id='offline' value='<%=offline%>' />
<input type='hidden' id='offline1' value='<%=offline1%>' />
<input type='hidden' id='offline2' value='<%=offline2%>' />
<input type='hidden' id='hasw_v' value='<%=hasWritableVariable%>' />
<input type='hidden' id='s_copyall' value='<%=s_copyall%>' />
<input type='hidden' id='s_notcopyall' value='<%=s_notcopyall%>' />
<INPUT type="hidden" id="permission" value="<%=permission%>" />
 <input type='hidden' id='save_confirm' value='<%=lan.getString("fdexport","exportconfirm") %>' />
<input type='hidden' id='save_error' value="<%=lan.getString("fdexport","exporterror") %>" />
<input type='hidden' id='virtkeyboard' value='<%=virtkey%>' />
<%=(OnScreenKey?"<input type='hidden' id='vkeytype' value='Numbers' />":"")%>
<input type='hidden' id='saveon' value="<%=lan.getString("dtlview","saveon") %>"/>
<input type='hidden' id='loadfrom' value="<%=lan.getString("dtlview","loadfrom") %>"/>
<input type='hidden' id='choosefile' value="<%=lan.getString("dtlview","choosefile") %>"/>
<input type='hidden' id='enterfilename' value="<%=lan.getString("dtlview","enterfilename") %>"/>
<input type='hidden' id='savepath' value="<%=lan.getString("dtlview","savepath") %>"/>
<input type='hidden' id='localas' value="<%=lan.getString("dtlview","localas") %>"/>
<input type='hidden' id='otheras' value="<%=lan.getString("dtlview","otheras") %>"/>
<input type='hidden' id='download' value="<%=lan.getString("dtlview","download") %>"/>
<input type='hidden' id='bLocal' value="<%=bLocal %>"/>
<input type='hidden' id='inner' value="<%=inner%>"/>
<input type='hidden' id='exportok' value="<%=lan.getString("dtlview","exportok")%>"/>
<input type='hidden' id='strValueNote' value="<%= strValueNote%>"/>


<form name="frmdtlset" id="frmdtlset" method="post" action="servlet/master;jsessionid=<%=jsession%>">

<input type='hidden' id='cmd' name='cmd' value="<%=cmd%>"/>
<input type='hidden' id='export_cmd' name='export_cmd' value="<%=export_cmd%>"/>
<input type='hidden' id='export_type' name='export_type' value="<%=export_type%>"/>
<input type='hidden' id='iddev' name='iddev' value="<%=idDev%>" /> 
<input type='hidden' id='exp_file' name='exp_file' value=""/>
<input type='hidden' id='imp_file' name='imp_file' value=""/>
<input type='hidden' id='ids_toset' name='ids_toset' value=""/>
<input type='hidden' id='scr_h' value="<%=screen_h%>"/>
<input type='hidden' id='scr_w' value="<%=screen_w%>"/>
<input type='hidden' id='s_maxval' value="<%=lan.getString("dtlview","s_maxval")%>"/>
<input type='hidden' id='s_minval' value="<%=lan.getString("dtlview","s_minval")%>"/>
<input type='hidden' id='current_grp' name='current_grp' value="<%=current_grp%>"/>
<input type='hidden' id='changegrpmsg' name='changegrpmsg' value="<%=changegrpmsg%>"/>
<input type='hidden' id='file_path' name='file_path'/>
<input type='hidden' id='noteInfo' name='noteInfo' value="" />
<input type='hidden' id='noteRequired' value="<%=lan.getString("alrmng","noteRequiredMessage")%>"/>
<input type="hidden" id="broadcastTitle" value="<%=lan.getString("dtlview","selectpage")%>" />

<table width="100%" border='0' cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<div id='div_groups'><%=html_groups%></div> 
		</td>
	</tr>
	<tr height="5px"><td></td></tr>
	<tr>
		<td>
			<table border='0' cellpadding="0" cellspacing="0" width="99%"> 
				<tr>
					<td>
					<div id="div_head">					
						<table border='0' id='h_table' class='table' cellpadding="0" cellspacing="1" width="99%">
							<tr class='th'>
								<td width="12%" align="center"><b><%=lan.getString("dtlview","col1")%></b></td>
								<td width="18%" align="center"><b><%=lan.getString("dtlview","col2")%></b></td>
								<td width="9%" align="center"><b><%=lan.getString("dtlview","col3")%></b></td>
								<td width="12%" align="center"><b><%=lan.getString("dtlview","col5")%></b></td>
								<td width="*" align="center"><b><%=lan.getString("dtlview","col4")%></b></td>
							</tr>
						</table>
					</div>
					<div id='div_head2' style="display:none;">
						<table border='0' id='h2_table' class='table' cellpadding="0" cellspacing="1" width="100%">
							<tr class='th'>
								<td width="25%" align="center"><b><%=lan.getString("dtlview","col1")%></b></td>
								<td width="25%" align="center"><b><%=lan.getString("dtlview","col2")%></b></td>
								<td width="*" align="center"><b><%=lan.getString("dtlview","col4")%></b></td>
							</tr>
						</table>
					</div>								
					</td>
				</tr>
				<tr>
					<td>
						<div id='div_params' style="width:100%;height:200pt; overflow:auto;background-color:cacaca;"></div>					
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

<p></p>
<div id='div_conf' style="width:20%;display:none;"></div>
<div id='div_link'></div>
</form>

<div id='div_mm' style='display:none;'></div>
<div id="longdescdiv" class="standardTxt"></div>
<%}%>


<div id="uploadwin" class="uploadWin">
	<div id="uploadwinheader" class="uploadWinHeader">
		<div class="uploadWinClose" onclick="document.getElementById('uploadwin').style.display='none';">X</div><%=lan.getString("dtlview","wintitle")%>
	</div>
	<div id="uploadwinbody" class="uploadWinBody">
		<div id="win_type"></div>
		<div>
			<form name="uploadfrm1" id="uploadfrm1" style="display:block;" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
				<input type="hidden" name="tipofile" value="impParamModule" />
				<input type="hidden" name="params_values" id="params_values" value="<%=sessionUser.getProperty("params_values") %>" />
				<div id="uploadwinlocalpath">
					<div id="lpradio"><input type="radio" class="bigRadio" id="win_radio_l" name="win_radio" onclick="chooseFileLocation(1);" checked></div>
					<div id="lplabel" style="width:100px;"><span id="localasDIV"></span></div>
					<div id="lploadbox"><span id="local_span_load"></span></div>
					<div id="lpsavebox"><span id="local_span_save"><input type="text" name="fileN" id="fileN" value="<%=exportName %>" <%=(OnScreenKey?"class='keyboardInput'":"")%> style="width:255px"/></span></div>
				</div>
				<div id="uploadwinotherpath">
					<div id="opradio"><input type="radio" class="bigRadio" id="win_radio_o" name="win_radio" onclick="chooseFileLocation(0);"></div>
					<div id="oplabel" style="width:100px;"><span id="otherasDIV"><%=lan.getString("dtlview","otheras")%></span></div>
					<div id="oploadbox"><span id="other_span_load"><%=fileDlg.inputLoadFile("upload_file", "", "style='width:188px;'", false)%></span></div>
					<div id="opsavebox"><span id="other_span_save"><%=fileDlg.inputSaveFile("save_file", "", "style='width:198px;'", false,exportName)%></span></div>		
				</div>
				<div id="uploadwinbuttons">
					<table border="0">
						<tr>
							<td class="groupCategory_small" style="width:110px;height:30px;" onclick="submit_template_file();"><%=lan.getString("dtlview","submit")%></td>
							<td class="groupCategory_small" style="width:110px;height:30px;" onclick="document.getElementById('uploadwin').style.display='none'"><%=lan.getString("dtlview","cancel")%></td>
						</tr>
					</table>
				</div>
			</form>
		</div>	
	</div>
</div>

<div id="textareaWin" class="uploadWin">
	<div id="uploadwinheader" class="uploadWinHeader">
	  <div class="uploadWinClose" onclick="document.getElementById('textareaWin').style.display='none';">X</div><%=lan.getString("note","insertnote")%>
	</div>
	<div id="uploadwinbody" class="uploadWinBody">
		<div>
				<div style="width:95%;height:80%;align:center;padding:2%">
					<textarea id="note4setvalue" style="width: 100%;height: 100%;Overflow:auto;" onkeydown="note_filterInput(this,event);" rows="6"></textarea>
				</div>
				<div id="uploadwinbuttons">
					<table border="0">
						<tr>
							<td class="groupCategory_small" style="width:110px;height:30px;" onclick="writeNote();"><%=lan.getString("dtlview","submit")%></td>
							<td class="groupCategory_small" style="width:110px;height:30px;" onclick="document.getElementById('textareaWin').style.display='none'"><%=lan.getString("dtlview","cancel")%></td>
						</tr>
					</table>
				</div>
		</div>	
	</div>
</div>