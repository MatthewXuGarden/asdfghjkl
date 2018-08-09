/*
 * Function called after load
 */
function algoProOnLoad()
{
	// Enable Refresh button
	enableAction(1);
}

function algoProRefresh()
{
	try {
		document.getElementById("algocmd").value = "algoProRefresh";
		document.getElementById("frm_algopro").submit();
	}
	catch(e){}
}

function algoAction(id) 
{
	var sMsg = document.getElementById("sConfAct").value;
	if(sMsg == null || sMsg == "")
		sMsg = "Are you sure to confirm action ?";
	
	document.getElementById("algoobj").value = document.getElementById("algoobj"+id).value;
	if(confirm(sMsg))
		document.getElementById("frm_algopro").submit();
}