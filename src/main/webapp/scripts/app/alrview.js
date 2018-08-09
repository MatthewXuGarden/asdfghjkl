function setActionButton()
{
	//allarme non visionato attivo tasto ack
	var state = document.getElementById("state").value;
	var endalarm = document.getElementById("returned").value;
	if (state=="blank")
	{
		disableAction(1);
		disableAction(2);
		disableAction(3);
	}
	else if (state=="ack")
	{
		enableAction(1);
		disableAction(2);
//		disableAction(3);
		enableAction(3);
	}
	else if (state=="cancel")
	{
		disableAction(1);
		enableAction(2);
//		disableAction(3);
		enableAction(3);
	}
	else if (state=="reset")
	{
		disableAction(1);
		disableAction(2);
		enableAction(3);
	}
	if(endalarm!="")
	{
		disableAction(2);
		disableAction(3);
	}
}


function alr_ack()
{
	if (confirm(document.getElementById("ackconfirm").value))
	{
		document.getElementById("cmd").value="ack";
		var ofrm = document.getElementById("frm_alrview");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
	else return true;
}

function alr_del()
{
	if (confirm(document.getElementById("deleteconfirm").value))
	{
		document.getElementById("cmd").value="cancel";
		var ofrm = document.getElementById("frm_alrview");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
	}
	else return true;	
}

function alr_res()
{
	if (confirm(document.getElementById("resetconfirm").value))
	{
		document.getElementById("cmd").value="reset";
		var ofrm = document.getElementById("frm_alrview");
		if(ofrm != null)
			MTstartServerComm();
		ofrm.submit();
		disableAction(3);
	}
	else return true;	
}

