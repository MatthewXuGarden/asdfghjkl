function getFileName()
{
	var filename = document.getElementById("namefile").value;
	return filename;
}	

function getFileSelected()
{
	var selected = document.getElementById("fileCombo").options[document.getElementById("fileCombo").selectedIndex];
	var file_sel = selected.innerHTML + "$$" + selected.value;
	return file_sel;
}

function getDevicesSelected()
{
	var table = document.getElementById("deviceTable");
	
	var ids = "";
	for (i=0;i<table.rows.length;i++)
	{
		var check_i = document.getElementById("ch_"+i);
		if (check_i.checked==true)
		{
			ids = ids + document.getElementById("h_"+i).value + ";";
		}
	}
	if (ids!="")
	{
		ids = ids.substring(0,ids.length-1);
	}
	
	closeWindow();
	copy_all(ids);
//	return ids;
}

function sel_all()
{
	var table = document.getElementById("deviceTable");
	var rows = table.rows.length;
	var enable = document.getElementById("sel_all").checked;
	for (i=0;i<rows;i++)
	{
		document.getElementById("ch_"+i).checked = enable ;
	}
}

function no_sel_all()
{
	document.getElementById("sel_all").checked = false;
}