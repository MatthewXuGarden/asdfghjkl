
function evnviewDoc(idevn,stype)
{
	var sess = top.frames["manager"].getSessionId();
	var sUrl = top.frames["manager"].getDocumentBase() + "servlet/dispdoc;"+sess+"?id="+idevn+"&type="+stype;
	var features = "";
	var winWidth = 600;
	var winHeight = 300;
	var clientH = (document.body.scrollHeight-winHeight)/2;	
	var clientW = (document.body.scrollWidth-winWidth)/2;
	if(stype == "F")
	{
//		features="dialogWidth=600px;dialogHeight=300px;dialogTop:300;dialogLeft:400;scroll=yes;border=thick;help=off;status=off;resizable=on";
//		window.showModalDialog(sUrl,"",features);		
		features = "height="+winHeight+",width="+winWidth+",top="+clientH+",left="+clientW+",toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no";
		window.open(sUrl,"",features);
	}
	else if(stype == "S")
	{	
//		features="dialogWidth=600px;dialogHeight=300px;dialogTop:300;dialogLeft:400;scroll=yes;border=thick;help=off;status=off;resizable=off";
//		window.showModalDialog(sUrl,"",features);
		features = "height="+winHeight+",width="+winWidth+",top="+clientH+",left="+clientW+",toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no";
		window.open(sUrl,"",features);
	}
	else
	{
		window.open(sUrl);
	}	
}

/**
* Per ridimensionare la tabella all'interno della pagina
*/
function resizeTableTab1()
{
	var hdev = MTcalcObjectHeight("trEnvList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-60);
	}
}


/**
* Per ridimensionare la tabella broadcast parametri all'interno eventi della pagina
*/
function resizeTableTabEvent()
{
	var hdev = MTcalcObjectHeight("trEventList");
	if(hdev != 0){
		MTresizeHtmlTable(0,hdev-23);
	}
}

function exportResult(format)
{
	MTstartServerComm();
	CommSend("servlet/ajrefresh", "POST", "format="+format,"exportEVSEARCH",true);
}

function Callback_exportEVSEARCH()
{
	if (xmlResponse!=null && xmlResponse.getElementsByTagName("file")[0] != null)
	{
		var filename = xmlResponse.getElementsByTagName("file")[0].childNodes[0].nodeValue;
	
		var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
		window.open(sUrl);
		MTstopServerComm();
	}
}
