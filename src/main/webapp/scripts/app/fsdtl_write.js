// new algorithm
var MIN_SB = 3;
var MAX_SB = 10;
var MIN_T = 10;
var MAX_T = 60;


function change_rack()
{
	var idrack = document.getElementById('comborack').value;
	document.getElementById('idrack').value = idrack;
	document.getElementById('cmd_combo').value = "change";
	var form = document.getElementById("frm_fsdtl");
	if (form != null)
	{
		MTstartServerComm();
		form.submit();
	}
}

function save_rack()
{
	var bNewAlg = document.getElementById('new_alg').value == 'true';
	if( bNewAlg ) {
		if( checkRanges1(document.getElementById('Sb'), 3, 10, 'err4') == 1 )
			return;
		if( checkRanges1(document.getElementById('t'), 10, 60, 'err5') == 1 )
			return;
	}
	else {
		if(checkRanges1(document.getElementById('timewinval'), 10, 180, 'err1')==1) return;
		if(checkRanges1(document.getElementById('waittimeval'), 10, 180, 'err2')==1) return;
		if(checkRanges1(document.getElementById('maxofftimeval'), 0, 100, 'err3')==1) return;
		if(document.getElementById('maxoffutilval').value=="")
		{
			 alert(document.getElementById('insertmaxoffutil').value);
			 return false;
		}
	}
	
	
	var num_dc = Number(document.getElementById('numdc').value);
	for (var i = 0; i < num_dc; i++)
	{
		if(checkRanges1(document.getElementById('maxdcid'+i),0,100,'err') == 1)
			return;
	}
	
	var nondef = "***";
			
	if ((nondef != document.getElementById('maxsetpt').value) && (nondef != document.getElementById('minsetpt').value) && (nondef != document.getElementById('gradval').value))
	{
		document.getElementById('cmd').value = "save_all";
	}
	else
	{
		if(bNewAlg)
			alert(document.getElementById('rackoffline_newalg').value);
		else
			alert(document.getElementById('rackoffline').value);
		document.getElementById('cmd').value = "save_utils";
	}
	
	var form = document.getElementById("frm_fsdtl_write");
	if (form != null)
	{
		if(document.getElementById('setAllDC').checked){
			var toSet = confirm(bNewAlg?document.getElementById("setAllDCConfirm_newalg").value:document.getElementById("setAllDCConfirm").value);
			if(toSet){
				var maxdc = Number(document.getElementById('maxDC').value);
				var num_dc = Number(document.getElementById('numdc').value);
				for (i = 0; i < num_dc; i++)
				{
					document.getElementById('maxdcid'+i).disabled = false;
					document.getElementById('maxdcid'+i).value = maxdc;
				}
			}else{
				return;
			}
		}
		MTstartServerComm();
		form.submit();
	}
}

function setAllDCs(obj){
	
	var checked = obj.checked;
	var num_dc = Number(document.getElementById('numdc').value);
	document.getElementById('maxDC').disabled = !checked;
	for (i = 0; i < num_dc; i++)
	{
		document.getElementById('maxdcid'+i).disabled = checked;
	}
	
}
/*
var alerted = false;
function setAllDC()
{
	if(!alerted){
		if(checkRanges1(document.getElementById("maxDC"),0,100,'err')==1)
			return false;
		var maxdc = Number(document.getElementById('maxDC').value);
		var num_dc = Number(document.getElementById('numdc').value);

		for (i = 0; i < num_dc; i++)
		{
			document.getElementById('maxdcid'+i).value = maxdc;
		}
	}
	return true;
}

function setAllDC4KD(e)
{
	if(e.keyCode==13){
		alerted=true;
		if(checkRanges1(document.getElementById("maxDC"),0,100,'err')==1)
			return false;
		var maxdc = Number(document.getElementById('maxDC').value);
		var num_dc = Number(document.getElementById('numdc').value);

		for (i = 0; i < num_dc; i++)
		{
			document.getElementById('maxdcid'+i).value = maxdc;
		}
	}
	return true;
}
*/

function checkRanges(tf, min, max, err)
{
	if(tf.value===undefined || tf.value=="" || Number(tf.value)<min || Number(tf.value)>max)
	{
		alert(document.getElementById(err).value);
		return 1;
	}
	else
	{
		return 0;
	}
}

function checkRanges1(tf, min, max, err)
{
	if(tf.value===undefined || tf.value=="" || Number(tf.value)<min || Number(tf.value)>max)
	{
		alert(document.getElementById(err).value);
		tf.focus();
		tf.select();
		return 1;
	}
	else
	{
		return 0;
	}
}
