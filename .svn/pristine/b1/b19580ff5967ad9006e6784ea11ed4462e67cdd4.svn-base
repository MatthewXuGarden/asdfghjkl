var rs_curactivelocal = null;
var rs_idthread = 0;

function browsOffline(obj,site,sdata)
{
	if(sdata != null && sdata != "null")
	{
		var sUrl = top.frames['manager'].getDocumentBase();
		var lang = document.getElementById("curlang").value;
		window.open(sUrl+"/arch/login/LoginAuto.jsp;"+getSessId()+"?idsite="+site+"&language="+lang+"&remote=yes",'','');	
	}
	return false;
}

function browsOnline(obj,site,phone,conn,type)
{
	var	curc = document.getElementById("curconn").value;
	var docon = false;
	if(curc != 0)
	{
		if(curc == site)
			RE_commandClose(site);
		else
		{
			if(confirm(document.getElementById("closeconn_a").value +curc + document.getElementById("closeconn_b").value))
				docon = true;
		}
	}
	else
		docon = true;
	
	if(docon)
		RE_commandOpen(site,phone,conn,type);
}

function RE_commandOpen(isite,iphone,iconn,itype)
{
	document.getElementById("cursite").value = isite;
	document.getElementById("curphone").value = iphone;
	document.getElementById("curtype").value = iconn;
	document.getElementById("curinstal").value = itype;
	document.getElementById("curaction").value = "openlocal";
	RE_commandSend();
}

function RE_commandClose(isite)
{
	document.getElementById("cursite").value = isite;
	document.getElementById("curaction").value = "closelocal";	
	RE_commandSend();
}

function RE_commandSend()
{
	RE_closeCurrentWindow();
	var ofrm = document.getElementById("frmlocallist");
	if(ofrm != null) {
		MTstartServerComm();
		ofrm.submit();
	}
}

function RE_closeCurrentWindow()
{
	if(rs_curactivelocal != null)
		rs_curactivelocal.close();
}

function openForBrowsOnline()
{
	var	iplocal = document.getElementById("curiplocal").value;
	var cfp = document.getElementById("comefrompost").value;
	var ist = document.getElementById("curinstal").value;
	var surl = "";
	
	if((iplocal != "undefined") && (iplocal != "") && (cfp == "T"))
	{
		RE_closeCurrentWindow();
		if(ist != null && ist == "PVP")
			surl = iplocal+"/PlantVisorPRO/servlet/login;"+getSessId();			
		else
			surl = iplocal;
		
		rs_curactivelocal = window.open(surl,'','');	
	}
	top.frames["allarm"].location.reload();
	
	if(cfp != "T")
		document.getElementById("comefrompost").value = "T";
	
	setTimeout("RE_checkInteraction()",2500);
}

function RE_updateRefWindow(objwin)
{
	RE_closeCurrentWindow();
	rs_curactivelocal = objwin;
}

function RE_checkInteraction()
{
	clearTimeout(rs_idthread);
	CommSend("servlet/ajrefresh","GET","cmd=rinter","RE");
	rs_idthread = setTimeout("RE_checkInteraction()",5000);
}

function Callback_RE()
{
	var Ncmd = xmlResponse.getElementsByTagName("response");
	var cmd = Ncmd[0].getAttribute("cmd");
	RE_clearAllRows()
	
	if(cmd == "load")
	{
		setTimeout("RE_reloadPage()",5000);
	}
	else if(cmd == "mark")
	{
		var Ncl = Ncmd[0].getElementsByTagName("cl");
		var item = null;
		var id = 0;
		var tabId = "";
		for(var i=0; i<Ncl.length; i++)
		{
			item = Ncl[i].childNodes[0];
			if(item != null)
			{
				id = item.nodeValue;
				tabId = "RTb_"+id;
				var oTb = document.getElementById(tabId);
				if(oTb != null)
					oTb.style.backgroundColor = "#FFE750";
			}
		}
	}
}

function RE_reloadPage()
{
	try 
	{
		var oBodyTrx = top.frames['body'].frames['bodytab'];
		if(oBodyTrx != null && !MTisServerComm())
		{
			var oFrmForRl = oBodyTrx.document.getElementById("frmrreload");
			if(oFrmForRl != null)
				oFrmForRl.submit();
		}
	}
	catch(e){}	
}

function RE_clearAllRows()
{
	var sId = "";
	var oTables = document.body.getElementsByTagName("TABLE"); 
	for(var i=0;i<oTables.length;i++) 
	{ 
    	if(oTables[i] != null)
    	{
    		sId = oTables[i].id;
    		if(sId.indexOf("RTb_") != -1)
    			oTables[i].style.backgroundColor = "#EAEAEA";
    	}
    }
}