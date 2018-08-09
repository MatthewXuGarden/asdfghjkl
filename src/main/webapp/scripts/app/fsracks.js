function forward()
{
	if (verify_maxracks())
	{
		var form = document.getElementById('formRacksConfigured');
		if(form != null)
		{
			MTstartServerComm();
			document.getElementById('archtabtoload').value = 1;
			form.submit();
		}
	}
}

function save_racks()
{
	if (verify_maxracks())
	{
		var form = document.getElementById('formRacksConfigured');
		if(form != null)
		{
			MTstartServerComm();
			form.submit();
		}
	}
}

function verify_maxracks()
{
	var max_racks = Number(document.getElementById('maxNumRacks').value);
	var num_racks = Number(document.getElementById('numRacks').value);
	if (num_racks > max_racks)
	{
		var conta_racks=0;
		for (i = 0; i < num_racks; i++)
		{
			if (document.getElementById('rack_n'+i).checked)
			{
				conta_racks++;
			}
		}
		if (conta_racks > max_racks)
		{
			alert(document.getElementById("overlimit").value + max_racks); 
			return false;
		}
	}
	return true;
}

function init_t3()
{
	if(document.getElementById("noracks"))
	{
		disableAction(1);
	}
	else
	{
		enableAction(1);
	}
}