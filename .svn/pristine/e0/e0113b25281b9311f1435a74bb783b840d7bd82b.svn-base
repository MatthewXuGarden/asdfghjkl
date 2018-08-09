var listVar = null;

function remove_dpds(){
	
	
	var allDevs = document.getElementById("depdevs2");
	var param = getListValue(allDevs);
	if(param ==""){
		alert(document.getElementById("nodev").value);
		return;
	}else{
		if(confirm(document.getElementById("dltdevcfm").value)){
			document.getElementById("devicesToRemoved").value=param;
			MTstartServerComm();
			document.getElementById("frm_set_line").submit();
		}else{
			return;
		}
	}
}

function freshDependencisList(){
	var allDevs = document.getElementById("depdevs2");
	var param = getListValue(allDevs);
	CommSend("servlet/ajrefresh","POST","cmd=dependencies_fresh&devids="+param,"freshDependencisList",true);
}

function Callback_freshDependencisList(xml){
	listVar =Lsw2;
	listVar.mData = [];
	var trs = xml.getElementsByTagName("tr");
	for( i =0;i<trs.length;i++){
		var tr = trs[i];
		var td1v = tr.childNodes[0].childNodes[0].nodeValue;
		var td2v = tr.childNodes[1].childNodes[0].nodeValue;
		var td3v = tr.childNodes[2].childNodes[0].nodeValue;
		var td4v = tr.childNodes[3].childNodes[0].nodeValue;
		var td5v = tr.childNodes[4].childNodes[0].nodeValue;
		var element = new Array("", 0, "",td1v,td2v,td3v,td4v,td5v);
		listVar.mData.splice(0, 0, element);
	}
	listVar.numRows = listVar.mData.length;
	listVar.render();
}



function getListValue(obj)
{
	var values = "";
	var tmp = obj.id;
	idlistbox = tmp.substring(0,(tmp.length-1));
	var list2 = document.getElementById(idlistbox+'2');
	
	for (i=0;i<list2.length;i++)
	{
		values = values + list2.options[i].value;
		values = values + ";";
	}
	values = values.substring(0,(values.length-1));
	return values;
}