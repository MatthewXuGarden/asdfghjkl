var curObj = null;
var objdivnot = null;
var objGuardian = null;
var idBlink = 0;
var guardianBlink = 0;
var guardianRight = false;

function topMouseOver(obj)
{
	if(obj.tagName == "DIV")
	{
		topMouseOverPrv(obj);
		return;
	}	
	
	var cNode = obj.childNodes;
		
	for(var i=0; i<cNode.length; i++)
	{
		if(cNode[i].tagName == "DIV")
		{
			cNode[i].style.backgroundImage="url(images/top/areaon.png)";
			cNode[i].style.fontSize="8pt";
			cNode[i].style.fontWeight="bold";
		}
	}
}

function topMouseOverPrv(obj)
{
	if((curObj != null) && (curObj.id == obj.id))
		return;
	
	obj.style.cursor="pointer";
	obj.style.backgroundImage="url(images/top/area_onmouseover.png)";
	obj.style.fontSize="8pt";
	obj.style.fontWeight="bold";
	return;
}

function topMouseOut(obj)
{
	if(obj.tagName == "DIV")
	{
		topMouseOutPrv(obj);
		return;
	}
	
	var cNode = obj.childNodes;
		
	for(var i=0; i<cNode.length; i++)
	{
		if(cNode[i].tagName == "DIV")
		{
			cNode[i].style.backgroundImage="url(images/top/areaoff.png)";
			cNode[i].style.fontWeight="normal";
			cNode[i].style.fontSize="8pt";
		}
	}
}

function topMouseOutPrv(obj)
{
	if((curObj != null) && (curObj.id == obj.id))
		return;
	
	obj.style.cursor="default";
	obj.style.backgroundImage="url(images/top/areaoff.png)";
	obj.style.fontSize="8pt";
	obj.style.fontWeight="normal";
	obj.style.color="";
	return
}

function loadMenu()
{
	var menuf = top.frames["manager"].InitializeMenu;
	if(menuf == null || menuf == undefined)
	{
		setTimeout("loadMenu()",500);
	}
	else
	{
		top.frames["manager"].InitializeMenu();
		setTimeout("topOpenAutomaticTrx()",1000);
	}
}

function topOpenAutomaticTrx()
{
	var db_home = document.getElementById("default_home").value;
	var default_home = 2; //default is global
	var dvm_global = document.getElementById("dvm_global").value;
	if(dvm_global != -1) //default is global
	{
		default_home = dvm_global;
	}
	
	if (db_home==1)//device
	{
		var dvm_devices = document.getElementById("dvm_devices").value;
		if(dvm_devices != -1)
			default_home = dvm_devices;
	}
	else if (db_home==2)//map
	{
		var dvm_map = document.getElementById("dvm_map").value;
		if(dvm_map != -1)
			default_home = dvm_map;
	}
	
	var wizarddone = document.getElementById("wizarddone").value;
	if(wizarddone == 0)
	{
		var dvm_wizard = document.getElementById("dvm_wizard").value;
		if(dvm_wizard != -1)
			default_home = dvm_wizard;
	}
	var redirect=top.frames["manager"].document.getElementById("redirectNum").value;
	if(redirect!=""){
		default_home=redirect;
	}
	var obj = top.frames["menuPVPRO2"].document.getElementById("dvm"+default_home);
	if(obj != null && obj != undefined){
		var str=obj.onclick.toString();
		if((str!=null) && (str.indexOf("}")>str.indexOf("{"))){
			var temp=str.substring(str.indexOf("{")+1,str.indexOf("}"));
			temp=temp.replace("comefrom=Menu","comefrom=Login");
			eval(temp);
		}else{
			obj.onclick();
		}
	}
}

function topMove()
{
	if(document.getElementById("logo")) 
		document.getElementById("logo").style.left=(document.body.clientWidth - 261) +'px';
	if(document.getElementById("buy")) 
		document.getElementById("buy").style.left=(document.body.clientWidth - 235) +'px';
	if(document.getElementById("guip")) 
		document.getElementById("guip").style.left=(document.body.clientWidth - 195) +'px';	
	if(document.getElementById("changepsw")) 
		document.getElementById("changepsw").style.left=(document.body.clientWidth - 140) +'px';
	if(document.getElementById("help")) 
		document.getElementById("help").style.left=(document.body.clientWidth - 108) +'px';
	if(document.getElementById("exit")) 
		document.getElementById("exit").style.left=(document.body.clientWidth - 75) +'px';
	if(document.getElementById("winclose")) 
		document.getElementById("winclose").style.left=(document.body.clientWidth - 44) +'px';	
}

function TopCloseBrowser(from)
{
	if(top.frames["body"] != null)
	{
		if(top.frames["body"].frames["bodytab"] != null)
		try {
			top.frames["body"].frames["bodytab"].RE_updateRefWindow(null);
		}
		catch(e){}
	}
	top.frames['manager'].logoutUser(from);	
}


function WinCloseBrowser()
{
	if(confirm(document.getElementById("wincloseconfirm").value))
	{
		top.window.open('', '_parent', ''); //  to prevent browser confirmation
		top.window.close();
	}
}


function writeNotify(message)
{
	objdivnot = document.getElementById('notify');
	objdivnot.innerHTML=message;
	
	if(message == '' || message == null)
		myBlink(3);
	else
		myBlink(1);
}

function startEngine(act){
	if((typeof(top.frames['body'].frames["bodytab"].restart_engine)=="function") && (top.frames['body'].frames["bodytab"].document.getElementById("isrestart")!=null) && (top.frames['body'].frames["bodytab"].document.getElementById("isrestart").value=="true")){
		top.frames['body'].frames["bodytab"].restart_engine();
	}else{
		top.frames['manager'].loadTrx(act);
	}
}
function myBlink(status)
{
	clearTimeout(idBlink);
	switch(status)
	{
		case 1:
			if(objdivnot != null)
				objdivnot.style.backgroundColor="#FFFFFF";
			idBlink = setTimeout("myBlink("+2+")",500);
			break;
		case 2:
			if(objdivnot != null)
				objdivnot.style.backgroundColor="#000000";
			idBlink = setTimeout("myBlink("+1+")",500);
			break;
		case 3:
			if(objdivnot != null)
				objdivnot.style.backgroundColor="#000000";
			break;
	}
}

//function TopCheckGuiMsg(state,right)
//{
//	if(right != null && right != undefined)
//	{
//		if(right == "TRUE")
//			guardianRight = true;
//		else
//			guardianRight = false;
//	}
//	
//	state = Number(state);
//	var objDivMsg = document.getElementById("guardian");
//		
//	switch(state)
//	{
//		case 1:
//			writeGuardian(document.getElementById('guardian_msg').value);
//			if(guardianRight)
//			{
//				//Kevin: page six and five are merged. so change from six to five
//				objDivMsg.onclick = TopGuiGoFive;
//				objDivMsg.style.cursor = "pointer";
//			}
//			break;
//		case 2:
//			writeGuardian(document.getElementById('guardian_msg').value);
//			if(guardianRight)
//			{
//				objDivMsg.onclick = TopGuiGoFive;
//				objDivMsg.style.cursor = "pointer";
//			}
//			break;
//		case 3:
//			writeGuardian("");
//			objDivMsg.style.cursor = "arrow";
//			break;
//		case 4:
//			var days = document.getElementById("guardian_snooze").value;
//			if(days != "")
//				writeGuardian("guardianPRO countdown " + days);
//			break;
//	}
//}

function writeGuardian(message)
{
	objGuardian = document.getElementById('guardian');
	objGuardian.innerHTML=message;
	
	if(message == '' || message == null)
		blinkGuardian(3);
	else
		blinkGuardian(1);
}

function blinkGuardian(status)
{
	clearTimeout(guardianBlink);
	switch(status)
	{
		case 1:
			if(objGuardian != null)
				objGuardian.style.backgroundColor="#FFFFFF";
			guardianBlink = setTimeout("blinkGuardian("+2+")",500);
			break;
		case 2:
			if(objGuardian != null)
				objGuardian.style.backgroundColor="#000000";
			guardianBlink = setTimeout("blinkGuardian("+1+")",500);
			break;
		case 3:
			if(objGuardian != null)
				objGuardian.style.backgroundColor="#000000";
			break;
	}
}

function TopGuiGoFive()
{
	var desc = document.getElementById("guardian_nav").value;
	top.frames["manager"].loadTrx("nop&folder=mgr&bo=BSystem&type=menu&desc="+desc+"&resource=SubTab5.jsp&curTab=tab5name");
}

//function TopGuiGoSix()
//{
//	var desc = document.getElementById("guardian_nav").value;
//	top.frames["manager"].loadTrx("nop&folder=mgr&bo=BSystem&type=menu&desc="+desc+"&resource=SubTab6.jsp&curTab=tab6name");
//}


function manageGuardianIcon(idx)
{
	switch(idx)
	{
		case 0:
			document.getElementById("guip").style.visibility = "hidden";
			break;
		case 1:
			document.getElementById("guip").style.visibility = "visible";
			document.getElementById("imgguip").src="images/gpro/gpro_act_nosnd.gif";
			break;
		case 2:
			document.getElementById("guip").style.visibility = "visible";
			document.getElementById("imgguip").src="images/gpro/gpro_act_snd.gif";
			break;
		default:
			document.getElementById("guip").style.visibility = "hidden";
			break;	
	}	
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
	var windowFeatures = "height="+winHeight+",width="+winWidth+",top="+clientH+",left="+clientW+",toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no";
	window.open(openUrl,"guardianPRO",windowFeatures);
	GB_WinGp = null;
	
}

function changePsw()
{
	if(confirm (document.getElementById("changepswconfirm").value))
	{
		top.frames["manager"].Redirect("servlet/login", "?changepwd=true");
	}
}