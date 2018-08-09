var TiIdTimer = null;
var TiIsRunning = false;
var TiObjToRender = null;

var TistartTime = 0;
var TistartDate = null;

var TxmlCommReq = null;
var jsession = "";

function TiStopClock()
{
}

function TiStartClock(sObjToRender,iTime) 
{
	TiObjToRender = document.getElementById(sObjToRender);
	jsession = document.getElementById("jsession").value;
	TiRender();
    setInterval("TiRender()",60000);
}

function TiRender()
{
	try
	{
		if(window.XMLHttpRequest)
			TxmlCommReq = new XMLHttpRequest();
		else if (window.ActiveXObject)
			TxmlCommReq = new ActiveXObject("Microsoft.XMLHTTP");
		
		if(TxmlCommReq != null)
		{
			TxmlCommReq.open("GET","servlet/ajrefresh;jsessionid=" + jsession + "?cmd=RTIME",true);
			TxmlCommReq.onreadystatechange = TiRenderBack;
			TxmlCommReq.send(null);
		}
	}
	catch(e){}
}

function TiRenderBack()
{
	if(TxmlCommReq.readyState==4) 
  	{
		if(TxmlCommReq.status==200) 
    	{
    		try 
    		{
    			var xmlResponse = TxmlCommReq.responseXML;
    			var tm = (xmlResponse.getElementsByTagName("t")[0]).childNodes[0].nodeValue;
    			if( tm != null && tm != "" )
					TiObjToRender.innerHTML = tm;
    			else // session expires; force logout
    				top.frames['manager'].Redirect('servlet/logout');
    		}
    		catch(e) {
				top.frames['manager'].Redirect('servlet/logout');
    		}
    		
    		try
    		{
    			var ris = "N";
				ris = (xmlResponse.getElementsByTagName("g")[0]).childNodes[0].nodeValue;
				manageGuardianIcon(Number(ris));
				/*
				if(ris == "0")
					deactiveGuardianTop();	
				else
					activeGuardianTop();
				*/
	    	}
	    	catch(e){}
	    }
    }
}

function activeGuardianTop()
{
	var oDiv = document.getElementById("guip");
	if(oDiv != null)
		oDiv.style.visibility="visible";
}
  
function deactiveGuardianTop()
{
	var oDiv = document.getElementById("guip");
	if(oDiv != null)
  		oDiv.style.visibility="hidden";
}
