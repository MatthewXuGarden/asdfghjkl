var xmlCommReq = null;
var callBackId = -1;
var xmlResponse = null;
var booleanModalRequest = false;

function CommSend(sUrl,sMethod,sParam,iCallBack,bModal)
{
	booleanModalRequest = (bModal === undefined) ? false : bModal
	var ses = "";
	if(top.frames["manager"] != null)
		ses = top.frames["manager"].getSessionId();
		
	var url = sUrl+";"+ses;
	if(sMethod == "GET" && sParam != "")
	{
		url = url+"?"+sParam;
		sParam=null;
	}
	callBackId = iCallBack;
	
	if(window.XMLHttpRequest)
		xmlCommReq = new XMLHttpRequest();
	else if (window.ActiveXObject)
		xmlCommReq = new ActiveXObject("Microsoft.XMLHTTP");
	
	if(xmlCommReq != null)
	{
		try 
		{
			if(booleanModalRequest){
				try {  MTstartServerComm(); }
				catch(e){}
			}
			xmlCommReq.open(sMethod,url,true);
			xmlCommReq.onreadystatechange = CommCallback;
			
			if(sMethod == "POST" && sParam != "")
			{
				xmlCommReq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			}
			xmlCommReq.send(sParam);
		}
		catch(e){alert('JS Error: '+e.description);}
	}
}

function CommCallback()
{
	if(xmlCommReq.readyState==4) 
  	{
  		if(booleanModalRequest){
			try { 
				MTstopServerComm(); 
				unlockModUser();
			}
			catch(e){}
		}
		if(xmlCommReq.status==200) 
    	{
    		try {
    			xmlResponse = xmlCommReq.responseXML;
    			//alert(xmlCommReq.responseText);
    			//eval("Callback_"+callBackId+"("+xmlResponse+")");
    			// DYN: callback argument it remains undefined in the previous sentence
    			eval("Callback_"+callBackId+"(xmlResponse)");
    			callBackId = -1;
    			xmlResponse = null;
    		}
    		catch(e){}
    	}
    }
}

function CommSend2(sUrl,sMethod,sParam,iCallBack,bModal)
{
	booleanModalRequest = (bModal === undefined) ? false : bModal
	var ses = "";
	if(top.frames["manager"] != null)
		ses = top.frames["manager"].getSessionId();
		
	var url = sUrl+";"+ses;
	if(sMethod == "GET" && sParam != "")
		url = url+"?"+sParam;
	
	callBackId = iCallBack;
	
	if(window.XMLHttpRequest)
		xmlCommReq = new XMLHttpRequest();
	else if (window.ActiveXObject)
		xmlCommReq = new ActiveXObject("Microsoft.XMLHTTP");
	
	if(xmlCommReq != null)
	{
		try 
		{
			if(booleanModalRequest){
				try {  MTstartServerComm(); }
				catch(e){}
			}
			xmlCommReq.open(sMethod,url,true);
			xmlCommReq.onreadystatechange = CommCallback;
			if(sMethod == "POST" && sParam != null){
				xmlCommReq.send(sParam);
			}
		}
		catch(e){alert("JS Error: "+e.description);}
	}
}

/*new objectized version*/
function AjaxRequest(sUrl,sMethod,sParam,iCallBack,bModal) {

	var ses = "";
	if(top.frames["manager"] != null){
		ses = top.frames["manager"].getSessionId();
	}
	var url = sUrl+";"+ses;
	if(sMethod == "GET" && sParam != ""){
		url = url+"?"+sParam;
		sParam=null;
	}
	var method = sMethod;
	var param = sParam;
	var callback = iCallBack;
	var modal = (bModal === undefined) ? false : bModal;
	var __xmlCommReq;
	var __xmlResponse;
	
	if(window.XMLHttpRequest)
		__xmlCommReq = new XMLHttpRequest();
	else if (window.ActiveXObject)
		__xmlCommReq = new ActiveXObject("Microsoft.XMLHTTP");
	if(__xmlCommReq != null){
		try {
			if(modal){
				try {
					MTstartServerComm();
				} catch(e) {}
			}
			__xmlCommReq.open(method,url,true);
			__xmlCommReq.onreadystatechange = function() {
				try{
					if(__xmlCommReq.readyState==4){
						if(modal){
							try { 
								MTstopServerComm(); 
								unlockModUser();
							}
							catch(e){}
						}
						if(__xmlCommReq.status==200){
							try {
								__xmlResponse = __xmlCommReq.responseXML;
								var t = __xmlCommReq.responseText;
								//var methods=__xmlResponse.getElementsByTagName("method")[0].childNodes[0].nodeValue;
								// alert(methods+"---"+xmlCommReq.responseText);
//								eval("function() {Callback_"+methods+"(t);}");
//								eval("Callback_"+methods+"('"+t+"')");
								callback(__xmlResponse);
								__xmlResponse = null;
							}
							catch(e){}
						}
					}
					}catch(err){alert(err.description);}
				};
			if(method == "POST" && param != ""){
				__xmlCommReq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			}
			__xmlCommReq.send(param);
		}
		catch(e){
			alert('JS Error: '+e.description);
			MTstopServerComm();
		}
	}
}
