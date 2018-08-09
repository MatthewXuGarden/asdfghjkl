var REFRESH_INTERVAL	= 60 * 1000; // 1 minute


function onLoadDetail()
{
	setTimeout("refresh()", REFRESH_INTERVAL);
}


function refresh()
{
	new AjaxRequest("servlet/ajrefresh", "POST", "action=refresh", Callback_refresh, false);
	return true;
}


function Callback_refresh(xml)
{
	xmlResponse = xml.firstChild;
	var bNewAlg = xmlResponse.getAttribute("bNewAlg") == "true";

	// Information area
	var axmlInfo = xmlResponse.getElementsByTagName("info");
	for(var i = 0; i < axmlInfo.length; i++) {
		var xmlInfo = axmlInfo[i];
		var id = xmlInfo.getAttribute("id");
		var value = xmlInfo.getAttribute("value");
		var element = document.getElementById(id);
		if( element )
			element.innerHTML = value;
	}
	
	// Rack area
	if( bNewAlg ) {
		var xmlRackData = xmlResponse.getElementsByTagName("rack")[0].firstChild;
		var varRackData = xmlRackData.nodeValue;
		eval(varRackData);
		Lsw1.mData = aValue;
		Lsw1.numRows = Lsw1.mData.length;
		Lsw1.render();
	}
	
	// Utility area
	var xmlUtilData = xmlResponse.getElementsByTagName("util")[0].firstChild;
	var varUtilData = xmlUtilData.nodeValue;
	eval(varUtilData);
	Lsw0.mData = aValue;
	Lsw0.numRows = Lsw0.mData.length;
	Lsw0.render();
	
	if(bNewAlg && typeof(newAlgorithmObj) != "undefined")
		newAlgorithmObj.remain(3);
		
	// Counters area
	if( bNewAlg ) {
		var axmlCount = xmlResponse.getElementsByTagName("count");
		for(var i = 0; i < axmlCount.length; i++) {
			var xmlCount = axmlCount[i];
			var id = xmlCount.getAttribute("id");
			var value = xmlCount.getAttribute("value");
			var element = document.getElementById(id);
			if( element )
				element.innerHTML = value;
		}
	}	
	
	setTimeout("refresh()", REFRESH_INTERVAL);
}


function change_rack()
{
	var idrack = document.getElementById('comborack').value;
	document.getElementById('idrack').value=idrack;
	document.getElementById('cmd').value="change";
	var form = document.getElementById("frm_fsdtl");
	if(form != null)
	{
		MTstartServerComm();
		form.submit();
	}
}

function reset_all()
{
	var idrack = document.getElementById('comborack').value;
	document.getElementById('cmd').value="reset_all";
	var form = document.getElementById("frm_fsdtl");
	document.getElementById('idrack').value=idrack;
	if(form != null)
	{
		MTstartServerComm();
		form.submit();
	}
}

function reset_one(obj)
{
	var idrack = document.getElementById('comborack').value;
	document.getElementById('cmd').value="reset_one";
	document.getElementById('idutil').value=obj.id.split("_")[1];
	document.getElementById('idrack').value=idrack;
	var form = document.getElementById("frm_fsdtl");
	if(form != null)
	{
		MTstartServerComm();
		form.submit();
	}
}

function redirect(iddevice)
{
	top.frames['manager'].loadTrx('dtlview/FramesetTab.jsp&folder=dtlview&bo=BDtlView&type=click&iddev='+iddevice+'&desc=ncode01');
}

function hideNewAlgorithmColumn()
{
	if(typeof(newAlgorithmObj) != "undefined")
		newAlgorithmObj.hideCol(3)
}
