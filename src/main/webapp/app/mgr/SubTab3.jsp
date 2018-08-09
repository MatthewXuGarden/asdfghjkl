<%@page import="com.carel.supervisor.director.packet.PacketMgr"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<%@ page language="java" 
	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.session.UserSession" 
	import="com.carel.supervisor.presentation.session.UserTransaction" 
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.director.*"
	import="java.util.Properties"
	import="java.util.Date"
	import="java.io.File" 
	import="com.carel.supervisor.presentation.bean.DevMdlBeanList" 
	import="com.carel.supervisor.presentation.session.Transaction"
	import="com.carel.supervisor.presentation.bean.FileDialogBean"
	import="com.carel.supervisor.presentation.bo.BSystem"
	import="com.carel.supervisor.base.conversion.DateUtils"
%>

<%	
			UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
			UserTransaction ut = sessionUser.getCurrentUserTransaction();
			Transaction transaction = sessionUser.getTransaction();
			boolean isProtected = ut.isTabProtected();
			String jsession = request.getSession().getId();
			Properties properties = transaction.getSystemParameter();
			String language = sessionUser.getLanguage();
			LangService l = LangMgr.getInstance().getLangService(language);
			
			
			String baseZipName = "";
			
			StringBuffer exportMsg = new StringBuffer();
			exportMsg.append("");
			
			if(properties==null)
			{		
				properties = new Properties();
			}
			
			
			String msgerr = properties.getProperty("error");
			if (null == msgerr)
			{
				msgerr = "";
			}
			
			boolean bActivated = false;
			boolean bLoad = false;
			bActivated = DirectorMgr.getInstance().isStarted();
			bLoad = DirectorMgr.getInstance().isInitialized();
		
			//info varie
			transaction.setSystemParameter(null);
			sessionUser.removeProperty("action");
			String images_export = "images/actions/export_on_black.png";
			String images_import = "images/actions/import_off.png";
			
			FileDialogBean fileDlg = new FileDialogBean(request);
			
			String fileName = BSystem.ZIP_NAME+DateUtils.date2String(new Date(),"yyyyMMddhhmmss");
			String fileNameSVG = BSystem.ZIP_NAME_4SVG+DateUtils.date2String(new Date(),"yyyyMMddhhmmss");
			String fileNameMdlSVG = BSystem.ZIP_NAME_4MDL;
			
			String images_export_on = "images/actions/export_on_black.png";
			String images_export_off = "images/actions/export_off.png";
			String images_import_on = "images/actions/import_on_black.png";
			String images_import_off = "images/actions/import_off.png";
			
			// Remote Control status
			File fRemoteControl = new File(BaseConfig.getCarelPath() + "tools\\Batch\\firewall.rc");
			boolean svgmaps_activation = PacketMgr.getInstance().isFunctionAllowed("svgmaps");
			boolean bRemoteControl = fRemoteControl.isFile();
			boolean bMapsExists = (new File(BaseConfig.getAppHome() + "app" + File.separator + "mstrmaps" + File.separator + "1.jsp").exists() ||
					new File(BaseConfig.getAppHome() + "app" + File.separator + "mstrmaps" + File.separator + "svgmaps" + File.separator + "webmi.js").exists())?true:false;
			
			File fFirefoxPortable = new File(BaseConfig.getCarelPath() + "tools\\Batch\\browsePVPRO.rc");
			boolean bFirefoxPortable = fFirefoxPortable.isFile();
%>
<input id="error" type="hidden" value="<%=msgerr%>" />
<input id="impjspmsg" type="hidden" value="<%=l.getString("mgr","impjspmsg")%>" />
<input id="impsvgmsg" type="hidden" value="<%=l.getString("mgr","impjspmsg")%>" />
<input id="delmapsmsg" type="hidden" value="<%=l.getString("mgr","delmapsmsg")%>"/>
<input id="isntxml" type="hidden" value="<%=l.getString("mgr","isntxml")%>" />
<input id="isntjsp" type="hidden" value="<%=l.getString("mgr","isntjsp")%>" />
<input id="errrul" type="hidden" value="<%=l.getString("mgr","errrul")%>" />
<input id="errdev" type="hidden" value="<%=l.getString("mgr","errdev")%>" />
<input id="impdevcrmsg" type="hidden" value="<%=l.getString("mgr","impdevcrmsg")%>" />
<input id="remdevmsg" type="hidden" value="<%=l.getString("mgr","remdevmsg")%>" />
<input id="imprulmsg" type="hidden" value="<%=l.getString("mgr","imprulmsg")%>" />
<input id="isntdev" type="hidden" value="<%=l.getString("mgr","isntdev")%>" />
<input id="enableRemoteControl" type="hidden" value="<%=l.getString("mgr","enableRemoteControl")%>" />
<input id="enableFirefoxPortable" type="hidden" value="<%=l.getString("mgr","enableFirefoxPortable")%>" />
<input id='save_confirm' type='hidden' value='<%=l.getString("fdexport","exportconfirm") %>' />
<input id='save_error' type='hidden' value="<%=l.getString("fdexport","exporterror") %>" />

<input id="impmsgdiv_vis" type="hidden" value="<%=properties.getProperty("impdevmsgvisib","")%>" />
<input id="impmsgdiv_disp" type="hidden" value="<%=properties.getProperty("impdevmsgdisp","")%>" />
<input id="window_msg" type="hidden" value="<%=properties.getProperty("windowmsg","")%>" />

<input id="sel_device" type="hidden" value="<%=l.getString("mgr","sel_device")%>" />


<%=fileDlg.renderFileDialog()%>

<table width='95%' cellpadding="2">	
<tr>
	<td>
		<fieldset class="field">
			<legend class="standardTxt" style="font-weight:bold"><%=l.getString("mgr","zipcfgdwn")%></legend>
			<br/>
			<form name="frmmgr1" id="frmmgr1" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
				<input id="action" name="action" type="hidden" value="exportxml"/>
				<table class='tableb' width="100%" height="45px">
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","expxml")%></b></td>
						<td class='table' width="35%" align='center'>
							<img title="<%=l.getString("mgr","expxml")%>" style="cursor:pointer" onclick="fdSetFile('<%=fileName %>');fdSaveFile('','zip',expsite_savefile)" src="<%=images_export%>"/>
						</td>
						<td class="table" width="20%">
						</td>
					</tr>
				</table>
			</form>
			<% if(svgmaps_activation) {%>
			<form name="frmmgr1b" id="frmmgr1b" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
				<input id="action" name="action" type="hidden" value="exportxmlforsvg"/>
				<table class='tableb' width="100%" height="45px">
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","expsvgxml")%></b></td>
						<td class='table' width="35%" align='center'>
							<img title="<%=l.getString("mgr","expsvgxml")%>" style="cursor:pointer" onclick="fdSetFile('<%=fileNameSVG %>');fdSaveFile('','zip',expsiteForSVG_savefile)" src="<%=images_export%>"/>
						</td>
						<td class="table" width="20%">
						</td>
					</tr>
				</table>
			</form>
			<form name="frmmgr1c" id="frmmgr1c" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
				<input id="action" name="action" type="hidden" value="expMdlConfForSVG"/>
				<table class='tableb' width="100%" height="45px">
					<tr class='standardTxt'>
						<td class='table' width="40"><b><%=l.getString("mgr","expmdlsvgxml")%></b></td>
						<td class='table' width="40%" align='center'>
							<table width="100%" cellpadding="1" cellspacing="1">
							  <tr valign="middle">
							  	<td align="right"><%=DevMdlBeanList.popolateComboDevMdl(sessionUser.getIdSite(), sessionUser.getLanguage())%></td>
								<td align="right"><img id='btn_exp_devdml' title="<%=l.getString("mgr","expmdlsvgxml")%>" style="cursor:pointer" onclick="if(document.getElementById('expsvg_iddevmdl').value!=-1){fdSetFile('<%=fileNameMdlSVG %>');fdSaveFile('','zip',expMdlForSVG_savefile)};" src="<%=images_export_off%>"/></td>
							</tr>
							</table>
						</td>
						<td class="table" width="20%">
						</td>
					</tr>
				</table>
			</form>
			<%} %>
			<br/>
		</fieldset>
		<br/>
		<br/>
		<fieldset class="field">
			<legend class="standardTxt" style="font-weight:bold"> <%=l.getString("mgr","devmdlmn")%></legend>
			<br/>
			<form name="frmmgr2" id="frmmgr2" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
				<input id="action" name="action" type="hidden" value="remdevcreator"/>
				<table class='tableb' width="100%" height="45px">
					<tr class='standardTxt'>	
						<td class='table' width="40%"><b><%=l.getString("mgr","remdevcreator")%></b></td>
						<td class='table' width="40%" align='center'>
							<table width="100%" cellpadding="1" cellspacing="1">
							  <tr valign="middle">
							  <td align="right"><%=DevMdlBeanList.popolateCombo(sessionUser.getIdSite(), sessionUser.getLanguage())%></td>
							  <td align="right"><img id='button7' title="<%=l.getString("mgr","remdevcreator")%>" style="cursor:pointer" src="images/actions/remove_off.png" onclick="actionConfirm('frmmgr2','button7','remdevcreator', 'remdevmsg');return false;"/></td>
							  </tr>
							</table>
						</td>
						<td class='table' width="30%" align='center'><%=properties.getProperty("remdevcrearetu","")%></td>
					</tr>
				</table>
			</form>
			<br/>

			<form name="uploadfrm1" id="uploadfrm1" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
				<input type="hidden" name="tipofile" value="dev" />
				<table class='tableb' width="100%" height="45px">
					<tr class='standardTxt'>	
						<td class='table' width="40%"><b><%=l.getString("mgr","impdevcreator")%></b></td>
						<td class='table' width="40%" align='center'>
						<table width="100%" cellpadding="1" cellspacing="1">
	  					  <tr valign="middle">
							<td align="right"><%=fileDlg.inputLoadFile("impdevcreator", "xml", "size='30%' onchange =\"enabImport('impdevcreator','button2','xml');\"", isProtected)%></td>
							<td align="right"><img id='button2' title="<%=l.getString("mgr","impdevcreator")%>" style="cursor:pointer" src="<%=images_import%>" onclick="actionConfirm('uploadfrm1','button2','impdevcreator','impdevcrmsg','xml','errdev');return false;"/></td>
						  </tr>
						</table>
						</td>
						<td class='table' width="30%" align='center'><%=properties.getProperty("impdevcreator","")%></td>
					</tr>
				</table>
			</form>
			<div id="impmsgdiv" style="visibility:<%=properties.getProperty("impdevmsgvisib","")%>;display:<%=properties.getProperty("impdevmsgdisp","")%>">
				<table class='tableb' width="100%" height="45px">
					<tr class='standardTxt'><td class='table' ><%=properties.getProperty("impdevvarmsg","")%></td></tr>
				</table>
			</div>
			<br/>
		</fieldset>
		<br/>
		<br/>

		<fieldset class="field">
			<legend class="standardTxt" style="font-weight:bold"> <%=l.getString("mgr","mapupld")%> </legend>
			<br/>
			<form name="uploadfrm2" id="uploadfrm2" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
				<input type="hidden" name="tipofile" value="jsp" />
				<table class='tableb' width="100%" height="45px">
					<tr class='standardTxt'>	
						<td class='table' width="40%"><b><%=l.getString("mgr","impjsp")%></b></td>
				     	<td class='table' width="40%" align='center'>
						<table width="100%" cellpadding="1" cellspacing="1">
	  					  <tr valign="middle">
							<td align="right"><%=fileDlg.inputLoadFile("impjsp", "zip", "size='30%' onchange =\"enabImport('impjsp','button1','zip');\"", isProtected)%></td>
				     		<td align="right"><img id='button1' onclick="actionConfirm('uploadfrm2','button1','impjsp','impjspmsg','zip','errdev');return false;" title="<%=l.getString("mgr","impjsp")%>" style="cursor:pointer" src="<%=images_import%>" /></td>						
						</tr>
						</table>			     		
						</td>
						<td class='table' width="30%" align='center'><%=properties.getProperty("impjsp","")%></td>
					</tr>
				</table>
				
				
				
			</form>
			<br/>
			<% if(svgmaps_activation) {%>
			<form name="uploadfrm2b" id="uploadfrm2b" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
				<input type="hidden" name="tipofile" value="svg" />
				<table class='tableb' width="100%" height="45px">
					<tr class='standardTxt'>	
						<td class='table' width="40%"><b><%=l.getString("mgr","impsvgmaps")%></b></td>
				     	<td class='table' width="40%" align='center'>
						<table width="100%" cellpadding="1" cellspacing="1">
	  					  <tr valign="middle">
							<td align="right"><%=fileDlg.inputLoadFile("impsvg", "zip", "size='30%' onchange =\"enabImport('impsvg','button3','zip');\"", isProtected)%></td>
				     		<td align="right"><img id='button3' onclick="actionConfirm('uploadfrm2b','button3','impsvg','impsvgmsg','zip','errdev');return false;" title="<%=l.getString("mgr","impsvgmaps")%>" style="cursor:pointer" src="<%=images_import%>" /></td>
						  </tr>
						</table>			     		
						</td>
						<td class='table' width="30%" align='center'><%=properties.getProperty("impsvg","")%></td>
					</tr>
				</table>
			</form>
			
			<form name="frmmgr4" id="frmmgr4" action="servlet/master;jsessionid=<%=jsession%>" method="post">
				<input id="action" name="action" type="hidden" value="remmaps"/>
				<table class='tableb' width="100%" height="45px"> 
						<tr class='standardTxt'>
							<td class='table' width="*"><b><%=l.getString("mgr","delmaps")%></b></td>
							<td class='table' width="35%" align='center'>
								<img title="Delete maps" style="cursor:pointer" id='svgmaps_clean' <%=(bMapsExists?"onclick=\"actionConfirm('frmmgr4','svgmaps_clean','remmaps','delmapsmsg');return false;\"  src=\"images/actions/remove_on_black.png\" style=\"cursor:pointer\"":"src=\"images/actions/remove_off.png\"")%> />
							</td>
							<td class='table' width="20%" align='center' style="word-wrap:break-word"></td>
						</tr>
					</table>
			</form>
			<!-- Import device detail page created with site.web IN PROGRESS -->
			<form name="uploadfrm3b" id="uploadfrm3b" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
				<input type="hidden" name="tipofile" value="svgdtl" />
				<table class='tableb' width="100%" height="45px">
					<tr class='standardTxt'>	
						<td class='table' width="40%"><b><%=l.getString("mgr","imp_svg_for_dtl")%></b></td>
				     	<td class='table' width="40%" align='center'>
						<table width="100%" cellpadding="1" cellspacing="1">
	  					  <tr valign="middle">
							<td align="right">
				     			<table>
				     				<tr height='50px'>
				     					<td align="left"><%=DevMdlBeanList.popolateComboDevFolders(sessionUser.getIdSite(), sessionUser.getLanguage())%></td>
				     					
				     				</tr>
				     				<tr>
				     					<td align="left"><%=fileDlg.inputLoadFile("impsvgdtl", "zip", "size='30%' onchange =\"enabImport('impsvgdtl','button4','zip');\"", isProtected)%></td>
				     				</tr>
				     			</table>
				     		</td>
				     		<td align="right" style="vertical-align:bottom"><img id='button4' onclick="confirm_upload_devdtl('uploadfrm3b','button4','dev_folder','impsvgdtl','impsvgmsg','zip','errdev');return false;" title="<%=l.getString("mgr","impsvgdtlmaps")%>" style="cursor:pointer" src="<%=images_import%>" /></td>
						  </tr>
						</table>			     		
						</td>
						<td class='table' width="30%" align='center'><%=properties.getProperty("impsvgdtl","")%></td>
					</tr>
				</table>
			</form>
			
			<%} %>
			<br/>
			<form name="frmmgr3" id="frmmgr3" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
				<input id="action" name="action" type="hidden" value="remote_control"/>
				<input id="remote_control" name="remote_control" type="hidden"/>
				<table class='tableb' width="100%" height="45px">
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","remote_control")%></b></td>
						<td class='table' width="35%" align="center">
						 <img title="<%=l.getString("mgr","start")%>" <%=(!bRemoteControl ? "onclick=\"enableRemoteControl(true);\" src=\"images/actions/start_on_black.png\" style=\"cursor:pointer\"":"src=\"images/actions/start_off.png\"")%> />
						 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						 <img title="<%=l.getString("mgr","stop")%>" <%=(bRemoteControl ? "onclick=\"enableRemoteControl(false);\" src=\"images/actions/stop_on_black.png\" style=\"cursor:pointer\"": "src=\"images/actions/stop_off.png\"")%> />
						</td>
						<td class='table' align='center' width="20%" <%=bRemoteControl ? "style='color:GREEN;'" : "style='color:RED;'"%> >
						 <B><%=bRemoteControl ? l.getString("mgr","active") : l.getString("mgr","disactive")%></B>
						</td>
					</tr>
				</table>
			</form>
			
			<br/>
			<form name="frmmgr5" id="frmmgr5" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
				<input id="action" name="action" type="hidden" value="firefox_portable"/>
				<input id="firefox_portable" name="firefox_portable" type="hidden"/>
				<table class='tableb' width="100%" height="45px">
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","firefox_portable")%></b></td>
						<td class='table' width="35%" align="center">
						 <img title="<%=l.getString("mgr","start")%>" <%=(!bFirefoxPortable ? "onclick=\"enableFirefoxPortable(true);\" src=\"images/actions/on_on_black.png\" style=\"cursor:pointer\"":"src=\"images/actions/on_off.png\"")%> />
						 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						 <img title="<%=l.getString("mgr","stop")%>" <%=(bFirefoxPortable ? "onclick=\"enableFirefoxPortable(false);\" src=\"images/actions/off_on_black.png\" style=\"cursor:pointer\"": "src=\"images/actions/off_off.png\"")%> />
						</td>
						<td class='table' align='center' width="20%" <%=bFirefoxPortable ? "style='color:GREEN;'" : "style='color:RED;'"%> >
						 <B><%=bFirefoxPortable ? l.getString("mgr","on") : l.getString("mgr","off")%></B>
						</td>
					</tr>
				</table>
			</form>
		</fieldset>
			<br/>
			<br/>
	<fieldset class="field">
	<legend class="standardTxt" style="font-weight:bold"><%=l.getString("mgr","picupld")%> </legend>
		<br/>
			<form name="uploadfrm4" id="uploadfrm4" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
			<input type="hidden" name="tipofile" value="toplogoimg" />
			<%=l.getString("mgr","imgmark")%>
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
			<tr class='standardTxt'>
				<td class='table' width="40%"><b><%=l.getString("mgr","changeimgtop")%></b></td>
				<td class='actionTD' width="40%" >
					<table cellpadding="1" cellspacing="1" width="100%">
						<tr>
							<td align="right">
							<%=fileDlg.inputLoadFile("changeimgtop", "png,jpg,bmp", "size='30%'", isProtected)%>
							</td>
							<td align="right"><img title="<%=l.getString("mgr","changeimgtop")%>" style="cursor:pointer" src="<%=images_import_on%>" onclick="actionForm('uploadfrm4','changeimgtop');return false;" /></td>
							<!--  quello che c'era prima era: "actionForm('changeimgtop')"; id="changeimgtop" name="imgimporttop" -->
						</tr>
					</table>
				</td>
				<td class='table' width="30%" align='center'><%=properties.getProperty("changeimgtop","")%></td>
			</tr>
			</table>
			</form>
		<br/>
		
		<form name="uploadfrm5" id="uploadfrm5" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
		<input type="hidden" name="tipofile" value="loginimg" />
		<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
			<tr class='standardTxt'>
				<td class='table' width="40%"><b><%=l.getString("mgr","changeimg")%></b></td>
				<td class='actionTD' width="40%" >
					<table cellpadding="1" cellspacing="1" width="100%">
						<tr>
							<td align="right">
							<%=fileDlg.inputLoadFile("changeimg", "png,jpg,bmp", "size='30%'", isProtected)%>
							</td>
							<td align="right"><img title="<%=l.getString("mgr","changeimg")%>" style="cursor:pointer" src="<%=images_import_on%>" onclick="actionForm('uploadfrm5','changeimg');return false;" /></td>
							<!-- quello che c'era prima era: "actionForm('changeimg')"; id="changeimg" name="imgimport" -->
						</tr>
					</table>
				</td>
				<td class='table' width="20%" align='center'><%=properties.getProperty("changeimg","")%></td>
			</tr>
		</table>
		</form>
		<br/> 
	</fieldset>

	<br/>
	</td>
</tr>	
</table>