function initLoginPage(type)
{
	if(document.getElementById("winclose")) 
		document.getElementById("winclose").style.left=(document.body.clientWidth - 44) +'px';	
	
	detectBrowserType();
	detectScreenResolution();
	if(type == "normal")
	{
		startLoginRefresh();
		var userO = document.getElementById("txtUser");
		setFocus(userO);
	}
	else if(type == "firsttime" || type== "pwdexpired")
	{
		var passwordO = document.getElementById("npassword");
		setFocus(passwordO);
	}
	else if(type == "changepwd")
	{
		startLoginRefresh();
		passwordO = document.getElementById("txtPassword");
		setFocus(passwordO);
	}
	if( type != "license" )
		manageLoginButton(1);
	
	// center login dialog
	computeViewport();
	var divLogin = document.getElementById("divLogin");
	var tabLogin = document.getElementById("tabLogin");
	divLogin.style.position = "fixed";
	divLogin.style.left = (viewportWidth - tabLogin.clientWidth) / 2 + "px";
	divLogin.style.top = (viewportHeight - tabLogin.clientHeight) / 2 + "px";
}

function startLoginRefresh()
{
	CommSend("servlet/ajrefresh","GET","cmd=lgnrfh","lg");
	setTimeout("startLoginRefresh()",10000);
}

function Callback_lg()
{
	// Alarm icon
	var ris = "N";
	if(xmlResponse != null)
		ris = (xmlResponse.getElementsByTagName("a")[0]).childNodes[0].nodeValue;
	if(ris == "Y")
		activeAllarm();	
	else
		deactiveAllarm();		
	
	// Guardian icon	
	ris = "N";
	if(xmlResponse != null)
		ris = (xmlResponse.getElementsByTagName("g")[0]).childNodes[0].nodeValue;
	if(ris == "0")
		deactiveGuardian();	
	else
		activeGuardian(ris);
}

function activeGuardian(ris)
{
	if(ris == "1")
	{
		document.getElementById("imgguardivlogin").src="images/gpro/guardian_ns.gif";
	}
	if(ris == "2")
	{
		document.getElementById("imgguardivlogin").src="images/gpro/guardian.gif";
	}
	document.getElementById("guardivlogin").style.visibility="visible";
}
  
function deactiveGuardian()
{
	var oDiv = document.getElementById("guardivlogin");
	if(oDiv != null)
  		oDiv.style.visibility="hidden";
}

function activeAllarm()
{
	var oDiv = document.getElementById("alarmdivlogin");
	if(oDiv != null)
		oDiv.style.visibility="visible";
}
  
function deactiveAllarm()
{
	var oDiv = document.getElementById("alarmdivlogin");
	if(oDiv != null)
  		oDiv.style.visibility="hidden";
}
  
function detectBrowserType()
{
	var hBType = document.getElementById("browser");
  	if(document.all)
  		hBType.value = "IE";
  	else
  		hBType.value = "FF";
}

function detectScreenResolution()
{
	try {
		document.getElementById("screenw").value = screen.width;
	}
	catch(e){ 
		document.getElementById("screenw").value = 768; 
	}
	
	try { 
		document.getElementById("screenh").value = screen.height; 
	}
	catch(e){
		document.getElementById("screenh").value = 1024;
	}
}

function setFocus(obj)
{
	if(obj != null)
		obj.focus();
}

function manageLoginButton(type)
{
	switch(type)
	{
		case 1:
			document.getElementById("message").style.display = "block";
			document.getElementById("lgnbttprg").disabled = false;
			document.getElementById("lgnmsgprg").style.display = "none";
			break;
		case 2:
			document.getElementById("message").style.display = "none";
			document.getElementById("lgnbttprg").disabled = true;
			document.getElementById("lgnmsgprg").style.display = "block";
			break;
	}
}


function checkFields(type)
{
	var cmd = document.getElementById("cmd").value;

	if(cmd == "firsttime" || cmd == "changepwd" || cmd == "pwdexpired")
	{
		var password= document.getElementById("npassword").value;
		var cpassword= document.getElementById("cpassword").value;
		
		if((password=="")||(password==null)){
			alert(document.getElementById("txtEnterPassword").value);
			return false;
		}
		if(type == "standard")
		{
			if(password.length<6){
				alert(document.getElementById("txtStandardPassword").value);
				document.getElementById("npassword").value="";
				document.getElementById("cpassword").value="";
				return false;
			}
		}
		else if(type == "strict")
		{
			if(password.length<8 || !strictCheckAtleast1char1number1speicial(password))
			{
				alert(document.getElementById("txtStrictPassword").value);
				document.getElementById("npassword").value="";
				document.getElementById("cpassword").value="";
				return false;
			}
		}
		if(password!=cpassword){
			alert(document.getElementById("txtConfPwdIncorrect").value);
			document.getElementById("npassword").value="";
			document.getElementById("cpassword").value="";
			return false;
		}
	}
	manageLoginButton(2);
	return true;
}

function cancelChangepwd()
{
	var base = document.getElementsByTagName("base");
	var jsession = document.getElementById("jsession").value;
	if(base != null && base != undefined)
	{
		window.location.href = base[0].href+"servlet/login;jsessionid="+jsession;
	}
	else
	{
		window.location.href = "login";
	}
}

function alarmViewAccess()
{
	var username = document.getElementById("txtUser");
	var newusername = document.createElement("input");
	newusername.setAttribute("type", "hidden");
	newusername.setAttribute("name", "txtUser");
	newusername.setAttribute("id", "txtUser");
	newusername.setAttribute("value", "dashboard");

	username.parentNode.replaceChild(newusername, username);
	
	document.getElementById("txtPassword").style.visibility = "hidden";
	document.getElementById("txtPassword").value = "dashboard";
	document.getElementById("loginfrm").submit();

}

function WinCloseBrowser()
{
	if(confirm(document.getElementById("wincloseconfirm").value))
	{
		top.window.open('', '_parent', ''); //  to prevent browser confirmation
		top.window.close();
	}
}

var lanPath = false;
var nextFoucusSubmit = false;

function languageKeydownNormal(obj,event){
	if(event.keyCode==9){
		nextFoucusSubmit = true;
		lanPath = true;
	}		
}

function helponfocus()
{
	if(lanPath==true){
		if(nextFoucusSubmit == true )
		{	
			var pwdisplay =document.getElementById("pwdisplayMark").style.display;
			if(pwdisplay=="none" ||pwdisplay==null){
				document.getElementById("loginArea2").focus();
			}else{
				var loginAreaDisplay = document.getElementById("loginArea").parentNode.parentNode.style.display;
				if(loginAreaDisplay=="none"||loginAreaDisplay==null){
					document.getElementById("loginArea2").focus();
				}else{
					document.getElementById("loginArea").focus();
				}
			}
			nextFoucusSubmit = false;
		}
	}
}

function lanOnfocus(obj,event){
	if(lanPath == true){
		lanPath =false;
		var cancelDisplay = document.getElementById("cancel").parentNode.parentNode.style.display;
		if(cancelDisplay == "none"){
			document.getElementById("help").focus();
		}else{
			document.getElementById("cancel").focus();
		}
	}
}



