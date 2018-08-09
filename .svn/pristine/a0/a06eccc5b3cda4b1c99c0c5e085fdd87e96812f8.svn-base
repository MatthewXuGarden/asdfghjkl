var PMCommReq=null;

function PM_Monitor(){
	PM_req_status()
	
	setInterval("PM_req_status()",10000);
	
}

function PM_req_status(){
	try
	{
		if(window.XMLHttpRequest)
			PMCommReq = new XMLHttpRequest();
		else if (window.ActiveXObject)
			PMCommReq = new ActiveXObject("Microsoft.XMLHTTP");
		if(PMCommReq != null)
		{
			PMCommReq.open("GET","servlet/ajrefresh?cmd=PARAM_STATUS",true);
			PMCommReq.onreadystatechange = PM_CallBack_Status;
			PMCommReq.send(null);
		}
	}
	catch(e){}
	
}

function PM_CallBack_Status(){
	if(PMCommReq.readyState==4) 
  	{
		if(PMCommReq.status==200) 
    	{
    		try 
    		{
    			var xmlResponse = PMCommReq.responseXML;    			
    			var tm = (xmlResponse.getElementsByTagName("pm")[0]).childNodes[0].nodeValue;
    			var mess = (xmlResponse.getElementsByTagName("stopmessage")[0]).childNodes[0].nodeValue;
    			if(tm != null && tm != "")
					{
    					pmstatusdiv = document.getElementById("pvppmstatus");
    					
    					if (tm=="true"){
    						
    						pmstatusdiv.innerHTML="<img src=\"./images/actions/params_on.png\" " +
    								" title=\""+mess+"\" />";
    						
    					}
    					else
    					{
    						
    						pmstatusdiv.innerHTML='';
    						
    					}
					} 			
    		}
    		catch(e){}

	    }
    }
}