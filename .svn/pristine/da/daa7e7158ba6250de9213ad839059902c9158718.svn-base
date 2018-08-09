var JSP_ext = "jsp";
var XML_ext = "xml";
//var PVP_ext = "pvp";
var CONF_ext = "conf";
var RULE_ext = "rule";
var ZIP_ext = "zip";
var IMAGE_exts = "jpeg,.jpg,.bmp,.gif,.png";
var OLD_COLOR = null;
var old_passw = "";

function setAction(actionid,actionname)
{
	
	document.getElementById(actionid).value = actionname;

}


function actionConfirm(frm_name,id_obj,type,msg,ext,msgerr)
{
	var obj = document.getElementById(id_obj);
	var img = obj.src.split("/");
	var tmp = img[img.length-1].split("_")[1];
	var is_on = tmp.split(".")[0];
	if (is_on=="on")
	{
		if (null != ext)
		{
			if (!file_filter(type,ext))
			{
				alert(document.getElementById(msgerr).value);
				return false;
			}
		}
		if (msg == null)
		{
			msg = type;
		}
			
		if (confirm(document.getElementById(msg).value))
		{
			//document.getElementById("action").value=type;
			MTstartServerComm();
			document.getElementById(frm_name).submit();	
		}
		else
		{
			return false;
		}
	}
	return false;
}

function confirm_upload_devdtl(frm_name,id_obj,dev_folder,type,msg,ext,msgerr)
{
	var obj = document.getElementById(id_obj);
	var folder = document.getElementById(dev_folder);
	var img = obj.src.split("/");
	var tmp = img[img.length-1].split("_")[1];
	var is_on = tmp.split(".")[0];
	
	if (folder.value=="-1")
	{
		alert(document.getElementById("sel_device").value);
		return false;
	}
	if (is_on=="on")
	{
		if (null != ext)
		{
			if (!file_filter(type,ext))
			{
				alert(document.getElementById(msgerr).value);
				return false;
			}
		}
		if (msg == null)
		{
			msg = type;
		}
			
		if (confirm(document.getElementById(msg).value))
		{
			//document.getElementById("action").value=type;
			MTstartServerComm();
			document.getElementById(frm_name).submit();	
		}
		else
		{
			return false;
		}
	}
	return false;
}

function actionConfirm2(msg,id_obj,filter,action)
{
//	if (confirm(document.getElementById(msg).value))
//	{
		fdSaveFile(id_obj,filter,action);
		if(filter == "conf")
		{
			var date = new Date();
			fdSetFile("bkp_"+date.format("dd-MM-yyyy_hh-mm-ss")+"_"+document.getElementById("version").value);
		}
		else if(filter == "rule")
		{
			date = new Date();
			fdSetFile("bkp_"+date.format("dd-MM-yyyy_hh-mm-ss")+"_"+document.getElementById("version").value);
		}
//	}
}
function sitemgrexp(local,path,filename)
{
	CommSend("servlet/ajrefresh", "POST", "cmd=sitemgrexp&path="+path+"&filename="+filename+"&local="+local,"expsite_back("+local+")",true);
}
function rulemgrexp(local,path,filename)
{
	CommSend("servlet/ajrefresh", "POST", "cmd=rulemgrexp&path="+path+"&filename="+filename+"&local="+local,"expsite_back("+local+")",true);
}
function expsitebackup(local,path,filename)
{
	var selectfile = document.getElementById("backupdownload").value;
	CommSend("servlet/ajrefresh", "POST", "cmd=expsitebackup&path="+path+"&filename="+filename+"&local="+local+"&selectfile="+selectfile,"expsite_back("+local+")",true);
}
function reloadDoc()
{
	document.location.reload();
}

function submitMultipart(formID)
{

	if (formID=="conffileform")
	{
		if (!file_filter("input" + formID, CONF_ext))
		{
			alert(document.getElementById("isntconf").value);
			return false;
		}
	}
	
	if (formID=="rulefileform")
	{
		if (!file_filter("input" + formID, RULE_ext))
		{
			alert(document.getElementById("isntrule").value);
			return false;
		}
	}

	if (formID=="rulesfileform")
	{
		if (!file_filter("input" + formID, XML_ext))
		{
			alert(document.getElementById("isntxml").value);
			return false;
		}
	}

	if (formID=="jspfileform")
	{
		if (!file_filter("input" + formID, JSP_ext))
		{
			alert(document.getElementById("isntjsp").value);
			return false;
		}
	}

	if (formID=="devfileform")
	{
		if (!file_filter("input" + formID, XML_ext))
		{
			alert(document.getElementById("isntdev").value);
			return false;
		}
	}

	if (formID=="logofileform"||formID=="loginfileform")
	{
		if (!image_file_filter("input" + formID))
		{
			alert(document.getElementById("isntimage").value);
			return false;
		}
	}
	
	MTstartServerComm();
	//alert(document.getElementById(formID).childNodes.length);
	//alert("HTML: "+document.getElementById(formID).innerHTML);
	document.getElementById(formID).submit();
	//alert(document.getElementById(formID).nodeName);
	setTimeout("reloadDoc()",15000);
	return false;
}


function actionForm(frm_name,type)
{	
	if (type=="importxml")
	{
		if (!file_filter(type, JSP_ext))
		{
			alert(document.getElementById("isntjsp").value);
			return false;
		}
	}
	else if (type=="importdevice")
	{
		if (!file_filter(type,XML_ext))
		{
			alert(document.getElementById("isntxml").value);
			return false;
		}
	}
	else if (type=="importrules")
	{
		if (!file_filter(type,XML_ext))
		{
			alert(document.getElementById("isntxml").value);
			return false;
		}
	}
	else if (type=="changeimg"||type=="changeimgtop")
	{
		if (!image_file_filter(type))
		{
			alert(document.getElementById("isntimage").value);
			return false;
		}
	}
	else if(type=="sitebacupload")
	{
		if(!file_filter(type,ZIP_ext))
		{
			alert(document.getElementById("isntzip").value);
			return false;
		}
		var file = document.getElementById(type).value;
		if(fdLocal)
		{
			file = file.substring(file.lastIndexOf('/')+1,file.lastIndexOf('.') );
		}
		else
		{
			file = file.substring(file.lastIndexOf('\\')+1,file.lastIndexOf('.') );
		}
		var backupdownload_obj = document.getElementById("backupdownload");
		for(var i=0;i<backupdownload_obj.options.length;i++)
		{
			var exist = backupdownload_obj.options[i].value;
			if(exist == file)
			{
				if(!confirm(document.getElementById("existsitebackup").value))
				{
					return;
				}
			}
		}
	}
	else if(type == "modbusslavestarttype")
	{
		/* combo replaced with checkbox
		if(document.getElementById("modbusslavestarttype").selectedIndex == 0)
		{
			alert(document.getElementById("selectstarttype").value);
			return;
		}
		*/
	}
	else if(type == "safetylevel")
	{
		var txtconfirmhide = document.getElementById("txtconfirmhide").value;
		if(document.getElementById("rd_hide").checked && !confirm(txtconfirmhide))
		{
			return;
		}
	}	
	//document.getElementById("action").value=type;
	MTstartServerComm();
	document.getElementById(frm_name).submit();	
}//action

function image_file_filter(input_name)
{
	var input_file = document.getElementById(input_name).value;
	var exts = IMAGE_exts.split(",");
	for (i=0;i<exts.length;i++)
	{
		if((input_file != "") && (input_file.length > 4) && 
	  		((input_file.substring(input_file.length-4,input_file.length)).toLowerCase() == exts[i]))
		{
			return true;
		}
		else if (input_file == "")  //img default
		{
			return true;
		}
	}
	return false;
} 

function verifyDev(obj)
{
	var img = document.getElementById("button7");
	if (obj.value==-1)
	{
		img.src = 'images/actions/remove_off.png';
	}
	else
	{
		img.src = 'images/actions/remove_on_black.png';
	}
}

function verifyDevMdl(obj)
{
	var img = document.getElementById("btn_exp_devdml");
	if (obj.value==-1)
	{
		img.src = 'images/actions/export_off.png';
	}
	else
	{
		img.src = 'images/actions/export_on_black.png';
	}
}


function exportBackup(){
	var fileName = document.getElementById('backupdownload').value;
	if( fileName != '' ) {
		fdSetFile(fileName);
		fdSaveFile('expsiteback','zip',expsitebackup);
	}
}
function checkImportAll(obj,button){
	var img;
	if(obj.options[obj.selectedIndex].value!=""){
		img = document.getElementById(button);
		img.src="images/actions/import_on_black.png";
		img.style.cursor="pointer";
	}//if
	else{
		img = document.getElementById(button);
		img.src="images/actions/import_off.png";
		img.style.cursor="";
	}//else
}

function SysSaveRegistration()
{
	var site_name = document.getElementById("site_name").value;
	var site_password = document.getElementById("site_password").value;
	var site_confirm_password = document
			.getElementById("site_confirm_password").value;

	if (site_name == "") {
		alert(document.getElementById("compilename").value);
		return true;
	}
	if ((site_password != "") && (site_password.length < 6)) {
		alert(document.getElementById("shortpassw").value);
		return true;
	}

	if (old_passw != site_password) {
		if (site_password != site_confirm_password) {
			alert(document.getElementById("wrongpassw").value);
			return true;
		}
	}
	var ofrm = document.getElementById("frm_registration_siteinfo");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}
function restartEngine_confirm(){
	if (confirm(top.frames['header'].document.getElementById("rest").value))
	{
		restartEngine();
	}
}

function restartEngine(){
	hideBalloon(); // hide engine restart balloon
	var param= "&cmd=rest";
	MTstartServerComm();
	new AjaxRequest("servlet/ajrefresh", "POST",param, Callback_restartEngine, true);
	return true;
}
function Callback_restartEngine(xml){
	MTstopServerComm();
}
function SysAlertBackUp()
{
	var path = "";
	if(document.getElementById("pathsysfile"))
		path = document.getElementById("pathsysfile").value;
	
	if(path != "" && path != null && path != "null")
	{
		//nuovo msg x backup
		if (document.getElementById("expmsg2"))
		{
			var newmsg = document.getElementById("expmsg2").value;
			alert(newmsg);
		}
		else
		{
			//vecchio msg x backup
			alert(path);
		}
	}
}

function SysAlertLicense()
{
	new AjaxRequest("servlet/ajrefresh", "POST", "cmd=cpuperusage",
			Callback_CPUUsage, false);
	var msg = document.getElementById("msgtouserforlicenze").value;
	if(msg != null && msg != "")
		alert(msg);
	//2010-7-8, Kevin Ge, get old password when init
	old_passw = document.getElementById("site_password").value;
}
function Callback_CPUUsage(xml)
{
	var cpu = xml.getElementsByTagName("cpu")[0].childNodes[0].nodeValue;
	document.getElementById("cpuperusage").innerHTML = cpu;
}
function Error()
{
	var msg = document.getElementById("error").value;
	if(msg != null && msg != "")
		alert(msg);
}

function HideImportMsg()
{
	var visiblity = "hidden";
	var display = "none";
	
	if(document.getElementById("impmsgdiv_vis").value=="")
		document.getElementById("impmsgdiv").style.visibility = visiblity;
	
	if(document.getElementById("impmsgdiv_disp").value=="")
		document.getElementById("impmsgdiv").style.display = display;
}

function ShowMsgWindow()
{	
	if(!document.getElementById("window_msg").value=="")
		alert(document.getElementById("window_msg").value);
}

function doVirtualKey(frm_name,val)
{
	var azione = "doVirtualKey";
	document.getElementById("whereKey").value = val;
	
    document.getElementById("action").value = azione;
	MTstartServerComm();
	document.getElementById(frm_name).submit();	
}

function activeBuzzer(frm_name,val)
{
	
	var azione ="";
	if(val == true)
		azione = "buzzeron";
	if(val == false)
		azione = "buzzeroff";
	
    document.getElementById("action").value = azione;
	MTstartServerComm();
	document.getElementById(frm_name).submit();	
}

function activeRemoteUsersMngm(frm_name,val)
{
	
	var azione ="";
	if(val == true)
		azione = "remoteusersmngmon";
	if(val == false)
		azione = "remoteusersmngmoff";
	
    document.getElementById("action").value = azione;
	MTstartServerComm();
	document.getElementById(frm_name).submit();	
}

function initMultiMessage()
{
	guipRefresh();
}
function guipRefresh()
{
	new AjaxRequest("servlet/ajrefresh", "POST","cmd=init", guipRefreshBack, false);
}

/* Alessandro 20100629 : no used because safety level setting is no more an ajax function
function hideShowSafetyLevel()
{
	var txtconfirmhide = document.getElementById("txtconfirmhide").value;
	var currSLStatus = document.getElementById("rd_hide").checked;
	if(currSLStatus && confirm(txtconfirmhide))
	{
		new AjaxRequest("servlet/ajrefresh", "POST","cmd=hide", "", false);
	}
	else if (!currSLStatus)
	{
		new AjaxRequest("servlet/ajrefresh", "POST","cmd=show", "", false);
	}
}
*/

function guipRefreshBack(xml)
{	
	var tdguardian = document.getElementById("tdguardian");
	var guardian = xml.getElementsByTagName("guardian")[0];
	if(guardian != null)
	{
		var txtGuardian = document.getElementById("txtGuardian").value;
		guardian = guardian.childNodes[0].nodeValue;
		if(guardian == "1")
		{
			tdguardian.innerHTML = txtGuardian+"<br>"+"<img src='images/gpro/guardian_ns.gif' onclick='openWinGp();' border='0'/>";
		}
		else if(guardian == "2")
		{
			tdguardian.innerHTML = txtGuardian+"<br>"+"<img src='images/gpro/guardian.gif' onclick='openWinGp();' border='0'/>";
		}
		else
		{
			tdguardian.innerHTML = "";
		}
	}
}

function link(var1,var2,var3)
{
	var desc = document.getElementById("desc"+var2).value;
	top.frames['manager'].loadTrx(var1+desc+var3);
}
function msgmouseover(obj,over)
{
	if(over)
	{
		OLD_COLOR = obj.className;
		obj.className = "tdMessageonover";
	}
	else
	{
		if(OLD_COLOR != null)
		{
			obj.className = OLD_COLOR;
		}
	}
}

function expsite_savefile(local,path,filename)
{
	CommSend("servlet/ajrefresh", "POST", "cmd=expsiteconf&path="+path+"&filename="+filename+"&local="+local,"expsite_back("+local+")",true);
}

function expsiteForSVG_savefile(local,path,filename)
{
	CommSend("servlet/ajrefresh", "POST", "cmd=expsiteconfForSVG&path="+path+"&filename="+filename+"&local="+local,"expsite_back("+local+")",true);
}

function Callback_expsite_back(local)
{
	MTstopServerComm();
	if (xmlResponse!=null && xmlResponse.getElementsByTagName("file")[0] != null)
	{
		var filename = xmlResponse.getElementsByTagName("file")[0].childNodes[0].nodeValue;
		var msg = "";
		if(filename == "ERROR")
		{
			msg = document.getElementById("save_error").value;
			alert(msg);
		}
		else
		{
			if(local == true)
			{
					msg = document.getElementById("save_confirm").value;
					alert(msg + filename);
			}
			else
			{
				var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
				window.open(sUrl);
			}
		}
	}
}

function expMdlForSVG_savefile(local,path,filename)
{
	var expsvg_iddevmdl = document.getElementById("expsvg_iddevmdl").value;
	CommSend("servlet/ajrefresh", "POST", "cmd=expMdlConfForSVG&path="+path+"&filename="+filename+"&local="+local+"&expsvg_iddevmdl="+expsvg_iddevmdl,"expmdl_back("+local+")",true);
}

function Callback_expmdl_back(local)
{
	MTstopServerComm();
	if (xmlResponse!=null && xmlResponse.getElementsByTagName("file")[0] != null)
	{
		var filename = xmlResponse.getElementsByTagName("file")[0].childNodes[0].nodeValue;
		var msg = "";
		if(filename == "ERROR")
		{
			msg = document.getElementById("save_error").value;
			alert(msg);
		}
		else
		{
			if(local == true)
			{
					msg = document.getElementById("save_confirm").value;
					alert(msg + filename);
			}
			else
			{
				var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
				window.open(sUrl);
			}
		}
	}
}

function setFileDialogDefaultfile(obj_id)
{
	var obj = document.getElementById(obj_id);
	fdSetFile(obj.value);
}

function openWinGp()
{
//	if(window.showModalDialog) 
//	{
//		var sess = top.frames["manager"].getSessionId();
//		GB_WinGp = window.showModalDialog(top.frames["manager"].getDocumentBase() +"arch/include/GuardianWin.jsp;"+sess,"gpfinestrella",
//										  "dialogWidth:600px;dialogHeight:260px;help:no;scroll:no;status:no;");
//		GB_WinGp = null;
//	} 
	//By glisten
	var sess = top.frames["manager"].getSessionId();
	var openUrl = top.frames["manager"].getDocumentBase() +"arch/include/GuardianWin.jsp;"+sess;
	var winWidth = 600;
	var winHeight = 260;
	var clientH = (document.body.scrollHeight-winHeight)/2;	
	var clientW = (document.body.scrollWidth-winWidth)/2;
	var windowFeatures = "height="+winHeight+",width="+winWidth+",top="+clientH+",left="+clientW+",toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no"
	window.open(openUrl,"guardianPRO",windowFeatures);
	GB_WinGp = null;
	
}

function enableRemoteControl(state)
{
	if(state)
		alert(document.getElementById("enableRemoteControl").value);
	var form = document.getElementById("frmmgr3");
	if( form ) {
		document.getElementById("remote_control").value = state;
		MTstartServerComm();
		form.submit();
	}
}

function enableFirefoxPortable(state)
{
	if(state)
		alert(document.getElementById("enableFirefoxPortable").value);
	var form = document.getElementById("frmmgr5");
	if( form ) {
		document.getElementById("firefox_portable").value = state;
		MTstartServerComm();
		form.submit();
	}
}
