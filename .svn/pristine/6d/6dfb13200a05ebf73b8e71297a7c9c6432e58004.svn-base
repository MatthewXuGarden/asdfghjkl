<%@ page language="java" 

	import="java.util.Properties"
	import="java.io.File" 

	import="com.carel.supervisor.presentation.helper.ServletHelper"
	import="com.carel.supervisor.presentation.bo.helper.BackupHelper"
	import="com.carel.supervisor.presentation.session.UserSession" 
	import="com.carel.supervisor.presentation.session.UserTransaction" 
	import="com.carel.supervisor.dataaccess.language.LangService"
	import="com.carel.supervisor.dataaccess.language.LangMgr"
	import="com.carel.supervisor.base.config.BaseConfig"
	import="com.carel.supervisor.director.*"
	import="com.carel.supervisor.presentation.bean.DevMdlBeanList"
	import="com.carel.supervisor.dataaccess.dataconfig.ProductInfo" 
	import="com.carel.supervisor.presentation.session.Transaction"
	import="com.carel.supervisor.presentation.helper.FtpCommander"
	import="com.carel.supervisor.presentation.helper.VirtualKeyboard"
	import="com.carel.supervisor.presentation.helper.Buzzer"
	import="com.carel.supervisor.presentation.bean.FileDialogBean"
	import="com.carel.supervisor.presentation.helper.ModbusSlaveCommander"
	import="com.carel.supervisor.base.config.*"	
	import="com.carel.supervisor.base.profiling.IProfiler"
	import="com.carel.supervisor.base.profiling.ProfilingMgr"
	
	import="com.carel.supervisor.dataaccess.datalog.impl.LangUsedBean"
	import="com.carel.supervisor.dataaccess.datalog.impl.LangUsedBeanList"
%>
<%	
	IProductInfo product = ProductInfoMgr.getInstance().getProductInfo();
	String safetyLevel = product.get("showsafetylevel");
	String safetyLevelYes = "1";
	String safetyLevelNo = "0";
	
	UserSession sessionUser = ServletHelper.retrieveSession(request.getRequestedSessionId(), request);
	UserTransaction ut = sessionUser.getCurrentUserTransaction();
	Transaction transaction = sessionUser.getTransaction();
	boolean isProtected = ut.isTabProtected();
	String jsession = request.getSession().getId();
	Properties properties = transaction.getSystemParameter();
	String language = sessionUser.getLanguage();
	LangService l = LangMgr.getInstance().getLangService(language);
	
	StringBuffer exportMsg = new StringBuffer();
	exportMsg.append("");
	if(properties!=null)
	{		
			if(properties.get("action")!=null)
			{
				if(properties.get("action").equals("exportxml"))
				{
					exportMsg.append(BaseConfig.getCarelPath());
					exportMsg.append("ide" + File.separator + "export");
				}
			}
	}
	else
	{
		properties = new Properties();
	}
	boolean bActivated = false;
	boolean bLoad = false;
	bActivated = DirectorMgr.getInstance().isStarted();
	bLoad = DirectorMgr.getInstance().isInitialized();
	
	boolean FTPactive = FtpCommander.testFTP();

	boolean OnScreenKey = VirtualKeyboard.getInstance().isOnScreenKey();
	boolean BuzzerOn = Buzzer.isBuzzerOn();
	
	IProfiler profiler = ProfilingMgr.getInstance().getProfiler();
	boolean RemoteUserOn = profiler.isRemoteManagementActive();
	//info varie
			
	String msgerr = properties.getProperty("error");
	if (null == msgerr)
	{
		msgerr = "";
	}
	transaction.setSystemParameter(null);
	sessionUser.removeProperty("action");
	
	
	String images_export_on = "images/actions/export_on_black.png";
	String images_export_off = "images/actions/export_off.png";
	String images_import_on = "images/actions/import_on_black.png";
	String images_import_off = "images/actions/import_off.png";
	
	//Lista delle cartelle di backup
	String dirBackup = BaseConfig.getCarelPath() + BackupHelper.BACKUP_PATH + File.separator + BackupHelper.ALL_PATH;
	File dir=new File(dirBackup);
	String[] dirList = dir.list();
	StringBuffer dirBckSelect= new StringBuffer();
	dirBckSelect.append("<option value=\"\">----------------------\n</option>");
	for(int i=0;i<dirList.length;i++){
		dirBckSelect.append("<option value=\"");
		dirBckSelect.append(dirList[i]);
		dirBckSelect.append("\">");
		dirBckSelect.append(dirList[i]);
		dirBckSelect.append("</option>");
		dirBckSelect.append("\n");
	}//for
		
	ProductInfo p = new ProductInfo();
	p.load();
	String home_sel = p.get("home");
	String timesession_sel = p.get("session");
	
	String savePath = properties.getProperty("pathexp");
	String lastPathDir = "";
	
	if ((savePath != null) && (! "".equals(savePath)))
	{
		lastPathDir = lastPathDir + savePath.substring(savePath.lastIndexOf(File.separator)+1);
	}
	
	//if(properties!=null)
	//{		
	//		if(properties.getProperty("action")!=null)
	//		{
	//			if(properties.getProperty("action").equals("backupexp") || properties.getProperty("action").equals("sitemgrexp") || properties.getProperty("action").equals("rulemgrexp"))
	//			{
	//				// properties.removeProperty("action");
	//				properties.setProperty(properties.getProperty("action"), lastPathDir);
	//			}
	//		}
	//}
	
	String timeoutSessione = l.getString("mgr","timeoutsession");
	
	//dealing with the [reboot] button. --simon add. 
	boolean other_mgt_show=true;
	boolean reboot_show=true;
	
	/*
	if(sessionUser.getProfile()>0){
		if(sessionUser.getPermission(18)==0){
			switch(sessionUser.getPermission(8)){
				case 0:
						break;
				case 1:
						break;
				case 2: other_mgt_show=true;
						reboot_show=false;
						break;
			}
		}else if(sessionUser.getPermission(18)==2){
			switch(sessionUser.getPermission(8)){
				case 0: other_mgt_show=false;
						reboot_show=true;
						break;
				case 1: other_mgt_show=false;
						reboot_show=true;
						break;
				case 2: other_mgt_show=true;
					reboot_show=true;
					break;
			}
		}
	} */
	
	boolean modbusslaveactive = false;
	String modbusslave_autostart = "";	
	int[] modbusslave = ModbusSlaveCommander.getStateAndStarttype();
	if(modbusslave != null && modbusslave.length == 2)
	{
		if(modbusslave[0] == ModbusSlaveCommander.E_STATE_RUNNING)
			modbusslaveactive = true;
		if( modbusslave[1] == ModbusSlaveCommander.E_STARTTYPE_AUTO_START )
			modbusslave_autostart = "checked";
	}
	
	FileDialogBean fileDlg = new FileDialogBean(request);
	
	String alertImport = "(Before executing the import operation please check the product type PP2*****E0 / PP2*****P0 and the software version)";
	
	String txtconfirmhide = l.getString("mgr","confirmhide");	
	String txtHideSafety = l.getString("mgr", "hidesafety");
	String txtShowSafety = l.getString("mgr", "showsafety");
	
	StringBuffer sb = new StringBuffer("");
	LangUsedBean[] lista = null;
	boolean error = false;
	String sCode = "";
	String sLang = "";
	String sEnco = "";
	LangUsedBeanList langused = new LangUsedBeanList();
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
		}
		catch(Exception e2)
		{
			sCode = "EN_en";
			sLang = "English";
		}	
	}
	
	if(!error) {
		String login_language = BaseConfig.getProductInfo("login_language");
		login_language = (login_language==null?"":login_language.trim());
		for(int i=0; i<lista.length; i++){
			sb.append("<option value=\""+lista[i].getLangcode()+"\" "+  ( (login_language.equalsIgnoreCase(lista[i].getLangcode().trim()))?"selected":"" ) +">"+lista[i].getLangdescription()+"</option>");
		}
	}
	else
		sb.append("<option value=\""+sCode+"\">"+sLang+"</option>");
	String version = BackupHelper.getVersion();
	
	// license agreement
	String strLicense = ProductInfoMgr.getInstance().getProductInfo().get("license_agreement");
	boolean bLicense = strLicense != null && (strLicense.equalsIgnoreCase("yes") || strLicense.equalsIgnoreCase("true"));
	
	// set value with note
	String strValueNote = ProductInfoMgr.getInstance().getProductInfo().get("value_note");
	boolean bValueNote = strValueNote != null && (strValueNote.equalsIgnoreCase("yes") || strValueNote.equalsIgnoreCase("true"));
%>

<input id="pathsysfile" type="hidden" value="<%=savePath%>"/>  
<input id="isntimage" type="hidden" value="<%=l.getString("mgr","isntimage")%>" /> 
<input id="isntxml" type="hidden" value="<%=l.getString("mgr","isntxml")%>" />
<input id="isntzip" type="hidden" value="<%=l.getString("mgr","isntzip")%>" />
<input id="stop" type="hidden" value="<%=l.getString("mgr","stopmsg")%>" />
<input id="start" type="hidden" value="<%=l.getString("mgr","startmsg")%>" />
<input id="rest" type="hidden" value="<%=l.getString("mgr","restmsg")%>" />
<input id="commmsg" type="hidden" value="<%=l.getString("mgr","commmsg")%>" />
<input id="impmsg" type="hidden" value="<%=l.getString("mgr","impmsg")%>" />
<input id="expmsg" type="hidden" value="<%=l.getString("mgr","expmsg")%>" />
<input id="expmsg2" type="hidden" value="<%=l.getString("mgr","expmsg2")%>" />
<input id="errall" type="hidden" value="<%=l.getString("mgr","errall")%>" />
<input id="errconf" type="hidden" value="<%=l.getString("mgr","errconf")%>" />
<input id="errrul" type="hidden" value="<%=l.getString("mgr","errrul")%>" />
<input id="errdev" type="hidden" value="<%=l.getString("mgr","errdev")%>" />
<input id="imprulmsg" type="hidden" value="<%=l.getString("mgr","imprulmsg")%>" />
<input id="reloaddevmsg" type="hidden" value="<%=l.getString("mgr","reldevmsg")%>" />
<input id="mainmsg" type="hidden" value="<%=l.getString("mgr","mainmsg")%>" />
<input id="ssd_msg" type="hidden" value="<%=l.getString("mgr","ssd_msg")%>">
<input id="error" type="hidden" value="<%=msgerr%>" />
<input id="isntjsp" type="hidden" value="<%=l.getString("mgr","isntjsp")%>" />
<input id="isntconf" type="hidden" value="<%=l.getString("mgr","isntconf")%>" />
<input id="isntrule" type="hidden" value="<%=l.getString("mgr","isntrule")%>" />
<input type='hidden' id='save_confirm' value='<%=l.getString("fdexport","exportconfirm") %>' />
<input type='hidden' id='save_error' value="<%=l.getString("fdexport","exporterror") %>" />
<input type='hidden' id='existsitebackup' value='<%=l.getString("mgr","existsitebackup") %>' />
<input type='hidden' id='selectstarttype' value='<%=l.getString("mgr","selectstarttype") %>' />
<input type='hidden' id='cnfmsysrestart' value='<%=l.getString("mgr","cnfmsysrestart") %>' />
<input type="hidden" id="txtconfirmhide" value="<%=txtconfirmhide %>"/>
<input type="hidden" id="version" value="<%=version %>"/>

<%=fileDlg.renderFileDialog()%>

<table width="95%" height="100%" cellpadding="1" >

<tr>
	<td>

	<form name="frmmgr1" id="frmmgr1" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 
		<input id="action" name="action" type="hidden" value=""/>
		
		<fieldset class="field">
			<legend class="standardTxt" style="font-weight:bold"><%=l.getString("mgr","parconf")%></legend>
			<br/>
			<table width="100%" height="45px">
				<tr class='standardTxt'>
					<td class='table' width="*"><b><%=l.getString("mgr","enginestatus")%></b></td>
					<td class='actionTD' width="35%">
						<%if(other_mgt_show==true){ %>	
							<img title="<%=l.getString("mgr","start")%>" name='startbtn' id='startbtn' <%=(!bActivated ?"onclick=\"setAction('action','start');actionConfirm('frmmgr1', 'startbtn','start','start');return false;\"  src=\"images/actions/start_on_black.png\" style=\"cursor:pointer\"":"src=\"images/actions/start_off.png\"")%>/>
							&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
							<img title="<%=l.getString("mgr","stop")%>" name='stopbtn' id='stopbtn' <%=(bActivated ?"onclick=\"setAction('action','stop');stop_broad('actionConfirm','frmmgr1','stopbtn','stop','stop');return false;\"  src=\"images/actions/stop_on_black.png\" style=\"cursor:pointer\"":"src=\"images/actions/stop_off.png\"")%>/>
						<%} %>
						<%if(reboot_show==true){ %>	
							&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
							<img title="<%=l.getString("mgr","restart")%>" name='restbtn' id='restbtn' style="cursor:pointer" onclick="setAction('action','rest');stop_broad('actionConfirm','frmmgr1','restbtn','rest','rest');"  src="images/actions/restart_on_black.png" />
						<%} %>
					</td>
					<td class='table' width="20%" align='center' width="30%" <%=(bActivated ? "style='color:GREEN;'":"style='color:RED;'")%> >
						<B><%=(bActivated ? l.getString("mgr","active"): l.getString("mgr","disactive"))%></B>
					</td>
				</tr>
			</table>
		<%if(other_mgt_show==true){ %>	
			<br/>

			<table width="100%" height="45px" >
				<tr class='standardTxt'>
					<td class='table' width="*"><b><%=l.getString("mgr","sysrestart")%></b></td>
					<td class='actionTD' width="35%" >
						<img title="" style="cursor:pointer" src="images/actions/refresh_on_black.png" id='sysrst' onclick="setAction('action','sysrestart');actionConfirm('frmmgr1','sysrst','sysrestart','cnfmsysrestart');return false;" />
					</td>
					<td class='table' width="20%" align='center' style="word-wrap:break-word"></td>
				</tr>
			</table>	

			<br/>
			
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
				<tr class='standardTxt'>
					<td class='table' width="*"><b><%=l.getString("mgr","screenkey")%></b></td>
					<td class='actionTD' width="35%" >
					 <img title="<%=l.getString("mgr","enable")%>" name='startbtn' id='startbtn' <%=(!OnScreenKey ? "onclick=\"doVirtualKey('frmmgr1',true);return false;\" src=\"images/actions/on_on_black.png\" style=\"cursor:pointer\"":"src=\"images/actions/on_off.png\"")%> />
					 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					 <img title="<%=l.getString("mgr","disable")%>" name='stopbtn' id='stopbtn' <%=(OnScreenKey ? "onclick=\"doVirtualKey('frmmgr1',false);return false;\" src=\"images/actions/off_on_black.png\" style=\"cursor:pointer\"": "src=\"images/actions/off_off.png\"")%> />
					</td>
					<td class='table' width="20%" align='center' width="30%" <%=(OnScreenKey ? "style='color:GREEN;'":"style='color:RED;'")%> >
					 <B><%=(OnScreenKey ? l.getString("mgr","on"): l.getString("mgr","off"))%></B>
					 <input type='hidden' id='whereKey' name='whereKey' value='' />
					</td>
				</tr>
			</table>

			<br>
			
			<table width="100%" height="45px" >
				<tr class='standardTxt'>
					<td class='table' width="*"><b><%=l.getString("mgr","reldev")%></b></td>
					<td class='actionTD' width="35%" >
						<img title="" style="cursor:pointer" src="images/actions/refresh_on_black.png" id='refr' onclick="setAction('action','reloaddevice');actionConfirm('frmmgr1','refr','reloaddevice','reloaddevmsg');return false;" />
					</td>
					<td class='table' width="20%" align='center' style="word-wrap:break-word"></td>
				</tr>
			</table>	
			
			<br/>
			
			<table width="100%" height="45px" >
				<tr class='standardTxt'>
					<td class='table' width="*"><b><%=l.getString("mgr","maintenance")%></b></td>
					<td class='actionTD' width="35%">
						<img title="" style="cursor:pointer" src="images/actions/maintenance_on_black.png" id='img_maint' onclick="setAction('action','maintenance');stop_broad('actionConfirm','frmmgr1','img_maint','maintenance','mainmsg');return false;" />
					</td>
					<td class='table' width="20%" align='center' style="word-wrap:break-word"></td>
				</tr>
			</table>
			<br/>

			<table width="100%" height="45px" >
				<tr class='standardTxt'>
					<td class='table' width="*"><b><%=l.getString("mgr", "ssd_stat")%></b></td>
					<td class='actionTD' width="35%">
						<img title="" style="cursor:pointer" src="images/actions/maintenance_on_black.png" id='img_ssd_stat' onclick="setAction('action','ssd_stat');stop_broad('actionConfirm','frmmgr1','img_ssd_stat','ssd_stat','ssd_msg');return false;" />
					</td>
					<td class='table' width="20%" align='center' style="word-wrap:break-word"></td>
				</tr>
			</table>
			<br/>

			<% if (!isProtected) {%>	
				<table width="100%" height="45px" > 
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","comm")%></b></td>
						<td class='actionTD' width="35%" >
							<img title="<%=l.getString("mgr","commlbl")%>" style="cursor:pointer" src="images/actions/clean_on_black.png" id='img_clean' onclick="setAction('action','reset');stop_broad('actionConfirm','frmmgr1','img_clean','reset','commmsg');return false;" />
						</td>
						<td class='table' width="20%" align='center' style="word-wrap:break-word"></td>
					</tr>
				</table>
				<br/>
			<%}%>
				
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
				<tr class='standardTxt'>
					<td class='table' width="*"><b><%=l.getString("mgr","changehome")%></b></td>
					<td class='actionTD' width="35%"  valign='middle'>
						<table cellpadding="1" cellspacing="1">
							<tr>
								<td align="right">
									<select id='homeselect' name='homeselect'>
										<option <%=(home_sel.equalsIgnoreCase("0")?"selected":"")%> value='0'><%=l.getString("mgr","vdefault")%></option>
										<option <%=(home_sel.equalsIgnoreCase("1")?"selected":"")%> value='1'><%=l.getString("mgr","vdevicelist")%></option>
										<option <%=(home_sel.equalsIgnoreCase("2")?"selected":"")%> value='2'><%=l.getString("mgr","vmap")%></option>
									</select>
								</td>
								<td align="right"><img title="<%=l.getString("mgr","changehome")%>" style="cursor:pointer" src="images/actions/save_on_black.png" onclick="setAction('action','changehome');actionForm('frmmgr1','changehome');return false;"/></td>
							</tr>
						</table>
					</td>
					<td class='table' width="20%" align='center'><%=properties.getProperty("changehome","")%></td>
				</tr>
			</table>
			<br/>
			
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
				<tr class='standardTxt'>
					<td class='table' width="*"><b><%=l.getString("mgr","deflanguage")%></b></td>
					<td class='actionTD' width="35%"  valign='middle'>
						<table cellpadding="1" cellspacing="1">
							<tr>
								<td align="right">
									<select name="deflanguageselect" id="defLanguage"  >
					      					<%=sb.toString()%>
					      			</select>
								</td>
								<td align="right"><img title="<%=l.getString("mgr","deflanguage")%>" style="cursor:pointer" src="images/actions/save_on_black.png" onclick="setAction('action','deflanguage');actionForm('frmmgr1','deflanguage');return false;"/></td>
							</tr>
						</table>
					</td>
					<td class='table' width="20%" align='center'><%=properties.getProperty("changehome","")%></td>
				</tr>
			</table>
			<br/>
			
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
				<tr class='standardTxt'>
					<td class='table' width="*"><b><%=timeoutSessione%></b></td>
					<td class='actionTD' width="35%"  valign='middle'>
						<table cellpadding="1" cellspacing="1">
							<tr>
								<td align="right">
									<select id='timeoutselect' name='timeoutselect'>
										<option <%=(timesession_sel.equalsIgnoreCase("5")?"selected":"")%>  value='5'>5</option>
										<option <%=(timesession_sel.equalsIgnoreCase("15")?"selected":"")%>  value='15'>15</option>
										<option <%=(timesession_sel.equalsIgnoreCase("30")?"selected":"")%>  value='30'>30</option>
										<option <%=(timesession_sel.equalsIgnoreCase("60")?"selected":"")%>  value='60'>60</option>
										<option <%=(timesession_sel.equalsIgnoreCase("120")?"selected":"")%> value='120'>120</option>
										<option <%=(timesession_sel.equalsIgnoreCase("240")?"selected":"")%> value='240'>240</option>
									</select>
								</td>
								<td align="right"><img title="<%=timeoutSessione%>" style="cursor:pointer" src="images/actions/save_on_black.png" onclick="setAction('action','sessiontimeout');actionForm('frmmgr1','sessiontimeout');return false;"/></td>
							</tr>
						</table>
					</td>
					<td class='table' width="20%" align='center'><%=properties.getProperty("timeoutok","")%></td>
				</tr>
			</table>
			<br/>		
		
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","ftpsrv")%></b></td>
						<td class='actionTD' width="35%" >
						 <img title="<%=l.getString("mgr","start")%>" name='startbtn' id='startbtn' <%=(!FTPactive ? "onclick=\"setAction('action','FTPstart');actionForm('frmmgr1','FTPstart');return false;\" src=\"images/actions/start_on_black.png\" style=\"cursor:pointer\"":"src=\"images/actions/start_off.png\"")%> />
						 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						 <img title="<%=l.getString("mgr","stop")%>" name='stopbtn' id='stopbtn' <%=(FTPactive ? "onclick=\"setAction('action','FTPstop');actionForm('frmmgr1','FTPstop');return false;\" src=\"images/actions/stop_on_black.png\" style=\"cursor:pointer\"": "src=\"images/actions/stop_off.png\"")%> />
						</td>
						<td class='table' align='center' width="20%" <%=(FTPactive ? "style='color:GREEN;'":"style='color:RED;'")%> >
						 <B><%=(FTPactive ? l.getString("mgr","active"): l.getString("mgr","disactive"))%></B>
						</td>
					</tr>
			</table>
			<br/>
			
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","modbusslavesrv")%></b></td>
						<td class='actionTD' width="35%" >
						 <img title="<%=l.getString("mgr","start")%>" <%=(!modbusslaveactive ? "onclick=\"setAction('action','modbusslavestart');actionForm('frmmgr1','modbusslavestart');return false;\" src=\"images/actions/start_on_black.png\" style=\"cursor:pointer\"":"src=\"images/actions/start_off.png\"")%> />
						 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						 <img title="<%=l.getString("mgr","stop")%>" <%=(modbusslaveactive ? "onclick=\"setAction('action','modbusslavestop');actionForm('frmmgr1','modbusslavestop');return false;\" src=\"images/actions/stop_on_black.png\" style=\"cursor:pointer\"": "src=\"images/actions/stop_off.png\"")%> />
						</td>
						<td class='table' align='center' width="20%" <%=(modbusslaveactive ? "style='color:GREEN;'":"style='color:RED;'")%> >
						 <B><%=(modbusslaveactive ? l.getString("mgr","active"): l.getString("mgr","disactive"))%></B>
						</td>
					</tr>
			</table>
			<br/>
			
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","modbusslavestarttype")%></b></td>
						<td class='actionTD' width="35%" >
							<table cellpadding="1" cellspacing="1">
								<tr>
									<td align="center">
										<input type="checkbox" id="modbusslave_autostart" name="modbusslave_autostart" <%=modbusslave_autostart%>>
									</td>
									<td align="right">
										<img title="<%=l.getString("mgr","modbusslavestarttype")%>" style="cursor:pointer" src="images/actions/save_on_black.png" onclick="setAction('action','modbusslave_autostart');actionForm('frmmgr1','modbusslavestarttype');return false;"/>
									</td>
								</tr>
							</table>
						</td>
						<td class='table' align='center' width="20%" >
						</td>
					</tr>
			</table>
			<br/>
			
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=txtHideSafety %></b></td>
						<td class='actionTD' width="35%" >
							<table cellpadding="1" cellspacing="1">
								<tr>
									<td align="center"><input type="checkbox" value="0" id="rd_hide" name="rd_hide" <%= ( safetyLevel.equalsIgnoreCase(safetyLevelNo) ? "checked=\"checked\"" : "" ) %> /></td>
									<td align="right">
										<img title="<%= ( safetyLevel.equalsIgnoreCase(safetyLevelNo) ? txtHideSafety : txtShowSafety ) %>" style="cursor:pointer" src="images/actions/save_on_black.png" onclick="setAction('action','safetylevel');actionForm('frmmgr1','safetylevel');return false;"/>
									</td>
								</tr>
							</table>
						</td>
						<td class='table' align='center' width="20%" ></td>
					</tr>
			</table>
			<br/>
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
				<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","buzzer")%></b></td>
						<td class='actionTD' width="35%" >
						 <img title="<%=l.getString("mgr","enablebuzz")%>" name='buzzeron' id='buzzeron' <%=(!BuzzerOn ? "onclick=\"activeBuzzer('frmmgr1',true);return false;\" src=\"images/actions/on_on_black.png\" style=\"cursor:pointer\"":"src=\"images/actions/on_off.png\"")%> />
						 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						 <img title="<%=l.getString("mgr","disablebuzz")%>"
					name='buzzeroff' id='buzzeroff'
					<%=(BuzzerOn ? "onclick=\"activeBuzzer('frmmgr1',false);return false;\" src=\"images/actions/off_on_black.png\" style=\"cursor:pointer\"": "src=\"images/actions/off_off.png\"")%> />
						</td>
						<td class='table' width="20%" align='center' width="30%" <%=(BuzzerOn ? "style='color:GREEN;'":"style='color:RED;'")%> >
						 <B><%=(BuzzerOn ? l.getString("mgr","buzzeron"): l.getString("mgr","buzzeroff"))%></B>
						</td>
				</tr>
			</table>
			<br/>
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
				<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","enableremoteuser")%></b></td>
						<td class='actionTD' width="35%" >
						 <img title="<%=l.getString("mgr","enableremoteuser")%>" name='remoteuser' id='remoteuser' <%=(!RemoteUserOn ? "onclick=\"activeRemoteUsersMngm('frmmgr1',true);return false;\" src=\"images/actions/on_on_black.png\" style=\"cursor:pointer\"":"src=\"images/actions/on_off.png\"")%> />
						 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
						 <img title="<%=l.getString("mgr","disableremoteuser")%>" name='remoteuser' id='remoteuser' <%=(RemoteUserOn ? "onclick=\"activeRemoteUsersMngm('frmmgr1',false);return false;\" src=\"images/actions/off_on_black.png\" style=\"cursor:pointer\"": "src=\"images/actions/off_off.png\"")%> />
						</td>
						<td class='table' width="20%" align='center' width="30%" <%=(RemoteUserOn ? "style='color:GREEN;'":"style='color:RED;'")%> >
						 <B><%=(RemoteUserOn ? l.getString("mgr","active"): l.getString("mgr","disactive"))%></B>
						</td>
				</tr>
			</table>
			<br>
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("license", "license_agreement")%></b></td>
						<td class='actionTD' width="35%" >
							<table cellpadding="1" cellspacing="1">
								<tr>
									<td align="center"><input type="checkbox" value="0" id="license_agreement" name="license_agreement" <%=bLicense ? "checked" : ""%> /></td>
									<td align="right">
										<img style="cursor:pointer" src="images/actions/save_on_black.png" onclick="setAction('action','license');actionForm('frmmgr1','license_agreement');return false;"/>
									</td>
								</tr>
							</table>
						</td>
						<td class='table' align='center' width="20%" ></td>
					</tr>
			</table>
			<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("note", "systempage")%></b></td>
						<td class='actionTD' width="35%" >
							<table cellpadding="1" cellspacing="1">
								<tr>
									<td align="center"><input type="checkbox" value="0" id="value_note" name="value_note" <%=bValueNote ? "checked" : ""%> /></td>
									<td align="right">
										<img style="cursor:pointer" src="images/actions/save_on_black.png" onclick="setAction('action','value_note');actionForm('frmmgr1','value_note');return false;"/>
									</td>
								</tr>
							</table>
						</td>
						<td class='table' align='center' width="20%" ></td>
					</tr>
			</table>
		<%} %>
		</fieldset>
	</form>
	<!-- fine form administration tools -->
	
	<%if(other_mgt_show==true){ %>
		<br/>	
		<% if (!isProtected) {%>
			<br/>
			<fieldset class="field">
				<legend class="standardTxt" style="font-weight:bold"><%=l.getString("mgr","sitebackup")%></legend>
				<br/>		
				<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","backupexp")%></b></td>
						<td class='actionTD' width="35%" >
							<img title="<%=l.getString("mgr","backupexp")%>" style="cursor:pointer" src="<%=images_export_on%>" id='img_backup' onclick="setAction('action','backupexp');actionConfirm('frmmgr1','img_backup','backupexp','expmsg');return false;" />
						</td>
						<td class='table' width="20%" align='center'><%=properties.getProperty("backupexp","")%></td>
					</tr>
				</table>
				<br/>
				
				<form name="frmmgr2" id="frmmgr2" action="servlet/master;jsessionid=<%=jsession%>" method="post">
					<input id="action1" name="action" type="hidden" value=""/>
					<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
						<tr class='standardTxt'>
							<td class='table' width="*"><b><%=l.getString("mgr","backupimp")%></b><br><%=alertImport %></td>
							<td class='actionTD' width="35%" >
								<table cellpadding="1" cellspacing="1">
									<tr>
										<td align="right"><select id="backupimp" name="backupimp" onchange ="checkImportAll(this,'button6');" ><%=dirBckSelect%></select></td>
										<td align="right"><img id="button6" title="<%=l.getString("mgr","backupimp")%>" src="<%=images_import_off%>" onclick="setAction('action1','backupimp');stop_broad('actionConfirm','frmmgr2','button6','backupimp','impmsg','','errall');return false;" /></td>
									</tr>						
								</table>
							</td>
							<td class='table' width="20%" align='center'><%=properties.getProperty("backupimp","")%></td>
						</tr>
					</table>
				</form>
				<br/>
				
				<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","backupdownload")%></b></td>
						<td class='actionTD' width="35%" >
							<table cellpadding="1" cellspacing="1">
								<tr>
									<td align="right"><select id="backupdownload" name="backupdownload" onchange ="checkImportAll(this,'button7');" ><%=dirBckSelect%></select></td>
									<td align="right"><img id="button7" title="<%=l.getString("mgr","backupdownload")%>" src="<%=images_export_off%>" onclick="exportBackup();"></img></td>
								</tr>						
							</table>
						</td>
						<td class='table'  width="20%"></td>
					</tr>
				</table>
				<br/>
				
				<form name="uploadfrm5" id="uploadfrm5" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
					<input type="hidden" name="tipofile" value="sitebackup" />
					<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
						<tr class='standardTxt'>
							<td class='table' width="*"><b><%=l.getString("mgr","uploadbackup")%></b></td>
							<td class='actionTD' width="35%" >
								<table cellpadding="1" cellspacing="1">
									<tr>
										<td align="right">
										<%=fileDlg.inputLoadFile("sitebacupload", "zip", "size='30%' onChange=enabImport('sitebacupload','button8','zip');", isProtected)%>
										</td>
										<td align="right"><img id='button8' title="<%=l.getString("mgr","uploadbackup")%>" src="<%=images_import_off%>" onclick="actionForm('uploadfrm5','sitebacupload');return false;" /></td>
										<!--  quello che c'era prima era: "actionForm('changeimgtop')"; id="changeimgtop" name="imgimporttop" -->
									</tr>
								</table>
							</td>
							<td class='table' width="20%" align='center'><%=properties.getProperty("sitebacupload","")%></td>
						</tr>
					</table>
				</form>
				<br/>
				
			</fieldset>
		<%}%>	
		
		<% if (!isProtected) {%>
			<br/>
			<br/>
			<fieldset class="field">
				<legend class="standardTxt" style="font-weight:bold"> <%=l.getString("mgr","confdwn")%> </legend>
				<br/>
				<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","sitemgrexp")%></b></td>
						<td class='actionTD' width="35%" >
							<img title="<%=l.getString("mgr","sitemgrexp")%>" style="cursor:pointer" src="<%=images_export_on%>" id='img_exp' onclick="actionConfirm2('expmsg','sitemgrexp','conf',sitemgrexp);" />					
						</td>
						<td class='table'  width="20%"></td>
					</tr>
				</table>
			
				<br/>
				<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","rulemgrexp")%></b></td>
						<td class='actionTD' width="35%" >
							<img title="<%=l.getString("mgr","rulemgrexp")%>" style="cursor:pointer" src="<%=images_export_on%>" id='img_ruleexp' onclick="actionConfirm2('expmsg','rulemgrexp','rule',rulemgrexp);" />
						</td>
						<td class='table' width="20%"></td>
					</tr>
				</table>
				<br/>
			</fieldset>		
			<br/>
			<br/>
		
			
			<fieldset class="field">
				<legend class="standardTxt"style="font-weight:bold"> <%=l.getString("mgr","confupld")%> </legend>
				<br/>		
				<form name="uploadfrm1" id="uploadfrm1" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
					<input type="hidden" name="tipofile" value="conf" />
					<table width="100%" cellpadding="1" cellspacing="1" height="45px">
						<tr class='standardTxt'>
							<td class='table' width="*"><b><%=l.getString("mgr","sitemgrimp")%></b></td>
							<td class='actionTD' width="35%" >
								<table cellpadding="1" cellspacing="1">
									<tr>
										<td align="right" valign="middle">
										<%=fileDlg.inputLoadFile("sitemgrimp", "conf", "size='30%' onChange=\"enabImport('sitemgrimp','button4','conf');\"", isProtected)%>
										</td>
										<td align="right"><img id="button4" title="<%=l.getString("mgr","sitemgrimp")%>" src="<%=images_import_off%>" onclick="actionConfirm('uploadfrm1','button4','sitemgrimp','impmsg','conf','errconf');return false;" /></td>
										<!--  quello che c'era prima era: "actionConfirm('button4','sitemgrimp','impmsg','conf','errconf')"; id="sitemgrimp" name="sitemgrimp" -->
									</tr>
								</table>
							</td>
							<td class='table' width="20%" align='center'><%=properties.getProperty("sitemgrimp","")%></td>
						</tr>
					</table>
				</form>
				<br/>
				
				<form name="uploadfrm2" id="uploadfrm2" action="servlet/ServUpload;jsessionid=<%=jsession%>" method="post" enctype='<%=fileDlg.enctype()%>'>
					<input type="hidden" name="tipofile" value="rule" />
					<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
						<tr class='standardTxt'>
							<td class='table' width="*"><b><%=l.getString("mgr","rulemgrimp")%></b></td>
							<td class='actionTD' width="35%" >
								<table cellpadding="1" cellspacing="1">
									<tr>
										<td align="right">
										<%=fileDlg.inputLoadFile("rulemgrimp", "rule", "size='30%' onChange=\"enabImport('rulemgrimp','button5','rule');\"", isProtected)%>
										</td>
										<td align="right"><img id="button5" title="<%=l.getString("mgr","rulemgrimp")%>" src="<%=images_import_off%>" onclick="stop_broad('actionConfirm','uploadfrm2','button5','rulemgrimp','impmsg','rule','errrul');return false;" /></td>
										<!--  quello che c'era prima era: "stop_broad('actionConfirm','button5','rulemgrimp','impmsg','rule','errrul')"; id="rulemgrimp" name="rulemgrimp" -->
									</tr>
								</table>
							</td>
							<td class='table' width="20%" align='center'><%=properties.getProperty("rulemgrimp","")%></td>
						</tr>
					</table>		
				</form>
				<br/> 
			</fieldset>
			<br/>
		<%}%>


	<br/>
	
	<div title="Alessandro: ghost div to hide this section" style="display:none;height:0px">
		<form name="frmmgr3" id="frmmgr3" action="servlet/master;jsessionid=<%=jsession%>" method="post"> 			
			<fieldset class="field">
				<legend class="standardTxt" style="font-weight:bold"><%=l.getString("mgr","support")%></legend>
				<br/>
				<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
					<tr class='standardTxt'>
						<td class='table' width="*"><b><%=l.getString("mgr","listDevices")%></b></td>
						<td class='actionTD' width="35%" >
							<img title="<%=l.getString("mgr","devices")%>" style="cursor:pointer" src="<%=images_export_on%>" id='img_backup' onclick="window.open('../app/mgr/ListDevice.jsp');return false;" />
						</td>
						<td class='table' width="20%" align='center'></td>
					</tr>
				</table>
				<br/>
			</fieldset>
		</form>
	</div>

<!--  /FORM principale -->

	<!--  Pagine di EXPORT originali: -->
	<% if (!isProtected) {/*%>
			<fieldset class="field">
				<legend class="standardTxt" style="font-weight:bold"> <%=l.getString("mgr","confdwn")%></legend>
				<br/>
				<form id='conffiledown' name='conffiledown' method='post' action="servlet/ServDownload;jsessionid=<%=jsession%>?tipofile=conf" target="Receiver">
						<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
							<tr class='standardTxt'>
								<td class='table' width="*"><b><%=l.getString("mgr","sitemgrexp")%></b></td>
								<td class='actionTD' width="35%" >
									<img title="<%=l.getString("mgr","sitemgrexp")%>" style="cursor:pointer" src="<%=images_export_on%>" id='img_exp' onclick="document.getElementById('conffiledown').submit();return false;window.location.reload();" />
								<!-- actionConfirm('img_exp','sitemgrexp','expmsg') -->
								</td>
								<td class='table' width="20%" align='center' style="word-wrap:break-word"><%=properties.getProperty("sitemgrexp","")%></td>
							</tr>
						</table>	
				</form>	
			
				<form id='rulefiledown' name='rulefiledown' method='post' action="servlet/ServDownload;jsessionid=<%=jsession%>?tipofile=rule" target="Receiver">		
						<table width="100%" cellpadding="1" cellspacing="1" height="45px" >
							<tr class='standardTxt'>
								<td class='table' width="*"><b><%=l.getString("mgr","rulemgrexp")%></b></td>
								<td class='actionTD' width="35%" >
									<img title="<%=l.getString("mgr","rulemgrexp")%>" style="cursor:pointer" src="<%=images_export_on%>" id='img_ruleexp' onclick="document.getElementById('rulefiledown').submit();return false;" />
								<!-- actionConfirm('img_ruleexp','rulemgrexp','expmsg') -->
								</td>
								<td class='table' width="20%" align='center' style="word-wrap:break-word"><%=properties.getProperty("rulemgrexp","")%></td>
							</tr>
						</table>
				</form>
			</fieldset>
		<br/>
	<%*/}%>	
<%} %>
	</td>
</tr>
</table>
