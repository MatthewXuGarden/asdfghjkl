function start_fs()
{
	document.getElementById("cmd").value='start_fs';
	var block_start = document.getElementById("rack_to_set").value;
	var form = document.getElementById("frm_fs");
	if (block_start=="5")
	{
		alert(document.getElementById("verifyconf").value);
	}
	else if (block_start=="10")
	{
		alert(document.getElementById("auxoldconfig").value);
	}
	else
	{
		if(form != null)
				MTstartServerComm();
		form.submit();
	}
}

function stop_fs()
{
	document.getElementById("cmd").value='stop_fs';	
	var form = document.getElementById("frm_fs");
	if(form != null)
			MTstartServerComm();
	form.submit();
}


function rack_onmouseover(id)
{
	document.getElementById("rack"+id).className="table_selected";
	var gauge = eval("document.gauge_"+id);
	gauge.SetVariable('obj.BackgroundColour','0xFFFFFF');
}

function rack_onmouseout(id)
{
	document.getElementById("rack"+id).className="table";
	var gauge = eval("document.gauge_"+id);
	gauge.SetVariable('obj.BackgroundColour','0xEAEAEA');
}

function go_to_detail(idrack,desc_rack)
{
	top.frames['manager'].loadTrx('nop&folder=fsdetail&bo=BFSDetail&type=click&idrack='+idrack+'&desc=fsdetail');
}

function go_to_config()
{
	document.getElementById('archtabtoload').value='2';
	var form = document.getElementById("frm_fs");
	document.getElementById('cmd').value='go_to_conf';
	if(form != null)
		MTstartServerComm();
	form.submit();
}

function init_gauges()
{
	// hack for chrome
	setTimeout("paint_gauges()", 2000);
}

function paint_gauges() {
	//id racks
	var ids_racks = document.getElementById("ids_racks");
	if (ids_racks!=null)
	{
		var ids = ids_racks.value.split(";");
		for (i=0;i<ids.length;i++)
		{
			var current_id = Number(ids[i]);
			var gauge = eval("document.gauge_"+current_id);

			var min = document.getElementById("gmin_"+current_id).value;
			var max =document.getElementById("gmax_"+current_id).value;
			var set = document.getElementById("gset_"+current_id).value;
			
			/*
			gauge.SetVariable('obj.BarColor','Pink');
			gauge.SetVariable('obj.MinScale',min);
			gauge.SetVariable('obj.MaxScale',max);
			gauge.SetVariable('obj.Value',set);*/
			
			gauge.SetVariable('obj.BackgroundColour','0xEAEAEA');
			
			gauge.SetVariable('obj.RampColour','0x008800');
			gauge.SetVariable('obj.LoValue',min);
			gauge.SetVariable('obj.HiValue',max);
			gauge.SetVariable('obj.Value',set);
		}
	}
}
